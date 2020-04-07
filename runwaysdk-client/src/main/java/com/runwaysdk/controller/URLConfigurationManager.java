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
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.RunwayConfigurationException;
import com.runwaysdk.controller.URLConfigurationManager.ControllerMapping.ActionMapping;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.mvc.Controller;
import com.runwaysdk.mvc.Endpoint;
import com.runwaysdk.mvc.ResponseIF;
import com.runwaysdk.request.ServletRequestIF;
import com.runwaysdk.request.ServletResponseIF;

/**
 * This class reads the url config file searching for controller definitions and
 * method->url mappings. Each method on the controller can map to many url regex
 * strings. The method name is assumed to also be regex, in which case it will
 * loop over all methods matching the regex and create a new action mapping from
 * method->url. The actions are queried in top-down fashion, meaning that
 * actions defined first in the xml will be the first to match a url.
 * 
 * This class also allows the definition of url forwards and redirects. Forwards
 * happen within the same request, the request is simply forwarded to another
 * url and no filters are applied. Redirects actually redirect the user; the
 * client sees the new url and filters are applied.
 */
public class URLConfigurationManager
{
  // Always use the SLF4J logger.
  private static Logger                log            = LoggerFactory.getLogger(URLConfigurationManager.class);

  private static Object                initializeLock = new Object();

  private static ArrayList<UriMapping> mappings;

  private String                       fileName;

  public URLConfigurationManager()
  {
    this("urlmap.xml");
  }

  public URLConfigurationManager(String fileName)
  {
    this.fileName = fileName;

    String exMsg = "An exception occurred while reading the xml servlet request mappings.";

    try
    {
      // The method returns immediately if the configuration file does not
      // exist.
      readMappings();
    }
    catch (ParserConfigurationException e)
    {
      throw new RunwayConfigurationException(exMsg, e);
    }
    catch (SAXException e)
    {
      throw new RunwayConfigurationException(exMsg, e);
    }
    catch (IOException e)
    {
      throw new RunwayConfigurationException(exMsg, e);
    }
  }

  /**
   * Returns the UriMapping associated with the given uri. This URI is assumed
   * to NOT include the application context path (if there is one).
   * 
   * @param uri
   * @return
   */
  public UriMapping getMapping(String uri)
  {

    if (mappings == null)
    {
      return null;
    }

    if (uri.startsWith("/"))
    {
      uri = uri.replaceFirst("/", "");
    }

    // If this is a ControllerMapping, calculate what action it would be
    // referencing.
    ArrayList<String> uris = new ArrayList<String>(Arrays.asList(uri.split("/")));
    String actionUri = "";
    if (uris.size() > 1)
    {
      uris.remove(0);
      actionUri = StringUtils.join(uris, "/");
    }

    for (UriMapping mapping : mappings)
    {
      if (mapping.handlesUri(uri))
      {
        if (mapping instanceof ControllerMapping)
        {
          ActionMapping action = ( (ControllerMapping) mapping ).getActionAtUri(actionUri);
          if (action != null)
          {
            return action;
          }
        }
        else
        {
          return mapping;
        }
      }
    }
 
    return null;
  }

  private void readMappings() throws ParserConfigurationException, SAXException, IOException
  {
    if (!ConfigurationManager.checkExistence(ConfigGroup.CLIENT, fileName))
    {
      return;
    }

    InputStream stream = ConfigurationManager.getResourceAsStream(ConfigGroup.CLIENT, fileName);

    readMappings(stream);
  }

  public void readMappings(InputStream stream) throws ParserConfigurationException, SAXException, IOException
  {
    synchronized (initializeLock)
    {
      if (mappings != null)
      {
        return;
      }

      mappings = new ArrayList<UriMapping>();
    }

    if (stream != null)
    {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(stream);

      Element xmlMappings = doc.getDocumentElement();

      NodeList children = xmlMappings.getChildNodes();
      for (int i = 0; i < children.getLength(); ++i)
      {
        Node n = children.item(i);

        if (n.getNodeType() == Node.ELEMENT_NODE)
        {
          Element el = (Element) n;

          String tagName = el.getTagName();

          if (tagName.equals(URLConfigurationConstant.MAP_TAG))
          {
            this.readMapping(el);
          }
          else if (tagName.equals(URLConfigurationConstant.PACKAGE_TAG))
          {
            this.readPackage(el);
          }
        }
      }

//      String info = "URL mappings successfully read from " + fileName + ":\n" + this.toString();
//      log.info(info);
    }
  }

