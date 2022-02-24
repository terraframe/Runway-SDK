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

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeLocalTextInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalTextDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTextDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.MdTransientDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeLocal;
import com.runwaysdk.transport.metadata.AttributeLocalTextMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeLocalTextMdSession;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeLocalTextDAO extends MdAttributeLocalDAO implements MdAttributeLocalTextDAOIF
{
  private static final long serialVersionUID = 3557608928227475156L;

  public MdAttributeLocalTextDAO()
  {
    super();
  }

  public MdAttributeLocalTextDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Map)
   */
  public MdAttributeLocalTextDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeLocalTextDAO(attributeMap, classType);
  }

  @Override
  public MdAttributeLocalTextDAO getBusinessDAO()
  {
    return (MdAttributeLocalTextDAO) super.getBusinessDAO();
  }

  public static MdAttributeLocalTextDAO newInstance()
  {
    return (MdAttributeLocalTextDAO) BusinessDAO.newInstance(MdAttributeLocalTextInfo.CLASS);
  }

  @Override
  protected void initializeStrategyObject()
  {
    if (this.definedByClass() instanceof MdEntityDAOIF)
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeLocal_E(this));
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

  public void addDefaultLocale()
  {
    addDefaultLocale(this.getMdStructDAOIF());
  }

  public static MdAttributeTextDAOIF addDefaultLocale(MdLocalStructDAOIF mdLocalStructDAOIF)
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

  protected static MdAttributeTextDAOIF addLocale(String attributeName, String columnName, String displayLabel, String description, MdLocalStructDAOIF mdLocalStructDAOIF)
  {
    MdAttributeTextDAO localText = MdAttributeTextDAO.newInstance();

    localText.getAttribute(MdAttributeTextInfo.NAME).setValue(attributeName);

    if (columnName.trim().length() != 0)
    {
      localText.getAttribute(MdAttributeTextInfo.COLUMN_NAME).setValue(columnName);
    }

    ( (AttributeLocal) localText.getAttribute(MdAttributeTextInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, displayLabel);
    ( (AttributeLocal) localText.getAttribute(MdAttributeTextInfo.DESCRIPTION) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, description);

    localText.getAttribute(MdAttributeTextInfo.REQUIRED).setValue(MdAttributeBooleanInfo.FALSE);
    localText.getAttribute(MdAttributeTextInfo.IMMUTABLE).setValue(MdAttributeBooleanInfo.FALSE);
    localText.getAttribute(MdAttributeTextInfo.GENERATE_ACCESSOR).setValue(Boolean.toString(false));
    localText.getAttribute(MdAttributeTextInfo.DEFINING_MD_CLASS).setValue(mdLocalStructDAOIF.getOid());
    localText.apply();

    return localText;
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitLocalText(this);
  }

  @Override
  public String attributeMdDTOType()
  {
    return AttributeLocalTextMdDTO.class.getName();
  }
  
  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession() 
  {
    AttributeLocalTextMdSession attrSes = new AttributeLocalTextMdSession();
    super.populateAttributeMdSession(attrSes);
    return attrSes;
  }

  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeLocalTextDAOIF.class.getName();
  }
  
}
