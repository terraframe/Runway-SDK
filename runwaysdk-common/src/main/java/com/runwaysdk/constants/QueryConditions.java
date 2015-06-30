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

public class QueryConditions {

	  /**
	   * A wildcard to use for comparing char attributes.
	   */
	  public static final String WILDCARD               = "%";

	  /**
	   * An equals condition.
	   */
	  public static final String EQUALS                 = "EQ";

	  /**
	   * An equals condition that ignores case.
	   */
	  public static final String EQUALS_IGNORE_CASE     = "EQi";

	  /**
	   * A not equals condition.
	   */
	  public static final String NOT_EQUALS             = "NE";

	  /**
	   * A not equals condition that ignores case.
	   */
	  public static final String NOT_EQUALS_IGNORE_CASE = "NEi";

	  /**
	   * A like condition.
	   */
	  public static final String LIKE                   = "LIKE";

	   /**
       * A like condition that ignores case
       */
      public static final String LIKE_IGNORE_CASE      = "LIKEi";

      /**
       * Not like
       */
      public static final String NOT_LIKE               = "NLIKE";

      /**
       * Not like ignores case
       */
      public static final String NOT_LIKE_IGNORE_CASE   = "NLIKEi";

      public static final String IN                     = "IN";

      public static final String IN_IGNORES_CASE        = "INi";

      public static final String NOT_IN                 = "NI";

      public static final String NOT_IN_IGNORES_CASE    = "NIi";

	  /**
	   * A less than condition.
	   */
	  public static final String LT                     = "LT";

	  /**
	   * A greater than condition.
	   */
	  public static final String GT                     = "GT";

	  /**
	   * A less than or equal to condition.
	   */
	  public static final String LT_EQ                  = "LE";

	  /**
	   * A greater than or equal to condition.
	   */
	  public static final String GT_EQ                  = "GE";

      /**
       * An enumeration contains all condition.
       */
      public static final String CONTAINS_ALL           = "containsAll";

      /**
       * An enumeration not contains all condition.
       */
      public static final String NOT_CONTAINS_ALL       = "notContainsAll";

      /**
       * An enumeration contains any condition.
       */
      public static final String CONTAINS_ANY           = "containsAny";

      /**
       * An enumeration not contains any condition.
       */
      public static final String NOT_CONTAINS_ANY       = "notContainsAny";

      /**
       * An enumeration contains exactly condition.
       */
      public static final String CONTAINS_EXACTLY        = "containsExactly";
}
