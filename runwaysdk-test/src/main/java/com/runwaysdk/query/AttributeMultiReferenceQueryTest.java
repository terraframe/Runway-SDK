/**
*
*/
package com.runwaysdk.query;

import java.util.List;
import java.util.Set;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.junit.Assert;

import com.runwaysdk.business.ontology.MdTermDAO;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.dataaccess.AttributeMultiReferenceIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDecimalDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
 ******************************************************************************/
public class AttributeMultiReferenceQueryTest extends TestCase
{
  private static MdTermDAO                    mdTerm;

  private static MdAttributeBooleanDAO        mdAttributeBoolean;

  private static MdAttributeCharacterDAO      mdAttributeCharacter;

  private static MdAttributeDateDAO           mdAttributeDate;

  private static MdAttributeDateTimeDAO       mdAttributeDateTime;

  private static MdAttributeTimeDAO           mdAttributeTime;

  private static MdAttributeDecimalDAO        mdAttributeDecimal;

  private static MdAttributeDoubleDAO         mdAttributeDouble;

  private static MdAttributeFloatDAO          mdAttributeFloat;

  private static MdAttributeIntegerDAO        mdAttributeInteger;

  private static MdAttributeLongDAO           mdAttributeLong;

  private static MdAttributeTextDAO           mdAttributeText;

  private static MdBusinessDAO                mdBusiness;

