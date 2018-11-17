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

@com.runwaysdk.business.ClassSignature(hash = 2071386451)
public abstract class GeoEntityDTOBase extends com.runwaysdk.business.ontology.TermDTO
{
  public final static String CLASS = "com.runwaysdk.system.gis.geo.GeoEntity";
  private static final long serialVersionUID = 2071386451;
  
  protected GeoEntityDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected GeoEntityDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String CREATEDATE = "createDate";
  public static java.lang.String CREATEDBY = "createdBy";
  public static java.lang.String DISPLAYLABEL = "displayLabel";
  public static java.lang.String ENTITYDOMAIN = "entityDomain";
  public static java.lang.String GEOID = "geoId";
  public static java.lang.String GEOLINE = "geoLine";
  public static java.lang.String GEOMULTILINE = "geoMultiLine";
  public static java.lang.String GEOMULTIPOINT = "geoMultiPoint";
  public static java.lang.String GEOMULTIPOLYGON = "geoMultiPolygon";
  public static java.lang.String GEOPOINT = "geoPoint";
  public static java.lang.String GEOPOLYGON = "geoPolygon";
  public static java.lang.String KEYNAME = "keyName";
  public static java.lang.String LASTUPDATEDATE = "lastUpdateDate";
  public static java.lang.String LASTUPDATEDBY = "lastUpdatedBy";
  public static java.lang.String LOCKEDBY = "lockedBy";
  public static java.lang.String OID = "oid";
  public static java.lang.String OWNER = "owner";
  public static java.lang.String SEQ = "seq";
  public static java.lang.String SITEMASTER = "siteMaster";
  public static java.lang.String TYPE = "type";
  public static java.lang.String UNIVERSAL = "universal";
  public static java.lang.String WKT = "wkt";
  public java.util.Date getCreateDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(CREATEDATE));
  }
  
  public boolean isCreateDateWritable()
  {
    return isWritable(CREATEDATE);
  }
  
  public boolean isCreateDateReadable()
  {
    return isReadable(CREATEDATE);
  }
  
  public boolean isCreateDateModified()
  {
    return isModified(CREATEDATE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO getCreateDateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO) getAttributeDTO(CREATEDATE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.SingleActorDTO getCreatedBy()
  {
    if(getValue(CREATEDBY) == null || getValue(CREATEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.SingleActorDTO.get(getRequest(), getValue(CREATEDBY));
    }
  }
  
  public String getCreatedByOid()
  {
    return getValue(CREATEDBY);
  }
  
  public boolean isCreatedByWritable()
  {
    return isWritable(CREATEDBY);
  }
  
  public boolean isCreatedByReadable()
  {
    return isReadable(CREATEDBY);
  }
  
  public boolean isCreatedByModified()
  {
    return isModified(CREATEDBY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getCreatedByMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(CREATEDBY).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.gis.geo.GeoEntityDisplayLabelDTO getDisplayLabel()
  {
    return (com.runwaysdk.system.gis.geo.GeoEntityDisplayLabelDTO) this.getAttributeStructDTO(DISPLAYLABEL).getStructDTO();
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
  
  public final com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO getDisplayLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO) getAttributeDTO(DISPLAYLABEL).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdDomainDTO getEntityDomain()
  {
    if(getValue(ENTITYDOMAIN) == null || getValue(ENTITYDOMAIN).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdDomainDTO.get(getRequest(), getValue(ENTITYDOMAIN));
    }
  }
  
  public String getEntityDomainOid()
  {
    return getValue(ENTITYDOMAIN);
  }
  
  public void setEntityDomain(com.runwaysdk.system.metadata.MdDomainDTO value)
  {
    if(value == null)
    {
      setValue(ENTITYDOMAIN, "");
    }
    else
    {
      setValue(ENTITYDOMAIN, value.getOid());
    }
  }
  
  public boolean isEntityDomainWritable()
  {
    return isWritable(ENTITYDOMAIN);
  }
  
  public boolean isEntityDomainReadable()
  {
    return isReadable(ENTITYDOMAIN);
  }
  
  public boolean isEntityDomainModified()
  {
    return isModified(ENTITYDOMAIN);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getEntityDomainMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(ENTITYDOMAIN).getAttributeMdDTO();
  }
  
  public String getGeoId()
  {
    return getValue(GEOID);
  }
  
  public void setGeoId(String value)
  {
    if(value == null)
    {
      setValue(GEOID, "");
    }
    else
    {
      setValue(GEOID, value);
    }
  }
  
  public boolean isGeoIdWritable()
  {
    return isWritable(GEOID);
  }
  
  public boolean isGeoIdReadable()
  {
    return isReadable(GEOID);
  }
  
  public boolean isGeoIdModified()
  {
    return isModified(GEOID);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getGeoIdMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(GEOID).getAttributeMdDTO();
  }
  
  public com.vividsolutions.jts.geom.LineString getGeoLine()
  {
    return (com.vividsolutions.jts.geom.LineString)getObjectValue(GEOLINE);
  }
  
  public void setGeoLine(com.vividsolutions.jts.geom.LineString value)
  {
    if(value == null)
    {
      setValue(GEOLINE, "");
    }
    else
    {
      setValue(GEOLINE, value);
    }
  }
  
  public boolean isGeoLineWritable()
  {
    return isWritable(GEOLINE);
  }
  
  public boolean isGeoLineReadable()
  {
    return isReadable(GEOLINE);
  }
  
  public boolean isGeoLineModified()
  {
    return isModified(GEOLINE);
  }
  
  public final com.runwaysdk.gis.transport.metadata.AttributeLineStringMdDTO getGeoLineMd()
  {
    return (com.runwaysdk.gis.transport.metadata.AttributeLineStringMdDTO) getAttributeDTO(GEOLINE).getAttributeMdDTO();
  }
  
  public com.vividsolutions.jts.geom.MultiLineString getGeoMultiLine()
  {
    return (com.vividsolutions.jts.geom.MultiLineString)getObjectValue(GEOMULTILINE);
  }
  
  public void setGeoMultiLine(com.vividsolutions.jts.geom.MultiLineString value)
  {
    if(value == null)
    {
      setValue(GEOMULTILINE, "");
    }
    else
    {
      setValue(GEOMULTILINE, value);
    }
  }
  
  public boolean isGeoMultiLineWritable()
  {
    return isWritable(GEOMULTILINE);
  }
  
  public boolean isGeoMultiLineReadable()
  {
    return isReadable(GEOMULTILINE);
  }
  
  public boolean isGeoMultiLineModified()
  {
    return isModified(GEOMULTILINE);
  }
  
  public final com.runwaysdk.gis.transport.metadata.AttributeMultiLineStringMdDTO getGeoMultiLineMd()
  {
    return (com.runwaysdk.gis.transport.metadata.AttributeMultiLineStringMdDTO) getAttributeDTO(GEOMULTILINE).getAttributeMdDTO();
  }
  
  public com.vividsolutions.jts.geom.MultiPoint getGeoMultiPoint()
  {
    return (com.vividsolutions.jts.geom.MultiPoint)getObjectValue(GEOMULTIPOINT);
  }
  
  public void setGeoMultiPoint(com.vividsolutions.jts.geom.MultiPoint value)
  {
    if(value == null)
    {
      setValue(GEOMULTIPOINT, "");
    }
    else
    {
      setValue(GEOMULTIPOINT, value);
    }
  }
  
  public boolean isGeoMultiPointWritable()
  {
    return isWritable(GEOMULTIPOINT);
  }
  
  public boolean isGeoMultiPointReadable()
  {
    return isReadable(GEOMULTIPOINT);
  }
  
  public boolean isGeoMultiPointModified()
  {
    return isModified(GEOMULTIPOINT);
  }
  
  public final com.runwaysdk.gis.transport.metadata.AttributeMultiPointMdDTO getGeoMultiPointMd()
  {
    return (com.runwaysdk.gis.transport.metadata.AttributeMultiPointMdDTO) getAttributeDTO(GEOMULTIPOINT).getAttributeMdDTO();
  }
  
  public com.vividsolutions.jts.geom.MultiPolygon getGeoMultiPolygon()
  {
    return (com.vividsolutions.jts.geom.MultiPolygon)getObjectValue(GEOMULTIPOLYGON);
  }
  
  public void setGeoMultiPolygon(com.vividsolutions.jts.geom.MultiPolygon value)
  {
    if(value == null)
    {
      setValue(GEOMULTIPOLYGON, "");
    }
    else
    {
      setValue(GEOMULTIPOLYGON, value);
    }
  }
  
  public boolean isGeoMultiPolygonWritable()
  {
    return isWritable(GEOMULTIPOLYGON);
  }
  
  public boolean isGeoMultiPolygonReadable()
  {
    return isReadable(GEOMULTIPOLYGON);
  }
  
  public boolean isGeoMultiPolygonModified()
  {
    return isModified(GEOMULTIPOLYGON);
  }
  
  public final com.runwaysdk.gis.transport.metadata.AttributeMultiPolygonMdDTO getGeoMultiPolygonMd()
  {
    return (com.runwaysdk.gis.transport.metadata.AttributeMultiPolygonMdDTO) getAttributeDTO(GEOMULTIPOLYGON).getAttributeMdDTO();
  }
  
  public com.vividsolutions.jts.geom.Point getGeoPoint()
  {
    return (com.vividsolutions.jts.geom.Point)getObjectValue(GEOPOINT);
  }
  
  public void setGeoPoint(com.vividsolutions.jts.geom.Point value)
  {
    if(value == null)
    {
      setValue(GEOPOINT, "");
    }
    else
    {
      setValue(GEOPOINT, value);
    }
  }
  
  public boolean isGeoPointWritable()
  {
    return isWritable(GEOPOINT);
  }
  
  public boolean isGeoPointReadable()
  {
    return isReadable(GEOPOINT);
  }
  
  public boolean isGeoPointModified()
  {
    return isModified(GEOPOINT);
  }
  
  public final com.runwaysdk.gis.transport.metadata.AttributePointMdDTO getGeoPointMd()
  {
    return (com.runwaysdk.gis.transport.metadata.AttributePointMdDTO) getAttributeDTO(GEOPOINT).getAttributeMdDTO();
  }
  
  public com.vividsolutions.jts.geom.Polygon getGeoPolygon()
  {
    return (com.vividsolutions.jts.geom.Polygon)getObjectValue(GEOPOLYGON);
  }
  
  public void setGeoPolygon(com.vividsolutions.jts.geom.Polygon value)
  {
    if(value == null)
    {
      setValue(GEOPOLYGON, "");
    }
    else
    {
      setValue(GEOPOLYGON, value);
    }
  }
  
  public boolean isGeoPolygonWritable()
  {
    return isWritable(GEOPOLYGON);
  }
  
  public boolean isGeoPolygonReadable()
  {
    return isReadable(GEOPOLYGON);
  }
  
  public boolean isGeoPolygonModified()
  {
    return isModified(GEOPOLYGON);
  }
  
  public final com.runwaysdk.gis.transport.metadata.AttributePolygonMdDTO getGeoPolygonMd()
  {
    return (com.runwaysdk.gis.transport.metadata.AttributePolygonMdDTO) getAttributeDTO(GEOPOLYGON).getAttributeMdDTO();
  }
  
  public String getKeyName()
  {
    return getValue(KEYNAME);
  }
  
  public void setKeyName(String value)
  {
    if(value == null)
    {
      setValue(KEYNAME, "");
    }
    else
    {
      setValue(KEYNAME, value);
    }
  }
  
  public boolean isKeyNameWritable()
  {
    return isWritable(KEYNAME);
  }
  
  public boolean isKeyNameReadable()
  {
    return isReadable(KEYNAME);
  }
  
  public boolean isKeyNameModified()
  {
    return isModified(KEYNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getKeyNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(KEYNAME).getAttributeMdDTO();
  }
  
  public java.util.Date getLastUpdateDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(LASTUPDATEDATE));
  }
  
  public boolean isLastUpdateDateWritable()
  {
    return isWritable(LASTUPDATEDATE);
  }
  
  public boolean isLastUpdateDateReadable()
  {
    return isReadable(LASTUPDATEDATE);
  }
  
  public boolean isLastUpdateDateModified()
  {
    return isModified(LASTUPDATEDATE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO getLastUpdateDateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO) getAttributeDTO(LASTUPDATEDATE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.SingleActorDTO getLastUpdatedBy()
  {
    if(getValue(LASTUPDATEDBY) == null || getValue(LASTUPDATEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.SingleActorDTO.get(getRequest(), getValue(LASTUPDATEDBY));
    }
  }
  
  public String getLastUpdatedByOid()
  {
    return getValue(LASTUPDATEDBY);
  }
  
  public boolean isLastUpdatedByWritable()
  {
    return isWritable(LASTUPDATEDBY);
  }
  
  public boolean isLastUpdatedByReadable()
  {
    return isReadable(LASTUPDATEDBY);
  }
  
  public boolean isLastUpdatedByModified()
  {
    return isModified(LASTUPDATEDBY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getLastUpdatedByMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(LASTUPDATEDBY).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.SingleActorDTO getLockedBy()
  {
    if(getValue(LOCKEDBY) == null || getValue(LOCKEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.SingleActorDTO.get(getRequest(), getValue(LOCKEDBY));
    }
  }
  
  public String getLockedByOid()
  {
    return getValue(LOCKEDBY);
  }
  
  public boolean isLockedByWritable()
  {
    return isWritable(LOCKEDBY);
  }
  
  public boolean isLockedByReadable()
  {
    return isReadable(LOCKEDBY);
  }
  
  public boolean isLockedByModified()
  {
    return isModified(LOCKEDBY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getLockedByMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(LOCKEDBY).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.ActorDTO getOwner()
  {
    if(getValue(OWNER) == null || getValue(OWNER).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.ActorDTO.get(getRequest(), getValue(OWNER));
    }
  }
  
  public String getOwnerOid()
  {
    return getValue(OWNER);
  }
  
  public void setOwner(com.runwaysdk.system.ActorDTO value)
  {
    if(value == null)
    {
      setValue(OWNER, "");
    }
    else
    {
      setValue(OWNER, value.getOid());
    }
  }
  
  public boolean isOwnerWritable()
  {
    return isWritable(OWNER);
  }
  
  public boolean isOwnerReadable()
  {
    return isReadable(OWNER);
  }
  
  public boolean isOwnerModified()
  {
    return isModified(OWNER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getOwnerMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(OWNER).getAttributeMdDTO();
  }
  
  public Long getSeq()
  {
    return com.runwaysdk.constants.MdAttributeLongUtil.getTypeSafeValue(getValue(SEQ));
  }
  
  public boolean isSeqWritable()
  {
    return isWritable(SEQ);
  }
  
  public boolean isSeqReadable()
  {
    return isReadable(SEQ);
  }
  
  public boolean isSeqModified()
  {
    return isModified(SEQ);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getSeqMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(SEQ).getAttributeMdDTO();
  }
  
  public String getSiteMaster()
  {
    return getValue(SITEMASTER);
  }
  
  public boolean isSiteMasterWritable()
  {
    return isWritable(SITEMASTER);
  }
  
  public boolean isSiteMasterReadable()
  {
    return isReadable(SITEMASTER);
  }
  
  public boolean isSiteMasterModified()
  {
    return isModified(SITEMASTER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getSiteMasterMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(SITEMASTER).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.gis.geo.UniversalDTO getUniversal()
  {
    if(getValue(UNIVERSAL) == null || getValue(UNIVERSAL).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.gis.geo.UniversalDTO.get(getRequest(), getValue(UNIVERSAL));
    }
  }
  
  public String getUniversalOid()
  {
    return getValue(UNIVERSAL);
  }
  
  public void setUniversal(com.runwaysdk.system.gis.geo.UniversalDTO value)
  {
    if(value == null)
    {
      setValue(UNIVERSAL, "");
    }
    else
    {
      setValue(UNIVERSAL, value.getOid());
    }
  }
  
  public boolean isUniversalWritable()
  {
    return isWritable(UNIVERSAL);
  }
  
  public boolean isUniversalReadable()
  {
    return isReadable(UNIVERSAL);
  }
  
  public boolean isUniversalModified()
  {
    return isModified(UNIVERSAL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getUniversalMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(UNIVERSAL).getAttributeMdDTO();
  }
  
  public String getWkt()
  {
    return getValue(WKT);
  }
  
  public void setWkt(String value)
  {
    if(value == null)
    {
      setValue(WKT, "");
    }
    else
    {
      setValue(WKT, value);
    }
  }
  
  public boolean isWktWritable()
  {
    return isWritable(WKT);
  }
  
  public boolean isWktReadable()
  {
    return isReadable(WKT);
  }
  
  public boolean isWktModified()
  {
    return isModified(WKT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getWktMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(WKT).getAttributeMdDTO();
  }
  
  public static final com.runwaysdk.system.gis.geo.GeoEntityViewDTO create(com.runwaysdk.constants.ClientRequestIF clientRequest, com.runwaysdk.system.gis.geo.GeoEntityDTO dto, java.lang.String parentId, java.lang.String relationshipType)
  {
    String[] _declaredTypes = new String[]{"com.runwaysdk.system.gis.geo.GeoEntity", "java.lang.String", "java.lang.String"};
    Object[] _parameters = new Object[]{dto, parentId, relationshipType};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.geo.GeoEntityDTO.CLASS, "create", _declaredTypes);
    return (com.runwaysdk.system.gis.geo.GeoEntityViewDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final com.runwaysdk.system.gis.geo.GeoEntityViewDTO[] getDirectDescendants(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String parentId, java.lang.String[] relationshipTypes, java.lang.Integer pageNum, java.lang.Integer pageSize)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "[Ljava.lang.String;", "java.lang.Integer", "java.lang.Integer"};
    Object[] _parameters = new Object[]{parentId, relationshipTypes, pageNum, pageSize};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.geo.GeoEntityDTO.CLASS, "getDirectDescendants", _declaredTypes);
    return (com.runwaysdk.system.gis.geo.GeoEntityViewDTO[]) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final com.runwaysdk.system.gis.geo.GeoEntityDTO getRoot(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.geo.GeoEntityDTO.CLASS, "getRoot", _declaredTypes);
    return (com.runwaysdk.system.gis.geo.GeoEntityDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO> getAllContains()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.gis.geo.LocatedInDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO> getAllContains(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.gis.geo.LocatedInDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.LocatedInDTO> getAllContainsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.LocatedInDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.gis.geo.LocatedInDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.LocatedInDTO> getAllContainsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.LocatedInDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.gis.geo.LocatedInDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.LocatedInDTO addContains(com.runwaysdk.system.gis.geo.GeoEntityDTO child)
  {
    return (com.runwaysdk.system.gis.geo.LocatedInDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.gis.geo.LocatedInDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.LocatedInDTO addContains(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.gis.geo.GeoEntityDTO child)
  {
    return (com.runwaysdk.system.gis.geo.LocatedInDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.gis.geo.LocatedInDTO.CLASS);
  }
  
  public void removeContains(com.runwaysdk.system.gis.geo.LocatedInDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeContains(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.geo.LocatedInDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllContains()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.gis.geo.LocatedInDTO.CLASS);
  }
  
  public static void removeAllContains(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.gis.geo.LocatedInDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymDTO> getAllSynonym()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.gis.geo.SynonymRelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymDTO> getAllSynonym(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.gis.geo.SynonymRelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymRelationshipDTO> getAllSynonymRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymRelationshipDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.gis.geo.SynonymRelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymRelationshipDTO> getAllSynonymRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymRelationshipDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.gis.geo.SynonymRelationshipDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.SynonymRelationshipDTO addSynonym(com.runwaysdk.system.gis.geo.SynonymDTO child)
  {
    return (com.runwaysdk.system.gis.geo.SynonymRelationshipDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.gis.geo.SynonymRelationshipDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.SynonymRelationshipDTO addSynonym(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.gis.geo.SynonymDTO child)
  {
    return (com.runwaysdk.system.gis.geo.SynonymRelationshipDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.gis.geo.SynonymRelationshipDTO.CLASS);
  }
  
  public void removeSynonym(com.runwaysdk.system.gis.geo.SynonymRelationshipDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeSynonym(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.geo.SynonymRelationshipDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllSynonym()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.gis.geo.SynonymRelationshipDTO.CLASS);
  }
  
  public static void removeAllSynonym(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.gis.geo.SynonymRelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeMultiTermDTO> getAllGeoEntityMultiTermAttributeRoots()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeMultiTermDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeMultiTermDTO> getAllGeoEntityMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeMultiTermDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO> getAllGeoEntityMultiTermAttributeRootsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO> getAllGeoEntityMultiTermAttributeRootsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO addGeoEntityMultiTermAttributeRoots(com.runwaysdk.system.metadata.MdAttributeMultiTermDTO parent)
  {
    return (com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO addGeoEntityMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdAttributeMultiTermDTO parent)
  {
    return (com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO.CLASS);
  }
  
  public void removeGeoEntityMultiTermAttributeRoots(com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeGeoEntityMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllGeoEntityMultiTermAttributeRoots()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO.CLASS);
  }
  
  public static void removeAllGeoEntityMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeTermDTO> getAllGeoEntityTermAttributeRoots()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeTermDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeTermDTO> getAllGeoEntityTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeTermDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO> getAllGeoEntityTermAttributeRootsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO> getAllGeoEntityTermAttributeRootsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO addGeoEntityTermAttributeRoots(com.runwaysdk.system.metadata.MdAttributeTermDTO parent)
  {
    return (com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO addGeoEntityTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdAttributeTermDTO parent)
  {
    return (com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO.CLASS);
  }
  
  public void removeGeoEntityTermAttributeRoots(com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeGeoEntityTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllGeoEntityTermAttributeRoots()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO.CLASS);
  }
  
  public static void removeAllGeoEntityTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO> getAllLocatedIn()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.gis.geo.LocatedInDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO> getAllLocatedIn(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.gis.geo.LocatedInDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.LocatedInDTO> getAllLocatedInRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.LocatedInDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.gis.geo.LocatedInDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.LocatedInDTO> getAllLocatedInRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.LocatedInDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.gis.geo.LocatedInDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.LocatedInDTO addLocatedIn(com.runwaysdk.system.gis.geo.GeoEntityDTO parent)
  {
    return (com.runwaysdk.system.gis.geo.LocatedInDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.gis.geo.LocatedInDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.LocatedInDTO addLocatedIn(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.gis.geo.GeoEntityDTO parent)
  {
    return (com.runwaysdk.system.gis.geo.LocatedInDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.gis.geo.LocatedInDTO.CLASS);
  }
  
  public void removeLocatedIn(com.runwaysdk.system.gis.geo.LocatedInDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeLocatedIn(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.geo.LocatedInDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllLocatedIn()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.gis.geo.LocatedInDTO.CLASS);
  }
  
  public static void removeAllLocatedIn(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.gis.geo.LocatedInDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.GeoEntityDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.gis.geo.GeoEntityDTO) dto;
  }
  
  public void apply()
  {
    if(isNewInstance())
    {
      getRequest().createBusiness(this);
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
  
  public static com.runwaysdk.system.gis.geo.GeoEntityQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.gis.geo.GeoEntityQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.gis.geo.GeoEntityDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.gis.geo.GeoEntityDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.geo.GeoEntityDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.gis.geo.GeoEntityDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.gis.geo.GeoEntityDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.geo.GeoEntityDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.gis.geo.GeoEntityDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
