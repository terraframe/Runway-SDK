/**
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
 */
package com.runwaysdk.dataaccess.attributes.value;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.ValueObject;

public abstract class Attribute implements AttributeIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 5491683249815645501L;

  protected String name;

  protected String value;

  protected MdAttributeConcreteDAOIF mdAttributeIF;

  // Reference to all MdAttributes that were involved in constructing this attribute;
  protected Set<MdAttributeConcreteDAOIF> entityMdAttributeIFset;

  protected String definingEntityType;

  protected ValueObject valueObject;

  /**
   * Creates an attribute with the given name.
   *
   * <br>
   * <b>Precondition: </b> name != null <br>
   * <b>Precondition: </b> !name.trim().equals("") <br>
   * <b>Precondition: </b> value != null <br>
   * <b>Precondition: </b> definingEntityType != null <br>
   * <b>Precondition: </b> !definingEntityType().equals("") <br>
   * <b>Precondition: </b> definingEntityType is the name of a class that defines an attribute with
   * this name
   *
   * @param name name of the attribute
   * @param the value of the attribute
   * @param definingEntityType name of the class that defines this attribute from which the value came
   * @param mdAttributeIF metadata that defines the attribute from which the value came.
   * @param entityMdAttributeIFset all MdAttributes that were involved in constructing this attribute.
   */
  protected Attribute(String name, String value, String definingEntityType, MdAttributeConcreteDAOIF mdAttributeIF, Set<MdAttributeConcreteDAOIF> entityMdAttributeIFset)
  {
    this.name = name;
    this.definingEntityType = definingEntityType;
    this.value = value;
    this.valueObject =null;
    this.mdAttributeIF = mdAttributeIF;
    this.entityMdAttributeIFset = entityMdAttributeIFset;
  }

  /**
   * Returns false, since value attributes cannot be modified.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   *
   * @return false, since value attributes cannot be modified.
   */
  public boolean isModified()
  {
    return false;
  }

  /**
   * Sets a reference to the object that contains this attribute
   *
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> this.getContainingComponent() == container
   *
   * @param container
   */
  public void setContainingComponent(ValueObject valueObject)
  {
    this.valueObject = valueObject;
  }

  /**
   * Returns the ValueObject object that contains this attribute.
   *
   * @return ValueObject object that contains this attribute.
   */
  public ValueObject getContainingComponent()
  {
    return this.valueObject;
  }

  /**
   * Returns the type of the class that defines the attribute from which this value came.
   *
   * @return type of the class that defines the attribute from which this value came.
   */
  public String getDefiningClassType()
  {
    return this.definingEntityType;
  }

  /**
   * Returns the display label of the attribute.
   *
   * @return display label of the attribute.
   */
  public String getDisplayLabel(Locale locale)
  {
    return this.mdAttributeIF.getDisplayLabel(locale);
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
    return this.mdAttributeIF.getDisplayLabels();
  }

  /**
   * Returns the MdAttribute that defines the attribute from which the value came.
   *
   * @return MdAttribute that defines the attribute from which the value came.
   */
  public MdAttributeConcreteDAOIF getMdAttribute()
  {
    return this.mdAttributeIF;
  }
  /**
   * Returns the concrete attribute metadata that defines this attribute.  If this is defined
   * by aa concrete attribute, this object is returned.  If it is a virtual attribute, then the
   * concrete attribute it references is returned.
   *
   * @return MdAttributeReferenceDAOIF that defines the this attribute
   */
  public MdAttributeConcreteDAOIF getMdAttributeConcrete()
  {
    return this.getMdAttribute();
  }

  /**
   * Returns all MdAttributes that are involved in building the select clause.
   * @return all MdAttributes that are involved in building the select clause.
   */
  public Set<MdAttributeConcreteDAOIF> getAllEntityMdAttributes()
  {
    return entityMdAttributeIFset;
  }

  /**
   * Return the name of the attribute.
   *
   * @return name of the attribute.
   */
  public String getName()
  {
    return this.name;
  }

  public String getRawValue()
  {
    return this.value;
  }

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

  public void validateEverything(String valueToValidate) {}

  /**
   * Builds a <code>com.runwaysdk.dataaccess.attributes.tranzient.Attribute</code> for a <code>TransientDAO</code> object.
   * @return <code>com.runwaysdk.dataaccess.attributes.tranzient.Attribute</code> for a <code>TransientDAO</code> object.
   */
  public com.runwaysdk.dataaccess.attributes.tranzient.Attribute buildTransientAttribute(String transientType)
  {
    com.runwaysdk.dataaccess.attributes.tranzient.Attribute transientAttribute =
      com.runwaysdk.dataaccess.attributes.tranzient.AttributeFactory.createAttribute(this.mdAttributeIF.getType(), this.mdAttributeIF.getKey(), this.getName(),
          transientType, this.getObjectValue());

    return transientAttribute;
  }

}
