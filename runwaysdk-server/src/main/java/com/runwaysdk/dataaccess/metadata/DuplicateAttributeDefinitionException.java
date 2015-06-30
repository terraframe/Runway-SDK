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

public class DuplicateAttributeDefinitionException extends AttributeDefinitionException
{
  /**
   * 
   */
  private static final long serialVersionUID = -2330231593706704041L;
  /**
   * class that a new attribute is being added to.
   */
  private MdClassDAOIF mdClassIF;
  
  /**
   * Constructor to set the developer message and the MdAttributeIF of the attribute that is
   * already defined by the given class.
   * 
   * @param devMessage
   * @param mdAttributeIF metadata of the attribute that is already defined.
   * @param mdClassIF class that a new attribute is being added to.
   */
  public DuplicateAttributeDefinitionException(String devMessage, MdAttributeConcreteDAOIF mdAttributeIF, MdClassDAOIF mdClassIF)
  {
    super(devMessage, mdAttributeIF);
    this.mdClassIF = mdClassIF;
  }
  
  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   * 
   */
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.duplicateAttributeDefinition(this.getLocale(), this.getMdAttribute(), this.mdClassIF);
  }
}
