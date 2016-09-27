/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.gis.dataaccess.io;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.io.dataDefinition.ExportMetadata;
import com.runwaysdk.dataaccess.io.dataDefinition.SAXExporter;
import com.runwaysdk.dataaccess.io.dataDefinition.SAXImporter;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.metadata.MdPackage;
import com.runwaysdk.gis.GISAbstractTest;
import com.runwaysdk.gis.TestConstants;
import com.runwaysdk.gis.constants.MdAttributeLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPointInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPolygonInfo;
import com.runwaysdk.gis.constants.MdAttributePointInfo;
import com.runwaysdk.gis.constants.MdAttributePolygonInfo;
import com.runwaysdk.gis.dataaccess.MdAttributeLineStringDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiLineStringDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPointDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPolygonDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributePointDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributePolygonDAOIF;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeLineStringDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiLineStringDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPolygonDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePolygonDAO;
import com.runwaysdk.session.Request;

public class GISSaxParseTest extends GISAbstractTest
{
  public static final String   JUNIT_PACKAGE = "temporary.junit.test.gis";
  public static final TypeInfo TEST_CLASS = new TypeInfo(JUNIT_PACKAGE, "SaxTestClass");

  /**
   * List of all XML files to test on
   */
  public static final String   POINT_ATTRIBUTE = TestConstants.XML_FILES + "pointAttributeTest.xml";

  /**
   * No setup needed non-Javadoc)
   *
   * @see junit.framework.TestCase#setUp()
   */
  @Before
  @Request
  public void saxSetUp() throws Exception
  {

  }

  /**
   * Delete all MetaData objects which were created in the class
   *
   * @see junit.framework.TestCase#tearDown()
   */
  @After
  @Request
  public void saxTearDown() throws Exception
  {
    new MdPackage(TEST_CLASS.getPackageName()).delete();
  }

  private MdBusinessDAO createMdBusiness1()
  {
    MdBusinessDAO testClassMdBusinessDAO = MdBusinessDAO.newInstance();
    testClassMdBusinessDAO = MdBusinessDAO.newInstance();
    testClassMdBusinessDAO.setValue(MdBusinessInfo.NAME,                   TEST_CLASS.getTypeName());
    testClassMdBusinessDAO.setValue(MdBusinessInfo.PACKAGE,                TEST_CLASS.getPackageName());
    testClassMdBusinessDAO.setStructValue(MdBusinessInfo.DISPLAY_LABEL,    MdAttributeLocalInfo.DEFAULT_LOCALE,      TEST_CLASS.getTypeName());
    testClassMdBusinessDAO.setStructValue(MdBusinessInfo.DESCRIPTION,      MdAttributeLocalInfo.DEFAULT_LOCALE,      TEST_CLASS.getTypeName());
    testClassMdBusinessDAO.setValue(MdBusinessInfo.REMOVE,                 MdAttributeBooleanInfo.TRUE);
    testClassMdBusinessDAO.setValue(MdBusinessInfo.EXTENDABLE,             MdAttributeBooleanInfo.TRUE);
    testClassMdBusinessDAO.setValue(MdBusinessInfo.ABSTRACT,               MdAttributeBooleanInfo.FALSE);
    testClassMdBusinessDAO.setValue(MdBusinessInfo.CACHE_ALGORITHM,        EntityCacheMaster.CACHE_NOTHING.getId());
    testClassMdBusinessDAO.apply();

    return testClassMdBusinessDAO;
  }

