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

@com.runwaysdk.business.ClassSignature(hash = 150937595)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to CompositeFieldCondition.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class CompositeFieldConditionBase extends com.runwaysdk.system.metadata.FieldCondition
{
  public final static String CLASS = "com.runwaysdk.system.metadata.CompositeFieldCondition";
  public final static java.lang.String FIRSTCONDITION = "firstCondition";
  public final static java.lang.String SECONDCONDITION = "secondCondition";
  private static final long serialVersionUID = 150937595;
  
  public CompositeFieldConditionBase()
  {
    super();
  }
  
  public com.runwaysdk.system.metadata.FieldCondition getFirstCondition()
  {
    if (getValue(FIRSTCONDITION).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.FieldCondition.get(getValue(FIRSTCONDITION));
    }
  }
  
  public String getFirstConditionId()
  {
    return getValue(FIRSTCONDITION);
  }
  
  public void validateFirstCondition()
  {
    this.validateAttribute(FIRSTCONDITION);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getFirstConditionMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.CompositeFieldCondition.CLASS);
    return mdClassIF.definesAttribute(FIRSTCONDITION);
  }
  
  public void setFirstCondition(com.runwaysdk.system.metadata.FieldCondition value)
  {
    if(value == null)
    {
      setValue(FIRSTCONDITION, "");
    }
    else
    {
      setValue(FIRSTCONDITION, value.getOid());
    }
  }
  
  public com.runwaysdk.system.metadata.FieldCondition getSecondCondition()
  {
    if (getValue(SECONDCONDITION).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.FieldCondition.get(getValue(SECONDCONDITION));
    }
  }
  
  public String getSecondConditionId()
  {
    return getValue(SECONDCONDITION);
  }
  
  public void validateSecondCondition()
  {
    this.validateAttribute(SECONDCONDITION);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getSecondConditionMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.CompositeFieldCondition.CLASS);
    return mdClassIF.definesAttribute(SECONDCONDITION);
  }
  
  public void setSecondCondition(com.runwaysdk.system.metadata.FieldCondition value)
  {
    if(value == null)
    {
      setValue(SECONDCONDITION, "");
    }
    else
    {
      setValue(SECONDCONDITION, value.getOid());
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static CompositeFieldCondition get(String oid)
  {
    return (CompositeFieldCondition) com.runwaysdk.business.Business.get(oid);
  }
  
  public static CompositeFieldCondition getByKey(String key)
  {
    return (CompositeFieldCondition) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static CompositeFieldCondition lock(java.lang.String oid)
  {
    CompositeFieldCondition _instance = CompositeFieldCondition.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static CompositeFieldCondition unlock(java.lang.String oid)
  {
    CompositeFieldCondition _instance = CompositeFieldCondition.get(oid);
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
