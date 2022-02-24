/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

/**
 * Constants for a URI query string
 */
public class QueryString
{
  public static final String PAGE_NUMBER = "ns-pageNumber";
  
  public static final String PAGE_SIZE = "ns-pageSize";
  
  public static final String SORT = "ns-sort";
  
  public static final String ORDER = "ns-order";
  
  public static final int DEFAULT_PAGE_NUMBER = 1;
  
  public static final int DEFAULT_PAGE_SIZE = 20;
  
  public static final String DELIM = "&";
  
  public static final String KEYVALUE = "=";
  
  /**
   * The key of the query string that denotes an empty query (no results).
   */
  public static final String EMPTY_QUERY = "ns-empty_query";
  
  /**
   * The key of the query string that denotes if this request is for
   * a new results component (e.g., ViewAllResults or NodeSearchResults).
   * The value of this key must be set to true in order to be valid.
   */
  public static final String NEW_RESULTS = "ns-new_results";
  
  /**
   * The key whose value is a groovy query string.
   */
  public static final String GROOVY = "ns-groovy";
  
  /**
   * The key whose value is a json query string
   */
  public static final String JSON_QUERY = "ns-json";
  
  /**
   * Order by ascending.
   */
  public static final String ASC                    = "asc";
  
  /**
   * Order by descending.
   */
  public static final String DESC                   = "desc";
  
  /**
   * The delimeter to use for sorting on an attribute defined
   * by an attribute struct. The internal convention to use is
   * "structName-structAttributeName".
   */
  public static final String STRUCT_SORT_DELIM = "-";
  
  public static final int STRUCT_SORT_STRUCT_INDEX = 0;
  
  public static final int STRUCT_SORT_ATTRIBUTE_INDEX = 1;
}
