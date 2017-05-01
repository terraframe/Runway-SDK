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

import java.util.HashSet;
import java.util.Set;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdTableClassIF;
import com.runwaysdk.gis.constants.GISConstants;
import com.runwaysdk.gis.dataaccess.MdAttributeLineStringDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiLineStringDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPointDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPolygonDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributePointDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributePolygonDAOIF;
import com.runwaysdk.query.ValueQuery.PluginIF;

public class GISValueQueryAttributeFactory implements PluginIF
{
  public String getModuleIdentifier()
  {
    return GISConstants.GIS_SYSTEM_PACKAGE;
  }

  public Attribute createAttribute(
      ComponentQuery rootComponentQuery, Selectable selectable, MdTableClassIF definingTableClassIF, String definingTableName,
      String definingTableAlias, MdAttributeConcreteDAOIF mdAttributeIF, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Set<Join> attrTableJoinSet = new HashSet<Join>();

    Attribute attribute = null;

    if (mdAttributeIF instanceof MdAttributePointDAOIF)
    {
      attribute =
        new AttributePoint((MdAttributePointDAOIF)mdAttributeIF, definingTableClassIF.definesType(), definingTableName, definingTableAlias,
            rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeLineStringDAOIF)
    {
      attribute =
        new AttributeLineString((MdAttributeLineStringDAOIF)mdAttributeIF, definingTableClassIF.definesType(), definingTableName, definingTableAlias,
            rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributePolygonDAOIF)
    {
      attribute =
        new AttributePolygon((MdAttributePolygonDAOIF)mdAttributeIF, definingTableClassIF.definesType(), definingTableName, definingTableAlias,
            rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeMultiPointDAOIF)
    {
      attribute =
        new AttributeMultiPoint((MdAttributeMultiPointDAOIF)mdAttributeIF, definingTableClassIF.definesType(), definingTableName, definingTableAlias,
            rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeMultiLineStringDAOIF)
    {
      attribute =
        new AttributeMultiLineString((MdAttributeMultiLineStringDAOIF)mdAttributeIF, definingTableClassIF.definesType(), definingTableName, definingTableAlias,
            rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeMultiPolygonDAOIF)
    {
      attribute =
        new AttributeMultiPolygon((MdAttributeMultiPolygonDAOIF)mdAttributeIF, definingTableClassIF.definesType(), definingTableName, definingTableAlias,
            rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }

    return attribute;
  }
}
