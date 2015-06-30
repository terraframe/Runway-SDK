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
package com.runwaysdk.dataaccess.schemamanager.xml;

import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSType;

/**
 * 
 * Derives the sequencing constraints on the child elements in the xsd schema
 * 
 * @author Aritra
 * 
 */
public class XSDConstraintsMiner extends DefaultXSVisitor
{
  private ContentPriorityIF contentPriority;

  public XSDConstraintsMiner()
  {
    this.contentPriority = new SequentialXMLContentPriority();
  }

  public XSDConstraintsMiner(ContentPriorityIF contentPriority)
  {
    this.contentPriority = contentPriority;
  }

  public void complexType(XSComplexType type)
  {
    // if there exists a search tag it must match with the name given to search
    // check if complex type extends other type
    if (type.getDerivationMethod() == XSType.EXTENSION)
    {
      XSType parentType = type.getBaseType();
      XSDConstraintsMiner parentConstraintsMiner = new XSDConstraintsMiner(this.contentPriority);
      parentType.visit(parentConstraintsMiner);
    }

    XSContentType contentType = type.getContentType();
    contentType.visit(new XSDConstraintsMiner(this.contentPriority));
  }

  public void elementDecl(XSElementDecl decl)
  {
    contentPriority.addChildTag(decl.getName());
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

  public ContentPriorityIF getContentPriority()
  {
    return contentPriority;
  }

}
