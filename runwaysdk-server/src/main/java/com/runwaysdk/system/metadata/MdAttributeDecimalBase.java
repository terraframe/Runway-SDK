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
package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = -1046972080)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MdAttributeDecimal.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class MdAttributeDecimalBase extends com.runwaysdk.system.metadata.MdAttributeDec
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeDecimal";
  public final static java.lang.String DEFAULTVALUE = "defaultValue";
  public final static java.lang.String ENDRANGE = "endRange";
  public final static java.lang.String STARTRANGE = "startRange";
  private static final long serialVersionUID = -1046972080;
  
  public MdAttributeDecimalBase()
  {
    super();
  }
  
  public java.math.BigDecimal getDefaultValue()
  {
    return com.runwaysdk.constants.MdAttributeDecimalUtil.getTypeSafeValue(getValue(DEFAULTVALUE));
  }
  
  public void validateDefaultValue()
  {
    this.validateAttribute(DEFAULTVALUE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getDefaultValueMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdAttributeDecimal.CLASS);
    return mdClassIF.definesAttribute(DEFAULTVALUE);
  }
  
  public void setDefaultValue(java.math.BigDecimal value)
  {
    if(value == null)
    {
      setValue(DEFAULTVALUE, "");
    }
    else
    {
      setValue(DEFAULTVALUE, value.toString());
    }
  }
  
  public java.math.BigDecimal getEndRange()
  {
    return com.runwaysdk.constants.MdAttributeDecimalUtil.getTypeSafeValue(getValue(ENDRANGE));
  }
  
  public void validateEndRange()
  {
    this.validateAttribute(ENDRANGE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getEndRangeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdAttributeDecimal.CLASS);
    return mdClassIF.definesAttribute(ENDRANGE);
  }
  
  public void setEndRange(java.math.BigDecimal value)
  {
    if(value == null)
    {
      setValue(ENDRANGE, "");
    }
    else
    {
      setValue(ENDRANGE, value.toString());
    }
  }
  
  public java.math.BigDecimal getStartRange()
  {
    return com.runwaysdk.constants.MdAttributeDecimalUtil.getTypeSafeValue(getValue(STARTRANGE));
  }
  
  public void validateStartRange()
  {
    this.validateAttribute(STARTRANGE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getStartRangeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdAttributeDecimal.CLASS);
    return mdClassIF.definesAttribute(STARTRANGE);
  }
  
  public void setStartRange(java.math.BigDecimal value)
  {
    if(value == null)
    {
      setValue(STARTRANGE, "");
    }
    else
    {
      setValue(STARTRANGE, value.toString());
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static MdAttributeDecimal get(String oid)
  {
    return (MdAttributeDecimal) com.runwaysdk.business.Business.get(oid);
  }
  
  public static MdAttributeDecimal getByKey(String key)
  {
    return (MdAttributeDecimal) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static MdAttributeDecimal lock(java.lang.String oid)
  {
    MdAttributeDecimal _instance = MdAttributeDecimal.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static MdAttributeDecimal unlock(java.lang.String oid)
  {
    MdAttributeDecimal _instance = MdAttributeDecimal.get(oid);
    _instance.unlock();
    
    return _instance;
  }
  
  public String toString()
  {
    if (this.isNew())
    {
      return "New: "+ this.getClassDisplayLabel();
    }
    else
    {
      return super.toString();
    }
  }
}
