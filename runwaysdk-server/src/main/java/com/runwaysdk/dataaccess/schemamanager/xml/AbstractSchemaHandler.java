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
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.runwaysdk.dataaccess.schemamanager.model.MergeSchema;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaElement;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaElementIF;

public abstract class AbstractSchemaHandler extends DefaultHandler
{

  /**
   * The handler this handler deletgates to
   */
  protected AbstractSchemaHandler childHandler;

  /**
   * The handler from which the present handler obtained the control
   */
  protected DefaultHandler        parentHandler;

  /**
   * The schema being built by merging the individual schemas
   */
  protected MergeSchema          schema;

  /**
   * The XMLReader that is parsing the schemas
   */
  protected XMLReader             reader;

  /**
   * The element that this handler builds
   */
  protected SchemaElementIF       root;
  
  protected AbstractSchemaHandler()
  {

  }

  public AbstractSchemaHandler(MergeSchema schema, XMLReader reader, DefaultHandler parentHandler, String tag, Attributes attributes)
  {
    this.parentHandler = parentHandler;
    this.schema = schema;
    this.reader = reader;
    
    initRootElement(attributes, tag);
  }

  protected AbstractSchemaHandler(MergeSchema schema, XMLReader reader, DefaultHandler parentHandler, SchemaElementIF rootElement)
  {
    this.parentHandler = parentHandler;
    this.schema = schema;
    this.reader = reader;
    this.root = rootElement;
  }

  /**
   * A protected constructor that does not initialize the root element
   * 
   * @param schema
   * @param reader
   * @param parentHandler
   */
  protected AbstractSchemaHandler(MergeSchema schema, XMLReader reader, DefaultHandler parentHandler)
  {
    this.reader = reader;
    this.parentHandler = parentHandler;
    this.schema = schema;
  }

  protected abstract void initRootElement(Attributes attributes, String localName);

  public MergeSchema schema()
  {
    return schema;
  }

  @Override
  public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException
  {
    if (childHandler != null)
    {
      obtainChildElement();
    }
    dispatchChildHandler(attributes, localName);
  }

  /**
   * Returns the element that this handler builds
   * 
   * @return
   */
  public SchemaElementIF builtElement()
  {
    return root;
  }

  /**
   * Implements what this handler does with the elements obtained from its
   * childHandler
   */
  protected void obtainChildElement()
  {
    if (childHandler != null)
    {
      // Add the most recently created child to the children set of this element
      SchemaElement childElement = (SchemaElement) childHandler.builtElement();
      root.addChild(childElement);
      schema.registerDependency(childElement);
    }
  }

  /**
   * deletegates to the appropriate childHandler based on the tag of the present
   * XML element
   * 
   * @param attributes
   *          TODO
   * @param tag
   *          TODO
   */
  protected abstract void dispatchChildHandler(Attributes attributes, String tag);

  /**
   * returns the control to the parent handler
   */
  protected void returnToParentHandler(String localName)
  {
    reader.setContentHandler(parentHandler);
    reader.setErrorHandler(parentHandler);

  }

  @Override
  public void endElement(String uri, String localName, String name)
  {
    /**
     * If the closing tag requires a change of state of the schema perform that
     */
    if (SMXMLTags.isStateTag(localName))
    {
      schema.leavingCurrentState();
    }

    if (localName.equals(terminalTag()))
    {
      // If the parsing is at the end of the element and if there is still one
      // more child to obtain
      if (childHandler != null)
      {
        obtainChildElement();
      }
      returnToParentHandler(localName);
    }

  }

  protected String terminalTag()
  {
    return root.getTag();
  }

}
