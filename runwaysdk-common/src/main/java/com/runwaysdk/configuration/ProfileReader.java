/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.configuration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.runwaysdk.util.FileIO;
import com.terraframe.utf8.UTF8ResourceBundle;

public class ProfileReader implements ConfigurationReaderIF
{
  public static final String  PROFILE_DIR = "profile.dir";

  private static String       profileName = System.getProperty("profile.name");
  
  private static LegacyPropertiesSupport legacyProps = LegacyPropertiesSupport.getInstance();

  /**
   * The properties in this file. For simplicity, we store them as Strings and
   * convert to other types (Boolean, Integer, etc.) on request.
   */
  private Map<String, String> properties;

  /**
   * The .properties file that we're reading
   */
  private File                file;

  /**
   * Used to check for circular dependencies.
   */
  private Set<File>           dependents;

  /**
   * Represents whether or not we will resolve variables in property values.
   */
  private boolean             resolve;

  private Map<String, String> rawProps;

  private Map<String, String> imports;

  private Map<String, String> supers;

  public ProfileReader(File file)
  {
    this(file, new HashSet<File>(), true);
  }

  protected ProfileReader(File file, Set<File> dependents, boolean resolve)
  {
    this.file = file;
    this.resolve = resolve;
    this.dependents = dependents;

    rawProps = new LinkedHashMap<String, String>();
    imports = new LinkedHashMap<String, String>();
    supers = new LinkedHashMap<String, String>();
  }

  public void read() throws IOException
  {
    // Check to see if this file is dependent on itself
    if (!dependents.add(file))
      throw new IOException("Circular dependency detected in profile [" + getProfileName() + "]");

    properties = new Hashtable<String, String>();
    properties.put("profile.name", getProfileName());

    // Read in the properties
    inputFromFile();

    // We have successfully read this file. Remove it from the dependency list
    dependents.remove(file);

    // These structures are temporary, so once we're done we null them out to
    // save memory
    rawProps = null;
    imports = null;
    supers = null;
  }
  
  private void inputFromFile() throws IOException
  {
    List<String> lines = FileIO.readLines(file);

    for (String line : lines)
    {
      // Comments aren't properties
      if (line.startsWith("#"))
        continue;

      // Blank lines are also boring
      if (line.trim().length() == 0)
        continue;

      String[] split = line.split("=", 2);
      // if (split.length!=2)
      // throw an error;

      String key = split[0].trim();
      String value = split[1].trim();
      String overridden = System.getProperty(key);

      if (key.equals("import"))
        readImports(value);
      else if (key.equals("super"))
        readSupers(value);
      else if (overridden != null)
        rawProps.put(key, overridden);
      else
        rawProps.put(key, value);
    }

    // Now we need to read (and resolve) the raw properties
    if (resolve)
    {
      for (String key : rawProps.keySet())
      {
        String resolved = resolve(rawProps.get(key));
        properties.put(key, resolved);
      }
    }
    else
    {
      properties = rawProps;
    }
  }

  /**
   * Reads and resolves an imported property file.
   * 
   * @param importPath
   * @throws IOException
   */
  protected void readImports(String importPath) throws IOException
  {
    // File profile = file.getParentFile().getParentFile();
    File profile = getProfileDir();
    ProfileReader imported = new ProfileReader(new File(profile, importPath), dependents, true);
    imported.read();
    imports.putAll(imported.properties);
  }

  private File getProfileDir()
  {
    String property = System.getProperty(PROFILE_DIR);
    if (property != null)
      return new File(property);

    return ProfileManager.getProfileDir();
  }

  /**
   * Reads a super property file. Properties are not resolved now, but instead
   * added to the rawProps collection, which is resolved after all imports and
   * supers have been read.
   * 
   * @param superName
   * @throws IOException
   */
  private void readSupers(String superName) throws IOException
  {
    File profileRoot = getProfileDir().getParentFile();
    File superFile = new File(profileRoot, superName + "/" + file.getParentFile().getName() + "/" + file.getName());

    ProfileReader imported = new ProfileReader(superFile, dependents, false);
    imported.read();
    rawProps.putAll(imported.properties);
  }

