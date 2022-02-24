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
package com.runwaysdk.dataaccess;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.MdAttributeDateUtil;
import com.runwaysdk.constants.MdWebAttributeInfo;
import com.runwaysdk.constants.MdWebBooleanInfo;
import com.runwaysdk.constants.MdWebBreakInfo;
import com.runwaysdk.constants.MdWebCharacterInfo;
import com.runwaysdk.constants.MdWebCommentInfo;
import com.runwaysdk.constants.MdWebDateInfo;
import com.runwaysdk.constants.MdWebDateTimeInfo;
import com.runwaysdk.constants.MdWebDecimalInfo;
import com.runwaysdk.constants.MdWebDoubleInfo;
import com.runwaysdk.constants.MdWebFieldInfo;
import com.runwaysdk.constants.MdWebFloatInfo;
import com.runwaysdk.constants.MdWebFormInfo;
import com.runwaysdk.constants.MdWebGeoInfo;
import com.runwaysdk.constants.MdWebHeaderInfo;
import com.runwaysdk.constants.MdWebIntegerInfo;
import com.runwaysdk.constants.MdWebLongInfo;
import com.runwaysdk.constants.MdWebMultipleTermInfo;
import com.runwaysdk.constants.MdWebReferenceInfo;
import com.runwaysdk.constants.MdWebSingleTermGridInfo;
import com.runwaysdk.constants.MdWebSingleTermInfo;
import com.runwaysdk.constants.MdWebTextInfo;
import com.runwaysdk.constants.MdWebTimeInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdFieldDAO;
import com.runwaysdk.dataaccess.metadata.MdWebBreakDAO;
import com.runwaysdk.dataaccess.metadata.MdWebCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdWebCommentDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFieldDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFormDAO;
import com.runwaysdk.dataaccess.metadata.MdWebGeoDAO;
import com.runwaysdk.dataaccess.metadata.MdWebGroupDAO;
import com.runwaysdk.dataaccess.metadata.MdWebHeaderDAO;
import com.runwaysdk.dataaccess.metadata.MdWebMultipleTermDAO;
import com.runwaysdk.dataaccess.metadata.MdWebSingleTermDAO;
import com.runwaysdk.dataaccess.metadata.MdWebSingleTermGridDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.MdWebHeader;
import com.runwaysdk.system.metadata.MdWebText;
import com.runwaysdk.system.metadata.MetadataDisplayLabel;
import com.runwaysdk.system.metadata.WebGroupField;

public class MdWebFormTest extends MdFormDAOTest
{
  /**
   * The MdForm representing the type TestForm.
   */
  private static MdWebFormDAO                        testForm;

  /**
   * List of the fields in the simple book form, in order.
   */
  private static LinkedHashMap<String, MdFieldDAOIF> fields;

  @SuppressWarnings("unused")
  private static String                              FORM_TYPE;

