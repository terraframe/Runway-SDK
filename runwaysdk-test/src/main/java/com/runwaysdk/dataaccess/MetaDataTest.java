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
/*
 * Created on Aug 9, 2005
 *
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package com.runwaysdk.dataaccess;

import java.util.LinkedList;
import java.util.List;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.IndexAttributeInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributePrimitiveInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdIndexInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.attributes.InvalidReferenceException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.AttributeOfWrongTypeForClassException;
import com.runwaysdk.dataaccess.metadata.CannotAddAttriubteToClassException;
import com.runwaysdk.dataaccess.metadata.ClassPublishException;
import com.runwaysdk.dataaccess.metadata.InheritanceException;
import com.runwaysdk.dataaccess.metadata.InvalidAttributeForIndexDefinitionException;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeStructDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdIndexDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.RequiredUniquenessConstraintException;

/**
 * @author nathan
 *
 */
public class MetaDataTest extends TestCase
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

  private static TypeInfo METADATA_TEST = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "MetaDataTest");

  private static TypeInfo STRUCT   = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "Struct");

  /**
   * Constructor for AttributeTest.
   *
   * @param name
   */
  public MetaDataTest(String name)
  {
    super(name);
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(new EntityMasterTestSetup(MetaDataTest.suite()));
  }

  /**
   * A suite() takes <b>this </b> <code>MetaData.class</code> and wraps it in
   * <code>MasterTestSetup</code>. The returned class is a suite of all the
   * tests in <code>AttributeTest</code>, with the global setUp() and
   * tearDown() methods from <code>MasterTestSetup</code>.
   *
   * @return A suite of tests wrapped in global setUp and tearDown methods
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(MetaDataTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
        classSetUp();
      }

      protected void tearDown()
      {
        MdStructDAO.getMdStructDAO(STRUCT.getType()).getBusinessDAO().delete();
        MdBusinessDAO.getMdBusinessDAO(METADATA_TEST.getType()).getBusinessDAO().delete();
      }
    };

    return wrapper;
  }

  public static void classSetUp()
  {
    // Create the TEST data type
    MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.NAME, METADATA_TEST.getTypeName());
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, METADATA_TEST.getPackageName());
    mdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Type");
    mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
    mdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdBusiness.setValue(MdElementInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdBusiness.setGenerateMdController(false);
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    // Create a struct
    MdStructDAO mdStruct = MdStructDAO.newInstance();
    mdStruct.setValue(MdStructInfo.NAME, STRUCT.getTypeName());
    mdStruct.setValue(MdStructInfo.PACKAGE, STRUCT.getPackageName());
    mdStruct.setValue(MdStructInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdStruct.setStructValue(MdStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Struct");
    mdStruct.setStructValue(MdStructInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Struct");
    mdStruct.setGenerateMdController(false);
    mdStruct.setValue(MdStructInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdStruct.apply();

    MdStructDAOIF mdStructIF = MdStructDAO.getMdStructDAO(STRUCT.getType());

    MdAttributeCharacterDAO mdAttrChar = MdAttributeCharacterDAO.newInstance();
    mdAttrChar.setValue(MdAttributeCharacterInfo.NAME, "someCharacter1");
    mdAttrChar.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE,  "Some Character 1");
    mdAttrChar.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrChar.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrChar.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdStructIF.getId());
    mdAttrChar.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttrChar.apply();

    mdAttrChar = MdAttributeCharacterDAO.newInstance();
    mdAttrChar.setValue(MdAttributeCharacterInfo.NAME, "someCharacter2");
    mdAttrChar.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE,  "Some Character 2");
    mdAttrChar.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrChar.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrChar.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdStructIF.getId());
    mdAttrChar.setValue(MdAttributeCharacterInfo.SIZE, "8");
    mdAttrChar.apply();
  }

  /**
   * Make sure we can set the publish attribute on the root of a hierarchy.
   */
  public void testValidPublishHierarchyRoot()
  {
    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(METADATA_TEST.getType());

    MdBusinessDAO subMdBusiness = MdBusinessDAO.newInstance();
    subMdBusiness.setValue(MdBusinessInfo.NAME, METADATA_TEST.getTypeName()+"_Sub");
    subMdBusiness.setValue(MdBusinessInfo.PACKAGE, METADATA_TEST.getPackageName());
    subMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    subMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Type Sublcass");
    subMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
    subMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    subMdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdBusinessIF.getId());
    subMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    subMdBusiness.setGenerateMdController(false);
    subMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    subMdBusiness.apply();

    MdBusinessDAO mdBusiness = null;

    try
    {
      mdBusiness = mdBusinessIF.getBusinessDAO();

      mdBusiness.setIsPublished(false);
      mdBusiness.apply();

      MdBusinessDAOIF subMdBusinessIF = MdBusinessDAO.get(subMdBusiness.getId());

      if (subMdBusinessIF.isPublished())
      {
        fail("Attribute ["+MdClassInfo.PUBLISH+"] on subclass was not modified with the attribute on the root of the hierarchy was modified.");
      }

    }
    catch(Throwable e)
    {
      fail("Unable to change the publish attribute on a MdBusiness that is the root of a hierarchy: "+e.getMessage());
    }
    finally
    {
      if (mdBusiness != null)
      {
        mdBusiness.setIsPublished(true);
        mdBusiness.apply();
      }

      subMdBusiness = MdBusinessDAO.get(subMdBusiness.getId()).getBusinessDAO();
      subMdBusiness.delete();
    }
  }

  /**
   * Make sure we can set the publish attribute on the root of a hierarchy.
   */
  public void testInvalidPublishHierarchyRoot()
  {
    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(METADATA_TEST.getType());

    MdBusinessDAO subMdBusiness = MdBusinessDAO.newInstance();
    subMdBusiness.setValue(MdBusinessInfo.NAME, METADATA_TEST.getTypeName()+"_Sub");
    subMdBusiness.setValue(MdBusinessInfo.PACKAGE, METADATA_TEST.getPackageName());
    subMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    subMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Type Sublcass");
    subMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
    subMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    subMdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdBusinessIF.getId());
    subMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    subMdBusiness.setGenerateMdController(false);
    subMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    subMdBusiness.apply();

    try
    {
      subMdBusiness.setIsPublished(false);
      subMdBusiness.apply();

      fail("Able to change the publish attribute on a MdBusiness that is not the root of a hierarchy.");
    }
    catch(ClassPublishException e)
    {
      // This is expected
    }
    finally
    {
      subMdBusiness.delete();
    }
  }

  /**
   * Make sure that references do not point to Structs.
   */
  public void testReferenceNoStruct()
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(METADATA_TEST.getType());

    MdStructDAOIF mdStructIF = MdStructDAO.getMdStructDAO(STRUCT.getType());

    try
    {
      MdAttributeReferenceDAO mdAttributeReference = MdAttributeReferenceDAO.newInstance();
      mdAttributeReference.setValue(MdAttributeReferenceInfo.NAME, "testSomeReference");
      mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE,  "Some Test Reference");
      mdAttributeReference.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeReference.setValue(MdAttributeReferenceInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
      mdAttributeReference.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, mdStructIF.getId());
      mdAttributeReference.apply();

      // The test should throw an exception - delete this object if it didn't
      mdAttributeReference.delete();
      fail("A reference attribute was defined that points to instances of a struct.");
    }
    catch (InvalidReferenceException e)
    {
      // This is expected
    }
  }

  /**
   * Makes sure you cannot define an enumeration master className that is
   * extendable. Subclasses of Constants.ROOT_ENUMERATION_ATTRIBUTE_CLASS cannot
   * be extended.
   */
  public void testInvalidNewEnummerationExtendable()
  {
    TypeInfo testEnumerationMaster = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "TestEnumerationMaster");

    MdBusinessDAOIF enumerationMaster = MdBusinessDAO.getMdBusinessDAO(EnumerationMasterInfo.CLASS);

    MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.NAME, testEnumerationMaster.getTypeName());
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, testEnumerationMaster.getPackageName());
    mdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Enumeration Master");
    mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Enumeration Master");
    mdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, enumerationMaster.getId());
    mdBusiness.setGenerateMdController(false);
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    try
    {
      mdBusiness.apply();
    }
    catch (InheritanceException e)
    {
      // This is expected
      return;
    }
    // should this test fail, delete this object
    mdBusiness.delete();
    fail("A master enumeration type was defined to be extendible.  Enumeration classes cannot be extended.");
  }

  /**
   * Makes sure that a class that extends EnumerationMaster cannot define an
   * MdAttributeEnumeration.
   */
  public void testExtendEnumerationMasterDefineEnumeration()
  {
    MdBusinessDAO mdBusiness = null;
    MdBusinessDAO stateEnumMdBusiness = null;
    try
    {
      TypeInfo testEnumerationMaster = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE,
          "TestEnumerationMaster");

      MdBusinessDAOIF enumerationMaster = MdBusinessDAO.getMdBusinessDAO(EnumerationMasterInfo.CLASS);

      mdBusiness = MdBusinessDAO.newInstance();
      mdBusiness.setValue(MdBusinessInfo.NAME, testEnumerationMaster.getTypeName());
      mdBusiness.setValue(MdBusinessInfo.PACKAGE, testEnumerationMaster.getPackageName());
      mdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Enumeration Master");
      mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Enumeration Master");
      mdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      mdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, enumerationMaster.getId());
      mdBusiness.setGenerateMdController(false);
      mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness.apply();

      // Define an enumeration that will be used by the MdAttributeEnumeration
      stateEnumMdBusiness = MdBusinessDAO.newInstance();
      stateEnumMdBusiness.setValue(MdBusinessInfo.NAME, "StateEnum");
      stateEnumMdBusiness.setValue(MdBusinessInfo.PACKAGE, EntityMasterTestSetup.JUNIT_PACKAGE);
      stateEnumMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      stateEnumMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "State");
      stateEnumMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "States of the Union");
      stateEnumMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      stateEnumMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      stateEnumMdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, enumerationMaster.getId());
      stateEnumMdBusiness.setGenerateMdController(false);
      stateEnumMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      stateEnumMdBusiness.apply();

      MdEnumerationDAO mdEnumeration = MdEnumerationDAO.newInstance();
      mdEnumeration.setValue(MdEnumerationInfo.NAME, "AnMdEnumeration");
      mdEnumeration.setValue(MdTypeInfo.PACKAGE, EntityMasterTestSetup.JUNIT_PACKAGE);
      mdEnumeration.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "All States in the United States");
      mdEnumeration.setStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test");
      mdEnumeration.setValue(MdEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
      mdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, stateEnumMdBusiness.getId());
      mdEnumeration.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdEnumeration.apply();


      // now (incorrectly) define an MdAttributeEnumeration on an MdBusiness that extends EnumerationMaster
      MdAttributeEnumerationDAO mdAttrEnum = MdAttributeEnumerationDAO.newInstance();
      mdAttrEnum.setValue(MdAttributeEnumerationInfo.NAME, "someEnumeration");
      mdAttrEnum.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE,  "some enum");
      mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, "");
      mdAttrEnum.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttrEnum.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, mdBusiness.getId());
      mdAttrEnum.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, mdEnumeration.getId());
      mdAttrEnum.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
      mdAttrEnum.apply();

      fail("An MdBusiness that extends EnumerationMaster cannot define an MdAttributeEnumeration.");
    }
    catch (CannotAddAttriubteToClassException e)
    {
      // This is expected
    }
    finally
    {
      TestFixtureFactory.delete(mdBusiness);
      TestFixtureFactory.delete(stateEnumMdBusiness);
    }
  }

  /**
   * Make sure that a reference attribute cannot be added to basic
   * classes.
   */
  public void testReferenceAttributeForAStruct()
  {
    MdStructDAOIF mdStructIF = MdStructDAO.getMdStructDAO(STRUCT.getType());

    MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.NAME, "SomeReference");
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, EntityMasterTestSetup.JUNIT_PACKAGE);
    mdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test invalid reference");
    mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Reference");
    mdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdBusiness.setGenerateMdController(false);
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    String referenceMdBusinessId = mdBusiness.apply();

    MdAttributeReferenceDAO mdAttributeReference = MdAttributeReferenceDAO.newInstance();
    mdAttributeReference.setValue(MdAttributeReferenceInfo.NAME, "testReference");
    mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE, "A reference");
    mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE, "");
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, referenceMdBusinessId);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdStructIF.getId());

    try
    {
      mdAttributeReference.apply();

      // the two lines below should not execute
      mdAttributeReference.delete();
      fail("A reference was added to a struct.");
    }
    catch (AttributeOfWrongTypeForClassException e)
    {
      // we want to land here
    }
    finally
    {
      mdBusiness.delete();
    }
  }

  /**
   * Make sure that struct attributes cannot be added to struct.
   */
  public void testStructAttributeForAStruct()
  {
    MdStructDAOIF mdStructIF = MdStructDAO.getMdStructDAO(STRUCT.getType());

    MdAttributeStructDAO mdAttrStruct = MdAttributeStructDAO.newInstance();
    mdAttrStruct.setValue(MdAttributeStructInfo.NAME, "testSomeStruct2");
    mdAttrStruct.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE, "Some Test Struct 2");
    mdAttrStruct.setValue(MdAttributeStructInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrStruct.setValue(MdAttributeStructInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrStruct.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, mdStructIF.getId());
    mdAttrStruct.setValue(MdAttributeStructInfo.MD_STRUCT, mdStructIF.getId());
    try
    {
      mdAttrStruct.apply();
    }
    catch (AttributeOfWrongTypeForClassException e)
    {
      // This is expected
      return;
    }
    // should this test fail, delete this object
    mdAttrStruct.delete();
    fail("A struct attribute was added to a struct.");
  }

  /**
   * Make sure that structs cannot be extended.
   */
  public void testDoNotExtendBStruct()
  {
    TypeInfo testExtendedBasic = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "TestExtendBasic");

    MdStructDAOIF mdStructIF = MdStructDAO.getMdStructDAO(STRUCT.getType());

    try
    {
      // Create an new type that we can reference with reference fields
      MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
      mdBusiness.setValue(MdBusinessInfo.NAME, testExtendedBasic.getTypeName());
      mdBusiness.setValue(MdBusinessInfo.PACKAGE, testExtendedBasic.getPackageName());
      mdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Extend Basic");
      mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit attempting to extend a struct");
      mdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      mdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdStructIF.getId());
      mdBusiness.setGenerateMdController(false);
      mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness.apply();

      // should this test fail, delete this object
      mdBusiness.delete();
      fail("A new class was allowed to extend a struct.");
    }
    catch (InvalidReferenceException e)
    {
      // This is expected
    }
  }

  /**
   * Tests that an attribute that should be unique should also be required.
   *
   */
  public void testRequiredUniqueAttribute()
  {
    MdStructDAOIF mdStruct = MdStructDAO.getMdStructDAO(STRUCT.getType());

    MdAttributeCharacterDAO mdAttrChar = MdAttributeCharacterDAO.newInstance();
    mdAttrChar.setValue(MdAttributeCharacterInfo.NAME, "uniqueRequired");
    mdAttrChar.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE, "Unique But Not Required");
    mdAttrChar.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrChar.addItem(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getId());
    mdAttrChar.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrChar.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdStruct.getId());
    mdAttrChar.setValue(MdAttributeCharacterInfo.SIZE, "32");

    try
    {
      mdAttrChar.apply();
    }
    catch (RequiredUniquenessConstraintException e)
    {
      if (!Database.allowsUniqueNonRequiredColumns())
      {
        // This is expected
        return;
      }
      fail("RequiredUniquenessConstraintException was thrown even though the database should support Unique NonRequired columns");
    }
    mdAttrChar.delete();
    
    if (!Database.allowsUniqueNonRequiredColumns())
    {
      fail("An attribute was defined to be unique, yet was not set to be required.  All unique attributes must be required.");
    }
  }

  /**
   * Tests that an attribute that should be unique should also be required.
   *
   */
  public void testRequiredUniqueAttributeGroup()
  {
    MdBusinessDAOIF metaDataMdBusiness = MdBusinessDAO.getMdBusinessDAO(METADATA_TEST.getType());

    MdAttributeCharacterDAO mdAttrChar = MdAttributeCharacterDAO.newInstance();
    mdAttrChar.setValue(MdAttributeCharacterInfo.NAME, "uniqueAttrGroupRequired");
    mdAttrChar.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE,  "Unique Attribute Group But Not Required");
    mdAttrChar.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrChar.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrChar.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, metaDataMdBusiness.getId());
    mdAttrChar.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttrChar.apply();

    MdIndexDAO mdIndex = MdIndexDAO.newInstance();
    mdIndex.setValue(MdIndexInfo.MD_ENTITY, metaDataMdBusiness.getId());
    mdIndex.setValue(MdIndexInfo.UNIQUE, MdAttributeBooleanInfo.TRUE);
    mdIndex.setStructValue(MdIndexInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE, "Index");
    mdIndex.apply();

    try
    {
      // add the unique group index
      mdIndex.addAttribute(mdAttrChar, 0);
      fail("An attribute was defined to be part of a unique attribute group, yet was not set to be required.  All such attributes must be required.");
    }
    catch (RequiredUniquenessConstraintException e)
    {
      // This is expected
    }
    finally
    {
      mdIndex.delete();
      mdAttrChar.delete();
    }
  }

  /**
   * Make an existing attribute on the TEST class unique, and check to make
   * sure that a database index for that attribute was created. Change the
   * attribute back to not being unique and check that the database no longer
   * has the index.
   */
  public void testUniqueAttributeDatabaseIndex()
  {
    MdBusinessDAOIF metaDataMdBusiness = MdBusinessDAO.getMdBusinessDAO(METADATA_TEST.getType());

    MdAttributeCharacterDAO mdAttribute1 = MdAttributeCharacterDAO.newInstance();
    mdAttribute1.setValue(MdAttributeCharacterInfo.NAME, "attrUniqueChar");
    mdAttribute1.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttribute1.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE,  "Text Attribute Group Text");
    mdAttribute1.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    mdAttribute1.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttribute1.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttribute1.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, metaDataMdBusiness.getId());

    try
    {
      mdAttribute1.apply();

      MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO(METADATA_TEST.getType());

      if (Database.uniqueAttributeExists(mdBusiness.getTableName(), "attr_unique_char", mdAttribute1.getIndexName()))
      {
        fail("A database unique index was created for an attribute that was not set to be unique.");
      }

      mdAttribute1.addItem(MdAttributeConcreteInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getId());
      mdAttribute1.apply();

      if (!Database.uniqueAttributeExists(mdBusiness.getTableName(), "attr_unique_char", mdAttribute1.getIndexName()))
      {
        fail("A database unique index was not created for an attribute that was set to be unique.");
      }

      mdAttribute1.addItem(MdAttributeConcreteInfo.INDEX_TYPE, IndexTypes.NO_INDEX.getId());
      mdAttribute1.apply();

      if (Database.uniqueAttributeExists(mdBusiness.getTableName(), "attr_unique_char", mdAttribute1.getIndexName()))
      {
        mdAttribute1.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);
        fail("A database unique index was not dropped for an attribute that was set to be unique.");
      }

    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
    finally
    {
      if (mdAttribute1 != null && !mdAttribute1.isNew())
      {
        mdAttribute1.delete();
      }

    }
  }

  /**
   * Give an existing attribute on the TEST a non unique index, and check to make
   * sure that a database index for that attribute was created. Change the
   * attribute back to not being unique and check that the database no longer
   * has the index.
   */
  public void testNonUniqueAttributeDatabaseIndex()
  {
    MdBusinessDAOIF metaDataMdBusiness = MdBusinessDAO.getMdBusinessDAO(METADATA_TEST.getType());

    MdAttributeConcreteDAO mdAttribute1 = MdAttributeCharacterDAO.newInstance();
    mdAttribute1.setValue(MdAttributeCharacterInfo.NAME, "attrNonUniqueChar");
    mdAttribute1.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttribute1.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE,  "Text Attribute Group Text");
    mdAttribute1.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    mdAttribute1.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttribute1.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttribute1.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, metaDataMdBusiness.getId());

    try
    {
      mdAttribute1.apply();

      MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO(METADATA_TEST.getType());

      if (Database.nonUniqueAttributeExists(mdBusiness.getTableName(), "attr_non_unique_char", mdAttribute1.getIndexName()))
      {
        fail("A database non unique index was created for an attribute that was not set to be non unique.");
      }

      mdAttribute1.addItem(MdAttributeConcreteInfo.INDEX_TYPE, IndexTypes.NON_UNIQUE_INDEX.getId());
      mdAttribute1.apply();

      if (!Database.nonUniqueAttributeExists(mdBusiness.getTableName(), "attr_non_unique_char", mdAttribute1.getIndexName()))
      {
        fail("A database non unique index was not created for an attribute that was set to be non unique.");
      }

      mdAttribute1.addItem(MdAttributeConcreteInfo.INDEX_TYPE, IndexTypes.NO_INDEX.getId());
      mdAttribute1.apply();

      if (Database.nonUniqueAttributeExists(mdBusiness.getTableName(), "attr_non_unique_char", mdAttribute1.getIndexName()))
      {
        mdAttribute1.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);
        fail("A database non unique index was not dropped for an attribute that was set to be not unique.");
      }

    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
    finally
    {
      if (mdAttribute1 != null && !mdAttribute1.isNew())
      {
        mdAttribute1.delete();
      }

    }
  }

  /**
   * Define two attributes on the test class that do not participate in a unique
   * attribute group. Check the database to ensure that no index was created.
   * Make both attributes participate in the uniqueness constraint. Check the
   * database to ensure that an index was created for both attributes. Remove
   * one attribute from the unique attribute group. Check the database to
   * ensure that the old index is deleted and a new index is created containing
   * only the one attribute that participates in the unique attribute group.
   * Remove the remaining attribute from the group and ensure that the database
   * has dropped the index.
   */
  public void testUniqueAttributeGroupDatabaseIndex()
  {
    MdBusinessDAOIF metaDataMdBusiness = MdBusinessDAO.getMdBusinessDAO(METADATA_TEST.getType());

    MdAttributeCharacterDAO mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "testAttributeGroupCharacter2");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.COLUMN_NAME, "test_attr_group_char2");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE, "Text Attribute Group Text");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, metaDataMdBusiness.getId());

    MdAttributeIntegerDAO mdAttributeInteger = MdAttributeIntegerDAO.newInstance();
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.NAME, "testAttributeGroupInteger2");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.COLUMN_NAME, "test_attr_group_int2");
    mdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE,  "Text Attribute Group Integer");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, metaDataMdBusiness.getId());

    try
    {
      mdAttributeCharacter.apply();
      mdAttributeInteger.apply();

      String metaDataTestTableName = MdBusinessDAO.getMdBusinessDAO(METADATA_TEST.getType()).getTableName();

      MdEntityDAOIF mdEntity = (MdEntityDAOIF)mdAttributeCharacter.definedByClass();

      // Should be an index
      MdIndexDAO mdIndex = MdIndexDAO.newInstance();
      mdIndex.setValue(MdIndexInfo.MD_ENTITY, metaDataMdBusiness.getId());
      mdIndex.setValue(MdIndexInfo.UNIQUE, MdAttributeBooleanInfo.TRUE);
      mdIndex.setStructValue(MdIndexInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE, "Index");
      mdIndex.apply();

      // add the unique group index
      mdIndex.addAttribute(mdAttributeCharacter, 0);
      mdIndex.apply();

      mdIndex.addAttribute(mdAttributeInteger, 1);
      mdIndex.setValue(MdIndexInfo.ACTIVE, MdAttributeBooleanInfo.TRUE);
      mdIndex.apply();

      List<String> columnNameList = new LinkedList<String>();
      columnNameList.add("test_attr_group_char2");
      columnNameList.add("test_attr_group_int2");
      if (!Database.groupAttributeIndexExists(metaDataTestTableName, mdIndex.getIndexName(), columnNameList))
      {
        fail("A database unique attribute group index was not created for a set of attributes that do participate in a unique attribute group.");
      }
      mdEntity = (MdEntityDAOIF)mdAttributeCharacter.definedByClass();
      if (mdEntity.getUniqueIndexes().size() == 0)
      {
        fail("Class \"" + mdEntity.definesType() + "\" has a unique attribute group.");
      }

      mdIndex.removeAttribute(mdAttributeInteger);

      // Should not be an index
      if (Database.getGroupIndexAttributes(metaDataTestTableName, mdIndex.getIndexName()).size() > 0)
      {
        fail("A database unique attribute group index was not dropped for a set of attributes in which one attribute was removed from the group.");
      }
      mdEntity = (MdEntityDAOIF)mdAttributeCharacter.definedByClass();
      if (mdEntity.getUniqueIndexes().size() > 1)
      {
        fail("Class \"" + mdEntity.definesType() + "\" does not have a unique attribute group.");
      }

      mdAttributeCharacter.addItem(MdAttributeConcreteInfo.INDEX_TYPE, IndexTypes.NO_INDEX.getId());
      mdAttributeCharacter.apply();

      // Should not be an index
      if (Database.getGroupIndexAttributes(metaDataTestTableName, mdIndex.getIndexName()).size() > 0)
      {
        fail("A database unique attribute group index was not dropped for a set of attributes in which one attribute was removed from the group.");
      }
      mdEntity = (MdEntityDAOIF)mdAttributeCharacter.definedByClass();
      if (mdEntity.getUniqueIndexes().size() > 1)
      {
        fail("Class \"" + mdEntity.definesType() + "\" does not have a unique attribute group.");
      }

    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
    finally
    {
      try
      {
        mdAttributeCharacter = MdAttributeCharacterDAO.get(mdAttributeCharacter.getId()).getBusinessDAO();
        if (mdAttributeCharacter != null && !mdAttributeCharacter.isNew())
        {
          mdAttributeCharacter.delete();
        }
      }
      catch (DataNotFoundException e) {}

      try
      {
        mdAttributeInteger = MdAttributeIntegerDAO.get(mdAttributeInteger.getId()).getBusinessDAO();
        if (mdAttributeInteger != null && !mdAttributeInteger.isNew())
        {
          mdAttributeInteger.delete();
        }
      }
      catch (DataNotFoundException e) {}
    }
  }

  /**
   * Ensure that an attribute added to an MdIndex via an IndexAttribute relationship is actually defined by
   * the MdEntity that the MdIndex is defined on.
   *
   */
  public void testValidIndexDefinition()
  {
    MdBusinessDAOIF metaDataMdBusiness = MdBusinessDAO.getMdBusinessDAO(METADATA_TEST.getType());

    MdAttributeCharacterDAO mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "testAttributeGroupCharacter3");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.COLUMN_NAME, "test_attr_group_char3");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE,  "Text Attribute Group Text");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, metaDataMdBusiness.getId());
    mdAttributeCharacter.apply();

    MdAttributeIntegerDAO mdAttributeInteger = MdAttributeIntegerDAO.newInstance();
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.NAME, "testAttributeGroupInteger3");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.COLUMN_NAME, "test_attr_group_int3");
    mdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE,  "Text Attribute Group Integer");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, metaDataMdBusiness.getId());
    mdAttributeInteger.apply();

    MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.NAME, "InvalidIndexTestClass");
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, METADATA_TEST.getPackageName());
    mdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Type");
    mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
    mdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdIndexDAO mdIndex = MdIndexDAO.newInstance();
    mdIndex.setValue(MdIndexInfo.MD_ENTITY, mdBusiness.getId());
    mdIndex.setValue(MdIndexInfo.UNIQUE, MdAttributeBooleanInfo.TRUE);
    mdIndex.setStructValue(MdIndexInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE, "Index");
    mdIndex.apply();

    RelationshipDAO relationshipDAO1 = mdIndex.addChild(mdAttributeCharacter, IndexAttributeInfo.CLASS);
    relationshipDAO1.setValue(IndexAttributeInfo.INDEX_ORDER, "0");
    RelationshipDAO relationshipDAO2 = mdIndex.addChild(mdAttributeInteger, IndexAttributeInfo.CLASS);
    relationshipDAO1.setValue(IndexAttributeInfo.INDEX_ORDER, "1");
    try
    {
      relationshipDAO1.apply();
      relationshipDAO2.apply();
      fail("Able to add attribute to an ["+MdIndexInfo.CLASS+ "] that are not defined by the same ["+MdElementInfo.CLASS+"] that the index is defined on.");
    }
    catch (InvalidAttributeForIndexDefinitionException ex)
    {
      // we want to land here
    }
    finally
    {
      if (relationshipDAO1 != null && !relationshipDAO1.isNew())
      {
        relationshipDAO1.delete();
      }
      if (relationshipDAO2 != null && !relationshipDAO2.isNew())
      {
        relationshipDAO2.delete();
      }
      if (mdIndex != null && !mdIndex.isNew())
      {
        mdIndex.delete();
      }
      if (mdAttributeCharacter != null && !mdAttributeCharacter.isNew())
      {
        mdAttributeCharacter.delete();
      }
      if (mdAttributeInteger != null && !mdAttributeInteger.isNew())
      {
        mdAttributeInteger.delete();
      }
      if (mdBusiness != null && !mdBusiness.isNew())
      {
        mdBusiness.delete();
      }
    }
  }


  /**
   * Tests that a type can't extend MdAttribute.
   */
  public void testExtendAttribute()
  {
    MdBusinessDAO mdBusiness = null;
    try
    {
      MdBusinessDAOIF mdPrimitive = MdBusinessDAO.getMdBusinessDAO(MdAttributePrimitiveInfo.CLASS);

      mdBusiness = MdBusinessDAO.newInstance();
      mdBusiness.setValue(MdBusinessInfo.NAME, "AttributeExtension");
      mdBusiness.setValue(MdBusinessInfo.PACKAGE, EntityMasterTestSetup.JUNIT_PACKAGE);
      mdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test type");
      mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Reference Type");
      mdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      mdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdPrimitive.getId());
      mdBusiness.setGenerateMdController(false);
      mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness.apply();

      fail("A type was able to extend MdAttribute (this isn't allowed).");
    }
    catch (DataAccessException e)
    {
      // we want to land here
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (mdBusiness != null && mdBusiness.isAppliedToDB())
        mdBusiness.delete();
    }
  }


  /**
   * Tests to make sure that an attribute cannot be added to an MdElement.
   */
  public void testAddAttributeToMdRelationshipClassAttirbute()
  {
    MdAttributeIntegerDAO mdAttributeInteger = null;

    try
    {
      MdRelationshipDAOIF mdRelationship = MdRelationshipDAO.getMdRelationshipDAO(RelationshipTypes.CLASS_ATTRIBUTE_CONCRETE.getType());

      mdAttributeInteger = MdAttributeIntegerDAO.newInstance();
      mdAttributeInteger.setValue(MdAttributeIntegerInfo.NAME,               "testInvalidInteger");
      mdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE,      "bad integer attribute");
      mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE,      "");
      mdAttributeInteger.setValue(MdAttributeIntegerInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
      mdAttributeInteger.setValue(MdAttributeIntegerInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
      mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, mdRelationship.getId());
      mdAttributeInteger.apply();

      fail("An attribute was added to relationship ["+RelationshipTypes.CLASS_ATTRIBUTE_CONCRETE.getType()+"] (this is not allowed)");
    }
    catch(CannotAddAttriubteToClassException e)
    {
      // we want to land here.
    }
    catch(Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if(mdAttributeInteger != null && mdAttributeInteger.isAppliedToDB()) mdAttributeInteger.delete();
    }
  }

  /**
   * Tests to make sure that an attribute cannot be added to an MdAttribute or a subclass.
   */
  public void testAddAttributeToMdAttribute()
  {
    MdAttributeIntegerDAO mdAttributeInteger = null;

    try
    {
      MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO(MdAttributeIntegerInfo.CLASS);

      mdAttributeInteger = MdAttributeIntegerDAO.newInstance();
      mdAttributeInteger.setValue(MdAttributeIntegerInfo.NAME,               "testInvalidInteger");
      mdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE,      "bad integer attribute");
      mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE,      "");
      mdAttributeInteger.setValue(MdAttributeIntegerInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
      mdAttributeInteger.setValue(MdAttributeIntegerInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
      mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, mdBusiness.getId());
      mdAttributeInteger.apply();

      fail("An attribute was added to an MdAttribute (this is not allowed)");
    }
    catch(CannotAddAttriubteToClassException e)
    {
      // we want to land here.
    }
    catch(Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if(mdAttributeInteger != null && mdAttributeInteger.isAppliedToDB()) mdAttributeInteger.delete();
    }
  }
}
