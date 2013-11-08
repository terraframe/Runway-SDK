/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.io;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.EntityDTO;
import com.runwaysdk.business.ontology.MdTermDAO;
import com.runwaysdk.business.ontology.MdTermRelationshipDAO;
import com.runwaysdk.business.rbac.MethodActorDAO;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.state.MdStateMachineDAO;
import com.runwaysdk.business.state.StateMasterDAO;
import com.runwaysdk.constants.AssociationType;
import com.runwaysdk.constants.CharacterConditionInfo;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.CompositeFieldConditionInfo;
import com.runwaysdk.constants.DoubleConditionInfo;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.EntityTypes;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.HashMethods;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdActionInfo;
import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
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
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeSymmetricInfo;
import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.MdAttributeVirtualInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdControllerInfo;
import com.runwaysdk.constants.MdDimensionInfo;
import com.runwaysdk.constants.MdDomainInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdExceptionInfo;
import com.runwaysdk.constants.MdIndexInfo;
import com.runwaysdk.constants.MdInformationInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdParameterInfo;
import com.runwaysdk.constants.MdProblemInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdStateMachineInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.constants.MdTermRelationshipInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.constants.MdUtilInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.MdWarningInfo;
import com.runwaysdk.constants.MdWebBooleanInfo;
import com.runwaysdk.constants.MdWebCharacterInfo;
import com.runwaysdk.constants.MdWebDateInfo;
import com.runwaysdk.constants.MdWebDoubleInfo;
import com.runwaysdk.constants.MdWebFloatInfo;
import com.runwaysdk.constants.MdWebFormInfo;
import com.runwaysdk.constants.MdWebGroupInfo;
import com.runwaysdk.constants.MdWebIntegerInfo;
import com.runwaysdk.constants.MdWebLongInfo;
import com.runwaysdk.constants.MdWebReferenceInfo;
import com.runwaysdk.constants.MdWebSingleTermGridInfo;
import com.runwaysdk.constants.MethodActorInfo;
import com.runwaysdk.constants.SymmetricMethods;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.metadata.AndFieldConditionDAO;
import com.runwaysdk.dataaccess.metadata.CharacterConditionDAO;
import com.runwaysdk.dataaccess.metadata.DoubleConditionDAO;
import com.runwaysdk.dataaccess.metadata.MdActionDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeBlobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeClobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDecimalDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFileDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeHashDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeStructDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeSymmetricDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTermDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeVirtualDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdControllerDAO;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdDomainDAO;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdExceptionDAO;
import com.runwaysdk.dataaccess.metadata.MdFieldDAO;
import com.runwaysdk.dataaccess.metadata.MdIndexDAO;
import com.runwaysdk.dataaccess.metadata.MdInformationDAO;
import com.runwaysdk.dataaccess.metadata.MdLocalStructDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.dataaccess.metadata.MdProblemDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.MdTreeDAO;
import com.runwaysdk.dataaccess.metadata.MdUtilDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.dataaccess.metadata.MdWarningDAO;
import com.runwaysdk.dataaccess.metadata.MdWebBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdWebCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdWebDateDAO;
import com.runwaysdk.dataaccess.metadata.MdWebDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFormDAO;
import com.runwaysdk.dataaccess.metadata.MdWebGroupDAO;
import com.runwaysdk.dataaccess.metadata.MdWebIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdWebLongDAO;
import com.runwaysdk.dataaccess.metadata.MdWebReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdWebSingleTermGridDAO;
import com.runwaysdk.dataaccess.metadata.MetadataDAO;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAO;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAOIF;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.vault.VaultDAO;

/**
 * Defines some utility methods to build test fixtures for the test cases.
 * 
 * @author RunwaySDK
 * 
 */
public class TestFixtureFactory
{

  public static class Constants
  {
    public static final String TEST_LOCAL_STRUCT_NAME   = "LocalStruct";

    public static final String TEST_STRUCT1             = "Struct1";

    public static final String TEST_PACKAGE             = "test.xmlclasses";

    public static final String TEST_CLASS1              = "Class1";

    public static final String TEST_SESSION_LIMIT       = "10";

    public static final String TEST_PASSWORD            = "blah";

    public static final String TEST_USER                = "testUser";

    public static final String TEST_ROLE2_DISPLAY_LABEL = "Test Role 2";

    public static final String TEST_ROLE2_NAME          = "runway.testRole2";

    public static final String TEST_ROLE1_DISPLAY_LABEL = "Test Role";

    public static final String TEST_ROLE1_NAME          = "runway.testRole";

    public static final String TEST_METHOD_RETURN_TYPE  = "void";

    public static String       TEST_METHOD_NAME         = "checkin";

    public static final String ATTRIBUTE_NAME           = "testCharacter";

    public static final String ATTRIBUTE_SIZE           = "200";

    public static final String ATTRIBUTE_DEFAULT_LOCALE = "Character Set Test";

    public static final String TEST_CLASS2              = "Class2";

    public static final String METHOD_NAME              = "testMethod";

    public static final String METHOD_DEFAULT_LOCALE    = "Test Method";

  }

  public static MethodActorDAO createMethodActor(MdMethodDAO mdMethod)
  {
    MethodActorDAO methodActor = MethodActorDAO.newInstance();
    methodActor.setValue(MethodActorInfo.MD_METHOD, mdMethod.getId());

    return methodActor;
  }

  public static MdMethodDAO createMdMethod(MdClassDAO mdClass)
  {
    return TestFixtureFactory.createMdMethod(mdClass, Constants.TEST_METHOD_NAME);
  }

