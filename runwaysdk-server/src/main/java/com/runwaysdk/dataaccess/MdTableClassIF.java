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
package com.runwaysdk.dataaccess;

import java.util.List;
import java.util.Map;

/**
 * Metadata classes that implement this interface reference a database table.
 * 
 * @author nathan
 *
 */
public interface MdTableClassIF extends MdClassDAOIF
{
  /**
   * Returns the name of the table that this metadata represents.
   * @return name of the table that this metadata represents.
   */
  public String getTableName();
  
  /**
   * Returns an {@link MdTableClassIF} representing the super class of this class, or null if
   * it does not have one.
   *
   * @return an  {@link MdTableClassIF} representing the super class of this class, or null if
   * it does not have one.
   */
  public MdTableClassIF getSuperClass();
  
  /**
   * Returns an array of {@link MdTableClassIF}  that defines immediate subclasses of this class.
   * @return an array of {@link MdTableClassIF}  that defines immediate subclasses of this class.
   */
  public List<? extends MdTableClassIF> getSubClasses();

  /**
   * Returns a map of {@link MdAttributeConcreteDAOIF} objects defined by this entity.
   * Key: attribute name in lower case Value: {@link MdAttributeConcreteDAOIF} 
   * @return map of {@link MdAttributeConcreteDAOIF} objects defined by this entity.
   */
  public Map<String, ? extends MdAttributeConcreteDAOIF> getDefinedMdAttributeMap();
  
  /**
   * Returns a complete list of {@link MdAttributeConcreteDAOIF} objects for this
   * instance of {@link MdTableDAOIF}. This list includes attributes inherited
   * from supertypes.
   *
   * @return a list of MdAttributeIF objects
   */
  public List<? extends MdAttributeConcreteDAOIF> getAllDefinedMdAttributes();
  

  /**
   * Returns a map of {@link MdAttributeConcreteDAOIF}  objects defined by the given entity type plus all
   * attributes defined by parent entities.  The key of the map is the attribute name
   * in lower case.
   *
   * @param  type Name of the entity
   * @return map of {@link MdAttributeConcreteDAOIF}  objects defined by the given entity type plus all
   * attributes defined by parent entities.
   */
  public Map<String, ? extends MdAttributeConcreteDAOIF> getAllDefinedMdAttributeMap();

  /**
   * Returns a map of {@link MdAttributeConcreteDAOIF} objects defined by this entity type plus all
   * attributes defined by parent entities.
   * <p/>
   * <br/>Map Key: mdAttributeID
   * <br/>Map Value: {@link MdAttributeConcreteDAOIF} 
   * <p/>
   * @return map of {@link MdAttributeConcreteDAOIF} objects defined by this entity type plus all
   * attributes defined by parent entities.
   */
  public Map<String, ? extends MdAttributeConcreteDAOIF> getAllDefinedMdAttributeIDMap();


}
