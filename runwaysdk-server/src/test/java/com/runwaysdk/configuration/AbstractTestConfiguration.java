package com.runwaysdk.configuration;

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

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.runwaysdk.business.email.EmailProperties;
import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.constants.ServerProperties;

abstract public class AbstractTestConfiguration
{
  abstract ConfigurationResolverIF getConfigResolver();
  
  @Before
  public void setUp()
  {
    ConfigurationManager.setConfigResolver(getConfigResolver());
  }

  @After
  public void tearDown()
  {
    CommonsConfigurationResolver.getInMemoryConfigurator().clear();
  }
  
  @Test
  public void testServerProperties()
  {
    assertEquals(true, ServerProperties.compileTimeWeaving());
    assertEquals("1.6", ServerProperties.getJavaComplianceLevel());
    assertEquals("SunJCE", ServerProperties.getSecurityProvider());
    assertEquals(false, ServerProperties.logTransactions());
    assertEquals(".keyStore", ServerProperties.getKeyStoreFile());
    assertEquals("JCEKS", ServerProperties.getKeyStoreType());
    assertEquals("iggy", ServerProperties.getKeyStorePassword());
    assertEquals(180, ServerProperties.getLockTimeout());
    assertEquals(true, ServerProperties.memoryOnlyCache());
    assertEquals(2000, ServerProperties.getGlobalCacheMemorySize());
    assertEquals(true, ServerProperties.getGlobalCacheFileLocation().contains("/cache/globalCache"));
    assertEquals("globalCache", ServerProperties.getGlobalCacheName());
    assertEquals(true, ServerProperties.getGlobalCacheStats());
    assertEquals("transactionCache", ServerProperties.getTransactionCacheName());
    assertEquals(200, ServerProperties.getTransationIdBucketSize());
  }
  
  @Test
  public void testDatabaseProperties()
  {
    assertEquals("/usr/lib/postgresql/9.1/bin/", DatabaseProperties.getDatabaseBinDirectory());
    assertEquals("pg_dump", DatabaseProperties.getDataDumpExecutable());
    assertEquals("pg_restore", DatabaseProperties.getDataImportExecutable());
    assertEquals(true, DatabaseProperties.getConnectionPooling());
    assertEquals("runwaydb", DatabaseProperties.getDatabaseName());
    assertEquals("runwaynamespace", DatabaseProperties.getNamespace());
    assertEquals(10, DatabaseProperties.getInitialConnections());
    assertEquals("jdbc/TestDB", DatabaseProperties.getJNDIDataSource());
    assertEquals(15, DatabaseProperties.getMaxConnections());
    assertEquals("runwaydb", DatabaseProperties.getPassword());
    assertEquals(5432, DatabaseProperties.getPort());
    assertEquals("127.0.0.1", DatabaseProperties.getServerName());
    assertEquals("runwaydb", DatabaseProperties.getUser());
  }
  
  @Test
  public void testEmailProperties()
  {
    assertEquals("your.smtp.host", EmailProperties.getSmtpHost());
    assertEquals("from@your.address.com", EmailProperties.getFromAddress());
    assertEquals("emailUser", EmailProperties.getLoginUser());
    assertEquals("emailPass", EmailProperties.getLoginPass());
    assertEquals(30, EmailProperties.getKeyExpire());
  }
}
