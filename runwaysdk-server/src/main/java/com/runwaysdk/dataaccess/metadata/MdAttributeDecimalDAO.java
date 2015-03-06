/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.metadata;

import java.util.Map;

import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDecimalUtil;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityGenerator;
import com.runwaysdk.dataaccess.MdAttributeDecimalDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.transport.metadata.caching.AttributeDecimalMdSession;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeDecimalDAO extends MdAttributeDecDAO implements MdAttributeDecimalDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = -6182874904508545134L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeDecimalDAO()
  {
    super();
  }


  public MdAttributeDecimalDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }


  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdAttributeDecimalDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeDecimalDAO(attributeMap, classType);
  }

  @Override
  protected void initializeStrategyObject()
  {
    if (this.definedByClass() instanceof MdEntityDAOIF)
    {
      this.mdAttributeStrategy = new MdAttributeDec_E(this);
    }
    else
    {
      this.mdAttributeStrategy = new MdAttributeConcrete_S(this);
    }
  }

  /**
   * Called for java class generation.  Returns the java type of this attribute
   * (double), which is used in the generated classes for type safety.
   *
   * @return The java type of this attribute (double)
   */
  public String javaType(boolean isDTO)
  {
    return "java.math.BigDecimal";
  }

  /**
   * Returns a string representing the query attribute class for attributes of this type.
   *
   * @return string representing the query attribute class for attributes of this type.
   */
  public String queryAttributeClass()
  {
    return com.runwaysdk.query.SelectableDecimal.class.getName();
  }

  @Override
  protected String generatedServerSetter(String attributeName)
  {
    String conversion = "value.toString()";
    return this.setterWrapper(attributeName, conversion);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.metadata.MdAttribute#generateTypesafeFormatting(java.lang.String)
   */
  protected String generateTypesafeFormatting(String formatMe)
  {
    String util = MdAttributeDecimalUtil.class.getName();
    return util + ".getTypeSafeValue(" + formatMe + ")";
  }

  /**
   * Used for data generation. Returns a random double.
   */
  public void setRandomValue(EntityDAO object)
  {
    object.setValue(this.definesAttribute(), "" + EntityGenerator.getRandom().nextDouble());
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeDecimalDAO getBusinessDAO()
  {
    return (MdAttributeDecimalDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new MdAttributeDecimal.
   * Some attributes will contain default values, as defined in the attribute
   * metadata. Otherwise, the attributes will be blank.
   *
   * @return MdAttributeDecimal
   */
  public static MdAttributeDecimalDAO newInstance()
  {
    return (MdAttributeDecimalDAO) BusinessDAO.newInstance(MdAttributeDecimalInfo.CLASS);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String, java.lang.String)
   */
  public static MdAttributeDecimalDAOIF get(String id)
  {
    return (MdAttributeDecimalDAOIF) BusinessDAO.get(id);
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitDecimal(this);
  }
  
  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession() 
  {
    AttributeDecimalMdSession attrSes = new AttributeDecimalMdSession();
    super.populateAttributeMdSession(attrSes);
    return attrSes;
  }

  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeDecimalDAOIF.class.getName();
  }
  
}
