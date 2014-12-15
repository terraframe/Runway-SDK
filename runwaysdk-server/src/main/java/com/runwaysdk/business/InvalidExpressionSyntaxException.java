
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
package com.runwaysdk.business;

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;

public class InvalidExpressionSyntaxException extends ExpressionException
{

  /**
   * 
   */
  private static final long serialVersionUID = 8456308337956866215L;

  private MdAttributeDAOIF mdAttributeDAOIF;
  
  /**
   * Constructs a new <code>InvalidExpressionSyntaxException</code> for when an expression attribute has an 
   * expression defined with an invalid syntax.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param attributeDisplayLabel
   * @param invalidValue
   */
  public InvalidExpressionSyntaxException(String _devMessage, MdAttributeDAOIF _mdAttributeDAOIF, Exception _expressionSyntaxException)
  {
    super(_devMessage, _mdAttributeDAOIF, _expressionSyntaxException);
    
    this.mdAttributeDAOIF = _mdAttributeDAOIF;
  }
  
  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   * 
   */
  public String getLocalizedMessage()
  { 
    return ServerExceptionMessageLocalizer.invalidExpressionSyntaxException(this.getLocale(), this.mdAttributeDAOIF, this.expressionSyntaxExceptionMessage);
  }
}
