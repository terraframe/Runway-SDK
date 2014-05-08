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
package com.runwaysdk.business;

import java.lang.reflect.Constructor;
import java.util.List;

import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdEntityInfo;
import com.runwaysdk.constants.StructInfo;
import com.runwaysdk.dataaccess.AttributeStructIF;
import com.runwaysdk.dataaccess.ComponentDAO;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.InvalidIdException;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.StructDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.AttributeStruct;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.transaction.LockObject;
import com.runwaysdk.generation.loader.LoaderDecorator;

/**
 * Business layer representation of {@link StructDAO}s. Represents instances that
 * standalone and instances that are part of an {@link AttributeStruct}.
 *
 * @author Justin Smethie
 */
public class Struct extends Entity implements StructInfo
{
  /**
   *
   */
  private static final long serialVersionUID = 3524154446951765076L;

  public final static String CLASS = Struct.class.getName();

  /**
   * Used if the Struct represents a struct, null if this is a standalone.
   */
  private MutableWithStructs parent;

  /**
   * Used if the Struct represents a struct, null if this is a standalone.
   */
  private String structName;

  /**
   * Constructs a new Struct of the given type
   *
   * @param type
   */
  public Struct()
  {
    super();
    setDataEntity(StructDAO.newInstance(getDeclaredType()));
  }

  /**
   * Primary entry point for other constructors to delegate to. structDAO can be an
   * existing or new instance, so this constructor is used for both cases. Can also be
   * used to create type unsafe Business representations of existing data.
   *
   * @param structDAO
   */
  Struct(StructDAO structDAO)
  {
    super();

    setDataEntity(structDAO);
    this.parent = null;
    this.structName = null;
  }

  /**
   * Returns a type unsafe Struct (non-generated) of the appropriate type unsafe type.
   * @param structDAO
   * @return Struct
   */
  static Struct typeUnsafeStructFactory(StructDAO structDAO)
  {
    MdEntityDAOIF mdEntityDAOIF = structDAO.getMdClassDAO();

    if (mdEntityDAOIF instanceof MdLocalStructDAOIF)
    {
      return new LocalStruct(structDAO);
    }
    else
    {
      return new Struct((StructDAO) structDAO);
    }
  }

  /**
   * Constructor used when the new Struct represents a struct attribute (as
   * opposed to a standalone).
   *
   * @param parent
   *          The Business parent of this Struct
   * @param structName
   *          The name of the the struct attribute this instance represents
   */
  protected Struct(MutableWithStructs parent, String structName)
  {
    super();

    ComponentDAO componentDAO;

    if (parent instanceof SessionComponent)
    {
      componentDAO = ((SessionComponent)parent).getTransientDAO();
    }
    else if (parent instanceof SmartException)
    {
      componentDAO = ((SmartException)parent).getTransientDAO();
    }
    else if (parent instanceof Notification)
    {
      componentDAO = ((Notification)parent).getTransientDAO();
    }
    else
    {
      // This cast is OK.  We are not modifying the EntityDAO, nor are we holding on to a reference to it.
      componentDAO = (EntityDAO)((Entity)parent).getEntityDAO();
    }

    setDataEntity(((AttributeStructIF)componentDAO.getAttributeIF(structName)).getStructDAO());
    this.parent = parent;
    this.structName = structName;
  }

  /**
   * Instantiates a Struct using reflection to invoke the empty constructor in the
   * concrete child class. Will throw a {@link ClassLoaderException} if any of a number of
   * Exceptions are caught during the reflection.
   *
   * @param structDAOIF
   *          The database object the new Struct will represent
   * @return A typesafe Struct.
   */
  static Struct instantiate(StructDAOIF structDAOIF)
  {
    // This cast is OK, as lock checks are in place to prevent this structDAOIF
    // from being modified via setters on the business struct.
    return instantiate((StructDAO)structDAOIF);
  }

  /**
   * Instantiates a Struct using reflection to invoke the empty constructor in the
   * concrete child class. Will throw a {@link ClassLoaderException} if any of a number of
   * Exceptions are caught during the reflection.
   *
   * @param structDAO
   *          The database object the new Struct will represent
   * @return A typesafe Struct.
   */
  private static Struct instantiate(StructDAO structDAO)
  {
    Struct object;

    try
    {
      Class<?> clazz = LoaderDecorator.load(structDAO.getType());
      Constructor<?> con = clazz.getConstructor();
      object = (Struct) con.newInstance();
    }
    catch (Exception e)
    {
      throw new ClassLoaderException(structDAO.getMdClassDAO(), e);
    }
    object.setDataEntity(structDAO);
    return object;
  }

