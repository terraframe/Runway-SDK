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
import com.runwaysdk.business.generation.dto.ComponentQueryDTOGenerator;
import com.runwaysdk.business.generation.view.ContentProviderIF;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.util.FileIO;

public class JavaArtifactMdEntityCommand extends JavaArtifactMdClassCommand
{
  private String            queryAPIsourceFile;

  private File              queryAPIclassDirectory;

  private String            queryDTOsourceFile;

  private File              queryDTOclassDirectory;

  private ContentProviderIF provider = null;

  public JavaArtifactMdEntityCommand(MdClassDAOIF mdCompopnentIF, Operation operation, Connection conn)
  {
    super(mdCompopnentIF, operation, conn);

    this.queryAPIsourceFile = TypeGenerator.getQueryAPIsourceFilePath(this.getMdTypeIF());
    this.queryAPIclassDirectory = TypeGenerator.getQueryAPIclassDirectory(this.getMdTypeIF());

    this.queryDTOsourceFile = TypeGenerator.getQueryDTOsourceFilePath(this.getMdTypeIF());
    this.queryDTOclassDirectory = TypeGenerator.getQueryDTOclassDirectory(this.getMdTypeIF());
  }

  protected MdEntityDAOIF getMdTypeIF()
  {
    return (MdEntityDAOIF) super.getMdTypeIF();
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
      this.deleteMdEntityFiles();
    }
    else
    {
      MdEntityDAOIF mdEntity = (MdEntityDAOIF) this.getMdTypeIF();

      // Make sure the DTO base and stub files are gone if this class is not
      // published.
      if (!mdEntity.isPublished())
      {
        this.deleteQueryDTOsource();
        this.deleteQueryDTOclasses();
      }

      // do nothing. file was already created.
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

    if (this.operation == Operation.DELETE || this.operation == Operation.CLEAN)
    {
      // If it is not new, then we need to restore the previous files.
      if (!this.getMdTypeIF().isNew())
      {
        // restore the query source file from the database.
        this.restoreToFileSystem_QuerySourceFile();
        // restore the query class files from the database.
        this.restoreToFileSystem_QueryClassFiles();

        this.restoreToFileSystem_ViewFiles();
      }
      // If it is not new, then there is nothing we need to do, as there is
      // nothing to restore.
    }
    else if (this.operation == Operation.UPDATE)
    {
      // The supertype, JavaArtifactMdTypeCommand will take crea of restoring
      // base source
      // and class files. Since we don't want to clobber custom code in stub
      // classes, we
      // take no action here.
      // restore the query source file from the database.
      this.restoreToFileSystem_QuerySourceFile();

      // restore the query class files from the database.
      this.restoreToFileSystem_QueryClassFiles();

      this.restoreToFileSystem_ViewFiles();
    }
    // CREATE
    // Delete the files that were created during this transaction.
    else
    {
      this.deleteMdEntityFiles();
    }
  }

  @Override
  public void doFinally()
  {
    if (this.provider != null)
    {
      provider.deleteBackup();
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
    String returnString = super.doItString();

    if (this.operation == Operation.DELETE || this.operation == Operation.CLEAN)
    {
      return returnString + "\nDelete file: " + queryAPIsourceFile + "\n" + "Delete file: " + queryAPIclassDirectory;
    }
    else if (this.operation == Operation.CREATE)
    {
      return returnString + "\nCreate file: " + queryAPIsourceFile + "\n" + "Create file: " + queryAPIclassDirectory;
    }
    else
    {
      return returnString + "\nRestore file: " + queryAPIsourceFile + "\n" + "Restore file: " + queryAPIclassDirectory;
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
    String returnString = super.undoItString();

    if (this.operation == Operation.DELETE || this.operation == Operation.CLEAN)
    {
      return returnString + "\nRestore file: " + queryAPIsourceFile + "\n" + "Restore file: " + queryAPIclassDirectory;
    }
    else if (this.operation == Operation.CREATE)
    {
      return returnString + "\nDelete file: " + queryAPIsourceFile + "\n" + "Delete file: " + queryAPIclassDirectory;
    }
    else
    {
      return returnString + "\nRestore file: " + queryAPIsourceFile + "\n" + "Restore file: " + queryAPIclassDirectory;
    }
  }

  /**
   * Restores from the database to the file system the query source file.
   */
  private void restoreToFileSystem_QuerySourceFile()
  {
    String queryAPIsource = Database.getMdEntityQueryAPIsource(this.getMdTypeIF().getId(), conn);
    String queryDTOsource = Database.getEntityQueryDTOsource(this.getMdTypeIF().getId(), conn);

    try
    {
      FileIO.write(this.queryAPIsourceFile, queryAPIsource);
      FileIO.write(this.queryDTOsourceFile, queryDTOsource);
    }
    catch (IOException e)
    {
      throw new SystemException(e);
    }
  }

  private void restoreToFileSystem_ViewFiles()
  {
    if (this.provider != null)
    {
      provider.reloadContent();
    }
  }

  /**
   * Restores from the database to the file system the query class files.
   */
  private void restoreToFileSystem_QueryClassFiles()
  {
    byte[] queryAPIclassBytes = Database.getMdEntityQueryAPIclass(this.getMdTypeIF().getId(), conn);
    byte[] queryDTOclassBytes = Database.getEntityQueryDTOclass(this.getMdTypeIF().getId(), conn);

    ClassManager.writeClasses(this.queryAPIclassDirectory, queryAPIclassBytes);
    ClassManager.writeClasses(this.queryDTOclassDirectory, queryDTOclassBytes);
  }

  /**
   * Deletes the class file and the source file from the file system, if they
   * exist.
   */
  private void deleteMdEntityFiles()
  {
    // Do not delete hardcoded types, as there are dependencies to these
    // classes.
    if (GenerationUtil.isHardcodedType(this.getMdTypeIF()))
    {
      return;
    }

    try
    {
      File fileQueryAPIsourceFile = new File(this.queryAPIsourceFile);
      FileIO.deleteFile(fileQueryAPIsourceFile);

      this.deleteQueryDTOsource();

      String queryAPIclassName = EntityQueryAPIGenerator.getQueryClassName(this.getMdTypeIF());
      ClassManager.deleteClasses(this.queryAPIclassDirectory, queryAPIclassName);

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
      File fileQueryDTOsourceFile = new File(this.queryDTOsourceFile);
      FileIO.deleteFile(fileQueryDTOsourceFile);
    }
    catch (IOException e)
    {
      throw new SystemException(e.getMessage());
    }
  }

  protected void deleteQueryDTOclasses()
  {
    String queryDTOclassName = ComponentQueryDTOGenerator.getQueryClassName(this.getMdTypeIF());
    ClassManager.deleteClasses(this.queryDTOclassDirectory, queryDTOclassName);
  }
}
