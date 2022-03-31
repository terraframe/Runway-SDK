package com.runwaysdk.resource;

import org.junit.Assert;
import org.junit.Test;

abstract public class ApplicationTreeResourceTest extends ApplicationResourceTest
{

  protected ApplicationTreeResource treeResource;
  
  public ApplicationTreeResourceTest(ApplicationTreeResource resource)
  {
    super(resource);
    this.treeResource = resource;
  }
  
  @Test
  public void getChild()
  {
    Assert.assertFalse(this.treeResource.getChild("/doesNotExist.txt").exists());
  }
  
}
