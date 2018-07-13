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
package com.runwaysdk.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class MockServletRequest implements ServletRequestIF
{
  private Map<String, String[]> parameters;

  private Map<String, Object>   attributes;

  private MockRequestDispatcher dispatcher;

  private String                path;

  public MockServletRequest()
  {
    this.parameters = new HashMap<String, String[]>();
    this.attributes = new HashMap<String, Object>();
    this.dispatcher = new MockRequestDispatcher();
  }

  @Override
  public String changeSessionId()
  {

    return null;
  }

  @Override
  public AsyncContext getAsyncContext()
  {

    return null;
  }

  @Override
  public Object getAttribute(String name)
  {
    return this.attributes.get(name);
  }

  @Override
  public Enumeration<String> getAttributeNames()
  {

    return null;
  }

  @Override
  public String getAuthType()
  {

    return null;
  }

  @Override
  public String getCharacterEncoding()
  {

    return null;
  }

  @Override
  public int getContentLength()
  {

    return 0;
  }

  @Override
  public long getContentLengthLong()
  {

    return 0;
  }

  @Override
  public String getContentType()
  {

    return null;
  }

  @Override
  public String getContextPath()
  {
    return "";
  }

  @Override
  public Cookie[] getCookies()
  {

    return null;
  }

  @Override
  public long getDateHeader(String name)
  {

    return 0;
  }

  @Override
  public DispatcherType getDispatcherType()
  {

    return null;
  }

  @Override
  public String getHeader(String name)
  {

    return null;
  }

  @Override
  public Enumeration<String> getHeaderNames()
  {

    return null;
  }

  @Override
  public Enumeration<String> getHeaders(String name)
  {

    return null;
  }

  @Override
  public ServletInputStream getInputStream() throws IOException
  {

    return null;
  }

  @Override
  public int getIntHeader(String name)
  {

    return 0;
  }

  @Override
  public String getLocalAddr()
  {

    return null;
  }

  @Override
  public Locale getLocale()
  {
    return Locale.US;
  }

  @Override
  public Enumeration<Locale> getLocales()
  {
    LinkedList<Locale> locales = new LinkedList<Locale>();
    locales.add(Locale.US);

    return Collections.enumeration(locales);
  }

  @Override
  public String getLocalName()
  {
    return null;
  }

  @Override
  public int getLocalPort()
  {
    return 0;
  }

  @Override
  public String getMethod()
  {
    return null;
  }

  @Override
  public String getParameter(String name)
  {
    if (this.parameters.containsKey(name))
    {
      return this.parameters.get(name)[0];
    }

    return null;
  }

  public void setParameter(String name, String value)
  {
    this.parameters.put(name, new String[] { value });
  }

  @Override
  public Map<String, String[]> getParameterMap()
  {
    return this.parameters;
  }

  @Override
  public Enumeration<String> getParameterNames()
  {
    return Collections.enumeration(this.parameters.keySet());
  }

  @Override
  public String[] getParameterValues(String name)
  {
    return this.parameters.get(name);
  }

  @Override
  public Part getPart(String name) throws IOException, ServletException
  {

    return null;
  }

  @Override
  public Collection<Part> getParts() throws IOException, ServletException
  {

    return null;
  }

  @Override
  public String getPathInfo()
  {

    return null;
  }

  @Override
  public String getPathTranslated()
  {

    return null;
  }

  @Override
  public String getProtocol()
  {

    return null;
  }

  @Override
  public String getQueryString()
  {

    return null;
  }

  @Override
  public BufferedReader getReader() throws IOException
  {

    return null;
  }

  @Override
  public String getRemoteAddr()
  {

    return null;
  }

  @Override
  public String getRemoteHost()
  {

    return null;
  }

  @Override
  public int getRemotePort()
  {

    return 0;
  }

  @Override
  public String getRemoteUser()
  {

    return null;
  }

  @Override
  public RequestDispatcherIF getRequestDispatcher(String location)
  {
    this.dispatcher.setLocation(location);

    return this.dispatcher;
  }

  public MockRequestDispatcher getDispatcher()
  {
    return dispatcher;
  }

  @Override
  public String getRequestedSessionId()
  {

    return null;
  }

  @Override
  public String getRequestURI()
  {

    return null;
  }

  @Override
  public StringBuffer getRequestURL()
  {

    return null;
  }

  @Override
  public String getScheme()
  {

    return null;
  }

  @Override
  public String getServerName()
  {

    return null;
  }

  @Override
  public int getServerPort()
  {

    return 0;
  }

  @Override
  public ServletContext getServletContext()
  {
    return null;
  }

  public void setServletPath(String path)
  {
    this.path = path;
  }

  @Override
  public String getServletPath()
  {

    return this.path;
  }

  @Override
  public HttpSession getSession()
  {

    return null;
  }

  @Override
  public HttpSession getSession(boolean create)
  {

    return null;
  }

  @Override
  public Principal getUserPrincipal()
  {

    return null;
  }

  @Override
  public boolean isAsyncStarted()
  {

    return false;
  }

  @Override
  public boolean isAsyncSupported()
  {

    return false;
  }

  @Override
  public boolean isRequestedSessionIdFromCookie()
  {

    return false;
  }

  @Override
  public boolean isRequestedSessionIdFromURL()
  {

    return false;
  }

  @Override
  public boolean isRequestedSessionIdValid()
  {

    return false;
  }

  @Override
  public boolean isSecure()
  {

    return false;
  }

  @Override
  public boolean isUserInRole(String role)
  {

    return false;
  }

  @Override
  public void login(String username, String password) throws ServletException
  {

  }

  @Override
  public void logout() throws ServletException
  {

  }

  @Override
  public void removeAttribute(String name)
  {

  }

  @Override
  public void setAttribute(String name, Object o)
  {
    this.attributes.put(name, o);
  }

  @Override
  public void setCharacterEncoding(String env) throws UnsupportedEncodingException
  {

  }

  @Override
  public boolean isMultipartContent()
  {
    return false;
  }

  @Override
  public List<FileItem> getFileItems(ServletFileUpload upload) throws FileUploadException
  {
    return null;
  }
}
