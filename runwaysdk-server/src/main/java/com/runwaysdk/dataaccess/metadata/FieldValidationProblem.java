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

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeProblem;

public class FieldValidationProblem extends AttributeProblem
{
  private String condition;

  /**
   * Constructs a new EmptyValueException with the specified developer message.
   * 
   * @param componentId
   *          id of the containing component.
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
   *          The attribute with no value
   * @param value
   *          The value given for the attribute
   */
  public FieldValidationProblem(String componentId, MdClassDAOIF mdClassIF, MdAttributeDAOIF mdAttributeIF, String developerMessage)
  {
    super(componentId, mdClassIF, mdAttributeIF, developerMessage);
  }

  public String getCondition()
  {
    return condition;
  }

  public void setCondition(String condition)
  {
    this.condition = condition;
  }

  /**
   * Fetches the localized message template and plugs in the correct parameters
   * to set the business error message.
   * 
   * @param locale
   *          The locale of the current session
   */
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.fieldValidationProblem(this.getLocale(), this.getAttributeDisplayLabel(), condition);
  }

}
