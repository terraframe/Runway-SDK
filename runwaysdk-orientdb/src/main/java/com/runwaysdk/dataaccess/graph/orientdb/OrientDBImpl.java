package com.runwaysdk.dataaccess.graph.orientdb;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.locationtech.spatial4j.shape.Shape;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.ODatabaseType;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.index.OIndex;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OClass.INDEX_TYPE;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.metadata.sequence.OSequence;
import com.orientechnologies.orient.core.metadata.sequence.OSequence.SEQUENCE_TYPE;
import com.orientechnologies.orient.core.metadata.sequence.OSequenceLibrary;
import com.orientechnologies.orient.core.record.ODirection;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.record.ORecord;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import com.orientechnologies.orient.core.storage.ORecordDuplicatedException;
import com.orientechnologies.spatial.shape.OShapeFactory;
import com.runwaysdk.constants.BusinessInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeEmbeddedInfo;
import com.runwaysdk.constants.graph.MdEdgeInfo;
import com.runwaysdk.constants.graph.MdGraphClassInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEmbeddedDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFloatDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLongDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTextDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.StaleEntityException;
import com.runwaysdk.dataaccess.graph.GraphDB;
import com.runwaysdk.dataaccess.graph.GraphDDLCommandAction;
import com.runwaysdk.dataaccess.graph.GraphObjectDAO;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.VertexObjectDAOIF;
import com.runwaysdk.dataaccess.graph.attributes.Attribute;
import com.runwaysdk.dataaccess.graph.attributes.AttributeEmbedded;
import com.runwaysdk.dataaccess.graph.attributes.AttributeEnumeration;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTime;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEmbeddedDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdGraphClassDAO;
import com.runwaysdk.gis.dataaccess.MdAttributeGeometryDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeLineStringDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiLineStringDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPointDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPolygonDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributePointDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributePolygonDAOIF;
import com.vividsolutions.jts.geom.Geometry;

public class OrientDBImpl implements GraphDB
{
  private OrientDB orientDB;

  // private ODatabasePool pool;

  public OrientDBImpl()
  {
    this.orientDB = this.getRootOrientDB();

    // this.pool = null;
  }

  @Override
  public void initializeDB()
  {
    this.dropDB();
    this.createDB();
  }

  private OrientDB getRootOrientDB()
  {
    return new OrientDB(OrientDBProperties.getUrl(), OrientDBProperties.getRootUserName(), OrientDBProperties.getRootUserPassword(), OrientDBConfig.defaultConfig());
  }

  private void dropDB()
  {
    // OrientDB orientDB = this.getRootOrientDB();
    //
    // try
    // {
    if (this.orientDB.exists(OrientDBProperties.getDatabaseName()))
    {
      this.orientDB.drop(OrientDBProperties.getDatabaseName());
      System.out.println("Dropped OrientDB Database: " + OrientDBProperties.getDatabaseName());
    }

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
    this.orientDB.create(OrientDBProperties.getDatabaseName(), ODatabaseType.PLOCAL);
    System.out.println("Created OrientDB Database: " + OrientDBProperties.getDatabaseName());
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

    ODatabaseSession dbSession = orientDB.open(OrientDBProperties.getDatabaseName(), OrientDBProperties.getAdminUserName(), OrientDBProperties.getAdminUserPassword());

    return new OrientDBRequest(dbSession);
  }

