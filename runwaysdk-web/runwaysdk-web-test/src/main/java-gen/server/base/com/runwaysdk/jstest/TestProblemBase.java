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

@com.runwaysdk.business.ClassSignature(hash = 236721288)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to TestProblem.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class TestProblemBase extends com.runwaysdk.business.Problem implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.jstest.TestProblem";
  public static java.lang.String ID = "id";
  public static java.lang.String PROBLEMCHAR = "problemChar";
  public static java.lang.String PROBLEMINT = "problemInt";
  private static final long serialVersionUID = 236721288;
  
  public TestProblemBase()
  {
    super();
  }
  
  public TestProblemBase(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public String getId()
  {
    return getValue(ID);
  }
  
  public void validateId()
  {
    this.validateAttribute(ID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getIdMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.jstest.TestProblem.CLASS);
    return mdClassIF.definesAttribute(ID);
  }
  
  public String getProblemChar()
  {
    return getValue(PROBLEMCHAR);
  }
  
  public void validateProblemChar()
  {
    this.validateAttribute(PROBLEMCHAR);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getProblemCharMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.jstest.TestProblem.CLASS);
    return mdClassIF.definesAttribute(PROBLEMCHAR);
  }
  
  public void setProblemChar(String value)
  {
    if(value == null)
    {
      setValue(PROBLEMCHAR, "");
    }
    else
    {
      setValue(PROBLEMCHAR, value);
    }
  }
  
  public Integer getProblemInt()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(PROBLEMINT));
  }
  
  public void validateProblemInt()
  {
    this.validateAttribute(PROBLEMINT);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getProblemIntMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.jstest.TestProblem.CLASS);
    return mdClassIF.definesAttribute(PROBLEMINT);
  }
  
  public void setProblemInt(Integer value)
  {
    if(value == null)
    {
      setValue(PROBLEMINT, "");
    }
    else
    {
      setValue(PROBLEMINT, java.lang.Integer.toString(value));
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public java.lang.String localize(java.util.Locale locale)
  {
    java.lang.String message = super.localize(locale);
    message = replace(message, "{id}", this.getId());
    message = replace(message, "{problemChar}", this.getProblemChar());
    message = replace(message, "{problemInt}", this.getProblemInt());
    return message;
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