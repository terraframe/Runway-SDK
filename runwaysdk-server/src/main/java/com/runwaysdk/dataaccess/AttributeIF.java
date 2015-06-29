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
/*
 * Created on Aug 8, 2005
 *
 */
package com.runwaysdk.dataaccess;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.ComponentIF;


/**
 * @author nathan
 *
 * TODO To change the template for this generated comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface AttributeIF extends Serializable
{
  /**
   * Returns the Component that contains this Attribute.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> return value != null
   *
   * @return the Component that contains this Attribute
   */
  public ComponentIF getContainingComponent();

  /**
   * Returns the name of the class that defines this attribute.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> return value != null
   *
   * @return name of the attribute
   */
  public String getDefiningClassType();

  /**
   * Returns the MdAttributeIF that defines the this attribute.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   *
   * @return MdAttributeIF that defines the this attribute
   */
  public MdAttributeDAOIF getMdAttribute();

  /**
   * Returns all MdAttributes that are involved in building the select clause.
   * @return all MdAttributes that are involved in building the select clause.
   */
  public Set<MdAttributeConcreteDAOIF> getAllEntityMdAttributes();

  /**
   * Returns the MdAttributeIF that defines the this attribute.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   *
   * @return MdAttributeIF that defines the this attribute
   */
  public MdAttributeDAOIF getMdAttributeConcrete();

  /**
   * Returns the name of the attribute.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> return value != null
   *
   * @return name of the attribute
   */
  public String getName();

  /**
   * A convenience method that gets the MdAttribute and returns the display
   * label. Note that the display label in independent of the value of the
   * attribute - all instances of the attribute have the same display label.
   *
   * @return The Display Label for the attribute, as defined in the Metadata
   */
  public String getDisplayLabel(Locale locale);

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getDisplayLabes();

  /**
   * Returns the value of the attribute.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> return value != null
   *
   * @return value of the attribute
   */
  public String getValue();

  /**
   * Some attributes store objects instead of strings.
   * @param name
   * @return object stored on the attribute.
   */
  public Object getObjectValue();

  /**
   * Returns the value of the attribute as stored in the database.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> return value != null
   *
   * @return value of the attribute
   */
  public String getRawValue();

  /**
   * Returns true if the value of the attribute has been modified since the attribute was
   * last commited, false otherwise.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   *
   * @return true if the value of the attribute has been modified since the attribute was
   *   last commited, false otherwise.
   */
  public boolean isModified();
}
