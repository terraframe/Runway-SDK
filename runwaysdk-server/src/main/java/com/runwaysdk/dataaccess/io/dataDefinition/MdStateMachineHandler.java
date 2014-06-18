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
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.business.state.MdStateMachineDAO;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdStateMachineInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;

/**
 * @author Justin
 *
 */
public class MdStateMachineHandler extends XMLHandler
{
  /**
   * Enumeration specifying that states of the
   * MdStateMachine handler.
   *
   * @author Justin Smethie
   */
  private enum State
  {
    /**
     * The Handler is importing a state machine
     */
    STATE_MACHINE,

    /**
     * The Handler is importing the states tag
     */
    STATES,

    /**
     * The Handler is importing the transitions tag
     */
    TRANSITIONS;
  }

  /**
   *  The new MdStateMachine
   */
  private MdStateMachineDAO        mdStateMachine;

  /**
   * The current state of the handler
   */
  private State state;

  /**
   * Constructor - Creates a MdStateMachine and sets the parameters according to the attributes parse
   *
   * @param attributes The attibutes of the class tag
   * @param reader The XMLReader stream
   * @param previousHandler The Handler which passed control
   * @param manager ImportManager which provides communication between handlers for a single import
   * @param mdBusiness The MdBusiness for which the MdStateMachine is defined.
   */
  public MdStateMachineHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager, MdBusinessDAO mdBusiness)
  {
    super(reader, previousHandler, manager);

    state = State.STATE_MACHINE;

    mdStateMachine = (MdStateMachineDAO) manager.getEntityDAO(MdStateMachineInfo.CLASS, attributes.getValue(XMLTags.NAME_ATTRIBUTE)).getEntityDAO();
    importMdStateMachine(attributes, mdBusiness);
    mdStateMachine.apply();
  }

  /**
   * Creates an MdStateMachine from the parse of the class tag attributes
   *
   * @param attributes The attributes of an class tag
   * @param mdBusiness The MdBusiness that defines the MdStateMachine
   */
  private final void importMdStateMachine(Attributes attributes, MdBusinessDAO mdBusiness)
  {
    String type = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String name = BusinessDAOFactory.getClassNameFromType(type);
    String pack = BusinessDAOFactory.getPackageFromType(type);

    mdStateMachine.setValue(MdStateMachineInfo.NAME, name);
    mdStateMachine.setValue(MdStateMachineInfo.PACKAGE, pack);
    mdStateMachine.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness.getId());
    mdStateMachine.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, MdStateMachineInfo.STATE_MASTER);

    ImportManager.setLocalizedValue(mdStateMachine, MetadataInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);

    ImportManager.setLocalizedValue(mdStateMachine, MdElementInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
  }

  /* (non-Javadoc)
   * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    if(state.equals(State.STATE_MACHINE) && localName.equals(XMLTags.STATES_TAG))
    {
      state = State.STATES;
    }
    else if(state.equals(State.STATE_MACHINE) && localName.equals(XMLTags.TRANSITIONS_TAG))
    {
      state = State.TRANSITIONS;
    }
    else if(state.equals(State.STATES) && localName.equals(XMLTags.STATE_TAG))
    {
      StateMasterHandler handler = new StateMasterHandler(attributes, reader, this, manager, mdStateMachine);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
    else if(state.equals(State.TRANSITIONS) && localName.equals(XMLTags.TRANSITION_TAG))
    {
      TransitionHandler handler = new TransitionHandler(attributes, reader, this, manager, mdStateMachine);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
    else if (localName.equals(XMLTags.CREATE_TAG))
    {
      manager.enterCreateState();
    }
  }

  /* (non-Javadoc)
   * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String fullName)
  {
    if(state.equals(State.TRANSITIONS) && localName.equals(XMLTags.TRANSITIONS_TAG))
    {
      state = State.STATE_MACHINE;
    }
    else if(state.equals(State.STATES) && localName.equals(XMLTags.STATES_TAG))
    {
      state = State.STATE_MACHINE;
    }
    else if(state.equals(State.STATE_MACHINE) && localName.equals(XMLTags.MD_STATE_MACHINE_TAG))
    {
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
    else if (localName.equals(XMLTags.CREATE_TAG))
    {
      manager.leavingCurrentState();
    }
  }
}
