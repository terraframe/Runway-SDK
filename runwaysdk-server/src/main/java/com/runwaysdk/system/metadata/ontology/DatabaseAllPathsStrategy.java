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
package com.runwaysdk.system.metadata.ontology;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.business.ontology.DDMSAllpathsLogic;
import com.runwaysdk.business.ontology.DefaultStrategy;
import com.runwaysdk.business.ontology.DeleteStrategyProviderIF;
import com.runwaysdk.business.ontology.OntologyEntryIF;
import com.runwaysdk.business.ontology.OntologyStrategyIF;
import com.runwaysdk.business.ontology.InitializationStrategyIF;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdTermDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.DuplicateDataDatabaseException;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.dataaccess.metadata.MdTermRelationshipDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.ontology.strategy.database.OntologyDatabase;
import com.runwaysdk.ontology.strategy.database.OntologyDatabaseFactory;
import com.runwaysdk.query.AttributeReference;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.RelationshipDAOQuery;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdRelationship;
import com.runwaysdk.system.metadata.MdTerm;

public class DatabaseAllPathsStrategy extends DatabaseAllPathsStrategyBase
{
  private static final long       serialVersionUID            = -428490646;

  private static Logger           logger                      = LoggerFactory.getLogger(DatabaseAllPathsStrategy.class);

  public static final String      PARENT_TERM_ATTR            = "parentTerm";

  public static final String      CHILD_TERM_ATTR             = "childTerm";

  public static final String      TERM_PARAMETER              = "mdTerm";

  public static final String      TERM_RELATIONSHIP_PARAMETER = "mdRelationship";

  public static final String      ALL_PATHS_PARAMETER         = "allPaths";

  public static final String      PARENT_PARAMETER            = "parent";

  public static final String      CHILD_PARAMETER             = "child";

  /**
   * Map between the relationship type and the MdBusiness
   */
  private Map<String, MdBusiness> allPaths;

  private String                  termClass;

  public DatabaseAllPathsStrategy()
  {
    super();
  }

