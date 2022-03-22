/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
/**
\ * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.gis.dataaccess.graph.attributes;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.graph.attributes.Attribute;
import com.runwaysdk.dataaccess.graph.attributes.GraphAttributeFactoryIF;
import com.runwaysdk.gis.dataaccess.MdAttributeLineStringDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiLineStringDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPointDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPolygonDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributePointDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributePolygonDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeShapeDAOIF;

public class GISGraphAttributeFactory implements GraphAttributeFactoryIF
{

  @Override
  public Attribute createGraphAttribute(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingType)
  {
    if (mdAttributeDAOIF instanceof MdAttributePointDAOIF)
    {
      return new AttributePoint(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributePolygonDAOIF)
    {
      return new AttributePolygon(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeShapeDAOIF)
    {
      return new AttributeShape(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeLineStringDAOIF)
    {
      return new AttributeLineString(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeMultiPointDAOIF)
    {
      return new AttributeMultiPoint(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeMultiPolygonDAOIF)
    {
      return new AttributeMultiPolygon(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeMultiLineStringDAOIF)
    {
      return new AttributeMultiLineString(mdAttributeDAOIF, definingType);
    }

    return null;
  }

}
