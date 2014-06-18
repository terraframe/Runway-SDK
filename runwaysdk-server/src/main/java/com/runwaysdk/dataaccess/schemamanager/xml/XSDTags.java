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
package com.runwaysdk.dataaccess.schemamanager.xml;

import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;

public class XSDTags implements XMLTags
{
  public static final String XS_ELEMENT = "element";
  public static final String XS_SEQUENCE = "sequence";
  public static final String XS_COMPLEX = "complexType";
  public static final String XS_ATTRIBUTE = "attribute";
  public static final String XS_ATTRIBUTEGROUP = "attributeGroup";
  public static final String XS_CHOICE = "choice";
  public static final String XS_EXTENSION = "extension";
  public static final String XS_TYPE = "type";
  public static final String XS_BASE = "base";
  
  
  public static boolean isElementGroupingTag(String tag) {
    return (tag.equals(XS_SEQUENCE) || tag.equals(XS_CHOICE));
  }
}
