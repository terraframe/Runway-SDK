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
package com.runwaysdk.gis.dataaccess.graph;

import java.util.Hashtable;
import java.util.Map;

import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.attributes.Attribute;
import com.runwaysdk.gis.dataaccess.MdGeoVertexDAOIF;
import com.runwaysdk.gis.dataaccess.metadata.graph.MdGeoVertexDAO;

public class GeoVertexDAO extends VertexObjectDAO
{
  /**
   * 
   */
  private static final long serialVersionUID = -6481983910027423078L;

  /**
   * The default constructor, does not set any attributes
   */
  public GeoVertexDAO()
  {
    super();
  }
  
  /**
   * Constructs a {@link GeoVertexDAO} from the given hashtable of
   * Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> classType != null
   * 
   * @param attributeMap
   * @param classType
   */
  public GeoVertexDAO(Map<String, Attribute> attributeMap, MdGeoVertexDAOIF mdGeoVertexDAOIF)
  {
    super(attributeMap, mdGeoVertexDAOIF);
  }
  
  /**
   * Returns a {@link MdGeoVertexDAOIF} that defines this Component's class.
   *
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   *
   * @return a {@link MdGeoVertexDAOIF} that defines this Component's class.
   */
  @Override
  public MdGeoVertexDAOIF getMdClassDAO()
  {
    return (MdGeoVertexDAOIF) super.getMdClassDAO();
  }
  
  public static GeoVertexDAO newInstance(String vertexType)
  {
    // get the meta data for the given class
    MdGeoVertexDAOIF mdGeoVertexDAOIF = MdGeoVertexDAO.getMdGeoVertexDAO(vertexType);

    return newInstance(mdGeoVertexDAOIF);
  }
  
  /**
   * Returns a new {@link GeoVertexDAO} instance of the given class name.
   * Default values are assigned attributes if specified by the metadata.
   * 
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("")
   * 
   * @param mdGeoVertexDAOIF
   *          Class name of the new BusinessDAO to instantiate
   * @return new {@link GeoVertexDAO} of the given type
   * @throws DataAccessException
   *           if the given class name is abstract
   * @throws DataAccessException
   *           if metadata is not defined for the given class name
   */
  public static GeoVertexDAO newInstance(MdGeoVertexDAOIF mdGeoVertexDAOIF)
  {
    VertexObjectDAO.checkIsAbstract(mdGeoVertexDAOIF);
    
    Hashtable<String, Attribute> attributeMap = getAttributeMap(mdGeoVertexDAOIF);
    
    GeoVertexDAO geoVertex = new GeoVertexDAO(attributeMap, mdGeoVertexDAOIF);
    
    VertexObjectDAO.initialize(attributeMap, geoVertex);

    return geoVertex;
  }
}
