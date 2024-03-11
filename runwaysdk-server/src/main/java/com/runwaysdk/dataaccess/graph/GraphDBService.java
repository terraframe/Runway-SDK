/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.graph;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.dataaccess.CoreException;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.DeleteContext;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.transaction.TransactionState;
import com.runwaysdk.session.RequestState;

public class GraphDBService
{
  private static GraphDBService  service;

  private ServiceLoader<GraphDB> loader;

  private GraphDB                graphDB;

  private GraphDBService()
  {
    this.loader = ServiceLoader.load(GraphDB.class);

    this.graphDB = null;

    try
    {
      Iterator<GraphDB> i = this.loader.iterator();

      while (i.hasNext())
      {
        this.graphDB = i.next();
      }
    }
    catch (ServiceConfigurationError serviceError)
    {
      throw new ProgrammingErrorException(serviceError);
    }

    if (this.graphDB == null)
    {
      this.graphDB = new GraphDBBalk();
    }
  }

  public static synchronized GraphDBService getInstance()
  {
    if (service == null)
    {
      service = new GraphDBService();
    }

    return service;
  }

  public void initializeDB()
  {
    this.graphDB.initializeDB();
  }

  public void initializeConnectionPool()
  {
    this.graphDB.initializeConnectionPool();
  }

  public void closeConnectionPool()
  {
    this.graphDB.closeConnectionPool();
  }

  public void close()
  {
    this.graphDB.close();
  }

  public GraphRequest getGraphDBRequest()
  {
    return this.getGraphDBRequest(true);
  }

  public GraphRequest getGraphDBRequest(boolean validateRequestState)
  {
    if (validateRequestState)
    {
      RequestState currentRequestState = RequestState.getCurrentRequestState();

      if (currentRequestState == null)
      {
        throw new CoreException("Request state expected.");
      }
    }

    return this.graphDB.getGraphDBRequest();
  }

  /**
   * This has a different method signature compared to
   * {@link GraphDBService#getGraphDBRequest} even though it does the same thing
   * so that aspects will not intercept this call and provide a different
   * {@link GraphRequest} object.
   * 
   * @return
   */
  public GraphRequest getDDLGraphDBRequest()
  {
    return this.getDDLGraphDBRequest(true);
  }

  /**
   * This has a different method signature compared to
   * {@link GraphDBService#getGraphDBRequest} even though it does the same thing
   * so that aspects will not intercept this call and provide a different
   * {@link GraphRequest} object. If the calling method is not validating the
   * transaction state then it is up to the calling method to ensure it is
   * either in a transaction or to close the request
   * 
   * @return
   */
  public GraphRequest getDDLGraphDBRequest(boolean validateTransactionState)
  {
    // The life cycle of DDL graph connections are managed inside the
    // transaction management aspect. As such if the system tries to get a
    // DDL graph request outside of a transaction then the graph database
    // connection will not be cleaned up. Therefore ensure that all requests
    // for a DDL graph request are inside of a transaction annotation.
    if (validateTransactionState)
    {
      TransactionState currentTransactionState = TransactionState.getCurrentTransactionState();

      if (currentTransactionState == null)
      {
        throw new CoreException("Transaction state expected.");
      }
    }

    GraphRequest ddlGraphRequest = this.graphDB.getGraphDBRequest();
    ddlGraphRequest.setIsDDLRequest();

    return ddlGraphRequest;
  }

  /**
   * @param superClassName
   *          TODO
   * @see GraphDB#createEmbeddedClass(GraphRequest, GraphRequest, String,
   *      String)
   */
  public GraphDDLCommandAction createEmbeddedClass(GraphRequest graphRequest, GraphRequest graphDBRequest, String className, String superClassName)
  {
    return this.graphDB.createEmbeddedClass(graphRequest, graphDBRequest, className, superClassName);
  }

  /**
   * @see GraphDB#deleteEmbeddedClass(GraphRequest, GraphRequest, String)
   */
  public GraphDDLCommandAction deleteEmbeddedClass(GraphRequest graphRequest, GraphRequest graphDBRequest, String className)
  {
    return this.graphDB.deleteEmbeddedClass(graphRequest, graphDBRequest, className);
  }

  /**
   * @param superClassName
   *          TODO
   * @see GraphDB#createVertexClass(GraphRequest, GraphRequest, String, String)
   */
  public GraphDDLCommandAction createVertexClass(GraphRequest graphRequest, GraphRequest graphDBRequest, String className, String superClassName)
  {
    return this.graphDB.createVertexClass(graphRequest, graphDBRequest, className, superClassName);
  }

  /**
   * @see GraphDB#deleteVertexClass(GraphRequest, GraphRequest, String)
   */
  public GraphDDLCommandAction deleteVertexClass(GraphRequest graphRequest, GraphRequest graphDBRequest, String className)
  {
    return this.graphDB.deleteVertexClass(graphRequest, graphDBRequest, className);
  }

