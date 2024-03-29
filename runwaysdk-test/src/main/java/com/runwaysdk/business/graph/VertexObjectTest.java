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
package com.runwaysdk.business.graph;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeLineStringDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiLineStringDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPolygonDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePolygonDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeShapeDAO;
import com.runwaysdk.session.Request;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

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

  private static MdAttributeEnumerationDAO     mdEnumerationAttribute;

  private static MdAttributePointDAO           mdPointAttribute;

  private static MdAttributePolygonDAO         mdPolygonAttribute;

  private static MdAttributeShapeDAO           mdShapeAttribute;

  private static MdAttributeLineStringDAO      mdLineStringAttribute;

  private static MdAttributeMultiPointDAO      mdMultiPointAttribute;

  private static MdAttributeMultiPolygonDAO    mdMultiPolygonAttribute;

  private static MdAttributeMultiLineStringDAO mdMultiLineStringAttribute;

  private static MdBusinessDAO                 mdBizEnum;

  private static MdEnumerationDAO              mdEnum;

  private static BusinessDAO                   colorado;

  private static BusinessDAO                   washington;

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

    mdBizEnum = TestFixtureFactory.createEnumClass1();
    mdBizEnum.setValue(MdEnumerationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBizEnum.apply();
    
    mdEnum = TestFixtureFactory.createMdEnumeration1(mdBizEnum);
    mdEnum.setValue(MdEnumerationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdEnum.apply();

    TestFixtureFactory.addCharacterAttribute(mdBizEnum).apply();

    colorado = BusinessDAO.newInstance(mdBizEnum.definesType());
    colorado.setValue(EnumerationMasterInfo.NAME, "CO");
    colorado.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Colorado");
    colorado.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "CO");
    colorado.apply();

    washington = BusinessDAO.newInstance(mdBizEnum.definesType());
    washington.setValue(EnumerationMasterInfo.NAME, "WA");
    washington.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Washington");
    washington.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "WA");
    washington.apply();

    mdEnumerationAttribute = TestFixtureFactory.addEnumerationAttribute(mdVertexDAO, mdEnum);
    mdEnumerationAttribute.apply();

    mdPointAttribute = TestFixtureFactory.addPointAttribute(mdVertexDAO);
    mdPointAttribute.apply();

    mdPolygonAttribute = TestFixtureFactory.addPolygonAttribute(mdVertexDAO);
    mdPolygonAttribute.apply();

    mdShapeAttribute = TestFixtureFactory.addShapeAttribute(mdVertexDAO);
    mdShapeAttribute.apply();
    
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

    TestFixtureFactory.delete(mdEnum);

    TestFixtureFactory.delete(mdBizEnum);

    LocalProperties.setSkipCodeGenAndCompile(false);
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testEnumAttribute()
  {
    String attributeName = mdEnumerationAttribute.definesAttribute();

    VertexObject vertex = new VertexObject(mdVertexDAO.definesType());

    String value = colorado.getOid();

    vertex.setValue(attributeName, value);

    Set<String> getValue = ( (Set<String>) vertex.getObjectValue(attributeName) );

    Assert.assertEquals(1, getValue.size());
    Assert.assertEquals(value, getValue.iterator().next());

    try
    {
      // Test create
      vertex.apply();

      VertexObject test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertNotNull(test);

      getValue = ( (Set<String>) test.getObjectValue(attributeName) );

      Assert.assertEquals(1, getValue.size());
      Assert.assertEquals(value, getValue.iterator().next());

      // Test update
      value = washington.getOid();

      vertex.setValue(attributeName, value);
      vertex.apply();

      test = VertexObject.get(mdVertexDAO, vertex.getOid());

      getValue = ( (Set<String>) test.getObjectValue(attributeName) );

      Assert.assertEquals(1, getValue.size());
      Assert.assertEquals(value, getValue.iterator().next());
    }
    finally
    {
      vertex.delete();
    }

    Assert.assertNull(VertexObject.get(mdVertexDAO, vertex.getOid()));
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

    Integer value = Integer.valueOf(5);
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
      value = Integer.valueOf(10);

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

    Long value = Long.valueOf(5);
    vertex.setValue(attributeName, value);

    Assert.assertEquals(value, vertex.getObjectValue(attributeName));

    try
    {
      vertex.apply();

      VertexObject test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = Long.valueOf(10);

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

    Double value = Double.valueOf(5D);
    vertex.setValue(attributeName, value);

    Assert.assertEquals(value, vertex.getObjectValue(attributeName));

    try
    {
      vertex.apply();

      VertexObject test = VertexObject.get(mdVertexDAO, vertex.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = Double.valueOf(10D);

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
  public void testShapeAttribute()
  {
    String attributeName = mdShapeAttribute.definesAttribute();
    VertexObject vertex = new VertexObject(mdVertexDAO.definesType());
    
    Geometry value = TestFixtureFactory.getPolygon();
    
    vertex.setValue(attributeName, value);
    
    Assert.assertEquals(value, vertex.getObjectValue(attributeName));
    
    try
    {
      vertex.apply();
      
      VertexObject test = VertexObject.get(mdVertexDAO, vertex.getOid());
      
      Assert.assertNotNull(test);
      
      Assert.assertEquals(value, test.getObjectValue(attributeName));
      
      // Test update
      value = TestFixtureFactory.getPoint();
      
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

      Assert.assertEquals(Long.valueOf(1), result);
    }
    finally
    {
      vertex.delete();
    }
  }
}
