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
import java.util.Map;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.business.ontology.DDMSAllpathsLogic;
import com.runwaysdk.business.ontology.DefaultStrategy;
import com.runwaysdk.business.ontology.DeleteStrategyProviderIF;
import com.runwaysdk.business.ontology.OntologyStrategyIF;
import com.runwaysdk.business.ontology.OntologyEntryIF;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.DuplicateDataDatabaseException;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
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
  private static final long  serialVersionUID            = -428490646;

  private static Log         log                         = LogFactory.getLog(DatabaseAllPathsStrategy.class);

  public static final String PARENT_TERM_ATTR            = "parentTerm";

  public static final String CHILD_TERM_ATTR             = "childTerm";

  public static final String TERM_PARAMETER              = "mdTerm";

  public static final String TERM_RELATIONSHIP_PARAMETER = "mdRelationship";

  public static final String ALL_PATHS_PARAMETER         = "allPaths";

  public static final String PARENT_PARAMETER            = "parent";

  public static final String CHILD_PARAMETER             = "child";

  private MdBusiness         termAllPaths;

  private String             termClass;

  public DatabaseAllPathsStrategy()
  {
    super();
  }

  /**
   * @return
   */
  public MdBusiness getAllPaths()
  {
    if (this.termAllPaths == null)
    {
      // MdTerm mdTerm = this.getMdTerm();
      // String packageName =
      // mdTerm.getPackageName().replace(Constants.SYSTEM_PACKAGE,
      // Constants.ROOT_PACKAGE + ".generated.system");
      // String typeName = mdTerm.getTypeName() + "AllPathsTable";
      //
      // this.termAllPaths = MdBusiness.getMdBusiness(typeName);

      if (this.termAllPaths == null)
      {
        throw new ProgrammingErrorException("Strategy has not been initalized.");
      }
    }

    return this.termAllPaths;
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#isInitialized(java.lang.String)
   */
  @Override
  public boolean isInitialized()
  {
    try
    {
      return this.getStrategyState().contains(StrategyState.INITIALIZED);
    }
    catch (DataNotFoundException e)
    {
      return false;
    }
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#configure(java.lang.String)
   */
  @Override
  public void configure(String termClass)
  {
    this.termClass = termClass;
    MdTerm mdTerm = this.getMdTerm();

    String packageName = mdTerm.getPackageName().replace(Constants.SYSTEM_PACKAGE, Constants.ROOT_PACKAGE + ".generated.system");
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
    mdParentTermAttr.setValue(MdAttributeReferenceInfo.NAME, PARENT_TERM_ATTR);
    mdParentTermAttr.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent");
    mdParentTermAttr.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent");
    mdParentTermAttr.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdParentTermAttr.setValue(MdAttributeReferenceInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdParentTermAttr.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, allPaths.getId());
    mdParentTermAttr.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, mdTerm.getId());
    mdParentTermAttr.setValue(MdAttributeReferenceInfo.INDEX_TYPE, IndexTypes.NON_UNIQUE_INDEX.getId());
    mdParentTermAttr.apply();

    MdAttributeReferenceDAO mdChildTermAttr = MdAttributeReferenceDAO.newInstance();
    mdChildTermAttr.setValue(MdAttributeReferenceInfo.NAME, CHILD_TERM_ATTR);
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

  /**
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#initialize()
   */
  @Override
  @Transaction
  public void initialize(String relationshipType)
  {
    if (this.isInitialized())
    {
      return;
    }

    createTableMetadata();

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put(ALL_PATHS_PARAMETER, this.getAllPaths());

    OntologyDatabase database = new OntologyDatabaseFactory().getInstance(Database.instance(), this);
    database.initialize(parameters);

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
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.business.ontology.OntologyStrategyIF#reinitialize(java.lang.
   * String)
   */
  @Override
  public void reinitialize(String relationshipType)
  {
    if (!this.isInitialized())
    {
      this.initialize(relationshipType);
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
    if (!this.isInitialized())
    {
      return;
    }

    MdBusiness table = MdBusiness.get(this.getAllPaths().getId());

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put(ALL_PATHS_PARAMETER, table);

    OntologyDatabase database = new OntologyDatabaseFactory().getInstance(Database.instance(), this);
    database.shutdown(parameters);

    // Delete the termAllPaths MdBusiness
    table.delete();

    // The super changes the StrategyState
    super.shutdown();
  }

  /**
   * Rebuilds the AllPaths table for an Ontology of a given type.
   */
  @Transaction
  public void rebuildAllPaths(String relationshipType)
  {
    // Clear all existing all paths records
    this.clear();

    MdTerm termDomain = this.getMdTerm();
    MdRelationship termRelationship = MdRelationship.getMdRelationship(relationshipType);

    // Rebuild the database
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put(TERM_PARAMETER, termDomain);
    parameters.put(TERM_RELATIONSHIP_PARAMETER, termRelationship);
    parameters.put(ALL_PATHS_PARAMETER, this.getAllPaths());

    OntologyDatabase database = new OntologyDatabaseFactory().getInstance(Database.instance(), this);
    database.rebuild(parameters);
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

  /**
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#addLink(com.runwaysdk
   *      .business.ontology.Term, com.runwaysdk.business.ontology.Term,
   *      java.lang.String)
   */
  @Override
  public void addLink(Term parent, Term child, String relationshipType)
  {
    this.add(child, relationshipType);

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put(PARENT_PARAMETER, parent);
    parameters.put(CHILD_PARAMETER, child);
    parameters.put(ALL_PATHS_PARAMETER, this.getAllPaths());

    OntologyDatabase database = new OntologyDatabaseFactory().getInstance(Database.instance(), this);
    database.copyTerm(parameters);
  }

  /**
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#isLeaf(com.runwaysdk
   *      .business.ontology.Term, java.lang.String)
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
    String allPathsType = this.getAllPaths().definesType();
    BusinessQuery pathsQ = f.businessQuery(allPathsType);

    String domainType = getMdTerm().definesType();
    BusinessQuery domainQ = f.businessQuery(domainType);

    AttributeReference childTerm = pathsQ.aReference(CHILD_TERM_ATTR);
    AttributeReference parentTerm = pathsQ.aReference(PARENT_TERM_ATTR);

    // make sure all children are *this* Universal, but don't include
    // the row where this Universal is its own parent
    pathsQ.WHERE(childTerm.EQ(term.getId()));
    pathsQ.AND(parentTerm.NE(term.getId()));

    // join the all paths with the universals

    domainQ.WHERE(domainQ.id().EQ(parentTerm.id()));

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

    String allPathsType = this.getAllPaths().definesType();
    BusinessQuery pathsQ = f.businessQuery(allPathsType);

    String domainType = getMdTerm().definesType();
    BusinessQuery domainQ = f.businessQuery(domainType);

    AttributeReference childTerm = pathsQ.aReference(CHILD_TERM_ATTR);
    AttributeReference parentTerm = pathsQ.aReference(PARENT_TERM_ATTR);

    // make sure all children are *this* Universal, but don't include
    // the row where this Universal is its own parent
    pathsQ.WHERE(parentTerm.EQ(term.getId()));
    pathsQ.AND(childTerm.NE(term.getId()));

    // join the all paths with the universals

    domainQ.WHERE(domainQ.id().EQ(childTerm.id()));

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
    QueryFactory f = new QueryFactory();

    String allPathsType = this.getAllPaths().definesType();
    BusinessQuery pathsQ = f.businessQuery(allPathsType);

    pathsQ.WHERE(pathsQ.aReference(CHILD_TERM_ATTR).EQ(term.getId()));
    pathsQ.OR(pathsQ.aReference(PARENT_TERM_ATTR).EQ(term.getId()));

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

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#remove(com.runwaysdk
   *      .business.ontology.Term, com.runwaysdk.business.ontology.Term,
   *      java.lang.String)
   */
  @Override
  public void removeLink(Term parent, Term term, String relationshipType)
  {
    DDMSAllpathsLogic helper = new DDMSAllpathsLogic(this, relationshipType);

    // First, remove the term and all children from the allpaths table.
    helper.deleteTermAndChildrenFromAllPaths(term.getId(), relationshipType);

    // Now we update the term and all its children. This will rebuild the
    // allpaths to what it should be.
    helper.updateAllPathForTerm(term.getId(), null, true);
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#add(com.runwaysdk.business
   *      .ontology.Term, java.lang.String)
   */
  @Override
  public void add(Term term, String relationshipType)
  {
    Savepoint savepoint = Database.setSavepoint();

    try
    {
      // Create a new entry into the all paths table between where the term is
      // the
      // parent and the child
      BusinessDAO instance = BusinessDAO.newInstance(this.getAllPaths().definesType());
      instance.setValue(PARENT_TERM_ATTR, term.getId());
      instance.setValue(CHILD_TERM_ATTR, term.getId());
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

  public class AllPathsDeleteStrategyProvider implements DeleteStrategyProviderIF
  {
    private String allpaths_table_name;

    private String relationshipType;

    private Long   delRootACount;

    private AllPathsDeleteStrategyProvider(Term deleteRoot, String relationshipType)
    {
      allpaths_table_name = termAllPaths.getTableName();

      this.relationshipType = relationshipType;

      // Count how many ancestors this term has. This will be used for later
      // calculations
      QueryFactory f = new QueryFactory();
      String allPathsType = DatabaseAllPathsStrategy.this.getAllPaths().definesType();
      BusinessQuery pathsQ = f.businessQuery(allPathsType);
      pathsQ.WHERE(pathsQ.aReference(CHILD_TERM_ATTR).EQ(deleteRoot.getId()));
      delRootACount = pathsQ.getCount() - 1;
    }

    @Override
    public boolean isTermAlreadyProcessed(Term child, Stack<Term> s)
    {
      String childCol = MdBusinessDAO.get(DatabaseAllPathsStrategy.this.getAllPaths().getId()).definesAttribute(CHILD_TERM_ATTR).getColumnName();
      String allpathsAncestorsSql = Database.selectClause(Arrays.asList("count(*)"), Arrays.asList(allpaths_table_name), Arrays.asList(childCol + " = '" + child.getId() + "'"));
      ResultSet resultSet = Database.selectFromWhere("count(*)", Term.TEMP_TABLE, Term.TEMP_TERM_ID_COL + " = '" + child.getId() + "' AND (" + allpathsAncestorsSql + ") > " + ( 2 + s.size() + delRootACount ));
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
      String allPathsType = DatabaseAllPathsStrategy.this.getAllPaths().definesType();
      BusinessQuery pathsQ = f.businessQuery(allPathsType);
      pathsQ.WHERE(pathsQ.aReference(CHILD_TERM_ATTR).EQ(child.getId()));
      long ancestorCount = pathsQ.getCount() - 1;

      return s.size() + delRootACount < ancestorCount;
    }
  }

  public DeleteStrategyProviderIF getDeleteStrategyProvider(Term deleteRoot, String relationshipType)
  {
    return new AllPathsDeleteStrategyProvider(deleteRoot, relationshipType);
  }

  public static OntologyStrategyIF factory(String termClass)
  {
    DatabaseAllPathsStrategyQuery query = new DatabaseAllPathsStrategyQuery(new QueryFactory());
    query.WHERE(query.getTermClass().EQ(termClass));
    OIterator<? extends DatabaseAllPathsStrategy> it = query.getIterator();

    try
    {
      if(it.hasNext())
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
}
