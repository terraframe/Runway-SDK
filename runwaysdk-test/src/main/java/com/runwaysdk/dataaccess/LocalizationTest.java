/**
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
 */
package com.runwaysdk.dataaccess;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.ClientSession;
import com.runwaysdk.ProblemException;
import com.runwaysdk.ProblemIF;
import com.runwaysdk.business.Struct;
import com.runwaysdk.business.generation.dto.ComponentDTOGenerator;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.EntityTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeLocalTextInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdDimensionInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.SupportedLocaleInfo;
import com.runwaysdk.dataaccess.attributes.EmptyValueProblem;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalTextDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdLocalStructDAO;
import com.runwaysdk.dataaccess.metadata.MdPackage;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.SupportedLocaleDAO;
import com.runwaysdk.facade.Facade;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.Session;

public class LocalizationTest extends TestCase
{
  /**
   * The run method is only overridden to allow aspects to weave in
   * 
   * @see junit.framework.TestCase#run()
   */
  @Override
  public TestResult run()
  {
    return super.run();
  }

  /**
   * The run method is only overridden to allow aspects to weave in
   * 
   * @see junit.framework.TestCase#run(junit.framework.TestResult)
   */
  @Override
  public void run(TestResult testResult)
  {
    super.run(testResult);
  }

  private static String                       pack           = "test.localize";

  private static MdBusinessDAO                phrases;

  private static MdLocalStructDAO             struct;

  private static MdAttributeLocalCharacterDAO lang;

  private static MdAttributeLocalTextDAO      formal;

  private static String                       sorryId;

  private static String                       greetingId;

  private static String                       defaultApology = "This is my default apology";

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(LocalizationTest.class);

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

