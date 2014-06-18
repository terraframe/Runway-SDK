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

import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.Type;

public class MdMethodHandler extends XMLHandler
{
  /**
   * The new MdMethod import file
   */
  private MdMethodDAO mdMethod;

  /**
   * Constructor - Creates a MdMethod object and sets the parameters according
   * to the attributes parse
   *
   * @param attributes
   *            The attibutes of the class tag
   * @param reader
   *            The XMLReader stream
   * @param previousHandler
   *            The Handler which passed control
   * @param manager
   *            ImportManager which provides communication between handlers for
   *            a single import
   * @param mdType
   *            The MdType for which the MdMethod is defined.
   */
  public MdMethodHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager, MdTypeDAO mdType)
  {
    super(reader, previousHandler, manager);

    // Get the MdBusiness to import, if this is a create then a new instance of
    // MdBusiness is imported
    mdMethod = (MdMethodDAO) manager.getEntityDAO(MdMethodInfo.CLASS, mdType.definesType() + "." + attributes.getValue(XMLTags.NAME_ATTRIBUTE)).getEntityDAO();

    importMdMethod(attributes, mdType);
    mdMethod.apply();
  }

  /*
   * (non-Javadoc)
   *
   * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
   *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    if (localName.equals(XMLTags.MD_PARAMETER_TAG))
    {
      MdParameterHandler handler = new MdParameterHandler(attributes, reader, this, manager, mdMethod);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
    else if (localName.equals(XMLTags.CREATE_TAG))
    {
      manager.enterCreateState();
    }
  }

  /**
   * Creates an MdMethod from the parse of the class tag attributes
   *
   * @param attributes
   *            The attributes of an class tag
   * @param mdType
   *            The mdType that defines the mdMethod
   */
  private final void importMdMethod(Attributes attributes, MdTypeDAO mdType)
  {
    mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdType.getId());

    if (attributes.getValue(XMLTags.METHOD_RETURN_ATTRIBUTE) != null)
    {
      Type returnType = new Type(attributes.getValue(XMLTags.METHOD_RETURN_ATTRIBUTE));

      // Ensure that the return type has been defined in the core if it is not a primitive
      if (returnType.isDefinedType() && !MdTypeDAO.isDefined(returnType.getRootType()))
      {        
        SearchHandler.searchEntity(manager, XMLTags.TYPE_TAGS, XMLTags.NAME_ATTRIBUTE, returnType.getRootType(), mdType.definesType());
      }

      mdMethod.setValue(MdMethodInfo.RETURN_TYPE, returnType.getType());
    }

    ImportManager.setValue(mdMethod, MdMethodInfo.NAME, attributes, XMLTags.NAME_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdMethod, MdMethodInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdMethod, MdMethodInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdMethod, MdMethodInfo.IS_STATIC, attributes, XMLTags.METHOD_STATIC_ATTRIBUTE);
  }

  /*
   * (non-Javadoc)
   *
   * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String fullName)
  {
    if (localName.equals(XMLTags.MD_METHOD_TAG))
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
