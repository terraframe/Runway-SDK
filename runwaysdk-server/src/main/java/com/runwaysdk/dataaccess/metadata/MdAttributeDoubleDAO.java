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

import java.util.Map;

import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeDoubleUtil;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityGenerator;
import com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdTransientDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.metadata.graph.MdAttributeConcrete_G;
import com.runwaysdk.transport.metadata.caching.AttributeDoubleMdSession;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeDoubleDAO extends MdAttributeDecDAO implements MdAttributeDoubleDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = 3814438057168720079L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeDoubleDAO()
  {
    super();
  }

  public MdAttributeDoubleDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdAttributeDoubleDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeDoubleDAO(attributeMap, classType);
  }

  @Override
  protected void initializeStrategyObject()
  {
    if (this.definedByClass() instanceof MdEntityDAOIF)
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeDec_E(this));
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
   * Called for java class generation.  Returns the java type of this attribute
   * (double), which is used in the generated classes for type safety.
   *
   * @return The java type of this attribute (double)
   */
  public String javaType(boolean isDTO)
  {
    return "Double";
  }
  
  /**
   * Returns the java class object for the attribute type.
   * 
   * @return the java class object for the attribute type.
   */
  public Class<?> javaClass()
  {
    return Double.class;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.metadata.MdAttribute#generateTypesafeFormatting(java.lang.String)
   */
  protected String generateTypesafeFormatting(String formatMe)
  {
    String util = MdAttributeDoubleUtil.class.getName();
    return util + ".getTypeSafeValue(" + formatMe + ")";
  }

  /**
   * Returns a string representing the query attribute class for attributes of this type.
   *
   * @return string representing the query attribute class for attributes of this type.
   */
  public String queryAttributeClass()
  {
    return com.runwaysdk.query.SelectableDouble.class.getName();
  }

  /**
   * Used for data generation. Returns a random double.
   */
  public void setRandomValue(EntityDAO object)
  {
    object.setValue(this.definesAttribute(), "" + EntityGenerator.getRandom().nextDouble());
  }

  @Override
  protected String generatedServerSetter(String attributeName)
  {
    String conversion = "java.lang.Double.toString(value)";
    return this.setterWrapper(attributeName, conversion);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeDoubleDAO getBusinessDAO()
  {
    return (MdAttributeDoubleDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new MdAttributeDouble.
   * Some attributes will contain default values, as defined in the attribute
   * metadata. Otherwise, the attributes will be blank.
   *
   * @return MdAttributeDouble
   */
  public static MdAttributeDoubleDAO newInstance()
  {
    return (MdAttributeDoubleDAO) BusinessDAO.newInstance(MdAttributeDoubleInfo.CLASS);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String, java.lang.String)
   */
  public static MdAttributeDoubleDAOIF get(String oid)
  {
    return (MdAttributeDoubleDAOIF) BusinessDAO.get(oid);
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitDouble(this);
  }
  
  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession() 
  {
    AttributeDoubleMdSession attrSes = new AttributeDoubleMdSession();
    super.populateAttributeMdSession(attrSes);
    return attrSes;
  }

  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeDoubleDAOIF.class.getName();
  }
  
}
