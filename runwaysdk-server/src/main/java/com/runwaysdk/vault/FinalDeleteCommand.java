/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.vault;

import java.io.File;
import java.io.IOException;

import com.runwaysdk.SystemException;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.util.FileIO;

/**
 * An undoable command which deletes a file.  Due
 * to the fact that the command is undoable the delete
 * occurs at the very end of a transaction.
 *
 * @author Justin Smethie
 */
public class FinalDeleteCommand implements Command
{
  /**
   * The directory of the file being deleted
   */
  private String directory;

  /**
   * Path of the file to delete
   */
  private String filePath;

  public FinalDeleteCommand(String filePath, String directory)
  {
    this.filePath = filePath;
    this.directory = directory;
  }

  public void doIt()
  {
    File file = new File(filePath);

    if (file.exists() && file.isFile())
    {
      try
      {
        FileIO.deleteFile(file);
      }
      catch (IOException e)
      {
        throw new SystemException(e.getMessage());
      }
    }

    //If there are no files in the directory remove the directory from the file system
    File dir = new File(directory);

    if (dir.exists() && dir.isDirectory())
    {
      File[] files = dir.listFiles();

      if (files.length == 0)
      {
        try
        {
          FileIO.deleteFile(dir);
        }
        catch (IOException e)
        {
          throw new SystemException(e.getMessage());
        }
      }
    }
  }

  public String doItString()
  {
    return "Deletes a specifiec file from the file system";
  }

  public boolean isUndoable()
  {
    return false;
  }

  public void undoIt()
  {
    return;
  }

  public String undoItString()
  {
    return "Undoable";
  }

  /**
   * Acts as a finally block in that, regardless of a successful or unsuccessful
   * transaction, this action is performed at the end of the transaction.
   */
  @Override
  public void doFinally(){}

}
