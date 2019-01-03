/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

@com.runwaysdk.business.ClassSignature(hash = 1335009393)
public abstract class GeoEntityViewDTOBase extends com.runwaysdk.business.ViewDTO
{
  public final static String CLASS = "com.runwaysdk.system.gis.geo.GeoEntityView";
  private static final long serialVersionUID = 1335009393;
  
  protected GeoEntityViewDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String CANCREATECHILDREN = "canCreateChildren";
  public static java.lang.String GEOENTITYDISPLAYLABEL = "geoEntityDisplayLabel";
  public static java.lang.String GEOENTITYID = "geoEntityId";
  public static java.lang.String OID = "oid";
  public static java.lang.String RELATIONSHIPID = "relationshipId";
  public static java.lang.String RELATIONSHIPTYPE = "relationshipType";
  public static java.lang.String UNIVERSALDISPLAYLABEL = "universalDisplayLabel";
  public Boolean getCanCreateChildren()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(CANCREATECHILDREN));
  }
  
  public void setCanCreateChildren(Boolean value)
  {
    if(value == null)
    {
      setValue(CANCREATECHILDREN, "");
    }
    else
    {
      setValue(CANCREATECHILDREN, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isCanCreateChildrenWritable()
  {
    return isWritable(CANCREATECHILDREN);
  }
  
  public boolean isCanCreateChildrenReadable()
  {
    return isReadable(CANCREATECHILDREN);
  }
  
  public boolean isCanCreateChildrenModified()
  {
    return isModified(CANCREATECHILDREN);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getCanCreateChildrenMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(CANCREATECHILDREN).getAttributeMdDTO();
  }
  
  public String getGeoEntityDisplayLabel()
  {
    return getValue(GEOENTITYDISPLAYLABEL);
  }
  
  public void setGeoEntityDisplayLabel(String value)
  {
    if(value == null)
    {
      setValue(GEOENTITYDISPLAYLABEL, "");
    }
    else
    {
      setValue(GEOENTITYDISPLAYLABEL, value);
    }
  }
  
  public boolean isGeoEntityDisplayLabelWritable()
  {
    return isWritable(GEOENTITYDISPLAYLABEL);
  }
  
  public boolean isGeoEntityDisplayLabelReadable()
  {
    return isReadable(GEOENTITYDISPLAYLABEL);
  }
  
  public boolean isGeoEntityDisplayLabelModified()
  {
    return isModified(GEOENTITYDISPLAYLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getGeoEntityDisplayLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(GEOENTITYDISPLAYLABEL).getAttributeMdDTO();
  }
  
  public String getGeoEntityId()
  {
    return getValue(GEOENTITYID);
  }
  
  public void setGeoEntityId(String value)
  {
    if(value == null)
    {
      setValue(GEOENTITYID, "");
    }
    else
    {
      setValue(GEOENTITYID, value);
    }
  }
  
  public boolean isGeoEntityIdWritable()
  {
    return isWritable(GEOENTITYID);
  }
  
  public boolean isGeoEntityIdReadable()
  {
    return isReadable(GEOENTITYID);
  }
  
  public boolean isGeoEntityIdModified()
  {
    return isModified(GEOENTITYID);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getGeoEntityIdMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(GEOENTITYID).getAttributeMdDTO();
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
  
  public String getRelationshipType()
  {
    return getValue(RELATIONSHIPTYPE);
  }
  
  public void setRelationshipType(String value)
  {
    if(value == null)
    {
      setValue(RELATIONSHIPTYPE, "");
    }
    else
    {
      setValue(RELATIONSHIPTYPE, value);
    }
  }
  
  public boolean isRelationshipTypeWritable()
  {
    return isWritable(RELATIONSHIPTYPE);
  }
  
  public boolean isRelationshipTypeReadable()
  {
    return isReadable(RELATIONSHIPTYPE);
  }
  
  public boolean isRelationshipTypeModified()
  {
    return isModified(RELATIONSHIPTYPE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getRelationshipTypeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(RELATIONSHIPTYPE).getAttributeMdDTO();
  }
  
  public String getUniversalDisplayLabel()
  {
    return getValue(UNIVERSALDISPLAYLABEL);
  }
  
  public void setUniversalDisplayLabel(String value)
  {
    if(value == null)
    {
      setValue(UNIVERSALDISPLAYLABEL, "");
    }
    else
    {
      setValue(UNIVERSALDISPLAYLABEL, value);
    }
  }
  
  public boolean isUniversalDisplayLabelWritable()
  {
    return isWritable(UNIVERSALDISPLAYLABEL);
  }
  
  public boolean isUniversalDisplayLabelReadable()
  {
    return isReadable(UNIVERSALDISPLAYLABEL);
  }
  
  public boolean isUniversalDisplayLabelModified()
  {
    return isModified(UNIVERSALDISPLAYLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getUniversalDisplayLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(UNIVERSALDISPLAYLABEL).getAttributeMdDTO();
  }
  
  public static GeoEntityViewDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.ViewDTO dto = (com.runwaysdk.business.ViewDTO)clientRequest.get(oid);
    
    return (GeoEntityViewDTO) dto;
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
