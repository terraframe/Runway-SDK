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
package com.runwaysdk.business;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.runwaysdk.RunwayVersionTest;
import com.runwaysdk.business.rbac.RBACTest;
import com.runwaysdk.format.CustomFormatTest;
import com.runwaysdk.format.DefaultFormatTest;
import com.runwaysdk.format.LocalizedFormatTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
  ExpressionAttributeTest.class,
  RBACTest.class,
  EntityGenTest.class,
  EntityMultiReferenceGenTest.class,
  EntityMultiTermGenTest.class,
  EntityAttributeIndicatorGenTest.class,
  TransientMultiReferenceGenTest.class,
  TransientMultiTermGenTest.class,
  UtilGenTest.class,
  ViewGenTest.class,
  RelationshipGenTest.class,
  BusinessLocking.class,
  VirtualAttributeGenTest.class,
  DefaultFormatTest.class,
  LocalizedFormatTest.class,
  CustomFormatTest.class,
  RunwayVersionTest.class,
  NoSourceGenTest.class
})
public class BusinessTestSuite
{
}
