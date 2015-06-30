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
package com.runwaysdk.business;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.runwaysdk.business.rbac.ActorDAOIF;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.AttributeMultiReferenceIF;
import com.runwaysdk.dataaccess.AttributeStructIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.TransientDAO;
import com.runwaysdk.dataaccess.attributes.AttributeException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.Ownable;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionFacade;
import com.runwaysdk.session.SessionIF;
import com.runwaysdk.util.IdParser;

public abstract class SessionComponent implements Transient, Ownable, Serializable
{

  /**
   * 
   */
  private static final long serialVersionUID = -4938545326518162971L;

  // Container for attributes on this view.
  TransientDAO              transientDAO;

  private ActorDAOIF        owner            = null;

  /**
   * Blank constructor can be used for new or existing instances. It is
   * <b>critical</b> that subclasses call
   * {@link Transient#setTransientDAO(TransientDAO)} to correclty initialize the
   * entity.
   */
  SessionComponent()
  {
    setTransientDAO(TransientDAO.newInstance(this.getDeclaredType()));
  }

  /**
   * Creates a new Session object that may not be typesafe. The DAO the new
   * object represents will be of the specified type, but if this constructor
   * has been super()d into, the concrete java type of the constructed object
   * will be unknown.
   * 
   * To guarantee that the java type and DAO type correspond correctly, use
   * {@link SessionComponent#Session()}, which uses polymorphism instead of a
   * parameter.
   * 
   * @param type
   *          The type of the DAO that this Session object will represent
   */
  public SessionComponent(String type)
  {
    super();
    this.setTransientDAO(TransientDAO.newInstance(type));
  }

  /**
   * Instantiates a Business using reflection to invoke the empty constructor in
   * the concrete child class. Will throw a {@link ClassLoaderException} if any
   * number of Exceptions are caught during the reflection.
   * 
   * @param businessDAO
   *          The database object the new Business will represent
   * @return A typesafe Business
   */
  static SessionComponent instantiate(TransientDAO transientDAO)
  {
    SessionComponent object;
    try
    {
      Class<?> clazz = LoaderDecorator.load(transientDAO.getType());
      Constructor<?> con = clazz.getConstructor();
      object = (SessionComponent) con.newInstance();

      object.setTransientDAO(transientDAO);

      // Set the private variables of the runtime type
      for (AttributeIF attribute : transientDAO.getAttributeArrayIF())
      {
        if (attribute instanceof AttributeStructIF)
        {
          AttributeStructIF attributeStruct = (AttributeStructIF) attribute;
          Struct struct = Struct.instantiate(object, attributeStruct.getName());
          String typeName = attributeStruct.getDefiningClassType() + TypeGeneratorInfo.BASE_SUFFIX;

          Class<?> c = LoaderDecorator.load(typeName);
          String structName = CommonGenerationUtil.lowerFirstCharacter(attribute.getName());

          Field field = c.getDeclaredField(structName);
          field.setAccessible(true);
          field.set(object, struct);
        }
      }
    }
    catch (Exception e)
    {
      throw new ClassLoaderException(transientDAO.getMdClassDAO(), e);
    }

    return object;
  }

  /**
   * Sets the owner of the SessionComponent to the given actor. This is used to
   * assign permissions to view instances on an individual basis.
   * 
   * @param owner
   */
  public void setOwner(ActorDAOIF owner)
  {
    this.owner = owner;
  }

  /**
   * Returns the Actor Data Access Object that owns this object.
   * 
   * @return Actor Data Access Object that owns this object.
   */
  public ActorDAOIF getOwnerDAO()
  {
    return this.owner;
  }

  /**
   * Returns the Actor id that owns this object.
   * 
   */
  public String getOwnerId()
  {
    if (this.owner != null)
    {
      return this.owner.getId();
    }
    else
    {
      return null;
    }
  }

  /**
   * Default visibility is on purpose: we don't want all generated classes to
   * see this method.
   * 
   * @param entityDAO
   */
  void setTransientDAO(TransientDAO transientDAO)
  {
    this.transientDAO = transientDAO;
  }

