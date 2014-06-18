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
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.Set;
import java.util.TreeSet;

import org.xml.sax.Attributes;

public class EntitySearchCriteria implements SearchCriteriaIF
{
  private String type;
  
  private String key;
  
  private Set<String> tags;
  
  public EntitySearchCriteria(String type, String key, String... tags)
  {
    this.type = type;
    this.key = key;
    this.tags = new TreeSet<String>();
    
    for(String tag : tags)
    {
      this.tags.add(tag);
    }
  }

  public boolean check(String tagName, Attributes attributes)
  {
    // Determine if this is a tag to check
    String _keys = attributes.getValue(XMLTags.KEY_ATTRIBUTE);
    String _types = attributes.getValue(XMLTags.TYPE_ATTRIBUTE);

    return (this.tags.contains(tagName) && key.equals(_keys) && type.equals(_types));
  }

  public String criteria()
  {
    return this.type + " - " + this.key;
  }
    
  @Override
  public boolean equals(Object obj)
  {
    if(obj instanceof EntitySearchCriteria)
    {
      EntitySearchCriteria _o = (EntitySearchCriteria) obj;

      return _o.type.equals(this.type) && _o.key.equals(this.key);
    }
    
    return false;
  }

  public int compareTo(SearchCriteriaIF o)
  {
    if(!(o instanceof EntitySearchCriteria))
    {
      return -1;
    }
    
    EntitySearchCriteria _o = (EntitySearchCriteria) o;
    
    if(_o.type.equals(this.type))
    {
      return _o.key.compareTo(this.key) ;
    }
    
    return _o.type.compareTo(this.type);
  }

}
