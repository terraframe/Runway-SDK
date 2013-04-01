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
package com.runwaysdk.vault;

import java.io.File;
import java.io.IOException;

import com.runwaysdk.SystemException;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.util.FileIO;


public class VaultCommand implements Command
{
  private String directory;

  public VaultCommand(String directory)
  {
    this.directory = directory;
  }

  public void doIt()
  {
    File dir = new File(directory);

    deleteDirectory(dir);
  }

  /**
   * Recursivley deletes all the files and sub directories of a given directory
   * @param directory The directory
   */
  private static void deleteDirectory(File directory)
  {
    if(directory.isDirectory())
    {
      File[] files = directory.listFiles();

      for(File file : files)
      {
        deleteDirectory(file);
      }
    }

    try
    {
      FileIO.deleteFile(directory);
    }
    catch (IOException e)
    {
      throw new SystemException(e.getMessage());
    }
  }

  public String doItString()
  {
    return "Deletes all VaultFiles of the vault, removes the vault directory and subdirectories from the file system";
  }

  public boolean isUndoable()
  {
    return false;
  }

  public void undoIt()
  {
  }

  public String undoItString()
  {
    return "Add all deleted VaultFiles back into the vault, recreated the vault directory structure in the file system";
  }

  /**
   * Acts as a finally block in that, regardless of a successful or unsuccessful
   * transaction, this action is performed at the end of the transaction.
   */
  @Override
  public void doFinally(){}
}
