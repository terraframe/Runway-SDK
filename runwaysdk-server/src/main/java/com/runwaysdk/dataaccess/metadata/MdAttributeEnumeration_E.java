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
 * Created on Aug 28, 2005
 *
 */
package com.runwaysdk.dataaccess.metadata;

import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.database.Database;

/**
 * @author nathan
 *
 */
public class MdAttributeEnumeration_E extends MdAttributeConcrete_E
{
  /**
   *
   */
  private static final long serialVersionUID = 8918935259483483520L;

  /**
   * If true then delete all enumeration mappings between instances of this
   * attribute and the enumeration items they reference. Set this to false when
   * deleting the MdEnumeration this maps to, as it will otherwise cause a
   * deadlock on some databases.
   */
  protected boolean         deleteInstances  = true;

  /**
   * @param mdAttribute
   */
  public MdAttributeEnumeration_E(MdAttributeConcreteDAO mdAttribute)
  {
    super(mdAttribute);
  }

  /**
   * Returns the MdAttribute.
   *
   * @return the MdAttribute
   */
  protected MdAttributeEnumerationDAO getMdAttribute()
  {
    return (MdAttributeEnumerationDAO) this.mdAttribute;
  }

  /**
   * Set the cache column name
   */
  protected void preSaveValidate()
  {
    super.preSaveValidate();

    if (this.getMdAttribute().isNew() && !this.appliedToDB)
    {
      this.getMdAttribute().getAttribute(MdAttributeEnumerationInfo.CACHE_COLUMN_NAME).setValue(
        MdAttributeEnumerationDAO.getCacheDbColumnName(this.getMdAttribute().getColumnName()));
    }
  }

  /**
   * Delete all enumeration mappings between instances of this attribute and the
   * enumeration items they reference.
   *
   * Deletes an attribute from the runway. The BusinessDAO is deleted from the
   * database and removed from the cache. All relationships pertaining to this
   * BusinessDAO are also removed as well.
   *
   * <br/>
   * <b>Postcondition: </b> BusinessDAO and all dependencies are removed from
   * the runway <br/>
   * <b>Postcondition: </b> Corresponding column from the defining table is
   * dropped
   * @param p_mdAttribute
   *          Attribute metadata BusinessDAO
   */
  public void delete(DeleteContext context)
  {
    // get the MdEntity that defines this attribute
    MdEntityDAOIF mdEntity = this.definedByClass();

    if (this.deleteInstances)
    {
      Database.deleteAllEnumAttributeInstances(this.getMdAttribute().getMdEnumerationDAO()
          .getTableName(), mdEntity.getTableName(), this.getMdAttribute().getColumnName());
    }

    super.delete(context);
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
    super.createDbColumn(tableName);

    if (!this.appliedToDB)
    {
      String cacheDbColumnType = DatabaseProperties
          .getDatabaseType(MdAttributeEnumerationInfo.CACHE_COLUMN_DATATYPE);
      Database.addField(tableName, this.getMdAttribute().getCacheColumnName(), cacheDbColumnType, this.getMdAttribute());
    }
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
    return Database.formatCharacterField(dbType, Database.DATABASE_SET_ID_SIZE);
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
    super.dropAttribute(tableName, columnName, dbColumnType);

    Database.dropField(tableName, this.getMdAttribute().getCacheColumnName(),
        MdAttributeEnumerationInfo.CACHE_COLUMN_DATATYPE, this.getMdAttribute());
  }

  /**
   * Validation specific rules for text attribute metadata.
   */
  protected void validate()
  {
    boolean isValid = true;

    if (!this.getMdAttribute().isNew())
    {
      if (this.getMdAttribute().isUnique() || this.getMdAttribute().isPartOfIndexedAttributeGroup())
      {
        isValid = false;
      }
    }
    else
    {
      AttributeEnumerationIF index = (AttributeEnumerationIF) this.getMdAttribute().getAttributeIF(
          MdAttributeConcreteInfo.INDEX_TYPE);
      if (!index.dereference()[0].getOid().equalsIgnoreCase(IndexTypes.NO_INDEX.getOid()))
      {
        isValid = false;
      }
    }

    if (!isValid)
    {
      String error = "[" + this.getMdAttribute().getMdBusinessDAO().definesType()
          + "] Attributes cannot participate in a uniqueness constraint.";
      throw new AttributeInvalidUniquenessConstraintException(error, this.getMdAttribute());
    }

    // make sure that this MdAttributeEnumeration isn't defined by an MdEntity
    // that extends EnumerationMaster
    for (MdEntityDAOIF mdEntityIF : definedByClass().getSuperClasses())
    {
      String type = mdEntityIF.definesType();
      if (type.equals(EnumerationMasterInfo.CLASS))
      {
        String error = "[" + this.getMdAttribute().getMdBusinessDAO().definesType()
            + "] Attributes cannot be defined by a type that extends " + EnumerationMasterInfo.CLASS;
        throw new CannotAddAttriubteToClassException(error, this.getMdAttribute(), mdEntityIF);
      }
    }
    super.validate();
  }

}
