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
package com.runwaysdk.dataaccess.graph;

import java.util.Calendar;
import java.util.Date;
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
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.attributes.AttributeFrequencyException;
import com.runwaysdk.dataaccess.graph.attributes.AttributeLocalEmbedded;
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
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalCharacterEmbeddedDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePointDAO;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.graph.ChangeFrequency;
import com.vividsolutions.jts.geom.Point;

public class VertexObjectDAOChangeOverTimeTest
{
  private static MdVertexDAO                          mdVertexDAO;

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

  private static MdAttributeLocalCharacterEmbeddedDAO mdLocalCharacterAttribute;

  private static MdAttributePointDAO                  mdPointAttribute;

  private static MdAttributeEnumerationDAO            mdEnumerationAttribute;

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
    mdEnumMasterDAO = TestFixtureFactory.createEnumClass1();
    mdEnumMasterDAO.apply();

    mdEnumerationDAO = TestFixtureFactory.createMdEnumeation1(mdEnumMasterDAO);
    mdEnumerationDAO.apply();

    mdVertexDAO = TestFixtureFactory.createMdVertex();
    mdVertexDAO.setValue(MdVertexInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.TRUE);
    mdVertexDAO.addItem(MdVertexInfo.FREQUENCY, ChangeFrequency.ANNUAL.getOid());
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

    mdTextAttribute = TestFixtureFactory.addTextAttribute(mdVertexDAO);
    mdTextAttribute.apply();

    mdLocalCharacterAttribute = TestFixtureFactory.addLocalCharacterEmbeddedAttribute(mdVertexDAO);
    mdLocalCharacterAttribute.apply();

    mdPointAttribute = TestFixtureFactory.addPointAttribute(mdVertexDAO);
    mdPointAttribute.apply();

