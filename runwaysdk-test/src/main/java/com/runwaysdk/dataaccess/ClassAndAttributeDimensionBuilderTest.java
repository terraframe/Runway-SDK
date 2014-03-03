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

import java.util.List;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;
import com.runwaysdk.query.OIterator;

public class ClassAndAttributeDimensionBuilderTest extends TestCase
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

  private static MdBusinessDAO           mdBusiness;

  private static MdDimensionDAO          mdDimension;

  private static MdAttributeCharacterDAO mdAttributeCharacter;

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ClassAndAttributeDimensionBuilderTest.class);

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
  }

  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdDimension);
    TestFixtureFactory.delete(mdBusiness);
  }

  public void testGetMdClasses()
  {
    MdClassDAOIF mdClass = (MdClassDAOIF) MdClassDAO.get(mdBusiness.getId());
    List<MdClassDimensionDAOIF> mdClassDimensions = mdClass.getMdClassDimensions();

    for (MdClassDimensionDAOIF mdClassDimension : mdClassDimensions)
    {
      TestFixtureFactory.delete(mdClassDimension);
    }

    assertEquals(0, mdClass.getMdClassDimensions().size());

    OIterator<BusinessDAOIF> it = new ClassAndAttributeDimensionBuilder().getMdClasses();

    try
    {
      List<BusinessDAOIF> mdClasses = it.getAll();

      assertEquals(1, mdClasses.size());
    }
    finally
    {
      it.close();
    }
  }

  public void testGetMdAttributes()
  {
    MdAttributeDAOIF mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttributeCharacter.getId());
    List<MdAttributeDimensionDAOIF> mdAttributeDimensions = mdAttribute.getMdAttributeDimensions();

    for (MdAttributeDimensionDAOIF mdAttributeDimension : mdAttributeDimensions)
    {
      TestFixtureFactory.delete(mdAttributeDimension);
    }


    // Refresh the object, as it will contain an updated dimension cache
    mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttribute.getId());
    
    assertEquals(0, mdAttribute.getMdAttributeDimensions().size());

    OIterator<BusinessDAOIF> it = new ClassAndAttributeDimensionBuilder().getMdAttributes();

    try
    {
      List<BusinessDAOIF> mdAttributees = it.getAll();

      assertEquals(1, mdAttributees.size());
    }
    finally
    {
      it.close();
    }
  }

  public void testBuildMdClasses()
  {
    MdClassDAOIF mdClass = (MdClassDAOIF) MdClassDAO.get(mdBusiness.getId());
    List<MdClassDimensionDAOIF> mdClassDimensions = mdClass.getMdClassDimensions();

    for (MdClassDimensionDAOIF mdClassDimension : mdClassDimensions)
    {
      TestFixtureFactory.delete(mdClassDimension);
    }

    assertEquals(0, mdClass.getMdClassDimensions().size());

    new ClassAndAttributeDimensionBuilder().build();

    assertEquals(1, mdClass.getMdClassDimensions().size());
  }

  public void testBuildMdAttributes()
  {
    MdAttributeDAOIF mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttributeCharacter.getId());
    List<MdAttributeDimensionDAOIF> mdAttributeDimensions = mdAttribute.getMdAttributeDimensions();

    for (MdAttributeDimensionDAOIF mdAttributeDimension : mdAttributeDimensions)
    {
      TestFixtureFactory.delete(mdAttributeDimension);
    }

    // Refresh the object, as it will contain an updated dimension cache
    mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttribute.getId());
    
    assertEquals(0, mdAttribute.getMdAttributeDimensions().size());

    new ClassAndAttributeDimensionBuilder().build();

    assertEquals(1, mdAttribute.getMdAttributeDimensions().size());
  }

  public void testDifferentSiteMasterMdClass()
  {
    String siteMaster = CommonProperties.getDomain() + "Different";
    MdClassDAOIF mdClass = (MdClassDAOIF) MdClassDAO.get(mdBusiness.getId());
    List<MdClassDimensionDAOIF> mdClassDimensions = mdClass.getMdClassDimensions();

    for (MdClassDimensionDAOIF mdClassDimension : mdClassDimensions)
    {
      TestFixtureFactory.delete(mdClassDimension);
    }

    assertEquals(0, mdClass.getMdClassDimensions().size());

    new ClassAndAttributeDimensionBuilder(siteMaster, false).build();

    List<MdClassDimensionDAOIF> test = mdClass.getMdClassDimensions();
    assertEquals(1, test.size());
    assertEquals(siteMaster, test.get(0).getSiteMaster());
  }

  public void testDifferentSiteMasterMdAttribute()
  {
    String siteMaster = CommonProperties.getDomain() + "Different";
    MdAttributeDAOIF mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttributeCharacter.getId());
    List<MdAttributeDimensionDAOIF> mdAttributeDimensions = mdAttribute.getMdAttributeDimensions();

    for (MdAttributeDimensionDAOIF mdAttributeDimension : mdAttributeDimensions)
    {
      TestFixtureFactory.delete(mdAttributeDimension);
    }

    // Refresh the object, as it will contain an updated dimension cache
    mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttribute.getId());
    
    assertEquals(0, mdAttribute.getMdAttributeDimensions().size());

    new ClassAndAttributeDimensionBuilder(siteMaster, false).build();

    List<MdAttributeDimensionDAOIF> test = mdAttribute.getMdAttributeDimensions();
    assertEquals(1, test.size());
    assertEquals(siteMaster, test.get(0).getSiteMaster());
  }

  public void testSequenceNumberMdClass()
  {
    MdClassDAOIF mdClass = (MdClassDAOIF) MdClassDAO.get(mdBusiness.getId());
    List<MdClassDimensionDAOIF> mdClassDimensions = mdClass.getMdClassDimensions();

    for (MdClassDimensionDAOIF mdClassDimension : mdClassDimensions)
    {
      TestFixtureFactory.delete(mdClassDimension);
    }

    assertEquals(0, mdClass.getMdClassDimensions().size());

    new ClassAndAttributeDimensionBuilder().build();

    List<MdClassDimensionDAOIF> test = mdClass.getMdClassDimensions();
    assertEquals(1, test.size());
    assertEquals(0L, test.get(0).getSequence());
  }

  public void testSequenceMdAttribute()
  {
    MdAttributeDAOIF mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttributeCharacter.getId());
    List<MdAttributeDimensionDAOIF> mdAttributeDimensions = mdAttribute.getMdAttributeDimensions();

    for (MdAttributeDimensionDAOIF mdAttributeDimension : mdAttributeDimensions)
    {
      TestFixtureFactory.delete(mdAttributeDimension);
    }

    // Refresh the object, as it will contain an updated dimension cache
    mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttribute.getId());
    
    assertEquals(0, mdAttribute.getMdAttributeDimensions().size());

    new ClassAndAttributeDimensionBuilder().build();

    List<MdAttributeDimensionDAOIF> test = mdAttribute.getMdAttributeDimensions();
    assertEquals(1, test.size());
    assertEquals(0L, test.get(0).getSequence());
  }

  public void testMainDifferentSiteMasterMdClass()
  {
    String siteMaster = CommonProperties.getDomain() + "Different";
    MdClassDAOIF mdClass = (MdClassDAOIF) MdClassDAO.get(mdBusiness.getId());
    List<MdClassDimensionDAOIF> mdClassDimensions = mdClass.getMdClassDimensions();

    for (MdClassDimensionDAOIF mdClassDimension : mdClassDimensions)
    {
      TestFixtureFactory.delete(mdClassDimension);
    }

    assertEquals(0, mdClass.getMdClassDimensions().size());

    ClassAndAttributeDimensionBuilder.main(new String[] { siteMaster });

    List<MdClassDimensionDAOIF> test = mdClass.getMdClassDimensions();
    assertEquals(1, test.size());
    assertEquals(siteMaster, test.get(0).getSiteMaster());
  }

  public void testMainDifferentSiteMasterMdAttribute()
  {
    String siteMaster = CommonProperties.getDomain() + "Different";
    MdAttributeDAOIF mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttributeCharacter.getId());
    List<MdAttributeDimensionDAOIF> mdAttributeDimensions = mdAttribute.getMdAttributeDimensions();

    for (MdAttributeDimensionDAOIF mdAttributeDimension : mdAttributeDimensions)
    {
      TestFixtureFactory.delete(mdAttributeDimension);
    }

    // Refresh the object, as it will contain an updated dimension cache
    mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttribute.getId());

    assertEquals(0, mdAttribute.getMdAttributeDimensions().size());

    ClassAndAttributeDimensionBuilder.main(new String[] { siteMaster });

    List<MdAttributeDimensionDAOIF> test = mdAttribute.getMdAttributeDimensions();
    assertEquals(1, test.size());
    assertEquals(siteMaster, test.get(0).getSiteMaster());
  }
}
