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
package com.runwaysdk.system.scheduler;

import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.ProgrammingErrorException;

public class QualifiedTypeJob extends QualifiedTypeJobBase
{
  private static final long serialVersionUID = 1991343666;

  private ExecutableJobIF   executableJobIF;

  public QualifiedTypeJob()
  {
    super();
  }

  @Override
  public void execute(ExecutionContext executionContext)
  {
    String className = this.getClassName();

    try
    {
      // TODO pass in *this* CustomJob or use a Proxy to treat the object as a
      // JobIF
      Class<? extends ExecutableJobIF> clazz;
      try
      {
        clazz = Class.forName(className).asSubclass(ExecutableJobIF.class);
        this.executableJobIF = clazz.newInstance();
      }
      catch (ClassNotFoundException e)
      {
        throw new ProgrammingErrorException("Could not find Job class [" + className + "]", e);
      }
      catch (IllegalAccessException e)
      {
        throw new ProgrammingErrorException("Could not access Job class [" + className + "]", e);
      }
      catch (InstantiationException e)
      {
        throw new ProgrammingErrorException("Could not instantiate Job class [" + className + "]", e);
      }

      this.executableJobIF.execute(executionContext);
    }
    catch (Throwable e)
    {
      e.printStackTrace();
      throw new ProgrammingErrorException("The CustomJob with class [" + className + "] could not be initialized.", e);
    }
  }

  public static QualifiedTypeJob newInstance(Class<? extends ExecutableJobIF> ej)
  {
    QualifiedTypeJob job = new QualifiedTypeJob();
    job.setClassName(ej.getName());
    job.getDisplayLabel().setValue(ej.getName());
    job.setStructValue(ExecutableJob.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, ej.getName());

    return job;
  }
}
