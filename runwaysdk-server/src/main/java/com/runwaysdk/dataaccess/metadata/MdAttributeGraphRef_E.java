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
 * Created on Aug 21, 2005
 *
 */
package com.runwaysdk.dataaccess.metadata;

import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.database.Database;

/**
 * @author nathan
 *
 */
public class MdAttributeGraphRef_E extends MdAttributeConcrete_E
{
  /**
   * Generated by Eclipse
   */
  private static final long serialVersionUID = -1857726735215561444L;

  /**
   * @param mdAttribute
   */
  public MdAttributeGraphRef_E(MdAttributeConcreteDAO mdAttribute)
  {
    super(mdAttribute);
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
    String dbType = DatabaseProperties.getDatabaseType(this.getMdAttribute());
    return Database.formatCharacterField(dbType, Database.DATABASE_ID_SIZE);
  }

  /**
   * Adds a column in the database to the given table.
   *
   * <br/>
   * <b>Precondition:</b> tableName != null <br/>
   * <b>Precondition:</b> !tableName.trim().equals("")
   *
   */
  protected void createDbColumn(String tableName)
  {
    if (!this.appliedToDB)
    {
      Database.addField(tableName, this.getMdAttribute().getColumnName(), this.dbColumnType, this.getMdAttribute());
    }
  }

  /**
   * Validates this metadata object.
   *
   * @throws DataAccessException
   *           when this MetaData object is not valid.
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
      String errMsg = "[" + mdEntityIF.definesType() + "] is a StructDAO and cannot have a reference attribute.";
      throw new AttributeOfWrongTypeForClassException(errMsg, this.getMdAttribute(), mdEntityIF);
    }
  }

}