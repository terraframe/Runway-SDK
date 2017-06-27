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
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ognl.ExpressionSyntaxException;
import ognl.Ognl;
import ognl.OgnlClassResolver;
import ognl.OgnlContext;
import ognl.OgnlException;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.MdAttributeBooleanUtil;
import com.runwaysdk.constants.MdAttributeDateTimeUtil;
import com.runwaysdk.constants.MdAttributeDateUtil;
import com.runwaysdk.constants.MdAttributeTimeUtil;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.AttributeMultiReferenceIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeNumberDAOIF;
import com.runwaysdk.dataaccess.MdAttributePrimitiveDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.StructDAOIF;
import com.runwaysdk.dataaccess.UnexpectedTypeException;
import com.runwaysdk.dataaccess.attributes.AttributeException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.transaction.LockObject;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.query.AttributeEnumeration;
import com.runwaysdk.query.AttributeLocal;
import com.runwaysdk.query.AttributeReference;
import com.runwaysdk.query.AttributeStruct;
import com.runwaysdk.query.GeneratedEntityQuery;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectablePrimitive;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;

/**
 * The root class of the business layer, Entity is the parent of all generated java classes. Entity provides access to the Data Access layer, and all associated functionality.
 * 
 * @author Eric Grunzke
 */
public abstract class Entity implements Mutable, Serializable
{
  /**
   * 
   */
  private static final long  serialVersionUID = -1542581762623923531L;

  public final static String CLASS            = Entity.class.getName();

  /**
   * All interaction with the core is delegated through this object. This should NOT be accessed outside of this class.
   */
  EntityDAO                  entityDAO;

  /**
   * Blank constructor can be used for new or existing instances. It is <b>critical</b> that subclasses call {@link Entity#setDataEntity(EntityDAO)} to correclty initialize the entity.
   */
  Entity()
  {
  }

  /**
   * Default visibility is on purpose: we don't want all generated classes to see this method.
   * 
   * @param entityDAO
   */
  void setDataEntity(EntityDAO entityDAO)
  {
    this.entityDAO = entityDAO;
  }

  /**
   * Default visibility is on purpose: we don't want all generated classes to see this method.
   * 
   * @return the EntityDAO
   */
  EntityDAOIF getEntityDAO()
  {
    return entityDAO;
  }

  /**
   * Indicates if this is a new instance. If it is new, then the records that represent this ComponentIF have not been created.
   */
  public boolean isNew()
  {
    return entityDAO.isNew();
  }

  /**
   * Indicates whether the delete method has completed execution and the object has been deleted;
   */
  public boolean isDeleted()
  {
    return entityDAO.isDeleted();
  }

  /**
   * Indicates if this instance has been applied to the db. Note that an instance can be both new and applied to the db at the same time, if a transaction method calls {@link #apply()} then continues
   * with more logic.
   * 
   * @return
   */
  public boolean isAppliedToDB()
  {
    return entityDAO.isAppliedToDB();
  }

  /**
   * Returns a MdClassIF that defines this Component's class.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   * 
   * @return a MdClassIF that defines this Component's class.
   */
  public MdClassDAOIF getMdClass()
  {
    return MdClassDAO.getMdClassDAO(this.getType());
  }

  /**
   * Returns a BusinessDAO representing the attribute metadata of the attribute with the given name.
   * 
   * <br/>
   * <b>Precondition:</b> name != null <br/>
   * <b>Precondition:</b> !name.trim().equals("") <br/>
   * <b>Precondition:</b> name is a valid attribute with respect the this Component's class.
   * 
   * @param name
   *          of the attribute.
   * @return BusinessDAO representing the attribute metadata of the attribute with the given name
   */
  public MdAttributeConcreteDAOIF getMdAttributeDAO(String name)
  {
    return this.entityDAO.getMdAttributeDAO(name);
  }

  /**
   * Returns a LinkedList of BusinessDAOs representing metadata for each attribute defined for this object's class.
   * 
   * <br/>
   * <b>Precondition:</b> true
   * 
   * @return List of BusinessDAOs representing metadata for each attribute defined for this object's class.
   */
  public List<? extends MdAttributeConcreteDAOIF> getMdAttributeDAOs()
  {
    return this.entityDAO.getMdAttributeDAOs();
  }

