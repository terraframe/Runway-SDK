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
package com.runwaysdk.dataaccess.attributes.tranzient;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.TransientDAO;
import com.runwaysdk.dataaccess.attributes.AttributeException;
import com.runwaysdk.dataaccess.attributes.EmptyValueProblem;
import com.runwaysdk.dataaccess.attributes.ImmutableAttributeProblem;
import com.runwaysdk.dataaccess.attributes.SystemAttributeProblem;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.session.Session;

public abstract class Attribute implements AttributeIF
{
  /**
   * 
   */
  private static final long serialVersionUID = -1251818478675195631L;

  /**
   * Canonical name of the attribute. <br>
   * <b>invariant </b> name != null <br>
   * <b>invariant </b> !name.trim().equals("")
   */
  final private String name;

  /**
   * The {@link TransientDAO} that contains this Attribute. Not defined on creation, but set by the
   * containing {@link TransientDAO. <br>
   * <b>invariant </b> containingComponent != null
   */
  private TransientDAO    containingTransientDAO;

  /**
   * Qualified name of the class that defines this attribute. <br>
   * <b>invariant </b> definingTransientType != null <br>
   * <b>invariant </b> !definingTransientType.equals("")
   */
  final private String definingTransientType;

  protected String mdAttributeKey;

  /**
   * Value of the attribute. <br>
   * <b>invariant </b> value != null
   */
  protected String       value;

  /**
   * Indicates if the value of this Attribute has been modified since it was last applied
   * to the database.
   */
  private boolean    isModified = false;

  /**
   * Creates an attribute with the given name and initializes the value to blank.
   *
   * <br>
   * <b>Precondition: </b> name != null <br>
   * <b>Precondition: </b> !name.trim().equals("") <br>
   * <b>Precondition: </b> definingTransientType != null <br>
   * <b>Precondition: </b> !definingTransientType().equals("") <br>
   * <b>Precondition: </b> definingTransientType is the name of a class that defines an attribute with
   * this name
   *
   * @param name name of the attribute
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType name of the class that defines this attribute
   */
  protected Attribute(String name, String mdAttributeKey, String definingTransientType)
  {
    this.name = name;
    this.mdAttributeKey = mdAttributeKey;
    this.definingTransientType = definingTransientType;
    this.value = "";
    this.containingTransientDAO = null;
  }

  /**
   * Creates an attribute with the given name and initializes the value to the given
   * value.
   *
   * <br>
   * <b>Precondition: </b> name != null <br>
   * <b>Precondition: </b> !name.trim().equals("") <br>
   * <b>Precondition: </b> definingTransientType != null <br>
   * <b>Precondition: </b> !definingTransientType.trim().equals("") <br>
   * <b>Precondition: </b> definingTransientType represents a class that defines an attribute with
   * this name <br>
   * <b>Precondition: </b> value != null
   *
   * @param name name of the attribute
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType name of the class that defines this attribute
   * @param value initial value of the attribute
   */
  protected Attribute(String name, String mdAttributeKey, String definingTransientType, String value)
  {
    this(name, mdAttributeKey, definingTransientType);
    this.value = value;
  }

  /**
   * Returns the name of the attribute.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> return value != null
   *
   * @return name of the attribute
   */
  public String getName()
  {
    return this.name;
  }

  /**
   * A convenience method that gets the MdAttribute and returns the display
   * label. Note that the display label in independent of the value of the
   * attribute - all instances of the attribute have the same display label.
   *
   * @param locale
   *
   * @return The Display Label for the attribute, as defined in the Metadata
   */
  public String getDisplayLabel(Locale locale)
  {
    return this.getMdAttribute().getDisplayLabel(locale);
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
    return this.getMdAttribute().getDisplayLabels();
  }

  /**
   * Returns true if the value of the attribute has been modified since the attribute was
   * last commited to the database, false otherwise.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   *
   * @return true if the attribute has been modified since it was last commited to the
   *         database, false otherwise.
   */
  public boolean isModified()
  {
    return isModified;
  }

  /**
   * Sets the attribute as modified.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   *
   * @param isModified true if the attribute has been modified false otherwise.
   */
  public void setModified(boolean isModified)
  {
    this.isModified = isModified;
  }

  /**
   * Sets the state of the attribute object after a transaction has been committed/
   *
   */
  public void setCommitState()
  {
    this.setModified(false);
  }

