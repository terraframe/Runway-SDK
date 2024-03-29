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

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.ProblemException;
import com.runwaysdk.ProblemIF;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeDimensionInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.dataaccess.attributes.AttributeLengthCharacterException;
import com.runwaysdk.dataaccess.attributes.AttributeValueCannotBeNegativeProblem;
import com.runwaysdk.dataaccess.attributes.AttributeValueCannotBePositiveProblem;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.attributes.EmptyValueProblem;
import com.runwaysdk.dataaccess.attributes.InvalidReferenceException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.metadata.ForbiddenMethodException;
import com.runwaysdk.dataaccess.metadata.MdAttributeBlobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.facade.Facade;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.AllPostalCodes;

public class MdDimensionTest
{
  private static MdBusinessDAO             testMdBusiness;

  private static MdAttributeCharacterDAOIF mdAttributeDAOIF_1;

  private static MdAttributeCharacterDAO   dimensionMdAttributeRequired;

  private static MdAttributeDAOIF          mdAttributeDAOIF_2;

  private static MdDimensionDAO            mdDimension;

  private static MdDimensionDAO            mdDimension2;

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    org.junit.Assume.assumeFalse("true".equals(System.getenv("RUNWAY_TEST_IGNORE_DIMENSION_TESTS")));
    
    testMdBusiness = TestFixtureFactory.createMdBusiness1();
    testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    testMdBusiness.apply();

    MdAttributeCharacterDAO mdAttributeDAO_1 = TestFixtureFactory.addCharacterAttribute(testMdBusiness, "characterAttribute");
    mdAttributeDAO_1.apply();
    mdAttributeDAOIF_1 = mdAttributeDAO_1;

    dimensionMdAttributeRequired = TestFixtureFactory.addCharacterAttribute(testMdBusiness, "testCharDimensionRequired");
    dimensionMdAttributeRequired.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    dimensionMdAttributeRequired.apply();

    MdAttributeDAO mdAttributeDAO_2 = TestFixtureFactory.addIntegerAttribute(testMdBusiness, "integerCharacter");
    mdAttributeDAO_2.apply();
    mdAttributeDAOIF_2 = mdAttributeDAO_2;

    mdDimension = TestFixtureFactory.createMdDimension();
    mdDimension.apply();

