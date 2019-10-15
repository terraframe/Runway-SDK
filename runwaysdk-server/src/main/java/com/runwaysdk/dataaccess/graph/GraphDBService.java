package com.runwaysdk.dataaccess.graph;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;

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

  public GraphRequest getGraphDBRequest()
  {
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
    return this.graphDB.getGraphDBRequest();
  }

  /**
   * @see GraphDB#createVertexClass(GraphRequest, GraphRequest, String)
   */
  public GraphDDLCommandAction createVertexClass(GraphRequest graphRequest, GraphRequest graphDBRequest, String className)
  {
    return this.graphDB.createVertexClass(graphRequest, graphDBRequest, className);
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
  public GraphDDLCommandAction createEdgeClass(GraphRequest graphRequest, GraphRequest graphDDLRequest, String edgeClass, String parentVertexClass, String childVertexClass)
  {
    return this.graphDB.createEdgeClass(graphRequest, graphDDLRequest, edgeClass, parentVertexClass, childVertexClass);
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

  /**
   * @see GraphDB#isEdgeClassDefined(GraphRequest, String)
   */
  public boolean isEdgeClassDefined(GraphRequest graphRequest, String className)
  {
    return this.graphDB.isEdgeClassDefined(graphRequest, className);
  }

  /**
   * @see GraphDB#createCharacterAttribute(GraphRequest, GraphRequest, String,
   *      String, boolean, int)
   */
  public GraphDDLCommandAction createCharacterAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, boolean required, int maxLength)
  {
    return this.graphDB.createCharacterAttribute(graphRequest, ddlGraphDBRequest, className, attributeName, required, maxLength);
  }

  public GraphDDLCommandAction createConcreteAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String columnType, boolean required)
  {
    return this.graphDB.createConcreteAttribute(graphRequest, ddlGraphDBRequest, className, attributeName, columnType, required);
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
   * @see GraphDB#dropAttribute(GraphRequest, GraphRequest, String, String)
   */
  public GraphDDLCommandAction dropAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName)
  {
    return this.graphDB.dropAttribute(graphRequest, ddlGraphDBRequest, className, attributeName);
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

}
