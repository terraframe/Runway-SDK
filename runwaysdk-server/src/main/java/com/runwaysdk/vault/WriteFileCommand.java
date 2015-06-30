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
package com.runwaysdk.vault;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.runwaysdk.SystemException;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.util.FileIO;

public class WriteFileCommand implements Command
{
  /**
   * The directory of the file being written
   */
  private String      directory;

  /**
   * The fully qualified path of the file being written
   */
  private String      path;

  /**
   * The number of bytes in 'existing'
   */
  private long        existingSize = 0;

  private long        size         = 0;

  /**
   * The FileIF corresponding to the file being written
   */
  private FileIF      fileIF;

  private InputStream stream;

  public WriteFileCommand(String directory, String path, InputStream stream, FileIF fileIF)
  {
    this.directory = directory;
    this.path = path;
    this.stream = stream;
    this.fileIF = fileIF;
    this.existingSize = 0;

    File file = new File(path);

    // Get the file if one already exist
    if (file.exists())
    {
      this.copyToTemp(file);
    }
  }

  /**
   * Copies the contents of a file in to a temporary file
   *
   * @param file The file to copy
   */
  private final void copyToTemp(File file)
  {
    try
    {
      InputStream inputStream = new FileInputStream(file);
      File tempFile = new File(file.getAbsolutePath() + ".temp");

      existingSize = FileIO.write(new FileOutputStream(tempFile), inputStream);

      // Ensure that the temp file is deleted at the end of the transaction
      FinalDeleteCommand command = new FinalDeleteCommand(tempFile.getAbsolutePath(), directory);
      command.doIt();
    }
    catch (FileNotFoundException e)
    {
      existingSize = 0;
    }
    catch (IOException e)
    {
      existingSize = 0;
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see com.runwaysdk.dataaccess.Command#doIt()
   */
  public void doIt()
  {
    File dir = new File(directory);

    // Create the directory structure if needed
    if (!dir.exists())
    {
      dir.mkdirs();
    }

    File file = new File(path);

    // Write the bytes to the file
    try
    {
      size = FileIO.write(new FileOutputStream(file), stream);
      fileIF.notify(new SizeEvent(SizeEvent.EventType.SIZE_CHANGE, size - existingSize));
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

  /*
   * (non-Javadoc)
   *
   * @see com.runwaysdk.dataaccess.Command#doItString()
   */
  public String doItString()
  {
    return "Creates the directory and file in the file system";
  }

  /*
   * (non-Javadoc)
   *
   * @see com.runwaysdk.dataaccess.Command#isUndoable()
   */
  public boolean isUndoable()
  {
    return true;
  }

  /*
   * (non-Javadoc)
   *
   * @see com.runwaysdk.dataaccess.Command#undoIt()
   */
  public void undoIt()
  {
    // Get the backup file
    File file = new File(path);
    File prev = new File(file.getAbsolutePath() + ".temp");

    if (prev.exists())
    {
      // Write the existing content back into the file
      try
      {
        InputStream input = new FileInputStream(prev);
        FileIO.write(new FileOutputStream(file), input);

        fileIF.notify(new SizeEvent(SizeEvent.EventType.SIZE_CHANGE, existingSize - size));
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
    else
    {
      // If the file did not exist previously
      // then delete the file from the file system
      if (file.exists() && file.isFile())
      {
        try
        {
          // Get the size of the file that was going to be written
          FileIO.deleteFile(file);
          fileIF.notify(new SizeEvent(SizeEvent.EventType.SIZE_CHANGE, -size));
        }
        catch (IOException e)
        {
          throw new SystemException(e.getMessage());
        }
      }

      File dir = new File(directory);

      if (dir.exists())
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

  /**
   * Acts as a finally block in that, regardless of a successful or unsuccessful
   * transaction, this action is performed at the end of the transaction.
   */
  @Override
  public void doFinally(){}

  public long getSize()
  {
    return size;
  }

  /*
   * (non-Javadoc)
   *
   * @see com.runwaysdk.dataaccess.Command#undoItString()
   */
  public String undoItString()
  {
    return "Reverts to the existing file or if the file was created then deletes the file that was added to the file system and attempts to remove the created directories";
  }

}
