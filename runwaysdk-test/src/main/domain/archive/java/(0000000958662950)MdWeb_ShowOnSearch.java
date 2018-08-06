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
package com.runwaysdk;

import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.IndexTypes;
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
import com.runwaysdk.constants.MdWebAttributeInfo;
import com.runwaysdk.constants.VaultInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.Vault;

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
    
    String[] types = new String[] { MdWebAttributeInfo.CLASS };

    for (String type : types)
    {
      MdBusinessDAO mdField = MdBusinessDAO.getMdBusinessDAO(type).getBusinessDAO();

      MdAttributeBooleanDAO showOnViewAll = MdAttributeBooleanDAO.newInstance();
      showOnViewAll.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdField.getOid());
      showOnViewAll.setValue(MdAttributeBooleanInfo.NAME, "showOnSearch");
      showOnViewAll.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      showOnViewAll.setValue(MdAttributeBooleanInfo.DEFAULT_VALUE, MdAttributeBooleanInfo.TRUE);
      showOnViewAll.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Flag indicating if the field should be shown on the search page.");
      showOnViewAll.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Show on search");
      showOnViewAll.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "True");
      showOnViewAll.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "False");
      showOnViewAll.apply();
    }
  }
}