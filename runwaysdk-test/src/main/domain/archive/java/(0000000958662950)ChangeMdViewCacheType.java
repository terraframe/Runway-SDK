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

import java.util.List;

import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.EntityCacheMaster;
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
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.MdWebAttributeInfo;
import com.runwaysdk.constants.VaultInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
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
    
    MdBusinessDAO mdBusinessDAO = MdBusinessDAO.getMdBusinessDAO(MdViewInfo.CLASS).getBusinessDAO();

    List<MdBusinessDAOIF> superCasses = mdBusinessDAO.getSuperClasses();

    for (MdBusinessDAOIF mdBusinessDAOIF : superCasses)
    {
      BusinessDAOIF cacheEnumItem = mdBusinessDAOIF.getCacheAlgorithm();

      int cacheCode = Integer.valueOf(cacheEnumItem.getAttributeIF(EntityCacheMaster.CACHE_CODE).getValue()).intValue();
      System.out.println(mdBusinessDAOIF.definesType() + " " + cacheCode);
    }

    // QueryFactory qf = new QueryFactory();
    // BusinessDAOQuery q = qf.businessDAOQuery(MdElementInfo.CLASS);
    //
    // OIterator<BusinessDAOIF> i = q.getIterator();
    //
    // for (BusinessDAOIF businessDAOIF : i)
    // {
    // MdElementDAOIF mdElementDAOIF = (MdElementDAOIF)businessDAOIF;
    // BusinessDAOIF cacheEnumItem = mdElementDAOIF.getCacheAlgorithm();
    //
    // int cacheCode = new
    // Integer(cacheEnumItem.getAttributeIF(EntityCacheMaster.CACHE_CODE).getValue()).intValue();
    // System.out.println(mdElementDAOIF.definesType()+" "+cacheCode);
    // }
  }
}