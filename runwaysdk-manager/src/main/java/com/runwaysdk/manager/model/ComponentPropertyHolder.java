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
package com.runwaysdk.manager.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import com.runwaysdk.dataaccess.EnumerationItemDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.manager.general.Localizer;
import com.runwaysdk.manager.model.object.PersistanceFacade;

public abstract class ComponentPropertyHolder implements IComponentObject
{
  private PropertyChangeSupport propertyChangeSupport;

  public ComponentPropertyHolder()
  {
    this.propertyChangeSupport = new PropertyChangeSupport(this);
  }

  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
  {
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
  {
    propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
  }

  protected void firePropertyChange(String propertyName, Object oldValue, Object newValue)
  {
    propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
  }

  @Override
  public void propertyChange(PropertyChangeEvent arg0)
  {
  }

  public List<MdAttributeDAOIF> definesMdAttributes()
  {
    MdEntityDAOIF mdEntity = this.getMdClassDAO();

    return PersistanceFacade.definesMdAttributes(mdEntity);
  }

  public String getInfo()
  {
    StringBuffer buffer = new StringBuffer();

    List<MdAttributeDAOIF> attributes = this.definesMdAttributes();

    for (MdAttributeDAOIF mdAttribute : attributes)
    {
      String attributeName = mdAttribute.definesAttribute();

      if (mdAttribute instanceof MdAttributeLocalDAOIF)
      {
        buffer.append(attributeName + " : " + this.getStructValue(attributeName, Localizer.DEFAULT_LOCALE) + "\n");
      }
      else if (mdAttribute instanceof MdAttributeEnumerationDAOIF)
      {
        StringBuilder builder = new StringBuilder();

        List<EnumerationItemDAOIF> items = this.getItems(attributeName);

        builder.append("[");

        if (items != null)
        {
          for (EnumerationItemDAOIF item : items)
          {
            builder.append(", " + item.getName());
          }
        }

        builder.append("]");

        buffer.append(attributeName + " : " + builder.toString().replaceFirst(", ", "") + "\n");
      }
      else
      {
        buffer.append(attributeName + " : " + this.getValue(attributeName) + "\n");
      }
    }

    return buffer.toString();
  }

  public void copyAttributes(IComponentObject entity)
  {
    List<MdAttributeDAOIF> attributes = this.definesMdAttributes();

    CopyVisitor visitor = new CopyVisitor(this, entity);

    for (MdAttributeDAOIF attribute : attributes)
    {
      attribute.accept(visitor);
    }
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof ComponentPropertyHolder)
    {
      ComponentPropertyHolder propertyHolder = (ComponentPropertyHolder) obj;

      if (propertyHolder.getType().equals(this.getType()))
      {
        EqualVisitor visitor = new EqualVisitor(this, propertyHolder);

        List<MdAttributeDAOIF> mdAttributes = this.definesMdAttributes();

        for (MdAttributeDAOIF mdAttribute : mdAttributes)
        {
          mdAttribute.accept(visitor);
        }

        return visitor.isEqual();
      }
    }

    return false;
  }
}