  /**
   * @see GraphDB#createEdgeClass(GraphRequest, GraphRequest, String, String,
   *      String)
   */
  public GraphDDLCommandAction createEdgeClass(GraphRequest graphRequest, GraphRequest graphDDLRequest, String edgeClass, String superClassName, String parentVertexClass, String childVertexClass)
  {
    return this.graphDB.createEdgeClass(graphRequest, graphDDLRequest, edgeClass, superClassName, parentVertexClass, childVertexClass);
  }

  /**
   * @see GraphDB#deleteEdgeClass(GraphRequest, GraphRequest, String)
   */
  public GraphDDLCommandAction deleteEdgeClass(GraphRequest graphRequest, GraphRequest graphDBRequest, String className)
  {
    return this.graphDB.deleteEdgeClass(graphRequest, graphDBRequest, className);
  }

  /**
   * @see GraphDB#isVertexClassDefined(GraphRequest, String)
   */
  public boolean isVertexClassDefined(GraphRequest graphRequest, String className)
  {
    return this.graphDB.isVertexClassDefined(graphRequest, className);
  }

  public Boolean isIndexDefined(GraphRequest graphRequest, String className, String indexName)
  {
    return this.graphDB.isIndexDefined(graphRequest, className, indexName);
  }

  /**
   * @see GraphDB#isEdgeClassDefined(GraphRequest, String)
   */
  public boolean isEdgeClassDefined(GraphRequest graphRequest, String className)
  {
    return this.graphDB.isEdgeClassDefined(graphRequest, className);
  }

  /**
   * @see GraphDB#isClassDefined(GraphRequest, String)
   */
  public boolean isClassDefined(GraphRequest graphRequest, String className)
  {
    return this.graphDB.isClassDefined(graphRequest, className);
  }

