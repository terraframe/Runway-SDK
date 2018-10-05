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
/*
 * Created on Jun 22, 2005
 */
package com.runwaysdk.dataaccess;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.runwaysdk.dataaccess.cache.CacheTest;
import com.runwaysdk.dataaccess.io.ExcelExporterTest;
import com.runwaysdk.dataaccess.io.ExcelImporterTest;
import com.runwaysdk.dataaccess.io.InstanceImportTest;
import com.runwaysdk.dataaccess.io.SAXParseTest;
import com.runwaysdk.dataaccess.io.VersionTest;
import com.runwaysdk.dataaccess.resolver.TransactionImportTest;
import com.runwaysdk.dataaccess.schemamanager.MergeTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
  ParentSessionTestSuite.class,
  ChildSessionTestSuite.class,
  CacheEverythingTestSuite.class,
  CacheNothingTestSuite.class,
  SAXParseTest.class,
//  LocalizationTest.class,  
  MdTableTestSuite.class,
  DeterministicIDTest.class,
  StaleObjectTest.class,
  MetaDataTest.class,
  MdBusinessTest.class,
  CacheTest.class,
  VersionTest.class,
  MergeTest.class,
  InstanceImportTest.class,
  SiteTest.class,
  MdDomainTest.class,
  MdTermTest.class,
  MdAttributeTermTest.class,
  MdAttributeMultiReferenceTest.class,
  MdAttributeMultiTermTest.class,
  ReservedWordsTest.class,
  KeyTest.class,
  IdPropigationTest.class,
  TransactionImportTest.class,
  MdWebFormTest.class,
  AttributeValidationTest.class,
  ExcelExporterTest.class,
  ExcelImporterTest.class,
  ClassAndAttributeDimensionBuilderTest.class,
  FieldConditionTest.class,
  TransientAttributeMultiReferenceTest.class,
  TransientAttributeMultiTermTest.class,
//   ResolverTest.class,
//   MdMobileFormTest.class,
//  ExcelImporterNoSourceTest.class,
  
})
public class DataAccessTestSuite
{
  // nothing
}
