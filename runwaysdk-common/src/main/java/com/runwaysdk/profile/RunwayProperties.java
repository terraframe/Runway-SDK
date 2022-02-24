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
package com.runwaysdk.profile;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.runwaysdk.configuration.ProfileReader;

public class RunwayProperties extends Task
{
  // The profile root directory that contains master.properties
  private File root;

  public void setRoot(File root)
  {
    this.root = root;
  }

  /**
   * The overridden hook method, this will intelligently load all of our
   * properties into the system.
   *
   * @see org.apache.tools.ant.Task#execute()
   */
  public void execute() throws BuildException
  {
    if (root==null)
      throw new BuildException("Attribute \"root\" is required for the TfProperties task.");

    if (!root.exists())
      throw new BuildException("Root path [" + root.getAbsolutePath() + "] does not exist.");

    if (!root.isDirectory())
      throw new BuildException("Root path [" + root.getAbsolutePath() + "] is invalid. \"root\" should point to your profile root directory.");

    File master = new File(root, "master.properties");
    File profile;

    // This means that there is no master, which means a flattened profile.
    if (!master.exists())
    {
      profile = root;
    }
    else
    {
      // Check to see if the user has manually specified profile.name
      String profileName = getProject().getProperty("profile.name");

      if (profileName!=null)
      {
        System.setProperty("profile.name", profileName);
      }

      profile = new File(root, ProfileReader.getProfileName());
    }

    try
    {
      // Set a system variable to store to profile directory
      System.setProperty(ProfileReader.PROFILE_DIR, profile.getAbsolutePath());
      load(profile);
    }
    catch (IOException e)
    {
      throw new BuildException(e);
    }
  }

  private void load(File file) throws IOException
  {
    if (file.isDirectory())
    {
      for (File f : file.listFiles())
        load(f);
      return;
    }

    if (!file.getName().endsWith(".properties"))
      return;

    // If we're here, file must not be a folder
    ProfileReader props = new ProfileReader(file);
    props.read();
    for (String key : props.getKeys())
      getProject().setNewProperty(key, props.getString(key));
  }
}
