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
package com.runwaysdk.business;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.DomainErrorException;
import com.runwaysdk.RunwayExceptionDTO;
import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.business.rbac.MethodActorDAO;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MethodActorInfo;
import com.runwaysdk.constants.TestProperties;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityMasterTestSetup;
import com.runwaysdk.dataaccess.RelationshipCardinalityException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.facade.Facade;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.AttributeWritePermissionException;
import com.runwaysdk.session.ExecuteInstancePermissionException;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.WritePermissionException;

public class MultiThreadTestSuite extends TestCase
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

  public static final TypeInfo         MULTITHREAD_TEST_CLASS_1                = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "MultiThreadTest1");

  public static final TypeInfo         MULTITHREAD_TEST_CLASS_2                = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "MultiThreadTest2");

  public static final TypeInfo         MULTITHREAD_RELATIONSHIP                = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "MultiThreadRelationship");

  private static MdBusinessDAO         multiThreadMdBusiness1;

  private static MdBusinessDAO         multiThreadMdBusiness2;

  private static MdRelationshipDAO     mdRelationship;

  private static MdAttributeIntegerDAO mdAttributeInteger;

  private static String                testType1ObjectId;

  private static String                testType2ObjectId;

  private static final Vector<UserDAO> userVector                              = new Vector<UserDAO>();

  private static final Vector<Integer> resultsVector                           = new Vector<Integer>();

  private static final Vector<String>  busObjectIdVector                       = new Vector<String>();

  private static int                   threadCounter                           = 0;

  private static int                   maxUsers                                = 1;

  private final static int             appLockThreadNumber                     = TestProperties.getAppLockTreadNumber();

  private final static int             userLockThreadNumber                    = TestProperties.getUserLockTreadNumber();

  private final static int             userLockModCacheThreadNumber            = TestProperties.getUserLockModCacheTreadNumber();

  private final static int             userLockModTypeTreadNumber              = TestProperties.getUserLockModTypeTreadNumber();

  private final static int             userLockModPublicPermissionsTreadNumber = TestProperties.getUserLockModPublicPermissionsTreadNumber();

  private final static int             userLockModRolePermissionsTreadNumber   = TestProperties.getUserLockModRolePermissionsTreadNumber();

  private final static int             userLockModOwnerPermissionsTreadNumber  = TestProperties.getUserLockModOwnerPermissionsTreadNumber();

  private final static int             userRoleMethodExecutePermissions        = TestProperties.getUserRoleMethodExecutePermissions();

  private final static int             methodCreatePermissions                 = TestProperties.getMethodCreatePermissions();

  private final static int             relLockCardinalityThreadNumber          = TestProperties.getRelLockCardinalityThreadNumber();

  private static int                   relationshipCardinality                 = relLockCardinalityThreadNumber;

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(MultiThreadTestSuite.suite());
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(MultiThreadTestSuite.class);

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
    multiThreadMdBusiness1 = MdBusinessDAO.newInstance();
    multiThreadMdBusiness1.setValue(MdBusinessInfo.NAME, MULTITHREAD_TEST_CLASS_1.getTypeName());
    multiThreadMdBusiness1.setValue(MdBusinessInfo.PACKAGE, MULTITHREAD_TEST_CLASS_1.getPackageName());
    multiThreadMdBusiness1.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    multiThreadMdBusiness1.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MultiThread Test Type 1");
    multiThreadMdBusiness1.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Type for testing that the core is thread safe.");
    multiThreadMdBusiness1.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    multiThreadMdBusiness1.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    // multiThreadMdBusiness1.setValue(MdEntityInfo.CACHE_ALGORITHM,
    // EntityCache.EVERYTHING.getId());
    multiThreadMdBusiness1.apply();

    mdAttributeInteger = MdAttributeIntegerDAO.newInstance();
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.NAME, "someInt");
    mdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Some Integer");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, multiThreadMdBusiness1.getId());
    mdAttributeInteger.apply();

    multiThreadMdBusiness2 = MdBusinessDAO.newInstance();
    multiThreadMdBusiness2.setValue(MdBusinessInfo.NAME, MULTITHREAD_TEST_CLASS_2.getTypeName());
    multiThreadMdBusiness2.setValue(MdBusinessInfo.PACKAGE, MULTITHREAD_TEST_CLASS_2.getPackageName());
    multiThreadMdBusiness2.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    multiThreadMdBusiness2.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MultiThread Test Type 2");
    multiThreadMdBusiness2.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Type for testing that the core is thread safe.");
    multiThreadMdBusiness2.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    multiThreadMdBusiness2.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    multiThreadMdBusiness2.apply();

    if (relLockCardinalityThreadNumber > 1)
    {
      relationshipCardinality = relLockCardinalityThreadNumber / 2;
    }

    // Now for a new Relationship
    mdRelationship = MdRelationshipDAO.newInstance();
    mdRelationship.setValue(MdRelationshipInfo.NAME, MULTITHREAD_RELATIONSHIP.getTypeName());
    mdRelationship.setValue(MdRelationshipInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdRelationship.setValue(MdRelationshipInfo.PACKAGE, MULTITHREAD_RELATIONSHIP.getPackageName());
    mdRelationship.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdRelationship.setValue(MdRelationshipInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdRelationship.setValue(MdRelationshipInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdRelationship.setValue(MdRelationshipInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdRelationship.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, multiThreadMdBusiness1.getId());
    mdRelationship.setValue(MdRelationshipInfo.PARENT_CARDINALITY, Integer.toString(relationshipCardinality));
    mdRelationship.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "TEST class");
    mdRelationship.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, multiThreadMdBusiness2.getId());
    mdRelationship.setValue(MdRelationshipInfo.CHILD_CARDINALITY, Integer.toString(relationshipCardinality));
    mdRelationship.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "points to \"" + EntityMasterTestSetup.REFERENCE_CLASS.getType() + "\" class");
    mdRelationship.setValue(MdRelationshipInfo.PARENT_METHOD, "TestParent1");
    mdRelationship.setValue(MdRelationshipInfo.CHILD_METHOD, "TestChild1");
    mdRelationship.apply();

    if (appLockThreadNumber > maxUsers)
      maxUsers = appLockThreadNumber;
    if (userLockThreadNumber > maxUsers)
      maxUsers = userLockThreadNumber;
    if (userLockModCacheThreadNumber > maxUsers)
      maxUsers = userLockModCacheThreadNumber;
    if (userLockModTypeTreadNumber > maxUsers)
      maxUsers = userLockModTypeTreadNumber;
    if (userLockModPublicPermissionsTreadNumber > maxUsers)
      maxUsers = userLockModPublicPermissionsTreadNumber;
    if (userLockModRolePermissionsTreadNumber > maxUsers)
      maxUsers = userLockModRolePermissionsTreadNumber;
    if (userLockModOwnerPermissionsTreadNumber > maxUsers)
      maxUsers = userLockModOwnerPermissionsTreadNumber;
    if (userRoleMethodExecutePermissions > maxUsers)
      maxUsers = userRoleMethodExecutePermissions;
    if (methodCreatePermissions > maxUsers)
      maxUsers = methodCreatePermissions;
    if (relLockCardinalityThreadNumber > maxUsers)
      maxUsers = relLockCardinalityThreadNumber;

    for (int i = 0; i < maxUsers; i++)
    {
      createNewUser();
    }

  }

  public static void classTearDown()
  {
    for (UserDAO user : userVector)
    {
      user.delete();
    }

    TestFixtureFactory.delete(mdRelationship);
    TestFixtureFactory.delete(multiThreadMdBusiness1);
    TestFixtureFactory.delete(multiThreadMdBusiness2);
  }

  public void setUp()
  {
    createCommonObject();
  }

  public void tearDown()
  {
    resultsVector.clear();
    deleteCommonObject();
  }

  /**
   * Increments the thread counter used by tests in this class.
   * 
   * @return incremented counter value.
   */
  private synchronized static int incThreadCounter()
  {
    return threadCounter++;
  }

  /**
   * Returns the thread counter value used by tests in this class.
   * 
   * @return incremented counter value.
   */
  @SuppressWarnings("unused")
  private synchronized static int getThreadCounter()
  {
    return threadCounter;
  }

  /**
   * Returns the thread counter value used by tests in this class.
   * 
   * @param _threadCounter
   * @return incremented counter value.
   */
  @SuppressWarnings("unused")
  private synchronized static void setThreadCounter(int _threadCounter)
  {
    threadCounter = _threadCounter;
  }

  /**
   * Creates an object of type MULTITHREAD_TEST_CLASS_1.getType() for multiple
   * threads to modify.
   * 
   */
  @Request
  public static void createCommonObject()
  {
    // Create common record
    BusinessDAO businessDAO = BusinessDAO.newInstance(MULTITHREAD_TEST_CLASS_1.getType());
    businessDAO.setValue("someInt", "0");
    testType1ObjectId = businessDAO.apply();
  }

  /**
   * Creates an object of type MULTITHREAD_TEST_CLASS_2.getType().
   * 
   */
  @Request
  public static void createCommonObject2()
  {
    // Create common record
    BusinessDAO businessDAO = BusinessDAO.newInstance(MULTITHREAD_TEST_CLASS_2.getType());
    testType2ObjectId = businessDAO.apply();
  }

  /**
   *
   *
   */
  public static void deleteCommonObject()
  {
    try
    {
      ObjectCache.flushCache();
      // Delete the common object
      BusinessDAO businessDAO = BusinessDAO.get(testType1ObjectId).getBusinessDAO();
      businessDAO.delete();
    }
    catch (DataNotFoundException e)
    {
      // do nothing
    }
  }

  /**
   *
   *
   */
  public static void deleteCommonObject2()
  {
    try
    {
      // Delete the common object
      BusinessDAO businessDAO = BusinessDAO.get(testType2ObjectId).getBusinessDAO();
      businessDAO.delete();
    }
    catch (DataNotFoundException e)
    {
      // do nothing
    }
  }

  public static void deleteCommonObject(String sessionId)
  {
    // Delete the common object
    Business user = Business.get(testType1ObjectId);
    user.appLock();

    user.delete();
  }

  /**
   * Creates a new user and adds that user to the admin role.
   * 
   * @param grantPermission
   *          true if the user is granted permission to modify the test type.
   * 
   * @return string that is both the username and the password of the user.
   */
  @Request
  @Transaction
  public static String createNewUser()
  {
    String seed = "worker" + incThreadCounter();
    UserDAO user = UserDAO.newInstance();
    user.setValue(UserInfo.USERNAME, seed);
    user.setValue(UserInfo.PASSWORD, seed);
    user.apply();

    userVector.add(user);

    return seed;
  }

  /**
   * Deletes the given user.
   * 
   * @param user
   */
  @Request
  public static void deleteUserObject(UserDAO user)
  {
    user.delete();
  }

  /**
   * 
   * @param sessionId
   * @return
   */
  @Request(RequestType.SESSION)
  public static void lockCommonObjectWithUserLock(String sessionId)
  {
    Business busObject = Business.get(testType1ObjectId);
    busObject.lock();
  }

  /**
   * 
   * @param sessionId
   * @return
   */
  @Request(RequestType.SESSION)
  public static Integer updateCommonObjectWithUserLock2(String sessionId)
  {
    Business busObject = Business.get(testType1ObjectId);
    Integer intVal = new Integer(busObject.getValue("someInt"));
    intVal += 1;
    busObject.setValue("someInt", intVal.toString());
    resultsVector.add(intVal);

    // System.out.println("FINISHED! " +intVal);

    busObject.apply();

    return intVal;
  }

  /**
   * Tests that multiple session threads must sequentially acquire an
   * application lock on an object in order to modify it. The type of the shared
   * object is cached.
   */
  public void testAppLockOnCachedType()
  {
    MdBusinessDAO updateMdBusiness = MdBusinessDAO.get(multiThreadMdBusiness1.getId()).getBusinessDAO();
    updateMdBusiness.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getId());
    updateMdBusiness.apply();

    this.appLock();
  }

  /**
   * Tests that multiple session threads must sequentially aquire an application
   * lock on an object in order to modify it. The type of the shared object is
   * not cached.
   */
  public void testAppLockOnNotCachedType()
  {
    MdBusinessDAO updateMdBusiness = MdBusinessDAO.get(multiThreadMdBusiness1.getId()).getBusinessDAO();
    updateMdBusiness.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
    updateMdBusiness.apply();

    this.appLock();
  }

  /**
   * Tests that multiple session threads must sequentially aquire an application
   * lock on an object in order to modify it.
   */
  public void appLock()
  {
    int _numOfThreads = appLockThreadNumber;

    // assign admin role to users
    RoleDAO adminRole = RoleDAO.findRole(RoleDAOIF.ADMIN_ROLE).getBusinessDAO();
    for (int i = 0; i < _numOfThreads; i++)
    {
      UserDAO user = userVector.get(i);
      adminRole.assignMember(user);
    }

    ExecutorService executor = Executors.newFixedThreadPool(_numOfThreads);

    CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(executor);

    // Update the common object
    int loopCount = 0;
    for (final UserDAO user : userVector)
    {
      if (loopCount >= _numOfThreads)
      {
        break;
      }

      Callable<Integer> callable = new Callable<Integer>()
      {
        public Integer call()
        {
          String sessionId = "";
          sessionId = Facade.login(user.getSingleActorName(), user.getSingleActorName(), new Locale[] { CommonProperties.getDefaultLocale() });

          int returnValue = 0;

          while (true)
          {
            try
            {
              returnValue = updateCommonObjectWithAppLock(sessionId);
              Facade.logout(sessionId);
              return returnValue;
            }
            catch (Throwable e)
            {
              boolean fail = false;
              if ( ( e instanceof RunwayExceptionDTO ) && ! ( (RunwayExceptionDTO) e ).getType().equals(LockException.class.getName()))
              {
                fail = true;
              }

              if (fail)
              {
                Facade.logout(sessionId);
                fail(e.getMessage());
              }
            }
          }

        }
      };
      completionService.submit(callable);

      loopCount++;
    }

    try
    {
      // Fetch the results as the complete
      for (int i = 0; i < _numOfThreads; i++)
      {
        Future<Integer> f = completionService.take();
        try
        {
          f.get();
        }
        catch (ExecutionException e)
        {
          Throwable cause = e.getCause();
          throw cause;
        }
      }

      int expectedResult = 0;
      for (int i = 0; i < _numOfThreads; i++)
      {
        int actualResult = resultsVector.get(i);

        expectedResult += 1;
        if (actualResult != expectedResult)
        {
          String errMsg = "Expected result was - " + expectedResult + "  Actual result - " + actualResult;

          throw new RuntimeException(errMsg);
        }
        // System.out.println("FINISHED! "+actualResult);
      }
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      // System.out.println("\n----------------------------------------------\n "+resultsVector+"\n----------------------------------------------");
      executor.shutdownNow();

      for (int i = 0; i < _numOfThreads; i++)
      {
        UserDAO user = userVector.get(i);
        adminRole.deassignMember(user);
      }

    }
  }

  /**
   * 
   * @param sessionId
   * @return
   */
  @Request(RequestType.SESSION)
  public static Integer updateCommonObjectWithAppLock(String sessionId)
  {
    Business busObject = Business.get(testType1ObjectId);

    // System.out.println("1 attempt lock: "+Thread.currentThread()+" "+busObject.getValue(EntityInfo.SEQUENCE));

    busObject.appLock();
    // System.out.println("2 lock attained: "+Thread.currentThread()+" "+busObject.getValue(EntityInfo.SEQUENCE));
    Integer intVal = new Integer(busObject.getValue("someInt"));
    intVal += 1;
    busObject.setValue("someInt", intVal.toString());
    resultsVector.add(intVal);
    busObject.apply();
    // System.out.println("3 lock released: "+Thread.currentThread()+" "+busObject.getValue(EntityInfo.SEQUENCE));
    // System.out.println("FINISHED! " +intVal);

    return intVal;
  }

  /**
   * Tests that multiple session threads must sequentially aquire a user lock on
   * an object in order to modify it. The type of the shared object is cached.
   */
  public void testUserLockOnCachedType()
  {
    MdBusinessDAO updateMdBusiness = MdBusinessDAO.get(multiThreadMdBusiness1.getId()).getBusinessDAO();
    updateMdBusiness.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getId());
    updateMdBusiness.apply();

    // System.out.println("Starting Test");
    // java.util.Date startTime = new java.util.Date();

    this.userLock();

    // java.util.Date endTime = new java.util.Date();
    // long totalTime = endTime.getTime() - startTime.getTime();
    // System.out.println("\nTotal Time: " + totalTime);
  }

  /**
   * Tests that multiple session threads must sequentially aquire a user lock on
   * an object in order to modify it. The type of the shared object is not
   * cached.
   */
  public void testUserLockOnNotCachedType()
  {
    multiThreadMdBusiness1.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
    multiThreadMdBusiness1.apply();

    // System.out.println("Starting Test");
    // java.util.Date startTime = new java.util.Date();

    this.userLock();

    // java.util.Date endTime = new java.util.Date();
    // long totalTime = endTime.getTime() - startTime.getTime();
    // System.out.println("\nTotal Time: " + totalTime);
  }

  /**
   * Tests that multiple session threads must sequentially aquire a user lock on
   * an object in order to modify it.
   */
  public void userLock()
  {
    int _numOfThreads = userLockThreadNumber;

    // assign admin role to users
    RoleDAO adminRole = RoleDAO.findRole(RoleDAOIF.ADMIN_ROLE).getBusinessDAO();
    for (int i = 0; i < _numOfThreads; i++)
    {
      UserDAO user = userVector.get(i);
      adminRole.assignMember(user);
    }

    ExecutorService executor = Executors.newFixedThreadPool(_numOfThreads);

    CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(executor);

    // Update the common object
    int loopCount = 0;
    for (final UserDAO user : userVector)
    {
      if (loopCount >= _numOfThreads)
      {
        break;
      }

      Callable<Integer> callable = new Callable<Integer>()
      {
        public Integer call()
        {
          String sessionId = "";
          sessionId = Facade.login(user.getSingleActorName(), user.getSingleActorName(), new Locale[] { CommonProperties.getDefaultLocale() });

          int returnValue = 0;

          while (true)
          {
            try
            {
              lockCommonObjectWithUserLock(sessionId);
              returnValue = updateCommonObjectWithUserLock2(sessionId);
              Facade.logout(sessionId);
              return returnValue;
            }
            catch (Throwable e)
            {
              boolean fail = false;
              if ( ( e instanceof RunwayExceptionDTO ) && ! ( (RunwayExceptionDTO) e ).getType().equals(LockException.class.getName()))
              {
                fail = true;
              }

              if (fail)
              {
                Facade.logout(sessionId);
                fail(e.getMessage());
              }
            }
          }

        }
      };
      completionService.submit(callable);

      loopCount++;
    }

    try
    {
      // Fetch the results as the complete
      for (int i = 0; i < _numOfThreads; i++)
      {
        Future<Integer> f = completionService.take();
        try
        {
          f.get();
        }
        catch (ExecutionException e)
        {
          Throwable cause = e.getCause();
          throw cause;
        }
      }

      int expectedResult = 0;
      for (int i = 0; i < _numOfThreads; i++)
      {
        int actualResult = resultsVector.get(i);

        expectedResult += 1;
        if (actualResult != expectedResult)
        {
          String errMsg = "Expected result was - " + expectedResult + "  Actual result - " + actualResult;

          throw new RuntimeException(errMsg);
        }
        // System.out.println("FINISHED! "+actualResult);
      }
    }
    catch (InterruptedException e)
    {
      Thread.currentThread().interrupt();
    }
    catch (Throwable e)
    {
      e.printStackTrace();
      fail(e.getMessage());
    }
    finally
    {
      // System.out.println("\n----------------------------------------------\n "+resultsVector+"\n----------------------------------------------");
      executor.shutdownNow();

      for (int i = 0; i < _numOfThreads; i++)
      {
        UserDAO user = userVector.get(i);
        adminRole.deassignMember(user);
      }

    }
  }

  /**
   * Test multiple threads updating a common object while the cache algorithm of
   * the object's type is modified.
   * 
   */
