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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

import com.runwaysdk.query.ListOIterator;
import com.runwaysdk.query.OIterator;

public class ArchiveFileResource extends ArchiveResource implements ApplicationFileResource
{

  public ArchiveFileResource(ApplicationFileResource archive)
  {
    super(archive);
  }
  
  public ApplicationFileResource getFileArchive()
  {
    return (ApplicationFileResource) super.archive;
  }

  @Override
  public OIterator<ApplicationFileResource> getChildrenFiles()
  {
    ArrayList<ApplicationFileResource> result = new ArrayList<ApplicationFileResource>();
    
    for (File file : extractAndListFiles())
    {
      result.add(new FileResource(file));
    }
    
    return new ListOIterator<ApplicationFileResource>(result);
  }

  @Override
  public Optional<ApplicationFileResource> getChildFile(String path)
  {
    for (File file : extractAndListFiles())
    {
      if (file.getName().equals(path))
        return Optional.of(new FileResource(file));
    }
    
    return Optional.empty();
  }

  @Override
  public File getUnderlyingFile()
  {
    return getFileArchive().getUnderlyingFile();
  }

  @Override
  public boolean isDirectory()
  {
    return getFileArchive().isDirectory();
  }

  @Override
  public Optional<ApplicationFileResource> getParentFile()
  {
    return getFileArchive().getParentFile();
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
