/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.dataaccess.metadata;

import java.util.Map;
import java.util.Random;

import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityGenerator;
import com.runwaysdk.dataaccess.MdAttributeTextDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.transport.metadata.AttributeTextMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;
import com.runwaysdk.transport.metadata.caching.AttributeTextMdSession;

public class MdAttributeTextDAO extends MdAttributePrimitiveDAO implements MdAttributeTextDAOIF
{

  /**
   *
   */
  private static final long serialVersionUID = 2904359965050534300L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeTextDAO()
  {
    super();
  }

  /**
   * Constructs a MdAttributeText from the given {@link Map} of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null
   * 
   * @param attributeMap
   * @param type
   */
  public MdAttributeTextDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

//  /**
//   * Returns the maximum number of characters an attribute of type
//   * MdAttributeText can have.
//   * 
//   * @return maximum number of characters an attribute of type MdAttributeText
//   *         can have.
//   */
//  public static int getMaxLength()
//  {
//    return Database.getMaxTextLength();
//  }

  /**
   *
   */
  @Override
  protected void initializeStrategyObject()
  {
    if (this.definedByClass() instanceof MdEntityDAOIF)
    {
      this.mdAttributeStrategy = new MdAttributeText_E(this);
    }
    else
    {
      this.mdAttributeStrategy = new MdAttributeConcrete_S(this);
    }
  }

  /**
   * Called for java class generation. Returns the java type of this attribute
   * (String), which is used in the generated classes for type safety.
   * 
   * @return The java type of this attribute (String)
   */
  public String javaType(boolean isDTO)
  {
    return "String";
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
    return com.runwaysdk.query.SelectableChar.class.getName();
  }

  /**
   * Used for data generation. Returns a random 256-character String.
   */
  public void setRandomValue(EntityDAO object)
  {
    Random random = EntityGenerator.getRandom();
    String s = new String();
    String alpha = "abcdefghijklmnopqrstuvwxyz    ";
    for (int i = 0; i < 256; i++)
      s += alpha.charAt(random.nextInt(30));
    object.setValue(this.definesAttribute(), s);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Map)
   */
  public MdAttributeTextDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeTextDAO(attributeMap, MdAttributeTextInfo.CLASS);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeTextDAO getBusinessDAO()
  {
    return (MdAttributeTextDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new {@link MdAttributeTextDAO}. Some attributes will contain
   * default values, as defined in the attribute metadata. Otherwise, the
   * attributes will be blank.
   * 
   * @return {@link MdAttributeTextDAO}.
   */
  public static MdAttributeTextDAO newInstance()
  {
    return (MdAttributeTextDAO) BusinessDAO.newInstance(MdAttributeTextInfo.CLASS);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdAttributeTextDAOIF get(String id)
  {
    return (MdAttributeTextDAOIF) BusinessDAO.get(id);
  }

  @Override
  public String attributeMdDTOType()
  {
    return AttributeTextMdDTO.class.getName();
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitText(this);
  }

  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession()
  {
    AttributeTextMdSession attrSes = new AttributeTextMdSession();
    super.populateAttributeMdSession(attrSes);
    return attrSes;
  }
}
