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

import java.util.List;
import java.util.Map;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.ValueObjectDTO;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.sql.MdAttributeConcrete_SQL;
import com.runwaysdk.session.PermissionFacade;
import com.runwaysdk.transport.attributes.AttributeBooleanDTO;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.attributes.AttributeDTOFactory;
import com.runwaysdk.transport.attributes.AttributeEnumerationDTO;
import com.runwaysdk.transport.metadata.AttributeBooleanMdDTO;
import com.runwaysdk.transport.metadata.ServerAttributeFacade;

public class ValueObjectToValueObjectDTO extends ComponentIFtoComponentDTOIF
{

  private Boolean              hasTypeReadAccess;

  // Key: MdAttribute id Value: indicates if the user has read permission on the
  // attribute.
  private Map<String, Boolean> attrReadPermissionMap;

  /**
   * Constructor to use when the ComponentIF parameter is to be converted into
   * an ComponentDTOIF.
   * 
   * @param sessionId
   * @param componentIF
   */
  public ValueObjectToValueObjectDTO(String sessionId, ValueObject valueObject)
  {
    super(sessionId, valueObject, false);
    this.hasTypeReadAccess = null;
    this.attrReadPermissionMap = null;
  }

  /**
   * Constructor to use when the ComponentIF parameter is to be converted into
   * an ComponentDTOIF.
   * 
   * @param sessionId
   * @param componentIF
   * @param hasTypeReadAccess
   *          true if the user has read access on all of the type involved in
   *          the ValueObject.
   * @param attrReadPermissionMap
   */
  public ValueObjectToValueObjectDTO(String sessionId, ComponentIF componentIF, Boolean hasTypeReadAccess, Map<String, Boolean> attrReadPermissionMap)
  {
    super(sessionId, componentIF, false);
    this.hasTypeReadAccess = hasTypeReadAccess;
    this.attrReadPermissionMap = attrReadPermissionMap;
  }

  /**
   * Returns the component that is being converted into a DTO.
   * 
   * @return component that is being converted into a DTO.
   */
  protected ValueObject getComponentIF()
  {
    return (ValueObject) super.getComponentIF();
  }

  /**
   * Creates and populates an ValueObjectDTO based on the provided ValueObject
   * when this object was constructed. The created ValueObjectDTO is returned.
   * 
   * @return
   */
  public ValueObjectDTO populate()
  {
    return (ValueObjectDTO) super.populate();
  }

  /**
   * Returns all MdAttributes that are defined by the type of the object being
   * converted.
   * 
   * @return MdAttributes that are defined by the type of the object being
   *         converted.
   */
  protected List<? extends MdAttributeDAOIF> getDefinedMdAttributes()
  {
    ValueObject valueObject = this.getComponentIF();

    return valueObject.getMdAttributeDAOs();
  }

  @Override
  protected boolean checkReadAccess()
  {
    if (hasTypeReadAccess != null)
    {
      return this.hasTypeReadAccess;
    }
    else
    {
      return PermissionFacade.checkTypeReadAccess(this.getSessionId(), this.getComponentIF());
    }
  }

  // ValueObjects cannot be written to.
  @Override
  protected boolean checkWriteAccess()
  {
    return false;
  }

  @Override
  protected boolean hasAttributeReadAccess(MdAttributeDAOIF mdAttribute)
  {
    if (this.attrReadPermissionMap != null)
    {
      // Custom SQL attributes always have permission
      if (mdAttribute instanceof MdAttributeConcrete_SQL)
      {
        return true;
      }

      Boolean hasAttributePermission = this.attrReadPermissionMap.get(mdAttribute.getId());
      if (hasAttributePermission != null)
      {
        return hasAttributePermission;
      }
      else
      {
        return false;
      }
    }
    else
    {
      return PermissionFacade.checkAttributeTypeReadAccess(this.getSessionId(), mdAttribute);
    }
  }

  // Value objects cannot be written to.
  @Override
  protected boolean hasAttributeWriteAccess(MdAttributeDAOIF mdAttribute)
  {
    return false;
  }

  @Override
  protected void setAttributeEnumerationNames(MdAttributeDAOIF mdAttributeIF, AttributeEnumerationDTO attributeEnumerationDTO)
  {
    BusinessFacade.getAttributeEnumerationNames(this.getComponentIF(), mdAttributeIF.definesAttribute(), attributeEnumerationDTO);
  }

  /**
   * Instantiates ValueObjectDTO
   * 
   * @param attributeMap
   * @param newInstance
   * @param readable
   * @param writable
   * @param modified
   * @return ValueObjectDTO
   */
  protected ValueObjectDTO factoryMethod(Map<String, AttributeDTO> attributeMap, boolean newInstance, boolean readable, boolean writable, boolean modified)
  {
    return ComponentDTOFacade.buildValueObjectDTO(null, this.getComponentIF().getType(), attributeMap, readable);
  }

  /**
   * Converts the given query selectable into an attributeDTO.
   * 
   * @param selectable
   * @return given query selectable into an attributeDTO.
   */
  public static AttributeDTO convertSelectable(String sessionId, Selectable selectable)
  {
    MdAttributeDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    boolean readable = PermissionFacade.checkAttributeTypeReadAccess(sessionId, mdAttributeIF);

    String name = selectable.getMdAttributeIF().definesAttribute();
    // the value must be an empty string because default values don't make sense
    // for ValueObjects
    AttributeDTO attributeDTO = AttributeDTOFactory.createAttributeDTO(name, mdAttributeIF.getMdAttributeConcrete().getType(), "", readable, false, false);

    // only set the most basic attribute metadata on the AttributeDTO (we're
    // primarily interested in the display label)
    // FIXME use factory method to set attribute metadata per specific type
    if (attributeDTO instanceof AttributeBooleanDTO)
    {
      ServerAttributeFacade.setBooleanMetadata(mdAttributeIF, (AttributeBooleanMdDTO) attributeDTO.getAttributeMdDTO());
    }
    else
    {
      ServerAttributeFacade.setAttributeMetadata(mdAttributeIF, attributeDTO.getAttributeMdDTO());
    }

    return attributeDTO;
  }

  @Override
  protected boolean getIsModified()
  {
    // ValueObjects are never modified or persisted.
    return false;
  }

  protected boolean getIsModified(String name)
  {
    // ValueObjects are never modified or persisted.
    return false;
  }

}
