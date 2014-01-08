/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
 ******************************************************************************/
package com.runwaysdk.controller;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONException;

import com.runwaysdk.ClientException;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.generation.LoaderDecoratorExceptionIF;
import com.runwaysdk.generation.loader.LoaderDecorator;

public class ServletDispatcher extends HttpServlet
{
  private enum ServletMethod {
    GET, POST
  }

  public static final String IS_ASYNCHRONOUS  = Constants.ROOT_PACKAGE + ".isAsynchronous_mojax";

  /**
   * Request key whose value is a JSON object representing an Ajax call with
   * complex objects from a controller being invoked directly.
   */
  public static final String MOJAX_OBJECT     = Constants.ROOT_PACKAGE + ".mojaxObject";

  /**
   * Request key whose value is a JSON Object representing a FormObject via an
   * Ajax call.
   */
  public static final String MOFO_OBJECT      = Constants.ROOT_PACKAGE + ".mofoObject";

  /**
   *
   */
  private static final long  serialVersionUID = -5069320031139205183L;

  private Boolean            isAsynchronous;

  private Boolean            hasFormObject;

  public ServletDispatcher()
  {
    this(false, false);
  }

  public ServletDispatcher(boolean isAsynchronous, boolean hasFormObject)
  {
    this.isAsynchronous = isAsynchronous;
    this.hasFormObject = hasFormObject;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    checkAndDispatch(req, resp, ServletMethod.POST);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    checkAndDispatch(req, resp, ServletMethod.GET);
  }

  /**
   * Loads objects from the parameter mapping and executes the given action. If
   * a parsing failure occurs when the objects are loaded then the proper
   * failure action is invoked.
   * 
   * @param req
   *          HttpServletRequest
   * @param resp
   *          HttpServletResponse
   * @param manager
   *          RequestManager manages the status of the parse
   * @param actionName
   *          The name of the action
   * @param controllerName
   *          The name of the controller
   * @param baseClass
   *          The controller base class
   * @param baseMethod
   *          The controller base method
   * 
   * @throws NoSuchMethodException
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   * @throws InstantiationException
   * @throws InvocationTargetException
   * @throws FileUploadException
   */
  private void dispatch(HttpServletRequest req, HttpServletResponse resp, RequestManager manager, String actionName, String controllerName, Class<?> baseClass, Method baseMethod) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException
  {
    Class<?> controllerClass = LoaderDecorator.load(controllerName);

    Constructor<?> constructor = controllerClass.getConstructor(HttpServletRequest.class, HttpServletResponse.class, Boolean.class);
    Object controller = constructor.newInstance(req, resp, isAsynchronous);

    // Add an asynchronous flag to the request object
    req.setAttribute(IS_ASYNCHRONOUS, new Boolean(isAsynchronous));

    // Get parameter information for the base method annotation
    ActionParameters annotation = baseMethod.getAnnotation(ActionParameters.class);

    // parameter map will be different depending on the call

    Map<String, Parameter> parameters = this.getParameters(req, annotation);

    Object[] objects = this.getObjects(req, manager, annotation, parameters);

    // Ensure an exception has not occured while converting the request
    // parameters to objects. If an exception did occur then invoke the failure
    // case
    if (manager.hasExceptions())
    {
      // FIXME handle case for ajax calls
      dispatchFailure(actionName, baseClass, controllerClass, controller, objects);
    }
    else
    {
      // No problems when converting parameters to objects
      dispatchSuccess(actionName, baseMethod, controllerClass, controller, objects);
    }
  }

  /**
   * @param req
   * @param manager
   * @param annotation
   * @param parameters
   * @return
   */
  private Object[] getObjects(HttpServletRequest req, RequestManager manager, ActionParameters annotation, Map<String, Parameter> parameters)
  {
    // The Ajax FormObject is parsed specially
    if (this.hasFormObject)
    {
      return new Object[] { new MofoParser(manager.getClientRequest(), req).getFormObject() };
    }
    else
    {
      // Use standard parsing
      return new DispatchUtil(annotation, manager, parameters).getObjects();
    }
  }

  /**
   * @param req
   * @param annotation
   * @return
   * @throws FileUploadException
   */
  @SuppressWarnings("unchecked")
  private Map<String, Parameter> getParameters(HttpServletRequest req, ActionParameters annotation)
  {
    String mojaxObject = req.getParameter(MOJAX_OBJECT);

    if (mojaxObject != null)
    {
      try
      {
        return this.getParameterMap(new MojaxObjectParser(annotation, mojaxObject).getMap());
      }
      catch (JSONException e)
      {
        throw new ClientException(e);
      }
    }
    else
    {
      if (ServletFileUpload.isMultipartContent(req))
      {
        Map<String, Parameter> parameters = new HashMap<String, Parameter>();

        // Create a factory for disk-based file items
        FileItemFactory factory = new DiskFileItemFactory();

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        try
        {
          List<FileItem> items = upload.parseRequest(req);

          for (FileItem item : items)
          {
            String fieldName = item.getFieldName();

            if (item.isFormField())
            {
              String fieldValue = item.getString();

              if (!parameters.containsKey(fieldName))
              {
                parameters.put(fieldName, new BasicParameter());
              }

              ( (BasicParameter) parameters.get(fieldName) ).add(fieldValue);
            }
            else if (!item.isFormField() && item.getSize() > 0)
            {
              parameters.put(fieldName, new MultipartFileParameter(item));
            }
          }

          return parameters;
        }
        catch (FileUploadException e)
        {
          // Change the exception type
          throw new RuntimeException(e);
        }
      }
      else
      {
        return this.getParameterMap(req.getParameterMap());
      }
    }
  }

