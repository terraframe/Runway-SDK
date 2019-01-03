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
package com.runwaysdk.transport.conversion.business;

import java.util.List;
import java.util.Locale;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.session.Session;
import com.runwaysdk.transport.conversion.ComponentIFtoComponentDTOIF;
import com.runwaysdk.transport.metadata.TypeMd;

public abstract class MutableToMutableDTO extends ComponentIFtoComponentDTOIF
{
  /**
   * The MdTypeIF that defines the entity.
   */
  private MdTypeDAOIF  mdTypeIF;

  /**
   * Constructor to use when the Entity parameter is to be converted into an
   * EntityDTO. This means that a BusinessDTO or RelationshipDTO will be
   * populated.
   *
   * @param sessionId
   * @param entity
   * @param convertMetaData
   */
  public MutableToMutableDTO(String sessionId, ComponentIF componentIF, boolean convertMetaData)
  {
    super(sessionId, componentIF, convertMetaData);

    // set the component metadata
    this.mdTypeIF = MdTypeDAO.getMdTypeDAO(componentIF.getType());
  }

  /**
   * Returns the component that is being converted into a DTO.
   * @return component that is being converted into a DTO.
   */
  protected Mutable getComponentIF()
  {
    return (Mutable)super.getComponentIF();
  }

  /**
   * Returns the MdTypeIF that defines the componentIF type.
   * @return MdTypeIF that defines the componentIF type.
   */
  protected MdTypeDAOIF getMdTypeIF()
  {
    return this.mdTypeIF;
  }
  
  /**
   * The Mutable object has been modified if any of its attributes have been modified.
   */
  protected boolean getIsModified()
  {
    Mutable mutable = this.getComponentIF();
    
    List<? extends MdAttributeDAOIF> definedAttributes = this.getDefinedMdAttributes();
    for (MdAttributeDAOIF mdAttribute : definedAttributes)
    {
      // skip special or non-visible attributes
      if (mdAttribute.getGetterVisibility() != VisibilityModifier.PUBLIC)
        continue;
      
      if(mutable.isModified(mdAttribute.definesAttribute()))
      {
        return true;
      }
    }
    
    return false;
  }
  
  @Override
  protected boolean getIsModified(String name)
  {
    return this.getComponentIF().isModified(name);
  }

  /**
   * Creates and populates an MutableDTO based on the provided ComponentIF
   * when this object was constructed. The created MutableDTO is returned.
   *
   * @return
   */
  public MutableDTO populate()
  {
    // Developers can programatically throw exceptions to prevent reads on objects.
    this.getComponentIF().customReadCheck();

    MutableDTO mutableDTO = (MutableDTO)super.populate();

    TypeMd typeMd = this.createTypeMd();
    mutableDTO.setMd(typeMd);

    return mutableDTO;
  }

  /**
   * Returns a new TypeMd object.
   *
   * @return
   */
  protected TypeMd createTypeMd()
  {
    if (this.convertMetaData())
    {
      Locale locale = Session.getCurrentLocale();

      return new TypeMd(this.getMdTypeIF().getDisplayLabel(locale), this.getMdTypeIF().getDescription(locale), this.getMdTypeIF().getOid(), this.getMdTypeIF().isGenerateSource());
    }
    else
    {
      return new TypeMd();
    }
  }


  /**
   * Returns all MdAttributes that are defined by the type of the object being converted.
   * @return MdAttributes that are defined by the type of the object being converted.
   */
  protected List<? extends MdAttributeDAOIF> getDefinedMdAttributes()
  {
    MdClassDAOIF mdClassIF = MdClassDAO.getMdClassDAO(this.getComponentIF().getType());

    // add the attributes
    return mdClassIF.getAllDefinedMdAttributes();
  }

}
