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

import com.runwaysdk.gis.constants.GISConstants;
import com.runwaysdk.gis.dataaccess.MdAttributeLineStringDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiLineStringDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPointDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPolygonDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributePointDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributePolygonDAOIF;
import com.runwaysdk.gis.dataaccess.attributes.value.MdAttributeLineString_Q;
import com.runwaysdk.gis.dataaccess.attributes.value.MdAttributeMultiLineString_Q;
import com.runwaysdk.gis.dataaccess.attributes.value.MdAttributeMultiPoint_Q;
import com.runwaysdk.gis.dataaccess.attributes.value.MdAttributeMultiPolygon_Q;
import com.runwaysdk.gis.dataaccess.attributes.value.MdAttributePoint_Q;
import com.runwaysdk.gis.dataaccess.attributes.value.MdAttributePolygon_Q;
import com.runwaysdk.query.Attribute.PluginIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeConcrete_Q;

public class GISAttributeMdAttributeConcrete_Q_Factory implements PluginIF
{
  public String getModuleIdentifier()
  {
    return GISConstants.GIS_SYSTEM_PACKAGE;
  }

  public MdAttributeConcrete_Q convertMdAttribute(MdAttributeConcreteDAOIF _mdAttributeIF)
  {
    MdAttributeConcrete_Q mdAttribute_Q = null;

    if (_mdAttributeIF instanceof MdAttributePointDAOIF)
    {
      mdAttribute_Q = new MdAttributePoint_Q((MdAttributePointDAOIF)_mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeLineStringDAOIF)
    {
      mdAttribute_Q = new MdAttributeLineString_Q((MdAttributeLineStringDAOIF)_mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributePolygonDAOIF)
    {
      mdAttribute_Q = new MdAttributePolygon_Q((MdAttributePolygonDAOIF)_mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeMultiPointDAOIF)
    {
      mdAttribute_Q = new MdAttributeMultiPoint_Q((MdAttributeMultiPointDAOIF)_mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeMultiLineStringDAOIF)
    {
      mdAttribute_Q = new MdAttributeMultiLineString_Q((MdAttributeMultiLineStringDAOIF)_mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeMultiPolygonDAOIF)
    {
      mdAttribute_Q = new MdAttributeMultiPolygon_Q((MdAttributeMultiPolygonDAOIF)_mdAttributeIF);
    }

    return mdAttribute_Q;
  }
}