  private static void classSetUp()
  {
    phrases = MdBusinessDAO.newInstance();
    phrases.setValue(MdTypeInfo.NAME, "Phrases");
    phrases.setValue(MdTypeInfo.PACKAGE, pack);
    phrases.setValue(MdElementInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    phrases.setStructValue(MdTypeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Localizable Phrases");
    phrases.setStructValue(MdTypeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A collection of localized phrases");
    phrases.setGenerateMdController(false);
    phrases.apply();

    struct = MdLocalStructDAO.newInstance();
    struct.setValue(MdTypeInfo.NAME, "PhrasesStruct");
    struct.setValue(MdTypeInfo.PACKAGE, pack);
    struct.setStructValue(MdTypeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Struct to hold Localized Phrases");
    struct.setGenerateMdController(false);
    struct.apply();

    lang = MdAttributeLocalCharacterDAO.newInstance();
    lang.setValue(MdAttributeLocalCharacterInfo.NAME, "lang");
    lang.setStructValue(MdAttributeLocalCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Language");
    lang.setValue(MdAttributeLocalCharacterInfo.DEFINING_MD_CLASS, phrases.getId());
    lang.setValue(MdAttributeLocalCharacterInfo.MD_STRUCT, struct.getId());
    lang.apply();

    formal = MdAttributeLocalTextDAO.newInstance();
    formal.setValue(MdAttributeLocalTextInfo.NAME, "formal");
    formal.setStructValue(MdAttributeLocalTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Formal Usage");
    formal.setValue(MdAttributeLocalTextInfo.DEFINING_MD_CLASS, phrases.getId());
    formal.setValue(MdAttributeLocalTextInfo.MD_STRUCT, struct.getId());
    formal.apply();

    BusinessDAO sorryDAO = BusinessDAO.newInstance(phrases.definesType());
    sorryDAO.setStructValue(lang.definesAttribute(), MdAttributeLocalInfo.DEFAULT_LOCALE, defaultApology);
    sorryId = sorryDAO.apply();

    BusinessDAO greetingDAO = BusinessDAO.newInstance(phrases.definesType());
    greetingId = greetingDAO.apply();
  }

  private static void classTearDown()
  {
    new MdPackage(pack).delete();
  }

  public void testRequiredAttribute()
  {
    BusinessDAO businessDAO1 = null;
    BusinessDAO businessDAO2 = null;
    BusinessDAO businessDAO3 = null;
    BusinessDAO businessDAO4 = null;
    
    Locale originalLocale = CommonProperties.getDefaultLocale();
    
    try
    {
      // Should not throw an exception
      businessDAO1 = BusinessDAO.newInstance(phrases.definesType());
      businessDAO1.apply();
    
      lang = (MdAttributeLocalCharacterDAO)MdAttributeLocalCharacterDAO.get(lang.getId()).getBusinessDAO();
      lang.setValue(MdAttributeLocalCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
      lang.apply();
    
      try
      {
        businessDAO2 = BusinessDAO.newInstance(phrases.definesType());
        businessDAO2.apply();
      }
      // This is expected
      catch (ProblemException pe)
      {
        List<ProblemIF> problemList = pe.getProblems();
        
        if (problemList.size() == 1 && problemList.get(0) instanceof EmptyValueProblem)
        {
          // Expected to land here
        }
        else
        {
          fail(EmptyValueProblem.class.getName() + " was not thrown.");
        }
      }
            
      lang.addLocale(Locale.GERMAN);
      
      CommonProperties.setDefaultLocaleForTestingPurposesOnly(Locale.GERMAN);
      
      businessDAO3 = BusinessDAO.newInstance(phrases.definesType());
      businessDAO3.setStructValue(lang.definesAttribute(), "de", "Das Boot");
      businessDAO3.apply();
      
      businessDAO4 = BusinessDAO.newInstance(phrases.definesType());
      businessDAO4.setStructValue(lang.definesAttribute(), MdAttributeLocalInfo.DEFAULT_LOCALE, "Das Boot");
      businessDAO4.apply();
    }
    finally
    { 
      CommonProperties.setDefaultLocaleForTestingPurposesOnly(originalLocale);
      
      if (businessDAO1 != null && !businessDAO1.isNew())
      {
        businessDAO1.delete();  
      }
    
      if (businessDAO2 != null && !businessDAO2.isNew())
      {
        businessDAO2.delete();
      }
      
      if (businessDAO3 != null && !businessDAO3.isNew())
      {
        businessDAO3.delete();
      }
      
      if (businessDAO4 != null && !businessDAO4.isNew())
      {
        businessDAO4.delete();
      }
      
      lang = (MdAttributeLocalCharacterDAO)MdAttributeLocalCharacterDAO.get(lang.getId()).getBusinessDAO();
      lang.setValue(MdAttributeLocalCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      lang.apply();
      
      lang.removeLocale(Locale.GERMAN);
    }
  }
  
  public void testCreateLocalizableNoStruct()
  {
    String structType = pack + ".PhrasesHello";
    try
    {
      MdStructDAO.getMdStructDAO(structType);
      fail("[" + structType + "] already exists");
    }
    catch (DataNotFoundException e)
    {
      // We expect the type to not exist yet
    }

    MdAttributeLocalCharacterDAO hello = MdAttributeLocalCharacterDAO.newInstance();
    hello.setValue(MdAttributeLocalCharacterInfo.NAME, "hello");
    hello.setStructValue(MdAttributeLocalCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Hello");
    hello.setValue(MdAttributeLocalCharacterInfo.DEFINING_MD_CLASS, phrases.getId());
    hello.apply();

    try
    {
      // Go check to make sure the struct was created
      MdStructDAO.getMdStructDAO(structType);
    }
    finally
    {
      hello.delete();
    }
  }

  public void testCreateLocalizableCustomStruct()
  {
    String structType = pack + ".PhrasesGoodbye";

    MdAttributeLocalCharacterDAO goodbye = MdAttributeLocalCharacterDAO.newInstance();
    goodbye.setValue(MdAttributeLocalCharacterInfo.NAME, "goodbye");
    goodbye.setStructValue(MdAttributeLocalCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Goodbye");
    goodbye.setValue(MdAttributeLocalCharacterInfo.DEFINING_MD_CLASS, phrases.getId());
    goodbye.setValue(MdAttributeLocalCharacterInfo.MD_STRUCT, struct.getId());
    goodbye.apply();

    try
    {
      MdStructDAO.getMdStructDAO(structType);
      fail("[" + structType + "] was autocreated even though mdStruct was specified");
    }
    catch (DataNotFoundException e)
    {
      // We expect the type to not be created since we specified mdStruct
      // manually
    }
    finally
    {
      goodbye.delete();
    }
  }

  public void testAddDeleteLocale() throws Exception
  {
    String fr_fr = Locale.FRANCE.toString();

    lang.addLocale(Locale.FRANCE);

    if (struct.definesAttribute(fr_fr) == null)
    {
      fail("Attribute failed to associate French with it");
    }

    try
    {
      String in = "Désolé";
      BusinessDAO sorryDAO = BusinessDAO.get(sorryId).getBusinessDAO();
      sorryDAO.setStructValue(lang.definesAttribute(), fr_fr, in);
      sorryDAO.apply();

      Class<?> phrasesClass = LoaderDecorator.load(phrases.definesType());

      Method get = phrasesClass.getMethod("get", String.class);
      Object object = get.invoke(null, sorryId);
      Struct struct = (Struct) phrasesClass.getMethod("getLang").invoke(object);
      String out = struct.getValue(fr_fr);

      assertEquals("Stored and Retrieved StructCharacters have different values.", in, out);
    }
    finally
    {
      lang.removeLocale(Locale.FRANCE);
    }
  }

  public void testGetLocalText() throws Exception
  {
    String fr_fr = Locale.FRANCE.toString();

    lang.addLocale(Locale.FRANCE);

    if (struct.definesAttribute(fr_fr) == null)
    {
      fail("Attribute failed to associate French with it");
    }

    try
    {
      String in = "Enchantée";
      BusinessDAO greetingDAO = BusinessDAO.get(greetingId).getBusinessDAO();
      greetingDAO.setStructValue(formal.definesAttribute(), fr_fr, in);
      greetingDAO.apply();

      Class<?> phrasesClass = LoaderDecorator.load(phrases.definesType());

      Method get = phrasesClass.getMethod("get", String.class);
      Object object = get.invoke(null, greetingId);
      Struct struct = (Struct) phrasesClass.getMethod("getFormal").invoke(object);
      String out = (String) struct.getValue(fr_fr);

      assertEquals("Stored and Retrieved StructCharacters have different values.", in, out);
    }
    finally
    {
      lang.removeLocale(Locale.FRANCE);
    }
  }

  public void testUserLocale() throws Exception
  {
    lang.addLocale(Locale.GERMAN);

    try
    {
      String in = "Es tut mir leid";

      BusinessDAO sorryDAO = BusinessDAO.get(sorryId).getBusinessDAO();
      sorryDAO.setStructValue(lang.definesAttribute(), "de", in);
      sorryDAO.apply();

      Class<?> phrasesClass = LoaderDecorator.load(phrases.definesType());
      Class<?> structClass = LoaderDecorator.load(struct.definesType());

      Method get = phrasesClass.getMethod("get", String.class);
      Object object = get.invoke(null, sorryId);
      Object struct = phrasesClass.getMethod("getLang").invoke(object);
      String out = (String) structClass.getMethod("getValue", Locale.class).invoke(struct, Locale.GERMANY);

      assertEquals("Stored and Retrieved StructCharacters have different values.", in, out);
    }
    finally
    {
      lang.removeLocale(Locale.GERMAN);
    }
  }

  public void testUserLocaleDTO() throws Exception
  {
    lang.addLocale(Locale.GERMAN);

    try
    {
      String in = "Es tut mir leid";

      BusinessDAO sorryDAO = BusinessDAO.get(sorryId).getBusinessDAO();
      sorryDAO.setStructValue(lang.definesAttribute(), "de", in);
      sorryDAO.apply();

      ClientSession systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { Locale.GERMAN });
      ClientRequestIF request = systemSession.getRequest();
      try
      {
        Class<?> phrasesDtoClass = LoaderDecorator.load(phrases.definesType() + ComponentDTOGenerator.DTO_SUFFIX);
        Class<?> structDtoClass = LoaderDecorator.load(struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX);

        Method get = phrasesDtoClass.getMethod("get", ClientRequestIF.class, String.class);
        Object object = get.invoke(null, request, sorryId);
        Object struct = phrasesDtoClass.getMethod("getLang").invoke(object);
        String out = (String) structDtoClass.getMethod("getValue", Locale.class).invoke(struct, Locale.GERMANY);

        assertEquals("Stored and Retrieved StructCharacters have different values.", in, out);
      }
      finally
      {
        systemSession.logout();
      }
    }
    finally
    {
      lang.removeLocale(Locale.GERMAN);
    }
  }

  public void testTextDTO() throws Exception
  {
    lang.addLocale(Locale.GERMAN);

    try
    {
      String in = "Grüß dich";

      BusinessDAO greetingDAO = BusinessDAO.get(greetingId).getBusinessDAO();
      greetingDAO.setStructValue(formal.definesAttribute(), "de", in);
      greetingDAO.apply();

      ClientSession systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { Locale.GERMAN });
      ClientRequestIF request = systemSession.getRequest();
      try
      {
        Class<?> phrasesDtoClass = LoaderDecorator.load(phrases.definesType() + ComponentDTOGenerator.DTO_SUFFIX);
        Class<?> structDtoClass = LoaderDecorator.load(struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX);

        Method get = phrasesDtoClass.getMethod("get", ClientRequestIF.class, String.class);
        Object object = get.invoke(null, request, greetingId);
        Object struct = phrasesDtoClass.getMethod("getFormal").invoke(object);
        String out = (String) structDtoClass.getMethod("getValue", Locale.class).invoke(struct, Locale.GERMANY);

        assertEquals("Stored and Retrieved StructCharacters have different values.", in, out);
      }
      finally
      {
        systemSession.logout();
      }
    }
    finally
    {
      lang.removeLocale(Locale.GERMAN);
    }
  }

  public void testDefaultCharacterDTO() throws Exception
  {
    lang.addLocale(Locale.GERMAN);

    try
    {
      String in = "Es tut mir leid";

      BusinessDAO sorryDAO = BusinessDAO.get(sorryId).getBusinessDAO();
      sorryDAO.setStructValue(lang.definesAttribute(), "de", in);
      sorryDAO.apply();

      ClientSession systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { Locale.GERMANY });
      ClientRequestIF request = systemSession.getRequest();

      try
      {
        Class<?> phrasesDtoClass = LoaderDecorator.load(phrases.definesType() + ComponentDTOGenerator.DTO_SUFFIX);
        Class<?> structDtoClass = LoaderDecorator.load(struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX);

        Method get = phrasesDtoClass.getMethod("get", ClientRequestIF.class, String.class);
        Object object = get.invoke(null, request, sorryId);
        Object struct = phrasesDtoClass.getMethod("getLang").invoke(object);
        String out = (String) structDtoClass.getMethod("getValue").invoke(struct);

        assertEquals("Stored and Retrieved StructCharacters have different values.", in, out);
      }
      finally
      {
        systemSession.logout();
      }
    }
    finally
    {
      lang.addLocale(Locale.GERMAN);
    }
  }

  public void testDefaultTextDTO() throws Exception
  {
    lang.addLocale(Locale.GERMAN);

    try
    {
      String in = "Grüß dich";

      BusinessDAO greetingDAO = BusinessDAO.get(greetingId).getBusinessDAO();
      greetingDAO.setStructValue(formal.definesAttribute(), "de", in);
      greetingDAO.apply();

      ClientSession systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { Locale.GERMANY });
      ClientRequestIF request = systemSession.getRequest();

      try
      {
        Class<?> phrasesDtoClass = LoaderDecorator.load(phrases.definesType() + ComponentDTOGenerator.DTO_SUFFIX);
        Class<?> structDtoClass = LoaderDecorator.load(struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX);

        Method get = phrasesDtoClass.getMethod("get", ClientRequestIF.class, String.class);
        Object object = get.invoke(null, request, greetingId);
        Object struct = phrasesDtoClass.getMethod("getFormal").invoke(object);
        String out = (String) structDtoClass.getMethod("getValue").invoke(struct);

        assertEquals("Stored and Retrieved StructCharacters have different values.", in, out);
      }
      finally
      {
        systemSession.logout();
      }
    }
    finally
    {
      lang.addLocale(Locale.GERMAN);
    }
  }

  public void testLocalizeDefaultDimensionQuery()
  {
    MdDimensionDAO mdDimensionDAO = MdDimensionDAO.newInstance();
    mdDimensionDAO.getAttribute(MdDimensionInfo.NAME).setValue("TestDimension");
    mdDimensionDAO.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Dimension");
    mdDimensionDAO.apply();

    String dimensionDefault = "Dimension default apology";

    BusinessDAO sorryDAO = BusinessDAO.get(sorryId).getBusinessDAO();
    sorryDAO.setStructValue(lang.definesAttribute(), mdDimensionDAO.getDefaultLocaleAttributeName(), dimensionDefault);
    sorryDAO.apply();

    String sessionId = null;

    try
    {
      sessionId = Facade.login(ServerConstants.PUBLIC_USER_PASSWORD, ServerConstants.PUBLIC_USER_PASSWORD, new Locale[] { Session.getCurrentLocale() });
      localizeDefaultDimensionQuery(sessionId, mdDimensionDAO, dimensionDefault);
    }
    finally
    {
      if (sessionId == null)
      {
        Facade.logout(sessionId);
      }
      mdDimensionDAO.delete();
    }
  }

  @Request(RequestType.SESSION)
  private void localizeDefaultDimensionQuery(String sessionId, MdDimensionDAOIF mdDimensionDAOIF, String dimensionDefault)
  {
    Session session = (Session) Session.getCurrentSession();
    session.setDimension((MdDimensionDAOIF) null);

    QueryFactory qf = new QueryFactory();
    ValueQuery vq = new ValueQuery(qf);
    BusinessDAOQuery bq = qf.businessDAOQuery(phrases.definesType());

    vq.SELECT(bq.aLocalCharacter(lang.definesAttribute()).localize("localValue"));
    vq.WHERE(bq.id().EQ(sorryId));

    OIterator<ValueObject> i = vq.getIterator();
    ValueObject valueObject = i.next();
    i.close();
    String out = valueObject.getValue("localValue");

    assertEquals("the non-dimensional default apology should have been returned.", defaultApology, out);

    session.setDimension(mdDimensionDAOIF);

    vq.clearSelectClause();
    vq.SELECT(bq.aLocalCharacter(lang.definesAttribute()).localize("localValue"));
    i = vq.getIterator();
    valueObject = i.next();
    i.close();
    out = valueObject.getValue("localValue");

    assertEquals("the dimensional default apology should have been returned.", dimensionDefault, out);
  }

  public void testLocalizeDefaultDimension()
  {
    MdDimensionDAO mdDimensionDAO = MdDimensionDAO.newInstance();
    mdDimensionDAO.getAttribute(MdDimensionInfo.NAME).setValue("TestDimension");
    mdDimensionDAO.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Dimension");
    mdDimensionDAO.apply();

    String dimensionDefault = "Dimension default apology";

    BusinessDAO sorryDAO = BusinessDAO.get(sorryId).getBusinessDAO();
    sorryDAO.setStructValue(lang.definesAttribute(), mdDimensionDAO.getDefaultLocaleAttributeName(), dimensionDefault);
    sorryDAO.apply();

    String sessionId = null;

    try
    {
      sessionId = Facade.login(ServerConstants.PUBLIC_USER_PASSWORD, ServerConstants.PUBLIC_USER_PASSWORD, new Locale[] { Session.getCurrentLocale() });
      localizeDefaultDimension(sessionId, mdDimensionDAO, sorryDAO, dimensionDefault);
    }
    finally
    {
      if (sessionId == null)
      {
        Facade.logout(sessionId);
      }
      mdDimensionDAO.delete();
    }
  }

  @Request(RequestType.SESSION)
  private void localizeDefaultDimension(String sessionId, MdDimensionDAOIF mdDimensionDAOIF, BusinessDAO sorryDAO, String dimensionDefault)
  {
    Session session = (Session) Session.getCurrentSession();
    session.setDimension((MdDimensionDAOIF) null);

    String out = sorryDAO.getLocalValue(lang.definesAttribute(), Session.getCurrentLocale());
    assertEquals("the non-dimensional default apology should have been returned.", defaultApology, out);

    session.setDimension(mdDimensionDAOIF);

    out = sorryDAO.getLocalValue(lang.definesAttribute(), Session.getCurrentLocale());
    assertEquals("the dimensional default apology should have been returned.", dimensionDefault, out);
  }

  /**
   * The dimension default has a value, but not the locale dimension.
   */
  public void testLocalizedLocaleWithDimensionDefault()
  {
    this.addLocale(Locale.GERMAN);

    MdDimensionDAO mdDimensionDAO = MdDimensionDAO.newInstance();
    mdDimensionDAO.getAttribute(MdDimensionInfo.NAME).setValue("TestDimension");
    mdDimensionDAO.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Dimension");
    mdDimensionDAO.apply();

    String germanAppology = "Es tut mir leid";

    String dimensionDefault = "Dimension default apology";

    // Make sure it retrieves the default dimension an not the non-dimension
    // locale
    BusinessDAO sorryDAO = BusinessDAO.get(sorryId).getBusinessDAO();
    sorryDAO.setStructValue(lang.definesAttribute(), "de", germanAppology);
    sorryDAO.setStructValue(lang.definesAttribute(), mdDimensionDAO.getDefaultLocaleAttributeName(), dimensionDefault);
    sorryDAO.apply();

    String sessionId = null;

    try
    {
      sessionId = Facade.login(ServerConstants.PUBLIC_USER_PASSWORD, ServerConstants.PUBLIC_USER_PASSWORD, new Locale[] { Locale.GERMANY });
      localizedLocaleWithDimensionDefault(sessionId, mdDimensionDAO, sorryDAO, germanAppology, dimensionDefault);

      String dimensionLocale = "Dimension Es tut mir leid";

      sorryDAO = BusinessDAO.get(sorryId).getBusinessDAO();
      sorryDAO.setStructValue(lang.definesAttribute(), mdDimensionDAO.getLocaleAttributeName(Locale.GERMAN), dimensionLocale);
      sorryDAO.apply();

      localizedLocaleWithDimension(sessionId, mdDimensionDAO, sorryDAO, germanAppology, dimensionLocale);
    }
    finally
    {
      if (sessionId == null)
      {
        Facade.logout(sessionId);
      }

      mdDimensionDAO.delete();

      this.deleteLocale(Locale.GERMAN);
    }
  }

  @Request(RequestType.SESSION)
  private void localizedLocaleWithDimensionDefault(String sessionId, MdDimensionDAOIF mdDimensionDAOIF, BusinessDAO sorryDAO, String localeValue, String dimensionDefault)
  {
    Session session = (Session) Session.getCurrentSession();
    session.setDimension((MdDimensionDAOIF) null);

    String out = sorryDAO.getLocalValue(lang.definesAttribute(), Session.getCurrentLocale());
    assertEquals("the non-dimensional local apology should have been returned.", localeValue, out);

    session.setDimension(mdDimensionDAOIF);

    out = sorryDAO.getLocalValue(lang.definesAttribute(), Session.getCurrentLocale());
    assertEquals("the dimensional default apology should have been returned.", dimensionDefault, out);
  }

  @Request(RequestType.SESSION)
  private void localizedLocaleWithDimension(String sessionId, MdDimensionDAOIF mdDimensionDAOIF, BusinessDAO sorryDAO, String localeValue, String dimensionLocale)
  {
    Session session = (Session) Session.getCurrentSession();
    session.setDimension((MdDimensionDAOIF) null);

    String out = sorryDAO.getLocalValue(lang.definesAttribute(), Session.getCurrentLocale());
    assertEquals("the non-dimensional local apology should have been returned.", localeValue, out);

    session.setDimension(mdDimensionDAOIF);

    out = sorryDAO.getLocalValue(lang.definesAttribute(), Session.getCurrentLocale());
    assertEquals("the dimensional local apology should have been returned.", dimensionLocale, out);
  }

  /**
   * The dimension default has a value, but not the locale dimension.
   */
  public void testLocalizedLocaleWithDimensionDefaultQuery()
  {
    this.addLocale(Locale.GERMAN);

    MdDimensionDAO mdDimensionDAO = MdDimensionDAO.newInstance();
    mdDimensionDAO.getAttribute(MdDimensionInfo.NAME).setValue("TestDimension");
    mdDimensionDAO.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Dimension");
    mdDimensionDAO.apply();

    String germanAppology = "Es tut mir leid";

    String dimensionDefault = "Dimension default apology";

    // Make sure it retrieves the default dimension an not the non-dimension
    // locale
    BusinessDAO sorryDAO = BusinessDAO.get(sorryId).getBusinessDAO();
    sorryDAO.setStructValue(lang.definesAttribute(), "de", germanAppology);
    sorryDAO.setStructValue(lang.definesAttribute(), mdDimensionDAO.getDefaultLocaleAttributeName(), dimensionDefault);
    sorryDAO.apply();

    String sessionId = null;

    try
    {
      sessionId = Facade.login(ServerConstants.PUBLIC_USER_PASSWORD, ServerConstants.PUBLIC_USER_PASSWORD, new Locale[] { Locale.GERMANY });
      localizedLocaleWithDimensionDefaultQuery(sessionId, mdDimensionDAO, sorryDAO, germanAppology, dimensionDefault);

      String dimensionLocale = "Dimension Es tut mir leid";

      sorryDAO = BusinessDAO.get(sorryId).getBusinessDAO();
      sorryDAO.setStructValue(lang.definesAttribute(), mdDimensionDAO.getLocaleAttributeName(Locale.GERMAN), dimensionLocale);
      sorryDAO.apply();

      localizedLocaleWithDimensionQuery(sessionId, mdDimensionDAO, sorryDAO, germanAppology, dimensionLocale);
    }
    finally
    {
      if (sessionId == null)
      {
        Facade.logout(sessionId);
      }
      mdDimensionDAO.delete();

      this.deleteLocale(Locale.GERMAN);
    }
  }

  @Request(RequestType.SESSION)
  private void localizedLocaleWithDimensionDefaultQuery(String sessionId, MdDimensionDAOIF mdDimensionDAOIF, BusinessDAO sorryDAO, String localeValue, String dimensionDefault)
  {
    Session session = (Session) Session.getCurrentSession();
    session.setDimension((MdDimensionDAOIF) null);

    QueryFactory qf = new QueryFactory();
    ValueQuery vq = new ValueQuery(qf);
    BusinessDAOQuery bq = qf.businessDAOQuery(phrases.definesType());

    vq.SELECT(bq.aLocalCharacter(lang.definesAttribute()).localize("localValue"));
    vq.WHERE(bq.id().EQ(sorryId));

    OIterator<ValueObject> i = vq.getIterator();
    ValueObject valueObject = i.next();
    i.close();
    String out = valueObject.getValue("localValue");
    assertEquals("the non-dimensional local apology should have been returned.", localeValue, out);

    session.setDimension(mdDimensionDAOIF);

    vq.clearSelectClause();
    vq.SELECT(bq.aLocalCharacter(lang.definesAttribute()).localize("localValue"));
    i = vq.getIterator();
    valueObject = i.next();
    i.close();
    out = valueObject.getValue("localValue");

    assertEquals("the dimensional default apology should have been returned.", dimensionDefault, out);
  }

  @Request(RequestType.SESSION)
  private void localizedLocaleWithDimensionQuery(String sessionId, MdDimensionDAOIF mdDimensionDAOIF, BusinessDAO sorryDAO, String localeValue, String dimensionLocale)
  {
    Session session = (Session) Session.getCurrentSession();
    session.setDimension((MdDimensionDAOIF) null);

    QueryFactory qf = new QueryFactory();
    ValueQuery vq = new ValueQuery(qf);
    BusinessDAOQuery bq = qf.businessDAOQuery(phrases.definesType());
    vq.SELECT(bq.aLocalCharacter(lang.definesAttribute()).localize("localValue"));
    vq.WHERE(bq.id().EQ(sorryId));

    OIterator<ValueObject> i = vq.getIterator();
    ValueObject valueObject = i.next();
    i.close();
    String out = valueObject.getValue("localValue");

    assertEquals("the non-dimensional local apology should have been returned.", localeValue, out);

    session.setDimension(mdDimensionDAOIF);

    vq.clearSelectClause();
    vq.SELECT(bq.aLocalCharacter(lang.definesAttribute()).localize("localValue"));
    i = vq.getIterator();
    valueObject = i.next();
    i.close();
    out = valueObject.getValue("localValue");

    assertEquals("the dimensional local apology should have been returned.", dimensionLocale, out);
  }

  public void testAddSupportedLocales()
  {
    MdLocalStructDAOIF mdLocalStructDAOIF = MdLocalStructDAO.getMdLocalStructDAO(EntityTypes.METADATADISPLAYLABEL.getType());
    int oldNumAttributes = mdLocalStructDAOIF.definesAttributes().size();

    this.addLocale(Locale.ITALY);

    int newNumAttributes = mdLocalStructDAOIF.definesAttributes().size();

    assertEquals("Adding a supported locale did not automatically add an additional attribute to a local struct type.", oldNumAttributes + 1, newNumAttributes);

    assertNotSame("Adding a supported locale did not automatically add an additional attribute to a local struct type.", null, mdLocalStructDAOIF.definesAttribute(Locale.ITALY.toString()));

    this.deleteLocale(Locale.ITALY);

    assertEquals("Removing a supported locale did not automatically remove an attribute from a local struct type.", oldNumAttributes, mdLocalStructDAOIF.definesAttributes().size());

    assertEquals("Removing a supported locale did not automatically remove an attribute from a local struct type.", null, mdLocalStructDAOIF.definesAttribute(Locale.ITALY.toString()));
  }

  public void testAddDimension()
  {
    MdLocalStructDAOIF mdLocalStructDAOIF = MdLocalStructDAO.getMdLocalStructDAO(EntityTypes.METADATADISPLAYLABEL.getType());
    int oldNumAttributes = mdLocalStructDAOIF.definesAttributes().size();

    MdDimensionDAO mdDimensionDAO = MdDimensionDAO.newInstance();
    mdDimensionDAO.getAttribute(MdDimensionInfo.NAME).setValue("TestDimension");
    mdDimensionDAO.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Dimension");
    mdDimensionDAO.apply();

    int newNumAttributes = mdLocalStructDAOIF.definesAttributes().size();

    assertEquals("Adding a dimension did not automatically add an additional attribute to a local struct type.", oldNumAttributes + 1, newNumAttributes);

    assertNotSame("Adding a dimension did not automatically add an additional attribute to a local struct type.", null, mdLocalStructDAOIF.definesAttribute(mdDimensionDAO.getDefaultLocaleAttributeName()));

    mdDimensionDAO.delete();

    assertEquals("Removing a dimension did not automatically remove an attribute from a local struct type.", oldNumAttributes, mdLocalStructDAOIF.definesAttributes().size());

    assertEquals("Removing a dimension did not automatically remove an attribute from a local struct type.", null, mdLocalStructDAOIF.definesAttribute(mdDimensionDAO.getDefaultLocaleAttributeName()));
  }

  public void testAddDimensionToLocales()
  {
    this.addLocale(Locale.ITALY);

    MdLocalStructDAOIF mdLocalStructDAOIF = MdLocalStructDAO.getMdLocalStructDAO(EntityTypes.METADATADISPLAYLABEL.getType());
    int oldNumAttributes = mdLocalStructDAOIF.definesAttributes().size();

    MdDimensionDAO mdDimensionDAO = MdDimensionDAO.newInstance();
    mdDimensionDAO.getAttribute(MdDimensionInfo.NAME).setValue("TestDimension");
    mdDimensionDAO.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Dimension");
    mdDimensionDAO.apply();

    int newNumAttributes = mdLocalStructDAOIF.definesAttributes().size();

    assertEquals("Adding a dimension with an existing locale did not automatically add two additional attribute to a local struct type.", oldNumAttributes + 2, newNumAttributes);

    assertNotSame("Adding a dimension with an existing locale did not automatically add an additional attribute to a local struct type.", null, mdLocalStructDAOIF.definesAttribute(mdDimensionDAO.getLocaleAttributeName(Locale.ITALY)));

    mdDimensionDAO.delete();

    assertEquals("Removing a dimension with an existing locale did not automatically remove an attribute from the local struct type.", oldNumAttributes, mdLocalStructDAOIF.definesAttributes().size());

    assertEquals("Removing a dimension with an existing locale did not automatically remove an attribute from the local struct type.", null, mdLocalStructDAOIF.definesAttribute(mdDimensionDAO.getLocaleAttributeName(Locale.ITALY)));

    this.deleteLocale(Locale.ITALY);
  }

  public void testAddLocaleToDimension()
  {
    MdDimensionDAO mdDimensionDAO = MdDimensionDAO.newInstance();
    mdDimensionDAO.getAttribute(MdDimensionInfo.NAME).setValue("TestDimension");
    mdDimensionDAO.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Dimension");
    mdDimensionDAO.apply();

    MdLocalStructDAOIF mdLocalStructDAOIF = MdLocalStructDAO.getMdLocalStructDAO(EntityTypes.METADATADISPLAYLABEL.getType());
    int oldNumAttributes = mdLocalStructDAOIF.definesAttributes().size();

    this.addLocale(Locale.ITALY);

    int newNumAttributes = mdLocalStructDAOIF.definesAttributes().size();

    assertEquals("Adding a dimension with an existing locale did not automatically add two additional attribute to a local struct type.", oldNumAttributes + 2, newNumAttributes);

    assertNotSame("Adding a dimension with an existing locale did not automatically add an additional attribute to a local struct type.", null, mdLocalStructDAOIF.definesAttribute(mdDimensionDAO.getLocaleAttributeName(Locale.ITALY)));

    this.deleteLocale(Locale.ITALY);

    assertEquals("Removing a dimension with an existing locale did not automatically remove an attribute from the local struct type.", oldNumAttributes, mdLocalStructDAOIF.definesAttributes().size());

    assertEquals("Removing a dimension with an existing locale did not automatically remove an attribute from the local struct type.", null, mdLocalStructDAOIF.definesAttribute(mdDimensionDAO.getLocaleAttributeName(Locale.ITALY)));

    mdDimensionDAO.delete();
  }

  // Test columns on local attributes
  public void testLocaleAttributes()
  {
    MdDimensionDAO mdDimension = TestFixtureFactory.createMdDimension();
    mdDimension.apply();

    try
    {
      MdLocalStructDAO mdStruct = TestFixtureFactory.createMdLocalStruct();
      mdStruct.apply();

      try
      {

        MdAttributeLocalCharacterDAO mdAttributeCharacter = TestFixtureFactory.addLocalCharacterAttribute(phrases, mdStruct);
        mdAttributeCharacter.apply();

        try
        {
          MdLocalStructDAOIF mdStructIF = mdAttributeCharacter.getMdStructDAOIF();

          MdAttributeDAOIF defaultDimension = mdStructIF.getDefaultLocale(mdDimension);

          assertNotNull(defaultDimension);
        }
        finally
        {
          TestFixtureFactory.delete(mdAttributeCharacter);
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdStruct);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdDimension);
    }

  }

  // Test columns on local attributes
  public void testLocaleAttributesWithDefaultLocaleStruct()
  {
    MdDimensionDAO mdDimension = TestFixtureFactory.createMdDimension();
    mdDimension.apply();

    try
    {

      MdAttributeLocalCharacterDAO mdAttributeCharacter = TestFixtureFactory.addLocalCharacterAttribute(phrases);
      mdAttributeCharacter.apply();

      try
      {
        MdLocalStructDAOIF mdStructIF = mdAttributeCharacter.getMdStructDAOIF();

        MdAttributeDAOIF defaultDimension = mdStructIF.getDefaultLocale(mdDimension);

        assertNotNull(defaultDimension);
      }
      finally
      {
        MdLocalStructDAOIF mdStructIF = mdAttributeCharacter.getMdStructDAOIF();

        TestFixtureFactory.delete(mdAttributeCharacter);
        TestFixtureFactory.delete(mdStructIF);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdDimension);
    }

  }

  // ///////////////////////////////////////////////////////////////////////////
  // Utility methods
  // ///////////////////////////////////////////////////////////////////////////

  private void addLocale(Locale locale)
  {
    SupportedLocaleDAO supportedLocaleDAO = SupportedLocaleDAO.newInstance();
    supportedLocaleDAO.setValue(SupportedLocaleInfo.NAME, locale.toString());
    supportedLocaleDAO.setValue(SupportedLocaleInfo.DISPLAY_LABEL, locale.toString());
    supportedLocaleDAO.setValue(SupportedLocaleInfo.LOCALE_LABEL, locale.toString());
    supportedLocaleDAO.apply();
  }

  @Request
  private void deleteLocale(Locale locale)
  {
//    SessionIF currentSession = Session.getCurrentSession();
//
//    if (currentSession != null)
//    {
//      ( (Session) currentSession ).setDimension((String) null);
//    }

    SupportedLocaleDAO supportedLocaleDAO = (SupportedLocaleDAO) SupportedLocaleDAO.getEnumeration(SupportedLocaleInfo.CLASS, locale.toString());
    supportedLocaleDAO.delete();
  }
}
