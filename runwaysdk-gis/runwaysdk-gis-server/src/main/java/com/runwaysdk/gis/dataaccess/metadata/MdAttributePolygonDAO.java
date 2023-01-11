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
package com.runwaysdk.gis.dataaccess.metadata;

import java.util.Map;

import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAOVisitor;
import com.runwaysdk.gis.constants.MdAttributePolygonInfo;
import com.runwaysdk.gis.dataaccess.MdAttributePolygonDAOIF;
import com.runwaysdk.gis.transport.metadata.AttributePolygonMdDTO;
import org.locationtech.jts.geom.Polygon;

public class MdAttributePolygonDAO extends MdAttributeGeometryDAO implements MdAttributePolygonDAOIF
{

  /**
   *
   */
  private static final long serialVersionUID = -8402455863045559995L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributePolygonDAO()
  {
    super();
  }

  /**
   * Constructs a MdAttributePolygonDAO from the given hashtable of Attributes.
   *
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null
   *
   * @param attributeMap
   * @param classType
   */
  public MdAttributePolygonDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdAttributePolygonDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributePolygonDAO(attributeMap, classType);
  }

  @Override
  public String attributeMdDTOType()
  {
    return AttributePolygonMdDTO.class.getName();
  }

  public void setRandomValue(EntityDAO object)
  {
  }

  public String javaType(boolean isDTO)
  {
    return Polygon.class.getName();
  }

  protected String generatedServerGetter(String attributeName)
  {
    String getter = "(" + Polygon.class.getName() + ")" + generateTypesafeFormatting("getObjectValue(" + attributeName.toUpperCase() + ")");

    return generateTypesafeFormatting(getter);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributePolygonDAO getBusinessDAO()
  {
    return (MdAttributePolygonDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new MdAttributePointDAO. Some attributes will contain default values, as defined in the attribute metadata. Otherwise, the attributes will be blank.
   *
   * @return MdAttributePointDAO
   */
  public static MdAttributePolygonDAO newInstance()
  {
    return (MdAttributePolygonDAO) BusinessDAO.newInstance(MdAttributePolygonInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String, java.lang.String)
   */
  public static MdAttributePolygonDAOIF get(String oid)
  {
    return (MdAttributePolygonDAOIF) BusinessDAO.get(oid);
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    if (visitor instanceof MdAttributeGeoDAOVisitorIF)
    {
      ( (MdAttributeGeoDAOVisitorIF) visitor ).visitPolygon(this);
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributePolygonDAOIF.class.getName();
  }

}
