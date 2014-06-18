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

import com.runwaysdk.constants.MdWebBooleanInfo;
import com.runwaysdk.constants.MdWebCharacterInfo;
import com.runwaysdk.constants.MdWebDateInfo;
import com.runwaysdk.constants.MdWebDoubleInfo;
import com.runwaysdk.constants.MdWebFieldInfo;
import com.runwaysdk.constants.MdWebFloatInfo;
import com.runwaysdk.constants.MdWebGeoInfo;
import com.runwaysdk.constants.MdWebIntegerInfo;
import com.runwaysdk.constants.MdWebLongInfo;
import com.runwaysdk.constants.MdWebMultipleTermInfo;
import com.runwaysdk.constants.MdWebPrimitiveInfo;
import com.runwaysdk.constants.MdWebReferenceInfo;
import com.runwaysdk.constants.MdWebSingleTermGridInfo;
import com.runwaysdk.constants.MdWebSingleTermInfo;
import com.runwaysdk.constants.MdWebTextInfo;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdWebFieldDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdFieldDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFieldDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFormDAO;

public class MdWebFieldHandler extends XMLHandler
{
  /**
   * The mdClass on which the mdAttributes are being defined
   */
  private MdWebFormDAO mdForm;

  /**
   * Handler Construction, parses and creates a list new MdAttribute definition
   * 
   * @param attributes
   *          The XML attributes of the tag.
   * @param reader
   *          The XML parsing stream.
   * @param previousHandler
   *          The Handler in which control was passed from.
   * @param manager
   *          ImportManager containing information about the status of the
   *          import.
   * @param mdForm
   *          The MdClass on which the MdAttribute is defined
   */
  public MdWebFieldHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager, MdWebFormDAO mdForm)
  {
    super(reader, previousHandler, manager);

    this.mdForm = mdForm;
  }

  /**
   * Handles all the attribute tags, see datatype.xsd for a complete list
   * Inherits from ContentHandler (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
   *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    MdFieldDAO mdField = null;

    // Delegates elements tag to methods
    if (localName.equals(XMLTags.GEO_TAG))
    {
      mdField = importMdWebAttribute(attributes, MdWebGeoInfo.CLASS);
    }
    else if (localName.equals(XMLTags.TERM_TAG))
    {
      mdField = importMdWebAttribute(attributes, MdWebSingleTermInfo.CLASS);
    }
    else if (localName.equals(XMLTags.MULTI_TERM_TAG))
    {
      mdField = importMdWebAttribute(attributes, MdWebMultipleTermInfo.CLASS);
    }
    else if (localName.equals(XMLTags.GRID_TAG))
    {
      mdField = importMdWebAttribute(attributes, MdWebSingleTermGridInfo.CLASS);
    }
    else if (localName.equals(XMLTags.CHARACTER_TAG))
    {
      mdField = importMdWebAttribute(attributes, MdWebCharacterInfo.CLASS);

      ImportManager.setValue(mdField, MdWebCharacterInfo.DISPLAY_LENGTH, attributes, XMLTags.DISPLAY_LENGTH);
      ImportManager.setValue(mdField, MdWebCharacterInfo.MAX_LENGTH, attributes, XMLTags.MAX_LENGTH);
      ImportManager.setValue(mdField, MdWebCharacterInfo.UNIQUE, attributes, XMLTags.UNIQUE);
    }
    else if (localName.equals(XMLTags.DATE_TAG))
    {
      mdField = importMdWebAttribute(attributes, MdWebDateInfo.CLASS);
    }
    else if (localName.equals(XMLTags.DOUBLE_TAG))
    {
      mdField = importMdWebAttribute(attributes, MdWebDoubleInfo.CLASS);
    }
    else if (localName.equals(XMLTags.FLOAT_TAG))
    {
      mdField = importMdWebAttribute(attributes, MdWebFloatInfo.CLASS);
    }
    else if (localName.equals(XMLTags.INTEGER_TAG))
    {
      mdField = importMdWebAttribute(attributes, MdWebIntegerInfo.CLASS);
    }
    else if (localName.equals(XMLTags.LONG_TAG))
    {
      mdField = importMdWebAttribute(attributes, MdWebLongInfo.CLASS);
    }
    else if (localName.equals(XMLTags.BOOLEAN_TAG))
    {
      mdField = importMdWebAttribute(attributes, MdWebBooleanInfo.CLASS);
    }
    else if (localName.equals(XMLTags.TEXT_TAG))
    {
      mdField = importMdWebAttribute(attributes, MdWebTextInfo.CLASS);
    }
    else if (localName.equals(XMLTags.REFERENCE_TAG))
    {
      mdField = importMdWebAttribute(attributes, MdWebReferenceInfo.CLASS);
    }

    this.apply(mdField);
  }

  private MdFieldDAO importMdWebField(Attributes attributes, String type)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

    MdFieldDAO mdField = manager.getMdField(mdForm, name, type);

    if (! ( mdField.getType().equals(type) ))
    {
      String errMsg = "The field [" + mdField.getFieldName() + "] on type [" + this.mdForm.definesType() + "] is not a [" + type + "] field.";

      throw new RuntimeException(errMsg);
    }

    ImportManager.setValue(mdField, MdWebFieldInfo.FIELD_NAME, attributes, XMLTags.NAME_ATTRIBUTE);
    ImportManager.setValue(mdField, MdWebFieldInfo.FIELD_ORDER, attributes, XMLTags.PARAMETER_ORDER_ATTRIBUTE);
    ImportManager.setValue(mdField, MdWebFieldInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setValue(mdField, MdWebFieldInfo.REQUIRED, attributes, XMLTags.REQUIRED_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdField, MdWebFieldInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdField, MdWebFieldInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);

    return mdField;
  }

  private MdFieldDAO importMdWebAttribute(Attributes attributes, String type)
  {
    MdFieldDAO mdField = this.importMdWebField(attributes, type);

    // Import optional reference attributes
    String attributeName = attributes.getValue(XMLTags.MD_ATTRIBUTE);

    if (attributeName != null)
    {
      MdClassDAOIF formMdClass = this.mdForm.getFormMdClass();
      MdAttributeDAOIF mdAttribute = formMdClass.definesAttribute(attributeName);

      // Ensure the parent class has already been defined in the database
      if (mdAttribute == null)
      {
        // The type is not defined in the database, check if it is defined
        // in the further down in the xml document.
        String[] search_tags = { XMLTags.MD_BUSINESS_TAG, XMLTags.MD_TERM_TAG };
        SearchHandler.searchEntity(manager, search_tags, XMLTags.NAME_ATTRIBUTE, formMdClass.definesType(), mdField.getKey());
      }

      mdField.setValue(MdWebPrimitiveInfo.DEFINING_MD_ATTRIBUTE, mdAttribute.getId());
    }
    return mdField;
  }

  /**
   * Checks if the attribute MdAttribute has already been defined. If the
   * MdAttribute has not already been applied then apply one, else throw an
   * exception
   * 
   * @pre mdAttribute instanceof MdAttribute
   * 
   * @param mdAttribute
   *          The MdAttribute MdAttribute to apply
   */
  private void apply(MdFieldDAO mdAttribute)
  {
    if (mdAttribute instanceof MdWebFieldDAOIF)
    {
      mdAttribute.setValue(MdWebFieldInfo.DEFINING_MD_FORM, mdForm.getId());
      mdAttribute.apply();
    }
  }

  /**
   * Sets the parameters of a MdAttribute as determined by the parse of the
   * attributes as defined by the attribute groups.
   * 
   * IMPORTANT: This method is static because it is used in RunwayGIS. If the
   * signature of this method is changed then RunwayGIS needs to be updated as
   * well.
   * 
   * @param mdAttribute
   *          The MdAttribute being created
   * @param attributes
   *          XML attributes used to populate the MdAttribute values
   */
  protected static void importField(MdWebFieldDAO mdAttribute, Attributes attributes)
  {
    // Set the Name attribute. This is always required
    mdAttribute.setValue(MdWebFieldInfo.FIELD_NAME, attributes.getValue(XMLTags.NAME_ATTRIBUTE));
    mdAttribute.setValue(MdWebFieldInfo.FIELD_ORDER, attributes.getValue(XMLTags.PARAMETER_ORDER_ATTRIBUTE));

    ImportManager.setLocalizedValue(mdAttribute, MdWebFieldInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdAttribute, MdWebFieldInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);

    ImportManager.setValue(mdAttribute, MdWebFieldInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);

  }

  /**
   * Passes parsing control back to the previous handler on the close of an
   * attributes tag Inherits from ContentHandler (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String fullName) throws SAXException
  {
    if (localName.equals(XMLTags.FIELDS_TAG))
    {
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
  }
}
