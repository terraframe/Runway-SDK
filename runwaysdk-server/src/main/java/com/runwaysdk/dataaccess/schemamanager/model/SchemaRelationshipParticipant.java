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
package com.runwaysdk.dataaccess.schemamanager.model;

import org.xml.sax.Attributes;

public class SchemaRelationshipParticipant extends KeyedElement
{

  private String        type;

  public SchemaRelationshipParticipant(Attributes attributes, String tag, String type, SchemaRelationship relationship)
  {
    super(attributes, tag, relationship);
    
    this.type = type;
  }
  
  @Override
  public String toString()
  {
    return this.getKey();
  }
  
  public <T> T accept(SchemaVisitor<T> v)
  {
    return v.visit(this);
  }

  public String getType()
  {
    return type;
  }

  @Override
  public SchemaElement getParent()
  {
    return (SchemaRelationship) super.getParent();
  }
  
  public String getKey()
  {
    return this.accept(new KeyResolver());
  }

}
