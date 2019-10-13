package com.runwaysdk.dataaccess.graph.orientdb;

import java.util.Iterator;

import com.orientechnologies.orient.core.db.ODatabasePool;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.ODatabaseType;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.index.OIndex;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLongDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.graph.GraphDB;
import com.runwaysdk.dataaccess.graph.GraphDDLCommandAction;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;

public class OrientDBImpl implements GraphDB
{

  // Graph TODO - move these into a settings file.
  public static final String   DB_NAME                = "testdb";

  private static final String  DB_SERVER_URL          = "remote:localhost";

  private static final String  DB_ROOT_USER_NAME      = "root";

  private static final String  DB_ROOT_USER_PASSWORD  = "root";

  // Graph TODO - move these into a settings file.
  private static final Integer DB_POOL_MIN            = 5;

  private static final Integer DB_POOL_MAX            = 20;

  private static final String  DB_ADMIN_USER_NAME     = "admin";

  private static final String  DB_ADMIN_USER_PASSWORD = "admin";

  private OrientDB             orientDB;

  private ODatabasePool        pool;

  public OrientDBImpl()
  {
    this.orientDB = this.getRootOrientDB();

    this.pool = null;
  }

  @Override
  public void initializeDB()
  {
    this.dropDB();
    this.createDB();
  }

  private OrientDB getRootOrientDB()
  {
    return new OrientDB(DB_SERVER_URL, DB_ROOT_USER_NAME, DB_ROOT_USER_PASSWORD, OrientDBConfig.defaultConfig());
  }

  private void dropDB()
  {
    // OrientDB orientDB = this.getRootOrientDB();
    //
    // try
    // {
    this.orientDB.drop(DB_NAME);
    System.out.println("Dropped OrientDB Database: " + DB_NAME);
    // }
    // finally
    // {
    // orientDB.close();
    // }
  }

  private void createDB()
  {
    // OrientDB orientDB = this.getRootOrientDB();
    //
    // try
    // {
    this.orientDB.create(DB_NAME, ODatabaseType.PLOCAL);
    System.out.println("Created OrientDB Database: " + DB_NAME);
    // }
    // finally
    // {
    // orientDB.close();
    // }
  }

  @Override
  public void initializeConnectionPool()
  {
    // OrientDB orientDB = this.getRootOrientDB();
    //
    // try
    // {
    // OrientDBConfigBuilder poolCfg = OrientDBConfig.builder();
    // poolCfg.addConfig(OGlobalConfiguration.DB_POOL_MIN, DB_POOL_MIN);
    // poolCfg.addConfig(OGlobalConfiguration.DB_POOL_MAX, DB_POOL_MAX);
    //
    // this.pool = new
    // ODatabasePool(this.orientDB,OrientDBService.DB_NAME,DB_ADMIN_USER_NAME,DB_ADMIN_USER_PASSWORD,
    // poolCfg.build());
    //
    // }
    // finally
    // {
    // orientDB.close();
    // }
  }

  @Override
  public void closeConnectionPool()
  {
    // this.pool.close();
    //
    // this.orientDB.close();
    System.out.println("Closing the GraphDB connection pool");
  }

  @Override
  public OrientDBRequest getGraphDBRequest()
  {
    // ODatabaseSession dbSession = this.pool.acquire();

    ODatabaseSession dbSession = orientDB.open(OrientDBImpl.DB_NAME, DB_ADMIN_USER_NAME, DB_ADMIN_USER_PASSWORD);

    return new OrientDBRequest(dbSession);
  }

  /**
   * @see GraphDB#createVertexClass(GraphRequest, GraphRequest, String)
   */
  @Override
  public GraphDDLCommandAction createVertexClass(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className)
  {
    GraphDDLCommandAction action = new GraphDDLCommandAction()
    {
      public void execute()
      {
        OrientDBRequest ddlOrientDBRequest = (OrientDBRequest) ddlGraphDBRequest;
        ODatabaseSession db = ddlOrientDBRequest.getODatabaseSession();

        // make sure the DDL graph request is current on the active thread.
        db.activateOnCurrentThread();

        OClass oClass = db.getClass(className);

        if (oClass == null)
        {
          oClass = db.createVertexClass(className);
        }

        // make sure the DML graph request is current on the active thread.
        OrientDBRequest orientDBRequest = (OrientDBRequest) graphRequest;
        db = orientDBRequest.getODatabaseSession();
        db.activateOnCurrentThread();
      }
    };

    return action;
  }

