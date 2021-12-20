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
package com.runwaysdk.dataaccess.io.instance;

import java.lang.Thread.UncaughtExceptionHandler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;

public class EntityValueHandler extends ValueHandler
{
  /**
   * The Attribute being imported
   */
  private Attribute attribute;

  /**
   * Constructor
   * 
   * @param reader
   *          The XMLReader which reads the XML document
   * @param previousHandler
   *          The handler which passed control
   * @param manager
   *          ImportManager which provides communication between handlers for a
   *          single import
   * @param attributes
   *          The attributes defined in the XML tag
   * @param entity
   *          TODO
   * @param struct
   *          The EntityDAO which owns the attribute being imported
   * @throws SAXException
   */
  public EntityValueHandler(XMLReader reader, XMLHandler previousHandler, ImportManager manager, Attributes attributes, EntityDAO entity, UncaughtExceptionHandler exceptionHandler) throws SAXException
  {
    super(reader, previousHandler, manager, exceptionHandler);

    String attributeName = attributes.getValue(XMLTags.ATTRIBUTE_TAG);

    this.attribute = entity.getAttribute(attributeName);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.io.instance.ValueHandler#setValue(java.lang.String
   * )
   */
  protected void setValue(final String value)
  {
    attribute.importValue(value);
  }
}
