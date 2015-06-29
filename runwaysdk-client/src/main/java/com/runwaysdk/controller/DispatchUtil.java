/**
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
 */
package com.runwaysdk.controller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import com.runwaysdk.AttributeNotificationDTO;
import com.runwaysdk.ClientRequest;

/**
 * 
 * @author Justin Smethie
 */
public class DispatchUtil
{
  /**
   * Annotation describing the parameter names and types
   */
  private ActionParameters annotation;

  /**
   * Contains information about the status of the scraping
   */
  private RequestManager   manager;

  /**
   * List of objects scrapped from the {@link HttpServletRequest}
   */
  private Object[]         objects;

  /**
   * @param annotation
   *          {@link ActionParameters} describing the parameter names and types
   * @param manager
   *          Manager of the {@link HttpServletRequest} scraping
   */
  public DispatchUtil(ActionParameters annotation, RequestManager manager, Map<String, Parameter> parameters)
  {
    this.annotation = annotation;
    this.manager = manager;

    objects = loadObjects(parameters);

    if (manager.hasExceptions())
    {
      // Rescrap all of the primitive values such that the values to strings
      this.prepareFailure(parameters);
      this.propigateExceptions();
    }
  }

  /**
   * Add all parse exceptions to the list of notifications and problems
   */
  private void propigateExceptions()
  {
    ClientRequest clientRequest = (ClientRequest) manager.getClientRequest();

    for (AttributeNotificationDTO problem : manager.getAttributeNotifications())
    {
      // Pull the ParseException out and add it to the
      // the list of out going messages messages?????
      ClientRequest.addAttributeNotification(clientRequest, problem);
    }

    // Don't use set messages because you need to append the new
    // messages to the list of existing messages
    ClientRequest.addProblems(clientRequest, manager.getProblems());
  }

  /**
   * @return Objects scrapped from the {@link HttpServletRequest}
   */
  public Object[] getObjects()
  {
    return objects;
  }

  /**
   * @return Loads all expected parameters to objects
   */
  private final Object[] loadObjects(Map<String, Parameter> parameters)
  {
    StringTokenizer toke = new StringTokenizer(annotation.parameters(), ",");
    List<Object> objects = new LinkedList<Object>();

    // Load objects from the parameters list
    while (toke.hasMoreTokens())
    {
      String parameter = toke.nextToken();
      String[] value = parameter.split(":");
      String type = value[0].trim();
      String parameterName = value[1].trim();

      objects.add(new RequestScraper(type, parameterName, manager, parameters).convert());
    }

    return objects.toArray(new Object[objects.size()]);
  }

  /**
   * Converts all primitive parameters into their {@link String} representations
   */
  private final void prepareFailure(Map<String, Parameter> parameters)
  {
    StringTokenizer toke = new StringTokenizer(annotation.parameters(), ",");

    for (int i = 0; i < objects.length; i++)
    {
      String parameter = toke.nextToken();
      String[] value = parameter.split(":");
      String parameterName = value[1];

      if (objects[i] != null)
      {
        Class<?> c = null;

        if (objects[i] instanceof Class<?>)
        {
          c = (Class<?>) objects[i];
        }
        else
        {
          c = objects[i].getClass();
        }

        if (DispatchUtil.isPrimitive(c))
        {
          if (c.isArray())
          {
            String type = new String[0].getClass().getName();
            objects[i] = new RequestScraper(type, parameterName, manager, parameters).convert();
          }
          else
          {
            String type = String.class.getName();
            objects[i] = new RequestScraper(type, parameterName, manager, parameters).convert();
          }
        }
      }
    }
  }

  /**
   * @param c
   *          Given {@link Class} to check
   * @return If a the given class is the java.lang representation of a primitive
   *         type
   */
  public static boolean isPrimitive(Class<?> c)
  {
    if (c.isArray())
    {
      c = c.getComponentType();
    }

    Package pack = c.getPackage();

    if (c.equals(Date.class) || ( pack != null && pack.equals(String.class.getPackage()) ))
    {
      return true;
    }

    return false;
  }

}
