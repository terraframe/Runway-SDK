/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.dataaccess.schemamanager.xml;

import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSType;

/**
 * A visitor that, given a name of an xsd element, finds its type name. For
 * example, given the element name mdProblem, it will find its type mdException,
 * from the xsd file. The traversal makes sure that all the element
 * declarations, including the nested ones are covered.
 * 
 * @author Aritra
 * 
 */
public class XSDElementFinder extends DefaultXSVisitor
{

  private String        searchName;

  private XSElementDecl searchElement;

  public XSDElementFinder(String searchName)
  {
    this.searchName = searchName;
  }

  public void schema(XSSchema schema)
  {
    this.searchElement = schema.getElementDecl(this.searchName);
  }

  @Override
  public void elementDecl(XSElementDecl element)
  {
    String elementName = element.getName();

    // If the current element has the name being searched store its type
    if (elementName.equals(this.searchName))
    {
      this.searchElement = element;

      // If the element does not refer to a type then return its content type
      // (Example create element)

    }

    // otherwise dig the content of the elements type
    else if (! ( elementName.equals(XMLTags.FIRST_CONDITION_TAG) || elementName.equals(XMLTags.SECOND_CONDITION_TAG) ))
    {
      XSType elementType = element.getType();
      XSComplexType elementComplexType = elementType.asComplexType();
      // if the type of the element is a complex type
      // visit whatever it has in its content.
      if (elementComplexType != null)
      {
        elementComplexType.getContentType().visit(this);
      }
    }
  }

  public XSElementDecl getElementDecl()
  {
    return searchElement;
  }

  @Override
  public void complexType(XSComplexType complexType)
  {
    complexType.getContentType().visit(this);
  }

  public void modelGroup(XSModelGroup group)
  {
    for (XSParticle particle : group.getChildren())
    {
      particle.visit(this);
    }
  }

  public void particle(XSParticle particle)
  {
    particle.getTerm().visit(this);
  }

}
