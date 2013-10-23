/**
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
 */
package com.runwaysdk;

import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;

public class Sandbox
{
  public static void main(String[] args) throws Exception
  {
//    createType();
    changeType();
  }

  @Request
  private static void changeType() {
    changeTypeInTransaction();
  }
  
  @Transaction
  private static void changeTypeInTransaction() {
    MdBusinessDAO idMapping = MdBusinessDAO.getMdBusinessDAO("com.runwaysdk.system.mobile.LocalIdMapping").getBusinessDAO();
    idMapping.setValue(MdClassInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    idMapping.apply();
    
    MdBusinessDAO sessionIdTo = MdBusinessDAO.getMdBusinessDAO("com.runwaysdk.system.mobile.SessionIdToMobileIdMapping").getBusinessDAO();
    sessionIdTo.setValue(MdClassInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    sessionIdTo.apply();
    
    MdBusinessDAO linkedStack = MdBusinessDAO.getMdBusinessDAO("com.runwaysdk.system.mobile.LinkedStackPersistance").getBusinessDAO();
    linkedStack.setValue(MdClassInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    linkedStack.apply();
  }
  
  
  
  @Request
  private static void createType()
  {
    createTypeInTransaction();
  }

  @Transaction
  private static void createTypeInTransaction()
  {
    Database.enableLoggingDMLAndDDLstatements(true);

    MdBusinessDAOIF superMdBusiness = MdBusinessDAO.getMdBusinessDAO(MdBusinessInfo.CLASS);

    MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, superMdBusiness.getId());
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, superMdBusiness.getValue(MdBusinessInfo.PACKAGE));
    mdBusiness.setValue(MdBusinessInfo.NAME, "MdTerm");
    mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "Business for term definition");
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "MdTerm");
    mdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdBusiness.setGenerateMdController(false);
    mdBusiness.apply();

    MdBusinessDAOIF enumerationMaster = MdBusinessDAO.getMdBusinessDAO(EnumerationMasterInfo.CLASS);

    MdBusinessDAO relationshipTypeEnumerationMaster = MdBusinessDAO.newInstance();
    relationshipTypeEnumerationMaster.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, enumerationMaster.getId());
    relationshipTypeEnumerationMaster.setValue(MdBusinessInfo.PACKAGE, superMdBusiness.getValue(MdBusinessInfo.PACKAGE));
    relationshipTypeEnumerationMaster.setValue(MdBusinessInfo.NAME, "AssociationTypeEnum");
    relationshipTypeEnumerationMaster.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    relationshipTypeEnumerationMaster.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "Enumeration master for association types");
    relationshipTypeEnumerationMaster.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "Association Type Enum");
    relationshipTypeEnumerationMaster.setGenerateMdController(false);
    relationshipTypeEnumerationMaster.apply();

    BusinessDAO relationship = BusinessDAO.newInstance(relationshipTypeEnumerationMaster.definesType());
    relationship.setValue(EnumerationMasterInfo.NAME, "Relationship");
    relationship.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Relationship");
    relationship.apply();

    BusinessDAO tree = BusinessDAO.newInstance(relationshipTypeEnumerationMaster.definesType());
    tree.setValue(EnumerationMasterInfo.NAME, "Tree");
    tree.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Tree");
    tree.apply();

    BusinessDAO graph = BusinessDAO.newInstance(relationshipTypeEnumerationMaster.definesType());
    graph.setValue(EnumerationMasterInfo.NAME, "Graph");
    graph.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Graph");
    graph.apply();

    MdEnumerationDAO mdEnumeration = MdEnumerationDAO.newInstance();
    mdEnumeration.setValue(MdEnumerationInfo.NAME, "AssociationType");
    mdEnumeration.setValue(MdEnumerationInfo.PACKAGE, superMdBusiness.getValue(MdBusinessInfo.PACKAGE));
    mdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, relationshipTypeEnumerationMaster.getId());
    mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Association type enumeration");
    mdEnumeration.apply();

    MdBusinessDAOIF superMdRelationship = MdBusinessDAO.getMdBusinessDAO(MdRelationshipInfo.CLASS);

    MdBusinessDAO mdRelationship = MdBusinessDAO.newInstance();
    mdRelationship.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, superMdRelationship.getId());
    mdRelationship.setValue(MdBusinessInfo.PACKAGE, superMdBusiness.getValue(MdBusinessInfo.PACKAGE));
    mdRelationship.setValue(MdBusinessInfo.NAME, "MdTermRelationship");
    mdRelationship.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "Metadata for defining relationships between terms");
    mdRelationship.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "MdTermRelationship");
    mdRelationship.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdRelationship.setGenerateMdController(false);
    mdRelationship.apply();

    MdAttributeEnumerationDAO mdAttribute = MdAttributeEnumerationDAO.newInstance();
    mdAttribute.setValue(MdAttributeEnumerationInfo.NAME, "associationType");
    mdAttribute.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Association type");
    mdAttribute.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
    mdAttribute.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, mdEnumeration.getId());
    mdAttribute.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, mdRelationship.getId());
    mdAttribute.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttribute.apply();
  }
}