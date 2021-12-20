/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
import java.util.List;
import java.util.Locale;

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.dataaccess.attributes.AttributeException;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.session.Session;
import com.runwaysdk.util.IdParser;


public abstract class ComponentDAO implements Comparable<ComponentDAO>, ComponentDAOIF, Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 4614560027810789102L;

  /**
   * The name of this component's type.
   */
  protected String  componentType;

  /**
   * The default constructor, does not set any attributes
   */
  public ComponentDAO()
  {
    this.componentType = null;
  }

  /**
   * Constructs a Component from the given hashtable of Attributes.
   *
   * <br/><b>Precondition:</b> attributeMap != null
   * <br/><b>Precondition:</b> componentType != null
   *
   * @param componentType
   */
  public ComponentDAO(String componentType)
  {
    this.componentType = componentType;
  }


  /**
   * Returns the OID of this Component.
   *
   * <br/><b>Precondition:</b> true <br/><b>Postcondition:</b> The state of
   * the InfoObject does not change <br/><b>Postcondition:</b> return value !=
   * null
   *
   * @return The OID of this Component.
   */
  public String getOid()
  {
    return this.getAttributeIF(ComponentInfo.OID).getValue();
  }

  /**
   * Returns the root oid of this component.
   *
   * @return root oid of this component.
   */
  public String getBaseOid()
  {
    return IdParser.parseRootFromId(this.getOid());
  }

  /**
   * Returns the name of the type of this Component instance.
   *
   * <br/><b>Precondition:</b> true <br/><b>Postcondition:</b> The state of
   * the Component does not change <br/><b>Postcondition:</b> return value !=
   * null
   *
   * @return The name of the class of this Component.
   */
  public String getType()
  {
    return this.componentType;
  }

  public String getKey()
  {
    return this.getValue(ComponentInfo.KEY);
  }
  
  /**
   * Returns a {@link MdClassDAOIF}  that defines this Component's class.
   *
   * <br/><b>Precondition:</b> true <br/><b>Postcondition:</b> true
   *
   * @return a {@link MdClassDAOIF}   that defines this Component's class.
   */
  public MdClassDAOIF getMdClassDAO()
  {
    return MdClassDAO.getMdClassDAO(this.getType());
  }

  /**
   * Returns the value of the given attribute of this Component.
   *
   * <br/><b>Precondition:</b> name != null.
   * <br/><b>Precondition:</b> !name.trim().equals("").
   * <br/><b>Precondition:</b> An attribute of the given name exists for instances of this class.
   * <br/><b>Postcondition:</b> Returns the value of the given attribute value != null.
   *
   * @param name
   *          name of the attribute
   * @return the value of the given attribute
   * @throws DataAccessException
   *           if no attribute with the given name exists for instances of this
   *           class
   */
  public String getValue(String name)
  {
    AttributeIF attribute = this.getAttributeIF(name);

    String value = new String("");

    value = attribute.getValue();

    return value;
  }


  /**
   * Some attributes store objects instead of strings.
   * @param name
   * @return object stored on the attribute.
   */
  public Object getObjectValue(String name)
  {
    AttributeIF attribute = this.getAttributeIF(name);

    Object value = attribute.getObjectValue();

    return value;
  }

  /**
   * Returns the value of the given attribute of this Componet.
   *
   * <br/><b>Precondition:</b> name != null.
   * <br/><b>Precondition:</b> !name.trim().equals("").
   * <br/><b>Precondition:</b> An attribute of the given name exists for instances of this class.
   * <br/><b>Postcondition:</b> Returns the value of the given attribute value != null.
   *
   * @param name
   *          name of the attribute
   * @return the value of the given attribute
   * @throws DataAccessException
   *           if no attribute with the given name exists for instances of this
   *           class
   */
  public byte[] getBlob(String name)
  {
    AttributeBlobIF attribute = (AttributeBlobIF)this.getAttributeIF(name);

    return attribute.getBlobAsBytes();
  }

  /**
   * @param compostiteAttributeName
   * @param attributeName
   * @return the value of a struct attribute
   */
  public String getStructValue(String structAttributeName, String attributeName)
  {
    AttributeStructIF attribute = (AttributeStructIF) this.getAttributeIF(structAttributeName);
    if (attribute instanceof com.runwaysdk.dataaccess.attributes.entity.AttributeStruct ||
        attribute instanceof com.runwaysdk.dataaccess.attributes.tranzient.AttributeStruct)
    {
      return attribute.getValue(attributeName);
    }
    else
    {
      String error = "Attribute [" + structAttributeName + "] on type [" + getType()
          + "] is not a struct attribute";
      throw new AttributeException(error);
    }
  }

  /**
   * Returns the value for the attribute that matches the given locale (or a best fit).
   *
   * @param localAttributeName
   * @param local
   * @return the value of a local attribute
   */
  public String getLocalValue(String localAttributeName, Locale locale)
  {
    AttributeLocalIF attribute = (AttributeLocalIF) this.getAttributeIF(localAttributeName);
    if (attribute instanceof com.runwaysdk.dataaccess.attributes.entity.AttributeLocal ||
        attribute instanceof com.runwaysdk.dataaccess.attributes.tranzient.AttributeLocal)
    {
      return attribute.getValue(locale);
    }
    else
    {
      String error = "Attribute [" + localAttributeName + "] on type [" + getType()
          + "] is not a local attribute";
      throw new AttributeException(error);
    }
  }

  /**
   * @param compostiteAttributeName
   * @param attributeName
   * @return the value of a struct attribute
   */
  public byte[] getStructBlob(String structAttributeName, String blobName)
  {
    AttributeStructIF attribute = (AttributeStructIF) this.getAttributeIF(structAttributeName);
    if (attribute instanceof com.runwaysdk.dataaccess.attributes.entity.AttributeStruct ||
        attribute instanceof com.runwaysdk.dataaccess.attributes.tranzient.AttributeStruct)
    {
      return attribute.getBlob(blobName);
    }
    else
    {
      String error = "Attribute [" + structAttributeName + "] on type [" + getType()
          + "] is not a struct attribute";
      throw new AttributeException(error);
    }
  }

  /**
   * Returns the Attribute object with the given name.
   *
   * <br/><b>Precondition:</b> name != null
   * <br/><b>Precondition:</b> !name.trim().equals("")
   * <br/><b>Precondition:</b> Attribute name is valid for the class of this Component
   * <br/><b>Postcondition:</b> return value != null
   *
   * @param name
   *          name of the attribute
   * @return Attribute object with the given name
   * @throws DataAccessException
   *           if the attribute with the given name does not exist for the class
   *           of the Component
   */
  public abstract AttributeIF getAttributeIF(String name);

  /**
   * Returns true if the component has an attribute with the given name, false otherwise.
   * @param name
   * @return true if the component has an attribute with the given name, false otherwise.
   */
  public abstract boolean hasAttribute(String name);

  /**
   * Returns an array of the Attributes for this Component.<br>
   * <br>
   * <b>Precondition:</b> true<br>
   * <b>Postcondition:</b> array.length = attributeMap.size();
   */
  public abstract AttributeIF[] getAttributeArrayIF();


  /**
   * Returns a boolean that indicates if this is a new instance (i.e. has not been committed to the database).
   *
   * <br/><b>Precondition:</b> true <br/><b>Postcondition:</b> true
   *
   * @return a boolean that indicates if this is a new instance
   */
  abstract public boolean isNew();

  /**
   * Do not call this method unless you know what you are doing.  Sets the new state of the object.
   *
   * <br/><b>Precondition:</b> true <br/><b>Postcondition:</b> true
   */
  abstract public void setIsNew(boolean isNew);

  /**
   * Returns a LinkedList of MdAttributeIF objects representing metadata for each attribute
   * of this object's class.
   *
   * <br/><b>Precondition:</b> true <br/><b>Postcondition:</b> true
   *
   * @return a LinkedList of MdAttributeIF objects representing metadata for each attribute
   * of this object's class.
   */
  public List<? extends MdAttributeDAOIF> getMdAttributeDAOs()
  {
    return this.getMdClassDAO().getAllDefinedMdAttributes();
  }

  /**
   * Returns a MdAttributeIF representing the attribute metadata of the attribute
   * with the given name.
   *
   * <br/><b>Precondition:</b> name != null <br/><b>Precondition:</b>
   * !name.trim().equals("")
   * <br/><b>Postcondition:</b> true
   *
   * @param name
   *          of the attribute.
   * @return MdAttributeIF representing the attribute metadata of the attribute with
   *         the given name
   */
  public MdAttributeDAOIF getMdAttributeDAO(String name)
  {
    MdAttributeDAOIF mdAttributeDAOIF = this.getAttributeIF(name).getMdAttribute();

    if (mdAttributeDAOIF == null)
    {
      String error = "An attribute named [" + name + "] does not exist on type [" + this.getType() + "]";
      throw new AttributeDoesNotExistException(error, name, this.getMdClassDAO());
    }

    return mdAttributeDAOIF;
  }

  /**
   * Writes to standard out all attribute names and their values of this
   * Component instance. All values that are keys are dereferenced and the
   * values referenced by those keys are returned.
   *
   * <br/><b>Precondition:</b> true
   *
   */
  public void printAttributes()
  {
    printAttributes(this, "");
  }


  /**
   *
   * @param componentObject
   * @param indent
   */
  protected static void printAttributes(ComponentDAO componentObject, String indent)
  {
    AttributeIF[] attributes = componentObject.getAttributeArrayIF();
    System.out.println(indent + "Printing attributes:");
    System.out.println(indent + "------------------------");
    for (int i = 0; i < attributes.length; i++)
    {
      printAttribute(attributes[i], indent);
    }
  }

  /**
   *
   */
  private static void printAttribute(AttributeIF attribute, String indent)
  {
    System.out.print(indent + "Name: " + attribute.getName());

    System.out.print(indent + "   Label: " + attribute.getDisplayLabel(Session.getCurrentLocale()));

    if (!(attribute instanceof AttributeBlobIF))
    {
      System.out.print("   Value: " + attribute.getValue());
    }

    if (attribute instanceof AttributeEnumerationIF)
    {
      AttributeEnumerationIF attributeEnumeration = (AttributeEnumerationIF) attribute;
      BusinessDAOIF[] componentObjectArray = attributeEnumeration.dereference();

      for (int j = 0; j < componentObjectArray.length; j++)
      {
        printAttributes((ComponentDAO)componentObjectArray[j], indent + "  ");
      }
    }
    else if (attribute instanceof AttributeStructIF)
    {
      AttributeStructIF attributeStruct = (AttributeStructIF) attribute;

      AttributeIF[] structAttributeArray = attributeStruct.getAttributeArrayIF();

      System.out.println("");
      for (int i = 0; i < structAttributeArray.length; i++)
      {
        printAttribute(structAttributeArray[i], indent + "  ");
      }
    }

    System.out.println("");
  }

  public int compareTo(ComponentDAO obj)
  {
    return this.getOid().compareTo(obj.getOid());
  }

  /**
   * Compares the OID field.  Returns true if they are the same value, false otherwise.
   */
  public boolean equals(Object obj)
  {
    if (!(obj instanceof ComponentDAO))
    {
      return false;
    }
    else
    {
      return this.getOid().equals(((ComponentDAO)obj).getOid());
    }
  }

  public int hashCode()
  {
    return this.getOid().hashCode();
  }
}
