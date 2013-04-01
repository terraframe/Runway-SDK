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
package com.runwaysdk.business;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import com.runwaysdk.business.state.StateException;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.BusinessInfo;
import com.runwaysdk.constants.MdEntityInfo;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.AttributeStructIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.InvalidIdException;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.TransitionDAOIF;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

/**
 * The root class of all business objects, Business provides functionality
 * specific to objects. Generic interaction with the Data Access layer is
 * accompished through methods inherited from
 * {@link com.runwaysdk.business.Entity}
 *
 * @author Eric Grunzke
 */
public class Business extends Element
{
  /**
   *
   */
  private static final long serialVersionUID = -4724268254610787034L;

  /**
   * Constructs a new, typesafe Business object. Creates a new DAO of type
   * {@link Entity#getDeclaredType()}, which, through polymorphism, will ensure
   * that the core type and the java type are the same.
   */
  public Business()
  {
    super();
    setDataEntity(BusinessDAO.newInstance(getDeclaredType()));
  }

  /**
   * Creates a new Business object that may not be typesafe. The DAO the new
   * object represents will be of the specified type, but if this constructor
   * has been super()d into, the concrete java type of the constructed object
   * will be unknown.
   *
   * To guarantee that the java type and DAO type correspond correctly, use
   * {@link Business#Business()}, which uses polymorphism instead of a
   * paramater.
   *
   * @param type
   *          The type of the DAO that this Business object will represent
   */
  public Business(String type)
  {
    super();
    setDataEntity(BusinessDAO.newInstance(type));
  }

  /**
   * Default visibilty only, this constructor is used to create a Business for a
   * BusinessDAO that is already in the database. All attribute values are
   * pulled from the BusinessDAO parameter.
   *
   * @param buisnessDAO
   */
  Business(BusinessDAO businessDAO)
  {
    super();
    setDataEntity(businessDAO);
  }

  /**
   * Using reflection, get returns an object of the specified type with the
   * specified id from the database. The returned Business is typesafe, meaning
   * that its actual type is that specified by the type parameter.
   *
   * @param id
   *          ID of the instance to get
   * @return Typesafe Business representing the id in the database
   */
  public static Business get(String id)
  {
    // An empty string likely indicates the value was never set in the database.
    if (id == null || id.length() == 0)
    {
      String errMsg = "Object with id [" + id + "] is not defined by a [" + MdEntityInfo.CLASS + "]";

      throw new InvalidIdException(errMsg, id);
    }

    Business reflected = instantiate(BusinessDAO.get(id));

    return reflected;
  }

  /**
   * Using reflection, get returns an object of the specified type with the
   * specified key from the database. The returned Business is typesafe, meaning
   * that its actual type is that specified by the type parameter.
   *
   * @param type
   *          type of the instance to get
   * @param key
   *          key of the instance to get
   * @return Typesafe Business representing the id in the database
   */
  public static Business get(String type, String key)
  {
    Business reflected = instantiate(BusinessDAO.get(type, key));

    return reflected;
  }

  /**
   * Using reflection, getEnumeration returns and object of the specified master
   * type with the specified enumeration name from the database. The returned
   * Business is typesafe, meaning that its actual type is that specified by the
   * master type parameter.
   *
   * @param masterType
   *          The type of the Enumeration Master
   * @param enumName
   *          The name of the Enumerated item.
   * @return Typesafe Business representing the enumerated item in the database
   */
  public static Business getEnumeration(String masterType, String enumName)
  {
    EnumerationItemDAO enumerationItem = ObjectCache.getEnumeration(masterType, enumName);
    return (Business) instantiate(enumerationItem);
  }

  /**
   * Returns an object of the specified type with the specified id from the
   * database without using reflection. The returned Business is not typesafe,
   * meaning that its actual type just a Business.
   *
   * @param id
   *          ID of the instance to get.
   * @return Typesafe Business representing the id in the database.
   */
  public static Business getBusiness(String id)
  {
    // Cast is OK, as the data access object cannot be modified unless the
    // logged in user
    // has a lock on the object.
    BusinessDAOIF object = BusinessDAO.get(id);
    return new Business((BusinessDAO) object);
  }

