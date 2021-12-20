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
/**
 * Created on Aug 29, 2005
 *
 */
package com.runwaysdk.dataaccess;

import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;


/**
 * @author nathan
 *
 */
public interface MdAttributeCharacterDAOIF extends MdAttributeCharDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE   = "md_attribute_character";
  
  /**
   *Returns the total maximum length of this character field.
   * @return total maximum length of this character field.
   */
  public String getSize();
  
  /**
   * 
   * @return
   */
  public MdAttributeCharacterDAO getBusinessDAO();
}
