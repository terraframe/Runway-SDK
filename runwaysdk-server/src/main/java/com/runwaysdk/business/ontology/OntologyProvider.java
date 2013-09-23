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
public interface OntologyProvider
{
  /**
   * Returns the MdBusiness domain class that defines terms.
   * 
   * @return
   */
  public MdBusiness getTermDomain();
  
  /**
   * Returns the MdRelationship that relates terms.
   * @return
   */
  public MdRelationship getTermRelationship();
  
  /**
   * Returns the MdBusiness that represents the AllPaths
   * @return
   */
  public MdBusiness getTermAllPaths();

  /**
   * Returns the name of the attribute on the term allpaths that
   * defines the term reference.
   * @return
   */
  public String getAllPathsParentTerm();

  /**
   * Returns the name of the attribute on the term allpaths that
   * defines the term's definition, which usually points to an MdBusiness.
   * @return
   */
  public String getAllPathsParentType();

  /**
   * Returns the name of the attribute on the term allpaths that
   * defines the term reference.
   * @return
   */
  public String getAllPathsChildTerm();

  /**
   * Returns the name of the attribute on the term allpaths that
   * defines the term's definition, which usually points to an MdBusiness.
   * @return
   */
  public String getAllPathsChildType();

  /**
   * Returns the name of the attribute on the term domain that
   * is referenced by getAllPathsParentType() and getAllPathsChildType().
   * @return
   */
  public String getDomainType();
  
}
