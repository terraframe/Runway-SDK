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
package com.runwaysdk.dataaccess.attributes;

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;

/**
 * Thrown when a required attribute has no value
 * 
 * @author William Rose
 */
public class AttributeValueCannotBeNegativeProblem extends AttributeValueProblem
{

  /**
   * Constructs a new EmptyValueException with the specified developer message.
   * 
   * @param componentId
   *          oid of the containing component.
   * @param mdClassIF
   *          defines the type of the containing component.
   * @param mdAttributeIF
   *          defines the attribute.
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param attribute
   *          The attribute with an invalid value
   * @param value
   *          The value given for the attribute
   */
  public AttributeValueCannotBeNegativeProblem(String componentId, MdClassDAOIF mdClassIF, MdAttributeDAOIF mdAttributeIF, String developerMessage, AttributeIF attribute, String value)
  {
    super(componentId, mdClassIF, mdAttributeIF, developerMessage, attribute, value);
  }

  /**
   * Fetches the localized message template and plugs in the correct parameters
   * to set the business error message.
   * 
   */
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.attributeValueCannotBeNegativeException(this.getLocale(), this.getAttribute(), this.getValue());
  }
}
