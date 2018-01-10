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
package com.runwaysdk.system;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.constants.VaultFileInfo;
import com.runwaysdk.vault.VaultFileDAO;

public class VaultFile extends VaultFileBase
{
  private static final long serialVersionUID = 1229405886296L;
  
  public VaultFile()
  {
    super();
  }
  
  /**
   * Creates and applies a new VaultFile using the given parameters. Purely a convenience method.
   * 
   * @param filename The name of the file, including extension but not the path. Example: WaterPoints.xlsx
   * @param file
   */
  public static VaultFile createAndApply(String fileName, InputStream file)
  {
    VaultFile entity = new VaultFile();
    VaultFileDAO fileDao = (VaultFileDAO) BusinessFacade.getEntityDAO(entity);

    // TODO ?
//    this.checkVaultPermissions(entity, Operation.CREATE);

    int index = fileName.lastIndexOf('.');

    String onlyName = fileName.substring(0, index);
    String extension = "xlsx";

    entity.setValue(VaultFileInfo.FILE_NAME, onlyName);
    entity.setValue(VaultFileInfo.EXTENSION, extension);

    fileDao.setSize(0);
    entity.apply();
    fileDao.putFile(file);
    
    return entity;
  }
  
}
