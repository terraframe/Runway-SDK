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
package com.runwaysdk;

import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.session.Request;

public class RunwayVersionTest
{
  @Request
  @Test
  public void testGetCurrentVersion()
  {
    RunwayMetadataVersion version = RunwayMetadataVersion.getCurrentVersion();

    Assert.assertEquals(RunwayMetadataVersion.MAJOR_VERSION, version.getMajorVersion());
    Assert.assertEquals(RunwayMetadataVersion.MINOR_VERSION, version.getMinorVersion());
    Assert.assertEquals(RunwayMetadataVersion.FIX_VERSION, version.getFixVersion());
  }

  @Request
  @Test
  public void testToString()
  {
    RunwayMetadataVersion version = RunwayMetadataVersion.getCurrentVersion();

    String expected = version.getMajorVersion() + RunwayMetadataVersion.VERSION_DELIMETER + version.getMinorVersion() + RunwayMetadataVersion.VERSION_DELIMETER + version.getFixVersion();
    String actual = version.toString();

    Assert.assertEquals(expected, actual);
  }

  @Request
  @Test
  public void testStringConstructor()
  {
    RunwayMetadataVersion expected = RunwayMetadataVersion.getCurrentVersion();
    RunwayMetadataVersion actual = new RunwayMetadataVersion(expected.toString());

    Assert.assertEquals(expected.getMajorVersion(), actual.getMajorVersion());
    Assert.assertEquals(expected.getMinorVersion(), actual.getMinorVersion());
    Assert.assertEquals(expected.getFixVersion(), actual.getFixVersion());
    Assert.assertEquals(expected.toString(), actual.toString());
  }

  @Request
  @Test
  public void testMajorMinorStringConstructor()
  {
    String toString = 2 + RunwayMetadataVersion.VERSION_DELIMETER + 3;

    RunwayMetadataVersion actual = new RunwayMetadataVersion(toString);

    Assert.assertEquals(new Integer(2), actual.getMajorVersion());
    Assert.assertEquals(new Integer(3), actual.getMinorVersion());
    Assert.assertEquals(new Integer(0), actual.getFixVersion());
  }

  @Request
  @Test
  public void testMajorStringConstructor()
  {
    String toString = new Integer(2).toString();

    RunwayMetadataVersion actual = new RunwayMetadataVersion(toString);

    Assert.assertEquals(new Integer(2), actual.getMajorVersion());
    Assert.assertEquals(new Integer(0), actual.getMinorVersion());
    Assert.assertEquals(new Integer(0), actual.getFixVersion());
  }

  @Request
  @Test
  public void testIsGreaterPositive()
  {
    RunwayMetadataVersion version = new RunwayMetadataVersion(5, 5, 5);

    Assert.assertTrue(version.isGreater(new RunwayMetadataVersion(5, 4, 5)));
    Assert.assertTrue(version.isGreater(new RunwayMetadataVersion(4, 9, 5)));
    Assert.assertTrue(version.isGreater(new RunwayMetadataVersion(4, 4, 10)));
  }

  @Request
  @Test
  public void testIsGreaterNegative()
  {
    RunwayMetadataVersion version = new RunwayMetadataVersion(5, 5, 5);

    Assert.assertFalse(version.isGreater(new RunwayMetadataVersion(5, 6, 4)));
    Assert.assertFalse(version.isGreater(new RunwayMetadataVersion(10, 5, 5)));
    Assert.assertFalse(version.isGreater(new RunwayMetadataVersion(5, 6, 10)));
  }

  @Request
  @Test
  public void testIsGreaterEqual()
  {
    RunwayMetadataVersion version = new RunwayMetadataVersion(5, 5, 5);

    Assert.assertFalse(version.isGreater(new RunwayMetadataVersion(5, 5, 5)));
    Assert.assertFalse(version.isGreater(new RunwayMetadataVersion(5, 5, 6)));
    Assert.assertFalse(version.isGreater(new RunwayMetadataVersion(5, 5, 4)));
  }

  @Request
  @Test
  public void testEquals()
  {
    RunwayMetadataVersion version = new RunwayMetadataVersion(2, 3, 4);

    Assert.assertTrue(version.equals(new RunwayMetadataVersion(2, 3, 4)));
    Assert.assertFalse(version.equals(new RunwayMetadataVersion(2, 3, 5)));
    Assert.assertFalse(version.equals(new RunwayMetadataVersion(2, 4, 4)));
    Assert.assertFalse(version.equals(new RunwayMetadataVersion(3, 3, 4)));
  }
}
