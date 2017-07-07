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
package com.runwaysdk.dataaccess.attributes.value;

import java.util.Locale;
import java.util.Map;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.transport.metadata.AttributeBooleanMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeBoolean_Q extends MdAttributePrimitive_Q implements MdAttributeBooleanDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = 952303035986154985L;


  /**
   * Used in value objects with attributes that contain values that are the result of functions, where the function result
   * data type does not match the datatype of the column.
   * @param mdAttributeIF metadata that defines the column.
   */
  public MdAttributeBoolean_Q(MdAttributeConcreteDAOIF mdAttributeIF)
  {
    super(mdAttributeIF);
  }

  @Override
  public String attributeMdDTOType()
  {
    return AttributeBooleanMdDTO.class.getName();
  }

  /**
   *
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getType()
   */
  public String getType()
  {
    return MdAttributeBooleanInfo.CLASS;
  }
  
  
  /**
   * Returns the java class object for the attribute type.
   * 
   * @return the java class object for the attribute type.
   */
  public Class<?> javaClass()
  {
    return Boolean.class;
  }

  /**
   * Returns the concrete attribute representing this attribute.  This is a concrete attribute so itself
   * is returned.
   *
   * @return concrete attribute representing this attribute.
   */
  public MdAttributeBooleanDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeBooleanDAOIF)super.getMdAttributeConcrete();
  }

  /**
   * Returns the positive display label of this metadata object
   *
   * @param locale
   *
   * @return the positive display label of this metadata object
   */
  public String getPositiveDisplayLabel(Locale locale)
  {
    return ((MdAttributeBooleanDAOIF)this.mdAttributeConcreteIF).getPositiveDisplayLabel(locale);
  }

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getPositiveDisplayLabels()
  {
    return ((MdAttributeBooleanDAOIF)this.mdAttributeConcreteIF).getPositiveDisplayLabels();
  }

  /**
   * Returns the negative display label of this metadata object
   *
   * @param locale
   *
   * @return the negative display label of this metadata object
   */
  public String getNegativeDisplayLabel(Locale locale)
  {
    return ((MdAttributeBooleanDAOIF)this.mdAttributeConcreteIF).getNegativeDisplayLabel(locale);
  }

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getNegativeDisplayLabels()
  {
    return ((MdAttributeBooleanDAOIF)this.mdAttributeConcreteIF).getNegativeDisplayLabels();
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeBooleanDAO getBusinessDAO()
  {
    this.unsupportedBusinessDAO();
    return null;
  }
  
  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession() 
  {
    throw new UnsupportedOperationException();
  }
}
