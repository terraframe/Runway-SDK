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

import com.runwaysdk.manager.general.Localizer;

public class ImportBean
{
  private String                location;

  private PropertyChangeSupport propertyChangeSupport;

  public ImportBean()
  {
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

  public String getLocation()
  {
    return location;
  }

  public void setLocation(String location)
  {
    propertyChangeSupport.firePropertyChange("location", this.location, this.location = location);
  }

  public void validate()
  {
    if (location == null)
    {
      String msg = Localizer.getMessage("MISSING_IMPORT_LOCATION");
      throw new RuntimeException(msg);
    }
  }

}
