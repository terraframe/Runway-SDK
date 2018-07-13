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
package com.runwaysdk.facade;

import java.util.Locale;

import org.junit.BeforeClass;

import com.runwaysdk.ClientSession;
import com.runwaysdk.business.Util;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.MdUtilInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;


public class UtilDTOAdapterTest extends SessionDTOAdapterTest
{
  @BeforeClass
  public static void classSetUp()
  {
    label = "default";
    systemSession = ClientSession.createUserSession(label, ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
    clientRequest = systemSession.getRequest();

    moreSetup();

    classSetUpRequest();
    finalizeSetup();
  }


  protected static void moreSetup()
  {
    source = "package com.test.controller;\n" + "public class " + parentMdSessionTypeName + " extends " + parentMdSessionTypeName + TypeGeneratorInfo.BASE_SUFFIX + " \n" + "{\n" + "public " + parentMdSessionTypeName + "()" + "{" + "   super();" + "}\n" + "public static " + parentMdSessionTypeName + " get(String id)" + "{\n" + "  return (" + parentMdSessionTypeName + ") " + Util.class.getName() + ".get(id);" + "}\n" + "public String toString()" + "{" + "  return \"" + toStringPrepend + "\" + getId();" + "}\n" + "  public void apply()\n" + "  {\n" + "    " + SessionIF.class.getName() + " session = " + Session.class.getName() + ".getCurrentSession();" + "    " + SingleActorDAOIF.class.getName() + " userIF = session.getUser();" + "    this.setOwner(userIF);" + "    super.apply();" + "  }\n"
        + "}";

    childMdSession = clientRequest.newBusiness(MdUtilInfo.CLASS);
    parentMdSession = clientRequest.newBusiness(MdUtilInfo.CLASS);
  }
}
