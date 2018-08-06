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

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.dataaccess.attributes.AttributeException;
import com.runwaysdk.dataaccess.attributes.AttributeSet;
import com.runwaysdk.dataaccess.attributes.AttributeTypeException;
import com.runwaysdk.dataaccess.attributes.tranzient.Attribute;
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeBlob;
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeStruct;
import com.runwaysdk.dataaccess.metadata.MdTransientDAO;
import com.runwaysdk.dataaccess.transaction.TransactionItem;

public class TransientDAO extends ComponentDAO implements TransactionItem, Serializable
{

  /**
   *
   */
  private static final long        serialVersionUID      = 37033128490277734L;

  /**
   * Map of Attribute objects the component has. They are of a name-value pair
   * relation. <br/>
   * <b>invariant</b> attributeMap != null
   */
  protected Map<String, Attribute> attributeMap;

  /**
   * Id used for AttributeProblems (not messages). New instances that fail will
   * have a different ID on the client.
   */
  private String                   problemNotificationId = "";

  /**
   * Indicates if this is a new instance. If it is new, then the records that
   * represent this component have not been created.
   */
  private boolean                  isNew = false;
  
  /**
   * The default constructor, does not set any attributes
   */
  public TransientDAO()
  {
    super();
  }

  /**
   * Constructs a TransientDAO from the given hashtable of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> classType != null
   * 
   * @param attributeMap
   * @param classType
   */
  public TransientDAO(Map<String, Attribute> attributeMap, String transientType)
  {
    super(transientType);
    this.attributeMap = attributeMap;
    this.linkAttributes();
  }

  /**
   * Returns a boolean that indicates if this is a new instance (i.e. has not been committed to the database).
   *
   * <br/><b>Precondition:</b> true <br/><b>Postcondition:</b> true
   *
   * @return a boolean that indicates if this is a new instance
   */
  public boolean isNew()
  {
    return this.isNew;
  }

  /**
   * Do not call this method unless you know what you are doing.  Sets the new state of the object.
   *
   * <br/><b>Precondition:</b> true <br/><b>Postcondition:</b> true
   */
  public void setIsNew(boolean isNew)
  {
    this.isNew = isNew;
  }
  
  /**
   * Returns the Id used for AttributeNotifications. New instances that fail
   * will have a different ID on the client.
   * 
   * @return notification oid.
   */
  public String getProblemNotificationId()
  {
    if (this.problemNotificationId.equals(""))
    {
      return this.getOid();
    }
    else
    {
      return this.problemNotificationId;
    }
  }

  /**
   * Sets the ID used for AttributeProblems. Should be called on new instances,
   * since the DTO on the client will have a different ID than the one
   * automatically created for this object.
   * 
   * @param problemNotificationId
   */
  public void setProblemNotificationId(String problemNotificationId)
  {
    this.problemNotificationId = problemNotificationId;
  }

  /**
   * Adds all of the attributes in the given map to this object. This is used
   * for converting ValueObjects into Transient objects.
   * 
   * @param foreignAttributeMap
   *          Map contains attributes derived from a Value Query
   */
  protected void addAttributes(Map<String, Attribute> foreignAttributeMap)
  {
    this.attributeMap.putAll(foreignAttributeMap);
    this.linkAttributes();
  }

  /**
   * Iterates over the map of Attributes, setting <b>this</b> as their
   * containing Component.<br>
   * <br>
   * <b>Precondition:</b> attributeMap != null<br>
   * <b>Postcondition:</b> attributeMap.get(value).getContainingComponent =
   * <b>this</b>
   */
  private void linkAttributes()
  {
    for (Attribute attribute : this.attributeMap.values())
    {
      attribute.setContainingComponent(this);
    }
    return;
  }

  /**
   * Not to be called from the constructor.
   */
  public void setTypeName(String entityType)
  {
    this.componentType = entityType;
  }

