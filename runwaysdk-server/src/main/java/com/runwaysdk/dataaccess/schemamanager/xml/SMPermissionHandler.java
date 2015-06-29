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
package com.runwaysdk.dataaccess.schemamanager.xml;

import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.runwaysdk.dataaccess.schemamanager.model.MergeSchema;

public class SMPermissionHandler extends AbstractSchemaHandler
{
  
  public SMPermissionHandler(XMLReader reader, DefaultHandler parentHandler, MergeSchema schema)
  {
    super(schema, reader, parentHandler, null, null);
    schema.enterPermissionsState();
  }

  @Override
  protected void dispatchChildHandler(Attributes attributes, String tag)
  {
    if (SMXMLTags.isPermissionHolderTag(tag))
    {
      childHandler = new PermissionHolderHandler(attributes, reader, this, schema, tag);
      reader.setContentHandler(childHandler);
      reader.setErrorHandler(childHandler);
    }
  }

  @Override
  protected void initRootElement(Attributes attributes, String localName)
  {

  }

  @Override
  protected void obtainChildElement()
  {
    // TODO Auto-generated method stub

  }
  
  @Override
  protected String terminalTag()
  {
    return XMLTags.PERMISSIONS_TAG; 
    
  }

}