  /**
   * Returns true if the object has an attribute with the given name, false otherwise. It is case sensitive.
   * 
   * @param name
   *          name of the attribute.
   * @return true if the object has an attribute with the given name, false otherwise. It is case sensitive.
   */
  public boolean hasAttribute(String name)
  {
    return this.entityDAO.hasAttribute(name);
  }

  /**
   * A generic, type-unsafe getter that takes the attribute name as a String, and returns the value as a String
   * 
   * @param name
   *          String name of the desired attribute
   * @return String representation of the value
   */
  public String getValue(String name)
  {
    return this.entityDAO.getValue(name);
  }

  /**
   * Some attributes store objects instead of strings.
   * 
   * @param name
   * @return object stored on the attribute.
   */
  public Object getObjectValue(String name)
  {
    return this.entityDAO.getObjectValue(name);
  }

  /**
   * Validates the attribute with the given name. If the attribute is not valid, then an AttributeException exception is thrown.
   * 
   * <br/>
   * <b>Precondition:</b> name != null <br/>
   * <b>Precondition:</b> !name.trim().equals("") <br/>
   * <b>Precondition:</b> An attribute of the given name exists for instances of this class
   * 
   * @param name
   *          name of the attribute
   * @throws AttributeException
   *           if the attribute is not valid.
   */
  public void validateAttribute(String name)
  {
    this.entityDAO.validateAttribute(name);
  }

  /**
   * A generic, type-unsafe setter that takes the attribute name a and value as Strings
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

    entityDAO.setValue(name, value);
  }

  /**
   * A generic, type-unsafe setter that takes the attribute name a and value as an Object.
   * 
   * @param name
   *          String name of the attribute
   * @param value
   *          String representation of the value
   */
  public void setValue(String name, Object _object)
  {
    entityDAO.setValue(name, _object);
  }

  /**
   * A generic, type-unsafe getter that takes a blob attribute name as a String, and returns the value as a byte array
   * 
   * @param blobName
   *          Name of the blob attribute
   * @return byte array representing the blob
   */
  public byte[] getBlob(String blobName)
  {
    return entityDAO.getBlob(blobName);
  }

  /**
   * A generic, type-unsafe getter that takes a blob attribute name as a String, and returns the value as a byte array
   * 
   * @param blobName
   *          Name of the blob attribute
   * @return byte array representing the blob
   */
  public void setBlob(String blobName, byte[] value)
  {
    entityDAO.setBlob(blobName, value);
  }

  /**
   * Returns a list of selected values for the given enumerated attribute. The declared type of the list is BusinessEnumeration, but each entry is instantiated through reflection, which allows for
   * accurate actual types.
   * 
   * @param name
   *          Name of the attribute enumeration
   * @return List of typesafe enumeration options that are selected
   */
  public List<? extends BusinessEnumeration> getEnumValues(String name)
  {
    AttributeEnumerationIF attribute = (AttributeEnumerationIF) entityDAO.getAttribute(name);
    Set<String> ids = ( attribute ).getCachedEnumItemIdSet();
    MdAttributeConcreteDAOIF mdAttribute = attribute.getMdAttributeConcrete();

    return loadEnumValues(ids, mdAttribute);
  }

  /**
   * Returns a list of selected values for the given enumerated attribute. The declared type of the list is BusinessEnumeration, but each enity is instantiated through reflection, which allows for
   * accurate actual types.
   * 
   * @param ids
   *          List of ids to instantiate
   * @param mdAttribute
   *          MdAttribute of instantiated types
   * @return
   */
  static List<? extends BusinessEnumeration> loadEnumValues(Set<String> ids, MdAttributeConcreteDAOIF mdAttribute)
  {
    List<BusinessEnumeration> items = new LinkedList<BusinessEnumeration>();

    try
    {
      Class<?> clazz = LoaderDecorator.load(mdAttribute.javaType(false));
      Method get = clazz.getMethod("get", String.class);

      for (String id : ids)
      {
        try
        {
          // The first parameer for invoke is the object to invoke the method
          // on.
          // get is static, so null means we don't need an object to invoke on.
          items.add((BusinessEnumeration) get.invoke(null, id));
        }
        catch (DataNotFoundException ex)
        {
          // Cached enums can still hold items that have been dropped. Ignore
          // items that aren't found.
        }
      }
      return items;
    }
    catch (Exception e)
    {
      throw new ClassLoaderException(mdAttribute, e);
    }
  }

