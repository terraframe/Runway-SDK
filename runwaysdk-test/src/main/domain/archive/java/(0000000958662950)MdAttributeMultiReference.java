/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
import com.runwaysdk.constants.MdRelationshipInfo;
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
    importWithRequest();
  }

  @Request
  public static void importWithRequest()
  {
//    undoIt();
    doIt();
  }
  
  @Transaction
  private static void undoIt()
  {
    Database.enableLoggingDMLAndDDLstatements(false);
    
    
  }
  
  @Transaction
  private static void doIt()
  {
    Database.enableLoggingDMLAndDDLstatements(true);
    
    MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO(MdBusinessInfo.CLASS);
    MdBusinessDAOIF mdAttributeConcrete = MdBusinessDAO.getMdBusinessDAO(MdAttributeConcreteInfo.CLASS);

    MdBusinessDAO mdAttributeMultiReference = MdBusinessDAO.newInstance();
    mdAttributeMultiReference.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    mdAttributeMultiReference.setValue(MdBusinessInfo.NAME, "MdAttributeMultiReference");
    mdAttributeMultiReference.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata definition for multi reference attributes");
    mdAttributeMultiReference.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdAttributeMultiReference");
    mdAttributeMultiReference.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeMultiReference.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdAttributeConcrete.getOid());
    mdAttributeMultiReference.setGenerateMdController(false);
    mdAttributeMultiReference.apply();

    MdAttributeReferenceDAO mdAttributeReference = MdAttributeReferenceDAO.newInstance();
    mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdAttributeMultiReference.getOid());
    mdAttributeReference.setValue(MdAttributeReferenceInfo.NAME, "mdBusiness");
    mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference MdBusiness");
    mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference MdBusiness");
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.IMMUTABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, mdBusiness.getOid());
    mdAttributeReference.apply();

    MdAttributeCharacterDAO tableName = MdAttributeCharacterDAO.newInstance();
    tableName.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdAttributeMultiReference.getOid());
    tableName.setValue(MdAttributeCharacterInfo.NAME, MdAttributeMultiReferenceInfo.TABLE_NAME);
    tableName.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Table name");
    tableName.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Table name");
    tableName.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    tableName.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.TRUE);
    tableName.setValue(MdAttributeCharacterInfo.SIZE, "255");
    tableName.apply();

    MdAttributeCharacterDAO defaultValue = MdAttributeCharacterDAO.newInstance();
    defaultValue.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdAttributeMultiReference.getOid());
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
    mdAttributeMultiTerm.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata definition for multi term attributes");
    mdAttributeMultiTerm.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdAttributeMultiTerm");
    mdAttributeMultiTerm.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeMultiTerm.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdAttributeMultiReference.getOid());
    mdAttributeMultiTerm.setGenerateMdController(false);
    mdAttributeMultiTerm.apply();
  }
}