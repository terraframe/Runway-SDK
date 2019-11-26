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
package com.runwaysdk.dataaccess.graph;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.graph.GraphClassInfo;
import com.runwaysdk.dataaccess.AttributeDoesNotExistException;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.ComponentDAO;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdGraphDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeException;
import com.runwaysdk.dataaccess.attributes.AttributeTypeException;
import com.runwaysdk.dataaccess.graph.attributes.Attribute;
import com.runwaysdk.dataaccess.graph.attributes.AttributeEmbedded;
import com.runwaysdk.dataaccess.graph.attributes.AttributeEnumeration;
import com.runwaysdk.dataaccess.graph.attributes.AttributeFactory;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTime;
import com.runwaysdk.dataaccess.metadata.graph.MdGraphClassDAO;

public abstract class GraphObjectDAO extends ComponentDAO implements GraphObjectDAOIF
{
  /**
   * 
   */
  private static final long        serialVersionUID      = 765877467852678882L;

  /**
   * Map of Attribute objects the component has. They are of a name-value pair
   * relation. <br/>
   * <b>invariant</b> attributeMap != null
   */
  protected Map<String, Attribute> attributeMap;

  /**
   * Id used for AttributeProblems (not messages). New instances that fail will
   * have a different OID on the client.
   */
  private String                   problemNotificationId = "";

  /**
   * Indicates if this is a new instance. If it is new, then the records that
   * represent this component have not been created.
   */
  private boolean                  isNew                 = false;

  private boolean                  isAppliedToDB         = false;

  private MdGraphClassDAOIF        mdGraphClassDAOIF;

  private Object                   rid;

  /**
   * The default constructor, does not set any attributes
   */
  public GraphObjectDAO()
  {
    super();
  }

  /**
   * Constructs a {@link GraphObjectDAO} from the given hashtable of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> mdGraphClassDAO != null
   * 
   * @param attributeMap
   * @param mdGraphClassDAO
   */
  public GraphObjectDAO(Map<String, Attribute> attributeMap, MdGraphClassDAOIF mdGraphClassDAOIF)
  {
    super(mdGraphClassDAOIF.definesType());
    this.attributeMap = attributeMap;
    this.mdGraphClassDAOIF = mdGraphClassDAOIF;
    this.linkAttributes();
  }

  /**
   * Returns a {@link MdGraphClassDAOIF} that defines this Component's class.
   *
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   *
   * @return a {@link MdGraphClassDAOIF} that defines this Component's class.
   */
  @Override
  public MdGraphClassDAOIF getMdClassDAO()
  {
    return mdGraphClassDAOIF;
  }

  /**
   * Returns a boolean that indicates if this is a new instance (i.e. has not
   * been committed to the database).
   *
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   *
   * @return a boolean that indicates if this is a new instance
   */
  @Override
  public boolean isNew()
  {
    return this.isNew;
  }

  /**
   * Do not call this method unless you know what you are doing. Sets the new
   * state of the object.
   *
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   */
  @Override
  public void setIsNew(boolean isNew)
  {
    this.isNew = isNew;
  }

  public void setRID(Object rid)
  {
    this.rid = rid;
  }

  public Object getRID()
  {
    return rid;
  }

  /**
   * Gets the sequence number of the entity object
   * 
   * @return The sequence number of the entity
   */
  public Long getSequence()
  {
    return (Long) this.getAttributeIF(ElementInfo.SEQUENCE).getObjectValue();
  }

  /**
   * Returns the Id used for AttributeNotifications. New instances that fail
   * will have a different OID on the client.
   * 
   * @return notification oid.
   */
  public String getProblemNotificationId()
  {
    if (this.problemNotificationId.equals(""))
    {
      return this.getOid();
    }
    else
    {
      return this.problemNotificationId;
    }
  }

