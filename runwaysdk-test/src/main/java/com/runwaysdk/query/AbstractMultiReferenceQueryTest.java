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
/**
*
*/
package com.runwaysdk.query;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.generation.BusinessQueryAPIGenerator;
import com.runwaysdk.business.generation.EntityQueryAPIGenerator;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.MdBusinessInfo;
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
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.Request;

public abstract class AbstractMultiReferenceQueryTest
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

  /**
  * 
  */
  public static void classSetUp(MdAttributeMultiReferenceDAO mdAttribute)
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

    mdAttributeMultiReference = mdAttribute;
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.NAME, "testMultiReference");
    mdAttributeMultiReference.setStructValue(MdAttributeMultiReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Test");
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.REF_MD_ENTITY, mdTerm.getOid());
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.DEFINING_MD_CLASS, mdBusiness.getOid());
    mdAttributeMultiReference.apply();
  }

  /**
  * 
  */
  @Request
  @AfterClass
  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdBusiness);
    TestFixtureFactory.delete(mdTerm);
  }

  protected abstract AttributeMultiReference getQueryAttribute(BusinessDAOQuery query);

  /*
   * (non-Javadoc)
   * 
   * @see junit.framework.TestCase#tearDown()
   */
  @Request
  @After
  public void tearDown() throws Exception
  {
    mdBusiness.deleteAllRecords();
    mdTerm.deleteAllRecords();
  }

  /**
   * @return the mdAttributeMultiReference
   */
  public MdAttributeMultiReferenceDAO getMdAttribute()
  {
    return mdAttributeMultiReference;
  }

  @Request
  @Test
  public void testBasicQueryForOjbectWithAttributeMultiReference()
  {
    BusinessDAO value = this.createTerm();
    this.createBusiness(value);

    QueryFactory factory = new QueryFactory();

    BusinessDAOQuery query = factory.businessDAOQuery(mdBusiness.definesType());
    OIterator<BusinessDAOIF> iterator = query.getIterator();

    Assert.assertTrue(iterator.hasNext());

    BusinessDAOIF test = iterator.next();
    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(value.getOid()));
  }

  @Request
  @Test
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
    query.WHERE(this.getQueryAttribute(query).aCharacter(mdAttributeCharacter.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getOid(), test.getOid());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term.getOid()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(this.getQueryAttribute(query).aCharacter(mdAttributeCharacter.definesAttribute()).EQ(value + "BAD"));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  @Request
  @Test
  public void testAttributeMultiReferenceQueryOnCharacter_2()
  {
    BusinessDAO term = this.createTerm();

    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    business.apply();

    String value = term.getValue(mdAttributeCharacter.definesAttribute());

    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(this.getQueryAttribute(query).aCharacter(mdAttributeCharacter.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  @Request
  @Test
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
    query.WHERE(this.getQueryAttribute(query).aBoolean(mdAttributeBoolean.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getOid(), test.getOid());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term.getOid()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(this.getQueryAttribute(query).aBoolean(mdAttributeBoolean.definesAttribute()).EQ(MdAttributeBooleanInfo.FALSE));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  @Request
  @Test
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
    query.WHERE(this.getQueryAttribute(query).aDate(mdAttributeDate.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getOid(), test.getOid());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term.getOid()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(this.getQueryAttribute(query).aDate(mdAttributeDate.definesAttribute()).EQ("2012-07-11"));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  @Request
  @Test
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
    query.WHERE(this.getQueryAttribute(query).aDateTime(mdAttributeDateTime.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getOid(), test.getOid());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term.getOid()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(this.getQueryAttribute(query).aDateTime(mdAttributeDateTime.definesAttribute()).EQ("2012-02-11 15:30:30"));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  @Request
  @Test
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
    query.WHERE(this.getQueryAttribute(query).aTime(mdAttributeTime.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getOid(), test.getOid());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term.getOid()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(this.getQueryAttribute(query).aTime(mdAttributeTime.definesAttribute()).EQ("15:30:30"));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  @Request
  @Test
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
    query.WHERE(this.getQueryAttribute(query).aDecimal(mdAttributeDecimal.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getOid(), test.getOid());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term.getOid()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(this.getQueryAttribute(query).aDecimal(mdAttributeDecimal.definesAttribute()).EQ("12.4"));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  @Request
  @Test
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
    query.WHERE(this.getQueryAttribute(query).aDouble(mdAttributeDouble.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getOid(), test.getOid());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term.getOid()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(this.getQueryAttribute(query).aDouble(mdAttributeDouble.definesAttribute()).EQ("12.4"));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  @Request
  @Test
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
    query.WHERE(this.getQueryAttribute(query).aFloat(mdAttributeFloat.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getOid(), test.getOid());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term.getOid()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(this.getQueryAttribute(query).aFloat(mdAttributeFloat.definesAttribute()).EQ("1.4"));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  @Request
  @Test
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
    query.WHERE(this.getQueryAttribute(query).aInteger(mdAttributeInteger.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getOid(), test.getOid());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term.getOid()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(this.getQueryAttribute(query).aInteger(mdAttributeInteger.definesAttribute()).EQ("12"));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  @Request
  @Test
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
    query.WHERE(this.getQueryAttribute(query).aLong(mdAttributeLong.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getOid(), test.getOid());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term.getOid()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(this.getQueryAttribute(query).aLong(mdAttributeLong.definesAttribute()).EQ("12"));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  @Request
  @Test
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
    query.WHERE(this.getQueryAttribute(query).aText(mdAttributeText.definesAttribute()).EQ(value));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getOid(), test.getOid());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term.getOid()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(this.getQueryAttribute(query).aText(mdAttributeText.definesAttribute()).EQ(value + " Different"));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  @Request
  @Test
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
    query.WHERE(this.getQueryAttribute(query).containsAll(term2.getOid(), term3.getOid()));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getOid(), test.getOid());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(2, result.size());
    Assert.assertTrue(result.contains(term2.getOid()));
    Assert.assertTrue(result.contains(term3.getOid()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(this.getQueryAttribute(query).containsAll(term1.getOid(), term2.getOid()));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  @Request
  @Test
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
    query.WHERE(this.getQueryAttribute(query).containsAny(term1.getOid(), term2.getOid()));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getOid(), test.getOid());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term2.getOid()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(this.getQueryAttribute(query).containsAny(term1.getOid()));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  @Request
  @Test
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
    query.WHERE(this.getQueryAttribute(query).containsExactly(term2.getOid(), term3.getOid()));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getOid(), test.getOid());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(2, result.size());
    Assert.assertTrue(result.contains(term2.getOid()));
    Assert.assertTrue(result.contains(term3.getOid()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(this.getQueryAttribute(query).containsExactly(term2.getOid()));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  @Request
  @Test
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
    query.WHERE(this.getQueryAttribute(query).notContainsAll(term1.getOid()));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertTrue(results.size() > 0);

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getOid(), test.getOid());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(2, result.size());
    Assert.assertTrue(result.contains(term2.getOid()));
    Assert.assertTrue(result.contains(term3.getOid()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(this.getQueryAttribute(query).notContainsAll(term2.getOid(), term3.getOid()));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  @Request
  @Test
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
    query.WHERE(this.getQueryAttribute(query).notContainsAny(term1.getOid()));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertTrue(results.size() > 0);

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business.getOid(), test.getOid());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term2.getOid()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(this.getQueryAttribute(query).notContainsAny(term2.getOid()));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  @Request
  @Test
  public void testEQ()
  {
    BusinessDAO term1 = this.createTerm();
    BusinessDAO business1 = this.createBusiness(term1);

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(this.getQueryAttribute(query).EQ(business1.getValue(this.getMdAttribute().definesAttribute())));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business1.getOid(), test.getOid());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term1.getOid()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(this.getQueryAttribute(query).EQ("BOGUS"));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  @Request
  @Test
  public void testNE()
  {
    BusinessDAO term1 = this.createTerm();
    BusinessDAO business1 = this.createBusiness(term1);

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(this.getQueryAttribute(query).NE("BOGUS"));

    List<BusinessDAOIF> results = query.getIterator().getAll();

    Assert.assertEquals(1, results.size());

    BusinessDAOIF test = results.get(0);

    Assert.assertEquals(business1.getOid(), test.getOid());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertTrue(result.contains(term1.getOid()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = factory.businessDAOQuery(mdBusiness.definesType());
    query.WHERE(this.getQueryAttribute(query).NE(business1.getValue(this.getMdAttribute().definesAttribute())));

    results = query.getIterator().getAll();

    Assert.assertEquals(0, results.size());
  }

  /*
   * Generated AttributeMultiReference query test
   */

  @Request
  @Test
  public void testBasicQueryForOjbectWithAttributeMultiReference_Generated() throws Exception
  {
    BusinessDAO value = this.createTerm();
    this.createBusiness(value);

    String queryType = EntityQueryAPIGenerator.getQueryClass(mdBusiness.definesType());
    Class<?> queryClass = LoaderDecorator.load(queryType);

    QueryFactory factory = new QueryFactory();
    Object query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

    // Load the iterator class
    OIterator<?> iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);

    Assert.assertTrue(iterator.hasNext());

    Business test = (Business) iterator.next();
    List<? extends Business> results = test.getMultiItems(this.getMdAttribute().definesAttribute());

    Assert.assertEquals(1, results.size());
    Assert.assertTrue(this.contains(results, value.getOid()));
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testAttributeMultiReferenceQueryOnCharacter_Generated() throws Exception
  {
    BusinessDAO term = this.createTerm();
    BusinessDAO business = this.createBusiness(term);
    String value = term.getValue(mdAttributeCharacter.definesAttribute());

    String queryType = EntityQueryAPIGenerator.getQueryClass(mdBusiness.definesType());
    Class<?> queryClass = LoaderDecorator.load(queryType);

    String termQueryType = BusinessQueryAPIGenerator.getMultiReferenceInterfaceCompiled(mdTerm);
    Class<?> termQueryClass = LoaderDecorator.load(termQueryType);

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    Object query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    SelectableMultiReference attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    SelectableChar attributeCharacter = (SelectableChar) termQueryClass.getMethod("getTestCharacter").invoke(attributeMultiReference);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, attributeCharacter.EQ(value));

    // Load the iterator class
    OIterator<?> iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);

    Assert.assertTrue(iterator.hasNext());

    Business test = (Business) iterator.next();
    Assert.assertEquals(business.getOid(), test.getOid());

    List<? extends Business> results = test.getMultiItems(this.getMdAttribute().definesAttribute());

    Assert.assertEquals(1, results.size());
    Assert.assertTrue(this.contains(results, term.getOid()));

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    attributeCharacter = (SelectableChar) termQueryClass.getMethod("getTestCharacter").invoke(attributeMultiReference);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, attributeCharacter.EQ(value + "BAD"));

    // Load the iterator class
    iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(0, results.size());
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testAttributeMultiReferenceQueryOnBoolean_Generated() throws Exception
  {
    BusinessDAO term = this.createTerm();
    BusinessDAO business = this.createBusiness(term);
    String value = term.getValue(mdAttributeBoolean.definesAttribute());

    String queryType = EntityQueryAPIGenerator.getQueryClass(mdBusiness.definesType());
    Class<?> queryClass = LoaderDecorator.load(queryType);

    String termQueryType = BusinessQueryAPIGenerator.getMultiReferenceInterfaceCompiled(mdTerm);
    Class<?> termQueryClass = LoaderDecorator.load(termQueryType);

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    Object query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    SelectableMultiReference attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    SelectableBoolean attributeBoolean = (SelectableBoolean) termQueryClass.getMethod("getTestBoolean").invoke(attributeMultiReference);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, attributeBoolean.EQ(value));

    OIterator<?> iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    List<? extends Business> results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(1, results.size());

    Business test = results.get(0);

    Assert.assertEquals(business.getOid(), test.getOid());

    /*
     * Test a failure case
     */
    factory = new QueryFactory();
    query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    attributeBoolean = (SelectableBoolean) termQueryClass.getMethod("getTestBoolean").invoke(attributeMultiReference);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, attributeBoolean.EQ(MdAttributeBooleanInfo.FALSE));

    iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(0, results.size());
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testAttributeMultiReferenceQueryOnDate_Generated() throws Exception
  {
    BusinessDAO term = this.createTerm();
    BusinessDAO business = this.createBusiness(term);
    Date value = new SimpleDateFormat(Constants.DATE_FORMAT).parse(term.getValue(mdAttributeDate.definesAttribute()));

    String queryType = EntityQueryAPIGenerator.getQueryClass(mdBusiness.definesType());
    Class<?> queryClass = LoaderDecorator.load(queryType);

    String termQueryType = BusinessQueryAPIGenerator.getMultiReferenceInterfaceCompiled(mdTerm);
    Class<?> termQueryClass = LoaderDecorator.load(termQueryType);

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    Object query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    SelectableMultiReference attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    SelectableMoment attributeDate = (SelectableMoment) termQueryClass.getMethod("getTestDate").invoke(attributeMultiReference);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, attributeDate.EQ(value));

    OIterator<?> iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    List<? extends Business> results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(1, results.size());
    Assert.assertEquals(business.getOid(), results.get(0).getOid());

    /*
     * Test a failure case
     */
    value = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2012-07-11");
    factory = new QueryFactory();
    query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    attributeDate = (SelectableMoment) termQueryClass.getMethod("getTestDate").invoke(attributeMultiReference);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, attributeDate.EQ(value));

    iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(0, results.size());
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testAttributeMultiReferenceQueryOnDateTime_Generated() throws Exception
  {
    BusinessDAO term = this.createTerm();
    BusinessDAO business = this.createBusiness(term);
    Date value = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse(term.getValue(mdAttributeDateTime.definesAttribute()));

    String queryType = EntityQueryAPIGenerator.getQueryClass(mdBusiness.definesType());
    Class<?> queryClass = LoaderDecorator.load(queryType);

    String termQueryType = BusinessQueryAPIGenerator.getMultiReferenceInterfaceCompiled(mdTerm);
    Class<?> termQueryClass = LoaderDecorator.load(termQueryType);

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    Object query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    SelectableMultiReference attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    SelectableMoment attributeDateTime = (SelectableMoment) termQueryClass.getMethod("getTestDateTime").invoke(attributeMultiReference);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, attributeDateTime.EQ(value));

    OIterator<?> iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    List<? extends Business> results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(1, results.size());
    Assert.assertEquals(business.getOid(), results.get(0).getOid());

    /*
     * Test a failure case
     */
    value = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2012-02-11 15:30:30");
    factory = new QueryFactory();
    query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    attributeDateTime = (SelectableMoment) termQueryClass.getMethod("getTestDateTime").invoke(attributeMultiReference);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, attributeDateTime.EQ(value));

    iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(0, results.size());
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testAttributeMultiReferenceQueryOnTime_Generated() throws Exception
  {
    BusinessDAO term = this.createTerm();
    BusinessDAO business = this.createBusiness(term);
    Date value = new SimpleDateFormat(Constants.TIME_FORMAT).parse(term.getValue(mdAttributeTime.definesAttribute()));

    String queryType = EntityQueryAPIGenerator.getQueryClass(mdBusiness.definesType());
    Class<?> queryClass = LoaderDecorator.load(queryType);

    String termQueryType = BusinessQueryAPIGenerator.getMultiReferenceInterfaceCompiled(mdTerm);
    Class<?> termQueryClass = LoaderDecorator.load(termQueryType);

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    Object query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    SelectableMultiReference attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    SelectableMoment attributeTime = (SelectableMoment) termQueryClass.getMethod("getTestTime").invoke(attributeMultiReference);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, attributeTime.EQ(value));

    OIterator<?> iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    List<? extends Business> results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(1, results.size());
    Assert.assertEquals(business.getOid(), results.get(0).getOid());

    /*
     * Test a failure case
     */
    value = new SimpleDateFormat(Constants.TIME_FORMAT).parse("15:30:30");
    factory = new QueryFactory();
    query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    attributeTime = (SelectableMoment) termQueryClass.getMethod("getTestTime").invoke(attributeMultiReference);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, attributeTime.EQ(value));

    iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(0, results.size());

  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testAttributeMultiReferenceQueryOnDecimal_Generated() throws Exception
  {
    BusinessDAO term = this.createTerm();
    BusinessDAO business = this.createBusiness(term);
    BigDecimal value = new BigDecimal(term.getValue(mdAttributeDecimal.definesAttribute()));

    String queryType = EntityQueryAPIGenerator.getQueryClass(mdBusiness.definesType());
    Class<?> queryClass = LoaderDecorator.load(queryType);

    String termQueryType = BusinessQueryAPIGenerator.getMultiReferenceInterfaceCompiled(mdTerm);
    Class<?> termQueryClass = LoaderDecorator.load(termQueryType);

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    Object query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    SelectableMultiReference attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    SelectableDecimal attributeDecimal = (SelectableDecimal) termQueryClass.getMethod("getTestDecimal").invoke(attributeMultiReference);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, attributeDecimal.EQ(value));

    OIterator<?> iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    List<? extends Business> results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(1, results.size());
    Assert.assertEquals(business.getOid(), results.get(0).getOid());

    /*
     * Test a failure case
     */
    value = new BigDecimal("65.1");
    factory = new QueryFactory();
    query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    attributeDecimal = (SelectableDecimal) termQueryClass.getMethod("getTestDecimal").invoke(attributeMultiReference);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, attributeDecimal.EQ(value));

    iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(0, results.size());
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testAttributeMultiReferenceQueryOnDouble_Generated() throws Exception
  {
    BusinessDAO term = this.createTerm();
    BusinessDAO business = this.createBusiness(term);
    Double value = Double.valueOf(term.getValue(mdAttributeDouble.definesAttribute()));

    String queryType = EntityQueryAPIGenerator.getQueryClass(mdBusiness.definesType());
    Class<?> queryClass = LoaderDecorator.load(queryType);

    String termQueryType = BusinessQueryAPIGenerator.getMultiReferenceInterfaceCompiled(mdTerm);
    Class<?> termQueryClass = LoaderDecorator.load(termQueryType);

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    Object query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    SelectableMultiReference attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    SelectableDouble attributeDouble = (SelectableDouble) termQueryClass.getMethod("getTestDouble").invoke(attributeMultiReference);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, attributeDouble.EQ(value));

    OIterator<?> iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    List<? extends Business> results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(1, results.size());
    Assert.assertEquals(business.getOid(), results.get(0).getOid());

    /*
     * Test a failure case
     */
    value = Double.valueOf("65.1");
    factory = new QueryFactory();
    query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    attributeDouble = (SelectableDouble) termQueryClass.getMethod("getTestDouble").invoke(attributeMultiReference);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, attributeDouble.EQ(value));

    iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(0, results.size());
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testAttributeMultiReferenceQueryOnFloat_Generated() throws Exception
  {
    BusinessDAO term = this.createTerm();
    BusinessDAO business = this.createBusiness(term);
    Float value = new Float(term.getValue(mdAttributeFloat.definesAttribute()));

    String queryType = EntityQueryAPIGenerator.getQueryClass(mdBusiness.definesType());
    Class<?> queryClass = LoaderDecorator.load(queryType);

    String termQueryType = BusinessQueryAPIGenerator.getMultiReferenceInterfaceCompiled(mdTerm);
    Class<?> termQueryClass = LoaderDecorator.load(termQueryType);

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    Object query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    SelectableMultiReference attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    SelectableFloat attributeFloat = (SelectableFloat) termQueryClass.getMethod("getTestFloat").invoke(attributeMultiReference);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, attributeFloat.EQ(value));

    OIterator<?> iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    List<? extends Business> results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(1, results.size());
    Assert.assertEquals(business.getOid(), results.get(0).getOid());

    /*
     * Test a failure case
     */
    value = new Float("65.1");
    factory = new QueryFactory();
    query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    attributeFloat = (SelectableFloat) termQueryClass.getMethod("getTestFloat").invoke(attributeMultiReference);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, attributeFloat.EQ(value));

    iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(0, results.size());
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testAttributeMultiReferenceQueryOnInteger_Generated() throws Exception
  {
    BusinessDAO term = this.createTerm();
    BusinessDAO business = this.createBusiness(term);
    Integer value = Integer.valueOf(term.getValue(mdAttributeInteger.definesAttribute()));

    String queryType = EntityQueryAPIGenerator.getQueryClass(mdBusiness.definesType());
    Class<?> queryClass = LoaderDecorator.load(queryType);

    String termQueryType = BusinessQueryAPIGenerator.getMultiReferenceInterfaceCompiled(mdTerm);
    Class<?> termQueryClass = LoaderDecorator.load(termQueryType);

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    Object query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    SelectableMultiReference attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    SelectableInteger attributeInteger = (SelectableInteger) termQueryClass.getMethod("getTestInteger").invoke(attributeMultiReference);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, attributeInteger.EQ(value));

    OIterator<?> iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    List<? extends Business> results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(1, results.size());
    Assert.assertEquals(business.getOid(), results.get(0).getOid());

    /*
     * Test a failure case
     */
    value = Integer.valueOf("65");
    factory = new QueryFactory();
    query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    attributeInteger = (SelectableInteger) termQueryClass.getMethod("getTestInteger").invoke(attributeMultiReference);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, attributeInteger.EQ(value));

    iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(0, results.size());
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testAttributeMultiReferenceQueryOnLong_Generated() throws Exception
  {
    BusinessDAO term = this.createTerm();
    BusinessDAO business = this.createBusiness(term);
    Long value = Long.valueOf(term.getValue(mdAttributeLong.definesAttribute()));

    String queryType = EntityQueryAPIGenerator.getQueryClass(mdBusiness.definesType());
    Class<?> queryClass = LoaderDecorator.load(queryType);

    String termQueryType = BusinessQueryAPIGenerator.getMultiReferenceInterfaceCompiled(mdTerm);
    Class<?> termQueryClass = LoaderDecorator.load(termQueryType);

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    Object query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    SelectableMultiReference attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    SelectableLong attributeLong = (SelectableLong) termQueryClass.getMethod("getTestLong").invoke(attributeMultiReference);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, attributeLong.EQ(value));

    OIterator<?> iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    List<? extends Business> results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(1, results.size());
    Assert.assertEquals(business.getOid(), results.get(0).getOid());

    /*
     * Test a failure case
     */
    value = Long.valueOf("65");
    factory = new QueryFactory();
    query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    attributeLong = (SelectableLong) termQueryClass.getMethod("getTestLong").invoke(attributeMultiReference);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, attributeLong.EQ(value));

    iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(0, results.size());
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testAttributeContainsAll_Generated() throws Exception
  {
    Business term1 = BusinessFacade.get(this.createTerm());
    Business term2 = BusinessFacade.get(this.createTerm());
    Business term3 = BusinessFacade.get(this.createTerm());
    Business business = BusinessFacade.get(this.createBusiness(term2, term3));

    Business[] value = (Business[]) Array.newInstance(term1.getClass(), 2);
    value[0] = term2;
    value[1] = term3;

    String queryType = EntityQueryAPIGenerator.getQueryClass(mdBusiness.definesType());
    Class<?> queryClass = LoaderDecorator.load(queryType);

    String termQueryType = BusinessQueryAPIGenerator.getMultiReferenceInterfaceCompiled(mdTerm);
    Class<?> termQueryClass = LoaderDecorator.load(termQueryType);

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    Object query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    SelectableMultiReference attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    Condition condition = (Condition) termQueryClass.getMethod("containsAll", value.getClass()).invoke(attributeMultiReference, (Object) value);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, condition);

    OIterator<?> iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    List<? extends Business> results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(1, results.size());
    Assert.assertEquals(business.getOid(), results.get(0).getOid());

    /*
     * Test a failure case
     */
    value = (Business[]) Array.newInstance(term1.getClass(), 2);
    value[0] = term1;
    value[1] = term2;

    factory = new QueryFactory();
    query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    condition = (Condition) termQueryClass.getMethod("containsAll", value.getClass()).invoke(attributeMultiReference, (Object) value);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, condition);

    iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(0, results.size());
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testAttributeContainsAny_Generated() throws Exception
  {
    Business term1 = BusinessFacade.get(this.createTerm());
    Business term2 = BusinessFacade.get(this.createTerm());
    Business business = BusinessFacade.get(this.createBusiness(term2));

    Business[] value = (Business[]) Array.newInstance(term1.getClass(), 2);
    value[0] = term1;
    value[1] = term2;

    String queryType = EntityQueryAPIGenerator.getQueryClass(mdBusiness.definesType());
    Class<?> queryClass = LoaderDecorator.load(queryType);

    String termQueryType = BusinessQueryAPIGenerator.getMultiReferenceInterfaceCompiled(mdTerm);
    Class<?> termQueryClass = LoaderDecorator.load(termQueryType);

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    Object query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    SelectableMultiReference attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    Condition condition = (Condition) termQueryClass.getMethod("containsAny", value.getClass()).invoke(attributeMultiReference, (Object) value);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, condition);

    OIterator<?> iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    List<? extends Business> results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(1, results.size());
    Assert.assertEquals(business.getOid(), results.get(0).getOid());

    /*
     * Test a failure case
     */
    value = (Business[]) Array.newInstance(term1.getClass(), 1);
    value[0] = term1;

    factory = new QueryFactory();
    query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    condition = (Condition) termQueryClass.getMethod("containsAny", value.getClass()).invoke(attributeMultiReference, (Object) value);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, condition);

    iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(0, results.size());
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testAttributeContainsExactly_Generated() throws Exception
  {
    Business term1 = BusinessFacade.get(this.createTerm());
    Business term2 = BusinessFacade.get(this.createTerm());
    Business business = BusinessFacade.get(this.createBusiness(term2));

    Business[] value = (Business[]) Array.newInstance(term1.getClass(), 1);
    value[0] = term2;

    String queryType = EntityQueryAPIGenerator.getQueryClass(mdBusiness.definesType());
    Class<?> queryClass = LoaderDecorator.load(queryType);

    String termQueryType = BusinessQueryAPIGenerator.getMultiReferenceInterfaceCompiled(mdTerm);
    Class<?> termQueryClass = LoaderDecorator.load(termQueryType);

    /*
     * Test a success case
     */
    QueryFactory factory = new QueryFactory();
    Object query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    SelectableMultiReference attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    Condition condition = (Condition) termQueryClass.getMethod("containsExactly", value.getClass()).invoke(attributeMultiReference, (Object) value);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, condition);

    OIterator<?> iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    List<? extends Business> results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(1, results.size());
    Assert.assertEquals(business.getOid(), results.get(0).getOid());

    /*
     * Test a failure case
     */
    value = (Business[]) Array.newInstance(term1.getClass(), 2);
    value[0] = term1;
    value[1] = term2;

    factory = new QueryFactory();
    query = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    attributeMultiReference = (SelectableMultiReference) queryClass.getMethod("getTestMultiReference").invoke(query);
    condition = (Condition) termQueryClass.getMethod("containsExactly", value.getClass()).invoke(attributeMultiReference, (Object) value);
    queryClass.getMethod("WHERE", Condition.class).invoke(query, condition);

    iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(query);
    results = (List<? extends Business>) iterator.getAll();

    Assert.assertEquals(0, results.size());
  }

  /**
   * @return
   */
  private BusinessDAO createTerm()
  {
    BusinessDAO term = BusinessDAO.newInstance(mdTerm.definesType());
    term.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term 1");
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
  private BusinessDAO createBusiness(ComponentIF... terms)
  {
    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());

    for (ComponentIF term : terms)
    {
      business.addItem(this.getMdAttribute().definesAttribute(), term.getOid());
    }

    business.apply();

    return business;
  }

  /**
   * @param results
   * @param oid
   * @return
   */
  private boolean contains(List<? extends Business> results, String oid)
  {
    for (Business result : results)
    {
      if (result.getOid().equals(oid))
      {
        return true;
      }
    }

    return false;
  }

}
