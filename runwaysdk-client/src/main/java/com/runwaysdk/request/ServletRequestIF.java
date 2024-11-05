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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.fileupload2.core.FileItem;
import org.apache.commons.fileupload2.core.FileUploadException;
import org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletFileUpload;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

public interface ServletRequestIF
{
  public String changeSessionId();

  public AsyncContext getAsyncContext();

  public Object getAttribute(String name);

  public Enumeration<String> getAttributeNames();

  public String getAuthType();

  public String getCharacterEncoding();

  public int getContentLength();

  public long getContentLengthLong();

  public String getContentType();

  public String getContextPath();

  public Cookie[] getCookies();

  public long getDateHeader(String name);

  public DispatcherType getDispatcherType();

  public String getHeader(String name);

  public Enumeration<String> getHeaderNames();

  public Enumeration<String> getHeaders(String name);

  public ServletInputStream getInputStream() throws IOException;

  public int getIntHeader(String name);

  public String getLocalAddr();

  public Locale getLocale();

  public Enumeration<Locale> getLocales();

  public String getLocalName();

  public int getLocalPort();

  public String getMethod();

  public String getParameter(String name);

  public Map<String, String[]> getParameterMap();

  public Enumeration<String> getParameterNames();

  public String[] getParameterValues(String name);

  public Part getPart(String name) throws IOException, ServletException;

  public Collection<Part> getParts() throws IOException, ServletException;

  public String getPathInfo();

  public String getPathTranslated();

  public String getProtocol();

  public String getQueryString();

  public BufferedReader getReader() throws IOException;

  public String getRemoteAddr();

  public String getRemoteHost();

  public int getRemotePort();

  public String getRemoteUser();

  public RequestDispatcherIF getRequestDispatcher(String location);

  public String getRequestedSessionId();

  public String getRequestURI();

  public StringBuffer getRequestURL();

  public String getScheme();

  public String getServerName();

  public int getServerPort();

  public ServletContext getServletContext();

  public String getServletPath();

  public HttpSession getSession();

  public HttpSession getSession(boolean create);

  public Principal getUserPrincipal();

  public boolean isAsyncStarted();

  public boolean isAsyncSupported();

  public boolean isRequestedSessionIdFromCookie();

  public boolean isRequestedSessionIdFromURL();

  public boolean isRequestedSessionIdValid();

  public boolean isSecure();

  public boolean isUserInRole(String role);

  public void login(String username, String password) throws ServletException;

  public void logout() throws ServletException;

  public void removeAttribute(String name);

  public void setAttribute(String name, Object o);

  public void setCharacterEncoding(String env) throws UnsupportedEncodingException;

  public boolean isMultipartContent();

  public List<FileItem> getFileItems(JakartaServletFileUpload upload) throws FileUploadException;
}