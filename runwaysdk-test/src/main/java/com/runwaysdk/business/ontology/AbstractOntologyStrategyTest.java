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
/**
*
*/
package com.runwaysdk.business.ontology;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.runwaysdk.business.Business;
import com.runwaysdk.constants.AssociationType;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.constants.MdTermRelationshipInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.dataaccess.metadata.MdTermRelationshipDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdRelationship;
import com.runwaysdk.system.metadata.MdTerm;
import com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategy;

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
public abstract class AbstractOntologyStrategyTest
{
  public static class TermHolder
  {
    public static String termAId;

    public static String termBId;

    public static String termCId;

    public static Term getTermA()
    {
      return getTerm(termAId);
    }

    public static Term getTermB()
    {
      return getTerm(termBId);
    }

    public static Term getTermC()
    {
      return getTerm(termCId);
    }

    public static Term getTerm(String id)
    {
      return (Term) Term.get(id);
    }
  }

  public static String                mdTermId;

  public static String                mdTermRelationshipId;

  public static final String          PACKAGE    = "com.runwaysdk.test.business.ontology";

  public static MdTermDAO             mdTerm;

  public static MdTermRelationshipDAO mdTermRelationship;

  protected static boolean            didDoSetUp = false;

  public abstract String getInitializeStrategySource();

  public abstract Class<?> getOntologyStrategy();

  public AbstractOntologyStrategyTest() throws Exception
  {
    didDoSetUp = false;
  }

  /**
   * Unfortunately we have to do this hack instead of the traditional classSetUp
   * fixture method because the classSetUp method must be static, but we require
   * that our abstract methods are overridden by subclasses before we call the
   * class set up so our's can't be static. This is the only way to achieve a
   * non-static class initialization.
   */
  @Request
  @Before
  public void setUp()
  {
    if (didDoSetUp == false)
    {
      doSetUp();
      initStrat(mdTerm, mdTermRelationship.definesType());
    }

    didDoSetUp = true;
  }

  @Request
  @After
  public void tearDown()
  {

  }

  public static void initStrat(MdTermDAO mdTerm, String mdTermRelationshipDefinesType)
  {
    OntologyStrategyIF strategy = DatabaseAllPathsStrategy.factory(mdTerm.definesType());
    strategy.configure(mdTerm.definesType());
    strategy.initialize(mdTermRelationshipDefinesType);
  }

  public static OntologyStrategyIF getStrategy(String mdTermDefinesType)
  {
    return DatabaseAllPathsStrategy.factory(mdTermDefinesType);    

//    return Term.getStrategy(mdTermDefinesType);
  }

  public static void shutDownStrat(String mdTermDefinesType)
  {
    DatabaseAllPathsStrategy.factory(mdTermDefinesType).shutdown();
  }

  @Transaction
  protected void doSetUp()
  {
    mdTerm = MdTermDAO.newInstance();
    mdTerm.setValue(MdTermInfo.NAME, "Alphabet");
    mdTerm.setValue(MdTermInfo.PACKAGE, PACKAGE);
    mdTerm.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdTerm.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdTerm.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTerm.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
    String source = "package " + PACKAGE + ";\n" + "public class Alphabet extends AlphabetBase \n" + "{\n" + "public Alphabet()\n" + "{\n" + "super();\n" + "}\n" + "public static " + OntologyStrategyIF.class.getName() + " createStrategy()\n" + "{\n return new " + this.getOntologyStrategy().getName() + "();\n" + "}\n" + "public static void configureStrategy(" + OntologyStrategyIF.class.getName() + " strategy)\n" + "{\n " + this.getInitializeStrategySource() + "\n" + "}\n" + "}\n";
    mdTerm.setValue(MdClassInfo.STUB_SOURCE, source);
    mdTerm.apply();

    mdTermRelationship = MdTermRelationshipDAO.newInstance();
    mdTermRelationship.setValue(MdTreeInfo.NAME, "Sequential");
    mdTermRelationship.setValue(MdTreeInfo.PACKAGE, PACKAGE);
    mdTermRelationship.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sequential Relationship");
    mdTermRelationship.setValue(MdTreeInfo.PARENT_MD_BUSINESS, mdTerm.getId());
    mdTermRelationship.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
    mdTermRelationship.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Previous Letter");
    mdTermRelationship.setValue(MdTreeInfo.CHILD_MD_BUSINESS, mdTerm.getId());
    mdTermRelationship.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
    mdTermRelationship.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Next Letter");
    mdTermRelationship.setValue(MdTreeInfo.PARENT_METHOD, "ParentTerm");
    mdTermRelationship.setValue(MdTreeInfo.CHILD_METHOD, "ChildTerm");
    mdTermRelationship.setGenerateMdController(false);
    mdTermRelationship.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.RELATIONSHIP.getId());
    mdTermRelationship.apply();

