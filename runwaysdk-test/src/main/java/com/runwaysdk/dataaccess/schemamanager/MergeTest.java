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
package com.runwaysdk.dataaccess.schemamanager;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.rbac.MethodActorDAO;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.business.state.MdStateMachineDAO;
import com.runwaysdk.business.state.MdStateMachineDAOIF;
import com.runwaysdk.business.state.StateMasterDAO;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.BusinessInfo;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.EntityTypes;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdActionInfo;
import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdControllerInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdExceptionInfo;
import com.runwaysdk.constants.MdIndexInfo;
import com.runwaysdk.constants.MdInformationInfo;
import com.runwaysdk.constants.MdLocalStructInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdParameterInfo;
import com.runwaysdk.constants.MdProblemInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdStateMachineInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.constants.XMLConstants;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.EnumerationItemDAOIF;
import com.runwaysdk.dataaccess.MdActionDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdControllerDAOIF;
import com.runwaysdk.dataaccess.MdElementDAOIF;
import com.runwaysdk.dataaccess.MdExceptionDAOIF;
import com.runwaysdk.dataaccess.MdIndexDAOIF;
import com.runwaysdk.dataaccess.MdInformationDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.MdProblemDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.MdViewDAOIF;
import com.runwaysdk.dataaccess.TransitionDAO;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.SAXParseTest;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.io.dataDefinition.ExportMetadata;
import com.runwaysdk.dataaccess.io.dataDefinition.VersionExporter;
import com.runwaysdk.dataaccess.io.dataDefinition.VersionHandler;
import com.runwaysdk.dataaccess.io.dataDefinition.VersionHandler.Action;
import com.runwaysdk.dataaccess.metadata.MdActionDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeBlobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeStructDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeVirtualDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdControllerDAO;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdExceptionDAO;
import com.runwaysdk.dataaccess.metadata.MdIndexDAO;
import com.runwaysdk.dataaccess.metadata.MdInformationDAO;
import com.runwaysdk.dataaccess.metadata.MdLocalStructDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdPackage;
import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.dataaccess.metadata.MdProblemDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.dataaccess.metadata.MdWebBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFormDAO;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAO;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAOIF;
import com.runwaysdk.session.Request;

public class MergeTest
{
  private static final String SUSPENDED_STATE       = "SUSPENDED";

  private static final String STATE_MACHINE_PACKAGE = "test.state";

  private static final String PASSIVE_STATE         = "PASSIVE";

  private static final String ACTIVE_STATE          = "ACTIVE";

  private static final String STATE_MACHINE_NAME    = "StatusStateMachine";

  private static final String MERGED_SCHEMA         = CommonProperties.getProjectBasedir() + "/target/testxml/MergeTest/(0001238646707003)merged.xml";

  private static final String UPDATE_SCHEMA_1       = CommonProperties.getProjectBasedir() + "/target/testxml/MergeTest/(0001238646707000)update1.xml";

  private static final String UPDATE_SCHEMA_2       = CommonProperties.getProjectBasedir() + "/target/testxml/MergeTest/(0001238646707001)update2.xml";

  private static final String UPDATE_SCHEMA_3       = CommonProperties.getProjectBasedir() + "/target/testxml/MergeTest/(0001238646707002)update3.xml";

  private static final String CREATE_SCHEMA         = CommonProperties.getProjectBasedir() + "/target/testxml/MergeTest/(0001238646706999)createSchema.xml";

  private static final String PARAMETER_TYPE        = "java.lang.Integer[][]";

  private static final String ATTRIBUTE_NAME        = TestFixConst.ATTRIBUTE_CHARACTER;

  private static final String RETURN_TYPE           = "void";

  private static final String METHOD_NAME           = "checkin";

  public static final String path   = TestConstants.Path.XMLFiles + "/";

  public static final String SCHEMA = XMLConstants.VERSION_XSD;

