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
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.HashMap;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.io.MarkupWriter;

public class DeleteVisitor extends MarkupVisitor
{
  /**
   * Writes the XML code
   */
  protected MarkupWriter                      writer;
  
  public DeleteVisitor(MarkupWriter writer)
  {
    this.writer = writer;
  }
  
  public void visit(ComponentIF component)
  {
    if(component instanceof BusinessDAOIF)
    {
      BusinessDAOIF businessDAO = (BusinessDAOIF) component;
      MdBusinessDAOIF mdBusiness = businessDAO.getMdBusinessDAO();
      
      visitEntity(businessDAO, mdBusiness, XMLTags.OBJECT_TAG);
    }
    else if(component instanceof RelationshipDAOIF)
    {
      RelationshipDAOIF relationshipDAO = (RelationshipDAOIF) component;
      MdRelationshipDAOIF mdRelationship = relationshipDAO.getMdRelationshipDAO();
      
      visitEntity(relationshipDAO, mdRelationship, XMLTags.RELATIONSHIP_TAG);
    }
  }
  
  private void visitEntity(EntityDAOIF entityDAO, MdEntityDAOIF mdEntityIF, String tag)
  {
    HashMap<String, String> attributes = new HashMap<String, String>();
    attributes.put(XMLTags.TYPE_ATTRIBUTE, mdEntityIF.definesType());
    attributes.put(XMLTags.KEY_ATTRIBUTE, entityDAO.getKey());

    writer.writeEmptyEscapedTag(tag, attributes);   
  }  

}
