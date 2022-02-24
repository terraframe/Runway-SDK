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
/**
 * 
 */
package com.runwaysdk.transport.attributes;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.transport.metadata.AttributeMultiReferenceMdDTO;
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
public class AttributeMultiReferenceDTO extends AttributeDTO implements Serializable
{

  /**
   * 
   */
  private static final long serialVersionUID = 7142580499833208157L;

  private Set<String>       itemIdSet;

  /**
   * Constructor to create a new AttributeMultiReference object.
   * 
   * @param name
   * @param value
   * @param writable
   * @param modified
   */
  protected AttributeMultiReferenceDTO(String name, String value, boolean readable, boolean writable, boolean modified)
  {
    super(name, value, readable, writable, modified);

    this.itemIdSet = new HashSet<String>();
  }

  public void addItem(String oid)
  {
    if (isWritable())
    {
      addItemInternal(oid);
      setModified(true);
    }
  }

  void addItemInternal(String oid)
  {
    if (isReadable() || isWritable())
    {
      this.itemIdSet.add(oid);
    }
  }

  public void setValue(String value)
  {
    this.addItem(value);
  }

  public void removeItem(String oid)
  {
    if (isWritable())
    {
      this.itemIdSet.remove(oid);
      setModified(true);
    }
  }

  public void clear()
  {
    if (isWritable())
    {
      this.itemIdSet.clear();
      setModified(true);
    }
  }

  public List<String> getItemIds()
  {
    return new LinkedList<String>(this.itemIdSet);
  }

  /**
   * If this is called on the front end, it will return all BusinessDTOs for
   * which this object has an internal mapping. If this is called on the
   * back-end, it will only return an empty list. This method should not be
   * called from the server anyway. There is no need for a server method to get
   * the BusinessDTOs representing the enum items.
   * 
   * <br/>
   * <b>Precondition</b>: this.getContainingDTO().getRequest() != null<br/>
   * 
   * @return String list of items.
   */
  public List<? extends BusinessDTO> getItems()
  {
    ClientRequestIF clientRequest = this.getContainingDTO().getRequest();

    // This is being called from the server. This method should not be
    // called from the server anyway. There is no need for a server method
    // to get the BusinessDTOs representing the enum items.
    if (clientRequest == null)
    {
      return new LinkedList<BusinessDTO>();
    }
    // This is being called from the client
    else
    {
      List<BusinessDTO> list = new LinkedList<BusinessDTO>();
      for (String itemId : this.itemIdSet)
      {
        list.add((BusinessDTO) clientRequest.get(itemId));
      }

      return list;
    }

  }

  @Override
  public String getType()
  {
    return MdAttributeMultiReferenceInfo.CLASS;
  }

  @Override
  public AttributeDTO clone()
  {
    // set the attribute value and metadata
    AttributeMultiReferenceDTO clone = (AttributeMultiReferenceDTO) super.clone();
    CommonAttributeFacade.setMultiReferenceMetadata(this.getAttributeMdDTO(), clone.getAttributeMdDTO());

    clone.itemIdSet = new HashSet<String>(this.itemIdSet);

    return clone;
  }

  @Override
  public AttributeMultiReferenceMdDTO getAttributeMdDTO()
  {
    return (AttributeMultiReferenceMdDTO) super.getAttributeMdDTO();
  }
}