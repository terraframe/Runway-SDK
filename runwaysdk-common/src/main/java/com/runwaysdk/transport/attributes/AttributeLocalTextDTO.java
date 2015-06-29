/**
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
 */
package com.runwaysdk.transport.attributes;

import java.io.Serializable;

import com.runwaysdk.constants.MdAttributeLocalTextInfo;
import com.runwaysdk.transport.metadata.AttributeLocalTextMdDTO;

public class AttributeLocalTextDTO extends AttributeLocalDTO implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -9163731399501890398L;

  /**
   * Constructor
   * 
   * @param name
   * @param value
   * @param readable
   * @param writable
   * @param modified
   */
  protected AttributeLocalTextDTO(String name, String value, boolean readable, boolean writable, boolean modified)
  {
    super(name, value, readable, writable, modified);
  }

  @Override
  public String getType()
  {
    return MdAttributeLocalTextInfo.CLASS;
  }

  /**
   * Returns a shallow clone of this attribute struct.
   */
  @Override
  public AttributeLocalTextDTO clone()
  {
    return (AttributeLocalTextDTO) super.clone();
  }

  @Override
  public AttributeLocalTextMdDTO getAttributeMdDTO()
  {
    return (AttributeLocalTextMdDTO) super.getAttributeMdDTO();
  }
}
