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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * A class to query Class instances.
 */
public abstract class ClassQueryDTO extends ComponentQueryDTO
{
  /**
   * 
   */
  private static final long serialVersionUID = 8175402262508846476L;

  /**
   * Map of query conditions where the key is the concatenated string of the
   * QueryCondition's attribute, condition, and value.
   */
  private Map<String, QueryCondition> conditions;
  
  /**
   * A list of order by conditions.
   */
  private List<OrderBy>               orderByList;
  
  /**
   * A map of classes where the keys are subclasses of the query type (and the
   * query type itself) and the value is a list of super classes for each type.
   * This map represents the entire hierarchy for which the query type is a
   * member, excluding siblings of the query type.
   */
  private Map<String, List<String>>   classes;

  /**
   * Constructor to set the query type.
   * 
   * @param type
   */
  protected ClassQueryDTO(String type)
  {
    super(type);
    
    classes = new HashMap<String, List<String>>();
    conditions = new HashMap<String, QueryCondition>();
    orderByList = new LinkedList<OrderBy>();
  }

  /**
   * Copies properties from the given componentQueryDTO into this one.
   * @param componentQueryDTO
   */
  public void copyProperties(ClassQueryDTO componentQueryDTO)
  {
    super.copyProperties(componentQueryDTO);
    this.classes = componentQueryDTO.classes;
    this.conditions = componentQueryDTO.conditions;
    this.orderByList = componentQueryDTO.orderByList;
  }

  /**
   * Returns a list of query conditions.
   * 
   * @return
   */
  public List<QueryCondition> getConditions()
  {
    List<QueryCondition> conditionList = new LinkedList<QueryCondition>(conditions.values());
    return conditionList;
  }

  /**
   * Returns true if this QueryDTO has the specified condition.
   * 
   * @attributeName
   * @condition
   * @value
   */
  public boolean hasCondition(String attributeName, String condition, String value)
  {
    String key = attributeName + condition + value;
    return conditions.containsKey(key);
  }
  
  /**
   * Clears all query conditions.
   */
  public void clearConditions()
  {
    conditions.clear();
  }
  
  /**
   * Clears the OrderBy objects.
   */
  public void clearOrderByList()
  {
    orderByList.clear();
  }

  
  /**
   * Adds a condition to this query.
   * 
   * @param attributeName
   * @param condition
   * @param value
   */
  public void addCondition(String attributeName, String condition, String value)
  {
    String key = attributeName + condition + value;
    conditions.put(key, new QueryCondition(attributeName, condition, value));
  }
  
  /**
   * Method to create an order by clause with an attribute and sort order. The order should
   * either be "asc" or "desc". Use the constants QueryConditions.ASC and
   * QueryConditions.DESC for the order param.
   * 
   * @param attribute
   * @param order
   */
  public void addOrderBy(String attribute, String order, String alias)
  {
    orderByList.add(new OrderBy(attribute, order, alias));
  }
  
    
  /**
   * Method for child classes to add OrderBy object themselves.
   * 
   * @param orderBy
   */
  protected void addOrderBy(OrderBy orderBy)
  {
    orderByList.add(orderBy);
  }

  
  /**
   * Adds a class (subclass/superclass of the query type and the query type
   * itself) and its superclasses.
   * 
   * @param subclass
   * @param superclasses
   */
  public void addClassType(String subclass, List<String> superclasses)
  {
    classes.put(subclass, superclasses);
  }

  /**
   * Returns a list of all class names (subclasses/superclass of the query type
   * and the query type itself)
   * 
   * @return
   */
  public List<String> getClassTypes()
  {
    return new LinkedList<String>(classes.keySet());
  }

  /**
   * Returns a list of all super class types of the specified class type.
   * 
   * @return
   */
  public List<String> getSuperClassTypes(String type)
  {
    return classes.get(type);
  }

