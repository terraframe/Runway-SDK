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
package com.runwaysdk.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import com.runwaysdk.util.FileIO;
import com.terraframe.utf8.UTF8ResourceBundle;

/**
 * Manages access to properties files in profiles
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
   * This can be set by programs (Like the Eclipse plugin) to use a profile home that isn't on the classpath.
   */
  private static String explicitlySpecifiedProfileHome = null;

  /**
   * A holder class for access to the singleton. Allows for lazy instantiation
   * and thread safety because the class is not loaded until the first access to
   * INSTANCE.
   */
  private static class Singleton
  {
    private static ProfileManager INSTANCE = new ProfileManager();
  }

  /**
   * Private constructor sets up the ClassLoader that will be used to find
   * bundles
   */
  private ProfileManager()
  {
    try {
      if (ProfileManager.explicitlySpecifiedProfileHome != null) {
        initialize(new File(ProfileManager.explicitlySpecifiedProfileHome));
      }
      else {
        File root;
        try {
          root = new File(ProfileManager.class.getClassLoader().getResource("master.properties").toURI()).getParentFile();
        }
        catch (Exception e) {
          root = new File(ProfileManager.class.getClassLoader().getResource("terraframe.properties").toURI()).getParentFile();
        }
        
        initialize(root);
      }
    }
    catch (Exception e) {
      throw new RunwayConfigurationException(e);
    }
  }
  
  private void initialize(File root) throws MalformedURLException {
    // First find the profiles directory. This is where either
    // 1) master.properties is or 2) where a flattened profile is
    File master = new File(root, "master.properties");
    
    if (master.exists())
    {
      isFlattened = false;
    }
    else
    {
      if (!new File(root, "terraframe.properties").exists()) {
        // If we can't find either master.properties or a flattened profile, we have a problem.
        String errMsg = "Unable to locate master.properties or a flattened profile on your classpath. Make sure that your project has been compiled and that master.properties exists at the classpath root.";
        throw new RunwayConfigurationException(errMsg);
      }
      
      isFlattened = true;
    }

    profile = root;
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
  
  public static boolean isFlattened() {
    return ProfileManager.isFlattened;
  }

  /**
   * Sets the profile home explicitly. This will be read as :  new File(profileHome + "/master.properties"), so the path can be
   * relative according to the java.io.File rules, or absolute.
   * 
   * @param profileHome
   */
  public static void setProfileHome(String profileHome)
  {
    ProfileManager.explicitlySpecifiedProfileHome = profileHome;
    
    if (!new File(ProfileManager.explicitlySpecifiedProfileHome).exists()) {
      throw new RunwayConfigurationException(new FileNotFoundException(profileHome));
    }
    
    ProfileManager.Singleton.INSTANCE = new ProfileManager();
  }
  
  public static String getExplicitySpecifiedProfileHome() {
    return ProfileManager.explicitlySpecifiedProfileHome;
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
