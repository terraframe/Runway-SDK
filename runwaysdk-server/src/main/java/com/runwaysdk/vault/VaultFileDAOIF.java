/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.vault;

import java.io.File;
import java.io.InputStream;

import com.runwaysdk.dataaccess.BusinessDAOIF;


public interface VaultFileDAOIF extends BusinessDAOIF, FileIF
{
  /**
   * Name of the table used to store intances of this class.
   */
  public static final String TABLE                     = "vault_file";
  
  /**
   * Return the extension of the VaultFile
   * @return
   */
  public String getExtension();
  
  /**
   * Return the file name of the VaultFile
   * @return
   */
  public String getFileName();
  
  /**
   * Return the file name inside of the Vault for the file 
   * @return
   */
  public String getVaultFileName();
  
  /**
   * Return the path inside of the vault for the File
   * @return
   */
  public String getVaultFilePath();
  
  /**
   * Returns the oid of the Vault which the file resides within.
   * 
   * @return oid of the Vault which the file resides within.
   */
  public String getVaultReference();
  
  /**
   * Returns a reference to the Vault which the file resides within.
   * 
   * @return reference to the Vault which the file resides within.
   */
  public VaultDAOIF getVault();
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAOIF#getBusinessDAO()
   */
  public VaultFileDAO getBusinessDAO();
  
  /**
   * @return A stream containing the contents of the vault file.
   */
  public InputStream getFileStream();
  
  /**
   * @return A File object that references the vault file.
   */
  public File getFile();
  
  /**
   * Writes an array of bytes to the vault file.
   * 
   * @param bytes The byte array to write.
   */
  public void putFile(byte[] bytes);  
  
  /**
   * Writes a stream of data to the vault file.
   * 
   * @param stream Stream of data to write
   */
  public void putFile(InputStream stream);
}