  /**
   * Returns the Component that contains this Attribute.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> return value != null
   *
   * @return the Component that contains this Attribute
   */
  public TransientDAO getContainingComponent()
  {
    if (this.containingTransientDAO == null)
    {
      String error = "Attribute [" + name + "]'s Contianing Component is null. It is the component's "
          + "responsiblity to call setContainingComponent() for all of its attributes.";
      throw new DataNotFoundException(error, MdTypeDAO.getMdTypeDAO(EntityInfo.CLASS));
    }
    return this.containingTransientDAO;
  }

  /**
   * Sets a reference to the object that contains this attribute
   *
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> this.getContainingComponent() == transientDAO
   *
   * @param container
   */
  public void setContainingComponent(TransientDAO transientDAO)
  {
    this.containingTransientDAO = transientDAO;
  }

  /**
   * Returns the name of the class that defines this attribute.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> return value != null
   *
   * @return name of the attribute
   */
  public String getDefiningClassType()
  {
    return this.definingTransientType;
  }

  /**
   * Returns the formatted value of the attribute.  Some attributes format
   * this value to something other than what is stored in the database.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> return value != null
   *
   * @return value of the attribute.
   */
  public String getValue()
  {
    return this.value;
  }

  /**
   * Some attributes store objects instead of strings.
   * @param name
   * @return object stored on the attribute.
   */
  public Object getObjectValue()
  {
    return this.getValue();
  }

  /**
   * Returns the exact string value as represented in the database.
   *
   * @return exact string value as represented in the database.
   */
  public String getRawValue()
  {
    return this.value;
  }


  /**
   * Checks the value, and sets it if it is valid.
   *
   * <br>
   * <b>Precondition: </b> value != null <br>
   * <b>Postcondition: </b> this.value = value this.value.equals(value)
   * this.validate(this.value) = <b>true </b>
   *
   * <br>
   * <b>modifies: </b> isModified() returns true, but only if the new value is
   *                   different from the old value
   *
   * @param value new value of the attribute
   *
   * @throws AttributeException if the value is invalid for this Attribute
   */
  public void setValue(String value)
  {
    // If the new value is the same as the old one, skip it
    if (!this.value.equals(value))
    {
      this.validate(value, this.getMdAttribute());
      this.value = value;

      this.setModified(true);
    }
  }

  /**
   * Most, but not all, attributes are represented as strings.  For some, they
   * are represented as objects. Object will be cast into the appropriate type.
   *  Precondition object is of expected type.
   */
  public void setValue(Object object)
  {
    this.setValue((String)object);
  }

  /**
   * Checks user permissions for the value, and passes it to <code>setValue</code>.
   *
   * <br>
   * <b>Precondition: </b> value != null <br>
   * <b>Postcondition: </b> this.value = value this.value.equals(value)
   * this.validate(this.value) = <b>true </b>
   *
   * <br>
   * <b>modifies: </b> isModified() returns true
   *
   * @param value new value of the attribute
   *
   * @throws AttributeException if the value is invalid for this Attribute
   */
  public void setValueAndValidate(String value)
  {
    this.validateSystem();
    this.setValue(value);
  }

  /**
   * Runs validation tests that are common to all Attribute classes, minus the required attribute test.
   *
   * @param valueToValidate the String value to be validated
   * @param mdAttribute the defining Metadata object of the class that contains this
   *          Attribute
   * @return true if the value is valid for all common tests
   */
  protected void validate(String valueToValidate, MdAttributeDAOIF mdAttribute)
  {
    this.validateMutable(mdAttribute);
  }

  /**
   * Checks if this attribute is required for its defining TransientDAO.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   *
   * @param valueToValidate The value to be checked if its requried in the the defining
   *          TransientDAO
   * @param mdAttributeIF The Metatdata TransientDAO that defines the Attribute
   *
   * @throws EmptyValueProblem if this attribute is required for its defining BusinessDAO
   *           but contains an empty value.
   */
  public void validateRequired(String valueToValidate, MdAttributeDAOIF mdAttributeIF)
  {
    if (mdAttributeIF.isDimensionRequired() && valueToValidate.trim().equals(""))
    {
      MdDimensionDAOIF mdDimensionDAOIF = Session.getCurrentDimension();

      String error = "Attribute [" + getName() + "] on type [" + getDefiningClassType()
      + "] requires a value for dimension ["+mdDimensionDAOIF.getName()+"]";
      EmptyValueProblem problem =
        new EmptyValueProblem(this.getContainingComponent().getProblemNotificationId(), mdAttributeIF.definedByClass(), mdAttributeIF, error, this);
      problem.throwIt();
    }

    //  make sure a value is provided if a value is required
    if ( mdAttributeIF.isRequired() && valueToValidate.trim().equals(""))
    {
      String error = "Attribute [" + getName() + "] on type [" + getDefiningClassType()
          + "] requires a value";
      EmptyValueProblem problem =
        new EmptyValueProblem(this.getContainingComponent().getProblemNotificationId(), mdAttributeIF.definedByClass(), mdAttributeIF, error, this);
      problem.throwIt();
    }
  }