  private Map<String, Parameter> getParameterMap(Map<String, String[]> parameters)
  {
    Map<String, Parameter> map = new HashMap<String, Parameter>();

    for (Entry<String, String[]> entry : parameters.entrySet())
    {
      map.put(entry.getKey(), new BasicParameter(entry.getValue()));
    }

    return map;
  }

  /**
   * Invokes the method of the given action name on the given controller.
   * 
   * @param actionName
   * @param baseMethod
   * @param controllerClass
   * @param controller
   * @param objects
   * @throws NoSuchMethodException
   * @throws SecurityException
   * 
   * @throws NoSuchMethodException
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  private void dispatchSuccess(String actionName, Method baseMethod, Class<?> controllerClass, Object controller, Object[] objects) throws IllegalAccessException, NoSuchMethodException
  {
    try
    {
      Method method = controllerClass.getMethod(actionName, ( (Class[]) baseMethod.getParameterTypes() ));
      method.invoke(controller, objects);
    }
    catch (InvocationTargetException e)
    {
      this.handleInvocationTargetException(e);
    }
  }

  private void handleInvocationTargetException(InvocationTargetException e)
  {
    Throwable target = e.getTargetException();

    if (target instanceof RuntimeException)
    {
      throw (RuntimeException) target;
    }

    throw new RuntimeException(target);
  }

  /**
   * Invokes the failure method of the given action for the given controller.
   * 
   * @param actionName
   * @param baseClass
   * @param controllerClass
   * @param controller
   * @param objects
   * 
   * @throws IllegalAccessException
   * @throws NoSuchMethodException
   */
  private void dispatchFailure(String actionName, Class<?> baseClass, Class<?> controllerClass, Object controller, Object[] objects) throws IllegalAccessException, NoSuchMethodException
  {
    try
    {

      String failureName = "fail" + CommonGenerationUtil.upperFirstCharacter(actionName);
      Method failureBase = RequestScraper.getMethod(failureName, baseClass);

      controllerClass.getMethod(failureName, ( (Class[]) failureBase.getParameterTypes() )).invoke(controller, objects);
    }
    catch (InvocationTargetException e)
    {
      this.handleInvocationTargetException(e);
    }
  }

  /**
   * Checks that the Servlet action can be invoked with the given method. If so
   * then it invokes the method.
   * 
   * @param req
   * @param resp
   * @param servletMethod
   */
  private void checkAndDispatch(HttpServletRequest req, HttpServletResponse resp, ServletMethod servletMethod)
  {
    String servletPath = ServletDispatcher.getServletPath(req);
    servletPath = servletPath.substring(0, servletPath.lastIndexOf("."));
    RequestManager manager = new RequestManager(req);

    try
    {
      int index = servletPath.lastIndexOf(".");
      String actionName = servletPath.substring(index + 1);
      String controllerName = servletPath.substring(0, index).replace("/", "");

      Class<?> baseClass = LoaderDecorator.load(controllerName + "Base");
      Method baseMethod = RequestScraper.getMethod(actionName, baseClass);

      if (baseMethod != null)
      {
        ActionParameters annotation = baseMethod.getAnnotation(ActionParameters.class);

        // POST methods cannot be invoked through GETS
        if (annotation.post() && servletMethod.equals(ServletMethod.GET))
        {
          String msg = "The uri [" + servletPath + "] can only be accessed by a post method";
          throw new IllegalURIMethodException(msg, req.getLocale(), servletPath);
        }
        else
        {
          dispatch(req, resp, manager, actionName, controllerName, baseClass, baseMethod);
        }
      }
      else
      {
        String msg = "A servlet at the uri [" + servletPath + "] does not exist.";
        throw new UnknownServletException(msg, req.getLocale(), servletPath);
      }
    }
    catch (RuntimeException ex)
    {
      if (ex instanceof LoaderDecoratorExceptionIF)
      {
        String msg = "A servlet at the uri [" + servletPath + "] does not exist.";
        throw new UnknownServletException(msg, req.getLocale(), servletPath);
      }
      else
      {
        throw ex;
      }
    }
    catch (NoSuchMethodException e)
    {
      String msg = "A servlet at the uri [" + servletPath + "] does not exist.";
      throw new UnknownServletException(msg, req.getLocale(), servletPath);
    }
    catch (InstantiationException e)
    {
      String msg = "A servlet at the uri [" + servletPath + "] does not exist.";
      throw new UnknownServletException(msg, req.getLocale(), servletPath);
    }
    catch (IllegalAccessException e)
    {
      String msg = "A servlet at the uri [" + servletPath + "] does not exist.";
      throw new UnknownServletException(msg, req.getLocale(), servletPath);
    }
    catch (InvocationTargetException e)
    {
      this.handleInvocationTargetException(e);
    }
  }

  private static final String getServletPath(HttpServletRequest request)
  {
    String servletPath = request.getServletPath();

    if (!"".equals(servletPath))
    {
      return servletPath;
    }

    String requestUri = request.getRequestURI();
    int startIndex = request.getContextPath().equals("") ? 0 : request.getContextPath().length();
    int endIndex = request.getPathInfo() == null ? requestUri.length() : requestUri.indexOf(request.getPathInfo());

    return requestUri.substring(startIndex, endIndex);
  }
}
