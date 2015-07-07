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
package com.runwaysdk.system.web;

import com.runwaysdk.AttributeNotification;
import com.runwaysdk.business.Entity;
import com.runwaysdk.business.View;
import com.runwaysdk.session.Session;

public abstract class AttributeNotificationProblem extends AttributeNotificationProblemBase implements AttributeNotification
{
  private static final long serialVersionUID = -1879040227;
  
  public AttributeNotificationProblem()
  {
    super();
  }
  
  public AttributeNotificationProblem(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public void setNotification(Entity entity, String attributeName)
  {
    this.setComponentId(entity.getId());
    this.setAttributeName(attributeName);
    this.setAttributeDisplayLabel(entity.getMdAttributeDAO(attributeName).getDisplayLabel(Session.getCurrentLocale()));
    this.setDefiningType(entity.getType());
    this.setDefiningTypeDisplayLabel(entity.getClassDisplayLabel());
  }

  public void setNotification(View view, String attributeName)
  {
    this.setComponentId(view.getId());
    this.setAttributeName(attributeName);
    this.setAttributeDisplayLabel(view.getMdAttributeDAO(attributeName).getDisplayLabel(Session.getCurrentLocale()));
    this.setDefiningType(view.getType());
    this.setDefiningTypeDisplayLabel(view.getClassDisplayLabel());
  }
  
}
