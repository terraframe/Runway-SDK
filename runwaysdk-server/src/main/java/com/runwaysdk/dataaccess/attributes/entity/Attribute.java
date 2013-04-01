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
/*
 * Created on Jun 9, 2005
 */
package com.runwaysdk.dataaccess.attributes.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeException;
import com.runwaysdk.dataaccess.attributes.EmptyValueProblem;
import com.runwaysdk.dataaccess.attributes.ImmutableAttributeProblem;
import com.runwaysdk.dataaccess.attributes.SystemAttributeProblem;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.query.EntityQuery;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;

/**
 * Root Attribute class. All attributes have the following properties in common.
 * 
 * @author Eric
 * @version $Revision: 1.24 $
 * @since 1.4
 */
public abstract class Attribute implements AttributeIF, Serializable
{
  // Built in attribute classes

  /**
   * 
   */
  private static final long serialVersionUID = -3279358753303773329L;

  /**
   * Cannonical name of the attribute. <br>
   * <b>invariant </b> name != null <br>
   * <b>invariant </b> !name.trim().equals("")
   */
  final private String name;

  /**
   * The Component that contains this Attribute. Not defined on creation, but
   * set by the containing Component. <br>
   * <b>invariant </b> containingComponent != null
   */
  private EntityDAO    containingEntity;

  /**
   * Quantified name of the class that defines this attribute. <br>
   * <b>invariant </b> definingClassType != null <br>
   * <b>invariant </b> !definingClassType.equals("")
   */
  final private String definingClassType;

  /**
   * Value of the attribute. <br>
   * <b>invariant </b> value != null
   */
  protected String     value;

  /**
   * Indicates if the value of this Attribute has been modified since it was
   * last applied to the database.
   */
  private boolean      isModified = false;

  protected String     mdAttributeKey;

  private boolean      isImport;

  /**
   * Creates an attribute with the given name and initializes the value to
   * blank.
   * 
   * <br>
   * <b>Precondition: </b> name != null <br>
   * <b>Precondition: </b> !name.trim().equals("") <br>
   * <b>Precondition: </b> definingClassType != null <br>
   * <b>Precondition: </b> !definingClassType().equals("") <br>
   * <b>Precondition: </b> definingClassType is the name of a class that defines
   * an attribute with this name
   * 
   * @param name
   *          name of the attribute
   * @param mdAttributeKey
   *          key of the defining attribute metadata
   * @param definingClassType
   *          name of the class that defines this attribute
   */
  protected Attribute(String name, String mdAttributeKey, String definingClassType)
  {
    this.name = name;
    this.isImport = false;
    this.mdAttributeKey = mdAttributeKey;
    this.definingClassType = definingClassType;
    this.value = "";
    this.containingEntity = null;
  }

  /**
   * Creates an attribute with the given name and initializes the value to the
   * given value.
   * 
   * <br>
   * <b>Precondition: </b> name != null <br>
   * <b>Precondition: </b> !name.trim().equals("") <br>
   * <b>Precondition: </b> definingClassType != null <br>
   * <b>Precondition: </b> !definingClassType.trim().equals("") <br>
   * <b>Precondition: </b> definingClassType represents a class that defines an
   * attribute with this name <br>
   * <b>Precondition: </b> value != null
   * 
   * @param name
   *          name of the attribute
   * @param definingClassType
   *          name of the class that defines this attribute
   * @param value
   *          initial value of the attribute
   */
  protected Attribute(String name, String mdAttributeKey, String definingClassType, String value)
  {
    this(name, mdAttributeKey, definingClassType);
    this.value = value;
  }

  /**
   * Returns true if the value of the attribute has been modified since the
   * attribute was last commited to the database, false otherwise.
   * 
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   * 
   * @return true if the attribute has been modified since it was last commited
   *         to the database, false otherwise.
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
   * @param isModified
   *          true if the attribute has been modified false otherwise.
   */
  public void setModified(boolean isModified)
  {
    this.isModified = isModified;
  }

