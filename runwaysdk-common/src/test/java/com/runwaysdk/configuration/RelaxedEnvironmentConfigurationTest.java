/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.junit.Assert;
import org.junit.Test;

public class RelaxedEnvironmentConfigurationTest
{
  private static Map<String, String> env()
  {
    Map<String, String> env = new HashMap<String, String>();
    env.put("DATABASE_PASSWORD", "fromEnv");
    env.put("KEYSTORE_PASSWORD", "ksFromEnv");
    env.put("LOCK_TIMEOUT", "42");
    env.put("USER", "osUser");
    env.put("port", "6543");
    env.put("exact.name", "exact");
    return env;
  }

  @Test
  public void testEnvironmentNameMapping()
  {
    Assert.assertEquals("DATABASE_PASSWORD", RelaxedEnvironmentConfiguration.toEnvironmentName("database.password"));
    Assert.assertEquals("KEYSTORE_PASSWORD", RelaxedEnvironmentConfiguration.toEnvironmentName("keyStore.password"));
    Assert.assertEquals("RUNTIME_COMPILER_IMPL", RelaxedEnvironmentConfiguration.toEnvironmentName("runtime.compiler-impl"));
    Assert.assertNull(RelaxedEnvironmentConfiguration.toEnvironmentName("user"));
  }

  @Test
  public void testLookup()
  {
    RelaxedEnvironmentConfiguration config = new RelaxedEnvironmentConfiguration(env());

    Assert.assertEquals("fromEnv", config.getString("database.password"));
    Assert.assertEquals("ksFromEnv", config.getString("keyStore.password"));
    Assert.assertEquals(Integer.valueOf(42), config.getInteger("lock.timeout", null));
    Assert.assertEquals("exact", config.getString("exact.name"));
    Assert.assertEquals("6543", config.getString("port"));

    // Dotless keys never fall back to upper-case OS variables
    Assert.assertFalse(config.containsKey("user"));
    Assert.assertNull(config.getString("user"));
  }

  @Test
  public void testInMemoryPrecedence()
  {
    InMemoryConfigurator mem = new InMemoryConfigurator(env());

    Assert.assertEquals("fromEnv", mem.getString("database.password"));

    mem.setProperty("database.password", "fromCode");
    Assert.assertEquals("fromCode", mem.getString("database.password"));

    // Re-interpolating must not lose environment values
    mem.interpolate();
    Assert.assertEquals("ksFromEnv", mem.getString("keyStore.password"));

    mem.clear();
    Assert.assertEquals("fromEnv", mem.getString("database.password"));
  }

  @Test
  public void testReadOnly()
  {
    RelaxedEnvironmentConfiguration config = new RelaxedEnvironmentConfiguration(env());

    // Clearing hides exactly the requested key, on this instance only
    config.clearProperty("database.password");
    Assert.assertNull(config.getString("database.password"));
    Assert.assertEquals("fromEnv", config.getString("DATABASE_PASSWORD"));
    Assert.assertEquals("fromEnv", new RelaxedEnvironmentConfiguration(env()).getString("database.password"));

    RelaxedEnvironmentConfiguration copy = (RelaxedEnvironmentConfiguration) config.clone();
    Assert.assertNull(copy.getString("database.password"));

    config.clear();
    Assert.assertTrue(config.isEmpty());
    Assert.assertNull(config.getString("keyStore.password"));

    try
    {
      config.addProperty("x.y", "z");
      Assert.fail("Expected UnsupportedOperationException");
    }
    catch (UnsupportedOperationException e)
    {
      // expected
    }
  }

  @Test
  public void testEnvironmentOverridesPropertiesFiles()
  {
    InMemoryConfigurator mem = new InMemoryConfigurator(env());

    PropertiesConfiguration file = new PropertiesConfiguration();
    file.setProperty("database.password", "fromFile");
    file.setProperty("user", "dbUser");
    file.setProperty("lock.timeout", "180");
    file.setProperty("ref", "${database.password}");

    // Same layering the resolvers and CommonsConfigurationReader use
    CompositeConfiguration composite = new CompositeConfiguration();
    composite.addConfiguration(mem.getImpl());
    composite.addConfiguration(file);

    Configuration interpolated = composite.interpolatedConfiguration();

    Assert.assertEquals("fromEnv", interpolated.getString("database.password"));
    Assert.assertEquals("fromEnv", interpolated.getString("ref"));
    Assert.assertEquals(Integer.valueOf(42), interpolated.getInteger("lock.timeout", null));
    Assert.assertEquals("dbUser", interpolated.getString("user"));
  }

  @Test
  public void testReaderOverridesBeatEnvironment()
  {
    InMemoryConfigurator mem = new InMemoryConfigurator(env());

    PropertiesConfiguration file = new PropertiesConfiguration();
    file.setProperty("database.password", "fromFile");

    // Mirrors CommonsConfigurationReader: overrides first, then the resolver chain, then the file
    BaseConfiguration overrides = new BaseConfiguration();
    CompositeConfiguration composite = new CompositeConfiguration();
    composite.addConfiguration(mem.getImpl());
    composite.addConfigurationFirst(overrides);
    composite.addConfiguration(file);

    overrides.setProperty("database.password", "fromReader");

    Assert.assertEquals("fromReader", composite.interpolatedConfiguration().getString("database.password"));
  }
}
