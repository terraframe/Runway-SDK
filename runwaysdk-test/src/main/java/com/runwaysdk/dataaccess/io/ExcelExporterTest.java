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
package com.runwaysdk.dataaccess.io;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdFieldDAOIF;
import com.runwaysdk.dataaccess.MdWebAttributeDAOIF;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.io.excel.DefaultExcelAttributeFilter;
import com.runwaysdk.dataaccess.io.excel.ExcelColumn;
import com.runwaysdk.dataaccess.io.excel.ExcelUtil;
import com.runwaysdk.dataaccess.io.excel.MdWebAttributeFilter;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFormDAO;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.Session;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

public class ExcelExporterTest extends TestCase
{
  private final class MockExcelExportListener implements ExcelExportListener
  {
    private static final String ATTRIBUTE_NAME = "testExtraColumn";

    private static final String DISPLAY_LABEL  = "Test Extra Column";

    @Override
    public void preHeader(ExcelColumn columnInfo)
    {
      // Do nothing
    }

    @Override
    public void addColumns(List<ExcelColumn> extraColumns)
    {
      extraColumns.add(new ExcelColumn(ATTRIBUTE_NAME, DISPLAY_LABEL));
    }
  }

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

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ExcelExporterTest.class);

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

  private static MdBusinessDAO mdBusiness;

  private static MdBusinessDAO mdBusiness2;

  private static MdBusinessDAO mdBusiness3;

  private static MdWebFormDAO  mdForm;

  /**
   * The setup done before the test suite is run
   */
  @Request
  public static void classSetUp()
  {
    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    MdAttributeCharacterDAO mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdBusiness);
    mdAttributeCharacter.apply();

    MdAttributeDoubleDAO mdAttributeDouble = TestFixtureFactory.addDoubleAttribute(mdBusiness);
    mdAttributeDouble.apply();

    MdAttributeIntegerDAO mdAttributeInteger = TestFixtureFactory.addIntegerAttribute(mdBusiness);
    mdAttributeInteger.apply();

    mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.apply();

    TestFixtureFactory.addBooleanAttribute(mdBusiness2).apply();

    mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter).apply();
    TestFixtureFactory.addDoubleField(mdForm, mdAttributeDouble).apply();
    TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger).apply();

    mdBusiness3 = TestFixtureFactory.createMdBusiness("TestBusiness3");
    mdBusiness3.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My*long/\\Great?Sheet[]NameYEAHHHHHHHHHHHHHHHHHHHHHH");
    mdBusiness3.apply();
  }

  /**
   * The tear down done after all the test in the test suite have run
   */
  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdForm);
    TestFixtureFactory.delete(mdBusiness);
    TestFixtureFactory.delete(mdBusiness2);
  }

  public void testExport() throws IOException, InvalidFormatException
  {
    ExcelExporter exporter = new ExcelExporter();
    exporter.addTemplate(mdBusiness.definesType());
    byte[] bytes = exporter.write();

    Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(bytes));

    assertEquals(1, workbook.getNumberOfSheets());

    Sheet sheet = workbook.getSheetAt(0);
    Row typeRow = sheet.getRow(0);
    Row attributeRow = sheet.getRow(1);
    Row labelRow = sheet.getRow(2);

    assertEquals(mdBusiness.definesType(), typeRow.getCell(0).getRichStringCellValue().toString());

    List<? extends MdAttributeDAOIF> attributes = ExcelUtil.getAttributes(mdBusiness, new DefaultExcelAttributeFilter());

    for (int i = 0; i < attributes.size(); i++)
    {
      MdAttributeDAOIF mdAttribute = attributes.get(i);

      String attributeName = attributeRow.getCell(i).getRichStringCellValue().toString();
      String label = labelRow.getCell(i).getRichStringCellValue().toString();

      assertEquals(mdAttribute.definesAttribute(), attributeName);
      assertEquals(mdAttribute.getDisplayLabel(Session.getCurrentLocale()), label);
    }

    // Ensure there aren't any extra columns
    assertNull(attributeRow.getCell(attributes.size()));
    assertNull(labelRow.getCell(attributes.size()));
  }

  public void testFormExport() throws IOException, InvalidFormatException
  {
    ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
    exporter.addTemplate(mdForm.definesType());
    byte[] bytes = exporter.write();

    Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(bytes));

    assertEquals(1, workbook.getNumberOfSheets());

    Sheet sheet = workbook.getSheetAt(0);
    Row typeRow = sheet.getRow(0);
    Row attributeRow = sheet.getRow(1);
    Row labelRow = sheet.getRow(2);

    assertEquals(mdBusiness.definesType(), typeRow.getCell(0).getRichStringCellValue().toString());

    List<? extends MdFieldDAOIF> fields = mdForm.getSortedFields();

    for (int i = 0; i < fields.size(); i++)
    {
      MdFieldDAOIF mdField = fields.get(i);
      MdAttributeDAOIF mdAttribute = ( (MdWebAttributeDAOIF) mdField ).getDefiningMdAttribute();

      String attributeName = attributeRow.getCell(i).getRichStringCellValue().toString();
      String label = labelRow.getCell(i).getRichStringCellValue().toString();

      assertEquals(mdAttribute.definesAttribute(), attributeName);
      assertEquals(mdField.getDisplayLabel(Session.getCurrentLocale()), label);
    }

    // Ensure there aren't any extra columns
    assertNull(attributeRow.getCell(fields.size()));
    assertNull(labelRow.getCell(fields.size()));
  }

  public void testExtraColumns() throws IOException, InvalidFormatException
  {
    ExcelExporter exporter = new ExcelExporter();
    exporter.addListener(new MockExcelExportListener());
    exporter.addTemplate(mdBusiness.definesType());
    byte[] bytes = exporter.write();

    Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(bytes));

    assertEquals(1, workbook.getNumberOfSheets());

    Sheet sheet = workbook.getSheetAt(0);
    Row typeRow = sheet.getRow(0);
    Row attributeRow = sheet.getRow(1);
    Row labelRow = sheet.getRow(2);

    assertEquals(mdBusiness.definesType(), typeRow.getCell(0).getRichStringCellValue().toString());

    int cellNum = ( (List<? extends MdAttributeDAOIF>) ExcelUtil.getAttributes(mdBusiness, new DefaultExcelAttributeFilter()) ).size();

    String attributeName = attributeRow.getCell(cellNum).getRichStringCellValue().toString();
    String label = labelRow.getCell(cellNum).getRichStringCellValue().toString();

    assertEquals(MockExcelExportListener.ATTRIBUTE_NAME, attributeName);
    assertEquals(MockExcelExportListener.DISPLAY_LABEL, label);
  }

  public void testAddRow() throws IOException, InvalidFormatException
  {
    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
    business.setValue("testDouble", "10");
    business.setValue("testInteger", "-1");

    ExcelExporter exporter = new ExcelExporter();

    ExcelExportSheet excelSheet = exporter.addTemplate(mdBusiness.definesType());
    excelSheet.addRow(business);

    byte[] bytes = exporter.write();

    FileUtils.writeByteArrayToFile(new File("test.xlsx"), bytes);

    Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(bytes));

    assertEquals(1, workbook.getNumberOfSheets());

    Sheet sheet = workbook.getSheetAt(0);
    Row row = sheet.getRow(3);

    List<? extends MdAttributeDAOIF> attributes = ExcelUtil.getAttributes(mdBusiness, new DefaultExcelAttributeFilter());

    for (int i = 0; i < attributes.size(); i++)
    {
      MdAttributeDAOIF mdAttribute = attributes.get(i);
      String value = ExcelUtil.getString(row.getCell(i));

      assertEquals(business.getValue(mdAttribute.definesAttribute()), value);
    }
  }

  public void testOverwriteValue() throws IOException, InvalidFormatException
  {
    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
    business.setValue("testDouble", "10");
    business.setValue("testInteger", "-1");

    Map<String, String> values = new HashMap<>();
    values.put("testDouble", "20");

    ExcelExporter exporter = new ExcelExporter();

    ExcelExportSheet excelSheet = exporter.addTemplate(mdBusiness.definesType());
    excelSheet.addRow(business, values);

    byte[] bytes = exporter.write();

    FileUtils.writeByteArrayToFile(new File("test.xlsx"), bytes);

    Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(bytes));

    assertEquals(1, workbook.getNumberOfSheets());

    Sheet sheet = workbook.getSheetAt(0);
    Row row = sheet.getRow(3);

    List<? extends MdAttributeDAOIF> attributes = ExcelUtil.getAttributes(mdBusiness, new DefaultExcelAttributeFilter());

    boolean test = false;

    for (int i = 0; i < attributes.size(); i++)
    {
      MdAttributeDAOIF mdAttribute = attributes.get(i);

      if (mdAttribute.definesAttribute().equals("testDouble"))
      {
        String value = ExcelUtil.getString(row.getCell(i));

        assertEquals("20", value);

        test = true;
      }
    }

    assertTrue("Test did not execute", test);
  }

  public void testMultipleSheets() throws IOException, InvalidFormatException
  {
    ExcelExporter exporter = new ExcelExporter();
    exporter.addTemplate(mdBusiness.definesType());
    exporter.addTemplate(mdBusiness2.definesType());
    byte[] bytes = exporter.write();

    Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(bytes));

    assertEquals(2, workbook.getNumberOfSheets());

    Sheet sheet = workbook.getSheetAt(0);
    Row typeRow = sheet.getRow(0);
    Row attributeRow = sheet.getRow(1);
    Row labelRow = sheet.getRow(2);

    assertEquals(mdBusiness.definesType(), typeRow.getCell(0).getRichStringCellValue().toString());

    List<? extends MdAttributeDAOIF> attributes = ExcelUtil.getAttributes(mdBusiness, new DefaultExcelAttributeFilter());

    for (int i = 0; i < attributes.size(); i++)
    {
      MdAttributeDAOIF mdAttribute = attributes.get(i);

      String attributeName = attributeRow.getCell(i).getRichStringCellValue().toString();
      String label = labelRow.getCell(i).getRichStringCellValue().toString();

      assertEquals(mdAttribute.definesAttribute(), attributeName);
      assertEquals(mdAttribute.getDisplayLabel(Session.getCurrentLocale()), label);
    }

    // Ensure there aren't any extra columns
    assertNull(attributeRow.getCell(attributes.size()));
    assertNull(labelRow.getCell(attributes.size()));

    sheet = workbook.getSheetAt(1);
    typeRow = sheet.getRow(0);
    attributeRow = sheet.getRow(1);
    labelRow = sheet.getRow(2);

    assertEquals(mdBusiness2.definesType(), typeRow.getCell(0).getRichStringCellValue().toString());

    attributes = ExcelUtil.getAttributes(mdBusiness2, new DefaultExcelAttributeFilter());

    for (int i = 0; i < attributes.size(); i++)
    {
      MdAttributeDAOIF mdAttribute = attributes.get(i);

      String attributeName = attributeRow.getCell(i).getRichStringCellValue().toString();
      String label = labelRow.getCell(i).getRichStringCellValue().toString();

      assertEquals(mdAttribute.definesAttribute(), attributeName);
      assertEquals(mdAttribute.getDisplayLabel(Session.getCurrentLocale()), label);
    }

    // Ensure there aren't any extra columns
    assertNull(attributeRow.getCell(attributes.size()));
    assertNull(labelRow.getCell(attributes.size()));
  }

  public void testMultipleSheetsWithDefaultListeners() throws IOException, InvalidFormatException
  {
    ExcelExporter exporter = new ExcelExporter();
    exporter.addListener(new MockExcelExportListener());
    exporter.addTemplate(mdBusiness.definesType());
    exporter.addTemplate(mdBusiness2.definesType());
    byte[] bytes = exporter.write();

    Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(bytes));

    assertEquals(2, workbook.getNumberOfSheets());

    Sheet sheet = workbook.getSheetAt(0);
    Row typeRow = sheet.getRow(0);
    Row attributeRow = sheet.getRow(1);
    Row labelRow = sheet.getRow(2);

    assertEquals(mdBusiness.definesType(), typeRow.getCell(0).getRichStringCellValue().toString());

    int cellNum = ( (List<? extends MdAttributeDAOIF>) ExcelUtil.getAttributes(mdBusiness, new DefaultExcelAttributeFilter()) ).size();

    String attributeName = attributeRow.getCell(cellNum).getRichStringCellValue().toString();
    String label = labelRow.getCell(cellNum).getRichStringCellValue().toString();

    assertEquals(MockExcelExportListener.ATTRIBUTE_NAME, attributeName);
    assertEquals(MockExcelExportListener.DISPLAY_LABEL, label);

    sheet = workbook.getSheetAt(1);
    typeRow = sheet.getRow(0);
    attributeRow = sheet.getRow(1);
    labelRow = sheet.getRow(2);

    assertEquals(mdBusiness2.definesType(), typeRow.getCell(0).getRichStringCellValue().toString());

    cellNum = ( (List<? extends MdAttributeDAOIF>) ExcelUtil.getAttributes(mdBusiness2, new DefaultExcelAttributeFilter()) ).size();

    attributeName = attributeRow.getCell(cellNum).getRichStringCellValue().toString();
    label = labelRow.getCell(cellNum).getRichStringCellValue().toString();

    assertEquals(MockExcelExportListener.ATTRIBUTE_NAME, attributeName);
    assertEquals(MockExcelExportListener.DISPLAY_LABEL, label);
  }

  public void testMultipleSheetsWithDifferentListeners() throws IOException, InvalidFormatException
  {
    List<ExcelExportListener> listeners = new LinkedList<ExcelExportListener>();
    listeners.add(new MockExcelExportListener());

    ExcelExporter exporter = new ExcelExporter();
    exporter.addTemplate(mdBusiness.definesType());
    exporter.addTemplate(mdBusiness2.definesType(), listeners);
    byte[] bytes = exporter.write();

    Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(bytes));

    assertEquals(2, workbook.getNumberOfSheets());

    Sheet sheet = workbook.getSheetAt(0);
    Row typeRow = sheet.getRow(0);
    Row attributeRow = sheet.getRow(1);
    Row labelRow = sheet.getRow(2);

    assertEquals(mdBusiness.definesType(), typeRow.getCell(0).getRichStringCellValue().toString());

    List<? extends MdAttributeDAOIF> attributes = ExcelUtil.getAttributes(mdBusiness, new DefaultExcelAttributeFilter());

    for (int i = 0; i < attributes.size(); i++)
    {
      MdAttributeDAOIF mdAttribute = attributes.get(i);

      String attributeName = attributeRow.getCell(i).getRichStringCellValue().toString();
      String label = labelRow.getCell(i).getRichStringCellValue().toString();

      assertEquals(mdAttribute.definesAttribute(), attributeName);
      assertEquals(mdAttribute.getDisplayLabel(Session.getCurrentLocale()), label);
    }

    // Ensure there aren't any extra columns
    assertNull(attributeRow.getCell(attributes.size()));
    assertNull(labelRow.getCell(attributes.size()));

    sheet = workbook.getSheetAt(1);
    typeRow = sheet.getRow(0);
    attributeRow = sheet.getRow(1);
    labelRow = sheet.getRow(2);

    assertEquals(mdBusiness2.definesType(), typeRow.getCell(0).getRichStringCellValue().toString());

    int cellNum = ( (List<? extends MdAttributeDAOIF>) ExcelUtil.getAttributes(mdBusiness2, new DefaultExcelAttributeFilter()) ).size();

    String attributeName = attributeRow.getCell(cellNum).getRichStringCellValue().toString();
    String label = labelRow.getCell(cellNum).getRichStringCellValue().toString();

    assertEquals(MockExcelExportListener.ATTRIBUTE_NAME, attributeName);
    assertEquals(MockExcelExportListener.DISPLAY_LABEL, label);
  }

  public void testInvalidSheetNames() throws IOException, InvalidFormatException
  {
    ExcelExporter exporter = new ExcelExporter();
    exporter.addTemplate(mdBusiness.definesType());
    exporter.addTemplate(mdBusiness2.definesType());
    exporter.addTemplate(mdBusiness3.definesType());
    byte[] bytes = exporter.write();

    Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(bytes));

    assertEquals(3, workbook.getNumberOfSheets());

    Sheet sheet = workbook.getSheetAt(0);
    Row typeRow = sheet.getRow(0);
    Row attributeRow = sheet.getRow(1);
    Row labelRow = sheet.getRow(2);

    assertEquals(mdBusiness.definesType(), typeRow.getCell(0).getRichStringCellValue().toString());

    List<? extends MdAttributeDAOIF> attributes = ExcelUtil.getAttributes(mdBusiness, new DefaultExcelAttributeFilter());

    for (int i = 0; i < attributes.size(); i++)
    {
      MdAttributeDAOIF mdAttribute = attributes.get(i);

      String attributeName = attributeRow.getCell(i).getRichStringCellValue().toString();
      String label = labelRow.getCell(i).getRichStringCellValue().toString();

      assertEquals(mdAttribute.definesAttribute(), attributeName);
      assertEquals(mdAttribute.getDisplayLabel(Session.getCurrentLocale()), label);
    }

    // Ensure there aren't any extra columns
    assertNull(attributeRow.getCell(attributes.size()));
    assertNull(labelRow.getCell(attributes.size()));

    sheet = workbook.getSheetAt(1);
    typeRow = sheet.getRow(0);
    attributeRow = sheet.getRow(1);
    labelRow = sheet.getRow(2);

    assertEquals(mdBusiness2.definesType(), typeRow.getCell(0).getRichStringCellValue().toString());

    attributes = ExcelUtil.getAttributes(mdBusiness2, new DefaultExcelAttributeFilter());

    for (int i = 0; i < attributes.size(); i++)
    {
      MdAttributeDAOIF mdAttribute = attributes.get(i);

      String attributeName = attributeRow.getCell(i).getRichStringCellValue().toString();
      String label = labelRow.getCell(i).getRichStringCellValue().toString();

      assertEquals(mdAttribute.definesAttribute(), attributeName);
      assertEquals(mdAttribute.getDisplayLabel(Session.getCurrentLocale()), label);
    }

    // Ensure there aren't any extra columns
    assertNull(attributeRow.getCell(attributes.size()));
    assertNull(labelRow.getCell(attributes.size()));
  }

  public static void writeFile(byte[] bytes) throws FileNotFoundException, IOException
  {
    FileOutputStream stream = new FileOutputStream(new File(TestConstants.Path.XLSFiles + "/test.xls"));

    try
    {
      stream.write(bytes);
    }
    finally
    {
      stream.flush();
      stream.close();
    }
  }
}
