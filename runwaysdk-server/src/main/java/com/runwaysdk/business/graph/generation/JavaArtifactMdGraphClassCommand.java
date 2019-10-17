/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.business.graph.generation;

import java.io.File;
import java.io.IOException;

import com.runwaysdk.SystemException;
import com.runwaysdk.business.generation.ClassManager;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.JavaArtifactMdTypeCommand.Operation;
import com.runwaysdk.business.generation.TypeGenerator;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.util.FileIO;

public class JavaArtifactMdGraphClassCommand implements Command
{
  /**
   * Specifies the operation under which the artifact was created
   */
  protected Operation       operation;

  /**
   * Fully qualified path and file name of the business base source file.
   */
  protected String          baseSourceFile;

  /**
   * Fully qualified path and file name of the business base class file.
   */
  protected File            baseClassDirectory;

  private String            stubSourceFile;

  private File              stubClassDirectory;

  private MdGraphClassDAOIF mdClassIF;

  public JavaArtifactMdGraphClassCommand(MdGraphClassDAOIF mdClassIF, Operation operation)
  {
    this.mdClassIF = mdClassIF;
    this.operation = operation;

    this.baseClassDirectory = TypeGenerator.getBaseClassDirectory(mdClassIF);
    this.baseSourceFile = TypeGenerator.getBaseSrcFilePath(mdClassIF);

    this.stubSourceFile = TypeGenerator.getJavaSrcFilePath(mdClassIF);
    this.stubClassDirectory = TypeGenerator.getStubClassDirectory(mdClassIF);
  }

  protected MdGraphClassDAOIF getMdTypeIF()
  {
    return (MdGraphClassDAOIF) this.mdClassIF;
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

    if (this.operation == Operation.DELETE || this.operation == Operation.CLEAN)
    {
      this.deleteMdClassFiles();
    }
  }

  /**
   * Executes the undo in this Command, and closes the connection.
   */
  public void undoIt()
  {
    if (this.getMdTypeIF().isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    {
      return;
    }

    if (this.operation == Operation.DELETE || this.operation == Operation.CLEAN)
    {
      // If it is not new, then we need to restore the previous files.
      if (!this.getMdTypeIF().isNew())
      {
        // Clear existing and possible defunct files.
        this.deleteMdClassFiles();

        if (LocalProperties.isDeployedInContainer())
        {
          // restore the stub source file from the database.
          this.restoreToFileSystem_StubSourceFile();
        }

        // restore the stub class file from the database.
        this.restoreToFileSystem_StubClassFile();
      }
      // If it is not new, then there is nothing we need to do, as there is
      // nothing to restore.
    }
    else if (this.operation == Operation.UPDATE)
    {
      // Clear existing and possible defunct files.
      this.deleteMdClassFiles();

      if (LocalProperties.isDeployedInContainer())
      {
        // restore the stub source file from the database.
        this.restoreToFileSystem_StubSourceFile();
      }

      // restore the stub class file from the database.
      this.restoreToFileSystem_StubClassFile();
    }
    // CREATE
    // Delete the files that were created during this transaction.
    else
    {
      this.deleteMdClassFiles();
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
    StringBuilder returnString = new StringBuilder();

    if (this.operation == Operation.DELETE || this.operation == Operation.CLEAN)
    {
      returnString.append("Delete file: " + baseSourceFile + "\n");
      returnString.append("Delete file: " + baseClassDirectory + "\n");
      returnString.append("Delete file: " + stubSourceFile + "\n");
      returnString.append("Delete file: " + stubClassDirectory + "\n");
    }
    else if (this.operation == Operation.CREATE)
    {
      returnString.append("Create file: " + baseSourceFile + "\n");
      returnString.append("Create file: " + baseClassDirectory + "\n");
      returnString.append("Create file: " + stubSourceFile + "\n");
      returnString.append("Create file: " + stubClassDirectory + "\n");
    }
    else
    {
      returnString.append("Restore file: " + baseSourceFile + "\n");
      returnString.append("Restore file: " + baseClassDirectory + "\n");
      returnString.append("Restore file: " + stubSourceFile + "\n");
      returnString.append("Restore file: " + stubClassDirectory + "\n");
    }

    return returnString.toString();
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
    StringBuilder returnString = new StringBuilder();

    if (this.operation == Operation.DELETE || this.operation == Operation.CLEAN)
    {
      returnString.append("Restore file: " + baseSourceFile + "\n");
      returnString.append("Restore file: " + baseClassDirectory + "\n");
      returnString.append("Restore file: " + stubSourceFile + "\n");
      returnString.append("Restore file: " + stubClassDirectory + "\n");
    }
    else if (this.operation == Operation.CREATE)
    {
      returnString.append("Delete file: " + baseSourceFile + "\n");
      returnString.append("Delete file: " + baseClassDirectory + "\n");
      returnString.append("Delete file: " + stubSourceFile + "\n");
      returnString.append("Delete file: " + stubClassDirectory + "\n");
    }

    return returnString.toString();
  }

  /**
   * Restores from the database to the filesystem the stub class file.
   */
  private void restoreToFileSystem_StubClassFile()
  {
    // byte[] stubClassBytes =
    // Database.getMdClassStubClass(this.getMdTypeIF().getOid(), conn);
    // ClassManager.writeClasses(stubClassDirectory, stubClassBytes);
  }

  /**
   * Restores from the database to the filesystem the stub source file.
   */
  private void restoreToFileSystem_StubSourceFile()
  {
    // String stubSource =
    // Database.getMdClassStubSource(this.getMdTypeIF().getOid(), conn);
    // try
    // {
    // FileIO.write(stubSourceFile, stubSource);
    // }
    // catch (IOException e)
    // {
    // throw new SystemException(e);
    // }
  }

  /**
   * Deletes the class file and the source file from the filesystem, if they
   * exist.
   */
  private void deleteMdClassFiles()
  {
    // Do not delete hardcoded types, as there are depencencies to these
    // classes.
    if (GenerationUtil.isHardcodedType(this.getMdTypeIF()))
    {
      return;
    }

    try
    {
      // Do not delete the business sub source file for CLEAN
      if (this.operation != Operation.CLEAN)
      {
        if (this.operation == Operation.DELETE || ( this.operation == Operation.CREATE && !LocalProperties.isDevelopEnvironment() ))
        {
          FileIO.deleteFile(new File(this.baseSourceFile));

          FileIO.deleteFile(new File(this.stubSourceFile));
        }
      }

      String baseClassType = this.getMdTypeIF().getTypeName() + TypeGeneratorInfo.BASE_SUFFIX;
      ClassManager.deleteClasses(baseClassDirectory, baseClassType);

      String stubClassName = this.getMdTypeIF().getTypeName();

      ClassManager.deleteClasses(this.stubClassDirectory, stubClassName);

    }
    catch (IOException e)
    {
      throw new SystemException(e.getMessage());
    }
  }

  /*
   * Indicates if this Command deletes something.
   * 
   * @return <code><b>true</b></code> if this Command deletes something.
   */
  public boolean isUndoable()
  {
    return true;
  }

  @Override
  public void doFinally()
  {
  }

}
