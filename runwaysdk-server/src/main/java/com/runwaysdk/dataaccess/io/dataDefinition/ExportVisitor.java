/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.Business;
import com.runwaysdk.business.Relationship;
import com.runwaysdk.constants.AndFieldConditionInfo;
import com.runwaysdk.constants.AssociationType;
import com.runwaysdk.constants.BasicConditionInfo;
import com.runwaysdk.constants.CharacterConditionInfo;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.CompositeFieldConditionInfo;
import com.runwaysdk.constants.DateConditionInfo;
import com.runwaysdk.constants.DoubleConditionInfo;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.LongConditionInfo;
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
import com.runwaysdk.constants.MdAttributeEmbeddedInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFileInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeGraphReferenceInfo;
import com.runwaysdk.constants.MdAttributeHashInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeJsonInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterEmbeddedInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
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
import com.runwaysdk.constants.MdAttributeUUIDInfo;
import com.runwaysdk.constants.MdAttributeVirtualInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdEntityInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdExceptionInfo;
import com.runwaysdk.constants.MdFieldInfo;
import com.runwaysdk.constants.MdIndexInfo;
import com.runwaysdk.constants.MdInformationInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdProblemInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.MdTableInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.constants.MdTermRelationshipInfo;
import com.runwaysdk.constants.MdTransientInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MdViewInfo;
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
import com.runwaysdk.constants.MdWebFloatInfo;
import com.runwaysdk.constants.MdWebFormInfo;
import com.runwaysdk.constants.MdWebGeoInfo;
import com.runwaysdk.constants.MdWebGroupInfo;
import com.runwaysdk.constants.MdWebHeaderInfo;
import com.runwaysdk.constants.MdWebIntegerInfo;
import com.runwaysdk.constants.MdWebLongInfo;
import com.runwaysdk.constants.MdWebMultipleTermInfo;
import com.runwaysdk.constants.MdWebNumberInfo;
import com.runwaysdk.constants.MdWebReferenceInfo;
import com.runwaysdk.constants.MdWebSingleTermGridInfo;
import com.runwaysdk.constants.MdWebSingleTermInfo;
import com.runwaysdk.constants.MdWebTextInfo;
import com.runwaysdk.constants.MdWebTimeInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.constants.graph.MdClassificationInfo;
import com.runwaysdk.constants.graph.MdEdgeInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.AttributeReferenceIF;
import com.runwaysdk.dataaccess.AttributeStructIF;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.CompositeFieldConditionDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.EnumerationItemDAOIF;
import com.runwaysdk.dataaccess.FieldConditionDAOIF;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEncryptionDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeGraphReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeHashDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiTermDAOIF;
import com.runwaysdk.dataaccess.MdAttributeNumberDAOIF;
import com.runwaysdk.dataaccess.MdAttributePrimitiveDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdAttributeSymmetricDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdAttributeVirtualDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdClassificationDAOIF;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdElementDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdExceptionDAOIF;
import com.runwaysdk.dataaccess.MdFieldDAOIF;
import com.runwaysdk.dataaccess.MdGraphDAOIF;
import com.runwaysdk.dataaccess.MdIndexDAOIF;
import com.runwaysdk.dataaccess.MdInformationDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.MdProblemDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.MdTableDAOIF;
import com.runwaysdk.dataaccess.MdTermDAOIF;
import com.runwaysdk.dataaccess.MdTermRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdTransientDAOIF;
import com.runwaysdk.dataaccess.MdUtilDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.MdViewDAOIF;
import com.runwaysdk.dataaccess.MdWarningDAOIF;
import com.runwaysdk.dataaccess.MdWebAttributeDAOIF;
import com.runwaysdk.dataaccess.MdWebBooleanDAOIF;
import com.runwaysdk.dataaccess.MdWebCharacterDAOIF;
import com.runwaysdk.dataaccess.MdWebCommentDAOIF;
import com.runwaysdk.dataaccess.MdWebDateDAOIF;
import com.runwaysdk.dataaccess.MdWebDecDAOIF;
import com.runwaysdk.dataaccess.MdWebFieldDAOIF;
import com.runwaysdk.dataaccess.MdWebFormDAOIF;
import com.runwaysdk.dataaccess.MdWebGroupDAOIF;
import com.runwaysdk.dataaccess.MdWebNumberDAOIF;
import com.runwaysdk.dataaccess.MdWebPrimitiveDAOIF;
import com.runwaysdk.dataaccess.MdWebSingleTermGridDAOIF;
import com.runwaysdk.dataaccess.MdWebTextDAOIF;
import com.runwaysdk.dataaccess.MetadataDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.AttributeCharacter;
import com.runwaysdk.dataaccess.attributes.entity.AttributeClob;
import com.runwaysdk.dataaccess.attributes.entity.AttributeJson;
import com.runwaysdk.dataaccess.attributes.entity.AttributeText;
import com.runwaysdk.dataaccess.graph.EdgeObjectDAOIF;
import com.runwaysdk.dataaccess.graph.VertexObjectDAOIF;
import com.runwaysdk.dataaccess.io.MarkupWriter;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;
import com.runwaysdk.dataaccess.metadata.MdLocalStructDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.MdTermRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdTreeDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFieldDAO;
import com.runwaysdk.dataaccess.metadata.MdWebPrimitiveDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.metadata.FieldConditionDAO;

public class ExportVisitor extends MarkupVisitor
{
  private static Map<String, PluginIF> pluginMap = new ConcurrentHashMap<String, PluginIF>();

  public static void registerPlugin(PluginIF pluginFactory)
  {
    pluginMap.put(pluginFactory.getModuleIdentifier(), pluginFactory);
  }

  /**
   * Writes the XML code
   */
  protected MarkupWriter               writer;

  protected ExportMetadata             metadata;

  /**
   * Flag denoting if source should be exported
   */
  protected boolean                    exportSource;

  /**
   * A list of attribute names that are masked out of the export file
   */
  private static final TreeSet<String> ATTRIBUTE_MASK = ExportVisitor.getAttriubteMask();

  /**
   * A mapping between a MdAttribute type and the xml tag
   */
  private static Map<String, String>   attributeTags  = ExportVisitor.loadMapping();

  public static void registerAttributeTags(ConcurrentHashMap<String, String> map)
  {
    attributeTags.putAll(map);
  }

  public ExportVisitor(MarkupWriter writer, ExportMetadata metadata)
  {
    this.metadata = metadata;
    this.writer = writer;
  }

  public ExportVisitor(MarkupWriter writer, boolean exportSource)
  {
    this.writer = writer;
  }

