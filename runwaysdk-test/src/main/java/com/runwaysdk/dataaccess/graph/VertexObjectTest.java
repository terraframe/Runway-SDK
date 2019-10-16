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
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.session.Request;

public class VertexObjectTest
{
  private static MdVertexDAO             mdVertexDAO;

  private static MdAttributeCharacterDAO mdCharacterAttribute;

  private static MdAttributeIntegerDAO   mdIntegerAttribute;

  private static MdAttributeLongDAO      mdLongAttribute;

  private static MdAttributeFloatDAO     mdFloatAttribute;

  private static MdAttributeDoubleDAO    mdDoubleAttribute;

  private static MdAttributeBooleanDAO   mdBooleanAttribute;

  private static MdAttributeDateDAO      mdDateAttribute;

  private static MdAttributeDateTimeDAO  mdDateTimeAttribute;

  private static MdAttributeTimeDAO      mdTimeAttribute;

  @Request
  @BeforeClass
  public static void classSetup()
  {
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
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdVertexDAO);
  }

  @Request
  @Test
  public void testCharacterAttribute()
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    AttributeIF test = vertexDAO.getAttributeIF(mdCharacterAttribute.definesAttribute());

    Assert.assertNotNull(test);

    vertexDAO.setValue(mdCharacterAttribute.definesAttribute(), "Test Value");
  }

  @Request
  @Test
  public void testIntegerAttribute()
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    AttributeIF test = vertexDAO.getAttributeIF(mdIntegerAttribute.definesAttribute());

    Assert.assertNotNull(test);

    vertexDAO.setValue(mdIntegerAttribute.definesAttribute(), new Integer(5));
  }

  @Request
  @Test
  public void testLongAttribute()
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    AttributeIF test = vertexDAO.getAttributeIF(mdLongAttribute.definesAttribute());

    Assert.assertNotNull(test);

    vertexDAO.setValue(mdLongAttribute.definesAttribute(), new Long(5));
  }

  @Request
  @Test
  public void testFloatAttribute()
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    AttributeIF test = vertexDAO.getAttributeIF(mdFloatAttribute.definesAttribute());

    Assert.assertNotNull(test);

    vertexDAO.setValue(mdFloatAttribute.definesAttribute(), new Float(5F));
  }

  @Request
  @Test
  public void testDoubleAttribute()
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    AttributeIF test = vertexDAO.getAttributeIF(mdDoubleAttribute.definesAttribute());

    Assert.assertNotNull(test);

    vertexDAO.setValue(mdDoubleAttribute.definesAttribute(), new Double(5D));
  }

  @Request
  @Test
  public void testBooleanAttribute()
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    AttributeIF test = vertexDAO.getAttributeIF(mdBooleanAttribute.definesAttribute());

    Assert.assertNotNull(test);

    vertexDAO.setValue(mdBooleanAttribute.definesAttribute(), Boolean.TRUE);
  }

  @Request
  @Test
  public void testDateAttribute()
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    AttributeIF test = vertexDAO.getAttributeIF(mdDateAttribute.definesAttribute());

    Assert.assertNotNull(test);

    vertexDAO.setValue(mdDateAttribute.definesAttribute(), new Date());
  }

  @Request
  @Test
  public void testDateTimeAttribute()
  {

    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    AttributeIF test = vertexDAO.getAttributeIF(mdDateTimeAttribute.definesAttribute());

    Assert.assertNotNull(test);

    vertexDAO.setValue(mdDateTimeAttribute.definesAttribute(), new Date());
  }

  @Request
  @Test
  public void testTimeAttribute()
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

    AttributeIF test = vertexDAO.getAttributeIF(mdTimeAttribute.definesAttribute());

    Assert.assertNotNull(test);

    vertexDAO.setValue(mdTimeAttribute.definesAttribute(), new Date());
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
}
