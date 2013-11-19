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

import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;

public class Sandbox
{
  public static void main(String[] args) throws Exception
  {
    // createType();
    // changeType();
    // updateStrategyType();
    // createMdAttributeTerm();
    createMdAttributeMultiReference();
  }

  @Request
  private static void changeType()
  {
    changeTypeInTransaction();
  }

  @Transaction
  private static void changeTypeInTransaction()
  {
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

    MdBusinessDAO strategyStateMaster = MdBusinessDAO.newInstance();
    strategyStateMaster.setValue(MdBusinessInfo.PACKAGE, Constants.ONTOLOGY_PACKAGE);
    strategyStateMaster.setValue(MdBusinessInfo.NAME, "StrategyStateMaster");
    strategyStateMaster.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "Strategy State Master");
    strategyStateMaster.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "Strategy State Master");
    strategyStateMaster.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    strategyStateMaster.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, EnumerationMasterInfo.ID_VALUE);
    strategyStateMaster.setGenerateMdController(false);
    strategyStateMaster.apply();

    BusinessDAO uninitialized = BusinessDAO.newInstance(strategyStateMaster.definesType());
    uninitialized.setValue(EnumerationMasterInfo.NAME, "UNINITIALIZED");
    uninitialized.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, "defaultLocale", "Uninitialized");
    uninitialized.apply();

    BusinessDAO initialized = BusinessDAO.newInstance(strategyStateMaster.definesType());
    initialized.setValue(EnumerationMasterInfo.NAME, "INITIALIZED");
    initialized.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, "defaultLocale", "Initialized");
    initialized.apply();

    BusinessDAO initializing = BusinessDAO.newInstance(strategyStateMaster.definesType());
    initializing.setValue(EnumerationMasterInfo.NAME, "INITIALIZING");
    initializing.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, "defaultLocale", "Initializing");
    initializing.apply();

    MdEnumerationDAO strategyState = MdEnumerationDAO.newInstance();
    strategyState.setValue(MdEnumerationInfo.PACKAGE, Constants.ONTOLOGY_PACKAGE);
    strategyState.setValue(MdEnumerationInfo.NAME, "StrategyState");
    strategyState.setStructValue(MdEnumerationInfo.DESCRIPTION, "defaultLocale", "Strategy State");
    strategyState.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, "defaultLocale", "Strategy State");
    strategyState.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, strategyStateMaster.getId());
    strategyState.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    strategyState.apply();

    MdBusinessDAO ontologyStrategy = MdBusinessDAO.newInstance();
    ontologyStrategy.setValue(MdBusinessInfo.PACKAGE, Constants.ONTOLOGY_PACKAGE);
    ontologyStrategy.setValue(MdBusinessInfo.NAME, "OntologyStrategy");
    ontologyStrategy.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "Ontology Strategy");
    ontologyStrategy.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "Ontology Strategy");
    ontologyStrategy.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    ontologyStrategy.setGenerateMdController(false);
    ontologyStrategy.apply();

    MdAttributeEnumerationDAO state = MdAttributeEnumerationDAO.newInstance();
    state.setValue(MdAttributeEnumerationInfo.NAME, "strategyState");
    state.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Strategy State");
    state.setStructValue(MdAttributeEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Strategy State");
    state.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    state.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, ontologyStrategy.getId());
    state.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, strategyState.getId());
    state.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
    state.apply();

    MdBusinessDAO databaseAllPathsStrategy = MdBusinessDAO.newInstance();
    databaseAllPathsStrategy.setValue(MdBusinessInfo.PACKAGE, Constants.ONTOLOGY_PACKAGE);
    databaseAllPathsStrategy.setValue(MdBusinessInfo.NAME, "DatabaseAllPathsStrategy");
    databaseAllPathsStrategy.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "Database all paths strategy");
    databaseAllPathsStrategy.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "Database all paths strategy");
    databaseAllPathsStrategy.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    databaseAllPathsStrategy.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, ontologyStrategy.getId());
    databaseAllPathsStrategy.setGenerateMdController(false);
    databaseAllPathsStrategy.apply();

    MdBusinessDAO postgresAllPathsStrategy = MdBusinessDAO.newInstance();
    postgresAllPathsStrategy.setValue(MdBusinessInfo.PACKAGE, Constants.ONTOLOGY_PACKAGE);
    postgresAllPathsStrategy.setValue(MdBusinessInfo.NAME, "PostgresAllPathsStrategy");
    postgresAllPathsStrategy.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "Postgres all paths strategy");
    postgresAllPathsStrategy.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "Postgres all paths strategy");
    postgresAllPathsStrategy.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    postgresAllPathsStrategy.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, databaseAllPathsStrategy.getId());
    postgresAllPathsStrategy.setGenerateMdController(false);
    postgresAllPathsStrategy.apply();

    MdBusinessDAOIF mdTerm = MdBusinessDAO.getMdBusinessDAO(MdTermInfo.CLASS);

    MdAttributeReferenceDAO strategy = MdAttributeReferenceDAO.newInstance();
    strategy.setValue(MdAttributeReferenceInfo.NAME, "strategy");
    strategy.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Strategy");
    strategy.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Strategy");
    strategy.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    strategy.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdTerm.getId());
    strategy.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, ontologyStrategy.getId());
    strategy.apply();
  }

  @Request
  private static void updateStrategyType()
  {
    updateStrategyTypeInTransaction();
  }

  @Transaction
  private static void updateStrategyTypeInTransaction()
  {
    Database.enableLoggingDMLAndDDLstatements(true);

    MdBusinessDAO databaseAllPathsStrategy = MdBusinessDAO.getMdBusinessDAO("com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategy").getBusinessDAO();
    databaseAllPathsStrategy.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    databaseAllPathsStrategy.setGenerateMdController(false);
    databaseAllPathsStrategy.apply();

    MdBusinessDAO.getMdBusinessDAO("com.runwaysdk.system.metadata.ontology.PostgresAllPathsStrategy").getBusinessDAO().delete();
  }

  @Request
  private static void createMdAttributeTerm()
  {
    createMdAttributeTermInTransaction();
  }

  @Transaction
  private static void createMdAttributeTermInTransaction()
  {
    Database.enableLoggingDMLAndDDLstatements(true);

    MdBusinessDAO mdAttributeReference = MdBusinessDAO.getMdBusinessDAO(MdAttributeReferenceInfo.CLASS).getBusinessDAO();
    mdAttributeReference.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeReference.apply();

    MdBusinessDAO mdAttributeTerm = MdBusinessDAO.newInstance();
    mdAttributeTerm.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    mdAttributeTerm.setValue(MdBusinessInfo.NAME, "MdAttributeTerm");
    mdAttributeTerm.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "Metadata definition for term attributes");
    mdAttributeTerm.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "MdAttributeTerm");
    mdAttributeTerm.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeTerm.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdAttributeReference.getId());
    mdAttributeTerm.setGenerateMdController(false);
    mdAttributeTerm.apply();
  }

  /**
   * 
   */
  @Request
  private static void createMdAttributeMultiReference()
  {
    Database.enableLoggingDMLAndDDLstatements(true);

    Sandbox.createMdAttributeMultiReferenceInTransaction();
  }

  @Transaction
  private static void createMdAttributeMultiReferenceInTransaction()
  {
    MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO(MdBusinessInfo.CLASS);
    MdBusinessDAOIF mdAttributeConcrete = MdBusinessDAO.getMdBusinessDAO(MdAttributeConcreteInfo.CLASS);

    MdBusinessDAO mdAttributeMultiReference = MdBusinessDAO.newInstance();
    mdAttributeMultiReference.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    mdAttributeMultiReference.setValue(MdBusinessInfo.NAME, "MdAttributeMultiReference");
    mdAttributeMultiReference.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "Metadata definition for multi reference attributes");
    mdAttributeMultiReference.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "MdAttributeMultiReference");
    mdAttributeMultiReference.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeMultiReference.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdAttributeConcrete.getId());
    mdAttributeMultiReference.setGenerateMdController(false);
    mdAttributeMultiReference.apply();

    MdAttributeReferenceDAO mdAttributeReference = MdAttributeReferenceDAO.newInstance();
    mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdAttributeMultiReference.getId());
    mdAttributeReference.setValue(MdAttributeReferenceInfo.NAME, "mdBusiness");
    mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference MdBusiness");
    mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference MdBusiness");
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.IMMUTABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, mdBusiness.getId());
    mdAttributeReference.apply();

    MdAttributeCharacterDAO tableName = MdAttributeCharacterDAO.newInstance();
    tableName.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdAttributeMultiReference.getId());
    tableName.setValue(MdAttributeCharacterInfo.NAME, MdAttributeMultiReferenceInfo.TABLE_NAME);
    tableName.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Table name");
    tableName.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Table name");
    tableName.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    tableName.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.TRUE);
    tableName.setValue(MdAttributeCharacterInfo.SIZE, "255");
    tableName.apply();

    MdAttributeCharacterDAO defaultValue = MdAttributeCharacterDAO.newInstance();
    defaultValue.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdAttributeMultiReference.getId());
    defaultValue.setValue(MdAttributeCharacterInfo.NAME, MdAttributeMultiReferenceInfo.DEFAULT_VALUE);
    defaultValue.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Default value");
    defaultValue.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Default value");
    defaultValue.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    defaultValue.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    defaultValue.setValue(MdAttributeCharacterInfo.SIZE, "64");
    defaultValue.apply();

    MdBusinessDAO mdAttributeMultiTerm = MdBusinessDAO.newInstance();
    mdAttributeMultiTerm.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    mdAttributeMultiTerm.setValue(MdBusinessInfo.NAME, "MdAttributeMultiTerm");
    mdAttributeMultiTerm.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "Metadata definition for multi term attributes");
    mdAttributeMultiTerm.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "MdAttributeMultiTerm");
    mdAttributeMultiTerm.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeMultiTerm.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdAttributeMultiReference.getId());
    mdAttributeMultiTerm.setGenerateMdController(false);
    mdAttributeMultiTerm.apply();
  }

}
