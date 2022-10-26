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
package com.runwaysdk;

import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;

/**
 * !!HEADS UP!!
 * 
 * !! DO NOT COMMIT YOUR RUNWAY JAVA METADATA TO THIS FILE ANYMORE !!
 * 
 * It is OK to move Runway metadata in and out of this file locally (for
 * purposes of running it), but it should NEVER BE COMMITTED here.
 * 
 * To commit your Runway metadata, create a new file with the name of your
 * feature (i.e. MdTable.java), and commit that file at:
 * runwaysdk-test/src/main/domain/archive/java
 * 
 * Why are we doing this?? Because this file has gotten to be a gigantic mess
 * and because we're losing track of our metadata. Some SQL diff files were
 * never committed and I had to dig through git history on this file to find the
 * Java source I needed to recreate the SQL diff files for DDMS.
 * 
 * Ideally your Java file should also be timestamped, i.e. :
 * (0001470948538281)MdTable.java
 * 
 * Don't forget to check in your sql diff to:
 * runwaysdk-test/src/main/domain/archive/sql
 * 
 * @author rrowlands
 *
 */
public class Sandbox
{
  public static void main(String[] args) throws Throwable
  {
    String test = "ABC";
    String de = test;
    
    test = "DEF";
    
    System.out.println(test);
    System.out.println(de);
    
//    mainInRequest();
  }
  
  @Request
  public static void mainInRequest() throws Throwable
  {
    mainInTransaction();
  }
  
  @Transaction
  public static void mainInTransaction() throws Throwable
  {
    // Are you sure the code you're about to write here shouldn't be in a test file somewhere instead?
  }
}