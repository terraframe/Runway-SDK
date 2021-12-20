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

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;

public class ReferenceAttributeNotReferencingClassException extends AttributeDefinitionException
{
  /**
   * 
   */
  private static final long serialVersionUID = 99677085747291689L;

  private MdClassDAOIF mdClassIF;
  
  /**
   * Thrown when a reference attribute type has not been configured to reference a class.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param mdAttribute
   *          Metadata containing the invalid definition.
   * @param mdClassIF
   *         Type that is not allowed to have attributes added to it at runtime.
   */
  public ReferenceAttributeNotReferencingClassException(String devMessage, MdAttributeConcreteDAOIF mdAttribute, MdClassDAOIF mdClassIF)
  {
    super(devMessage, mdAttribute);
    
    this.mdClassIF = mdClassIF;
  }
  
  /**
   * Returns the <code>MdAttributeIF</code> of the attribute involved in the exception.
   * @return the <code>MdAttributeIF</code> of the attribute involved in the exception.
   */
  protected MdAttributeConcreteDAOIF getMdAttribute()
  {
    return (MdAttributeConcreteDAOIF)super.getMdAttribute();
  }

  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   * 
   */
  public String getLocalizedMessage()
  {
    // Pass in the MdClass that defines the type of the MdAttribute
    return ServerExceptionMessageLocalizer.referenceAttributeNotReferencingClassException(this.getLocale(), this.getMdAttribute(), this.mdClassIF);
  }
}
