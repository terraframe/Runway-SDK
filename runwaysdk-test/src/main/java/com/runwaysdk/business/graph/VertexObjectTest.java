package com.runwaysdk.business.graph;

import java.util.Date;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

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

    VertexObject vertexObject = new VertexObject(mdVertexDAO.definesType());

    String value = "Test Value";

    vertexObject.setValue(attributeName, value);

    Assert.assertEquals(value, vertexObject.getObjectValue(attributeName));
  }

  @Request
  @Test
  public void testIntegerAttribute()
  {
    String attributeName = mdIntegerAttribute.definesAttribute();
    VertexObject vertexObject = new VertexObject(mdVertexDAO.definesType());

    Integer value = new Integer(5);
    vertexObject.setValue(attributeName, value);

    Assert.assertEquals(value, vertexObject.getObjectValue(attributeName));
  }

  @Request
  @Test
  public void testLongAttribute()
  {
    String attributeName = mdLongAttribute.definesAttribute();
    VertexObject vertexObject = new VertexObject(mdVertexDAO.definesType());

    Long value = new Long(5);
    vertexObject.setValue(attributeName, value);

    Assert.assertEquals(value, vertexObject.getObjectValue(attributeName));
  }

  @Request
  @Test
  public void testFloatAttribute()
  {
    String attributeName = mdFloatAttribute.definesAttribute();
    VertexObject vertexObject = new VertexObject(mdVertexDAO.definesType());

    Float value = new Float(5F);
    vertexObject.setValue(attributeName, value);

    Assert.assertEquals(value, vertexObject.getObjectValue(attributeName));
  }

  @Request
  @Test
  public void testDoubleAttribute()
  {
    String attributeName = mdDoubleAttribute.definesAttribute();
    VertexObject vertexObject = new VertexObject(mdVertexDAO.definesType());

    Double value = new Double(5D);
    vertexObject.setValue(attributeName, value);

    Assert.assertEquals(value, vertexObject.getObjectValue(attributeName));
  }

  @Request
  @Test
  public void testBooleanAttribute()
  {
    String attributeName = mdBooleanAttribute.definesAttribute();
    VertexObject vertexObject = new VertexObject(mdVertexDAO.definesType());

    Boolean value = Boolean.TRUE;
    vertexObject.setValue(attributeName, value);

    Assert.assertEquals(value, vertexObject.getObjectValue(attributeName));
  }

  @Request
  @Test
  public void testDateAttribute()
  {
    String attributeName = mdDateAttribute.definesAttribute();
    VertexObject vertexObject = new VertexObject(mdVertexDAO.definesType());

    Date value = new Date();

    vertexObject.setValue(attributeName, value);

    Assert.assertEquals(value, vertexObject.getObjectValue(attributeName));
  }

  @Request
  @Test
  public void testDateTimeAttribute()
  {
    String attributeName = mdDateTimeAttribute.definesAttribute();
    VertexObject vertexObject = new VertexObject(mdVertexDAO.definesType());

    Date value = new Date();
    vertexObject.setValue(attributeName, value);

    Assert.assertEquals(value, vertexObject.getObjectValue(attributeName));
  }

  @Request
  @Test
  public void testTimeAttribute()
  {
    String attributeName = mdTimeAttribute.definesAttribute();
    VertexObject vertexObject = new VertexObject(mdVertexDAO.definesType());

    Date value = new Date();
    vertexObject.setValue(attributeName, value);

    Assert.assertEquals(value, vertexObject.getObjectValue(attributeName));
  }

  @Request
  @Test
  public void testPointAttribute()
  {
    String attributeName = mdPointAttribute.definesAttribute();
    VertexObject vertexObject = new VertexObject(mdVertexDAO.definesType());

    Point value = TestFixtureFactory.getPoint();

    vertexObject.setValue(attributeName, value);

    Assert.assertEquals(value, vertexObject.getObjectValue(attributeName));
  }

  @Request
  @Test
  public void testPolygonAttribute()
  {
    String attributeName = mdPolygonAttribute.definesAttribute();
    VertexObject vertexObject = new VertexObject(mdVertexDAO.definesType());

    Polygon value = TestFixtureFactory.getPolygon();

    vertexObject.setValue(attributeName, value);

    Assert.assertEquals(value, vertexObject.getObjectValue(attributeName));
  }

  @Request
  @Test
  public void testLineStringAttribute()
  {
    String attributeName = mdLineStringAttribute.definesAttribute();
    VertexObject vertexObject = new VertexObject(mdVertexDAO.definesType());

    LineString value = TestFixtureFactory.getLineString();

    vertexObject.setValue(attributeName, value);

    Assert.assertEquals(value, vertexObject.getObjectValue(attributeName));
  }

  @Request
  @Test
  public void testMultiPointAttribute()
  {
    String attributeName = mdMultiPointAttribute.definesAttribute();
    VertexObject vertexObject = new VertexObject(mdVertexDAO.definesType());

    MultiPoint value = TestFixtureFactory.getMultiPoint();

    vertexObject.setValue(attributeName, value);

    Assert.assertEquals(value, vertexObject.getObjectValue(attributeName));
  }

  @Request
  @Test
  public void testMultiPolygonAttribute()
  {
    String attributeName = mdMultiPolygonAttribute.definesAttribute();
    VertexObject vertexObject = new VertexObject(mdVertexDAO.definesType());

    MultiPolygon value = TestFixtureFactory.getMultiPolygon();

    vertexObject.setValue(attributeName, value);

    Assert.assertEquals(value, vertexObject.getObjectValue(attributeName));
  }

  @Request
  @Test
  public void testMultiLineStringAttribute()
  {
    String attributeName = mdMultiLineStringAttribute.definesAttribute();
    VertexObject vertexObject = new VertexObject(mdVertexDAO.definesType());

    MultiLineString value = TestFixtureFactory.getMultiLineString();

    vertexObject.setValue(attributeName, value);

    Assert.assertEquals(value, vertexObject.getObjectValue(attributeName));
  }
}
