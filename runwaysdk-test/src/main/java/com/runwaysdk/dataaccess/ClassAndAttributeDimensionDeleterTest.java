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
/**
*
*/
package com.runwaysdk.dataaccess;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

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
import com.runwaysdk.session.Request;

public class ClassAndAttributeDimensionDeleterTest
{
  private static MdBusinessDAO                mdBusiness;

  private static MdDimensionDAO               mdDimension;

  private static MdAttributeCharacterDAO      mdAttributeCharacter;

  private static MdAttributeLocalCharacterDAO mdAttributeLocalCharacter;

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    org.junit.Assume.assumeFalse("true".equals(System.getenv("RUNWAY_TEST_IGNORE_DIMENSION_TESTS")));
    
    mdDimension = TestFixtureFactory.createMdDimension();
    mdDimension.apply();

    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdBusiness);
    mdAttributeCharacter.apply();

    mdAttributeLocalCharacter = TestFixtureFactory.addLocalCharacterAttribute(mdBusiness);
    mdAttributeLocalCharacter.apply();
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdDimension);
    TestFixtureFactory.delete(mdBusiness);
  }

  @Request
  @Test
  public void testDeleteMdClasses()
  {
    MdClassDAOIF mdClass = (MdClassDAOIF) MdClassDAO.get(mdBusiness.getOid());

    Assert.assertEquals(1, mdClass.getMdClassDimensions().size());

    ClassAndAttributeDimensionDeleter deleter = new ClassAndAttributeDimensionDeleter();
    deleter.addItem(new Item(mdClass.definesType(), Type.CLASS));
    deleter.delete();

    try
    {
      Assert.assertEquals(0, mdClass.getMdClassDimensions().size());
    }
    finally
    {
      // Rebuild the dimension information
      new ClassAndAttributeDimensionBuilder().build();
    }
  }

  @Request
  @Test
  public void testDeleteMdAttributes()
  {
    MdAttributeDAOIF mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttributeCharacter.getOid());

    Assert.assertEquals(1, mdAttribute.getMdAttributeDimensions().size());

    ClassAndAttributeDimensionDeleter deleter = new ClassAndAttributeDimensionDeleter();
    deleter.addItem(new Item(mdAttribute.getKey(), Type.ATTRIBUTE));
    deleter.delete();

    try
    {
      mdAttribute.getMdAttributeDimensions().size();

      Assert.fail("Found data which should not exists");
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