  private MdAttributePointDAO addPointAttribute(MdEntityDAO mdEntity)
  {
    MdAttributePointDAO mdAttributePointDAO = MdAttributePointDAO.newInstance();
    mdAttributePointDAO.setValue(MdAttributePointInfo.NAME, "testPoint");
    mdAttributePointDAO.setStructValue(MdAttributePointInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Point");
    mdAttributePointDAO.setStructValue(MdAttributePointInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Point");
    mdAttributePointDAO.setValue(MdAttributePointInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributePointDAO.setValue(MdAttributePointInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributePointDAO.setValue(MdAttributePointInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributePointDAO.setValue(MdAttributePointInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    mdAttributePointDAO.setValue(MdAttributePointInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributePointDAO.setValue(MdAttributePointInfo.SRID, "4326");
//    mdAttributePointDAO.setValue(MdAttributePointInfo.DIMENSION, "2");
    mdAttributePointDAO.setValue(MdAttributePointInfo.DEFINING_MD_CLASS, mdEntity.getId());
    mdAttributePointDAO.apply();

    return mdAttributePointDAO;
  }

  private MdAttributeLineStringDAO addLineStringAttribute(MdEntityDAO mdEntity)
  {
    MdAttributeLineStringDAO mdAttributeLineStringDAO = MdAttributeLineStringDAO.newInstance();
    mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.NAME, "testLineString");
    mdAttributeLineStringDAO.setStructValue(MdAttributeLineStringInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test LineString");
    mdAttributeLineStringDAO.setStructValue(MdAttributeLineStringInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test LineString");
    mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.SRID, "4326");
//    mdAttributePointDAO.setValue(MdAttributeLineStringInfo.DIMENSION, "2");
    mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.DEFINING_MD_CLASS, mdEntity.getId());
    mdAttributeLineStringDAO.apply();

    return mdAttributeLineStringDAO;
  }

  private MdAttributePolygonDAO addPolygonAttribute(MdEntityDAO mdEntity)
  {
    MdAttributePolygonDAO mdAttributePolygonDAO = MdAttributePolygonDAO.newInstance();
    mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.NAME, "testPolygon");
    mdAttributePolygonDAO.setStructValue(MdAttributePolygonInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,"Test Polygon");
    mdAttributePolygonDAO.setStructValue(MdAttributePolygonInfo.DESCRIPTION,  MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Polygon");
    mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.SRID, "4326");
//    mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.DIMENSION, "2");
    mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.DEFINING_MD_CLASS, mdEntity.getId());
    mdAttributePolygonDAO.apply();

    return mdAttributePolygonDAO;
  }

  private MdAttributeMultiPointDAO addMultiPointAttribute(MdEntityDAO mdEntity)
  {
    MdAttributeMultiPointDAO mdAttributeMultiPointDAO = MdAttributeMultiPointDAO.newInstance();
    mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.NAME, "testMultiPoint");
    mdAttributeMultiPointDAO.setStructValue(MdAttributeMultiPointInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiPoint");
    mdAttributeMultiPointDAO.setStructValue(MdAttributeMultiPointInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiPoint");
    mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.SRID, "4326");
//    mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.DIMENSION, "2");
    mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.DEFINING_MD_CLASS, mdEntity.getId());
    mdAttributeMultiPointDAO.apply();

    return mdAttributeMultiPointDAO;
  }

  private MdAttributeMultiLineStringDAO addMultiLineStringAttribute(MdEntityDAO mdEntity)
  {
    MdAttributeMultiLineStringDAO mdAttributeMultiLineStringDAO = MdAttributeMultiLineStringDAO.newInstance();
    mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.NAME, "testMultiLineString");
    mdAttributeMultiLineStringDAO.setStructValue(MdAttributeMultiLineStringInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiLineString");
    mdAttributeMultiLineStringDAO.setStructValue(MdAttributeMultiLineStringInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiLineString");
    mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.SRID, "4326");
//    mdAttributeMultiPointDAO.setValue(MdAttributeMultiLineStringInfo.DIMENSION, "2");
    mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.DEFINING_MD_CLASS, mdEntity.getId());
    mdAttributeMultiLineStringDAO.apply();

    return mdAttributeMultiLineStringDAO;
  }

  private MdAttributeMultiPolygonDAO addMultiPolygonAttribute(MdEntityDAO mdEntity)
  {
    MdAttributeMultiPolygonDAO mdAttributeMultiPolygonDAO = MdAttributeMultiPolygonDAO.newInstance();
    mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.NAME, "testMultiPolygon");
    mdAttributeMultiPolygonDAO.setStructValue(MdAttributeMultiPolygonInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiPolygon");
    mdAttributeMultiPolygonDAO.setStructValue(MdAttributeMultiPolygonInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiPolygon");
    mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.SRID, "4326");
//    mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.DIMENSION, "2");
    mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.DEFINING_MD_CLASS, mdEntity.getId());
    mdAttributeMultiPolygonDAO.apply();

    return mdAttributeMultiPolygonDAO;
  }

  /**
   * Test setting of attributes on a blob datatype
   */
  @Test
  @Request
  public void testCreatePoint()
  {
    MdBusinessDAO mdBusiness1 = createMdBusiness1();
    mdBusiness1.apply();

    addPointAttribute(mdBusiness1).apply();

    SAXExporter.export("test.xml", TestConstants.DATATYPE_GIS_XSD, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    mdBusiness1 = MdBusinessDAO.get(mdBusiness1.getId()).getBusinessDAO();
    mdBusiness1.delete();
    
    SAXImporter.runImport(new File("test.xml"), TestConstants.DATATYPE_GIS_XSD);

    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(TEST_CLASS.getType());
    MdAttributePointDAOIF mdAttributePointDAOIF = (MdAttributePointDAOIF)mdBusinessIF.definesAttribute("testPoint");
    AttributeEnumerationIF index = (AttributeEnumerationIF) mdAttributePointDAOIF.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    assertEquals("Test Point", mdAttributePointDAOIF.getStructValue(MdAttributePointInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals("4326", mdAttributePointDAOIF.getValue(MdAttributePointInfo.SRID));
    assertEquals("2", mdAttributePointDAOIF.getValue(MdAttributePointInfo.DIMENSION));

    assertEquals(MdAttributeBooleanInfo.FALSE, mdAttributePointDAOIF.getValue(MdAttributePointInfo.IMMUTABLE));
    assertEquals(MdAttributeBooleanInfo.FALSE, mdAttributePointDAOIF.getValue(MdAttributePointInfo.REQUIRED));
    assertEquals(MdAttributeBooleanInfo.FALSE, mdAttributePointDAOIF.getValue(MdAttributePointInfo.REMOVE));
    assertEquals("Test Point", mdAttributePointDAOIF.getStructValue(MdAttributePointInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals(index.dereference()[0].getId(), IndexTypes.NO_INDEX.getId());
  }


  /**
   * Test setting of attributes on a blob datatype
   */
  @Test
  @Request
  public void testCreateLineString()
  {
    MdBusinessDAO mdBusiness1 = createMdBusiness1();
    mdBusiness1.apply();

    addLineStringAttribute(mdBusiness1).apply();

    SAXExporter.export("test.xml", TestConstants.DATATYPE_GIS_XSD, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    mdBusiness1 = MdBusinessDAO.get(mdBusiness1.getId()).getBusinessDAO();
    mdBusiness1.delete();

    SAXImporter.runImport(new File("test.xml"), TestConstants.DATATYPE_GIS_XSD);

    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(TEST_CLASS.getType());
    MdAttributeLineStringDAOIF mdAttributeLineStringDAOIF = (MdAttributeLineStringDAOIF)mdBusinessIF.definesAttribute("testLineString");
    AttributeEnumerationIF index = (AttributeEnumerationIF) mdAttributeLineStringDAOIF.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    assertEquals("Test LineString", mdAttributeLineStringDAOIF.getStructValue(MdAttributeLineStringInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals("4326", mdAttributeLineStringDAOIF.getValue(MdAttributeLineStringInfo.SRID));
    assertEquals("2", mdAttributeLineStringDAOIF.getValue(MdAttributeLineStringInfo.DIMENSION));

    assertEquals(MdAttributeBooleanInfo.FALSE, mdAttributeLineStringDAOIF.getValue(MdAttributeLineStringInfo.IMMUTABLE));
    assertEquals(MdAttributeBooleanInfo.FALSE, mdAttributeLineStringDAOIF.getValue(MdAttributeLineStringInfo.REQUIRED));
    assertEquals(MdAttributeBooleanInfo.FALSE, mdAttributeLineStringDAOIF.getValue(MdAttributeLineStringInfo.REMOVE));
    assertEquals("Test LineString", mdAttributeLineStringDAOIF.getStructValue(MdAttributeLineStringInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals(index.dereference()[0].getId(), IndexTypes.NO_INDEX.getId());
  }

  /**
   * Test setting of attributes on a blob datatype
   */
  @Test
  @Request
  public void testCreatePolygon()
  {
    MdBusinessDAO mdBusiness1 = createMdBusiness1();
    mdBusiness1.apply();

    addPolygonAttribute(mdBusiness1).apply();

    SAXExporter.export("target/test.xml", TestConstants.DATATYPE_GIS_XSD, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    mdBusiness1 = MdBusinessDAO.get(mdBusiness1.getId()).getBusinessDAO();
    mdBusiness1.delete();

    SAXImporter.runImport(new File("target/test.xml"), TestConstants.DATATYPE_GIS_XSD);

    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(TEST_CLASS.getType());
    MdAttributePolygonDAOIF mdAttributePolygonDAOIF = (MdAttributePolygonDAOIF)mdBusinessIF.definesAttribute("testPolygon");
    AttributeEnumerationIF index = (AttributeEnumerationIF) mdAttributePolygonDAOIF.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    assertEquals("Test Polygon", mdAttributePolygonDAOIF.getStructValue(MdAttributePolygonInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals("4326", mdAttributePolygonDAOIF.getValue(MdAttributePolygonInfo.SRID));
    assertEquals("2", mdAttributePolygonDAOIF.getValue(MdAttributePolygonInfo.DIMENSION));

    assertEquals(MdAttributeBooleanInfo.FALSE, mdAttributePolygonDAOIF.getValue(MdAttributePolygonInfo.IMMUTABLE));
    assertEquals(MdAttributeBooleanInfo.FALSE, mdAttributePolygonDAOIF.getValue(MdAttributePolygonInfo.REQUIRED));
    assertEquals(MdAttributeBooleanInfo.FALSE, mdAttributePolygonDAOIF.getValue(MdAttributePolygonInfo.REMOVE));
    assertEquals("Test Polygon", mdAttributePolygonDAOIF.getStructValue(MdAttributePolygonInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals(index.dereference()[0].getId(), IndexTypes.NO_INDEX.getId());
  }

  /**
   * Test setting of attributes on a blob datatype
   */
  @Test
  @Request
  public void testCreateMultiPoint()
  {
    MdBusinessDAO mdBusiness1 = createMdBusiness1();
    mdBusiness1.apply();

    addMultiPointAttribute(mdBusiness1).apply();

    SAXExporter.export("target/test.xml", TestConstants.DATATYPE_GIS_XSD, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    mdBusiness1 = MdBusinessDAO.get(mdBusiness1.getId()).getBusinessDAO();
    mdBusiness1.delete();

    SAXImporter.runImport(new File("target/test.xml"), TestConstants.DATATYPE_GIS_XSD);

    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(TEST_CLASS.getType());
    MdAttributeMultiPointDAOIF mdAttributeMultiPointDAOIF = (MdAttributeMultiPointDAOIF)mdBusinessIF.definesAttribute("testMultiPoint");
    AttributeEnumerationIF index = (AttributeEnumerationIF) mdAttributeMultiPointDAOIF.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    assertEquals("Test MultiPoint", mdAttributeMultiPointDAOIF.getStructValue(MdAttributeMultiPointInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals("4326", mdAttributeMultiPointDAOIF.getValue(MdAttributeMultiPointInfo.SRID));
    assertEquals("2", mdAttributeMultiPointDAOIF.getValue(MdAttributeMultiPointInfo.DIMENSION));

    assertEquals(MdAttributeBooleanInfo.FALSE, mdAttributeMultiPointDAOIF.getValue(MdAttributeMultiPointInfo.IMMUTABLE));
    assertEquals(MdAttributeBooleanInfo.FALSE, mdAttributeMultiPointDAOIF.getValue(MdAttributeMultiPointInfo.REQUIRED));
    assertEquals(MdAttributeBooleanInfo.FALSE, mdAttributeMultiPointDAOIF.getValue(MdAttributeMultiPointInfo.REMOVE));
    assertEquals("Test MultiPoint", mdAttributeMultiPointDAOIF.getStructValue(MdAttributePolygonInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals(index.dereference()[0].getId(), IndexTypes.NO_INDEX.getId());
  }

  /**
   * Test setting of attributes on a blob datatype
   */
  @Test
  @Request
  public void testCreateMultiLineString()
  {
    MdBusinessDAO mdBusiness1 = createMdBusiness1();
    mdBusiness1.apply();

    addMultiLineStringAttribute(mdBusiness1).apply();

    SAXExporter.export("target/test.xml", TestConstants.DATATYPE_GIS_XSD, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    mdBusiness1 = MdBusinessDAO.get(mdBusiness1.getId()).getBusinessDAO();
    mdBusiness1.delete();

    SAXImporter.runImport(new File("target/test.xml"), TestConstants.DATATYPE_GIS_XSD);

    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(TEST_CLASS.getType());
    MdAttributeMultiLineStringDAOIF mdAttributeMultiLineStringDAOIF = (MdAttributeMultiLineStringDAOIF)mdBusinessIF.definesAttribute("testMultiLineString");
    AttributeEnumerationIF index = (AttributeEnumerationIF) mdAttributeMultiLineStringDAOIF.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    assertEquals("Test MultiLineString", mdAttributeMultiLineStringDAOIF.getStructValue(MdAttributeMultiLineStringInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals("4326", mdAttributeMultiLineStringDAOIF.getValue(MdAttributeMultiLineStringInfo.SRID));
    assertEquals("2", mdAttributeMultiLineStringDAOIF.getValue(MdAttributeMultiLineStringInfo.DIMENSION));

    assertEquals(MdAttributeBooleanInfo.FALSE, mdAttributeMultiLineStringDAOIF.getValue(MdAttributeMultiLineStringInfo.IMMUTABLE));
    assertEquals(MdAttributeBooleanInfo.FALSE, mdAttributeMultiLineStringDAOIF.getValue(MdAttributeMultiLineStringInfo.REQUIRED));
    assertEquals(MdAttributeBooleanInfo.FALSE, mdAttributeMultiLineStringDAOIF.getValue(MdAttributeMultiLineStringInfo.REMOVE));
    assertEquals("Test MultiLineString", mdAttributeMultiLineStringDAOIF.getStructValue(MdAttributeMultiLineStringInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals(index.dereference()[0].getId(), IndexTypes.NO_INDEX.getId());
  }

  /**
   * Test setting of attributes on a blob datatype
   */
  @Test
  @Request
  public void testCreateMultiPolygon()
  {
    MdBusinessDAO mdBusiness1 = createMdBusiness1();
    mdBusiness1.apply();

    addMultiPolygonAttribute(mdBusiness1).apply();

    SAXExporter.export("target/test.xml", TestConstants.DATATYPE_GIS_XSD, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness1 }));

    mdBusiness1 = MdBusinessDAO.get(mdBusiness1.getId()).getBusinessDAO();
    mdBusiness1.delete();

    SAXImporter.runImport(new File("target/test.xml"), TestConstants.DATATYPE_GIS_XSD);

    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(TEST_CLASS.getType());
    MdAttributeMultiPolygonDAOIF mdAttributeMultiPolygonDAOIF = (MdAttributeMultiPolygonDAOIF)mdBusinessIF.definesAttribute("testMultiPolygon");
    AttributeEnumerationIF index = (AttributeEnumerationIF) mdAttributeMultiPolygonDAOIF.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    assertEquals("Test MultiPolygon", mdAttributeMultiPolygonDAOIF.getStructValue(MdAttributeMultiPolygonInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals("4326", mdAttributeMultiPolygonDAOIF.getValue(MdAttributeMultiPolygonInfo.SRID));
    assertEquals("2", mdAttributeMultiPolygonDAOIF.getValue(MdAttributeMultiPolygonInfo.DIMENSION));

    assertEquals(MdAttributeBooleanInfo.FALSE, mdAttributeMultiPolygonDAOIF.getValue(MdAttributeMultiPolygonInfo.IMMUTABLE));
    assertEquals(MdAttributeBooleanInfo.FALSE, mdAttributeMultiPolygonDAOIF.getValue(MdAttributeMultiPolygonInfo.REQUIRED));
    assertEquals(MdAttributeBooleanInfo.FALSE, mdAttributeMultiPolygonDAOIF.getValue(MdAttributeMultiPolygonInfo.REMOVE));
    assertEquals("Test MultiPolygon", mdAttributeMultiPolygonDAOIF.getStructValue(MdAttributeMultiPolygonInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals(index.dereference()[0].getId(), IndexTypes.NO_INDEX.getId());
  }
}
