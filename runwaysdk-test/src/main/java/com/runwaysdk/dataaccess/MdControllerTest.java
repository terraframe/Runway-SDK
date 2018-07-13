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
package com.runwaysdk.dataaccess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.ProblemException;
import com.runwaysdk.business.generation.GeneratorIF;
import com.runwaysdk.business.generation.ProviderFactory;
import com.runwaysdk.business.generation.facade.ControllerGenerator;
import com.runwaysdk.business.generation.view.ContentProviderIF;
import com.runwaysdk.constants.MdActionInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdControllerInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdParameterInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.InvalidIdentifierException;
import com.runwaysdk.dataaccess.metadata.MdActionDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdControllerDAO;
import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.dataaccess.metadata.ParameterDefinitionException_InvalidType;
import com.runwaysdk.dataaccess.metadata.ParameterDefinitionException_NameExists;
import com.runwaysdk.session.Request;

public class MdControllerTest
{
  private static MdControllerDAO mdController;

  private static MdControllerDAO mdController2;

  private static MdActionDAO     mdAction;

  private static MdActionDAO     mdAction3;

  private static MdActionDAO     queryAction;

  private static MdParameterDAO  testParameter;

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    try
    {
      mdController = MdControllerDAO.newInstance();
      mdController.setValue(MdControllerInfo.NAME, "TestController");
      mdController.setValue(MdControllerInfo.PACKAGE, "com.test");
      mdController.setStructValue(MdControllerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Controller");
      mdController.setStructValue(MdControllerInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Controller");
      mdController.apply();

      mdController2 = MdControllerDAO.newInstance();
      mdController2.setValue(MdControllerInfo.NAME, "TestController2");
      mdController2.setValue(MdControllerInfo.PACKAGE, "com.test");
      mdController2.setStructValue(MdControllerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Controller2");
      mdController2.setStructValue(MdControllerInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Controller2");
      mdController2.apply();

      mdAction = MdActionDAO.newInstance();
      mdAction.setValue(MdActionInfo.NAME, "testAction");
      mdAction.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
      mdAction.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Action");
      mdAction.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Action");
      mdAction.setValue(MdActionInfo.IS_POST, MdAttributeBooleanInfo.TRUE);
      mdAction.apply();

      testParameter = MdParameterDAO.newInstance();
      testParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction.getId());
      testParameter.setValue(MdParameterInfo.TYPE, "java.lang.Long");
      testParameter.setValue(MdParameterInfo.NAME, "seq");
      testParameter.setValue(MdParameterInfo.ORDER, "0");
      testParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sequence");
      testParameter.apply();

      mdAction3 = MdActionDAO.newInstance();
      mdAction3.setValue(MdActionInfo.NAME, "testAction3");
      mdAction3.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
      mdAction3.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Action3");
      mdAction3.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Action3");
      mdAction3.setValue(MdActionInfo.IS_POST, MdAttributeBooleanInfo.FALSE);
      mdAction3.apply();

      queryAction = MdActionDAO.newInstance();
      queryAction.setValue(MdActionInfo.NAME, "postAction");
      queryAction.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
      queryAction.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Post Action3");
      queryAction.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Post Action3");
      queryAction.setValue(MdActionInfo.IS_POST, MdAttributeBooleanInfo.FALSE);
      queryAction.setValue(MdActionInfo.IS_QUERY, MdAttributeBooleanInfo.TRUE);
      queryAction.apply();
    }
    catch (ProblemException e)
    {
    }

    finalizeSetup();
  }

  private static void finalizeSetup()
  {
    // String stubSource = getControllerSource();
    // mdController.setValue(MdControllerInfo.STUB_SOURCE, stubSource);
    // mdController.apply();
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdAction);
    TestFixtureFactory.delete(mdController);
    TestFixtureFactory.delete(mdController2);
  }

  @Request
  @Test
  public void testGetMdController()
  {
    MdControllerDAOIF mdControllerIF = MdControllerDAO.get(mdController.getId());

    Assert.assertEquals("TestController", mdControllerIF.getValue(MdControllerInfo.NAME));
    Assert.assertEquals("com.test", mdControllerIF.getValue(MdControllerInfo.PACKAGE));
    Assert.assertEquals("Test Controller", mdControllerIF.getStructValue(MdControllerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    Assert.assertEquals("Test Controller", mdControllerIF.getStructValue(MdControllerInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
  }

  @Request
  @Test
  public void testGetMdAction()
  {
    MdActionDAOIF mdActionIF = MdActionDAO.get(mdAction.getId());

    Assert.assertEquals("testAction", mdActionIF.getValue(MdActionInfo.NAME));
    Assert.assertEquals(mdController.getId(), mdActionIF.getValue(MdActionInfo.ENCLOSING_MD_CONTROLLER));
    Assert.assertEquals("Test Action", mdActionIF.getStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    Assert.assertEquals("Test Action", mdActionIF.getStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
  }

  @Request
  @Test
  public void testGetMdActions()
  {
    MdControllerDAOIF mdControllerIF = MdControllerDAO.get(mdController.getId());

    List<String> ids = new LinkedList<String>();

    for (MdActionDAOIF mdAction : mdControllerIF.getMdActionDAOs())
    {
      ids.add(mdAction.getId());
    }

    Assert.assertEquals(3, ids.size());
    Assert.assertTrue(ids.contains(mdAction.getId()));
    Assert.assertTrue(ids.contains(mdAction3.getId()));
  }

  @Request
  @Test
  public void testGetMultipleMdActions()
  {
    MdActionDAO mdAction2 = MdActionDAO.newInstance();
    mdAction2.setValue(MdActionInfo.NAME, "testAction2");
    mdAction2.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
    mdAction2.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Action2");
    mdAction2.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Action2");
    String id = mdAction2.apply();

    List<String> ids = new LinkedList<String>();

    try
    {
      MdControllerDAOIF mdControllerIF = MdControllerDAO.get(mdController.getId());

      for (MdActionDAOIF mdAction : mdControllerIF.getMdActionDAOs())
      {
        ids.add(mdAction.getId());
      }

    }
    finally
    {
      mdAction2.delete();
    }

    Assert.assertEquals(4, ids.size());
    Assert.assertTrue(ids.contains(id));
    Assert.assertTrue(ids.contains(mdAction.getId()));
    Assert.assertTrue(ids.contains(mdAction3.getId()));
  }

  @Request
  @Test
  public void testGetParameters()
  {
    List<MdParameterDAOIF> mdParameters = mdAction.getMdParameterDAOs();

    Assert.assertEquals(1, mdParameters.size());
    Assert.assertEquals(testParameter.getId(), mdParameters.get(0).getId());
  }

  @Request
  @Test
  public void testInvalidMdActionName()
  {
    try
    {
      MdActionDAO invalidAction = MdActionDAO.newInstance();
      invalidAction.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
      invalidAction.setValue(MdActionInfo.NAME, "This is an invalid name.");
      invalidAction.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Invalid Action");
      invalidAction.apply();

      Assert.fail("Able to define a MdAction with a name that does not follow java naming conventions");
    }
    catch (InvalidIdentifierException e)
    {
      // Expected
    }
  }

  @Request
  @Test
  public void testMdActionMultiApply()
  {
    MdActionDAO mdAction2 = MdActionDAO.newInstance();
    mdAction2.setValue(MdActionInfo.NAME, "testAction2");
    mdAction2.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
    mdAction2.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Action2");
    mdAction2.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Action2");
    mdAction2.apply();

    try
    {
      mdAction2.apply();
    }
    catch (Exception e)
    {
      Assert.fail(e.getLocalizedMessage());
    }
    finally
    {
      mdAction2.delete();
    }
  }

  @Request
  @Test
  public void testNamingConstraint()
  {
    try
    {
      MdActionDAO duplicateMdAction = MdActionDAO.newInstance();
      duplicateMdAction.setValue(MdActionInfo.NAME, "testAction");
      duplicateMdAction.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
      duplicateMdAction.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Action");
      duplicateMdAction.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Action");
      duplicateMdAction.apply();

      Assert.fail("Able to create a MdAction with duplicate name");
    }
    catch (Exception e)
    {
      // Excepted
    }
  }

  /**
   * Ensure it is possible to create duplicate action names on different
   * MdControllers
   */
  @Request
  @Test
  public void testNamingConstraint2()
  {
    MdActionDAO duplicateMdAction = MdActionDAO.newInstance();

    try
    {
      duplicateMdAction.setValue(MdActionInfo.NAME, "testAction");
      duplicateMdAction.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController2.getId());
      duplicateMdAction.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Action");
      duplicateMdAction.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Action");
      duplicateMdAction.apply();
    }
    catch (Exception e)
    {
      Assert.fail(e.getLocalizedMessage());
    }
    finally
    {
      if (duplicateMdAction.isAppliedToDB())
      {
        duplicateMdAction.delete();
      }
    }
  }

  @Request
  @Test
  public void testInvalidParameterName()
  {
    try
    {
      MdParameterDAO mdParameter = MdParameterDAO.newInstance();
      mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction.getId());
      mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.Long");
      mdParameter.setValue(MdParameterInfo.NAME, "this is a invalid name");
      mdParameter.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Invalid name");
      mdParameter.setValue(MdParameterInfo.ORDER, "100");
      mdParameter.apply();

      mdParameter.delete();
      Assert.fail("Able to add a parameter where the name does not follow java naming conventions");
    }
    catch (InvalidIdentifierException e)
    {
      // Expected
    }
  }

  @Request
  @Test
  public void testInvalidParameterType()
  {
    try
    {
      MdParameterDAO mdParameter = MdParameterDAO.newInstance();
      mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction.getId());
      mdParameter.setValue(MdParameterInfo.TYPE, "invalidType[]");
      mdParameter.setValue(MdParameterInfo.NAME, "validName");
      mdParameter.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "validName");
      mdParameter.setValue(MdParameterInfo.ORDER, "100");
      mdParameter.apply();

      mdParameter.delete();
      Assert.fail("Able to add a parameter where the type is undefined");
    }
    catch (ParameterDefinitionException_InvalidType e)
    {
      // Expected
    }
  }

  @Request
  @Test
  public void testArrayParameterType()
  {
    try
    {
      MdParameterDAO mdParameter = MdParameterDAO.newInstance();
      mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction.getId());
      mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.Integer[][]");
      mdParameter.setValue(MdParameterInfo.NAME, "validName");
      mdParameter.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "validName");
      mdParameter.setValue(MdParameterInfo.ORDER, "100");
      mdParameter.apply();

      mdParameter.delete();
      Assert.fail("Able to add a multi-dimensional array as a action parameter");
    }
    catch (ParameterDefinitionException_InvalidType e)
    {
      // Expected
    }
  }

  /**
   * Test parameters of the same name on different methods
   */
  @Request
  @Test
  public void testParameterNameOnDifferentMethods()
  {
    MdParameterDAO mdParameter = null;
    try
    {
      mdParameter = MdParameterDAO.newInstance();
      mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction3.getId());
      mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.Long");
      mdParameter.setValue(MdParameterInfo.NAME, "seq");
      mdParameter.setValue(MdParameterInfo.ORDER, "0");
      mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sequence");
      mdParameter.apply();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      Assert.fail("Unable to create a MdParameter of an existing name on a different MdMethod: " + e.getLocalizedMessage());
    }

    if (mdParameter != null)
    {
      mdParameter.delete();
    }
  }