  /**
   * A generic, type-unsafe method for adding an item to an enumerated attribute that takes the attribute name and enumeration item as Strings
   * 
   * @param name
   *          String name of the enumerated attribute
   * @param item
   *          String representation of the enumeration item
   */
  public void addEnumItem(String name, String item)
  {
    entityDAO.addItem(name, item);
  }

  /**
   * Replaces the items of an enumerated attribute. If the attribute does not allow multiplicity, then the {@code values} collection must contain only one item.
   * 
   * @param name
   *          Name of the enumerated attribute
   * @param values
   *          Collection of enumerated item ids
   */
  public void replaceEnumItems(String name, Collection<String> values)
  {
    entityDAO.replaceItems(name, values);
  }

  /**
   * A generic, type-unsafe method for removing an item from an enumerated attribute that takes the attribute name and enumeration item as Strings
   * 
   * @param name
   *          String name of the enumerated attribute
   * @param item
   *          String representation of the enumeration item
   */
  public void removeEnumItem(String name, String item)
  {
    entityDAO.removeItem(name, item);
  }

  /**
   * A generic method for clearing out all selected items on an enumerated attribute.
   * 
   * @param name
   *          String name of the enumerated attribute
   */
  public void clearEnum(String name)
  {
    entityDAO.clearItems(name);
  }

  /**
   * Returns the type of this Entity. Generic entity objects can represent specific types - this method returns the declared type of the object.
   * 
   * @return The declared type of this object
   */
  protected abstract String getDeclaredType();

  /**
   * Returns the Universally Unique ID (UUID) for this entity.
   * 
   * @return <b>this</b> entity's UUID
   */
  public String getId()
  {
    return entityDAO.getId();
  }

  /**
   * Returns the Id used for AttributeProblems (not messages). New instances that fail will have a different ID on the client.
   * 
   * @return problem notification id.
   */
  public String getProblemNotificationId()
  {
    return entityDAO.getProblemNotificationId();
  }

  public String getType()
  {
    return entityDAO.getType();
  }

  public String getKey()
  {
    return entityDAO.getKey();
  }

  /**
   * Returns the display label of the class.
   * 
   * @return <b>this</b> entity's UUID
   */
  public String getClassDisplayLabel()
  {
    return entityDAO.getMdClassDAO().getDisplayLabel(Session.getCurrentLocale());
  }

  /**
   * Locks the given Entity by the current treads.
   * 
   * @throws DataAccessException
   *           if the Entity is locked by another user
   */
  public void appLock()
  {
    SessionIF session = Session.getCurrentSession();
    if (session != null)
    {
      this.appLock(session.getUser().getId());
    }
  }

  /**
   * Releases the lock on the given Entity by the current treads.
   * 
   * @throws DataAccessException
   *           if the Entity is locked by another user
   */
  public void releaseAppLock(String userId)
  {
    // Only the thread that has a lock on this object can unlock it.
    ( LockObject.getLockObject() ).appLock(this.getId());

    ( LockObject.getLockObject() ).releaseAppLock(this.getId());
  }

  /**
   * Releases the lock on the given Entity by the current treads.
   * 
   * @throws DataAccessException
   *           if the Entity is locked by another user
   */
  public void releaseAppLock()
  {
    SessionIF session = Session.getCurrentSession();
    if (session != null)
    {
      this.releaseAppLock(session.getUser().getId());
    }
  }

  /**
   * Locks the given Entity by the current treads.
   * 
   * @param userId
   * @throws DataAccessException
   *           if the Entity is locked by another user
   */
  public abstract void appLock(String userId);

