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
package com.runwaysdk.system.web;

@com.runwaysdk.business.ClassSignature(hash = -1214448301)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to AttributeNotificationProblem.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class AttributeNotificationProblemBase extends com.runwaysdk.business.Problem
{
  public final static String CLASS = "com.runwaysdk.system.web.AttributeNotificationProblem";
  public final static java.lang.String ATTRIBUTEDISPLAYLABEL = "attributeDisplayLabel";
  public final static java.lang.String ATTRIBUTENAME = "attributeName";
  public final static java.lang.String COMPONENTID = "componentId";
  public final static java.lang.String DEFININGTYPE = "definingType";
  public final static java.lang.String DEFININGTYPEDISPLAYLABEL = "definingTypeDisplayLabel";
  public final static java.lang.String OID = "oid";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -1214448301;
  
  public AttributeNotificationProblemBase()
  {
    super();
  }
  
  public AttributeNotificationProblemBase(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public String getAttributeDisplayLabel()
  {
    return getValue(ATTRIBUTEDISPLAYLABEL);
  }
  
  public void validateAttributeDisplayLabel()
  {
    this.validateAttribute(ATTRIBUTEDISPLAYLABEL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getAttributeDisplayLabelMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.web.AttributeNotificationProblem.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(ATTRIBUTEDISPLAYLABEL);
  }
  
  public void setAttributeDisplayLabel(String value)
  {
    if(value == null)
    {
      setValue(ATTRIBUTEDISPLAYLABEL, "");
    }
    else
    {
      setValue(ATTRIBUTEDISPLAYLABEL, value);
    }
  }
  
  public String getAttributeName()
  {
    return getValue(ATTRIBUTENAME);
  }
  
  public void validateAttributeName()
  {
    this.validateAttribute(ATTRIBUTENAME);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getAttributeNameMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.web.AttributeNotificationProblem.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(ATTRIBUTENAME);
  }
  
  public void setAttributeName(String value)
  {
    if(value == null)
    {
      setValue(ATTRIBUTENAME, "");
    }
    else
    {
      setValue(ATTRIBUTENAME, value);
    }
  }
  
  public String getComponentId()
  {
    return getValue(COMPONENTID);
  }
  
  public void validateComponentId()
  {
    this.validateAttribute(COMPONENTID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getComponentIdMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.web.AttributeNotificationProblem.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(COMPONENTID);
  }
  
  public void setComponentId(String value)
  {
    if(value == null)
    {
      setValue(COMPONENTID, "");
    }
    else
    {
      setValue(COMPONENTID, value);
    }
  }
  
  public String getDefiningType()
  {
    return getValue(DEFININGTYPE);
  }
  
  public void validateDefiningType()
  {
    this.validateAttribute(DEFININGTYPE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getDefiningTypeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.web.AttributeNotificationProblem.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(DEFININGTYPE);
  }
  
  public void setDefiningType(String value)
  {
    if(value == null)
    {
      setValue(DEFININGTYPE, "");
    }
    else
    {
      setValue(DEFININGTYPE, value);
    }
  }
  
  public String getDefiningTypeDisplayLabel()
  {
    return getValue(DEFININGTYPEDISPLAYLABEL);
  }
  
  public void validateDefiningTypeDisplayLabel()
  {
    this.validateAttribute(DEFININGTYPEDISPLAYLABEL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getDefiningTypeDisplayLabelMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.web.AttributeNotificationProblem.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(DEFININGTYPEDISPLAYLABEL);
  }
  
  public void setDefiningTypeDisplayLabel(String value)
  {
    if(value == null)
    {
      setValue(DEFININGTYPEDISPLAYLABEL, "");
    }
    else
    {
      setValue(DEFININGTYPEDISPLAYLABEL, value);
    }
  }
  
  public String getOid()
  {
    return getValue(OID);
  }
  
  public void validateOid()
  {
    this.validateAttribute(OID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF getOidMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.web.AttributeNotificationProblem.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public java.lang.String localize(java.util.Locale locale)
  {
    java.lang.String message = super.localize(locale);
    message = replace(message, "{attributeDisplayLabel}", this.getAttributeDisplayLabel());
    message = replace(message, "{attributeName}", this.getAttributeName());
    message = replace(message, "{componentId}", this.getComponentId());
    message = replace(message, "{definingType}", this.getDefiningType());
    message = replace(message, "{definingTypeDisplayLabel}", this.getDefiningTypeDisplayLabel());
    message = replace(message, "{oid}", this.getOid());
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
