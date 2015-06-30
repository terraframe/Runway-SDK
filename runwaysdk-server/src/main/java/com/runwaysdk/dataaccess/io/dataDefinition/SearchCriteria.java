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
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.Set;
import java.util.TreeSet;

import org.xml.sax.Attributes;

public class SearchCriteria implements SearchCriteriaIF
{
  /**
   * A list of the xml tags in which to search
   */
  private Set<String> tags;

  /**
   * The name of the attribute to search
   */
  private String      attribute;

  /**
   * The key of to search for
   */
  private String      key;

  public SearchCriteria(String[] tags, String attribute, String key)
  {
    this.tags = new TreeSet<String>();
    this.attribute = attribute;
    this.key = key;

    for (String tag : tags)
    {
      this.tags.add(tag);
    }
  }

  public boolean check(String tagName, Attributes attributes)
  {
    // Determine if this is a tag to check
    String value = attributes.getValue(attribute);

    return (tags.contains(tagName) && key.equals(value));
  }

  public String criteria()
  {
    return key;
  }
  
  @Override
  public boolean equals(Object obj)
  {
    if(obj instanceof SearchCriteria)
    {
      SearchCriteria _o = (SearchCriteria) obj;

      return _o.key.equals(this.key);
    }
    
    return false;
  }

  public int compareTo(SearchCriteriaIF o)
  {
    if(!(o instanceof SearchCriteria))
    {
      return -1;
    }
    
    SearchCriteria _o = (SearchCriteria) o;

    return _o.key.compareTo(this.key);
  }
}