  private static MdAttributeMultiReferenceDAO mdAttributeMultiReference;

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
    suite.addTestSuite(AttributeMultiReferenceQueryTest.class);

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
  * 
  */
  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdBusiness);
    TestFixtureFactory.delete(mdTerm);
  }

  /**
  * 
  */
  public static void classSetUp()
  {
    mdTerm = TestFixtureFactory.createMdTerm();
    mdTerm.apply();

    mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdTerm);
    mdAttributeBoolean.apply();

    mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdTerm);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "Default Character");
    mdAttributeCharacter.apply();

    mdAttributeDate = TestFixtureFactory.addDateAttribute(mdTerm, IndexTypes.NO_INDEX);
    mdAttributeDate.apply();

    mdAttributeDateTime = TestFixtureFactory.addDateTimeAttribute(mdTerm);
    mdAttributeDateTime.apply();

    mdAttributeTime = TestFixtureFactory.addTimeAttribute(mdTerm);
    mdAttributeTime.apply();

    mdAttributeDecimal = TestFixtureFactory.addDecimalAttribute(mdTerm);
    mdAttributeDecimal.apply();

    mdAttributeDouble = TestFixtureFactory.addDoubleAttribute(mdTerm);
    mdAttributeDouble.apply();

    mdAttributeFloat = TestFixtureFactory.addFloatAttribute(mdTerm);
    mdAttributeFloat.apply();

    mdAttributeInteger = TestFixtureFactory.addIntegerAttribute(mdTerm);
    mdAttributeInteger.apply();

    mdAttributeLong = TestFixtureFactory.addLongAttribute(mdTerm);
    mdAttributeLong.apply();

    mdAttributeText = TestFixtureFactory.addTextAttribute(mdTerm);
    mdAttributeText.apply();

    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    mdAttributeMultiReference = MdAttributeMultiReferenceDAO.newInstance();
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.NAME, "testMultiReference");
    mdAttributeMultiReference.setStructValue(MdAttributeMultiReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Test");
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.REF_MD_ENTITY, mdTerm.getId());
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttributeMultiReference.apply();
  }

  /*
   * (non-Javadoc)
   * 
   * @see junit.framework.TestCase#tearDown()
   */
  @Override
  protected void tearDown() throws Exception
  {
    mdBusiness.deleteAllRecords();
    mdTerm.deleteAllRecords();
  }

  public void testBasicQueryForOjbectWithAttributeMultiReference()
  {
    BusinessDAO value = this.createTerm();
    this.createBusiness(value);

    QueryFactory factory = new QueryFactory();

    BusinessDAOQuery query = factory.businessDAOQuery(mdBusiness.definesType());
    OIterator<BusinessDAOIF> iterator = query.getIterator();

    Assert.assertTrue(iterator.hasNext());

    BusinessDAOIF test = iterator.next();
    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(value.getId()));
  }

  public void testAttributeMultiReferenceQueryOnCharacter()
  {
    BusinessDAO term = this.createTerm();
    BusinessDAO business = this.createBusiness(term);
    String value = term.getValue(mdAttributeCharacter.definesAttribute());

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).aCharacter(mdAttributeCharacter.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getId(), test.getId());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term.getId()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).aCharacter(mdAttributeCharacter.definesAttribute()).EQ(value + "BAD"));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  public void testAttributeMultiReferenceQueryOnCharacter_2()
  {
    BusinessDAO term = this.createTerm();

    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    business.apply();

    String value = term.getValue(mdAttributeCharacter.definesAttribute());

    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).aCharacter(mdAttributeCharacter.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  public void testAttributeMultiReferenceQueryOnBoolean()
  {
    BusinessDAO term = this.createTerm();
    BusinessDAO business = this.createBusiness(term);
    String value = term.getValue(mdAttributeBoolean.definesAttribute());

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).aBoolean(mdAttributeBoolean.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getId(), test.getId());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term.getId()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).aBoolean(mdAttributeBoolean.definesAttribute()).EQ(MdAttributeBooleanInfo.FALSE));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  public void testAttributeMultiReferenceQueryOnDate()
  {
    BusinessDAO term = this.createTerm();
    BusinessDAO business = this.createBusiness(term);
    String value = term.getValue(mdAttributeDate.definesAttribute());

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).aDate(mdAttributeDate.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getId(), test.getId());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term.getId()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).aDate(mdAttributeDate.definesAttribute()).EQ("2012-07-11"));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  public void testAttributeMultiReferenceQueryOnDateTime()
  {
    BusinessDAO term = this.createTerm();
    BusinessDAO business = this.createBusiness(term);
    String value = term.getValue(mdAttributeDateTime.definesAttribute());

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).aDateTime(mdAttributeDateTime.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getId(), test.getId());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term.getId()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).aDateTime(mdAttributeDateTime.definesAttribute()).EQ("2012-02-11 15:30:30"));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  public void testAttributeMultiReferenceQueryOnTime()
  {
    BusinessDAO term = this.createTerm();
    BusinessDAO business = this.createBusiness(term);
    String value = term.getValue(mdAttributeTime.definesAttribute());

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).aTime(mdAttributeTime.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getId(), test.getId());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term.getId()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).aTime(mdAttributeTime.definesAttribute()).EQ("15:30:30"));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  public void testAttributeMultiReferenceQueryOnDecimal()
  {
    BusinessDAO term = this.createTerm();
    BusinessDAO business = this.createBusiness(term);
    String value = term.getValue(mdAttributeDecimal.definesAttribute());

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).aDecimal(mdAttributeDecimal.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getId(), test.getId());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term.getId()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).aDecimal(mdAttributeDecimal.definesAttribute()).EQ("12.4"));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  public void testAttributeMultiReferenceQueryOnDouble()
  {
    BusinessDAO term = this.createTerm();
    BusinessDAO business = this.createBusiness(term);
    String value = term.getValue(mdAttributeDouble.definesAttribute());

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).aDouble(mdAttributeDouble.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getId(), test.getId());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term.getId()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).aDouble(mdAttributeDouble.definesAttribute()).EQ("12.4"));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  public void testAttributeMultiReferenceQueryOnFloat()
  {
    BusinessDAO term = this.createTerm();
    BusinessDAO business = this.createBusiness(term);
    String value = term.getValue(mdAttributeFloat.definesAttribute());

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).aFloat(mdAttributeFloat.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getId(), test.getId());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term.getId()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).aFloat(mdAttributeFloat.definesAttribute()).EQ("1.4"));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  public void testAttributeMultiReferenceQueryOnInteger()
  {
    BusinessDAO term = this.createTerm();
    BusinessDAO business = this.createBusiness(term);
    String value = term.getValue(mdAttributeInteger.definesAttribute());

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).aInteger(mdAttributeInteger.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getId(), test.getId());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term.getId()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).aInteger(mdAttributeInteger.definesAttribute()).EQ("12"));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  public void testAttributeMultiReferenceQueryOnLong()
  {
    BusinessDAO term = this.createTerm();
    BusinessDAO business = this.createBusiness(term);
    String value = term.getValue(mdAttributeLong.definesAttribute());

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).aLong(mdAttributeLong.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getId(), test.getId());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term.getId()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).aLong(mdAttributeLong.definesAttribute()).EQ("12"));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  public void testAttributeMultiReferenceQueryOnText()
  {
    BusinessDAO term = this.createTerm();
    BusinessDAO business = this.createBusiness(term);
    String value = term.getValue(mdAttributeText.definesAttribute());

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).aText(mdAttributeText.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getId(), test.getId());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term.getId()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).aText(mdAttributeText.definesAttribute()).EQ(value + " Different"));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  public void testAttributeContainsAll()
  {
    BusinessDAO term1 = this.createTerm();
    BusinessDAO term2 = this.createTerm();
    BusinessDAO term3 = this.createTerm();
    BusinessDAO business = this.createBusiness(term2, term3);

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).containsAll(term2.getId(), term3.getId()));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getId(), test.getId());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(2, result.size());
    Assert.assertTrue(result.contains(term2.getId()));
    Assert.assertTrue(result.contains(term3.getId()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).containsAll(term1.getId(), term2.getId()));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  public void testAttributeContainsAny()
  {
    BusinessDAO term1 = this.createTerm();
    BusinessDAO term2 = this.createTerm();
    BusinessDAO business = this.createBusiness(term2);

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).containsAny(term1.getId(), term2.getId()));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getId(), test.getId());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term2.getId()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).containsAny(term1.getId()));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  public void testAttributeContainsExactly()
  {
    BusinessDAO term2 = this.createTerm();
    BusinessDAO term3 = this.createTerm();
    BusinessDAO business = this.createBusiness(term2, term3);

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).containsExactly(term2.getId(), term3.getId()));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getId(), test.getId());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(2, result.size());
    Assert.assertTrue(result.contains(term2.getId()));
    Assert.assertTrue(result.contains(term3.getId()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).containsExactly(term2.getId()));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  public void testAttributeNotContainsAll()
  {
    BusinessDAO term1 = this.createTerm();
    BusinessDAO term2 = this.createTerm();
    BusinessDAO term3 = this.createTerm();
    BusinessDAO business = this.createBusiness(term2, term3);

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).notContainsAll(term1.getId()));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getId(), test.getId());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(2, result.size());
    Assert.assertTrue(result.contains(term2.getId()));
    Assert.assertTrue(result.contains(term3.getId()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).containsAll(term2.getId(), term3.getId()));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  public void testAttributeNotContainsAny()
  {
    BusinessDAO term1 = this.createTerm();
    BusinessDAO term2 = this.createTerm();
    BusinessDAO business = this.createBusiness(term2);

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).notContainsAny(term1.getId(), term2.getId()));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getId(), test.getId());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term2.getId()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(query.aMultiReference(mdAttributeMultiReference.definesAttribute()).notContainsAny(term2.getId()));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  /**
   * @return
   */
  private BusinessDAO createTerm()
  {
    BusinessDAO term = BusinessDAO.newInstance(mdTerm.definesType());
    term.setValue(mdAttributeBoolean.definesAttribute(), MdAttributeBooleanInfo.TRUE);
    term.setValue(mdAttributeCharacter.definesAttribute(), "Test Value");
    term.setValue(mdAttributeDate.definesAttribute(), "2012-02-11");
    term.setValue(mdAttributeDateTime.definesAttribute(), "2012-02-11 12:30:30");
    term.setValue(mdAttributeTime.definesAttribute(), "12:30:30");
    term.setValue(mdAttributeDecimal.definesAttribute(), "12.1");
    term.setValue(mdAttributeDouble.definesAttribute(), "13.2");
    term.setValue(mdAttributeFloat.definesAttribute(), "0");
    term.setValue(mdAttributeInteger.definesAttribute(), "-10");
    term.setValue(mdAttributeLong.definesAttribute(), "20");
    term.setValue(mdAttributeText.definesAttribute(), "Test Text Value");
    term.apply();

    return term;
  }

  /**
   * @param term
   * @return
   */
  private BusinessDAO createBusiness(BusinessDAO... terms)
  {
    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());

    for (BusinessDAO term : terms)
    {
      business.addItem(mdAttributeMultiReference.definesAttribute(), term.getId());
    }

    business.apply();

    return business;
  }

}