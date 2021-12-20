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
package com.runwaysdk.web.javascript;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.runwaysdk.constants.ClientProperties;
import com.runwaysdk.dataaccess.io.FileReadExceptionDTO;

/**
 * Caches all javascript.
 *
 */
public class JavascriptCache
{
  /**
   * The one and only instance of this class.
   */
  private static JavascriptCache instance = null;
  
  /**
   * The javascript.
   */
  private String javascript;
  
  /**
   * Private constructor to create singleton.
   */
  private JavascriptCache()
  {    
    String jsDir = ClientProperties.getJavascriptDir();
    
    StringBuilder builder = addFileContents(new File(jsDir + "/" + JavascriptConstants.RUNWAY_JS_FILE));
    
    javascript = builder.toString();// FIXME broken (not replacing Ajax endpoint)
  }
  
  /**
   * Returns javascript code.
   * 
   * @return
   */
  public static synchronized String getJavascript()
  {
    if(instance == null)
    {
      instance = new JavascriptCache();
    }
    
    return instance.javascript;
  }
  
  /**
   * Adds the contents of one file to the outgoing javascript string.
   * 
   * @param file
   */
  private StringBuilder addFileContents(File jsFile)
  {
    try
    {
      StringBuilder builder = new StringBuilder();
      Scanner scanner = new Scanner(jsFile);
      while(scanner.hasNextLine())
      {
        builder.append(scanner.nextLine()+"\n"); // add a newline to make the source readable
      }
      scanner.close();
      
      return builder;
    }
    catch (FileNotFoundException e)
    {
      String error = "The javascript file ["+jsFile.toString()+"] cannot be read.";
      throw new FileReadExceptionDTO(error);
    }
  }
}