  private void readPackage(Element element)
  {
    String name = element.getAttribute(URLConfigurationConstant.NAME_ATTRIBUTE);

    Reflections reflections = new Reflections(name);

    Set<Class<?>> types = reflections.getTypesAnnotatedWith(Controller.class);

    for (Class<?> type : types)
    {
      Controller[] urlMapping = type.getAnnotationsByType(Controller.class);

      String uri = urlMapping[0].url();

      ControllerMapping mapping = new ControllerMapping(uri, type.getName(), ControllerVersion.V2);

      Method[] methods = type.getMethods();

      for (Method method : methods)
      {
        if (Modifier.isPublic(method.getModifiers()))
        {
          Class<?> returnType = method.getReturnType();

          if (ResponseIF.class.isAssignableFrom(returnType))
          {
            Endpoint urlAction = method.getAnnotation(Endpoint.class);

            if (urlAction != null && !urlAction.url().equals("[unassigned]"))
            {
              String url = urlAction.url();

              mapping.add(method.getName(), url, ControllerVersion.V2);
            }
            else
            {
              mapping.add(method.getName(), method.getName(), ControllerVersion.V2);
            }
          }
        }
      }

      mappings.add(mapping);
    }
  }

  private void readMapping(Element element)
  {
    String uri = element.getAttribute(URLConfigurationConstant.URI_ATTRIBUTE);
    String controllerClassName = element.getAttribute(URLConfigurationConstant.CONTROLLER_ATTRIBUTE);
    String redirect = element.getAttribute(URLConfigurationConstant.REDIRECT_ATTRIBUTE);
    String forward = element.getAttribute(URLConfigurationConstant.FORWARD_ATTRIBUTE);

    if (uri == null)
    {
      String exMsg = "Request mapping requires a uri.";
      throw new RunwayConfigurationException(exMsg);
    }

    if (controllerClassName != null && controllerClassName != "")
    {
      readController(uri, controllerClassName, element);
    }
    else if (forward != null && forward != "")
    {
      mappings.add(new UriForwardMapping(uri, forward, ControllerVersion.V1));
    }
    else if (redirect != null && redirect != "")
    {
      mappings.add(new UriRedirectMapping(uri, redirect, ControllerVersion.V1));
    }
    else
    {
      String exMsg = "Request mapping requires either a controller class name or a redirect url.";
      throw new RunwayConfigurationException(exMsg);
    }
  }

  private void readController(String uri, String controllerClassName, Element el)
  {
    ControllerMapping controllerMapping = new ControllerMapping(uri, controllerClassName, ControllerVersion.V1);

    ArrayList<Method> methods = null;
    try
    {
      Class<?> clazz = LoaderDecorator.load(controllerClassName);
      Class<?> clazzBase = LoaderDecorator.load(controllerClassName + "Base");
      methods = new ArrayList<Method>(Arrays.asList(clazz.getMethods()));
      Iterator<Method> it = methods.iterator();
      while (it.hasNext())
      {
        Method m = it.next();
        if (!m.getDeclaringClass().equals(clazz) && !m.getDeclaringClass().equals(clazzBase))
        {
          it.remove();
        }
      }
    }
    catch (Throwable t)
    {
      String exMsg = "Exception loading controller class [" + controllerClassName + "].";
      throw new RunwayConfigurationException(exMsg, t);
    }

    NodeList actions = el.getChildNodes();
    for (int iAction = 0; iAction < actions.getLength(); ++iAction)
    {
      Node nodeAct = actions.item(iAction);

      if (nodeAct.getNodeType() == Node.ELEMENT_NODE)
      {
        Element elAction = (Element) nodeAct;

        String method = elAction.getAttribute("method");
        String actionUrl = elAction.getAttribute("uri");

        boolean didMatch = false;
        for (Method m : methods)
        {
          if (m.getName().matches(method))
          {
            controllerMapping.add(m.getName(), actionUrl, ControllerVersion.V1);
            didMatch = true;
          }
        }

        if (!didMatch)
        {
          String exMsg = "The method regex [" + method + "] for action [" + actionUrl + "] did not match any methods on the controller class definition [" + controllerClassName + "].";
          throw new RunwayConfigurationException(exMsg);
        }
      }
    }

    mappings.add(controllerMapping);
  }

  @Override
  public String toString()
  {
    String out = "";

    for (UriMapping mapping : mappings)
    {
      out = out + mapping.toString() + "\n";
    }

    return out;
  }

  /**
   * ABSTRACT URI MAPPING
   */
  public abstract class UriMapping
  {
    private String            uri;

    private ControllerVersion version;

    public UriMapping(ControllerVersion version)
    {
      this(null, version);
    }

    public UriMapping(String uri, ControllerVersion version)
    {
      this.uri = uri;
      this.version = version;
    }

    public ControllerVersion getVersion()
    {
      return version;
    }

    public void setVersion(ControllerVersion version)
    {
      this.version = version;
    }

    public String getUri()
    {
      return this.uri;
    }

    public void setUri(String uri)
    {
      this.uri = uri;
    }

    public boolean handlesUri(String uri)
    {
      if (uri.matches(this.getUri()))
      {
        return true;
      }

      return false;
    }

    public abstract void performRequest(DispatcherIF dispatcher, RequestManager manager) throws IOException;
  }

  /**
   * URI FORWARD MAPPING
   */
  public class UriForwardMapping extends UriMapping
  {
    private String uriEnd;

    public UriForwardMapping(String uriStart, String uriEnd, ControllerVersion version)
    {
      super(uriStart, version);

      this.uriEnd = uriEnd;
    }

