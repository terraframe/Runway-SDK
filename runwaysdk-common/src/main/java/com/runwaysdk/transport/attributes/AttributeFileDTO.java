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
package com.runwaysdk.transport.attributes;

import com.runwaysdk.constants.MdAttributeFileInfo;
import com.runwaysdk.transport.metadata.AttributeFileMdDTO;
import com.runwaysdk.transport.metadata.CommonAttributeFacade;

public class AttributeFileDTO extends AttributeDTO
{
  /**
   * 
   */
  private static final long serialVersionUID = 3645891385059100013L;

  protected AttributeFileDTO(String name, String value, boolean readable, boolean writable, boolean modified)
  {
    super(name, value, readable, writable, modified);
  }

  @Override
  public AttributeDTO clone()
  {
    // set the attribute values and metadata
    AttributeFileDTO clone = new AttributeFileDTO(getName(), getValue(), isReadable(), isWritable(), isModified());
    CommonAttributeFacade.setAttributeMetadata(this.getAttributeMdDTO(), clone.getAttributeMdDTO());
    
    return clone;
  }

  @Override
  public String getType()
  {
    return MdAttributeFileInfo.CLASS;
  }
  
  @Override
  public AttributeFileMdDTO getAttributeMdDTO()
  {
    return (AttributeFileMdDTO) super.getAttributeMdDTO();
  }
}
