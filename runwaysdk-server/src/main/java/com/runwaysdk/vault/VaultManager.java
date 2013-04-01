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

/**
 * Singleton class which manages Vault objects.  Determines which
 * Vault a VaultFile is assigned to based upon the VaultStrategy
 * in use.
 * 
 * @author Justin Smethie
 */
public class VaultManager
{
  /**
   * Single vault manager
   */
  private static VaultManager manager;
  
  /**
   * The strategy of the vault manager.  By
   * default the VaultManager uses CounterStrategy.
   */
  private VaultStrategy strategy;
  
  private VaultManager()
  {
    strategy = new CounterStrategy();
  }
  
  /**
   * Returns the next Vault to use based upon its VaultStrategy.
   * 
   * @return
   */
  private VaultDAOIF getNextVault()
  {
    return strategy.getNextVault();
  }
  
  /**
   * Set the VaultStrategy the VaultManager uses.
   * 
   * @param strategy The new VaultStrategy which the VaultManager should use
   */
  private void setVaultStrategy(VaultStrategy strategy)
  {
    this.strategy = strategy;
  }
    
  /**
   * Returns the VaultStrategy that the VaultManager is 
   * currently using.  The scope of the method is package
   * level and should be used for testing only.
   * 
   * @return
   */
  VaultStrategy getStrategy()
  {
    return strategy;
  }
  
  /**
   * Returns the singleton instance of the VaultManager
   * @return
   */
  public static VaultManager instance()
  {
    if(manager == null)
    {
      manager = new VaultManager();
    }
    
    return manager;
  }
  
  /**
   * Returns the next Vault to use based upon its VaultStrategy.
   * 
   * @return
   */
  public static VaultDAOIF nextVault()
  {
    return instance().getNextVault();
  }
  
  /**
   * Set the VaultStrategy the VaultManager uses.
   * 
   * @param strategy The new VaultStrategy which the VaultManager should use
   */
  public static void setStrategy(VaultStrategy strategy)
  {
    instance().setVaultStrategy(strategy);
  }  
}
