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
package com.runwaysdk.dataaccess.graph;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.RunwayException;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;

public interface GraphDB
{
  public void initializeDB();

  public void initializeConnectionPool();

  public void closeConnectionPool();

  public GraphRequest getGraphDBRequest();

  /**
   * Creates a vertex class in the Graph Database. The convention is that the
   * name of the class is the same as the name of the table in the relational
   * database.
   * 
   * @param graphRequest
   *          The DDL {@link GraphRequest}.
   * @param graphDDLRequest
   *          The DDL {@link GraphRequest}.
   * @param className
   *          The name of the table in the graph database that should match the
   *          table name in the relational database.
   * @param superClassName
   *          TODO
   * @return {@link GraphDDLCommandAction} so that it can be executed at the
   *         proper time within the transaction.
   */
  public GraphDDLCommandAction createVertexClass(GraphRequest graphRequest, GraphRequest graphDDLRequest, String className, String superClassName);

  /**
   * Deletes a vertex class in the Graph Database. The convention is that the
   * name of the class is the same as the name of the table in the relational
   * database.
   * 
   * @param graphRequest
   *          The DDL {@link GraphRequest}.
   * @param graphDDLRequest
   *          The DDL {@link GraphRequest}.
   * @param graphDBRequest
   *          The DDL {@link GraphRequest}.
   * @param className
   *          The name of the table in the graph database that should match the
   *          table name in the relational database.
   * @return {@link GraphDDLCommandAction} so that it can be executed at the
   *         proper time within the transaction.
   */
  public GraphDDLCommandAction deleteVertexClass(GraphRequest graphRequest, GraphRequest graphDDLRequest, String className);

  /**
   * Creates an edge class in the Graph Database. The convention is that the
   * name of the class is the same as the name of the table in the relational
   * database.
   * 
   * @param graphRequest
   *          The DDL {@link GraphRequest}.
   * @param graphDDLRequest
   *          The DDL {@link GraphRequest}.
   * @param edgeClass
   *          The name of the table in the graph database that should match the
   *          table name in the relational database.
   * @param parentVertexClass
   *          The name of the table in the graph database of the parent vertex
   *          class
   * @param childVertexClass
   *          The name of the table in the graph database of the child vertex.
   * @return {@link GraphDDLCommandAction} so that it can be closed or committed
   *         in the command object.
   */
  public GraphDDLCommandAction createEdgeClass(GraphRequest graphRequest, GraphRequest graphDDLRequest, String edgeClass, String parentVertexClass, String childVertexClass);

  /**
   * Deletes a class in the Graph Database. The convention is that the name of
   * the class is the same as the name of the table in the relational database.
   * 
   * @param graphRequest
   *          The DDL {@link GraphRequest}.
   * @param graphDDLRequest
   *          The DDL {@link GraphRequest}.
   * @param graphDBRequest
   *          The DDL {@link GraphRequest}.
   * @param className
   *          The name of the table in the graph database that should match the
   *          table name in the relational database.
   * @return {@link GraphDDLCommandAction} so that it can be closed or committed
   *         in the command object.
   */
  public GraphDDLCommandAction deleteEdgeClass(GraphRequest graphRequest, GraphRequest graphDDLRequest, String className);

  /**
   * Returns true if the vertex class has been defined in the graph database,
   * false otherwise.
   * 
   * @param graphRequest
   * @param className
   *          The name of the table in the graph database.
   * @return true if the vertex class has been defined in the graph database,
   *         false otherwise.
   */
  public boolean isVertexClassDefined(GraphRequest graphRequest, String className);

  /**
   * Returns true if the edge class has been defined in the graph database,
   * false otherwise.
   * 
   * @param graphRequest
   * @param className
   *          The name of the table in the graph database.
   * @return true if the edge class has been defined in the graph database,
   *         false otherwise.
   */
  public boolean isEdgeClassDefined(GraphRequest graphRequest, String className);

