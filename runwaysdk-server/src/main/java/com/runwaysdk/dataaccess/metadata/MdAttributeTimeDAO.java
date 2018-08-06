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
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.MdAttributeTimeUtil;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityGenerator;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdTransientDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.transport.metadata.AttributeTimeMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;
import com.runwaysdk.transport.metadata.caching.AttributeTimeMdSession;

public class MdAttributeTimeDAO extends MdAttributeMomentDAO implements MdAttributeTimeDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 6054724006397016278L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeTimeDAO()
  {
    super();
  }
 

  public MdAttributeTimeDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdAttributeTimeDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeTimeDAO(attributeMap, MdAttributeTimeInfo.CLASS);
  }
  
  @Override
  public String getFormat()
  {
    return Constants.TIME_FORMAT;
  }

  @Override
  protected void initializeStrategyObject()
  {
    if (this.definedByClass() instanceof MdEntityDAOIF)
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeConcrete_E(this));
    }
    else if (this.definedByClass() instanceof MdTransientDAOIF)
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeConcrete_S(this));
    }
    else
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeConcrete_T(this));
    }
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.metadata.MdAttribute#generateTypesafeFormatting(java.lang.String)
   */
  protected String generateTypesafeFormatting(String formatMe)
  {
    String util = MdAttributeTimeUtil.class.getName();
    return util + ".getTypeSafeValue(" + formatMe + ")";
  }

  @Override
  protected String generatedServerSetter(String attributeName)
  {
    String conversion = "new java.text.SimpleDateFormat("+Constants.class.getName()+".TIME_FORMAT).format(value)";
    return this.setterWrapper(attributeName, conversion);
  }
  
  /**
   * Used for data generation. Returns a random time.
   */
  public void setRandomValue(EntityDAO object)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT);
    object.setValue(definesAttribute(), sdf.format(new Date(EntityGenerator.getRandom().nextInt())));
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeTimeDAO getBusinessDAO()
  {
    return (MdAttributeTimeDAO) super.getBusinessDAO();
  }
  
  /**
   * Returns a new MdAttributeTime. 
   * Some attributes will contain default values, as defined in the attribute
   * metadata. Otherwise, the attributes will be blank.
   * 
   * @return MdAttributeTime
   */
  public static MdAttributeTimeDAO newInstance()
  {
    return (MdAttributeTimeDAO) BusinessDAO.newInstance(MdAttributeTimeInfo.CLASS);
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdAttributeTimeDAOIF get(String oid)
  {
    return (MdAttributeTimeDAOIF) BusinessDAO.get(oid);
  }


  @Override
  public String attributeMdDTOType()
  {
    return AttributeTimeMdDTO.class.getName();
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitTime(this);
  }
  
  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession() 
  {
    AttributeTimeMdSession attrSes = new AttributeTimeMdSession();
    super.populateAttributeMdSession(attrSes);
    return attrSes;
  }
  
  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeTimeDAOIF.class.getName();
  }
  
}