  /**
   * Default visibility is on purpose: we don't want all generated classes to
   * see this method.
   * 
   * @return the TransientDAO
   */
  TransientDAO getTransientDAO()
  {
    return this.transientDAO;
  }

  /**
   * Returns the type of this Entity. Generic entity objects can represent
   * specific types - this method returns the declared type of the object.
   * 
   * @return The declared type of this object
   */
  protected abstract String getDeclaredType();

  /**
   * Indicates if this is a new instance. If it is new, then the records that
   * represent this ComponentIF have not been created.
   */
  public boolean isNew()
  {
    return transientDAO.isNew();
  }

  /**
   * Returns a BusinessDAO representing the attribute metadata of the attribute
   * with the given name.
   * 
   * <br/>
   * <b>Precondition:</b> name != null <br/>
   * <b>Precondition:</b> !name.trim().equals("") <br/>
   * <b>Precondition:</b> name is a valid attribute with respect the this
   * Component's class.
   * 
   * @param name
   *          of the attribute.
   * @return BusinessDAO representing the attribute metadata of the attribute
   *         with the given name
   */
  public MdAttributeDAOIF getMdAttributeDAO(String name)
  {
    return this.transientDAO.getMdAttributeDAO(name);
  }

  /**
   * Returns a LinkedList of BusinessDAOs representing metadata for each
   * attribute defined for this object's class.
   * 
   * <br/>
   * <b>Precondition:</b> true
   * 
   * @return List of BusinessDAOs representing metadata for each attribute
   *         defined for this object's class.
   */
  public List<? extends MdAttributeDAOIF> getMdAttributeDAOs()
  {
    return this.transientDAO.getMdAttributeDAOs();
  }

  /**
   * Returns true if the business Object has an attribute with the given name,
   * false otherwise. It is case sensitive.
   * 
   * @param name
   *          name of the attribute.
   * @return true if the business Object has an attribute with the given name,
   *         false otherwise. It is case sensitive.
   */
  public boolean hasAttribute(String name)
  {
    return this.transientDAO.hasAttribute(name);
  }

  /**
   * A generic, type-unsafe getter that takes the attribute name as a String,
   * and returns the value as a String
   * 
   * @param name
   *          String name of the desired attribute
   * @return String representation of the value
   */
  public String getValue(String name)
  {
    return this.transientDAO.getValue(name);
  }

  /**
   * Some attributes store objects instead of strings.
   * 
   * @param name
   * @return object stored on the attribute.
   */
  public Object getObjectValue(String name)
  {
    return this.transientDAO.getObjectValue(name);
  }

  /**
   * Returns the type unsafe Struct (actual type of Struct) associated with an
   * AttributeStruct
   * 
   * @param structName
   *          The name of the AttributeStruct
   * @return A Struct representation of the AttributeStruct
   */
  protected Struct getGenericStruct(String structName)
  {
    // The StructDAO that the Struct delegates to
    // must be the same java object as the StructDAO which
    // the AttributeStructIF delegates to or else the apply method
    // will apply a different StructDAO.
    return new Struct(this, structName);
  }

  /**
   * A generic, type-unsafe getter for struct attributes that takes the
   * attribute and struct names as Strings, and returns the value as a String.
   * 
   * @param structName
   *          String name of the struct
   * @param name
   *          String name of the desired attribute
   * @return String representation of the struct value
   */
  public String getStructValue(String structName, String attributeName)
  {
    return this.transientDAO.getStructValue(structName, attributeName);
  }

  /**
   * Returns the value for the attribute that matches the given locale (or a
   * best fit).
   * 
   * @param localAttributeName
   * @param local
   * @return the value of a local attribute
   */
  public String getLocalValue(String localAttributeName, Locale locale)
  {
    return transientDAO.getLocalValue(localAttributeName, locale);
  }

  /**
   * A generic, type-unsafe getter for struct blob attributes that takes the
   * attribute and struct names as Strings, and returns the value as a byte
   * array
   * 
   * @param structName
   *          String name of the struct
   * @param blobName
   *          String name of the desired blob attribute
   * @return byte[] representation of the struct value
   */
  public byte[] getStructBlob(String structName, String blobName)
  {
    return this.transientDAO.getStructBlob(structName, blobName);
  }

