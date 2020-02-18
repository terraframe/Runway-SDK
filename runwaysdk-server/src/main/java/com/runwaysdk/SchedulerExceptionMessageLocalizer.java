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
package com.runwaysdk;

import java.util.Locale;

import com.runwaysdk.constants.CommonProperties;


/**
 * Provides typesafe getter for access to localized business-layer error
 * messages.
 * 
 * @author Richard Rowlands
 */
public class SchedulerExceptionMessageLocalizer
{
  public static String schedulerJobCannotResumeException(Locale locale)
  {
    return LocalizationFacade.getMessage(CommonProperties.getDefaultLocale(), "SchedulerJobCannotResume", "The server was shutdown while the job was running.");
  }

  public static String schedulerMisfireException(Locale locale)
  {
    return LocalizationFacade.getMessage(CommonProperties.getDefaultLocale(), "SchedulerJobMisfire", "The job could not be scheduled as requested, the system is out of resources (scheduler misfire).");
  }
}
