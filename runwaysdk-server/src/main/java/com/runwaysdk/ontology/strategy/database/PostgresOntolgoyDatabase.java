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
/**
 * 
 */
package com.runwaysdk.ontology.strategy.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.DatabaseException;
import com.runwaysdk.dataaccess.database.general.PostgreSQL;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdEntity;
import com.runwaysdk.system.metadata.MdRelationship;
import com.runwaysdk.system.metadata.MdTerm;
import com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategy;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
public class PostgresOntolgoyDatabase implements OntologyDatabase
{
  private static Logger          log = LoggerFactory.getLogger(PostgresOntolgoyDatabase.class);

  /**
   * The standard newline for readable printing.
   */
  private static final String NL  = "\n";

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.system.metadata.ontology.OntologyDatabase#rebuild(com.runwaysdk .system.metadata.MdTerm, com.runwaysdk.system.metadata.MdRelationship, com.runwaysdk.system.metadata.MdBusiness)
   */
  public void rebuild(Map<String, Object> parameters)
  {
    MdTerm termDomain = (MdTerm) this.getParameter(parameters, DatabaseAllPathsStrategy.TERM_PARAMETER);
    MdRelationship termRelationship = (MdRelationship) this.getParameter(parameters, DatabaseAllPathsStrategy.TERM_RELATIONSHIP_PARAMETER);
    MdBusiness termAllPaths = (MdBusiness) this.getParameter(parameters, DatabaseAllPathsStrategy.ALL_PATHS_PARAMETER);

    String allpathsTable = termAllPaths.getTableName();
    String allPathsRootTypeId = this.getAllPathsTypeIdRoot(termAllPaths);

    // Create the INSERT structure. Preserve column order so the values can
    // be appropriately matched.
    String oid = getColumn(termAllPaths, MetadataInfo.OID);
    String siteMaster = getColumn(termAllPaths, MetadataInfo.SITE_MASTER);
    String createdBy = getColumn(termAllPaths, MetadataInfo.CREATED_BY);
    String key = getColumn(termAllPaths, MetadataInfo.KEY);
    String type = getColumn(termAllPaths, MetadataInfo.TYPE);
    String domain = getColumn(termAllPaths, MetadataInfo.DOMAIN);
    String lastUpdateDate = getColumn(termAllPaths, MetadataInfo.LAST_UPDATE_DATE);
    String sequence = getColumn(termAllPaths, MetadataInfo.SEQUENCE);
    String lockedBy = getColumn(termAllPaths, MetadataInfo.LOCKED_BY);
    String createDate = getColumn(termAllPaths, MetadataInfo.CREATE_DATE);
    String owner = getColumn(termAllPaths, MetadataInfo.OWNER);
    String lastUpdatedBy = getColumn(termAllPaths, MetadataInfo.LAST_UPDATED_BY);
    String parentTerm = getColumn(termAllPaths, DatabaseAllPathsStrategy.PARENT_TERM_ATTR);
    String childTerm = getColumn(termAllPaths, DatabaseAllPathsStrategy.CHILD_TERM_ATTR);
//    String sequenceName = this.getSequenceName(termAllPaths);

    String[] metadataColumns = new String[] { oid, siteMaster, key, type, domain, lastUpdateDate, sequence, createdBy, lockedBy, createDate, owner, lastUpdatedBy, parentTerm, childTerm };

    String insertColumns = StringUtils.join(metadataColumns, "," + NL);

    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO " + allpathsTable + " (" + insertColumns + ") " + NL);

    // Create the recursive WITH clause
    String originalChild = "original_child";
    String view = "quick_paths";
    String relationshipTable = termRelationship.getTableName();

    sql.append("WITH RECURSIVE " + view + " (" + originalChild + ") AS (" + NL);
    sql.append("  SELECT " + RelationshipDAOIF.CHILD_OID_COLUMN + " AS " + originalChild + ", " + RelationshipDAOIF.PARENT_OID_COLUMN + NL);
    sql.append("  FROM " + relationshipTable + NL);
    sql.append("  UNION" + NL);
    sql.append("  SELECT " + originalChild + ", l." + RelationshipDAOIF.PARENT_OID_COLUMN + NL);
    sql.append("  FROM " + relationshipTable + " l" + NL);
    sql.append("  INNER JOIN " + view + " ON (l." + RelationshipDAOIF.CHILD_OID_COLUMN + " = " + view + "." + RelationshipDAOIF.PARENT_OID_COLUMN + ")" + NL);
    sql.append(")" + NL);

    // Create the primary SELECT body
    String domainTable = termDomain.getTableName();

    // non-term values
    Timestamp transactionDate = new Timestamp(new Date().getTime());
    String siteMasterValue = CommonProperties.getDomain();
    SessionIF sessionIF = Session.getCurrentSession();
    String createdById = sessionIF != null ? sessionIF.getUser().getOid() : ServerConstants.SYSTEM_USER_ID;

    sql.append("SELECT" + NL);
    
    //    uuid(concat(substring(uuid_generate_v3(uuid_ns_url(), (p.oid::text || c.oid::text))::text from 0 for 31), '5b04')) AS oid,


    // standard metadata fields
    sql.append("  uuid(concat(substring(uuid_generate_v3(uuid_ns_url(), (p." + oid + "::text || c." + oid + "::text))::text from 0 for 31), '" + allPathsRootTypeId + "')) AS " + oid + "," + NL);
    sql.append("  '" + siteMasterValue + "'  AS " + siteMaster + "," + NL);
    sql.append("  uuid(concat(substring(uuid_generate_v3(uuid_ns_url(), (p." + oid + "::text || c." + oid + "::text))::text from 0 for 31), '" + allPathsRootTypeId + "')) AS " + key + "," + NL);
    sql.append("  '" + termAllPaths.definesType() + "' AS " + type + "," + NL);
    sql.append("  NULL AS " + domain + "," + NL);
    sql.append("  ? AS " + lastUpdateDate + "," + NL);
    sql.append("  NEXTVAL('" + PostgreSQL.OBJECT_UPDATE_SEQUENCE + "') AS " + sequence + "," + NL);
    sql.append("  '" + createdById + "'  AS " + createdBy + "," + NL);
    sql.append("  NULL AS " + lockedBy + "," + NL);
    sql.append("  ? AS " + createDate + "," + NL);
    sql.append("  '" + createdById + "' AS " + owner + "," + NL);
    sql.append("  '" + createdById + "' AS " + lastUpdateDate + "," + NL);

    // parent term
    sql.append("  paths." + RelationshipInfo.PARENT_OID + " AS " + parentTerm + "," + NL);

    // child term
    sql.append("  paths." + originalChild + " AS " + childTerm + NL);

    sql.append("FROM " + domainTable + " as p, " + NL);
    sql.append(domainTable + " as c," + NL);
    sql.append("(SELECT " + originalChild + ", " + RelationshipInfo.PARENT_OID + " FROM " + view + " UNION SELECT " + oid + "," + oid + " FROM " + domainTable + " ) AS paths" + NL);

    sql.append("WHERE p." + oid + " = paths." + RelationshipInfo.PARENT_OID + " AND c." + oid + " = paths." + originalChild + ";" + NL);

    int afterCount = this.execute(sql.toString(), transactionDate, transactionDate);

    if (log.isDebugEnabled())
    {
      log.debug("The type [" + termAllPaths + "] had [" + afterCount + "] objects in table [" + allpathsTable + "] AFTER a complete allpaths rebuild.");
    }
  }