  /**
   * Returns a list of selected values for the given enumerated attribute. The
   * declared type of the list is BusinessEnumeration, but each entry is
   * instantiated through reflection, which allows for accurate actual types.
   * 
   * @param name
   *          Name of the attribute enumeration
   * @return List of typesafe enumeration options that are selected
   */
  public List<? extends BusinessEnumeration> getStructEnumValues(String structName, String attributeName)
  {
    AttributeStructIF struct = (AttributeStructIF) transientDAO.getAttributeIF(structName);
    AttributeEnumerationIF attribute = (AttributeEnumerationIF) struct.getAttributeIF(attributeName);

    Set<String> ids = ( attribute ).getCachedEnumItemIdSet();
    MdAttributeConcreteDAOIF mdAttribute = attribute.getMdAttributeConcrete();

    return Entity.loadEnumValues(ids, mdAttribute);
  }

  /**
   * Validates the attribute with the given name. If the attribute is not valid,
   * then an AttributeException exception is thrown.
   * 
   * <br/>
   * <b>Precondition:</b> name != null <br/>
   * <b>Precondition:</b> !name.trim().equals("") <br/>
   * <b>Precondition:</b> An attribute of the given name exists for instances of
   * this class
   * 
   * @param name
   *          name of the attribute
   * @throws AttributeException
   *           if the attribute is not valid.
   */
  public void validateAttribute(String name)
  {
    this.transientDAO.validateAttribute(name);
  }

  /**
   * Returns the Struct associated with an AttributeStruct
   * 
   * @param structName
   *          The name of the AttributeStruct
   * @return A Struct representation of the AttributeStruct
   */
  public Struct getStruct(String structName)
  {
    Struct struct = Struct.instantiate(this, structName);

    return struct;
  }

  /**
   * A generic, type-unsafe setter that takes the attribute name a and value as
   * Strings
   * 
   * @param name
   *          String name of the attribute
   * @param value
   *          String representation of the value
   */
  public void setValue(String name, String _value)
  {
    String value = "";

    if (_value != null)
    {
      value = _value;
    }

    this.transientDAO.setValue(name, value);
  }

  /**
   * A generic, type-unsafe setter that takes the attribute name a and value as
   * an Object.
   * 
   * @param name
   *          String name of the attribute
   * @param value
   *          String representation of the value
   */
  public void setValue(String name, Object _object)
  {
    this.transientDAO.setValue(name, _object);
  }

  /**
   * A generic, type-unsafe setter for struct attributes that takes the
   * attribute name, struct name, and value as Strings
   * 
   * @param structName
   *          String name of the struct
   * @param name
   *          String name of the desired attribute
   * @param vale
   *          String representation of the struct value
   */
  public void setStructValue(String structName, String attributeName, String _value)
  {
    String value = "";

    if (_value != null)
    {
      value = _value;
    }

    this.transientDAO.setStructValue(structName, attributeName, value);
  }

  /**
   * Adds an item to an enumerated struct attribute.
   * 
   * @param structName
   *          The name of the struct
   * @param attributeName
   *          The name of the attribute (inside the struct)
   * @param value
   *          The value to set
   */
  public void addStructItem(String structName, String attributeName, String value)
  {
    this.transientDAO.addStructItem(structName, attributeName, value);
  }

  /**
   * A generic, type-unsafe setter for struct attributes that takes the
   * attribute name, struct name, and value as Strings
   * 
   * @param structName
   *          String name of the struct
   * @param blobName
   *          String name of the desired attribute
   * @param vale
   *          String representation of the struct value
   */
  public void setStructBlob(String structAttributeName, String blobName, byte[] value)
  {
    this.transientDAO.setStructBlob(structAttributeName, blobName, value);
  }

  /**
   * A generic, type-unsafe getter that takes a blob attribute name as a String,
   * and returns the value as a byte array
   * 
   * @param blobName
   *          Name of the blob attribute
   * @return byte array representing the blob
   */
  public byte[] getBlob(String blobName)
  {
    return this.transientDAO.getBlob(blobName);
  }

