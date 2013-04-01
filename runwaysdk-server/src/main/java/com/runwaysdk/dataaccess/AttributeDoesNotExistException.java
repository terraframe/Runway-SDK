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
package com.runwaysdk.dataaccess;

import com.runwaysdk.ServerExceptionMessageLocalizer;

public class AttributeDoesNotExistException extends DataAccessException
{
  
  /**
   * 
   */
  private static final long serialVersionUID = 1254096379451502816L;

  private String attributeName;
  
  private MdClassDAOIF definingMdClassIF;
  
  /**
   * Constructs a new AttributeDoesNotExistException with the specified developer message
   * and a default business message. Leaving the default business message is
   * discouraged, as it provides no context information for end users.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param attributeName
   *          name of the attribute that does not exist on a component
   * @param definingMdClassIF
   *          name of the defining type, if there is one (may be null)
   */
  public AttributeDoesNotExistException(String devMessage, String attributeName, MdClassDAOIF definingMdClassIF)
  {
    super(devMessage);
    this.attributeName = attributeName;
    this.definingMdClassIF = definingMdClassIF;
  }

  /**
   * Constructs a new AttributeDoesNotExistException with the specified detail message and
   * cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is <i>not</i>
   * automatically incorporated in this AttributeDoesNotExistException's detail message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   * @param attributeName
   *          name of the attribute that does not exist on a component
   * @param definingMdClassIF
   *          name of the defining type, if there is one (may be null)
   */
  public AttributeDoesNotExistException(String devMessage, Throwable cause, String attributeName, MdClassDAOIF definingMdClassIF)
  {
    super(devMessage, cause);
    this.attributeName = attributeName;
    this.definingMdClassIF = definingMdClassIF;
  }

  /**
   * Constructs a new AttributeDoesNotExistException with the specified cause and a
   * developer message taken from the cause. This constructor is useful if the
   * AttributeDoesNotExistException is a wrapper for another throwable.
   * 
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   * @param attributeName
   *          name of the attribute that does not exist on a component
   * @param definingMdClassIF
   *          name of the defining type, if there is one (may be null)
   */
  public AttributeDoesNotExistException(Throwable cause, String attributeName, MdClassDAOIF definingMdClassIF)
  {
    super(cause);
    this.attributeName = attributeName;
    this.definingMdClassIF = definingMdClassIF;
  }
  
  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   * 
   */
  public String getLocalizedMessage()
  { 
    if (definingMdClassIF == null)
    {
      return ServerExceptionMessageLocalizer.attributeDoesNotExistExceptionNoType(this.getLocale(), attributeName);
    }
    else
    {
      return ServerExceptionMessageLocalizer.attributeDoesNotExistExceptionWithType(this.getLocale(), attributeName, this.definingMdClassIF.getDisplayLabel(this.getLocale()));
    }
  }
}
