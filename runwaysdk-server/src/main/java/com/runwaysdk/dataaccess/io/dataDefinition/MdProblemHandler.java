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

import com.runwaysdk.constants.MdMessageInfo;
import com.runwaysdk.constants.MdProblemInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.dataaccess.MdProblemDAOIF;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdProblemDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;

public class MdProblemHandler extends XMLHandler
{
  /**
   * The BusinessDAO created by the metadata
   */
  private MdProblemDAO mdProblem;

  /**
   * Constructor - Creates a MdBusiness BusinessDAO and sets the parameters
   * according to the attributes parse
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
   */
  public MdProblemHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager)
  {
    super(reader, previousHandler, manager);

    // Get the MdExcpetion to import, if this is a create then a new instance of MdProblem is imported
    mdProblem = (MdProblemDAO) manager.getEntityDAO(MdProblemInfo.CLASS, attributes.getValue(XMLTags.NAME_ATTRIBUTE)).getEntityDAO();

    importMdProblem(attributes);

    // Make sure the class has not already been defined
    if (!manager.isCreated(mdProblem.definesType()))
    {
      mdProblem.apply();
    }
  }

  /**
   * Parses the attributes tag Inherited from ContentHandler (non-Javadoc)
   *
   * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
   *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    if (localName.equals(XMLTags.CREATE_TAG))
    {
      manager.enterCreateState();
    }

    // If this object has already been created in this import
    // then parsing the attributes is not needed
    if (manager.isCreated(mdProblem.definesType()))
    {
      return;
    }

    // Delegate control to a new AttributesHandler
    if (localName.equals(XMLTags.ATTRIBUTES_TAG))
    {
      MdAttributeHandler handler = new MdAttributeHandler(attributes, reader, this, manager, mdProblem);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
    else if (localName.equals(XMLTags.STUB_SOURCE_TAG))
    {
      SourceHandler handler = new SourceHandler(reader, this, manager, mdProblem, MdProblemInfo.STUB_SOURCE);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
    else if (localName.equals(XMLTags.DTO_STUB_SOURCE_TAG))
    {
      SourceHandler handler = new SourceHandler(reader, this, manager, mdProblem, MdProblemInfo.DTO_STUB_SOURCE);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
  }

  /**
   * Creates an MdProblem from the parse of the mdProblem attributes.
   *
   * @param attributes
   *            The attributes of the mdProblem tag
   */
  private final void importMdProblem(Attributes attributes)
  {
    // Import the required attributes and Breakup the type into a package and name
    String type = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    mdProblem.setValue(MdTypeInfo.NAME, BusinessDAOFactory.getClassNameFromType(type));
    mdProblem.setValue(MdTypeInfo.PACKAGE, BusinessDAOFactory.getPackageFromType(type));

    // Import optional attributes
    ImportManager.setLocalizedValue(mdProblem, MdProblemInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setValue(mdProblem, MdProblemInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdProblem, MdProblemInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdProblem, MdProblemInfo.PUBLISH, attributes, XMLTags.PUBLISH_ATTRIBUTE);
    ImportManager.setValue(mdProblem, MdProblemInfo.ABSTRACT, attributes, XMLTags.ABSTRACT_ATTRIBUTE);
    ImportManager.setValue(mdProblem, MdProblemInfo.EXTENDABLE, attributes, XMLTags.EXTENDABLE_ATTRIBUTE);
    ImportManager.setValue(mdProblem, MdProblemInfo.EXPORTED, attributes, XMLTags.EXPORTED_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdProblem, MdMessageInfo.MESSAGE, attributes, XMLTags.MESSAGE_ATTRIBUTE);


    // Import optional reference attributes
    String extend = attributes.getValue(XMLTags.EXTENDS_ATTRIBUTE);

    if (extend != null)
    {
      // Ensure the parent class has already been defined in the database
      if (!MdTypeDAO.isDefined(extend))
      {
        // The type is not defined in the database, check if it is defined
        // in the further down in the xml document.
        String[] search_tags = { XMLTags.MD_PROBLEM_TAG};
        SearchHandler.searchEntity(manager, search_tags, XMLTags.NAME_ATTRIBUTE, extend, mdProblem.definesType());
      }

      MdProblemDAOIF superException= MdProblemDAO.getMdProblem(extend);
      mdProblem.setValue(MdProblemInfo.SUPER_MD_PROBLEM, superException.getId());
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
    if (localName.equals(XMLTags.MD_PROBLEM_TAG))
    {
      manager.endImport(mdProblem.definesType());
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
    else if (localName.equals(XMLTags.CREATE_TAG) || localName.equals(XMLTags.UPDATE_TAG))
    {
      manager.leavingCurrentState();
    }
  }

}
