/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
 */
package com.runwaysdk.business.ontology;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.junit.Assert;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.dataaccess.metadata.MdTermRelationshipDAO;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdTerm;
import com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategy;

/**
 * This class provides utilities for creating complex multi-term allpaths tests.
 * 
 * @author rrowlands
 */
public class AllpathsTestUtil
{
  public final String           TEST_DATA_IDENTIFIER = "AllpathsTest";

  private MdTermDAO             mdTerm;

  private MdTermRelationshipDAO mdTermRel;

  public BusinessDAO            root;

  public BusinessDAO            spacer;

  public BusinessDAO            testRoot;

  public BusinessDAO            a;

  public BusinessDAO            b;

  public BusinessDAO            c;

  public BusinessDAO            d;

  public BusinessDAO            e;

  public BusinessDAO            f;

  public BusinessDAO            g;

  public BusinessDAO            h;

  public BusinessDAO            i;

  public BusinessDAO            j;

  public BusinessDAO            k;

  public BusinessDAO            l;

  public AllpathsTestUtil(MdTermDAO mdTerm, MdTermRelationshipDAO mdTermRel)
  {
    this.mdTerm = mdTerm;
    this.mdTermRel = mdTermRel;
  }

  public void createTestData()
  {
    root = BusinessDAO.get(this.mdTerm.definesType(), Term.ROOT_KEY).getBusinessDAO();

    // Root Term
    // / \
    // Spacer \
    // / \
    // testRoot F
    // / | /
    // A B /
    // | / | \ /
    // | C E G
    // | /| \ /
    // | / | H
    // D | /
    // | | I
    // J |
    // \ |
    // K
    // |
    // L

    // Spacer
    spacer = BusinessDAO.newInstance(mdTerm.definesType());
    spacer.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, TEST_DATA_IDENTIFIER + " Spacer");
    spacer.apply();
    root.addChild(spacer, mdTermRel.definesType()).apply();

