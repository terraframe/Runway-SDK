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
package com.runwaysdk.dataaccess;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdLocalStructDAO;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.session.Request;

public class ClassAndAttributeDimensionBuilderTest
{
  private static MdBusinessDAO                mdBusiness;

  private static MdDimensionDAO               mdDimension;

  private static MdAttributeCharacterDAO      mdAttributeCharacter;

  private static MdAttributeLocalCharacterDAO mdAttributeLocalCharacter;

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    mdDimension = TestFixtureFactory.createMdDimension();
    mdDimension.apply();

    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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
  public void testGetMdClasses()
  {
    MdClassDAOIF mdClass = (MdClassDAOIF) MdClassDAO.get(mdBusiness.getOid());
    List<MdClassDimensionDAOIF> mdClassDimensions = mdClass.getMdClassDimensions();

    for (MdClassDimensionDAOIF mdClassDimension : mdClassDimensions)
    {
      TestFixtureFactory.delete(mdClassDimension);
    }

    Assert.assertEquals(0, mdClass.getMdClassDimensions().size());

    OIterator<BusinessDAOIF> it = new ClassAndAttributeDimensionBuilder().getMdClasses();

    try
    {
      List<BusinessDAOIF> mdClasses = it.getAll();

      Assert.assertEquals(1, mdClasses.size());
    }
    finally
    {
      it.close();
    }
  }

  @Request
  @Test
  public void testGetMdAttributes()
  {
    MdAttributeDAOIF mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttributeCharacter.getOid());
    List<MdAttributeDimensionDAOIF> mdAttributeDimensions = mdAttribute.getMdAttributeDimensions();

    for (MdAttributeDimensionDAOIF mdAttributeDimension : mdAttributeDimensions)
    {
      TestFixtureFactory.delete(mdAttributeDimension);
    }

