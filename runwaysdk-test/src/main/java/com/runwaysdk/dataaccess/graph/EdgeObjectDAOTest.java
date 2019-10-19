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

    parent.addChild(child, mdEdgeDAO);

    List<VertexObjectDAOIF> children = parent.getChildren(mdEdgeDAO);

    Assert.assertEquals(1, children.size());

    VertexObjectDAOIF test = children.get(0);

    Assert.assertEquals(child.getOid(), test.getOid());

    parent.removeChild(child, mdEdgeDAO);

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

    child.addParent(parent, mdEdgeDAO);

    List<VertexObjectDAOIF> parents = child.getParents(mdEdgeDAO);

    Assert.assertEquals(1, parents.size());

    VertexObjectDAOIF test = parents.get(0);

    Assert.assertEquals(parent.getOid(), test.getOid());

    child.removeParent(parent, mdEdgeDAO);

    parents = child.getParents(mdEdgeDAO);

    Assert.assertEquals(0, parents.size());
  }
}
