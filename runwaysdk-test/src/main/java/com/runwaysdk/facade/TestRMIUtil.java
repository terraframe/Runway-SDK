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
package com.runwaysdk.facade;

import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class TestRMIUtil
{
  public static void startServer()
  {
    // Setup the SSL context used by the RMI client and server
    try
    {
      KeyStore ks = KeyStore.getInstance("jks");
      ks.load(TestRMIUtil.class.getResourceAsStream("/server.keystore"), "keystore".toCharArray());

      KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
      kmf.init(ks, "keystore".toCharArray());

      KeyStore ts = KeyStore.getInstance("jks");
      ts.load(TestRMIUtil.class.getResourceAsStream("/trust.keystore"), "keystore".toCharArray());

      TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      tmf.init(ts);

      SSLContext context = SSLContext.getInstance("SSL");
      context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

      SSLContext.setDefault(context);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }

    RemoteAdapterServer.startServer();
  }
}
