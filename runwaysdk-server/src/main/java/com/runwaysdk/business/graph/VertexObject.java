package com.runwaysdk.business.graph;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.business.ClassLoaderException;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.VertexObjectDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
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

  /**
   * Default visibility is on purpose: we don't want all generated classes to
   * see this method.
   * 
   * @return the GraphObjectDAO
   */
  VertexObjectDAO getGraphObjectDAO()
  {
    return (VertexObjectDAO) super.getGraphObjectDAO();
  }

  @Override
  protected String getDeclaredType()
  {
    return VertexObject.class.getName();
  }

  public void addChild(VertexObject child, String edgeType)
  {
    MdEdgeDAOIF mdEdge = MdEdgeDAO.getMdEdgeDAO(edgeType);

    this.addChild(child, mdEdge);
  }

  public void addChild(VertexObject child, MdEdgeDAOIF mdEdge)
  {
    this.getGraphObjectDAO().addChild(child.getGraphObjectDAO(), mdEdge);
  }

  public void removeChild(VertexObject child, String edgeType)
  {
    MdEdgeDAOIF mdEdge = MdEdgeDAO.getMdEdgeDAO(edgeType);

    this.removeChild(child, mdEdge);
  }

  public void removeChild(VertexObject child, MdEdgeDAOIF mdEdge)
  {
    this.getGraphObjectDAO().removeChild(child.getGraphObjectDAO(), mdEdge);
  }

  public List<VertexObject> getChildren(String edgeType)
  {
    MdEdgeDAOIF mdEdge = MdEdgeDAO.getMdEdgeDAO(edgeType);

    return this.getChildren(mdEdge);
  }

  public List<VertexObject> getChildren(MdEdgeDAOIF mdEdge)
  {
    List<VertexObjectDAOIF> children = this.getGraphObjectDAO().getChildren(mdEdge);

    return this.convert(children);
  }

  public void addParent(VertexObject parent, String edgeType)
  {
    MdEdgeDAOIF mdEdge = MdEdgeDAO.getMdEdgeDAO(edgeType);

    this.addParent(parent, mdEdge);
  }

  public void addParent(VertexObject parent, MdEdgeDAOIF mdEdge)
  {
    this.getGraphObjectDAO().addParent(parent.getGraphObjectDAO(), mdEdge);
  }

  public void removeParent(VertexObject parent, String edgeType)
  {
    MdEdgeDAOIF mdEdge = MdEdgeDAO.getMdEdgeDAO(edgeType);

    this.removeParent(parent, mdEdge);
  }

  public void removeParent(VertexObject parent, MdEdgeDAOIF mdEdge)
  {
    this.getGraphObjectDAO().removeParent(parent.getGraphObjectDAO(), mdEdge);
  }

  public List<VertexObject> getParents(String edgeType)
  {
    MdEdgeDAOIF mdEdge = MdEdgeDAO.getMdEdgeDAO(edgeType);

    return this.getParents(mdEdge);
  }

  public List<VertexObject> getParents(MdEdgeDAOIF mdEdge)
  {
    List<VertexObjectDAOIF> parents = this.getGraphObjectDAO().getParents(mdEdge);

    return this.convert(parents);
  }

  protected List<VertexObject> convert(List<VertexObjectDAOIF> children)
  {
    LinkedList<VertexObject> list = new LinkedList<VertexObject>();

    for (VertexObjectDAOIF child : children)
    {
      list.add(VertexObject.instantiate((VertexObjectDAO) child));
    }

    return list;
  }

  public static VertexObject get(String type, String oid)
  {
    return VertexObject.get(MdVertexDAO.getMdVertexDAO(type), oid);
  }

  public static VertexObject get(MdVertexDAOIF mdVertexDAO, String oid)
  {
    VertexObjectDAOIF dao = VertexObjectDAO.get(mdVertexDAO, oid);

    if (dao != null)
    {
      return instantiate((VertexObjectDAO) dao);
    }

    return null;
  }

  public static VertexObject instantiate(VertexObjectDAO vertexDAO)
  {
    MdVertexDAOIF mdVertexDAO = vertexDAO.getMdClassDAO();

    if (mdVertexDAO.isGenerateSource())
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

    return new VertexObject((VertexObjectDAO) vertexDAO);
  }

}