  /**
   * @param parameters
   * @param parameterName
   * @return
   */
  private Object getParameter(Map<String, Object> parameters, String parameterName)
  {
    if (parameters.containsKey(parameterName))
    {
      return parameters.get(parameterName);
    }

    throw new ProgrammingErrorException("Strategy did not provide the correct parameters.  Expecting parameter of name [" + parameterName + "]");
  }

  /*
   * Copies the allpath entries of the parent and adds it to the child.
   * 
   * @ PRECONDITION The child has been added to the allpaths strategy, i.e. there is a record from child -> child.
   */
  @Override
  public void copyTerm(Map<String, Object> parameters)
  {
    Term parent = (Term) this.getParameter(parameters, DatabaseAllPathsStrategy.PARENT_PARAMETER);
    Term child = (Term) this.getParameter(parameters, DatabaseAllPathsStrategy.CHILD_PARAMETER);
    MdBusiness allPaths = (MdBusiness) this.getParameter(parameters, DatabaseAllPathsStrategy.ALL_PATHS_PARAMETER);

    String tableName = allPaths.getTableName();
    String oid = getColumn(allPaths, MetadataInfo.OID);
    String siteMaster = getColumn(allPaths, MetadataInfo.SITE_MASTER);
    String createdBy = getColumn(allPaths, MetadataInfo.CREATED_BY);
    String key = getColumn(allPaths, MetadataInfo.KEY);
    String type = getColumn(allPaths, MetadataInfo.TYPE);
    String domain = getColumn(allPaths, MetadataInfo.DOMAIN);
    String lastUpdateDate = getColumn(allPaths, MetadataInfo.LAST_UPDATE_DATE);
    String sequence = getColumn(allPaths, MetadataInfo.SEQUENCE);
    String lockedBy = getColumn(allPaths, MetadataInfo.LOCKED_BY);
    String createDate = getColumn(allPaths, MetadataInfo.CREATE_DATE);
    String owner = getColumn(allPaths, MetadataInfo.OWNER);
    String lastUpdatedBy = getColumn(allPaths, MetadataInfo.LAST_UPDATED_BY);
    String parentTerm = getColumn(allPaths, DatabaseAllPathsStrategy.PARENT_TERM_ATTR);
    String childTerm = getColumn(allPaths, DatabaseAllPathsStrategy.CHILD_TERM_ATTR);
    String allPathsRootTypeId = this.getAllPathsTypeIdRoot(allPaths);
//    String sequenceName = this.getSequenceName(allPaths);

    String createdById = new String();
    SessionIF sessionIF = Session.getCurrentSession();
    if (sessionIF != null)
    {
      createdById = sessionIF.getUser().getOid();
    }
    else
    {
      createdById = ServerConstants.SYSTEM_USER_ID;
    }

    // non-term values
    Timestamp transactionDate = new Timestamp(new Date().getTime());

    String[] metadataColumns = new String[] { oid, siteMaster, key, type, domain, lastUpdateDate, sequence, createdBy, lockedBy, createDate, owner, lastUpdatedBy, parentTerm, childTerm };

    String insertColumns = StringUtils.join(metadataColumns, "," + NL);

    String childOid = child.getOid();
    String parentOid = parent.getOid();

//    uuid(concat(substring(uuid_generate_v3(uuid_ns_url(), (p.oid::text || c.oid::text))::text from 0 for 33), '5b04')) AS oid,

    String identifierSQL = "uuid(concat(substring(uuid_generate_v3(uuid_ns_url(), (allpaths_parent." + parentTerm + "::text || allpaths_child." + childTerm + "::text))::text from 0 for 31), '" + allPathsRootTypeId + "'))";

    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO " + tableName + " (" + insertColumns + ") " + NL);
    sql.append(" SELECT " + NL);
    sql.append("   " + identifierSQL + " AS newId," + NL);
    sql.append("   '" + CommonProperties.getDomain() + "' AS " + siteMaster + "," + NL);
    sql.append("   " + identifierSQL + " AS newKey," + NL);
    sql.append("    '" + allPaths.definesType() + "' AS \"" + type + "\"," + NL);
    sql.append("    NULL AS " + domain + "," + NL);
    sql.append("    ? AS " + lastUpdateDate + "," + NL);
    sql.append("    NEXTVAL('" + PostgreSQL.OBJECT_UPDATE_SEQUENCE + "') AS " + sequence + "," + NL);
    sql.append("    '" + createdById + "' AS " + createdBy + "," + NL);
    sql.append("    NULL AS " + lockedBy + "," + NL);
    sql.append("    ? AS " + createDate + "," + NL);
    sql.append("    '" + createdById + "' AS \"" + owner + "\"," + NL);
    sql.append("    '" + createdById + "' AS " + lastUpdatedBy + "," + NL);
    sql.append("    allpaths_parent." + parentTerm + " AS " + parentTerm + ", " + NL);
    sql.append("    allpaths_child." + childTerm + "   AS " + childTerm + NL);

    sql.append(" FROM " + NL);
    // Fech all of the recursive children of the given child term, including
    // the child term itself.
    sql.append("  (SELECT " + childTerm + " " + NL);
    sql.append("    FROM " + tableName + " " + NL);
    sql.append("    WHERE " + parentTerm + " = '" + childOid + "' ) AS allpaths_child, " + NL);
    // Fech all of the recursive parents of the given new parent term,
    // including the new parent term itself.
    sql.append("  (SELECT " + parentTerm + " " + NL);
    sql.append("     FROM " + tableName + " " + NL);
    sql.append("     WHERE " + childTerm + " = '" + parentOid + "' " + NL + "    ) AS allpaths_parent " + NL);
    // Since a term can have multiple parents, a path to one of the new
    // parent's parents may already exist
    sql.append(" WHERE allpaths_parent." + parentTerm + " NOT IN " + NL);
    sql.append("   (SELECT " + parentTerm + " " + NL);
    sql.append("      FROM " + tableName + " " + NL);
    sql.append("      WHERE " + parentTerm + " = allpaths_parent." + parentTerm + " " + NL);
    sql.append("      AND " + childTerm + " = allpaths_child." + childTerm + ") " + NL);

    Connection conn = Database.getConnection();

    PreparedStatement prepared = null;

    try
    {
      prepared = conn.prepareStatement(sql.toString());
      prepared.setTimestamp(1, new Timestamp(transactionDate.getTime()));
      prepared.setTimestamp(2, new Timestamp(transactionDate.getTime()));
      prepared.executeUpdate();
    }
    catch (SQLException e)
    {
      throw new ProgrammingErrorException(e);
    }
    finally
    {
      if (prepared != null)
      {
        try
        {
          prepared.close();
        }
        catch (SQLException e)
        {
          throw new ProgrammingErrorException(e);
        }
      }
    }
  }

