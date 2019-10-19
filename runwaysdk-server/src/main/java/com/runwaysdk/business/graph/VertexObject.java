package com.runwaysdk.business.graph;

import java.lang.reflect.Constructor;

import com.runwaysdk.business.ClassLoaderException;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.VertexObjectDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.generation.loader.LoaderDecorator;

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

    if (dao != null)
    {
      if (mdVertexDAO.isGenerateSource())
      {
        return instantiate((VertexObjectDAO) dao);
      }
      else
      {
        return new VertexObject((VertexObjectDAO) dao);
      }
    }

    return null;
  }

  private static VertexObject instantiate(VertexObjectDAO vertexDAO)
  {
    VertexObject object;

    try
    {
      Class<?> clazz = LoaderDecorator.load(vertexDAO.getType());

      Constructor<?> con = clazz.getConstructor();
      object = (VertexObject) con.newInstance();
      object.setGraphObjectDAO(vertexDAO);
    }
    catch (Exception e)
    {
      throw new ClassLoaderException(vertexDAO.getMdClassDAO(), e);
    }

    return object;
  }

}