  /**
   * Persists this entity and all changes to the database. <code><b>new</b></code> instances of Entity are <i>not</i> persisted until <code>apply()</code> is called. Similarly, changes made to
   * instances through the generated java classes are not persisted until <code>apply()</code> is called.
   * 
   * <b>Precondition:</b> Session user has a lock on this object, assuming this object has a ComponentIF.LOCKED_BY field.
   */
  public void apply()
  {
    // This block is not in Element.apply because AbstractRequestManagement
    // would
    // perform the locked by check twice. If the lockedby field is cleared, it
    // will
    // fail the check when super.apply() is called.
    if (entityDAO.hasAttribute(ElementInfo.LOCKED_BY))
    {
      entityDAO.getAttribute(ElementInfo.LOCKED_BY).setValue("");
    }

    com.runwaysdk.dataaccess.attributes.entity.Attribute keyAttribute = entityDAO.getAttribute(ComponentInfo.KEY);

    // Do not change if the key has been modified by the user
    if (!keyAttribute.isModified())
    {
      // Set the key value on the entity DAO
      String key = this.buildKey();

      if (!key.equals(""))
      {
        keyAttribute.setValue(key);
      }
    }

    // Time to process the expression attributes.
    List<? extends MdAttributeConcreteDAOIF> mdAttrList = this.getMdAttributeDAOs();

    for (MdAttributeConcreteDAOIF mdAttributeConcreteDAOIF : mdAttrList)
    {
      if (mdAttributeConcreteDAOIF instanceof MdAttributePrimitiveDAOIF)
      {
        MdAttributePrimitiveDAOIF mdAttributePrimitiveDAOIF = (MdAttributePrimitiveDAOIF) mdAttributeConcreteDAOIF;

        if (mdAttributePrimitiveDAOIF.isExpression())
        {
          String attributeName = mdAttributePrimitiveDAOIF.definesAttribute();
          // Clear the existing value
          entityDAO.setValue(attributeName, "");

          String expressionString = mdAttributePrimitiveDAOIF.getExpression();

          try
          {
            Object expression;
            try
            {
              expression = Ognl.parseExpression(expressionString);
            }
            catch (ExpressionSyntaxException e)
            {
              String devMessage = "The attribute [" + mdAttributePrimitiveDAOIF.definesAttribute() + "] has an invalid expression syntax:\n" + e.getLocalizedMessage();
              throw new InvalidExpressionSyntaxException(devMessage, mdAttributePrimitiveDAOIF, e);
            }

            OgnlContext ognlContext = new OgnlContext();

            Object expressionValue;

            try
            {
              // I am offended that I even have to do this. OGNL stores reflection method definitions which cause
              // problems when classes are reloaded.
              OgnlClassResolver.clearOgnlRuntimeMethodCache();
              expressionValue = Ognl.getValue(expression, ognlContext, this);
            }
            catch (RuntimeException e)
            {
              String devMessage = "The expression on attribute [" + mdAttributePrimitiveDAOIF.definesAttribute() + "] has an error:\n" + e.getLocalizedMessage();
              throw new ExpressionException(devMessage, mdAttributePrimitiveDAOIF, e);
            }

            Object setterValue;

            if (mdAttributePrimitiveDAOIF instanceof MdAttributeNumberDAOIF && ( expressionValue instanceof Integer || expressionValue instanceof Long || expressionValue instanceof Float || expressionValue instanceof Double || expressionValue instanceof BigDecimal ))
            {
              setterValue = expressionValue.toString();
            }
            else if (mdAttributePrimitiveDAOIF instanceof MdAttributeDateTimeDAOIF && expressionValue instanceof Date)
            {
              setterValue = MdAttributeDateTimeUtil.getTypeUnsafeValue((Date) expressionValue);
            }
            else if (mdAttributePrimitiveDAOIF instanceof MdAttributeDateDAOIF && expressionValue instanceof Date)
            {
              setterValue = MdAttributeDateUtil.getTypeUnsafeValue((Date) expressionValue);
            }
            else if (mdAttributePrimitiveDAOIF instanceof MdAttributeTimeDAOIF && expressionValue instanceof Date)
            {
              setterValue = MdAttributeTimeUtil.getTypeUnsafeValue((Date) expressionValue);
            }
            else if (mdAttributePrimitiveDAOIF instanceof MdAttributeBooleanDAOIF && expressionValue instanceof Integer)
            {
              setterValue = MdAttributeBooleanUtil.format((Integer) expressionValue);
            }
            else if (mdAttributePrimitiveDAOIF instanceof MdAttributeCharDAOIF && expressionValue instanceof String)
            {
              setterValue = expressionValue;
            }
            else if (expressionValue == null)
            {
              setterValue = "";
            }
            else
            {
              setterValue = expressionValue.toString();
            }

            // Clear the existing value
            entityDAO.setValue(attributeName, setterValue);
          }
          catch (OgnlException e)
          {
            String devMessage = "The expression on attribute [" + mdAttributePrimitiveDAOIF.definesAttribute() + "] has an error:\n" + e.getLocalizedMessage();
            throw new ExpressionException(devMessage, mdAttributePrimitiveDAOIF, e);
          }
        }
      }
    }

    entityDAO.apply();
  }

