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
/**
*
*/
package com.runwaysdk.query;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiTermDAO;
import com.runwaysdk.session.Request;

import junit.extensions.TestSetup;
import junit.framework.TestResult;
import junit.framework.TestSuite;

public class MultiTermQueryTest extends AbstractMultiReferenceQueryTest
{
  @Request
  @BeforeClass
  public static void classSetup()
  {
    AbstractMultiReferenceQueryTest.classSetUp(MdAttributeMultiTermDAO.newInstance());
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    AbstractMultiReferenceQueryTest.classTearDown();
  }

  /**
   * @param query
   * @return
   */
  protected AttributeMultiReference getQueryAttribute(BusinessDAOQuery query)
  {
    return query.aMultiTerm(this.getMdAttribute().definesAttribute());
  }
}
