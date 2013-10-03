package com.runwaysdk.system.metadata.ontology;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.business.RelationshipQuery;
import com.runwaysdk.business.ontology.MdTermDAO;
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
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.AttributeReference;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.RelationshipDAOQuery;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdEntity;
import com.runwaysdk.system.metadata.MdRelationship;
import com.runwaysdk.util.IdParser;

public class PostgresAllPathsStrategy extends PostgresAllPathsStrategyBase
{
  private static final long serialVersionUID = -672024276;
  
  /**
   * The standard newline for readable printing. TODO use method concatenation
   * method to clean up the code.
   */
  private static final String NL = "\n";
  
  private static Log log = LogFactory.getLog(PostgresAllPathsStrategy.class);
  
  private MdBusiness termAllPaths;
  
  public PostgresAllPathsStrategy()
  {
    super();
  }

  /*
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#initialize()
   */
  @Override
  public void initialize(String relationshipType)
  {
    // TODO : Create the termAllPaths MdBusiness
    
    rebuildAllPaths(relationshipType);
    
    // The super changes the StrategyState
    super.initialize();
  }

  /*
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#shutdown()
   */
  @Override
  public void shutdown()
  {
    // TODO : Delete the termAllPaths MdBusiness
    
    clear();
    
    // The super changes the StrategyState
    super.shutdown();
  }
  
  /**
   * Rebuilds the AllPaths table for an Ontology of a given type.
   */
  @Transaction
  public void rebuildAllPaths(String relationshipType)
  {
    MdBusiness termDomain = this.provider.getTermDomain();
    MdRelationship termRelationship = MdRelationship.getMdRelationship(relationshipType);

    String allpathsTable = termAllPaths.getTableName();
    String allPathsRootTypeId = this.getAllPathsTypeIdRoot();

    // Clear all existing all paths records
    this.clear();

    
    // Create the INSERT structure. Preserve column order so the values can
    // be appropriately matched.
    String sql = "";
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
    String parentTerm = getColumn(termAllPaths, termRelationship.getParentMdBusinessId());
    String parentType = getColumn(termAllPaths, termRelationship.getParentMdBusiness().definesType());
    String childTerm = getColumn(termAllPaths, termRelationship.getChildMdBusinessId());
    String childType = getColumn(termAllPaths, termRelationship.getChildMdBusiness().definesType());

    String[] metadataColumns = new String[] { id, siteMaster, key, type, domain, lastUpdateDate,
        sequence, createdBy, lockedBy, createDate, owner, lastUpdatedBy, parentTerm, parentType,
        childTerm, childType };

    String insertColumns = StringUtils.join(metadataColumns, "," + NL);
    sql += "INSERT INTO " + allpathsTable + " (" + insertColumns + ") " + NL;

    // Create the recursive WITH clause
    String originalChild = "original_child";
    String view = "quick_paths";
    String relationshipTable = termRelationship.getTableName();

    sql += "WITH RECURSIVE " + view + " (" + originalChild + ") AS (" + NL;
    sql += "  SELECT " + RelationshipDAOIF.CHILD_ID_COLUMN + " AS " + originalChild + ", "
        + RelationshipDAOIF.PARENT_ID_COLUMN + NL;
    sql += "  FROM " + relationshipTable + NL;
    sql += "  UNION" + NL;
    sql += "  SELECT " + originalChild + ", l." + RelationshipDAOIF.PARENT_ID_COLUMN + NL;
    sql += "  FROM " + relationshipTable + " l" + NL;
    sql += "  INNER JOIN " + view + " ON (l." + RelationshipDAOIF.CHILD_ID_COLUMN + " = " + view + "."
        + RelationshipDAOIF.PARENT_ID_COLUMN + ")" + NL;
    sql += ")" + NL;

    // Create the primary SELECT body
    String domainTable = termDomain.getTableName();
    String domainType = getColumn(termDomain, this.provider.getDomainType());

    // non-term values
    Timestamp transactionDate = new Timestamp(new Date().getTime());
    String siteMasterValue = CommonProperties.getDomain();
    SessionIF sessionIF = Session.getCurrentSession();
    String createdById = sessionIF != null ? sessionIF.getUser().getId() : ServerConstants.SYSTEM_USER_ID;

    sql += "SELECT" + NL;

    // standard metadata fields
    sql += "  MD5(p." + id + " || c." + id + " ) || '" + allPathsRootTypeId + "' AS " + id + "," + NL;
    sql += "  '" + siteMasterValue + "'  AS " + siteMaster + "," + NL;
    sql += "  MD5(p." + id + " || c." + id + " ) || '" + allPathsRootTypeId + "' AS " + key + "," + NL;
    sql += "  '" + termAllPaths.definesType() + "' AS " + type + "," + NL;
    sql += "  '' AS " + domain + "," + NL;
    sql += "  ? AS " + lastUpdateDate + "," + NL;
    sql += "  NEXTVAL('" + PostgreSQL.UNIQUE_OBJECT_ID_SEQUENCE + "') AS " + sequence + "," + NL;
    sql += "  '" + createdById + "'  AS " + createdBy + "," + NL;
    sql += "  NULL AS " + lockedBy + "," + NL;
    sql += "  ? AS " + createDate + "," + NL;
    sql += "  '" + createdById + "' AS " + owner + "," + NL;
    sql += "  '" + createdById + "' AS " + lastUpdateDate + "," + NL;

    // parent term
    sql += "  paths." + RelationshipInfo.PARENT_ID + " AS " + parentTerm + "," + NL;

    // parent type
    sql += "  p." + domainType + " AS " + parentType + "," + NL;

    // child term
    sql += "  paths." + originalChild + " AS " + childTerm + ", " + NL;

    // child type
    sql += "c." + domainType + " AS " + childType + NL;

    sql += "FROM " + domainTable + " as p, " + NL;
    sql += domainTable + " as c," + NL;
    sql += "(SELECT " + originalChild + ", " + RelationshipInfo.PARENT_ID + " FROM " + view
        + " UNION SELECT " + id + "," + id + " FROM " + domainTable + " ) AS paths" + NL;

    sql += "WHERE p." + id + " = paths." + RelationshipInfo.PARENT_ID + " AND c." + id + " = paths."
        + originalChild + ";" + NL;


    int afterCount = this.execute(sql, transactionDate, transactionDate);
    
    if(log.isDebugEnabled())
    {
      log.debug("The type ["+termAllPaths+"] had ["+afterCount+"] objects in table ["+allpathsTable+"] AFTER a complete allpaths rebuild.");
    }
  }
  
