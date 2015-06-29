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

import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.business.rbac.ActorDAO;
import com.runwaysdk.dataaccess.AttributeDoesNotExistException;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MetadataDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;

public class MdRelationshipPermissionHandler extends AbstractPermissionHandler
{
  private MetadataDAOIF                          metadata;

  /**
   * Root mdEntity on which the permissions are being added
   */
  private MdEntityDAOIF                          mdEntity;

  /**
   * Map of all attributes defined by or inherited by the class. Key: attribute
   * name Value: MdAttributeIF
   */
  public Map<String, ? extends MdAttributeDAOIF> mdAttributeMap;

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
   * @param actors
   *          The actor on which to import permissions
   * @param action
   *          TODO
   */
  public MdRelationshipPermissionHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager, List<ActorDAO> actors, PermissionAction action)
  {
    super(reader, previousHandler, manager, actors, action);

    String type = attributes.getValue(XMLTags.TYPE_ATTRIBUTE);

    this.mdEntity = MdEntityDAO.getMdEntityDAO(type);
    this.mdAttributeMap = this.mdEntity.getAllDefinedMdAttributeMap();
    this.metadata = this.mdEntity;
  }

  /**
   * Parses the attributes tag Inherited from ContentHandler (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
   *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    if (localName.equals(XMLTags.OPERATION_TAG))
    {
      String operationName = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

      this.setPermission(operationName, mdEntity, metadata);
    }
    else if (localName.equals(XMLTags.ATTRIBUTE_PERMISSION_TAG))
    {
      String attributeName = attributes.getValue(XMLTags.PERMISSION_ATTRIBUTE_NAME);
      metadata = this.getMdAttribute(attributeName);
    }
    else if (localName.equals(XMLTags.MD_METHOD_PERMISSION_TAG))
    {
      String methodName = attributes.getValue(XMLTags.METHOD_PERMISSION_NAME_ATTRIBUTE);
      metadata = mdEntity.getMdMethod(methodName);
    }
    else if (localName.equals(XMLTags.PARENT_STATE_PERMISSION_TAG) || localName.equals(XMLTags.CHILD_STATE_PERMISSION_TAG))
    {
      DirectionalStatePermissionHandler handler = new DirectionalStatePermissionHandler(attributes, reader, this, manager, actors, (MdRelationshipDAOIF) mdEntity, localName, action);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
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
    if (localName.equals(XMLTags.MD_RELATIONSHIP_PERMISSION_TAG))
    {
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
  }

  /**
   * Returns the MdAttribute that defines the attribute with the given names.
   * 
   * @param attributeName
   * @return MdAttribute that defines the attribute with the given names.
   */
  private MdAttributeDAOIF getMdAttribute(String attributeName)
  {
    MdAttributeDAOIF mdAttributeIF = mdAttributeMap.get(attributeName.toLowerCase());
    if (mdAttributeIF == null)
    {
      String errMsg = "Attribute [" + attributeName + "] is not defined by class [" + this.mdEntity.definesType() + "]";
      throw new AttributeDoesNotExistException(errMsg, attributeName, this.mdEntity);
    }
    return mdAttributeIF;
  }
}
