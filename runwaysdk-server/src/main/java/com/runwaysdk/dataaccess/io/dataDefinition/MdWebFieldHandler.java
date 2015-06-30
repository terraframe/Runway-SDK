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

import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.constants.MdWebAttributeInfo;
import com.runwaysdk.constants.MdWebBooleanInfo;
import com.runwaysdk.constants.MdWebBreakInfo;
import com.runwaysdk.constants.MdWebCharacterInfo;
import com.runwaysdk.constants.MdWebCommentInfo;
import com.runwaysdk.constants.MdWebDateInfo;
import com.runwaysdk.constants.MdWebDateTimeInfo;
import com.runwaysdk.constants.MdWebDecInfo;
import com.runwaysdk.constants.MdWebDecimalInfo;
import com.runwaysdk.constants.MdWebDoubleInfo;
import com.runwaysdk.constants.MdWebFieldInfo;
import com.runwaysdk.constants.MdWebFloatInfo;
import com.runwaysdk.constants.MdWebGeoInfo;
import com.runwaysdk.constants.MdWebGroupInfo;
import com.runwaysdk.constants.MdWebHeaderInfo;
import com.runwaysdk.constants.MdWebIntegerInfo;
import com.runwaysdk.constants.MdWebLongInfo;
import com.runwaysdk.constants.MdWebMultipleTermInfo;
import com.runwaysdk.constants.MdWebNumberInfo;
import com.runwaysdk.constants.MdWebPrimitiveInfo;
import com.runwaysdk.constants.MdWebReferenceInfo;
import com.runwaysdk.constants.MdWebSingleTermGridInfo;
import com.runwaysdk.constants.MdWebSingleTermInfo;
import com.runwaysdk.constants.MdWebTextInfo;
import com.runwaysdk.constants.MdWebTimeInfo;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdWebFieldDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdFieldDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFieldDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFormDAO;
import com.runwaysdk.dataaccess.metadata.MdWebSingleTermGridDAO;

public class MdWebFieldHandler extends XMLHandler implements ConditionListIF
{
  /**
   * List of the conditions upon which to apply to the database. The list is
   * stored in a list because field conditions have dependencies on field
   * definitions. The list is processed after all of the fields have been
   * defined.
   */
  private List<ConditionAttributeIF> conditions;

  /**
   * List of the groups to create or update. The list is stored in a list
   * because groups have dependencies on field definitions. The list is
   * processed after all of the fields have been defined.
   */
  private List<GroupAttribute>       groups;

  /**
   * The mdClass on which the mdAttributes are being defined
   */
  private MdWebFormDAO               mdForm;

  /**
   * The current mdField which is being parsed.
   */
  private MdFieldDAO                 currentMdField;

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
  public MdWebFieldHandler(Attributes attributes, XMLReader reader, MdWebFormHandler previousHandler, ImportManager manager, MdWebFormDAO mdForm)
  {
    super(reader, previousHandler, manager);

    this.mdForm = mdForm;
    this.conditions = new LinkedList<ConditionAttributeIF>();
    this.groups = new LinkedList<GroupAttribute>();
  }

