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
/**
 * 
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.constants.MdFieldInfo;
import com.runwaysdk.constants.MdWebBooleanInfo;
import com.runwaysdk.constants.MdWebDecInfo;
import com.runwaysdk.constants.MdWebDecimalInfo;
import com.runwaysdk.constants.MdWebDoubleInfo;
import com.runwaysdk.constants.MdWebFieldInfo;
import com.runwaysdk.constants.MdWebFloatInfo;
import com.runwaysdk.constants.MdWebIntegerInfo;
import com.runwaysdk.constants.MdWebLongInfo;
import com.runwaysdk.constants.MdWebNumberInfo;
import com.runwaysdk.constants.MdWebPrimitiveInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdWebFieldDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdFieldDAO;
import com.runwaysdk.dataaccess.metadata.MdWebAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdWebBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdWebDecDAO;
import com.runwaysdk.dataaccess.metadata.MdWebNumberDAO;
import com.runwaysdk.dataaccess.metadata.MdWebSingleTermGridDAO;

public class GridFieldHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  private static abstract class FieldHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    private String type;

    public FieldHandler(ImportManager manager, String type)
    {
      super(manager);

      this.type = type;
    }

    protected abstract void configure(MdWebSingleTermGridDAO mdWebSingleGrid, MdFieldDAO mdField, Attributes attributes);

    protected MdWebSingleTermGridDAO getMdGrid(TagContext context)
    {
      return (MdWebSingleTermGridDAO) context.getObject(MdFieldInfo.CLASS);
    }

    protected void populate(MdWebSingleTermGridDAO mdGrid, MdFieldDAO mdField, Attributes attributes)
    {
      ImportManager.setValue(mdField, MdWebFieldInfo.FIELD_NAME, attributes, XMLTags.NAME_ATTRIBUTE);
      ImportManager.setValue(mdField, MdWebFieldInfo.FIELD_ORDER, attributes, XMLTags.PARAMETER_ORDER_ATTRIBUTE);
      ImportManager.setValue(mdField, MdWebFieldInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
      ImportManager.setValue(mdField, MdWebFieldInfo.REQUIRED, attributes, XMLTags.REQUIRED_ATTRIBUTE);
      ImportManager.setLocalizedValue(mdField, MdWebFieldInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
      ImportManager.setLocalizedValue(mdField, MdWebFieldInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onStartElement(
     * java.lang.String, org.xml.sax.Attributes,
     * com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
     */
    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      MdWebSingleTermGridDAO mdWebSingleGrid = this.getMdGrid(context);

      String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
      MdFieldDAO mdField = this.getManager().getMdField(mdWebSingleGrid, name, type);

      if (! ( mdField.getType().equals(type) ))
      {
        String errMsg = "The field [" + mdField.getFieldName() + "] on grid field [" + mdWebSingleGrid.getKey() + "] is not a [" + type + "] field.";

        throw new RuntimeException(errMsg);
      }

      this.configure(mdWebSingleGrid, mdField, attributes);

      this.apply(mdWebSingleGrid, mdField);

      context.setObject(MdFieldInfo.CLASS, mdField);
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
    private void apply(MdWebSingleTermGridDAO mdWebSingleGrid, MdFieldDAO mdAttribute)
    {
      if (mdAttribute instanceof MdWebFieldDAOIF)
      {
        String key = MdFieldDAO.buildKey(mdWebSingleGrid.getKey(), mdAttribute.getFieldName());

        mdAttribute.setValue(MdWebFieldInfo.KEY, key);
        mdAttribute.apply();

        if (this.getManager().isCreateState())
        {
          RelationshipDAO.newInstance(mdWebSingleGrid.getOid(), mdAttribute.getOid(), RelationshipTypes.WEB_GRID_FIELD.getType()).apply();
        }
        else if (this.getManager().isCreateOrUpdateState())
        {
          try
          {
            RelationshipDAO.get(mdWebSingleGrid.getOid(), mdAttribute.getOid(), RelationshipTypes.WEB_GRID_FIELD.getType());
          }
          catch (DataNotFoundException e)
          {
            RelationshipDAO.newInstance(mdWebSingleGrid.getOid(), mdAttribute.getOid(), RelationshipTypes.WEB_GRID_FIELD.getType()).apply();
          }

        }
      }

    }
  }

  private static class FieldAttributeHandler extends FieldHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public FieldAttributeHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void populate(MdWebSingleTermGridDAO mdGrid, MdWebAttributeDAO mdField, Attributes attributes)
    {
      super.populate(mdGrid, mdField, attributes);

      // Import optional reference attributes
      String attributeName = attributes.getValue(XMLTags.MD_ATTRIBUTE);
      String classType = attributes.getValue(XMLTags.TYPE_ATTRIBUTE);

      if (attributeName != null)
      {
        MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(classType);
        MdAttributeDAOIF mdAttribute = mdClass.definesAttribute(attributeName);

        // Ensure the parent class has already been defined in the database
        if (mdAttribute == null)
        {
          // The type is not defined in the database, check if it is defined
          // in the further down in the xml document.
          String[] search_tags = { XMLTags.MD_BUSINESS_TAG, XMLTags.MD_TERM_TAG };
          SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, mdClass.definesType(), mdField.getKey());
        }
        
        mdAttribute = mdClass.definesAttribute(attributeName);        
        mdField.setValue(MdWebPrimitiveInfo.DEFINING_MD_ATTRIBUTE, mdAttribute.getOid());
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.runwaysdk.dataaccess.io.dataDefinition.MdWebFieldHandler.FieldHandler
     * #configure(com.runwaysdk.dataaccess.metadata.MdWebFormDAO,
     * com.runwaysdk.dataaccess.metadata.MdFieldDAO, org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdWebSingleTermGridDAO mdGrid, MdFieldDAO mdField, Attributes attributes)
    {
      this.populate(mdGrid, (MdWebAttributeDAO) mdField, attributes);
    }
  }

  private static class FieldPrimitiveHandler extends FieldAttributeHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public FieldPrimitiveHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void populate(MdWebSingleTermGridDAO mdGrid, MdWebBooleanDAO mdField, Attributes attributes)
    {
      super.populate(mdGrid, mdField, attributes);

      ImportManager.setValue(mdField, MdWebPrimitiveInfo.IS_EXPRESSION, attributes, XMLTags.IS_EXPRESSION);
      ImportManager.setValue(mdField, MdWebPrimitiveInfo.EXPRESSION, attributes, XMLTags.EXPRESSION);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.GridFieldHandler.
     * FieldAttributeHandler#configure(com.runwaysdk.dataaccess.metadata.
     * MdWebSingleTermGridDAO, com.runwaysdk.dataaccess.metadata.MdFieldDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdWebSingleTermGridDAO mdGrid, MdFieldDAO mdField, Attributes attributes)
    {
      this.populate(mdGrid, (MdWebBooleanDAO) mdField, attributes);
    }
  }

  private static class FieldNumberHandler extends FieldPrimitiveHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public FieldNumberHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void populate(MdWebSingleTermGridDAO mdGrid, MdWebNumberDAO mdField, Attributes attributes)
    {
      super.populate(mdGrid, mdField, attributes);

      ImportManager.setValue(mdField, MdWebNumberInfo.STARTRANGE, attributes, XMLTags.STARTRANGE);
      ImportManager.setValue(mdField, MdWebNumberInfo.ENDRANGE, attributes, XMLTags.ENDRANGE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.GridFieldHandler.
     * FieldAttributeHandler#configure(com.runwaysdk.dataaccess.metadata.
     * MdWebSingleTermGridDAO, com.runwaysdk.dataaccess.metadata.MdFieldDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdWebSingleTermGridDAO mdGrid, MdFieldDAO mdField, Attributes attributes)
    {
      this.populate(mdGrid, (MdWebNumberDAO) mdField, attributes);
    }

  }

  private static class FieldDecHandler extends FieldNumberHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public FieldDecHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void populate(MdWebSingleTermGridDAO mdGrid, MdWebDecDAO mdField, Attributes attributes)
    {
      super.populate(mdGrid, mdField, attributes);

      ImportManager.setValue(mdField, MdWebDecInfo.DECPRECISION, attributes, XMLTags.DEC_PRECISION);
      ImportManager.setValue(mdField, MdWebDecInfo.DECSCALE, attributes, XMLTags.DEC_SCALE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.GridFieldHandler.
     * FieldAttributeHandler#configure(com.runwaysdk.dataaccess.metadata.
     * MdWebSingleTermGridDAO, com.runwaysdk.dataaccess.metadata.MdFieldDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdWebSingleTermGridDAO mdGrid, MdFieldDAO mdField, Attributes attributes)
    {
      this.populate(mdGrid, (MdWebDecDAO) mdField, attributes);
    }
  }

  private static class FieldBooleanHandler extends FieldPrimitiveHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public FieldBooleanHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void populate(MdWebSingleTermGridDAO mdGrid, MdWebBooleanDAO mdField, Attributes attributes)
    {
      super.populate(mdGrid, mdField, attributes);

      ImportManager.setValue(mdField, MdWebBooleanInfo.DEFAULT_VALUE, attributes, XMLTags.DEFAULT_VALUE_ATTRIBUTE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.GridFieldHandler.
     * FieldAttributeHandler#configure(com.runwaysdk.dataaccess.metadata.
     * MdWebSingleTermGridDAO, com.runwaysdk.dataaccess.metadata.MdFieldDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdWebSingleTermGridDAO mdGrid, MdFieldDAO mdField, Attributes attributes)
    {
      this.populate(mdGrid, (MdWebBooleanDAO) mdField, attributes);
    }
  }

  public GridFieldHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.DECIMAL_TAG, new FieldDecHandler(manager, MdWebDecimalInfo.CLASS));
    this.addHandler(XMLTags.DOUBLE_TAG, new FieldDecHandler(manager, MdWebDoubleInfo.CLASS));
    this.addHandler(XMLTags.FLOAT_TAG, new FieldDecHandler(manager, MdWebFloatInfo.CLASS));
    this.addHandler(XMLTags.INTEGER_TAG, new FieldNumberHandler(manager, MdWebIntegerInfo.CLASS));
    this.addHandler(XMLTags.LONG_TAG, new FieldNumberHandler(manager, MdWebLongInfo.CLASS));
    this.addHandler(XMLTags.BOOLEAN_TAG, new FieldBooleanHandler(manager, MdWebBooleanInfo.CLASS));
  }
}
