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
import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.CharacterConditionInfo;
import com.runwaysdk.constants.DateConditionInfo;
import com.runwaysdk.constants.DoubleConditionInfo;
import com.runwaysdk.constants.LongConditionInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdWebIntegerInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.TransientDAO;
import com.runwaysdk.dataaccess.io.ExcelImporter.ImportContext;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.io.excel.AttributeColumn;
import com.runwaysdk.dataaccess.io.excel.ContextBuilder;
import com.runwaysdk.dataaccess.io.excel.DefaultExcelAttributeFilter;
import com.runwaysdk.dataaccess.io.excel.ExcelUtil;
import com.runwaysdk.dataaccess.io.excel.FormValidationImportListener;
import com.runwaysdk.dataaccess.io.excel.MdWebAttributeFilter;
import com.runwaysdk.dataaccess.metadata.CharacterConditionDAO;
import com.runwaysdk.dataaccess.metadata.DateConditionDAO;
import com.runwaysdk.dataaccess.metadata.DoubleConditionDAO;
import com.runwaysdk.dataaccess.metadata.LongConditionDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeVirtualDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.dataaccess.metadata.MdWebCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdWebDateDAO;
import com.runwaysdk.dataaccess.metadata.MdWebDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFormDAO;
import com.runwaysdk.dataaccess.metadata.MdWebIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdWebLongDAO;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.FieldOperation;

public class ExcelImporterTest
{
  private static MdBusinessDAO           mdBusiness;

  private static MdBusinessDAO           mdBusiness2;

  private static MdAttributeCharacterDAO mdAttributeCharacter;

  private static MdAttributeDoubleDAO    mdAttributeDouble;

  private static MdAttributeIntegerDAO   mdAttributeInteger;

  private static MdAttributeLongDAO      mdAttributeLong;