  /**
   * Delegates the export to the type specific export method, e.g.
   * exportMdBusiness
   * 
   * @param component
   *          The component to export
   */
  public void visit(ComponentIF component)
  {
    if (component instanceof MdParameterDAOIF)
    {
      // Do nothing, filter out. These are exported as part of MdBusiness,
      // MdRelationship, etc.
      return;
    }

    if (component instanceof MdBusinessDAOIF)
    {
      MdBusinessDAOIF mdBusinessIF = (MdBusinessDAOIF) component;

      // Determine if the mdBusinessIF extends ENUMERATION_MASTER
      List<MdBusinessDAOIF> superMdBusinessIFList = mdBusinessIF.getSuperClasses();
      MdBusinessDAOIF enumeration = MdBusinessDAO.getMdBusinessDAO(EnumerationMasterInfo.CLASS);

      if (superMdBusinessIFList.contains(enumeration))
      {
        visitMdBusinessEnum(mdBusinessIF);
      }
      else
      {
        visitMdBusiness(mdBusinessIF);
      }
    }
    else if (component instanceof MdTableDAOIF)
    {
      visitMdTable((MdTableDAOIF) component);
    }
    else if (component instanceof MdLocalStructDAO)
    {
      visitMdLocalStruct((MdLocalStructDAOIF) component);
    }
    else if (component instanceof MdStructDAO)
    {
      visitMdStruct((MdStructDAOIF) component);
    }
    else if (component instanceof MdRelationshipDAO)
    {
      MdRelationshipDAOIF mdRelationship = (MdRelationshipDAOIF) component;
      visitMdRelationship(mdRelationship);
    }
    // Export MdEnumerations
    else if (component instanceof MdEnumerationDAOIF)
    {
      MdEnumerationDAOIF enumeration = (MdEnumerationDAOIF) component;
      visitMdEnumeration(enumeration);
    }
    else if (component instanceof MdIndexDAOIF)
    {
      visitMdIndex((MdIndexDAOIF) component);
    }
    else if (component instanceof MdWebFormDAOIF)
    {
      visitMdWebForm((MdWebFormDAOIF) component);
    }
    else if (component instanceof MdExceptionDAOIF)
    {
      visitMdException((MdExceptionDAOIF) component);
    }
    else if (component instanceof MdProblemDAOIF)
    {
      visitMdProblem((MdProblemDAOIF) component);
    }
    else if (component instanceof MdInformationDAOIF)
    {
      visitMdInformation((MdInformationDAOIF) component);
    }
    else if (component instanceof MdWarningDAOIF)
    {
      visitMdWarning((MdWarningDAOIF) component);
    }
    else if (component instanceof MdViewDAOIF)
    {
      visitMdView((MdViewDAOIF) component);
    }
    else if (component instanceof MdUtilDAOIF)
    {
      visitMdUtil((MdUtilDAOIF) component);
    }
    else if (component instanceof MdVertexDAOIF)
    {
      visitMdVertex((MdVertexDAOIF) component);
    }
    else if (component instanceof MdClassificationDAOIF)
    {
      visitMdClassification((MdClassificationDAOIF) component);
    }
    else if (component instanceof MdEdgeDAOIF)
    {
      visitMdEdge((MdEdgeDAOIF) component);
    }
    else if (component instanceof RelationshipDAOIF)
    {
      visitRelationship((RelationshipDAOIF) component);
    }
    else if (component instanceof EdgeObjectDAOIF)
    {
      visitEdge((EdgeObjectDAOIF) component);
    }
    else if (component instanceof Relationship)
    {
      visitRelationship((RelationshipDAOIF) EntityDAO.get(component.getOid()));
    }
    else if (component instanceof VertexObjectDAOIF)
    {
      visitVertex((VertexObjectDAOIF) component);
    }
    else if (component instanceof BusinessDAOIF)
    {
      visitObject((BusinessDAOIF) component);
    }
    else if (component instanceof Business)
    {
      visitObject((BusinessDAOIF) EntityDAO.get(component.getOid()));
    }
    else
    {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * Specifies behavior upon entering a MdMethod on visit: Exports the MdMethod
   * tag only.
   * 
   * @param methodIF
   *          MdMethod being visited
   */
  protected void enterMdMethod(MdMethodDAOIF methodIF)
  {
    HashMap<String, String> attributes = new HashMap<String, String>();
    attributes.put(XMLTags.NAME_ATTRIBUTE, methodIF.getName());
    attributes.put(XMLTags.METHOD_RETURN_ATTRIBUTE, methodIF.getReturnType().getType());

    Map<String, String> localValues = methodIF.getDisplayLabels();
    writeLocaleValues(attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE, localValues);

    Map<String, String> localDescriptions = methodIF.getDescriptions();
    writeLocaleValues(attributes, XMLTags.DESCRIPTION_ATTRIBUTE, localDescriptions);

    attributes.put(XMLTags.METHOD_STATIC_ATTRIBUTE, methodIF.getValue(MdMethodInfo.IS_STATIC));

    writer.openEscapedTag(XMLTags.MD_METHOD_TAG, attributes);
  }

  /**
   * Visits a MdMethod: Export a MdMethod and its MdParameters
   * 
   * @param methodIF
   *          MdMethod to visit
   * @param mdParameters
   *          List of MdParameters defined for the MdMethod
   */
  public void visitMdMethod(MdMethodDAOIF methodIF, List<MdParameterDAOIF> mdParameters)
  {
    enterMdMethod(methodIF);

    for (MdParameterDAOIF mdParameter : mdParameters)
    {
      visitMdParameter(mdParameter);
    }

    exitMdMethod(methodIF);
  }

  /**
   * Specifies visit behavior after the MdMethod has been visited. This method
   * is likely to be overwritten in child classes.
   * 
   * @param methodIF
   *          MdMethod being visited
   */
  protected void exitMdMethod(MdMethodDAOIF methodIF)
  {
    writer.closeTag();
  }

  /**
   * Visits a MdParameter: exports the MdParameter tag
   * 
   * @param mdParameter
   *          MdParameter to visit
   */
  public void visitMdParameter(MdParameterDAOIF mdParameter)
  {
    HashMap<String, String> paramAttributes = new HashMap<String, String>();
    paramAttributes.put(XMLTags.NAME_ATTRIBUTE, mdParameter.getParameterName());
    paramAttributes.put(XMLTags.PARAMETER_ORDER_ATTRIBUTE, mdParameter.getParameterOrder());
    paramAttributes.put(XMLTags.TYPE_ATTRIBUTE, mdParameter.getParameterType().getType());

    Map<String, String> localValues = mdParameter.getDisplayLabels();
    writeLocaleValues(paramAttributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE, localValues);

    paramAttributes.put(XMLTags.DESCRIPTION_ATTRIBUTE, mdParameter.getDescription(CommonProperties.getDefaultLocale()));

    writer.writeEmptyEscapedTag(XMLTags.MD_PARAMETER_TAG, paramAttributes);
  }

  protected void enterMdWebForm(MdWebFormDAOIF mdWebForm)
  {
    HashMap<String, String> attributes = new HashMap<String, String>();
    attributes.put(XMLTags.NAME_ATTRIBUTE, mdWebForm.definesType());

    // Map<String, String> localValues = mdWebForm.getDisplayLabels();
    // writeLocaleValues(attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE,
    // localValues);
    attributes.put(XMLTags.DISPLAY_LABEL_ATTRIBUTE, mdWebForm.getDisplayLabel(Session.getCurrentLocale()));

    attributes.put(XMLTags.REMOVE_ATTRIBUTE, mdWebForm.getValue(MdWebFormInfo.REMOVE));

    Map<String, String> localDescValues = mdWebForm.getDescriptions();
    writeLocaleValues(attributes, XMLTags.DESCRIPTION_ATTRIBUTE, localDescValues);

    attributes.put(XMLTags.FORM_NAME, mdWebForm.getFormName());
    attributes.put(XMLTags.FORM_MD_CLASS, mdWebForm.getFormMdClass().definesType());

    // Write the INDEX_TAG with is parameters
    writer.openEscapedTag(XMLTags.MD_WEB_FORM_TAG, attributes);
  }

  /**
   * Visits a MdWebForm: Export a MdWebForm
   * 
   * @param mdWebForm
   *          The MdWebForm to visit
   */
  public void visitMdWebForm(MdWebFormDAOIF mdWebForm)
  {
    enterMdWebForm(mdWebForm);

    this.visitMdWebFields(mdWebForm.getAllMdFields());

    exitMdWebForm(mdWebForm);
  }

  protected void visitMdWebFields(List<? extends MdFieldDAOIF> fields)
  {
    writer.openTag(XMLTags.FIELDS_TAG);

    for (MdFieldDAOIF mdField : fields)
    {
      String tag = getTagName(mdField);
      HashMap<String, String> parameters = this.getFieldParameters(mdField);

      String conditionId = mdField.getValue(MdFieldInfo.FIELD_CONDITION);

      writer.openTag(tag, parameters);

      if (mdField instanceof MdWebSingleTermGridDAOIF)
      {
        List<RelationshipDAOIF> relationships = mdField.getChildren(RelationshipTypes.WEB_GRID_FIELD.getType());

        if (relationships.size() > 0)
        {
          writer.openTag(XMLTags.GRID_FIELDS_TAG);

          for (RelationshipDAOIF relationship : relationships)
          {
            MdWebPrimitiveDAOIF mdWebPrimitive = (MdWebPrimitiveDAOIF) MdWebPrimitiveDAO.get(relationship.getChildOid());

            String primitiveTag = getTagName(mdWebPrimitive);

            MdAttributeDAOIF mdAttribute = mdWebPrimitive.getDefiningMdAttribute();
            MdClassDAOIF mdClass = mdAttribute.getMdAttributeConcrete().definedByClass();

            HashMap<String, String> primitiveParameters = this.getFieldParameters(mdWebPrimitive);
            primitiveParameters.put(XMLTags.TYPE_ATTRIBUTE, mdClass.getKey());

            writer.writeEmptyTag(primitiveTag, primitiveParameters);
          }

          writer.closeTag();
        }
      }

      writer.openTag(XMLTags.CONDITION_TAG);
      if (conditionId != null && conditionId.length() > 0)
      {
        this.visitCondition(FieldConditionDAO.get(conditionId));
      }
      else
      {
        writer.writeEmptyTag(XMLTags.NONE_TAG);
      }
      writer.closeTag();

      if (mdField instanceof MdWebFieldDAOIF)
      {
        MdWebGroupDAOIF group = ( (MdWebFieldDAOIF) mdField ).getContainingGroup();

        HashMap<String, String> groupParameters = new HashMap<String, String>();

        if (group != null)
        {
          groupParameters.put(XMLTags.GROUP_NAME_ATTRIBUTE, group.getFieldName());
        }

        writer.writeEmptyTag(XMLTags.FIELD_GROUP_TAG, groupParameters);
      }

      writer.closeTag();
    }

    writer.closeTag();
  }

  /**
   * @param condition
   */
  private void visitCondition(FieldConditionDAOIF condition)
  {
    String conditionTag = ExportVisitor.getTagName(condition);

    if (condition instanceof CompositeFieldConditionDAOIF)
    {
      FieldConditionDAOIF firstCondition = FieldConditionDAO.get(condition.getValue(CompositeFieldConditionInfo.FIRST_CONDITION));
      FieldConditionDAOIF secondCondition = FieldConditionDAO.get(condition.getValue(CompositeFieldConditionInfo.SECOND_CONDITION));

      writer.openTag(XMLTags.AND_TAG);

      writer.openTag(XMLTags.FIRST_CONDITION_TAG);
      this.visitCondition(firstCondition);
      writer.closeTag();

      writer.openTag(XMLTags.SECOND_CONDITION_TAG);
      this.visitCondition(secondCondition);
      writer.closeTag();

      writer.closeTag();
    }
    else
    {
      HashMap<String, String> conditionParameters = this.getConditionParameters(condition);

      writer.writeEmptyEscapedTag(conditionTag, conditionParameters);
    }
  }

  /**
   * @param condition
   * @return
   */
  private HashMap<String, String> getConditionParameters(FieldConditionDAOIF condition)
  {
    MdFieldDAOIF field = MdWebFieldDAO.get(condition.getValue(BasicConditionInfo.DEFINING_MD_FIELD));

    AttributeEnumerationIF attribute = (AttributeEnumerationIF) condition.getAttributeIF(BasicConditionInfo.OPERATION);

    Set<String> itemIds = attribute.getEnumItemIdList();
    Iterator<String> it = itemIds.iterator();
    String itemId = it.next();
    String operation = EnumerationItemDAO.get(itemId).getName();

    HashMap<String, String> parameters = new HashMap<String, String>();
    parameters.put(XMLTags.FIELD_ATTRIBUTE, field.getFieldName());
    parameters.put(XMLTags.OPERATION_TAG, operation);
    parameters.put(XMLTags.VALUE_ATTRIBUTE, condition.getValue(BasicConditionInfo.VALUE));

    return parameters;
  }

  protected void exitMdWebForm(MdWebFormDAOIF mdWebForm)
  {
    writer.closeTag();
  }

  /**
   * Specifies behavior upon entering a MdBusiness on visit: Exports the
   * MdBusiness tag with its attributes.
   * 
   * @param mdBusinessIF
   *          The MdBusiness being visited
   */
  protected void enterMdBusiness(MdBusinessDAOIF mdBusinessIF)
  {
    // Get the attribute_tag-value mapping of the entity
    HashMap<String, String> attributes = getMdBusinessParameters(mdBusinessIF);

    String tagName = XMLTags.MD_BUSINESS_TAG;

    if (mdBusinessIF instanceof MdTermDAOIF)
    {
      tagName = XMLTags.MD_TERM_TAG;
    }

    // Write the CLASS_TAG with its parameters
    writer.openEscapedTag(tagName, attributes);
  }

  /**
   * Visits a MdBusiness: Export a MdBusiness
   * 
   * @param mdBusiness
   *          The MdBusiness to visit
   */
  public void visitMdBusiness(MdBusinessDAOIF mdBusinessIF)
  {
    enterMdBusiness(mdBusinessIF);

    exportBusinessComponents(mdBusinessIF);

    exitMdBusiness(mdBusinessIF);
  }

  /**
   * Specifies visit behavior after the MdBusiness has been visited. Exports the
   * stubSource and dtoStubSource tag before closing the mdBusiness tag. This
   * method is likely to be overwritten in child classes.
   * 
   * @param mdBusinessIF
   *          MdBusiness being visited
   */
  protected void exitMdBusiness(MdBusinessDAOIF mdBusinessIF)
  {
    writer.closeTag();
  }

  /**
   * Specifies behavior upon entering a MdTable on visit: Exports the MdTable
   * tag with its attributes.
   * 
   * @param mdTableIF
   *          The MdTable being visited
   */
  protected void enterMdTable(MdTableDAOIF mdTableIF)
  {
    // Get the attribute_tag-value mapping of the entity
    HashMap<String, String> attributes = getMdTableParameters(mdTableIF);

    String tagName = XMLTags.MD_TABLE_TAG;

    if (mdTableIF instanceof MdTermDAOIF)
    {
      tagName = XMLTags.MD_TERM_TAG;
    }

    // Write the CLASS_TAG with its parameters
    writer.openEscapedTag(tagName, attributes);
  }

  /**
   * Visits a MdTable: Export a MdTable
   * 
   * @param mdTable
   *          The MdTable to visit
   */
  public void visitMdTable(MdTableDAOIF mdTableIF)
  {
    enterMdTable(mdTableIF);

    exportTableComponents(mdTableIF);

    exitMdTable(mdTableIF);
  }

  /**
   * Exports common children tags of MdTablees
   * 
   * @param mdTableIF
   *          The MdTable to export
   */
  private void exportTableComponents(MdTableDAOIF mdTableIF)
  {
    if (metadata != null)
    {
      List<? extends MdAttributeDAOIF> attrs = metadata.filterAttributes(mdTableIF);

      visitMdAttributes(attrs);
    }
  }

  /**
   * Specifies visit behavior after the MdTable has been visited. Exports the
   * stubSource and dtoStubSource tag before closing the mdTable tag. This
   * method is likely to be overwritten in child classes.
   * 
   * @param mdTableIF
   *          MdTable being visited
   */
  protected void exitMdTable(MdTableDAOIF mdTableIF)
  {
    writer.closeTag();
  }

  /**
   * Exports common children tags of MdEntities
   * 
   * @param mdEntity
   *          The MdEntity to export
   */
  private void exportEntityComponents(MdEntityDAOIF mdEntity)
  {
    if (metadata != null)
    {
      List<? extends MdAttributeDAOIF> attrs = metadata.filterAttributes(mdEntity);

      // Don't export out DisplayLabel on MdTerms, that attribute is
      // automatically created when you create a new MdTerm.
      // If we start having other metadata that has attributes like this that
      // shouldn't be exported then we'll have to have
      // a more elegant solution but for now this will sloppily work.
      if (mdEntity instanceof MdTermDAOIF)
      {
        Iterator<? extends MdAttributeDAOIF> it = attrs.iterator();
        while (it.hasNext())
        {
          MdAttributeDAOIF attr = it.next();

          if (attr instanceof MdAttributeLocalCharacterDAOIF && attr.getValue(MdAttributeStructInfo.NAME).equals(MdTermInfo.DISPLAY_LABEL))
          {
            it.remove();
            break;
          }
        }
      }

      visitMdAttributes(attrs);
    }

    for (MdMethodDAOIF mdMethod : mdEntity.getMdMethods())
    {
      visitMdMethod(mdMethod, mdMethod.getMdParameterDAOs());
    }
  }

  /**
   * Exports common children tags of MdBusinesses
   * 
   * @param mdBusinessIF
   *          The MdBusiness to export
   */
  private void exportBusinessComponents(MdBusinessDAOIF mdBusinessIF)
  {
    exportEntityComponents(mdBusinessIF);
    // Write the MdStateMachine defined by the entity

    if ( ( metadata != null && metadata.isExportSource() ) || exportSource)
    {
      writer.openTag(XMLTags.STUB_SOURCE_TAG);
      writer.writeCData(mdBusinessIF.getValue(MdBusinessInfo.STUB_SOURCE));
      writer.closeTag();

      writer.openTag(XMLTags.DTO_STUB_SOURCE_TAG);
      writer.writeCData(mdBusinessIF.getValue(MdBusinessInfo.DTO_STUB_SOURCE));
      writer.closeTag();
    }
  }

  /**
   * Specifies behavior upon entering a MdStruct on visit: Exports the MdStruct
   * tag with its attributes.
   * 
   * @param mdStructIF
   *          The MdStruct being visited
   */
  protected void enterMdStruct(MdStructDAOIF mdStructIF)
  {
    // Get the attribute_tag-value mapping of the entity
    HashMap<String, String> attributes = getMdStructParameters(mdStructIF);

    // Write the CLASS_TAG with its parameters
    writer.openEscapedTag(XMLTags.MD_STRUCT_TAG, attributes);
  }

  /**
   * Visits a MdStruct: Export a MdStruct
   * 
   * @param mdStructIF
   *          The MdStruct to visit
   */
  public void visitMdStruct(MdStructDAOIF mdStructIF)
  {
    // Do not export system standalone classes
    if (!MdTypeDAO.isSystemPackageMetadata(mdStructIF.getPackage()))
    {
      enterMdStruct(mdStructIF);
      exportEntityComponents(mdStructIF);

      if ( ( metadata != null && metadata.isExportSource() ) || exportSource)
      {
        writer.openTag(XMLTags.STUB_SOURCE_TAG);
        writer.writeCData(mdStructIF.getValue(MdBusinessInfo.STUB_SOURCE));
        writer.closeTag();

        writer.openTag(XMLTags.DTO_STUB_SOURCE_TAG);
        writer.writeCData(mdStructIF.getValue(MdBusinessInfo.DTO_STUB_SOURCE));
        writer.closeTag();
      }

      exitMdStruct(mdStructIF);
    }
  }

  /**
   * Specifies visit behavior after the MdStruct has been visited. Exports the
   * stubSource and dtoStubSource tag before closing the mdStruct tag. This
   * method is likely to be overwritten in child classes.
   * 
   * @param mdStruct
   *          MdStruct being visited
   */
  protected void exitMdStruct(MdStructDAOIF mdStruct)
  {

    writer.closeTag();
  }

  /**
   * Specifies behavior upon entering a MdLocalStruct on visit: Exports the
   * MdLocalStruct tag with its attributes.
   * 
   * @param mdLocalStructIF
   *          The MdLocalStruct being visited
   */
  protected void enterMdLocalStruct(MdLocalStructDAOIF mdLocalStructIF)
  {
    // Get the attribute_tag-value mapping of the entity
    HashMap<String, String> attributes = getMdStructParameters(mdLocalStructIF);

    // Write the CLASS_TAG with its parameters
    writer.openEscapedTag(XMLTags.MD_LOCAL_STRUCT_TAG, attributes);
  }

  /**
   * Visits a MdLocalStruct: Export a MdLocalStruct
   * 
   * @param mdLocalStructIF
   *          The MdLocalStruct to visit
   */
  public void visitMdLocalStruct(MdLocalStructDAOIF mdLocalStructIF)
  {
    // Do not export system standalone classes
    if (!MdTypeDAO.isSystemPackageMetadata(mdLocalStructIF.getPackage()))
    {
      enterMdLocalStruct(mdLocalStructIF);
      exportEntityComponents(mdLocalStructIF);
      writer.openTag(XMLTags.STUB_SOURCE_TAG);
      writer.writeCData(mdLocalStructIF.getValue(MdBusinessInfo.STUB_SOURCE));
      writer.closeTag();

      writer.openTag(XMLTags.DTO_STUB_SOURCE_TAG);
      writer.writeCData(mdLocalStructIF.getValue(MdBusinessInfo.DTO_STUB_SOURCE));
      writer.closeTag();

      exitMdLocalStruct(mdLocalStructIF);
    }
  }

  /**
   * Specifies visit behavior after the MdLocalStruct has been visited. Exports
   * the stubSource and dtoStubSource tag before closing the mdStruct tag. This
   * method is likely to be overwritten in child classes.
   * 
   * @param mdStruct
   *          MdLocalStruct being visited
   */
  protected void exitMdLocalStruct(MdLocalStructDAOIF mdLocalStructIF)
  {

    writer.closeTag();
  }

  /**
   * Specifies behavior upon entering a MdBusiness which extends
   * EnumerationMaster on visit: Exports the MdBusinessEnum tag with its
   * attributes.
   * 
   * @param mdBusinessIF
   *          The MdBusinessIF which extends EnumerationMaster being visited
   */
  protected void enterMdBusinessEnum(MdBusinessDAOIF mdBusinessIF)
  {
    // Get the attribute_tag-value mapping of the entity
    HashMap<String, String> attributes = getMdStructParameters(mdBusinessIF);

    // Write the CLASS_TAG with its parameters
    writer.openEscapedTag(XMLTags.ENUMERATION_MASTER_TAG, attributes);
  }

  /**
   * Visits a MdBusiness: Export a MdBusiness which EnumerationMaster
   * 
   * @param mdFacade
   *          The MdFacade to visit
   */
  public void visitMdBusinessEnum(MdBusinessDAOIF mdBusinessIF)
  {
    // Do not export system standalone classes
    if (!MdTypeDAO.isSystemPackageMetadata(mdBusinessIF.getPackage()))
    {
      enterMdBusinessEnum(mdBusinessIF);
      exportEntityComponents(mdBusinessIF);
      exitMdBusinessEnum(mdBusinessIF);
    }
  }

  /**
   * Specifies visit behavior after the MdBusiness has been visited. Exports the
   * stubSource and dtoStubSource tag before closing the mdBusiness tag. This
   * method is likely to be overwritten in child classes.
   * 
   * @param mdBusinessIF
   *          MdBusiness being visited
   */
  protected void exitMdBusinessEnum(MdBusinessDAOIF mdBusinessIF)
  {
    if ( ( metadata != null && metadata.isExportSource() ) || exportSource)
    {
      writer.openTag(XMLTags.STUB_SOURCE_TAG);
      writer.writeCData(mdBusinessIF.getValue(MdBusinessInfo.STUB_SOURCE));
      writer.closeTag();

      writer.openTag(XMLTags.DTO_STUB_SOURCE_TAG);
      writer.writeCData(mdBusinessIF.getValue(MdBusinessInfo.DTO_STUB_SOURCE));
      writer.closeTag();
    }

    writer.closeTag();
  }

  /**
   * Visits a MdIndex: Export a MdIndex
   * 
   * @param mdIndex
   *          The MdIndex to visit
   */
  public void visitMdIndex(MdIndexDAOIF mdIndex)
  {
    HashMap<String, String> attributes = getMdIndexParameters(mdIndex);

    // Write the INDEX_TAG with is parameters
    writer.openEscapedTag(XMLTags.MD_INDEX_TAG, attributes);

    // Write the attributes of the index
    visitIndexAttributes(mdIndex.getIndexedAttributes());

    writer.closeTag();
  }

  /**
   * Specifies behavior upon entering a MdRelationship on visit: Exports the
   * MdRelationship tag with its attributes.
   * 
   * @param mdRelationship
   *          The MdRelationship being visited
   */
  protected void enterMdRelationship(MdRelationshipDAOIF mdRelationship)
  {
    // Get the attribute_tag-value mapping of the entity
    HashMap<String, String> attributes = getMdRelationshipParameters(mdRelationship);

    String tagName = XMLTags.MD_RELATIONSHIP_TAG;

    if (mdRelationship instanceof MdTermRelationshipDAO)
    {
      tagName = XMLTags.MD_TERM_RELATIONSHIP_TAG;
    }
    else if (mdRelationship instanceof MdGraphDAOIF)
    {
      tagName = XMLTags.MD_GRAPH_TAG;
    }
    else if (mdRelationship instanceof MdTreeDAO)
    {
      tagName = XMLTags.MD_TREE_TAG;
    }

    // Write the CLASS_TAG with its parameters
    writer.openEscapedTag(tagName, attributes);
  }

  /**
   * Visits a MdRelationship: Export a MdRelationship
   * 
   * @param mdRelationship
   *          The MdRelationshiop to visit
   */
  public void visitMdRelationship(MdRelationshipDAOIF mdRelationship)
  {
    // Do not export system standalone classes
    if (!MdTypeDAO.isSystemPackageMetadata(mdRelationship.getPackage()))
    {
      enterMdRelationship(mdRelationship);

      // Write parent tag
      writer.writeEmptyEscapedTag(XMLTags.PARENT_TAG, getParentParameters(mdRelationship));

      // write child tag
      writer.writeEmptyEscapedTag(XMLTags.CHILD_TAG, getChildParameters(mdRelationship));

      exportEntityComponents(mdRelationship);

      if ( ( metadata != null && metadata.isExportSource() ) || exportSource)
      {
        writer.openTag(XMLTags.STUB_SOURCE_TAG);
        writer.writeCData(mdRelationship.getValue(MdBusinessInfo.STUB_SOURCE));
        writer.closeTag();

        writer.openTag(XMLTags.DTO_STUB_SOURCE_TAG);
        writer.writeCData(mdRelationship.getValue(MdBusinessInfo.DTO_STUB_SOURCE));
        writer.closeTag();
      }

      exitMdRelationship(mdRelationship);
    }
  }

  /**
   * Specifies visit behavior after the MdRelationship has been visited. Exports
   * the stubSource and dtoStubSource tag before closing the mdRelationship tag.
   * This method is likely to be overwritten in child classes.
   * 
   * @param mdRelationship
   *          MdRelationship being visited
   */
  protected void exitMdRelationship(MdRelationshipDAOIF mdRelationship)
  {

    /*
     * writer.openTag(XMLTags.STUB_SOURCE_TAG);
     * writer.writeCData(mdRelationship.getValue(MdBusinessInfo.STUB_SOURCE));
     * writer.closeTag();
     * 
     * writer.openTag(XMLTags.DTO_STUB_SOURCE_TAG);
     * writer.writeCData(mdRelationship
     * .getValue(MdBusinessInfo.DTO_STUB_SOURCE)); writer.closeTag();
     */

    writer.closeTag();
  }

  protected void enterMdExcpetion(MdExceptionDAOIF mdException)
  {
    // Get the attribute_tag-value mapping of the entity
    HashMap<String, String> attributes = getMdExceptionParameters(mdException);

    // Write the CLASS_TAG with its parameters
    writer.openEscapedTag(XMLTags.MD_EXCEPTION_TAG, attributes);
  }

  public void visitMdException(MdExceptionDAOIF mdException)
  {
    enterMdExcpetion(mdException);

    // Write the attributes of the entity
    visitMdAttributes(mdException.definesAttributes());

    exitMdException(mdException);
  }

  protected void exitMdException(MdExceptionDAOIF mdException)
  {
    if ( ( metadata != null && metadata.isExportSource() ) || exportSource)
    {
      writer.openTag(XMLTags.STUB_SOURCE_TAG);
      writer.writeCData(mdException.getValue(MdExceptionInfo.STUB_SOURCE));
      writer.closeTag();

      writer.openTag(XMLTags.DTO_STUB_SOURCE_TAG);
      writer.writeCData(mdException.getValue(MdExceptionInfo.DTO_STUB_SOURCE));
      writer.closeTag();
    }

    writer.closeTag();
  }

  protected void enterMdProblem(MdProblemDAOIF mdProblem)
  {
    // Get the attribute_tag-value mapping of the entity
    HashMap<String, String> attributes = getMdProblemParameters(mdProblem);

    // Write the CLASS_TAG with its parameters
    writer.openEscapedTag(XMLTags.MD_PROBLEM_TAG, attributes);
  }

  public void visitMdProblem(MdProblemDAOIF mdProblem)
  {
    enterMdProblem(mdProblem);

    // Write the attributes of the entity
    visitMdAttributes(mdProblem.definesAttributes());

    exitMdProblem(mdProblem);
  }

  protected void exitMdProblem(MdProblemDAOIF mdProblem)
  {
    if ( ( metadata != null && metadata.isExportSource() ) || exportSource)
    {
      writer.openTag(XMLTags.STUB_SOURCE_TAG);
      writer.writeCData(mdProblem.getValue(MdProblemInfo.STUB_SOURCE));
      writer.closeTag();

      writer.openTag(XMLTags.DTO_STUB_SOURCE_TAG);
      writer.writeCData(mdProblem.getValue(MdProblemInfo.DTO_STUB_SOURCE));
      writer.closeTag();
    }

    writer.closeTag();
  }

  protected void enterMdInformation(MdInformationDAOIF mdInformation)
  {
    // Get the attribute_tag-value mapping of the entity
    HashMap<String, String> attributes = getMdInformationParameters(mdInformation);

    // Write the CLASS_TAG with its parameters
    writer.openEscapedTag(XMLTags.MD_INFORMATION_TAG, attributes);
  }

  public void visitMdInformation(MdInformationDAOIF mdInformation)
  {
    enterMdInformation(mdInformation);

    // Write the attributes of the entity
    visitMdAttributes(mdInformation.definesAttributes());

    exitMdInformation(mdInformation);
  }

  protected void exitMdInformation(MdInformationDAOIF mdInformation)
  {
    if ( ( metadata != null && metadata.isExportSource() ) || exportSource)
    {
      writer.openTag(XMLTags.STUB_SOURCE_TAG);
      writer.writeCData(mdInformation.getValue(MdInformationInfo.STUB_SOURCE));
      writer.closeTag();

      writer.openTag(XMLTags.DTO_STUB_SOURCE_TAG);
      writer.writeCData(mdInformation.getValue(MdInformationInfo.DTO_STUB_SOURCE));
      writer.closeTag();
    }

    writer.closeTag();
  }

  protected void enterMdWarning(MdWarningDAOIF mdWarning)
  {
    // Get the attribute_tag-value mapping of the entity
    HashMap<String, String> attributes = getMdWarningParameters(mdWarning);

    // Write the CLASS_TAG with its parameters
    writer.openEscapedTag(XMLTags.MD_WARNING_TAG, attributes);
  }

  public void visitMdWarning(MdWarningDAOIF mdWarning)
  {
    enterMdWarning(mdWarning);

    // Write the attributes of the entity
    visitMdAttributes(mdWarning.definesAttributes());

    exitMdWarning(mdWarning);
  }

  protected void exitMdWarning(MdWarningDAOIF mdWarning)
  {
    if ( ( metadata != null && metadata.isExportSource() ) || exportSource)
    {
      writer.openTag(XMLTags.STUB_SOURCE_TAG);
      writer.writeCData(mdWarning.getValue(MdInformationInfo.STUB_SOURCE));
      writer.closeTag();

      writer.openTag(XMLTags.DTO_STUB_SOURCE_TAG);
      writer.writeCData(mdWarning.getValue(MdInformationInfo.DTO_STUB_SOURCE));
      writer.closeTag();
    }

    writer.closeTag();
  }

  protected void enterMdView(MdViewDAOIF mdView)
  {
    // Get the attribute_tag-value mapping of the entity
    HashMap<String, String> attributes = getMdViewParameters(mdView);

    // Write the view tag with its attributes
    writer.openEscapedTag(XMLTags.MD_VIEW_TAG, attributes);
  }

  public void visitMdView(MdViewDAOIF mdView)
  {
    enterMdView(mdView);

    // Write the attributes of the entity
    visitMdAttributes(mdView.definesAttributes());

    for (MdMethodDAOIF mdMethod : mdView.getMdMethods())
    {
      visitMdMethod(mdMethod, mdMethod.getMdParameterDAOs());
    }

    exitMdView(mdView);
  }

  protected void exitMdView(MdViewDAOIF mdView)
  {
    if ( ( metadata != null && metadata.isExportSource() ) || exportSource)
    {
      writer.openTag(XMLTags.STUB_SOURCE_TAG);
      writer.writeCData(mdView.getValue(MdViewInfo.STUB_SOURCE));
      writer.closeTag();

      writer.openTag(XMLTags.DTO_STUB_SOURCE_TAG);
      writer.writeCData(mdView.getValue(MdViewInfo.DTO_STUB_SOURCE));
      writer.closeTag();

      writer.openTag(XMLTags.QUERY_STUB_SOURCE_TAG);
      writer.writeCData(mdView.getValue(MdViewInfo.QUERY_STUB_SOURCE));
      writer.closeTag();
    }

    writer.closeTag();
  }

  protected void enterMdUtil(MdUtilDAOIF mdUtil)
  {
    // Get the attribute_tag-value mapping of the entity
    HashMap<String, String> attributes = getMdUtilParameters(mdUtil);

    // Write the CLASS_TAG with its parameters
    writer.openEscapedTag(XMLTags.MD_UTIL_TAG, attributes);
  }

  public void visitMdUtil(MdUtilDAOIF mdUtil)
  {
    enterMdUtil(mdUtil);

    // Write the attributes of the entity
    visitMdAttributes(mdUtil.definesAttributes());

    for (MdMethodDAOIF mdMethod : mdUtil.getMdMethods())
    {
      visitMdMethod(mdMethod, mdMethod.getMdParameterDAOs());
    }

    exitMdUtil(mdUtil);
  }

  protected void exitMdUtil(MdUtilDAOIF mdUtil)
  {
    if ( ( metadata != null && metadata.isExportSource() ) || exportSource)
    {
      writer.openTag(XMLTags.STUB_SOURCE_TAG);
      writer.writeCData(mdUtil.getValue(MdBusinessInfo.STUB_SOURCE));
      writer.closeTag();

      writer.openTag(XMLTags.DTO_STUB_SOURCE_TAG);
      writer.writeCData(mdUtil.getValue(MdBusinessInfo.DTO_STUB_SOURCE));
      writer.closeTag();
    }
    writer.closeTag();
  }

  private HashMap<String, String> getMdVertexParameters(MdVertexDAOIF mdVertex)
  {
    HashMap<String, String> parameters = getMdClassParameters(mdVertex);

    // Get the super entity
    MdVertexDAOIF superClass = mdVertex.getSuperClass();

    if (superClass != null)
    {
      String superType = superClass.definesType();

      // Add the super class to the extends mapping
      parameters.put(XMLTags.EXTENDS_ATTRIBUTE, superType);
    }

    parameters.put(XMLTags.ABSTRACT_ATTRIBUTE, Boolean.toString(mdVertex.isAbstract()));
    parameters.put(XMLTags.DATABASE_CLASS_NAME, mdVertex.getValue(MdVertexInfo.DB_CLASS_NAME));
    parameters.put(XMLTags.ENABLE_CHANGE_OVER_TIME, Boolean.toString(mdVertex.isEnableChangeOverTime()));

    return parameters;
  }

  protected void enterMdVertex(MdVertexDAOIF mdVertex)
  {
    // Get the attribute_tag-value mapping of the entity
    HashMap<String, String> attributes = getMdVertexParameters(mdVertex);

    // Write the CLASS_TAG with its parameters

    writer.openEscapedTag(XMLTags.MD_VERTEX_TAG, attributes);
  }

  public void visitMdVertex(MdVertexDAOIF mdVertex)
  {
    enterMdVertex(mdVertex);

    // Write the attributes of the entity
    visitMdAttributes(mdVertex.definesAttributes());

    // for (MdMethodDAOIF mdMethod : mdVertex.getMdMethods())
    // {
    // visitMdMethod(mdMethod, mdMethod.getMdParameterDAOs());
    // }

    exitMdVertex(mdVertex);
  }

  protected void exitMdVertex(MdVertexDAOIF mdVertex)
  {
    writer.closeTag();
  }

  private HashMap<String, String> getMdClassificationParameters(MdClassificationDAOIF mdClassification)
  {
    HashMap<String, String> parameters = new HashMap<String, String>();

    String name = mdClassification.definesType();

    // Map the parameter value to its correct attribute tag
    parameters.put(XMLTags.NAME_ATTRIBUTE, name);

    Map<String, String> localValues = mdClassification.getDisplayLabels();
    writeLocaleValues(parameters, XMLTags.DISPLAY_LABEL_ATTRIBUTE, localValues);

    parameters.put(XMLTags.REMOVE_ATTRIBUTE, mdClassification.getValue(MdEntityInfo.REMOVE));

    Map<String, String> localDescValues = mdClassification.getDescriptions();
    writeLocaleValues(parameters, XMLTags.DESCRIPTION_ATTRIBUTE, localDescValues);

    VertexObjectDAOIF root = mdClassification.getRoot();

    if (root != null)
    {
      parameters.put(MdClassificationInfo.ROOT, root.getOid());
    }

    return parameters;
  }

  public void visitMdClassification(MdClassificationDAOIF mdClassification)
  {
    // Get the attribute_tag-value mapping of the entity
    HashMap<String, String> attributes = getMdClassificationParameters(mdClassification);

    // Write the CLASS_TAG with its parameters
    writer.writeEmptyTag(XMLTags.MD_CLASSIFICATION_TAG, attributes);
  }

  private HashMap<String, String> getMdEdgeParameters(MdEdgeDAOIF mdEdge)
  {
    HashMap<String, String> parameters = getMdClassParameters(mdEdge);

    // Get the super entity
    MdVertexDAOIF parent = MdVertexDAO.get(mdEdge.getValue(MdEdgeInfo.PARENT_MD_VERTEX));
    MdVertexDAOIF child = MdVertexDAO.get(mdEdge.getValue(MdEdgeInfo.CHILD_MD_VERTEX));

    parameters.put(XMLTags.PARENT_TAG, parent.definesType());
    parameters.put(XMLTags.CHILD_TAG, child.definesType());

    parameters.put(XMLTags.ABSTRACT_ATTRIBUTE, Boolean.toString(mdEdge.isAbstract()));
    parameters.put(XMLTags.DATABASE_CLASS_NAME, mdEdge.getValue(MdEdgeInfo.DB_CLASS_NAME));
    parameters.put(XMLTags.ENABLE_CHANGE_OVER_TIME, Boolean.toString(mdEdge.isEnableChangeOverTime()));

    return parameters;
  }

  protected void enterMdEdge(MdEdgeDAOIF mdEdge)
  {
    // Get the attribute_tag-value mapping of the entity
    HashMap<String, String> attributes = getMdEdgeParameters(mdEdge);

    // Write the CLASS_TAG with its parameters
    writer.openEscapedTag(XMLTags.MD_EDGE_TAG, attributes);
  }

  public void visitMdEdge(MdEdgeDAOIF mdEdge)
  {
    enterMdEdge(mdEdge);

    // Write the attributes of the entity
    visitMdAttributes(mdEdge.definesAttributes());

    // for (MdMethodDAOIF mdMethod : mdEdge.getMdMethods())
    // {
    // visitMdMethod(mdMethod, mdMethod.getMdParameterDAOs());
    // }

    exitMdEdge(mdEdge);
  }

  protected void exitMdEdge(MdEdgeDAOIF mdEdge)
  {
    writer.closeTag();
  }

  /**
   * Exports all of the attributes of a given entity
   * 
   * @param mdAttributes
   *          List of MdAttributes to export
   */
  public void visitMdAttributes(List<? extends MdAttributeDAOIF> mdAttributes)
  {
    // Open the empty ATTRIBUTES tag
    writer.openTag(XMLTags.ATTRIBUTES_TAG);

    for (MdAttributeDAOIF mdAttribute : mdAttributes)
    {
      if (!ATTRIBUTE_MASK.contains(mdAttribute.definesAttribute()))
      {
        String tag = getTagName(mdAttribute);
        HashMap<String, String> parameters = getAttributeParameters(mdAttribute);

        if (mdAttribute instanceof MdAttributeMultiTermDAOIF)
        {
          writer.openEscapedTag(tag, parameters);

          this.writeRootTerms((MdAttributeMultiTermDAOIF) mdAttribute);

          writer.closeTag();
        }
        else if (mdAttribute instanceof MdAttributeTermDAOIF)
        {
          writer.openEscapedTag(tag, parameters);

          this.writeRootTerms((MdAttributeTermDAOIF) mdAttribute);

          writer.closeTag();
        }
        else
        {
          writer.writeEmptyEscapedTag(tag, parameters);
        }
      }
    }

    writer.closeTag();
  }

  protected void writeRootTerms(MdAttributeMultiTermDAOIF mdAttributeMultiTerm)
  {
    this.writeRootTerms(mdAttributeMultiTerm.getAllAttributeRoots());
  }

  protected void writeRootTerms(MdAttributeTermDAOIF mdAttributeTerm)
  {
    this.writeRootTerms(mdAttributeTerm.getAllAttributeRoots());
  }

  protected void writeRootTerms(List<RelationshipDAOIF> roots)
  {
    for (RelationshipDAOIF root : roots)
    {
      BusinessDAOIF term = root.getChild();
      String selectable = root.getValue(MdAttributeTermInfo.SELECTABLE);

      Map<String, String> attributes = new HashMap<String, String>();
      attributes.put(XMLTags.KEY_ATTRIBUTE, term.getKey());
      attributes.put(XMLTags.SELECTABLE, selectable);

      writer.writeEmptyEscapedTag(XMLTags.ROOT_TERM_TAG, attributes);
    }
  }

  /**
   * Specifies behavior upon entering a MdEnumeration on visit: Exports the
   * MdEnumeration tag with its attributes.
   * 
   * @param mdEnumeration
   *          The MdEnumeration being visited
   */
  protected void enterMdEnumeration(MdEnumerationDAOIF mdEnumeration)
  {
    // Get the attribute_tag-value mapping of the entity
    HashMap<String, String> attributes = getFilterParameters(mdEnumeration);

    // Write the CLASS_TAG with its parameters
    writer.openEscapedTag(XMLTags.MD_ENUMERATION_TAG, attributes);
  }

  /**
   * Exports a MdEnumeration except of MdEnumerations which reside in the System
   * and MetaData package.
   */
  public void visitMdEnumeration(MdEnumerationDAOIF filter)
  {
    // Do not export system enumeration filters

    if (!MdTypeDAO.isSystemPackageMetadata(filter.getPackage()))
    {
      enterMdEnumeration(filter);

      // Write the instances of the filter
      if (MdAttributeBooleanInfo.TRUE.equals(filter.getValue(MdEnumerationInfo.INCLUDE_ALL)))
      {
        writer.writeEmptyTag(XMLTags.INCLUDEALL_TAG);
      }
      else
      {
        for (BusinessDAOIF item : filter.getAllEnumItems())
        {
          HashMap<String, String> param = new HashMap<String, String>();
          param.put(XMLTags.ENUM_NAME_ATTRIBUTE, item.getAttributeIF(EnumerationMasterInfo.NAME).getValue());

          writer.writeEmptyEscapedTag(XMLTags.ADD_ENUM_ITEM_TAG, param);
        }
      }

      exitMdEnumeration(filter);
    }
  }

  /**
   * Specifies visit behavior after the MdEnumeration has been visited. This
   * method is likely to be overwritten in child classes.
   * 
   * @param mdEnumeration
   *          MdEnumeration being visited
   */
  protected void exitMdEnumeration(MdEnumerationDAOIF mdEnumeration)
  {
    writer.closeTag();
  }

  public void visitIndexAttributes(List<MdAttributeConcreteDAOIF> mdAttributes)
  {
    int count = 0;
    for (MdAttributeConcreteDAOIF mdAttribute : mdAttributes)
    {
      HashMap<String, String> attributes = new HashMap<String, String>();

      String name = mdAttribute.definesAttribute();
      attributes.put(XMLTags.INDEX_NAME_ATTRIBUTE, name);
      attributes.put(XMLTags.INDEX_ORDER_ATTRIBUTE, Integer.toString(count));

      // Write the INDEX_ATTRIBUTE_TAG with is parameters
      writer.writeEmptyEscapedTag(XMLTags.INDEX_ATTRIBUTE_TAG, attributes);
      count++;
    }
  }

  public void visitRelationship(RelationshipDAOIF relationship)
  {
    HashMap<String, String> parameters = new HashMap<String, String>();
    // The name of the class being instaniated
    parameters.put(XMLTags.TYPE_ATTRIBUTE, relationship.getType());

    parameters.put(XMLTags.PARENT_KEY_TAG, relationship.getParent().getKey());
    parameters.put(XMLTags.CHILD_KEY_TAG, relationship.getChild().getKey());

    parameters.put(XMLTags.KEY_ATTRIBUTE, relationship.getKey());

    writer.openEscapedTag(XMLTags.RELATIONSHIP_TAG, parameters);

    // Write all of the values of the instance
    visitValues(relationship.getAttributeArrayIF());

    writer.closeTag();
  }

  public void visitEdge(EdgeObjectDAOIF relationship)
  {
    HashMap<String, String> parameters = new HashMap<String, String>();
    // The name of the class being instaniated
    parameters.put(XMLTags.TYPE_ATTRIBUTE, relationship.getType());

    parameters.put(XMLTags.PARENT_KEY_TAG, relationship.getParent().getKey());
    parameters.put(XMLTags.CHILD_KEY_TAG, relationship.getChild().getKey());

    parameters.put(XMLTags.KEY_ATTRIBUTE, relationship.getKey());

    writer.openEscapedTag(XMLTags.EDGE_TAG, parameters);

    // Write all of the values of the instance
//    visitValues(relationship.getAttributeArrayIF());

    writer.closeTag();
  }

  /**
   * Exports all of the instances of classes, standalone classes, enumeration
   * classes, and relationship which were exported
   */
  public void visitVertex(VertexObjectDAOIF businessDAO)
  {
    HashMap<String, String> parameters = new HashMap<String, String>();
    // The type of the instance
    parameters.put(XMLTags.TYPE_ATTRIBUTE, businessDAO.getType());
    parameters.put(XMLTags.KEY_ATTRIBUTE, businessDAO.getKey());

    writer.openEscapedTag(XMLTags.VERTEX_TAG, parameters);

    // Write all of the values of the instance
    visitValues(businessDAO.getAttributeArrayIF());

    writer.closeTag();
  }

  /**
   * Exports all of the instances of classes, standalone classes, enumeration
   * classes, and relationship which were exported
   */
  public void visitObject(BusinessDAOIF businessDAO)
  {
    HashMap<String, String> parameters = new HashMap<String, String>();
    // The type of the instance
    parameters.put(XMLTags.TYPE_ATTRIBUTE, businessDAO.getType());
    parameters.put(XMLTags.KEY_ATTRIBUTE, businessDAO.getKey());

    writer.openEscapedTag(XMLTags.OBJECT_TAG, parameters);

    // Write all of the values of the instance
    visitValues(businessDAO.getAttributeArrayIF());

    writer.closeTag();
  }

  /**
   * Exports the attribute-value mappings of an object. Does not export system
   * attributes.
   * 
   */
  protected void visitValues(AttributeIF[] attributes)
  {
    // Get all attributes of the entity

    for (AttributeIF attribute : attributes)
    {
      String attributeName = attribute.getName();
      String attributeValue = attribute.getValue();

      // Do not export system attributes or values that do not have a value
      if (ATTRIBUTE_MASK.contains( ( attributeName )) //
          || ( attributeValue.equals("") // 
              && ! ( attribute instanceof AttributeCharacter ) //
              && ! ( attribute instanceof AttributeText ) //
              && ! ( attribute instanceof AttributeJson ) //
              && ! ( attribute instanceof AttributeClob ) ))
      {
        continue;
      }

      HashMap<String, String> parameters = new HashMap<String, String>();

      // The attribute is a reference to another instance
      if (attribute instanceof AttributeReferenceIF)
      {
        visitReferenceValue(attributeName, attributeValue, parameters);
      }
      else if (attribute instanceof AttributeStructIF)
      {
        visitStructValue(attribute, attributeName, parameters);
      }
      // The attribute is a reference to multiple instances
      else if (attribute instanceof AttributeEnumerationIF)
      {
        visitEnumerationValue(attribute, attributeName, parameters);
      }
      // The attribute does not reference another instance
      else
      {
        // load parameters
        parameters.put(XMLTags.ENTITY_ATTRIBUTE_NAME_ATTRIBUTE, attributeName);
        parameters.put(XMLTags.ENTITY_ATTRIBUTE_VALUE_ATTRIBUTE, attributeValue);

        // write an instance_value
        writer.writeEmptyEscapedTag(XMLTags.ATTRIBUTE_TAG, parameters);
      }
    }
  }

  protected void visitEnumerationValue(AttributeIF attribute, String attributeName, HashMap<String, String> parameters)
  {
    parameters.put(XMLTags.ENTITY_ATTRIBUTE_NAME_ATTRIBUTE, attributeName);

    // write the selection tag
    writer.openEscapedTag(XMLTags.ATTRIBUTE_ENUMERATION_TAG, parameters);

    // Get all of the enumeration IDs the attribute references
    AttributeEnumerationIF enumeration = (AttributeEnumerationIF) attribute;
    EnumerationItemDAOIF[] enumItems = enumeration.dereference();

    for (EnumerationItemDAOIF enumItem : enumItems)
    {
      HashMap<String, String> enumParam = new HashMap<String, String>();
      enumParam.put(XMLTags.ENUM_NAME_ATTRIBUTE, enumItem.getName());

      // write the enumerated item tag
      writer.writeEmptyEscapedTag(XMLTags.ENUMERATED_ITEM_TAG, enumParam);
    }

    writer.closeTag();
  }

  protected void visitStructValue(AttributeIF attribute, String attributeName, HashMap<String, String> parameters)
  {
    parameters.put(XMLTags.ENTITY_ATTRIBUTE_NAME_ATTRIBUTE, attributeName);

    // write the struct tag
    writer.openEscapedTag(XMLTags.ATTRIBUTE_STRUCT_TAG, parameters);

    // Get all of the enumeration IDs the attribute references
    AttributeStructIF struct = (AttributeStructIF) attribute;

    visitValues(struct.getAttributeArrayIF());

    writer.closeTag();
  }

  protected void visitReferenceValue(String attributeName, String attributeValue, HashMap<String, String> parameters)
  {
    // load parameters
    parameters.put(XMLTags.ENTITY_ATTRIBUTE_NAME_ATTRIBUTE, attributeName);
    EntityDAOIF referenceEntityDAOIF = EntityDAO.get(attributeValue);
    parameters.put(XMLTags.KEY_ATTRIBUTE, referenceEntityDAOIF.getKey());

    // write instance_ref tag
    writer.writeEmptyEscapedTag(XMLTags.ATTRIBUTE_REFERENCE_TAG, parameters);
  }

  /**
   * Returns the attribute-value mappings for all of the MdBusiness attributes
   * 
   * @param mdBusinessIF
   *          The MdBusiness to get the parameters
   * @return A HashMap of the parameter-value pairs
   */
  private HashMap<String, String> getMdBusinessParameters(MdBusinessDAOIF mdBusinessIF)
  {
    HashMap<String, String> parameters = getMdElementParameters(mdBusinessIF);

    // Get the super entity
    MdElementDAOIF superClass = mdBusinessIF.getSuperClass();

    if (superClass != null)
    {
      String superType = superClass.definesType();

      // Add the super class to the extends mapping
      parameters.put(XMLTags.EXTENDS_ATTRIBUTE, superType);
    }

    // Get the cache alogrithm
    String indexType = ( (AttributeEnumerationIF) mdBusinessIF.getAttributeIF(MdBusinessInfo.CACHE_ALGORITHM) ).dereference()[0].getOid();

    if (indexType.equals(EntityCacheMaster.CACHE_EVERYTHING.getOid()))
    {
      parameters.put(XMLTags.CACHE_ALGORITHM_ATTRIBUTE, XMLTags.EVERYTHING_ENUMERATION);
    }
    else if (indexType.equals(EntityCacheMaster.CACHE_NOTHING.getOid()))
    {
      parameters.put(XMLTags.CACHE_ALGORITHM_ATTRIBUTE, XMLTags.NOTHING_ENUMERATION);
    }
    else
    {
      parameters.put(XMLTags.CACHE_ALGORITHM_ATTRIBUTE, XMLTags.RECENTLY_ENUMERATION);
    }

    parameters.put(XMLTags.CACHE_SIZE_ATTRIBUTE, mdBusinessIF.getValue(MdBusinessInfo.CACHE_SIZE));

    return parameters;
  }

  /**
   * Returns the attribute-value mappings for all of the MdTable attributes
   * 
   * @param mdTableIF
   *          The MdTable to get the parameters
   * @return A HashMap of the parameter-value pairs
   */
  private HashMap<String, String> getMdTableParameters(MdTableDAOIF mdTableIF)
  {
    HashMap<String, String> parameters = getMdClassParameters(mdTableIF);

    parameters.put(XMLTags.ENTITY_TABLE, mdTableIF.getValue(MdTableInfo.TABLE_NAME));

    return parameters;
  }

  private HashMap<String, String> getMdTransientParameters(MdTransientDAOIF mdTransient)
  {
    HashMap<String, String> parameters = getMdClassParameters(mdTransient);

    parameters.put(XMLTags.EXTENDABLE_ATTRIBUTE, mdTransient.getValue(MdTransientInfo.EXTENDABLE));
    parameters.put(XMLTags.ABSTRACT_ATTRIBUTE, mdTransient.getValue(MdTransientInfo.ABSTRACT));
    parameters.put(XMLTags.GENERATE_SOURCE, mdTransient.getValue(MdTransientInfo.GENERATE_SOURCE));

    return parameters;
  }

  private HashMap<String, String> getMdExceptionParameters(MdExceptionDAOIF mdExceptionIF)
  {
    HashMap<String, String> parameters = getMdTransientParameters(mdExceptionIF);

    Map<String, String> localValues = mdExceptionIF.getMessages();
    writeLocaleValues(parameters, XMLTags.MESSAGE_ATTRIBUTE, localValues);

    // Get the super entity
    MdExceptionDAOIF superClass = mdExceptionIF.getSuperClass();

    if (superClass != null)
    {
      String superType = superClass.definesType();

      // Add the super class to the extends mapping
      parameters.put(XMLTags.EXTENDS_ATTRIBUTE, superType);
    }

    return parameters;
  }

  private HashMap<String, String> getMdProblemParameters(MdProblemDAOIF mdProblem)
  {
    HashMap<String, String> parameters = getMdTransientParameters(mdProblem);

    Map<String, String> localValues = mdProblem.getMessages();
    writeLocaleValues(parameters, XMLTags.MESSAGE_ATTRIBUTE, localValues);

    // Get the super entity
    MdProblemDAOIF superClass = mdProblem.getSuperClass();

    if (superClass != null)
    {
      String superType = superClass.definesType();

      // Add the super class to the extends mapping
      parameters.put(XMLTags.EXTENDS_ATTRIBUTE, superType);
    }

    return parameters;
  }

  private HashMap<String, String> getMdWarningParameters(MdWarningDAOIF mdWarning)
  {
    HashMap<String, String> parameters = getMdTransientParameters(mdWarning);

    Map<String, String> localValues = mdWarning.getMessages();
    writeLocaleValues(parameters, XMLTags.MESSAGE_ATTRIBUTE, localValues);

    // Get the super entity
    MdWarningDAOIF superClass = mdWarning.getSuperClass();

    if (superClass != null)
    {
      String superType = superClass.definesType();

      // Add the super class to the extends mapping
      parameters.put(XMLTags.EXTENDS_ATTRIBUTE, superType);
    }

    return parameters;
  }

  private HashMap<String, String> getMdInformationParameters(MdInformationDAOIF mdInformation)
  {
    HashMap<String, String> parameters = getMdTransientParameters(mdInformation);

    Map<String, String> localValues = mdInformation.getMessages();
    writeLocaleValues(parameters, XMLTags.MESSAGE_ATTRIBUTE, localValues);

    // Get the super entity
    MdInformationDAOIF superClass = mdInformation.getSuperClass();

    if (superClass != null)
    {
      String superType = superClass.definesType();

      // Add the super class to the extends mapping
      parameters.put(XMLTags.EXTENDS_ATTRIBUTE, superType);
    }

    return parameters;
  }

  private HashMap<String, String> getMdViewParameters(MdViewDAOIF mdView)
  {
    HashMap<String, String> parameters = getMdTransientParameters(mdView);

    // Get the super entity
    MdViewDAOIF superClass = mdView.getSuperClass();

    if (superClass != null)
    {
      String superType = superClass.definesType();

      // Add the super class to the extends mapping
      parameters.put(XMLTags.EXTENDS_ATTRIBUTE, superType);
    }

    return parameters;
  }

  private HashMap<String, String> getMdUtilParameters(MdUtilDAOIF mdUtil)
  {
    HashMap<String, String> parameters = getMdTransientParameters(mdUtil);

    // Get the super entity
    MdUtilDAOIF superClass = mdUtil.getSuperClass();

    if (superClass != null)
    {
      String superType = superClass.definesType();

      // Add the super class to the extends mapping
      parameters.put(XMLTags.EXTENDS_ATTRIBUTE, superType);
    }

    return parameters;
  }

  /**
   * Returns the parameter-value mappings defined for a MdEntity of a given
   * MdEntity.
   * 
   * @param mdClassIF
   *          The MdEntity to get the parameters
   * 
   * @return A HashMap of the parameter-value pairs
   */
  private HashMap<String, String> getMdClassParameters(MdClassDAOIF mdClassIF)
  {
    HashMap<String, String> parameters = new HashMap<String, String>();

    String name = mdClassIF.definesType();

    // Map the parameter value to its correct attribute tag
    parameters.put(XMLTags.NAME_ATTRIBUTE, name);

    Map<String, String> localValues = mdClassIF.getDisplayLabels();
    writeLocaleValues(parameters, XMLTags.DISPLAY_LABEL_ATTRIBUTE, localValues);

    parameters.put(XMLTags.REMOVE_ATTRIBUTE, mdClassIF.getValue(MdEntityInfo.REMOVE));

    Map<String, String> localDescValues = mdClassIF.getDescriptions();
    writeLocaleValues(parameters, XMLTags.DESCRIPTION_ATTRIBUTE, localDescValues);

    if (mdClassIF.getSuperClass() == null)
    {
      parameters.put(XMLTags.PUBLISH_ATTRIBUTE, mdClassIF.getValue(MdEntityInfo.PUBLISH));
    }

    return parameters;
  }

  private HashMap<String, String> getMdEntityParameters(MdEntityDAOIF mdEntity)
  {
    HashMap<String, String> parameters = getMdClassParameters(mdEntity);

    parameters.put(XMLTags.ENTITY_TABLE, mdEntity.getTableName());

    return parameters;
  }

  private HashMap<String, String> getMdElementParameters(MdElementDAOIF mdElement)
  {
    HashMap<String, String> parameters = getMdEntityParameters(mdElement);

    parameters.put(XMLTags.EXTENDABLE_ATTRIBUTE, mdElement.getValue(MdElementInfo.EXTENDABLE));
    parameters.put(XMLTags.ABSTRACT_ATTRIBUTE, mdElement.getValue(MdElementInfo.ABSTRACT));
    parameters.put(XMLTags.GENERATE_SOURCE, mdElement.getValue(MdElementInfo.GENERATE_SOURCE));

    return parameters;
  }

  private HashMap<String, String> getMdStructParameters(MdEntityDAOIF mdStruct)
  {
    HashMap<String, String> parameters = getMdEntityParameters(mdStruct);

    // Get the cache alogrithm
    String indexType = ( (AttributeEnumerationIF) mdStruct.getAttributeIF(MdStructInfo.CACHE_ALGORITHM) ).dereference()[0].getOid();

    if (indexType.equals(EntityCacheMaster.CACHE_EVERYTHING.getOid()))
    {
      parameters.put(XMLTags.CACHE_ALGORITHM_ATTRIBUTE, XMLTags.EVERYTHING_ENUMERATION);
    }
    else if (indexType.equals(EntityCacheMaster.CACHE_NOTHING.getOid()))
    {
      parameters.put(XMLTags.CACHE_ALGORITHM_ATTRIBUTE, XMLTags.NOTHING_ENUMERATION);
    }
    else
    {
      parameters.put(XMLTags.CACHE_ALGORITHM_ATTRIBUTE, XMLTags.RECENTLY_ENUMERATION);
    }

    parameters.put(XMLTags.CACHE_SIZE_ATTRIBUTE, mdStruct.getValue(MdStructInfo.CACHE_SIZE));

    return parameters;
  }

  private HashMap<String, String> getMdIndexParameters(MdIndexDAOIF mdIndex)
  {
    HashMap<String, String> attributes = new HashMap<String, String>();

    String unqiue = mdIndex.getValue(MdIndexInfo.UNIQUE);
    String active = mdIndex.getValue(MdIndexInfo.ACTIVE);
    String oid = mdIndex.getValue(MdIndexInfo.MD_ENTITY);
    String label = mdIndex.getStructValue(MdIndexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE);
    MdElementDAOIF mdEntity = MdElementDAO.get(oid);

    attributes.put(XMLTags.INDEX_ACTIVE_ATTRIBUTE, active);
    attributes.put(XMLTags.INDEX_UNIQUE_ATTRIBUTE, unqiue);
    attributes.put(XMLTags.INDEX_ENTITY_TYPE_ATTRIBUTE, mdEntity.definesType());
    attributes.put(XMLTags.DISPLAY_LABEL_ATTRIBUTE, label);

    return attributes;
  }

  /**
   * Returns the parameter-value mappings for all of the MdAttribute parameters
   * 
   * @param mdAttributeIF
   *          The attribute to get the parameters
   * @return A HashMap of the parameter-value pairs
   */
  private HashMap<String, String> getAttributeParameters(MdAttributeDAOIF mdAttributeIF)
  {
    HashMap<String, String> parameters = new HashMap<String, String>();

    StringBuilder builder = new StringBuilder();
    List<MdAttributeDimensionDAOIF> mdAttributeDimensions = mdAttributeIF.getMdAttributeDimensions();

    for (MdAttributeDimensionDAOIF mdAttributeDimension : mdAttributeDimensions)
    {
      if (mdAttributeDimension.getValue(MdAttributeDimensionInfo.REQUIRED).equals(MdAttributeBooleanInfo.TRUE))
      {
        builder.append(", " + mdAttributeDimension.definingMdDimension().getName());
      }
    }

    if (metadata != null && metadata.hasRename(mdAttributeIF))
    {
      parameters.put(XMLTags.RENAME_ATTRIBUTE, metadata.getRename(mdAttributeIF));
    }
    
    String requiredForDimension = builder.toString();

    if (requiredForDimension.length() > 0)
    {
      String _requiredForDimension = requiredForDimension.replaceFirst(",", "").trim();

      parameters.put(XMLTags.REQUIRED_FOR_DIMENSION_ATTRIBUTE, _requiredForDimension);
    }

    if (mdAttributeIF instanceof MdAttributeVirtualDAOIF)
    {
      MdAttributeVirtualDAOIF virtual = (MdAttributeVirtualDAOIF) mdAttributeIF;
      String concreteId = virtual.getValue(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE);

      MdAttributeConcreteDAOIF concrete = MdAttributeConcreteDAO.get(concreteId);
      String classId = concrete.getValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS);
      MdClassDAOIF mdClass = (MdClassDAOIF) MdClassDAO.get(classId);

      parameters.put(XMLTags.TYPE_ATTRIBUTE, mdClass.definesType());
      parameters.put(XMLTags.CONCRETE_ATTRIBUTE, concrete.definesAttribute());
      parameters.put(XMLTags.NAME_ATTRIBUTE, mdAttributeIF.getValue(MdAttributeVirtualInfo.NAME));

      Map<String, String> localValues = mdAttributeIF.getDisplayLabels();
      writeLocaleValues(parameters, XMLTags.DISPLAY_LABEL_ATTRIBUTE, localValues);

      // Map the parameter value to its correct attribute tag for all common
      // parameters
      if (!mdAttributeIF.getValue(MetadataInfo.DESCRIPTION).equals(""))
      {
        Map<String, String> localDescValues = mdAttributeIF.getDescriptions();
        writeLocaleValues(parameters, XMLTags.DESCRIPTION_ATTRIBUTE, localDescValues);
      }

      if (!virtual.getValue(MdAttributeVirtualInfo.REQUIRED).equals(""))
      {
        parameters.put(XMLTags.REQUIRED_ATTRIBUTE, virtual.getValue(MdAttributeVirtualInfo.REQUIRED));
      }
    }

    if (mdAttributeIF instanceof MdAttributeConcreteDAOIF)
    {
      // Map the parameter value to its correct attribute tag for all common
      // parameters
      Map<String, String> localValues = mdAttributeIF.getDisplayLabels();
      writeLocaleValues(parameters, XMLTags.DISPLAY_LABEL_ATTRIBUTE, localValues);

      Map<String, String> localDescValues = mdAttributeIF.getDescriptions();
      writeLocaleValues(parameters, XMLTags.DESCRIPTION_ATTRIBUTE, localDescValues);

      parameters.put(XMLTags.REQUIRED_ATTRIBUTE, mdAttributeIF.getValue(MdAttributeConcreteInfo.REQUIRED));
      parameters.put(XMLTags.IMMUTABLE_ATTRIBUTE, mdAttributeIF.getValue(MdAttributeConcreteInfo.IMMUTABLE));
      parameters.put(XMLTags.NAME_ATTRIBUTE, mdAttributeIF.getValue(MdAttributeConcreteInfo.NAME));
      parameters.put(XMLTags.COLUMN_ATTRIBUTE, mdAttributeIF.getValue(MdAttributeConcreteInfo.COLUMN_NAME));
      parameters.put(XMLTags.REMOVE_ATTRIBUTE, mdAttributeIF.getValue(MetadataInfo.REMOVE));

      String indexType = ( (AttributeEnumerationIF) mdAttributeIF.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE) ).dereference()[0].getOid();

      if (indexType.equals(IndexTypes.UNIQUE_INDEX.getOid()))
      {
        parameters.put(XMLTags.INDEX_TYPE_ATTRIBUTE, XMLTags.UNIQUE_INDEX_ENUMERATION);
      }
      else if (indexType.equals(IndexTypes.NON_UNIQUE_INDEX.getOid()))
      {
        parameters.put(XMLTags.INDEX_TYPE_ATTRIBUTE, XMLTags.NON_UNIQUE_INDEX_ENUMERATION);
      }
      else
      {
        parameters.put(XMLTags.INDEX_TYPE_ATTRIBUTE, XMLTags.NO_INDEX_ENUMERATION);
      }

      String getterVisibility = ( (AttributeEnumerationIF) mdAttributeIF.getAttributeIF(MdAttributeConcreteInfo.GETTER_VISIBILITY) ).dereference()[0].getOid();

      if (getterVisibility.equals(VisibilityModifier.PUBLIC.getOid()))
      {
        parameters.put(XMLTags.GETTER_VISIBILITY_ATTRIBUTE, XMLTags.PUBLIC_VISIBILITY_ENUMERATION);
      }
      else
      {
        parameters.put(XMLTags.GETTER_VISIBILITY_ATTRIBUTE, XMLTags.PROTECTED_VISIBILITY_ENUMERATION);
      }

      String setterVisibility = ( (AttributeEnumerationIF) mdAttributeIF.getAttributeIF(MdAttributeConcreteInfo.SETTER_VISIBILITY) ).dereference()[0].getOid();

      if (setterVisibility.equals(VisibilityModifier.PUBLIC.getOid()))
      {
        parameters.put(XMLTags.SETTER_VISIBILITY_ATTRIBUTE, XMLTags.PUBLIC_VISIBILITY_ENUMERATION);
      }
    }

    if (mdAttributeIF instanceof MdAttributePrimitiveDAOIF)
    {
      addParameter(mdAttributeIF, parameters, XMLTags.IS_EXPRESSION, MdAttributePrimitiveInfo.IS_EXPRESSION);
      addParameter(mdAttributeIF, parameters, XMLTags.EXPRESSION, MdAttributePrimitiveInfo.EXPRESSION);

      parameters.put(XMLTags.DEFAULT_VALUE_ATTRIBUTE, mdAttributeIF.getValue(MdAttributeConcreteInfo.DEFAULT_VALUE));
    }

    // Map the parameter value to its correct attribute tag for parameters
    // common to integer, long, float, double, and decimal types
    if (mdAttributeIF instanceof MdAttributeNumberDAOIF)
    {
      parameters.put(XMLTags.REJECT_POSITIVE_ATTRIBUTE, mdAttributeIF.getValue(MdAttributeNumberInfo.REJECT_POSITIVE));
      parameters.put(XMLTags.REJECT_NEGATIVE_ATTRIBUTE, mdAttributeIF.getValue(MdAttributeNumberInfo.REJECT_NEGATIVE));
      parameters.put(XMLTags.REJECT_ZERO_ATTRIBUTE, mdAttributeIF.getValue(MdAttributeNumberInfo.REJECT_ZERO));
    }

    // Map the parameter value to its correct attribute tag for parameters
    // common to float, double, and decimal types
    if (mdAttributeIF instanceof MdAttributeDecDAOIF)
    {
      parameters.put(XMLTags.LENGTH_ATTRIBUTE, mdAttributeIF.getValue(MdAttributeDecInfo.LENGTH));
      parameters.put(XMLTags.DECIMAL_TAG, mdAttributeIF.getValue(MdAttributeDecInfo.DECIMAL));
    }

    // Map the parameter value to its correct attribute tag for parameters
    // common to character type
    if (mdAttributeIF instanceof MdAttributeCharacterDAOIF)
    {
      parameters.put(XMLTags.SIZE_ATTRIBUTE, mdAttributeIF.getValue(MdAttributeCharacterInfo.SIZE));
    }

    // Map the parameter value to its correct attribute tag for parameters
    // common to booleans type
    if (mdAttributeIF instanceof MdAttributeBooleanDAOIF)
    {
      MdAttributeBooleanDAOIF mdAttributeBooleanDAOIF = (MdAttributeBooleanDAOIF) mdAttributeIF;

      Map<String, String> localPositiveValues = mdAttributeBooleanDAOIF.getPositiveDisplayLabels();
      writeLocaleValues(parameters, XMLTags.POSITIVE_DISPLAY_LABEL_ATTRIBUTE, localPositiveValues);

      Map<String, String> localNegativeValues = mdAttributeBooleanDAOIF.getNegativeDisplayLabels();
      writeLocaleValues(parameters, XMLTags.NEGATIVE_DISPLAY_LABEL_ATTRIBUTE, localNegativeValues);
    }

    // Map the parameter value to its correct attribute tag for parameters
    // common to struct type

    if (mdAttributeIF instanceof MdAttributeStructDAOIF && ! ( mdAttributeIF instanceof MdAttributeLocalDAOIF ))
    {
      MdStructDAOIF mdStructIF = ( (MdAttributeStructDAOIF) mdAttributeIF ).getMdStructDAOIF();

      String classType = mdStructIF.getValue(MdTypeInfo.PACKAGE) + "." + mdStructIF.getValue(MdTypeInfo.NAME);

      parameters.put(XMLTags.TYPE_ATTRIBUTE, classType);
    }

    // Map the parameter value to its correct attribute tag for parameters
    // common to foriegnObject type
    if (mdAttributeIF instanceof MdAttributeReferenceDAOIF)
    {
      parameters.put(XMLTags.DEFAULT_KEY_ATTRIBUTE, mdAttributeIF.getValue(MdAttributeConcreteInfo.DEFAULT_VALUE));

      MdBusinessDAOIF refClass = ( (MdAttributeReferenceDAOIF) mdAttributeIF ).getReferenceMdBusinessDAO();

      String classType = refClass.getValue(MdTypeInfo.PACKAGE) + "." + refClass.getValue(MdTypeInfo.NAME);

      parameters.put(XMLTags.TYPE_ATTRIBUTE, classType);

      // Overload the value of the defaultValue parameter
      String defaultValue = mdAttributeIF.getValue(MdAttributeConcreteInfo.DEFAULT_VALUE);

      if (!defaultValue.equals(""))
      {
        parameters.put(XMLTags.DEFAULT_KEY_ATTRIBUTE, EntityDAO.get(defaultValue).getKey());
      }
      else
      {
        parameters.remove(XMLTags.DEFAULT_KEY_ATTRIBUTE);
      }
    }

    // Map the parameter value to its correct attribute tag for parameters
    // common to foriegnObject type
    if (mdAttributeIF instanceof MdAttributeGraphReferenceDAOIF)
    {
      MdClassDAOIF refClass = ( (MdAttributeGraphReferenceDAOIF) mdAttributeIF ).getReferenceMdVertexDAOIF();

      String classType = refClass.getValue(MdTypeInfo.PACKAGE) + "." + refClass.getValue(MdTypeInfo.NAME);

      parameters.put(XMLTags.TYPE_ATTRIBUTE, classType);
    }

    // Map the parameter value to its correct attribute tag for parameters
    // common to foriegnObject type
    if (mdAttributeIF instanceof MdAttributeMultiReferenceDAOIF)
    {
      parameters.put(XMLTags.DEFAULT_KEY_ATTRIBUTE, mdAttributeIF.getValue(MdAttributeConcreteInfo.DEFAULT_VALUE));

      MdBusinessDAOIF refClass = ( (MdAttributeMultiReferenceDAOIF) mdAttributeIF ).getReferenceMdBusinessDAO();

      String classType = refClass.getValue(MdTypeInfo.PACKAGE) + "." + refClass.getValue(MdTypeInfo.NAME);

      parameters.put(XMLTags.TYPE_ATTRIBUTE, classType);

      // Overload the value of the defaultValue parameter
      String defaultValue = mdAttributeIF.getValue(MdAttributeConcreteInfo.DEFAULT_VALUE);

      if (!defaultValue.equals(""))
      {
        parameters.put(XMLTags.DEFAULT_KEY_ATTRIBUTE, EntityDAO.get(defaultValue).getKey());
      }
      else
      {
        parameters.remove(XMLTags.DEFAULT_KEY_ATTRIBUTE);
      }
    }

    // Map the parameter value to its correct attribute tag for parameters
    // common to foriegnProperty type
    if (mdAttributeIF instanceof MdAttributeEnumerationDAOIF)
    {
      parameters.put(XMLTags.DEFAULT_VALUE_ATTRIBUTE, mdAttributeIF.getValue(MdAttributeConcreteInfo.DEFAULT_VALUE));
      MdEnumerationDAOIF refClass = ( (MdAttributeEnumerationDAOIF) mdAttributeIF ).getMdEnumerationDAO();

      String classType = refClass.getValue(MdTypeInfo.PACKAGE) + "." + refClass.getValue(MdTypeInfo.NAME);

      parameters.put(XMLTags.TYPE_ATTRIBUTE, classType);
      parameters.put(XMLTags.SELECT_MULTIPLE_ATTRIBUTE, mdAttributeIF.getValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE));

      // Overload the value of the defaultValue parameter
      String defaultValue = mdAttributeIF.getValue(MdAttributeConcreteInfo.DEFAULT_VALUE);
      if (!defaultValue.equals(""))
      {
        parameters.put(XMLTags.DEFAULT_VALUE_ATTRIBUTE, EnumerationItemDAO.get(defaultValue).getName());
      }
      else
      {
        parameters.remove(XMLTags.DEFAULT_VALUE_ATTRIBUTE);
      }
    }