  /**
   * A generic, type-unsafe getter that takes a blob attribute name as a String,
   * and returns the value as a byte array
   * 
   * @param blobName
   *          Name of the blob attribute
   * @return byte array representing the blob
   */
  public void setBlob(String blobName, byte[] value)
  {
    this.transientDAO.setBlob(blobName, value);
  }

  /**
   * Returns a list of selected values for the given enumerated attribute. The
   * declared type of the list is BusinessEnumeration, but each entry is
   * instantiated through reflection, which allows for accurate actual types.
   * 
   * @param name
   *          Name of the attribute enumeration
   * @return List of typesafe enumeration options that are selected
   */
  public List<? extends BusinessEnumeration> getEnumValues(String name)
  {
    AttributeEnumerationIF attribute = (AttributeEnumerationIF) transientDAO.getAttribute(name);
    Set<String> ids = ( attribute ).getCachedEnumItemIdSet();
    MdAttributeConcreteDAOIF mdAttribute = attribute.getMdAttributeConcrete();

    return Entity.loadEnumValues(ids, mdAttribute);
  }

  /**
   * A generic, type-unsafe method for adding an item to an enumerated attribute
   * that takes the attribute name and enumeration item as Strings
   * 
   * @param name
   *          String name of the enumerated attribute
   * @param item
   *          String representation of the enumeration item
   */
  public void addEnumItem(String name, String item)
  {
    this.transientDAO.addItem(name, item);
  }

  /**
   * Replaces the items of an enumerated attribute. If the attribute does not
   * allow multiplicity, then the {@code values} collection must contain only
   * one item.
   * 
   * @param name
   *          Name of the enumerated attribute
   * @param values
   *          Collection of enumerated item ids
   */
  public void replaceEnumItems(String name, Collection<String> values)
  {
    this.transientDAO.replaceItems(name, values);
  }

  /**
   * A generic, type-unsafe method for removing an item from an enumerated
   * attribute that takes the attribute name and enumeration item as Strings
   * 
   * @param name
   *          String name of the enumerated attribute
   * @param item
   *          String representation of the enumeration item
   */
  public void removeEnumItem(String name, String item)
  {
    this.transientDAO.removeItem(name, item);
  }

  /**
   * A generic method for clearing out all selected items on an enumerated
   * attribute.
   * 
   * @param name
   *          String name of the enumerated attribute
   */
  public void clearEnum(String name)
  {
    this.transientDAO.clearItems(name);
  }

  /**
   * Replaces the items of an enumerated struct attribute. If the attribute does
   * not allow multiplicity, then the {@code values} collection must contain
   * only one item.
   * 
   * @param structName
   *          The name of the struct
   * @param attributeName
   *          The name of the attribute (inside the struct)
   * @param values
   *          Collection of enumerated it ids
   */
  public void replaceStructItems(String structName, String attributeName, Collection<String> values)
  {
    transientDAO.replaceStructItems(structName, attributeName, values);
  }

  /**
   * Remove an item for an enumerated struct attribute.
   * 
   * @param structName
   *          The name of the struct
   * @param attributeName
   *          The name of the attribute (inside the struct)
   * @param value
   *          The value to set
   */
  public void removeStructItem(String structName, String attributeName, String value)
  {
    transientDAO.removeStructItem(structName, attributeName, value);
  }

  /**
   * Clears all the values of a struct enumeration attribute.
   * 
   * @param structName
   *          The name of the struct
   * @param attributeName
   *          The name of the attribute (inside the struct)
   */
  public void clearStructItems(String structName, String attributeName)
  {
    transientDAO.clearStructItems(structName, attributeName);
  }

  /**
   * Returns the Universally Unique ID (UUID) for this entity.
   * 
   * @return <b>this</b> entity's UUID
   */
  public String getId()
  {
    return this.transientDAO.getId();
  }

