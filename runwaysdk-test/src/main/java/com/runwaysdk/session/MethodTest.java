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
package com.runwaysdk.session;

import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.runwaysdk.ClasspathTestRunner;
import com.runwaysdk.business.Business;
import com.runwaysdk.business.rbac.MethodActorDAO;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;

@RunWith(ClasspathTestRunner.class)
public class MethodTest
{
  /**
   * The test assignable method object
   */
  private static MethodActorDAO         methodActor;

  /**
   * The test mdMethod object
   */
  private static MdMethodDAO            mdMethod;

  /**
   * The test MdMethod object
   */
  private static MdMethodDAO            mdMethod2;

  /**
   * The test mdBusiness data object
   */
  private static MdBusinessDAO          mdBusiness;

  /**
   * The test mdBusiness2 data object
   */
  private static MdBusinessDAO          mdBusiness2;

  /**
   * The test mdRelationshiop
   */
  private static MdRelationshipDAO      mdTree;

  /**
   * The test mdAttribute of the MdBusiness
   */
  private static MdAttributeConcreteDAO mdAttribute;

  /**
   * The test businessDAO of the MdBusiness
   */
  private static BusinessDAO            businessDAO;

  private static Business               business1;

  /**
   * The setup done before the test suite is run
   */
  @Request
  @BeforeClass
  public static void classSetUp()
  {
    // Create a new MdBusiness
    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    mdMethod = TestFixtureFactory.createMdMethod(mdBusiness);
    mdMethod.apply();

    // Create a new user
    methodActor = TestFixtureFactory.createMethodActor(mdMethod);
    methodActor.apply();

    // Create an MdAttribute on the MdBusiness
    mdAttribute = TestFixtureFactory.addBooleanAttribute(mdBusiness);
    mdAttribute.apply();

    // Create a new MdBusiness
    mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.apply();

    // Create a new relationship
    mdTree = TestFixtureFactory.createMdTree(mdBusiness2, mdBusiness2);
    mdTree.apply();

    // Create a businessDAO of MdBusiness
    businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.apply();

    business1 = Business.getBusiness(businessDAO.getId());

    mdMethod2 = TestFixtureFactory.createMdMethod(mdBusiness, "checkout2");
    mdMethod2.apply();
  }

  /**
   * The tear down done after all the test in the test suite have run
   */
  @Request
  @AfterClass
  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdTree);
    TestFixtureFactory.delete(mdMethod);
    TestFixtureFactory.delete(mdMethod2);
    TestFixtureFactory.delete(mdBusiness);
    TestFixtureFactory.delete(mdBusiness2);
  }

  /**
   * Delete all MetaData objects which were created in the class
   * 
   * @see junit.framework.TestCase#tearDown()
   */
  @Request
  @After
  public void tearDown() throws Exception
  {
    Set<RelationshipDAOIF> set = methodActor.getAllPermissions();

    for (RelationshipDAOIF reference : set)
    {
      // Revoke any type permissions given to newUser
      methodActor.revokeAllPermissions(reference.getChildId());
    }

    // Clear any lingering sessions
    MethodFacade.clear();
  }

  /**
   * Test session access
   */
  @Request
  @Test
  public void testCheckAccess()
  {
    // Grant permissions to the user on the md class
    methodActor.grantPermission(Operation.DELETE, mdBusiness.getId());

    // Check various access of the user
    boolean access = MethodFacade.checkAccess(mdMethod, Operation.DELETE, business1);
    boolean no_access = MethodFacade.checkAccess(mdMethod, Operation.WRITE, business1);

    Assert.assertEquals(true, access);
    Assert.assertEquals(false, no_access);
  }

  /**
   * Test closing a session
   */
  @Request
  @Test
  public void testCloseSession()
  {
    MethodFacade.checkAccess(mdMethod, Operation.WRITE, business1);
    Assert.assertEquals(true, MethodCache.getMethodCache().contains(mdMethod));

    int size = PermissionObserver.size();
    MethodFacade.closeMethod(mdMethod);

    Assert.assertEquals(false, MethodCache.getMethodCache().contains(mdMethod));
    Assert.assertEquals(size - 1, PermissionObserver.size());
  }

  /**
   * Test clearing out all sessions
   */
  @Request
  @Test
  public void testClearSessions()
  {
    MethodFacade.clear();

    Assert.assertEquals(0, MethodCache.getMethodCache().size());
    Assert.assertEquals(0, PermissionObserver.size());
  }

  @Request
  @Test
  public void testRelationshipPermission()
  {
    methodActor.grantPermission(Operation.ADD_CHILD, mdTree.getId());

    Assert.assertFalse(MethodFacade.checkRelationshipAccess(mdMethod, Operation.ADD_PARENT, business1, mdTree.getId()));
    Assert.assertTrue(MethodFacade.checkRelationshipAccess(mdMethod, Operation.ADD_CHILD, business1, mdTree.getId()));
  }

  /**
   * Ensure that changes to permissions are automatically updated to the
   * MethodCache
   */
  @Request
  @Test
  public void testReloadPermissions()
  {
    // Grant permissions to the user on the md class
    methodActor.grantPermission(Operation.DELETE, mdBusiness.getId());

    Assert.assertEquals(true, MethodFacade.checkAccess(mdMethod, Operation.DELETE, business1));
    Assert.assertEquals(false, MethodFacade.checkAccess(mdMethod, Operation.WRITE, business1));

    // Grant permissions to the user on the md class
    methodActor.grantPermission(Operation.WRITE, mdBusiness.getId());

    Assert.assertEquals(true, MethodFacade.checkAccess(mdMethod, Operation.WRITE, business1));
  }

  /**
   * Ensure that changes to role permissions are automatically updated to the
   * MethodCache
   */
  @Request
  @Test
  public void testReloadRolePermissions()
  {
    RoleDAO role = RoleDAO.createRole("runway.Master", "Master");

    try
    {
      // Grant permissions to the user on the md class
      role.assignMember(methodActor);

      methodActor.grantPermission(Operation.DELETE, mdBusiness.getId());

      Assert.assertEquals(true, MethodFacade.checkAccess(mdMethod, Operation.DELETE, business1));
      Assert.assertEquals(false, MethodFacade.checkAccess(mdMethod, Operation.WRITE, business1));

      // Grant permissions to the user on the md class
      role.grantPermission(Operation.WRITE, mdBusiness.getId());

      Assert.assertEquals(true, MethodFacade.checkAccess(mdMethod, Operation.WRITE, business1));
    }
    catch (Throwable t)
    {
      throw new RuntimeException(t);
    }
    finally
    {
      role.delete();
    }
  }

  /**
   * Ensure that the MethodCache works when not permissions are given exist
   */
  @Request
  @Test
  public void testNoPermissions()
  {
    Assert.assertFalse(MethodFacade.checkAccess(mdMethod, Operation.WRITE, business1));
    Assert.assertFalse(MethodFacade.checkAttributeAccess(mdMethod, Operation.WRITE, business1, mdAttribute));
    Assert.assertFalse(MethodFacade.checkRelationshipAccess(mdMethod, Operation.ADD_PARENT, business1, mdTree.getId()));
  }

  @Request
  @Test
  public void testExecutePermissions()
  {
    methodActor.grantPermission(Operation.EXECUTE, mdMethod2.getId());

    Assert.assertEquals(true, MethodFacade.checkExecuteAccess(mdMethod, mdMethod2));
  }

  @Request
  @Test
  public void testNoExecutePermissions()
  {
    Assert.assertEquals(false, MethodFacade.checkExecuteAccess(mdMethod, mdMethod2));
  }

}
