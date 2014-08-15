/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.business.ontology;

import java.util.Map;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.LocalStructDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.system.ontology.TermUtilDTO;
import com.runwaysdk.transport.attributes.AttributeDTO;

public class TermDTO extends BusinessDTO
{
  private static final long serialVersionUID = -3281742783423272733L;
  
  public static final String CLASS = "com.runwaysdk.business.ontology.TermDTO";
  
  protected TermDTO(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  public TermDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap)
  {
    super(clientRequest, type, attributeMap);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected TermDTO(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  /**
   * Convenience method, delegates to TermUtil.
   * 
   * @param relationshipType
   * @return
   */
  public TermDTO[] getAllAncestors(String[] relationshipTypes)
  {
    return TermUtilDTO.getAllAncestors(this.getRequest(), this.getId(), relationshipTypes);
  }
  
  /**
   * Convenience method, delegates to TermUtil.
   * 
   * @param relationshipType
   * @return
   */
  public TermAndRelDTO[] getDirectDescendants(String[] relationshipTypes)
  {
    return TermUtilDTO.getDirectDescendants(this.getRequest(), this.getId(), relationshipTypes);
  }
  
  /**
   * Convenience method, delegates to TermUtil.
   * 
   * @param relationshipType
   * @return
   */
  public TermAndRelDTO[] getDirectAncestors(String[] relationshipTypes)
  {
    return TermUtilDTO.getDirectAncestors(this.getRequest(), this.getId(), relationshipTypes);
  }
  
  /**
   * Convenience method, delegates to TermUtil.
   * 
   * @param relationshipType
   * @return
   */
  public TermDTO[] getAllDescendants(String[] relationshipTypes)
  {
    return TermUtilDTO.getAllDescendants(this.getRequest(), this.getId(), relationshipTypes);
  }
  
  public LocalStructDTO getDisplayLabel() {
    return (LocalStructDTO) this.getAttributeStructDTO(MdTermInfo.DISPLAY_LABEL).getStructDTO();
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  @Override
  public String toString() {
    return this.getStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE);
  }
}
