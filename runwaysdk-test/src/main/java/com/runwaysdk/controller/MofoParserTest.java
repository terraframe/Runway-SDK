/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.controller;

import java.lang.reflect.Method;
import java.util.Locale;

import junit.framework.TestCase;

import org.junit.Assert;

import com.runwaysdk.ClientSession;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.form.FormObject;

public class MofoParserTest extends TestCase
{
  public final class MockClass
  {
    @ActionParameters(parameters = "com.runwaysdk.form.FormObject:criteria, java.lang.String:type, java.lang.String:sortAttribute, java.lang.Boolean:isAscending, java.lang.Integer:pageSize, java.lang.Integer:pageNumber", post = true)
    public void testMethod(FormObject critiera, String type, String sortAttribute, Boolean isAscending, Integer pageSize, Integer pageNumber)
    {

    }
  }

  /**
   * @return
   * @throws NoSuchMethodException
   */
  private ActionParameters getAnnotation() throws NoSuchMethodException
  {
    Method method = MockClass.class.getMethod("testMethod", FormObject.class, String.class, String.class, Boolean.class, Integer.class, Integer.class);
    ActionParameters annotation = method.getAnnotation(ActionParameters.class);

    return annotation;
  }

  public void testNullFormObject() throws NoSuchMethodException, SecurityException
  {
    ClientSession session = ClientSession.createUserSession("default", "SYSTEM", "SYSTEM", new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      String json = "{\"criteria\":null,\"type\":\"dss.vector.solutions.form.business.TestForm\",\"sortAttribute\":null,\"isAscending\":true,\"pageSize\":20,\"pageNumber\":1}";

      RequestManager manager = new RequestManager(null, session, session.getRequest());

      ActionParameters annotation = this.getAnnotation();

      MofoParser parser = new MofoParser(manager, annotation, json);
      Object[] objects = parser.getObjects();

      Assert.assertEquals(6, objects.length);
      Assert.assertNull(objects[0]);
      Assert.assertEquals("dss.vector.solutions.form.business.TestForm", objects[1]);
      Assert.assertNull(objects[2]);
      Assert.assertTrue((Boolean) objects[3]);
      Assert.assertEquals(20, objects[4]);
      Assert.assertEquals(1, objects[5]);
    }
    finally
    {
      if (session != null)
      {
        session.logout();
      }
    }
  }
}
