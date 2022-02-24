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
package com.runwaysdk.request;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;
import java.util.StringTokenizer;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SslRMIClientSocketFactory implements RMIClientSocketFactory, Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 9145956358512570757L;

  /**
   * <p>
   * Creates a new <code>SslRMIClientSocketFactory</code>.
   * </p>
   */
  public SslRMIClientSocketFactory()
  {
    // We don't force the initialization of the default SSLSocketFactory
    // at construction time - because the RMI client socket factory is
    // created on the server side, where that initialization is a priori
    // meaningless, unless both server and client run in the same JVM.
    // We could possibly override readObject() to force this initialization,
    // but it might not be a good idea to actually mix this with possible
    // deserialization problems.
    // So contrarily to what we do for the server side, the initialization
    // of the SSLSocketFactory will be delayed until the first time
    // createSocket() is called - note that the default SSLSocketFactory
    // might already have been initialized anyway if someone in the JVM
    // already called SSLSocketFactory.getDefault().
    //
  }

  /**
   * <p>
   * Creates an SSL socket.
   * </p>
   *
   * <p>
   * If the system property
   * <code>javax.rmi.ssl.client.enabledCipherSuites</code> is specified, this
   * method will call {@link SSLSocket#setEnabledCipherSuites(String[])} before
   * returning the socket. The value of this system property is a string that is
   * a comma-separated list of SSL/TLS cipher suites to enable.
   * </p>
   *
   * <p>
   * If the system property <code>javax.rmi.ssl.client.enabledProtocols</code>
   * is specified, this method will call
   * {@link SSLSocket#setEnabledProtocols(String[])} before returning the
   * socket. The value of this system property is a string that is a
   * comma-separated list of SSL/TLS protocol versions to enable.
   * </p>
   */
  public Socket createSocket(String host, int port) throws IOException
  {
    // Retrieve the SSLSocketFactory
    //
    final SocketFactory sslSocketFactory = getFactory();
    // Create the SSLSocket
    //
    final SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(host, port);
    // Set the SSLSocket Enabled Cipher Suites
    //
    final String enabledCipherSuites = (String) System.getProperty("javax.rmi.ssl.client.enabledCipherSuites");
    if (enabledCipherSuites != null)
    {
      StringTokenizer st = new StringTokenizer(enabledCipherSuites, ",");
      int tokens = st.countTokens();
      String enabledCipherSuitesList[] = new String[tokens];
      for (int i = 0; i < tokens; i++)
      {
        enabledCipherSuitesList[i] = st.nextToken();
      }
      try
      {
        sslSocket.setEnabledCipherSuites(enabledCipherSuitesList);
      }
      catch (IllegalArgumentException e)
      {
        throw (IOException) new IOException(e.getMessage()).initCause(e);
      }
    }
    // Set the SSLSocket Enabled Protocols
    //
    final String enabledProtocols = (String) System.getProperty("javax.rmi.ssl.client.enabledProtocols");
    if (enabledProtocols != null)
    {
      StringTokenizer st = new StringTokenizer(enabledProtocols, ",");
      int tokens = st.countTokens();
      String enabledProtocolsList[] = new String[tokens];
      for (int i = 0; i < tokens; i++)
      {
        enabledProtocolsList[i] = st.nextToken();
      }
      try
      {
        sslSocket.setEnabledProtocols(enabledProtocolsList);
      }
      catch (IllegalArgumentException e)
      {
        throw (IOException) new IOException(e.getMessage()).initCause(e);
      }
    }
    // Return the preconfigured SSLSocket
    //
    return sslSocket;
  }

  /**
   * <p>
   * Indicates whether some other object is "equal to" this one.
   * </p>
   *
   * <p>
   * Because all instances of this class are functionally equivalent (they all
   * use the default <code>SSLSocketFactory</code>), this method simply returns
   * <code>this.getClass().equals(obj.getClass())</code>.
   * </p>
   *
   * <p>
   * A subclass should override this method (as well as {@link #hashCode()}) if
   * its instances are not all functionally equivalent.
   * </p>
   */
  public boolean equals(Object obj)
  {
    if (obj == null)
    {
      return false;
    }
    if (obj == this)
    {
      return true;
    }

    return this.getClass().equals(obj.getClass());
  }

  /**
   * <p>
   * Returns a hash code value for this <code>SslRMIClientSocketFactory</code>.
   * </p>
   *
   * @return a hash code value for this <code>SslRMIClientSocketFactory</code>.
   */
  public int hashCode()
  {
    return this.getClass().hashCode();
  }

  private static SocketFactory factory;

  public static synchronized SocketFactory getFactory()
  {
    if (factory == null)
    {
      factory = SSLSocketFactory.getDefault();
    }

    return factory;
  }

  public static synchronized void setFactory(SocketFactory factory)
  {
    SslRMIClientSocketFactory.factory = factory;
  }
}
