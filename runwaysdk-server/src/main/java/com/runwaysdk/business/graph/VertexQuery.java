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
package com.runwaysdk.business.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.VertexObjectDAOIF;

public class VertexQuery<T>
{
  private String              statement;

  private Map<String, Object> parameters;

  /**
   * @param statement
   */
  public VertexQuery(String statement)
  {
    super();
    this.statement = statement;
    this.parameters = new TreeMap<String, Object>();
  }

  /**
   * @param statement
   * @param parameters
   */
  public VertexQuery(String statement, Map<String, Object> parameters)
  {
    super();
    this.statement = statement;
    this.parameters = parameters;
  }

  public void setParameter(String name, Object value)
  {
    this.parameters.put(name, value);
  }

  public T getSingleResult()
  {
    List<T> results = this.getResults();

    if (results.size() == 0)
    {
      return null;
    }
    else if (results.size() == 1)
    {
      return results.get(0);
    }
    else
    {
      throw new ProgrammingErrorException("Multiple results were returned when only one is allowed");
    }
  }

  @SuppressWarnings("unchecked")
  public List<T> getResults()
  {
    GraphDBService service = GraphDBService.getInstance();
    GraphRequest request = service.getGraphDBRequest();

    List<VertexObjectDAOIF> results = service.query(request, statement, parameters);

    LinkedList<T> list = new LinkedList<T>();
    for (VertexObjectDAOIF result : results)
    {
      list.add((T) VertexObject.instantiate((VertexObjectDAO) result));
    }

    return list;
  }
}