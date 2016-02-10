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
package com.runwaysdk.dataaccess;

import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;

/**
 * This class has DAO migration tasks for refactoring routines.
 * 
 * @author nathan
 *
 */
public class MigrationUtil
{ 
  /** 
   * Sets all attribute references to have non-unique indexes if they have no index defined.
   */
  @Request
  public static void setReferenceAttributeDefaultIndexes()
  {
    ServerProperties.setIgnoreSiteMaster(true);
    ServerProperties.setAllowModificationOfMdAttribute(true);
    
    try
    {
      setReferenceAttributeDefaultIndexes_Transaction();
    }
    finally
    {
      ServerProperties.setIgnoreSiteMaster(false);
      ServerProperties.setAllowModificationOfMdAttribute(false);
    }
  }   
  
  @Transaction
  private static void setReferenceAttributeDefaultIndexes_Transaction()
  {
    QueryFactory qf = new QueryFactory();
    
    BusinessDAOQuery mdAttrRefQ = qf.businessDAOQuery(MdAttributeReferenceInfo.CLASS);
    
    OIterator<BusinessDAOIF> i = mdAttrRefQ.getIterator();
    
    for (BusinessDAOIF businessDAOIF : i)
    {
      MdAttributeReferenceDAO mdAttrRef = ((MdAttributeReferenceDAOIF)businessDAOIF).getBusinessDAO();
      
      MdClassDAOIF mdClassDAOIF = mdAttrRef.definedByClass();
      
      AttributeEnumerationIF index = (AttributeEnumerationIF) mdAttrRef.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);
      String derefId = index.dereference()[0].getId();

      if (derefId.equalsIgnoreCase(IndexTypes.NO_INDEX.getId()))
      {
        mdAttrRef.setValue(MdAttributeConcreteInfo.INDEX_TYPE, IndexTypes.NON_UNIQUE_INDEX.getId());
        mdAttrRef.apply();
        
        System.out.println("DEFAULT SET for "+mdClassDAOIF.definesType()+"."+mdAttrRef.definesAttribute()+" "+index.dereference()[0].getName());
      }
      else
      {
        System.out.println("                "+mdClassDAOIF.definesType()+"."+mdAttrRef.definesAttribute()+" "+index.dereference()[0].getName());
      }
    }
  }

  
}
