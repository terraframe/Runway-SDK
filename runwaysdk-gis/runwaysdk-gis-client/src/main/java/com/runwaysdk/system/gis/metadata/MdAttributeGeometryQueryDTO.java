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
package com.runwaysdk.system.gis.metadata;

@com.runwaysdk.business.ClassSignature(hash = -1208643282)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MdAttributeGeometry.java
 *
 * @author Autogenerated by RunwaySDK
 */
public class MdAttributeGeometryQueryDTO extends com.runwaysdk.system.metadata.MdAttributeConcreteQueryDTO
{
private static final long serialVersionUID = -1208643282;

  protected MdAttributeGeometryQueryDTO(String type)
  {
    super(type);
  }

@SuppressWarnings("unchecked")
public java.util.List<? extends com.runwaysdk.system.gis.metadata.MdAttributeGeometryDTO> getResultSet()
{
  return (java.util.List<? extends com.runwaysdk.system.gis.metadata.MdAttributeGeometryDTO>)super.getResultSet();
}
}