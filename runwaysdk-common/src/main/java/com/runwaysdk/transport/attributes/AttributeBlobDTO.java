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
package com.runwaysdk.transport.attributes;

import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.transport.metadata.AttributeBlobMdDTO;
import com.runwaysdk.transport.metadata.CommonAttributeFacade;
import com.runwaysdk.util.Base64;

public class AttributeBlobDTO extends AttributeDTO
{
  /**
   * 
   */
  private static final long serialVersionUID = -6029565701260422896L;

  protected AttributeBlobDTO(String name, String value, boolean readable, boolean writable, boolean modified)
  {
    super(name, value, readable, writable, modified);
  } 
  
  /**
   * Returns the blob value as a byte array.
   * 
   * @return
   */
  public byte[] getBlob()
  {
    String value = super.getValue();
    if(value.length() != 0)
      return Base64.decode(value);
    else
      return ComponentDTOIF.EMPTY_VALUE.getBytes();
  }
  
  /**
   * Returns the value of the blob as a string.
   */
  public String getValue()
  {
    return new String(getBlob());
  }
  
  /**
   * Sets the blob value
   */
  public void setValue(String value)
  {
    super.setValue(Base64.encodeToString(value.getBytes(), false));
  }
  
  /**
   * Sets the blob value.
   * 
   * @param value
   */
  public void setValue(byte[] value)
  {
    super.setValue(Base64.encodeToString(value, false));
  }
  
  public String getType()
  {
    return MdAttributeBlobInfo.CLASS;
  }

  @Override
  public AttributeDTO clone()
  {
	AttributeBlobDTO clone = (AttributeBlobDTO) super.clone();
	
    clone.setValue(getBlob());
    
    CommonAttributeFacade.setAttributeMetadata(this.getAttributeMdDTO(), clone.getAttributeMdDTO());
    
    return clone;
  }
  
  @Override
  public AttributeBlobMdDTO getAttributeMdDTO()
  {
    return (AttributeBlobMdDTO) super.getAttributeMdDTO();
  }
}
