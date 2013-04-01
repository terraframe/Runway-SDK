/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.constants;


public interface VaultFileInfo extends BusinessInfo
{
  /**
   * Class VaultFile.
   */
  public static final String CLASS = Constants.SYSTEM_PACKAGE + "." + "VaultFile";

  /**
   * The name of the extension attribute in VaultFile.
   */
  public static final String EXTENSION = "fileExtension";
  
  /**
   * The name of the file name attribute in VaultFile.
   */
  public static final String FILE_NAME = "fileName";

  /**
   * The size of the file in the VaultFile.
   */
  public static final String FILE_SIZE = "fileSize";
  
  /**
   * The name of the vault file name attribute in VaultFile.
   */
  public static final String VAULT_FILE_NAME = "vaultName";
  
  /**
   * The name of the vault file path attribute in VaultFile.
   */
  public static final String VAULT_FILE_PATH = "vaultPath";
  
  /**
   * The name of the vault reference attribute in VaultFile.
   */
  public static final String VAULT_REF = "vaultReference";
}
