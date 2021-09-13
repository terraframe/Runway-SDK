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

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.AttributeCharacterParseException;
import com.runwaysdk.AttributeIntegerParseException;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeClassificationInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.graph.MdClassificationInfo;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.graph.attributes.AttributeCharacter;
import com.runwaysdk.dataaccess.graph.attributes.AttributeLocalEmbedded;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeClassificationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEmbeddedDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeGraphReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalCharacterEmbeddedDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdClassificationDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.AttributeLineStringParseException;
import com.runwaysdk.gis.AttributeMultiLineStringParseException;
import com.runwaysdk.gis.AttributeMultiPointParseException;
import com.runwaysdk.gis.AttributeMultiPolygonParseException;
import com.runwaysdk.gis.AttributePointParseException;
import com.runwaysdk.gis.AttributePolygonParseException;
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

public class VertexObjectDAOTest
{
  private static MdClassificationDAO                  mdClassificationDAO;

  private static MdVertexDAO                          mdReferenceDAO;

  private static MdVertexDAO                          mdVertexDAO;

  private static MdBusinessDAO                        mdBusinessDAO;

  private static MdBusinessDAO                        mdEnumMasterDAO;

  private static MdEnumerationDAO                     mdEnumerationDAO;

  private static MdAttributeCharacterDAO              mdCharacterAttribute;

  private static MdAttributeIntegerDAO                mdIntegerAttribute;

  private static MdAttributeLongDAO                   mdLongAttribute;

  private static MdAttributeFloatDAO                  mdFloatAttribute;

  private static MdAttributeDoubleDAO                 mdDoubleAttribute;

  private static MdAttributeBooleanDAO                mdBooleanAttribute;

  private static MdAttributeDateDAO                   mdDateAttribute;

  private static MdAttributeDateTimeDAO               mdDateTimeAttribute;

  private static MdAttributeTimeDAO                   mdTimeAttribute;

  private static MdAttributeTextDAO                   mdTextAttribute;

  private static MdAttributePointDAO                  mdPointAttribute;

  private static MdAttributePolygonDAO                mdPolygonAttribute;

  private static MdAttributeLineStringDAO             mdLineStringAttribute;

  private static MdAttributeMultiPointDAO             mdMultiPointAttribute;

  private static MdAttributeMultiPolygonDAO           mdMultiPolygonAttribute;

  private static MdAttributeMultiLineStringDAO        mdMultiLineStringAttribute;

  private static MdAttributeReferenceDAO              mdReferenceAttribute;

  private static MdAttributeEnumerationDAO            mdEnumerationAttribute;

  private static MdAttributeEmbeddedDAO               mdAttributeEmbedded;

  private static MdAttributeLocalCharacterEmbeddedDAO mdLocalCharacterAttribute;

  private static MdAttributeGraphReferenceDAO         mdGraphReferenceAttribute;

  private static MdAttributeClassificationDAO         mdClassificationAttribute;

  // Embedded class
  private static MdVertexDAO                          mdEmbeddedVertexDAO;

  private static MdAttributeCharacterDAO              mdEmbeddedCharacterAttribute;

  private static VertexObjectDAO                      attributeRoot;

  @Request
  @BeforeClass
  public static void classSetup()
  {
    LocalProperties.setSkipCodeGenAndCompile(true);

    TestFixtureFactory.deleteMdClass(TestFixConst.TEST_VERTEX1_TYPE);
    TestFixtureFactory.deleteMdClass(TestFixConst.TEST_EMBEDDED_VERTEX1_TYPE);
    TestFixtureFactory.deleteMdClass(TestFixConst.TEST_CLASS1_TYPE);
    TestFixtureFactory.deleteMdClass(TestFixConst.TEST_ENUM_CLASS1_TYPE);

//    createMdClassification();

    classSetup_Transaction();
  }