    if (mdAttributeIF instanceof MdAttributeEncryptionDAOIF)
    {
      parameters.put(XMLTags.DEFAULT_VALUE_ATTRIBUTE, mdAttributeIF.getValue(MdAttributeConcreteInfo.DEFAULT_VALUE));
    }

    if (mdAttributeIF instanceof MdAttributeHashDAOIF)
    {
      BusinessDAOIF hashMethod = ( (AttributeEnumerationIF) mdAttributeIF.getAttributeIF(MdAttributeHashInfo.HASH_METHOD) ).dereference()[0];

      parameters.put(XMLTags.HASH_METHOD_ATTRIBUTE, hashMethod.getValue(EnumerationMasterInfo.NAME));
    }

    if (mdAttributeIF instanceof MdAttributeSymmetricDAOIF)
    {
      BusinessDAOIF hashMethod = ( (AttributeEnumerationIF) mdAttributeIF.getAttributeIF(MdAttributeSymmetricInfo.SYMMETRIC_METHOD) ).dereference()[0];

      parameters.put(XMLTags.SYMMETRIC_METHOD_ATTRIBUTE, hashMethod.getValue(EnumerationMasterInfo.NAME));
      parameters.put(XMLTags.SYMMETRIC_KEYSIZE_ATTRIBUTE, mdAttributeIF.getValue(MdAttributeSymmetricInfo.SECRET_KEY_SIZE));
    }

