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
package com.runwaysdk.dataaccess.schemamanager.xml;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLFilterImpl;

import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;

public class UndoItXMLFilter extends XMLFilterImpl
{

  private Stack<SchemaEvent> eventStack;

  private boolean            isBlocked = true;

  public UndoItXMLFilter()
  {
    eventStack = new Stack<SchemaEvent>();
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException
  {
    if (qName.equals(XMLTags.UNDO_IT_TAG))
      unblock();
    if (!isBlocked() && !qName.equals(XMLTags.UNDO_IT_TAG))
    {
      // the parsing is inside the undo it tag
      // construct a start event
      SchemaEvent event = new StartSchemaEvent(uri, qName, qName, atts)
      {
        @Override
        public void dispatchAction() throws SAXException
        {
          parentStartElement(this.uri, this.qName, this.name, this.getAttributes());
        }
      };

      eventStack.push(event);
    }
  }

  private void parentStartElement(String uri, String localName, String qName, Attributes atts) throws SAXException
  {
    super.startElement(uri, qName, qName, atts);
  }

  private void parentEndElement(String uri, String localName, String qName) throws SAXException
  {
    super.endElement(uri, qName, qName);
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    if (qName.equals(XMLTags.UNDO_IT_TAG))
      block();
    if (!isBlocked() && !qName.equals(XMLTags.UNDO_IT_TAG))
    {
      // the parsing is inside the undo it tag
      // construct a start event
      SchemaEvent event = new EndSchemaEvent(uri, qName, qName)
      {
        @Override
        public void dispatchAction() throws SAXException
        {
          parentEndElement(this.uri, this.qName, this.getName());
        }
      };
      eventStack.push(event);
    }

  }

  @Override
  public void endDocument() throws SAXException
  {
    while (!eventStack.isEmpty())
    {
      eventStack.pop().dispatchAction();
    }
  }

  private void block()
  {
    isBlocked = true;
  }

  private void unblock()
  {
    isBlocked = false;
  }

  public boolean isBlocked()
  {
    return isBlocked;
  }

  private abstract static class SchemaEvent
  {
    protected String uri;

    protected String qName;

    protected String name;

    public SchemaEvent(String uri, String qName, String name)
    {
      this.uri = uri;
      this.qName = qName;
      this.name = name;
    }
    
    public String getName()
    {
      return name;
    }

    public abstract void dispatchAction() throws SAXException;

  }

  private abstract static class StartSchemaEvent extends SchemaEvent
  {

    protected Attributes attributes;

    public StartSchemaEvent(String uri, String qName, String name, Attributes attributes)
    {
      super(uri, qName, name);
      attributes = new AttributesImpl(attributes);
    }
    
    public Attributes getAttributes()
    {
      return attributes;
    }

  }

  private abstract static class EndSchemaEvent extends SchemaEvent
  {
    public EndSchemaEvent(String uri, String qName, String name)
    {
      super(uri, qName, name);
    }

  }
}