  /**
   * @see GraphDB#createVertexClass(GraphRequest, GraphRequest, String, String)
   */
  @Override
  public GraphDDLCommandAction createVertexClass(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String superClassName)
  {
    GraphDDLCommandAction action = new GraphDDLCommandAction()
    {
      public void execute()
      {
        OrientDBRequest ddlOrientDBRequest = (OrientDBRequest) ddlGraphDBRequest;
        ODatabaseSession db = ddlOrientDBRequest.getODatabaseSession();

        // make sure the DDL graph request is current on the active thread.
        db.activateOnCurrentThread();

        try
        {
          OClass oClass = db.getClass(className);

          if (oClass == null)
          {
            oClass = db.createVertexClass(className);

            if (superClassName != null)
            {
              OClass v = db.getMetadata().getSchema().getClass(superClassName);

              if (v == null)
              {
                throw new ProgrammingErrorException("Unable to find graph database class [" + superClassName + "]");
              }

              oClass.addSuperClass(v);
            }
            else
            {
              // OClass v = db.getMetadata().getSchema().getClass("V");
              // oClass.addSuperClass(v);
            }

            OSequenceLibrary sequenceLibrary = db.getMetadata().getSequenceLibrary();
            sequenceLibrary.createSequence(className + "Seq", SEQUENCE_TYPE.ORDERED, new OSequence.CreateParams().setStart(0L));
          }
        }
        finally
        {
          // make sure the DML graph request is current on the active thread.
          OrientDBRequest orientDBRequest = (OrientDBRequest) graphRequest;
          db = orientDBRequest.getODatabaseSession();
          db.activateOnCurrentThread();
        }
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
    return this.deleteClass(graphRequest, graphDDLRequest, className, true);
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

        try
        {
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

            // OClass e = db.getMetadata().getSchema().getClass("E");
            // oClass.addSuperClass(e);
          }
        }
        finally
        {
          // make sure the DML graph request is current on the active thread.
          OrientDBRequest orientDBRequest = (OrientDBRequest) graphRequest;
          db = orientDBRequest.getODatabaseSession();
          db.activateOnCurrentThread();
        }
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
    return deleteClass(graphRequest, graphDDLRequest, className, false);
  }

  private GraphDDLCommandAction deleteClass(GraphRequest graphRequest, GraphRequest graphDDLRequest, String className, boolean includeSequence)
  {
    GraphDDLCommandAction action = new GraphDDLCommandAction()
    {
      public void execute()
      {
        OrientDBRequest ddlOrientDDLRequest = (OrientDBRequest) graphDDLRequest;
        ODatabaseSession db = ddlOrientDDLRequest.getODatabaseSession();

        // make sure the DDL graph request is current on the active thread.
        db.activateOnCurrentThread();

        try
        {
          db.getMetadata().getSchema().dropClass(className);

          if (includeSequence)
          {
            OSequenceLibrary sequenceLibrary = db.getMetadata().getSequenceLibrary();
            sequenceLibrary.dropSequence(className + "Seq");
          }
        }
        finally
        {
          // make sure the DML graph request is current on the active thread.
          OrientDBRequest orientDBRequest = (OrientDBRequest) graphRequest;
          db = orientDBRequest.getODatabaseSession();
          db.activateOnCurrentThread();
        }
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

  @Override
  public boolean isIndexDefined(GraphRequest graphRequest, String className, String attributeName)
  {
    OrientDBRequest ddlOrientLRequest = (OrientDBRequest) graphRequest;
    ODatabaseSession db = ddlOrientLRequest.getODatabaseSession();

    OClass oClass = db.getClass(className);

    if (oClass != null)
    {
      Iterator<OIndex<?>> i = oClass.getInvolvedIndexes(attributeName).iterator();

      return i.hasNext();
    }
    else
    {
      return false;
    }
  }

  /**
   * @see GraphDB#createCharacterAttribute(GraphRequest, GraphRequest, String,
   *      String, boolean, int, boolean)
   */
  @Override
  public GraphDDLCommandAction createCharacterAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, boolean required, int maxLength, boolean cot)
  {
    return new OrientDBCreateCharacterAction(graphRequest, ddlGraphDBRequest, className, attributeName, OType.STRING.name(), required, maxLength, cot);
  }

  /**
   * @see GraphDB#modifiyCharacterAttributeLength(GraphRequest, GraphRequest,
   *      String, String, newMaxLength)
   */
  @Override
  public GraphDDLCommandAction modifiyCharacterAttributeLength(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, int newMaxLength)
  {
    GraphDDLCommandAction action = new OrientDBUpdatePropertyAction(graphRequest, ddlGraphDBRequest, className, attributeName)
    {
      @Override
      protected void configure(OProperty oProperty)
      {
        oProperty.setMax(Integer.toString(newMaxLength));
      }
    };

    return action;
  }

  @Override
  public GraphDDLCommandAction createConcreteAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String columnType, boolean required, boolean cot)
  {
    return new OrientDBCreatePropertyAction(graphRequest, ddlGraphDBRequest, className, attributeName, columnType, required, cot);
  }

  @Override
  public GraphDDLCommandAction createSetAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String setType, boolean required, boolean cot)
  {
    return new OrientDBCreateEmbeddedSetPropertyAction(graphRequest, ddlGraphDBRequest, className, attributeName, required, cot);
  }

  @Override
  public GraphDDLCommandAction createEmbeddedAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String embeddedClassType, boolean required, boolean cot)
  {
    return new OrientDBCreateEmbeddedPropertyAction(graphRequest, ddlGraphDBRequest, className, attributeName, embeddedClassType, required, cot);
  }

