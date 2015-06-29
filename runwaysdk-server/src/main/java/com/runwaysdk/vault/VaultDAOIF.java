/**
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
 */
package com.runwaysdk.vault;

import com.runwaysdk.dataaccess.BusinessDAOIF;


public interface VaultDAOIF extends BusinessDAOIF
{
  /**
   * Returns the root path of the vault
   * @return The root path of the vault
   */
  public String getVaultPath();
  
  /**
   * Increments the byte count attribute by the given amount
   * and applies the changes to the database.  However, this
   * method balks if the Vault is in the middle of being deleted.
   * 
   * @param byteCount Amount to increment the byte count
   */
  public void incrementByteCount(long byteCount);
}
