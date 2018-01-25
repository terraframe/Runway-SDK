/**
 * "/" * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdTableInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.MdWebAttributeInfo;
import com.runwaysdk.constants.SingleActorInfo;
import com.runwaysdk.constants.VaultInfo;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdTableDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.Vault;
import com.runwaysdk.system.metadata.MdAttributeCharacter;


public class Sandbox
{
  public static void main(String[] args) throws Exception
  {
    importWithRequest();
  }

  @Request
  public static void importWithRequest()
  {
    LocalProperties.setSkipCodeGenAndCompile(true);
    
    undoIt();
    doIt();
  }
  
  @Transaction
  private static void undoIt()
  {
    Database.enableLoggingDMLAndDDLstatements(false);
    undoMdTerm();
  }
  
  @Transaction
  private static void doIt()
  {
    Database.enableLoggingDMLAndDDLstatements(true);
    doMdTerm();
  }
  
  private static void undoMdTerm()
  {
    MdBusinessDAO.getMdBusinessDAO("com.runwaysdk.system.metadata.MdTerm").getBusinessDAO().delete();
    
    MdEnumerationDAO.getMdEnumerationDAO("com.runwaysdk.system.metadata.AssociationType").getBusinessDAO().delete();
    
    MdBusinessDAO.getMdBusinessDAO("com.runwaysdk.system.metadata.AssociationTypeEnum").getBusinessDAO().delete();
    
    MdBusinessDAO.getMdBusinessDAO("com.runwaysdk.system.metadata.MdTermRelationship").getBusinessDAO().delete();
    
    MdBusinessDAO.getMdBusinessDAO("com.runwaysdk.system.metadata.MdAttributeTerm").getBusinessDAO().delete();
  }

  private static void doMdTerm()
  {
    MdBusinessDAOIF superMdBusiness = MdBusinessDAO.getMdBusinessDAO(MdBusinessInfo.CLASS);

    MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, superMdBusiness.getId());
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, superMdBusiness.getValue(MdBusinessInfo.PACKAGE));
    mdBusiness.setValue(MdBusinessInfo.NAME, "MdTerm");
    mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "Business for term definition");
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "MdTerm");
    mdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdBusiness.setGenerateMdController(false);
    mdBusiness.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
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
    relationshipTypeEnumerationMaster.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
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
    mdRelationship.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
    mdRelationship.apply();

    MdAttributeEnumerationDAO mdAttribute = MdAttributeEnumerationDAO.newInstance();
    mdAttribute.setValue(MdAttributeEnumerationInfo.NAME, "associationType");
    mdAttribute.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Association type");
    mdAttribute.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
    mdAttribute.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, mdEnumeration.getId());
    mdAttribute.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, mdRelationship.getId());
    mdAttribute.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttribute.apply();
    
    MdBusinessDAO mdAttributeReference = MdBusinessDAO.getMdBusinessDAO(MdAttributeReferenceInfo.CLASS).getBusinessDAO();
    mdAttributeReference.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeReference.apply();

    MdBusinessDAO mdAttributeTerm = MdBusinessDAO.newInstance();
    mdAttributeTerm.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    mdAttributeTerm.setValue(MdBusinessInfo.NAME, "MdAttributeTerm");
    mdAttributeTerm.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata definition for term attributes");
    mdAttributeTerm.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdAttributeTerm");
    mdAttributeTerm.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeTerm.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdAttributeReference.getId());
    mdAttributeTerm.setGenerateMdController(false);
    mdAttributeTerm.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
    mdAttributeTerm.apply();
  }
}