  /**
   * Deletes this entity from the database. Any attempt to {@link Entity#apply()} this entity will throw an exception, so it is the responsibility of the developer to remove references to deleted
   * instances of Entity.
   */
  public void delete()
  {
    entityDAO.delete(true);
  }

  /**
   * When an object at the business layer is converted into a DTO, this method is invoked to ensure there are not any READ violations that are enforced programatically. This method should be
   * ovewritten in business classes if special programatic READ permissions need to be implemented. This method should throw an exception if declarative READ permissions are not adequate.
   */
  public void customReadCheck()
  {
  }

  /**
   * Returns an object of the specified type with the specified id from the database without using reflection. The returned Entity is not typesafe, meaning that its actual type just a Entity.
   * 
   * @param id
   *          ID of the instance to get.
   * @return Typesafe Entity representing the id in the database.
   */
  public static Entity getEntity(String id)
  {
    EntityDAO entityDAO = EntityDAO.get(id).getEntityDAO();

    return Entity.getEntity(entityDAO);
  }

  /**
   * Returns an object of the specified type with the specified id from the database without using reflection. The returned Entity is not typesafe, meaning that its actual type just a Entity.
   * 
   * @param id
   *          ID of the instance to get.
   * @return Typesafe Entity representing the id in the database.
   */
  public static Entity getEntity(EntityDAO entityDAO)
  {
    if (entityDAO instanceof RelationshipDAO)
    { // Cast is OK, as the data access object cannot be modified unless the
      // logged in user
      // has a lock on the object.
      Relationship relationship = new Relationship((RelationshipDAO) entityDAO);
      return relationship;
    }
    else if (entityDAO instanceof BusinessDAO)
    {
      // Cast is OK, as the data access object cannot be modified unless the
      // logged in user
      // has a lock on the object.
      return new Business((BusinessDAO) entityDAO);
    }
    else if (entityDAO instanceof StructDAO)
    {
      // Cast is OK, as the data access object cannot be modified unless the
      // logged in user
      // has a lock on the object.
      return Struct.typeUnsafeStructFactory((StructDAO) entityDAO);
    }
    throw new UnexpectedTypeException("ID [" + entityDAO.getId() + "] is not an Entity");
  }

  /**
   * Using reflection, get returns an object of the specified type with the specified id from the database. The returned Entity is typesafe, meaning that its actual type is that specified by the type
   * parameter.
   * 
   * @param id
   *          ID of the instance to get
   * @return Typesafe Business representing the id in the database
   */
  public static Entity get(String id)
  {
    EntityDAOIF entityDAOIF = EntityDAO.get(id);

    if (entityDAOIF instanceof RelationshipDAO)
    {
      return Relationship.instantiate((RelationshipDAOIF) entityDAOIF);
    }
    else if (entityDAOIF instanceof BusinessDAO)
    {
      return Business.instantiate((BusinessDAOIF) entityDAOIF);
    }
    else if (entityDAOIF instanceof StructDAO)
    {
      return Struct.instantiate((StructDAOIF) entityDAOIF);
    }

    throw new UnexpectedTypeException("ID [" + id + "] is not an Entity");
  }

  /**
   * Writes to standard out all attribute names and their values of this BusinessDAO instance. All values that are keys are dereferenced and the values referenced by those keys are returned.
   * 
   * <br/>
   * <b>Precondition:</b> true
   * 
   */
  public void printAttributes()
  {
    this.entityDAO.printAttributes();
  }

  /**
   * Returns if an attribute of the Entity has been modified from its orginal value loaded from the database.
   * 
   * @param name
   *          The name of the attribute
   * @return
   */
  public boolean isModified(String name)
  {
    return entityDAO.getAttribute(name).isModified();
  }

