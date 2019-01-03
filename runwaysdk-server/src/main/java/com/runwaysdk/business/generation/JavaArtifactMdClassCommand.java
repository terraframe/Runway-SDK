/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
 */
package com.runwaysdk.business.generation;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;

import com.runwaysdk.SystemException;
import com.runwaysdk.business.generation.dto.ComponentDTOGenerator;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.util.FileIO;



public class JavaArtifactMdClassCommand extends JavaArtifactMdTypeCommand
{
  private String     businessStubSourceFile;
  private File       businessStubClassDirectory;
  private String     dtoStubSourceFile;
  private File       dtoStubClassDirectory;

  private MdClassDAOIF  mdClassIF;

  public JavaArtifactMdClassCommand(MdClassDAOIF mdClassIF, Operation operation, Connection conn)
  {
    super(mdClassIF, operation, conn);

    this.businessStubSourceFile           = TypeGenerator.getJavaSrcFilePath(mdClassIF);
    this.businessStubClassDirectory       = TypeGenerator.getStubClassDirectory(mdClassIF);
    this.dtoStubSourceFile                = TypeGenerator.getDTOstubSrcFilePath(mdClassIF);
    this.dtoStubClassDirectory            = TypeGenerator.getDTOstubClassDirectory(mdClassIF);

    this.mdClassIF = mdClassIF;
  }

