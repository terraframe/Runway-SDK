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
package com.runwaysdk.profile;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.configuration.ProfileManager;
import com.runwaysdk.configuration.ProfileReader;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.util.FileIO;

public class ProfileFlattener
{
  private File destination;
  private File source;

  private class CustomReader extends ProfileReader
  {
    private Set<String> imports = new HashSet<String>();

    public CustomReader(File file)
    {
      super(file, new HashSet<File>(), false);
    }

    @Override
    protected void readImports(String importPath) throws IOException
    {
      imports.add(importPath);
    }
  }

  public static void main(String[] args)
  {
    ProfileFlattener profileFlattener = null;
    if (args.length == 1)
    {
      profileFlattener = new ProfileFlattener(args[0]);
    }
    else if (args.length == 2)
    {
      profileFlattener = new ProfileFlattener(args[0], args[1]);
    }
    else
    {
      String errMsg = "Two arguments are accepted by ProfileFlattener:\n" +
                      "  1) Name of the source profile\n" +
                      "  2) Name of the destination profile\n" +
                      "The source profile parameter is optional.  If not specified, then the current profile is used as the source.";
      CommonExceptionProcessor.processException(ExceptionConstants.CoreException.getExceptionClass(), errMsg);
    }
    profileFlattener.write();
  }

  /**
   * Flattens the given profile, which means that all supers will be inlined
   * into a single file. This makes the profile a self contained entity, leaving
   * only intraprofile imports as dependencies.
   *
   * @param dest
   *            The name of the profile that will be flattened
   */
  public ProfileFlattener(String dest)
  {
    this(ProfileReader.getProfileName(), dest);
  }

  public ProfileFlattener(String src, String dest)
  {
    File root = ProfileManager.getProfileDir().getParentFile();
    destination = new File(root, dest);
    source = new File(root, src);
  }

  public void write()
  {
    for (File file : FileIO.listFilesRecursively(source))
    {
      if (!file.getName().endsWith(".properties"))
      {
        try
        {
          FileIO.copyFile(file, new File(destination, file.getName()));
          continue;
        }
        catch (IOException e)
        {
          String errMsg = "Problem copying file [" + file.getName() + "].";
          CommonExceptionProcessor.processException(ExceptionConstants.ConfigurationException.getExceptionClass(), errMsg, e);
        }
      }

      CustomReader reader = new CustomReader(file);
      try
      {
        reader.read();
      }
      catch (IOException e)
      {
        String errMsg = "Problem reading properties file [" + file.getName() + "].";
        CommonExceptionProcessor.processException(ExceptionConstants.ConfigurationException.getExceptionClass(), errMsg, e);
      }

      write(file.getName(), reader);
    }
  }

  private void write(String fileName, CustomReader reader)
  {
    String data = new String();
    for (String mport : reader.imports)
      data += "import=" + new File(mport).getName() + "\n";
    for (String key : reader.getKeys())
      data += key + "=" + reader.getString(key) + "\n";

    File file = new File(destination, fileName);
    try
    {
      FileIO.write(file, data);
    }
    catch (IOException e)
    {
      String errMsg = "Problem writing properties file [" + file.getName() + "].";
      CommonExceptionProcessor.processException(ExceptionConstants.ConfigurationException.getExceptionClass(), errMsg, e);
    }
  }
}
