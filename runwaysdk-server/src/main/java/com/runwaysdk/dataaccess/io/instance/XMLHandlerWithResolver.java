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
package com.runwaysdk.dataaccess.io.instance;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.StreamSource;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.resolver.IConflictResolver;

public abstract class XMLHandlerWithResolver extends XMLHandler
{
  private IConflictResolver resolver;

  public XMLHandlerWithResolver(XMLReader reader, XMLHandler previousHandler, ImportManager manager, IConflictResolver resolver) throws SAXException
  {
    super(reader, previousHandler, manager);

    this.resolver = resolver;
  }

  public XMLHandlerWithResolver(StreamSource source, String schemaLocation, IConflictResolver resolver) throws SAXException
  {
    super(source, schemaLocation);

    this.resolver = resolver;
  }

  public IConflictResolver getResolver()
  {
    return resolver;
  }
}
