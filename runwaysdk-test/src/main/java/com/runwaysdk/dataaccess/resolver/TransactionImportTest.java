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
package com.runwaysdk.dataaccess.resolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.RunwayVersion;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ImportLogInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterInfo;
import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.attributes.entity.AttributeStruct;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.database.EntityDAOFactory;
import com.runwaysdk.dataaccess.io.SAXParseTest;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.XMLParseException;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeStructDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdLocalStructDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.MdWarningDAO;
import com.runwaysdk.dataaccess.transaction.ActionEnumDAO;
import com.runwaysdk.dataaccess.transaction.ImportLogDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.dataaccess.transaction.TransactionImportManager;
import com.runwaysdk.dataaccess.transaction.TransactionVersionException;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.util.FileIO;
import com.runwaysdk.vault.VaultDAO;
import com.runwaysdk.vault.VaultFileDAO;

public class TransactionImportTest extends TestCase
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
    suite.addTestSuite(TransactionImportTest.class);

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

  public void testDataPropigation()
  {
    File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);
    File second = new File(ZIP_DIR + RESOLVER_TEST_SECOND);

    MdBusinessExportBuilder builder = new MdBusinessExportBuilder();
    ObjectExportBuilder objectBuilder = new ObjectExportBuilder(first, builder)
    {
      protected void setup()
      {
      };

      @Override
      protected BusinessDAO doIt()
      {
        new TransactionImportManager(file.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

        return super.doIt();
      }

    };

    MdBusinessDAO mdBusiness = builder.generate(COWBELL_SITE, first);
    BusinessDAO object = objectBuilder.generate(DOORMAN_SITE, second);

    TestFixtureFactory.setDomain(TRUMPET_SITE);

    try
    {
      new TransactionImportManager(second.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

      String type = mdBusiness.definesType();
      MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(type);
      BusinessDAOIF businessDAOIF = BusinessDAO.get(object.getId());

      // Ensure the data from the first site is propigated
      assertNotNull(mdBusinessIF);

      // Ensure data from the second site is propigated
      assertNotNull(businessDAOIF);
    }
    finally
    {
      TestFixtureFactory.delete(builder.getMdBusiness());
    }
  }

  public void testGreaterImporterVersion()
  {
    RunwayVersion currentVersion = RunwayVersion.getCurrentVersion();
    RunwayVersion version = new RunwayVersion(currentVersion.getMajorVersion(), currentVersion.getMinorVersion() + 1, currentVersion.getFixVersion());

    File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);
    File second = new File(ZIP_DIR + RESOLVER_TEST_SECOND);

    MdBusinessExportBuilder builder = new MdBusinessExportBuilder();
    ObjectExportBuilder objectBuilder = new ObjectExportBuilder(first, builder)
    {
      protected void setup()
      {
      };

      @Override
      protected BusinessDAO doIt()
      {
        new TransactionImportManager(file.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

        return super.doIt();
      }

    };

    builder.generate(COWBELL_SITE, first);
    objectBuilder.generate(DOORMAN_SITE, second);

    TestFixtureFactory.setDomain(TRUMPET_SITE);

    try
    {
      TransactionImportManager manager = new TransactionImportManager(second.getAbsolutePath(), new DefaultConflictResolver());
      manager.setVersion(version);
      manager.importTransactions();
    }
    catch (TransactionVersionException e)
    {
      fail("Unable to import transactions with an earlier import version");
    }
    finally
    {
      TestFixtureFactory.delete(builder.getMdBusiness());
    }
  }

  public void testLesserImporterVersion()
  {
    RunwayVersion currentVersion = RunwayVersion.getCurrentVersion();
    RunwayVersion version = new RunwayVersion(currentVersion.getMajorVersion(), currentVersion.getMinorVersion() - 1, currentVersion.getFixVersion());

    File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);
    File second = new File(ZIP_DIR + RESOLVER_TEST_SECOND);

    MdBusinessExportBuilder builder = new MdBusinessExportBuilder();
    ObjectExportBuilder objectBuilder = new ObjectExportBuilder(first, builder)
    {
      protected void setup()
      {
      };

      @Override
      protected BusinessDAO doIt()
      {
        new TransactionImportManager(file.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

        return super.doIt();
      }

    };

    builder.generate(COWBELL_SITE, first);
    objectBuilder.generate(DOORMAN_SITE, second);

    TestFixtureFactory.setDomain(TRUMPET_SITE);

    try
    {
      TransactionImportManager manager = new TransactionImportManager(second.getAbsolutePath(), new DefaultConflictResolver());
      manager.setVersion(version);
      manager.importTransactions();

      fail("Able to import transaction from a later import version");
    }
    catch (XMLParseException e)
    {
      if (e.getCause() instanceof TransactionVersionException) {
        // This is expected
        TransactionVersionException tve = (TransactionVersionException) e.getCause();
  
        assertEquals(version.toString(), tve.getExpectedVersion());
        assertEquals(RunwayVersion.getCurrentVersion().toString(), tve.getActualVersion());
      }
      else {
        throw e;
      }
    }
    finally
    {
      TestFixtureFactory.delete(builder.getMdBusiness());
    }
  }

  public void testImportDeleteFromSameDomain()
  {
    File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);
    File second = new File(ZIP_DIR + RESOLVER_TEST_SECOND);

    MdBusinessDAO mdBusiness = new CreateTestClassExportBuilder().generate(COWBELL_SITE, first);

    try
    {
      new CreateTestDeleteExportBuilder(first, mdBusiness).generate(DOORMAN_SITE, second);

      new TransactionImportManager(first.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();
      new TransactionImportManager(second.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

      // Ensure that the delete of the MdBusiness was imported
      try
      {
        MdBusinessDAO.getMdBusinessDAO(mdBusiness.definesType());

        fail("Able to get an MdBussines which should have been deleted in an import originating from the same site");
      }
      catch (DataNotFoundException e)
      {
        // This is expected
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdBusiness);
    }
  }

  public void testImportOfMdWarning()
  {
    File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);

    MdWarningDAO mdWarning = new MdWarningExportBuilder().generate(COWBELL_SITE, first);

    TestFixtureFactory.setDomain(TRUMPET_SITE);

    try
    {

      new TransactionImportManager(first.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

      // Ensure that the delete of the MdBusiness was imported
      try
      {
        MdWarningDAO.getMdWarning(mdWarning.definesType());
      }
      catch (DataNotFoundException e)
      {
        fail(e.getLocalizedMessage());
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdWarning);
    }
  }

  public void testImportOfStructAttribute()
  {
    File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);

    ExportBuilder<BusinessDAO> builder = new ExportBuilder<BusinessDAO>()
    {
      private MdBusinessDAO mdBusiness;

      private MdStructDAO   mdStruct;

      @Request
      protected BusinessDAO doIt()
      {
        mdStruct = TestFixtureFactory.createMdStruct1();
        mdStruct.apply();

        MdAttributeCharacterDAO mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdStruct);
        mdAttributeCharacter.apply();

        mdBusiness = TestFixtureFactory.createMdBusiness1();
        mdBusiness.apply();

        MdAttributeStructDAO mdAttributeStruct = TestFixtureFactory.addStructAttribute(mdBusiness, mdStruct);
        mdAttributeStruct.apply();

        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setStructValue(mdAttributeStruct.definesAttribute(), mdAttributeCharacter.definesAttribute(), "test-value");
        business.apply();

        return business;
      }

      @Override
      protected void undoIt()
      {
        TestFixtureFactory.delete(mdBusiness);
        TestFixtureFactory.delete(mdStruct);
      }

    };

    BusinessDAO expected = builder.generate(COWBELL_SITE, first);
    TestFixtureFactory.setDomain(TRUMPET_SITE);

    try
    {

      new TransactionImportManager(first.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

      // Ensure that the delete of the MdBusiness was imported
      try
      {
        BusinessDAO.get(expected.getId());
      }
      catch (Exception e)
      {
        fail(e.getLocalizedMessage());
      }
    }
    finally
    {
      builder.undoIt();
    }
  }

  public void testImportOfStructAttributeInSingleTransaction()
  {
    File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);

    ExportBuilder<BusinessDAO> builder = new ExportBuilder<BusinessDAO>()
    {
      private MdBusinessDAO mdBusiness;

      private MdStructDAO   mdStruct;

      @Request
      protected BusinessDAO doIt()
      {
        return this.doItTransaction();
      }

      @Transaction
      private BusinessDAO doItTransaction()
      {
        mdStruct = TestFixtureFactory.createMdStruct1();
        mdStruct.apply();

        MdAttributeCharacterDAO mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdStruct);
        mdAttributeCharacter.apply();

        mdBusiness = TestFixtureFactory.createMdBusiness1();
        mdBusiness.apply();

        MdAttributeStructDAO mdAttributeStruct = TestFixtureFactory.addStructAttribute(mdBusiness, mdStruct);
        mdAttributeStruct.apply();

        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setStructValue(mdAttributeStruct.definesAttribute(), mdAttributeCharacter.definesAttribute(), "test-value");
        business.apply();
        return business;
      }

      @Override
      protected void undoIt()
      {
        TestFixtureFactory.delete(mdBusiness);
        TestFixtureFactory.delete(mdStruct);
      }

    };

    BusinessDAO expected = builder.generate(COWBELL_SITE, first);
    TestFixtureFactory.setDomain(TRUMPET_SITE);

    try
    {
      new TransactionImportManager(first.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

      // Ensure that the delete of the MdBusiness was imported
      try
      {
        BusinessDAO.get(expected.getId());
      }
      catch (Exception e)
      {
        fail(e.getLocalizedMessage());
      }
    }
    finally
    {
      builder.undoIt();
    }
  }

  public void testImportOfLocalCharacter()
  {
    File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);

    ExportBuilder<BusinessDAO> builder = new ExportBuilder<BusinessDAO>()
    {
      private MdBusinessDAO    mdBusiness;

      private MdLocalStructDAO mdStruct;

      @Request
      protected BusinessDAO doIt()
      {
        mdStruct = TestFixtureFactory.createMdLocalStruct();
        mdStruct.apply();

        TestFixtureFactory.addCharacterAttribute(mdStruct, MdAttributeLocalDAO.DEFAULT_LOCALE).apply();

        mdBusiness = TestFixtureFactory.createMdBusiness1();
        mdBusiness.apply();

        MdAttributeLocalCharacterDAO mdAttributeLocal = TestFixtureFactory.addLocalCharacterAttribute(mdBusiness, mdStruct);
        mdAttributeLocal.apply();

        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setStructValue(mdAttributeLocal.definesAttribute(), MdAttributeLocalCharacterDAO.DEFAULT_LOCALE, "test-value");
        business.apply();

        return business;
      }

      @Override
      protected void undoIt()
      {
        TestFixtureFactory.delete(mdBusiness);
      }

    };

    try
    {
      BusinessDAO expected = builder.generate(COWBELL_SITE, first);
      TestFixtureFactory.setDomain(TRUMPET_SITE);

      new TransactionImportManager(first.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

      // Ensure that the delete of the MdBusiness was imported
      try
      {
        BusinessDAO.get(expected.getId());
      }
      catch (Exception e)
      {
        fail(e.getLocalizedMessage());
      }
    }
    finally
    {
      builder.undoIt();
    }
  }

  public void testImportOfLocalText()
  {
    File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);

    ExportBuilder<BusinessDAO> builder = new ExportBuilder<BusinessDAO>()
    {
      private MdBusinessDAO    mdBusiness;

      private MdLocalStructDAO mdStruct;

      @Request
      protected BusinessDAO doIt()
      {
        mdStruct = TestFixtureFactory.createMdLocalStruct();
        mdStruct.apply();

        TestFixtureFactory.addTextAttribute(mdStruct, MdAttributeLocalDAO.DEFAULT_LOCALE).apply();

        mdBusiness = TestFixtureFactory.createMdBusiness1();
        mdBusiness.apply();

        MdAttributeLocalTextDAO mdAttributeLocal = TestFixtureFactory.addLocalTextAttribute(mdBusiness, mdStruct);
        mdAttributeLocal.apply();

        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setStructValue(mdAttributeLocal.definesAttribute(), MdAttributeLocalCharacterDAO.DEFAULT_LOCALE, "test-value");
        business.apply();

        return business;
      }

      @Override
      protected void undoIt()
      {
        TestFixtureFactory.delete(mdBusiness);
      }

    };

    try
    {
      BusinessDAO expected = builder.generate(COWBELL_SITE, first);
      TestFixtureFactory.setDomain(TRUMPET_SITE);

      new TransactionImportManager(first.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

      // Ensure that the delete of the MdBusiness was imported
      try
      {
        BusinessDAO.get(expected.getId());
      }
      catch (Exception e)
      {
        fail(e.getLocalizedMessage());
      }
    }
    finally
    {
      builder.undoIt();
    }
  }

  public void testImportLog()
  {
    final File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);
    final File second = new File(ZIP_DIR + RESOLVER_TEST_SECOND);
    final File third = new File(ZIP_DIR + RESOLVER_TEST_SECOND);

    MdBusinessExportBuilder builder = new MdBusinessExportBuilder();
    ObjectExportBuilder objectBuilder = new ObjectExportBuilder(first, builder)
    {
      protected void setup()
      {
      };

      @Override
      protected BusinessDAO doIt()
      {
        new TransactionImportManager(file.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

        return super.doIt();
      }
    };

    final MdBusinessDAO mdBusiness = builder.generate(COWBELL_SITE, first);
    objectBuilder.generate(DOORMAN_SITE, second);

    ExportBuilder<Void> propigation = new ExportBuilder<Void>()
    {

      @Override
      protected Void doIt()
      {
        new TransactionImportManager(first.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();
        new TransactionImportManager(second.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

        return null;
      }

      @Override
      protected void undoIt()
      {
        TestFixtureFactory.delete(mdBusiness);
      }
    };

    propigation.generate(DOORWAY_SITE, third);

    TestFixtureFactory.setDomain(TRUMPET_SITE);

    try
    {
      new TransactionImportManager(third.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(ImportLogInfo.CLASS);
      query.WHERE(query.get(ImportLogInfo.SOURCE_SITE).EQ(builder.getSourceSite()));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      try
      {
        assertTrue(iterator.hasNext());

        BusinessDAOIF importLog = iterator.next();

        assertEquals(builder.getExportSequence(), importLog.getValue(ImportLogInfo.LAST_EXPORT_SEQUENCE));
      }
      finally
      {
        iterator.close();
      }

    }
    finally
    {
      TestFixtureFactory.delete(builder.getMdBusiness());
    }
  }

  public void testSkipOfImportApplyFromSameDomain()
  {
    List<String> existing = UserDAO.getEntityIdsDB(UserInfo.CLASS);

    File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);

    List<UserDAO> users = new UserExportBuilder().generate(COWBELL_SITE, first);

    try
    {
      // IMPORTANT: Do not change sites before importing.

      // This import should skip the creation of all users because the import
      // originated from its current site
      new TransactionImportManager(first.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

      List<String> ids = UserDAO.getEntityIdsDB(UserInfo.CLASS);

      // Ensure that none of the users were re-imported
      for (UserDAO user : users)
      {
        assertFalse(ids.contains(user.getId()));
      }
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

  public void testTransactionUpdate()
  {
    TestFixtureFactory.setDomain(COWBELL_SITE);

    final MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    try
    {
      MdAttributeDAO mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdBusiness);
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getId());
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributeCharacter.apply();

      TestFixtureFactory.addBooleanAttribute(mdBusiness).apply();

      final File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);

      ExportBuilder<BusinessDAO> businessBuilder = new ExportBuilder<BusinessDAO>()
      {
        BusinessDAO object;

        @Override
        protected void undoIt()
        {
          TestFixtureFactory.delete(object);
        }

        @Override
        @Request
        protected BusinessDAO doIt()
        {
          object = this.create();

          return object;
        }

        @Transaction
        private BusinessDAO create()
        {
          BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
          business.setValue("testCharacter", "TestValue");
          business.setValue("testBoolean", "false");
          business.apply();

          business.setValue("testCharacter", "UpdateValue");
          business.apply();

          return business;
        }
      };

      final BusinessDAO object = businessBuilder.generate(COWBELL_SITE, first);

      TestFixtureFactory.setDomain(TRUMPET_SITE);

      new TransactionImportManager(first.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

      BusinessDAOIF businessDAOIF = BusinessDAO.get(object.getId());

      assertEquals("UpdateValue", businessDAOIF.getValue("testCharacter"));
    }
    finally
    {
      TestFixtureFactory.delete(mdBusiness);
    }
  }

  public void testExportAndImportOfVaultFiles()
  {
    final File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);

    ExportBuilder<VaultFileDAO> businessBuilder = new ExportBuilder<VaultFileDAO>()
    {
      VaultDAO     vault;

      VaultFileDAO file;

      @Override
      protected void undoIt()
      {
        TestFixtureFactory.delete(file);

        TestFixtureFactory.delete(vault);
      }

      @Override
      @Request
      protected VaultFileDAO doIt()
      {
        try
        {
          vault = TestFixtureFactory.createVault();
          vault.apply();

          this.create();

          return file;
        }
        catch (IOException e)
        {
          throw new RuntimeException(e);
        }
      }

      @Transaction
      private VaultFileDAO create() throws IOException
      {
        file = VaultFileDAO.newInstance();
        file.setFileName("testFile");
        file.setExtension("xml");
        file.setSize(0);
        file.apply();

        FileInputStream iStream = new FileInputStream(new File(SAXParseTest.FILTER_SET));
        try
        {
          file.putFile(iStream);
        }
        finally
        {
          iStream.close();
        }

        return file;
      }
    };

    final VaultFileDAO object = businessBuilder.generate(COWBELL_SITE, first);

    try
    {
      TestFixtureFactory.setDomain(TRUMPET_SITE);

      new TransactionImportManager(first.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

      BusinessDAOIF businessDAOIF = BusinessDAO.get(object.getId());

      assertNotNull(businessDAOIF);
    }
    finally
    {
      businessBuilder.undoIt();
    }
  }

  public void testUpldateLocalCharacterValue()
  {
    final String value = "update-value";
    File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);

    ExportBuilder<BusinessDAO> builder = new ExportBuilder<BusinessDAO>()
    {
      private MdBusinessDAO    mdBusiness;

      private MdLocalStructDAO mdStruct;

      @Request
      protected BusinessDAO doIt()
      {
        BusinessDAO business = create();

        this.update(business);

        return business;
      }

      @Transaction
      private BusinessDAO create()
      {
        mdStruct = TestFixtureFactory.createMdLocalStruct();
        mdStruct.apply();

        MdAttributeCharacterDAO mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdStruct, MdAttributeLocalDAO.DEFAULT_LOCALE);
        mdAttributeCharacter.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
        mdAttributeCharacter.apply();

        mdBusiness = TestFixtureFactory.createMdBusiness1();
        mdBusiness.apply();

        MdAttributeLocalCharacterDAO mdAttributeLocal = TestFixtureFactory.addLocalCharacterAttribute(mdBusiness, mdStruct);
        mdAttributeLocal.apply();

        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setStructValue(mdAttributeLocal.definesAttribute(), MdAttributeLocalCharacterDAO.DEFAULT_LOCALE, "test-value");
        business.apply();

        return business;
      }

      @Transaction
      private void update(BusinessDAO expected)
      {
        BusinessDAO business = BusinessDAO.get(expected.getId()).getBusinessDAO();
        AttributeStruct attributeStruct = (AttributeStruct) business.getAttribute("testLocalCharacter");

        StructDAO struct = attributeStruct.getStructDAO();
        struct.setValue(MdAttributeLocalCharacterDAO.DEFAULT_LOCALE, value);
        struct.apply();

        EntityDAOFactory.logTransactionItem(business, ActionEnumDAO.UPDATE, true);
      }

      @Override
      protected void undoIt()
      {
        TestFixtureFactory.delete(mdBusiness);
      }
    };

    try
    {
      BusinessDAO expected = builder.generate(COWBELL_SITE, first);
      TestFixtureFactory.setDomain(TRUMPET_SITE);

      new TransactionImportManager(first.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

      // Ensure that the delete of the MdBusiness was imported
      try
      {
        BusinessDAOIF test = BusinessDAO.get(expected.getId());

        assertEquals(value, test.getStructValue("testLocalCharacter", MdAttributeLocalCharacterDAO.DEFAULT_LOCALE));
      }
      catch (Exception e)
      {
        fail(e.getLocalizedMessage());
      }
    }
    finally
    {
      builder.undoIt();
    }
  }

  public void testUpdateExemptRoleName()
  {
    final String value = "update-value";
    File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);

    ExportBuilder<Void> builder = new ExportBuilder<Void>()
    {
      @Request
      protected Void doIt()
      {
        this.update();

        return null;
      }

      @Transaction
      private void update()
      {
        RoleDAO role = RoleDAO.findRole("AdminScreenAccess").getBusinessDAO();
        AttributeStruct attributeStruct = (AttributeStruct) role.getAttribute("displayLabel");

        StructDAO struct = attributeStruct.getStructDAO();

        struct.setValue(MdAttributeLocalCharacterInfo.DEFAULT_LOCALE, value);
        struct.apply();

        EntityDAOFactory.logTransactionItem(role, ActionEnumDAO.UPDATE, true);
      }

      @Override
      protected void undoIt()
      {
      }
    };

    try
    {
      builder.generate(COWBELL_SITE, first);
      TestFixtureFactory.setDomain(TRUMPET_SITE);

      new TransactionImportManager(first.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

      // Ensure that the delete of the MdBusiness was imported
      try
      {
        RoleDAOIF test = RoleDAO.findRole("AdminScreenAccess");

        assertEquals(value, test.getStructValue("displayLabel", MdAttributeLocalCharacterInfo.DEFAULT_LOCALE));
      }
      catch (Exception e)
      {
        fail(e.getLocalizedMessage());
      }
    }
    finally
    {
      builder.undoIt();
    }
  }

  public void testUpldateExistingWithLocalCharacterAttributes()
  {
    final String value = "update-value";
    final String propertyDescription = "propertyDescription";
    final String propertyLabel = "propertyLabel";

    File first = new File(ZIP_DIR + RESOLVER_TEST_FIRST);

    ExportBuilder<BusinessDAO> builder = new ExportBuilder<BusinessDAO>()
    {
      private MdBusinessDAO                mdBusiness;

      private MdAttributeCharacterDAO      mdAttributeCharacter;

      private MdAttributeLocalCharacterDAO mdPropertyLabel;

      private MdAttributeLocalCharacterDAO mdPropertyDescription;

      @Request
      protected BusinessDAO doIt()
      {
        BusinessDAO business = create();

        this.addMdAttributes();

        this.updateLocalValue(business, mdPropertyLabel.definesAttribute());

        this.updateLocalValue(business, mdPropertyDescription.definesAttribute());

        return business;
      }

      @Transaction
      private BusinessDAO create()
      {
        mdBusiness = TestFixtureFactory.createMdBusiness1();
        mdBusiness.apply();

        mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdBusiness);
        mdAttributeCharacter.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
        mdAttributeCharacter.apply();

        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(mdAttributeCharacter.definesAttribute(), "test-value");
        business.apply();

        return business;
      }

      @Transaction
      private void addMdAttributes()
      {
        MdLocalStructDAO propertyLabelStruct = TestFixtureFactory.createMdLocalStruct(mdBusiness.getTypeName() + "PropertyLabel");
        propertyLabelStruct.apply();

        TestFixtureFactory.addCharacterAttribute(propertyLabelStruct, MdAttributeLocalCharacterInfo.DEFAULT_LOCALE).apply();

        mdPropertyLabel = TestFixtureFactory.addLocalCharacterAttribute(mdBusiness, propertyLabelStruct, propertyLabel);
        mdPropertyLabel.apply();

        MdLocalStructDAO propertyDescriptionStruct = TestFixtureFactory.createMdLocalStruct(mdBusiness.getTypeName() + "PropertyDescription");
        propertyDescriptionStruct.apply();

        TestFixtureFactory.addCharacterAttribute(propertyDescriptionStruct, MdAttributeLocalCharacterInfo.DEFAULT_LOCALE).apply();

        mdPropertyDescription = TestFixtureFactory.addLocalCharacterAttribute(mdBusiness, propertyDescriptionStruct, propertyDescription);
        mdPropertyDescription.apply();
      }

      @Transaction
      private void updateLocalValue(BusinessDAO expected, String attributeName)
      {
        BusinessDAO business = BusinessDAO.get(expected.getId()).getBusinessDAO();

        AttributeStruct attributeStruct = (AttributeStruct) business.getAttribute(attributeName);

        StructDAO struct = attributeStruct.getStructDAO();
        struct.setValue(MdAttributeLocalCharacterInfo.DEFAULT_LOCALE, value);
        struct.apply();

        ObjectCache.updateCache(business);

        if (ServerProperties.logTransactions() && ( business.getSiteMaster() != null && business.getSiteMaster().length() > 0 ))
        {
          EntityDAOFactory.logTransactionItem(business, ActionEnumDAO.UPDATE, true);
        }
      }

      @Override
      protected void undoIt()
      {
        TestFixtureFactory.delete(mdBusiness);
      }
    };

    try
    {
      BusinessDAO expected = builder.generate(COWBELL_SITE, first);
      TestFixtureFactory.setDomain(TRUMPET_SITE);

      new TransactionImportManager(first.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

      // Ensure that the delete of the MdBusiness was imported
      try
      {
        BusinessDAOIF test = BusinessDAO.get(expected.getId());

        assertEquals(value, test.getStructValue(propertyLabel, MdAttributeLocalCharacterInfo.DEFAULT_LOCALE));
        assertEquals(value, test.getStructValue(propertyDescription, MdAttributeLocalCharacterInfo.DEFAULT_LOCALE));
      }
      catch (Exception e)
      {
        fail(e.getLocalizedMessage());
      }
    }
    finally
    {
      builder.undoIt();
    }
  }

}
