/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
 * This file is part of Runway SDK(tm).
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.manager.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.runwaysdk.dataaccess.transaction.TransactionExportManager;
import com.runwaysdk.manager.controller.IExportStrategy;
import com.runwaysdk.manager.general.Localizer;

public class ExportBean
{
  private Long                  lower;

  private String                type;

  private String                location;

  private PropertyChangeSupport propertyChangeSupport;

  public ExportBean()
  {
    this.type = "ALL";
    this.lower = 0L;
    this.location = "C:\\Backup\\" + System.currentTimeMillis() + ".zip";

    this.propertyChangeSupport = new PropertyChangeSupport(this);
  }

  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
  {
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener)
  {
    propertyChangeSupport.removePropertyChangeListener(listener);
  }

  public Long getLower()
  {
    return lower;
  }

  public void setLower(Long lower)
  {
    propertyChangeSupport.firePropertyChange("lower", this.lower, this.lower = lower);
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    propertyChangeSupport.firePropertyChange("type", this.type, this.type = type);
  }

  public String getLocation()
  {
    return location;
  }

  public void setLocation(String location)
  {
    propertyChangeSupport.firePropertyChange("location", this.location, this.location = location);
  }

  public IExportStrategy getExportStrategy()
  {
    IExportStrategy strategey = new IExportStrategy()
    {
      @Override
      public void execute(TransactionExportManager manager)
      {
        manager.export(0L);
      }
    };

    if (type.equals("RANGE"))
    {
      strategey = new IExportStrategy()
      {
        @Override
        public void execute(TransactionExportManager manager)
        {
          manager.export(lower);
        }
      };
    }
    else if (type.equals("NOT_IMPORTED"))
    {
      strategey = new IExportStrategy()
      {
        @Override
        public void execute(TransactionExportManager manager)
        {
          manager.export();
        }
      };
    }
    return strategey;
  }

  public void validate()
  {
    if (location == null)
    {
      String msg = Localizer.getMessage("MISSING_EXPORT_LOCATION");

      throw new RuntimeException(msg);
    }

    if (type.equals("RANGE"))
    {
      if (lower == null)
      {
        String msg = Localizer.getMessage("MISSING_START_SEQ");

        throw new RuntimeException(msg);
      }
    }
  }

}
