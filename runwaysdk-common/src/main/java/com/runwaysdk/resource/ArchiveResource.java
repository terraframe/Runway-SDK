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
package com.runwaysdk.resource;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FileUtils;

import com.runwaysdk.query.ListOIterator;
import com.runwaysdk.query.OIterator;

public class ArchiveResource implements ApplicationCollectionResource, ApplicationTreeResource
{
  
  public static final int      BUFFER_SIZE     = 1024;
  
  protected ApplicationResource archive;
  
  protected CloseableFile extractedParent = null;

  public ArchiveResource(ApplicationResource archive)
  {
    this.archive = archive;
  }
  
  public ApplicationResource getArchive()
  {
    return this.archive;
  }
  
  public CloseableFile extract() throws ResourceException
  {
    if (extractedParent != null)
      return extractedParent;
    
    try
    {
      extractedParent = new CloseableFile(Files.createTempDirectory(this.archive.getBaseName()).toFile());
      
      try (CloseableFile fArchive = archive.openNewFile())
      {
        String extension = archive.getNameExtension();

        if (extension.equalsIgnoreCase("zip"))
        {
          unzip(fArchive);
        }
        else if (extension.equalsIgnoreCase("gz"))
        {
          untar(fArchive);
        }
      }
    }
    catch(IOException ex)
    {
      throw new ResourceException(ex);
    }
    
    return extractedParent;
  }
  
  protected void untar(File fArchive) throws IOException
  {
    byte data[] = new byte[BUFFER_SIZE];

    try (GzipCompressorInputStream gzipIn = new GzipCompressorInputStream(archive.openNewStream()))
    {
      try (TarArchiveInputStream tarIn = new TarArchiveInputStream(gzipIn))
      {
        TarArchiveEntry entry;

        while ( ( entry = (TarArchiveEntry) tarIn.getNextEntry() ) != null)
        {
          /** If the entry is a directory, create the directory. **/
          String filename = entry.getName();
          if (entry.isDirectory())
          {
            File f = new File(filename);
            boolean created = f.mkdir();
            if (!created)
            {
              System.out.printf("Unable to create directory '%s', during extraction of archive contents.\n", f.getAbsolutePath());
            }
          }
          else
          {
            var extracted = new File(extractedParent, entry.getName());
            
            try (FileOutputStream fos = new FileOutputStream(extracted))
            {
              int count;

              try (BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER_SIZE))
              {
                while ( ( count = tarIn.read(data, 0, BUFFER_SIZE) ) != -1)
                {
                  dest.write(data, 0, count);
                }
              }
            }
          }
        }
      }
    }
  }
  
  protected void unzip(File fArchive) throws IOException
  {
    byte[] buffer = new byte[BUFFER_SIZE];
    
//    try (ZipFile zipFile = ZipFile.builder().setFile(fArchive).get())
    try (ZipFile zipFile = new ZipFile(fArchive))
    {
      Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();

      while (entries.hasMoreElements())
      {
        ZipArchiveEntry entry = entries.nextElement();
        
        var extracted = new File(extractedParent, entry.getName());

        try (FileOutputStream fos = new FileOutputStream(extracted))
        {
          try (InputStream zis = zipFile.getInputStream(entry))
          {
            int len;
            while ( ( len = zis.read(buffer) ) > 0)
            {
              fos.write(buffer, 0, len);
            }
          }
        }
      }
    }
  }

  @Override
  public String getAbsolutePath()
  {
    return archive.getAbsolutePath();
  }

  @Override
  public InputStream openNewStream()
  {
    return archive.openNewStream();
  }

  @Override
  public CloseableFile openNewFile()
  {
    return archive.openNewFile();
  }

  @Override
  public String getName()
  {
    return archive.getName();
  }

  @Override
  public String getBaseName()
  {
    return archive.getBaseName();
  }

  @Override
  public String getNameExtension()
  {
    return archive.getNameExtension();
  }

  @Override
  public boolean isRemote()
  {
    return archive.isRemote();
  }

  @Override
  public boolean exists()
  {
    return archive.exists();
  }

  @Override
  public void close()
  {
    archive.close();
    
    FileUtils.deleteQuietly(extractedParent);
    extractedParent = null;
  }

  @Override
  public void delete()
  {
    archive.delete();
    
    FileUtils.deleteQuietly(extractedParent);
    extractedParent = null;
  }

  @Override
  public Collection<ApplicationResource> getContents()
  {
    File extracted = extract();
    
    ArrayList<ApplicationResource> result = new ArrayList<ApplicationResource>();
    
    for (File file : extracted.listFiles())
    {
      result.add(new FileResource(file));
    }
    
    return result;
  }
  
  public List<ApplicationTreeResource> getTreeContents()
  {
    File extracted = extract();
    
    List<ApplicationTreeResource> result = new ArrayList<ApplicationTreeResource>();
    
    for (File file : extracted.listFiles())
    {
      result.add(new FileResource(file));
    }
    
    return result;
  }

  @Override
  public OIterator<ApplicationTreeResource> getChildren()
  {
    return new ListOIterator<ApplicationTreeResource>(getTreeContents());
  }

  @Override
  public Optional<ApplicationTreeResource> getChild(String path)
  {
    File extracted = extract();
    
    for (File file : extracted.listFiles())
    {
      if (file.getName().equals(path))
        return Optional.of(new FileResource(file));
    }
    
    return Optional.empty();
  }

  @Override
  public Optional<ApplicationTreeResource> getParent()
  {
    return Optional.empty();
  }

  @Override
  public boolean hasChildren()
  {
    File extracted = extract();
    
    return extracted.listFiles().length > 0;
  }
  
  /**
   * Applies the given {@code Consumer} function to every child in the entire subtree
   * rooted at this {@code ArchiveFileResource}.
   *
   * @param action the function that will be executed for each child
   */
  @Override
  public void forAllChildren(Consumer<ApplicationTreeResource> action)
  {
    forAllChildrenHelper(this, action);
  }
  
  /**
   * A private helper that recurses through all children.
   */
  private static void forAllChildrenHelper(ApplicationTreeResource resource, Consumer<ApplicationTreeResource> action)
  {
    OIterator<ApplicationTreeResource> children = resource.getChildren();
    
    while (children.hasNext())
    {
      ApplicationTreeResource child = children.next();
      action.accept(child);
      forAllChildrenHelper(child, action);
    }
  }
  
}