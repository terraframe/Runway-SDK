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
package com.runwaysdk.dataaccess.schemamanager;

import java.util.Map;

import junit.framework.TestCase;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.runwaysdk.dataaccess.schemamanager.model.KeyedElement;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaClass;
import com.runwaysdk.dataaccess.schemamanager.xml.CreateElement;
import com.runwaysdk.dataaccess.schemamanager.xml.XSDConstraintsManager;

public class TestXSDConstraints extends TestCase
{

  private XSDConstraintsManager constraintsManager = new XSDConstraintsManager(
                                                       "C:/Users/runway/workspace/runway/mergeTestFIles/version_gis.xsd");

  public void testMdBusinessOptions()
  {
    Attributes mdBusinessAattributes = new AttributesImpl();
    Attributes mdBusinessOptionAttributes = new AttributesImpl();
    SchemaClass mdBusinessElement = new SchemaClass(mdBusinessAattributes, XMLTags.MD_BUSINESS_TAG);
    KeyedElement element = new KeyedElement(mdBusinessOptionAttributes, XMLTags.CREATE_TAG,
        mdBusinessElement);
    mdBusinessElement.addChild(element);
    Map<String, Integer> childPriorityMap = constraintsManager.getConstraints(element)
        .childPriorityMap();
    assertTrue(childPriorityMap.get("attributes") == 0);
    assertTrue(childPriorityMap.get("mdMethod") == 1);
    assertTrue(childPriorityMap.get("mdStateMachine") == 2);
  }

  public void testMdControllerOptions()
  {
    Attributes mdControllerAattributes = new AttributesImpl();
    Attributes mdControllerOptionAttributes = new AttributesImpl();
    SchemaClass mdControllerElement = new SchemaClass(mdControllerAattributes, XMLTags.MD_CONTROLLER_TAG);
    KeyedElement element = new KeyedElement(mdControllerOptionAttributes, XMLTags.CREATE_TAG,
        mdControllerElement);
    mdControllerElement.addChild(element);
    Map<String, Integer> childPriorityMap = constraintsManager.getConstraints(element)
        .childPriorityMap();
    System.out.println(childPriorityMap.toString());
    assertTrue(childPriorityMap.get("mdAction") == 0);
  }

  public void testCreateElementChildren()
  {
    CreateElement createElement = new CreateElement();
    Map<String, Integer> childPriorityMap = constraintsManager.getConstraints(createElement)
        .childPriorityMap();
    System.out.println(childPriorityMap.toString());
    assertTrue(childPriorityMap.get("mdBusiness") == 6);
    assertTrue(childPriorityMap.get("relationship") == 2);
    assertTrue(childPriorityMap.get("mdController") == 17);
  }

  public void testUpdateElementChildren()
  {

  }
}
