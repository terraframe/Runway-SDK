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
package com.runwaysdk.business.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.graph.ResultSetConverterIF;

public class GraphQuery<T>
{
  private String              statement;

  private Map<String, Object> parameters;

  private Class<?>            forceResultType;
  
  private ResultSetConverterIF converter;

  /**
   * @param statement
   */
  public GraphQuery(String statement)
  {
    this(statement, new TreeMap<String, Object>(), (ResultSetConverterIF) null);
  }

  /**
   * @param statement
   * @param parameters
   */
  public GraphQuery(String statement, Map<String, Object> parameters)
  {
    this(statement, parameters, (ResultSetConverterIF) null);
  }
  
  /**
   * @param statement
   * @param parameters
   */
  public GraphQuery(String statement, Map<String, Object> parameters, Class<?> forceResultType)
  {
    super();
    this.statement = statement;
    this.parameters = parameters;
    this.forceResultType = forceResultType;
  }
  
  public GraphQuery(String statement, Map<String, Object> parameters, ResultSetConverterIF converter)
  {
    super();
    this.statement = statement;
    this.parameters = parameters;
    this.converter = converter;
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

    List<Object> results;
    
    if (this.converter == null)
    {
      results = service.query(request, statement, parameters, this.forceResultType);
    }
    else
    {
      results = service.query(request, statement, parameters, this.converter);
    }

    LinkedList<T> list = new LinkedList<T>();

    for (Object result : results)
    {
      list.add((T) result);
    }

    return list;
  }

  @SuppressWarnings("unchecked")
  public List<T> getRawResults()
  {
    GraphDBService service = GraphDBService.getInstance();
    GraphRequest request = service.getGraphDBRequest();

    List<Object> results = service.query(request, statement, parameters);

    LinkedList<T> list = new LinkedList<T>();

    for (Object result : results)
    {
      list.add((T) result);
    }

    return list;
  }
}
