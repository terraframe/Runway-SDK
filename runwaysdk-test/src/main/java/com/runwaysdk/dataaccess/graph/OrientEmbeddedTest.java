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
import org.locationtech.jts.geom.Point;

import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePointDAO;
import com.runwaysdk.session.Request;

public class OrientEmbeddedTest
{
  private static MdVertexDAO                          mdVertexDAO;
  
  private static MdAttributeTextDAO                   mdTextAttribute;

  // Embedded class
  private static MdVertexDAO                          mdEmbeddedVertexDAO;

  private static MdAttributeCharacterDAO              mdEmbeddedCharacterAttribute;
  
  private static MdAttributePointDAO                  mdPointAttribute;

  @Request
  @BeforeClass
  public static void classSetup()
  {
    LocalProperties.setSkipCodeGenAndCompile(true);

    TestFixtureFactory.deleteMdClass(TestFixConst.TEST_VERTEX1_TYPE);
    TestFixtureFactory.deleteMdClass(TestFixConst.TEST_EMBEDDED_VERTEX1_TYPE);
    TestFixtureFactory.deleteMdClass(TestFixConst.TEST_CLASS1_TYPE);
    TestFixtureFactory.deleteMdClass(TestFixConst.TEST_ENUM_CLASS1_TYPE);

    // createMdClassification();

    classSetup_Transaction();
  }

  @Transaction
  private static void classSetup_Transaction()
  {
    mdVertexDAO = TestFixtureFactory.createMdVertex();
    mdVertexDAO.apply();

    mdTextAttribute = TestFixtureFactory.addTextAttribute(mdVertexDAO);
    mdTextAttribute.apply();
    
    mdPointAttribute = TestFixtureFactory.addPointAttribute(mdVertexDAO);
    mdPointAttribute.apply();

    // Define the embedded class
    mdEmbeddedVertexDAO = TestFixtureFactory.createMdVertex("TestEmbeddedClass");
    mdEmbeddedVertexDAO.apply();

    mdEmbeddedCharacterAttribute = TestFixtureFactory.addCharacterAttribute(mdEmbeddedVertexDAO);
    mdEmbeddedCharacterAttribute.apply();
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
    TestFixtureFactory.delete(mdEmbeddedVertexDAO);
  }
  
  @Request
  @Test
  public void testPointAttribute()
  {
    String attributeName = mdPointAttribute.definesAttribute();
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

    Point value = TestFixtureFactory.getPoint();

    vertexDAO.setValue(attributeName, value);

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = TestFixtureFactory.getPoint2();

      vertexDAO.setValue(attributeName, value);
      vertexDAO.apply();

      test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName));
    }
    finally
    {
      vertexDAO.delete();
    }

    Assert.assertNull(VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid()));
  }

  @Request
  @Test
  public void testTextAttribute()
  {
    String attributeName = mdTextAttribute.definesAttribute();
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

    String value = "Test";
    vertexDAO.setValue(attributeName, value);

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = "Update";

      vertexDAO.setValue(attributeName, value);
      vertexDAO.apply();

      test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName));
    }
    finally
    {
      vertexDAO.delete();
    }

    Assert.assertNull(VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid()));
  }
}
