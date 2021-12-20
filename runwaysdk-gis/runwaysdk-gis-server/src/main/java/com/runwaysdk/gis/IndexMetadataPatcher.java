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
package com.runwaysdk.gis;

import com.runwaysdk.dataaccess.MigrationUtil;
import com.runwaysdk.dataaccess.database.Database;

/**
 * This class can be used to patch the indexing metadata into a live environment.
 * 
 * @author rrowlands
 */
public class IndexMetadataPatcher
{
  public static void main(String[] args)
  {
    MigrationUtil.setReferenceAttributeDefaultIndexes();
    
    Database.parseAndExecute("update metadata set key_name='com.runwaysdk.system.gis.metadata.MdAttributeGeometry' where key_name='com.runwaysdk.system.metadata.MdAttributeGeometry'");
    Database.parseAndExecute("update metadata set key_name='com.runwaysdk.system.gis.metadata.MdAttributePoint' where key_name='com.runwaysdk.system.metadata.MdAttributePoint'");
    Database.parseAndExecute("update metadata set key_name='com.runwaysdk.system.gis.metadata.MdAttributeMultiPoint' where key_name='com.runwaysdk.system.metadata.MdAttributeMultiPoint'");
    Database.parseAndExecute("update metadata set key_name='com.runwaysdk.system.gis.metadata.MdAttributeMultiLineString' where key_name='com.runwaysdk.system.metadata.MdAttributeMultiLineString'");
    Database.parseAndExecute("update metadata set key_name='com.runwaysdk.system.gis.metadata.MdAttributeMultiPolygon' where key_name='com.runwaysdk.system.metadata.MdAttributeMultiPolygon'");
    Database.parseAndExecute("update metadata set key_name='com.runwaysdk.system.gis.metadata.MdAttributeLineString' where key_name='com.runwaysdk.system.metadata.MdAttributeLineString'");
    Database.parseAndExecute("update metadata set key_name='com.runwaysdk.system.gis.metadata.MdAttributePolygon' where key_name='com.runwaysdk.system.metadata.MdAttributePolygon'");
  }
}
