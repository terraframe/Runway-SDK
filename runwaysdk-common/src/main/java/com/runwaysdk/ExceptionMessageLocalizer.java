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
package com.runwaysdk;

import java.util.Hashtable;
import java.util.ResourceBundle;

public abstract class ExceptionMessageLocalizer
{
  /**
   * Rather than reload the bundle every time a message is requested, maintain a cache.
   * ResourceBundles are soft cached under the hood, but can be unloaded when there are no
   * more references to them. Since the lifespan for each bundle reference is a single
   * method, we don't want to rely on nondeterministic soft caching.
   */
  protected volatile static Hashtable<String, ResourceBundle> props = new Hashtable<String, ResourceBundle>();
  
  /**
   * Parses the parameterized, localized error message template for the given exception.
   * The variable String arguments represent the parameters in the template string. For
   * example, given the template "The {0} in the {1}." and arguments "cat" and "hat", the
   * final String will be "The cat in the hat."
   * 
   * @param key
   *          The name of the Exception whose message is being retrieved
   * @param params
   *          The array of parameters to plug into the template string
   */
  protected static String parseMessage(String template, String... params)
  {
    // Sub in all the parameters
    for (int i = 0; i < params.length; i++)
    {
      // If the string is too long (more than 256 characters) shorten it.
      // We don't want a 3 page error message.
      if (params[i].length() > 256)
        params[i] = params[i].substring(0, 256) + "...";

      template = template.replace("{" + i + "}", params[i]);
    }

    return template;
  }
}
