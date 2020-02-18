/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;

public class VertexInheritanceDAOTest
{
  private static MdVertexDAO             mdParentDAO;

  private static MdVertexDAO             mdChildDAO;

  private static MdAttributeCharacterDAO mdCharacterAttribute;

  @Request
  @BeforeClass
  public static void classSetup()
  {
    LocalProperties.setSkipCodeGenAndCompile(true);

    classSetup_Transaction();
  }

  @Transaction
  private static void classSetup_Transaction()
  {
    mdParentDAO = TestFixtureFactory.createMdVertex("TestParent");
    mdParentDAO.setValue(MdVertexInfo.ABSTRACT, Boolean.TRUE.toString());
    mdParentDAO.apply();

    mdChildDAO = TestFixtureFactory.createMdVertex("TestChild");
    mdChildDAO.setValue(MdVertexInfo.SUPER_MD_VERTEX, mdParentDAO.getOid());
    mdChildDAO.apply();

    mdCharacterAttribute = TestFixtureFactory.addCharacterAttribute(mdParentDAO);
    mdCharacterAttribute.apply();

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
    TestFixtureFactory.delete(mdChildDAO);
    TestFixtureFactory.delete(mdParentDAO);
  }

  @Request
  @Test
  public void testCharacterAttribute()
  {
    String attributeName = mdCharacterAttribute.definesAttribute();

    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdChildDAO.definesType());

    Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

    String value = "Test Value";

    vertexDAO.setValue(attributeName, value);

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName));

    try
    {
      // Test create
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdChildDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = "Updated Value";

      vertexDAO.setValue(attributeName, value);
      vertexDAO.apply();

      test = VertexObjectDAO.get(mdParentDAO, vertexDAO.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName));
    }
    finally
    {
      vertexDAO.delete();
    }

    Assert.assertNull(VertexObjectDAO.get(mdChildDAO, vertexDAO.getOid()));
  }

}
