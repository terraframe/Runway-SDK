package com.runwaysdk.resource;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

abstract public class ApplicationResourceTest
{
  protected ApplicationResource resource;
  
  public ApplicationResourceTest(ApplicationResource resource)
  {
    this.resource = resource;
  }
  
  @Test
  public void openNewStream() throws IOException
  {
    InputStream stream = this.resource.openNewStream();
    
    Assert.assertNotNull(stream);
  }
  
  @Test
  public void openNewFile()
  {
    CloseableFile file = this.resource.openNewFile();
    
    Assert.assertTrue(file.exists());
    
    file.close();
  }
  
  @Test
  public void getUnderlyingFile()
  {
    CloseableFile file = this.resource.openNewFile();
    
    Assert.assertTrue(file.exists());
    
    file.close();
  }
  
  @Test
  public void getName()
  {
    Assert.assertTrue(this.resource.getName().length() > 0);
  }
  
  @Test
  public void getBaseName()
  {
    Assert.assertTrue(this.resource.getBaseName().length() > 0);
  }
  
  @Test
  public void getNameExtension()
  {
    Assert.assertTrue(this.resource.getNameExtension().length() > 0);
  }
  
  @Test
  public void isRemote()
  {
    Assert.assertFalse(this.resource.isRemote());
  }
  
  @Test
  public void exists()
  {
    Assert.assertTrue(this.resource.exists());
  }
}
