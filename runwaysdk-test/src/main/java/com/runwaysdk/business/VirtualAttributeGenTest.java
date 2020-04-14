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
package com.runwaysdk.business;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.runwaysdk.ClasspathTestRunner;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeVirtualInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeVirtualDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdPackage;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.Request;

@RunWith(ClasspathTestRunner.class)
public class VirtualAttributeGenTest
{
  private static String                    pack = "test.viewquery";

  /**
   * MdBusinessIF instance that defines GENRE.
   */
  private static MdBusinessDAOIF           genreEnumMdBusinessIF;

  /**
   * MdEnumerationIF instance that defines GENRE.
   */
  private static MdEnumerationDAOIF        genreMdEnumerationIF;

  /**
   * Metadata that defines of the single select enumeration
   */
  private static MdAttributeEnumerationDAO mdAttrEnumSingle;

  private static MdBusinessDAO             bookMdBusiness;

  private static MdAttributeCharacterDAO   titleMdAttribute;

  private static MdBusinessDAO             authorMdBusiness;

  private static MdAttributeCharacterDAO   authorNameMdAttribute;

  private static MdViewDAO                 bookMdView;

  private static MdAttributeVirtualDAO     virtualGenreMdAttribute;

  private static MdAttributeVirtualDAO     virtualTitleMdAttribute;

  private static MdAttributeVirtualDAO     virtualAuthorNameMdAttribute;

  private static MdBusinessDAO             personMdBusiness;

  private static MdBusinessDAO             employeeMdBusiness;

  private static MdAttributeCharacterDAO   personNameMdAttribute;

  private static MdAttributeIntegerDAO     employeeIdMdAttribute;

  private static MdViewDAO                 employeeMdView;

  private static MdAttributeVirtualDAO     virtualPersonNameMdAttribute;

  private static MdAttributeVirtualDAO     virtualEmployeeIdMdAttribute;

  private static MdAttributeVirtualDAO     virtualSupervisorNameMdAttribute;

