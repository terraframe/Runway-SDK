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
package com.runwaysdk.dataaccess.metadata;

public class DuplicateAttributeDefinedInSubclassExceptionDTO extends AttributeDefinitionExceptionDTO
{
  /**
   * 
   */
  private static final long serialVersionUID = -2792271477486920913L;

  /**
   * Constructor to set the developer message and the MdAttributeIF of the attribute that is
   * already defined by the given class.
   * 
   * @param type of the runway exception.
   * @param localizedMessage end user error message.
   * @param developerMessage developer error message.
   */
  public DuplicateAttributeDefinedInSubclassExceptionDTO(String type, String localizedMessage, String developerMessage)
  {
    super(type, localizedMessage, developerMessage);
  }
}
