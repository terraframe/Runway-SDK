/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.system.gis.geo;

@com.runwaysdk.business.ClassSignature(hash = -17242990)
public abstract class SynonymViewDTOBase extends com.runwaysdk.business.ViewDTO
{
  public final static String CLASS = "com.runwaysdk.system.gis.geo.SynonymView";
  private static final long serialVersionUID = -17242990;
  
  protected SynonymViewDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public final static java.lang.String DISPLAYLABEL = "displayLabel";
  public final static java.lang.String OID = "oid";
  public final static java.lang.String RELATIONSHIPID = "relationshipId";
  public final static java.lang.String SYNONYMID = "synonymId";
  public String getDisplayLabel()
  {
    return getValue(DISPLAYLABEL);
  }
  
  public void setDisplayLabel(String value)
  {
    if(value == null)
    {
      setValue(DISPLAYLABEL, "");
    }
    else
    {
      setValue(DISPLAYLABEL, value);
    }
  }
  
  public boolean isDisplayLabelWritable()
  {
    return isWritable(DISPLAYLABEL);
  }
  
  public boolean isDisplayLabelReadable()
  {
    return isReadable(DISPLAYLABEL);
  }
  
  public boolean isDisplayLabelModified()
  {
    return isModified(DISPLAYLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getDisplayLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(DISPLAYLABEL).getAttributeMdDTO();
  }
  
  public String getRelationshipId()
  {
    return getValue(RELATIONSHIPID);
  }
  
  public void setRelationshipId(String value)
  {
    if(value == null)
    {
      setValue(RELATIONSHIPID, "");
    }
    else
    {
      setValue(RELATIONSHIPID, value);
    }
  }
  
  public boolean isRelationshipIdWritable()
  {
    return isWritable(RELATIONSHIPID);
  }
  
  public boolean isRelationshipIdReadable()
  {
    return isReadable(RELATIONSHIPID);
  }
  
  public boolean isRelationshipIdModified()
  {
    return isModified(RELATIONSHIPID);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getRelationshipIdMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(RELATIONSHIPID).getAttributeMdDTO();
  }
  
  public String getSynonymId()
  {
    return getValue(SYNONYMID);
  }
  
  public void setSynonymId(String value)
  {
    if(value == null)
    {
      setValue(SYNONYMID, "");
    }
    else
    {
      setValue(SYNONYMID, value);
    }
  }
  
  public boolean isSynonymIdWritable()
  {
    return isWritable(SYNONYMID);
  }
  
  public boolean isSynonymIdReadable()
  {
    return isReadable(SYNONYMID);
  }
  
  public boolean isSynonymIdModified()
  {
    return isModified(SYNONYMID);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getSynonymIdMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(SYNONYMID).getAttributeMdDTO();
  }
  
  public static SynonymViewDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.ViewDTO dto = (com.runwaysdk.business.ViewDTO)clientRequest.get(oid);
    
    return (SynonymViewDTO) dto;
  }
  
  public void apply()
  {
    if(isNewInstance())
    {
      getRequest().createSessionComponent(this);
    }
    else
    {
      getRequest().update(this);
    }
  }
  public void delete()
  {
    getRequest().delete(this.getOid());
  }
  
}