    mdDimension2 = TestFixtureFactory.createMdDimension("D2");
    mdDimension2.apply();
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdAttributeDAOIF_1);
    TestFixtureFactory.delete(dimensionMdAttributeRequired);
    TestFixtureFactory.delete(mdAttributeDAOIF_2);
    TestFixtureFactory.delete(mdDimension);
    TestFixtureFactory.delete(mdDimension2);
    TestFixtureFactory.delete(testMdBusiness);
  }

  @Request
  @Before
  public void setUp()
  {
    dimensionAudit();
  }

  /**
   * Tests for a valid default value set on attribute dimensions.
   * 
   */
  @Request
  @Test
  public void testInvalidDefaultBooleanAttribute()
  {
    MdAttributeBooleanDAO mdAttrBoolean = TestFixtureFactory.addBooleanAttribute(testMdBusiness);
    mdAttrBoolean.apply();

    try
    {
      MdAttributeDimensionDAO mdAttributeDimensionDAO = mdAttrBoolean.getMdAttributeDimension(mdDimension).getBusinessDAO();
      mdAttributeDimensionDAO.setValue(MdAttributeDimensionInfo.DEFAULT_VALUE, "Hello!");

      try
      {
        mdAttributeDimensionDAO.apply();
        Assert.fail("A boolean default value on an attribute dimension was set with an invalid value.");
      }
      catch (AttributeValueException e)
      {
        // This is expected
      }
    }
    finally
    {
      mdAttrBoolean.delete();
    }
  }

  /**
   * Tests for a valid default value set on attribute dimensions.
   * 
   */
  @Request
  @Test
  public void testValidDefaultBooleanAttribute()
  {
    MdAttributeBooleanDAO mdAttrBoolean = TestFixtureFactory.addBooleanAttribute(testMdBusiness);
    mdAttrBoolean.apply();

    String dimensionDefaultValue = MdAttributeBooleanInfo.TRUE;
    MdAttributeDimensionDAO mdAttributeDimensionDAO = (MdAttributeDimensionDAO) mdAttrBoolean.getMdAttributeDimension(mdDimension).getBusinessDAO();
    mdAttributeDimensionDAO.setValue(MdAttributeDimensionInfo.DEFAULT_VALUE, dimensionDefaultValue);
    try
    {
      try
      {
        mdAttributeDimensionDAO.apply();
      }
      catch (Throwable e)
      {
        Assert.fail("Unable to set a valid boolean default value on an attribute dimension.");
      }

      String sessionId = null;
      try
      {
        sessionId = Facade.login(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { Session.getCurrentLocale() });
        validDefaultBooleanAttribute(sessionId, dimensionDefaultValue);
      }
      finally
      {
        if (sessionId != null)
        {
          Facade.logout(sessionId);
        }
      }
    }
    finally
    {
      mdAttrBoolean.delete();
    }
  }

  @Request(RequestType.SESSION)
  private void validDefaultBooleanAttribute(String sessionId, String dimensionDefaultValue)
  {
    Session session = (Session) Session.getCurrentSession();
    session.setDimension(mdDimension);
    BusinessDAO businessDAO = BusinessDAO.newInstance(testMdBusiness.definesType());
    Assert.assertEquals("Dimension default value was not set on a new business object.", dimensionDefaultValue, businessDAO.getValue(TestFixConst.ATTRIBUTE_BOOLEAN));
  }

  /**
   * Tests for a valid default value set on attribute dimensions.
   * 
   */
  @Request
  @Test
  public void testInvalidDefaultCharacterAttribute()
  {
    MdAttributeCharacterDAO mdAttrCharacter = TestFixtureFactory.addCharacterAttribute(testMdBusiness);
    mdAttrCharacter.setValue(MdAttributeCharacterInfo.SIZE, "4");
    mdAttrCharacter.apply();

    try
    {
      MdAttributeDimensionDAO mdAttributeDimensionDAO = mdAttrCharacter.getMdAttributeDimension(mdDimension).getBusinessDAO();
      mdAttributeDimensionDAO.setValue(MdAttributeDimensionInfo.DEFAULT_VALUE, "This is longer than 4 characters");
      mdAttributeDimensionDAO.apply();

      Assert.fail("A character default value on an attribute dimension was set with an invalid value.");
    }
    catch (AttributeLengthCharacterException e)
    {
      // This is expected
    }
    finally
    {
      mdAttrCharacter.delete();
    }
  }

  /**
   * Tests for a valid default value set on attribute dimensions.
   * 
   */
  @Request
  @Test
  public void testValidDefaultCharacterAttribute()
  {
    MdAttributeCharacterDAO mdAttrCharacter = TestFixtureFactory.addCharacterAttribute(testMdBusiness);
    mdAttrCharacter.setValue(MdAttributeCharacterInfo.SIZE, "4");
    mdAttrCharacter.apply();

    String dimensionDefaultValue = "1234";
    MdAttributeDimensionDAO mdAttributeDimensionDAO = (MdAttributeDimensionDAO) mdAttrCharacter.getMdAttributeDimension(mdDimension).getBusinessDAO();
    mdAttributeDimensionDAO.setValue(MdAttributeDimensionInfo.DEFAULT_VALUE, dimensionDefaultValue);

    try
    {
      try
      {
        mdAttributeDimensionDAO.apply();
      }
      catch (Throwable e)
      {
        Assert.fail("Unable to set a valid character default value on an attribute dimension.");
      }

      String sessionId = null;
      try
      {
        sessionId = Facade.login(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { Session.getCurrentLocale() });
        validDefaultCharacterAttribute(sessionId, dimensionDefaultValue);
      }
      finally
      {
        if (sessionId != null)
        {
          Facade.logout(sessionId);
        }
      }
    }
    finally
    {
      mdAttrCharacter.delete();
    }
  }

  @Request(RequestType.SESSION)
  private void validDefaultCharacterAttribute(String sessionId, String dimensionDefaultValue)
  {
    Session session = (Session) Session.getCurrentSession();
    session.setDimension(mdDimension);
    BusinessDAO businessDAO = BusinessDAO.newInstance(testMdBusiness.definesType());
    Assert.assertEquals("Dimension default value was not set on a new business object.", dimensionDefaultValue, businessDAO.getValue(TestFixConst.ATTRIBUTE_CHARACTER));
  }

  /**
   * Tests for a valid default value set on attribute dimensions.
   * 
   * @throws Exception
   * 
   */
  @Request
  @Test
  public void testInvalidDefaultFloatAttribute() throws Exception
  {
    MdAttributeFloatDAO mdAttrFloat = TestFixtureFactory.addFloatAttribute(testMdBusiness);
    mdAttrFloat.setValue(MdAttributeFloatInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttrFloat.apply();

    try
    {
      MdAttributeDimensionDAO mdAttributeDimensionDAO = mdAttrFloat.getMdAttributeDimension(mdDimension).getBusinessDAO();
      mdAttributeDimensionDAO.setValue(MdAttributeDimensionInfo.DEFAULT_VALUE, "-1.0");
      mdAttributeDimensionDAO.apply();

      Assert.fail("A float default value on an attribute dimension was set with an invalid value.");
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
        if (p instanceof AttributeValueCannotBeNegativeProblem)
          problemFound = true;
      }
      if (!problemFound)
        throw new Exception("AttributeValueCannotBeNegativeProblem was not thrown");
    }
    finally
    {
      mdAttrFloat.delete();
    }
  }

  /**
   * Tests for a valid default value set on attribute dimensions.
   * 
   */
  @Request
  @Test
  public void testValidDefaultFloatAttribute()
  {
    MdAttributeFloatDAO mdAttrFloat = TestFixtureFactory.addFloatAttribute(testMdBusiness);
    mdAttrFloat.setValue(MdAttributeFloatInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttrFloat.apply();

    String dimensionDefaultValue = "1.1";
    MdAttributeDimensionDAO mdAttributeDimensionDAO = (MdAttributeDimensionDAO) mdAttrFloat.getMdAttributeDimension(mdDimension).getBusinessDAO();
    mdAttributeDimensionDAO.setValue(MdAttributeDimensionInfo.DEFAULT_VALUE, dimensionDefaultValue);

    try
    {
      try
      {
        mdAttributeDimensionDAO.apply();
      }
      catch (Throwable e)
      {
        Assert.fail("Unable to set a valid float default value on an attribute dimension.");
      }

      String sessionId = null;
      try
      {
        sessionId = Facade.login(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { Session.getCurrentLocale() });
        validDefaultFloatAttribute(sessionId, dimensionDefaultValue);
      }
      finally
      {
        if (sessionId != null)
        {
          Facade.logout(sessionId);
        }
      }
    }
    finally
    {
      mdAttrFloat.delete();
    }
  }

  @Request(RequestType.SESSION)
  private void validDefaultFloatAttribute(String sessionId, String dimensionDefaultValue)
  {
    Session session = (Session) Session.getCurrentSession();
    session.setDimension(mdDimension);
    BusinessDAO businessDAO = BusinessDAO.newInstance(testMdBusiness.definesType());
    Assert.assertEquals("Dimension default value was not set on a new business object.", dimensionDefaultValue, businessDAO.getValue("testFloat"));
  }

  /**
   * Tests that an attribute that should be unique should also be required.
   * 
   */
  @Request
  @Test
  public void testInvalidDefaultEnumerationAttribute()
  {
    MdEnumerationDAOIF mdEnumerationIF = MdEnumerationDAO.getMdEnumerationDAO(AllPostalCodes.CLASS);

    MdAttributeEnumerationDAO mdAttrEnum = MdAttributeEnumerationDAO.newInstance();
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.NAME, "secondEnumeration");
    mdAttrEnum.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Attribute Enumeration");
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, mdEnumerationIF.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, testMdBusiness.getOid());
    mdAttrEnum.apply();

    MdAttributeDimensionDAO mdAttributeDimensionDAO = (MdAttributeDimensionDAO) mdAttrEnum.getMdAttributeDimension(mdDimension).getBusinessDAO();
    mdAttributeDimensionDAO.setValue(MdAttributeDimensionInfo.DEFAULT_VALUE, "An invalid enumeration item");

    try
    {
      mdAttributeDimensionDAO.apply();
      Assert.fail("A reference attribute was defined with an invalid value.");
    }
    catch (AttributeValueException e)
    {
      // This is expected
    }
    finally
    {
      mdAttrEnum.delete();
    }
  }

  /**
   * Tests that an attribute that should be unique should also be required.
   * 
   */
  @Request
  @Test
  public void testValidDefaultEnumerationAttribute()
  {
    MdEnumerationDAOIF mdEnumerationIF = MdEnumerationDAO.getMdEnumerationDAO(AllPostalCodes.CLASS);

    MdAttributeEnumerationDAO mdAttrEnum = MdAttributeEnumerationDAO.newInstance();
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.NAME, "attrEnumeration");
    mdAttrEnum.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Attribute Enumeration");
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, mdEnumerationIF.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, testMdBusiness.getOid());
    mdAttrEnum.apply();

    String masterDefiningType = mdEnumerationIF.getMasterListMdBusinessDAO().definesType();

    QueryFactory qf = new QueryFactory();
    BusinessDAOQuery q = qf.businessDAOQuery(masterDefiningType);

    OIterator<BusinessDAOIF> i = q.getIterator();

    BusinessDAOIF businessDAOIF = null;
    try
    {
      businessDAOIF = i.next();
    }
    finally
    {
      i.close();
    }

    String dimensionDefaultValue = businessDAOIF.getOid();
    MdAttributeDimensionDAO mdAttributeDimensionDAO = (MdAttributeDimensionDAO) mdAttrEnum.getMdAttributeDimension(mdDimension).getBusinessDAO();
    mdAttributeDimensionDAO.setValue(MdAttributeDimensionInfo.DEFAULT_VALUE, dimensionDefaultValue);

    try
    {
      try
      {
        mdAttributeDimensionDAO.apply();
      }
      catch (AttributeValueException e)
      {
        Assert.fail("Unable to set a valid enumeration attribute as an attribute dimension default value.");
      }

      String sessionId = null;
      try
      {
        sessionId = Facade.login(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { Session.getCurrentLocale() });
        validDefaultEnumerationAttribute(sessionId, dimensionDefaultValue);
      }
      finally
      {
        if (sessionId != null)
        {
          Facade.logout(sessionId);
        }
      }
    }
    finally
    {
      // mdAttributeDimensionDAO.delete();
      mdAttrEnum.delete();
    }
  }

  @Request(RequestType.SESSION)
  private void validDefaultEnumerationAttribute(String sessionId, String dimensionDefaultValue)
  {
    Session session = (Session) Session.getCurrentSession();
    session.setDimension(mdDimension);
    BusinessDAO businessDAO = BusinessDAO.newInstance(testMdBusiness.definesType());

    Set<String> idSet = ( (AttributeEnumerationIF) businessDAO.getAttributeIF("attrEnumeration") ).getCachedEnumItemIdSet();

    Assert.assertTrue("Dimension default value was not set on a new business object.", idSet.contains(dimensionDefaultValue));
  }

  @Request
  @Test
  public void testInvalidClobValue()
  {
    MdAttributeBlobDAO mdBlobAttribute = TestFixtureFactory.addBlobAttribute(testMdBusiness);
    mdBlobAttribute.apply();

    try
    {
      MdAttributeDimensionDAO mdAttributeDimensionDAO = mdBlobAttribute.getMdAttributeDimension(mdDimension).getBusinessDAO();
      mdAttributeDimensionDAO.setValue(MdAttributeDimensionInfo.DEFAULT_VALUE, "Invalid value");
      mdAttributeDimensionDAO.apply();

      Assert.fail("A float default value on an attribute dimension was set with an invalid value.");
    }
    catch (ForbiddenMethodException e)
    {
      // This is expected
    }
    finally
    {
      mdBlobAttribute.delete();
    }
  }

  @Request
  @Test
  public void testInvalidReferenceValue()
  {
    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness2.apply();

    try
    {
      MdAttributeReferenceDAO mdAttributeReference = TestFixtureFactory.addReferenceAttribute(testMdBusiness, mdBusiness2);
      mdAttributeReference.apply();

      try
      {
        MdAttributeDimensionDAO mdAttributeDimensionDAO = mdAttributeReference.getMdAttributeDimension(mdDimension).getBusinessDAO();
        mdAttributeDimensionDAO.setValue(MdAttributeDimensionInfo.DEFAULT_VALUE, "99999999-9999-9999-9999-999999999999");
        mdAttributeDimensionDAO.apply();

        Assert.fail("A reference attribute was defined with an invalid value.");
      }
      catch (InvalidReferenceException e)
      {
        // This is expected
      }
      finally
      {
        TestFixtureFactory.delete(mdAttributeReference);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdBusiness2);
    }
  }

  @Request
  @Test
  public void testValidReferenceValue()
  {
    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness2.apply();

    try
    {
      BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness2.definesType());
      businessDAO.apply();

      MdAttributeReferenceDAO mdAttributeReference = TestFixtureFactory.addReferenceAttribute(testMdBusiness, mdBusiness2);
      mdAttributeReference.apply();

      try
      {
        MdAttributeDimensionDAO mdAttributeDimensionDAO = mdAttributeReference.getMdAttributeDimension(mdDimension).getBusinessDAO();
        mdAttributeDimensionDAO.setValue(MdAttributeDimensionInfo.DEFAULT_VALUE, businessDAO.getOid());
        mdAttributeDimensionDAO.apply();
      }
      catch (AttributeValueException e)
      {
        Assert.fail("Unable to define reference attribute was with a valid default value.");
      }
      finally
      {
        TestFixtureFactory.delete(mdAttributeReference);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdBusiness2);
    }
  }

  @Request
  @Test
  public void testInvalidDateValue()
  {
    MdAttributeDateDAO mdDateAttribute = TestFixtureFactory.addDateAttribute(testMdBusiness);
    mdDateAttribute.apply();

    try
    {
      MdAttributeDimensionDAO mdAttributeDimensionDAO = mdDateAttribute.getMdAttributeDimension(mdDimension).getBusinessDAO();
      mdAttributeDimensionDAO.setValue(MdAttributeDimensionInfo.DEFAULT_VALUE, "INVALID DATE");
      mdAttributeDimensionDAO.apply();

      Assert.fail("Able to set an invalid date default value");
    }
    catch (Throwable e)
    {
      // This is expected
    }
    finally
    {
      mdDateAttribute.delete();
    }
  }

  @Request
  @Test
  public void testValidDateValue()
  {
    MdAttributeDateDAO mdDateAttribute = TestFixtureFactory.addDateAttribute(testMdBusiness);
    mdDateAttribute.apply();

    try
    {
      MdAttributeDimensionDAO mdAttributeDimensionDAO = mdDateAttribute.getMdAttributeDimension(mdDimension).getBusinessDAO();
      mdAttributeDimensionDAO.setValue(MdAttributeDimensionInfo.DEFAULT_VALUE, "2008-12-12");
      mdAttributeDimensionDAO.apply();
    }
    catch (Throwable e)
    {
      Assert.fail(e.getLocalizedMessage());
    }
    finally
    {
      mdDateAttribute.delete();
    }
  }

  @Request
  @Test
  public void testValidTextValue()
  {
    MdAttributeTextDAO mdAttributeText = TestFixtureFactory.addTextAttribute(testMdBusiness);
    mdAttributeText.apply();

    try
    {
      MdAttributeDimensionDAO mdAttributeDimensionDAO = mdAttributeText.getMdAttributeDimension(mdDimension).getBusinessDAO();
      mdAttributeDimensionDAO.setValue(MdAttributeDimensionInfo.DEFAULT_VALUE, "Blah!!!");
      mdAttributeDimensionDAO.apply();
    }
    catch (Throwable e)
    {
      Assert.fail(e.getLocalizedMessage());
    }
    finally
    {
      mdAttributeText.delete();
    }
  }

  /**
   * Tests for a valid default value set on attribute dimensions.
   * 
   * @throws Exception
   * 
   */
  @Request
  @Test
  public void testInvalidDefaultIntegerAttribute() throws Exception
  {
    MdAttributeIntegerDAO mdAttributeInteger = TestFixtureFactory.addIntegerAttribute(testMdBusiness);
    mdAttributeInteger.apply();

    try
    {
      MdAttributeDimensionDAO mdAttributeDimensionDAO = mdAttributeInteger.getMdAttributeDimension(mdDimension).getBusinessDAO();
      mdAttributeDimensionDAO.setValue(MdAttributeDimensionInfo.DEFAULT_VALUE, "1");
      mdAttributeDimensionDAO.apply();

      Assert.fail("A integer default value on an attribute dimension was set with an invalid value.");
    }
    catch (ProblemException e)
    {
      // This is expected
      List<ProblemIF> problems = e.getProblems();
      boolean problemFound = false;
      for (ProblemIF p : problems)
      {
        if (p instanceof AttributeValueCannotBePositiveProblem)
          problemFound = true;
      }
      if (!problemFound)
        throw new Exception("AttributeValueCannotBePositiveProblem was not thrown");
    }
    finally
    {
      mdAttributeInteger.delete();
    }
  }

  /**
   * Tests for a valid default value set on attribute dimensions.
   * 
   */
  @Request
  @Test
  public void testValidDefaultIntegerAttribute()
  {
    MdAttributeIntegerDAO mdAttributeInteger = TestFixtureFactory.addIntegerAttribute(testMdBusiness);
    mdAttributeInteger.apply();

    try
    {
      MdAttributeDimensionDAO mdAttributeDimensionDAO = mdAttributeInteger.getMdAttributeDimension(mdDimension).getBusinessDAO();
      mdAttributeDimensionDAO.setValue(MdAttributeDimensionInfo.DEFAULT_VALUE, "-1");
      mdAttributeDimensionDAO.apply();
    }
    catch (Throwable e)
    {
      e.printStackTrace();
      Assert.fail(e.getLocalizedMessage());
    }
    finally
    {
      mdAttributeInteger.delete();
    }
  }

  @Request
  @Test
  public void testDimensionalNotRequiredAttribute()
  {
    MdAttributeDimensionDAO mdAttributeDimensionDAO = dimensionMdAttributeRequired.getMdAttributeDimension(mdDimension).getBusinessDAO();
    mdAttributeDimensionDAO.setValue(MdAttributeDimensionInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDimensionDAO.apply();

    String sessionId = null;

    try
    {
      sessionId = Facade.login(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { Session.getCurrentLocale() });
      dimensionalNotRequiredAttribute(sessionId);
    }
    finally
    {
      if (sessionId != null)
      {
        Facade.logout(sessionId);
      }
    }
  }

  @Request(RequestType.SESSION)
  private void dimensionalNotRequiredAttribute(String sessionId)
  {
    Session session = (Session) Session.getCurrentSession();
    session.setDimension(mdDimension);

    Assert.assertFalse("Attribute Dimension required permission is incorrect.", dimensionMdAttributeRequired.isRequiredForDTO());

    BusinessDAO businessDAO = null;
    try
    {
      businessDAO = BusinessDAO.newInstance(testMdBusiness.definesType());
      businessDAO.apply();
    }
    catch (ProblemException e)
    {
      Assert.fail("Unable to add a non-required attribute for a dimension.");
    }
    finally
    {
      if (businessDAO != null && !businessDAO.isNew())
      {
        businessDAO.delete();
      }
      session.setDimension((MdDimensionDAOIF) null);
    }
  }

  @Request
  @Test
  public void testDimensionalRequiredAttribute()
  {
    MdAttributeCharacterDAO mdAttributeCharacterDAO = dimensionMdAttributeRequired;

    MdAttributeDimensionDAO mdAttributeDimensionDAO = mdAttributeCharacterDAO.getMdAttributeDimension(mdDimension).getBusinessDAO();
    mdAttributeDimensionDAO.setValue(MdAttributeDimensionInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeDimensionDAO.apply();

    String sessionId = null;

    try
    {
      sessionId = Facade.login(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { Session.getCurrentLocale() });
      dimensionalRequiredAttribute(sessionId);
    }
    finally
    {
      if (sessionId != null)
      {
        Facade.logout(sessionId);
      }

      mdAttributeDimensionDAO.setValue(MdAttributeDimensionInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeDimensionDAO.apply();
    }
  }

  @Request(RequestType.SESSION)
  private void dimensionalRequiredAttribute(String sessionId)
  {
    Session session = (Session) Session.getCurrentSession();
    session.setDimension(mdDimension);

    Assert.assertTrue("Attribute Dimension required permission is incorrect.", dimensionMdAttributeRequired.isRequiredForDTO());

    BusinessDAO businessDAO = null;
    try
    {
      businessDAO = BusinessDAO.newInstance(testMdBusiness.definesType());
      businessDAO.apply();
      Assert.fail("Attribute.validateRequired() accepted a blank value on a required field for a dimension.");
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
        Assert.fail(EmptyValueProblem.class.getName() + " was not thrown.");
      }
    }
    finally
    {
      if (businessDAO != null && !businessDAO.isNew())
      {
        businessDAO.delete();
      }
      session.setDimension((MdDimensionDAOIF) null);
    }
  }

  @Request
  @Test
  public void testCreateMdAttributeDimension()
  {
    MdAttributeDimensionDAOIF mdAttributeDimension = this.getMdAttributeCharacter1().getMdAttributeDimension(mdDimension);

    MdAttributeDimensionDAOIF test = MdAttributeDimensionDAO.get(mdAttributeDimension.getOid());

    Assert.assertEquals(mdAttributeDAOIF_1.getOid(), test.definingMdAttribute().getOid());
    Assert.assertEquals(mdDimension.getOid(), test.definingMdDimension().getOid());
  }

  @Request
  @Test
  public void testDoubleApply()
  {
    MdAttributeDimensionDAO mdAttributeDimension = this.getMdAttributeCharacter1().getMdAttributeDimension(mdDimension).getBusinessDAO();
    mdAttributeDimension.apply();

    try
    {
      mdAttributeDimension.apply();

      MdAttributeDimensionDAOIF test = MdAttributeDimensionDAO.get(mdAttributeDimension.getOid());

      Assert.assertEquals(mdAttributeDAOIF_1.getOid(), test.definingMdAttribute().getOid());
      Assert.assertEquals(mdDimension.getOid(), test.definingMdDimension().getOid());
    }
    finally
    {
    }
  }

  @Request
  @Test
  public void testDeleteMdBusinessWithAttributeDimesnions()
  {
    MdBusinessDAO _mdBusiness = TestFixtureFactory.createMdBusiness2();
    _mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    _mdBusiness.apply();

    MdAttributeCharacterDAO _mdAttribute = TestFixtureFactory.addCharacterAttribute(_mdBusiness);
    _mdAttribute.apply();

    MdAttributeDimensionDAOIF mdAttributeDimension = _mdAttribute.getMdAttributeDimension(mdDimension);

    try
    {
      TestFixtureFactory.delete(_mdBusiness);

      try
      {
        MdAttributeDimensionDAO.get(mdAttributeDimension.getOid());

        Assert.fail("MdAttributeDimension was not deleted when the defining MdBusiness of the MdAttribute was deleted.");
      }
      catch (DataNotFoundException e)
      {
        // This is expected
      }
    }
    catch (Exception e)
    {
      TestFixtureFactory.delete(_mdAttribute);
      TestFixtureFactory.delete(_mdBusiness);

      e.printStackTrace();

      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDeleteMdDimensionWithAttributeDimesnions()
  {
    MdDimensionDAO _mdDimension = TestFixtureFactory.createMdDimension("TempD");
    _mdDimension.apply();

    MdAttributeDimensionDAOIF mdAttributeDimension = this.getMdAttributeCharacter1().getMdAttributeDimension(_mdDimension);

    try
    {
      TestFixtureFactory.delete(_mdDimension);

      try
      {
        MdAttributeDimensionDAO.get(mdAttributeDimension.getOid());

        Assert.fail("MdAttributeDimension was not deleted when the defining MdBusiness of the MdAttribute was deleted.");
      }
      catch (DataNotFoundException e)
      {
        // This is expected
      }
    }
    catch (Exception e)
    {
      TestFixtureFactory.delete(_mdDimension);

      e.printStackTrace();

      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testGetMdAttributeDimension()
  {
    MdAttributeDimensionDAOIF test = this.getMdAttributeCharacter1().getMdAttributeDimension(mdDimension);

    Assert.assertNotNull(test);
    Assert.assertEquals(mdAttributeDAOIF_1.getOid(), test.definingMdAttribute().getOid());
    Assert.assertEquals(mdDimension.getOid(), test.definingMdDimension().getOid());
  }

  @Request
  @Test
  public void testGetAllMdAttributeDimension()
  {
    List<MdAttributeDimensionDAOIF> list = this.getMdAttributeCharacter1().getMdAttributeDimensions();

    Assert.assertEquals(2, list.size());
  }

  @Request
  @Test
  public void testGetMdAttributeDimensionFromMdDimension()
  {
    MdAttributeDimensionDAOIF test = this.getMdAttributeCharacter1().getMdAttributeDimension(mdDimension);

    Assert.assertNotNull(test);
    Assert.assertEquals(mdAttributeDAOIF_1.getOid(), test.definingMdAttribute().getOid());
    Assert.assertEquals(mdDimension.getOid(), test.definingMdDimension().getOid());
  }

  @Request
  @Test
  public void testGetAllMdAttributeDimensionFromDimension()
  {
    List<MdAttributeDimensionDAOIF> list = mdDimension.getMdAttributeDimensions();

    Assert.assertTrue(0 != list.size());
  }

  @Request
  @Test
  public void testGetMdClassDimension()
  {
    MdClassDimensionDAOIF test = testMdBusiness.getMdClassDimension(mdDimension);

    Assert.assertNotNull(test);
    Assert.assertEquals(testMdBusiness.getOid(), test.definingMdClass().getOid());
    Assert.assertEquals(mdDimension.getOid(), test.definingMdDimension().getOid());
  }

  @Request
  @Test
  public void testGetAllMdClassDimension()
  {
    List<MdClassDimensionDAOIF> list = testMdBusiness.getMdClassDimensions();

    Assert.assertEquals(2, list.size());
  }

  @Request
  @Test
  public void testGetMdClassDimensionFromMdDimension()
  {
    MdClassDimensionDAOIF test = mdDimension.getMdClassDimension(testMdBusiness);

    Assert.assertNotNull(test);
    Assert.assertEquals(testMdBusiness.getOid(), test.definingMdClass().getOid());
    Assert.assertEquals(mdDimension.getOid(), test.definingMdDimension().getOid());
  }

  @Request
  @Test
  public void testGetAllMdClassDimensionFromDimension()
  {
    List<MdClassDimensionDAOIF> list = mdDimension.getMdClassDimensions();

    Assert.assertTrue(0 != list.size());
  }

  /**
   * Returns a fresh object from the cache in case the cached attribute
   * dimension information has changed.
   * 
   * @return
   */
  private MdAttributeCharacterDAOIF getMdAttributeCharacter1()
  {
    // Refresh the object, as the dimension information is cached
    mdAttributeDAOIF_1 = MdAttributeCharacterDAO.get(mdAttributeDAOIF_1.getOid());
    return mdAttributeDAOIF_1;
  }

  /**
   * Returns a fresh object from the cache in case the cached attribute
   * dimension information has changed.
   * 
   * @return
   */
  @SuppressWarnings("unused")
  private MdAttributeDAOIF getMdAttributeCharacter2()
  {
    // Refresh the object, as the dimension information is cached
    mdAttributeDAOIF_2 = MdAttributeCharacterDAO.get(mdAttributeDAOIF_2.getOid());
    return mdAttributeDAOIF_2;
  }

  // private static int testCount = 0;

  /**
   * Audits the <code>MdDimensionDAOIF</code>s, the
   * code>MdAttributeDAOIF</code>s, and the
   * code>MdAttributeDimensionsDAOIF</code>s to check if all of the caches are
   * correct.
   */
  private void dimensionAudit()
  {
    // testCount++;
    // System.out.print("\n"+testCount+" ");

    boolean foundProblems = false;

    List<MdDimensionDAOIF> dimensionList = MdDimensionDAO.getAllMdDimensions();

    // Get a reference to all of the metadata attributes in the cache
    List<? extends EntityDAOIF> mdAttrList = EntityDAO.getCachedEntityDAOs(MdAttributeInfo.CLASS);

    for (EntityDAOIF entityDAOIF : mdAttrList)
    {
      MdAttributeDAOIF mdAttributeDAOIF = (MdAttributeDAOIF) entityDAOIF;
      List<MdAttributeDimensionDAOIF> mdAttributeCachedDimensions = mdAttributeDAOIF.getMdAttributeDimensions();

      List<MdAttributeDimensionDAOIF> collectedCachedDimensions = new LinkedList<MdAttributeDimensionDAOIF>();

      for (MdDimensionDAOIF mdDimensionDAOIF : dimensionList)
      {
        try
        {
          collectedCachedDimensions.add(mdAttributeDAOIF.getMdAttributeDimension(mdDimensionDAOIF));
        }
        catch (NullPointerException e)
        {
          // System.out.println("Dimension ["+mdDimensionDAOIF.getName()+"] was
          // missing from attribute ["+mdAttributeDAOIF.getKey()+"]");
        }
      }

      if (dimensionList.size() != collectedCachedDimensions.size())
      {
        System.out.print("Attribute [" + mdAttributeDAOIF.getKey() + "] has dimensions: ");
        for (MdAttributeDimensionDAOIF mdAttributeDimensionDAOIF : mdAttributeCachedDimensions)
        {
          System.out.print("[" + mdAttributeDimensionDAOIF.definingMdDimension().getName() + "] ");
        }
        System.out.print(" but should have: ");
        for (MdDimensionDAOIF mdDimensionDAOIF : dimensionList)
        {
          System.out.print("[" + mdDimensionDAOIF.getName() + "] ");
        }

        foundProblems = true;
      }
    }

    if (foundProblems)
    {
      throw new RuntimeException("Cached dimension structures became out of sync.");
    }

  }
}
