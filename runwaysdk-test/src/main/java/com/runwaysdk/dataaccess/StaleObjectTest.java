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
package com.runwaysdk.dataaccess;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdTreeDAO;
import com.runwaysdk.session.Request;

public class StaleObjectTest
{
  /**
   *
   */
  private static TypeInfo utensilClass = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "UtensilOne");

  /**
   *
   *
   */
  @Request
  @BeforeClass
  public static void classSetUp()
  {
    createUtinsilType();
  }

  /**
   *
   *
   */
  @Request
  @AfterClass
  public static void classTearDown()
  {
    MdBusinessDAO utensilDO = (MdBusinessDAO) ( MdBusinessDAO.getMdBusinessDAO(utensilClass.getType()) ).getBusinessDAO();
    if (utensilDO != null && !utensilDO.isNew())
    {
      utensilDO.delete();
    }
  }

  /**
   *
   *
   */
  private static void createUtinsilType()
  {
    // Create the utensil data type
    MdBusinessDAO utensilMdBusiness = MdBusinessDAO.newInstance();
    utensilMdBusiness.setValue(MdBusinessInfo.NAME, utensilClass.getTypeName());
    utensilMdBusiness.setValue(MdBusinessInfo.PACKAGE, utensilClass.getPackageName());
    utensilMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    utensilMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, utensilClass.getTypeName() + " Test Type");
    utensilMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
    utensilMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    utensilMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    utensilMdBusiness.setGenerateMdController(false);
    utensilMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    utensilMdBusiness.apply();
    utensilClass.setId(utensilMdBusiness.getId());
  }

  /**
   * Makes sure a StaleEntityException is thrown when an object is saved to the
   * database but is not the most current state of the object.
   */
  @Request
  @Test
  public void testStaleObjectSave()
  {
    MdBusinessDAO version1 = null;
    MdBusinessDAO version2 = null;

    try
    {
      version1 = (MdBusinessDAO) ( MdBusinessDAO.getMdBusinessDAO(utensilClass.getType()) ).getBusinessDAO();
      version2 = (MdBusinessDAO) ( MdBusinessDAO.getMdBusinessDAO(utensilClass.getType()) ).getBusinessDAO();

      version1.setStructValue(MetadataInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "version 1 description: test save");
      version2.setStructValue(MetadataInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "version 2 description: test save");

      version2.apply();
      version1.apply(); // version 1 is stale

      Assert.fail("A stale object was able to be saved to the database.");
    }
    catch (StaleEntityException e)
    {
      // we want to land here
    }
  }

  /**
   * Makes sure a StaleEntityException is thrown when an object is saved to the
   * database but is not the most current state of the object.
   */
  @Request
  @Test
  public void testStaleObjectDelete()
  {
    MdBusinessDAO version1 = (MdBusinessDAO) ( MdBusinessDAO.getMdBusinessDAO(utensilClass.getType()) ).getBusinessDAO();
    MdBusinessDAO version2 = (MdBusinessDAO) ( MdBusinessDAO.getMdBusinessDAO(utensilClass.getType()) ).getBusinessDAO();

    try
    {
      version1.setStructValue(MetadataInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "version 1 description: test delete");
      version2.setStructValue(MetadataInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "version 2 description: test delete");

      version2.apply();
      version1.delete(); // version 1 is stale

      Assert.fail("A stale MdBusinss was able to be deleted to the database.");
    }
    catch (StaleEntityException e)
    {
      // we want to land here
    }
    finally
    {
      // If the MdBusiness was deleted, then redefine it for future tests.
      if (!version1.isDefined())
      {
        createUtinsilType();
      }
    }
  }

  /**
   *
   *
   */
  @Request
  @Test
  public void testDoubleDeleteObject()
  {
    try
    {
      BusinessDAO instance = BusinessDAO.newInstance(utensilClass.getType());
      instance.apply();
      instance.delete();
      instance.delete(); // wrong!

      Assert.fail("A stale object was able to be deleted from the database.");
    }
    catch (StaleEntityException e)
    {
      // we want to land here
    }
  }

  /**
   *
   */
  @Request
  @Test
  public void testDoubleDeleteMdRelationship()
  {
    MdBusinessDAO A = null;
    MdBusinessDAO W = null;
    MdTreeDAO AtoW = null;

    try
    {
      TypeInfo A_Class = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "AA");
      A = MdBusinessDAO.newInstance();
      A.setValue(MdBusinessInfo.NAME, A_Class.getTypeName());
      A.setValue(MdBusinessInfo.PACKAGE, A_Class.getPackageName());
      A.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      A.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, A_Class.getTypeName() + " test type");
      A.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      A.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      A.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      A.setGenerateMdController(false);
      A.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      A.apply();

      TypeInfo W_Class = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "WW");
      W = MdBusinessDAO.newInstance();
      W.setValue(MdBusinessInfo.NAME, W_Class.getTypeName());
      W.setValue(MdBusinessInfo.PACKAGE, W_Class.getPackageName());
      W.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      W.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, W_Class.getTypeName() + " test type");
      W.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      W.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      W.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      W.setGenerateMdController(false);
      W.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      W.apply();

      TypeInfo AtoW_REL = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "AtoW");
      AtoW = MdTreeDAO.newInstance();
      AtoW.setValue(MdTreeInfo.NAME, AtoW_REL.getTypeName());
      AtoW.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
      AtoW.setValue(MdTreeInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
      AtoW.setValue(MdTreeInfo.PACKAGE, AtoW_REL.getPackageName());
      AtoW.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A to W");
      AtoW.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      AtoW.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      AtoW.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      AtoW.setValue(MdTreeInfo.PARENT_MD_BUSINESS, A.getId());
      AtoW.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
      AtoW.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "parent A");
      AtoW.setValue(MdTreeInfo.CHILD_MD_BUSINESS, W.getId());
      AtoW.setValue(MdTreeInfo.CHILD_CARDINALITY, "1");
      AtoW.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "child W");
      AtoW.setValue(MdTreeInfo.PARENT_METHOD, "A");
      AtoW.setValue(MdTreeInfo.CHILD_METHOD, "W");
      AtoW.setGenerateMdController(false);
      AtoW.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      AtoW.apply();

      AtoW.delete();
      AtoW.delete();// wrong!

      Assert.fail("A relationship was able to be deleted twice.");
    }
    catch (StaleEntityException e)
    {
      // we want to land here
    }
    finally
    {
      if (A != null && !A.isNew())
      {
        TestFixtureFactory.delete(A);
      }
      if (W != null && !W.isNew())
      {
        TestFixtureFactory.delete(W);
      }
      if (AtoW != null && !AtoW.isNew() && !AtoW.isAppliedToDB())
      {
        TestFixtureFactory.delete(AtoW);
      }
    }
  }

  /**
   *
   */
  @Request
  @Test
  public void testDoubleDeleteMdBusiness()
  {
    MdBusinessDAO utensilDO = (MdBusinessDAO) ( MdBusinessDAO.getMdBusinessDAO(utensilClass.getType()) ).getBusinessDAO();

    try
    {
      utensilDO.delete();
      utensilDO.delete(); // this is wrong!

      Assert.fail("An object was able to deleted twice, which is incorrect.");
    }
    catch (StaleEntityException e)
    {
      // we want to land here
    }
    // If the MdBusiness was deleted, then redefine it for future tests.
    if (!utensilDO.isDefined())
    {
      createUtinsilType();
    }
  }
}
