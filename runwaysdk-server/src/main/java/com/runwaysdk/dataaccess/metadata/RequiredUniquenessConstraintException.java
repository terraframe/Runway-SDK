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

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;

/**
 * Exception thrown when an MdAttribute is defined that attempts to apply a
 * uniqueness constraint on a non-required attribute.
 */
public class RequiredUniquenessConstraintException extends AttributeDefinitionException
{
  /**
   * 
   */
  private static final long serialVersionUID = 3366901493586526373L;

  /**
   * Constructor to set the developer message and the MdAttributeIF that violated
   * the Uniqueness Constraint.
   * 
   * @param devMessage
   * @param mdAttribute
   */
  public RequiredUniquenessConstraintException(String devMessage, MdAttributeConcreteDAOIF mdAttributeIF)
  {
    super(devMessage, mdAttributeIF);
  }
  
  /**
   * Returns the <code>MdAttributeIF</code> of the attribute involved in the exception.
   * @return the <code>MdAttributeIF</code> of the attribute involved in the exception.
   */
  protected MdAttributeConcreteDAOIF getMdAttribute()
  {
    return (MdAttributeConcreteDAOIF)this.getMdAttribute();
  }
  
  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   * 
   */
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.requiredUniquenessConstraintException(this.getLocale(), this.getMdAttribute());
  }

}
