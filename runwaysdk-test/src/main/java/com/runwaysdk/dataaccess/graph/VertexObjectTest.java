package com.runwaysdk.dataaccess.graph;

import org.junit.Assert;
import org.junit.Test;

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
  @Request
  @Test
  public void testCharacterAttribute()
  {
    MdVertexDAO mdVertexDAO = TestFixtureFactory.createMdVertex();
    mdVertexDAO.apply();

    try
    {
      MdAttributeCharacterDAO mdCharacterAttribute = TestFixtureFactory.addCharacterAttribute(mdVertexDAO);
      mdCharacterAttribute.apply();

      VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

      // Print the attributes. This will be moved into its own test.
      AttributeIF test = vertexDAO.getAttributeIF(mdCharacterAttribute.definesAttribute());

      Assert.assertNotNull(test);
    }
    finally
    {
      mdVertexDAO.delete();
    }
  }

  @Request
  @Test
  public void testIntegerAttribute()
  {
    MdVertexDAO mdVertexDAO = TestFixtureFactory.createMdVertex();
    mdVertexDAO.apply();

    try
    {
      MdAttributeIntegerDAO mdIntegerAttribute = TestFixtureFactory.addIntegerAttribute(mdVertexDAO);
      mdIntegerAttribute.apply();

      VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

      // Print the attributes. This will be moved into its own test.
      AttributeIF test = vertexDAO.getAttributeIF(mdIntegerAttribute.definesAttribute());

      Assert.assertNotNull(test);
    }
    finally
    {
      mdVertexDAO.delete();
    }
  }

  @Request
  @Test
  public void testLongAttribute()
  {
    MdVertexDAO mdVertexDAO = TestFixtureFactory.createMdVertex();
    mdVertexDAO.apply();

    try
    {
      MdAttributeLongDAO mdLongAttribute = TestFixtureFactory.addLongAttribute(mdVertexDAO);
      mdLongAttribute.apply();

      VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

      // Print the attributes. This will be moved into its own test.
      AttributeIF test = vertexDAO.getAttributeIF(mdLongAttribute.definesAttribute());

      Assert.assertNotNull(test);
    }
    finally
    {
      mdVertexDAO.delete();
    }
  }

  @Request
  @Test
  public void testFloatAttribute()
  {
    MdVertexDAO mdVertexDAO = TestFixtureFactory.createMdVertex();
    mdVertexDAO.apply();

    try
    {
      MdAttributeFloatDAO mdFloatAttribute = TestFixtureFactory.addFloatAttribute(mdVertexDAO);
      mdFloatAttribute.apply();

      VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

      // Print the attributes. This will be moved into its own test.
      AttributeIF test = vertexDAO.getAttributeIF(mdFloatAttribute.definesAttribute());

      Assert.assertNotNull(test);
    }
    finally
    {
      mdVertexDAO.delete();
    }
  }

  @Request
  @Test
  public void testDoubleAttribute()
  {
    MdVertexDAO mdVertexDAO = TestFixtureFactory.createMdVertex();
    mdVertexDAO.apply();
    
    try
    {
      MdAttributeDoubleDAO mdDoubleAttribute = TestFixtureFactory.addDoubleAttribute(mdVertexDAO);
      mdDoubleAttribute.apply();
      
      VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());
      
      // Print the attributes. This will be moved into its own test.
      AttributeIF test = vertexDAO.getAttributeIF(mdDoubleAttribute.definesAttribute());
      
      Assert.assertNotNull(test);
    }
    finally
    {
      mdVertexDAO.delete();
    }
  }
  
  @Request
  @Test
  public void testBooleanAttribute()
  {
    MdVertexDAO mdVertexDAO = TestFixtureFactory.createMdVertex();
    mdVertexDAO.apply();
    
    try
    {
      MdAttributeBooleanDAO mdBooleanAttribute = TestFixtureFactory.addBooleanAttribute(mdVertexDAO);
      mdBooleanAttribute.apply();
      
      VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());
      
      // Print the attributes. This will be moved into its own test.
      AttributeIF test = vertexDAO.getAttributeIF(mdBooleanAttribute.definesAttribute());
      
      Assert.assertNotNull(test);
    }
    finally
    {
      mdVertexDAO.delete();
    }
  }
  
  @Request
  @Test
  public void testDateAttribute()
  {
    MdVertexDAO mdVertexDAO = TestFixtureFactory.createMdVertex();
    mdVertexDAO.apply();
    
    try
    {
      MdAttributeDateDAO mdDateAttribute = TestFixtureFactory.addDateAttribute(mdVertexDAO);
      mdDateAttribute.apply();
      
      VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());
      
      // Print the attributes. This will be moved into its own test.
      AttributeIF test = vertexDAO.getAttributeIF(mdDateAttribute.definesAttribute());
      
      Assert.assertNotNull(test);
    }
    finally
    {
      mdVertexDAO.delete();
    }
  }
  
  @Request
  @Test
  public void testDateTimeAttribute()
  {
    MdVertexDAO mdVertexDAO = TestFixtureFactory.createMdVertex();
    mdVertexDAO.apply();
    
    try
    {
      MdAttributeDateTimeDAO mdDateTimeAttribute = TestFixtureFactory.addDateTimeAttribute(mdVertexDAO);
      mdDateTimeAttribute.apply();
      
      VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());
      
      // Print the attributes. This will be moved into its own test.
      AttributeIF test = vertexDAO.getAttributeIF(mdDateTimeAttribute.definesAttribute());
      
      Assert.assertNotNull(test);
    }
    finally
    {
      mdVertexDAO.delete();
    }
  }
  
  @Request
  @Test
  public void testTimeAttribute()
  {
    MdVertexDAO mdVertexDAO = TestFixtureFactory.createMdVertex();
    mdVertexDAO.apply();
    
    try
    {
      MdAttributeTimeDAO mdTimeAttribute = TestFixtureFactory.addTimeAttribute(mdVertexDAO);
      mdTimeAttribute.apply();
      
      VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());
      
      // Print the attributes. This will be moved into its own test.
      AttributeIF test = vertexDAO.getAttributeIF(mdTimeAttribute.definesAttribute());
      
      Assert.assertNotNull(test);
    }
    finally
    {
      mdVertexDAO.delete();
    }
  }
  
}
