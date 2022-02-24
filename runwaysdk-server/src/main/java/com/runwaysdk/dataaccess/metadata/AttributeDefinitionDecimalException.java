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

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;

public class AttributeDefinitionDecimalException extends AttributeDefinitionException
{

  /**
   * 
   */
  private static final long serialVersionUID = 8660297899447236139L;

  private int               length;

  private int               decimal;

  public AttributeDefinitionDecimalException(String devMessage, MdAttributeDAOIF mdAttribute, int length, int decimal)
  {
    super(devMessage, mdAttribute);

    this.length = length;
    this.decimal = decimal;
  }

  public AttributeDefinitionDecimalException(String devMessage, Throwable cause, MdAttributeDAOIF mdAttribute, int length, int decimal)
  {
    super(devMessage, cause, mdAttribute);

    this.length = length;
    this.decimal = decimal;
  }

  public AttributeDefinitionDecimalException(Throwable cause, MdAttributeDAOIF mdAttribute, int length, int decimal)
  {
    super(cause, mdAttribute);

    this.length = length;
    this.decimal = decimal;
  }

  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   * 
   */
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.attributeDefinitionDecimalException(this.getLocale(), this.getMdAttribute(), length, decimal);
  }

}