    // Refresh the object, as it will contain an updated dimension cache
    mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttribute.getOid());

    Assert.assertEquals(0, mdAttribute.getMdAttributeDimensions().size());

    OIterator<BusinessDAOIF> it = new ClassAndAttributeDimensionBuilder().getMdAttributes();

    try
    {
      List<BusinessDAOIF> mdAttributees = it.getAll();

      Assert.assertEquals(1, mdAttributees.size());
    }
    finally
    {
      it.close();
    }
  }

  @Request
  @Test
  public void testBuildMdClasses()
  {
    MdClassDAOIF mdClass = (MdClassDAOIF) MdClassDAO.get(mdBusiness.getOid());
    List<MdClassDimensionDAOIF> mdClassDimensions = mdClass.getMdClassDimensions();

    for (MdClassDimensionDAOIF mdClassDimension : mdClassDimensions)
    {
      TestFixtureFactory.delete(mdClassDimension);
    }

    Assert.assertEquals(0, mdClass.getMdClassDimensions().size());

    new ClassAndAttributeDimensionBuilder().build();

    Assert.assertEquals(1, mdClass.getMdClassDimensions().size());
  }

  @Request
  @Test
  public void testBuildMdAttributes()
  {
    MdAttributeDAOIF mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttributeCharacter.getOid());
    List<MdAttributeDimensionDAOIF> mdAttributeDimensions = mdAttribute.getMdAttributeDimensions();

    for (MdAttributeDimensionDAOIF mdAttributeDimension : mdAttributeDimensions)
    {
      TestFixtureFactory.delete(mdAttributeDimension);
    }

    // Refresh the object, as it will contain an updated dimension cache
    mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttribute.getOid());

    Assert.assertEquals(0, mdAttribute.getMdAttributeDimensions().size());

    new ClassAndAttributeDimensionBuilder().build();

    // Refresh the object, as it will contain an updated dimension cache
    mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttribute.getOid());

    Assert.assertEquals(1, mdAttribute.getMdAttributeDimensions().size());
  }

  @Request
  @Test
  public void testBuildMdLocalAttributes()
  {
    MdAttributeLocalCharacterDAO mdAttribute = ( (MdAttributeLocalCharacterDAOIF) MdAttributeDAO.get(mdAttributeLocalCharacter.getOid()) ).getBusinessDAO();
    MdLocalStructDAO struct = mdAttribute.getMdStructDAOIF().getBusinessDAO();

    BusinessDAO defaultLocale = struct.definesAttribute(mdDimension.getDefaultLocaleAttributeName()).getBusinessDAO();
    defaultLocale.delete();

    // Make sure this is false
    Assert.assertFalse(mdAttribute.definesDefaultLocale(mdDimension));

    new ClassAndAttributeDimensionBuilder().build();

    Assert.assertTrue(mdAttribute.definesDefaultLocale(mdDimension));
  }

  @Request
  @Test
  public void testDifferentSiteMasterMdClass()
  {
    String siteMaster = CommonProperties.getDomain() + "Different";
    MdClassDAOIF mdClass = (MdClassDAOIF) MdClassDAO.get(mdBusiness.getOid());
    List<MdClassDimensionDAOIF> mdClassDimensions = mdClass.getMdClassDimensions();

    for (MdClassDimensionDAOIF mdClassDimension : mdClassDimensions)
    {
      TestFixtureFactory.delete(mdClassDimension);
    }

    Assert.assertEquals(0, mdClass.getMdClassDimensions().size());

    new ClassAndAttributeDimensionBuilder(siteMaster, false).build();

    List<MdClassDimensionDAOIF> test = mdClass.getMdClassDimensions();
    Assert.assertEquals(1, test.size());
    Assert.assertEquals(siteMaster, test.get(0).getSiteMaster());
  }

  @Request
  @Test
  public void testDifferentSiteMasterMdAttribute()
  {
    String siteMaster = CommonProperties.getDomain() + "Different";
    MdAttributeDAOIF mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttributeCharacter.getOid());
    List<MdAttributeDimensionDAOIF> mdAttributeDimensions = mdAttribute.getMdAttributeDimensions();

    for (MdAttributeDimensionDAOIF mdAttributeDimension : mdAttributeDimensions)
    {
      TestFixtureFactory.delete(mdAttributeDimension);
    }

    // Refresh the object, as it will contain an updated dimension cache
    mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttribute.getOid());

    Assert.assertEquals(0, mdAttribute.getMdAttributeDimensions().size());

    new ClassAndAttributeDimensionBuilder(siteMaster, false).build();

    // Refresh the object, as it will contain an updated dimension cache
    mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttribute.getOid());

    List<MdAttributeDimensionDAOIF> test = mdAttribute.getMdAttributeDimensions();
    Assert.assertEquals(1, test.size());
    Assert.assertEquals(siteMaster, test.get(0).getSiteMaster());
  }

  @Request
  @Test
  public void testSequenceNumberMdClass()
  {
    MdClassDAOIF mdClass = (MdClassDAOIF) MdClassDAO.get(mdBusiness.getOid());
    List<MdClassDimensionDAOIF> mdClassDimensions = mdClass.getMdClassDimensions();

    for (MdClassDimensionDAOIF mdClassDimension : mdClassDimensions)
    {
      TestFixtureFactory.delete(mdClassDimension);
    }

    Assert.assertEquals(0, mdClass.getMdClassDimensions().size());

    new ClassAndAttributeDimensionBuilder().build();

    List<MdClassDimensionDAOIF> test = mdClass.getMdClassDimensions();
    Assert.assertEquals(1, test.size());
    Assert.assertEquals(0L, test.get(0).getSequence());
  }

  @Request
  @Test
  public void testSequenceMdAttribute()
  {
    MdAttributeDAOIF mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttributeCharacter.getOid());
    List<MdAttributeDimensionDAOIF> mdAttributeDimensions = mdAttribute.getMdAttributeDimensions();

    for (MdAttributeDimensionDAOIF mdAttributeDimension : mdAttributeDimensions)
    {
      TestFixtureFactory.delete(mdAttributeDimension);
    }

    // Refresh the object, as it will contain an updated dimension cache
    mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttribute.getOid());

    Assert.assertEquals(0, mdAttribute.getMdAttributeDimensions().size());

    new ClassAndAttributeDimensionBuilder().build();

    // Refresh the object, as it will contain an updated dimension cache
    mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttribute.getOid());

    List<MdAttributeDimensionDAOIF> test = mdAttribute.getMdAttributeDimensions();
    Assert.assertEquals(1, test.size());
    Assert.assertEquals(0L, test.get(0).getSequence());
  }

  @Request
  @Test
  public void testMainDifferentSiteMasterMdClass()
  {
    String siteMaster = CommonProperties.getDomain() + "Different";
    MdClassDAOIF mdClass = (MdClassDAOIF) MdClassDAO.get(mdBusiness.getOid());
    List<MdClassDimensionDAOIF> mdClassDimensions = mdClass.getMdClassDimensions();

    for (MdClassDimensionDAOIF mdClassDimension : mdClassDimensions)
    {
      TestFixtureFactory.delete(mdClassDimension);
    }

    Assert.assertEquals(0, mdClass.getMdClassDimensions().size());

    ClassAndAttributeDimensionBuilder.main(new String[] { siteMaster });

    List<MdClassDimensionDAOIF> test = mdClass.getMdClassDimensions();
    Assert.assertEquals(1, test.size());
    Assert.assertEquals(siteMaster, test.get(0).getSiteMaster());
  }

  @Request
  @Test
  public void testMainDifferentSiteMasterMdAttribute()
  {
    String siteMaster = CommonProperties.getDomain() + "Different";
    MdAttributeDAOIF mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttributeCharacter.getOid());
    List<MdAttributeDimensionDAOIF> mdAttributeDimensions = mdAttribute.getMdAttributeDimensions();

    for (MdAttributeDimensionDAOIF mdAttributeDimension : mdAttributeDimensions)
    {
      TestFixtureFactory.delete(mdAttributeDimension);
    }

    // Refresh the object, as it will contain an updated dimension cache
    mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttribute.getOid());

    Assert.assertEquals(0, mdAttribute.getMdAttributeDimensions().size());

    ClassAndAttributeDimensionBuilder.main(new String[] { siteMaster });

    // Refresh the object, as it will contain an updated dimension cache
    mdAttribute = (MdAttributeDAOIF) MdAttributeDAO.get(mdAttribute.getOid());

    List<MdAttributeDimensionDAOIF> test = mdAttribute.getMdAttributeDimensions();
    Assert.assertEquals(1, test.size());
    Assert.assertEquals(siteMaster, test.get(0).getSiteMaster());
  }
}
