/**
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.business.ontology;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import junit.framework.TestResult;

import com.runwaysdk.constants.AssociationType;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.constants.MdTermRelationshipInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.dataaccess.metadata.MdTermRelationshipDAO;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdRelationship;
import com.runwaysdk.system.metadata.MdTerm;

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
public abstract class AbstractOntologyStrategyTest extends TestCase
{
  @Override
  public TestResult run()
  {
    return super.run();
  }

  @Override
  public void run(TestResult testResult)
  {
    super.run(testResult);
  }

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

  public static final String          PACKAGE = "com.runwaysdk.test.business.ontology";

  public static MdTermDAO             mdTerm;

  public static MdTermRelationshipDAO mdTermRelationship;

  public abstract String getInitializeStrategySource();

  public abstract Class<?> getOntologyStrategy();

  public AbstractOntologyStrategyTest() throws Exception
  {
    if (didDoSetUp == false)
    {
      doSetUp();
    }

    didDoSetUp = true;
  }

  protected static boolean didDoSetUp = false;

  protected void doSetUp() throws Exception
  {
    mdTerm = MdTermDAO.newInstance();
    mdTerm.setValue(MdTermInfo.NAME, "Alphabet");
    mdTerm.setValue(MdTermInfo.PACKAGE, PACKAGE);
    mdTerm.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdTerm.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdTerm.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTerm.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
    String source = "package " + PACKAGE + ";\n" + "public class Alphabet extends AlphabetBase implements com.runwaysdk.generation.loader.Reloadable\n" + "{\n" + "public Alphabet()\n" + "{\n" + "super();\n" + "}\n" + "public static " + OntologyStrategyIF.class.getName() + " createStrategy()\n" + "{\n return new " + this.getOntologyStrategy().getName() + "();\n" + "}\n" + "public static void configureStrategy(" + OntologyStrategyIF.class.getName() + " strategy)\n" + "{\n " + this.getInitializeStrategySource() + "\n" + "}\n" + "}\n";
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

    // Lets define a relationship A > B > C between these terms.
    BusinessDAO termA = BusinessDAO.newInstance(mdTerm.definesType());
    termA.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "termA");
    termA.apply();
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
    
    initStrat(mdTerm.definesType(), mdTermRelationship.definesType());
  }
  
  public static void initStrat(String mdTermDefinesType, String mdTermRelationshipDefinesType) {
    Term.assignStrategy(mdTermDefinesType);
    Term.getStrategy(mdTermDefinesType).initialize(mdTermRelationshipDefinesType);
  }
  
  public static OntologyStrategyIF getStrategy(String mdTermDefinesType) {
    return Term.getStrategy(mdTermDefinesType);
  }
  
  public static void shutDownStrat(String mdTermDefinesType) {
    Term.getStrategy(mdTermDefinesType).shutdown();
  }
  
  protected static void classTearDown()
  {
    shutDownStrat(mdTerm.definesType());

    TermHolder.getTermA().delete();
    TermHolder.getTermB().delete();
    TermHolder.getTermC().delete();

    MdRelationship.get(mdTermRelationshipId).delete();
    MdBusiness.get(mdTermId).delete();

    didDoSetUp = false;
  }
  
  public void testInitialized() {
    assertTrue(Term.getStrategy(mdTerm.definesType()).isInitialized());
  }

  public void testCopyTerm() throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
  {
    // Do the copy from the strategy
    Class<?> clazz = LoaderDecorator.load(mdTerm.definesType());
    Term termZ = (Term) clazz.newInstance();
    termZ.getDisplayLabel().setValue("termZ");
    termZ.apply();

    TermHolder.getTermA().addLink(termZ, mdTermRelationship.definesType());

    Term ret = (Term) termZ.getChildren(mdTermRelationship.definesType()).next();
    assertNotNull(ret);
    assertNotNull(ret.getChildren(mdTermRelationship.definesType()).next());

    termZ.delete();
  }

  public void testIsLeafNode() throws InstantiationException, IllegalAccessException
  {
    Class<?> clazz = LoaderDecorator.load(mdTerm.definesType());
    Term termL = (Term) clazz.newInstance();
    termL.getDisplayLabel().setValue("termZ");
    termL.apply();

    assertTrue(termL.isLeaf(mdTermRelationship.definesType()));
    assertFalse(TermHolder.getTermA().isLeaf(mdTermRelationship.definesType()));
    assertFalse(TermHolder.getTermB().isLeaf(mdTermRelationship.definesType()));
    assertTrue(TermHolder.getTermC().isLeaf(mdTermRelationship.definesType()));

    termL.delete();
  }

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
    assertEquals(3, descendants.size());
    assertTrue(descendants.get(0) instanceof Term);
    assertTrue(descendants.get(1) instanceof Term);
    assertTrue(descendants.get(2) instanceof Term);
  }

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
    assertEquals(3, ancestors.size());
    assertTrue(ancestors.get(0) instanceof Term);
    assertTrue(ancestors.get(1) instanceof Term);
    assertTrue(ancestors.get(2) instanceof Term);
  }

  public void testGetAllDescendants() throws Exception
  {
    List<Term> descendents = TermHolder.getTermA().getAllDescendants(mdTermRelationship.definesType()).getAll();
    Iterator<Term> it = descendents.iterator();
    assertEquals(it.next(), TermHolder.getTermB());
    assertEquals(it.next(), TermHolder.getTermC());
  }

  public void testGetAllAncestors() throws Exception
  {
    List<Term> ancestors = TermHolder.getTermC().getAllAncestors(mdTermRelationship.definesType()).getAll();

    assertEquals(2, ancestors.size());
    assertTrue(ancestors.contains(TermHolder.getTermB()));
    assertTrue(ancestors.contains(TermHolder.getTermA()));
  }

  public void testRemoveInternalTerm() throws Exception
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
    assertEquals(3, parent3.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
    assertEquals(2, parent4.getAllDescendants(mdTermRelationship.definesType()).getAll().size());

    parent1.removeTerm(mdTermRelationship.definesType());

    assertEquals(1, parent3.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
    assertEquals(0, parent4.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
  }

  public void testRemoveLeafTerm() throws Exception
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
    assertEquals(2, parent3.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
    assertEquals(2, parent4.getAllDescendants(mdTermRelationship.definesType()).getAll().size());

    child.removeTerm(mdTermRelationship.definesType());

    assertEquals(1, parent3.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
    assertEquals(1, parent4.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
  }

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
    assertEquals(3, parent3.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
    assertEquals(2, parent4.getAllDescendants(mdTermRelationship.definesType()).getAll().size());

    parent1.removeLink(parent4, mdTermRelationship.definesType());

    assertEquals(3, parent3.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
    assertEquals(0, parent4.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
  }

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
    assertEquals(2, parent3.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
    assertEquals(2, parent4.getAllDescendants(mdTermRelationship.definesType()).getAll().size());

    child.removeLink(parent1, mdTermRelationship.definesType());

    assertEquals(1, parent3.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
    assertEquals(2, parent4.getAllDescendants(mdTermRelationship.definesType()).getAll().size());
  }
  
  public void testCopyTermsAndGetAllDescendants() throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
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
    assertEquals(it.next().getId(), bb.getId());
    assertEquals(it.next().getId(), cc.getId());
  }
}
