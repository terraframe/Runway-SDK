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
package com.runwaysdk.controller;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;

import com.runwaysdk.AttributeNotificationDTO;
import com.runwaysdk.ClientException;
import com.runwaysdk.ProblemExceptionDTO;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.controller.URLConfigurationManager.UriMapping;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.generation.LoaderDecoratorExceptionIF;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.request.RequestDecorator;
import com.runwaysdk.request.ResponseDecorator;
import com.runwaysdk.request.ServletRequestIF;
import com.runwaysdk.request.ServletResponseIF;

public class ServletDispatcher extends HttpServlet implements DispatcherIF
{
  public static final String      IS_ASYNCHRONOUS  = Constants.ROOT_PACKAGE + ".isAsynchronous_mojax";

  /**
   * Request key whose value is a JSON object representing an Ajax call with
   * complex objects from a controller being invoked directly.
   */
  public static final String      MOJAX_OBJECT     = Constants.ROOT_PACKAGE + ".mojaxObject";

  /**
   * Request key whose value is a JSON Object representing a FormObject via an
   * Ajax call.
   */
  public static final String      MOFO_OBJECT      = Constants.ROOT_PACKAGE + ".mofoObject";

  /**
   *
   */
  private static final long       serialVersionUID = -5069320031139205183L;

  private Boolean                 isAsynchronous;

  private Boolean                 hasFormObject;

  private URLConfigurationManager xmlMapper;

  public ServletDispatcher()
  {
    this(false, false);
  }

  public ServletDispatcher(URLConfigurationManager manager)
  {
    this.isAsynchronous = false;
    this.hasFormObject = false;
    this.xmlMapper = manager;
  }

