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
package com.runwaysdk.business.state;

import com.runwaysdk.dataaccess.MetadataDAOIF;

/**
 * @author Justin
 *
 */
public interface StateMasterDAOIF extends MetadataDAOIF
{  
  /**
   * The name corresponding to the state name attribute
   */
  public static final String STATE_NAME = "stateName";
  
  /**
   * The name of the entryState attribute on the MdState type 
   */
  public static final String ENTRY_STATE = "entryState";
    
  /**
   * The name of the transition attribute on Transitions
   */
  public static final String TRANSITION_NAME = "transitionName";
  
  /**
   * The display label of the state
   */
  public static final String DISPLAY_LABEL = "displayLabel";
  
  /**
   * Enumeration of choices for the entryState attribute
   * 
   * @author Justin Smethie
   */
  public enum Entry
  {
    /**
     * StateMasterDAO is the default entry to the MdStateMachine.  Only
     * one default StateMasterDAO is allowed for a MdStateMachine.
     */
    DEFAULT_ENTRY_STATE("0000000000000000000000000000096500000000000000000000000000000967"), 

    /**
     * StateMasterDAO is a entry point ot the MdStateMachine
     */
    ENTRY_STATE("0000000000000000000000000000096100000000000000000000000000000967"),

    /**
     * StateMachine is not a entry point to the MdStateMachine
     */
    NOT_ENTRY_STATE("0000000000000000000000000000096400000000000000000000000000000967");
    
    private String id;
    
    private Entry(String id)
    {
      this.id = id; 
    }
    
    /**
     * The id of the Entry
     * @return
     */
    public String getId()
    {
      return id;
    }
  }
  
  /**
   * Get the name of the state
   * @return The name of the state
   */
  public String getName();
  
  /**
   * Returns the fully qualified name of the state, including the type and the name of the state.
   * @return fully qualified name of the state, including the type and the name of the state.
   */
  public String getQualifiedName();
  
  /**
   * If the state is an entry state
   * 
   * @return
   */
  public boolean isEntryState();
  
  /**
   * If the state is the default entry state of the MdState 
   * 
   * @return
   */
  public boolean isDefaultState();
   
  /**
   * Get the next state as determined by a given transition
   * 
   * @param transitionName The name of the transition
   * @return Get the next state of the transition
   */
  public StateMasterDAOIF getNextState(String transitionName);
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAOIF#getBusinessDAO()
   */
  public StateMasterDAO getBusinessDAO();
}
