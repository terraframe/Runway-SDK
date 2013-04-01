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
package com.runwaysdk.dataaccess.metadata;

import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.database.Database;

public class MdAttributeClob_E extends MdAttributeConcrete_E
{

  /**
   *
   */
  private static final long serialVersionUID = 8050385955533723766L;

  /**
   * @param mdAttribute
   */
  public MdAttributeClob_E(MdAttributeConcreteDAO mdAttribute)
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
    return Database.formatClobField(dbType);
  }

  /**
   * Validation specific rules for text attribute metadata.
   */
  protected void validate()
  {
    boolean isValid = true;

    if (!this.getMdAttribute().isNew())
    {
      if (this.getMdAttribute().isUnique() ||
          this.getMdAttribute().isPartOfIndexedAttributeGroup())
      {
        isValid = false;
      }
    }
    else
    {
      AttributeEnumerationIF index = (AttributeEnumerationIF)this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);
      if (!index.dereference()[0].getId().equalsIgnoreCase(IndexTypes.NO_INDEX.getId()))
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

    super.validate();
  }

}