  /**
   * Instantiates a Business using reflection to invoke the empty constructor in
   * the concrete child class. Will throw a {@link ClassLoaderException} if any
   * number of Exceptions are caught during the reflection.
   *
   * @param businessDAOIF
   *          The database object the new Business will represent
   * @return A typesafe Business
   */
  static Business instantiate(BusinessDAOIF businessDAOIF)
  {
    // This cast is OK, as lock checks are in place to prevent this
    // businessDAOIF
    // from being modified via setters on the business object.
    return instantiate((BusinessDAO) businessDAOIF);
  }

  /**
   * Instantiates a Business using reflection to invoke the empty constructor in
   * the concrete child class. Will throw a {@link ClassLoaderException} if any
   * number of Exceptions are caught during the reflection.
   *
   * @param businessDAO
   *          The database object the new Business will represent
   * @return A typesafe Business
   */
  private static Business instantiate(BusinessDAO businessDAO)
  {
    Business object;
    try
    {
      Class<?> clazz = LoaderDecorator.load(businessDAO.getType());
      Constructor<?> con = clazz.getConstructor();
      object = (Business) con.newInstance();

      object.setDataEntity(businessDAO);

      // Set the private variables of the runtime type
      for (AttributeIF attribute : businessDAO.getAttributeArrayIF())
      {
        if (attribute instanceof AttributeStructIF)
        {
          AttributeStructIF attributeStruct = (AttributeStructIF) attribute;
          Struct struct = Struct.instantiate(object, attributeStruct.getName());
          String typeName = attributeStruct.getDefiningClassType() + TypeGeneratorInfo.BASE_SUFFIX;

          Class<?> c = LoaderDecorator.load(typeName);
          String structName = CommonGenerationUtil.lowerFirstCharacter(attribute.getName());

          Field field = c.getDeclaredField(structName);
          field.setAccessible(true);
          field.set(object, struct);
        }
      }
    }
    catch (Exception e)
    {
      throw new ClassLoaderException(businessDAO.getMdClassDAO(), e);
    }

    return object;
  }

  /**
   * Accesses the delegation object for interaction with the Data Access layer.
   *
   * @return the BusinessDAO this Business represents
   */
  BusinessDAO businessDAO()
  {
    return (BusinessDAO) entityDAO;
  }

  /**
   *
   * @param transitionName
   */
  public void promote(String transitionName)
  {
    try
    {
      businessDAO().promote(transitionName);
    }
    catch (StateException e)
    {
      String source = "Unknown";
      for (TransitionDAOIF r : businessDAO().getMdBusinessDAO().definesMdStateMachine().definesTransitions())
        if (r.getName().equals(transitionName))
          source = r.getParent().getValue(StateMasterDAOIF.STATE_NAME);

      String error = "You cannot '" + transitionName + "' from the [" + businessDAO().currentState().getName() + "] State: you must be in the [" + source + "] State.";
      throw new InvalidTransistionException(error);
    }
  }

  /**
   * Default visibility is on purpose:
   * {@link BusinessFacade#currentState(Business)} can access this method, but
   * inherited children cannot. This allows internal access through the Facade
   * without exposing implementation to generated classes.
   *
   * @return The current state of this Business
   */
  StateMasterDAOIF currentState()
  {
    return businessDAO().currentState();
  }

  /**
   * Returns the state label for this object. If this object does not partake in
   * a state machine then an empty string is returned.
   *
   * @return The state label or an empty string if the state label doesn't
   *         exist.
   */
  public String getState()
  {
    if (this.hasState())
    {
      return this.currentState().getValue(StateMasterDAOIF.STATE_NAME);
    }
    else
    {
      return "";
    }
  }

  /**
   * Default visibility is on purpose:
   * {@link BusinessFacade#currentState(Business)} can access this method, but
   * inherited children cannot. This allows internal access through the Facade
   * without exposing implementation to generated classes.
   *
   * @return The current state of this Business
   */
  boolean hasState()
  {
    return businessDAO().hasState();
  }

  protected String getDeclaredType()
  {
    return BusinessInfo.CLASS;
  }

