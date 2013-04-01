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
package com.runwaysdk.business.generation.facade;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import com.runwaysdk.SystemException;
import com.runwaysdk.business.generation.ClassManager;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.JavaArtifactMdTypeCommand;
import com.runwaysdk.business.generation.TypeGenerator;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdFacadeDAO;
import com.runwaysdk.facade.wsdd.WebServiceDeployer;
import com.runwaysdk.util.FileIO;

public class JavaArtifactMdFacadeCommand extends JavaArtifactMdTypeCommand implements Command
{
  private String        stubSourceFile;
  private File          stubClassDirectory;

  private List<String>  generatedServerSourceFiles;
  private String        generatedServerSourceDirectoryPath;
  private File          generatedServerClassesDirectory;

  private List<String>  generatedCommonSourceFiles;
  private String        generatedCommonSourceDirectoryPath;
  private File          generatedCommonClassesDirectory;

  private List<String>  generatedClientSourceFiles;
  private String        generatedClientSourceDirectoryPath;
  private File          generatedClientClassesDirectory;

  public JavaArtifactMdFacadeCommand(MdFacadeDAOIF mdFacadeIF, Operation operation, Connection conn)
  {
    super(mdFacadeIF, operation, conn);

    this.stubSourceFile                     = TypeGenerator.getJavaSrcFilePath(mdFacadeIF);
    this.stubClassDirectory                 = TypeGenerator.getStubClassDirectory(mdFacadeIF);

    this.generatedServerSourceFiles         = MdFacadeDAO.generatedServerSourceFiles(mdFacadeIF);
    this.generatedServerSourceDirectoryPath = TypeGenerator.getGeneratedServerSourceDirectory(mdFacadeIF);
    this.generatedServerClassesDirectory    = TypeGenerator.getGeneratedServerClassesDirectory(mdFacadeIF);

    this.generatedCommonSourceFiles         = MdFacadeDAO.generatedCommonSourceFiles(mdFacadeIF);
    this.generatedCommonSourceDirectoryPath = TypeGenerator.getGeneratedCommonSourceDirectory(mdFacadeIF);
    this.generatedCommonClassesDirectory    = TypeGenerator.getGeneratedCommonClassesDirectory(mdFacadeIF);

    this.generatedClientSourceFiles         = MdFacadeDAO.generatedClientSourceFiles(mdFacadeIF);
    this.generatedClientSourceDirectoryPath = TypeGenerator.getGeneratedClientSourceDirectory(mdFacadeIF);
    this.generatedClientClassesDirectory    = TypeGenerator.getGeneratedClientClassesDirectory(mdFacadeIF);
  }


  protected MdFacadeDAOIF getMdTypeIF()
  {
    return (MdFacadeDAOIF)super.getMdTypeIF();
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

    if (this.operation == Operation.DELETE || this.operation == Operation.CLEAN)
    {
    	this.deleteMdFacadeFiles();
    }
    else
    {
        // do nothing. the file was already created
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
        this.deleteMdFacadeFiles();
        //restore the stub class file from the database.
        this.restoreToFileSystem_StubClassFile();

        if (LocalProperties.isDeployedInContainer())
        {
          //restore the stub source file from the database.
          this.restoreToFileSystem_StubSourceFile();
        }

        //restore the generated server classes from the database.
        this.restoreToFileSystem_ServerClassesFile();
        //restore the generated common classes from the database.
        this.restoreToFileSystem_CommonClassesFile();
        //restore the generated Client classes from the database.
        this.restoreToFileSystem_ClientClassesFile();

        // redeploy the web services
	  	WebServiceDeployer serviceDeployer = new WebServiceDeployer();
	  	serviceDeployer.addService((MdFacadeDAOIF)this.getMdTypeIF());
	  	serviceDeployer.deploy();

      }
      // If it is not new, then there is nothing we need to do, as there is nothing to restore.
    }
    else if (this.operation == Operation.UPDATE)
    {
      // Clear existing and possible defunct files.
      this.deleteMdFacadeFiles();

      if (LocalProperties.isDeployedInContainer())
      {
        //restore the stub source file from the database.
        this.restoreToFileSystem_StubSourceFile();
      }

      //restore the stub class file from the database.
      this.restoreToFileSystem_StubClassFile();
      //restore the generated server classes from the database.
      this.restoreToFileSystem_ServerClassesFile();
      //restore the generated common classes from the database.
      this.restoreToFileSystem_CommonClassesFile();
      //restore the generated Client classes from the database.
      this.restoreToFileSystem_ClientClassesFile();

      boolean enabled = CommonProperties.getContainerWebServiceEnabled();

      if(enabled)
      {
        // redeploy the web services
        WebServiceDeployer serviceDeployer = new WebServiceDeployer();
        serviceDeployer.addService((MdFacadeDAOIF)this.getMdTypeIF());
        serviceDeployer.deploy();
      }
    }
    // CREATE
    // Delete the files that were created during this transaction.
    else
    {
      this.deleteMdFacadeFiles();
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
    String returnString = super.doItString();

    if (this.operation == Operation.DELETE ||
        this.operation == Operation.CLEAN)
    {
      return returnString + "\nDelete file: " + stubSourceFile + "\n" +
             "Delete file: " + stubClassDirectory + "\n" +
             "Delete files: " + this.generatedServerSourceFiles + "\n" +
             "Delete files: " + this.generatedCommonSourceFiles + "\n" +
             "Delete files: " + this.generatedClientSourceFiles;
    }
    else if (this.operation == Operation.CREATE)
    {
      return returnString+"\nCreate file: "+stubSourceFile + "\n" +
             "Create file: "+stubClassDirectory + "\n" +
             "Create files: " + this.generatedServerSourceFiles + "\n" +
             "Create files: " + this.generatedCommonSourceFiles + "\n" +
             "Create files: " + this.generatedClientSourceFiles;
    }
    else
    {
      return returnString+"\nRestore file: "+stubSourceFile+"\n"+
             "Restore file: " + stubClassDirectory + "\n" +
             "Restore files: " + this.generatedServerSourceFiles + "\n" +
             "Restore files: " + this.generatedCommonSourceFiles + "\n" +
             "Restore files: " + this.generatedClientSourceFiles;
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
      return returnString + "\nRestore file: " + stubSourceFile + "\n" +
      "Restore file: " + stubClassDirectory + "\n" +
      "Restore files: " + this.generatedServerSourceFiles + "\n" +
      "Restore files: " + this.generatedCommonSourceFiles + "\n" +
      "Restore files: " + this.generatedClientSourceFiles;
    }
    else if (this.operation == Operation.CREATE)
    {
      return returnString+"\nDelete file: "+stubSourceFile + "\n" +
      "Delete file: "+stubClassDirectory + "\n" +
      "Delete files: " + this.generatedServerSourceFiles + "\n" +
      "Delete files: " + this.generatedCommonSourceFiles + "\n" +
      "Delete files: " + this.generatedClientSourceFiles;
    }
    else
    {
      return returnString+"\nRestore file: "+stubSourceFile+"\n"+
      "Restore file: " + stubClassDirectory + "\n" +
      "Restore files: " + this.generatedServerSourceFiles + "\n" +
      "Restore files: " + this.generatedCommonSourceFiles + "\n" +
      "Restore files: " + this.generatedClientSourceFiles;
    }
  }

