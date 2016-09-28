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
package com.runwaysdk.dataaccess.metadata;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdControllerInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdFormInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdControllerDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdFormDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;


public class MdPackage
{
  private String packageName;
  
  public MdPackage(String name)
  {
    packageName = name;
  }

  /**
   * @return
   */
  private BusinessDAOQuery createPackageQuery(String type)
  {   
    QueryFactory queryFactory = new QueryFactory();
    BusinessDAOQuery businessDAOquery = queryFactory.businessDAOQuery(type);
    businessDAOquery.WHERE(businessDAOquery.aCharacter(MdTypeInfo.PACKAGE).LIKE(packageName + '*'));
    return businessDAOquery;  
    
  }
  
  public List<MdTypeDAOIF> getMdTypes()
  {
    List<MdTypeDAOIF> list = new LinkedList<MdTypeDAOIF>();
    
    BusinessDAOQuery mdTypeQuery = createPackageQuery(MdTypeInfo.CLASS); 
    OIterator<BusinessDAOIF> mdBusinessIterator = mdTypeQuery.getIterator();
    
    while (mdBusinessIterator.hasNext())
    {
      list.add((MdTypeDAOIF)mdBusinessIterator.next());
    }
    
    return list;
  }
  
  public List<MdBusinessDAOIF> getMdBusinesses()
  {
    List<MdBusinessDAOIF> list = new LinkedList<MdBusinessDAOIF>();
    
    BusinessDAOQuery mdBusinessQuery = createPackageQuery(MdBusinessInfo.CLASS); 
    OIterator<BusinessDAOIF> mdBusinessIterator = mdBusinessQuery.getIterator();
    
    while (mdBusinessIterator.hasNext())
    {
      list.add((MdBusinessDAOIF)mdBusinessIterator.next());
    }
    
    return list;
  }
  
  public List<MdRelationshipDAOIF> getMdRelationships()
  {
    List<MdRelationshipDAOIF> list = new LinkedList<MdRelationshipDAOIF>();
    
    BusinessDAOQuery mdRelationshipQuery = createPackageQuery(MdRelationshipInfo.CLASS); 
    OIterator<BusinessDAOIF> mdRelationshipIterator = mdRelationshipQuery.getIterator();
    
    while (mdRelationshipIterator.hasNext())
    {
      list.add((MdRelationshipDAOIF)mdRelationshipIterator.next());
    }
    
    return list;
    
  }
  
  public List<MdEnumerationDAOIF> getMdEnumerations()
  {
    List<MdEnumerationDAOIF> list = new LinkedList<MdEnumerationDAOIF>();
    
    BusinessDAOQuery mdEnumerationQuery = createPackageQuery(MdEnumerationInfo.CLASS); 
    OIterator<BusinessDAOIF> mdEnumerationIterator = mdEnumerationQuery.getIterator();
    
    while (mdEnumerationIterator.hasNext())
    {
      list.add((MdEnumerationDAOIF)mdEnumerationIterator.next());
    }
    return list;
  }
  
  public List<MdControllerDAOIF> getMdControllers()
  {
    List<MdControllerDAOIF> list = new LinkedList<MdControllerDAOIF>();
    
    BusinessDAOQuery query = createPackageQuery(MdControllerInfo.CLASS); 
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    while (iterator.hasNext())
    {
      list.add((MdControllerDAOIF)iterator.next());
    }
    return list;
  }
  
  public List<MdFormDAOIF> getMdForms()
  {
    List<MdFormDAOIF> list = new LinkedList<MdFormDAOIF>();
    
    BusinessDAOQuery query = createPackageQuery(MdFormInfo.CLASS); 
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    while (iterator.hasNext())
    {
      list.add((MdFormDAOIF)iterator.next());
    }
    return list;
  }
  
  public List<MdStructDAOIF> getMdStructs()
  {
    List<MdStructDAOIF> list = new LinkedList<MdStructDAOIF>();
	  
    BusinessDAOQuery mdStructQuery = createPackageQuery(MdStructInfo.CLASS);
    OIterator<BusinessDAOIF> mdStructIterator = mdStructQuery.getIterator();
	  
	while(mdStructIterator.hasNext())
	{
      list.add((MdStructDAOIF)mdStructIterator.next());
	}
	  
	return list;
  }
    
  @Transaction
  public void delete()
  {
    // First delete MdEnumerations
    for (MdEnumerationDAOIF mdEnumeration : getMdEnumerations())
    {
      if(MdTypeDAO.isDefined(mdEnumeration.definesType()))
      {
        EntityDAO.get(mdEnumeration.getId()).getEntityDAO().delete();
      }
    }
    
    // Then delete MdForm
    for(MdFormDAOIF mdForm : getMdForms())
    {
      if(MdTypeDAO.isDefined(mdForm.definesType()))
      {
        EntityDAO.get(mdForm.getId()).getEntityDAO().delete();
      }      
    }    
    
    // Then delete MdControllers
    for(MdControllerDAOIF mdController : getMdControllers())
    {
      if(MdTypeDAO.isDefined(mdController.definesType()))
      {
        EntityDAO.get(mdController.getId()).getEntityDAO().delete();
      }      
    }    

    // Then delete MdRelationships
    for (MdRelationshipDAOIF mdRelationship : getMdRelationships())
    {
      if(MdTypeDAO.isDefined(mdRelationship.definesType()))
      {
        EntityDAO.get(mdRelationship.getId()).getEntityDAO().delete();
      }
    }
    
    // Finally, delete MdBusinesses
    for (MdBusinessDAOIF mdBusinesses : getMdBusinesses())
    {
      if(MdTypeDAO.isDefined(mdBusinesses.definesType()))
      {
        EntityDAO.get(mdBusinesses.getId()).getEntityDAO().delete();
      }
    }
    
    // delete structs
    for (MdStructDAOIF mdStruct : getMdStructs())
    {
      if(MdTypeDAO.isDefined(mdStruct.definesType()))
      {
        EntityDAO.get(mdStruct.getId()).getEntityDAO().delete();
      }
    }
    
    // And remove any remaining types
    for (MdTypeDAOIF mdType : getMdTypes())
    {
      if(MdTypeDAO.isDefined(mdType.definesType()))
      {
        EntityDAO.get(mdType.getId()).getEntityDAO().delete();
      }
    }
  }
}