  /**
   * Returns all parents of <b>this</b> from Relationships of the given type.
   *
   * @param type
   *          type of Relatinoship to get
   * @return List of parents
   */
  public OIterator<? extends Business> getParents(String type)
  {
    MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(type);

    if (mdRelationshipIF.cacheAllInstances())
    {
      HashMap<String, Business> map = new HashMap<String, Business>();
      ArrayList<Business> arrayList = new ArrayList<Business>();
      for (RelationshipDAOIF relationship : businessDAO().getParents(type))
      {
        BusinessDAOIF parentBusinessDAOIF = relationship.getParent();
        if (!map.containsKey(parentBusinessDAOIF.getId()))
        {
          Business business = instantiate(parentBusinessDAOIF);
          map.put(parentBusinessDAOIF.getId(), business);
          arrayList.add(business);
        }
      }
      return new CashedComponentIterator<Business>(arrayList);
    }
    else
    {
      QueryFactory queryFactory = new QueryFactory();

      // Get the relationships where this object is the parent
      RelationshipQuery rq = queryFactory.relationshipQuery(type);
      rq.WHERE(rq.childId().EQ(this.getId()));

      // Now fetch the child objects
      BusinessQuery bq = queryFactory.businessQuery(mdRelationshipIF.getParentMdBusiness().definesType());
      bq.WHERE(bq.isParentIn(rq));

      return bq.getIterator();
    }
  }

  /**
   * Returns all relationships that this Business is a child of. This method is
   * similar to {@link #getParents(String)}, only instead of returning the
   * parents, it returns the relationships that contain the parents.
   *
   * @param type
   *          type of Relationship to get
   * @return List of Relationships
   */
  public OIterator<? extends Relationship> getParentRelationships(String type)
  {
    MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(type);

    if (mdRelationshipIF.cacheAllInstances())
    {
      ArrayList<Relationship> arrayList = new ArrayList<Relationship>();
      for (RelationshipDAOIF relationshipDAOIF : businessDAO().getParents(type))
      {
        Relationship relationship = Relationship.instantiate(relationshipDAOIF);
        arrayList.add(relationship);
      }
      return new CashedComponentIterator<Relationship>(arrayList);
    }
    else
    {
      QueryFactory queryFactory = new QueryFactory();

      // Get the relationships where this object is the parent
      RelationshipQuery rq = queryFactory.relationshipQuery(type);
      rq.WHERE(rq.childId().EQ(this.getId()));

      return rq.getIterator();
    }
  }

  /**
   * Returns all relationships of the given type between <b>this</b> and the
   * specified child.
   *
   * @param parent
   *          Object that is the child of <b>this</b>
   * @param type
   *          The type of relationship that connects <b>this</b> to the child
   * @return List of realtionships
   */
  public OIterator<? extends Relationship> getRelationshipsWithParent(Element parent, String type)
  {
    MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(type);

    if (mdRelationshipIF.cacheAllInstances())
    {
      ArrayList<Relationship> arrayList = new ArrayList<Relationship>();
      for (RelationshipDAOIF relationshipDAOIF : businessDAO().getParents(parent.getId(), type))
      {
        Relationship relationship = Relationship.instantiate(relationshipDAOIF);
        arrayList.add(relationship);
      }
      return new CashedComponentIterator<Relationship>(arrayList);
    }
    else
    {
      QueryFactory queryFactory = new QueryFactory();

      // Get the relationships where this object is the parent
      RelationshipQuery rq = queryFactory.relationshipQuery(type);
      rq.WHERE(rq.childId().EQ(this.getId()).AND(rq.parentId().EQ(parent.getId())));

      return rq.getIterator();
    }
  }

  /**
   * Returns all children of <b>this</b> from Relationships of the given type.
   *
   * @param type
   *          type of Relationship to get
   * @return List of children
   */
  public OIterator<? extends Business> getChildren(String type)
  {
    MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(type);

    if (mdRelationshipIF.cacheAllInstances())
    {
      HashMap<String, Business> map = new HashMap<String, Business>();
      ArrayList<Business> arrayList = new ArrayList<Business>();
      for (RelationshipDAOIF relationship : businessDAO().getChildren(type))
      {
        BusinessDAOIF childBusinessDAOIF = relationship.getChild();
        if (!map.containsKey(childBusinessDAOIF.getId()))
        {
          Business business = instantiate(childBusinessDAOIF);
          map.put(childBusinessDAOIF.getId(), business);
          arrayList.add(business);
        }
      }
      return new CashedComponentIterator<Business>(arrayList);
    }
    else
    {
      QueryFactory queryFactory = new QueryFactory();

      // Get the relationships where this object is the parent
      RelationshipQuery rq = queryFactory.relationshipQuery(type);
      rq.WHERE(rq.parentId().EQ(this.getId()));

      // Now fetch the child objects
      BusinessQuery bq = queryFactory.businessQuery(mdRelationshipIF.getChildMdBusiness().definesType());
      bq.WHERE(bq.isChildIn(rq));

      return bq.getIterator();
    }
  }

