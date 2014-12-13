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

import com.runwaysdk.RunwayException;
import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;

public class ExpressionException extends RunwayException
{

  /**
   * 
   */
  private static final long serialVersionUID = -4011180720101769969L;

  protected MdAttributeDAOIF mdAttributeDAOIF;

  protected String expressionSyntaxExceptionMessage;
  
  /**
   * Constructs a new <code>ExpressionException</code> for when an expression attribute has an 
   * expression that attempts to perform an action that is not allowed, such as calling a setter method.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param _mdAttributeDAOIF
   * @param _expressionSyntaxException
   */
  public ExpressionException(String _devMessage, MdAttributeDAOIF _mdAttributeDAOIF, Exception _expressionSyntaxException)
  {
    super(_devMessage);
    
    this.mdAttributeDAOIF = _mdAttributeDAOIF;
    
    this.expressionSyntaxExceptionMessage = _expressionSyntaxException.getLocalizedMessage();
  }
  
  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   * 
   */
  public String getLocalizedMessage()
  { 
    return ServerExceptionMessageLocalizer.expressionException(this.getLocale(), this.mdAttributeDAOIF, this.expressionSyntaxExceptionMessage);
  }

}
