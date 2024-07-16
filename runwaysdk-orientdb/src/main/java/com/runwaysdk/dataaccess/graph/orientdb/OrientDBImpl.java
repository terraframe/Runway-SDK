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
package com.runwaysdk.dataaccess.graph.orientdb;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.locationtech.jts.geom.Geometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecimalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEmbeddedDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFloatDAOIF;
import com.runwaysdk.dataaccess.MdAttributeGraphRefDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLongDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTextDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.graph.EdgeObjectDAO;
import com.runwaysdk.dataaccess.graph.EdgeObjectDAOIF;
import com.runwaysdk.dataaccess.graph.EmbeddedGraphObjectDAO;
import com.runwaysdk.dataaccess.graph.GraphDB;
import com.runwaysdk.dataaccess.graph.GraphDDLCommandAction;
import com.runwaysdk.dataaccess.graph.GraphObjectDAO;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.graph.ResultSetConverterIF;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.VertexObjectDAOIF;
import com.runwaysdk.dataaccess.graph.attributes.Attribute;
import com.runwaysdk.dataaccess.graph.attributes.AttributeEmbedded;
import com.runwaysdk.dataaccess.graph.attributes.AttributeEnumeration;
import com.runwaysdk.dataaccess.graph.attributes.AttributeGraphRef;
import com.runwaysdk.dataaccess.graph.attributes.AttributeGraphRef.ID;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTime;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTimeCollection;
import com.runwaysdk.dataaccess.metadata.DeleteContext;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.gis.dataaccess.MdAttributeGeometryDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeLineStringDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiLineStringDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPointDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPolygonDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributePointDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributePolygonDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeShapeDAOIF;

public class OrientDBImpl implements GraphDB
{
  private OrientDB orientDB;

  // private ODatabasePool pool;

