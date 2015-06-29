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
package com.runwaysdk.dataaccess.io.excel;

import com.runwaysdk.RunwayException;
import com.runwaysdk.ServerExceptionMessageLocalizer;

public class FieldConversionException extends RunwayException
{

  /**
   * 
   */
  private static final long serialVersionUID = -7377531545880403092L;

  private FieldColumn       colum;

  public FieldConversionException(String msg, FieldColumn column)
  {
    super(msg);

    this.colum = column;
  }

  /**
   * Fetches the localized message template and plugs in the correct parameters
   * to set the business error message.
   * 
   */
  protected String defaultLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.fieldConversionException(this.getLocale(), this.colum.getDisplayLabel());
  }

}