  @Request
  @Test
  public void testDuplicateParameter()
  {
    try
    {
      MdParameterDAO mdParameter = MdParameterDAO.newInstance();
      mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction.getId());
      mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.Long");
      mdParameter.setValue(MdParameterInfo.NAME, "seq");
      mdParameter.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sequence");
      mdParameter.setValue(MdParameterInfo.ORDER, "100");
      mdParameter.apply();

      mdParameter.delete();
      Assert.fail("Able to add a parameter of identical name as another parameter on the same method");
    }
    catch (ParameterDefinitionException_NameExists e)
    {
      // Expected
    }
  }

  @Request
  @Test
  public void testEnclosingTypeForParameters()
  {
    MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, "com.test");
    mdBusiness.setValue(MdBusinessInfo.NAME, "TestBusiness");
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Business");
    mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Business.");
    mdBusiness.apply();

    try
    {
      MdParameterDAO mdParameter = MdParameterDAO.newInstance();
      mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdBusiness.getId());
      mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.Long");
      mdParameter.setValue(MdParameterInfo.NAME, "seq");
      mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sequence");
      mdParameter.setValue(MdParameterInfo.ORDER, "100");
      mdParameter.apply();

      Assert.fail("Able to add a MdParameter to an invalid enclosing type");
    }
    catch (Exception e)
    {
      // Expected to land here
    }
    finally
    {
      mdBusiness.delete();
    }
  }

  @Request
  @Test
  public void testGetActionParameters()
  {
    MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, "com.test");
    mdBusiness.setValue(MdBusinessInfo.NAME, "TestBusiness");
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Business");
    mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Business.");
    mdBusiness.apply();

    try
    {
      MdParameterDAO mdParameter = MdParameterDAO.newInstance();
      mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction3.getId());
      mdParameter.setValue(MdParameterInfo.TYPE, mdBusiness.definesType());
      mdParameter.setValue(MdParameterInfo.NAME, "seq");
      mdParameter.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sequence");
      mdParameter.setValue(MdParameterInfo.ORDER, "100");
      mdParameter.apply();

      Assert.fail("Able to add a defined type to a MdParameter on a GET action");
    }
    catch (ParameterDefinitionException_InvalidType e)
    {
      // Expected to land here
    }
    finally
    {
      mdBusiness.delete();
    }

  }

  @Request
  @Test
  public void testGetActionArray()
  {
    try
    {
      MdParameterDAO mdParameter = MdParameterDAO.newInstance();
      mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction3.getId());
      mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.Long[]");
      mdParameter.setValue(MdParameterInfo.NAME, "seq");
      mdParameter.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sequence");
      mdParameter.setValue(MdParameterInfo.ORDER, "100");
      mdParameter.apply();

      Assert.fail("Able to add a primitive array to a MdParameter on a GET action");
    }
    catch (ParameterDefinitionException_InvalidType e)
    {
      // Expected to land here
    }
  }

  /**
   * Ensure that the query parameters are defined
   */
  @Request
  @Test
  public void testQueryAction()
  {
    boolean sortAttribute = false;
    boolean isAscending = false;
    boolean pageNumber = false;
    boolean pageSize = false;

    try
    {
      for (MdParameterDAOIF mdParameter : queryAction.getMdParameterDAOs())
      {
        if (mdParameter.getValue(MdParameterInfo.NAME).equals(MdActionInfo.SORT_ATTRIBUTE))
        {
          sortAttribute = true;
        }

        if (mdParameter.getValue(MdParameterInfo.NAME).equals(MdActionInfo.IS_ASCENDING))
        {
          isAscending = true;
        }

        if (mdParameter.getValue(MdParameterInfo.NAME).equals(MdActionInfo.PAGE_NUMBER))
        {
          pageNumber = true;
        }

        if (mdParameter.getValue(MdParameterInfo.NAME).equals(MdActionInfo.PAGE_SIZE))
        {
          pageSize = true;
        }
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }

    Assert.assertTrue(sortAttribute);
    Assert.assertTrue(isAscending);
    Assert.assertTrue(pageNumber);
    Assert.assertTrue(pageSize);
  }

  @Request
  @Test
  public void testFileGeneration()
  {
    for (GeneratorIF gen : mdController.getGenerators())
    {
      ControllerGenerator generator = (ControllerGenerator) gen;

      File source = new File(generator.getPath());

      if (!source.exists())
      {
        Assert.fail("File: " + source.getAbsolutePath() + " was not generated.");
      }
    }
  }

  @Request
  @Test
  public void testOverwriteGenericJSPs() throws IOException
  {
    String testString = "This is a test file";

    MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, "com.test");
    mdBusiness.setValue(MdBusinessInfo.NAME, "TestBusiness");
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Business");
    mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Business.");

    ContentProviderIF provider = new ProviderFactory().getProvider(mdBusiness);

    for (String path : provider.getGeneratedFilePaths())
    {
      File file = new File(path);
      if (!file.getParentFile().exists())
      {
        file.getParentFile().mkdirs();
      }

      BufferedWriter writer = new BufferedWriter(new FileWriter(file));

      writer.write(testString);
      writer.flush();
      writer.close();

      Assert.assertTrue(file.exists());
    }

    mdBusiness.apply();

    try
    {
      for (String path : provider.getGeneratedFilePaths())
      {
        BufferedReader reader = new BufferedReader(new FileReader(path));

        try
        {
          Assert.assertEquals(testString, reader.readLine());
        }
        finally
        {
          reader.close();
        }
      }
    }
    finally
    {
      mdBusiness.delete();
    }

  }
}