  /**
   * Sets the OID used for AttributeProblems. Should be called on new instances,
   * since the DTO on the client will have a different OID than the one
   * automatically created for this object.
   * 
   * @param problemNotificationId
   */
  public void setProblemNotificationId(String problemNotificationId)
  {
    this.problemNotificationId = problemNotificationId;
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
   * Not to be called from the constructor.
   */
  public void setTypeName(String graphType)
  {
    this.componentType = graphType;
  }

  /**
   * Returns true if this instance has been written to the database. It does not
   * indicate if it has been committed to the database.
   * 
   * @return true if this instance has been written to the database. It does not
   *         indicate if it has been committed to the database.
   */
  public boolean isAppliedToDB()
  {
    return this.isAppliedToDB;
  }

  /**
   * If true then this instance has been written to the database.
   * 
   * @param appliedToDB
   */
  public void setAppliedToDB(boolean appliedToDB)
  {
    this.isAppliedToDB = appliedToDB;
  }

  /**
   * Returns the Attribute object with the given name.
   * 
   * <br/>
   * <b>Precondition:</b> name != null <br/>
   * <b>Precondition:</b> !name.trim().equals("") <br/>
   * <b>Precondition:</b> Attribute name is valid for the class of this
   * Component <br/>
   * <b>Postcondition:</b> return value != null
   * 
   * @param name
   *          name of the attribute
   * @return Attribute object with the given name
   * @throws DataAccessException
   *           if the attribute with the given name does not exist for the class
   *           of the Component
   */
  @Override
  public AttributeIF getAttributeIF(String name)
  {
    AttributeIF returnAttribute = (AttributeIF) this.attributeMap.get(name);

    if (returnAttribute == null)
    {
      String error = "An attribute named [" + name + "] does not exist on type [" + this.getType() + "]";
      throw new AttributeDoesNotExistException(error, name, this.getMdClassDAO());
    }

    return returnAttribute;
  }

  /**
   * Returns true if the component has an attribute with the given name, false
   * otherwise.
   * 
   * @param name
   * @return true if the component has an attribute with the given name, false
   *         otherwise.
   */
  @Override
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
   * Returns an array of the Attributes for this Component.<br>
   * <br>
   * <b>Precondition:</b> true<br>
   * <b>Postcondition:</b> array.length = attributeMap.size();
   */
  @Override
  public AttributeIF[] getAttributeArrayIF()
  {
    Collection<Attribute> values = this.attributeMap.values();
    return this.attributeMap.values().toArray(new AttributeIF[values.size()]);
    // Heads up: clean up
    // AttributeIF[] array = new Attribute[this.attributeMap.size()];
    // int index = 0;
    //
    // for (AttributeIF attribute : this.attributeMap.values())
    // {
    // array[index] = attribute;
    // index++;
    // }
    //
    // return array;
  }

  /**
   * Returns an array of attribute objects.
   * 
   * @return array of attribute objects.
   */
  public Attribute[] getAttributeArray()
  {
    Collection<Attribute> values = this.attributeMap.values();

    return this.attributeMap.values().toArray(new Attribute[values.size()]);
  }

  /**
   * @see GraphObjectDAOIF#getEmbeddedComponentDAO(String)
   */
  public ComponentDAO getEmbeddedComponentDAO(String attributeName)
  {
    Attribute attribute = this.getAttribute(attributeName);

    if (attribute instanceof AttributeEmbedded)
    {
      AttributeEmbedded attributeEmbedded = (AttributeEmbedded) attribute;

      return attributeEmbedded.getObjectValue();
    }
    else
    {
      return null;
    }
  }

  /**
   * @see GraphObjectDAOIF#getEmbeddedComponentDAO(String)
   */
  public ComponentDAO getEmbeddedComponentDAO(String attributeName, Date date)
  {
    Attribute attribute = this.getAttribute(attributeName);

    if (attribute instanceof AttributeEmbedded)
    {
      AttributeEmbedded attributeEmbedded = (AttributeEmbedded) attribute;

      return (ComponentDAO) attributeEmbedded.getObjectValue(date);
    }
    else
    {
      return null;
    }
  }

  /**
   * Returns a {@link MdGraphDAOIF} that defines this object's class.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   * 
   * @return a {@link MdGraphDAOIF} that defines this object's class.
   */
  public MdGraphClassDAOIF getMdGraphClassDAO()
  {
    return MdGraphClassDAO.getMdGraphClassDAO(this.getType());
  }

  /**
   * Returns a LinkedList of MdAttributeIF objects representing metadata for
   * each attribute of this object's class.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   * 
   * @return a LinkedList of MdAttributeIF objects representing metadata for
   *         each attribute of this object's class.
   */
  public List<? extends MdAttributeDAOIF> getMdAttributeDAOs()
  {
    return this.getMdClassDAO().getAllDefinedMdAttributes();
  }

  @Override
  public MdAttributeConcreteDAOIF getMdAttributeDAO(String name)
  {
    return (MdAttributeConcreteDAOIF) super.getMdAttributeDAO(name);
  }

  /**
   * Returns the attribute object of the given name.
   * 
   * @param name
   * @return
   */
  public Attribute getAttribute(String name)
  {
    return (Attribute) this.getAttributeIF(name);
  }

  /**
   * Validates the attribute with the given name. If the attribute is not valid,
   * then an AttributeException exception is thrown.
   * 
   * <br/>
   * <b>Precondition:</b> name != null <br/>
   * <b>Precondition:</b> !name.trim().equals("") <br/>
   * <b>Precondition:</b> An attribute of the given name exists for instances of
   * this class.
   * 
   * @param name
   *          name of the attribute
   * @throws AttributeException
   *           if the attribute is not valid.
   */
  public void validateAttribute(String name)
  {
    Attribute attribute = this.getAttribute(name);

    attribute.validate(attribute.getValue());
  }

  /**
   * Some attributes store objects instead of strings.
   * 
   * @param name
   * @return object stored on the attribute.
   */
  public Object getObjectValue(String name, Date date)
  {
    Attribute attribute = (Attribute) this.getAttributeIF(name);

    return attribute.getObjectValue(date);
  }

  /**
   * Some attributes store objects instead of strings.
   * 
   * @param name
   * @return object stored on the attribute.
   */
  public List<ValueOverTime> getValuesOverTime(String name)
  {
    Attribute attribute = (Attribute) this.getAttributeIF(name);

    return attribute.getValuesOverTime().asList();
  }

  public void setValue(String name, Object value, Date startDate, Date endDate)
  {
    if (value instanceof String)
    {
      String stringValue = (String) value;

      if (name.equals(GraphClassInfo.TYPE))
      {
        this.componentType = stringValue;
      }
    }

    Attribute attribute = this.getAttribute(name);

    attribute.setValue(value, startDate, endDate);
  }

  /**
   * Sets the attribute of the given name with the given value.
   * 
   * <br/>
   * <b>Precondition:</b> name != null <br/>
   * <b>Precondition:</b> !name.trim().equals("") <br/>
   * <b>Precondition:</b> value != null <br/>
   * <b>Precondition:</b> Attribute name is valid for this EntityDAO's class
   * <br/>
   * <b>Postcondition:</b> Attribute is set with the given value
   * 
   * <br/>
   * <b>modifies:</b> this.componentType if the attribute being modified is the
   * name of the class.
   * 
   * @param name
   *          name of the attribute
   * @param value
   *          value to assign to the given attribute
   * @throws DataAccessException
   *           if the attribute with the given name does not exist for the class
   *           of the {@link GraphObjectDAO}
   */
  public void setValue(String name, Object value)
  {
    if (value instanceof String)
    {
      String stringValue = (String) value;

      if (name.equals(GraphClassInfo.TYPE))
      {
        this.componentType = stringValue;
      }
    }

    Attribute attribute = this.getAttribute(name);

    attribute.setValue(value);
  }

  /**
   * Adds an item to an enumerated attribute. If the attribute does not allow
   * multiplicity, the <code>value</code> replaces the previous item.
   * 
   * @param name
   *          Name of the set attribute
   * @param value
   *          Value to be added to the attribute
   */
  public void addItem(String name, String value)
  {
    try
    {
      AttributeEnumeration attrSet = (AttributeEnumeration) this.getAttribute(name);
      attrSet.addItem(value);
    }
    catch (ClassCastException e)
    {
      String error = "Attribute [" + name + "] on type [" + getType() + "] is not a set attribute";
      throw new AttributeTypeException(error);
    }
  }

  /**
   * Replaces the items of a set attribute. If the attribute does not allow
   * multiplicity, then the {@code values} collection must contain only one
   * item.
   * 
   * @param name
   *          Name of the set attribute
   * @param values
   *          Collection of set item ids
   */
  public void replaceItems(String name, Collection<String> values)
  {
    try
    {
      AttributeEnumeration attrSet = (AttributeEnumeration) this.getAttribute(name);
      attrSet.replaceItems(values);
    }
    catch (ClassCastException e)
    {
      String error = "Attribute [" + name + "] on type [" + getType() + "] is not a set attribute";
      throw new AttributeTypeException(error);
    }
  }

  /**
   * Deletes an item from a set Attribute.
   * 
   * @param name
   *          Name of the set attribute
   * @param value
   *          Value to be removed from the attribute
   */
  public void removeItem(String name, String value)
  {
    try
    {
      AttributeEnumeration attrSet = (AttributeEnumeration) this.getAttribute(name);
      attrSet.removeItem(value);
    }
    catch (ClassCastException e)
    {
      String error = "Attribute [" + name + "] on type [" + getType() + "] is not an enumerated attribute";
      throw new AttributeTypeException(error);
    }
  }

  /**
   * Deletes an item from a set Attribute.
   * 
   * @param name
   *          Name of the set attribute
   * @param value
   *          Value to be removed from the attribute
   */
  public void clearItems(String name)
  {
    try
    {
      AttributeEnumeration attrSet = (AttributeEnumeration) this.getAttribute(name);
      attrSet.clearItems();
    }
    catch (ClassCastException e)
    {
      String error = "Attribute [" + name + "] on type [" + getType() + "] is not an enumerated attribute";
      throw new AttributeTypeException(error);
    }
  }

  /**
   * Finalizes attributes, such as required attributes.
   * 
   * @return oid of the object.
   */
  public String apply()
  {
    return this.save();
  }

  public void delete()
  {
    GraphRequest request = GraphDBService.getInstance().getGraphDBRequest();

    if (!this.isNew)
    {
      GraphDBService.getInstance().delete(request, this);
    }
  }

  /**
   * This is a hook method for aspects so that the transient object apply can be
   * managed by transaction management.
   * 
   * @return oid of the object.
   */
  private String save()
  {
    List<? extends MdAttributeDAOIF> mdAttributeList = this.getMdClassDAO().definesAttributes();

    for (MdAttributeDAOIF mdAttribute : mdAttributeList)
    {
      String fieldName = mdAttribute.definesAttribute();

      if (this.hasAttribute(fieldName))
      {
        Attribute attribute = this.getAttribute(fieldName);
        attribute.validateRequired(attribute.getObjectValue(), mdAttribute);
      }
    }

    GraphRequest request = GraphDBService.getInstance().getGraphDBRequest();

    if (this.isNew && !this.isAppliedToDB)
    {
      GraphDBService.getInstance().insert(request, this);
      this.setAppliedToDB(true);
    }
    else
    {
      GraphDBService.getInstance().update(request, this);
    }

    return this.getOid();
  }

  /**
   * Sets isNew = false and sets all attributes to isModified = false.
   * 
   */
  public void setCommitState()
  {
    this.setIsNew(false);

    this.problemNotificationId = "";

    Attribute[] attributeArray = this.getAttributeArray();

    for (int i = 0; i < attributeArray.length; i++)
    {
      attributeArray[i].setCommitState();
    }
  }

  /**
   * Returns a <code>Map</code> of Attribute objects for the given
   * {@link MdGraphClassDAOIF}, not including attributes that are inherited from
   * other classes. If a default value is defined for the attribute, it is
   * assigned to the attribute.
   * 
   * <br/>
   * <b>Precondition:</b> mdGraphClassDAOIF != null
   * 
   * @param mdGraphClassDAOIF
   *          Create Attribute objects for attributes defined by the given
   *          entity.
   * @return <code>Map</code> of Attribute objects for the given entity, not
   *         including attributes that are inherited from other entities
   */
  protected static Map<String, Attribute> createRecordsForEntity(MdGraphClassDAOIF mdGraphClassDAOIF)
  {
    Map<String, Attribute> attributeMap = new HashMap<String, Attribute>();

    // get the all attribute meta data for this class
    List<? extends MdAttributeConcreteDAOIF> mdAttributeList = mdGraphClassDAOIF.definesAttributes();

    for (MdAttributeConcreteDAOIF mdAttribute : mdAttributeList)
    {
      Attribute attribute = GraphObjectDAO.createAttributeForGraphObject(mdGraphClassDAOIF, mdAttribute);
      attributeMap.put(attribute.getName(), attribute);
    }

    return attributeMap;
  }

  public static Attribute createAttributeForGraphObject(MdGraphClassDAOIF mdGraphClassDAOIF, MdAttributeConcreteDAOIF mdAttribute)
  {
    Attribute attribute;
    // assign a default value (if any) as defined in the meta data
    // String attrDefaultValue = mdAttribute.getAttributeInstanceDefaultValue();

    // Check for sessionDefaultValue

    // // New enumeration attributes need a unique oid so they can map to the
    // // MdEnumerationIF.getDatabaseTableName() table
    // if (mdAttribute instanceof MdAttributeEnumerationDAOIF)
    // {
    // String setOid = ServerIDGenerator.nextID();
    // attribute = AttributeFactory.createAttribute(mdAttribute.getKey(),
    // mdAttribute.getType(), attrName, mdEntityDAOIF.definesType(), setOid);
    //
    // AttributeEnumeration attributeEnumeration = (AttributeEnumeration)
    // attribute;
    //
    // if (!attrDefaultValue.equals(""))
    // {
    // attributeEnumeration.setDefaultValue(attrDefaultValue);
    // }
    if (mdAttribute instanceof MdAttributeUUIDDAOIF)
    {
      attribute = AttributeFactory.createAttribute(mdAttribute, mdGraphClassDAOIF.definesType());
      attribute.setValueInternal(UUID.randomUUID().toString());
    }
    else
    {
      attribute = AttributeFactory.createAttribute(mdAttribute, mdGraphClassDAOIF.definesType());
      // attribute.setValueInternal(attrDefaultValue);
    }
    return attribute;
  }
}
