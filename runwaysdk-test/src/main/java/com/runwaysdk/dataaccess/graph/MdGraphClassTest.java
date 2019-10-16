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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeUUIDInfo;
import com.runwaysdk.constants.graph.MdEdgeInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;

public class MdGraphClassTest
{
  public static String TEST_PACKAGE          = "test";
  
  public static String VERTEX_CLASS_NAME_1   = "TestVertexClass1";
  
  public static String VERTEX_CLASS_NAME_2   = "TestVertexClass2";
  
  public static String VERTEX_CLASS_1        = TEST_PACKAGE+"."+VERTEX_CLASS_NAME_1;
  
  public static String VERTEX_CLASS_2        = TEST_PACKAGE+"."+VERTEX_CLASS_NAME_2;
  
  public static String EDGE_CLASS_NAME       = "TestEdgeClass";

  public static String EDGE_CLASS            = TEST_PACKAGE+"."+EDGE_CLASS_NAME;

  public static String CHAR_ATTR_NAME        = "charAttr";
  
  public static String MAX_CHAR_ATTR_SIZE    = "12";
  
  
  @Request
  @BeforeClass
  public static void classSetUp()
  {
    
//    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(VERTEX_CLASS_1);
//    
//    System.out.println("!!!!HEADS UP!!!!!");
//    mdVertex.definesAttributes().forEach(a -> a.definesAttribute());
    
    deleteVertexClass(VERTEX_CLASS_1);
    
    deleteVertexClass(VERTEX_CLASS_2);

    deleteEdgeClass(EDGE_CLASS);
  }
  
  @Request
  @AfterClass
  public static void classTearDown()
  {
    deleteVertexClass(VERTEX_CLASS_1);
    
    deleteVertexClass(VERTEX_CLASS_2);

    deleteEdgeClass(EDGE_CLASS);
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

    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(VERTEX_CLASS_1);

// Print the attributes. This will be moved into its own test.
vertexDAO.printAttributes();
    
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
    
    try
    {
      MdEdgeDAO mdEdgeDAO = createEdgeClass(EDGE_CLASS_NAME, parentMdVertexDAO.getOid(), childMdVertexDAO.getOid());
    
      String dbClassName = mdEdgeDAO.getValue(MdVertexInfo.DB_CLASS_NAME);
      GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
      boolean classDefined = GraphDBService.getInstance().isEdgeClassDefined(graphRequest, dbClassName);
    
      Assert.assertEquals("Edge class was not defined", true, classDefined);
    
      mdEdgeDAO.delete();
    
      classDefined = GraphDBService.getInstance().isEdgeClassDefined(graphRequest, dbClassName);
      Assert.assertEquals("Edge class still exists in the database", false, classDefined);
    }
    finally
    {
      parentMdVertexDAO.delete();
      childMdVertexDAO.delete();
    }
  }
  
  
  @Request
  @Test
  public void testCreateCharAttrMdVertex() 
  {
    MdVertexDAO mdVertexDAO = createVertexClass(VERTEX_CLASS_NAME_1);
    MdAttributeCharacterDAO mdAttrChar = createMdAttrChar(mdVertexDAO, CHAR_ATTR_NAME) ;
       
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
    MdVertexDAOIF mdVertexDAOIF = (MdVertexDAOIF)mdAttrChar.definedByClass();
    
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

  private static MdVertexDAO createVertexClass(String vertexName)
  {
    MdVertexDAO mdVertexDAO = MdVertexDAO.newInstance();
    mdVertexDAO.setValue(MdVertexInfo.NAME, vertexName);
    mdVertexDAO.setValue(MdVertexInfo.PACKAGE, TEST_PACKAGE);
    mdVertexDAO.setStructValue(MdVertexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Vertex Class");
    mdVertexDAO.setValue(MdVertexInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdVertexDAO.setValue(MdVertexInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdVertexDAO.apply();
    
    return mdVertexDAO;
  }
  
  private static MdEdgeDAO createEdgeClass(String edgeName, String parentMdEdgeOid, String childMdEdgeOid)
  {
    MdEdgeDAO mdEdgeDAO = MdEdgeDAO.newInstance();
    mdEdgeDAO.setValue(MdEdgeInfo.NAME, edgeName);
    mdEdgeDAO.setValue(MdEdgeInfo.PACKAGE, TEST_PACKAGE);
    mdEdgeDAO.setStructValue(MdEdgeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Vertex Class");
    
    mdEdgeDAO.setValue(MdEdgeInfo.PARENT_MD_VERTEX, parentMdEdgeOid);
    mdEdgeDAO.setValue(MdEdgeInfo.CHILD_MD_VERTEX, childMdEdgeOid);
    
    mdEdgeDAO.setValue(MdEdgeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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
    MdVertexDAOIF mdVertexDAOIF = null;

    try
    {
      mdVertexDAOIF = MdVertexDAO.getMdVertexDAO(vertexClassName);
    }
    catch (DataNotFoundException ex) {};
    
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
    catch (DataNotFoundException ex) {};
    
    if (mdEdgeDAOIF != null)
    {
      mdEdgeDAOIF.getBusinessDAO().delete();
    }
  }
  

}
