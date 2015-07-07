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
package com.runwaysdk.facade;

import java.rmi.Remote;
import java.util.Locale;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.runwaysdk.ClientSession;
import com.runwaysdk.business.generation.facade.RMIFacadeProxyGenerator;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.generation.loader.WebTestGeneratedClassLoader;
import com.runwaysdk.request.RMIClientRequest;

public class RMIFacadeGenerationTest extends FacadeGenerationTest
{
  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(RMIFacadeGenerationTest.suite());
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTestSuite(RMIFacadeGenerationTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp() throws Exception
      {
        TestRMIUtil.startServer();

        label = "rmiDefault";

        systemSession = ClientSession.createUserSession(label, ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

        try
        {
          clientRequest = systemSession.getRequest();
          clientRequest.setKeepMessages(false);

          classSetUp();
          finalizeSetup();

          // Bind the remote controller to the RMI Server
          String adapterName = pack + ".RMI" + facadeName + "Adapter";
          String clientRequestName = pack + ".RMI" + facadeName + "Proxy";
          String address = "//localhost:" + CommonProperties.getRMIPort() + "/";
          String serviceName = null;

          Class<?> controllerClass = WebTestGeneratedClassLoader.load(adapterName);
          serviceName = (String) controllerClass.getField("SERVICE_NAME").get(serviceName);
          Remote controller = (Remote) controllerClass.getConstructor().newInstance();
          RemoteAdapterServer.bindNewAdapter(controller, serviceName);

          // Load the clientRequest with the new
          facadeProxyClass = LoaderDecorator.load(clientRequestName);
          generatedProxy = facadeProxyClass.getConstructor(String.class, String.class).newInstance(label, address);
        }
        catch (Exception e)
        {
          systemSession.logout();
        }
      }

      protected void tearDown()
      {
        classTearDown();

        try
        {
          facadeProxyClass.getMethod(RMIFacadeProxyGenerator.UNBIND_METHOD_NAME).invoke(generatedProxy);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }

        ( (RMIClientRequest) clientRequest ).unbindRMIClientRequest();

        RemoteAdapterServer.stopServer();
      }
    };

    return wrapper;
  }
}