  @Transaction
  protected static void classSetUpTrans()
  {
    try
    {
      defineMdClasses();

      fields = new LinkedHashMap<String, MdFieldDAOIF>();

      // form
      String testFormName = "TestForm";

      FORM_TYPE = PACKAGE + testFormName;

      testForm = MdWebFormDAO.newInstance();
      testForm.setValue(MdWebFormInfo.NAME, testFormName);
      testForm.setValue(MdWebFormInfo.PACKAGE, PACKAGE);
      testForm.setValue(MdWebFormInfo.FORM_NAME, testFormName);
      testForm.setStructValue(MdWebFormInfo.DISPLAY_LABEL, MetadataDisplayLabel.DEFAULTLOCALE, "Test Form.");
      testForm.setStructValue(MdWebFormInfo.DESCRIPTION, MetadataDisplayLabel.DEFAULTLOCALE, "An MdForm representation of a TestType.");
      testForm.setValue(MdWebFormInfo.FORM_MD_CLASS, testTypeMd.getOid());
      testForm.apply();

      MdFieldDAO charField = createField(testForm, charAttr, MdWebCharacterInfo.CLASS);
      charField.setValue(MdWebCharacterInfo.DISPLAY_LENGTH, "15");
      charField.setValue(MdWebCharacterInfo.MAX_LENGTH, "20");
      applyField(charField);

      MdFieldDAO textField = createField(testForm, textAttr, MdWebTextInfo.CLASS);
      textField.setValue(MdWebTextInfo.WIDTH, "50");
      textField.setValue(MdWebText.HEIGHT, "40");
      applyField(textField);

      // Set the end date range to now plus one day
      Date now = new Date();
      Calendar cal = Calendar.getInstance();
      cal.setTime(now);
      cal.add(Calendar.DATE, 1);
      Date endDate = cal.getTime();

      MdFieldDAO dateField = createField(testForm, dateAttr, MdWebDateInfo.CLASS);
      dateField.setValue(MdWebDateInfo.AFTER_TODAY_EXCLUSIVE, "true");
      dateField.setValue(MdWebDateInfo.AFTER_TODAY_INCLUSIVE, "false");
      dateField.setValue(MdWebDateInfo.BEFORE_TODAY_INCLUSIVE, "false");
      dateField.setValue(MdWebDateInfo.BEFORE_TODAY_INCLUSIVE, "false");
      dateField.setValue(MdWebDateInfo.END_DATE, MdAttributeDateUtil.getTypeUnsafeValue(endDate));
      dateField.setValue(MdWebDateInfo.START_DATE, "");
      applyField(dateField);

      MdFieldDAO dateTimeField = createField(testForm, dateTimeAttr, MdWebDateTimeInfo.CLASS);
      applyField(dateTimeField);

      MdFieldDAO timeField = createField(testForm, timeAttr, MdWebTimeInfo.CLASS);
      applyField(timeField);

      MdFieldDAO floatField = createField(testForm, floatAttr, MdWebFloatInfo.CLASS);
      floatField.setValue(MdWebFloatInfo.DECPRECISION, "7");
      floatField.setValue(MdWebFloatInfo.DECSCALE, "3");
      floatField.setValue(MdWebFloatInfo.STARTRANGE, "1.00");
      floatField.setValue(MdWebFloatInfo.ENDRANGE, "5.45");
      applyField(floatField);

      MdFieldDAO decimalField = createField(testForm, decimalAttr, MdWebDecimalInfo.CLASS);
      decimalField.setValue(MdWebDecimalInfo.DECPRECISION, "5");
      decimalField.setValue(MdWebDecimalInfo.DECSCALE, "2");
      decimalField.setValue(MdWebDecimalInfo.STARTRANGE, "-2.00");
      decimalField.setValue(MdWebDecimalInfo.ENDRANGE, "20.00");
      applyField(decimalField);

      MdFieldDAO doubleField = createField(testForm, doubleAttr, MdWebDoubleInfo.CLASS);
      doubleField.setValue(MdWebDoubleInfo.DECPRECISION, "10");
      doubleField.setValue(MdWebDoubleInfo.DECSCALE, "5");
      doubleField.setValue(MdWebDoubleInfo.STARTRANGE, "");
      doubleField.setValue(MdWebDoubleInfo.ENDRANGE, "");
      applyField(doubleField);

      MdFieldDAO integerField = createField(testForm, integerAttr, MdWebIntegerInfo.CLASS);
      integerField.setValue(MdWebIntegerInfo.STARTRANGE, "1");
      integerField.setValue(MdWebIntegerInfo.ENDRANGE, "100");
      applyField(integerField);

      MdFieldDAO longField = createField(testForm, longAttr, MdWebLongInfo.CLASS);
      longField.setValue(MdWebLongInfo.STARTRANGE, "1");
      longField.setValue(MdWebLongInfo.ENDRANGE, "100");
      applyField(longField);

      MdFieldDAO booleanField = createField(testForm, booleanAttr, MdWebBooleanInfo.CLASS);
      applyField(booleanField);

      MdFieldDAO refField = createField(testForm, referenceAttr, MdWebReferenceInfo.CLASS);
      applyField(refField);

      MdWebGroupDAO group = TestFixtureFactory.addGroupField(testForm);
      applyField(group);

      MdFieldDAO groupIntField = createField(testForm, group1Int, MdWebIntegerInfo.CLASS);
      applyField(groupIntField);

      groupIntField.addParent(group, WebGroupField.CLASS).apply();

      MdWebGroupDAO group2 = TestFixtureFactory.addGroupField(testForm, "nestedGroup");
      applyField(group2);

      MdFieldDAO groupBoolField = createField(testForm, group2Boolean, MdWebBooleanInfo.CLASS);
      applyField(groupBoolField);

      groupBoolField.addParent(group2, WebGroupField.CLASS).apply();

      // geo field
      MdFieldDAO geoField = createField(testForm, geoAttr, MdWebGeoInfo.CLASS);
      applyField(geoField);

      // single term
      MdFieldDAO singleTermField = createField(testForm, singleTermAttr, MdWebSingleTermInfo.CLASS);
      applyField(singleTermField);

      // multiple term
      MdFieldDAO multipleTermField = createField(testForm, multipleTermAttr, MdWebMultipleTermInfo.CLASS);
      applyField(multipleTermField);

      // single term grid
      MdFieldDAO singleGridField = createField(testForm, singleGridAttr, MdWebSingleTermGridInfo.CLASS);
      applyField(singleGridField);

      // // point
      // MdFieldDAO pointField = createField(testForm, pointAttr,
      // MdWebPointInfo.CLASS);
      // applyField(pointField);

      // header
      MdFieldDAO headerField = MdWebHeaderDAO.newInstance();
      headerField.setValue(MdWebHeaderInfo.FIELD_NAME, "testHeader");
      headerField.setStructValue(MdWebHeaderInfo.DISPLAY_LABEL, MetadataDisplayLabel.DEFAULTLOCALE, "Test Header");
      headerField.setStructValue(MdWebHeaderInfo.DESCRIPTION, MetadataDisplayLabel.DEFAULTLOCALE, "Test Header Desc");
      headerField.setValue(MdWebHeaderInfo.DEFINING_MD_FORM, testForm.getOid());
      headerField.setStructValue(MdWebHeader.HEADERTEXT, MetadataDisplayLabel.DEFAULTLOCALE, "This is a test header");
      applyField(headerField);

      // break
      MdFieldDAO breakField = MdWebBreakDAO.newInstance();
      breakField.setValue(MdWebBreakInfo.FIELD_NAME, "testBreak");
      breakField.setStructValue(MdWebBreakInfo.DISPLAY_LABEL, MetadataDisplayLabel.DEFAULTLOCALE, "Test Break");
      breakField.setStructValue(MdWebBreakInfo.DESCRIPTION, MetadataDisplayLabel.DEFAULTLOCALE, "Test Break Desc");
      breakField.setValue(MdWebBreakInfo.DEFINING_MD_FORM, testForm.getOid());
      applyField(breakField);

      // comment
      MdFieldDAO commentField = MdWebCommentDAO.newInstance();
      commentField.setValue(MdWebCommentInfo.FIELD_NAME, "testComment");
      commentField.setStructValue(MdWebCommentInfo.DISPLAY_LABEL, MetadataDisplayLabel.DEFAULTLOCALE, "Test Comment");
      commentField.setStructValue(MdWebCommentInfo.DESCRIPTION, MetadataDisplayLabel.DEFAULTLOCALE, "Test Comment Desc");
      commentField.setValue(MdWebCommentInfo.DEFINING_MD_FORM, testForm.getOid());
      commentField.setStructValue(MdWebCommentInfo.COMMENT_TEXT, MetadataDisplayLabel.DEFAULTLOCALE, "This is a test comment");
      applyField(commentField);

      // apply each MdField
      Iterator<MdFieldDAOIF> allFields = fields.values().iterator();
      while (allFields.hasNext())
      {
        allFields.next().getBusinessDAO().apply();
      }
    }
    catch (Throwable t)
    {
      classTearDown();
      throw new RuntimeException(t);
    }
  }