  /**
   * Adds a character attribute of the given name.
   * 
   * @param graphRequest
   * @param ddlGraphDBRequest
   * @param className
   * @param attributeName
   * @param required
   *          true if required, false otherwise.
   * @param maxLength
   * @param cot
   *          TODO
   * @return {@link GraphDDLCommandAction} so that it can be closed or committed
   *         in the command object.
   */
  public GraphDDLCommandAction createCharacterAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, boolean required, int maxLength, boolean cot);

  /**
   * Modifies the maximum length of the character attribute.
   * 
   * @param graphRequest
   * @param ddlGraphDBRequest
   * @param className
   * @param attributeName
   * @param newMaxLength
   *          new max length
   * @return {@link GraphDDLCommandAction} so that it can be closed or committed
   *         in the command object.
   */
  public GraphDDLCommandAction modifiyCharacterAttributeLength(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, int newMaxLength);

  /**
   * Returns the maximum length allowed for the character attribute, or 0 if the
   * attribute does not exist.
   * 
   * @param graphRequest
   * @param className
   * @param attributeName
   * @return maximum length allowed for the character attribute, or 0 if the
   *         attribute does not exist.
   */
  public int getCharacterAttributeMaxLength(GraphRequest graphRequest, String className, String attributeName);

  /**
   * Changes the required property on the attribute definition.
   * 
   * @param graphRequest
   * @param ddlGraphDBRequest
   * @param className
   * @param attributeName
   * @param required
   * @return {@link GraphDDLCommandAction} so that it can be closed or committed
   *         in the command object.
   */
  public GraphDDLCommandAction modifiyAttributeRequired(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, boolean required);

  /**
   * Returns true if the attribute is required, false otherwise.
   * 
   * @param graphRequest
   * @param className
   * @param attributeName
   * @return Returns true if the attribute is required, false otherwise.
   */
  public boolean isAttributeRequired(GraphRequest graphRequest, String className, String attributeName);

  public boolean isIndexDefined(GraphRequest graphRequest, String className, String indexName);

  /**
   * Drop the attribute from the class.
   * 
   * @param graphRequest
   * @param ddlGraphDBRequest
   * @param className
   * @param attributeName
   * @return {@link GraphDDLCommandAction} so that it can be closed or committed
   *         in the command object.
   */
  public GraphDDLCommandAction dropAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName);

  public GraphDDLCommandAction dropGeometryAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName);

  /**
   * Returns true if the attribute is defined on the class, false otherwise.
   * 
   * @param graphDBRequest
   * @param className
   * @param attributeName
   * @return Returns true if the attribute is defined on the class, false
   *         otherwise.
   */
  public boolean isClassAttributeDefined(GraphRequest graphDBRequest, String className, String attributeName);

  /**
   * Modifies an index to the given attribute.
   * 
   * @param graphRequest
   * @param ddlGraphDBRequest
   * @param className
   * @param attributeName
   * @param indexType
   * @return {@link GraphRequest} so that it can be closed or committed in the
   *         command object.
   */
  public GraphDDLCommandAction modifiyAttributeIndex(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, IndexTypes indexType);

  /**
   * Returns the type of index {@link IndexTypes} defined on the given
   * attribute.
   * 
   * @param graphRequest
   * @param className
   * @param attributeName
   * @return he type of index {@link IndexTypes} defined on the given attribute.
   */
  public IndexTypes getIndexType(GraphRequest graphRequest, String className, String attributeName);

  /**
   * Returns the name of the index, or null if none exists.
   * 
   * @param graphRequest
   * @param className
   * @param attributeName
   * @return name of the index, or null if none exists.
   */
  public String getIndexName(GraphRequest graphRequest, String className, String attributeName);

  /**
   * @param mdAttribute
   * 
   * @return Name of the database column type
   */
  public String getDbColumnType(MdAttributeConcreteDAOIF mdAttribute);

  /**
   * Adds an attribute of the given name and type.
   * 
   * @param graphRequest
   * @param ddlGraphDBRequest
   * @param className
   * @param attributeName
   * @param columnType
   * @param required
   * @param cot
   *          TODO
   * @return
   */
  public GraphDDLCommandAction createConcreteAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String columnType, boolean required, boolean cot);

  /**
   * Adds an embedded attribute of the given name and type.
   * 
   * @param graphRequest
   * @param ddlGraphDBRequest
   * @param className
   * @param attributeName
   * @param geometryType
   * @param required
   * @param cot
   *          TODO
   * @return
   */
  public GraphDDLCommandAction createEmbeddedAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String geometryType, boolean required, boolean cot);

  /**
   * Adds a geometry attribute of the given name and type.
   * 
   * @param graphRequest
   * @param ddlGraphDBRequest
   * @param className
   * @param attributeName
   * @param geometryType
   * @param required
   * @param cot
   *          TODO
   * @return
   */
  public GraphDDLCommandAction createGeometryAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String geometryType, boolean required, boolean cot);

  public GraphDDLCommandAction createSetAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String setType, boolean required, boolean cot);

  /**
   * Inserts a new object
   * 
   * @param graphRequest
   *          Request
   * @param graphObjectDAO
   *          dao
   */
  public void insert(GraphRequest graphRequest, GraphObjectDAO graphObjectDAO);

  /**
   * Update an existing dao
   * 
   * @param graphRequest
   *          Request
   * @param graphObjectDAO
   *          dao
   */
  public void update(GraphRequest graphRequest, GraphObjectDAO graphObjectDAO);

  /**
   * Delete an existing dao
   * 
   * @param graphRequest
   *          Request
   * @param graphObjectDAO
   *          dao
   */
  public void delete(GraphRequest graphRequest, GraphObjectDAO graphObjectDAO);

  public VertexObjectDAOIF get(GraphRequest graphRequest, MdVertexDAOIF mdVertexDAOIF, String oid);

  public List<VertexObjectDAOIF> getChildren(GraphRequest request, VertexObjectDAOIF vertexDAO, MdEdgeDAOIF mdEdge);

  public List<EdgeObjectDAOIF> getChildEdges(GraphRequest request, VertexObjectDAO vertexDAO, MdEdgeDAOIF mdEdge);

  public List<VertexObjectDAOIF> getParents(GraphRequest request, VertexObjectDAOIF vertexDAO, MdEdgeDAOIF mdEdge);

  public List<EdgeObjectDAOIF> getParentEdges(GraphRequest request, VertexObjectDAOIF vertexDAO, MdEdgeDAOIF mdEdge);

  public List<EdgeObjectDAOIF> getEdges(GraphRequest request, VertexObjectDAOIF parent, VertexObjectDAOIF child, MdEdgeDAOIF mdEdge);

  public List<VertexObjectDAOIF> query(GraphRequest request, String statement, Map<String, Object> parameters);

  /**
   * Transforms the given {@link RuntimeException} from the graph database and
   * transforms it into a {@link RunwayException}.
   *
   * @param locale
   * @param runEx
   * @return converted {@link RunwayException}
   */
  public RuntimeException processException(Locale locale, RuntimeException runEx);
}