    for (PluginIF plugin : pluginMap.values())
    {
      plugin.addParameters(parameters, mdAttributeIF);
    }

    return parameters;
  }

  private HashMap<String, String> getFieldParameters(MdFieldDAOIF mdField)
  {
    HashMap<String, String> parameters = new HashMap<String, String>();

    parameters.put(XMLTags.NAME_ATTRIBUTE, mdField.getFieldName());
    parameters.put(XMLTags.PARAMETER_ORDER_ATTRIBUTE, mdField.getFieldOrder());
    parameters.put(XMLTags.REQUIRED_ATTRIBUTE, mdField.getValue(MdFieldInfo.REQUIRED));
    parameters.put(XMLTags.REMOVE_ATTRIBUTE, mdField.getValue(MdFieldInfo.REMOVE));

    Map<String, String> localValues = mdField.getDisplayLabels();
    writeLocaleValues(parameters, XMLTags.DISPLAY_LABEL_ATTRIBUTE, localValues);

    if (!mdField.getValue(MetadataInfo.DESCRIPTION).equals(""))
    {
      Map<String, String> localDescValues = mdField.getDescriptions();
      writeLocaleValues(parameters, XMLTags.DESCRIPTION_ATTRIBUTE, localDescValues);
    }

    if (mdField instanceof MdWebAttributeDAOIF)
    {
      addParameter(mdField, parameters, XMLTags.SHOW_ON_SEARCH, MdWebAttributeInfo.SHOW_ON_SEARCH);
      addParameter(mdField, parameters, XMLTags.SHOW_ON_VIEW_ALL, MdWebAttributeInfo.SHOW_ON_VIEW_ALL);

      MdWebAttributeDAOIF mdWebAttribute = (MdWebAttributeDAOIF) mdField;

      parameters.put(XMLTags.MD_ATTRIBUTE, mdWebAttribute.getDefiningMdAttribute().definesAttribute());
    }

    if (mdField instanceof MdWebPrimitiveDAOIF)
    {
      addParameter(mdField, parameters, XMLTags.IS_EXPRESSION, MdAttributePrimitiveInfo.IS_EXPRESSION);
      addParameter(mdField, parameters, XMLTags.EXPRESSION, MdAttributePrimitiveInfo.EXPRESSION);
    }

    if (mdField instanceof MdWebNumberDAOIF)
    {
      MdWebNumberDAOIF mdWebNumber = (MdWebNumberDAOIF) mdField;

      this.addParameter(mdWebNumber, parameters, XMLTags.STARTRANGE, MdWebNumberInfo.STARTRANGE);
      this.addParameter(mdWebNumber, parameters, XMLTags.ENDRANGE, MdWebNumberInfo.ENDRANGE);
    }

    if (mdField instanceof MdWebDecDAOIF)
    {
      MdWebDecDAOIF mdWebDec = (MdWebDecDAOIF) mdField;

      parameters.put(XMLTags.DEC_PRECISION, mdWebDec.getValue(MdWebDecInfo.DECPRECISION));
      parameters.put(XMLTags.DEC_SCALE, mdWebDec.getValue(MdWebDecInfo.DECSCALE));
    }

    if (mdField instanceof MdWebCharacterDAOIF)
    {
      MdWebCharacterDAOIF mdWebCharacter = (MdWebCharacterDAOIF) mdField;

      parameters.put(XMLTags.MAX_LENGTH, mdWebCharacter.getValue(MdWebCharacterInfo.MAX_LENGTH));
      parameters.put(XMLTags.DISPLAY_LENGTH, mdWebCharacter.getValue(MdWebCharacterInfo.DISPLAY_LENGTH));
    }
    else if (mdField instanceof MdWebBooleanDAOIF)
    {
      MdWebBooleanDAOIF mdWebBoolean = (MdWebBooleanDAOIF) mdField;

      this.addParameter(mdWebBoolean, parameters, XMLTags.DEFAULT_VALUE_ATTRIBUTE, MdWebBooleanInfo.DEFAULT_VALUE);
    }
    else if (mdField instanceof MdWebDateDAOIF)
    {
      MdWebDateDAOIF mdWebDate = (MdWebDateDAOIF) mdField;

      this.addParameter(mdWebDate, parameters, XMLTags.AFTER_TODAY_EXCLUSIVE, MdWebDateInfo.AFTER_TODAY_EXCLUSIVE);
      this.addParameter(mdWebDate, parameters, XMLTags.AFTER_TODAY_INCLUSIVE, MdWebDateInfo.AFTER_TODAY_INCLUSIVE);
      this.addParameter(mdWebDate, parameters, XMLTags.BEFORE_TODAY_EXCLUSIVE, MdWebDateInfo.BEFORE_TODAY_EXCLUSIVE);
      this.addParameter(mdWebDate, parameters, XMLTags.BEFORE_TODAY_INCLUSIVE, MdWebDateInfo.BEFORE_TODAY_INCLUSIVE);
      this.addParameter(mdWebDate, parameters, XMLTags.START_DATE, MdWebDateInfo.START_DATE);
      this.addParameter(mdWebDate, parameters, XMLTags.END_DATE, MdWebDateInfo.END_DATE);
    }
    else if (mdField instanceof MdWebTextDAOIF)
    {
      MdWebTextDAOIF mdWebText = (MdWebTextDAOIF) mdField;

      parameters.put(XMLTags.HEIGHT, mdWebText.getValue(MdWebTextInfo.HEIGHT));
      parameters.put(XMLTags.WIDTH, mdWebText.getValue(MdWebTextInfo.WIDTH));
    }
    else if (mdField instanceof MdWebCommentDAOIF)
    {
      MdWebCommentDAOIF mdWebComment = (MdWebCommentDAOIF) mdField;

      parameters.put(XMLTags.COMMENT_TEXT, mdWebComment.getValue(MdWebCommentInfo.COMMENT_TEXT));
    }

    return parameters;
  }

  /**
   * @param metadata
   * @param parameters
   * @param parameterName
   */
  private void addParameter(MetadataDAOIF metadata, HashMap<String, String> parameters, String parameterName, String attributeName)
  {
    if (!metadata.getValue(attributeName).equals(""))
    {
      parameters.put(parameterName, metadata.getValue(attributeName));
    }
  }

  /**
   * Writes local values to the attribute with the given name
   * 
   * @param attributes
   * @param tagName
   * @param localValues
   */
  protected static void writeLocaleValues(HashMap<String, String> attributes, String tagName, Map<String, String> localValues)
  {
    for (Entry<String, String> entry : localValues.entrySet())
    {
      String locale = entry.getKey();
      if (locale.equals(MdAttributeLocalInfo.DEFAULT_LOCALE))
      {
        attributes.put(tagName, entry.getValue());
      }
      else
      {
        // attributes.put(tagName + "_" + locale, localValues.get(locale));
      }
    }
  }

  /**
   * Returns the parameter-value mappings for all of the MdEnumeration
   * parameters
   * 
   * @param filter
   *          The MdEnumeration to get the parameters
   * @return A HashMap of the parameter-value pairs
   */
  private HashMap<String, String> getFilterParameters(MdEnumerationDAOIF filter)
  {
    HashMap<String, String> parameters = new HashMap<String, String>();

    String name = filter.definesEnumeration();

    // Map the parameter value to its correct attribute tag
    parameters.put(XMLTags.NAME_ATTRIBUTE, name);

    Map<String, String> localValues = filter.getDisplayLabels();
    writeLocaleValues(parameters, XMLTags.DISPLAY_LABEL_ATTRIBUTE, localValues);

    parameters.put(XMLTags.REMOVE_ATTRIBUTE, filter.getValue(MetadataInfo.REMOVE));

    Map<String, String> localDescValues = filter.getDescriptions();
    writeLocaleValues(parameters, XMLTags.DESCRIPTION_ATTRIBUTE, localDescValues);

    MdBusinessDAOIF refClass = filter.getMasterListMdBusinessDAO();

    parameters.put(XMLTags.ENUMERATION_TABLE, filter.getTableName());

    String classType = refClass.getValue(MdTypeInfo.PACKAGE) + "." + refClass.getValue(MdTypeInfo.NAME);

    parameters.put(XMLTags.TYPE_ATTRIBUTE, classType);

    return parameters;
  }

  /**
   * Returns the parameter-value mappings for all of the Parent parameters
   * 
   * @param relationship
   *          The MdRelationship to get the Parent parameters
   * @return A HashMap of the parameter-value pairs
   */
  private HashMap<String, String> getParentParameters(MdRelationshipDAOIF relationship)
  {
    HashMap<String, String> parameters = new HashMap<String, String>();

    MdBusinessDAOIF refClass = ( (MdRelationshipDAOIF) relationship ).getParentMdBusiness();

    String classType = refClass.definesType();

    parameters.put(XMLTags.NAME_ATTRIBUTE, classType);

    Map<String, String> localValues = relationship.getParentDisplayLabes();
    writeLocaleValues(parameters, XMLTags.DISPLAY_LABEL_ATTRIBUTE, localValues);

    parameters.put(XMLTags.CARDINALITY_ATTRIBUTE, relationship.getValue(MdRelationshipInfo.PARENT_CARDINALITY));
    parameters.put(XMLTags.RELATIONSHIP_METHOD_TAG, relationship.getValue(MdRelationshipInfo.PARENT_METHOD));
    return parameters;
  }

  /**
   * Returns the parameter-value mappings for all of the Child parameters
   * 
   * @param relationship
   *          The MdRelationship to get the Child parameters
   * @return A HashMap of the parameter-value pairs
   */
  private HashMap<String, String> getChildParameters(MdRelationshipDAOIF relationship)
  {
    HashMap<String, String> parameters = new HashMap<String, String>();

    MdBusinessDAOIF refClass = ( (MdRelationshipDAOIF) relationship ).getChildMdBusiness();

    String classType = refClass.definesType();

    parameters.put(XMLTags.NAME_ATTRIBUTE, classType);

    Map<String, String> localValues = relationship.getChildDisplayLabes();
    writeLocaleValues(parameters, XMLTags.DISPLAY_LABEL_ATTRIBUTE, localValues);

    parameters.put(XMLTags.CARDINALITY_ATTRIBUTE, relationship.getValue(MdRelationshipInfo.CHILD_CARDINALITY));
    parameters.put(XMLTags.RELATIONSHIP_METHOD_TAG, relationship.getValue(MdRelationshipInfo.CHILD_METHOD));

    return parameters;
  }

  /**
   * Returns the parameter-value mappings for all of the MdElement parameters
   * 
   * @param mdRelationship
   *          The MdElement to get the parameters
   * @return A HashMap of the parameter-value pairs
   */
  private HashMap<String, String> getMdRelationshipParameters(MdRelationshipDAOIF mdRelationship)
  {
    HashMap<String, String> parameters = getMdElementParameters(mdRelationship);

    // Set the composition flag
    parameters.put(XMLTags.COMPOSITION_ATTRIBUTE, mdRelationship.getValue(MdRelationshipInfo.COMPOSITION));

    // Set the cache algorithm
    String indexType = ( (AttributeEnumerationIF) mdRelationship.getAttributeIF(MdRelationshipInfo.CACHE_ALGORITHM) ).dereference()[0].getOid();

    if (indexType.equals(EntityCacheMaster.CACHE_EVERYTHING.getOid()))
    {
      parameters.put(XMLTags.CACHE_ALGORITHM_ATTRIBUTE, XMLTags.EVERYTHING_ENUMERATION);
    }
    else
    {
      parameters.put(XMLTags.CACHE_ALGORITHM_ATTRIBUTE, XMLTags.NOTHING_ENUMERATION);
    }

    if (mdRelationship instanceof MdTermRelationshipDAOIF)
    {
      // Set the association type
      String associationType = ( (AttributeEnumerationIF) mdRelationship.getAttributeIF(MdTermRelationshipInfo.ASSOCIATION_TYPE) ).dereference()[0].getOid();

      if (associationType.equals(AssociationType.RELATIONSHIP.getOid()))
      {
        parameters.put(XMLTags.ASSOCIATION_TYPE_ATTRIBUTE, XMLTags.RELATIONSHIP_OPTION);
      }
      else if (associationType.equals(AssociationType.TREE.getOid()))
      {
        parameters.put(XMLTags.ASSOCIATION_TYPE_ATTRIBUTE, XMLTags.TREE_OPTION);
      }
      else if (associationType.equals(AssociationType.GRAPH.getOid()))
      {
        parameters.put(XMLTags.ASSOCIATION_TYPE_ATTRIBUTE, XMLTags.GRAPH_OPTION);
      }
    }

    // Set the super MdRelationship
    MdElementDAOIF superClass = mdRelationship.getSuperClass();

    if (superClass != null)
    {
      String superType = superClass.definesType();

      // Add the super class to the extends mapping
      parameters.put(XMLTags.EXTENDS_ATTRIBUTE, superType);
    }

    return parameters;
  }

  /**
   * Get the XML tag based on the MdAttribute
   * 
   * @param attribute
   *          An MdAttriubteIF != null
   * @return The corresponding XML tag of the attribute
   */
  private static String getTagName(MdAttributeDAOIF attribute)
  {
    return attributeTags.get(attribute.getType());
  }

  private static String getTagName(MdFieldDAOIF field)
  {
    return attributeTags.get(field.getType());
  }

  private static String getTagName(FieldConditionDAOIF condition)
  {
    return attributeTags.get(condition.getType());
  }

  /**
   * The list of attributes to mask when exporting a MdEntity
   * 
   * @return
   */
  private static TreeSet<String> getAttriubteMask()
  {
    TreeSet<String> tree = new TreeSet<String>();

    tree.add(ElementInfo.CREATED_BY);
    tree.add(ElementInfo.LAST_UPDATED_BY);
    tree.add(EntityInfo.OID);
    tree.add(ElementInfo.LAST_UPDATE_DATE);
    tree.add(EntityInfo.TYPE);
    tree.add(MdAttributeConcreteInfo.DEFAULT_VALUE);
    tree.add(ElementInfo.CREATE_DATE);
    tree.add(ElementInfo.LOCKED_BY);
    tree.add(ElementInfo.OWNER);
    tree.add(ElementInfo.SEQUENCE);
    tree.add(ElementInfo.SITE_MASTER);
    tree.add(EntityInfo.KEY);
    tree.add(ElementInfo.DOMAIN);

    return tree;
  }

  /**
   * Loads the mapping between MdAttribute and XMLTag into the attribute_tags
   * HashMap
   */
  private static HashMap<String, String> loadMapping()
  {
    HashMap<String, String> attributeTags = new HashMap<String, String>();

    // attribute types
    attributeTags.put(MdAttributeUUIDInfo.CLASS, XMLTags.UUID_TAG);
    attributeTags.put(MdAttributeIntegerInfo.CLASS, XMLTags.INTEGER_TAG);
    attributeTags.put(MdAttributeLongInfo.CLASS, XMLTags.LONG_TAG);
    attributeTags.put(MdAttributeFloatInfo.CLASS, XMLTags.FLOAT_TAG);
    attributeTags.put(MdAttributeDoubleInfo.CLASS, XMLTags.DOUBLE_TAG);
    attributeTags.put(MdAttributeDecimalInfo.CLASS, XMLTags.DECIMAL_TAG);
    attributeTags.put(MdAttributeTimeInfo.CLASS, XMLTags.TIME_TAG);
    attributeTags.put(MdAttributeDateTimeInfo.CLASS, XMLTags.DATETIME_TAG);
    attributeTags.put(MdAttributeDateInfo.CLASS, XMLTags.DATE_TAG);
    attributeTags.put(MdAttributeCharacterInfo.CLASS, XMLTags.CHARACTER_TAG);
    attributeTags.put(MdAttributeTextInfo.CLASS, XMLTags.TEXT_TAG);
    attributeTags.put(MdAttributeJsonInfo.CLASS, XMLTags.JSON_TAG);
    attributeTags.put(MdAttributeClobInfo.CLASS, XMLTags.CLOB_TAG);
    attributeTags.put(MdAttributeLocalCharacterInfo.CLASS, XMLTags.LOCAL_CHARACTER_TAG);
    attributeTags.put(MdAttributeLocalTextInfo.CLASS, XMLTags.LOCAL_TEXT_TAG);
    attributeTags.put(MdAttributeBooleanInfo.CLASS, XMLTags.BOOLEAN_TAG);
    attributeTags.put(MdAttributeStructInfo.CLASS, XMLTags.STRUCT_TAG);
    attributeTags.put(MdAttributeReferenceInfo.CLASS, XMLTags.REFERENCE_TAG);
    attributeTags.put(MdAttributeTermInfo.CLASS, XMLTags.TERM_TAG);
    attributeTags.put(MdAttributeEnumerationInfo.CLASS, XMLTags.ENUMERATION_TAG);
    attributeTags.put(MdAttributeMultiReferenceInfo.CLASS, XMLTags.MULTI_REFERENCE_TAG);
    attributeTags.put(MdAttributeMultiTermInfo.CLASS, XMLTags.MULTI_TERM_TAG);
    attributeTags.put(MdAttributeHashInfo.CLASS, XMLTags.HASH_TAG);
    attributeTags.put(MdAttributeSymmetricInfo.CLASS, XMLTags.SYMMETRIC_TAG);
    attributeTags.put(MdAttributeBlobInfo.CLASS, XMLTags.BLOB_TAG);
    attributeTags.put(MdAttributeFileInfo.CLASS, XMLTags.FILE_TAG);
    attributeTags.put(MdAttributeVirtualInfo.CLASS, XMLTags.VIRTUAL_TAG);
    attributeTags.put(MdAttributeEmbeddedInfo.CLASS, XMLTags.EMBEDDED_TAG);
    attributeTags.put(MdAttributeGraphReferenceInfo.CLASS, XMLTags.GRAPH_REFERENCE_TAG);
    attributeTags.put(MdAttributeLocalCharacterEmbeddedInfo.CLASS, XMLTags.LOCAL_CHARACTER_EMBEDDED_TAG);

    // Field types
    attributeTags.put(MdWebIntegerInfo.CLASS, XMLTags.INTEGER_TAG);
    attributeTags.put(MdWebFloatInfo.CLASS, XMLTags.FLOAT_TAG);
    attributeTags.put(MdWebDoubleInfo.CLASS, XMLTags.DOUBLE_TAG);
    attributeTags.put(MdWebDecimalInfo.CLASS, XMLTags.DECIMAL_TAG);
    attributeTags.put(MdWebDateInfo.CLASS, XMLTags.DATE_TAG);
    attributeTags.put(MdWebTimeInfo.CLASS, XMLTags.TIME_TAG);
    attributeTags.put(MdWebDateTimeInfo.CLASS, XMLTags.DATETIME_TAG);
    attributeTags.put(MdWebCharacterInfo.CLASS, XMLTags.CHARACTER_TAG);
    attributeTags.put(MdWebLongInfo.CLASS, XMLTags.LONG_TAG);
    attributeTags.put(MdWebBooleanInfo.CLASS, XMLTags.BOOLEAN_TAG);
    attributeTags.put(MdWebTextInfo.CLASS, XMLTags.TEXT_TAG);
    attributeTags.put(MdWebGeoInfo.CLASS, XMLTags.GEO_TAG);
    attributeTags.put(MdWebSingleTermInfo.CLASS, XMLTags.TERM_TAG);
    attributeTags.put(MdWebMultipleTermInfo.CLASS, XMLTags.MULTI_TERM_TAG);
    attributeTags.put(MdWebSingleTermGridInfo.CLASS, XMLTags.GRID_TAG);
    attributeTags.put(MdWebReferenceInfo.CLASS, XMLTags.REFERENCE_TAG);
    attributeTags.put(MdWebBreakInfo.CLASS, XMLTags.BREAK_TAG);
    attributeTags.put(MdWebCommentInfo.CLASS, XMLTags.COMMENT_TAG);
    attributeTags.put(MdWebGroupInfo.CLASS, XMLTags.GROUP_TAG);
    attributeTags.put(MdWebHeaderInfo.CLASS, XMLTags.HEADER_TAG);

    // Condition types
    attributeTags.put(AndFieldConditionInfo.CLASS, XMLTags.AND_TAG);
    attributeTags.put(CharacterConditionInfo.CLASS, XMLTags.CHARACTER_TAG);
    attributeTags.put(DateConditionInfo.CLASS, XMLTags.DATE_TAG);
    attributeTags.put(DoubleConditionInfo.CLASS, XMLTags.DOUBLE_TAG);
    attributeTags.put(LongConditionInfo.CLASS, XMLTags.LONG_TAG);

    return attributeTags;
  }

  public static interface PluginIF
  {
    public String getModuleIdentifier();

    public void addParameters(HashMap<String, String> parameters, MdAttributeDAOIF mdAttributeIF);
  }
}