    mdEnumerationAttribute = TestFixtureFactory.addEnumerationAttribute(mdVertexDAO, mdEnumerationDAO);
    mdEnumerationAttribute.apply();
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
  }

  public Date startDate()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.clear();
    calendar.set(2012, Calendar.JANUARY, 1);

    return calendar.getTime();
  }

  public Date endDate()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.clear();
    calendar.set(2014, Calendar.DECEMBER, 31);

    return calendar.getTime();
  }

  public Date date()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.clear();
    calendar.set(2012, Calendar.MAY, 13);

    return calendar.getTime();
  }

  @Request
  @Test
  public void testCharacterAttribute()
  {
    String attributeName = mdCharacterAttribute.definesAttribute();

    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

    String value = "Test Value";

    vertexDAO.setValue(attributeName, value, startDate(), endDate());

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName, date()));
    Assert.assertEquals(1, vertexDAO.getValuesOverTime(attributeName).size());

    try
    {
      // Test create
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName, date()));
      Assert.assertEquals(1, test.getValuesOverTime(attributeName).size());

      // Test update
      value = "Updated Value";

      vertexDAO.getValuesOverTime(attributeName).get(0).setValue(value);
      vertexDAO.apply();

      test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName, date()));
      Assert.assertEquals(1, test.getValuesOverTime(attributeName).size());

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
    vertexDAO.setValue(attributeName, value, startDate(), endDate());

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName, date()));

    try
    {
      // Test create
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName, date()));
      Assert.assertEquals(1, test.getValuesOverTime(attributeName).size());

      // Test update
      value = new Integer(10);

      vertexDAO.getValuesOverTime(attributeName).get(0).setValue(value);
      vertexDAO.apply();

      test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName, date()));
      Assert.assertEquals(1, test.getValuesOverTime(attributeName).size());
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
    vertexDAO.setValue(attributeName, value, startDate(), endDate());

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName, date()));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName, date()));
      Assert.assertEquals(1, test.getValuesOverTime(attributeName).size());

      // Test update
      value = new Long(10);

      vertexDAO.getValuesOverTime(attributeName).get(0).setValue(value);
      vertexDAO.apply();

      test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName, date()));
      Assert.assertEquals(1, test.getValuesOverTime(attributeName).size());
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
    vertexDAO.setValue(attributeName, value, startDate(), endDate());

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName, date()));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName, date()));
      Assert.assertEquals(1, test.getValuesOverTime(attributeName).size());

      // Test update
      value = new Float(10F);

      vertexDAO.getValuesOverTime(attributeName).get(0).setValue(value);
      vertexDAO.apply();

      test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName, date()));
      Assert.assertEquals(1, test.getValuesOverTime(attributeName).size());
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
    vertexDAO.setValue(attributeName, value, startDate(), endDate());

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName, date()));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName, date()));
      Assert.assertEquals(1, test.getValuesOverTime(attributeName).size());

      // Test update
      value = new Double(10D);

      vertexDAO.getValuesOverTime(attributeName).get(0).setValue(value);
      vertexDAO.apply();

      test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName, date()));
      Assert.assertEquals(1, test.getValuesOverTime(attributeName).size());
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
    vertexDAO.setValue(attributeName, value, startDate(), endDate());

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName, date()));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName, date()));
      Assert.assertEquals(1, test.getValuesOverTime(attributeName).size());

      // Test update
      value = Boolean.FALSE;

      vertexDAO.getValuesOverTime(attributeName).get(0).setValue(value);
      vertexDAO.apply();

      test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName, date()));
      Assert.assertEquals(1, test.getValuesOverTime(attributeName).size());
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

    vertexDAO.setValue(attributeName, value, startDate(), endDate());

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName, date()));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName, date()));
      Assert.assertEquals(1, test.getValuesOverTime(attributeName).size());

      // Test update
      cal = TestFixtureFactory.getDate();
      cal.set(Calendar.DAY_OF_MONTH, 1);

      value = cal.getTime();

      vertexDAO.getValuesOverTime(attributeName).get(0).setValue(value);
      vertexDAO.apply();

      test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName, date()));
      Assert.assertEquals(1, test.getValuesOverTime(attributeName).size());
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
    vertexDAO.setValue(attributeName, value, startDate(), endDate());

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName, date()));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName, date()));
      Assert.assertEquals(1, test.getValuesOverTime(attributeName).size());

      // Test update
      value = new Date();

      vertexDAO.getValuesOverTime(attributeName).get(0).setValue(value);
      vertexDAO.apply();

      test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName, date()));
      Assert.assertEquals(1, test.getValuesOverTime(attributeName).size());
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
    vertexDAO.setValue(attributeName, value, startDate(), endDate());

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName, date()));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName, date()));
      Assert.assertEquals(1, test.getValuesOverTime(attributeName).size());

      // Test update
      value = new Date();

      vertexDAO.getValuesOverTime(attributeName).get(0).setValue(value);
      vertexDAO.apply();

      test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName, date()));
      Assert.assertEquals(1, test.getValuesOverTime(attributeName).size());
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
    vertexDAO.setValue(attributeName, value, startDate(), endDate());

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName, date()));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName, date()));
      Assert.assertEquals(1, test.getValuesOverTime(attributeName).size());

      // Test update
      value = "Update";

      vertexDAO.getValuesOverTime(attributeName).get(0).setValue(value);
      vertexDAO.apply();

      test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName, date()));
      Assert.assertEquals(1, test.getValuesOverTime(attributeName).size());
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
    attribute.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, value, startDate(), endDate());

    Assert.assertEquals(value, attribute.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE, date()));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      AttributeLocalEmbedded testAttribute = (AttributeLocalEmbedded) test.getAttributeIF(attributeName);

      Assert.assertEquals(value, testAttribute.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE, date()));

      // Test update
      value = "Update";

      attribute.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, value, startDate(), endDate());
      vertexDAO.apply();

      test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      testAttribute = (AttributeLocalEmbedded) test.getAttributeIF(attributeName);

      Assert.assertEquals(value, testAttribute.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE, date()));
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

    vertexDAO.setValue(attributeName, value, startDate(), endDate());

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName, date()));

    try
    {
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName, date()));

      // Test update
      value = TestFixtureFactory.getPoint2();

      vertexDAO.setValue(attributeName, value, startDate(), endDate());
      vertexDAO.apply();

      test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName, date()));
    }
    finally
    {
      vertexDAO.delete();
    }

    Assert.assertNull(VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid()));
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

      vertexDAO.setValue(attributeName, value, startDate(), endDate());

      try
      {
        vertexDAO.apply();

        VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

        Assert.assertNotNull(test);

        Set<String> set = (Set<String>) test.getObjectValue(attributeName, date());

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
  @Test(expected = AttributeFrequencyException.class)
  public void testBadFrequency()
  {
    String attributeName = mdFloatAttribute.definesAttribute();
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    vertexDAO.setValue(attributeName, new Float(5F), new Date(), new Date());
  }

}
