package com.runwaysdk.dataaccess.graph;

import java.util.Calendar;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.AttributeCharacterParseException;
import com.runwaysdk.AttributeIntegerParseException;
import com.runwaysdk.constants.LocalProperties;
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

}
