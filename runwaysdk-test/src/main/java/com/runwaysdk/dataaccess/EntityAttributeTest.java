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
/*
 * Created on Jun 13, 2005
 */
package com.runwaysdk.dataaccess;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.ProblemException;
import com.runwaysdk.ProblemIF;
import com.runwaysdk.ThreadTransactionCallable;
import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.EntityTypes;
import com.runwaysdk.constants.IndexAttributeInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeClobInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdIndexInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.attributes.AttributeLengthByteException;
import com.runwaysdk.dataaccess.attributes.AttributeLengthCharacterException;
import com.runwaysdk.dataaccess.attributes.AttributeValueCannotBeNegativeProblem;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.attributes.AttributeValueProblem;
import com.runwaysdk.dataaccess.attributes.EmptyValueProblem;
import com.runwaysdk.dataaccess.attributes.ImmutableAttributeProblem;
import com.runwaysdk.dataaccess.attributes.InvalidReferenceException;
import com.runwaysdk.dataaccess.attributes.SystemAttributeProblem;
import com.runwaysdk.dataaccess.attributes.entity.AttributeBlob;
import com.runwaysdk.dataaccess.attributes.entity.AttributeBoolean;
import com.runwaysdk.dataaccess.attributes.entity.AttributeCharacter;
import com.runwaysdk.dataaccess.attributes.entity.AttributeClob;
import com.runwaysdk.dataaccess.attributes.entity.AttributeDate;
import com.runwaysdk.dataaccess.attributes.entity.AttributeDateTime;
import com.runwaysdk.dataaccess.attributes.entity.AttributeDecimal;
import com.runwaysdk.dataaccess.attributes.entity.AttributeDouble;
import com.runwaysdk.dataaccess.attributes.entity.AttributeFloat;
import com.runwaysdk.dataaccess.attributes.entity.AttributeInteger;
import com.runwaysdk.dataaccess.attributes.entity.AttributeLong;
import com.runwaysdk.dataaccess.attributes.entity.AttributeReference;
import com.runwaysdk.dataaccess.attributes.entity.AttributeText;
import com.runwaysdk.dataaccess.attributes.entity.AttributeTime;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.DatabaseException;
import com.runwaysdk.dataaccess.io.XMLImporter;
import com.runwaysdk.dataaccess.metadata.MdAttributeBlobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeClobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDecimalDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeStructDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdIndexDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.MdTreeDAO;
import com.runwaysdk.dataaccess.metadata.NoAttributeOnIndexException;
import com.runwaysdk.dataaccess.transaction.ThreadTransactionState;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.dataaccess.transaction.TransactionType;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

/**
 * J-Unit tests for class Attribute. Adds several attributes to the
 * MasterTestSetup.TEST_CLASS class:
 * <ul>
 * <li>TEST_CHARACTER - varchar(10)
 * <li>TEST_CHAR64 - varchar(64)
 * <li>TEST_INT - int
 * <li>TEST_LONG - bigint
 * <li>TEST_FLOAT - float
 * <li>TEST_DECIMAL - decimal
 * <li>TEST_DOUBLE - double
 * <li>TEST_DATE - date
 * <li>TEST_DATETIME - datetime
 * <li>TEST_REFERENCE_FIELD - varchar(32)
 * <li>TEST_REFERENCE_OBJECT - varchar(32)
 * <li>TEST_BOOLEAN - varchar(5)
 * <li>TEST_IMMUTABLE - varchar(32)
 * </ul>
 * After the tests are completed, these attributes are deleted.
 * 
 * @author Eric G
 */
