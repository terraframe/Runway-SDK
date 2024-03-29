/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.business.graph.generation;

import org.junit.Test;

import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeLineStringDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiLineStringDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPolygonDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePolygonDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeShapeDAO;
import com.runwaysdk.session.Request;

public class GeneratorTest
{
  private static MdVertexDAO                   mdParentDAO;

  private static MdVertexDAO                   mdChildDAO;

  private static MdEdgeDAO                     mdEdgeDAO;

  private static MdBusinessDAO                 mdBusinessDAO;

  private static MdBusinessDAO                 mdEnumMasterDAO;

  private static MdEnumerationDAO              mdEnumerationDAO;

  private static MdAttributeCharacterDAO       mdCharacterAttribute;

  private static MdAttributeIntegerDAO         mdIntegerAttribute;

  private static MdAttributeLongDAO            mdLongAttribute;

  private static MdAttributeFloatDAO           mdFloatAttribute;

  private static MdAttributeDoubleDAO          mdDoubleAttribute;

  private static MdAttributeBooleanDAO         mdBooleanAttribute;

  private static MdAttributeDateDAO            mdDateAttribute;

  private static MdAttributeDateTimeDAO        mdDateTimeAttribute;

  private static MdAttributeTimeDAO            mdTimeAttribute;

  private static MdAttributePointDAO           mdPointAttribute;

  private static MdAttributePolygonDAO         mdPolygonAttribute;

  private static MdAttributeShapeDAO           mdShapeAttribute;

  private static MdAttributeLineStringDAO      mdLineStringAttribute;

  private static MdAttributeMultiPointDAO      mdMultiPointAttribute;

  private static MdAttributeMultiPolygonDAO    mdMultiPolygonAttribute;

  private static MdAttributeMultiLineStringDAO mdMultiLineStringAttribute;

  private static MdAttributeReferenceDAO       mdReferenceAttribute;

  private static MdAttributeEnumerationDAO     mdEnumerationAttribute;

  @Transaction
  protected static void classSetup_Transaction()
  {
    mdBusinessDAO = TestFixtureFactory.createMdBusiness1();
    mdBusinessDAO.apply();

    mdEnumMasterDAO = TestFixtureFactory.createEnumClass1();
    mdEnumMasterDAO.apply();

    mdEnumerationDAO = TestFixtureFactory.createMdEnumeration1(mdEnumMasterDAO);
    mdEnumerationDAO.apply();

    mdParentDAO = TestFixtureFactory.createMdVertex("TestParent");
    mdParentDAO.apply();

    mdChildDAO = TestFixtureFactory.createMdVertex("TestChild");
    mdChildDAO.apply();

    mdEdgeDAO = TestFixtureFactory.createMdEdge(mdParentDAO, mdChildDAO);
    mdEdgeDAO.apply();

    mdCharacterAttribute = TestFixtureFactory.addCharacterAttribute(mdParentDAO);
    mdCharacterAttribute.apply();

    mdIntegerAttribute = TestFixtureFactory.addIntegerAttribute(mdParentDAO);
    mdIntegerAttribute.apply();

    mdLongAttribute = TestFixtureFactory.addLongAttribute(mdParentDAO);
    mdLongAttribute.apply();

    mdFloatAttribute = TestFixtureFactory.addFloatAttribute(mdParentDAO);
    mdFloatAttribute.apply();

    mdDoubleAttribute = TestFixtureFactory.addDoubleAttribute(mdParentDAO);
    mdDoubleAttribute.apply();

    mdBooleanAttribute = TestFixtureFactory.addBooleanAttribute(mdParentDAO);
    mdBooleanAttribute.apply();

    mdDateAttribute = TestFixtureFactory.addDateAttribute(mdParentDAO);
    mdDateAttribute.apply();

    mdDateTimeAttribute = TestFixtureFactory.addDateTimeAttribute(mdParentDAO);
    mdDateTimeAttribute.apply();

    mdTimeAttribute = TestFixtureFactory.addTimeAttribute(mdParentDAO);
    mdTimeAttribute.apply();

    mdPointAttribute = TestFixtureFactory.addPointAttribute(mdParentDAO);
    mdPointAttribute.apply();

    mdShapeAttribute = TestFixtureFactory.addShapeAttribute(mdParentDAO);
    mdShapeAttribute.apply();
    
    mdPolygonAttribute = TestFixtureFactory.addPolygonAttribute(mdParentDAO);
    mdPolygonAttribute.apply();

    mdLineStringAttribute = TestFixtureFactory.addLineStringAttribute(mdParentDAO);
    mdLineStringAttribute.apply();

    mdMultiPointAttribute = TestFixtureFactory.addMultiPointAttribute(mdParentDAO);
    mdMultiPointAttribute.apply();

    mdMultiPolygonAttribute = TestFixtureFactory.addMultiPolygonAttribute(mdParentDAO);
    mdMultiPolygonAttribute.apply();

    mdMultiLineStringAttribute = TestFixtureFactory.addMultiLineStringAttribute(mdParentDAO);
    mdMultiLineStringAttribute.apply();

    mdReferenceAttribute = TestFixtureFactory.addReferenceAttribute(mdParentDAO, mdBusinessDAO);
    mdReferenceAttribute.apply();

    mdEnumerationAttribute = TestFixtureFactory.addEnumerationAttribute(mdParentDAO, mdEnumerationDAO);
    mdEnumerationAttribute.apply();
  }

  @Transaction
  protected static void classTearDown_Transaction()
  {
    TestFixtureFactory.delete(mdEdgeDAO);
    TestFixtureFactory.delete(mdParentDAO);
    TestFixtureFactory.delete(mdChildDAO);
    TestFixtureFactory.delete(mdEnumerationDAO);
    TestFixtureFactory.delete(mdEnumMasterDAO);
    TestFixtureFactory.delete(mdBusinessDAO);
  }

  @Request
  @Test
  public void testGenerate()
  {
    try
    {
      classSetup_Transaction();
    }
    finally
    {
      classTearDown_Transaction();
    }
  }
}
