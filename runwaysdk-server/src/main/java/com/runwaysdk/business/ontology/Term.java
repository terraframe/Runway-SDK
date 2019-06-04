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
package com.runwaysdk.business.ontology;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.LocalStruct;
import com.runwaysdk.business.Relationship;
import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeUUIDInfo;
import com.runwaysdk.dataaccess.CoreException;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdTermDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.ReservedWords;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.ontology.ImmutableRootException;
import com.runwaysdk.system.ontology.TermUtil;

abstract public class Term extends Business implements QualifiedOntologyEntryIF
{
  public static final String CLASS            = "com.runwaysdk.business.ontology.Term";

  private static final long  serialVersionUID = -2009350279143212154L;
  
  public static final String ROOT_KEY             = "ROOT";

  public Term()
  {
    super();
  }

  public static final String TEMP_TABLE_PREFIX = "TDEL_";
  public static final String TEMP_TERM_ID_COL = "termId";
  public static final String TEMP_PARENT_OID_COL = "parentOid";
  public static final String TEMP_DEPTH_COL = "depth";
  public static final String INDEX_NAME_PREFIX = "TDIN_";
  public static final List<String> TEMP_TABLE_COLUMNS = Arrays.asList(
//      TEMP_TERM_ID_COL + " " + Database.formatCharacterField(DatabaseProperties.getDatabaseType(MdAttributeCharacterInfo.CLASS), "36"),
//      TEMP_PARENT_OID_COL + " " + Database.formatCharacterField(DatabaseProperties.getDatabaseType(MdAttributeCharacterInfo.CLASS), "36"),
      TEMP_TERM_ID_COL + " " + DatabaseProperties.getDatabaseType(MdAttributeUUIDInfo.CLASS),
      TEMP_PARENT_OID_COL + " " + DatabaseProperties.getDatabaseType(MdAttributeUUIDInfo.CLASS),
      TEMP_DEPTH_COL + " " + DatabaseProperties.getDatabaseType(MdAttributeIntegerInfo.CLASS)
  );
  private static final List<String> TEMP_TABLE_ATTRS = Arrays.asList(
      MdAttributeUUIDInfo.CLASS, MdAttributeUUIDInfo.CLASS, MdAttributeIntegerInfo.CLASS
  );
  
  /**
   * !Warning!: Be very careful when overriding this method! Overriding should only be used to completely change the behavior (and not super), or to add a "before hook" into delete. If you want special delete logic done
   *          every time a term is deleted then you need to override deletePerTerm instead because this delete method is not called on all children (only the very first deleted term).
   * 
   * Deletes the term, all of it's children, all associated relationships, and updates the ontology strategy across all ontological relationship types. May be a potentially expensive operation.
   * 
   * TODO: Multi-threading
   * TODO: At what point is it faster to rebuild the Allpaths table?
   * TODO: Add better support in Query API for managing tables so this temp table logic can be more cross DB
   * TODO: Should the temp table logic be moved into the DeleteStrategyProvider?
   * TODO: If we ever start using multiple relationships, we'll need to use different temp tables per relationship otherwise there could be conflicts.
   */
  @Transaction
  @Override
  public void delete()
  {
    this.delete(true);
  }
  
  public void delete(boolean deleteChildren)
  {
    if (this.getKey().equals(ROOT_KEY))
    {
      ImmutableRootException exception = new ImmutableRootException("Cannot delete the root Term.");
      exception.setRootName(this.getDisplayLabel().getValue());
      exception.apply();
      
      throw exception;
    }
    
    if (deleteChildren)
    {
      String[] rels = TermUtil.getAllChildRelationships(this.getOid());
      for (String relationship : rels)
      {
        exhaustiveDelete(relationship);
      }
    }
    else
    {
      String[] rels = TermUtil.getAllChildRelationships(this.getOid());
      for (String relationship : rels)
      {
        this.getStrategyWithInstance().removeTerm(this, relationship);
      }
    }
    
    this.beforeDeleteTerm();
    super.delete();
    
    this.afterDeleteTerm();
  }
  
