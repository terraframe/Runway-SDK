package com.runwaysdk.resource;

import java.io.File;
import java.util.ArrayList;
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
    File extracted = extract();
    
    ArrayList<ApplicationFileResource> result = new ArrayList<ApplicationFileResource>();
    
    for (File file : extracted.listFiles())
    {
      result.add(new FileResource(file));
    }
    
    return new ListOIterator<ApplicationFileResource>(result);
  }

  @Override
  public Optional<ApplicationFileResource> getChildFile(String path)
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
