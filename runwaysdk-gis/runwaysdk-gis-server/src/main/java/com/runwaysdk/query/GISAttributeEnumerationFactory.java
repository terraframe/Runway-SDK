/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.query;

import java.util.Set;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.gis.constants.GISConstants;
import com.runwaysdk.gis.dataaccess.MdAttributeLineStringDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiLineStringDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPointDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPolygonDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributePointDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributePolygonDAOIF;
import com.runwaysdk.query.AttributeEnumeration.PluginIF;

public class GISAttributeEnumerationFactory implements PluginIF
{
  public String getModuleIdentifier()
  {
    return GISConstants.GIS_SYSTEM_PACKAGE;
  }

  public Attribute internalAttributeFactory(
      String attributeName, MdAttributeConcreteDAOIF mdAttributeIF,
      String refAttributeNameSpace, String parameterTableName, String parameterTableAlias,
      ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Attribute attribute = null;

    if (mdAttributeIF instanceof MdAttributePointDAOIF)
    {
      attribute = new AttributePoint((MdAttributePointDAOIF)mdAttributeIF, refAttributeNameSpace, parameterTableName,
          parameterTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeLineStringDAOIF)
    {
      attribute = new AttributeLineString((MdAttributeLineStringDAOIF)mdAttributeIF, refAttributeNameSpace, parameterTableName,
          parameterTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributePolygonDAOIF)
    {
      attribute = new AttributePolygon((MdAttributePolygonDAOIF)mdAttributeIF, refAttributeNameSpace, parameterTableName,
          parameterTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeMultiPointDAOIF)
    {
      attribute = new AttributeMultiPoint((MdAttributeMultiPointDAOIF)mdAttributeIF, refAttributeNameSpace, parameterTableName,
          parameterTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeMultiLineStringDAOIF)
    {
      attribute = new AttributeMultiLineString((MdAttributeMultiLineStringDAOIF)mdAttributeIF, refAttributeNameSpace, parameterTableName,
          parameterTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeMultiPolygonDAOIF)
    {
      attribute = new AttributeMultiPolygon((MdAttributeMultiPolygonDAOIF)mdAttributeIF, refAttributeNameSpace, parameterTableName,
          parameterTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }

    return attribute;
  }
}
