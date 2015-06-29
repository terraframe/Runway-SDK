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

import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;

public class UpdateHandlerFactory implements HandlerFactoryIF
{

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.io.dataDefinition.HandlerFactoryIF#getHandler(
   * java.lang.String, org.xml.sax.Attributes, org.xml.sax.XMLReader,
   * com.runwaysdk.dataaccess.io.XMLHandler,
   * com.runwaysdk.dataaccess.io.ImportManager)
   */
  public XMLHandler getHandler(String localName, Attributes attributes, XMLReader reader, XMLHandler handler, ImportManager manager)
  {
    if (localName.equals(XMLTags.ENUMERATION_MASTER_TAG) || localName.equals(XMLTags.MD_BUSINESS_TAG) || localName.equals(XMLTags.MD_TERM_TAG) )
    {
      return new MdBusinessHandler(attributes, reader, handler, manager, localName);
    }
    else if (localName.equals(XMLTags.MD_STRUCT_TAG))
    {
      return new MdStructHandler(attributes, reader, handler, manager, localName);
    }
    else if (localName.equals(XMLTags.MD_LOCAL_STRUCT_TAG))
    {
      return new MdLocalStructHandler(attributes, reader, handler, manager, localName);
    }
    else if (localName.equals(XMLTags.MD_RELATIONSHIP_TAG) || localName.equals(XMLTags.MD_TREE_TAG) || localName.equals(XMLTags.MD_GRAPH_TAG) || localName.equals(XMLTags.MD_TERM_RELATIONSHIP_TAG) )
    {
      return new MdRelationshipHandlerWithCaching(attributes, reader, handler, manager, localName);
    }
    else if (localName.equals(XMLTags.OBJECT_TAG))
    {
      return new ObjectHandler(attributes, reader, handler, manager);
    }
    else if (localName.equals(XMLTags.RELATIONSHIP_TAG))
    {
      return new RelationshipHandler(attributes, reader, handler, manager);
    }
    else if (localName.equals(XMLTags.MD_ENUMERATION_TAG))
    {
      return new MdEnumerationHandler(attributes, reader, handler, manager);

    }
    else if (localName.equals(XMLTags.MD_FACADE_TAG))
    {
      return new MdFacadeHandler(attributes, reader, handler, manager);
    }
    else if (localName.equals(XMLTags.MD_EXCEPTION_TAG))
    {
      return new MdExceptionHandler(attributes, reader, handler, manager);
    }
    else if (localName.equals(XMLTags.MD_PROBLEM_TAG))
    {
      return new MdProblemHandler(attributes, reader, handler, manager);
    }
    else if (localName.equals(XMLTags.MD_INFORMATION_TAG))
    {
      return new MdInformationHandler(attributes, reader, handler, manager);
    }
    else if (localName.equals(XMLTags.MD_WARNING_TAG))
    {
      return new MdWarningHandler(attributes, reader, handler, manager);
    }
    else if (localName.equals(XMLTags.MD_VIEW_TAG))
    {
      return new MdViewHandler(attributes, reader, handler, manager);
    }
    else if (localName.equals(XMLTags.MD_UTIL_TAG))
    {
      return new MdUtilHandler(attributes, reader, handler, manager);
    }
    else if (localName.equals(XMLTags.MD_CONTROLLER_TAG))
    {
      return new MdControllerHandler(attributes, reader, handler, manager);
    }
    else if (localName.equals(XMLTags.MD_WEB_FORM_TAG))
    {
      return new MdWebFormHandler(attributes, reader, handler, manager);
    }

    return null;
  }

}
