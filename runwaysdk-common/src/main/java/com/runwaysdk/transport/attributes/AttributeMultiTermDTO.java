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
/**
 * 
 */
package com.runwaysdk.transport.attributes;

import java.io.Serializable;

import com.runwaysdk.constants.MdAttributeMultiTermInfo;
import com.runwaysdk.transport.metadata.AttributeMultiTermMdDTO;
import com.runwaysdk.transport.metadata.CommonAttributeFacade;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
public class AttributeMultiTermDTO extends AttributeMultiReferenceDTO implements Serializable
{

  /**
   * 
   */
  private static final long serialVersionUID = 7142580499833208157L;

  /**
   * Constructor to create a new AttributeMultiTerm object.
   * 
   * @param name
   * @param value
   * @param writable
   * @param modified
   */
  protected AttributeMultiTermDTO(String name, String value, boolean readable, boolean writable, boolean modified)
  {
    super(name, value, readable, writable, modified);
  }

  @Override
  public String getType()
  {
    return MdAttributeMultiTermInfo.CLASS;
  }

  @Override
  public AttributeDTO clone()
  {
    // set the attribute value and metadata
    AttributeMultiTermDTO clone = (AttributeMultiTermDTO) super.clone();
    CommonAttributeFacade.setMultiTermMetadata(this.getAttributeMdDTO(), clone.getAttributeMdDTO());

    return clone;
  }

  @Override
  public AttributeMultiTermMdDTO getAttributeMdDTO()
  {
    return (AttributeMultiTermMdDTO) super.getAttributeMdDTO();
  }
}