  /**
   * Takes a raw property value and dereferences all variables inside it. For
   * example, given:
   * 
   * <ul>
   * <li>first.name=50</li>
   * <li>last.name=Cent</li>
   * <li>full.name=${first.name} ${last.name}</li>
   * </ul>
   * 
   * After resolution, full.name=50 Cent
   * 
   * @param value
   * @return
   */
  private String resolve(String value)
  {
    if (value == null)
      return value;

    // We need to copy the original so that in-line replaces don't eff up the
    // indices
    String original = new String(value);
    Matcher matcher = Pattern.compile("\\$\\{[^}]*}").matcher(original);
    while (matcher.find())
    {
      String varName = original.substring(matcher.start() + 2, matcher.end() - 1);
      String varValue = properties.get(varName);
      if (varValue == null)
        varValue = resolve(rawProps.get(varName));
      if (varValue == null)
        varValue = imports.get(varName);
      if (varValue == null)
        varValue = supers.get(varName);
      if (varValue == null)
        continue;

      value = value.replace("${" + varName + "}", varValue);
    }
    return value;
  }

  /**
   * Returns a set of the LEGACY keys in this properties file, suitable for iteration.
   * Note that the set includes keys that have been imported or supered in from
   * other files
   * 
   * @return
   */
  public Set<String> getKeys()
  {
    return properties.keySet();
  }

  /**
   * Returns the value for the given key as a String. If the key isn't found,
   * null is returned.
   * 
   * @param key
   * @return
   */
  public String getString(String key)
  {
    return properties.get(legacyProps.iModernToLegacy(key));
  }

  /**
   * Returns the value for the given key as a Boolean. If the String value
   * cannot be parsed into a boolean, an exception is thrown. If the key isn't
   * found, null is returned.
   * 
   * @param key
   * @return
   */
  public Boolean getBoolean(String key)
  {
    return Boolean.parseBoolean(properties.get(legacyProps.iModernToLegacy(key)));
  }

  /**
   * Returns the value for the given key as a Boolean. If the String value
   * cannot be parsed into an integer, an exception is thrown. If the key isn't
   * found, the default value is returned.
   * 
   * @param key
   * @param defaultValue
   * @return
   */
  public Integer getInteger(String key)
  {
    return Integer.parseInt(properties.get(legacyProps.iModernToLegacy(key)));
  }

  /**
   * Returns the value for the given key as a String. If the key isn't found,
   * the default value is returned.
   * 
   * @param key
   * @param defaultValue
   * @return
   */
  public String getString(String key, String defaultValue)
  {
    String value = properties.get(legacyProps.iModernToLegacy(key));

    if (value != null)
    {
      return value;
    }

    return defaultValue;
  }

  /**
   * Returns the value for the given key as a Boolean. If the String value
   * cannot be parsed into a boolean, an exception is thrown. If the key isn't
   * found, the default value is returned.
   * 
   * @param key
   * @param defaultValue
   * @return
   */
  public Boolean getBoolean(String key, Boolean defaultValue)
  {
    String value = properties.get(legacyProps.iModernToLegacy(key));

    if (value != null)
    {
      return Boolean.parseBoolean(value);
    }

    return defaultValue;
  }

  /**
   * Returns the value for the given key as a Boolean. If the String value
   * cannot be parsed into an integer, an exception is thrown. If the key isn't
   * found, null is returned.
   * 
   * @param key
   * @return
   */
  public Integer getInteger(String key, Integer defaultValue)
  {
    String value = properties.get(legacyProps.iModernToLegacy(key));

    if (value != null)
    {
      return Integer.parseInt(value);
    }

    return defaultValue;
  }

  /**
   * Gets the name of the profile that's being used. If the profile name was not
   * set as a command line argument, it is read from master.properties. In
   * either case, once read it is stored as a System Property and read from
   * there on subsequent requests.
   * 
   * @return
   */
  public static String getProfileName()
  {
    if (profileName == null)
    {
      try
      {
        ResourceBundle bundle = UTF8ResourceBundle.getBundle("master");
        profileName = bundle.getString("profile.name");
      }
      catch (MissingResourceException e)
      {
        profileName = "";
      }
    }
    return profileName;
  }
  
  /**
   * @see com.runwaysdk.configuration.ConfigurationReaderIF#setProperty(java.lang.String, java.lang.Object)
   */
  @Override
  public void setProperty(String key, Object value)
  {
    throw new UnsupportedOperationException();
  }
}