  public ServletDispatcher(boolean isAsynchronous, boolean hasFormObject)
  {
    this.isAsynchronous = isAsynchronous;
    this.hasFormObject = hasFormObject;
    this.xmlMapper = new URLConfigurationManager();
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    checkAndDispatch(new RequestManager(req, resp, ServletMethod.POST));
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    checkAndDispatch(new RequestManager(req, resp, ServletMethod.GET));
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
   * @throws IOException
   * @throws FileUploadException
   */
  private void dispatch(RequestManager manager, String actionName, String controllerName, Class<?> baseClass, Method baseMethod) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, IOException
  {
    Class<?> controllerClass = LoaderDecorator.load(controllerName);
    Constructor<?> constructor = controllerClass.getConstructor(HttpServletRequest.class, HttpServletResponse.class, Boolean.class);

    HttpServletRequest req = ( (RequestDecorator) manager.getReq() ).getRequest();
    HttpServletResponse resp = ( (ResponseDecorator) manager.getResp() ).getResponse();

    Object controller = constructor.newInstance(req, resp, isAsynchronous);

    // Add an asynchronous flag to the request object
    req.setAttribute(IS_ASYNCHRONOUS, new Boolean(isAsynchronous));

    // Get parameter information for the base method annotation
    ActionParameters annotation = baseMethod.getAnnotation(ActionParameters.class);

    // parameter map will be different depending on the call

    Map<String, ParameterValue> parameters = this.getParameters(req, annotation);

    Object[] objects = this.getObjects(req, manager, annotation, parameters);

    // Ensure an exception has not occured while converting the request
    // parameters to objects. If an exception did occur then invoke the failure
    // case
    try
    {
      if (manager.hasExceptions())
      {
        dispatchFailure(actionName, baseClass, controllerClass, controller, objects);
      }
      else
      {
        // No problems when converting parameters to objects
        dispatchSuccess(actionName, baseMethod, controllerClass, controller, objects);
      }
    }
    catch (InvocationTargetException e)
    {
      if (e.getCause() instanceof UndefinedControllerActionException)
      {
        RuntimeException ex;

        if (manager.getProblems().size() > 0)
        {
          ex = new ProblemExceptionDTO("", manager.getProblems());
        }
        else
        {
          List<AttributeNotificationDTO> attrNots = manager.getAttributeNotifications();
          List<String> msgs = new ArrayList<String>();
          for (int i = 0; i < attrNots.size(); ++i)
          {
            msgs.add(attrNots.get(i).getMessage());
          }

          ex = new RuntimeException(StringUtils.join(msgs, ", "));
        }

        ErrorUtility.prepareThrowable(ex, manager.getReq(), manager.getResp(), this.isAsynchronous, true);
      }

      this.handleInvocationTargetException(manager.getReq(), manager.getResp(), e);
    }
  }

  /**
   * @param req
   * @param manager
   * @param annotation
   * @param parameters
   * @return
   */
  private Object[] getObjects(HttpServletRequest req, RequestManager manager, ActionParameters annotation, Map<String, ParameterValue> parameters)
  {
    // The Ajax FormObject is parsed specially
    if (this.hasFormObject)
    {
      Object[] objects = new MofoParser(manager, annotation, req).getObjects();

      return objects;
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
  private Map<String, ParameterValue> getParameters(HttpServletRequest req, ActionParameters annotation)
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
        Map<String, ParameterValue> parameters = new HashMap<String, ParameterValue>();

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

  private Map<String, ParameterValue> getParameterMap(Map<String, String[]> parameters)
  {
    Map<String, ParameterValue> map = new HashMap<String, ParameterValue>();

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
   * @throws IOException
   * @throws InvocationTargetException
   */
  private void dispatchSuccess(String actionName, Method baseMethod, Class<?> controllerClass, Object controller, Object[] objects) throws IllegalAccessException, NoSuchMethodException, IOException, IllegalArgumentException, InvocationTargetException
  {
    Method method = controllerClass.getMethod(actionName, ( (Class[]) baseMethod.getParameterTypes() ));
    method.invoke(controller, objects);
  }

  private void handleInvocationTargetException(ServletRequestIF req, ServletResponseIF resp, InvocationTargetException e) throws IOException
  {
    Throwable target = e.getTargetException();

    ErrorUtility.prepareThrowable(target, req, resp, this.isAsynchronous, true);
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
   * @throws IOException
   * @throws SecurityException
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   */
  private void dispatchFailure(String actionName, Class<?> baseClass, Class<?> controllerClass, Object controller, Object[] objects) throws IllegalAccessException, NoSuchMethodException, IOException, IllegalArgumentException, InvocationTargetException, SecurityException
  {
    String failureName = "fail" + CommonGenerationUtil.upperFirstCharacter(actionName);
    Method failureBase = RequestScraper.getMethod(failureName, baseClass);

    controllerClass.getMethod(failureName, ( (Class[]) failureBase.getParameterTypes() )).invoke(controller, objects);
  }

  /**
   * Checks that the Servlet action can be invoked with the given method. If so
   * then it invokes the method.
   * 
   * @param req
   * @param resp
   * @param servletMethod
   * @throws IOException
   */
  public void checkAndDispatch(RequestManager manager) throws IOException
  {
    String servletPath = ServletDispatcher.getServletPath(manager.getReq());

    UriMapping uriMapping = xmlMapper.getMapping(servletPath);
    if (uriMapping != null)
    {
      uriMapping.performRequest(this, manager);
    }
    else
    {
      // Else expect that the controller classname followed by the action name
      // and then a prefix (like mojo) is in the url.
      int lastIndexOf = servletPath.lastIndexOf(".");

      if (lastIndexOf != -1)
      {
        servletPath = servletPath.substring(0, lastIndexOf);

        int index = servletPath.lastIndexOf(".");
        String actionName = servletPath.substring(index + 1);
        String controllerName = servletPath.substring(0, index).replace("/", "");

        invokeControllerAction(controllerName, actionName, manager);
      }
      else
      {
        String msg = "An endpoint at the uri [" + servletPath + "] does not exist.";

        throw new UnknownServletException(msg, manager.getLocale(), servletPath);
      }
    }
  }

  @Override
  public void invokeControllerAction(String controllerName, String actionName, RequestManager manager) throws IOException
  {
    String servletPath = ServletDispatcher.getServletPath(manager.getReq());

    try
    {
      Class<?> baseClass = LoaderDecorator.load(controllerName + "Base");
      Method baseMethod = RequestScraper.getMethod(actionName, baseClass);

      if (baseMethod != null)
      {
        ActionParameters annotation = baseMethod.getAnnotation(ActionParameters.class);

        // POST methods cannot be invoked through GETS
        if (annotation.post() && manager.getMethod().equals(ServletMethod.GET))
        {
          String msg = "The uri [" + servletPath + "] can only be accessed by a post method";
          throw new IllegalURIMethodException(msg, manager.getLocale(), servletPath);
        }
        else
        {
          dispatch(manager, actionName, controllerName, baseClass, baseMethod);
        }
      }
      else
      {
        String msg = "A servlet at the uri [" + servletPath + "] does not exist.";
        throw new UnknownServletException(msg, manager.getLocale(), servletPath);
      }
    }
    catch (RuntimeException ex)
    {
      if (ex instanceof LoaderDecoratorExceptionIF)
      {
        String msg = "A servlet at the uri [" + servletPath + "] does not exist.";
        throw new UnknownServletException(msg, manager.getLocale(), servletPath);
      }
      else
      {
        throw ex;
      }
    }
    catch (NoSuchMethodException e)
    {
      String msg = "A servlet at the uri [" + servletPath + "] does not exist.";
      throw new UnknownServletException(msg, manager.getLocale(), servletPath);
    }
    catch (InstantiationException e)
    {
      String msg = "A servlet at the uri [" + servletPath + "] does not exist.";
      throw new UnknownServletException(msg, manager.getLocale(), servletPath);
    }
    catch (IllegalAccessException e)
    {
      String msg = "A servlet at the uri [" + servletPath + "] does not exist.";
      throw new UnknownServletException(msg, manager.getLocale(), servletPath);
    }
    catch (InvocationTargetException e)
    {
      this.handleInvocationTargetException(manager.getReq(), manager.getResp(), e);
    }
  }

  /**
   * This method strips the context path from the request URI and returns it.
   * Use this method to handle URI's in a context path agnostic manner.
   * 
   * @param request
   * @return
   */
  private static final String getServletPath(ServletRequestIF request)
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

  public boolean hasXmlMapping(HttpServletRequest req, HttpServletResponse resp)
  {
    return xmlMapper.getMapping(ServletDispatcher.getServletPath(new RequestDecorator(req))) != null;
  }
}
