/**
 * 
 */
package com.runwaysdk.system.metadata.ontology;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.DatabaseException;
import com.runwaysdk.dataaccess.database.general.PostgreSQL;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdEntity;
import com.runwaysdk.system.metadata.MdRelationship;
import com.runwaysdk.system.metadata.MdTerm;
import com.runwaysdk.util.IdParser;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
 ******************************************************************************/
public class PostgresOntolgoyDatabase implements OntologyDatabase
{
  private static Log          log = LogFactory.getLog(PostgresOntolgoyDatabase.class);

  /**
   * The standard newline for readable printing.
   */
  private static final String NL  = "\n";

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.system.metadata.ontology.OntologyDatabase#rebuild(com.runwaysdk
   * .system.metadata.MdTerm, com.runwaysdk.system.metadata.MdRelationship,
   * com.runwaysdk.system.metadata.MdBusiness)
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
    String id = getColumn(termAllPaths, MetadataInfo.ID);
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

    String[] metadataColumns = new String[] { id, siteMaster, key, type, domain, lastUpdateDate, sequence, createdBy, lockedBy, createDate, owner, lastUpdatedBy, parentTerm, childTerm };

    String insertColumns = StringUtils.join(metadataColumns, "," + NL);

    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO " + allpathsTable + " (" + insertColumns + ") " + NL);

    // Create the recursive WITH clause
    String originalChild = "original_child";
    String view = "quick_paths";
    String relationshipTable = termRelationship.getTableName();

    sql.append("WITH RECURSIVE " + view + " (" + originalChild + ") AS (" + NL);
    sql.append("  SELECT " + RelationshipDAOIF.CHILD_ID_COLUMN + " AS " + originalChild + ", " + RelationshipDAOIF.PARENT_ID_COLUMN + NL);
    sql.append("  FROM " + relationshipTable + NL);
    sql.append("  UNION" + NL);
    sql.append("  SELECT " + originalChild + ", l." + RelationshipDAOIF.PARENT_ID_COLUMN + NL);
    sql.append("  FROM " + relationshipTable + " l" + NL);
    sql.append("  INNER JOIN " + view + " ON (l." + RelationshipDAOIF.CHILD_ID_COLUMN + " = " + view + "." + RelationshipDAOIF.PARENT_ID_COLUMN + ")" + NL);
    sql.append(")" + NL);

    // Create the primary SELECT body
    String domainTable = termDomain.getTableName();

    // non-term values
    Timestamp transactionDate = new Timestamp(new Date().getTime());
    String siteMasterValue = CommonProperties.getDomain();
    SessionIF sessionIF = Session.getCurrentSession();
    String createdById = sessionIF != null ? sessionIF.getUser().getId() : ServerConstants.SYSTEM_USER_ID;

    sql.append("SELECT" + NL);

    // standard metadata fields
    sql.append("  MD5(p." + id + " || c." + id + " ) || '" + allPathsRootTypeId + "' AS " + id + "," + NL);
    sql.append("  '" + siteMasterValue + "'  AS " + siteMaster + "," + NL);
    sql.append("  MD5(p." + id + " || c." + id + " ) || '" + allPathsRootTypeId + "' AS " + key + "," + NL);
    sql.append("  '" + termAllPaths.definesType() + "' AS " + type + "," + NL);
    sql.append("  '' AS " + domain + "," + NL);
    sql.append("  ? AS " + lastUpdateDate + "," + NL);
    sql.append("  NEXTVAL('" + PostgreSQL.UNIQUE_OBJECT_ID_SEQUENCE + "') AS " + sequence + "," + NL);
    sql.append("  '" + createdById + "'  AS " + createdBy + "," + NL);
    sql.append("  NULL AS " + lockedBy + "," + NL);
    sql.append("  ? AS " + createDate + "," + NL);
    sql.append("  '" + createdById + "' AS " + owner + "," + NL);
    sql.append("  '" + createdById + "' AS " + lastUpdateDate + "," + NL);

    // parent term
    sql.append("  paths." + RelationshipInfo.PARENT_ID + " AS " + parentTerm + "," + NL);

    // child term
    sql.append("  paths." + originalChild + " AS " + childTerm + NL);

    sql.append("FROM " + domainTable + " as p, " + NL);
    sql.append(domainTable + " as c," + NL);
    sql.append("(SELECT " + originalChild + ", " + RelationshipInfo.PARENT_ID + " FROM " + view + " UNION SELECT " + id + "," + id + " FROM " + domainTable + " ) AS paths" + NL);

    sql.append("WHERE p." + id + " = paths." + RelationshipInfo.PARENT_ID + " AND c." + id + " = paths." + originalChild + ";" + NL);

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
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.system.metadata.ontology.OntologyDatabase#copyTerm(java.util
   * .Map)
   */
  @Override
  public void copyTerm(Map<String, Object> parameters)
  {
    Term parent = (Term) this.getParameter(parameters, DatabaseAllPathsStrategy.PARENT_PARAMETER);
    Term child = (Term) this.getParameter(parameters, DatabaseAllPathsStrategy.CHILD_PARAMETER);
    MdBusiness allPaths = (MdBusiness) this.getParameter(parameters, DatabaseAllPathsStrategy.ALL_PATHS_PARAMETER);

    String tableName = allPaths.getTableName();
    String id = getColumn(allPaths, MetadataInfo.ID);
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

    String createdById = new String();
    SessionIF sessionIF = Session.getCurrentSession();
    if (sessionIF != null)
    {
      createdById = sessionIF.getUser().getId();
    }
    else
    {
      createdById = ServerConstants.SYSTEM_USER_ID;
    }

    // non-term values
    Timestamp transactionDate = new Timestamp(new Date().getTime());

    String[] metadataColumns = new String[] { id, siteMaster, key, type, domain, lastUpdateDate, sequence, createdBy, lockedBy, createDate, owner, lastUpdatedBy, parentTerm, childTerm };

    String insertColumns = StringUtils.join(metadataColumns, "," + NL);

    String childId = child.getId();
    String parentId = parent.getId();

    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO " + tableName + " (" + insertColumns + ") " + NL);
    sql.append(" SELECT " + NL);
    sql.append("   MD5(allpaths_parent." + parentTerm + " || allpaths_child." + childTerm + " ) || '" + allPathsRootTypeId + "' AS newId," + NL);
    sql.append("   '" + CommonProperties.getDomain() + "' AS " + siteMaster + "," + NL);
    sql.append("   MD5(allpaths_parent." + parentTerm + " || allpaths_child." + childTerm + " ) || '" + allPathsRootTypeId + "' AS newKey," + NL);
    sql.append("    '" + allPaths.definesType() + "' AS \"" + type + "\"," + NL);
    sql.append("    '' AS " + domain + "," + NL);
    sql.append("    ? AS " + lastUpdateDate + "," + NL);
    sql.append("    NEXTVAL('" + PostgreSQL.UNIQUE_OBJECT_ID_SEQUENCE + "') AS " + sequence + "," + NL);
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
    sql.append("    WHERE " + parentTerm + " = '" + childId + "' ) AS allpaths_child, " + NL);
    // Fech all of the recursive parents of the given new parent term,
    // including the new parent term itself.
    sql.append("  (SELECT " + parentTerm + " " + NL);
    sql.append("     FROM " + tableName + " " + NL);
    sql.append("     WHERE " + childTerm + " = '" + parentId + "' " + NL + "    ) AS allpaths_parent " + NL);
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
   * Returns the last 32 characters of the MdBusiness that defines the allpaths
   * metadata. This is used for rapid id creation.
   * 
   * @return
   */
  private String getAllPathsTypeIdRoot(MdBusiness allPaths)
  {
    return IdParser.parseRootFromId(allPaths.getId());
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
   * Executes the given SQL and manages the connection. This takes in a variable
   * number of prepared statement arguments that are assigned in order: <code>
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

}
