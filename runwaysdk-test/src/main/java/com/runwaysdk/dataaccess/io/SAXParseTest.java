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
package com.runwaysdk.dataaccess.io;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.rbac.MethodActorDAO;
import com.runwaysdk.business.rbac.MethodActorDAOIF;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.constants.AndFieldConditionInfo;
import com.runwaysdk.constants.BasicConditionInfo;
import com.runwaysdk.constants.BusinessInfo;
import com.runwaysdk.constants.CharacterConditionInfo;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.DateConditionInfo;
import com.runwaysdk.constants.DoubleConditionInfo;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.HashMethods;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.LongConditionInfo;
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
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.MdAttributeMultiTermInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeSymmetricInfo;
import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.MdAttributeVirtualInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdExceptionInfo;
import com.runwaysdk.constants.MdIndexInfo;
import com.runwaysdk.constants.MdInformationInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdProblemInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.constants.MdTermRelationshipInfo;
import com.runwaysdk.constants.MdUtilInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.MdWarningInfo;
import com.runwaysdk.constants.MdWebBooleanInfo;
import com.runwaysdk.constants.MdWebDateInfo;
import com.runwaysdk.constants.MdWebDecimalInfo;
import com.runwaysdk.constants.MdWebDoubleInfo;
import com.runwaysdk.constants.MdWebFloatInfo;
import com.runwaysdk.constants.MdWebGroupInfo;
import com.runwaysdk.constants.MdWebIntegerInfo;
import com.runwaysdk.constants.MdWebLongInfo;
import com.runwaysdk.constants.MdWebTextInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.constants.SymmetricMethods;
import com.runwaysdk.constants.TermInfo;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.constants.XMLConstants;
import com.runwaysdk.constants.graph.MdEdgeInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.AndFieldConditionDAOIF;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.CharacterConditionDAOIF;
import com.runwaysdk.dataaccess.DateConditionDAOIF;
import com.runwaysdk.dataaccess.DoubleConditionDAOIF;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.FieldConditionDAOIF;
import com.runwaysdk.dataaccess.LongConditionDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeHashDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalTextDAOIF;
import com.runwaysdk.dataaccess.MdAttributeSymmetricDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdElementDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdExceptionDAOIF;
import com.runwaysdk.dataaccess.MdFieldDAOIF;
import com.runwaysdk.dataaccess.MdIndexDAOIF;
import com.runwaysdk.dataaccess.MdInformationDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.MdProblemDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.MdTermDAOIF;
import com.runwaysdk.dataaccess.MdTermRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdUtilDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.MdViewDAOIF;
import com.runwaysdk.dataaccess.MdWarningDAOIF;
import com.runwaysdk.dataaccess.MdWebBooleanDAOIF;
import com.runwaysdk.dataaccess.MdWebCharacterDAOIF;
import com.runwaysdk.dataaccess.MdWebDateDAOIF;
import com.runwaysdk.dataaccess.MdWebDoubleDAOIF;
import com.runwaysdk.dataaccess.MdWebFormDAOIF;
import com.runwaysdk.dataaccess.MdWebLongDAOIF;
import com.runwaysdk.dataaccess.MetadataDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.TermAttributeDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.AttributeLocalCharacter;
import com.runwaysdk.dataaccess.attributes.entity.AttributeLocalText;
import com.runwaysdk.dataaccess.attributes.entity.AttributeStruct;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.io.dataDefinition.ExportMetadata;
import com.runwaysdk.dataaccess.io.dataDefinition.SAXExporter;
import com.runwaysdk.dataaccess.io.dataDefinition.SAXImporter;
import com.runwaysdk.dataaccess.metadata.AndFieldConditionDAO;
import com.runwaysdk.dataaccess.metadata.CharacterConditionDAO;
import com.runwaysdk.dataaccess.metadata.DateConditionDAO;
import com.runwaysdk.dataaccess.metadata.DoubleConditionDAO;
import com.runwaysdk.dataaccess.metadata.DuplicateAttributeDefinitionException;
import com.runwaysdk.dataaccess.metadata.LongConditionDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeBlobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDecimalDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLinkDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalCharacterEmbeddedDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiTermDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTermDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeVirtualDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdExceptionDAO;
import com.runwaysdk.dataaccess.metadata.MdGraphDAO;
import com.runwaysdk.dataaccess.metadata.MdIndexDAO;
import com.runwaysdk.dataaccess.metadata.MdInformationDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdPackage;
import com.runwaysdk.dataaccess.metadata.MdProblemDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.dataaccess.metadata.MdTermRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdTreeDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.MdUtilDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.dataaccess.metadata.MdWarningDAO;
import com.runwaysdk.dataaccess.metadata.MdWebBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdWebBreakDAO;
import com.runwaysdk.dataaccess.metadata.MdWebCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdWebCommentDAO;
import com.runwaysdk.dataaccess.metadata.MdWebDateDAO;
import com.runwaysdk.dataaccess.metadata.MdWebDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdWebDecimalDAO;
import com.runwaysdk.dataaccess.metadata.MdWebDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFormDAO;
import com.runwaysdk.dataaccess.metadata.MdWebGeoDAO;
import com.runwaysdk.dataaccess.metadata.MdWebGroupDAO;
import com.runwaysdk.dataaccess.metadata.MdWebHeaderDAO;
import com.runwaysdk.dataaccess.metadata.MdWebIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdWebLongDAO;
import com.runwaysdk.dataaccess.metadata.MdWebMultipleTermDAO;
import com.runwaysdk.dataaccess.metadata.MdWebPrimitiveDAO;
import com.runwaysdk.dataaccess.metadata.MdWebSingleTermGridDAO;
import com.runwaysdk.dataaccess.metadata.MdWebTextDAO;
import com.runwaysdk.dataaccess.metadata.MdWebTimeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.RelationshipDAOQuery;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.FieldConditionDAO;

/**
 * @author Justin
 * 
 */
public class SAXParseTest
{
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

  public static final String   WARNING                     = "test.xmlclasses.Warning1";

  public static final String   WARNING2                    = "test.xmlclasses.Warning2";

  public static final String   RELATIONSHIP                = "test.xmlclasses.Relationship1";

  public static final String   RELATIONSHIP2               = "test.xmlclasses.Relationship2";

  public static final String   ENUM_CLASS                  = "test.xmlclasses.EnumClassTest";

  public static final String[] classNames                  = { RELATIONSHIP, RELATIONSHIP2, FILTER, FILTER2, CLASS, CLASS2, CLASS3, ENUM_CLASS };

  @BeforeClass
  public static void classSetup()
  {
    LocalProperties.setSkipCodeGenAndCompile(true);
  }

  @AfterClass
  public static void classTearDown()
  {
    LocalProperties.setSkipCodeGenAndCompile(false);
  }

