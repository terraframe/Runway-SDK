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
package com.runwaysdk.business.graph;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.BusinessEnumeration;
import com.runwaysdk.business.Entity;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.dataaccess.ComponentDAO;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeException;
import com.runwaysdk.dataaccess.graph.GraphObjectDAO;
import com.runwaysdk.dataaccess.graph.GraphObjectDAOIF;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.attributes.AttributeEmbedded;
import com.runwaysdk.dataaccess.graph.attributes.AttributeEnumeration;
import com.runwaysdk.dataaccess.graph.attributes.AttributeLocalEmbedded;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.session.Session;

public abstract class GraphObject implements Mutable
{

  public final static String CLASS = GraphObject.class.getName();

  /**
   * All interaction with the core is delegated through this object. This should
   * NOT be accessed outside of this class.
   */
  GraphObjectDAO             graphObjectDAO;

  /**
   * Blank constructor can be used for new or existing instances. It is
   * <b>critical</b> that subclasses call
   * {@link GraphObject#setDataGraphObject(GraphObjectDAO)} to correclty
   * initialize the graphObject.
   */
  GraphObject()
  {
  }

  /**
   * Default visibility is on purpose: we don't want all generated classes to
   * see this method.
   * 
   * @param graphObjectDAO
   */
  void setGraphObjectDAO(GraphObjectDAO graphObjectDAO)
  {
    this.graphObjectDAO = graphObjectDAO;
  }

  /**
   * Default visibility is on purpose: we don't want all generated classes to
   * see this method.
   * 
   * @return the GraphObjectDAO
   */
  public GraphObjectDAO getGraphObjectDAO()
  {
    return graphObjectDAO;
  }

  public Object getRID()
  {
    return this.graphObjectDAO.getRID();
  }

  @Override
  public String toString()
  {
    return this.getMdClass().toString();
  }

  /**
   * Indicates if this is a new instance. If it is new, then the records that
   * represent this ComponentIF have not been created.
   */
  public boolean isNew()
  {
    return graphObjectDAO.isNew();
  }

  /**
   * Returns a MdClassIF that defines this Component's class.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   * 
   * @return a MdClassIF that defines this Component's class.
   */
  public MdClassDAOIF getMdClass()
  {
    return MdClassDAO.getMdClassDAO(this.getType());
  }

  /**
   * Returns a BusinessDAO representing the attribute metadata of the attribute
   * with the given name.
   * 
   * <br/>
   * <b>Precondition:</b> name != null <br/>
   * <b>Precondition:</b> !name.trim().equals("") <br/>
   * <b>Precondition:</b> name is a valid attribute with respect the this
   * Component's class.
   * 
   * @param name
   *          of the attribute.
   * @return BusinessDAO representing the attribute metadata of the attribute
   *         with the given name
   */
  public MdAttributeConcreteDAOIF getMdAttributeDAO(String name)
  {
    return this.graphObjectDAO.getMdAttributeDAO(name);
  }

  /**
   * Returns a LinkedList of BusinessDAOs representing metadata for each
   * attribute defined for this object's class.
   * 
   * <br/>
   * <b>Precondition:</b> true
   * 
   * @return List of BusinessDAOs representing metadata for each attribute
   *         defined for this object's class.
   */
  public List<? extends MdAttributeDAOIF> getMdAttributeDAOs()
  {
    return this.graphObjectDAO.getMdAttributeDAOs();
  }

  /**
   * Returns true if the object has an attribute with the given name, false
   * otherwise. It is case sensitive.
   * 
   * @param name
   *          name of the attribute.
   * @return true if the object has an attribute with the given name, false
   *         otherwise. It is case sensitive.
   */
  public boolean hasAttribute(String name)
  {
    return this.graphObjectDAO.hasAttribute(name);
  }

  /**
   * Some attributes store objects instead of strings.
   * 
   * @param name
   * @return object stored on the attribute.
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getObjectValue(String name)
  {
    return (T) this.graphObjectDAO.getObjectValue(name);
  }

  /**
   * Some attributes store objects instead of strings.
   * 
   * @param name
   * @return object stored on the attribute.
   */
  @SuppressWarnings("unchecked")
  public <T> T getObjectValue(String name, Date date)
  {
    return (T) this.graphObjectDAO.getObjectValue(name, date);
  }

