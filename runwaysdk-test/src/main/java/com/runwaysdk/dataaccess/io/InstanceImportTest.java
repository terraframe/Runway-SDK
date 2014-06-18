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
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.SystemException;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.ImportLogInfo;
import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeClobInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.constants.TransactionItemInfo;
import com.runwaysdk.constants.TransactionRecordInfo;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.constants.XMLConstants;
import com.runwaysdk.dataaccess.AttributeEncryptionIF;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.AttributeStructIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdAttributeBlobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeClobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDecimalDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.MdTreeDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.resolver.DefaultConflictResolver;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.dataaccess.transaction.TransactionEventChangeListener;
import com.runwaysdk.dataaccess.transaction.TransactionExportManager;
import com.runwaysdk.dataaccess.transaction.TransactionImportManager;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.util.FileIO;

public class InstanceImportTest extends TestCase
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

  public static final String EXPROT_TEST_DIR   = TestConstants.Path.transactionExportFiles + "/";

  public static final String EXPORT_FILE_NAME  = "testExport";

  /**
   * List of all XML files to test on
   */
  public static final String INSTANCE_SET      = SAXParseTest.path + "instanceTest.xml";

  public static final String SEQUENCE_TEST1    = SAXParseTest.path + "testSequence.xml";

  public static final String SEQUENCE_TEST2    = SAXParseTest.path + "testSequence2.xml";

  public static final String SEQUENCE_TEST3    = SAXParseTest.path + "testSequence3.xml";

  public static final String UNKNOWN_REFERENCE = SAXParseTest.path + "unknownReference.xml";

  public static final String ENUM_CLASS        = "test.xmlclasses.EnumClass1";

  public static final String CLASS             = "test.xmlclasses.Class1";

  public static final String CLASS2            = "test.xmlclasses.Class2";

  public static final String STRUCT            = "test.xmlclasses.ClassBasic1";

  public static final String RELATIONSHIP      = "test.xmlclasses.Relationship1";

  public static final URL    SCHEMA_LOCATION   = InstanceImportTest.class.getResource(XMLConstants.INSTANCE_XSD);

  public static final String exportSiteDomain  = "export.terraframe.com";

  public static final String importSiteDomain  = "import.terraframe.com";

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(InstanceImportTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
        classSetUp();
      }

      protected void tearDown()
      {
        classTearDown();
      }
    };

    return wrapper;
  }

  /**
   * The setup done before the test suite is run
   */
  public static void classSetUp()
  {

  }

  /**
   * The tear down done after all the test in the test suite have run
   */
  public static void classTearDown()
  {
    File file = new File(INSTANCE_SET);
    try
    {
      FileIO.deleteFile(file);
    }
    catch (IOException e)
    {
      throw new SystemException(e.getMessage());
    }
  }

  protected void tearDown() throws Exception
  {
    if (MdTypeDAO.isDefined(ENUM_CLASS))
    {
      MdBusinessDAO mdEnumClass = (MdBusinessDAO) MdBusinessDAO.getMdBusinessDAO(ENUM_CLASS).getBusinessDAO();
      mdEnumClass.delete();
    }

    // Delete Secondary Class
    if (MdTypeDAO.isDefined(CLASS2))
    {
      MdBusinessDAO mdBusiness2 = (MdBusinessDAO) MdBusinessDAO.getMdBusinessDAO(CLASS2).getBusinessDAO();
      mdBusiness2.delete();
    }

    if (MdTypeDAO.isDefined(CLASS))
    {
      MdBusinessDAO mdBusiness = (MdBusinessDAO) MdBusinessDAO.getMdBusinessDAO(CLASS).getBusinessDAO();
      mdBusiness.delete();
    }

    // Delete Primary Relationship
    if (MdTypeDAO.isDefined(RELATIONSHIP))
    {
      MdRelationshipDAO mdRelationship = ( MdRelationshipDAO.getMdRelationshipDAO(RELATIONSHIP) ).getBusinessDAO();
      mdRelationship.delete();
    }

    if (MdTypeDAO.isDefined(STRUCT))
    {
      MdStructDAO mdStruct = (MdStructDAO) MdStructDAO.getMdStructDAO(STRUCT).getBusinessDAO();
      mdStruct.delete();
    }

    try
    {
      UserDAO newUser = UserDAO.findUser("s-Meth").getBusinessDAO();
      newUser.delete();
    }
    catch (Exception e)
    {
      // Do nothing, newUser does not exist.
    }

    resetTransactionDatatypes();
    ServerProperties.setLogTransactions(false);
  }

  // /**
  // * Test importing and exporting different attributes and MdTypes where the
  // export site
  // * is different than the import site.
  // */
  // public void testInstanceImportDifferentSites() throws IOException
  // {
  // _testInstance(true, true, exportSiteDomain, importSiteDomain);
  // }
  //
  // /**
  // * Test importing and exporting different attributes and MdTypes where the
  // export site
  // * is the same as the import site, but the import site is cleanup before
  // import.
  // */
  // public void testInstanceImportSameSite() throws IOException
  // {
  // _testInstance(true, true, exportSiteDomain, exportSiteDomain);
  // }

  /**
   * Test importing and exporting different attributes and MdTypes where the
   * export site is the same as the import site, but the import site is not
   * cleanup before import.
   * 
   * The imported records should just be ignored, as the sequence numbers on the
   * importing node will remain the same.
   */
  public void testInstanceImportSameSiteNoDataCleanup() throws IOException
  {
    _testInstance(false, false, exportSiteDomain, exportSiteDomain);
  }

  /**
   * Test importing and exporting different attributes and MdTypes
   * 
   * @param cleanupData
   *          true if newly created data should be deleted after an export,
   *          false otherwise.
   * @param deleteLogs
   *          true if logs should be delted between import and export
   * @param testExportSiteDomain
   * @param testImportSiteDomain
   */
  private void _testInstance(boolean cleanupData, boolean deleteLogs, String testExportSiteDomain, String testImportSiteDomain) throws IOException
  {
    CommonProperties.setDomain(testExportSiteDomain);
    ServerProperties.setLogTransactions(true);

    UserDAO newUser = UserDAO.newInstance();
    newUser.setValue(UserInfo.USERNAME, "s-Meth");
    newUser.setValue(UserInfo.PASSWORD, "mypass12");
    newUser.setValue(UserInfo.SESSION_LIMIT, "10");
    newUser.apply();

    MdBusinessDAO mdBusinessEnum = MdBusinessDAO.newInstance();
    MdEnumerationDAO mdEnumeration = MdEnumerationDAO.newInstance();
    MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
    MdAttributeBlobDAO mdAttributeBlob = MdAttributeBlobDAO.newInstance();
    MdAttributeBooleanDAO mdAttributeBoolean = MdAttributeBooleanDAO.newInstance();
    MdAttributeDecimalDAO mdAttributeDecimal = MdAttributeDecimalDAO.newInstance();
    MdAttributeDoubleDAO mdAttributeDouble = MdAttributeDoubleDAO.newInstance();
    MdAttributeFloatDAO mdAttributeFloat = MdAttributeFloatDAO.newInstance();
    MdAttributeIntegerDAO mdAttributeInteger = MdAttributeIntegerDAO.newInstance();
    MdAttributeTextDAO mdAttributeText = MdAttributeTextDAO.newInstance();
    MdAttributeClobDAO mdAttributeClob = MdAttributeClobDAO.newInstance();
    MdAttributeEnumerationDAO mdAttributeEnumeration = MdAttributeEnumerationDAO.newInstance();
    MdBusinessDAO mdBusiness2 = MdBusinessDAO.newInstance();
    MdRelationshipDAO mdRelationship = MdTreeDAO.newInstance();
    MdAttributeConcreteDAO mdAttribute = MdAttributeCharacterDAO.newInstance();

    this.ddlCreateTestInstance(mdBusinessEnum, mdEnumeration, mdBusiness, mdAttributeBlob, mdAttributeBoolean, mdAttributeDecimal, mdAttributeDouble, mdAttributeFloat, mdAttributeInteger, mdAttributeText, mdAttributeClob, mdAttributeEnumeration, mdBusiness2, mdRelationship, mdAttribute);

    BusinessDAO enumObject = BusinessDAO.newInstance(mdBusinessEnum.definesType());
    enumObject.setValue(EnumerationMasterInfo.NAME, "CO");
    enumObject.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Colorado");
    enumObject.apply();

    BusinessDAO businessDAO1 = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO1.setValue("testBlob", "Hello");
    businessDAO1.setValue("testBoolean", MdAttributeBooleanInfo.TRUE);
    businessDAO1.setValue("testDecimal", "3.10");
    businessDAO1.setValue("testDouble", "4.50");
    businessDAO1.setValue("testFloat", "2.30");
    businessDAO1.setValue("testInteger", "17");
    businessDAO1.setValue("testText", "This is gonna get much worse.");
    businessDAO1.setValue("testClob", "This is gonna get much worse part 2, Electric Boogaloo.");
    businessDAO1.addItem("testEnumeration", enumObject.getId());
    businessDAO1.apply();

    BusinessDAO businessDAO2 = BusinessDAO.newInstance(mdBusiness2.definesType());
    businessDAO2.apply();

    RelationshipDAO relationshipDAO1 = RelationshipDAO.newInstance(businessDAO2.getId(), businessDAO1.getId(), mdRelationship.definesType());
    relationshipDAO1.setValue("testChar", "Hello World!!!");
    relationshipDAO1.apply();

    TransactionExportManager.export(new LinkedList<String>(), CommonProperties.getTransactionXMLschemaLocation(), "testExport", EXPROT_TEST_DIR, new TransactionEventChangeListener());

    mdEnumeration = MdEnumerationDAO.get(mdEnumeration.getId()).getBusinessDAO();

    Map<String, Object> newUserA = loadAttributes(EntityDAO.get(newUser.getId()).getAttributeArrayIF());
    Map<String, Object> mdEnumerationA = loadAttributes(EntityDAO.get(mdEnumeration.getId()).getAttributeArrayIF());
    Map<String, Object> mdBusinessA = loadAttributes(MdBusinessDAO.get(mdBusiness.getId()).getAttributeArrayIF());
    Map<String, Object> mdBusinessA2 = loadAttributes(MdBusinessDAO.get(mdBusiness2.getId()).getAttributeArrayIF());
    Map<String, Object> mdRelationshipA = loadAttributes(MdRelationshipDAO.get(mdRelationship.getId()).getAttributeArrayIF());
    Map<String, Object> businessDAO1A = loadAttributes(EntityDAO.get(businessDAO1.getId()).getAttributeArrayIF());
    Map<String, Object> businessDAO2A = loadAttributes(EntityDAO.get(businessDAO2.getId()).getAttributeArrayIF());
    Map<String, Object> relationshipA = loadAttributes(EntityDAO.get(relationshipDAO1.getId()).getAttributeArrayIF());

    String d1id = businessDAO1.getId();
    String d2id = businessDAO2.getId();
    String r1id = relationshipDAO1.getId();

    if (cleanupData)
    {
      newUser.delete();
      ddlDeleteTestInstance(mdBusinessEnum, mdBusiness, mdBusiness2, mdRelationship);
    }

    if (deleteLogs)
    {
      resetTransactionDatatypes();
    }

    CommonProperties.setDomain(testImportSiteDomain);

    TransactionImportManager.importTransactions(EXPROT_TEST_DIR + File.separator + "testExport.zip", new DefaultConflictResolver(), new TransactionEventChangeListener());

    UserDAO newUser1 = UserDAO.findUser("s-Meth").getBusinessDAO();

    assertTrue(MdTypeDAO.isDefined(CLASS));
    assertTrue(MdTypeDAO.isDefined(CLASS2));
    assertTrue(MdTypeDAO.isDefined(RELATIONSHIP));
    assertTrue(checkAttributes(newUser1.getAttributeArrayIF(), newUserA));
    assertTrue(checkAttributes(MdEnumerationDAO.getMdEnumerationDAO("test.enumeration.AllValues").getAttributeArrayIF(), mdEnumerationA));
    assertTrue(checkAttributes(MdBusinessDAO.getMdElementDAO(CLASS).getAttributeArrayIF(), mdBusinessA));
    assertTrue(checkAttributes(MdBusinessDAO.getMdElementDAO(CLASS2).getAttributeArrayIF(), mdBusinessA2));
    assertTrue(checkAttributes(MdRelationshipDAO.getMdElementDAO(RELATIONSHIP).getAttributeArrayIF(), mdRelationshipA));
    assertTrue(checkAttributes(BusinessDAO.get(d1id).getAttributeArrayIF(), businessDAO1A));
    assertTrue(checkAttributes(BusinessDAO.get(d2id).getAttributeArrayIF(), businessDAO2A));
    assertTrue(checkAttributes(RelationshipDAO.get(r1id).getAttributeArrayIF(), relationshipA));

    CommonProperties.setDomain(testExportSiteDomain);

    FileIO.deleteFile(new File(EXPROT_TEST_DIR + File.separator + "testExport.zip"));

    resetTransactionDatatypes();
  }

  @Transaction
  private void ddlCreateTestInstance(MdBusinessDAO mdBusinessEnum, MdEnumerationDAO mdEnumeration, MdBusinessDAO mdBusiness, MdAttributeBlobDAO mdAttributeBlob, MdAttributeBooleanDAO mdAttributeBoolean, MdAttributeDecimalDAO mdAttributeDecimal, MdAttributeDoubleDAO mdAttributeDouble, MdAttributeFloatDAO mdAttributeFloat, MdAttributeIntegerDAO mdAttributeInteger, MdAttributeTextDAO mdAttributeText, MdAttributeClobDAO mdAttributeClob, MdAttributeEnumerationDAO mdAttributeEnumeration,
      MdBusinessDAO mdBusiness2, MdRelationshipDAO mdRelationship, MdAttributeConcreteDAO mdAttribute)
  {
    mdBusinessEnum.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, EnumerationMasterInfo.ID_VALUE);
    mdBusinessEnum.setValue(MdBusinessInfo.PACKAGE, "test.xmlclasses");
    mdBusinessEnum.setValue(MdBusinessInfo.NAME, "EnumClass1");
    mdBusinessEnum.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Importer Enumeration Class");
    mdBusinessEnum.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdBusinessEnum.apply();

    mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, mdBusinessEnum.getId());
    mdEnumeration.setValue(MdEnumerationInfo.NAME, "AllValues");
    mdEnumeration.setValue(MdEnumerationInfo.PACKAGE, "test.enumeration");
    mdEnumeration.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "AllValues");
    mdEnumeration.apply();

    mdBusiness.setValue(MdBusinessInfo.PACKAGE, "test.xmlclasses");
    mdBusiness.setValue(MdBusinessInfo.NAME, "Class1");
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Importer Class 1");
    mdBusiness.apply();

    mdAttributeBlob.setValue(MdAttributeBlobInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttributeBlob.setStructValue(MdAttributeBlobInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Blob");
    mdAttributeBlob.setStructValue(MdAttributeBlobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Blob");
    mdAttributeBlob.setValue(MdAttributeBlobInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeBlob.setValue(MdAttributeBlobInfo.NAME, "testBlob");
    mdAttributeBlob.apply();

    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Boolean");
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Boolean");
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.NAME, "testBoolean");
    mdAttributeBoolean.apply();

    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttributeDecimal.setStructValue(MdAttributeDecimalInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Decimal");
    mdAttributeDecimal.setStructValue(MdAttributeDecimalInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Decimal");
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.NAME, "testDecimal");
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.LENGTH, "3");
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.DECIMAL, "2");
    mdAttributeDecimal.apply();

    mdAttributeDouble.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttributeDouble.setStructValue(MdAttributeDoubleInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Double");
    mdAttributeDouble.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Double");
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.NAME, "testDouble");
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.LENGTH, "6");
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.DECIMAL, "2");
    mdAttributeDouble.apply();

    mdAttributeFloat.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttributeFloat.setStructValue(MdAttributeFloatInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Float");
    mdAttributeFloat.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Float");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.NAME, "testFloat");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.LENGTH, "5");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.DECIMAL, "2");
    mdAttributeFloat.apply();

    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Integer");
    mdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Integer");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.NAME, "testInteger");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttributeInteger.apply();

    mdAttributeText.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttributeText.setStructValue(MdAttributeTextInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Text");
    mdAttributeText.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Text");
    mdAttributeText.setValue(MdAttributeTextInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeText.setValue(MdAttributeTextInfo.NAME, "testText");
    mdAttributeText.apply();

    mdAttributeClob.setValue(MdAttributeClobInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttributeClob.setStructValue(MdAttributeClobInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Clob");
    mdAttributeClob.setStructValue(MdAttributeClobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Clob");
    mdAttributeClob.setValue(MdAttributeClobInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeClob.setValue(MdAttributeClobInfo.NAME, "testClob");
    mdAttributeClob.apply();

    mdAttributeEnumeration.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttributeEnumeration.setValue(MdAttributeEnumerationInfo.DESCRIPTION, "Test Enumeration");
    mdAttributeEnumeration.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Enumeration");
    mdAttributeEnumeration.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeEnumeration.setValue(MdAttributeEnumerationInfo.NAME, "testEnumeration");
    mdAttributeEnumeration.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeEnumeration.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, mdEnumeration.getId());
    mdAttributeEnumeration.apply();

    mdBusiness2.setValue(MdBusinessInfo.PACKAGE, "test.xmlclasses");
    mdBusiness2.setValue(MdBusinessInfo.NAME, "Class2");
    mdBusiness2.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Importer Class 2");
    mdBusiness2.apply();

    mdRelationship.setValue(MdRelationshipInfo.PACKAGE, "test.xmlclasses");
    mdRelationship.setValue(MdRelationshipInfo.NAME, "Relationship1");
    mdRelationship.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Importer Relationship 1");
    mdRelationship.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "*");
    mdRelationship.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Class 1");
    mdRelationship.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, mdBusiness.getId());
    mdRelationship.setValue(MdRelationshipInfo.CHILD_METHOD, "Class1");
    mdRelationship.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
    mdRelationship.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Class 2");
    mdRelationship.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, mdBusiness2.getId());
    mdRelationship.setValue(MdRelationshipInfo.PARENT_METHOD, "Class2");
    mdRelationship.apply();

    mdAttribute.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdRelationship.getId());
    mdAttribute.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Character");
    mdAttribute.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Character");
    mdAttribute.setValue(MdAttributeCharacterInfo.NAME, "testChar");
    mdAttribute.setValue(MdAttributeCharacterInfo.SIZE, Integer.toString(MdAttributeCharacterInfo.MAX_CHARACTER_SIZE));
    mdAttribute.apply();
  }

  @Transaction
  private void ddlDeleteTestInstance(MdBusinessDAO mdBusinessEnum, MdBusinessDAO mdBusiness, MdBusinessDAO mdBusiness2, MdRelationshipDAO mdRelationship)
  {
    mdRelationship.delete();
    mdBusiness2.delete();
    mdBusiness.delete();
    mdBusinessEnum.delete();
  }

  // /**
  // * Test importing a BusinessDAO with a lower sequence number than the one
  // * existing in the Database.
  // */
  // public void testLowerSequence()
  // {
  // MdBusinessDAO mdBusiness = ddlCreateTestLowerSequence();
  //
  // BusinessDAO businessDAO1 =
  // BusinessDAO.newInstance(mdBusiness.definesType());
  // businessDAO1.setValue("testChar", "firstValue");
  // businessDAO1.apply();
  // // Heads up: change
  // // FileInstanceExporter.export(INSTANCE_SET, SCHEMA_LOCATION.toString(),
  // businessDAO1.getSequence(), false);
  //
  // businessDAO1.setValue("testChar", "secondValue");
  // businessDAO1.apply();
  //
  // long seq = businessDAO1.getSequence();
  //
  // InstanceImporter.runImport(new File(INSTANCE_SET), SCHEMA_LOCATION);
  //
  // BusinessDAOIF d = BusinessDAO.get(businessDAO1.getId());
  //
  // assertEquals(seq, d.getSequence());
  // assertEquals("secondValue", d.getValue("testChar"));
  // }
  //
  // private MdBusinessDAO ddlCreateTestLowerSequence()
  // {
  // MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
  // mdBusiness.setValue(MdBusinessInfo.PACKAGE, "test.xmlclasses");
  // mdBusiness.setValue(MdBusinessInfo.NAME, "Class1");
  // mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Importer Class 1");
  // mdBusiness.apply();
  //
  // MdAttributeCharacterDAO mdAttribute =
  // MdAttributeCharacterDAO.newInstance();
  // mdAttribute.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS,
  // mdBusiness.getId());
  // mdAttribute.setStructValue(MdAttributeCharacterInfo.DESCRIPTION,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Character");
  // mdAttribute.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Character");
  // mdAttribute.setValue(MdAttributeCharacterInfo.NAME, "testChar");
  // mdAttribute.setValue(MdAttributeCharacterInfo.SIZE,
  // Integer.toString(MdAttributeCharacterInfo.MAX_CHARACTER_SIZE));
  // mdAttribute.apply();
  // return mdBusiness;
  // }
  //
  // /**
  // * Test importing a BusinessDAO with a higher sequence number
  // * than the one in the Database.
  // */
  // public void testUpperSequence()
  // {
  // InstanceImporter.runImport(new File(SEQUENCE_TEST1), SCHEMA_LOCATION);
  // InstanceImporter.runImport(new File(SEQUENCE_TEST2), SCHEMA_LOCATION);
  //
  // String id = EntityDAO.getEntityIdsDB(CLASS).get(0);
  // BusinessDAOIF businessDAO = BusinessDAO.get(id);
  //
  // assertEquals(21975, businessDAO.getSequence());
  // assertEquals("secondValue", businessDAO.getValue("testCharacter"));
  // }
  //
  // /**
  // * Test importing a BusinessDAO from a different Domain than the one in the
  // Database
  // */
  // public void testBadSite()
  // {
  // InstanceImporter.runImport(new File(SEQUENCE_TEST1), SCHEMA_LOCATION);
  // InstanceImporter.runImport(new File(SEQUENCE_TEST3), SCHEMA_LOCATION);
  //
  // String id = EntityDAO.getEntityIdsDB(CLASS).get(0);
  // BusinessDAOIF businessDAO = BusinessDAO.get(id);
  //
  // assertEquals(21974, businessDAO.getSequence());
  // assertEquals("firstValue", businessDAO.getValue("testCharacter"));
  // }
  //
  // /**
  // * Ensure that referenceAttributes are able to set a reference to an unknown
  // object
  // */
  // public void testUnknowReference()
  // {
  // InstanceImporter.runImport(new File(UNKNOWN_REFERENCE), SCHEMA_LOCATION);
  //
  // String id = EntityDAO.getEntityIdsDB(CLASS).get(0);
  // BusinessDAOIF businessDAO = BusinessDAO.get(id);
  //
  // //The reference does not exist in the database
  // assertEquals("8przkwyitd0aysugzn0p1fpyin4uq2m26vox54re7owwda4ivvx5yybva0n2a15n",
  // businessDAO
  // .getValue("testRef"));
  // }
  //
  // /**
  // * Test Importing and exporting StateMachine
  // */
  // public void testState()
  // {
  // MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
  // MdStateMachineDAO mdState = MdStateMachineDAO.newInstance();
  //
  // ddlCreateTestState(mdBusiness, mdState);
  //
  // StateMasterDAO state1 = mdState.addState("Start_State",
  // StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
  // String stateId = state1.apply();
  //
  // StateMasterDAO state2 = mdState.addState("End_State",
  // StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId());
  // state2.apply();
  //
  // RelationshipDAO transition = mdState.addTransition("GoTran",
  // state1.getId(), state2.getId());
  // transition.apply();
  //
  // BusinessDAO businessDAO =
  // BusinessDAO.newInstance(mdBusiness.definesType());
  // String id = businessDAO.apply();
  // businessDAO.promote("GoTran");
  //
  // assertEquals(state2, businessDAO.currentState());
  // // Heads up: change
  // // FileInstanceExporter.export(INSTANCE_SET, SCHEMA_LOCATION.toString(),
  // Math.min(mdBusiness.getSequence(), mdState.getSequence()), false);
  //
  // Map<String, Object> state1A = loadAttributes(state1.getAttributeArrayIF());
  // Map<String, Object> businessDAOA =
  // loadAttributes(businessDAO.getAttributeArrayIF());
  //
  // InstanceImporter.runImport(new File(INSTANCE_SET), SCHEMA_LOCATION);
  //
  // BusinessDAOIF data = BusinessDAO.get(id);
  // BusinessDAOIF state = BusinessDAO.get(stateId);
  //
  // assertEquals(state2, data.currentState());
  // assertTrue(checkAttributes(data.getAttributeArrayIF(), businessDAOA));
  // assertTrue(checkAttributes(state.getAttributeArrayIF(), state1A));
  // }
  //
  // @Transaction
  // private void ddlCreateTestState(MdBusinessDAO mdBusiness, MdStateMachineDAO
  // mdState)
  // {
  // mdBusiness.setValue(MdBusinessInfo.PACKAGE, "test.xmlclasses");
  // mdBusiness.setValue(MdBusinessInfo.NAME, "Class1");
  // mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Importer Class 1");
  // mdBusiness.apply();
  //
  // mdState.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER,
  // mdBusiness.getId());
  // mdState.setValue(MdStateMachineInfo.PACKAGE, "test.xmlclasses");
  // mdState.setValue(MdStateMachineInfo.NAME, "State1");
  // mdState.setStructValue(MdStateMachineInfo.DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "State Machine 1");
  // mdState.setValue(MdStateMachineInfo.SUPER_MD_BUSINESS,
  // MdStateMachineInfo.STATE_MASTER);
  // mdState.apply();
  // }
  //
  // public void testStructEnumeration()
  // {
  // MdBusinessDAO mdBusinessEnum = MdBusinessDAO.newInstance();
  // MdEnumerationDAO mdEnumeration = MdEnumerationDAO.newInstance();
  // MdStructDAO mdStruct = MdStructDAO.newInstance();
  // MdAttributeEnumerationDAO mdAttributeEnumeration =
  // MdAttributeEnumerationDAO.newInstance();
  // MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
  // MdAttributeStructDAO mdAttributeStruct =
  // MdAttributeStructDAO.newInstance();
  //
  // this.ddlCreateTestStructEnumeration(mdBusinessEnum, mdEnumeration,
  // mdStruct, mdAttributeEnumeration,
  // mdBusiness, mdAttributeStruct);
  //
  // BusinessDAO enumObject =
  // BusinessDAO.newInstance(mdBusinessEnum.definesType());
  // enumObject.setValue(EnumerationMasterInfo.NAME, "CO");
  // enumObject.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Colorado");
  // enumObject.apply();
  //
  // BusinessDAO enumObject2 =
  // BusinessDAO.newInstance(mdBusinessEnum.definesType());
  // enumObject2.setValue(EnumerationMasterInfo.NAME, "JU");
  // enumObject2.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "JU");
  // enumObject2.apply();
  //
  // BusinessDAO businessDAO =
  // BusinessDAO.newInstance(mdBusiness.definesType());
  // businessDAO.addStructItem("testStruct", "testEnumeration",
  // enumObject.getId());
  // String id = businessDAO.apply();
  // // Heads up: change
  // // FileInstanceExporter.export(INSTANCE_SET, SCHEMA_LOCATION.toString(),
  // mdBusinessEnum.getSequence(), false);
  //
  // Map<String, Object> mdEnumerationA =
  // loadAttributes(mdEnumeration.getAttributeArrayIF());
  // Map<String, Object> mdBusinessA =
  // loadAttributes(mdBusiness.getAttributeArrayIF());
  // Map<String, Object> businessDAO1A =
  // loadAttributes(businessDAO.getAttributeArrayIF());
  //
  // ddlDeleteTestStructEnumeration(mdBusinessEnum, mdStruct, mdBusiness);
  //
  // InstanceImporter.runImport(new File(INSTANCE_SET), SCHEMA_LOCATION);
  //
  // assertTrue(checkAttributes(MdEnumerationDAO.getMdEnumerationDAO("test.enumeration.AllValues")
  // .getAttributeArrayIF(), mdEnumerationA));
  // assertTrue(checkAttributes(MdBusinessDAO.getMdElementDAO(CLASS).getAttributeArrayIF(),
  // mdBusinessA));
  // assertTrue(checkAttributes(BusinessDAO.get(id).getAttributeArrayIF(),
  // businessDAO1A));
  //
  // // Clean up
  // mdStruct =
  // MdStructDAO.getMdStructDAO("test.xmlclasses.ClassBasic1").getBusinessDAO();
  // mdStruct.delete();
  //
  // }
  //
  // @Transaction
  // private void ddlCreateTestStructEnumeration(MdBusinessDAO mdBusinessEnum,
  // MdEnumerationDAO mdEnumeration, MdStructDAO mdStruct,
  // MdAttributeEnumerationDAO mdAttributeEnumeration, MdBusinessDAO mdBusiness,
  // MdAttributeStructDAO mdAttributeStruct)
  // {
  // mdBusinessEnum.setValue(MdBusinessInfo.SUPER_MD_BUSINESS,
  // EnumerationMasterInfo.ID_VALUE);
  // mdBusinessEnum.setValue(MdBusinessInfo.PACKAGE, "test.xmlclasses");
  // mdBusinessEnum.setValue(MdBusinessInfo.NAME, "EnumClass1");
  // mdBusinessEnum.setValue(MdBusinessInfo.EXTENDABLE,
  // MdAttributeBooleanInfo.FALSE);
  // mdBusinessEnum.setStructValue(MdBusinessInfo.DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Importer Enumeration Class");
  // mdBusinessEnum.apply();
  //
  // mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL,
  // MdAttributeBooleanInfo.TRUE);
  // mdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS,
  // mdBusinessEnum.getId());
  // mdEnumeration.setValue(MdEnumerationInfo.NAME, "AllValues");
  // mdEnumeration.setValue(MdEnumerationInfo.PACKAGE, "test.enumeration");
  // mdEnumeration.setStructValue(MdEnumerationInfo.DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "AllValues");
  // mdEnumeration.apply();
  //
  // mdStruct.setValue(MdStructInfo.PACKAGE, "test.xmlclasses");
  // mdStruct.setValue(MdStructInfo.NAME, "ClassBasic1");
  // mdStruct.setStructValue(MdStructInfo.DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Importer Basic 1");
  // mdStruct.apply();
  //
  // mdAttributeEnumeration.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS,
  // mdStruct.getId());
  // mdAttributeEnumeration.setStructValue(MdAttributeEnumerationInfo.DESCRIPTION,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Enumeration");
  // mdAttributeEnumeration.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Enumeration");
  // mdAttributeEnumeration.setValue(MdAttributeEnumerationInfo.REQUIRED,
  // MdAttributeBooleanInfo.TRUE);
  // mdAttributeEnumeration.setValue(MdAttributeEnumerationInfo.NAME,
  // "testEnumeration");
  // mdAttributeEnumeration.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE,
  // MdAttributeBooleanInfo.FALSE);
  // mdAttributeEnumeration.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION,
  // mdEnumeration.getId());
  // mdAttributeEnumeration.apply();
  //
  // mdBusiness.setValue(MdBusinessInfo.PACKAGE, "test.xmlclasses");
  // mdBusiness.setValue(MdBusinessInfo.NAME, "Class1");
  // mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Importer Class 1");
  // mdBusiness.apply();
  //
  // mdAttributeStruct.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS,
  // mdBusiness.getId());
  // mdAttributeStruct.setStructValue(MdAttributeStructInfo.DESCRIPTION,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Struct");
  // mdAttributeStruct.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Struct");
  // mdAttributeStruct.setValue(MdAttributeStructInfo.REQUIRED,
  // MdAttributeBooleanInfo.TRUE);
  // mdAttributeStruct.setValue(MdAttributeStructInfo.NAME, "testStruct");
  // mdAttributeStruct.setValue(MdAttributeStructInfo.MD_STRUCT,
  // mdStruct.getId());
  // mdAttributeStruct.apply();
  // }
  //
  // @Transaction
  // private void ddlDeleteTestStructEnumeration(MdBusinessDAO mdBusinessEnum,
  // MdStructDAO mdStruct,
  // MdBusinessDAO mdBusiness)
  // {
  // mdBusiness.delete();
  // mdStruct.delete();
  // mdBusinessEnum.delete();
  // }

  /**
   * Determines if the value of an attribute is consistent with its mapping.
   * Returns false if the value of the attribute do not match the values of the
   * mapping.
   * 
   * @param attributes
   *          The array of attributes
   * @param hashMap
   *          The attribute-value mapping
   * @return
   */
  @SuppressWarnings("unchecked")
  private boolean checkAttributes(AttributeIF[] attributes, Map<String, Object> hashMap)
  {
    for (AttributeIF attribute : attributes)
    {
      if (attribute instanceof AttributeEnumerationIF)
      {
        AttributeEnumerationIF enu = (AttributeEnumerationIF) attribute;

        List<String> list = (List<String>) hashMap.get(attribute.getName());

        for (BusinessDAOIF businessDAO : enu.dereference())
        {
          if (!list.contains(businessDAO.getId()))
          {
            return false;
          }
        }
      }
      else if (attribute instanceof AttributeEncryptionIF)
      {
        String hashValue = (String) hashMap.get(attribute.getName());
        String attrValue = attribute.getRawValue();

        if (!hashValue.equals(attrValue))
        {
          return false;
        }
      }
      else if (attribute instanceof AttributeStructIF)
      {
        AttributeStructIF struct = (AttributeStructIF) attribute;

        HashMap<String, Object> structHash = (HashMap<String, Object>) hashMap.get(attribute.getName());

        if (!checkAttributes(removeIDandKey(struct.getAttributeArrayIF()), structHash))
        {
          return false;
        }
      }
      else
      {
        String hashValue = ( (String) hashMap.get(attribute.getName()) ).replace("\r", "");
        String attrValue = attribute.getValue().replace("\r", "");

        if (!hashValue.trim().equals(attrValue.trim()))
        {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * Loads the values of an array of attributes into a mapping between the
   * Attribute name and the value. In some cases the value is represented as
   * other name-value mappings or list of values.
   * 
   * @param attributes
   *          The array of attributes to map
   * @return
   */
  private static Map<String, Object> loadAttributes(AttributeIF[] attributes)
  {
    Map<String, Object> hashMap = new HashMap<String, Object>();

    for (AttributeIF attribute : attributes)
    {
      if (attribute instanceof AttributeEnumerationIF)
      {
        AttributeEnumerationIF enu = (AttributeEnumerationIF) attribute;

        List<String> list = new LinkedList<String>();

        for (BusinessDAOIF businessDAO : enu.dereference())
        {
          list.add(businessDAO.getId());
        }

        hashMap.put(enu.getName(), list);
      }
      else if (attribute instanceof AttributeEncryptionIF)
      {
        hashMap.put(attribute.getName(), attribute.getRawValue());
      }
      else if (attribute instanceof AttributeStructIF)
      {
        AttributeStructIF struct = (AttributeStructIF) attribute;

        AttributeIF[] array = removeIDandKey(struct.getAttributeArrayIF());

        Map<String, Object> structMap = loadAttributes(array);

        hashMap.put(struct.getName(), structMap);
      }
      else
      {
        hashMap.put(attribute.getName(), attribute.getValue());
      }
    }

    return hashMap;
  }

  /**
   * Removes the {@link ComponentInfo.ID} and {@link ComponentInfo.KEY}
   * attributes from an array of attributes.
   * 
   * @param array
   *          An array of attributes
   * @return AttributeIF[]
   */
  private static AttributeIF[] removeIDandKey(AttributeIF[] array)
  {
    List<AttributeIF> list = new LinkedList<AttributeIF>();

    for (AttributeIF attribute : array)
    {
      if (!attribute.getName().equals(EntityInfo.ID) && !attribute.getName().equals(EntityInfo.KEY))
      {
        list.add(attribute);
      }
    }

    return list.toArray(new AttributeIF[list.size()]);
  }

  @Transaction
  private static void resetTransactionDatatypes()
  {
    MdBusinessDAOIF mdBusTransactionItem = MdBusinessDAO.getMdBusinessDAO(TransactionItemInfo.CLASS);
    mdBusTransactionItem.getBusinessDAO().deleteAllRecords();

    MdBusinessDAOIF mdBusTransactionRecord = MdBusinessDAO.getMdBusinessDAO(TransactionRecordInfo.CLASS);
    mdBusTransactionRecord.getBusinessDAO().deleteAllRecords();

    QueryFactory qf = new QueryFactory();
    BusinessDAOQuery importLogQ = qf.businessDAOQuery(ImportLogInfo.CLASS);
    OIterator<BusinessDAOIF> i = importLogQ.getIterator();
    for (BusinessDAOIF importLogDAOIF : i)
    {
      importLogDAOIF.getBusinessDAO().delete();
    }

    Database.resetTransactionSequence();
  }

  public static void buildSequenceXML()
  {
    MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.NAME, "Class1");
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, "test.xmlclasses");
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Class1");
    mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Class1 Desc");
    mdBusiness.apply();

    // long seq = mdBusiness.getSequence();

    MdAttributeCharacterDAO mdCharacter = MdAttributeCharacterDAO.newInstance();
    mdCharacter.setValue(MdAttributeCharacterInfo.NAME, "testCharacter");
    mdCharacter.setValue(MdAttributeCharacterInfo.SIZE, Integer.toString(MdAttributeCharacterInfo.MAX_CHARACTER_SIZE));
    mdCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "testCharacter");
    mdCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdCharacter.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("testCharacter", "firstValue");
    businessDAO.apply();
    // Heads up: change
    // FileInstanceExporter.export(SEQUENCE_TEST1, SCHEMA_LOCATION.toString(),
    // seq, false);

    businessDAO.setValue("testCharacter", "secondValue");
    businessDAO.apply();
    // Heads up: change
    // FileInstanceExporter.export(SEQUENCE_TEST1, SCHEMA_LOCATION.toString(),
    // businessDAO.getSequence(), false);
  }

  public static void buildUnkownReferenceXML()
  {
    MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.NAME, "Class1");
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, "test.xmlclasses");
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Class1");
    mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Class1 Desc");
    mdBusiness.apply();

    MdAttributeReferenceDAO mdReference = MdAttributeReferenceDAO.newInstance();
    mdReference.setValue(MdAttributeReferenceInfo.NAME, "testRef");
    mdReference.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "testRef");
    mdReference.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdReference.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, mdBusiness.getId());
    mdReference.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.apply();

    BusinessDAO businessDAO2 = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO2.setValue("testRef", businessDAO.getId());
    businessDAO2.apply();
    // Heads up: change
    // FileInstanceExporter.export(UNKNOWN_REFERENCE,
    // SCHEMA_LOCATION.toString(), mdBusiness.getSequence(), false);

    // After running this method go and remove the first businessDAO from
    // UNKOWN_REFERENCE.xml
  }

  public static void main(String args[])
  {
    // InstanceImportTest.buildSequenceXML();
    InstanceImportTest.buildUnkownReferenceXML();
  }
}
