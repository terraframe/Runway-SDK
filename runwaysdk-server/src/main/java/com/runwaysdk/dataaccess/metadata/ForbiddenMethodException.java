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

import com.runwaysdk.business.BusinessException;

/**
 * Certain attribute types (Blob, Struct, and ReferenceField at the time of this
 * writing) require special handling during code generation. Because of this,
 * some abstract methods from
 * {@link com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO} should
 * not be called for these special cases. If they are, this exception is thrown.
 * 
 * @author Eric Grunzke
 */
public class ForbiddenMethodException extends BusinessException
{
  /**
   * Generated by Eclipse
   */
  private static final long serialVersionUID = -3660277266759264298L;

  /**
   * @param devMessage
   *          The non-localized developer error message. An informative
   *          developer message is important, because this exception uses the
   *          default message for the end user.
   */
  public ForbiddenMethodException(String devMessage)
  {
    super(devMessage);
  }
  
  public ForbiddenMethodException(String devMessage, Throwable throwable)
  {
    super(devMessage, throwable);
  }
}
