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

import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.database.Database;

public class MdAttributeIndicator_E extends MdAttributeConcrete_E
{
  /**
   * 
   */
  private static final long serialVersionUID = -927194333693014887L;

  /**
   * @param mdAttribute
   */
  public MdAttributeIndicator_E(MdAttributeConcreteDAO mdAttribute)
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
    return Database.formatCharacterField(dbType, Database.DATABASE_ID_SIZE);
  }
  
  /**
   * Adds a column in the database to the given table.
   *
   * <br/><b>Precondition:</b>  tableName != null
   * <br/><b>Precondition:</b>  !tableName.trim().equals("")
   *
   */
  protected void createDbColumn(String tableName)
  {
    // Don't do anything (balking pattern), as the indicator has no database column.
  }
  
  /**
   * Drops the column of the given name from the given table.
   *
   * <br/>
   * <b>Precondition:</b> tableName != null <br/>
   * <b>Precondition:</b> attrName != null
   *
   * <br/>
   * <b>Postcondition:</b> All instances of the given class have the given
   * attribute cleared
   *
   * @param tableName
   *          Name of the table the attribute belongs to.
   * @param columnName
   *          Column name of the attribute to drop from the database.
   * @param the
   *          database column type formatted to the database vendor syntax.
   */
  protected void dropAttribute(String tableName, String columnName, String dbColumnType)
  {
// Heads up: Test
//    Database.dropField(tableName, columnName, dbColumnType, this.getMdAttribute());
  }

  
  /**
   *Validates this metadata object.
   *
   * @throws DataAccessException when this MetaData object is not valid.
   */
  protected void validate()
  {
    if (this.getMdAttribute().isNew())
    {
      this.validateNew();
    }

    super.validate();
  }
  
  /**
   * Cannot reference instances of a structs.
   *
   */
  private void validateNew()
  {
    MdEntityDAOIF mdEntityIF = this.definedByClass();
    if (mdEntityIF instanceof MdStructDAOIF)
    {
      String errMsg = "[" + mdEntityIF.definesType()
          + "] is a StructDAO and cannot have a reference attribute.";
      throw new AttributeOfWrongTypeForClassException(errMsg, this.getMdAttribute(), mdEntityIF);
    }
  }
}
