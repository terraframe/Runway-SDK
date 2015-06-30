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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.constants.MdFacadeInfo;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdFacadeDAO;

public class MdFacadeHandler extends XMLHandler
{
  /**
   *  The new MdFacade created by the imported xml
   */
  private MdFacadeDAO          mdFacade;

  /**
   * Constructor - Creates a MdBusiness object and sets the parameters according to the attributes parse
   *
   * @param attributes The attibutes of the class tag
   * @param reader The XMLReader stream
   * @param previousHandler The Handler which passed control
   * @param manager ImportManager which provides communication between handlers for a single import
   */
  public MdFacadeHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager)
  {
    super(reader, previousHandler, manager);

    // Get the MdFacade to import, if this is a create then a new instance of MdFacade is imported
    mdFacade = (MdFacadeDAO) manager.getEntityDAO(MdFacadeInfo.CLASS, attributes.getValue(XMLTags.NAME_ATTRIBUTE)).getEntityDAO();

    importMdFacade(attributes);

    // Make sure the class has not already been defined
    if (!manager.isCreated(mdFacade.definesType()))
    {
      mdFacade.apply();
      manager.addMapping(mdFacade.definesType(), mdFacade.getId());
    }
  }

  /* (non-Javadoc)
   * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    if (localName.equals(XMLTags.CREATE_TAG))
    {
      manager.enterCreateState();
    }

    if (manager.isCreated(mdFacade.definesType()))
    {
      return;
    }

    if(localName.equals(XMLTags.MD_METHOD_TAG))
    {
      MdMethodHandler aHandler = new MdMethodHandler(attributes, reader, this, manager, mdFacade);
      reader.setContentHandler(aHandler);
      reader.setErrorHandler(aHandler);
    }
    else if (localName.equals(XMLTags.STUB_SOURCE_TAG))
    {
      SourceHandler handler = new SourceHandler(reader, this, manager, mdFacade, MdFacadeInfo.STUB_SOURCE);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
  }

  /**
   * Creates an MdFacade from the parse of the class tag attributes.
   * @param attributes The attributes of an class tag
   * @return MdFacade from the parse of the class tag attributes.
   */
  private final void importMdFacade(Attributes attributes)
  {
    //Breakup the type into a package and name
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    mdFacade.setValue(MdFacadeInfo.NAME, BusinessDAOFactory.getClassNameFromType(name));
    mdFacade.setValue(MdFacadeInfo.PACKAGE, BusinessDAOFactory.getPackageFromType(name));

    ImportManager.setLocalizedValue(mdFacade, MdFacadeInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setValue(mdFacade, MdFacadeInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdFacade, MdFacadeInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdFacade, MdFacadeInfo.EXPORTED, attributes, XMLTags.EXPORTED_ATTRIBUTE);
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
    if (localName.equals(XMLTags.MD_FACADE_TAG))
    {
      //Add the mdFacade to the created type
      manager.endImport(mdFacade.definesType());
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
    else if (localName.equals(XMLTags.CREATE_TAG))
    {
      manager.leavingCurrentState();
    }
  }
}
