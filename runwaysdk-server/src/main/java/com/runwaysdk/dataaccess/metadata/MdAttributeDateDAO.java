/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateUtil;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityGenerator;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdTransientDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeFactory;
import com.runwaysdk.dataaccess.metadata.graph.MdAttributeConcrete_G;
import com.runwaysdk.transport.metadata.AttributeDateMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeDateMdSession;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeDateDAO extends MdAttributeMomentDAO implements MdAttributeDateDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -5327735080507758914L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeDateDAO()
  {
    super();
  }

  public MdAttributeDateDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdAttributeDateDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeDateDAO(attributeMap, classType);
  }

  @Override
  public String getFormat()
  {
    return Constants.DATE_FORMAT;
  }

  @Override
  protected void initializeStrategyObject()
  {
    if (this.definedByClass() instanceof MdEntityDAOIF)
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeConcrete_E(this));
    }
    else if (this.definedByClass() instanceof MdGraphClassDAOIF)
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeConcrete_G(this));
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

  @Override
  protected String generatedServerSetter(String attributeName)
  {
    String conversion = "new java.text.SimpleDateFormat(" + Constants.class.getName() + ".DATE_FORMAT).format(value)";
    return this.setterWrapper(attributeName, conversion);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.metadata.MdAttribute#generateTypesafeFormatting
   * (java.lang.String)
   */
  protected String generateTypesafeFormatting(String formatMe)
  {
    String util = MdAttributeDateUtil.class.getName();
    return util + ".getTypeSafeValue(" + formatMe + ")";
  }

  /**
   * Used for data generation. Returns a random Date between 1970-01-01 and
   * about 3969-12-31.
   */
  public void setRandomValue(EntityDAO object)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
    long time = EntityGenerator.getRandom().nextLong();
    if (time < 0)
      time *= -1;
    object.setValue(definesAttribute(), sdf.format(new Date(time / 292472)));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeDateDAO getBusinessDAO()
  {
    return (MdAttributeDateDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new MdAttributeDate. Some attributes will contain default values,
   * as defined in the attribute metadata. Otherwise, the attributes will be
   * blank.
   * 
   * 
   * @return MdAttributeDate.
   */
  public static MdAttributeDateDAO newInstance()
  {
    return (MdAttributeDateDAO) BusinessDAO.newInstance(MdAttributeDateInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String,
   * java.lang.String)
   */
  public static MdAttributeDateDAOIF get(String oid)
  {
    return (MdAttributeDateDAOIF) BusinessDAO.get(oid);
  }

  @Override
  public String attributeMdDTOType()
  {
    return AttributeDateMdDTO.class.getName();
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitDate(this);
  }

  @Override
  public String save(boolean validateRequired)
  {
    this.validateStartDate();
    this.validateEndDate();

    return super.save(validateRequired);
  }

  private void validateStartDate()
  {
    this.validateSelfAttribute(MdAttributeDateInfo.START_DATE);
  }

  private void validateEndDate()
  {
    this.validateSelfAttribute(MdAttributeDateInfo.END_DATE);
  }

  private void validateSelfAttribute(String attributeName)
  {
    if (this.getAttribute(attributeName).isModified())
    {
      String value = this.getAttribute(attributeName).getValue();
      MdClassDAOIF definingMdClassDAOIF = this.definedByClass();

      Attribute spoofAttribute = AttributeFactory.createAttribute(this.getKey(), this.getType(), attributeName, definingMdClassDAOIF.definesType(), value);
      spoofAttribute.setContainingComponent(this);

      MdAttributeDateDAO definingMdAttribute = (MdAttributeDateDAO) this.getBusinessDAO();

      if (definingMdAttribute.hasAttribute(MdAttributeConcreteInfo.IMMUTABLE))
      {
        definingMdAttribute.setValue(MdAttributeConcreteInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      }

      spoofAttribute.validate(definingMdAttribute, value);
    }
  }
  
  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession() 
  {
    AttributeDateMdSession attrSes = new AttributeDateMdSession();
    super.populateAttributeMdSession(attrSes);
    return attrSes;
  }
  
  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeDateDAOIF.class.getName();
  }
}
