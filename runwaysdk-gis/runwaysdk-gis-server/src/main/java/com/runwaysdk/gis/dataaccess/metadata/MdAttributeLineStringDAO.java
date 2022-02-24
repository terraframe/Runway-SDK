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
import com.runwaysdk.gis.constants.MdAttributeLineStringInfo;
import com.runwaysdk.gis.dataaccess.MdAttributeLineStringDAOIF;
import com.runwaysdk.gis.transport.metadata.AttributeLineStringMdDTO;
import com.vividsolutions.jts.geom.LineString;

public class MdAttributeLineStringDAO extends MdAttributeGeometryDAO implements MdAttributeLineStringDAOIF
{

  /**
   *
   */
  private static final long serialVersionUID = 5186356969637344991L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeLineStringDAO()
  {
    super();
  }

  /**
   * Constructs a MdAttributeLineStringDAO from the given hashtable of Attributes.
   *
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null
   *
   * @param attributeMap
   * @param classType
   */
  public MdAttributeLineStringDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdAttributeLineStringDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeLineStringDAO(attributeMap, classType);
  }

  @Override
  public String attributeMdDTOType()
  {
    return AttributeLineStringMdDTO.class.getName();
  }

  public void setRandomValue(EntityDAO object)
  {
  }

  public String javaType(boolean isDTO)
  {
    return LineString.class.getName();
  }

  protected String generatedServerGetter(String attributeName)
  {
    String getter = "(" + LineString.class.getName() + ")" + generateTypesafeFormatting("getObjectValue(" + attributeName.toUpperCase() + ")");

    return generateTypesafeFormatting(getter);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeLineStringDAO getBusinessDAO()
  {
    return (MdAttributeLineStringDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new MdAttributeLineStringDAO. Some attributes will contain default values, as defined in the attribute metadata. Otherwise, the attributes will be blank.
   *
   * @return MdAttributeLineStringDAO
   */
  public static MdAttributeLineStringDAO newInstance()
  {
    return (MdAttributeLineStringDAO) BusinessDAO.newInstance(MdAttributeLineStringInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String, java.lang.String)
   */
  public static MdAttributeLineStringDAOIF get(String oid)
  {
    return (MdAttributeLineStringDAOIF) BusinessDAO.get(oid);
  }

  public void accept(MdAttributeDAOVisitor visitor)
  {
    if (visitor instanceof MdAttributeGeoDAOVisitorIF)
    {
      ( (MdAttributeGeoDAOVisitorIF) visitor ).visitLineString(this);
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeLineStringDAOIF.class.getName();
  }
}
