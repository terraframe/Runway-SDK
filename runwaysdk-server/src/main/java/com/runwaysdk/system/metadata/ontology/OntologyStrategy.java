/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.runwaysdk.system.metadata.ontology;

import com.runwaysdk.business.ontology.OntologyStrategyIF;

/**
 * This is the abstract class that ontology strategies with state will extend.
 * The DefaultStrategy DOES NOT extend this class, so use OntologyStrategyIF to
 * refer to a generic strategy. If you are trying to create a new strategy (with
 * state) then create a new Runway class that extends this or a sibling
 * (DatabaseAllPathsStrategy).
 */
public abstract class OntologyStrategy extends OntologyStrategyBase implements OntologyStrategyIF
{
  private static final long serialVersionUID = 2061031175;

  public OntologyStrategy()
  {
    super();
  }

  /*
   * Subclasses need to super or the strategy state will not be changed!
   * 
   * @see
   * com.runwaysdk.business.ontology.OntologyStrategyIF#initialize(com.runwaysdk
   * .business.ontology.MdTermDAO)
   */
  @Override
  public void initialize(String relationshipType)
  {
    this.clearStrategyState();
    this.addStrategyState(StrategyState.INITIALIZED);
    this.apply();
  }

  /*
   * Subclasses need to super or the strategy state will not be changed!
   * 
   * @see
   * com.runwaysdk.business.ontology.OntologyStrategyIF#initialize(com.runwaysdk
   * .business.ontology.MdTermDAO)
   */
  @Override
  public void reinitialize(String relationshipType)
  {
    shutdown();
    initialize(relationshipType);
  }

  /*
   * Subclasses need to super or the strategy state will not be changed!
   * 
   * @see
   * com.runwaysdk.business.ontology.OntologyStrategyIF#initialize(com.runwaysdk
   * .business.ontology.MdTermDAO)
   */
  @Override
  public void shutdown()
  {
    this.clearStrategyState();
    this.addStrategyState(StrategyState.UNINITIALIZED);
    this.apply();
  }
}