  @Transaction
  private static void classSetup_Transaction()
  {
    createMdClassification();
    
    mdReferenceDAO = TestFixtureFactory.createMdVertex("TestLinkClass");
    mdReferenceDAO.apply();

    mdVertexDAO = TestFixtureFactory.createMdVertex();
    mdVertexDAO.apply();

    mdBusinessDAO = TestFixtureFactory.createMdBusiness1();
    mdBusinessDAO.apply();

    mdEnumMasterDAO = TestFixtureFactory.createEnumClass1();
    mdEnumMasterDAO.apply();

    mdEnumerationDAO = TestFixtureFactory.createMdEnumeation1(mdEnumMasterDAO);
    mdEnumerationDAO.apply();

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

    mdTextAttribute = TestFixtureFactory.addTextAttribute(mdVertexDAO);
    mdTextAttribute.apply();

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

    mdReferenceAttribute = TestFixtureFactory.addReferenceAttribute(mdVertexDAO, mdBusinessDAO);
    mdReferenceAttribute.apply();

    mdLocalCharacterAttribute = TestFixtureFactory.addLocalCharacterEmbeddedAttribute(mdVertexDAO);
    mdLocalCharacterAttribute.apply();

    mdEnumerationAttribute = TestFixtureFactory.addEnumerationAttribute(mdVertexDAO, mdEnumerationDAO);
    mdEnumerationAttribute.apply();

    mdGraphReferenceAttribute = TestFixtureFactory.addGraphReferenceAttribute(mdVertexDAO, mdReferenceDAO);
    mdGraphReferenceAttribute.apply();

    mdClassificationAttribute = TestFixtureFactory.addClassificationAttribute(mdVertexDAO, mdClassificationDAO);
    mdClassificationAttribute.setValue(MdAttributeClassificationInfo.ROOT, attributeRoot.getOid());
    mdClassificationAttribute.apply();

    // Define the embedded class
    mdEmbeddedVertexDAO = TestFixtureFactory.createMdVertex("TestEmbeddedClass");
    mdEmbeddedVertexDAO.apply();

    mdEmbeddedCharacterAttribute = TestFixtureFactory.addCharacterAttribute(mdEmbeddedVertexDAO);
    mdEmbeddedCharacterAttribute.apply();

    mdAttributeEmbedded = TestFixtureFactory.addEmbeddedttribute(mdVertexDAO, mdEmbeddedVertexDAO);
    mdAttributeEmbedded.apply();

  }

//  @Transaction
  private static void createMdClassification()
  {
    // Define the link class
    mdClassificationDAO = MdClassificationDAO.create(TestFixConst.TEST_PACKAGE, "TestClassification", "Test Classification");

    // Create nodes
    VertexObjectDAO root = VertexObjectDAO.newInstance(mdClassificationDAO.getReferenceMdVertexDAO());
    root.setValue("code", "TEST-ROOT");
    root.apply();

    attributeRoot = VertexObjectDAO.newInstance(mdClassificationDAO.getReferenceMdVertexDAO());
    attributeRoot.apply();

    attributeRoot.addParent(root, mdClassificationDAO.getReferenceMdEdgeDAO()).apply();

    mdClassificationDAO.setValue(MdClassificationInfo.ROOT, root.getOid());
    mdClassificationDAO.apply();
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
    TestFixtureFactory.delete(mdEnumerationDAO);
    TestFixtureFactory.delete(mdEnumMasterDAO);
    TestFixtureFactory.delete(mdBusinessDAO);
    TestFixtureFactory.delete(mdEmbeddedVertexDAO);
    TestFixtureFactory.delete(mdReferenceDAO);
    TestFixtureFactory.delete(mdClassificationDAO);
  }