public class EntityAttributeTest extends TestCase
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
   * <code>testObject</code> is a BusinessDAO that is mapped to a new instance
   * of the MasterTestSetup.TEST_CLASS class for each test. Values are set and
   * tested on it.
   */
  private BusinessDAO                                 testObject;

  /**
   * Contains the MdAttribute for each attribute defined in classSetUp(). Is
   * referenced by the classTearDown() method, where all attribtues are deleted
   */
  private static LinkedList<MdAttributeConcreteDAOIF> definitions;

  private static MdBusinessDAOIF                      testMdBusinessIF;

  private static MdBusinessDAOIF                      referenceMdBusinessIF;

  private static MdTreeDAO                            someTree;

  /**
   * The launch point for the Junit tests.
   * 
   * @param args
   */
  public static void main(String[] args)
  {

    if (DatabaseProperties.getDatabaseClass().equals("hsqldb"))
      XMLImporter.main(new String[] { TestConstants.Path.schema_xsd, TestConstants.Path.metadata_xml });

    junit.textui.TestRunner.run(new EntityMasterTestSetup(EntityAttributeTest.suite()));

  }

  /**
   * A suite() takes <b>this </b> <code>EntityAttributeTest.class</code> and
   * wraps it in <code>MasterTestSetup</code>. The returned class is a suite of
   * all the tests in <code>AttributeTest</code>, with the global setUp() and
   * tearDown() methods from <code>MasterTestSetup</code>.
   * 
   * @return A suite of tests wrapped in global setUp and tearDown methods
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(EntityAttributeTest.class);

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

  public static void classSetUp()
  {
    definitions = new LinkedList<MdAttributeConcreteDAOIF>();

    testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    referenceMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.REFERENCE_CLASS.getType());

    TypeInfo someTreeInfo = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "SomeTree");
    someTree = MdTreeDAO.newInstance();
    someTree.setValue(MdTreeInfo.NAME, someTreeInfo.getTypeName());
    someTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.TRUE);
    someTree.setValue(MdTreeInfo.PACKAGE, someTreeInfo.getPackageName());
    someTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "some tree Relationship");
    someTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    someTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, testMdBusinessIF.getId());
    someTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
    someTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "blah 1");
    someTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, referenceMdBusinessIF.getId());
    someTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
    someTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "blah 2");
    someTree.setValue(MdTreeInfo.PARENT_METHOD, "someParentAccessor");
    someTree.setValue(MdTreeInfo.CHILD_METHOD, "someChildAccessor");
    someTree.setValue(MdElementInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    someTree.apply();

    MdStructDAOIF phoneNumber = MdStructDAO.getMdStructDAO(EntityTypes.PHONE_NUMBER.getType());

    // home phone
    MdAttributeStructDAO mdAttrStruct = MdAttributeStructDAO.newInstance();
    mdAttrStruct.setValue(MdAttributeStructInfo.NAME, "homePhone");
    mdAttrStruct.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "homePhone attr");
    mdAttrStruct.setValue(MdAttributeStructInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrStruct.setValue(MdAttributeStructInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrStruct.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttrStruct.setValue(MdAttributeStructInfo.MD_STRUCT, phoneNumber.getId());
    mdAttrStruct.apply();
    definitions.add(mdAttrStruct);

    // work phone
    mdAttrStruct = MdAttributeStructDAO.newInstance();
    mdAttrStruct.setValue(MdAttributeStructInfo.NAME, "workPhone");
    mdAttrStruct.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "workPhone attr");
    mdAttrStruct.setValue(MdAttributeStructInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrStruct.setValue(MdAttributeStructInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrStruct.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttrStruct.setValue(MdAttributeStructInfo.MD_STRUCT, phoneNumber.getId());
    mdAttrStruct.apply();
    definitions.add(mdAttrStruct);

    // cell phone
    mdAttrStruct = MdAttributeStructDAO.newInstance();
    mdAttrStruct.setValue(MdAttributeStructInfo.NAME, "cellPhone");
    mdAttrStruct.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "cellPhone attr");
    mdAttrStruct.setValue(MdAttributeStructInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrStruct.setValue(MdAttributeStructInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrStruct.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttrStruct.setValue(MdAttributeStructInfo.MD_STRUCT, phoneNumber.getId());
    mdAttrStruct.apply();
    definitions.add(mdAttrStruct);

    MdAttributeTextDAO mdAttributeText = MdAttributeTextDAO.newInstance();
    mdAttributeText.setValue(MdAttributeTextInfo.NAME, "testText");
    mdAttributeText.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Text field");
    mdAttributeText.setValue(MdAttributeTextInfo.DEFAULT_VALUE, "");
    mdAttributeText.setValue(MdAttributeTextInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeText.setValue(MdAttributeTextInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeText.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeText.apply();
    definitions.add(mdAttributeText);

    MdAttributeClobDAO mdAttributeClob = MdAttributeClobDAO.newInstance();
    mdAttributeClob.setValue(MdAttributeClobInfo.NAME, "testClob");
    mdAttributeClob.setStructValue(MdAttributeClobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Clob field");
    mdAttributeClob.setValue(MdAttributeClobInfo.DEFAULT_VALUE, "");
    mdAttributeClob.setValue(MdAttributeClobInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeClob.setValue(MdAttributeClobInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeClob.setValue(MdAttributeClobInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeClob.apply();
    definitions.add(mdAttributeClob);

    // Add an attribute to the reference type
    MdAttributeCharacterDAO mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "refChar");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A string");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "I wish I was a reference field!");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, referenceMdBusinessIF.getId());
    mdAttributeCharacter.apply();

    // Add atributes to the test type
    mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "testCharacter");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "16");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Required Character Length 16");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "Yo diggity");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.addItem(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getId());
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeCharacter.apply();
    definitions.add(mdAttributeCharacter);

    MdAttributeBlobDAO mdAttributeBlob = MdAttributeBlobDAO.newInstance();
    mdAttributeBlob.setValue(MdAttributeBlobInfo.NAME, "testBlob");
    mdAttributeBlob.setStructValue(MdAttributeBlobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Some blob");
    mdAttributeBlob.setValue(MdAttributeBlobInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeBlob.addItem(MdAttributeBlobInfo.INDEX_TYPE, IndexTypes.NO_INDEX.getId());
    mdAttributeBlob.setValue(MdAttributeBlobInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBlob.setValue(MdAttributeBlobInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeBlob.apply();
    definitions.add(mdAttributeBlob);

    mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "testCharacterChangeSize");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "16");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Required Character Length 16, but change size to 32");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "Yo diggity dog");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeCharacter.apply();
    definitions.add(mdAttributeCharacter);

    mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "testChar64");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "64");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Character Length 64");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeCharacter.apply();
    definitions.add(mdAttributeCharacter);

    MdAttributeIntegerDAO mdAttributeInteger = MdAttributeIntegerDAO.newInstance();
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.NAME, "testInteger");
    mdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Integer");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeInteger.apply();
    definitions.add(mdAttributeInteger);

    MdAttributeLongDAO mdAttributeLong = MdAttributeLongDAO.newInstance();
    mdAttributeLong.setValue(MdAttributeLongInfo.NAME, "testLong");
    mdAttributeLong.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Long");
    mdAttributeLong.setValue(MdAttributeLongInfo.DEFAULT_VALUE, "");
    mdAttributeLong.setValue(MdAttributeLongInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeLong.setValue(MdAttributeLongInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeLong.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeLong.apply();
    definitions.add(mdAttributeLong);

    MdAttributeFloatDAO mdAttributeFloat = MdAttributeFloatDAO.newInstance();
    mdAttributeFloat.setValue(MdAttributeFloatInfo.NAME, "testFloat");
    mdAttributeFloat.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Float");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.DEFAULT_VALUE, "");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.LENGTH, "10");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.DECIMAL, "2");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeFloat.apply();
    definitions.add(mdAttributeFloat);

    mdAttributeFloat = MdAttributeFloatDAO.newInstance();
    mdAttributeFloat.setValue(MdAttributeFloatInfo.NAME, "testIndexFloat");
    mdAttributeFloat.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Index Float");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.DEFAULT_VALUE, "");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.LENGTH, "10");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.DECIMAL, "2");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeFloat.apply();
    definitions.add(mdAttributeFloat);

    mdAttributeFloat = MdAttributeFloatDAO.newInstance();
    mdAttributeFloat.setValue(MdAttributeFloatInfo.NAME, "floatBounds");
    mdAttributeFloat.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Float 1");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.DEFAULT_VALUE, "");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.LENGTH, "10");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.DECIMAL, "2");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeFloat.apply();
    definitions.add(mdAttributeFloat);

    MdAttributeDecimalDAO mdAttributeDecimal = MdAttributeDecimalDAO.newInstance();
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.NAME, "testDecimal");
    mdAttributeDecimal.setStructValue(MdAttributeDecimalInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Decimal");
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.DEFAULT_VALUE, "");
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.LENGTH, "13");
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.DECIMAL, "3");
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeDecimal.apply();
    definitions.add(mdAttributeDecimal);

    MdAttributeDoubleDAO mdAttributeDouble = MdAttributeDoubleDAO.newInstance();
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.NAME, "testDouble");
    mdAttributeDouble.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Double");
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.DEFAULT_VALUE, "");
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.LENGTH, "16");
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.DECIMAL, "4");
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeDouble.apply();
    definitions.add(mdAttributeDouble);

    MdAttributeTimeDAO mdAttributeTime = MdAttributeTimeDAO.newInstance();
    mdAttributeTime.setValue(MdAttributeTimeInfo.NAME, "testTime");
    mdAttributeTime.setStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Time (HH:MM:SS)");
    mdAttributeTime.setValue(MdAttributeTimeInfo.DEFAULT_VALUE, "");
    mdAttributeTime.setValue(MdAttributeTimeInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeTime.setValue(MdAttributeTimeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeTime.setValue(MdAttributeTimeInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeTime.apply();
    definitions.add(mdAttributeTime);

    MdAttributeDateDAO mdAttributeDate = MdAttributeDateDAO.newInstance();
    mdAttributeDate.setValue(MdAttributeDateInfo.NAME, "testDate");
    mdAttributeDate.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Date (YYYY-MM-DD)");
    mdAttributeDate.setValue(MdAttributeDateInfo.DEFAULT_VALUE, "");
    mdAttributeDate.setValue(MdAttributeDateInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDate.setValue(MdAttributeDateInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeDate.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeDate.apply();
    definitions.add(mdAttributeDate);

    MdAttributeDateTimeDAO mdAttributeDateTime = MdAttributeDateTimeDAO.newInstance();
    mdAttributeDateTime.setValue(MdAttributeDateTimeInfo.NAME, "testDateTime");
    mdAttributeDateTime.setStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "DateTime (YYYY-MM-DD HH:MM:SS)");
    mdAttributeDateTime.setValue(MdAttributeDateTimeInfo.DEFAULT_VALUE, "");
    mdAttributeDateTime.setValue(MdAttributeDateTimeInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDateTime.setValue(MdAttributeDateTimeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeDateTime.setValue(MdAttributeDateTimeInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeDateTime.apply();
    definitions.add(mdAttributeDateTime);

    MdAttributeReferenceDAO mdAttributeReference = MdAttributeReferenceDAO.newInstance();
    mdAttributeReference.setValue(MdAttributeReferenceInfo.NAME, "testReference");
    mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A reference");
    mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE, "");
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, referenceMdBusinessIF.getId());
    mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeReference.apply();
    definitions.add(mdAttributeReference);

//    // New addition to AttributeReference: the ability to reference MdRelationships (whereas before it was only MdBusiness).
//    MdAttributeReferenceDAO mdAttributeRelationshipReference = MdAttributeReferenceDAO.newInstance();
//    mdAttributeRelationshipReference.setValue(MdAttributeReferenceInfo.NAME, "testRelationshipReference");
//    mdAttributeRelationshipReference.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A reference");
//    mdAttributeRelationshipReference.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE, "");
//    mdAttributeRelationshipReference.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
//    mdAttributeRelationshipReference.setValue(MdAttributeReferenceInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
//    mdAttributeRelationshipReference.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, someTree.getId());
//    mdAttributeRelationshipReference.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
//    mdAttributeRelationshipReference.apply();
//    definitions.add(mdAttributeRelationshipReference);
//    
//    // New addition to MdAttributeReference: the ability to reference MdStructs (whereas before it was only MdBusiness).
//    MdAttributeReferenceDAO mdAttributeStructReference = MdAttributeReferenceDAO.newInstance();
//    mdAttributeStructReference.setValue(MdAttributeReferenceInfo.NAME, "testStructReference");
//    mdAttributeStructReference.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A reference");
//    mdAttributeStructReference.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE, "");
//    mdAttributeStructReference.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
//    mdAttributeStructReference.setValue(MdAttributeReferenceInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
//    mdAttributeStructReference.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, phoneNumber.getId());
//    mdAttributeStructReference.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
//    mdAttributeStructReference.apply();
//    definitions.add(mdAttributeStructReference);
    
    MdAttributeBooleanDAO mdAttributeBoolean = MdAttributeBooleanDAO.newInstance();
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.NAME, "testBoolean");
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Our first Boolean");
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeBoolean.apply();
    definitions.add(mdAttributeBoolean);

    mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "testImmutable");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "An Immutable String");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "You can't change this");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeCharacter.apply();
    definitions.add(mdAttributeCharacter);

    // Finally, add an instance of MasterTestSetup.TEST_CLASS to the
    // database
  }

  public static void classTearDown()
  {
    // Delete all of the attributes created by this class
    BusinessDAO temp = null;
    for (MdAttributeConcreteDAOIF mdAttributeIF : definitions)
    {
      temp = BusinessDAO.get(mdAttributeIF.getId()).getBusinessDAO();
      temp.delete();
    }

    someTree.delete();
  }

  /**
   * Set the testObject to a new Instance of the MasterTestSetup.TEST_CLASS
   * class.
   */
  protected void setUp() throws Exception
  {
    super.setUp();
    testObject = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
  }

  /**
   * If testObject was applied, it is removed from the database.
   * 
   * @see TestCase#tearDown()
   */
  protected void tearDown() throws Exception
  {
    super.tearDown();
    if (!testObject.isNew())
    {
      testObject = BusinessDAO.get(testObject.getId()).getBusinessDAO();
      testObject.delete();
    }
  }

  /**
   * Constructor for AttributeTest.
   * 
   * @param name
   */
  public EntityAttributeTest(String name)
  {
    super(name);
  }

  public void testRollbackSavepointObjectState() throws SQLException
  {
    this.rollbackSavepointObjectState();
    // Object was not applied to DB, therefore it should be deleted.
    if (!testObject.isAppliedToDB())
    {
      testObject.setIsNew(true);
    }
  }

  @Transaction
  private void rollbackSavepointObjectState() throws SQLException
  {
    Savepoint savepoint = Database.setSavepoint();
    Integer savepointId = new Integer(savepoint.getSavepointId());

    try
    {
      assertEquals("Checking the state of the object before the actual test.  The new object should not be applied to the database.", false, testObject.isAppliedToDB());

      testObject.setValue("testCharacter", "A Value");
      testObject.apply();

      assertEquals("Within a savepoint new object was incorrectly marked as not new.", true, testObject.isNew());
      assertEquals("Within a savepoint new object was applied and applied to database should have been set to true.", true, testObject.isAppliedToDB());

      testObject.rollbackState(savepointId);

      assertEquals("Savepoint was rolledback but the object state was incorrectly set to true.", true, testObject.isNew());
      assertEquals("Savepoint was rolledback but the object did not have the applied to database flag changed back to false.", false, testObject.isAppliedToDB());

      Database.rollbackSavepoint(savepoint);
    }
    catch (Throwable e)
    {
      Database.rollbackSavepoint(savepoint);
    }
    finally
    {
      Database.releaseSavepoint(savepoint);
    }
  }

  /**
   * Tests Attribute.validateRequired() where the value is defined but not
   * required
   */
  public void testNotRequired()
  {
    try
    {
      testObject.setValue("testChar64", "A Value");
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests Attribute.validateRequired() where the value is defined but not
   * required
   */
  public void testNotRequired_Thread()
  {
    ThreadTransactionCallable<Object> callable = new ThreadTransactionCallable<Object>()
    {
      private ThreadTransactionState threadTransactionState;

      public void setThreadTransactionState(ThreadTransactionState threadTransactionState)
      {
        this.threadTransactionState = threadTransactionState;
      }

      public Object call()
      {
        request(this.threadTransactionState);
        return null;
      }

      @Request(RequestType.THREAD)
      public void request(ThreadTransactionState threadTransactionState)
      {
        transaction(threadTransactionState);
      }

      @Transaction(TransactionType.THREAD)
      public void transaction(ThreadTransactionState threadTransactionState)
      {
        testObject.setValue("testChar64", "A Value");
      }
    };

    try
    {
      threadTransactionHelper(callable);
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
  }

  /**
   * Tests Attribute.validateRequired() where the value is not defined but not
   * required
   */
  public void testNotRequiredBlankValue()
  {
    try
    {
      testObject.setValue("testChar64", "");
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests Attribute.validateRequired() where the value is not defined but not
   * required
   */
  public void testNotRequiredBlankValue_Thread()
  {
    ThreadTransactionCallable<Object> callable = new ThreadTransactionCallable<Object>()
    {
      private ThreadTransactionState threadTransactionState;

      public void setThreadTransactionState(ThreadTransactionState threadTransactionState)
      {
        this.threadTransactionState = threadTransactionState;
      }

      public Object call()
      {
        request(this.threadTransactionState);
        return null;
      }

      @Request(RequestType.THREAD)
      public void request(ThreadTransactionState threadTransactionState)
      {
        transaction(threadTransactionState);
      }

      @Transaction(TransactionType.THREAD)
      public void transaction(ThreadTransactionState threadTransactionState)
      {
        testObject.setValue("testChar64", "");
      }
    };

    try
    {
      threadTransactionHelper(callable);
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
  }

  /**
   * Tests Attribute.validateRequired() where the value is defined and required
   */
  public void testRequired()
  {
    try
    {
      testObject.setValue("testCharacter", "A Value");
      testObject.apply();
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests Attribute.validateRequired() where the value is defined and required
   */
  public void testRequired_Thread()
  {
    ThreadTransactionCallable<Object> callable = new ThreadTransactionCallable<Object>()
    {
      private ThreadTransactionState threadTransactionState;

      public void setThreadTransactionState(ThreadTransactionState threadTransactionState)
      {
        this.threadTransactionState = threadTransactionState;
      }

      public Object call()
      {
        request(this.threadTransactionState);
        return null;
      }

      @Request(RequestType.THREAD)
      public void request(ThreadTransactionState threadTransactionState)
      {
        transaction(threadTransactionState);
      }

      @Transaction(TransactionType.THREAD)
      public void transaction(ThreadTransactionState threadTransactionState)
      {
        testObject.setValue("testCharacter", "A Value");
        testObject.apply();
      }
    };

    try
    {
      threadTransactionHelper(callable);
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
  }

  /**
   * Tests Attribute.validateRequired() where the value is defined and required
   */
  public void testRequiredValidateMethod()
  {
    try
    {
      testObject.setValue("testCharacter", "A Value");
      testObject.validateAttribute("testCharacter");
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests Attribute.validateRequired() where the value is defined and required
   */
  public void testRequiredValidateMethod_Thread()
  {
    ThreadTransactionCallable<Object> callable = new ThreadTransactionCallable<Object>()
    {
      private ThreadTransactionState threadTransactionState;

      public void setThreadTransactionState(ThreadTransactionState threadTransactionState)
      {
        this.threadTransactionState = threadTransactionState;
      }

      public Object call()
      {
        request(this.threadTransactionState);
        return null;
      }

      @Request(RequestType.THREAD)
      public void request(ThreadTransactionState threadTransactionState)
      {
        transaction(threadTransactionState);
      }

      @Transaction(TransactionType.THREAD)
      public void transaction(ThreadTransactionState threadTransactionState)
      {
        testObject.setValue("testCharacter", "A Value");
        testObject.validateAttribute("testCharacter");
      }
    };

    try
    {
      threadTransactionHelper(callable);
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
  }

  /**
   * Tests Attribute.validateRequired() where the value is required but not
   * defined. This is expected to fail, so the Exception is caught and compared
   * to its expected value.
   */
  public void testRequiredWithBlankValue()
  {
    try
    {
      testObject.setValue("testCharacter", "");
      testObject.apply();
      fail("Attribute.validateRequired() accepted a blank value on a required field.");
    }
    catch (ProblemException e)
    {
      ProblemException problemException = (ProblemException) e;

      if (problemException.getProblems().size() == 1 && problemException.getProblems().get(0) instanceof EmptyValueProblem)
      {
        // Expected to land here
      }
      else
      {
        fail(EmptyValueProblem.class.getName() + " was not thrown.");
      }
    }
  }

  /**
   * Tests Attribute.validateRequired() where the value is required but not
   * defined. This is expected to fail, so the Exception is caught and compared
   * to its expected value.
   */
  public void testRequiredWithBlankValue_Thread()
  {
    ThreadTransactionCallable<Object> callable = new ThreadTransactionCallable<Object>()
    {
      private ThreadTransactionState threadTransactionState;

      public void setThreadTransactionState(ThreadTransactionState threadTransactionState)
      {
        this.threadTransactionState = threadTransactionState;
      }

      public Object call()
      {
        request(this.threadTransactionState);
        return null;
      }

      @Request(RequestType.THREAD)
      public void request(ThreadTransactionState threadTransactionState)
      {
        transaction(threadTransactionState);
      }

      @Transaction(TransactionType.THREAD)
      public void transaction(ThreadTransactionState threadTransactionState)
      {
        testObject.setValue("testCharacter", "");
        testObject.apply();
      }
    };

    try
    {
      threadTransactionHelper(callable);
      fail("Attribute.validateRequired() accepted a blank value on a required field.");
    }
    catch (ProblemException e)
    {
      ProblemException problemException = (ProblemException) e;

      if (problemException.getProblems().size() == 1 && problemException.getProblems().get(0) instanceof EmptyValueProblem)
      {
        // Expected to land here
      }
      else
      {
        fail(EmptyValueProblem.class.getName() + " was not thrown.");
      }
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
  }

  /**
   * Tests Attribute.validateRequired() where the value is required but not
   * defined. This is expected to fail, so the Exception is caught and compared
   * to its expected value.
   */
  public void testRequiredWithBlankValueValidateMethod()
  {
    try
    {
      requiredWithBlankValueValidateMethod();
      fail("Attribute.validateRequired() accepted a blank value on a required field.");
    }
    catch (ProblemException e)
    {
      ProblemException problemException = (ProblemException) e;

      if (problemException.getProblems().size() == 1 && problemException.getProblems().get(0) instanceof EmptyValueProblem)
      {
        // Expected to land here
      }
      else
      {
        fail(EmptyValueProblem.class.getName() + " was not thrown.");
      }
    }
  }

  /**
   * Tests Attribute.validateRequired() where the value is required but not
   * defined. This is expected to fail, so the Exception is caught and compared
   * to its expected value.
   */
  public void testRequiredWithBlankValueValidateMethod_Thread()
  {
    ThreadTransactionCallable<Object> callable = new ThreadTransactionCallable<Object>()
    {
      private ThreadTransactionState threadTransactionState;

      public void setThreadTransactionState(ThreadTransactionState threadTransactionState)
      {
        this.threadTransactionState = threadTransactionState;
      }

      public Object call()
      {
        request(this.threadTransactionState);
        return null;
      }

      @Request(RequestType.THREAD)
      public void request(ThreadTransactionState threadTransactionState)
      {
        transaction(threadTransactionState);
      }

      @Transaction(TransactionType.THREAD)
      public void transaction(ThreadTransactionState threadTransactionState)
      {
        requiredWithBlankValueValidateMethod();
      }
    };

    try
    {
      threadTransactionHelper(callable);
      fail("Attribute.validateRequired() accepted a blank value on a required field.");
    }
    catch (ProblemException e)
    {
      ProblemException problemException = (ProblemException) e;

      if (problemException.getProblems().size() == 1 && problemException.getProblems().get(0) instanceof EmptyValueProblem)
      {
        // Expected to land here
      }
      else
      {
        fail(EmptyValueProblem.class.getName() + " was not thrown.");
      }
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
  }

  @Transaction
  private void requiredWithBlankValueValidateMethod()
  {
    testObject.setValue("testCharacter", "");
    testObject.validateAttribute("testCharacter");
  }

  /**
   * Tests Attribute.validateSystem() by trying to modify a system-only
   * attribute.
   */
  public void testImmutable()
  {
    try
    {
      testObject.apply();
      immutable();
      fail("Attribute.validateMutable() allowed a user to modify an immutable attribute.");
    }
    catch (ProblemException e)
    {
      List<ProblemIF> problemList = ( (ProblemException) e ).getProblems();

      if (problemList.size() == 1 && problemList.get(0) instanceof ImmutableAttributeProblem)
      {
        // This is expected
      }
      else
      {
        fail(e.getMessage());
      }
    }
  }

  /**
   * Tests Attribute.validateSystem() by trying to modify a system-only
   * attribute.
   */
  public void testImmutable_Thread()
  {
    ThreadTransactionCallable<Object> callable = new ThreadTransactionCallable<Object>()
    {
      private ThreadTransactionState threadTransactionState;

      public void setThreadTransactionState(ThreadTransactionState threadTransactionState)
      {
        this.threadTransactionState = threadTransactionState;
      }

      public Object call()
      {
        request(this.threadTransactionState, null);
        return null;
      }

      @Request(RequestType.THREAD)
      public void request(ThreadTransactionState threadTransactionState, Object object)
      {
        // Thread thread =
        // RequestState.getCurrentRequestState().getMainThread();
        // System.out.println("Spawned Thread Request: "+thread);
        transaction(threadTransactionState);
      }

      @Transaction(TransactionType.THREAD)
      public void transaction(ThreadTransactionState threadTransactionState)
      {
        // Thread thread =
        // RequestState.getCurrentRequestState().getMainThread();
        // System.out.println("Spawned Thread Outer Transaction: "+thread);
        immutable();
      }
    };

    try
    {
      // Thread thread = RequestState.getCurrentRequestState().getMainThread();
      // System.out.println("Main Thread: "+thread);

      testObject.apply();
      threadTransactionHelper(callable);
      fail("Attribute.validateMutable() allowed a user to modify an immutable attribute.");
    }
    catch (ProblemException e)
    {
      List<ProblemIF> problemList = ( (ProblemException) e ).getProblems();

      if (problemList.size() == 1 && problemList.get(0) instanceof ImmutableAttributeProblem)
      {
        // Expected to land here
      }
      else
      {
        fail(e.getMessage());
      }
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
  }

  @Transaction
  private void immutable()
  {
    // Thread thread = RequestState.getCurrentRequestState().getMainThread();
    // System.out.println("Spawned Thread Outer Transaction: "+thread);
    testObject.setValue("testImmutable", "Totally mutable");
  }

  /**
   * Tests Attribute.validateMutable() by trying to modify an immutable
   * attribute.
   */
  public void testSystem()
  {
    try
    {
      system();
      fail("Attribute.validateSystem() allowed a user to modify a SYSTEM attribute.");
    }
    catch (ProblemException e)
    {
      List<ProblemIF> problemList = ( (ProblemException) e ).getProblems();

      if (problemList.size() == 1 && problemList.get(0) instanceof SystemAttributeProblem)
      {
        // This is expected
      }
      else
      {
        fail(e.getMessage());
      }
    }
  }

  /**
   * Tests Attribute.validateMutable() by trying to modify an immutable
   * attribute.
   */
  public void testSystem_Thread()
  {
    ThreadTransactionCallable<Object> callable = new ThreadTransactionCallable<Object>()
    {
      private ThreadTransactionState threadTransactionState;

      public void setThreadTransactionState(ThreadTransactionState threadTransactionState)
      {
        this.threadTransactionState = threadTransactionState;
      }

      public Object call()
      {
        request(this.threadTransactionState);
        return null;
      }

      @Request(RequestType.THREAD)
      public void request(ThreadTransactionState threadTransactionState)
      {
        transaction(threadTransactionState);
      }

      @Transaction(TransactionType.THREAD)
      public void transaction(ThreadTransactionState threadTransactionState)
      {
        system();
      }
    };

    try
    {
      threadTransactionHelper(callable);
      fail("Attribute.validateSystem() allowed a user to modify a SYSTEM attribute.");
    }
    catch (ProblemException e)
    {
      List<ProblemIF> problemList = ( (ProblemException) e ).getProblems();

      if (problemList.size() == 1 && problemList.get(0) instanceof SystemAttributeProblem)
      {
        // This is expected
      }
      else
      {
        fail(e.getMessage());
      }
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
  }

  @Transaction
  private void system()
  {
    testObject.setValue(ElementInfo.CREATE_DATE, "2000-01-01 12:00:00");
  }

  /**
   * Thread test helper method.
   */
  @SuppressWarnings("unchecked")
  @Transaction
  public static Object threadTransactionHelper(ThreadTransactionCallable<Object> callable) throws Throwable
  {
    final ThreadTransactionState threadTransactionState = ThreadTransactionState.getCurrentThreadTransactionState();
    callable.setThreadTransactionState(threadTransactionState);

    ExecutorService executor = Executors.newFixedThreadPool(1);

    CompletionService<Object> completionService = new ExecutorCompletionService<Object>(executor);

    Object returnObject = null;

    try
    {
      completionService.submit(callable);

      Future<Object> f = completionService.take();
      try
      {
        returnObject = f.get();
      }
      catch (ExecutionException e)
      {
        Throwable cause = e.getCause();
        throw cause;
      }
    }
    catch (InterruptedException e)
    {
      Thread.currentThread().interrupt();
      throw e;
    }
    finally
    {
      executor.shutdown();
    }

    return returnObject;
  }

  /**
   * Tests Attribute.validateUnique() with a unique value.
   */
  public void testUnique()
  {
    // We test against this instance for uniqueness
    BusinessDAO uniqueObject = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    uniqueObject.setValue("testCharacter", "Not Unique");
    uniqueObject.apply();

    try
    {
      testObject.setValue("testCharacter", "Yabba Dabba Doo");
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
    finally
    {
      uniqueObject.delete();
    }
  }

  /**
   * Tests Attribute.validateUnique() with a non-unique value.
   */
  public void testUniqueFail()
  {
    // We test against this instance for uniqueness
    BusinessDAO uniqueObject = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    uniqueObject.setValue("testCharacter", "Not Unique");
    uniqueObject.apply();

    try
    {
      // We're trying to set a non-unique value to a unique field
      testObject.setValue("testCharacter", "Not Unique");
      testObject.apply();
      fail("Attribute.validateUnique() accepted a non-unique value on a unique field.");
    }
    catch (DuplicateDataException e)
    {
      // This is expected
    }
    finally
    {
      uniqueObject.delete();
    }
  }

  /**
   * Test a group of attributes set to be unique on a class. Two objects do not
   * contain the same values for a set of attributes and should not fail.
   */
  public void testUniqueGroup()
  {
    MdAttributeCharacterDAO mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();

    MdAttributeIntegerDAO mdAttributeInteger = MdAttributeIntegerDAO.newInstance();

    MdIndexDAO mdIndex = MdIndexDAO.newInstance();

    RelationshipDAO relationshipDAO1 = null;

    RelationshipDAO relationshipDAO2 = null;

    uniqueGroupNonTransaction(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2);

    BusinessDAO object1 = null;

    BusinessDAO object2 = null;

    try
    {
      boolean indexExists = Database.groupAttributeIndexExists(testMdBusinessIF.getTableName(), mdIndex.getIndexName());

      if (!indexExists)
      {
        fail("Database index does not exist when it should.");
      }

      object1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
      object1.setValue("testCharacter", "Diggy 1");
      object1.setValue("testCharacterChangeSize", "Something 1");
      object1.setValue("testAttrGroupCharacter0", "CharValue");
      object1.setValue("testAttrGroupInteger0", "100");
      object1.apply();

      object2 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
      object1.setValue("testCharacter", "Diggy 2");
      object1.setValue("testCharacterChangeSize", "Something 2");
      object2.setValue("testAttrGroupCharacter0", "CharValue");
      object2.setValue("testAttrGroupInteger0", "101");
      object2.apply();

    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
    finally
    {
      this.uniqueGroupCleanup(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2, object1, object2);
    }

  }

  /**
   * Test a group of attributes set to be unique on a class. Two objects do not
   * contain the same values for a set of attributes and should not fail.
   */
  public void testUniqueGroupTransaction()
  {
    MdAttributeCharacterDAO mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();

    MdAttributeIntegerDAO mdAttributeInteger = MdAttributeIntegerDAO.newInstance();

    MdIndexDAO mdIndex = MdIndexDAO.newInstance();

    RelationshipDAO relationshipDAO1 = null;

    RelationshipDAO relationshipDAO2 = null;

    uniqueGroupTransaction(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2);

    BusinessDAO object1 = null;

    BusinessDAO object2 = null;

    try
    {
      boolean indexExists = Database.groupAttributeIndexExists(testMdBusinessIF.getTableName(), mdIndex.getIndexName());

      if (!indexExists)
      {
        fail("Database index does not exist when it should.");
      }

      object1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
      object1.setValue("testCharacter", "Diggy 1");
      object1.setValue("testCharacterChangeSize", "Something 1");
      object1.setValue("testAttrGroupCharacter0", "CharValue");
      object1.setValue("testAttrGroupInteger0", "100");
      object1.apply();

      object2 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
      object1.setValue("testCharacter", "Diggy 2");
      object1.setValue("testCharacterChangeSize", "Something 2");
      object2.setValue("testAttrGroupCharacter0", "CharValue");
      object2.setValue("testAttrGroupInteger0", "101");
      object2.apply();

    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
    finally
    {
      this.uniqueGroupCleanup(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2, object1, object2);
    }
  }

  private void uniqueGroupCleanup(MdAttributeCharacterDAO mdAttributeCharacter, MdAttributeIntegerDAO mdAttributeInteger, MdIndexDAO mdIndex, RelationshipDAO relationshipDAO1, RelationshipDAO relationshipDAO2, BusinessDAO object1, BusinessDAO object2)
  {
    mdIndexSetInactiveCleanup(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2);

    if (object1 != null && !object1.isNew())
    {
      object1.delete();
    }
    if (object2 != null && !object2.isNew())
    {
      object2.delete();
    }
  }

  /**
   * 
   * @param mdAttributeCharacter
   * @param mdAttributeInteger
   * @param mdIndex
   */
  private void uniqueGroupNonTransaction(MdAttributeCharacterDAO mdAttributeCharacter, MdAttributeIntegerDAO mdAttributeInteger, MdIndexDAO mdIndex, RelationshipDAO relationshipDAO1, RelationshipDAO relationshipDAO2)
  {
    this.uniqueGroup(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2);
  }

  /**
   * 
   * @param mdAttributeCharacter
   * @param mdAttributeInteger
   * @param mdIndex
   */
  @Transaction
  private void uniqueGroupTransaction(MdAttributeCharacterDAO mdAttributeCharacter, MdAttributeIntegerDAO mdAttributeInteger, MdIndexDAO mdIndex, RelationshipDAO relationshipDAO1, RelationshipDAO relationshipDAO2)
  {
    this.uniqueGroup(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2);
  }

  private void uniqueGroup(MdAttributeCharacterDAO mdAttributeCharacter, MdAttributeIntegerDAO mdAttributeInteger, MdIndexDAO mdIndex, RelationshipDAO relationshipDAO1, RelationshipDAO relationshipDAO2)
  {
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "testAttrGroupCharacter0");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Text Attribute Group Text");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());

    mdAttributeInteger.setValue(MdAttributeIntegerInfo.NAME, "testAttrGroupInteger0");
    mdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Text Attribute Group Integer");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());

    mdAttributeCharacter.apply();

    mdAttributeInteger.apply();

    mdIndex.setValue(MdIndexInfo.MD_ENTITY, testMdBusinessIF.getId());
    mdIndex.setValue(MdIndexInfo.UNIQUE, MdAttributeBooleanInfo.TRUE);
    mdIndex.setStructValue(MdIndexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Index");
    mdIndex.apply();

    relationshipDAO1 = mdIndex.addChild(mdAttributeCharacter, IndexAttributeInfo.CLASS);
    relationshipDAO1.setValue(IndexAttributeInfo.INDEX_ORDER, "0");
    relationshipDAO1.apply();
    relationshipDAO2 = mdIndex.addChild(mdAttributeInteger, IndexAttributeInfo.CLASS);
    relationshipDAO2.setValue(IndexAttributeInfo.INDEX_ORDER, "1");
    relationshipDAO2.apply();

    mdIndex.setValue(MdIndexInfo.ACTIVE, MdAttributeBooleanInfo.TRUE);
    mdIndex.apply();
  }

  /**
   * Test a group of indexed attributes set to be NON unique on a class. This is
   * basically to ensure that SQL code that generates the database indexes does
   * not have a syntax error.
   */
  public void testNonUniqueGroup()
  {
    MdAttributeCharacterDAO mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();

    MdAttributeIntegerDAO mdAttributeInteger = MdAttributeIntegerDAO.newInstance();

    MdIndexDAO mdIndex = MdIndexDAO.newInstance();

    RelationshipDAO relationshipDAO1 = null;

    RelationshipDAO relationshipDAO2 = null;

    this.nonUniqueGroupNonTransaction(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2);

    BusinessDAO object1 = null;

    BusinessDAO object2 = null;

    try
    {

      List<String> columnNameList = Database.getGroupIndexAttributes(testMdBusinessIF.getTableName(), mdIndex.getIndexName());

      boolean containsAttribute1 = false;
      boolean containsAttribute2 = false;
      for (String columnName : columnNameList)
      {
        if (columnName.trim().equalsIgnoreCase("test_attr_group_character1"))
        {
          containsAttribute1 = true;
        }
        else if (columnName.trim().equalsIgnoreCase("test_attr_group_integer1"))
        {
          containsAttribute2 = true;
        }
      }

      if (!containsAttribute1 && !containsAttribute2)
      {
        fail("Non unique attribute group did not contain specified attributes.");
      }

      object1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
      object1.setValue("testCharacter", "Diggy 1");
      object1.setValue("testCharacterChangeSize", "Something 1");
      object1.setValue("testAttrGroupCharacter1", "CharValue");
      object1.setValue("testAttrGroupInteger1", "100");
      object1.apply();

      object2 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
      object1.setValue("testCharacter", "Diggy 2");
      object1.setValue("testCharacterChangeSize", "Something 2");
      object2.setValue("testAttrGroupCharacter1", "CharValue");
      object2.setValue("testAttrGroupInteger1", "101");
      object2.apply();

    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
    finally
    {
      this.uniqueGroupCleanup(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2, object1, object2);
    }
  }

  /**
   * Test a group of indexed attributes set to be NON unique on a class. This is
   * basically to ensure that SQL code that generates the database indexes does
   * not have a syntax error.
   */
  public void testNonUniqueGroupTransaction()
  {
    MdAttributeCharacterDAO mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();

    MdAttributeIntegerDAO mdAttributeInteger = MdAttributeIntegerDAO.newInstance();

    MdIndexDAO mdIndex = MdIndexDAO.newInstance();

    RelationshipDAO relationshipDAO1 = null;

    RelationshipDAO relationshipDAO2 = null;

    this.nonUniqueGroupTransaction(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2);

    String characterColumnName = mdAttributeCharacter.getColumnName();
    String integerColumnName = mdAttributeInteger.getColumnName();

    BusinessDAO object1 = null;

    BusinessDAO object2 = null;

    try
    {

      List<String> columnNameList = Database.getGroupIndexAttributes(testMdBusinessIF.getTableName(), mdIndex.getIndexName());

      boolean containsAttribute1 = false;
      boolean containsAttribute2 = false;
      for (String columnName : columnNameList)
      {
        if (columnName.trim().equalsIgnoreCase(characterColumnName))
        {
          containsAttribute1 = true;
        }
        else if (columnName.trim().equalsIgnoreCase(integerColumnName))
        {
          containsAttribute2 = true;
        }
      }

      if (!containsAttribute1 && !containsAttribute2)
      {
        fail("Non unique attribute group did not contain specified attributes.");
      }

      object1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
      object1.setValue("testCharacter", "Diggy 1");
      object1.setValue("testCharacterChangeSize", "Something 1");
      object1.setValue("testAttrGroupCharacter1", "CharValue");
      object1.setValue("testAttrGroupInteger1", "100");
      object1.apply();

      object2 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
      object1.setValue("testCharacter", "Diggy 2");
      object1.setValue("testCharacterChangeSize", "Something 2");
      object2.setValue("testAttrGroupCharacter1", "CharValue");
      object2.setValue("testAttrGroupInteger1", "101");
      object2.apply();

    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
    finally
    {
      this.uniqueGroupCleanup(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2, object1, object2);
    }
  }

  private void nonUniqueGroupNonTransaction(MdAttributeCharacterDAO mdAttributeCharacter, MdAttributeIntegerDAO mdAttributeInteger, MdIndexDAO mdIndex, RelationshipDAO relationshipDAO1, RelationshipDAO relationshipDAO2)
  {
    this.nonUniqueGroup(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2);
  }

  @Transaction
  private void nonUniqueGroupTransaction(MdAttributeCharacterDAO mdAttributeCharacter, MdAttributeIntegerDAO mdAttributeInteger, MdIndexDAO mdIndex, RelationshipDAO relationshipDAO1, RelationshipDAO relationshipDAO2)
  {
    this.nonUniqueGroup(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2);
  }

  private void nonUniqueGroup(MdAttributeCharacterDAO mdAttributeCharacter, MdAttributeIntegerDAO mdAttributeInteger, MdIndexDAO mdIndex, RelationshipDAO relationshipDAO1, RelationshipDAO relationshipDAO2)
  {
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "testAttrGroupCharacter1");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Text Attribute Group Text");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeCharacter.apply();

    mdAttributeInteger.setValue(MdAttributeIntegerInfo.NAME, "testAttrGroupInteger1");
    mdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Text Attribute Group Integer");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeInteger.apply();

    mdIndex.setValue(MdIndexInfo.MD_ENTITY, testMdBusinessIF.getId());
    mdIndex.setValue(MdIndexInfo.UNIQUE, MdAttributeBooleanInfo.FALSE);
    mdIndex.setStructValue(MdIndexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Index");
    mdIndex.apply();

    relationshipDAO1 = mdIndex.addChild(mdAttributeCharacter, IndexAttributeInfo.CLASS);
    relationshipDAO1.setValue(IndexAttributeInfo.INDEX_ORDER, "0");
    relationshipDAO1.apply();
    relationshipDAO2 = mdIndex.addChild(mdAttributeInteger, IndexAttributeInfo.CLASS);
    relationshipDAO2.setValue(IndexAttributeInfo.INDEX_ORDER, "1");
    relationshipDAO2.apply();

    mdIndex.setValue(MdIndexInfo.ACTIVE, MdAttributeBooleanInfo.TRUE);
    mdIndex.apply();
  }

  /**
   * Test to see if the correct exception is thrown when one tries to activate
   * an index yet no attributes for the index have been defined.
   */
  public void testMdIndexNoAttributes()
  {
    try
    {
      this.mdIndexNoAttributes();

      fail("Able to activate an MdIndex without any attributes.");
    }
    catch (NoAttributeOnIndexException e)
    {
      // this is expected
    }
  }

  /**
   * Test to see if the correct exception is thrown when one tries to activate
   * an index yet no attributes for the index have been defined.
   */
  public void testMdIndexNoAttributesTransaction()
  {
    try
    {
      this.mdIndexNoAttributesTransaction();

      fail("Able to activate an MdIndex without any attributes.");
    }
    catch (NoAttributeOnIndexException e)
    {
      // this is expected
    }
  }

  /**
   * Test to see if the correct exception is thrown when one tries to activate
   * an index yet no attributes for the index have been defined.
   */
  @Transaction
  private void mdIndexNoAttributesTransaction()
  {
    this.mdIndexNoAttributes();
  }

  private void mdIndexNoAttributes()
  {
    MdIndexDAO mdIndex = MdIndexDAO.newInstance();
    mdIndex.setValue(MdIndexInfo.MD_ENTITY, testMdBusinessIF.getId());
    mdIndex.setValue(MdIndexInfo.UNIQUE, MdAttributeBooleanInfo.FALSE);
    mdIndex.setStructValue(MdIndexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Index");
    mdIndex.apply();

    try
    {
      mdIndex.setValue(MdIndexInfo.ACTIVE, MdAttributeBooleanInfo.TRUE);
      mdIndex.apply();
    }
    finally
    {
      if (mdIndex != null && mdIndex.isAppliedToDB())
      {
        mdIndex.delete();
      }
    }

  }

  /**
   * Ensures that a database index is dropped when an MdIndex is set to
   * inactive.
   */
  public void testMdIndexSetInactive()
  {
    MdAttributeCharacterDAO mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();

    MdAttributeIntegerDAO mdAttributeInteger = MdAttributeIntegerDAO.newInstance();

    MdIndexDAO mdIndex = MdIndexDAO.newInstance();

    RelationshipDAO relationshipDAO1 = null;

    RelationshipDAO relationshipDAO2 = null;

    this.mdIndexSetInactiveNonTrasaction(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2);

    try
    {

      boolean indexExists = Database.groupAttributeIndexExists(testMdBusinessIF.getTableName(), mdIndex.getIndexName());

      if (!indexExists)
      {
        fail("Database index does not exist when it should.");
      }

      mdIndex = MdIndexDAO.get(mdIndex.getId()).getBusinessDAO();

      mdIndex.setValue(MdIndexInfo.ACTIVE, MdAttributeBooleanInfo.FALSE);
      mdIndex.apply();

      indexExists = Database.groupAttributeIndexExists(testMdBusinessIF.getTableName(), mdIndex.getIndexName());

      if (indexExists)
      {
        fail("Database index exists when it should not.  The MdIndex set the [" + MdIndexInfo.ACTIVE + "] attribute to [" + MdAttributeBooleanInfo.FALSE + "].");
      }

    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
    finally
    {
      this.mdIndexSetInactiveCleanup(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2);
    }
  }

  private void mdIndexSetInactiveNonTrasaction(MdAttributeCharacterDAO mdAttributeCharacter, MdAttributeIntegerDAO mdAttributeInteger, MdIndexDAO mdIndex, RelationshipDAO relationshipDAO1, RelationshipDAO relationshipDAO2)
  {
    this.mdIndexSetInactive(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2);
  }

  /**
   * Ensures that a database index is dropped when an MdIndex is set to
   * inactive.
   */
  public void testMdIndexSetInactiveTransaction()
  {
    MdAttributeCharacterDAO mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();

    MdAttributeIntegerDAO mdAttributeInteger = MdAttributeIntegerDAO.newInstance();

    MdIndexDAO mdIndex = MdIndexDAO.newInstance();

    RelationshipDAO relationshipDAO1 = null;

    RelationshipDAO relationshipDAO2 = null;

    this.mdIndexSetInactiveTrasaction(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2);

    try
    {

      boolean indexExists = Database.groupAttributeIndexExists(testMdBusinessIF.getTableName(), mdIndex.getIndexName());

      if (!indexExists)
      {
        fail("Database index does not exist when it should.");
      }

      mdIndex = MdIndexDAO.get(mdIndex.getId()).getBusinessDAO();

      mdIndex.setValue(MdIndexInfo.ACTIVE, MdAttributeBooleanInfo.FALSE);
      mdIndex.apply();

      indexExists = Database.groupAttributeIndexExists(testMdBusinessIF.getTableName(), mdIndex.getIndexName());

      if (indexExists)
      {
        fail("Database index exists when it should not.  The MdIndex set the [" + MdIndexInfo.ACTIVE + "] attribute to [" + MdAttributeBooleanInfo.FALSE + "].");
      }

    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
    finally
    {
      this.mdIndexSetInactiveCleanup(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2);
    }
  }

  @Transaction
  private void mdIndexSetInactiveTrasaction(MdAttributeCharacterDAO mdAttributeCharacter, MdAttributeIntegerDAO mdAttributeInteger, MdIndexDAO mdIndex, RelationshipDAO relationshipDAO1, RelationshipDAO relationshipDAO2)
  {
    this.mdIndexSetInactive(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2);
  }

  private void mdIndexSetInactive(MdAttributeCharacterDAO mdAttributeCharacter, MdAttributeIntegerDAO mdAttributeInteger, MdIndexDAO mdIndex, RelationshipDAO relationshipDAO1, RelationshipDAO relationshipDAO2)
  {
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "testAttrGroupCharacter2");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Text Attribute Group Text");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeCharacter.apply();

    mdAttributeInteger.setValue(MdAttributeIntegerInfo.NAME, "testAttrGroupInteger2");
    mdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Text Attribute Group Integer");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeInteger.apply();

    mdIndex.setValue(MdIndexInfo.MD_ENTITY, testMdBusinessIF.getId());
    mdIndex.setValue(MdIndexInfo.UNIQUE, MdAttributeBooleanInfo.FALSE);
    mdIndex.setStructValue(MdIndexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Index");
    mdIndex.apply();

    relationshipDAO1 = mdIndex.addChild(mdAttributeCharacter, IndexAttributeInfo.CLASS);
    relationshipDAO1.setValue(IndexAttributeInfo.INDEX_ORDER, "0");
    relationshipDAO1.apply();
    relationshipDAO2 = mdIndex.addChild(mdAttributeInteger, IndexAttributeInfo.CLASS);
    relationshipDAO2.setValue(IndexAttributeInfo.INDEX_ORDER, "1");
    relationshipDAO2.apply();

    mdIndex.setValue(MdIndexInfo.ACTIVE, MdAttributeBooleanInfo.TRUE);
    mdIndex.apply();
  }

  private void mdIndexSetInactiveCleanup(MdAttributeCharacterDAO mdAttributeCharacter, MdAttributeIntegerDAO mdAttributeInteger, MdIndexDAO mdIndex, RelationshipDAO relationshipDAO1, RelationshipDAO relationshipDAO2)
  {
    if (relationshipDAO1 != null && !relationshipDAO1.isNew())
    {
      relationshipDAO1.delete();
    }
    if (relationshipDAO2 != null && !relationshipDAO2.isNew())
    {
      relationshipDAO2.delete();
    }
    if (mdIndex != null && !mdIndex.isNew())
    {
      mdIndex.delete();
    }

    mdAttributeCharacter = MdAttributeCharacterDAO.get(mdAttributeCharacter.getId()).getBusinessDAO();

    if (mdAttributeCharacter != null && !mdAttributeCharacter.isNew())
    {
      mdAttributeCharacter.delete();
    }

    mdAttributeInteger = MdAttributeIntegerDAO.get(mdAttributeInteger.getId()).getBusinessDAO();

    if (mdAttributeInteger != null && !mdAttributeInteger.isNew())
    {
      mdAttributeInteger.delete();
    }
  }

  /**
   * Test a group of attributes set to be unique on a class. Two objects contain
   * the same values for a set of attributes and should fail.
   */
  public void testUniqueGroupFail()
  {
    MdAttributeCharacterDAO mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();

    MdAttributeIntegerDAO mdAttributeInteger = MdAttributeIntegerDAO.newInstance();

    MdIndexDAO mdIndex = MdIndexDAO.newInstance();

    RelationshipDAO relationshipDAO1 = null;

    RelationshipDAO relationshipDAO2 = null;

    this.uniqueGroupFailNonTransaction(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2);

    BusinessDAO object1 = null;

    BusinessDAO object2 = null;

    try
    {
      object1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
      object1.setValue("testCharacter", "Diggy 1");
      object1.setValue("testCharacterChangeSize", "Something 1");
      object1.setValue("testAttrGroupCharacter3", "CharValue");
      object1.setValue("testAttrGroupInteger3", "100");
      object1.apply();

      object2 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
      object1.setValue("testCharacter", "Diggy 2");
      object1.setValue("testCharacterChangeSize", "Something 2");
      object2.setValue("testAttrGroupCharacter3", "CharValue");
      object2.setValue("testAttrGroupInteger3", "100");
      object2.apply();

      fail("Unique attribute group constraint violation.");

    }
    catch (DuplicateDataException e)
    {
      // This is expected
    }
    finally
    {
      this.uniqueGroupCleanup(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2, object1, object2);
    }
  }

  /**
   * Test a group of attributes set to be unique on a class. Two objects contain
   * the same values for a set of attributes and should fail.
   */
  public void testUniqueGroupFailTransaction()
  {
    MdAttributeCharacterDAO mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();

    MdAttributeIntegerDAO mdAttributeInteger = MdAttributeIntegerDAO.newInstance();

    MdIndexDAO mdIndex = MdIndexDAO.newInstance();

    RelationshipDAO relationshipDAO1 = null;

    RelationshipDAO relationshipDAO2 = null;

    this.uniqueGroupFailTransaction(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2);

    BusinessDAO object1 = null;

    BusinessDAO object2 = null;

    try
    {
      object1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
      object1.setValue("testCharacter", "Diggy 1");
      object1.setValue("testCharacterChangeSize", "Something 1");
      object1.setValue("testAttrGroupCharacter3", "CharValue");
      object1.setValue("testAttrGroupInteger3", "100");
      object1.apply();

      object2 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
      object1.setValue("testCharacter", "Diggy 2");
      object1.setValue("testCharacterChangeSize", "Something 2");
      object2.setValue("testAttrGroupCharacter3", "CharValue");
      object2.setValue("testAttrGroupInteger3", "100");
      object2.apply();

      fail("Unique attribute group constraint violation.");

    }
    catch (DuplicateDataException e)
    {
      // This is expected
    }
    finally
    {
      this.uniqueGroupCleanup(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2, object1, object2);
    }
  }

  private void uniqueGroupFailNonTransaction(MdAttributeCharacterDAO mdAttributeCharacter, MdAttributeIntegerDAO mdAttributeInteger, MdIndexDAO mdIndex, RelationshipDAO relationshipDAO1, RelationshipDAO relationshipDAO2)
  {
    this.uniqueGroupFail(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2);
  }

  @Transaction
  private void uniqueGroupFailTransaction(MdAttributeCharacterDAO mdAttributeCharacter, MdAttributeIntegerDAO mdAttributeInteger, MdIndexDAO mdIndex, RelationshipDAO relationshipDAO1, RelationshipDAO relationshipDAO2)
  {
    this.uniqueGroupFail(mdAttributeCharacter, mdAttributeInteger, mdIndex, relationshipDAO1, relationshipDAO2);
  }

  private void uniqueGroupFail(MdAttributeCharacterDAO mdAttributeCharacter, MdAttributeIntegerDAO mdAttributeInteger, MdIndexDAO mdIndex, RelationshipDAO relationshipDAO1, RelationshipDAO relationshipDAO2)
  {
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "testAttrGroupCharacter3");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Text Attribute Group Text");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());

    mdAttributeInteger.setValue(MdAttributeIntegerInfo.NAME, "testAttrGroupInteger3");
    mdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Text Attribute Group Integer");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());

    mdAttributeCharacter.apply();

    mdAttributeInteger.apply();

    mdIndex.setValue(MdIndexInfo.MD_ENTITY, testMdBusinessIF.getId());
    mdIndex.setValue(MdIndexInfo.UNIQUE, MdAttributeBooleanInfo.TRUE);
    mdIndex.setStructValue(MdIndexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Index");
    mdIndex.apply();

    relationshipDAO1 = mdIndex.addChild(mdAttributeCharacter, IndexAttributeInfo.CLASS);
    relationshipDAO1.setValue(IndexAttributeInfo.INDEX_ORDER, "0");
    relationshipDAO1.apply();
    relationshipDAO2 = mdIndex.addChild(mdAttributeInteger, IndexAttributeInfo.CLASS);
    relationshipDAO2.setValue(IndexAttributeInfo.INDEX_ORDER, "1");
    relationshipDAO2.apply();

    mdIndex.setValue(MdIndexInfo.ACTIVE, MdAttributeBooleanInfo.TRUE);
    mdIndex.apply();
  }

  /**
   * Tests a normal character and then stores->retrieves->compares the values to
   * make sure retrieved value is the same as the input.
   */
  public void testCharacter()
  {
    try
    {
      // set value and store it
      String key = "testCharacter";
      String value = "New Value";
      testObject.setValue(key, value);
      testObject.apply();

      // compare the input and the retrieved value
      BusinessDAOIF appliedObject = BusinessDAO.get(testObject.getId());
      AttributeCharacter retrieved = (AttributeCharacter) appliedObject.getAttributeIF(key);

      if (!retrieved.getValue().equals(value))
      {
        fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests a String that is too long
   */
  public void testCharWithLongString()
  {
    try
    {
      // TEST_CHARACTER has a limit of 10 characters
      testObject.setValue("testCharacter", "This string is too long.");
      fail("Accepted a String that was too large.");
    }
    catch (AttributeLengthCharacterException e)
    {
      // So we expect to catch an AttributeLengthCharacterException
    }
  }

  /**
   * Changes the DB size of an attribute.
   */
  public void testCharChangeSize()
  {
    MdAttributeCharacterDAO mdAttributeCharacter = (MdAttributeCharacterDAO) ( testObject.getAttributeIF("testCharacterChangeSize").getMdAttribute() ).getBusinessDAO();
    try
    {
      // TEST_CHARACTER_CHANGE_SIZE should be able to take an attribute of
      // up to
      // 32 characters.
      mdAttributeCharacter.getAttribute(MdAttributeCharacterInfo.SIZE).setValue("32");
      mdAttributeCharacter.apply();
      // Test with a string of 32 characters
      testObject.setValue("testCharacterChangeSize", "01234567890123456789012345678901");
    }
    catch (Exception e)
    {
      fail(e.getMessage());
      // fail("Attribute testCharacterChangeSize originally was defined to a size of 16.  An attempt to redefine it to 32 failed.");
      return;
    }

    try
    {
      // Test with a string of 33 characters
      testObject.setValue("testCharacterChangeSize", "012345678901234567890123456789012");
      fail("Attribute testCharacterChangeSize originally was redefined to a size of 32, but it accepted a string of size 33.");
    }
    catch (AttributeLengthCharacterException e)
    {
      // This is expected
    }
  }

  /**
   * Tests a normal text and then stores->retrieves->compares the values to make
   * sure retrieved value is the same as the input.
   */
  public void testTextStoreRetrieve()
  {
    try
    {
      // set value and store it
      String key = "testText";
      String value = "This is the testing text, hopefully it works!!!";
      testObject.setValue(key, value);
      testObject.apply();

      // compare the input and the retrieved value
      BusinessDAOIF appliedObject = BusinessDAO.get(testObject.getId());
      AttributeText retrieved = (AttributeText) appliedObject.getAttributeIF(key);

      if (!retrieved.getValue().equals(value))
      {
        fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests a normal CLOB and then stores->retrieves->compares the values to make
   * sure retrieved value is the same as the input.
   */
  public void testClobStoreRetrieve()
  {
    try
    {
      // set value and store it
      String key = "testClob";
      String value = "This is the testing clob text, hopefully it works!!!";
      testObject.setValue(key, value);
      testObject.apply();

      // compare the input and the retrieved value
      BusinessDAOIF appliedObject = BusinessDAO.get(testObject.getId());
      AttributeClob retrieved = (AttributeClob) appliedObject.getAttributeIF(key);

      if (!retrieved.getValue().equals(value))
      {
        fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Creates a normal CLOB, deletes the object, and then fetches the CLOB value
   * from the stale object to ensure a database error is not thrown.
   */
  public void testClobStoreRetrieveAfterDelete()
  {
    try
    {
      // set value and store it
      String key = "testClob";
      String value = "This is the testing clob text, hopefully it works!!!";
      BusinessDAO testClobObject = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
      testClobObject.setValue(key, value);
      testClobObject.apply();

      // now delete the object.
      testClobObject.delete();

      AttributeClob retrieved = (AttributeClob) testClobObject.getAttributeIF(key);
      if (!retrieved.getValue().equals(""))
      {
        fail("Retrieving a CLOB value from the database from an object that was deleted and an empty string was not returned.");
      }
    }
    catch (DatabaseException e)
    {
      fail(e.toString());
    }
  }

  // testObject =
  // BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());

  /**
   * Tests a CLOB by not setting a value, then saving the containing component.
   * The value is then set after applying and tested to make sure it's using the
   * database CLOB bytes and not the cached ones.
   */
  public void testClobAddValuePostApply()
  {
    try
    {
      // set value and store it
      String key = "testClob";
      String value = "This another test of clob text attributes. Hopefully it works!!!";
      ;
      testObject.apply();

      // compare the input and the retrieved value
      BusinessDAOIF appliedObject = BusinessDAO.get(testObject.getId());
      AttributeClob clob = (AttributeClob) appliedObject.getAttributeIF(key);
      clob.setValue(value);
      clob.flushClobCache();// make sure we're not using the cache value.

      MdBusinessDAOIF mdBusinessDAOIF = appliedObject.getMdBusinessDAO();
      MdAttributeConcreteDAOIF mdAttributeDAOIF = mdBusinessDAOIF.definesAttribute(key);

      String databaseValue = Database.getClob(mdBusinessDAOIF.getTableName(), mdAttributeDAOIF.getColumnName(), testObject.getId());

      if (!databaseValue.equals(value))
      {
        fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  /**
   * Tests some text that is too long
   */
  public void testTooLongText()
  {
    try
    {
      StringBuffer longStringBuf = new StringBuffer();
      String stringFrag = "0123456789";
      int maxLoopIterations = ( MdAttributeTextDAO.getMaxLength() / 10 ) + 1;
      for (int i = 0; i < maxLoopIterations; i++)
      {
        longStringBuf.append(stringFrag);
      }
      // TEST_CHARACTER has a limit MdAttributeText.getMaxLength()
      testObject.setValue("testText", longStringBuf.toString());
      fail("Accepted a String that was too large.");
    }
    catch (AttributeLengthCharacterException e)
    {
      // This is expected
    }
  }

  /**
   * Tests a blank (which is acceptable) String
   */
  public void testCharWithEmptyString()
  {
    try
    {
      testObject.setValue("testChar64", "");
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests a normal boolean and then stores->retrieves->compares the values to
   * make sure retrieved value is the same as the input.
   */
  public void testBoolean()
  {
    try
    {
      // set value and store it
      String key = "testBoolean";
      String value = MdAttributeBooleanInfo.TRUE;
      testObject.setValue(key, value);
      testObject.apply();

      // compare the input and the retrieved value
      BusinessDAOIF appliedObject = BusinessDAO.get(testObject.getId());
      AttributeBoolean retrieved = (AttributeBoolean) appliedObject.getAttributeIF(key);

      if (Boolean.parseBoolean(retrieved.getValue()) != Boolean.parseBoolean( ( value )))
      {
        fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests AttributeBoolean.validate() with an invalid value
   * 
   * @throws Exception
   */
  public void testBooleanInvalid() throws Exception
  {
    try
    {
      testObject.setValue("testBoolean", "rawr");
      fail("AttributeBoolean accepted an invalid value.");
    }
    catch (AttributeValueException e)
    {
      // This is expected
    }
    catch (ProblemException e)
    {
      // This is expected
      List<ProblemIF> problems = e.getProblems();
      boolean problemFound = false;
      for (ProblemIF p : problems)
      {
        if (p instanceof AttributeValueProblem)
          problemFound = true;
      }
      if (!problemFound)
        throw new Exception("AttributeValueProblem was not thrown");
    }
  }

  /**
   * Tests a normal integer and then stores->retrieves->compares the values to
   * make sure retrieved value is the same as the input.
   */
  public void testInteger()
  {
    try
    {
      // set value and store it
      String key = "testInteger";
      String value = "12";
      testObject.setValue(key, value);
      testObject.apply();

      // compare the input and the retrieved value
      BusinessDAOIF appliedObject = BusinessDAO.get(testObject.getId());
      AttributeInteger retrieved = (AttributeInteger) appliedObject.getAttributeIF(key);

      if (Integer.parseInt(retrieved.getValue()) != Integer.parseInt( ( value )))
      {
        fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests an integer with an alphabetic String
   */
  public void testIntegerWithAlphas()
  {
    try
    {
      // Try to set an alphabetic string to an int
      testObject.setValue("testInteger", "This isn't a number");
      fail("An integer accepted alpha input");
    }
    catch (AttributeValueException e)
    {
      // We expect to catch an AttributeValueException
    }
  }

  /**
   * Tests an unacceptably large number
   */
  public void testIntegerWithLargeNumber()
  {
    try
    {
      // This number is far too large to fit in an int
      testObject.setValue("testInteger", "2147483648");
      fail("Accepted a number too large to fit in an integer");
    }
    catch (AttributeValueException e)
    {
      // We expect to catch an AttributeValueException
    }
  }

  /**
   * Tests a normal long and then stores->retrieves->compares the values to make
   * sure retrieved value is the same as the input.
   */
  public void testLong()
  {
    try
    {
      // set value and store it
      String key = "testLong";
      String value = "20";
      testObject.setValue(key, value);
      testObject.apply();

      // compare the input and the retrieved value
      BusinessDAOIF appliedObject = BusinessDAO.get(testObject.getId());
      AttributeLong retrieved = (AttributeLong) appliedObject.getAttributeIF(key);

      if (Long.parseLong(retrieved.getValue()) != Long.parseLong( ( value )))
      {
        fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests an alphabetic String
   */
  public void testLongWithAlphas()
  {
    try
    {
      // Try to set an alphabetic string to a long
      testObject.setValue("testLong", "This isn't a number");
      fail("Accepted alpha input into a long");
    }
    catch (DataAccessException e)
    {
      // This is expected
    }
  }

  /**
   * Tests an unacceptably large number
   */
  public void testLongWithLargeNumber()
  {
    try
    {
      // This number is far too large to fit in a long
      testObject.setValue("testLong", "1234567890123456789012345678901234567890");
      fail("Accepted a number too large to fit in a long");
    }
    catch (AttributeValueException e)
    {
      // This is expected
    }
  }

  /**
   * Tests a normal float and then stores->retrieves->compares the values to
   * make sure retrieved value is the same as the input.
   */
  public void testFloat()
  {
    try
    {
      // set value and store it
      String key = "testFloat";
      String value = "4.2";
      testObject.setValue(key, value);
      testObject.apply();

      // compare the input and the retrieved value
      BusinessDAOIF appliedObject = BusinessDAO.get(testObject.getId());
      AttributeFloat retrieved = (AttributeFloat) appliedObject.getAttributeIF(key);

      if (Float.parseFloat(retrieved.getValue()) != Float.parseFloat( ( value )))
      {
        fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests an int cast to a float
   */
  public void testFloatWithInt()
  {
    try
    {
      testObject.setValue("testFloat", "42");
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests a large (exponent) number
   */
  public void testFloatWithExponent()
  {
    try
    {
      testObject.setValue("testFloat", "4.2E-5");
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests a large (but acceptable) number
   */
  public void testFloatWithLong()
  {
    try
    {
      testObject.setValue("testFloat", "123456789012345");
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests an unacceptably large float
   */
  public void testFloatWithLargeNumber()
  {
    try
    {
      // This number is far too large to fit in a float
      testObject.setValue("testFloat", "12345678901234567890123456789012345678901234567890");
      fail("Accepted a number too large to fit in a float");
    }
    catch (AttributeValueException e)
    {
      // We expect to catch an AttributeValueException
    }
  }

  /**
   * Tests a normal double and then stores->retrieves->compares the values to
   * make sure retrieved value is the same as the input.
   */
  public void testDouble()
  {
    try
    {
      // set value and store it
      String key = "testDouble";
      String value = "3.14";
      testObject.setValue(key, value);
      testObject.apply();

      // compare the input and the retrieved value
      BusinessDAOIF appliedObject = BusinessDAO.get(testObject.getId());
      AttributeDouble retrieved = (AttributeDouble) appliedObject.getAttributeIF(key);

      if (Double.parseDouble(retrieved.getValue()) != Double.parseDouble( ( value )))
      {
        fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests an int cast to a double
   */
  public void testDoubleWithInt()
  {
    try
    {
      testObject.setValue("testDouble", "-5");
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests a large (exponent) number into a double
   */
  public void testDoubleWithExponent()
  {
    try
    {
      testObject.setValue("testDouble", "12.3E45");
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests an unacceptably large exponent double
   */
  public void testDoubleWithLargeExponent()
  {
    try
    {
      testObject.setValue("testDouble", "123.456E7890");
      fail("Accepted a number too large to fit in a double");
    }
    catch (AttributeValueException e)
    {
      // This is expected
    }
  }

  /**
   * Tests a normal decimal and then stores->retrieves->compares the values to
   * make sure retrieved value is the same as the input.
   */
  public void testDecimal()
  {
    try
    {
      // set value and store it
      String key = "testDecimal";
      String value = "1.4";
      testObject.setValue(key, value);
      testObject.apply();

      // compare the input and the retrieved value
      BusinessDAOIF appliedObject = BusinessDAO.get(testObject.getId());
      AttributeDecimal retrieved = (AttributeDecimal) appliedObject.getAttributeIF(key);

      if (Double.parseDouble(retrieved.getValue()) != Double.parseDouble( ( value )))
      {
        fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests an int cast to a decimal
   */
  public void testDecimalWithInt()
  {
    try
    {
      testObject.setValue("testDecimal", "-27");
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests a large (exponent) number into a decimal
   */
  public void testDecimalWithExponent()
  {
    try
    {
      testObject.setValue("testDecimal", "12.0E7");
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests a large (exponent) number into a decimal
   */
  public void testDecimalWithTooLargeExponent()
  {
    try
    {
      testObject.setValue("testDecimal", "12.0E10");

      fail("A decimal was set with an exponet that would produce a decimal that is too long.");
    }
    catch (AttributeValueException e)
    {
      // this is expected
    }
  }

  /**
   * Tests a normal time and then stores->retrieves->compares the values to make
   * sure retrieved value is the same as the input.
   */
  public void testTime()
  {
    try
    {
      // set value and store it
      String key = "testTime";
      String value = "08:59:14";
      testObject.setValue(key, value);
      testObject.apply();

      // compare the input and the retrieved value
      BusinessDAOIF appliedObject = BusinessDAO.get(testObject.getId());
      AttributeTime retrieved = (AttributeTime) appliedObject.getAttributeIF(key);
      if (!retrieved.getValue().equals(value))
      {
        fail("The stored database value for " + key + " does not equal the input value.");
      }
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests with a completely invalid time
   */
  public void testTimeInvalid()
  {
    try
    {
      testObject.setValue("testTime", "This isn't a time");
      fail("Attribute accepted an invalid time");
    }
    catch (AttributeValueException e)
    {
      // We expect to catch an AttributeValueException
    }
  }

  /**
   * Tests a time that is too early
   */
  public void testTimeLowerBound()
  {
    try
    {
      testObject.setValue("testTime", "-00:00:01");
      fail("Accepted too early of a time");
    }
    catch (AttributeValueException e)
    {
      // This is expected
    }
  }

  /**
   * Tests a time that is too late
   */
  public void testTimeUpperBound()
  {
    try
    {
      testObject.setValue("testTime", "23:59:60");
      fail("Accepted too late of a time");
    }
    catch (AttributeValueException e)
    {
      // We expect to catch an AttributeValueException
    }
  }

  /**
   * Tests a normal date and then stores->retrieves->compares the values to make
   * sure retrieved value is the same as the input.
   */
  public void testDate()
  {
    try
    {
      // set value and store it
      String key = "testDate";
      String value = "2005-06-15";
      testObject.setValue(key, value);
      testObject.apply();

      // retrieve the applied object and test
      BusinessDAOIF appliedObject = BusinessDAO.get(testObject.getId());
      AttributeDate retrieved = (AttributeDate) appliedObject.getAttributeIF(key);

      if (!retrieved.getValue().equals(value))
      {
        fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests a completely invalid date
   */
  public void testDateInvalid()
  {
    try
    {
      // This is a datetime, not a date
      testObject.setValue("testDate", "2005-12-31 2:15:32");
      fail("Attribute accepted an invalid Date");
    }
    catch (DataAccessException e)
    {
      // This is expected
    }
  }

  /**
   * Tests a date that is too early
   */
  public void testDateLowerBound()
  {
    try
    {
      testObject.setValue("testDate", "1752-01-01");
      fail("Accepted too early of a date");
    }
    catch (AttributeValueException e)
    {
      // We expect to catch an AttributeValueException
    }
  }

  /**
   * Tests a date that is too late
   */
  public void testDateUpperBound()
  {
    try
    {
      testObject.setValue("testDate", "2005-01-32");
      fail("Accepted too late of a date");
    }
    catch (DataAccessException e)
    {
      // This is expected
    }
  }

  /**
   * Tests a normal date and then stores->retrieves->compares the values to make
   * sure retrieved value is the same as the input.
   */
  public void testDateTime()
  {
    try
    {
      // set value and store it
      String key = "testDateTime";
      String value = "2005-06-15 08:59:14";
      testObject.setValue(key, value);
      testObject.apply();

      // retrieve the applied object and test
      BusinessDAOIF appliedObject = BusinessDAO.get(testObject.getId());
      AttributeDateTime retrieved = (AttributeDateTime) appliedObject.getAttributeIF(key);

      if (!retrieved.getValue().equals(value))
      {
        fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests with a completely invalid date
   */
  public void testDateTimeInvalid()
  {
    try
    {
      // This is a date, not a time
      testObject.setValue("testDateTime", "2005-06-15");
      fail("Accepted an invalid String");
    }
    catch (AttributeValueException e)
    {
      // We expect to catch an AttributeValueException
    }
  }

  /**
   * Tests a datetime that is too early
   */
  public void testDateTimeLowerBound()
  {
    try
    {
      testObject.setValue("testDateTime", "1752-01-01 00:00:00");
      fail("Accepted too early of a datetime");
    }
    catch (AttributeValueException e)
    {
      // This is expected
    }
  }

  /**
   * Tests a datetime that is too late
   */
  public void testDateTimeUpperBound()
  {
    try
    {
      testObject.setValue("testDateTime", "2005-13-01 23:59:59");
      fail("Accepted too late of a datetime");
    }
    catch (AttributeValueException e)
    {
      // We expect to catch an AttributeValueException
    }
  }

  /**
   * Ensures that a struct attribute can be added to a class with existing
   * instances.
   * 
   */
  public void testAddStructToClassWithInstances()
  {
    BusinessDAO testObj = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    testObj.apply();
    MdAttributeStructDAO mdAttrStruct = null;
    try
    {
      MdStructDAOIF phoneNumber = MdStructDAO.getMdStructDAO(EntityTypes.PHONE_NUMBER.getType());

      mdAttrStruct = MdAttributeStructDAO.newInstance();
      mdAttrStruct.setValue(MdAttributeStructInfo.NAME, "homePhoneTest");
      mdAttrStruct.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "homePhone attr");
      mdAttrStruct.setValue(MdAttributeStructInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttrStruct.setValue(MdAttributeStructInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdAttrStruct.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
      mdAttrStruct.setValue(MdAttributeStructInfo.MD_STRUCT, phoneNumber.getId());
      mdAttrStruct.apply();

      testObj = BusinessDAO.get(testObj.getId()).getBusinessDAO();
    }
    catch (DataAccessException e)
    {
      fail("Failed to retrieve an object that existed prior to adding a struct attribute.");
    }
    finally
    {

      testObj.delete();

      if (mdAttrStruct != null && mdAttrStruct.isAppliedToDB())
      {
        mdAttrStruct.delete();
      }
    }
  }

  /**
   * Ensures that a struct attribute can be added to a class with existing
   * instances.
   * 
   */
  public void testAddStructToClassWithInstancesAndApply()
  {
    BusinessDAO testObj = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    testObj.apply();
    MdAttributeStructDAO mdAttrStruct = null;
    try
    {
      MdStructDAOIF phoneNumber = MdStructDAO.getMdStructDAO(EntityTypes.PHONE_NUMBER.getType());

      mdAttrStruct = MdAttributeStructDAO.newInstance();
      mdAttrStruct.setValue(MdAttributeStructInfo.NAME, "homePhoneTest");
      mdAttrStruct.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "homePhone attr");
      mdAttrStruct.setValue(MdAttributeStructInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttrStruct.setValue(MdAttributeStructInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdAttrStruct.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
      mdAttrStruct.setValue(MdAttributeStructInfo.MD_STRUCT, phoneNumber.getId());
      mdAttrStruct.apply();

      testObj = BusinessDAO.get(testObj.getId()).getBusinessDAO();

      testObj.setStructValue("homePhoneTest", "areaCode", "303");
      testObj.setStructValue("homePhoneTest", "prefix", "979");
      testObj.setStructValue("homePhoneTest", "suffix", "7745");
      testObj.apply();

      testObj = BusinessDAO.get(testObj.getId()).getBusinessDAO();

      if (!testObj.getStructValue("homePhoneTest", "areaCode").equals("303") || !testObj.getStructValue("homePhoneTest", "prefix").equals("979") || !testObj.getStructValue("homePhoneTest", "suffix").equals("7745"))
      {
        fail("Failed to set values for a struct attribute that was added to a type that already had instances. ");
      }

    }
    catch (DataAccessException e)
    {
      fail("Failed to retrieve an object that existed prior to adding a struct attribute.");
    }
    finally
    {
      testObj.delete();

      if (mdAttrStruct != null && mdAttrStruct.isAppliedToDB())
      {
        mdAttrStruct.delete();
      }
    }
  }

  /**
   * Tests setting a reference.
   */
  public void testReference()
  {
    BusinessDAO reference = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    reference.apply();

    try
    {
      testObject.setValue("testReference", reference.getId());
      assertTrue(testObject.getAttributeIF("testReference") instanceof AttributeReference);
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
    finally
    {
      reference.delete();
    }
  }

  /**
   * Tests setting a reference attribute to an invalid target object.
   */
  public void testSetInvalidReference()
  {
    BusinessDAO reference = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    reference.apply();

    try
    {
      testObject.setValue("testReference", reference.getId());
      testObject.getAttributeIF("testReference");
      fail("AttributeReference accepted a reference to an object of the wrong type.");
    }
    catch (InvalidReferenceException e)
    {
      // This is expected
    }
    finally
    {
      reference.delete();
    }
  }

  /**
   * Tests dereferencing a reference.
   */
  public void testDereferenceReference()
  {
    BusinessDAO reference = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    reference.apply();

    try
    {
      testObject.setValue("testReference", reference.getId());
      testObject.apply();
      AttributeReference fo = (AttributeReference) testObject.getAttributeIF("testReference");
      assertEquals(reference.getId(), testObject.getAttributeIF("testReference").getValue());
      assertEquals(reference.getId(), fo.dereference().getId());
      assertEquals(reference.getAttributeIF("refChar").getValue(), fo.dereference().getAttributeIF("refChar").getValue());
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
    finally
    {
      reference.delete();
    }
  }

  public void testStruct()
  {
    try
    {
      // create a new contact object and its attributes
      MdStructDAOIF phoneNumber = MdStructDAO.getMdStructDAO(EntityTypes.PHONE_NUMBER.getType());
      Map<String, ? extends MdAttributeDAOIF> chunks = phoneNumber.getAllDefinedMdAttributeMap();

      testObject.setStructValue("homePhone", chunks.get("areacode").definesAttribute(), "303");
      testObject.setStructValue("homePhone", chunks.get("prefix").definesAttribute(), "979");
      testObject.setStructValue("homePhone", chunks.get("suffix").definesAttribute(), "7745");

      testObject.setStructValue("cellPhone", chunks.get("areacode").definesAttribute(), "720");
      testObject.setStructValue("cellPhone", chunks.get("prefix").definesAttribute(), "363");
      testObject.setStructValue("cellPhone", chunks.get("suffix").definesAttribute(), "8174");

      testObject.setStructValue("workPhone", chunks.get("areacode").definesAttribute(), "606");
      testObject.setStructValue("workPhone", chunks.get("prefix").definesAttribute(), "980");
      testObject.setStructValue("workPhone", chunks.get("suffix").definesAttribute(), "4370");

      testObject.apply();

      // get the three phone numbers
      if (!testObject.getStructValue("homePhone", "areaCode").equals("303") || !testObject.getStructValue("homePhone", "prefix").equals("979") || !testObject.getStructValue("homePhone", "suffix").equals("7745"))
      {
        fail("Failed to correctly persist and retrieve a struct attribute value.");
      }

      if (!testObject.getStructValue("cellPhone", "areaCode").equals("720") || !testObject.getStructValue("cellPhone", "prefix").equals("363") || !testObject.getStructValue("cellPhone", "suffix").equals("8174"))
      {
        fail("Failed to correctly persist and retrieve a struct attribute value.");
      }

      if (!testObject.getStructValue("workPhone", "areaCode").equals("606") || !testObject.getStructValue("workPhone", "prefix").equals("980") || !testObject.getStructValue("workPhone", "suffix").equals("4370"))
      {
        fail("Failed to correctly persist and retrieve a struct attribute value.");
      }
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Make sure we cannot add an attribute to a class with the same name as an
   * attribute inherited from a supertype.
   */
  public void testDuplicateInheritedAttribute()
  {
    MdBusinessDAO other = null;
    MdAttributeFloatDAO float1DO = null;

    try
    {
      // Create the utensil data type
      TypeInfo otherClass = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "OtherTest");
      other = MdBusinessDAO.newInstance();
      other.setValue(MdBusinessInfo.NAME, otherClass.getTypeName());
      other.setValue(MdBusinessInfo.PACKAGE, otherClass.getPackageName());
      other.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      other.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, otherClass.getTypeName() + " Test Type");
      other.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      other.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      other.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      other.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, testMdBusinessIF.getId());
      other.apply();

      // first declaration of attribute Cost
      TypeInfo floatClass = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "testFloat");
      float1DO = MdAttributeFloatDAO.newInstance();
      float1DO.setValue(MdAttributeFloatInfo.NAME, floatClass.getTypeName());
      float1DO.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Float 1");
      float1DO.setValue(MdAttributeFloatInfo.DEFAULT_VALUE, "");
      float1DO.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      float1DO.setValue(MdAttributeFloatInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      float1DO.setValue(MdAttributeFloatInfo.LENGTH, "10");
      float1DO.setValue(MdAttributeFloatInfo.DECIMAL, "2");
      float1DO.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, other.getId());
      float1DO.apply();

      // if we hit the line below, then our check failed
      fail("A subclass was able to declare an attribute of the same name as held by its superclass.");
    }
    catch (DataAccessException e)
    {
      // we should land here and fail
    }
    finally
    {
      if (other != null && other.isAppliedToDB())
        other.delete();
    }
  }

  /**
   * Test to ensure that a non unique attribute is set and handled properly.
   */
  public void testNonUniqueAttribute()
  {
    try
    {
      // first declaration of attribute Cost
      MdAttributeFloatDAO mdFloatAttr = ( (MdAttributeFloatDAO) testObject.getAttribute("testIndexFloat").getMdAttribute() ).getBusinessDAO();
      mdFloatAttr.setValue(MdAttributeConcreteInfo.INDEX_TYPE, IndexTypes.NON_UNIQUE_INDEX.getId());
      mdFloatAttr.apply();

      // check for correctness
      if (!Database.nonUniqueAttributeExists(testMdBusinessIF.getTableName(), "test_index_float", mdFloatAttr.getIndexName()))
      {
        fail("An attribute with an index of type non unique was not correctly created.");
      }

      // now take away the index
      mdFloatAttr.setValue(MdAttributeConcreteInfo.INDEX_TYPE, IndexTypes.NO_INDEX.getId());
      mdFloatAttr.apply();

      // make sure the change was correctly saved.
      if (Database.nonUniqueAttributeExists(testMdBusinessIF.getTableName(), "test_index_float", mdFloatAttr.getIndexName()))
      {
        fail("An attribute's index of type non unique was not deleted correctly.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  /**
   * A test to make sure that attribute names cannot exceed
   * Constants.MAX_DB_IDENTIFIER_SIZE in length.
   */
  public void testMaxIdentifierSize()
  {
    MdAttributeFloatDAO float1DO = null;

    try
    {
      StringBuffer buffer = new StringBuffer();

      for (int i = 0; i < 65; i++)
      {
        buffer.append("a");
      }
      // first declaration of attribute Cost
      TypeInfo floatClass = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, buffer.toString());
      float1DO = MdAttributeFloatDAO.newInstance();
      float1DO.setValue(MdAttributeFloatInfo.NAME, floatClass.getTypeName());
      float1DO.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Float 1");
      float1DO.setValue(MdAttributeFloatInfo.DEFAULT_VALUE, "");
      float1DO.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
      float1DO.setValue(MdAttributeFloatInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      float1DO.setValue(MdAttributeFloatInfo.LENGTH, "10");
      float1DO.setValue(MdAttributeFloatInfo.DECIMAL, "2");
      float1DO.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
      float1DO.apply();

      fail("An attribute identifier length is too long, but was accepted by the system.");
    }
    catch (AttributeLengthCharacterException e)
    {
      // This is expected
    }
    finally
    {
      if (float1DO != null && float1DO.isAppliedToDB())
        float1DO.delete();
    }
  }

  /**
   * Test to make sure that attributes obey the restrictions on positive, zero,
   * or negative values.
   * 
   * @throws Exception
   */
  public void testRejectedBounds() throws Exception
  {
    try
    {
      // create a new instance w/ attribute value and test
      testObject.setValue("floatBounds", "-1.00");
      testObject.apply();

      fail("An attribute number instance was able to violate the positive, zero, or negative restrictions.");
    }
    catch (ProblemException e)
    {
      // This is expected
      List<ProblemIF> problems = e.getProblems();
      boolean problemFound = false;
      for (ProblemIF p : problems)
      {
        if (p instanceof AttributeValueCannotBeNegativeProblem)
          problemFound = true;
      }
      if (!problemFound)
        throw new Exception("AttributeValueCannotBeNegativeProblem was not thrown");
    }
  }

  /**
   * A reference should only be allowed to reference a class, never an object.
   */
  public void testReferenceReferenceRelationship()
  {
      MdAttributeReferenceDAO mdAttributeReference = MdAttributeReferenceDAO.newInstance();
      mdAttributeReference.setValue(MdAttributeReferenceInfo.NAME, "testReference");
      mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A reference");
      mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE, "");
      mdAttributeReference.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeReference.setValue(MdAttributeReferenceInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdAttributeReference.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, someTree.getId());
      mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
      mdAttributeReference.apply();
  }

  /**
   * Make sure MdAttributeReference only reference BusinessDAOs and not
   * Relationships
   */
  public void testReferenceReference()
  {
    try
    {
      MdAttributeReferenceDAO mdAttributeReference = MdAttributeReferenceDAO.newInstance();
      mdAttributeReference.setValue(MdAttributeReferenceInfo.NAME, "testReference");
      mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A reference");
      mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE, "");
      mdAttributeReference.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeReference.setValue(MdAttributeReferenceInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdAttributeReference.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, someTree.getId());
      mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, testObject.getId());
      mdAttributeReference.apply();

      fail("An " + MdAttributeReferenceDAO.CLASS + " was incorrectly able to reference a relatioship.");
    }
    catch (InvalidReferenceException e)
    {
      // This is expected
    }
  }

  /**
   * Make sure we abort the deletion of a BusinessDAO if it is referenced by a
   * required attribute on another object.
   * 
   */
  public void testDeletedRequiredReference()
  {
    BusinessDAO someTestObject = null;
    BusinessDAO someReference = null;

    MdAttributeReferenceDAO mdAttributeReference = MdAttributeReferenceDAO.newInstance();

    String attributeName = "testRequiredReference";

    try
    {
      mdAttributeReference = MdAttributeReferenceDAO.newInstance();
      mdAttributeReference.setValue(MdAttributeReferenceInfo.NAME, attributeName);
      mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A reference");
      mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE, "");
      mdAttributeReference.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
      mdAttributeReference.setValue(MdAttributeReferenceInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdAttributeReference.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, referenceMdBusinessIF.getId());
      mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
      mdAttributeReference.apply();

      someReference = BusinessDAO.newInstance(referenceMdBusinessIF.definesType());
      someReference.apply();

      someTestObject = BusinessDAO.newInstance(testMdBusinessIF.definesType());

      someTestObject.setValue(attributeName, someReference.getId());
      someTestObject.apply();

      // Delete the object that is referenced by someTestObject.
      someReference.delete();

      fail("An object was deleted that is referenced by a required attribute on another object.");

    }
    catch (CannotDeleteReferencedObject e)
    {
      // This is expected
    }
    finally
    {
      if (!mdAttributeReference.isNew())
      {
        mdAttributeReference.delete();
      }
      if (someTestObject != null && !someTestObject.isNew())
      {
        someTestObject.delete();
      }
      if (someReference != null && !someReference.isNew())
      {
        someReference.delete();
      }
    }

  }

  /**
   * Tests a valid blob using a byte array.
   */
  public void testBlobWithBytes()
  {
    try
    {
      // set value and store it
      String key = "testBlob";
      byte[] value = { 'h', 'e', 'l', 'l', 'o' };
      AttributeBlob blob = (AttributeBlob) testObject.getAttribute(key);
      blob.setBlobAsBytes(value);

      // make sure the cached blob value is correct
      if (!EntityAttributeTest.equalsBytes(value, blob.getBlobAsBytes()))
      {
        fail("The cached value for " + key + " does not equal the input value");
      }
      testObject.apply();

      // compare the input and the retrieved value
      BusinessDAOIF appliedObject = BusinessDAO.get(testObject.getId());
      AttributeBlob blobRetrieved = (AttributeBlob) appliedObject.getAttributeIF(key);

      if (!EntityAttributeTest.equalsBytes(value, blobRetrieved.getBlobAsBytes()))
      {
        fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (Exception e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests a valid blob using a byte array and performs manipulation on the
   * blob. This method won't apply the blob data to the database as it tests the
   * attribute's ability to manipulate data without using the JDBC API.
   */
  public void testBlobManipulationCache()
  {
    try
    {
      // set value and store it
      String key = "testBlob";
      byte[] value = { 'h', 'e', 'l', 'l', 'o' };
      AttributeBlob blob = (AttributeBlob) testObject.getAttribute(key);
      blob.setBlobAsBytes(value);

      // change the blob value
      byte[] newValue = { 'j' };
      blob.setBlobAsBytes(1, newValue, 0, 1);

      // compare to the target bytes
      byte[] target = { 'j', 'e', 'l', 'l', 'o' };

      if (!EntityAttributeTest.equalsBytes(target, blob.getBlobAsBytes()))
      {
        fail("AttributeBlob.setBlobAsBytes() failed to manipulate the blob in cache: first round.");
      }

      // change the blob value again
      byte[] newValue2 = { 'h', 'e', 't', 't', 'o' };
      blob.setBlobAsBytes(3, newValue2, 2, 2);

      // compare to the target2 bytes
      byte[] target2 = { 'j', 'e', 't', 't', 'o' };

      if (!EntityAttributeTest.equalsBytes(target2, blob.getBlobAsBytes()))
      {
        fail("AttributeBlob.setBlobAsBytes() failed to manipulate the blob in cache: second round.");
      }

      // final manipulation to write past the end of the current blob
      byte[] newValue3 = { 'N', 'N', 'N', 'N', 'o', 'n' };
      blob.setBlobAsBytes(5, newValue3, 4, 2);

      // compare to the target3 bytes
      byte[] target3 = { 'j', 'e', 't', 't', 'o', 'n' };

      if (!EntityAttributeTest.equalsBytes(target3, blob.getBlobAsBytes()))
      {
        fail("AttributeBlob.setBlobAsBytes() failed to manipulate the blob in cache: final round.");
      }
    }
    catch (Exception e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests a valid blob using a byte array and performs manipulation on the
   * blob. This method applies the blob data to the database and performs
   * manipulations using the JDBC API.
   */
  public void testBlobManipulationDatabase()
  {
    try
    {
      // set value and store it
      String key = "testBlob";
      byte[] value = { 'h', 'e', 'l', 'l', 'o' };
      AttributeBlob blob = (AttributeBlob) testObject.getAttribute(key);
      blob.setBlobAsBytes(value);
      testObject.apply();

      // change the blob value
      byte[] newValue = { 'j' };
      blob.setBlobAsBytes(1, newValue, 0, 1);

      // compare to the target bytes
      byte[] target = { 'j', 'e', 'l', 'l', 'o' };

      if (!EntityAttributeTest.equalsBytes(target, blob.getBlobAsBytes()))
      {
        fail("AttributeBlob.setBlobAsBytes() failed to manipulate the blob in cache: first round.");
      }

      // change the blob value again
      byte[] newValue2 = { 'h', 'e', 't', 't', 'o' };
      blob.setBlobAsBytes(3, newValue2, 2, 2);

      // compare to the target2 bytes
      byte[] target2 = { 'j', 'e', 't', 't', 'o' };

      if (!EntityAttributeTest.equalsBytes(target2, blob.getBlobAsBytes()))
      {
        fail("AttributeBlob.setBlobAsBytes() failed to manipulate the blob in cache: second round.");
      }

      // final manipulation to write past the end of the current blob
      byte[] newValue3 = { 'N', 'N', 'N', 'N', 'o', 'n' };
      blob.setBlobAsBytes(5, newValue3, 4, 2);

      // compare to the target3 bytes
      byte[] target3 = { 'j', 'e', 't', 't', 'o', 'n' };

      if (!EntityAttributeTest.equalsBytes(target3, blob.getBlobAsBytes()))
      {
        fail("AttributeBlob.setBlobAsBytes() failed to manipulate the blob in cache: final round.");
      }
    }
    catch (Exception e)
    {
      fail(e.toString());
    }
  }

  /**
   * Truncates a blob in the cache and tests to make sure it is successful.
   */
  public void testBlobTruncateCache()
  {
    try
    {
      // set value and store it
      String key = "testBlob";
      byte[] value = { 'h', 'e', 'l', 'l', 'o' };
      AttributeBlob blob = (AttributeBlob) testObject.getAttribute(key);
      blob.setBlobAsBytes(value);
      blob.truncateBlob(3);
      int length = (int) blob.getBlobSize();

      if (length != 3)
      {
        fail("The cached blob value did not get truncated correctly.");
      }
    }
    catch (Exception e)
    {
      fail(e.toString());
    }
  }

  /**
   * Truncates a blob in the database and tests to make sure it is successful.
   */
  public void testBlobTruncateDatabase()
  {
    try
    {
      // set value and store it
      String key = "testBlob";
      byte[] value = { 'h', 'e', 'l', 'l', 'o' };
      AttributeBlob blob = (AttributeBlob) testObject.getAttribute(key);
      blob.setBlobAsBytes(value);
      testObject.apply();
      blob.truncateBlob(3);

      int length = (int) blob.getBlobSize();

      if (length != 3)
      {
        fail("The cached blob value did not get truncated correctly.");
      }
    }
    catch (Exception e)
    {
      fail(e.toString());
    }
  }

  /**
   * Changes the size of a blob to test if the size is truncated if necessary.
   */
  public void testBlobChangeSize()
  {
    try
    {
      // set value and store it
      String key = "testBlob";
      byte[] value = { 'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd' };
      AttributeBlob blob = (AttributeBlob) testObject.getAttribute(key);
      blob.setBlobAsBytes(value);
      testObject.apply();

      byte[] value2 = { 'h', 'e', 'l', 'l', 'o' };
      blob.setBlobAsBytes(value2);
      testObject.apply();

      int length = (int) blob.getBlobSize();
      byte[] rValue = blob.getBlobAsBytes();

      if (length != value2.length || !equalsBytes(rValue, value2))
      {
        fail("The cached blob value did not get truncated correctly when the size changed.");
      }
    }
    catch (Exception e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests a blob by storing bytes in the blob cache and then getting it back
   * and testing for correctness.
   */
  public void testBlobGetBytesCache()
  {
    try
    {
      // set value and store it
      String key = "testBlob";
      byte[] value = { 'h', 'e', 'l', 'l', 'o' };
      AttributeBlob blob = (AttributeBlob) testObject.getAttribute(key);
      blob.setBlobAsBytes(value);

      byte[] allBytes = blob.getBlobAsBytes();

      // test all bytes
      if (value.length != allBytes.length)
      {
        fail("AttributeBlob.getBlobAsBytes() did not return the correct number of cached bytes.");
      }

      for (int i = 0; i < value.length; i++)
      {
        if (value[i] != allBytes[i])
        {
          fail("AttributeBlob.getBlobAsBytes() did not return the correct cached bytes.");
          break;
        }
      }

      // test a sub set of bytes
      byte[] subBytes = blob.getBlobAsBytes(3, 3);
      byte[] target = { 'l', 'l', 'o' };

      if (subBytes.length != target.length)
      {
        fail("AttributeBlob.getBlobAsBytes() did not return the correct number of cached bytes.");
      }

      for (int i = 0; i < target.length; i++)
      {
        if (subBytes[i] != target[i])
        {
          fail("AttributeBlob.getBlobAsBytes() did not return the correct cached bytes.");
          break;
        }
      }
    }
    catch (Exception e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests a blob by storing bytes in the database and then getting it back and
   * testing for correctness.
   */
  public void testBlobGetBytesDatabase()
  {
    try
    {
      // set value and store it
      String key = "testBlob";
      byte[] value = { 'h', 'e', 'l', 'l', 'o' };
      AttributeBlob blob = (AttributeBlob) testObject.getAttribute(key);
      blob.setBlobAsBytes(value);
      testObject.apply();

      // test all bytes
      byte[] allBytes = blob.getBlobAsBytes();

      if (value.length != allBytes.length)
      {
        fail("AttributeBlob.getBlobAsBytes() did not return the correct number of cached bytes.");
      }

      for (int i = 0; i < value.length; i++)
      {
        if (value[i] != allBytes[i])
        {
          fail("AttributeBlob.getBlobAsBytes() did not return the correct cached bytes.");
          break;
        }
      }

      // test a sub set of bytes
      byte[] subBytes = blob.getBlobAsBytes(3, 3);
      byte[] target = { 'l', 'l', 'o' };

      if (subBytes.length != target.length)
      {
        fail("AttributeBlob.getBlobAsBytes() did not return the correct number of cached bytes.");
      }

      for (int i = 0; i < target.length; i++)
      {
        if (subBytes[i] != target[i])
        {
          fail("AttributeBlob.getBlobAsBytes() did not return the correct cached bytes.");
          break;
        }
      }
    }
    catch (Exception e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests a blob by not setting a value, then saving the containing component.
   * The value is then set after applying and tested to make sure it's using the
   * database blob bytes and not the cached ones.
   */
  public void testBlobAddValuePostApply()
  {
    try
    {
      // set value and store it
      String key = "testBlob";
      byte[] value = { 'h', 'e', 'l', 'l', 'o' };
      testObject.apply();

      // compare the input and the retrieved value
      BusinessDAOIF appliedObject = BusinessDAO.get(testObject.getId());
      AttributeBlob blob = (AttributeBlob) appliedObject.getAttributeIF(key);
      blob.setBlobAsBytes(value);
      blob.flushBlobCache();// make sure we're not using the cache value.

      if (!EntityAttributeTest.equalsBytes(value, blob.getBlobAsBytes()))
      {
        fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  /**
   * Tests a blob attribute with a size that is too large.
   */
  public void testBlobInvalidSize()
  {
    try
    {
      // set value and store it
      String key = "testBlob";
      byte[] value = new byte[MdAttributeBlobDAO.getMaxLength() + 1];
      AttributeBlob blob = (AttributeBlob) testObject.getAttribute(key);
      blob.setBlobAsBytes(value);

      fail("An attribute blob was able to exceed the maximum size.");
    }
    catch (AttributeLengthByteException e)
    {
      // This is expected
    }
  }

  /**
   * Performs a byte to byte comparison of the input byte[] and the value of
   * this attributes.
   * 
   * @param bytes
   *          The byte array to compare to the current value of this attribute.
   * @return true if all the bytes are equal, false otherwise.
   */
  public static boolean equalsBytes(byte[] bytes, byte[] bytes2)
  {
    if (bytes.length != bytes2.length)
      return false;

    int index = 0;
    for (byte b : bytes)
    {
      if (b != bytes2[index])
        return false;

      index++;
    }
    return true;
  }
}
