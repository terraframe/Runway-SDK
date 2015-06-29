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
package com.runwaysdk.facade;

import com.runwaysdk.RunwayException;
import com.runwaysdk.ServerExceptionMessageLocalizer;

public class GroovyQueryException extends RunwayException
{
  /**
   * 
   */
  private static final long serialVersionUID = 425488808339268113L;

  /**
   * The query that caused the error.
   */
  private String groovyQuery;
  
  /**
   * The query error as described by the Groovy exception.
   */
  private String queryError;
  
  /**
   * Creates a new GroovyQueryException with the given cause and specific queryError.
   * The queryError value is a string that describes either a groovy compilation or property
   * error.
   * 
   * @param cause
   * @param groovyQuery The query string that caused the exception.
   * @param queryError The groovy error message.
   */
  public GroovyQueryException(Throwable cause, String groovyQuery, String queryError)
  {
    super(cause);
    
    this.groovyQuery = groovyQuery;
    this.queryError = queryError;
  }

  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   * 
   */
  public String getLocalizedMessage()
  { 
    return ServerExceptionMessageLocalizer.groovyQueryException(this.getLocale(), groovyQuery, queryError);
  }
}
