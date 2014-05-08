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
package com.runwaysdk.vault;

import java.util.List;

import com.runwaysdk.constants.VaultInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;


public class CounterStrategy extends VaultStrategy
{  
  /** (non-Javadoc)
   * @see com.runwaysdk.vault.VaultStrategy#getNextVault()
   * Returns the vault with the minimum number of files in it
   */
  public VaultDAOIF getNextVault()
  {    
    long minCount = Long.MAX_VALUE;
    VaultDAOIF minVault = null;
    
    List<? extends EntityDAOIF> entityDAOlist = BusinessDAO.getCachedEntityDAOs(VaultInfo.CLASS);

    if(entityDAOlist.size() == 0)
    {
      String error = "No vaults have been created.";
      
      throw new VaultException(error);
    }
    
    //Find the vault with the minimum number of bytes
    for(EntityDAOIF entityDAOIF : entityDAOlist)
    {
      VaultDAOIF vault = (VaultDAOIF)entityDAOIF;      
      long byteCount = Long.parseLong(vault.getValue(VaultInfo.BYTE_COUNT));
      
      if(byteCount < minCount)
      {
        minCount = byteCount;
        minVault = vault;
      }
    }
    
    return minVault;
  }
}
