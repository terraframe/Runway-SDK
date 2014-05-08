/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.resolver;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.CompositeException;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.transaction.ImportLogDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.dataaccess.transaction.TransactionImportInvalidItem;
import com.runwaysdk.dataaccess.transaction.TransactionImportManager;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.Roles;
import com.runwaysdk.util.FileIO;

public class ResolverTest extends TestCase
{
  private class ReferenceMdBusinessExportBuilder extends ExportBuilder<Void>
  {
    private MdBusinessDAO mdBusiness;

    private MdBusinessDAO referenceMdBusiness;

    private BusinessDAO   referenceBusiness;

    @Request
    protected Void doIt()
    {
      referenceMdBusiness = TestFixtureFactory.createMdBusiness2();
      referenceMdBusiness.apply();

      mdBusiness = TestFixtureFactory.createMdBusiness1();
      mdBusiness.apply();

      TestFixtureFactory.addReferenceAttribute(mdBusiness, referenceMdBusiness).apply();

      referenceBusiness = BusinessDAO.newInstance(referenceMdBusiness.definesType());
      referenceBusiness.apply();

      return null;
    }

    @Override
    protected void undoIt()
    {
      TestFixtureFactory.delete(referenceMdBusiness);
      TestFixtureFactory.delete(mdBusiness);
    }

    public BusinessDAO getReferenceBusiness()
    {
      return referenceBusiness;
    }

    public MdBusinessDAO getMdBusiness()
    {
      return mdBusiness;
    }
  }

  private class ReferenceBusinessExportBuilder extends ExportBuilder<Void>
  {
    private MdBusinessDAO mdBusiness;

    private BusinessDAO   referenceBusiness;

    private BusinessDAO   business;

    private File          file;

    public ReferenceBusinessExportBuilder(MdBusinessDAO mdBusiness, BusinessDAO referenceBusiness, File file)
    {
      this.mdBusiness = mdBusiness;
      this.referenceBusiness = referenceBusiness;
      this.file = file;
    }

    @Override
    protected void setup()
    {
      new TransactionImportManager(file.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();
    }

    @Request
    protected Void doIt()
    {
      business = BusinessDAO.newInstance(mdBusiness.definesType());
      business.setValue("testReference", referenceBusiness.getId());
      business.apply();

      return null;
    }

    @Override
    protected void undoIt()
    {
      TestFixtureFactory.delete(business);
    }
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

  /**
   * Site used to create the import transaction zip file. The import sequence
   * number for this site needs to be reset after every test or else the
   * subsequent tests will not run.
   */
  public static final String COWBELL_SITE         = "cowbell.runway.com";

  /**
   * Site used to create the import transaction zip file. The import sequence
   * number for this site needs to be reset after every test or else the
   * subsequent tests will not run.
   */
  public static final String DOORWAY_SITE         = "doorway.runway.com";

  /**
   * Site used to create the import transaction zip file. The import sequence
   * number for this site needs to be reset after every test or else the
   * subsequent tests will not run.
   */
  public static final String DOORMAN_SITE         = "doorman.runway.com";

  public static final String TRUMPET_SITE         = "trumpet.runway.com";

  /**
   * Location of the transaction zip files
   */
  public static final String ZIP_DIR              = TestConstants.Path.transactionExportFiles + "/";

  /**
   * Filename of the transaction zip file.
   */
  public static final String RESOLVER_TEST_FIRST  = "resolver1283440767138.zip";

  /**
   * Filename of the transaction zip file.
   */
  public static final String RESOLVER_TEST_SECOND = "resolver1283441035364.zip";

  /**
   * Filename of the transaction zip file.
   */
  public static final String RESOLVER_TEST_THIRD  = "resolver1283441035365.zip";

  /**
   * Role prefix
   */
  public static final String ROLE_PREFIX          = "number.Role";

  /**
   * Domain of the system before the tests occur.
   */
  private static String      domain;

  /**
   * Log transaction flag of the system before the tests occur.
   */
  private static boolean     log;

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ResolverTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
        classSetUp();
      }

      protected void tearDown()
      {
        classTearDown();
      }
    };

