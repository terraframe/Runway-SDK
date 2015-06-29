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
package com.runwaysdk;





/**
 * Exception that is thrown when there is a problem with converting objects to DOM Documents 
 * and visa versa.
 */
public class ServerSideException extends ClientException
{
  /**
   * Auto-generated serial id
   */
  private static final long serialVersionUID = -5763249910326121427L;

  
  /**
   * Localized message from the server, if there was one.
   */
  private String serverBusinessMessage = null;
  
  /**
   * The name of the class that this exception wraps.
   */
  private String wrappedExceptionName = "";


  /**
   * Constructs a new ServerSideException with the specified cause and a
   * developer message taken from the cause. This constructor is useful if the
   * ServerSideException is a wrapper for another throwable.
   * 
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   * @param wrappedExceptionName
   *          Name of the exception thrown on the server.
   * @param serverBusinessMessage
   *          Error message from the server.
   *          
   */
  public ServerSideException(Throwable throwable, String wrappedExceptionName, String serverBusinessMessage)
  {
    super(throwable);
    this.wrappedExceptionName = wrappedExceptionName;
    this.serverBusinessMessage = serverBusinessMessage;
  }
  
  /**
   * Checks if this ApplicationException wraps an exception of the given
   * Exception name.
   * 
   * @param exceptionName
   *          The fully qualified name of the Exception
   * @return true if this ApplicationException wraps exceptionName, false
   *         otherwise.
   */
  public boolean wrapsException(String exceptionName)
  {
    return wrappedExceptionName.equalsIgnoreCase(exceptionName);
  }

  /**
   * Fetches the localized message template and plugs in the correct parameters
   * to set the business error message.
   * 
   */
  protected String defaultLocalizedMessage()
  {
    return ClientExceptionMessageLocalizer.serverSideException(this.getLocale());
  }

  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   * 
   */
  public String getLocalizedMessage()
  {   
    if (serverBusinessMessage != null)
    {
      return serverBusinessMessage;
    }
    else
    {
      return super.defaultLocalizedMessage();
    }
  }
}