    /**
     * @return the uriEnd
     */
    public String getUriEnd()
    {
      return uriEnd;
    }

    @Override
    public void performRequest(DispatcherIF dispatcher, RequestManager manager)
    {
      try
      {
        ServletRequestIF req = manager.getReq();
        ServletResponseIF resp = manager.getResp();

        req.getRequestDispatcher(this.getUriEnd()).forward(req, resp);
      }
      catch (ServletException e)
      {
        throw new RuntimeException(e);
      }
      catch (IOException e)
      {
        throw new RuntimeException(e);
      }
    }

    @Override
    public String toString()
    {
      return this.toString("");
    }

    public String toString(String indent)
    {
      return indent + "UriForwardMapping: \"" + this.getUri() + "\" = \"" + this.getUriEnd() + "\"";
    }
  }

  /**
   * URI REDIRECT MAPPING
   */
  public class UriRedirectMapping extends UriForwardMapping
  {
    public UriRedirectMapping(String uriStart, String uriEnd, ControllerVersion version)
    {
      super(uriStart, uriEnd, version);
    }

    @Override
    public void performRequest(DispatcherIF dispatcher, RequestManager manager)
    {
      try
      {
        ServletRequestIF req = manager.getReq();
        ServletResponseIF resp = manager.getResp();

        resp.sendRedirect(req.getContextPath() + "/" + this.getUriEnd());
      }
      catch (IOException e)
      {
        throw new RuntimeException(e);
      }
    }

    public String toString(String indent)
    {
      return indent + "UriRedirectMapping: \"" + this.getUri() + "\" = \"" + this.getUriEnd() + "\"";
    }
  }

  /**
   * CONTROLLER MAPPING
   */
  public class ControllerMapping extends UriMapping
  {
    private String                   className;

    private ArrayList<ActionMapping> actions;

    public ControllerMapping(String controllerUri, String className, ControllerVersion version)
    {
      super(controllerUri, version);

      this.className = className;
      actions = new ArrayList<ActionMapping>();
    }

    public String getControllerClassName()
    {
      return this.className;
    }

    @Override
    public boolean handlesUri(String uri)
    {
      String[] contPath = uri.split("/");
      return super.handlesUri(contPath[0]);
    }

    public ActionMapping getActionAtUri(String uri)
    {
      for (int i = actions.size() - 1; i >= 0; --i)
      {
        ActionMapping action = actions.get(i);
        if (action.handlesUri(uri))
        {
          return action;
        }
      }

      return null;
    }

    public void add(String actionName, String url, ControllerVersion version)
    {
      Iterator<ActionMapping> it = actions.iterator();
      while (it.hasNext())
      {
        ActionMapping action = it.next();

        if (action.getMethodName().equals(actionName))
        {
          String endpoint = this.getUri() + "/" + actionName;

          throw new DuplicateEndpointException(endpoint, "The endpoint [" + endpoint + "] cannot be registered multiple times");
        }
      }

      actions.add(new ActionMapping(this, actionName, url, version));
    }

    public String toString(String indent)
    {
      String innerIndent = indent;
      if (indent == null)
      {
        indent = "";
        innerIndent = "  ";
      }

      String out = indent + this.getControllerClassName() + " : \"" + this.getUri() + "\" = [\n";

      for (int i = actions.size() - 1; i >= 0; --i)
      {
        ActionMapping action = actions.get(i);
        out = out + indent + action.toString(innerIndent) + "\n";
      }

      out = out + indent + "]";

      return out;
    }

    @Override
    public String toString()
    {
      return this.toString(null);
    }

    /**
     * ACTION MAPPING
     */
    public class ActionMapping extends UriMapping
    {
      String            actionName;

      ControllerMapping controller;

      public ActionMapping(ControllerMapping controller, String actionName, String uri, ControllerVersion version)
      {
        super(version);

        this.actionName = actionName;
        this.controller = controller;

        // The Uri must be set after the actionName is set since we do property
        // replacement in it.
        this.setUri(uri);
      }

      @Override
      public void setUri(String uri)
      {
        String replaced = uri.replace("${method}", actionName);
        super.setUri(replaced);
      }

      public void setMethodName(String val)
      {
        this.actionName = val;
      }

      public String getMethodName()
      {
        return actionName;
      }

      public ControllerMapping getControllerMapping()
      {
        return this.controller;
      }

      @Override
      public String toString()
      {
        return this.toString("");
      }

      public String toString(String indent)
      {
        return indent + "ActionMapping: " + actionName + " = \"" + this.getUri() + "\"";
      }

      @Override
      public void performRequest(DispatcherIF dispatcher, RequestManager manager) throws IOException
      {
        String actionName = getMethodName();
        String controllerName = getControllerMapping().getControllerClassName();

        dispatcher.invokeControllerAction(controllerName, actionName, manager);
      }
    }

    @Override
    public void performRequest(DispatcherIF dispatcher, RequestManager manager)
    {
      throw new UnsupportedOperationException("You can't perform a request on a ControllerMapping, only an ActionMapping.");
    }
  }
}