  /**
   * @see GraphDB#deleteVertexClass(GraphRequest, GraphRequest, String).
   */
  @Override
  public GraphDDLCommandAction deleteVertexClass(GraphRequest graphRequest, GraphRequest graphDDLRequest, String className)
  {
    return this.deleteClass(graphRequest, graphDDLRequest, className);
  }

  /**
   * @see GraphDB#createEdgeClass(GraphRequest, GraphRequest, String, String,
   *      String)
   */
  @Override
  public GraphDDLCommandAction createEdgeClass(GraphRequest graphRequest, GraphRequest graphDDLRequest, String edgeClass, String parentVertexClass, String childVertexClass)
  {
    GraphDDLCommandAction action = new GraphDDLCommandAction()
    {
      public void execute()
      {
        OrientDBRequest ddlOrientDDLRequest = (OrientDBRequest) graphDDLRequest;
        ODatabaseSession db = ddlOrientDDLRequest.getODatabaseSession();

        // make sure the DDL graph request is current on the active thread.
        db.activateOnCurrentThread();

        OClass oClass = db.getClass(edgeClass);

        if (oClass == null)
        {
          oClass = db.createEdgeClass(edgeClass);

          // Add constraints to parent and child vertex classes.
          // create property myE.out LINK A
          // create property myE.in LINK B
          // "SELECT FROM V WHERE name = ? and surnanme = ?"
          String statement = "CREATE PROPERTY " + edgeClass + ".OUT LINK " + parentVertexClass;
          db.command(statement);

          statement = "CREATE PROPERTY " + edgeClass + ".IN LINK " + childVertexClass;
          db.command(statement);
        }

        // make sure the DML graph request is current on the active thread.
        OrientDBRequest orientDBRequest = (OrientDBRequest) graphRequest;
        db = orientDBRequest.getODatabaseSession();
        db.activateOnCurrentThread();
      }
    };

    return action;
  }

  /**
   * @see GraphDB#deleteEdgeClass(GraphRequest, GraphRequest, String)
   */
  @Override
  public GraphDDLCommandAction deleteEdgeClass(GraphRequest graphRequest, GraphRequest graphDDLRequest, String className)
  {
    return deleteClass(graphRequest, graphDDLRequest, className);
  }

  private GraphDDLCommandAction deleteClass(GraphRequest graphRequest, GraphRequest graphDDLRequest, String className)
  {
    GraphDDLCommandAction action = new GraphDDLCommandAction()
    {
      public void execute()
      {
        OrientDBRequest ddlOrientDDLRequest = (OrientDBRequest) graphDDLRequest;
        ODatabaseSession db = ddlOrientDDLRequest.getODatabaseSession();

        // make sure the DDL graph request is current on the active thread.
        db.activateOnCurrentThread();

        db.getMetadata().getSchema().dropClass(className);

        // make sure the DML graph request is current on the active thread.
        OrientDBRequest orientDBRequest = (OrientDBRequest) graphRequest;
        db = orientDBRequest.getODatabaseSession();
        db.activateOnCurrentThread();
      }
    };

    return action;
  }

  /**
   * @see GraphDB#isVertexClassDefined(GraphRequest, String)
   */
  public boolean isVertexClassDefined(GraphRequest graphRequest, String className)
  {
    return this.isClassDefined(graphRequest, className);
  }

  /**
   * @see GraphDB#isEdgeClassDefined(GraphRequest, String)
   */
  public boolean isEdgeClassDefined(GraphRequest graphRequest, String className)
  {
    return this.isClassDefined(graphRequest, className);
  }

  private boolean isClassDefined(GraphRequest graphRequest, String className)
  {
    OrientDBRequest ddlOrientLRequest = (OrientDBRequest) graphRequest;
    ODatabaseSession db = ddlOrientLRequest.getODatabaseSession();

    OClass oClass = db.getClass(className);

    if (oClass == null)
    {
      return false;
    }
    else
    {
      return true;
    }
  }

