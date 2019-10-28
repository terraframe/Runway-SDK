package com.runwaysdk.business.graph;

import java.util.List;
import java.util.Locale;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.RunwayExceptionDTO;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.facade.Facade;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

public class VertexObjectPermissionTest
{
  private static MdVertexDAO             mdParentDAO;

  private static MdAttributeCharacterDAO mdCharacterAttribute;

  private static MdVertexDAO             mdChildDAO;

  private static MdEdgeDAO               mdEdgeDAO;

  private static UserDAO                 user;

  private static UserDAO                 noPermission;

  private static String                  sessionId;

  private static String                  noSessionId;

  @Request
  @BeforeClass
  public static void classSetup()
  {
    LocalProperties.setSkipCodeGenAndCompile(true);

    classSetup_Transaction();

    sessionId = Facade.login(user.getUsername(), TestFixConst.TEST_PASSWORD, new Locale[] { Locale.ENGLISH });
    noSessionId = Facade.login(noPermission.getUsername(), TestFixConst.TEST_PASSWORD, new Locale[] { Locale.ENGLISH });
  }

  @Transaction
  protected static void classSetup_Transaction()
  {
    mdParentDAO = TestFixtureFactory.createMdVertex("TestParent");
    mdParentDAO.setValue(MdVertexInfo.GENERATE_SOURCE, Boolean.FALSE.toString());
    mdParentDAO.apply();

    mdCharacterAttribute = TestFixtureFactory.addCharacterAttribute(mdParentDAO);
    mdCharacterAttribute.apply();

    mdChildDAO = TestFixtureFactory.createMdVertex("TestChild");
    mdChildDAO.setValue(MdVertexInfo.GENERATE_SOURCE, Boolean.FALSE.toString());
    mdChildDAO.apply();

    mdEdgeDAO = TestFixtureFactory.createMdEdge(mdParentDAO, mdChildDAO);
    mdEdgeDAO.apply();

    noPermission = TestFixtureFactory.createUser("NoPermission");
    noPermission.apply();

    user = TestFixtureFactory.createUser();
    user.apply();

    user.grantPermission(Operation.CREATE, mdParentDAO.getOid());
    user.grantPermission(Operation.WRITE, mdParentDAO.getOid());
    user.grantPermission(Operation.WRITE_ALL, mdParentDAO.getOid());
    user.grantPermission(Operation.DELETE, mdParentDAO.getOid());

    user.grantPermission(Operation.CREATE, mdChildDAO.getOid());
    user.grantPermission(Operation.WRITE, mdChildDAO.getOid());
    user.grantPermission(Operation.WRITE_ALL, mdChildDAO.getOid());
    user.grantPermission(Operation.DELETE, mdChildDAO.getOid());

    user.grantPermission(Operation.ADD_CHILD, mdEdgeDAO.getOid());
    user.grantPermission(Operation.ADD_PARENT, mdEdgeDAO.getOid());
    user.grantPermission(Operation.DELETE_CHILD, mdEdgeDAO.getOid());
    user.grantPermission(Operation.DELETE_PARENT, mdEdgeDAO.getOid());
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    Facade.logout(sessionId);
    Facade.logout(noSessionId);

    classTearDown_Transaction();

    LocalProperties.setSkipCodeGenAndCompile(false);
  }

  @Transaction
  protected static void classTearDown_Transaction()
  {
    TestFixtureFactory.delete(noPermission);
    TestFixtureFactory.delete(user);
    TestFixtureFactory.delete(mdEdgeDAO);
    TestFixtureFactory.delete(mdChildDAO);
    TestFixtureFactory.delete(mdParentDAO);
  }

  @Test(expected = RunwayExceptionDTO.class)
  public void testNoPermissionException()
  {
    testAddChild(noSessionId);
  }

  @Test
  public void testAddChild()
  {
    testAddChild(sessionId);
  }

  @Request(RequestType.SESSION)
  public void testAddChild(String sessionId)
  {
    VertexObject parent = new VertexObject(mdParentDAO.definesType());
    parent.apply();

    VertexObject child = new VertexObject(mdChildDAO.definesType());
    child.apply();

    parent.addChild(child, mdEdgeDAO);

    List<VertexObject> children = parent.getChildren(mdEdgeDAO, VertexObject.class);

    Assert.assertEquals(1, children.size());

    VertexObject test = children.get(0);

    Assert.assertEquals(child.getOid(), test.getOid());

    parent.removeChild(child, mdEdgeDAO);

    children = parent.getChildren(mdEdgeDAO, VertexObject.class);

    Assert.assertEquals(0, children.size());
  }

  @Test
  public void testAddParent()
  {
    testAddParent(sessionId);
  }

  @Request(RequestType.SESSION)
  public void testAddParent(String sessionId)
  {
    VertexObject parent = new VertexObject(mdParentDAO.definesType());
    parent.apply();

    VertexObject child = new VertexObject(mdChildDAO.definesType());
    child.apply();

    child.addParent(parent, mdEdgeDAO);

    List<VertexObject> parents = child.getParents(mdEdgeDAO, VertexObject.class);

    Assert.assertEquals(1, parents.size());

    VertexObject test = parents.get(0);

    Assert.assertEquals(parent.getOid(), test.getOid());

    child.removeParent(parent, mdEdgeDAO);

    parents = child.getParents(mdEdgeDAO, VertexObject.class);

    Assert.assertEquals(0, parents.size());
  }

  @Test
  public void testWrite()
  {
    testWrite(sessionId);
  }

  @Request(RequestType.SESSION)
  public void testWrite(String sessionId)
  {
    String attributeName = mdCharacterAttribute.definesAttribute();

    VertexObject parent = new VertexObject(mdParentDAO.definesType());
    parent.setValue(attributeName, "Test Value");
    parent.apply();

    VertexObject test = VertexObject.get(mdParentDAO, parent.getOid());
    test.setValue(attributeName, "Updated Value");
    test.apply();
  }

}