  /**
   * The setup done before the test suite is run
   */
  @Request
  @BeforeClass
  public static void classSetUp()
  {
    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdBusiness);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, "true");
    mdAttributeCharacter.apply();

    mdAttributeDouble = TestFixtureFactory.addDoubleAttribute(mdBusiness);
    mdAttributeDouble.apply();

    mdAttributeInteger = TestFixtureFactory.addIntegerAttribute(mdBusiness);
    mdAttributeInteger.apply();

    mdAttributeLong = TestFixtureFactory.addLongAttribute(mdBusiness);
    mdAttributeLong.apply();

    mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.apply();

    TestFixtureFactory.addBooleanAttribute(mdBusiness2).apply();
  }

  /**
   * The tear down done after all the test in the test suite have run
   */
  @Request
  @AfterClass
  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdBusiness);
    TestFixtureFactory.delete(mdBusiness2);
  }

  @Request
  @Test
  public void testImport() throws IOException
  {
    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
    business.setValue("testDouble", "10.0000");
    business.setValue("testInteger", "-1");

    ExcelExporter exporter = new ExcelExporter();

    ExcelExportSheet excelSheet = exporter.addTemplate(mdBusiness.definesType());
    excelSheet.addRow(business);

    byte[] bytes = exporter.write();

    ExcelExporterTest.writeFile(bytes);

    ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));
    byte[] results = importer.read();

    Assert.assertEquals(0, results.length);

    List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

    Assert.assertEquals(1, ids.size());

    BusinessDAOIF test = BusinessDAO.get(ids.get(0));

    try
    {
      Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
      Assert.assertEquals(business.getValue("testDouble"), test.getValue("testDouble"));
      Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
    }
    finally
    {
      TestFixtureFactory.delete(test);
    }
  }

  @Request
  @Test
  public void testError() throws IOException, InvalidFormatException
  {
    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    business.setValue("testDouble", "10");
    business.setValue("testInteger", "-1");

    ExcelExporter exporter = new ExcelExporter();

    ExcelExportSheet excelSheet = exporter.addTemplate(mdBusiness.definesType());
    excelSheet.addRow(business);

    byte[] bytes = exporter.write();

    ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));
    byte[] results = importer.read();

    Assert.assertFalse(results.length == 0);

    ExcelExporterTest.writeFile(results);

    Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(results));

    Assert.assertEquals(2, workbook.getNumberOfSheets());

    Sheet importSheet = workbook.getSheetAt(0);

    Row typeRow = importSheet.getRow(0);
    Row attributeRow = importSheet.getRow(1);
    Row labelRow = importSheet.getRow(2);
    Row row = importSheet.getRow(3);

    Assert.assertEquals(mdBusiness.definesType(), typeRow.getCell(0).getRichStringCellValue().toString());

    List<? extends MdAttributeDAOIF> attributes = ExcelUtil.getAttributes(mdBusiness, new DefaultExcelAttributeFilter());

    for (int i = 0; i < attributes.size(); i++)
    {
      MdAttributeDAOIF mdAttribute = attributes.get(i);

      String attributeName = attributeRow.getCell(i).getRichStringCellValue().toString();
      String label = labelRow.getCell(i).getRichStringCellValue().toString();
      String value = ExcelUtil.getString(row.getCell(i));

      Assert.assertEquals(mdAttribute.definesAttribute(), attributeName);
      Assert.assertEquals(mdAttribute.getDisplayLabel(Session.getCurrentLocale()), label);
      Assert.assertEquals(business.getValue(mdAttribute.definesAttribute()), value);
    }

    Sheet errorSheet = workbook.getSheetAt(1);

    Row errorRow = errorSheet.getRow(1);

    Assert.assertEquals(4, ExcelUtil.getInteger(errorRow.getCell(0)).intValue());
    Assert.assertEquals(mdBusiness.getTypeName(), ExcelUtil.getString(errorRow.getCell(1)));
  }

  @Request
  @Test
  public void testSuccessAndError() throws IOException, InvalidFormatException
  {
    BusinessDAO valid = BusinessDAO.newInstance(mdBusiness.definesType());
    valid.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
    valid.setValue("testDouble", "10.0000");
    valid.setValue("testInteger", "-1");

    BusinessDAO invalid = BusinessDAO.newInstance(mdBusiness.definesType());
    invalid.setValue("testDouble", "10");
    invalid.setValue("testInteger", "-1");

    ExcelExporter exporter = new ExcelExporter();

    ExcelExportSheet excelSheet = exporter.addTemplate(mdBusiness.definesType());
    excelSheet.addRow(valid);
    excelSheet.addRow(invalid);

    byte[] bytes = exporter.write();

    ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));
    byte[] results = importer.read();

    Assert.assertFalse(results.length == 0);

    ExcelExporterTest.writeFile(results);

    Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(results));

    Assert.assertEquals(2, workbook.getNumberOfSheets());

    Sheet importSheet = workbook.getSheetAt(0);

    Row typeRow = importSheet.getRow(0);
    Row attributeRow = importSheet.getRow(1);
    Row labelRow = importSheet.getRow(2);
    Row row = importSheet.getRow(3);

    Assert.assertEquals(mdBusiness.definesType(), typeRow.getCell(0).getRichStringCellValue().toString());

    List<? extends MdAttributeDAOIF> attributes = ExcelUtil.getAttributes(mdBusiness, new DefaultExcelAttributeFilter());

    for (int i = 0; i < attributes.size(); i++)
    {
      MdAttributeDAOIF mdAttribute = attributes.get(i);

      String attributeName = attributeRow.getCell(i).getRichStringCellValue().toString();
      String label = labelRow.getCell(i).getRichStringCellValue().toString();
      String value = ExcelUtil.getString(row.getCell(i));

      Assert.assertEquals(mdAttribute.definesAttribute(), attributeName);
      Assert.assertEquals(mdAttribute.getDisplayLabel(Session.getCurrentLocale()), label);
      Assert.assertEquals(invalid.getValue(mdAttribute.definesAttribute()), value);
    }

    Sheet errorSheet = workbook.getSheetAt(1);

    Row errorRow = errorSheet.getRow(1);

    Assert.assertEquals(4, ExcelUtil.getInteger(errorRow.getCell(0)).intValue());
    Assert.assertEquals(mdBusiness.getTypeName(), ExcelUtil.getString(errorRow.getCell(1)));

    List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

    Assert.assertEquals(1, ids.size());

    BusinessDAOIF test = BusinessDAO.get(ids.get(0));

    try
    {
      Assert.assertEquals(valid.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
      Assert.assertEquals(valid.getValue("testDouble"), test.getValue("testDouble"));
      Assert.assertEquals(valid.getValue("testInteger"), test.getValue("testInteger"));
    }
    finally
    {
      TestFixtureFactory.delete(test);
    }
  }

  @Request
  @Test
  public void testContextBuilder()
  {
    final String TRANSFORMED_VALUE = "Transformed Value";

    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
    business.setValue("testDouble", "10.0000");
    business.setValue("testInteger", "-1");

    ExcelExporter exporter = new ExcelExporter();

    ExcelExportSheet excelSheet = exporter.addTemplate(mdBusiness.definesType());
    excelSheet.addRow(business);

    byte[] bytes = exporter.write();

    ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes), new ContextBuilder()
    {
      @Override
      protected void buildAttributeColumn(ImportContext context, MdAttributeDAOIF mdAttribute)
      {
        if (mdAttribute instanceof MdAttributeCharacterDAOIF)
        {
          context.addExpectedColumn(new AttributeColumn(mdAttribute)
          {
            public Object getValue(Cell cell) throws Exception
            {
              return TRANSFORMED_VALUE;
            };
          });
        }
        else
        {
          super.buildAttributeColumn(context, mdAttribute);
        }
      }
    });

    byte[] results = importer.read();

    Assert.assertEquals(0, results.length);

    List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

    Assert.assertEquals(1, ids.size());

    BusinessDAOIF test = BusinessDAO.get(ids.get(0));

    try
    {
      Assert.assertEquals(TRANSFORMED_VALUE, test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
      Assert.assertEquals(business.getValue("testDouble"), test.getValue("testDouble"));
      Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
    }
    finally
    {
      TestFixtureFactory.delete(test);
    }
  }

  @Request
  @Test
  public void testMultipleSheets() throws IOException
  {
    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
    business.setValue("testDouble", "10.0000");
    business.setValue("testInteger", "-1");

    BusinessDAO business2 = BusinessDAO.newInstance(mdBusiness2.definesType());
    business2.setValue(TestFixConst.ATTRIBUTE_BOOLEAN, "true");

    ExcelExporter exporter = new ExcelExporter();

    ExcelExportSheet mdBusinessSheet = exporter.addTemplate(mdBusiness.definesType());
    mdBusinessSheet.addRow(business);

    ExcelExportSheet mdBusinessSheet2 = exporter.addTemplate(mdBusiness2.definesType());
    mdBusinessSheet2.addRow(business2);

    byte[] bytes = exporter.write();

    ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));
    byte[] results = importer.read();

    Assert.assertEquals(0, results.length);

    List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

    Assert.assertEquals(1, ids.size());

    BusinessDAOIF test = BusinessDAO.get(ids.get(0));

    try
    {
      Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
      Assert.assertEquals(business.getValue("testDouble"), test.getValue("testDouble"));
      Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
    }
    finally
    {
      TestFixtureFactory.delete(test);
    }

    ids = BusinessDAO.getEntityIdsFromDB(mdBusiness2);

    Assert.assertEquals(1, ids.size());

    test = BusinessDAO.get(ids.get(0));

    try
    {
      Assert.assertEquals(business2.getValue(TestFixConst.ATTRIBUTE_BOOLEAN), test.getValue(TestFixConst.ATTRIBUTE_BOOLEAN));
    }
    finally
    {
      TestFixtureFactory.delete(test);
    }
  }

  @Request
  @Test
  public void testMultipleSheetsWithErrors() throws IOException, InvalidFormatException
  {
    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    business.setValue("testDouble", "10");
    business.setValue("testInteger", "-1");

    BusinessDAO business2 = BusinessDAO.newInstance(mdBusiness2.definesType());
    business2.setValue(TestFixConst.ATTRIBUTE_BOOLEAN, "true");

    ExcelExporter exporter = new ExcelExporter();

    ExcelExportSheet mdBusinessSheet = exporter.addTemplate(mdBusiness.definesType());
    mdBusinessSheet.addRow(business);

    ExcelExportSheet mdBusinessSheet2 = exporter.addTemplate(mdBusiness2.definesType());
    mdBusinessSheet2.addRow(business2);

    byte[] bytes = exporter.write();

    ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));
    byte[] results = importer.read();

    Assert.assertFalse(results.length == 0);

    ExcelExporterTest.writeFile(results);

    Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(results));

    Assert.assertEquals(3, workbook.getNumberOfSheets());

    Sheet importSheet = workbook.getSheetAt(0);

    Row typeRow = importSheet.getRow(0);
    Row attributeRow = importSheet.getRow(1);
    Row labelRow = importSheet.getRow(2);
    Row row = importSheet.getRow(3);

    Assert.assertEquals(mdBusiness.definesType(), typeRow.getCell(0).getRichStringCellValue().toString());

    List<? extends MdAttributeDAOIF> attributes = ExcelUtil.getAttributes(mdBusiness, new DefaultExcelAttributeFilter());

    for (int i = 0; i < attributes.size(); i++)
    {
      MdAttributeDAOIF mdAttribute = attributes.get(i);

      String attributeName = attributeRow.getCell(i).getRichStringCellValue().toString();
      String label = labelRow.getCell(i).getRichStringCellValue().toString();
      String value = ExcelUtil.getString(row.getCell(i));

      Assert.assertEquals(mdAttribute.definesAttribute(), attributeName);
      Assert.assertEquals(mdAttribute.getDisplayLabel(Session.getCurrentLocale()), label);
      Assert.assertEquals(business.getValue(mdAttribute.definesAttribute()), value);
    }

    Sheet errorSheet = workbook.getSheetAt(2);

    Row errorRow = errorSheet.getRow(1);

    Assert.assertEquals(4, ExcelUtil.getInteger(errorRow.getCell(0)).intValue());
    Assert.assertEquals(mdBusiness.getTypeName(), ExcelUtil.getString(errorRow.getCell(1)));
  }

  @Request
  @Test
  public void testWebFormContextBuilder() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter).apply();
      TestFixtureFactory.addDoubleField(mdForm, mdAttributeDouble).apply();
      TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger).apply();

      BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
      business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
      business.setValue("testDouble", "10.0000");
      business.setValue("testInteger", "-1");

      ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
      ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
      excelSheet.addRow(business);

      byte[] bytes = exporter.write();

      ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));
      byte[] results = importer.read();

      Assert.assertEquals(0, results.length);

      List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

      Assert.assertEquals(1, ids.size());

      BusinessDAOIF test = BusinessDAO.get(ids.get(0));

      try
      {
        Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
        Assert.assertEquals(business.getValue("testDouble"), test.getValue("testDouble"));
        Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
      }
      finally
      {
        TestFixtureFactory.delete(test);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testCharacterEQValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdForm, mdAttributeDouble);
      mdWebDouble.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "EQ");

      CharacterConditionDAO condition = CharacterConditionDAO.newInstance();
      condition.setValue(CharacterConditionInfo.DEFINING_MD_FIELD, mdWebCharacter.getId());
      condition.setValue(CharacterConditionInfo.VALUE, "Test Character Value");
      condition.addItem(CharacterConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      try
      {
        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
        business.setValue("testDouble", "10.0000");
        business.setValue("testInteger", "-1");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        Assert.assertEquals(0, results.length);

        List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

        Assert.assertEquals(1, ids.size());

        BusinessDAOIF test = BusinessDAO.get(ids.get(0));

        try
        {
          Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
          Assert.assertEquals(business.getValue("testDouble"), test.getValue("testDouble"));
          Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
        }
        finally
        {
          TestFixtureFactory.delete(test);
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdWebInteger);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testFailCharacterEQValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdForm, mdAttributeDouble);
      mdWebDouble.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "EQ");

      CharacterConditionDAO condition = CharacterConditionDAO.newInstance();
      condition.setValue(CharacterConditionInfo.DEFINING_MD_FIELD, mdWebCharacter.getId());
      condition.setValue(CharacterConditionInfo.VALUE, "Test Character Value");
      condition.addItem(CharacterConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      try
      {

        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Different Value");
        business.setValue("testDouble", "10.0000");
        business.setValue("testInteger", "-1");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        ExcelExporterTest.writeFile(results);

        Assert.assertTrue(results.length > 0);
      }
      finally
      {
        TestFixtureFactory.delete(mdWebInteger);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testNoValueValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdForm, mdAttributeDouble);
      mdWebDouble.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "EQ");

      CharacterConditionDAO condition = CharacterConditionDAO.newInstance();
      condition.setValue(CharacterConditionInfo.DEFINING_MD_FIELD, mdWebCharacter.getId());
      condition.setValue(CharacterConditionInfo.VALUE, "Test Character Value");
      condition.addItem(CharacterConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      try
      {
        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Fail");
        business.setValue("testDouble", "10.0000");
        business.setValue("testInteger", "");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        Assert.assertEquals(0, results.length);

        List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

        Assert.assertEquals(1, ids.size());

        BusinessDAOIF test = BusinessDAO.get(ids.get(0));

        try
        {
          Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
          Assert.assertEquals(business.getValue("testDouble"), test.getValue("testDouble"));
          Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
        }
        finally
        {
          TestFixtureFactory.delete(test);
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdWebInteger);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testDoubleEQValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdForm, mdAttributeDouble);
      mdWebDouble.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "EQ");

      DoubleConditionDAO condition = DoubleConditionDAO.newInstance();
      condition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getId());
      condition.setValue(DoubleConditionInfo.VALUE, "10.00");
      condition.addItem(DoubleConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      try
      {
        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
        business.setValue("testDouble", "10.0000");
        business.setValue("testInteger", "-1");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        Assert.assertEquals(0, results.length);

        List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

        Assert.assertEquals(1, ids.size());

        BusinessDAOIF test = BusinessDAO.get(ids.get(0));

        try
        {
          Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
          Assert.assertEquals(business.getValue("testDouble"), test.getValue("testDouble"));
          Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
        }
        finally
        {
          TestFixtureFactory.delete(test);
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdWebInteger);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testFailDoubleEQValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdForm, mdAttributeDouble);
      mdWebDouble.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "EQ");

      DoubleConditionDAO condition = DoubleConditionDAO.newInstance();
      condition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getId());
      condition.setValue(DoubleConditionInfo.VALUE, "10.00");
      condition.addItem(DoubleConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      try
      {
        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Different Value");
        business.setValue("testDouble", "11");
        business.setValue("testInteger", "-1");
        business.setValue("testLong", "23");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        ExcelExporterTest.writeFile(results);

        Assert.assertTrue(results.length > 0);
      }
      finally
      {
        TestFixtureFactory.delete(mdWebInteger);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testLongEQValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebLongDAO mdWebLong = TestFixtureFactory.addLongField(mdForm, mdAttributeLong);
      mdWebLong.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "EQ");

      LongConditionDAO condition = LongConditionDAO.newInstance();
      condition.setValue(LongConditionInfo.DEFINING_MD_FIELD, mdWebLong.getId());
      condition.setValue(LongConditionInfo.VALUE, "24");
      condition.addItem(LongConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
      business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
      business.setValue("testLong", "24");
      business.setValue("testInteger", "-1");

      ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
      ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
      excelSheet.addRow(business);

      byte[] bytes = exporter.write();

      ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

      for (ImportContext context : importer.getContexts())
      {
        context.addListener(new FormValidationImportListener(mdForm));
      }

      byte[] results = importer.read();

      Assert.assertEquals(0, results.length);

      List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

      Assert.assertEquals(1, ids.size());

      BusinessDAOIF test = BusinessDAO.get(ids.get(0));

      try
      {
        Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
        Assert.assertEquals(business.getValue("testLong"), test.getValue("testLong"));
        Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
      }
      finally
      {
        TestFixtureFactory.delete(test);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testFailLongEQValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebLongDAO mdWebLong = TestFixtureFactory.addLongField(mdForm, mdAttributeLong);
      mdWebLong.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "EQ");

      LongConditionDAO condition = LongConditionDAO.newInstance();
      condition.setValue(LongConditionInfo.DEFINING_MD_FIELD, mdWebLong.getId());
      condition.setValue(LongConditionInfo.VALUE, "34");
      condition.addItem(LongConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
      business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Different Value");
      business.setValue("testLong", "23");
      business.setValue("testInteger", "-1");

      ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
      ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
      excelSheet.addRow(business);

      byte[] bytes = exporter.write();

      ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

      for (ImportContext context : importer.getContexts())
      {
        context.addListener(new FormValidationImportListener(mdForm));
      }

      byte[] results = importer.read();

      ExcelExporterTest.writeFile(results);

      Assert.assertTrue(results.length > 0);
    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testDateEQValidation() throws IOException
  {
    MdAttributeDateDAO mdAttributeDate = TestFixtureFactory.addDateAttribute(mdBusiness);
    mdAttributeDate.apply();

    try
    {

      MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
      mdForm.apply();

      try
      {
        MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
        mdWebCharacter.apply();

        MdWebDateDAO mdWebDate = TestFixtureFactory.addDateField(mdForm, mdAttributeDate);
        mdWebDate.apply();

        EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "EQ");

        DateConditionDAO condition = DateConditionDAO.newInstance();
        condition.setValue(DateConditionInfo.DEFINING_MD_FIELD, mdWebDate.getId());
        condition.setValue(DateConditionInfo.VALUE, "2001-10-09");
        condition.addItem(DateConditionInfo.OPERATION, item.getId());
        condition.apply();

        MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
        mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
        mdWebInteger.apply();

        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
        business.setValue("testDate", "2001-10-09");
        business.setValue("testInteger", "-1");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        Assert.assertEquals(0, results.length);

        List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

        Assert.assertEquals(1, ids.size());

        BusinessDAOIF test = BusinessDAO.get(ids.get(0));

        try
        {
          Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
          Assert.assertEquals(business.getValue("testDate"), test.getValue("testDate"));
          Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
        }
        finally
        {
          TestFixtureFactory.delete(test);
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdForm);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdAttributeDate);
    }
  }

  @Request
  @Test
  public void testFailDateEQValidation() throws IOException
  {

    MdAttributeDateDAO mdAttributeDate = TestFixtureFactory.addDateAttribute(mdBusiness);
    mdAttributeDate.apply();

    try
    {

      MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
      mdForm.apply();

      try
      {
        MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
        mdWebCharacter.apply();

        MdWebDateDAO mdWebDate = TestFixtureFactory.addDateField(mdForm, mdAttributeDate);
        mdWebDate.apply();

        EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "EQ");

        DateConditionDAO condition = DateConditionDAO.newInstance();
        condition.setValue(DateConditionInfo.DEFINING_MD_FIELD, mdWebDate.getId());
        condition.setValue(DateConditionInfo.VALUE, "2001-10-09");
        condition.addItem(DateConditionInfo.OPERATION, item.getId());
        condition.apply();

        MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
        mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
        mdWebInteger.apply();

        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Different Value");
        business.setValue("testDate", "2001-11-09");
        business.setValue("testInteger", "-1");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        ExcelExporterTest.writeFile(results);

        Assert.assertTrue(results.length > 0);
      }
      finally
      {
        TestFixtureFactory.delete(mdForm);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdAttributeDate);
    }
  }

  @Request
  @Test
  public void testCharacterNEQValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdForm, mdAttributeDouble);
      mdWebDouble.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "NEQ");

      CharacterConditionDAO condition = CharacterConditionDAO.newInstance();
      condition.setValue(CharacterConditionInfo.DEFINING_MD_FIELD, mdWebCharacter.getId());
      condition.setValue(CharacterConditionInfo.VALUE, "Test Character Value");
      condition.addItem(CharacterConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      try
      {
        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Diff Test Character Value");
        business.setValue("testDouble", "10.0000");
        business.setValue("testInteger", "-1");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        Assert.assertEquals(0, results.length);

        List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

        Assert.assertEquals(1, ids.size());

        BusinessDAOIF test = BusinessDAO.get(ids.get(0));

        try
        {
          Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
          Assert.assertEquals(business.getValue("testDouble"), test.getValue("testDouble"));
          Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
        }
        finally
        {
          TestFixtureFactory.delete(test);
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdWebInteger);
      }

    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testFailCharacterNEQValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdForm, mdAttributeDouble);
      mdWebDouble.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "NEQ");

      CharacterConditionDAO condition = CharacterConditionDAO.newInstance();
      condition.setValue(CharacterConditionInfo.DEFINING_MD_FIELD, mdWebCharacter.getId());
      condition.setValue(CharacterConditionInfo.VALUE, "Test Character Value");
      condition.addItem(CharacterConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      try
      {
        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
        business.setValue("testDouble", "10.0000");
        business.setValue("testInteger", "-1");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        ExcelExporterTest.writeFile(results);

        Assert.assertTrue(results.length > 0);
      }
      finally
      {
        TestFixtureFactory.delete(mdWebInteger);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testDoubleNEQValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdForm, mdAttributeDouble);
      mdWebDouble.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "NEQ");

      DoubleConditionDAO condition = DoubleConditionDAO.newInstance();
      condition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getId());
      condition.setValue(DoubleConditionInfo.VALUE, "11.00");
      condition.addItem(DoubleConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      try
      {
        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
        business.setValue("testDouble", "10.0000");
        business.setValue("testInteger", "-1");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        Assert.assertEquals(0, results.length);

        List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

        Assert.assertEquals(1, ids.size());

        BusinessDAOIF test = BusinessDAO.get(ids.get(0));

        try
        {
          Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
          Assert.assertEquals(business.getValue("testDouble"), test.getValue("testDouble"));
          Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
        }
        finally
        {
          TestFixtureFactory.delete(test);
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdWebInteger);
      }

    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testFailDoubleNEQValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdForm, mdAttributeDouble);
      mdWebDouble.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "NEQ");

      DoubleConditionDAO condition = DoubleConditionDAO.newInstance();
      condition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getId());
      condition.setValue(DoubleConditionInfo.VALUE, "11");
      condition.addItem(DoubleConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      try
      {

        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Different Value");
        business.setValue("testDouble", "11");
        business.setValue("testInteger", "-1");
        business.setValue("testLong", "23");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        ExcelExporterTest.writeFile(results);

        Assert.assertTrue(results.length > 0);
      }
      finally
      {
        TestFixtureFactory.delete(mdWebInteger);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testLongNEQValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebLongDAO mdWebLong = TestFixtureFactory.addLongField(mdForm, mdAttributeLong);
      mdWebLong.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "NEQ");

      LongConditionDAO condition = LongConditionDAO.newInstance();
      condition.setValue(LongConditionInfo.DEFINING_MD_FIELD, mdWebLong.getId());
      condition.setValue(LongConditionInfo.VALUE, "22");
      condition.addItem(LongConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      try
      {
        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
        business.setValue("testLong", "24");
        business.setValue("testInteger", "-1");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        Assert.assertEquals(0, results.length);

        List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

        Assert.assertEquals(1, ids.size());

        BusinessDAOIF test = BusinessDAO.get(ids.get(0));

        try
        {
          Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
          Assert.assertEquals(business.getValue("testLong"), test.getValue("testLong"));
          Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
        }
        finally
        {
          TestFixtureFactory.delete(test);
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdWebInteger);
      }

    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testFailLongNEQValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebLongDAO mdWebLong = TestFixtureFactory.addLongField(mdForm, mdAttributeLong);
      mdWebLong.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "NEQ");

      LongConditionDAO condition = LongConditionDAO.newInstance();
      condition.setValue(LongConditionInfo.DEFINING_MD_FIELD, mdWebLong.getId());
      condition.setValue(LongConditionInfo.VALUE, "23");
      condition.addItem(LongConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      try
      {
        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Different Value");
        business.setValue("testLong", "23");
        business.setValue("testInteger", "-1");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        ExcelExporterTest.writeFile(results);

        Assert.assertTrue(results.length > 0);
      }
      finally
      {
        TestFixtureFactory.delete(mdWebInteger);
      }

    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testDateNEQValidation() throws IOException
  {
    MdAttributeDateDAO mdAttributeDate = TestFixtureFactory.addDateAttribute(mdBusiness);
    mdAttributeDate.apply();

    try
    {

      MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
      mdForm.apply();

      try
      {
        MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
        mdWebCharacter.apply();

        MdWebDateDAO mdWebDate = TestFixtureFactory.addDateField(mdForm, mdAttributeDate);
        mdWebDate.apply();
        EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "NEQ");

        DateConditionDAO condition = DateConditionDAO.newInstance();
        condition.setValue(DateConditionInfo.DEFINING_MD_FIELD, mdWebDate.getId());
        condition.setValue(DateConditionInfo.VALUE, "2001-10-09");
        condition.addItem(DateConditionInfo.OPERATION, item.getId());
        condition.apply();

        MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
        mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
        mdWebInteger.apply();

        try
        {
          BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
          business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
          business.setValue("testDate", "2001-11-09");
          business.setValue("testInteger", "-1");

          ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
          ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
          excelSheet.addRow(business);

          byte[] bytes = exporter.write();

          ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

          for (ImportContext context : importer.getContexts())
          {
            context.addListener(new FormValidationImportListener(mdForm));
          }

          byte[] results = importer.read();

          Assert.assertEquals(0, results.length);

          List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

          Assert.assertEquals(1, ids.size());

          BusinessDAOIF test = BusinessDAO.get(ids.get(0));

          try
          {
            Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
            Assert.assertEquals(business.getValue("testDate"), test.getValue("testDate"));
            Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
          }
          finally
          {
            TestFixtureFactory.delete(test);
          }
        }
        finally
        {
          TestFixtureFactory.delete(mdWebInteger);
        }

      }
      finally
      {
        TestFixtureFactory.delete(mdForm);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdAttributeDate);
    }
  }

  @Request
  @Test
  public void testFailDateNEQValidation() throws IOException
  {

    MdAttributeDateDAO mdAttributeDate = TestFixtureFactory.addDateAttribute(mdBusiness);
    mdAttributeDate.apply();

    try
    {

      MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
      mdForm.apply();

      try
      {
        MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
        mdWebCharacter.apply();

        MdWebDateDAO mdWebDate = TestFixtureFactory.addDateField(mdForm, mdAttributeDate);
        mdWebDate.apply();

        EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "NEQ");

        DateConditionDAO condition = DateConditionDAO.newInstance();
        condition.setValue(DateConditionInfo.DEFINING_MD_FIELD, mdWebDate.getId());
        condition.setValue(DateConditionInfo.VALUE, "2001-10-09");
        condition.addItem(DateConditionInfo.OPERATION, item.getId());
        condition.apply();

        MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
        mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
        mdWebInteger.apply();

        try
        {
          BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
          business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Different Value");
          business.setValue("testDate", "2001-10-09");
          business.setValue("testInteger", "-1");

          ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
          ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
          excelSheet.addRow(business);

          byte[] bytes = exporter.write();

          ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

          for (ImportContext context : importer.getContexts())
          {
            context.addListener(new FormValidationImportListener(mdForm));
          }

          byte[] results = importer.read();

          ExcelExporterTest.writeFile(results);

          Assert.assertTrue(results.length > 0);
        }
        finally
        {
          TestFixtureFactory.delete(mdWebInteger);
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdForm);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdAttributeDate);
    }
  }

  @Request
  @Test
  public void testDoubleGTValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdForm, mdAttributeDouble);
      mdWebDouble.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "GT");

      DoubleConditionDAO condition = DoubleConditionDAO.newInstance();
      condition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getId());
      condition.setValue(DoubleConditionInfo.VALUE, "10.0000");
      condition.addItem(DoubleConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      try
      {

        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
        business.setValue("testDouble", "11.0000");
        business.setValue("testInteger", "-1");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        Assert.assertEquals(0, results.length);

        List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

        Assert.assertEquals(1, ids.size());

        BusinessDAOIF test = BusinessDAO.get(ids.get(0));

        try
        {
          Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
          Assert.assertEquals(business.getValue("testDouble"), test.getValue("testDouble"));
          Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
        }
        finally
        {
          TestFixtureFactory.delete(test);
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdWebInteger);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testFailDoubleGTValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdForm, mdAttributeDouble);
      mdWebDouble.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "GT");

      DoubleConditionDAO condition = DoubleConditionDAO.newInstance();
      condition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getId());
      condition.setValue(DoubleConditionInfo.VALUE, "10.0000");
      condition.addItem(DoubleConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      try
      {
        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Different Value");
        business.setValue("testDouble", "9");
        business.setValue("testInteger", "-1");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        ExcelExporterTest.writeFile(results);

        Assert.assertTrue(results.length > 0);
      }
      finally
      {
        TestFixtureFactory.delete(mdWebInteger);
      }

    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testLongGTValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebLongDAO mdWebLong = TestFixtureFactory.addLongField(mdForm, mdAttributeLong);
      mdWebLong.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "GT");

      LongConditionDAO condition = LongConditionDAO.newInstance();
      condition.setValue(LongConditionInfo.DEFINING_MD_FIELD, mdWebLong.getId());
      condition.setValue(LongConditionInfo.VALUE, "10");
      condition.addItem(LongConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      try
      {
        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
        business.setValue("testLong", "11");
        business.setValue("testInteger", "-1");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        Assert.assertEquals(0, results.length);

        List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

        Assert.assertEquals(1, ids.size());

        BusinessDAOIF test = BusinessDAO.get(ids.get(0));

        try
        {
          Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
          Assert.assertEquals(business.getValue("testLong"), test.getValue("testLong"));
          Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
        }
        finally
        {
          TestFixtureFactory.delete(test);
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdWebInteger);
      }

    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testFailLongGTValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebLongDAO mdWebLong = TestFixtureFactory.addLongField(mdForm, mdAttributeLong);
      mdWebLong.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "GT");

      LongConditionDAO condition = LongConditionDAO.newInstance();
      condition.setValue(LongConditionInfo.DEFINING_MD_FIELD, mdWebLong.getId());
      condition.setValue(LongConditionInfo.VALUE, "10");
      condition.addItem(LongConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      try
      {
        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Different Value");
        business.setValue("testLong", "9");
        business.setValue("testInteger", "-1");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        ExcelExporterTest.writeFile(results);

        Assert.assertTrue(results.length > 0);
      }
      finally
      {
        TestFixtureFactory.delete(mdWebInteger);
      }

    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testDateGTValidation() throws IOException
  {
    MdAttributeDateDAO mdAttributeDate = TestFixtureFactory.addDateAttribute(mdBusiness);
    mdAttributeDate.apply();

    try
    {

      MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
      mdForm.apply();

      try
      {
        MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
        mdWebCharacter.apply();

        MdWebDateDAO mdWebDate = TestFixtureFactory.addDateField(mdForm, mdAttributeDate);
        mdWebDate.apply();

        EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "GT");

        DateConditionDAO condition = DateConditionDAO.newInstance();
        condition.setValue(DateConditionInfo.DEFINING_MD_FIELD, mdWebDate.getId());
        condition.setValue(DateConditionInfo.VALUE, "2001-10-11");
        condition.addItem(DateConditionInfo.OPERATION, item.getId());
        condition.apply();

        MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
        mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
        mdWebInteger.apply();

        try
        {
          BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
          business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
          business.setValue("testDate", "2001-11-11");
          business.setValue("testInteger", "-1");

          ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
          ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
          excelSheet.addRow(business);

          byte[] bytes = exporter.write();

          ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

          for (ImportContext context : importer.getContexts())
          {
            context.addListener(new FormValidationImportListener(mdForm));
          }

          byte[] results = importer.read();

          Assert.assertEquals(0, results.length);

          List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

          Assert.assertEquals(1, ids.size());

          BusinessDAOIF test = BusinessDAO.get(ids.get(0));

          try
          {
            Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
            Assert.assertEquals(business.getValue("testDate"), test.getValue("testDate"));
            Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
          }
          finally
          {
            TestFixtureFactory.delete(test);
          }
        }
        finally
        {
          TestFixtureFactory.delete(mdWebInteger);
        }

      }
      finally
      {
        TestFixtureFactory.delete(mdForm);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdAttributeDate);
    }
  }

  @Request
  @Test
  public void testFailDateGTValidation() throws IOException
  {

    MdAttributeDateDAO mdAttributeDate = TestFixtureFactory.addDateAttribute(mdBusiness);
    mdAttributeDate.apply();

    try
    {

      MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
      mdForm.apply();

      try
      {
        MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
        mdWebCharacter.apply();

        MdWebDateDAO mdWebDate = TestFixtureFactory.addDateField(mdForm, mdAttributeDate);
        mdWebDate.apply();

        EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "GT");

        DateConditionDAO condition = DateConditionDAO.newInstance();
        condition.setValue(DateConditionInfo.DEFINING_MD_FIELD, mdWebDate.getId());
        condition.setValue(DateConditionInfo.VALUE, "2001-10-09");
        condition.addItem(DateConditionInfo.OPERATION, item.getId());
        condition.apply();

        MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
        mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
        mdWebInteger.apply();

        try
        {
          BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
          business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Different Value");
          business.setValue("testDate", "2001-08-08");
          business.setValue("testInteger", "-1");

          ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
          ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
          excelSheet.addRow(business);

          byte[] bytes = exporter.write();

          ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

          for (ImportContext context : importer.getContexts())
          {
            context.addListener(new FormValidationImportListener(mdForm));
          }

          byte[] results = importer.read();

          ExcelExporterTest.writeFile(results);

          Assert.assertTrue(results.length > 0);
        }
        finally
        {
          TestFixtureFactory.delete(mdWebInteger);
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdForm);
      }

    }
    finally
    {
      TestFixtureFactory.delete(mdAttributeDate);
    }
  }

  @Request
  @Test
  public void testDoubleGTEValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdForm, mdAttributeDouble);
      mdWebDouble.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "GTE");

      DoubleConditionDAO condition = DoubleConditionDAO.newInstance();
      condition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getId());
      condition.setValue(DoubleConditionInfo.VALUE, "10.0000");
      condition.addItem(DoubleConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      try
      {
        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
        business.setValue("testDouble", "11.0000");
        business.setValue("testInteger", "-1");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        Assert.assertEquals(0, results.length);

        List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

        Assert.assertEquals(1, ids.size());

        BusinessDAOIF test = BusinessDAO.get(ids.get(0));

        try
        {
          Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
          Assert.assertEquals(business.getValue("testDouble"), test.getValue("testDouble"));
          Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
        }
        finally
        {
          TestFixtureFactory.delete(test);
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdWebInteger);
      }

    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testFailDoubleGTEValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdForm, mdAttributeDouble);
      mdWebDouble.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "GTE");

      DoubleConditionDAO condition = DoubleConditionDAO.newInstance();
      condition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getId());
      condition.setValue(DoubleConditionInfo.VALUE, "10.0000");
      condition.addItem(DoubleConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      try
      {
        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Different Value");
        business.setValue("testDouble", "9");
        business.setValue("testInteger", "-1");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        ExcelExporterTest.writeFile(results);

        Assert.assertTrue(results.length > 0);
      }
      finally
      {
        TestFixtureFactory.delete(mdWebInteger);
      }

    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testLongGTEValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebLongDAO mdWebLong = TestFixtureFactory.addLongField(mdForm, mdAttributeLong);
      mdWebLong.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "GTE");

      LongConditionDAO condition = LongConditionDAO.newInstance();
      condition.setValue(LongConditionInfo.DEFINING_MD_FIELD, mdWebLong.getId());
      condition.setValue(LongConditionInfo.VALUE, "10");
      condition.addItem(LongConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      try
      {
        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
        business.setValue("testLong", "11");
        business.setValue("testInteger", "-1");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        Assert.assertEquals(0, results.length);

        List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

        Assert.assertEquals(1, ids.size());

        BusinessDAOIF test = BusinessDAO.get(ids.get(0));

        try
        {
          Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
          Assert.assertEquals(business.getValue("testLong"), test.getValue("testLong"));
          Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
        }
        finally
        {
          TestFixtureFactory.delete(test);
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdWebInteger);
      }

    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testFailLongGTEValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebLongDAO mdWebLong = TestFixtureFactory.addLongField(mdForm, mdAttributeLong);
      mdWebLong.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "GTE");

      LongConditionDAO condition = LongConditionDAO.newInstance();
      condition.setValue(LongConditionInfo.DEFINING_MD_FIELD, mdWebLong.getId());
      condition.setValue(LongConditionInfo.VALUE, "10");
      condition.addItem(LongConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      try
      {
        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Different Value");
        business.setValue("testLong", "9");
        business.setValue("testInteger", "-1");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        ExcelExporterTest.writeFile(results);

        Assert.assertTrue(results.length > 0);
      }
      finally
      {
        TestFixtureFactory.delete(mdWebInteger);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testDateGTEValidation() throws IOException
  {
    MdAttributeDateDAO mdAttributeDate = TestFixtureFactory.addDateAttribute(mdBusiness);
    mdAttributeDate.apply();

    try
    {

      MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
      mdForm.apply();

      try
      {
        MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
        mdWebCharacter.apply();

        MdWebDateDAO mdWebDate = TestFixtureFactory.addDateField(mdForm, mdAttributeDate);
        mdWebDate.apply();

        EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "GTE");

        DateConditionDAO condition = DateConditionDAO.newInstance();
        condition.setValue(DateConditionInfo.DEFINING_MD_FIELD, mdWebDate.getId());
        condition.setValue(DateConditionInfo.VALUE, "2001-10-11");
        condition.addItem(DateConditionInfo.OPERATION, item.getId());
        condition.apply();

        MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
        mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
        mdWebInteger.apply();

        try
        {
          BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
          business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
          business.setValue("testDate", "2001-11-11");
          business.setValue("testInteger", "-1");

          ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
          ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
          excelSheet.addRow(business);

          byte[] bytes = exporter.write();

          ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

          for (ImportContext context : importer.getContexts())
          {
            context.addListener(new FormValidationImportListener(mdForm));
          }

          byte[] results = importer.read();

          Assert.assertEquals(0, results.length);

          List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

          Assert.assertEquals(1, ids.size());

          BusinessDAOIF test = BusinessDAO.get(ids.get(0));

          try
          {
            Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
            Assert.assertEquals(business.getValue("testDate"), test.getValue("testDate"));
            Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
          }
          finally
          {
            TestFixtureFactory.delete(test);
          }
        }
        finally
        {
          TestFixtureFactory.delete(mdWebInteger);
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdForm);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdAttributeDate);
    }
  }

  @Request
  @Test
  public void testFailDateGTEValidation() throws IOException
  {

    MdAttributeDateDAO mdAttributeDate = TestFixtureFactory.addDateAttribute(mdBusiness);
    mdAttributeDate.apply();

    try
    {

      MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
      mdForm.apply();

      try
      {
        MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
        mdWebCharacter.apply();

        MdWebDateDAO mdWebDate = TestFixtureFactory.addDateField(mdForm, mdAttributeDate);
        mdWebDate.apply();

        EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "GTE");

        DateConditionDAO condition = DateConditionDAO.newInstance();
        condition.setValue(DateConditionInfo.DEFINING_MD_FIELD, mdWebDate.getId());
        condition.setValue(DateConditionInfo.VALUE, "2001-10-09");
        condition.addItem(DateConditionInfo.OPERATION, item.getId());
        condition.apply();

        MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
        mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
        mdWebInteger.apply();

        try
        {
          BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
          business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Different Value");
          business.setValue("testDate", "2001-08-08");
          business.setValue("testInteger", "-1");

          ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
          ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
          excelSheet.addRow(business);

          byte[] bytes = exporter.write();

          ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

          for (ImportContext context : importer.getContexts())
          {
            context.addListener(new FormValidationImportListener(mdForm));
          }

          byte[] results = importer.read();

          ExcelExporterTest.writeFile(results);

          Assert.assertTrue(results.length > 0);
        }
        finally
        {
          TestFixtureFactory.delete(mdWebInteger);
        }

      }
      finally
      {
        TestFixtureFactory.delete(mdForm);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdAttributeDate);
    }
  }

  @Request
  @Test
  public void testDoubleLTValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdForm, mdAttributeDouble);
      mdWebDouble.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LT");

      DoubleConditionDAO condition = DoubleConditionDAO.newInstance();
      condition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getId());
      condition.setValue(DoubleConditionInfo.VALUE, "10.0000");
      condition.addItem(DoubleConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      try
      {
        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
        business.setValue("testDouble", "9.0000");
        business.setValue("testInteger", "-1");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        Assert.assertEquals(0, results.length);

        List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

        Assert.assertEquals(1, ids.size());

        BusinessDAOIF test = BusinessDAO.get(ids.get(0));

        try
        {
          Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
          Assert.assertEquals(business.getValue("testDouble"), test.getValue("testDouble"));
          Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
        }
        finally
        {
          TestFixtureFactory.delete(test);
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdWebInteger);
      }

    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testFailDoubleLTValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdForm, mdAttributeDouble);
      mdWebDouble.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LT");

      DoubleConditionDAO condition = DoubleConditionDAO.newInstance();
      condition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getId());
      condition.setValue(DoubleConditionInfo.VALUE, "10.0000");
      condition.addItem(DoubleConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
      business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Different Value");
      business.setValue("testDouble", "23");
      business.setValue("testInteger", "-1");

      ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
      ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
      excelSheet.addRow(business);

      byte[] bytes = exporter.write();

      ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

      for (ImportContext context : importer.getContexts())
      {
        context.addListener(new FormValidationImportListener(mdForm));
      }

      byte[] results = importer.read();

      ExcelExporterTest.writeFile(results);

      Assert.assertTrue(results.length > 0);
    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testLongLTValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebLongDAO mdWebLong = TestFixtureFactory.addLongField(mdForm, mdAttributeLong);
      mdWebLong.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LT");

      LongConditionDAO condition = LongConditionDAO.newInstance();
      condition.setValue(LongConditionInfo.DEFINING_MD_FIELD, mdWebLong.getId());
      condition.setValue(LongConditionInfo.VALUE, "10");
      condition.addItem(LongConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
      business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
      business.setValue("testLong", "2");
      business.setValue("testInteger", "-1");

      ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
      ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
      excelSheet.addRow(business);

      byte[] bytes = exporter.write();

      ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

      for (ImportContext context : importer.getContexts())
      {
        context.addListener(new FormValidationImportListener(mdForm));
      }

      byte[] results = importer.read();

      Assert.assertEquals(0, results.length);

      List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

      Assert.assertEquals(1, ids.size());

      BusinessDAOIF test = BusinessDAO.get(ids.get(0));

      try
      {
        Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
        Assert.assertEquals(business.getValue("testLong"), test.getValue("testLong"));
        Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
      }
      finally
      {
        TestFixtureFactory.delete(test);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testFailLongLTValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebLongDAO mdWebLong = TestFixtureFactory.addLongField(mdForm, mdAttributeLong);
      mdWebLong.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LT");

      LongConditionDAO condition = LongConditionDAO.newInstance();
      condition.setValue(LongConditionInfo.DEFINING_MD_FIELD, mdWebLong.getId());
      condition.setValue(LongConditionInfo.VALUE, "10");
      condition.addItem(LongConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
      business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Different Value");
      business.setValue("testLong", "45");
      business.setValue("testInteger", "-1");

      ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
      ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
      excelSheet.addRow(business);

      byte[] bytes = exporter.write();

      ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

      for (ImportContext context : importer.getContexts())
      {
        context.addListener(new FormValidationImportListener(mdForm));
      }

      byte[] results = importer.read();

      ExcelExporterTest.writeFile(results);

      Assert.assertTrue(results.length > 0);
    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testDateLTValidation() throws IOException
  {
    MdAttributeDateDAO mdAttributeDate = TestFixtureFactory.addDateAttribute(mdBusiness);
    mdAttributeDate.apply();

    try
    {

      MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
      mdForm.apply();

      try
      {
        MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
        mdWebCharacter.apply();

        MdWebDateDAO mdWebDate = TestFixtureFactory.addDateField(mdForm, mdAttributeDate);
        mdWebDate.apply();

        EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LT");

        DateConditionDAO condition = DateConditionDAO.newInstance();
        condition.setValue(DateConditionInfo.DEFINING_MD_FIELD, mdWebDate.getId());
        condition.setValue(DateConditionInfo.VALUE, "2001-10-11");
        condition.addItem(DateConditionInfo.OPERATION, item.getId());
        condition.apply();

        MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
        mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
        mdWebInteger.apply();

        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
        business.setValue("testDate", "2001-02-02");
        business.setValue("testInteger", "-1");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        Assert.assertEquals(0, results.length);

        List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

        Assert.assertEquals(1, ids.size());

        BusinessDAOIF test = BusinessDAO.get(ids.get(0));

        try
        {
          Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
          Assert.assertEquals(business.getValue("testDate"), test.getValue("testDate"));
          Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
        }
        finally
        {
          TestFixtureFactory.delete(test);
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdForm);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdAttributeDate);
    }
  }

  @Request
  @Test
  public void testFailDateLTValidation() throws IOException
  {

    MdAttributeDateDAO mdAttributeDate = TestFixtureFactory.addDateAttribute(mdBusiness);
    mdAttributeDate.apply();

    try
    {

      MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
      mdForm.apply();

      try
      {
        MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
        mdWebCharacter.apply();

        MdWebDateDAO mdWebDate = TestFixtureFactory.addDateField(mdForm, mdAttributeDate);
        mdWebDate.apply();

        EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LT");

        DateConditionDAO condition = DateConditionDAO.newInstance();
        condition.setValue(DateConditionInfo.DEFINING_MD_FIELD, mdWebDate.getId());
        condition.setValue(DateConditionInfo.VALUE, "2001-10-09");
        condition.addItem(DateConditionInfo.OPERATION, item.getId());
        condition.apply();

        MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
        mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
        mdWebInteger.apply();

        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Different Value");
        business.setValue("testDate", "2001-11-11");
        business.setValue("testInteger", "-1");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        ExcelExporterTest.writeFile(results);

        Assert.assertTrue(results.length > 0);
      }
      finally
      {
        TestFixtureFactory.delete(mdForm);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdAttributeDate);
    }
  }

  @Request
  @Test
  public void testDoubleLTEValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdForm, mdAttributeDouble);
      mdWebDouble.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LTE");

      DoubleConditionDAO condition = DoubleConditionDAO.newInstance();
      condition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getId());
      condition.setValue(DoubleConditionInfo.VALUE, "12.12");
      condition.addItem(DoubleConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
      business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
      business.setValue("testDouble", "11.0000");
      business.setValue("testInteger", "-1");

      ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
      ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
      excelSheet.addRow(business);

      byte[] bytes = exporter.write();

      ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

      for (ImportContext context : importer.getContexts())
      {
        context.addListener(new FormValidationImportListener(mdForm));
      }

      byte[] results = importer.read();

      Assert.assertEquals(0, results.length);

      List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

      Assert.assertEquals(1, ids.size());

      BusinessDAOIF test = BusinessDAO.get(ids.get(0));

      try
      {
        Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
        Assert.assertEquals(business.getValue("testDouble"), test.getValue("testDouble"));
        Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
      }
      finally
      {
        TestFixtureFactory.delete(test);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testFailDoubleLTEValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebDoubleDAO mdWebDouble = TestFixtureFactory.addDoubleField(mdForm, mdAttributeDouble);
      mdWebDouble.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LTE");

      DoubleConditionDAO condition = DoubleConditionDAO.newInstance();
      condition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getId());
      condition.setValue(DoubleConditionInfo.VALUE, "3.4");
      condition.addItem(DoubleConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
      business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Different Value");
      business.setValue("testDouble", "9");
      business.setValue("testInteger", "-1");

      ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
      ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
      excelSheet.addRow(business);

      byte[] bytes = exporter.write();

      ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

      for (ImportContext context : importer.getContexts())
      {
        context.addListener(new FormValidationImportListener(mdForm));
      }

      byte[] results = importer.read();

      ExcelExporterTest.writeFile(results);

      Assert.assertTrue(results.length > 0);
    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testLongLTEValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebLongDAO mdWebLong = TestFixtureFactory.addLongField(mdForm, mdAttributeLong);
      mdWebLong.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LTE");

      LongConditionDAO condition = LongConditionDAO.newInstance();
      condition.setValue(LongConditionInfo.DEFINING_MD_FIELD, mdWebLong.getId());
      condition.setValue(LongConditionInfo.VALUE, "10");
      condition.addItem(LongConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
      business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
      business.setValue("testLong", "2");
      business.setValue("testInteger", "-1");

      ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
      ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
      excelSheet.addRow(business);

      byte[] bytes = exporter.write();

      ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

      for (ImportContext context : importer.getContexts())
      {
        context.addListener(new FormValidationImportListener(mdForm));
      }

      byte[] results = importer.read();

      Assert.assertEquals(0, results.length);

      List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

      Assert.assertEquals(1, ids.size());

      BusinessDAOIF test = BusinessDAO.get(ids.get(0));

      try
      {
        Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
        Assert.assertEquals(business.getValue("testLong"), test.getValue("testLong"));
        Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
      }
      finally
      {
        TestFixtureFactory.delete(test);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testFailLongLTEValidation() throws IOException
  {
    MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    try
    {
      MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
      mdWebCharacter.apply();

      MdWebLongDAO mdWebLong = TestFixtureFactory.addLongField(mdForm, mdAttributeLong);
      mdWebLong.apply();

      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LTE");

      LongConditionDAO condition = LongConditionDAO.newInstance();
      condition.setValue(LongConditionInfo.DEFINING_MD_FIELD, mdWebLong.getId());
      condition.setValue(LongConditionInfo.VALUE, "10");
      condition.addItem(LongConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
      mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      mdWebInteger.apply();

      BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
      business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Different Value");
      business.setValue("testLong", "329");
      business.setValue("testInteger", "-1");

      ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
      ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
      excelSheet.addRow(business);

      byte[] bytes = exporter.write();

      ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

      for (ImportContext context : importer.getContexts())
      {
        context.addListener(new FormValidationImportListener(mdForm));
      }

      byte[] results = importer.read();

      ExcelExporterTest.writeFile(results);

      Assert.assertTrue(results.length > 0);
    }
    finally
    {
      TestFixtureFactory.delete(mdForm);
    }
  }

  @Request
  @Test
  public void testDateLTEValidation() throws IOException
  {
    MdAttributeDateDAO mdAttributeDate = TestFixtureFactory.addDateAttribute(mdBusiness);
    mdAttributeDate.apply();

    try
    {

      MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
      mdForm.apply();

      try
      {
        MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
        mdWebCharacter.apply();

        MdWebDateDAO mdWebDate = TestFixtureFactory.addDateField(mdForm, mdAttributeDate);
        mdWebDate.apply();

        EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LTE");

        DateConditionDAO condition = DateConditionDAO.newInstance();
        condition.setValue(DateConditionInfo.DEFINING_MD_FIELD, mdWebDate.getId());
        condition.setValue(DateConditionInfo.VALUE, "2001-10-11");
        condition.addItem(DateConditionInfo.OPERATION, item.getId());
        condition.apply();

        MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
        mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
        mdWebInteger.apply();

        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Character Value");
        business.setValue("testDate", "2001-02-03");
        business.setValue("testInteger", "-1");

        ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
        ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
        excelSheet.addRow(business);

        byte[] bytes = exporter.write();

        ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

        for (ImportContext context : importer.getContexts())
        {
          context.addListener(new FormValidationImportListener(mdForm));
        }

        byte[] results = importer.read();

        Assert.assertEquals(0, results.length);

        List<String> ids = BusinessDAO.getEntityIdsFromDB(mdBusiness);

        Assert.assertEquals(1, ids.size());

        BusinessDAOIF test = BusinessDAO.get(ids.get(0));

        try
        {
          Assert.assertEquals(business.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
          Assert.assertEquals(business.getValue("testDate"), test.getValue("testDate"));
          Assert.assertEquals(business.getValue("testInteger"), test.getValue("testInteger"));
        }
        finally
        {
          TestFixtureFactory.delete(test);
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdForm);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdAttributeDate);
    }
  }

  @Request
  @Test
  public void testFailDateLTEValidation() throws IOException
  {

    MdAttributeDateDAO mdAttributeDate = TestFixtureFactory.addDateAttribute(mdBusiness);
    mdAttributeDate.apply();

    try
    {

      MdWebFormDAO mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
      mdForm.apply();

      try
      {
        MdWebCharacterDAO mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
        mdWebCharacter.apply();

        MdWebDateDAO mdWebDate = TestFixtureFactory.addDateField(mdForm, mdAttributeDate);
        mdWebDate.apply();

        EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LTE");

        DateConditionDAO condition = DateConditionDAO.newInstance();
        condition.setValue(DateConditionInfo.DEFINING_MD_FIELD, mdWebDate.getId());
        condition.setValue(DateConditionInfo.VALUE, "2001-02-05");
        condition.addItem(DateConditionInfo.OPERATION, item.getId());
        condition.apply();

        MdWebIntegerDAO mdWebInteger = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
        mdWebInteger.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
        mdWebInteger.apply();

        try
        {
          BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
          business.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Different Value");
          business.setValue("testDate", "2001-08-08");
          business.setValue("testInteger", "-1");

          ExcelExporter exporter = new FormExcelExporter(new MdWebAttributeFilter());
          ExcelExportSheet excelSheet = exporter.addTemplate(mdForm.definesType());
          excelSheet.addRow(business);

          byte[] bytes = exporter.write();

          ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));

          for (ImportContext context : importer.getContexts())
          {
            context.addListener(new FormValidationImportListener(mdForm));
          }

          byte[] results = importer.read();

          ExcelExporterTest.writeFile(results);

          Assert.assertTrue(results.length > 0);
        }
        finally
        {
          TestFixtureFactory.delete(mdWebInteger);
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdForm);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdAttributeDate);
    }
  }

  @Request
  @Test
  public void testImportView() throws IOException
  {
    MdViewDAO mdView = TestFixtureFactory.createMdView1();
    mdView.apply();

    try
    {
      MdAttributeVirtualDAO mdAttributeVirtual = TestFixtureFactory.addVirtualAttribute(mdView, mdAttributeDouble);
      mdAttributeVirtual.apply();

      TransientDAO mutable = TransientDAO.newInstance(mdView.definesType());
      mutable.setValue("testVirtual", "10.0000");

      ExcelExporter exporter = new ExcelExporter();

      ExcelExportSheet excelSheet = exporter.addTemplate(mdView.definesType());
      excelSheet.addRow(mutable);

      byte[] bytes = exporter.write();

      ExcelExporterTest.writeFile(bytes);

      ExcelImporter importer = new ExcelImporter(new ByteArrayInputStream(bytes));
      byte[] results = importer.read();

      Assert.assertEquals(0, results.length);
    }
    finally
    {
      TestFixtureFactory.delete(mdView);
    }
  }

}
