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

@com.runwaysdk.business.ClassSignature(hash = 1004051346)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to GeoEntityProblemView.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class GeoEntityProblemViewBase extends com.runwaysdk.business.View
{
  public final static String CLASS = "com.runwaysdk.system.gis.geo.GeoEntityProblemView";
  public final static java.lang.String CONCRETEID = "concreteId";
  public final static java.lang.String GEOID = "geoId";
  public final static java.lang.String OID = "oid";
  public final static java.lang.String PROBLEM = "problem";
  public final static java.lang.String PROBLEMNAME = "problemName";
  private static final long serialVersionUID = 1004051346;
  
  public GeoEntityProblemViewBase()
  {
    super();
  }
  
  public String getConcreteId()
  {
    return getValue(CONCRETEID);
  }
  
  public void validateConcreteId()
  {
    this.validateAttribute(CONCRETEID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getConcreteIdMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.gis.geo.GeoEntityProblemView.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(CONCRETEID);
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
  
  public String getGeoId()
  {
    return getValue(GEOID);
  }
  
  public void validateGeoId()
  {
    this.validateAttribute(GEOID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getGeoIdMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.gis.geo.GeoEntityProblemView.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(GEOID);
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
  
  public String getOid()
  {
    return getValue(OID);
  }
  
  public void validateId()
  {
    this.validateAttribute(OID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getOidMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.gis.geo.GeoEntityProblemView.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  public String getProblem()
  {
    return getValue(PROBLEM);
  }
  
  public void validateProblem()
  {
    this.validateAttribute(PROBLEM);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getProblemMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.gis.geo.GeoEntityProblemView.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(PROBLEM);
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
  
  public String getProblemName()
  {
    return getValue(PROBLEMNAME);
  }
  
  public void validateProblemName()
  {
    this.validateAttribute(PROBLEMNAME);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getProblemNameMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.gis.geo.GeoEntityProblemView.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(PROBLEMNAME);
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
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static GeoEntityProblemView get(String oid)
  {
    return (GeoEntityProblemView) com.runwaysdk.business.View.get(oid);
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
