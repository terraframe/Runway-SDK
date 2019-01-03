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
package com.runwaysdk.transport.conversion.business;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.BusinessEnumeration;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.facade.FacadeUtil;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.transport.attributes.AttributeDTO;

/**
 * Converts a Business into a BusinessDTO
 */
public class BusinessToBusinessDTO extends ElementToElementDTO
{	
  /**
   * 
   * @param sessionId
   * @param business
   * @param convertMetaData
   */
  public BusinessToBusinessDTO(String sessionId, Business business, boolean convertMetaData)
  {
    super(sessionId, business, convertMetaData);
  }

  /**
   * 
   */
  protected Business getComponentIF()
  {
    return (Business)super.getComponentIF();
  }
  
  /**
   * Instantiates BusinessDTO (not type safe)
   * @param sessionId
   * @param type
   * @param attributeMap
   * @param newInstance
   * @param readable
   * @param writable
   * @param modified
   * @return BusinessDTO (not type safe)
   */
  protected BusinessDTO factoryMethod(Map<String, AttributeDTO> attributeMap,
      boolean newInstance, boolean readable, boolean writable, boolean modified)
  {
    return ComponentDTOFacade.buildBusinessDTO(null, this.getComponentIF().getType(), attributeMap,
        newInstance, readable, writable, modified,  this.getComponentIF().toString(), this.getComponentIF().checkUserLock());
  }
  
  /**
   * Returns the populated BusinessDTO
   * 
   */
  public BusinessDTO populate()
  {
    BusinessDTO businessDTO = (BusinessDTO) super.populate();
        
    return businessDTO;
  }
 
  
  /**
   * Returns the BusinessDTO of the enumeration items for the given MdEnumeration type with the given names.
   * 
   * @param sessionId The session Id
   * @param enumType The type of the enumeration
   * @param enumNames names of the enumeration items.
   * @return BusinessDTO of the enumeration items for the give MdEnumeration type.
   */
  public static List<BusinessDTO> getEnumerations(String sessionId, String enumType, String[] enumNames)
  {   
    BusinessEnumeration[] busEnumArray = new BusinessEnumeration[enumNames.length];
    try
    {
      Class<?> c = LoaderDecorator.load(enumType);

      for (int i=0; i<enumNames.length; i++)
      {
        busEnumArray[i] = (BusinessEnumeration) c.getMethod("valueOf", String.class).invoke(null, enumNames[i]);
      }
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }

    return getEnumerationItems(sessionId, busEnumArray);
  }
  
  /**
   * Returns the BusinessDTO of the enumeration items for the given MdEnumeration type.
   * 
   * @param sessionId The session Id
   * @param enumType The type of the enumeration
   * @return BusinessDTO of the enumeration items for the give MdEnumeration type.
   */
  public static List<BusinessDTO> getAllEnumerations(String sessionId, String enumType)
  {   
    BusinessEnumeration[] busEnumArray;
    
    try
    {
      Class<?> c = LoaderDecorator.load(enumType);
      busEnumArray = (BusinessEnumeration[]) c.getMethod("values").invoke(null);
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }

    return getEnumerationItems(sessionId, busEnumArray);
  }
  
  /**
   * Returns <code>BusinessDTO</code>s that have attributes for the given enumerations.
   * @param sessionId
   * @param busEnumArray
   * @return <code>BusinessDTO</code>s that have attributes for the given enumerations.
   */
  private static List<BusinessDTO> getEnumerationItems(String sessionId, BusinessEnumeration[] busEnumArray)
  {
    List<BusinessDTO> businessDTOList = new LinkedList<BusinessDTO>();
    
    for (BusinessEnumeration businessEnumeration : busEnumArray)
    {
      Business entity = Business.get(businessEnumeration.getOid());
      businessDTOList.add((BusinessDTO) FacadeUtil.populateComponentDTOIF(sessionId, entity, true));
    }

    return businessDTOList;
  }
  
}
