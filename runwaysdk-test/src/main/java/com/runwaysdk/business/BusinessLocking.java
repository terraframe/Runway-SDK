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
package com.runwaysdk.business;

import java.util.Locale;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.runwaysdk.ClasspathTestRunner;
import com.runwaysdk.RunwayExceptionDTO;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.dataaccess.EntityMasterTestSetup;
import com.runwaysdk.facade.Facade;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

@RunWith(ClasspathTestRunner.class)
public class BusinessLocking
{
  private static final TypeInfo lockTestInfo = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "LockTest");

  private static Business       testMdEntity = null;

  /**
   * The test user object
   */
  private static UserDAO        newUser;

  /**
   * The username for the user
   */
  private final static String   username     = "johndoe";

  /**
   * The password for the user
   */
  private final static String   password     = "12345";

  /**
   * The maximum number of sessions for the user
   */
  private final static int      sessionLimit = 1;

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    testMdEntity = BusinessFacade.newBusiness(MdBusinessInfo.CLASS);
    testMdEntity.setValue(MdBusinessInfo.NAME, lockTestInfo.getTypeName());
    testMdEntity.setValue(MdBusinessInfo.PACKAGE, lockTestInfo.getPackageName());
    testMdEntity.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    testMdEntity.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Query Ref Type");
    testMdEntity.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary ref type to perform queries on.");
    testMdEntity.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    testMdEntity.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    testMdEntity.apply();

    Business integerMdAttribute = BusinessFacade.newBusiness(MdAttributeIntegerInfo.CLASS);
    integerMdAttribute.setValue(MdAttributeIntegerInfo.NAME, "integerAttr");
    integerMdAttribute.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "An integer");
    integerMdAttribute.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
    integerMdAttribute.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    integerMdAttribute.setValue(MdAttributeIntegerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    integerMdAttribute.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, testMdEntity.getId());
    integerMdAttribute.apply();

    // Create a new user
    newUser = UserDAO.newInstance();
    newUser.setValue(UserInfo.USERNAME, username);
    newUser.setValue(UserInfo.PASSWORD, password);
    newUser.setValue(UserInfo.SESSION_LIMIT, Integer.toString(sessionLimit));
    newUser.apply();

    RoleDAOIF adminRoleIF = RoleDAO.findRole(RoleDAOIF.ADMIN_ROLE);
    ( adminRoleIF.getBusinessDAO() ).assignMember(newUser);
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    testMdEntity.delete();
    newUser.delete();
  }

  /**
   * Makes sure you cannot delete an object unless the object has a user lock.
   *
   */
  @Request
  @Test
  public void testDeleteWithUserLock()
  {
    String sessionId = null;
    try
    {
      sessionId = Facade.login(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

      deleteWithUserLock(sessionId);
    }
    catch (LockException e)
    {
      Assert.fail("Unable to delete an object even though a proper lock was attained.\n" + e.getMessage());
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public void deleteWithUserLock(String sessionId)
  {
    Business instance = new Business(lockTestInfo.getType());
    instance.apply();

    instance.lock();
    instance.delete();
  }

  /**
   * Makes sure you cannot delete an object unless the object has a user lock.
   *
   */
  @Request
  @Test
  public void testDeleteWithAppLock()
  {
    String sessionId = null;
    try
    {
      sessionId = Facade.login(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

      deleteWithAppLock(sessionId);
    }
    catch (LockException e)
    {
      Assert.fail("Unable to delete an object even though a proper lock was attained.\n" + e.getMessage());
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public void deleteWithAppLock(String sessionId)
  {
    Business instance = new Business(lockTestInfo.getType());
    instance.apply();

    instance.appLock();
    instance.delete();
  }

  /**
   * Tests a correct use of locking an object.
   */
  @Request
  @Test
  public void testCorrectLocking()
  {
    String sessionId = null;
    try
    {
      sessionId = Facade.login(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

      correctLocking(sessionId);
    }
    catch (LockException e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public void correctLocking(String sessionId)
  {
    Business instance = null;

    try
    {
      instance = new Business(lockTestInfo.getType());
      instance.apply();

      instance.lock();
      instance.setValue("integerAttr", "53");
      instance.apply();
    }
    finally
    {
      if (instance != null && !instance.isNew())
      {
        instance.lock();
        instance.delete();
      }
    }
  }

  /**
   * Tests an incorrect use of locking by unlocking the object before it's
   * applied.
   */
  @Request
  @Test
  public void testApplyNoUserLock()
  {
    String sessionId = null;
    try
    {
      sessionId = Facade.login(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

      applyNoUserLock(sessionId);

      Facade.logout(sessionId);
    }
    catch (Throwable e)
    {
      boolean fail = true;

      if (e instanceof RunwayExceptionDTO)
      {
        RunwayExceptionDTO runwayExceptionDTO = (RunwayExceptionDTO) e;
        if (runwayExceptionDTO.getType().equals(LockException.class.getName()))
        {
          fail = false;
        }
      }

      if (fail)
      {
        Assert.fail(e.getMessage());
      }
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public void applyNoUserLock(String sessionId)
  {
    Business instance = null;

    try
    {
      instance = new Business(lockTestInfo.getType());

      instance.apply();

      instance.lock();
      instance.setValue("integerAttr", "53");
      instance.unlock();

      instance.apply();

      Assert.fail("An object was able to be applied without being locked.");
    }
    finally
    {
      if (instance != null && !instance.isNew())
      {
        instance.delete();
      }
    }
  }

  /**
   * Tests an incorrect use of locking by not locking an object before setting
   * attribute values.
   */
  @Request
  @Test
  public void testSetValueNoUserLock()
  {
    String sessionId = null;
    try
    {
      sessionId = Facade.login(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

      setValueNoUserLock(sessionId);
    }
    catch (Throwable e)
    {
      boolean fail = true;

      if (e instanceof RunwayExceptionDTO)
      {
        RunwayExceptionDTO runwayExceptionDTO = (RunwayExceptionDTO) e;
        if (runwayExceptionDTO.getType().equals(LockException.class.getName()))
        {
          fail = false;
        }
      }

      if (fail)
      {
        Assert.fail(e.getMessage());
      }
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public void setValueNoUserLock(String sessionId)
  {
    Business instance = null;

    try
    {
      instance = new Business(lockTestInfo.getType());
      instance.apply();

      instance.setValue("integerAttr", "53");

      Assert.fail("An object was able to set an attribute value without being locked.");
    }
    finally
    {
      if (instance != null && !instance.isNew())
      {
        instance.delete();
      }
    }
  }

  /**
   * Make sure you cannot attain an application lock on an object that does not
   * have an application lock, but has a user lock.
   *
   */
  @Request
  @Test
  public void testUserAppLockInterferrence()
  {
    String systemSessionId = null;
    String userSessionId = null;

    try
    {
      systemSessionId = Facade.login(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
      String objectId = systemLockSomeObject(systemSessionId);

      userSessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });
      tryAppLock(userSessionId, objectId);

      // delete the object that was created
      Facade.delete(systemSessionId, objectId);
    }
    catch (BusinessException e)
    {
      // we want to land here
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      Facade.logout(systemSessionId);
      Facade.logout(userSessionId);
    }
  }

  /**
   *
   * @param sessionId
   * @return
   */
  @Request(RequestType.SESSION)
  private String systemLockSomeObject(String sessionId)
  {
    Business instance = new Business(lockTestInfo.getType());

    instance.apply();

    instance.lock();

    return instance.getId();
  }

  /**
   *
   * @param sessionId
   * @param objectId
   */
  @Request(RequestType.SESSION)
  private void tryAppLock(String sessionId, String objectId)
  {
    try
    {
      Business instance = Business.get(objectId);
      instance.appLock();
      Assert.fail("One user was able to get an application lock on the object that has a user lock from another user.");
    }
    catch (LockException e)
    {
      // we want to be here
    }
  }

  private String testSessionId;

  private String testObjectId;

  /**
   * Tests an incorrect use of locking by not locking an object before setting
   * attribute values.
   */
  @Request
  @Test
  public void testSetValueExistingOtherAppLock()
  {
    String userSessionId = null;

    Thread thread = null;

    try
    {
      testSessionId = Facade.login(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
      testObjectId = createSomeObject(testSessionId);

      thread = new Thread()
      {
        public void run()
        {
          setAppLockAndSleep(testSessionId, testObjectId);
        }
      };
      thread.start();

      // Make sure the thread has an opportunity to set the lock;
      Thread.sleep(2000);

      userSessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });
      setValueWithExistingAppLock(userSessionId, testObjectId);
      Assert.fail("Able to set the value of an object that has an active application lock set by another thread.");
    }
    catch (Throwable e)
    {
      boolean fail = true;

      if (e instanceof RunwayExceptionDTO)
      {
        RunwayExceptionDTO runwayExceptionDTO = (RunwayExceptionDTO) e;
        if (runwayExceptionDTO.getType().equals(LockException.class.getName()))
        {
          fail = false;
        }
      }

      if (fail)
      {
        Assert.fail(e.getMessage());
      }
    }
    finally
    {
      thread.interrupt();
      try
      {
        // give the other thread a chance to finish
        Thread.sleep(2000);
      }
      catch (InterruptedException e)
      {
        Assert.fail(e.getMessage());
      }
      deleteSomeObject(testSessionId, testObjectId);

      Facade.logout(testSessionId);
      Facade.logout(userSessionId);
    }
  }

  @Request(RequestType.SESSION)
  private String createSomeObject(String sessionId)
  {
    Business instance = new Business(lockTestInfo.getType());
    instance.apply();

    return instance.getId();
  }

  @Request(RequestType.SESSION)
  private synchronized void setAppLockAndSleep(String sessionId, String objectid)
  {
    try
    {
      Business instance = Business.get(objectid);
      instance.appLock();
      this.wait();
    }
    catch (InterruptedException e)
    {
      // This thread will be interrupted, in which case we will let it continue.
    }

  }

  @Request(RequestType.SESSION)
  private void setValueWithExistingAppLock(String sessionId, String objectid)
  {
    Business instance = Business.get(objectid);
    instance.setValue("integerAttr", "53");
  }

  @Request(RequestType.SESSION)
  private void deleteSomeObject(String sessionId, String objectid)
  {
    Business instance = Business.get(objectid);
    instance.lock();
    instance.delete();
  }
}