  /**
   * Returns all relationships that this Business is a parent of. This method is
   * similar to {@link #getChildren(String)}, only instead of returning the
   * children, it returns the relationships that contain the children.
   *
   * @param type
   *          type of Relationship to get
   * @return List of Relationships
   */
  public OIterator<? extends Relationship> getChildRelationships(String type)
  {
    MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(type);

    if (mdRelationshipIF.cacheAllInstances())
    {
      ArrayList<Relationship> arrayList = new ArrayList<Relationship>();
      for (RelationshipDAOIF relationshipDAOIF : businessDAO().getChildren(type))
      {
        Relationship relationship = Relationship.instantiate(relationshipDAOIF);
        arrayList.add(relationship);
      }
      return new CashedComponentIterator<Relationship>(arrayList);
    }
    else
    {
      QueryFactory queryFactory = new QueryFactory();

      // Get the relationships where this object is the parent
      RelationshipQuery rq = queryFactory.relationshipQuery(type);
      rq.WHERE(rq.parentId().EQ(this.getId()));

      return rq.getIterator();
    }
  }

  /**
   * Returns all relationships of the given type between <b>this</b> and the
   * specified child.
   *
   * @param child
   *          Object that is the child of <b>this</b>
   * @param type
   *          The type of relationship that connects <b>this</b> to the child
   * @return List of realtionships
   */
  public OIterator<? extends Relationship> getRelationshipsWithChild(Business child, String type)
  {
    MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(type);

    if (mdRelationshipIF.cacheAllInstances())
    {
      ArrayList<Relationship> arrayList = new ArrayList<Relationship>();
      for (RelationshipDAOIF relationshipDAOIF : businessDAO().getChildren(child.getId(), type))
      {
        Relationship relationship = Relationship.instantiate(relationshipDAOIF);
        arrayList.add(relationship);
      }
      return new CashedComponentIterator<Relationship>(arrayList);
    }
    else
    {
      QueryFactory queryFactory = new QueryFactory();

      // Get the relationships where this object is the parent
      RelationshipQuery rq = queryFactory.relationshipQuery(type);
      rq.WHERE(rq.parentId().EQ(this.getId()).AND(rq.childId().EQ(child.getId())));

      return rq.getIterator();
    }
  }

  /**
   * Generic, type-unsafe Relationship creation mechanism. Creates a
   * Relationship of the given type with the given parent and <b>this</b> as the
   * child.
   *
   * @param parent
   *          Parent of <b>this</b> in the new relationship
   * @param relationshipType
   *          type of the desired relationship
   * @return The newly created Relationship
   */
  public Relationship addParent(Business parent, String relationshipType)
  {
    return this.addParent(parent.getId(), relationshipType);
  }

  /**
   * Type-safe Relationship creation mechanism. Creates a Relationship of the
   * given type with the given parent and <b>this</b> as the child.
   *
   * @param root
   *          id of the Parent of <b>this</b> in the new relationship
   * @param relationshipType
   *          type of the desired relationship
   * @return The newly created Relationship
   */
  public Relationship addParent(String parentId, String relationshipType)
  {
    try
    {
      Class<?> clazz = LoaderDecorator.load(relationshipType);
      Constructor<?> con = clazz.getConstructor(String.class, String.class);
      return (Relationship) con.newInstance(parentId, this.getId());
    }
    catch (Exception e)
    {
      throw new ClassLoaderException(MdRelationshipDAO.getMdRelationshipDAO(relationshipType), e);
    }
  }

  /**
   * Generic, type-unsafe Relationship delete mechanism. Removes all instances
   * of the given object as a parent of <b>this</b> object.
   *
   * @param parent
   *          Parent of <b>this</b> to be removed.
   * @param relationshipType
   *          type of the desired relationship
   */
  public void removeAllParents(Business parent, String relationshipType)
  {
    businessDAO().removeAllParents(parent.businessDAO(), relationshipType, true);
  }

