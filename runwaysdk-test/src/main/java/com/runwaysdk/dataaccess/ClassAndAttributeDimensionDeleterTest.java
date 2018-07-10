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
package com.runwaysdk.dataaccess;

import com.runwaysdk.dataaccess.ClassAndAttributeDimensionDeleter.Item;
import com.runwaysdk.dataaccess.ClassAndAttributeDimensionDeleter.Type;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
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
public class ClassAndAttributeDimensionDeleterTest extends TestCase
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

  private static MdBusinessDAO                mdBusiness;

  private static MdDimensionDAO               mdDimension;

  private static MdAttributeCharacterDAO      mdAttributeCharacter;

  private static MdAttributeLocalCharacterDAO mdAttributeLocalCharacter;

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ClassAndAttributeDimensionDeleterTest.class);

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

  public static void classSetUp()
  {
    mdDimension = TestFixtureFactory.createMdDimension();
    mdDimension.apply();

    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdBusiness);
    mdAttributeCharacter.apply();

    mdAttributeLocalCharacter = TestFixtureFactory.addLocalCharacterAttribute(mdBusiness);
    mdAttributeLocalCharacter.apply();
  }

  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdDimension);
    TestFixtureFactory.delete(mdBusiness);
  }

  public void testDeleteMdClasses()
  {
    MdClassDAOIF mdClass = (MdClassDAOIF) MdClassDAO.get(mdBusiness.getId());

    assertEquals(1, mdClass.getMdClassDimensions().size());

    ClassAndAttributeDimensionDeleter deleter = new ClassAndAttributeDimensionDeleter();
    deleter.addItem(new Item(mdClass.definesType(), Type.CLASS));
    deleter.delete();

    try
    {
      assertEquals(0, mdClass.getMdClassDimensions().size());
    }
    finally
    {
      // Rebuild the dimension information
      new ClassAndAttributeDimensionBuilder().build();
    }
  }

  public void testDeleteMdAttributes()
  {
    MdAttributeDAOIF mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttributeCharacter.getId());

    assertEquals(1, mdAttribute.getMdAttributeDimensions().size());

    ClassAndAttributeDimensionDeleter deleter = new ClassAndAttributeDimensionDeleter();
    deleter.addItem(new Item(mdAttribute.getKey(), Type.ATTRIBUTE));
    deleter.delete();

    try
    {
      mdAttribute.getMdAttributeDimensions().size();

      fail("Found data which should not exists");
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }
    finally
    {
      // Rebuild the dimension information
      new ClassAndAttributeDimensionBuilder().build();
    }
  }
}
