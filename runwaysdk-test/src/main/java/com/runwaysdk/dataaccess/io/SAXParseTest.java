/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.io;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.generation.EntityQueryAPIGenerator;
import com.runwaysdk.business.rbac.MethodActorDAO;
import com.runwaysdk.business.rbac.MethodActorDAOIF;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.business.state.MdStateMachineDAO;
import com.runwaysdk.business.state.MdStateMachineDAOIF;
import com.runwaysdk.business.state.StateMasterDAO;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.EntityTypes;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.HashMethods;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdActionInfo;
import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDimensionInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFileInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeHashInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeMomentInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeSymmetricInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.MdAttributeVirtualInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdControllerInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdExceptionInfo;
import com.runwaysdk.constants.MdFacadeInfo;
import com.runwaysdk.constants.MdIndexInfo;
import com.runwaysdk.constants.MdInformationInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdParameterInfo;
import com.runwaysdk.constants.MdProblemInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdStateMachineInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.MdUtilInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.constants.SymmetricMethods;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.constants.XMLConstants;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdActionDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.MdAttributeHashDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalTextDAOIF;
import com.runwaysdk.dataaccess.MdAttributeSymmetricDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdControllerDAOIF;
import com.runwaysdk.dataaccess.MdElementDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdExceptionDAOIF;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.MdFieldDAOIF;
import com.runwaysdk.dataaccess.MdIndexDAOIF;
import com.runwaysdk.dataaccess.MdInformationDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.MdProblemDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.MdUtilDAOIF;
import com.runwaysdk.dataaccess.MdViewDAOIF;
import com.runwaysdk.dataaccess.MdWebFormDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.TransitionDAO;
import com.runwaysdk.dataaccess.TransitionDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.AttributeLocalCharacter;
import com.runwaysdk.dataaccess.attributes.entity.AttributeLocalText;
import com.runwaysdk.dataaccess.attributes.entity.AttributeStruct;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.dataDefinition.ExportMetadata;
import com.runwaysdk.dataaccess.io.dataDefinition.SAXExporter;
import com.runwaysdk.dataaccess.io.dataDefinition.SAXImporter;
import com.runwaysdk.dataaccess.metadata.DuplicateAttributeDefinitionException;
import com.runwaysdk.dataaccess.metadata.MdActionDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeBlobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeVirtualDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdControllerDAO;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdExceptionDAO;
import com.runwaysdk.dataaccess.metadata.MdFacadeDAO;
import com.runwaysdk.dataaccess.metadata.MdGraphDAO;
import com.runwaysdk.dataaccess.metadata.MdIndexDAO;
import com.runwaysdk.dataaccess.metadata.MdInformationDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdPackage;
import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.dataaccess.metadata.MdProblemDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.MdTreeDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.MdUtilDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.dataaccess.metadata.MdWebBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdWebCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdWebDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFormDAO;
import com.runwaysdk.dataaccess.metadata.MdWebIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdWebLongDAO;
import com.runwaysdk.dataaccess.metadata.MdWebReferenceDAO;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAO;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAOIF;

/**
 * @author Justin
 * 
 */
public class SAXParseTest extends TestCase
{
  @Override
  public TestResult run()
  {
    return super.run();
  }

  @Override
  public void run(TestResult testResult)
  {
    super.run(testResult);
  }

  public static final String   path                        = TestConstants.Path.XMLFiles + "/";

  public static final String   SCHEMA                      = XMLConstants.DATATYPE_XSD;
  
  public static final String   tempXMLFile                 = CommonProperties.getProjectBasedir() + "/target/testxml/saxParseTest.xml";

  /**
   * List of all XML files to test on
   */
  public static final String   STANDALONE_STRUCT_SET       = path + "standaloneSetTest.xml";

  public static final String   RELATIONSHIP_SET            = path + "relationshipSetTest.xml";

  public static final String   TREE_SET                    = path + "treeTest.xml";

  public static final String   GRAPH_SET                   = path + "graphTest.xml";

  public static final String   FILTER_SET                  = path + "filterSetTest.xml";

  public static final String   REMOVABLE_FILTER_SET        = path + "RemovablefilterSetTest.xml";

  public static final String   INSTANCE_REF_SET            = path + "instance_refSetTest.xml";

  public static final String   CIRCULAR_TEST               = path + "circularTest.xml";

  public static final String   DUPLICATE_TEST              = path + "duplicateAttributeTest.xml";

  public static final String   INVALID_INSTANCE_TEST       = path + "invalidInstanceTest.xml";

  public static final String   CACHE_ALGORITHM_TEST        = path + "cacheAlgorithmTest.xml";

  public static final String   INVALID_SCHEMA_TEST         = path + "invalidSchemaTest.xml";

  public static final String   SELECTION_SET_TEST          = path + "selectionSetTest.xml";

  public static final String   FACADE_SET_TEST             = path + "mdFacadeTest.xml";

  public static final String   METHOD_SET_TEST             = path + "mdMethodTest.xml";

  public static final String   INDEX_TEST                  = path + "indexTest.xml";

  public static final String   INVALID_ID                  = path + "invalidId.xml";

  public static final String   TEST_ALL_PERMISSIONS        = path + "testAllPermissions.xml";

  public static final String   TEST_MULTI_ROLE_PERMISSIONS = path + "testMultiRolePermissions.xml";

  public static final String   TEST_MULTI_USER_PERMISSIONS = path + "testMultiUserPermissions.xml";

  /**
   * Standard datatype names used in the XML test files
   */
  public static final String   FILTER                      = "test.xmlclasses.Filter1";

  public static final String   FILTER2                     = "test.xmlclasses.Filter2";

  public static final String   CLASS                       = "test.xmlclasses.Class1";

  public static final String   CLASS2                      = "test.xmlclasses.Class2";

  public static final String   CLASS3                      = "test.xmlclasses.Class3";

  public static final String   VIEW                        = "test.xmlclasses.View1";

  public static final String   EXCEPTION                   = "test.xmlclasses.Exception1";

  public static final String   EXCEPTION2                  = "test.xmlclasses.Exception2";

  public static final String   INFORMATION                 = "test.xmlclasses.Information1";

  public static final String   INFORMATION2                = "test.xmlclasses.Information2";

  public static final String   RELATIONSHIP                = "test.xmlclasses.Relationship1";

  public static final String   RELATIONSHIP2               = "test.xmlclasses.Relationship2";

  public static final String   ENUM_CLASS                  = "test.xmlclasses.EnumClassTest";

  public static final String   FACADE_CLASS                = "test.xmlclasses.Facade1";

  public static final String[] classNames                  = { RELATIONSHIP, RELATIONSHIP2, FILTER, FILTER2, CLASS, CLASS2, CLASS3, ENUM_CLASS, FACADE_CLASS };

  /**
   * A suite() takes <b>this </b> <code>AttributeTest.class</code> and wraps it
   * in <code>MasterTestSetup</code>. The returned class is a suite of all the
   * tests in <code>AttributeTest</code>, with the global setUp() and tearDown()
   * methods from <code>MasterTestSetup</code>.
   * 
   * @return A suite of tests wrapped in global setUp and tearDown methods
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(SAXParseTest.class);

    return suite;
  }

  /**
   * No setup needed non-Javadoc)
   * 
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception
  {

  }

  /**
   * Delete all MetaData objects which were created in the class
   * 
   * @see junit.framework.TestCase#tearDown()
   */
  protected void tearDown() throws Exception
  {
    new MdPackage("test.xmlclasses").delete();

    try
    {
      TestFixtureFactory.delete(UserDAO.findUser("testUser"));
    }
    catch (DataNotFoundException e)
    {

    }

    try
    {
      TestFixtureFactory.delete(RoleDAO.findRole("runway.testRole"));
    }
    catch (DataNotFoundException e)
    {

    }

    try
    {
      TestFixtureFactory.delete(RoleDAO.findRole("runway.testRole2"));
    }
    catch (DataNotFoundException e)
    {

    }
  }

