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
package com.runwaysdk.constants;

public enum GeneratedActions
{
  VIEW_ACTION("view"),
  VIEW_ALL_ACTION("viewAll"),
  NEW_INSTANCE_ACTION("newInstance"),
  NEW_RELATIONSHIP_ACTION("newRelationship"),
  VIEW_PAGE_ACTION("viewPage"),
  CREATE_ACTION("create"),
  UPDATE_ACTION("update"),
  CANCEL_ACTION("cancel"),
  DELETE_ACTION("delete"),
  EDIT_ACTION("edit"),
  PARENT_QUERY_ACTION("parentQuery"),
  CHILD_QUERY_ACTION("childQuery");

  private String name;
  
  private GeneratedActions(String name)
  {
    this.name = name;
  }
  
  public String getName()
  {
    return name;
  }
}
