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
/**
*
*/
package com.runwaysdk.business.ontology;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
 ******************************************************************************/
public class DefaultStrategyTest extends AbstractOntologyStrategyTest
{
  public DefaultStrategyTest() throws Exception
  {
    super();
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(DefaultStrategyTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp() throws Exception
      {
        // classSetUp();
      }

      protected void tearDown() throws Exception
      {
        classTearDown();
      }
    };

    return wrapper;
  }

  @Override
  public Class<?> getOntologyStrategy()
  {
    return DefaultStrategy.class;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.business.ontology.AbstractOntologyStrategyTest#
   * getInitializeStrategySource()
   */
  @Override
  public String getInitializeStrategySource()
  {
    return "";
  }

}
