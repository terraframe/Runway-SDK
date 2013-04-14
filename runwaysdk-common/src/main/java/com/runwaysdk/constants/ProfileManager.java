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
package com.runwaysdk.constants;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.TreeMap;

import com.runwaysdk.configuration.RunwayConfigurationException;
import com.runwaysdk.util.FileIO;
import com.terraframe.utf8.UTF8ResourceBundle;

/**
 * Manages access to properties files in profiles
 * 
 * @author Eric
 */
public class ProfileManager
{
  /**
   * Searches the specified profile director for bundles. This is what makes
   * this work.
   */
  private ClassLoader    loader;

  /**
   * The root directory of the profile: profiles/profileName/
   */
  private File           profile;

  /**
   * The root directory of all profiles: profiles/
   */
  private File           profilesRoot;

  private static boolean isFlattened;

  /**
   * A holder class for access to the singleton. Allows for lazy instantiation
   * and thread safety because the class is not loaded until the first access to
   * INSTANCE.
   */
  private static class Singleton
  {
    private static final ProfileManager INSTANCE = new ProfileManager();
  }

  /**
   * Private constructor sets up the ClassLoader that will be used to find
   * bundles
   */
  private ProfileManager()
  {
    // First find the profiles directory. This is where either
    // 1) master.properties is or 2) where a flattened profile is
    URL profileHome = ProfileManager.class.getResource("/master.properties");

    if (profileHome != null)
    {
      isFlattened = false;
      
      // Are they telling us their profile home is somewhere else? (Used if Runway is bundled in a jar)
      Properties prop = new Properties();
      
      try {
        prop.load(getClass().getClassLoader().getResourceAsStream("/master.properties"));
      }
      catch (IOException e) {
        throw new RunwayConfigurationException(e);
      }
      
      String proHome = prop.getProperty("profile.home");
      
      if (proHome != null) {
        File newProfileHome = new File(proHome + "/master.properties");
        
        if (newProfileHome == null || newProfileHome.exists() == false) {
          String errMsg = "Your master.properties on the classpath specified an alternate profile home, but Runway was unable to find a master.properties at the location it specified. [" + proHome + "/master.properties]";
          throw new RunwayConfigurationException(errMsg);
        }
        
        try {
          profileHome = newProfileHome.toURI().toURL();
        }
        catch (MalformedURLException e) {
          throw new RunwayConfigurationException(e);
        }
      }
    }
    else
    {
      profileHome = ProfileManager.class.getResource("/terraframe.properties");
      isFlattened = true;
    }

    // If we can't find either master.properties or a flattened profile, we have
    // a problem
    if (profileHome == null)
    {
      String errMsg = "Unable to locate master.properties or a flattened profile on your classpath. Make sure that your project has been compiled and that master.properties exists in src/main/resources.";
      throw new RunwayConfigurationException(errMsg);
    }

    // Finally convert the URL into a file
    try
    {
      profile = new File(profileHome.toURI()).getParentFile();
    }
    catch (URISyntaxException e)
    {
      //CommonExceptionProcessor.processException(ExceptionConstants.ConfigurationException.getExceptionClass(), e.getMessage(), e);
      throw new RunwayConfigurationException(e);
    }

    profilesRoot = profile;

    // Now that we know the profiles directory, we need to set up the
    // classloader for localizable content
    if (!isFlattened)
    {
      try
      {
        // Append the profile name to the profile directory
        profile = new File(profile, ProfileReader.getProfileName());
        List<File> folderTree = FileIO.getFolderTree(profile);
        URL[] urls = new URL[folderTree.size()];
        for (int i = 0; i < folderTree.size(); i++)
          urls[i] = folderTree.get(i).toURI().toURL();
        loader = new URLClassLoader(urls);
      }
      catch (MalformedURLException e)
      {
        String errMsg = "Profile [" + ProfileReader.getProfileName() + "] was not found.";
        throw new RunwayConfigurationException(errMsg);
        //CommonExceptionProcessor.processException(ExceptionConstants.ConfigurationException.getExceptionClass(), errMsg, e);
      }
    }
    else
    {
      loader = ProfileManager.class.getClassLoader();
    }
  }

  /**
   * Returns the given bundle as retrieved from the current profile
   * 
   * @param fileName
   * @return
   */
  public static ProfileReader getBundle(String fileName)
  {
    File props = new File(Singleton.INSTANCE.profile, fileName);
    if (!props.exists())
      props = new File(Singleton.INSTANCE.profile, props.getName());

    try
    {
      ProfileReader profileReader = new ProfileReader(props);
      profileReader.read();
      return profileReader;
    }
    catch (IOException e)
    {
      e.printStackTrace();
      String errMsg = "Problem reading properties file [" + fileName + "].";
      throw new RunwayConfigurationException(errMsg);
      //CommonExceptionProcessor.processException(ExceptionConstants.ConfigurationException.getExceptionClass(), errMsg, e);
    }
  }

  /**
   * Returns the given bundle from the specified locale as retrieved from the
   * current profile
   * 
   * @param baseName
   * @param locale
   * @return
   */
  public static ResourceBundle getBundle(String baseName, Locale locale)
  {
    return UTF8ResourceBundle.getBundle(baseName, locale, Singleton.INSTANCE.loader);
  }

  public static File getResource(String name)
  {
    URL url = ProfileManager.class.getClassLoader().getResource(name);
    File file = null;

    if (url == null)
    {
      String errMsg = "Problems locating file [" + name + "] - unable to find the resource on the classpath.";
//      ProfileManager.logger.fatal(errMsg);
      throw new RunwayConfigurationException(errMsg);
    }

    try
    {
      file = new File(url.toURI());
    }
    catch (URISyntaxException e)
    {
      String errMsg = "Problems locating resource [" + name + "] - unable to convert URL [" + url + "] to a file.";
      throw new RunwayConfigurationException(errMsg);
      //CommonExceptionProcessor.processException(ExceptionConstants.ConfigurationException.getExceptionClass(), errMsg, e);
    }
    return file;
  }

  /**
   * 
   * @param name
   * @param cliComOrServ
   *          This parameter specifies in which directory (client, common, or
   *          server) to look for the resource in the current profile.
   * @return
   */
  public static File getResource(String name, String cliComOrServ)
  {
    if (isFlattened)
    {
      return getResource(name);
    }

    File file = new File(Singleton.INSTANCE.profile, cliComOrServ + "/" + name);

    if (!file.exists())
    {
      String errMsg = "Attempted to find resource of name [" + name + "], but the file did not exist at location [" + file.getAbsolutePath() + "].";
//      ProfileManager.logger.fatal(errMsg);
      throw new RunwayConfigurationException(errMsg);
    }

    return file;
  }

  public static Map<String, String> getAllProperties() throws IOException
  {
    Map<String, String> allProps = new TreeMap<String, String>();
    for (File file : FileIO.listFilesRecursively(Singleton.INSTANCE.profile))
    {
      if (file.isDirectory())
        continue;
      if (!file.getName().endsWith(".properties"))
        continue;

      ProfileReader reader = new ProfileReader(file);
      reader.read();
      for (String key : reader.getKeys())
        allProps.put(key, reader.getString(key));
    }
    return allProps;
  }

  public static File getProfileDir()
  {
    return Singleton.INSTANCE.profile;
  }

  public static File getProfileRootDir()
  {
    return Singleton.INSTANCE.profilesRoot;
  }
}
