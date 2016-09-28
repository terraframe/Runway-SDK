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

@com.runwaysdk.business.ClassSignature(hash = 2143264018)
public abstract class GeoEntityProblemViewDTOBase extends com.runwaysdk.business.ViewDTO
{
  public final static String CLASS = "com.runwaysdk.system.gis.geo.GeoEntityProblemView";
  private static final long serialVersionUID = 2143264018;
  
  protected GeoEntityProblemViewDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String CONCRETEID = "concreteId";
  public static java.lang.String GEOID = "geoId";
  public static java.lang.String ID = "id";
  public static java.lang.String PROBLEM = "problem";
  public static java.lang.String PROBLEMNAME = "problemName";
  public String getConcreteId()
  {
    return getValue(CONCRETEID);
  }
  
  public void setConcreteId(String value)
  {
    if(value == null)
    {
      setValue(CONCRETEID, "");
    }
    else
    {
      setValue(CONCRETEID, value);
    }
  }
  
  public boolean isConcreteIdWritable()
  {
    return isWritable(CONCRETEID);
  }
  
  public boolean isConcreteIdReadable()
  {
    return isReadable(CONCRETEID);
  }
  
  public boolean isConcreteIdModified()
  {
    return isModified(CONCRETEID);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getConcreteIdMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(CONCRETEID).getAttributeMdDTO();
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
  
  public String getProblem()
  {
    return getValue(PROBLEM);
  }
  
  public void setProblem(String value)
  {
    if(value == null)
    {
      setValue(PROBLEM, "");
    }
    else
    {
      setValue(PROBLEM, value);
    }
  }
  
  public boolean isProblemWritable()
  {
    return isWritable(PROBLEM);
  }
  
  public boolean isProblemReadable()
  {
    return isReadable(PROBLEM);
  }
  
  public boolean isProblemModified()
  {
    return isModified(PROBLEM);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getProblemMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(PROBLEM).getAttributeMdDTO();
  }
  
  public String getProblemName()
  {
    return getValue(PROBLEMNAME);
  }
  
  public void setProblemName(String value)
  {
    if(value == null)
    {
      setValue(PROBLEMNAME, "");
    }
    else
    {
      setValue(PROBLEMNAME, value);
    }
  }
  
  public boolean isProblemNameWritable()
  {
    return isWritable(PROBLEMNAME);
  }
  
  public boolean isProblemNameReadable()
  {
    return isReadable(PROBLEMNAME);
  }
  
  public boolean isProblemNameModified()
  {
    return isModified(PROBLEMNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getProblemNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(PROBLEMNAME).getAttributeMdDTO();
  }
  
  public static GeoEntityProblemViewDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.ViewDTO dto = (com.runwaysdk.business.ViewDTO)clientRequest.get(id);
    
    return (GeoEntityProblemViewDTO) dto;
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
    getRequest().delete(this.getId());
  }
  
}
