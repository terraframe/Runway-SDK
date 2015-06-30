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

import com.runwaysdk.constants.MdWebFormInfo;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFormDAO;

public class MdWebFormHandler extends XMLHandler
{
  /**
   * The MdWebFormDAO created by the metadata
   */
  private MdWebFormDAO mdWebForm;

  public MdWebFormHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager)
  {
    super(reader, previousHandler, manager);

    // Get the MdWebForm to import, if this is a create then a new instance of
    // MdWebForm is imported
    this.mdWebForm = (MdWebFormDAO) manager.getEntityDAO(MdWebFormInfo.CLASS, attributes.getValue(XMLTags.NAME_ATTRIBUTE)).getEntityDAO();

    importMdWebForm(attributes);

    // Make sure the class has not already been defined
    if (!manager.isCreated(mdWebForm.definesType()))
    {
      mdWebForm.apply();

      manager.addMapping(mdWebForm.definesType(), mdWebForm.getId());
    }
  }

  /**
   * Creates an MdWebForm from the parse of the class tag attributes.
   * 
   * @param attributes
   *          The attributes of an class tag
   * @return MdWebForm from the parse of the class tag attributes.
   */
  private final void importMdWebForm(Attributes attributes)
  {
    // Breakup the type into a package and name
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    mdWebForm.setValue(MdWebFormInfo.NAME, BusinessDAOFactory.getClassNameFromType(name));
    mdWebForm.setValue(MdWebFormInfo.PACKAGE, BusinessDAOFactory.getPackageFromType(name));

    ImportManager.setValue(mdWebForm, MdWebFormInfo.FORM_NAME, attributes, XMLTags.FORM_NAME);
    ImportManager.setValue(mdWebForm, MdWebFormInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setValue(mdWebForm, MdWebFormInfo.EXPORTED, attributes, XMLTags.EXPORTED_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdWebForm, MdWebFormInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdWebForm, MdWebFormInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);

    // Import optional reference attributes
    String classType = attributes.getValue(XMLTags.FORM_MD_CLASS);

    if (classType != null)
    {
      // Ensure the parent class has already been defined in the database
      if (!MdTypeDAO.isDefined(classType))
      {
        // The type is not defined in the database, check if it is defined
        // in the further down in the xml document.
        String[] search_tags = { XMLTags.MD_BUSINESS_TAG, XMLTags.MD_TERM_TAG };
        SearchHandler.searchEntity(manager, search_tags, XMLTags.NAME_ATTRIBUTE, classType, mdWebForm.definesType());
      }

      MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(classType);
      mdWebForm.setValue(MdWebFormInfo.FORM_MD_CLASS, mdClass.getId());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
   * java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    if (localName.equals(XMLTags.CREATE_TAG))
    {
      manager.enterCreateState();
    }

    if (manager.isCreated(mdWebForm.definesType()))
    {
      return;
    }

    if (localName.equals(XMLTags.FIELDS_TAG))
    {
      MdWebFieldHandler aHandler = new MdWebFieldHandler(attributes, reader, this, manager, mdWebForm);
      reader.setContentHandler(aHandler);
      reader.setErrorHandler(aHandler);
    }
  }

  public void endElement(String namespaceURI, String localName, String fullName)
  {
    if (localName.equals(XMLTags.MD_WEB_FORM_TAG))
    {
      // Add the mdFacade to the created type
      manager.endImport(mdWebForm.definesType());
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
    else if (localName.equals(XMLTags.CREATE_TAG))
    {
      manager.leavingCurrentState();
    }
  }
}
