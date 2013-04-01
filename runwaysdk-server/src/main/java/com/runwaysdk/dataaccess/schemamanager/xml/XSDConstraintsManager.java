/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.schemamanager.xml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.SAXException;

import com.runwaysdk.dataaccess.schemamanager.model.SchemaElementIF;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSType;
import com.sun.xml.xsom.parser.XSOMParser;

/**
 * Manages the child sequencing constraints associated with different complex
 * types in the xml schemas Manages the child sequencing constraints associated
 * with different complex types in the xml schemas
 * 
 * @author Aritra
 * 
 */
public class XSDConstraintsManager
{

  private Map<XSType, ContentPriorityIF> tagToConstraints;

  private XSSchemaSet                    schemaSet;

  public XSDConstraintsManager(String xsdLocation)
  {
    tagToConstraints = new HashMap<XSType, ContentPriorityIF>();
    try
    {
      XSOMParser parser = new XSOMParser();
      parser.parse(new File(xsdLocation));
      schemaSet = parser.getResult();
    }
    catch (SAXException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  public ContentPriorityIF getConstraints(SchemaElementIF element)
  {
    XSType type = element.getXSType(schemaSet);

    if (tagToConstraints.containsKey(type))
    {
      return tagToConstraints.get(type);
    }
    else
    {
      XSDConstraintsMiner constraintsMiner = new XSDConstraintsMiner();
      type.visit(constraintsMiner);
      ContentPriorityIF contentPriority = constraintsMiner.getContentPriority();
      return contentPriority;
    }

  }

  /*
   * 
   * private ContentPriorityIF getConstraints(String tag) { if
   * (tagToConstraints.containsKey(tag)) { return tagToConstraints.get(tag); }
   * else { XSDElementFinder typeFinder = new XSDElementFinder(tag);
   * 
   * for (XSSchema schema : schemaSet.getSchemas()) { schema.visit(typeFinder);
   * }
   * 
   * XSType searchType = typeFinder.getType(); if (searchType != null) {
   * System.out.println("Type found for " + tag + " is " + searchType);
   * XSDConstraintsMiner constraintsMiner = new XSDConstraintsMiner();
   * searchType.visit(constraintsMiner); ContentPriorityIF contentPriority =
   * constraintsMiner.getContentPriority(); if (contentPriority != null) return
   * contentPriority; } throw new XSDDefinitionNotResolvedException(tag,
   * "The definition of the xml element "
   * +tag+" could not be resolved in the xsd file"); } }
   * 
   * 
   * 
   * 
   * public String getTypeName(String elementName) { XSDElementFinder typeFinder
   * = new XSDElementFinder(elementName); for (XSSchema schema :
   * schemaSet.getSchemas()) { typeFinder.schema(schema); }
   * 
   * return typeFinder.getType().toString(); }
   * 
   * public XMLElementSorter<SchemaElement> childComparator(String tag) { return
   * new XMLChildrenSorter(getConstraints(tag)); }
   * 
   * public static void main(String[] args) {
   * 
   * XSDConstraintsManager manager = new XSDConstraintsManager(
   * "C:/Users/runway/workspace/runway/mergeTestFIles/version_gis.xsd");
   * 
   * XMLElementSorter<SchemaElement> childComparator =
   * manager.childComparator("mdControllerOptions"); Map<String, Integer>
   * childPriorityMap = childComparator.priorities().childPriorityMap(); for
   * (String elementName : childPriorityMap.keySet()) {
   * System.out.println(elementName + " has the priority " +
   * childPriorityMap.get(elementName)); }
   * 
   * }
   */

  public XMLElementSorter<SchemaElementIF> childComparator(SchemaElementIF element)
  {
    return new XMLChildrenSorter(getConstraints(element));
  }

}
