/**
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
 */
package com.runwaysdk.business.generation;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;

import com.runwaysdk.SystemException;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.dataaccess.MdViewDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.util.FileIO;

public class JavaArtifactMdViewCommand extends JavaArtifactMdTransientCommand
{
  private String     queryBaseSourceFile;
  private File       queryBaseClassDirectory;

  private String     queryStubSourceFile;
  private File       queryStubClassDirectory;

  private String     queryDTOsourceFile;
  private File       queryDTOclassDirectory;

  public JavaArtifactMdViewCommand(MdViewDAOIF mdViewIF, Operation operation, Connection conn)
  {
    super(mdViewIF, operation, conn);

    this.queryBaseSourceFile          = TypeGenerator.getBaseQueryAPIsourceFilePath(this.getMdTypeIF());
    this.queryBaseClassDirectory      = TypeGenerator.getQueryAPIclassDirectory(this.getMdTypeIF());

    this.queryStubSourceFile          = TypeGenerator.getStubQueryAPIsourceFilePath(this.getMdTypeIF());
    this.queryStubClassDirectory      = TypeGenerator.getQueryAPIclassDirectory(this.getMdTypeIF());

    this.queryDTOsourceFile           = TypeGenerator.getQueryDTOsourceFilePath(this.getMdTypeIF());
    this.queryDTOclassDirectory       = TypeGenerator.getQueryDTOclassDirectory(this.getMdTypeIF());
  }

