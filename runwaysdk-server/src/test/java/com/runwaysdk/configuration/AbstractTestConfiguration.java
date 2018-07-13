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
import org.junit.Assert;
import org.junit.Test;

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
    Assert.assertEquals(true, ServerProperties.compileTimeWeaving());
    Assert.assertEquals("1.6", ServerProperties.getJavaComplianceLevel());
    Assert.assertEquals("SunJCE", ServerProperties.getSecurityProvider());
    Assert.assertEquals(false, ServerProperties.logTransactions());
    Assert.assertEquals(".keyStore", ServerProperties.getKeyStoreFile());
    Assert.assertEquals("JCEKS", ServerProperties.getKeyStoreType());
    Assert.assertEquals("iggy", ServerProperties.getKeyStorePassword());
    Assert.assertEquals(180, ServerProperties.getLockTimeout());
    Assert.assertEquals(true, ServerProperties.memoryOnlyCache());
    Assert.assertEquals(2000, ServerProperties.getGlobalCacheMemorySize());
    Assert.assertEquals(true, ServerProperties.getGlobalCacheFileLocation().contains("/cache/globalCache"));
    Assert.assertEquals("globalCache", ServerProperties.getGlobalCacheName());
    Assert.assertEquals(true, ServerProperties.getGlobalCacheStats());
    Assert.assertEquals("transactionCache", ServerProperties.getTransactionCacheName());
    Assert.assertEquals(200, ServerProperties.getTransationIdBucketSize());
  }

  // @Test
  // public void testDatabaseProperties()
  // {
  // Assert.assertEquals("/usr/lib/postgresql/9.1/bin/",
  // DatabaseProperties.getDatabaseBinDirectory());
  // Assert.assertEquals("pg_dump", DatabaseProperties.getDataDumpExecutable());
  // Assert.assertEquals("pg_restore",
  // DatabaseProperties.getDataImportExecutable());
  // Assert.assertEquals(true, DatabaseProperties.getConnectionPooling());
  // Assert.assertEquals("runwaydb", DatabaseProperties.getDatabaseName());
  // Assert.assertEquals("runwaynamespace", DatabaseProperties.getNamespace());
  // Assert.assertEquals(10, DatabaseProperties.getInitialConnections());
  // Assert.assertEquals("jdbc/TestDB", DatabaseProperties.getJNDIDataSource());
  // Assert.assertEquals(15, DatabaseProperties.getMaxConnections());
  // Assert.assertEquals("runwaydb", DatabaseProperties.getPassword());
  // Assert.assertEquals(5432, DatabaseProperties.getPort());
  // Assert.assertEquals("127.0.0.1", DatabaseProperties.getServerName());
  // Assert.assertEquals("runwaydb", DatabaseProperties.getUser());
  // }
}
