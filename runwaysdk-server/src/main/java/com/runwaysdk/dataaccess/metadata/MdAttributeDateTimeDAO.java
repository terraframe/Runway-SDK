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
package com.runwaysdk.dataaccess.metadata;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeDateTimeUtil;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityGenerator;
import com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeDateTimeMdSession;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeDateTimeDAO extends MdAttributeMomentDAO implements MdAttributeDateTimeDAOIF
{
  
  /**
   * 
   */
  private static final long serialVersionUID = 3449164074717243887L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeDateTimeDAO()
  {
    super();
  }
 
  
  public MdAttributeDateTimeDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdAttributeDateTimeDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeDateTimeDAO(attributeMap, classType);
  }
  
  @Override
  public String getFormat()
  {
    return Constants.DATETIME_FORMAT;
  }

  @Override
  protected void initializeStrategyObject()
  {
    if (this.definedByClass() instanceof MdEntityDAOIF)
    {
      this.mdAttributeStrategy = new MdAttributeConcrete_E(this);
    }
    else
    {
      this.mdAttributeStrategy = new MdAttributeConcrete_S(this);
    }
  }

  @Override
  protected String generatedServerSetter(String attributeName)
  {
    String conversion = "new java.text.SimpleDateFormat("+Constants.class.getName()+".DATETIME_FORMAT).format(value)";
    return this.setterWrapper(attributeName, conversion);
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.metadata.MdAttribute#generateTypesafeFormatting(java.lang.String)
   */
  protected String generateTypesafeFormatting(String formatMe)
  {
    String util = MdAttributeDateTimeUtil.class.getName();
    return util + ".getTypeSafeValue(" + formatMe + ")";
  }

  /**
   * Used for data generation. Returns a random Date between 1970-01-01 00:00:00 and
   * about 3969-12-31 23:59:59.
   */
  public void setRandomValue(EntityDAO object)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_FORMAT);
    long time = EntityGenerator.getRandom().nextLong();
    if (time < 0) time *= -1;
    object.setValue(this.definesAttribute(), sdf.format(new Date(time / 292472)));
  }


  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeDateTimeDAO getBusinessDAO()
  {
    return (MdAttributeDateTimeDAO) super.getBusinessDAO();
  }
  
  
  /**
   * Returns a new MdAttributeDateTime.
   * Some attributes will contain default values, as defined in the attribute
   * metadata. Otherwise, the attributes will be blank.
   * 
   * @return MdAttributeDateTimeIF
   */
  public static MdAttributeDateTimeDAO newInstance()
  {
    return (MdAttributeDateTimeDAO) BusinessDAO.newInstance(MdAttributeDateTimeInfo.CLASS);
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String, java.lang.String)
   */
  public static MdAttributeDateTimeDAOIF get(String id)
  {
    return (MdAttributeDateTimeDAOIF) BusinessDAO.get(id);
  }


  @Override
  public String attributeMdDTOType()
  {
    return AttributeDateTimeMdDTO.class.getName();
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitDateTime(this);
  }
  
  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession() 
  {
    AttributeDateTimeMdSession attrSes = new AttributeDateTimeMdSession();
    super.populateAttributeMdSession(attrSes);
    return attrSes;
  }

  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeDateTimeDAOIF.class.getName();
  }
}
