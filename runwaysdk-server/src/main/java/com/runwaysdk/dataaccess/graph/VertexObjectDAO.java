/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.graph;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.database.AbstractInstantiationException;
import com.runwaysdk.dataaccess.graph.attributes.Attribute;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

public class VertexObjectDAO extends GraphObjectDAO implements VertexObjectDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = -7425379312301977450L;

  /**
   * The default constructor, does not set any attributes
   */
  public VertexObjectDAO()
  {
    super();
  }

  /**
   * Constructs a {@link VertexObjectDAO} from the given hashtable of
   * Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> classType != null
   * 
   * @param attributeMap
   * @param classType
   */
  public VertexObjectDAO(Map<String, Attribute> attributeMap, MdVertexDAOIF mdVertexDAOIF)
  {
    super(attributeMap, mdVertexDAOIF);
  }

  /**
   * Returns a {@link MdVertexDAOIF} that defines this Component's class.
   *
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   *
   * @return a {@link MdVertexDAOIF} that defines this Component's class.
   */
  @Override
  public MdVertexDAOIF getMdClassDAO()
  {
    return (MdVertexDAOIF) super.getMdClassDAO();
  }

  public EdgeObjectDAO addChild(VertexObjectDAOIF child, String edgeType)
  {
    MdEdgeDAOIF mdEdge = MdEdgeDAO.getMdEdgeDAO(edgeType);

    return this.addChild(child, mdEdge);
  }

  public EdgeObjectDAO addChild(VertexObjectDAOIF child, MdEdgeDAOIF mdEdge)
  {
    return EdgeObjectDAO.newInstance(this, child, mdEdge);
  }

  public void removeChild(VertexObjectDAOIF child, String edgeType)
  {
    MdEdgeDAOIF mdEdge = MdEdgeDAO.getMdEdgeDAO(edgeType);

    this.removeChild(child, mdEdge);
  }

  public void removeChild(VertexObjectDAOIF child, MdEdgeDAOIF mdEdge)
  {
    List<EdgeObjectDAOIF> edges = this.getEdges(this, child, mdEdge);

    for (EdgeObjectDAOIF edge : edges)
    {
      ( (EdgeObjectDAO) edge ).delete();
    }
  }

  public List<VertexObjectDAOIF> getChildren(String edgeType)
  {
    MdEdgeDAOIF mdEdge = MdEdgeDAO.getMdEdgeDAO(edgeType);

    return this.getChildren(mdEdge);
  }

  public List<VertexObjectDAOIF> getChildren(MdEdgeDAOIF mdEdge)
  {
    if (this.isAppliedToDB())
    {
      GraphRequest request = GraphDBService.getInstance().getGraphDBRequest();

      return GraphDBService.getInstance().getChildren(request, this, mdEdge);
    }

    return new LinkedList<VertexObjectDAOIF>();
  }

  public List<EdgeObjectDAOIF> getChildEdges(String edgeType)
  {
    MdEdgeDAOIF mdEdge = MdEdgeDAO.getMdEdgeDAO(edgeType);

    return this.getChildEdges(mdEdge);
  }

  public List<EdgeObjectDAOIF> getChildEdges(MdEdgeDAOIF mdEdge)
  {
    if (this.isAppliedToDB())
    {
      GraphRequest request = GraphDBService.getInstance().getGraphDBRequest();

      return GraphDBService.getInstance().getChildEdges(request, this, mdEdge);
    }

    return new LinkedList<EdgeObjectDAOIF>();
  }

  public EdgeObjectDAO addParent(VertexObjectDAOIF parent, String edgeType)
  {
    MdEdgeDAOIF mdEdge = MdEdgeDAO.getMdEdgeDAO(edgeType);

    return this.addParent(parent, mdEdge);
  }

  public EdgeObjectDAO addParent(VertexObjectDAOIF parent, MdEdgeDAOIF mdEdge)
  {
    return EdgeObjectDAO.newInstance(parent, this, mdEdge);
  }

  public void removeParent(VertexObjectDAOIF parent, String edgeType)
  {
    MdEdgeDAOIF mdEdge = MdEdgeDAO.getMdEdgeDAO(edgeType);

    this.removeParent(parent, mdEdge);
  }

  public void removeParent(VertexObjectDAOIF parent, MdEdgeDAOIF mdEdge)
  {
    List<EdgeObjectDAOIF> edges = this.getEdges(parent, this, mdEdge);

    for (EdgeObjectDAOIF edge : edges)
    {
      ( (EdgeObjectDAO) edge ).delete();
    }
  }

  public List<VertexObjectDAOIF> getParents(String edgeType)
  {
    MdEdgeDAOIF mdEdge = MdEdgeDAO.getMdEdgeDAO(edgeType);

    return this.getParents(mdEdge);
  }

  public List<VertexObjectDAOIF> getParents(MdEdgeDAOIF mdEdge)
  {
    if (this.isAppliedToDB())
    {
      GraphRequest request = GraphDBService.getInstance().getGraphDBRequest();

      return GraphDBService.getInstance().getParents(request, this, mdEdge);
    }

    return new LinkedList<VertexObjectDAOIF>();
  }

  public List<EdgeObjectDAOIF> getParentEdges(String edgeType)
  {
    MdEdgeDAOIF mdEdge = MdEdgeDAO.getMdEdgeDAO(edgeType);

    return this.getParentEdges(mdEdge);
  }

  public List<EdgeObjectDAOIF> getParentEdges(MdEdgeDAOIF mdEdge)
  {
    if (this.isAppliedToDB())
    {
      GraphRequest request = GraphDBService.getInstance().getGraphDBRequest();

      return GraphDBService.getInstance().getParentEdges(request, this, mdEdge);
    }

    return new LinkedList<EdgeObjectDAOIF>();
  }

  public List<EdgeObjectDAOIF> getParentEdges(VertexObjectDAOIF parent, MdEdgeDAOIF mdEdge)
  {
    return getEdges(parent, this, mdEdge);
  }

  protected List<EdgeObjectDAOIF> getEdges(VertexObjectDAOIF parent, VertexObjectDAOIF child, MdEdgeDAOIF mdEdge)
  {
    if (this.isAppliedToDB())
    {
      GraphRequest request = GraphDBService.getInstance().getGraphDBRequest();

      return GraphDBService.getInstance().getEdges(request, parent, child, mdEdge);
    }

    return new LinkedList<EdgeObjectDAOIF>();
  }

  public static VertexObjectDAO newInstance(String vertexType)
  {
    // get the meta data for the given class
    MdVertexDAOIF mdVertexDAOIF = MdVertexDAO.getMdVertexDAO(vertexType);

    return newInstance(mdVertexDAOIF);
  }

  /**
   * Returns a new {@link VertexObjectDAO} instance of the given class name.
   * Default values are assigned attributes if specified by the metadata.
   * 
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("")
   * 
   * @param mdVertexDAOIF
   *          Class name of the new BusinessDAO to instantiate
   * @return new {@link VertexObjectDAO} of the given type
   * @throws DataAccessException
   *           if the given class name is abstract
   * @throws DataAccessException
   *           if metadata is not defined for the given class name
   */
  public static VertexObjectDAO newInstance(MdVertexDAOIF mdVertexDAOIF)
  {
    checkIsAbstract(mdVertexDAOIF);

    Hashtable<String, Attribute> attributeMap = getAttributeMap(mdVertexDAOIF);

    VertexObjectDAO vertexObjectDAO = new VertexObjectDAO(attributeMap, mdVertexDAOIF);

    initialize(attributeMap, vertexObjectDAO);

    return vertexObjectDAO;
  }

  /**
   * Checks if the defining type is abstract. If so, a {@link VectorObjectDAO}
   * cannot be instantiated.
   * 
   * @param mdVertexDAOIF
   * @throws AbstractInstantiationException
   */
  protected static void checkIsAbstract(MdVertexDAOIF mdVertexDAOIF)
  {
    if (mdVertexDAOIF.isAbstract())
    {
      String errMsg = "Class [" + mdVertexDAOIF.definesType() + "] is abstract and cannot be instantiated";
      throw new AbstractInstantiationException(errMsg, mdVertexDAOIF);
    }
  }

  /**
   * Returns an {@link Attribute} map to be used by a a {@link VertexObjectDAO}.
   * 
   * @param mdVertexDAOIF
   * @return
   */
  protected static Hashtable<String, Attribute> getAttributeMap(MdVertexDAOIF mdVertexDAOIF)
  {
    Hashtable<String, Attribute> attributeMap = new Hashtable<String, Attribute>();

    // get list of all classes in inheritance relationship
    List<? extends MdVertexDAOIF> superMdVertexList = mdVertexDAOIF.getSuperClasses();
    superMdVertexList.forEach(md -> attributeMap.putAll(GraphObjectDAO.createRecordsForEntity(md)));

    return attributeMap;
  }

  /**
   * Initializes the given {@link VertexObjectDAO} and {@link Attribute}
   * objects.
   * 
   * @param attributeMap
   * @param vertexObjectDAO
   */
  protected static void initialize(Hashtable<String, Attribute> attributeMap, VertexObjectDAO vertexObjectDAO)
  {
    attributeMap.values().forEach(a -> a.setContainingComponent(vertexObjectDAO));

    vertexObjectDAO.setIsNew(true);

    vertexObjectDAO.setAppliedToDB(false);
  }

  public static VertexObjectDAOIF get(MdVertexDAOIF mdVertexDAO, String oid)
  {
    GraphRequest request = GraphDBService.getInstance().getGraphDBRequest();

    return GraphDBService.getInstance().get(request, mdVertexDAO, oid);
  }

  public static boolean isChild(VertexObjectDAOIF root, VertexObjectDAOIF attributeRoot, MdEdgeDAOIF referenceMdEdgeDAO)
  {
    StringBuilder statement = new StringBuilder();
    statement.append("SELECT count(*) FROM (");
    statement.append(" TRAVERSE in('" + referenceMdEdgeDAO.getDBClassName() + "') FROM :child");
    statement.append(") WHERE @rid = :parent");

    GraphQuery<Long> query = new GraphQuery<Long>(statement.toString());
    query.setParameter("child", attributeRoot.getRID());
    query.setParameter("parent", root.getRID());

    Long result = query.getSingleResult();

    return ( result != null && result > 0 );
  }

}
