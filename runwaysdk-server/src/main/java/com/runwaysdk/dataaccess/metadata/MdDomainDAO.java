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

import com.runwaysdk.constants.MdDomainInfo;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdDomainDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeLocal;

public class MdDomainDAO extends MetadataDAO implements MdDomainDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = 1209375092375923L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdDomainDAO()
  {
    super();
  }

  public static String buildKey(String domainName)
  {
    return domainName;
  }
  
  /**
  *
  */
 public String apply()
 {
   String key = buildKey(this.getName());
   this.setKey(key);
   
   return super.apply();
 }
  
  /**
   * Returns the signature of the metadata.
   *
   * @return signature of the metadata.
   */
  public String getSignature()
  {
    return "Name:"+this.getName();
  }

  /**
   * Returns the name of this domain.
   * @return name of this domain.
   */
  public String getName()
  {
    return this.getAttributeIF(MdDomainInfo.DOMAIN_NAME).getValue();
  }

  /**
   * Constructs a MdDomain from the given hashtable of Attributes.
   *
   * <br/><b>Precondition:</b> attributeMap != null
   * <br/><b>Precondition:</b> type != null <br/>
   *
   *
   * @param attributeMap
   * @param type
   */
  public MdDomainDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdDomainDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdDomainDAO(attributeMap, MdDomainInfo.CLASS);
  }

  /**
   * Returns the display label of this metadata object
   *
   * @param
   *
   * @return the display label of this metadata object
   */
  public String getDisplayLabel(Locale locale)
  {
    return ((AttributeLocal)this.getAttributeIF(MdDomainInfo.DISPLAY_LABEL)).getValue(locale);
  }

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getDisplayLabels()
  {
    return ((AttributeLocalIF)this.getAttributeIF(MdDomainInfo.DISPLAY_LABEL)).getLocalValues();
  }
  
  /**
   * Returns a new MdDomain.
   * Some attributes will contain default values, as defined in the attribute
   * metadata. Otherwise, the attributes will be blank.
   *
   * @return instance of MdDomain.
   */
  public static MdDomainDAO newInstance()
  {
    return (MdDomainDAO) BusinessDAO.newInstance(MdDomainInfo.CLASS);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdDomainDAO getBusinessDAO()
  {
    return (MdDomainDAO) super.getBusinessDAO();
  }

}
