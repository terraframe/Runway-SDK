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
package com.runwaysdk.business;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.business.generation.ProviderFactory;
import com.runwaysdk.business.generation.view.ContentProviderIF;
import com.runwaysdk.constants.MdControllerInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdActionDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdControllerDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;
import com.runwaysdk.util.FileIO;



public class JSPRollbackTest
{
  private static MdControllerDAO mdController;

  private static MdBusinessDAO   mdBusiness;

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setGenerateMdController(true);
    mdBusiness.apply();

    mdController = TestFixtureFactory.createMdController();
    mdController.apply();

    MdActionDAO mdAction = TestFixtureFactory.createMdAction(mdController);
    mdAction.apply();

    MdActionDAO mdParameter = TestFixtureFactory.createMdParameter(mdAction, mdBusiness);
    mdParameter.apply();

    finalizeSetup();
  }

  private static void finalizeSetup()
  {
    mdController.setValue(MdControllerInfo.STUB_SOURCE, getSource());
    mdController.apply();
  }

  private static String getSource()
  {
    String source = "package test.xmlclasses; \n" + "public class TestController extends TestControllerBase implements \n" + "{\n" + "  public TestController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean bool)\n" + "  {\n" + "    super(req, resp, bool);\n" + "  }\n" + "  @Request @Test public void testAction(" + mdBusiness.definesType() + "DTO" + " test)\n" + "  {\n" + "  }\n" + "}\n";

    return source;
  }

  private static void classTearDown()
  {
    TestFixtureFactory.delete(mdController);
    TestFixtureFactory.delete(mdBusiness);
  }

  @Request
  @Test
  public void testJSPRoleback() throws Exception
  {
    // Ensure that the jsp files exist
    ContentProviderIF provider = new ProviderFactory().getProvider(mdBusiness);
    HashMap<String, String> map = new HashMap<String, String>();

    for (String path : provider.getGeneratedFilePaths())
    {
      File content = new File(path);

      if (content.exists())
      {
        map.put(path, FileIO.readString(content));
      }
    }

    Assert.assertNotNull(map);

    // Try to delete MdBusiness, This should fail and rollback because there is
    // a compile dependency on MdBusiness in the MdController
    try
    {
      this.deleteMdBusiness();

      Assert.fail("Able to delete MdBusiness");
    }
    catch (Exception e)
    {

    }

    // Ensure that the rollback reset the content of the jsp files
    Set<String> paths = map.keySet();

    for (String path : paths)
    {
      String oldContent = map.get(path);
      String currentContent = FileIO.readString(path);

      Assert.assertEquals(oldContent, currentContent);
    }

    // Ensure that the backup files were removed when the rollback completed
    for (String path : provider.getGeneratedFilePaths())
    {
      File source = new File(path + ".backup");

      Assert.assertFalse(source.exists());
    }

  }

  @Transaction
  public void deleteMdBusiness()
  {
    mdBusiness.delete();
  }
}
