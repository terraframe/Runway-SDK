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

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.AttributeCharacterParseException;
import com.runwaysdk.AttributeIntegerParseException;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.graph.attributes.AttributeCharacter;
import com.runwaysdk.dataaccess.graph.attributes.AttributeLocalEmbedded;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEmbeddedDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalCharacterEmbeddedDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.AttributeLineStringParseException;
import com.runwaysdk.gis.AttributeMultiLineStringParseException;
import com.runwaysdk.gis.AttributeMultiPointParseException;
import com.runwaysdk.gis.AttributeMultiPolygonParseException;
import com.runwaysdk.gis.AttributePointParseException;
import com.runwaysdk.gis.AttributePolygonParseException;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeLineStringDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiLineStringDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPolygonDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePolygonDAO;
import com.runwaysdk.session.Request;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class VertexObjectDAODeleteTest
{
  private static MdVertexDAO             mdVertexDAO;

  private static MdAttributeCharacterDAO mdCharacterAttribute;

  @Request
  @BeforeClass
  public static void classSetup()
  {
    LocalProperties.setSkipCodeGenAndCompile(true);

    TestFixtureFactory.deleteMdClass(TestFixConst.TEST_VERTEX1_TYPE);

    classSetup_Transaction();
  }

  @Transaction
  private static void classSetup_Transaction()
  {
    mdVertexDAO = TestFixtureFactory.createMdVertex();
    mdVertexDAO.apply();
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
  }

  @Request
  @Test
  public void testDeleteRequiredCharacterAttributeWithData()
  {
    try
    {
      mdCharacterAttribute = TestFixtureFactory.addCharacterAttribute(mdVertexDAO);
      mdCharacterAttribute.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
      mdCharacterAttribute.apply();

      String attributeName = mdCharacterAttribute.definesAttribute();

      VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance(mdVertexDAO.definesType());

      Assert.assertNotNull(vertexDAO.getAttributeIF(attributeName));

      String value = "Test Value";

      vertexDAO.setValue(attributeName, value);

      Assert.assertEquals(value, vertexDAO.getObjectValue(attributeName));

      // Test create
      vertexDAO.apply();

      VertexObjectDAOIF test = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test);

      Assert.assertEquals(value, test.getObjectValue(attributeName));

      mdCharacterAttribute.delete();

      mdCharacterAttribute = null;

      VertexObjectDAOIF test2 = VertexObjectDAO.get(mdVertexDAO, vertexDAO.getOid());

      Assert.assertNotNull(test2);
    }
    finally
    {
//      if (mdCharacterAttribute != null)
//      {
//        mdCharacterAttribute.delete();
//      }
    }
  }

}