  public static final String CLASS  = "test.xmlclasses.Class1";

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
      UserDAO.findUser(TestFixConst.TEST_USER).getEntityDAO().delete();
    }
    catch (DataNotFoundException e)
    {

    }

    try
    {
      RoleDAO.findRole(TestFixConst.TEST_ROLE1_NAME).getEntityDAO().delete();
    }
    catch (DataNotFoundException e)
    {

    }

    try
    {
      RoleDAO.findRole(TestFixConst.TEST_ROLE2_NAME).getEntityDAO().delete();
    }
    catch (DataNotFoundException e)
    {

    }

    // Remove all imported timestamps
    List<String> timestamps = Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY);

    for (String timestamp : timestamps)
    {
      Database.removePropertyValue(Database.VERSION_NUMBER, MdAttributeCharacterInfo.CLASS, timestamp, Database.VERSION_TIMESTAMP_PROPERTY);
    }
  }

  /**
   * Initial: a mdBusiness and a blob attribute Update : creates a blob
   * attribute in the mdBusiness
   */
  @Request
  @Test
  public void testUpdateBlobMerge()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    ComponentIF[] componentArray = new ComponentIF[] { mdBusiness1 };
    final MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addBlobAttribute(mdBusiness1);
    mdAttribute.apply();

    generateCreateSchema(componentArray);
    ExportMetadata metadata = new ExportMetadata();
    generateMerge(metadata, componentArray, new UpdateActions()
    {
      @Override
      public void perform()
      {
        mdAttribute.setStructValue(MdAttributeBlobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Blob Update Test");
        mdAttribute.setValue(MdAttributeBlobInfo.IMMUTABLE, MdAttributeBooleanInfo.TRUE);
        mdAttribute.setValue(MdAttributeBlobInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
        mdAttribute.setStructValue(MdAttributeBlobInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Blob Test");
        mdAttribute.apply();
      }
    });

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeBlobDAO attribute = (MdAttributeBlobDAO) ( mdEntityIF.definesAttribute("testBlob") ).getBusinessDAO();
    Assert.assertEquals("Blob Update Test", attribute.getStructValue(MdAttributeBlobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
  }

  /**
   * Initial: a mdBusiness and a blob attribute Update : creates a blob
   * attribute in the mdBusiness
   */
  @Request
  @Test
  public void testAttributeRename()
  {
    final String newAttributeName = "renamedAttribute";

    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addBooleanAttribute(mdBusiness1);
    mdAttribute.apply();

    // Generate the initial model
    generateCreateSchema(new ComponentIF[] { mdBusiness1 });

    ExportMetadata metadata = ExportMetadata.buildUpdate(mdBusiness1);
    metadata.renameAttribute(mdAttribute, newAttributeName);

    // Export the model with the rnamed attributed
    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, metadata);

    mdAttribute.setValue(MdAttributeBooleanInfo.NAME, newAttributeName);
    mdAttribute.apply();

    // Export the model to delete renamed attribute
    VersionExporter.export(UPDATE_SCHEMA_2, SCHEMA, ExportMetadata.buildDelete(mdAttribute));

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1, UPDATE_SCHEMA_2);

    TestFixtureFactory.delete(mdBusiness1);

    // Import merge file
    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);

    Assert.assertNull(mdEntityIF.definesAttribute("testBlob"));
    Assert.assertNull(mdEntityIF.definesAttribute(newAttributeName));
  }

  /**
   * Initial: a mdBusiness and a blob attribute Update : creates a blob
   * attribute in the mdBusiness
   */
  @Request
  @Test
  public void testAttributeRenameWithPermissions()
  {
    final String newAttributeName = "renamedAttribute";

    RoleDAO role = TestFixtureFactory.createRole1();
    role.apply();

    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addBooleanAttribute(mdBusiness1);
    mdAttribute.apply();

    // Add permissions to the MdBusiness
    role.grantPermission(Operation.CREATE, mdBusiness1.getId());
    role.grantPermission(Operation.WRITE, mdBusiness1.getId());

    // Add attribute permissions
    role.grantPermission(Operation.WRITE, mdAttribute.getId());
    role.grantPermission(Operation.READ, mdAttribute.getId());

    // Generate the initial model
    ExportMetadata metadata = new ExportMetadata();
    metadata.addCreate(mdBusiness1);
    metadata.addCreate(role);
    metadata.addGrantPermissions(role);

    generateCreateSchema(metadata);

    ExportMetadata update = ExportMetadata.buildUpdate(mdBusiness1);
    update.renameAttribute(mdAttribute, newAttributeName);

    // Export the model with the rnamed attributed
    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, update);

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

    TestFixtureFactory.delete(mdBusiness1);
    TestFixtureFactory.delete(role);

    // Import merge file
    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF mdAttributeIF = mdEntityIF.definesAttribute(newAttributeName);
    RoleDAOIF roleIF = RoleDAO.findRole(role.getRoleName());
    Set<Operation> permissions = roleIF.getAllPermissions(mdAttributeIF);

    Assert.assertNotNull(mdAttributeIF);
    Assert.assertTrue(permissions.contains(Operation.READ));
    Assert.assertTrue(permissions.contains(Operation.WRITE));
  }

  /**
   * Test updating a created action on a auto defined controller
   */
  @Request
  @Test
  public void testControllerActionUpdateMerge()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(true);
    mdBusiness1.apply();

    try
    {
      MdControllerDAOIF mdController = MdControllerDAO.getMdControllerDAO(mdBusiness1.definesType() + "Controller");

      MdActionDAO mdAction = MdActionDAO.newInstance();
      mdAction.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
      mdAction.setValue(MdActionInfo.DISPLAY_LABEL, "testAction");
      mdAction.setValue(MdActionInfo.NAME, "testAction");

      ExportMetadata create = ExportMetadata.buildUpdate(mdController);
      create.addNewMdAction(mdController, mdAction, new MdParameterDAO[] {});

      VersionExporter.export(CREATE_SCHEMA, SCHEMA, create);

      mdAction.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
      mdAction.setValue(MdActionInfo.DISPLAY_LABEL, "testAction");
      mdAction.setValue(MdActionInfo.NAME, "testAction");
      mdAction.setValue(MdActionInfo.IS_POST, "true");
      mdAction.setValue(MdActionInfo.IS_QUERY, "true");
      mdAction.apply();

      VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, ExportMetadata.buildUpdate(mdController));

      mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

      mdAction.delete();

      // Import merge file
      VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

      MdControllerDAOIF mdControllerIF = MdControllerDAO.getMdControllerDAO(mdController.definesType());

      MdActionDAOIF mdActionIF = mdControllerIF.definesMdAction(mdAction.getName());

      Assert.assertNotNull(mdActionIF);
      Assert.assertTrue(new Boolean(mdActionIF.getValue(MdActionInfo.IS_QUERY)).booleanValue());
      Assert.assertTrue(new Boolean(mdActionIF.getValue(MdActionInfo.IS_POST)).booleanValue());
    }
    finally
    {
      mdBusiness1.delete();
    }
  }

  /**
   * Simple Attribute update test
   * 
   * Initial: A mdBusiness, mdStruct, and a mdStruct type attribute in the
   * mdBusiness Update: Update to the attributes of the mdStruct and to the
   * struct type attributes
   * 
   */
  @Request
  @Test
  public void testUpdateStructMerge()
  {
    final MdStructDAO mdStruct = TestFixtureFactory.createMdStruct1();
    mdStruct.setValue(MdStructInfo.CACHE_SIZE, "525");
    mdStruct.setGenerateMdController(false);
    mdStruct.setValue(MdStructInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdStruct.apply();

    MdAttributeConcreteDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdStruct);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFAULT_VALUE, MdAttributeBooleanInfo.FALSE);
    mdAttributeBoolean.apply();

    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    final MdAttributeConcreteDAO mdAttribute = TestFixtureFactory.addStructAttribute(mdBusiness1, mdStruct);
    mdAttribute.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness1.definesType());
    businessDAO.setStructValue("testStruct", TestFixConst.ATTRIBUTE_BOOLEAN, MdAttributeBooleanInfo.TRUE);
    businessDAO.apply();

    ComponentIF[] componentArray = new ComponentIF[] { mdBusiness1, mdStruct };
    generateCreateSchema(componentArray);

    final ExportMetadata metadata = new ExportMetadata();
    generateMerge(metadata, componentArray, new UpdateActions()
    {
      @Override
      public void perform()
      {
        mdStruct.setValue(MdBusinessInfo.CACHE_SIZE, "600");

        MdStructDAO updateStruct = MdStructDAO.get(mdStruct.getId()).getBusinessDAO();
        updateStruct.setValue(MdBusinessInfo.CACHE_SIZE, "600");
        updateStruct.apply();

        mdAttribute.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Struct Update Test");
        mdAttribute.apply();
      }
    });

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF mdAttributeIF = mdEntityIF.definesAttribute("testStruct");
    MdStructDAOIF mdStructIF = MdStructDAO.getMdStructDAO(mdStruct.definesType());

    Assert.assertEquals(mdAttributeIF.getStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Struct Update Test");

    Assert.assertEquals("600", mdStructIF.getValue(MdBusinessInfo.CACHE_SIZE));

    // Ensure that the correct class is being referenced
    Assert.assertEquals(mdAttributeIF.getValue(MdAttributeStructInfo.MD_STRUCT), mdStructIF.getId());

    // List<String> ids = EntityDAO.getEntityIdsDB(CLASS);

    // This condition is failing: fix
    // Assert.assertEquals(1, ids.size());

    // BusinessDAOIF businessDAOIF = BusinessDAO.get(ids.get(0));
    // AttributeStruct attribute = (AttributeStruct)
    // businessDAOIF.getAttributeIF("testStruct");

    // Assert.assertEquals(MdAttributeBooleanInfo.TRUE,
    // attribute.getValue(TestFixConst.ATTRIBUTE_BOOLEAN));
  }

  /**
   * Test setting of attributes of and on the class datatype
   */

  @Request
  @Test
  public void testUpdateMdInformationMerge()
  {
    // Create test MdBusiness
    MdInformationDAO mdInformation1 = TestFixtureFactory.createMdInformation1();
    mdInformation1.setValue(MdInformationInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdInformation1.setValue(MdInformationInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdInformation1.setValue(MdInformationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdInformation1.setValue(MdInformationInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdInformation1.setValue(MdInformationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdInformation1.apply();

    MdInformationDAO mdInformation2 = TestFixtureFactory.createMdInformation2();
    mdInformation2.setValue(MdInformationInfo.SUPER_MD_INFORMATION, mdInformation1.getId());
    mdInformation2.setValue(MdInformationInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdInformation2.setValue(MdInformationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdInformation2.apply();

    generateCreateSchema(new ComponentIF[] { mdInformation1, mdInformation2 });

    MdAttributeConcreteDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdInformation1);

    ExportMetadata updateMetadata = ExportMetadata.buildUpdate(mdInformation1, mdInformation2);
    updateMetadata.addNewMdAttribute(mdInformation1, mdAttributeBoolean);

    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, updateMetadata);

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

    TestFixtureFactory.delete(mdInformation2);
    TestFixtureFactory.delete(mdInformation1);

    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    MdInformationDAOIF mdInformation1IF = MdInformationDAO.getMdInformation(SAXParseTest.INFORMATION);
    MdInformationDAOIF mdInformation2IF = MdInformationDAO.getMdInformation(SAXParseTest.INFORMATION2);

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
    Assert.assertEquals(mdInformation2IF.getValue(MdInformationInfo.SUPER_MD_INFORMATION), mdInformation1IF.getId());

    // Ensure the attributes are linked to the correct MdBusiness object
    Assert.assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdInformation1IF.getId());
  }

  @Request
  @Test
  public void testUpdateMdEnumerationMerge()
  {
    VersionHandler.runImport(new File(SAXParseTest.REMOVABLE_FILTER_SET), Action.DO_IT, XMLConstants.VERSION_XSD);
    final MdElementDAOIF enumerationMaster = MdElementDAO.getMdElementDAO("test.xmlclasses.EnumClassTest");
    final MdEnumerationDAO mdEnumeration = MdEnumerationDAO.getMdEnumerationDAO(SAXParseTest.FILTER).getBusinessDAO();
    final MdEnumerationDAO mdEnumeration2 = MdEnumerationDAO.getMdEnumerationDAO(SAXParseTest.FILTER2).getBusinessDAO();

    final ExportMetadata updateMetadata = new ExportMetadata();

    generateMerge(updateMetadata, new ComponentIF[] { mdEnumeration, mdEnumeration2 }, new ComponentIF[] { mdEnumeration, mdEnumeration2, enumerationMaster }, new UpdateActions()
    {
      @Override
      public void perform()
      {
        mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
        mdEnumeration.apply();

        mdEnumeration2.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
        mdEnumeration2.apply();

        List<BusinessDAOIF> allEnumItems = mdEnumeration2.getAllEnumItems();

        mdEnumeration2.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.FALSE);
        mdEnumeration2.apply();

        // Remove an arbitrary item from the list of valid enum items.
        updateMetadata.addRemoveEnumItem(mdEnumeration2, allEnumItems.get(0));
      }
    }, SAXParseTest.REMOVABLE_FILTER_SET, MergeTest.UPDATE_SCHEMA_1);

    List<BusinessDAOIF> removedItems = updateMetadata.getRemoveEnumItems(mdEnumeration2);

    MdEnumerationDAO updatedmdEnumeration = MdEnumerationDAO.getMdEnumerationDAO(SAXParseTest.FILTER).getBusinessDAO();
    MdEnumerationDAO updatedmdEnumeration2 = MdEnumerationDAO.getMdEnumerationDAO(SAXParseTest.FILTER2).getBusinessDAO();

    Assert.assertEquals(updatedmdEnumeration.getStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Enumeration Filter Test");
    Assert.assertEquals(updatedmdEnumeration.getStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), "Filter Set Test");
    Assert.assertEquals(updatedmdEnumeration.getAttributeIF(MdEnumerationInfo.REMOVE).getValue(), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(updatedmdEnumeration.getAttributeIF(MdEnumerationInfo.INCLUDE_ALL).getValue(), MdAttributeBooleanInfo.TRUE);

    List<BusinessDAOIF> updatedItems = updatedmdEnumeration.getAllEnumItems();
    Assert.assertEquals(3, updatedItems.size());

    List<BusinessDAOIF> updatedItems2 = updatedmdEnumeration2.getAllEnumItems();
    Assert.assertEquals(updatedmdEnumeration2.getAttributeIF(MdEnumerationInfo.INCLUDE_ALL).getValue(), MdAttributeBooleanInfo.FALSE);
    Assert.assertEquals(updatedItems2.size(), 2);

    // Ensure that none of the items removed from MdEnumeration2 are still in
    // the enum items list.
    for (BusinessDAOIF item : updatedItems2)
    {
      Assert.assertFalse(removedItems.contains(item));
    }
  }

  @Request
  @Test
  public void testUpdateMdProblemMerge()
  {
    // Create test MdBusiness
    final MdProblemDAO mdProblem1 = TestFixtureFactory.createMdProblem1();
    mdProblem1.setValue(MdProblemInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdProblem1.setValue(MdProblemInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdProblem1.setValue(MdProblemInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdProblem1.setValue(MdProblemInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdProblem1.setValue(MdProblemInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdProblem1.apply();

    final MdAttributeConcreteDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdProblem1);

    MdProblemDAO mdProblem2 = TestFixtureFactory.createMdProblem2();
    mdProblem2.setValue(MdProblemInfo.SUPER_MD_PROBLEM, mdProblem1.getId());
    mdProblem2.setValue(MdProblemInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdProblem2.setValue(MdProblemInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdProblem2.apply();
    final ComponentIF[] componentArray = new ComponentIF[] { mdProblem1, mdProblem2 };
    generateCreateSchema(componentArray);
    final ExportMetadata metadata = new ExportMetadata();

    generateMerge(metadata, componentArray, new UpdateActions()
    {
      @Override
      public void perform()
      {
        metadata.addNewMdAttribute(mdProblem1, mdAttributeBoolean);
      }
    });
    // Export the test entities

    MdProblemDAOIF mdProblem1IF = MdProblemDAO.getMdProblem(SAXParseTest.EXCEPTION);
    MdProblemDAOIF mdProblem2IF = MdProblemDAO.getMdProblem(SAXParseTest.EXCEPTION2);

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
    Assert.assertEquals(mdProblem2IF.getValue(MdProblemInfo.SUPER_MD_PROBLEM), mdProblem1IF.getId());

    // Ensure the attributes are linked to the correct MdBusiness object
    Assert.assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdProblem1IF.getId());
  }

  /**
   * Initial schema : two relationship objects Update : Delete one by the delete
   * tag
   * 
   */
  /*
   * @Request @Test public void testDeleteRelationshipMerge() { // Create the
   * Metadata entities final MdBusinessDAO mdBusiness1 =
   * TestFixtureFactory.createMdBusiness1(); final MdBusinessDAO mdBusiness2 =
   * TestFixtureFactory.createMdBusiness2(); mdBusiness1.apply();
   * mdBusiness2.apply();
   * 
   * final MdRelationshipDAO mdRelationship1 =
   * TestFixtureFactory.createMdRelationship1(mdBusiness1, mdBusiness2);
   * mdRelationship1.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
   * mdRelationship1.apply();
   * 
   * TestFixtureFactory.addBooleanAttribute(mdRelationship1).apply();
   * 
   * // Create BusinessDAO for the RelationshipDAO BusinessDAO businessDAO1 =
   * BusinessDAO.newInstance(mdBusiness1.definesType()); businessDAO1.apply();
   * 
   * BusinessDAO businessDAO2 =
   * BusinessDAO.newInstance(mdBusiness2.definesType()); businessDAO2.apply();
   * 
   * // Create Test RelationshipDAO RelationshipDAO relationshipDAO1 =
   * RelationshipDAO.newInstance(businessDAO1.getId(), businessDAO2 .getId(),
   * mdRelationship1.definesType());
   * relationshipDAO1.setValue(TestFixConst.ATTRIBUTE_BOOLEAN,
   * MdAttributeBooleanInfo.TRUE); relationshipDAO1.apply();
   * 
   * final RelationshipDAO relationshipDAO2 =
   * RelationshipDAO.newInstance(businessDAO1.getId(), businessDAO2.getId(),
   * mdRelationship1.definesType());
   * relationshipDAO2.setValue(TestFixConst.ATTRIBUTE_BOOLEAN,
   * MdAttributeBooleanInfo.TRUE); String id = relationshipDAO2.apply();
   * 
   * final ComponentIF[] componentArray = new ComponentIF[] { mdBusiness1,
   * mdBusiness2, mdRelationship1, relationshipDAO1, relationshipDAO2 };
   * generateCreateSchema(componentArray); final ExportMetadata metadata = new
   * ExportMetadata(); generateMerge(metadata, componentArray, new
   * UpdateActions() {
   * 
   * @Override public void perform() { metadata.addDelete(relationshipDAO2); }
   * });
   * 
   * // Get the instance of CLASS, CLASS2, and RELATIONSHIP List<String> c1Ids =
   * EntityDAO.getEntityIdsDB(CLASS); List<String> c2Ids =
   * EntityDAO.getEntityIdsDB(SAXParseTest.CLASS2); List<String> r1Ids =
   * RelationshipDAO.getEntityIdsDB(SAXParseTest.RELATIONSHIP);
   * 
   * BusinessDAOIF c1 = BusinessDAO.get(c1Ids.get(0)); BusinessDAOIF c2 =
   * BusinessDAO.get(c2Ids.get(0)); RelationshipDAOIF r1 =
   * RelationshipDAO.get(r1Ids.get(0));
   * 
   * // Ensure that the parent references the instance of CLASS
   * Assert.assertEquals(c1.getId(), r1.getParentId()); // Ensure that the child
   * reference the instance of CLASS2 Assert.assertEquals(c2.getId(),
   * r1.getChildId()); // Ensure that the value of testBoolean is true
   * Assert.assertEquals(MdAttributeBooleanInfo.TRUE,
   * r1.getValue(TestFixConst.ATTRIBUTE_BOOLEAN));
   * 
   * try { RelationshipDAO.get(id);
   * 
   * Assert.fail("SAXImporter did not delete the RelationshipDAO with the id ["
   * + id + "]"); } catch (DataNotFoundException e) { // This is expected }
   * 
   * Assert.assertEquals(1,
   * MdRelationshipDAO.getEntityIdsDB(mdRelationship1.definesType()).size()); }
   */

  @Request
  @Test
  public void testUpdateMdControllerMerge()
  {
    final MdControllerDAO mdController = MdControllerDAO.newInstance();
    mdController.setValue(MdControllerInfo.NAME, "Controller1");
    mdController.setValue(MdControllerInfo.PACKAGE, "test.xmlclasses");
    mdController.setStructValue(MdControllerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Controller1");
    mdController.setStructValue(MdControllerInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Controller1");
    mdController.setValue(MdControllerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdController.apply();

    final MdActionDAO mdAction = MdActionDAO.newInstance();
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
    final MdParameterDAO mdParameter3 = MdParameterDAO.newInstance();
    mdParameter3.setValue(MdParameterInfo.NAME, "param3");
    mdParameter3.setValue(MdParameterInfo.TYPE, "java.lang.String");
    mdParameter3.setValue(MdParameterInfo.ORDER, "4");
    mdParameter3.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "param3");

    final MdActionDAO mdAction2 = MdActionDAO.newInstance();
    mdAction2.setValue(MdActionInfo.NAME, "checkout");
    mdAction2.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkout");
    mdAction2.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkout");

    final MdParameterDAO mdParameter4 = MdParameterDAO.newInstance();
    mdParameter4.setValue(MdParameterInfo.NAME, "param4");
    mdParameter4.setValue(MdParameterInfo.TYPE, "java.lang.String");
    mdParameter4.setValue(MdParameterInfo.ORDER, "0");
    mdParameter4.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "param4");

    generateCreateSchema(new ComponentIF[] { mdController });

    // Export mdController with all of its MdActions defined in the database,
    // and
    // export a new MdAction for the mdController, as well as a new MdParameter
    // for an existing MdAction of mdController
    final ExportMetadata updateMetadata = new ExportMetadata();
    generateMerge(updateMetadata, new ComponentIF[] { mdController }, new UpdateActions()
    {
      public void perform()
      {
        updateMetadata.addNewMdAction(mdController, mdAction2, mdParameter4);
        updateMetadata.addNewMdParameter(mdAction, mdParameter3);
      }
    });

    Assert.assertTrue(MdControllerDAO.isDefined("test.xmlclasses.Controller1"));

    MdControllerDAOIF mdControllerIF = MdControllerDAO.getMdControllerDAO("test.xmlclasses.Controller1");
    Assert.assertEquals("Controller1", mdControllerIF.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals("Controller1", mdControllerIF.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<MdActionDAOIF> mdActions = mdControllerIF.getMdActionDAOs();

    Assert.assertEquals(2, mdActions.size());

    int i = 0;
    int j = 1;

    if (!"checkin".equals(mdActions.get(0).getName()))
    {
      i = 1;
      j = 0;
    }

    Assert.assertEquals("checkin", mdActions.get(i).getName());
    Assert.assertEquals("checkin", mdActions.get(i).getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals("checkin", mdActions.get(i).getDescription(CommonProperties.getDefaultLocale()));

    List<MdParameterDAOIF> mdParameters = mdActions.get(i).getMdParameterDAOs();
    Assert.assertEquals(3, mdParameters.size());
    Assert.assertEquals("param1", mdParameters.get(0).getParameterName());
    Assert.assertEquals("java.lang.String", mdParameters.get(0).getParameterType().getType());
    Assert.assertEquals("0", mdParameters.get(0).getParameterOrder());
    Assert.assertEquals("param1", mdParameters.get(0).getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals("param1", mdParameters.get(0).getDescription(CommonProperties.getDefaultLocale()));

    Assert.assertEquals("param2", mdParameters.get(1).getParameterName());
    Assert.assertEquals("java.lang.Integer", mdParameters.get(1).getParameterType().getType());
    Assert.assertEquals("1", mdParameters.get(1).getParameterOrder());
    Assert.assertEquals("param2", mdParameters.get(1).getDisplayLabel(CommonProperties.getDefaultLocale()));

    Assert.assertEquals("param3", mdParameters.get(2).getParameterName());
    Assert.assertEquals("java.lang.String", mdParameters.get(2).getParameterType().getType());
    Assert.assertEquals("4", mdParameters.get(2).getParameterOrder());
    Assert.assertEquals("param3", mdParameters.get(2).getDisplayLabel(CommonProperties.getDefaultLocale()));

    Assert.assertEquals("checkout", mdActions.get(j).getName());
    Assert.assertEquals("checkout", mdActions.get(j).getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals("checkout", mdActions.get(j).getDescription(CommonProperties.getDefaultLocale()));

    mdParameters = mdActions.get(j).getMdParameterDAOs();
    Assert.assertEquals(1, mdParameters.size());
    Assert.assertEquals("param4", mdParameters.get(0).getParameterName());
    Assert.assertEquals("java.lang.String", mdParameters.get(0).getParameterType().getType());
    Assert.assertEquals("0", mdParameters.get(0).getParameterOrder());
    Assert.assertEquals("param4", mdParameters.get(0).getDisplayLabel(CommonProperties.getDefaultLocale()));
  }

  /**
   * Test setting of attributes on the character datatype minus any overlapping
   * attributes from the boolean test
   */
  @Request
  @Test
  public void testAddAttributeToMdBusinessMerge()
  {
    MdBusinessDAO mdBusiness = createMdBusiness();
    mdBusiness.setGenerateMdController(false);
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeDateDAO mdAttributeDate = TestFixtureFactory.addDateAttribute(mdBusiness);
    mdAttributeDate.apply();

    // Export create definition of the test MdBusiness
    VersionExporter.export(CREATE_SCHEMA, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness }));

    // Create a character attribute for the MdBusiness
    MdAttributeConcreteDAO mdAttribute = createCharacterAttribute(mdBusiness);

    // Export a update definition for the MdBusiness with the new character
    // attribute
    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness });
    metadata.addNewMdAttribute(mdBusiness, mdAttribute);

    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, metadata);

    // Delete applied definitions
    TestFixtureFactory.delete(mdBusiness);

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

    // Import merge file
    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    // Check that the merged import creates the expected MdBusiness and
    // MdAttribute
    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute(ATTRIBUTE_NAME);
    AttributeEnumerationIF index = (AttributeEnumerationIF) attribute.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    Assert.assertEquals(attribute.getStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "Character Set Test");
    Assert.assertEquals(attribute.getValue(MdAttributeCharacterInfo.SIZE), "200");
    Assert.assertEquals(attribute.getValue(MdAttributeCharacterInfo.IMMUTABLE), MdAttributeBooleanInfo.TRUE);
    Assert.assertEquals(index.dereference()[0].getId(), IndexTypes.NO_INDEX.getId());
  }

  @Request
  @Test
  public void testAddParameterToMdMethodMerge()
  {
    MdBusinessDAO mdBusiness = createMdBusiness();
    mdBusiness.setGenerateMdController(false);
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdMethodDAO mdMethod = createMethod(mdBusiness);
    mdMethod.apply();
    VersionExporter.export(CREATE_SCHEMA, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness }));

    MdParameterDAO mdParameter = createMdParameter(mdMethod);
    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness });
    metadata.addNewMdParameter(mdMethod, mdParameter);
    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, metadata);

    TestFixtureFactory.delete(mdBusiness);

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);
    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdMethodDAOIF mdMethodIF = mdEntityIF.getMdMethod(METHOD_NAME);
    for (MdParameterDAOIF mdParameterDAOIF : mdMethodIF.getMdParameterDAOs())
    {
      if (mdParameterDAOIF.getValue(MdParameterInfo.NAME).equals("validName"))
      {
        Assert.assertTrue(true);
      }
    }

  }

  @Request
  @Test
  public void testAddStateToStateMachineMerge()
  {
    MdBusinessDAO mdBusiness = createMdBusiness();
    mdBusiness.setGenerateMdController(false);
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdStateMachineDAO mdStateMachine = createMdStateMachine(mdBusiness);
    mdStateMachine.setGenerateMdController(false);
    mdStateMachine.setValue(MdStateMachineInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdStateMachine.apply();

    StateMasterDAO activeState = mdStateMachine.addState(ACTIVE_STATE, StateMasterDAOIF.Entry.ENTRY_STATE.getId());
    activeState.apply();
    StateMasterDAO passiveState = mdStateMachine.addState(PASSIVE_STATE, StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId());
    passiveState.apply();
    TransitionDAO activeToPassive = mdStateMachine.addTransition("activeToPassive", activeState.getId(), passiveState.getId());
    activeToPassive.apply();
    VersionExporter.export(CREATE_SCHEMA, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness }));

    ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness });
    StateMasterDAO suspendedState = mdStateMachine.addState(SUSPENDED_STATE, StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId());
    TransitionDAO suspendedToPassive = mdStateMachine.addTransition("suspendedToPassive", suspendedState.getId(), passiveState.getId());
    metadata.addNewTransitions(mdStateMachine, suspendedToPassive);
    metadata.addNewStates(mdStateMachine, suspendedState);
    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, metadata);

    TestFixtureFactory.delete(mdBusiness);

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

    // Import merge file
    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdBusinessDAOIF mdBusinessDAOIF = (MdBusinessDAOIF) mdEntityIF;
    MdStateMachineDAOIF mdStateMachineDAOIF = mdBusinessDAOIF.definesMdStateMachine();
    StateMasterDAOIF active = mdStateMachineDAOIF.definesStateMaster(ACTIVE_STATE);
    StateMasterDAOIF suspended = mdStateMachineDAOIF.definesStateMaster(SUSPENDED_STATE);
    Assert.assertTrue(active != null && suspended != null);

  }

  @Request
  @Test
  public void testDeleteMerge()
  {
    final MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setGenerateMdController(false);
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setGenerateMdController(false);
    mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness2.apply();

    final ComponentIF[] componentArray = new ComponentIF[] { mdBusiness, mdBusiness2 };
    generateCreateSchema(componentArray);
    final ExportMetadata metadata = new ExportMetadata();
    generateMerge(metadata, new ComponentIF[0], componentArray, new UpdateActions()
    {
      @Override
      public void perform()
      {
        metadata.addDelete(mdBusiness);
      }
    });

    try
    {
      // The first mdBusiness must not exist
      MdElementDAO.getMdElementDAO(TestFixConst.TEST_PACKAGE + "." + TestFixConst.TEST_CLASS1);
      Assert.fail();
    }
    catch (DataNotFoundException dataNotFoundException)
    {

    }
    try
    {
      // The second mdBusiness still must exist
      MdElementDAO.getMdElementDAO(TestFixConst.TEST_PACKAGE + "." + TestFixConst.TEST_CLASS2);
    }
    catch (DataNotFoundException dataNotFoundException)
    {
      Assert.fail();
    }

  }

  @Request
  @Test
  public void testUpdateMdExceptionMerge()
  { // Create test MdBusiness
    final MdExceptionDAO mdException1 = TestFixtureFactory.createMdException1();
    mdException1.setValue(MdExceptionInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdException1.setValue(MdExceptionInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdException1.setValue(MdExceptionInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdException1.setValue(MdExceptionInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdException1.setValue(MdExceptionInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdException1.apply();

    final MdAttributeConcreteDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdException1);

    final MdExceptionDAO mdException2 = TestFixtureFactory.createMdException2();
    mdException2.setValue(MdExceptionInfo.SUPER_MD_EXCEPTION, mdException1.getId());
    mdException2.setValue(MdExceptionInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdException2.setValue(MdExceptionInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdException2.apply();

    ComponentIF[] componentArray = new ComponentIF[] { mdException1, mdException2 };
    generateCreateSchema(componentArray);
    final ExportMetadata metadata = new ExportMetadata();
    generateMerge(metadata, componentArray, new UpdateActions()
    {
      @Override
      public void perform()
      {
        metadata.addNewMdAttribute(mdException1, mdAttributeBoolean);
      }
    });

    MdExceptionDAOIF mdException1IF = MdExceptionDAO.getMdException(SAXParseTest.EXCEPTION);
    MdExceptionDAOIF mdException2IF = MdExceptionDAO.getMdException(SAXParseTest.EXCEPTION2);

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
    Assert.assertEquals(mdException2IF.getValue(MdExceptionInfo.SUPER_MD_EXCEPTION), mdException1IF.getId());

    // Ensure the attributes are linked to the correct MdBusiness object
    Assert.assertEquals(attribute.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS), mdException1IF.getId());
  }

  @Request
  @Test
  public void testRevokeUserPermissionMerge()
  {
    // Create a test User
    UserDAO user = TestFixtureFactory.createUser();
    user.apply();

    RoleDAO role = TestFixtureFactory.createRole1();
    role.apply();

    role.assignMember(user);

    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setGenerateMdController(false);
    mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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
    mdStateMachine.setGenerateMdController(false);
    mdStateMachine.setValue(MdStateMachineInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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
    mdStateMachine2.setGenerateMdController(false);
    mdStateMachine2.setValue(MdStateMachineInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

    // Add permissions a State
    user.grantPermission(Operation.DELETE, state11.getId());
    user.grantPermission(Operation.READ, state11.getId());

    // Add struct permissions
    MdStructDAO mdStruct = TestFixtureFactory.createMdStruct1();
    mdStruct.setGenerateMdController(false);
    mdStruct.setValue(MdStructInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

    generateCreateSchema(metadata);

    ExportMetadata metadataGrant = new ExportMetadata();
    metadataGrant.addGrantPermissions(user);

    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, metadataGrant);

    ExportMetadata metadataAfterUpdate = new ExportMetadata();

    metadataAfterUpdate.addRevokePermissions(user);

    user.grantPermission(Operation.READ, mdAttributeChar.getId());
    user.revokeAllPermissions(tuple4.getId());

    VersionExporter.export(UPDATE_SCHEMA_2, SCHEMA, metadataAfterUpdate);

    TestFixtureFactory.delete(tuple);
    TestFixtureFactory.delete(tuple2);
    TestFixtureFactory.delete(tuple3);
    TestFixtureFactory.delete(tuple4);

    // Add permissions to the MdBusiness
    user.revokePermission(Operation.CREATE, mdBusiness1.getId());
    user.revokePermission(Operation.WRITE, mdBusiness1.getId());

    // Add attribute permissions
    user.revokePermission(Operation.WRITE, mdAttributeChar.getId());

    // Add permissions a State
    user.revokePermission(Operation.DELETE, state11.getId());
    user.revokePermission(Operation.READ, state11.getId());

    user.revokePermission(Operation.DELETE, mdStruct.getId());

    user.revokePermission(Operation.WRITE, tuple.getId());

    // Add permissions to a MdRelationship
    user.revokePermission(Operation.CREATE, mdRelationship.getId());
    user.revokePermission(Operation.DELETE, mdRelationship.getId());

    // Add permissions to an attribute defined by the MdRelationship
    user.revokePermission(Operation.READ, mdAttributeBool.getId());

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1, UPDATE_SCHEMA_2);

    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    UserDAOIF userIF = UserDAO.findUser("testUser");
    TypeTupleDAOIF tupleIF = TypeTupleDAO.findTuple(mdAttributeChar.getId(), state12.getId());
    TypeTupleDAOIF tuple2IF = TypeTupleDAO.findTuple(mdRelationship.getId(), state11.getId());
    TypeTupleDAOIF tuple3IF = TypeTupleDAO.findTuple(mdRelationship.getId(), state12.getId());
    TypeTupleDAOIF tuple4IF = TypeTupleDAO.findTuple(mdRelationship.getId(), state21.getId());

    Set<RoleDAOIF> assignedRoles = userIF.assignedRoles();
    Assert.assertEquals(1, assignedRoles.size());
    Assert.assertTrue(assignedRoles.contains(role));

    Set<Operation> operations = userIF.getAllPermissions(mdBusiness1);
    Assert.assertEquals(0, operations.size());

    operations = userIF.getAllPermissions(mdAttributeChar);
    Assert.assertEquals(0, operations.size());

    operations = userIF.getAllPermissions(state11);
    Assert.assertEquals(0, operations.size());

    operations = userIF.getAllPermissions(mdRelationship);
    Assert.assertEquals(0, operations.size());

    operations = userIF.getAllPermissions(mdAttributeBool);
    Assert.assertEquals(0, operations.size());

    Assert.assertNull(tupleIF);
    Assert.assertNull(tuple2IF);
    Assert.assertNull(tuple3IF);

    operations = userIF.getAllPermissions(tuple4IF);
    Assert.assertEquals(1, operations.size());
    Assert.assertTrue(operations.contains(Operation.ADD_PARENT));

    operations = userIF.getAllPermissions(mdStruct);
    Assert.assertEquals(0, operations.size());
  }

  @Request
  @Test
  public void testUserPermissionMerge()
  {
    // Create a test User
    UserDAO user = TestFixtureFactory.createUser();
    user.apply();

    RoleDAO role = TestFixtureFactory.createRole1();
    role.apply();

    role.assignMember(user);

    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setGenerateMdController(false);
    mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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
    mdStateMachine.setGenerateMdController(false);
    mdStateMachine.setValue(MdStateMachineInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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
    mdStateMachine2.setGenerateMdController(false);
    mdStateMachine2.setValue(MdStateMachineInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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
    mdRelationship.setGenerateMdController(false);
    mdRelationship.setValue(MdRelationshipInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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
    mdStruct.setGenerateMdController(false);
    mdStruct.setValue(MdStructInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

    generateCreateSchema(metadata);

    ExportMetadata metadataAfterUpdate = new ExportMetadata();
    // metadata.addExisting(BusinessDAO.get(com.runwaysdk.constants.Locale.EN_US.getId()));
    // metadata.addCreate(user);
    metadataAfterUpdate.addGrantPermissions(user);

    user.grantPermission(Operation.DELETE_PARENT, tuple4.getId());

    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, metadataAfterUpdate);

    TestFixtureFactory.delete(tuple);
    TestFixtureFactory.delete(tuple2);
    TestFixtureFactory.delete(tuple3);
    TestFixtureFactory.delete(tuple4);
    TestFixtureFactory.delete(user);

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    UserDAOIF userIF = UserDAO.findUser("testUser");
    TypeTupleDAOIF tupleIF = TypeTupleDAO.findTuple(mdAttributeChar.getId(), state12.getId());
    TypeTupleDAOIF tuple2IF = TypeTupleDAO.findTuple(mdRelationship.getId(), state11.getId());
    TypeTupleDAOIF tuple3IF = TypeTupleDAO.findTuple(mdRelationship.getId(), state12.getId());
    TypeTupleDAOIF tuple4IF = TypeTupleDAO.findTuple(mdRelationship.getId(), state21.getId());

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

    operations = userIF.getAllPermissions(state11);
    Assert.assertEquals(2, operations.size());
    Assert.assertTrue(operations.contains(Operation.DELETE));
    Assert.assertTrue(operations.contains(Operation.READ));

    operations = userIF.getAllPermissions(tupleIF);
    Assert.assertEquals(1, operations.size());
    Assert.assertTrue(operations.contains(Operation.WRITE));

    operations = userIF.getAllPermissions(mdRelationship);
    Assert.assertEquals(2, operations.size());
    Assert.assertTrue(operations.contains(Operation.DELETE));
    Assert.assertTrue(operations.contains(Operation.CREATE));

    operations = userIF.getAllPermissions(mdAttributeBool);
    Assert.assertEquals(1, operations.size());
    Assert.assertTrue(operations.contains(Operation.READ));

    operations = userIF.getAllPermissions(tuple2IF);
    Assert.assertEquals(2, operations.size());
    Assert.assertTrue(operations.contains(Operation.ADD_CHILD));
    Assert.assertTrue(operations.contains(Operation.READ_CHILD));

    operations = userIF.getAllPermissions(tuple3IF);
    Assert.assertEquals(1, operations.size());
    Assert.assertTrue(operations.contains(Operation.WRITE_CHILD));

    operations = userIF.getAllPermissions(tuple4IF);
    Assert.assertEquals(2, operations.size());
    Assert.assertTrue(operations.contains(Operation.ADD_PARENT));
    Assert.assertTrue(operations.contains(Operation.DELETE_PARENT));

    operations = userIF.getAllPermissions(mdStruct);
    Assert.assertEquals(1, operations.size());
    Assert.assertTrue(operations.contains(Operation.DELETE));
  }

  @Request
  @Test
  public void testAttributePermissionDelete()
  {
    // Create a test User
    UserDAO user = TestFixtureFactory.createUser();
    user.apply();

    RoleDAO role = TestFixtureFactory.createRole1();
    role.apply();

    role.assignMember(user);

    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setGenerateMdController(false);
    mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness2.apply();

    MdAttributeConcreteDAO mdAttributeChar = TestFixtureFactory.addCharacterAttribute(mdBusiness1);
    mdAttributeChar.apply();

    // Add permissions to the MdBusiness
    user.grantPermission(Operation.CREATE, mdBusiness1.getId());
    user.grantPermission(Operation.WRITE, mdBusiness1.getId());

    // Add attribute permissions
    user.grantPermission(Operation.WRITE, mdAttributeChar.getId());
    user.grantPermission(Operation.READ, mdAttributeChar.getId());

    // Export the permissions
    ExportMetadata metadata = new ExportMetadata();
    metadata.addCreate(mdBusiness1, mdBusiness2, user);
    metadata.addGrantPermissions(user);

    generateCreateSchema(metadata);

    ExportMetadata metadataAfterUpdate = new ExportMetadata();
    metadataAfterUpdate.addDelete(mdAttributeChar);

    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, metadataAfterUpdate);

    TestFixtureFactory.delete(user);
    TestFixtureFactory.delete(mdBusiness2);
    TestFixtureFactory.delete(mdBusiness1);

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    UserDAOIF userIF = UserDAO.findUser("testUser");
    MdBusinessDAOIF mdBusiness1IF = MdBusinessDAO.getMdBusinessDAO(mdBusiness1.definesType());

    Set<RoleDAOIF> assignedRoles = userIF.assignedRoles();
    Assert.assertEquals(1, assignedRoles.size());
    Assert.assertTrue(assignedRoles.contains(role));

    Set<Operation> operations = userIF.getAllPermissions(mdBusiness1IF);
    Assert.assertEquals(2, operations.size());
    Assert.assertTrue(operations.contains(Operation.WRITE));
    Assert.assertTrue(operations.contains(Operation.CREATE));
  }

  @Request
  @Test
  public void testControllerDeleteOrdering()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(true);
    mdBusiness1.apply();

    try
    {
      MdControllerDAOIF mdController = MdControllerDAO.getMdControllerDAO(mdBusiness1.definesType() + "Controller");

      VersionExporter.export(CREATE_SCHEMA, SCHEMA, ExportMetadata.buildCreate(mdBusiness1));

      try
      {
        VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, ExportMetadata.buildDelete(mdController, mdBusiness1));

        mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

        TestFixtureFactory.delete(mdController);
        TestFixtureFactory.delete(mdBusiness1);

        // Import merge file
        VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);
      }
      finally
      {
        // mdAction.delete();
      }
    }
    finally
    {
      // mdBusiness1.delete();
    }
  }

  @Request
  @Test
  public void testControllerDeleteReverseOrdering()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(true);
    mdBusiness1.apply();

    try
    {
      MdControllerDAOIF mdController = MdControllerDAO.getMdControllerDAO(mdBusiness1.definesType() + "Controller");

      VersionExporter.export(CREATE_SCHEMA, SCHEMA, ExportMetadata.buildCreate(mdBusiness1));

      try
      {
        VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, ExportMetadata.buildDelete(mdBusiness1, mdController));

        mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

        TestFixtureFactory.delete(mdController);
        TestFixtureFactory.delete(mdBusiness1);

        // Import merge file
        VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);
      }
      finally
      {
        // mdAction.delete();
      }
    }
    finally
    {
      // mdBusiness1.delete();
    }
  }

  @Request
  @Test
  public void testDeleteRelationshipAndController()
  {
    MdBusinessDAO parentMdBusiness = TestFixtureFactory.createMdBusiness1();
    parentMdBusiness.setGenerateMdController(true);
    parentMdBusiness.apply();

    MdBusinessDAO childMdBusiness = TestFixtureFactory.createMdBusiness2();
    childMdBusiness.setGenerateMdController(true);
    childMdBusiness.apply();

    try
    {
      MdRelationshipDAO mdRelationship = TestFixtureFactory.createMdRelationship1(parentMdBusiness, childMdBusiness);
      mdRelationship.setGenerateMdController(true);
      mdRelationship.apply();

      MdControllerDAOIF mdController = MdControllerDAO.getMdControllerDAO(mdRelationship.definesType() + "Controller");

      VersionExporter.export(CREATE_SCHEMA, SCHEMA, ExportMetadata.buildCreate(mdRelationship));

      VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, ExportMetadata.buildDelete(mdRelationship, mdController));

      TestFixtureFactory.delete(mdController);
      TestFixtureFactory.delete(mdRelationship);

      mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

      VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

      try
      {
        MdRelationshipDAO.getMdRelationshipDAO(mdRelationship.definesType());

        Assert.fail("Able to get a deleted MdRelationship");
      }
      catch (DataNotFoundException e)
      {
        // This is expected
      }
    }
    finally
    {
      TestFixtureFactory.delete(parentMdBusiness);
      TestFixtureFactory.delete(childMdBusiness);
    }
  }

  @Request
  @Test
  public void testDeleteMdMethodPermission()
  {
    // Create a test User
    UserDAO user = TestFixtureFactory.createUser();
    user.apply();

    RoleDAO role = TestFixtureFactory.createRole1();
    role.apply();

    role.assignMember(user);

    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setGenerateMdController(false);
    mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness2.apply();

    MdMethodDAO mdMethod = TestFixtureFactory.createMdMethod(mdBusiness1);
    mdMethod.apply();

    MethodActorDAO methodActor = TestFixtureFactory.createMethodActor(mdMethod);
    methodActor.apply();

    methodActor.grantPermission(Operation.CREATE, mdBusiness1.getId());
    methodActor.grantPermission(Operation.WRITE, mdBusiness1.getId());

    // Add permissions to the MdBusiness
    user.grantPermission(Operation.CREATE, mdBusiness1.getId());
    user.grantPermission(Operation.WRITE, mdBusiness1.getId());

    // Add attribute permissions
    user.grantPermission(Operation.EXECUTE, mdMethod.getId());

    // Export the permissions
    ExportMetadata metadata = new ExportMetadata();
    metadata.addCreate(mdBusiness1, mdBusiness2, user);
    metadata.addGrantPermissions(user, methodActor);

    generateCreateSchema(metadata);

    ExportMetadata metadataAfterUpdate = new ExportMetadata();
    metadataAfterUpdate.addDelete(mdMethod);

    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, metadataAfterUpdate);

    TestFixtureFactory.delete(user);
    TestFixtureFactory.delete(mdBusiness2);
    TestFixtureFactory.delete(mdBusiness1);

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    UserDAOIF userIF = UserDAO.findUser("testUser");
    MdBusinessDAOIF mdBusiness1IF = MdBusinessDAO.getMdBusinessDAO(mdBusiness1.definesType());

    Set<RoleDAOIF> assignedRoles = userIF.assignedRoles();
    Assert.assertEquals(1, assignedRoles.size());
    Assert.assertTrue(assignedRoles.contains(role));

    Set<Operation> operations = userIF.getAllPermissions(mdBusiness1IF);
    Assert.assertEquals(2, operations.size());
    Assert.assertTrue(operations.contains(Operation.WRITE));
    Assert.assertTrue(operations.contains(Operation.CREATE));
  }

  @Request
  @Test
  public void testUpdatedAttributePermissionDelete()
  {
    // Create a test User
    UserDAO user = TestFixtureFactory.createUser();
    user.apply();

    RoleDAO role = TestFixtureFactory.createRole1();
    role.apply();

    role.assignMember(user);

    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setGenerateMdController(false);
    mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness2.apply();

    // Add permissions to the MdBusiness
    user.grantPermission(Operation.CREATE, mdBusiness1.getId());
    user.grantPermission(Operation.WRITE, mdBusiness1.getId());

    // Export the permissions
    ExportMetadata metadata = new ExportMetadata();
    metadata.addCreate(mdBusiness1, mdBusiness2, user);

    generateCreateSchema(metadata);

    MdAttributeConcreteDAO mdAttributeChar = TestFixtureFactory.addCharacterAttribute(mdBusiness1);

    ExportMetadata metadataAfterCreate = new ExportMetadata();
    metadataAfterCreate.addUpdate(mdBusiness1);
    metadataAfterCreate.addNewMdAttribute(mdBusiness1, mdAttributeChar);

    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, metadataAfterCreate);

    mdAttributeChar.apply();

    // Add attribute permissions
    user.grantPermission(Operation.WRITE, mdAttributeChar.getId());
    user.grantPermission(Operation.READ, mdAttributeChar.getId());

    ExportMetadata metadataAfterUpdate = new ExportMetadata();
    metadataAfterUpdate.addGrantPermissions(user);

    VersionExporter.export(UPDATE_SCHEMA_2, SCHEMA, metadataAfterUpdate);

    ExportMetadata metadataAfterPermissions = new ExportMetadata();
    metadataAfterPermissions.addDelete(mdAttributeChar);

    VersionExporter.export(UPDATE_SCHEMA_3, SCHEMA, metadataAfterPermissions);

    TestFixtureFactory.delete(user);
    TestFixtureFactory.delete(mdBusiness2);
    TestFixtureFactory.delete(mdBusiness1);

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1, UPDATE_SCHEMA_2, UPDATE_SCHEMA_3);

    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    UserDAOIF userIF = UserDAO.findUser("testUser");
    MdBusinessDAOIF mdBusiness1IF = MdBusinessDAO.getMdBusinessDAO(mdBusiness1.definesType());

    Set<RoleDAOIF> assignedRoles = userIF.assignedRoles();
    Assert.assertEquals(1, assignedRoles.size());
    Assert.assertTrue(assignedRoles.contains(role));

    Set<Operation> operations = userIF.getAllPermissions(mdBusiness1IF);
    Assert.assertEquals(2, operations.size());
    Assert.assertTrue(operations.contains(Operation.WRITE));
    Assert.assertTrue(operations.contains(Operation.CREATE));
  }

  @Request
  @Test
  public void testUpdateOfCreatedAttributeOnPartialSchemaElement()
  {
    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    // Export partial schema element with definition of new attribute
    MdAttributeConcreteDAO mdAttributeChar = TestFixtureFactory.addCharacterAttribute(mdBusiness1);

    ExportMetadata metadata = new ExportMetadata();
    metadata.addUpdate(mdBusiness1);
    metadata.addNewMdAttribute(mdBusiness1, mdAttributeChar);

    generateCreateSchema(metadata);

    // Update and apply the MdAttribute definition so that it is different then
    // the exported definition
    String displayLabel = "Updated Display";

    mdAttributeChar.setStructValue(MdAttributeConcreteInfo.DISPLAY_LABEL, "defaultLocale", displayLabel);
    mdAttributeChar.apply();

    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, ExportMetadata.buildUpdate(mdBusiness1));

    TestFixtureFactory.delete(mdAttributeChar);

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    MdBusinessDAOIF mdBusiness1IF = MdBusinessDAO.getMdBusinessDAO(mdBusiness1.definesType());

    MdAttributeDAOIF mdAttributeIF = mdBusiness1IF.definesAttribute(mdAttributeChar.definesAttribute());

    Assert.assertNotNull(mdAttributeIF);
    Assert.assertEquals(displayLabel, mdAttributeIF.getStructValue(MdAttributeConcreteInfo.DISPLAY_LABEL, "defaultLocale"));
  }

  @Request
  @Test
  public void testUpdateOfCreatedMethodOnPartialSchemaElement()
  {
    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    // Export partial schema element with definition of new attribute
    MdMethodDAO mdMethod = TestFixtureFactory.createMdMethod(mdBusiness1);
    MdParameterDAO mdParameter = TestFixtureFactory.createMdParameter(null, "parameter0", 0);

    ExportMetadata metadata = new ExportMetadata();
    metadata.addUpdate(mdBusiness1);
    metadata.addNewMdMethod(mdBusiness1, mdMethod, mdParameter);

    generateCreateSchema(metadata);

    String displayLabel = "Updated Display";

    mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, "defaultLocale", displayLabel);
    mdMethod.apply();

    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod.getId());
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, "defaultLocale", displayLabel);
    mdParameter.apply();

    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, ExportMetadata.buildUpdate(mdBusiness1));

    TestFixtureFactory.delete(mdParameter);
    TestFixtureFactory.delete(mdMethod);

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    MdBusinessDAOIF mdBusiness1IF = MdBusinessDAO.getMdBusinessDAO(mdBusiness1.definesType());
    MdMethodDAOIF mdMethodIF = mdBusiness1IF.getMdMethod(mdMethod.getName());

    Assert.assertNotNull(mdMethodIF);
    Assert.assertEquals(displayLabel, mdMethodIF.getStructValue(MdMethodInfo.DISPLAY_LABEL, "defaultLocale"));

    List<MdParameterDAOIF> list = mdMethodIF.getMdParameterDAOs();

    Assert.assertEquals(1, list.size());
    Assert.assertEquals(displayLabel, list.get(0).getStructValue(MdParameterInfo.DISPLAY_LABEL, "defaultLocale"));
  }

  @Request
  @Test
  public void testDeleteThenUpdateOfMdMethod()
  {
    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdMethodDAO mdMethod = TestFixtureFactory.createMdMethod(mdBusiness1);
    mdMethod.apply();

    MdParameterDAO mdParameter = TestFixtureFactory.createMdParameter(mdMethod, "parameter0", 0);
    mdParameter.apply();

    MdParameterDAO mdParameter1 = TestFixtureFactory.createMdParameter(mdMethod, "parameter1", 1);
    mdParameter1.apply();

    MdParameterDAO mdParameter2 = TestFixtureFactory.createMdParameter(mdMethod, "parameter2", 2);
    mdParameter2.apply();

    MdParameterDAO mdParameter3 = TestFixtureFactory.createMdParameter(mdMethod, "parameter3", 3);
    mdParameter3.apply();

    generateCreateSchema(ExportMetadata.buildCreate(mdBusiness1));

    MdMethodDAO _mdMethod = TestFixtureFactory.createMdMethod(mdBusiness1);
    MdParameterDAO _mdParameter = TestFixtureFactory.createMdParameter(null, "parameter0", 0);
    MdParameterDAO _mdParameter1 = TestFixtureFactory.createMdParameter(null, "parameter1", 1);
    MdParameterDAO _mdParameter2 = TestFixtureFactory.createMdParameter(null, "parameter2", 2);

    // Delete the referenced type.
    TestFixtureFactory.delete(mdMethod);

    ExportMetadata metadata = ExportMetadata.buildDelete(mdMethod);
    metadata.addUpdate(mdBusiness1);
    metadata.addNewMdMethod(mdBusiness1, _mdMethod, new MdParameterDAO[] { _mdParameter, _mdParameter1, _mdParameter2 });

    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, metadata);

    TestFixtureFactory.delete(mdBusiness1);

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    MdBusinessDAOIF mdBusiness1IF = MdBusinessDAO.getMdBusinessDAO(mdBusiness1.definesType());
    MdMethodDAOIF mdMethodIF = mdBusiness1IF.getMdMethod(TestFixConst.TEST_METHOD_NAME);

    Assert.assertNotNull(mdMethodIF);

    List<MdParameterDAOIF> list = mdMethodIF.getMdParameterDAOs();
    List<String> names = new LinkedList<String>();

    Assert.assertEquals(3, list.size());

    for (MdParameterDAOIF mdParameterIF : list)
    {
      names.add(mdParameterIF.getParameterName());
    }

    Assert.assertTrue(names.contains("parameter0"));
    Assert.assertTrue(names.contains("parameter1"));
    Assert.assertTrue(names.contains("parameter2"));
  }

  @Request
  @Test
  public void testDeleteOfReferencedAttributeType()
  {
    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setGenerateMdController(false);
    mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness2.apply();

    MdAttributeReferenceDAO mdAttribute = TestFixtureFactory.addReferenceAttribute(mdBusiness1, mdBusiness2);
    mdAttribute.apply();

    generateCreateSchema(ExportMetadata.buildCreate(mdBusiness1, mdBusiness2));

    // Delete the referenced type.
    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, ExportMetadata.buildDelete(mdBusiness2));

    TestFixtureFactory.delete(mdBusiness1);
    TestFixtureFactory.delete(mdBusiness2);

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    MdBusinessDAOIF mdBusiness1IF = MdBusinessDAO.getMdBusinessDAO(mdBusiness1.definesType());
    MdAttributeDAOIF mdAttributeIF = mdBusiness1IF.definesAttribute(mdAttribute.definesAttribute());

    Assert.assertNull(mdAttributeIF);
  }

  @Request
  @Test
  public void testDeleteOfEnumerationMasterWithItems()
  {
    // Create test MdBusiness
    MdBusinessDAO enumerationMaster = TestFixtureFactory.createEnumClass1();
    enumerationMaster.setGenerateMdController(false);
    enumerationMaster.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    enumerationMaster.apply();

    EnumerationItemDAO item = EnumerationItemDAO.newInstance(enumerationMaster.definesType());
    item.setValue("enumName", "TEST_ITEM");
    item.setStructValue("displayLabel", "defaultLocale", "Test Item");
    item.apply();

    generateCreateSchema(ExportMetadata.buildCreate(enumerationMaster, item));

    // Delete the referenced type.
    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, ExportMetadata.buildDelete(enumerationMaster));

    TestFixtureFactory.delete(enumerationMaster);

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    try
    {
      MdBusinessDAO.getMdBusinessDAO(enumerationMaster.definesType());

      Assert.fail("Able to get an enumeration master which was suppose to be deleted");
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }
  }

  @Request
  @Test
  public void testCreateThenDeleteThenCreateOfObject()
  {
    String key = "TestKey";

    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdLocalStructDAO mdLocalStruct = TestFixtureFactory.createMdLocalStruct();
    mdLocalStruct.setGenerateMdController(false);
    mdLocalStruct.setValue(MdLocalStructInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdLocalStruct.apply();

    MdAttributeLocalCharacterDAO.addDefaultLocale(mdLocalStruct);

    MdAttributeCharacterDAO mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdBusiness1);
    mdAttributeCharacter.apply();

    MdAttributeStructDAO mdAttributeLocal = TestFixtureFactory.addLocalCharacterAttribute(mdBusiness1, mdLocalStruct);
    mdAttributeLocal.apply();

    BusinessDAO object = BusinessDAO.newInstance(mdBusiness1.definesType());
    object.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character");
    object.getAttribute("keyName").setValue(key);
    object.apply();

    // Partial schema element definition
    generateCreateSchema(ExportMetadata.buildCreate(object));

    TestFixtureFactory.delete(object);

    // Delete the referenced type.
    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, ExportMetadata.buildDelete(object));

    BusinessDAO object2 = BusinessDAO.newInstance(mdBusiness1.definesType());
    object2.setStructValue("testLocalCharacter", "defaultLocale", "Default Locale");
    object2.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Updated Locale");
    object2.getAttribute("keyName").setValue(key);
    object2.apply();

    VersionExporter.export(UPDATE_SCHEMA_2, SCHEMA, ExportMetadata.buildCreate(object2));

    TestFixtureFactory.delete(object2);

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1, UPDATE_SCHEMA_2);

    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    List<String> ids = MdBusinessDAO.getEntityIdsDB(mdBusiness1.definesType());

    Assert.assertEquals(1, ids.size());

    BusinessDAOIF objectIF = BusinessDAO.get(ids.get(0));

    Assert.assertNotNull(objectIF);
    Assert.assertEquals("Updated Locale", objectIF.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
    Assert.assertEquals("Default Locale", objectIF.getStructValue("testLocalCharacter", "defaultLocale"));
  }

  @Request
  @Test
  public void testUpdateOfAttributeStructOnObject()
  {
    String key = "TestKey";

    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdLocalStructDAO mdLocalStruct = TestFixtureFactory.createMdLocalStruct();
    mdLocalStruct.setGenerateMdController(false);
    mdLocalStruct.setValue(MdLocalStructInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdLocalStruct.apply();

    MdAttributeLocalCharacterDAO.addDefaultLocale(mdLocalStruct);

    MdAttributeCharacterDAO mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdBusiness1);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter.apply();

    MdAttributeStructDAO mdAttributeLocal = TestFixtureFactory.addLocalCharacterAttribute(mdBusiness1, mdLocalStruct);
    mdAttributeLocal.apply();

    BusinessDAO object = BusinessDAO.newInstance(mdBusiness1.definesType());
    object.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character");
    object.getAttribute("keyName").setValue(key);
    object.setStructValue("testLocalCharacter", "defaultLocale", "Default Locale");
    object.apply();

    // Partial schema element definition
    generateCreateSchema(ExportMetadata.buildCreate(object));

    String updatedValue = "Updated Locale";

    object.setStructValue("testLocalCharacter", "defaultLocale", updatedValue);
    object.setValue(TestFixConst.ATTRIBUTE_CHARACTER, updatedValue);
    object.apply();

    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, ExportMetadata.buildUpdate(object));

    TestFixtureFactory.delete(object);

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    BusinessDAOIF objectIF = BusinessDAO.get(mdBusiness1.definesType(), key);

    Assert.assertNotNull(objectIF);
    Assert.assertEquals(updatedValue, objectIF.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
    Assert.assertEquals(updatedValue, objectIF.getStructValue("testLocalCharacter", "defaultLocale"));
  }

  @Request
  @Test
  public void testUpdateOfAttributeEnumerationOnObject()
  {
    String key = "TestKey";

    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdBusinessDAO mdEnumerationMaster = TestFixtureFactory.createEnumClass1();
    mdEnumerationMaster.setGenerateMdController(false);
    mdEnumerationMaster.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdEnumerationMaster.apply();

    EnumerationItemDAO hearts = TestFixtureFactory.createEnumObject(mdEnumerationMaster, "Hearts");
    hearts.apply();

    EnumerationItemDAO clubs = TestFixtureFactory.createEnumObject(mdEnumerationMaster, "Clubs");
    clubs.apply();

    MdEnumerationDAO mdEnumeration = TestFixtureFactory.createMdEnumeation1(mdEnumerationMaster);
    mdEnumeration.setValue(MdEnumerationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdEnumeration.apply();

    MdAttributeEnumerationDAO mdAttribute = TestFixtureFactory.addEnumerationAttribute(mdBusiness1, mdEnumeration);
    mdAttribute.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
    mdAttribute.apply();

    BusinessDAO object = BusinessDAO.newInstance(mdBusiness1.definesType());
    object.addItem(mdAttribute.definesAttribute(), hearts.getId());
    object.getAttribute("keyName").setValue(key);
    object.apply();

    // Partial schema element definition
    generateCreateSchema(ExportMetadata.buildCreate(object));

    object.addItem(mdAttribute.definesAttribute(), clubs.getId());
    object.apply();

    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, ExportMetadata.buildUpdate(object));

    TestFixtureFactory.delete(object);

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    BusinessDAOIF objectIF = BusinessDAO.get(mdBusiness1.definesType(), key);

    Assert.assertNotNull(objectIF);

    AttributeEnumerationIF attributeIF = (AttributeEnumerationIF) objectIF.getAttributeIF(mdAttribute.definesAttribute());
    EnumerationItemDAOIF[] attributes = attributeIF.dereference();

    Assert.assertEquals(2, attributes.length);
    Assert.assertTrue(attributes[0].getName().equals(hearts.getName()) || attributes[1].getName().equals(hearts.getName()));
    Assert.assertTrue(attributes[0].getName().equals(clubs.getName()) || attributes[1].getName().equals(clubs.getName()));
  }

  @Request
  @Test
  public void testDeleteOfVirtualAttribute()
  {
    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdAttributeCharacterDAO mdAttribute = TestFixtureFactory.addCharacterAttribute(mdBusiness1);
    mdAttribute.apply();

    MdViewDAO mdView = TestFixtureFactory.createMdView1();
    mdView.setValue(MdViewInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdView.apply();

    MdAttributeVirtualDAO mdAttributeVirtual = TestFixtureFactory.addVirtualAttribute(mdView, mdAttribute);
    mdAttributeVirtual.apply();

    // Partial schema element definition
    generateCreateSchema(ExportMetadata.buildCreate(mdBusiness1, mdView));

    // Delete the referenced type.
    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, ExportMetadata.buildDelete(mdAttribute, mdAttributeVirtual));

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

    TestFixtureFactory.delete(mdBusiness1);
    TestFixtureFactory.delete(mdView);

    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(mdBusiness1.definesType());
    MdViewDAOIF mdViewIF = MdViewDAO.getMdViewDAO(mdView.definesType());

    Assert.assertNull(mdBusinessIF.definesAttribute(mdAttribute.definesAttribute()));
    Assert.assertNull(mdViewIF.definesAttribute(mdAttributeVirtual.definesAttribute()));
  }

  @Request
  @Test
  public void testDeleteOfPartialSchemaReferencedAttributeType()
  {
    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setGenerateMdController(false);
    mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness2.apply();

    MdAttributeReferenceDAO mdAttribute = TestFixtureFactory.addReferenceAttribute(mdBusiness1, mdBusiness2);

    ExportMetadata metadata = ExportMetadata.buildUpdate(mdBusiness1);
    metadata.addCreate(mdBusiness2);
    metadata.addNewMdAttribute(mdBusiness1, mdAttribute);

    // Partial schema element definition
    generateCreateSchema(metadata);

    // Delete the referenced type.
    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, ExportMetadata.buildDelete(mdBusiness2));

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

    TestFixtureFactory.delete(mdBusiness2);

    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    MdBusinessDAOIF mdBusiness1IF = MdBusinessDAO.getMdBusinessDAO(mdBusiness1.definesType());
    MdAttributeDAOIF mdAttributeIF = mdBusiness1IF.definesAttribute(mdAttribute.definesAttribute());

    Assert.assertNull(mdAttributeIF);
  }

  @Request
  @Test
  public void testIndexMerge()
  {
    final MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
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
    mdIndex.setValue(MdIndexInfo.MD_ENTITY, mdBusiness1.getId());
    mdIndex.setValue(MdIndexInfo.UNIQUE, MdAttributeBooleanInfo.TRUE);
    mdIndex.apply();

    mdIndex.addAttribute(mdBoolean, 0);
    mdIndex.apply();

    mdIndex.addAttribute(mdChar, 1);
    mdIndex.apply();

    VersionExporter.export(CREATE_SCHEMA, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdIndex, mdBusiness1 }));

    final ExportMetadata updateMetadata = new ExportMetadata();
    generateMerge(updateMetadata, new ComponentIF[] { mdBusiness1 }, new ComponentIF[] { mdIndex, mdBusiness1 }, new UpdateActions()
    {
      @Override
      public void perform()
      {
        mdBusiness1.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "mdBusiness Update Test in Presence of Index");

      }
    });

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
    Assert.assertEquals(mdBusiness1.getStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), "mdBusiness Update Test in Presence of Index");
  }

  /**
   * Ensure that multiple indexes on the same type are not merged together.
   */
  @Request
  @Test
  public void testMultiIndexMerge()
  {
    final MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setGenerateMdController(false);
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeConcreteDAO mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdBusiness);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeBoolean.apply();

    MdAttributeConcreteDAO mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdBusiness);
    mdAttributeCharacter.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.apply();

    MdAttributeDateDAO mdAttributeDate = TestFixtureFactory.addDateAttribute(mdBusiness);
    mdAttributeDate.setValue(MdAttributeDateInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeDate.apply();

    MdIndexDAO firstMdIndex = MdIndexDAO.newInstance();
    firstMdIndex.setStructValue(MdIndexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "index");
    firstMdIndex.setValue(MdIndexInfo.MD_ENTITY, mdBusiness.getId());
    firstMdIndex.setValue(MdIndexInfo.UNIQUE, MdAttributeBooleanInfo.TRUE);
    firstMdIndex.apply();

    firstMdIndex.addAttribute(mdAttributeBoolean, 0);
    firstMdIndex.apply();

    String newId = firstMdIndex.apply();
    firstMdIndex = MdIndexDAO.get(newId).getBusinessDAO();

    firstMdIndex.addAttribute(mdAttributeCharacter, 1);
    firstMdIndex.apply();

    newId = firstMdIndex.apply();
    firstMdIndex = MdIndexDAO.get(newId).getBusinessDAO();

    VersionExporter.export(CREATE_SCHEMA, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { firstMdIndex, mdBusiness }));

    firstMdIndex.delete();

    MdIndexDAO secondMdIndex = MdIndexDAO.newInstance();
    secondMdIndex.setStructValue(MdIndexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "index");
    secondMdIndex.setValue(MdIndexInfo.MD_ENTITY, mdBusiness.getId());
    secondMdIndex.setValue(MdIndexInfo.UNIQUE, MdAttributeBooleanInfo.TRUE);
    secondMdIndex.apply();

    secondMdIndex.addAttribute(mdAttributeBoolean, 0);
    secondMdIndex.apply();

    secondMdIndex.addAttribute(mdAttributeDate, 1);
    secondMdIndex.apply();

    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { secondMdIndex }));

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

    TestFixtureFactory.delete(mdBusiness);

    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(CLASS);

    List<MdIndexDAOIF> indexs = mdBusinessIF.getIndexes();

    Assert.assertEquals(2, indexs.size());
  }

  @Request
  @Test
  public void testAddMdActionParameterToPartialMdControllerMerge()
  {
    final MdControllerDAO mdController = MdControllerDAO.newInstance();
    mdController.setValue(MdControllerInfo.NAME, "Controller1");
    mdController.setValue(MdControllerInfo.PACKAGE, "test.xmlclasses");
    mdController.setStructValue(MdControllerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Controller1");
    mdController.setStructValue(MdControllerInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Controller1");
    mdController.setValue(MdControllerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdController.apply();

    final MdActionDAO mdAction = MdActionDAO.newInstance();
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
    final MdParameterDAO mdParameter3 = MdParameterDAO.newInstance();
    mdParameter3.setValue(MdParameterInfo.NAME, "param3");
    mdParameter3.setValue(MdParameterInfo.TYPE, "java.lang.String");
    mdParameter3.setValue(MdParameterInfo.ORDER, "4");
    mdParameter3.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "param3");

    final MdActionDAO mdAction2 = MdActionDAO.newInstance();
    mdAction2.setValue(MdActionInfo.NAME, "checkout");
    mdAction2.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkout");
    mdAction2.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkout");

    final MdParameterDAO mdParameter4 = MdParameterDAO.newInstance();
    mdParameter4.setValue(MdParameterInfo.NAME, "param4");
    mdParameter4.setValue(MdParameterInfo.TYPE, "java.lang.String");
    mdParameter4.setValue(MdParameterInfo.ORDER, "0");
    mdParameter4.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "param4");

    // generateCreateSchema(new ComponentIF[] { mdController });
    ExportMetadata firstUpdate = new ExportMetadata();
    firstUpdate.addUpdate(mdController);
    firstUpdate.addNewMdAction(mdController, mdAction2, mdParameter4);
    VersionExporter.export(CREATE_SCHEMA, SCHEMA, firstUpdate);

    // Export mdController with all of its MdActions defined in the database,
    // and
    // export a new MdAction for the mdController, as well as a new MdParameter
    // for an existing MdAction of mdController
    final ExportMetadata secondUpdate = new ExportMetadata();
    generateMerge(secondUpdate, new ComponentIF[] { mdController }, new ComponentIF[] {}, new UpdateActions()
    {
      public void perform()
      {
        secondUpdate.addNewMdParameter(mdAction, mdParameter3);
      }
    });

    Assert.assertTrue(MdControllerDAO.isDefined("test.xmlclasses.Controller1"));

    MdControllerDAOIF mdControllerIF = MdControllerDAO.getMdControllerDAO("test.xmlclasses.Controller1");
    Assert.assertEquals("Controller1", mdControllerIF.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals("Controller1", mdControllerIF.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<MdActionDAOIF> mdActions = mdControllerIF.getMdActionDAOs();

    Assert.assertEquals(2, mdActions.size());

    int i = 0;
    int j = 1;

    if (!"checkin".equals(mdActions.get(0).getName()))
    {
      i = 1;
      j = 0;
    }

    Assert.assertEquals("checkin", mdActions.get(i).getName());
    Assert.assertEquals("checkin", mdActions.get(i).getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals("checkin", mdActions.get(i).getDescription(CommonProperties.getDefaultLocale()));

    List<MdParameterDAOIF> mdParameters = mdActions.get(i).getMdParameterDAOs();
    Assert.assertEquals(3, mdParameters.size());
    Assert.assertEquals("param1", mdParameters.get(0).getParameterName());
    Assert.assertEquals("java.lang.String", mdParameters.get(0).getParameterType().getType());
    Assert.assertEquals("0", mdParameters.get(0).getParameterOrder());
    Assert.assertEquals("param1", mdParameters.get(0).getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals("param1", mdParameters.get(0).getDescription(CommonProperties.getDefaultLocale()));

    Assert.assertEquals("param2", mdParameters.get(1).getParameterName());
    Assert.assertEquals("java.lang.Integer", mdParameters.get(1).getParameterType().getType());
    Assert.assertEquals("1", mdParameters.get(1).getParameterOrder());
    Assert.assertEquals("param2", mdParameters.get(1).getDisplayLabel(CommonProperties.getDefaultLocale()));

    Assert.assertEquals("param3", mdParameters.get(2).getParameterName());
    Assert.assertEquals("java.lang.String", mdParameters.get(2).getParameterType().getType());
    Assert.assertEquals("4", mdParameters.get(2).getParameterOrder());
    Assert.assertEquals("param3", mdParameters.get(2).getDisplayLabel(CommonProperties.getDefaultLocale()));

    Assert.assertEquals("checkout", mdActions.get(j).getName());
    Assert.assertEquals("checkout", mdActions.get(j).getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals("checkout", mdActions.get(j).getDescription(CommonProperties.getDefaultLocale()));

    mdParameters = mdActions.get(j).getMdParameterDAOs();
    Assert.assertEquals(1, mdParameters.size());
    Assert.assertEquals("param4", mdParameters.get(0).getParameterName());
    Assert.assertEquals("java.lang.String", mdParameters.get(0).getParameterType().getType());
    Assert.assertEquals("0", mdParameters.get(0).getParameterOrder());
    Assert.assertEquals("param4", mdParameters.get(0).getDisplayLabel(CommonProperties.getDefaultLocale()));
  }

  @Request
  @Test
  public void testAddMdActionToPartialMdControllerMerge()
  {
    final MdControllerDAO mdController = MdControllerDAO.newInstance();
    mdController.setValue(MdControllerInfo.NAME, "Controller1");
    mdController.setValue(MdControllerInfo.PACKAGE, "test.xmlclasses");
    mdController.setStructValue(MdControllerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Controller1");
    mdController.setStructValue(MdControllerInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Controller1");
    mdController.setValue(MdControllerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdController.apply();

    final MdActionDAO mdAction = MdActionDAO.newInstance();
    mdAction.setValue(MdActionInfo.NAME, "checkin");
    mdAction.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkin");
    mdAction.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkin");

    // Define new MdActions and MdParameters to be added to the existing
    // MdController
    final MdParameterDAO mdParameter3 = MdParameterDAO.newInstance();
    mdParameter3.setValue(MdParameterInfo.NAME, "param3");
    mdParameter3.setValue(MdParameterInfo.TYPE, "java.lang.String");
    mdParameter3.setValue(MdParameterInfo.ORDER, "4");
    mdParameter3.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "param3");

    final MdActionDAO mdAction2 = MdActionDAO.newInstance();
    mdAction2.setValue(MdActionInfo.NAME, "checkout");
    mdAction2.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkout");
    mdAction2.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkout");

    final MdParameterDAO mdParameter4 = MdParameterDAO.newInstance();
    mdParameter4.setValue(MdParameterInfo.NAME, "param4");
    mdParameter4.setValue(MdParameterInfo.TYPE, "java.lang.String");
    mdParameter4.setValue(MdParameterInfo.ORDER, "0");
    mdParameter4.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "param4");

    // generateCreateSchema(new ComponentIF[] { mdController });
    ExportMetadata firstUpdate = new ExportMetadata();
    firstUpdate.addUpdate(mdController);
    firstUpdate.addNewMdAction(mdController, mdAction, mdParameter4);
    VersionExporter.export(CREATE_SCHEMA, SCHEMA, firstUpdate);

    // Export mdController with all of its MdActions defined in the database,
    // and
    // export a new MdAction for the mdController, as well as a new MdParameter
    // for an existing MdAction of mdController
    final ExportMetadata secondUpdate = new ExportMetadata();
    generateMerge(secondUpdate, new ComponentIF[] { mdController }, new ComponentIF[] {}, new UpdateActions()
    {
      public void perform()
      {
        secondUpdate.addNewMdAction(mdController, mdAction2, mdParameter3);
      }
    });

    Assert.assertTrue(MdControllerDAO.isDefined("test.xmlclasses.Controller1"));

    MdControllerDAOIF mdControllerIF = MdControllerDAO.getMdControllerDAO("test.xmlclasses.Controller1");
    Assert.assertEquals("Controller1", mdControllerIF.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals("Controller1", mdControllerIF.getDisplayLabel(CommonProperties.getDefaultLocale()));

    List<MdActionDAOIF> mdActions = mdControllerIF.getMdActionDAOs();

    Assert.assertEquals(2, mdActions.size());

    int i = 0;
    int j = 1;

    if (!"checkin".equals(mdActions.get(0).getName()))
    {
      i = 1;
      j = 0;
    }

    Assert.assertEquals("checkin", mdActions.get(i).getName());
    Assert.assertEquals("checkin", mdActions.get(i).getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals("checkin", mdActions.get(i).getDescription(CommonProperties.getDefaultLocale()));

    List<MdParameterDAOIF> mdParameters = mdActions.get(i).getMdParameterDAOs();
    Assert.assertEquals(1, mdParameters.size());
    Assert.assertEquals("param4", mdParameters.get(0).getParameterName());
    Assert.assertEquals("java.lang.String", mdParameters.get(0).getParameterType().getType());
    Assert.assertEquals("0", mdParameters.get(0).getParameterOrder());
    Assert.assertEquals("param4", mdParameters.get(0).getDisplayLabel(CommonProperties.getDefaultLocale()));

    Assert.assertEquals("checkout", mdActions.get(j).getName());
    Assert.assertEquals("checkout", mdActions.get(j).getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals("checkout", mdActions.get(j).getDescription(CommonProperties.getDefaultLocale()));

    mdParameters = mdActions.get(j).getMdParameterDAOs();
    Assert.assertEquals(1, mdParameters.size());
    Assert.assertEquals("param3", mdParameters.get(0).getParameterName());
    Assert.assertEquals("java.lang.String", mdParameters.get(0).getParameterType().getType());
    Assert.assertEquals("4", mdParameters.get(0).getParameterOrder());
    Assert.assertEquals("param3", mdParameters.get(0).getDisplayLabel(CommonProperties.getDefaultLocale()));

  }

  /*
   * @Request @Test public void testUpdateObjectMerge() { final MdBusinessDAO
   * mdBusiness1 = TestFixtureFactory.createMdBusiness1(); final MdBusinessDAO
   * mdBusiness2 = TestFixtureFactory.createMdBusiness2();
   * 
   * mdBusiness1.apply(); mdBusiness2.apply();
   * 
   * TestFixtureFactory.addCharacterAttribute(mdBusiness1).apply();
   * TestFixtureFactory.addBooleanAttribute(mdBusiness1).apply();
   * TestFixtureFactory.addReferenceAttribute(mdBusiness2, mdBusiness1).apply();
   * 
   * 
   * final BusinessDAO businessDAO1 =
   * BusinessDAO.newInstance(mdBusiness1.definesType());
   * businessDAO1.setValue(TestFixConst.ATTRIBUTE_BOOLEAN,
   * MdAttributeBooleanInfo.TRUE);
   * businessDAO1.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "3");
   * businessDAO1.apply();
   * 
   * final BusinessDAO businessDAO2 =
   * BusinessDAO.newInstance(mdBusiness1.definesType());
   * businessDAO2.setValue(TestFixConst.ATTRIBUTE_BOOLEAN,
   * MdAttributeBooleanInfo.FALSE);
   * businessDAO2.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Who are you?");
   * businessDAO2.apply();
   * 
   * ExportMetadata metadata = new ExportMetadata();
   * metadata.addCreate(businessDAO1, businessDAO2);
   * VersionExporter.export(CREATE_SCHEMA, SCHEMA, metadata);
   * 
   * final BusinessDAO businessDAO3 =
   * BusinessDAO.newInstance(mdBusiness2.definesType());
   * businessDAO3.setValue("testReference", businessDAO1.getId());
   * businessDAO3.apply();
   * 
   * final BusinessDAO businessDAO4 =
   * BusinessDAO.newInstance(mdBusiness1.definesType());
   * businessDAO4.setValue(TestFixConst.ATTRIBUTE_BOOLEAN,
   * MdAttributeBooleanInfo.TRUE);
   * businessDAO4.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Delete");
   * String id = businessDAO4.apply();
   * 
   * final ExportMetadata updateMetadata = new ExportMetadata();
   * generateMerge(updateMetadata, new ComponentIF[] {}, new ComponentIF[]
   * {mdBusiness2}, new UpdateActions () {
   * 
   * @Override public void perform() { updateMetadata.addCreate(businessDAO1);
   * updateMetadata.addCreate(businessDAO2);
   * updateMetadata.addCreate(businessDAO3);
   * updateMetadata.addCreate(businessDAO4);
   * updateMetadata.addDelete(businessDAO4);
   * updateMetadata.addUpdate(businessDAO2);
   * updateMetadata.addUpdate(businessDAO3);
   * updateMetadata.addDelete(mdBusiness2);
   * 
   * } });
   * 
   * businessDAO2.setValue(TestFixConst.ATTRIBUTE_BOOLEAN,
   * MdAttributeBooleanInfo.TRUE); businessDAO2.apply();
   * 
   * businessDAO3.setValue("testReference", businessDAO2.getId());
   * businessDAO3.apply();
   * 
   * 
   * // Get the ids of the CLASS1 List<String> classIds =
   * EntityDAO.getEntityIdsDB(SAXParseTest.CLASS);
   * 
   * // Get the first instance of CLASS2 List<String> class2Ids =
   * EntityDAO.getEntityIdsDB(SAXParseTest.CLASS2);
   * 
   * BusinessDAOIF class2 = BusinessDAO.get(class2Ids.get(0));
   * 
   * // Assert that the values of refTest refer to instances of CLASS
   * Assert.assertTrue(classIds.contains(class2.getValue("testReference")));
   * 
   * try { BusinessDAO.get(id);
   * 
   * Assert.fail("SAXImporter did not delete the businessDAO with the id [" + id
   * + "]"); } catch (DataNotFoundException e) { // This is expected } }
   */

  @Request
  @Test
  public void testDeleteAttributeFromMdBusinessMerge()
  {
    MdBusinessDAO mdBusiness = createMdBusiness();
    mdBusiness.setGenerateMdController(false);
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    final MdAttributeConcreteDAO mdAttribute = createCharacterAttribute(mdBusiness);
    mdAttribute.apply();

    generateCreateSchema(new ComponentIF[] { mdBusiness });

    final ExportMetadata updateMetadata = new ExportMetadata();

    generateMerge(updateMetadata, new ComponentIF[] {}, new ComponentIF[] { mdBusiness }, new UpdateActions()
    {
      @Override
      public void perform()
      {
        updateMetadata.addDelete(mdAttribute);
      }
    });

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdAttributeDAOIF attribute = mdEntityIF.definesAttribute(ATTRIBUTE_NAME);
    if (attribute != null)
    {
      Assert.fail("The attribute was not deleted");
    }
    else
      Assert.assertTrue(true);
  }

  @Request
  @Test
  public void testDeleteMdMethodFromMdBusinessMerge()
  {
    MdBusinessDAO mdBusiness = createMdBusiness();
    mdBusiness.setGenerateMdController(false);
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    final MdMethodDAO mdMethod = createMethod(mdBusiness);
    mdMethod.apply();
    generateCreateSchema(new ComponentIF[] { mdBusiness });
    final ExportMetadata updateMetadata = new ExportMetadata();
    generateMerge(updateMetadata, new ComponentIF[] {}, new ComponentIF[] { mdBusiness }, new UpdateActions()
    {
      @Override
      public void perform()
      {
        updateMetadata.addDelete(mdMethod);
      }
    });

    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    try
    {
      mdEntityIF.getMdMethod(METHOD_NAME);
      Assert.fail("mdMethod could not be deleted");
    }
    catch (DataNotFoundException dataNotFoundException)
    {

    }

  }

  @Request
  @Test
  public void testDeleteMdMethodFromPartialSchema()
  {
    // Create test MdBusiness
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    // Export partial schema element with definition of new attribute
    MdMethodDAO mdMethod1 = TestFixtureFactory.createMdMethod(mdBusiness1, "constantMethod");
    MdParameterDAO mdParameter1 = TestFixtureFactory.createMdParameter(null, "parameter0", 0);

    ExportMetadata metadata = new ExportMetadata();
    metadata.addUpdate(mdBusiness1);
    metadata.addNewMdMethod(mdBusiness1, mdMethod1, mdParameter1);

    generateCreateSchema(metadata);

    // Export partial schema element with definition of new attribute
    MdMethodDAO mdMethod = TestFixtureFactory.createMdMethod(mdBusiness1);
    MdParameterDAO mdParameter = TestFixtureFactory.createMdParameter(null, "parameter0", 0);

    ExportMetadata updateMetadata = new ExportMetadata();
    updateMetadata.addUpdate(mdBusiness1);
    updateMetadata.addNewMdMethod(mdBusiness1, mdMethod, mdParameter);

    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, updateMetadata);

    mdMethod.apply();

    VersionExporter.export(UPDATE_SCHEMA_2, SCHEMA, ExportMetadata.buildDelete(mdMethod));

    TestFixtureFactory.delete(mdMethod);

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1, UPDATE_SCHEMA_2);

    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    MdBusinessDAOIF mdBusiness1IF = MdBusinessDAO.getMdBusinessDAO(mdBusiness1.definesType());

    try
    {
      mdBusiness1IF.getMdMethod(mdMethod.getName());
      Assert.fail("Able to retrieve ");
    }
    catch (Exception e)
    {
      // This is expected
    }
  }

  @Request
  @Test
  public void testDeleteParameterFromMdMethodMerge()
  {
    MdBusinessDAO mdBusiness = createMdBusiness();
    mdBusiness.setGenerateMdController(false);
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    final MdMethodDAO mdMethod = createMethod(mdBusiness);
    mdMethod.apply();

    final MdParameterDAO mdParameter = createMdParameter(mdMethod);
    mdParameter.apply();

    generateCreateSchema(new ComponentIF[] { mdBusiness });
    final ExportMetadata updateMetadata = new ExportMetadata();
    generateMerge(updateMetadata, new ComponentIF[] {}, new ComponentIF[] { mdBusiness }, new UpdateActions()
    {
      @Override
      public void perform()
      {
        updateMetadata.addDelete(mdParameter);
      }
    });
    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(CLASS);
    MdMethodDAOIF mdMethodIF = mdEntityIF.getMdMethod(METHOD_NAME);
    Assert.assertEquals(mdMethodIF.getMdParameterDAOs().size(), 0);

  }

  @Request
  @Test
  public void testAddMdActionParameterToPartialMdControllerMergePreserveCreate()
  {
    final MdControllerDAO mdController = MdControllerDAO.newInstance();
    mdController.setValue(MdControllerInfo.NAME, "Controller1");
    mdController.setValue(MdControllerInfo.PACKAGE, "test.xmlclasses");
    mdController.setStructValue(MdControllerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Controller1");
    mdController.setStructValue(MdControllerInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Controller1");
    mdController.setValue(MdControllerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdController.apply();

    final MdActionDAO mdAction2 = MdActionDAO.newInstance();
    mdAction2.setValue(MdActionInfo.NAME, "checkout");
    mdAction2.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkout");
    mdAction2.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkout");
    mdAction2.setValue(MdActionInfo.IS_QUERY, "true");

    // generateCreateSchema(new ComponentIF[] { mdController });
    ExportMetadata firstUpdate = new ExportMetadata();
    firstUpdate.addUpdate(mdController);
    firstUpdate.addNewMdAction(mdController, mdAction2);
    VersionExporter.export(CREATE_SCHEMA, SCHEMA, firstUpdate);

    // Export mdController with all of its MdActions defined in the database,
    // and
    // export a new MdAction for the mdController, as well as a new MdParameter
    // for an existing MdAction of mdController
    final ExportMetadata secondUpdate = new ExportMetadata();

    generateMerge(secondUpdate, new ComponentIF[] { mdController }, new ComponentIF[] {}, new UpdateActions()
    {
      public void perform()
      {
        mdAction2.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
        mdAction2.setValue(MdActionInfo.IS_QUERY, "false");
        mdAction2.apply();
      }

      @Override
      public void postPerform()
      {
        TestFixtureFactory.delete(mdAction2);
      }
    });

  }

  @Request
  @Test
  public void testPartialMergePreserveCreateWithChildren()
  {
    final MdControllerDAO mdController = MdControllerDAO.newInstance();
    mdController.setValue(MdControllerInfo.NAME, "Controller1");
    mdController.setValue(MdControllerInfo.PACKAGE, "test.xmlclasses");
    mdController.setStructValue(MdControllerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Controller1");
    mdController.setStructValue(MdControllerInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Controller1");
    mdController.setValue(MdControllerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdController.apply();

    final MdActionDAO mdAction2 = MdActionDAO.newInstance();
    mdAction2.setValue(MdActionInfo.NAME, "checkout");
    mdAction2.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkout");
    mdAction2.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkout");
    mdAction2.setValue(MdActionInfo.IS_QUERY, "true");

    // generateCreateSchema(new ComponentIF[] { mdController });
    ExportMetadata firstUpdate = new ExportMetadata();
    firstUpdate.addUpdate(mdController);
    firstUpdate.addNewMdAction(mdController, mdAction2);
    VersionExporter.export(CREATE_SCHEMA, SCHEMA, firstUpdate);

    // Export mdController with all of its MdActions defined in the database,
    // and export a new MdAction for the mdController, as well as a new
    // MdParameter for an existing MdAction of mdController
    MdParameterDAO mdParameter = MdParameterDAO.newInstance();
    mdParameter.setValue(MdParameterInfo.NAME, "param1");
    mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.String");
    mdParameter.setValue(MdParameterInfo.ORDER, "0");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "param1");
    mdParameter.setStructValue(MdParameterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "param1");

    final ExportMetadata secondUpdate = new ExportMetadata();
    secondUpdate.addNewMdParameter(mdAction2, mdParameter);

    generateMerge(secondUpdate, new ComponentIF[] { mdController }, new ComponentIF[] {}, new UpdateActions()
    {
      public void perform()
      {
        mdAction2.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
        mdAction2.setValue(MdActionInfo.IS_QUERY, "false");
        mdAction2.apply();

      }

      @Override
      public void postPerform()
      {
        TestFixtureFactory.delete(mdAction2);
      }
    });

  }

  @Request
  @Test
  public void testPartialMergeDeleteChildren()
  {
    final MdControllerDAO mdController = MdControllerDAO.newInstance();
    mdController.setValue(MdControllerInfo.NAME, "Controller1");
    mdController.setValue(MdControllerInfo.PACKAGE, "test.xmlclasses");
    mdController.setStructValue(MdControllerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Controller1");
    mdController.setStructValue(MdControllerInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Controller1");
    mdController.setValue(MdControllerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdController.apply();

    final MdActionDAO mdAction2 = MdActionDAO.newInstance();
    mdAction2.setValue(MdActionInfo.NAME, "checkout");
    mdAction2.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkout");
    mdAction2.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "checkout");
    mdAction2.setValue(MdActionInfo.IS_QUERY, "true");

    // generateCreateSchema(new ComponentIF[] { mdController });
    ExportMetadata firstUpdate = new ExportMetadata();
    firstUpdate.addUpdate(mdController);
    firstUpdate.addNewMdAction(mdController, mdAction2);
    VersionExporter.export(CREATE_SCHEMA, SCHEMA, firstUpdate);

    final ExportMetadata secondUpdate = new ExportMetadata();
    secondUpdate.addDelete(mdAction2);

    generateMerge(secondUpdate, new ComponentIF[] {}, new ComponentIF[] {}, new UpdateActions()
    {
      public void perform()
      {
        mdAction2.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
        mdAction2.apply();
      }

      @Override
      public void postPerform()
      {
        TestFixtureFactory.delete(mdAction2);
      }
    });
  }

  @Request
  @Test
  public void testAttributeDimension()
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
        mdBusiness.setGenerateMdController(false);
        mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
        mdBusiness.apply();

        MdAttributeCharacterDAO mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdBusiness);
        mdAttributeCharacter.apply();

        MdAttributeLongDAO mdAttributeLong = TestFixtureFactory.addLongAttribute(mdBusiness);
        mdAttributeLong.apply();

        for (MdAttributeDAOIF mdAttribute : mdBusiness.definesAttributes())
        {

          if (!mdAttribute.isSystem())
          {
            role.grantPermission(Operation.WRITE, mdAttribute.getId());
          }
        }

        ExportMetadata metadata = ExportMetadata.buildCreate(role);
        metadata.addGrantPermissions(role);

        generateCreateSchema(metadata);

        for (MdAttributeDAOIF mdAttribute : mdBusiness.definesAttributes())
        {

          if (!mdAttribute.isSystem())
          {
            role.revokeAllPermissions(mdAttribute.getId());

            MdAttributeDimensionDAOIF mdAttributeDimension = mdAttribute.getMdAttributeDimension(mdDimension);

            role.grantPermission(Operation.READ, mdAttributeDimension.getId());
          }
        }

        // Delete the referenced type.
        ExportMetadata updateMetadata = new ExportMetadata(false);
        updateMetadata.addGrantPermissions(role);

        VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, updateMetadata);

        mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

        TestFixtureFactory.delete(role);

        VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

        RoleDAOIF test = RoleDAO.findRole(role.getRoleName());

        for (MdAttributeDAOIF mdAttribute : mdBusiness.definesAttributes())
        {
          if (!mdAttribute.isSystem())
          {
            Set<Operation> permissions = test.getAssignedPermissions(mdAttribute);
            Assert.assertTrue(permissions.contains(Operation.WRITE));
            Assert.assertFalse(permissions.contains(Operation.READ));

            MdAttributeDimensionDAOIF mdAttributeDimension = mdAttribute.getMdAttributeDimension(mdDimension);
            Set<Operation> dimensionPermissions = test.getAssignedPermissions(mdAttributeDimension);

            Assert.assertTrue(dimensionPermissions.contains(Operation.READ));
            Assert.assertFalse(dimensionPermissions.contains(Operation.WRITE));
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
      try
      {
        RoleDAOIF _role = RoleDAO.findRole(role.getRoleName());

        TestFixtureFactory.delete(_role);
      }
      catch (Exception e)
      {
      }
    }
  }

  /**
   * Initial: a mdBusiness and a blob attribute Update : creates a blob
   * attribute in the mdBusiness
   */
  @Request
  @Test
  public void testAttributeOnRelationship()
  {
    MdBusinessDAO parentMdBusiness = TestFixtureFactory.createMdBusiness1();
    parentMdBusiness.setGenerateMdController(false);
    parentMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    parentMdBusiness.apply();

    MdBusinessDAO childMdBusiness = TestFixtureFactory.createMdBusiness2();
    childMdBusiness.setGenerateMdController(false);
    childMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    childMdBusiness.apply();

    MdRelationshipDAO mdRelationship = TestFixtureFactory.createMdRelationship1(parentMdBusiness, childMdBusiness);
    mdRelationship.setGenerateMdController(false);
    mdRelationship.setValue(MdRelationshipInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdRelationship.apply();

    generateCreateSchema(ExportMetadata.buildCreate(mdRelationship));

    MdAttributeCharacterDAO mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdRelationship);

    ExportMetadata updateMetadata = ExportMetadata.buildUpdate(mdRelationship);
    updateMetadata.addNewMdAttribute(mdRelationship, mdAttributeCharacter);

    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, updateMetadata);

    TestFixtureFactory.delete(mdRelationship);

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(mdRelationship.definesType());

    Assert.assertNotNull(mdRelationshipIF);
    Assert.assertNotNull(mdRelationshipIF.definesAttribute(mdAttributeCharacter.definesAttribute()));
  }

  @Request
  @Test
  public void testCreateUpdateThenDeleteOnObject()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setGenerateMdController(false);
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeCharacterDAO mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdBusiness);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue(mdAttributeCharacter.definesAttribute(), "create-value");
    businessDAO.setValue(BusinessInfo.KEY, "test-key");
    businessDAO.apply();

    generateCreateSchema(ExportMetadata.buildCreate(businessDAO));

    businessDAO.setValue(mdAttributeCharacter.definesAttribute(), "update-value");
    businessDAO.apply();

    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, ExportMetadata.buildUpdate(businessDAO));

    VersionExporter.export(UPDATE_SCHEMA_2, SCHEMA, ExportMetadata.buildDelete(businessDAO));

    TestFixtureFactory.delete(businessDAO);

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1, UPDATE_SCHEMA_2);

    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    List<String> ids = BusinessDAO.getEntityIdsDB(mdBusiness.definesType());

    // Ensure that the object was deleted
    Assert.assertEquals(0, ids.size());
  }

  @Request
  @Test
  public void testFormMerge()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdAttributeBooleanDAO mdAttribute = TestFixtureFactory.addBooleanAttribute(mdBusiness1);
    mdAttribute.apply();

    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness1);
    mdForm.apply();

    MdWebBooleanDAO mdWebBoolean = TestFixtureFactory.addBooleanField(mdForm, mdAttribute);
    mdWebBoolean.apply();

    // Generate the initial model
    generateCreateSchema(new ComponentIF[] { mdBusiness1 });

    ExportMetadata metadata = ExportMetadata.buildCreate(mdForm);

    // Export the model with the rnamed attributed
    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, metadata);

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

    TestFixtureFactory.delete(mdForm);
    TestFixtureFactory.delete(mdBusiness1);

    // Import merge file
    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    MdTypeDAOIF testForm = MdWebFormDAO.getMdTypeDAO(mdForm.definesType());

    Assert.assertNotNull(testForm);
  }

  @Request
  @Test
  public void testFieldReferenceOnAttributeRename()
  {
    final String newAttributeName = "renamedAttribute";

    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdAttributeBooleanDAO mdAttribute = TestFixtureFactory.addBooleanAttribute(mdBusiness1);
    mdAttribute.apply();

    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness1);
    mdForm.apply();

    MdWebBooleanDAO mdWebBoolean = TestFixtureFactory.addBooleanField(mdForm, mdAttribute);
    mdWebBoolean.apply();

    // Generate the initial model
    generateCreateSchema(new ComponentIF[] { mdBusiness1, mdForm });

    ExportMetadata metadata = ExportMetadata.buildUpdate(mdBusiness1);
    metadata.renameAttribute(mdAttribute, newAttributeName);

    // Export the model with the rnamed attributed
    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, metadata);

    mdAttribute.setValue(MdAttributeBooleanInfo.NAME, newAttributeName);
    mdAttribute.apply();

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

    TestFixtureFactory.delete(mdForm);
    TestFixtureFactory.delete(mdBusiness1);

    // Import merge file
    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    MdTypeDAOIF testForm = MdWebFormDAO.getMdTypeDAO(mdForm.definesType());

    Assert.assertNotNull(testForm);
  }

  @Request
  @Test
  public void testDeleteOfRenamedAttributeWithFieldReference()
  {
    final String newAttributeName = "renamedAttribute";

    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdAttributeBooleanDAO mdAttribute = TestFixtureFactory.addBooleanAttribute(mdBusiness1);
    mdAttribute.apply();

    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness1);
    mdForm.apply();

    MdWebBooleanDAO mdWebBoolean = TestFixtureFactory.addBooleanField(mdForm, mdAttribute);
    mdWebBoolean.apply();

    // Generate the initial model
    generateCreateSchema(new ComponentIF[] { mdBusiness1, mdForm });

    ExportMetadata metadata = ExportMetadata.buildUpdate(mdBusiness1);
    metadata.renameAttribute(mdAttribute, newAttributeName);

    // Export the model with the rnamed attributed
    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, metadata);

    mdAttribute.setValue(MdAttributeBooleanInfo.NAME, newAttributeName);
    mdAttribute.apply();

    // Export the model to delete renamed attribute
    VersionExporter.export(UPDATE_SCHEMA_2, SCHEMA, ExportMetadata.buildDelete(mdAttribute));

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1, UPDATE_SCHEMA_2);

    TestFixtureFactory.delete(mdForm);
    TestFixtureFactory.delete(mdBusiness1);

    // Import merge file
    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

    MdTypeDAOIF testForm = MdWebFormDAO.getMdTypeDAO(mdForm.definesType());

    Assert.assertNotNull(testForm);
  }

  private void mergeSchema(String... fileStrings)
  {
    List<File> files = new ArrayList<File>();
    for (String fileString : fileStrings)
    {
      files.add(new File(fileString));
    }
    ListIterator<File> schemaIterator = files.listIterator();
    // Create merge file
    SchemaManager.merge(schemaIterator, SCHEMA, MERGED_SCHEMA);
  }

  private MdBusinessDAO createMdBusiness()
  {
    MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.NAME, "Class1");
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, "test.xmlclasses");
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "mdBusiness Set Test");
    mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Set mdBusiness Attributes Test");
    mdBusiness.setGenerateMdController(false);
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    return mdBusiness;
  }

  private MdAttributeCharacterDAO createCharacterAttribute(MdEntityDAO mdEntity)
  {
    MdAttributeCharacterDAO mdAttribute = MdAttributeCharacterDAO.newInstance();
    mdAttribute.setValue(MdAttributeCharacterInfo.NAME, ATTRIBUTE_NAME);
    mdAttribute.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Character Set Test");
    mdAttribute.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.NO_INDEX.getId());
    mdAttribute.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.TRUE);
    mdAttribute.setValue(MdAttributeCharacterInfo.SIZE, "200");
    mdAttribute.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  private MdMethodDAO createMethod(MdEntityDAO mdEntity)
  {
    MdMethodDAO mdMethod = MdMethodDAO.newInstance();
    mdMethod.setValue(MdMethodInfo.NAME, METHOD_NAME);
    mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdEntity.getId());
    mdMethod.setValue(MdMethodInfo.RETURN_TYPE, RETURN_TYPE);
    mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, METHOD_NAME);
    mdMethod.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, METHOD_NAME);
    mdMethod.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    return mdMethod;

  }

  private MdParameterDAO createMdParameter(MdMethodDAO mdMethod)
  {
    MdParameterDAO mdParameter = MdParameterDAO.newInstance();
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod.getId());
    mdParameter.setValue(MdParameterInfo.TYPE, PARAMETER_TYPE);
    mdParameter.setValue(MdParameterInfo.NAME, "validName");
    mdParameter.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "validName");
    mdParameter.setValue(MdParameterInfo.ORDER, "100");
    return mdParameter;
  }

  private MdStateMachineDAO createMdStateMachine(MdBusinessDAO mdBusiness)
  {
    MdStateMachineDAO mdState = MdStateMachineDAO.newInstance();
    mdState.setValue(MdStateMachineInfo.PACKAGE, STATE_MACHINE_PACKAGE);
    mdState.setValue(MdStateMachineInfo.NAME, STATE_MACHINE_NAME);
    mdState.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, STATE_MACHINE_NAME);
    mdState.setValue(MdStateMachineInfo.SUPER_MD_BUSINESS, EntityTypes.STATE_MASTER.getId());
    mdState.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness.getId());
    mdState.setGenerateMdController(false);
    mdState.setValue(MdStructInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    return mdState;
  }

  private void generateCreateSchema(ComponentIF[] componentArray)
  {
    VersionExporter.export(CREATE_SCHEMA, SCHEMA, ExportMetadata.buildCreate(componentArray));
  }

  private void generateCreateSchema(ExportMetadata metadata)
  {
    VersionExporter.export(CREATE_SCHEMA, SCHEMA, metadata);
  }

  private void generateMerge(ExportMetadata metadata, ComponentIF[] componentArray, UpdateActions updates)
  {
    metadata.addUpdate(componentArray);
    updates.perform();
    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, metadata);
    for (ComponentIF component : componentArray)
    {
      TestFixtureFactory.delete(component);
    }
    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);
    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);
  }

  private void generateMerge(ExportMetadata metadata, ComponentIF[] updateComponentArray, ComponentIF[] deleteComponentArray, UpdateActions updates)
  {
    metadata.addUpdate(updateComponentArray);
    updates.perform();
    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, metadata);

    for (ComponentIF component : deleteComponentArray)
    {
      TestFixtureFactory.delete(component);
    }

    mergeSchema(CREATE_SCHEMA, UPDATE_SCHEMA_1);

    updates.postPerform();

    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);
  }

  private void generateMerge(ExportMetadata metadata, ComponentIF[] updateComponentArray, ComponentIF[] deleteComponentArray, UpdateActions updates, String... fileStrings)
  {
    metadata.addUpdate(updateComponentArray);
    updates.perform();
    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, metadata);

    for (ComponentIF component : deleteComponentArray)
    {
      TestFixtureFactory.delete(component);
    }

    mergeSchema(fileStrings);
    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);

  }

  @SuppressWarnings("unused")
  private void generateMerge(ExportMetadata metadata, ComponentIF[] componentArray, UpdateActions updates, String... fileStrings)
  {
    metadata.addUpdate(componentArray);
    updates.perform();
    VersionExporter.export(UPDATE_SCHEMA_1, SCHEMA, metadata);
    for (ComponentIF component : componentArray)
    {
      TestFixtureFactory.delete(component);
    }
    mergeSchema(fileStrings);
    VersionHandler.runImport(new File(MERGED_SCHEMA), Action.DO_IT, XMLConstants.VERSION_XSD);
  }

  private abstract static class UpdateActions
  {
    public abstract void perform();

    public void postPerform()
    {
    }
  }

}
