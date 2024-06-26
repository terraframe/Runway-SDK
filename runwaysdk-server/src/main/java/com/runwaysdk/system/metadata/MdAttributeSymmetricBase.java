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

@com.runwaysdk.business.ClassSignature(hash = -77506889)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MdAttributeSymmetric.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class MdAttributeSymmetricBase extends com.runwaysdk.system.metadata.MdAttributeEncryption
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeSymmetric";
  public final static java.lang.String SECRETKEYSIZE = "secretKeySize";
  public final static java.lang.String SYMMETRICMETHOD = "symmetricMethod";
  private static final long serialVersionUID = -77506889;
  
  public MdAttributeSymmetricBase()
  {
    super();
  }
  
  public Integer getSecretKeySize()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(SECRETKEYSIZE));
  }
  
  public void validateSecretKeySize()
  {
    this.validateAttribute(SECRETKEYSIZE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getSecretKeySizeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdAttributeSymmetric.CLASS);
    return mdClassIF.definesAttribute(SECRETKEYSIZE);
  }
  
  public void setSecretKeySize(Integer value)
  {
    if(value == null)
    {
      setValue(SECRETKEYSIZE, "");
    }
    else
    {
      setValue(SECRETKEYSIZE, java.lang.Integer.toString(value));
    }
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.system.metadata.SymmetricOptions> getSymmetricMethod()
  {
    return (java.util.List<com.runwaysdk.system.metadata.SymmetricOptions>) getEnumValues(SYMMETRICMETHOD);
  }
  
  public void addSymmetricMethod(com.runwaysdk.system.metadata.SymmetricOptions value)
  {
    if(value != null)
    {
      addEnumItem(SYMMETRICMETHOD, value.getOid());
    }
  }
  
  public void removeSymmetricMethod(com.runwaysdk.system.metadata.SymmetricOptions value)
  {
    if(value != null)
    {
      removeEnumItem(SYMMETRICMETHOD, value.getOid());
    }
  }
  
  public void clearSymmetricMethod()
  {
    clearEnum(SYMMETRICMETHOD);
  }
  
  public void validateSymmetricMethod()
  {
    this.validateAttribute(SYMMETRICMETHOD);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getSymmetricMethodMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdAttributeSymmetric.CLASS);
    return mdClassIF.definesAttribute(SYMMETRICMETHOD);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static MdAttributeSymmetric get(String oid)
  {
    return (MdAttributeSymmetric) com.runwaysdk.business.Business.get(oid);
  }
  
  public static MdAttributeSymmetric getByKey(String key)
  {
    return (MdAttributeSymmetric) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static MdAttributeSymmetric lock(java.lang.String oid)
  {
    MdAttributeSymmetric _instance = MdAttributeSymmetric.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static MdAttributeSymmetric unlock(java.lang.String oid)
  {
    MdAttributeSymmetric _instance = MdAttributeSymmetric.get(oid);
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
