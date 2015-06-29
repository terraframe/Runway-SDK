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

import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.business.rbac.ActorDAO;
import com.runwaysdk.business.rbac.MethodActorDAO;
import com.runwaysdk.business.rbac.MethodActorDAOIF;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.constants.MethodActorInfo;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;

public class MethodPermissionHandler extends XMLHandler
{
  /**
   * The BusinessDAO created by the metadata
   */
  private List<ActorDAO>   actors;

  /**
   * Enumeration of the desired permission action, defaults to GRANT
   */
  private PermissionAction action = PermissionAction.GRANT;

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
   */
  public MethodPermissionHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler,
      ImportManager manager)
  {
    super(reader, previousHandler, manager);

    actors = new LinkedList<ActorDAO>();

    String type = attributes.getValue(XMLTags.TYPE_ATTRIBUTE);
    String methodName = attributes.getValue(XMLTags.METHOD_NAME_ATTRIBUTE);

    MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(type);
    MdMethodDAOIF mdMethod = mdClass.getMdMethod(methodName);

    MethodActorDAOIF methodActor = mdMethod.getMethodActor();

    if (methodActor == null)
    {
      MethodActorDAO newActor = MethodActorDAO.newInstance();
      newActor.setValue(MethodActorInfo.MD_METHOD, mdMethod.getId());
      newActor.apply();

      methodActor = newActor;
    }

    actors.add(methodActor.getBusinessDAO());
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
    // Delegate control to a new PermissionHandler
    if (localName.equals(XMLTags.GRANT_TAG))
    {
      action = PermissionAction.GRANT;
    }
    else if (localName.equals(XMLTags.REVOKE_TAG))
    {
      action = PermissionAction.REVOKE;
    }
    else if (localName.equals(XMLTags.MD_BUSINESS_PERMISSION_TAG)
        || localName.equals(XMLTags.MD_STRUCT_PERMISSION_TAG)
        || localName.equals(XMLTags.MD_UTIL_PERMISSION_TAG)
        || localName.equals(XMLTags.MD_VIEW_PERMISSION_TAG))
    {
      MdClassPermissionHandler handler = new MdClassPermissionHandler(attributes, reader, this, manager,
          actors, localName, action);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
    else if (localName.equals(XMLTags.MD_FACADE_PERMISSION_TAG))
    {
      MdFacadePermissionHandler handler = new MdFacadePermissionHandler(attributes, reader, this,
          manager, actors, action);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
    else if (localName.equals(XMLTags.MD_RELATIONSHIP_PERMISSION_TAG))
    {
      MdRelationshipPermissionHandler handler = new MdRelationshipPermissionHandler(attributes, reader,
          this, manager, actors, action);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
    else if (localName.equals(XMLTags.ASSIGNED_ROLE_TAG))
    {
      String roleName = attributes.getValue(XMLTags.ROLENAME_ATTRIBUTE);
      RoleDAOIF roleIF = RoleDAO.findRole(roleName);

      for (ActorDAO actor : actors)
      {
        if (roleIF != null)
        {
          RoleDAO role = roleIF.getBusinessDAO();
          role.assignMember((MethodActorDAO)actor);
        }
      }
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
    if (localName.equals(XMLTags.METHOD_TAG))
    {
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
  }
}
