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

import java.util.Locale;
import java.util.Map;

import com.runwaysdk.constants.MdMobileCharacterInfo;
import com.runwaysdk.constants.MdWebCharacterInfo;
import com.runwaysdk.constants.MdWebFieldInfo;
import com.runwaysdk.constants.MobileFormFieldInfo;
import com.runwaysdk.dataaccess.AttributeReferenceIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdCharacterFieldDAOIF;
import com.runwaysdk.dataaccess.MdMobileCharacterDAOIF;
import com.runwaysdk.dataaccess.MdMobileFormDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeLocal;

public class MdMobileCharacterDAO extends MdMobilePrimitiveDAO implements MdMobileCharacterDAOIF, MdCharacterFieldDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -7399356395136703954L;

  public MdMobileCharacterDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public MdMobileCharacterDAO()
  {
    super();
  }

  @Override
  public String getDisplayLabel(Locale locale)
  {
    return ((AttributeLocal)this.getAttributeIF(MdMobileCharacterInfo.DISPLAY_LABEL)).getValue(locale);
  }

  @Override
  public Map<String, String> getDisplayLabels()
  {
    return ((AttributeLocal)this.getAttributeIF(MdMobileCharacterInfo.DISPLAY_LABEL)).getLocalValues();
  }

  @Override
  public String getFieldName()
  {
    return this.getAttribute(MdWebCharacterInfo.FIELD_NAME).getValue();
  }
  
  @Override
  public String getMdFormId()
  {
    return this.getAttribute(MdWebFieldInfo.DEFINING_MD_FORM).getValue();
  }
  
  @Override
  public MdMobileFormDAOIF getMdForm()
  {
    return (MdMobileFormDAOIF) ((AttributeReferenceIF)this.getAttribute(MdMobileCharacterInfo.DEFINING_MD_FORM)).dereference();
  }

  @Override
  public String getDisplayLength()
  {
    return this.getAttribute(MdMobileCharacterInfo.DISPLAY_LENGTH).getValue();
  }

  @Override
  public String getMaxLength()
  {
    return this.getAttribute(MdMobileCharacterInfo.MAX_LENGTH).getValue();
  }

  @Override
  public String getFieldOrder()
  {
    return this.getAttribute(MdMobileCharacterInfo.FIELD_ORDER).getValue();
  }
  
  public static MdWebCharacterDAO newInstance()
  {
    return (MdWebCharacterDAO) BusinessDAO.newInstance(MdWebCharacterInfo.CLASS);
  }
  
  @Override
  public MdMobileCharacterDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdMobileCharacterDAO(attributeMap, classType);
  }
  
  /**
   * Applies this MdField and creates the relationship to the MdForm if this
   * instance is new.
   */
  @Override
  public String apply()
  {
    boolean firstApply = (this.isNew() && !this.isAppliedToDB() && !this.isImport());
    
    String oid = super.apply();
    
    if(firstApply)
    {
      String formId = this.getMdFormId();
      RelationshipDAO rel = RelationshipDAO.newInstance(formId, oid, MobileFormFieldInfo.CLASS);
      rel.apply();
    }
    
    return oid;
  }
}
