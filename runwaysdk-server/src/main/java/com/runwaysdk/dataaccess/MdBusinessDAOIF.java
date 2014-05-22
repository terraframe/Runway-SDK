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
/**
 * Created on Aug 13, 2005
 *
 */
package com.runwaysdk.dataaccess;

import java.util.List;

import com.runwaysdk.business.state.MdStateMachineDAOIF;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;


/**
 * @author nathan
 *
 */
public interface MdBusinessDAOIF extends MdElementDAOIF
{
  /**
   * Name of the table used to store intances of this class.
   */
  public static final String TABLE                     = "md_business";

  /**
   * Returns true if this class defines an enumeration master list (a subclass of the EnumerationMaster), false otherwise.
   * @return true if this class defines an enumeration master list (a subclass of the EnumerationMaster), false otherwise.
   */
  public boolean isEnumerationMasterMdBusiness();

  /**
   * Returns a list of all MdEnumerations that use this class to define the master list of options.
   * @return list of all MdEnumerations that use this class to define the master list of options.
   */
  public List<MdEnumerationDAOIF> getMdEnumerationDAOs();

  /**
   * Returns an array of MdBusinessIF that defines immediate subentites of this
   * entity.
   *
   * @return an array of MdBusinessIF that defines immediate subentites of this
   *         entity.
   */
  public List<MdBusinessDAOIF> getSubClasses();

  /**
   * Returns a list of MdBusinessIF objects that represent entites that are
   * subclasses of the given entity, including all recursive entities.
   *
   * @return list of MdBusinessIF objects that represent entites that are
   *         subclasses of the given entity, including all recursive entities.
   */
  public List<MdBusinessDAOIF> getAllSubClasses();

  /**
   * Returns a list of MdBusinessIF objects that are subclasses of the given
   * entity. Only non abstract entities are returned (i.e. entities that can be
   * instantiated)
   *
   * @return list of MdBusinessIF objects that are subclasses of the given entity.
   *         Only non abstract entities are returned (i.e. entities that can be
   *         instantiated)
   */
  public List<MdBusinessDAOIF> getAllConcreteSubClasses();

  /**
   * Returns an MdBusinessIF representing the super entity of this entity, or null if
   * it does not have one.
   *
   * @return an MdBusinessIF representing the super entity of this entity, or null if
   * it does not have one.
   */
  public MdBusinessDAOIF getSuperClass();

  /**
   * Returns a list of MdBusinessIF object representing every parent of this
   * MdBusinessIF partaking in an inheritance relationship, including this class.
   *
   * @return a list of parent MdBusinessIF objects.
   */
  public List<MdBusinessDAOIF> getSuperClasses();

  /**
   * Returns a list of MdRelationships that reference this class for the child objects.
   *
   * @return list of MdRelationships that reference this class for the child objects.
   */
  public List<MdRelationshipDAOIF> getChildMdRelationships();

  /**
   * Returns a list of MdRelationships that reference this class for the child objects, ordered by
   * the name of the parent method.
   *
   * @return list of MdRelationships that reference this class for the child objects, ordered by
   * the name of the parentMethod.
   */
  public List<MdRelationshipDAOIF> getChildMdRelationshipsOrdered();

  /**
   * Returns a list of MdRelationships that reference this class for the parent objects.
   *
   * @return list of MdRelationships that reference this class for the parent objects.
   */
  public List<MdRelationshipDAOIF> getParentMdRelationships();

  /**
   * Returns a list of MdRelationships that reference this class for the parent objects, ordered by
   * the child method.
   *
   * @return list of MdRelationships that reference this class for the parent objects, ordered by
   * the child method.
   */
  public List<MdRelationshipDAOIF> getParentMdRelationshipsOrdered();

  /**
   * Returns a list of MdRelationships that reference this class or a superclass for the child objects.
   *
   * @return list of MdRelationships that reference this class or a superclass for the child objects.
   */
  public List<MdRelationshipDAOIF> getAllChildMdRelationships();

  /**
   * Returns a list of MdRelationships that reference this class or a superclass for the parent objects.
   *
   * @return list of MdRelationships that reference this class or a superclass for the parent objects.
   */
  public List<MdRelationshipDAOIF> getAllParentMdRelationships();

  /**
   * Returns reference attributes that reference this type either directly or indirectly (via inheritance) reference the given type.
   *
   * @param mdBusinessDAOIF
   * @return {@link List} of {@link MdAttributeReferenceDAOIF}
   */
  public List<MdAttributeReferenceDAOIF> getAllReferenceAttributes();

  /**
   * Returns the MdState which this MdBusiness owns
   *
   * @return The MdState in which this class is the owner of
   */
  public MdStateMachineDAOIF definesMdStateMachine();

  /**
   * Checks if this class definition owns a state machine
   *
   * @return <code>true</code> if this
   */
  public boolean hasStateMachine();

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdBusinessDAO getBusinessDAO();
}
