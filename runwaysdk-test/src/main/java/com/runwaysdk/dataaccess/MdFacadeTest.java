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
package com.runwaysdk.dataaccess;

import java.io.IOException;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.business.generation.CompilerException;
import com.runwaysdk.business.generation.TypeGenerator;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdFacadeInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdParameterInfo;
import com.runwaysdk.dataaccess.attributes.InvalidReferenceException;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.InvalidIdentifierException;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdFacadeDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.dataaccess.metadata.MethodDefinitionException_DefiningType;
import com.runwaysdk.dataaccess.metadata.MethodDefinitionException_InvalidReturnType;
import com.runwaysdk.dataaccess.metadata.MethodDefinitionException_NameExists;
import com.runwaysdk.dataaccess.metadata.ParameterDefinitionException_InvalidOrder;
import com.runwaysdk.dataaccess.metadata.ParameterDefinitionException_InvalidType;
import com.runwaysdk.dataaccess.metadata.ParameterDefinitionException_NameExists;
import com.runwaysdk.util.FileIO;

public class MdFacadeTest extends TestCase
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

  private static MdFacadeDAO      mdFacade;

  private static MdMethodDAO      facadeMethod;

  private static MdParameterDAO   facadeParam;

  private static MdBusinessDAO    mdBusinessEnum;

  private static MdEnumerationDAO mdEnum;

  private static MdBusinessDAO    mdBusiness;

  private static MdBusinessDAO    mdBusinessUnpublished;

  private static MdBusinessDAO    subClass;

  private static MdMethodDAO      entityMethod;

  private static MdMethodDAO      entityMethod2;

  private static MdParameterDAO   entityParam;

  private static MdAttributeConcreteDAO   mdAttribute;

  /**
   * Launch-point for the standalone textui JUnit tests in this class.
   * @param args
   */
  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(MdFacadeTest.suite());
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(MdFacadeTest.class);

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

  protected static void classSetUp()
  {
    mdBusinessEnum = MdBusinessDAO.newInstance();
    mdBusinessEnum.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdBusiness");
    mdBusinessEnum.setValue(MdBusinessInfo.PACKAGE, "test");
    mdBusinessEnum.setValue(MdBusinessInfo.NAME, "ClassEnum1");
    mdBusinessEnum.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdBusinessEnum.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, EnumerationMasterInfo.ID_VALUE);
    mdBusinessEnum.apply();

    mdEnum = MdEnumerationDAO.newInstance();
    mdEnum.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, mdBusinessEnum.getId());
    mdEnum.setValue(MdEnumerationInfo.PACKAGE, "test");
    mdEnum.setValue(MdEnumerationInfo.NAME, "Enum1");
    mdEnum.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Enum1" );
    mdEnum.apply();

    mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdBusiness");
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, "test");
    mdBusiness.setValue(MdBusinessInfo.NAME, "Class1");
    mdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdBusiness.apply();

    mdBusinessUnpublished = MdBusinessDAO.newInstance();
    mdBusinessUnpublished.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdBusinessUnpublished");
    mdBusinessUnpublished.setValue(MdBusinessInfo.PACKAGE, "test");
    mdBusinessUnpublished.setValue(MdBusinessInfo.NAME, "UnpublishedClass");
    mdBusinessUnpublished.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdBusinessUnpublished.setValue(MdBusinessInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    mdBusinessUnpublished.apply();

    mdAttribute = MdAttributeCharacterDAO.newInstance();
    mdAttribute.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttribute.setValue(MdAttributeCharacterInfo.NAME, "aCharacter");
    mdAttribute.setValue(MdAttributeCharacterInfo.SIZE, Integer.toString(MdAttributeCharacterInfo.MAX_CHARACTER_SIZE));
    mdAttribute.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE,  "aCharacter");
    mdAttribute.apply();

    subClass = MdBusinessDAO.newInstance();
    subClass.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "SubClass");
    subClass.setValue(MdBusinessInfo.PACKAGE, "test");
    subClass.setValue(MdBusinessInfo.NAME, "SubClass1");
    subClass.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdBusiness.getId());
    subClass.apply();

    entityMethod = MdMethodDAO.newInstance();
    entityMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusiness.getId());
    entityMethod.setValue(MdMethodInfo.NAME, "checkout");
    entityMethod.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.Integer");
    entityMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Check Out");
    entityMethod.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    entityMethod.apply();

    entityMethod2 = MdMethodDAO.newInstance();
    entityMethod2.setValue(MdMethodInfo.REF_MD_TYPE, mdBusiness.getId());
    entityMethod2.setValue(MdMethodInfo.NAME, "allItems");
    entityMethod2.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.Integer[]");
    entityMethod2.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "All Items");
    entityMethod2.apply();

    entityParam = MdParameterDAO.newInstance();
    entityParam.setValue(MdParameterInfo.ENCLOSING_METADATA, entityMethod.getId());
    entityParam.setValue(MdParameterInfo.TYPE, "java.lang.Long");
    entityParam.setValue(MdParameterInfo.NAME, "seq");
    entityParam.setValue(MdParameterInfo.ORDER, "0");
    entityParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sequence");
    entityParam.apply();

    mdFacade = MdFacadeDAO.newInstance();
    mdFacade.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdFacade");
    mdFacade.setValue(MdBusinessInfo.PACKAGE, "test");
    mdFacade.setValue(MdBusinessInfo.NAME, "Facade1");
    mdFacade.apply();

    facadeMethod = MdMethodDAO.newInstance();
    facadeMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    facadeMethod.setValue(MdMethodInfo.NAME, "checkout");
    facadeMethod.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.Integer");
    facadeMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Check Out");
    facadeMethod.apply();

    facadeParam = MdParameterDAO.newInstance();
    facadeParam.setValue(MdParameterInfo.ENCLOSING_METADATA, facadeMethod.getId());
    facadeParam.setValue(MdParameterInfo.TYPE, mdBusiness.definesType() + "[]");
    facadeParam.setValue(MdParameterInfo.NAME, "class1Array");
    facadeParam.setValue(MdParameterInfo.ORDER, "0");
    facadeParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Class1 Array");
    facadeParam.apply();
  }

  protected static void classTearDown()
  {
    TestFixtureFactory.delete(facadeMethod);
    TestFixtureFactory.delete(entityMethod);
    TestFixtureFactory.delete(mdFacade);
    TestFixtureFactory.delete(subClass);
    TestFixtureFactory.delete(mdBusiness);
    TestFixtureFactory.delete(mdBusinessEnum);
    TestFixtureFactory.delete(mdBusinessUnpublished);
  }

  @Override
  protected void setUp() throws Exception
  {
  }

  @Override
  protected void tearDown() throws Exception
  {
  }

  public void testRollback() throws IOException
  {
    //Get the .java and .class for the generated mdBusiness1 class
    String baseSource = FileIO.readString(TypeGenerator.getBaseSrcFilePath(mdFacade));
    String stubSource = FileIO.readString(TypeGenerator.getJavaSrcFilePath(mdFacade));

    String badStubSource = "i am invalid code == true";

    //Set the stub source to invalid java code
    try
    {
      mdFacade.setValue(MdBusinessInfo.STUB_SOURCE, badStubSource);
      mdFacade.apply();

      fail("do not pass go.");
    }
    catch(CompilerException e)
    {
      //Do nothing this is expected
    }

    //Get the new .java and .class for the generated mdFacade class
    String newBaseSource = FileIO.readString(TypeGenerator.getBaseSrcFilePath(mdFacade));
    String newStubSource = FileIO.readString(TypeGenerator.getJavaSrcFilePath(mdFacade));

    //Ensure that the old and new .java and .class files are identical
    assertEquals(baseSource, newBaseSource);

    if (!LocalProperties.isDeployedInContainer())
    {
      try
      {
        assertEquals(badStubSource, newStubSource);
      }
      catch(Throwable t)
      {
        throw new RuntimeException(t);
      }
      finally
      {
        //Restore the stub source
        mdFacade.setValue(MdBusinessInfo.STUB_SOURCE, stubSource);
        mdFacade.apply();
      }
    }
    else
    {
      assertEquals(stubSource, newStubSource);
    }
  }

  public void testMdFacade()
  {
    BusinessDAOIF businessDAO = BusinessDAO.get(mdFacade.getId());

    assertNotNull(businessDAO);
    assertEquals("MdFacade", businessDAO.getStructValue(MdFacadeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals("test", businessDAO.getValue(MdFacadeInfo.PACKAGE));
    assertEquals("Facade1", businessDAO.getValue(MdFacadeInfo.NAME));
  }

  public void testFacadeMethod()
  {
    BusinessDAOIF mdMethod = BusinessDAO.get(facadeMethod.getId());

    assertEquals("java.lang.Integer", mdMethod.getValue(MdMethodInfo.RETURN_TYPE));
    assertEquals("checkout", mdMethod.getValue(MdMethodInfo.NAME));
    assertEquals(mdFacade.getId(), mdMethod.getValue(MdMethodInfo.REF_MD_TYPE));
    assertEquals(MdAttributeBooleanInfo.FALSE, mdMethod.getValue(MdMethodInfo.IS_STATIC));
  }

  public void testEntityMethod()
  {
    MdMethodDAOIF mdMethod = (MdMethodDAOIF) BusinessDAO.get(entityMethod.getId());

    assertEquals("java.lang.Integer", mdMethod.getValue(MdMethodInfo.RETURN_TYPE));
    assertEquals("checkout", mdMethod.getValue(MdMethodInfo.NAME));
    assertEquals(mdBusiness.getId(), mdMethod.getValue(MdMethodInfo.REF_MD_TYPE));
    assertEquals(MdAttributeBooleanInfo.TRUE, mdMethod.getValue(MdMethodInfo.IS_STATIC));
    assertEquals(true, mdMethod.isStatic());
  }

  public void testInvalidReturnType()
  {
    try
    {
      MdMethodDAO mdMethod = MdMethodDAO.newInstance();
      mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusiness.getId());
      mdMethod.setValue(MdMethodInfo.NAME, "invalidReturn");
      mdMethod.setValue(MdMethodInfo.RETURN_TYPE, "int");
      mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Invalid Return Test ");
      mdMethod.apply();

      fail("Able to create a MdMethod with an invalid return type");
    }
    catch (MethodDefinitionException_InvalidReturnType e)
    {
      //expected
    }
  }

  public void testInvalidUnpublishedReturnType()
  {
    try
    {
      MdMethodDAO mdMethod = MdMethodDAO.newInstance();
      mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusiness.getId());
      mdMethod.setValue(MdMethodInfo.NAME, "invalidReturn");
      mdMethod.setValue(MdMethodInfo.RETURN_TYPE, mdBusinessUnpublished.definesType());
      mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Invalid Return Test ");
      mdMethod.apply();

      fail("Able to create a MdMethod with a return type that is not published.");
    }
    catch (MethodDefinitionException_InvalidReturnType e)
    {
      //expected
    }
  }

  public void testChangeInvalidUnpublishedReturnType()
  {
    MdMethodDAO mdMethod = MdMethodDAO.newInstance();
    mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusiness.getId());
    mdMethod.setValue(MdMethodInfo.NAME, "validReturn");
    mdMethod.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.Integer");
    mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Valid Return Test ");
    mdMethod.apply();

    try
    {
      mdMethod.setValue(MdMethodInfo.RETURN_TYPE, mdBusinessUnpublished.definesType());
      mdMethod.apply();

      fail("Able to change the return type on an MdMethod to a type that is not published.");
    }
    catch (MethodDefinitionException_InvalidReturnType e)
    {
      //expected
    }
    finally
    {
      mdMethod.delete();
    }
  }

  public void testDuplicateMethodName()
  {
    try
    {
      MdMethodDAO mdMethod = MdMethodDAO.newInstance();
      mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusiness.getId());
      mdMethod.setValue(MdMethodInfo.NAME, "checkout");
      mdMethod.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.Integer");
      mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Check Out");
      mdMethod.apply();

      fail("Able to create a MdMethod with a duplicate name as an existing MdMethod");
    }
    catch (MethodDefinitionException_NameExists e)
    {
      //expected
    }
  }

  public void testMdAttributeReference()
  {
    try
    {
      MdMethodDAO mdMethod = MdMethodDAO.newInstance();
      mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdAttribute.getId());
      mdMethod.setValue(MdMethodInfo.NAME, "myMethod");
      mdMethod.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.Integer");
      mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Method Method");
      mdMethod.apply();

      fail("Able to create a MdMethod with a reference to an MdAttribute");
    }
    catch (InvalidReferenceException e)
    {
      //expected
    }
  }

  public void testMethodCleanUp()
  {
    MdBusinessDAO mdBusiness2 = MdBusinessDAO.newInstance();
    mdBusiness2.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Md Class");
    mdBusiness2.setValue(MdBusinessInfo.PACKAGE, "test");
    mdBusiness2.setValue(MdBusinessInfo.NAME, "Class2");
    mdBusiness2.apply();

    MdMethodDAO mdMethod = MdMethodDAO.newInstance();
    mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusiness2.getId());
    mdMethod.setValue(MdMethodInfo.NAME, "checkout");
    mdMethod.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.Integer");
    mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Check Out");
    String id = mdMethod.apply();

    TestFixtureFactory.delete(mdBusiness2);

    try
    {
      BusinessDAO.get(id);

      fail("MdMethod was not deleted with its defining MdEntity");
    }
    catch (Exception e)
    {
      //Expected
    }
  }

  public void testMethodCleanUp2()
  {
    MdFacadeDAO mdFacade2 = MdFacadeDAO.newInstance();
    mdFacade2.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,"MdFacade");
    mdFacade2.setValue(MdBusinessInfo.PACKAGE, "test");
    mdFacade2.setValue(MdBusinessInfo.NAME, "Facade2");
    mdFacade2.apply();

    MdMethodDAO mdMethod = MdMethodDAO.newInstance();
    mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade2.getId());
    mdMethod.setValue(MdMethodInfo.NAME, "checkout");
    mdMethod.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.Integer");
    mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Check Out");
    String id = mdMethod.apply();

    TestFixtureFactory.delete(mdFacade2);

    try
    {
      BusinessDAO.get(id);

      fail("MdMethod was not deleted with its defining MdFacade");
    }
    catch (Exception e)
    {
      //Expected
    }
  }

  public void testArrayReturnType()
  {
    MdMethodDAOIF mdMethod = MdMethodDAO.get(entityMethod2.getId());

    assertEquals(entityMethod2.getValue(MdMethodInfo.RETURN_TYPE), mdMethod
        .getValue(MdMethodInfo.RETURN_TYPE));
    assertEquals(entityMethod2.getValue(MdMethodInfo.NAME), mdMethod.getValue(MdMethodInfo.NAME));
    assertEquals(mdBusiness.getId(), mdMethod.getValue(MdMethodInfo.REF_MD_TYPE));
  }

  public void testInvalidMethodName()
  {
    try
    {
      MdMethodDAO mdMethod = MdMethodDAO.newInstance();
      mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusiness.getId());
      mdMethod.setValue(MdMethodInfo.NAME, "This is an invalid name.");
      mdMethod.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.Integer[]");
      mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "All Items");
      mdMethod.apply();

      fail("Able to define a MdMethod with a name that does not follow java naming conventions");
    }
    catch (InvalidIdentifierException e)
    {
      //Expected
    }
  }

  public void testFacadeParameter()
  {
    MdParameterDAOIF mdParameter = MdParameterDAO.get(facadeParam.getId());

    assertEquals(facadeParam.getValue(MdParameterInfo.TYPE), mdParameter.getValue(MdParameterInfo.TYPE));
    assertEquals(facadeParam.getValue(MdParameterInfo.NAME), mdParameter.getValue(MdParameterInfo.NAME));
    assertEquals(facadeParam.getValue(MdParameterInfo.ORDER), mdParameter
        .getValue(MdParameterInfo.ORDER));
    assertEquals(facadeMethod.getId(), mdParameter.getValue(MdParameterInfo.ENCLOSING_METADATA));
  }

  public void testEntityParameter()
  {
    MdParameterDAOIF mdParameter = MdParameterDAO.get(entityParam.getId());

    assertEquals(entityParam.getValue(MdParameterInfo.TYPE), mdParameter.getValue(MdParameterInfo.TYPE));
    assertEquals(entityParam.getValue(MdParameterInfo.NAME), mdParameter.getValue(MdParameterInfo.NAME));
    assertEquals(entityParam.getValue(MdParameterInfo.ORDER), mdParameter
        .getValue(MdParameterInfo.ORDER));
    assertEquals(entityMethod.getId(), mdParameter.getValue(MdParameterInfo.ENCLOSING_METADATA));
  }

  public void testDuplicateParameter()
  {
    try
    {
      MdParameterDAO mdParameter = MdParameterDAO.newInstance();
      mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, entityMethod.getId());
      mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.Long");
      mdParameter.setValue(MdParameterInfo.NAME, "seq");
      mdParameter.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sequence");
      mdParameter.setValue(MdParameterInfo.ORDER, "100");
      mdParameter.apply();

      mdParameter.delete();
      fail("Able to add a parameter of identical name as another parameter on the same method");
    }
    catch (ParameterDefinitionException_NameExists e)
    {
      //Expected
    }
  }

  public void testUnpublishedParameter()
  {
    try
    {
      MdParameterDAO mdParameter = MdParameterDAO.newInstance();
      mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, entityMethod.getId());
      mdParameter.setValue(MdParameterInfo.TYPE, mdBusinessUnpublished.definesType());
      mdParameter.setValue(MdParameterInfo.NAME, "unpubParam1");
      mdParameter.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sequence");
      mdParameter.setValue(MdParameterInfo.ORDER, "100");
      mdParameter.apply();

      mdParameter.delete();
      fail("Able to add a parameter of a type that is not published.");
    }
    catch (ParameterDefinitionException_InvalidType e)
    {
      //Expected
    }
  }

  public void testChangeUnpublishedParameter()
  {
    MdParameterDAO mdParameter = MdParameterDAO.newInstance();
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, entityMethod.getId());
    mdParameter.setValue(MdParameterInfo.TYPE, mdBusiness.definesType());
    mdParameter.setValue(MdParameterInfo.NAME, "unpubParam2");
    mdParameter.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sequence");
    mdParameter.setValue(MdParameterInfo.ORDER, "100");
    mdParameter.apply();

    try
    {
      mdParameter.setValue(MdParameterInfo.TYPE, mdBusinessUnpublished.definesType());
      mdParameter.apply();

      fail("Able to add a parameter of a type that is not published.");
    }
    catch (ParameterDefinitionException_InvalidType e)
    {
      //Expected
    }
    finally
    {
      mdParameter.delete();
    }
  }

  public void testInvalidParameterName()
  {
    try
    {
      MdParameterDAO mdParameter = MdParameterDAO.newInstance();
      mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, entityMethod.getId());
      mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.Long");
      mdParameter.setValue(MdParameterInfo.NAME, "this is a invalid name");
      mdParameter.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Invalid name");
      mdParameter.setValue(MdParameterInfo.ORDER, "100");
      mdParameter.apply();

      mdParameter.delete();
      fail("Able to add a parameter where the name does not follow java naming conventions");
    }
    catch (InvalidIdentifierException e)
    {
      //Expected
    }
  }

  public void testInvalidParameterType()
  {
    try
    {
      MdParameterDAO mdParameter = MdParameterDAO.newInstance();
      mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, entityMethod.getId());
      mdParameter.setValue(MdParameterInfo.TYPE, "invalidType[]");
      mdParameter.setValue(MdParameterInfo.NAME, "validName");
      mdParameter.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "validName");
      mdParameter.setValue(MdParameterInfo.ORDER, "100");
      mdParameter.apply();

      mdParameter.delete();
      fail("Able to add a parameter where the type is undefined");
    }
    catch (ParameterDefinitionException_InvalidType e)
    {
      //Expected
    }
  }

  /**
   * Test parameters of the same name on different methods
   */
  public void testParameterNameOnDifferentMethods()
  {
    MdParameterDAO mdParameter = null;
    try
    {
      mdParameter = MdParameterDAO.newInstance();
      mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, entityMethod2.getId());
      mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.Long");
      mdParameter.setValue(MdParameterInfo.NAME, "seq");
      mdParameter.setValue(MdParameterInfo.ORDER, "0");
      mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sequence");
      mdParameter.apply();
    }
    catch (Exception e)
    {
      fail("Unable to create a MdParameter of an existing name on a different MdMethod");
    }

    if (mdParameter != null)
    {
      mdParameter.delete();
    }
  }

  /**
   * Test that a duplicate method name is not allowed
   * when defined on a Super Class then on a Sub Class
   */
  public void testDuplicateMethodNameOnSubClass()
  {
    MdMethodDAO mdMethod = null;

    try
    {
      mdMethod = MdMethodDAO.newInstance();
      mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, subClass.getId());
      mdMethod.setValue(MdMethodInfo.NAME, "checkout");
      mdMethod.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.Integer");
      mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Check Out");
      mdMethod.apply();

      mdMethod.delete();
      fail("Allowed to generate a duplicate method name on a subclass");
    }
    catch (MethodDefinitionException_NameExists e)
    {
      //Expected
    }
  }

  /**
   * Test that a duplicate method name is not allowed
   * when defined on a Sub Class then on a Super Class
   */
  public void testDuplicateMethodNameOnSuperClass()
  {
    MdMethodDAO subMethod = MdMethodDAO.newInstance();
    subMethod.setValue(MdMethodInfo.REF_MD_TYPE, subClass.getId());
    subMethod.setValue(MdMethodInfo.NAME, "voldemortIsEvil");
    subMethod.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.Integer");
    subMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Check Out");
    subMethod.apply();

    try
    {
      MdMethodDAO superMethod = MdMethodDAO.newInstance();
      superMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusiness.getId());
      superMethod.setValue(MdMethodInfo.NAME, "voldemortIsEvil");
      superMethod.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.Integer");
      superMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Check Out");
      superMethod.apply();

      superMethod.delete();
      subMethod.delete();

      fail("Allowed to generate a duplicate method name on a Super class");
    }
    catch (MethodDefinitionException_NameExists e)
    {
      //Expected
      subMethod.delete();
    }
  }

  public void testVoidParameterType()
  {
    try
    {
      MdParameterDAO mdParameter = MdParameterDAO.newInstance();
      mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, entityMethod.getId());
      mdParameter.setValue(MdParameterInfo.TYPE, "void");
      mdParameter.setValue(MdParameterInfo.NAME, "validName");
      mdParameter.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "validName");
      mdParameter.setValue(MdParameterInfo.ORDER, "100");
      mdParameter.apply();

      mdParameter.delete();
      fail("Able to add a parameter where the type is void");
    }
    catch (ParameterDefinitionException_InvalidType e)
    {
      //Expected
    }
  }

  public void testVoidArrayReturnType()
  {
    MdMethodDAO mdMethod = null;

    try
    {
      mdMethod = MdMethodDAO.newInstance();
      mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusiness.getId());
      mdMethod.setValue(MdMethodInfo.NAME, "nizzle");
      mdMethod.setValue(MdMethodInfo.RETURN_TYPE, "void[]");
      mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "nizzle");
      mdMethod.apply();

      mdMethod.delete();
      fail("Allowed to generate a method with a void[] return type");
    }
    catch (MethodDefinitionException_InvalidReturnType e)
    {
      //Expected
    }
  }

  public void testTiedParameterOrder()
  {
    try
    {
      MdParameterDAO mdParameter = MdParameterDAO.newInstance();
      mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, entityMethod.getId());
      mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.Long");
      mdParameter.setValue(MdParameterInfo.NAME, "leaks");
      mdParameter.setValue(MdParameterInfo.ORDER, "0");
      mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Leaks");
      mdParameter.apply();

      mdParameter.delete();
      fail("Allowed to set a Parameter with the same order as an existing parameter");
    }
    catch (ParameterDefinitionException_InvalidOrder e)
    {
      //Expected
    }
  }

  public void testDeletingAReturnTypeMdEntity()
  {
    MdBusinessDAO mdBusiness2 = MdBusinessDAO.newInstance();
    mdBusiness2.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdBusiness");
    mdBusiness2.setValue(MdBusinessInfo.PACKAGE, "test");
    mdBusiness2.setValue(MdBusinessInfo.NAME, "Class2");
    mdBusiness2.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdBusiness2.apply();

    MdMethodDAO mdMethod = MdMethodDAO.newInstance();
    mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusiness.getId());
    mdMethod.setValue(MdMethodInfo.NAME, "nizzle");
    mdMethod.setValue(MdMethodInfo.RETURN_TYPE, mdBusiness2.definesType());
    mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Nizzle");
    mdMethod.apply();

    try
    {
      mdBusiness2.delete();

      mdMethod.delete();
      fail("Able to delete an MdEntity when it is referenced by the return type of MdMethod");
    }
    catch (CompilerException e)
    {
      //Expected
    }

    mdMethod.delete();
    mdBusiness2.delete();
  }

  public void testDeletingAParameterTypeMdEntity()
  {
    MdBusinessDAO mdBusiness2 = MdBusinessDAO.newInstance();
    mdBusiness2.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdBusiness");
    mdBusiness2.setValue(MdBusinessInfo.PACKAGE, "test");
    mdBusiness2.setValue(MdBusinessInfo.NAME, "Class2");
    mdBusiness2.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdBusiness2.apply();

    MdMethodDAO mdMethod = MdMethodDAO.newInstance();
    mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusiness.getId());
    mdMethod.setValue(MdMethodInfo.NAME, "nizzle");
    mdMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Nizzle");
    mdMethod.apply();

    MdParameterDAO mdParameter = MdParameterDAO.newInstance();
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod.getId());
    mdParameter.setValue(MdParameterInfo.TYPE, mdBusiness2.definesType());
    mdParameter.setValue(MdParameterInfo.NAME, "leaks");
    mdParameter.setValue(MdParameterInfo.ORDER, "0");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Leaks");
    mdParameter.apply();

    try
    {
      mdBusiness2.delete();

      mdMethod.delete();
      fail("Able to delete an MdEntity when it is referenced by a parameter type of MdMethod");
    }
    catch (CompilerException e)
    {
      //Expected
    }

    mdMethod.delete();
    mdBusiness2.delete();
  }

  public void testMdEnumerationReference()
  {
    try
    {
      MdMethodDAO mdMethod = MdMethodDAO.newInstance();
      mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdEnum.getId());
      mdMethod.setValue(MdMethodInfo.NAME, "myMethod");
      mdMethod.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.Integer");
      mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Method Method");
      mdMethod.apply();

      fail("Able to create a MdMethod with a reference to an MdEnumeration");
    }
    catch (MethodDefinitionException_DefiningType e)
    {
      //expected
    }
  }

  public void testParameterWithExclaimationStartCharacterName()
  {
    try
    {
      MdParameterDAO mdParameter = MdParameterDAO.newInstance();
      mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, entityMethod.getId());
      mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.Long");
      mdParameter.setValue(MdParameterInfo.NAME, "!_aLong");
      mdParameter.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Invalid name");
      mdParameter.setValue(MdParameterInfo.ORDER, "100");
      mdParameter.apply();

      mdParameter.delete();
      fail("Able to add a parameter where the name does not follow java naming conventions");
    }
    catch (InvalidIdentifierException e)
    {
      //Expected
    }
  }
}
