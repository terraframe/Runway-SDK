/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;

public class PermissionHandlerFactory implements HandlerFactoryIF
{
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.io.dataDefinition.HandlerFactoryIF#getHandler(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF, com.runwaysdk.dataaccess.io.ImportManager)
   */
  @Override
  public TagHandlerIF getHandler(String localName, Attributes attributes, TagHandlerIF prev, ImportManager manager)
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.io.dataDefinition.HandlerFactoryIF#supports(java.lang.String)
   */
  @Override
  public boolean supports(TagContext context, String localName)
  {
    // TODO Auto-generated method stub
    return false;
  }
  
//  /*
//   * (non-Javadoc)
//   * 
//   * @see com.runwaysdk.dataaccess.io.dataDefinition.HandlerFactoryIF#supports(java.lang.String)
//   */
//  @Override
//  public boolean supports(String localName)
//  {
//    if (localName.equals(XMLTags.USER_TAG))
//    {
//      return true;
//    }
//    else if (localName.equals(XMLTags.ROLE_TAG))
//    {
//      return true;
//    }
//    else if (localName.equals(XMLTags.METHOD_TAG))
//    {
//      return true;
//    }
//
//    return false;
//  }
//
//  /*
//   * (non-Javadoc)
//   * 
//   * @see com.runwaysdk.dataaccess.io.dataDefinition.HandlerFactoryIF#getHandler (java.lang.String, org.xml.sax.Attributes, org.xml.sax.XMLReader, com.runwaysdk.dataaccess.io.XMLHandler,
//   * com.runwaysdk.dataaccess.io.ImportManager)
//   */
//  public XMLHandler getHandler(String localName, Attributes attributes, TagHandlerIF prev, ImportManager manager)
//  {
//    if (localName.equals(XMLTags.USER_TAG))
//    {
//      return new UserPermissionHandler(attributes, reader, prev, manager);
//    }
//    else if (localName.equals(XMLTags.ROLE_TAG))
//    {
//      return new RolePermissionHandler(attributes, reader, prev, manager);
//    }
//    else if (localName.equals(XMLTags.METHOD_TAG))
//    {
//      return new MethodPermissionHandler(attributes, reader, prev, manager);
//    }
//
//    return null;
//  }
//
}
