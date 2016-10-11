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
package com.runwaysdk.mvc;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
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
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.ClientException;
import com.runwaysdk.ProblemExceptionDTO;
import com.runwaysdk.business.ProblemDTO;
import com.runwaysdk.controller.BasicParameter;
import com.runwaysdk.controller.DispatchUtil;
import com.runwaysdk.controller.DispatcherIF;
import com.runwaysdk.controller.ErrorUtility;
import com.runwaysdk.controller.IllegalURIMethodException;
import com.runwaysdk.controller.MultipartFileParameter;
import com.runwaysdk.controller.ParameterIF;
import com.runwaysdk.controller.ParameterValue;
import com.runwaysdk.controller.RequestManager;
import com.runwaysdk.controller.RequestScraper;
import com.runwaysdk.controller.ServletMethod;
import com.runwaysdk.controller.URLConfigurationManager;
import com.runwaysdk.controller.URLConfigurationManager.UriMapping;
import com.runwaysdk.controller.UnknownServletException;
import com.runwaysdk.generation.LoaderDecoratorExceptionIF;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.request.ServletRequestIF;
import com.runwaysdk.transport.conversion.json.RunwayExceptionDTOToJSON;
import com.runwaysdk.web.ServletUtility;

public class DispatcherServlet extends HttpServlet implements DispatcherIF
{
  /**
   * 
   */
  private static final long       serialVersionUID = 2153995526229356351L;

  // Always use the SLF4J logger.
  private static Logger           log              = LoggerFactory.getLogger(URLConfigurationManager.class);

  private URLConfigurationManager xmlMapper;

  public DispatcherServlet()
  {
    this(new URLConfigurationManager());
  }

  public DispatcherServlet(URLConfigurationManager manager)
  {
    this.xmlMapper = manager;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    this.checkAndDispatch(new RequestManager(req, resp, ServletMethod.POST));
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    this.checkAndDispatch(new RequestManager(req, resp, ServletMethod.GET));
  }

  /**
   * Checks that the Servlet action can be invoked with the given method. If so
   * then it invokes the method.
   * 
   * @param req
   * @param resp
   * @param servletMethod
   * 
   * @throws IOException
   */
  public void checkAndDispatch(RequestManager manager) throws IOException
  {
    String servletPath = ServletUtility.getServletPath(manager.getReq());

    UriMapping uriMapping = xmlMapper.getMapping(servletPath);

    if (uriMapping != null)
    {
      uriMapping.performRequest(this, manager);
    }
    else
    {
      String msg = "An endpoint at the uri [" + servletPath + "] does not exist.";

      throw new UnknownServletException(msg, manager.getLocale(), servletPath);
    }
  }

  @Override
  public void invokeControllerAction(String controllerName, String actionName, RequestManager manager) throws IOException
  {
    String servletPath = ServletUtility.getServletPath(manager.getReq());

    try
    {
      Class<?> clazz = LoaderDecorator.load(controllerName);
      Method method = RequestScraper.getMethod(actionName, clazz);

      if (method != null)
      {
        Endpoint annotation = method.getAnnotation(Endpoint.class);

        if (annotation != null)
        {
          if (annotation.method().equals(ServletMethod.POST) && manager.getMethod().equals(ServletMethod.GET))
          {
            String msg = "The uri [" + servletPath + "] can only be accessed by a post method";
            throw new IllegalURIMethodException(msg, manager.getLocale(), servletPath);
          }
          else
          {
            dispatch(manager, clazz, method);
          }
        }
        else if (manager.getMethod().equals(ServletMethod.GET))
        {
          // By default assume that the method only allows GET requests
          dispatch(manager, clazz, method);
        }
        else
        {
          String msg = "The uri [" + servletPath + "] can only be accessed by a post method";
          throw new IllegalURIMethodException(msg, manager.getLocale(), servletPath);
        }
      }
      else
      {
        String msg = "An endpoint at the uri [" + servletPath + "] does not exist.";
        throw new UnknownServletException(msg, manager.getLocale(), servletPath);
      }
    }
    catch (RuntimeException ex)
    {
      if (ex instanceof LoaderDecoratorExceptionIF)
      {
        String msg = "An endpoint at the uri [" + servletPath + "] does not exist.";
        throw new UnknownServletException(msg, manager.getLocale(), servletPath);
      }
      else
      {
        throw ex;
      }
    }
    catch (NoSuchMethodException e)
    {
      String msg = "An endpoint at the uri [" + servletPath + "] does not exist.";
      throw new UnknownServletException(msg, manager.getLocale(), servletPath);
    }
    catch (InvocationTargetException e)
    {
      this.handleInvocationTargetException(manager, e);
    }
    catch (Throwable e)
    {
      throw new ClientException(e);
    }
  }

