/**
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
 */
package com.runwaysdk.query;

import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.business.RelationshipQuery;
import com.runwaysdk.business.StructQuery;

public class QueryFacade
{
  /**
   * Given a GeneratedComponentQuery, this method returns the ComponentQuery that the GeneratedComponentQuery delegates to.
   *
   * @param query
   * @return
   */
  public static ComponentQuery getCompomentQuery(GeneratedComponentQuery query)
  {
    return query.getComponentQuery();
  }

  /**
   * Given a GeneratedBusinessQuery, this method returns the GeneratedBusinessQuery that the GeneratedBusinessQuery delegates to.
   *
   * @param query
   * @return
   */
  public static BusinessQuery getBusinessQuery(GeneratedBusinessQuery query)
  {
    return query.getBusinessQuery();
  }

  /**
   * Given a GeneratedRelationshipQuery, this method returns the GeneratedRelationshipQuery that the GeneratedRelationshipQuery delegates to.
   *
   * @param query
   * @return
   */
  public static RelationshipQuery getRelationshipQuery(GeneratedRelationshipQuery query)
  {
    return query.getRelationshipQuery();
  }

  /**
   * Given a GeneratedStructQuery, this method returns the GeneratedStructQuery that the GeneratedStructQuery delegates to.
   *
   * @param query
   * @return
   */
  public static StructQuery getStructQuery(GeneratedStructQuery query)
  {
    return query.getStructQuery();
  }

  /**
   * Sets the given generated entity query to being used in the given ValueQuery.
   * @param valueQuery
   * @param generatedEntityQuery
   */
  public static void setUsedInSelectClause(ValueQuery valueQuery, GeneratedEntityQuery generatedEntityQuery)
  {
    EntityQuery entityQuery = generatedEntityQuery.getComponentQuery();
    entityQuery.setUsedInValueQuery(valueQuery);
  }
}
