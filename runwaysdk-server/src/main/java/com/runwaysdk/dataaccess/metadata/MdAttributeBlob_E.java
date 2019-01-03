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

import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;

/**
 * 
 */
public class MdAttributeBlob_E extends MdAttributeConcrete_E
{
  /**
   * 
   */
  private static final long serialVersionUID = -5586157936178811562L;

  /**
   * @param mdAttribute
   */
  public MdAttributeBlob_E(MdAttributeConcreteDAO mdAttribute)
  {
    super(mdAttribute);
  } 

  /**
   * Validation specific rules for blob attribute metadata.
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
      if (!index.dereference()[0].getOid().equalsIgnoreCase(IndexTypes.NO_INDEX.getOid()))
      {
        isValid = false;
      }
    }
    
    if (!isValid)
    {
      MdBusinessDAOIF mdBusiness = this.getMdAttribute().getMdBusinessDAO();
      String error = "[" + mdBusiness.definesType()
          + "] Attributes cannot participate in a uniqueness constraint.";
      throw new AttributeInvalidUniquenessConstraintException(error, this.getMdAttribute());
    }
    
    super.validate();
  }

}