  private Logger   logger = LoggerFactory.getLogger(OrientDBImpl.class);

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
    this.createAppUser();
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
      logger.info("Dropped OrientDB Database: " + OrientDBProperties.getDatabaseName());
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
    logger.info("Created OrientDB Database: " + OrientDBProperties.getDatabaseName());
    // }
    // finally
    // {
    // orientDB.close();
    // }
  }

  private void createAppUser()
  {
    String adminUser = OrientDBProperties.getRootUserName();
    String adminPass = OrientDBProperties.getRootUserPassword();

    ODatabaseSession rootSession = orientDB.open(OrientDBProperties.getDatabaseName(), adminUser, adminPass);

    try
    {
      rootSession.activateOnCurrentThread();

      // Remove pre-loaded users
      String deleteAdmin = "delete from ouser where name = 'admin';";
      try (OResultSet rs = rootSession.command(deleteAdmin))
      {
      }

      String deleteReader = "delete from ouser where name = 'reader';";
      try (OResultSet rs = rootSession.command(deleteReader))
      {
      }

      String deleteWriter = "delete from ouser where name = 'writer';";
      try (OResultSet rs = rootSession.command(deleteWriter))
      {
      }

      String appUser = OrientDBProperties.getAppUserName();
      String appPass = OrientDBProperties.getAppUserPassword();

      String sqlAdminUser = "insert into ouser set name = '" + appUser + "', password = '" + appPass + "', status = 'ACTIVE', roles = (select from ORole where name = 'admin')";
      try (OResultSet rs = rootSession.command(sqlAdminUser))
      {
      }

      logger.info("Created app user with name [" + appUser + "].");
    }
    finally
    {
      rootSession.close();
    }
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
    logger.info("Closing the GraphDB connection pool");
  }

  @Override
  public void close()
  {
    this.orientDB.close();

    this.orientDB = null;
  }

  @Override
  public OrientDBRequest getGraphDBRequest()
  {
    // ODatabaseSession dbSession = this.pool.acquire();

    ODatabaseSession dbSession = orientDB.open(OrientDBProperties.getDatabaseName(), OrientDBProperties.getAppUserName(), OrientDBProperties.getAppUserPassword());

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

            // OSequenceLibrary sequenceLibrary =
            // db.getMetadata().getSequenceLibrary();
            // sequenceLibrary.createSequence(className + "Seq",
            // SEQUENCE_TYPE.ORDERED, new
            // OSequence.CreateParams().setStart(0L));
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
    GraphDDLCommandAction action = new OrientDBDeleteClassAction(graphRequest, graphDDLRequest, className, true)
    {
      @Override
      public void execute()
      {
        OrientDBRequest request = this.getGraphRequest();
        ODatabaseSession db = request.getODatabaseSession();

        String statement = "DELETE EDGE WHERE out.@class='" + className + "' or in.@class='" + className + "'";

        try (OResultSet command = db.command(statement))
        {
          // Do nothing
        }

        super.execute();
      }
    };
    return action;
  }

  /**
   * @see GraphDB#createEdgeClass(GraphRequest, GraphRequest, String, String,
   *      String)
   */
  @Override
  public GraphDDLCommandAction createEdgeClass(GraphRequest graphRequest, GraphRequest graphDDLRequest, String edgeClass, String superClassName, String parentVertexClass, String childVertexClass)
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

            if (superClassName != null)
            {
              OClass e = db.getMetadata().getSchema().getClass(superClassName);

              if (e == null)
              {
                throw new ProgrammingErrorException("Unable to find graph database class [" + superClassName + "]");
              }

              oClass.addSuperClass(e);
            }

            // Add constraints to parent and child vertex classes.
            // create property myE.out LINK A
            // create property myE.in LINK B
            // "SELECT FROM V WHERE name = ? and surnanme = ?"
            String statement = "CREATE PROPERTY " + edgeClass + ".OUT LINK " + parentVertexClass;
            try (OResultSet rs = db.command(statement))
            {
            }

            statement = "CREATE PROPERTY " + edgeClass + ".IN LINK " + childVertexClass;
            try (OResultSet rs = db.command(statement))
            {
            }

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

  /**
   * @see GraphDB#createEmbeddedClass(GraphRequest, GraphRequest, String,
   *      String)
   */
  @Override
  public GraphDDLCommandAction createEmbeddedClass(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String superClassName)
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
            if (StringUtils.isEmpty(superClassName))
            {
              oClass = db.createClass(className);
            }
            else
            {
              oClass = db.createClass(className, superClassName);
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
   * @see GraphDB#deleteEmbeddedClass(GraphRequest, GraphRequest, String)
   */
  @Override
  public GraphDDLCommandAction deleteEmbeddedClass(GraphRequest graphRequest, GraphRequest graphDDLRequest, String className)
  {
    return deleteClass(graphRequest, graphDDLRequest, className, false);
  }

  private GraphDDLCommandAction deleteClass(GraphRequest graphRequest, GraphRequest graphDDLRequest, String className, boolean includeSequence)
  {
    return new OrientDBDeleteClassAction(graphRequest, graphDDLRequest, className, includeSequence);
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

  public boolean isClassDefined(GraphRequest graphRequest, String className)
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
      Iterator<?> i = oClass.getInvolvedIndexes(attributeName).iterator();

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
  public GraphDDLCommandAction createGraphReferenceAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String embeddedClassType, boolean required, boolean cot)
  {
    return new OrientDBCreateLinkPropertyAction(graphRequest, ddlGraphDBRequest, className, attributeName, embeddedClassType, required, cot);
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
      maxLength = Integer.valueOf(oProperty.getMax());
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
   * @see GraphDB#dropAttribute(GraphRequest, GraphRequest, String, String,
   *      boolean, boolean)
   */
  @Override
  public GraphDDLCommandAction dropAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, boolean cot, DeleteContext context)
  {
    GraphDDLCommandAction action = new OrientDBDDLAction(graphRequest, ddlGraphDBRequest)
    {
      @Override
      protected void executeDDL(ODatabaseSession db)
      {
        OClass oClass = db.getClass(className);

        if (oClass != null)
        {
          List<String> attrs = new LinkedList<String>();
          attrs.add(attributeName);

          if (cot)
          {
            attrs.add(attributeName + OrientDBConstant.COT_SUFFIX);
          }

          for (String attr : attrs)
          {
            if (oClass.getProperty(attr) != null)
            {
              oClass.dropProperty(attr);

              if (context.isRemoveValues())
              {
                // Delete any existing values
                try (OResultSet rs = db.command("UPDATE " + className + " REMOVE " + attr))
                {
                  // Do nothing
                }
              }
            }
          }
        }
      }
    };

    return action;
  }

  @Override
  public GraphDDLCommandAction dropGeometryAttribute(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, boolean cot, DeleteContext context)
  {
    GraphDDLCommandAction action = new OrientDBDDLAction(graphRequest, ddlGraphDBRequest)
    {
      @Override
      protected void executeDDL(ODatabaseSession db)
      {
        OClass oClass = db.getClass(className);

        if (oClass != null)
        {
          List<String> attrs = new LinkedList<String>();
          attrs.add(attributeName);

          if (cot)
          {
            attrs.add(attributeName + OrientDBConstant.COT_SUFFIX);
          }

          for (String attr : attrs)
          {
            Iterator<OIndex> i = oClass.getInvolvedIndexes(attr).iterator();

            while (i.hasNext())
            {
              i.next().delete();
            }

            oClass.dropProperty(attr);

            // Delete any existing values
            if (context.isRemoveValues())
            {
              try (OResultSet rs = db.command("UPDATE " + className + " REMOVE " + attr))
              {
                // Do nothing
              }
            }
          }
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

  @Override
  public GraphDDLCommandAction ddlCommand(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String statement, Map<String, Object> parameters)
  {
    GraphDDLCommandAction action = new GraphDDLCommandAction()
    {
      public void execute()
      {
        OrientDBRequest ddlOrientDBRequest = (OrientDBRequest) ddlGraphDBRequest;
        ODatabaseSession db = ddlOrientDBRequest.getODatabaseSession();

        // make sure the DDL graph request is current on the active thread.
        db.activateOnCurrentThread();

        try (OResultSet result = db.command(statement, parameters))
        {
          // Close the ddl result set
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
              Iterator<OIndex> i = oClass.getInvolvedIndexes(attributeName).iterator();

              while (i.hasNext())
              {
                OIndex oIndex = i.next();

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
      Iterator<OIndex> i = oClass.getInvolvedIndexes(attributeName).iterator();

      while (i.hasNext())
      {
        OIndex oIndex = i.next();
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
      Iterator<OIndex> i = oClass.getInvolvedIndexes(attributeName).iterator();

      while (i.hasNext())
      {
        OIndex oIndex = i.next();

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
    else if (mdAttribute instanceof MdAttributeDecimalDAOIF)
    {
      return OType.DECIMAL.name();
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
    else if (mdAttribute instanceof MdAttributeGraphRefDAOIF)
    {
      return OType.LINK.name();
    }
    else if (mdAttribute instanceof MdAttributePointDAOIF)
    {
      return "OPoint";
    }
    else if (mdAttribute instanceof MdAttributeLineStringDAOIF)
    {
      return "OLineString";
    }
    else if (mdAttribute instanceof MdAttributeShapeDAOIF)
    {
      return "OShape";
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
  public void insert(GraphRequest request, GraphObjectDAO graphObjectDAO)
  {
    if (graphObjectDAO instanceof VertexObjectDAO)
    {
      MdGraphClassDAOIF mdClassDAO = graphObjectDAO.getMdClassDAO();
      String dbClassName = mdClassDAO.getValue(MdVertexInfo.DB_CLASS_NAME);

      OrientDBRequest orientDBRequest = (OrientDBRequest) request;

      ODatabaseSession db = orientDBRequest.getODatabaseSession();
      OVertex vertex = db.newVertex(dbClassName);

      this.populateElement(db, graphObjectDAO, vertex);
      this.populateSequence(db, graphObjectDAO, vertex);

      ORecord record = vertex.save();

      graphObjectDAO.setRID(record.getIdentity());
      graphObjectDAO.setCommitState();

      new ResultSetConverter().populateDAO(vertex, graphObjectDAO);
    }
    else if (graphObjectDAO instanceof EdgeObjectDAO)
    {
      this.addEdge(request, (EdgeObjectDAO) graphObjectDAO);
    }
    else if (graphObjectDAO instanceof EmbeddedGraphObjectDAO)
    {
      throw new UnsupportedOperationException();
    }
  }

  @Override
  public void update(GraphRequest graphRequest, GraphObjectDAO graphObjectDAO)
  {
    OrientDBRequest orientDBRequest = (OrientDBRequest) graphRequest;
    ODatabaseSession db = orientDBRequest.getODatabaseSession();

    OElement element = db.load((ORID) graphObjectDAO.getRID());

    if (graphObjectDAO instanceof EmbeddedGraphObjectDAO)
    {
      throw new UnsupportedOperationException();
    }

    // Validate the sequence number
    if (graphObjectDAO instanceof VertexObjectDAO)
    {
      Object oSeq = element.getProperty(BusinessInfo.SEQUENCE);
      Long iSeq = graphObjectDAO.getSequence();

      // if (!oSeq.equals(iSeq))
      // {
      // throw new StaleEntityException("Sequence numbers do not match",
      // graphObjectDAO);
      // }
    }

    this.populateElement(db, graphObjectDAO, element);

    if (graphObjectDAO instanceof VertexObjectDAO)
    {
      this.populateSequence(db, graphObjectDAO, element);
    }

    element.save();

    new ResultSetConverter().populateDAO(element, graphObjectDAO);
  }

  protected void populateSequence(ODatabaseSession db, GraphObjectDAO graphObjectDAO, OElement element)
  {
    // OSequence seq = getSequence(db, graphObjectDAO);
    //// long next = seq.next();
    //
    long sequence = graphObjectDAO.getSequence() != null ? graphObjectDAO.getSequence() + 1 : 0;

    element.setProperty(BusinessInfo.SEQUENCE, sequence);
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

          return (VertexObjectDAOIF) new ResultSetConverter().buildDAO(element);
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
  public List<Object> query(GraphRequest request, String statement, Map<String, Object> parameters)
  {
    return this.query(request, statement, parameters, (Class<?>) null);
  }

  @Override
  public List<Object> query(GraphRequest request, String statement, Map<String, Object> parameters, Class<?> resultType)
  {
    return this.query(request, statement, parameters, new ResultSetConverter(resultType));
  }

  @Override
  public List<Object> query(GraphRequest request, String statement, Map<String, Object> parameters, ResultSetConverterIF converter)
  {
    List<Object> results = new LinkedList<Object>();

    OrientDBRequest orientDBRequest = (OrientDBRequest) request;

    ODatabaseSession db = orientDBRequest.getODatabaseSession();

    if (converter == null)
    {
      converter = new ResultSetConverter();
    }

    /*
     * Convert geometry parameters
     */
    Set<Entry<String, Object>> entries = parameters.entrySet();

    for (Entry<String, Object> entry : entries)
    {
      Object value = entry.getValue();

      if (value instanceof Geometry)
      {
        ODocument docVal = geometryToDocument((Geometry) value);

        if (docVal != null)
        {
          entry.setValue(docVal);
        }
      }
    }

    try (OResultSet rs = db.query(statement, parameters))
    {
      while (rs.hasNext())
      {
        Object converted = converter.convert(request, rs.next());

        if (converted != null)
        {
          results.add(converted);
        }
      }
    }

    return results;
  }

  @Override
  public void command(GraphRequest request, String statement, Map<String, Object> parameters)
  {
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
        entry.setValue(geometryToDocument((Geometry) value));
      }
    }

    try (OResultSet command = db.command(statement, parameters))
    {
      // Do nothing
    }
  }

  public void addEdge(GraphRequest request, EdgeObjectDAO edgeDAO)
  {
    MdEdgeDAOIF mdEdge = edgeDAO.getMdGraphClassDAO();

    String edgeClass = mdEdge.getValue(MdEdgeInfo.DB_CLASS_NAME);

    OrientDBRequest orientDBRequest = (OrientDBRequest) request;

    ODatabaseSession db = orientDBRequest.getODatabaseSession();
    OVertex pVertex = db.load((ORID) edgeDAO.getParent().getRID());
    OVertex cVertex = db.load((ORID) edgeDAO.getChild().getRID());

    OEdge edge = pVertex.addEdge(cVertex, edgeClass);

    this.populateElement(db, edgeDAO, edge);

    edge.save();

    new ResultSetConverter().populateDAO(edge, edgeDAO);
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
    LinkedList<VertexObjectDAOIF> list = new LinkedList<VertexObjectDAOIF>();
    String edgeClass = mdEdge.getValue(MdEdgeInfo.DB_CLASS_NAME);

    OrientDBRequest orientDBRequest = (OrientDBRequest) request;

    ODatabaseSession db = orientDBRequest.getODatabaseSession();
    OVertex vertex = db.load((ORID) vertexDAO.getRID());

    if (vertex != null)
    {
      Iterable<OVertex> targets = vertex.getVertices(direction, edgeClass);

      ResultSetConverter converter = new ResultSetConverter();
      for (OVertex target : targets)
      {
        list.add((VertexObjectDAOIF) converter.buildDAO(target));
      }
    }

    return list;
  }

  @Override
  public List<EdgeObjectDAOIF> getChildEdges(GraphRequest request, VertexObjectDAO vertexDAO, MdEdgeDAOIF mdEdge)
  {
    return this.getEdges(request, vertexDAO, ODirection.OUT, mdEdge);
  }

  @Override
  public List<EdgeObjectDAOIF> getParentEdges(GraphRequest request, VertexObjectDAOIF vertexDAO, MdEdgeDAOIF mdEdge)
  {
    return this.getEdges(request, vertexDAO, ODirection.IN, mdEdge);
  }

  @Override
  public List<EdgeObjectDAOIF> getEdges(GraphRequest request, VertexObjectDAOIF parent, VertexObjectDAOIF child, MdEdgeDAOIF mdEdge)
  {
    List<EdgeObjectDAOIF> results = new LinkedList<EdgeObjectDAOIF>();

    OrientDBRequest orientDBRequest = (OrientDBRequest) request;

    ODatabaseSession db = orientDBRequest.getODatabaseSession();

    String statement = "SELECT FROM " + mdEdge.getDBClassName() + " WHERE out = ? AND in = ?";

    ResultSetConverter converter = new ResultSetConverter();
    try (OResultSet rs = db.query(statement, parent.getRID(), child.getRID()))
    {
      while (rs.hasNext())
      {
        OResult result = rs.next();

        if (result.isElement())
        {
          OElement element = result.toElement();

          results.add((EdgeObjectDAOIF) converter.buildDAO(element));
        }
        else
        {
          throw new UnsupportedOperationException("Unexpected result type");
        }
      }
    }

    return results;
  }

  private List<EdgeObjectDAOIF> getEdges(GraphRequest request, VertexObjectDAOIF vertexDAO, ODirection direction, MdEdgeDAOIF mdEdge)
  {
    LinkedList<EdgeObjectDAOIF> list = new LinkedList<EdgeObjectDAOIF>();
    String edgeClass = mdEdge.getValue(MdEdgeInfo.DB_CLASS_NAME);

    OrientDBRequest orientDBRequest = (OrientDBRequest) request;

    ODatabaseSession db = orientDBRequest.getODatabaseSession();
    OVertex vertex = db.load((ORID) vertexDAO.getRID());

    if (vertex != null)
    {
      Iterable<OEdge> targets = vertex.getEdges(direction, edgeClass);

      ResultSetConverter converter = new ResultSetConverter();
      for (OEdge target : targets)
      {
        list.add((EdgeObjectDAOIF) converter.buildDAO(target));
      }
    }

    return list;
  }

  private ODocument geometryToDocument(Geometry geom)
  {
    // The OrientDB spatial library has a bug where if the geometry is empty it
    // will detect the type incorrectly.
    // You can see this here:
    // https://github.com/orientechnologies/orientdb-spatial/blob/514fe655155f7f1b3db53b79e784b8919f841156/src/main/java/com/orientechnologies/spatial/shape/OComplexShapeBuilder.java#L113
    // Where if the collection is an empty array it just always returns true
    // We're hacking around this here by just setting null if it's empty.
    if (geom != null && geom.isEmpty())
    {
      geom = null;
    }

    if (geom == null)
    {
      return null;
    }

    ODocument document = OShapeFactory.INSTANCE.toDoc(geom);

    return document;
  }

  protected void populateElement(ODatabaseSession db, GraphObjectDAO graphObjectDAO, OElement element)
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

        element.setProperty(columnName, geometryToDocument(value));

        if (mdClass.isEnableChangeOverTime())
        {
          this.populateGeometryChangeOverTime(db, element, attribute, this.getDbColumnType(mdAttribute), columnName);
        }
      }
      else if (mdAttribute instanceof MdAttributeEmbeddedDAOIF)
      {
        MdAttributeEmbeddedDAOIF mdAttributeEmbedded = (MdAttributeEmbeddedDAOIF) mdAttribute;
        String embeddedClassName = ( (MdGraphClassDAOIF) mdAttributeEmbedded.getEmbeddedMdClassDAOIF() ).getDBClassName();
        String columnName = mdAttributeEmbedded.getColumnName();

        OElement innerElement = null;

        AttributeEmbedded attributeEmbedded = (AttributeEmbedded) attribute;
        GraphObjectDAO embeddedObject = (GraphObjectDAO) attributeEmbedded.getObjectValue();

        if (embeddedObject != null)
        {
          OElement oElement = element.getProperty(columnName);

          if (oElement == null)
          {
            innerElement = db.newElement(embeddedClassName);
          }
          else if (oElement instanceof OElement)
          {
            innerElement = (OElement) oElement;
          }

          if (innerElement != null)
          {
            // persist the values on the inner embedded object
            this.populateElement(db, embeddedObject, innerElement);
          }
        }
        // else embeddedObject == null- if there is no embedded object, then set
        // the propperty to null

        element.setProperty(columnName, innerElement);

        if (mdClass.isEnableChangeOverTime())
        {
          this.populateEmbeddedChangeOvertTime(db, element, attribute, embeddedClassName, columnName);
        }
      }
      else if (mdAttribute instanceof MdAttributeEnumerationDAOIF)
      {
        Set<String> value = ( (AttributeEnumeration) attribute ).getObjectValue();
        String columnName = mdAttribute.getColumnName();

        if (value.size() > 0)
        {
          element.setProperty(columnName, value);
        }
        else
        {
          element.setProperty(columnName, null);
        }

        if (mdClass.isEnableChangeOverTime())
        {
          this.populateEnumChangeOverTime(db, element, attribute, columnName);
        }
      }
      else if (mdAttribute instanceof MdAttributeGraphRefDAOIF)
      {
        ID id = ( (AttributeGraphRef) attribute ).getRID();
        String columnName = mdAttribute.getColumnName();

        if (id != null)
        {
          element.setProperty(columnName, id.getRid());
        }
        else
        {
          element.setProperty(columnName, null);
        }

        if (mdClass.isEnableChangeOverTime())
        {
          this.populateLinkChangeOverTime(db, element, (AttributeGraphRef) attribute, columnName);
        }
      }
      else
      {
        String columnName = mdAttribute.getColumnName();

        element.setProperty(columnName, attribute.getObjectValue());

        if (mdClass.isEnableChangeOverTime())
        {
          this.populateChangeOverTime(db, element, attribute, columnName);
        }
      }
    }
  }

  protected void populateChangeOverTime(ODatabaseSession db, OElement element, Attribute attribute, String columnName)
  {
    ValueOverTimeCollection valuesOverTime = attribute.getValuesOverTime();
    valuesOverTime.validate();
    List<OElement> documents = new LinkedList<OElement>();

    for (ValueOverTime vot : valuesOverTime)
    {
      if (vot.getValue() != null)
      {
        OElement document = db.newElement(OrientDBConstant.CHANGE_OVER_TIME);
        document.setProperty(OrientDBConstant.START_DATE, vot.getStartDate());
        document.setProperty(OrientDBConstant.END_DATE, vot.getEndDate());
        document.setProperty(OrientDBConstant.VALUE, vot.getValue());
        document.setProperty(OrientDBConstant.OID, vot.getOid());

        documents.add(document);
      }
    }

    element.setProperty(columnName + OrientDBConstant.COT_SUFFIX, documents);
  }

  protected void populateLinkChangeOverTime(ODatabaseSession db, OElement element, AttributeGraphRef attribute, String columnName)
  {
    ValueOverTimeCollection valuesOverTime = attribute.getValuesOverTime();
    valuesOverTime.validate();
    List<OElement> documents = new LinkedList<OElement>();

    for (ValueOverTime vot : valuesOverTime)
    {
      Object value = vot.getValue();

      if (value != null)
      {

        OElement document = db.newElement(OrientDBConstant.CHANGE_OVER_TIME);
        document.setProperty(OrientDBConstant.START_DATE, vot.getStartDate());
        document.setProperty(OrientDBConstant.END_DATE, vot.getEndDate());
        document.setProperty(OrientDBConstant.OID, vot.getOid());

        if (value instanceof ID)
        {
          document.setProperty(OrientDBConstant.VALUE, ( (ID) value ).getRid());
        }
        else
        {
          String oid = (String) value;

          if (attribute.getId() != null && attribute.getValue().equals(oid))
          {
            document.setProperty(OrientDBConstant.VALUE, attribute.getId().getRid());
          }
          else
          {
            VertexObjectDAOIF v = attribute.dereference((String) value);

            document.setProperty(OrientDBConstant.VALUE, v.getRID());
          }
        }

        documents.add(document);
      }
    }

    element.setProperty(columnName + OrientDBConstant.COT_SUFFIX, documents);
  }

  protected void populateEnumChangeOverTime(ODatabaseSession db, OElement element, Attribute attribute, String columnName)
  {
    ValueOverTimeCollection valuesOverTime = attribute.getValuesOverTime();
    valuesOverTime.validate();
    List<OElement> documents = new LinkedList<OElement>();

    for (ValueOverTime vot : valuesOverTime)
    {
      if (vot.getValue() != null)
      {
        OElement document = db.newElement(OrientDBConstant.ENUM_CHANGE_OVER_TIME);
        document.setProperty(OrientDBConstant.START_DATE, vot.getStartDate());
        document.setProperty(OrientDBConstant.END_DATE, vot.getEndDate());
        document.setProperty(OrientDBConstant.VALUE, vot.getValue());
        document.setProperty(OrientDBConstant.OID, vot.getOid());

        documents.add(document);
      }
    }

    element.setProperty(columnName + OrientDBConstant.COT_SUFFIX, documents);
  }

  protected void populateGeometryChangeOverTime(ODatabaseSession db, OElement element, Attribute attribute, String geometryClassName, String columnName)
  {
    ValueOverTimeCollection valuesOverTime = attribute.getValuesOverTime();
    valuesOverTime.validate();
    List<OElement> documents = new LinkedList<OElement>();

    for (ValueOverTime vot : valuesOverTime)
    {
      OElement document = db.newElement(geometryClassName + OrientDBConstant.COT_SUFFIX);
      document.setProperty(OrientDBConstant.START_DATE, vot.getStartDate());
      document.setProperty(OrientDBConstant.END_DATE, vot.getEndDate());
      document.setProperty(OrientDBConstant.OID, vot.getOid());

      ODocument docVal = geometryToDocument((Geometry) vot.getValue());

      if (docVal != null)
      {
        document.setProperty(OrientDBConstant.VALUE, docVal);
      }

      documents.add(document);
    }

    element.setProperty(columnName + OrientDBConstant.COT_SUFFIX, documents);
  }

  protected void populateEmbeddedChangeOvertTime(ODatabaseSession db, OElement element, Attribute attribute, String embeddedClassName, String columnName)
  {
    ValueOverTimeCollection valuesOverTime = attribute.getValuesOverTime();
    valuesOverTime.validate();
    List<OElement> documents = new LinkedList<OElement>();

    for (ValueOverTime vot : valuesOverTime)
    {
      if (vot.getValue() != null)
      {
        OElement votVertex = db.newElement(embeddedClassName);
        this.populateElement(db, (GraphObjectDAO) vot.getValue(), votVertex);

        OElement document = db.newElement(embeddedClassName + OrientDBConstant.COT_SUFFIX);
        document.setProperty(OrientDBConstant.START_DATE, vot.getStartDate());
        document.setProperty(OrientDBConstant.END_DATE, vot.getEndDate());
        document.setProperty(OrientDBConstant.VALUE, votVertex);
        document.setProperty(OrientDBConstant.OID, vot.getOid());

        documents.add(document);
      }
    }

    element.setProperty(columnName + OrientDBConstant.COT_SUFFIX, documents);
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

      List<MdAttributeDAOIF> mdAttributes = new LinkedList<MdAttributeDAOIF>();
      mdAttributes.add(mdAttribute);

      List<String> valueList = new LinkedList<String>();
      valueList.add(value);

      throw new DuplicateDataException(devMsg, runEx, mdGraphClass, mdAttributes, valueList);
    }
    else
    {
      throw runEx;
    }
  }

  // protected OSequence getSequence(ODatabaseSession db, GraphObjectDAO
  // graphObjectDAO)
  // {
  // MdGraphClassDAOIF mdGraphClassDAO = graphObjectDAO.getMdGraphClassDAO();
  // String className = mdGraphClassDAO.getDBClassName();
  //
  // return db.getMetadata().getSequenceLibrary().getSequence(className +
  // "Seq");
  // }

  public static String generateIndexName()
  {
    return ( "i" + UUID.randomUUID().toString().replaceAll("-", "") ).substring(0, 30);
  }

  public static OClass getOrCreateChangeOverTime(ODatabaseSession db)
  {
    OClass oClass = db.getClass(OrientDBConstant.CHANGE_OVER_TIME);

    if (oClass == null)
    {
      oClass = db.createClass(OrientDBConstant.CHANGE_OVER_TIME);
      oClass.createProperty(OrientDBConstant.START_DATE, OType.DATETIME);
      oClass.createProperty(OrientDBConstant.END_DATE, OType.DATETIME);
      oClass.createProperty(OrientDBConstant.VALUE, OType.ANY);
      oClass.createProperty(OrientDBConstant.OID, OType.STRING);
    }

    return oClass;
  }

  public static OClass getOrCreateEnumerationChangeOverTime(ODatabaseSession db)
  {
    OClass oClass = db.getClass(OrientDBConstant.ENUM_CHANGE_OVER_TIME);

    if (oClass == null)
    {
      oClass = db.createClass(OrientDBConstant.ENUM_CHANGE_OVER_TIME);
      oClass.createProperty(OrientDBConstant.START_DATE, OType.DATETIME);
      oClass.createProperty(OrientDBConstant.END_DATE, OType.DATETIME);
      oClass.createProperty(OrientDBConstant.VALUE, OType.EMBEDDEDSET, OType.STRING);
      oClass.createProperty(OrientDBConstant.OID, OType.STRING);
    }

    return oClass;
  }

  public static OClass getOrCreateChangeOverTime(ODatabaseSession db, OClass embeddedClass, OType type)
  {
    OClass oClass = db.getClass(embeddedClass.getName() + OrientDBConstant.COT_SUFFIX);

    if (oClass == null)
    {
      oClass = db.createClass(embeddedClass.getName() + OrientDBConstant.COT_SUFFIX);
      oClass.createProperty(OrientDBConstant.START_DATE, OType.DATETIME);
      oClass.createProperty(OrientDBConstant.END_DATE, OType.DATETIME);
      oClass.createProperty(OrientDBConstant.VALUE, type, embeddedClass);
      oClass.createProperty(OrientDBConstant.OID, OType.STRING);
    }

    return oClass;
  }

}
