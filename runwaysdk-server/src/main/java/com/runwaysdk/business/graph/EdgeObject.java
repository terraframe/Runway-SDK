package com.runwaysdk.business.graph;

import com.runwaysdk.dataaccess.graph.EdgeObjectDAO;

public class EdgeObject extends GraphObject
{
  /**
   * Lazily instantiated, this reference to the parent of the relationship is
   * included to prevent repeated instantiations through reflection.
   */
  private VertexObject parent;

  /**
   * Lazily instantiated, this reference to the child of the relationship is
   * included to prevent repeated instantiations through reflection.
   */
  private VertexObject child;

  /**
   * Constructor for new instances of Realtionships.
   *
   * @param parentOid
   *          Database oid of the parent
   * @param childOid
   *          Database oid of the child
   * @param type
   *          type the relationship
   */
  public EdgeObject(String parentOid, String childOid)
  {
    super();
    setGraphObjectDAO(EdgeObjectDAO.newInstance(parentOid, childOid, getDeclaredType()));
    parent = null;
    child = null;
  }

  /**
   * Constructor for generic instances of Realtionships. Should not be called by
   * subclasses, as their java type may not correctly represent their DAO type.
   *
   * @param parentOid
   *          Database oid of the parent
   * @param childOid
   *          Database oid of the child
   * @param type
   *          type the relationship
   */
  public EdgeObject(String parentOid, String childOid, String type)
  {
    super();
    setGraphObjectDAO(EdgeObjectDAO.newInstance(parentOid, childOid, type));
    parent = null;
    child = null;
  }

  /**
   * Default visibilty only, this constructor is used to create a EdgeObject for
   * a EdgeObjectDAO that is already in the database. All attribute values are
   * pulled from the EdgeObjectDAO parameter.
   *
   * @param relationship
   */
  EdgeObject(EdgeObjectDAO relationship)
  {
    super();
    setGraphObjectDAO(relationship);
  }

  // /**
  // * Overwrites the parent oid if this relationship is new and has not been
  // * applied to the database. This method should be used very carefully as it
  // is
  // * a backdoor method which can cause data corruption.
  // *
  // * @param parentOid
  // */
  // public void overwriteParentOid(String parentOid)
  // {
  // ( (EdgeObjectDAO) this.relationship() ).overwriteParentOid(parentOid);
  // }
  //
  // /**
  // * Overwrites the child oid if this relationship is new and has not been
  // * applied to the database. This method should be used very carefully as it
  // is
  // * a backdoor method which can cause data corruption.
  // *
  // * @param childOid
  // */
  // public void overwriteChildOid(String childOid)
  // {
  // ( (EdgeObjectDAO) this.entityDAO ).overwriteParentOid(childOid);
  // }
  //

  @Override
  EdgeObjectDAO getGraphObjectDAO()
  {
    return (EdgeObjectDAO) super.getGraphObjectDAO();
  }

//  /**
//   * Provides access to the typesafe parent of this relationship. The parent is
//   * lazily instantiated.
//   *
//   * @return Typesafe parent of this relationship
//   */
//  public VertexObject getParent()
//  {
//    // Lazy instantiation of parent
//    if (parent == null)
//      parent = VertexObject.get(getGraphObjectDAO().getParentOid());
//
//    return parent;
//  }

  /**
   * Returns the oid of the parent on this object.
   *
   * @return oid of the parent on this object.
   */
  public String getParentOid()
  {
    return this.getGraphObjectDAO().getParentOid();
  }

//  /**
//   * Provides access to the typesafe child of this relationship. The child is
//   * lazily instantiated.
//   *
//   * @return Typesafe child of this relationship
//   */
//  public VertexObject getChild()
//  {
//    // Lazy instantiation of child
//    if (child == null)
//      child = VertexObject.get(getGraphObjectDAO().getChildOid());
//
//    return child;
//  }
//
  /**
   * Returns the oid of the child on this object.
   *
   * @return oid of the child on this object.
   */
  public String getChildOid()
  {
    return this.getGraphObjectDAO().getChildOid();
  }

  protected String getDeclaredType()
  {
    return EdgeObject.class.getName();
  }

