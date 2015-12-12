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
package com.runwaysdk.dataaccess;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.dataaccess.attributes.value.Attribute;

public class ValueObject extends ComponentDAO
{

  /**
   *
   */
  private static final long serialVersionUID = -3912917173993182938L;

  private String id;

  /**
   * Map of Attribute objects the component has. They are of a name-value pair relation.
   * <br/><b>invariant</b> attributeMap != null
   */
  protected Map<String, Attribute> attributeMap;


  /**
   * Indicates if this is a new instance. If it is new, then the records that
   * represent this component have not been created.
   */
  private boolean isNew = false;
  
  /**
   * Constructs a TransientDAO from the given hashtable of Attributes.
   *
   * <br/><b>Precondition:</b> attributeMap != null
   * <br/><b>Precondition:</b> classType != null
   *
   * @param attributeMap
   * @param transientType
   */
  public ValueObject(Map<String, Attribute> attributeMap, String transientType, String id)
  {
    super(transientType);
    this.attributeMap = attributeMap;
    this.id = id;
    this.linkAttributes();
  }

  /**
   * Map of Attribute objects the component has. They are of a name-value pair relation.
   * @return Attribute objects the component has. They are of a name-value pair relation.
   */
  public Map<String, Attribute> getAttributeMap()
  {
    return this.attributeMap;
  }

  /**
   * Returns a boolean that indicates if this is a new instance (i.e. has not been committed to the database).
   *
   * <br/><b>Precondition:</b> true <br/><b>Postcondition:</b> true
   *
   * @return a boolean that indicates if this is a new instance
   */
  public boolean isNew()
  {
    return this.isNew;
  }

  /**
   * Do not call this method unless you know what you are doing.  Sets the new state of the object.
   *
   * <br/><b>Precondition:</b> true <br/><b>Postcondition:</b> true
   */
  public void setIsNew(boolean isNew)
  {
    this.isNew = isNew;
  }
  
  /**
   * Iterates over the map of Attributes, setting <b>this</b> as their
   * containing Component.<br>
   * <br>
   * <b>Precondition:</b> attributeMap != null<br>
   * <b>Postcondition:</b> attributeMap.get(value).getContainingComponent =
   * <b>this</b>
   */
  private void linkAttributes()
  {
    for (Attribute attribute : this.attributeMap.values())
    {
      attribute.setContainingComponent(this);
    }
    return;
  }

  /**
   * Returns the Id used for AttributeProblems (not messages).  New instances that fail will have a
   * different ID on the client.
   *
   * @return problem notification id.
   */
  public String getProblemNotificationId()
  {
    return this.getId();
  }

  /**
   * Returns the ID of this Component.
   *
   * <br/><b>Precondition:</b> true <br/><b>Postcondition:</b> The state of
   * the InfoObject does not change <br/><b>Postcondition:</b> return value !=
   * null
   *
   * @return The ID of this Component.
   */
  public String getId()
  {
    return this.id;
  }

  /**
   * Returns true if the component has an attribute with the given name, false otherwise.
   * @param name
   * @return true if the component has an attribute with the given name, false otherwise.
   */
  public boolean hasAttribute(String name)
  {
    if (this.attributeMap.get(name) != null)
    {
      return true;
    }
    else
    {
      return false;
    }
  }


  /**
   * Returns the Attribute object with the given name.
   *
   * <br/><b>Precondition:</b> name != null
   * <br/><b>Precondition:</b> !name.trim().equals("")
   * <br/><b>Precondition:</b> Attribute name is valid for the class of this Component
   * <br/><b>Postcondition:</b> return value != null
   *
   * @param name
   *          name of the attribute
   * @return Attribute object with the given name
   * @throws DataAccessException
   *           if the attribute with the given name does not exist for the class
   *           of the Component
   */
  public AttributeIF getAttributeIF(String name)
  {
    AttributeIF returnAttribute = (AttributeIF) this.attributeMap.get(name);

    if (returnAttribute == null)
    {
      String error = "An attribute named [" + name + "] does not exist on the ["+this.getClass().getName()+"]";
      throw new AttributeDoesNotExistException(error, name, null);
    }

    return returnAttribute;
  }

  /**
   * Returns an array of the Attributes for this Component.<br>
   * <br>
   * <b>Precondition:</b> true<br>
   * <b>Postcondition:</b> array.length = attributeMap.size();
   */
  public AttributeIF[] getAttributeArrayIF()
  {
    AttributeIF[] array = new Attribute[this.attributeMap.size()];
    int index = 0;

    for (AttributeIF attribute : this.attributeMap.values())
    {
      array[index] = attribute;
      index++;
    }

    return array;
  }

  /**
   * Returns a LinkedList of MdAttributeIF objects representing metadata for each attribute
   * of this object's class.
   *
   * <br/><b>Precondition:</b> true <br/><b>Postcondition:</b> true
   *
   * @return a LinkedList of MdAttributeIF objects representing metadata for each attribute
   * of this object's class.
   */
  public List<? extends MdAttributeDAOIF> getMdAttributeDAOs()
  {
    List<MdAttributeDAOIF> mdAttributeIFList = new LinkedList<MdAttributeDAOIF>();

    for (AttributeIF attribute : this.getAttributeArrayIF())
    {
      mdAttributeIFList.add(attribute.getMdAttribute());
    }

    return mdAttributeIFList;
  }

  /**
   * Returns a map of all MdEntities that were involved in this query.
   * @return map of all MdEntities that were involved in this query.
   */
  public Map<String, MdEntityDAOIF> getDefiningMdEntities()
  {
    Map<String, MdEntityDAOIF> mdEntityIFset = new HashMap<String, MdEntityDAOIF>();

    for (AttributeIF attribute : this.getAttributeArrayIF())
    {
      MdEntityDAOIF mdEntityIF = (MdEntityDAOIF)attribute.getMdAttribute().definedByClass();

      // Pass through SQL objects do not have type metadata
      if (mdEntityIF != null)
      {
        mdEntityIFset.put(mdEntityIF.definesType(), mdEntityIF);
      }
    }

    return mdEntityIFset;
  }

}
