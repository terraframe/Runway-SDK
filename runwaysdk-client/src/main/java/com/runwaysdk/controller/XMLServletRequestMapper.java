/**
 * *****************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 *****************************************************************************
 */

package com.runwaysdk.controller;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.RunwayConfigurationException;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.controller.XMLServletRequestMapper.ControllerMapping.ActionMapping;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.web.NoExtensionDispatchFilter;

/**
 * This class reads fileName searching for controller definitions and method->url mappings. Each method on the controller can map to many url regex strings.
 * The method name is assumed to also be regex, in which case it will loop over all methods matching the regex and create a new action mapping from method->url.
 * The actions are queried in top-down fashion, meaning that actions defined first in the xml will be the first to match a url.
 */
public class XMLServletRequestMapper
{
  // Always use the SLF4J logger.
  private static Logger log = LoggerFactory.getLogger(NoExtensionDispatchFilter.class);
  
  String fileName = "urlmap.xml";
  
  
  private static HashMap<String, ControllerMapping> map;
  
  private static Object lock = new Object();
  
  
  public XMLServletRequestMapper() {
    String exMsg = "An exception occurred while reading the xml servlet request mappings.";
    
    try {
      // The method returns immediately if the configuration file does not exist.
      readMappings();
    }
    catch (ParserConfigurationException e) {
      throw new RunwayConfigurationException(exMsg, e);
    }
    catch (SAXException e) {
      throw new RunwayConfigurationException(exMsg, e);
    }
    catch (IOException e) {
      throw new RunwayConfigurationException(exMsg, e);
    }
  }
  
  public ActionMapping getMapping(String servletPath, HttpServletRequest req) {
    
    if (map == null) { return null; }
    
    if (servletPath.startsWith("/")) {
      servletPath = servletPath.replaceFirst("/", "");
    }
    
    String controllerURL;
    String controllerAction;
    
    String pathInfo = req.getPathInfo();
    if (pathInfo != null) {
      if (pathInfo.startsWith("/")) {
        pathInfo = pathInfo.replaceFirst("/", "");
      }
      
      String[] actions = pathInfo.split("/");
      
      if (actions == null) {
        return null;
      }
      
      controllerURL = servletPath;
      controllerAction = actions[0];
    }
    else {
      String[] controllerAndAction = servletPath.split("/");
      
      controllerURL = controllerAndAction[0];
      controllerAction = "";
      for (int i = 1; i < controllerAndAction.length; ++i) {
        controllerAction = controllerAction + controllerAndAction[i];
        
        if (i != controllerAndAction.length-1) {
          controllerAction += "/";
        }
      }
    }
    
    ControllerMapping controller = map.get(controllerURL);
    
    if (controller != null) {
      return controller.getActionAtUri(controllerAction);
    }
    
    return null;
  }
  
  private void readMappings() throws ParserConfigurationException, SAXException, IOException
  {
    if (!ConfigurationManager.checkExistence(ConfigGroup.CLIENT, fileName)) {
      return;
    }
    
    synchronized(lock) {
      if (map != null) {
        return;
      }
      
      map = new HashMap<String, ControllerMapping>();
    }
    
    InputStream stream = ConfigurationManager.getResourceAsStream(ConfigGroup.CLIENT, fileName);
    
    if (stream != null) {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(stream);
      
      Element mappings = doc.getDocumentElement();
      
      NodeList children = mappings.getChildNodes();
      for (int i = children.getLength()-1; i >= 0; --i) {
        Node n = children.item(i);
        
        if (n.getNodeType() == Node.ELEMENT_NODE) {
          Element el = (Element) n;
          
          String url = el.getAttribute("url");
          String controller = el.getAttribute("controller");
          
          if (url == null || url == "") {
            String exMsg = "Servlet request mapping requires a url.";
            throw new RunwayConfigurationException(exMsg);
          }
          else if (controller == null || controller == "") {
            String exMsg = "Servlet request mapping requires a controller class name.";
            throw new RunwayConfigurationException(exMsg);
          }
          
          ControllerMapping actionMapper;
          if (map.get(controller) != null) {
            actionMapper = map.get(controller);
          }
          else {
            actionMapper = new ControllerMapping(controller);
          }
          
          ArrayList<Method> methods = null;
          try {
            Class<?> clazz = LoaderDecorator.load(controller);
            Class<?> clazzBase = LoaderDecorator.load(controller + "Base");
            methods = new ArrayList<Method>(Arrays.asList(clazz.getMethods()));
            Iterator<Method> it = methods.iterator();
            while (it.hasNext()) {
              Method m = it.next();
              if (!m.getDeclaringClass().equals(clazz) && !m.getDeclaringClass().equals(clazzBase)) {
                it.remove();
              }
            }
          }
          catch (Throwable t) {
            String exMsg = "Exception loading controller class [" + controller + "].";
            throw new RunwayConfigurationException(exMsg, t);
          }
          
          NodeList actions = el.getChildNodes();
          for (int iAction = actions.getLength()-1; 0 <= iAction; --iAction) {
            Node nodeAct = actions.item(iAction);
            
            if (nodeAct.getNodeType() == Node.ELEMENT_NODE) {
              Element elAction = (Element) nodeAct;
              
              String method = elAction.getAttribute("method");
              String actionUrl = elAction.getAttribute("url");
              
              boolean didMatch = false;
              for (Method m : methods) {
                if (m.getName().matches(method)) {
                  actionMapper.add(m.getName(), actionUrl);
                  didMatch = true;
                }
              }
              
              if (!didMatch) {
                String exMsg = "The method regex [" + method + "] for action [" + actionUrl + "] did not match any methods on the controller class definition [" + controller + "].";
                throw new RunwayConfigurationException(exMsg);
              }
            }
          }
          
          map.put(url, actionMapper);
        }
      }
      
      String info = "URL mappings successfully read from " + fileName + ":\n" + this.toString();
      log.info(info);
      System.out.println(info);
    }
  }
  
