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
import com.runwaysdk.business.generation.dto.ComponentDTOGenerator;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.util.FileIO;


public class JavaArtifactMdTypeCommand implements Command
{
  /**
   * Specifies the operation under which the artifact was created
   */
  protected Operation  operation;

  /**
   * Fully qualified path and file name of the business base source file.
   */
  protected String     businessBaseSourceFile;

  /**
   * Fully qualified path and file name of the business base class file.
   */
  protected File     businessBaseClassDiretory;

  /**
   * Fully qualified path and file name of the dto base source file.
   */
  protected String     dtoBaseSourceFile;

  /**
   * Fully qualified path and file name of the dto class file.
   */
  protected File       dtoBaseClassDirectory;

  /**
   * Type metadata.
   */
  private MdTypeDAOIF   mdTypeIF;

  protected MdTypeDAOIF getMdTypeIF()
  {
    return this.mdTypeIF;
  }

  protected Connection conn;

  public enum Operation
  {
    /**
     * The Java artifact is created when an MdType was created.
     */
    CREATE,

    /**
     * The Java artifact is updated when an MdType was updated.
     */
    UPDATE,

    /**
     * The Java artifact is deleted when an MdType was deleted.
     */
    DELETE,

    /**
     * The Java artifact is cleaned up for a new build.
     */
    CLEAN;
  }

  public JavaArtifactMdTypeCommand(MdTypeDAOIF mdTypeIF, Operation operation, Connection conn)
  {
    super();
    this.mdTypeIF       = mdTypeIF;
    this.operation      = operation;
    this.conn           = conn;

    this.businessBaseClassDiretory  = TypeGenerator.getBaseClassDirectory(mdTypeIF);
    this.businessBaseSourceFile = TypeGenerator.getBaseSrcFilePath(mdTypeIF);
    this.dtoBaseClassDirectory = TypeGenerator.getDTObaseClassDirectory(mdTypeIF);
    this.dtoBaseSourceFile = TypeGenerator.getDTObaseSrcFilePath(mdTypeIF);
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

    if (this.operation == Operation.DELETE ||
        this.operation == Operation.CLEAN)
    {
      this.deleteMdTypeFiles();
    }
    else
    {
      // These casts are OK, as the object itself is not modified.
      if (((MdTypeDAO)mdTypeIF).isImport())
      {
        ((MdTypeDAO)mdTypeIF).writeJavaToFile();
      }
      else
      {
        GenerationManager.generate(mdTypeIF);
      }
    }
  }

  /**
   * Executes the undo in this Command, and closes the connection.
   * Must be called after the database transaciton has been rolled back.
   */
  public void undoIt()
  {
    if (this.getMdTypeIF().isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    {
      return;
    }

    if (this.operation == Operation.DELETE ||
        this.operation == Operation.CLEAN)
    {
      // If it is not new, then we need to restore the previous files.
      if (!mdTypeIF.isNew())
      {
        // Clear existing and possible defunct files.
        this.deleteMdTypeFiles();
        //restore the base class file from the database.
        this.restoreBaseFiles();
      }
      // If it is not new, then there is nothing we need to do, as there is nothing to restore.
    }
    else if (this.operation == Operation.UPDATE)
    {
      // Clear existing and possible defunct files.
      this.deleteMdTypeFiles();
      // Restore baseClassFile from the database.
      this.restoreBaseFiles();
    }
    // CREATE
    // Delete the files that were created during this transaction.
    else
    {
      this.deleteMdTypeFiles();
    }
  }

  /**
   * Acts as a finally block in that, regardless of a successful or unsuccessful
   * transaction, this action is performed at the end of the transaction.
   */
  @Override
  public void doFinally(){}

  /**
   * Returns a human readable string describing what this command
   * is trying to do.
   * @return human readable string describing what this command
   * is trying to do.
   */
  public String doItString()
  {
    if (this.operation == Operation.DELETE ||
        this.operation == Operation.CLEAN)
    {
      return "Restore file: "+businessBaseClassDiretory;
    }
    else if (this.operation == Operation.CREATE)
    {
      return "Delete file: "+businessBaseSourceFile+"\n"+
             "Delete file: "+businessBaseClassDiretory;
    }
    else
    {
      return "Restore file: "+businessBaseClassDiretory;
    }
  }

  /**
   * Returns a human readable string describing what this command
   * needs to do in order to undo itself.
   * @return human readable string describing what this command
   * needs to do in order to undo itself.
   */
  public String undoItString()
  {
    if (this.operation == Operation.DELETE ||
        this.operation == Operation.CLEAN)
    {
      return "Restore file: "+businessBaseSourceFile;
    }
    else if (this.operation == Operation.CREATE)
    {
      return "Delete file: "+businessBaseSourceFile;
    }
    else
    {
      return "Restore file: "+businessBaseSourceFile;
    }
  }

  /**
   * Pulls the base source and class files from the database and writes them to
   * the filesystem.
   */
  private void restoreBaseFiles()
  {
    try
    {
      byte[] baseClass = Database.getMdTypeBaseClass(this.mdTypeIF.getId(), conn);
      ClassManager.writeClasses(businessBaseClassDiretory, baseClass);

      String baseSource = Database.getMdTypeBaseSource(this.mdTypeIF.getId(), conn);
      FileIO.write(businessBaseSourceFile, baseSource);

      byte[] dtoClass = Database.getMdTypeDTOclass(this.mdTypeIF.getId(), conn);
      ClassManager.writeClasses(dtoBaseClassDirectory, dtoClass);

      String dtoSource = Database.getMdTypeDTOsource(this.mdTypeIF.getId(), conn);
      FileIO.write(dtoBaseSourceFile, dtoSource);
    }
    catch(IOException e)
    {
      throw new SystemException(e);
    }
  }

  /**
   * Deletes the class file and the source file from the filesystem, if they exist.
   *
   */
  protected void deleteMdTypeFiles()
  {
    // Do not delete hardcoded types, as there are depencencies to these classes.
    if (GenerationUtil.isHardcodedType(this.getMdTypeIF()))
    {
      return;
    }

    try
    {
      File baseSourceFile = new File(this.businessBaseSourceFile);
      FileIO.deleteFile(baseSourceFile);

      String baseClassType = mdTypeIF.getTypeName() + TypeGeneratorInfo.BASE_SUFFIX;
      ClassManager.deleteClasses(businessBaseClassDiretory, baseClassType);

      this.deleteDTOBaseSource();

      this.deleteDTOBaseClasses();
    }
    catch (IOException e)
    {
      throw new SystemException(e.getMessage());
    }
  }

  /**
   * Indicates if this Command deletes something.
   * @return <code><b>true</b></code> if this Command deletes something.
   */
  public boolean isUndoable()
  {
    return true;
  }

  protected void deleteDTOBaseSource()
  {
    try
    {
      File dtoBaseSourceFile = new File(this.dtoBaseSourceFile);
      FileIO.deleteFile(dtoBaseSourceFile);
    }
    catch (IOException e)
    {
      throw new SystemException(e.getMessage());
    }
  }

  protected void deleteDTOBaseClasses()
  {
    String dtoBaseClassType = mdTypeIF.getTypeName() + ComponentDTOGenerator.DTO_BASE_SUFFIX ;
    ClassManager.deleteClasses(dtoBaseClassDirectory, dtoBaseClassType);
  }

}

