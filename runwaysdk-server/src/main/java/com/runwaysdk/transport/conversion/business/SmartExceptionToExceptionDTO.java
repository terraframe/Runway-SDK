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
package com.runwaysdk.transport.conversion.business;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.business.ExceptionDTO;
import com.runwaysdk.business.SmartException;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.attributes.AttributeEnumerationDTO;

public class SmartExceptionToExceptionDTO extends LocalizableToLocalizableDTO
{
  /**
   * Constructor to use when the Session parameter is to be converted into an
   * DTO. 
   * 
   * @param sessionId
   * @param smartException
   * @param convertMetaData
   */
  public SmartExceptionToExceptionDTO(String sessionId, SmartException smartException, boolean convertMetaData)
  {
    super(sessionId, smartException, convertMetaData);

    if (!GenerationUtil.isSkipCompileAndCodeGeneration(this.getMdTypeIF()))
    {
      this.setTypeSafe(true);
    }
  }

  /**
   * Returns the component that is being converted into a DTO.
   * @return component that is being converted into a DTO.
   */
  protected SmartException getComponentIF()
  {
    return (SmartException)super.getComponentIF();
  }
  
  /**
   * Creates and populates an ComponentDTO based on the provided ComponentIF
   * when this object was constructed. The created ComponentDTO is returned.
   * 
   * @return
   */
  public ExceptionDTO populate()
  {
    ExceptionDTO populate = (ExceptionDTO)super.populate();
    populate.setLocalizedMessage(getComponentIF().getMessage());

    String developerMessage = null;
    
    if (getComponentIF().getLocale() !=null)
    {
      populate.setLocalizedMessage(getComponentIF().localize(getComponentIF().getLocale()));

      SessionIF currentSession = Session.getCurrentSession();
      
      if (currentSession != null && currentSession.userHasRole(RoleDAOIF.DEVELOPER_ROLE))
      {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        getComponentIF().printStackTrace(printWriter);
        developerMessage = result.toString();
      }
      else
      {
        developerMessage = "";
      }
    }
    else
    {
      populate.setLocalizedMessage(""); 
      developerMessage = "";
    }

    populate.setDeveloperMessage(developerMessage);
    
    return populate;
  }

  /**
   * Returns true.
   * @return true.
   */
  protected boolean checkReadAccess()
  {
    return true;
  }  

  /**
   * Returns true.
   * @return true.
   */
  protected boolean checkWriteAccess()
  {
    return true;
  }

  /**
   * Returns true.
   * 
   * @param sessionId
   * @param mdAttribute
   *            The MdAttributeIF object to check for write permission on.
   * @return true.
   */
  protected boolean hasAttributeReadAccess(MdAttributeDAOIF mdAttribute)
  {
    return true;
  }
  
  /**
   * Returns true.
   * 
   * @param mdAttribute
   *            The MdAttributeIF object to check for write permission on.
   * @return true.
   */
  protected boolean hasAttributeWriteAccess(MdAttributeDAOIF mdAttribute)
  {
    return true;
  }
  
  @Override
  protected void setAttributeEnumerationNames(MdAttributeDAOIF mdAttributeIF, AttributeEnumerationDTO attributeEnumerationDTO)
  {
    BusinessFacade.getAttributeEnumerationNames(this.getComponentIF(), mdAttributeIF.definesAttribute(), attributeEnumerationDTO);
  }

  @Override
  protected ComponentDTOIF factoryMethod(Map<String, AttributeDTO> attributeMap, boolean newInstance,
      boolean readable, boolean writable, boolean modified)
  {
    return ComponentDTOFacade.buildExceptionDTO(null, this.getComponentIF().getType(), attributeMap, 
        newInstance, readable, writable, modified, this.getComponentIF().toString());
  }

}
