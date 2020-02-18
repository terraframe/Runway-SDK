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

import java.util.Map;

import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeIntegerUtil;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityGenerator;
import com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdTransientDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.metadata.graph.MdAttributeConcrete_G;
import com.runwaysdk.transport.metadata.caching.AttributeIntegerMdSession;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeIntegerDAO extends MdAttributeNumberDAO implements MdAttributeIntegerDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = -3638554760117820252L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeIntegerDAO()
  {
    super();
  }

  public MdAttributeIntegerDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdAttributeIntegerDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeIntegerDAO(attributeMap, MdAttributeIntegerInfo.CLASS);
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

  /**
   * Called for java class generation. Returns the java type of this attribute
   * (int), which is used in the generated classes for type safety.
   *
   * @return The java type of this attribute (int)
   */
  public String javaType(boolean isDTO)
  {
    return "Integer";
  }

  /**
   * Returns the java class object for the attribute type.
   * 
   * @return the java class object for the attribute type.
   */
  public Class<?> javaClass()
  {
    return Integer.class;
  }

  /**
   * Returns a string representing the query attribute class for attributes of
   * this type.
   *
   * @return string representing the query attribute class for attributes of
   *         this type.
   */
  public String queryAttributeClass()
  {
    return com.runwaysdk.query.SelectableInteger.class.getName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.metadata.MdAttribute#generateTypesafeFormatting(
   * java.lang.String)
   */
  protected String generateTypesafeFormatting(String formatMe)
  {
    String util = MdAttributeIntegerUtil.class.getName();
    return util + ".getTypeSafeValue(" + formatMe + ")";
  }

  @Override
  protected String generatedServerSetter(String attributeName)
  {
    String conversion = "java.lang.Integer.toString(value)";
    return this.setterWrapper(attributeName, conversion);
  }

  /**
   * Used for data generation. Returns a random int.
   */
  public void setRandomValue(EntityDAO object)
  {
    object.setValue(this.definesAttribute(), "" + EntityGenerator.getRandom().nextInt());
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeIntegerDAO getBusinessDAO()
  {
    return (MdAttributeIntegerDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new MdAttributeInteger. Some attributes will contain default
   * values, as defined in the attribute metadata. Otherwise, the attributes
   * will be blank.
   *
   * @return MdAttributeInteger.
   */
  public static MdAttributeIntegerDAO newInstance()
  {
    return (MdAttributeIntegerDAO) BusinessDAO.newInstance(MdAttributeIntegerInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String,
   * java.lang.String)
   */
  public static MdAttributeIntegerDAOIF get(String oid)
  {
    return (MdAttributeIntegerDAOIF) BusinessDAO.get(oid);
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitInteger(this);
  }

  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession()
  {
    AttributeIntegerMdSession attrSes = new AttributeIntegerMdSession();
    super.populateAttributeMdSession(attrSes);
    return attrSes;
  }

  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeIntegerDAOIF.class.getName();
  }
}
