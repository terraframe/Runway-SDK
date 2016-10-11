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
package com.runwaysdk.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class RequestDecorator implements ServletRequestIF
{
  private HttpServletRequest request;

  public RequestDecorator(HttpServletRequest request)
  {
    this.request = request;
  }

  public HttpServletRequest getRequest()
  {
    return request;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#changeSessionId()
   */
  @Override
  public String changeSessionId()
  {
    return this.request.changeSessionId();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getAsyncContext()
   */
  @Override
  public AsyncContext getAsyncContext()
  {
    return this.request.getAsyncContext();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.controller.RequestDecoratorIF#getAttribute(java.lang.String)
   */
  @Override
  public Object getAttribute(String name)
  {
    return this.request.getAttribute(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getAttributeNames()
   */
  @Override
  public Enumeration<String> getAttributeNames()
  {
    return this.request.getAttributeNames();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getAuthType()
   */
  @Override
  public String getAuthType()
  {
    return this.request.getAuthType();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getCharacterEncoding()
   */
  @Override
  public String getCharacterEncoding()
  {
    return this.request.getCharacterEncoding();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getContentLength()
   */
  @Override
  public int getContentLength()
  {
    return this.request.getContentLength();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getContentLengthLong()
   */
  @Override
  public long getContentLengthLong()
  {
    return this.request.getContentLengthLong();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getContentType()
   */
  @Override
  public String getContentType()
  {
    return this.request.getContentType();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getContextPath()
   */
  @Override
  public String getContextPath()
  {
    return this.request.getContextPath();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getCookies()
   */
  @Override
  public Cookie[] getCookies()
  {
    return this.request.getCookies();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.controller.RequestDecoratorIF#getDateHeader(java.lang.String)
   */
  @Override
  public long getDateHeader(String name)
  {
    return this.request.getDateHeader(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getDispatcherType()
   */
  @Override
  public DispatcherType getDispatcherType()
  {
    return this.request.getDispatcherType();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.controller.RequestDecoratorIF#getHeader(java.lang.String)
   */
  @Override
  public String getHeader(String name)
  {
    return this.request.getHeader(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getHeaderNames()
   */
  @Override
  public Enumeration<String> getHeaderNames()
  {
    return this.request.getHeaderNames();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.controller.RequestDecoratorIF#getHeaders(java.lang.String)
   */
  @Override
  public Enumeration<String> getHeaders(String name)
  {
    return this.request.getHeaders(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getInputStream()
   */
  @Override
  public ServletInputStream getInputStream() throws IOException
  {
    return this.request.getInputStream();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.controller.RequestDecoratorIF#getIntHeader(java.lang.String)
   */
  @Override
  public int getIntHeader(String name)
  {
    return this.request.getIntHeader(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getLocalAddr()
   */
  @Override
  public String getLocalAddr()
  {
    return this.request.getLocalAddr();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getLocale()
   */
  @Override
  public Locale getLocale()
  {
    return this.request.getLocale();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getLocales()
   */
  @Override
  public Enumeration<Locale> getLocales()
  {
    return this.request.getLocales();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getLocalName()
   */
  @Override
  public String getLocalName()
  {
    return this.request.getLocalName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getLocalPort()
   */
  @Override
  public int getLocalPort()
  {
    return this.request.getLocalPort();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getMethod()
   */
  @Override
  public String getMethod()
  {
    return this.request.getMethod();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.controller.RequestDecoratorIF#getParameter(java.lang.String)
   */
  @Override
  public String getParameter(String name)
  {
    return this.request.getParameter(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getParameterMap()
   */
  @Override
  public Map<String, String[]> getParameterMap()
  {
    return this.request.getParameterMap();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getParameterNames()
   */
  @Override
  public Enumeration<String> getParameterNames()
  {
    return this.request.getParameterNames();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.controller.RequestDecoratorIF#getParameterValues(java.lang
   * .String)
   */
  @Override
  public String[] getParameterValues(String name)
  {
    return this.request.getParameterValues(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getPart(java.lang.String)
   */
  @Override
  public Part getPart(String name) throws IOException, ServletException
  {
    return this.request.getPart(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getParts()
   */
  @Override
  public Collection<Part> getParts() throws IOException, ServletException
  {
    return this.request.getParts();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getPathInfo()
   */
  @Override
  public String getPathInfo()
  {
    return this.request.getPathInfo();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getPathTranslated()
   */
  @Override
  public String getPathTranslated()
  {
    return this.request.getPathTranslated();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getProtocol()
   */
  @Override
  public String getProtocol()
  {
    return this.request.getProtocol();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getQueryString()
   */
  @Override
  public String getQueryString()
  {
    return this.request.getQueryString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getReader()
   */
  @Override
  public BufferedReader getReader() throws IOException
  {
    return this.request.getReader();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getRemoteAddr()
   */
  @Override
  public String getRemoteAddr()
  {
    return this.request.getRemoteAddr();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getRemoteHost()
   */
  @Override
  public String getRemoteHost()
  {
    return this.request.getRemoteHost();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getRemotePort()
   */
  @Override
  public int getRemotePort()
  {
    return this.request.getRemotePort();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getRemoteUser()
   */
  @Override
  public String getRemoteUser()
  {
    return this.request.getRemoteUser();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.controller.RequestDecoratorIF#getRequestDispatcher(java.lang
   * .String)
   */
  @Override
  public RequestDispatcherIF getRequestDispatcher(String path)
  {
    return new DispatchDecorator(this.request.getRequestDispatcher(path));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getRequestedSessionId()
   */
  @Override
  public String getRequestedSessionId()
  {
    return this.request.getRequestedSessionId();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getRequestURI()
   */
  @Override
  public String getRequestURI()
  {
    return this.request.getRequestURI();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getRequestURL()
   */
  @Override
  public StringBuffer getRequestURL()
  {
    return this.request.getRequestURL();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getScheme()
   */
  @Override
  public String getScheme()
  {
    return this.request.getScheme();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getServerName()
   */
  @Override
  public String getServerName()
  {
    return this.request.getServerName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getServerPort()
   */
  @Override
  public int getServerPort()
  {
    return this.request.getServerPort();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getServletContext()
   */
  @Override
  public ServletContext getServletContext()
  {
    return this.request.getServletContext();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getServletPath()
   */
  @Override
  public String getServletPath()
  {
    return this.request.getServletPath();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getSession()
   */
  @Override
  public HttpSession getSession()
  {
    return this.request.getSession();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getSession(boolean)
   */
  @Override
  public HttpSession getSession(boolean create)
  {
    return this.request.getSession(create);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#getUserPrincipal()
   */
  @Override
  public Principal getUserPrincipal()
  {
    return this.request.getUserPrincipal();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#isAsyncStarted()
   */
  @Override
  public boolean isAsyncStarted()
  {
    return this.request.isAsyncStarted();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#isAsyncSupported()
   */
  @Override
  public boolean isAsyncSupported()
  {
    return this.request.isAsyncSupported();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.controller.RequestDecoratorIF#isRequestedSessionIdFromCookie
   * ()
   */
  @Override
  public boolean isRequestedSessionIdFromCookie()
  {
    return this.request.isRequestedSessionIdFromCookie();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.controller.RequestDecoratorIF#isRequestedSessionIdFromURL()
   */
  @Override
  public boolean isRequestedSessionIdFromURL()
  {
    return this.request.isRequestedSessionIdFromURL();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.controller.RequestDecoratorIF#isRequestedSessionIdValid()
   */
  @Override
  public boolean isRequestedSessionIdValid()
  {
    return this.request.isRequestedSessionIdValid();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#isSecure()
   */
  @Override
  public boolean isSecure()
  {
    return this.request.isSecure();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.controller.RequestDecoratorIF#isUserInRole(java.lang.String)
   */
  @Override
  public boolean isUserInRole(String role)
  {
    return this.request.isUserInRole(role);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#login(java.lang.String,
   * java.lang.String)
   */
  @Override
  public void login(String username, String password) throws ServletException
  {
    this.request.login(username, password);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.RequestDecoratorIF#logout()
   */
  @Override
  public void logout() throws ServletException
  {
    this.request.logout();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.controller.RequestDecoratorIF#removeAttribute(java.lang.String
   * )
   */
  @Override
  public void removeAttribute(String name)
  {
    this.request.removeAttribute(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.controller.RequestDecoratorIF#setAttribute(java.lang.String,
   * java.lang.Object)
   */
  @Override
  public void setAttribute(String name, Object o)
  {
    this.request.setAttribute(name, o);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.controller.RequestDecoratorIF#setCharacterEncoding(java.lang
   * .String)
   */
  @Override
  public void setCharacterEncoding(String env) throws UnsupportedEncodingException
  {
    this.request.setCharacterEncoding(env);
  }

  @Override
  public boolean isMultipartContent()
  {
    return ServletFileUpload.isMultipartContent(this.request);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<FileItem> getFileItems(ServletFileUpload upload) throws FileUploadException
  {
    return upload.parseRequest(this.request);
  }
}