  @Request
  @Test
  public void testEmbeddedAttribute()
  {
    VertexObjectDAO vertexDAO1 = null;
    VertexObjectDAO vertexDAO2 = null;

    try
    {
      String embeddedAttributeName = mdAttributeEmbedded.definesAttribute();

      vertexDAO1 = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

      Assert.assertNotNull(vertexDAO1.getAttributeIF(embeddedAttributeName));

      VertexObjectDAO embeddedVertexDAO = (VertexObjectDAO) vertexDAO1.getEmbeddedComponentDAO(embeddedAttributeName);

      Assert.assertNotNull(embeddedVertexDAO);

      // Make sure the embedded attribute is defined
      Assert.assertNotNull(vertexDAO1.getAttributeIF(embeddedAttributeName));

      // Make sure the embedded object has a character attribute defined.
      String embeddedVertexCharacterName = mdEmbeddedCharacterAttribute.definesAttribute();
      AttributeIF attributeIF = embeddedVertexDAO.getAttributeIF(embeddedVertexCharacterName);
      Assert.assertTrue(attributeIF instanceof AttributeCharacter);

      embeddedVertexDAO.setValue(embeddedVertexCharacterName, "Test Embedded Value");

      vertexDAO1.apply();

      vertexDAO1 = (VertexObjectDAO) VertexObjectDAO.get(mdVertexDAO, vertexDAO1.getOid());

      embeddedVertexDAO = (VertexObjectDAO) vertexDAO1.getEmbeddedComponentDAO(embeddedAttributeName);
      Assert.assertNotNull(embeddedVertexDAO);

      // Make sure the embedded attribute is defined
      Assert.assertNotNull(vertexDAO1.getAttributeIF(embeddedAttributeName));
      attributeIF = embeddedVertexDAO.getAttributeIF(embeddedVertexCharacterName);
      Assert.assertTrue(attributeIF instanceof AttributeCharacter);

      Assert.assertEquals("The character attribute on the embedded object did not persist correctly.", "Test Embedded Value", attributeIF.getValue());

      // Set the date attribute or otherwise it will generate a duplicate data
      // exception as two objects will
      // both have null values
      String dateAttributeName = mdDateAttribute.definesAttribute();
      vertexDAO2 = VertexObjectDAO.newInstance(mdVertexDAO.definesType());
      Calendar cal = TestFixtureFactory.getDate();
      Date value = cal.getTime();
      vertexDAO2.setValue(dateAttributeName, value);
      VertexObjectDAO embeddedVertexDAO2 = (VertexObjectDAO) vertexDAO2.getEmbeddedComponentDAO(embeddedAttributeName);
      embeddedVertexDAO2.setValue(embeddedVertexCharacterName, "Test Embedded Value2");
      vertexDAO2.apply();
    }
    finally
    {
      if (vertexDAO1 != null && vertexDAO1.isAppliedToDB())
      {
        vertexDAO1.delete();
      }

      if (vertexDAO2 != null && vertexDAO2.isAppliedToDB())
      {
        vertexDAO2.delete();
      }
    }
  }

  @Request
  @Test
  public void testDuplicateDataException()
  {
    VertexObjectDAO vertexDAO1 = null;
    VertexObjectDAO vertexDAO2 = null;

    try
    {
      vertexDAO1 = VertexObjectDAO.newInstance(mdVertexDAO.definesType());
      vertexDAO1.apply();

      try
      {
        vertexDAO2 = VertexObjectDAO.newInstance(mdVertexDAO.definesType());
        vertexDAO2.apply();
        Assert.fail("A vertex object with a duplicate unique attribute was incorrectly applied. ");
      }
      catch (RuntimeException runEx)
      {
        Assert.assertTrue(DuplicateDataException.class.getName() + " should have been thrown", runEx instanceof DuplicateDataException);
      }
    }
    finally
    {
      if (vertexDAO1 != null && vertexDAO1.isAppliedToDB())
      {
        vertexDAO1.delete();
      }

      if (vertexDAO2 != null && vertexDAO2.isAppliedToDB())
      {
        vertexDAO2.delete();
      }
    }
  }