    // Test Root
    testRoot = BusinessDAO.newInstance(mdTerm.definesType());
    testRoot.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, TEST_DATA_IDENTIFIER + " TestRoot");
    testRoot.apply();
    spacer.addChild(testRoot, mdTermRel.definesType()).apply();

    // A
    a = BusinessDAO.newInstance(mdTerm.definesType());
    a.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, TEST_DATA_IDENTIFIER + " A");
    a.apply();
    testRoot.addChild(a, mdTermRel.definesType()).apply();

    // B
    b = BusinessDAO.newInstance(mdTerm.definesType());
    b.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, TEST_DATA_IDENTIFIER + " B");
    b.apply();
    testRoot.addChild(b, mdTermRel.definesType()).apply();

    // C
    c = BusinessDAO.newInstance(mdTerm.definesType());
    c.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, TEST_DATA_IDENTIFIER + " C");
    c.apply();
    b.addChild(c, mdTermRel.definesType()).apply();

    // D
    d = BusinessDAO.newInstance(mdTerm.definesType());
    d.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, TEST_DATA_IDENTIFIER + " D");
    d.apply();
    c.addChild(d, mdTermRel.definesType()).apply();
    a.addChild(d, mdTermRel.definesType()).apply();

    // E
    e = BusinessDAO.newInstance(mdTerm.definesType());
    e.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, TEST_DATA_IDENTIFIER + " E");
    e.apply();
    b.addChild(e, mdTermRel.definesType()).apply();

    // F
    f = BusinessDAO.newInstance(mdTerm.definesType());
    f.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, TEST_DATA_IDENTIFIER + " F");
    f.apply();
    root.addChild(f, mdTermRel.definesType()).apply();

    // G
    g = BusinessDAO.newInstance(mdTerm.definesType());
    g.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, TEST_DATA_IDENTIFIER + " G");
    g.apply();
    b.addChild(g, mdTermRel.definesType()).apply();
    f.addChild(g, mdTermRel.definesType()).apply();

    // H
    h = BusinessDAO.newInstance(mdTerm.definesType());
    h.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, TEST_DATA_IDENTIFIER + " H");
    h.apply();
    g.addChild(h, mdTermRel.definesType()).apply();
    e.addChild(h, mdTermRel.definesType()).apply();

    // I
    i = BusinessDAO.newInstance(mdTerm.definesType());
    i.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, TEST_DATA_IDENTIFIER + " I");
    i.apply();
    h.addChild(i, mdTermRel.definesType()).apply();

    // J
    j = BusinessDAO.newInstance(mdTerm.definesType());
    j.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, TEST_DATA_IDENTIFIER + " J");
    j.apply();
    d.addChild(j, mdTermRel.definesType()).apply();

    // K
    k = BusinessDAO.newInstance(mdTerm.definesType());
    k.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, TEST_DATA_IDENTIFIER + " K");
    k.apply();
    j.addChild(k, mdTermRel.definesType()).apply();
    c.addChild(k, mdTermRel.definesType()).apply();

    // L
    l = BusinessDAO.newInstance(mdTerm.definesType());
    l.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, TEST_DATA_IDENTIFIER + " L");
    l.apply();
    k.addChild(l, mdTermRel.definesType()).apply();

    this.getStrategy().reinitialize(mdTermRel.definesType());
  }

  public void destroyTestData()
  {
    
    OntologyStrategyIF strategy = this.getStrategy();
    strategy.shutdown();

    BusinessQuery q = new QueryFactory().businessQuery(mdTerm.definesType());
    OIterator<? extends Business> it = q.getIterator();
    while (it.hasNext())
    {
      Term t = (Term) it.next();
      if (t.getDisplayLabel().getValue().contains(TEST_DATA_IDENTIFIER))
      {
        TestFixtureFactory.delete(EntityDAO.get(t.getOid()));
      }
    }

    long pre = System.nanoTime();

    strategy.initialize(mdTermRel.definesType());

    long post = System.nanoTime();
    long elapsed = ( post - pre ) / 1000000000;
    System.out.println("Rebuilding the allpaths table took: " + elapsed + " seconds");
  }

  public void validateAllpaths()
  {
    Stack<Term> s = new Stack<Term>();
    s.push(Term.get(root.getOid()));

    while (!s.empty())
    {
      Term current = s.pop();

      OIterator<? extends Business> children = current.getChildren(this.mdTermRel.definesType());
      try
      {
        while (children.hasNext())
        {
          Term child = (Term) children.next();

          if (child.getDisplayLabel().getValue().contains(TEST_DATA_IDENTIFIER))
          {
            s.push(child);
          }
        }
      }
      finally
      {
        children.close();
      }

      validateAllpaths(current);
    }
  }

  public void validateAllpaths(Term term)
  {
    enforceAPExist(term, term);
    Set<String> ancestors = validateParentAllpaths(term, term);
    assertNumChildEntries(term, ancestors.size() + 1);
  }

  public Set<String> validateParentAllpaths(Term child, Term parent)
  {
    if (parent.equals(root))
    {
      return new HashSet<String>();
    }

    Set<String> ancestors = new HashSet<String>();

    OIterator<? extends Business> parents = parent.getParents(mdTermRel.definesType());
    try
    {
      while (parents.hasNext())
      {
        Term parentOfParent = (Term) parents.next();

        enforceAPExist(child, parentOfParent);

        ancestors.addAll(validateParentAllpaths(child, parentOfParent));

        ancestors.add(parentOfParent.getOid());
      }
    }
    finally
    {
      parents.close();
    }

    return ancestors;
  }

  public void enforceAPExist(Term child, Term parent)
  {
    OntologyStrategyIF strategy = this.getStrategy();

    if (strategy instanceof DatabaseAllPathsStrategy)
    {
      DatabaseAllPathsStrategy apStrat = (DatabaseAllPathsStrategy) strategy;

      BusinessQuery apq = new QueryFactory().businessQuery(apStrat.getAllPaths().definesType());
      apq.WHERE(apq.get(DatabaseAllPathsStrategy.CHILD_TERM_ATTR).EQ(child.getOid()));
      apq.AND(apq.get(DatabaseAllPathsStrategy.PARENT_TERM_ATTR).EQ(parent.getOid()));
      OIterator<? extends Business> it = apq.getIterator();
      if (!it.hasNext())
      {
        Assert.fail("Expected AllPaths entry between parent [" + parent.getDisplayLabel().getValue() + "] and child [" + child.getDisplayLabel().getValue() + "].");
      }
      it.next();
      if (it.hasNext())
      {
        Assert.fail("Too many AllPaths entries defined between parent [" + parent.getDisplayLabel().getValue() + "] and child [" + child.getDisplayLabel().getValue() + "].");
      }
    }
    else
    {
      throw new UnsupportedOperationException();
    }
  }

  public void assertNumChildEntries(Term child, int num)
  {
    OntologyStrategyIF strategy = this.getStrategy();

    if (strategy instanceof DatabaseAllPathsStrategy)
    {
      DatabaseAllPathsStrategy apStrat = (DatabaseAllPathsStrategy) strategy;

      BusinessQuery apq = new QueryFactory().businessQuery(apStrat.getAllPaths().definesType());
      apq.WHERE(apq.get(DatabaseAllPathsStrategy.CHILD_TERM_ATTR).EQ(child.getOid()));
      Assert.assertEquals("Number of allpaths entries for child [" + child.getDisplayLabel().getValue() + "] is incorrect.", num, apq.getCount());
    }
    else
    {
      throw new UnsupportedOperationException();
    }
  }

  public void ensureNoTestDataExists()
  {
    OntologyStrategyIF strategy = this.getStrategy();

    if (strategy instanceof DatabaseAllPathsStrategy)
    {
      BusinessQuery apq = new QueryFactory().businessQuery(mdTerm.definesType());
      OIterator<? extends Business> it = apq.getIterator();
      while (it.hasNext())
      {
        Term ap = (Term) it.next();
        String termName = ap.getDisplayLabel().getValue();

        if (termName.contains(TEST_DATA_IDENTIFIER))
        {
          Assert.fail("The test term [" + termName + "] still exists and it shouldn't.");
          // Assert.fail("Test AllPaths data exists between parent [" +
          // parentName + "] and child [" + childName + "].");
        }
      }
    }
    else
    {
      throw new UnsupportedOperationException();
    }
  }
  
  private OntologyStrategyIF getStrategy()
  {
    OntologyStrategyIF strategy = OntologyStrategyFactory.get(mdTerm.definesType(), new OntologyStrategyBuilderIF()
    {
      @Override
      public OntologyStrategyIF build()
      {
        return DatabaseAllPathsStrategy.factory(mdTerm.definesType());
      }
    });
    strategy.configure(mdTerm.definesType());
    return strategy;
  }  

  // private void checkDB()
  // {
  // ArrayList<ArrayList<String>> arrs = new ArrayList<ArrayList<String>>();
  //
  // ResultSet results = null;
  // try
  // {
  // results = Database.query("select\n" +
  // "child_dl.default_locale as child,\n" +
  // "parent_dl.default_locale as parent\n" +
  // "from\n" +
  // "alphabet_all_paths_table ap\n" +
  // "inner join alphabet child on ap.child_term = child.oid\n" +
  // "inner join alphabet_display_label child_dl on child_dl.oid =
  // child.display_label\n" +
  // "inner join alphabet parent on ap.parent_term = parent.oid\n" +
  // "inner join alphabet_display_label parent_dl on parent_dl.oid =
  // parent.display_label\n" +
  // "where\n" +
  // "child_dl.default_locale = 'AllpathsTest I'");
  //
  // if (results.next())
  // {
  // ArrayList<String> list = new ArrayList<String>();
  //
  // list.add(results.getString("child"));
  // list.add(results.getString("parent"));
  // arrs.add(list);
  // }
  // }
  // catch (SQLException e)
  // {
  // e.printStackTrace();
  // }
  // finally
  // {
  // if (results != null)
  // {
  // try
  // {
  // results.close();
  // }
  // catch (SQLException e)
  // {
  // e.printStackTrace();
  // }
  // }
  // }
  //
  // arrs.toString();
  // }
}
