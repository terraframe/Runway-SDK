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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.SystemException;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.util.FileIO;

/**
 * @author Justin Smethie
 *
 */
public class DeleteFileCommand implements Command
{
  private static Logger logger = LoggerFactory.getLogger(DeleteFileCommand.class);

  /**
   * The directory of the file being deleted
   */
  private String        directory;

  /**
   * The fully qualified path of the file being deleted
   */
  private String        filePath;

  /**
   * The FileIF corresponding to the file being deleted
   */
  private FileIF        fileIF;

  /**
   * Flag indicating if the Command should notify the file of a change in its
   * content.
   */
  private boolean       notifyFile;

  /**
   * The size of existing bytes in the file
   */
  private long          existingSize;

  public DeleteFileCommand(String path, FileIF fileIF, boolean notifyVault)
  {
    File file = new File(path);

    this.directory = file.getParent();
    this.filePath = file.getAbsolutePath();

    this.fileIF = fileIF;
    this.notifyFile = notifyVault;

    // Store the contents of the file in a temp file incase of an undo
    copyToTemp(file);
  }

  /**
   * Copies the contents of a file in to a temporary file
   *
   * @param file
   *          The file to copy
   */
  private final void copyToTemp(File file)
  {
    try (InputStream inputStream = new FileInputStream(file))
    {
      File tempFile = new File(file.getAbsolutePath() + ".temp");
      existingSize = IOUtils.copy(inputStream, new FileOutputStream(tempFile));

      // Ensure that the temp file is deleted at the end of the transaction
      FinalDeleteCommand command = new FinalDeleteCommand(tempFile.getAbsolutePath(), directory);
      command.doIt();
    }
    catch (IOException e)
    {
      existingSize = 0;
    }
  }

  public void doIt()
  {
    File file = new File(filePath);

    if (file.exists() && file.isFile())
    {
      try
      {
        FileIO.deleteFile(file);

        if (notifyFile)
        {
          fileIF.notify(new SizeEvent(SizeEvent.EventType.SIZE_CHANGE, -existingSize));
        }
      }
      catch (IOException e)
      {
        throw new SystemException(e.getMessage());
      }
    }

    // If there are no files in the directory remove the directory from the file
    // system
    File dir = new File(directory);

    if (dir.exists() && dir.isDirectory())
    {
      File[] files = dir.listFiles();

      if (files != null && files.length == 0)
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
    return true;
  }

  public void undoIt()
  {
    File prev = new File(filePath + ".temp");

    if (prev.exists())
    {
      // Create the directory structure
      File dir = new File(directory);
      if (!dir.mkdirs())
      {
        logger.debug("Unable to create directory: " + dir.getAbsolutePath());
      }

      File file = new File(filePath);

      // Write the existing content back into the file
      try
      {
        InputStream input = new FileInputStream(prev);
        FileIO.write(new FileOutputStream(file), input);

        if (notifyFile)
        {
          fileIF.notify(new SizeEvent(SizeEvent.EventType.SIZE_CHANGE, existingSize));
        }
      }
      catch (FileNotFoundException e)
      {
        throw new VaultException(e);
      }
      catch (IOException e)
      {
        throw new VaultException(e);
      }
    }
  }

  public String undoItString()
  {
    return "Method is undoable";
  }

  /**
   * Acts as a finally block in that, regardless of a successful or unsuccessful
   * transaction, this action is performed at the end of the transaction.
   */
  @Override
  public void doFinally()
  {
  }

}
