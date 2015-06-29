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
package com.runwaysdk.controller.table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.ClassQueryDTO;
import com.runwaysdk.business.ClassQueryDTO.OrderBy;
import com.runwaysdk.constants.MdActionInfo;
import com.runwaysdk.constants.QueryString;

/**
 * @author jsmethie
 *
 */
public class Context
{
  /**
   * Action for the query method
   */
  private String              action;

  /**
   * Extra parameters used in the context
   */
  private Map<String, String> parameters;
  
  /**
   * QueryDTO which contains information about
   * the sort order, sort attribute, page number
   * and page size.
   */
  private ClassQueryDTO   query;
  
  public Context()
  {
    this.parameters = new HashMap<String, String>();
    this.query = null;
  }

  public void setAction(String action)
  {
    this.action = action;
  }
  
  public void addParameter(String name, String value)
  {
    parameters.put(name, value);
  }
    
  public void setQuery(ClassQueryDTO query)
  {
    this.query = query;
  }
  
  public Map<String, String> generateParameterMap(String attribute)
  {
    Map<String, String> params = new HashMap<String, String>();
    List<OrderBy> list = query.getOrderByList();
    boolean ascending = true;
    
    if(list.size() > 0)
    {
      ascending = list.get(list.size() - 1).isAscending();
    }
    
    params.put(MdActionInfo.SORT_ATTRIBUTE, attribute);
    params.put(MdActionInfo.IS_ASCENDING, String.valueOf(!ascending));
    params.put(MdActionInfo.PAGE_NUMBER, String.valueOf(QueryString.DEFAULT_PAGE_NUMBER));
    params.put(MdActionInfo.PAGE_SIZE, String.valueOf(query.getPageSize()));
    
    return params;
  }
  
  public Map<String, String> generateParameterMap(Integer pageNumber)
  {
    Map<String, String> params = new HashMap<String, String>();
    List<OrderBy> list = query.getOrderByList();
    
    if(list.size() > 0)
    {
      String orderAttribute = list.get(list.size() - 1).getAlias();
      boolean ascending = list.get(list.size() - 1).isAscending();

      params.put(MdActionInfo.SORT_ATTRIBUTE, orderAttribute);
      params.put(MdActionInfo.IS_ASCENDING, String.valueOf(ascending));
    }
    
    params.put(MdActionInfo.PAGE_NUMBER, String.valueOf(pageNumber));
    params.put(MdActionInfo.PAGE_SIZE, String.valueOf(query.getPageSize()));
    
    return params;
  }
  
  /**
   * Generates the context href link such that
   * the sort order is on the given attribute.
   * 
   * @param attribute Name of the attribute
   * @return HREF link
   */
  public String generateLink(String attribute)
  {
    StringBuffer buffer = new StringBuffer(action);    
    List<OrderBy> list = query.getOrderByList();
    boolean ascending = true;
    
    if(list.size() > 0)
    {
      ascending = list.get(list.size() - 1).isAscending();
    }
    
    //Set the query parameter
    buffer.append("&"+MdActionInfo.SORT_ATTRIBUTE+"=" + attribute);
    buffer.append("&"+MdActionInfo.IS_ASCENDING+"=" + !ascending);
    buffer.append("&"+MdActionInfo.PAGE_NUMBER+"=" + QueryString.DEFAULT_PAGE_NUMBER);
    buffer.append("&"+MdActionInfo.PAGE_SIZE+"=" + query.getPageSize());
        
    //Add all additional context parameters
    for(String key : parameters.keySet())
    {
      buffer.append("&" + key + "=" + parameters.get(key));
    }    
        
    //For a valid html link the first & must be replaced with ?
    return buffer.toString().replaceFirst("&", "?");
  }
  
  /**
   * Generates a context href link with the given page number 
   * @param pageNumber
   * @return
   */
  public String generateLink(Integer pageNumber)
  {
    //Use the last OrderBy object in a query to determine
    //the sort attribute and sort order.
    List<OrderBy> list = query.getOrderByList();
    StringBuffer buffer = new StringBuffer(action);    
    
    if(list.size() > 0)
    {
      String orderAttribute = list.get(list.size() - 1).getAlias();
      boolean ascending = list.get(list.size() - 1).isAscending();

      buffer.append("&"+MdActionInfo.SORT_ATTRIBUTE+"=" + orderAttribute);
      buffer.append("&"+MdActionInfo.IS_ASCENDING+"=" + ascending);
    }
            
    //Set the query parameters.  All context links must have these parameters
    buffer.append("&"+MdActionInfo.PAGE_NUMBER+"=" + pageNumber);
    buffer.append("&"+MdActionInfo.PAGE_SIZE+"=" + query.getPageSize());
        
    //Set all additional context parameters
    for(String key : parameters.keySet())
    {
      buffer.append("&" + key + "=" + parameters.get(key));
    }    

    //For a valid html link the first & must be replaced with ?
    return buffer.toString().replaceFirst("&", "?");
  }

  
  public String getAction()
  {
    return action;
  }
}
