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

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.session.Request;

public class EdgeObjectDAOTest
{
  private static MdVertexDAO mdParentDAO;

  private static MdVertexDAO mdChildDAO;

  private static MdEdgeDAO   mdEdgeDAO;

  @Request
  @BeforeClass
  public static void classSetup()
  {
    LocalProperties.setSkipCodeGenAndCompile(true);

    mdParentDAO = TestFixtureFactory.createMdVertex("TestParent");
    mdParentDAO.apply();

    mdChildDAO = TestFixtureFactory.createMdVertex("TestChild");
    mdChildDAO.apply();

    mdEdgeDAO = TestFixtureFactory.createMdEdge(mdParentDAO, mdChildDAO);
    mdEdgeDAO.apply();

  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdEdgeDAO);
    TestFixtureFactory.delete(mdChildDAO);
    TestFixtureFactory.delete(mdParentDAO);

    LocalProperties.setSkipCodeGenAndCompile(false);
  }

  @Request
  @Test
  public void testAddChild()
  {
    VertexObjectDAO parent = VertexObjectDAO.newInstance(mdParentDAO);
    parent.apply();

    VertexObjectDAO child = VertexObjectDAO.newInstance(mdChildDAO);
    child.apply();

    parent.addChild(child, mdEdgeDAO).apply();

    List<VertexObjectDAOIF> children = parent.getChildren(mdEdgeDAO);

    Assert.assertEquals(1, children.size());

    VertexObjectDAOIF test = children.get(0);

    Assert.assertEquals(child.getOid(), test.getOid());

    List<EdgeObjectDAOIF> edges = parent.getChildEdges(mdEdgeDAO);

    for (EdgeObjectDAOIF edge : edges)
    {
      ( (EdgeObjectDAO) edge ).delete();
    }

    children = parent.getChildren(mdEdgeDAO);

    Assert.assertEquals(0, children.size());
  }

  @Request
  @Test
  public void testAddParent()
  {
    VertexObjectDAO parent = VertexObjectDAO.newInstance(mdParentDAO);
    parent.apply();

    VertexObjectDAO child = VertexObjectDAO.newInstance(mdChildDAO);
    child.apply();

    child.addParent(parent, mdEdgeDAO).apply();

    List<VertexObjectDAOIF> parents = child.getParents(mdEdgeDAO);

    Assert.assertEquals(1, parents.size());

    VertexObjectDAOIF test = parents.get(0);

    Assert.assertEquals(parent.getOid(), test.getOid());

    List<EdgeObjectDAOIF> edges = child.getParentEdges(mdEdgeDAO);

    for (EdgeObjectDAOIF edge : edges)
    {
      ( (EdgeObjectDAO) edge ).delete();
    }

    parents = child.getParents(mdEdgeDAO);

    Assert.assertEquals(0, parents.size());
  }
}
