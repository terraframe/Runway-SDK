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

import java.util.Locale;
import java.util.Map;

public interface AttributeLocalIF extends AttributeStructIF
{
  
  /**
   * Returns the <code>MdLocalStructDAOIF</code> that defines the type that this attribute references.
   * 
   *  Preconditions:  this.structDAO has been initialized.
   *  
   */
  public MdLocalStructDAOIF getMdStructDAOIF();

  /**
   * Returns the value for the given locale.  If the locale was not
   * defined on the attribute, the best match is returned.
   * 
   * @param locale
   * @return
   */
  public String getValue(Locale locale);
 
  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   * 
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getLocalValues();
  
}
