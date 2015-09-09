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
/**
 * 
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.List;

import org.xml.sax.Attributes;

import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.constants.MdDimensionInfo;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;

public class DimensionOperationHandler extends OperationHandler implements TagHandlerIF, HandlerFactoryIF
{
  /**
   * 
   */
  public DimensionOperationHandler(ImportManager manager)
  {
    super(manager);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    String operationName = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String dimension = attributes.getValue(XMLTags.DIMENSION_ATTRIBUTE);

    if (operationName.equals(XMLTags.READ_ALL_ATTRIBUTES))
    {
      this.setDimensionPermission(Operation.READ, dimension, context);
    }
    else if (operationName.equals(XMLTags.WRITE_ALL_ATTRIBUTES))
    {
      this.setDimensionPermission(Operation.WRITE, dimension, context);
    }
  }

  private void setDimensionPermission(Operation operation, String dimension, TagContext context)
  {
    MdClassDAOIF mdClass = (MdClassDAOIF) this.getMdType(context);

    for (MdAttributeDAOIF mdAttribute : mdClass.definesAttributes())
    {
      if (dimension.equalsIgnoreCase("*"))
      {
        List<MdAttributeDimensionDAOIF> list = mdAttribute.getMdAttributeDimensions();

        for (MdAttributeDimensionDAOIF mdAttributeDimension : list)
        {
          this.setPermission(operation, mdAttributeDimension.getId(), context);
        }
      }
      else
      {
        String key = MdDimensionDAO.buildKey(dimension);
        MdDimensionDAOIF mdDimension = MdDimensionDAO.get(MdDimensionInfo.CLASS, key);
        MdAttributeDimensionDAOIF mdAttributeDimension = mdAttribute.getMdAttributeDimension(mdDimension);

        this.setPermission(operation, mdAttributeDimension.getId(), context);
      }
    }
  }
}
