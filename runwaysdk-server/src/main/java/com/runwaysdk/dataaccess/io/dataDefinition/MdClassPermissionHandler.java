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

import java.util.Map;

import org.xml.sax.Attributes;

import com.runwaysdk.constants.MdDimensionInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.dataaccess.AttributeDoesNotExistException;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;

public class MdClassPermissionHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  protected class AttributePermissionHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public AttributePermissionHandler(ImportManager manager)
    {
      super(manager);

      this.addHandler(XMLTags.OPERATION_TAG, new OperationHandler(manager));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.AbstractPermissionHandler.OperationHandler#onStartElement(java.lang.String, org.xml.sax.Attributes,
     * com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
     */
    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      String attributeName = attributes.getValue(XMLTags.PERMISSION_ATTRIBUTE_NAME);
      String dimension = attributes.getValue(XMLTags.DIMENSION_ATTRIBUTE);
      MdClassDAOIF mdClass = (MdClassDAOIF) context.getObject(MdTypeInfo.CLASS);

      MdAttributeDAOIF mdAttribute = this.getMdAttribute(mdClass, attributeName);

      if (dimension != null && dimension.length() > 0)
      {
        String key = MdDimensionDAO.buildKey(dimension);
        MdDimensionDAOIF mdDimension = MdDimensionDAO.get(MdDimensionInfo.CLASS, key);
        MdAttributeDimensionDAOIF mdAttributeDimension = mdAttribute.getMdAttributeDimension(mdDimension);

        context.setObject(MetadataInfo.CLASS, mdAttributeDimension);
      }
      else
      {
        context.setObject(MetadataInfo.CLASS, mdAttribute);
      }
    }

    /**
     * Returns the MdAttribute that defines the attribute with the given names.
     * 
     * @param attributeName
     * @return MdAttribute that defines the attribute with the given names.
     */
    private MdAttributeDAOIF getMdAttribute(MdClassDAOIF mdClass, String attributeName)
    {
      Map<String, ? extends MdAttributeDAOIF> mdAttributeMap = mdClass.getAllDefinedMdAttributeMap();

      MdAttributeDAOIF mdAttributeIF = mdAttributeMap.get(attributeName.toLowerCase());
      if (mdAttributeIF == null)
      {
        String errMsg = "Attribute [" + attributeName + "] is not defined by class [" + mdClass.definesType() + "]";
        throw new AttributeDoesNotExistException(errMsg, attributeName, mdClass);
      }

      return mdAttributeIF;
    }
  }

  public MdClassPermissionHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.OPERATION_TAG, new OperationHandler(manager));
    this.addHandler(XMLTags.OPERATION_DIMENSION_TAG, new DimensionOperationHandler(manager));
    this.addHandler(XMLTags.ATTRIBUTE_PERMISSION_TAG, new AttributePermissionHandler(manager));
    this.addHandler(XMLTags.MD_METHOD_PERMISSION_TAG, new MdMethodPermissionHandler(manager));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    String type = attributes.getValue(XMLTags.TYPE_ATTRIBUTE);

    MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(type);

    context.setObject(MdTypeInfo.CLASS, mdClass);
    context.setObject(MetadataInfo.CLASS, mdClass);
  }
}
