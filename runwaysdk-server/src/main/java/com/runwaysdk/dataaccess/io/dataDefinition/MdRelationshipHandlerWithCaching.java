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
package com.runwaysdk.dataaccess.io.dataDefinition;


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
public class MdRelationshipHandlerWithCaching //extends MdRelationshipHandler
{
//
//  private List<SAXEvent>        cachedEvents;
//
//  private CachedEventDispatcher cachedEventDispatcher;
//
//  public MdRelationshipHandlerWithCaching(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager, String tagName)
//  {
//    super(attributes, reader, previousHandler, manager, tagName);
//    cachedEvents = new ArrayList<SAXEvent>();
//    cachedEventDispatcher = new CachedEventDispatcher();
//  }
//
//  @Override
//  public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException
//  {
//    if (manager.isCreated(mdRelationshipDAO.definesType()))
//    {
//      return;
//    }
//
//    if (qName.equals(XMLTags.PARENT_TAG))
//    {
//      importParent(attributes);
//    }
//    else if (qName.equals(XMLTags.CHILD_TAG))
//    {
//      importChild(attributes);
//    }
//    else
//    {
//      // cache all the events other than parent and child
//      cachedEvents.add(new StartSAXEvent(namespaceURI, qName, fullName, attributes));
//    }
//  }
//
//  @Override
//  public void endElement(String namespaceURI, String localName, String qName) throws SAXException
//  {
//    if (!qName.equals(XMLTags.PARENT_TAG) && !qName.equals(XMLTags.CHILD_TAG))
//    {
//      cachedEvents.add(new EndSAXEvent(namespaceURI, qName, fullName));
//    }
//
//    if (qName.equals(XMLTags.MD_RELATIONSHIP_TAG) || qName.equals(XMLTags.MD_TREE_TAG) || qName.equals(XMLTags.MD_GRAPH_TAG) || qName.equals(XMLTags.MD_TERM_RELATIONSHIP_TAG))
//    {
//      if (!manager.isCreated(mdRelationshipDAO.definesType()))
//      {
//        mdRelationshipDAO.apply();
//        manager.addMapping(mdRelationshipDAO.definesType(), mdRelationshipDAO.getOid());
//      }
//
//      // once the end of the mdRelationship tag has been encountered, dispatch
//      // the cached events
//      if (!cachedEvents.isEmpty())
//        startCachedEventDispatch();
//
//      manager.endImport(mdRelationshipDAO.definesType());
//      reader.setContentHandler(previousHandler);
//      reader.setErrorHandler(previousHandler);
//    }
//  }
//
//  private void startCachedEventDispatch()
//  {
//    XMLHandler cachedEventHandler = new MdRelationshipHandler(MdRelationshipHandlerWithCaching.this.mdRelationshipDAO, cachedEventDispatcher, this, manager);
//    cachedEventDispatcher.setContentHandler(cachedEventHandler);
//    cachedEventDispatcher.setErrorHandler(cachedEventHandler);
//    cachedEventDispatcher.fireCachedEvents(cachedEvents);
//  }
//
//  /**
//   * 
//   * An XML filter that is used to dispatch the cached events.
//   * 
//   * @author runways
//   * 
//   */
//  private static class CachedEventDispatcher extends XMLFilterImpl
//  {
//    public void fireCachedEvents(List<SAXEvent> saxEvents)
//    {
//      for (SAXEvent saxEvent : saxEvents)
//      {
//        try
//        {
//          if (saxEvent instanceof StartSAXEvent)
//          {
//            StartSAXEvent startEvent = (StartSAXEvent) saxEvent;
//            super.startElement(startEvent.uri, startEvent.qName, startEvent.name, startEvent.attributes);
//          }
//
//          else
//          {
//            EndSAXEvent endSAXEvent = (EndSAXEvent) saxEvent;
//            super.endElement(endSAXEvent.uri, endSAXEvent.qName, endSAXEvent.name);
//          }
//        }
//        catch (SAXException e)
//        {
//          // TODO Auto-generated catch block
//          e.printStackTrace();
//        }
//      }
//
//    }
//
//  }
//
//  private abstract static class SAXEvent
//  {
//    protected String uri;
//
//    protected String qName;
//
//    protected String name;
//
//    public SAXEvent(String uri, String qName, String name)
//    {
//      this.uri = uri;
//      this.qName = qName;
//      this.name = name;
//    }
//  }
//
//  private static class StartSAXEvent extends SAXEvent
//  {
//
//    protected Attributes attributes;
//
//    public StartSAXEvent(String uri, String qName, String name, Attributes attributes)
//    {
//      super(uri, qName, name);
//      this.attributes = new AttributesImpl(attributes);
//    }
//
//  }
//
//  private static class EndSAXEvent extends SAXEvent
//  {
//    public EndSAXEvent(String uri, String qName, String name)
//    {
//      super(uri, qName, name);
//    }
//  }
}
