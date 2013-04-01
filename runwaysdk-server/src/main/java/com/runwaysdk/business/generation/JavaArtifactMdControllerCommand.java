/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.business.generation;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;

import com.runwaysdk.SystemException;
import com.runwaysdk.business.generation.facade.ControllerGenerator;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.MdControllerDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.util.FileIO;

public class JavaArtifactMdControllerCommand implements Command
{
  public enum Operation {
    /**
     * The Java artifact is created when an MdTypeDAO was created.
     */
    CREATE,

    /**
     * The Java artifact is updated when an MdTypeDAO was updated.
     */
    UPDATE,

    /**
     * The Java artifact is deleted when an MdTypeDAO was deleted.
     */
    DELETE,

    /**
     * The Java artifact is cleaned up for a new build.
     */
    CLEAN;
  }

  /**
   * Type metadata.
   */
  private MdControllerDAOIF mdController;

  protected Connection      conn;

  protected Operation       operation;

  public JavaArtifactMdControllerCommand(MdControllerDAOIF mdController, Operation operation,
      Connection conn)
  {
    this.mdController = mdController;
    this.conn = conn;
    this.operation = operation;
  }

  protected MdControllerDAOIF getMdTypeIF()
  {
    return this.mdController;
  }

  /**
   * Executes the statement in this Command.
   */
  public void doIt()
  {
    if (this.getMdTypeIF().isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    {
      return;
    }

    if (this.operation == Operation.DELETE)
    {
      this.deleteFiles();
    }
    else if (this.operation == Operation.CLEAN)
    {
      this.cleanFiles();
    }
    else
    {
      // These casts are OK, as the object itself is not modified.
      if ( ( (MdTypeDAO) this.getMdTypeIF() ).isImport())
      {
        ( (MdTypeDAO) this.getMdTypeIF() ).writeJavaToFile();
      }
      else
      {
        GenerationManager.generate(this.getMdTypeIF());
      }
    }
  }

  /**
   * Executes the undo in this Command, and closes the connection. Must be
   * called after the database transaciton has been rolled back.
   */
  public void undoIt()
  {
    if (this.operation == Operation.DELETE || this.operation == Operation.CLEAN)
    {
      // If it is not new, then we need to restore the previous files.
      if (!this.getMdTypeIF().isNew())
      {
         this.deleteFiles();

        if (LocalProperties.isDeployedInContainer())
        {
          // restore the base class file from the database.
          this.restoreFiles();
        }
      }
      // If it is not new, then there is nothing we need to do, as there is
      // nothing to restore.
    }
    else if (this.operation == Operation.UPDATE)
    {
       this.deleteFiles();

      // Restore baseClassFile from the database.
      if (LocalProperties.isDeployedInContainer())
      {
        this.restoreFiles();
      }
    }
    // CREATE
    // Delete the files that were created during this transaction.
    else
    {
       this.deleteFiles();
    }
  }

  /**
   * Returns a human readable string describing what this command is trying to
   * do.
   *
   * @return human readable string describing what this command is trying to do.
   */
  public String doItString()
  {
    if (this.operation == Operation.CREATE)
    {
      StringBuffer buffer = new StringBuffer();

      for (GeneratorIF gen : this.getMdTypeIF().getGenerators())
      {
        ControllerGenerator generator = (ControllerGenerator) gen;

        buffer.append("Delete file: " + generator.getPath() + "\n");
        buffer.append("Delete file: " + generator.getClassFile() + "\n");
      }

      return buffer.toString();
    }
    else
    {
      StringBuffer buffer = new StringBuffer();

      for (GeneratorIF gen : this.getMdTypeIF().getGenerators())
      {
        ControllerGenerator generator = (ControllerGenerator) gen;

        buffer.append("Restore file: " + generator.getClassFile() + "\n");
      }

      return buffer.toString();
    }
  }

  /**
   * Returns a human readable string describing what this command needs to do in
   * order to undo itself.
   *
   * @return human readable string describing what this command needs to do in
   *         order to undo itself.
   */
  public String undoItString()
  {
    return "blah";
  }

  /**
   * Acts as a finally block in that, regardless of a successful or unsuccessful
   * transaction, this action is performed at the end of the transaction.
   */
  @Override
  public void doFinally(){}

  /**
   * Pulls the base source and class files from the database and writes them to
   * the filesystem.
   */
  private void restoreFiles()
  {
    try
    {
      for (GeneratorIF gen : this.getMdTypeIF().getGenerators())
      {
        ControllerGenerator generator = (ControllerGenerator) gen;

        if (LocalProperties.isDeployedInContainer() || !(generator instanceof StubMarker ))
        {
          byte[] bytes = Database.getBlobAsBytes(this.getMdTypeIF(), generator.getClassAttribute(), conn);
          String source = Database.getSource(this.getMdTypeIF(), generator.getSourceAttribute(), conn);

          FileIO.write(new File(generator.getClassFile()), bytes);
          ClassManager.writeClasses(new File(generator.getClassFile()).getParentFile(), bytes);
          FileIO.write(new File(generator.getPath()), source);
        }
      }
    }
    catch (IOException e)
    {
      throw new SystemException(e.getMessage());
    }
  }

  /**
   * Deletes the class file and the source file from the filesystem, if they
   * exist.
   *
   */
  protected void deleteFiles()
  {
    // Do not delete hardcoded types, as there are depencencies to these
    // classes.
    if (GenerationUtil.isHardcodedType(this.getMdTypeIF()))
    {
      return;
    }

    try
    {
      for (GeneratorIF gen : this.getMdTypeIF().getGenerators())
      {
        ControllerGenerator generator = (ControllerGenerator) gen;

//        // Do not delete the business sub source file for CLEAN
//        if (this.operation != Operation.CLEAN)
//        {
//          if (this.operation == Operation.DELETE ||
//             (this.operation == Operation.CREATE && LocalProperties.isDeployEnviroment()))
//          {
//            File fileStubSourceFile = new File(this.businessStubSourceFile);
//            FileIO.deleteFile(fileStubSourceFile);
//          }
//        }

        boolean deleteArtifacts = true;

        if ((generator instanceof StubMarker ))
        {
          boolean isDelete = this.operation == Operation.DELETE;
          boolean isCreateInDevelop = this.operation == Operation.CREATE && !LocalProperties.isDevelopEnvironment();

          deleteArtifacts = (isDelete || isCreateInDevelop);
        }

        if (deleteArtifacts)
        {
          String path = generator.getPath();

          FileIO.deleteFile(new File(path));
        }

        // Always delete the class file(s).
        String className = generator.getFileName();
        String classFile = generator.getClassFile();

        File parentFile = new File(classFile).getParentFile();

        ClassManager.deleteClasses(parentFile, className);
      }
    }
    catch (IOException e)
    {
      throw new SystemException(e.getMessage());
    }
  }

  /**
   * Deletes the class file and the source file from the filesystem, if they
   * exist.
   *
   */
  protected void cleanFiles()
  {
    try
    {
      for (GeneratorIF gen : this.getMdTypeIF().getGenerators())
      {
        ControllerGenerator generator = (ControllerGenerator) gen;

        // Do not delete stub files on cleans
        if (! ( generator instanceof StubMarker ))
        {
          FileIO.deleteFile(new File(generator.getPath()));
          String stubClassName = this.getMdTypeIF().getTypeName();
          ClassManager.deleteClasses(new File(generator.getClassFile()).getParentFile(), stubClassName);
        }
      }
    }
    catch (IOException e)
    {
      throw new SystemException(e.getMessage());
    }
  }

  /**
   * Indicates if this Command deletes something.
   *
   * @return <code><b>true</b></code> if this Command deletes something.
   */
  public boolean isUndoable()
  {
    return true;
  }

}
