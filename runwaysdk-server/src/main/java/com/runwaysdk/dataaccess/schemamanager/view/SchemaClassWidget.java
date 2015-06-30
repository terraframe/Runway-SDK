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
package com.runwaysdk.dataaccess.schemamanager.view;


import java.awt.Color;

import javax.swing.JTextArea;

import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.ComponentWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.LabelWidget.Alignment;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

import com.runwaysdk.dataaccess.schemamanager.model.SchemaClass;

public class SchemaClassWidget extends Widget
{
  
  private JTextArea attributesField;
  private JTextArea methodsField;
  
  public SchemaClassWidget(Scene scene, SchemaClass mdClass)
  {
    super(scene);
    attributesField = new JTextArea();
    methodsField = new JTextArea();
    setLayout(LayoutFactory.createVerticalFlowLayout());
    setBorder(BorderFactory.createLineBorder());
    LabelWidget nameLabel = createLabelCompartment(scene, mdClass.getXMLAttributeValue("name"), headerColor());
    addChild(nameLabel);
    refreshAttributes(mdClass);
    refreshMethods(mdClass);
    LabelWidget attributesLabel = createLabelCompartment(scene, "Attributes", Color.LIGHT_GRAY);
    addChild(attributesLabel);
    addChild(createTextCompartment(scene, buildAttributeString(mdClass), attributesField));
    LabelWidget methodLabel = createLabelCompartment(scene, "Methods", Color.LIGHT_GRAY);
    addChild(methodLabel);
    addChild(createTextCompartment(scene, buildMethodString(mdClass) , methodsField));
    getActions().addAction(ActionFactory.createMoveAction());
    
  }
  
  public Color headerColor() {
    return Color.YELLOW;
  }
  
  public void refreshAttributes(SchemaClass mdClass) {
    attributesField.setText(buildAttributeString(mdClass));
  }
  
  public void refreshMethods(SchemaClass mdClass) {
    methodsField.setText(buildMethodString(mdClass));
  }
  
  private String buildAttributeString(SchemaClass mdClass) {
    String attributeString = "";
//    for (SchemaAttribute attribute : mdClass.attributes()) {
//      attributeString += attribute.getXMLAttributeValue(XMLTags.NAME_ATTRIBUTE)+"\n";
//    }
    return attributeString;
  }
  
  private String buildMethodString(SchemaClass mdClass) {
    String methodString = "";
//    for (SchemaMethod method : mdClass.methods()) {
//      methodString += method.getXMLAttributeValue(XMLTags.NAME_ATTRIBUTE)+"\n";
//    }
    return methodString;
  }
  
  private LabelWidget createLabelCompartment(Scene scene, String displayString, Color bkColor) {
    LabelWidget compartment = new LabelWidget(scene, displayString);
    compartment.setOpaque(true);
    compartment.setAlignment(Alignment.CENTER);
    compartment.setBackground(bkColor);
    return compartment;
  }
  
  private ComponentWidget createTextCompartment(Scene scene, String displayText, JTextArea textArea) {
    textArea.setText(displayText);
    ComponentWidget compartment = new ComponentWidget(scene, textArea);
    return compartment;
  }

}