  public void addCondition(ConditionAttributeIF condition)
  {
    this.conditions.add(condition);
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
    if (localName.equals(XMLTags.CONDITION_TAG))
    {
      FieldConditionHandler aHandler = new FieldConditionHandler(reader, this, this.manager, this.mdForm, this.currentMdField);
      reader.setContentHandler(aHandler);
      reader.setErrorHandler(aHandler);
    }
    else if (localName.equals(XMLTags.GRID_FIELDS_TAG))
    {
      GridFieldHandler aHandler = new GridFieldHandler(reader, this, this.manager, (MdWebSingleTermGridDAO) this.currentMdField);
      reader.setContentHandler(aHandler);
      reader.setErrorHandler(aHandler);
    }
    else if (localName.equals(XMLTags.FIELD_GROUP_TAG))
    {
      this.groups.add(new GroupAttribute(attributes, mdForm, this.currentMdField));
    }
    else
    {
      // Delegates elements tag to methods
      if (localName.equals(XMLTags.GEO_TAG))
      {
        currentMdField = importMdWebAttribute(attributes, MdWebGeoInfo.CLASS);
      }
      else if (localName.equals(XMLTags.TERM_TAG))
      {
        currentMdField = importMdWebAttribute(attributes, MdWebSingleTermInfo.CLASS);
      }
      else if (localName.equals(XMLTags.MULTI_TERM_TAG))
      {
        currentMdField = importMdWebAttribute(attributes, MdWebMultipleTermInfo.CLASS);
      }
      else if (localName.equals(XMLTags.GRID_TAG))
      {
        currentMdField = importMdWebAttribute(attributes, MdWebSingleTermGridInfo.CLASS);
      }
      else if (localName.equals(XMLTags.CHARACTER_TAG))
      {
        currentMdField = importMdWebPrimitive(attributes, MdWebCharacterInfo.CLASS);

        ImportManager.setValue(currentMdField, MdWebCharacterInfo.DISPLAY_LENGTH, attributes, XMLTags.DISPLAY_LENGTH);
        ImportManager.setValue(currentMdField, MdWebCharacterInfo.MAX_LENGTH, attributes, XMLTags.MAX_LENGTH);
        ImportManager.setValue(currentMdField, MdWebCharacterInfo.UNIQUE, attributes, XMLTags.UNIQUE);
      }
      else if (localName.equals(XMLTags.DATE_TAG))
      {
        currentMdField = importMdWebPrimitive(attributes, MdWebDateInfo.CLASS);

        ImportManager.setValue(currentMdField, MdWebDateInfo.AFTER_TODAY_EXCLUSIVE, attributes, XMLTags.AFTER_TODAY_EXCLUSIVE);
        ImportManager.setValue(currentMdField, MdWebDateInfo.AFTER_TODAY_INCLUSIVE, attributes, XMLTags.AFTER_TODAY_INCLUSIVE);
        ImportManager.setValue(currentMdField, MdWebDateInfo.BEFORE_TODAY_EXCLUSIVE, attributes, XMLTags.BEFORE_TODAY_EXCLUSIVE);
        ImportManager.setValue(currentMdField, MdWebDateInfo.BEFORE_TODAY_INCLUSIVE, attributes, XMLTags.BEFORE_TODAY_INCLUSIVE);
        ImportManager.setValue(currentMdField, MdWebDateInfo.START_DATE, attributes, XMLTags.START_DATE);
        ImportManager.setValue(currentMdField, MdWebDateInfo.END_DATE, attributes, XMLTags.END_DATE);
      }
      else if (localName.equals(XMLTags.DATETIME_TAG))
      {
        currentMdField = importMdWebPrimitive(attributes, MdWebDateTimeInfo.CLASS);
      }
      else if (localName.equals(XMLTags.TIME_TAG))
      {
        currentMdField = importMdWebPrimitive(attributes, MdWebTimeInfo.CLASS);
      }
      else if (localName.equals(XMLTags.DECIMAL_TAG))
      {
        currentMdField = importMdWebDec(attributes, MdWebDecimalInfo.CLASS);
      }
      else if (localName.equals(XMLTags.DOUBLE_TAG))
      {
        currentMdField = importMdWebDec(attributes, MdWebDoubleInfo.CLASS);
      }
      else if (localName.equals(XMLTags.FLOAT_TAG))
      {
        currentMdField = importMdWebDec(attributes, MdWebFloatInfo.CLASS);
      }
      else if (localName.equals(XMLTags.INTEGER_TAG))
      {
        currentMdField = importMdWebNumber(attributes, MdWebIntegerInfo.CLASS);
      }
      else if (localName.equals(XMLTags.LONG_TAG))
      {
        currentMdField = importMdWebNumber(attributes, MdWebLongInfo.CLASS);
      }
      else if (localName.equals(XMLTags.BOOLEAN_TAG))
      {
        currentMdField = importMdWebPrimitive(attributes, MdWebBooleanInfo.CLASS);

        ImportManager.setValue(currentMdField, MdWebBooleanInfo.DEFAULT_VALUE, attributes, XMLTags.DEFAULT_VALUE_ATTRIBUTE);
      }
      else if (localName.equals(XMLTags.TEXT_TAG))
      {
        currentMdField = importMdWebPrimitive(attributes, MdWebTextInfo.CLASS);

        ImportManager.setValue(currentMdField, MdWebTextInfo.HEIGHT, attributes, XMLTags.HEIGHT);
        ImportManager.setValue(currentMdField, MdWebTextInfo.WIDTH, attributes, XMLTags.WIDTH);
      }
      else if (localName.equals(XMLTags.REFERENCE_TAG))
      {
        currentMdField = importMdWebAttribute(attributes, MdWebReferenceInfo.CLASS);
      }
      else if (localName.equals(XMLTags.BREAK_TAG))
      {
        currentMdField = importMdWebField(attributes, MdWebBreakInfo.CLASS);
      }
      else if (localName.equals(XMLTags.COMMENT_TAG))
      {
        currentMdField = importMdWebField(attributes, MdWebCommentInfo.CLASS);

        ImportManager.setValue(currentMdField, MdWebCommentInfo.COMMENT_TEXT, attributes, XMLTags.COMMENT_TEXT);
      }
      else if (localName.equals(XMLTags.GROUP_TAG))
      {
        currentMdField = importMdWebField(attributes, MdWebGroupInfo.CLASS);
      }
      else if (localName.equals(XMLTags.HEADER_TAG))
      {
        currentMdField = importMdWebField(attributes, MdWebHeaderInfo.CLASS);
      }

      this.apply(currentMdField);
    }
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

    ImportManager.setValue(mdField, MdWebAttributeInfo.SHOW_ON_SEARCH, attributes, XMLTags.SHOW_ON_SEARCH);
    ImportManager.setValue(mdField, MdWebAttributeInfo.SHOW_ON_VIEW_ALL, attributes, XMLTags.SHOW_ON_VIEW_ALL);

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

  private MdFieldDAO importMdWebPrimitive(Attributes attributes, String type)
  {
    MdFieldDAO mdField = this.importMdWebAttribute(attributes, type);

    ImportManager.setValue(mdField, MdWebPrimitiveInfo.IS_EXPRESSION, attributes, XMLTags.IS_EXPRESSION);
    ImportManager.setValue(mdField, MdWebPrimitiveInfo.EXPRESSION, attributes, XMLTags.EXPRESSION);

    return mdField;
  }

  private MdFieldDAO importMdWebNumber(Attributes attributes, String type)
  {
    MdFieldDAO mdField = this.importMdWebPrimitive(attributes, type);
    ImportManager.setValue(mdField, MdWebNumberInfo.STARTRANGE, attributes, XMLTags.STARTRANGE);
    ImportManager.setValue(mdField, MdWebNumberInfo.ENDRANGE, attributes, XMLTags.ENDRANGE);

    return mdField;
  }

  private MdFieldDAO importMdWebDec(Attributes attributes, String type)
  {
    MdFieldDAO mdField = this.importMdWebNumber(attributes, type);
    ImportManager.setValue(mdField, MdWebDecInfo.DECPRECISION, attributes, XMLTags.DEC_PRECISION);
    ImportManager.setValue(mdField, MdWebDecInfo.DECSCALE, attributes, XMLTags.DEC_SCALE);

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
      // Apply all of the field conditions
      for (ConditionAttributeIF condition : conditions)
      {
        condition.process();
      }

      // Apply all groups
      for (GroupAttribute group : groups)
      {
        group.process();
      }

      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
  }
}
