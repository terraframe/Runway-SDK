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
package com.runwaysdk.dataaccess.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.DeleteContext;

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

  @Override
  public void close()
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
  public GraphDDLCommandAction createVertexClass(GraphRequest graphRequest, GraphRequest graphDDLRequest, String className, String superClassName)
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

  @Override
  public boolean isIndexDefined(GraphRequest graphRequest, String className, String indexName)
  {
    return false;
  }

  /**
   * @see GraphDB#createCharacterAttribute(GraphRequest, GraphRequest, String,
   *      String, boolean, int, boolean)
   */
  @Override
  public GraphDDLCommandAction createCharacterAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, boolean required, int maxLength, boolean cot)
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
   * @see GraphDB#dropAttribute(GraphRequest, GraphRequest, String, String,
   *      boolean, DeleteContext)
   */
  @Override
  public GraphDDLCommandAction dropAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, boolean cot, DeleteContext context)
  {
    return new GraphDDLCommandAction()
    {
      public void execute()
      {
      }
    };
  }

  @Override
  public GraphDDLCommandAction dropGeometryAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, boolean cot, DeleteContext context)
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
  public GraphDDLCommandAction createConcreteAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String columnType, boolean required, boolean cot)
  {
    return new GraphDDLCommandAction()
    {
      public void execute()
      {
      }
    };
  }

  @Override
  public GraphDDLCommandAction createSetAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String setType, boolean required, boolean cot)
  {
    return new GraphDDLCommandAction()
    {
      public void execute()
      {
      }
    };
  }

  public GraphDDLCommandAction createEmbeddedAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String geometryType, boolean required, boolean cot)
  {
    return new GraphDDLCommandAction()
    {
      public void execute()
      {
      }
    };
  }
  
  @Override
  public GraphDDLCommandAction createGraphReferenceAttribute(GraphRequest graphRequest, GraphRequest graphDDLRequest, String dbClassName, String dbAttrName, String linkClassType, boolean required, boolean changeOverTime)
  {
    return new GraphDDLCommandAction()
    {
      public void execute()
      {
      }
    };
  }


  @Override
  public GraphDDLCommandAction createGeometryAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String geometryType, boolean required, boolean cot)
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
  public List<VertexObjectDAOIF> getChildren(GraphRequest request, VertexObjectDAOIF vertexDAO, MdEdgeDAOIF mdEdge)
  {
    return new LinkedList<VertexObjectDAOIF>();
  }

  @Override
  public List<EdgeObjectDAOIF> getChildEdges(GraphRequest request, VertexObjectDAO vertexDAO, MdEdgeDAOIF mdEdge)
  {
    return new LinkedList<EdgeObjectDAOIF>();
  }

  @Override
  public List<VertexObjectDAOIF> getParents(GraphRequest request, VertexObjectDAOIF vertexDAO, MdEdgeDAOIF mdEdge)
  {
    return new LinkedList<VertexObjectDAOIF>();
  }

  @Override
  public List<EdgeObjectDAOIF> getParentEdges(GraphRequest request, VertexObjectDAOIF vertexDAO, MdEdgeDAOIF mdEdge)
  {
    return new LinkedList<EdgeObjectDAOIF>();
  }

  @Override
  public List<EdgeObjectDAOIF> getEdges(GraphRequest request, VertexObjectDAOIF parent, VertexObjectDAOIF child, MdEdgeDAOIF mdEdge)
  {
    return new LinkedList<EdgeObjectDAOIF>();
  }

  @Override
  public List<Object> query(GraphRequest request, String statement, Map<String, Object> parameters)
  {
    return new LinkedList<Object>();
  }

  @Override
  public void command(GraphRequest request, String statement, Map<String, Object> parameters)
  {
  }

  @Override
  public GraphDDLCommandAction ddlCommand(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String statement, Map<String, Object> parameters)
  {
    return new GraphDDLCommandAction()
    {
      public void execute()
      {
      }
    };
  }

  /**
   * @see GraphDB#processException(Locale, RuntimeException)
   */
  public RuntimeException processException(Locale locale, RuntimeException runEx)
  {
    return new ProgrammingErrorException("Balk exception");
  }
}
