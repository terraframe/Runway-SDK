/**
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
 */
package com.runwaysdk.system.web;

@com.runwaysdk.business.ClassSignature(hash = -408192448)
public abstract class AttributeNotificationProblemDTOBase extends com.runwaysdk.business.ProblemDTO
{
  public final static String CLASS = "com.runwaysdk.system.web.AttributeNotificationProblem";
  private static final long serialVersionUID = -408192448;
  
  public AttributeNotificationProblemDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  public AttributeNotificationProblemDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF, java.util.Locale locale)
  {
    super(clientRequestIF, locale);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ATTRIBUTEDISPLAYLABEL = "attributeDisplayLabel";
  public static java.lang.String ATTRIBUTENAME = "attributeName";
  public static java.lang.String COMPONENTID = "componentId";
  public static java.lang.String DEFININGTYPE = "definingType";
  public static java.lang.String DEFININGTYPEDISPLAYLABEL = "definingTypeDisplayLabel";
  public static java.lang.String ID = "id";
  public String getAttributeDisplayLabel()
  {
    return getValue(ATTRIBUTEDISPLAYLABEL);
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
  
  public boolean isAttributeDisplayLabelWritable()
  {
    return isWritable(ATTRIBUTEDISPLAYLABEL);
  }
  
  public boolean isAttributeDisplayLabelReadable()
  {
    return isReadable(ATTRIBUTEDISPLAYLABEL);
  }
  
  public boolean isAttributeDisplayLabelModified()
  {
    return isModified(ATTRIBUTEDISPLAYLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getAttributeDisplayLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(ATTRIBUTEDISPLAYLABEL).getAttributeMdDTO();
  }
  
  public String getAttributeName()
  {
    return getValue(ATTRIBUTENAME);
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
  
  public boolean isAttributeNameWritable()
  {
    return isWritable(ATTRIBUTENAME);
  }
  
  public boolean isAttributeNameReadable()
  {
    return isReadable(ATTRIBUTENAME);
  }
  
  public boolean isAttributeNameModified()
  {
    return isModified(ATTRIBUTENAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getAttributeNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(ATTRIBUTENAME).getAttributeMdDTO();
  }
  
  public String getComponentId()
  {
    return getValue(COMPONENTID);
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
  
  public boolean isComponentIdWritable()
  {
    return isWritable(COMPONENTID);
  }
  
  public boolean isComponentIdReadable()
  {
    return isReadable(COMPONENTID);
  }
  
  public boolean isComponentIdModified()
  {
    return isModified(COMPONENTID);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getComponentIdMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(COMPONENTID).getAttributeMdDTO();
  }
  
  public String getDefiningType()
  {
    return getValue(DEFININGTYPE);
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
  
  public boolean isDefiningTypeWritable()
  {
    return isWritable(DEFININGTYPE);
  }
  
  public boolean isDefiningTypeReadable()
  {
    return isReadable(DEFININGTYPE);
  }
  
  public boolean isDefiningTypeModified()
  {
    return isModified(DEFININGTYPE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getDefiningTypeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(DEFININGTYPE).getAttributeMdDTO();
  }
  
  public String getDefiningTypeDisplayLabel()
  {
    return getValue(DEFININGTYPEDISPLAYLABEL);
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
  
  public boolean isDefiningTypeDisplayLabelWritable()
  {
    return isWritable(DEFININGTYPEDISPLAYLABEL);
  }
  
  public boolean isDefiningTypeDisplayLabelReadable()
  {
    return isReadable(DEFININGTYPEDISPLAYLABEL);
  }
  
  public boolean isDefiningTypeDisplayLabelModified()
  {
    return isModified(DEFININGTYPEDISPLAYLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getDefiningTypeDisplayLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(DEFININGTYPEDISPLAYLABEL).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{attributeDisplayLabel}", this.getAttributeDisplayLabel().toString());
    template = template.replace("{attributeName}", this.getAttributeName().toString());
    template = template.replace("{componentId}", this.getComponentId().toString());
    template = template.replace("{definingType}", this.getDefiningType().toString());
    template = template.replace("{definingTypeDisplayLabel}", this.getDefiningTypeDisplayLabel().toString());
    template = template.replace("{id}", this.getId().toString());
    
    return template;
  }
  
}
