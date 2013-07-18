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
package com.runwaysdk.jstest;

import groovy.lang.GroovyShell;
import groovy.lang.Script;

import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.Request;
import com.runwaysdk.util.DateUtilities;

public class BuildDomain
{
  @Request
  public static void main(String[] args)
  {
//    String s = "2005-07-15T08:59:14";
    String s = "2008-01-25T19:36:44Z";
    DateUtilities.parseDate(s);
  }
}
