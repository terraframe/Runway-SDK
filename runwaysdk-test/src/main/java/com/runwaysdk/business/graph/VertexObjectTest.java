/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
 */
package com.runwaysdk.business.graph;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeLineStringDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiLineStringDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPolygonDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePolygonDAO;
import com.runwaysdk.session.Request;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class VertexObjectTest
{
  private static MdVertexDAO                   mdVertexDAO;

  private static MdAttributeCharacterDAO       mdCharacterAttribute;

  private static MdAttributeIntegerDAO         mdIntegerAttribute;

  private static MdAttributeLongDAO            mdLongAttribute;

  private static MdAttributeFloatDAO           mdFloatAttribute;

  private static MdAttributeDoubleDAO          mdDoubleAttribute;

  private static MdAttributeBooleanDAO         mdBooleanAttribute;

  private static MdAttributeDateDAO            mdDateAttribute;

  private static MdAttributeDateTimeDAO        mdDateTimeAttribute;

  private static MdAttributeTimeDAO            mdTimeAttribute;

  private static MdAttributePointDAO           mdPointAttribute;

  private static MdAttributePolygonDAO         mdPolygonAttribute;

  private static MdAttributeLineStringDAO      mdLineStringAttribute;

  private static MdAttributeMultiPointDAO      mdMultiPointAttribute;

  private static MdAttributeMultiPolygonDAO    mdMultiPolygonAttribute;

  private static MdAttributeMultiLineStringDAO mdMultiLineStringAttribute;

  @Request
  @BeforeClass
  public static void classSetup()
  {
    LocalProperties.setSkipCodeGenAndCompile(true);

    mdVertexDAO = TestFixtureFactory.createMdVertex();
    mdVertexDAO.setValue(MdVertexInfo.GENERATE_SOURCE, Boolean.FALSE.toString());
    mdVertexDAO.apply();

    mdCharacterAttribute = TestFixtureFactory.addCharacterAttribute(mdVertexDAO);
    mdCharacterAttribute.apply();

    mdIntegerAttribute = TestFixtureFactory.addIntegerAttribute(mdVertexDAO);
    mdIntegerAttribute.apply();

    mdLongAttribute = TestFixtureFactory.addLongAttribute(mdVertexDAO);
    mdLongAttribute.apply();

    mdFloatAttribute = TestFixtureFactory.addFloatAttribute(mdVertexDAO);
    mdFloatAttribute.apply();

    mdDoubleAttribute = TestFixtureFactory.addDoubleAttribute(mdVertexDAO);
    mdDoubleAttribute.apply();

    mdBooleanAttribute = TestFixtureFactory.addBooleanAttribute(mdVertexDAO);
    mdBooleanAttribute.apply();

    mdDateAttribute = TestFixtureFactory.addDateAttribute(mdVertexDAO);
    mdDateAttribute.setValue(MdAttributeDateInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdDateAttribute.apply();

    mdDateTimeAttribute = TestFixtureFactory.addDateTimeAttribute(mdVertexDAO);
    mdDateTimeAttribute.apply();

    mdTimeAttribute = TestFixtureFactory.addTimeAttribute(mdVertexDAO);
    mdTimeAttribute.apply();

    mdPointAttribute = TestFixtureFactory.addPointAttribute(mdVertexDAO);
    mdPointAttribute.apply();

    mdPolygonAttribute = TestFixtureFactory.addPolygonAttribute(mdVertexDAO);
    mdPolygonAttribute.apply();

    mdLineStringAttribute = TestFixtureFactory.addLineStringAttribute(mdVertexDAO);
    mdLineStringAttribute.apply();

    mdMultiPointAttribute = TestFixtureFactory.addMultiPointAttribute(mdVertexDAO);
    mdMultiPointAttribute.apply();

    mdMultiPolygonAttribute = TestFixtureFactory.addMultiPolygonAttribute(mdVertexDAO);
    mdMultiPolygonAttribute.apply();

    mdMultiLineStringAttribute = TestFixtureFactory.addMultiLineStringAttribute(mdVertexDAO);
    mdMultiLineStringAttribute.apply();
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdVertexDAO);

    LocalProperties.setSkipCodeGenAndCompile(false);
  }

  @Request
  @Test
  public void testCharacterAttribute()
  {
    String attributeName = mdCharacterAttribute.definesAttribute();

    VertexObject vertex = new VertexObject(mdVertexDAO.definesType());

    String value = "Test Value";

    vertex.setValue(attributeName, value);

    Assert.assertEquals(value, vertex.getObjectValue(attributeName));

    try
    {
      // Test create
      vertex.apply();

      VertexObject test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = "Updated Value";

      vertex.setValue(attributeName, value);
      vertex.apply();

      test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName));
    }
    finally
    {
      vertex.delete();
    }

    Assert.assertNull(VertexObject.get(mdVertexDAO, vertex.getOid()));
  }

  @Request
  @Test
  public void testIntegerAttribute()
  {
    String attributeName = mdIntegerAttribute.definesAttribute();
    VertexObject vertex = new VertexObject(mdVertexDAO.definesType());

    Integer value = new Integer(5);
    vertex.setValue(attributeName, value);

    Assert.assertEquals(value, vertex.getObjectValue(attributeName));

    try
    {
      // Test create
      vertex.apply();

      VertexObject test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = new Integer(10);

      vertex.setValue(attributeName, value);
      vertex.apply();

      test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName));
    }
    finally
    {
      vertex.delete();
    }

    Assert.assertNull(VertexObject.get(mdVertexDAO, vertex.getOid()));
  }

  @Request
  @Test
  public void testLongAttribute()
  {
    String attributeName = mdLongAttribute.definesAttribute();
    VertexObject vertex = new VertexObject(mdVertexDAO.definesType());

    Long value = new Long(5);
    vertex.setValue(attributeName, value);

    Assert.assertEquals(value, vertex.getObjectValue(attributeName));

    try
    {
      vertex.apply();

      VertexObject test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = new Long(10);

      vertex.setValue(attributeName, value);
      vertex.apply();

      test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName));
    }
    finally
    {
      vertex.delete();
    }

    Assert.assertNull(VertexObject.get(mdVertexDAO, vertex.getOid()));
  }

  @Request
  @Test
  public void testFloatAttribute()
  {
    String attributeName = mdFloatAttribute.definesAttribute();
    VertexObject vertex = new VertexObject(mdVertexDAO.definesType());

    Float value = new Float(5F);
    vertex.setValue(attributeName, value);

    Assert.assertEquals(value, vertex.getObjectValue(attributeName));

    try
    {
      vertex.apply();

      VertexObject test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = new Float(10F);

      vertex.setValue(attributeName, value);
      vertex.apply();

      test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName));
    }
    finally
    {
      vertex.delete();
    }

    Assert.assertNull(VertexObject.get(mdVertexDAO, vertex.getOid()));
  }

  @Request
  @Test
  public void testDoubleAttribute()
  {
    String attributeName = mdDoubleAttribute.definesAttribute();
    VertexObject vertex = new VertexObject(mdVertexDAO.definesType());

    Double value = new Double(5D);
    vertex.setValue(attributeName, value);

    Assert.assertEquals(value, vertex.getObjectValue(attributeName));

    try
    {
      vertex.apply();

      VertexObject test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = new Double(10D);

      vertex.setValue(attributeName, value);
      vertex.apply();

      test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName));
    }
    finally
    {
      vertex.delete();
    }

    Assert.assertNull(VertexObject.get(mdVertexDAO, vertex.getOid()));
  }

  @Request
  @Test
  public void testBooleanAttribute()
  {
    String attributeName = mdBooleanAttribute.definesAttribute();
    VertexObject vertex = new VertexObject(mdVertexDAO.definesType());

    Boolean value = Boolean.TRUE;
    vertex.setValue(attributeName, value);

    Assert.assertEquals(value, vertex.getObjectValue(attributeName));

    try
    {
      vertex.apply();

      VertexObject test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = Boolean.FALSE;

      vertex.setValue(attributeName, value);
      vertex.apply();

      test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName));
    }
    finally
    {
      vertex.delete();
    }

    Assert.assertNull(VertexObject.get(mdVertexDAO, vertex.getOid()));
  }

  @Request
  @Test
  public void testDateAttribute()
  {
    String attributeName = mdDateAttribute.definesAttribute();
    VertexObject vertex = new VertexObject(mdVertexDAO.definesType());

    Calendar cal = TestFixtureFactory.getDate();

    Date value = cal.getTime();

    vertex.setValue(attributeName, value);

    Assert.assertEquals(value, vertex.getObjectValue(attributeName));

    try
    {
      vertex.apply();

      VertexObject test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      cal = TestFixtureFactory.getDate();
      cal.set(Calendar.DAY_OF_MONTH, 1);

      value = cal.getTime();

      vertex.setValue(attributeName, value);
      vertex.apply();

      test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName));
    }
    finally
    {
      vertex.delete();
    }

    Assert.assertNull(VertexObject.get(mdVertexDAO, vertex.getOid()));
  }

  @Request
  @Test
  public void testDateTimeAttribute()
  {
    String attributeName = mdDateTimeAttribute.definesAttribute();
    VertexObject vertex = new VertexObject(mdVertexDAO.definesType());

    Date value = new Date();
    vertex.setValue(attributeName, value);

    Assert.assertEquals(value, vertex.getObjectValue(attributeName));

    try
    {
      vertex.apply();

      VertexObject test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = new Date();

      vertex.setValue(attributeName, value);
      vertex.apply();

      test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName));
    }
    finally
    {
      vertex.delete();
    }

    Assert.assertNull(VertexObject.get(mdVertexDAO, vertex.getOid()));
  }

  @Request
  @Test
  public void testTimeAttribute()
  {
    String attributeName = mdTimeAttribute.definesAttribute();
    VertexObject vertex = new VertexObject(mdVertexDAO.definesType());

    Date value = new Date();
    vertex.setValue(attributeName, value);

    Assert.assertEquals(value, vertex.getObjectValue(attributeName));

    try
    {
      vertex.apply();

      VertexObject test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = new Date();

      vertex.setValue(attributeName, value);
      vertex.apply();

      test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName));
    }
    finally
    {
      vertex.delete();
    }

    Assert.assertNull(VertexObject.get(mdVertexDAO, vertex.getOid()));
  }

  @Request
  @Test
  public void testPointAttribute()
  {
    String attributeName = mdPointAttribute.definesAttribute();
    VertexObject vertex = new VertexObject(mdVertexDAO.definesType());

    Point value = TestFixtureFactory.getPoint();

    vertex.setValue(attributeName, value);

    Assert.assertEquals(value, vertex.getObjectValue(attributeName));

    try
    {
      vertex.apply();

      VertexObject test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = TestFixtureFactory.getPoint2();

      vertex.setValue(attributeName, value);
      vertex.apply();

      test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName));
    }
    finally
    {
      vertex.delete();
    }

    Assert.assertNull(VertexObject.get(mdVertexDAO, vertex.getOid()));
  }

  @Request
  @Test
  public void testPolygonAttribute()
  {
    String attributeName = mdPolygonAttribute.definesAttribute();
    VertexObject vertex = new VertexObject(mdVertexDAO.definesType());

    Polygon value = TestFixtureFactory.getPolygon();

    vertex.setValue(attributeName, value);

    Assert.assertEquals(value, vertex.getObjectValue(attributeName));

    try
    {
      vertex.apply();

      VertexObject test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = TestFixtureFactory.getPolygon2();

      vertex.setValue(attributeName, value);
      vertex.apply();

      test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName));
    }
    finally
    {
      vertex.delete();
    }

    Assert.assertNull(VertexObject.get(mdVertexDAO, vertex.getOid()));
  }

  @Request
  @Test
  public void testLineStringAttribute()
  {
    String attributeName = mdLineStringAttribute.definesAttribute();
    VertexObject vertex = new VertexObject(mdVertexDAO.definesType());

    LineString value = TestFixtureFactory.getLineString();

    vertex.setValue(attributeName, value);

    Assert.assertEquals(value, vertex.getObjectValue(attributeName));

    try
    {
      vertex.apply();

      VertexObject test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));
    }
    finally
    {
      vertex.delete();
    }

    Assert.assertNull(VertexObject.get(mdVertexDAO, vertex.getOid()));
  }

  @Request
  @Test
  public void testMultiPointAttribute()
  {
    String attributeName = mdMultiPointAttribute.definesAttribute();
    VertexObject vertex = new VertexObject(mdVertexDAO.definesType());

    MultiPoint value = TestFixtureFactory.getMultiPoint();

    vertex.setValue(attributeName, value);

    Assert.assertEquals(value, vertex.getObjectValue(attributeName));

    try
    {
      vertex.apply();

      VertexObject test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));
    }
    finally
    {
      vertex.delete();
    }

    Assert.assertNull(VertexObject.get(mdVertexDAO, vertex.getOid()));
  }

  @Request
  @Test
  public void testMultiPolygonAttribute()
  {
    String attributeName = mdMultiPolygonAttribute.definesAttribute();
    VertexObject vertex = new VertexObject(mdVertexDAO.definesType());

    MultiPolygon value = TestFixtureFactory.getMultiPolygon();

    vertex.setValue(attributeName, value);

    Assert.assertEquals(value, vertex.getObjectValue(attributeName));

    try
    {
      vertex.apply();

      VertexObject test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));
    }
    finally
    {
      vertex.delete();
    }

    Assert.assertNull(VertexObject.get(mdVertexDAO, vertex.getOid()));
  }

  @Request
  @Test
  public void testMultiLineStringAttribute()
  {
    String attributeName = mdMultiLineStringAttribute.definesAttribute();
    VertexObject vertex = new VertexObject(mdVertexDAO.definesType());

    MultiLineString value = TestFixtureFactory.getMultiLineString();

    vertex.setValue(attributeName, value);

    Assert.assertEquals(value, vertex.getObjectValue(attributeName));

    try
    {
      vertex.apply();

      VertexObject test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));
    }
    finally
    {
      vertex.delete();
    }

    Assert.assertNull(VertexObject.get(mdVertexDAO, vertex.getOid()));
  }

  @Request
  @Test
  public void testQuery()
  {
    String attributeName = mdCharacterAttribute.definesAttribute();

    String value = "Test Value";

    VertexObject vertex = new VertexObject(mdVertexDAO.definesType());
    vertex.setValue(attributeName, value);

    try
    {
      // Test create
      vertex.apply();

      String stm = "SELECT FROM " + mdVertexDAO.getDBClassName() + " WHERE " + mdCharacterAttribute.getColumnName() + " = :name";

      GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(stm);
      query.setParameter("name", value);

      List<VertexObject> results = query.getResults();

      Assert.assertEquals(1, results.size());
    }
    finally
    {
      vertex.delete();
    }
  }

  @Request
  @Test
  public void testCountQuery()
  {
    String attributeName = mdCharacterAttribute.definesAttribute();

    VertexObject vertex = new VertexObject(mdVertexDAO.definesType());
    vertex.setValue(attributeName, "Test Value");

    try
    {
      // Test create
      vertex.apply();

      String stm = "SELECT COUNT(*) FROM " + mdVertexDAO.getDBClassName();

      GraphQuery<Long> query = new GraphQuery<Long>(stm);

      Long result = query.getSingleResult();

      Assert.assertEquals(new Long(1), result);
    }
    finally
    {
      vertex.delete();
    }
  }
}
