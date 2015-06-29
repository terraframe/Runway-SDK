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
package com.runwaysdk.business;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.transport.attributes.AttributeDTO;

public abstract class ComponentQueryDTO implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -6316290153600890402L;

  /**
   * The type of the object to query.
   */
  private String                      type;

  /**
   * A list containing the result set.
   */
  private List<ComponentDTOIF>        resultSet;

  /**
   * The page size of the result set.
   */
  private int                       pageSize;

  /**
   * The current page number of the result set.
   */
  private int                       pageNumber;

  /**
   * The total result count of the query (excluding the influence of pageNumber
   * and pageSize.
   */
  private long                      count;

  /**
   * Flag denoting if a separate query should be executed to grab a count of
   * query results.
   */
  private boolean                   countEnabled;

  /**
   * A list of attributes defined by the type of this query.
   */
  private Map<String, AttributeDTO> definedAttributes;

  private String                    groovyQuery;
  
  private String                    jsonQuery;

  /**
   * Constructor to set the query type.
   * 
   * @param type
   */
  protected ComponentQueryDTO(String type)
  {
    this.type = type;
    this.resultSet = new LinkedList<ComponentDTOIF>();
    this.definedAttributes = new HashMap<String, AttributeDTO>();

    this.countEnabled = false;

    this.groovyQuery = "";
    this.jsonQuery = "";

    // 0 is the default value, which can be checked by calling code.
    // since a page size and page number of 0 makes no sense, a value of
    // 0 indicates that ALL results are to be requested.
    this.pageSize = 0;
    this.pageNumber = 0;
    this.count = 0;
  }

  /**
   * Copies properties from the given componentQueryDTO into this one.
   * 
   * @param componentQueryDTO
   */
  public void copyProperties(ComponentQueryDTO componentQueryDTO)
  {
    this.pageSize = componentQueryDTO.pageSize;
    this.pageNumber = componentQueryDTO.pageNumber;
    this.count = componentQueryDTO.count;
    this.countEnabled = componentQueryDTO.countEnabled;
    this.definedAttributes = componentQueryDTO.definedAttributes;
  }

  public String getGroovyQuery()
  {
    return this.groovyQuery;
  }

  public void setGroovyQuery(String groovyQuery)
  {
    this.groovyQuery = groovyQuery;
  }
  
  public String getJsonQuery()
  {
    return this.jsonQuery;
  }
  
  public void setJsonQuery(String jsonQuery)
  {
    this.jsonQuery = jsonQuery;
  }

  /**
   * Returns the type of the object to query.
   * 
   * @return String representation of the object's type.
   */
  public String getType()
  {
    return this.type;
  }

  /**
   * Returns the result set.
   * 
   * @return result set.
   */
  public List<? extends ComponentDTOIF> getResultSet()
  {
    return this.resultSet;
  }

  /**
   * clears the result set.
   */
  public void clearResultSet()
  {
    this.getResultSet().clear();
  }

  /**
   * Adds an item to the result set.
   * 
   * @param componentDTOIF
   */
  protected void addResultItem(ComponentDTOIF componentDTOIF)
  {
    this.resultSet.add(componentDTOIF);

    if (componentDTOIF instanceof ComponentDTO)
    {
      ComponentDTO componentDTO = (ComponentDTO) componentDTOIF;
      componentDTO.setDefinedAttributeMetadata(this.getDefinedAttributes());
    }
  }

  /**
   * Adds a result set to this QueryDTO. The specified result set will overwrite
   * the previous result set stored in this QueryDTO
   * 
   * @param resultSet
   */
  @SuppressWarnings("unchecked")
  protected void setResultSet(List<? extends ComponentDTOIF> resultSet)
  {
    this.resultSet = (List<ComponentDTOIF>) resultSet;
  }

  /**
   * Sets the page size of the result set. A value of 0 is the same as not
   * requesting a page size in the first place.
   * 
   * @param pageSize
   */
  public void setPageSize(Integer pageSize)
  {
    this.pageSize = pageSize;
  }

  /**
   * Returns the page size. A value of 0 means that the page size is disabled.
   * Therefore, all results are returned.
   * 
   * @return
   */
  public int getPageSize()
  {
    return this.pageSize;
  }

  /**
   * Sets the page number of the result set. A value of 0 is the same as not
   * requesting a page number in the first place.
   * 
   * @param pageNumber
   */
  public void setPageNumber(Integer pageNumber)
  {
    this.pageNumber = pageNumber;
  }

  /**
   * Returns the page number. A value of 0 means that the page number is
   * disabled. Therefore, all results are returned.
   * 
   * @return
   */
  public int getPageNumber()
  {
    return this.pageNumber;
  }
  
  /**
   * Sets the total result count of the query.
   * 
   * @param count
   */
  public void setCount(long count)
  {
    this.count = count;
  }

  /**
   * Returns the total result count of the query. The count is the total count
   * of the result set. It is not affected by the pageSize and pageNumber
   * settings.
   * 
   * @return
   */
  public long getCount()
  {
    return count;
  }

  /**
   * Sets the flag denoting if this QueryDTO should also retrieve the total
   * count result of the query. Note that setting enabled to true means a second
   * query will be performed to grab the count number. Also note that the count
   * number is independent of the result set affected by this.pageNumber and
   * this.pageSize.
   * 
   * @param enabled
   */
  public void setCountEnabled(boolean enabled)
  {
    this.countEnabled = enabled;
  }

  /**
   * Returns the flag denoting if this QueryDTO should grab the total result
   * count.
   * 
   * @return
   */
  public boolean isCountEnabled()
  {
    return countEnabled;
  }

  /**
   * Adds an attribute to this QueryDTO that the query type defines.
   * 
   * @param name
   * @param name
   * @return
   */
  protected void addAttribute(AttributeDTO attributeDTO)
  {
    this.definedAttributes.put(attributeDTO.getName(), attributeDTO);
  }

  /**
   * Returns an AttributeDTO that represents an attribute this query type
   * defines.
   * 
   * @return
   */
  public AttributeDTO getAttributeDTO(String name)
  {
    return this.definedAttributes.get(name);
  }

  /**
   * A list of attributes defined by the type of this query.
   */
  protected Map<String, AttributeDTO> getDefinedAttributes()
  {
    return this.definedAttributes;
  }

  /**
   * Returns a list of all attribute names that this query type defines.
   * 
   * @return String List of attribute names.
   */
  public List<String> getAttributeNames()
  {
    List<String> names = new LinkedList<String>();
    String[] namesArray = new String[this.definedAttributes.size()];
    this.definedAttributes.keySet().toArray(namesArray);

    for (String nameItem : namesArray)
    {
      names.add(nameItem);
    }

    Collections.sort(names);

    return names;
  }

  /**
   * Clears all defined attributes on this QueryDTO.
   * 
   */
  public void clearAttributes()
  {
    this.definedAttributes.clear();
  }

}
