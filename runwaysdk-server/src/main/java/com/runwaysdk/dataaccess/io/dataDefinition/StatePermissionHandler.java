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
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MetadataDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAO;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAOIF;

public class StatePermissionHandler extends AbstractPermissionHandler
{
  private StateMasterDAOIF stateMaster;

  private MdBusinessDAOIF  mdBusiness;

  private MetadataDAOIF    metadata;

  /**
   * Constructor - Creates a MdBusiness BusinessDAO and sets the parameters
   * according to the attributes parse
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
   * @param action
   *          TODO
   */
  public StatePermissionHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler,
      ImportManager manager, List<ActorDAO> actors, MdBusinessDAOIF mdBusiness, PermissionAction action)
  {
    super(reader, previousHandler, manager, actors, action);

    String stateName = attributes.getValue(XMLTags.STATE_NAME_ATTRIBUTE);

    MdStateMachineDAOIF mdStateMachine = mdBusiness.definesMdStateMachine();

    this.mdBusiness = mdBusiness;
    this.stateMaster = mdStateMachine.definesStateMaster(stateName);
    this.metadata = this.stateMaster;
  }

  /**
   * Parses the attributes tag Inherited from ContentHandler (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
   *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes)
      throws SAXException
  {
    if (localName.equals(XMLTags.OPERATION_TAG))
    {
      String operationName = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
      Operation operation = Operation.valueOf(operationName);

      this.setPermission(operation, metadata.getId());
    }
    else if (localName.equals(XMLTags.ATTRIBUTE_PERMISSION_TAG))
    {
      String attributeName = attributes.getValue(XMLTags.PERMISSION_ATTRIBUTE_NAME);
      MdAttributeDAOIF mdAttribute = mdBusiness.definesAttribute(attributeName);

      TypeTupleDAOIF typeTuple = TypeTupleDAO.findTuple(mdAttribute.getId(), stateMaster
          .getId());

      if (typeTuple == null)
      {
        TypeTupleDAO newTuple = TypeTupleDAO.newInstance();
        newTuple.setValue(TypeTupleDAOIF.METADATA, mdAttribute.getId());
        newTuple.setValue(TypeTupleDAOIF.STATE_MASTER, stateMaster.getId());
        newTuple.setStructValue(TypeTupleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,
            mdAttribute.definesAttribute() + "-" + stateMaster.getName() + " tuple");
        newTuple.apply();

        typeTuple = newTuple;
      }

      metadata = typeTuple;
    }
  }

  /**
   * When the class tag is closed: Returns parsing control back to the Handler
   * which passed control
   * 
   * Inherits from ContentHandler (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String fullName)
  {
    if (localName.equals(XMLTags.STATE_PERMISSION_TAG))
    {
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
    else if (localName.equals(XMLTags.ATTRIBUTE_PERMISSION_TAG))
    {
      metadata = null;
    }

  }
}