  /**
   * Restores from the database to the filesystem the stub class file.
   */
  private void restoreToFileSystem_StubClassFile()
  {
    byte[] stubClassBytes = Database.getMdFacadeStubClass(this.getMdTypeIF().getId(), conn);
    ClassManager.writeClasses(this.stubClassDirectory, stubClassBytes);
  }

  /**
   * Restores from the database to the filesystem the stub source file.
   */
  private void restoreToFileSystem_StubSourceFile()
  {
    String stubSource = Database.getMdFacadeStubSource(this.getMdTypeIF().getId(), conn);
    try
    {
      FileIO.write(this.stubSourceFile, stubSource);
    }
    catch(IOException e)
    {
      throw new SystemException(e);
    }
  }

  /**
   * Restores from the database to the filesystem the generated server classes.
   */
  private void restoreToFileSystem_ServerClassesFile()
  {
    byte[] serverClassesBytes = Database.getMdFacadeServerClasses(this.getMdTypeIF().getId(), conn);
    ClassManager.writeClasses(this.generatedServerClassesDirectory, serverClassesBytes);
  }

  /**
   * Restores from the database to the filesystem the generated common classes.
   */
  private void restoreToFileSystem_CommonClassesFile()
  {
    byte[] commonClassesBytes = Database.getMdFacadeCommonClasses(this.getMdTypeIF().getId(), conn);
    ClassManager.writeClasses(this.generatedCommonClassesDirectory, commonClassesBytes);
  }

  /**
   * Restores from the database to the filesystem the generated client classes.
   */
  private void restoreToFileSystem_ClientClassesFile()
  {
    byte[] clientClassesBytes = Database.getMdFacadeClientClasses(this.getMdTypeIF().getId(), conn);
    ClassManager.writeClasses(this.generatedClientClassesDirectory, clientClassesBytes);
  }


  /**
   * Deletes the class files and the source files from the filesystem, if they exist.
   */
  private void deleteMdFacadeFiles()
  {
    // Do not delete hardcoded types, as there are depencencies to these classes.
    if (GenerationUtil.isHardcodedType(this.getMdTypeIF()))
    {
      return;
    }

    try
    {
      if (this.operation != Operation.CLEAN)
      {
        // Do not delete the stub source file if we are performing a CLEAN
        if (this.operation == Operation.DELETE ||
            (this.operation == Operation.CREATE && !LocalProperties.isDevelopEnvironment()))
        {
          File fileStubSourceFile = new File(this.stubSourceFile);
          FileIO.deleteFile(fileStubSourceFile);
        }
      }

      String stubClassName = this.getMdTypeIF().getTypeName();
      ClassManager.deleteClasses(this.stubClassDirectory, stubClassName);

      // Generated server source files
      for (String fileName : this.generatedServerSourceFiles)
      {
        FileIO.deleteFile(new File(this.generatedServerSourceDirectoryPath+fileName));
      }
      ClassManager.deleteGeneratedServerClasses(this.generatedServerClassesDirectory, (MdFacadeDAOIF)this.getMdTypeIF());

      // Generated common source files
      for (String fileName : this.generatedCommonSourceFiles)
      {
        FileIO.deleteFile(new File(this.generatedCommonSourceDirectoryPath+fileName));
      }
      ClassManager.deleteGeneratedCommonClasses(this.generatedCommonClassesDirectory, (MdFacadeDAOIF)this.getMdTypeIF());

      // Generated client source files
      for (String fileName : this.generatedClientSourceFiles)
      {
        FileIO.deleteFile(new File(this.generatedClientSourceDirectoryPath+fileName));
      }
      ClassManager.deleteGeneratedClientClasses(this.generatedClientClassesDirectory, (MdFacadeDAOIF)this.getMdTypeIF());
    }
    catch (IOException e)
    {
      throw new SystemException(e.getMessage());
    }
  }
}