  /**
   * Returns the Id used for AttributeProblems (not messages). New instances
   * that fail will have a different ID on the client.
   * 
   * @return problem notification id.
   */
  public String getProblemNotificationId()
  {
    return transientDAO.getProblemNotificationId();
  }

  public String getKey()
  {
    return this.transientDAO.getKey();
  }

  public String getType()
  {
    return this.transientDAO.getType();
  }

  /**
   * Returns the display label of the class.
   * 
   * @return <b>this</b> entity's UUID
   */
  public String getClassDisplayLabel()
  {
    return this.transientDAO.getMdClassDAO().getDisplayLabel(Session.getCurrentLocale());
  }

  /**
   * If this method is executed within the context of a session, then this
   * object will be stored in that session.
   */
  public void apply()
  {
    this.transientDAO.apply();

    Session currentSession = (Session) Session.getCurrentSession();

    if (currentSession != null)
    {
      SessionFacade.put(currentSession.getId(), this);
    }
  }

  /**
   * Object is applied (put into a ready state) but is not persisted to the
   * user's session.
   */
  public void applyNoPersist()
  {
    this.transientDAO.apply();
  }

  /**
   * If this method is executed within the context of a session, then this
   * object will be removed from that session.
   */
  public void delete()
  {
    Session currentSession = (Session) Session.getCurrentSession();

    if (currentSession != null)
    {
      currentSession.remove(this.getId());
    }
  }

  /**
   * When an object at the business layer is converted into a DTO, this method
   * is invoked to ensure there are not any READ violations that are enforced
   * programatically. This method should be ovewritten in business classes if
   * special programatic READ permissions need to be implemented. This method
   * should throw an exception if customized READ permissions are not adequate.
   */
  public void customReadCheck()
  {
  }

  /**
   * Writes to standard out all attribute names and their values of this
   * BusinessDAO instance. All values that are keys are dereferenced and the
   * values referenced by those keys are returned.
   * 
   * <br/>
   * <b>Precondition:</b> true
   * 
   */
  public void printAttributes()
  {
    this.transientDAO.printAttributes();
  }

  /**
   * Returns if an attribute of the Entity has been modified from its orginal
   * value loaded from the database.
   * 
   * @param name
   *          The name of the attribute
   * @return
   */
  public boolean isModified(String name)
  {
    return transientDAO.getAttribute(name).isModified();
  }

  /**
   * Returns the SessionComponent object with the given id, if it has been
   * persisted to the user's session.
   * 
   * @param id
   * @return Util object with the given id.
   */
  public static SessionComponent get(String id)
  {
    SessionIF session = Session.getCurrentSession();
    if (session != null)
    {
      Mutable mutable = session.get(id);

      if (mutable != null)
      {
        return (SessionComponent) mutable;
      }
    }

    String errMsg = "An instance of type [" + SessionComponent.class.getName() + "] with id [" + id + "] does not exist in the user's session.";
    throw new DataNotFoundException(errMsg, MdClassDAO.getMdClassByRootId(IdParser.parseMdTypeRootIdFromId(id)));
  }

  /**
   * The String representation of this entity.
   */
  public String toString()
  {
    return this.getId();
  }

  public void addMultiItem(String name, String item)
  {
    transientDAO.addItem(name, item);
  }

  public void replaceMultiItems(String name, Collection<String> values)
  {
    transientDAO.replaceItems(name, values);
  }

  public void removeMultiItem(String name, String item)
  {
    transientDAO.removeItem(name, item);
  }

  public void clearMultiItems(String name)
  {
    transientDAO.clearItems(name);
  }

  public List<? extends Business> getMultiItems(String name)
  {
    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) transientDAO.getAttribute(name);
    Set<String> ids = ( attribute ).getCachedItemIdSet();
    MdAttributeConcreteDAOIF mdAttribute = attribute.getMdAttributeConcrete();

    return loadSetValues(ids, mdAttribute);
  }

  static List<? extends Business> loadSetValues(Set<String> ids, MdAttributeConcreteDAOIF mdAttribute)
  {
    List<Business> items = new LinkedList<Business>();

    for (String id : ids)
    {
      items.add(Business.get(id));
    }
    return items;
  }
}
