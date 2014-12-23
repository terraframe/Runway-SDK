/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.EntityTypes;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeClobInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeDecInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDimensionInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFileInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeHashInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalTextInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.MdAttributeMultiTermInfo;
import com.runwaysdk.constants.MdAttributeNumberInfo;
import com.runwaysdk.constants.MdAttributePrimitiveInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeSymmetricInfo;
import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.MdAttributeVirtualInfo;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.MdAttributeRefDAOIF;
import com.runwaysdk.dataaccess.MdAttributeVirtualDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.attributes.InvalidAttributeTypeException;
import com.runwaysdk.dataaccess.attributes.InvalidReferenceException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLException;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdAttributeBlobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeClobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDecimalDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFileDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeHashDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeStructDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeSymmetricDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeVirtualDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdLocalStructDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

/**
 * @author Justin Smethie
 * @date 6/01/06
 */
public class MdAttributeHandler extends XMLHandler
{
  private static Map<String, PluginIF> pluginMap = new ConcurrentHashMap<String, PluginIF>();

  public static void registerPlugin(PluginIF pluginFactory)
  {
    pluginMap.put(pluginFactory.getModuleIdentifier(), pluginFactory);
  }

  /**
   * The mdClass on which the mdAttributes are being defined
   */
  private MdClassDAO mdClass;

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
   * @param mdClass
   *          The MdClass on which the MdAttribute is defined
   */
  public MdAttributeHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager, MdClassDAO mdClass)
  {
    super(reader, previousHandler, manager);

    this.mdClass = mdClass;
  }

  /**
   * Handles all the attribute tags, see datatype.xsd for a complete list
   * Inherits from ContentHandler (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
   *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  @Override
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    MdAttributeDAO mdAttribute = null;

    // Delegates elements tag to methods
    if (localName.equals(XMLTags.BOOLEAN_TAG))
    {
      mdAttribute = importBoolean(attributes);
    }
    else if (localName.equals(XMLTags.CHARACTER_TAG))
    {
      mdAttribute = importChar(attributes);
    }
    else if (localName.equals(XMLTags.LOCAL_CHARACTER_TAG))
    {
      mdAttribute = importLocalCharacter(attributes);
    }
    else if (localName.equals(XMLTags.TEXT_TAG))
    {
      mdAttribute = importText(attributes);
    }
    else if (localName.equals(XMLTags.CLOB_TAG))
    {
      mdAttribute = importClob(attributes);
    }
    else if (localName.equals(XMLTags.LOCAL_TEXT_TAG))
    {
      mdAttribute = importLocalText(attributes);
    }
    else if (localName.equals(XMLTags.INTEGER_TAG))
    {
      mdAttribute = importInteger(attributes);
    }
    else if (localName.equals(XMLTags.LONG_TAG))
    {
      mdAttribute = importLong(attributes);
    }
    else if (localName.equals(XMLTags.FLOAT_TAG))
    {
      mdAttribute = importFloat(attributes);
    }
    else if (localName.equals(XMLTags.DOUBLE_TAG))
    {
      mdAttribute = importDouble(attributes);
    }
    else if (localName.equals(XMLTags.DECIMAL_TAG))
    {
      mdAttribute = importDecimal(attributes);
    }
    else if (localName.equals(XMLTags.TIME_TAG))
    {
      mdAttribute = importTime(attributes);
    }
    else if (localName.equals(XMLTags.DATETIME_TAG))
    {
      mdAttribute = importDateTime(attributes);
    }
    else if (localName.equals(XMLTags.DATE_TAG))
    {
      mdAttribute = importDate(attributes);
    }
    else if (localName.equals(XMLTags.STRUCT_TAG))
    {
      mdAttribute = importStruct(attributes);
    }
    else if (localName.equals(XMLTags.REFERENCE_TAG))
    {
      mdAttribute = importReference(attributes);
    }
    else if (localName.equals(XMLTags.TERM_TAG))
    {
      mdAttribute = importTerm(attributes);
    }
    else if (localName.equals(XMLTags.ENUMERATION_TAG))
    {
      mdAttribute = importEnumeration(attributes);
    }
    else if (localName.equals(XMLTags.MULTI_REFERENCE_TAG))
    {
      mdAttribute = importMultiReference(attributes);
    }
    else if (localName.equals(XMLTags.MULTI_TERM_TAG))
    {
      mdAttribute = importMultiTerm(attributes);
    }
    else if (localName.equals(XMLTags.SYMMETRIC_TAG))
    {
      mdAttribute = importSymmetricEncryption(attributes);
    }
    else if (localName.equals(XMLTags.HASH_TAG))
    {
      mdAttribute = importHashEncryption(attributes);
    }
    else if (localName.equals(XMLTags.BLOB_TAG))
    {
      mdAttribute = importBlob(attributes);
    }
    else if (localName.equals(XMLTags.FILE_TAG))
    {
      mdAttribute = importFile(attributes);
    }
    else if (localName.equals(XMLTags.VIRTUAL_TAG))
    {
      mdAttribute = importVirtual(attributes);
    }

    if (mdAttribute == null)
    {
      for (PluginIF plugin : pluginMap.values())
      {
        mdAttribute = plugin.startElement(manager, mdClass, namespaceURI, localName, fullName, attributes);

        if (mdAttribute != null)
        {
          break;
        }
      }
    }

    this.apply(mdAttribute);

    // Import REQIRED for dimension
    String requiredForDimension = attributes.getValue(XMLTags.REQUIRED_FOR_DIMENSION_ATTRIBUTE);

    if (requiredForDimension != null && requiredForDimension.length() > 0)
    {
      String[] dimensions = requiredForDimension.split(",");

      for (String dimension : dimensions)
      {
        MdDimensionDAOIF mdDimension = MdDimensionDAO.getByName(dimension.trim());
        MdAttributeDimensionDAOIF mdAttributeDimensionIF = mdAttribute.getMdAttributeDimension(mdDimension);

        MdAttributeDimensionDAO mdAttributeDimension = mdAttributeDimensionIF.getBusinessDAO();
        mdAttributeDimension.setValue(MdAttributeDimensionInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
        mdAttributeDimension.apply();
      }
    }
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
  private void apply(MdAttributeDAO mdAttribute)
  {
    if (mdAttribute instanceof MdAttributeConcreteDAOIF)
    {
      mdAttribute.setValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS, mdClass.getId());
      mdAttribute.apply();
    }
    else if (mdAttribute instanceof MdAttributeVirtualDAOIF && mdClass instanceof MdViewDAO)
    {
      try
      {
        mdAttribute.setValue(MdAttributeVirtualInfo.DEFINING_MD_VIEW, mdClass.getId());
        mdAttribute.apply();
      }
      catch (InvalidReferenceException e)
      {
        String errMsg = "Attribute [" + mdAttribute.getDisplayLabel(CommonProperties.getDefaultLocale()) + "] on type [" + mdClass.definesType() + "] does not reference a valid [" + MdAttributeConcreteInfo.CLASS + "]";
        throw new InvalidReferenceException(errMsg, e.getMdAttributeReference());
      }
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
  public static void importAttributes(MdAttributeConcreteDAO mdAttribute, Attributes attributes)
  {
    // Set the Name attribute. This is always required
    mdAttribute.setValue(MdAttributeConcreteInfo.NAME, attributes.getValue(XMLTags.NAME_ATTRIBUTE));

    String columnName = attributes.getValue(XMLTags.COLUMN_ATTRIBUTE);
    if (columnName != null && columnName.trim().length() != 0)
    {
      mdAttribute.setColumnName(columnName);
    }

    ImportManager.setLocalizedValue(mdAttribute, MdAttributeConcreteInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdAttribute, MdAttributeConcreteInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);

    ImportManager.setValue(mdAttribute, MdAttributeConcreteInfo.DEFAULT_VALUE, attributes, XMLTags.DEFAULT_VALUE_ATTRIBUTE);
    ImportManager.setValue(mdAttribute, MdAttributeConcreteInfo.REQUIRED, attributes, XMLTags.REQUIRED_ATTRIBUTE);
    ImportManager.setValue(mdAttribute, MdAttributeConcreteInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setValue(mdAttribute, MdAttributeConcreteInfo.IMMUTABLE, attributes, XMLTags.IMMUTABLE_ATTRIBUTE);

    importIndexType(mdAttribute, attributes.getValue(XMLTags.INDEX_TYPE_ATTRIBUTE));
    importVisibility(mdAttribute, attributes.getValue(XMLTags.GETTER_VISIBILITY_ATTRIBUTE), MdAttributeConcreteInfo.GETTER_VISIBILITY);
    importVisibility(mdAttribute, attributes.getValue(XMLTags.SETTER_VISIBILITY_ATTRIBUTE), MdAttributeConcreteInfo.SETTER_VISIBILITY);

    String rename = attributes.getValue(XMLTags.RENAME_ATTRIBUTE);

    if (rename != null && rename.length() > 0)
    {
      mdAttribute.setValue(MdAttributeConcreteInfo.NAME, rename);
    }
  }

  private static void importIndexType(MdAttributeConcreteDAO mdAttribute, String indexType)
  {
    if (indexType != null)
    {
      // Default to non unique indexing
      String indexingId = IndexTypes.NON_UNIQUE_INDEX.getId();

      // Change to an everything caching algorithm
      if (indexType.equals(XMLTags.UNIQUE_INDEX_ENUMERATION))
      {
        indexingId = IndexTypes.UNIQUE_INDEX.getId();
      }
      // Change to a nonthing caching algorithm
      else if (indexType.equals(XMLTags.NO_INDEX_ENUMERATION))
      {
        indexingId = IndexTypes.NO_INDEX.getId();
      }

      mdAttribute.addItem(MdAttributeConcreteInfo.INDEX_TYPE, indexingId);
    }
  }

  private static void importVisibility(MdAttributeConcreteDAO mdAttribute, String value, String attributeName)
  {
    if (value != null && value.equals(XMLTags.PUBLIC_VISIBILITY_ENUMERATION))
    {
      mdAttribute.addItem(attributeName, VisibilityModifier.PUBLIC.getId());
    }
    else if (value != null && value.equals(XMLTags.PROTECTED_VISIBILITY_ENUMERATION))
    {
      mdAttribute.addItem(attributeName, VisibilityModifier.PROTECTED.getId());
    }
  }

  /**
   * Imports the attributes common to all MdAttributeNumber: rejectPositive,
   * rejectNegative, rejectZero
   * 
   * @param mdAttribute
   *          The MdAttribute being created
   * @param attributes
   *          The XML attributes of the tag.
   */
  private void importNumber(MdAttributeConcreteDAO mdAttribute, Attributes attributes)
  {
    ImportManager.setValue(mdAttribute, MdAttributeNumberInfo.REJECT_NEGATIVE, attributes, XMLTags.REJECT_NEGATIVE_ATTRIBUTE);
    ImportManager.setValue(mdAttribute, MdAttributeNumberInfo.REJECT_POSITIVE, attributes, XMLTags.REJECT_POSITIVE_ATTRIBUTE);
    ImportManager.setValue(mdAttribute, MdAttributeNumberInfo.REJECT_ZERO, attributes, XMLTags.REJECT_ZERO_ATTRIBUTE);

    this.importPrimitive(mdAttribute, attributes);
  }

  /**
   * Sets the parameters of a MdAttribute as determined by the parse of the
   * attributes as defined by the dec attribute group
   * 
   * @param mdAttribute
   *          The MdAttribute being created
   * @param attributes
   *          The XML attributes of the tag.
   */
  private void importDec(MdAttributeConcreteDAO mdAttribute, Attributes attributes)
  {
    ImportManager.setValue(mdAttribute, MdAttributeDecInfo.LENGTH, attributes, XMLTags.LENGTH_ATTRIBUTE);
    ImportManager.setValue(mdAttribute, MdAttributeDecInfo.DECIMAL, attributes, XMLTags.DECIMAL_ATTRIBUTE);

    this.importNumber(mdAttribute, attributes);
  }

  /**
   * Sets the parameters of a boolean MdAttribute based upon the tag attributes
   * 
   * @param attributes
   *          The attributes of a boolean tag
   * @return A new MdAttribute
   */
  private MdAttributeConcreteDAO importBoolean(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String type = MdAttributeBooleanInfo.CLASS;

    MdAttributeDAO mdAttribute = manager.getMdAttribute(mdClass, name, type);

    if (! ( mdAttribute instanceof MdAttributeBooleanDAO ))
    {
      String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + this.mdClass.definesType() + "] is not a boolean attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeBooleanInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeBooleanDAO mdAttributeBoolean = (MdAttributeBooleanDAO) mdAttribute;

    ImportManager.setLocalizedValue(mdAttribute, MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, attributes, XMLTags.POSITIVE_DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdAttribute, MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, attributes, XMLTags.NEGATIVE_DISPLAY_LABEL_ATTRIBUTE);

    // Standard attributes
    importPrimitive(mdAttributeBoolean, attributes);

    return mdAttributeBoolean;
  }

  /**
   * Sets the parameters of a boolean MdAttribute based upon the tag attributes
   * 
   * @param attributes
   *          The attributes of a boolean tag
   * @return A new MdAttribute
   */
  protected void importPrimitive(MdAttributeConcreteDAO mdAttribute, Attributes attributes)
  {
    ImportManager.setValue(mdAttribute, MdAttributePrimitiveInfo.IS_EXPRESSION, attributes, XMLTags.IS_EXPRESSION);
    ImportManager.setValue(mdAttribute, MdAttributePrimitiveInfo.EXPRESSION, attributes, XMLTags.EXPRESSION);

    // Standard attributes
    importAttributes(mdAttribute, attributes);
  }

  /**
   * Sets the parameters of a character MdAttribute based upon the tag
   * attributes
   * 
   * @param attributes
   *          The attributes of a char tag
   * @return A new MdAttribute
   */
  private MdAttributeConcreteDAO importChar(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String type = MdAttributeCharacterInfo.CLASS;

    MdAttributeDAO mdAttribute = manager.getMdAttribute(mdClass, name, type);

    if (! ( mdAttribute instanceof MdAttributeCharacterDAO ))
    {
      String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + this.mdClass.definesType() + "] is not a character attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeCharacterInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeCharacterDAO mdAttributeCharacter = (MdAttributeCharacterDAO) mdAttribute;

    ImportManager.setValue(mdAttributeCharacter, MdAttributeCharacterInfo.SIZE, attributes, XMLTags.SIZE_ATTRIBUTE);

    importPrimitive(mdAttributeCharacter, attributes);

    return mdAttributeCharacter;
  }

  /**
   * Sets the parameters of a text MdAttribute based upon the tag attributes
   * 
   * @param attributes
   *          The attributes of a text tag
   * @return A new MdAttribute
   */
  private MdAttributeConcreteDAO importText(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String type = MdAttributeTextInfo.CLASS;

    MdAttributeDAO mdAttribute = manager.getMdAttribute(mdClass, name, type);

    if (! ( mdAttribute instanceof MdAttributeTextDAO ))
    {
      String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + this.mdClass.definesType() + "] is not a text attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeTextInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeTextDAO mdAttributeTextDAO = (MdAttributeTextDAO) mdAttribute;

    importPrimitive(mdAttributeTextDAO, attributes);

    return mdAttributeTextDAO;
  }

  /**
   * Sets the parameters of a text MdAttribute based upon the tag attributes
   * 
   * @param attributes
   *          The attributes of a text tag
   * @return A new MdAttribute
   */
  private MdAttributeConcreteDAO importClob(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String type = MdAttributeClobInfo.CLASS;

    MdAttributeDAO mdAttribute = manager.getMdAttribute(mdClass, name, type);

    if (! ( mdAttribute instanceof MdAttributeClobDAO ))
    {
      String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + this.mdClass.definesType() + "] is not a clob attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeClobInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeClobDAO mdAttributeClobDAO = (MdAttributeClobDAO) mdAttribute;

    importPrimitive(mdAttributeClobDAO, attributes);

    return mdAttributeClobDAO;
  }

  /**
   * Sets the parameters of a integer MdAttribute based upon the tag attributes
   * 
   * @param attributes
   *          The attributes of a integer tag
   * @return A new MdAttribute
   */
  private MdAttributeConcreteDAO importInteger(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String type = MdAttributeIntegerInfo.CLASS;

    MdAttributeDAO mdAttribute = manager.getMdAttribute(mdClass, name, type);

    if (! ( mdAttribute instanceof MdAttributeIntegerDAO ))
    {
      String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + this.mdClass.definesType() + "] is not a integer attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeIntegerInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeIntegerDAO mdAttributeIntegerDAO = (MdAttributeIntegerDAO) mdAttribute;

    // Check Number attributes
    importNumber(mdAttributeIntegerDAO, attributes);

    return mdAttributeIntegerDAO;
  }

  /**
   * Sets the parameters of a long MdAttribute based upon the tag attributes
   * 
   * @param attributes
   *          The attributes of a long tag
   * @return A new MdAttribute
   */
  private MdAttributeConcreteDAO importLong(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String type = MdAttributeLongInfo.CLASS;

    MdAttributeDAO mdAttribute = manager.getMdAttribute(mdClass, name, type);

    if (! ( mdAttribute instanceof MdAttributeLongDAO ))
    {
      String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + this.mdClass.definesType() + "] is not a long attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeLongInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeLongDAO mdAttributeLongDAO = (MdAttributeLongDAO) mdAttribute;

    // Check Number attributes
    importNumber(mdAttributeLongDAO, attributes);

    return mdAttributeLongDAO;
  }

  /**
   * Sets the parameters of a float MdAttribute based upon the tag attributes
   * 
   * @param attributes
   *          The attributes of a float tag
   * @return A new MdAttribute
   */
  private MdAttributeConcreteDAO importFloat(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String type = MdAttributeFloatInfo.CLASS;

    MdAttributeDAO mdAttribute = manager.getMdAttribute(mdClass, name, type);

    if (! ( mdAttribute instanceof MdAttributeFloatDAO ))
    {
      String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + this.mdClass.definesType() + "] is not a float attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeFloatInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeFloatDAO mdAttributeFloatDAO = (MdAttributeFloatDAO) mdAttribute;

    // Check dec attributes
    importDec(mdAttributeFloatDAO, attributes);

    return mdAttributeFloatDAO;
  }

  /**
   * Sets the parameters of a double MdAttribute based upon the tag attributes
   * 
   * @param attributes
   *          The attributes of a double tag
   * @return A new MdAttribute
   */
  private MdAttributeConcreteDAO importDouble(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String type = MdAttributeDoubleInfo.CLASS;

    MdAttributeDAO mdAttribute = manager.getMdAttribute(mdClass, name, type);

    if (! ( mdAttribute instanceof MdAttributeDoubleDAO ))
    {
      String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + this.mdClass.definesType() + "] is not a double attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeDoubleInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeDoubleDAO mdAttributeDoubleDAO = (MdAttributeDoubleDAO) mdAttribute;

    // Check dec attributes
    importDec(mdAttributeDoubleDAO, attributes);

    return mdAttributeDoubleDAO;
  }

  /**
   * Sets the parameters of a decimal MdAttribute based upon the tag attributes
   * 
   * @param attributes
   *          The attributes of a decimal tag
   * @return A new MdAttribute
   */
  private MdAttributeConcreteDAO importDecimal(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String type = MdAttributeDecimalInfo.CLASS;

    MdAttributeDAO mdAttribute = manager.getMdAttribute(mdClass, name, type);

    if (! ( mdAttribute instanceof MdAttributeDecimalDAO ))
    {
      String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + this.mdClass.definesType() + "] is not a decimal attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeDecimalInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeDecimalDAO mdAttributeDecimalDAO = (MdAttributeDecimalDAO) mdAttribute;

    // Check dec attributes
    this.importDec(mdAttributeDecimalDAO, attributes);

    return mdAttributeDecimalDAO;
  }

  /**
   * Sets the parameters of a time MdAttribute based upon the tag attributes
   * 
   * @param attributes
   *          The attributes of a time tag
   * @return A new MdAttribute
   */
  private MdAttributeConcreteDAO importTime(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String type = MdAttributeTimeInfo.CLASS;

    MdAttributeDAO mdAttribute = manager.getMdAttribute(mdClass, name, type);

    if (! ( mdAttribute instanceof MdAttributeTimeDAO ))
    {
      String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + this.mdClass.definesType() + "] is not a time attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeTimeInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeTimeDAO mdAttributeTimeDAO = (MdAttributeTimeDAO) mdAttribute;

    // Check standard attributes
    importPrimitive(mdAttributeTimeDAO, attributes);

    return mdAttributeTimeDAO;
  }

  /**
   * Sets the parameters of a DateTime MdAttribute based upon the tag attributes
   * 
   * @param attributes
   *          The attributes of a dateTime tag
   * @return A new MdAttribute
   */
  private MdAttributeConcreteDAO importDateTime(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String type = MdAttributeDateTimeInfo.CLASS;

    MdAttributeDAO mdAttribute = manager.getMdAttribute(mdClass, name, type);

    if (! ( mdAttribute instanceof MdAttributeDateTimeDAO ))
    {
      String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + this.mdClass.definesType() + "] is not a date time attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeDateTimeInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeDateTimeDAO mdAttributeDateTime = (MdAttributeDateTimeDAO) mdAttribute;

    // Check standard values
    importPrimitive(mdAttributeDateTime, attributes);

    return mdAttributeDateTime;
  }

  /**
   * Sets the parameters of a Date MdAttribute based upon the tag attributes
   * 
   * @param attributes
   *          The attributes of a date tag
   * @return A new MdAttribute
   */
  private MdAttributeConcreteDAO importDate(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String type = MdAttributeDateInfo.CLASS;

    MdAttributeDAO mdAttribute = manager.getMdAttribute(mdClass, name, type);

    if (! ( mdAttribute instanceof MdAttributeDateDAO ))
    {
      String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + this.mdClass.definesType() + "] is not a date attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeDateInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeDateDAO mdAttributeDate = (MdAttributeDateDAO) mdAttribute;

    // Check local values
    importPrimitive(mdAttributeDate, attributes);

    return mdAttributeDate;
  }

  /**
   * Sets the parameters of a struct MdAttribute based upon the tag attributes
   * 
   * @param attributes
   *          The attributes of a struct tag
   * @return A new MdAttribute
   */
  private MdAttributeConcreteDAO importStruct(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String type = MdAttributeStructInfo.CLASS;

    MdAttributeDAO mdAttribute = manager.getMdAttribute(mdClass, name, type);

    if (! ( mdAttribute instanceof MdAttributeStructDAO ))
    {
      String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + this.mdClass.definesType() + "] is not a struct attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeStructInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeStructDAO mdAttributeStructDAO = (MdAttributeStructDAO) mdAttribute;

    // Check standard attributes
    importAttributes(mdAttributeStructDAO, attributes);

    importStructType(mdAttribute, attributes.getValue(XMLTags.TYPE_ATTRIBUTE));

    return mdAttributeStructDAO;
  }

  private void importStructType(MdAttributeDAO mdAttribute, String structType)
  {
    if (structType != null)
    {
      // Ensure that the class being reference is defined in the database
      if (!MdTypeDAO.isDefined(structType))
      {
        String[] search_tags = { XMLTags.MD_STRUCT_TAG };
        SearchHandler.searchEntity(manager, search_tags, XMLTags.NAME_ATTRIBUTE, structType, mdClass.definesType());
      }

      // Get the databaseID of the enumeration reference
      MdStructDAOIF refMdStructIF = MdStructDAO.getMdStructDAO(structType);

      // Reference to a struct class
      mdAttribute.setValue(MdAttributeStructInfo.MD_STRUCT, refMdStructIF.getId());
    }
  }

  /**
   * Sets the parameters of a local character MdAttribute based upon the tag
   * attributes
   * 
   * @param attributes
   *          The attributes of a struct tag
   * @return A new MdAttribute
   */
  private MdAttributeConcreteDAO importLocalCharacter(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String type = MdAttributeLocalCharacterInfo.CLASS;

    MdAttributeDAO mdAttribute = manager.getMdAttribute(mdClass, name, type);

    if (! ( mdAttribute instanceof MdAttributeLocalCharacterDAO ))
    {
      String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + this.mdClass.definesType() + "] is not a local character attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeLocalCharacterInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeLocalCharacterDAO mdAttributeLocalCharacterDAO = (MdAttributeLocalCharacterDAO) mdAttribute;

    // Check standard attributes
    importAttributes(mdAttributeLocalCharacterDAO, attributes);

    String localType = attributes.getValue(XMLTags.TYPE_ATTRIBUTE);

    importLocalType(mdAttribute, localType);

    return mdAttributeLocalCharacterDAO;
  }

  private void importLocalType(MdAttributeDAO mdAttribute, String localType)
  {
    if (localType != null)
    {
      // Ensure that the class being reference is defined in the database
      if (!MdTypeDAO.isDefined(localType))
      {
        String[] search_tags = { XMLTags.MD_LOCAL_STRUCT_TAG };
        SearchHandler.searchEntity(manager, search_tags, XMLTags.NAME_ATTRIBUTE, localType, mdClass.definesType());
      }

      // Get the databaseID of the enumeration reference
      MdLocalStructDAOIF refMdLocalStructIF = MdLocalStructDAO.getMdLocalStructDAO(localType);

      // Reference to a struct class
      mdAttribute.setValue(MdAttributeLocalCharacterInfo.MD_STRUCT, refMdLocalStructIF.getId());
    }

  }

  /**
   * Sets the parameters of a local text MdAttribute based upon the tag
   * attributes
   * 
   * @param attributes
   *          The attributes of a struct tag
   * @return A new MdAttribute
   */
  private MdAttributeConcreteDAO importLocalText(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String type = MdAttributeLocalTextInfo.CLASS;

    MdAttributeDAO mdAttribute = manager.getMdAttribute(mdClass, name, type);

    if (! ( mdAttribute instanceof MdAttributeLocalTextDAO ))
    {
      String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + this.mdClass.definesType() + "] is not a local text attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeLocalTextInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeLocalTextDAO mdAttributeLocalTextDAO = (MdAttributeLocalTextDAO) mdAttribute;

    // Check standard attributes
    importAttributes(mdAttributeLocalTextDAO, attributes);

    importLocalType(mdAttribute, attributes.getValue(XMLTags.TYPE_ATTRIBUTE));

    return mdAttributeLocalTextDAO;
  }

  /**
   * Sets the parameters of a reference MdAttribute based upon the tag
   * attributes Not yet supported by the datatypes.xsd
   * 
   * @param attributes
   *          The attributes of a reference tag
   * @return A new MdAttribute
   */
  private MdAttributeConcreteDAO importReference(Attributes attributes)
  {
    return this.importReference(attributes, MdAttributeReferenceInfo.CLASS);
  }

  private MdAttributeConcreteDAO importTerm(Attributes attributes)
  {
    return this.importReference(attributes, MdAttributeTermInfo.CLASS);
  }

  /**
   * @param attributes
   * @param mdAttribute
   * @return
   */
  private MdAttributeConcreteDAO importReference(Attributes attributes, String type)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

    MdAttributeDAO mdAttribute = manager.getMdAttribute(mdClass, name, type);

    if (! ( mdAttribute instanceof MdAttributeReferenceDAO ))
    {
      String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + this.mdClass.definesType() + "] is not a reference attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeReferenceInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeReferenceDAO mdAttributeReferenceDAO = (MdAttributeReferenceDAO) mdAttribute;

    importAttributes(mdAttributeReferenceDAO, attributes);

    importReferenceType(mdAttribute, attributes.getValue(XMLTags.TYPE_ATTRIBUTE));

    importReferenceDefaultValue(mdAttribute, attributes.getValue(XMLTags.DEFAULT_KEY_ATTRIBUTE));

    return mdAttributeReferenceDAO;
  }

  private MdAttributeConcreteDAO importMultiReference(Attributes attributes)
  {
    return this.importMultiReference(attributes, MdAttributeMultiReferenceInfo.CLASS);
  }

  private MdAttributeConcreteDAO importMultiTerm(Attributes attributes)
  {
    return this.importMultiReference(attributes, MdAttributeMultiTermInfo.CLASS);
  }

  /**
   * @param attributes
   * @param mdAttribute
   * @return
   */
  private MdAttributeConcreteDAO importMultiReference(Attributes attributes, String type)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

    MdAttributeDAO mdAttribute = manager.getMdAttribute(mdClass, name, type);

    if (! ( mdAttribute instanceof MdAttributeMultiReferenceDAO ))
    {
      String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + this.mdClass.definesType() + "] is not a reference attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeReferenceInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeMultiReferenceDAO mdAttributeReferenceDAO = (MdAttributeMultiReferenceDAO) mdAttribute;

    importAttributes(mdAttributeReferenceDAO, attributes);

    importReferenceType(mdAttribute, attributes.getValue(XMLTags.TYPE_ATTRIBUTE));

    importReferenceDefaultValue(mdAttribute, attributes.getValue(XMLTags.DEFAULT_KEY_ATTRIBUTE));

    return mdAttributeReferenceDAO;
  }

  private void importReferenceDefaultValue(MdAttributeDAO mdAttribute, String defaultValue)
  {
    if (defaultValue != null)
    {
      String referenceType = ( (MdAttributeRefDAOIF) mdAttribute ).getReferenceMdBusinessDAO().definesType();

      String id = "";

      try
      {
        id = EntityDAO.getIdFromKey(referenceType, defaultValue);
      }
      catch (DataNotFoundException e)
      {
        SearchCriteriaIF criteria = new EntitySearchCriteria(referenceType, defaultValue, XMLTags.OBJECT_TAG);

        SearchHandler.searchEntity(manager, criteria, mdClass.definesType());
      }

      if (id.equals(""))
      {
        id = EntityDAO.getIdFromKey(referenceType, defaultValue);
      }

      mdAttribute.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE, id);
    }
  }

  private void importReferenceType(MdAttributeDAO mdAttribute, String referenceType)
  {
    if (referenceType != null)
    {
      // Ensure that the class being reference is defined in the database
      if (!MdTypeDAO.isDefined(referenceType))
      {
        String[] search_tags = { XMLTags.MD_BUSINESS_TAG, XMLTags.MD_STRUCT_TAG, XMLTags.MD_TERM_TAG };
        SearchHandler.searchEntity(manager, search_tags, XMLTags.NAME_ATTRIBUTE, referenceType, mdClass.definesType());
      }

      // Get the databaseID of the enumeration reference
      MdBusinessDAOIF refMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(referenceType);
      mdAttribute.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, refMdBusinessIF.getId());
    }
  }

  /**
   * Sets the parameters of a enumeration MdAttribute based upon the tag
   * attributes
   * 
   * @param attributes
   *          The attributes of a enumeration tag
   * @return A new MdAttribute
   */
  private MdAttributeConcreteDAO importEnumeration(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String type = MdAttributeEnumerationInfo.CLASS;

    MdAttributeDAO mdAttribute = manager.getMdAttribute(mdClass, name, type);

    if (! ( mdAttribute instanceof MdAttributeEnumerationDAO ))
    {
      String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + this.mdClass.definesType() + "] is not a enumeration attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeEnumerationInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeEnumerationDAO mdAttributeEnumerationDAO = (MdAttributeEnumerationDAO) mdAttribute;

    // Import all of the standard attributes
    importAttributes(mdAttributeEnumerationDAO, attributes);

    // NOTE: enumeration type must be imported before the default value
    // because the enumeration type is required in order to dereference
    // the actual id of the default value.
    importEnumerationType(mdAttributeEnumerationDAO, attributes.getValue(XMLTags.TYPE_ATTRIBUTE));

    importEnumerationDefaultValue(mdAttributeEnumerationDAO, attributes.getValue(XMLTags.DEFAULT_VALUE_ATTRIBUTE));

    // Parse the multipleSelect attribute
    ImportManager.setValue(mdAttributeEnumerationDAO, MdAttributeEnumerationInfo.SELECT_MULTIPLE, attributes, XMLTags.SELECT_MULTIPLE_ATTRIBUTE, "true");

    return mdAttributeEnumerationDAO;
  }

  private void importEnumerationDefaultValue(MdAttributeEnumerationDAO mdAttributeEnumerationDAO, String defaultValue)
  {
    if (defaultValue != null)
    {
      // The value is the enumeration item name
      String masterListType = mdAttributeEnumerationDAO.getMdEnumerationDAO().getMasterListMdBusinessDAO().definesType();
      String enumerationItemKey = EnumerationItemDAO.buildKey(masterListType, defaultValue);

      String id = "";

      try
      {
        id = EntityDAO.getIdFromKey(masterListType, enumerationItemKey);
      }
      catch (DataNotFoundException e)
      {
        SearchCriteriaIF criteria = new EntitySearchCriteria(masterListType, enumerationItemKey, XMLTags.OBJECT_TAG);

        SearchHandler.searchEntity(manager, criteria, mdClass.definesType());
      }

      if (id.equals(""))
      {
        id = EntityDAO.getIdFromKey(masterListType, enumerationItemKey);
      }

      mdAttributeEnumerationDAO.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, id);
    }
  }

  private void importEnumerationType(MdAttributeEnumerationDAO mdAttributeEnumerationDAO, String mdEnumerationType)
  {
    if (mdEnumerationType != null)
    {
      // Ensure that the enumeration_filter has already been defined
      if (!MdTypeDAO.isDefined(mdEnumerationType))
      {
        String[] search_tags = { XMLTags.MD_ENUMERATION_TAG };
        SearchHandler.searchEntity(manager, search_tags, XMLTags.NAME_ATTRIBUTE, mdEnumerationType, mdClass.definesType());
      }

      MdEnumerationDAOIF mdEnumerationIF = MdEnumerationDAO.getMdEnumerationDAO(mdEnumerationType);
      mdAttributeEnumerationDAO.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, mdEnumerationIF.getId());
    }
  }

  /**
   * Sets the parameters of a MdAttributeSymmetric MdAttribute based upon the
   * tag attributes
   * 
   * @param attributes
   *          The attributes of a struct tag
   * @return A new MdAttributeSymmetric MdAttribute
   */
  /**
   * @param attributes
   * @return
   */
  private MdAttributeConcreteDAO importSymmetricEncryption(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String type = MdAttributeSymmetricInfo.CLASS;

    MdAttributeDAO mdAttribute = manager.getMdAttribute(mdClass, name, type);

    if (! ( mdAttribute instanceof MdAttributeSymmetricDAO ))
    {
      String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + this.mdClass.definesType() + "] is not a symmetric attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeSymmetricInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeSymmetricDAO mdAttributeSymmetricDAO = (MdAttributeSymmetricDAO) mdAttribute;

    // Check standard attributes
    importAttributes(mdAttributeSymmetricDAO, attributes);

    // Parse the referenceName attribute
    String symmetricMethod = attributes.getValue(XMLTags.SYMMETRIC_METHOD_ATTRIBUTE);

    importSymmetricMethod(mdAttributeSymmetricDAO, symmetricMethod);

    ImportManager.setValue(mdAttributeSymmetricDAO, MdAttributeSymmetricInfo.SECRET_KEY_SIZE, attributes, XMLTags.SYMMETRIC_KEYSIZE_ATTRIBUTE);

    return mdAttributeSymmetricDAO;
  }

  private void importSymmetricMethod(MdAttributeDAO mdAttribute, String symmetricMethod)
  {
    if (symmetricMethod != null)
    {
      // Find the id associated with the of the symmetric method
      QueryFactory qFactory = new QueryFactory();
      BusinessDAOQuery symMethodQ = qFactory.businessDAOQuery(EntityTypes.SYMMETRIC_METHOD.getType());
      symMethodQ.WHERE(symMethodQ.aCharacter(EnumerationMasterInfo.NAME).EQ(symmetricMethod));
      OIterator<BusinessDAOIF> symMethodIterator = symMethodQ.getIterator();

      // If the encryption method has been defined
      if (symMethodIterator.hasNext())
      {
        String methodId = symMethodIterator.next().getId();

        // Set the hash value
        mdAttribute.addItem(MdAttributeSymmetricInfo.SYMMETRIC_METHOD, methodId);
        symMethodIterator.close();
      }
      else
      {
        symMethodIterator.close();

        String error = "The symmetric encryption method [" + symmetricMethod + "] is not defined in the symmetric methods enumeration";
        throw new XMLException(error);
      }
    }
  }

  /**
   * Sets the parameters of a MdAttributeHash MdAttribute based upon the tag
   * attributes
   * 
   * @param attributes
   *          The attributes of a struct tag
   * @return A new MdAttributeHash MdAttribute
   */
  private MdAttributeConcreteDAO importHashEncryption(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String type = MdAttributeHashInfo.CLASS;

    MdAttributeDAO mdAttribute = manager.getMdAttribute(mdClass, name, type);

    if (! ( mdAttribute instanceof MdAttributeHashDAO ))
    {
      String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + this.mdClass.definesType() + "] is not a hash attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeHashInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeHashDAO mdAttributeHashDAO = (MdAttributeHashDAO) mdAttribute;

    // Check standard attributes
    importAttributes(mdAttributeHashDAO, attributes);

    importHashMethod(mdAttribute, attributes.getValue(XMLTags.HASH_METHOD_ATTRIBUTE));

    return mdAttributeHashDAO;
  }

  private void importHashMethod(MdAttributeDAO mdAttribute, String hashMethod)
  {
    // Parse the referenceName attribute
    if (hashMethod != null)
    {
      // Find the id associated with the name of the hash method
      QueryFactory qFactory = new QueryFactory();
      BusinessDAOQuery hashMethodQ = qFactory.businessDAOQuery(EntityTypes.HASH_METHOD.getType());
      hashMethodQ.WHERE(hashMethodQ.aCharacter(EnumerationMasterInfo.NAME).EQ(hashMethod));
      OIterator<BusinessDAOIF> hashMethodIterator = hashMethodQ.getIterator();

      // If the encryption method has been defined
      if (hashMethodIterator.hasNext())
      {
        String methodId = hashMethodIterator.next().getId();

        // Set the hash value
        mdAttribute.addItem(MdAttributeHashInfo.HASH_METHOD, methodId);
        hashMethodIterator.close();
      }
      else
      {
        hashMethodIterator.close();
        String error = "The hash encryption method [" + hashMethod + "] is not defined in the hash methods enumeration";
        throw new XMLException(error);
      }
    }
  }

  private MdAttributeConcreteDAO importBlob(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String type = MdAttributeBlobInfo.CLASS;
    MdAttributeDAO mdAttribute = manager.getMdAttribute(mdClass, name, type);

    if (! ( mdAttribute instanceof MdAttributeBlobDAO ))
    {
      String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + this.mdClass.definesType() + "] is not a blob attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeBlobInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeBlobDAO mdAttributeBlob = (MdAttributeBlobDAO) mdAttribute;

    // Check standard attributes
    importAttributes(mdAttributeBlob, attributes);

    return mdAttributeBlob;
  }

  private MdAttributeConcreteDAO importFile(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String type = MdAttributeFileInfo.CLASS;

    MdAttributeDAO mdAttribute = manager.getMdAttribute(mdClass, name, type);

    if (! ( mdAttribute instanceof MdAttributeFileDAO ))
    {
      String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + this.mdClass.definesType() + "] is not a file attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeFileInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeFileDAO mdAttributeFileDAO = (MdAttributeFileDAO) mdAttribute;

    // Check standard attributes
    importAttributes(mdAttributeFileDAO, attributes);

    return mdAttributeFileDAO;
  }

  private MdAttributeDAO importVirtual(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String type = MdAttributeVirtualInfo.CLASS;

    MdAttributeDAO mdAttribute = manager.getMdAttribute(mdClass, name, type);

    if (! ( mdAttribute instanceof MdAttributeVirtualDAO ))
    {
      String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + this.mdClass.definesType() + "] is not a virtual attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeVirtualInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeVirtualDAO mdAttributeVirtualDAO = (MdAttributeVirtualDAO) mdAttribute;

    if (name == null)
    {
      name = "";
    }

    mdAttributeVirtualDAO.setValue(MdAttributeVirtualInfo.NAME, name);

    // Set the optional values
    ImportManager.setLocalizedValue(mdAttributeVirtualDAO, MdAttributeVirtualInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);

    ImportManager.setValue(mdAttributeVirtualDAO, MdAttributeVirtualInfo.REQUIRED, attributes, XMLTags.REQUIRED_ATTRIBUTE);

    ImportManager.setLocalizedValue(mdAttributeVirtualDAO, MdAttributeVirtualInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);

    ImportManager.setValue(mdAttributeVirtualDAO, MdAttributeVirtualInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);

    // Set the optional MdAttributeConcreate value
    String concreteType = attributes.getValue(XMLTags.TYPE_ATTRIBUTE);
    String concreteName = attributes.getValue(XMLTags.CONCRETE_ATTRIBUTE);

    importConcreteAttribute(mdAttributeVirtualDAO, concreteType, concreteName);

    return mdAttributeVirtualDAO;
  }

  private void importConcreteAttribute(MdAttributeVirtualDAO mdAttributeVirtualDAO, String concreteType, String concreteName)
  {
    if (concreteName != null && concreteType != null)
    {
      if (!MdTypeDAO.isDefined(concreteType))
      {
        String[] search_tags = XMLTags.TYPE_TAGS;
        SearchHandler.searchEntity(manager, search_tags, XMLTags.NAME_ATTRIBUTE, concreteType, mdClass.definesType());
      }

      MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(concreteType);
      MdAttributeDAOIF concreteAttribute = mdClass.definesAttribute(concreteName);

      // IMPORTANT: It is possible that the concrete type is defined before this
      // schema was imported. However, the definition of the concrete attribute
      // may not be defined until an update statement in the current xml file.
      // As such it is possible to have the type defined but not have the
      // concrete attribute defined. Therefore, if the attribute is not defined
      // then we need to search and import the definition of the class which
      // exists in the current xml file.
      if (concreteAttribute == null)
      {
        String[] search_tags = XMLTags.TYPE_TAGS;
        SearchHandler.searchEntity(manager, search_tags, XMLTags.NAME_ATTRIBUTE, concreteType, mdClass.definesType());

        concreteAttribute = mdClass.definesAttribute(concreteName);
      }

      if (concreteAttribute instanceof MdAttributeConcreteDAOIF)
      {
        mdAttributeVirtualDAO.setValue(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE, concreteAttribute.getId());
      }
    }
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
    if (localName.equals(XMLTags.ATTRIBUTES_TAG))
    {
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
  }

  public static interface PluginIF
  {
    public String getModuleIdentifier();

    public MdAttributeDAO startElement(ImportManager manager, MdClassDAO mdClass, String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException;
  }
}
