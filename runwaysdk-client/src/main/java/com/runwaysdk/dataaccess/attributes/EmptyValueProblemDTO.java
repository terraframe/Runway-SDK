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

import com.runwaysdk.business.AttributeProblemDTO;
import com.runwaysdk.constants.EmptyValueProblemDTOInfo;

public class EmptyValueProblemDTO extends AttributeProblemDTO
{
  public static final String CLASS = EmptyValueProblemDTOInfo.CLASS;
  
  /**
   * 
   */
  private static final long serialVersionUID = -2077450290896939118L;

  /**
   * Constructs a new EmptyValueExceptionDTO with the specified localized message from the server. 
   * 
   * @param type of the runway exception.
   * @param componentId oid of the containing component.
   * @param definingType type of the containing component.
   * @param definingTypeDisplayLabel display label of the type of the containing component.
   * @param attributeName name of the attribute.
   * @param attributeDisplayLabel display label of the attribute.
   * @param localizedMessage end user error message.
   */
  public EmptyValueProblemDTO(String type, String componentId, 
      String definingType, String definingTypeDisplayLabel,
      String attributeName, String attributeId, String attributeDisplayLabel,
      String localizedMessage)
  {
    super(type, componentId, 
        definingType, definingTypeDisplayLabel,
        attributeName, attributeId, attributeDisplayLabel,
        localizedMessage);
  }
}
