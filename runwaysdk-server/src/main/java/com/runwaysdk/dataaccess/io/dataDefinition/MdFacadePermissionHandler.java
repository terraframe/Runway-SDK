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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.business.rbac.ActorDAO;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.MetadataDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdFacadeDAO;

public class MdFacadePermissionHandler extends AbstractPermissionHandler
{
  /**
   * The current Metadata definition on which permissions are being set
   */
  private MetadataDAOIF metadata;

  /**
   * Root mdEntity on which the permissions are being added
   */
  private MdFacadeDAOIF mdFacade;
    
  /**
   * Constructor - Creates a MdBusiness BusinessDAO and sets the parameters according to the attributes parse
   * @param attributes The attibutes of the class tag
   * @param reader The XMLReader stream
   * @param previousHandler The Handler which passed control
   * @param manager ImportManager which provides communication between handlers for a single import
   * @param actors The actor on which to import permissions
   * @param action TODO
   */
  public MdFacadePermissionHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager, List<ActorDAO> actors, PermissionAction action)
  {
    super(reader, previousHandler, manager, actors, action);

    String type = attributes.getValue(XMLTags.TYPE_ATTRIBUTE);

    this.mdFacade = MdFacadeDAO.getMdFacadeDAO(type);
    
    //By default the permissions are going to be set on the root MdClass
    //However, metadata will change to an attribute defined by the root MdClass
    this.metadata = this.mdFacade;
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
    else if (localName.equals(XMLTags.MD_METHOD_PERMISSION_TAG))
    {
      String methodName = attributes.getValue(XMLTags.METHOD_PERMISSION_NAME_ATTRIBUTE);
      metadata = mdFacade.getMdMethod(methodName);      
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
    if (localName.equals(XMLTags.MD_FACADE_PERMISSION_TAG))
    {
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
  }
}
