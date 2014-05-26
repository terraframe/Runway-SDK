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
/*
 * Created on Jun 22, 2005
 */
package com.runwaysdk.dataaccess;

import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.business.ontology.MdTermRelationshipTest;
import com.runwaysdk.business.ontology.MdTermTest;
import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.dataaccess.cache.CacheTest;
import com.runwaysdk.dataaccess.io.ExcelExporterTest;
import com.runwaysdk.dataaccess.io.ExcelImporterTest;
import com.runwaysdk.dataaccess.io.InstanceImportTest;
import com.runwaysdk.dataaccess.io.SAXParseTest;
import com.runwaysdk.dataaccess.io.VersionTest;
import com.runwaysdk.dataaccess.io.XMLImporter;
import com.runwaysdk.dataaccess.resolver.ResolverTest;
import com.runwaysdk.dataaccess.resolver.TransactionImportTest;
import com.runwaysdk.dataaccess.schemamanager.MergeTest;
import com.runwaysdk.system.metadata.ontology.OntologyStrategyTest;

/**
 * 
 * @author Eric
 * @version $Revision 1.0 $
 * @since
 **/
public class DataAccessTestSuite extends TestSuite
{
  @Override
  public void run(TestResult testResult)
  {
    super.run(testResult);
  }

  public static void main(String args[])
  {
    if (DatabaseProperties.getDatabaseClass().equals("hsqldb"))
    {
      XMLImporter.main(new String[] { TestConstants.Path.schema_xsd, TestConstants.Path.metadata_xml });
    }
    junit.textui.TestRunner.run(DataAccessTestSuite.suite());
  }

  public static Test suite()
  {
    TestSuite testSuite = new TestSuite(DataAccessTestSuite.class.getSimpleName());
    testSuite.setName(DataAccessTestSuite.class.getName());

    // TransientAttributeTest, parent and child
    TestSuite sessionSuite = new TestSuite();

    TestSuite suite = new TestSuite(TransientAttributeTest.class.getSimpleName() + "_PARENT_SESSION");
    suite.addTest(TransientAttributeTest.suite());
    sessionSuite.addTest(new SessionMasterTestSetup(suite, SessionMasterTestSetup.PARENT_SESSION_CLASS));

    suite = new TestSuite(TransientAttributeTest.class.getSimpleName() + "_CHILD_SESSION");
    suite.addTest(TransientAttributeTest.suite());
    sessionSuite.addTest(new SessionMasterTestSetup(suite, SessionMasterTestSetup.CHILD_SESSION_CLASS));

    // Test classes where the cache algorithm for the test and reference classes
    // are cached.
    suite = new TestSuite("Cached Tests");
    suite.addTest(EntityAttributeTest.suite());
    suite.addTest(EnumerationTest.suite());
    TestSuite caching = new TestSuite("Cached Tests");
    caching.addTest(new EntityMasterTestSetup(suite, EntityCacheMaster.CACHE_EVERYTHING.getCacheCode()));

    // Test classes where the cache algorithm for the test and reference classes
    // are not cached.
    suite = new TestSuite("Not Cached Tests");
    suite.addTest(EntityAttributeTest.suite());
    suite.addTest(EntityAttributeMultiReferenceTest.suite());
    suite.addTest(EntityAttributeMultiTermTest.suite());
    suite.addTest(EnumerationTest.suite());
    suite.addTest(RelationshipTest.suite());
    suite.addTest(MdRelationshipTest.suite());
    suite.addTest(MdAttributeTest.suite());
    suite.addTest(RegexTest.suite());
    suite.addTest(EncryptionTest.suite());
    suite.addTest(MdDimensionTest.suite());
    suite.addTest(MdTermTest.suite());
    suite.addTest(OntologyStrategyTest.suite());
    suite.addTest(MdTermRelationshipTest.suite());

    TestSuite noCaching = new TestSuite("Not Cached Tests");
    noCaching.addTest(new EntityMasterTestSetup(suite, EntityCacheMaster.CACHE_NOTHING.getCacheCode()));

//    testSuite.addTest(sessionSuite);
//    testSuite.addTest(caching);
//    testSuite.addTest(noCaching);

//    // Test classes added here are only run once and are not wrapped by
//    // MasterTestSetup
//    testSuite.addTest(DeterministicIDTest.suite());
//    testSuite.addTest(StaleObjectTest.suite());
//    testSuite.addTest(MetaDataTest.suite());
//    testSuite.addTest(MdBusinessTest.suite());
//    testSuite.addTest(CacheTest.suite());
    
//    testSuite.addTest(SAXParseTest.suite());
//    testSuite.addTest(VersionTest.suite());
    testSuite.addTest(MergeTest.suite());
    
//    testSuite.addTest(InstanceImportTest.suite());
//    testSuite.addTest(SiteTest.suite());
//    testSuite.addTest(MdFacadeTest.suite());
//    testSuite.addTest(MdDomainTest.suite());
//    testSuite.addTest(MdTermTest.suite());
//    testSuite.addTest(MdTermRelationshipTest.suite());
//    testSuite.addTest(MdAttributeTermTest.suite());
//    testSuite.addTest(MdAttributeMultiReferenceTest.suite());
//    testSuite.addTest(MdAttributeMultiTermTest.suite());
//    testSuite.addTest(MdDomainTest.suite());
//    testSuite.addTest(MdControllerTest.suite());
//    testSuite.addTest(LocalizationTest.suite());
//    testSuite.addTest(ReservedWordsTest.suite());
//    testSuite.addTest(KeyTest.suite());
    
//    testSuite.addTest(ResolverTest.suite());
//    testSuite.addTest(TransactionImportTest.suite());
//    testSuite.addTest(MdWebFormTest.suite());
//    // testSuite.addTest(MdMobileFormTest.suite());
//    testSuite.addTest(AttributeValidationTest.suite());
//    testSuite.addTest(ExcelExporterTest.suite());
//    testSuite.addTest(ExcelImporterTest.suite());
//    testSuite.addTest(AttributeValidationTest.suite());
//    testSuite.addTest(ClassAndAttributeDimensionBuilderTest.suite());
//    testSuite.addTest(FieldConditionTest.suite());
//    testSuite.addTest(TransientAttributeMultiReferenceTest.suite());
//    testSuite.addTest(TransientAttributeMultiTermTest.suite());

    return testSuite;
  }
}
