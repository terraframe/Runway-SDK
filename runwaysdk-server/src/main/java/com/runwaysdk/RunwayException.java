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
package com.runwaysdk;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import com.runwaysdk.business.LocalizableIF;
import com.runwaysdk.localization.CommonExceptionMessageLocalizer;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.Session;

/**
 * The abstract root of all Exceptions in Runway. Contains the mechanisms for
 * setting and retreiving buiness layer (end user) and data access layer
 * (developer) exception messages. Exceptions in Runway will take relevant
 * entities as parameters and construct messages internally. However, messages
 * can be overridden to allow for context specific information, most often taken
 * from the business layer.
 * 
 * Note that <code>RunwayException</code> (and thus all exceptions in the
 * heirarchy) extends RuntimeException.
 * 
 * @author Eric Grunzke
 */
public abstract class RunwayException extends RuntimeException implements RunwayExceptionIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 5331683158357103999L;

  /**
   * The locale of the session user.
   */
  private Locale            locale;

  /**
   * Constructs a new <code>RunwayException</code> with the specified developer
   * message and a default business message. Leaving the default business
   * message is discouraged, as it provides no context information for end
   * users.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   */
  public RunwayException(String devMessage)
  {
    super(devMessage);
  }

  /**
   * Constructs a new <code>RunwayException</code> with the specified detail
   * message and cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is
   * <i>not</i> automatically incorporated in this
   * <code>RunwayException</code>'s detail message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is permitted,
   *          and indicates that the cause is nonexistent or unknown.)
   */
  public RunwayException(String devMessage, Throwable cause)
  {
    super(devMessage, cause);
  }

  /**
   * Constructs a new <code>RunwayException</code> with the specified cause and
   * a developer message taken from the cause. This constructor is useful if the
   * <code>RunwayException</code> is a wrapper for another throwable.
   * 
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is permitted,
   *          and indicates that the cause is nonexistent or unknown.)
   */
  public RunwayException(Throwable cause)
  {
    super(cause);
  }

  /**
   * Sets the locale used for the localized message.
   * 
   * @param locale
   */
  public void setLocale(Locale locale)
  {
    this.locale = locale;
  }

  /**
   * Returns the locale set for this exception. If none is externally specified,
   * it returns the locale for the Session.
   * 
   * @return the locale set for this exception.
   */
  public Locale getLocale()
  {
    if (this.locale == null)
    {
      this.locale = Session.getCurrentLocale();
    }
    return this.locale;
  }

  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   * 
   */
  public String getLocalizedMessage()
  {
    return CommonExceptionMessageLocalizer.runwayException(this.getLocale());
  }

  public String toString()
  {
    String s = getClass().getName();
    String message = this.getMessage();
    return ( message != null ) ? ( s + ": " + message ) : s;
  }
  
  /**
   * Localizes the throwable in a way that will properly account for Java exceptions and all
   * built-in Runway exception types (like SmartException). If a message cannot be produced
   * from the standard methods (i.e. localize), then the class display label or even the
   * exception's fully qualified classname may be used (in worst case scenario).
   * 
   * @param t
   * @param locale
   * @return A message (hopefully user-friendly, localized) for the exception.
   */
  @Request
  public static String localizeThrowable(Throwable t, Locale locale)
  {
    String errorMessage = null;
    
    if (t instanceof InvocationTargetException)
    {
      t = t.getCause();
    }
    
    if (t instanceof LocalizableIF)
    {
      LocalizableIF localizable = ( (LocalizableIF) t );
      
      errorMessage = localizable.localize(locale);
      
      if (errorMessage == null || errorMessage.length() == 0)
      {
        errorMessage = t.getLocalizedMessage();
      }
      if (errorMessage == null || errorMessage.length() == 0)
      {
        errorMessage = localizable.getClassDisplayLabel();
      }
      if (errorMessage == null || errorMessage.length() == 0)
      {
        errorMessage = localizable.getType();
      }
    }
    else
    {
      if (t instanceof RunwayException)
      {
        ( (RunwayException) t ).setLocale(locale);
      }
      
      errorMessage = t.getLocalizedMessage();
      
      if (errorMessage == null || errorMessage.length() == 0)
      {
        errorMessage = t.getMessage();
      }
    }
    
    if (errorMessage == null || errorMessage.length() == 0)
    {
      errorMessage = t.getClass().getTypeName();
    }
    
    return errorMessage;
  }
}
