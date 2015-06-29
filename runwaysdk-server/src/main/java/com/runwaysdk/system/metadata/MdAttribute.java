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
package com.runwaysdk.system.metadata;

import java.util.Locale;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;

public abstract class MdAttribute extends MdAttributeBase
{
  private static final long serialVersionUID = 1229405887898L;

  public MdAttribute()
  {
    super();
  }
  
  public abstract MetadataDisplayLabel getDisplayLabel();

  /**
   * Returns the display label of this metadata object. If this metadata has no
   * display label, it returns the one from the referenced
   * <code>MdAttributeConcrete</code>.
   *
   * @return the display label of this metadata object.
   */
  public String getDisplayLabel(Locale locale)
  {
    return ((MdAttributeDAOIF)BusinessFacade.getEntityDAO(this)).getDisplayLabel(locale);
  }
  
  /**
   * Returns the column name of the attribute.
   * 
   * @return
   */
  public abstract String getColumnName();
  
  /**
   * Returns the attribute name.
   * 
   * @return
   */
  public abstract String getAttributeName();
}
