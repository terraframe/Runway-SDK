/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.business.state.MdStateMachineDAO;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.TransitionDAO;
import com.runwaysdk.dataaccess.TransitionDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;

public class TransitionHandler extends XMLHandler
{
  /**
   * The MdStateMachine on which the transition is defined
   */
  private MdStateMachineDAO mdStateMachine;

  /**
   * Constructor - Creates a Transitions and sets the parameters according to
   * the attributes parse
   * 
   * @param attributes
   *          The attibutes of the class tag
   * @param reader
   *          The XMLReader stream
   * @param previousHandler
   *          The Handler which passed control
   * @param manager
   *          ImportManager which provides communication between handlers for a
   *          single import
   * @param mdStateMachine
   *          The MdStateMachine for which the StateMaster is defined.
   */
  public TransitionHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager, MdStateMachineDAO mdStateMachine)
  {
    super(reader, previousHandler, manager);

    this.mdStateMachine = mdStateMachine;

    importTransition(attributes);
  }

  /**
   * Creates a StateMaster
   * 
   * @param attributes
   *          The attributes of an class tag
   */
  private final void importTransition(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String source = attributes.getValue(XMLTags.SOURCE_ATTRIBUTE);
    String sink = attributes.getValue(XMLTags.SINK_ATTRIBUTE);

    String sourceId = mdStateMachine.definesStateMaster(source).getId();
    String sinkId = mdStateMachine.definesStateMaster(sink).getId();

    MdRelationshipDAOIF mdTransition = mdStateMachine.getMdTransition();
    TransitionDAO newTransition = null;

    if (manager.isCreateState())
    {
      newTransition = TransitionDAO.newInstance(sourceId, sinkId, mdTransition.definesType());
      newTransition.setName(name);
    }
    else if (manager.isCreateOrUpdateState())
    {
      try
      {
        String key = TransitionDAO.buildKey(mdStateMachine.definesType(), name);
        newTransition = (TransitionDAO) RelationshipDAO.get(mdTransition.definesType(), key);
      }
      catch (DataNotFoundException e)
      {
        newTransition = TransitionDAO.newInstance(sourceId, sinkId, mdTransition.definesType());
        newTransition.setName(name);
      }
    }
    else
    {
      String key = TransitionDAO.buildKey(mdStateMachine.definesType(), name);
      newTransition = (TransitionDAO) RelationshipDAO.get(mdTransition.definesType(), key);
    }

    ImportManager.setLocalizedValue(newTransition, TransitionDAOIF.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    newTransition.apply();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
   * java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
   * java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String fullName)
  {
    if (localName.equals(XMLTags.TRANSITION_TAG))
    {
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
  }
}
