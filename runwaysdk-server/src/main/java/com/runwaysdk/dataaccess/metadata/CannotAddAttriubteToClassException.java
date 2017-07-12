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

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;

public class CannotAddAttriubteToClassException extends AttributeDefinitionException
{

  /**
   * 
   */
  private static final long serialVersionUID = -3703805310695117200L;

  private MdClassDAOIF forbiddenMdClassIF;
  
  /**
   * Thrown when an attribute is added to a type that cannot have attributes to it at runtime.
   * Constructs a new CannotAddAttriubteToClassException with the specified developer message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param mdAttribute
   *          Metadata containing the invalid definition.
   * @param forbiddenMdClassIF
   *         Type that is not allowed to have attributes added to it at runtime.
   */
  public CannotAddAttriubteToClassException(String devMessage, MdAttributeConcreteDAOIF mdAttribute, MdClassDAOIF forbiddenMdClassIF)
  {
    super(devMessage, mdAttribute);
    
    this.forbiddenMdClassIF = forbiddenMdClassIF;
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
    return ServerExceptionMessageLocalizer.cannotAddAttriubteToClassException(this.getLocale(), this.getMdAttribute(), this.forbiddenMdClassIF);
  }
}