  /**
   * Executes the given SQL and manages the connection. This takes in
   * a variable number of prepared statement arguments that are assigned
   * in order:
   * <code>
   * preparedStatement.setObject(1, args[0]);
   * preparedStatement.setObject(2, args[1]);
   * </code>
   * ... and so on.
   * 
   * @param sql
   * @param args
   * @return The number of rows updated
   */
  private int execute(String sql, Object ... args)
  {
    Connection conn = Database.getConnection();

    PreparedStatement prepared = null;

    try
    {
      prepared = conn.prepareStatement(sql);
      
      // prepared statements start counting at 1, not 0.
      int queryIndex = 1;
      for(Object arg : args)
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
   * Returns the last 32 characters of the MdBusiness that defines the allpaths
   * metadata. This is used for rapid id creation.
   * 
   * @return
   */
  private String getAllPathsTypeIdRoot()
  {
    return IdParser.parseRootFromId(this.provider.getTermAllPaths().getId());
  }
  
  @Transaction
  private void clear()
  {
    String allpathsTable = termAllPaths.getTableName();
    
    if(log.isDebugEnabled())
    {
      // report the number of records that are being deleted
      BusinessDAOQuery q = new QueryFactory().businessDAOQuery(termAllPaths.definesType());
      long beforeCount = q.getCount();
      
      log.debug("The type ["+termAllPaths+"] had ["+beforeCount+"] objects in table ["+allpathsTable+"] BEFORE a complete allpaths rebuild.");
    }
    
    termAllPaths.deleteAllTableRecords();
  }

  /* 
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#copyTerm(com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.Term, java.lang.String)
   */
  @Override
  public void copyTerm(Term parent, Term child, String relationshipType)
  {
//    MdBusiness termAllPaths = this.provider.getTermAllPaths();
//
//    String allpathsTable = termAllPaths.getTableName();
//    
//    String sql = "";
//    String id = getColumn(termAllPaths, MetadataInfo.ID);
//    String siteMaster = getColumn(termAllPaths, MetadataInfo.SITE_MASTER);
//    String createdBy = getColumn(termAllPaths, MetadataInfo.CREATED_BY);
//    String key = getColumn(termAllPaths, MetadataInfo.KEY);
//    String type = getColumn(termAllPaths, MetadataInfo.TYPE);
//    String domain = getColumn(termAllPaths, MetadataInfo.DOMAIN);
//    String lastUpdateDate = getColumn(termAllPaths, MetadataInfo.LAST_UPDATE_DATE);
//    String sequence = getColumn(termAllPaths, MetadataInfo.SEQUENCE);
//    String lockedBy = getColumn(termAllPaths, MetadataInfo.LOCKED_BY);
//    String createDate = getColumn(termAllPaths, MetadataInfo.CREATE_DATE);
//    String owner = getColumn(termAllPaths, MetadataInfo.OWNER);
//    String lastUpdatedBy = getColumn(termAllPaths, MetadataInfo.LAST_UPDATED_BY);
//    String parentTerm = getColumn(termAllPaths, this.provider.getAllPathsParentTerm());
//    String parentType = getColumn(termAllPaths, this.provider.getAllPathsParentType());
//    String childTerm = getColumn(termAllPaths, this.provider.getAllPathsChildTerm());
//    String childType = getColumn(termAllPaths, this.provider.getAllPathsChildType());
//    
//    // non-term values
//    Timestamp transactionDate = new Timestamp(new Date().getTime());
//
//    String[] metadataColumns = new String[] { id, siteMaster, key, type, domain, lastUpdateDate,
//        sequence, createdBy, lockedBy, createDate, owner, lastUpdatedBy, parentTerm, parentType,
//        childTerm, childType };
//
//    String insertColumns = StringUtils.join(metadataColumns, "," + NL);
//    sql += "INSERT INTO " + allpathsTable + " (" + insertColumns + ") " + NL;
//    
//    String childId = child.getId();
//    String parentId = parent.getId();
//    
//    sql +=    " FROM \n"
//        +
//        // Fech all of the recursive children of the given child term, including
//        // the child term itself.
//        "  (SELECT " + childTerm + "," + childType + " \n" + "    FROM " + allpathsTable + " \n" + "     WHERE " + parentTerm + " = '" + childId
//        + "' ) AS allpaths_child, \n"
//        +
//        // Fech all of the recursive parents of the given new parent term,
//        // including the new parent term itself.
//        "  (SELECT " + parentTerm + ", " + parentType + " \n" + "     FROM " + allpathsTable + " \n" + "    WHERE " + childTerm + " = '" + parentId + "' \n"
//        + "    ) AS allpaths_parent \n"
//        +
//        // Since a term can have multiple parents, a path to one of the new
//        // parent's parents may already exist
//        " WHERE allpaths_parent." + parentTerm + " NOT IN \n" + "   (SELECT " + parentTerm + " \n" + "      FROM " + allpathsTable + " \n" + "     WHERE " 
//        + parentTerm + " = allpaths_parent." + parentTerm + " \n" + "      AND "
//        + childTerm + " = allpaths_child." + childTerm + ") \n";
//
//    Connection conn = Database.getConnection();
//
//    PreparedStatement prepared = null;
//
//    try
//    {
//      prepared = conn.prepareStatement(sql);
//      prepared.setTimestamp(1, new Timestamp(transactionDate.getTime()));
//      prepared.setTimestamp(2, new Timestamp(transactionDate.getTime()));
//      prepared.executeUpdate();
//    }
//    catch (SQLException e)
//    {
//      throw new ProgrammingErrorException(e);
//    }
//    finally
//    {
//      if (prepared != null)
//      {
//        try
//        {
//          prepared.close();
//        }
//        catch (SQLException e)
//        {
//          throw new ProgrammingErrorException(e);
//        }
//      }
//    }
  }

  /* 
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#isLeaf(com.runwaysdk.business.ontology.Term, java.lang.String)
   */
  @Override
  public boolean isLeaf(Term term, String relationshipType)
  {
//    String relType = this.provider.getTermRelationship().definesType();
//    
//    // make sure there are no children
//    QueryFactory f = new QueryFactory();
//    RelationshipDAOQuery q = f.relationshipDAOQuery(relType);
//    q.WHERE(q.parentId().EQ(term.getId()));
//    
//    if(q.getCount() > 0)
//    {
//      // disqualified...already has children
//      return false;
//    }
//    
//    // ensure there's only one parent
//    f = new QueryFactory();
//    q = f.relationshipDAOQuery(relType);
//    q.WHERE(q.childId().EQ(term.getId()));    
//    
//    // a leaf can only have one or less parents
//    return q.getCount() <= 1;
    
    return false;
  }
  
  @Override
  public void deleteLeaf(Term term)
  {
//    MdBusiness allpaths = this.provider.getTermAllPaths();
//    String childTerm = this.provider.getAllPathsChildTerm();
//    String table = allpaths.getTableName();
//    
//    String sql = "DELETE FROM " + table + " WHERE " + getColumn(allpaths, childTerm) + " = ?";
//
//    int deleted = this.execute(sql, term.getId());
//    
//    if(log.isDebugEnabled())
//    {
//      log.debug("Deleting leaf term ["+term+"] removed ["+deleted+"] rows.");
//    }
  }

  /* 
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#getAllAncestors(com.runwaysdk.business.ontology.Term, java.lang.String)
   */
  @Override
  public List<Term> getAllAncestors(Term term, String relationshipType)
  {
//    QueryFactory f = new QueryFactory();
//
//    // restrict the all paths table 
//    String allPathsType = this.provider.getTermAllPaths().definesType();
//    BusinessQuery pathsQ = f.businessQuery(allPathsType);
//    
//    String domainType = this.provider.getTermDomain().definesType();
//    BusinessQuery domainQ = f.businessQuery(domainType);
//    
//    AttributeReference childTerm = pathsQ.aReference(this.provider.getAllPathsChildTerm());
//    AttributeReference parentTerm = pathsQ.aReference(this.provider.getAllPathsParentTerm());
//
//    // make sure all children are *this* Universal, but don't include
//    // the row where this Universal is its own parent
//    pathsQ.WHERE(childTerm.EQ(child.getId()));
//    pathsQ.AND(parentTerm.NE(child.getId()));
//    
//    // join the all paths with the universals
//    
//    domainQ.WHERE(domainQ.id().EQ(parentTerm.id()));
//    
//    OIterator<? extends Business> iter = domainQ.getIterator();
//    List<Term> terms = new LinkedList<Term>();
//    try
//    {
//      while(iter.hasNext())
//      {
//        terms.add((Term)iter.next());
//      }
//      
//      return terms;
//    }
//    finally
//    {
//      iter.close();
//    }
    
    return null;
  }

  /* 
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#getAllDescendants(com.runwaysdk.business.ontology.Term, java.lang.String)
   */
  @Override
  public List<Term> getAllDescendants(Term term, String relationshipType)
  {
//    QueryFactory f = new QueryFactory();
//
//    // restrict the all paths table 
//    String allPathsType = this.provider.getTermAllPaths().definesType();
//    BusinessQuery pathsQ = f.businessQuery(allPathsType);
//    
//    String domainType = this.provider.getTermDomain().definesType();
//    BusinessQuery domainQ = f.businessQuery(domainType);
//    
//    AttributeReference childTerm = pathsQ.aReference(this.provider.getAllPathsChildTerm());
//    AttributeReference parentTerm = pathsQ.aReference(this.provider.getAllPathsParentTerm());
//
//    // make sure all children are *this* Universal, but don't include
//    // the row where this Universal is its own parent
//    pathsQ.WHERE(parentTerm.EQ(parent.getId()));
//    pathsQ.AND(childTerm.NE(parent.getId()));
//    
//    // join the all paths with the universals
//    
//    domainQ.WHERE(domainQ.id().EQ(childTerm.id()));
//    
//    OIterator<? extends Business> iter = domainQ.getIterator();
//    List<Term> terms = new LinkedList<Term>();
//    try
//    {
//      while(iter.hasNext())
//      {
//        terms.add((Term)iter.next());
//      }
//      
//      return terms;
//    }
//    finally
//    {
//      iter.close();
//    }
    
    return null;
  }

  /* 
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#getDirectAncestors(com.runwaysdk.business.ontology.Term, java.lang.String)
   */
  @Override
  public List<Term> getDirectAncestors(Term term, String relationshipType)
  {
//    QueryFactory f = new QueryFactory();
//    
//    BusinessQuery b = f.businessQuery(this.provider.getTermDomain().definesType());
//    RelationshipQuery r = f.relationshipQuery(this.provider.getTermRelationship().definesType());
//
//    b.WHERE(r.childId().EQ(child.getId()));
//    b.AND(b.isParentIn(r));
//    
//    System.out.println(b.getSQL());
//    OIterator<? extends Business> iter = b.getIterator();
//    List<Term> terms = new LinkedList<Term>();
//    try
//    {
//      while(iter.hasNext())
//      {
//        terms.add((Term) iter.next());
//      }
//      
//      return terms;
//    }
//    finally
//    {
//      iter.close();
//    }
    
    return null;
  }

  /* 
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#getDirectDescendants(com.runwaysdk.business.ontology.Term, java.lang.String)
   */
  @Override
  public List<Term> getDirectDescendants(Term term, String relationshipType)
  {
//    QueryFactory f = new QueryFactory();
//    
//    BusinessQuery b = f.businessQuery(this.provider.getTermDomain().definesType());
//    RelationshipQuery r = f.relationshipQuery(this.provider.getTermRelationship().definesType());
//
//    b.WHERE(r.parentId().EQ(parent.getId()));
//    b.AND(b.isChildIn(r));
//    
//    OIterator<? extends Business> iter = b.getIterator();
//    List<Term> terms = new LinkedList<Term>();
//    try
//    {
//      while(iter.hasNext())
//      {
//        terms.add((Term) iter.next());
//      }
//      
//      return terms;
//    }
//    finally
//    {
//      iter.close();
//    }
    
    return null;
  }
}
