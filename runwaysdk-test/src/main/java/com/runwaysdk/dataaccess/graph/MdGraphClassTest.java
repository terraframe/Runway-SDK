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

import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeUUIDInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
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
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeLineStringDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiLineStringDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPolygonDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePolygonDAO;
import com.runwaysdk.session.Request;

public class MdGraphClassTest
{
  public static String TEST_PACKAGE        = TestFixConst.TEST_PACKAGE;

  public static String VERTEX_CLASS_NAME_1 = "TestVertexClass1";

  public static String VERTEX_CLASS_NAME_2 = "TestVertexClass2";

  public static String VERTEX_CLASS_1      = TEST_PACKAGE + "." + VERTEX_CLASS_NAME_1;

  public static String VERTEX_CLASS_2      = TEST_PACKAGE + "." + VERTEX_CLASS_NAME_2;

  public static String EDGE_CLASS_NAME     = "TestEdgeClass";

  public static String EDGE_CLASS          = TEST_PACKAGE + "." + EDGE_CLASS_NAME;

  public static String CHAR_ATTR_NAME      = "charAttr";

  public static String MAX_CHAR_ATTR_SIZE  = "12";

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    LocalProperties.setSkipCodeGenAndCompile(true);

    // MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(VERTEX_CLASS_1);
    //
    // System.out.println("!!!!HEADS UP!!!!!");
    // mdVertex.definesAttributes().forEach(a -> a.definesAttribute());

    deleteEdgeClass(EDGE_CLASS);

    deleteVertexClass(VERTEX_CLASS_2);