  /**
   * Returns the list of order by clauses.
   * 
   * @return
   */
  public List<OrderBy> getOrderByList()
  {
    return orderByList;
  }

  /**
   * Inner class to specify order by
   */
  public class OrderBy implements Serializable
  {    
    /**
     * 
     */
    private static final long serialVersionUID = 1939971129340206916L;

    /**
     * The attribute to sort on
     */
    private String attribute;

    /**
     * The sort order 'asc' or 'desc'
     */
    private String order;
    
    /**
     * Array of attributes required to create the order by selectable
     */
    private String alias;

    /**
     * Constructor to set the order by attribute and sort order. The order
     * should either be "asc" or "desc". Use the constants QueryString.ASC
     * and QueryString.DESC for the order param.
     * 
     * @param attribute
     * @param order
     */
    protected OrderBy(String attribute, String order, String alias)
    {
      this.attribute = attribute;
      this.order = order;
      this.alias = alias;
    }
    
    /**
     * Returns the attribute for the order by clause.
     */
    public String getAttribute()
    {
      return attribute;
    }

    /**
     * Returns the sort order for the order by clause.
     * 
     * @return
     */
    public String getOrder()
    {
      return order;
    }
    
    public boolean isAscending()
    {
      return order.equals("asc");
    }
    
    public String getFullyQualifiedName()
    {
      return attribute;
    }
    
    public String getAlias()
    {
      return this.alias;
    }    
  }


  /**
   * Inner class to specify a single query condition.
   */
  public class QueryCondition implements Serializable
  {
    /**
     * Auto-generated serial id.
     */
    private static final long serialVersionUID = -2928896344450605433L;

    /**
     * The name of the attribute to use with the condition.
     */
    private String            attribute;

    /**
     * A query condition.
     */
    private String            condition;

    /**
     * The value of the condition.
     */
    private String            value;

    /**
     * Constructor.
     * 
     * @param condition
     * @param value
     */
    private QueryCondition(String attribute, String condition, String value)
    {
      this.attribute = attribute;
      this.condition = condition;
      this.value = value;
    }

    /**
     * Returns the name of the attribute to be used in the condition.
     * 
     * @return
     */
    public String getAttribute()
    {
      return attribute;
    }

    /**
     * Returns the type of condition.
     * 
     * @return
     */
    public String getCondition()
    {
      return condition;
    }

    /**
     * Returns the value of the condition.
     * 
     * @return
     */
    public String getConditionValue()
    {
      return value;
    }
  }
  
  /**
   * Method to create an order by clause with an attribute and sort order defined by a struct
   * through a struct attribute. The order should
   * either be "asc" or "desc". Use the constants QueryConditions.ASC and
   * QueryConditions.DESC for the order param.
   * 
   * @param attributeStruct
   * @param attribute
   * @param order
   */
  public void addStructOrderBy(String attributeStruct, String attribute, String order, String alias)
  {
    this.addOrderBy(new StructOrderBy(attributeStruct, attribute, order, alias));
  }
  
  /**
   * Inner class to specify an Order By with an attribute
   * on a struct.
   */
  public class StructOrderBy extends OrderBy
  {
    /**
     * 
     */
    private static final long serialVersionUID = -7015145099123285461L;
    
    private String attributeStruct;
    
    /**
     * Constructor to set the struct name, the attribute on the struct, and
     * the sort order.
     * 
     * @param struct
     * @param attribute
     * @param order
     */
    private StructOrderBy(String attributeStruct, String attribute, String order, String alias)
    {
      super(attribute, order, alias);
      
      this.attributeStruct = attributeStruct;
    }
    
    /**
     * Returns the name of attribute struct which contains an attribute
     * to order by.
     * 
     * @return
     */
    public String getAttributeStruct()
    {
      return attributeStruct;
    }
    
    @Override
    public String getFullyQualifiedName()
    {
      return attributeStruct + "-" + super.getFullyQualifiedName();
    }
  }
}

