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
package com.runwaysdk.jstest;

@com.runwaysdk.business.ClassSignature(hash = 1237406671)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to StateEnum.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class StateEnumBase extends com.runwaysdk.system.EnumerationMaster implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.jstest.StateEnum";
  public static java.lang.String ENUMINT = "enumInt";
  public static java.lang.String STATECODE = "stateCode";
  public static java.lang.String STATENAME = "stateName";
  public static java.lang.String STATEPHONE = "statePhone";
  private com.runwaysdk.business.Struct statePhone = null;
  
  private static final long serialVersionUID = 1237406671;
  
  public StateEnumBase()
  {
    super();
    statePhone = super.getStruct("statePhone");
  }
  
  public Integer getEnumInt()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(ENUMINT));
  }
  
  public void validateEnumInt()
  {
    this.validateAttribute(ENUMINT);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getEnumIntMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.jstest.StateEnum.CLASS);
    return mdClassIF.definesAttribute(ENUMINT);
  }
  
  public void setEnumInt(Integer value)
  {
    if(value == null)
    {
      setValue(ENUMINT, "");
    }
    else
    {
      setValue(ENUMINT, java.lang.Integer.toString(value));
    }
  }
  
  public String getStateCode()
  {
    return getValue(STATECODE);
  }
  
  public void validateStateCode()
  {
    this.validateAttribute(STATECODE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getStateCodeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.jstest.StateEnum.CLASS);
    return mdClassIF.definesAttribute(STATECODE);
  }
  
  public void setStateCode(String value)
  {
    if(value == null)
    {
      setValue(STATECODE, "");
    }
    else
    {
      setValue(STATECODE, value);
    }
  }
  
  public String getStateName()
  {
    return getValue(STATENAME);
  }
  
  public void validateStateName()
  {
    this.validateAttribute(STATENAME);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getStateNameMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.jstest.StateEnum.CLASS);
    return mdClassIF.definesAttribute(STATENAME);
  }
  
  public void setStateName(String value)
  {
    if(value == null)
    {
      setValue(STATENAME, "");
    }
    else
    {
      setValue(STATENAME, value);
    }
  }
  
  public com.runwaysdk.system.PhoneNumber getStatePhone()
  {
    return (com.runwaysdk.system.PhoneNumber) statePhone;
  }
  
  public void validateStatePhone()
  {
    this.validateAttribute(STATEPHONE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getStatePhoneMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.jstest.StateEnum.CLASS);
    return mdClassIF.definesAttribute(STATEPHONE);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static StateEnumQuery getAllInstances(String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    StateEnumQuery query = new StateEnumQuery(new com.runwaysdk.query.QueryFactory());
    com.runwaysdk.business.Entity.getAllInstances(query, sortAttribute, ascending, pageSize, pageNumber);
    return query;
  }
  
  public static StateEnum get(String id)
  {
    return (StateEnum) com.runwaysdk.business.Business.get(id);
  }
  
  public static StateEnum getByKey(String key)
  {
    return (StateEnum) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static StateEnum getEnumeration(String enumName)
  {
    return (StateEnum) com.runwaysdk.business.Business.getEnumeration(com.runwaysdk.jstest.StateEnum.CLASS ,enumName);
  }
  
  public static StateEnum lock(java.lang.String id)
  {
    StateEnum _instance = StateEnum.get(id);
    _instance.lock();
    
    return _instance;
  }
  
  public static StateEnum unlock(java.lang.String id)
  {
    StateEnum _instance = StateEnum.get(id);
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