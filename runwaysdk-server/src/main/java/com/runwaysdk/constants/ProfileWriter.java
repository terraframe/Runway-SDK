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
package com.runwaysdk.constants;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.configuration.ProfileManager;
import com.runwaysdk.dataaccess.io.FileReadException;
import com.runwaysdk.dataaccess.io.FileWriteException;
import com.runwaysdk.util.FileIO;


public class ProfileWriter
{
  private File parentRoot;
  private File childRoot;
  
  public ProfileWriter(String parent, String child)
  {
    File profileHome = ProfileManager.getProfileDir().getParentFile();
    this.parentRoot = new File(profileHome, parent);
    this.childRoot = new File(profileHome, child);
  }
  
  public void create()
  {
    for (File parent : FileIO.listFilesRecursively(parentRoot))
    {
      File child = getChildFileFromParent(parent);
      
      // If this isn't a .properties file, just copy it
      if (!child.getName().endsWith(".properties"))
      {
        try
        {
          FileIO.copyFile(parent, child);
          continue;
        }
        catch (IOException e)
        {
          throw new FileWriteException(child, e);
        }
      }
      
      List<String> lines = new LinkedList<String>();
//      lines.add("include=../../" + parentRoot.getName() + "/" + parent.getParentFile().getName() + "/" + parent.getName());
      lines.add("super=" + parentRoot.getName());
      try
      {
        for(String line : FileIO.readString(parent).split("\n"))
        {
          String trim = line.trim();
          if (trim.startsWith("#") || trim.startsWith("import") || trim.length()==0)
            lines.add(trim);
          else
            lines.add("#" + trim);
        }
      }
      catch (IOException e)
      {
        throw new FileReadException(parent, e);
      }
      
      String total = new String();
      for (String line : lines)
        total += line + "\n";
      try
      {
        FileIO.write(child, total);
      }
      catch (IOException e)
      {
        throw new FileWriteException(child, e);
      }
    }
  }
  
  private File getChildFileFromParent(File parentFile)
  {
    String fullPath = parentFile.getAbsolutePath();
    String profilePath = parentRoot.getAbsolutePath();
    String propertyPath = fullPath.substring(profilePath.length());
    String newPath = childRoot.getAbsolutePath() + '/' + propertyPath;
    return new File(newPath);
  }
  
  public static void main(String[] args)
  {
    if (args.length!=2)
    {
      System.out.println("Usage: ProfileWriter [parentProfile] [childProfile]");
      System.out.println("  -parentProfile: Name of the existing profile to inherit from");
      System.out.println("  -childProfile: Name of the new child profile to create");
    }
    new ProfileWriter(args[0], args[1]).create();
  }
}