  /**
   * Returns the last 32 characters of the MdBusiness that defines the allpaths metadata. This is used for rapid oid creation.
   * 
   * @return
   */
  private String getAllPathsTypeIdRoot(MdBusiness allPaths)
  {
//    return IdParser.parseRootFromId(allPaths.getOid());
    return ((MdBusinessDAOIF)BusinessFacade.getEntityDAO(allPaths)).getRootId();
  }

  /**
   * Returns the column name of the attribute on the type.
   * 
   * @param md
   * @param attribute
   * @return
   */
  private String getColumn(MdEntity md, String attribute)
  {
    MdEntityDAOIF mdDAO = (MdEntityDAOIF) BusinessFacade.getEntityDAO(md);
    return mdDAO.definesAttribute(attribute).getColumnName();
  }

  /**
   * Executes the given SQL and manages the connection. This takes in a variable number of prepared statement arguments that are assigned in order: <code>
   * preparedStatement.setObject(1, args[0]);
   * preparedStatement.setObject(2, args[1]);
   * </code> ... and so on.
   * 
   * @param sql
   * @param args
   * @return The number of rows updated
   */
  private int execute(String sql, Object... args)
  {
    Connection conn = Database.getConnection();

    PreparedStatement prepared = null;

    try
    {
      prepared = conn.prepareStatement(sql);

      // prepared statements start counting at 1, not 0.
      int queryIndex = 1;
      for (Object arg : args)
      {
        prepared.setObject(queryIndex++, arg);
      }

      return prepared.executeUpdate();
    }
    catch (SQLException e)
    {
      throw new DatabaseException(e);
    }
    finally
    {
      if (prepared != null)
      {
        try
        {
          prepared.close();
        }
        catch (SQLException e)
        {
          throw new DatabaseException(e);
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.system.metadata.ontology.OntologyDatabase#initialize(java.util.Map)
   */
  @Override
  public void initialize(Map<String, Object> parameters)
  {
    MdBusiness allPaths = (MdBusiness) this.getParameter(parameters, DatabaseAllPathsStrategy.ALL_PATHS_PARAMETER);
    MdBusinessDAOIF allpathsDAO = MdBusinessDAO.get(allPaths.getOid());

    String sequenceName = this.getSequenceName(allPaths);

    List<String> statements = new LinkedList<String>();
    statements.add("CREATE SEQUENCE IF NOT EXISTS " + sequenceName + " INCREMENT 1 START " + Database.STARTING_SEQUENCE_NUMBER);
    
    String allPathsTN = allPaths.getTableName();
    String childCol = allpathsDAO.definesAttribute(DatabaseAllPathsStrategy.CHILD_TERM_ATTR).getColumnName();
    String parentCol = allpathsDAO.definesAttribute(DatabaseAllPathsStrategy.PARENT_TERM_ATTR).getColumnName();
    statements.add("ALTER TABLE " + allPathsTN + " DROP CONSTRAINT IF EXISTS " + allPathsTN + "_unique_constraint");
    statements.add("ALTER TABLE " + allPathsTN + " ADD CONSTRAINT " + allPathsTN + "_unique_constraint UNIQUE(" + parentCol + ", " + childCol + ")");

    Database.executeBatch(statements);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.system.metadata.ontology.OntologyDatabase#shutdown(java.util.HashMap)
   */
  @Override
  public void shutdown(Map<String, Object> parameters)
  {
    MdBusiness allPaths = (MdBusiness) this.getParameter(parameters, DatabaseAllPathsStrategy.ALL_PATHS_PARAMETER);
    String sequenceName = this.getSequenceName(allPaths);

    List<String> statements = new LinkedList<String>();
    statements.add("DROP SEQUENCE IF EXISTS " + sequenceName);

    Database.executeBatch(statements);
  }

  /**
   * @param allPaths
   * @return
   */
  private String getSequenceName(MdBusiness allPaths)
  {
    return allPaths.getTableName() + "_sequence";
  }

}