  // /**
  // * Using reflection, get returns an object of the specified type with the
  // * specified oid from the database. The returned EdgeObject is typesafe,
  // * meaning that its actual type is that specified by the type parameter.
  // *
  // * @param oid
  // * OID of the instance to get
  // * @return Typesafe EdgeObject representing the oid in the database
  // */
  // public static EdgeObject get(String oid)
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
  // EdgeObject reflected = instantiate(EdgeObjectDAO.get(oid));
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
  // public static EdgeObject get(String type, String key)
  // {
  // EdgeObject reflected = instantiate(EdgeObjectDAO.get(type, key));
  //
  // return reflected;
  // }
  //
  // /**
  // * Returns an object of the specified type with the specified oid from the
  // * database without using reflection. The returned EdgeObject is not
  // typesafe,
  // * meaning that its actual type just a EdgeObject.
  // *
  // * @param oid
  // * OID of the instance to get.
  // * @param type
  // * type of the instance to get.
  // * @return Typesafe EdgeObject representing the oid in the database.
  // */
  // public static EdgeObject getEdgeObject(String oid)
  // {
  // EdgeObjectDAOIF relationshipIF = EdgeObjectDAO.get(oid);
  // // Cast is OK, as the data access object cannot be modified unless the
  // // logged in user
  // // has a lock on the object.
  // return new EdgeObject((EdgeObjectDAO) relationshipIF);
  // }
  //
  // /**
  // * Instantiates a EdgeObject using reflection to invoke
  // * {@link EdgeObject#EdgeObject(String, String, String)} in the concrete
  // child
  // * class. Will throw a {@link ClassLoaderException} if any number of
  // * Exceptions are caught during the reflection.
  // *
  // * @param relationshipIF
  // * The database relationship that the new EdgeObject will represent
  // * @param type
  // * The type to be instantiated
  // * @return A typesafe EdgeObject
  // */
  // static EdgeObject instantiate(EdgeObjectDAOIF relationshipIF)
  // {
  // // This cast is OK, as lock checks are in place to prevent this
  // relationship
  // // from being modified via setters on the business relationship.
  // return instantiate((EdgeObjectDAO) relationshipIF);
  // }
  //
  // /**
  // * Instantiates a EdgeObject using reflection to invoke
  // * {@link EdgeObject#EdgeObject(String, String)} in the concrete child
  // class.
  // * Will throw a {@link ClassLoaderException} if any number of Exceptions are
  // * caught during the reflection.
  // *
  // * @param relationshipDAO
  // * The database relationship that the new EdgeObject will represent
  // * @param type
  // * The type to be instantiated
  // * @return A typesafe EdgeObject
  // */
  // private static EdgeObject instantiate(EdgeObjectDAO relationshipDAO)
  // {
  // EdgeObject relationship;
  // String type = relationshipDAO.getType();
  //
  // try
  // {
  // if (relationshipDAO.getMdClassDAO().isGenerateSource())
  // {
  // Class<?> clazz = LoaderDecorator.load(type);
  // Class<String> stringClass = String.class;
  // Constructor<?> con = clazz.getConstructor(stringClass, stringClass);
  //
  // relationship = (EdgeObject) con.newInstance(relationshipDAO.getParentOid(),
  // relationshipDAO.getChildOid());
  //
  // // Set the private variables of the runtime type
  // for (AttributeIF attribute : relationshipDAO.getAttributeArrayIF())
  // {
  // if (attribute instanceof AttributeStructIF)
  // {
  // AttributeStructIF struct = (AttributeStructIF) attribute;
  // Struct bStruct = Struct.instantiate(relationship, struct.getName());
  // String typeName = struct.getDefiningClassType() +
  // TypeGeneratorInfo.BASE_SUFFIX;
  //
  // Class<?> c = LoaderDecorator.load(typeName);
  // String structName =
  // CommonGenerationUtil.lowerFirstCharacter(attribute.getName());
  //
  // Field field = c.getDeclaredField(structName);
  // field.setAccessible(true);
  // field.set(relationship, bStruct);
  // }
  // }
  // }
  // else
  // {
  // relationship = new EdgeObject(relationshipDAO);
  // }
  // }
  // catch (RunwayException d)
  // {
  // // We want our own exceptions to pass through
  // throw d;
  // }
  // catch (Exception e)
  // {
  // // And we want to wrap Sun's exceptions in our own
  // throw new ClassLoaderException(MdEdgeObjectDAO.getMdEdgeObjectDAO(type),
  // e);
  // }
  //
  // relationship.setGraphObjectDAO(relationshipDAO);
  // return relationship;
  // }
}