  /**
   * Loads objects from the parameter mapping and executes the given action. If
   * a parsing failure occurs when the objects are loaded then the proper
   * failure action is invoked.
   * 
   * @param manager
   *          RequestManager manages the status of the parse
   * @param clazz
   *          The controller class
   * @param method
   *          The controller method
   * @throws Throwable
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
  private void dispatch(RequestManager manager, Class<?> clazz, Method method) throws Throwable
  {
    Constructor<?> constructor = clazz.getConstructor();
    Object controller = constructor.newInstance();

    Map<String, ParameterValue> parameters = this.getParameters(manager);

    Object[] objects = this.getObjects(manager, method, parameters);

    try
    {
      // Ensure an exception has not occurred while converting the request
      // parameters to objects. If an exception did occur then invoke the
      // failure
      // case
      List<ProblemDTO> problems = manager.getProblems();

      if (problems.size() > 0)
      {
        throw new ProblemExceptionDTO("", problems);
      }

      ResponseIF response = (ResponseIF) method.invoke(controller, objects);
      response.handle(manager);
    }
    catch (Throwable e)
    {
      Endpoint annotation = method.getAnnotation(Endpoint.class);

      if (annotation.error().equals(ErrorSerialization.JSON))
      {
        log.error("JSON handling of error", e);
        
        if (e instanceof InvocationTargetException)
        {
          e = ( (InvocationTargetException) e ).getTargetException();
        }

        RunwayExceptionDTOToJSON converter = new RunwayExceptionDTOToJSON(e.getClass().getName(), e.getMessage(), e.getLocalizedMessage());
        JSONObject json = converter.populate();

        ErrorRestResponse response = new ErrorRestResponse(json.toString());
        response.handle(manager);
      }
      else
      {
        throw e;
      }
    }
  }

  /**
   * @param manager
   * @param method
   * @param parameterMap
   * @return
   */
  private Object[] getObjects(RequestManager manager, Method method, Map<String, ParameterValue> parameterMap)
  {
    // Use standard parsing
    Parameter[] parameters = method.getParameters();

    List<ParameterIF> parameterIfs = new LinkedList<ParameterIF>();

    for (Parameter parameter : parameters)
    {
      parameterIfs.add(new ParameterDecorator(parameter));
    }

    return new DispatchUtil(parameterIfs, manager, parameterMap).getObjects();
  }

  /**
   * @param req
   * @return
   * @throws FileUploadException
   */
  private Map<String, ParameterValue> getParameters(RequestManager manager)
  {
    ServletRequestIF request = manager.getReq();

    if (request.isMultipartContent())
    {
      Map<String, ParameterValue> parameters = new HashMap<String, ParameterValue>();

      // Create a factory for disk-based file items
      FileItemFactory factory = new DiskFileItemFactory();

      // Create a new file upload handler
      ServletFileUpload upload = new ServletFileUpload(factory);

      try
      {
        List<FileItem> items = request.getFileItems(upload);

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
      String contentType = request.getContentType();

      if (contentType != null && contentType.contains("application/json"))
      {
        return this.getParamaterMap(request);
      }
      else
      {
        return this.getParameterMap(request.getParameterMap());
      }
    }
  }

  @SuppressWarnings("unchecked")
  public Map<String, ParameterValue> getParamaterMap(ServletRequestIF request)
  {
    try
    {
      String body = IOUtils.toString(request.getReader());

      JSONObject object = new JSONObject(body);

      Map<String, ParameterValue> values = new HashMap<String, ParameterValue>();

      Iterator<String> keys = object.keys();

      while (keys.hasNext())
      {
        String key = keys.next();
        String value = object.get(key).toString();

        values.put(key, new BasicParameter(new String[] { value }));
      }

      return values;
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
    catch (JSONException e)
    {
      throw new RuntimeException(e);
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

  private void handleInvocationTargetException(RequestManager manager, InvocationTargetException e) throws IOException
  {
    Throwable target = e.getTargetException();

    ErrorUtility.prepareThrowable(target, manager.getReq(), manager.getResp(), false, true);
  }
}
