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
package com.runwaysdk.dataaccess.io;

import java.io.IOException;
import java.util.Locale;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdPackage;
import com.runwaysdk.facade.Facade;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.system.metadata.MdAttributeBoolean;
import com.runwaysdk.system.metadata.MdAttributeCharacter;
import com.runwaysdk.system.metadata.MdAttributeDouble;
import com.runwaysdk.system.metadata.MdAttributeInteger;
import com.runwaysdk.system.metadata.MdAttributeStruct;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdStruct;
import com.runwaysdk.transport.conversion.ExcelErrors;
import com.runwaysdk.util.FileIO;

public class ExcelTest extends TestCase
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
   * List of all XML files to test on
   */
  public static final String path = TestConstants.Path.XLSFiles + "/";
  public static final String CORRECT = ExcelTest.path + "correct.xls";
  public static final String REQUIRED = ExcelTest.path + "required.xls";
  public static final String GENERATED = ExcelTest.path + "generated.xls";

  private String sessionId;
  //private ExcelImporter importer;
  private static final String BOOK_TYPE = "test.library.Book";

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ExcelTest.class);

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

  /**
   * The setup done before the test suite is run
   */
  @Request
  public static void classSetUp()
  {
    MdStruct dimensions = new MdStruct();
    dimensions.setPackageName("test.library");
    dimensions.setTypeName("Dimensions");
    dimensions.getDisplayLabel().setDefaultValue("Shipping Dimensions");
    dimensions.apply();

    MdAttributeDouble height = new MdAttributeDouble();
    height.setDefiningMdClass(dimensions);
    height.setAttributeName("height");
    height.getDisplayLabel().setDefaultValue("Height");
    height.setDatabaseLength(6);
    height.setDatabaseDecimal(2);
    height.setRequired(true);
    height.apply();

    MdAttributeDouble width = new MdAttributeDouble();
    width.setDefiningMdClass(dimensions);
    width.setAttributeName("width");
    width.getDisplayLabel().setDefaultValue("Width");
    width.setDatabaseLength(6);
    width.setDatabaseDecimal(2);
    width.setRequired(true);
    width.apply();

    MdAttributeDouble depth = new MdAttributeDouble();
    depth.setDefiningMdClass(dimensions);
    depth.setAttributeName("deep");
    depth.getDisplayLabel().setDefaultValue("Depth");
    depth.setDatabaseLength(6);
    depth.setDatabaseDecimal(2);
    depth.apply();

    MdBusiness book = new MdBusiness();
    book.setPackageName("test.library");
    book.setTypeName("Book");
    book.getDisplayLabel().setDefaultValue("Test Book");
    book.apply();

    MdAttributeCharacter title = new MdAttributeCharacter();
    title.setDefiningMdClass(book);
    title.setAttributeName("title");
    title.getDisplayLabel().setDefaultValue("Book Title");
    title.setDatabaseSize(64);
    title.setRequired(true);
    title.apply();

    MdAttributeInteger pages = new MdAttributeInteger();
    pages.setDefiningMdClass(book);
    pages.setAttributeName("pages");
    pages.getDisplayLabel().setDefaultValue("Page Count");
    pages.apply();

    MdAttributeBoolean inPrint = new MdAttributeBoolean();
    inPrint.setDefiningMdClass(book);
    inPrint.setAttributeName("inPrint");
    inPrint.getDisplayLabel().setDefaultValue("In Print");
    inPrint.apply();

    MdAttributeStruct dimensionsAttribute = new MdAttributeStruct();
    dimensionsAttribute.setMdStruct(dimensions);
    dimensionsAttribute.setDefiningMdClass(book);
    dimensionsAttribute.setAttributeName("dimensions");
    dimensionsAttribute.getDisplayLabel().setDefaultValue("Book Dimensions");
    dimensionsAttribute.setRequired(true);
    dimensionsAttribute.apply();
  }

  /**
   * The tear down done after all the test in the test suite have run
   */
  public static void classTearDown()
  {
    new MdPackage("test.library").delete();
  }

  @Override
  public void setUp()
  {
    sessionId = Facade.login(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[]{CommonProperties.getDefaultLocale()});
    //importer = new ExcelImporter();
  }

  @Override
  public void tearDown()
  {
    Facade.logout(sessionId);

    BusinessQuery query = new QueryFactory().businessQuery(BOOK_TYPE);
    OIterator<Business> iterator = query.getIterator();
    try
    {
      while (iterator.hasNext())
        iterator.next().delete();
    }
    finally
    {
      iterator.close();
    }
  }

  public void testBasicImport() throws Exception
  {
    basicImport(sessionId);
  }

  @Request(RequestType.SESSION)
  private void basicImport(String sessionId) throws IOException
  {
    ExcelErrors errors = read(CORRECT);
    if (errors.size()!=0)
      fail("Expected no problems, but got " + errors.size());

    int size = MdBusinessDAO.getEntityIdsDB(BOOK_TYPE).size();
    if (size!=3)
      fail("Expected to create 3 Books, but found " + size);
  }

  public void testFailRequired() throws Exception
  {
    failRequired(sessionId);
  }

  @Request(RequestType.SESSION)
  private void failRequired(String sessionId) throws IOException
  {
    ExcelErrors errors = read(REQUIRED);
    if (errors.size()!=3)
      fail("Expected 3 problems, but got " + errors.size());
  }

  public void testGenerateTemplate() throws IOException
  {
    ExcelExporter exporter = new ExcelExporter();
    exporter.addTemplate(BOOK_TYPE);
    FileIO.write(GENERATED, exporter.write());
  }

  private ExcelErrors read(String fileName) throws IOException
  {
//    return importer.read(new BufferedInputStream(new FileInputStream(fileName)));
    // This is not even close to right.  I need to extract information from the xls file.
    return null;
  }
}