  /**
   * @see GraphDB#createCharacterAttribute(GraphRequest, GraphRequest, String,
   *      String, boolean, int)
   */
  @Override
  public GraphDDLCommandAction createCharacterAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, boolean required, int maxLength)
  {
    return new OrientDBCreateCharacterAction(className, attributeName, OType.STRING.name(), required, maxLength);
  }

  /**
   * @see GraphDB#modifiyCharacterAttributeLength(GraphRequest, GraphRequest,
   *      String, String, newMaxLength)
   */
  @Override
  public GraphDDLCommandAction modifiyCharacterAttributeLength(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, int newMaxLength)
  {
    GraphDDLCommandAction action = new OrientDBDDLAction()
    {
      protected void executeDDL(ODatabaseSession db)
      {
        OClass oClass = db.getClass(className);
        
        if (oClass != null)
        {
          OProperty oProperty = oClass.getProperty(attributeName);
          oProperty.setMax(Integer.toString(newMaxLength));
        }
      }
    };

    return action;
  }

  @Override
  public GraphDDLCommandAction createConcreteAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String columnType, boolean required)
  {
    return new OrientDBCreatePropertyAction(className, attributeName, columnType, required);
  }

  /**
   * @see GraphDB#getCharacterAttributeMaxLength(GraphRequest, String, String)
   */
  @Override
  public int getCharacterAttributeMaxLength(GraphRequest graphRequest, String className, String attributeName)
  {
    int maxLength = 0;

    OrientDBRequest orientDBRequest = (OrientDBRequest) graphRequest;
    ODatabaseSession db = orientDBRequest.getODatabaseSession();

    OClass oClass = db.getClass(className);

    if (oClass != null)
    {
      OProperty oProperty = oClass.getProperty(attributeName);
      maxLength = new Integer(oProperty.getMax());
    }

    return maxLength;
  }

  /**
   * @see GraphDB#modifiyAttributeRequired(GraphRequest, GraphRequest, String,
   *      String, boolean)
   */
  @Override
  public GraphDDLCommandAction modifiyAttributeRequired(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, boolean required)
  {
    GraphDDLCommandAction action = new GraphDDLCommandAction()
    {
      public void execute()
      {
        OrientDBRequest ddlOrientDBRequest = (OrientDBRequest) ddlGraphDBRequest;
        ODatabaseSession db = ddlOrientDBRequest.getODatabaseSession();

        // make sure the DDL graph request is current on the active thread.
        db.activateOnCurrentThread();

        OClass oClass = db.getClass(className);

        if (oClass != null)
        {
          OProperty oProperty = oClass.getProperty(attributeName);
          oProperty.setMandatory(required);
        }

        // make sure the DML graph request is current on the active thread.
        OrientDBRequest orientDBRequest = (OrientDBRequest) graphRequest;
        db = orientDBRequest.getODatabaseSession();
        db.activateOnCurrentThread();
      }
    };

    return action;
  }

  /**
   * @see GraphDB#isAttributeRequired(GraphRequest, String, String)
   */
  @Override
  public boolean isAttributeRequired(GraphRequest graphRequest, String className, String attributeName)
  {
    boolean isRequired = false;

    OrientDBRequest orientDBRequest = (OrientDBRequest) graphRequest;
    ODatabaseSession db = orientDBRequest.getODatabaseSession();

    OClass oClass = db.getClass(className);

    if (oClass != null)
    {
      OProperty oProperty = oClass.getProperty(attributeName);
      isRequired = oProperty.isMandatory();
    }

    return isRequired;
  }

  /**
   * @see GraphDB#dropAttribute(GraphRequest, GraphRequest, String, String)
   */
  @Override
  public GraphDDLCommandAction dropAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName)
  {
    GraphDDLCommandAction action = new GraphDDLCommandAction()
    {
      public void execute()
      {
        OrientDBRequest ddlOrientDBRequest = (OrientDBRequest) ddlGraphDBRequest;
        ODatabaseSession db = ddlOrientDBRequest.getODatabaseSession();

        // make sure the DDL graph request is current on the active thread.
        db.activateOnCurrentThread();

        OClass oClass = db.getClass(className);

        if (oClass != null)
        {
          oClass.dropProperty(attributeName);
        }

        // make sure the DML graph request is current on the active thread.
        OrientDBRequest orientDBRequest = (OrientDBRequest) graphRequest;
        db = orientDBRequest.getODatabaseSession();
        db.activateOnCurrentThread();
      }
    };

    return action;
  }

  /**
   * @see GraphDB#isClassAttributeDefined(GraphRequest, String, String)
   */
  @Override
  public boolean isClassAttributeDefined(GraphRequest graphRequest, String className, String attributeName)
  {
    OrientDBRequest ddlOrientLRequest = (OrientDBRequest) graphRequest;
    ODatabaseSession db = ddlOrientLRequest.getODatabaseSession();

    OClass oClass = db.getClass(className);

    if (oClass == null)
    {
      return false;
    }
    else
    {
      OProperty oProperty = oClass.getProperty(attributeName);

      if (oProperty == null)
      {
        return false;
      }
      else
      {
        return true;
      }
    }
  }

  /**
   * @see GraphDB#modifiyAttributeIndex(GraphRequest, GraphRequest, String,
   *      String, boolean)
   */
  @Override
  public GraphDDLCommandAction modifiyAttributeIndex(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, IndexTypes indexType)
  {
    GraphDDLCommandAction action = new GraphDDLCommandAction()
    {
      public void execute()
      {
        OrientDBRequest ddlOrientDBRequest = (OrientDBRequest) ddlGraphDBRequest;
        ODatabaseSession db = ddlOrientDBRequest.getODatabaseSession();

        // make sure the DDL graph request is current on the active thread.
        db.activateOnCurrentThread();

        OClass oClass = db.getClass(className);

        if (oClass != null)
        {
          OProperty oProperty = oClass.getProperty(attributeName);

          OClass.INDEX_TYPE oClassIndexType = convertIndexType(indexType);

          if (oClassIndexType != null)
          {
            oProperty.createIndex(oClassIndexType);
          }
          // Clear the index if it is set to none.
          else
          {
            Iterator<OIndex<?>> i = oClass.getInvolvedIndexes(attributeName).iterator();

            while (i.hasNext())
            {
              OIndex<?> oIndex = i.next();

              oIndex.delete();
              // oIndex.clear();
              // oIndex.flush();
            }
          }
        }

        // make sure the DML graph request is current on the active thread.
        OrientDBRequest orientDBRequest = (OrientDBRequest) graphRequest;
        db = orientDBRequest.getODatabaseSession();
        db.activateOnCurrentThread();
      }
    };

    return action;
  }

  /**
   * @see GraphDB#getIndexType(GraphRequest, String, String)
   */
  @Override
  public IndexTypes getIndexType(GraphRequest graphRequest, String className, String attributeName)
  {

    IndexTypes returnValue = IndexTypes.NO_INDEX;

    OrientDBRequest orientDBRequest = (OrientDBRequest) graphRequest;
    ODatabaseSession db = orientDBRequest.getODatabaseSession();

    OClass oClass = db.getClass(className);

    if (oClass != null)
    {
      Iterator<OIndex<?>> i = oClass.getInvolvedIndexes(attributeName).iterator();

      while (i.hasNext())
      {
        OIndex<?> oIndex = i.next();
        if (oIndex.isUnique())
        {
          returnValue = IndexTypes.UNIQUE_INDEX;
        }
        else
        {
          returnValue = IndexTypes.NON_UNIQUE_INDEX;
        }
      }
    }
    return returnValue;
  }

  /**
   * @see GraphDB#getIndexType(GraphRequest, String, String)
   */
  @Override
  public String getIndexName(GraphRequest graphRequest, String className, String attributeName)
  {
    String returnValue = null;

    OrientDBRequest orientDBRequest = (OrientDBRequest) graphRequest;
    ODatabaseSession db = orientDBRequest.getODatabaseSession();

    OClass oClass = db.getClass(className);

    if (oClass != null)
    {
      Iterator<OIndex<?>> i = oClass.getInvolvedIndexes(attributeName).iterator();

      while (i.hasNext())
      {
        OIndex<?> oIndex = i.next();
        returnValue = oIndex.getName();
      }
    }
    return returnValue;
  }

  private OClass.INDEX_TYPE convertIndexType(IndexTypes indexType)
  {
    if (indexType.equals(IndexTypes.UNIQUE_INDEX))
    {
      return OClass.INDEX_TYPE.UNIQUE;
    }
    else if (indexType.equals(IndexTypes.NON_UNIQUE_INDEX))
    {
      return OClass.INDEX_TYPE.NOTUNIQUE;
    }
    else
    {
      return null;
    }
  }

  @Override
  public String getDbColumnType(MdAttributeConcreteDAO mdAttribute)
  {
    if (mdAttribute instanceof MdAttributeIntegerDAOIF)
    {
      return OType.INTEGER.name();
    }
    else if (mdAttribute instanceof MdAttributeLongDAOIF)
    {
      return OType.LONG.name();
    }

    throw new ProgrammingErrorException("Unknown column type for MdAttribute [" + mdAttribute.getType() + "]");
  }

  // private IndexTypes convertIndexType(OClass.INDEX_TYPE indexType)
  // {
  // if (indexType.equals(OClass.INDEX_TYPE.UNIQUE))
  // {
  // return IndexTypes.UNIQUE_INDEX;
  // }
  // else if (indexType.equals(OClass.INDEX_TYPE.NOTUNIQUE))
  // {
  // return IndexTypes.NON_UNIQUE_INDEX;
  // }
  // else
  // {
  // return IndexTypes.NO_INDEX;
  // }
  // }
}