  @Request
  @Test
  public void testCharacterAttribute()
  {
    String attributeName = mdCharacterAttribute.definesAttribute();

    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

    String value = "Test Value";

    vertexDAO.setValue(attributeName, value);

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName));

    try
    {
      // Test create
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = "Updated Value";

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
  public void testIntegerAttribute()
  {
    String attributeName = mdIntegerAttribute.definesAttribute();
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

    Integer value = new Integer(5);
    vertexDAO.setValue(attributeName, value);

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName));

    try
    {
      // Test create
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = new Integer(10);

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
  public void testLongAttribute()
  {
    String attributeName = mdLongAttribute.definesAttribute();
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

    Long value = new Long(5);
    vertexDAO.setValue(attributeName, value);

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = new Long(10);

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
  public void testFloatAttribute()
  {
    String attributeName = mdFloatAttribute.definesAttribute();
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

    Float value = new Float(5F);
    vertexDAO.setValue(attributeName, value);

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = new Float(10F);

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
  public void testDoubleAttribute()
  {
    String attributeName = mdDoubleAttribute.definesAttribute();
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

    Double value = new Double(5D);
    vertexDAO.setValue(attributeName, value);

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = new Double(10D);

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
  public void testBooleanAttribute()
  {
    String attributeName = mdBooleanAttribute.definesAttribute();
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

    Boolean value = Boolean.TRUE;
    vertexDAO.setValue(attributeName, value);

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = Boolean.FALSE;

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
  public void testDateAttribute()
  {
    String attributeName = mdDateAttribute.definesAttribute();
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

    Calendar cal = TestFixtureFactory.getDate();

    Date value = cal.getTime();

    vertexDAO.setValue(attributeName, value);

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      cal = TestFixtureFactory.getDate();
      cal.set(Calendar.DAY_OF_MONTH, 1);

      value = cal.getTime();

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
  public void testDateTimeAttribute()
  {
    String attributeName = mdDateTimeAttribute.definesAttribute();
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

    Date value = new Date();
    vertexDAO.setValue(attributeName, value);

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = new Date();

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
  public void testTimeAttribute()
  {
    String attributeName = mdTimeAttribute.definesAttribute();
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

    Date value = new Date();
    vertexDAO.setValue(attributeName, value);

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = new Date();

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

  @Request
  @Test
  public void testLocalCharacterAttribute()
  {
    String attributeName = mdLocalCharacterAttribute.definesAttribute();
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    AttributeLocalEmbedded attribute = (AttributeLocalEmbedded) vertexDAO.getAttribute(attributeName);

    Assert.assertNotNull(attribute);

    String value = "Test";
    attribute.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, value);

    Assert.assertEquals(value, attribute.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getEmbeddedComponentDAO(attributeName).getValue(MdAttributeLocalInfo.DEFAULT_LOCALE));

      // Test update
      value = "Update";

      attribute.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, value);
      vertexDAO.apply();

      test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertEquals(value, test.getEmbeddedComponentDAO(attributeName).getValue(MdAttributeLocalInfo.DEFAULT_LOCALE));
    }
    finally
    {
      vertexDAO.delete();
    }

    Assert.assertNull(VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid()));
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
  public void testPolygonAttribute()
  {
    String attributeName = mdPolygonAttribute.definesAttribute();
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

    Polygon value = TestFixtureFactory.getPolygon();

    vertexDAO.setValue(attributeName, value);

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = TestFixtureFactory.getPolygon2();

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
  public void testLineStringAttribute()
  {
    String attributeName = mdLineStringAttribute.definesAttribute();
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

    LineString value = TestFixtureFactory.getLineString();

    vertexDAO.setValue(attributeName, value);

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

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
  public void testMultiPointAttribute()
  {
    String attributeName = mdMultiPointAttribute.definesAttribute();
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

    MultiPoint value = TestFixtureFactory.getMultiPoint();

    vertexDAO.setValue(attributeName, value);

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

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
  public void testMultiPolygonAttribute()
  {
    String attributeName = mdMultiPolygonAttribute.definesAttribute();
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

    MultiPolygon value = TestFixtureFactory.getMultiPolygon();

    vertexDAO.setValue(attributeName, value);

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

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
  public void testMultiLineStringAttribute()
  {
    String attributeName = mdMultiLineStringAttribute.definesAttribute();
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

    MultiLineString value = TestFixtureFactory.getMultiLineString();

    vertexDAO.setValue(attributeName, value);

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

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
  public void testReferenceAttribute()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusinessDAO.definesType());
    businessDAO.apply();

    try
    {
      String attributeName = mdReferenceAttribute.definesAttribute();
      VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

      Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

      String value = businessDAO.getOid();
      vertexDAO.setValue(attributeName, value);

      Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName));

      try
      {
        vertexDAO.apply();

        VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

        Assert.assertNotNull(test);

        Assert.assertEquals(value, test.getObjectValue(attributeName));
      }
      finally
      {
        vertexDAO.delete();
      }

      Assert.assertNull(VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid()));
    }
    finally
    {
      businessDAO.delete();
    }
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testEnumerationAttribute()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdEnumMasterDAO.definesType());
    businessDAO.setValue(EnumerationMasterInfo.NAME, "test");
    businessDAO.apply();

    try
    {
      String attributeName = mdEnumerationAttribute.definesAttribute();
      VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

      Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

      String value = businessDAO.getOid();

      vertexDAO.addItem(attributeName, value);

      try
      {
        vertexDAO.apply();

        VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

        Assert.assertNotNull(test);

        Set<String> set = (Set<String>) test.getObjectValue(attributeName);

        Assert.assertTrue(set.contains(value));
      }
      finally
      {
        vertexDAO.delete();
      }

      Assert.assertNull(VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid()));
    }
    finally
    {
      businessDAO.delete();
    }
  }

  @Request
  @Test(expected = AttributeCharacterParseException.class)
  public void testBadCharacterAttribute()
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    vertexDAO.setValue(mdCharacterAttribute.definesAttribute(), new Integer(4));
  }

  @Request
  @Test(expected = AttributeIntegerParseException.class)
  public void testBadIntegerAttribute()
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    vertexDAO.setValue(mdIntegerAttribute.definesAttribute(), "Test Value");
  }

  @Request
  @Test(expected = AttributeIntegerParseException.class)
  public void testBadLongAttribute()
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    vertexDAO.setValue(mdLongAttribute.definesAttribute(), "Test Value");
  }

  @Request
  @Test(expected = AttributeIntegerParseException.class)
  public void testBadFloatAttribute()
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    vertexDAO.setValue(mdFloatAttribute.definesAttribute(), "Test Value");
  }

  @Request
  @Test(expected = AttributeIntegerParseException.class)
  public void testBadDoubleAttribute()
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    vertexDAO.setValue(mdDoubleAttribute.definesAttribute(), "Test Value");
  }

  @Request
  @Test(expected = AttributeIntegerParseException.class)
  public void testBadBooleanAttribute()
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    vertexDAO.setValue(mdBooleanAttribute.definesAttribute(), "Test Value");
  }

  @Request
  @Test(expected = AttributeIntegerParseException.class)
  public void testBadDateAttribute()
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    vertexDAO.setValue(mdDateAttribute.definesAttribute(), "Test Value");
  }

  @Request
  @Test(expected = AttributeIntegerParseException.class)
  public void testBadDateTimeAttribute()
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    vertexDAO.setValue(mdDateTimeAttribute.definesAttribute(), "Test Value");
  }

  @Request
  @Test(expected = AttributeIntegerParseException.class)
  public void testBadTimeAttribute()
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    vertexDAO.setValue(mdTimeAttribute.definesAttribute(), "Test Value");
  }

  @Request
  @Test(expected = AttributePointParseException.class)
  public void testBadPointAttribute()
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    vertexDAO.setValue(mdPointAttribute.definesAttribute(), "Test Value");
  }

  @Request
  @Test(expected = AttributePolygonParseException.class)
  public void testBadPolygonAttribute()
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    vertexDAO.setValue(mdPolygonAttribute.definesAttribute(), "Test Value");
  }

  @Request
  @Test(expected = AttributeLineStringParseException.class)
  public void testBadLineStringAttribute()
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    vertexDAO.setValue(mdLineStringAttribute.definesAttribute(), "Test Value");
  }

  @Request
  @Test(expected = AttributeMultiPointParseException.class)
  public void testBadMultiPointAttribute()
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    vertexDAO.setValue(mdMultiPointAttribute.definesAttribute(), "Test Value");
  }

  @Request
  @Test(expected = AttributeMultiPolygonParseException.class)
  public void testBadMultiPolygonAttribute()
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    vertexDAO.setValue(mdMultiPolygonAttribute.definesAttribute(), "Test Value");
  }

  @Request
  @Test(expected = AttributeMultiLineStringParseException.class)
  public void testBadMultiLineStringAttribute()
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    vertexDAO.setValue(mdMultiLineStringAttribute.definesAttribute(), "Test Value");
  }

  @Request
  @Test
  public void testGraphReferenceAttribute()
  {
    VertexObjectDAO classifierDAO = VertexObjectDAO.newInstance(mdReferenceDAO.definesType());

    try
    {
      classifierDAO.apply();

      String attributeName = mdGraphReferenceAttribute.definesAttribute();
      VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

      Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

      vertexDAO.setValue(attributeName, classifierDAO);

      Assert.assertEquals(classifierDAO.getOid(), vertexDAO.getObjectValue(attributeName));

      try
      {
        // Test create
        vertexDAO.apply();

        VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

        Assert.assertNotNull(test);

        Assert.assertEquals(classifierDAO.getOid(), test.getObjectValue(attributeName));
      }
      finally
      {
        vertexDAO.delete();
      }

      Assert.assertNull(VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid()));
    }
    finally
    {
      classifierDAO.delete();
    }
  }

  @Request
  @Test
  public void testClassificationAttribute()
  {
    String attributeName = mdClassificationAttribute.definesAttribute();
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

    vertexDAO.setValue(attributeName, attributeRoot);

    Assert.assertEquals(attributeRoot.getOid(), vertexDAO.getObjectValue(attributeName));

    try
    {
      // Test create
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(attributeRoot.getOid(), test.getObjectValue(attributeName));
    }
    finally
    {
      vertexDAO.delete();
    }

    Assert.assertNull(VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid()));
  }

}
