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
package com.runwaysdk.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration2.MapConfiguration;

/**
 * Exposes operating system environment variables as Runway configuration
 * properties.
 * <p>
 * A property is first looked up by its exact name. If it isn't found and the
 * key contains a '.' or '-', it is then looked up by its "relaxed" name: every
 * character other than a letter or digit replaced with '_', and upper-cased.
 * For example, {@code database.password} can be supplied by the environment
 * variable {@code DATABASE_PASSWORD} and {@code keyStore.password} by
 * {@code KEYSTORE_PASSWORD}.
 * <p>
 * Keys without a '.' or '-' (such as {@code user} or {@code port}) are only
 * matched by an environment variable with exactly the same name. This keeps
 * standard OS variables like USER, HOME, PATH or DOMAIN from silently
 * overriding application properties of the same name.
 * <p>
 * The process environment is never modified. Adding a property throws
 * {@link UnsupportedOperationException}. Clearing a property is supported
 * (unlike {@link org.apache.commons.configuration2.EnvironmentConfiguration})
 * because {@link org.apache.commons.configuration2.AbstractConfiguration#interpolatedConfiguration()}
 * and {@code CompositeConfiguration.setProperty()} clear a key from every child
 * configuration; it hides that key, exactly as requested, from this instance
 * only.
 */
public class RelaxedEnvironmentConfiguration extends MapConfiguration
{
  /**
   * Property keys that have been cleared from this instance.
   */
  private Set<String> cleared = new HashSet<String>();

  private boolean     clearedAll = false;

  public RelaxedEnvironmentConfiguration()
  {
    this(System.getenv());
  }

  public RelaxedEnvironmentConfiguration(Map<String, String> environment)
  {
    super(new HashMap<String, Object>(environment));
  }

  /**
   * @return The relaxed environment variable name for the given property key,
   *         or null if the key has no relaxed form (it contains no '.' or '-').
   */
  public static String toEnvironmentName(String key)
  {
    if (key == null || ( key.indexOf('.') == -1 && key.indexOf('-') == -1 ))
    {
      return null;
    }

    return key.replaceAll("[^A-Za-z0-9]", "_").toUpperCase(Locale.ROOT);
  }

  @Override
  protected Object getPropertyInternal(String key)
  {
    if (this.isCleared(key))
    {
      return null;
    }

    Object value = super.getPropertyInternal(key);

    if (value == null)
    {
      String envName = toEnvironmentName(key);

      if (envName != null)
      {
        value = super.getPropertyInternal(envName);
      }
    }

    return value;
  }

  @Override
  protected void addPropertyDirect(String key, Object value)
  {
    throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is read-only.");
  }

  @Override
  protected void clearPropertyDirect(String key)
  {
    this.cleared.add(key);
  }

  @Override
  protected void clearInternal()
  {
    this.clearedAll = true;
  }

  @Override
  protected boolean isEmptyInternal()
  {
    return !this.getKeysInternal().hasNext();
  }

  @Override
  protected Iterator<String> getKeysInternal()
  {
    if (this.clearedAll)
    {
      return Collections.emptyIterator();
    }

    List<String> keys = new ArrayList<String>();
    super.getKeysInternal().forEachRemaining(k -> {
      if (!this.cleared.contains(k))
      {
        keys.add(k);
      }
    });
    return keys.iterator();
  }

  @Override
  protected int sizeInternal()
  {
    int size = 0;
    for (Iterator<String> it = this.getKeysInternal(); it.hasNext(); it.next())
    {
      size++;
    }
    return size;
  }

  @Override
  public Object clone()
  {
    RelaxedEnvironmentConfiguration copy = (RelaxedEnvironmentConfiguration) super.clone();
    copy.cleared = new HashSet<String>(this.cleared);
    return copy;
  }

  private boolean isCleared(String key)
  {
    return this.clearedAll || this.cleared.contains(key);
  }

  @Override
  protected boolean containsKeyInternal(String key)
  {
    if (this.isCleared(key))
    {
      return false;
    }

    if (super.containsKeyInternal(key))
    {
      return true;
    }

    String envName = toEnvironmentName(key);

    return envName != null && super.containsKeyInternal(envName);
  }
}