  /**
   * @return Key is a required field, but the default implementation is an empty string. However, this method should be overwritten in child classes to return meaningful key values. Key values must be
   *         unique for all entities which are part of the same type hierarchy.
   */
  protected String buildKey()
  {
    return "";
  }

  /**
   * The String representation of this entity.
   */
  public String toString()
  {
    return this.getKeyName();
  }

  /**
   * *************************************************************************** ****
   */
  /** Generated accessor methods * */
  /**
   * *************************************************************************** ****
   */

  public static java.lang.String KEYNAME    = "keyName";

  public static java.lang.String SITEMASTER = "siteMaster";

  public String getKeyName()
  {
    return getValue(KEYNAME);
  }

  public void validateKeyName()
  {
    this.validateAttribute(KEYNAME);
  }

  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getKeyNameMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.business.Entity.CLASS);
    return mdClassIF.definesAttribute(KEYNAME);
  }

  public void setKeyName(String value)
  {
    if (value == null)
    {
      setValue(KEYNAME, "");
    }
    else
    {
      setValue(KEYNAME, value);
    }
  }

  public String getSiteMaster()
  {
    return getValue(SITEMASTER);
  }

  public void validateSiteMaster()
  {
    this.validateAttribute(SITEMASTER);
  }

  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getSiteMasterMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.business.Entity.CLASS);
    return mdClassIF.definesAttribute(SITEMASTER);
  }

  public void setSiteMaster(String value)
  {
    if (value == null)
    {
      setValue(SITEMASTER, "");
    }
    else
    {
      setValue(SITEMASTER, value);
    }
  }

  public static void getAllInstances(GeneratedEntityQuery query, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    SelectablePrimitive selectablePrimitive = (SelectablePrimitive) query.get("id");

    if (sortAttribute != null)
    {
      Selectable attribute;

      if (sortAttribute.contains("-"))
      {
        String[] attributeNames = sortAttribute.split("-");

        AttributeStruct attributeStruct = (AttributeStruct) query.get(attributeNames[0]);
        attribute = attributeStruct.get(attributeNames[1]);
      }
      else
      {
        attribute = query.get(sortAttribute);
      }

      if (attribute instanceof AttributeEnumeration)
      {
        attribute = ( (AttributeEnumeration) attribute ).aCharacter("enumName");
      }
      else if (attribute instanceof AttributeReference)
      {
        attribute = ( (AttributeReference) attribute ).aCharacter("keyName");
      }
      else if (attribute instanceof AttributeLocal)
      {
        attribute = ( (AttributeLocal) attribute ).getSessionLocale();
      }

      selectablePrimitive = (SelectablePrimitive) attribute;
    }

    if (ascending)
    {
      query.ORDER_BY_ASC(selectablePrimitive, sortAttribute);
    }
    else
    {
      query.ORDER_BY_DESC(selectablePrimitive, sortAttribute);
    }

    if (pageSize != 0 && pageNumber != 0)
    {
      query.restrictRows(pageSize, pageNumber);
    }
  }

  @Override
  public boolean equals(Object obj)
  {
    if (! ( obj instanceof ComponentIF ))
    {
      return false;
    }

    ComponentIF comp = (ComponentIF) obj;
    return this.getId().equals(comp.getId());
  }

  public void addMultiItem(String name, String item)
  {
    entityDAO.addItem(name, item);
  }

  public void replaceMultiItems(String name, Collection<String> values)
  {
    entityDAO.replaceItems(name, values);
  }

  public void removeMultiItem(String name, String item)
  {
    entityDAO.removeItem(name, item);
  }

  public void clearMultiItems(String name)
  {
    entityDAO.clearItems(name);
  }

  public List<? extends Business> getMultiItems(String name)
  {
    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) entityDAO.getAttribute(name);
    Set<String> ids = ( attribute ).getCachedItemIdSet();
    MdAttributeConcreteDAOIF mdAttribute = attribute.getMdAttributeConcrete();

    return loadSetValues(ids, mdAttribute);
  }

  /**
   * @return the disconnected
   */
  public boolean isDisconnected()
  {
    return this.entityDAO.isDisconnected();
  }

  /**
   * @param _disconnected
   *          the disconnected to set
   */
  public void setDisconnected(boolean _disconnected)
  {
    this.entityDAO.setDisconnected(true);
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