  /**
   * Generic, type-unsafe Relationship delete mechanism. Removes all instances
   * of the given object as a parent of <b>this</b> object.
   *
   * @param parentId
   *          id of the Parent of <b>this</b> to be removed.
   * @param relationshipType
   *          type of the desired relationship
   */
  public void removeAllParents(String parentId, String relationshipType)
  {
    businessDAO().removeAllParents(parentId, relationshipType, true);
  }

  /**
   * Removes the given relationship that represents a parent relationship with
   * this object.
   *
   * <br/>
   * <b>Precondition:</b> relationshipId != null <br/>
   * <b>Precondition:</b> !relationshipId().equals("") <br>
   * <b>Precondition:</b> id to relationship object must represent the a parent
   * relationship with this object.
   *
   * @param relationshipId
   *          id to a parent relationship.
   */
  public void removeParent(String relationshipId)
  {
    businessDAO().removeParent(relationshipId, true);
  }

  /**
   * Removes the given relationship that represents a parent relationship with
   * this object.
   *
   * <br>
   * <b>Precondition:</b>Relationship object must represent the a parent
   * relationship with this object.
   *
   * @param relationship
   *          parent relationship.
   */
  public void removeParent(Relationship relationship)
  {
    businessDAO().removeParent((RelationshipDAO) relationship.getEntityDAO(), true);
  }

  /**
   * Generic, type-unsafe Relationship creation mechanism. Creates a
   * Relationship of the given type with the given child and <b>this</b> as the
   * parent.
   *
   * @param child
   *          Child of <b>this</b> in the new relationship
   * @param relationshipType
   *          type of the desired relationship
   * @return The newly created Relationship.
   */
  public Relationship addChild(Business child, String relationshipType)
  {
    return this.addChild(child.getId(), relationshipType);
  }

  /**
   * Type-safe Relationship creation mechanism. Creates a Relationship of the
   * given type with the given child and <b>this</b> as the parent.
   *
   * @param childId
   *          id of the Child of <b>this</b> in the new relationship
   * @param relationshipType
   *          type of the desired relationship
   * @return The newly created Relationship.
   */
  public Relationship addChild(String childId, String relationshipType)
  {
    try
    {
      Class<?> clazz = LoaderDecorator.load(relationshipType);
      Constructor<?> con = clazz.getConstructor(String.class, String.class);
      return (Relationship) con.newInstance(this.getId(), childId);
    }
    catch (Exception e)
    {
      throw new ClassLoaderException(MdRelationshipDAO.getMdRelationshipDAO(relationshipType), e);
    }
  }

  /**
   * Generic, type-unsafe Relationship delete mechanism. Removes all instances
   * of this object as a child of <b>this</b> object.
   *
   * @param child
   *          Child of <b>this</b> to be removed.
   * @param relationshipType
   *          type of the desired relationship
   */
  public void removeAllChildren(Business child, String relationshipType)
  {
    businessDAO().removeAllChildren(child.businessDAO(), relationshipType, true);
  }

  /**
   * Generic, type-unsafe Relationship delete mechanism. Removes all instances
   * of this object as a child of <b>this</b> object.
   *
   * @param childId
   *          id of the child of <b>this</b> to be removed.
   * @param relationshipType
   *          type of the desired relationship
   */
  public void removeAllChildren(String childId, String relationshipType)
  {
    businessDAO().removeAllChildren(childId, relationshipType, true);
  }

  /**
   * Removes the given relationship that represents a child relationship with
   * this object.
   *
   * <br/>
   * <b>Precondition:</b> relationshipId != null <br/>
   * <b>Precondition:</b> !relationshipId().equals("") <br/>
   * <b>Precondition:</b> Id to relationship object must represent the a child
   * relationship with this object.
   *
   * @param relationshipId
   *          id to a child relationship.
   */
  public void removeChild(String relationshipId)
  {
    businessDAO().removeChild(relationshipId, true);
  }

  /**
   * Removes the given relationship that represents a child relationship with
   * this object.
   *
   * <br/>
   * <b>Precondition:</b> relationship != null <br/>
   * <b>Precondition:</b>Relationship object must represent the a child
   * relationship with this object.
   *
   * @param relationship
   *          child relationship.
   */
  public void removeChild(Relationship relationship)
  {
    businessDAO().removeChild((RelationshipDAO) relationship.getEntityDAO(), true);
  }

  /**
   * ***************************************************************************
   * ****
   */
  /** Generated accessor methods * */
  /**
   * ***************************************************************************
   * ****
   */

}
