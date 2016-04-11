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
package com.runwaysdk.ontology.io;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.xml.sax.SAXException;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.ontology.AbstractOntologyStrategyTest;
import com.runwaysdk.business.ontology.OntologyStrategyIF;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.constants.AssociationType;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.constants.MdTermRelationshipInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.io.StringStreamSource;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.XMLParseException;
import com.runwaysdk.dataaccess.io.dataDefinition.ImportPluginIF;
import com.runwaysdk.dataaccess.io.dataDefinition.SAXImporter;
import com.runwaysdk.dataaccess.io.dataDefinition.SAXSourceParser;
import com.runwaysdk.dataaccess.io.dataDefinition.VersionExporter;
import com.runwaysdk.dataaccess.io.dataDefinition.VersionHandler;
import com.runwaysdk.dataaccess.io.dataDefinition.VersionHandler.Action;
import com.runwaysdk.dataaccess.io.dataDefinition.VersionPlugin;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.dataaccess.metadata.MdTermRelationshipDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.system.metadata.MdTerm;
import com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategy;

public class XMLTermExporterTest extends TestCase
{
  public static void main(String[] args)
  {
    String s = "Vsl\"Out\"Lrg.2\"";

    System.out.println(s.replace("\"", "&quot;"));
  }

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

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(XMLTermExporterTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
        classSetUp();
        afterTransactionFinishes();
      }

      protected void tearDown()
      {
        classTearDown();
      }
    };

    return wrapper;
  }

  private static String                termAId;

  private static String                termBId;

  private static String                termCId;

  private static String                termBaId;

  private static String                termBbId;

  private static String                termBbaId;

  private static MdTermDAO             mdTerm;

  private static MdTermRelationshipDAO mdTermRelationship;

  public static final String           PACKAGE = "com.runwaysdk.test.ontology.io";

  /**
   * Set the testObject to a new Instance of the TEST type.
   */
  @Transaction
  protected static void classSetUp()
  {
    mdTerm = MdTermDAO.newInstance();
    mdTerm.setValue(MdTermInfo.NAME, "Alphabet");
    mdTerm.setValue(MdTermInfo.PACKAGE, PACKAGE);
    mdTerm.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdTerm.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdTerm.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTerm.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
    String source = "package " + PACKAGE + ";\n" + "public class Alphabet extends AlphabetBase implements com.runwaysdk.generation.loader.Reloadable\n" + "{\n" + "public Alphabet()\n" + "{\n" + "super();\n" + "}\n" + "public static " + OntologyStrategyIF.class.getName() + " createStrategy()\n" + "{\n return new " + DatabaseAllPathsStrategy.class.getName() + "();\n" + "}\n" + "public static void configureStrategy(" + OntologyStrategyIF.class.getName() + " strategy)\n" + "{\n ((" + DatabaseAllPathsStrategy.class.getCanonicalName() + ")strategy).configure(CLASS);\n" + "}\n" + "}\n";
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

    // Lets make it a little more complicated by adding B > Ba and B > Bb and Bb
    // > Bba
    BusinessDAO termBa = BusinessDAO.newInstance(mdTerm.definesType());
    termBa.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "termBa");
    termBa.apply();
    termB.addChild(termBa, mdTermRelationship.definesType()).apply();
    BusinessDAO termBb = BusinessDAO.newInstance(mdTerm.definesType());
    termBb.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "termBb");
    termBb.apply();
    termB.addChild(termBb, mdTermRelationship.definesType()).apply();
    BusinessDAO termBba = BusinessDAO.newInstance(mdTerm.definesType());
    termBba.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "termBba&<>\""); // Special
                                                                                                       // characters
                                                                                                       // test
    termBba.apply();
    termBb.addChild(termBba, mdTermRelationship.definesType()).apply();

    termAId = termA.getId();
    termBId = termB.getId();
    termCId = termC.getId();

    termBaId = termBa.getId();
    termBbId = termBb.getId();
    termBbaId = termBba.getId();
  }

  protected static void afterTransactionFinishes()
  {
    AbstractOntologyStrategyTest.initStrat(mdTerm.definesType(), mdTermRelationship.definesType());
  }

  protected static void deleteTermInstances()
  {
    OntologyStrategyIF strat = AbstractOntologyStrategyTest.getStrategy(mdTerm.definesType());

    strat.removeTerm(Term.get(termAId), mdTermRelationship.definesType());
    Term.get(termAId).delete();
    strat.removeTerm(Term.get(termBId), mdTermRelationship.definesType());
    Term.get(termBId).delete();
    strat.removeTerm(Term.get(termCId), mdTermRelationship.definesType());
    Term.get(termCId).delete();
    strat.removeTerm(Term.get(termBaId), mdTermRelationship.definesType());
    Term.get(termBaId).delete();
    strat.removeTerm(Term.get(termBbId), mdTermRelationship.definesType());
    Term.get(termBbId).delete();
    strat.removeTerm(Term.get(termBbaId), mdTermRelationship.definesType());
    Term.get(termBbaId).delete();
  }

  /**
   * If testObject was applied, it is removed from the database.
   * 
   * @see TestCase#tearDown()
   */
  protected static void classTearDown()
  {
    AbstractOntologyStrategyTest.shutDownStrat(mdTerm.definesType());
    TestFixtureFactory.delete(mdTerm);
    TestFixtureFactory.delete(mdTermRelationship);
  }

  public void testExportImport() throws UnsupportedEncodingException
  {
    ByteArrayOutputStream os = new ByteArrayOutputStream();

    TermExporter exporter = new TermExporter(new VersionExporter(os));
    exporter.exportAll(Term.get(termAId), true);

    String xml = os.toString("UTF-8");

    System.out.println(xml);

    deleteTermInstances();

    try
    {
      ImportPluginIF[] plugins = SAXSourceParser.plugins(new VersionPlugin(Action.DO_IT));

      VersionHandler importer = new VersionHandler(new StringStreamSource(xml), "classpath:com/runwaysdk/resources/xsd/version.xsd", plugins);
      importer.begin();
    }
    catch (SAXException e)
    {
      throw new XMLParseException(e);
    }

    // These operations will throw exceptions if the objects don't exist
    Term termA = Term.get(mdTerm.definesType(), termAId);
    Term termB = Term.get(mdTerm.definesType(), termBId);
    Term termC = Term.get(mdTerm.definesType(), termCId);
    @SuppressWarnings("unused")
    Term termBa = Term.get(mdTerm.definesType(), termBaId);
    Term termBb = Term.get(mdTerm.definesType(), termBbId);

    Term termBba = Term.get(mdTerm.definesType(), termBbaId);
    assertEquals("termBba&<>\"", termBba.getDisplayLabel().getValue());

    List<? extends Business> aChildren = termA.getChildren(mdTermRelationship.definesType()).getAll();
    assertEquals(1, aChildren.size());
    assertEquals(aChildren.get(0).getId(), termB.getId());

    List<? extends Business> bChildren = termB.getChildren(mdTermRelationship.definesType()).getAll();
    assertEquals(3, bChildren.size());

    List<? extends Business> cChildren = termC.getChildren(mdTermRelationship.definesType()).getAll();
    assertEquals(0, cChildren.size());

    List<? extends Business> bbChildren = termBb.getChildren(mdTermRelationship.definesType()).getAll();
    assertEquals(1, bbChildren.size());
  }
}
