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
package com.runwaysdk.transport.conversion;

import com.runwaysdk.RunwayProblem;
import com.runwaysdk.business.RunwayProblemDTO;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.attributes.AttributeProblem;
import com.runwaysdk.dataaccess.io.excel.ExcelProblem;

public abstract class RunwayProblemToRunwayProblemDTO
{ 
  private RunwayProblem frameworkProblem;
  
  protected RunwayProblemToRunwayProblemDTO(RunwayProblem frameworkProblem)
  {
    this.frameworkProblem = frameworkProblem;
  }

  protected RunwayProblem getRunwayProblem()
  {
    return this.frameworkProblem;
  }

  public abstract RunwayProblemDTO populate();
  
  public static RunwayProblemToRunwayProblemDTO getConverter(RunwayProblem frameworkProblem)
  {
    if (frameworkProblem instanceof AttributeProblem)
    {
      return new AttributeProblemToAttributeProblemDTO((AttributeProblem)frameworkProblem);
    }
    else if (frameworkProblem instanceof ExcelProblem)
    {
      return new ExcelProblemConverter((ExcelProblem)frameworkProblem);
    }
    else
    {
      String message = "No DTO converter found for Problem type ["
          + frameworkProblem.getClass().getName() + "]";
      throw new ProgrammingErrorException(message);
    }
  }
}
