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
import com.runwaysdk.gis.constants.MdAttributeShapeInfo;
import com.runwaysdk.gis.dataaccess.MdAttributeShapeDAOIF;
import com.runwaysdk.gis.transport.metadata.AttributeShapeMdDTO;
import org.locationtech.jts.geom.Geometry;

public class MdAttributeShapeDAO extends MdAttributeGeometryDAO implements MdAttributeShapeDAOIF
{

  /**
   *
   */
  private static final long serialVersionUID = -8402455863045559995L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeShapeDAO()
  {
    super();
  }

  /**
   * Constructs a MdAttributeShapeDAO from the given hashtable of Attributes.
   *
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null
   *
   * @param attributeMap
   * @param classType
   */
  public MdAttributeShapeDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdAttributeShapeDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeShapeDAO(attributeMap, classType);
  }

  @Override
  public String attributeMdDTOType()
  {
    return AttributeShapeMdDTO.class.getName();
  }

  public void setRandomValue(EntityDAO object)
  {
  }

  public String javaType(boolean isDTO)
  {
    return Geometry.class.getName();
  }

  protected String generatedServerGetter(String attributeName)
  {
    String getter = "(" + Geometry.class.getName() + ")" + generateTypesafeFormatting("getObjectValue(" + attributeName.toUpperCase() + ")");

    return generateTypesafeFormatting(getter);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeShapeDAO getBusinessDAO()
  {
    return (MdAttributeShapeDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new MdAttributePointDAO. Some attributes will contain default values, as defined in the attribute metadata. Otherwise, the attributes will be blank.
   *
   * @return MdAttributePointDAO
   */
  public static MdAttributeShapeDAO newInstance()
  {
    return (MdAttributeShapeDAO) BusinessDAO.newInstance(MdAttributeShapeInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String, java.lang.String)
   */
  public static MdAttributeShapeDAOIF get(String oid)
  {
    return (MdAttributeShapeDAOIF) BusinessDAO.get(oid);
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    throw new UnsupportedOperationException();
  }

  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeShapeDAOIF.class.getName();
  }

}
