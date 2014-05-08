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

import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;

public interface MdDimensionDAOIF extends MetadataDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE   = "md_dimension";
  
  /**
   * Returns the name of this {@link MdDimensionDAO}
   *
   * @return name of this {@link MdDimensionDAO}
   */
  public String getName();

  /**
   * Returns the attribute accessor name of this {@link MdDimensionDAO} that
   * is added to local structs.
   *
   * @return attribute accessor name of this {@link MdDimensionDAO} that
   * is added to local structs.
   */
  public String getAttributeAccessor();
  
  /**
   * Returns the name of the attribute that defines the default locale for the dimension.
   * 
   * @return name of the attribute that defines the default locale for the dimension
   */
  public String getDefaultLocaleAttributeName();

  /**
   * Returns the name of the attribute that defines the locale for the dimension.
   * 
   * @param localeToStringValue
   * 
   * @return name of the attribute that defines the locale for the dimension
   */
  public String getLocaleAttributeName(String localeToStringValue);
  
  /**
   * Returns the name of the attribute that defines the locale for the dimension.
   * 
   * @param mdDimensionDAOIF
   * @param locale
   * 
   * @return name of the attribute that defines the locale for the dimension
   */
  public String getLocaleAttributeName(Locale locale);

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdDimensionDAO getBusinessDAO();
  
  public List<MdAttributeDimensionDAOIF> getMdAttributeDimensions();
  
  public MdAttributeDimensionDAOIF getMdAttributeDimension(MdAttributeDAOIF mdAttribute);

}