  /**
   * Instantiates a Struct using reflection to invoke the (Element, String)
   * constructor in the concrete child class. Will throw a {@link ClassLoaderException} if
   * any Exceptions are caught during the reflection.
   *
   * @param entity The parent entity of the new struct instance
   * @param structName The name of the struct attribute
   * @return A typesafe Struct.
   */
  static Struct instantiate(Element entity, String structName)
  {
    return instantiate((MutableWithStructs)entity, structName);
  }

  /**
   * Instantiates a Struct using reflection to invoke the (Element, String)
   * constructor in the concrete child class. Will throw a {@link ClassLoaderException} if
   * any Exceptions are caught during the reflection.
   *
   * @param tranzient The parent Transient of the new struct instance
   * @param structName The name of the struct attribute
   * @return A typesafe Struct.
   */
  static Struct instantiate(Transient tranzient, String structName)
  {
    return instantiate((MutableWithStructs)tranzient, structName);
  }

  private static Struct instantiate(MutableWithStructs component, String structName)
  {
    MdAttributeDAOIF mdAttribute = component.getMdAttributeDAO(structName).getMdAttributeConcrete();

    String id = mdAttribute.getValue(MdAttributeStructInfo.MD_STRUCT);
    String structType = MdStructDAO.get(id).definesType();

    try
    {
      Class<?> clazz = LoaderDecorator.load(structType);
      Constructor<?> con = clazz.getConstructor(MutableWithStructs.class, String.class);
      return (Struct) con.newInstance(component, structName);
    }
    catch (Exception e)
    {
      MdClassDAOIF mdClassIF = MdClassDAO.getMdClassDAO(component.getType());
      throw new ClassLoaderException(mdClassIF, e);
    }
  }

  /**
   * Creates a new Struct that is not typesafe, but is the correct concrete
   * implementation of Struct. For example, calling
   * newInstance("package.Book") will return an instance of Struct, not an
   * instance of Book.
   *
   * @param type
   * @return
   */
  static Struct newInstance(String type)
  {
    StructDAO structDAO = StructDAO.newInstance(type);
    return Struct.typeUnsafeStructFactory(structDAO);
  }

  /**
   * Simple getter to cast the underlying EntityDAO to a StructDAO.
   *
   * @return the {@link StructDAO} this represents.
   */
  StructDAO getStructDAO()
  {
    return (StructDAO) super.getEntityDAO();
  }

  protected String getDeclaredType()
  {
    return StructInfo.CLASS;
  }

  /**
   * A generic, type-unsafe getter that takes the attribute name as a String,
   * and returns the value as a String
   *
   * @param name String name of the desired attribute
   * @return String representation of the value
   */
  public String getValue(String name)
  {
    if(this.parent != null)
    {
      return this.parent.getStructValue(structName, name);
    }
    else
    {
      return super.getValue(name);
    }
  }

  /**
   * A generic, type-unsafe setter that takes the attribute name a and value as
   * Strings
   *
   * @param name String name of the attribute
   * @param value String representation of the value
   */
  public void setValue(String name, String value)
  {
    if(this.parent != null)
    {
      this.parent.setStructValue(structName, name, value);
    }
    else
    {
      super.setValue(name, value);
    }
  }

  /**
   * A generic, type-unsafe getter that takes a blob attribute name as a
   * String, and returns the value as a byte array
   *
   * @param blobName Name of the blob attribute
   * @return byte array representing the blob
   */
  public byte[] getBlob(String blobName)
  {
    if(this.parent != null)
    {
      return this.parent.getStructBlob(structName, blobName);
    }
    else
    {
      return super.getBlob(blobName);
    }
  }

  /**
   * A generic, type-unsafe getter that takes a blob attribute name as a
   * String, and returns the value as a byte array
   *
   * @param blobName Name of the blob attribute
   * @return byte array representing the blob
   */
  public void setBlob(String blobName, byte[] value)
  {
    if(this.parent != null)
    {
      this.parent.setStructBlob(structName, blobName, value);
    }
    else
    {
      super.setBlob(blobName, value);
    }
  }

  /**
   * Returns a list of selected values for the given enumerated attribute. The
   * declared type of the list is BusinessEnumeration, but each entry is
   * instantiated through reflection, which allows for accurate actual types.
   *
   * @param name Name of the attribute enumeration
   * @return List of typesafe enumeration options that are selected
   */
  public List<? extends BusinessEnumeration> getEnumValues(String name)
  {
    if(this.parent != null)
    {
      return this.parent.getStructEnumValues(structName, name);
    }

    return super.getEnumValues(name);
  }

  /**
   * A generic, type-unsafe method for adding an item to an enumerated attribute
   * that takes the attribute name and enumeration item as Strings
   *
   * @param name String name of the enumerated attribute
   * @param item ID of the enumeration item
   */
  public void addEnumItem(String name, String item)
  {
    if(this.parent != null)
    {
      this.parent.addStructItem(structName, name, item);
    }
    else
    {
      super.addEnumItem(name, item);
    }
  }

