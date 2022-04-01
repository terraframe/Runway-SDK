/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdAttributeClassificationInfo;
import com.runwaysdk.constants.graph.MdClassificationInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.metadata.MdAttributeClassificationDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdClassificationDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.AbstractClassification;

public class AbstractClassificationTest
{
  private static MdClassificationDAO          mdClassificationDAO;

  private static MdVertexDAO                  mdVertexDAO;

  private static MdAttributeClassificationDAO mdClassificationAttribute;

  private static VertexObjectDAO              root;

  private static VertexObjectDAO              child;

  private static VertexObjectDAO              other;

  private static VertexObjectDAO              grandchild;

  private static String                       CODE = "TT";

  @Request
  @BeforeClass
  public static void classSetup()
  {
    LocalProperties.setSkipCodeGenAndCompile(true);

    classSetup_Transaction();
  }

  @Transaction
  private static void classSetup_Transaction()
  {
    // Define the link class
    mdClassificationDAO = MdClassificationDAO.create(TestFixConst.TEST_PACKAGE, "TestClassification", "Test Classification");

    // Create nodes
    root = VertexObjectDAO.newInstance(mdClassificationDAO.getReferenceMdVertexDAO());
    root.setValue(AbstractClassification.CODE, "ROOT");
    root.apply();

    child = VertexObjectDAO.newInstance(mdClassificationDAO.getReferenceMdVertexDAO());
    child.setValue(AbstractClassification.CODE, CODE);
    child.apply();

    child.addParent(root, mdClassificationDAO.getReferenceMdEdgeDAO()).apply();

    other = VertexObjectDAO.newInstance(mdClassificationDAO.getReferenceMdVertexDAO());
    other.setValue(AbstractClassification.CODE, "OTHER");
    other.apply();

    other.addParent(root, mdClassificationDAO.getReferenceMdEdgeDAO()).apply();

    grandchild = VertexObjectDAO.newInstance(mdClassificationDAO.getReferenceMdVertexDAO());
    grandchild.setValue(AbstractClassification.CODE, "GRANDCHILD");
    grandchild.apply();

    grandchild.addParent(child, mdClassificationDAO.getReferenceMdEdgeDAO()).apply();

    mdClassificationDAO.setValue(MdClassificationInfo.ROOT, root.getOid());
    mdClassificationDAO.apply();

    mdVertexDAO = TestFixtureFactory.createMdVertex();
    mdVertexDAO.apply();

    mdClassificationAttribute = TestFixtureFactory.addClassificationAttribute(mdVertexDAO, mdClassificationDAO);
    mdClassificationAttribute.setValue(MdAttributeClassificationInfo.ROOT, root.getOid());
    mdClassificationAttribute.apply();
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    classTearDown_Transaction();

    LocalProperties.setSkipCodeGenAndCompile(false);
  }

  @Transaction
  public static void classTearDown_Transaction()
  {
    TestFixtureFactory.delete(mdVertexDAO);
    TestFixtureFactory.delete(mdClassificationDAO);
  }

  @Request
  @Test
  public void testFindMatchingClassification()
  {
    VertexObject result = AbstractClassification.findMatchingClassification(CODE, mdClassificationAttribute);

    Assert.assertNotNull(result);
    Assert.assertEquals(CODE, result.getObjectValue(AbstractClassification.CODE));
  }

  @Request
  @Test
  public void testGetByCode()
  {
    VertexObject result = AbstractClassification.get(CODE, mdClassificationDAO);

    Assert.assertNotNull(result);
    Assert.assertEquals(CODE, result.getObjectValue(AbstractClassification.CODE));
  }

  @Request
  @Test
  public void testIsChild()
  {
    Assert.assertTrue(VertexObjectDAO.isChild(root.getRID(), child.getRID(), mdClassificationDAO.getReferenceMdEdgeDAO()));
    Assert.assertFalse(VertexObjectDAO.isChild(other.getRID(), child.getRID(), mdClassificationDAO.getReferenceMdEdgeDAO()));
    Assert.assertTrue(VertexObjectDAO.isChild(root.getRID(), grandchild.getRID(), mdClassificationDAO.getReferenceMdEdgeDAO()));
    Assert.assertTrue(VertexObjectDAO.isChild(child.getRID(), grandchild.getRID(), mdClassificationDAO.getReferenceMdEdgeDAO()));
  }

}