  @Override
  public String toString() {
    String out = "";
    
    Set<String> keys = map.keySet();
    for (String key : keys) {
      ControllerMapping controller = map.get(key);
      out = out + controller.toString();
    }
    
    return out;
  }
  
  class ControllerMapping {
    private String controller;
    private ArrayList<ActionMapping> actions;
    
    class ActionMapping {
      String actionName;
      ArrayList<String> uris;
      ControllerMapping controller;
      
      public ActionMapping(ControllerMapping controller, String actionName, String url) {
        this.actionName = actionName;
        this.controller = controller;
        
        this.uris = new ArrayList<String>();
        this.addUri(url);
      }
      
      public void addUri(String uri) {
        String replaced = uri.replace("${method}", actionName);
        
        if (!this.uris.contains(replaced)) {
          this.uris.add(replaced);
        }
      }
      
      public boolean handlesURI(String uri) {
        for (String myUri : uris) {
          if (uri.matches(myUri)) {
            return true;
          }
        }
        
        return false;
      }
      
      public void setMethodName(String val) {
        this.actionName = val;
      }
      
      public String getMethodName() {
        return actionName;
      }
      
      public ControllerMapping getControllerMapping() {
        return this.controller;
      }
      
      @Override
      public String toString() {
        return this.toString("");
      }
      
      public String toString(String indent) {
        return indent + "ActionMapping: " + actionName + " = " + uris.toString(); 
      }
    }
    
    public ControllerMapping(String controller) {
      this.controller = controller;
      actions = new ArrayList<ActionMapping>();
    }
    
    public String getControllerClassName() {
      return this.controller;
    }
    
    public ActionMapping getActionAtUri(String uri) {
      for (int i = actions.size()-1; i >= 0; --i) {
        ActionMapping action = actions.get(i);
        if (action.handlesURI(uri)) {
          return action;
        }
      }
      
      return null;
    }
    
    public void add(String actionName, String url) {
//      Iterator<ActionMapping> it = actions.iterator();
//      while (it.hasNext()) {
//        ActionMapping action = it.next();
//        if (action.getMethodName().equals(actionName)) {
//          action.addUri(url);
//          it.remove();
//          actions.add(action);
//          return;
//        }
//      }
      
      actions.add(new ActionMapping(this, actionName, url));
    }
    
    public String toString(String indent) {
      String innerIndent = indent;
      if (indent == null) {
        indent = "";
        innerIndent = "  ";
      }
      
      String out = indent + "ControllerMapping: " + this.controller + " = [\n";
      
      for (int i = actions.size()-1; i >= 0; --i) {
        ActionMapping action = actions.get(i);
        out = out + indent + action.toString(innerIndent) + "\n";
      }
      
      out = out + indent + "]";
      
      return out;
    }
    
    @Override
    public String toString() {
      return this.toString(null);
    }
  }
}
