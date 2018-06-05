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

import java.io.IOException;

import com.runwaysdk.patcher.RunwayPatcher;

/**
 * !!HEADS UP!!
 * 
 * !! DO NOT COMMIT YOUR RUNWAY JAVA METADATA TO THIS FILE ANYMORE !!
 * 
 * It is OK to move Runway metadata in and out of this file locally (for purposes of running it), but it should NEVER BE COMMITTED here.
 * 
 * To commit your Runway metadata, create a new file with the name of your feature (i.e. MdTable.java), and commit that file at:
 *   runwaysdk-test/src/main/domain/archive/java
 * 
 * Why are we doing this?? Because this file has gotten to be a gigantic mess and because we're losing track of our metadata. Some SQL
 * diff files were never committed and I had to dig through git history on this file to find the Java source I needed to recreate
 * the SQL diff files for DDMS.
 * 
 * Ideally your Java file should also be timestamped, i.e. : (0001470948538281)MdTable.java
 * 
 * Don't forget to check in your sql diff to:
 *   runwaysdk-test/src/main/domain/archive/sql
 * 
 * @author rrowlands
 *
 */
public class Sandbox
{
  public static void main(String[] args)
  {
    String attributeName = "test.value";
    int index = attributeName.lastIndexOf(".");
    String structName = attributeName.substring(0, index);
    String structAttribute = attributeName.substring(index + 1);
    
    System.out.println(structName);    
    System.out.println(structAttribute);
  }

//  public static void main(String[] args) throws IOException
//  {
//    
//    RunwayPatcher.main(new String[]{"postgres","postgres","postgres","true"});
////    Sandbox.bootstrap();
////    InstallerCP.main(new String[]{"postgres", "postgres", "postgres", "com/runwaysdk/resources/xsd/schema.xsd"});
//  }  
}
