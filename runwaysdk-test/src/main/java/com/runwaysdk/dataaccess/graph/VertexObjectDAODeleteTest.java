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
package com.runwaysdk.dataaccess.graph;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;

public class VertexObjectDAODeleteTest
{
  private static MdVertexDAO             mdVertexDAO;

  private static MdAttributeCharacterDAO mdCharacterAttribute;

  @Request
  @BeforeClass
  public static void classSetup()
  {
    LocalProperties.setSkipCodeGenAndCompile(true);

    TestFixtureFactory.deleteMdClass(TestFixConst.TEST_VERTEX1_TYPE);

    classSetup_Transaction();
  }

  @Transaction
  private static void classSetup_Transaction()
  {
    mdVertexDAO = TestFixtureFactory.createMdVertex();
    mdVertexDAO.apply();
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    classTearDown_Transaction();

    LocalProperties.setSkipCodeGenAndCompile(false);
  }

  @Transaction
  public static void classTearDown_Transaction()
  {
    TestFixtureFactory.delete(mdVertexDAO);
  }

  @Request
  @Test
  public void testDeleteRequiredCharacterAttributeWithData()
  {
    try
    {
      mdCharacterAttribute = TestFixtureFactory.addCharacterAttribute(mdVertexDAO);
      mdCharacterAttribute.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
      mdCharacterAttribute.apply();

      String attributeName = mdCharacterAttribute.definesAttribute();

      VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

      Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

      String value = "Test Value";

      vertexDAO.setValue(attributeName, value);

      Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName));

      // Test create
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      mdCharacterAttribute.delete();

      mdCharacterAttribute = null;

      VertexObjectDAOIF test2 = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test2);
    }
    finally
    {
//      if (mdCharacterAttribute != null)
//      {
//        mdCharacterAttribute.delete();
//      }
    }
  }

}
