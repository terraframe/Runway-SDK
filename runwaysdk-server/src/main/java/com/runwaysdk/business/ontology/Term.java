/**
 * 
 */
package com.runwaysdk.business.ontology;

import java.util.List;

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
public interface Term
{

//  public boolean isDirectAncestorOf(Term child);
//  public boolean isRecursiveAncestorOf(Term child);
//
//  public boolean isDirectDescendentOf(Term parent);
//  public boolean isRecursiveDescendentOf(Term parent);
  
  /**
   * Returns the unique id of this term.
   * @return
   */
  public String getId();
  
  /**
   * Returns all direct ancestor terms.
   * 
   * @return
   */
  public List<Term> getDirectAncestors();
  
  /**
   * Returns all direct descendant terms.
   * 
   * @return
   */
  public List<Term> getDirectDescendants();
  
  /**
   * Returns all ancestor terms, recursively.
   * 
   * @return
   */
  public List<Term> getAllAncestors();
  
  /**
   * Returns all descendant terms, recursively.
   * @return
   */
  public List<Term> getAllDescendants();
  
  
}
