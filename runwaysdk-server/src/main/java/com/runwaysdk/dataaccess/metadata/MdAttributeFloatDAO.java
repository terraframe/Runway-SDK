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

import com.runwaysdk.constants.MdAttributeDecInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeFloatUtil;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityGenerator;
import com.runwaysdk.dataaccess.MdAttributeFloatDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.transport.metadata.caching.AttributeFloatMdSession;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeFloatDAO extends MdAttributeDecDAO implements MdAttributeFloatDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = -7818664430668441985L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeFloatDAO()
  {
    super();
  }


  public MdAttributeFloatDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
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
   * (float), which is used in the generated classes for type safety.
   *
   * @return The java type of this attribute (float)
   */
  public String javaType(boolean isDTO)
  {
    return "Float";
  }

  /**
   * Returns a string representing the query attribute class for attributes of this type.
   *
   * @return string representing the query attribute class for attributes of this type.
   */
  public String queryAttributeClass()
  {
    return com.runwaysdk.query.SelectableFloat.class.getName();
  }

  @Override
  protected String generatedServerSetter(String attributeName)
  {
    String conversion = "java.lang.Float.toString(value)";
    return this.setterWrapper(attributeName, conversion);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.metadata.MdAttribute#generateTypesafeFormatting(java.lang.String)
   */
  protected String generateTypesafeFormatting(String formatMe)
  {
    String util = MdAttributeFloatUtil.class.getName();
    return util + ".getTypeSafeValue(" + formatMe + ")";
  }

  /**
   * Used for data generation. Returns a random float.
   */
  public void setRandomValue(EntityDAO object)
  {
    int length = Integer.parseInt(attributeMap.get(MdAttributeDecInfo.LENGTH).getValue());
    int decimal= Integer.parseInt(attributeMap.get(MdAttributeDecInfo.DECIMAL).getValue());
    float random = EntityGenerator.getRandom().nextFloat();
    random *= Math.pow(10.0, EntityGenerator.getRandom().nextInt(length - decimal));
    object.setValue(this.definesAttribute(), "" + random);
  }


  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdAttributeFloatDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeFloatDAO(attributeMap, classType);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeFloatDAO getBusinessDAO()
  {
    return (MdAttributeFloatDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new MdAttributeFloat.
   * Some attributes will contain default values, as defined in the attribute
   * metadata. Otherwise, the attributes will be blank.
   *
   * @return dAttributeFloat.
   */
  public static MdAttributeFloatDAO newInstance()
  {
    return (MdAttributeFloatDAO) BusinessDAO.newInstance(MdAttributeFloatInfo.CLASS);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String, java.lang.String)
   */
  public static MdAttributeFloatDAOIF get(String id)
  {
    return (MdAttributeFloatDAOIF) BusinessDAO.get(id);
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitFloat(this);
  }
  
  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession() {
    AttributeFloatMdSession attrSes = new AttributeFloatMdSession();
    super.populateAttributeMdSession(attrSes);
    return attrSes;
  }
}
