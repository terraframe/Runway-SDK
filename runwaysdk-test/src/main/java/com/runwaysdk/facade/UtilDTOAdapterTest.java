/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.facade;

import java.util.Locale;

import junit.extensions.TestSetup;
import junit.framework.Test;

import com.runwaysdk.ClientSession;
import com.runwaysdk.TestSuiteTF;
import com.runwaysdk.business.Util;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.MdUtilInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;

public class UtilDTOAdapterTest extends SessionDTOAdapterTest
{
  public static Test suite()
  {
    TestSuiteTF suite = new TestSuiteTF();
    suite.addTestSuite(UtilDTOAdapterTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
        label = "default";
        systemSession = ClientSession.createUserSession(label, ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[]{CommonProperties.getDefaultLocale()});
        clientRequest = systemSession.getRequest();

        moreSetup();

        classSetUp();
        finalizeSetup();
      }

      protected void tearDown()
      {
        classTearDown();
      }
    };

    return wrapper;
  }

  protected static void moreSetup()
  {
    source = "package com.test.controller;\n" + "public class " + parentMdSessionTypeName + " extends " + parentMdSessionTypeName + TypeGeneratorInfo.BASE_SUFFIX + " implements " + Reloadable.class.getName() + "\n" + "{\n" + "public " + parentMdSessionTypeName + "()" + "{" + "   super();" + "}\n"
        + "public static " + parentMdSessionTypeName + " get(String id)" + "{\n" + "  return (" + parentMdSessionTypeName + ") " + Util.class.getName() + ".get(id);" + "}\n" + "public String toString()" + "{" + "  return \"" + toStringPrepend + "\" + getId();" + "}\n" + "  public void apply()\n"
        + "  {\n" + "    " + SessionIF.class.getName() + " session = " + Session.class.getName() + ".getCurrentSession();" + "    " + UserDAOIF.class.getName() + " userIF = session.getUser();" + "    this.setOwner(userIF);" + "    super.apply();" + "  }\n" + "}";

    childMdSession = clientRequest.newBusiness(MdUtilInfo.CLASS);
    parentMdSession = clientRequest.newBusiness(MdUtilInfo.CLASS);
  }
}
