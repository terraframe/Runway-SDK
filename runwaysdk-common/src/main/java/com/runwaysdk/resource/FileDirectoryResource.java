package com.runwaysdk.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class FileDirectoryResource extends FileResource implements ApplicationCollectionResource
{

  public FileDirectoryResource(File file)
  {
    super(file);
  }

  @Override
  public Collection<ApplicationResource> getContents()
  {
    ArrayList<ApplicationResource> contents = new ArrayList<ApplicationResource>();
    
    Iterator<ApplicationTreeResource> it = this.getChildren();
    
    while(it.hasNext())
    {
      contents.add((it.next()));
    }
    
    return contents;
  }

}
