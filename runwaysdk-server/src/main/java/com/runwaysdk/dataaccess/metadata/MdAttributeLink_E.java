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
package com.runwaysdk.dataaccess.metadata;

import com.runwaysdk.constants.MdAttributeEmbeddedInfo;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;

/**
 * Embedded attributes are not currently supported {@link MdEntityDAOIF} types.
 * @author nathan
 *
 */
public class MdAttributeLink_E extends MdAttributeConcrete_E
{
  /**
   * 
   */
  private static final long serialVersionUID = -4301893860228485571L;

  /**
   * @param mdAttribute
   */
  public MdAttributeLink_E(MdAttributeConcreteDAO mdAttribute)
  {
    super(mdAttribute);
    
    throw new ProgrammingErrorException("Embedded attributes are not supported on "+MdAttributeEmbeddedInfo.CLASS+"");
  }
  
  /**
   * Returns the formated DB column type used in the database in the syntax of
   * the current DB vendor.
   *
   * @return formated DB column type used in the database in the syntax of the
   *         current DB vendor.
   */
  protected String getDbColumnType()
  {
    return "";
  }
  
  /**
   * Balk.
   */
  protected void createDbColumn(String tableName) {}
  
  /**
   * Balk.
   */
  protected void validate() {}
}
