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
package com.runwaysdk.util;

import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.session.Request;

public class IDGeneratorTest
{
  @Request
  @Test
  public void testGenerateId()
  {
    String key = "com.test.TestBusiness1.method";

    Assert.assertEquals(IDGenerator.generateId(key), IDGenerator.generateId(key));
    Assert.assertEquals(32, IDGenerator.generateId(key).length());
  }
}
