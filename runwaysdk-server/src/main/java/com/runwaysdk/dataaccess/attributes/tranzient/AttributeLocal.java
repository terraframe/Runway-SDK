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
package com.runwaysdk.dataaccess.attributes.tranzient;

import java.util.Locale;
import java.util.Map;

import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalDAO;

public abstract class AttributeLocal extends AttributeStruct implements AttributeLocalIF
{
  /**
   * 
   */
  private static final long serialVersionUID = -5334245078402002510L;

  /**
   * @param name
   * @param mdAttributeKey key of the defining metadata.
   * @param definingType
   * @param value
   */
  public AttributeLocal(String name, String mdAttributeKey, String definingType, String value)
  {
     super(name, mdAttributeKey, definingType, value);
  }

  /**
   *
   * @param name
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType
   * @param value
   * @param structDAO
   */
  public AttributeLocal(String name, String mdAttributeKey, String definingTransientType, String value, StructDAO structDAO)
  {
     super(name, mdAttributeKey, definingTransientType, value, structDAO);
  }

  /**
   * Returns the <code>MdLocalStructDAOIF</code> that defines the type that this attribute references.
   *
   *  Preconditions:  this.structDAO has been initialized.
   *
   */
  public MdLocalStructDAOIF getMdStructDAOIF()
  {
    return (MdLocalStructDAOIF)super.getMdStructDAOIF();
  }

  /**
   * Returns the localized string that is the best fit for the given locale.
   *
   * Duplicated in <code>LocalStruct</code>
   */
  public String getValue(Locale locale)
  {
    return MdAttributeLocalDAO.findAttributeValueMatch(this, locale);
  }

  /**
   * Alias for getLocaleMap
   */
  public Map<String, String> getLocalValues()
  {
    return this.getLocaleMap();
  }
  
  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getLocaleMap()
  {
    return com.runwaysdk.dataaccess.attributes.entity.AttributeLocal.getLocalValues(this.getStructDAO());
  }
}
