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
package com.runwaysdk.dataaccess.schemamanager.model;

public interface SchemaObserver
{
  /**
   * Called to send notification to this observer about the creation of a class
   * @param mdClass
   */
  void notifyClassCreated(SchemaClass mdClass);
  
  /**
   * Called to send notification to this observer about the creation of a relationship
   * @param mdRelationship
   */
  void notifyRelationshipCreated(SchemaRelationship mdRelationship);
  
  
 
  boolean isAcceptable(SchemaClass schemaClass);
  
  /**
   * A method to specify which relationships the observer is interested in
   * @param relationship
   * @return
   */
  boolean isAcceptable (SchemaRelationship relationship);
}