// Commented out because it fails deterministically when run on Cruise Control
//  public void testUpdateCache()
//  {
//    int _numOfThreads = userLockModCacheThreadNumber;
//
//    int numberOfAdminThreads = _numOfThreads * 4;
//
//    // assign admin role to users
//    RoleDAO adminRole = RoleDAO.findRole(RoleDAOIF.ADMIN_ROLE).getBusinessDAO();
//    for (int i = 0; i < _numOfThreads; i++)
//    {
//      UserDAO user = userVector.get(i);
//      adminRole.assignMember(user);
//    }
//
//    ExecutorService executor = Executors.newFixedThreadPool(_numOfThreads);
//
//    CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(executor);
//
//    // Update the common object
//    int loopCount = 0;
//    for (final UserDAO user : userVector)
//    {
//      if (loopCount >= _numOfThreads)
//      {
//        break;
//      }
//
//      Callable<Integer> callable = new Callable<Integer>()
//      {
//        public Integer call()
//        {
//          String sessionId = "";
//          sessionId = Facade.login(user.getSingleActorName(), user.getSingleActorName(), new Locale[] { CommonProperties.getDefaultLocale() });
//
//          int returnValue = 0;
//
//          while (true)
//          {
//            try
//            {
//              lockCommonObjectWithUserLock(sessionId);
//              returnValue = updateCommonObjectWithUserLock2(sessionId);
//              Facade.logout(sessionId);
//              return returnValue;
//            }
//            catch (Throwable e)
//            {
//              boolean fail = false;
//              if ( ( e instanceof RunwayExceptionDTO ) && ! ( (RunwayExceptionDTO) e ).getType().equals(LockException.class.getName()))
//              {
//                fail = true;
//              }
//
//              if (fail)
//              {
//                e.printStackTrace();
//                Facade.logout(sessionId);
//                fail(e.getMessage());
//              }
//            }
//          }
//
//        }
//      };
//      completionService.submit(callable);
//
//      loopCount++;
//    }
//
//    // Muck with the collection class algorithm while the common object of this
//    // type are being modified by
//    // concurrent threads.
//    for (int i = 0; i < numberOfAdminThreads; i++)
//    {
//      if (i % 3 == 0)
//      {
//        // System.out.println("MRU");
//        MdBusinessDAO updateMdBusiness = MdBusinessDAO.get(multiThreadMdBusiness1.getId()).getBusinessDAO();
//        updateMdBusiness.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getId());
//        updateMdBusiness.setValue(MdElementInfo.CACHE_SIZE, "5");
//        updateMdBusiness.apply();
//      }
//      else if (i % 2 == 0)
//      {
//        // System.out.println("EVERYTHING");
//        MdBusinessDAO updateMdBusiness = MdBusinessDAO.get(multiThreadMdBusiness1.getId()).getBusinessDAO();
//        updateMdBusiness.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getId());
//        updateMdBusiness.apply();
//      }
//      else
//      {
//        // System.out.println("NOTHING");
//        MdBusinessDAO updateMdBusiness = MdBusinessDAO.get(multiThreadMdBusiness1.getId()).getBusinessDAO();
//        updateMdBusiness.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
//        updateMdBusiness.apply();
//      }
//    }
//
//    try
//    {
//      // Fetch the results as the complete
//      for (int i = 0; i < _numOfThreads; i++)
//      {
//        Future<Integer> f = completionService.take();
//        try
//        {
//          f.get();
//        }
//        catch (ExecutionException e)
//        {
//          Throwable cause = e.getCause();
//          throw cause;
//        }
//      }
//
//      int expectedResult = 0;
//      for (int i = 0; i < _numOfThreads; i++)
//      {
//        int actualResult = resultsVector.get(i);
//
//        expectedResult += 1;
//        if (actualResult != expectedResult)
//        {
//          String errMsg = "Expected result was - " + expectedResult + "  Actual result - " + actualResult;
//
//          throw new RuntimeException(errMsg);
//        }
//        // System.out.println("FINISHED! "+actualResult);
//      }
//
//    }
//    catch (InterruptedException e)
//    {
//      e.printStackTrace();
//      Thread.currentThread().interrupt();
//    }
//    catch (Throwable e)
//    {
//      e.printStackTrace();
//      fail(e.getMessage());
//    }
//    finally
//    {
//      executor.shutdownNow();
//
//      for (int i = 0; i < _numOfThreads; i++)
//      {
//        UserDAO user = userVector.get(i);
//        adminRole.deassignMember(user);
//      }
//
//    }
//  }

  /**
   * Test multiple threads updating a common object while the type is modified
   * by adding and removing an attribute.
   * 
   */
  public void testUpdateClass()
  {
    MdBusinessDAO updateMdBusiness = MdBusinessDAO.get(multiThreadMdBusiness1.getId()).getBusinessDAO();
    updateMdBusiness.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getId());
    updateMdBusiness.apply();

    int _numOfThreads = userLockModTypeTreadNumber;

    int numberOfAdminThreads = _numOfThreads / 3;

    // assign admin role to users
    RoleDAO adminRole = RoleDAO.findRole(RoleDAOIF.ADMIN_ROLE).getBusinessDAO();
    for (int i = 0; i < _numOfThreads; i++)
    {
      UserDAO user = userVector.get(i);
      adminRole.assignMember(user);
    }

    ExecutorService executor = Executors.newFixedThreadPool(_numOfThreads);

    CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(executor);

    // Update the common object
    int loopCount = 0;
    for (final UserDAO user : userVector)
    {
      if (loopCount >= _numOfThreads)
      {
        break;
      }

      Callable<Integer> callable = new Callable<Integer>()
      {
        public Integer call()
        {
          String sessionId = "";
          sessionId = Facade.login(user.getSingleActorName(), user.getSingleActorName(), new Locale[] { CommonProperties.getDefaultLocale() });

          int returnValue = 0;

          while (true)
          {
            try
            {
              lockCommonObjectWithUserLock(sessionId);
              returnValue = updateCommonObjectWithUserLock2(sessionId);
              Facade.logout(sessionId);
              return returnValue;
            }
            catch (Throwable e)
            {
              boolean fail = false;
              if ( ( e instanceof RunwayExceptionDTO ) && ! ( (RunwayExceptionDTO) e ).getType().equals(LockException.class.getName()))
              {
                fail = true;
              }

              if (fail)
              {
                Facade.logout(sessionId);
                fail(e.getMessage());
              }
            }
          }

        }
      };
      completionService.submit(callable);
      loopCount++;
    }

    List<MdAttributeIntegerDAO> mdAttributeList = new LinkedList<MdAttributeIntegerDAO>();

    MdAttributeIntegerDAO mdAttributeIntegerSomeInt2 = null;

    try
    {
      // Muck with the class definition.
      for (int i = 0; i < numberOfAdminThreads; i++)
      {
        // System.out.println("Adding attribute");
        mdAttributeIntegerSomeInt2 = MdAttributeIntegerDAO.newInstance();
        mdAttributeIntegerSomeInt2.setValue(MdAttributeIntegerInfo.NAME, "someOtherInt_" + i);
        mdAttributeIntegerSomeInt2.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Some Other Integer");
        mdAttributeIntegerSomeInt2.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
        mdAttributeIntegerSomeInt2.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
        mdAttributeIntegerSomeInt2.setValue(MdAttributeIntegerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
        mdAttributeIntegerSomeInt2.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, multiThreadMdBusiness1.getId());
        mdAttributeIntegerSomeInt2.apply();

        mdAttributeList.add(mdAttributeIntegerSomeInt2);
      }
    }
    catch (Throwable e)
    {
      // Oracle takes a long time to add a column. Sometimes it times out. I
      // don't want the
      // test to fail when this happens.
      e.getCause().printStackTrace();
    }

    try
    {
      // Fetch the results as the complete
      for (int i = 0; i < _numOfThreads; i++)
      {
        Future<Integer> f = completionService.take();
        try
        {
          f.get();
        }
        catch (ExecutionException e)
        {
          Throwable cause = e.getCause();
          throw cause;
        }
      }

      int expectedResult = 0;
      for (int i = 0; i < _numOfThreads; i++)
      {
        int actualResult = resultsVector.get(i);

        expectedResult += 1;
        if (actualResult != expectedResult)
        {
          String errMsg = "Expected result was - " + expectedResult + "  Actual result - " + actualResult;

          throw new RuntimeException(errMsg);
        }
        // System.out.println("FINISHED! "+actualResult);
      }

    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
    catch (Throwable e)
    {
      if (e.getCause() != null)
      {
        e.getCause().printStackTrace();
      }
      else
      {
        e.printStackTrace();
      }
      fail(e.getMessage());
    }
    finally
    {
      executor.shutdownNow();

      for (int i = 0; i < _numOfThreads; i++)
      {
        UserDAO user = userVector.get(i);
        adminRole.deassignMember(user);
      }

      for (MdAttributeIntegerDAO mdAttributeInteger : mdAttributeList)
      {
        mdAttributeInteger.delete();
      }

      multiThreadMdBusiness1 = MdBusinessDAO.get(multiThreadMdBusiness1.getId()).getBusinessDAO();
      multiThreadMdBusiness1.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
      multiThreadMdBusiness1.apply();

    }
  }

  // //////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Change permissions for the public user.
   * 
   */
  public void testSessionChangePublicPermissions()
  {
    int _numOfThreads = userLockModPublicPermissionsTreadNumber;

    ExecutorService executor = Executors.newFixedThreadPool(_numOfThreads);

    CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(executor);

    // Update the common object
    int loopCount = 0;
    for (final UserDAO user : userVector)
    {
      if (loopCount >= _numOfThreads)
      {
        break;
      }
      Callable<Integer> callable = new Callable<Integer>()
      {
        public Integer call()
        {
          String sessionId = "";
          sessionId = Facade.login(user.getSingleActorName(), user.getSingleActorName(), new Locale[] { CommonProperties.getDefaultLocale() });

          int returnValue = 0;

          while (true)
          {
            try
            {
              lockCommonObjectWithUserLock(sessionId);
              returnValue = updateCommonObjectWithUserLock2(sessionId);
              Facade.logout(sessionId);
              return returnValue;
            }
            catch (Throwable e)
            {
              boolean fail = true;
              if ( ( e instanceof RunwayExceptionDTO ) && ( ( (RunwayExceptionDTO) e ).getType().equals(LockException.class.getName()) || ( (RunwayExceptionDTO) e ).getType().equals(WritePermissionException.class.getName()) || ( (RunwayExceptionDTO) e ).getType().equals(AttributeWritePermissionException.class.getName()) ))
              {
                fail = false;
              }

              Facade.logout(sessionId);
              if (fail)
              {
                fail(e.getMessage());
              }

              // Logout and relogin again
              sessionId = Facade.login(user.getSingleActorName(), user.getSingleActorName(), new Locale[] { CommonProperties.getDefaultLocale() });
            }
          }
        }
      };
      completionService.submit(callable);
      loopCount++;
    }

    RoleDAO publicRole = RoleDAO.findRole(RoleDAO.PUBLIC_ROLE).getBusinessDAO();

    try
    {

      Thread.sleep(5000);

      publicRole.grantPermission(Operation.WRITE, multiThreadMdBusiness1.getId());
      publicRole.grantPermission(Operation.WRITE, mdAttributeInteger.getId());
      // System.out.println("Changing permissions");

      // Fetch the results as the complete
      for (int i = 0; i < _numOfThreads; i++)
      {
        Future<Integer> f = completionService.take();
        try
        {
          f.get();
        }
        catch (ExecutionException e)
        {
          Throwable cause = e.getCause();
          throw cause;
        }
      }

      int expectedResult = 0;
      for (int i = 0; i < _numOfThreads; i++)
      {
        int actualResult = resultsVector.get(i);

        expectedResult += 1;
        if (actualResult != expectedResult)
        {
          String errMsg = "Expected result was - " + expectedResult + "  Actual result - " + actualResult;

          throw new RuntimeException(errMsg);
        }
        // System.out.println("FINISHED! "+actualResult);
      }

    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
    catch (Throwable e)
    {
      e.printStackTrace();
      fail(e.getMessage());
    }
    finally
    {
      executor.shutdownNow();

      publicRole.revokePermission(Operation.WRITE, multiThreadMdBusiness1.getId());
      publicRole.revokePermission(Operation.WRITE, mdAttributeInteger.getId());

      resultsVector.clear();

      multiThreadMdBusiness1 = MdBusinessDAO.get(multiThreadMdBusiness1.getId()).getBusinessDAO();
      multiThreadMdBusiness1.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
      multiThreadMdBusiness1.apply();
    }

  }

  /**
   * Change permissions for a role that users are assigned to.
   * 
   */
  public void testSessionChangeRolePermissions()
  {
    int _numOfThreads = userLockModRolePermissionsTreadNumber;
    String roleName = "runway.SomeTestRole";

    RoleDAO testRole = RoleDAO.createRole(roleName, "SomeTestRole");
    // Add the users to the role.
    for (UserDAO user : userVector)
    {
      testRole.assignMember(user);
    }

    ExecutorService executor = Executors.newFixedThreadPool(_numOfThreads);

    CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(executor);

    int loopCount = 0;
    for (final UserDAO user : userVector)
    {
      if (loopCount >= _numOfThreads)
      {
        break;
      }
      Callable<Integer> callable = new Callable<Integer>()
      {
        public Integer call()
        {
          String sessionId = "";
          sessionId = Facade.login(user.getSingleActorName(), user.getSingleActorName(), new Locale[] { CommonProperties.getDefaultLocale() });

          while (true)
          {
            try
            {
              lockCommonObjectWithUserLock(sessionId);
              updateCommonObjectWithUserLock2(sessionId);
              Facade.logout(sessionId);
              return 0;
            }
            catch (Throwable e)
            {
              boolean fail = true;
              if ( ( e instanceof RunwayExceptionDTO ) && ( ( (RunwayExceptionDTO) e ).getType().equals(LockException.class.getName()) || ( (RunwayExceptionDTO) e ).getType().equals(WritePermissionException.class.getName()) || ( (RunwayExceptionDTO) e ).getType().equals(AttributeWritePermissionException.class.getName()) ))
              {
                fail = false;
              }

              Facade.logout(sessionId);
              if (fail)
              {
                fail(e.getMessage());
              }

              sessionId = Facade.login(user.getSingleActorName(), user.getSingleActorName(), new Locale[] { CommonProperties.getDefaultLocale() });
            }
          }

        }
      };
      completionService.submit(callable);
      loopCount++;
    }

    try
    {

      Thread.sleep(5000);

      testRole.grantPermission(Operation.WRITE, multiThreadMdBusiness1.getId());
      testRole.grantPermission(Operation.WRITE, mdAttributeInteger.getId());

      // System.out.println("Changing permissions");

      // Fetch the results as the complete
      for (int i = 0; i < _numOfThreads; i++)
      {
        Future<Integer> f = completionService.take();
        try
        {
          f.get();
        }
        catch (ExecutionException e)
        {
          Throwable cause = e.getCause();
          throw cause;
        }
      }

      int expectedResult = 0;
      for (int i = 0; i < _numOfThreads; i++)
      {
        int actualResult = resultsVector.get(i);

        expectedResult += 1;
        if (actualResult != expectedResult)
        {
          String errMsg = "Expected result was - " + expectedResult + "  Actual result - " + actualResult;

          throw new RuntimeException(errMsg);
        }
        // System.out.println("FINISHED! "+actualResult);
      }

    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
    catch (Throwable e)
    {
      e.printStackTrace();
      fail(e.getMessage());
    }
    finally
    {
      executor.shutdownNow();

      resultsVector.clear();

      multiThreadMdBusiness1 = MdBusinessDAO.get(multiThreadMdBusiness1.getId()).getBusinessDAO();
      multiThreadMdBusiness1.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
      multiThreadMdBusiness1.apply();

      testRole.delete();

    }

  }

  /**
   * 
   * @param sessionId
   * @return
   */
  @Request(RequestType.SESSION)
  public static String createSingleObjectForUser(String sessionId)
  {
    Business busObject = BusinessFacade.newBusiness(multiThreadMdBusiness1.definesType());
    busObject.setValue("someInt", "1");
    busObject.apply();
    busObjectIdVector.add(busObject.getId());

    return busObject.getId();
  }

  /**
   * 
   * @param sessionId
   * @return
   */
  @Request(RequestType.SESSION)
  public static Integer updateSingleObjectWithUserLock(String sessionId, String objectId)
  {
    Business busObject = Business.get(objectId);
    busObject.lock();
    Integer intVal = new Integer(busObject.getValue("someInt"));
    intVal += 1;
    busObject.setValue("someInt", intVal.toString());

    // System.out.println("FINISHED! " +intVal);

    busObject.apply();

    return intVal;
  }

  /**
   * Change the owner permissions for a type.
   * 
   */
  public void testSessionChangeOwnerPermissions()
  {
    int _numOfThreads = userLockModOwnerPermissionsTreadNumber;

    RoleDAO ownerRole = RoleDAO.get(RoleDAO.OWNER_ID).getBusinessDAO();

    // Add the users to the role.
    for (UserDAO user : userVector)
    {
      user.grantPermission(Operation.CREATE, multiThreadMdBusiness1.getId());
      user.grantPermission(Operation.WRITE, mdAttributeInteger.getId());
    }

    ExecutorService executor = Executors.newFixedThreadPool(_numOfThreads);

    CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(executor);

    int loopCount = 0;
    for (final UserDAO user : userVector)
    {
      if (loopCount >= _numOfThreads)
      {
        break;
      }
      Callable<Integer> callable = new Callable<Integer>()
      {
        public Integer call()
        {
          String sessionId = "";
          sessionId = Facade.login(user.getSingleActorName(), user.getSingleActorName(), new Locale[] { CommonProperties.getDefaultLocale() });

          final String busObjId = createSingleObjectForUser(sessionId);

          while (true)
          {
            try
            {
              updateSingleObjectWithUserLock(sessionId, busObjId);
              Facade.logout(sessionId);
              return 0;
            }
            catch (Throwable e)
            {
              boolean fail = true;
              if ( ( e instanceof RunwayExceptionDTO ) && ( ( (RunwayExceptionDTO) e ).getType().equals(LockException.class.getName()) || ( (RunwayExceptionDTO) e ).getType().equals(WritePermissionException.class.getName()) || ( (RunwayExceptionDTO) e ).getType().equals(AttributeWritePermissionException.class.getName()) ))
              {
                fail = false;
              }

              Facade.logout(sessionId);
              if (fail)
              {
                fail(e.getMessage());
              }

              sessionId = Facade.login(user.getSingleActorName(), user.getSingleActorName(), new Locale[] { CommonProperties.getDefaultLocale() });
            }
          }

        }
      };
      completionService.submit(callable);
      loopCount++;
    }

    try
    {
      Thread.sleep(5000);

      ownerRole.grantPermission(Operation.WRITE, multiThreadMdBusiness1.getId());

      // System.out.println("Changing permissions");

      // Fetch the results as the complete
      for (int i = 0; i < _numOfThreads; i++)
      {
        Future<Integer> f = completionService.take();
        try
        {
          f.get();
        }
        catch (ExecutionException e)
        {
          Throwable cause = e.getCause();
          throw cause;
        }
      }

    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
    catch (Throwable e)
    {
      e.printStackTrace();
      fail(e.getMessage());
    }
    finally
    {
      executor.shutdownNow();

      resultsVector.clear();

      for (String id : busObjectIdVector)
      {
        BusinessDAO businessDAO = BusinessDAO.get(id).getBusinessDAO();
        businessDAO.delete();
      }

      busObjectIdVector.clear();

      multiThreadMdBusiness1 = MdBusinessDAO.get(multiThreadMdBusiness1.getId()).getBusinessDAO();
      multiThreadMdBusiness1.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
      multiThreadMdBusiness1.apply();
    }

  }

  // //////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Change the owner permissions for a type.
   * 
   */
  public void testSessionChangeRoleMethodExecutePermissions() throws Exception
  {
    MdMethodDAO mdMethod = MdMethodDAO.newInstance();
    mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, multiThreadMdBusiness1.getId());
    mdMethod.setValue(MdMethodInfo.NAME, "someMethod");
    mdMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Some Method");
    mdMethod.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.FALSE);
    mdMethod.apply();

    // Build the new source for CollecticreateSingleObjectForUseron.java
    String classStubSource = "package " + multiThreadMdBusiness1.getPackage() + ";\n" + "\n" + "\n" + "public class " + multiThreadMdBusiness1.getTypeName() + " extends " + multiThreadMdBusiness1.getTypeName() + TypeGeneratorInfo.BASE_SUFFIX + Reloadable.IMPLEMENTS + "\n" + "{\n" + "\n" + "  public " + multiThreadMdBusiness1.getTypeName() + "()\n" + "  {\n" + "    super();\n" + "  }\n" + "\n" + "  public static " + multiThreadMdBusiness1.getTypeName() + " get(String id)\n" + "  {\n" + "    return (" + multiThreadMdBusiness1.getTypeName() + ") " + Business.class.getName() + ".get(id);\n" + "  }\n" + "\n" + "  " + "@" + Authenticate.class.getName() + "\n" + "  public void someMethod()\n" + "  {\n" + "  }\n" + "}";

    multiThreadMdBusiness1 = MdBusinessDAO.get(multiThreadMdBusiness1.getId()).getBusinessDAO();
    multiThreadMdBusiness1.setValue(MdBusinessInfo.STUB_SOURCE, classStubSource);
    multiThreadMdBusiness1.apply();

    int _numOfThreads = userRoleMethodExecutePermissions;

    RoleDAO ownerRole = RoleDAO.get(RoleDAO.OWNER_ID).getBusinessDAO();

    // Add the users to the role.
    for (UserDAO user : userVector)
    {
      user.grantPermission(Operation.CREATE, multiThreadMdBusiness1.getId());
      user.grantPermission(Operation.WRITE, mdAttributeInteger.getId());
    }

    ExecutorService executor = Executors.newFixedThreadPool(_numOfThreads);

    CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(executor);

    int loopCount = 0;
    for (final UserDAO user : userVector)
    {
      if (loopCount >= _numOfThreads)
      {
        break;
      }
      Callable<Integer> callable = new Callable<Integer>()
      {
        public Integer call()
        {
          String sessionId = "";
          sessionId = Facade.login(user.getSingleActorName(), user.getSingleActorName(), new Locale[] { CommonProperties.getDefaultLocale() });

          final String busObjId = createSingleObjectForUser(sessionId);

          while (true)
          {
            try
            {
              executeMethodOnObject(sessionId, busObjId);
              // System.out.println("Did it....");
              Facade.logout(sessionId);
              return 0;
            }
            catch (Throwable e)
            {
              boolean fail = true;
              if ( ( e instanceof RunwayExceptionDTO ) && ( ( (RunwayExceptionDTO) e ).getType().equals(LockException.class.getName()) || ( (RunwayExceptionDTO) e ).getType().equals(ExecuteInstancePermissionException.class.getName()) || ( (RunwayExceptionDTO) e ).getType().equals(AttributeWritePermissionException.class.getName()) ))
              {
                fail = false;
              }

              Facade.logout(sessionId);
              if (fail)
              {
                fail(e.getMessage());
              }

              sessionId = Facade.login(user.getSingleActorName(), user.getSingleActorName(), new Locale[] { CommonProperties.getDefaultLocale() });
            }
          }

        }
      };
      completionService.submit(callable);
      loopCount++;
    }

    try
    {
      Thread.sleep(5000);

      ownerRole.grantPermission(Operation.EXECUTE, mdMethod.getId());

      // System.out.println("Changing permissions");

      // Fetch the results as the complete
      for (int i = 0; i < _numOfThreads; i++)
      {
        Future<Integer> f = completionService.take();
        try
        {
          f.get();
        }
        catch (ExecutionException e)
        {
          Throwable cause = e.getCause();
          throw cause;
        }
      }

    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
    catch (Throwable e)
    {
      e.printStackTrace();
      fail(e.getMessage());
    }
    finally
    {
      executor.shutdownNow();

      resultsVector.clear();

      for (String id : busObjectIdVector)
      {
        BusinessDAO businessDAO = BusinessDAO.get(id).getBusinessDAO();
        businessDAO.delete();
      }

      busObjectIdVector.clear();

      mdMethod.delete();

    }

  }

  /**
   * Change the owner permissions for a type.
   * 
   */
  public void testSessionChangeMethodWritePermissions() throws Exception
  {
    MdMethodDAO mdMethod = MdMethodDAO.newInstance();
    mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, multiThreadMdBusiness1.getId());
    mdMethod.setValue(MdMethodInfo.NAME, "someStaticMethod");
    mdMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Some Method");
    mdMethod.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    mdMethod.apply();

    MethodActorDAO methodActor = MethodActorDAO.newInstance();
    methodActor.setValue(MethodActorInfo.MD_METHOD, mdMethod.getId());
    methodActor.apply();

    String classStubSource = "package " + multiThreadMdBusiness1.getPackage() + ";\n" + "\n" + "\n" + "public class " + multiThreadMdBusiness1.getTypeName() + " extends " + multiThreadMdBusiness1.getTypeName() + TypeGeneratorInfo.BASE_SUFFIX + Reloadable.IMPLEMENTS + "\n" + "{\n" + "\n" + "  public " + multiThreadMdBusiness1.getTypeName() + "()\n" + "  {\n" + "    super();\n" + "  }\n" + "\n" + "  public static " + multiThreadMdBusiness1.getTypeName() + " get(String id)\n" + "  {\n" + "    return (" + multiThreadMdBusiness1.getTypeName() + ") " + Business.class.getName() + ".get(id);\n" + "  }\n" + "\n" + "  " + "@" + Authenticate.class.getName() + "\n" + "  public static void someStaticMethod() \n" + "  {\n" + "    " + multiThreadMdBusiness1.definesType() + " object = new "
        + multiThreadMdBusiness1.definesType() + "();\n" + "    object.setSomeInt(1);\n" + "    object.apply();\n" + "    object.delete();\n" + "  }\n" + "}";

    multiThreadMdBusiness1 = MdBusinessDAO.get(multiThreadMdBusiness1.getId()).getBusinessDAO();
    multiThreadMdBusiness1.setValue(MdBusinessInfo.STUB_SOURCE, classStubSource);
    multiThreadMdBusiness1.apply();

    Business busObject = BusinessFacade.newBusiness(multiThreadMdBusiness1.definesType());

    busObject.setValue("someInt", "1");
    busObject.apply();
    busObjectIdVector.add(busObject.getId());

    int _numOfThreads = methodCreatePermissions;

    RoleDAO publicRole = RoleDAO.findRole(RoleDAO.PUBLIC_ROLE).getBusinessDAO();
    publicRole.grantPermission(Operation.EXECUTE, mdMethod.getId());

    // Add the users to the role.
    ExecutorService executor = Executors.newFixedThreadPool(_numOfThreads);

    CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(executor);

    int loopCount = 0;
    for (final UserDAO user : userVector)
    {
      if (loopCount >= _numOfThreads)
      {
        break;
      }
      Callable<Integer> callable = new Callable<Integer>()
      {
        public Integer call()
        {
          String sessionId = "";
          sessionId = Facade.login(user.getSingleActorName(), user.getSingleActorName(), new Locale[] { CommonProperties.getDefaultLocale() });

          while (true)
          {
            try
            {
              executeMethodStatic(sessionId);
              // System.out.println("Did it....");
              Facade.logout(sessionId);
              return 0;
            }
            catch (Throwable e)
            {
              boolean fail = true;
              if ( ( e instanceof RunwayExceptionDTO ) && ( ( (RunwayExceptionDTO) e ).getType().equals(LockException.class.getName()) || ( (RunwayExceptionDTO) e ).getType().equals(DomainErrorException.class.getName()) ))
              {
                fail = false;
              }

              Facade.logout(sessionId);
              if (fail)
              {
                fail(e.getMessage());
              }

              sessionId = Facade.login(user.getSingleActorName(), user.getSingleActorName(), new Locale[] { CommonProperties.getDefaultLocale() });
            }
          }

        }
      };
      completionService.submit(callable);
      loopCount++;
    }

    try
    {
      Thread.sleep(5000);
      methodActor.grantPermission(Operation.CREATE, multiThreadMdBusiness1.getId());
      methodActor.grantPermission(Operation.WRITE, mdAttributeInteger.getId());
      methodActor.grantPermission(Operation.DELETE, multiThreadMdBusiness1.getId());

      // System.out.println("Changing permissions");

      // Fetch the results as the complete
      for (int i = 0; i < _numOfThreads; i++)
      {
        Future<Integer> f = completionService.take();
        try
        {
          f.get();
        }
        catch (ExecutionException e)
        {
          Throwable cause = e.getCause();
          throw cause;
        }
      }

    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
    catch (Throwable e)
    {
      e.printStackTrace();
      fail(e.getMessage());
    }
    finally
    {
      executor.shutdownNow();

      resultsVector.clear();

      this.deleteObjects();

      methodActor.delete();
      mdMethod.delete();

    }

  }

  @Transaction
  private void deleteObjects()
  {
    QueryFactory qf = new QueryFactory();

    BusinessDAOQuery b = qf.businessDAOQuery(multiThreadMdBusiness1.definesType());

    OIterator<BusinessDAOIF> i = b.getIterator();

    try
    {
      for (BusinessDAOIF businessDAOIF : i)
      {
        businessDAOIF.getBusinessDAO().delete();
      }
    }
    finally
    {
      i.close();
    }
  }

  /**
   * 
   * @param sessionId
   * @param objectId
   */
  @SuppressWarnings("unchecked")
  @Request(RequestType.SESSION)
  public static void executeMethodOnObject(String sessionId, String objectId) throws Throwable
  {
    Business busObject = Business.get(objectId);

    String classType = multiThreadMdBusiness1.definesType();
    Class objectClass = LoaderDecorator.load(classType);

    try
    {
      objectClass.getMethod("someMethod").invoke(busObject);
    }
    // catch(IllegalAccessException ex)
    // {
    // fail(ex.getMessage());
    // }
    // catch(NoSuchMethodException ex)
    // {
    // fail(ex.getMessage());
    // }
    catch (InvocationTargetException ex)
    {
      throw ex.getCause();
      // fail(ex.getMessage());
    }

  }

  /**
   * 
   * @param sessionId
   * @param objectId
   */
  @SuppressWarnings("unchecked")
  @Request(RequestType.SESSION)
  public static void executeMethodStatic(String sessionId) throws Throwable
  {
    String classType = multiThreadMdBusiness1.definesType();
    Class objectClass = LoaderDecorator.load(classType);

    try
    {
      objectClass.getMethod("someStaticMethod").invoke(null);
    }
    catch (InvocationTargetException ex)
    {
      throw ex.getCause();
    }

  }

  // //////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * 
   * @param sessionId
   * @return
   */
  @Request(RequestType.SESSION)
  public static void addChildRelationship(String sessionId, String busObjectId, String childBusObjectId, String relationshipType)
  {
    Business business = Business.get(busObjectId);

    Relationship relationship = business.addChild(childBusObjectId, relationshipType);
    relationship.apply();
  }

  /**
   * 
   * @param sessionId
   * @return
   */
  @Request(RequestType.SESSION)
  public static void addParentRelationship(String sessionId, String busObjectId, String parentBusObjectId, String relationshipType)
  {
    Business business = Business.get(busObjectId);

    Relationship relationship = business.addParent(parentBusObjectId, relationshipType);
    relationship.apply();
  }

  /**
   * Change the owner permissions for a type.
   * 
   */
  public void testRelationshipCardinality()
  {
    int _numOfThreads = relLockCardinalityThreadNumber;

    createCommonObject2();

    // assign admin role to users. This test does not test permissions on
    // relationships.
    RoleDAO adminRole = RoleDAO.findRole(RoleDAOIF.ADMIN_ROLE).getBusinessDAO();
    for (int i = 0; i < _numOfThreads; i++)
    {
      UserDAO user = userVector.get(i);
      adminRole.assignMember(user);
    }

    ExecutorService executor = Executors.newFixedThreadPool(_numOfThreads);

    CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(executor);

    int loopCount = 0;
    for (final UserDAO user : userVector)
    {
      if (loopCount >= _numOfThreads)
      {
        break;
      }
      Callable<Integer> callable = new Callable<Integer>()
      {
        public Integer call()
        {
          String sessionId = "";
          sessionId = Facade.login(user.getSingleActorName(), user.getSingleActorName(), new Locale[] { CommonProperties.getDefaultLocale() });

          boolean continueLoop = true;

          while (continueLoop)
          {
            try
            {
              addChildRelationship(sessionId, testType1ObjectId, testType2ObjectId, MULTITHREAD_RELATIONSHIP.getType());

              continueLoop = false;
            }
            catch (Throwable e)
            {
              boolean fail = true;
              if ( ( e instanceof RunwayExceptionDTO ) && ( (RunwayExceptionDTO) e ).getType().equals(RelationshipCardinalityException.class.getName()))
              {
                fail = false;
              }

              continueLoop = false;

              if (fail)
              {
                Facade.logout(sessionId);
                fail(e.getMessage());
              }

            } // try...catch
          } // while (true)

          continueLoop = true;
          while (continueLoop)
          {
            try
            {
              addParentRelationship(sessionId, testType2ObjectId, testType1ObjectId, MULTITHREAD_RELATIONSHIP.getType());

              continueLoop = false;
            }
            catch (Throwable e)
            {
              boolean fail = true;
              if ( ( e instanceof RunwayExceptionDTO ) && ( (RunwayExceptionDTO) e ).getType().equals(RelationshipCardinalityException.class.getName()))
              {
                fail = false;
              }

              continueLoop = false;

              if (fail)
              {
                Facade.logout(sessionId);
                fail(e.getMessage());
              }
            } // try...catch
          }

          Facade.logout(sessionId);

          return 1;

        }
      };
      completionService.submit(callable);
      loopCount++;
    }

    try
    {
      // Fetch the results as the complete
      for (int i = 0; i < _numOfThreads; i++)
      {
        Future<Integer> f = completionService.take();
        try
        {
          f.get();
        }
        catch (ExecutionException e)
        {
          Throwable cause = e.getCause();
          throw cause;
        }
      }
      Business testType1Object = Business.get(testType1ObjectId);
      Business testType2Object = Business.get(testType2ObjectId);

      assertEquals("Parent cardinality constraint did not match.", relationshipCardinality, testType2Object.getParentRelationships(MULTITHREAD_RELATIONSHIP.getType()).getAll().size());
      assertEquals("Child cardinality constraint did not match.", relationshipCardinality, testType1Object.getChildRelationships(MULTITHREAD_RELATIONSHIP.getType()).getAll().size());

    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
    catch (Throwable e)
    {
      e.printStackTrace();
      fail(e.getMessage());
    }
    finally
    {
      executor.shutdownNow();

      for (int i = 0; i < _numOfThreads; i++)
      {
        UserDAO user = userVector.get(i);
        adminRole.deassignMember(user);
      }

      deleteCommonObject2();
    }

  }

}
