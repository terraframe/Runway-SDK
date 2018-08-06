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
package com.runwaysdk.business;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import com.runwaysdk.RunwayException;
import com.runwaysdk.constants.MdEntityInfo;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.AttributeStructIF;
import com.runwaysdk.dataaccess.InvalidIdException;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.generation.loader.LoaderDecorator;

/**
 * The root class of all relationships, Relationship provides functionality specific to relationships. Generic interaction with the Data Access layer is accompished through methods inherited from
 * {@link com.runwaysdk.business.Entity}
 *
 * @author Eric Grunzke
 */
public class Relationship extends Element implements Serializable
{
  public final static String CLASS            = Relationship.class.getName();

  /**
   *
   */
  private static final long  serialVersionUID = 6258495472348701912L;

  /**
   * Lazily instantiated, this reference to the parent of the relationship is included to prevent repeated instantiations through reflection.
   */
  private Business           parent;

  /**
   * Lazily instantiated, this reference to the child of the relationship is included to prevent repeated instantiations through reflection.
   */
  private Business           child;

  /**
   * Constructor for new instances of Realtionships.
   *
   * @param parentId
   *          Database oid of the parent
   * @param childId
   *          Database oid of the child
   * @param type
   *          type the relationship
   */
  public Relationship(String parentId, String childId)
  {
    super();
    setDataEntity(RelationshipDAO.newInstance(parentId, childId, getDeclaredType()));
    parent = null;
    child = null;
  }

  /**
   * Constructor for generic instances of Realtionships. Should not be called by subclasses, as their java type may not correctly represent their DAO type.
   *
   * @param parentId
   *          Database oid of the parent
   * @param childId
   *          Database oid of the child
   * @param type
   *          type the relationship
   */
  public Relationship(String parentId, String childId, String type)
  {
    super();
    setDataEntity(RelationshipDAO.newInstance(parentId, childId, type));
    parent = null;
    child = null;
  }

  /**
   * Default visibilty only, this constructor is used to create a Relationship for a RelationshipDAO that is already in the database. All attribute values are pulled from the RelationshipDAO
   * parameter.
   *
   * @param relationship
   */
  Relationship(RelationshipDAO relationship)
  {
    super();
    setDataEntity(relationship);
  }

  /**
   * Overwrites the parent oid if this relationship is new and has not been applied to the database. This method should be used very carefully as it is a backdoor method which can cause data
   * corruption.
   *
   * @param parentId
   */
  public void overwriteParentId(String parentId)
  {
    ( (RelationshipDAO) this.entityDAO ).overwriteParentId(parentId);
  }

  /**
   * Overwrites the child oid if this relationship is new and has not been applied to the database. This method should be used very carefully as it is a backdoor method which can cause data corruption.
   *
   * @param childId
   */
  public void overwriteChildId(String childId)
  {
    ( (RelationshipDAO) this.entityDAO ).overwriteParentId(childId);
  }

  /**
   * Using reflection, get returns an object of the specified type with the specified oid from the database. The returned Relationship is typesafe, meaning that its actual type is that specified by the
   * type parameter.
   *
   * @param oid
   *          ID of the instance to get
   * @return Typesafe Relationship representing the oid in the database
   */
  public static Relationship get(String oid)
  {
    // An empty string likely indicates the value was never set in the database.
    if (oid == null || oid.length() == 0)
    {
      String errMsg = "Object with oid [" + oid + "] is not defined by a [" + MdEntityInfo.CLASS + "]";

      throw new InvalidIdException(errMsg, oid);
    }

    Relationship reflected = instantiate(RelationshipDAO.get(oid));

    return reflected;
  }

  /**
   * Using reflection, get returns an object of the specified type with the specified key from the database. The returned Business is typesafe, meaning that its actual type is that specified by the
   * type parameter.
   *
   * @param type
   *          type of the instance to get
   * @param key
   *          key of the instance to get
   * @return Typesafe Business representing the oid in the database
   */
  public static Relationship get(String type, String key)
  {
    Relationship reflected = instantiate(RelationshipDAO.get(type, key));

    return reflected;
  }

