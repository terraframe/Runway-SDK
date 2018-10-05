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
package com.runwaysdk;

import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Locale;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.facade.TestRMIUtil;
import com.runwaysdk.request.SslRMIClientSocketFactory;

public class RMIClient
{
  public static void main(String[] args) throws Exception
  {
    KeyStore ts = KeyStore.getInstance("jks");
    ts.load(TestRMIUtil.class.getResourceAsStream("/trust.keystore"), "keystore".toCharArray());

    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(ts);

    SSLContext context = SSLContext.getInstance("SSL");
    context.init(new KeyManager[] {}, tmf.getTrustManagers(), new SecureRandom());

    SslRMIClientSocketFactory.setFactory(context.getSocketFactory());

    ClientSession session = ClientSession.createUserSession("rmiDefault", ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      System.out.println("Client has logged in.");
    }
    finally
    {
      session.logout();
    }

  }
}