  /**
   * Delete all MetaData objects which were created in the class
   * 
   * @see junit.framework.TestCase#tearDown()
   */
  @Request
  @After
  public void tearDown() throws Exception
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
  @Request
  @Test
  public void testNoSource()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();
  }

  /**
   * Test setting of attributes on the local text datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testCreateLocalText()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

    Assert.assertEquals(mdAttributeIF.getStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Local Text Test");

    List<String> ids = EntityDAO.getEntityIdsDB(CLASS);

    Assert.assertEquals(1, ids.size());

    BusinessDAOIF businessDAOIF = BusinessDAO.get(ids.get(0));
    AttributeLocalText attribute = (AttributeLocalText) businessDAOIF.getAttributeIF("testLocalText");

    Assert.assertEquals("Yo Diggidy", attribute.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE));
  }

  /**
   * Test setting of attributes on the local character datatype minus any
   * overlapping attributes from the boolean test
   */
  @Request
  @Test
  public void testCreateLocalCharacter()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

    Assert.assertEquals(mdAttributeIF.getStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Local Character Test");

    List<String> ids = EntityDAO.getEntityIdsDB(CLASS);

    Assert.assertEquals(1, ids.size());

    BusinessDAOIF businessDAOIF = BusinessDAO.get(ids.get(0));
    AttributeLocalCharacter attribute = (AttributeLocalCharacter) businessDAOIF.getAttributeIF("testLocalCharacter");

    Assert.assertEquals("Yo Diggidy", attribute.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE));
  }

  /**
   * Test setting of attributes on a blob datatype
   */
  @Request
  @Test
  public void testCreateBlob()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    TestFixtureFactory.addBlobAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeConcreteDAO attribute = (MdAttributeConcreteDAO) ( mdEntityIF.definesAttribute("testBlob") ).getBusinessDAO();
    AttributeEnumerationIF index = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    Assert.assertEquals(attribute.getStructValue(MdAttributeConcreteInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Blob Set Test");
    Assert.assertEquals(attribute.getValue(MdAttributeConcreteInfo.IMMUTABLE), MdAttributeBooleanInfo.FALSE);
    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, attribute.getValue(MdAttributeConcreteInfo.REQUIRED));
    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, attribute.getValue(MdAttributeConcreteInfo.REMOVE));
    Assert.assertEquals(attribute.getStructValue(MdAttributeConcreteInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Blob Test");
    Assert.assertEquals(index.dereference()[0].getOid(), IndexTypes.NO_INDEX.getOid());
  }

  /**
   * Test setting of attributes on a boolean datatype
   */
  @Request
  @Test
  public void testCreateBoolean()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdAttributeBooleanDAO mdBoolean = TestFixtureFactory.addBooleanAttribute(mdBusiness1);
    mdBoolean.setValue(MdAttributeBooleanInfo.DEFAULT_VALUE, MdAttributeBooleanInfo.FALSE);
    mdBoolean.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdBoolean.setValue(MdAttributeBooleanInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());
    mdBoolean.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Boolean Test");
    mdBoolean.setValue(MdAttributeBooleanInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdBoolean.setValue(MdAttributeBooleanInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    mdBoolean.setValue(MdAttributeBooleanInfo.IS_EXPRESSION, MdAttributeBooleanInfo.TRUE);
    mdBoolean.setValue(MdAttributeBooleanInfo.EXPRESSION, MdAttributeBooleanInfo.TRUE);
    mdBoolean.apply();

    mdBusiness1.setValue(MdBusinessInfo.DTO_STUB_SOURCE, TestFixtureFactory.getMdBusinessDTOStub());

    MdBusinessDAO updateBusiness = MdBusinessDAO.get(mdBusiness1.getOid()).getBusinessDAO();
    updateBusiness.setValue(MdBusinessInfo.DTO_STUB_SOURCE, TestFixtureFactory.getMdBusinessDTOStub());
    updateBusiness.apply();

    ExportMetadata metadata = new ExportMetadata(true);
    metadata.addCreate(new ComponentIF[] { mdBusiness1 });

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    Assert.assertEquals(TestFixtureFactory.getMdBusinessDTOStub().trim(), mdEntityIF.getValue(MdBusinessInfo.DTO_STUB_SOURCE).trim());

    MdAttributeBooleanDAO attribute = (MdAttributeBooleanDAO) ( mdEntityIF.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN) ).getBusinessDAO();
    AttributeEnumerationIF index = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    Assert.assertEquals(attribute.getStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Boolean Set Test");
    Assert.assertEquals(attribute.getValue(MdAttributeBooleanInfo.DEFAULT_VALUE), MdAttributeBooleanInfo.FALSE);
    Assert.assertEquals(attribute.getValue(MdAttributeBooleanInfo.IMMUTABLE), MdAttributeBooleanInfo.FALSE);
    Assert.assertEquals(attribute.getValue(MdAttributeBooleanInfo.REQUIRED), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(attribute.getValue(MdAttributeBooleanInfo.REMOVE), MdAttributeBooleanInfo.FALSE);
    Assert.assertEquals(attribute.getStructValue(MdAttributeBooleanInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Boolean Test");
    Assert.assertEquals(attribute.getStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(attribute.getStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), MdAttributeBooleanInfo.FALSE);
    Assert.assertEquals(index.dereference()[0].getOid(), IndexTypes.UNIQUE_INDEX.getOid());
    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, attribute.getValue(MdAttributeBooleanInfo.IS_EXPRESSION));
    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, attribute.getValue(MdAttributeBooleanInfo.EXPRESSION));

    attribute.setValue(MdAttributeBooleanInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(attribute.getValue(MdAttributeBooleanInfo.REMOVE), MdAttributeBooleanInfo.TRUE);

    attribute.apply();
  }

  /**
   * Test setting of attributes on the character datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testCreateCharacter()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdAttributeCharacterDAO mdAttribute = TestFixtureFactory.addCharacterAttribute(mdBusiness1);
    mdAttribute.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.TRUE);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute(TestFixConst.ATTRIBUTE_CHARACTER);
    AttributeEnumerationIF index = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    Assert.assertEquals(attribute.getStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Character Set Test");
    Assert.assertEquals(attribute.getValue(MdAttributeCharacterInfo.SIZE), "200");
    Assert.assertEquals(attribute.getValue(MdAttributeCharacterInfo.IMMUTABLE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(index.dereference()[0].getOid(), IndexTypes.NO_INDEX.getOid());
  }

  /**
   * Test the manual setting of the column name, as well as automatically
   * generated column name collisions.
   */
  @Request
  @Test
  public void testColumnNames()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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
    MdAttributeDAOIF mdAttributeDAOIF_1 = mdEntityIF.definesAttribute(TestFixConst.ATTRIBUTE_CHARACTER);
    MdAttributeDAOIF mdAttributeDAOIF_2 = mdEntityIF.definesAttribute("testCharacterCharacterCharacterCharacter1");
    MdAttributeDAOIF mdAttributeDAOIF_3 = mdEntityIF.definesAttribute("testCharacterCharacterCharacterCharacter2");

    // Test manual column name
    Assert.assertEquals(mdAttributeDAOIF_1.getValue(MdAttributeCharacterInfo.COLUMN_NAME), "test_char");
    Assert.assertEquals(mdAttributeDAOIF_2.getValue(MdAttributeCharacterInfo.COLUMN_NAME), "test_character_character_cha");
    Assert.assertEquals(mdAttributeDAOIF_3.getValue(MdAttributeCharacterInfo.COLUMN_NAME), "test_character_character_ch0");
  }

  /**
   * Test setting of attributes on the date datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testCreateDate()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    TestFixtureFactory.addDateAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testDate");
    AttributeEnumerationIF index = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    Assert.assertEquals(attribute.getStructValue(MdAttributeMomentInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Date Set Test");
    Assert.assertEquals(attribute.getValue(MdAttributeMomentInfo.REQUIRED), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(attribute.getValue(MdAttributeMomentInfo.DEFAULT_VALUE), "2006-02-11");
    Assert.assertEquals(index.dereference()[0].getOid(), IndexTypes.UNIQUE_INDEX.getOid());
  }

  /**
   * Test setting of attributes on the date datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testRequiredForDimension()
  {
    MdDimensionDAO mdDimension = TestFixtureFactory.createMdDimension();
    mdDimension.apply();

    try
    {
      MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
      mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

      Assert.assertEquals(mdAttributeDimensionIF.getValue(MdAttributeDimensionInfo.REQUIRED), MdAttributeBooleanInfo.TRUE);
      Assert.assertEquals(mdAttributeIF.getStructValue(MdAttributeMomentInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Date Set Test");
      Assert.assertEquals(mdAttributeIF.getValue(MdAttributeMomentInfo.REQUIRED), MdAttributeBooleanInfo.TRUE);
      Assert.assertEquals(mdAttributeIF.getValue(MdAttributeMomentInfo.DEFAULT_VALUE), "2006-02-11");
      Assert.assertEquals(index.dereference()[0].getOid(), IndexTypes.UNIQUE_INDEX.getOid());
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
  @Request
  @Test
  public void testCreateDateTime()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    TestFixtureFactory.addDateTimeAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testDateTime");
    AttributeEnumerationIF index = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    Assert.assertEquals(attribute.getStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "dateTime Set Test");
    Assert.assertEquals(index.dereference()[0].getOid(), IndexTypes.NON_UNIQUE_INDEX.getOid());
  }

  /**
   * Test setting of attributes on the decimal datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testCreateDecimal()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    TestFixtureFactory.addDecimalAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testDecimal");

    Assert.assertEquals(attribute.getStructValue(MdAttributeDecimalInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Decimal Set Test");
    Assert.assertEquals(attribute.getValue(MdAttributeDecimalInfo.LENGTH), "10");
    Assert.assertEquals(attribute.getValue(MdAttributeDecimalInfo.DECIMAL), "2");
    Assert.assertEquals(attribute.getValue(MdAttributeDecimalInfo.REJECT_NEGATIVE), MdAttributeBooleanInfo.TRUE);
  }

  /**
   * Test setting of attributes on the double datatype minus any overlapping
   * attributes from the boolean and decimal test
   */
  @Request
  @Test
  public void testCreateDouble()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    TestFixtureFactory.addDoubleAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testDouble");

    Assert.assertEquals(attribute.getStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Double Set Test");
    Assert.assertEquals(attribute.getValue(MdAttributeDoubleInfo.LENGTH), "9");
    Assert.assertEquals(attribute.getValue(MdAttributeDoubleInfo.DECIMAL), "4");
    Assert.assertEquals(attribute.getValue(MdAttributeDoubleInfo.REJECT_ZERO), MdAttributeBooleanInfo.TRUE);
  }

  /**
   * Test setting of attributes on the double datatype minus any overlapping
   * attributes from the boolean and decimal test
   */
  @Request
  @Test
  public void testCreateVirtual()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

    Assert.assertEquals(virtual.getValue(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE), attribute.getOid());
  }

  /**
   * Test setting of attributes on the enumeration datatype minus any
   * overlapping attributes from the boolean test As a side effect does testing
   * on setting instance/instance_value tags
   */
  @Request
  @Test
  public void testCreateEnumeration()
  {
    MdBusinessDAO mdBusinessEnum1 = TestFixtureFactory.createEnumClass1();
    mdBusinessEnum1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    mdBusinessEnum1.apply();
    mdBusiness1.apply();

    TestFixtureFactory.addCharacterAttribute(mdBusinessEnum1).apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusinessEnum1.definesType());
    businessDAO.setValue(EnumerationMasterInfo.NAME, "CO");
    businessDAO.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Colorado");
    businessDAO.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "CO");
    businessDAO.apply();

    MdEnumerationDAO mdEnumeration = TestFixtureFactory.createMdEnumeation1(mdBusinessEnum1);
    mdEnumeration.setValue(MdEnumerationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdEnumeration.apply();

    MdAttributeConcreteDAO addEnumerationAttribute = TestFixtureFactory.addEnumerationAttribute(mdBusiness1, mdEnumeration);
    addEnumerationAttribute.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, businessDAO.getOid());
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

    Assert.assertEquals("Enumeration Set Test", mdAttributeIF.getStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    Assert.assertEquals(MdAttributeBooleanInfo.FALSE, mdAttributeIF.getValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE));
    Assert.assertEquals("CO", BusinessDAO.get(defaultValue).getValue(EnumerationMasterInfo.NAME));
    Assert.assertEquals("Colorado", BusinessDAO.get(defaultValue).getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));

    // Ensure that the enumeration is reference the correct enumeration class
    Assert.assertEquals(mdAttributeIF.getValue(MdAttributeEnumerationInfo.MD_ENUMERATION), mdEnumerationIF.getOid());

    MdBusinessDAOIF testMdBusinessEnum1 = MdBusinessDAO.getMdBusinessDAO(mdBusinessEnum1.definesType());
    List<MdAttributeEnumerationDAOIF> attributes = testMdBusinessEnum1.getAllEnumerationAttributes();

    Assert.assertEquals(1, attributes.size());
    Assert.assertEquals(mdAttributeIF.getOid(), attributes.get(0).getOid());
  }

  /**
   * Test setting of attributes on a blob datatype
   */
  @Request
  @Test
  public void testCreateFile()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    TestFixtureFactory.addFileAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeConcreteDAO attribute = (MdAttributeConcreteDAO) ( mdEntityIF.definesAttribute("testFile") ).getBusinessDAO();
    AttributeEnumerationIF index = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    Assert.assertEquals(attribute.getStructValue(MdAttributeFileInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "File Set Test");
    Assert.assertEquals(attribute.getValue(MdAttributeFileInfo.IMMUTABLE), MdAttributeBooleanInfo.FALSE);
    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, attribute.getValue(MdAttributeFileInfo.REQUIRED));
    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, attribute.getValue(MdAttributeFileInfo.REMOVE));
    Assert.assertEquals(attribute.getStructValue(MetadataInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "File Test");
    Assert.assertEquals(index.dereference()[0].getOid(), IndexTypes.NO_INDEX.getOid());
  }

  /**
   * Test setting of attributes on the character datatype minus any overlapping
   * attributes from the boolean, decimal, and double test
   */
  @Request
  @Test
  public void testCreateFloat()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    TestFixtureFactory.addFloatAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testFloat");

    Assert.assertEquals(attribute.getStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Float Set Test");
    Assert.assertEquals(attribute.getValue(MdAttributeFloatInfo.LENGTH), "10");
    Assert.assertEquals(attribute.getValue(MdAttributeFloatInfo.DECIMAL), "2");
    Assert.assertEquals(attribute.getValue(MdAttributeFloatInfo.REJECT_POSITIVE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(attribute.getValue(MdAttributeFloatInfo.REJECT_NEGATIVE), MdAttributeBooleanInfo.TRUE);
  }

  /**
   * Test setting of attributes on the reference datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testCreateReference()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    mdBusiness1.apply();
    mdBusiness2.apply();

    TestFixtureFactory.addCharacterAttribute(mdBusiness2).apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness2.definesType());
    businessDAO.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "CO");
    businessDAO.apply();

    MdAttributeConcreteDAO addReferenceAttribute = TestFixtureFactory.addReferenceAttribute(mdBusiness1, mdBusiness2);
    addReferenceAttribute.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE, businessDAO.getOid());
    addReferenceAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1, mdBusiness2, businessDAO }));

    TestFixtureFactory.delete(mdBusiness1);
    TestFixtureFactory.delete(mdBusiness2);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testReference");
    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(CLASS2);

    Assert.assertEquals(attribute.getStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Reference Test");

    // Ensure that the reference is referencing the correct class
    Assert.assertEquals(attribute.getValue(MdAttributeReferenceInfo.REF_MD_ENTITY), mdBusinessIF.getOid());
  }

  /**
   * Test setting of attributes on the reference datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testCreateMultiReference()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdBusinessDAO mdTerm = TestFixtureFactory.createMdTerm();
    mdTerm.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTerm.apply();

    TestFixtureFactory.addCharacterAttribute(mdTerm).apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(mdTerm.definesType());
    businessDAO.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "CO");
    businessDAO.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 1");
    businessDAO.apply();

    MdAttributeMultiReferenceDAO mdAttribute = TestFixtureFactory.addMultiReferenceAttribute(mdBusiness1, mdTerm);
    mdAttribute.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE, businessDAO.getOid());
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1, mdTerm, businessDAO }));

    TestFixtureFactory.delete(mdBusiness1);
    TestFixtureFactory.delete(mdTerm);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute(mdAttribute.definesAttribute());

    try
    {
      MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(mdTerm.definesType());

      String actual = attribute.getStructValue(MdAttributeMultiReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE);
      String expected = mdAttribute.getStructValue(MdAttributeMultiReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE);
      Assert.assertEquals(expected, actual);

      // Ensure that the reference is referencing the correct class
      Assert.assertEquals(attribute.getValue(MdAttributeMultiReferenceInfo.REF_MD_ENTITY), mdBusinessIF.getOid());
    }
    finally
    {
      TestFixtureFactory.delete(attribute);
    }
  }

  /**
   * Test setting of attributes on the reference datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testCreateMultiTerm()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdTermDAO mdTerm = TestFixtureFactory.createMdTerm();
    mdTerm.setValue(MdTermInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTerm.apply();

    TestFixtureFactory.addCharacterAttribute(mdTerm).apply();

    MdTermRelationshipDAO mdTermRelationship = TestFixtureFactory.createMdTermRelationship(mdTerm);
    mdTermRelationship.setValue(MdTermRelationshipInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTermRelationship.apply();

    BusinessDAO parent = BusinessDAO.newInstance(mdTerm.definesType());
    parent.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Root");
    parent.setValue(BusinessInfo.KEY, "Root");
    parent.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Root");
    parent.apply();

    BusinessDAO child = BusinessDAO.newInstance(mdTerm.definesType());
    child.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "CO");
    child.setValue(BusinessInfo.KEY, "CO");
    child.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 1");
    child.apply();

    RelationshipDAO relationship = RelationshipDAO.newInstance(parent.getOid(), child.getOid(), mdTermRelationship.definesType());
    relationship.apply();

    MdAttributeMultiTermDAO mdAttribute = TestFixtureFactory.addMultiTermAttribute(mdBusiness1, mdTerm);
    mdAttribute.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE, child.getOid());
    mdAttribute.apply();

    // Add attribute roots
    mdAttribute.addAttributeRoot(parent, true);

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1, mdTerm, mdTermRelationship, parent, child, relationship }));

    TestFixtureFactory.delete(mdTermRelationship);
    TestFixtureFactory.delete(mdAttribute);
    TestFixtureFactory.delete(mdTerm);
    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF mdAttributeMultiTermIF = mdEntityIF.definesAttribute(mdAttribute.definesAttribute());

    try
    {
      MdTermDAOIF mdTermIF = MdTermDAO.getMdTermDAO(mdTerm.definesType());

      String actual = mdAttributeMultiTermIF.getStructValue(MdAttributeMultiTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE);
      String expected = mdAttribute.getStructValue(MdAttributeMultiTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE);
      Assert.assertEquals(expected, actual);

      // Ensure that the reference is referencing the correct class
      Assert.assertEquals(mdAttributeMultiTermIF.getValue(MdAttributeMultiTermInfo.REF_MD_ENTITY), mdTermIF.getOid());

      // Ensure the correct attribute roots were set
      List<RelationshipDAOIF> roots = ( (TermAttributeDAOIF) mdAttributeMultiTermIF ).getAllAttributeRoots();

      Assert.assertTrue(roots.size() > 0);

      RelationshipDAOIF rootIF = roots.get(0);

      Assert.assertEquals(mdAttributeMultiTermIF.getKey(), rootIF.getParent().getKey());
      Assert.assertEquals(parent.getKey(), rootIF.getChild().getKey());
    }
    finally
    {
      TestFixtureFactory.delete(mdAttributeMultiTermIF);
    }
  }

  /**
   * Test setting of attributes on the reference datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testCreateTerm()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdTermDAO mdTerm = TestFixtureFactory.createMdTerm();
    mdTerm.setValue(MdTermInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTerm.apply();

    TestFixtureFactory.addCharacterAttribute(mdTerm).apply();

    MdTermRelationshipDAO mdTermRelationship = TestFixtureFactory.createMdTermRelationship(mdTerm);
    mdTermRelationship.setValue(MdTermRelationshipInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTermRelationship.apply();

    BusinessDAO parent = BusinessDAO.newInstance(mdTerm.definesType());
    parent.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Root");
    parent.setValue(BusinessInfo.KEY, "Root");
    parent.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Root");
    parent.apply();

    BusinessDAO child = BusinessDAO.newInstance(mdTerm.definesType());
    child.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "CO");
    child.setValue(BusinessInfo.KEY, "CO");
    child.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 1");
    child.apply();

    RelationshipDAO relationship = RelationshipDAO.newInstance(parent.getOid(), child.getOid(), mdTermRelationship.definesType());
    relationship.apply();

    MdAttributeTermDAO addTermAttribute = TestFixtureFactory.addTermAttribute(mdBusiness1, mdTerm);
    addTermAttribute.setValue(MdAttributeTermInfo.DEFAULT_VALUE, child.getOid());
    addTermAttribute.apply();

    // Add attribute roots
    addTermAttribute.addAttributeRoot(parent, true);

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1, mdTerm, mdTermRelationship, parent, child, relationship }));

    TestFixtureFactory.delete(mdTermRelationship);
    TestFixtureFactory.delete(mdTerm);
    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdBusinessDAO.getMdElementDAO(mdBusiness1.definesType());
    MdAttributeTermDAOIF mdAttributeTermIF = (MdAttributeTermDAOIF) mdEntityIF.definesAttribute("testTerm");

    MdTermDAOIF mdTermIF = MdTermDAO.getMdTermDAO(mdTerm.definesType());

    Assert.assertEquals(mdAttributeTermIF.getStructValue(MdAttributeTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Term Test");

    // Ensure that the reference is referencing the correct class
    Assert.assertEquals(mdAttributeTermIF.getValue(MdAttributeTermInfo.REF_MD_ENTITY), mdTermIF.getOid());

    // Ensure the correct attribute roots were set
    RelationshipDAOQuery query = new QueryFactory().relationshipDAOQuery(mdTermIF.getTermAttributeRootsRelationshipType());
    query.WHERE(query.parentOid().EQ(mdAttributeTermIF.getOid()));

    OIterator<RelationshipDAOIF> it = query.getIterator();

    try
    {
      Assert.assertTrue(it.hasNext());

      RelationshipDAOIF rootIF = it.next();

      Assert.assertEquals(mdAttributeTermIF.getKey(), rootIF.getParent().getKey());
      Assert.assertEquals(parent.getKey(), rootIF.getChild().getKey());
    }
    finally
    {
      it.close();
    }
  }

  /**
   * Test setting of attributes on the integer datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testCreateInteger()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    TestFixtureFactory.addIntegerAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testInteger");

    Assert.assertEquals(attribute.getStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Integer Set Test");
    Assert.assertEquals(attribute.getValue(MdAttributeIntegerInfo.REJECT_POSITIVE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(attribute.getValue(MdAttributeIntegerInfo.REJECT_ZERO), MdAttributeBooleanInfo.TRUE);
  }

  /**
   * Test setting of attributes on the long datatype minus any overlapping
   * attributes from the boolean test.
   */
  @Request
  @Test
  public void testCreateLong()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    TestFixtureFactory.addLongAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testLong");

    Assert.assertEquals(attribute.getStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Long Set Test");
    Assert.assertEquals(attribute.getValue(MdAttributeLongInfo.REJECT_ZERO), MdAttributeBooleanInfo.TRUE);
  }

  /**
   * Test setting of attributes on the text datatype minus any overlapping
   * attributes from the boolean test.
   */
  @Request
  @Test
  public void testCreateText()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    TestFixtureFactory.addTextAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testText");

    Assert.assertEquals(attribute.getStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Text Set Test");
  }

  /**
   * Test setting of attributes on the text datatype minus any overlapping
   * attributes from the boolean test.
   */
  @Request
  @Test
  public void testCreateClob()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    TestFixtureFactory.addClobAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testClob");

    Assert.assertEquals(attribute.getStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Clob Set Test");
  }

  /**
   * Test setting of attributes on the time datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testCreateTime()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    TestFixtureFactory.addTimeAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testTime");

    Assert.assertEquals(attribute.getStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Time Set Test");
  }

  /**
   * Test setting of attributes on the struct datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testCreateStruct()
  {
    MdStructDAO mdStruct = TestFixtureFactory.createMdStruct1();
    mdStruct.setStructValue(MdStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Struct Set Test");
    mdStruct.setValue(MdStructInfo.CACHE_SIZE, "525");
    mdStruct.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdStruct.apply();

    MdAttributeBooleanDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdStruct);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFAULT_VALUE, MdAttributeBooleanInfo.FALSE);
    mdAttributeBoolean.apply();

    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    TestFixtureFactory.addStructAttribute(mdBusiness1, mdStruct).apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO.setStructValue("testStruct", TestFixConst.ATTRIBUTE_BOOLEAN, MdAttributeBooleanInfo.TRUE);
    businessDAO.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1, mdStruct, businessDAO }));

    String structType = mdStruct.definesType();

    TestFixtureFactory.delete(mdBusiness1);
    TestFixtureFactory.delete(mdStruct);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF mdAttributeIF = mdEntityIF.definesAttribute("testStruct");
    MdStructDAOIF mdStructIF = MdStructDAO.getMdStructDAO(structType);

    Assert.assertEquals(mdAttributeIF.getStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Struct Set Test");
    Assert.assertEquals(mdStructIF.getValue(MdBusinessInfo.CACHE_SIZE), "525");

    // Ensure that the correct class is being referenced
    Assert.assertEquals(mdAttributeIF.getValue(MdAttributeStructInfo.MD_STRUCT), mdStructIF.getOid());

    List<String> ids = EntityDAO.getEntityIdsDB(CLASS);

    Assert.assertEquals(1, ids.size());

    BusinessDAOIF businessDAOIF = BusinessDAO.get(ids.get(0));
    AttributeStruct attribute = (AttributeStruct) businessDAOIF.getAttributeIF("testStruct");

    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, attribute.getValue(TestFixConst.ATTRIBUTE_BOOLEAN));
  }

  /**
   * Test setting of symmetric attribute specific value s
   */
  @Request
  @Test
  public void testCreateSymmetric()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    TestFixtureFactory.addSymmetricAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeSymmetricDAOIF attribute = (MdAttributeSymmetricDAOIF) mdEntityIF.definesAttribute("testSymmetric");

    // Ensure that the symmetric encryption method is set
    AttributeEnumerationIF method = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeSymmetricInfo.SYMMETRIC_METHOD);
    Assert.assertEquals(SymmetricMethods.DES.getOid(), method.dereference()[0].getOid());
    Assert.assertEquals("56", attribute.getValue(MdAttributeSymmetricInfo.SECRET_KEY_SIZE));
  }

  /**
   * Test setting of mdHash attribute specific values
   */
  @Request
  @Test
  public void testCreateHash()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    TestFixtureFactory.addHashAttribute(mdBusiness1).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeHashDAOIF attribute = (MdAttributeHashDAOIF) mdEntityIF.definesAttribute("testHash");

    // Ensure that the hash encryption method is set
    AttributeEnumerationIF method = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeHashInfo.HASH_METHOD);
    Assert.assertEquals(HashMethods.MD5.getOid(), method.dereference()[0].getOid());
  }

  /**
   * Test the setting of cache size on both class and struct.
   */
  @Request
  @Test
  public void testCacheAlgorithmSet()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getOid());
    mdBusiness1.setValue(MdBusinessInfo.CACHE_SIZE, "50");
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness2.apply();

    MdStructDAO mdStruct = TestFixtureFactory.createMdStruct1();
    mdStruct.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getOid());
    mdStruct.setValue(MdStructInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdStruct.apply();

    MdRelationshipDAO mdRelationship1 = TestFixtureFactory.createMdRelationship1(mdBusiness1, mdBusiness2);
    mdRelationship1.setValue(MdRelationshipInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
    mdRelationship1.setValue(MdRelationshipInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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
    Assert.assertEquals(EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getOid(), attribute.dereference()[0].getOid());

    // Ensure that the correct cache size is set on structs
    attribute = (AttributeEnumerationIF) mdStruct2.getAttributeIF(MdElementInfo.CACHE_ALGORITHM);
    Assert.assertEquals(EntityCacheMaster.CACHE_EVERYTHING.getOid(), attribute.dereference()[0].getOid());

    // Ensure that the correct cache algorithm is set on a relationship
    attribute = (AttributeEnumerationIF) mdRelationship.getAttributeIF(MdElementInfo.CACHE_ALGORITHM);
    Assert.assertEquals(EntityCacheMaster.CACHE_NOTHING.getOid(), attribute.dereference()[0].getOid());
  }

  /**
   * Test setting of attributes of and on the class datatype
   */
  @Request
  @Test
  public void testCreateMdBusiness()
  {
    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdBusiness1.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdBusiness1.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdBusiness1.setValue(MdBusinessInfo.CACHE_SIZE, "50");
    mdBusiness1.setValue(MdBusinessInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    TestFixtureFactory.addBooleanAttribute(mdBusiness1).apply();

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdBusiness1.getOid());
    mdBusiness2.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness2.apply();

    mdBusiness1.setValue(MdBusinessInfo.STUB_SOURCE, TestFixtureFactory.getMdBusinessStub());

    MdBusinessDAO updateBusiness = MdBusinessDAO.get(mdBusiness1.getOid()).getBusinessDAO();
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

    MdAttributeDAOIF attribute = mdBusiness1IF.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN);

    Assert.assertEquals(mdBusiness1IF.getStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdBusiness Set Test");
    Assert.assertEquals(mdBusiness1IF.getStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdBusiness Attributes Test");
    Assert.assertEquals(mdBusiness1IF.getValue(MdBusinessInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdBusiness1IF.getValue(MdBusinessInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdBusiness1IF.getValue(MdBusinessInfo.CACHE_SIZE), "50");
    Assert.assertEquals(MdAttributeBooleanInfo.FALSE, mdBusiness1IF.getValue(MdBusinessInfo.PUBLISH));
    Assert.assertEquals(TestFixtureFactory.getMdBusinessStub().trim(), mdBusiness1IF.getValue(MdBusinessInfo.STUB_SOURCE).trim());

    // Change to false when casscading delete is implemented
    Assert.assertEquals(mdBusiness1IF.getValue(MetadataInfo.REMOVE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdBusiness2IF.getValue(MdElementInfo.EXTENDABLE), MdAttributeBooleanInfo.FALSE);

    // Ensure inheritance is linking to the correct super class
    Assert.assertEquals(mdBusiness2IF.getValue(MdBusinessInfo.SUPER_MD_BUSINESS), mdBusiness1IF.getOid());

    // Ensure the attributes are linked to the correct MdBusiness object
    Assert.assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdBusiness1IF.getOid());

    mdBusiness2 = mdBusiness2IF.getBusinessDAO();
    mdBusiness2.setValue(MetadataInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdBusiness2.apply();
  }

  /**
   * Test setting of attributes of and on the struct datatype,
   */
  @Request
  @Test
  public void testCreateMdStruct()
  {
    SAXImporter.runImport(new File(STANDALONE_STRUCT_SET));

    MdStructDAO mdStruct = ( (MdStructDAO) MdStructDAO.getMdStructDAO(CLASS) ).getBusinessDAO();
    MdAttributeConcreteDAOIF attribute = mdStruct.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN);

    Assert.assertEquals("Struct Set Test", mdStruct.getStructValue(MdStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    Assert.assertEquals("Set Struct Attributes Test", mdStruct.getStructValue(MdAttributeStructInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, mdStruct.getValue(MdAttributeStructInfo.REMOVE));

    // Ensure the attributes are linked to the struct
    Assert.assertEquals(mdStruct.getOid(), attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS));
  }

  /**
   * Test setting of attributes of and on the relationship datatype
   */
  @Request
  @Test
  public void testCreateMdRelationship()
  {
    try
    {
      SAXImporter.runImport(new File(RELATIONSHIP_SET));

      MdRelationshipDAO mdRelationship = MdRelationshipDAO.getMdRelationshipDAO(RELATIONSHIP).getBusinessDAO();
      MdBusinessDAOIF mdBusiness1 = MdBusinessDAO.getMdBusinessDAO(CLASS);
      MdBusinessDAOIF mdBusiness2 = MdBusinessDAO.getMdBusinessDAO(CLASS2);
      MdAttributeConcreteDAOIF attribute = mdRelationship.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN);

      Assert.assertEquals("Relationship Set Test", mdRelationship.getStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
      Assert.assertEquals("Relationship Test", mdRelationship.getStructValue(MdRelationshipInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
      Assert.assertEquals(MdAttributeBooleanInfo.FALSE, mdRelationship.getValue(MdRelationshipInfo.REMOVE));
      Assert.assertEquals(MdAttributeBooleanInfo.TRUE, mdRelationship.getValue(MdRelationshipInfo.EXTENDABLE));
      Assert.assertEquals(MdAttributeBooleanInfo.TRUE, mdRelationship.getValue(MdRelationshipInfo.ABSTRACT));

      // Ensure the parent attributes are correctly set
      Assert.assertEquals(mdRelationship.getValue(MdRelationshipInfo.PARENT_MD_BUSINESS), mdBusiness1.getOid());
      Assert.assertEquals(mdRelationship.getValue(MdRelationshipInfo.PARENT_CARDINALITY), "1");
      Assert.assertEquals(mdRelationship.getStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Parent Set Test");

      // Ensure the child attributes are correctly set
      Assert.assertEquals(mdRelationship.getValue(MdRelationshipInfo.CHILD_MD_BUSINESS), mdBusiness2.getOid());
      Assert.assertEquals(mdRelationship.getValue(MdRelationshipInfo.CHILD_CARDINALITY), "*");
      Assert.assertEquals(mdRelationship.getStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Child Set Test");

      // Ensure the attributes are linked to the relationship
      Assert.assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdRelationship.getOid());

      mdRelationship.setValue(MetadataInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdRelationship.apply();

      // Test the sub relationship
      MdRelationshipDAO mdRelationship2 = MdRelationshipDAO.getMdRelationshipDAO(RELATIONSHIP2).getBusinessDAO();
      Assert.assertEquals(mdRelationship.getOid(), mdRelationship2.getValue(MdRelationshipInfo.SUPER_MD_RELATIONSHIP));
      Assert.assertEquals("2", mdRelationship2.getValue(MdRelationshipInfo.CHILD_CARDINALITY));
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
  @Request
  @Test
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
    mdProblem2.setValue(MdProblemInfo.SUPER_MD_PROBLEM, mdProblem1.getOid());
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

    MdAttributeDAOIF attribute = mdProblem1IF.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN);

    Assert.assertEquals(mdProblem1IF.getStructValue(MdProblemInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdProblem Set Test");
    Assert.assertEquals(mdProblem1IF.getStructValue(MdProblemInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdProblem Attributes Test");
    Assert.assertEquals(mdProblem1IF.getValue(MdProblemInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdProblem1IF.getValue(MdProblemInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(MdAttributeBooleanInfo.FALSE, mdProblem1IF.getValue(MdProblemInfo.PUBLISH));

    // Change to false when casscading delete is implemented
    Assert.assertEquals(mdProblem1IF.getValue(MdProblemInfo.REMOVE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdProblem2IF.getValue(MdProblemInfo.EXTENDABLE), MdAttributeBooleanInfo.FALSE);

    // Ensure inheritance is linking to the correct super class
    Assert.assertEquals(mdProblem2IF.getValue(MdProblemInfo.SUPER_MD_PROBLEM), mdProblem1IF.getOid());

    // Ensure the attributes are linked to the correct MdBusiness object
    Assert.assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdProblem1IF.getOid());
  }

  /**
   * Test setting of attributes of and on the class datatype
   */
  @Request
  @Test
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
    mdInformation2.setValue(MdInformationInfo.SUPER_MD_INFORMATION, mdInformation1.getOid());
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

    MdAttributeDAOIF attribute = mdInformation1IF.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN);

    Assert.assertEquals(mdInformation1IF.getStructValue(MdInformationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdInformation Set Test");
    Assert.assertEquals(mdInformation1IF.getStructValue(MdInformationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdInformation Attributes Test");
    Assert.assertEquals(mdInformation1IF.getValue(MdInformationInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdInformation1IF.getValue(MdInformationInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(MdAttributeBooleanInfo.FALSE, mdInformation1IF.getValue(MdInformationInfo.PUBLISH));

    // Change to false when casscading delete is implemented
    Assert.assertEquals(mdInformation1IF.getValue(MdInformationInfo.REMOVE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdInformation2IF.getValue(MdInformationInfo.EXTENDABLE), MdAttributeBooleanInfo.FALSE);

    // Ensure inheritance is linking to the correct super class
    Assert.assertEquals(mdInformation2IF.getValue(MdInformationInfo.SUPER_MD_INFORMATION), mdInformation1IF.getOid());

    // Ensure the attributes are linked to the correct MdBusiness object
    Assert.assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdInformation1IF.getOid());
  }

  /**
   * Test setting of attributes of and on the class datatype
   */
  @Request
  @Test
  public void testCreateMdWarning()
  {
    // Create test MdBusiness
    MdWarningDAO mdWarning1 = TestFixtureFactory.createMdWarning();
    mdWarning1.setValue(MdWarningInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdWarning1.setValue(MdWarningInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdWarning1.setValue(MdWarningInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdWarning1.setValue(MdWarningInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdWarning1.apply();

    TestFixtureFactory.addBooleanAttribute(mdWarning1).apply();

    MdWarningDAO mdWarning2 = TestFixtureFactory.createMdWarning();
    mdWarning2.setValue(MdWarningInfo.NAME, "Warning2");
    mdWarning2.setValue(MdWarningInfo.SUPER_MD_WARNING, mdWarning1.getOid());
    mdWarning2.setValue(MdWarningInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdWarning2.apply();

    // Export the test entities
    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdWarning2, mdWarning1 }));

    // Delete the test entites
    TestFixtureFactory.delete(mdWarning1);
    TestFixtureFactory.delete(mdWarning2);

    // Import the test entites
    SAXImporter.runImport(new File(tempXMLFile));

    MdWarningDAOIF mdWarning1IF = MdWarningDAO.getMdWarning(WARNING);
    MdWarningDAOIF mdWarning2IF = MdWarningDAO.getMdWarning(WARNING2);

    MdAttributeDAOIF attribute = mdWarning1IF.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN);

    Assert.assertEquals(mdWarning1IF.getStructValue(MdWarningInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdWarning Set Test");
    Assert.assertEquals(mdWarning1IF.getStructValue(MdWarningInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdWarning Attributes Test");
    Assert.assertEquals(mdWarning1IF.getValue(MdWarningInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdWarning1IF.getValue(MdWarningInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(MdAttributeBooleanInfo.FALSE, mdWarning1IF.getValue(MdWarningInfo.PUBLISH));

    // Change to false when casscading delete is implemented
    Assert.assertEquals(mdWarning1IF.getValue(MdWarningInfo.REMOVE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdWarning2IF.getValue(MdWarningInfo.EXTENDABLE), MdAttributeBooleanInfo.FALSE);

    // Ensure inheritance is linking to the correct super class
    Assert.assertEquals(mdWarning2IF.getValue(MdWarningInfo.SUPER_MD_WARNING), mdWarning1IF.getOid());

    // Ensure the attributes are linked to the correct MdBusiness object
    Assert.assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdWarning1IF.getOid());
  }

  /**
   * Test setting of attributes of and on the class datatype
   */
  @Request
  @Test
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
    mdException2.setValue(MdExceptionInfo.SUPER_MD_EXCEPTION, mdException1.getOid());
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

    MdAttributeDAOIF attribute = mdException1IF.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN);

    Assert.assertEquals(mdException1IF.getStructValue(MdExceptionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdException Set Test");
    Assert.assertEquals(mdException1IF.getStructValue(MdExceptionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdException Attributes Test");
    Assert.assertEquals(mdException1IF.getValue(MdExceptionInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdException1IF.getValue(MdExceptionInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(MdAttributeBooleanInfo.FALSE, mdException1IF.getValue(MdBusinessInfo.PUBLISH));

    // Change to false when cascading delete is implemented
    Assert.assertEquals(mdException1IF.getValue(MdExceptionInfo.REMOVE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdException2IF.getValue(MdExceptionInfo.EXTENDABLE), MdAttributeBooleanInfo.FALSE);

    // Ensure inheritance is linking to the correct super class
    Assert.assertEquals(mdException2IF.getValue(MdExceptionInfo.SUPER_MD_EXCEPTION), mdException1IF.getOid());

    // Ensure the attributes are linked to the correct MdBusiness object
    Assert.assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdException1IF.getOid());
  }

  @Request
  @Test
  public void testCreateMdView()
  {
    // Create test MdView
    MdViewDAO mdView1 = TestFixtureFactory.createMdView1();
    mdView1.setValue(MdViewInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdView1.setValue(MdViewInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdView1.setValue(MdViewInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdView1.setValue(MdViewInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdView1.setValue(MdViewInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdView1.apply();

    TestFixtureFactory.addBooleanAttribute(mdView1).apply();

    MdMethodDAO mdMethod = MdMethodDAO.newInstance();
    mdMethod.setValue(MdMethodInfo.NAME, "checkin");
    mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdView1.getOid());
    mdMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkin");
    mdMethod.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkin");
    mdMethod.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    mdMethod.apply();

    MdViewDAO mdView2 = TestFixtureFactory.createMdView2();
    mdView2.setValue(MdViewInfo.SUPER_MD_VIEW, mdView1.getOid());
    mdView2.setValue(MdViewInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdView2.setValue(MdViewInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

    MdAttributeDAOIF attribute = mdView1IF.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN);

    Assert.assertEquals(mdView1IF.getStructValue(MdViewInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdException Set Test");
    Assert.assertEquals(mdView1IF.getStructValue(MdViewInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdException Attributes Test");
    Assert.assertEquals(mdView1IF.getValue(MdViewInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdView1IF.getValue(MdViewInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(MdAttributeBooleanInfo.FALSE, mdView1IF.getValue(MdBusinessInfo.PUBLISH));

    // Change to false when cascading delete is implemented
    Assert.assertEquals(mdView1IF.getValue(MdViewInfo.REMOVE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdView2IF.getValue(MdViewInfo.EXTENDABLE), MdAttributeBooleanInfo.FALSE);

    // Ensure inheritance is linking to the correct super class
    Assert.assertEquals(mdView2IF.getValue(MdViewInfo.SUPER_MD_VIEW), mdView1IF.getOid());

    // Ensure the attributes are linked to the correct MdBusiness object
    Assert.assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdView1IF.getOid());

    List<MdMethodDAOIF> mdMethods = mdView1IF.getMdMethods();

    Assert.assertEquals(1, mdMethods.size());

    Assert.assertEquals(true, mdMethods.get(0).isStatic());
    Assert.assertEquals("checkin", mdMethods.get(0).getName());
    Assert.assertTrue(mdMethods.get(0).getReturnType().isVoid());
    Assert.assertEquals("checkin", mdMethods.get(0).getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals("checkin", mdMethods.get(0).getDescription(CommonProperties.getDefaultLocale()));
  }

  @Request
  @Test
  public void testCreateMdUtil()
  {
    // Create test MdView
    MdUtilDAO mdUtil1 = TestFixtureFactory.createMdUtil1();
    mdUtil1.setValue(MdUtilInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdUtil1.setValue(MdUtilInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdUtil1.setValue(MdUtilInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdUtil1.setValue(MdUtilInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdUtil1.setValue(MdUtilInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdUtil1.apply();

    TestFixtureFactory.addBooleanAttribute(mdUtil1).apply();

    MdMethodDAO mdMethod = MdMethodDAO.newInstance();
    mdMethod.setValue(MdMethodInfo.NAME, "checkin");
    mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdUtil1.getOid());
    mdMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkin");
    mdMethod.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkin");
    mdMethod.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    mdMethod.apply();

    MdUtilDAO mdUtil2 = TestFixtureFactory.createMdUtil2();
    mdUtil2.setValue(MdUtilInfo.SUPER_MD_UTIL, mdUtil1.getOid());
    mdUtil2.setValue(MdUtilInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdUtil2.setValue(MdUtilInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

    MdAttributeDAOIF attribute = mdUtil1IF.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN);

    Assert.assertEquals(mdUtil1IF.getStructValue(MdUtilInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdException Set Test");
    Assert.assertEquals(mdUtil1IF.getStructValue(MdUtilInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdException Attributes Test");
    Assert.assertEquals(mdUtil1IF.getValue(MdUtilInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdUtil1IF.getValue(MdUtilInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(MdAttributeBooleanInfo.FALSE, mdUtil1IF.getValue(MdBusinessInfo.PUBLISH));

    // Change to false when casscading delete is implemented
    Assert.assertEquals(mdUtil1IF.getValue(MdUtilInfo.REMOVE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdUtil2IF.getValue(MdUtilInfo.EXTENDABLE), MdAttributeBooleanInfo.FALSE);

    // Ensure inheritance is linking to the correct super class
    Assert.assertEquals(mdUtil2IF.getValue(MdUtilInfo.SUPER_MD_UTIL), mdUtil1IF.getOid());

    // Ensure the attributes are linked to the correct MdBusiness object
    Assert.assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdUtil1IF.getOid());

    List<MdMethodDAOIF> mdMethods = mdUtil1IF.getMdMethods();

    Assert.assertEquals(1, mdMethods.size());

    Assert.assertEquals(true, mdMethods.get(0).isStatic());
    Assert.assertEquals("checkin", mdMethods.get(0).getName());
    Assert.assertTrue(mdMethods.get(0).getReturnType().isVoid());
    Assert.assertEquals("checkin", mdMethods.get(0).getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals("checkin", mdMethods.get(0).getDescription(CommonProperties.getDefaultLocale()));
  }

  // @Request @Test public void testCreateMdWebForm()
  // {
  // MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
  // mdBusiness2.apply();
  //
  // MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
  // mdBusiness.apply();
  //
  // MdAttributeCharacterDAO mdAttributeCharacter =
  // TestFixtureFactory.addCharacterAttribute(mdBusiness);
  // mdAttributeCharacter.apply();
  //
  // MdAttributeBooleanDAO mdAttributeBoolean =
  // TestFixtureFactory.addBooleanAttribute(mdBusiness);
  // mdAttributeBoolean.apply();
  //
  // MdAttributeIntegerDAO mdAttributeInteger =
  // TestFixtureFactory.addIntegerAttribute(mdBusiness);
  // mdAttributeInteger.apply();
  //
  // MdAttributeLongDAO mdAttributeLong =
  // TestFixtureFactory.addLongAttribute(mdBusiness);
  // mdAttributeLong.apply();
  //
  // MdAttributeDoubleDAO mdAttributeDouble =
  // TestFixtureFactory.addDoubleAttribute(mdBusiness);
  // mdAttributeDouble.apply();
  //
  // MdAttributeFloatDAO mdAttributeFloat =
  // TestFixtureFactory.addFloatAttribute(mdBusiness);
  // mdAttributeFloat.apply();
  //
  // MdAttributeReferenceDAO mdAttributeReference =
  // TestFixtureFactory.addReferenceAttribute(mdBusiness, mdBusiness2);
  // mdAttributeReference.apply();
  //
  // MdAttributeReferenceDAO mdAttributeTerm =
  // TestFixtureFactory.addReferenceAttribute(mdBusiness, mdBusiness2,
  // "testTerm");
  // mdAttributeTerm.apply();
  //
  // MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
  // mdWebForm.apply();
  //
  // MdWebCharacterDAO mdWebCharacter =
  // TestFixtureFactory.addCharacterField(mdWebForm, mdAttributeCharacter);
  // mdWebCharacter.apply();
  //
  // MdWebBooleanDAO mdWebBoolean =
  // TestFixtureFactory.addBooleanField(mdWebForm, mdAttributeBoolean);
  // mdWebBoolean.apply();
  //
  // MdWebIntegerDAO mdWebInteger =
  // TestFixtureFactory.addIntegerField(mdWebForm, mdAttributeInteger);
  // mdWebInteger.apply();
  //
  // MdWebLongDAO mdWebLong = TestFixtureFactory.addLongField(mdWebForm,
  // mdAttributeLong);
  // mdWebLong.apply();
  //
  // MdWebFloatDAO mdWebFloat = TestFixtureFactory.addFloatField(mdWebForm,
  // mdAttributeFloat);
  // mdWebFloat.apply();
  //
  // MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdWebForm,
  // mdAttributeDouble);
  // mdWebDouble.apply();
  //
  // MdWebReferenceDAO mdWebReference =
  // TestFixtureFactory.addReferenceField(mdWebForm, mdAttributeReference);
  // mdWebReference.apply();
  //
  // MdWebSingleTermDAO mdWebSingleTerm =
  // TestFixtureFactory.addSingleTermField(mdWebForm, mdAttributeTerm);
  // mdWebSingleTerm.apply();
  //
  // SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new
  // ComponentIF[] { mdBusiness, mdWebForm }));
  //
  // TestFixtureFactory.delete(mdWebForm);
  // TestFixtureFactory.delete(mdBusiness);
  //
  // SAXImporter.runImport(new File(tempXMLFile));
  //
  // MdWebFormDAOIF test = (MdWebFormDAOIF)
  // MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());
  //
  // Assert.assertEquals(mdWebForm.getFormName(), test.getFormName());
  // Assert.assertEquals(mdBusiness.definesType(),
  // test.getFormMdClass().definesType());
  // Assert.assertEquals(mdWebForm.getDescription(CommonProperties.getDefaultLocale()),
  // test.getDescription(CommonProperties.getDefaultLocale()));
  // Assert.assertEquals(mdWebForm.getDisplayLabel(CommonProperties.getDefaultLocale()),
  // test.getDisplayLabel(CommonProperties.getDefaultLocale()));
  //
  // List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();
  //
  // Assert.assertEquals(8, fields.size());
  //
  // MdWebCharacterDAO testField = (MdWebCharacterDAO) fields.get(0);
  //
  // Assert.assertEquals(mdWebCharacter.getFieldName(),
  // testField.getFieldName());
  // Assert.assertEquals(mdWebCharacter.getFieldOrder(),
  // testField.getFieldOrder());
  // Assert.assertEquals(mdWebCharacter.getDescription(CommonProperties.getDefaultLocale()),
  // testField.getDescription(CommonProperties.getDefaultLocale()));
  // Assert.assertEquals(mdWebCharacter.getDisplayLabel(CommonProperties.getDefaultLocale()),
  // testField.getDisplayLabel(CommonProperties.getDefaultLocale()));
  // Assert.assertEquals(mdAttributeCharacter.definesAttribute(),
  // testField.getDefiningMdAttribute().definesAttribute());
  // }

  @Request
  @Test
  public void testCreateMdWebDate()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeDateDAO mdAttributeDate = TestFixtureFactory.addDateAttribute(mdBusiness);
    mdAttributeDate.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebDateDAO mdWebDate = TestFixtureFactory.addDateField(mdWebForm, mdAttributeDate);
    mdWebDate.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    Assert.assertEquals(mdWebForm.getFormName(), test.getFormName());
    Assert.assertEquals(mdBusiness.definesType(), test.getFormMdClass().definesType());
    Assert.assertEquals(mdWebForm.getDescription(CommonProperties.getDefaultLocale()), test.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebForm.getDisplayLabel(CommonProperties.getDefaultLocale()), test.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(1, fields.size());

    MdWebDateDAO testField = (MdWebDateDAO) fields.get(0);

    Assert.assertEquals(mdWebDate.getFieldName(), testField.getFieldName());
    Assert.assertEquals(mdWebDate.getFieldOrder(), testField.getFieldOrder());
    Assert.assertEquals(mdWebDate.getDescription(CommonProperties.getDefaultLocale()), testField.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebDate.getDisplayLabel(CommonProperties.getDefaultLocale()), testField.getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdAttributeDate.definesAttribute(), testField.getDefiningMdAttribute().definesAttribute());
    Assert.assertEquals(mdWebDate.getValue(MdWebDateInfo.AFTER_TODAY_EXCLUSIVE), testField.getValue(MdWebDateInfo.AFTER_TODAY_EXCLUSIVE));
    Assert.assertEquals(mdWebDate.getValue(MdWebDateInfo.AFTER_TODAY_INCLUSIVE), testField.getValue(MdWebDateInfo.AFTER_TODAY_INCLUSIVE));
    Assert.assertEquals(mdWebDate.getValue(MdWebDateInfo.BEFORE_TODAY_EXCLUSIVE), testField.getValue(MdWebDateInfo.BEFORE_TODAY_EXCLUSIVE));
    Assert.assertEquals(mdWebDate.getValue(MdWebDateInfo.BEFORE_TODAY_INCLUSIVE), testField.getValue(MdWebDateInfo.BEFORE_TODAY_INCLUSIVE));
    Assert.assertEquals(mdWebDate.getValue(MdWebDateInfo.START_DATE), testField.getValue(MdWebDateInfo.START_DATE));
    Assert.assertEquals(mdWebDate.getValue(MdWebDateInfo.END_DATE), testField.getValue(MdWebDateInfo.END_DATE));
  }

  @Request
  @Test
  public void testCreateMdWebText()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeTextDAO mdAttributeText = TestFixtureFactory.addTextAttribute(mdBusiness);
    mdAttributeText.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebTextDAO mdWebText = TestFixtureFactory.addTextField(mdWebForm, mdAttributeText);
    mdWebText.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    Assert.assertEquals(mdWebForm.getFormName(), test.getFormName());
    Assert.assertEquals(mdBusiness.definesType(), test.getFormMdClass().definesType());
    Assert.assertEquals(mdWebForm.getDescription(CommonProperties.getDefaultLocale()), test.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebForm.getDisplayLabel(CommonProperties.getDefaultLocale()), test.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(1, fields.size());

    MdWebTextDAO testField = (MdWebTextDAO) fields.get(0);

    Assert.assertEquals(mdWebText.getFieldName(), testField.getFieldName());
    Assert.assertEquals(mdWebText.getFieldOrder(), testField.getFieldOrder());
    Assert.assertEquals(mdWebText.getDescription(CommonProperties.getDefaultLocale()), testField.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebText.getDisplayLabel(CommonProperties.getDefaultLocale()), testField.getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdAttributeText.definesAttribute(), testField.getDefiningMdAttribute().definesAttribute());
    Assert.assertEquals(mdWebText.getValue(MdWebTextInfo.HEIGHT), testField.getValue(MdWebTextInfo.HEIGHT));
    Assert.assertEquals(mdWebText.getValue(MdWebTextInfo.WIDTH), testField.getValue(MdWebTextInfo.WIDTH));
  }

  @Request
  @Test
  public void testCreateMdWebInteger()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeIntegerDAO mdAttributeInteger = TestFixtureFactory.addIntegerAttribute(mdBusiness);
    mdAttributeInteger.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdWebForm, mdAttributeInteger);
    mdWebInteger.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    Assert.assertEquals(mdWebForm.getFormName(), test.getFormName());
    Assert.assertEquals(mdBusiness.definesType(), test.getFormMdClass().definesType());
    Assert.assertEquals(mdWebForm.getDescription(CommonProperties.getDefaultLocale()), test.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebForm.getDisplayLabel(CommonProperties.getDefaultLocale()), test.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(1, fields.size());

    MdWebIntegerDAO testField = (MdWebIntegerDAO) fields.get(0);

    Assert.assertEquals(mdWebInteger.getFieldName(), testField.getFieldName());
    Assert.assertEquals(mdWebInteger.getFieldOrder(), testField.getFieldOrder());
    Assert.assertEquals(mdWebInteger.getDescription(CommonProperties.getDefaultLocale()), testField.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebInteger.getDisplayLabel(CommonProperties.getDefaultLocale()), testField.getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdAttributeInteger.definesAttribute(), testField.getDefiningMdAttribute().definesAttribute());
    Assert.assertEquals(mdWebInteger.getValue(MdWebIntegerInfo.STARTRANGE), testField.getValue(MdWebIntegerInfo.STARTRANGE));
    Assert.assertEquals(mdWebInteger.getValue(MdWebIntegerInfo.ENDRANGE), testField.getValue(MdWebIntegerInfo.ENDRANGE));
  }

  @Request
  @Test
  public void testCreateMdWebLong()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeLongDAO mdAttributeLong = TestFixtureFactory.addLongAttribute(mdBusiness);
    mdAttributeLong.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebLongDAO mdWebLong = TestFixtureFactory.addLongField(mdWebForm, mdAttributeLong);
    mdWebLong.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    Assert.assertEquals(mdWebForm.getFormName(), test.getFormName());
    Assert.assertEquals(mdBusiness.definesType(), test.getFormMdClass().definesType());
    Assert.assertEquals(mdWebForm.getDescription(CommonProperties.getDefaultLocale()), test.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebForm.getDisplayLabel(CommonProperties.getDefaultLocale()), test.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(1, fields.size());

    MdWebLongDAO testField = (MdWebLongDAO) fields.get(0);

    Assert.assertEquals(mdWebLong.getFieldName(), testField.getFieldName());
    Assert.assertEquals(mdWebLong.getFieldOrder(), testField.getFieldOrder());
    Assert.assertEquals(mdWebLong.getDescription(CommonProperties.getDefaultLocale()), testField.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebLong.getDisplayLabel(CommonProperties.getDefaultLocale()), testField.getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdAttributeLong.definesAttribute(), testField.getDefiningMdAttribute().definesAttribute());
    Assert.assertEquals(mdWebLong.getValue(MdWebLongInfo.STARTRANGE), testField.getValue(MdWebLongInfo.STARTRANGE));
    Assert.assertEquals(mdWebLong.getValue(MdWebLongInfo.ENDRANGE), testField.getValue(MdWebLongInfo.ENDRANGE));
  }

  @Request
  @Test
  public void testCreateMdWebDouble()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeDoubleDAO mdAttributeDouble = TestFixtureFactory.addDoubleAttribute(mdBusiness);
    mdAttributeDouble.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdWebForm, mdAttributeDouble);
    mdWebDouble.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    Assert.assertEquals(mdWebForm.getFormName(), test.getFormName());
    Assert.assertEquals(mdBusiness.definesType(), test.getFormMdClass().definesType());
    Assert.assertEquals(mdWebForm.getDescription(CommonProperties.getDefaultLocale()), test.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebForm.getDisplayLabel(CommonProperties.getDefaultLocale()), test.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(1, fields.size());

    MdWebDoubleDAO testField = (MdWebDoubleDAO) fields.get(0);

    Assert.assertEquals(mdWebDouble.getFieldName(), testField.getFieldName());
    Assert.assertEquals(mdWebDouble.getFieldOrder(), testField.getFieldOrder());
    Assert.assertEquals(mdWebDouble.getDescription(CommonProperties.getDefaultLocale()), testField.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebDouble.getDisplayLabel(CommonProperties.getDefaultLocale()), testField.getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdAttributeDouble.definesAttribute(), testField.getDefiningMdAttribute().definesAttribute());
    Assert.assertEquals(mdWebDouble.getValue(MdWebDoubleInfo.STARTRANGE), testField.getValue(MdWebDoubleInfo.STARTRANGE));
    Assert.assertEquals(mdWebDouble.getValue(MdWebDoubleInfo.ENDRANGE), testField.getValue(MdWebDoubleInfo.ENDRANGE));
    Assert.assertEquals(mdWebDouble.getValue(MdWebDoubleInfo.DECPRECISION), testField.getValue(MdWebDoubleInfo.DECPRECISION));
    Assert.assertEquals(mdWebDouble.getValue(MdWebDoubleInfo.DECSCALE), testField.getValue(MdWebDoubleInfo.DECSCALE));
  }

  @Request
  @Test
  public void testCreateMdWebFloat()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeFloatDAO mdAttributeFloat = TestFixtureFactory.addFloatAttribute(mdBusiness);
    mdAttributeFloat.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebFloatDAO mdWebFloat = TestFixtureFactory.addFloatField(mdWebForm, mdAttributeFloat);
    mdWebFloat.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    Assert.assertEquals(mdWebForm.getFormName(), test.getFormName());
    Assert.assertEquals(mdBusiness.definesType(), test.getFormMdClass().definesType());
    Assert.assertEquals(mdWebForm.getDescription(CommonProperties.getDefaultLocale()), test.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebForm.getDisplayLabel(CommonProperties.getDefaultLocale()), test.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(1, fields.size());

    MdWebFloatDAO testField = (MdWebFloatDAO) fields.get(0);

    Assert.assertEquals(mdWebFloat.getFieldName(), testField.getFieldName());
    Assert.assertEquals(mdWebFloat.getFieldOrder(), testField.getFieldOrder());
    Assert.assertEquals(mdWebFloat.getDescription(CommonProperties.getDefaultLocale()), testField.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebFloat.getDisplayLabel(CommonProperties.getDefaultLocale()), testField.getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdAttributeFloat.definesAttribute(), testField.getDefiningMdAttribute().definesAttribute());
    Assert.assertEquals(mdWebFloat.getValue(MdWebFloatInfo.STARTRANGE), testField.getValue(MdWebFloatInfo.STARTRANGE));
    Assert.assertEquals(mdWebFloat.getValue(MdWebFloatInfo.ENDRANGE), testField.getValue(MdWebFloatInfo.ENDRANGE));
    Assert.assertEquals(mdWebFloat.getValue(MdWebFloatInfo.DECPRECISION), testField.getValue(MdWebFloatInfo.DECPRECISION));
    Assert.assertEquals(mdWebFloat.getValue(MdWebFloatInfo.DECSCALE), testField.getValue(MdWebFloatInfo.DECSCALE));
  }

  @Request
  @Test
  public void testCreateMdWebDecimal()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeDecimalDAO mdAttributeDecimal = TestFixtureFactory.addDecimalAttribute(mdBusiness);
    mdAttributeDecimal.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebDecimalDAO mdWebDecimal = TestFixtureFactory.addDecimalField(mdWebForm, mdAttributeDecimal);
    mdWebDecimal.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    Assert.assertEquals(mdWebForm.getFormName(), test.getFormName());
    Assert.assertEquals(mdBusiness.definesType(), test.getFormMdClass().definesType());
    Assert.assertEquals(mdWebForm.getDescription(CommonProperties.getDefaultLocale()), test.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebForm.getDisplayLabel(CommonProperties.getDefaultLocale()), test.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(1, fields.size());

    MdWebDecimalDAO testField = (MdWebDecimalDAO) fields.get(0);

    Assert.assertEquals(mdWebDecimal.getFieldName(), testField.getFieldName());
    Assert.assertEquals(mdWebDecimal.getFieldOrder(), testField.getFieldOrder());
    Assert.assertEquals(mdWebDecimal.getDescription(CommonProperties.getDefaultLocale()), testField.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebDecimal.getDisplayLabel(CommonProperties.getDefaultLocale()), testField.getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdAttributeDecimal.definesAttribute(), testField.getDefiningMdAttribute().definesAttribute());
    Assert.assertEquals(mdWebDecimal.getValue(MdWebDecimalInfo.STARTRANGE), testField.getValue(MdWebDecimalInfo.STARTRANGE));
    Assert.assertEquals(mdWebDecimal.getValue(MdWebDecimalInfo.ENDRANGE), testField.getValue(MdWebDecimalInfo.ENDRANGE));
    Assert.assertEquals(mdWebDecimal.getValue(MdWebDecimalInfo.DECPRECISION), testField.getValue(MdWebDecimalInfo.DECPRECISION));
    Assert.assertEquals(mdWebDecimal.getValue(MdWebDecimalInfo.DECSCALE), testField.getValue(MdWebDecimalInfo.DECSCALE));
  }

  @Request
  @Test
  public void testCreateMdWebGeo()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdBusinessDAO referenceBusiness = TestFixtureFactory.createMdBusiness2();
    referenceBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    referenceBusiness.apply();

    MdAttributeReferenceDAO mdAttributeReference = TestFixtureFactory.addReferenceAttribute(mdBusiness, referenceBusiness);
    mdAttributeReference.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebGeoDAO mdWebGeo = TestFixtureFactory.addGeoField(mdWebForm, mdAttributeReference);
    mdWebGeo.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    Assert.assertEquals(mdWebForm.getFormName(), test.getFormName());
    Assert.assertEquals(mdBusiness.definesType(), test.getFormMdClass().definesType());
    Assert.assertEquals(mdWebForm.getDescription(CommonProperties.getDefaultLocale()), test.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebForm.getDisplayLabel(CommonProperties.getDefaultLocale()), test.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(1, fields.size());

    MdWebGeoDAO testField = (MdWebGeoDAO) fields.get(0);

    Assert.assertEquals(mdWebGeo.getFieldName(), testField.getFieldName());
    Assert.assertEquals(mdWebGeo.getFieldOrder(), testField.getFieldOrder());
    Assert.assertEquals(mdWebGeo.getDescription(CommonProperties.getDefaultLocale()), testField.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebGeo.getDisplayLabel(CommonProperties.getDefaultLocale()), testField.getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdAttributeReference.definesAttribute(), testField.getDefiningMdAttribute().definesAttribute());
  }

  @Request
  @Test
  public void testCreateMdWebSingleTermGrid()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdBusinessDAO referenceBusiness = TestFixtureFactory.createMdBusiness2();
    referenceBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    referenceBusiness.apply();

    MdAttributeReferenceDAO mdAttributeReference = TestFixtureFactory.addReferenceAttribute(mdBusiness, referenceBusiness);
    mdAttributeReference.apply();

    MdAttributeLongDAO mdAttributeLong = TestFixtureFactory.addLongAttribute(referenceBusiness);
    mdAttributeLong.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebSingleTermGridDAO mdWebSingleTermGrid = TestFixtureFactory.addSingleTermGridField(mdWebForm, mdAttributeReference);
    mdWebSingleTermGrid.apply();

    MdWebLongDAO mdWebLong = TestFixtureFactory.addLongField(mdWebSingleTermGrid, mdAttributeLong);
    mdWebLong.apply();

    mdWebSingleTermGrid.addChild(mdWebLong.getOid(), RelationshipTypes.WEB_GRID_FIELD.getType()).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { referenceBusiness, mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebLong);
    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);
    TestFixtureFactory.delete(referenceBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    Assert.assertEquals(mdWebForm.getFormName(), test.getFormName());
    Assert.assertEquals(mdBusiness.definesType(), test.getFormMdClass().definesType());
    Assert.assertEquals(mdWebForm.getDescription(CommonProperties.getDefaultLocale()), test.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebForm.getDisplayLabel(CommonProperties.getDefaultLocale()), test.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(1, fields.size());

    MdWebSingleTermGridDAO testField = (MdWebSingleTermGridDAO) fields.get(0);

    Assert.assertEquals(mdWebSingleTermGrid.getFieldName(), testField.getFieldName());
    Assert.assertEquals(mdWebSingleTermGrid.getFieldOrder(), testField.getFieldOrder());
    Assert.assertEquals(mdWebSingleTermGrid.getDescription(CommonProperties.getDefaultLocale()), testField.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebSingleTermGrid.getDisplayLabel(CommonProperties.getDefaultLocale()), testField.getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdAttributeReference.definesAttribute(), testField.getDefiningMdAttribute().definesAttribute());

    // Test the fields MdWebLong
    List<RelationshipDAOIF> relationships = testField.getChildren(RelationshipTypes.WEB_GRID_FIELD.getType());

    Assert.assertEquals(1, relationships.size());

    if (relationships.size() > 0)
    {
      for (RelationshipDAOIF relationship : relationships)
      {
        MdWebLongDAOIF testWebLong = (MdWebLongDAOIF) MdWebPrimitiveDAO.get(relationship.getChildOid());

        Assert.assertEquals(mdWebLong.getFieldName(), testWebLong.getFieldName());
        Assert.assertEquals(mdWebLong.getFieldOrder(), testWebLong.getFieldOrder());
        Assert.assertEquals(mdWebLong.getDescription(CommonProperties.getDefaultLocale()), testWebLong.getDescription(CommonProperties.getDefaultLocale()));
        Assert.assertEquals(mdWebLong.getDisplayLabel(CommonProperties.getDefaultLocale()), testWebLong.getDisplayLabel(CommonProperties.getDefaultLocale()));
        Assert.assertEquals(mdAttributeLong.definesAttribute(), testWebLong.getDefiningMdAttribute().definesAttribute());

        TestFixtureFactory.delete(testWebLong);
      }
    }
  }

  @Request
  @Test
  public void testCreateMdWebMultipleTerm()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdBusinessDAO referenceBusiness = TestFixtureFactory.createMdBusiness2();
    referenceBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    referenceBusiness.apply();

    MdAttributeReferenceDAO mdAttributeReference = TestFixtureFactory.addReferenceAttribute(mdBusiness, referenceBusiness);
    mdAttributeReference.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebMultipleTermDAO mdWebMultipleTerm = TestFixtureFactory.addMultipleTermField(mdWebForm, mdAttributeReference);
    mdWebMultipleTerm.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    Assert.assertEquals(mdWebForm.getFormName(), test.getFormName());
    Assert.assertEquals(mdBusiness.definesType(), test.getFormMdClass().definesType());
    Assert.assertEquals(mdWebForm.getDescription(CommonProperties.getDefaultLocale()), test.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebForm.getDisplayLabel(CommonProperties.getDefaultLocale()), test.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(1, fields.size());

    MdWebMultipleTermDAO testField = (MdWebMultipleTermDAO) fields.get(0);

    Assert.assertEquals(mdWebMultipleTerm.getFieldName(), testField.getFieldName());
    Assert.assertEquals(mdWebMultipleTerm.getFieldOrder(), testField.getFieldOrder());
    Assert.assertEquals(mdWebMultipleTerm.getDescription(CommonProperties.getDefaultLocale()), testField.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebMultipleTerm.getDisplayLabel(CommonProperties.getDefaultLocale()), testField.getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdAttributeReference.definesAttribute(), testField.getDefiningMdAttribute().definesAttribute());
  }

  @Request
  @Test
  public void testCreateMdWebBoolean()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeBooleanDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdBusiness);
    mdAttributeBoolean.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebBooleanDAO mdWebBoolean = TestFixtureFactory.addBooleanField(mdWebForm, mdAttributeBoolean);
    mdWebBoolean.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    Assert.assertEquals(mdWebForm.getFormName(), test.getFormName());
    Assert.assertEquals(mdBusiness.definesType(), test.getFormMdClass().definesType());
    Assert.assertEquals(mdWebForm.getDescription(CommonProperties.getDefaultLocale()), test.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebForm.getDisplayLabel(CommonProperties.getDefaultLocale()), test.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(1, fields.size());

    MdWebBooleanDAO testField = (MdWebBooleanDAO) fields.get(0);

    Assert.assertEquals(mdWebBoolean.getFieldName(), testField.getFieldName());
    Assert.assertEquals(mdWebBoolean.getFieldOrder(), testField.getFieldOrder());
    Assert.assertEquals(mdWebBoolean.getDescription(CommonProperties.getDefaultLocale()), testField.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebBoolean.getDisplayLabel(CommonProperties.getDefaultLocale()), testField.getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdAttributeBoolean.definesAttribute(), testField.getDefiningMdAttribute().definesAttribute());
    Assert.assertEquals(mdWebBoolean.getValue(MdWebBooleanInfo.DEFAULT_VALUE), testField.getValue(MdWebBooleanInfo.DEFAULT_VALUE));
  }

  @Request
  @Test
  public void testCreateMdWebDateTime()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeDateTimeDAO mdAttributeDateTime = TestFixtureFactory.addDateTimeAttribute(mdBusiness);
    mdAttributeDateTime.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebDateTimeDAO mdWebDateTime = TestFixtureFactory.addDateTimeField(mdWebForm, mdAttributeDateTime);
    mdWebDateTime.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    Assert.assertEquals(mdWebForm.getFormName(), test.getFormName());
    Assert.assertEquals(mdBusiness.definesType(), test.getFormMdClass().definesType());
    Assert.assertEquals(mdWebForm.getDescription(CommonProperties.getDefaultLocale()), test.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebForm.getDisplayLabel(CommonProperties.getDefaultLocale()), test.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(1, fields.size());

    MdWebDateTimeDAO testField = (MdWebDateTimeDAO) fields.get(0);

    Assert.assertEquals(mdWebDateTime.getFieldName(), testField.getFieldName());
    Assert.assertEquals(mdWebDateTime.getFieldOrder(), testField.getFieldOrder());
    Assert.assertEquals(mdWebDateTime.getDescription(CommonProperties.getDefaultLocale()), testField.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebDateTime.getDisplayLabel(CommonProperties.getDefaultLocale()), testField.getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdAttributeDateTime.definesAttribute(), testField.getDefiningMdAttribute().definesAttribute());
  }

  @Request
  @Test
  public void testCreateMdWebTime()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeTimeDAO mdAttributeTime = TestFixtureFactory.addTimeAttribute(mdBusiness);
    mdAttributeTime.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebTimeDAO mdWebTime = TestFixtureFactory.addTimeField(mdWebForm, mdAttributeTime);
    mdWebTime.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    Assert.assertEquals(mdWebForm.getFormName(), test.getFormName());
    Assert.assertEquals(mdBusiness.definesType(), test.getFormMdClass().definesType());
    Assert.assertEquals(mdWebForm.getDescription(CommonProperties.getDefaultLocale()), test.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebForm.getDisplayLabel(CommonProperties.getDefaultLocale()), test.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(1, fields.size());

    MdWebTimeDAO testField = (MdWebTimeDAO) fields.get(0);

    Assert.assertEquals(mdWebTime.getFieldName(), testField.getFieldName());
    Assert.assertEquals(mdWebTime.getFieldOrder(), testField.getFieldOrder());
    Assert.assertEquals(mdWebTime.getDescription(CommonProperties.getDefaultLocale()), testField.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebTime.getDisplayLabel(CommonProperties.getDefaultLocale()), testField.getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdAttributeTime.definesAttribute(), testField.getDefiningMdAttribute().definesAttribute());
  }

  @Request
  @Test
  public void testCreateMdWebBreak()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebBreakDAO mdWebBreak = TestFixtureFactory.addBreakField(mdWebForm);
    mdWebBreak.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    Assert.assertEquals(mdWebForm.getFormName(), test.getFormName());
    Assert.assertEquals(mdBusiness.definesType(), test.getFormMdClass().definesType());
    Assert.assertEquals(mdWebForm.getDescription(CommonProperties.getDefaultLocale()), test.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebForm.getDisplayLabel(CommonProperties.getDefaultLocale()), test.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(1, fields.size());

    MdWebBreakDAO testField = (MdWebBreakDAO) fields.get(0);

    Assert.assertEquals(mdWebBreak.getFieldName(), testField.getFieldName());
    Assert.assertEquals(mdWebBreak.getFieldOrder(), testField.getFieldOrder());
    Assert.assertEquals(mdWebBreak.getDescription(CommonProperties.getDefaultLocale()), testField.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebBreak.getDisplayLabel(CommonProperties.getDefaultLocale()), testField.getDisplayLabel(CommonProperties.getDefaultLocale()));
  }

  @Request
  @Test
  public void testCreateMdWebComment()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebCommentDAO mdWebComment = TestFixtureFactory.addCommentField(mdWebForm);
    mdWebComment.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    Assert.assertEquals(mdWebForm.getFormName(), test.getFormName());
    Assert.assertEquals(mdBusiness.definesType(), test.getFormMdClass().definesType());
    Assert.assertEquals(mdWebForm.getDescription(CommonProperties.getDefaultLocale()), test.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebForm.getDisplayLabel(CommonProperties.getDefaultLocale()), test.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(1, fields.size());

    MdWebCommentDAO testField = (MdWebCommentDAO) fields.get(0);

    Assert.assertEquals(mdWebComment.getFieldName(), testField.getFieldName());
    Assert.assertEquals(mdWebComment.getFieldOrder(), testField.getFieldOrder());
    Assert.assertEquals(mdWebComment.getDescription(CommonProperties.getDefaultLocale()), testField.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebComment.getDisplayLabel(CommonProperties.getDefaultLocale()), testField.getDisplayLabel(CommonProperties.getDefaultLocale()));
  }

  @Request
  @Test
  public void testCreateMdWebGroup()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeDoubleDAO mdAttributeDouble = TestFixtureFactory.addDoubleAttribute(mdBusiness);
    mdAttributeDouble.apply();

    MdAttributeBooleanDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdBusiness);
    mdAttributeBoolean.apply();

    MdAttributeCharacterDAO mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdBusiness);
    mdAttributeCharacter.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebGroupDAO mdWebGroup = TestFixtureFactory.addGroupField(mdWebForm);
    mdWebGroup.setValue(MdWebGroupInfo.FIELD_ORDER, "-10");
    mdWebGroup.apply();

    MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdWebForm, mdAttributeCharacter);
    mdWebCharacter.setValue(MdWebGroupInfo.FIELD_ORDER, "0");
    mdWebCharacter.apply();

    MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdWebForm, mdAttributeDouble);
    mdWebDouble.setValue(MdWebGroupInfo.FIELD_ORDER, "1");
    mdWebDouble.apply();

    MdWebBooleanDAO mdWebBoolean = TestFixtureFactory.addBooleanField(mdWebForm, mdAttributeBoolean);
    mdWebBoolean.setValue(MdWebGroupInfo.FIELD_ORDER, "2");
    mdWebBoolean.apply();

    mdWebGroup.addField(mdWebCharacter).apply();
    mdWebGroup.addField(mdWebDouble).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    Assert.assertEquals(mdWebForm.getFormName(), test.getFormName());
    Assert.assertEquals(mdBusiness.definesType(), test.getFormMdClass().definesType());
    Assert.assertEquals(mdWebForm.getDescription(CommonProperties.getDefaultLocale()), test.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebForm.getDisplayLabel(CommonProperties.getDefaultLocale()), test.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(4, fields.size());

    MdWebGroupDAO testGroup = (MdWebGroupDAO) fields.get(0);

    Assert.assertEquals(mdWebGroup.getFieldName(), testGroup.getFieldName());
    Assert.assertEquals(mdWebGroup.getFieldOrder(), testGroup.getFieldOrder());
    Assert.assertEquals(mdWebGroup.getDescription(CommonProperties.getDefaultLocale()), testGroup.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebGroup.getDisplayLabel(CommonProperties.getDefaultLocale()), testGroup.getDisplayLabel(CommonProperties.getDefaultLocale()));

    MdWebCharacterDAO testCharaceter = (MdWebCharacterDAO) fields.get(1);
    MdWebDoubleDAO testDouble = (MdWebDoubleDAO) fields.get(2);
    MdWebBooleanDAO testBoolean = (MdWebBooleanDAO) fields.get(3);

    Assert.assertEquals(testGroup.getOid(), testCharaceter.getContainingGroup().getOid());
    Assert.assertEquals(testGroup.getOid(), testDouble.getContainingGroup().getOid());
    Assert.assertNull(testBoolean.getContainingGroup());
  }

  @Request
  @Test
  public void testCreateMdWebHeader()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebHeaderDAO mdWebHeader = TestFixtureFactory.addHeaderField(mdWebForm);
    mdWebHeader.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    Assert.assertEquals(mdWebForm.getFormName(), test.getFormName());
    Assert.assertEquals(mdBusiness.definesType(), test.getFormMdClass().definesType());
    Assert.assertEquals(mdWebForm.getDescription(CommonProperties.getDefaultLocale()), test.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebForm.getDisplayLabel(CommonProperties.getDefaultLocale()), test.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(1, fields.size());

    MdWebHeaderDAO testField = (MdWebHeaderDAO) fields.get(0);

    Assert.assertEquals(mdWebHeader.getFieldName(), testField.getFieldName());
    Assert.assertEquals(mdWebHeader.getFieldOrder(), testField.getFieldOrder());
    Assert.assertEquals(mdWebHeader.getDescription(CommonProperties.getDefaultLocale()), testField.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebHeader.getDisplayLabel(CommonProperties.getDefaultLocale()), testField.getDisplayLabel(CommonProperties.getDefaultLocale()));
  }

  @Request
  @Test
  public void testCreateBasicDateCondition()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeDateDAO mdAttributeDate = TestFixtureFactory.addDateAttribute(mdBusiness);
    mdAttributeDate.apply();

    MdAttributeBooleanDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdBusiness);
    mdAttributeBoolean.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebDateDAO mdWebDate = TestFixtureFactory.addDateField(mdWebForm, mdAttributeDate);
    mdWebDate.apply();

    DateConditionDAO condition = TestFixtureFactory.addDateCondition(mdWebDate);
    condition.apply();

    String operationId = this.getOperationId(condition);

    MdWebBooleanDAO mdWebBoolean = TestFixtureFactory.addBooleanField(mdWebForm, mdAttributeBoolean);
    mdWebBoolean.setValue(MdWebBooleanInfo.FIELD_CONDITION, condition.getOid());
    mdWebBoolean.setValue(MdWebBooleanInfo.FIELD_ORDER, "10");
    mdWebBoolean.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(2, fields.size());

    MdWebBooleanDAO testField = (MdWebBooleanDAO) fields.get(1);

    Assert.assertEquals(mdWebBoolean.getFieldName(), testField.getFieldName());

    String conditionId = testField.getValue(MdWebBooleanInfo.FIELD_CONDITION);

    Assert.assertTrue(conditionId.length() > 0);

    MdWebDateDAOIF definingField = (MdWebDateDAOIF) fields.get(0);
    FieldConditionDAOIF testCondition = FieldConditionDAO.get(conditionId);

    Assert.assertTrue( ( testCondition instanceof DateConditionDAOIF ));
    Assert.assertEquals(condition.getValue(DateConditionInfo.VALUE), testCondition.getValue(DateConditionInfo.VALUE));

    String testOperationId = this.getOperationId(testCondition);

    Assert.assertEquals(operationId, testOperationId);
    Assert.assertEquals(definingField.getOid(), testCondition.getValue(BasicConditionInfo.DEFINING_MD_FIELD));
  }

  @Request
  @Test
  public void testCreateBasicCharacterCondition()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeCharacterDAO mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdBusiness);
    mdAttributeCharacter.apply();

    MdAttributeBooleanDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdBusiness);
    mdAttributeBoolean.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdWebForm, mdAttributeCharacter);
    mdWebCharacter.apply();

    CharacterConditionDAO condition = TestFixtureFactory.addCharacterCondition(mdWebCharacter);
    condition.apply();

    String operationId = this.getOperationId(condition);

    MdWebBooleanDAO mdWebBoolean = TestFixtureFactory.addBooleanField(mdWebForm, mdAttributeBoolean);
    mdWebBoolean.setValue(MdWebBooleanInfo.FIELD_CONDITION, condition.getOid());
    mdWebBoolean.setValue(MdWebBooleanInfo.FIELD_ORDER, "10");
    mdWebBoolean.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(2, fields.size());

    MdWebBooleanDAOIF testField = (MdWebBooleanDAOIF) fields.get(1);

    Assert.assertEquals(mdWebBoolean.getFieldName(), testField.getFieldName());

    String conditionId = testField.getValue(MdWebBooleanInfo.FIELD_CONDITION);

    Assert.assertTrue(conditionId.length() > 0);

    MdWebCharacterDAOIF definingField = (MdWebCharacterDAOIF) fields.get(0);
    FieldConditionDAOIF testCondition = FieldConditionDAO.get(conditionId);

    Assert.assertTrue( ( testCondition instanceof CharacterConditionDAOIF ));
    Assert.assertEquals(condition.getValue(CharacterConditionInfo.VALUE), testCondition.getValue(CharacterConditionInfo.VALUE));

    String testOperationId = this.getOperationId(testCondition);

    Assert.assertEquals(operationId, testOperationId);
    Assert.assertEquals(definingField.getOid(), testCondition.getValue(BasicConditionInfo.DEFINING_MD_FIELD));
  }

  @Request
  @Test
  public void testCreateBasicLongCondition()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeLongDAO mdAttributeLong = TestFixtureFactory.addLongAttribute(mdBusiness);
    mdAttributeLong.apply();

    MdAttributeBooleanDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdBusiness);
    mdAttributeBoolean.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebLongDAO mdWebLong = TestFixtureFactory.addLongField(mdWebForm, mdAttributeLong);
    mdWebLong.apply();

    LongConditionDAO condition = TestFixtureFactory.addLongCondition(mdWebLong);
    condition.apply();

    String operationId = this.getOperationId(condition);

    MdWebBooleanDAO mdWebBoolean = TestFixtureFactory.addBooleanField(mdWebForm, mdAttributeBoolean);
    mdWebBoolean.setValue(MdWebBooleanInfo.FIELD_CONDITION, condition.getOid());
    mdWebBoolean.setValue(MdWebBooleanInfo.FIELD_ORDER, "10");
    mdWebBoolean.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(2, fields.size());

    MdWebBooleanDAO testField = (MdWebBooleanDAO) fields.get(1);

    Assert.assertEquals(mdWebBoolean.getFieldName(), testField.getFieldName());

    String conditionId = testField.getValue(MdWebBooleanInfo.FIELD_CONDITION);

    Assert.assertTrue(conditionId.length() > 0);

    MdWebLongDAOIF definingField = (MdWebLongDAOIF) fields.get(0);
    FieldConditionDAOIF testCondition = FieldConditionDAO.get(conditionId);

    Assert.assertTrue( ( testCondition instanceof LongConditionDAOIF ));
    Assert.assertEquals(condition.getValue(LongConditionInfo.VALUE), testCondition.getValue(LongConditionInfo.VALUE));

    String testOperationId = this.getOperationId(testCondition);

    Assert.assertEquals(operationId, testOperationId);
    Assert.assertEquals(definingField.getOid(), testCondition.getValue(BasicConditionInfo.DEFINING_MD_FIELD));
  }

  @Request
  @Test
  public void testCreateBasicDoubleCondition()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeDoubleDAO mdAttributeDouble = TestFixtureFactory.addDoubleAttribute(mdBusiness);
    mdAttributeDouble.apply();

    MdAttributeBooleanDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdBusiness);
    mdAttributeBoolean.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdWebForm, mdAttributeDouble);
    mdWebDouble.apply();

    DoubleConditionDAO condition = TestFixtureFactory.addDoubleCondition(mdWebDouble);
    condition.apply();

    String operationId = this.getOperationId(condition);

    MdWebBooleanDAO mdWebBoolean = TestFixtureFactory.addBooleanField(mdWebForm, mdAttributeBoolean);
    mdWebBoolean.setValue(MdWebBooleanInfo.FIELD_CONDITION, condition.getOid());
    mdWebBoolean.setValue(MdWebBooleanInfo.FIELD_ORDER, "10");
    mdWebBoolean.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(2, fields.size());

    MdWebBooleanDAO testField = (MdWebBooleanDAO) fields.get(1);

    Assert.assertEquals(mdWebBoolean.getFieldName(), testField.getFieldName());

    String conditionId = testField.getValue(MdWebBooleanInfo.FIELD_CONDITION);

    Assert.assertTrue(conditionId.length() > 0);

    MdWebDoubleDAOIF definingField = (MdWebDoubleDAOIF) fields.get(0);
    FieldConditionDAOIF testCondition = FieldConditionDAO.get(conditionId);

    Assert.assertTrue( ( testCondition instanceof DoubleConditionDAOIF ));
    Assert.assertEquals("12.6000000000", testCondition.getValue(DoubleConditionInfo.VALUE));

    String testOperationId = this.getOperationId(testCondition);

    Assert.assertEquals(operationId, testOperationId);
    Assert.assertEquals(definingField.getOid(), testCondition.getValue(BasicConditionInfo.DEFINING_MD_FIELD));
  }

  @Request
  @Test
  public void testCreateAndCondition()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeDoubleDAO mdAttributeDouble = TestFixtureFactory.addDoubleAttribute(mdBusiness);
    mdAttributeDouble.apply();

    MdAttributeBooleanDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdBusiness);
    mdAttributeBoolean.apply();

    MdAttributeCharacterDAO mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdBusiness);
    mdAttributeCharacter.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdWebForm, mdAttributeCharacter);
    mdWebCharacter.apply();

    CharacterConditionDAO firstCondition = TestFixtureFactory.addCharacterCondition(mdWebCharacter);
    firstCondition.apply();

    MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdWebForm, mdAttributeDouble);
    mdWebDouble.apply();

    DoubleConditionDAO secondCondition = TestFixtureFactory.addDoubleCondition(mdWebDouble);
    secondCondition.apply();

    AndFieldConditionDAO condition = TestFixtureFactory.addAndCondition(firstCondition, secondCondition);
    condition.apply();

    MdWebBooleanDAO mdWebBoolean = TestFixtureFactory.addBooleanField(mdWebForm, mdAttributeBoolean);
    mdWebBoolean.setValue(MdWebBooleanInfo.FIELD_CONDITION, condition.getOid());
    mdWebBoolean.setValue(MdWebBooleanInfo.FIELD_ORDER, "10");
    mdWebBoolean.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(3, fields.size());

    MdWebBooleanDAO testField = (MdWebBooleanDAO) fields.get(2);

    Assert.assertEquals(mdWebBoolean.getFieldName(), testField.getFieldName());

    String conditionId = testField.getValue(MdWebBooleanInfo.FIELD_CONDITION);

    Assert.assertTrue(conditionId.length() > 0);

    FieldConditionDAOIF testCondition = FieldConditionDAO.get(conditionId);

    Assert.assertTrue( ( testCondition instanceof AndFieldConditionDAOIF ));

    FieldConditionDAOIF testFirstCondition = FieldConditionDAO.get(testCondition.getValue(AndFieldConditionInfo.FIRST_CONDITION));
    FieldConditionDAOIF testSecondCondition = FieldConditionDAO.get(testCondition.getValue(AndFieldConditionInfo.SECOND_CONDITION));

    Assert.assertTrue( ( testFirstCondition instanceof CharacterConditionDAOIF ));
    Assert.assertTrue( ( testSecondCondition instanceof DoubleConditionDAOIF ));
  }

  @Request
  @Test
  public void testCreateNestedAndCondition()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeDoubleDAO mdAttributeDouble = TestFixtureFactory.addDoubleAttribute(mdBusiness);
    mdAttributeDouble.apply();

    MdAttributeBooleanDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdBusiness);
    mdAttributeBoolean.apply();

    MdAttributeCharacterDAO mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdBusiness);
    mdAttributeCharacter.apply();

    MdAttributeLongDAO mdAttributeLong = TestFixtureFactory.addLongAttribute(mdBusiness);
    mdAttributeLong.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdWebForm, mdAttributeCharacter);
    mdWebCharacter.apply();

    CharacterConditionDAO firstCondition = TestFixtureFactory.addCharacterCondition(mdWebCharacter);
    firstCondition.apply();

    MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdWebForm, mdAttributeDouble);
    mdWebDouble.apply();

    DoubleConditionDAO secondCondition = TestFixtureFactory.addDoubleCondition(mdWebDouble);
    secondCondition.apply();

    MdWebLongDAO mdWebLong = TestFixtureFactory.addLongField(mdWebForm, mdAttributeLong);
    mdWebLong.apply();

    LongConditionDAO thirdCondition = TestFixtureFactory.addLongCondition(mdWebLong);
    thirdCondition.apply();

    AndFieldConditionDAO innerAndCondition = TestFixtureFactory.addAndCondition(firstCondition, secondCondition);
    innerAndCondition.apply();

    AndFieldConditionDAO condition = TestFixtureFactory.addAndCondition(innerAndCondition, thirdCondition);
    condition.apply();

    MdWebBooleanDAO mdWebBoolean = TestFixtureFactory.addBooleanField(mdWebForm, mdAttributeBoolean);
    mdWebBoolean.setValue(MdWebBooleanInfo.FIELD_CONDITION, condition.getOid());
    mdWebBoolean.setValue(MdWebBooleanInfo.FIELD_ORDER, "10");
    mdWebBoolean.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, mdWebForm }));

    TestFixtureFactory.delete(mdWebForm);
    TestFixtureFactory.delete(mdBusiness);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(4, fields.size());

    MdWebBooleanDAO testField = (MdWebBooleanDAO) fields.get(3);

    Assert.assertEquals(mdWebBoolean.getFieldName(), testField.getFieldName());

    String conditionId = testField.getValue(MdWebBooleanInfo.FIELD_CONDITION);

    Assert.assertTrue(conditionId.length() > 0);

    FieldConditionDAOIF testCondition = FieldConditionDAO.get(conditionId);

    Assert.assertTrue( ( testCondition instanceof AndFieldConditionDAOIF ));

    FieldConditionDAOIF testFirstCondition = FieldConditionDAO.get(testCondition.getValue(AndFieldConditionInfo.FIRST_CONDITION));
    FieldConditionDAOIF testSecondCondition = FieldConditionDAO.get(testCondition.getValue(AndFieldConditionInfo.SECOND_CONDITION));

    Assert.assertTrue( ( testFirstCondition instanceof AndFieldConditionDAOIF ));
    Assert.assertTrue( ( testSecondCondition instanceof LongConditionDAOIF ));
  }

  @Request
  @Test
  public void testCreateOrUpdateNestedAndCondition()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeDoubleDAO mdAttributeDouble = TestFixtureFactory.addDoubleAttribute(mdBusiness);
    mdAttributeDouble.apply();

    MdAttributeBooleanDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdBusiness);
    mdAttributeBoolean.apply();

    MdAttributeCharacterDAO mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdBusiness);
    mdAttributeCharacter.apply();

    MdAttributeLongDAO mdAttributeLong = TestFixtureFactory.addLongAttribute(mdBusiness);
    mdAttributeLong.apply();

    MdWebFormDAO mdWebForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdWebForm.apply();

    MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdWebForm, mdAttributeCharacter);
    mdWebCharacter.apply();

    CharacterConditionDAO firstCondition = TestFixtureFactory.addCharacterCondition(mdWebCharacter);
    firstCondition.apply();

    MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdWebForm, mdAttributeDouble);
    mdWebDouble.apply();

    DoubleConditionDAO secondCondition = TestFixtureFactory.addDoubleCondition(mdWebDouble);
    secondCondition.apply();

    MdWebLongDAO mdWebLong = TestFixtureFactory.addLongField(mdWebForm, mdAttributeLong);
    mdWebLong.apply();

    LongConditionDAO thirdCondition = TestFixtureFactory.addLongCondition(mdWebLong);
    thirdCondition.apply();

    AndFieldConditionDAO innerAndCondition = TestFixtureFactory.addAndCondition(firstCondition, secondCondition);
    innerAndCondition.apply();

    AndFieldConditionDAO condition = TestFixtureFactory.addAndCondition(innerAndCondition, thirdCondition);
    condition.apply();

    MdWebBooleanDAO mdWebBoolean = TestFixtureFactory.addBooleanField(mdWebForm, mdAttributeBoolean);
    mdWebBoolean.setValue(MdWebBooleanInfo.FIELD_CONDITION, condition.getOid());
    mdWebBoolean.setValue(MdWebBooleanInfo.FIELD_ORDER, "10");
    mdWebBoolean.apply();

    // Export out both the MdBusiness and the MdWebForm
    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreateOrUpdate(new ComponentIF[] { mdBusiness, mdWebForm }));

    // Only delete the MdWebForm. The will simulate creating a new MdWebForm,
    // but updating an existing mdBusiness
    TestFixtureFactory.delete(mdWebForm);

    SAXImporter.runImport(new File(tempXMLFile));

    MdWebFormDAOIF test = (MdWebFormDAOIF) MdWebFormDAO.getMdTypeDAO(mdWebForm.definesType());

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(4, fields.size());

    MdWebBooleanDAO testField = (MdWebBooleanDAO) fields.get(3);

    Assert.assertEquals(mdWebBoolean.getFieldName(), testField.getFieldName());

    String conditionId = testField.getValue(MdWebBooleanInfo.FIELD_CONDITION);

    Assert.assertTrue(conditionId.length() > 0);

    FieldConditionDAOIF testCondition = FieldConditionDAO.get(conditionId);

    Assert.assertTrue( ( testCondition instanceof AndFieldConditionDAOIF ));

    FieldConditionDAOIF testFirstCondition = FieldConditionDAO.get(testCondition.getValue(AndFieldConditionInfo.FIRST_CONDITION));
    FieldConditionDAOIF testSecondCondition = FieldConditionDAO.get(testCondition.getValue(AndFieldConditionInfo.SECOND_CONDITION));

    Assert.assertTrue( ( testFirstCondition instanceof AndFieldConditionDAOIF ));
    Assert.assertTrue( ( testSecondCondition instanceof LongConditionDAOIF ));
  }

  @Request
  @Test
  public void testSelectionSet()
  {
    MdBusinessDAO mdBusinessEnum1 = TestFixtureFactory.createEnumClass1();
    mdBusinessEnum1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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
    mdEnumeration.setValue(MdEnumerationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdEnumeration.apply();

    mdEnumeration.addEnumItem(businessDAO1.getOid());
    mdEnumeration.addEnumItem(businessDAO3.getOid());

    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    TestFixtureFactory.addTimeAttribute(mdBusiness1).apply();
    MdAttributeConcreteDAO mdAttributeEnum = TestFixtureFactory.addEnumerationAttribute(mdBusiness1, mdEnumeration);
    mdAttributeEnum.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeEnum.apply();

    BusinessDAO businessDAO4 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO4.setValue("testTime", "23:45:32");
    businessDAO4.addItem("testEnumeration", businessDAO1.getOid());
    businessDAO4.addItem("testEnumeration", businessDAO3.getOid());
    businessDAO4.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { businessDAO4, mdBusiness1, mdBusinessEnum1, mdEnumeration, businessDAO1, businessDAO2, businessDAO3 }));

    TestFixtureFactory.delete(mdEnumeration);
    TestFixtureFactory.delete(mdBusinessEnum1);
    TestFixtureFactory.delete(mdBusiness1);

    SAXImporter.runImport(new File(tempXMLFile));

    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO("test.xmlclasses.Class1");
    List<String> ids = EntityDAO.getEntityIdsDB(mdBusinessIF.definesType());

    Assert.assertEquals(1, ids.size());

    String oid = ids.get(0);

    BusinessDAOIF businessDAO = BusinessDAO.get(oid);

    AttributeEnumerationIF attribute = (AttributeEnumerationIF) businessDAO.getAttributeIF("testEnumeration");
    BusinessDAOIF[] enums = attribute.dereference();

    Assert.assertEquals("23:45:32", businessDAO.getValue("testTime"));
    Assert.assertEquals(2, enums.length);
    Assert.assertTrue(enums[0].getValue(EnumerationMasterInfo.NAME).equals("ONE") || enums[0].getValue(EnumerationMasterInfo.NAME).equals("THREE"));
    Assert.assertTrue(enums[0].getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE).equals("One") || enums[0].getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE).equals("Three"));

    Assert.assertTrue(enums[1].getValue(EnumerationMasterInfo.NAME).equals("ONE") || enums[1].getValue(EnumerationMasterInfo.NAME).equals("THREE"));
    Assert.assertTrue(enums[1].getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE).equals("One") || enums[1].getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE).equals("Three"));
  }

  /**
   * Test setting of attributes of and on the relationship datatype
   */
  @Request
  @Test
  public void testTreeSet()
  {
    SAXImporter.runImport(new File(TREE_SET));

    Assert.assertTrue(MdTypeDAO.isDefined(RELATIONSHIP));

    MdTreeDAO mdRelationship = MdTreeDAO.getMdTreeDAO(RELATIONSHIP).getBusinessDAO();
    MdBusinessDAOIF mdBusiness1 = MdBusinessDAO.getMdBusinessDAO(CLASS);
    MdBusinessDAOIF mdBusiness2 = MdBusinessDAO.getMdBusinessDAO(CLASS2);
    MdAttributeConcreteDAOIF attribute = mdRelationship.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN);

    Assert.assertEquals("Relationship Set Test", mdRelationship.getStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    Assert.assertEquals("Relationship Test", mdRelationship.getStructValue(MdRelationshipInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
    Assert.assertEquals(MdAttributeBooleanInfo.FALSE, mdRelationship.getValue(MdRelationshipInfo.REMOVE));
    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, mdRelationship.getValue(MdRelationshipInfo.EXTENDABLE));
    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, mdRelationship.getValue(MdRelationshipInfo.ABSTRACT));

    // Ensure the parent attributes are correctly set
    Assert.assertEquals(mdRelationship.getValue(MdRelationshipInfo.PARENT_MD_BUSINESS), mdBusiness1.getOid());
    Assert.assertEquals(mdRelationship.getValue(MdRelationshipInfo.PARENT_CARDINALITY), "1");
    Assert.assertEquals(mdRelationship.getStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Parent Set Test");

    // Ensure the child attributes are correctly set
    Assert.assertEquals(mdRelationship.getValue(MdRelationshipInfo.CHILD_MD_BUSINESS), mdBusiness2.getOid());
    Assert.assertEquals(mdRelationship.getValue(MdRelationshipInfo.CHILD_CARDINALITY), "*");
    Assert.assertEquals(mdRelationship.getStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Child Set Test");

    // Ensure the attributes are linked to the relationship
    Assert.assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdRelationship.getOid());

    mdRelationship.setValue(MdRelationshipInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdRelationship.apply();

    // Test the sub relationship
    MdTreeDAO mdRelationship2 = MdTreeDAO.getMdTreeDAO(RELATIONSHIP2).getBusinessDAO();
    Assert.assertEquals(mdRelationship.getOid(), mdRelationship2.getValue(MdRelationshipInfo.SUPER_MD_RELATIONSHIP));
    Assert.assertEquals("2", mdRelationship2.getValue(MdRelationshipInfo.CHILD_CARDINALITY));
  }

  /**
   * Test setting of attributes of and on the relationship datatype
   */
  @Request
  @Test
  public void testGraphSet()
  {
    SAXImporter.runImport(new File(GRAPH_SET));

    MdGraphDAO mdRelationship = MdGraphDAO.getMdGraphDAO(RELATIONSHIP).getBusinessDAO();
    MdBusinessDAOIF mdBusiness1 = MdBusinessDAO.getMdBusinessDAO(CLASS);
    MdBusinessDAOIF mdBusiness2 = MdBusinessDAO.getMdBusinessDAO(CLASS2);
    MdAttributeConcreteDAOIF attribute = mdRelationship.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN);

    Assert.assertEquals("Relationship Set Test", mdRelationship.getStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    Assert.assertEquals("Relationship Test", mdRelationship.getStructValue(MdRelationshipInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
    Assert.assertEquals(MdAttributeBooleanInfo.FALSE, mdRelationship.getValue(MdRelationshipInfo.REMOVE));
    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, mdRelationship.getValue(MdRelationshipInfo.EXTENDABLE));
    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, mdRelationship.getValue(MdRelationshipInfo.ABSTRACT));

    // Ensure the parent attributes are correctly set
    Assert.assertEquals(mdRelationship.getValue(MdRelationshipInfo.PARENT_MD_BUSINESS), mdBusiness1.getOid());
    Assert.assertEquals(mdRelationship.getValue(MdRelationshipInfo.PARENT_CARDINALITY), "1");
    Assert.assertEquals(mdRelationship.getStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Parent Set Test");

    // Ensure the child attributes are correctly set
    Assert.assertEquals(mdRelationship.getValue(MdRelationshipInfo.CHILD_MD_BUSINESS), mdBusiness2.getOid());
    Assert.assertEquals(mdRelationship.getValue(MdRelationshipInfo.CHILD_CARDINALITY), "*");
    Assert.assertEquals(mdRelationship.getStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Child Set Test");

    // Ensure the attributes are linked to the relationship
    Assert.assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdRelationship.getOid());

    mdRelationship.setValue(MdRelationshipInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdRelationship.apply();

    // Test the sub relationship
    MdGraphDAO mdRelationship2 = MdGraphDAO.getMdGraphDAO(RELATIONSHIP2).getBusinessDAO();
    Assert.assertEquals(mdRelationship.getOid(), mdRelationship2.getValue(MdRelationshipInfo.SUPER_MD_RELATIONSHIP));
    Assert.assertEquals("2", mdRelationship2.getValue(MdRelationshipInfo.CHILD_CARDINALITY));
  }

  /**
   * Test creating Instances that reference other instances
   */
  @Request
  @Test
  public void testCreateObject()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    mdBusiness1.apply();
    mdBusiness2.apply();

    TestFixtureFactory.addCharacterAttribute(mdBusiness1).apply();
    TestFixtureFactory.addBooleanAttribute(mdBusiness1).apply();
    TestFixtureFactory.addReferenceAttribute(mdBusiness2, mdBusiness1).apply();

    BusinessDAO businessDAO1 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO1.setValue(TestFixConst.ATTRIBUTE_BOOLEAN, MdAttributeBooleanInfo.TRUE);
    businessDAO1.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "3");
    businessDAO1.apply();

    BusinessDAO businessDAO2 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO2.setValue(TestFixConst.ATTRIBUTE_BOOLEAN, MdAttributeBooleanInfo.FALSE);
    businessDAO2.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Who are you?");
    businessDAO2.apply();

    BusinessDAO businessDAO3 = BusinessDAO.newInstance(mdBusiness2.definesType());
    businessDAO3.setValue("testReference", businessDAO1.getOid());
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
    Assert.assertTrue(classIds.contains(class2.getValue("testReference")));
  }

  @Request
  @Test
  public void testCreateMdEnumeration()
  {
    SAXImporter.runImport(new File(FILTER_SET));

    MdEnumerationDAO mdEnumeration = MdEnumerationDAO.getMdEnumerationDAO(FILTER).getBusinessDAO();
    MdEnumerationDAO mdEnumeration2 = MdEnumerationDAO.getMdEnumerationDAO(FILTER2).getBusinessDAO();

    List<BusinessDAOIF> items = mdEnumeration.getAllEnumItems();

    Assert.assertEquals(mdEnumeration.getStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Enumeration Filter Test");
    Assert.assertEquals(mdEnumeration.getStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Filter Set Test");
    Assert.assertEquals(mdEnumeration.getValue(MdEnumerationInfo.REMOVE), MdAttributeBooleanInfo.FALSE);
    Assert.assertEquals(mdEnumeration.getValue(MdEnumerationInfo.INCLUDE_ALL), MdAttributeBooleanInfo.FALSE);

    // Check that the instance of the filter are correctly mapped
    Assert.assertEquals(items.size(), 2);

    Assert.assertTrue(items.get(0).getValue("testChar").equals("CO") || items.get(0).getValue("testChar").equals("CA"));
    Assert.assertTrue(items.get(1).getValue("testChar").equals("CO") || items.get(1).getValue("testChar").equals("CA"));

    Assert.assertEquals(mdEnumeration2.getValue(MdEnumerationInfo.INCLUDE_ALL), MdAttributeBooleanInfo.TRUE);

    mdEnumeration.setValue(MdEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.apply();
  }

  @Request
  @Test
  public void testCreateRelationship()
  {
    // Create the Metadata entities
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    mdBusiness1.apply();
    mdBusiness2.apply();

    MdRelationshipDAO mdRelationship1 = TestFixtureFactory.createMdRelationship1(mdBusiness1, mdBusiness2);
    mdRelationship1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdRelationship1.apply();

    TestFixtureFactory.addBooleanAttribute(mdRelationship1).apply();

    // Create BusinessDAO for the RelationshipDAO
    BusinessDAO businessDAO1 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO1.apply();

    BusinessDAO businessDAO2 = BusinessDAO.newInstance(mdBusiness2.definesType());
    businessDAO2.apply();

    // Create Test RelationshipDAO
    RelationshipDAO relationshipDAO1 = RelationshipDAO.newInstance(businessDAO1.getOid(), businessDAO2.getOid(), mdRelationship1.definesType());
    relationshipDAO1.setValue(TestFixConst.ATTRIBUTE_BOOLEAN, MdAttributeBooleanInfo.TRUE);
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
    Assert.assertEquals(c1.getOid(), r1.getParentOid());
    // Ensure that the child reference the instance of CLASS2
    Assert.assertEquals(c2.getOid(), r1.getChildOid());
    // Ensure that the value of testBoolean is true
    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, r1.getValue(TestFixConst.ATTRIBUTE_BOOLEAN));
  }

  @Request
  @Test
  public void testCreateMdTree()
  {
    // Create the Metadata entities
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    mdBusiness1.apply();
    mdBusiness2.apply();

    MdTreeDAO mdTree1 = TestFixtureFactory.createMdTree(mdBusiness1, mdBusiness2);
    mdTree1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    mdTree1.apply();

    TestFixtureFactory.addBooleanAttribute(mdTree1).apply();

    // Export the test entities
    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1, mdBusiness2, mdTree1 }));

    // Delete the entities
    TestFixtureFactory.delete(mdTree1);
    TestFixtureFactory.delete(mdBusiness1);
    TestFixtureFactory.delete(mdBusiness2);

    SAXImporter.runImport(new File(tempXMLFile));
  }

  @Request
  @Test
  public void testIndex()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdBoolean = TestFixtureFactory.addBooleanAttribute(mdBusiness1);
    mdBoolean.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdBoolean.apply();

    MdAttributeConcreteDAO mdChar = TestFixtureFactory.addCharacterAttribute(mdBusiness1);
    mdChar.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdChar.apply();

    MdIndexDAO mdIndex = MdIndexDAO.newInstance();
    mdIndex.setStructValue(MdIndexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "index");
    mdIndex.setValue(MdIndexInfo.MD_ENTITY, mdBusiness1.getOid());
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
    Assert.assertEquals(1, indexs.size());

    MdIndexDAOIF mdIndexIF = indexs.get(0);
    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, mdIndexIF.getValue(MdIndexInfo.UNIQUE));
    Assert.assertEquals("index", mdIndexIF.getStructValue(MdIndexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));

    List<MdAttributeConcreteDAOIF> mdAttributes = mdIndexIF.getIndexedAttributes();

    Assert.assertEquals(2, mdAttributes.size());
    Assert.assertTrue(TestFixConst.ATTRIBUTE_BOOLEAN.equals(mdAttributes.get(0).definesAttribute()) || TestFixConst.ATTRIBUTE_BOOLEAN.equals(mdAttributes.get(1).definesAttribute()));
    Assert.assertTrue(TestFixConst.ATTRIBUTE_CHARACTER.equals(mdAttributes.get(0).definesAttribute()) || TestFixConst.ATTRIBUTE_CHARACTER.equals(mdAttributes.get(1).definesAttribute()));
  }

  @Request
  @Test
  public void testMdMethod()
  {
    SAXImporter.runImport(new File(METHOD_SET_TEST));

    MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO(CLASS);

    List<MdMethodDAOIF> mdMethods = mdBusiness.getMdMethods();

    // There should be three methods: Two user defined methods, and the auto
    // generated query method
    Assert.assertEquals(2, mdMethods.size());

    for (MdMethodDAOIF mdMethod : mdMethods)
    {
      if (mdMethod.getName().equals("checkin"))
      {
        Assert.assertTrue(mdMethod.getReturnType().isVoid());
        List<MdParameterDAOIF> mdParameters = mdMethod.getMdParameterDAOs();
        Assert.assertEquals(1, mdParameters.size());
        Assert.assertEquals("testClass2", mdParameters.get(0).getParameterName());
        Assert.assertEquals("test.xmlclasses.Class2", mdParameters.get(0).getParameterType().getType());
        Assert.assertEquals("1", mdParameters.get(0).getParameterOrder());
      }
      else if (mdMethod.getName().equals("checkout"))
      {
        Assert.assertEquals("checkout", mdMethod.getName());
        Assert.assertTrue(mdMethod.getReturnType().isArray());
        Assert.assertEquals("test.xmlclasses.Relationship1[]", mdMethod.getReturnType().getType());

        List<MdParameterDAOIF> mdParameters = mdMethod.getMdParameterDAOs();
        Assert.assertEquals(2, mdParameters.size());

        Assert.assertEquals("testInteger", mdParameters.get(0).getParameterName());
        Assert.assertEquals("java.lang.Integer", mdParameters.get(0).getParameterType().getType());
        Assert.assertEquals("1", mdParameters.get(0).getParameterOrder());

        Assert.assertEquals(TestFixConst.ATTRIBUTE_CHARACTER, mdParameters.get(1).getParameterName());
        Assert.assertEquals("java.lang.String", mdParameters.get(1).getParameterType().getType());
        Assert.assertEquals("4", mdParameters.get(1).getParameterOrder());
      }
    }

    for (MdMethodDAOIF mdMethod : mdMethods)
    {
      TestFixtureFactory.delete(mdMethod);
    }
  }

  /**
   * Test for a thrown error on circular dependencies in the xml document
   */
  @Request
  @Test
  public void testCircularDependency()
  {
    try
    {
      SAXImporter.runImport(new File(CIRCULAR_TEST));

      Assert.fail("Failed circular dependency check between two classes");
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
  @Request
  @Test
  public void testDuplicateAttribute()
  {
    try
    {
      SAXImporter.runImport(new File(DUPLICATE_TEST));

      Assert.fail("Failed duplicate attribute name in the same class check");
    }
    catch (XMLParseException e)
    {
      // This is what we want.

      if (! ( e.getCause() instanceof DuplicateAttributeDefinitionException ) && ! ( e.getCause() instanceof DuplicateDataException ))
      {
        throw e;
      }
    }
  }

  /**
   * Test for a thrown error on circular dependencies in the xml document
   */
  @Request
  @Test
  public void testInvalidSchema()
  {
    try
    {
      SAXImporter.runImport(new File(INVALID_SCHEMA_TEST));

      Assert.fail("Failed invalid schema check");
    }
    catch (XMLParseException e)
    {
      // This is expected
    }
  }

  /**
   * Test for a thrown error on circular dependencies in the xml document
   */
  @Request
  @Test
  public void testInvalidPuesdoId()
  {
    try
    {
      SAXImporter.runImport(new File(INVALID_ID));

      Assert.fail("Failed invalid puesdo oid check");
    }
    catch (XMLException e)
    {
      // This is expected
    }
  }

  @Request
  @Test
  public void testSearchingOfDefinitionInAnUpdateTag()
  {
    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

    Assert.assertNotNull(mdAttributeVirtualIF);
  }

  @Request
  @Test
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
      mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness1.apply();

      MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
      mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness2.apply();

      MdAttributeConcreteDAO mdAttributeChar = TestFixtureFactory.addCharacterAttribute(mdBusiness1);
      mdAttributeChar.apply();

      MdRelationshipDAO mdRelationship = TestFixtureFactory.createMdRelationship1(mdBusiness1, mdBusiness2);
      mdRelationship.setValue(MdRelationshipInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdRelationship.apply();

      MdAttributeConcreteDAO mdAttributeBool = TestFixtureFactory.addBooleanAttribute(mdRelationship);
      mdAttributeBool.apply();

      // Add permissions to the MdBusiness
      user.grantPermission(Operation.CREATE, mdBusiness1.getOid());
      user.grantPermission(Operation.WRITE, mdBusiness1.getOid());

      // Add attribute permissions
      user.grantPermission(Operation.WRITE, mdAttributeChar.getOid());
      user.grantPermission(Operation.READ, mdAttributeChar.getOid());

      // Add struct permissions
      MdStructDAO mdStruct = TestFixtureFactory.createMdStruct1();
      mdStruct.setValue(MdStructInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdStruct.apply();

      user.grantPermission(Operation.DELETE, mdStruct.getOid());

      // Add permissions to a MdRelationship
      user.grantPermission(Operation.CREATE, mdRelationship.getOid());
      user.grantPermission(Operation.DELETE, mdRelationship.getOid());

      // Add permissions to an attribute defined by the MdRelationship
      user.grantPermission(Operation.READ, mdAttributeBool.getOid());

      // Export the permissions
      ExportMetadata metadata = new ExportMetadata();
      metadata.addCreate(user);
      metadata.addGrantPermissions(user);

      SAXExporter.export(tempXMLFile, SCHEMA, metadata);

      TestFixtureFactory.delete(user);

      SAXImporter.runImport(new File(tempXMLFile));

      UserDAOIF userIF = UserDAO.findUser("testUser");

      Set<RoleDAOIF> assignedRoles = userIF.assignedRoles();
      Assert.assertEquals(1, assignedRoles.size());
      Assert.assertTrue(assignedRoles.contains(role));

      Set<Operation> operations = userIF.getAllPermissions(mdBusiness1);
      Assert.assertEquals(2, operations.size());
      Assert.assertTrue(operations.contains(Operation.WRITE));
      Assert.assertTrue(operations.contains(Operation.CREATE));

      operations = userIF.getAllPermissions(mdAttributeChar);
      Assert.assertEquals(2, operations.size());
      Assert.assertTrue(operations.contains(Operation.WRITE));
      Assert.assertTrue(operations.contains(Operation.READ));

      operations = userIF.getAllPermissions(mdRelationship);
      Assert.assertEquals(2, operations.size());
      Assert.assertTrue(operations.contains(Operation.DELETE));
      Assert.assertTrue(operations.contains(Operation.CREATE));

      operations = userIF.getAllPermissions(mdAttributeBool);
      Assert.assertEquals(1, operations.size());
      Assert.assertTrue(operations.contains(Operation.READ));

      operations = userIF.getAllPermissions(mdStruct);
      Assert.assertEquals(1, operations.size());
      Assert.assertTrue(operations.contains(Operation.DELETE));
    }
    catch (Throwable e)
    {
      throw new RuntimeException(e);
    }
  }

  @Request
  @Test
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
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness2.apply();

    MdAttributeConcreteDAO mdAttributeChar = TestFixtureFactory.addCharacterAttribute(mdBusiness1);
    mdAttributeChar.apply();

    MdRelationshipDAO mdRelationship = TestFixtureFactory.createMdRelationship1(mdBusiness1, mdBusiness2);
    mdRelationship.apply();

    MdAttributeConcreteDAO mdAttributeBool = TestFixtureFactory.addBooleanAttribute(mdRelationship);
    mdAttributeBool.apply();

    // Add permissions to the MdBusiness
    user.grantPermission(Operation.CREATE, mdBusiness1.getOid());
    user.grantPermission(Operation.WRITE, mdBusiness1.getOid());

    // Add attribute permissions
    user.grantPermission(Operation.WRITE, mdAttributeChar.getOid());
    user.grantPermission(Operation.READ, mdAttributeChar.getOid());

    // Add struct permissions
    MdStructDAO mdStruct = TestFixtureFactory.createMdStruct1();
    mdStruct.apply();

    user.grantPermission(Operation.DELETE, mdStruct.getOid());

    // Add permissions to a MdRelationship
    user.grantPermission(Operation.CREATE, mdRelationship.getOid());
    user.grantPermission(Operation.DELETE, mdRelationship.getOid());

    // Add permissions to an attribute defined by the MdRelationship
    user.grantPermission(Operation.READ, mdAttributeBool.getOid());

    // Export the permissions
    ExportMetadata metadata = new ExportMetadata();
    metadata.addRevokePermissions(user);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    SAXImporter.runImport(new File(tempXMLFile));

    UserDAOIF userIF = UserDAO.findUser("testUser");

    Set<RoleDAOIF> assignedRoles = userIF.assignedRoles();
    Assert.assertEquals(1, assignedRoles.size());
    Assert.assertTrue(assignedRoles.contains(role));

    Set<Operation> operations = userIF.getAllPermissions(mdBusiness1);
    Assert.assertEquals(0, operations.size());

    operations = userIF.getAllPermissions(mdAttributeChar);
    Assert.assertEquals(0, operations.size());

    operations = userIF.getAllPermissions(mdRelationship);
    Assert.assertEquals(0, operations.size());

    operations = userIF.getAllPermissions(mdAttributeBool);
    Assert.assertEquals(0, operations.size());

    operations = userIF.getAllPermissions(mdStruct);
    Assert.assertEquals(0, operations.size());
  }

  @Request
  @Test
  public void testGrantDenyPermissions()
  {
    RoleDAO role1 = TestFixtureFactory.createRole1();
    role1.apply();

    RoleDAO role2 = TestFixtureFactory.createRole2();
    role2.apply();

    role1.addAscendant(role2);

    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    // Create test MdView
    MdViewDAO mdView1 = TestFixtureFactory.createMdView1();
    mdView1.setValue(MdViewInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdView1.apply();

    // Create test MdUtil
    MdUtilDAO mdUtil1 = TestFixtureFactory.createMdUtil1();
    mdUtil1.setValue(MdUtilInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdUtil1.apply();

    // Create test MdStruct
    MdStructDAO mdStruct = TestFixtureFactory.createMdStruct1();
    mdStruct.setValue(MdStructInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdStruct.apply();

    // Add permissions to the MdBusiness
    role1.grantPermission(Operation.DENY_CREATE, mdBusiness1.getOid());
    role1.grantPermission(Operation.DENY_WRITE, mdBusiness1.getOid());
    role1.grantPermission(Operation.DENY_READ, mdView1.getOid());
    role1.grantPermission(Operation.DENY_DELETE, mdView1.getOid());
    role1.grantPermission(Operation.DENY_WRITE, mdView1.getOid());
    role1.grantPermission(Operation.DENY_CREATE, mdUtil1.getOid());
    role1.grantPermission(Operation.DENY_DELETE, mdStruct.getOid());
    role1.grantPermission(Operation.DENY_WRITE, mdStruct.getOid());
    role1.grantPermission(Operation.DENY_CREATE, mdStruct.getOid());

    // Export the permissions
    ExportMetadata metadata = new ExportMetadata();
    metadata.addCreate(role1);
    metadata.addGrantPermissions(role1);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    TestFixtureFactory.delete(role1);

    SAXImporter.runImport(new File(tempXMLFile));

    RoleDAOIF roleIF = RoleDAO.findRole("runway.testRole");

    roleIF = RoleDAO.findRole("runway.testRole");
    Assert.assertEquals("Test Role", roleIF.getStructValue(RoleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));

    Set<Operation> operations = roleIF.getAllPermissions(mdBusiness1);
    Assert.assertEquals(2, operations.size());
    Assert.assertTrue(operations.contains(Operation.DENY_CREATE));
    Assert.assertTrue(operations.contains(Operation.DENY_WRITE));

    operations = roleIF.getAllPermissions(mdView1);
    Assert.assertEquals(3, operations.size());
    Assert.assertTrue(operations.contains(Operation.DENY_READ));
    Assert.assertTrue(operations.contains(Operation.DENY_DELETE));
    Assert.assertTrue(operations.contains(Operation.DENY_WRITE));

    operations = roleIF.getAllPermissions(mdUtil1);
    Assert.assertEquals(1, operations.size());
    Assert.assertTrue(operations.contains(Operation.DENY_CREATE));

    operations = roleIF.getAllPermissions(mdStruct);
    Assert.assertEquals(3, operations.size());
    Assert.assertTrue(operations.contains(Operation.DENY_CREATE));
    Assert.assertTrue(operations.contains(Operation.DENY_DELETE));
    Assert.assertTrue(operations.contains(Operation.DENY_WRITE));

    Set<RoleDAOIF> superRoles = roleIF.getSuperRoles();
    Assert.assertEquals(1, superRoles.size());
    Assert.assertTrue(superRoles.contains(role2));
  }

  @Request
  @Test
  public void testRolePermissions()
  {
    RoleDAO role1 = TestFixtureFactory.createRole1();
    role1.apply();

    RoleDAO role2 = TestFixtureFactory.createRole2();
    role2.apply();

    role1.addAscendant(role2);

    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    // Create test MdView
    MdViewDAO mdView1 = TestFixtureFactory.createMdView1();
    mdView1.setValue(MdViewInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdView1.apply();

    // Create test MdUtil
    MdUtilDAO mdUtil1 = TestFixtureFactory.createMdUtil1();
    mdUtil1.setValue(MdViewInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdUtil1.apply();

    // Create test MdStruct
    MdStructDAO mdStruct = TestFixtureFactory.createMdStruct1();
    mdStruct.setValue(MdStructInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdStruct.apply();

    // Add permissions to the MdBusiness
    role1.grantPermission(Operation.CREATE, mdBusiness1.getOid());
    role1.grantPermission(Operation.WRITE, mdBusiness1.getOid());
    role1.grantPermission(Operation.READ, mdView1.getOid());
    role1.grantPermission(Operation.DELETE, mdView1.getOid());
    role1.grantPermission(Operation.WRITE, mdView1.getOid());
    role1.grantPermission(Operation.CREATE, mdUtil1.getOid());
    role1.grantPermission(Operation.DELETE, mdStruct.getOid());
    role1.grantPermission(Operation.WRITE, mdStruct.getOid());
    role1.grantPermission(Operation.CREATE, mdStruct.getOid());

    // Export the permissions
    ExportMetadata metadata = new ExportMetadata();
    metadata.addCreate(role1);
    metadata.addGrantPermissions(role1);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    TestFixtureFactory.delete(role1);

    SAXImporter.runImport(new File(tempXMLFile));

    RoleDAOIF roleIF = RoleDAO.findRole("runway.testRole");

    roleIF = RoleDAO.findRole("runway.testRole");
    Assert.assertEquals("Test Role", roleIF.getStructValue(RoleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));

    Set<Operation> operations = roleIF.getAllPermissions(mdBusiness1);
    Assert.assertEquals(2, operations.size());
    Assert.assertTrue(operations.contains(Operation.CREATE));
    Assert.assertTrue(operations.contains(Operation.WRITE));

    operations = roleIF.getAllPermissions(mdView1);
    Assert.assertEquals(3, operations.size());
    Assert.assertTrue(operations.contains(Operation.READ));
    Assert.assertTrue(operations.contains(Operation.DELETE));
    Assert.assertTrue(operations.contains(Operation.WRITE));

    operations = roleIF.getAllPermissions(mdUtil1);
    Assert.assertEquals(1, operations.size());
    Assert.assertTrue(operations.contains(Operation.CREATE));

    operations = roleIF.getAllPermissions(mdStruct);
    Assert.assertEquals(3, operations.size());
    Assert.assertTrue(operations.contains(Operation.CREATE));
    Assert.assertTrue(operations.contains(Operation.DELETE));
    Assert.assertTrue(operations.contains(Operation.WRITE));

    Set<RoleDAOIF> superRoles = roleIF.getSuperRoles();
    Assert.assertEquals(1, superRoles.size());
    Assert.assertTrue(superRoles.contains(role2));
  }

  @Request
  @Test
  public void testRolePermissionsAll()
  {
    RoleDAO role = TestFixtureFactory.createRole1();
    role.apply();

    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    // Export the permissions
    ExportMetadata metadata = new ExportMetadata();
    metadata.addCreate(role);
    metadata.grantAllPermissions(role, mdBusiness1);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    TestFixtureFactory.delete(role);

    SAXImporter.runImport(new File(tempXMLFile));

    RoleDAOIF roleIF = RoleDAO.findRole("runway.testRole");

    roleIF = RoleDAO.findRole("runway.testRole");
    Assert.assertEquals("Test Role", roleIF.getStructValue(RoleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));

    Set<Operation> operations = roleIF.getAllPermissions(mdBusiness1);
    Assert.assertTrue(operations.contains(Operation.CREATE));
    Assert.assertTrue(operations.contains(Operation.WRITE));
    Assert.assertTrue(operations.contains(Operation.READ));
    Assert.assertTrue(operations.contains(Operation.DELETE));
    Assert.assertTrue(operations.contains(Operation.READ_ALL));
    Assert.assertTrue(operations.contains(Operation.WRITE_ALL));
  }

  @Request
  @Test
  public void testRevokeRolePermissions()
  {
    RoleDAO role1 = TestFixtureFactory.createRole1();
    role1.apply();

    RoleDAO role2 = TestFixtureFactory.createRole2();
    role2.apply();

    role1.addAscendant(role2);

    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    // Create test MdView
    MdViewDAO mdView1 = TestFixtureFactory.createMdView1();
    mdView1.setValue(MdViewInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdView1.apply();

    // Create test MdUtil
    MdUtilDAO mdUtil1 = TestFixtureFactory.createMdUtil1();
    mdUtil1.setValue(MdUtilInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdUtil1.apply();

    // Create test MdStruct
    MdStructDAO mdStruct = TestFixtureFactory.createMdStruct1();
    mdStruct.setValue(MdStructInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdStruct.apply();

    // Add permissions to the MdBusiness
    role1.grantPermission(Operation.CREATE, mdBusiness1.getOid());
    role1.grantPermission(Operation.WRITE, mdBusiness1.getOid());
    role1.grantPermission(Operation.READ, mdView1.getOid());
    role1.grantPermission(Operation.DELETE, mdView1.getOid());
    role1.grantPermission(Operation.WRITE, mdView1.getOid());
    role1.grantPermission(Operation.CREATE, mdUtil1.getOid());
    role1.grantPermission(Operation.DELETE, mdStruct.getOid());
    role1.grantPermission(Operation.WRITE, mdStruct.getOid());
    role1.grantPermission(Operation.CREATE, mdStruct.getOid());

    // Export the permissions
    ExportMetadata metadata = new ExportMetadata();
    metadata.addRevokePermissions(role1);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    SAXImporter.runImport(new File(tempXMLFile));

    RoleDAOIF roleIF = RoleDAO.findRole("runway.testRole");

    roleIF = RoleDAO.findRole("runway.testRole");
    Assert.assertEquals("Test Role", roleIF.getStructValue(RoleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));

    Set<Operation> operations = roleIF.getAllPermissions(mdBusiness1);
    Assert.assertEquals(0, operations.size());

    operations = roleIF.getAllPermissions(mdView1);
    Assert.assertEquals(0, operations.size());

    operations = roleIF.getAllPermissions(mdUtil1);
    Assert.assertEquals(0, operations.size());

    operations = roleIF.getAllPermissions(mdStruct);
    Assert.assertEquals(0, operations.size());

    Set<RoleDAOIF> superRoles = roleIF.getSuperRoles();
    Assert.assertEquals(1, superRoles.size());
    Assert.assertTrue(superRoles.contains(role2));
  }

  @Request
  @Test
  public void testMethodPermissions()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdMethodDAO mdMethod = TestFixtureFactory.createMdMethod(mdBusiness);
    mdMethod.apply();

    MethodActorDAO methodActor = TestFixtureFactory.createMethodActor(mdMethod);
    methodActor.apply();

    RoleDAO role = TestFixtureFactory.createRole1();
    role.apply();

    role.assignMember(methodActor);

    // Create test MdView
    MdViewDAO mdView1 = TestFixtureFactory.createMdView1();
    mdView1.setValue(MdViewInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdView1.apply();

    // Create test MdUtil
    MdUtilDAO mdUtil1 = TestFixtureFactory.createMdUtil1();
    mdUtil1.setValue(MdUtilInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdUtil1.apply();

    // Create test MdStruct
    MdStructDAO mdStruct = TestFixtureFactory.createMdStruct1();
    mdStruct.setValue(MdStructInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdStruct.apply();

    // Add permissions to the MdBusiness
    role.grantPermission(Operation.EXECUTE, mdMethod.getOid());
    methodActor.grantPermission(Operation.CREATE, mdBusiness.getOid());
    methodActor.grantPermission(Operation.WRITE, mdBusiness.getOid());
    methodActor.grantPermission(Operation.READ, mdView1.getOid());
    methodActor.grantPermission(Operation.DELETE, mdView1.getOid());
    methodActor.grantPermission(Operation.WRITE, mdView1.getOid());
    methodActor.grantPermission(Operation.CREATE, mdUtil1.getOid());
    methodActor.grantPermission(Operation.DELETE, mdStruct.getOid());
    methodActor.grantPermission(Operation.WRITE, mdStruct.getOid());
    methodActor.grantPermission(Operation.WRITE, mdStruct.getOid());

    // Export the permissions
    ExportMetadata metadata = new ExportMetadata();
    metadata.addGrantPermissions(role);
    metadata.addGrantPermissions(methodActor);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    // Remove all existing permissions
    TestFixtureFactory.delete(methodActor);
    role.revokeAllPermissions(mdMethod.getOid());

    SAXImporter.runImport(new File(tempXMLFile));

    MethodActorDAOIF methodActorIF = mdMethod.getMethodActor();
    RoleDAOIF roleIF = RoleDAO.get(role.getOid());

    Set<RoleDAOIF> assignedRoles = methodActorIF.assignedRoles();
    Assert.assertEquals(1, assignedRoles.size());
    Assert.assertTrue(assignedRoles.contains(role));

    Set<Operation> operations = methodActorIF.getAllPermissions(mdBusiness);
    Assert.assertEquals(2, operations.size());
    Assert.assertTrue(operations.contains(Operation.CREATE));
    Assert.assertTrue(operations.contains(Operation.WRITE));

    operations = roleIF.getAllPermissions(mdMethod);
    Assert.assertEquals(1, operations.size());
    Assert.assertTrue(operations.contains(Operation.EXECUTE));

    operations = methodActorIF.getAllPermissions(mdView1);
    Assert.assertEquals(3, operations.size());
    Assert.assertTrue(operations.contains(Operation.READ));
    Assert.assertTrue(operations.contains(Operation.DELETE));
    Assert.assertTrue(operations.contains(Operation.WRITE));

    operations = methodActorIF.getAllPermissions(mdUtil1);
    Assert.assertEquals(1, operations.size());
    Assert.assertTrue(operations.contains(Operation.CREATE));

    operations = methodActorIF.getAllPermissions(mdStruct);
    Assert.assertEquals(2, operations.size());
    Assert.assertTrue(operations.contains(Operation.DELETE));
    Assert.assertTrue(operations.contains(Operation.WRITE));
  }

  @Request
  @Test
  public void testRevokeMethodPermissions()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdMethodDAO mdMethod = TestFixtureFactory.createMdMethod(mdBusiness);
    mdMethod.apply();

    MethodActorDAO methodActor = TestFixtureFactory.createMethodActor(mdMethod);
    methodActor.apply();

    RoleDAO role = TestFixtureFactory.createRole1();
    role.apply();

    role.assignMember(methodActor);

    // Create test MdView
    MdViewDAO mdView1 = TestFixtureFactory.createMdView1();
    mdView1.setValue(MdViewInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdView1.apply();

    // Create test MdUtil
    MdUtilDAO mdUtil1 = TestFixtureFactory.createMdUtil1();
    mdUtil1.setValue(MdViewInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdUtil1.apply();

    // Create test MdStruct
    MdStructDAO mdStruct = TestFixtureFactory.createMdStruct1();
    mdStruct.setValue(MdStructInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdStruct.apply();

    // Add permissions to the MdBusiness
    role.grantPermission(Operation.EXECUTE, mdMethod.getOid());
    methodActor.grantPermission(Operation.CREATE, mdBusiness.getOid());
    methodActor.grantPermission(Operation.WRITE, mdBusiness.getOid());
    methodActor.grantPermission(Operation.READ, mdView1.getOid());
    methodActor.grantPermission(Operation.DELETE, mdView1.getOid());
    methodActor.grantPermission(Operation.WRITE, mdView1.getOid());
    methodActor.grantPermission(Operation.CREATE, mdUtil1.getOid());
    methodActor.grantPermission(Operation.DELETE, mdStruct.getOid());
    methodActor.grantPermission(Operation.WRITE, mdStruct.getOid());
    methodActor.grantPermission(Operation.WRITE, mdStruct.getOid());

    // Export the permissions
    ExportMetadata metadata = new ExportMetadata();
    metadata.addRevokePermissions(role);
    metadata.addRevokePermissions(methodActor);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    SAXImporter.runImport(new File(tempXMLFile));

    MethodActorDAOIF methodActorIF = mdMethod.getMethodActor();
    RoleDAOIF roleIF = RoleDAO.get(role.getOid());

    Set<RoleDAOIF> assignedRoles = methodActorIF.assignedRoles();
    Assert.assertEquals(1, assignedRoles.size());
    Assert.assertTrue(assignedRoles.contains(role));

    Set<Operation> operations = methodActorIF.getAllPermissions(mdBusiness);
    Assert.assertEquals(0, operations.size());

    operations = roleIF.getAllPermissions(mdMethod);
    Assert.assertEquals(0, operations.size());

    operations = methodActorIF.getAllPermissions(mdView1);
    Assert.assertEquals(0, operations.size());

    operations = methodActorIF.getAllPermissions(mdUtil1);
    Assert.assertEquals(0, operations.size());

    operations = methodActorIF.getAllPermissions(mdStruct);
    Assert.assertEquals(0, operations.size());
  }

  @Request
  @Test
  public void testUpdateMdBusiness()
  {
    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdBusiness1.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdBusiness1.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdBusiness1.setValue(MdBusinessInfo.CACHE_SIZE, "50");
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addBooleanAttribute(mdBusiness1);
    mdAttribute.apply();

    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 });

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    mdBusiness1 = MdBusinessDAO.get(mdBusiness1.getOid()).getBusinessDAO();

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

    MdAttributeDAOIF attribute = mdBusiness1IF.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN);

    Assert.assertEquals(mdBusiness1IF.getStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdBusiness Set Test");
    Assert.assertEquals(mdBusiness1IF.getStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdBusiness Attributes Test");
    Assert.assertEquals(mdBusiness1IF.getValue(MdBusinessInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdBusiness1IF.getValue(MdBusinessInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdBusiness1IF.getValue(MdBusinessInfo.CACHE_SIZE), "50");

    // Change to false when casscading delete is implemented
    Assert.assertEquals(mdBusiness1IF.getValue(MetadataInfo.REMOVE), MdAttributeBooleanInfo.TRUE);

    // Ensure the attributes are linked to the correct MdBusiness object
    Assert.assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdBusiness1IF.getOid());
  }

  /**
   * Test setting of attributes on a boolean datatype
   */
  @Request
  @Test
  public void testRenameAttribute()
  {
    String updatedName = "updatedName";

    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

    Assert.assertEquals(attribute.getStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Boolean Set Test");
    Assert.assertEquals(attribute.getStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Positive_Label");
    Assert.assertEquals(attribute.getStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Negative_Label");

    // This will fail until the object cached is fixed. It currently holds onto
    // the old attribute definition.
    Assert.assertNull(mdEntityIF.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN));
  }

  /**
   * Test setting of attributes on a boolean datatype
   */
  @Request
  @Test
  public void testUpdateBoolean()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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
    MdAttributeBooleanDAO attribute = (MdAttributeBooleanDAO) ( mdEntityIF.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN) ).getBusinessDAO();

    Assert.assertEquals(attribute.getStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Boolean Set Test");
    Assert.assertEquals(attribute.getStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Positive_Label");
    Assert.assertEquals(attribute.getStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Negative_Label");
  }

  /**
   * Test setting of attributes on a blob datatype
   */
  @Request
  @Test
  public void testUpdateBlob()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

    Assert.assertEquals(attribute.getStructValue(MdAttributeBlobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Blob Set Test");
    Assert.assertEquals(attribute.getValue(MdAttributeBlobInfo.IMMUTABLE), MdAttributeBooleanInfo.FALSE);
    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, attribute.getValue(MdAttributeBlobInfo.REQUIRED));
    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, attribute.getValue(MdAttributeBlobInfo.REMOVE));
    Assert.assertEquals(attribute.getStructValue(MdAttributeBlobInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Blob Test");
    Assert.assertEquals(index.dereference()[0].getOid(), IndexTypes.NO_INDEX.getOid());
  }

  /**
   * Test setting of attributes on the character datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testUpdateCharacter()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdAttributeCharacterDAO mdAttribute = TestFixtureFactory.addCharacterAttribute(mdBusiness1);
    mdAttribute.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.TRUE);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Character Update Test");
    mdAttribute.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttribute.setValue(MdAttributeCharacterInfo.SIZE, "300");
    mdAttribute.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());
    mdAttribute.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute(TestFixConst.ATTRIBUTE_CHARACTER);
    AttributeEnumerationIF index = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    Assert.assertEquals(attribute.getStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Character Set Test");
    Assert.assertEquals(attribute.getValue(MdAttributeCharacterInfo.SIZE), "200");
    Assert.assertEquals(attribute.getValue(MdAttributeCharacterInfo.IMMUTABLE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(index.dereference()[0].getOid(), IndexTypes.NO_INDEX.getOid());
  }

  /**
   * Test setting of attributes on the date datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testUpdateDate()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

    Assert.assertEquals(attribute.getStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Date Set Test");
    Assert.assertEquals(attribute.getValue(MdAttributeDateInfo.REQUIRED), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(attribute.getValue(MdAttributeDateInfo.DEFAULT_VALUE), "2006-02-11");
    Assert.assertEquals(index.dereference()[0].getOid(), IndexTypes.UNIQUE_INDEX.getOid());
  }

  /**
   * Test setting of attributes on the dateTime datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testUpdateDateTime()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

    Assert.assertEquals(attribute.getStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "dateTime Set Test");
    Assert.assertEquals(index.dereference()[0].getOid(), IndexTypes.NON_UNIQUE_INDEX.getOid());
  }

  /**
   * Test setting of attributes on the decimal datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testUpdateDecimal()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

    Assert.assertEquals(attribute.getStructValue(MdAttributeDecimalInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Decimal Set Test");
    Assert.assertEquals(attribute.getValue(MdAttributeDecimalInfo.LENGTH), "10");
    Assert.assertEquals(attribute.getValue(MdAttributeDecimalInfo.DECIMAL), "2");
    Assert.assertEquals(attribute.getValue(MdAttributeDecimalInfo.REJECT_NEGATIVE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(attribute.getValue(MdAttributeDecimalInfo.REJECT_ZERO), MdAttributeBooleanInfo.FALSE);
    Assert.assertEquals(attribute.getValue(MdAttributeDecimalInfo.REJECT_POSITIVE), MdAttributeBooleanInfo.FALSE);
  }

  /**
   * Test setting of attributes on the double datatype minus any overlapping
   * attributes from the boolean and decimal test
   */
  @Request
  @Test
  public void testUpdateDouble()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

    Assert.assertEquals(attribute.getStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Double Set Test");
    Assert.assertEquals(attribute.getValue(MdAttributeDoubleInfo.LENGTH), "9");
    Assert.assertEquals(attribute.getValue(MdAttributeDoubleInfo.DECIMAL), "4");
    Assert.assertEquals(attribute.getValue(MdAttributeDoubleInfo.REJECT_ZERO), MdAttributeBooleanInfo.TRUE);
  }

  @Request
  @Test
  public void testUpdateVirtual()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdAttributeDoubleDAO concrete = TestFixtureFactory.addDoubleAttribute(mdBusiness1);
    concrete.apply();

    MdViewDAO mdView = TestFixtureFactory.createMdView1();
    mdView.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdView.apply();

    TestFixtureFactory.addVirtualAttribute(mdView, concrete).apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1, mdView }));

    SAXImporter.runImport(new File(tempXMLFile));

    MdViewDAOIF mdViewIF = MdViewDAO.getMdViewDAO(VIEW);
    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testDouble");
    MdAttributeDAOIF virtual = mdViewIF.definesAttribute("testVirtual");

    Assert.assertEquals(virtual.getValue(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE), attribute.getOid());
  }

  /**
   * Test setting of attributes on the enumeration datatype minus any
   * overlapping attributes from the boolean test As a side effect does testing
   * on setting instance/instance_value tags
   */
  @Request
  @Test
  public void testUpdateEnumeration()
  {
    MdBusinessDAO mdBusinessEnum1 = TestFixtureFactory.createEnumClass1();
    mdBusinessEnum1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    mdBusinessEnum1.apply();
    mdBusiness1.apply();

    TestFixtureFactory.addCharacterAttribute(mdBusinessEnum1).apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusinessEnum1.definesType());
    businessDAO.setValue(EnumerationMasterInfo.NAME, "CO");
    businessDAO.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Colorado");
    businessDAO.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "CO");
    businessDAO.apply();

    MdEnumerationDAO mdEnumeration = TestFixtureFactory.createMdEnumeation1(mdBusinessEnum1);
    mdEnumeration.setValue(MdEnumerationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

    Assert.assertEquals(attribute.getStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Enumeration Set Test");
    Assert.assertEquals(attribute.getValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE), MdAttributeBooleanInfo.FALSE);

    // Ensure that the enumeration is reference the correct enumeration class
    Assert.assertEquals(attribute.getValue(MdAttributeEnumerationInfo.MD_ENUMERATION), enumeration.getOid());
  }

  /**
   * Test setting of attributes on a blob datatype
   */
  @Request
  @Test
  public void testUpdateFile()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

    Assert.assertEquals(attribute.getStructValue(MdAttributeFileInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "File Set Test");
    Assert.assertEquals(attribute.getValue(MdAttributeFileInfo.IMMUTABLE), MdAttributeBooleanInfo.FALSE);
    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, attribute.getValue(MdAttributeFileInfo.REQUIRED));
    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, attribute.getValue(MdAttributeFileInfo.REMOVE));
    Assert.assertEquals(attribute.getStructValue(MdAttributeFileInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "File Test");
    Assert.assertEquals(index.dereference()[0].getOid(), IndexTypes.NO_INDEX.getOid());
  }

  /**
   * Test setting of attributes on the character datatype minus any overlapping
   * attributes from the boolean, decimal, and double test
   */
  @Request
  @Test
  public void testUpdateFloat()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

    Assert.assertEquals(attribute.getStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Float Set Test");
    Assert.assertEquals(attribute.getValue(MdAttributeFloatInfo.LENGTH), "10");
    Assert.assertEquals(attribute.getValue(MdAttributeFloatInfo.DECIMAL), "2");
    Assert.assertEquals(attribute.getValue(MdAttributeFloatInfo.REJECT_POSITIVE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(attribute.getValue(MdAttributeFloatInfo.REJECT_NEGATIVE), MdAttributeBooleanInfo.TRUE);
  }

  /**
   * Test setting of mdHash attribute specific values
   */
  @Request
  @Test
  public void testUpdateHash()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addHashAttribute(mdBusiness1);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeHashInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Hash Update Test");
    mdAttribute.setValue(MdAttributeHashInfo.HASH_METHOD, HashMethods.SHA.getOid());
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeHashDAOIF attribute = (MdAttributeHashDAOIF) mdEntityIF.definesAttribute("testHash");

    // Ensure that the hash encryption method is set
    AttributeEnumerationIF method = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeHashInfo.HASH_METHOD);
    Assert.assertEquals(HashMethods.MD5.getOid(), method.dereference()[0].getOid());
  }

  /**
   * Test setting of attributes on the integer datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testUpdateInteger()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

    Assert.assertEquals(attribute.getStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Integer Set Test");
    Assert.assertEquals(attribute.getValue(MdAttributeIntegerInfo.REJECT_POSITIVE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(attribute.getValue(MdAttributeIntegerInfo.REJECT_ZERO), MdAttributeBooleanInfo.TRUE);
  }

  /**
   * Test setting of attributes on the long datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testUpdateLong()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

    Assert.assertEquals(attribute.getStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Long Set Test");
    Assert.assertEquals(attribute.getValue(MdAttributeLongInfo.REJECT_ZERO), MdAttributeBooleanInfo.TRUE);
  }

  /**
   * Test setting of attributes on the reference datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testUpdateReference()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

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

    Assert.assertEquals(attribute.getStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Reference Test");

    // Ensure that the reference is referencing the correct class
    Assert.assertEquals(attribute.getValue(MdAttributeReferenceInfo.REF_MD_ENTITY), mdBusinessIF.getOid());
  }

  /**
   * Test setting of attributes on the reference datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testUpdateMultiReference()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    mdBusiness1.apply();
    mdBusiness2.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addMultiReferenceAttribute(mdBusiness1, mdBusiness2);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeMultiReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference Update Test");
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute(mdAttribute.definesAttribute());

    try
    {
      MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(mdBusiness2.definesType());

      String actual = attribute.getStructValue(MdAttributeMultiReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE);
      Assert.assertEquals("Term Test", actual);

      // Ensure that the reference is referencing the correct class
      Assert.assertEquals(attribute.getValue(MdAttributeMultiReferenceInfo.REF_MD_ENTITY), mdBusinessIF.getOid());
    }
    finally
    {
      TestFixtureFactory.delete(attribute);
    }
  }

  /**
   * Test setting of attributes on the reference datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testUpdateMultiTerm()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    MdTermDAO mdTerm = TestFixtureFactory.createMdTerm();
    mdTerm.setValue(MdTermInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    mdBusiness1.apply();
    mdTerm.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addMultiTermAttribute(mdBusiness1, mdTerm);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeMultiTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference Update Test");
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute(mdAttribute.definesAttribute());

    try
    {
      MdTermDAOIF mdTermIF = MdTermDAO.getMdTermDAO(mdTerm.definesType());

      String actual = attribute.getStructValue(MdAttributeMultiTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE);
      Assert.assertEquals("Term Test", actual);

      // Ensure that the reference is referencing the correct class
      Assert.assertEquals(attribute.getValue(MdAttributeMultiTermInfo.REF_MD_ENTITY), mdTermIF.getOid());
    }
    finally
    {
      TestFixtureFactory.delete(attribute);
    }
  }

  /**
   * Test setting of attributes on the reference datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testUpdateTerm()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdTermDAO mdTerm = TestFixtureFactory.createMdTerm();
    mdTerm.setValue(MdTermInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTerm.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addTermAttribute(mdBusiness1, mdTerm);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Update Test");
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdBusinessDAO.getMdElementDAO(mdBusiness1.definesType());
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testTerm");

    MdTermDAOIF mdTermIF = MdTermDAO.getMdTermDAO(mdTerm.definesType());

    Assert.assertEquals(attribute.getStructValue(MdAttributeTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Term Test");

    // Ensure that the reference is referencing the correct class
    Assert.assertEquals(attribute.getValue(MdAttributeTermInfo.REF_MD_ENTITY), mdTermIF.getOid());
  }

  /**
   * Test setting of attributes on the struct datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testUpdateStruct()
  {
    MdStructDAO mdStruct = TestFixtureFactory.createMdStruct1();
    mdStruct.setValue(MdBusinessInfo.CACHE_SIZE, "525");
    mdStruct.setValue(MdStructInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdStruct.apply();

    MdAttributeConcreteDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdStruct);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFAULT_VALUE, MdAttributeBooleanInfo.FALSE);
    mdAttributeBoolean.apply();

    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addStructAttribute(mdBusiness1, mdStruct);
    mdAttribute.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO.setStructValue("testStruct", TestFixConst.ATTRIBUTE_BOOLEAN, MdAttributeBooleanInfo.TRUE);
    businessDAO.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Struct Update Test");
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF mdAttributeIF = mdEntityIF.definesAttribute("testStruct");
    MdStructDAOIF mdStructIF = MdStructDAO.getMdStructDAO(mdStruct.definesType());

    Assert.assertEquals(mdAttributeIF.getStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Struct Set Test");
    Assert.assertEquals(mdStructIF.getValue(MdBusinessInfo.CACHE_SIZE), "525");

    // Ensure that the correct class is being referenced
    Assert.assertEquals(mdAttributeIF.getValue(MdAttributeStructInfo.MD_STRUCT), mdStructIF.getOid());

    List<String> ids = EntityDAO.getEntityIdsDB(CLASS);

    Assert.assertEquals(1, ids.size());

    BusinessDAOIF businessDAOIF = BusinessDAO.get(ids.get(0));
    AttributeStruct attribute = (AttributeStruct) businessDAOIF.getAttributeIF("testStruct");

    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, attribute.getValue(TestFixConst.ATTRIBUTE_BOOLEAN));
  }

  /**
   * Test setting of symmetric attribute specific value.
   */
  @Request
  @Test
  public void testUpdateSymmetric()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addSymmetricAttribute(mdBusiness1);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeSymmetricInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Symmetric Update TEST");
    mdAttribute.setValue(MdAttributeSymmetricInfo.SECRET_KEY_SIZE, "256");
    mdAttribute.setValue(MdAttributeSymmetricInfo.SYMMETRIC_METHOD, SymmetricMethods.AES.getOid());
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeSymmetricDAOIF attribute = (MdAttributeSymmetricDAOIF) mdEntityIF.definesAttribute("testSymmetric");

    // Ensure that the symmetric encryption method is set
    AttributeEnumerationIF method = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeSymmetricInfo.SYMMETRIC_METHOD);
    Assert.assertEquals(SymmetricMethods.DES.getOid(), method.dereference()[0].getOid());
    Assert.assertEquals("56", attribute.getValue(MdAttributeSymmetricInfo.SECRET_KEY_SIZE));
  }

  /**
   * Test updating of attributes on the text datatype minus any overlapping
   * attributes from the boolean test.
   */
  @Request
  @Test
  public void testUpdateClob()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addTextAttribute(mdBusiness1);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Text Update Test");
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testText");

    Assert.assertEquals(attribute.getStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Text Set Test");
  }

  /**
   * Test updating of attributes on the clob datatype minus any overlapping
   * attributes from the boolean test.
   */
  @Request
  @Test
  public void testUpdateText()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addClobAttribute(mdBusiness1);
    mdAttribute.apply();

    SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 }));

    mdAttribute.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Clob Update Test");
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testClob");

    Assert.assertEquals(attribute.getStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Clob Set Test");
  }

  /**
   * Test setting of attributes on the time datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testUpdateTime()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addTimeAttribute(mdBusiness1);
    mdAttribute.apply();

    MdAttributeTimeDAO mdAttribute2 = MdAttributeTimeDAO.newInstance();
    mdAttribute2.setValue(MdAttributeTimeInfo.NAME, "testTime2");
    mdAttribute2.setStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Time Set Test2");
    mdAttribute2.setValue(MdAttributeTimeInfo.DEFINING_MD_CLASS, mdBusiness1.getOid());

    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness1 });
    metadata.addNewMdAttribute(mdBusiness1, mdAttribute2);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    mdAttribute.setStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Time Update Test");
    mdAttribute.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute("testTime");

    Assert.assertEquals(attribute.getStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Time Set Test");
    Assert.assertEquals(mdEntityIF.definesAttribute("testTime2").getStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Time Set Test2");
  }

  /**
   * Test setting of attributes of and on the relationship datatype
   */
  @Request
  @Test
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
    MdAttributeDAOIF attribute = mdRelationship.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN);
    MdAttributeDAOIF attributeCharacter = mdRelationship.definesAttribute(TestFixConst.ATTRIBUTE_CHARACTER);

    Assert.assertEquals("Relationship Set Test", mdRelationship.getStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    Assert.assertEquals("Relationship Test", mdRelationship.getStructValue(MdRelationshipInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
    Assert.assertEquals(MdAttributeBooleanInfo.FALSE, mdRelationship.getValue(MdRelationshipInfo.REMOVE));
    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, mdRelationship.getValue(MdRelationshipInfo.EXTENDABLE));
    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, mdRelationship.getValue(MdRelationshipInfo.ABSTRACT));

    // Ensure the parent attributes are correctly set
    Assert.assertEquals(mdRelationship.getValue(MdRelationshipInfo.PARENT_MD_BUSINESS), mdBusiness1.getOid());
    Assert.assertEquals(mdRelationship.getValue(MdRelationshipInfo.PARENT_CARDINALITY), "1");
    Assert.assertEquals(mdRelationship.getStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Parent Set Test");

    // Ensure the child attributes are correctly set
    Assert.assertEquals(mdRelationship.getValue(MdRelationshipInfo.CHILD_MD_BUSINESS), mdBusiness2.getOid());
    Assert.assertEquals(mdRelationship.getValue(MdRelationshipInfo.CHILD_CARDINALITY), "*");
    Assert.assertEquals(mdRelationship.getStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Child Set Test");

    // Ensure the attributes are linked to the relationship
    Assert.assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdRelationship.getOid());
    Assert.assertEquals(attributeCharacter.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdRelationship.getOid());

    mdRelationship.setValue(MetadataInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdRelationship.apply();

    // Test the sub relationship
    MdRelationshipDAO mdRelationship2 = MdRelationshipDAO.getMdRelationshipDAO(RELATIONSHIP2).getBusinessDAO();
    Assert.assertEquals(mdRelationship.getOid(), mdRelationship2.getValue(MdRelationshipInfo.SUPER_MD_RELATIONSHIP));
    Assert.assertEquals("2", mdRelationship2.getValue(MdRelationshipInfo.CHILD_CARDINALITY));
  }

  /**
   * Test setting of attributes of and on the struct datatype,
   */
  @Request
  @Test
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
    MdAttributeDAOIF attribute = mdStruct.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN);
    MdAttributeDAOIF attributeCharacter = mdStruct.definesAttribute(TestFixConst.ATTRIBUTE_CHARACTER);

    Assert.assertEquals("Struct Set Test", mdStruct.getStructValue(MdStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    Assert.assertEquals("Set Struct Attributes Test", mdStruct.getStructValue(MdStructInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, mdStruct.getValue(MdStructInfo.REMOVE));

    // Ensure the attributes are linked to the struct
    Assert.assertEquals(mdStruct.getOid(), attribute.getValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS));
    Assert.assertEquals(mdStruct.getOid(), attributeCharacter.getValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS));
  }

  /**
   * Test setting of attributes of and on the class datatype
   */
  @Request
  @Test
  public void testUpdateMdException()
  {
    // Create test MdBusiness
    MdExceptionDAO mdException1 = TestFixtureFactory.createMdException1();
    mdException1.setValue(MdExceptionInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdException1.setValue(MdExceptionInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdException1.setValue(MdExceptionInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdException1.setValue(MdExceptionInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdException1.setValue(MdExceptionInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdException1.apply();

    MdAttributeConcreteDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdException1);

    MdExceptionDAO mdException2 = TestFixtureFactory.createMdException2();
    mdException2.setValue(MdExceptionInfo.SUPER_MD_EXCEPTION, mdException1.getOid());
    mdException2.setValue(MdExceptionInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdException2.setValue(MdExceptionInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdException2.apply();

    // Export the test entities
    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdException1, mdException2 });
    metadata.addNewMdAttribute(mdException1, mdAttributeBoolean);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    // Import the test entites
    SAXImporter.runImport(new File(tempXMLFile));

    MdExceptionDAOIF mdException1IF = MdExceptionDAO.getMdException(EXCEPTION);
    MdExceptionDAOIF mdException2IF = MdExceptionDAO.getMdException(EXCEPTION2);

    MdAttributeDAOIF attribute = mdException1IF.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN);

    Assert.assertEquals(mdException1IF.getStructValue(MdElementInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdException Set Test");
    Assert.assertEquals(mdException1IF.getStructValue(MdElementInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdException Attributes Test");
    Assert.assertEquals(mdException1IF.getValue(MdElementInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdException1IF.getValue(MdElementInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(MdAttributeBooleanInfo.FALSE, mdException1IF.getValue(MdElementInfo.PUBLISH));

    // Change to false when casscading delete is implemented
    Assert.assertEquals(mdException1IF.getValue(MdElementInfo.REMOVE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdException2IF.getValue(MdElementInfo.EXTENDABLE), MdAttributeBooleanInfo.FALSE);

    // Ensure inheritance is linking to the correct super class
    Assert.assertEquals(mdException2IF.getValue(MdExceptionInfo.SUPER_MD_EXCEPTION), mdException1IF.getOid());

    // Ensure the attributes are linked to the correct MdBusiness object
    Assert.assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdException1IF.getOid());
  }

  @Request
  @Test
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
    mdProblem2.setValue(MdProblemInfo.SUPER_MD_PROBLEM, mdProblem1.getOid());
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

    MdAttributeDAOIF attribute = mdProblem1IF.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN);

    Assert.assertEquals(mdProblem1IF.getStructValue(MdProblemInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdProblem Set Test");
    Assert.assertEquals(mdProblem1IF.getStructValue(MdProblemInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdProblem Attributes Test");
    Assert.assertEquals(mdProblem1IF.getValue(MdProblemInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdProblem1IF.getValue(MdProblemInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(MdAttributeBooleanInfo.FALSE, mdProblem1IF.getValue(MdProblemInfo.PUBLISH));

    // Change to false when casscading delete is implemented
    Assert.assertEquals(mdProblem1IF.getValue(MdProblemInfo.REMOVE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdProblem2IF.getValue(MdProblemInfo.EXTENDABLE), MdAttributeBooleanInfo.FALSE);

    // Ensure inheritance is linking to the correct super class
    Assert.assertEquals(mdProblem2IF.getValue(MdProblemInfo.SUPER_MD_PROBLEM), mdProblem1IF.getOid());

    // Ensure the attributes are linked to the correct MdBusiness object
    Assert.assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdProblem1IF.getOid());
  }

  @Request
  @Test
  public void testUpdateMdInformation()
  {
    // Create test MdBusiness
    MdInformationDAO mdInformation1 = TestFixtureFactory.createMdInformation1();
    mdInformation1.setValue(MdInformationInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdInformation1.setValue(MdInformationInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdInformation1.setValue(MdInformationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdInformation1.setValue(MdInformationInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdInformation1.setValue(MdInformationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdInformation1.apply();

    MdAttributeConcreteDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdInformation1);

    MdInformationDAO mdInformation2 = TestFixtureFactory.createMdInformation2();
    mdInformation2.setValue(MdInformationInfo.SUPER_MD_INFORMATION, mdInformation1.getOid());
    mdInformation2.setValue(MdInformationInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdInformation2.setValue(MdInformationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdInformation2.apply();

    // Export the test entities
    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdInformation1, mdInformation2 });
    metadata.addNewMdAttribute(mdInformation1, mdAttributeBoolean);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    // Import the test entites
    SAXImporter.runImport(new File(tempXMLFile));

    MdInformationDAOIF mdInformation1IF = MdInformationDAO.getMdInformation(INFORMATION);
    MdInformationDAOIF mdInformation2IF = MdInformationDAO.getMdInformation(INFORMATION2);

    MdAttributeDAOIF attribute = mdInformation1IF.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN);

    Assert.assertEquals(mdInformation1IF.getStructValue(MdInformationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdInformation Set Test");
    Assert.assertEquals(mdInformation1IF.getStructValue(MdInformationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdInformation Attributes Test");
    Assert.assertEquals(mdInformation1IF.getValue(MdInformationInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdInformation1IF.getValue(MdInformationInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(MdAttributeBooleanInfo.FALSE, mdInformation1IF.getValue(MdInformationInfo.PUBLISH));

    // Change to false when casscading delete is implemented
    Assert.assertEquals(mdInformation1IF.getValue(MdInformationInfo.REMOVE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdInformation2IF.getValue(MdInformationInfo.EXTENDABLE), MdAttributeBooleanInfo.FALSE);

    // Ensure inheritance is linking to the correct super class
    Assert.assertEquals(mdInformation2IF.getValue(MdInformationInfo.SUPER_MD_INFORMATION), mdInformation1IF.getOid());

    // Ensure the attributes are linked to the correct MdBusiness object
    Assert.assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdInformation1IF.getOid());
  }

  @Request
  @Test
  public void testUpdateMdView()
  {
    // Create test MdView
    MdViewDAO mdView1 = TestFixtureFactory.createMdView1();
    mdView1.setValue(MdViewInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdView1.setValue(MdViewInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdView1.setValue(MdViewInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdView1.setValue(MdViewInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdView1.setValue(MdViewInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdView1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addBooleanAttribute(mdView1);

    MdViewDAO mdView2 = TestFixtureFactory.createMdView2();
    mdView2.setValue(MdViewInfo.SUPER_MD_VIEW, mdView1.getOid());
    mdView2.setValue(MdViewInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdView2.setValue(MdViewInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdView2.apply();

    // Export the test entities
    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdView1, mdView2 });
    metadata.addNewMdAttribute(mdView1, mdAttribute);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    // Import the test entites
    SAXImporter.runImport(new File(tempXMLFile));

    MdViewDAOIF mdView1IF = MdViewDAO.getMdViewDAO(mdView1.definesType());
    MdViewDAOIF mdView2IF = MdViewDAO.getMdViewDAO(mdView2.definesType());

    MdAttributeDAOIF attribute = mdView1IF.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN);

    Assert.assertEquals(mdView1IF.getStructValue(MdViewInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdException Set Test");
    Assert.assertEquals(mdView1IF.getStructValue(MdViewInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdException Attributes Test");
    Assert.assertEquals(mdView1IF.getValue(MdViewInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdView1IF.getValue(MdViewInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(MdAttributeBooleanInfo.FALSE, mdView1IF.getValue(MdViewInfo.PUBLISH));

    // Change to false when casscading delete is implemented
    Assert.assertEquals(mdView1IF.getValue(MdViewInfo.REMOVE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdView2IF.getValue(MdViewInfo.EXTENDABLE), MdAttributeBooleanInfo.FALSE);

    // Ensure inheritance is linking to the correct super class
    Assert.assertEquals(mdView2IF.getValue(MdViewInfo.SUPER_MD_VIEW), mdView1IF.getOid());

    // Ensure the attributes are linked to the correct MdBusiness object
    Assert.assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdView1IF.getOid());
  }

  @Request
  @Test
  public void testUpdateMdUtil()
  {
    // Create test MdView
    MdUtilDAO mdUtil1 = TestFixtureFactory.createMdUtil1();
    mdUtil1.setValue(MdUtilInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdUtil1.setValue(MdUtilInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdUtil1.setValue(MdUtilInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdUtil1.setValue(MdUtilInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdUtil1.setValue(MdUtilInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdUtil1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addBooleanAttribute(mdUtil1);

    MdUtilDAO mdUtil2 = TestFixtureFactory.createMdUtil2();
    mdUtil2.setValue(MdUtilInfo.SUPER_MD_UTIL, mdUtil1.getOid());
    mdUtil2.setValue(MdUtilInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdUtil2.setValue(MdUtilInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdUtil2.apply();

    // Export the test entities
    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdUtil1, mdUtil2 });
    metadata.addNewMdAttribute(mdUtil1, mdAttribute);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    // Import the test entites
    SAXImporter.runImport(new File(tempXMLFile));

    MdUtilDAOIF mdUtil1IF = MdUtilDAO.getMdUtil(mdUtil1.definesType());
    MdUtilDAOIF mdUtil2IF = MdUtilDAO.getMdUtil(mdUtil2.definesType());

    MdAttributeDAOIF attribute = mdUtil1IF.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN);

    Assert.assertEquals(mdUtil1IF.getStructValue(MdUtilInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdException Set Test");
    Assert.assertEquals(mdUtil1IF.getStructValue(MdUtilInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdException Attributes Test");
    Assert.assertEquals(mdUtil1IF.getValue(MdUtilInfo.EXTENDABLE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdUtil1IF.getValue(MdUtilInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(MdAttributeBooleanInfo.FALSE, mdUtil1IF.getValue(MdUtilInfo.PUBLISH));

    // Change to false when casscading delete is implemented
    Assert.assertEquals(mdUtil1IF.getValue(MdUtilInfo.REMOVE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(mdUtil2IF.getValue(MdUtilInfo.EXTENDABLE), MdAttributeBooleanInfo.FALSE);

    // Ensure inheritance is linking to the correct super class
    Assert.assertEquals(mdUtil2IF.getValue(MdUtilInfo.SUPER_MD_UTIL), mdUtil1IF.getOid());

    // Ensure the attributes are linked to the correct MdBusiness object
    Assert.assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdUtil1IF.getOid());
  }

  @Request
  @Test
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
    mdEnumeration2.addEnumItem(items.get(0).getOid());

    SAXImporter.runImport(new File(tempXMLFile));

    mdEnumeration = MdEnumerationDAO.getMdEnumerationDAO(FILTER).getBusinessDAO();
    mdEnumeration2 = MdEnumerationDAO.getMdEnumerationDAO(FILTER2).getBusinessDAO();

    try
    {
      items = mdEnumeration.getAllEnumItems();

      Assert.assertEquals(mdEnumeration.getStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Enumeration Filter Test");
      Assert.assertEquals(mdEnumeration.getStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Filter Set Test");
      Assert.assertEquals(mdEnumeration.getValue(MdEnumerationInfo.REMOVE), MdAttributeBooleanInfo.FALSE);
      Assert.assertEquals(mdEnumeration.getValue(MdEnumerationInfo.INCLUDE_ALL), MdAttributeBooleanInfo.FALSE);

      // Check that the instance of the filter are correctly mapped
      Assert.assertEquals(2, items.size());
      BusinessDAOIF item = items.get(0);
      Assert.assertTrue(item.getValue("testChar").equals("CO") || items.get(0).getValue("testChar").equals("NY"));
      Assert.assertEquals(mdEnumeration2.getValue(MdEnumerationInfo.INCLUDE_ALL), MdAttributeBooleanInfo.TRUE);
    }
    finally
    {
      mdEnumeration.setValue(MdEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdEnumeration.apply();
    }
  }

  /**
   * Test creating Instances that reference other instances
   */
  @Request
  @Test
  public void testUpdateObject()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    mdBusiness1.apply();
    mdBusiness2.apply();

    TestFixtureFactory.addCharacterAttribute(mdBusiness1).apply();
    TestFixtureFactory.addBooleanAttribute(mdBusiness1).apply();
    TestFixtureFactory.addReferenceAttribute(mdBusiness2, mdBusiness1).apply();

    BusinessDAO businessDAO1 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO1.setValue(TestFixConst.ATTRIBUTE_BOOLEAN, MdAttributeBooleanInfo.TRUE);
    businessDAO1.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "3");
    businessDAO1.apply();

    BusinessDAO businessDAO2 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO2.setValue(TestFixConst.ATTRIBUTE_BOOLEAN, MdAttributeBooleanInfo.FALSE);
    businessDAO2.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Who are you?");
    businessDAO2.apply();

    BusinessDAO businessDAO3 = BusinessDAO.newInstance(mdBusiness2.definesType());
    businessDAO3.setValue("testReference", businessDAO1.getOid());
    businessDAO3.apply();

    BusinessDAO businessDAO4 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO4.setValue(TestFixConst.ATTRIBUTE_BOOLEAN, MdAttributeBooleanInfo.TRUE);
    businessDAO4.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Delete");
    String oid = businessDAO4.apply();

    ExportMetadata metadata = new ExportMetadata();
    metadata.addDelete(businessDAO4);
    metadata.addUpdate(businessDAO2);
    metadata.addUpdate(businessDAO3);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    businessDAO2.setValue(TestFixConst.ATTRIBUTE_BOOLEAN, MdAttributeBooleanInfo.TRUE);
    businessDAO2.apply();

    businessDAO3.setValue("testReference", businessDAO2.getOid());
    businessDAO3.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    // Get the ids of the CLASS1
    List<String> classIds = EntityDAO.getEntityIdsDB(CLASS);

    // Get the first instance of CLASS2
    List<String> class2Ids = EntityDAO.getEntityIdsDB(CLASS2);

    BusinessDAOIF class2 = BusinessDAO.get(class2Ids.get(0));

    // Assert that the values of refTest refer to instances of CLASS
    Assert.assertTrue(classIds.contains(class2.getValue("testReference")));

    try
    {
      BusinessDAO.get(oid);

      Assert.fail("SAXImporter did not delete the businessDAO with the oid [" + oid + "]");
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }
  }

  /**
   * Test creating Instances that reference other instances
   */
  @Request
  @Test
  public void testRekeyObject()
  {
    final String newKey = "newKey";

    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    mdBusiness1.apply();
    mdBusiness2.apply();

    TestFixtureFactory.addCharacterAttribute(mdBusiness1).apply();
    TestFixtureFactory.addBooleanAttribute(mdBusiness1).apply();
    TestFixtureFactory.addReferenceAttribute(mdBusiness2, mdBusiness1).apply();

    BusinessDAO businessDAO1 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO1.setValue(TestFixConst.ATTRIBUTE_BOOLEAN, MdAttributeBooleanInfo.TRUE);
    businessDAO1.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "3");
    businessDAO1.apply();

    BusinessDAO businessDAO2 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO2.setValue(TestFixConst.ATTRIBUTE_BOOLEAN, MdAttributeBooleanInfo.FALSE);
    businessDAO2.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Who are you?");
    businessDAO2.apply();

    BusinessDAO businessDAO3 = BusinessDAO.newInstance(mdBusiness2.definesType());
    businessDAO3.setValue("testReference", businessDAO1.getOid());
    businessDAO3.apply();

    BusinessDAO businessDAO4 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO4.setValue(TestFixConst.ATTRIBUTE_BOOLEAN, MdAttributeBooleanInfo.TRUE);
    businessDAO4.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Delete");
    businessDAO4.apply();

    ExportMetadata metadata = new ExportMetadata();
    metadata.addDelete(businessDAO4);
    metadata.addUpdate(businessDAO2);
    metadata.addUpdate(businessDAO3);
    metadata.rekeyEntity(businessDAO2, newKey);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    businessDAO2.setValue(TestFixConst.ATTRIBUTE_BOOLEAN, MdAttributeBooleanInfo.TRUE);
    businessDAO2.apply();

    businessDAO3.setValue("testReference", businessDAO2.getOid());
    businessDAO3.apply();

    SAXImporter.runImport(new File(tempXMLFile));

    Assert.assertNotNull(EntityDAO.get(mdBusiness1.definesType(), newKey));
  }

  @Request
  @Test
  public void testUpdateRelationship()
  {
    // Create the Metadata entities
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    mdBusiness1.apply();
    mdBusiness2.apply();

    MdRelationshipDAO mdRelationship1 = TestFixtureFactory.createMdRelationship1(mdBusiness1, mdBusiness2);
    mdRelationship1.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
    mdRelationship1.setValue(MdRelationshipInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdRelationship1.apply();

    TestFixtureFactory.addBooleanAttribute(mdRelationship1).apply();

    // Create BusinessDAO for the RelationshipDAO
    BusinessDAO businessDAO1 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO1.apply();

    BusinessDAO businessDAO2 = BusinessDAO.newInstance(mdBusiness2.definesType());
    businessDAO2.apply();

    // Create Test RelationshipDAO
    RelationshipDAO relationshipDAO1 = RelationshipDAO.newInstance(businessDAO1.getOid(), businessDAO2.getOid(), mdRelationship1.definesType());
    relationshipDAO1.setValue(TestFixConst.ATTRIBUTE_BOOLEAN, MdAttributeBooleanInfo.TRUE);
    relationshipDAO1.apply();

    RelationshipDAO relationshipDAO2 = RelationshipDAO.newInstance(businessDAO1.getOid(), businessDAO2.getOid(), mdRelationship1.definesType());
    relationshipDAO2.setValue(TestFixConst.ATTRIBUTE_BOOLEAN, MdAttributeBooleanInfo.TRUE);
    String oid = relationshipDAO2.apply();

    Assert.assertEquals(2, MdRelationshipDAO.getEntityIdsDB(mdRelationship1.definesType()).size());

    // Export the test entities
    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { relationshipDAO1 });
    metadata.addDelete(relationshipDAO2);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    relationshipDAO1.setValue(TestFixConst.ATTRIBUTE_BOOLEAN, MdAttributeBooleanInfo.FALSE);
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
    Assert.assertEquals(c1.getOid(), r1.getParentOid());
    // Ensure that the child reference the instance of CLASS2
    Assert.assertEquals(c2.getOid(), r1.getChildOid());
    // Ensure that the value of testBoolean is true
    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, r1.getValue(TestFixConst.ATTRIBUTE_BOOLEAN));

    try
    {
      RelationshipDAO.get(oid);

      Assert.fail("SAXImporter did not delete the RelationshipDAO with the oid [" + oid + "]");
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }

    Assert.assertEquals(1, MdRelationshipDAO.getEntityIdsDB(mdRelationship1.definesType()).size());
  }

  @Request
  @Test
  public void testRekeyRelationship()
  {
    final String newKey = "newKey";

    // Create the Metadata entities
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    mdBusiness1.apply();
    mdBusiness2.apply();

    MdRelationshipDAO mdRelationship1 = TestFixtureFactory.createMdRelationship1(mdBusiness1, mdBusiness2);
    mdRelationship1.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
    mdRelationship1.setValue(MdRelationshipInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdRelationship1.apply();

    TestFixtureFactory.addBooleanAttribute(mdRelationship1).apply();

    // Create BusinessDAO for the RelationshipDAO
    BusinessDAO businessDAO1 = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO1.apply();

    BusinessDAO businessDAO2 = BusinessDAO.newInstance(mdBusiness2.definesType());
    businessDAO2.apply();

    // Create Test RelationshipDAO
    RelationshipDAO relationshipDAO1 = RelationshipDAO.newInstance(businessDAO1.getOid(), businessDAO2.getOid(), mdRelationship1.definesType());
    relationshipDAO1.setValue(TestFixConst.ATTRIBUTE_BOOLEAN, MdAttributeBooleanInfo.TRUE);
    relationshipDAO1.apply();

    // Export the test entities
    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { relationshipDAO1 });
    metadata.rekeyEntity(relationshipDAO1, newKey);

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    SAXImporter.runImport(new File(tempXMLFile));

    Assert.assertNotNull(RelationshipDAO.get(mdRelationship1.definesType(), newKey));
  }

  @Request
  @Test
  public void testReadAndWriteAllPermissions()
  {
    SAXImporter.runImport(new File(TEST_ALL_PERMISSIONS));

    try
    {
      RoleDAOIF role = RoleDAO.findRole("runway.testRole");
      MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO(CLASS);
      MdRelationshipDAOIF mdRelationship = MdRelationshipDAO.getMdRelationshipDAO(RELATIONSHIP);

      MetadataDAOIF[] metadatas = { mdBusiness, mdRelationship };

      for (MetadataDAOIF metadata : metadatas)
      {
        Set<Operation> permissions = role.getAllPermissions(metadata);

        Assert.assertTrue(permissions.contains(Operation.READ));
        Assert.assertTrue(permissions.contains(Operation.READ_ALL));
        Assert.assertTrue(permissions.contains(Operation.WRITE));
        Assert.assertTrue(permissions.contains(Operation.WRITE_ALL));
      }

    }
    finally
    {
      TestFixtureFactory.delete(RoleDAO.findRole("runway.testRole"));
    }

  }

  @Request
  @Test
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

      MetadataDAOIF[] metadatas = { mdBusiness, mdRelationship };

      for (RoleDAOIF role : roles)
      {

        for (MetadataDAOIF metadata : metadatas)
        {
          Set<Operation> permissions = role.getAllPermissions(metadata);

          Assert.assertTrue(permissions.contains(Operation.READ));
          Assert.assertTrue(permissions.contains(Operation.READ_ALL));
          Assert.assertTrue(permissions.contains(Operation.WRITE));
          Assert.assertTrue(permissions.contains(Operation.WRITE_ALL));
        }
      }

    }
    finally
    {
      TestFixtureFactory.delete(RoleDAO.findRole("runway.testRole"));
      TestFixtureFactory.delete(RoleDAO.findRole("runway.testRole2"));
    }

  }

  @Request
  @Test
  public void testMultiUserPermissions()
  {
    SAXImporter.runImport(new File(TEST_MULTI_USER_PERMISSIONS));

    try
    {
      UserDAOIF user1 = UserDAO.findUser("testUser");
      UserDAOIF user2 = UserDAO.findUser("testUser2");

      UserDAOIF[] users = { user1, user2 };

      MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO(CLASS);
      MdRelationshipDAOIF mdRelationship = MdRelationshipDAO.getMdRelationshipDAO(RELATIONSHIP);

      MetadataDAOIF[] metadatas = { mdBusiness, mdRelationship };

      for (UserDAOIF user : users)
      {

        for (MetadataDAOIF metadata : metadatas)
        {
          Set<Operation> permissions = user.getAllPermissions(metadata);

          Assert.assertTrue(permissions.contains(Operation.READ));
          Assert.assertTrue(permissions.contains(Operation.READ_ALL));
          Assert.assertTrue(permissions.contains(Operation.WRITE));
          Assert.assertTrue(permissions.contains(Operation.WRITE_ALL));
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
  @Request
  @Test
  public void testImportObjectsWithSameKey()
  {
    String KEY = "KEY";

    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    try
    {
      MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
      mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness2.apply();

      try
      {
        MdAttributeReferenceDAO mdAttribute = TestFixtureFactory.addReferenceAttribute(mdBusiness1, mdBusiness2);
        mdAttribute.apply();

        // Create the referenced object
        BusinessDAO referenceDAO = BusinessDAO.newInstance(mdBusiness2.definesType());
        referenceDAO.setKey(KEY);
        referenceDAO.apply();

        // Create the business object
        BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness1.definesType());
        businessDAO.setValue("testReference", referenceDAO.getOid());
        businessDAO.setKey(KEY);
        businessDAO.apply();

        SAXExporter.export(tempXMLFile, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { businessDAO, referenceDAO }));

        TestFixtureFactory.delete(businessDAO);
        TestFixtureFactory.delete(referenceDAO);

        SAXImporter.runImport(new File(tempXMLFile));

        Assert.assertNotNull(BusinessDAO.get(mdBusiness1.definesType(), KEY));
        Assert.assertNotNull(BusinessDAO.get(mdBusiness2.definesType(), KEY));
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

  @Request
  @Test
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

            role.grantPermission(Operation.READ, mdAttributeDimension.getOid());
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

            Assert.assertTrue(permissions.contains(Operation.READ));
            Assert.assertFalse(permissions.contains(Operation.WRITE));
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

  @Request
  @Test
  public void testUpdateMdWebForm()
  {
    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness2.apply();

    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

    Assert.assertEquals(mdWebForm.getFormName(), test.getFormName());
    Assert.assertEquals(mdBusiness.definesType(), test.getFormMdClass().definesType());
    Assert.assertEquals(mdWebForm.getDescription(CommonProperties.getDefaultLocale()), test.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebForm.getDisplayLabel(CommonProperties.getDefaultLocale()), test.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<? extends MdFieldDAOIF> fields = test.getOrderedMdFields();

    Assert.assertEquals(3, fields.size());

    MdWebIntegerDAO testField = (MdWebIntegerDAO) fields.get(2);

    Assert.assertEquals(mdWebInteger.getFieldName(), testField.getFieldName());
    Assert.assertEquals(mdWebInteger.getFieldOrder(), testField.getFieldOrder());
    Assert.assertEquals(mdWebInteger.getDescription(CommonProperties.getDefaultLocale()), testField.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdWebInteger.getDisplayLabel(CommonProperties.getDefaultLocale()), testField.getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(mdAttributeInteger.definesAttribute(), testField.getDefiningMdAttribute().definesAttribute());
  }

  /**
   * Test setting of attributes of and on the class datatype
   */
  @Request
  @Test
  public void testCreateMdTerm()
  {
    // Create test MdBusiness
    MdTermDAO mdTerm = TestFixtureFactory.createMdTerm();
    // mdTerm.setValue(MdEntityInfo.HAS_DETERMINISTIC_IDS, true);
    mdTerm.setValue(MdTermInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTerm.apply();

    MdAttributeBooleanDAO mdBoolean = TestFixtureFactory.addBooleanAttribute(mdTerm);
    mdBoolean.apply();

    // Export the test entities
    ExportMetadata metadata = new ExportMetadata(true);
    metadata.addCreate(new ComponentIF[] { mdTerm });

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    // Delete the test entites
    TestFixtureFactory.delete(mdTerm);

    // Import the test entites
    SAXImporter.runImport(new File(tempXMLFile));

    MdTermDAOIF mdTermIF = MdTermDAO.getMdTermDAO(mdTerm.definesType());

    MdAttributeDAOIF attribute = mdTermIF.definesAttribute(mdBoolean.definesAttribute());

    Assert.assertEquals(mdTerm.getStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), mdTermIF.getStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    Assert.assertEquals(mdTerm.getStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), mdTermIF.getStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
    Assert.assertEquals(mdTerm.getValue(MdBusinessInfo.EXTENDABLE), mdTermIF.getValue(MdBusinessInfo.EXTENDABLE));
    Assert.assertEquals(mdTerm.getValue(MdBusinessInfo.ABSTRACT), mdTermIF.getValue(MdBusinessInfo.ABSTRACT));
    Assert.assertEquals(mdTerm.getValue(MdBusinessInfo.CACHE_SIZE), mdTermIF.getValue(MdBusinessInfo.CACHE_SIZE));
    Assert.assertEquals(mdTerm.getValue(MdBusinessInfo.PUBLISH), mdTermIF.getValue(MdBusinessInfo.PUBLISH));
    Assert.assertEquals(mdTerm.getValue(MdBusinessInfo.REMOVE), mdTermIF.getValue(MdBusinessInfo.REMOVE));
    Assert.assertEquals(mdTerm.getValue(MdBusinessInfo.EXTENDABLE), mdTermIF.getValue(MdBusinessInfo.EXTENDABLE));
    // Assert.assertEquals(mdTerm.getValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS),
    // mdTermIF.getValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS));

    // Ensure the attributes are linked to the correct MdBusiness object
    Assert.assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdTermIF.getOid());
  }

  /**
   * Test setting of attributes of and on the class datatype
   */
  @Request
  @Test
  public void testCreateMdTermRelationship()
  {
    // Create test MdBusiness
    MdTermDAO mdTerm = TestFixtureFactory.createMdTerm("ParentTerm");
    mdTerm.setValue(MdTermInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTerm.apply();

    MdTermRelationshipDAO mdTermRelationship = TestFixtureFactory.createMdTermRelationship(mdTerm);
    mdTermRelationship.apply();

    // Export the test entities
    ExportMetadata metadata = new ExportMetadata(true);
    metadata.addCreate(new ComponentIF[] { mdTerm, mdTermRelationship });

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    // Delete the test entites
    TestFixtureFactory.delete(mdTerm);

    // Import the test entites
    SAXImporter.runImport(new File(tempXMLFile));

    MdTermRelationshipDAOIF mdTermRelationshipIF = MdTermRelationshipDAO.getMdTermRelationshipDAO(mdTermRelationship.definesType());

    Assert.assertEquals(mdTermRelationship.getStructValue(MdTermRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), mdTermRelationshipIF.getStructValue(MdTermRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    Assert.assertEquals(mdTermRelationship.getStructValue(MdTermRelationshipInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), mdTermRelationshipIF.getStructValue(MdTermRelationshipInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
    Assert.assertEquals(mdTermRelationship.getValue(MdTermRelationshipInfo.EXTENDABLE), mdTermRelationshipIF.getValue(MdTermRelationshipInfo.EXTENDABLE));
    Assert.assertEquals(mdTermRelationship.getValue(MdTermRelationshipInfo.ABSTRACT), mdTermRelationshipIF.getValue(MdTermRelationshipInfo.ABSTRACT));
    Assert.assertEquals(mdTermRelationship.getValue(MdTermRelationshipInfo.CACHE_SIZE), mdTermRelationshipIF.getValue(MdTermRelationshipInfo.CACHE_SIZE));
    Assert.assertEquals(mdTermRelationship.getValue(MdTermRelationshipInfo.PUBLISH), mdTermRelationshipIF.getValue(MdTermRelationshipInfo.PUBLISH));
    Assert.assertEquals(mdTermRelationship.getValue(MdTermRelationshipInfo.REMOVE), mdTermRelationshipIF.getValue(MdTermRelationshipInfo.REMOVE));
    Assert.assertEquals(mdTerm.definesType(), mdTermRelationshipIF.getParentMdBusiness().definesType());
    Assert.assertEquals(mdTermRelationship.getValue(MdTermRelationshipInfo.PARENT_CARDINALITY), mdTermRelationshipIF.getValue(MdTermRelationshipInfo.PARENT_CARDINALITY));
    Assert.assertEquals(mdTermRelationship.getValue(MdTermRelationshipInfo.PARENT_METHOD), mdTermRelationshipIF.getValue(MdTermRelationshipInfo.PARENT_METHOD));
    Assert.assertEquals(mdTermRelationship.getStructValue(MdTermRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), mdTermRelationshipIF.getStructValue(MdTermRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    Assert.assertEquals(mdTerm.definesType(), mdTermRelationshipIF.getChildMdBusiness().definesType());
    Assert.assertEquals(mdTermRelationship.getValue(MdTermRelationshipInfo.CHILD_CARDINALITY), mdTermRelationshipIF.getValue(MdTermRelationshipInfo.CHILD_CARDINALITY));
    Assert.assertEquals(mdTermRelationship.getValue(MdTermRelationshipInfo.CHILD_METHOD), mdTermRelationshipIF.getValue(MdTermRelationshipInfo.CHILD_METHOD));
    Assert.assertEquals(mdTermRelationship.getStructValue(MdTermRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), mdTermRelationshipIF.getStructValue(MdTermRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));

    String expectedType = ( (AttributeEnumerationIF) mdTermRelationship.getAttributeIF(MdTermRelationshipInfo.ASSOCIATION_TYPE) ).dereference()[0].getOid();
    String actualType = ( (AttributeEnumerationIF) mdTermRelationshipIF.getAttributeIF(MdTermRelationshipInfo.ASSOCIATION_TYPE) ).dereference()[0].getOid();

    Assert.assertEquals(expectedType, actualType);
  }

  /**
   * Test setting of attributes of and on the class datatype
   */
  @Request
  @Test
  public void testCreateMdVertex()
  {
    // Create test MdVertex
    MdVertexDAO mdVertex1 = TestFixtureFactory.createMdVertex();
    mdVertex1.setValue(MdVertexInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdVertex1.setValue(MdVertexInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdVertex1.setValue(MdVertexInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdVertex1.setValue(MdVertexInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdVertex1.apply();

    TestFixtureFactory.addBooleanAttribute(mdVertex1).apply();

    MdVertexDAO mdVertex2 = TestFixtureFactory.createMdVertex("TestVertex2");
    mdVertex2.setValue(MdVertexInfo.SUPER_MD_VERTEX, mdVertex1.getOid());
    mdVertex2.setValue(MdVertexInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdVertex2.apply();

    // Export the test entities
    ExportMetadata metadata = new ExportMetadata(true);
    metadata.addCreate(new ComponentIF[] { mdVertex1, mdVertex2 });

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    // Delete the test entites
    TestFixtureFactory.delete(mdVertex2);
    TestFixtureFactory.delete(mdVertex1);

    // Import the test entites
    SAXImporter.runImport(new File(tempXMLFile));

    MdVertexDAOIF mdVertex1IF = MdVertexDAO.getMdVertexDAO(mdVertex1.definesType());
    MdVertexDAOIF mdVertex2IF = MdVertexDAO.getMdVertexDAO(mdVertex2.definesType());

    MdAttributeDAOIF attribute = mdVertex1IF.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN);

    Assert.assertEquals(mdVertex1IF.getStructValue(MdVertexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdVertex Set Test");
    Assert.assertEquals(mdVertex1IF.getStructValue(MdVertexInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdVertex Attributes Test");
    Assert.assertEquals(mdVertex1IF.getValue(MdVertexInfo.ABSTRACT), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(MdAttributeBooleanInfo.FALSE, mdVertex1IF.getValue(MdVertexInfo.PUBLISH));

    // Change to false when cascading delete is implemented
    Assert.assertEquals(mdVertex1IF.getValue(MetadataInfo.REMOVE), MdAttributeBooleanInfo.TRUE);

    // Ensure inheritance is linking to the correct super class
    Assert.assertEquals(mdVertex2IF.getValue(MdVertexInfo.SUPER_MD_VERTEX), mdVertex1IF.getOid());

    // Ensure the attributes are linked to the correct MdVertex object
    Assert.assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdVertex1IF.getOid());
  }

  /**
   * Test setting of attributes of and on the class datatype
   */
  @Request
  @Test
  public void testCreateLocalCharacterEmbedded()
  {
    // Create test MdVertex
    MdVertexDAO mdVertex1 = TestFixtureFactory.createMdVertex();
    mdVertex1.setValue(MdVertexInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdVertex1.setValue(MdVertexInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdVertex1.setValue(MdVertexInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdVertex1.setValue(MdVertexInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdVertex1.apply();

    MdAttributeLocalCharacterEmbeddedDAO mdAttribute = TestFixtureFactory.addLocalCharacterEmbeddedAttribute(mdVertex1);
    mdAttribute.apply();

    // Export the test entities
    ExportMetadata metadata = new ExportMetadata(true);
    metadata.addCreate(new ComponentIF[] { mdVertex1 });

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    // Delete the test entites
    TestFixtureFactory.delete(mdVertex1);

    // Import the test entites
    SAXImporter.runImport(new File(tempXMLFile));

    MdVertexDAOIF mdVertex1IF = MdVertexDAO.getMdVertexDAO(mdVertex1.definesType());

    MdAttributeDAOIF attribute = mdVertex1IF.definesAttribute(mdAttribute.definesAttribute());

    Assert.assertNotNull(attribute);
  }

  /**
   * Test setting of attributes of and on the class datatype
   */
  @Request
  @Test
  public void testCreateLink()
  {
    // Create test MdVertex
    MdVertexDAO mdVertex2 = TestFixtureFactory.createMdVertex("TestVertex2");
    mdVertex2.apply();

    MdVertexDAO mdVertex1 = TestFixtureFactory.createMdVertex();
    mdVertex1.setValue(MdVertexInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdVertex1.setValue(MdVertexInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdVertex1.setValue(MdVertexInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdVertex1.setValue(MdVertexInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdVertex1.apply();

    MdAttributeLinkDAO mdAttribute = TestFixtureFactory.addLinkAttribute(mdVertex1, mdVertex2);
    mdAttribute.apply();

    // Export the test entities
    ExportMetadata metadata = new ExportMetadata(true);
    metadata.addCreate(new ComponentIF[] { mdVertex1 });

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    // Delete the test entites
    TestFixtureFactory.delete(mdVertex1);

    // Import the test entites
    SAXImporter.runImport(new File(tempXMLFile));

    MdVertexDAOIF mdVertex1IF = MdVertexDAO.getMdVertexDAO(mdVertex1.definesType());

    try
    {
      MdAttributeDAOIF attribute = mdVertex1IF.definesAttribute(mdAttribute.definesAttribute());

      Assert.assertNotNull(attribute);
    }
    finally
    {
      TestFixtureFactory.delete(mdVertex1IF);
    }
  }

  /**
   * Test setting of attributes of and on the class datatype
   */
  @Request
  @Test
  public void testCreateMdEdge()
  {
    // Create test MdVertex
    MdVertexDAO mdVertex1 = TestFixtureFactory.createMdVertex();
    mdVertex1.apply();

    MdVertexDAO mdVertex2 = TestFixtureFactory.createMdVertex("TestVertex2");
    mdVertex2.apply();

    MdEdgeDAO mdEdge = TestFixtureFactory.createMdEdge(mdVertex1, mdVertex2);
    mdEdge.apply();

    // Export the test entities
    ExportMetadata metadata = new ExportMetadata(true);
    metadata.addCreate(new ComponentIF[] { mdVertex1, mdVertex2, mdEdge });

    SAXExporter.export(tempXMLFile, SCHEMA, metadata);

    // Delete the test entites
    TestFixtureFactory.delete(mdEdge);
    TestFixtureFactory.delete(mdVertex2);
    TestFixtureFactory.delete(mdVertex1);

    // Import the test entites
    SAXImporter.runImport(new File(tempXMLFile));

    MdEdgeDAOIF mdEdgeIF = MdEdgeDAO.getMdEdgeDAO(mdEdge.definesType());

    Assert.assertEquals(mdEdgeIF.getStructValue(MdEdgeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdEdge Set Test");
    Assert.assertEquals(mdEdgeIF.getStructValue(MdEdgeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Set mdEdge Attributes Test");

    String pOid = mdEdgeIF.getValue(MdEdgeInfo.PARENT_MD_VERTEX);
    String cOid = mdEdgeIF.getValue(MdEdgeInfo.CHILD_MD_VERTEX);

    MdVertexDAOIF parent = MdVertexDAO.get(pOid);
    MdVertexDAOIF child = MdVertexDAO.get(cOid);

    Assert.assertEquals(parent.definesType(), mdVertex1.definesType());
    Assert.assertEquals(child.definesType(), mdVertex2.definesType());
  }

  /**
   * @param testCondition
   * @return
   */
  private String getOperationId(FieldConditionDAOIF testCondition)
  {
    AttributeEnumerationIF attribute = (AttributeEnumerationIF) testCondition.getAttributeIF(BasicConditionInfo.OPERATION);

    Set<String> itemIds = attribute.getEnumItemIdList();
    Iterator<String> it = itemIds.iterator();

    if (it.hasNext())
    {
      String itemId = it.next();

      return EnumerationItemDAO.get(itemId).getName();
    }

    return null;
  }
}
