package com.runwaysdk.dataaccess.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;

/**
 * This class implements the Balk pattern for instances where no graph database
 * is present.
 * 
 * @author nathan
 *
 */
public class GraphDBBalk implements GraphDB
{

  @Override
  public void initializeDB()
  {
  }

  @Override
  public void initializeConnectionPool()
  {
  }

  @Override
  public void closeConnectionPool()
  {
  }

  /**
   * @see GraphDB#getGraphDBRequest()
   */
  @Override
  public GraphRequest getGraphDBRequest()
  {
    return new GraphRequestBalk();
  }

  /**
   * @see GraphDB#createVertexClass(GraphRequest, String)
   */
  @Override
  public GraphDDLCommandAction createVertexClass(GraphRequest graphRequest, GraphRequest graphDDLRequest, String className)
  {
    return new GraphDDLCommandAction()
    {
      public void execute()
      {
      }
    };
  }

  /**
   * @see GraphDB#deleteVertexClass(GraphRequest, String)
   */
  @Override
  public GraphDDLCommandAction deleteVertexClass(GraphRequest graphRequest, GraphRequest graphDDLRequest, String className)
  {
    return new GraphDDLCommandAction()
    {
      public void execute()
      {
      }
    };
  }

  /**
   * @see GraphDB#createEdgeClass(GraphRequest, GraphRequest, String, String,
   *      String)
   */
  @Override
  public GraphDDLCommandAction createEdgeClass(GraphRequest graphRequest, GraphRequest graphDDLRequest, String edgeClass, String parentVertexClass, String childVertexClass)
  {
    return new GraphDDLCommandAction()
    {
      public void execute()
      {
      }
    };
  }

  /**
   * @see GraphDB#deleteEdgeClass(GraphRequest, String)
   */
  @Override
  public GraphDDLCommandAction deleteEdgeClass(GraphRequest graphRequest, GraphRequest graphDDLRequest, String className)
  {
    return new GraphDDLCommandAction()
    {
      public void execute()
      {
      }
    };
  }

  /**
   * @see GraphDB#isVertexClassDefined(GraphRequest, String)
   */
  @Override
  public boolean isVertexClassDefined(GraphRequest graphRequest, String className)
  {
    return false;
  }

  /**
   * @see GraphDB#isEdgeClassDefined(GraphRequest, String)
   */
  @Override
  public boolean isEdgeClassDefined(GraphRequest graphRequest, String className)
  {
    return false;
  }

  /**
   * @see GraphDB#createCharacterAttribute(GraphRequest, GraphRequest, String,
   *      String, boolean, int)
   */
  @Override
  public GraphDDLCommandAction createCharacterAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, boolean required, int maxLength)
  {
    return new GraphDDLCommandAction()
    {
      public void execute()
      {
      }
    };
  }

  /**
   * @see GraphDB#modifiyCharacterAttributeLength(GraphRequest, GraphRequest,
   *      String, String, newMaxLength)
   */
  @Override
  public GraphDDLCommandAction modifiyCharacterAttributeLength(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, int newMaxLength)
  {
    return new GraphDDLCommandAction()
    {
      public void execute()
      {
      }
    };
  }

  /**
   * @see GraphDB#getCharacterAttributeMaxLength(GraphRequest, String, String)
   */
  @Override
  public int getCharacterAttributeMaxLength(GraphRequest graphRequest, String className, String attributeName)
  {
    return 0;
  }

  /**
   * @see GraphDB#modifiyAttributeRequired(GraphRequest, GraphRequest, String,
   *      String, boolean)
   */
  @Override
  public GraphDDLCommandAction modifiyAttributeRequired(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, boolean required)
  {
    return new GraphDDLCommandAction()
    {
      public void execute()
      {
      }
    };
  }

  /**
   * @see GraphDB#isAttributeRequired(GraphRequest, String, String)
   */
  @Override
  public boolean isAttributeRequired(GraphRequest graphRequest, String className, String attributeName)
  {
    return false;
  }

  /**
   * @see GraphDB#dropAttribute(GraphRequest, GraphRequest, String, String)
   */
  @Override
  public GraphDDLCommandAction dropAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName)
  {
    return new GraphDDLCommandAction()
    {
      public void execute()
      {
      }
    };
  }

  /**
   * @see GraphDB#isClassAttributeDefined(GraphRequest, String, String)
   */
  @Override
  public boolean isClassAttributeDefined(GraphRequest graphDBRequest, String className, String attributeName)
  {
    return false;
  }

  /**
   * @see GraphDB#modifiyAttributeIndex(GraphRequest, GraphRequest, String,
   *      String, IndexTypes)
   */
  @Override
  public GraphDDLCommandAction modifiyAttributeIndex(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, IndexTypes indexType)
  {
    return new GraphDDLCommandAction()
    {
      public void execute()
      {
      }
    };
  }

  /**
   * @see GraphDB#getIndexType(GraphRequest, String, String)
   */
  @Override
  public IndexTypes getIndexType(GraphRequest graphRequest, String className, String attributeName)
  {
    return IndexTypes.NO_INDEX;
  }

  /**
   * @see GraphDB#getIndexName(GraphRequest, String, String)
   */
  @Override
  public String getIndexName(GraphRequest graphRequest, String className, String attributeName)
  {
    return null;
  }

  @Override
  public String getDbColumnType(MdAttributeConcreteDAOIF mdAttribute)
  {
    return "String";
  }

  @Override
  public GraphDDLCommandAction createConcreteAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String columnType, boolean required)
  {
    return new GraphDDLCommandAction()
    {
      public void execute()
      {
      }
    };
  }

  @Override
  public GraphDDLCommandAction createGeometryAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String geometryType, boolean required)
  {
    return new GraphDDLCommandAction()
    {
      public void execute()
      {
      }
    };
  }

  @Override
  public void insert(GraphRequest graphRequest, GraphObjectDAO graphObjectDAO)
  {
  }

  @Override
  public void update(GraphRequest graphRequest, GraphObjectDAO graphObjectDAO)
  {
  }

  @Override
  public void delete(GraphRequest graphRequest, GraphObjectDAO graphObjectDAO)
  {
  }

  @Override
  public VertexObjectDAOIF get(GraphRequest graphRequest, MdVertexDAOIF mdVertexDAOIF, String oid)
  {
    return null;
  }

  @Override
  public void addEdge(GraphRequest request, VertexObjectDAOIF parent, VertexObjectDAOIF child, MdEdgeDAOIF mdEdge)
  {
  }

  @Override
  public void removeEdge(GraphRequest request, VertexObjectDAOIF parent, VertexObjectDAOIF child, MdEdgeDAOIF mdEdge)
  {
  }

  @Override
  public List<VertexObjectDAOIF> getChildren(GraphRequest request, VertexObjectDAOIF vertexDAO, MdEdgeDAOIF mdEdge)
  {
    return new LinkedList<VertexObjectDAOIF>();
  }

  @Override
  public List<VertexObjectDAOIF> getParents(GraphRequest request, VertexObjectDAOIF vertexDAO, MdEdgeDAOIF mdEdge)
  {
    return new LinkedList<VertexObjectDAOIF>();
  }

  @Override
  public List<VertexObjectDAOIF> query(GraphRequest request, MdVertexDAOIF mdVertex, String statement, Map<String, Object> parameters)
  {
    return new LinkedList<VertexObjectDAOIF>();
  }
}
