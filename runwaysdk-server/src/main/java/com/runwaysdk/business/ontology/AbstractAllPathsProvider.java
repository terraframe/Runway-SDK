package com.runwaysdk.business.ontology;

import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdRelationship;

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
public abstract class AbstractAllPathsProvider implements AllPathsProvider
{
  /**
   * The domain class that implements Term behavior.
   */
  private MdBusiness termDomain;
  
  /**
   * The relationship class that relates Terms from the termDomain class.
   */
  private MdRelationship termRelationship;
  
  /**
   * The domain class that models an allpaths behaviors that flattens the
   * termRelationship definitions.
   */
  private MdBusiness termAllPaths;
  
  /**
   * 
   * @param termDomain
   * @param termRelationship
   * @param termAllpaths
   */
  public AbstractAllPathsProvider(String termDomain, String termRelationship, String termAllpaths)
  {
    this.termDomain = MdBusiness.getMdBusiness(termDomain);
    this.termRelationship = (MdRelationship) MdRelationship.getMdElement(termRelationship);
    this.termAllPaths = MdBusiness.getMdBusiness(termAllpaths);
  }
  
  @Override
  public MdBusiness getTermDomain()
  {
    return this.termDomain;
  }
  
  @Override
  public MdRelationship getTermRelationship()
  {
    return this.termRelationship;
  }
  
  @Override
  public MdBusiness getTermAllPaths()
  {
    return this.termAllPaths;
  }
  
}
