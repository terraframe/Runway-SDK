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
package com.runwaysdk.transport.metadata.caching;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * This class is used to transport class metadata from server to client. This
 * allows the client to cache the class metadata. A DTO is not used in order to
 * reduce network traffic. This metadata is specific to a particular session.
 * 
 * @author Richard Rowlands
 * 
 */
public class ClassMdSession
{
  private String                          definesType;

  private String                          displayLabel;

  private String                          description;

  private String                          id;

  private ClassMdSession                  superType;
    
  private boolean                         readable;
  
  private boolean                         writable;
  
  private boolean                         deleteable;
  
  private Map<String, AttributeMdSession> mdAttributeMap = new HashMap<String, AttributeMdSession>();

  public ClassMdSession(String definesType, String displayLabel, String description, String id,
      ClassMdSession superType, boolean readable, boolean writable, boolean deleteable, Map<String, AttributeMdSession> mdAttributeMap)
  {
    this.definesType    = definesType;
    this.displayLabel   = displayLabel;
    this.description    = description;
    this.id             = id;
    this.superType      = superType;
    this.readable       = readable;
    this.writable       = writable;
    this.deleteable     = deleteable;
    this.mdAttributeMap = mdAttributeMap;
  }

  public String getDefinesType()
  {
    return definesType;
  }

  public String getDisplayLabel()
  {
    return displayLabel;
  }

  public String getDescription()
  {
    return description;
  }

  public String getId()
  {
    return id;
  }

  public ClassMdSession getSuperType()
  {
    return superType;
  }

  public Map<String, AttributeMdSession> getAttributeMap()
  {
    return mdAttributeMap;
  }

  public boolean isReadable()
  {
    return readable;
  }

  public boolean isWritable()
  {
    return writable;
  }

  public boolean isDeleteable()
  {
    return deleteable;
  }

}