  public static MdMethodDAO createMdMethod(MdClassDAO mdClass, String methodName)
  {
    MdMethodDAO mdMethod = MdMethodDAO.newInstance();

    mdMethod.setValue(MdMethodInfo.NAME, methodName);
    mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdClass.getId());
    mdMethod.setValue(MdMethodInfo.RETURN_TYPE, Constants.TEST_METHOD_RETURN_TYPE);
    mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, methodName);
    mdMethod.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, methodName);
    mdMethod.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);

    return mdMethod;
  }

  public static RoleDAO createRole1()
  {
    RoleDAO role = RoleDAO.newInstance();
    role.setRoleName(Constants.TEST_ROLE1_NAME);
    role.setDefaultDisplayLabel(Constants.TEST_ROLE1_DISPLAY_LABEL);

    return role;
  }

  public static RoleDAO createRole2()
  {
    RoleDAO role = RoleDAO.newInstance();
    role.setRoleName(Constants.TEST_ROLE2_NAME);
    role.setDefaultDisplayLabel(Constants.TEST_ROLE2_DISPLAY_LABEL);

    return role;
  }

  public static UserDAO createUser()
  {
    return TestFixtureFactory.createUser(Constants.TEST_USER);
  }

  public static UserDAO createUser(String username)
  {
    UserDAO user = UserDAO.newInstance();
    user.setUsername(username);
    user.setPassword(Constants.TEST_PASSWORD);
    user.setSessionLimit(Constants.TEST_SESSION_LIMIT);
    user.setInactive(false);

    return user;
  }

  public static MdBusinessDAO createMdBusiness1()
  {
    MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.NAME, Constants.TEST_CLASS1);
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, Constants.TEST_PACKAGE);
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "mdBusiness Set Test");
    mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Set mdBusiness Attributes Test");

    return mdBusiness;
  }

  public static MdBusinessDAO createMdBusiness2(MdBusinessDAO superMdBusiness)
  {
    MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.NAME, Constants.TEST_CLASS2);
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, Constants.TEST_PACKAGE);
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Class2 Set Test");
    mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");

    if (superMdBusiness != null)
    {
      mdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, superMdBusiness.getId());
    }

    return mdBusiness;
  }

  public static MdBusinessDAO createMdBusiness2()
  {
    return TestFixtureFactory.createMdBusiness2(null);
  }

  public static MdStructDAO createMdStruct1()
  {
    MdStructDAO mdStruct = MdStructDAO.newInstance();
    mdStruct.setValue(MdStructInfo.NAME, Constants.TEST_STRUCT1);
    mdStruct.setValue(MdStructInfo.PACKAGE, Constants.TEST_PACKAGE);
    mdStruct.setStructValue(MdStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "mdStruct Set Test");
    mdStruct.setStructValue(MdStructInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Set mdStruct Attributes Test");

    return mdStruct;
  }

  public static MdLocalStructDAO createMdLocalStruct()
  {
    return TestFixtureFactory.createMdLocalStruct(Constants.TEST_LOCAL_STRUCT_NAME);
  }

  public static MdLocalStructDAO createMdLocalStruct(String name)
  {
    MdLocalStructDAO mdLocalStruct = MdLocalStructDAO.newInstance();
    mdLocalStruct.setValue(MdStructInfo.NAME, name);
    mdLocalStruct.setValue(MdStructInfo.PACKAGE, Constants.TEST_PACKAGE);
    mdLocalStruct.setStructValue(MdStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "mdLocalStruct Set Test");
    mdLocalStruct.setStructValue(MdStructInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Set mdLocalStruct Attributes Test");

    return mdLocalStruct;
  }

  public static MdExceptionDAO createMdException1()
  {
    MdExceptionDAO mdException = MdExceptionDAO.newInstance();
    mdException.setValue(MdExceptionInfo.NAME, "Exception1");
    mdException.setValue(MdExceptionInfo.PACKAGE, Constants.TEST_PACKAGE);
    mdException.setStructValue(MdExceptionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "mdException Set Test");
    mdException.setStructValue(MdExceptionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Set mdException Attributes Test");

    return mdException;
  }

  public static MdExceptionDAO createMdException2()
  {
    MdExceptionDAO mdException = MdExceptionDAO.newInstance();
    mdException.setValue(MdExceptionInfo.NAME, "Exception2");
    mdException.setValue(MdExceptionInfo.PACKAGE, Constants.TEST_PACKAGE);
    mdException.setStructValue(MdExceptionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "mdException2 Set Test");
    mdException.setStructValue(MdExceptionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");

    return mdException;
  }

  public static MdProblemDAO createMdProblem1()
  {
    MdProblemDAO mdProblem = MdProblemDAO.newInstance();
    mdProblem.setValue(MdProblemInfo.NAME, "Exception1");
    mdProblem.setValue(MdProblemInfo.PACKAGE, Constants.TEST_PACKAGE);
    mdProblem.setStructValue(MdProblemInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "mdProblem Set Test");
    mdProblem.setStructValue(MdProblemInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Set mdProblem Attributes Test");

    return mdProblem;
  }

  public static MdProblemDAO createMdProblem2()
  {
    MdProblemDAO mdProblem = MdProblemDAO.newInstance();
    mdProblem.setValue(MdProblemInfo.NAME, "Exception2");
    mdProblem.setValue(MdProblemInfo.PACKAGE, Constants.TEST_PACKAGE);
    mdProblem.setStructValue(MdProblemInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "mdProblem2 Set Test");
    mdProblem.setStructValue(MdProblemInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");

    return mdProblem;
  }

  public static MdInformationDAO createMdInformation1()
  {
    MdInformationDAO mdInformation = MdInformationDAO.newInstance();
    mdInformation.setValue(MdInformationInfo.NAME, "Information1");
    mdInformation.setValue(MdInformationInfo.PACKAGE, Constants.TEST_PACKAGE);
    mdInformation.setStructValue(MdInformationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "mdInformation Set Test");
    mdInformation.setStructValue(MdInformationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Set mdInformation Attributes Test");

    return mdInformation;
  }

  public static MdIndexDAO createMdIndex(MdBusinessDAO mdBusiness)
  {
    MdIndexDAO mdIndex = MdIndexDAO.newInstance();
    mdIndex.setValue(MdIndexInfo.MD_ENTITY, mdBusiness.getId());
    mdIndex.setValue(MdIndexInfo.UNIQUE, MdAttributeBooleanInfo.TRUE);
    mdIndex.setStructValue(MdIndexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Index");
    return mdIndex;

  }

  public static MdInformationDAO createMdInformation2()
  {
    MdInformationDAO mdInformation = MdInformationDAO.newInstance();
    mdInformation.setValue(MdInformationInfo.NAME, "Information2");
    mdInformation.setValue(MdInformationInfo.PACKAGE, Constants.TEST_PACKAGE);
    mdInformation.setStructValue(MdInformationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "mdInformation2 Set Test");
    mdInformation.setStructValue(MdInformationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");

    return mdInformation;
  }

  public static MdViewDAO createMdView1()
  {
    MdViewDAO mdView = MdViewDAO.newInstance();
    mdView.setValue(MdViewInfo.NAME, "View1");
    mdView.setValue(MdViewInfo.PACKAGE, Constants.TEST_PACKAGE);
    mdView.setStructValue(MdViewInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "mdException Set Test");
    mdView.setStructValue(MdViewInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Set mdException Attributes Test");

    return mdView;
  }

  public static MdViewDAO createMdView2()
  {
    MdViewDAO mdView = MdViewDAO.newInstance();
    mdView.setValue(MdViewInfo.NAME, "View2");
    mdView.setValue(MdViewInfo.PACKAGE, Constants.TEST_PACKAGE);
    mdView.setStructValue(MdViewInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "mdException2 Set Test");
    mdView.setStructValue(MdViewInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");

    return mdView;
  }

  public static MdUtilDAO createMdUtil1()
  {
    MdUtilDAO mdUtil = MdUtilDAO.newInstance();
    mdUtil.setValue(MdUtilInfo.NAME, "Util1");
    mdUtil.setValue(MdUtilInfo.PACKAGE, Constants.TEST_PACKAGE);
    mdUtil.setStructValue(MdUtilInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "mdException Set Test");
    mdUtil.setStructValue(MdUtilInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Set mdException Attributes Test");

    return mdUtil;
  }

  public static MdUtilDAO createMdUtil2()
  {
    MdUtilDAO mdUtil = MdUtilDAO.newInstance();
    mdUtil.setValue(MdUtilInfo.NAME, "Util2");
    mdUtil.setValue(MdUtilInfo.PACKAGE, Constants.TEST_PACKAGE);
    mdUtil.setStructValue(MdUtilInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "mdException2 Set Test");
    mdUtil.setStructValue(MdUtilInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");

    return mdUtil;
  }

  public static MdBusinessDAO createEnumClass1()
  {
    MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.NAME, "EnumClassTest");
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, Constants.TEST_PACKAGE);
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Enumeration mdBusiness Test");
    mdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, EnumerationMasterInfo.ID_VALUE);

    return mdBusiness;
  }

  public static MdEnumerationDAO createMdEnumeation1(MdBusinessDAO mdBusiness)
  {
    MdEnumerationDAO mdEnumeration = MdEnumerationDAO.newInstance();
    mdEnumeration.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Enumeratin Filter Test");
    mdEnumeration.setValue(MdEnumerationInfo.NAME, "Filter1");
    mdEnumeration.setValue(MdEnumerationInfo.PACKAGE, Constants.TEST_PACKAGE);
    mdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, mdBusiness.getId());
    mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);

    return mdEnumeration;
  }

  public static MdDimensionDAO createMdDimension()
  {
    return TestFixtureFactory.createMdDimension("D1");
  }

  public static MdDimensionDAO createMdDimension(String name)
  {
    MdDimensionDAO mdDimension = MdDimensionDAO.newInstance();
    mdDimension.setValue(MdDimensionInfo.NAME, name);
    mdDimension.setStructValue(MdDimensionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Dimension");

    return mdDimension;
  }

  public static MdRelationshipDAO createMdRelationship1(MdBusinessDAO parent, MdBusinessDAO child)
  {
    MdRelationshipDAO mdRelationship = MdRelationshipDAO.newInstance();
    mdRelationship.setValue(MdRelationshipInfo.NAME, "Relationship1");
    mdRelationship.setValue(MdRelationshipInfo.PACKAGE, Constants.TEST_PACKAGE);
    mdRelationship.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "mdRelationship Set Test");
    mdRelationship.setStructValue(MdRelationshipInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "mdRelationship Test");
    mdRelationship.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
    mdRelationship.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent Set Test");
    mdRelationship.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, parent.getId());
    mdRelationship.setValue(MdRelationshipInfo.PARENT_METHOD, Constants.TEST_CLASS2);
    mdRelationship.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "*");
    mdRelationship.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child Set Test");
    mdRelationship.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, child.getId());
    mdRelationship.setValue(MdRelationshipInfo.CHILD_METHOD, Constants.TEST_CLASS1);

    return mdRelationship;
  }

  public static MdAttributeBooleanDAO addBooleanAttribute(MdClassDAO mdEntity, String attributeName)
  {
    MdAttributeBooleanDAO mdAttribute = MdAttributeBooleanDAO.newInstance();
    mdAttribute.setValue(MdAttributeBooleanInfo.NAME, attributeName);
    mdAttribute.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Boolean Set Test");
    mdAttribute.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    mdAttribute.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    mdAttribute.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeBooleanDAO addBooleanAttribute(MdClassDAO mdEntity)
  {
    return addBooleanAttribute(mdEntity, "testBoolean");
  }

  public static MdAttributeCharacterDAO addCharacterAttribute(MdClassDAO mdEntity)
  {
    return TestFixtureFactory.addCharacterAttribute(mdEntity, "testCharacter");
  }

  public static MdAttributeCharacterDAO addCharacterAttribute(MdClassDAO mdEntity, String attributeName)
  {
    MdAttributeCharacterDAO mdAttribute = MdAttributeCharacterDAO.newInstance();
    mdAttribute.setValue(MdAttributeCharacterInfo.NAME, attributeName);
    mdAttribute.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, Constants.ATTRIBUTE_DEFAULT_LOCALE);
    mdAttribute.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.NO_INDEX.getId());
    mdAttribute.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttribute.setValue(MdAttributeCharacterInfo.SIZE, Constants.ATTRIBUTE_SIZE);
    mdAttribute.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeLocalCharacterDAO addLocalCharacterAttribute(MdEntityDAO mdEntity, MdLocalStructDAO mdLocalStruct)
  {
    return TestFixtureFactory.addLocalCharacterAttribute(mdEntity, mdLocalStruct, "testLocalCharacter");
  }

  public static MdAttributeLocalCharacterDAO addLocalCharacterAttribute(MdEntityDAO mdEntity, MdLocalStructDAO mdLocalStruct, String attributeName)
  {
    MdAttributeLocalCharacterDAO mdAttribute = MdAttributeLocalCharacterDAO.newInstance();
    mdAttribute.setValue(MdAttributeStructInfo.NAME, attributeName);
    mdAttribute.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Local Character Test");
    mdAttribute.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, mdEntity.getId());
    mdAttribute.setValue(MdAttributeStructInfo.MD_STRUCT, mdLocalStruct.getId());

    return mdAttribute;
  }

  public static MdAttributeLocalCharacterDAO addLocalCharacterAttribute(MdEntityDAO mdEntity)
  {
    return TestFixtureFactory.addLocalCharacterAttribute(mdEntity, "testLocalCharacter");
  }

  public static MdAttributeLocalCharacterDAO addLocalCharacterAttribute(MdEntityDAO mdEntity, String attributeName)
  {
    MdAttributeLocalCharacterDAO mdAttribute = MdAttributeLocalCharacterDAO.newInstance();
    mdAttribute.setValue(MdAttributeStructInfo.NAME, attributeName);
    mdAttribute.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Local Character Test");
    mdAttribute.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeLocalTextDAO addLocalTextAttribute(MdEntityDAO mdEntity)
  {
    MdAttributeLocalTextDAO mdAttribute = MdAttributeLocalTextDAO.newInstance();
    mdAttribute.setValue(MdAttributeStructInfo.NAME, "testLocalText");
    mdAttribute.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Local Text Test");
    mdAttribute.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeLocalTextDAO addLocalTextAttribute(MdEntityDAO mdEntity, MdLocalStructDAO mdLocalStruct)
  {
    MdAttributeLocalTextDAO mdAttribute = MdAttributeLocalTextDAO.newInstance();
    mdAttribute.setValue(MdAttributeStructInfo.NAME, "testLocalText");
    mdAttribute.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Local Text Test");
    mdAttribute.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, mdEntity.getId());
    mdAttribute.setValue(MdAttributeStructInfo.MD_STRUCT, mdLocalStruct.getId());

    return mdAttribute;
  }

  public static MdAttributeEnumerationDAO addEnumerationAttribute(MdEntityDAOIF mdEntity, MdEnumerationDAOIF mdEnumeration)
  {
    MdAttributeEnumerationDAO mdAttribute = MdAttributeEnumerationDAO.newInstance();
    mdAttribute.setValue(MdAttributeEnumerationInfo.NAME, "testEnumeration");
    mdAttribute.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Enumeration Set Test");
    mdAttribute.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
    mdAttribute.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, mdEnumeration.getId());
    mdAttribute.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeBlobDAO addBlobAttribute(MdEntityDAO mdEntity)
  {
    MdAttributeBlobDAO mdAttribute = MdAttributeBlobDAO.newInstance();
    mdAttribute.setValue(MdAttributeBlobInfo.NAME, "testBlob");
    mdAttribute.setStructValue(MdAttributeBlobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Blob Set Test");
    mdAttribute.setValue(MdAttributeBlobInfo.INDEX_TYPE, IndexTypes.NO_INDEX.getId());
    mdAttribute.setValue(MdAttributeBlobInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttribute.setValue(MdAttributeBlobInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttribute.setValue(MdAttributeBlobInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttribute.setStructValue(MdAttributeBlobInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Blob Test");
    mdAttribute.setValue(MdAttributeBlobInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeDimensionDAO addDimensionAttribute(MdDimensionDAO mdDimension, MdAttributeDAO mdAttribute)
  {
    MdAttributeDimensionDAO mdAttributeDimension = MdAttributeDimensionDAO.newInstance();
    mdAttributeDimension.setValue(MdAttributeDimensionInfo.DEFINING_MD_ATTRIBUTE, mdAttribute.getId());
    mdAttributeDimension.setValue(MdAttributeDimensionInfo.DEFINING_MD_DIMENSION, mdDimension.getId());

    return mdAttributeDimension;
  }

  public static MdAttributeFileDAO addFileAttribute(MdEntityDAO mdEntity)
  {
    MdAttributeFileDAO mdAttribute = MdAttributeFileDAO.newInstance();
    mdAttribute.setValue(MdAttributeFileInfo.NAME, "testFile");
    mdAttribute.setStructValue(MdAttributeFileInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "File Set Test");
    mdAttribute.setValue(MdAttributeFileInfo.INDEX_TYPE, IndexTypes.NO_INDEX.getId());
    mdAttribute.setValue(MdAttributeFileInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttribute.setValue(MdAttributeFileInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttribute.setValue(MdAttributeFileInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttribute.setStructValue(MdAttributeFileInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "File Test");
    mdAttribute.setValue(MdAttributeFileInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeDateDAO addDateAttribute(MdEntityDAO mdEntity)
  {
    return TestFixtureFactory.addDateAttribute(mdEntity, "testDate");
  }

  public static MdAttributeDateDAO addDateAttribute(MdEntityDAO mdEntity, IndexTypes indexType)
  {
    return TestFixtureFactory.addDateAttribute(mdEntity, "testDate", indexType);
  }

  public static MdAttributeDateDAO addDateAttribute(MdEntityDAO mdEntity, String attributeName)
  {
    return TestFixtureFactory.addDateAttribute(mdEntity, attributeName, IndexTypes.UNIQUE_INDEX);
  }

  public static MdAttributeDateDAO addDateAttribute(MdEntityDAO mdEntity, String attributeName, IndexTypes indexType)
  {
    MdAttributeDateDAO mdAttribute = MdAttributeDateDAO.newInstance();
    mdAttribute.setValue(MdAttributeDateInfo.NAME, attributeName);
    mdAttribute.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Date Set Test");
    mdAttribute.setValue(MdAttributeDateInfo.INDEX_TYPE, indexType.getId());
    mdAttribute.setValue(MdAttributeDateInfo.DEFAULT_VALUE, "2006-02-11");
    mdAttribute.setValue(MdAttributeDateInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttribute.setStructValue(MdAttributeDateInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Date Test");
    mdAttribute.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeDateTimeDAO addDateTimeAttribute(MdClassDAO mdClass)
  {
    MdAttributeDateTimeDAO mdAttribute = MdAttributeDateTimeDAO.newInstance();
    mdAttribute.setValue(MdAttributeDateTimeInfo.NAME, "testDateTime");
    mdAttribute.setStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "dateTime Set Test");
    mdAttribute.setValue(MdAttributeDateTimeInfo.INDEX_TYPE, IndexTypes.NON_UNIQUE_INDEX.getId());
    mdAttribute.setValue(MdAttributeDateTimeInfo.DEFINING_MD_CLASS, mdClass.getId());

    return mdAttribute;
  }

  public static MdAttributeDecimalDAO addDecimalAttribute(MdEntityDAO mdEntity)
  {
    return TestFixtureFactory.addDecimalAttribute(mdEntity, "testDecimal");
  }

  public static MdAttributeDecimalDAO addDecimalAttribute(MdEntityDAO mdEntity, String attributeName)
  {
    MdAttributeDecimalDAO mdAttribute = MdAttributeDecimalDAO.newInstance();
    mdAttribute.setValue(MdAttributeDecimalInfo.NAME, attributeName);
    mdAttribute.setStructValue(MdAttributeDecimalInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Decimal Set Test");
    mdAttribute.setValue(MdAttributeDecimalInfo.LENGTH, Constants.TEST_SESSION_LIMIT);
    mdAttribute.setValue(MdAttributeDecimalInfo.DECIMAL, "2");
    mdAttribute.setValue(MdAttributeDecimalInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttribute.setValue(MdAttributeDecimalInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeDoubleDAO addDoubleAttribute(MdEntityDAO mdEntity)
  {
    return TestFixtureFactory.addDoubleAttribute(mdEntity, "testDouble");
  }

  public static MdAttributeDoubleDAO addDoubleAttribute(MdEntityDAO mdEntity, String attributeName)
  {
    MdAttributeDoubleDAO mdAttribute = MdAttributeDoubleDAO.newInstance();
    mdAttribute.setValue(MdAttributeDoubleInfo.NAME, attributeName);
    mdAttribute.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Double Set Test");
    mdAttribute.setValue(MdAttributeDoubleInfo.LENGTH, "9");
    mdAttribute.setValue(MdAttributeDoubleInfo.DECIMAL, "4");
    mdAttribute.setValue(MdAttributeDoubleInfo.REJECT_ZERO, MdAttributeBooleanInfo.TRUE);
    mdAttribute.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeFloatDAO addFloatAttribute(MdEntityDAO mdEntity)
  {
    return TestFixtureFactory.addFloatAttribute(mdEntity, "testFloat");
  }

  private static MdAttributeFloatDAO addFloatAttribute(MdEntityDAO mdEntity, String attributeName)
  {
    MdAttributeFloatDAO mdAttribute = MdAttributeFloatDAO.newInstance();
    mdAttribute.setValue(MdAttributeFloatInfo.NAME, attributeName);
    mdAttribute.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Float Set Test");
    mdAttribute.setValue(MdAttributeFloatInfo.LENGTH, "10");
    mdAttribute.setValue(MdAttributeFloatInfo.DECIMAL, "2");
    mdAttribute.setValue(MdAttributeFloatInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttribute.setValue(MdAttributeFloatInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.TRUE);
    mdAttribute.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeReferenceDAO addReferenceAttribute(MdEntityDAO mdEntity, MdEntityDAO referenceEntity)
  {
    MdAttributeReferenceDAO mdAttribute = MdAttributeReferenceDAO.newInstance();
    mdAttribute.setValue(MdAttributeReferenceInfo.NAME, "testReference");
    mdAttribute.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference Test");
    mdAttribute.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, referenceEntity.getId());
    mdAttribute.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeTermDAO addTermAttribute(MdEntityDAO mdEntity, MdTermDAO termEntity)
  {
    MdAttributeTermDAO mdAttribute = MdAttributeTermDAO.newInstance();
    mdAttribute.setValue(MdAttributeTermInfo.NAME, "testTerm");
    mdAttribute.setStructValue(MdAttributeTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Test");
    mdAttribute.setValue(MdAttributeTermInfo.REF_MD_ENTITY, termEntity.getId());
    mdAttribute.setValue(MdAttributeTermInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeReferenceDAO addReferenceAttribute2(MdEntityDAO mdEntity, MdEntityDAO referenceEntity)
  {
    MdAttributeReferenceDAO mdAttribute = MdAttributeReferenceDAO.newInstance();
    mdAttribute.setValue(MdAttributeReferenceInfo.NAME, "testReference2");
    mdAttribute.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference2 Test");
    mdAttribute.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, referenceEntity.getId());
    mdAttribute.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeReferenceDAO addReferenceAttribute3(MdEntityDAO mdEntity, MdEntityDAO referenceEntity)
  {
    MdAttributeReferenceDAO mdAttribute = MdAttributeReferenceDAO.newInstance();
    mdAttribute.setValue(MdAttributeReferenceInfo.NAME, "testReference3");
    mdAttribute.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference3 Test");
    mdAttribute.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, referenceEntity.getId());
    mdAttribute.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeReferenceDAO addReferenceAttribute4(MdEntityDAO mdEntity, MdEntityDAO referenceEntity)
  {
    MdAttributeReferenceDAO mdAttribute = MdAttributeReferenceDAO.newInstance();
    mdAttribute.setValue(MdAttributeReferenceInfo.NAME, "testReference4");
    mdAttribute.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference4 Test");
    mdAttribute.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, referenceEntity.getId());
    mdAttribute.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeReferenceDAO addReferenceAttribute5(MdEntityDAO mdEntity, MdEntityDAO referenceEntity)
  {
    MdAttributeReferenceDAO mdAttribute = MdAttributeReferenceDAO.newInstance();
    mdAttribute.setValue(MdAttributeReferenceInfo.NAME, "testReference5");
    mdAttribute.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference5 Test");
    mdAttribute.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, referenceEntity.getId());
    mdAttribute.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeReferenceDAO addReferenceAttribute6(MdEntityDAO mdEntity, MdEntityDAO referenceEntity)
  {
    MdAttributeReferenceDAO mdAttribute = MdAttributeReferenceDAO.newInstance();
    mdAttribute.setValue(MdAttributeReferenceInfo.NAME, "testReference6");
    mdAttribute.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference6 Test");
    mdAttribute.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, referenceEntity.getId());
    mdAttribute.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeIntegerDAO addIntegerAttribute(MdClassDAO mdEntity)
  {
    return TestFixtureFactory.addIntegerAttribute(mdEntity, "testInteger");
  }

  public static MdAttributeIntegerDAO addIntegerAttribute(MdClassDAO mdEntity, String attributeName)
  {
    MdAttributeIntegerDAO mdAttribute = MdAttributeIntegerDAO.newInstance();
    mdAttribute.setValue(MdAttributeIntegerInfo.NAME, attributeName);
    mdAttribute.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Integer Set Test");
    mdAttribute.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.TRUE);
    mdAttribute.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.TRUE);
    mdAttribute.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeLongDAO addLongAttribute(MdClassDAO mdEntity)
  {
    return TestFixtureFactory.addLongAttribute(mdEntity, "testLong");
  }

  private static MdAttributeLongDAO addLongAttribute(MdClassDAO mdEntity, String attributeName)
  {
    MdAttributeLongDAO mdAttribute = MdAttributeLongDAO.newInstance();
    mdAttribute.setValue(MdAttributeLongInfo.NAME, attributeName);
    mdAttribute.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Long Set Test");
    mdAttribute.setValue(MdAttributeLongInfo.REJECT_ZERO, MdAttributeBooleanInfo.TRUE);
    mdAttribute.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeTextDAO addTextAttribute(MdEntityDAO mdEntity)
  {
    return addTextAttribute(mdEntity, "testText");
  }

  public static MdAttributeTextDAO addTextAttribute(MdEntityDAO mdEntity, String attributeName)
  {
    MdAttributeTextDAO mdAttribute = MdAttributeTextDAO.newInstance();
    mdAttribute.setValue(MdAttributeTextInfo.NAME, attributeName);
    mdAttribute.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Text Set Test");
    mdAttribute.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, mdEntity.getId());
    return mdAttribute;
  }

  public static MdAttributeClobDAO addClobAttribute(MdEntityDAO mdEntity)
  {
    MdAttributeClobDAO mdAttribute = MdAttributeClobDAO.newInstance();
    mdAttribute.setValue(MdAttributeTextInfo.NAME, "testClob");
    mdAttribute.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Clob Set Test");
    mdAttribute.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeTimeDAO addTimeAttribute(MdClassDAO mdEntity)
  {
    MdAttributeTimeDAO mdAttribute = MdAttributeTimeDAO.newInstance();
    mdAttribute.setValue(MdAttributeTimeInfo.NAME, "testTime");
    mdAttribute.setStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Time Set Test");
    mdAttribute.setValue(MdAttributeTimeInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeStructDAO addStructAttribute(MdEntityDAO mdEntity, MdStructDAO mdStruct)
  {
    MdAttributeStructDAO mdAttribute = MdAttributeStructDAO.newInstance();
    mdAttribute.setValue(MdAttributeStructInfo.NAME, "testStruct");
    mdAttribute.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Struct Set Test");
    mdAttribute.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, mdEntity.getId());
    mdAttribute.setValue(MdAttributeStructInfo.MD_STRUCT, mdStruct.getId());

    return mdAttribute;
  }

  public static MdAttributeSymmetricDAO addSymmetricAttribute(MdEntityDAO mdEntity)
  {
    MdAttributeSymmetricDAO mdAttribute = MdAttributeSymmetricDAO.newInstance();
    mdAttribute.setValue(MdAttributeSymmetricInfo.NAME, "testSymmetric");
    mdAttribute.setStructValue(MdAttributeSymmetricInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Symmetric TEST");
    mdAttribute.setValue(MdAttributeSymmetricInfo.SECRET_KEY_SIZE, "56");
    mdAttribute.setValue(MdAttributeSymmetricInfo.SYMMETRIC_METHOD, SymmetricMethods.DES.getId());
    mdAttribute.setValue(MdAttributeSymmetricInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeHashDAO addHashAttribute(MdEntityDAO mdEntity)
  {
    MdAttributeHashDAO mdAttribute = MdAttributeHashDAO.newInstance();
    mdAttribute.setValue(MdAttributeHashInfo.NAME, "testHash");
    mdAttribute.setStructValue(MdAttributeHashInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Hash Test");
    mdAttribute.setValue(MdAttributeHashInfo.HASH_METHOD, HashMethods.MD5.getId());
    mdAttribute.setValue(MdAttributeHashInfo.DEFINING_MD_CLASS, mdEntity.getId());

    return mdAttribute;
  }

  public static MdAttributeVirtualDAO addVirtualAttribute(MdViewDAO mdView, MdAttributeConcreteDAO concrete)
  {
    MdAttributeVirtualDAO virtual = MdAttributeVirtualDAO.newInstance();
    virtual.setValue(MdAttributeVirtualInfo.NAME, "testVirtual");
    virtual.setValue(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE, concrete.getId());
    virtual.setValue(MdAttributeVirtualInfo.DEFINING_MD_VIEW, mdView.getId());

    return virtual;
  }

  public static String getMdBusinessStub()
  {
    String source = "package test.xmlclasses;" + "public class Class1 extends Class1Base implements " + Reloadable.class.getName() + "\n" + "{\n" + "  public Class1()\n" + "  {\n" + "    super();\n" + "  }\n" + "  public static Class1 get(String id)\n" + "  {\n" + "    return (Class1) " + Business.class.getName() + ".get(id);\n" + "  }\n" + "  public void tempMethod()\n" + "  {\n" + "    return;\n" + "  }\n" + "}";

    return source;
  }

  public static String getMdBusinessDTOStub()
  {
    String source = "package test.xmlclasses;\n" + "public class Class1DTO extends Class1DTOBase implements " + Reloadable.class.getName() + "\n" + "{\n" + "  public static final long serialVersionUID = 1205173531292L;\n" + "  public Class1DTO(" + ClientRequestIF.class.getName() + " clientRequest)\n" + "  {\n" + "    super(clientRequest);\n" + "  }\n" + "  /**\n" + "  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.\n" + "  * \n" + "  * @param businessDTO The BusinessDTO to duplicate\n" + "  * @param clientRequest The clientRequest this DTO should use to communicate with the server.\n" + "  */\n" + "  protected Class1DTO(" + BusinessDTO.class.getName() + " businessDTO, " + ClientRequestIF.class.getName() + " clientRequest)\n" + "  {\n"
        + "    super(businessDTO, clientRequest);\n" + "  }\n" + "  public static Class1DTO get(" + ClientRequestIF.class.getName() + " clientRequest, String id)\n" + "  {\n" + "    " + EntityDTO.class.getName() + " dto = (" + EntityDTO.class.getName() + ")clientRequest.get(id);\n" + "    return (Class1DTO) dto;\n" + "  }\n" + "  public void tempMethod()\n" + "  {\n" + "    return;\n" + "  }\n" + "}\n";

    return source;
  }

  public static String getMdFacadeStub()
  {
    String source = "package test.xmlclasses;" + "public class Facade1 extends Facade1Base\n" + "{\n" + "  public void tempMethod()\n" + "  {\n" + "    return;\n" + "  }\n" + "}";

    return source;
  }

  public static MdParameterDAO createMdParameter(MdMethodDAO mdMethod, String name, int index)
  {
    MdParameterDAO mdParameter = MdParameterDAO.newInstance();

    if (mdMethod != null)
    {
      mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod.getId());
    }

    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, name);
    mdParameter.setValue(MdParameterInfo.NAME, name);
    mdParameter.setValue(MdParameterInfo.ORDER, Integer.toString(index));
    mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.String");

    return mdParameter;
  }

  public static EnumerationItemDAO createEnumObject(MdBusinessDAO mdEnumerationMaster, String display)
  {
    EnumerationItemDAO businessDAO = EnumerationItemDAO.newInstance(mdEnumerationMaster.definesType());
    businessDAO.setValue(EnumerationMasterInfo.NAME, display.toUpperCase());
    businessDAO.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, "defaultLocale", display);

    return businessDAO;
  }

  public static void delete(ComponentIF component)
  {
    if (component instanceof EntityDAO)
    {
      try
      {
        EntityDAO.get(component.getId()).getEntityDAO().delete();
      }
      catch (DataNotFoundException dataNotFoundException)
      {
        System.out.println("[" + component.getKey() + "] of type [" + component.getType() + "] could not be deleted ");
      }
    }
  }

  public static MdRelationshipDAO createMdTree(MdBusinessDAO parent, MdBusinessDAO child)
  {
    MdTreeDAO mdTree = MdTreeDAO.newInstance();
    mdTree.setValue(MdTreeInfo.NAME, "Relationship1");
    mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PACKAGE, "test.rbac");
    mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);

    mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, parent.getId());
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
    mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "TEST class");
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "TestParent");

    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, child.getId());
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "1");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test child class");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "TestChild");

    return mdTree;
  }

  public static MdStateMachineDAO createMdStateMachine(MdBusinessDAO mdBusiness)
  {
    MdStateMachineDAO mdState = MdStateMachineDAO.newInstance();
    mdState.setValue(MdStateMachineInfo.NAME, "TVMachine");
    mdState.setValue(MdStateMachineInfo.PACKAGE, "test.state");
    mdState.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "TV StateMachine");
    mdState.setValue(MdStateMachineInfo.SUPER_MD_BUSINESS, EntityTypes.STATE_MASTER.getId());
    mdState.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness.getId());

    return mdState;
  }

  public static TypeTupleDAO createTypeTuple(StateMasterDAO state, MetadataDAO metadata)
  {
    TypeTupleDAO typeTuple = TypeTupleDAO.newInstance();
    typeTuple.setStateMaster(state.getId());
    typeTuple.setMetaData(metadata.getId());
    typeTuple.setStructValue(TypeTupleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Tuple");

    return typeTuple;
  }

  public static MdDomainDAO createMdDomain()
  {
    MdDomainDAO mdDomain = MdDomainDAO.newInstance();
    mdDomain.setValue(MdDomainInfo.DOMAIN_NAME, "testDomain");
    mdDomain.setStructValue(MdDomainInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Domain");

    return mdDomain;
  }

  public static MdControllerDAO createMdController()
  {
    MdControllerDAO mdController = MdControllerDAO.newInstance();
    mdController.setValue(MdControllerInfo.NAME, "TestController");
    mdController.setValue(MdControllerInfo.PACKAGE, Constants.TEST_PACKAGE);
    mdController.setStructValue(MdControllerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Controller");
    mdController.setStructValue(MdControllerInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Controller");

    return mdController;
  }

  public static MdActionDAO createMdAction(MdControllerDAO mdController)
  {
    MdActionDAO mdAction = MdActionDAO.newInstance();
    mdAction.setValue(MdActionInfo.NAME, "testAction");
    mdAction.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
    mdAction.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Action");
    mdAction.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Action");
    mdAction.setValue(MdActionInfo.IS_POST, MdAttributeBooleanInfo.TRUE);

    return mdAction;
  }

  public static MdActionDAO createMdParameter(MdActionDAO mdAction, MdBusinessDAO mdBusiness)
  {
    MdParameterDAO mdParameter = MdParameterDAO.newInstance();
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction.getId());
    mdParameter.setValue(MdParameterInfo.TYPE, mdBusiness.definesType());
    mdParameter.setValue(MdParameterInfo.NAME, "test");
    mdParameter.setValue(MdParameterInfo.ORDER, "0");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test");

    return mdAction;
  }

  public static MdWarningDAO createMdWarning()
  {
    MdWarningDAO mdWarning = MdWarningDAO.newInstance();
    mdWarning.setValue(MdWarningInfo.NAME, "Warning1");
    mdWarning.setValue(MdWarningInfo.PACKAGE, Constants.TEST_PACKAGE);
    mdWarning.setValue(MdWarningInfo.ABSTRACT, "false");
    mdWarning.setStructValue(MdWarningInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "mdWarning Set Test");
    mdWarning.setStructValue(MdWarningInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Set mdWarning Attributes Test");

    return mdWarning;
  }

  public static MdWebFormDAO createMdWebForm(MdBusinessDAO mdBusiness)
  {
    MdWebFormDAO mdWebForm = MdWebFormDAO.newInstance();
    mdWebForm.setValue(MdWebFormInfo.NAME, "TestWebForm");
    mdWebForm.setValue(MdWebFormInfo.PACKAGE, Constants.TEST_PACKAGE);
    mdWebForm.setStructValue(MdWebFormInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test WebForm");
    mdWebForm.setStructValue(MdWebFormInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test WebForm");
    mdWebForm.setValue(MdWebFormInfo.FORM_NAME, "TestWebForm");
    mdWebForm.setValue(MdWebFormInfo.FORM_MD_CLASS, mdBusiness.getId());

    return mdWebForm;
  }

  public static MdWebCharacterDAO addCharacterField(MdWebFormDAO mdWebForm, MdAttributeCharacterDAO mdAttributeCharacter)
  {
    MdWebCharacterDAO mdWebCharacter = MdWebCharacterDAO.newInstance();
    String fieldName = mdAttributeCharacter.getValue(MdAttributeCharacterInfo.NAME);
    mdWebCharacter.setValue(MdWebCharacterInfo.FIELD_NAME, fieldName);
    mdWebCharacter.setValue(MdWebCharacterInfo.FIELD_ORDER, "0");
    mdWebCharacter.setValue(MdWebCharacterInfo.DISPLAY_LENGTH, "50");
    mdWebCharacter.setValue(MdWebCharacterInfo.MAX_LENGTH, "200");
    mdWebCharacter.setValue(MdWebCharacterInfo.DEFINING_MD_FORM, mdWebForm.getId());
    mdWebCharacter.setValue(MdWebCharacterInfo.DEFINING_MD_ATTRIBUTE, mdAttributeCharacter.getId());
    mdWebCharacter.setStructValue(MdWebCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Character Field");
    mdWebCharacter.setStructValue(MdWebCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Character Field");

    return mdWebCharacter;
  }

  public static MdWebBooleanDAO addBooleanField(MdWebFormDAO mdWebForm, MdAttributeBooleanDAO mdAttributeBoolean)
  {
    MdWebBooleanDAO mdWebBoolean = MdWebBooleanDAO.newInstance();
    mdWebBoolean.setValue(MdWebBooleanInfo.FIELD_NAME, "booleanField");
    mdWebBoolean.setValue(MdWebBooleanInfo.FIELD_ORDER, "1");
    mdWebBoolean.setValue(MdWebBooleanInfo.DEFINING_MD_FORM, mdWebForm.getId());
    mdWebBoolean.setValue(MdWebBooleanInfo.DEFINING_MD_ATTRIBUTE, mdAttributeBoolean.getId());
    mdWebBoolean.setStructValue(MdWebBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Boolean Field");
    mdWebBoolean.setStructValue(MdWebBooleanInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Boolean Field");

    return mdWebBoolean;
  }

  public static MdWebIntegerDAO addIntegerField(MdWebFormDAO mdWebForm, MdAttributeIntegerDAO mdAttributeInteger)
  {
    MdWebIntegerDAO mdWebInteger = MdWebIntegerDAO.newInstance();
    mdWebInteger.setValue(MdWebIntegerInfo.FIELD_NAME, "integerField");
    mdWebInteger.setValue(MdWebIntegerInfo.FIELD_ORDER, "2");
    mdWebInteger.setValue(MdWebIntegerInfo.DEFINING_MD_FORM, mdWebForm.getId());
    mdWebInteger.setValue(MdWebIntegerInfo.DEFINING_MD_ATTRIBUTE, mdAttributeInteger.getId());
    mdWebInteger.setStructValue(MdWebIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Integer Field");
    mdWebInteger.setStructValue(MdWebIntegerInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Integer Field");

    return mdWebInteger;
  }

  public static MdWebLongDAO addLongField(MdWebFormDAO mdWebForm, MdAttributeLongDAO mdAttributeLong)
  {
    MdWebLongDAO mdWebLong = MdWebLongDAO.newInstance();
    mdWebLong.setValue(MdWebLongInfo.FIELD_NAME, "longField");
    mdWebLong.setValue(MdWebLongInfo.FIELD_ORDER, "3");
    mdWebLong.setValue(MdWebLongInfo.DEFINING_MD_FORM, mdWebForm.getId());
    mdWebLong.setValue(MdWebLongInfo.DEFINING_MD_ATTRIBUTE, mdAttributeLong.getId());
    mdWebLong.setStructValue(MdWebLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Long Field");
    mdWebLong.setStructValue(MdWebLongInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Long Field");

    return mdWebLong;
  }

  public static MdWebFloatDAO addFloatField(MdWebFormDAO mdWebForm, MdAttributeFloatDAO mdAttributeFloat)
  {
    MdWebFloatDAO mdWebFloat = MdWebFloatDAO.newInstance();
    mdWebFloat.setValue(MdWebFloatInfo.FIELD_NAME, "floatField");
    mdWebFloat.setValue(MdWebFloatInfo.FIELD_ORDER, "4");
    mdWebFloat.setValue(MdWebFloatInfo.DEFINING_MD_FORM, mdWebForm.getId());
    mdWebFloat.setValue(MdWebFloatInfo.DEFINING_MD_ATTRIBUTE, mdAttributeFloat.getId());
    mdWebFloat.setStructValue(MdWebFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Float Field");
    mdWebFloat.setStructValue(MdWebFloatInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Float Field");

    return mdWebFloat;
  }

  public static MdWebDoubleDAO addDoubleField(MdWebFormDAO mdWebForm, MdAttributeDoubleDAO mdAttributeDouble)
  {
    MdWebDoubleDAO mdWebDouble = MdWebDoubleDAO.newInstance();
    String fieldName = mdAttributeDouble.getValue(MdAttributeDoubleInfo.NAME);
    mdWebDouble.setValue(MdWebDoubleInfo.FIELD_NAME, fieldName);
    mdWebDouble.setValue(MdWebDoubleInfo.FIELD_ORDER, "5");
    mdWebDouble.setValue(MdWebDoubleInfo.DEFINING_MD_FORM, mdWebForm.getId());
    mdWebDouble.setValue(MdWebDoubleInfo.DEFINING_MD_ATTRIBUTE, mdAttributeDouble.getId());
    mdWebDouble.setStructValue(MdWebDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Double Field");
    mdWebDouble.setStructValue(MdWebDoubleInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Double Field");

    return mdWebDouble;
  }

  public static MdWebSingleTermGridDAO addSingleTermGridField(MdWebFormDAO mdWebForm, MdAttributeReferenceDAO mdAttributeReference)
  {
    MdWebSingleTermGridDAO mdWebSingleTermGrid = MdWebSingleTermGridDAO.newInstance();
    mdWebSingleTermGrid.setValue(MdWebSingleTermGridInfo.FIELD_NAME, "gridField");
    mdWebSingleTermGrid.setValue(MdWebSingleTermGridInfo.FIELD_ORDER, "6");
    mdWebSingleTermGrid.setValue(MdWebSingleTermGridInfo.DEFINING_MD_FORM, mdWebForm.getId());
    mdWebSingleTermGrid.setValue(MdWebSingleTermGridInfo.DEFINING_MD_ATTRIBUTE, mdAttributeReference.getId());
    mdWebSingleTermGrid.setStructValue(MdWebSingleTermGridInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "SingleTermGrid Field");
    mdWebSingleTermGrid.setStructValue(MdWebSingleTermGridInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "SingleTermGrid Field");

    return mdWebSingleTermGrid;
  }

  public static MdWebDateDAO addDateField(MdWebFormDAO mdForm, MdAttributeDateDAO mdAttributeDate)
  {
    MdWebDateDAO mdWebDate = MdWebDateDAO.newInstance();
    mdWebDate.setValue(MdWebDateInfo.FIELD_NAME, "dateField");
    mdWebDate.setValue(MdWebDateInfo.FIELD_ORDER, "7");
    mdWebDate.setValue(MdWebDateInfo.DEFINING_MD_FORM, mdForm.getId());
    mdWebDate.setValue(MdWebDateInfo.DEFINING_MD_ATTRIBUTE, mdAttributeDate.getId());
    mdWebDate.setStructValue(MdWebDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Date Field");
    mdWebDate.setStructValue(MdWebDateInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Date Field");

    return mdWebDate;
  }

  public static DoubleConditionDAO createDoubleCondition(String operation, String value, MdFieldDAO definingField)
  {
    DoubleConditionDAO doubleCondition = DoubleConditionDAO.newInstance();
    String definingFieldId = definingField.getId();

    doubleCondition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, definingFieldId);
    doubleCondition.setValue(DoubleConditionInfo.OPERATION, operation);
    doubleCondition.setValue(DoubleConditionInfo.VALUE, value);
    return doubleCondition;
  }

  public static CharacterConditionDAO createCharacterCondition(String operation, String value, MdFieldDAO definingField)
  {
    CharacterConditionDAO characterCondition = CharacterConditionDAO.newInstance();
    String definingFieldId = definingField.getId();

    characterCondition.setValue(CharacterConditionInfo.DEFINING_MD_FIELD, definingFieldId);
    characterCondition.setValue(CharacterConditionInfo.OPERATION, operation);
    characterCondition.setValue(CharacterConditionInfo.VALUE, value);
    return characterCondition;
  }

  public static AndFieldConditionDAO createAndFieldCondition(String firstCondition, String secondCondition)
  {
    AndFieldConditionDAO andFieldCondition = AndFieldConditionDAO.newInstance();

    andFieldCondition.setValue(CompositeFieldConditionInfo.FIRST_CONDITION, firstCondition);
    andFieldCondition.setValue(CompositeFieldConditionInfo.SECOND_CONDITION, secondCondition);
    return andFieldCondition;
  }

  /**
   * Sets the domain and refreshes the cache, because when simulating a new
   * domain we do not want artifacts in the cache from the previous domain.
   * 
   * @param domain
   */
  public static void setDomain(String domain)
  {
    CommonProperties.setDomain(domain);
    // Cache is refreshed because a new domain needs new metadata. Some cache
    // implementations have a delay
    // before they remove deleted objects, which can wreak havoc on some tests.
    ObjectCache.refreshCache();
  }

  public static VaultDAO createVault()
  {
    VaultDAO vault = VaultDAO.newInstance();
    vault.setVaultPath("vault1");

    return vault;
  }

  public static MdWebGroupDAO addGroupField(MdWebFormDAO mdForm, String fieldName)
  {
    MdWebGroupDAO mdWebGroup = MdWebGroupDAO.newInstance();
    mdWebGroup.setValue(MdWebGroupInfo.FIELD_NAME, fieldName);
    mdWebGroup.setValue(MdWebGroupInfo.FIELD_ORDER, "-1");
    mdWebGroup.setValue(MdWebGroupInfo.DEFINING_MD_FORM, mdForm.getId());
    mdWebGroup.setStructValue(MdWebGroupInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Group Field");
    mdWebGroup.setStructValue(MdWebGroupInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Group Field");

    return mdWebGroup;
  }

  public static MdWebGroupDAO addGroupField(MdWebFormDAO mdForm)
  {
    return addGroupField(mdForm, "groupField");
  }

  public static MdWebReferenceDAO addReferenceField(MdWebFormDAO mdWebForm, MdAttributeReferenceDAO mdAttributeReference)
  {
    MdWebReferenceDAO mdWebReference = MdWebReferenceDAO.newInstance();
    mdWebReference.setValue(MdWebReferenceInfo.FIELD_NAME, "referenceField");
    mdWebReference.setValue(MdWebReferenceInfo.FIELD_ORDER, "9");
    mdWebReference.setValue(MdWebReferenceInfo.DEFINING_MD_FORM, mdWebForm.getId());
    mdWebReference.setValue(MdWebReferenceInfo.DEFINING_MD_ATTRIBUTE, mdAttributeReference.getId());
    mdWebReference.setStructValue(MdWebReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference Field");
    mdWebReference.setStructValue(MdWebReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference Field");

    return mdWebReference;
  }

  public static MdBusinessDAO createMdBusiness(String name)
  {
    MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.NAME, name);
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, Constants.TEST_PACKAGE);
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "mdBusiness Set Test");
    mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Set mdBusiness Attributes Test");

    return mdBusiness;
  }

  /**
   * @return
   */
  public static MdTermDAO createMdTerm()
  {
    return TestFixtureFactory.createMdTerm("TestTerm");
  }

  public static MdTermDAO createMdTerm(String name)
  {
    return TestFixtureFactory.createMdTerm(Constants.TEST_PACKAGE, name);
  }

  public static MdTermDAO createMdTerm(String packageName, String typeName)
  {
    MdTermDAO mdTerm = MdTermDAO.newInstance();
    mdTerm.setValue(MdTermInfo.NAME, typeName);
    mdTerm.setValue(MdTermInfo.PACKAGE, packageName);
    mdTerm.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdTerm.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdTerm.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTerm.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());

    return mdTerm;
  }

  /**
   * @param mdTerm
   * @return
   */
  public static MdTermRelationshipDAO createMdTermRelationship(MdTermDAO mdTerm)
  {
    MdTermRelationshipDAO mdTermRelationship = MdTermRelationshipDAO.newInstance();
    mdTermRelationship.setValue(MdTreeInfo.NAME, "LocatedIn");
    mdTermRelationship.setValue(MdTreeInfo.PACKAGE, Constants.TEST_PACKAGE);
    mdTermRelationship.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Located In");
    mdTermRelationship.setValue(MdTreeInfo.PARENT_MD_BUSINESS, mdTerm.getId());
    mdTermRelationship.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
    mdTermRelationship.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent Term");
    mdTermRelationship.setValue(MdTreeInfo.CHILD_MD_BUSINESS, mdTerm.getId());
    mdTermRelationship.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
    mdTermRelationship.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child Term");
    mdTermRelationship.setValue(MdTreeInfo.PARENT_METHOD, "ParentTerm");
    mdTermRelationship.setValue(MdTreeInfo.CHILD_METHOD, "ChildTerm");
    mdTermRelationship.setGenerateMdController(false);
    mdTermRelationship.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.TREE.getId());

    return mdTermRelationship;
  }

  /**
   * @param mdBusiness1
   * @param mdBusiness2
   * @return
   */
  public static MdAttributeMultiReferenceDAO addMultiReferenceAttribute(MdBusinessDAO mdBusiness, MdBusinessDAO referenceMdBusiness)
  {
    MdAttributeMultiReferenceDAO mdAttributeMultiReference = MdAttributeMultiReferenceDAO.newInstance();
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.NAME, "testMultiReference");
    mdAttributeMultiReference.setStructValue(MdAttributeMultiReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Test");
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.REF_MD_ENTITY, referenceMdBusiness.getId());
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.DEFINING_MD_CLASS, mdBusiness.getId());

    return mdAttributeMultiReference;
  }
}
