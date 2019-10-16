package com.runwaysdk.dataaccess.graph;

import java.util.Date;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.AttributeCharacterParseException;
import com.runwaysdk.AttributeIntegerParseException;
import com.runwaysdk.dataaccess.AttributeIF;
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
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.session.Request;

public class EdgeObjectTest
{
  private static MdVertexDAO             mdParentDAO;

  private static MdVertexDAO             mdChildDAO;

  private static MdEdgeDAO               mdEdgeDAO;

  private static MdAttributeCharacterDAO mdCharacterAttribute;

  private static MdAttributeIntegerDAO   mdIntegerAttribute;

  private static MdAttributeLongDAO      mdLongAttribute;

  private static MdAttributeFloatDAO     mdFloatAttribute;

  private static MdAttributeDoubleDAO    mdDoubleAttribute;

  private static MdAttributeBooleanDAO   mdBooleanAttribute;

  private static MdAttributeDateDAO      mdDateAttribute;

  private static MdAttributeDateTimeDAO  mdDateTimeAttribute;

  private static MdAttributeTimeDAO      mdTimeAttribute;

  private static VertexObjectDAO         parent;

  private static VertexObjectDAO         child;

  @Request
  @BeforeClass
  public static void classSetup()
  {
    mdParentDAO = TestFixtureFactory.createMdVertex("TestParent");
    mdParentDAO.apply();

    mdChildDAO = TestFixtureFactory.createMdVertex("TestChild");
    mdChildDAO.apply();

    mdEdgeDAO = TestFixtureFactory.createMdEdge(mdParentDAO, mdChildDAO);
    mdEdgeDAO.apply();

    mdCharacterAttribute = TestFixtureFactory.addCharacterAttribute(mdEdgeDAO);
    mdCharacterAttribute.apply();

    mdIntegerAttribute = TestFixtureFactory.addIntegerAttribute(mdEdgeDAO);
    mdIntegerAttribute.apply();

    mdLongAttribute = TestFixtureFactory.addLongAttribute(mdEdgeDAO);
    mdLongAttribute.apply();

    mdFloatAttribute = TestFixtureFactory.addFloatAttribute(mdEdgeDAO);
    mdFloatAttribute.apply();

    mdDoubleAttribute = TestFixtureFactory.addDoubleAttribute(mdEdgeDAO);
    mdDoubleAttribute.apply();

    mdBooleanAttribute = TestFixtureFactory.addBooleanAttribute(mdEdgeDAO);
    mdBooleanAttribute.apply();

    mdDateAttribute = TestFixtureFactory.addDateAttribute(mdEdgeDAO);
    mdDateAttribute.apply();

    mdDateTimeAttribute = TestFixtureFactory.addDateTimeAttribute(mdEdgeDAO);
    mdDateTimeAttribute.apply();

    mdTimeAttribute = TestFixtureFactory.addTimeAttribute(mdEdgeDAO);
    mdTimeAttribute.apply();

    parent = VertexObjectDAO.newInstance(mdParentDAO);

    child = VertexObjectDAO.newInstance(mdChildDAO);
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdEdgeDAO);
    TestFixtureFactory.delete(mdChildDAO);
    TestFixtureFactory.delete(mdParentDAO);
  }

  @Request
  @Test
  public void testNewInstance()
  {
    EdgeObjectDAO edgeDAO = EdgeObjectDAO.newInstance(parent.getOid(), child.getOid(), mdEdgeDAO.definesType());

    Assert.assertNotNull(edgeDAO);
    Assert.assertEquals(parent.getOid(), edgeDAO.getParentOid());
    Assert.assertEquals(child.getOid(), edgeDAO.getChildOid());
  }

  @Request
  @Test
  public void testCharacterAttribute()
  {
    EdgeObjectDAO edgeDAO = EdgeObjectDAO.newInstance(parent.getOid(), child.getOid(), mdEdgeDAO.definesType());

    String attributeName = mdCharacterAttribute.definesAttribute();

    AttributeIF test = edgeDAO.getAttributeIF(attributeName);

    Assert.assertNotNull(test);

    String value = "Test Value";

    edgeDAO.setValue(attributeName, value);

    Assert.assertEquals(value, edgeDAO.getObjectValue(attributeName));
  }

  @Request
  @Test
  public void testIntegerAttribute()
  {
    String attributeName = mdIntegerAttribute.definesAttribute();
    EdgeObjectDAO edgeDAO = EdgeObjectDAO.newInstance(parent.getOid(), child.getOid(), mdEdgeDAO.definesType());

    AttributeIF test = edgeDAO.getAttributeIF(attributeName);

    Assert.assertNotNull(test);

    Integer value = new Integer(5);
    edgeDAO.setValue(attributeName, value);

    Assert.assertEquals(value, edgeDAO.getObjectValue(attributeName));
  }

  @Request
  @Test
  public void testLongAttribute()
  {
    String attributeName = mdLongAttribute.definesAttribute();
    EdgeObjectDAO edgeDAO = EdgeObjectDAO.newInstance(parent.getOid(), child.getOid(), mdEdgeDAO.definesType());

    AttributeIF test = edgeDAO.getAttributeIF(attributeName);

    Assert.assertNotNull(test);

    Long value = new Long(5);
    edgeDAO.setValue(attributeName, value);

    Assert.assertEquals(value, edgeDAO.getObjectValue(attributeName));
  }

  @Request
  @Test
  public void testFloatAttribute()
  {
    String attributeName = mdFloatAttribute.definesAttribute();
    EdgeObjectDAO edgeDAO = EdgeObjectDAO.newInstance(parent.getOid(), child.getOid(), mdEdgeDAO.definesType());

    AttributeIF test = edgeDAO.getAttributeIF(attributeName);

    Assert.assertNotNull(test);

    Float value = new Float(5F);
    edgeDAO.setValue(attributeName, value);

    Assert.assertEquals(value, edgeDAO.getObjectValue(attributeName));
  }

  @Request
  @Test
  public void testDoubleAttribute()
  {
    String attributeName = mdDoubleAttribute.definesAttribute();
    EdgeObjectDAO edgeDAO = EdgeObjectDAO.newInstance(parent.getOid(), child.getOid(), mdEdgeDAO.definesType());

    AttributeIF test = edgeDAO.getAttributeIF(attributeName);

    Assert.assertNotNull(test);

    Double value = new Double(5D);
    edgeDAO.setValue(attributeName, value);

    Assert.assertEquals(value, edgeDAO.getObjectValue(attributeName));
  }

  @Request
  @Test
  public void testBooleanAttribute()
  {
    String attributeName = mdBooleanAttribute.definesAttribute();
    EdgeObjectDAO edgeDAO = EdgeObjectDAO.newInstance(parent.getOid(), child.getOid(), mdEdgeDAO.definesType());

    AttributeIF test = edgeDAO.getAttributeIF(attributeName);

    Assert.assertNotNull(test);

    Boolean value = Boolean.TRUE;
    edgeDAO.setValue(attributeName, value);

    Assert.assertEquals(value, edgeDAO.getObjectValue(attributeName));
  }

  @Request
  @Test
  public void testDateAttribute()
  {
    String attributeName = mdDateAttribute.definesAttribute();
    EdgeObjectDAO edgeDAO = EdgeObjectDAO.newInstance(parent.getOid(), child.getOid(), mdEdgeDAO.definesType());

    AttributeIF test = edgeDAO.getAttributeIF(attributeName);

    Assert.assertNotNull(test);

    Date value = new Date();

    edgeDAO.setValue(attributeName, value);

    Assert.assertEquals(value, edgeDAO.getObjectValue(attributeName));
  }

  @Request
  @Test
  public void testDateTimeAttribute()
  {
    String attributeName = mdDateTimeAttribute.definesAttribute();
    EdgeObjectDAO edgeDAO = EdgeObjectDAO.newInstance(parent.getOid(), child.getOid(), mdEdgeDAO.definesType());

    AttributeIF test = edgeDAO.getAttributeIF(attributeName);

    Assert.assertNotNull(test);

    Date value = new Date();
    edgeDAO.setValue(attributeName, value);

    Assert.assertEquals(value, edgeDAO.getObjectValue(attributeName));
  }

  @Request
  @Test
  public void testTimeAttribute()
  {
    String attributeName = mdTimeAttribute.definesAttribute();
    EdgeObjectDAO edgeDAO = EdgeObjectDAO.newInstance(parent.getOid(), child.getOid(), mdEdgeDAO.definesType());

    AttributeIF test = edgeDAO.getAttributeIF(attributeName);

    Assert.assertNotNull(test);

    Date value = new Date();
    edgeDAO.setValue(attributeName, value);

    Assert.assertEquals(value, edgeDAO.getObjectValue(attributeName));
  }

  @Request
  @Test(expected = AttributeCharacterParseException.class)
  public void testBadCharacterAttribute()
  {
    EdgeObjectDAO edgeDAO = EdgeObjectDAO.newInstance(parent.getOid(), child.getOid(), mdEdgeDAO.definesType());

    edgeDAO.setValue(mdCharacterAttribute.definesAttribute(), new Integer(4));
  }

  @Request
  @Test(expected = AttributeIntegerParseException.class)
  public void testBadIntegerAttribute()
  {
    EdgeObjectDAO edgeDAO = EdgeObjectDAO.newInstance(parent.getOid(), child.getOid(), mdEdgeDAO.definesType());

    edgeDAO.setValue(mdIntegerAttribute.definesAttribute(), "Test Value");
  }

  @Request
  @Test(expected = AttributeIntegerParseException.class)
  public void testBadLongAttribute()
  {
    EdgeObjectDAO edgeDAO = EdgeObjectDAO.newInstance(parent.getOid(), child.getOid(), mdEdgeDAO.definesType());

    edgeDAO.setValue(mdLongAttribute.definesAttribute(), "Test Value");
  }

  @Request
  @Test(expected = AttributeIntegerParseException.class)
  public void testBadFloatAttribute()
  {
    EdgeObjectDAO edgeDAO = EdgeObjectDAO.newInstance(parent.getOid(), child.getOid(), mdEdgeDAO.definesType());

    edgeDAO.setValue(mdFloatAttribute.definesAttribute(), "Test Value");
  }

  @Request
  @Test(expected = AttributeIntegerParseException.class)
  public void testBadDoubleAttribute()
  {
    EdgeObjectDAO edgeDAO = EdgeObjectDAO.newInstance(parent.getOid(), child.getOid(), mdEdgeDAO.definesType());

    edgeDAO.setValue(mdDoubleAttribute.definesAttribute(), "Test Value");
  }

  @Request
  @Test(expected = AttributeIntegerParseException.class)
  public void testBadBooleanAttribute()
  {
    EdgeObjectDAO edgeDAO = EdgeObjectDAO.newInstance(parent.getOid(), child.getOid(), mdEdgeDAO.definesType());

    edgeDAO.setValue(mdBooleanAttribute.definesAttribute(), "Test Value");
  }

  @Request
  @Test(expected = AttributeIntegerParseException.class)
  public void testBadDateAttribute()
  {
    EdgeObjectDAO edgeDAO = EdgeObjectDAO.newInstance(parent.getOid(), child.getOid(), mdEdgeDAO.definesType());

    edgeDAO.setValue(mdDateAttribute.definesAttribute(), "Test Value");
  }

  @Request
  @Test(expected = AttributeIntegerParseException.class)
  public void testBadDateTimeAttribute()
  {
    EdgeObjectDAO edgeDAO = EdgeObjectDAO.newInstance(parent.getOid(), child.getOid(), mdEdgeDAO.definesType());

    edgeDAO.setValue(mdDateTimeAttribute.definesAttribute(), "Test Value");
  }

  @Request
  @Test(expected = AttributeIntegerParseException.class)
  public void testBadTimeAttribute()
  {
    EdgeObjectDAO edgeDAO = EdgeObjectDAO.newInstance(parent.getOid(), child.getOid(), mdEdgeDAO.definesType());

    edgeDAO.setValue(mdTimeAttribute.definesAttribute(), "Test Value");
  }
}
