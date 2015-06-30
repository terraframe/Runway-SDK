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
package com.runwaysdk.dataaccess.transaction;

import com.runwaysdk.AttributeNotification;
import com.runwaysdk.business.Entity;
import com.runwaysdk.business.View;
import com.runwaysdk.session.Session;

public class AttributeNotificationMap
{

  private Entity entity;

  private String entityAttributeName;

  private View   view;

  private String viewAttributeName;

  public AttributeNotificationMap(Entity entity, String entityAttributeName, View view, String viewAttributeName)
  {
    this.entity = entity;
    this.entityAttributeName = entityAttributeName;
    this.view = view;
    this.viewAttributeName = viewAttributeName;
  }

  public String getMapKey()
  {
    return getMapKey(this.entity.getId(), this.entityAttributeName);
  }

  public static String getMapKey(String componentId, String attributeName)
  {
    return componentId+"-"+attributeName;
  }


  /**
   * Returns the id of the component that defines the attribute.
   * @return id of the component that defines the attribute.
   */
  public String getEntityId()
  {
    return this.entity.getId();
  }

  public Entity getEntity()
  {
    return entity;
  }

  public String getEntityAttributeName()
  {
    return entityAttributeName;
  }

  public View getView()
  {
    return view;
  }

  public String getViewAttributeName()
  {
    return viewAttributeName;
  }


  /**
   * Converts the metadata on the given {@link AttributeNotification}
   * @param attributeNotification
   */
  public void convertAttributeNotification(AttributeNotification attributeNotification)
  {
    attributeNotification.setComponentId(this.view.getId());

    attributeNotification.setDefiningType(this.view.getType());
    attributeNotification.setDefiningTypeDisplayLabel(this.view.getClassDisplayLabel());

    attributeNotification.setAttributeName(this.viewAttributeName);

    String attrDisplayLabel =  this.view.getMdAttributeDAO(this.viewAttributeName).getDisplayLabel(Session.getCurrentLocale());
    attributeNotification.setAttributeDisplayLabel(attrDisplayLabel);
  }

}
