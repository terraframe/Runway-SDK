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
package com.runwaysdk.session;

import java.util.Locale;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.UserInfo;

/**
 * This test is designed to test the SessionCacheCleanupWorker to make sure that when a session
 * expires it is automatically cleaned up.
 * 
 * We can't put this test in the SessionTest or IntegratedSessionTest because it's incompatible
 * with the way those tests are injecting the cache. It also doesn't need to be run for all the
 * different cache types.
 * 
 * @author rrowlands
 */
public class SessionCleanupTest
{
  @BeforeClass
  public static void classSetUp()
  {
    // If this is run in the context of the SessionTestSuite then the worker thread might not be running
    // because of how the injector logic works.
    SessionCacheCleanupWorker.startWorkerThread();
  }
  
  @Request
  @Test
  public void testCleanUp() throws InterruptedException
  {
    Session.setSessionTime(10);
    
    // Create a new session
    String sessionId = SessionFacade.logIn(UserInfo.PUBLIC_USER_NAME, ServerConstants.PUBLIC_USER_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

    // Convert the session time(sec) to milliseconds and
    // add enough time to ensure that the a session will expire
    long sessionTime = ( Session.getSessionTime() + 15 ) * 1000;
    long waitTime = System.currentTimeMillis() + sessionTime;

    // Get the public session
    SessionIF publicSession = SessionFacade.getPublicSession();
    Assert.assertNotNull(publicSession);

    // Ensure the session is active
    Assert.assertEquals(true, SessionFacade.containsSession(sessionId));

    // Wait until the session is expired
    while (System.currentTimeMillis() < waitTime)
    {
      Thread.sleep(100);
    }

    // Ensure that the public session has not been cleaned up
    Assert.assertTrue(SessionFacade.containsSession(publicSession.getOid()));
    Assert.assertEquals(publicSession.getOid(), SessionFacade.getPublicSession().getOid());

    try
    {
      SessionFacade.getSession(sessionId);

      Assert.fail("Expired session was not cleaned up");
    }
    catch (InvalidSessionException e)
    {
      // This is expected
    }
    
    Session.setSessionTime(CommonProperties.getSessionTime());
  }
}
