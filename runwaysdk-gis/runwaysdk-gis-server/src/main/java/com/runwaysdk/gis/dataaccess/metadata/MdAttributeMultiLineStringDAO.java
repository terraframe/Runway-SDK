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
package com.runwaysdk.gis.dataaccess.metadata;

import java.util.Map;

import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAOVisitor;
import com.runwaysdk.gis.constants.MdAttributeMultiLineStringInfo;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiLineStringDAOIF;
import com.runwaysdk.gis.transport.metadata.AttributeMultiLineStringMdDTO;
import com.vividsolutions.jts.geom.MultiLineString;

public class MdAttributeMultiLineStringDAO extends MdAttributeGeometryDAO implements MdAttributeMultiLineStringDAOIF
{

  /**
   *
   */
  private static final long serialVersionUID = -227679672210203008L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeMultiLineStringDAO()
  {
    super();
  }

  /**
   * Constructs a MdAttributeMultiLineStringDAO from the given hashtable of Attributes.
   *
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null
   *
   * @param attributeMap
   * @param classType
   */
  public MdAttributeMultiLineStringDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdAttributeMultiLineStringDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeMultiLineStringDAO(attributeMap, classType);
  }

  @Override
  public String attributeMdDTOType()
  {
    return AttributeMultiLineStringMdDTO.class.getName();
  }

  public void setRandomValue(EntityDAO object)
  {
  }

  public String javaType(boolean isDTO)
  {
    return MultiLineString.class.getName();
  }

  protected String generatedServerGetter(String attributeName)
  {
    String getter = "(" + MultiLineString.class.getName() + ")" + generateTypesafeFormatting("getObjectValue(" + attributeName.toUpperCase() + ")");

    return generateTypesafeFormatting(getter);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeMultiLineStringDAO getBusinessDAO()
  {
    return (MdAttributeMultiLineStringDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new MdAttributeMultiLineStringDAO. Some attributes will contain default values, as defined in the attribute metadata. Otherwise, the attributes will be blank.
   *
   * @return MdAttributeMultiLineStringDAO
   */
  public static MdAttributeMultiLineStringDAO newInstance()
  {
    return (MdAttributeMultiLineStringDAO) BusinessDAO.newInstance(MdAttributeMultiLineStringInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String, java.lang.String)
   */
  public static MdAttributeMultiLineStringDAOIF get(String oid)
  {
    return (MdAttributeMultiLineStringDAOIF) BusinessDAO.get(oid);
  }

  public void accept(MdAttributeDAOVisitor visitor)
  {
    if (visitor instanceof MdAttributeGeoDAOVisitorIF)
    {
      ( (MdAttributeGeoDAOVisitorIF) visitor ).visitMultiLineString(this);
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeMultiLineStringDAOIF.class.getName();
  }

}