    deleteVertexClass(VERTEX_CLASS_1);

  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    LocalProperties.setSkipCodeGenAndCompile(false);
  }

  @Request
  @Before
  public void setUp() throws Exception
  {

  }

  @Request
  @After
  public void tearDown() throws Exception
  {
    deleteEdgeClass(EDGE_CLASS);

    deleteVertexClass(VERTEX_CLASS_2);

    deleteVertexClass(VERTEX_CLASS_1);
  }

  @Request
  @Test
  public void testCreateMdVertex()
  {
    MdVertexDAO mdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);

    String dbClassName = mdVertexDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    boolean classDefined = GraphDBService.getInstance().isVertexClassDefined(graphRequest, dbClassName);
    Assert.assertEquals("Vertex class was not defined", true, classDefined);

    MdAttributeDAOIF mdAttribute = mdVertexDAO.definesAttribute(ComponentInfo.OID);
    String dbAttrName = mdAttribute.definesAttribute();

    int charMaxSize = GraphDBService.getInstance().getCharacterAttributeMaxLength(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("The size of the character attribute was not set correctly", MdAttributeUUIDInfo.UUID_STRING_LENGTH, charMaxSize);

    boolean isRequired = GraphDBService.getInstance().isAttributeRequired(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("The attribute was supposed to be required", true, isRequired);

    IndexTypes indexType = GraphDBService.getInstance().getIndexType(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("The wrong type of index is defined", IndexTypes.UNIQUE_INDEX, indexType);

    mdVertexDAO.delete();

    classDefined = GraphDBService.getInstance().isVertexClassDefined(graphRequest, dbClassName);
    Assert.assertEquals("Vertex class still exists in the database", false, classDefined);
  }

  @Request
  @Test
  public void testCreateMdEdge()
  {
    MdVertexDAO parentMdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);
    MdVertexDAO childMdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_2);

    MdEdgeDAO mdEdgeDAO = createEdgeClass(EDGE_CLASS_NAME, parentMdVertexDAO.getOid(), childMdVertexDAO.getOid());
    String dbClassName = mdEdgeDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();

    try
    {
      boolean classDefined = GraphDBService.getInstance().isEdgeClassDefined(graphRequest, dbClassName);

      Assert.assertEquals("Edge class was not defined", true, classDefined);

      Set<String> parentEdgeIDs = ObjectCache.getParentMdEdgeDAOids(parentMdVertexDAO.getOid());
      Assert.assertEquals("Metadata did not properly store an edge class", 1, parentEdgeIDs.size());
      Assert.assertEquals("Metadata did not properly store an edge class", true, parentEdgeIDs.contains(mdEdgeDAO.getOid()));

      Set<String> childEdgeIDs = ObjectCache.getChildMdEdgeDAOids(childMdVertexDAO.getOid());

      Assert.assertEquals("Metadata did not properly store an edge class", 1, childEdgeIDs.size());
      Assert.assertEquals("Metadata did not properly store an edge class", true, childEdgeIDs.contains(mdEdgeDAO.getOid()));
    }
    finally
    {
      mdEdgeDAO.delete();

      parentMdVertexDAO.delete();
      childMdVertexDAO.delete();
    }

    boolean classDefined = GraphDBService.getInstance().isEdgeClassDefined(graphRequest, dbClassName);
    Assert.assertEquals("Edge class still exists in the database", false, classDefined);

    Set<String> parentEdgeIDs = ObjectCache.getParentMdEdgeDAOids(parentMdVertexDAO.getOid());
    Assert.assertEquals("Metadata did not properly store an edge class", 0, parentEdgeIDs.size());
    Assert.assertEquals("Metadata did not properly store an edge class", false, parentEdgeIDs.contains(mdEdgeDAO.getOid()));

    Set<String> childEdgeIDs = ObjectCache.getChildMdEdgeDAOids(childMdVertexDAO.getOid());

    Assert.assertEquals("Metadata did not properly store an edge class", 0, childEdgeIDs.size());
    Assert.assertEquals("Metadata did not properly store an edge class", false, childEdgeIDs.contains(mdEdgeDAO.getOid()));
  }

  @Request
  @Test
  public void testCreateCharAttrMdVertex()
  {
    MdVertexDAO mdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);
    MdAttributeCharacterDAO mdAttrChar = createMdAttrChar(mdVertexDAO, CHAR_ATTR_NAME);

    String dbClassName = mdVertexDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
    String dbAttrName = mdAttrChar.definesAttribute();
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();

    boolean attrDefined = GraphDBService.getInstance().isClassAttributeDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Attribute was not defined in the graph DB", true, attrDefined);

    this.testCreateCharAttrMdVertex_Transaction(graphRequest, mdAttrChar);

    attrDefined = GraphDBService.getInstance().isClassAttributeDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Attribute is still defined in the graph DB", false, attrDefined);
  }

  @Transaction
  private void testCreateCharAttrMdVertex_Transaction(GraphRequest graphRequest, MdAttributeCharacterDAO mdAttrChar)
  {
    MdVertexDAOIF mdVertexDAOIF = (MdVertexDAOIF) mdAttrChar.definedByClass();

    String dbClassName = mdVertexDAOIF.getValue(MdVertexInfo.DB_CLASS_NAME);
    String dbAttrName = mdAttrChar.definesAttribute();

    mdAttrChar.delete();

    boolean attrDefined = GraphDBService.getInstance().isClassAttributeDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Attribute metadata deleted but the attribute should still be defined in the database in the middle of a transaction.", true, attrDefined);
  }

  @Request
  @Test
  public void testCreateCharAttrWithIndex()
  {
    MdVertexDAO mdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);

    // Unique index
    MdAttributeCharacterDAO mdAttrChar = createMdAttrChar(mdVertexDAO, CHAR_ATTR_NAME, IndexTypes.UNIQUE_INDEX);

    String dbClassName = mdVertexDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
    String dbAttrName = mdAttrChar.definesAttribute();
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();

    String indexName = mdAttrChar.getAttributeIF(MdAttributeCharacterInfo.INDEX_NAME).getValue();
    Assert.assertNotEquals("The name of the index should not be null", "", indexName);

    IndexTypes indexType = GraphDBService.getInstance().getIndexType(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("The wrong type of index is defined", IndexTypes.UNIQUE_INDEX, indexType);

    // No index
    mdAttrChar.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.NO_INDEX.getOid());
    mdAttrChar.apply();

    indexName = mdAttrChar.getAttributeIF(MdAttributeCharacterInfo.INDEX_NAME).getValue();
    Assert.assertEquals("The name of the index should be null", "", indexName);

    indexType = GraphDBService.getInstance().getIndexType(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("The wrong type of index is defined", IndexTypes.NO_INDEX, indexType);

    // Non unique index
    mdAttrChar.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.NON_UNIQUE_INDEX.getOid());
    mdAttrChar.apply();

    indexName = mdAttrChar.getAttributeIF(MdAttributeCharacterInfo.INDEX_NAME).getValue();
    Assert.assertNotEquals("The name of the index should not be null", "", indexName);

    indexType = GraphDBService.getInstance().getIndexType(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("The wrong type of index is defined", IndexTypes.NON_UNIQUE_INDEX, indexType);

    mdAttrChar.delete();
  }

  @Request
  @Test
  public void testCreateCharAttrRequired()
  {
    MdVertexDAO mdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);
    MdAttributeCharacterDAO mdAttrChar = createMdAttrChar(mdVertexDAO, CHAR_ATTR_NAME, true);

    String dbClassName = mdVertexDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
    String dbAttrName = mdAttrChar.definesAttribute();
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();

    boolean isRequired = GraphDBService.getInstance().isAttributeRequired(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("The attribute was supposed to be required", true, isRequired);

    mdAttrChar.setValue(MdAttributeCharacterInfo.REQUIRED, Boolean.toString(false).toUpperCase());
    mdAttrChar.apply();

    isRequired = GraphDBService.getInstance().isAttributeRequired(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("The attribute was supposed to be not required", false, isRequired);

    mdAttrChar.setValue(MdAttributeCharacterInfo.REQUIRED, Boolean.toString(true).toUpperCase());
    mdAttrChar.apply();

    isRequired = GraphDBService.getInstance().isAttributeRequired(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("The attribute was supposed to be required", true, isRequired);

    mdAttrChar.delete();
  }

  @Request
  @Test
  public void testCreateCharAttrSize()
  {
    MdVertexDAO mdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);
    MdAttributeCharacterDAO mdAttrChar = createMdAttrChar(mdVertexDAO, CHAR_ATTR_NAME);

    String dbClassName = mdVertexDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
    String dbAttrName = mdAttrChar.definesAttribute();
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();

    int charMaxSize = GraphDBService.getInstance().getCharacterAttributeMaxLength(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("The size of the character attribute was not set correctly", Integer.parseInt(MAX_CHAR_ATTR_SIZE), charMaxSize);

    mdAttrChar.setValue(MdAttributeCharacterInfo.SIZE, "16");
    mdAttrChar.apply();

    charMaxSize = GraphDBService.getInstance().getCharacterAttributeMaxLength(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("The size of the character attribute was not set correctly", 16, charMaxSize);

    mdAttrChar.setValue(MdAttributeCharacterInfo.SIZE, MAX_CHAR_ATTR_SIZE);
    mdAttrChar.apply();

    charMaxSize = GraphDBService.getInstance().getCharacterAttributeMaxLength(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("The size of the character attribute was not set correctly", Integer.parseInt(MAX_CHAR_ATTR_SIZE), charMaxSize);

    mdAttrChar.delete();
  }

  @Request
  @Test
  public void testCreateIntegerAttrMdVertex()
  {
    MdVertexDAO mdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);
    MdAttributeIntegerDAO mdAttribute = TestFixtureFactory.addIntegerAttribute(mdVertexDAO);
    mdAttribute.apply();

    String dbClassName = mdVertexDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
    String dbAttrName = mdAttribute.definesAttribute();
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();

    boolean attrDefined = GraphDBService.getInstance().isClassAttributeDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Attribute was not defined in the graph DB", true, attrDefined);
  }

  @Request
  @Test
  public void testCreateLongAttrMdVertex()
  {
    MdVertexDAO mdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);
    MdAttributeLongDAO mdAttribute = TestFixtureFactory.addLongAttribute(mdVertexDAO);
    mdAttribute.apply();

    String dbClassName = mdVertexDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
    String dbAttrName = mdAttribute.definesAttribute();
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();

    boolean attrDefined = GraphDBService.getInstance().isClassAttributeDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Attribute was not defined in the graph DB", true, attrDefined);
  }

  @Request
  @Test
  public void testCreateFloatAttrMdVertex()
  {
    MdVertexDAO mdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);
    MdAttributeFloatDAO mdAttribute = TestFixtureFactory.addFloatAttribute(mdVertexDAO);
    mdAttribute.apply();

    String dbClassName = mdVertexDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
    String dbAttrName = mdAttribute.definesAttribute();
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();

    boolean attrDefined = GraphDBService.getInstance().isClassAttributeDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Attribute was not defined in the graph DB", true, attrDefined);
  }

  @Request
  @Test
  public void testCreateTextAttrMdVertex()
  {
    MdVertexDAO mdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);
    MdAttributeTextDAO mdAttribute = TestFixtureFactory.addTextAttribute(mdVertexDAO);
    mdAttribute.apply();

    String dbClassName = mdVertexDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
    String dbAttrName = mdAttribute.definesAttribute();
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();

    boolean attrDefined = GraphDBService.getInstance().isClassAttributeDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Attribute was not defined in the graph DB", true, attrDefined);
  }

  @Request
  @Test
  public void testCreateDoubleAttrMdVertex()
  {
    MdVertexDAO mdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);
    MdAttributeDoubleDAO mdAttribute = TestFixtureFactory.addDoubleAttribute(mdVertexDAO);
    mdAttribute.apply();

    String dbClassName = mdVertexDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
    String dbAttrName = mdAttribute.definesAttribute();
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();

    boolean attrDefined = GraphDBService.getInstance().isClassAttributeDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Attribute was not defined in the graph DB", true, attrDefined);
  }

  @Request
  @Test
  public void testCreateBooleanAttrMdVertex()
  {
    MdVertexDAO mdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);
    MdAttributeBooleanDAO mdAttribute = TestFixtureFactory.addBooleanAttribute(mdVertexDAO);
    mdAttribute.apply();

    String dbClassName = mdVertexDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
    String dbAttrName = mdAttribute.definesAttribute();
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();

    boolean attrDefined = GraphDBService.getInstance().isClassAttributeDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Attribute was not defined in the graph DB", true, attrDefined);
  }

  @Request
  @Test
  public void testCreateDateAttrMdVertex()
  {
    MdVertexDAO mdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);
    MdAttributeDateDAO mdAttribute = TestFixtureFactory.addDateAttribute(mdVertexDAO);
    mdAttribute.apply();

    String dbClassName = mdVertexDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
    String dbAttrName = mdAttribute.definesAttribute();
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();

    Boolean attrDefined = GraphDBService.getInstance().isClassAttributeDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Attribute was not defined in the graph DB", true, attrDefined);
  }

  @Request
  @Test
  public void testCreateDateTimeAttrMdVertex()
  {
    MdVertexDAO mdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);
    MdAttributeDateTimeDAO mdAttribute = TestFixtureFactory.addDateTimeAttribute(mdVertexDAO);
    mdAttribute.apply();

    String dbClassName = mdVertexDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
    String dbAttrName = mdAttribute.definesAttribute();
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();

    Boolean attrDefined = GraphDBService.getInstance().isClassAttributeDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Attribute was not defined in the graph DB", true, attrDefined);
  }

  @Request
  @Test
  public void testCreateTimeAttrMdVertex()
  {
    MdVertexDAO mdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);
    MdAttributeTimeDAO mdAttribute = TestFixtureFactory.addTimeAttribute(mdVertexDAO);
    mdAttribute.apply();

    String dbClassName = mdVertexDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
    String dbAttrName = mdAttribute.definesAttribute();
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();

    Boolean attrDefined = GraphDBService.getInstance().isClassAttributeDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Attribute was not defined in the graph DB", true, attrDefined);
  }

  @Request
  @Test
  public void testCreatePointAttrMdVertex()
  {
    MdVertexDAO mdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);
    MdAttributePointDAO mdAttribute = TestFixtureFactory.addPointAttribute(mdVertexDAO);
    mdAttribute.apply();

    String dbClassName = mdVertexDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
    String dbAttrName = mdAttribute.definesAttribute();
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();

    Boolean attrDefined = GraphDBService.getInstance().isClassAttributeDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Attribute was not defined in the graph DB", true, attrDefined);

    Boolean indexDefined = GraphDBService.getInstance().isIndexDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Index was not defined in the graph DB", true, indexDefined);
  }

  @Request
  @Test
  public void testCreatePolygonAttrMdVertex()
  {
    MdVertexDAO mdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);
    MdAttributePolygonDAO mdAttribute = TestFixtureFactory.addPolygonAttribute(mdVertexDAO);
    mdAttribute.apply();

    String dbClassName = mdVertexDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
    String dbAttrName = mdAttribute.definesAttribute();
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();

    Boolean attrDefined = GraphDBService.getInstance().isClassAttributeDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Attribute was not defined in the graph DB", true, attrDefined);

    Boolean indexDefined = GraphDBService.getInstance().isIndexDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Index was not defined in the graph DB", true, indexDefined);
  }

  @Request
  @Test
  public void testCreateLineStringAttrMdVertex()
  {
    MdVertexDAO mdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);
    MdAttributeLineStringDAO mdAttribute = TestFixtureFactory.addLineStringAttribute(mdVertexDAO);
    mdAttribute.apply();

    String dbClassName = mdVertexDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
    String dbAttrName = mdAttribute.definesAttribute();
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();

    Boolean attrDefined = GraphDBService.getInstance().isClassAttributeDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Attribute was not defined in the graph DB", true, attrDefined);

    Boolean indexDefined = GraphDBService.getInstance().isIndexDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Index was not defined in the graph DB", true, indexDefined);
  }

  @Request
  @Test
  public void testCreateMultiPointAttrMdVertex()
  {
    MdVertexDAO mdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);
    MdAttributeMultiPointDAO mdAttribute = TestFixtureFactory.addMultiPointAttribute(mdVertexDAO);
    mdAttribute.apply();

    String dbClassName = mdVertexDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
    String dbAttrName = mdAttribute.definesAttribute();
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();

    Boolean attrDefined = GraphDBService.getInstance().isClassAttributeDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Attribute was not defined in the graph DB", true, attrDefined);

    Boolean indexDefined = GraphDBService.getInstance().isIndexDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Index was not defined in the graph DB", true, indexDefined);
  }

  @Request
  @Test
  public void testCreateMultiPolygonAttrMdVertex()
  {
    MdVertexDAO mdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);
    MdAttributeMultiPolygonDAO mdAttribute = TestFixtureFactory.addMultiPolygonAttribute(mdVertexDAO);
    mdAttribute.apply();

    String dbClassName = mdVertexDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
    String dbAttrName = mdAttribute.definesAttribute();
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();

    Boolean attrDefined = GraphDBService.getInstance().isClassAttributeDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Attribute was not defined in the graph DB", true, attrDefined);

    Boolean indexDefined = GraphDBService.getInstance().isIndexDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Index was not defined in the graph DB", true, indexDefined);
  }

  @Request
  @Test
  public void testCreateMultiLineStringAttrMdVertex()
  {
    MdVertexDAO mdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);
    MdAttributeMultiLineStringDAO mdAttribute = TestFixtureFactory.addMultiLineStringAttribute(mdVertexDAO);
    mdAttribute.apply();

    String dbClassName = mdVertexDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
    String dbAttrName = mdAttribute.definesAttribute();
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();

    Boolean attrDefined = GraphDBService.getInstance().isClassAttributeDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Attribute was not defined in the graph DB", true, attrDefined);

    Boolean indexDefined = GraphDBService.getInstance().isIndexDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Index was not defined in the graph DB", true, indexDefined);
  }

  @Request
  @Test
  public void testCreateReferenceAttrMdVertex()
  {
    MdBusinessDAO mdBusinessDAO = TestFixtureFactory.createMdBusiness(VERTEX_CLASS_NAME_2);
    mdBusinessDAO.apply();

    MdVertexDAO mdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);

    MdAttributeReferenceDAO mdAttribute = TestFixtureFactory.addReferenceAttribute(mdVertexDAO, mdBusinessDAO);
    mdAttribute.apply();

    String dbClassName = mdVertexDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
    String dbAttrName = mdAttribute.definesAttribute();
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();

    Boolean attrDefined = GraphDBService.getInstance().isClassAttributeDefined(graphRequest, dbClassName, dbAttrName);
    Assert.assertEquals("Attribute was not defined in the graph DB", true, attrDefined);
  }

  @Request
  @Test
  public void testCreateEnumerationAttrMdVertex()
  {
    MdBusinessDAO mdBusinessDAO = TestFixtureFactory.createEnumClass(VERTEX_CLASS_NAME_2);
    mdBusinessDAO.apply();

    MdEnumerationDAO mdEnumeration = TestFixtureFactory.createMdEnumeation1(mdBusinessDAO);

    try
    {
      mdEnumeration.apply();

      MdVertexDAO mdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);

      MdAttributeEnumerationDAO mdAttribute = TestFixtureFactory.addEnumerationAttribute(mdVertexDAO, mdEnumeration);
      mdAttribute.apply();

      String dbClassName = mdVertexDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
      String dbAttrName = mdAttribute.definesAttribute();
      GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();

      Boolean attrDefined = GraphDBService.getInstance().isClassAttributeDefined(graphRequest, dbClassName, dbAttrName);
      Assert.assertEquals("Attribute was not defined in the graph DB", true, attrDefined);
    }
    finally
    {
      TestFixtureFactory.delete(mdEnumeration);
    }
  }

  private static MdVertexDAO createVertexClass(String vertexName)
  {
    MdVertexDAO mdVertexDAO = TestFixtureFactory.createMdVertex(vertexName);
    mdVertexDAO.setValue(MdVertexInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    mdVertexDAO.apply();

    return mdVertexDAO;
  }

  private static MdEdgeDAO createEdgeClass(String edgeName, String parentMdEdgeOid, String childMdEdgeOid)
  {
    MdEdgeDAO mdEdgeDAO = TestFixtureFactory.createMdEdge(parentMdEdgeOid, childMdEdgeOid, edgeName);
    mdEdgeDAO.apply();

    return mdEdgeDAO;
  }

  private MdAttributeCharacterDAO createMdAttrChar(MdGraphClassDAOIF mdGraphClassDAOIF, String attrName)
  {
    return this.createMdAttrChar(mdGraphClassDAOIF, attrName, false, IndexTypes.NO_INDEX);
  }

  private MdAttributeCharacterDAO createMdAttrChar(MdGraphClassDAOIF mdGraphClassDAOIF, String attrName, IndexTypes indexType)
  {
    return this.createMdAttrChar(mdGraphClassDAOIF, attrName, false, indexType);
  }

  private MdAttributeCharacterDAO createMdAttrChar(MdGraphClassDAOIF mdGraphClassDAOIF, String attrName, boolean required)
  {
    return this.createMdAttrChar(mdGraphClassDAOIF, attrName, required, IndexTypes.NO_INDEX);
  }

  private MdAttributeCharacterDAO createMdAttrChar(MdGraphClassDAOIF mdGraphClassDAOIF, String attrName, boolean required, IndexTypes indexType)
  {
    MdAttributeCharacterDAO mdAttrChar = MdAttributeCharacterDAO.newInstance();
    mdAttrChar.setValue(MdAttributeCharacterInfo.NAME, attrName);
    mdAttrChar.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Character");
    mdAttrChar.setValue(MdAttributeCharacterInfo.REQUIRED, Boolean.toString(required).toUpperCase());
    mdAttrChar.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttrChar.setValue(MdAttributeCharacterInfo.INDEX_TYPE, indexType.getOid());
    mdAttrChar.setValue(MdAttributeCharacterInfo.SIZE, MAX_CHAR_ATTR_SIZE);
    mdAttrChar.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdGraphClassDAOIF.getOid());
    mdAttrChar.apply();

    return mdAttrChar;
  }

  private static void deleteVertexClass(String vertexClassName)
  {
    MdClassDAOIF mdVertexDAOIF = null;

    try
    {
      mdVertexDAOIF = MdClassDAO.getMdClassDAO(vertexClassName);
    }
    catch (DataNotFoundException ex)
    {
    }

    if (mdVertexDAOIF != null)
    {
      mdVertexDAOIF.getBusinessDAO().delete();
    }
  }

  private static void deleteEdgeClass(String edgeClassName)
  {
    MdEdgeDAOIF mdEdgeDAOIF = null;

    try
    {
      mdEdgeDAOIF = MdEdgeDAO.getMdEdgeDAO(edgeClassName);
    }
    catch (DataNotFoundException ex)
    {
    }
    ;

    if (mdEdgeDAOIF != null)
    {
      mdEdgeDAOIF.getBusinessDAO().delete();
    }
  }

}