  /**
   * Returns an object of the specified type with the specified oid from the database without using reflection. The returned Relationship is not typesafe, meaning that its actual type just a
   * Relationship.
   *
   * @param oid
   *          ID of the instance to get.
   * @param type
   *          type of the instance to get.
   * @return Typesafe Relationship representing the oid in the database.
   */
  public static Relationship getRelationship(String oid)
  {
    RelationshipDAOIF relationshipIF = RelationshipDAO.get(oid);
    // Cast is OK, as the data access object cannot be modified unless the logged in user
    // has a lock on the object.
    return new Relationship((RelationshipDAO) relationshipIF);
  }

  /**
   * Instantiates a Relationship using reflection to invoke {@link Relationship#Relationship(String, String, String)} in the concrete child class. Will throw a {@link ClassLoaderException} if any
   * number of Exceptions are caught during the reflection.
   *
   * @param relationshipIF
   *          The database relationship that the new Relationship will represent
   * @param type
   *          The type to be instantiated
   * @return A typesafe Relationship
   */
  static Relationship instantiate(RelationshipDAOIF relationshipIF)
  {
    // This cast is OK, as lock checks are in place to prevent this relationship
    // from being modified via setters on the business relationship.
    return instantiate((RelationshipDAO) relationshipIF);
  }

  /**
   * Instantiates a Relationship using reflection to invoke {@link Relationship#Relationship(String, String)} in the concrete child class. Will throw a {@link ClassLoaderException} if any number of
   * Exceptions are caught during the reflection.
   *
   * @param relationshipDAO
   *          The database relationship that the new Relationship will represent
   * @param type
   *          The type to be instantiated
   * @return A typesafe Relationship
   */
  private static Relationship instantiate(RelationshipDAO relationshipDAO)
  {
    Relationship relationship;
    String type = relationshipDAO.getType();

    try
    {
      if (relationshipDAO.getMdClassDAO().isGenerateSource())
      {
        Class<?> clazz = LoaderDecorator.load(type);
        Class<String> stringClass = String.class;
        Constructor<?> con = clazz.getConstructor(stringClass, stringClass);

        relationship = (Relationship) con.newInstance(relationshipDAO.getParentId(), relationshipDAO.getChildId());

        // Set the private variables of the runtime type
        for (AttributeIF attribute : relationshipDAO.getAttributeArrayIF())
        {
          if (attribute instanceof AttributeStructIF)
          {
            AttributeStructIF struct = (AttributeStructIF) attribute;
            Struct bStruct = Struct.instantiate(relationship, struct.getName());
            String typeName = struct.getDefiningClassType() + TypeGeneratorInfo.BASE_SUFFIX;

            Class<?> c = LoaderDecorator.load(typeName);
            String structName = CommonGenerationUtil.lowerFirstCharacter(attribute.getName());

            Field field = c.getDeclaredField(structName);
            field.setAccessible(true);
            field.set(relationship, bStruct);
          }
        }
      }
      else
      {
        relationship = new Relationship(relationshipDAO);
      }
    }
    catch (RunwayException d)
    {
      // We want our own exceptions to pass through
      throw d;
    }
    catch (Exception e)
    {
      // And we want to wrap Sun's exceptions in our own
      throw new ClassLoaderException(MdRelationshipDAO.getMdRelationshipDAO(type), e);
    }

    relationship.setDataEntity(relationshipDAO);
    return relationship;
  }

  /**
   * Delegation object for interaction with the Data Access layer.
   */
  RelationshipDAO relationship()
  {
    return (RelationshipDAO) entityDAO;
  }

  /**
   * Provides access to the typesafe parent of this relationship. The parent is lazily instantiated.
   *
   * @return Typesafe parent of this relationship
   */
  public Business getParent()
  {
    // Lazy instantiation of parent
    if (parent == null)
      parent = Business.get(relationship().getParentId());

    return parent;
  }

  /**
   * Returns the oid of the parent on this object.
   *
   * @return oid of the parent on this object.
   */
  public String getParentId()
  {
    return this.relationship().getParentId();
  }

  /**
   * Provides access to the typesafe child of this relationship. The child is lazily instantiated.
   *
   * @return Typesafe child of this relationship
   */
  public Business getChild()
  {
    // Lazy instantiation of child
    if (child == null)
      child = Business.get(relationship().getChildId());

    return child;
  }

  /**
   * Returns the oid of the child on this object.
   *
   * @return oid of the child on this object.
   */
  public String getChildId()
  {
    return this.relationship().getChildId();
  }

  protected String getDeclaredType()
  {
    return RelationshipInfo.CLASS;
  }
}