  /**
   * Returns the Attribute object with the given name.
   * 
   * <br/>
   * <b>Precondition:</b> name != null <br/>
   * <b>Precondition:</b> !name.trim().equals("") <br/>
   * <b>Precondition:</b> Attribute name is valid for the class of this
   * Component <br/>
   * <b>Postcondition:</b> return value != null
   * 
   * @param name
   *          name of the attribute
   * @return Attribute object with the given name
   * @throws DataAccessException
   *           if the attribute with the given name does not exist for the class
   *           of the Component
   */
  public AttributeIF getAttributeIF(String name)
  {
    AttributeIF returnAttribute = (AttributeIF) this.attributeMap.get(name);

    if (returnAttribute == null)
    {
      String error = "An attribute named [" + name + "] does not exist on type [" + this.getType() + "]";
      throw new AttributeDoesNotExistException(error, name, this.getMdClassDAO());
    }

    return returnAttribute;
  }

  /**
   * Returns true if the component has an attribute with the given name, false
   * otherwise.
   * 
   * @param name
   * @return true if the component has an attribute with the given name, false
   *         otherwise.
   */
  public boolean hasAttribute(String name)
  {
    if (this.attributeMap.get(name) != null)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Returns an array of the Attributes for this Component.<br>
   * <br>
   * <b>Precondition:</b> true<br>
   * <b>Postcondition:</b> array.length = attributeMap.size();
   */
  public AttributeIF[] getAttributeArrayIF()
  {
    AttributeIF[] array = new Attribute[this.attributeMap.size()];
    int index = 0;

    for (AttributeIF attribute : this.attributeMap.values())
    {
      array[index] = attribute;
      index++;
    }

    return array;
  }

  /**
   * Returns an array of attribute objects.
   * 
   * @return array of attribute objects.
   */
  public Attribute[] getAttributeArray()
  {
    return (Attribute[]) getAttributeArrayIF();
  }

  /**
   * Returns a MdTransient that defines this object's classs.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   * 
   * @return a MdTransient that defines this object's classs.
   */
  public MdTransientDAOIF getMdTransientDAO()
  {
    return MdTransientDAO.getMdTransientDAO(this.getType());
  }

  /**
   * Returns a LinkedList of MdAttributeIF objects representing metadata for
   * each attribute of this object's class.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   * 
   * @return a LinkedList of MdAttributeIF objects representing metadata for
   *         each attribute of this object's class.
   */
  public List<? extends MdAttributeDAOIF> getMdAttributeDAOs()
  {
    return this.getMdClassDAO().getAllDefinedMdAttributes();
  }

  /**
   * Returns the attribute object of the given name.
   * 
   * @param name
   * @return
   */
  public Attribute getAttribute(String name)
  {
    return (Attribute) this.getAttributeIF(name);
  }

  /**
   * Returns a new TransientDAO with the attributes defined for the class with
   * the given type. Some attributes will contain default values, as defined in
   * the attribute metadata. Otherwise, the attributes will be blank.
   * 
   * <br/>
   * <b>Precondition:</b> classType parameter represents a valid classType in
   * the database. <br/>
   * <b>Precondition:</b> classType must not be abstract, otherwise a DataAccess
   * exception will be thrown <br/>
   * <b>Postcondition:</b> TransientDAO returned is an instance of the given
   * classType. The BusinessDAO contains all attributes defined for that
   * classType.
   * 
   * @param classType
   *          Valid classType.
   * @return TransientDAO instance of the given class
   * 
   */
  public static TransientDAO newInstance(String classType)
  {
    return TransientDAOFactory.newInstance(classType);
  }

  /**
   * Validates the attribute with the given name. If the attribute is not valid,
   * then an AttributeException exception is thrown.
   * 
   * <br/>
   * <b>Precondition:</b> name != null <br/>
   * <b>Precondition:</b> !name.trim().equals("") <br/>
   * <b>Precondition:</b> An attribute of the given name exists for instances of
   * this class.
   * 
   * @param name
   *          name of the attribute
   * @throws AttributeException
   *           if the attribute is not valid.
   */
  public void validateAttribute(String name)
  {
    Attribute attribute = this.getAttribute(name);

    attribute.validateEverything(attribute.getValue());
  }

  /**
   * Sets the attribute of the given name with the given value.
   * 
   * <br/>
   * <b>Precondition:</b> name != null <br/>
   * <b>Precondition:</b> !name.trim().equals("") <br/>
   * <b>Precondition:</b> value != null <br/>
   * <b>Precondition:</b> Attribute name is valid for this EntityDAO's class <br/>
   * <b>Postcondition:</b> Attribute is set with the given value
   * 
   * <br/>
   * <b>modifies:</b> this.componentType if the attribute being modified is the
   * name of the class.
   * 
   * @param name
   *          name of the attribute
   * @param value
   *          value to assign to the given attribute
   * @throws DataAccessException
   *           if the attribute with the given name does not exist for the class
   *           of the EntityDAO
   */
  public void setValue(String name, String value)
  {
    // Setting and validating are a single method
    Attribute attribute = this.getAttribute(name);

    // Use blob specific validation if necessary.
    if (attribute instanceof AttributeBlob)
    {
      ( (AttributeBlob) attribute ).validate( ( (AttributeBlob) attribute ).getBlobAsBytes(), attribute.getMdAttribute());
    }
    else
    {
      attribute.setValueAndValidate(value);
    }

    if (name.equals(EntityInfo.TYPE))
    {
      this.componentType = value;
    }
  }

  /**
   * Some attribute types store objects instead of Strings.
   * 
   * @param name
   * @param _object
   */
  public void setValue(String name, Object _object)
  {
    if (_object instanceof String)
    {
      this.setValue(name, (String) _object);
    }
    else
    {
      Attribute attribute = this.getAttribute(name);

      attribute.setValue(_object);
    }
  }

  /**
   * Adds an item to an enumerated atribute. If the attribute does not allow
   * multiplicity, the <code>enumItemID</code> replaces the previous item.
   * 
   * @param name
   *          Name of the enumerated attribute
   * @param value
   *          Value to be added to the attribute
   */
  public void addItem(String name, String value)
  {
    try
    {
      AttributeSet attrSet = (AttributeSet) this.getAttribute(name);
      attrSet.addItem(value);
    }
    catch (ClassCastException e)
    {
      String error = "Attribute [" + name + "] on type [" + getType() + "] is not an enumerated attribute";
      throw new AttributeTypeException(error);
    }
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
  public void replaceItems(String name, Collection<String> values)
  {
    try
    {
      AttributeSet attrSet = (AttributeSet) this.getAttribute(name);
      attrSet.replaceItems(values);
    }
    catch (ClassCastException e)
    {
      String error = "Attribute [" + name + "] on type [" + getType() + "] is not an enumerated attribute";
      throw new AttributeTypeException(error);
    }
  }

  /**
   * Deletes an item from an Enumerated Attribute.
   * 
   * @param name
   *          Name of the enumerated attribute
   * @param value
   *          Value to be removed from the attribute
   */
  public void removeItem(String name, String value)
  {
    try
    {
      AttributeSet attrSet = (AttributeSet) this.getAttribute(name);
      attrSet.removeItem(value);
    }
    catch (ClassCastException e)
    {
      String error = "Attribute [" + name + "] on type [" + getType() + "] is not an enumerated attribute";
      throw new AttributeTypeException(error);
    }
  }

  /**
   * Deletes an item from an Enumerated Attribute.
   * 
   * @param name
   *          Name of the enumerated attribute
   * @param value
   *          Value to be removed from the attribute
   */
  public void clearItems(String name)
  {
    try
    {
      AttributeSet attrSet = (AttributeSet) this.getAttribute(name);
      attrSet.clearItems();
    }
    catch (ClassCastException e)
    {
      String error = "Attribute [" + name + "] on type [" + getType() + "] is not an enumerated attribute";
      throw new AttributeTypeException(error);
    }
  }

  /**
   * Sets the value of a struct attribute.
   * 
   * @param compostiteAttributeName
   * @param attributeName
   * @param value
   */
  public void setStructValue(String compostiteAttributeName, String attributeName, String value)
  {
    // Setting and validating are a single method
    AttributeIF attribute = this.getAttributeIF(compostiteAttributeName);
    if (attribute instanceof AttributeStruct)
    {
      ( (AttributeStruct) attribute ).setValue(attributeName, value);
    }
    else
    {
      String error = "Attribute [" + compostiteAttributeName + "] is not a struct attribute";
      throw new AttributeException(error);
    }
  }

  /**
   * Sets the value of a struct blob attribute
   * 
   * @param structAttributeName
   * @param blobName
   * @param value
   */
  public void setStructBlob(String structAttributeName, String blobName, byte[] value)
  {
    // Setting and validating are a single method
    AttributeIF attribute = this.getAttributeIF(structAttributeName);
    if (attribute instanceof AttributeStruct)
    {
      ( (AttributeStruct) attribute ).setBlob(blobName, value);
    }
    else
    {
      String error = "Attribute [" + structAttributeName + "] is not a struct attribute";
      throw new AttributeException(error);
    }
  }

  /**
   *
   */
  public byte[] getBlob(String attributeName)
  {
    AttributeBlobIF blob = (AttributeBlobIF) this.getAttributeIF(attributeName);
    return blob.getBlobAsBytes();
  }

  /**
   * 
   * @param attributeName
   * @param value
   */
  public void setBlob(String attributeName, byte[] value)
  {
    AttributeBlob blob = (AttributeBlob) this.getAttribute(attributeName);
    blob.setBlobAsBytes(value);
  }

  /**
   * Sets the value of a struct attribute.
   * 
   * @param structName
   *          The name of the struct
   * @param attributeName
   *          The name of the attribute (inside the struct)
   * @param value
   *          The value to set
   * @throws AttributeException
   *           If the supplied structName isn't really a struct
   */
  public void addStructItem(String structName, String attributeName, String value)
  {
    // Setting and validating are a single method
    AttributeIF attribute = this.getAttributeIF(structName);
    if (attribute instanceof AttributeStruct)
    {
      ( (AttributeStruct) attribute ).addItem(attributeName, value);
    }
    else
    {
      String error = "Attribute [" + structName + "] is not a struct attribute";
      throw new AttributeException(error);
    }
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
   * @throws AttributeException
   *           If the supplied structName isn't really a struct
   */
  public void removeStructItem(String structName, String attributeName, String value)
  {
    // Setting and validating are a single method
    AttributeIF attribute = this.getAttributeIF(structName);
    if (attribute instanceof AttributeStruct)
    {
      ( (AttributeStruct) attribute ).removeItem(attributeName, value);
    }
    else
    {
      String error = "Attribute [" + structName + "] is not a struct attribute";
      throw new AttributeException(error);
    }
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
   * 
   * @throws AttributeException
   *           If the supplied structName isn't really a struct
   */
  public void replaceStructItems(String structName, String attributeName, Collection<String> values)
  {
    // Setting and validating are a single method
    AttributeIF attribute = this.getAttributeIF(structName);
    if (attribute instanceof AttributeStruct)
    {
      ( (AttributeStruct) attribute ).replaceItems(attributeName, values);
    }
    else
    {
      String error = "Attribute [" + structName + "] is not a struct attribute";
      throw new AttributeException(error);
    }
  }

  /**
   * Clears all the values of a struct enumeration attribute.
   * 
   * @param structName
   *          The name of the struct
   * @param attributeName
   *          The name of the attribute (inside the struct)
   * @throws AttributeException
   *           If the supplied structName isn't really a struct
   */
  public void clearStructItems(String structName, String attributeName)
  {
    // Setting and validating are a single method
    AttributeIF attribute = this.getAttributeIF(structName);
    if (attribute instanceof AttributeStruct)
    {
      ( (AttributeStruct) attribute ).clearItems(attributeName);
    }
    else
    {
      String error = "Attribute [" + structName + "] is not a struct attribute";
      throw new AttributeException(error);
    }
  }

  /**
   * Finalizes attributes, such as required attributes.
   * 
   * @return oid of the object.
   */
  public String apply()
  {
    return this.save();
  }

  /**
   * This is a hook method for aspects so that the transient object apply can be
   * managed by transaction management.
   * 
   * @return oid of the object.
   */
  private String save()
  {
    List<? extends MdAttributeDAOIF> mdAttributeList = this.getMdClassDAO().definesAttributes();

    for (MdAttributeDAOIF mdAttribute : mdAttributeList)
    {
      String fieldName = mdAttribute.definesAttribute();

      if (this.hasAttribute(fieldName))
      {
        Attribute attribute = this.getAttribute(fieldName);
        attribute.validateRequired(attribute.getValue(), mdAttribute);
      }
    }

    return this.getOid();
  }

  /**
   * Sets isNew = false and sets all attributes to isModified = false.
   * 
   */
  public void setCommitState()
  {
    this.setIsNew(false);

    this.problemNotificationId = "";

    Attribute[] attributeArray = this.getAttributeArray();

    for (int i = 0; i < attributeArray.length; i++)
    {
      attributeArray[i].setCommitState();
    }
  }

}