  /**
   * Runs validation tests that are common to all Attribute classes, including the required attribute test.
   *
   * @param valueToValidate the String value to be validated
   * @return true if the value is valid for all common tests
   * @throws AttributeException
   *    if the attribute is not valid.
   */
  public void validateEverything(String valueToValidate)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttribute();
    this.validateRequired(valueToValidate, mdAttributeIF);
    this.validate(valueToValidate, mdAttributeIF);
  }

  /**
   * This is used to validate the default value of the given {@link# MdAttributeIF}.
   * Runs validation tests that are common to all Attribute classes, but not the required attribute test.
   *
   * @param mdAttributeIF
   * @param valueToValidate the String value to be validated
   * @return true if the value is valid for all common tests
   * @throws AttributeException
   *    if the attribute is not valid.
   */
  public void validateDefaultValue(MdAttributeDAOIF mdAttributeIF, String valueToValidate)
  {
    this.validate(valueToValidate, mdAttributeIF);
  }

  /**
   * Checks if this attribute is required for its defining BusinessDAO.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   *
   * @param valueToValidate The value to be checked if its requried in the the defining
   *          BusinessDAO.
   *
   * @throws ImmutableAttributeProblem if this attribute is required for its defining BusinessDAO
   *           but contains an empty value.
   */
  protected void validateSystem()
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttribute();
    //  make sure a value is provided if a value is required
    if ( mdAttributeIF.isSystem())
    {
      String error = "Attribute [" + getName() + "] on type [" + getDefiningClassType()
          + "] is a system attribute and cannot be modified.";
      SystemAttributeProblem problem =
        new SystemAttributeProblem(this.getContainingComponent().getProblemNotificationId(), mdAttributeIF.definedByClass(), mdAttributeIF, error, this);
      problem.throwIt();
    }
  }

  /**
   * Checks if this attribute is immutable.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   *
   * @param mdAttribute The Metatdata BusinessDAO that defines the Attribute
   *
   * @throws ImmutableAttributeProblem if this attribute is immutable, and the containing
   *           component is not new
   */
  protected void validateMutable(MdAttributeDAOIF mdAttribute)
  {
    if ( !mdAttribute.isImmutable() || this.containingTransientDAO.isNew() || ServerProperties.getAllowModificationOfMdAttribute())
    {
      return;
    }
    else
    {
      String error = "Cannot change the immutable attribute [" + getName() + "] on type ["
          + getDefiningClassType() + "]";
      ImmutableAttributeProblem problem =
        new ImmutableAttributeProblem(this.getContainingComponent().getProblemNotificationId(), mdAttribute.definedByClass(), mdAttribute, error, this);
      problem.throwIt();
    }
  }


  /**
   * Returns the MdAttributeDAOIF that defines the this attribute.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   *
   * @return MdAttributeDAOIF that defines the this attribute
   */
  public MdAttributeDAOIF getMdAttribute()
  {
    return MdAttributeDAO.getByKey(this.mdAttributeKey);
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute.  If this is defined
   * by a concrete attribute, this object is returned.  If it is a virtual attribute, then the
   * concrete attribute it references is returned.
   *
   * @return MdAttributeDAOIF that defines the this attribute
   */
  public MdAttributeConcreteDAOIF getMdAttributeConcrete()
  {
    return this.getMdAttribute().getMdAttributeConcrete();
  }

  /**
   * Returns all MdAttributes that are involved in building the select clause.
   * @return all MdAttributes that are involved in building the select clause.
   */
  public Set<MdAttributeConcreteDAOIF> getAllEntityMdAttributes()
  {
    Set<MdAttributeConcreteDAOIF> set = new HashSet<MdAttributeConcreteDAOIF>();
    set.add(this.getMdAttributeConcrete());
    return set;
  }

  @Override
  public String toString()
  {
    return getName() + "=" + getValue();
  }

}
