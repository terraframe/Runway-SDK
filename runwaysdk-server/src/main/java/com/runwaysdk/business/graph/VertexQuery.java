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