  private static MdAttributeVirtualDAO     virtualSupervisorIdMdAttribute;

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    MdBusinessDAOIF enumMasterMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EnumerationMasterInfo.CLASS);

    // New Type (GENRE) extends Enumeration_Attribute
    MdBusinessDAO genreEnumMdBusiness = MdBusinessDAO.newInstance();
    genreEnumMdBusiness.setValue(MdBusinessInfo.NAME, "GenreMaster");
    genreEnumMdBusiness.setValue(MdBusinessInfo.PACKAGE, pack);
    genreEnumMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    genreEnumMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Genre Master");
    genreEnumMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Genre Master");
    genreEnumMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    genreEnumMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    genreEnumMdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, enumMasterMdBusinessIF.getOid());
    genreEnumMdBusiness.apply();
    genreEnumMdBusinessIF = genreEnumMdBusiness;

    // Instantiate an md_enumeration to define State
    MdEnumerationDAO mdEnumeration = MdEnumerationDAO.newInstance();
    mdEnumeration.setValue(MdEnumerationInfo.NAME, "Genre");
    mdEnumeration.setValue(MdEnumerationInfo.PACKAGE, pack);
    mdEnumeration.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "All Genres");
    mdEnumeration.setStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test");
    mdEnumeration.setValue(MdEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, genreEnumMdBusinessIF.getOid());
    mdEnumeration.apply();
    genreMdEnumerationIF = mdEnumeration;

    // Define the options for the enumeration
    BusinessDAO businessDAO = BusinessDAO.newInstance(genreEnumMdBusiness.definesType());
    businessDAO.setValue(EnumerationMasterInfo.NAME, "FICTION");
    businessDAO.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Fiction");
    businessDAO.apply();

    businessDAO = BusinessDAO.newInstance(genreEnumMdBusiness.definesType());
    businessDAO.setValue(EnumerationMasterInfo.NAME, "NON_FICTION");
    businessDAO.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Non Fiction");
    businessDAO.apply();

    bookMdBusiness = MdBusinessDAO.newInstance();
    bookMdBusiness.setValue(MdBusinessInfo.NAME, "Book");
    bookMdBusiness.setValue(MdBusinessInfo.PACKAGE, pack);
    bookMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Book");
    bookMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    bookMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    bookMdBusiness.apply();

    mdAttrEnumSingle = MdAttributeEnumerationDAO.newInstance();
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.NAME, "genre");
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, "Single select state attribute");
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, "");
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, bookMdBusiness.getOid());
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, genreMdEnumerationIF.getOid());
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
    mdAttrEnumSingle.apply();

    titleMdAttribute = MdAttributeCharacterDAO.newInstance();
    titleMdAttribute.setValue(MdAttributeCharacterInfo.NAME, "title");
    titleMdAttribute.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Title");
    titleMdAttribute.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "Moby Dick");
    titleMdAttribute.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test title Attribute");
    titleMdAttribute.setValue(MdAttributeCharacterInfo.SIZE, "64");
    titleMdAttribute.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    titleMdAttribute.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());
    titleMdAttribute.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, bookMdBusiness.getOid());
    titleMdAttribute.apply();

    authorMdBusiness = MdBusinessDAO.newInstance();
    authorMdBusiness.setValue(MdBusinessInfo.NAME, "Author");
    authorMdBusiness.setValue(MdBusinessInfo.PACKAGE, pack);
    authorMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Author");
    authorMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    authorMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    authorMdBusiness.apply();

    authorNameMdAttribute = MdAttributeCharacterDAO.newInstance();
    authorNameMdAttribute.setValue(MdAttributeCharacterInfo.NAME, "authorName");
    authorNameMdAttribute.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Author Name");
    authorNameMdAttribute.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "Herman Melville");
    authorNameMdAttribute.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test authorNameame Attribute");
    authorNameMdAttribute.setValue(MdAttributeCharacterInfo.SIZE, "64");
    authorNameMdAttribute.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    authorNameMdAttribute.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());
    authorNameMdAttribute.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, authorMdBusiness.getOid());
    authorNameMdAttribute.apply();

    bookMdView = MdViewDAO.newInstance();
    bookMdView.setValue(MdViewInfo.NAME, "BookView");
    bookMdView.setValue(MdViewInfo.PACKAGE, pack);
    bookMdView.setStructValue(MdViewInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Book");
    bookMdView.setValue(MdViewInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    bookMdView.setValue(MdViewInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    bookMdView.apply();

    virtualGenreMdAttribute = MdAttributeVirtualDAO.newInstance();
    virtualGenreMdAttribute.setValue(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE, mdAttrEnumSingle.getOid());
    virtualGenreMdAttribute.setValue(MdAttributeVirtualInfo.DEFINING_MD_VIEW, bookMdView.getOid());
    virtualGenreMdAttribute.apply();

    virtualTitleMdAttribute = MdAttributeVirtualDAO.newInstance();
    virtualTitleMdAttribute.setValue(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE, titleMdAttribute.getOid());
    virtualTitleMdAttribute.setValue(MdAttributeVirtualInfo.DEFINING_MD_VIEW, bookMdView.getOid());
    virtualTitleMdAttribute.apply();

    virtualAuthorNameMdAttribute = MdAttributeVirtualDAO.newInstance();
    virtualAuthorNameMdAttribute.setValue(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE, authorNameMdAttribute.getOid());
    virtualAuthorNameMdAttribute.setValue(MdAttributeVirtualInfo.DEFINING_MD_VIEW, bookMdView.getOid());
    virtualAuthorNameMdAttribute.apply();

    personMdBusiness = MdBusinessDAO.newInstance();
    personMdBusiness.setValue(MdBusinessInfo.NAME, "Person");
    personMdBusiness.setValue(MdBusinessInfo.PACKAGE, pack);
    personMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Person");
    personMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    personMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    personMdBusiness.apply();

    personNameMdAttribute = MdAttributeCharacterDAO.newInstance();
    personNameMdAttribute.setValue(MdAttributeCharacterInfo.NAME, "personName");
    personNameMdAttribute.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Person Name");
    personNameMdAttribute.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    personNameMdAttribute.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Person Name Attribute");
    personNameMdAttribute.setValue(MdAttributeCharacterInfo.SIZE, "64");
    personNameMdAttribute.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    personNameMdAttribute.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());
    personNameMdAttribute.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, personMdBusiness.getOid());
    personNameMdAttribute.apply();

    employeeMdBusiness = MdBusinessDAO.newInstance();
    employeeMdBusiness.setValue(MdBusinessInfo.NAME, "Employee");
    employeeMdBusiness.setValue(MdBusinessInfo.PACKAGE, pack);
    employeeMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Employee");
    employeeMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    employeeMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    employeeMdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, personMdBusiness.getOid());
    employeeMdBusiness.apply();

    employeeIdMdAttribute = MdAttributeIntegerDAO.newInstance();
    employeeIdMdAttribute.setValue(MdAttributeIntegerInfo.NAME, "employeeId");
    employeeIdMdAttribute.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Employee Id");
    employeeIdMdAttribute.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
    employeeIdMdAttribute.setStructValue(MdAttributeIntegerInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Employee Id Attribute");
    employeeIdMdAttribute.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    employeeIdMdAttribute.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());
    employeeIdMdAttribute.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, employeeMdBusiness.getOid());
    employeeIdMdAttribute.apply();

    MdAttributeReferenceDAO supervisorReferenceMdAttribute = MdAttributeReferenceDAO.newInstance();
    supervisorReferenceMdAttribute.setValue(MdAttributeReferenceInfo.NAME, "supervisor");
    supervisorReferenceMdAttribute.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Supervisor");
    supervisorReferenceMdAttribute.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE, "");
    supervisorReferenceMdAttribute.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Employee Supervisor");
    supervisorReferenceMdAttribute.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    supervisorReferenceMdAttribute.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, employeeMdBusiness.getOid());
    supervisorReferenceMdAttribute.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, employeeMdBusiness.getOid());
    supervisorReferenceMdAttribute.apply();

    MdRelationshipDAO mdRelationship = MdRelationshipDAO.newInstance();
    mdRelationship.setValue(MdRelationshipInfo.PACKAGE, pack);
    mdRelationship.setValue(MdRelationshipInfo.NAME, "TestRelationship");
    mdRelationship.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Relationship");
    mdRelationship.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
    mdRelationship.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent Relationship");
    mdRelationship.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, employeeMdBusiness.getOid());
    mdRelationship.setValue(MdRelationshipInfo.PARENT_METHOD, "testParent");
    mdRelationship.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "*");
    mdRelationship.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child Relationship");
    mdRelationship.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, employeeMdBusiness.getOid());
    mdRelationship.setValue(MdRelationshipInfo.CHILD_METHOD, "testChild");
    mdRelationship.apply();

    employeeMdView = MdViewDAO.newInstance();
    employeeMdView.setValue(MdViewInfo.NAME, "EmployeeView");
    employeeMdView.setValue(MdViewInfo.PACKAGE, pack);
    employeeMdView.setStructValue(MdViewInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Employee");
    employeeMdView.setValue(MdViewInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    employeeMdView.setValue(MdViewInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    employeeMdView.apply();

    virtualPersonNameMdAttribute = MdAttributeVirtualDAO.newInstance();
    virtualPersonNameMdAttribute.setValue(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE, personNameMdAttribute.getOid());
    virtualPersonNameMdAttribute.setValue(MdAttributeVirtualInfo.DEFINING_MD_VIEW, employeeMdView.getOid());
    virtualPersonNameMdAttribute.apply();

    virtualEmployeeIdMdAttribute = MdAttributeVirtualDAO.newInstance();
    virtualEmployeeIdMdAttribute.setValue(MdAttributeVirtualInfo.NAME, "empId");

    virtualEmployeeIdMdAttribute.setValue(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE, employeeIdMdAttribute.getOid());
    virtualEmployeeIdMdAttribute.setValue(MdAttributeVirtualInfo.DEFINING_MD_VIEW, employeeMdView.getOid());
    virtualEmployeeIdMdAttribute.apply();

    virtualSupervisorNameMdAttribute = MdAttributeVirtualDAO.newInstance();
    virtualSupervisorNameMdAttribute.setValue(MdAttributeVirtualInfo.NAME, "supervisorName");
    virtualSupervisorNameMdAttribute.setStructValue(MdAttributeVirtualInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Supervisor Name");
    virtualSupervisorNameMdAttribute.setStructValue(MdAttributeVirtualInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Supervisor Name Attribute");
    virtualSupervisorNameMdAttribute.setValue(MdAttributeVirtualInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    virtualSupervisorNameMdAttribute.setValue(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE, personNameMdAttribute.getOid());
    virtualSupervisorNameMdAttribute.setValue(MdAttributeVirtualInfo.DEFINING_MD_VIEW, employeeMdView.getOid());
    virtualSupervisorNameMdAttribute.apply();

    virtualSupervisorIdMdAttribute = MdAttributeVirtualDAO.newInstance();
    virtualSupervisorIdMdAttribute.setValue(MdAttributeVirtualInfo.NAME, "supervisorId");
    virtualSupervisorIdMdAttribute.setStructValue(MdAttributeVirtualInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Supervisor Id");
    virtualSupervisorIdMdAttribute.setStructValue(MdAttributeVirtualInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Id of the supervisor");
    virtualSupervisorIdMdAttribute.setValue(MdAttributeVirtualInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    virtualSupervisorIdMdAttribute.setValue(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE, employeeIdMdAttribute.getOid());
    virtualSupervisorIdMdAttribute.setValue(MdAttributeVirtualInfo.DEFINING_MD_VIEW, employeeMdView.getOid());
    virtualSupervisorIdMdAttribute.apply();

    BusinessDAO employee1 = BusinessDAO.newInstance(employeeMdBusiness.definesType());
    employee1.setValue("personName", "Supervisor Bill");
    employee1.setValue("employeeId", "123");
    employee1.apply();

    BusinessDAO employee2 = BusinessDAO.newInstance(employeeMdBusiness.definesType());
    employee2.setValue("personName", "Employee Joe");
    employee2.setValue("employeeId", "456");
    employee2.setValue("supervisor", employee1.getOid());
    employee2.apply();

    BusinessDAO employee3 = BusinessDAO.newInstance(employeeMdBusiness.definesType());
    employee3.setValue("personName", "Employee Sally");
    employee3.setValue("employeeId", "789");
    employee3.setValue("supervisor", employee1.getOid());
    employee3.apply();
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    new MdPackage(pack).delete();
  }

  @Request
  @Test
  public void testDefaultVirtualMetaData()
  {
    String assertErrorMessage = "Metadata on virtual attribute does not match referenced concrete attribute";

    Assert.assertEquals(assertErrorMessage, titleMdAttribute.definesAttribute(), virtualTitleMdAttribute.definesAttribute());
    Assert.assertEquals(assertErrorMessage, titleMdAttribute.getDisplayLabel(CommonProperties.getDefaultLocale()), virtualTitleMdAttribute.getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(assertErrorMessage, titleMdAttribute.getDescription(CommonProperties.getDefaultLocale()), virtualTitleMdAttribute.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(assertErrorMessage, titleMdAttribute.getDefaultValue(), virtualTitleMdAttribute.getDefaultValue());
    Assert.assertEquals(assertErrorMessage, titleMdAttribute.isImmutable(), virtualTitleMdAttribute.isImmutable());
    Assert.assertEquals(assertErrorMessage, titleMdAttribute.isSystem(), virtualTitleMdAttribute.isSystem());
    Assert.assertEquals(assertErrorMessage, titleMdAttribute.isRequired(), virtualTitleMdAttribute.isRequired());
    Assert.assertEquals(assertErrorMessage, titleMdAttribute.getGetterVisibility().getJavaModifier(), virtualTitleMdAttribute.getGetterVisibility().getJavaModifier());
    Assert.assertEquals(assertErrorMessage, titleMdAttribute.getSetterVisibility().getJavaModifier(), virtualTitleMdAttribute.getSetterVisibility().getJavaModifier());
    Assert.assertEquals(assertErrorMessage, titleMdAttribute.javaType(false), virtualTitleMdAttribute.javaType(false));
    Assert.assertEquals(assertErrorMessage, titleMdAttribute.attributeMdDTOType(), virtualTitleMdAttribute.attributeMdDTOType());
    Assert.assertEquals(assertErrorMessage, titleMdAttribute.generatedServerGetter(), virtualTitleMdAttribute.generatedServerGetter());
    Assert.assertEquals(assertErrorMessage, titleMdAttribute.generatedServerSetter(), virtualTitleMdAttribute.generatedServerSetter());

    Assert.assertEquals(assertErrorMessage, authorNameMdAttribute.definesAttribute(), virtualAuthorNameMdAttribute.definesAttribute());
    Assert.assertEquals(assertErrorMessage, authorNameMdAttribute.getDisplayLabel(CommonProperties.getDefaultLocale()), virtualAuthorNameMdAttribute.getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(assertErrorMessage, authorNameMdAttribute.getDescription(CommonProperties.getDefaultLocale()), virtualAuthorNameMdAttribute.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(assertErrorMessage, authorNameMdAttribute.getDefaultValue(), virtualAuthorNameMdAttribute.getDefaultValue());
    Assert.assertEquals(assertErrorMessage, authorNameMdAttribute.isImmutable(), virtualAuthorNameMdAttribute.isImmutable());
    Assert.assertEquals(assertErrorMessage, authorNameMdAttribute.isSystem(), virtualAuthorNameMdAttribute.isSystem());
    Assert.assertEquals(assertErrorMessage, authorNameMdAttribute.isRequired(), virtualAuthorNameMdAttribute.isRequired());
    Assert.assertEquals(assertErrorMessage, authorNameMdAttribute.getGetterVisibility().getJavaModifier(), virtualAuthorNameMdAttribute.getGetterVisibility().getJavaModifier());
    Assert.assertEquals(assertErrorMessage, authorNameMdAttribute.getSetterVisibility().getJavaModifier(), virtualAuthorNameMdAttribute.getSetterVisibility().getJavaModifier());
    Assert.assertEquals(assertErrorMessage, authorNameMdAttribute.javaType(false), virtualAuthorNameMdAttribute.javaType(false));
    Assert.assertEquals(assertErrorMessage, authorNameMdAttribute.attributeMdDTOType(), virtualAuthorNameMdAttribute.attributeMdDTOType());
    Assert.assertEquals(assertErrorMessage, authorNameMdAttribute.generatedServerGetter(), virtualAuthorNameMdAttribute.generatedServerGetter());
    Assert.assertEquals(assertErrorMessage, authorNameMdAttribute.generatedServerSetter(), virtualAuthorNameMdAttribute.generatedServerSetter());

  }

  @Request
  @Test
  public void testOverriddenVirtualMetaData()
  {
    String assertErrorMessage = "Metadata on virtual attribute does not match referenced concrete attribute";

    Assert.assertEquals(assertErrorMessage, personNameMdAttribute.definesAttribute(), virtualPersonNameMdAttribute.definesAttribute());
    Assert.assertEquals(assertErrorMessage, personNameMdAttribute.getDisplayLabel(CommonProperties.getDefaultLocale()), virtualPersonNameMdAttribute.getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(assertErrorMessage, personNameMdAttribute.getDescription(CommonProperties.getDefaultLocale()), virtualPersonNameMdAttribute.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(assertErrorMessage, personNameMdAttribute.getDefaultValue(), virtualPersonNameMdAttribute.getDefaultValue());
    Assert.assertEquals(assertErrorMessage, personNameMdAttribute.isImmutable(), virtualPersonNameMdAttribute.isImmutable());
    Assert.assertEquals(assertErrorMessage, personNameMdAttribute.isSystem(), virtualPersonNameMdAttribute.isSystem());
    Assert.assertEquals(assertErrorMessage, personNameMdAttribute.isRequired(), virtualPersonNameMdAttribute.isRequired());
    Assert.assertEquals(assertErrorMessage, personNameMdAttribute.getGetterVisibility().getJavaModifier(), virtualPersonNameMdAttribute.getGetterVisibility().getJavaModifier());
    Assert.assertEquals(assertErrorMessage, personNameMdAttribute.getSetterVisibility().getJavaModifier(), virtualPersonNameMdAttribute.getSetterVisibility().getJavaModifier());
    Assert.assertEquals(assertErrorMessage, personNameMdAttribute.javaType(false), virtualPersonNameMdAttribute.javaType(false));
    Assert.assertEquals(assertErrorMessage, personNameMdAttribute.attributeMdDTOType(), virtualPersonNameMdAttribute.attributeMdDTOType());
    Assert.assertEquals(assertErrorMessage, personNameMdAttribute.generatedServerGetter(), virtualPersonNameMdAttribute.generatedServerGetter());
    Assert.assertEquals(assertErrorMessage, personNameMdAttribute.generatedServerSetter(), virtualPersonNameMdAttribute.generatedServerSetter());
    Assert.assertEquals(assertErrorMessage, employeeIdMdAttribute.getGetterVisibility().getJavaModifier(), virtualSupervisorIdMdAttribute.getGetterVisibility().getJavaModifier());
    Assert.assertEquals(assertErrorMessage, employeeIdMdAttribute.getSetterVisibility().getJavaModifier(), virtualSupervisorIdMdAttribute.getSetterVisibility().getJavaModifier());
    Assert.assertEquals(assertErrorMessage, employeeIdMdAttribute.javaType(false), virtualEmployeeIdMdAttribute.javaType(false));
    Assert.assertEquals(assertErrorMessage, employeeIdMdAttribute.attributeMdDTOType(), virtualEmployeeIdMdAttribute.attributeMdDTOType());

    Assert.assertEquals(assertErrorMessage, "supervisorName", virtualSupervisorNameMdAttribute.definesAttribute());
    Assert.assertEquals(assertErrorMessage, "Supervisor Name", virtualSupervisorNameMdAttribute.getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(assertErrorMessage, "Supervisor Name Attribute", virtualSupervisorNameMdAttribute.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(assertErrorMessage, personNameMdAttribute.getDefaultValue(), virtualSupervisorNameMdAttribute.getDefaultValue());
    Assert.assertEquals(assertErrorMessage, personNameMdAttribute.isImmutable(), virtualSupervisorNameMdAttribute.isImmutable());
    Assert.assertEquals(assertErrorMessage, personNameMdAttribute.isSystem(), virtualSupervisorNameMdAttribute.isSystem());
    Assert.assertEquals(assertErrorMessage, false, virtualSupervisorNameMdAttribute.isRequired());
    Assert.assertEquals(assertErrorMessage, personNameMdAttribute.getGetterVisibility().getJavaModifier(), virtualSupervisorNameMdAttribute.getGetterVisibility().getJavaModifier());
    Assert.assertEquals(assertErrorMessage, personNameMdAttribute.getSetterVisibility().getJavaModifier(), virtualSupervisorNameMdAttribute.getSetterVisibility().getJavaModifier());
    Assert.assertEquals(assertErrorMessage, personNameMdAttribute.javaType(false), virtualSupervisorNameMdAttribute.javaType(false));
    Assert.assertEquals(assertErrorMessage, personNameMdAttribute.attributeMdDTOType(), virtualSupervisorNameMdAttribute.attributeMdDTOType());
    Assert.assertEquals(assertErrorMessage, "getValue(SUPERVISORNAME)", virtualSupervisorNameMdAttribute.generatedServerGetter());
    Assert.assertEquals(assertErrorMessage, "setValue(SUPERVISORNAME, value)", virtualSupervisorNameMdAttribute.generatedServerSetter());

    Assert.assertEquals(assertErrorMessage, "supervisorId", virtualSupervisorIdMdAttribute.definesAttribute());
    Assert.assertEquals(assertErrorMessage, "Supervisor Id", virtualSupervisorIdMdAttribute.getDisplayLabel(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(assertErrorMessage, "Id of the supervisor", virtualSupervisorIdMdAttribute.getDescription(CommonProperties.getDefaultLocale()));
    Assert.assertEquals(assertErrorMessage, employeeIdMdAttribute.isImmutable(), virtualSupervisorIdMdAttribute.isImmutable());
    Assert.assertEquals(assertErrorMessage, employeeIdMdAttribute.isSystem(), virtualSupervisorIdMdAttribute.isSystem());
    Assert.assertEquals(assertErrorMessage, false, virtualSupervisorIdMdAttribute.isRequired());
    Assert.assertEquals(assertErrorMessage, employeeIdMdAttribute.getGetterVisibility().getJavaModifier(), virtualSupervisorIdMdAttribute.getGetterVisibility().getJavaModifier());
    Assert.assertEquals(assertErrorMessage, employeeIdMdAttribute.getSetterVisibility().getJavaModifier(), virtualSupervisorIdMdAttribute.getSetterVisibility().getJavaModifier());
    Assert.assertEquals(assertErrorMessage, employeeIdMdAttribute.javaType(false), virtualSupervisorIdMdAttribute.javaType(false));
    Assert.assertEquals(assertErrorMessage, employeeIdMdAttribute.attributeMdDTOType(), virtualSupervisorIdMdAttribute.attributeMdDTOType());
    Assert.assertEquals(assertErrorMessage, com.runwaysdk.constants.MdAttributeIntegerUtil.class.getName() + ".getTypeSafeValue(getValue(SUPERVISORID))", virtualSupervisorIdMdAttribute.generatedServerGetter());
    Assert.assertEquals(assertErrorMessage, "setValue(SUPERVISORID, " + java.lang.Integer.class.getName() + ".toString(value))", virtualSupervisorIdMdAttribute.generatedServerSetter());

  }

  @Request
  @Test
  public void testDefaultAttributes() throws Exception
  {
    Class<?> viewClass = LoaderDecorator.load(bookMdView.definesType());

    View newBookView = (View) viewClass.getConstructor().newInstance();

    Assert.assertTrue("BookView does not have a \"title\" attribute", newBookView.hasAttribute("title"));

    try
    {
      viewClass.getMethod("getTitle").invoke(newBookView);
    }
    catch (NoSuchMethodException e)
    {
      Assert.fail("Accessor method does not exist on View class.");
    }

    Assert.assertTrue("BookView does not have an \"authorName\" attribute", newBookView.hasAttribute("authorName"));

    try
    {
      viewClass.getMethod("getAuthorName").invoke(newBookView);
    }
    catch (NoSuchMethodException e)
    {
      Assert.fail("Accessor method does not exist on View class.");
    }
  }

  @Request
  @Test
  public void testOverridenAttributes() throws Exception
  {
    Class<?> viewClass = LoaderDecorator.load(employeeMdView.definesType());

    View newBookView = (View) viewClass.getConstructor().newInstance();

    Assert.assertTrue("EmployeeView does not have an \"personName\" attribute", newBookView.hasAttribute("personName"));
    try
    {
      viewClass.getMethod("getPersonName").invoke(newBookView);
    }
    catch (NoSuchMethodException e)
    {
      Assert.fail("Accessor method does not exist on View class.");
    }

    Assert.assertTrue("EmployeeView does not have an \"employeeId\" attribute", newBookView.hasAttribute("empId"));
    try
    {
      viewClass.getMethod("getEmpId").invoke(newBookView);
    }
    catch (NoSuchMethodException e)
    {
      Assert.fail("Accessor method does not exist on View class.");
    }

    Assert.assertTrue("EmployeeView does not have a \"supervisorName\" attribute", newBookView.hasAttribute("supervisorName"));
    try
    {
      viewClass.getMethod("getSupervisorName").invoke(newBookView);
    }
    catch (NoSuchMethodException e)
    {
      Assert.fail("Accessor method does not exist on View class.");
    }

    Assert.assertTrue("EmployeeView does not have a \"supervisorId\" attribute", newBookView.hasAttribute("supervisorId"));
    try
    {
      viewClass.getMethod("getSupervisorId").invoke(newBookView);
    }
    catch (NoSuchMethodException e)
    {
      Assert.fail("Accessor method does not exist on View class.");
    }

  }
}
