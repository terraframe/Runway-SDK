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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.runwaysdk.query.ListOIterator;
import com.runwaysdk.query.OIterator;

public class FileResource implements ApplicationFileResource
{
  File file;
  
  public FileResource(File file)
  {
    this.file = file;
  }
  
  @Override
  public String getAbsolutePath()
  {
    return this.file.getAbsolutePath();
  }

  @Override
  public InputStream openNewStream()
  {
    try
    {
      return new FileInputStream(this.file);
    }
    catch (FileNotFoundException e)
    {
      throw new ResourceException(e);
    }
  }

  @Override
  public String getName()
  {
    return this.file.getName();
  }

  @Override
  public String getBaseName()
  {
    return FilenameUtils.getBaseName(this.getName());
  }

  @Override
  public String getNameExtension()
  {
    return FilenameUtils.getExtension(this.getName());
  }

  @Override
  public boolean isRemote()
  {
    return false;
  }

  @Override
  public void close()
  {
    if (this.file instanceof CloseableFile)
    {
      ( (CloseableFile) this.file ).close();
    }
  }

  @Override
  public void delete()
  {
    FileUtils.deleteQuietly(this.file);
  }

  @Override
  public CloseableFile openNewFile()
  {
    return new CloseableFile(this.file, false);
  }
  
  @Override
  public File getUnderlyingFile()
  {
    return this.file;
  }

  @Override
  public boolean isDirectory()
  {
    return this.file.isDirectory();
  }

  @Override
  public OIterator<ApplicationTreeResource> getChildren()
  {
    ArrayList<ApplicationTreeResource> children = new ArrayList<ApplicationTreeResource>();
    
    File[] files = new File[0];
    if (file.isDirectory())
      files = this.file.listFiles();
    
    for (File file : files)
    {
      children.add(new FileResource(file));
    }
    
    return new ListOIterator<ApplicationTreeResource>(children);
  }
  
  @Override
  public OIterator<ApplicationFileResource> getChildrenFiles()
  {
    ArrayList<ApplicationFileResource> children = new ArrayList<ApplicationFileResource>();
    
    Iterator<ApplicationTreeResource> it = this.getChildren();
    
    while (it.hasNext())
    {
      children.add((FileResource) it.next());
    }
    
    return new ListOIterator<ApplicationFileResource>(children);
  }

  @Override
  public Optional<ApplicationTreeResource> getParent()
  {
    return Optional.of(new FileResource(this.file.getParentFile()));
  }
  
  @Override
  public Optional<ApplicationFileResource> getParentFile()
  {
    var op = this.getParent();
    
    if (op.isPresent())
      return Optional.of((ApplicationFileResource) op.get());
    else
      return Optional.empty();
  }
  
  @Override
  public Optional<ApplicationTreeResource> getChild(String path)
  {
    File f = new File(this.file.getAbsolutePath() + File.separator + path);
    
    if (f.exists())
      return Optional.of(new FileResource(f));
    else
      return Optional.empty();
  }
  
  @Override
  public Optional<ApplicationFileResource> getChildFile(String path)
  {
    var op = this.getChild(path);
    
    if (op.isPresent())
      return Optional.of((ApplicationFileResource) op.get());
    else
      return Optional.empty();
  }

  @Override
  public boolean exists()
  {
    return this.file.exists();
  }

  @Override
  public boolean hasChildren()
  {
    if (!this.file.isDirectory())
      return false;
    
    return this.file.listFiles().length > 0;
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
  
  /**
   * Applies the given {@code Consumer} function to every child in the entire subtree
   * rooted at this {@code ArchiveFileResource}.
   *
   * @param action the function that will be executed for each child
   */
  @Override
  public void forAllFileChildren(Consumer<ApplicationFileResource> action)
  {
    forAllChildrenHelper(this, action);
  }
  
  /**
   * A private helper that recurses through all children.
   */
  private static void forAllChildrenHelper(ApplicationFileResource resource, Consumer<ApplicationFileResource> action)
  {
    OIterator<ApplicationFileResource> children = resource.getChildrenFiles();
    
    while (children.hasNext())
    {
      ApplicationFileResource child = children.next();
      action.accept(child);
      forAllChildrenHelper(child, action);
    }
  }
}
