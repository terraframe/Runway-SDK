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
package com.runwaysdk.dataaccess.schemamanager.view;

import java.awt.Color;

import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.layout.LayoutFactory.ConnectionWidgetLayoutAlignment;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;

import com.runwaysdk.dataaccess.schemamanager.model.SchemaRelationship;

public class SchemaRelationshipWidget extends ConnectionWidget
{
  public SchemaRelationshipWidget(Scene scene, SchemaRelationship relationship) {
    super(scene);
    setLineColor(Color.BLUE);
    LabelWidget relationshipLabel = new LabelWidget(scene, relationship.renderedName());
    relationshipLabel.setOpaque(true);
    relationshipLabel.getActions().addAction(ActionFactory.createMoveAction());
    addChild(relationshipLabel);
    setConstraint(relationshipLabel, ConnectionWidgetLayoutAlignment.CENTER_RIGHT, 0.5f);
  }

}
