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
package com.runwaysdk.business.state;

import java.util.List;

import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdTreeDAOIF;
import com.runwaysdk.dataaccess.TransitionDAO;
import com.runwaysdk.dataaccess.TransitionDAOIF;
import com.runwaysdk.system.StateMaster;


public interface MdStateMachineDAOIF extends MdBusinessDAOIF
{
  /**
   * The table name of the MdState class
   */
  public static final String TABLE = "md_state_machine";

  /**
   * Returns a list of StateMasterIFs which are defined for this
   * MdStateMachine.
   *
   * @return
   */
  public List<StateMasterDAOIF> definesStateMasters();


  /**
   * Returns the StateMasterIF defined for this MdStateMachine
   * with the given name.
   *
   * @param stateName The name of the state.
   * @return
   */
  public StateMasterDAOIF definesStateMaster(String stateName);

  /**
   * Returns a list of transitions which this MdState defines
   * @return
   */
  public List<TransitionDAOIF> definesTransitions();

  /**
   * Returns the Transition defined on this MdStateMachine with
   * the give transition name.
   *
   * @param transitionName The name of the transition.
   * @return
   */
  public TransitionDAOIF definesTransition(String transitionName);

  /**
   * @param source The source {@link StateMaster} of the directional {@link TransitionDAO}
   *
   * @return Returns all of the {@link TransitionDAO} defined on this
   *         {@link MdStateMachineDAO} with the given source {@link StateMaster}.
   */
  public List<TransitionDAOIF> definesTransitions(StateMasterDAOIF source);


  /**
   * Return the entry state of this MdState
   *
   * @return The entry state of this MdState
   */
  public List<StateMasterDAOIF> getEntryState();

  /**
   * Get the default state of the MdState
   *
   * @return The defualt entry state of the MdState
   */
  public StateMasterDAOIF getDefaultState();

  /**
   * Get the MdBusinessIF which owns this MdState
   *
   * @return The MdBusinessIF which defines this MdState
   */
  public MdBusinessDAOIF getStateMachineOwner();

  /**
   * Returns the status relationship defined for a given state of this MdState
   *
   * @param state The state of the status relationship
   * @return The status MdRelationship defined for a given state
   */
  public MdRelationshipDAOIF getMdStatus(StateMasterDAOIF state);

  /**
   * Gets the transition relationship defined for this MdState
   *
   * @return The transition relationship defined for this MdState
   */
  public MdRelationshipDAOIF getMdTransition();

  /**
   * Returns the abstract status relationship for this MdState
   *
   * @return The abstract status relationship for this MdState
   */
  public MdTreeDAOIF getMdStatus();

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAOIF#getBusinessDAO()
   */
  public MdStateMachineDAO getBusinessDAO();
}