  /**
   * @see GraphObjectDAOIF#getEmbeddedComponent(String)
   */
  public GraphObject getEmbeddedComponent(String attributeName)
  {
    ComponentDAO componentDAO = this.graphObjectDAO.getEmbeddedComponentDAO(attributeName);

    if (componentDAO instanceof VertexObjectDAO)
    {
      return VertexObject.instantiate((VertexObjectDAO) componentDAO);
    }
    else
    {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * @see GraphObjectDAOIF#getEmbeddedComponent(String)
   */
  public GraphObject getEmbeddedComponent(String attributeName, Date date)
  {
    ComponentDAO componentDAO = this.graphObjectDAO.getEmbeddedComponentDAO(attributeName, date);

    if (componentDAO instanceof VertexObjectDAO)
    {
      return VertexObject.instantiate((VertexObjectDAO) componentDAO);
    }
    else
    {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * Returns a list of selected values for the given enumerated attribute. The
   * declared type of the list is BusinessEnumeration, but each entry is
   * instantiated through reflection, which allows for accurate actual types.
   * 
   * @param name
   *          Name of the attribute enumeration
   * @return List of typesafe enumeration options that are selected
   */
  public List<? extends BusinessEnumeration> getEnumValues(String name)
  {
    AttributeEnumeration attribute = (AttributeEnumeration) graphObjectDAO.getAttribute(name);
    Set<String> ids = attribute.getObjectValue();

    MdAttributeConcreteDAOIF mdAttribute = attribute.getMdAttributeConcrete();

    return Entity.loadEnumValues(ids, mdAttribute);
  }

  /**
   * Validates the attribute with the given name. If the attribute is not valid,
   * then an AttributeException exception is thrown.
   * 
   * <br/>
   * <b>Precondition:</b> name != null <br/>
   * <b>Precondition:</b> !name.trim().equals("") <br/>
   * <b>Precondition:</b> An attribute of the given name exists for instances of
   * this class
   * 
   * @param name
   *          name of the attribute
   * @throws AttributeException
   *           if the attribute is not valid.
   */
  public void validateAttribute(String name)
  {
    this.graphObjectDAO.validateAttribute(name);
  }

  /**
   * A generic, type-unsafe setter that takes the attribute name a and value as
   * an Object.
   * 
   * @param name
   *          String name of the attribute
   * @param value
   *          String representation of the value
   */
  public void setValue(String name, Object _object)
  {
    graphObjectDAO.setValue(name, _object);
  }

  /**
   * A generic, type-unsafe setter that takes the attribute name a and value as
   * an Object.
   * 
   * @param name
   *          String name of the attribute
   * @param value
   *          String representation of the value
   */
  public void setValue(String name, Object _object, Date startDate, Date endDate)
  {
    graphObjectDAO.setValue(name, _object, startDate, endDate);
  }

  public void setEmbeddedValue(String name, String embeddedAttributeName, Object _object)
  {
    AttributeEmbedded attribute = (AttributeLocalEmbedded) this.graphObjectDAO.getAttribute(name);
    attribute.setValue(embeddedAttributeName, _object);
  }

  public void setEmbeddedValue(String name, String embeddedAttributeName, Object _object, Date startDate, Date endDate)
  {
    AttributeEmbedded attribute = (AttributeLocalEmbedded) this.graphObjectDAO.getAttribute(name);
    attribute.setValue(embeddedAttributeName, _object, startDate, endDate);
  }

  @SuppressWarnings("unchecked")
  public <T> T getEmbeddedValue(String name, String embeddedAttributeName)
  {
    AttributeEmbedded attribute = (AttributeLocalEmbedded) this.graphObjectDAO.getAttribute(name);

    return (T) attribute.getValue(embeddedAttributeName);
  }

  @SuppressWarnings("unchecked")
  public <T> T getEmbeddedValue(String name, String embeddedAttributeName, Date date)
  {
    AttributeEmbedded attribute = (AttributeLocalEmbedded) this.graphObjectDAO.getAttribute(name);

    return (T) attribute.getValue(embeddedAttributeName, date);
  }

  /**
   * A generic, type-unsafe method for adding an item to an enumerated attribute
   * that takes the attribute name and enumeration item as Strings
   * 
   * @param name
   *          String name of the enumerated attribute
   * @param item
   *          String representation of the enumeration item
   */
  public void addEnumItem(String name, String item)
  {
    graphObjectDAO.addItem(name, item);
  }

  /**
   * Replaces the items of an enumerated attribute. If the attribute does not
   * allow multiplicity, then the {@code values} collection must contain only
   * one item.
   * 
   * @param name
   *          Name of the enumerated attribute
   * @param values
   *          Collection of enumerated item ids
   */
  public void replaceItems(String name, Collection<String> values)
  {
    graphObjectDAO.replaceItems(name, values);
  }

  /**
   * A generic, type-unsafe method for removing an item from an enumerated
   * attribute that takes the attribute name and enumeration item as Strings
   * 
   * @param name
   *          String name of the enumerated attribute
   * @param item
   *          String representation of the enumeration item
   */
  public void removeEnumItem(String name, String item)
  {
    graphObjectDAO.removeItem(name, item);
  }

  /**
   * A generic method for clearing out all selected items on an enumerated
   * attribute.
   * 
   * @param name
   *          String name of the enumerated attribute
   */
  public void clearEnum(String name)
  {
    graphObjectDAO.clearItems(name);
  }

  /**
   * Returns the type of this GraphObject. Generic graphObject objects can
   * represent specific types - this method returns the declared type of the
   * object.
   * 
   * @return The declared type of this object
   */
  protected abstract String getDeclaredType();

  /**
   * Returns the Universally Unique OID (UUID) for this graphObject.
   * 
   * @return <b>this</b> graphObject's UUID
   */
  public String getOid()
  {
    return graphObjectDAO.getOid();
  }

  /**
   * Returns the Id used for AttributeProblems (not messages). New instances
   * that fail will have a different OID on the client.
   * 
   * @return problem notification oid.
   */
  public String getProblemNotificationId()
  {
    return graphObjectDAO.getProblemNotificationId();
  }

  public String getType()
  {
    return graphObjectDAO.getType();
  }

  public String getKey()
  {
    return graphObjectDAO.getKey();
  }

  /**
   * Returns the display label of the class.
   * 
   * @return <b>this</b> graphObject's UUID
   */
  public String getClassDisplayLabel()
  {
    return graphObjectDAO.getMdClassDAO().getDisplayLabel(Session.getCurrentLocale());
  }

  /**
   * Persists this graphObject and all changes to the database.
   * <code><b>new</b></code> instances of GraphObject are <i>not</i> persisted
   * until <code>apply()</code> is called. Similarly, changes made to instances
   * through the generated java classes are not persisted until
   * <code>apply()</code> is called.
   * 
   * <b>Precondition:</b> Session user has a lock on this object, assuming this
   * object has a ComponentIF.LOCKED_BY field.
   */
  public void apply()
  {
    graphObjectDAO.apply();
  }

  /**
   * Deletes this graphObject from the database. Any attempt to
   * {@link GraphObject#apply()} this graphObject will throw an exception, so it
   * is the responsibility of the developer to remove references to deleted
   * instances of GraphObject.
   */
  public void delete()
  {
    graphObjectDAO.delete();
  }

  /**
   * When an object at the business layer is converted into a DTO, this method
   * is invoked to ensure there are not any READ violations that are enforced
   * programatically. This method should be ovewritten in business classes if
   * special programatic READ permissions need to be implemented. This method
   * should throw an exception if declarative READ permissions are not adequate.
   */
  public void customReadCheck()
  {
  }

  /**
   * Writes to standard out all attribute names and their values of this
   * BusinessDAO instance. All values that are keys are dereferenced and the
   * values referenced by those keys are returned.
   * 
   * <br/>
   * <b>Precondition:</b> true
   * 
   */
  public void printAttributes()
  {
    this.graphObjectDAO.printAttributes();
  }

  /**
   * Returns if an attribute of the GraphObject has been modified from its
   * orginal value loaded from the database.
   * 
   * @param name
   *          The name of the attribute
   * @return
   */
  public boolean isModified(String name)
  {
    return graphObjectDAO.getAttribute(name).isModified();
  }

  /**
   * @return Key is a required field, but the default implementation is an empty
   *         string. However, this method should be overwritten in child classes
   *         to return meaningful key values. Key values must be unique for all
   *         entities which are part of the same type hierarchy.
   */
  protected String buildKey()
  {
    return "";
  }

  @Override
  public boolean equals(Object obj)
  {
    if (! ( obj instanceof ComponentIF ))
    {
      return false;
    }

    ComponentIF comp = (ComponentIF) obj;
    return this.getOid().equals(comp.getOid());
  }

  @Override
  public void setValue(String name, String _value)
  {
    this.getGraphObjectDAO().setValue(name, _value);
  }

  @Override
  public void setBlob(String blobName, byte[] value)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void replaceEnumItems(String name, Collection<String> values)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void addMultiItem(String name, String itemId)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void replaceMultiItems(String name, Collection<String> itemIds)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void removeMultiItem(String name, String item)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clearMultiItems(String name)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getValue(String name)
  {
    throw new UnsupportedOperationException();
  }
}
