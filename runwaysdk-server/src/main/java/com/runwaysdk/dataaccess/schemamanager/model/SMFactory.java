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
package com.runwaysdk.dataaccess.schemamanager.model;

import org.xml.sax.Attributes;

import com.runwaysdk.dataaccess.schemamanager.xml.SMXMLTags;

public class SMFactory
{
  public static SchemaElementIF newInstance(Attributes attributes, String tag, SchemaElementIF parent)
  {
    if (parent.getTag().equals(SMXMLTags.ATTRIBUTES_TAG))
    {
      if (tag.equals(SMXMLTags.VIRTUAL_TAG))
      {
        return new SchemaVirtualAttribute(attributes, tag, parent);
      }
      else
      {
        return new SchemaAttribute(attributes, tag, parent);
      }
    }
    else if (parent.getTag().equals(SMXMLTags.FIELDS_TAG))
    {
      return new SchemaField(attributes, tag, parent);
    }
    else if (SMXMLTags.isClassTag(tag))
    {
      return new SchemaClass(attributes, tag);
    }

    return new KeyedElement(attributes, tag, parent);
  }
}
