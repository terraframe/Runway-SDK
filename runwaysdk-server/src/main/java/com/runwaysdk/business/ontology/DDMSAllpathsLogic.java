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
package com.runwaysdk.business.ontology;

import java.sql.Savepoint;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.runwaysdk.business.RelationshipQuery;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.DuplicateDataDatabaseException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategy;

/**
 * This class contains logic that was ported over from DDMS (but that we may/may not want to continue using
 *   in the future, depending on how performant it is). This class is not actually used in DDMS. I separated
 *   it like this so that we would know that its very much still under review. If you review this code and
 *   you also run a profiler on it and we decide that we like it, we can move it into the DatabaseAllpathsStrategy.
 *   This logic would likely, however, be better off written in SQL.
 * 
 * @author rrowlands
 */
public class DDMSAllpathsLogic
{
  private static final String CHILD_TERM_ATTR = DatabaseAllPathsStrategy.CHILD_TERM_ATTR;
  
  private static final String PARENT_TERM_ATTR = DatabaseAllPathsStrategy.PARENT_TERM_ATTR;
  
  private DatabaseAllPathsStrategy strategy;
  
  private String relationshipType;
  
  public DDMSAllpathsLogic(DatabaseAllPathsStrategy strategy, String relationshipType)
  {
    this.strategy = strategy;
    this.relationshipType = relationshipType;
  }
  
  /**
   * Removes the term and all its children from the allpaths table.
   * 
   * @param termId
   */
  public void deleteTermAndChildrenFromAllPaths(String rootId, String relationshipType)
  {
    // Queue results in a breadth first traverse
    Queue<String> qNext = new ArrayDeque<String>();
    qNext.offer(rootId);
    while (qNext.size() > 0)
    {
      String sCurrent = qNext.poll();
      Term tCurrent = Term.get(sCurrent);
      
      strategy.removeTerm(tCurrent, relationshipType);
      
      @SuppressWarnings("unchecked")
      OIterator<Term> childIt = (OIterator<Term>) tCurrent.getChildren(relationshipType);
      for (Term child: childIt)
      {
        qNext.offer(child.getOid());
      }
    }
  }
  
  @Transaction
  public void updateAllPathForTerm(String childOid, String parentOid, boolean copyChildren)
  {
    createPath(childOid, childOid);

    // If an oid of a parent is given, only build paths between this node, the
    // given parent
    // and that parent's parents. This is ideal for copies, so we don't have to
    // traverse
    // the paths of existing parents.
    List<String> parentOidList;
    if (parentOid != null)
    {
      parentOidList = this.getRecursiveParentOids(parentOid);
      parentOidList.add(0, parentOid);
    }
    else
    {
      parentOidList = this.getRecursiveParentOids(childOid);
    }

    for (String someParentOid : parentOidList)
    {
      createPath(someParentOid, childOid);
    }

    if (copyChildren)
    {
      // Update paths of the children.  
      List<String> childOfChildOidList = this.getChildOids(childOid);
      for (String childOfChild : childOfChildOidList)
      {
        if (parentOid != null)
        {
          updateAllPathForTerm(childOfChild, childOid, copyChildren);
        }
        else
        {
          updateAllPathForTerm(childOfChild, null, copyChildren);
        }
      }
    }
  }

  private void createPath(String parentOid, String childOid)
  {
    // create save point
    Savepoint savepoint = Database.setSavepoint();

    try
    {
      BusinessDAO instance = BusinessDAO.newInstance(strategy.getAllPaths(this.relationshipType).definesType());
      instance.setValue(PARENT_TERM_ATTR, parentOid);
      instance.setValue(CHILD_TERM_ATTR, childOid);
      instance.apply();
    }
    catch (DuplicateDataDatabaseException ex)
    {
      // This might happen. Relationship already exists.
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
  
  public List<String> getRecursiveParentOids(String childOid)
  {
    QueryFactory queryFactory = new QueryFactory();
    
    RelationshipQuery relQ = queryFactory.relationshipQuery(this.relationshipType);
    ValueQuery valueQuery = queryFactory.valueQuery();

    valueQuery.SELECT(relQ.parentOid());
    valueQuery.WHERE(relQ.childOid().EQ(childOid));
    
    OIterator<ValueObject> qit = valueQuery.getIterator();

    List<String> parentOidList = new LinkedList<String>();
    
    for (ValueObject valueObject : qit)
    {
      String parentOid = valueObject.getValue(RelationshipInfo.PARENT_OID);
      parentOidList.add(parentOid);
      parentOidList.addAll(getRecursiveParentOids(parentOid));
    }

    return parentOidList;
  }
  
  public List<String> getChildOids(String parentOid)
  {
    List<String> childOidList = new LinkedList<String>();
    
    QueryFactory queryFactory = new QueryFactory();
    RelationshipQuery relQ = queryFactory.relationshipQuery(this.relationshipType);
    ValueQuery valueQuery = queryFactory.valueQuery();

    valueQuery.SELECT(relQ.childOid());
    valueQuery.WHERE(relQ.parentOid().EQ(parentOid));

    List<ValueObject> valueObjectList = valueQuery.getIterator().getAll();

    for (ValueObject valueObject : valueObjectList)
    {
      String childOid = valueObject.getValue(RelationshipInfo.CHILD_OID);
      childOidList.add(childOid);
    }
    
    return childOidList;
  }
}
