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
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.AttributeStructIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdLocalizableDAOIF;
import com.runwaysdk.dataaccess.TransientDAO;
import com.runwaysdk.dataaccess.attributes.AttributeException;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.Session;

public abstract class Notification implements MutableWithStructs, LocalizableIF, Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -4323327353912623761L;

  /**
   * All interaction with the core is delegated through this object. This should
   * NOT be accessed outside of this class.
   */
  private TransientDAO      transientDAO;

  /**
   * The locale of the session user.
   */
  protected Locale          locale;

  /**
   * The unlocalized, developer-only error message
   */
  private String            devMessage;

  public Notification()
  {
    this(null);
  }

  public Notification(String devMessage)
  {
    this.devMessage = devMessage;
    transientDAO = TransientDAO.newInstance(getDeclaredType());
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
  
  void setTransientDAO(TransientDAO transientDAO)
  {
    this.transientDAO = transientDAO;
  }


  /**
   * Sets the locale used for the localized message.
   * 
   * @param locale
   */
  public void setLocale(Locale locale)
  {
    this.locale = locale;
  }

  /**
   * Returns the locale set for this exception. If none is externally specified,
   * it returns the locale for the Session.
   * 
   * @return the locale set for this exception.
   */
  public Locale getLocale()
  {
    if (this.locale == null)
    {
      return Session.getCurrentLocale();
    }
    else
    {
      return this.locale;
    }
  }

  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   */
  public String getLocalizedMessage()
  {
    return this.localize(this.getLocale());
  }

  /**
   * Returns a message set by developers for developers.
   * 
   * @return a message set by developers for developers.
   */
  public String getDeveloperMessage()
  {
    return this.devMessage;
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

  public String toString()
  {
    String toString = getDeclaredType();
    if (devMessage != null)
      toString += ": " + devMessage;
    return toString;
  }

  /**
   * Returns the type of this Entity. Generic entity objects can represent
   * specific types - this method returns the declared type of the object.
   * 
   * @return The declared type of this object
   */
  protected abstract String getDeclaredType();

  protected String replace(String template, String replaceMe, Object newValue)
  {
    if (newValue == null)
    {
      return template;
    }

    String toString = newValue.toString();
    if (toString.length() == 0)
    {
      return template;
    }
    return template.replace(replaceMe, toString);
  }

  /**
   * A simple termination of the super chain on this method signature. Returns
   * the localized message template with no substitutions.
   * 
   * @param locale
   * @param message
   * @return
   */
  public String localize(Locale locale)
  {
    return getLocalizedTemplate(locale);
  }

  /**
   * Returns the localized message template with no substitutions.
   * 
   * @param locale
   * @param message
   * @return
   */
  public String getLocalizedTemplate(Locale locale)
  {
    MdLocalizableDAOIF metadata = (MdLocalizableDAOIF) transientDAO.getMdClassDAO();
    return metadata.getMessage(locale);
  }

  /**
   * Returns the ID of this exception.
   * 
   * @see com.runwaysdk.ComponentIF#getId()
   */
  public String getId()
  {
    return transientDAO.getId();
  }

  public String getKey()
  {
    return transientDAO.getKey();
  }

  /**
   * Returns a MdAttributeIF that defines the attribute with the given name.
   * 
   * @param name
   *          Name of the attribute
   * @see com.runwaysdk.ComponentIF#getMdAttributeDAO(java.lang.String)
   */
  public MdAttributeDAOIF getMdAttributeDAO(String name)
  {
    return transientDAO.getMdAttributeDAO(name);
  }

  /**
   * Returns a List of MdAttributeIFs that define each attribute on this
   * exception.
   * 
   * @see com.runwaysdk.ComponentIF#getMdAttributeDAOs()
   */
  public List<? extends MdAttributeDAOIF> getMdAttributeDAOs()
  {
    return transientDAO.getMdAttributeDAOs();
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
   * Returns the fully qualified type name for this exception
   * 
   * @see com.runwaysdk.ComponentIF#getType()
   */
  public String getType()
  {
    return transientDAO.getType();
  }

  /**
   * Returns <code>true</code> if this exception has not yet been localized.
   * 
   * @see com.runwaysdk.ComponentIF#isNew()
   */
  public boolean isNew()
  {
    return transientDAO.isNew();
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
   * Writes to standard out all attribute name-value pairs on this exception.
   * All values that are keys are dereferenced to improve clarity in output.
   * 
   * @see com.runwaysdk.ComponentIF#printAttributes()
   */
  public void printAttributes()
  {
    transientDAO.printAttributes();
  }

  /**
   * Returns the value of the given attribute of this exception.
   * 
   * @see com.runwaysdk.ComponentIF#getValue(java.lang.String)
   */
  public String getValue(String name)
  {
    return transientDAO.getValue(name);
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
   * A generic, type-unsafe getter for struct attributes that takes the
   * attribute and struct names as Strings, and returns the value as a String
   * 
   * @param structName
   *          String name of the struct
   * @param name
   *          String name of the desired attribute
   * @return String representation of the struct value
   */
  public String getStructValue(String structName, String attributeName)
  {
    return transientDAO.getStructValue(structName, attributeName);
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
    return transientDAO.getStructBlob(structName, blobName);
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
   * A generic, type-unsafe setter that takes the attribute name a and value as
   * Strings
   * 
   * @param name
   *          String name of the attribute
   * @param value
   *          String representation of the value
   */
  public void setValue(String name, String value)
  {
    transientDAO.setValue(name, value);
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
    transientDAO.setValue(name, _object);
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
    transientDAO.setBlob(blobName, value);
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
    transientDAO.addStructItem(structName, attributeName, value);
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
    transientDAO.addItem(name, item);
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
    transientDAO.clearItems(name);
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
    transientDAO.replaceItems(name, values);
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
    transientDAO.removeItem(name, item);
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

    transientDAO.setStructValue(structName, attributeName, value);
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
    transientDAO.setStructBlob(structAttributeName, blobName, value);
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
    transientDAO.validateAttribute(name);
  }

  /**
   * This method is here simply to satisfy an interface. This will probably
   * never be called.
   */
  public void apply()
  {
    transientDAO.apply();
  }

  /**
   * Finalizes the Notification. Transaction will obtain a reference to this
   * notification and prevent the transaction from completing if the
   * notification is a problem.
   */
  public abstract void throwIt();

  public void delete()
  {
    // Balk. Exceptions live in memory only, so there is nothign to delete.
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

  public static Notification instantiate(TransientDAO transientDAO)
  {
    Notification object;
    try
    {
      Class<?> clazz = LoaderDecorator.load(transientDAO.getType());
      Constructor<?> con = clazz.getConstructor();
      object = (Notification) con.newInstance();

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
}
