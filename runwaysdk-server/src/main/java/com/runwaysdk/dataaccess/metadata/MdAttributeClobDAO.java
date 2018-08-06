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
import java.util.Random;

import com.runwaysdk.constants.MdAttributeClobInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityGenerator;
import com.runwaysdk.dataaccess.MdAttributeClobDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdTransientDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.transport.metadata.AttributeClobMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeClobDAO extends MdAttributePrimitiveDAO implements MdAttributeClobDAOIF
{

  /**
   *
   */
  private static final long serialVersionUID = -7002222234507425970L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeClobDAO()
  {
    super();
  }

  /**
   * Constructs a MdAttributeClob from the given {@link Map} of Attributes.
   *
   * <br/><b>Precondition:</b>   attributeMap != null
   * <br/><b>Precondition:</b>   type != null
   *
   * @param attributeMap
   * @param type
   */
  public MdAttributeClobDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   *
   */
  @Override
  protected void initializeStrategyObject()
  {
    if (this.definedByClass() instanceof MdEntityDAOIF)
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeClob_E(this));
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
   * (String), which is used in the generated classes for type safety.
   *
   * @return The java type of this attribute (String)
   */
  public String javaType(boolean isDTO)
  {
    return "String";
  }
  
  /**
   * Returns the java class object for the attribute type.
   * 
   * @return the java class object for the attribute type.
   */
  public Class<?> javaClass()
  {
    return String.class;
  }

  /**
   * Returns a string representing the query attribute class for attributes of this type.
   *
   * @return string representing the query attribute class for attributes of this type.
   */
  public String queryAttributeClass()
  {
    return com.runwaysdk.query.SelectableChar.class.getName();
  }

  /**
   * Used for data generation.  Returns a random 256-character String.
   */
  public void setRandomValue(EntityDAO object)
  {
    Random random = EntityGenerator.getRandom();
    String s = new String();
    String alpha = "abcdefghijklmnopqrstuvwxyz    ";
    for (int i=0; i<256; i++)
      s += alpha.charAt(random.nextInt(30));
    object.setValue(this.definesAttribute(), s);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Map)
   */
  public MdAttributeClobDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeClobDAO(attributeMap, MdAttributeClobInfo.CLASS);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeClobDAO getBusinessDAO()
  {
    return (MdAttributeClobDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new {@link MdAttributeClobDAO}.
   * Some attributes will contain default values, as defined in the attribute
   * metadata. Otherwise, the attributes will be blank.
   *
   * @return {@link MdAttributeClobDAO}.
   */
  public static MdAttributeClobDAO newInstance()
  {
    return (MdAttributeClobDAO) BusinessDAO.newInstance(MdAttributeClobInfo.CLASS);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdAttributeClobDAOIF get(String oid)
  {
    return (MdAttributeClobDAOIF) BusinessDAO.get(oid);
  }

  @Override
  public String attributeMdDTOType()
  {
    return AttributeClobMdDTO.class.getName();
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitClob(this);
  }
  
  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession() 
  {
    throw new UnsupportedOperationException();
  }
  
  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeClobDAOIF.class.getName();
  }
}
