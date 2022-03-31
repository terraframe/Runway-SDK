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
