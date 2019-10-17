package com.runwaysdk.business.graph;

import com.runwaysdk.business.Entity;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;

public class VertexObject extends GraphObject
{

  /**
   * Constructs a new, typesafe VertexObject object. Creates a new DAO of type
   * {@link Entity#getDeclaredType()}, which, through polymorphism, will ensure
   * that the core type and the java type are the same.
   */
  public VertexObject()
  {
    super();
    setGraphObjectDAO(VertexObjectDAO.newInstance(getDeclaredType()));
  }

  /**
   * Creates a new VertexObject object that may not be typesafe. The DAO the new
   * object represents will be of the specified type, but if this constructor
   * has been super()d into, the concrete java type of the constructed object
   * will be unknown.
   * 
   * To guarantee that the java type and DAO type correspond correctly, use
   * {@link VertexObject#VertexObject()}, which uses polymorphism instead of a
   * paramater.
   * 
   * @param type
   *          The type of the DAO that this VertexObject object will represent
   */
  public VertexObject(String type)
  {
    super();
    setGraphObjectDAO(VertexObjectDAO.newInstance(type));
  }

  /**
   * Default visibilty only, this constructor is used to create a VertexObject
   * for a VertexObjectDAO that is already in the database. All attribute values
   * are pulled from the VertexObjectDAO parameter.
   * 
   * @param buisnessDAO
   */
  VertexObject(VertexObjectDAO businessDAO)
  {
    super();
    setGraphObjectDAO(businessDAO);
  }

  @Override
  protected String getDeclaredType()
  {
    return VertexObject.class.getName();
  }

  // /**
  // * Using reflection, get returns an object of the specified type with the
  // * specified oid from the database. The returned VertexObject is typesafe,
  // * meaning that its actual type is that specified by the type parameter.
  // *
  // * @param oid
  // * OID of the instance to get
  // * @return Typesafe VertexObject representing the oid in the database
  // */
  // public static VertexObject get(String oid)
  // {
  // // An empty string likely indicates the value was never set in the
  // database.
  // if (oid == null || oid.length() == 0)
  // {
  // String errMsg = "Object with oid [" + oid + "] is not defined by a [" +
  // MdEntityInfo.CLASS + "]";
  //
  // throw new InvalidIdException(errMsg, oid);
  // }
  //
  // VertexObject reflected = instantiate(VertexObjectDAO.get(oid));
  //
  // return reflected;
  // }
  //
  // /**
  // * Using reflection, get returns an object of the specified type with the
  // * specified key from the database. The returned VertexObject is typesafe,
  // * meaning that its actual type is that specified by the type parameter.
  // *
  // * @param type
  // * type of the instance to get
  // * @param key
  // * key of the instance to get
  // * @return Typesafe VertexObject representing the oid in the database
  // */
  // public static VertexObject get(String type, String key)
  // {
  // VertexObject reflected = instantiate(VertexObjectDAO.get(type, key));
  //
  // return reflected;
  // }
}