  /**
   * A generic, type-unsafe method for removing an item from an enumerated
   * attribute that takes the attribute name and enumeration item as Strings
   *
   * @param name String name of the enumerated attribute
   * @param item ID of the enumeration item
   */
  public void removeEnumItem(String name, String item)
  {
    if(this.parent != null)
    {
      this.parent.removeStructItem(structName, name, item);
    }
    else
    {
      super.removeEnumItem(name, item);
    }
  }

  /**
   * A generic method for clearing out all selected items on an enumerated
   * attribute.
   *
   * @param name String name of the enumerated attribute
   */
  public void clearEnum(String name)
  {
    if(this.parent != null)
    {
      this.parent.clearStructItems(structName, name);
    }
    else
    {
      super.clearEnum(name);
    }
  }

  /**
   * Locks the given Entity by the current treads.
   *
   * @param userId
   * @throws DataAccessException
   *           if the Entity is locked by another user
   */
  public void appLock(String userId)
  {
    // Wait until this thread attains a lock on this object
    (LockObject.getLockObject()).appLock(this.getId());

    this.setDataEntity((EntityDAO.get(this.entityDAO.getId())).getEntityDAO());
  }

  /**
   * Persists this entity and all changes to the database.
   * <code><b>new</b></code> instances of Struct are <i>not</i>
   * persisted until <code>apply()</code> is called. Similarly, changes made
   * to instances through the generated java classes are not persisted until
   * <code>apply()</code> is called.
   *
   * <b>Precondtion:</b> Session user has a lock on this object, assuming this
   *                     object has a ComponentIF.LOCKED_BY field.
   */
  public void apply()
  {
    // If this is a struct (not a standalone), do nothing. This will be applied in the
    // context of the parent's apply() method.
    if(this.parent != null)
    {
      return;
    }

    super.apply();
  }

  /**
   * Deletes this entity from the database. Any attempt to
   * {@link Entity#apply()} this entity will throw an exception, so it
   * is the responsibility of the developer to remove references to deleted
   * instances of Entity.
   */
  public void delete()
  {
    // If this is a struct (not a standalone), do nothing. This will be deleted in the
    // context of the parent's delete() method.
    if(this.parent != null)
    {
      return;
    }

    super.delete();
  }

  /**
   * Checks if the current user has a lock on this Struct. Note that this method
   * balks and just returns false since Structs can't be locked.
   *
   * @return false
   */
  public boolean checkUserLock()
  {
    return false;
  }
  /**
   * Returns an object of the specified type with the specified id from the
   * database by using reflection. The returned Entity is
   * type safe.
   *
   * @param id ID of the instance to get.
   * @return Type safe Struct representing the id in the database.
   */
  public static Struct get(String id)
  {
    // An empty string likely indicates the value was never set in the database.
    if (id == null || id.length() == 0)
    {
      String errMsg = "Object with id [" + id + "] is not defined by a [" + MdEntityInfo.CLASS + "]";

      throw new InvalidIdException(errMsg, id);
    }

    // This cast is OK, as lock checks are in place to prevent this StructDAO
    // from being modified via setters on the business object.
    StructDAO object = (StructDAO)StructDAO.get(id);
    Struct struct = (Struct) Struct.instantiate(object);
    struct.entityDAO = object;

    return struct;
  }

  /**
   * Using reflection, get returns an object of the specified type with the
   * specified key from the database. The returned Business is typesafe, meaning
   * that its actual type is that specified by the type parameter.
   *
   * @param type
   *            type of the instance to get
   * @param key
   *            key of the instance to get
   * @return Typesafe Business representing the id in the database
   */
  public static Struct get(String type, String key)
  {
    // This cast is OK, as lock checks are in place to prevent this StructDAO
    // from being modified via setters on the business object.
    StructDAO object = (StructDAO)StructDAO.get(type, key);
    Struct struct = (Struct) Struct.instantiate(object);
    struct.entityDAO = object;

    return struct;
  }

  /**
   * Returns an object of the specified type with the specified id from the
   * database without using reflection. The returned Struct is
   * not typesafe, meaning that its actual type just a Struct.
   *
   * @param id ID of the instance to get.
   * @return Type unsafe Struct representing the id in the database.
   */
  public static Struct getStruct(String id)
  {
    StructDAO object = StructDAO.get(id).getStructDAO();
    return Struct.typeUnsafeStructFactory(object);
  }

  /**
   * Returns the UserIF of the user who has locked this object. Since Structs cannot
   * be locked, this method balks and always return null;
   *
   * @return null
   */
  public UserDAOIF getLockedBy()
  {
    return null;
  }
}