  protected MdClassDAOIF getMdTypeIF()
  {
    return (MdClassDAOIF)super.getMdTypeIF();
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

    super.doIt();

    if (this.operation == Operation.DELETE ||
        this.operation == Operation.CLEAN)
    {
      this.deleteMdClassFiles();
    }
    else
    {
      // Make sure the DTO base and stub files are gone if this class is not published.
      if (!this.mdClassIF.isPublished())
      {
        this.deleteDTOBaseClasses();
        this.deleteDTOBaseSource();
        this.deleteDTOStubClasses();
        this.deleteDTOStubSource();
      }
      // do nothing.  file was already created.
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

    super.undoIt();

    if (this.operation == Operation.DELETE ||
        this.operation == Operation.CLEAN)
    {
      // If it is not new, then we need to restore the previous files.
      if (!this.getMdTypeIF().isNew())
      {
        // Clear existing and possible defunct files.
        this.deleteMdClassFiles();

        if (LocalProperties.isDeployedInContainer())
        {
          //restore the stub source file from the database.
          this.restoreToFileSystem_StubSourceFile();
          //restore the dto stub source file from the database.
          this.restoreToFileSystem_DTOStubSourceFile();
        }

        //restore the stub class file from the database.
        this.restoreToFileSystem_StubClassFile();
        //restore the dto stub class file from the database.
        this.restoreToFileSystem_DTOStubClassFile();
      }
      // If it is not new, then there is nothing we need to do, as there is nothing to restore.
    }
    else if (this.operation == Operation.UPDATE)
    {
      // Clear existing and possible defunct files.
      this.deleteMdClassFiles();

      if (LocalProperties.isDeployedInContainer())
      {
        //restore the stub source file from the database.
        this.restoreToFileSystem_StubSourceFile();
        //restore the dto stub source file from the database.
        this.restoreToFileSystem_DTOStubSourceFile();
      }

      //restore the stub class file from the database.
      this.restoreToFileSystem_StubClassFile();
      //restore the dto stub class file from the database.
      this.restoreToFileSystem_DTOStubClassFile();
    }
    // CREATE
    // Delete the files that were created during this transaction.
    else
    {
      this.deleteMdClassFiles();
    }
  }


  /**
   * Returns a human readable string describing what this command
   * is trying to do.
   * @return human readable string describing what this command
   * is trying to do.
   */
  public String doItString()
  {
    String returnString = super.doItString();

    if (this.operation == Operation.DELETE ||
        this.operation == Operation.CLEAN)
    {
      return returnString + "\nDelete file: " + businessStubSourceFile + "\n" +
             "Delete file: " + businessStubClassDirectory + "\n" +
             "Delete file: " + dtoStubSourceFile + "\n" +
             "Delete file: " + dtoStubClassDirectory;
    }
    else if (this.operation == Operation.CREATE)
    {
      return returnString+"\nCreate file: "+businessStubSourceFile + "\n" +
             "Create file: "+businessStubClassDirectory + "\n" +
             "Create file: " +dtoStubSourceFile + "\n" +
             "Create file: " +dtoStubClassDirectory;
    }
    else
    {
      return returnString+"\nRestore file: "+businessStubSourceFile+"\n"+
             "Restore file: " + businessStubClassDirectory + "\n" +
             "Restore file: " + dtoStubSourceFile + "\n" +
             "Restore file: " + dtoStubClassDirectory;
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
    String returnString = super.undoItString();

    if (this.operation == Operation.DELETE ||
        this.operation == Operation.CLEAN)
    {
      return returnString + "\nRestore file: " + businessStubSourceFile + "\n" +
      "Restore file: " + businessStubClassDirectory + "\n" +
      "Restore file: " + dtoStubSourceFile + "\n" +
      "Restore file: " + dtoStubClassDirectory;
    }
    else if (this.operation == Operation.CREATE)
    {
      return returnString+"\nDelete file: "+businessStubSourceFile + "\n" +
      "Delete file: "+businessStubClassDirectory + "\n" +
      "Delete file: " +dtoStubSourceFile + "\n" +
      "Delete file: " +dtoStubClassDirectory;
    }
    else
    {
      return returnString+"\nRestore file: "+businessStubSourceFile+"\n"+
      "Restore file: " + businessStubClassDirectory + "\n" +
      "Restore file: " +dtoStubSourceFile + "\n" +
      "Restore file: " +dtoStubClassDirectory;
    }
  }

  /**
   * Restores from the database to the filesystem the stub class file.
   */
  private void restoreToFileSystem_StubClassFile()
  {
    byte[] stubClassBytes = Database.getMdClassStubClass(this.getMdTypeIF().getOid(), conn);
    ClassManager.writeClasses(businessStubClassDirectory, stubClassBytes);
  }

  /**
   * Restores from the database to the filesystem the stub source file.
   */
  private void restoreToFileSystem_StubSourceFile()
  {
    String stubSource = Database.getMdClassStubSource(this.getMdTypeIF().getOid(), conn);
    try
    {
      FileIO.write(businessStubSourceFile, stubSource);
    }
    catch(IOException e)
    {
      throw new SystemException(e);
    }
  }

  /**
   * Restores from the database to the filesystem the dto stub class file.
   */
  private void restoreToFileSystem_DTOStubClassFile()
  {
    byte[] dtoStubClassBytes = Database.getMdClassDTOStubClass(this.getMdTypeIF().getOid(), conn);
    ClassManager.writeClasses(dtoStubClassDirectory, dtoStubClassBytes);
  }

  /**
   * Restores from the database to the filesystem the dto stub source file.
   */
  private void restoreToFileSystem_DTOStubSourceFile()
  {
    String stubSource = Database.getMdClassDTOStubSource(this.getMdTypeIF().getOid(), conn);
    try
    {
      FileIO.write(dtoStubSourceFile, stubSource);
    }
    catch(IOException e)
    {
      throw new SystemException(e);
    }
  }


  /**
   * Deletes the class file and the source file from the filesystem, if they exist.
   */
  private void deleteMdClassFiles()
  {
    // Do not delete hardcoded types, as there are depencencies to these classes.
    if (GenerationUtil.isHardcodedType(this.getMdTypeIF()))
    {
      return;
    }

    try
    {
      // Do not delete the business sub source file for CLEAN
      if (this.operation != Operation.CLEAN)
      {
        if (this.operation == Operation.DELETE ||
            (this.operation == Operation.CREATE && !LocalProperties.isDevelopEnvironment()))
        {
          File fileStubSourceFile = new File(this.businessStubSourceFile);
          FileIO.deleteFile(fileStubSourceFile);
        }
      }

      String stubClassName = this.getMdTypeIF().getTypeName();
      ClassManager.deleteClasses(this.businessStubClassDirectory, stubClassName);

      // Do not delete the dto sub source file if we are deleting this class, or a clean
      // is performed on a class that is not published.
      boolean deleteDTOStubSource = false;
      if (this.operation == Operation.CLEAN)
      {
        if (!this.mdClassIF.isPublished())
        {
          deleteDTOStubSource = true;
        }
      }
      else
      {
        if (this.operation == Operation.DELETE ||
           (this.operation == Operation.CREATE && !LocalProperties.isDevelopEnvironment()))
        {
          deleteDTOStubSource = true;
        }
      }

      if (deleteDTOStubSource)
      {
        this.deleteDTOStubSource();
      }

      this.deleteDTOStubClasses();

    }
    catch (IOException e)
    {
      throw new SystemException(e.getMessage());
    }
  }


  private void deleteDTOStubSource()
  {
    try
    {
      File dtoStubSourceFile = new File(this.dtoStubSourceFile);
      FileIO.deleteFile(dtoStubSourceFile);
    }
    catch (IOException e)
    {
      throw new SystemException(e.getMessage());
    }
  }

  protected void deleteDTOStubClasses()
  {
    String dtoStubClassName = this.getMdTypeIF().getTypeName() + ComponentDTOGenerator.DTO_SUFFIX;
    ClassManager.deleteClasses(this.dtoStubClassDirectory, dtoStubClassName);
  }

}
