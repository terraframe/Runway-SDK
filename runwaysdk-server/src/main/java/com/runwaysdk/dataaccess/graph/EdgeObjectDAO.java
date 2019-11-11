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
import java.util.List;
import java.util.Map;

import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.database.AbstractInstantiationException;
import com.runwaysdk.dataaccess.graph.attributes.Attribute;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;

public class EdgeObjectDAO extends GraphObjectDAO implements EdgeObjectDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 801720319418056258L;

  private VertexObjectDAOIF parent;

  private VertexObjectDAOIF child;

  /**
   * The default constructor, does not set any attributes
   */
  public EdgeObjectDAO()
  {
    super();
  }

  /**
   *
   */
  public EdgeObjectDAO(Map<String, Attribute> attributeMap, MdEdgeDAOIF mdEdgeDAOIF, VertexObjectDAOIF parent, VertexObjectDAOIF child)
  {
    super(attributeMap, mdEdgeDAOIF);

    this.parent = parent;
    this.child = child;
  }

  /**
   * Returns a {@link MdEdgeDAOIF} that defines this Component's class.
   *
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   *
   * @return a {@link MdEdgeDAOIF} that defines this Component's class.
   */
  @Override
  public MdEdgeDAOIF getMdClassDAO()
  {
    return (MdEdgeDAOIF) super.getMdClassDAO();
  }

  public MdEdgeDAOIF getMdGraphClassDAO()
  {
    return (MdEdgeDAOIF) super.getMdGraphClassDAO();
  }

  /**
   * @return the parent
   */
  public VertexObjectDAOIF getParent()
  {
    return parent;
  }

  /**
   * @param parent
   *          the parent to set
   */
  public void setParent(VertexObjectDAOIF parent)
  {
    this.parent = parent;
  }

  /**
   * @return the child
   */
  public VertexObjectDAOIF getChild()
  {
    return child;
  }

  /**
   * @param child
   *          the child to set
   */
  public void setChild(VertexObjectDAOIF child)
  {
    this.child = child;
  }

  public static EdgeObjectDAO newInstance(VertexObjectDAOIF parent, VertexObjectDAOIF child, String edgeType)
  {
    // get the meta data for the given class
    MdEdgeDAOIF mdEdgeDAOIF = MdEdgeDAO.getMdEdgeDAO(edgeType);

    return newInstance(parent, child, mdEdgeDAOIF);
  }

  /**
   * Returns a new {@link EdgeObjectDAO} instance of the given class name.
   * Default values are assigned attributes if specified by the metadata.
   * 
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("")
   * 
   * @param parent
   *          TODO
   * @param child
   *          TODO
   * @param mdEdgeDAOIF
   *          Class name of the new BusinessDAO to instantiate
   * 
   * @return new {@link EdgeObjectDAO} of the given type
   * @throws DataAccessException
   *           if the given class name is abstract
   * @throws DataAccessException
   *           if metadata is not defined for the given class name
   */
  public static EdgeObjectDAO newInstance(VertexObjectDAOIF parent, VertexObjectDAOIF child, MdEdgeDAOIF mdEdgeDAOIF)
  {
    if (mdEdgeDAOIF.isAbstract())
    {
      String errMsg = "Class [" + mdEdgeDAOIF.definesType() + "] is abstract and cannot be instantiated";
      throw new AbstractInstantiationException(errMsg, mdEdgeDAOIF);
    }

    Hashtable<String, Attribute> attributeMap = new Hashtable<String, Attribute>();

    // get list of all classes in inheritance relationship
    List<? extends MdEdgeDAOIF> superMdEdgeList = mdEdgeDAOIF.getSuperClasses();
    superMdEdgeList.forEach(md -> attributeMap.putAll(GraphObjectDAO.createRecordsForEntity(md)));

    EdgeObjectDAO edgeObjectDAO = new EdgeObjectDAO(attributeMap, mdEdgeDAOIF, parent, child);

    attributeMap.values().forEach(a -> a.setContainingComponent(edgeObjectDAO));

    edgeObjectDAO.setIsNew(true);

    edgeObjectDAO.setAppliedToDB(false);

    return edgeObjectDAO;
  }
}
