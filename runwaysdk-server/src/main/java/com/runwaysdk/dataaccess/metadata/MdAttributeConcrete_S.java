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
package com.runwaysdk.dataaccess.metadata;

import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdTransientDAOIF;

public class MdAttributeConcrete_S extends MdAttributeConcreteStrategy
{
  /**
   * 
   */
  private static final long serialVersionUID = -2282911016723560870L;

  /**
   * @param mdAttribute
   */
  public MdAttributeConcrete_S(MdAttributeConcreteDAO mdAttribute)
  {
    super(mdAttribute);
  } 
  
  /**
   * Returns the {@link MdTransientDAOIF} that defines this {@link MdAttributeConcreteDAOIF}.
   * 
   * @return the {@link MdTransientDAOIF} that defines this {@link MdAttributeConcreteDAOIF}.
   */
  public MdTransientDAOIF definedByClass()
  { 
    return (MdTransientDAOIF) super.definedByClass();    
  }
  
  protected void preSaveValidate()
  {
    super.preSaveValidate();
 
    this.nonMdEntityCheckExistingForAttributeOnCreate();
    
    if (this.getMdAttribute().isNew() && !this.appliedToDB)
    {
      // Supply a default value, as it does not make sense to have a column name for a transient attribute.
      this.getMdAttribute().getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValue("n_a");
    }
  }
  
  /**
   * Contains special logic for saving an attribute.
   */
  public void save()
  {
    this.validate();
    
    this.nonMdEntityPostSaveValidationOperations();
  }

  /**
   * No special validation logic.
   */
  protected void validate() 
  {
    this.nonMdEntityValidate();
  }
  
}