  private String caclulateTempTableName()
  {
    String name = TEMP_TABLE_PREFIX + this.getOid();
    
    String clean = ReservedWords.sanitizeForDatabaseIdentifer(name);
    
    return clean;
  }
  
  private String calculateTableIndexName()
  {
    String name = INDEX_NAME_PREFIX + this.getOid();
    
    String clean = ReservedWords.sanitizeForDatabaseIdentifer(name);
    
    return clean;
  }
  
  private void exhaustiveDelete(String relationship)
  {
    OntologyStrategyIF strategy = this.getStrategyWithInstance();
    
    DeleteStrategyProviderIF deleteProvider = strategy.getDeleteStrategyProvider(this, relationship);
    
    // Create us a temp table for storing multiple parents that need to be rebuilt on the post step.
    String tempTableName = caclulateTempTableName();
    Database.createTempTable(tempTableName, TEMP_TABLE_COLUMNS, "DROP");
    try
    {
      Database.addNonUniqueIndex(tempTableName, TEMP_TERM_ID_COL, calculateTableIndexName());
      
      // Depth first search because we're using a stack.
      Stack<Term> s = new Stack<Term>();
      s.push(this);
      
      stackLoop:
      while (!s.empty())
      {
        Term current = s.pop();
        
        // Push the first child
        OIterator<? extends Business> children = current.getChildren(relationship);
        try
        {
          // We're going to save on memory here by only pushing the first (unprocessed) child. When we loop back up to this node hopefully it will be deleted.
          childLoop:
          while (children.hasNext())
          {
            Term child = (Term) children.next();
            
            // If this child is in our temp table, then it has already been processed (and not deleted). We have to do this query here to prevent infinite loops.
            if (deleteProvider.isTermAlreadyProcessed(child, s, tempTableName))
            {
              continue childLoop;
            }
            
            s.push(current);
            s.push(child);
            continue stackLoop;
          }
        }
        finally
        {
          children.close();
        }
        
        
        boolean multiParentAncestor = deleteProvider.doesAncestorHaveMultipleParents(current, s);
        if (multiParentAncestor)
        {
          @SuppressWarnings("unchecked")
          List<Term> parents = (List<Term>) current.getParents(relationship).getAll();
          
          List<String> parentOids = new ArrayList<String>();
          for (Term parent : parents)
          {
            parentOids.add(parent.getOid());
          }
          
          insertIntoTemp(current.getOid(), parentOids, s.size(), tempTableName);
        }
        else
        {
          String sql = Database.instance().buildSQLDeleteWhereStatement(tempTableName, Arrays.asList(TEMP_TERM_ID_COL + " = '" + current.getOid() + "' OR " + TEMP_PARENT_OID_COL + " = '" + current.getOid() + "'"));
          Database.parseAndExecute(sql);
          
          // TODO: We can't do this with the standard deleteWhere because it causes:
          // A SQL DDL operation was performed in a transaction on table [RUNWAY_ALLPATHS_MULTIPARENT_TEMP] after a SQL DML operation had been perfromed on the same table.  DDL statements cannot be performed if DML statements have aleady been performed on the same table within the same transaction.
          // See ruway apps ticket 558 for more details.
  //        Database.deleteWhere(TEMP_TABLE, TEMP_TERM_ID_COL + " = '" + current.getOid() + "' OR " + TEMP_PARENT_OID_COL + " = '" + current.getOid() + "'");
          
          strategy.removeTerm(current, relationship);
          if (s.size() != 0)
          {
            current.beforeDeleteTerm();
            EntityDAO.get(current.getOid()).getEntityDAO().delete();
            current.afterDeleteTerm();
          }
        }
      }
      
      
      // Post step: since we destroyed terms with multiple parents those multiple parents (that aren't our children) must now be rebuilt.
      //   We have to do 2 loops here because we need two separate phases for deleting any still existing allpaths data and then rebuilding it.
      String selectSql = Database.selectClause(Arrays.asList(TEMP_TERM_ID_COL, TEMP_PARENT_OID_COL, TEMP_DEPTH_COL), Arrays.asList(tempTableName),  new ArrayList<String>());
      ResultSet resultSet = Database.query(selectSql + " ORDER BY " + TEMP_DEPTH_COL + " DESC");
      
      try
      {
        while (resultSet.next())
        {
          String termId = resultSet.getString(TEMP_TERM_ID_COL);
          
          strategy.removeTerm(Term.get(termId), relationship);
        }
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
      
      // Post Step loop #2: Rebuild the terms with multiple parents.
      ResultSet resultSet2 = Database.query(selectSql + " ORDER BY " + TEMP_DEPTH_COL + " ASC");
      
      try
      {
        while (resultSet2.next())
        {
          String termId = resultSet2.getString(TEMP_TERM_ID_COL);
          String parentOid = resultSet2.getString(TEMP_PARENT_OID_COL);
          
          strategy.addLink(Term.get(parentOid), Term.get(termId), relationship);
        }
      }
      catch (SQLException sqlEx1)
      {
        Database.throwDatabaseException(sqlEx1);
      }
      finally
      {
        try
        {
          java.sql.Statement statement = resultSet2.getStatement();
          resultSet2.close();
          statement.close();
        }
        catch (SQLException sqlEx2)
        {
          Database.throwDatabaseException(sqlEx2);
        }
      }
    }
    finally
    {
      Database.dropTables(Arrays.asList(tempTableName));
    }
  }
  
  private void insertIntoTemp(String termId, List<String> parentOids, Integer depth, String tempTableName)
  {
    List<PreparedStatement> statements = new ArrayList<PreparedStatement>();
    
    for (String parentOid : parentOids)
    {
      List<String> bindVals = Arrays.asList("?","?","?");
      List<Object> vals = Arrays.asList(termId, parentOid, String.valueOf(depth));
      
      PreparedStatement preparedStmt = Database.buildPreparedSQLInsertStatement(tempTableName, Arrays.asList(TEMP_TERM_ID_COL, TEMP_PARENT_OID_COL, TEMP_DEPTH_COL), bindVals, vals, TEMP_TABLE_ATTRS);
      statements.add(preparedStmt);
    }
    
    Database.executeStatementBatch(statements);
  }
  /**
   * If subtypes of Term want custom delete logic they have to override this method (and not the delete method). This is because when deleting a term and all children the override "delete"
   * method is not invoked for every child term.
   */
  protected void beforeDeleteTerm()
  {
    deletePerTerm();
  }
  protected void afterDeleteTerm()
  {
  }
  protected void deletePerTerm()
  {
    
  }
  
  /**
   * Returns a <code>MdTermDAOIF</code> that defines this Component's class.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   * 
   * @return a <code>MdTermDAOIF</code> that defines this Component's class.
   */
  public MdTermDAOIF getMdTerm()
  {
    return (MdTermDAOIF) MdClassDAO.getMdClassDAO(this.getType());
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getDirectAncestors(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  public OIterator<Term> getDirectAncestors(String relationshipType)
  {
    return getStrategyWithInstance().getDirectAncestors(this, relationshipType);
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getDirectDescendants(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  public OIterator<Term> getDirectDescendants(String relationshipType)
  {
    return getStrategyWithInstance().getDirectDescendants(this, relationshipType);
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getAllAncestors(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  public OIterator<Term> getAllAncestors(String relationshipType)
  {
    return getStrategyWithInstance().getAllAncestors(this, relationshipType);
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getAllDescendants(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  public OIterator<Term> getAllDescendants(String relationshipType)
  {
    return getStrategyWithInstance().getAllDescendants(this, relationshipType);
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#isLeaf(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  public boolean isLeaf(String relationshipType)
  {
    return getStrategyWithInstance().isLeaf(this, relationshipType);
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#addLink(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  @Transaction
  public Relationship addLink(Term parent, String relationshipType)
  {
    if (this.getKey().equals(ROOT_KEY))
    {
      ImmutableRootException exception = new ImmutableRootException("Cannot modify the root Term.");
      exception.setRootName(this.getDisplayLabel().getValue());
      exception.apply();
      
      throw exception;
    }
    
    // Create the relationship
    Relationship rel = parent.addChild(this, relationshipType);
    rel.apply();
    
    // Update the strategy
    getStrategyWithInstance().addLink(parent, this, relationshipType);
    
    return rel;
  }

  /**
   * Removes the relationship between this term and the given parent. The ontology strategy will be updated such that this node (and all children nodes)
   * remain correct.
   */
  @Transaction
  public void removeLink(Term parent, String relationshipType)
  {
    if (this.getKey().equals(ROOT_KEY))
    {
      ImmutableRootException exception = new ImmutableRootException("Cannot modify the root Term.");
      exception.setRootName(this.getDisplayLabel().getValue());
      exception.apply();
      
      throw exception;
    }
    
    // Remove the relationship
    parent.removeAllChildren(this, relationshipType);
    
    // Update the strategy
    getStrategyWithInstance().removeLink(parent, this, relationshipType);
  }
  
  /**
   * Adds the term to the relationship type strategy.
   * 
   * @param relationshipType
   */
  @Transaction
  public void addTerm(String relationshipType)
  {
    getStrategyWithInstance().add(this, relationshipType);
  }

  /**
   * Returns the root term of the ontology tree defined by this Term. This root term must be created manually, and with the key "ROOT".
   * Alternatively, this static method can also be overridden by the concrete Term subtype and a different root term can be provided.
   * 
   * @param termType The CLASS of the concrete term subtype.
   * @return The root term of the ontology tree defined by this Term.
   */
  public static Term getRoot(String termType)
  {
    return (Term) Business.get(termType, ROOT_KEY);
  }

  /**
   * Returns the ontology strategy associated with this Term type. This static method can be overridden by the concrete Term subtype to
   * return a different ontology strategy. If not overridden, this Term will use the DefaultStrategy.
   * 
   * @return The ontology strategy associated with this Term type.
   */
  protected static OntologyStrategyIF createStrategy()
  {
    return DefaultStrategy.Singleton.INSTANCE;
  }

  /**
   * Assigns a strategy to the MdTerm specified by termType. This method is
   * called by a static initializer block in TermBaseGenerator.
   * 
   * @param termType
   */
  @Request
  protected static OntologyStrategyIF assignStrategy(String termType)
  {
    return assignStrategyTrasaction(termType);
  }

  @Transaction
  private static OntologyStrategyIF assignStrategyTrasaction(String termType)
  {
//    MdTermDAOIF mdTermDAOIF = MdTermDAO.getMdTermDAO(termType);
//
//    String strategyId = mdTermDAOIF.getValue(MdTermInfo.STRATEGY);

    // 1) Get an instance of the Strategy.
    OntologyStrategyIF strategy = Term.callCreateStrategy(termType);

//    if (strategyId == null || strategyId.equals(""))
//    {
//      // Instantiate an instance of the Strategy
//      strategy = Term.callCreateStrategy(termType);
//
//      // Ensure correct values of attributes, if its a stateful strategy
//      if (strategy instanceof OntologyStrategy)
//      {
//        OntologyStrategy statefulStrat = (OntologyStrategy) strategy;
//
//        if (statefulStrat.isNew())
//        {
//          statefulStrat.addStrategyState(StrategyState.UNINITIALIZED);
//          statefulStrat.apply();
//        }
//
////        MdTermDAO mdTermDAO = MdTermDAO.get(mdTermDAOIF.getOid()).getBusinessDAO();
////        mdTermDAO.setValue(MdTermInfo.STRATEGY, statefulStrat.getOid());
////        mdTermDAO.apply();
//      }
//    }
//    else
//    {
//      strategy = OntologyStrategy.get(strategyId);
//    }

    // 2) Configure the strategy
    Term.callConfigureStrategy(termType, strategy);
    
    // 3) Create and apply a root node if it doesn't exist already
//    try {
//      getRoot(termType);
//    }
//    catch (DataNotFoundException e) {
//      BusinessDAO dao = BusinessDAO.newInstance(termType);
//      Term root = (Term) Business.get(dao.getOid());
//      root.getDisplayLabel().setValue("ROOT");
//      root.setKeyName(ROOT_KEY);
//      root.apply();
//    }

    return strategy;
  }

  /**
   * Terms may override this static method "configureStrategy" to pass in additional (typically transient) instantiation parameters to the strategy.
   * If Term subclasses do not implement this method then the strategy's configure method is invoked with the Term's CLASS as the parameter.
   * The configureStrategy method is invoked with the OntologyStrategyIF instance as the one and only parameter, and it is invoked immediately after
   * the strategy is instantiated (but not initialized).
   * @param strategy
   */
  private static void callConfigureStrategy(String termType, OntologyStrategyIF strategy)
  {
    Class<?> clazz = LoaderDecorator.load(termType);
    
    try
    {
      Method m = clazz.getMethod("configureStrategy", OntologyStrategyIF.class);
      m.invoke(null, strategy);
    }
    catch (NoSuchMethodException e)
    {
      strategy.configure(termType);
    }
    catch (Exception e)
    {
      throw new CoreException(e);
    }
  }

  /**
   * Invokes the static createStrategy method on the generated Term type if it
   * exists, otherwise invokes the default Term.createStrategy. This allows for
   * users to override the strategy with a static method.
   * 
   * @param termType
   * @return
   */
  private static OntologyStrategyIF callCreateStrategy(String termType)
  {
    Class<?> clazz = LoaderDecorator.load(termType);

    OntologyStrategyIF strat;

    try
    {
      Method m = clazz.getMethod("createStrategy", new Class<?>[] {});
      strat = (OntologyStrategyIF) m.invoke(null, new Object[] {});
    }
    catch (NoSuchMethodException e)
    {
      strat = Term.createStrategy();
    }
    catch (Exception e)
    {
      throw new CoreException(e);
    }

    return strat;
  }

  /**
   * This method is delegated to by a generated getStrategy() method in
   * TermBaseGenerator.
   * 
   * @param termType
   * @return
   */
  protected static OntologyStrategyIF getStrategy(String termType)
  {
    Class<?> clazz = LoaderDecorator.load(termType);

    OntologyStrategyIF strat;

    try
    {
      Method m = clazz.getMethod("getStrategy", new Class<?>[] {});
      strat = (OntologyStrategyIF) m.invoke(null, new Object[] {});
    }
    catch (Exception e)
    {
      throw new CoreException(e);
    }

    return strat;
  }

  /**
   * This method is a convenience and only used with instance methods in this
   * class.
   * 
   * @return
   */
  protected OntologyStrategyIF getStrategyWithInstance()
  {
    return Term.getStrategy(this.getMdClass().definesType());
  }
  
  /**
   * A DisplayLabel attribute is automatically generated for every term subtype. This method will be overridden by your Term subtype.
   * 
   * @return The display label of the term.
   */
  abstract public LocalStruct getDisplayLabel();

  @Override
  @Transaction
  public void apply()
  {
    if (this.getKey().equals(ROOT_KEY))
    {
      ImmutableRootException exception = new ImmutableRootException("Cannot modify the root Term.");
      exception.setRootName(this.getDisplayLabel().getValue());
      exception.apply();
      
      throw exception;
    }
    
    super.apply();
  }
  
  public static Term get(String oid)
  {
    return (Term) Business.get(oid);
  }

  public static Term get(String definesType, String key)
  {
    return (Term) Business.get(definesType, key);
  }
  
  @Override
  public String toString()
  {
    String template = "%s [%s : %s]";
    Object[] args = new Object[] { this.getDisplayLabel().getValue(), this.getClassDisplayLabel(), this.buildKey() };
    return String.format(template, args);
  }

  public String getQualifier()
  {
    return "";
  }
  
  
  @Override
  public String getLabel()
  {
    return this.getDisplayLabel().getValue();
  }


  public List<OntologyEntryIF> getSynonyms()
  {
    return new LinkedList<OntologyEntryIF>();
  }
}
