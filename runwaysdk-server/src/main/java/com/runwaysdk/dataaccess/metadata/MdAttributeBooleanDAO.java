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

import java.util.Locale;
import java.util.Map;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeBooleanUtil;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityGenerator;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.session.Session;
import com.runwaysdk.transport.metadata.AttributeBooleanMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeBooleanMdSession;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeBooleanDAO extends MdAttributePrimitiveDAO implements MdAttributeBooleanDAOIF
{

  /**
   *
   */
  private static final long serialVersionUID = 998077633471702965L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeBooleanDAO()
  {
    super();
  }

  /**
   * Constructs a MdAttributeBoolean from the given hashtable of Attributes.
   *
   * <br/><b>Precondition:</b>   attributeMap != null
   * <br/><b>Precondition:</b>   type != null
   *
   * @param attributeMap
   * @param type
   */
  public MdAttributeBooleanDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdAttributeBooleanDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeBooleanDAO(attributeMap, classType);
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


  /**
   * Returns the positive display label of this metadata object
   *
   * @param locale
   *
   * @return the positive display label of this metadata object
   */
  public String getPositiveDisplayLabel(Locale locale)
  {
    return ((AttributeLocalIF)this.getAttributeIF(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL)).getValue(locale);
  }

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getPositiveDisplayLabels()
  {
    return ((AttributeLocalIF)this.getAttributeIF(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL)).getLocalValues();
  }

  /**
   * Returns the negative display label of this metadata object
   *
   * @param locale
   *
   * @return the negative display label of this metadata object
   */
  public String getNegativeDisplayLabel(Locale locale)
  {
    return ((AttributeLocalIF)this.getAttributeIF(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL)).getValue(locale);
  }

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getNegativeDisplayLabels()
  {
    return ((AttributeLocalIF)this.getAttributeIF(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL)).getLocalValues();
  }

  /**
   * Called for java class generation.  Returns the java type of this attribute
   * (boolean), which is used in the generated classes for type safety.
   *
   * @return The java type of this attribute (boolean)
   */
  public String javaType(boolean isDTO)
  {
    return "Boolean";
  }
  /**
   * Returns a string representing the query attribute class for attributes of this type.
   *
   * @return string representing the query attribute class for attributes of this type.
   */
  public String queryAttributeClass()
  {
    return com.runwaysdk.query.SelectableBoolean.class.getName();
  }

  @Override
  protected String generatedServerSetter(String attributeName)
  {
    String conversion = "java.lang.Boolean.toString(value)";
    return this.setterWrapper(attributeName, conversion);
  }

  /**
   * Used for data generation.  Returns a random boolean.
   */
  public void setRandomValue(EntityDAO object)
  {
    object.setValue(this.definesAttribute(), "" + EntityGenerator.getRandom().nextBoolean());
  }


  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.metadata.MdAttribute#generateTypesafeFormatting(java.lang.String)
   */
  protected String generateTypesafeFormatting(String formatMe)
  {
    String util = MdAttributeBooleanUtil.class.getName();
    return util + ".getTypeSafeValue(" + formatMe + ")";
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeBooleanDAO getBusinessDAO()
  {
    return (MdAttributeBooleanDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new BusinessDAO.
   * Some attributes will contain default values, as defined in the attribute
   * metadata. Otherwise, the attributes will be blank.
   *
   * @return BusinessDAO instance of MdAttributeBoolean.
   */
  public static MdAttributeBooleanDAO newInstance()
  {
    return (MdAttributeBooleanDAO) BusinessDAO.newInstance(MdAttributeBooleanInfo.CLASS);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String, java.lang.String)
   */
  public static MdAttributeBooleanDAOIF get(String id)
  {
    return (MdAttributeBooleanDAOIF) BusinessDAO.get(id);
  }

  @Override
  public String attributeMdDTOType()
  {
    return AttributeBooleanMdDTO.class.getName();
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitBoolean(this);
  }

  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession() 
  {
    AttributeBooleanMdSession attrSes = new AttributeBooleanMdSession(this.getPositiveDisplayLabel(Session.getCurrentLocale()), this.getNegativeDisplayLabel(Session.getCurrentLocale()));
    super.populateAttributeMdSession(attrSes);
    return attrSes;
  }

  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeBooleanDAOIF.class.getName();
  }
  
}