  /**
   * Sets the state of the attribute object after a transaction has been
   * committed/
   * 
   */
  public void setCommitState()
  {
    this.setModified(false);
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
   *         String value.
   */
  public Map<String, String> getDisplayLabes()
  {
    return this.getMdAttribute().getDisplayLabels();
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
  public EntityDAO getContainingComponent()
  {
    if (containingEntity == null)
    {
      String error = "Attribute [" + name + "]'s Contianing Component is null. It is the component's " + "responsiblity to call setContainingComponent() for all of its attributes.";
      throw new DataNotFoundException(error, MdTypeDAO.getMdTypeDAO(EntityInfo.CLASS));
    }
    return containingEntity;
  }

  /**
   * Sets a reference to the object that contains this attribute
   * 
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> this.getContainingComponent() == containingEntity
   * 
   * @param container
   */
  public void setContainingComponent(EntityDAO containingEntity)
  {
    this.containingEntity = containingEntity;
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
    return this.definingClassType;
  }

  /**
   * Returns the formatted value of the attribute. Some attributes format this
   * value to something other than what is stored in the database.
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
   * 
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
   * Returns the value of the attribute as stored in the database. Not all
   * objects are represented as Strings in the <code>Attribute</code> hierarchy.
   * 
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> return value != null
   * 
   * @return value of the attribute
   */
  public Object getRawValueObject()
  {
    return this.getRawValue();
  }

  /**
   * Returns the JDBC prepared statement variable used for this type. Most just
   * need to return the "?" character, but others require that special methods
   * or variables are used in addition.
   * 
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> return value != null
   * 
   * @return value of the attribute
   */
  public String getPreparedStatementVar()
  {
    return "?";
  }

  /**
   * The import equivalent of setValue. Used only during importing from an xml
   * file. By default has the same behavior as setValue. However, special logic
   * may be added for children classes.
   * 
   * @param value
   *          The value to set the attribute
   */
  public void importValue(String value)
  {
    this.setValue(value);
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
   * different from the old value
   * 
   * @param value
   *          new value of the attribute
   * 
   * @throws AttributeException
   *           if the value is invalid for this Attribute
   */
  public void setValue(String value)
  {
    // If the new value is the same as the old one, skip it
    if (this.valueIsDifferent(value))
    {
      this.validate(value, this.getMdAttribute());
      this.value = value;
      this.isModified = true;
    }
  }

  /**
   * Sets the value but does absolutely no validation. Use with caution!!!
   * 
   * @param value
   */
  public void setValueNoValidation(String value)
  {
    if (this.valueIsDifferent(value))
    {
      this.value = value;
      this.isModified = true;
    }
  }

  /**
   * Most, but not all, attributes are represented as strings. For some, they
   * are represented as objects. Object will be cast into the appropriate type.
   * Precondition object is of expected type.
   */
  public void setValue(Object object)
  {
    this.setValue((String) object);
  }

  /**
   * Returns true if the given value is different than the value of this
   * attribute.
   * 
   * @return true if the given value is different than the value of this
   *         attribute.
   */
  protected boolean valueIsDifferent(String value)
  {
    return !this.value.equals(value);
  }

  /**
   * Checks user permissions for the value, and passes it to
   * <code>setValue</code>.
   * 
   * <br>
   * <b>Precondition: </b> value != null <br>
   * <b>Postcondition: </b> this.value = value this.value.equals(value)
   * this.validate(this.value) = <b>true </b>
   * 
   * <br>
   * <b>modifies: </b> isModified() returns true
   * 
   * @param value
   *          new value of the attribute
   * 
   * @throws AttributeException
   *           if the value is invalid for this Attribute
   */
  public void setValueAndValidate(String value)
  {
    // If the new value is the same as the old one, skip it
    if (this.valueIsDifferent(value))
    {
      this.validateSystem();
    }

    this.setValue(value);
  }

  /**
   * Runs validation tests that are common to all Attribute classes, minus the
   * required attribute test.
   * 
   * @param valueToValidate
   *          the String value to be validated
   * @param mdAttribute
   *          the defining Metadata object of the class that contains this
   *          Attribute
   * @return true if the value is valid for all common tests
   */
  protected void validate(String valueToValidate, MdAttributeDAOIF mdAttribute)
  {
    this.validateMutable(mdAttribute);
  }

  /**
   * Runs validation tests that are common to all Attribute classes, including
   * the required attribute test.
   * 
   * @param valueToValidate
   *          the String value to be validated
   * @return true if the value is valid for all common tests
   * @throws AttributeException
   *           if the attribute is not valid.
   */
  public void validateEverything(String valueToValidate)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttribute();
    this.validateRequired(valueToValidate, mdAttributeIF);
    this.validate(valueToValidate, mdAttributeIF);
  }

  /**
   * This is used to validate the default value of the given {@link#
   * MdAttributeDAOIF}. Runs validation tests that are common to all Attribute
   * classes, but not the required attribute test.
   * 
   * @param mdAttributeDAOIF
   * @param valueToValidate
   *          the String value to be validated
   * @return true if the value is valid for all common tests
   * @throws AttributeException
   *           if the attribute is not valid.
   */
  public void validate(MdAttributeDAOIF mdAttributeDAOIF, String valueToValidate)
  {
    this.validate(valueToValidate, mdAttributeDAOIF);
  }

  /**
   * Checks if this attribute is immutable.
   * 
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   * 
   * @param mdAttribute
   *          The Metatdata BusinessDAO that defines the Attribute
   * 
   * @throws ImmutableAttributeProblem
   *           if this attribute is immutable, and the containing component is
   *           not new
   */
  protected void validateMutable(MdAttributeDAOIF mdAttribute)
  {
    if (!mdAttribute.isImmutable() || this.containingEntity.isNew())
    {
      return;
    }
    else
    {
      String error = "Cannot change the immutable attribute [" + getName() + "] on type [" + getDefiningClassType() + "]";
      ImmutableAttributeProblem problem = new ImmutableAttributeProblem(this.getContainingComponent().getProblemNotificationId(), mdAttribute.definedByClass(), mdAttribute, error, this);
      problem.throwIt();
    }
  }

  /**
   * Checks if this attribute is required for its defining BusinessDAO.
   * 
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   * 
   * @param valueToValidate
   *          The value to be checked if its required in the the defining
   *          BusinessDAO
   * @param mdAttributeIF
   *          The Metatdata BusinessDAO that defines the Attribute
   * 
   * @throws EmptyValueProblem
   *           if this attribute is required for its defining BusinessDAO but
   *           contains an empty value.
   */
  public void validateRequired(String valueToValidate, MdAttributeConcreteDAOIF mdAttributeIF)
  {
    if (mdAttributeIF.isDimensionRequired() && valueToValidate.trim().equals(""))
    {
      MdDimensionDAOIF mdDimensionDAOIF = Session.getCurrentDimension();

      String error = "Attribute [" + getName() + "] on type [" + getDefiningClassType() + "] requires a value for dimension [" + mdDimensionDAOIF.getName() + "]";
      EmptyValueProblem problem = new EmptyValueProblem(this.getContainingComponent().getProblemNotificationId(), mdAttributeIF.definedByClass(), mdAttributeIF, error, this);
      problem.throwIt();
    }

    // make sure a value is provided if a value is required
    if (mdAttributeIF.isRequired() && valueToValidate.trim().equals(""))
    {
      String error = "Attribute [" + getName() + "] on type [" + getDefiningClassType() + "] requires a value";
      EmptyValueProblem problem = new EmptyValueProblem(this.getContainingComponent().getProblemNotificationId(), mdAttributeIF.definedByClass(), mdAttributeIF, error, this);
      problem.throwIt();
    }
  }

  /**
   * Checks if this attribute is required for its defining BusinessDAO.
   * 
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   * 
   * @param valueToValidate
   *          The value to be checked if its required in the the defining
   *          BusinessDAO.
   * 
   * @throws ImmutableAttributeProblem
   *           if this attribute is required for its defining BusinessDAO but
   *           contains an empty value.
   */
  protected void validateSystem()
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttribute();
    // make sure a value is provided if a value is required
    if (mdAttributeIF.isSystem())
    {
      String error = "Attribute [" + getName() + "] on type [" + getDefiningClassType() + "] is a system attribute and cannot be modified.";
      SystemAttributeProblem problem = new SystemAttributeProblem(this.getContainingComponent().getProblemNotificationId(), mdAttributeIF.definedByClass(), mdAttributeIF, error, this);
      problem.throwIt();
    }
  }

  /**
   * Checks if another BusinessDAO of the same class has the same value for the
   * same attribute.
   * 
   * <br>
   * <b>Precondition: </b> unique.equalsIgnoreCase("Y") <br>
   * <b>Postcondition: </b> true
   * 
   * @param valueToValidate
   *          The value that will be check for uniqueness
   * @param mdAttribute
   *          The Metatdata BusinessDAO that defines the Attribute.
   * 
   * @throws DuplicateDataException
   *           If a BusinessDAO already exists that has the same value for the
   *           given attribute.
   */
  public void validateUnique(String valueToValidate, MdAttributeConcreteDAOIF mdAttribute)
  {
    if (mdAttribute.isUnique())
    {
      // get metadata for the class that defines this attribute
      QueryFactory qFactory = new QueryFactory();

      EntityQuery entityQ = qFactory.entityQuery(this.containingEntity);
      entityQ.WHERE(entityQ.get(this.getName()).EQ(valueToValidate));

      // This cast is type safe because only an EntityDAO will be changing
      // values of attributes.
      // Hence, only an EntityDAO would call this method.
      if ( ( (EntityDAO) getContainingComponent() ).isAppliedToDB())
      {
        entityQ.WHERE(entityQ.aCharacter(EntityInfo.ID).NE(getContainingComponent().getId()));
      }

      if (entityQ.getCount() > 0)
      {
        MdEntityDAOIF mdEntityIF = (MdEntityDAOIF) mdAttribute.definedByClass();
        String error = "Attribute [" + mdAttribute.definesAttribute() + "]" + " must be unique.  An instance of [" + mdEntityIF.definesType() + "] already exists with the value of [" + valueToValidate + "].";

        List<AttributeIF> attributeList = new LinkedList<AttributeIF>();
        attributeList.add(this);

        List<String> valueList = new LinkedList<String>();
        valueList.add(valueToValidate);

        throw new DuplicateDataException(error, mdEntityIF, attributeList, valueList);
      }

    }
  }

  /**
   * Returns the BusinessDAO that defines the this attribute.
   * 
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   * 
   * @return BusinessDAO that defines the this attribute
   */
  public MdAttributeConcreteDAOIF getMdAttribute()
  {
    return MdAttributeConcreteDAO.getByKey(this.mdAttributeKey);
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by aa concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return MdAttributeConcreteDAOIF that defines the this attribute
   */
  public MdAttributeConcreteDAOIF getMdAttributeConcrete()
  {
    return this.getMdAttribute();
  }

  /**
   * Returns all MdAttributes that are involved in building the select clause.
   * 
   * @return all MdAttributes that are involved in building the select clause.
   */
  public Set<MdAttributeConcreteDAOIF> getAllEntityMdAttributes()
  {
    Set<MdAttributeConcreteDAOIF> set = new HashSet<MdAttributeConcreteDAOIF>();
    set.add(this.getMdAttribute());
    return set;
  }

  /**
   * Cleans up any object that references this attribute in some way.
   * <b>Precondition: </b> this attribute MUST be a member of the given
   * EntityDAO <br>
   * 
   * @param entityDAO
   */
  public void removeReferences(EntityDAO entityDAO, boolean businessContext)
  {
  }

  /**
   * Initializes any references to this attribute. Many attribute classes have
   * nothing to initialize.
   * 
   * <b>Precondition: </b> this MdAttribute MUST be the metadata of this
   * attribute. <br>
   */
  public void initReferences(MdAttributeConcreteDAOIF mdAttribute)
  {
  }

  /**
   * Updates any references to this attribute. Many attribute classes aren't
   * referenced by anything.
   * 
   * <b>Precondition: </b> this MdAttribute MUST be the metadata of this
   * attribute. <br>
   */
  public void updateReferences(MdAttributeConcreteDAOIF mdAttribute)
  {
  }

  /**
   * Returns a deep clone of this attribute.
   * 
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   * 
   * @return a deep clone of this Attribute
   */
  public abstract Attribute attributeClone();

  /**
  *
  */
  public Attribute attributeCopy()
  {
    return this.attributeClone();
  }

  @Override
  public String toString()
  {
    return getName() + "=" + getValue();
  }

  /**
   * Sets appliedToDB to false if the object is new, as the database will
   * rollback any newly inserted records.
   * 
   */
  public void rollbackState()
  {
    // Balk: In general this method does not need to do anything.
    // However, it behavior is overwritten in some of its children
    // classes.
  }

  /**
   * Call to rollback to a savepoint.
   * 
   */
  public void rollbackState(Integer rollbackSavepointId)
  {
    // Balk: In general this method does not need to do anything.
    // However, it behavior is overwritten in some of its children
    // classes.
  }

  public void setImport(boolean isImport)
  {
    this.isImport = isImport;
  }

  public boolean isImport()
  {
    return isImport;
  }
}
