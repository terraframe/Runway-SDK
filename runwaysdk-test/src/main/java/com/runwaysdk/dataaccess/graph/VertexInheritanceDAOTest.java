package com.runwaysdk.dataaccess.graph;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;

public class VertexInheritanceDAOTest
{
  private static MdVertexDAO             mdParentDAO;

  private static MdVertexDAO             mdChildDAO;

  private static MdAttributeCharacterDAO mdCharacterAttribute;

  @Request
  @BeforeClass
  public static void classSetup()
  {
    LocalProperties.setSkipCodeGenAndCompile(true);

    classSetup_Transaction();
  }

  @Transaction
  private static void classSetup_Transaction()
  {
    mdParentDAO = TestFixtureFactory.createMdVertex("TestParent");
    mdParentDAO.setValue(MdVertexInfo.ABSTRACT, Boolean.TRUE.toString());
    mdParentDAO.apply();

    mdChildDAO = TestFixtureFactory.createMdVertex("TestChild");
    mdChildDAO.setValue(MdVertexInfo.SUPER_MD_VERTEX, mdParentDAO.getOid());
    mdChildDAO.apply();

    mdCharacterAttribute = TestFixtureFactory.addCharacterAttribute(mdParentDAO);
    mdCharacterAttribute.apply();

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
    TestFixtureFactory.delete(mdChildDAO);
    TestFixtureFactory.delete(mdParentDAO);
  }

  @Request
  @Test
  public void testCharacterAttribute()
  {
    String attributeName = mdCharacterAttribute.definesAttribute();

    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdChildDAO.definesType());

    Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

    String value = "Test Value";

    vertexDAO.setValue(attributeName, value);

    Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName));

    try
    {
      // Test create
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdChildDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      // Test update
      value = "Updated Value";

      vertexDAO.setValue(attributeName, value);
      vertexDAO.apply();

      test = VertexObjectDAO.get(mdChildDAO, vertexDAO.getOid());

      Assert.assertEquals(value, test.getObjectValue(attributeName));
    }
    finally
    {
      vertexDAO.delete();
    }

    Assert.assertNull(VertexObjectDAO.get(mdChildDAO, vertexDAO.getOid()));
  }

}
