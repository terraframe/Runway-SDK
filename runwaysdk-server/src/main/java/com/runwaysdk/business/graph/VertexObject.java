package com.runwaysdk.business.graph;

import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.VertexObjectDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

public class VertexObject extends GraphObject
{

  public VertexObject()
  {
    super();
    setGraphObjectDAO(VertexObjectDAO.newInstance(getDeclaredType()));
  }

  public VertexObject(String type)
  {
    super();
    setGraphObjectDAO(VertexObjectDAO.newInstance(type));
  }

  VertexObject(VertexObjectDAO vertexDAO)
  {
    super();
    setGraphObjectDAO(vertexDAO);
  }

  @Override
  protected String getDeclaredType()
  {
    return VertexObject.class.getName();
  }

  public static VertexObject get(MdVertexDAO mdVertexDAO, String oid)
  {
    VertexObjectDAOIF dao = VertexObjectDAO.get(mdVertexDAO, oid);

    return new VertexObject((VertexObjectDAO) dao);
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
