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

import com.runwaysdk.dataaccess.metadata.MdClassDAO;

public interface MdClassDAOIF extends MdTypeDAOIF
{
  /**
   * Name of the table used to store intances of this class.
   */
  public static final String TABLE   = "md_class";

  /**
   * Column name of the attribute that stores the stub .class blob.
   */
  public static final String STUB_CLASS_COLUMN         = "stub_class";

  /**
   * Column name of the attribute that stores the stub .java clob.
   */
  public static final String STUB_SOURCE_COLUMN        = "stub_source";

  /**
   * Column name of the attribute that stores the dto stub .class blob
   */
  public static final String DTO_STUB_CLASS_COLUMN     = "stub_dto_class";

  /**
   * Column name of the attribute that stores the dto stub .java clob
   */
  public static final String DTO_STUB_SOURCE_COLUMN    = "stub_dto_source";

  /**
   * Returns true if the class is published, false otherwise.
   * A published class has a representation in the DTO layer.
   * @return true if the class is published, false otherwise.
   */
  public boolean isPublished();

  /**
   * Returns true if the type is abstract, false otherwise.
   *
   * @return true if the type is abstract, false otherwise.
   */
  public boolean isAbstract();

  /**
   * Returns true if the type can be extended, false otherwise.
   * @return true if the type can be extended, false otherwise.
   */
  public boolean isExtendable();

  /**
   * Returns the MdMethodIF defined by this MdEntity with the given
   * method name.
   *
   * @param methodName The name of the method
   * @return
   */
  public MdMethodDAOIF getMdMethod(String methodName);

  /**
   * Returns a list of all MdMethods defined by this MdEntity and its
   * super entities.
   */
  public List<MdMethodDAOIF> getAllMdMethods();

  /**
   * Returns an array of MdClassIF that defines immediate subclasses of this class.
   * @return an array of MdClassIF that defines immediate subclasses of this class.
   */
  public List<? extends MdClassDAOIF> getSubClasses();

  /**
   *Returns a list of MdClassIF objects that represent entites
   * that are subclasses of the given entity, including all recursive entities.
   *
   * @return list of MdClassIF objects that represent entites
   * that are subclasses of the given entity, including all recursive entities.
   */
  public List<? extends MdClassDAOIF> getAllSubClasses();

  /**
   *Returns a list of MdClassIF objects that are subclasses of the given class, including all recursive
   * entities. Only non abstract class are returned (i.e. class that can be instantiated)
   *
   * @return list of MdClassIF objects
   * that are subclasses of the given class.  Only non abstract entities
   * are returned (i.e. classes that can be instantiated)
   */
  public List<? extends MdClassDAOIF> getAllConcreteSubClasses();

  /**
   * Returns an MdClassIF representing the super class of this class, or null if
   * it does not have one.
   *
   * @return an MdClassIF representing the super class of this class, or null if
   * it does not have one.
   */
  public MdClassDAOIF getSuperClass();

  /**
   * Returns a list of MdClassIF instances representing every
   * parent of this MdClassIF partaking in an inheritance relationship.
   *
   * @return a list of MdClassIF instances that are parents of this class.
   */
  public List<? extends MdClassDAOIF> getSuperClasses();

  /**
   *Returns the MdClassIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   *
   * @return MdClassIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   */
  public MdClassDAOIF getRootMdClassDAO();

  /**
   * Returns true if this class is the root class of a hierarchy, false otherwise.
   * @return true if this class is the root class of a hierarchy, false otherwise.
   */
  public boolean isRootOfHierarchy();

  /**
   * Returns a sorted list of <code>MdAttributeDAOIF</code> objects that this <code>MdClassDAOIF</code> defines.
   *
   * @return an List of <code>MdAttributeDAOIF</code> objects that this <code>MdClassDAOIF</code> defines.
   */
  public List<? extends MdAttributeDAOIF> definesAttributes();

  /**
   * Returns a sorted list of <code>MdAttributeDAOIF</code> objects that this <code>MdClassDAOIF</code> defines.
   * The list is sorted by the alphabetical order of the attribute names
   *
   * @return an List of <code>MdAttributeDAOIF</code> objects that this <code>MdClassDAOIF</code> defines.
   */
  public List<? extends MdAttributeDAOIF> definesAttributesOrdered();

  /**
   * Returns the MdAttribute that defines the given attribute for the this entity.  This method
   * only works if the attribute is explicitly defined by the this.  In other words, it
   * will return null if the attribute exits for the given entity, but is inherited from a
   * super entity.
   *
   * <br/><b>Precondition:</b>   attributeName != null
   * <br/><b>Precondition:</b>   !attributeName.trim().equals("")
   */
  public MdAttributeDAOIF definesAttribute(String attributeName);

  /**
   *Returns true if this entity defines the given attribute, false otherwise.
   * @return true if this entity defines the given attribute, false otherwise.
   */
  public boolean definesAttribute(MdAttributeDAOIF mdAttributeIF);

  /**
   * Returns a complete list of {@link MdAttributeDAOIF} objects for this
   * instance of MdBusiness. This list includes attributes inherited
   * from supertypes.
   *
   * @return a list of {@link MdAttributeDAOIF} objects
   */
  public List<? extends MdAttributeDAOIF> getAllDefinedMdAttributes();

  /**
   * Returns a map of {@link MdAttributeDAOIF} objects defined by this entity.
   * Key: attribute name in lower case Value: {@link MdAttributeDAOIF} 
   * @return map of {@link MdAttributeDAOIF} objects defined by this entity.
   */
  public Map<String, ? extends MdAttributeDAOIF> getDefinedMdAttributeMap();

  /**
   * Returns a map of {@link MdAttributeDAOIF} objects defined by this entity type plus all
   * attributes defined by parent entities.
   * <p/>
   * <br/>Map Key: mdAttributeID
   * <br/>Map Value: {@link MdAttributeDAOIF} 
   * <p/>
   * @return map of {@link MdAttributeDAOIF} objects defined by this entity type plus all
   * attributes defined by parent entities.
   */
  public Map<String, ? extends MdAttributeDAOIF> getAllDefinedMdAttributeIDMap();

  /**
   * Returns a map of {@link MdAttributeDAOIF} objects defined by the given entity type plus all
   * attributes defined by parent entities.  The key of the map is the attribute name
   * in lower case.
   *
   * @param  type Name of the entity
   * @return map of {@link MdAttributeDAOIF} objects defined by the given entity type plus all
   * attributes defined by parent entities.
   */
  public Map<String, ? extends MdAttributeDAOIF> getAllDefinedMdAttributeMap();

  /**
   * Returns the names of all supertypes that this type inherits from, including
   * this type.
   *
   * @return names of all supertypes that this type inherits from, including
   *         this type.
   */
  public List<String> getSuperTypes();

  /**
   * (non-Javadoc)
   *
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdClassDAO getBusinessDAO();

  public MdClassDimensionDAOIF getMdClassDimension(MdDimensionDAOIF mdDimension);

  public List<MdClassDimensionDAOIF> getMdClassDimensions();
}
