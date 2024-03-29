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
package com.runwaysdk.business;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.runwaysdk.ClasspathTestRunner;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.dataaccess.metadata.MdSessionDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.session.Request;

@RunWith(ClasspathTestRunner.class)
public class ViewGenTest extends SessionComponentGenTest
{
  @Request
  @BeforeClass
  public static void classSetUp()
  {
    collection = MdViewDAO.newInstance();
    superMdSessionAttributeNameHack = MdViewInfo.SUPER_MD_VIEW;
    collectionSub = MdViewDAO.newInstance();
    sessionClassSetUp();
  }

  protected MdSessionDAO newMdSession()
  {
    return MdViewDAO.newInstance();
  }

  protected String buildGetMethod()
  {
    return "return (Collection) " + View.class.getName() + ".get(oid);\n";
  }
}
