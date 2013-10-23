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
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.DatabaseException;
import com.runwaysdk.dataaccess.database.general.PostgreSQL;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
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
import com.runwaysdk.system.metadata.MdTerm;
import com.runwaysdk.util.IdParser;

public class PostgresAllPathsStrategy extends PostgresAllPathsStrategyBase
{
  private static final long   serialVersionUID = -672024276;

  /**
   * The standard newline for readable printing. TODO use method concatenation
   * method to clean up the code.
   */
  private static final String NL               = "\n";

  private static Log          log              = LogFactory.getLog(PostgresAllPathsStrategy.class);

  private static final String parentTermAttr   = "parentTerm";

  private static final String childTermAttr    = "childTerm";

  private MdBusiness          termAllPaths;

  private String              termClass;

  /**
   * @return
   */
  private MdBusiness getAllPaths()
  {
    if (this.termAllPaths == null)
    {
      throw new RuntimeException("Strategy has not been initalized.");
    }

    return this.termAllPaths;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.business.ontology.OntologyStrategyIF#isInitialized(java.lang
   * .String)
   */
  @Override
  public boolean isInitialized(String relationshipType)
  {
    return this.getStrategyState().contains(StrategyState.INITIALIZED);
  }

  public void configure(String termClass)
  {
    this.termClass = termClass;
    MdTerm mdTerm = this.getMdTerm();

    String packageName = mdTerm.getPackageName();
    String typeName = mdTerm.getTypeName() + "AllPathsTable";

    try
    {
      this.termAllPaths = MdBusiness.getByKey(packageName + "." + typeName);
    }
    catch (DataNotFoundException e)
    {
      this.termAllPaths = null;
    }
  }

  @Transaction
  private void createTableMetadata()
  {
    MdTerm mdTerm = this.getMdTerm();

    String packageName = mdTerm.getPackageName().replace(Constants.SYSTEM_PACKAGE, Constants.ROOT_PACKAGE + ".generated.system");
    String typeName = mdTerm.getTypeName() + "AllPathsTable";

    MdBusinessDAO allPaths = MdBusinessDAO.newInstance();
    allPaths.setValue(MdBusinessInfo.NAME, typeName);
    allPaths.setValue(MdBusinessInfo.PACKAGE, packageName);
    allPaths.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "AllPaths Table");
    allPaths.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Used for storing AllPaths data.");
    allPaths.setValue(MdBusinessInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    allPaths.setGenerateMdController(false);
    allPaths.apply();

    MdAttributeReferenceDAO mdParentTermAttr = MdAttributeReferenceDAO.newInstance();
    mdParentTermAttr.setValue(MdAttributeReferenceInfo.NAME, parentTermAttr);
    mdParentTermAttr.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent");
    mdParentTermAttr.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent");
    mdParentTermAttr.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdParentTermAttr.setValue(MdAttributeReferenceInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdParentTermAttr.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, allPaths.getId());
    mdParentTermAttr.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, mdTerm.getId());
    mdParentTermAttr.setValue(MdAttributeReferenceInfo.INDEX_TYPE, IndexTypes.NON_UNIQUE_INDEX.getId());
    mdParentTermAttr.apply();