  /**
   * @param cot
   *          TODO
   * @see GraphDB#createCharacterAttribute(GraphRequest, GraphRequest, String,
   *      String, boolean, int, boolean)
   */
  public GraphDDLCommandAction createCharacterAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, boolean required, int maxLength, boolean cot)
  {
    return this.graphDB.createCharacterAttribute(graphRequest, ddlGraphDBRequest, className, attributeName, required, maxLength, cot);
  }

  public GraphDDLCommandAction createConcreteAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String columnType, boolean required, boolean cot)
  {
    return this.graphDB.createConcreteAttribute(graphRequest, ddlGraphDBRequest, className, attributeName, columnType, required, cot);
  }

  public GraphDDLCommandAction createEmbeddedAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String embeddedClassType, boolean required, boolean cot)
  {
    return this.graphDB.createEmbeddedAttribute(graphRequest, ddlGraphDBRequest, className, attributeName, embeddedClassType, required, cot);
  }

  public GraphDDLCommandAction createGraphReferenceAttribute(GraphRequest graphRequest, GraphRequest graphDDLRequest, String dbClassName, String dbAttrName, String linkClassType, boolean required, boolean changeOverTime)
  {
    return this.graphDB.createGraphReferenceAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, linkClassType, required, changeOverTime);
  }

  public GraphDDLCommandAction createGeometryAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String geometryType, boolean required, boolean cot)
  {
    return this.graphDB.createGeometryAttribute(graphRequest, ddlGraphDBRequest, className, attributeName, geometryType, required, cot);
  }

  public GraphDDLCommandAction createSetAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String setType, boolean required, boolean cot)
  {
    return this.graphDB.createSetAttribute(graphRequest, ddlGraphDBRequest, className, attributeName, setType, required, cot);
  }

  /**
   * @see GraphDB#modifiyCharacterAttributeLength(GraphRequest, GraphRequest,
   *      String, String, newMaxLength)
   */
  public GraphDDLCommandAction modifiyCharacterAttributeLength(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, int newMaxLength)
  {
    return this.graphDB.modifiyCharacterAttributeLength(graphRequest, ddlGraphDBRequest, className, attributeName, newMaxLength);
  }

  /**
   * @see GraphDB#getCharacterAttributeMaxLength(GraphRequest, String, String)
   */
  public int getCharacterAttributeMaxLength(GraphRequest graphRequest, String className, String attributeName)
  {
    return this.graphDB.getCharacterAttributeMaxLength(graphRequest, className, attributeName);
  }

  /**
   * @see GraphDB#modifiyAttributeRequired(GraphRequest, GraphRequest, String,
   *      String, boolean)
   */
  public GraphDDLCommandAction modifiyAttributeRequired(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, boolean required)
  {
    return this.graphDB.modifiyAttributeRequired(graphRequest, ddlGraphDBRequest, className, attributeName, required);
  }

  /**
   * @see GraphDB#isAttributeRequired(GraphRequest, String, String)
   */
  public boolean isAttributeRequired(GraphRequest graphRequest, String className, String attributeName)
  {
    return this.graphDB.isAttributeRequired(graphRequest, className, attributeName);
  }

  /**
   * @param cot
   *          TODO
   * @param context
   *          TODO
   * @see GraphDB#dropAttribute(GraphRequest, GraphRequest, String, String,
   *      boolean, DeleteContext)
   */
  public GraphDDLCommandAction dropAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, boolean cot, DeleteContext context)
  {
    return this.graphDB.dropAttribute(graphRequest, ddlGraphDBRequest, className, attributeName, cot, context);
  }

  /**
   * @param cot
   *          TODO
   * @param context
   *          TODO
   * @see GraphDB#dropAttribute(GraphRequest, GraphRequest, String, String,
   *      boolean, DeleteContext)
   */
  public GraphDDLCommandAction dropGeometryAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, boolean cot, DeleteContext context)
  {
    return this.graphDB.dropGeometryAttribute(graphRequest, ddlGraphDBRequest, className, attributeName, cot, context);
  }

  /**
   * @see GraphDB#isClassAttributeDefined(GraphRequest, String, String)
   */
  public boolean isClassAttributeDefined(GraphRequest graphRequest, String className, String attributeName)
  {
    return this.graphDB.isClassAttributeDefined(graphRequest, className, attributeName);
  }

  /**
   * @see GraphDB#modifiyAttributeIndex(GraphRequest, GraphRequest, String,
   *      String, IndexTypes)
   */
  public GraphDDLCommandAction modifiyAttributeIndex(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, IndexTypes indexType)
  {
    return this.graphDB.modifiyAttributeIndex(graphRequest, ddlGraphDBRequest, className, attributeName, indexType);
  }

  /**
   * @see GraphDB#getIndexType(GraphRequest, String, String)
   */
  public IndexTypes getIndexType(GraphRequest graphRequest, String className, String attributeName)
  {
    return this.graphDB.getIndexType(graphRequest, className, attributeName);
  }

  /**
   * @see GraphDB#getIndexName(GraphRequest, String, String)
   */
  public String getIndexName(GraphRequest graphRequest, String className, String attributeName)
  {
    return this.graphDB.getIndexName(graphRequest, className, attributeName);
  }

  public String getDbColumnType(MdAttributeConcreteDAO mdAttribute)
  {
    return this.graphDB.getDbColumnType(mdAttribute);
  }

  public void insert(GraphRequest graphRequest, GraphObjectDAO graphObjectDAO)
  {
    this.graphDB.insert(graphRequest, graphObjectDAO);
  }

  public void update(GraphRequest graphRequest, GraphObjectDAO graphObjectDAO)
  {
    this.graphDB.update(graphRequest, graphObjectDAO);
  }

  public void delete(GraphRequest graphRequest, GraphObjectDAO graphObjectDAO)
  {
    this.graphDB.delete(graphRequest, graphObjectDAO);
  }

  public VertexObjectDAOIF get(GraphRequest graphRequest, MdVertexDAOIF mdVertex, String oid)
  {
    return this.graphDB.get(graphRequest, mdVertex, oid);
  }

  public List<VertexObjectDAOIF> getChildren(GraphRequest request, VertexObjectDAO vertexDAO, MdEdgeDAOIF mdEdge)
  {
    return this.graphDB.getChildren(request, vertexDAO, mdEdge);
  }

  public List<EdgeObjectDAOIF> getChildEdges(GraphRequest request, VertexObjectDAO vertexDAO, MdEdgeDAOIF mdEdge)
  {
    return this.graphDB.getChildEdges(request, vertexDAO, mdEdge);
  }

  public List<VertexObjectDAOIF> getParents(GraphRequest request, VertexObjectDAO vertexDAO, MdEdgeDAOIF mdEdge)
  {
    return this.graphDB.getParents(request, vertexDAO, mdEdge);
  }

  public List<EdgeObjectDAOIF> getParentEdges(GraphRequest request, VertexObjectDAO vertexDAO, MdEdgeDAOIF mdEdge)
  {
    return this.graphDB.getParentEdges(request, vertexDAO, mdEdge);
  }

  public List<EdgeObjectDAOIF> getEdges(GraphRequest request, VertexObjectDAOIF parent, VertexObjectDAOIF child, MdEdgeDAOIF mdEdge)
  {
    return this.graphDB.getEdges(request, parent, child, mdEdge);
  }

  public List<Object> query(GraphRequest request, String statement, Map<String, Object> parameters)
  {
    return this.graphDB.query(request, statement, parameters);
  }

  public List<Object> query(GraphRequest request, String statement, Map<String, Object> parameters, Class<?> resultType)
  {
    return this.graphDB.query(request, statement, parameters, resultType);
  }

  public void command(GraphRequest request, String statement, Map<String, Object> parameters)
  {
    this.graphDB.command(request, statement, parameters);
  }

  /**
   * @see GraphDB#processException(Locale, RuntimeException)
   */
  public RuntimeException processException(Locale locale, RuntimeException runEx)
  {
    return this.graphDB.processException(locale, runEx);
  }

  public GraphDDLCommandAction ddlCommand(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String statement, Map<String, Object> parameters)
  {
    return this.graphDB.ddlCommand(graphRequest, ddlGraphDBRequest, statement, parameters);
  }

}
