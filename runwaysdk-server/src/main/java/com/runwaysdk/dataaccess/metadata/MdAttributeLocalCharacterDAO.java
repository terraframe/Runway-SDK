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

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalCharacterDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeLocal;
import com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeLocalCharacterMdSession;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeLocalCharacterDAO extends MdAttributeLocalDAO implements MdAttributeLocalCharacterDAOIF
{
  private static final long serialVersionUID = -8736721459795325659L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeLocalCharacterDAO()
  {
    super();
  }

  /**
   * Constructs a MdAttributeStruct from the given hashtable of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> classType != null
   * 
   * 
   * @param attributeMap
   * @param classType
   */
  public MdAttributeLocalCharacterDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Map, String)
   */
  public MdAttributeLocalCharacterDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeLocalCharacterDAO(attributeMap, classType);
  }

  @Override
  public MdAttributeLocalCharacterDAO getBusinessDAO()
  {
    return (MdAttributeLocalCharacterDAO) super.getBusinessDAO();
  }

  public static MdAttributeLocalCharacterDAO newInstance()
  {
    return (MdAttributeLocalCharacterDAO) BusinessDAO.newInstance(MdAttributeLocalCharacterInfo.CLASS);
  }
  
  public void addDefaultLocale()
  {   
    addDefaultLocale(this.getMdStructDAOIF());
  }
  
  public static MdAttributeCharacterDAOIF addDefaultLocale(MdLocalStructDAOIF mdLocalStructDAOIF)
  {   
    String attributeName = MdAttributeLocalInfo.DEFAULT_LOCALE;
    String columnName = "";    
    String displayLabel = "Default Locale";
    String description = "Default locale";

    return addLocale(attributeName, columnName, displayLabel, description, mdLocalStructDAOIF);
  }

  protected MdAttributeDAOIF addLocaleWrapper(String attributeName, String columnName, String displayLabel, String description, MdLocalStructDAOIF mdLocalStructDAOIF)
  {
    return addLocale(attributeName, columnName, displayLabel, description, mdLocalStructDAOIF);
  }
  
  protected static MdAttributeCharacterDAOIF addLocale(String attributeName, String columnName, String displayLabel, String description, MdLocalStructDAOIF mdLocalStructDAOIF)
  {
    MdAttributeCharacterDAO localChar = MdAttributeCharacterDAO.newInstance();
    localChar.getAttribute(MdAttributeCharacterInfo.NAME).setValue(attributeName);
    if (columnName.trim().length() != 0)
    {
      localChar.getAttribute(MdAttributeCharacterInfo.COLUMN_NAME).setValue(columnName);
    }
    ( (AttributeLocal) localChar.getAttribute(MdAttributeCharacterInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, displayLabel);
    ( (AttributeLocal) localChar.getAttribute(MdAttributeCharacterInfo.DESCRIPTION) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, description);
    localChar.getAttribute(MdAttributeCharacterInfo.REQUIRED).setValue(MdAttributeBooleanInfo.FALSE);
    localChar.getAttribute(MdAttributeCharacterInfo.IMMUTABLE).setValue(MdAttributeBooleanInfo.FALSE);
    localChar.getAttribute(MdAttributeCharacterInfo.SIZE).setValue(Integer.toString(MdAttributeCharacterInfo.MAX_CHARACTER_SIZE));
    localChar.getAttribute(MdAttributeCharacterInfo.GENERATE_ACCESSOR).setValue(Boolean.toString(false));
    localChar.getAttribute(MdAttributeCharacterInfo.DEFINING_MD_CLASS).setValue(mdLocalStructDAOIF.getOid());
    localChar.apply();

    return localChar;
  }

  /**
   * Returns the MdStructIF that defines the class used to store the values of
   * the struct attribute.
   * 
   * @return the MdStructIF that defines the class used to store the values of
   *         the struct attribute.
   */
  public MdLocalStructDAOIF getMdStructDAOIF()
  {
    return (MdLocalStructDAOIF) super.getMdStructDAOIF();
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitLocalCharacter(this);
  }
  

  @Override
  public String attributeMdDTOType()
  {
    return AttributeLocalCharacterMdDTO.class.getName();
  }
  
  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession() 
  {
    AttributeLocalCharacterMdSession attrSes = new AttributeLocalCharacterMdSession();
    super.populateAttributeMdSession(attrSes);
    return attrSes;
  }
  
  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeLocalCharacterDAOIF.class.getName();
  }
}
