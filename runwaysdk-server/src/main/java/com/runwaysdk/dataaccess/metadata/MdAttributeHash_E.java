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
package com.runwaysdk.dataaccess.metadata;

import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.constants.MdAttributeHashInfo;
import com.runwaysdk.dataaccess.database.Database;


public class MdAttributeHash_E extends MdAttributeConcrete_E
{
  private static final long serialVersionUID = 4974872165032380400L;

  /**
   * @param mdAttribute
   */
  public MdAttributeHash_E(MdAttributeConcreteDAO mdAttribute)
  {
    super(mdAttribute);
  }

  /**
   * Returns the formated DB column type used in the database in the syntax of the current DB vendor.
   * @return formated DB column type used in the database in the syntax of the current DB vendor.
   */
  protected String getDbColumnType()
  {
    String dbType = DatabaseProperties.getDatabaseType(this.getMdAttribute());
    return Database.formatCharacterField(dbType, Integer.toString(MdAttributeHashInfo.HASH_SIZE));
  }

  /**
   * Modifies the column definition in the database to the given table, if such a
   * modification is necessary.
   *
   * <br/><b>Precondition:</b>  tableName != null
   * <br/><b>Precondition:</b>  !tableName.trim().equals("")
   *
   */
  protected void modifyDbColumn(String tableName)
  {
    // do nothing since the size of an MdAttributeHash cannot be modified.
  }



}