  /**
   * Test setting of attributes on the local text datatype minus any overlapping
   * attributes from the boolean test
   */
  public void testCreateLocalText()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    TestFixtureFactory.addLocalTextAttribute(mdBusiness1).apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO.setStructValue("testLocalText", MdAttributeLocalInfo.DEFAULT_LOCALE, "Yo Diggidy");
    businessDAO.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1, businessDAO }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeLocalTextDAOIF mdAttributeIF = (MdAttributeLocalTextDAOIF) mdEntityIF.definesAttribute("testLocalText");

    assertEquals(mdAttributeIF.getStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Local Text Test");

    List<String> ids = EntityDAO.getEntityIdsDB(CLASS);

    assertEquals(1, ids.size());

    BusinessDAOIF businessDAOIF = BusinessDAO.get(ids.get(0));
    AttributeLocalText attribute = (AttributeLocalText) businessDAOIF.getAttributeIF("testLocalText");

    assertEquals("Yo Diggidy", attribute.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE));
  }

  /**
   * Test setting of attributes on the local character datatype minus any
   * overlapping attributes from the boolean test
   */
  public void testCreateLocalCharacter()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    TestFixtureFactory.addLocalCharacterAttribute(mdBusiness1).apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO.setStructValue("testLocalCharacter", MdAttributeLocalInfo.DEFAULT_LOCALE, "Yo Diggidy");
    businessDAO.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1, businessDAO }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF mdAttributeIF = mdEntityIF.definesAttribute("testLocalCharacter");

    assertEquals(mdAttributeIF.getStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Local Character Test");

    List<String> ids = EntityDAO.getEntityIdsDB(CLASS);

    assertEquals(1, ids.size());

    BusinessDAOIF businessDAOIF = BusinessDAO.get(ids.get(0));
    AttributeLocalCharacter attribute = (AttributeLocalCharacter) businessDAOIF.getAttributeIF("testLocalCharacter");

    assertEquals("Yo Diggidy", attribute.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE));
  }

  /**
   * Test setting of attributes on a blob datatype
   */
  public void testCreateBlob()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    TestFixtureFactory.addBlobAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeConcreteDAO attribute = (MdAttributeConcreteDAO) ( mdEntityIF.definesAttribute("testBlob") ).getBusinessDAO();
    AttributeEnumerationIF index = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    assertEquals(attribute.getStructValue(MdAttributeConcreteInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Blob Set Test");
    assertEquals(attribute.getValue(MdAttributeConcreteInfo.IMMUTABLE), MdAttributeBooleanInfo.FALSE);
    assertEquals(MdAttributeBooleanInfo.TRUE, attribute.getValue(MdAttributeConcreteInfo.REQUIRED));
    assertEquals(MdAttributeBooleanInfo.TRUE, attribute.getValue(MdAttributeConcreteInfo.REMOVE));
    assertEquals(attribute.getStructValue(MdAttributeConcreteInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Blob Test");
    assertEquals(index.dereference()[0].getId(), IndexTypes.NO_INDEX.getId());
  }

  /**
   * Test setting of attributes on a boolean datatype
   */
  public void testCreateBoolean()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdBoolean = TestFixtureFactory.addBooleanAttribute(mdBusiness1);
    mdBoolean.setValue(MdAttributeBooleanInfo.DEFAULT_VALUE, MdAttributeBooleanInfo.FALSE);
    mdBoolean.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdBoolean.setValue(MdAttributeBooleanInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getId());
    mdBoolean.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Boolean Test");
    mdBoolean.setValue(MdAttributeBooleanInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdBoolean.setValue(MdAttributeBooleanInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    mdBoolean.apply();

    mdBusiness1.setValue(MdBusinessInfo.DTO_STUB_SOURCE, TestFixtureFactory.getMdBusinessDTOStub());

    MdBusinessDAO updateBusiness = MdBusinessDAO.get(mdBusiness1.getId()).getBusinessDAO();
    updateBusiness.setValue(MdBusinessInfo.DTO_STUB_SOURCE, TestFixtureFactory.getMdBusinessDTOStub());
    updateBusiness.apply();

    ExportMetadata metadata = new ExportMetadata(true);
    metadata.addCreate(new ComponentIF[] { mdBusiness1 });

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    assertEquals(TestFixtureFactory.getMdBusinessDTOStub().trim(), mdEntityIF.getValue(MdBusinessInfo.DTO_STUB_SOURCE).trim());

    MdAttributeBooleanDAO attribute = (MdAttributeBooleanDAO) ( mdEntityIF.definesAttribute("testBoolean") ).getBusinessDAO();
    AttributeEnumerationIF index = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    assertEquals(attribute.getStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Boolean Set Test");
    assertEquals(attribute.getValue(MdAttributeBooleanInfo.DEFAULT_VALUE), MdAttributeBooleanInfo.FALSE);
    assertEquals(attribute.getValue(MdAttributeBooleanInfo.IMMUTABLE), MdAttributeBooleanInfo.FALSE);
    assertEquals(attribute.getValue(MdAttributeBooleanInfo.REQUIRED), MdAttributeBooleanInfo.TRUE);
    assertEquals(attribute.getValue(MdAttributeBooleanInfo.REMOVE), MdAttributeBooleanInfo.FALSE);
    assertEquals(attribute.getStructValue(MdAttributeBooleanInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Boolean Test");
    assertEquals(attribute.getStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), MdAttributeBooleanInfo.TRUE);
    assertEquals(attribute.getStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), MdAttributeBooleanInfo.FALSE);
    assertEquals(index.dereference()[0].getId(), IndexTypes.UNIQUE_INDEX.getId());

    attribute.setValue(MdAttributeBooleanInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    assertEquals(attribute.getValue(MdAttributeBooleanInfo.REMOVE), MdAttributeBooleanInfo.TRUE);

    attribute.apply();
  }

  /**
   * Test setting of attributes on the character datatype minus any overlapping
   * attributes from the boolean test
   */
  public void testCreateCharacter()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeCharacterDAO mdAttribute = TestFixtureFactory.addCharacterAttribute(mdBusiness1);
    mdAttribute.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.TRUE);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testCharacter");
    AttributeEnumerationIF index = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    assertEquals(attribute.getStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Character Set Test");
    assertEquals(attribute.getValue(MdAttributeCharacterInfo.SIZE), "200");
    assertEquals(attribute.getValue(MdAttributeCharacterInfo.IMMUTABLE), MdAttributeBooleanInfo.TRUE);
    assertEquals(index.dereference()[0].getId(), IndexTypes.NO_INDEX.getId());
  }

  /**
   * Test the manual setting of the column name, as well as automatically
   * generated column name collisions.
   */
  public void testColumnNames()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeCharacterDAO mdAttributeCharacterDAO_1 = TestFixtureFactory.addCharacterAttribute(mdBusiness1);
    mdAttributeCharacterDAO_1.setColumnName("test_char");
    mdAttributeCharacterDAO_1.apply();

    // test column name collision
    MdAttributeCharacterDAO mdAttributeCharacterDAO_2 = TestFixtureFactory.addCharacterAttribute(mdBusiness1);
    mdAttributeCharacterDAO_2.getAttribute(MdAttributeCharacterInfo.NAME).setValue("testCharacterCharacterCharacterCharacter1");
    mdAttributeCharacterDAO_2.apply();
    MdAttributeCharacterDAO mdAttributeCharacterDAO_3 = TestFixtureFactory.addCharacterAttribute(mdBusiness1);
    mdAttributeCharacterDAO_3.getAttribute(MdAttributeCharacterInfo.NAME).setValue("testCharacterCharacterCharacterCharacter2");
    mdAttributeCharacterDAO_3.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF mdAttributeDAOIF_1 = mdEntityIF.definesAttribute("testCharacter");
    MdAttributeDAOIF mdAttributeDAOIF_2 = mdEntityIF.definesAttribute("testCharacterCharacterCharacterCharacter1");
    MdAttributeDAOIF mdAttributeDAOIF_3 = mdEntityIF.definesAttribute("testCharacterCharacterCharacterCharacter2");

    // Test manual column name
    assertEquals(mdAttributeDAOIF_1.getValue(MdAttributeCharacterInfo.COLUMN_NAME), "test_char");
    assertEquals(mdAttributeDAOIF_2.getValue(MdAttributeCharacterInfo.COLUMN_NAME), "test_character_character_cha");
    assertEquals(mdAttributeDAOIF_3.getValue(MdAttributeCharacterInfo.COLUMN_NAME), "test_character_character_ch0");
  }

  /**
   * Test setting of attributes on the date datatype minus any overlapping
   * attributes from the boolean test
   */
  public void testCreateDate()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    TestFixtureFactory.addDateAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testDate");
    AttributeEnumerationIF index = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    assertEquals(attribute.getStructValue(MdAttributeMomentInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Date Set Test");
    assertEquals(attribute.getValue(MdAttributeMomentInfo.REQUIRED), MdAttributeBooleanInfo.TRUE);
    assertEquals(attribute.getValue(MdAttributeMomentInfo.DEFAULT_VALUE), "2006-02-11");
    assertEquals(index.dereference()[0].getId(), IndexTypes.UNIQUE_INDEX.getId());
  }

  /**
   * Test setting of attributes on the date datatype minus any overlapping
   * attributes from the boolean test
   */
  public void testRequiredForDimension()
  {
    MdDimensionDAO mdDimension = TestFixtureFactory.createMdDimension();
    mdDimension.apply();

    try
    {

      MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
      mdBusiness1.apply();

      MdAttributeDateDAO mdAttribute = TestFixtureFactory.addDateAttribute(mdBusiness1);
      mdAttribute.apply();

      MdAttributeDimensionDAO mdAttributeDimension = mdAttribute.getMdAttributeDimension(mdDimension).getBusinessDAO();
      mdAttributeDimension.setValue(MdAttributeDimensionInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
      mdAttributeDimension.apply();

      SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

      TestFixtureFactory.delete(mdBusiness1);

      SAXImporter.runImport(new File(tempXMLFile));

      MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
      MdAttributeDAOIF mdAttributeIF = mdEntityIF.definesAttribute("testDate");
      MdAttributeDimensionDAOIF mdAttributeDimensionIF = mdAttributeIF.getMdAttributeDimension(mdDimension);
      AttributeEnumerationIF index = (AttributeEnumerationIF) mdAttributeIF.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

      assertEquals(mdAttributeDimensionIF.getValue(MdAttributeDimensionInfo.REQUIRED), MdAttributeBooleanInfo.TRUE);
      assertEquals(mdAttributeIF.getStructValue(MdAttributeMomentInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Date Set Test");
      assertEquals(mdAttributeIF.getValue(MdAttributeMomentInfo.REQUIRED), MdAttributeBooleanInfo.TRUE);
      assertEquals(mdAttributeIF.getValue(MdAttributeMomentInfo.DEFAULT_VALUE), "2006-02-11");
      assertEquals(index.dereference()[0].getId(), IndexTypes.UNIQUE_INDEX.getId());
    }
    finally
    {
      TestFixtureFactory.delete(mdDimension);
    }
  }

  /**
   * Test setting of attributes on the dateTime datatype minus any overlapping
   * attributes from the boolean test
   */
  public void testCreateDateTime()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    TestFixtureFactory.addDateTimeAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testDateTime");
    AttributeEnumerationIF index = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    assertEquals(attribute.getStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "dateTime Set Test");
    assertEquals(index.dereference()[0].getId(), IndexTypes.NON_UNIQUE_INDEX.getId());
  }

  /**
   * Test setting of attributes on the decimal datatype minus any overlapping
   * attributes from the boolean test
   */
  public void testCreateDecimal()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    TestFixtureFactory.addDecimalAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testDecimal");

    assertEquals(attribute.getStructValue(MdAttributeDecimalInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Decimal Set Test");
    assertEquals(attribute.getValue(MdAttributeDecimalInfo.LENGTH), "10");
    assertEquals(attribute.getValue(MdAttributeDecimalInfo.DECIMAL), "2");
    assertEquals(attribute.getValue(MdAttributeDecimalInfo.REJECT_NEGATIVE), MdAttributeBooleanInfo.TRUE);
  }

  /**
   * Test setting of attributes on the double datatype minus any overlapping
   * attributes from the boolean and decimal test
   */
  public void testCreateDouble()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    TestFixtureFactory.addDoubleAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testDouble");

    assertEquals(attribute.getStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Double Set Test");
    assertEquals(attribute.getValue(MdAttributeDoubleInfo.LENGTH), "9");
    assertEquals(attribute.getValue(MdAttributeDoubleInfo.DECIMAL), "4");
    assertEquals(attribute.getValue(MdAttributeDoubleInfo.REJECT_ZERO), MdAttributeBooleanInfo.TRUE);
  }

  /**
   * Test setting of attributes on the double datatype minus any overlapping
   * attributes from the boolean and decimal test
   */
  public void testCreateVirtual()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeDoubleDAO concrete = TestFixtureFactory.addDoubleAttribute(mdBusiness1);
    concrete.apply();

    MdViewDAO mdView = TestFixtureFactory.createMdView1();
    mdView.apply();

    TestFixtureFactory.addVirtualAttribute(mdView, concrete).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1, mdView }));

    TestFixtureFactory.delete(mdBusiness1);
    TestFixtureFactory.delete(mdView);

    SAXImporter.runImport(new File(tempXMLFile));

    MdViewDAOIF mdViewIF = MdViewDAO.getMdViewDAO(VIEW);
    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testDouble");
    MdAttributeDAOIF virtual = mdViewIF.definesAttribute("testVirtual");

    assertEquals(virtual.getValue(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE), attribute.getId());
  }

  /**
   * Test setting of attributes on the enumeration datatype minus any
   * overlapping attributes from the boolean test As a side effect does testing
   * on setting instance/instance_value tags
   */
  public void testCreateEnumeration()
  {
    MdBusinessDAO mdBusinessEnum1 = TestFixtureFactory.createEnumClass1();
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();

    mdBusinessEnum1.apply();
    mdBusiness1.apply();

    TestFixtureFactory.addCharacterAttribute(mdBusinessEnum1).apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusinessEnum1.definesType());
    businessDAO.setValue(EnumerationMasterInfo.NAME, "CO");
    businessDAO.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Colorado");
    businessDAO.setValue("testCharacter", "CO");
    businessDAO.apply();

    MdEnumerationDAO mdEnumeration = TestFixtureFactory.createMdEnumeation1(mdBusinessEnum1);
    mdEnumeration.apply();

    MdAttributeConcreteDAO addEnumerationAttribute = TestFixtureFactory.addEnumerationAttribute(mdBusiness1, mdEnumeration);
    addEnumerationAttribute.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, businessDAO.getId());
    addEnumerationAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1, mdBusinessEnum1, businessDAO, mdEnumeration }));

    // Delete test entities
    TestFixtureFactory.delete(mdBusiness1);
    TestFixtureFactory.delete(mdEnumeration);
    TestFixtureFactory.delete(mdBusinessEnum1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF mdAttributeIF = mdEntityIF.definesAttribute("testEnumeration");
    MdEnumerationDAOIF mdEnumerationIF = MdEnumerationDAO.getMdEnumerationDAO(FILTER);
    String defaultValue = mdAttributeIF.getDefaultValue();

    assertEquals("Enumeration Set Test", mdAttributeIF.getStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals(MdAttributeBooleanInfo.FALSE, mdAttributeIF.getValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE));
    assertEquals("CO", BusinessDAO.get(defaultValue).getValue(EnumerationMasterInfo.NAME));
    assertEquals("Colorado", BusinessDAO.get(defaultValue).getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));

    // Ensure that the enumeration is reference the correct enumeration class
    assertEquals(mdAttributeIF.getValue(MdAttributeEnumerationInfo.MD_ENUMERATION), mdEnumerationIF.getId());
  }

  /**
   * Test setting of attributes on a blob datatype
   */
  public void testCreateFile()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    TestFixtureFactory.addFileAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeConcreteDAO attribute = (MdAttributeConcreteDAO) ( mdEntityIF.definesAttribute("testFile") ).getBusinessDAO();
    AttributeEnumerationIF index = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    assertEquals(attribute.getStructValue(MdAttributeFileInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "File Set Test");
    assertEquals(attribute.getValue(MdAttributeFileInfo.IMMUTABLE), MdAttributeBooleanInfo.FALSE);
    assertEquals(MdAttributeBooleanInfo.TRUE, attribute.getValue(MdAttributeFileInfo.REQUIRED));
    assertEquals(MdAttributeBooleanInfo.TRUE, attribute.getValue(MdAttributeFileInfo.REMOVE));
    assertEquals(attribute.getStructValue(MetadataInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "File Test");
    assertEquals(index.dereference()[0].getId(), IndexTypes.NO_INDEX.getId());
  }

  /**
   * Test setting of attributes on the character datatype minus any overlapping
   * attributes from the boolean, decimal, and double test
   */
  public void testCreateFloat()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    TestFixtureFactory.addFloatAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testFloat");

    assertEquals(attribute.getStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Float Set Test");
    assertEquals(attribute.getValue(MdAttributeFloatInfo.LENGTH), "10");
    assertEquals(attribute.getValue(MdAttributeFloatInfo.DECIMAL), "2");
    assertEquals(attribute.getValue(MdAttributeFloatInfo.REJECT_POSITIVE), MdAttributeBooleanInfo.TRUE);
    assertEquals(attribute.getValue(MdAttributeFloatInfo.REJECT_NEGATIVE), MdAttributeBooleanInfo.TRUE);
  }

  /**
   * Test setting of attributes on the reference datatype minus any overlapping
   * attributes from the boolean test
   */
  public void testCreateReference()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();

    mdBusiness1.apply();
    mdBusiness2.apply();

    TestFixtureFactory.addCharacterAttribute(mdBusiness2).apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness2.definesType());
    businessDAO.setValue("testCharacter", "CO");
    businessDAO.apply();

    MdAttributeConcreteDAO addReferenceAttribute = TestFixtureFactory.addReferenceAttribute(mdBusiness1, mdBusiness2);
    addReferenceAttribute.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE, businessDAO.getId());
    addReferenceAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1, mdBusiness2, businessDAO }));

    TestFixtureFactory.delete(mdBusiness1);
    TestFixtureFactory.delete(mdBusiness2);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testReference");
    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(CLASS2);

    assertEquals(attribute.getStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Reference Test");

    // Ensure that the reference is referencing the correct class
    assertEquals(attribute.getValue(MdAttributeReferenceInfo.REF_MD_BUSINESS), mdBusinessIF.getId());
  }

  /**
   * Test setting of attributes on the integer datatype minus any overlapping
   * attributes from the boolean test
   */
  public void testCreateInteger()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    TestFixtureFactory.addIntegerAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testInteger");

    assertEquals(attribute.getStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Integer Set Test");
    assertEquals(attribute.getValue(MdAttributeIntegerInfo.REJECT_POSITIVE), MdAttributeBooleanInfo.TRUE);
    assertEquals(attribute.getValue(MdAttributeIntegerInfo.REJECT_ZERO), MdAttributeBooleanInfo.TRUE);
  }

  /**
   * Test setting of attributes on the long datatype minus any overlapping
   * attributes from the boolean test.
   */
  public void testCreateLong()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    TestFixtureFactory.addLongAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testLong");

    assertEquals(attribute.getStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Long Set Test");
    assertEquals(attribute.getValue(MdAttributeLongInfo.REJECT_ZERO), MdAttributeBooleanInfo.TRUE);
  }

  /**
   * Test setting of attributes on the text datatype minus any overlapping
   * attributes from the boolean test.
   */
  public void testCreateText()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    TestFixtureFactory.addTextAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testText");

    assertEquals(attribute.getStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Text Set Test");
  }

  /**
   * Test setting of attributes on the text datatype minus any overlapping
   * attributes from the boolean test.
   */
  public void testCreateClob()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    TestFixtureFactory.addClobAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testClob");

    assertEquals(attribute.getStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Clob Set Test");
  }

  /**
   * Test setting of attributes on the time datatype minus any overlapping
   * attributes from the boolean test
   */
  public void testCreateTime()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    TestFixtureFactory.addTimeAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testTime");

    assertEquals(attribute.getStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Time Set Test");
  }

  /**
   * Test setting of attributes on the struct datatype minus any overlapping
   * attributes from the boolean test
   */
  public void testCreateStruct()
  {
    MdStructDAO mdStruct = TestFixtureFactory.createMdStruct1();
    mdStruct.setStructValue(MdStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Struct Set Test");
    mdStruct.setValue(MdStructInfo.CACHE_SIZE, "525");
    mdStruct.apply();

    MdAttributeBooleanDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdStruct);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFAULT_VALUE, MdAttributeBooleanInfo.FALSE);
    mdAttributeBoolean.apply();

    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    TestFixtureFactory.addStructAttribute(mdBusiness1, mdStruct).apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO.setStructValue("testStruct", "testBoolean", MdAttributeBooleanInfo.TRUE);
    businessDAO.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1, mdStruct, businessDAO }));

    String structType = mdStruct.definesType();

    TestFixtureFactory.delete(mdBusiness1);
    TestFixtureFactory.delete(mdStruct);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF mdAttributeIF = mdEntityIF.definesAttribute("testStruct");
    MdStructDAOIF mdStructIF = MdStructDAO.getMdStructDAO(structType);

    assertEquals(mdAttributeIF.getStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Struct Set Test");
    assertEquals(mdStructIF.getValue(MdBusinessInfo.CACHE_SIZE), "525");

    // Ensure that the correct class is being referenced
    assertEquals(mdAttributeIF.getValue(MdAttributeStructInfo.MD_STRUCT), mdStructIF.getId());

    List<String> ids = EntityDAO.getEntityIdsDB(CLASS);

    assertEquals(1, ids.size());

    BusinessDAOIF businessDAOIF = BusinessDAO.get(ids.get(0));
    AttributeStruct attribute = (AttributeStruct) businessDAOIF.getAttributeIF("testStruct");

    assertEquals(MdAttributeBooleanInfo.TRUE, attribute.getValue("testBoolean"));
  }

  /**
   * Test setting of symmetric attribute specific value s
   */
  public void testCreateSymmetric()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    TestFixtureFactory.addSymmetricAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeSymmetricDAOIF attribute = (MdAttributeSymmetricDAOIF) mdEntityIF.definesAttribute("testSymmetric");

    // Ensure that the symmetric encryption method is set
    AttributeEnumerationIF method = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeSymmetricInfo.SYMMETRIC_METHOD);
    assertEquals(SymmetricMethods.DES.getId(), method.dereference()[0].getId());
    assertEquals("56", attribute.getValue(MdAttributeSymmetricInfo.SECRET_KEY_SIZE));
  }

  /**
   * Test setting of mdHash attribute specific values
   */
  public void testCreateHash()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    TestFixtureFactory.addHashAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeHashDAOIF attribute = (MdAttributeHashDAOIF) mdEntityIF.definesAttribute("testHash");

    // Ensure that the hash encryption method is set
    AttributeEnumerationIF method = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeHashInfo.HASH_METHOD);
    assertEquals(HashMethods.MD5.getId(), method.dereference()[0].getId());
  }

  /**
   * Test the setting of cache size on both class and struct.
   */
  public void testCacheAlgorithmSet()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getId());
    mdBusiness1.setValue(MdBusinessInfo.CACHE_SIZE, "50");
    mdBusiness1.apply();

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.apply();

    MdStructDAO mdStruct = TestFixtureFactory.createMdStruct1();
    mdStruct.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getId());
    mdStruct.apply();

    MdRelationshipDAO mdRelationship1 = TestFixtureFactory.createMdRelationship1(mdBusiness1, mdBusiness2);
    mdRelationship1.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
    mdRelationship1.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1, mdBusiness2, mdStruct, mdRelationship1 }));

    String structType = mdStruct.definesType();

    TestFixtureFactory.delete(mdRelationship1);
    TestFixtureFactory.delete(mdBusiness2);
    TestFixtureFactory.delete(mdBusiness1);
    TestFixtureFactory.delete(mdStruct);

    SAXImporter.runImport(new File(tempXMLFile));

    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(CLASS);
    MdStructDAOIF mdStruct2 = MdStructDAO.getMdStructDAO(structType);
    MdRelationshipDAOIF mdRelationship = MdRelationshipDAO.getMdRelationshipDAO(RELATIONSHIP);

    // Ensure that the correct cache size is set on standard classes
    AttributeEnumerationIF attribute = (AttributeEnumerationIF) mdBusinessIF.getAttributeIF(MdElementInfo.CACHE_ALGORITHM);
    assertEquals(EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getId(), attribute.dereference()[0].getId());

    // Ensure that the correct cache size is set on structs
    attribute = (AttributeEnumerationIF) mdStruct2.getAttributeIF(MdElementInfo.CACHE_ALGORITHM);
    assertEquals(EntityCacheMaster.CACHE_EVERYTHING.getId(), attribute.dereference()[0].getId());

    // Ensure that the correct cache algorithm is set on a relationship
    attribute = (AttributeEnumerationIF) mdRelationship.getAttributeIF(MdElementInfo.CACHE_ALGORITHM);
    assertEquals(EntityCacheMaster.CACHE_NOTHING.getId(), attribute.dereference()[0].getId());
  }

  /**
   * Test setting of attributes of and on the class datatype
   */
  public void testCreateMdBusiness()
  {
    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdBusiness1.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdBusiness1.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdBusiness1.setValue(MdBusinessInfo.CACHE_SIZE, "50");
    mdBusiness1.setValue(MdBusinessInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    TestFixtureFactory.addBooleanAttribute(mdBusiness1).apply();

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdBusiness1.getId());
    mdBusiness2.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdBusiness2.apply();

    // Create test MdStateMachine
    MdStateMachineDAO mdStateMachine = MdStateMachineDAO.newInstance();
    mdStateMachine.setValue(MdStateMachineInfo.NAME, "StateMachine1");
    mdStateMachine.setValue(MdStateMachineInfo.PACKAGE, "test.xmlclasses");
    mdStateMachine.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "StateMachine1");
    mdStateMachine.setStructValue(MdStateMachineInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "State Machine of Class1");
    mdStateMachine.setValue(MdStateMachineInfo.SUPER_MD_BUSINESS, EntityTypes.STATE_MASTER.getId());
    mdStateMachine.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness1.getId());
    mdStateMachine.apply();

    // Add states
    StateMasterDAO state1 = mdStateMachine.addState("Available", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
    state1.apply();

    StateMasterDAO state2 = mdStateMachine.addState("CheckedOut", StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId());
    state2.apply();

    StateMasterDAO state3 = mdStateMachine.addState("CheckedIn", StateMasterDAOIF.Entry.ENTRY_STATE.getId());
    state3.apply();

    mdStateMachine.addTransition("CheckOut", state1.getId(), state2.getId()).apply();
    mdStateMachine.addTransition("CheckIn", state2.getId(), state3.getId()).apply();
    mdStateMachine.addTransition("Stock", state3.getId(), state1.getId()).apply();

    mdBusiness1.setValue(MdBusinessInfo.STUB_SOURCE, TestFixtureFactory.getMdBusinessStub());

    MdBusinessDAO updateBusiness = MdBusinessDAO.get(mdBusiness1.getId()).getBusinessDAO();
    updateBusiness.setValue(MdBusinessInfo.STUB_SOURCE, TestFixtureFactory.getMdBusinessStub());
    updateBusiness.apply();

    // Export the test entities
    ExportMetadata metadata = new ExportMetadata(true);
    metadata.addCreate(new ComponentIF[] { mdBusiness1, mdBusiness2 });

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    // Delete the test entites
    TestFixtureFactory.delete(mdBusiness2);
    TestFixtureFactory.delete(mdBusiness1);

    // Import the test entites
    SAXImporter.runImport(new File(tempXMLFile));

    MdBusinessDAOIF mdBusiness1IF = MdBusinessDAO.getMdBusinessDAO(CLASS);
    MdBusinessDAOIF mdBusiness2IF = MdBusinessDAO.getMdBusinessDAO(CLASS2);

    MdAttributeDAOIF attribute = mdBusiness1IF.definesAttribute("testBoolean");

    assertEquals(mdBusiness1IF.getStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdBusiness Set Test");
    assertEquals(mdBusiness1IF.getStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdBusiness Attributes Test");
    assertEquals(mdBusiness1IF.getValue(MdBusinessInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdBusiness1IF.getValue(MdBusinessInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdBusiness1IF.getValue(MdBusinessInfo.CACHE_SIZE), "50");
    assertEquals(MdAttributeBooleanInfo.FALSE, mdBusiness1IF.getValue(MdBusinessInfo.PUBLISH));
    assertEquals(TestFixtureFactory.getMdBusinessStub().trim(), mdBusiness1IF.getValue(MdBusinessInfo.STUB_SOURCE).trim());

    // Change to false when casscading delete is implemented
    assertEquals(mdBusiness1IF.getValue(MetadataInfo.REMOVE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdBusiness2IF.getValue(MdElementInfo.EXTENDABLE), MdAttributeBooleanInfo.FALSE);

    // Ensure inheritance is linking to the correct super class
    assertEquals(mdBusiness2IF.getValue(MdBusinessInfo.SUPER_MD_BUSINESS), mdBusiness1IF.getId());

    // Ensure the attributes are linked to the correct MdBusiness object
    assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdBusiness1IF.getId());

    // Test that the MdStateMachine on the MdBusiness was correctly set
    MdStateMachineDAOIF mdStateMachineIF = mdBusiness1IF.definesMdStateMachine();

    assertNotNull(mdStateMachineIF);
    assertEquals(3, mdStateMachineIF.definesStateMasters().size());

    // Ensure that the states were imported correctly
    StateMasterDAOIF available = mdStateMachineIF.definesStateMaster("Available");
    assertEquals(true, available.isDefaultState());
    assertEquals(true, available.isEntryState());

    StateMasterDAOIF checkedOut = mdStateMachineIF.definesStateMaster("CheckedOut");
    assertEquals(false, checkedOut.isDefaultState());
    assertEquals(false, checkedOut.isEntryState());

    StateMasterDAOIF checkedIn = mdStateMachineIF.definesStateMaster("CheckedIn");
    assertEquals(false, checkedIn.isDefaultState());
    assertEquals(true, checkedIn.isEntryState());

    // Ensure that the transitions were imported correctly
    assertEquals(3, mdStateMachineIF.definesTransitions().size());

    TransitionDAOIF checkOut = mdStateMachineIF.definesTransition("CheckOut");
    assertEquals(available.getId(), checkOut.getParentId());
    assertEquals(checkedOut.getId(), checkOut.getChildId());

    RelationshipDAOIF checkIn = mdStateMachineIF.definesTransition("CheckIn");
    assertEquals(checkedOut.getId(), checkIn.getParentId());
    assertEquals(checkedIn.getId(), checkIn.getChildId());

    RelationshipDAOIF stock = mdStateMachineIF.definesTransition("Stock");
    assertEquals(checkedIn.getId(), stock.getParentId());
    assertEquals(available.getId(), stock.getChildId());

    mdBusiness2 = mdBusiness2IF.getBusinessDAO();
    mdBusiness2.setValue(MetadataInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdBusiness2.apply();
  }

  /**
   * Test setting of attributes of and on the struct datatype,
   */
  public void testCreateMdStruct()
  {
    SAXImporter.runImport(new File(STANDALONE_STRUCT_SET));

    MdStructDAO mdStruct = ( (MdStructDAO) MdStructDAO.getMdStructDAO(CLASS) ).getBusinessDAO();
    MdAttributeConcreteDAOIF attribute = mdStruct.definesAttribute("testBoolean");

    assertEquals("Struct Set Test", mdStruct.getStructValue(MdStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals("Set Struct Attributes Test", mdStruct.getStructValue(MdAttributeStructInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals(MdAttributeBooleanInfo.TRUE, mdStruct.getValue(MdAttributeStructInfo.REMOVE));

    // Ensure the attributes are linked to the struct
    assertEquals(mdStruct.getId(), attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS));
  }

  /**
   * Test setting of attributes of and on the relationship datatype
   */
  public void testCreateMdRelationship()
  {
    try
    {
      SAXImporter.runImport(new File(RELATIONSHIP_SET));

      MdRelationshipDAO mdRelationship = MdRelationshipDAO.getMdRelationshipDAO(RELATIONSHIP).getBusinessDAO();
      MdBusinessDAOIF mdBusiness1 = MdBusinessDAO.getMdBusinessDAO(CLASS);
      MdBusinessDAOIF mdBusiness2 = MdBusinessDAO.getMdBusinessDAO(CLASS2);
      MdAttributeConcreteDAOIF attribute = mdRelationship.definesAttribute("testBoolean");

      assertEquals("Relationship Set Test", mdRelationship.getStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
      assertEquals("Relationship Test", mdRelationship.getStructValue(MdRelationshipInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
      assertEquals(MdAttributeBooleanInfo.FALSE, mdRelationship.getValue(MdRelationshipInfo.REMOVE));
      assertEquals(MdAttributeBooleanInfo.TRUE, mdRelationship.getValue(MdRelationshipInfo.EXTENDABLE));
      assertEquals(MdAttributeBooleanInfo.TRUE, mdRelationship.getValue(MdRelationshipInfo.ABSTRACT));

      // Ensure the parent attributes are correctly set
      assertEquals(mdRelationship.getValue(MdRelationshipInfo.PARENT_MD_BUSINESS), mdBusiness1.getId());
      assertEquals(mdRelationship.getValue(MdRelationshipInfo.PARENT_CARDINALITY), "1");
      assertEquals(mdRelationship.getStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Parent Set Test");

      // Ensure the child attributes are correctly set
      assertEquals(mdRelationship.getValue(MdRelationshipInfo.CHILD_MD_BUSINESS), mdBusiness2.getId());
      assertEquals(mdRelationship.getValue(MdRelationshipInfo.CHILD_CARDINALITY), "*");
      assertEquals(mdRelationship.getStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Child Set Test");

      // Ensure the attributes are linked to the relationship
      assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdRelationship.getId());

      mdRelationship.setValue(MetadataInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdRelationship.apply();

      // Test the sub relationship
      MdRelationshipDAO mdRelationship2 = MdRelationshipDAO.getMdRelationshipDAO(RELATIONSHIP2).getBusinessDAO();
      assertEquals(mdRelationship.getId(), mdRelationship2.getValue(MdRelationshipInfo.SUPER_MD_RELATIONSHIP));
      assertEquals("2", mdRelationship2.getValue(MdRelationshipInfo.CHILD_CARDINALITY));
    }
    catch (RuntimeException e)
    {
      e.printStackTrace();
      throw e;
    }
  }

  /**
   * Test setting of attributes of and on the class datatype
   */
  public void testCreateMdProblem()
  {
    // Create test MdBusiness
    MdProblemDAO mdProblem1 = TestFixtureFactory.createMdProblem1();
    mdProblem1.setValue(MdProblemInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdProblem1.setValue(MdProblemInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdProblem1.setValue(MdProblemInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdProblem1.setValue(MdProblemInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdProblem1.apply();

    TestFixtureFactory.addBooleanAttribute(mdProblem1).apply();

    MdProblemDAO mdProblem2 = TestFixtureFactory.createMdProblem2();
    mdProblem2.setValue(MdProblemInfo.SUPER_MD_PROBLEM, mdProblem1.getId());
    mdProblem2.setValue(MdProblemInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdProblem2.apply();

    // Export the test entities
    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdProblem2, mdProblem1 }));

    // Delete the test entites
    TestFixtureFactory.delete(mdProblem2);
    TestFixtureFactory.delete(mdProblem1);

    // Import the test entites
    SAXImporter.runImport(new File(tempXMLFile));

    MdProblemDAOIF mdProblem1IF = MdProblemDAO.getMdProblem(EXCEPTION);
    MdProblemDAOIF mdProblem2IF = MdProblemDAO.getMdProblem(EXCEPTION2);

    MdAttributeDAOIF attribute = mdProblem1IF.definesAttribute("testBoolean");

    assertEquals(mdProblem1IF.getStructValue(MdProblemInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdProblem Set Test");
    assertEquals(mdProblem1IF.getStructValue(MdProblemInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdProblem Attributes Test");
    assertEquals(mdProblem1IF.getValue(MdProblemInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdProblem1IF.getValue(MdProblemInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    assertEquals(MdAttributeBooleanInfo.FALSE, mdProblem1IF.getValue(MdProblemInfo.PUBLISH));

    // Change to false when casscading delete is implemented
    assertEquals(mdProblem1IF.getValue(MdProblemInfo.REMOVE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdProblem2IF.getValue(MdProblemInfo.EXTENDABLE), MdAttributeBooleanInfo.FALSE);

    // Ensure inheritance is linking to the correct super class
    assertEquals(mdProblem2IF.getValue(MdProblemInfo.SUPER_MD_PROBLEM), mdProblem1IF.getId());

    // Ensure the attributes are linked to the correct MdBusiness object
    assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdProblem1IF.getId());
  }

  /**
   * Test setting of attributes of and on the class datatype
   */
  public void testCreateMdInformation()
  {
    // Create test MdBusiness
    MdInformationDAO mdInformation1 = TestFixtureFactory.createMdInformation1();
    mdInformation1.setValue(MdInformationInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdInformation1.setValue(MdInformationInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdInformation1.setValue(MdInformationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdInformation1.setValue(MdInformationInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdInformation1.apply();

    TestFixtureFactory.addBooleanAttribute(mdInformation1).apply();

    MdInformationDAO mdInformation2 = TestFixtureFactory.createMdInformation2();
    mdInformation2.setValue(MdInformationInfo.SUPER_MD_INFORMATION, mdInformation1.getId());
    mdInformation2.setValue(MdInformationInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdInformation2.apply();

    // Export the test entities
    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdInformation2, mdInformation1 }));

    // Delete the test entites
    TestFixtureFactory.delete(mdInformation1);
    TestFixtureFactory.delete(mdInformation2);

    // Import the test entites
    SAXImporter.runImport(new File(tempXMLFile));

    MdInformationDAOIF mdInformation1IF = MdInformationDAO.getMdInformation(INFORMATION);
    MdInformationDAOIF mdInformation2IF = MdInformationDAO.getMdInformation(INFORMATION2);

    MdAttributeDAOIF attribute = mdInformation1IF.definesAttribute("testBoolean");

    assertEquals(mdInformation1IF.getStructValue(MdInformationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdInformation Set Test");
    assertEquals(mdInformation1IF.getStructValue(MdInformationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdInformation Attributes Test");
    assertEquals(mdInformation1IF.getValue(MdInformationInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdInformation1IF.getValue(MdInformationInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    assertEquals(MdAttributeBooleanInfo.FALSE, mdInformation1IF.getValue(MdInformationInfo.PUBLISH));

    // Change to false when casscading delete is implemented
    assertEquals(mdInformation1IF.getValue(MdInformationInfo.REMOVE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdInformation2IF.getValue(MdInformationInfo.EXTENDABLE), MdAttributeBooleanInfo.FALSE);

    // Ensure inheritance is linking to the correct super class
    assertEquals(mdInformation2IF.getValue(MdInformationInfo.SUPER_MD_INFORMATION), mdInformation1IF.getId());

    // Ensure the attributes are linked to the correct MdBusiness object
    assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdInformation1IF.getId());
  }

  /**
   * Test setting of attributes of and on the class datatype
   */
  public void testCreateMdException()
  {
    // Create test MdBusiness
    MdExceptionDAO mdException1 = TestFixtureFactory.createMdException1();
    mdException1.setValue(MdExceptionInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdException1.setValue(MdExceptionInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdException1.setValue(MdExceptionInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdException1.setValue(MdExceptionInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdException1.apply();

    TestFixtureFactory.addBooleanAttribute(mdException1).apply();

    MdExceptionDAO mdException2 = TestFixtureFactory.createMdException2();
    mdException2.setValue(MdExceptionInfo.SUPER_MD_EXCEPTION, mdException1.getId());
    mdException2.setValue(MdExceptionInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdException2.apply();

    // Export the test entities
    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdException2, mdException1 }));

    // Delete the test entities
    TestFixtureFactory.delete(mdException2);
    TestFixtureFactory.delete(mdException1);

    // Import the test entities
    SAXImporter.runImport(new File(tempXMLFile));

    MdExceptionDAOIF mdException1IF = MdExceptionDAO.getMdException(EXCEPTION);
    MdExceptionDAOIF mdException2IF = MdExceptionDAO.getMdException(EXCEPTION2);

    MdAttributeDAOIF attribute = mdException1IF.definesAttribute("testBoolean");

    assertEquals(mdException1IF.getStructValue(MdExceptionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdException Set Test");
    assertEquals(mdException1IF.getStructValue(MdExceptionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdException Attributes Test");
    assertEquals(mdException1IF.getValue(MdExceptionInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdException1IF.getValue(MdExceptionInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    assertEquals(MdAttributeBooleanInfo.FALSE, mdException1IF.getValue(MdBusinessInfo.PUBLISH));

    // Change to false when cascading delete is implemented
    assertEquals(mdException1IF.getValue(MdExceptionInfo.REMOVE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdException2IF.getValue(MdExceptionInfo.EXTENDABLE), MdAttributeBooleanInfo.FALSE);

    // Ensure inheritance is linking to the correct super class
    assertEquals(mdException2IF.getValue(MdExceptionInfo.SUPER_MD_EXCEPTION), mdException1IF.getId());

    // Ensure the attributes are linked to the correct MdBusiness object
    assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdException1IF.getId());
  }

  public void testCreateMdView()
  {
    // Create test MdView
    MdViewDAO mdView1 = TestFixtureFactory.createMdView1();
    mdView1.setValue(MdViewInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdView1.setValue(MdViewInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdView1.setValue(MdViewInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdView1.setValue(MdViewInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdView1.apply();

    TestFixtureFactory.addBooleanAttribute(mdView1).apply();

    MdMethodDAO mdMethod = MdMethodDAO.newInstance();
    mdMethod.setValue(MdMethodInfo.NAME, "checkin");
    mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdView1.getId());
    mdMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkin");
    mdMethod.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkin");
    mdMethod.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    mdMethod.apply();

    MdViewDAO mdView2 = TestFixtureFactory.createMdView2();
    mdView2.setValue(MdViewInfo.SUPER_MD_VIEW, mdView1.getId());
    mdView2.setValue(MdViewInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdView2.apply();

    // Export the test entities
    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdView2, mdView1 }));

    String type1 = mdView1.definesType();
    String view2 = mdView2.definesType();

    // Delete the test entites
    TestFixtureFactory.delete(mdView2);
    TestFixtureFactory.delete(mdView1);

    // Import the test entites
    SAXImporter.runImport(new File(tempXMLFile));

    MdViewDAOIF mdView1IF = MdViewDAO.getMdViewDAO(type1);
    MdViewDAOIF mdView2IF = MdViewDAO.getMdViewDAO(view2);

    MdAttributeDAOIF attribute = mdView1IF.definesAttribute("testBoolean");

    assertEquals(mdView1IF.getStructValue(MdViewInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdException Set Test");
    assertEquals(mdView1IF.getStructValue(MdViewInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdException Attributes Test");
    assertEquals(mdView1IF.getValue(MdViewInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdView1IF.getValue(MdViewInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    assertEquals(MdAttributeBooleanInfo.FALSE, mdView1IF.getValue(MdBusinessInfo.PUBLISH));

    // Change to false when casscading delete is implemented
    assertEquals(mdView1IF.getValue(MdViewInfo.REMOVE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdView2IF.getValue(MdViewInfo.EXTENDABLE), MdAttributeBooleanInfo.FALSE);

    // Ensure inheritance is linking to the correct super class
    assertEquals(mdView2IF.getValue(MdViewInfo.SUPER_MD_VIEW), mdView1IF.getId());

    // Ensure the attributes are linked to the correct MdBusiness object
    assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdView1IF.getId());

    List<MdMethodDAOIF> mdMethods = mdView1IF.getMdMethods();

    assertEquals(1, mdMethods.size());

    assertEquals(true, mdMethods.get(0).isStatic());
    assertEquals("checkin", mdMethods.get(0).getName());
    assertTrue(mdMethods.get(0).getReturnType().isVoid());
    assertEquals("checkin", mdMethods.get(0).getDisplayLabel(CommonProperties.getDefaultLocale()));
    assertEquals("checkin", mdMethods.get(0).getDescription(CommonProperties.getDefaultLocale()));
  }

  public void testCreateMdUtil()
  {
    // Create test MdView
    MdUtilDAO mdUtil1 = TestFixtureFactory.createMdUtil1();
    mdUtil1.setValue(MdUtilInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdUtil1.setValue(MdUtilInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdUtil1.setValue(MdUtilInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdUtil1.setValue(MdUtilInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdUtil1.apply();

    TestFixtureFactory.addBooleanAttribute(mdUtil1).apply();

    MdMethodDAO mdMethod = MdMethodDAO.newInstance();
    mdMethod.setValue(MdMethodInfo.NAME, "checkin");
    mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdUtil1.getId());
    mdMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkin");
    mdMethod.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkin");
    mdMethod.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    mdMethod.apply();

    MdUtilDAO mdUtil2 = TestFixtureFactory.createMdUtil2();
    mdUtil2.setValue(MdUtilInfo.SUPER_MD_UTIL, mdUtil1.getId());
    mdUtil2.setValue(MdUtilInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdUtil2.apply();

    // Export the test entities
    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdUtil2, mdUtil1 }));

    String type1 = mdUtil1.definesType();
    String type2 = mdUtil2.definesType();

    // Delete the test entites
    TestFixtureFactory.delete(mdUtil2);
    TestFixtureFactory.delete(mdUtil1);

    // Import the test entites
    SAXImporter.runImport(new File(tempXMLFile));

    MdUtilDAOIF mdUtil1IF = MdUtilDAO.getMdUtil(type1);
    MdUtilDAOIF mdUtil2IF = MdUtilDAO.getMdUtil(type2);

    MdAttributeDAOIF attribute = mdUtil1IF.definesAttribute("testBoolean");

    assertEquals(mdUtil1IF.getStructValue(MdUtilInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdException Set Test");
    assertEquals(mdUtil1IF.getStructValue(MdUtilInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdException Attributes Test");
    assertEquals(mdUtil1IF.getValue(MdUtilInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdUtil1IF.getValue(MdUtilInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    assertEquals(MdAttributeBooleanInfo.FALSE, mdUtil1IF.getValue(MdBusinessInfo.PUBLISH));

    // Change to false when casscading delete is implemented
    assertEquals(mdUtil1IF.getValue(MdUtilInfo.REMOVE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdUtil2IF.getValue(MdUtilInfo.EXTENDABLE), MdAttributeBooleanInfo.FALSE);

    // Ensure inheritance is linking to the correct super class
    assertEquals(mdUtil2IF.getValue(MdUtilInfo.SUPER_MD_UTIL), mdUtil1IF.getId());

    // Ensure the attributes are linked to the correct MdBusiness object
    assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdUtil1IF.getId());

    List<MdMethodDAOIF> mdMethods = mdUtil1IF.getMdMethods();

    assertEquals(1, mdMethods.size());

    assertEquals(true, mdMethods.get(0).isStatic());
    assertEquals("checkin", mdMethods.get(0).getName());
    assertTrue(mdMethods.get(0).getReturnType().isVoid());
    assertEquals("checkin", mdMethods.get(0).getDisplayLabel(CommonProperties.getDefaultLocale()));
    assertEquals("checkin", mdMethods.get(0).getDescription(CommonProperties.getDefaultLocale()));
  }

  public void testCreateMdWebForm()
  {
    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.apply();

    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    MdAttributeCharacterDAO mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdBusiness);
    mdAttributeCharacter.apply();

    MdAttributeBooleanDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdBusiness);
    mdAttributeBoolean.apply();

    MdAttributeIntegerDAO mdAttributeInteger = TestFixtureFactory.addIntegerAttribute(mdBusiness);
    mdAttributeInteger.apply();

    MdAttributeLongDAO mdAttributeLong = TestFixtureFactory.addLongAttribute(mdBusiness);
    mdAttributeLong.apply();

    MdAttributeDoubleDAO mdAttributeDouble = TestFixtureFactory.addDoubleAttribute(mdBusiness);
    mdAttributeDouble.apply();

    MdAttributeFloatDAO mdAttributeFloat = TestFixtureFactory.addFloatAttribute(mdBusiness);
    mdAttributeFloat.apply();

    MdAttributeReferenceDAO mdAttributeReference = TestFixtureFactory.addReferenceAttribute(mdBusiness, mdBusiness2);
    mdAttributeReference.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdWebForm, mdAttributeCharacter);
    mdWebCharacter.apply();

    MdWebBooleanDAO mdWebBoolean = TestFixtureFactory.addBooleanField(mdWebForm, mdAttributeBoolean);
    mdWebBoolean.apply();

    MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdWebForm, mdAttributeInteger);
    mdWebInteger.apply();

    MdWebLongDAO mdWebLong = TestFixtureFactory.addLongField(mdWebForm, mdAttributeLong);
    mdWebLong.apply();

    MdWebFloatDAO mdWebFloat = TestFixtureFactory.addFloatField(mdWebForm, mdAttributeFloat);
    mdWebFloat.apply();

    MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdWebForm, mdAttributeDouble);
    mdWebDouble.apply();

    MdWebReferenceDAO mdWebReference = TestFixtureFactory.addReferenceField(mdWebForm, mdAttributeReference);
    mdWebReference.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    assertEquals(mdWebForm.getFormName(), test.getFormName());
    assertEquals(mdBusiness.definesType(), test.getFormMdClass().definesType());
    assertEquals(mdWebForm.getDescription(CommonProperties.getDefaultLocale()), test.getDescription(CommonProperties.getDefaultLocale()));
    assertEquals(mdWebForm.getDisplayLabel(CommonProperties.getDefaultLocale()), test.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    assertEquals(7, fields.size());

    MdWebCharacterDAO testField = (MdWebCharacterDAO) fields.get(0);

    assertEquals(mdWebCharacter.getFieldName(), testField.getFieldName());
    assertEquals(mdWebCharacter.getFieldOrder(), testField.getFieldOrder());
    assertEquals(mdWebCharacter.getDescription(CommonProperties.getDefaultLocale()), testField.getDescription(CommonProperties.getDefaultLocale()));
    assertEquals(mdWebCharacter.getDisplayLabel(CommonProperties.getDefaultLocale()), testField.getDisplayLabel(CommonProperties.getDefaultLocale()));
    assertEquals(mdAttributeCharacter.definesAttribute(), testField.getDefiningMdAttribute().definesAttribute());
  }

  public void testSelectionSet()
  {
    MdBusinessDAO mdBusinessEnum1 = TestFixtureFactory.createEnumClass1();
    mdBusinessEnum1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addFloatAttribute(mdBusinessEnum1);
    mdAttribute.setValue(MdAttributeFloatInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.FALSE);
    mdAttribute.setValue(MdAttributeFloatInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttribute.apply();

    BusinessDAO businessDAO1 = BusinessDAO.newInstance(mdBusinessEnum1.definesType());
    businessDAO1.setValue(EnumerationMasterInfo.NAME, "ONE");
    businessDAO1.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "One");
    businessDAO1.setValue("testFloat", "9.1");
    businessDAO1.apply();

    BusinessDAO businessDAO2 = BusinessDAO.newInstance(mdBusinessEnum1.definesType());
    businessDAO2.setValue(EnumerationMasterInfo.NAME, "TWO");
    businessDAO2.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Two");
    businessDAO2.setValue("testFloat", "3.1");
    businessDAO2.apply();

    BusinessDAO businessDAO3 = BusinessDAO.newInstance(mdBusinessEnum1.definesType());
    businessDAO3.setValue(EnumerationMasterInfo.NAME, "THREE");
    businessDAO3.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Three");
    businessDAO3.setValue("testFloat", "435329.1");
    businessDAO3.apply();

    MdEnumerationDAO mdEnumeration = TestFixtureFactory.createMdEnumeation1(mdBusinessEnum1);
    mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.FALSE);
    mdEnumeration.apply();

    mdEnumeration.addEnumItem(businessDAO1.getId());
    mdEnumeration.addEnumItem(businessDAO3.getId());

    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    TestFixtureFactory.addTimeAttribute(mdBusiness1).apply();
    MdAttributeConcreteDAO mdAttributeEnum = TestFixtureFactory.addEnumerationAttribute(mdBusiness1, mdEnumeration);
    mdAttributeEnum.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeEnum.apply();

    BusinessDAO businessDAO4 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO4.setValue("testTime", "23:45:32");
    businessDAO4.addItem("testEnumeration", businessDAO1.getId());
    businessDAO4.addItem("testEnumeration", businessDAO3.getId());
    businessDAO4.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { businessDAO4, mdBusiness1, mdBusinessEnum1, mdEnumeration, businessDAO1, businessDAO2, businessDAO3 }));

    TestFixtureFactory.delete(mdEnumeration);
    TestFixtureFactory.delete(mdBusinessEnum1);
    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO("test.xmlclasses.Class1");
    List<String> ids = EntityDAO.getEntityIdsDB(mdBusinessIF.definesType());

    assertEquals(1, ids.size());

    String id = ids.get(0);

    BusinessDAOIF businessDAO = BusinessDAO.get(id);

    AttributeEnumerationIF attribute = (AttributeEnumerationIF) businessDAO.getAttributeIF("testEnumeration");
    BusinessDAOIF[] enums = attribute.dereference();

    assertEquals("23:45:32", businessDAO.getValue("testTime"));
    assertEquals(2, enums.length);
    assertTrue(enums[0].getValue(EnumerationMasterInfo.NAME).equals("ONE") || enums[0].getValue(EnumerationMasterInfo.NAME).equals("THREE"));
    assertTrue(enums[0].getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE).equals("One") || enums[0].getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE).equals("Three"));

    assertTrue(enums[1].getValue(EnumerationMasterInfo.NAME).equals("ONE") || enums[1].getValue(EnumerationMasterInfo.NAME).equals("THREE"));
    assertTrue(enums[1].getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE).equals("One") || enums[1].getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE).equals("Three"));
  }

  /**
   * Test setting of attributes of and on the relationship datatype
   */
  public void testTreeSet()
  {
    SAXImporter.runImport(new File(TREE_SET));

    assertTrue(MdTypeDAO.isDefined(RELATIONSHIP));

    MdTreeDAO mdRelationship = MdTreeDAO.getMdTreeDAO(RELATIONSHIP).getBusinessDAO();
    MdBusinessDAOIF mdBusiness1 = MdBusinessDAO.getMdBusinessDAO(CLASS);
    MdBusinessDAOIF mdBusiness2 = MdBusinessDAO.getMdBusinessDAO(CLASS2);
    MdAttributeConcreteDAOIF attribute = mdRelationship.definesAttribute("testBoolean");

    assertEquals("Relationship Set Test", mdRelationship.getStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals("Relationship Test", mdRelationship.getStructValue(MdRelationshipInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals(MdAttributeBooleanInfo.FALSE, mdRelationship.getValue(MdRelationshipInfo.REMOVE));
    assertEquals(MdAttributeBooleanInfo.TRUE, mdRelationship.getValue(MdRelationshipInfo.EXTENDABLE));
    assertEquals(MdAttributeBooleanInfo.TRUE, mdRelationship.getValue(MdRelationshipInfo.ABSTRACT));

    // Ensure the parent attributes are correctly set
    assertEquals(mdRelationship.getValue(MdRelationshipInfo.PARENT_MD_BUSINESS), mdBusiness1.getId());
    assertEquals(mdRelationship.getValue(MdRelationshipInfo.PARENT_CARDINALITY), "1");
    assertEquals(mdRelationship.getStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Parent Set Test");

    // Ensure the child attributes are correctly set
    assertEquals(mdRelationship.getValue(MdRelationshipInfo.CHILD_MD_BUSINESS), mdBusiness2.getId());
    assertEquals(mdRelationship.getValue(MdRelationshipInfo.CHILD_CARDINALITY), "*");
    assertEquals(mdRelationship.getStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Child Set Test");

    // Ensure the attributes are linked to the relationship
    assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdRelationship.getId());

    mdRelationship.setValue(MdRelationshipInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdRelationship.apply();

    // Test the sub relationship
    MdTreeDAO mdRelationship2 = MdTreeDAO.getMdTreeDAO(RELATIONSHIP2).getBusinessDAO();
    assertEquals(mdRelationship.getId(), mdRelationship2.getValue(MdRelationshipInfo.SUPER_MD_RELATIONSHIP));
    assertEquals("2", mdRelationship2.getValue(MdRelationshipInfo.CHILD_CARDINALITY));
  }

  /**
   * Test setting of attributes of and on the relationship datatype
   */
  public void testGraphSet()
  {
    SAXImporter.runImport(new File(GRAPH_SET));

    MdGraphDAO mdRelationship = MdGraphDAO.getMdGraphDAO(RELATIONSHIP).getBusinessDAO();
    MdBusinessDAOIF mdBusiness1 = MdBusinessDAO.getMdBusinessDAO(CLASS);
    MdBusinessDAOIF mdBusiness2 = MdBusinessDAO.getMdBusinessDAO(CLASS2);
    MdAttributeConcreteDAOIF attribute = mdRelationship.definesAttribute("testBoolean");

    assertEquals("Relationship Set Test", mdRelationship.getStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals("Relationship Test", mdRelationship.getStructValue(MdRelationshipInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals(MdAttributeBooleanInfo.FALSE, mdRelationship.getValue(MdRelationshipInfo.REMOVE));
    assertEquals(MdAttributeBooleanInfo.TRUE, mdRelationship.getValue(MdRelationshipInfo.EXTENDABLE));
    assertEquals(MdAttributeBooleanInfo.TRUE, mdRelationship.getValue(MdRelationshipInfo.ABSTRACT));

    // Ensure the parent attributes are correctly set
    assertEquals(mdRelationship.getValue(MdRelationshipInfo.PARENT_MD_BUSINESS), mdBusiness1.getId());
    assertEquals(mdRelationship.getValue(MdRelationshipInfo.PARENT_CARDINALITY), "1");
    assertEquals(mdRelationship.getStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Parent Set Test");

    // Ensure the child attributes are correctly set
    assertEquals(mdRelationship.getValue(MdRelationshipInfo.CHILD_MD_BUSINESS), mdBusiness2.getId());
    assertEquals(mdRelationship.getValue(MdRelationshipInfo.CHILD_CARDINALITY), "*");
    assertEquals(mdRelationship.getStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Child Set Test");

    // Ensure the attributes are linked to the relationship
    assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdRelationship.getId());

    mdRelationship.setValue(MdRelationshipInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdRelationship.apply();

    // Test the sub relationship
    MdGraphDAO mdRelationship2 = MdGraphDAO.getMdGraphDAO(RELATIONSHIP2).getBusinessDAO();
    assertEquals(mdRelationship.getId(), mdRelationship2.getValue(MdRelationshipInfo.SUPER_MD_RELATIONSHIP));
    assertEquals("2", mdRelationship2.getValue(MdRelationshipInfo.CHILD_CARDINALITY));
  }

  /**
   * Test creating Instances that reference other instances
   */
  public void testCreateObject()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();

    mdBusiness1.apply();
    mdBusiness2.apply();

    TestFixtureFactory.addCharacterAttribute(mdBusiness1).apply();
    TestFixtureFactory.addBooleanAttribute(mdBusiness1).apply();
    TestFixtureFactory.addReferenceAttribute(mdBusiness2, mdBusiness1).apply();

    BusinessDAO businessDAO1 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO1.setValue("testBoolean", MdAttributeBooleanInfo.TRUE);
    businessDAO1.setValue("testCharacter", "3");
    businessDAO1.apply();

    BusinessDAO businessDAO2 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO2.setValue("testBoolean", MdAttributeBooleanInfo.FALSE);
    businessDAO2.setValue("testCharacter", "Who are you?");
    businessDAO2.apply();

    BusinessDAO businessDAO3 = BusinessDAO.newInstance(mdBusiness2.definesType());
    businessDAO3.setValue("testReference", businessDAO1.getId());
    businessDAO3.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { businessDAO3, businessDAO1, businessDAO2, mdBusiness1, mdBusiness2 }));

    TestFixtureFactory.delete(mdBusiness2);
    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    // Get the ids of the CLASS1
    List<String> classIds = EntityDAO.getEntityIdsDB(CLASS);

    // Get the first instance of CLASS2
    List<String> class2Ids = EntityDAO.getEntityIdsDB(CLASS2);

    BusinessDAOIF class2 = BusinessDAO.get(class2Ids.get(0));

    // Assert that the values of refTest refer to instances of CLASS
    assertTrue(classIds.contains(class2.getValue("testReference")));
  }

  public void testCreateMdEnumeration()
  {
    SAXImporter.runImport(new File(FILTER_SET));

    MdEnumerationDAO mdEnumeration = MdEnumerationDAO.getMdEnumerationDAO(FILTER).getBusinessDAO();
    MdEnumerationDAO mdEnumeration2 = MdEnumerationDAO.getMdEnumerationDAO(FILTER2).getBusinessDAO();

    List<BusinessDAOIF> items = mdEnumeration.getAllEnumItems();

    assertEquals(mdEnumeration.getStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Enumeration Filter Test");
    assertEquals(mdEnumeration.getStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Filter Set Test");
    assertEquals(mdEnumeration.getValue(MdEnumerationInfo.REMOVE), MdAttributeBooleanInfo.FALSE);
    assertEquals(mdEnumeration.getValue(MdEnumerationInfo.INCLUDE_ALL), MdAttributeBooleanInfo.FALSE);

    // Check that the instance of the filter are correctly mapped
    assertEquals(items.size(), 2);

    assertTrue(items.get(0).getValue("testChar").equals("CO") || items.get(0).getValue("testChar").equals("CA"));
    assertTrue(items.get(1).getValue("testChar").equals("CO") || items.get(1).getValue("testChar").equals("CA"));

    assertEquals(mdEnumeration2.getValue(MdEnumerationInfo.INCLUDE_ALL), MdAttributeBooleanInfo.TRUE);

    mdEnumeration.setValue(MdEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.apply();
  }

  public void testCreateRelationship()
  {
    // Create the Metadata entities
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness1.apply();
    mdBusiness2.apply();

    MdRelationshipDAO mdRelationship1 = TestFixtureFactory.createMdRelationship1(mdBusiness1, mdBusiness2);
    mdRelationship1.apply();

    TestFixtureFactory.addBooleanAttribute(mdRelationship1).apply();

    // Create BusinessDAO for the RelationshipDAO
    BusinessDAO businessDAO1 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO1.apply();

    BusinessDAO businessDAO2 = BusinessDAO.newInstance(mdBusiness2.definesType());
    businessDAO2.apply();

    // Create Test RelationshipDAO
    RelationshipDAO relationshipDAO1 = RelationshipDAO.newInstance(businessDAO1.getId(), businessDAO2.getId(), mdRelationship1.definesType());
    relationshipDAO1.setValue("testBoolean", MdAttributeBooleanInfo.TRUE);
    relationshipDAO1.apply();

    // Export the test entities
    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1, mdBusiness2, mdRelationship1, businessDAO1, businessDAO2, relationshipDAO1 }));

    // Delete the entities
    TestFixtureFactory.delete(mdRelationship1);
    TestFixtureFactory.delete(mdBusiness1);
    TestFixtureFactory.delete(mdBusiness2);

    SAXImporter.runImport(new File(tempXMLFile));

    // Get the instance of CLASS, CLASS2, and RELATIONSHIP
    List<String> c1Ids = EntityDAO.getEntityIdsDB(CLASS);
    List<String> c2Ids = EntityDAO.getEntityIdsDB(CLASS2);
    List<String> r1Ids = RelationshipDAO.getEntityIdsDB(RELATIONSHIP);

    BusinessDAOIF c1 = BusinessDAO.get(c1Ids.get(0));
    BusinessDAOIF c2 = BusinessDAO.get(c2Ids.get(0));
    RelationshipDAOIF r1 = RelationshipDAO.get(r1Ids.get(0));

    // Ensure that the parent references the instance of CLASS
    assertEquals(c1.getId(), r1.getParentId());
    // Ensure that the child reference the instance of CLASS2
    assertEquals(c2.getId(), r1.getChildId());
    // Ensure that the value of testBoolean is true
    assertEquals(MdAttributeBooleanInfo.TRUE, r1.getValue("testBoolean"));
  }

  public void testIndex()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdBoolean = TestFixtureFactory.addBooleanAttribute(mdBusiness1);
    mdBoolean.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdBoolean.apply();

    MdAttributeConcreteDAO mdChar = TestFixtureFactory.addCharacterAttribute(mdBusiness1);
    mdChar.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdChar.apply();

    MdIndexDAO mdIndex = MdIndexDAO.newInstance();
    mdIndex.setStructValue(MdIndexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "index");
    mdIndex.setValue(MdIndexInfo.MD_ENTITY, mdBusiness1.getId());
    mdIndex.setValue(MdIndexInfo.UNIQUE, MdAttributeBooleanInfo.TRUE);
    mdIndex.apply();

    mdIndex.addAttribute(mdBoolean, 0);
    mdIndex.addAttribute(mdChar, 1);

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdIndex, mdBusiness1 }));

    TestFixtureFactory.delete(mdIndex);
    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO(CLASS);

    List<MdIndexDAOIF> indexs = mdBusiness.getIndexes();
    assertEquals(1, indexs.size());

    MdIndexDAOIF mdIndexIF = indexs.get(0);
    assertEquals(MdAttributeBooleanInfo.TRUE, mdIndexIF.getValue(MdIndexInfo.UNIQUE));
    assertEquals("index", mdIndexIF.getStructValue(MdIndexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));

    List<MdAttributeConcreteDAOIF> mdAttributes = mdIndexIF.getIndexedAttributes();

    assertEquals(2, mdAttributes.size());
    assertTrue("testBoolean".equals(mdAttributes.get(0).definesAttribute()) || "testBoolean".equals(mdAttributes.get(1).definesAttribute()));
    assertTrue("testCharacter".equals(mdAttributes.get(0).definesAttribute()) || "testCharacter".equals(mdAttributes.get(1).definesAttribute()));
  }

  public void testFacade()
  {
    SAXImporter.runImport(new File(FACADE_SET_TEST));

    MdFacadeDAOIF mdFacade = MdFacadeDAO.getMdFacadeDAO(FACADE_CLASS);
    assertEquals("Facade1", mdFacade.getStructValue(MdFacadeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals("A test Facade", mdFacade.getStructValue(MdFacadeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals(MdAttributeBooleanInfo.TRUE, mdFacade.getValue(MdFacadeInfo.REMOVE));

    List<MdMethodDAOIF> mdMethods = mdFacade.getMdMethods();

    assertEquals(2, mdMethods.size());

    for (MdMethodDAOIF mdMethod : mdMethods)
    {
      if (mdMethod.getName().equals("checkin"))
      {
        assertTrue(mdMethod.getReturnType().isVoid());
        List<MdParameterDAOIF> mdParameters = mdMethod.getMdParameterDAOs();
        assertEquals(1, mdParameters.size());
        assertEquals("testInteger", mdParameters.get(0).getParameterName());
        assertEquals("java.lang.Integer", mdParameters.get(0).getParameterType().getType());
        assertEquals("1", mdParameters.get(0).getParameterOrder());
        assertEquals(true, mdMethod.isStatic());
      }
      else
      {
        assertEquals("checkout", mdMethod.getName());
        assertTrue(mdMethod.getReturnType().isArray());
        assertEquals("java.lang.Integer[]", mdMethod.getReturnType().getType());

        List<MdParameterDAOIF> mdParameters = mdMethod.getMdParameterDAOs();
        assertEquals(2, mdParameters.size());

        assertEquals("testInteger", mdParameters.get(0).getParameterName());
        assertEquals("java.lang.Integer", mdParameters.get(0).getParameterType().getType());
        assertEquals("1", mdParameters.get(0).getParameterOrder());

        assertEquals("testCharacter", mdParameters.get(1).getParameterName());
        assertEquals("java.lang.String", mdParameters.get(1).getParameterType().getType());
        assertEquals("4", mdParameters.get(1).getParameterOrder());
      }
    }

    for (MdMethodDAOIF mdMethod : mdMethods)
    {
      TestFixtureFactory.delete(mdMethod);
    }
  }

  public void testMdMethod()
  {
    SAXImporter.runImport(new File(METHOD_SET_TEST));

    MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO(CLASS);

    List<MdMethodDAOIF> mdMethods = mdBusiness.getMdMethods();

    // There should be three methods: Two user defined methods, and the auto
    // generated query method
    assertEquals(2, mdMethods.size());

    for (MdMethodDAOIF mdMethod : mdMethods)
    {
      if (mdMethod.getName().equals("checkin"))
      {
        assertTrue(mdMethod.getReturnType().isVoid());
        List<MdParameterDAOIF> mdParameters = mdMethod.getMdParameterDAOs();
        assertEquals(1, mdParameters.size());
        assertEquals("testClass2", mdParameters.get(0).getParameterName());
        assertEquals("test.xmlclasses.Class2", mdParameters.get(0).getParameterType().getType());
        assertEquals("1", mdParameters.get(0).getParameterOrder());
      }
      else if (mdMethod.getName().equals("checkout"))
      {
        assertEquals("checkout", mdMethod.getName());
        assertTrue(mdMethod.getReturnType().isArray());
        assertEquals("test.xmlclasses.Relationship1[]", mdMethod.getReturnType().getType());

        List<MdParameterDAOIF> mdParameters = mdMethod.getMdParameterDAOs();
        assertEquals(2, mdParameters.size());

        assertEquals("testInteger", mdParameters.get(0).getParameterName());
        assertEquals("java.lang.Integer", mdParameters.get(0).getParameterType().getType());
        assertEquals("1", mdParameters.get(0).getParameterOrder());

        assertEquals("testCharacter", mdParameters.get(1).getParameterName());
        assertEquals("java.lang.String", mdParameters.get(1).getParameterType().getType());
        assertEquals("4", mdParameters.get(1).getParameterOrder());
      }
    }

    for (MdMethodDAOIF mdMethod : mdMethods)
    {
      TestFixtureFactory.delete(mdMethod);
    }
  }

  public void testCreateMdFacade()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    MdFacadeDAO mdFacade = MdFacadeDAO.newInstance();
    mdFacade.setValue(MdFacadeInfo.NAME, "Facade1");
    mdFacade.setValue(MdFacadeInfo.PACKAGE, "test.xmlclasses");
    mdFacade.setStructValue(MdFacadeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Facade1");
    mdFacade.setStructValue(MdFacadeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Facade1");
    mdFacade.setValue(MdFacadeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdFacade.apply();

    MdMethodDAO mdMethod = MdMethodDAO.newInstance();
    mdMethod.setValue(MdMethodInfo.NAME, "checkin");
    mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    mdMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkin");
    mdMethod.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkin");
    mdMethod.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    mdMethod.apply();

    MdParameterDAO mdParameter = MdParameterDAO.newInstance();
    mdParameter.setValue(MdParameterInfo.NAME, "param1");
    mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.String");
    mdParameter.setValue(MdParameterInfo.ORDER, "0");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "param1");
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod.getId());
    mdParameter.setStructValue(MdParameterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "param1");
    mdParameter.apply();

    MdParameterDAO mdParameter2 = MdParameterDAO.newInstance();
    mdParameter2.setValue(MdParameterInfo.NAME, "param2");
    mdParameter2.setValue(MdParameterInfo.TYPE, "java.lang.Integer");
    mdParameter2.setValue(MdParameterInfo.ORDER, "1");
    mdParameter2.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "param2");
    mdParameter2.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod.getId());
    mdParameter2.apply();

    // Test importing and exporting a query return type
    String returnType = mdBusiness.definesType() + EntityQueryAPIGenerator.QUERY_API_SUFFIX;
    MdMethodDAO mdMethod2 = MdMethodDAO.newInstance();
    mdMethod2.setValue(MdMethodInfo.NAME, "checkout");
    mdMethod2.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    mdMethod2.setValue(MdMethodInfo.RETURN_TYPE, returnType);
    mdMethod2.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkout");
    mdMethod2.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkout");
    mdMethod2.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    mdMethod2.apply();

    mdFacade = MdFacadeDAO.get(mdFacade.getId()).getBusinessDAO();

    mdFacade.setValue(MdFacadeInfo.STUB_SOURCE, TestFixtureFactory.getMdFacadeStub());
    mdFacade.apply();

    ExportMetadata metadata = new ExportMetadata(true);
    metadata.addCreate(new ComponentIF[] { mdFacade, mdBusiness });

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    TestFixtureFactory.delete(mdFacade);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    assertTrue(MdFacadeDAO.isDefined("test.xmlclasses.Facade1"));

    MdFacadeDAOIF mdFacadeIF = MdFacadeDAO.getMdFacadeDAO("test.xmlclasses.Facade1");
    assertEquals("Facade1", mdFacadeIF.getDescription(CommonProperties.getDefaultLocale()));
    assertEquals("Facade1", mdFacadeIF.getDisplayLabel(CommonProperties.getDefaultLocale()));
    assertEquals(TestFixtureFactory.getMdFacadeStub(), mdFacadeIF.getValue(MdFacadeInfo.STUB_SOURCE));

    List<MdMethodDAOIF> mdMethods = mdFacadeIF.getMdMethods();

    assertEquals(2, mdMethods.size());

    MdMethodDAOIF mdMethodIF = mdMethods.get(0);
    MdMethodDAOIF mdMethod2IF = mdMethods.get(1);

    if (!mdMethods.get(0).getName().equals("checkin"))
    {
      mdMethodIF = mdMethods.get(1);
      mdMethod2IF = mdMethods.get(0);
    }

    assertEquals(true, mdMethodIF.isStatic());
    assertEquals("checkin", mdMethodIF.getName());
    assertTrue(mdMethodIF.getReturnType().isVoid());
    assertEquals("checkin", mdMethodIF.getDisplayLabel(CommonProperties.getDefaultLocale()));
    assertEquals("checkin", mdMethodIF.getDescription(CommonProperties.getDefaultLocale()));

    List<MdParameterDAOIF> mdParameters = mdMethodIF.getMdParameterDAOs();
    assertEquals(2, mdParameters.size());
    assertEquals("param1", mdParameters.get(0).getParameterName());
    assertEquals("java.lang.String", mdParameters.get(0).getParameterType().getType());
    assertEquals("0", mdParameters.get(0).getParameterOrder());
    assertEquals("param1", mdParameters.get(0).getDisplayLabel(CommonProperties.getDefaultLocale()));
    assertEquals("param1", mdParameters.get(0).getDescription(CommonProperties.getDefaultLocale()));

    assertEquals("param2", mdParameters.get(1).getParameterName());
    assertEquals("java.lang.Integer", mdParameters.get(1).getParameterType().getType());
    assertEquals("1", mdParameters.get(1).getParameterOrder());
    assertEquals("param2", mdParameters.get(1).getDisplayLabel(CommonProperties.getDefaultLocale()));

    assertEquals(true, mdMethod2IF.isStatic());
    assertEquals("checkout", mdMethod2IF.getName());
    assertEquals(returnType, mdMethod2IF.getReturnType().getType());
    assertEquals("checkout", mdMethod2IF.getDisplayLabel(CommonProperties.getDefaultLocale()));
    assertEquals("checkout", mdMethod2IF.getDescription(CommonProperties.getDefaultLocale()));

    new File(tempXMLFile).delete();
  }

  public void testCreateMdController()
  {
    MdControllerDAO mdController = MdControllerDAO.newInstance();
    mdController.setValue(MdControllerInfo.NAME, "Controller1");
    mdController.setValue(MdControllerInfo.PACKAGE, "test.xmlclasses");
    mdController.setStructValue(MdControllerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Controller1");
    mdController.setStructValue(MdControllerInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Controller1");
    mdController.setValue(MdControllerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdController.apply();

    MdActionDAO mdAction = MdActionDAO.newInstance();
    mdAction.setValue(MdActionInfo.NAME, "testAction");
    mdAction.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "testAction");
    mdAction.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "testAction");
    mdAction.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
    mdAction.apply();

    MdParameterDAO mdParameter = MdParameterDAO.newInstance();
    mdParameter.setValue(MdParameterInfo.NAME, "param1");
    mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.String");
    mdParameter.setValue(MdParameterInfo.ORDER, "0");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "param1");
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction.getId());
    mdParameter.setStructValue(MdParameterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "param1");
    mdParameter.apply();

    MdParameterDAO mdParameter2 = MdParameterDAO.newInstance();
    mdParameter2.setValue(MdParameterInfo.NAME, "param2");
    mdParameter2.setValue(MdParameterInfo.TYPE, "java.lang.Integer");
    mdParameter2.setValue(MdParameterInfo.ORDER, "1");
    mdParameter2.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "param2");
    mdParameter2.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction.getId());
    mdParameter2.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdController }));

    TestFixtureFactory.delete(mdController);

    SAXImporter.runImport(new File(tempXMLFile));

    assertTrue(MdControllerDAO.isDefined("test.xmlclasses.Controller1"));

    MdControllerDAOIF mdControllerIF = MdControllerDAO.getMdControllerDAO("test.xmlclasses.Controller1");
    assertEquals("Controller1", mdControllerIF.getDescription(CommonProperties.getDefaultLocale()));
    assertEquals("Controller1", mdControllerIF.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<MdActionDAOIF> mdActions = mdControllerIF.getMdActionDAOs();

    assertEquals(1, mdActions.size());
    assertEquals("testAction", mdActions.get(0).getName());
    assertEquals("testAction", mdActions.get(0).getDisplayLabel(CommonProperties.getDefaultLocale()));
    assertEquals("testAction", mdActions.get(0).getDescription(CommonProperties.getDefaultLocale()));

    List<MdParameterDAOIF> mdParameters = mdActions.get(0).getMdParameterDAOs();
    assertEquals(2, mdParameters.size());
    assertEquals("param1", mdParameters.get(0).getParameterName());
    assertEquals("java.lang.String", mdParameters.get(0).getParameterType().getType());
    assertEquals("0", mdParameters.get(0).getParameterOrder());
    assertEquals("param1", mdParameters.get(0).getDisplayLabel(CommonProperties.getDefaultLocale()));
    assertEquals("param1", mdParameters.get(0).getDescription(CommonProperties.getDefaultLocale()));

    assertEquals("param2", mdParameters.get(1).getParameterName());
    assertEquals("java.lang.Integer", mdParameters.get(1).getParameterType().getType());
    assertEquals("1", mdParameters.get(1).getParameterOrder());
    assertEquals("param2", mdParameters.get(1).getDisplayLabel(CommonProperties.getDefaultLocale()));

    new File(tempXMLFile).delete();
  }

  public void testMdStateMachineExport()
  {
    String stateMachineName = "Blog";
    String stateMachinePackage = "test.state";
    String stateMachineLabel = "Star State";
    String stateMachineDescription = "Star State desc";

    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    // Create a new MdState
    MdStateMachineDAO mdStateMachine = MdStateMachineDAO.newInstance();
    mdStateMachine.setValue(MdStateMachineInfo.NAME, stateMachineName);
    mdStateMachine.setValue(MdStateMachineInfo.PACKAGE, stateMachinePackage);
    mdStateMachine.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, stateMachineLabel);
    mdStateMachine.setStructValue(MdStateMachineInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, stateMachineDescription);
    mdStateMachine.setValue(MdStateMachineInfo.SUPER_MD_BUSINESS, EntityTypes.STATE_MASTER.getId());
    mdStateMachine.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness.getId());
    mdStateMachine.apply();

    // Add states
    String stateName = "Writing";
    StateMasterDAO state = mdStateMachine.addState(stateName, StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
    state.apply();

    String state1Name = "Editing";
    StateMasterDAO state1 = mdStateMachine.addState(state1Name, StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId());
    state1.apply();

    String state2Name = "Reading";
    StateMasterDAO state2 = mdStateMachine.addState(state2Name, StateMasterDAOIF.Entry.ENTRY_STATE.getId());
    state2.apply();

    String transitionName = "Written";
    String transition1Name = "Edited";

    mdStateMachine.addTransition(transitionName, state.getId(), state1.getId()).apply();
    mdStateMachine.addTransition(transition1Name, state1.getId(), state2.getId()).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness }));

    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    assertTrue(MdBusinessDAO.isDefined("test.xmlclasses.Class1"));

    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO("test.xmlclasses.Class1");

    MdStateMachineDAOIF mdStateMachineIF = mdBusinessIF.definesMdStateMachine();

    assertNotNull(mdStateMachine);
    assertEquals(stateMachinePackage + "." + stateMachineName, mdStateMachineIF.definesType());
    assertEquals(stateMachineLabel, mdStateMachineIF.getDisplayLabel(CommonProperties.getDefaultLocale()));
    assertEquals(stateMachineDescription, mdStateMachineIF.getDescription(CommonProperties.getDefaultLocale()));

    assertEquals(3, mdStateMachine.definesStateMasters().size());

    // Ensure that the states were imported correctly
    StateMasterDAOIF stateIF = mdStateMachine.definesStateMaster(stateName);
    assertEquals(true, stateIF.isDefaultState());
    assertEquals(true, stateIF.isEntryState());

    StateMasterDAOIF state1IF = mdStateMachine.definesStateMaster(state1Name);
    assertEquals(false, state1IF.isDefaultState());
    assertEquals(false, state1IF.isEntryState());

    StateMasterDAOIF state2IF = mdStateMachine.definesStateMaster(state2Name);
    assertEquals(false, state2IF.isDefaultState());
    assertEquals(true, state2IF.isEntryState());

    // Ensure that the transitions were imported correctly
    assertEquals(2, mdStateMachine.definesTransitions().size());

    TransitionDAOIF checkOut = mdStateMachine.definesTransition(transitionName);
    assertEquals(stateIF.getId(), checkOut.getParentId());
    assertEquals(state1IF.getId(), checkOut.getChildId());

    RelationshipDAOIF checkIn = mdStateMachine.definesTransition(transition1Name);
    assertEquals(state1IF.getId(), checkIn.getParentId());
    assertEquals(state2IF.getId(), checkIn.getChildId());

    new File(tempXMLFile).delete();
  }

  /**
   * Test for a thrown error on circular dependencies in the xml document
   */
  public void testCircularDependency()
  {
    try
    {
      SAXImporter.runImport(new File(CIRCULAR_TEST));

      fail("Failed circular dependency check between two classes");
    }
    catch (XMLParseException e)
    {
      // This is expected
    }
  }

  /**
   * Test for a thrown error on duplicate attribute names in the same class in
   * the xml
   */
  public void testDuplicateAttribute()
  {
    try
    {
      SAXImporter.runImport(new File(DUPLICATE_TEST));

      fail("Failed duplicate attribute name in the same class check");
    }
    catch (DuplicateAttributeDefinitionException e)
    {
      // Expected to land here or next exception
    }
    catch (DuplicateDataException e)
    {
      // This is expected
    }
  }

  /**
   * Test for a thrown error on circular dependencies in the xml document
   */
  public void testInvalidSchema()
  {
    try
    {
      SAXImporter.runImport(new File(INVALID_SCHEMA_TEST));

      fail("Failed invalid schema check");
    }
    catch (XMLParseException e)
    {
      // This is expected
    }
  }

  /**
   * Test for a thrown error on circular dependencies in the xml document
   */
  public void testInvalidPuesdoId()
  {
    try
    {
      SAXImporter.runImport(new File(INVALID_ID));

      fail("Failed invalid puesdo id check");
    }
    catch (XMLException e)
    {
      // This is expected
    }
  }

  public void testSearchingOfDefinitionInAnUpdateTag()
  {
    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    // Export partial schema element with definition of new attribute
    MdAttributeConcreteDAO mdAttributeChar = TestFixtureFactory.addCharacterAttribute(mdBusiness1);
    mdAttributeChar.apply();

    MdViewDAO mdView = TestFixtureFactory.createMdView1();
    mdView.apply();

    MdAttributeVirtualDAO mdAttributeVirtual = TestFixtureFactory.addVirtualAttribute(mdView, mdAttributeChar);
    mdAttributeVirtual.apply();

    ExportMetadata metadata = new ExportMetadata();
    metadata.addCreate(mdView);
    metadata.addUpdate(mdBusiness1);
    metadata.addNewMdAttribute(mdBusiness1, mdAttributeChar);
    metadata.addNewMdAttribute(mdView, mdAttributeVirtual);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    TestFixtureFactory.delete(mdView);
    TestFixtureFactory.delete(mdAttributeChar);

    SAXImporter.runImport(new File(tempXMLFile));

    MdViewDAOIF mdViewIF = MdViewDAO.getMdViewDAO(mdView.definesType());
    MdAttributeDAOIF mdAttributeVirtualIF = mdViewIF.definesAttribute(mdAttributeVirtual.definesAttribute());

    assertNotNull(mdAttributeVirtualIF);
  }

  public void testUserPermission()
  {
    try
    {
      // Create a test User
      UserDAO user = TestFixtureFactory.createUser();
      user.apply();

      RoleDAO role = TestFixtureFactory.createRole1();
      role.apply();

      role.assignMember(user);

      // Create test MdBusiness
      MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
      mdBusiness1.apply();

      MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
      mdBusiness2.apply();

      MdAttributeConcreteDAO mdAttributeChar = TestFixtureFactory.addCharacterAttribute(mdBusiness1);
      mdAttributeChar.apply();

      // Create a new MdState
      MdStateMachineDAO mdStateMachine = MdStateMachineDAO.newInstance();
      mdStateMachine.setValue(MdStateMachineInfo.NAME, "Blog");
      mdStateMachine.setValue(MdStateMachineInfo.PACKAGE, "test.state");
      mdStateMachine.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Star State");
      mdStateMachine.setStructValue(MdStateMachineInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Star State desc");
      mdStateMachine.setValue(MdStateMachineInfo.SUPER_MD_BUSINESS, EntityTypes.STATE_MASTER.getId());
      mdStateMachine.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness1.getId());
      mdStateMachine.apply();

      StateMasterDAO state11 = mdStateMachine.addState("Writing", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
      state11.apply();

      StateMasterDAO state12 = mdStateMachine.addState("Editing", StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId());
      state12.apply();

      StateMasterDAO state13 = mdStateMachine.addState("Reading", StateMasterDAOIF.Entry.ENTRY_STATE.getId());
      state13.apply();

      mdStateMachine.addTransition("Written", state11.getId(), state12.getId()).apply();
      mdStateMachine.addTransition("Edited", state12.getId(), state13.getId()).apply();

      // Add a StateMachine to mdBusiness2
      MdStateMachineDAO mdStateMachine2 = MdStateMachineDAO.newInstance();
      mdStateMachine2.setValue(MdStateMachineInfo.NAME, "StateMachine1");
      mdStateMachine2.setValue(MdStateMachineInfo.PACKAGE, "test.xmlclasses");
      mdStateMachine2.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "StateMachine1");
      mdStateMachine2.setStructValue(MdStateMachineInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "State Machine of Class1");
      mdStateMachine2.setValue(MdStateMachineInfo.SUPER_MD_BUSINESS, EntityTypes.STATE_MASTER.getId());
      mdStateMachine2.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness2.getId());
      mdStateMachine2.apply();

      // Add states
      StateMasterDAO state21 = mdStateMachine2.addState("Available", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
      state21.apply();

      StateMasterDAO state22 = mdStateMachine2.addState("CheckedOut", StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId());
      state22.apply();

      StateMasterDAO state23 = mdStateMachine2.addState("CheckedIn", StateMasterDAOIF.Entry.ENTRY_STATE.getId());
      state23.apply();

      mdStateMachine2.addTransition("CheckOut", state21.getId(), state22.getId()).apply();
      mdStateMachine2.addTransition("CheckIn", state22.getId(), state23.getId()).apply();
      mdStateMachine2.addTransition("Stock", state23.getId(), state21.getId()).apply();

      MdRelationshipDAO mdRelationship = TestFixtureFactory.createMdRelationship1(mdBusiness1, mdBusiness2);
      mdRelationship.apply();

      MdAttributeConcreteDAO mdAttributeBool = TestFixtureFactory.addBooleanAttribute(mdRelationship);
      mdAttributeBool.apply();

      // Add permissions to the MdBusiness
      user.grantPermission(Operation.CREATE, mdBusiness1.getId());
      user.grantPermission(Operation.WRITE, mdBusiness1.getId());

      // Add attribute permissions
      user.grantPermission(Operation.WRITE, mdAttributeChar.getId());
      user.grantPermission(Operation.READ, mdAttributeChar.getId());

      // Add permissions a State
      user.grantPermission(Operation.DELETE, state11.getId());
      user.grantPermission(Operation.READ, state11.getId());

      // Add struct permissions
      MdStructDAO mdStruct = TestFixtureFactory.createMdStruct1();
      mdStruct.apply();

      user.grantPermission(Operation.DELETE, mdStruct.getId());

      // Add permissions to a State-Attribute pairing
      TypeTupleDAO tuple = TypeTupleDAO.newInstance();
      tuple.setStateMaster(state12.getId());
      tuple.setMetaData(mdAttributeChar.getId());
      tuple.setStructValue(TypeTupleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "tuple");
      tuple.apply();

      user.grantPermission(Operation.WRITE, tuple.getId());

      // Add permissions to a MdRelationship
      user.grantPermission(Operation.CREATE, mdRelationship.getId());
      user.grantPermission(Operation.DELETE, mdRelationship.getId());

      // Add permissions to an attribute defined by the MdRelationship
      user.grantPermission(Operation.READ, mdAttributeBool.getId());

      // Add directional permissions to the parent state of the MdRelationship
      TypeTupleDAO tuple2 = TypeTupleDAO.newInstance();
      tuple2.setStateMaster(state11.getId());
      tuple2.setMetaData(mdRelationship.getId());
      tuple2.setStructValue(TypeTupleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "tuple");
      tuple2.apply();

      user.grantPermission(Operation.ADD_CHILD, tuple2.getId());
      user.grantPermission(Operation.READ_CHILD, tuple2.getId());

      TypeTupleDAO tuple3 = TypeTupleDAO.newInstance();
      tuple3.setStateMaster(state12.getId());
      tuple3.setMetaData(mdRelationship.getId());
      tuple3.setStructValue(TypeTupleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "tuple");
      tuple3.apply();

      user.grantPermission(Operation.WRITE_CHILD, tuple3.getId());

      TypeTupleDAO tuple4 = TypeTupleDAO.newInstance();
      tuple4.setStateMaster(state21.getId());
      tuple4.setMetaData(mdRelationship.getId());
      tuple4.setStructValue(TypeTupleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "tuple");
      tuple4.apply();

      user.grantPermission(Operation.ADD_PARENT, tuple4.getId());

      // Export the permissions
      ExportMetadata metadata = new ExportMetadata();
      metadata.addCreate(user);
      metadata.addGrantPermissions(user);

      SAXExporter.export(tempXMLFile, SCHEMA, metadata);

      TestFixtureFactory.delete(tuple);
      TestFixtureFactory.delete(tuple2);
      TestFixtureFactory.delete(tuple3);
      TestFixtureFactory.delete(tuple4);
      TestFixtureFactory.delete(user);

      SAXImporter.runImport(new File(tempXMLFile));

      UserDAOIF userIF = UserDAO.findUser("testUser");
      TypeTupleDAOIF tupleIF = TypeTupleDAO.findTuple(mdAttributeChar.getId(), state12.getId());
      TypeTupleDAOIF tuple2IF = TypeTupleDAO.findTuple(mdRelationship.getId(), state11.getId());
      TypeTupleDAOIF tuple3IF = TypeTupleDAO.findTuple(mdRelationship.getId(), state12.getId());
      TypeTupleDAOIF tuple4IF = TypeTupleDAO.findTuple(mdRelationship.getId(), state21.getId());

      Set<RoleDAOIF> assignedRoles = userIF.assignedRoles();
      assertEquals(1, assignedRoles.size());
      assertTrue(assignedRoles.contains(role));

      Set<Operation> operations = userIF.getAllPermissions(mdBusiness1);
      assertEquals(2, operations.size());
      assertTrue(operations.contains(Operation.WRITE));
      assertTrue(operations.contains(Operation.CREATE));

      operations = userIF.getAllPermissions(mdAttributeChar);
      assertEquals(2, operations.size());
      assertTrue(operations.contains(Operation.WRITE));
      assertTrue(operations.contains(Operation.READ));

      operations = userIF.getAllPermissions(state11);
      assertEquals(2, operations.size());
      assertTrue(operations.contains(Operation.DELETE));
      assertTrue(operations.contains(Operation.READ));

      operations = userIF.getAllPermissions(tupleIF);
      assertEquals(1, operations.size());
      assertTrue(operations.contains(Operation.WRITE));

      operations = userIF.getAllPermissions(mdRelationship);
      assertEquals(2, operations.size());
      assertTrue(operations.contains(Operation.DELETE));
      assertTrue(operations.contains(Operation.CREATE));

      operations = userIF.getAllPermissions(mdAttributeBool);
      assertEquals(1, operations.size());
      assertTrue(operations.contains(Operation.READ));

      operations = userIF.getAllPermissions(tuple2IF);
      assertEquals(2, operations.size());
      assertTrue(operations.contains(Operation.ADD_CHILD));
      assertTrue(operations.contains(Operation.READ_CHILD));

      operations = userIF.getAllPermissions(tuple3IF);
      assertEquals(1, operations.size());
      assertTrue(operations.contains(Operation.WRITE_CHILD));

      operations = userIF.getAllPermissions(tuple4IF);
      assertEquals(1, operations.size());
      assertTrue(operations.contains(Operation.ADD_PARENT));

      operations = userIF.getAllPermissions(mdStruct);
      assertEquals(1, operations.size());
      assertTrue(operations.contains(Operation.DELETE));
    }
    catch (Throwable e)
    {
      throw new RuntimeException(e);
    }
  }

  public void testRevokeUserPermission()
  {
    // Create a test User
    UserDAO user = TestFixtureFactory.createUser();
    user.apply();

    RoleDAO role = TestFixtureFactory.createRole1();
    role.apply();

    role.assignMember(user);

    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.apply();

    MdAttributeConcreteDAO mdAttributeChar = TestFixtureFactory.addCharacterAttribute(mdBusiness1);
    mdAttributeChar.apply();

    // Create a new MdState
    MdStateMachineDAO mdStateMachine = MdStateMachineDAO.newInstance();
    mdStateMachine.setValue(MdStateMachineInfo.NAME, "Blog");
    mdStateMachine.setValue(MdStateMachineInfo.PACKAGE, "test.state");
    mdStateMachine.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Star State");
    mdStateMachine.setStructValue(MdStateMachineInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Star State desc");
    mdStateMachine.setValue(MdStateMachineInfo.SUPER_MD_BUSINESS, EntityTypes.STATE_MASTER.getId());
    mdStateMachine.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness1.getId());
    mdStateMachine.apply();

    StateMasterDAO state11 = mdStateMachine.addState("Writing", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
    state11.apply();

    StateMasterDAO state12 = mdStateMachine.addState("Editing", StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId());
    state12.apply();

    StateMasterDAO state13 = mdStateMachine.addState("Reading", StateMasterDAOIF.Entry.ENTRY_STATE.getId());
    state13.apply();

    mdStateMachine.addTransition("Written", state11.getId(), state12.getId()).apply();
    mdStateMachine.addTransition("Edited", state12.getId(), state13.getId()).apply();

    // Add a StateMachine to mdBusiness2
    MdStateMachineDAO mdStateMachine2 = MdStateMachineDAO.newInstance();
    mdStateMachine2.setValue(MdStateMachineInfo.NAME, "StateMachine1");
    mdStateMachine2.setValue(MdStateMachineInfo.PACKAGE, "test.xmlclasses");
    mdStateMachine2.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "StateMachine1");
    mdStateMachine2.setStructValue(MdStateMachineInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "State Machine of Class1");
    mdStateMachine2.setValue(MdStateMachineInfo.SUPER_MD_BUSINESS, EntityTypes.STATE_MASTER.getId());
    mdStateMachine2.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness2.getId());
    mdStateMachine2.apply();

    // Add states
    StateMasterDAO state21 = mdStateMachine2.addState("Available", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
    state21.apply();

    StateMasterDAO state22 = mdStateMachine2.addState("CheckedOut", StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId());
    state22.apply();

    StateMasterDAO state23 = mdStateMachine2.addState("CheckedIn", StateMasterDAOIF.Entry.ENTRY_STATE.getId());
    state23.apply();

    mdStateMachine2.addTransition("CheckOut", state21.getId(), state22.getId()).apply();
    mdStateMachine2.addTransition("CheckIn", state22.getId(), state23.getId()).apply();
    mdStateMachine2.addTransition("Stock", state23.getId(), state21.getId()).apply();

    MdRelationshipDAO mdRelationship = TestFixtureFactory.createMdRelationship1(mdBusiness1, mdBusiness2);
    mdRelationship.apply();

    MdAttributeConcreteDAO mdAttributeBool = TestFixtureFactory.addBooleanAttribute(mdRelationship);
    mdAttributeBool.apply();

    // Add permissions to the MdBusiness
    user.grantPermission(Operation.CREATE, mdBusiness1.getId());
    user.grantPermission(Operation.WRITE, mdBusiness1.getId());

    // Add attribute permissions
    user.grantPermission(Operation.WRITE, mdAttributeChar.getId());
    user.grantPermission(Operation.READ, mdAttributeChar.getId());

    // Add permissions a State
    user.grantPermission(Operation.DELETE, state11.getId());
    user.grantPermission(Operation.READ, state11.getId());

    // Add struct permissions
    MdStructDAO mdStruct = TestFixtureFactory.createMdStruct1();
    mdStruct.apply();

    user.grantPermission(Operation.DELETE, mdStruct.getId());

    // Add permissions to a State-Attribute pairing
    TypeTupleDAO tuple = TypeTupleDAO.newInstance();
    tuple.setStateMaster(state12.getId());
    tuple.setMetaData(mdAttributeChar.getId());
    tuple.setStructValue(TypeTupleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "tuple");
    tuple.apply();

    user.grantPermission(Operation.WRITE, tuple.getId());

    // Add permissions to a MdRelationship
    user.grantPermission(Operation.CREATE, mdRelationship.getId());
    user.grantPermission(Operation.DELETE, mdRelationship.getId());

    // Add permissions to an attribute defined by the MdRelationship
    user.grantPermission(Operation.READ, mdAttributeBool.getId());

    // Add directional permissions to the parent state of the MdRelationship
    TypeTupleDAO tuple2 = TypeTupleDAO.newInstance();
    tuple2.setStateMaster(state11.getId());
    tuple2.setMetaData(mdRelationship.getId());
    tuple2.setStructValue(TypeTupleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "tuple");
    tuple2.apply();

    user.grantPermission(Operation.ADD_CHILD, tuple2.getId());
    user.grantPermission(Operation.READ_CHILD, tuple2.getId());

    TypeTupleDAO tuple3 = TypeTupleDAO.newInstance();
    tuple3.setStateMaster(state12.getId());
    tuple3.setMetaData(mdRelationship.getId());
    tuple3.setStructValue(TypeTupleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "tuple");
    tuple3.apply();

    user.grantPermission(Operation.WRITE_CHILD, tuple3.getId());

    TypeTupleDAO tuple4 = TypeTupleDAO.newInstance();
    tuple4.setStateMaster(state21.getId());
    tuple4.setMetaData(mdRelationship.getId());
    tuple4.setStructValue(TypeTupleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "tuple");
    tuple4.apply();

    user.grantPermission(Operation.ADD_PARENT, tuple4.getId());

    // Export the permissions
    ExportMetadata metadata = new ExportMetadata();
    metadata.addRevokePermissions(user);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    SAXImporter.runImport(new File(tempXMLFile));

    UserDAOIF userIF = UserDAO.findUser("testUser");
    TypeTupleDAOIF tupleIF = TypeTupleDAO.findTuple(mdAttributeChar.getId(), state12.getId());
    TypeTupleDAOIF tuple2IF = TypeTupleDAO.findTuple(mdRelationship.getId(), state11.getId());
    TypeTupleDAOIF tuple3IF = TypeTupleDAO.findTuple(mdRelationship.getId(), state12.getId());
    TypeTupleDAOIF tuple4IF = TypeTupleDAO.findTuple(mdRelationship.getId(), state21.getId());

    Set<RoleDAOIF> assignedRoles = userIF.assignedRoles();
    assertEquals(1, assignedRoles.size());
    assertTrue(assignedRoles.contains(role));

    Set<Operation> operations = userIF.getAllPermissions(mdBusiness1);
    assertEquals(0, operations.size());

    operations = userIF.getAllPermissions(mdAttributeChar);
    assertEquals(0, operations.size());

    operations = userIF.getAllPermissions(state11);
    assertEquals(0, operations.size());

    operations = userIF.getAllPermissions(tupleIF);
    assertEquals(0, operations.size());

    operations = userIF.getAllPermissions(mdRelationship);
    assertEquals(0, operations.size());

    operations = userIF.getAllPermissions(mdAttributeBool);
    assertEquals(0, operations.size());

    operations = userIF.getAllPermissions(tuple2IF);
    assertEquals(0, operations.size());

    operations = userIF.getAllPermissions(tuple3IF);
    assertEquals(0, operations.size());

    operations = userIF.getAllPermissions(tuple4IF);
    assertEquals(0, operations.size());

    operations = userIF.getAllPermissions(mdStruct);
    assertEquals(0, operations.size());
  }

  public void testRolePermissions()
  {
    RoleDAO role1 = TestFixtureFactory.createRole1();
    role1.apply();

    RoleDAO role2 = TestFixtureFactory.createRole2();
    role2.apply();

    role1.addAscendant(role2);

    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    // Create test MdView
    MdViewDAO mdView1 = TestFixtureFactory.createMdView1();
    mdView1.apply();

    // Create test MdUtil
    MdUtilDAO mdUtil1 = TestFixtureFactory.createMdUtil1();
    mdUtil1.apply();

    // Create test MdStruct
    MdStructDAO mdStruct = TestFixtureFactory.createMdStruct1();
    mdStruct.apply();

    // Add permissions to the MdBusiness
    role1.grantPermission(Operation.CREATE, mdBusiness1.getId());
    role1.grantPermission(Operation.WRITE, mdBusiness1.getId());
    role1.grantPermission(Operation.READ, mdView1.getId());
    role1.grantPermission(Operation.DELETE, mdView1.getId());
    role1.grantPermission(Operation.WRITE, mdView1.getId());
    role1.grantPermission(Operation.CREATE, mdUtil1.getId());
    role1.grantPermission(Operation.DELETE, mdStruct.getId());
    role1.grantPermission(Operation.WRITE, mdStruct.getId());
    role1.grantPermission(Operation.CREATE, mdStruct.getId());

    // Export the permissions
    ExportMetadata metadata = new ExportMetadata();
    metadata.addCreate(role1);
    metadata.addGrantPermissions(role1);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    TestFixtureFactory.delete(role1);

    SAXImporter.runImport(new File(tempXMLFile));

    RoleDAOIF roleIF = RoleDAO.findRole("runway.testRole");

    roleIF = RoleDAO.findRole("runway.testRole");
    assertEquals("Test Role", roleIF.getStructValue(RoleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));

    Set<Operation> operations = roleIF.getAllPermissions(mdBusiness1);
    assertEquals(2, operations.size());
    assertTrue(operations.contains(Operation.CREATE));
    assertTrue(operations.contains(Operation.WRITE));

    operations = roleIF.getAllPermissions(mdView1);
    assertEquals(3, operations.size());
    assertTrue(operations.contains(Operation.READ));
    assertTrue(operations.contains(Operation.DELETE));
    assertTrue(operations.contains(Operation.WRITE));

    operations = roleIF.getAllPermissions(mdUtil1);
    assertEquals(1, operations.size());
    assertTrue(operations.contains(Operation.CREATE));

    operations = roleIF.getAllPermissions(mdStruct);
    assertEquals(3, operations.size());
    assertTrue(operations.contains(Operation.CREATE));
    assertTrue(operations.contains(Operation.DELETE));
    assertTrue(operations.contains(Operation.WRITE));

    Set<RoleDAOIF> superRoles = roleIF.getSuperRoles();
    assertEquals(1, superRoles.size());
    assertTrue(superRoles.contains(role2));
  }

  public void testRevokeRolePermissions()
  {
    RoleDAO role1 = TestFixtureFactory.createRole1();
    role1.apply();

    RoleDAO role2 = TestFixtureFactory.createRole2();
    role2.apply();

    role1.addAscendant(role2);

    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    // Create test MdView
    MdViewDAO mdView1 = TestFixtureFactory.createMdView1();
    mdView1.apply();

    // Create test MdUtil
    MdUtilDAO mdUtil1 = TestFixtureFactory.createMdUtil1();
    mdUtil1.apply();

    // Create test MdStruct
    MdStructDAO mdStruct = TestFixtureFactory.createMdStruct1();
    mdStruct.apply();

    // Add permissions to the MdBusiness
    role1.grantPermission(Operation.CREATE, mdBusiness1.getId());
    role1.grantPermission(Operation.WRITE, mdBusiness1.getId());
    role1.grantPermission(Operation.READ, mdView1.getId());
    role1.grantPermission(Operation.DELETE, mdView1.getId());
    role1.grantPermission(Operation.WRITE, mdView1.getId());
    role1.grantPermission(Operation.CREATE, mdUtil1.getId());
    role1.grantPermission(Operation.DELETE, mdStruct.getId());
    role1.grantPermission(Operation.WRITE, mdStruct.getId());
    role1.grantPermission(Operation.CREATE, mdStruct.getId());

    // Export the permissions
    ExportMetadata metadata = new ExportMetadata();
    metadata.addRevokePermissions(role1);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    SAXImporter.runImport(new File(tempXMLFile));

    RoleDAOIF roleIF = RoleDAO.findRole("runway.testRole");

    roleIF = RoleDAO.findRole("runway.testRole");
    assertEquals("Test Role", roleIF.getStructValue(RoleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));

    Set<Operation> operations = roleIF.getAllPermissions(mdBusiness1);
    assertEquals(0, operations.size());

    operations = roleIF.getAllPermissions(mdView1);
    assertEquals(0, operations.size());

    operations = roleIF.getAllPermissions(mdUtil1);
    assertEquals(0, operations.size());

    operations = roleIF.getAllPermissions(mdStruct);
    assertEquals(0, operations.size());

    Set<RoleDAOIF> superRoles = roleIF.getSuperRoles();
    assertEquals(1, superRoles.size());
    assertTrue(superRoles.contains(role2));
  }

  public void testMethodPermissions()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    MdMethodDAO mdMethod = TestFixtureFactory.createMdMethod(mdBusiness);
    mdMethod.apply();

    MdFacadeDAO mdFacade = MdFacadeDAO.newInstance();
    mdFacade.setValue(MdFacadeInfo.NAME, "Facade1");
    mdFacade.setValue(MdFacadeInfo.PACKAGE, "test.xmlclasses");
    mdFacade.setStructValue(MdFacadeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Facade1");
    mdFacade.setStructValue(MdFacadeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Facade1");
    mdFacade.setValue(MdFacadeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdFacade.apply();

    MdMethodDAO mdMethod2 = MdMethodDAO.newInstance();
    mdMethod2.setValue(MdMethodInfo.NAME, "checkin");
    mdMethod2.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    mdMethod2.setValue(MdMethodInfo.RETURN_TYPE, "void");
    mdMethod2.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkin");
    mdMethod2.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkin");
    mdMethod2.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    mdMethod2.apply();

    MethodActorDAO methodActor = TestFixtureFactory.createMethodActor(mdMethod);
    methodActor.apply();

    RoleDAO role = TestFixtureFactory.createRole1();
    role.apply();

    role.assignMember(methodActor);

    // Create test MdView
    MdViewDAO mdView1 = TestFixtureFactory.createMdView1();
    mdView1.apply();

    // Create test MdUtil
    MdUtilDAO mdUtil1 = TestFixtureFactory.createMdUtil1();
    mdUtil1.apply();

    // Create test MdStruct
    MdStructDAO mdStruct = TestFixtureFactory.createMdStruct1();
    mdStruct.apply();

    // Add permissions to the MdBusiness
    role.grantPermission(Operation.EXECUTE, mdMethod.getId());
    methodActor.grantPermission(Operation.CREATE, mdBusiness.getId());
    methodActor.grantPermission(Operation.WRITE, mdBusiness.getId());
    methodActor.grantPermission(Operation.READ, mdView1.getId());
    methodActor.grantPermission(Operation.DELETE, mdView1.getId());
    methodActor.grantPermission(Operation.WRITE, mdView1.getId());
    methodActor.grantPermission(Operation.CREATE, mdUtil1.getId());
    methodActor.grantPermission(Operation.DELETE, mdStruct.getId());
    methodActor.grantPermission(Operation.WRITE, mdStruct.getId());
    methodActor.grantPermission(Operation.WRITE, mdStruct.getId());
    methodActor.grantPermission(Operation.EXECUTE, mdMethod2.getId());

    // Export the permissions
    ExportMetadata metadata = new ExportMetadata();
    metadata.addGrantPermissions(role);
    metadata.addGrantPermissions(methodActor);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    // Remove all existing permissions
    TestFixtureFactory.delete(methodActor);
    role.revokeAllPermissions(mdMethod.getId());

    SAXImporter.runImport(new File(tempXMLFile));

    MethodActorDAOIF methodActorIF = mdMethod.getMethodActor();
    RoleDAOIF roleIF = RoleDAO.get(role.getId());

    Set<RoleDAOIF> assignedRoles = methodActorIF.assignedRoles();
    assertEquals(1, assignedRoles.size());
    assertTrue(assignedRoles.contains(role));

    Set<Operation> operations = methodActorIF.getAllPermissions(mdBusiness);
    assertEquals(2, operations.size());
    assertTrue(operations.contains(Operation.CREATE));
    assertTrue(operations.contains(Operation.WRITE));

    operations = roleIF.getAllPermissions(mdMethod);
    assertEquals(1, operations.size());
    assertTrue(operations.contains(Operation.EXECUTE));

    operations = methodActorIF.getAllPermissions(mdView1);
    assertEquals(3, operations.size());
    assertTrue(operations.contains(Operation.READ));
    assertTrue(operations.contains(Operation.DELETE));
    assertTrue(operations.contains(Operation.WRITE));

    operations = methodActorIF.getAllPermissions(mdUtil1);
    assertEquals(1, operations.size());
    assertTrue(operations.contains(Operation.CREATE));

    operations = methodActorIF.getAllPermissions(mdStruct);
    assertEquals(2, operations.size());
    assertTrue(operations.contains(Operation.DELETE));
    assertTrue(operations.contains(Operation.WRITE));

    operations = methodActorIF.getAllPermissions(mdMethod2);
    assertEquals(1, operations.size());
    assertTrue(operations.contains(Operation.EXECUTE));
  }

  public void testRevokeMethodPermissions()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    MdMethodDAO mdMethod = TestFixtureFactory.createMdMethod(mdBusiness);
    mdMethod.apply();

    MdFacadeDAO mdFacade = MdFacadeDAO.newInstance();
    mdFacade.setValue(MdFacadeInfo.NAME, "Facade1");
    mdFacade.setValue(MdFacadeInfo.PACKAGE, "test.xmlclasses");
    mdFacade.setStructValue(MdFacadeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Facade1");
    mdFacade.setStructValue(MdFacadeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Facade1");
    mdFacade.setValue(MdFacadeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdFacade.apply();

    MdMethodDAO mdMethod2 = MdMethodDAO.newInstance();
    mdMethod2.setValue(MdMethodInfo.NAME, "checkin");
    mdMethod2.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    mdMethod2.setValue(MdMethodInfo.RETURN_TYPE, "void");
    mdMethod2.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkin");
    mdMethod2.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkin");
    mdMethod2.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    mdMethod2.apply();

    MethodActorDAO methodActor = TestFixtureFactory.createMethodActor(mdMethod);
    methodActor.apply();

    RoleDAO role = TestFixtureFactory.createRole1();
    role.apply();

    role.assignMember(methodActor);

    // Create test MdView
    MdViewDAO mdView1 = TestFixtureFactory.createMdView1();
    mdView1.apply();

    // Create test MdUtil
    MdUtilDAO mdUtil1 = TestFixtureFactory.createMdUtil1();
    mdUtil1.apply();

    // Create test MdStruct
    MdStructDAO mdStruct = TestFixtureFactory.createMdStruct1();
    mdStruct.apply();

    // Add permissions to the MdBusiness
    role.grantPermission(Operation.EXECUTE, mdMethod.getId());
    methodActor.grantPermission(Operation.CREATE, mdBusiness.getId());
    methodActor.grantPermission(Operation.WRITE, mdBusiness.getId());
    methodActor.grantPermission(Operation.READ, mdView1.getId());
    methodActor.grantPermission(Operation.DELETE, mdView1.getId());
    methodActor.grantPermission(Operation.WRITE, mdView1.getId());
    methodActor.grantPermission(Operation.CREATE, mdUtil1.getId());
    methodActor.grantPermission(Operation.DELETE, mdStruct.getId());
    methodActor.grantPermission(Operation.WRITE, mdStruct.getId());
    methodActor.grantPermission(Operation.WRITE, mdStruct.getId());
    methodActor.grantPermission(Operation.EXECUTE, mdMethod2.getId());

    // Export the permissions
    ExportMetadata metadata = new ExportMetadata();
    metadata.addRevokePermissions(role);
    metadata.addRevokePermissions(methodActor);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    SAXImporter.runImport(new File(tempXMLFile));

    MethodActorDAOIF methodActorIF = mdMethod.getMethodActor();
    RoleDAOIF roleIF = RoleDAO.get(role.getId());

    Set<RoleDAOIF> assignedRoles = methodActorIF.assignedRoles();
    assertEquals(1, assignedRoles.size());
    assertTrue(assignedRoles.contains(role));

    Set<Operation> operations = methodActorIF.getAllPermissions(mdBusiness);
    assertEquals(0, operations.size());

    operations = roleIF.getAllPermissions(mdMethod);
    assertEquals(0, operations.size());

    operations = methodActorIF.getAllPermissions(mdView1);
    assertEquals(0, operations.size());

    operations = methodActorIF.getAllPermissions(mdUtil1);
    assertEquals(0, operations.size());

    operations = methodActorIF.getAllPermissions(mdStruct);
    assertEquals(0, operations.size());

    operations = methodActorIF.getAllPermissions(mdMethod2);
    assertEquals(0, operations.size());
  }

  public void testUpdateMdStateMachine()
  {
    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdBusiness1.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdBusiness1.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdBusiness1.setValue(MdBusinessInfo.CACHE_SIZE, "50");
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addBooleanAttribute(mdBusiness1);
    mdAttribute.apply();

    // Create test MdStateMachine
    MdStateMachineDAO mdStateMachine = MdStateMachineDAO.newInstance();
    mdStateMachine.setValue(MdStateMachineInfo.NAME, "StateMachine1");
    mdStateMachine.setValue(MdStateMachineInfo.PACKAGE, "test.xmlclasses");
    mdStateMachine.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "StateMachine1");
    mdStateMachine.setStructValue(MdStateMachineInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "State Machine of Class1");
    mdStateMachine.setValue(MdStateMachineInfo.SUPER_MD_BUSINESS, EntityTypes.STATE_MASTER.getId());
    mdStateMachine.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness1.getId());
    mdStateMachine.apply();

    // Add states
    StateMasterDAO state1 = mdStateMachine.addState("Available", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
    state1.apply();

    StateMasterDAO state2 = mdStateMachine.addState("CheckedOut", StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId());
    state2.apply();

    mdStateMachine.addTransition("CheckOut", state1.getId(), state2.getId()).apply();

    // Create new states and transitions for the existing MdStateMachine
    StateMasterDAO state3 = mdStateMachine.addState("CheckedIn", StateMasterDAOIF.Entry.ENTRY_STATE.getId());
    TransitionDAO transition2 = mdStateMachine.addTransition("CheckIn", state2.getId(), state3.getId());
    TransitionDAO transition3 = mdStateMachine.addTransition("Stock", state3.getId(), state1.getId());

    // Export the test entities
    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 });
    metadata.addNewStates(mdStateMachine, state3);
    metadata.addNewTransitions(mdStateMachine, transition2, transition3);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    mdBusiness1 = MdBusinessDAO.get(mdBusiness1.getId()).getBusinessDAO();

    // Change the values of mdBusiness, booleanAttribute, and the stateMachine
    mdBusiness1.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.setValue(MdBusinessInfo.CACHE_SIZE, "500");
    mdBusiness1.apply();

    mdAttribute.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Boolean Update Test");
    mdAttribute.apply();

    mdStateMachine.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "StateMachine1 Update");
    mdStateMachine.setStructValue(MdStateMachineInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "State Machine of MdBusiness1");
    mdStateMachine.apply();

    // Import the test entites
    SAXImporter.runImport(new File(tempXMLFile));

    MdBusinessDAOIF mdBusiness1IF = MdBusinessDAO.getMdBusinessDAO(CLASS);

    MdAttributeDAOIF attribute = mdBusiness1IF.definesAttribute("testBoolean");

    assertEquals(mdBusiness1IF.getStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdBusiness Set Test");
    assertEquals(mdBusiness1IF.getStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdBusiness Attributes Test");
    assertEquals(mdBusiness1IF.getValue(MdBusinessInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdBusiness1IF.getValue(MdBusinessInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdBusiness1IF.getValue(MdBusinessInfo.CACHE_SIZE), "50");

    // Change to false when casscading delete is implemented
    assertEquals(mdBusiness1IF.getValue(MetadataInfo.REMOVE), MdAttributeBooleanInfo.TRUE);

    // Ensure the attributes are linked to the correct MdBusiness object
    assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdBusiness1IF.getId());

    // Test that the MdStateMachine on the MdBusiness was correctly set
    MdStateMachineDAOIF mdStateMachineIF = mdBusiness1IF.definesMdStateMachine();

    assertNotNull(mdStateMachineIF);
    assertEquals(3, mdStateMachineIF.definesStateMasters().size());

    // Ensure that the states were imported correctly
    StateMasterDAOIF available = mdStateMachineIF.definesStateMaster("Available");
    assertEquals(true, available.isDefaultState());
    assertEquals(true, available.isEntryState());

    StateMasterDAOIF checkedOut = mdStateMachineIF.definesStateMaster("CheckedOut");
    assertEquals(false, checkedOut.isDefaultState());
    assertEquals(false, checkedOut.isEntryState());

    StateMasterDAOIF checkedIn = mdStateMachineIF.definesStateMaster("CheckedIn");
    assertEquals(false, checkedIn.isDefaultState());
    assertEquals(true, checkedIn.isEntryState());

    // Ensure that the transitions were imported correctly
    assertEquals(3, mdStateMachineIF.definesTransitions().size());

    TransitionDAOIF checkOut = mdStateMachineIF.definesTransition("CheckOut");
    assertEquals(available.getId(), checkOut.getParentId());
    assertEquals(checkedOut.getId(), checkOut.getChildId());

    RelationshipDAOIF checkIn = mdStateMachineIF.definesTransition("CheckIn");
    assertEquals(checkedOut.getId(), checkIn.getParentId());
    assertEquals(checkedIn.getId(), checkIn.getChildId());

    RelationshipDAOIF stock = mdStateMachineIF.definesTransition("Stock");
    assertEquals(checkedIn.getId(), stock.getParentId());
    assertEquals(available.getId(), stock.getChildId());
  }

  public void testUpdateMdBusiness()
  {
    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdBusiness1.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdBusiness1.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdBusiness1.setValue(MdBusinessInfo.CACHE_SIZE, "50");
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addBooleanAttribute(mdBusiness1);
    mdAttribute.apply();

    // Create new states and transitions for a new MdStateMachine
    MdStateMachineDAO mdStateMachine = MdStateMachineDAO.newInstance();
    mdStateMachine.setValue(MdStateMachineInfo.NAME, "StateMachine1");
    mdStateMachine.setValue(MdStateMachineInfo.PACKAGE, "test.xmlclasses");
    mdStateMachine.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "StateMachine1");
    mdStateMachine.setStructValue(MdStateMachineInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "State Machine of Class1");
    mdStateMachine.setValue(MdStateMachineInfo.SUPER_MD_BUSINESS, EntityTypes.STATE_MASTER.getId());
    mdStateMachine.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness1.getId());
    mdStateMachine.apply();

    StateMasterDAO state1 = mdStateMachine.addState("Available", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
    StateMasterDAO state2 = mdStateMachine.addState("CheckedOut", StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId());
    StateMasterDAO state3 = mdStateMachine.addState("CheckedIn", StateMasterDAOIF.Entry.ENTRY_STATE.getId());
    TransitionDAO transition1 = mdStateMachine.addTransition("CheckOut", state1.getId(), state2.getId());
    TransitionDAO transition2 = mdStateMachine.addTransition("CheckIn", state2.getId(), state3.getId());
    TransitionDAO transition3 = mdStateMachine.addTransition("Stock", state3.getId(), state1.getId());
    TestFixtureFactory.delete(mdStateMachine);

    // Export the test entities
    List<StateMasterDAO> newStates = new LinkedList<StateMasterDAO>();
    newStates.add(state1);
    newStates.add(state2);
    newStates.add(state3);

    List<TransitionDAO> newTransitions = new LinkedList<TransitionDAO>();
    newTransitions.add(transition1);
    newTransitions.add(transition2);
    newTransitions.add(transition3);

    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 });
    metadata.addNewMdStateMachine(mdBusiness1, mdStateMachine, newStates, newTransitions);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    mdBusiness1 = MdBusinessDAO.get(mdBusiness1.getId()).getBusinessDAO();

    // Change the values of mdBusiness, booleanAttribute, and the stateMachine
    mdBusiness1.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.setValue(MdBusinessInfo.CACHE_SIZE, "500");
    mdBusiness1.apply();

    mdAttribute.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Boolean Update Test");
    mdAttribute.apply();

    // Import the test entites
    SAXImporter.runImport(new File(tempXMLFile));

    MdBusinessDAOIF mdBusiness1IF = MdBusinessDAO.getMdBusinessDAO(CLASS);

    MdAttributeDAOIF attribute = mdBusiness1IF.definesAttribute("testBoolean");

    assertEquals(mdBusiness1IF.getStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdBusiness Set Test");
    assertEquals(mdBusiness1IF.getStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdBusiness Attributes Test");
    assertEquals(mdBusiness1IF.getValue(MdBusinessInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdBusiness1IF.getValue(MdBusinessInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdBusiness1IF.getValue(MdBusinessInfo.CACHE_SIZE), "50");

    // Change to false when casscading delete is implemented
    assertEquals(mdBusiness1IF.getValue(MetadataInfo.REMOVE), MdAttributeBooleanInfo.TRUE);

    // Ensure the attributes are linked to the correct MdBusiness object
    assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdBusiness1IF.getId());

    // Test that the MdStateMachine on the MdBusiness was correctly set
    MdStateMachineDAOIF mdStateMachineIF = mdBusiness1IF.definesMdStateMachine();

    assertNotNull(mdStateMachineIF);
    assertEquals(3, mdStateMachineIF.definesStateMasters().size());

    // Ensure that the states were imported correctly
    StateMasterDAOIF available = mdStateMachineIF.definesStateMaster("Available");
    assertEquals(true, available.isDefaultState());
    assertEquals(true, available.isEntryState());

    StateMasterDAOIF checkedOut = mdStateMachineIF.definesStateMaster("CheckedOut");
    assertEquals(false, checkedOut.isDefaultState());
    assertEquals(false, checkedOut.isEntryState());

    StateMasterDAOIF checkedIn = mdStateMachineIF.definesStateMaster("CheckedIn");
    assertEquals(false, checkedIn.isDefaultState());
    assertEquals(true, checkedIn.isEntryState());

    // Ensure that the transitions were imported correctly
    assertEquals(3, mdStateMachineIF.definesTransitions().size());

    TransitionDAOIF checkOut = mdStateMachineIF.definesTransition("CheckOut");
    assertEquals(available.getId(), checkOut.getParentId());
    assertEquals(checkedOut.getId(), checkOut.getChildId());

    RelationshipDAOIF checkIn = mdStateMachineIF.definesTransition("CheckIn");
    assertEquals(checkedOut.getId(), checkIn.getParentId());
    assertEquals(checkedIn.getId(), checkIn.getChildId());

    RelationshipDAOIF stock = mdStateMachineIF.definesTransition("Stock");
    assertEquals(checkedIn.getId(), stock.getParentId());
    assertEquals(available.getId(), stock.getChildId());
  }

  /**
   * Test setting of attributes on a boolean datatype
   */
  public void testRenameAttribute()
  {
    String updatedName = "updatedName";

    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdBoolean = TestFixtureFactory.addBooleanAttribute(mdBusiness1);
    mdBoolean.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Boolean Test");
    mdBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Positive_Label");
    mdBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Negative_Label");
    mdBoolean.apply();

    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 });
    metadata.renameAttribute(mdBoolean, updatedName);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    mdBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Not_Positive_Label");
    mdBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Not_Negative_Label");
    mdBoolean.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);

    MdAttributeBooleanDAO attribute = (MdAttributeBooleanDAO) ( mdEntityIF.definesAttribute(updatedName) ).getBusinessDAO();

    assertEquals(attribute.getStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Boolean Set Test");
    assertEquals(attribute.getStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Positive_Label");
    assertEquals(attribute.getStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Negative_Label");

    // This will fail until the object cached is fixed. It currently holds onto
    // the old attribute definition.
    assertNull(mdEntityIF.definesAttribute("testBoolean"));
  }

  /**
   * Test setting of attributes on a boolean datatype
   */
  public void testUpdateBoolean()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdBoolean = TestFixtureFactory.addBooleanAttribute(mdBusiness1);
    mdBoolean.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Boolean Test");
    mdBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Positive_Label");
    mdBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Negative_Label");
    mdBoolean.apply();

    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 });
    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    mdBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Not_Positive_Label");
    mdBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Not_Negative_Label");
    mdBoolean.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeBooleanDAO attribute = (MdAttributeBooleanDAO) ( mdEntityIF.definesAttribute("testBoolean") ).getBusinessDAO();

    assertEquals(attribute.getStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Boolean Set Test");
    assertEquals(attribute.getStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Positive_Label");
    assertEquals(attribute.getStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Negative_Label");
  }

  /**
   * Test setting of attributes on a blob datatype
   */
  public void testUpdateBlob()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addBlobAttribute(mdBusiness1);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeBlobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Blob Update Test");
    mdAttribute.setValue(MdAttributeBlobInfo.IMMUTABLE, MdAttributeBooleanInfo.TRUE);
    mdAttribute.setValue(MdAttributeBlobInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttribute.setStructValue(MdAttributeBlobInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Blob Test");
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeBlobDAO attribute = (MdAttributeBlobDAO) ( mdEntityIF.definesAttribute("testBlob") ).getBusinessDAO();
    AttributeEnumerationIF index = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    assertEquals(attribute.getStructValue(MdAttributeBlobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Blob Set Test");
    assertEquals(attribute.getValue(MdAttributeBlobInfo.IMMUTABLE), MdAttributeBooleanInfo.FALSE);
    assertEquals(MdAttributeBooleanInfo.TRUE, attribute.getValue(MdAttributeBlobInfo.REQUIRED));
    assertEquals(MdAttributeBooleanInfo.TRUE, attribute.getValue(MdAttributeBlobInfo.REMOVE));
    assertEquals(attribute.getStructValue(MdAttributeBlobInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Blob Test");
    assertEquals(index.dereference()[0].getId(), IndexTypes.NO_INDEX.getId());
  }

  /**
   * Test setting of attributes on the character datatype minus any overlapping
   * attributes from the boolean test
   */
  public void testUpdateCharacter()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeCharacterDAO mdAttribute = TestFixtureFactory.addCharacterAttribute(mdBusiness1);
    mdAttribute.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.TRUE);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Character Update Test");
    mdAttribute.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttribute.setValue(MdAttributeCharacterInfo.SIZE, "300");
    mdAttribute.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getId());
    mdAttribute.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testCharacter");
    AttributeEnumerationIF index = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    assertEquals(attribute.getStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Character Set Test");
    assertEquals(attribute.getValue(MdAttributeCharacterInfo.SIZE), "200");
    assertEquals(attribute.getValue(MdAttributeCharacterInfo.IMMUTABLE), MdAttributeBooleanInfo.TRUE);
    assertEquals(index.dereference()[0].getId(), IndexTypes.NO_INDEX.getId());
  }

  /**
   * Test setting of attributes on the date datatype minus any overlapping
   * attributes from the boolean test
   */
  public void testUpdateDate()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addDateAttribute(mdBusiness1);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Date Update Test");
    mdAttribute.setValue(MdAttributeDateInfo.DEFAULT_VALUE, "2006-02-12");
    mdAttribute.setValue(MdAttributeDateInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttribute.setStructValue(MdAttributeDateInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Date Update Test");
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testDate");
    AttributeEnumerationIF index = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    assertEquals(attribute.getStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Date Set Test");
    assertEquals(attribute.getValue(MdAttributeDateInfo.REQUIRED), MdAttributeBooleanInfo.TRUE);
    assertEquals(attribute.getValue(MdAttributeDateInfo.DEFAULT_VALUE), "2006-02-11");
    assertEquals(index.dereference()[0].getId(), IndexTypes.UNIQUE_INDEX.getId());
  }

  /**
   * Test setting of attributes on the dateTime datatype minus any overlapping
   * attributes from the boolean test
   */
  public void testUpdateDateTime()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addDateTimeAttribute(mdBusiness1);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "dateTime Update Test");
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testDateTime");
    AttributeEnumerationIF index = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    assertEquals(attribute.getStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "dateTime Set Test");
    assertEquals(index.dereference()[0].getId(), IndexTypes.NON_UNIQUE_INDEX.getId());
  }

  /**
   * Test setting of attributes on the decimal datatype minus any overlapping
   * attributes from the boolean test
   */
  public void testUpdateDecimal()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addDecimalAttribute(mdBusiness1);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeDecimalInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Decimal Update Test");
    mdAttribute.setValue(MdAttributeDecimalInfo.LENGTH, "12");
    mdAttribute.setValue(MdAttributeDecimalInfo.DECIMAL, "3");
    mdAttribute.setValue(MdAttributeDecimalInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.FALSE);
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testDecimal");

    assertEquals(attribute.getStructValue(MdAttributeDecimalInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Decimal Set Test");
    assertEquals(attribute.getValue(MdAttributeDecimalInfo.LENGTH), "10");
    assertEquals(attribute.getValue(MdAttributeDecimalInfo.DECIMAL), "2");
    assertEquals(attribute.getValue(MdAttributeDecimalInfo.REJECT_NEGATIVE), MdAttributeBooleanInfo.TRUE);
    assertEquals(attribute.getValue(MdAttributeDecimalInfo.REJECT_ZERO), MdAttributeBooleanInfo.FALSE);
    assertEquals(attribute.getValue(MdAttributeDecimalInfo.REJECT_POSITIVE), MdAttributeBooleanInfo.FALSE);
  }

  /**
   * Test setting of attributes on the double datatype minus any overlapping
   * attributes from the boolean and decimal test
   */
  public void testUpdateDouble()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addDoubleAttribute(mdBusiness1);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Double Update Test");
    mdAttribute.setValue(MdAttributeDoubleInfo.LENGTH, "4");
    mdAttribute.setValue(MdAttributeDoubleInfo.DECIMAL, "2");
    mdAttribute.setValue(MdAttributeDoubleInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testDouble");

    assertEquals(attribute.getStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Double Set Test");
    assertEquals(attribute.getValue(MdAttributeDoubleInfo.LENGTH), "9");
    assertEquals(attribute.getValue(MdAttributeDoubleInfo.DECIMAL), "4");
    assertEquals(attribute.getValue(MdAttributeDoubleInfo.REJECT_ZERO), MdAttributeBooleanInfo.TRUE);
  }

  public void testUpdateVirtual()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeDoubleDAO concrete = TestFixtureFactory.addDoubleAttribute(mdBusiness1);
    concrete.apply();

    MdViewDAO mdView = TestFixtureFactory.createMdView1();
    mdView.apply();

    TestFixtureFactory.addVirtualAttribute(mdView, concrete).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1, mdView }));

    SAXImporter.runImport(new File(tempXMLFile));

    MdViewDAOIF mdViewIF = MdViewDAO.getMdViewDAO(VIEW);
    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testDouble");
    MdAttributeDAOIF virtual = mdViewIF.definesAttribute("testVirtual");

    assertEquals(virtual.getValue(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE), attribute.getId());
  }

  /**
   * Test setting of attributes on the enumeration datatype minus any
   * overlapping attributes from the boolean test As a side effect does testing
   * on setting instance/instance_value tags
   */
  public void testUpdateEnumeration()
  {
    MdBusinessDAO mdBusinessEnum1 = TestFixtureFactory.createEnumClass1();
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();

    mdBusinessEnum1.apply();
    mdBusiness1.apply();

    TestFixtureFactory.addCharacterAttribute(mdBusinessEnum1).apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusinessEnum1.definesType());
    businessDAO.setValue(EnumerationMasterInfo.NAME, "CO");
    businessDAO.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Colorado");
    businessDAO.setValue("testCharacter", "CO");
    businessDAO.apply();

    MdEnumerationDAO mdEnumeration = TestFixtureFactory.createMdEnumeation1(mdBusinessEnum1);
    mdEnumeration.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addEnumerationAttribute(mdBusiness1, mdEnumeration);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Enumeration Update Test");
    mdAttribute.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testEnumeration");
    MdEnumerationDAOIF enumeration = MdEnumerationDAO.getMdEnumerationDAO(FILTER);

    assertEquals(attribute.getStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Enumeration Set Test");
    assertEquals(attribute.getValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE), MdAttributeBooleanInfo.FALSE);

    // Ensure that the enumeration is reference the correct enumeration class
    assertEquals(attribute.getValue(MdAttributeEnumerationInfo.MD_ENUMERATION), enumeration.getId());
  }

  /**
   * Test setting of attributes on a blob datatype
   */
  public void testUpdateFile()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addFileAttribute(mdBusiness1);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeBlobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "File Update Test");
    mdAttribute.setValue(MdAttributeBlobInfo.IMMUTABLE, MdAttributeBooleanInfo.TRUE);
    mdAttribute.setValue(MdAttributeBlobInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    mdAttribute.setStructValue(MdAttributeBlobInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "File Update Test");
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeConcreteDAO attribute = (MdAttributeConcreteDAO) ( mdEntityIF.definesAttribute("testFile") ).getBusinessDAO();
    AttributeEnumerationIF index = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    assertEquals(attribute.getStructValue(MdAttributeFileInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "File Set Test");
    assertEquals(attribute.getValue(MdAttributeFileInfo.IMMUTABLE), MdAttributeBooleanInfo.FALSE);
    assertEquals(MdAttributeBooleanInfo.TRUE, attribute.getValue(MdAttributeFileInfo.REQUIRED));
    assertEquals(MdAttributeBooleanInfo.TRUE, attribute.getValue(MdAttributeFileInfo.REMOVE));
    assertEquals(attribute.getStructValue(MdAttributeFileInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "File Test");
    assertEquals(index.dereference()[0].getId(), IndexTypes.NO_INDEX.getId());
  }

  /**
   * Test setting of attributes on the character datatype minus any overlapping
   * attributes from the boolean, decimal, and double test
   */
  public void testUpdateFloat()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addFloatAttribute(mdBusiness1);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Float Update Test");
    mdAttribute.setValue(MdAttributeFloatInfo.LENGTH, "11");
    mdAttribute.setValue(MdAttributeFloatInfo.DECIMAL, "3");
    mdAttribute.setValue(MdAttributeFloatInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.FALSE);
    mdAttribute.setValue(MdAttributeFloatInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testFloat");

    assertEquals(attribute.getStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Float Set Test");
    assertEquals(attribute.getValue(MdAttributeFloatInfo.LENGTH), "10");
    assertEquals(attribute.getValue(MdAttributeFloatInfo.DECIMAL), "2");
    assertEquals(attribute.getValue(MdAttributeFloatInfo.REJECT_POSITIVE), MdAttributeBooleanInfo.TRUE);
    assertEquals(attribute.getValue(MdAttributeFloatInfo.REJECT_NEGATIVE), MdAttributeBooleanInfo.TRUE);
  }

  /**
   * Test setting of mdHash attribute specific values
   */
  public void testUpdateHash()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addHashAttribute(mdBusiness1);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeHashInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Hash Update Test");
    mdAttribute.setValue(MdAttributeHashInfo.HASH_METHOD, HashMethods.SHA.getId());
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeHashDAOIF attribute = (MdAttributeHashDAOIF) mdEntityIF.definesAttribute("testHash");

    // Ensure that the hash encryption method is set
    AttributeEnumerationIF method = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeHashInfo.HASH_METHOD);
    assertEquals(HashMethods.MD5.getId(), method.dereference()[0].getId());
  }

  /**
   * Test setting of attributes on the integer datatype minus any overlapping
   * attributes from the boolean test
   */
  public void testUpdateInteger()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addIntegerAttribute(mdBusiness1);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Integer Update Test");
    mdAttribute.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttribute.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testInteger");

    assertEquals(attribute.getStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Integer Set Test");
    assertEquals(attribute.getValue(MdAttributeIntegerInfo.REJECT_POSITIVE), MdAttributeBooleanInfo.TRUE);
    assertEquals(attribute.getValue(MdAttributeIntegerInfo.REJECT_ZERO), MdAttributeBooleanInfo.TRUE);
  }

  /**
   * Test setting of attributes on the long datatype minus any overlapping
   * attributes from the boolean test
   */
  public void testUpdateLong()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addLongAttribute(mdBusiness1);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Long Update Test");
    mdAttribute.setValue(MdAttributeLongInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testLong");

    assertEquals(attribute.getStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Long Set Test");
    assertEquals(attribute.getValue(MdAttributeLongInfo.REJECT_ZERO), MdAttributeBooleanInfo.TRUE);
  }

  /**
   * Test setting of attributes on the reference datatype minus any overlapping
   * attributes from the boolean test
   */
  public void testUpdateReference()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();

    mdBusiness1.apply();
    mdBusiness2.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addReferenceAttribute(mdBusiness1, mdBusiness2);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference Update Test");
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testReference");
    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(CLASS2);

    assertEquals(attribute.getStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Reference Test");

    // Ensure that the reference is referencing the correct class
    assertEquals(attribute.getValue(MdAttributeReferenceInfo.REF_MD_BUSINESS), mdBusinessIF.getId());
  }

  /**
   * Test setting of attributes on the struct datatype minus any overlapping
   * attributes from the boolean test
   */
  public void testUpdateStruct()
  {
    MdStructDAO mdStruct = TestFixtureFactory.createMdStruct1();
    mdStruct.setValue(MdBusinessInfo.CACHE_SIZE, "525");
    mdStruct.apply();

    MdAttributeConcreteDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdStruct);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFAULT_VALUE, MdAttributeBooleanInfo.FALSE);
    mdAttributeBoolean.apply();

    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addStructAttribute(mdBusiness1, mdStruct);
    mdAttribute.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO.setStructValue("testStruct", "testBoolean", MdAttributeBooleanInfo.TRUE);
    businessDAO.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Struct Update Test");
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF mdAttributeIF = mdEntityIF.definesAttribute("testStruct");
    MdStructDAOIF mdStructIF = MdStructDAO.getMdStructDAO(mdStruct.definesType());

    assertEquals(mdAttributeIF.getStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Struct Set Test");
    assertEquals(mdStructIF.getValue(MdBusinessInfo.CACHE_SIZE), "525");

    // Ensure that the correct class is being referenced
    assertEquals(mdAttributeIF.getValue(MdAttributeStructInfo.MD_STRUCT), mdStructIF.getId());

    List<String> ids = EntityDAO.getEntityIdsDB(CLASS);

    assertEquals(1, ids.size());

    BusinessDAOIF businessDAOIF = BusinessDAO.get(ids.get(0));
    AttributeStruct attribute = (AttributeStruct) businessDAOIF.getAttributeIF("testStruct");

    assertEquals(MdAttributeBooleanInfo.TRUE, attribute.getValue("testBoolean"));
  }

  /**
   * Test setting of symmetric attribute specific value.
   */
  public void testUpdateSymmetric()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addSymmetricAttribute(mdBusiness1);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeSymmetricInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Symmetric Update TEST");
    mdAttribute.setValue(MdAttributeSymmetricInfo.SECRET_KEY_SIZE, "256");
    mdAttribute.setValue(MdAttributeSymmetricInfo.SYMMETRIC_METHOD, SymmetricMethods.AES.getId());
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeSymmetricDAOIF attribute = (MdAttributeSymmetricDAOIF) mdEntityIF.definesAttribute("testSymmetric");

    // Ensure that the symmetric encryption method is set
    AttributeEnumerationIF method = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeSymmetricInfo.SYMMETRIC_METHOD);
    assertEquals(SymmetricMethods.DES.getId(), method.dereference()[0].getId());
    assertEquals("56", attribute.getValue(MdAttributeSymmetricInfo.SECRET_KEY_SIZE));
  }

  /**
   * Test updating of attributes on the text datatype minus any overlapping
   * attributes from the boolean test.
   */
  public void testUpdateClob()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addTextAttribute(mdBusiness1);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Text Update Test");
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testText");

    assertEquals(attribute.getStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Text Set Test");
  }

  /**
   * Test updating of attributes on the clob datatype minus any overlapping
   * attributes from the boolean test.
   */
  public void testUpdateText()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addClobAttribute(mdBusiness1);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Clob Update Test");
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testClob");

    assertEquals(attribute.getStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Clob Set Test");
  }

  /**
   * Test setting of attributes on the time datatype minus any overlapping
   * attributes from the boolean test
   */
  public void testUpdateTime()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addTimeAttribute(mdBusiness1);
    mdAttribute.apply();

    MdAttributeTimeDAO mdAttribute2 = MdAttributeTimeDAO.newInstance();
    mdAttribute2.setValue(MdAttributeTimeInfo.NAME, "testTime2");
    mdAttribute2.setStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Time Set Test2");
    mdAttribute2.setValue(MdAttributeTimeInfo.DEFINING_MD_CLASS, mdBusiness1.getId());

    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 });
    metadata.addNewMdAttribute(mdBusiness1, mdAttribute2);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    mdAttribute.setStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Time Update Test");
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testTime");

    assertEquals(attribute.getStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Time Set Test");
    assertEquals(mdEntityIF.definesAttribute("testTime2").getStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Time Set Test2");
  }

  /**
   * Test setting of attributes of and on the relationship datatype
   */
  public void testUpdateMdRelationship()
  {
    SAXImporter.runImport(new File(RELATIONSHIP_SET));

    MdRelationshipDAO mdRelationship = MdRelationshipDAO.getMdRelationshipDAO(RELATIONSHIP).getBusinessDAO();

    // Create a new MdAttribute for the existing MdRelationship
    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addCharacterAttribute(mdRelationship);

    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdRelationship });
    metadata.addNewMdAttribute(mdRelationship, mdAttribute);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    mdRelationship.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Relationship Update Test");
    mdRelationship.setStructValue(MdRelationshipInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Relationship Update Test");
    mdRelationship.setValue(MdRelationshipInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdRelationship.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    mdRelationship = MdRelationshipDAO.getMdRelationshipDAO(RELATIONSHIP).getBusinessDAO();
    MdBusinessDAOIF mdBusiness1 = MdBusinessDAO.getMdBusinessDAO(CLASS);
    MdBusinessDAOIF mdBusiness2 = MdBusinessDAO.getMdBusinessDAO(CLASS2);
    MdAttributeDAOIF attribute = mdRelationship.definesAttribute("testBoolean");
    MdAttributeDAOIF attributeCharacter = mdRelationship.definesAttribute("testCharacter");

    assertEquals("Relationship Set Test", mdRelationship.getStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals("Relationship Test", mdRelationship.getStructValue(MdRelationshipInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals(MdAttributeBooleanInfo.FALSE, mdRelationship.getValue(MdRelationshipInfo.REMOVE));
    assertEquals(MdAttributeBooleanInfo.TRUE, mdRelationship.getValue(MdRelationshipInfo.EXTENDABLE));
    assertEquals(MdAttributeBooleanInfo.TRUE, mdRelationship.getValue(MdRelationshipInfo.ABSTRACT));

    // Ensure the parent attributes are correctly set
    assertEquals(mdRelationship.getValue(MdRelationshipInfo.PARENT_MD_BUSINESS), mdBusiness1.getId());
    assertEquals(mdRelationship.getValue(MdRelationshipInfo.PARENT_CARDINALITY), "1");
    assertEquals(mdRelationship.getStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Parent Set Test");

    // Ensure the child attributes are correctly set
    assertEquals(mdRelationship.getValue(MdRelationshipInfo.CHILD_MD_BUSINESS), mdBusiness2.getId());
    assertEquals(mdRelationship.getValue(MdRelationshipInfo.CHILD_CARDINALITY), "*");
    assertEquals(mdRelationship.getStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Child Set Test");

    // Ensure the attributes are linked to the relationship
    assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdRelationship.getId());
    assertEquals(attributeCharacter.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdRelationship.getId());

    mdRelationship.setValue(MetadataInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdRelationship.apply();

    // Test the sub relationship
    MdRelationshipDAO mdRelationship2 = MdRelationshipDAO.getMdRelationshipDAO(RELATIONSHIP2).getBusinessDAO();
    assertEquals(mdRelationship.getId(), mdRelationship2.getValue(MdRelationshipInfo.SUPER_MD_RELATIONSHIP));
    assertEquals("2", mdRelationship2.getValue(MdRelationshipInfo.CHILD_CARDINALITY));
  }

  public void testUpdateMdFacade()
  {
    MdFacadeDAO mdFacade = MdFacadeDAO.newInstance();
    mdFacade.setValue(MdFacadeInfo.NAME, "Facade1");
    mdFacade.setValue(MdFacadeInfo.PACKAGE, "test.xmlclasses");
    mdFacade.setStructValue(MdFacadeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Facade1");
    mdFacade.setStructValue(MdFacadeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Facade1");
    mdFacade.setValue(MdFacadeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdFacade.apply();

    MdMethodDAO mdMethod = MdMethodDAO.newInstance();
    mdMethod.setValue(MdMethodInfo.NAME, "checkin");
    mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    mdMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkin");
    mdMethod.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkin");
    mdMethod.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    mdMethod.apply();

    MdParameterDAO mdParameter = MdParameterDAO.newInstance();
    mdParameter.setValue(MdParameterInfo.NAME, "param1");
    mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.String");
    mdParameter.setValue(MdParameterInfo.ORDER, "0");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "param1");
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod.getId());
    mdParameter.setStructValue(MdParameterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "param1");
    mdParameter.apply();

    MdParameterDAO mdParameter2 = MdParameterDAO.newInstance();
    mdParameter2.setValue(MdParameterInfo.NAME, "param2");
    mdParameter2.setValue(MdParameterInfo.TYPE, "java.lang.Integer");
    mdParameter2.setValue(MdParameterInfo.ORDER, "1");
    mdParameter2.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "param2");
    mdParameter2.setStructValue(MdParameterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "param2");
    mdParameter2.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod.getId());
    mdParameter2.apply();

    // Define new MdMethods and MdParameters to be added to the existing
    // MdFacade
    MdParameterDAO mdParameter3 = MdParameterDAO.newInstance();
    mdParameter3.setValue(MdParameterInfo.NAME, "param3");
    mdParameter3.setValue(MdParameterInfo.TYPE, "java.lang.String");
    mdParameter3.setValue(MdParameterInfo.ORDER, "4");
    mdParameter3.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "param3");

    MdMethodDAO mdMethod2 = MdMethodDAO.newInstance();
    mdMethod2.setValue(MdMethodInfo.NAME, "checkout");
    mdMethod2.setValue(MdMethodInfo.RETURN_TYPE, "void");
    mdMethod2.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkout");
    mdMethod2.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkout");
    mdMethod2.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);

    MdParameterDAO mdParameter4 = MdParameterDAO.newInstance();
    mdParameter4.setValue(MdParameterInfo.NAME, "param4");
    mdParameter4.setValue(MdParameterInfo.TYPE, "java.lang.String");
    mdParameter4.setValue(MdParameterInfo.ORDER, "0");
    mdParameter4.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "param4");

    // Export mdFacade with all of its MdMethods defined in the database, and
    // export a new MdMethod for the mdFacade, as well as a new MdParameter
    // for an existing MdMethod of mdFacade
    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdFacade });
    metadata.addNewMdMethod(mdFacade, mdMethod2, mdParameter4);
    metadata.addNewMdParameter(mdMethod, mdParameter3);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    mdFacade = MdFacadeDAO.get(mdFacade.getId()).getBusinessDAO();

    mdFacade.setStructValue(MdFacadeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Facade1 Update");
    mdFacade.setStructValue(MdFacadeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Facade1 Update");
    mdFacade.setValue(MdFacadeInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    mdFacade.apply();

    mdMethod.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.Long");
    mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "CheckIn Update");
    mdMethod.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "CheckIn Update");
    mdMethod.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.FALSE);
    mdMethod.apply();

    mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.Integer");
    mdParameter.setValue(MdParameterInfo.ORDER, "99");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "First Integer");
    mdParameter.setStructValue(MdParameterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parameter Update Test");
    mdParameter.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    assertTrue(MdFacadeDAO.isDefined("test.xmlclasses.Facade1"));

    MdFacadeDAOIF mdFacadeIF = MdFacadeDAO.getMdFacadeDAO("test.xmlclasses.Facade1");
    assertEquals("Facade1", mdFacadeIF.getDescription(CommonProperties.getDefaultLocale()));
    assertEquals("Facade1", mdFacadeIF.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<MdMethodDAOIF> mdMethods = mdFacadeIF.getMdMethods();

    assertEquals(2, mdMethods.size());

    int i = 0;
    int j = 1;

    if (!"checkin".equals(mdMethods.get(0).getName()))
    {
      i = 1;
      j = 0;
    }

    assertEquals(true, mdMethods.get(i).isStatic());
    assertEquals("checkin", mdMethods.get(i).getName());
    assertTrue(mdMethods.get(i).getReturnType().isVoid());
    assertEquals("checkin", mdMethods.get(i).getDisplayLabel(CommonProperties.getDefaultLocale()));
    assertEquals("checkin", mdMethods.get(i).getDescription(CommonProperties.getDefaultLocale()));

    List<MdParameterDAOIF> mdParameters = mdMethods.get(i).getMdParameterDAOs();
    assertEquals(3, mdParameters.size());
    assertEquals("param1", mdParameters.get(0).getParameterName());
    assertEquals("java.lang.String", mdParameters.get(0).getParameterType().getType());
    assertEquals("0", mdParameters.get(0).getParameterOrder());
    assertEquals("param1", mdParameters.get(0).getDisplayLabel(CommonProperties.getDefaultLocale()));
    assertEquals("param1", mdParameters.get(0).getDescription(CommonProperties.getDefaultLocale()));

    assertEquals("param2", mdParameters.get(1).getParameterName());
    assertEquals("java.lang.Integer", mdParameters.get(1).getParameterType().getType());
    assertEquals("1", mdParameters.get(1).getParameterOrder());
    assertEquals("param2", mdParameters.get(1).getDisplayLabel(CommonProperties.getDefaultLocale()));

    assertEquals("param3", mdParameters.get(2).getParameterName());
    assertEquals("java.lang.String", mdParameters.get(2).getParameterType().getType());
    assertEquals("4", mdParameters.get(2).getParameterOrder());
    assertEquals("param3", mdParameters.get(2).getDisplayLabel(CommonProperties.getDefaultLocale()));

    assertEquals(true, mdMethods.get(j).isStatic());
    assertEquals("checkout", mdMethods.get(j).getName());
    assertTrue(mdMethods.get(j).getReturnType().isVoid());
    assertEquals("checkout", mdMethods.get(j).getDisplayLabel(CommonProperties.getDefaultLocale()));
    assertEquals("checkout", mdMethods.get(j).getDescription(CommonProperties.getDefaultLocale()));

    mdParameters = mdMethods.get(j).getMdParameterDAOs();
    assertEquals(1, mdParameters.size());
    assertEquals("param4", mdParameters.get(0).getParameterName());
    assertEquals("java.lang.String", mdParameters.get(0).getParameterType().getType());
    assertEquals("0", mdParameters.get(0).getParameterOrder());
    assertEquals("param4", mdParameters.get(0).getDisplayLabel(CommonProperties.getDefaultLocale()));
  }

  public void testUpdateMdController()
  {
    MdControllerDAO mdController = MdControllerDAO.newInstance();
    mdController.setValue(MdControllerInfo.NAME, "Controller1");
    mdController.setValue(MdControllerInfo.PACKAGE, "test.xmlclasses");
    mdController.setStructValue(MdControllerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Controller1");
    mdController.setStructValue(MdControllerInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Controller1");
    mdController.setValue(MdControllerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdController.apply();

    MdActionDAO mdAction = MdActionDAO.newInstance();
    mdAction.setValue(MdActionInfo.NAME, "checkin");
    mdAction.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkin");
    mdAction.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkin");
    mdAction.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
    mdAction.apply();

    MdParameterDAO mdParameter = MdParameterDAO.newInstance();
    mdParameter.setValue(MdParameterInfo.NAME, "param1");
    mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.String");
    mdParameter.setValue(MdParameterInfo.ORDER, "0");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "param1");
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction.getId());
    mdParameter.setStructValue(MdParameterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "param1");
    mdParameter.apply();

    MdParameterDAO mdParameter2 = MdParameterDAO.newInstance();
    mdParameter2.setValue(MdParameterInfo.NAME, "param2");
    mdParameter2.setValue(MdParameterInfo.TYPE, "java.lang.Integer");
    mdParameter2.setValue(MdParameterInfo.ORDER, "1");
    mdParameter2.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "param2");
    mdParameter2.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction.getId());
    mdParameter2.apply();

    // Define new MdActions and MdParameters to be added to the existing
    // MdController
    MdParameterDAO mdParameter3 = MdParameterDAO.newInstance();
    mdParameter3.setValue(MdParameterInfo.NAME, "param3");
    mdParameter3.setValue(MdParameterInfo.TYPE, "java.lang.String");
    mdParameter3.setValue(MdParameterInfo.ORDER, "4");
    mdParameter3.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "param3");

    MdActionDAO mdAction2 = MdActionDAO.newInstance();
    mdAction2.setValue(MdActionInfo.NAME, "checkout");
    mdAction2.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkout");
    mdAction2.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkout");

    MdParameterDAO mdParameter4 = MdParameterDAO.newInstance();
    mdParameter4.setValue(MdParameterInfo.NAME, "param4");
    mdParameter4.setValue(MdParameterInfo.TYPE, "java.lang.String");
    mdParameter4.setValue(MdParameterInfo.ORDER, "0");
    mdParameter4.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "param4");

    // Export mdController with all of its MdActions defined in the database,
    // and
    // export a new MdAction for the mdController, as well as a new MdParameter
    // for an existing MdAction of mdController
    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdController });
    metadata.addNewMdAction(mdController, mdAction2, mdParameter4);
    metadata.addNewMdParameter(mdAction, mdParameter3);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    mdController = MdControllerDAO.get(mdController.getId()).getBusinessDAO();

    mdController.setStructValue(MdControllerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Controller1 Update");
    mdController.setStructValue(MdControllerInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Controller1 Update");
    mdController.setValue(MdControllerInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    mdController.apply();

    mdAction.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "CheckIn Update");
    mdAction.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "CheckIn Update");
    mdAction.apply();

    mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.Integer");
    mdParameter.setValue(MdParameterInfo.ORDER, "99");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "First Integer");
    mdParameter.setStructValue(MdParameterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parameter Update Test");
    mdParameter.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    assertTrue(MdControllerDAO.isDefined("test.xmlclasses.Controller1"));

    MdControllerDAOIF mdControllerIF = MdControllerDAO.getMdControllerDAO("test.xmlclasses.Controller1");
    assertEquals("Controller1", mdControllerIF.getDescription(CommonProperties.getDefaultLocale()));
    assertEquals("Controller1", mdControllerIF.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<MdActionDAOIF> mdActions = mdControllerIF.getMdActionDAOs();

    assertEquals(2, mdActions.size());

    int i = 0;
    int j = 1;

    if (!"checkin".equals(mdActions.get(0).getName()))
    {
      i = 1;
      j = 0;
    }

    assertEquals("checkin", mdActions.get(i).getName());
    assertEquals("checkin", mdActions.get(i).getDisplayLabel(CommonProperties.getDefaultLocale()));
    assertEquals("checkin", mdActions.get(i).getDescription(CommonProperties.getDefaultLocale()));

    List<MdParameterDAOIF> mdParameters = mdActions.get(i).getMdParameterDAOs();
    assertEquals(3, mdParameters.size());
    assertEquals("param1", mdParameters.get(0).getParameterName());
    assertEquals("java.lang.String", mdParameters.get(0).getParameterType().getType());
    assertEquals("0", mdParameters.get(0).getParameterOrder());
    assertEquals("param1", mdParameters.get(0).getDisplayLabel(CommonProperties.getDefaultLocale()));
    assertEquals("param1", mdParameters.get(0).getDescription(CommonProperties.getDefaultLocale()));

    assertEquals("param2", mdParameters.get(1).getParameterName());
    assertEquals("java.lang.Integer", mdParameters.get(1).getParameterType().getType());
    assertEquals("1", mdParameters.get(1).getParameterOrder());
    assertEquals("param2", mdParameters.get(1).getDisplayLabel(CommonProperties.getDefaultLocale()));

    assertEquals("param3", mdParameters.get(2).getParameterName());
    assertEquals("java.lang.String", mdParameters.get(2).getParameterType().getType());
    assertEquals("4", mdParameters.get(2).getParameterOrder());
    assertEquals("param3", mdParameters.get(2).getDisplayLabel(CommonProperties.getDefaultLocale()));

    assertEquals("checkout", mdActions.get(j).getName());
    assertEquals("checkout", mdActions.get(j).getDisplayLabel(CommonProperties.getDefaultLocale()));
    assertEquals("checkout", mdActions.get(j).getDescription(CommonProperties.getDefaultLocale()));

    mdParameters = mdActions.get(j).getMdParameterDAOs();
    assertEquals(1, mdParameters.size());
    assertEquals("param4", mdParameters.get(0).getParameterName());
    assertEquals("java.lang.String", mdParameters.get(0).getParameterType().getType());
    assertEquals("0", mdParameters.get(0).getParameterOrder());
    assertEquals("param4", mdParameters.get(0).getDisplayLabel(CommonProperties.getDefaultLocale()));
  }

  /**
   * Test setting of attributes of and on the struct datatype,
   */
  public void testUpdateMdStruct()
  {
    SAXImporter.runImport(new File(STANDALONE_STRUCT_SET));

    MdStructDAO mdStruct = ( (MdStructDAO) MdStructDAO.getMdStructDAO(CLASS) ).getBusinessDAO();

    // Create a new MdAttribute for the existing MdStruct
    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addCharacterAttribute(mdStruct);

    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdStruct });
    metadata.addNewMdAttribute(mdStruct, mdAttribute);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    // Change the MdStruct from what was exported
    mdStruct.setStructValue(MdStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Struct Update Test");
    mdStruct.setStructValue(MdStructInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Update Struct Attributes Test");
    mdStruct.setValue(MdStructInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    mdStruct.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    // Ensure that the MdStruct was set back to its exported form
    mdStruct = ( (MdStructDAO) MdStructDAO.getMdStructDAO(CLASS) ).getBusinessDAO();
    MdAttributeDAOIF attribute = mdStruct.definesAttribute("testBoolean");
    MdAttributeDAOIF attributeCharacter = mdStruct.definesAttribute("testCharacter");

    assertEquals("Struct Set Test", mdStruct.getStructValue(MdStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals("Set Struct Attributes Test", mdStruct.getStructValue(MdStructInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals(MdAttributeBooleanInfo.TRUE, mdStruct.getValue(MdStructInfo.REMOVE));

    // Ensure the attributes are linked to the struct
    assertEquals(mdStruct.getId(), attribute.getValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS));
    assertEquals(mdStruct.getId(), attributeCharacter.getValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS));
  }

  /**
   * Test setting of attributes of and on the class datatype
   */
  public void testUpdateMdException()
  {
    // Create test MdBusiness
    MdExceptionDAO mdException1 = TestFixtureFactory.createMdException1();
    mdException1.setValue(MdExceptionInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdException1.setValue(MdExceptionInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdException1.setValue(MdExceptionInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdException1.setValue(MdExceptionInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdException1.apply();

    MdAttributeConcreteDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdException1);

    MdExceptionDAO mdException2 = TestFixtureFactory.createMdException2();
    mdException2.setValue(MdExceptionInfo.SUPER_MD_EXCEPTION, mdException1.getId());
    mdException2.setValue(MdExceptionInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdException2.apply();

    // Export the test entities
    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdException1, mdException2 });
    metadata.addNewMdAttribute(mdException1, mdAttributeBoolean);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    // Import the test entites
    SAXImporter.runImport(new File(tempXMLFile));

    MdExceptionDAOIF mdException1IF = MdExceptionDAO.getMdException(EXCEPTION);
    MdExceptionDAOIF mdException2IF = MdExceptionDAO.getMdException(EXCEPTION2);

    MdAttributeDAOIF attribute = mdException1IF.definesAttribute("testBoolean");

    assertEquals(mdException1IF.getStructValue(MdElementInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdException Set Test");
    assertEquals(mdException1IF.getStructValue(MdElementInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdException Attributes Test");
    assertEquals(mdException1IF.getValue(MdElementInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdException1IF.getValue(MdElementInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    assertEquals(MdAttributeBooleanInfo.FALSE, mdException1IF.getValue(MdElementInfo.PUBLISH));

    // Change to false when casscading delete is implemented
    assertEquals(mdException1IF.getValue(MdElementInfo.REMOVE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdException2IF.getValue(MdElementInfo.EXTENDABLE), MdAttributeBooleanInfo.FALSE);

    // Ensure inheritance is linking to the correct super class
    assertEquals(mdException2IF.getValue(MdExceptionInfo.SUPER_MD_EXCEPTION), mdException1IF.getId());

    // Ensure the attributes are linked to the correct MdBusiness object
    assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdException1IF.getId());
  }

  public void testUpdateMdProblem()
  {
    // Create test MdBusiness
    MdProblemDAO mdProblem1 = TestFixtureFactory.createMdProblem1();
    mdProblem1.setValue(MdProblemInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdProblem1.setValue(MdProblemInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdProblem1.setValue(MdProblemInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdProblem1.setValue(MdProblemInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdProblem1.apply();

    MdAttributeConcreteDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdProblem1);

    MdProblemDAO mdProblem2 = TestFixtureFactory.createMdProblem2();
    mdProblem2.setValue(MdProblemInfo.SUPER_MD_PROBLEM, mdProblem1.getId());
    mdProblem2.setValue(MdProblemInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdProblem2.apply();

    // Export the test entities
    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdProblem1, mdProblem2 });
    metadata.addNewMdAttribute(mdProblem1, mdAttributeBoolean);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    // Import the test entites
    SAXImporter.runImport(new File(tempXMLFile));

    MdProblemDAOIF mdProblem1IF = MdProblemDAO.getMdProblem(EXCEPTION);
    MdProblemDAOIF mdProblem2IF = MdProblemDAO.getMdProblem(EXCEPTION2);

    MdAttributeDAOIF attribute = mdProblem1IF.definesAttribute("testBoolean");

    assertEquals(mdProblem1IF.getStructValue(MdProblemInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdProblem Set Test");
    assertEquals(mdProblem1IF.getStructValue(MdProblemInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdProblem Attributes Test");
    assertEquals(mdProblem1IF.getValue(MdProblemInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdProblem1IF.getValue(MdProblemInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    assertEquals(MdAttributeBooleanInfo.FALSE, mdProblem1IF.getValue(MdProblemInfo.PUBLISH));

    // Change to false when casscading delete is implemented
    assertEquals(mdProblem1IF.getValue(MdProblemInfo.REMOVE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdProblem2IF.getValue(MdProblemInfo.EXTENDABLE), MdAttributeBooleanInfo.FALSE);

    // Ensure inheritance is linking to the correct super class
    assertEquals(mdProblem2IF.getValue(MdProblemInfo.SUPER_MD_PROBLEM), mdProblem1IF.getId());

    // Ensure the attributes are linked to the correct MdBusiness object
    assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdProblem1IF.getId());
  }

  public void testUpdateMdInformation()
  {
    // Create test MdBusiness
    MdInformationDAO mdInformation1 = TestFixtureFactory.createMdInformation1();
    mdInformation1.setValue(MdInformationInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdInformation1.setValue(MdInformationInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdInformation1.setValue(MdInformationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdInformation1.setValue(MdInformationInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdInformation1.apply();

    MdAttributeConcreteDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdInformation1);

    MdInformationDAO mdInformation2 = TestFixtureFactory.createMdInformation2();
    mdInformation2.setValue(MdInformationInfo.SUPER_MD_INFORMATION, mdInformation1.getId());
    mdInformation2.setValue(MdInformationInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdInformation2.apply();

    // Export the test entities
    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdInformation1, mdInformation2 });
    metadata.addNewMdAttribute(mdInformation1, mdAttributeBoolean);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    // Import the test entites
    SAXImporter.runImport(new File(tempXMLFile));

    MdInformationDAOIF mdInformation1IF = MdInformationDAO.getMdInformation(INFORMATION);
    MdInformationDAOIF mdInformation2IF = MdInformationDAO.getMdInformation(INFORMATION2);

    MdAttributeDAOIF attribute = mdInformation1IF.definesAttribute("testBoolean");

    assertEquals(mdInformation1IF.getStructValue(MdInformationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdInformation Set Test");
    assertEquals(mdInformation1IF.getStructValue(MdInformationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdInformation Attributes Test");
    assertEquals(mdInformation1IF.getValue(MdInformationInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdInformation1IF.getValue(MdInformationInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    assertEquals(MdAttributeBooleanInfo.FALSE, mdInformation1IF.getValue(MdInformationInfo.PUBLISH));

    // Change to false when casscading delete is implemented
    assertEquals(mdInformation1IF.getValue(MdInformationInfo.REMOVE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdInformation2IF.getValue(MdInformationInfo.EXTENDABLE), MdAttributeBooleanInfo.FALSE);

    // Ensure inheritance is linking to the correct super class
    assertEquals(mdInformation2IF.getValue(MdInformationInfo.SUPER_MD_INFORMATION), mdInformation1IF.getId());

    // Ensure the attributes are linked to the correct MdBusiness object
    assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdInformation1IF.getId());
  }

  public void testUpdateMdView()
  {
    // Create test MdView
    MdViewDAO mdView1 = TestFixtureFactory.createMdView1();
    mdView1.setValue(MdViewInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdView1.setValue(MdViewInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdView1.setValue(MdViewInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdView1.setValue(MdViewInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdView1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addBooleanAttribute(mdView1);

    MdViewDAO mdView2 = TestFixtureFactory.createMdView2();
    mdView2.setValue(MdViewInfo.SUPER_MD_VIEW, mdView1.getId());
    mdView2.setValue(MdViewInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdView2.apply();

    // Export the test entities
    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdView1, mdView2 });
    metadata.addNewMdAttribute(mdView1, mdAttribute);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    // Import the test entites
    SAXImporter.runImport(new File(tempXMLFile));

    MdViewDAOIF mdView1IF = MdViewDAO.getMdViewDAO(mdView1.definesType());
    MdViewDAOIF mdView2IF = MdViewDAO.getMdViewDAO(mdView2.definesType());

    MdAttributeDAOIF attribute = mdView1IF.definesAttribute("testBoolean");

    assertEquals(mdView1IF.getStructValue(MdViewInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdException Set Test");
    assertEquals(mdView1IF.getStructValue(MdViewInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdException Attributes Test");
    assertEquals(mdView1IF.getValue(MdViewInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdView1IF.getValue(MdViewInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    assertEquals(MdAttributeBooleanInfo.FALSE, mdView1IF.getValue(MdViewInfo.PUBLISH));

    // Change to false when casscading delete is implemented
    assertEquals(mdView1IF.getValue(MdViewInfo.REMOVE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdView2IF.getValue(MdViewInfo.EXTENDABLE), MdAttributeBooleanInfo.FALSE);

    // Ensure inheritance is linking to the correct super class
    assertEquals(mdView2IF.getValue(MdViewInfo.SUPER_MD_VIEW), mdView1IF.getId());

    // Ensure the attributes are linked to the correct MdBusiness object
    assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdView1IF.getId());
  }

  public void testUpdateMdUtil()
  {
    // Create test MdView
    MdUtilDAO mdUtil1 = TestFixtureFactory.createMdUtil1();
    mdUtil1.setValue(MdUtilInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdUtil1.setValue(MdUtilInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdUtil1.setValue(MdUtilInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdUtil1.setValue(MdUtilInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdUtil1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addBooleanAttribute(mdUtil1);

    MdUtilDAO mdUtil2 = TestFixtureFactory.createMdUtil2();
    mdUtil2.setValue(MdUtilInfo.SUPER_MD_UTIL, mdUtil1.getId());
    mdUtil2.setValue(MdUtilInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdUtil2.apply();

    // Export the test entities
    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdUtil1, mdUtil2 });
    metadata.addNewMdAttribute(mdUtil1, mdAttribute);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    // Import the test entites
    SAXImporter.runImport(new File(tempXMLFile));

    MdUtilDAOIF mdUtil1IF = MdUtilDAO.getMdUtil(mdUtil1.definesType());
    MdUtilDAOIF mdUtil2IF = MdUtilDAO.getMdUtil(mdUtil2.definesType());

    MdAttributeDAOIF attribute = mdUtil1IF.definesAttribute("testBoolean");

    assertEquals(mdUtil1IF.getStructValue(MdUtilInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdException Set Test");
    assertEquals(mdUtil1IF.getStructValue(MdUtilInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdException Attributes Test");
    assertEquals(mdUtil1IF.getValue(MdUtilInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdUtil1IF.getValue(MdUtilInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    assertEquals(MdAttributeBooleanInfo.FALSE, mdUtil1IF.getValue(MdUtilInfo.PUBLISH));

    // Change to false when casscading delete is implemented
    assertEquals(mdUtil1IF.getValue(MdUtilInfo.REMOVE), MdAttributeBooleanInfo.TRUE);
    assertEquals(mdUtil2IF.getValue(MdUtilInfo.EXTENDABLE), MdAttributeBooleanInfo.FALSE);

    // Ensure inheritance is linking to the correct super class
    assertEquals(mdUtil2IF.getValue(MdUtilInfo.SUPER_MD_UTIL), mdUtil1IF.getId());

    // Ensure the attributes are linked to the correct MdBusiness object
    assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdUtil1IF.getId());
  }

  public void testUpdateMdEnumeration()
  {
    SAXImporter.runImport(new File(FILTER_SET));

    MdEnumerationDAO mdEnumeration = MdEnumerationDAO.getMdEnumerationDAO(FILTER).getBusinessDAO();
    MdEnumerationDAO mdEnumeration2 = MdEnumerationDAO.getMdEnumerationDAO(FILTER2).getBusinessDAO();
    List<BusinessDAOIF> items = mdEnumeration.getAllEnumItems();

    ExportMetadata metadata = new ExportMetadata();
    metadata.addUpdate(mdEnumeration);
    metadata.addUpdate(mdEnumeration2);
    metadata.addRemoveEnumItem(mdEnumeration, items.get(0));

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.apply();

    mdEnumeration2.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.FALSE);
    mdEnumeration2.apply();

    mdEnumeration2.cleanEnumItems();
    mdEnumeration2.addEnumItem(items.get(0).getId());

    SAXImporter.runImport(new File(tempXMLFile));

    mdEnumeration = MdEnumerationDAO.getMdEnumerationDAO(FILTER).getBusinessDAO();
    mdEnumeration2 = MdEnumerationDAO.getMdEnumerationDAO(FILTER2).getBusinessDAO();

    items = mdEnumeration.getAllEnumItems();

    assertEquals(mdEnumeration.getStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Enumeration Filter Test");
    assertEquals(mdEnumeration.getStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Filter Set Test");
    assertEquals(mdEnumeration.getValue(MdEnumerationInfo.REMOVE), MdAttributeBooleanInfo.FALSE);
    assertEquals(mdEnumeration.getValue(MdEnumerationInfo.INCLUDE_ALL), MdAttributeBooleanInfo.FALSE);

    // Check that the instance of the filter are correctly mapped
    assertEquals(1, items.size());
    assertTrue(items.get(0).getValue("testChar").equals("CO") || items.get(0).getValue("testChar").equals("CA"));
    assertEquals(mdEnumeration2.getValue(MdEnumerationInfo.INCLUDE_ALL), MdAttributeBooleanInfo.TRUE);

    mdEnumeration.setValue(MdEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.apply();
  }

  /**
   * Test creating Instances that reference other instances
   */
  public void testUpdateObject()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();

    mdBusiness1.apply();
    mdBusiness2.apply();

    TestFixtureFactory.addCharacterAttribute(mdBusiness1).apply();
    TestFixtureFactory.addBooleanAttribute(mdBusiness1).apply();
    TestFixtureFactory.addReferenceAttribute(mdBusiness2, mdBusiness1).apply();

    BusinessDAO businessDAO1 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO1.setValue("testBoolean", MdAttributeBooleanInfo.TRUE);
    businessDAO1.setValue("testCharacter", "3");
    businessDAO1.apply();

    BusinessDAO businessDAO2 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO2.setValue("testBoolean", MdAttributeBooleanInfo.FALSE);
    businessDAO2.setValue("testCharacter", "Who are you?");
    businessDAO2.apply();

    BusinessDAO businessDAO3 = BusinessDAO.newInstance(mdBusiness2.definesType());
    businessDAO3.setValue("testReference", businessDAO1.getId());
    businessDAO3.apply();

    BusinessDAO businessDAO4 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO4.setValue("testBoolean", MdAttributeBooleanInfo.TRUE);
    businessDAO4.setValue("testCharacter", "Test Delete");
    String id = businessDAO4.apply();

    ExportMetadata metadata = new ExportMetadata();
    metadata.addDelete(businessDAO4);
    metadata.addUpdate(businessDAO2);
    metadata.addUpdate(businessDAO3);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    businessDAO2.setValue("testBoolean", MdAttributeBooleanInfo.TRUE);
    businessDAO2.apply();

    businessDAO3.setValue("testReference", businessDAO2.getId());
    businessDAO3.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    // Get the ids of the CLASS1
    List<String> classIds = EntityDAO.getEntityIdsDB(CLASS);

    // Get the first instance of CLASS2
    List<String> class2Ids = EntityDAO.getEntityIdsDB(CLASS2);

    BusinessDAOIF class2 = BusinessDAO.get(class2Ids.get(0));

    // Assert that the values of refTest refer to instances of CLASS
    assertTrue(classIds.contains(class2.getValue("testReference")));

    try
    {
      BusinessDAO.get(id);

      fail("SAXImporter did not delete the businessDAO with the id [" + id + "]");
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }
  }

  /**
   * Test creating Instances that reference other instances
   */
  public void testRekeyObject()
  {
    final String newKey = "newKey";

    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();

    mdBusiness1.apply();
    mdBusiness2.apply();

    TestFixtureFactory.addCharacterAttribute(mdBusiness1).apply();
    TestFixtureFactory.addBooleanAttribute(mdBusiness1).apply();
    TestFixtureFactory.addReferenceAttribute(mdBusiness2, mdBusiness1).apply();

    BusinessDAO businessDAO1 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO1.setValue("testBoolean", MdAttributeBooleanInfo.TRUE);
    businessDAO1.setValue("testCharacter", "3");
    businessDAO1.apply();

    BusinessDAO businessDAO2 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO2.setValue("testBoolean", MdAttributeBooleanInfo.FALSE);
    businessDAO2.setValue("testCharacter", "Who are you?");
    businessDAO2.apply();

    BusinessDAO businessDAO3 = BusinessDAO.newInstance(mdBusiness2.definesType());
    businessDAO3.setValue("testReference", businessDAO1.getId());
    businessDAO3.apply();

    BusinessDAO businessDAO4 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO4.setValue("testBoolean", MdAttributeBooleanInfo.TRUE);
    businessDAO4.setValue("testCharacter", "Test Delete");
    businessDAO4.apply();

    ExportMetadata metadata = new ExportMetadata();
    metadata.addDelete(businessDAO4);
    metadata.addUpdate(businessDAO2);
    metadata.addUpdate(businessDAO3);
    metadata.rekeyEntity(businessDAO2, newKey);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    businessDAO2.setValue("testBoolean", MdAttributeBooleanInfo.TRUE);
    businessDAO2.apply();

    businessDAO3.setValue("testReference", businessDAO2.getId());
    businessDAO3.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    assertNotNull(EntityDAO.get(mdBusiness1.definesType(), newKey));
  }

  public void testUpdateRelationship()
  {
    // Create the Metadata entities
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness1.apply();
    mdBusiness2.apply();

    MdRelationshipDAO mdRelationship1 = TestFixtureFactory.createMdRelationship1(mdBusiness1, mdBusiness2);
    mdRelationship1.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
    mdRelationship1.apply();

    TestFixtureFactory.addBooleanAttribute(mdRelationship1).apply();

    // Create BusinessDAO for the RelationshipDAO
    BusinessDAO businessDAO1 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO1.apply();

    BusinessDAO businessDAO2 = BusinessDAO.newInstance(mdBusiness2.definesType());
    businessDAO2.apply();

    // Create Test RelationshipDAO
    RelationshipDAO relationshipDAO1 = RelationshipDAO.newInstance(businessDAO1.getId(), businessDAO2.getId(), mdRelationship1.definesType());
    relationshipDAO1.setValue("testBoolean", MdAttributeBooleanInfo.TRUE);
    relationshipDAO1.apply();

    RelationshipDAO relationshipDAO2 = RelationshipDAO.newInstance(businessDAO1.getId(), businessDAO2.getId(), mdRelationship1.definesType());
    relationshipDAO2.setValue("testBoolean", MdAttributeBooleanInfo.TRUE);
    String id = relationshipDAO2.apply();

    assertEquals(2, MdRelationshipDAO.getEntityIdsDB(mdRelationship1.definesType()).size());

    // Export the test entities
    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { relationshipDAO1 });
    metadata.addDelete(relationshipDAO2);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    relationshipDAO1.setValue("testBoolean", MdAttributeBooleanInfo.FALSE);
    relationshipDAO1.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    // Get the instance of CLASS, CLASS2, and RELATIONSHIP
    List<String> c1Ids = EntityDAO.getEntityIdsDB(CLASS);
    List<String> c2Ids = EntityDAO.getEntityIdsDB(CLASS2);
    List<String> r1Ids = RelationshipDAO.getEntityIdsDB(RELATIONSHIP);

    BusinessDAOIF c1 = BusinessDAO.get(c1Ids.get(0));
    BusinessDAOIF c2 = BusinessDAO.get(c2Ids.get(0));
    RelationshipDAOIF r1 = RelationshipDAO.get(r1Ids.get(0));

    // Ensure that the parent references the instance of CLASS
    assertEquals(c1.getId(), r1.getParentId());
    // Ensure that the child reference the instance of CLASS2
    assertEquals(c2.getId(), r1.getChildId());
    // Ensure that the value of testBoolean is true
    assertEquals(MdAttributeBooleanInfo.TRUE, r1.getValue("testBoolean"));

    try
    {
      RelationshipDAO.get(id);

      fail("SAXImporter did not delete the RelationshipDAO with the id [" + id + "]");
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }

    assertEquals(1, MdRelationshipDAO.getEntityIdsDB(mdRelationship1.definesType()).size());
  }

  public void testRekeyRelationship()
  {
    final String newKey = "newKey";

    // Create the Metadata entities
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness1.apply();
    mdBusiness2.apply();

    MdRelationshipDAO mdRelationship1 = TestFixtureFactory.createMdRelationship1(mdBusiness1, mdBusiness2);
    mdRelationship1.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
    mdRelationship1.apply();

    TestFixtureFactory.addBooleanAttribute(mdRelationship1).apply();

    // Create BusinessDAO for the RelationshipDAO
    BusinessDAO businessDAO1 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO1.apply();

    BusinessDAO businessDAO2 = BusinessDAO.newInstance(mdBusiness2.definesType());
    businessDAO2.apply();

    // Create Test RelationshipDAO
    RelationshipDAO relationshipDAO1 = RelationshipDAO.newInstance(businessDAO1.getId(), businessDAO2.getId(), mdRelationship1.definesType());
    relationshipDAO1.setValue("testBoolean", MdAttributeBooleanInfo.TRUE);
    relationshipDAO1.apply();

    // Export the test entities
    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { relationshipDAO1 });
    metadata.rekeyEntity(relationshipDAO1, newKey);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    SAXImporter.runImport(new File(tempXMLFile));

    assertNotNull(RelationshipDAO.get(mdRelationship1.definesType(), newKey));
  }

  public void testReadAndWriteAllPermissions()
  {
    SAXImporter.runImport(new File(TEST_ALL_PERMISSIONS));

    try
    {
      RoleDAOIF role = RoleDAO.findRole("runway.testRole");
      MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO(CLASS);
      MdRelationshipDAOIF mdRelationship = MdRelationshipDAO.getMdRelationshipDAO(RELATIONSHIP);

      for (MdAttributeDAOIF mdAttribute : mdBusiness.definesAttributes())
      {
        if (!mdAttribute.isSystem())
        {
          Set<Operation> permissions = role.getAllPermissions(mdAttribute);

          assertTrue(permissions.contains(Operation.READ));
          assertTrue(permissions.contains(Operation.WRITE));
        }
      }

      for (MdAttributeDAOIF mdAttribute : mdRelationship.definesAttributes())
      {
        if (!mdAttribute.isSystem())
        {
          Set<Operation> permissions = role.getAllPermissions(mdAttribute);

          assertTrue(permissions.contains(Operation.READ));
          assertTrue(permissions.contains(Operation.WRITE));
        }
      }

    }
    finally
    {
      TestFixtureFactory.delete(RoleDAO.findRole("runway.testRole"));
    }

  }

  public void testMultiRolePermissions()
  {
    SAXImporter.runImport(new File(TEST_MULTI_ROLE_PERMISSIONS));

    try
    {
      RoleDAOIF role1 = RoleDAO.findRole("runway.testRole");
      RoleDAOIF role2 = RoleDAO.findRole("runway.testRole2");

      RoleDAOIF[] roles = { role1, role2 };

      MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO(CLASS);
      MdRelationshipDAOIF mdRelationship = MdRelationshipDAO.getMdRelationshipDAO(RELATIONSHIP);

      for (MdAttributeDAOIF mdAttribute : mdBusiness.definesAttributes())
      {
        if (!mdAttribute.isSystem())
        {
          for (RoleDAOIF role : roles)
          {
            Set<Operation> permissions = role.getAllPermissions(mdAttribute);

            assertTrue(permissions.contains(Operation.READ));
            assertTrue(permissions.contains(Operation.WRITE));
          }
        }
      }

      for (MdAttributeDAOIF mdAttribute : mdRelationship.definesAttributes())
      {
        if (!mdAttribute.isSystem())
        {
          for (RoleDAOIF role : roles)
          {
            Set<Operation> permissions = role.getAllPermissions(mdAttribute);

            assertTrue(permissions.contains(Operation.READ));
            assertTrue(permissions.contains(Operation.WRITE));
          }
        }
      }

    }
    finally
    {
      TestFixtureFactory.delete(RoleDAO.findRole("runway.testRole"));
      TestFixtureFactory.delete(RoleDAO.findRole("runway.testRole2"));
    }

  }

  public void testMultiUserPermissions()
  {
    SAXImporter.runImport(new File(TEST_MULTI_USER_PERMISSIONS));

    try
    {
      UserDAOIF role1 = UserDAO.findUser("testUser");
      UserDAOIF role2 = UserDAO.findUser("testUser2");

      UserDAOIF[] roles = { role1, role2 };

      MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO(CLASS);
      MdRelationshipDAOIF mdRelationship = MdRelationshipDAO.getMdRelationshipDAO(RELATIONSHIP);

      for (MdAttributeDAOIF mdAttribute : mdBusiness.definesAttributes())
      {
        if (!mdAttribute.isSystem())
        {
          for (UserDAOIF role : roles)
          {
            Set<Operation> permissions = role.getAllPermissions(mdAttribute);

            assertTrue(permissions.contains(Operation.READ));
            assertTrue(permissions.contains(Operation.WRITE));
          }
        }
      }

      for (MdAttributeDAOIF mdAttribute : mdRelationship.definesAttributes())
      {
        if (!mdAttribute.isSystem())
        {
          for (UserDAOIF role : roles)
          {
            Set<Operation> permissions = role.getAllPermissions(mdAttribute);

            assertTrue(permissions.contains(Operation.READ));
            assertTrue(permissions.contains(Operation.WRITE));
          }
        }
      }

    }
    finally
    {
      TestFixtureFactory.delete(UserDAO.findUser("testUser"));
      TestFixtureFactory.delete(UserDAO.findUser("testUser2"));
    }

  }

  /**
   * Import Object of different types with the same key, where one object
   * references the other
   */
  public void testImportObjectsWithSameKey()
  {
    String KEY = "KEY";

    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.apply();

    try
    {
      MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
      mdBusiness2.apply();

      try
      {
        MdAttributeReferenceDAO mdAttribute = TestFixtureFactory.addReferenceAttribute(mdBusiness1, mdBusiness2);
        mdAttribute.apply();

        // Create the referenced object
        BusinessDAO referenceDAO = BusinessDAO.newInstance(mdBusiness2.definesType());
        referenceDAO.getAttribute(EntityInfo.KEY).setValue(KEY);
        referenceDAO.apply();

        // Create the business object
        BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness1.definesType());
        businessDAO.setValue("testReference", referenceDAO.getId());
        businessDAO.getAttribute(EntityInfo.KEY).setValue(KEY);
        businessDAO.apply();

        SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { businessDAO, referenceDAO }));

        TestFixtureFactory.delete(businessDAO);
        TestFixtureFactory.delete(referenceDAO);

        SAXImporter.runImport(new File(tempXMLFile));

        assertNotNull(BusinessDAO.get(mdBusiness1.definesType(), KEY));
        assertNotNull(BusinessDAO.get(mdBusiness2.definesType(), KEY));
      }
      finally
      {
        TestFixtureFactory.delete(mdBusiness2);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdBusiness1);
    }
  }

  public void testDimensionAttributePermissions()
  {
    RoleDAO role = TestFixtureFactory.createRole1();
    role.apply();

    try
    {
      MdDimensionDAO mdDimension = TestFixtureFactory.createMdDimension();
      mdDimension.apply();

      try
      {
        MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
        mdBusiness.apply();

        MdAttributeCharacterDAO mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdBusiness);
        mdAttributeCharacter.apply();

        MdAttributeLongDAO mdAttributeLong = TestFixtureFactory.addLongAttribute(mdBusiness);
        mdAttributeLong.apply();

        for (MdAttributeDAOIF mdAttribute : mdBusiness.definesAttributes())
        {
          if (!mdAttribute.isSystem())
          {
            MdAttributeDimensionDAOIF mdAttributeDimension = mdAttribute.getMdAttributeDimension(mdDimension);

            role.grantPermission(Operation.READ, mdAttributeDimension.getId());
          }
        }

        ExportMetadata metadata = ExportMetadata.buildCreate(new ComponentIF[] { role });
        metadata.addGrantPermissions(role);

        SAXExporter.export(tempXMLFile, SCHEMA, metadata);

        TestFixtureFactory.delete(role);

        SAXImporter.runImport(new File(tempXMLFile));

        RoleDAOIF test = RoleDAO.findRole(role.getRoleName());

        for (MdAttributeDAOIF mdAttribute : mdBusiness.definesAttributes())
        {
          if (!mdAttribute.isSystem())
          {
            MdAttributeDimensionDAOIF mdAttributeDimension = mdAttribute.getMdAttributeDimension(mdDimension);

            Set<Operation> permissions = test.getAssignedPermissions(mdAttributeDimension);

            assertTrue(permissions.contains(Operation.READ));
            assertFalse(permissions.contains(Operation.WRITE));
          }
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdDimension);
      }
    }
    finally
    {
      RoleDAOIF _role = RoleDAO.findRole(role.getRoleName());

      TestFixtureFactory.delete(_role);
    }
  }

  public void testUpdateMdWebForm()
  {
    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.apply();

    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    MdAttributeCharacterDAO mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdBusiness);
    mdAttributeCharacter.apply();

    MdAttributeBooleanDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdBusiness);
    mdAttributeBoolean.apply();

    MdAttributeIntegerDAO mdAttributeInteger = TestFixtureFactory.addIntegerAttribute(mdBusiness);
    mdAttributeInteger.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdWebForm, mdAttributeCharacter);
    mdWebCharacter.apply();

    MdWebBooleanDAO mdWebBoolean = TestFixtureFactory.addBooleanField(mdWebForm, mdAttributeBoolean);
    mdWebBoolean.apply();

    MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdWebForm, mdAttributeInteger);

    // Export the test entities
    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdWebForm });
    metadata.addNewMdField(mdWebForm, mdWebInteger);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    assertEquals(mdWebForm.getFormName(), test.getFormName());
    assertEquals(mdBusiness.definesType(), test.getFormMdClass().definesType());
    assertEquals(mdWebForm.getDescription(CommonProperties.getDefaultLocale()), test.getDescription(CommonProperties.getDefaultLocale()));
    assertEquals(mdWebForm.getDisplayLabel(CommonProperties.getDefaultLocale()), test.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    assertEquals(3, fields.size());

    MdWebIntegerDAO testField = (MdWebIntegerDAO) fields.get(2);

    assertEquals(mdWebInteger.getFieldName(), testField.getFieldName());
    assertEquals(mdWebInteger.getFieldOrder(), testField.getFieldOrder());
    assertEquals(mdWebInteger.getDescription(CommonProperties.getDefaultLocale()), testField.getDescription(CommonProperties.getDefaultLocale()));
    assertEquals(mdWebInteger.getDisplayLabel(CommonProperties.getDefaultLocale()), testField.getDisplayLabel(CommonProperties.getDefaultLocale()));
    assertEquals(mdAttributeInteger.definesAttribute(), testField.getDefiningMdAttribute().definesAttribute());
  }

  public static void main(String args[])
  {
    TestRunner.run(SAXParseTest.class);
  }
}