    MdAttributeReferenceDAO mdChildTermAttr = MdAttributeReferenceDAO.newInstance();
    mdChildTermAttr.setValue(MdAttributeReferenceInfo.NAME, childTermAttr);
    mdChildTermAttr.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child");
    mdChildTermAttr.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child");
    mdChildTermAttr.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdChildTermAttr.setValue(MdAttributeReferenceInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdChildTermAttr.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, allPaths.getId());
    mdChildTermAttr.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, mdTerm.getId());
    mdChildTermAttr.setValue(MdAttributeReferenceInfo.INDEX_TYPE, IndexTypes.NON_UNIQUE_INDEX.getId());
    mdChildTermAttr.apply();

    this.termAllPaths = MdBusiness.get(allPaths.getId());
  }

  /*
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#initialize()
   */
  @Override
  public void initialize(String relationshipType)
  {
    createTableMetadata();

    try
    {
      this.rebuildAllPaths(relationshipType);

      // The super changes the StrategyState
      super.initialize(relationshipType);
    }
    catch (RuntimeException e)
    {
      this.getAllPaths().delete();

      throw e;
    }
  }

  /*
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#shutdown()
   */
  @Override
  public void shutdown()
  {
    // TODO : Delete the termAllPaths MdBusiness
    MdBusiness.get(this.getAllPaths().getId()).delete();

    // The super changes the StrategyState
    super.shutdown();
  }

  /**
   * Rebuilds the AllPaths table for an Ontology of a given type.
   */
  @Transaction
  public void rebuildAllPaths(String relationshipType)
  {
    MdTerm termDomain = this.getMdTerm();
    MdRelationship termRelationship = MdRelationship.getMdRelationship(relationshipType);

    String allpathsTable = this.getAllPaths().getTableName();
    String allPathsRootTypeId = this.getAllPathsTypeIdRoot();

    // Clear all existing all paths records
    this.clear();

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
    String parentTerm = getColumn(termAllPaths, parentTermAttr);
    String childTerm = getColumn(termAllPaths, childTermAttr);

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
    sql.append("  '" + this.getAllPaths().definesType() + "' AS " + type + "," + NL);
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
    return IdParser.parseRootFromId(this.getAllPaths().getId());
  }

  @Transaction
  private void clear()
  {
    String allpathsTable = this.getAllPaths().getTableName();

    if (log.isDebugEnabled())
    {
      // report the number of records that are being deleted
      BusinessDAOQuery q = new QueryFactory().businessDAOQuery(this.getAllPaths().definesType());
      long beforeCount = q.getCount();

      log.debug("The type [" + termAllPaths + "] had [" + beforeCount + "] objects in table [" + allpathsTable + "] BEFORE a complete allpaths rebuild.");
    }

    this.getAllPaths().deleteAllTableRecords();
  }

  /*
   * @see
   * com.runwaysdk.system.metadata.ontology.OntologyStrategy#copyTerm(com.runwaysdk
   * .business.ontology.Term, com.runwaysdk.business.ontology.Term,
   * java.lang.String)
   */
  @Override
  public void copyTerm(Term parent, Term child, String relationshipType)
  {
    /*
     * First create the direct relationship
     */
    parent.addChild(child, relationshipType).apply();

    /*
     * Second update the all paths data structure
     */
    String allpathsTable = this.getAllPaths().getTableName();

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
    String parentTerm = getColumn(termAllPaths, parentTermAttr);
    String childTerm = getColumn(termAllPaths, childTermAttr);
    String allPathsRootTypeId = this.getAllPathsTypeIdRoot();

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
    sql.append("INSERT INTO " + allpathsTable + " (" + insertColumns + ") " + NL);
    sql.append(" SELECT " + NL);
    sql.append("   MD5(allpaths_parent." + parentTerm + " || allpaths_child." + childTerm + " ) || '" + allPathsRootTypeId + "' AS newId," + NL);
    sql.append("   '" + CommonProperties.getDomain() + "' AS " + siteMaster + "," + NL);
    sql.append("   MD5(allpaths_parent." + parentTerm + " || allpaths_child." + childTerm + " ) || '" + allPathsRootTypeId + "' AS newKey," + NL);
    sql.append("    '" + this.getAllPaths().definesType() + "' AS \"" + type + "\"," + NL);
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
    sql.append("    FROM " + allpathsTable + " " + NL);
    sql.append("    WHERE " + parentTerm + " = '" + childId + "' ) AS allpaths_child, " + NL);
    // Fech all of the recursive parents of the given new parent term,
    // including the new parent term itself.
    sql.append("  (SELECT " + parentTerm + " " + NL);
    sql.append("     FROM " + allpathsTable + " " + NL);
    sql.append("     WHERE " + childTerm + " = '" + parentId + "' " + NL + "    ) AS allpaths_parent " + NL);
    // Since a term can have multiple parents, a path to one of the new
    // parent's parents may already exist
    sql.append(" WHERE allpaths_parent." + parentTerm + " NOT IN " + NL);
    sql.append("   (SELECT " + parentTerm + " " + NL);
    sql.append("      FROM " + allpathsTable + " " + NL);
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

  /*
   * @see
   * com.runwaysdk.system.metadata.ontology.OntologyStrategy#isLeaf(com.runwaysdk
   * .business.ontology.Term, java.lang.String)
   */
  @Override
  public boolean isLeaf(Term term, String relationshipType)
  {
    // make sure there are no children
    QueryFactory f = new QueryFactory();
    RelationshipDAOQuery q = f.relationshipDAOQuery(relationshipType);
    q.WHERE(q.parentId().EQ(term.getId()));

    if (q.getCount() > 0)
    {
      // disqualified...already has children
      return false;
    }

    // ensure there's only one parent
    f = new QueryFactory();
    q = f.relationshipDAOQuery(relationshipType);
    q.WHERE(q.childId().EQ(term.getId()));

    // a leaf can only have one or less parents
    return q.getCount() <= 1;
  }

  /*
   * @see
   * com.runwaysdk.system.metadata.ontology.OntologyStrategy#getAllAncestors
   * (com.runwaysdk.business.ontology.Term, java.lang.String)
   */
  @Override
  public List<Term> getAllAncestors(Term term, String relationshipType)
  {
    // MdRelationship mdRel =
    // MdRelationship.getMdRelationship(relationshipType);

    QueryFactory f = new QueryFactory();

    // restrict the all paths table
    String allPathsType = this.getAllPaths().definesType();
    BusinessQuery pathsQ = f.businessQuery(allPathsType);

    String domainType = getMdTerm().definesType();
    BusinessQuery domainQ = f.businessQuery(domainType);

    AttributeReference childTerm = pathsQ.aReference(childTermAttr);
    AttributeReference parentTerm = pathsQ.aReference(parentTermAttr);

    // make sure all children are *this* Universal, but don't include
    // the row where this Universal is its own parent
    pathsQ.WHERE(childTerm.EQ(term.getId()));
    pathsQ.AND(parentTerm.NE(term.getId()));

    // join the all paths with the universals

    domainQ.WHERE(domainQ.id().EQ(parentTerm.id()));

    OIterator<? extends Business> iter = domainQ.getIterator();
    List<Term> terms = new LinkedList<Term>();
    try
    {
      while (iter.hasNext())
      {
        terms.add((Term) iter.next());
      }

      return terms;
    }
    finally
    {
      iter.close();
    }
  }

  /*
   * @see
   * com.runwaysdk.system.metadata.ontology.OntologyStrategy#getAllDescendants
   * (com.runwaysdk.business.ontology.Term, java.lang.String)
   */
  @Override
  public List<Term> getAllDescendants(Term term, String relationshipType)
  {
    // MdRelationship mdRel =
    // MdRelationship.getMdRelationship(relationshipType);

    QueryFactory f = new QueryFactory();

    // restrict the all paths table
    String allPathsType = this.getAllPaths().definesType();
    BusinessQuery pathsQ = f.businessQuery(allPathsType);

    String domainType = getMdTerm().definesType();
    BusinessQuery domainQ = f.businessQuery(domainType);

    AttributeReference childTerm = pathsQ.aReference(childTermAttr);
    AttributeReference parentTerm = pathsQ.aReference(parentTermAttr);

    // make sure all children are *this* Universal, but don't include
    // the row where this Universal is its own parent
    pathsQ.WHERE(parentTerm.EQ(term.getId()));
    pathsQ.AND(childTerm.NE(term.getId()));

    // join the all paths with the universals

    domainQ.WHERE(domainQ.id().EQ(childTerm.id()));

    OIterator<? extends Business> iter = domainQ.getIterator();
    List<Term> terms = new LinkedList<Term>();
    try
    {
      while (iter.hasNext())
      {
        terms.add((Term) iter.next());
      }

      return terms;
    }
    finally
    {
      iter.close();
    }
  }

  /*
   * @see
   * com.runwaysdk.system.metadata.ontology.OntologyStrategy#getDirectAncestors
   * (com.runwaysdk.business.ontology.Term, java.lang.String)
   */
  @Override
  public List<Term> getDirectAncestors(Term term, String relationshipType)
  {
    List<Term> terms = new LinkedList<Term>();

    OIterator<? extends Business> iterator = term.getParents(relationshipType);

    try
    {
      while (iterator.hasNext())
      {
        terms.add((Term) iterator.next());
      }

      return terms;
    }
    finally
    {
      iterator.close();
    }
  }

  /**
   * @return
   */
  public MdTerm getMdTerm()
  {
    return MdTerm.getByKey(this.termClass);
  }

  /*
   * @see
   * com.runwaysdk.system.metadata.ontology.OntologyStrategy#getDirectDescendants
   * (com.runwaysdk.business.ontology.Term, java.lang.String)
   */
  @Override
  public List<Term> getDirectDescendants(Term term, String relationshipType)
  {
    List<Term> terms = new LinkedList<Term>();

    OIterator<? extends Business> iterator = term.getChildren(relationshipType);

    try
    {
      while (iterator.hasNext())
      {
        terms.add((Term) iterator.next());
      }

      return terms;
    }
    finally
    {
      iterator.close();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.business.ontology.OntologyStrategyIF#remove(com.runwaysdk
   * .business.ontology.Term, java.lang.String)
   */
  @Override
  public void removeTerm(Term term, String relationshipType)
  {
    boolean isLeaf = this.isLeaf(term, relationshipType);

    /*
     * First remove all of the direct links to the nodes parents.
     */
    List<Term> parents = this.getDirectAncestors(term, relationshipType);

    for (Term parent : parents)
    {
      term.removeAllParents(parent, relationshipType);
    }

    if (!isLeaf)
    {
      /*
       * First remove all of the direct links to the nodes children.
       */
      List<Term> children = this.getDirectDescendants(term, relationshipType);

      for (Term child : children)
      {
        term.removeAllChildren(child, relationshipType);
      }

      /*
       * There is no good way to handle deletes of non-leaf nodes. As such we
       * have to clear the all paths table and rebuild it.
       */
      this.clear();
      this.rebuildAllPaths(relationshipType);
    }

    QueryFactory f = new QueryFactory();

    // restrict the all paths table
    String allPathsType = this.getAllPaths().definesType();
    BusinessQuery pathsQ = f.businessQuery(allPathsType);

    pathsQ.WHERE(pathsQ.aReference(childTermAttr).EQ(term.getId()));

    OIterator<? extends Business> iter = pathsQ.getIterator();
    try
    {
      while (iter.hasNext())
      {
        iter.next().delete();
      }
    }
    finally
    {
      iter.close();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.business.ontology.OntologyStrategyIF#remove(com.runwaysdk
   * .business.ontology.Term, com.runwaysdk.business.ontology.Term,
   * java.lang.String)
   */
  @Override
  public void removeLink(Term parent, Term term, String relationshipType)
  {
    boolean isLeaf = this.isLeaf(term, relationshipType);

    /*
     * First remove all of the direct links to the nodes parents.
     */
    parent.removeAllChildren(term, relationshipType);

    if (isLeaf)
    {
      /*
       * If this is a leaf the only thing left is delete all of the records in
       * the all path where the term is a child.
       */
      QueryFactory f = new QueryFactory();

      // restrict the all paths table
      String allPathsType = this.getAllPaths().definesType();
      BusinessQuery pathsQ = f.businessQuery(allPathsType);

      pathsQ.WHERE(pathsQ.aReference(childTermAttr).EQ(term.getId()));
      pathsQ.AND(pathsQ.aReference(parentTermAttr).EQ(parent.getId()));

      OIterator<? extends Business> iter = pathsQ.getIterator();
      try
      {
        while (iter.hasNext())
        {
          iter.next().delete();
        }
      }
      finally
      {
        iter.close();
      }
    }
    else
    {
      /*
       * There is no good way to handle deletes of non-leaf nodes. As such we
       * have to clear the all paths table and rebuild it.
       */
      this.clear();
      this.rebuildAllPaths(relationshipType);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.business.ontology.OntologyStrategyIF#add(com.runwaysdk.business
   * .ontology.Term, java.lang.String)
   */
  @Override
  public void add(Term term, String relationshipType)
  {
    // Create a new entry into the all paths table between where the term is the
    // parent and the child
    BusinessDAO instance = BusinessDAO.newInstance(this.getAllPaths().definesType());
    instance.setValue(parentTermAttr, term.getId());
    instance.setValue(childTermAttr, term.getId());
    instance.apply();
  }
}
