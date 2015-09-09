/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.LinkedList;

import org.xml.sax.Attributes;

import com.runwaysdk.constants.MdFieldInfo;
import com.runwaysdk.constants.MdTypeInfo;
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
import com.runwaysdk.dataaccess.metadata.MdFieldDAO;
import com.runwaysdk.dataaccess.metadata.MdWebAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdWebBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdWebCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdWebCommentDAO;
import com.runwaysdk.dataaccess.metadata.MdWebDateDAO;
import com.runwaysdk.dataaccess.metadata.MdWebDecDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFormDAO;
import com.runwaysdk.dataaccess.metadata.MdWebNumberDAO;
import com.runwaysdk.dataaccess.metadata.MdWebPrimitiveDAO;
import com.runwaysdk.dataaccess.metadata.MdWebTextDAO;

public class MdWebFieldHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  public static class ConditionHolder implements ConditionListIF
  {
    private LinkedList<ConditionAttributeIF> conditions;

    /**
     * 
     */
    public ConditionHolder()
    {
      this.conditions = new LinkedList<ConditionAttributeIF>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.ConditionListIF#addCondition(com.runwaysdk.dataaccess.io.dataDefinition.ConditionAttributeIF)
     */
    @Override
    public void addCondition(ConditionAttributeIF condition)
    {
      this.conditions.add(condition);
    }

    /**
     * @return the conditions
     */
    public LinkedList<ConditionAttributeIF> getConditions()
    {
      return conditions;
    }
  }

  private static class GroupAttributeHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public GroupAttributeHandler(ImportManager manager)
    {
      super(manager);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      MdWebFormDAO mdForm = (MdWebFormDAO) context.getObject(MdTypeInfo.CLASS);
      MdFieldDAO mdField = (MdFieldDAO) context.getObject(MdFieldInfo.CLASS);
      LinkedList<GroupAttribute> groups = (LinkedList<GroupAttribute>) context.getObject(GROUPS);

      groups.add(new GroupAttribute(attributes, mdForm, mdField));

    }
  }

  private static class FieldHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    private String type;

    public FieldHandler(ImportManager manager, String type)
    {
      super(manager);

      this.type = type;

      this.addHandler(XMLTags.CONDITION_TAG, new FieldConditionHandler(manager));
      this.addHandler(XMLTags.FIELD_GROUP_TAG, new GroupAttributeHandler(manager));
    }

    protected MdWebFormDAO getMdForm(TagContext context)
    {
      return (MdWebFormDAO) context.getObject(MdTypeInfo.CLASS);
    }

    protected void populate(MdWebFormDAO mdForm, MdFieldDAO mdField, Attributes attributes)
    {
      ImportManager.setValue(mdField, MdWebFieldInfo.FIELD_NAME, attributes, XMLTags.NAME_ATTRIBUTE);
      ImportManager.setValue(mdField, MdWebFieldInfo.FIELD_ORDER, attributes, XMLTags.PARAMETER_ORDER_ATTRIBUTE);
      ImportManager.setValue(mdField, MdWebFieldInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
      ImportManager.setValue(mdField, MdWebFieldInfo.REQUIRED, attributes, XMLTags.REQUIRED_ATTRIBUTE);
      ImportManager.setLocalizedValue(mdField, MdWebFieldInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
      ImportManager.setLocalizedValue(mdField, MdWebFieldInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    }

    protected void configure(MdWebFormDAO mdForm, MdFieldDAO mdField, Attributes attributes)
    {
      this.populate(mdForm, mdField, attributes);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
     */
    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      MdWebFormDAO mdForm = this.getMdForm(context);
      String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

      MdFieldDAO mdField = this.getManager().getMdField(mdForm, name, this.type);

      if (! ( mdField.getType().equals(type) ))
      {
        String errMsg = "The field [" + mdField.getFieldName() + "] on type [" + mdForm.definesType() + "] is not a [" + type + "] field.";

        throw new RuntimeException(errMsg);
      }

      this.configure(mdForm, mdField, attributes);

      this.apply(mdForm, mdField);

      context.setObject(MdFieldInfo.CLASS, mdField);
    }

    /**
     * Checks if the attribute MdAttribute has already been defined. If the MdAttribute has not already been applied then apply one, else throw an exception
     * 
     * @pre mdAttribute instanceof MdAttribute
     * 
     * @param mdAttribute
     *          The MdAttribute MdAttribute to apply
     */
    private void apply(MdWebFormDAO mdForm, MdFieldDAO mdAttribute)
    {
      if (mdAttribute instanceof MdWebFieldDAOIF)
      {
        mdAttribute.setValue(MdWebFieldInfo.DEFINING_MD_FORM, mdForm.getId());
        mdAttribute.apply();
      }
    }
  }

  private static class FieldAttributeHandler extends FieldHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public FieldAttributeHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void populate(MdWebFormDAO mdForm, MdWebAttributeDAO mdField, Attributes attributes)
    {
      super.populate(mdForm, mdField, attributes);

      ImportManager.setValue(mdField, MdWebAttributeInfo.SHOW_ON_SEARCH, attributes, XMLTags.SHOW_ON_SEARCH);
      ImportManager.setValue(mdField, MdWebAttributeInfo.SHOW_ON_VIEW_ALL, attributes, XMLTags.SHOW_ON_VIEW_ALL);

      // Import optional reference attributes
      String attributeName = attributes.getValue(XMLTags.MD_ATTRIBUTE);

      if (attributeName != null)
      {
        MdClassDAOIF formMdClass = mdForm.getFormMdClass();
        MdAttributeDAOIF mdAttribute = formMdClass.definesAttribute(attributeName);

        // Ensure the parent class has already been defined in the database
        if (mdAttribute == null)
        {
          // The type is not defined in the database, check if it is defined
          // in the further down in the xml document.
          String[] search_tags = { XMLTags.MD_BUSINESS_TAG, XMLTags.MD_TERM_TAG };
          SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, formMdClass.definesType(), mdField.getKey());
        }

        mdField.setValue(MdWebPrimitiveInfo.DEFINING_MD_ATTRIBUTE, mdAttribute.getId());
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdWebFieldHandler.FieldHandler#configure(com.runwaysdk.dataaccess.metadata.MdWebFormDAO, com.runwaysdk.dataaccess.metadata.MdFieldDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdWebFormDAO mdForm, MdFieldDAO mdField, Attributes attributes)
    {
      this.populate(mdForm, (MdWebAttributeDAO) mdField, attributes);
    }
  }

  private static class FieldCommentHandler extends FieldHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public FieldCommentHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void populate(MdWebFormDAO mdForm, MdWebCommentDAO mdField, Attributes attributes)
    {
      super.populate(mdForm, mdField, attributes);

      ImportManager.setValue(mdField, MdWebCommentInfo.COMMENT_TEXT, attributes, XMLTags.COMMENT_TEXT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdWebFieldHandler.FieldHandler#configure(com.runwaysdk.dataaccess.metadata.MdWebFormDAO, com.runwaysdk.dataaccess.metadata.MdFieldDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdWebFormDAO mdForm, MdFieldDAO mdField, Attributes attributes)
    {
      this.populate(mdForm, (MdWebCommentDAO) mdField, attributes);
    }
  }

  private static class FieldPrimitiveHandler extends FieldAttributeHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public FieldPrimitiveHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void populate(MdWebFormDAO mdForm, MdWebPrimitiveDAO mdField, Attributes attributes)
    {
      super.populate(mdForm, mdField, attributes);

      ImportManager.setValue(mdField, MdWebPrimitiveInfo.IS_EXPRESSION, attributes, XMLTags.IS_EXPRESSION);
      ImportManager.setValue(mdField, MdWebPrimitiveInfo.EXPRESSION, attributes, XMLTags.EXPRESSION);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdWebFieldHandler.FieldHandler#configure(com.runwaysdk.dataaccess.metadata.MdWebFormDAO, com.runwaysdk.dataaccess.metadata.MdFieldDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdWebFormDAO mdForm, MdFieldDAO mdField, Attributes attributes)
    {
      this.populate(mdForm, (MdWebPrimitiveDAO) mdField, attributes);
    }
  }

  private static class FieldNumberHandler extends FieldPrimitiveHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public FieldNumberHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void populate(MdWebFormDAO mdForm, MdWebNumberDAO mdField, Attributes attributes)
    {
      super.populate(mdForm, mdField, attributes);

      ImportManager.setValue(mdField, MdWebNumberInfo.STARTRANGE, attributes, XMLTags.STARTRANGE);
      ImportManager.setValue(mdField, MdWebNumberInfo.ENDRANGE, attributes, XMLTags.ENDRANGE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdWebFieldHandler.FieldHandler#configure(com.runwaysdk.dataaccess.metadata.MdWebFormDAO, com.runwaysdk.dataaccess.metadata.MdFieldDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdWebFormDAO mdForm, MdFieldDAO mdField, Attributes attributes)
    {
      this.populate(mdForm, (MdWebNumberDAO) mdField, attributes);
    }
  }

  private static class FieldCharacterHandler extends FieldPrimitiveHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public FieldCharacterHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void populate(MdWebFormDAO mdForm, MdWebCharacterDAO mdField, Attributes attributes)
    {
      super.populate(mdForm, mdField, attributes);

      ImportManager.setValue(mdField, MdWebCharacterInfo.DISPLAY_LENGTH, attributes, XMLTags.DISPLAY_LENGTH);
      ImportManager.setValue(mdField, MdWebCharacterInfo.MAX_LENGTH, attributes, XMLTags.MAX_LENGTH);
      ImportManager.setValue(mdField, MdWebCharacterInfo.UNIQUE, attributes, XMLTags.UNIQUE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdWebFieldHandler.FieldHandler#configure(com.runwaysdk.dataaccess.metadata.MdWebFormDAO, com.runwaysdk.dataaccess.metadata.MdFieldDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdWebFormDAO mdForm, MdFieldDAO mdField, Attributes attributes)
    {
      this.populate(mdForm, (MdWebCharacterDAO) mdField, attributes);
    }
  }

  private static class FieldDateHandler extends FieldPrimitiveHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public FieldDateHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void populate(MdWebFormDAO mdForm, MdWebDateDAO mdField, Attributes attributes)
    {
      super.populate(mdForm, mdField, attributes);

      ImportManager.setValue(mdField, MdWebDateInfo.AFTER_TODAY_EXCLUSIVE, attributes, XMLTags.AFTER_TODAY_EXCLUSIVE);
      ImportManager.setValue(mdField, MdWebDateInfo.AFTER_TODAY_INCLUSIVE, attributes, XMLTags.AFTER_TODAY_INCLUSIVE);
      ImportManager.setValue(mdField, MdWebDateInfo.BEFORE_TODAY_EXCLUSIVE, attributes, XMLTags.BEFORE_TODAY_EXCLUSIVE);
      ImportManager.setValue(mdField, MdWebDateInfo.BEFORE_TODAY_INCLUSIVE, attributes, XMLTags.BEFORE_TODAY_INCLUSIVE);
      ImportManager.setValue(mdField, MdWebDateInfo.START_DATE, attributes, XMLTags.START_DATE);
      ImportManager.setValue(mdField, MdWebDateInfo.END_DATE, attributes, XMLTags.END_DATE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdWebFieldHandler.FieldHandler#configure(com.runwaysdk.dataaccess.metadata.MdWebFormDAO, com.runwaysdk.dataaccess.metadata.MdFieldDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdWebFormDAO mdForm, MdFieldDAO mdField, Attributes attributes)
    {
      this.populate(mdForm, (MdWebDateDAO) mdField, attributes);
    }
  }

  private static class FieldBooleanHandler extends FieldPrimitiveHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public FieldBooleanHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void populate(MdWebFormDAO mdForm, MdWebBooleanDAO mdField, Attributes attributes)
    {
      super.populate(mdForm, mdField, attributes);

      ImportManager.setValue(mdField, MdWebBooleanInfo.DEFAULT_VALUE, attributes, XMLTags.DEFAULT_VALUE_ATTRIBUTE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdWebFieldHandler.FieldHandler#configure(com.runwaysdk.dataaccess.metadata.MdWebFormDAO, com.runwaysdk.dataaccess.metadata.MdFieldDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdWebFormDAO mdForm, MdFieldDAO mdField, Attributes attributes)
    {
      this.populate(mdForm, (MdWebBooleanDAO) mdField, attributes);
    }
  }

  private static class FieldTextHandler extends FieldPrimitiveHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public FieldTextHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void populate(MdWebFormDAO mdForm, MdWebTextDAO mdField, Attributes attributes)
    {
      super.populate(mdForm, mdField, attributes);

      ImportManager.setValue(mdField, MdWebTextInfo.HEIGHT, attributes, XMLTags.HEIGHT);
      ImportManager.setValue(mdField, MdWebTextInfo.WIDTH, attributes, XMLTags.WIDTH);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdWebFieldHandler.FieldHandler#configure(com.runwaysdk.dataaccess.metadata.MdWebFormDAO, com.runwaysdk.dataaccess.metadata.MdFieldDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdWebFormDAO mdForm, MdFieldDAO mdField, Attributes attributes)
    {
      this.populate(mdForm, (MdWebTextDAO) mdField, attributes);
    }
  }

  private static class FieldDecHandler extends FieldNumberHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public FieldDecHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void populate(MdWebFormDAO mdForm, MdWebDecDAO mdField, Attributes attributes)
    {
      super.populate(mdForm, mdField, attributes);

      ImportManager.setValue(mdField, MdWebDecInfo.DECPRECISION, attributes, XMLTags.DEC_PRECISION);
      ImportManager.setValue(mdField, MdWebDecInfo.DECSCALE, attributes, XMLTags.DEC_SCALE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdWebFieldHandler.FieldHandler#configure(com.runwaysdk.dataaccess.metadata.MdWebFormDAO, com.runwaysdk.dataaccess.metadata.MdFieldDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdWebFormDAO mdForm, MdFieldDAO mdField, Attributes attributes)
    {
      this.populate(mdForm, (MdWebDecDAO) mdField, attributes);
    }
  }

  private static class FieldGridHandler extends FieldAttributeHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public FieldGridHandler(ImportManager manager, String type)
    {
      super(manager, type);

      this.addHandler(XMLTags.GRID_FIELDS_TAG, new GridFieldHandler(manager));
    }
  }

  public static final String GROUPS = "GROUPS";

  public MdWebFieldHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.GEO_TAG, new FieldAttributeHandler(manager, MdWebGeoInfo.CLASS));
    this.addHandler(XMLTags.TERM_TAG, new FieldAttributeHandler(manager, MdWebSingleTermInfo.CLASS));
    this.addHandler(XMLTags.MULTI_TERM_TAG, new FieldAttributeHandler(manager, MdWebMultipleTermInfo.CLASS));
    this.addHandler(XMLTags.GRID_TAG, new FieldGridHandler(manager, MdWebSingleTermGridInfo.CLASS));
    this.addHandler(XMLTags.CHARACTER_TAG, new FieldCharacterHandler(manager, MdWebCharacterInfo.CLASS));
    this.addHandler(XMLTags.DATE_TAG, new FieldDateHandler(manager, MdWebDateInfo.CLASS));
    this.addHandler(XMLTags.DATETIME_TAG, new FieldPrimitiveHandler(manager, MdWebDateTimeInfo.CLASS));
    this.addHandler(XMLTags.TIME_TAG, new FieldPrimitiveHandler(manager, MdWebTimeInfo.CLASS));
    this.addHandler(XMLTags.DECIMAL_TAG, new FieldDecHandler(manager, MdWebDecimalInfo.CLASS));
    this.addHandler(XMLTags.DOUBLE_TAG, new FieldDecHandler(manager, MdWebDoubleInfo.CLASS));
    this.addHandler(XMLTags.FLOAT_TAG, new FieldDecHandler(manager, MdWebFloatInfo.CLASS));
    this.addHandler(XMLTags.INTEGER_TAG, new FieldNumberHandler(manager, MdWebIntegerInfo.CLASS));
    this.addHandler(XMLTags.LONG_TAG, new FieldNumberHandler(manager, MdWebLongInfo.CLASS));
    this.addHandler(XMLTags.BOOLEAN_TAG, new FieldBooleanHandler(manager, MdWebBooleanInfo.CLASS));
    this.addHandler(XMLTags.TEXT_TAG, new FieldTextHandler(manager, MdWebTextInfo.CLASS));
    this.addHandler(XMLTags.REFERENCE_TAG, new FieldAttributeHandler(manager, MdWebReferenceInfo.CLASS));
    this.addHandler(XMLTags.BREAK_TAG, new FieldHandler(manager, MdWebBreakInfo.CLASS));
    this.addHandler(XMLTags.COMMENT_TAG, new FieldCommentHandler(manager, MdWebCommentInfo.CLASS));
    this.addHandler(XMLTags.GROUP_TAG, new FieldHandler(manager, MdWebGroupInfo.CLASS));
    this.addHandler(XMLTags.HEADER_TAG, new FieldHandler(manager, MdWebHeaderInfo.CLASS));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    context.setObject(ConditionListIF.CONDITIONS, new ConditionHolder());
    context.setObject(GROUPS, new LinkedList<GroupAttribute>());
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onEndElement(java.lang.String, java.lang.String, java.lang.String, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @SuppressWarnings("unchecked")
  @Override
  public void onEndElement(String uri, String localName, String name, TagContext context)
  {
    ConditionHolder holder = (ConditionHolder) context.getObject(ConditionListIF.CONDITIONS);
    LinkedList<GroupAttribute> groups = (LinkedList<GroupAttribute>) context.getObject(GROUPS);

    LinkedList<ConditionAttributeIF> conditions = holder.getConditions();

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
  }
}
