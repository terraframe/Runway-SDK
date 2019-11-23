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

import com.runwaysdk.dataaccess.graph.EdgeObjectDAO;
import com.runwaysdk.dataaccess.graph.EdgeObjectDAOIF;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.VertexObjectDAOIF;

public class EdgeObject extends GraphObject
{
  /**
   * /** Constructor for new instances of Realtionships.
   *
   * @param parentOid
   *          Database oid of the parent
   * @param childOid
   *          Database oid of the child
   * @param type
   *          type the relationship
   */
  public EdgeObject(VertexObjectDAOIF parent, VertexObjectDAOIF child)
  {
    super();
    setGraphObjectDAO(EdgeObjectDAO.newInstance(parent, child, getDeclaredType()));
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
  public EdgeObject(VertexObjectDAOIF parent, VertexObjectDAOIF child, String type)
  {
    super();
    setGraphObjectDAO(EdgeObjectDAO.newInstance(parent, child, type));
  }

  /**
   * Default visibilty only, this constructor is used to create a EdgeObject for
   * a EdgeObjectDAO that is already in the database. All attribute values are
   * pulled from the EdgeObjectDAO parameter.
   *
   * @param edge
   */
  EdgeObject(EdgeObjectDAO edge)
  {
    super();
    setGraphObjectDAO(edge);
  }

  @Override
  public EdgeObjectDAO getGraphObjectDAO()
  {
    return (EdgeObjectDAO) super.getGraphObjectDAO();
  }

  public VertexObject getParent()
  {
    return VertexObject.instantiate((VertexObjectDAO) this.getGraphObjectDAO().getParent());
  }

  public VertexObject getChild()
  {
    return VertexObject.instantiate((VertexObjectDAO) this.getGraphObjectDAO().getChild());
  }

  protected String getDeclaredType()
  {
    return EdgeObject.class.getName();
  }

  public static EdgeObject instantiate(EdgeObjectDAOIF edgeDAO)
  {
    return new EdgeObject((EdgeObjectDAO) edgeDAO);
  }

}