  /**
   * @param relationshipType
   *          TODO
   * @return
   */
  public MdBusiness getAllPaths(String relationshipType)
  {
    if (this.allPaths == null || !this.allPaths.containsKey(relationshipType))
    {
      throw new ProgrammingErrorException("Strategy has not been initalized.");
    }

    return this.allPaths.get(relationshipType);
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#isInitialized(java.lang.String)
   */
  @Override
  public boolean isInitialized()
  {
    return ( this.allPaths.size() > 0 );
    // try
    // {
    // return this.getStrategyState().contains(StrategyState.INITIALIZED);
    // }
    // catch (DataNotFoundException e)
    // {
    // return false;
    // }
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#configure(java.lang.String)
   */
  @Override
  public void configure(String termClass)
  {
    if (this.allPaths == null)
    {
      this.termClass = termClass;
      this.allPaths = new HashMap<String, MdBusiness>();

      MdTerm mdTerm = this.getMdTerm();
      MdTermDAOIF mdTermDAO = MdTermDAO.get(mdTerm.getOid());

      List<MdRelationshipDAOIF> mdRelationships = mdTermDAO.getAllChildMdRelationships();

      for (MdRelationshipDAOIF mdRelationship : mdRelationships)
      {
        if (mdRelationship instanceof MdTermRelationshipDAO)
        {
          String packageName = mdTerm.getPackageName().replace(Constants.SYSTEM_PACKAGE, Constants.ROOT_PACKAGE + ".generated.system");
          String typeName = mdRelationship.getTypeName() + "AllPathsTable";

          try
          {
            this.allPaths.put(mdRelationship.definesType(), MdBusiness.getByKey(packageName + "." + typeName));
          }
          catch (DataNotFoundException e)
          {
            logger.debug("Unable to find allpaths [" + typeName + "]");
          }
        }
      }
    }
  }

  @Transaction
  private void createTableMetadata(String relationshipType, InitializationStrategyIF strategy)
  {
    if (this.allPaths == null || !this.allPaths.containsKey(relationshipType))
    {
      MdRelationshipDAOIF mdRelationship = MdRelationshipDAO.getMdRelationshipDAO(relationshipType);
      MdTerm mdTerm = this.getMdTerm();
      String packageName = getPackageName(mdTerm);
      String typeName = getTypeName(mdRelationship);

      MdBusinessDAO allPaths = MdBusinessDAO.newInstance();
      allPaths.setValue(MdBusinessInfo.NAME, typeName);
      allPaths.setValue(MdBusinessInfo.PACKAGE, packageName);
      allPaths.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "AllPaths Table");
      allPaths.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Used for storing AllPaths data.");
      allPaths.setValue(MdBusinessInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);

      if (strategy != null)
      {
        strategy.preApply(allPaths);
      }

      allPaths.apply();

      MdAttributeReferenceDAO mdParentTermAttr = MdAttributeReferenceDAO.newInstance();
      mdParentTermAttr.setValue(MdAttributeReferenceInfo.NAME, PARENT_TERM_ATTR);
      mdParentTermAttr.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent");
      mdParentTermAttr.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent");
      mdParentTermAttr.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
      mdParentTermAttr.setValue(MdAttributeReferenceInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdParentTermAttr.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, allPaths.getOid());
      mdParentTermAttr.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, mdTerm.getOid());
      mdParentTermAttr.setValue(MdAttributeReferenceInfo.INDEX_TYPE, IndexTypes.NON_UNIQUE_INDEX.getOid());
      mdParentTermAttr.apply();

      MdAttributeReferenceDAO mdChildTermAttr = MdAttributeReferenceDAO.newInstance();
      mdChildTermAttr.setValue(MdAttributeReferenceInfo.NAME, CHILD_TERM_ATTR);
      mdChildTermAttr.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child");
      mdChildTermAttr.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child");
      mdChildTermAttr.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
      mdChildTermAttr.setValue(MdAttributeReferenceInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdChildTermAttr.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, allPaths.getOid());
      mdChildTermAttr.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, mdTerm.getOid());
      mdChildTermAttr.setValue(MdAttributeReferenceInfo.INDEX_TYPE, IndexTypes.NON_UNIQUE_INDEX.getOid());
      mdChildTermAttr.apply();

      this.allPaths.put(relationshipType, MdBusiness.get(allPaths.getOid()));

      if (strategy != null)
      {
        strategy.postApply(allPaths);
      }
    }
    else
    {
      // This should never happen
      logger.error("Table metadata already exists for DatabaseAllPathsStrategy [" + relationshipType + "]. Bad manners!");
    }
  }

  /**
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#initialize()
   */
  @Override
  @Transaction
  public void initialize(String relationshipType, InitializationStrategyIF strategy)
  {
    if (this.allPaths != null && this.allPaths.containsKey(relationshipType))
    {
      return;
    }

    createTableMetadata(relationshipType, strategy);

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put(ALL_PATHS_PARAMETER, this.getAllPaths(relationshipType));

    OntologyDatabase database = new OntologyDatabaseFactory().getInstance(Database.instance(), this);
    database.initialize(parameters);

    try
    {
      this.rebuildAllPaths(relationshipType);

      // The super changes the StrategyState
      super.initialize(relationshipType, strategy);
    }
    catch (RuntimeException e)
    {
      this.getAllPaths(relationshipType).delete();

      throw e;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.business.ontology.OntologyStrategyIF#reinitialize(java.lang.
   * String)
   */
  @Override
  public void reinitialize(String relationshipType, InitializationStrategyIF strategy)
  {
    if (!this.isInitialized())
    {
      this.initialize(relationshipType, strategy);
    }

    this.rebuildAllPaths(relationshipType);
  }

  /**
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#shutdown()
   */
  @Override
  @Transaction
  public void shutdown()
  {
    Set<String> relationshipTypes = this.allPaths.keySet();

    for (String relationshipType : relationshipTypes)
    {
      MdBusiness table = MdBusiness.get(this.getAllPaths(relationshipType).getOid());

      Map<String, Object> parameters = new HashMap<String, Object>();
      parameters.put(ALL_PATHS_PARAMETER, table);

      OntologyDatabase database = new OntologyDatabaseFactory().getInstance(Database.instance(), this);
      database.shutdown(parameters);

      // Truncate the allpaths table records
      MdBusinessDAO.get(table.getOid()).getBusinessDAO().deleteAllRecords();

      // Delete the termAllPaths MdBusiness
      table.delete();
    }

    this.allPaths.clear();

    // The super changes the StrategyState
    super.shutdown();
  }

  @Override
  public void shutdown(String relationshipType)
  {
    if (this.allPaths.containsKey(relationshipType))
    {
      MdBusiness table = MdBusiness.get(this.getAllPaths(relationshipType).getOid());

      Map<String, Object> parameters = new HashMap<String, Object>();
      parameters.put(ALL_PATHS_PARAMETER, table);

      OntologyDatabase database = new OntologyDatabaseFactory().getInstance(Database.instance(), this);
      database.shutdown(parameters);

      // Truncate the allpaths table records
      MdBusinessDAO.get(table.getOid()).getBusinessDAO().deleteAllRecords();

      // Delete the termAllPaths MdBusiness
      table.delete();

      this.allPaths.remove(relationshipType);
    }
  }

  /**
   * Rebuilds the AllPaths table for an Ontology of a given type.
   */
  @Transaction
  public void rebuildAllPaths(String relationshipType)
  {
    // Clear all existing all paths records
    this.clear(relationshipType);

    MdTerm termDomain = this.getMdTerm();
    MdRelationship termRelationship = MdRelationship.getMdRelationship(relationshipType);

    // Rebuild the database
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put(TERM_PARAMETER, termDomain);
    parameters.put(TERM_RELATIONSHIP_PARAMETER, termRelationship);
    parameters.put(ALL_PATHS_PARAMETER, this.getAllPaths(relationshipType));

    OntologyDatabase database = new OntologyDatabaseFactory().getInstance(Database.instance(), this);
    database.rebuild(parameters);
  }

  @Transaction
  private void clear(String relationshipType)
  {
    MdBusiness mdBusiness = this.getAllPaths(relationshipType);
    String allpathsTable = mdBusiness.getTableName();

    if (logger.isDebugEnabled())
    {
      // report the number of records that are being deleted
      BusinessDAOQuery q = new QueryFactory().businessDAOQuery(mdBusiness.definesType());
      long beforeCount = q.getCount();

      logger.debug("The type [" + mdBusiness.definesType() + "] had [" + beforeCount + "] objects in table [" + allpathsTable + "] BEFORE a complete allpaths rebuild.");
    }

    mdBusiness.deleteAllTableRecords();
  }

  /**
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#addLink(com.runwaysdk
   *      .business.ontology.Term, com.runwaysdk.business.ontology.Term,
   *      java.lang.String)
   */
  @Override
  public void addLink(Term parent, Term child, String relationshipType)
  {
    if (this.allPaths.containsKey(relationshipType))
    {
      this.add(child, relationshipType);

      Map<String, Object> parameters = new HashMap<String, Object>();
      parameters.put(PARENT_PARAMETER, parent);
      parameters.put(CHILD_PARAMETER, child);
      parameters.put(ALL_PATHS_PARAMETER, this.getAllPaths(relationshipType));

      OntologyDatabase database = new OntologyDatabaseFactory().getInstance(Database.instance(), this);
      database.copyTerm(parameters);
    }
  }

  /**
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#isLeaf(com.runwaysdk
   *      .business.ontology.Term, java.lang.String)
   */
  @Override
  public boolean isLeaf(Term term, String relationshipType)
  {
    if (this.allPaths.containsKey(relationshipType))
    {

      // make sure there are no children
      QueryFactory f = new QueryFactory();
      RelationshipDAOQuery q = f.relationshipDAOQuery(relationshipType);
      q.WHERE(q.parentOid().EQ(term.getOid()));

      if (q.getCount() > 0)
      {
        // disqualified...already has children
        return false;
      }

      // ensure there's only one parent
      f = new QueryFactory();
      q = f.relationshipDAOQuery(relationshipType);
      q.WHERE(q.childOid().EQ(term.getOid()));

      // a leaf can only have one or less parents
      return q.getCount() <= 1;
    }

    return true;
  }

  /**
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#getAllAncestors
   *      (com.runwaysdk.business.ontology.Term, java.lang.String)
   */
  @SuppressWarnings("unchecked")
  @Override
  public OIterator<Term> getAllAncestors(Term term, String relationshipType)
  {
    // MdRelationship mdRel =
    // MdRelationship.getMdRelationship(relationshipType);

    QueryFactory f = new QueryFactory();

    // restrict the all paths table
    String allPathsType = this.getAllPaths(relationshipType).definesType();
    BusinessQuery pathsQ = f.businessQuery(allPathsType);

    String domainType = getMdTerm().definesType();
    BusinessQuery domainQ = f.businessQuery(domainType);

    AttributeReference childTerm = pathsQ.aReference(CHILD_TERM_ATTR);
    AttributeReference parentTerm = pathsQ.aReference(PARENT_TERM_ATTR);

    // make sure all children are *this* Universal, but don't include
    // the row where this Universal is its own parent
    pathsQ.WHERE(childTerm.EQ(term.getOid()));
    pathsQ.AND(parentTerm.NE(term.getOid()));

    // join the all paths with the universals

    domainQ.WHERE(domainQ.oid().EQ(parentTerm.oid()));

    OIterator<? extends Business> iter = domainQ.getIterator();
    // List<Term> terms = new LinkedList<Term>();
    // try
    // {
    // while (iter.hasNext())
    // {
    // terms.add((Term) iter.next());
    // }
    //
    // return terms;
    // }
    // finally
    // {
    // iter.close();
    // }

    return (OIterator<Term>) iter;
  }

  /**
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#getAllDescendants
   *      (com.runwaysdk.business.ontology.Term, java.lang.String)
   */
  @SuppressWarnings("unchecked")
  @Override
  public OIterator<Term> getAllDescendants(Term term, String relationshipType)
  {
    // MdRelationship mdRel =
    // MdRelationship.getMdRelationship(relationshipType);

    QueryFactory f = new QueryFactory();

    String allPathsType = this.getAllPaths(relationshipType).definesType();
    BusinessQuery pathsQ = f.businessQuery(allPathsType);

    String domainType = getMdTerm().definesType();
    BusinessQuery domainQ = f.businessQuery(domainType);

    AttributeReference childTerm = pathsQ.aReference(CHILD_TERM_ATTR);
    AttributeReference parentTerm = pathsQ.aReference(PARENT_TERM_ATTR);

    // make sure all children are *this* Universal, but don't include
    // the row where this Universal is its own parent
    pathsQ.WHERE(parentTerm.EQ(term.getOid()));
    pathsQ.AND(childTerm.NE(term.getOid()));

    // join the all paths with the universals

    domainQ.WHERE(domainQ.oid().EQ(childTerm.oid()));

    OIterator<? extends Business> iter = domainQ.getIterator();
    // List<Term> terms = new LinkedList<Term>();
    // try
    // {
    // while (iter.hasNext())
    // {
    // terms.add((Term) iter.next());
    // }
    //
    // return terms;
    // }
    // finally
    // {
    // iter.close();
    // }

    return (OIterator<Term>) iter;
  }

  /**
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#getDirectAncestors
   *      (com.runwaysdk.business.ontology.Term, java.lang.String)
   */
  @Override
  public OIterator<Term> getDirectAncestors(Term term, String relationshipType)
  {
    return DefaultStrategy.Singleton.INSTANCE.getDirectAncestors(term, relationshipType);
  }

  /**
   * @return
   */
  public MdTerm getMdTerm()
  {
    return MdTerm.getByKey(this.termClass);
  }

  /**
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#getDirectDescendants
   *      (com.runwaysdk.business.ontology.Term, java.lang.String)
   */
  @Override
  public OIterator<Term> getDirectDescendants(Term term, String relationshipType)
  {
    return DefaultStrategy.Singleton.INSTANCE.getDirectDescendants(term, relationshipType);
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#remove(com.runwaysdk
   *      .business.ontology.Term, java.lang.String)
   */
  @Override
  public void removeTerm(Term term, String relationshipType)
  {
    if (this.allPaths.containsKey(relationshipType))
    {
      QueryFactory f = new QueryFactory();

      String allPathsType = this.getAllPaths(relationshipType).definesType();
      BusinessQuery pathsQ = f.businessQuery(allPathsType);

      pathsQ.WHERE(pathsQ.aReference(CHILD_TERM_ATTR).EQ(term.getOid()));
      pathsQ.OR(pathsQ.aReference(PARENT_TERM_ATTR).EQ(term.getOid()));

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
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#remove(com.runwaysdk
   *      .business.ontology.Term, com.runwaysdk.business.ontology.Term,
   *      java.lang.String)
   */
  @Override
  public void removeLink(Term parent, Term term, String relationshipType)
  {
    if (this.allPaths.containsKey(relationshipType))
    {
      DDMSAllpathsLogic helper = new DDMSAllpathsLogic(this, relationshipType);

      // First, remove the term and all children from the allpaths table.
      helper.deleteTermAndChildrenFromAllPaths(term.getOid(), relationshipType);

      // Now we update the term and all its children. This will rebuild the
      // allpaths to what it should be.
      helper.updateAllPathForTerm(term.getOid(), null, true);
    }
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#add(com.runwaysdk.business
   *      .ontology.Term, java.lang.String)
   */
  @Override
  public void add(Term term, String relationshipType)
  {
    if (this.allPaths.containsKey(relationshipType))
    {
      Savepoint savepoint = Database.setSavepoint();

      try
      {
        // Create a new entry into the all paths table between where the term is
        // the
        // parent and the child
        BusinessDAO instance = BusinessDAO.newInstance(this.getAllPaths(relationshipType).definesType());
        instance.setValue(PARENT_TERM_ATTR, term.getOid());
        instance.setValue(CHILD_TERM_ATTR, term.getOid());
        instance.apply();
      }
      catch (DuplicateDataDatabaseException ex)
      {
        // This might happen. Entry already exists.
        Database.rollbackSavepoint(savepoint);
      }
      catch (RuntimeException ex)
      {
        Database.rollbackSavepoint(savepoint);
        throw ex;
      }
      finally
      {
        Database.releaseSavepoint(savepoint);
      }
    }
  }

  public class AllPathsDeleteStrategyProvider implements DeleteStrategyProviderIF
  {
    private String allpaths_table_name;

    private String relationshipType;

    private Long   delRootACount;

    private AllPathsDeleteStrategyProvider(Term deleteRoot, String relationshipType)
    {
      allpaths_table_name = allPaths.get(relationshipType).getTableName();

      this.relationshipType = relationshipType;

      // Count how many ancestors this term has. This will be used for later
      // calculations
      QueryFactory f = new QueryFactory();
      String allPathsType = DatabaseAllPathsStrategy.this.getAllPaths(relationshipType).definesType();
      BusinessQuery pathsQ = f.businessQuery(allPathsType);
      pathsQ.WHERE(pathsQ.aReference(CHILD_TERM_ATTR).EQ(deleteRoot.getOid()));
      delRootACount = pathsQ.getCount() - 1;
    }

    @Override
    public boolean isTermAlreadyProcessed(Term child, Stack<Term> s, String tempTableName)
    {
      String childCol = MdBusinessDAO.get(DatabaseAllPathsStrategy.this.getAllPaths(relationshipType).getOid()).definesAttribute(CHILD_TERM_ATTR).getColumnName();
      String allpathsAncestorsSql = Database.selectClause(Arrays.asList("count(*)"), Arrays.asList(allpaths_table_name), Arrays.asList(childCol + " = '" + child.getOid() + "'"));
      ResultSet resultSet = Database.selectFromWhere("count(*)", tempTableName, Term.TEMP_TERM_ID_COL + " = '" + child.getOid() + "' AND (" + allpathsAncestorsSql + ") > " + ( 2 + s.size() + delRootACount ));
      try
      {
        if (resultSet.next())
        {
          int count = resultSet.getInt("count");

          if (count > 0)
          {
            return true;
          }
        }

        return false;
      }
      catch (SQLException sqlEx1)
      {
        Database.throwDatabaseException(sqlEx1);
      }
      finally
      {
        try
        {
          java.sql.Statement statement = resultSet.getStatement();
          resultSet.close();
          statement.close();
        }
        catch (SQLException sqlEx2)
        {
          Database.throwDatabaseException(sqlEx2);
        }
      }

      return true;
    }

    @Override
    public boolean doesAncestorHaveMultipleParents(Term child, Stack<Term> s)
    {
      // Count how many ancestors this term has. This will be used for later
      // calculations
      QueryFactory f = new QueryFactory();
      String allPathsType = DatabaseAllPathsStrategy.this.getAllPaths(relationshipType).definesType();
      BusinessQuery pathsQ = f.businessQuery(allPathsType);
      pathsQ.WHERE(pathsQ.aReference(CHILD_TERM_ATTR).EQ(child.getOid()));
      long ancestorCount = pathsQ.getCount() - 1;

      return s.size() + delRootACount < ancestorCount;
    }
  }

  public DeleteStrategyProviderIF getDeleteStrategyProvider(Term deleteRoot, String relationshipType)
  {
    if (this.allPaths.containsKey(relationshipType))
    {
      return new AllPathsDeleteStrategyProvider(deleteRoot, relationshipType);
    }
    else
    {
      return new DeleteStrategyProviderIF()
      {
        @Override
        public boolean isTermAlreadyProcessed(Term child, Stack<Term> stack, String tempTableName)
        {
          return true;
        }

        @Override
        public boolean doesAncestorHaveMultipleParents(Term child, Stack<Term> stack)
        {
          return false;
        }
      };
    }
  }

  public static OntologyStrategyIF factory(String termClass)
  {
    DatabaseAllPathsStrategyQuery query = new DatabaseAllPathsStrategyQuery(new QueryFactory());
    query.WHERE(query.getTermClass().EQ(termClass));
    OIterator<? extends DatabaseAllPathsStrategy> it = query.getIterator();

    try
    {
      if (it.hasNext())
      {
        DatabaseAllPathsStrategy strategy = it.next();
        return strategy;
      }
    }
    finally
    {
      it.close();
    }

    DatabaseAllPathsStrategy strategy = new DatabaseAllPathsStrategy();
    strategy.setTermClass(termClass);

    return strategy;
  }

  @Override
  public void addSynonym(Term term, OntologyEntryIF synonym)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateSynonym(OntologyEntryIF synonym)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void removeSynonym(OntologyEntryIF synonym)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateLabel(Term term, String label)
  {
    // TODO Auto-generated method stub
  }

  public static String getPackageName(MdTerm mdTerm)
  {
    return mdTerm.getPackageName().replace(Constants.SYSTEM_PACKAGE, Constants.ROOT_PACKAGE + ".generated.system");
  }

  public static String getTypeName(MdRelationshipDAOIF mdRelationship)
  {
    return mdRelationship.getTypeName() + "AllPathsTable";
  }

}