  protected static MdWebFieldDAO createField(MdWebFormDAOIF form, MdAttributeDAOIF md, String fieldClass)
  {

    BusinessDAO field = BusinessDAO.newInstance(fieldClass);

    String fieldName = md.definesAttribute();
    String display = md.getDisplayLabel(Locale.ENGLISH);
    String description = md.getDescription(Locale.ENGLISH);

    // set the common attributes
    field.setValue(MdWebFieldInfo.FIELD_NAME, fieldName);
    field.setStructValue(MdWebFieldInfo.DISPLAY_LABEL, MetadataDisplayLabel.DEFAULTLOCALE, display);
    field.setStructValue(MdWebFieldInfo.DESCRIPTION, MetadataDisplayLabel.DEFAULTLOCALE, description);
    field.setValue(MdWebFieldInfo.DEFINING_MD_FORM, form.getOid());

    if (field instanceof MdWebAttributeDAOIF)
    {
      field.setValue(MdWebAttributeInfo.DEFINING_MD_ATTRIBUTE, md.getOid());
    }

    return (MdWebFieldDAO) field;
  }

  private static void applyField(BusinessDAO field)
  {
    int order = fields.size();
    field.setValue(MdWebFieldInfo.FIELD_ORDER, Integer.toString(order));

    MdFieldDAOIF f = (MdFieldDAOIF) field;
    field.apply();
    fields.put(f.getFieldName(), f);
  }

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    classSetUpTrans();
  }

  @Transaction
  protected static void classTearDownTrans()
  {
    try
    {
      // TODO auto-cleanup of the Form once its underlying type is deleted?
      // TODO auto-cleanup of the Field once its underlying MdAttribute is
      // deleted?
      delete(testForm);
      destroyMdClasses();
    }
    catch (Throwable t)
    {
      System.out.println(t);
      throw new RuntimeException(t);
    }
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    classTearDownTrans();
  }

  /**
   * Tests the metadata on the MdForm instance.
   */
  @Request
  @Test
  public void testFormMetadata()
  {
    MdWebFormDAOIF form = MdWebFormDAO.get(testForm.getOid());

    Assert.assertEquals(testForm.getOid(), form.getOid());
    Assert.assertEquals(testForm.getTypeName(), form.getTypeName());
    Assert.assertEquals(testForm.getPackage(), form.getPackage());
    Assert.assertEquals(testForm.getFormName(), form.getFormName());
    Assert.assertEquals(testForm.getDisplayLabel(Locale.ENGLISH), form.getDisplayLabel(Locale.ENGLISH));
    Assert.assertEquals(testForm.getDescription(Locale.ENGLISH), form.getDescription(Locale.ENGLISH));
    Assert.assertEquals(testForm.getFormMdClass(), testTypeMd);
  }

  /**
   * Tests the relationships between the form and its fields. The field order
   * should be preserved in calls to MdWebFormDAO.getAllMdFields();
   */
  @Request
  @Test
  public void testFieldOrder()
  {
    MdWebFormDAOIF form = MdWebFormDAO.get(testForm.getOid());
    List<? extends MdFieldDAOIF> allFields = form.getOrderedMdFields();

    Iterator<String> iter = fields.keySet().iterator();
    int index = 0;
    while (iter.hasNext())
    {
      MdFieldDAOIF expected = fields.get(iter.next());
      MdFieldDAOIF returned = allFields.get(index);

      Assert.assertEquals(Integer.parseInt(returned.getFieldOrder()), index);
      Assert.assertEquals(returned.getFieldOrder(), expected.getFieldOrder());
      Assert.assertEquals(expected.getBusinessDAO().getOid(), returned.getBusinessDAO().getOid());

      index++;
    }
  }

  /**
   * Tests attributes common to all fields.
   */
  protected void _testField(MdWebFieldDAOIF expected, MdWebFieldDAOIF returned)
  {
    Assert.assertEquals(expected.getFieldName(), returned.getFieldName());
    Assert.assertEquals(expected.getFieldOrder(), returned.getFieldOrder());
    Assert.assertEquals(expected.getMdFormId(), returned.getMdFormId());
    Assert.assertEquals(expected.getDisplayLabel(Locale.ENGLISH), returned.getDisplayLabel(Locale.ENGLISH));
    Assert.assertEquals(expected.getDescription(Locale.ENGLISH), returned.getDescription(Locale.ENGLISH));
    Assert.assertEquals(expected.getMdForm(), returned.getMdForm());
    Assert.assertEquals(expected.getBusinessDAO(), returned.getBusinessDAO());

    if (expected instanceof MdWebAttributeDAOIF)
    {
      Assert.assertEquals( ( (MdWebAttributeDAOIF) expected ).getDefiningMdAttribute(), ( (MdWebAttributeDAOIF) returned ).getDefiningMdAttribute());
    }
  }

  @Request
  @Test
  public void testGeo()
  {
    String fieldName = geoAttr.definesAttribute();
    MdWebGeoDAO field = (MdWebGeoDAO) fields.get(fieldName);

    MdWebFormDAOIF form = MdWebFormDAO.get(testForm.getOid());
    MdWebGeoDAO geoField = (MdWebGeoDAO) form.getMdField(fieldName);

    _testField(field, geoField);
  }

  @Request
  @Test
  public void testSingleTerm()
  {
    String fieldName = singleTermAttr.definesAttribute();
    MdWebSingleTermDAO field = (MdWebSingleTermDAO) fields.get(fieldName);

    MdWebFormDAOIF form = MdWebFormDAO.get(testForm.getOid());
    MdWebSingleTermDAO singleTerm = (MdWebSingleTermDAO) form.getMdField(fieldName);

    _testField(field, singleTerm);
  }

  @Request
  @Test
  public void testMultipleTerm()
  {
    String fieldName = multipleTermAttr.definesAttribute();
    MdWebMultipleTermDAO field = (MdWebMultipleTermDAO) fields.get(fieldName);

    MdWebFormDAOIF form = MdWebFormDAO.get(testForm.getOid());
    MdWebMultipleTermDAO multipleTerm = (MdWebMultipleTermDAO) form.getMdField(fieldName);

    _testField(field, multipleTerm);
  }

  @Request
  @Test
  public void testSingleTermGrid()
  {
    String fieldName = singleGridAttr.definesAttribute();
    MdWebSingleTermGridDAO field = (MdWebSingleTermGridDAO) fields.get(fieldName);

    MdWebFormDAOIF form = MdWebFormDAO.get(testForm.getOid());
    MdWebSingleTermGridDAO singleTermGrid = (MdWebSingleTermGridDAO) form.getMdField(fieldName);

    _testField(field, singleTermGrid);
  }

  // @Request @Test public void testPoint()
  // {
  // String fieldName = pointAttr.definesAttribute();
  // MdWebPointDAO field = (MdWebPointDAO) fields.get(fieldName);
  //
  // MdWebFormDAOIF form = MdWebFormDAO.get(testForm.getOid());
  // MdWebPointDAO point = (MdWebPointDAO) form.getMdField(fieldName);
  //
  // _testField(field, point);
  // }

  /**
   * Tests the character field metadata.
   */
  @Request
  @Test
  public void testCharacter()
  {
    String fieldName = charAttr.definesAttribute();
    MdWebCharacterDAO field = (MdWebCharacterDAO) fields.get(fieldName);

    MdWebFormDAOIF form = MdWebFormDAO.get(testForm.getOid());
    MdWebCharacterDAO charField = (MdWebCharacterDAO) form.getMdField(fieldName);

    _testField(field, charField);

    Assert.assertEquals(field.getDisplayLength(), charField.getDisplayLength());
    Assert.assertEquals(field.getMaxLength(), charField.getMaxLength());
  }

  @Request
  @Test
  public void testText()
  {
    String fieldName = textAttr.definesAttribute();
    MdWebFieldDAO field = (MdWebFieldDAO) fields.get(fieldName);

    MdWebFormDAOIF form = MdWebFormDAO.get(testForm.getOid());
    MdWebFieldDAO charField = (MdWebFieldDAO) form.getMdField(fieldName);

    _testField(field, charField);
  }

  @Request
  @Test
  public void testDate()
  {
    String fieldName = dateAttr.definesAttribute();
    MdWebFieldDAO field = (MdWebFieldDAO) fields.get(fieldName);

    MdWebFormDAOIF form = MdWebFormDAO.get(testForm.getOid());
    MdWebFieldDAO charField = (MdWebFieldDAO) form.getMdField(fieldName);

    _testField(field, charField);
  }

  @Request
  @Test
  public void testDouble()
  {
    String fieldName = doubleAttr.definesAttribute();
    MdWebFieldDAO field = (MdWebFieldDAO) fields.get(fieldName);

    MdWebFormDAOIF form = MdWebFormDAO.get(testForm.getOid());
    MdWebFieldDAO charField = (MdWebFieldDAO) form.getMdField(fieldName);

    _testField(field, charField);
  }

  @Request
  @Test
  public void testFloat()
  {
    String fieldName = floatAttr.definesAttribute();
    MdWebFieldDAO field = (MdWebFieldDAO) fields.get(fieldName);

    MdWebFormDAOIF form = MdWebFormDAO.get(testForm.getOid());
    MdWebFieldDAO charField = (MdWebFieldDAO) form.getMdField(fieldName);

    _testField(field, charField);
  }

  @Request
  @Test
  public void testInteger()
  {
    String fieldName = integerAttr.definesAttribute();
    MdWebFieldDAO field = (MdWebFieldDAO) fields.get(fieldName);

    MdWebFormDAOIF form = MdWebFormDAO.get(testForm.getOid());
    MdWebFieldDAO charField = (MdWebFieldDAO) form.getMdField(fieldName);

    _testField(field, charField);
  }

  @Request
  @Test
  public void testBoolean()
  {
    String fieldName = booleanAttr.definesAttribute();
    MdWebFieldDAO field = (MdWebFieldDAO) fields.get(fieldName);

    MdWebFormDAOIF form = MdWebFormDAO.get(testForm.getOid());
    MdWebFieldDAO charField = (MdWebFieldDAO) form.getMdField(fieldName);

    _testField(field, charField);
  }

}
