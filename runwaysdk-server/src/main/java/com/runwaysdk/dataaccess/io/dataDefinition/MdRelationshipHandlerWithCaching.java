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
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLFilterImpl;

import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;

/**
 * 
 * @author Aritra
 * 
 * 
 *         Handles the mdRelationship tags so that the parent and child tags
 *         within them are always processed before the other components,
 *         irrespective of the order of appearance of parent and child in the
 *         XML document. In order to achieve this, it only processes the parent
 *         and child tag and caches all the events related to any other tag.
 *         When it sees the end of the mdRelationship tags the cached events are
 *         dispatched to a {@link MdRelationshipHandler}.
 * 
 * 
 */
public class MdRelationshipHandlerWithCaching extends MdRelationshipHandler
{

  private List<SAXEvent>        cachedEvents;

  private CachedEventDispatcher cachedEventDispatcher;

  public MdRelationshipHandlerWithCaching(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager, String tagName)
  {
    super(attributes, reader, previousHandler, manager, tagName);
    cachedEvents = new ArrayList<SAXEvent>();
    cachedEventDispatcher = new CachedEventDispatcher();
  }

  @Override
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    if (manager.isCreated(mdRelationshipDAO.definesType()))
    {
      return;
    }

    if (localName.equals(XMLTags.PARENT_TAG))
    {
      importParent(attributes);
    }
    else if (localName.equals(XMLTags.CHILD_TAG))
    {
      importChild(attributes);
    }
    else
    {
      // cache all the events other than parent and child
      cachedEvents.add(new StartSAXEvent(namespaceURI, localName, fullName, attributes));
    }
  }

  @Override
  public void endElement(String namespaceURI, String localName, String fullName) throws SAXException
  {
    if (!localName.equals(XMLTags.PARENT_TAG) && !localName.equals(XMLTags.CHILD_TAG))
    {
      cachedEvents.add(new EndSAXEvent(namespaceURI, localName, fullName));
    }

    if (localName.equals(XMLTags.MD_RELATIONSHIP_TAG) || localName.equals(XMLTags.MD_TREE_TAG) || localName.equals(XMLTags.MD_GRAPH_TAG) || localName.equals(XMLTags.MD_TERM_RELATIONSHIP_TAG))
    {
      if (!manager.isCreated(mdRelationshipDAO.definesType()))
      {
        mdRelationshipDAO.apply();
        manager.addMapping(mdRelationshipDAO.definesType(), mdRelationshipDAO.getId());
      }

      // once the end of the mdRelationship tag has been encountered, dispatch
      // the cached events
      if (!cachedEvents.isEmpty())
        startCachedEventDispatch();

      manager.endImport(mdRelationshipDAO.definesType());
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
  }

  private void startCachedEventDispatch()
  {
    XMLHandler cachedEventHandler = new MdRelationshipHandler(MdRelationshipHandlerWithCaching.this.mdRelationshipDAO, cachedEventDispatcher, this, manager);
    cachedEventDispatcher.setContentHandler(cachedEventHandler);
    cachedEventDispatcher.setErrorHandler(cachedEventHandler);
    cachedEventDispatcher.fireCachedEvents(cachedEvents);
  }

  /**
   * 
   * An XML filter that is used to dispatch the cached events.
   * 
   * @author runways
   * 
   */
  private static class CachedEventDispatcher extends XMLFilterImpl
  {
    public void fireCachedEvents(List<SAXEvent> saxEvents)
    {
      for (SAXEvent saxEvent : saxEvents)
      {
        try
        {
          if (saxEvent instanceof StartSAXEvent)
          {
            StartSAXEvent startEvent = (StartSAXEvent) saxEvent;
            super.startElement(startEvent.uri, startEvent.localName, startEvent.name, startEvent.attributes);
          }

          else
          {
            EndSAXEvent endSAXEvent = (EndSAXEvent) saxEvent;
            super.endElement(endSAXEvent.uri, endSAXEvent.localName, endSAXEvent.name);
          }
        }
        catch (SAXException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }

    }

  }

  private abstract static class SAXEvent
  {
    protected String uri;

    protected String localName;

    protected String name;

    public SAXEvent(String uri, String localName, String name)
    {
      this.uri = uri;
      this.localName = localName;
      this.name = name;
    }
  }

  private static class StartSAXEvent extends SAXEvent
  {

    protected Attributes attributes;

    public StartSAXEvent(String uri, String localName, String name, Attributes attributes)
    {
      super(uri, localName, name);
      this.attributes = new AttributesImpl(attributes);
    }

  }

  private static class EndSAXEvent extends SAXEvent
  {
    public EndSAXEvent(String uri, String localName, String name)
    {
      super(uri, localName, name);
    }
  }
}