    return wrapper;
  }

  @Request
  public static void classSetUp()
  {
    domain = CommonProperties.getDomain();
    log = ServerProperties.logTransactions();
  }

  @Request
  public static void classTearDown()
  {
    TestFixtureFactory.setDomain(domain);
    ServerProperties.setLogTransactions(log);

    try
    {
      FileIO.deleteDirectory(new File(ZIP_DIR));
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  @Request
  protected void tearDown() throws Exception
  {
    // Reset the stored sequence number for sites cowbell.runway.com and
    // doorway.runway.com
    ImportLogDAO.setLastExportSeqFromSite(COWBELL_SITE, 0L);
    ImportLogDAO.setLastExportSeqFromSite(DOORWAY_SITE, 0L);
    ImportLogDAO.setLastExportSeqFromSite(DOORMAN_SITE, 0L);
    ImportLogDAO.setLastExportSeqFromSite(TRUMPET_SITE, 0L);

    ExportBuilder.resetTransactions();
  }

  public void testResolver()
  {
    File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);
    File second = new File(ZIP_DIR + RESOLVER_TEST_SECOND);

    RoleExportBuilder generator = new RoleExportBuilder(ROLE_PREFIX, false);
    generator.generate(COWBELL_SITE, first);
    generator.generate(DOORMAN_SITE, second);

    try
    {
      TestFixtureFactory.setDomain(DOORWAY_SITE);

      roleConflictResolver(ZIP_DIR + RESOLVER_TEST_FIRST, ZIP_DIR + RESOLVER_TEST_SECOND);
    }
    finally
    {
      try
      {
        FileIO.deleteFile(first);
        FileIO.deleteFile(second);
      }
      catch (IOException e)
      {
        // Cleanup failed but it doesn't matter. The artifact is overwritten
        // next run.
      }
    }
  }

  public void testConflictInSingleTransaction()
  {
    File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);
    File second = new File(ZIP_DIR + RESOLVER_TEST_SECOND);

    RoleExportBuilder generator = new RoleExportBuilder(ROLE_PREFIX, true);
    generator.generate(COWBELL_SITE, first);
    generator.generate(DOORMAN_SITE, second);

    try
    {
      TestFixtureFactory.setDomain(DOORWAY_SITE);

      roleConflictResolver(ZIP_DIR + RESOLVER_TEST_FIRST, ZIP_DIR + RESOLVER_TEST_SECOND);
    }
    finally
    {
      try
      {
        FileIO.deleteFile(first);
        FileIO.deleteFile(second);
      }
      catch (IOException e)
      {
        // Cleanup failed but it doesn't matter. The artifact is overwritten
        // next run.
      }
    }
  }

  public void roleConflictResolver(String first, String second)
  {
    List<String> existing = UserDAO.getEntityIdsDB(Roles.CLASS);

    try
    {
      File setup = new File(first);
      File conflict = new File(second);

      // IMPORTANT: IT IS ASSUMED THAT THE FIRST IMPORT CREATES THREE ROLES:
      // number.Role0, number.Role1, and number.Role2
      new TransactionImportManager(setup.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

      // Test Basic role specific resolver
      new TransactionImportManager(conflict.getAbsolutePath(), new RoleResolver()).importTransactions();

      // Ensure that the first import worked
      assertNotNull(RoleDAO.findRole(ROLE_PREFIX + "0"));
      assertNotNull(RoleDAO.findRole(ROLE_PREFIX + "1"));
      assertNotNull(RoleDAO.findRole(ROLE_PREFIX + "2"));

      // Ensure that the conflicts were resolved
      assertNotNull(RoleDAO.findRole(ROLE_PREFIX + "0_update"));
      assertNotNull(RoleDAO.findRole(ROLE_PREFIX + "1_update"));
      assertNotNull(RoleDAO.findRole(ROLE_PREFIX + "2_update"));
    }
    finally
    {
      // Delete all roles
      List<String> ids = RoleDAO.getEntityIdsDB(Roles.CLASS);

      for (String id : ids)
      {
        if (!existing.contains(id))
        {
          RoleDAO.get(id).getBusinessDAO().delete();
        }
      }
    }
  }

  /**
   * 
   */
  public void testConflictOnDelete()
  {
    List<String> existing = UserDAO.getEntityIdsDB(UserInfo.CLASS);

    File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);
    File second = new File(ZIP_DIR + RESOLVER_TEST_SECOND);
    File third = new File(ZIP_DIR + RESOLVER_TEST_THIRD);

    final List<UserDAO> users = new UserExportBuilder().generate(COWBELL_SITE, first);

    String ownerId = users.get(0).getId();

    final UserDAO reference = new SingleUserExportBuilder(users, first, ownerId).generate(DOORMAN_SITE, second);
    new DeleteExportBuilder(users, first, ownerId).generate(DOORWAY_SITE, third);

    TestFixtureFactory.setDomain(TRUMPET_SITE);

    try
    {
      new TransactionImportManager(first.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();
      new TransactionImportManager(second.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

      // Test basic delete resolver
      new TransactionImportManager(third.getAbsolutePath(), new DeleteConflictResolver(reference)).importTransactions();

      assertFalse(UserDAO.getEntityIdsDB(UserInfo.CLASS).contains(users.get(0).getId()));
      assertFalse(UserDAO.getEntityIdsDB(UserInfo.CLASS).contains(reference.getId()));
    }
    finally
    {
      List<String> ids = UserDAO.getEntityIdsDB(UserInfo.CLASS);

      for (String id : ids)
      {
        if (!existing.contains(id))
        {
          UserDAO user = UserDAO.get(id).getBusinessDAO();
          user.delete();
        }
      }
    }
  }

  public void testConflictWithMultiApply()
  {
    File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);
    File second = new File(ZIP_DIR + RESOLVER_TEST_SECOND);
    File third = new File(ZIP_DIR + RESOLVER_TEST_THIRD);

    MdBusinessDAO[] array = new MdReferenceExportBuilder().generate(COWBELL_SITE, first);

    try
    {
      new ReferenceExportBuilder(first, array[0], array[1]).generate(DOORMAN_SITE, second);
      new ReferenceExportBuilder(first, array[0], array[1]).generate(DOORWAY_SITE, third);
      
      TestFixtureFactory.setDomain(TRUMPET_SITE);

      new TransactionImportManager(first.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();
      
      new TransactionImportManager(second.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();
      
      new TransactionImportManager(third.getAbsolutePath(), new ObjectResolver()).importTransactions();
      
      List<String> ids = EntityDAO.getEntityIdsDB(array[0].definesType());

      // Only two objects of array[0].definesType() should exist
      assertEquals(2, ids.size());
    }
    catch(Throwable e)
    {
      // Heads up: debug
      e.printStackTrace();
    }
    finally
    {
      for (MdBusinessDAO mdBusiness : array)
      {
        TestFixtureFactory.delete(mdBusiness);
        ObjectCache.flushCache();
      }
    }
  }

  public void testConflictOnCreation()
  {
    File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);
    File second = new File(ZIP_DIR + RESOLVER_TEST_SECOND);
    File third = new File(ZIP_DIR + RESOLVER_TEST_THIRD);

    MdBusinessDAO mdBusiness = new CreateTestClassExportBuilder().generate(COWBELL_SITE, first);

    try
    {
      new CreateTestDeleteExportBuilder(first, mdBusiness).generate(DOORMAN_SITE, second);
      new CreateTestObjectExportBuilder(first, mdBusiness).generate(DOORWAY_SITE, third);

      TestFixtureFactory.setDomain(TRUMPET_SITE);

      new TransactionImportManager(first.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();
      new TransactionImportManager(second.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

      try
      {
        new TransactionImportManager(third.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

        fail("Did not throw an exception when importing an item where the defining MdBusiness was previously deleted");
      }
      catch (TransactionImportInvalidItem e)
      {
        // This is expected
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdBusiness);
    }
  }

  public void testConflictOnParent()
  {
    File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);
    File second = new File(ZIP_DIR + RESOLVER_TEST_SECOND);
    File third = new File(ZIP_DIR + RESOLVER_TEST_THIRD);

    final MdRelationshipExportBuilder generator = new MdRelationshipExportBuilder();
    generator.generate(COWBELL_SITE, first);

    BusinessDAO parent = generator.getParent();
    BusinessDAO child = generator.getChild();
    String relationshipType = generator.getMdRelationship().definesType();

    try
    {
      new DeleteObjectExportBuilder(first, parent)
      {
        @Override
        protected void undoIt()
        {
          generator.undoIt();
        }
      }.generate(DOORMAN_SITE, second);

      RelationshipDAO relationship = new RelationshipExportBuilder(first, parent, child, relationshipType)
      {
        @Override
        protected void undoIt()
        {
          generator.undoIt();
        }
      }.generate(DOORWAY_SITE, third);

      TestFixtureFactory.setDomain(TRUMPET_SITE);

      new TransactionImportManager(first.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();
      new TransactionImportManager(second.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();
      new TransactionImportManager(third.getAbsolutePath(), new DefaultConflictResolver()
      {
        @Override
        public void resolve(ImportConflict conflict)
        {
          // Do nothing skip creating the relationship
        }
      }).importTransactions();

      try
      {
        RelationshipDAO.get(relationship.getId());

        fail("Relationship was imported even though it didn't have a parent");
      }
      catch (DataNotFoundException e)
      {
        // This is expected
      }
    }
    finally
    {
      generator.undoIt();
    }
  }

  public void testConflictOnChild()
  {
    File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);
    File second = new File(ZIP_DIR + RESOLVER_TEST_SECOND);
    File third = new File(ZIP_DIR + RESOLVER_TEST_THIRD);

    final MdRelationshipExportBuilder generator = new MdRelationshipExportBuilder();
    generator.generate(COWBELL_SITE, first);

    BusinessDAO parent = generator.getParent();
    BusinessDAO child = generator.getChild();
    String relationshipType = generator.getMdRelationship().definesType();

    try
    {
      new DeleteObjectExportBuilder(first, child)
      {
        @Override
        protected void undoIt()
        {
          generator.undoIt();
        }
      }.generate(DOORMAN_SITE, second);

      RelationshipDAO relationship = new RelationshipExportBuilder(first, parent, child, relationshipType)
      {
        @Override
        protected void undoIt()
        {
          generator.undoIt();
        }
      }.generate(DOORWAY_SITE, third);

      TestFixtureFactory.setDomain(TRUMPET_SITE);

      new TransactionImportManager(first.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();
      new TransactionImportManager(second.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();
      new TransactionImportManager(third.getAbsolutePath(), new DefaultConflictResolver()
      {
        @Override
        public void resolve(ImportConflict conflict)
        {
          // Do nothing skip creating the relationship
        }
      }).importTransactions();

      try
      {
        RelationshipDAO.get(relationship.getId());

        fail("Relationship was imported even though it didn't have a child");
      }
      catch (DataNotFoundException e)
      {
        // This is expected
      }
    }
    finally
    {
      generator.undoIt();
    }
  }

  public void testSiteMasterAfterConflict()
  {
    File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);
    File second = new File(ZIP_DIR + RESOLVER_TEST_SECOND);
    File third = new File(ZIP_DIR + RESOLVER_TEST_THIRD);

    MdBusinessExportBuilder builder = new MdBusinessExportBuilder();
    builder.generate(COWBELL_SITE, first);

    final BusinessDAO business = new ObjectExportBuilder(first, builder).generate(DOORMAN_SITE, second);
    new ObjectExportBuilder(first, builder).generate(DOORWAY_SITE, third);

    final MdAttributeCharacterDAO mdAttribute = builder.getMdAttribute();
    final String attributeName = mdAttribute.definesAttribute();
    final String updateValue = business.getAttribute(attributeName).getValue() + "_Update";

    TestFixtureFactory.setDomain(TRUMPET_SITE);

    try
    {
      new TransactionImportManager(first.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();
      new TransactionImportManager(second.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();
      new TransactionImportManager(third.getAbsolutePath(), new ConflictAdapter()
      {
        @Override
        public void resolve(ImportConflict conflict)
        {
          EntityDAO local = strategy.get(business.getId()).getEntityDAO();
          local.setValue(attributeName, updateValue);

          strategy.apply(local);
          strategy.apply(conflict.getEntityDAO());
        }
      }).importTransactions();

      BusinessDAOIF test = BusinessDAO.get(business.getId());

      assertNotNull(test);
      assertEquals(updateValue, test.getValue(attributeName));
      assertEquals(DOORMAN_SITE, test.getSiteMaster());
      assertEquals(business.getSequence(), test.getSequence());
    }
    finally
    {
      TestFixtureFactory.delete(builder.getMdBusiness());
    }
  }

  public void testSynchronizationOfResolvedFile()
  {
    File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);
    File second = new File(ZIP_DIR + RESOLVER_TEST_SECOND);
    File third = new File(ZIP_DIR + RESOLVER_TEST_THIRD);

    RoleExportBuilder generator = new RoleExportBuilder(ROLE_PREFIX, true);
    generator.generate(COWBELL_SITE, first);
    generator.generate(DOORMAN_SITE, second);

    ResolvedExportBuilder builder = new ResolvedExportBuilder(first, second);
    List<String> ids = builder.generate(DOORWAY_SITE, third);

    TestFixtureFactory.setDomain(TRUMPET_SITE);

    try
    {
      new TransactionImportManager(third.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

      List<String> actualIds = RoleDAO.getEntityIdsDB(Roles.CLASS);

      assertEquals(ids.size(), actualIds.size());

      for (String id : ids)
      {
        assertTrue(actualIds.contains(id));
      }
    }
    finally
    {
      builder.undoIt();
    }

  }

  public void testCardinalityConstraint()
  {
    final File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);
    final File second = new File(ZIP_DIR + RESOLVER_TEST_SECOND);
    final File third = new File(ZIP_DIR + RESOLVER_TEST_THIRD);

    final MdRelationshipExportBuilder generator = new MdRelationshipExportBuilder()
    {
      @Transaction
      protected void createObjectsInTransaction()
      {
        child = BusinessDAO.newInstance(childBusiness.definesType());
        child.apply();
      }
    };
    generator.generate(COWBELL_SITE, first);

    BusinessDAO child = generator.getChild();

    MdRelationshipDAO mdRelationship = generator.getMdRelationship();

    String relationshipType = mdRelationship.definesType();
    MdBusinessDAO parentMdBusiness = generator.getParentBusiness();

    try
    {
      ParentExportBuilder childBuilder = new ParentExportBuilder(parentMdBusiness.definesType(), relationshipType, child)
      {
        protected void setup()
        {
          new TransactionImportManager(first.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();
        }

        @Override
        protected void undoIt()
        {
          super.undoIt();

          generator.undoIt();
        }
      };

      childBuilder.generate(DOORMAN_SITE, second);
      childBuilder.generate(DOORWAY_SITE, third);

      TestFixtureFactory.setDomain(TRUMPET_SITE);

      new TransactionImportManager(first.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();
      new TransactionImportManager(second.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

      try
      {
        new TransactionImportManager(third.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

        fail("Able to import a transaction which should cause a relationship cardinality constraint");
      }
      catch (Exception e)
      {
        // This is expected
      }

    }
    finally
    {
      generator.undoIt();
    }
  }

  public void testConflictOnInvalidReference()
  {
    File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);
    File second = new File(ZIP_DIR + RESOLVER_TEST_SECOND);
    File third = new File(ZIP_DIR + RESOLVER_TEST_THIRD);

    final ReferenceMdBusinessExportBuilder builder = new ReferenceMdBusinessExportBuilder();
    builder.generate(COWBELL_SITE, first);

    new DeleteObjectExportBuilder(first, builder.getReferenceBusiness())
    {
      @Override
      protected void undoIt()
      {
        builder.undoIt();
      }
    }.generate(DOORMAN_SITE, second);

    new ReferenceBusinessExportBuilder(builder.getMdBusiness(), builder.getReferenceBusiness(), first)
    {
      protected void undoIt()
      {
        builder.undoIt();
      };
    }.generate(DOORWAY_SITE, third);

    try
    {
      TestFixtureFactory.setDomain(TRUMPET_SITE);

      new TransactionImportManager(first.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();
      new TransactionImportManager(second.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();
      new TransactionImportManager(third.getAbsolutePath(), new DefaultConflictResolver()
      {
        @Override
        public void resolve(ImportConflict conflict)
        {
          assertTrue(conflict.getException() instanceof CompositeException);

          this.apply(conflict);
        }

        @Request
        private void apply(ImportConflict conflict)
        {
          try
          {
            conflict.getEntityDAO().apply();
          }
          catch (Exception e)
          {
            fail(e.getMessage());
          }
        }
      }).importTransactions();
    }
    finally
    {
      builder.undoIt();
    }
  }

}
