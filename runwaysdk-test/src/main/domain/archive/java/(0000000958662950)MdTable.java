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

import com.runwaysdk.constants.AggregationFunctionInfo;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.IndicatorElementInfo;
import com.runwaysdk.constants.IndicatorCompositeInfo;
import com.runwaysdk.constants.IndicatorPrimitiveInfo;
import com.runwaysdk.constants.JobOperationInfo;
import com.runwaysdk.constants.MathOperatorInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeIndicatorInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.MdAttributePrimitiveInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdTableInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.MdWebAttributeInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.constants.SingleActorInfo;
import com.runwaysdk.constants.VaultInfo;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.IndicatorCompositeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdTableDAO;
import com.runwaysdk.dataaccess.metadata.MdTreeDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.Vault;
import com.runwaysdk.system.metadata.MdAttributeBoolean;
import com.runwaysdk.system.metadata.MdAttributeCharacter;
import com.runwaysdk.system.metadata.MdAttributeDateTime;
import com.runwaysdk.system.metadata.MdAttributeEnumeration;
import com.runwaysdk.system.metadata.MdAttributeIndices;
import com.runwaysdk.system.metadata.MdAttributeInteger;
import com.runwaysdk.system.metadata.MdAttributeLocalCharacter;
import com.runwaysdk.system.metadata.MdAttributeLocalText;
import com.runwaysdk.system.metadata.MdAttributeLong;
import com.runwaysdk.system.metadata.MdAttributeReference;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdRelationship;
import com.runwaysdk.system.metadata.ontology.OntologyStrategy;

public class Sandbox implements Job
{
  public static void main(String[] args) throws Exception
  {
    importWithDiff();
  }

  @Request
  public static void importWithDiff()
  {
    Database.enableLoggingDMLAndDDLstatements(true);

    createMdTable();
  }
  
  public static void testMdTable()
  {
    // MdTableDAO mdTable = MdTableDAO.newInstance();
    // mdTable.setValue(MdTableInfo.NAME, "TestTable");
    // mdTable.setValue(MdTableInfo.PACKAGE, "some.package");
    // mdTable.setStructValue(MdTableInfo.DISPLAY_LABEL,
    // MdAttributeLocalInfo.DEFAULT_LOCALE, "MdTable");
    // mdTable.setValue(MdTableInfo.TABLE_NAME, "md_class");
    //
    // String id = mdTable.apply();
    //
    // System.out.println();

    // MdBusinessDAO mdTable =
    // MdBusinessDAO.getMdBusinessDAO(MdTableInfo.CLASS).getBusinessDAO();
    //
    // MdAttributeConcreteDAO mdAttribute =
    // (MdAttributeConcreteDAO)mdTable.getMdAttributeDAO(MdTableInfo.TABLE_NAME).getBusinessDAO();
    // mdAttribute.setValue(MdAttributeCharacterInfo.INDEX_TYPE,
    // IndexTypes.NON_UNIQUE_INDEX.getId());
    // mdAttribute.apply();

    // mdTable.apply();
    // mdTable.printAttributes();
    //
    // MdBusinessDAOIF mdClass =
    // MdBusinessDAO.getMdBusinessDAO(MdClassInfo.CLASS);

    MdTableDAO mdTable = MdTableDAO.newInstance();
    mdTable.printAttributes();

  }

  @Transaction
  public static void createMdTable()
  {
    MdBusinessDAOIF mdClass = MdBusinessDAO.getMdBusinessDAO(MdClassInfo.CLASS);

    MdBusinessDAO mdTable = MdBusinessDAO.newInstance();
    mdTable.setValue(MdBusinessInfo.NAME, "MdTable");
    mdTable.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    mdTable.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdTable");
    mdTable.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata for relational database tables for entities whose lifecylce is not direclty managed");
    mdTable.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdClass.getId());
    mdTable.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdTable.setValue(MdBusinessInfo.EXPORTED, MdAttributeBooleanInfo.TRUE);
    mdTable.setValue(MdBusinessInfo.PUBLISH, MdAttributeBooleanInfo.TRUE);
    mdTable.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.TRUE);
    mdTable.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.FALSE);

    String mdTableMdId = mdTable.apply();

    System.out.println("MdTable ID: " + mdTableMdId);

    MdAttributeCharacter tableName = new MdAttributeCharacter();
    tableName.setValue(MdAttributeCharacterInfo.NAME, MdTableInfo.TABLE_NAME);
    tableName.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    tableName.setValue(MdAttributeCharacterInfo.GENERATE_ACCESSOR, MdAttributeBooleanInfo.TRUE);
    tableName.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdTableMdId);
    tableName.setValue(MdAttributeCharacterInfo.COLUMN_NAME, MdTableInfo.COLUMN_TABLE_NAME);
    tableName.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Table Name");
    tableName.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Name of the table in the database");
    tableName.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    tableName.setValue(MdAttributeCharacterInfo.SYSTEM, MdAttributeBooleanInfo.FALSE);
    tableName.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.TRUE);
    tableName.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.NON_UNIQUE_INDEX.getId());
    tableName.setValue(MdAttributeCharacterInfo.SETTER_VISIBILITY, VisibilityModifier.PUBLIC.getId());
    tableName.setValue(MdAttributeCharacterInfo.GETTER_VISIBILITY, VisibilityModifier.PUBLIC.getId());
    tableName.setValue(MdAttributeCharacterInfo.SIZE, MdTableInfo.MAX_TABLE_NAME);
    tableName.apply();
  }
}
