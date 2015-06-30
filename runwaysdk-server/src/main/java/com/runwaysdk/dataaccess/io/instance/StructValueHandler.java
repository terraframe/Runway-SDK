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
package com.runwaysdk.dataaccess.io.instance;

import java.lang.Thread.UncaughtExceptionHandler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeStruct;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;

/**
 * Parses instance_value tags to instiante an Attribute with a value. Makes use
 * of the tempelate pattern to deal with differences when the Attribute belongs
 * to an EntityDAO and an AttributeStruct. This class control behavior when the
 * Attribute belongs to an AttributeStruct.
 * 
 * @author Justin Smethie
 * @date 06/09/06
 */
public class StructValueHandler extends ValueHandler
{
  /**
   * The name of the Attribute being imported
   */
  private String          attributeName;

  /**
   * The AttributeStruct which owns the Attribute
   */
  private AttributeStruct attributeStruct;

  /**
   * Constructor
   * 
   * @param reader
   *          The XMLReader which reads the XML document
   * @param previousHandler
   *          The handler which passed control
   * @param manager
   *          ImportManager which provides communication between handlers for a
   *          single impot
   * @param attributeStruct
   *          The AttributeStruct which owns the attribute being imported
   * @param attributes
   *          The attributes defined in the XML tag
   * @param exceptionHandler TODO
   * @throws SAXException
   */
  public StructValueHandler(XMLReader reader, XMLHandler previousHandler, ImportManager manager, AttributeStruct attributeStruct, Attributes attributes, UncaughtExceptionHandler exceptionHandler) throws SAXException
  {
    super(reader, previousHandler, manager, exceptionHandler);

    this.attributeName = attributes.getValue(XMLTags.ATTRIBUTE_TAG);
    this.attributeStruct = attributeStruct;
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
    StructDAO structDAO = attributeStruct.getStructDAO();
    Attribute attribute = structDAO.getAttribute(attributeName);

    attribute.importValue(value);
  }
}
