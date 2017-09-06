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

public class InvalidIndicatorDefinition extends MetadataException
{
  /**
   * 
   */
  private static final long serialVersionUID = -6521886282744211727L;
  private String localizedIndicatorDisplayLabel;
  
  /**
   * Constructs a new {@link InvalidIndicatorDefinition} with the specified developer message.
   * 
   * @param _devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.     
   * @param _localizedIndicatorLabel
   *        Localized Indicator Display Label
   */
  public InvalidIndicatorDefinition(String _devMessage, String _localizedIndicatorLabel)
  {
    super(_devMessage);
    
    this.localizedIndicatorDisplayLabel = _localizedIndicatorLabel;
  }

  /**
   * Constructs a new {@link InvalidIndicatorDefinition} with the specified developer message and
   * cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is <i>not</i>
   * automatically incorporated in this MetaDataException's detail message.
   * 
   * @param devMe_devMessagessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param _cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   * @param _localizedIndicatorLabel
   *        Localized Indicator Display Label
   */
  public InvalidIndicatorDefinition(String _devMessage, Throwable _cause, String _localizedIndicatorLabel)
  {
    super(_devMessage, _cause);
    this.localizedIndicatorDisplayLabel = _localizedIndicatorLabel;
  }

  /**
   * Constructs a new  {@link InvalidIndicatorDefinition} with the specified cause and a developer
   * message taken from the cause. This constructor is useful if the
   * MetaDataException is a wrapper for another throwable.
   * 
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   * @param _localizedIndicatorLabel
   *        Localized Indicator Display Label
   */
  public InvalidIndicatorDefinition(Throwable _cause, String _localizedIndicatorLabel)
  {
    super(_cause);
    this.localizedIndicatorDisplayLabel = _localizedIndicatorLabel;
  }
  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   * 
   */
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.invalidIndicatorDefinition(this.getLocale(), this.localizedIndicatorDisplayLabel);
  }

}
