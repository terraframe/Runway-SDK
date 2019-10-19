package com.runwaysdk.business.graph;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.session.Request;

public class EdgeObjectTest
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
    mdParentDAO.setValue(MdVertexInfo.GENERATE_SOURCE, Boolean.FALSE.toString());
    mdParentDAO.apply();

    mdChildDAO = TestFixtureFactory.createMdVertex("TestChild");
    mdChildDAO.setValue(MdVertexInfo.GENERATE_SOURCE, Boolean.FALSE.toString());
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
    VertexObject parent = new VertexObject(mdParentDAO.definesType());
    parent.apply();

    VertexObject child = new VertexObject(mdChildDAO.definesType());
    child.apply();

    parent.addChild(child, mdEdgeDAO);

    List<VertexObject> children = parent.getChildren(mdEdgeDAO);

    Assert.assertEquals(1, children.size());

    VertexObject test = children.get(0);

    Assert.assertEquals(child.getOid(), test.getOid());

    parent.removeChild(child, mdEdgeDAO);

    children = parent.getChildren(mdEdgeDAO);

    Assert.assertEquals(0, children.size());
  }

  @Request
  @Test
  public void testAddParent()
  {
    VertexObject parent = new VertexObject(mdParentDAO.definesType());
    parent.apply();

    VertexObject child = new VertexObject(mdChildDAO.definesType());
    child.apply();

    child.addParent(parent, mdEdgeDAO);

    List<VertexObject> parents = child.getParents(mdEdgeDAO);

    Assert.assertEquals(1, parents.size());

    VertexObject test = parents.get(0);

    Assert.assertEquals(parent.getOid(), test.getOid());

    child.removeParent(parent, mdEdgeDAO);

    parents = child.getParents(mdEdgeDAO);

    Assert.assertEquals(0, parents.size());
  }

}
