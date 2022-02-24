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

/**
 * The elements in the XML schema that will not be rendered in the visualization
 * 
 * @author Aritra
 * 
 */
public class KeyedElement extends SchemaElement implements KeyedElementIF
{
  protected boolean merged;

  public KeyedElement(Attributes attributes, String tag)
  {
    super(attributes, tag);
  }

  public KeyedElement(Attributes attributes, String tag, SchemaElementIF parent)
  {
    super(attributes, tag, parent);

    this.merged = false;
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
  
  public boolean isMerged()
  {
    return merged;
  }

  public void setMerged(boolean merged)
  {
    this.merged = merged;
  }

  public static KeyedElement newInstance(Attributes attributes, String tag, SchemaElementIF parent)
  {
    return (KeyedElement) SMFactory.newInstance(attributes, tag, parent);
  }
  
  public void remove()
  {
    this.fireEvent(new DeleteEvent(this));

    super.remove();
  }
}