    // Gotta have a root term.
    BusinessDAO root = BusinessDAO.newInstance(mdTerm.definesType());
    root.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "ROOT");
    root.setKey(Term.ROOT_KEY);
    root.apply();

    // Lets define a relationship A > B > C between these terms.
    BusinessDAO termA = BusinessDAO.newInstance(mdTerm.definesType());
    termA.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "termA");
    termA.apply();
    root.addChild(termA, mdTermRelationship.definesType()).apply();
    BusinessDAO termB = BusinessDAO.newInstance(mdTerm.definesType());
    termB.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "termB");
    termB.apply();
    termA.addChild(termB, mdTermRelationship.definesType()).apply();
    BusinessDAO termC = BusinessDAO.newInstance(mdTerm.definesType());
    termC.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "termC");
    termC.apply();
    termB.addChild(termC, mdTermRelationship.definesType()).apply();

    TermHolder.termAId = termA.getId();
    TermHolder.termBId = termB.getId();
    TermHolder.termCId = termC.getId();

    mdTermId = mdTerm.getId();
    mdTermRelationshipId = mdTermRelationship.getId();
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    shutDownStrat(mdTerm.definesType());

    TestFixtureFactory.delete(TermHolder.getTermA());
    TestFixtureFactory.delete(TermHolder.getTermB());
    TestFixtureFactory.delete(TermHolder.getTermC());

    MdRelationship.get(mdTermRelationshipId).delete();
    MdBusiness.get(mdTermId).delete();

    didDoSetUp = false;
  }

  // @Request @Test public void testInitialized() {
  // Assert.assertTrue(Term.getStrategy(mdTerm.definesType()).isInitialized());
  // }

  @Request
  @Test
  public void testCopyTerm() throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
  {
    // Do the copy from the strategy
    Class<?> clazz = LoaderDecorator.load(mdTerm.definesType());
    Term termZ = (Term) clazz.newInstance();
    termZ.getDisplayLabel().setValue("termZ");
    termZ.apply();

    TermHolder.getTermA().addLink(termZ, mdTermRelationship.definesType());

    Term ret = (Term) termZ.getChildren(mdTermRelationship.definesType()).next();
    Assert.assertNotNull(ret);
    Assert.assertNotNull(ret.getChildren(mdTermRelationship.definesType()).next());

    termZ.delete();
  }

  @Request
  @Test
  public void testIsLeafNode() throws InstantiationException, IllegalAccessException
  {
    Class<?> clazz = LoaderDecorator.load(mdTerm.definesType());
    Term termL = (Term) clazz.newInstance();
    termL.getDisplayLabel().setValue("termZ");
    termL.apply();

    Assert.assertTrue(termL.isLeaf(mdTermRelationship.definesType()));
    Assert.assertFalse(TermHolder.getTermA().isLeaf(mdTermRelationship.definesType()));
    Assert.assertFalse(TermHolder.getTermB().isLeaf(mdTermRelationship.definesType()));
    Assert.assertTrue(TermHolder.getTermC().isLeaf(mdTermRelationship.definesType()));

    termL.delete();
  }

  @Request
  @Test
  public void testGetDirectDescendants() throws Exception
  {
    Class<?> clazz = LoaderDecorator.load(mdTerm.definesType());
    Term parent = (Term) clazz.newInstance();
    parent.getDisplayLabel().setValue("parent");
    parent.apply();

    Term child1 = (Term) clazz.newInstance();
    child1.getDisplayLabel().setValue("child1");
    child1.apply();
    parent.addChild(child1, mdTermRelationship.definesType()).apply();
    Term child2 = (Term) clazz.newInstance();
    child2.getDisplayLabel().setValue("child2");
    child2.apply();
    parent.addChild(child2, mdTermRelationship.definesType()).apply();
    Term child3 = (Term) clazz.newInstance();
    child3.getDisplayLabel().setValue("child3");
    child3.apply();
    parent.addChild(child3, mdTermRelationship.definesType()).apply();

    // Create a child of a child to make sure it doesn't get returned
    Term childOfChild = (Term) clazz.newInstance();
    childOfChild.getDisplayLabel().setValue("childOfChild");
    childOfChild.apply();
    child3.addChild(childOfChild, mdTermRelationship.definesType()).apply();

    List<Term> descendants = parent.getDirectDescendants(mdTermRelationship.definesType()).getAll();
    Assert.assertEquals(3, descendants.size());
    Assert.assertTrue(descendants.get(0) instanceof Term);
    Assert.assertTrue(descendants.get(1) instanceof Term);
    Assert.assertTrue(descendants.get(2) instanceof Term);
  }

  @Request
  @Test
  public void testGetDirectAncestors() throws Exception
  {
    Class<?> clazz = LoaderDecorator.load(mdTerm.definesType());
    Term child = (Term) clazz.newInstance();
    child.getDisplayLabel().setValue("child");
    child.apply();

    Term parent1 = (Term) clazz.newInstance();
    parent1.getDisplayLabel().setValue("parent1");
    parent1.apply();
    child.addParent(parent1, mdTermRelationship.definesType()).apply();
    Term parent2 = (Term) clazz.newInstance();
    parent2.getDisplayLabel().setValue("parent2");
    parent2.apply();
    child.addParent(parent2, mdTermRelationship.definesType()).apply();
    Term parent3 = (Term) clazz.newInstance();
    parent3.getDisplayLabel().setValue("parent3");
    parent3.apply();
    child.addParent(parent3, mdTermRelationship.definesType()).apply();

    // Create a parent of a parent to make sure it doesn't get returned
    Term parentOfParent = (Term) clazz.newInstance();
    parentOfParent.getDisplayLabel().setValue("parentOfParent");
    parentOfParent.apply();
    parent3.addParent(parentOfParent, mdTermRelationship.definesType()).apply();

    List<Term> ancestors = child.getDirectAncestors(mdTermRelationship.definesType()).getAll();
    Assert.assertEquals(3, ancestors.size());
    Assert.assertTrue(ancestors.get(0) instanceof Term);
    Assert.assertTrue(ancestors.get(1) instanceof Term);
    Assert.assertTrue(ancestors.get(2) instanceof Term);
  }

  @Request
  @Test
  public void testGetAllDescendants() throws Exception
  {
    List<Term> descendents = TermHolder.getTermA().getAllDescendants(mdTermRelationship.definesType()).getAll();
    Iterator<Term> it = descendents.iterator();
    Assert.assertEquals(it.next(), TermHolder.getTermB());
    Assert.assertEquals(it.next(), TermHolder.getTermC());
  }

  @Request
  @Test
  public void testGetAllAncestors() throws Exception
  {
    List<Term> ancestors = TermHolder.getTermC().getAllAncestors(mdTermRelationship.definesType()).getAll();

    Assert.assertEquals(3, ancestors.size());
    Assert.assertTrue(ancestors.contains(TermHolder.getTermB()));
    Assert.assertTrue(ancestors.contains(TermHolder.getTermA()));
    Assert.assertTrue(ancestors.contains(Business.get(this.mdTerm.definesType(), Term.ROOT_KEY)));
  }

  @Request
  @Test
  public void testDeleteInternalTerm() throws Exception
  {
    Class<?> clazz = LoaderDecorator.load(mdTerm.definesType());

    Term child = (Term) clazz.newInstance();
    child.getDisplayLabel().setValue("child");
    child.apply();
    child.addTerm(mdTermRelationship.definesType());

    Term parent1 = (Term) clazz.newInstance();
    parent1.getDisplayLabel().setValue("parent1");
    parent1.apply();
    parent1.addTerm(mdTermRelationship.definesType());

    Term parent2 = (Term) clazz.newInstance();
    parent2.getDisplayLabel().setValue("parent2");
    parent2.apply();
    parent2.addTerm(mdTermRelationship.definesType());

    Term parent3 = (Term) clazz.newInstance();
    parent3.getDisplayLabel().setValue("parent3");
    parent3.apply();
    parent3.addTerm(mdTermRelationship.definesType());

    Term parent4 = (Term) clazz.newInstance();
    parent4.getDisplayLabel().setValue("parent4");
    parent4.apply();
    parent4.addTerm(mdTermRelationship.definesType());

    // Path 1: C -> 1 -> 2 -> 3
    // Path 2: C -> 1 -> 4
    child.addLink(parent1, mdTermRelationship.definesType());
    parent1.addLink(parent2, mdTermRelationship.definesType());
    parent2.addLink(parent3, mdTermRelationship.definesType());
    parent1.addLink(parent4, mdTermRelationship.definesType());

    // Ensure the setup is correct
    Assert.assertEquals(3, parent3.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
    Assert.assertEquals(2, parent4.getAllDescendants(mdTermRelationship.definesType()).getAll().size());

    parent1.delete();

    Assert.assertEquals(1, parent3.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
    Assert.assertEquals(0, parent4.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
  }

  @Request
  @Test
  public void testDeleteLeafTerm() throws Exception
  {
    Class<?> clazz = LoaderDecorator.load(mdTerm.definesType());

    Term child = (Term) clazz.newInstance();
    child.getDisplayLabel().setValue("child");
    child.apply();
    child.addTerm(mdTermRelationship.definesType());

    Term parent1 = (Term) clazz.newInstance();
    parent1.getDisplayLabel().setValue("parent1");
    parent1.apply();
    parent1.addTerm(mdTermRelationship.definesType());

    Term parent2 = (Term) clazz.newInstance();
    parent2.getDisplayLabel().setValue("parent2");
    parent2.apply();
    parent2.addTerm(mdTermRelationship.definesType());

    Term parent3 = (Term) clazz.newInstance();
    parent3.getDisplayLabel().setValue("parent3");
    parent3.apply();
    parent3.addTerm(mdTermRelationship.definesType());

    Term parent4 = (Term) clazz.newInstance();
    parent4.getDisplayLabel().setValue("parent4");
    parent4.apply();
    parent4.addTerm(mdTermRelationship.definesType());

    // Path 1: C -> 1 -> 3
    // Path 2: C -> 2 -> 4
    child.addLink(parent1, mdTermRelationship.definesType());
    child.addLink(parent2, mdTermRelationship.definesType());
    parent1.addLink(parent3, mdTermRelationship.definesType());
    parent2.addLink(parent4, mdTermRelationship.definesType());

    // Ensure the setup is correct
    Assert.assertEquals(2, parent3.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
    Assert.assertEquals(2, parent4.getAllDescendants(mdTermRelationship.definesType()).getAll().size());

    child.delete();

    Assert.assertEquals(1, parent3.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
    Assert.assertEquals(1, parent4.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
  }

  @Request
  @Test
  public void testRemoveInternalLink() throws Exception
  {
    Class<?> clazz = LoaderDecorator.load(mdTerm.definesType());

    Term child = (Term) clazz.newInstance();
    child.getDisplayLabel().setValue("child");
    child.apply();
    child.addTerm(mdTermRelationship.definesType());

    Term parent1 = (Term) clazz.newInstance();
    parent1.getDisplayLabel().setValue("parent1");
    parent1.apply();
    parent1.addTerm(mdTermRelationship.definesType());

    Term parent2 = (Term) clazz.newInstance();
    parent2.getDisplayLabel().setValue("parent2");
    parent2.apply();
    parent2.addTerm(mdTermRelationship.definesType());

    Term parent3 = (Term) clazz.newInstance();
    parent3.getDisplayLabel().setValue("parent3");
    parent3.apply();
    parent3.addTerm(mdTermRelationship.definesType());

    Term parent4 = (Term) clazz.newInstance();
    parent4.getDisplayLabel().setValue("parent4");
    parent4.apply();
    parent4.addTerm(mdTermRelationship.definesType());

    // Path 1: C -> 1 -> 2 -> 3
    // Path 2: C -> 1 -> 4
    child.addLink(parent1, mdTermRelationship.definesType());
    parent1.addLink(parent2, mdTermRelationship.definesType());
    parent2.addLink(parent3, mdTermRelationship.definesType());
    parent1.addLink(parent4, mdTermRelationship.definesType());

    // Ensure the setup is correct
    Assert.assertEquals(3, parent3.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
    Assert.assertEquals(2, parent4.getAllDescendants(mdTermRelationship.definesType()).getAll().size());

    parent1.removeLink(parent4, mdTermRelationship.definesType());

    Assert.assertEquals(3, parent3.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
    Assert.assertEquals(0, parent4.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
  }

  @Request
  @Test
  public void testRemoveLeafLink() throws Exception
  {
    Class<?> clazz = LoaderDecorator.load(mdTerm.definesType());

    Term child = (Term) clazz.newInstance();
    child.getDisplayLabel().setValue("child");
    child.apply();
    child.addTerm(mdTermRelationship.definesType());

    Term parent1 = (Term) clazz.newInstance();
    parent1.getDisplayLabel().setValue("parent1");
    parent1.apply();
    parent1.addTerm(mdTermRelationship.definesType());

    Term parent2 = (Term) clazz.newInstance();
    parent2.getDisplayLabel().setValue("parent2");
    parent2.apply();
    parent2.addTerm(mdTermRelationship.definesType());

    Term parent3 = (Term) clazz.newInstance();
    parent3.getDisplayLabel().setValue("parent3");
    parent3.apply();
    parent3.addTerm(mdTermRelationship.definesType());

    Term parent4 = (Term) clazz.newInstance();
    parent4.getDisplayLabel().setValue("parent4");
    parent4.apply();
    parent4.addTerm(mdTermRelationship.definesType());

    // Path 1: C -> 1 -> 3
    // Path 2: C -> 2 -> 4
    child.addLink(parent1, mdTermRelationship.definesType());
    child.addLink(parent2, mdTermRelationship.definesType());
    parent1.addLink(parent3, mdTermRelationship.definesType());
    parent2.addLink(parent4, mdTermRelationship.definesType());

    // Ensure the setup is correct
    Assert.assertEquals(2, parent3.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
    Assert.assertEquals(2, parent4.getAllDescendants(mdTermRelationship.definesType()).getAll().size());

    child.removeLink(parent1, mdTermRelationship.definesType());

    Assert.assertEquals(1, parent3.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
    Assert.assertEquals(2, parent4.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
  }

  @Request
  @Test
  public void testCopyTermsAndGetAllDescendants() throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException
  {
    Class<?> clazz = LoaderDecorator.load(mdTerm.definesType());

    Term aa = (Term) clazz.newInstance();
    aa.getDisplayLabel().setValue("aa");
    aa.apply();
    aa.addTerm(mdTermRelationship.definesType());

    Term bb = (Term) clazz.newInstance();
    bb.getDisplayLabel().setValue("bb");
    bb.apply();
    bb.addTerm(mdTermRelationship.definesType());

    Term cc = (Term) clazz.newInstance();
    cc.getDisplayLabel().setValue("cc");
    cc.apply();
    cc.addTerm(mdTermRelationship.definesType());

    bb.addLink(aa, mdTermRelationship.definesType());
    cc.addLink(bb, mdTermRelationship.definesType());

    List<Term> descendents = aa.getAllDescendants(mdTermRelationship.definesType()).getAll();
    Iterator<Term> it = descendents.iterator();
    Assert.assertEquals(it.next().getId(), bb.getId());
    Assert.assertEquals(it.next().getId(), cc.getId());
  }
}
