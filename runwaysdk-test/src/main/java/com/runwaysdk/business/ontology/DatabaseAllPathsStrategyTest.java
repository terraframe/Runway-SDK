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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.runwaysdk.ClasspathTestRunner;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategy;

@RunWith(ClasspathTestRunner.class)
public class DatabaseAllPathsStrategyTest extends AbstractOntologyStrategyTest
{
  public DatabaseAllPathsStrategyTest() throws Exception
  {
    super();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.business.ontology.AbstractOntologyStrategyTest#
   * getOntologyStrategy()
   */
  @Override
  public Class<?> getOntologyStrategy()
  {
    return DatabaseAllPathsStrategy.class;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.business.ontology.AbstractOntologyStrategyTest#
   * getInitializeStrategySource()
   */
  @Override
  public String getInitializeStrategySource()
  {
    return "((" + DatabaseAllPathsStrategy.class.getCanonicalName() + ")strategy).configure(CLASS);";
  }

  @Request
  @Test
  public void testDelete()
  {
    AllpathsTestUtil util = new AllpathsTestUtil(mdTerm, mdTermRelationship);

    util.destroyTestData();
    try
    {
      util.createTestData();
      util.validateAllpaths();

      System.out.println("Invoking delete on a Term");

      Term testRoot = Term.get(util.testRoot.getId());

      long pre = System.nanoTime();
      testRoot.delete();
      long post = System.nanoTime();
      long elapsed = ( post - pre ) / 1000000000;
      System.out.println("deleting a term took: " + elapsed + " seconds");

      util.validateAllpaths();
      Term.get(util.f.getId()).delete(); // F exists outside the delRoot
      Term.get(util.spacer.getId()).delete(); // Spacer exists outside the
                                              // delRoot
      util.ensureNoTestDataExists();
    }
    finally
    {
      util.destroyTestData();
    }
  }

  // @Request
  // @Request @Test public void testExportImport() throws Exception
  // {
  // AllpathsTestUtil util = new AllpathsTestUtil(mdTerm, mdTermRelationship);
  //
  // util.destroyTestData();
  //
  // try
  // {
  // util.createTestData();
  //
  // OntologyExcelExporter.exportToFile(new File("OntologyExport2.xls"),
  // Term.getByTermId("AllpathsTest Delete Root"));
  //
  // util.destroyTestData();
  //
  // OntologyExcelImporter.main(new String[]{"OntologyExport2.xls"});
  //
  // util.validateAllpaths();
  // }
  // finally
  // {
  // util.destroyTestData();
  // }
  // }

  @Request
  @Test
  public void testRemoveLink() throws Exception
  {
    AllpathsTestUtil util = new AllpathsTestUtil(mdTerm, mdTermRelationship);

    util.destroyTestData();

    try
    {
      util.createTestData();
      util.validateAllpaths();

      Term delRoot = Term.get(util.testRoot.getId());
      Term b = Term.get(util.b.getId());

      b.removeLink(delRoot, mdTermRelationship.definesType());
      util.validateAllpaths();
    }
    finally
    {
      util.destroyTestData();
    }
  }

  @Request
  @Test
  public void testDeleteRelationship() throws Exception
  {
    AllpathsTestUtil util = new AllpathsTestUtil(mdTerm, mdTermRelationship);

    util.destroyTestData();

    try
    {
      util.createTestData();
      util.validateAllpaths();

      Term a = Term.get(util.a.getId());
      Term b = Term.get(util.b.getId());
      Term delRoot = Term.get(util.testRoot.getId());

      b.removeLink(delRoot, mdTermRelationship.definesType());
      b.addLink(a, mdTermRelationship.definesType());
      util.validateAllpaths();

      b.addLink(delRoot, mdTermRelationship.definesType());
      util.validateAllpaths();

      b.removeLink(a, mdTermRelationship.definesType());
      util.validateAllpaths();
    }
    finally
    {
      util.destroyTestData();
    }
  }
}
