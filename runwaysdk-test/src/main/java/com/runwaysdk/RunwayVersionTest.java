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
package com.runwaysdk;

import junit.framework.TestCase;
import junit.framework.TestResult;

public class RunwayVersionTest extends TestCase
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

  public void testGetCurrentVersion()
  {
    RunwayVersion version = RunwayVersion.getCurrentVersion();

    assertEquals(RunwayVersion.MAJOR_VERSION, version.getMajorVersion());
    assertEquals(RunwayVersion.MINOR_VERSION, version.getMinorVersion());
    assertEquals(RunwayVersion.FIX_VERSION, version.getFixVersion());
  }

  public void testToString()
  {
    RunwayVersion version = RunwayVersion.getCurrentVersion();

    String expected = version.getMajorVersion() + RunwayVersion.VERSION_DELIMETER + version.getMinorVersion() + RunwayVersion.VERSION_DELIMETER + version.getFixVersion();
    String actual = version.toString();

    assertEquals(expected, actual);
  }

  public void testStringConstructor()
  {
    RunwayVersion expected = RunwayVersion.getCurrentVersion();
    RunwayVersion actual = new RunwayVersion(expected.toString());

    assertEquals(expected.getMajorVersion(), actual.getMajorVersion());
    assertEquals(expected.getMinorVersion(), actual.getMinorVersion());
    assertEquals(expected.getFixVersion(), actual.getFixVersion());
    assertEquals(expected.toString(), actual.toString());
  }

  public void testMajorMinorStringConstructor()
  {
    String toString = 2 + RunwayVersion.VERSION_DELIMETER + 3;

    RunwayVersion actual = new RunwayVersion(toString);

    assertEquals(new Integer(2), actual.getMajorVersion());
    assertEquals(new Integer(3), actual.getMinorVersion());
    assertEquals(new Integer(0), actual.getFixVersion());
  }

  public void testMajorStringConstructor()
  {
    String toString = new Integer(2).toString();

    RunwayVersion actual = new RunwayVersion(toString);

    assertEquals(new Integer(2), actual.getMajorVersion());
    assertEquals(new Integer(0), actual.getMinorVersion());
    assertEquals(new Integer(0), actual.getFixVersion());
  }

  public void testIsGreaterPositive()
  {
    RunwayVersion version = new RunwayVersion(5, 5, 5);

    assertTrue(version.isGreater(new RunwayVersion(5, 4, 5)));
    assertTrue(version.isGreater(new RunwayVersion(4, 9, 5)));
    assertTrue(version.isGreater(new RunwayVersion(4, 4, 10)));
  }

  public void testIsGreaterNegative()
  {
    RunwayVersion version = new RunwayVersion(5, 5, 5);

    assertFalse(version.isGreater(new RunwayVersion(5, 6, 4)));
    assertFalse(version.isGreater(new RunwayVersion(10, 5, 5)));
    assertFalse(version.isGreater(new RunwayVersion(5, 6, 10)));
  }

  public void testIsGreaterEqual()
  {
    RunwayVersion version = new RunwayVersion(5, 5, 5);

    assertFalse(version.isGreater(new RunwayVersion(5, 5, 5)));
    assertFalse(version.isGreater(new RunwayVersion(5, 5, 6)));
    assertFalse(version.isGreater(new RunwayVersion(5, 5, 4)));
  }

  public void testEquals()
  {
    RunwayVersion version = new RunwayVersion(2, 3, 4);

    assertTrue(version.equals(new RunwayVersion(2, 3, 4)));
    assertFalse(version.equals(new RunwayVersion(2, 3, 5)));
    assertFalse(version.equals(new RunwayVersion(2, 4, 4)));
    assertFalse(version.equals(new RunwayVersion(3, 3, 4)));
  }
}
