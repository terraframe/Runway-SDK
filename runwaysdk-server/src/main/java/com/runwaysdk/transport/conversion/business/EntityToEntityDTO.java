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
package com.runwaysdk.transport.conversion.business;

import java.util.Map;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.Entity;
import com.runwaysdk.business.EntityDTO;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.session.PermissionFacade;
import com.runwaysdk.session.SessionFacade;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.attributes.AttributeEnumerationDTO;

public abstract class EntityToEntityDTO extends MutableToMutableDTO
{

  /**
   * Constructor to use when the Entity parameter is to be converted into an
   * EntityDTO. This means that a BusinessDTO or RelationshipDTO will be
   * populated.
   * 
   * @param sessionId
   * @param entity
   * @param convertMetaData
   */
  public EntityToEntityDTO(String sessionId, Entity entity, boolean convertMetaData)
  {
    super(sessionId, entity, convertMetaData);

    if (!GenerationUtil.isReservedType(this.getMdTypeIF()))
    {
      // Refresh the Class. If the transaction created a new class loader
      // instance (as the result of
      // defining/modifying metadata) then this Class is tied to the old class
      // loader. We need to
      // reinstantiate it, otherwise reflection will cause an error later on, as
      // the new class loader
      // will load up a new Class vs. the one that this object is an instance
      // of.
      this.setTypeSafe(true);

      EntityDAOIF entityDAOIF = BusinessFacade.getEntityDAO(entity);
      this.setComponetIF(BusinessFacade.get(entityDAOIF));
    }
  }

  /**
   * Returns the MdEntityIF that defines the componentIF type.
   * 
   * @return MdEntityIF that defines the componentIF type.
   */
  protected MdEntityDAOIF getMdTypeIF()
  {
    return (MdEntityDAOIF) super.getMdTypeIF();
  }

  /**
   * Returns the entity that is being converted into a DTO.
   * 
   * @return entity that is being converted into a DTO.
   */
  protected Entity getComponentIF()
  {
    return (Entity) super.getComponentIF();
  }

  /**
   * Sets enumeration item names on the given
   * <code>AttributeEnumerationDTO</code>
   * 
   * @param mdAttributeIF
   * @param attributeEnumerationDTO
   */
  protected void setAttributeEnumerationNames(MdAttributeDAOIF mdAttributeIF, AttributeEnumerationDTO attributeEnumerationDTO)
  {
    BusinessFacade.getAttributeEnumerationNames(this.getComponentIF(), mdAttributeIF.definesAttribute(), attributeEnumerationDTO);
  }

  /**
   * Instantiates the proper entityDTO class (not type safe)
   * 
   * @param sessionId
   * @param type
   * @param attributeMap
   * @param newInstance
   * @param readable
   * @param writable
   * @param modified
   * @return proper entityDTO class (not type safe)
   */
  protected abstract EntityDTO factoryMethod(Map<String, AttributeDTO> attributeMap, boolean newInstance, boolean readable, boolean writable, boolean modified);

  /**
   * Returns true if the user bound to the session has permission to read this
   * object, false otherwise.
   * 
   * @return false if the user bound to the session has permission to read this
   *         object, false otherwise.
   */
  protected boolean checkReadAccess()
  {
    return PermissionFacade.checkReadAccess(this.getSessionId(), this.getComponentIF());
  }

  /**
   * Returns true if the user bound to the session has permission to write to
   * this object, false otherwise.
   * 
   * @return false if the user bound to the session has permission to write to
   *         this object, false otherwise.
   */
  protected boolean checkWriteAccess()
  {
    return SessionFacade.checkAccess(this.getSessionId(), Operation.WRITE, this.getComponentIF());
  }

  /**
   * Checks permissions on an attribute to see if it can be read or not.
   * 
   * @param sessionId
   * @param mdAttribute
   *          The MdAttributeIF object to check for write permission on.
   * @return true if the attribute can be read, false otherwise.
   */
  protected boolean hasAttributeReadAccess(MdAttributeDAOIF mdAttribute)
  {
    return PermissionFacade.checkAttributeReadAccess(this.getSessionId(), this.getComponentIF(), mdAttribute);
  }

  /**
   * Checks permissions on an attribute to see if it can be edited or not.
   * 
   * @param mdAttribute
   *          The MdAttributeIF object to check for write permission on.
   * @return true if attribute can be edited, false otherwise.
   */
  protected boolean hasAttributeWriteAccess(MdAttributeDAOIF mdAttribute)
  {
    return SessionFacade.checkAttributeAccess(this.getSessionId(), Operation.WRITE, this.getComponentIF(), mdAttribute);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.transport.conversion.business.MutableToMutableDTO#populate()
   */
  @Override
  public MutableDTO populate()
  {
    EntityDTO entity = (EntityDTO) super.populate();
    entity.setDisconnected(this.getComponentIF().isDisconnected());

    return entity;
  }

}