  protected MdViewDAOIF getMdTypeIF()
  {
    return (MdViewDAOIF)super.getMdTypeIF();
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
      this.deleteMdViewFiles();
    }
    else
    {
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
        this.deleteMdViewFiles();

        // restore the query source file from the database.
        this.restoreToFileSystem_QueryNonStubSourceFile();

        if (LocalProperties.isDeployedInContainer())
        {
          this.restoreToFileSystem_QueryStubSourceFile();
        }
        // restore the query class files from the database.
        this.restoreToFileSystem_QueryClassFiles();
      }
      // If it is not new, then there is nothing we need to do, as there is nothing to restore.
    }
    else if (this.operation == Operation.UPDATE)
    {
      // Clear existing and possible defunct files.
      this.deleteMdViewFiles();

      this.restoreToFileSystem_QueryNonStubSourceFile();
      // The supertype, JavaArtifactMdTypeCommand will take crea of restoring base source
      // and class files. Since we don't want to clobber custom code in stub classes, we
      // take no action here.
      if (LocalProperties.isDeployedInContainer())
      {
        // restore the query source file from the database.
        this.restoreToFileSystem_QueryStubSourceFile();
      }

      // restore the query class files from the database.
      this.restoreToFileSystem_QueryClassFiles();

    }
    // CREATE
    // Delete the files that were created during this transaction.
    else
    {
      this.deleteMdViewFiles();
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
      return returnString + "\nDelete file: " + queryBaseSourceFile + "\n" +
             "Delete file: " + queryStubSourceFile + "\n" +
             "Delete file: " + queryDTOsourceFile + "\n" +
             "Delete file: " + queryBaseClassDirectory;
    }
    else if (this.operation == Operation.CREATE)
    {
      return returnString+"\nCreate file: " + queryBaseSourceFile + "\n" +
             "Create file: " + queryStubSourceFile + "\n" +
             "Create file: " + queryBaseClassDirectory + "\n" +
             "Create file: " + queryDTOsourceFile;
    }
    else
    {
      return returnString+"\nRestore file: " + queryBaseSourceFile + "\n" +
             "Restore file: " + queryStubSourceFile + "\n" +
             "Restore file: " + queryBaseClassDirectory + "\n" +
             "Restore file: " + queryDTOsourceFile;
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
      return returnString + "Restore file: " + queryBaseSourceFile + "\n" +
      "Restore file: " + queryStubSourceFile + "\n" +
      "Restore file: " + queryDTOsourceFile + "\n" +
      "Restore file: " + queryBaseClassDirectory;
    }
    else if (this.operation == Operation.CREATE)
    {
      return returnString+"Delete file: " + queryBaseSourceFile + "\n" +
      "Delete file: " + queryStubSourceFile + "\n" +
      "Delete file: " + queryDTOsourceFile + "\n" +
      "Delete file: " + queryBaseClassDirectory;
    }
    else
    {
      return returnString+"Restore file: " + queryBaseSourceFile + "\n" +
      "Restore file: " + queryStubSourceFile + "\n" +
      "Restore file: " + queryDTOsourceFile + "\n" +
      "Restore file: " + queryBaseClassDirectory;
    }
  }

  /**
   * Restores from the database to the filesystem the query source file.
   */
  private void restoreToFileSystem_QueryNonStubSourceFile()
  {
    String queryBaseSource = Database.getMdViewBaseQuerySource(this.getMdTypeIF().getId(), conn);
    String queryDTOsource = Database.getViewQueryDTOsource(this.getMdTypeIF().getId(), conn);

    try
    {
      FileIO.write(this.queryBaseSourceFile, queryBaseSource);
      FileIO.write(this.queryDTOsourceFile, queryDTOsource);
    }
    catch(IOException e)
    {
      throw new SystemException(e);
    }
  }

  /**
   * Restores from the database to the filesystem the query source file.
   */
  private void restoreToFileSystem_QueryStubSourceFile()
  {
    String queryStubSource = Database.getMdViewStubQuerySource(this.getMdTypeIF().getId(), conn);
    try
    {
      FileIO.write(this.queryStubSourceFile, queryStubSource);
    }
    catch(IOException e)
    {
      throw new SystemException(e);
    }
  }

  /**
   * Restores from the database to the filesystem the query class files.
   */
  private void restoreToFileSystem_QueryClassFiles()
  {
    byte[] queryBaseClassBytes = Database.getMdViewBaseQueryClass(this.getMdTypeIF().getId(), conn);
    ClassManager.writeClasses(this.queryBaseClassDirectory, queryBaseClassBytes);

    byte[] queryStubClassBytes = Database.getMdViewStubQueryClass(this.getMdTypeIF().getId(), conn);
    ClassManager.writeClasses(this.queryStubClassDirectory, queryStubClassBytes);

    byte[] queryDTOclassBytes = Database.getViewQueryDTOclass(this.getMdTypeIF().getId(), conn);
    ClassManager.writeClasses(this.queryDTOclassDirectory, queryDTOclassBytes);
  }

  /**
   * Deletes the class file and the source file from the filesystem, if they exist.
   */
  private void deleteMdViewFiles()
  {
    // Do not delete hardcoded types, as there are dependencies to these classes.
    if (GenerationUtil.isHardcodedType(this.getMdTypeIF()))
    {
      return;
    }

    try
    {
      File fileBaseQuerySourceFile = new File(this.queryBaseSourceFile);
      FileIO.deleteFile(fileBaseQuerySourceFile);

      String queryBaseClassName = ViewQueryBaseAPIGenerator.getQueryBaseClassName(this.getMdTypeIF());
      ClassManager.deleteClasses(this.queryBaseClassDirectory, queryBaseClassName);

      // Do not delete the business sub source file for CLEAN
      if (this.operation != Operation.CLEAN)
      {
        if (this.operation == Operation.DELETE ||
            (this.operation == Operation.CREATE && !LocalProperties.isDevelopEnvironment()))
        {
          File fileStubQuerySourceFile = new File(this.queryStubSourceFile);
          FileIO.deleteFile(fileStubQuerySourceFile);
        }
      }

      String queryStubClassName = ViewQueryStubAPIGenerator.getQueryStubClassName(this.getMdTypeIF());
      ClassManager.deleteClasses(this.queryStubClassDirectory, queryStubClassName);

      this.deleteQueryDTOsource();
      this.deleteQueryDTOclasses();

    }
    catch (IOException e)
    {
      throw new SystemException(e.getMessage());
    }
  }

  private void deleteQueryDTOsource()
  {
    try
    {
      File fileDTOQuerySourceFile = new File(this.queryDTOsourceFile);
      FileIO.deleteFile(fileDTOQuerySourceFile);
    }
    catch (IOException e)
    {
      throw new SystemException(e.getMessage());
    }
  }

  protected void deleteQueryDTOclasses()
  {
    String queryDTOclassName = ViewQueryBaseAPIGenerator.getQueryBaseClassName(this.getMdTypeIF());
    ClassManager.deleteClasses(this.queryDTOclassDirectory, queryDTOclassName);
  }
}
