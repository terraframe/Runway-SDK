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
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.constants.MdControllerInfo;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdControllerDAO;

public class MdControllerHandler extends XMLHandler
{
  private MdControllerDAO mdController;

  public MdControllerHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager)
  {
    super(reader, previousHandler, manager);

    // Get the MdController to import, if this is a create then a new instance of MdController is imported
    mdController = (MdControllerDAO) manager.getEntityDAO(MdControllerInfo.CLASS, attributes.getValue(XMLTags.NAME_ATTRIBUTE)).getEntityDAO();

    importMdController(attributes);

    // Make sure the class has not already been defined
    if (!manager.isCreated(mdController.definesType()))
    {
      mdController.apply();

      manager.addMapping(mdController.definesType(), mdController.getId());
    }
  }

  /**
   * Creates an MdController from the parse of the class tag attributes.
   * @param attributes The attributes of an class tag
   * @return MdController from the parse of the class tag attributes.
   */
  private final void importMdController(Attributes attributes)
  {
    //Breakup the type into a package and name
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    mdController.setValue(MdControllerInfo.NAME, BusinessDAOFactory.getClassNameFromType(name));
    mdController.setValue(MdControllerInfo.PACKAGE, BusinessDAOFactory.getPackageFromType(name));

    ImportManager.setLocalizedValue(mdController, MdControllerInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setValue(mdController, MdControllerInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdController, MdControllerInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdController, MdControllerInfo.EXPORTED, attributes, XMLTags.EXPORTED_ATTRIBUTE);

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

    if (manager.isCreated(mdController.definesType()))
    {
      return;
    }

    if(localName.equals(XMLTags.MD_ACTION_TAG))
    {
      MdActionHandler aHandler = new MdActionHandler(attributes, reader, this, manager, mdController);
      reader.setContentHandler(aHandler);
      reader.setErrorHandler(aHandler);
    }
    else if (localName.equals(XMLTags.STUB_SOURCE_TAG))
    {
      SourceHandler handler = new SourceHandler(reader, this, manager, mdController, MdControllerInfo.STUB_SOURCE);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
  }

  public void endElement(String namespaceURI, String localName, String fullName)
  {
    if (localName.equals(XMLTags.MD_CONTROLLER_TAG))
    {
      //Add the mdFacade to the created type
      manager.endImport(mdController.definesType());
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
    else if (localName.equals(XMLTags.CREATE_TAG))
    {
      manager.leavingCurrentState();
    }
  }
}
