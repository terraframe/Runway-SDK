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

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.business.rbac.ActorDAO;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.state.MdStateMachineDAOIF;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MetadataDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAO;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAOIF;

public class DirectionalStatePermissionHandler extends AbstractPermissionHandler
{
  private StateMasterDAOIF    stateMaster;

  private MdRelationshipDAOIF mdRelationship;

  private MetadataDAOIF       metadata;

  /**
   * Constructor - Creates a MdBusiness BusinessDAO and sets the parameters according to the attributes parse
   * @param attributes The attibutes of the class tag
   * @param reader The XMLReader stream
   * @param previousHandler The Handler which passed control
   * @param manager ImportManager which provides communication between handlers for a single impot
   * @param action TODO
   * @param locator The Locator stream
   */
  public DirectionalStatePermissionHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager,
      List<ActorDAO> actors, MdRelationshipDAOIF mdRelationship, String tagName, PermissionAction action)
  {
    super(reader, previousHandler, manager, actors, action);

    String stateName = attributes.getValue(XMLTags.STATE_NAME_ATTRIBUTE);
    MdStateMachineDAOIF mdStateMachine = null;

    if (tagName.equals(XMLTags.PARENT_STATE_PERMISSION_TAG))
    {
      mdStateMachine = mdRelationship.getParentMdBusiness().definesMdStateMachine();
    }
    else
    {
      mdStateMachine = mdRelationship.getChildMdBusiness().definesMdStateMachine();
    }

    this.mdRelationship = mdRelationship;
    this.stateMaster = mdStateMachine.definesStateMaster(stateName);

    this.metadata = TypeTupleDAO.findTuple(this.mdRelationship.getId(), this.stateMaster.getId());

    if (this.metadata == null)
    {
      TypeTupleDAO newTuple = TypeTupleDAO.newInstance();
      newTuple.setValue(TypeTupleDAOIF.METADATA, this.mdRelationship.getId());
      newTuple.setValue(TypeTupleDAOIF.STATE_MASTER, stateMaster.getId());
      newTuple.setStructValue(TypeTupleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,  mdRelationship.definesType() + "-" + stateMaster.getName() + " tuple");
      newTuple.apply();

      this.metadata = newTuple;
    }
  }

  /**
   * Parses the attributes tag
   * Inherited from ContentHandler
   * (non-Javadoc)
   * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    if (localName.equals(XMLTags.OPERATION_TAG))
    {
      String operationName = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
      Operation operation = Operation.valueOf(operationName);

      this.setPermission(operation, metadata.getId());
    }
  }

  /**
   * When the class tag is closed:
   * Returns parsing control back to the Handler which passed control
   *
   * Inherits from ContentHandler
   *  (non-Javadoc)
   * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String fullName)
  {
    if (localName.equals(XMLTags.PARENT_STATE_PERMISSION_TAG) || localName.equals(XMLTags.CHILD_STATE_PERMISSION_TAG))
    {
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
  }

}