  @Override
  public GraphDDLCommandAction createGeometryAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String geometryType, boolean required, boolean cot)
  {
    return new OrientDBCreateGeometryPropertyAction(graphRequest, ddlGraphDBRequest, className, attributeName, geometryType, required, cot);
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
    GraphDDLCommandAction action = new OrientDBUpdatePropertyAction(graphRequest, ddlGraphDBRequest, className, attributeName)
    {
      @Override
      protected void configure(OProperty oProperty)
      {
        oProperty.setMandatory(required);
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
    GraphDDLCommandAction action = new OrientDBDDLAction(graphRequest, ddlGraphDBRequest)
    {
      @Override
      protected void executeDDL(ODatabaseSession db)
      {
        OClass oClass = db.getClass(className);

        if (oClass != null)
        {
          oClass.dropProperty(attributeName);
        }

      }
    };

    return action;
  }

  @Override
  public GraphDDLCommandAction dropGeometryAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName)
  {
    GraphDDLCommandAction action = new OrientDBDDLAction(graphRequest, ddlGraphDBRequest)
    {
      @Override
      protected void executeDDL(ODatabaseSession db)
      {
        OClass oClass = db.getClass(className);

        if (oClass != null)
        {
          Iterator<OIndex<?>> i = oClass.getInvolvedIndexes(attributeName).iterator();

          while (i.hasNext())
          {
            i.next().delete();
          }

          oClass.dropProperty(attributeName);
        }
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

        try
        {
          OClass oClass = db.getClass(className);

          if (oClass != null)
          {
            OProperty oProperty = oClass.getProperty(attributeName);

            OClass.INDEX_TYPE oClassIndexType = convertIndexType(indexType);

            if (oClassIndexType != null)
            {
              String indexName = OrientDBImpl.generateIndexName();

              oClass.createIndex(indexName, oClassIndexType, oProperty.getName());
            }
            // Clear the index if it is set to none.
            else
            {
              Iterator<OIndex<?>> i = oClass.getInvolvedIndexes(attributeName).iterator();

              while (i.hasNext())
              {
                OIndex<?> oIndex = i.next();

                String indexType = oIndex.getType();

                if (!indexType.equals(INDEX_TYPE.SPATIAL.name()))
                {
                  oIndex.delete();
                }
                // oIndex.clear();
                // oIndex.flush();
              }
            }
          }
        }
        finally
        {
          // make sure the DML graph request is current on the active thread.
          OrientDBRequest orientDBRequest = (OrientDBRequest) graphRequest;
          db = orientDBRequest.getODatabaseSession();
          db.activateOnCurrentThread();
        }
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

        if (!oIndex.getType().equals(INDEX_TYPE.SPATIAL.name()))
        {
          returnValue = oIndex.getName();
        }
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
  public String getDbColumnType(MdAttributeConcreteDAOIF mdAttribute)
  {
    if (mdAttribute instanceof MdAttributeIntegerDAOIF)
    {
      return OType.INTEGER.name();
    }
    else if (mdAttribute instanceof MdAttributeLongDAOIF)
    {
      return OType.LONG.name();
    }
    else if (mdAttribute instanceof MdAttributeDoubleDAOIF)
    {
      return OType.DOUBLE.name();
    }
    else if (mdAttribute instanceof MdAttributeFloatDAOIF)
    {
      return OType.FLOAT.name();
    }
    else if (mdAttribute instanceof MdAttributeBooleanDAOIF)
    {
      return OType.BOOLEAN.name();
    }
    else if (mdAttribute instanceof MdAttributeDateDAOIF)
    {
      return OType.DATE.name();
    }
    else if (mdAttribute instanceof MdAttributeDateTimeDAOIF)
    {
      return OType.DATETIME.name();
    }
    else if (mdAttribute instanceof MdAttributeTimeDAOIF)
    {
      return OType.DATETIME.name();
    }
    else if (mdAttribute instanceof MdAttributeCharDAOIF)
    {
      return OType.STRING.name();
    }
    else if (mdAttribute instanceof MdAttributeTextDAOIF)
    {
      return OType.STRING.name();
    }
    else if (mdAttribute instanceof MdAttributeUUIDDAOIF)
    {
      return OType.STRING.name();
    }
    else if (mdAttribute instanceof MdAttributeReferenceDAOIF)
    {
      return OType.STRING.name();
    }
    else if (mdAttribute instanceof MdAttributeEnumerationDAOIF)
    {
      return OType.STRING.name();
    }
    else if (mdAttribute instanceof MdAttributePointDAOIF)
    {
      return "OPoint";
    }
    else if (mdAttribute instanceof MdAttributeLineStringDAOIF)
    {
      return "OLineString";
    }
    else if (mdAttribute instanceof MdAttributePolygonDAOIF)
    {
      return "OPolygon";
    }
    else if (mdAttribute instanceof MdAttributeMultiPointDAOIF)
    {
      return "OMultiPoint";
    }
    else if (mdAttribute instanceof MdAttributeMultiLineStringDAOIF)
    {
      return "OMultiLineString";
    }
    else if (mdAttribute instanceof MdAttributeMultiPolygonDAOIF)
    {
      return "OMultiPolygon";
    }
    else if (mdAttribute instanceof MdAttributeEmbeddedDAOIF)
    {
      MdAttributeEmbeddedDAOIF mdAttributeEmbeddedDAO = (MdAttributeEmbeddedDAOIF) mdAttribute;

      MdClassDAOIF mdClassDAOIF = mdAttributeEmbeddedDAO.getEmbeddedMdClassDAOIF();

      if (mdClassDAOIF instanceof MdGraphClassDAOIF)
      {
        return ( (MdGraphClassDAOIF) mdClassDAOIF ).getDBClassName();
      }
      else
      {
        throw new ProgrammingErrorException("Attribute [" + MdAttributeEmbeddedInfo.CLASS + "] can only be defined on a [" + MdGraphClassInfo.CLASS + "]");
      }
    }

    throw new ProgrammingErrorException("Unknown column type for MdAttribute [" + mdAttribute.getType() + "]");
  }

  @Override
  public void insert(GraphRequest graphRequest, GraphObjectDAO graphObjectDAO)
  {
    MdGraphClassDAOIF mdClassDAO = graphObjectDAO.getMdClassDAO();
    String dbClassName = mdClassDAO.getValue(MdVertexInfo.DB_CLASS_NAME);

    OrientDBRequest orientDBRequest = (OrientDBRequest) graphRequest;

    ODatabaseSession db = orientDBRequest.getODatabaseSession();
    OVertex vertex = db.newVertex(dbClassName);

    this.populateVertex(db, graphObjectDAO, vertex);
    this.populateSequence(db, graphObjectDAO, vertex);

    ORecord record = vertex.save();

    graphObjectDAO.setRID(record.getIdentity());
    graphObjectDAO.setCommitState();

    this.populateDAO(vertex, graphObjectDAO);
  }

  @Override
  public void update(GraphRequest graphRequest, GraphObjectDAO graphObjectDAO)
  {
    OrientDBRequest orientDBRequest = (OrientDBRequest) graphRequest;
    ODatabaseSession db = orientDBRequest.getODatabaseSession();

    OVertex vertex = db.load((ORID) graphObjectDAO.getRID());

    // Validate the sequence number
    Object oSeq = vertex.getProperty(BusinessInfo.SEQUENCE);
    Long iSeq = graphObjectDAO.getSequence();

    if (!oSeq.equals(iSeq))
    {
      throw new StaleEntityException("Sequence numbers do not match", graphObjectDAO);
    }

    this.populateVertex(db, graphObjectDAO, vertex);
    this.populateSequence(db, graphObjectDAO, vertex);

    vertex.save();

    this.populateDAO(vertex, graphObjectDAO);
  }

  protected void populateSequence(ODatabaseSession db, GraphObjectDAO graphObjectDAO, OVertex vertex)
  {
    OSequence seq = getSequence(db, graphObjectDAO);
    long next = seq.next();

    vertex.setProperty(BusinessInfo.SEQUENCE, next);
  }

  @Override
  public void delete(GraphRequest graphRequest, GraphObjectDAO graphObjectDAO)
  {
    OrientDBRequest orientDBRequest = (OrientDBRequest) graphRequest;

    ODatabaseSession db = orientDBRequest.getODatabaseSession();
    OElement element = db.load((ORID) graphObjectDAO.getRID());
    if (element != null)
    {
      element.delete();
    }
  }

  @Override
  public VertexObjectDAOIF get(GraphRequest graphRequest, MdVertexDAOIF mdVertexDAOIF, String oid)
  {
    String dbClassName = mdVertexDAOIF.getValue(MdVertexInfo.DB_CLASS_NAME);

    OrientDBRequest orientDBRequest = (OrientDBRequest) graphRequest;

    ODatabaseSession db = orientDBRequest.getODatabaseSession();

    String statement = "SELECT FROM " + dbClassName + " WHERE oid = ?";

    try (OResultSet rs = db.query(statement, oid))
    {
      if (rs.hasNext())
      {
        OResult result = rs.next();

        if (result.isElement())
        {
          OElement element = result.toElement();

          return this.buildDAO(element);
        }
        else
        {
          throw new UnsupportedOperationException("Unexpected result type");
        }
      }

    }

    return null;
  }

  @Override
  public List<VertexObjectDAOIF> query(GraphRequest request, String statement, Map<String, Object> parameters)
  {
    List<VertexObjectDAOIF> results = new LinkedList<VertexObjectDAOIF>();

    OrientDBRequest orientDBRequest = (OrientDBRequest) request;

    ODatabaseSession db = orientDBRequest.getODatabaseSession();

    /*
     * Convert geometry parameters
     */
    Set<Entry<String, Object>> entries = parameters.entrySet();

    for (Entry<String, Object> entry : entries)
    {
      Object value = entry.getValue();

      if (value instanceof Geometry)
      {
        entry.setValue(OShapeFactory.INSTANCE.toDoc((Geometry) value));
      }
    }

    try (OResultSet rs = db.query(statement, parameters))
    {
      while (rs.hasNext())
      {
        OResult result = rs.next();

        if (result.isElement())
        {
          OElement element = result.toElement();

          results.add(this.buildDAO(element));
        }
        else
        {
          throw new UnsupportedOperationException("Unexpected result type");
        }
      }
    }

    return results;
  }

  @Override
  public void addEdge(GraphRequest request, VertexObjectDAOIF parent, VertexObjectDAOIF child, MdEdgeDAOIF mdEdge)
  {
    String edgeClass = mdEdge.getValue(MdEdgeInfo.DB_CLASS_NAME);

    OrientDBRequest orientDBRequest = (OrientDBRequest) request;

    ODatabaseSession db = orientDBRequest.getODatabaseSession();
    OVertex pVertex = db.load((ORID) parent.getRID());
    OVertex cVertex = db.load((ORID) child.getRID());

    OEdge edge = pVertex.addEdge(cVertex, edgeClass);
    edge.setProperty("oid", UUID.randomUUID().toString());
    edge.save();
  }

  @Override
  public void removeEdge(GraphRequest request, VertexObjectDAOIF parent, VertexObjectDAOIF child, MdEdgeDAOIF mdEdge)
  {
    OrientDBRequest orientDBRequest = (OrientDBRequest) request;
    String edgeClass = mdEdge.getValue(MdEdgeInfo.DB_CLASS_NAME);

    ODatabaseSession db = orientDBRequest.getODatabaseSession();

    // String statement = "DELETE EDGE " + edgeClass + " FROM ? TO ?";
    // db.command(statement, parent.getRID(), child.getRID());

    OVertex vertex = db.load((ORID) parent.getRID());

    Iterable<OEdge> edges = vertex.getEdges(ODirection.OUT, edgeClass);

    for (OEdge edge : edges)
    {
      OVertex to = edge.getTo();

      if (to.getIdentity().equals(child.getRID()))
      {
        edge.delete();
      }
    }
  }

  @Override
  public List<VertexObjectDAOIF> getChildren(GraphRequest request, VertexObjectDAOIF vertexDAO, MdEdgeDAOIF mdEdge)
  {
    return this.getVertices(request, vertexDAO, ODirection.OUT, mdEdge);
  }

  @Override
  public List<VertexObjectDAOIF> getParents(GraphRequest request, VertexObjectDAOIF vertexDAO, MdEdgeDAOIF mdEdge)
  {
    return this.getVertices(request, vertexDAO, ODirection.IN, mdEdge);
  }

  private List<VertexObjectDAOIF> getVertices(GraphRequest request, VertexObjectDAOIF vertexDAO, ODirection direction, MdEdgeDAOIF mdEdge)
  {
    String edgeClass = mdEdge.getValue(MdEdgeInfo.DB_CLASS_NAME);

    OrientDBRequest orientDBRequest = (OrientDBRequest) request;

    ODatabaseSession db = orientDBRequest.getODatabaseSession();
    OVertex vertex = db.load((ORID) vertexDAO.getRID());

    Iterable<OVertex> targets = vertex.getVertices(direction, edgeClass);

    LinkedList<VertexObjectDAOIF> list = new LinkedList<VertexObjectDAOIF>();

    for (OVertex target : targets)
    {
      list.add(this.buildDAO(target));
    }

    return list;
  }

  protected VertexObjectDAOIF buildDAO(OElement element)
  {
    OClass oClass = element.getSchemaType().get();
    MdGraphClassDAOIF mdGraph = MdGraphClassDAO.getMdGraphClassByTableName(oClass.getName());

    return this.buildDAO((MdVertexDAOIF) mdGraph, element);
  }

  protected VertexObjectDAOIF buildDAO(MdVertexDAOIF mdVertexDAOIF, OElement vertex)
  {
    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAOIF);

    populateDAO(vertex, vertexDAO);

    return vertexDAO;
  }

  protected void populateDAO(OElement vertex, GraphObjectDAO vertexDAO)
  {
    vertexDAO.setIsNew(false);
    vertexDAO.setAppliedToDB(true);

    Attribute[] attributes = vertexDAO.getAttributeArray();
    MdGraphClassDAOIF mdClass = vertexDAO.getMdGraphClassDAO();

    for (Attribute attribute : attributes)
    {
      MdAttributeConcreteDAOIF mdAttribute = attribute.getMdAttribute();
      String columnName = mdAttribute.getColumnName();
      Object value = vertex.getProperty(columnName);

      if (value != null)
      {
        if (mdAttribute instanceof MdAttributeGeometryDAOIF)
        {
          ODocument doc = (ODocument) value;

          Shape shape = OShapeFactory.INSTANCE.fromDoc(doc);
          Geometry geometry = OShapeFactory.INSTANCE.toGeometry(shape);

          attribute.setValueInternal(geometry);
        }
        else if (mdAttribute instanceof MdAttributeEnumerationDAO)
        {
          attribute.setValueInternal(value);
        }
        else if (mdAttribute instanceof MdAttributeEmbeddedDAO)
        {
          AttributeEmbedded attributeEmbedded = (AttributeEmbedded) attribute;
          GraphObjectDAO embedGraphObjectDAO = (GraphObjectDAO) attributeEmbedded.getObjectValue();

          if (value instanceof OVertex)
          {
            OVertex embedOVertex = (OVertex) value;

            this.populateDAO(embedOVertex, embedGraphObjectDAO);
          }

          if (mdClass.isEnableChangeOverTime())
          {
            MdVertexDAOIF embeddedVertex = (MdVertexDAOIF) embedGraphObjectDAO.getMdGraphClassDAO();

            List<OElement> elements = vertex.getProperty(columnName + OrientDBConstant.COT_SUFFIX);

            attribute.clearValuesOverTime();

            for (OElement element : elements)
            {
              Date startDate = element.getProperty("startDate");
              Date endDate = element.getProperty("endDate");
              OElement vElement = element.getProperty("value");

              VertexObjectDAO votDAO = VertexObjectDAO.newInstance(embeddedVertex);
              this.populateDAO(vElement, votDAO);

              attribute.setValueInternal(votDAO, startDate, endDate);
            }
          }
        }
        else
        {
          attribute.setValueInternal(value);
        }
      }

      if (mdClass.isEnableChangeOverTime())
      {
        populateChangeOverTime(vertex, attribute, mdAttribute);
      }
    }

    vertexDAO.setRID(vertex.getIdentity());
  }

  protected void populateChangeOverTime(OElement vertex, Attribute attribute, MdAttributeConcreteDAOIF mdAttribute)
  {
    String columnName = mdAttribute.getColumnName();

    if (mdAttribute instanceof MdAttributeEmbeddedDAOIF)
    {
      MdVertexDAOIF embeddedVertex = (MdVertexDAOIF) ( (MdAttributeEmbeddedDAOIF) mdAttribute ).getEmbeddedMdClassDAOIF();

      List<OElement> elements = vertex.getProperty(columnName + OrientDBConstant.COT_SUFFIX);

      attribute.clearValuesOverTime();

      for (OElement element : elements)
      {
        Date startDate = element.getProperty("startDate");
        Date endDate = element.getProperty("endDate");
        OElement vElement = element.getProperty("value");

        VertexObjectDAO votDAO = VertexObjectDAO.newInstance(embeddedVertex);
        this.populateDAO(vElement, votDAO);

        attribute.setValueInternal(votDAO, startDate, endDate);
      }
    }
    else
    {
      List<OElement> elements = vertex.getProperty(columnName + OrientDBConstant.COT_SUFFIX);

      attribute.clearValuesOverTime();

      for (OElement element : elements)
      {
        Date startDate = element.getProperty("startDate");
        Date endDate = element.getProperty("endDate");
        Object votValue = element.getProperty("value");

        attribute.setValueInternal(votValue, startDate, endDate);
      }
    }
  }

  protected void populateVertex(ODatabaseSession db, GraphObjectDAO graphObjectDAO, OVertex vertex)
  {
    Attribute[] attributes = graphObjectDAO.getAttributeArray();

    for (Attribute attribute : attributes)
    {
      MdAttributeConcreteDAOIF mdAttribute = attribute.getMdAttribute();
      MdGraphClassDAOIF mdClass = (MdGraphClassDAOIF) mdAttribute.definedByClass();

      if (mdAttribute instanceof MdAttributeGeometryDAOIF)
      {
        Geometry value = (Geometry) attribute.getObjectValue();
        String columnName = mdAttribute.getColumnName();

        if (value != null)
        {
          ODocument document = OShapeFactory.INSTANCE.toDoc(value);

          vertex.setProperty(columnName, document);
        }
        else
        {
          vertex.setProperty(columnName, null);
        }
      }
      else if (mdAttribute instanceof MdEnumerationDAOIF)
      {
        Set<String> value = ( (AttributeEnumeration) attribute ).getObjectValue();
        String columnName = mdAttribute.getColumnName();

        if (value.size() > 0)
        {
          vertex.setProperty(columnName, value);
        }
        else
        {
          vertex.setProperty(columnName, null);
        }
      }
      else if (mdAttribute instanceof MdAttributeEmbeddedDAOIF)
      {
        MdAttributeEmbeddedDAOIF mdAttributeEmbedded = (MdAttributeEmbeddedDAOIF) mdAttribute;
        String embeddedClassName = ( (MdGraphClassDAOIF) mdAttributeEmbedded.getEmbeddedMdClassDAOIF() ).getDBClassName();
        String columnName = mdAttributeEmbedded.getColumnName();

        OVertex innerVertex = null;

        AttributeEmbedded attributeEmbedded = (AttributeEmbedded) attribute;
        GraphObjectDAO embeddedObject = (GraphObjectDAO) attributeEmbedded.getObjectValue();

        if (embeddedObject != null)
        {
          OElement oElement = vertex.getProperty(columnName);

          if (oElement == null)
          {
            innerVertex = db.newVertex(embeddedClassName);
          }
          else if (oElement instanceof OVertex)
          {
            innerVertex = (OVertex) oElement;
          }

          if (innerVertex != null)
          {
            // persist the values on the inner embedded object
            this.populateVertex(db, embeddedObject, innerVertex);
          }
        }
        // else embeddedObject == null- if there is no embedded object, then set
        // the propperty to null

        vertex.setProperty(columnName, innerVertex);

        if (mdClass.isEnableChangeOverTime())
        {
          this.populateEmbeddedChangeOvertTime(db, vertex, attribute, embeddedClassName, columnName);
        }
      }
      else
      {
        String columnName = mdAttribute.getColumnName();

        vertex.setProperty(columnName, attribute.getObjectValue());

        if (mdClass.isEnableChangeOverTime())
        {
          this.populateChangeOverTime(db, vertex, attribute, columnName);
        }
      }
    }
  }

  protected void populateChangeOverTime(ODatabaseSession db, OVertex vertex, Attribute attribute, String columnName)
  {
    List<ValueOverTime> valuesOverTime = attribute.getValuesOverTime();
    List<OVertex> documents = new LinkedList<OVertex>();

    for (ValueOverTime vot : valuesOverTime)
    {
      OVertex document = db.newVertex("ChangeOverTime");
      document.setProperty("startDate", vot.getStartDate());
      document.setProperty("endDate", vot.getEndDate());
      document.setProperty("value", vot.getValue());

      documents.add(document);
    }

    vertex.setProperty(columnName + OrientDBConstant.COT_SUFFIX, documents);
  }

  protected void populateEmbeddedChangeOvertTime(ODatabaseSession db, OVertex vertex, Attribute attribute, String embeddedClassName, String columnName)
  {
    List<ValueOverTime> valuesOverTime = attribute.getValuesOverTime();
    List<OVertex> documents = new LinkedList<OVertex>();

    for (ValueOverTime vot : valuesOverTime)
    {
      OVertex votVertex = db.newVertex(embeddedClassName);
      this.populateVertex(db, (GraphObjectDAO) vot.getValue(), votVertex);

      OVertex document = db.newVertex(embeddedClassName + OrientDBConstant.COT_SUFFIX);
      document.setProperty("startDate", vot.getStartDate());
      document.setProperty("endDate", vot.getEndDate());
      document.setProperty("value", votVertex);

      documents.add(document);
    }

    vertex.setProperty(columnName + OrientDBConstant.COT_SUFFIX, documents);
  }

  /**
   * @see GraphDB#processException(Locale, RuntimeException)
   */
  @Override
  public RuntimeException processException(Locale locale, RuntimeException runEx)
  {
    if (runEx instanceof ORecordDuplicatedException)
    {
      ORecordDuplicatedException dupEx = (ORecordDuplicatedException) runEx;

      Object key = dupEx.getKey();

      String value;

      if (key == null)
      {
        value = "NULL";
      }
      else
      {
        value = key.toString();
      }

      String dbIndexName = dupEx.getIndexName();
      MdAttributeConcreteDAOIF mdAttribute = MdAttributeConcreteDAO.getMdAttributeWithIndex(dbIndexName);
      MdGraphClassDAOIF mdGraphClass = (MdGraphClassDAOIF) mdAttribute.definedByClass();

      String devMsg = "The graph class [" + mdGraphClass.definesType() + "] already has an instance with attribute [" + mdAttribute.definesAttribute() + "] with value [" + value + "]";

      List<String> localizedAttrLabels = new LinkedList<String>();
      localizedAttrLabels.add(mdAttribute.getDisplayLabel(locale));

      List<String> valueList = new LinkedList<String>();
      valueList.add(value);

      throw new DuplicateDataException(devMsg, localizedAttrLabels, mdGraphClass, valueList);
    }
    else
    {
      throw runEx;
    }
  }

  protected OSequence getSequence(ODatabaseSession db, GraphObjectDAO graphObjectDAO)
  {
    MdGraphClassDAOIF mdGraphClassDAO = graphObjectDAO.getMdGraphClassDAO();
    String className = mdGraphClassDAO.getDBClassName();

    return db.getMetadata().getSequenceLibrary().getSequence(className + "Seq");
  }

  public static String generateIndexName()
  {
    return ( "i" + UUID.randomUUID().toString().replaceAll("-", "") ).substring(0, 30);
  }

  public static OClass getOrCreateChangeOverTime(ODatabaseSession db)
  {
    OClass oClass = db.getClass(OrientDBConstant.CHANGE_OVER_TIME);

    if (oClass == null)
    {
      oClass = db.createVertexClass(OrientDBConstant.CHANGE_OVER_TIME);
      oClass.createProperty(OrientDBConstant.START_DATE, OType.DATETIME);
      oClass.createProperty(OrientDBConstant.END_DATE, OType.DATETIME);
      oClass.createProperty(OrientDBConstant.VALUE, OType.ANY);
    }

    return oClass;
  }

  public static OClass getOrCreateChangeOverTime(ODatabaseSession db, OClass vClass)
  {
    OClass oClass = db.getClass(vClass.getName() + OrientDBConstant.COT_SUFFIX);

    if (oClass == null)
    {
      oClass = db.createVertexClass(vClass.getName() + OrientDBConstant.COT_SUFFIX);
      oClass.createProperty(OrientDBConstant.START_DATE, OType.DATETIME);
      oClass.createProperty(OrientDBConstant.END_DATE, OType.DATETIME);
      oClass.createProperty(OrientDBConstant.VALUE, OType.EMBEDDED, vClass);
    }

    return oClass;
  }

}
