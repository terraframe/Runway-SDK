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
package com.runwaysdk.manager.model.databinding;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.value.SimpleValueProperty;
import org.eclipse.core.internal.databinding.property.value.SimplePropertyObservableValue;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.manager.model.IComponentObject;


public class RunwayObservables
{

  public static IObservableValue observeValue(Object source, String attribute)
  {
    return RunwayObservables.observeValue(Realm.getDefault(), source, attribute);
  }

  public static IObservableValue observeValue(Realm realm, Object source, String propertyName)
  {
    PropertyVisitor visitor = new PropertyVisitor(propertyName);
    
    IComponentObject entity = (IComponentObject) source;
    
    MdAttributeDAOIF mdAttribute = entity.getMdAttributeDAO(propertyName);

    mdAttribute.accept(visitor);
        
    SimpleValueProperty property = visitor.getProperty();

    return new SimplePropertyObservableValue(realm, source, property);
  }  
}
