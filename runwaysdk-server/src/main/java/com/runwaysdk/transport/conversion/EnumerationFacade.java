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
package com.runwaysdk.transport.conversion;

import java.lang.reflect.InvocationTargetException;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.BusinessEnumeration;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.InvalidEnumerationName;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.facade.FacadeUtil;
import com.runwaysdk.generation.loader.LoaderDecorator;

public class EnumerationFacade
{
  /**
   * Returns the Business of the enumeration item of the given name and type.
   * 
   * @param enumType
   *          The type of the enumeration
   * @param enumName
   *          The name of the enumerated item
   * @return Business of the enumeration item of the given name and type.
   */
  public static Business getBusinessForEnumeration(String enumType, String enumName)
  {
    // This will generate a localized message if the enumType is not valid.
    MdEnumerationDAOIF mdEnumerationIF = MdEnumerationDAO.getMdEnumerationDAO(enumType);

    if (mdEnumerationIF.isGenerateSource())
    {
      try
      {
        Class<?> clazz = LoaderDecorator.load(enumType);

        try
        {
          BusinessEnumeration enu = (BusinessEnumeration) clazz.getMethod("valueOf", String.class).invoke(null, enumName);

          return Business.get(enu.getOid());
        }
        catch (InvocationTargetException ite)
        {
          Throwable cause = ite.getCause();

          if (cause != null && ( cause instanceof IllegalArgumentException ))
          {
            String errMsg = "The enummeration name [" + enumName + "] is not valid for enumeration [" + enumType + "].";
            throw new InvalidEnumerationName(errMsg, enumName, mdEnumerationIF);
          }

          throw new ProgrammingErrorException(ite);
        }
      }
      catch (IllegalAccessException | NoSuchMethodException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }
    else
    {
      String enumMaster = mdEnumerationIF.getMasterListMdBusinessDAO().definesType();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(enumMaster, enumName);

      return BusinessFacade.get(item);
    }
  }

  /**
   * Returns the BusinessDTO of the enumeration item of the given name and type.
   * 
   * @param sesionId
   * @param enumType
   *          The type of the enumeration
   * @param enumName
   *          The name of the enumerated item
   * @return BusinessDTO of the enumeration item of the given name and type.
   */
  public static BusinessDTO getBusinessDTOForEnumeration(String sessionId, String enumType, String enumName)
  {
    Business business = getBusinessForEnumeration(enumType, enumName);

    return (BusinessDTO) FacadeUtil.populateComponentDTOIF(sessionId, business, true);
  }
}
