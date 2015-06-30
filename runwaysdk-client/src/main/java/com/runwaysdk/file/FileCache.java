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
package com.runwaysdk.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.locks.ReentrantLock;

import com.runwaysdk.constants.ClientProperties;
import com.runwaysdk.util.FileIO;

/**
 * A singleton that puts files into a cache on the application server
 * 
 * @author Justin
 *
 */
public class FileCache
{
  /**
   * Singleton instance of the cache
   */
  private static FileCache    cache            = null;
      
  /**
   * Gards to ensure that invariants between mulitple state fields hold.
   */
  private final ReentrantLock fileCacheLock;

  private FileCache()
  {
    fileCacheLock = new ReentrantLock();
  }
  
  public synchronized void setFile(String dir, String fileName, InputStream stream) throws IOException
  {
    fileCacheLock.lock();
    try
    {
      String root = ClientProperties.getFileCacheDirectory();
      String location = root + dir + fileName;
            
      File file = new File(location);
      
      if(!file.exists())
      {
        file.getParentFile().mkdirs();
        file.createNewFile();
      }      
      
      file.deleteOnExit();
            
      FileIO.write(new FileOutputStream(file), stream);
    }
    finally
    {
      fileCacheLock.unlock();
    }
  }

  public synchronized void deleteFile(String dir, String fileName)
  {
    fileCacheLock.lock();
    try
    {
      String root = ClientProperties.getFileCacheDirectory();
      String location = root + dir + fileName;   
      
      File file = new File(location);
      
      if(file.exists() && file.isFile())
      {
        file.delete();
      }
    }
    finally
    {
      fileCacheLock.unlock();
    }
  }

  public InputStream getFile(String dir, String fileName) throws IOException
  {
    fileCacheLock.lock();
    try
    {
      String root = ClientProperties.getFileCacheDirectory();
      String location = root + dir + fileName;      
      
      File file = new File(location);

      if(file.exists()  && file.isFile())
      {
        return new FileInputStream(file);
      }
      else
      {
        String msg = "The file [" + fileName + "] is not cached.";
        throw new IOException(msg);
      }
    }
    finally
    {
      fileCacheLock.unlock();
    }
  }

  public void clearCache()
  {
    String root = ClientProperties.getFileCacheDirectory();
    
    File rootFile = new File(root);
    
    deleteDirectory(rootFile);
  }

  private void deleteDirectory(File directory)
  {
    File[] files = directory.listFiles();
    
    if(files == null)
    {
      return;
    }
    
    for(File file : files)
    {
      if(file.isDirectory())
      {
        deleteDirectory(file);
      }
      else
      {
        file.delete();
      }
    }
  }

  public synchronized static FileCache instance()
  {
    if (cache == null)
    {
      cache = new FileCache();
    }

    return cache;
  }
}
