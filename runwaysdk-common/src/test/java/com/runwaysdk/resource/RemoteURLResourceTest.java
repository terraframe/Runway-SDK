package com.runwaysdk.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class RemoteURLResourceTest extends ApplicationResourceTest
{

  public RemoteURLResourceTest() throws MalformedURLException
  {
    super(new RemoteURLResource(new URL("https://dl.cloudsmith.io/public/terraframe/public/raw/names/RemoteURLResourceTest.txt/versions/1.0/test.txt"), "test.txt"));
  }
  
  @Test
  public void getName()
  {
    Assert.assertTrue(this.resource.getName().length() > 0);
  }
  
  @Test
  public void openNewStream() throws IOException
  {
    InputStream stream = this.resource.openNewStream();
    
    Assert.assertNotNull(stream);
    Assert.assertEquals("test\n", IOUtils.toString(stream, "UTF-8"));
  }
  
  @Test
  public void isRemote()
  {
    Assert.assertTrue(this.resource.isRemote());
  }
  
  @Test
  public void exists()
  {
  }
  
}
