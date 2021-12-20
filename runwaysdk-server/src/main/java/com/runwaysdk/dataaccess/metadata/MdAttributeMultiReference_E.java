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
 * 
 */
package com.runwaysdk.dataaccess.metadata;

import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF;
import com.runwaysdk.dataaccess.database.Database;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
public class MdAttributeMultiReference_E extends MdAttributeConcrete_E
{
  /**
   *
   */
  private static final long serialVersionUID = 8918935259483483520L;

  /**
   * If true then delete all enumeration mappings between instances of this
   * attribute and the enumeration items they reference. Set this to false when
   * deleting the MdBusiness this maps to, as it will otherwise cause a deadlock
   * on some databases.
   */
  protected boolean         deleteInstances  = true;

  /**
   * @param mdAttribute
   */
  public MdAttributeMultiReference_E(MdAttributeConcreteDAO mdAttribute)
  {
    super(mdAttribute);
  }

  /**
   * Returns the MdAttribute.
   * 
   * @return the MdAttribute
   */
  protected MdAttributeMultiReferenceDAO getMdAttribute()
  {
    return (MdAttributeMultiReferenceDAO) this.mdAttribute;
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

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeConcrete_E#save()
   */
  @Override
  public void save()
  {
    boolean originalAppliedToDB = this.appliedToDB;

    super.save();

    if (!originalAppliedToDB)
    {
      String tableName = ( (MdAttributeMultiReferenceDAOIF) this.mdAttribute ).getTableName();

      Database.createEnumerationTable(tableName, this.mdAttribute.getOid());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeConcrete_E#delete()
   */
  @Override
  public void delete(DeleteContext context)
  {
    super.delete(context);

    if (this.deleteInstances)
    {
      String tableName = ( (MdAttributeMultiReferenceDAOIF) this.mdAttribute ).getTableName();

      Database.dropClassTable(tableName);
    }
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
      AttributeEnumerationIF index = (AttributeEnumerationIF) this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

      if (!index.dereference()[0].getOid().equalsIgnoreCase(IndexTypes.NO_INDEX.getOid()))
      {
        isValid = false;
      }
    }

    if (!isValid)
    {
      String error = "[" + this.getMdAttribute().getMdBusinessDAO().definesType() + "] Attributes cannot participate in a uniqueness constraint.";
      throw new AttributeInvalidUniquenessConstraintException(error, this.getMdAttribute());
    }

    super.validate();
  }
}