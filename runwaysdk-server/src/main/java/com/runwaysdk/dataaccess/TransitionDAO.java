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
package com.runwaysdk.dataaccess;

import java.util.Map;

import com.runwaysdk.business.state.MdStateMachineDAOIF;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.database.RelationshipDAOFactory;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;

public class TransitionDAO extends GraphDAO implements TransitionDAOIF, SpecializedDAOImplementationIF
{
  /**
   *
   */
  private static final long serialVersionUID = 3272834691554406615L;

  /**
   *
   */
  public TransitionDAO(String parentId, String childId, Map<String, Attribute> attributeMap, String relationshipType)
  {
    super(parentId, childId, attributeMap, relationshipType);
  }

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getDisplayLabes()
  {
    return ((AttributeLocalIF)this.getAttributeIF(MdTypeInfo.DISPLAY_LABEL)).getLocalValues();
  }

  /**
   * Returns the signature of the metadata.
   *
   * @return signature of the metadata.
   */
  public String getSignature()
  {
    return this.getQualifiedName();
  }

  @Override
  public StateMasterDAOIF getParent()
  {
    return (StateMasterDAOIF) super.getParent();
  }

  @Override
  public StateMasterDAOIF getChild()
  {
    return (StateMasterDAOIF) super.getChild();
  }

  @Override
  public String apply()
  {
    if(this.isNew())
    {
      MdRelationshipDAOIF mdRelationship = MdRelationshipDAO.getMdRelationshipDAO(this.getType());
      MdStateMachineDAOIF mdStateMachine = (MdStateMachineDAOIF) mdRelationship.getParentMdBusiness();

      String key = TransitionDAO.buildKey(mdStateMachine.definesType(), this.getName());
      this.setKey(key);
    }
    return super.apply();
  }

  /**
   * This is a hook method for aspects.
   */
  public String save(boolean save)
  {
    return super.save(save);
  }

  /**
   * This is a hook method for aspects.
   */
  public void delete()
  {
    super.delete();
  }

  /**
   * Returns the name of the transition.
   * @return name of the transition.
   */
  public String getName()
  {
    return this.getAttributeIF(StateMasterDAOIF.TRANSITION_NAME).getValue();
  }

  /**
   *
   * @param transitionName
   */
  public void setName(String transitionName)
  {
    this.getAttribute(StateMasterDAOIF.TRANSITION_NAME).setValue(transitionName);
  }

  /**
   * Returns the fully qualified name of the transition, including the type and the name of the transition.
   * @return fully qualified name of the transition, including the type and the name of the transition.
   */
  public String getQualifiedName()
  {
    return this.getType()+"."+this.getName();
  }

  /**
   *
   */
  public int hashCode()
  {
    return this.getQualifiedName().hashCode();
  }

  /**
   *
   * @param relationshipType
   * @return
   */
  public static TransitionDAO newInstance(String parentId, String childId, String relationshipType)
  {
    return (TransitionDAO)RelationshipDAOFactory.newInstance(parentId, childId, relationshipType);
  }

  /**
   * Returns a deep cloned-copy of this Relationshipo
   */
  public TransitionDAO getRelationshipDAO()
  {
    return (TransitionDAO)super.getRelationshipDAO();
  }

  public static String buildKey(String mdStateMachineType, String transitionName)
  {
    return mdStateMachineType + "." + transitionName;
  }
}
