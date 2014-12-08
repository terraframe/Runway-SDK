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
package com.runwaysdk.dataaccess;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.business.Business;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAOVisitor;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public interface MdAttributeDAOIF extends MetadataDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE = "md_attribute";

  /**
   * Returns the column name of attribute.
   * 
   * @return
   */
  public String getColumnName();
  
  /**
   * Builds a new AttributeMdSession containing all the same data for this class.
   * This is used for client-side metadata caching.
   */
  public AttributeMdSession getAttributeMdSession();
  
  /**
   * Returns the concrete attribute representing this attribute. If this is a
   * concrete attribute, this object is returned. If it is a virtual attribute,
   * then the concrete attribute it references is returned.
   *
   * @return concrete attribute representing this attribute.
   */
  public MdAttributeConcreteDAOIF getMdAttributeConcrete();

  /**
   * Returns a string representing the query attribute class for attributes of
   * this type.
   *
   * @return string representing the query attribute class for attributes of
   *         this type.
   */
  public String queryAttributeClass();

  /**
   * Returns the display label of this metadata object. If this metadata has no
   * display label, it returns the one from the
   * <code>MdAttributeConcreteIF</code>.
   *
   * @return the display label of this metadata object.
   */
  public String getDisplayLabel(Locale locale);

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *         String value.
   */
  public Map<String, String> getDisplayLabels();

  /**
   * Returns the MdClassIF that defines this MdAttribute.
   *
   * @return the MdClassIF that defines this MdAttribute.
   */
  public MdClassDAOIF definedByClass();

  /**
   * Returns the name of the attribute that this attribute defines.
   *
   * @return the name of the attribute that this attribute defines.
   */
  public String definesAttribute();

  /**
   * Returns the default value for the attribute that this metadata defines. If
   * no default value has been defined, an empty string is returned.
   *
   * @return the default value for the attribute that this metadata defines.
   */
  public String getDefaultValue();

  /**
   * If a default value has been defined for a dimension attached to this session, then that
   * value is returned, otherwise the default value assigned to the attribute definition is returned.
   *
   * @return default value
   */
  public String getAttributeInstanceDefaultValue();

  /**
   * Returns true if the attribute is immutable, false otherwise. Immutable
   * attributes cannot have their value changed after they receive an initial
   * value.
   *
   * @return true if the attribute is immutable, false otherwise.
   */
  public boolean isImmutable();

  /**
   * Returns true if the attribute is a system attribute, false otherwise.
   * System attributes can only be modified by the core.
   *
   * @return true if the attribute is a system attribute, false otherwise.
   */
  public boolean isSystem();

  /**
   * Returns true if instances of this attribute require a value, false
   * otherwise.
   *
   * @return true if instances of this attribute require a value, false
   *         otherwise.
   */
  public boolean isRequired();

  /**
   * Returns the visibility modifier of the getter.
   *
   * @return the visibility modifier of the getter.
   */
  public VisibilityModifier getGetterVisibility();

  /**
   * Returns the visibility modifier of the setter.
   *
   * @return the visibility modifier of the setter.
   */
  public VisibilityModifier getSetterVisibility();

  /**
   * Called for java class generation. Returns the java type of this attribute,
   * which is used in the generated classes for type safety.
   *
   * @param isDTO
   *          indicates if the generation is for a DTO-layer object
   * @return The java type of this attribute
   */
  public String javaType(boolean isDTO);

  /**
   * Called for java class DTO generation. Returns the type of AttributeMd (DTO
   * layer) this MdAttribute requires to represent its metadata on a DTO.
   *
   * @return the class name of the AttributeMd type needed.
   */
  public String attributeMdDTOType();

  /**
   * Called for java class generation, this method returns a String representing
   * the java code for the server object that fetches the desired attribute
   * (through getValue methods inherited from {@link Business}), and converts it
   * from String into the appropriate type.
   *
   * @return java code for typesafe getValue
   */
  public String generatedServerGetter();

  /**
   * Called for java class generation, this method returns a String repesenting
   * the java code for the client object that fetches the desired attribute
   * (through getValue methods inherited from {@link Business}), and converts it
   * from String into the appropriate type.
   *
   * @return java code for typesafe getValue
   */
  public String generatedClientGetter();

  /**
   * Called for java class generation, this method returns a String repesenting
   * the java code for the object on the server that sets the desired attribute
   * (through setValue methods inherited from {@link Business}) by converting a
   * typsafe parameter into a String.
   *
   * @return java code for typesafe setValue
   */
  public String generatedServerSetter();

  /**
   * Called for java class generation, this method returns a String repesenting
   * the java code for the object on the server that sets the desired attribute
   * (through setValue methods inherited from {@link Business}) by converting a
   * typsafe parameter into a String.
   *
   * @return java code for typesafe setValue
   */
  public String generatedClientSetter();

  /**
   * Takes the provided value string, and wraps it in the java code to call
   * {@link Business#setValue(String, String)}. The value String is actually a
   * String representing java code that can be inserted into generated classes,
   * converting typesafe input into the required String type.
   *
   * The standard usage model is that concrete MdAttribute classes will generate
   * the code to converate their typesafe input into a String, then pass that
   * code to this method, which generates the call the generic setter.
   *
   * @param value
   *          Code that converts typesafe input into a String
   * @return Code to set this attribute
   */
  public String setterWrapper(String value);

  public boolean getGenerateAccessor();

  /**
   * Returns all attribute dimensions for this attribute.
   *
   * @return all attribute dimensions for this attribute.
   */
  public List<MdAttributeDimensionDAOIF> getMdAttributeDimensions();

  /**
   * Returns the attribute dimension for the given dimension.
   *
   * @param mdDimension dimension
   *
   * @return attribute dimension for the given dimension.
   */
  public MdAttributeDimensionDAOIF getMdAttributeDimension(MdDimensionDAOIF mdDimension);

  /**
   * Returns true if the current session is associated with a dimension and
   * that dimension requires this attribute.
   *
   * @return true if the current session is associated with a dimension and
   * that dimension requires this attribute.
   */
  public boolean isDimensionRequired();

  /**
   * Returns true if the metadata for this attribute on a DTO needs to be marked as required,
   * false otherwise.
   *
   * @return true if the metadata for this attribute on a DTO needs to be marked as required,
   * false otherwise.
   */
  public boolean isRequiredForDTO();

  /**
   * Accepts the visitor and dispatches to the correct visit method
   *
   * @param visitor
   */
  public void accept(MdAttributeDAOVisitor visitor);
  
  /**
   * Returns the full name of the Java interface class that the concrete class directly implements. For example:
   * <code>MdAttributeCharacterDAO</code> directly implements <code>MdAttributeCharacterDAOIF</code>.
   * 
   * @return the full name of the Java interface class that the concrete class directly implements. For example:
   * <code>MdAttributeCharacterDAO</code> directly implements <code>MdAttributeCharacterDAOIF</code>.
   */
  public String getInterfaceClassName(); 
 
}
