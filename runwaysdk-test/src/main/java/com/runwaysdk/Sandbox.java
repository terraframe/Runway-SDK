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
package com.runwaysdk;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.graph.orientdb.OrientDBRequest;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.gis.dataaccess.metadata.graph.MdGeoVertexDAO;
import com.runwaysdk.session.Request;

/**
 * !!HEADS UP!!
 * 
 * !! DO NOT COMMIT YOUR RUNWAY JAVA METADATA TO THIS FILE ANYMORE !!
 * 
 * It is OK to move Runway metadata in and out of this file locally (for
 * purposes of running it), but it should NEVER BE COMMITTED here.
 * 
 * To commit your Runway metadata, create a new file with the name of your
 * feature (i.e. MdTable.java), and commit that file at:
 * runwaysdk-test/src/main/domain/archive/java
 * 
 * Why are we doing this?? Because this file has gotten to be a gigantic mess
 * and because we're losing track of our metadata. Some SQL diff files were
 * never committed and I had to dig through git history on this file to find the
 * Java source I needed to recreate the SQL diff files for DDMS.
 * 
 * Ideally your Java file should also be timestamped, i.e. :
 * (0001470948538281)MdTable.java
 * 
 * Don't forget to check in your sql diff to:
 * runwaysdk-test/src/main/domain/archive/sql
 * 
 * @author rrowlands
 *
 */
public class Sandbox
{
  @Request
  public static void main(String[] args)
  {
    long daysBetween = ChronoUnit.MONTHS.between(LocalDate.parse("2016-08-31"), LocalDate.parse("2016-12-20"));
    
    System.out.println(daysBetween);

    // UUID uuid1 = UUID.nameUUIDFromBytes( ("Test String" ).getBytes());
    // UUID uuid2 = UUID.nameUUIDFromBytes( ("Test String" ).getBytes());
    //
    // System.out.println(uuid1.toString());
    // System.out.println(uuid2.getMostSignificantBits()+"
    // "+uuid2.getLeastSignificantBits());

    // Versioning.run(new String[] {
    // "/home/terraframe/git/Runway-SDK/runwaysdk-test/src/main/domain",
    // XMLConstants.VERSION_XSD });

    // ServerProperties.setAllowModificationOfMdAttribute(true);
    //
    // try
    // {
    // Versioning.run(new String[] {
    // "/home/terraframe/git/Runway-SDK/runwaysdk-test/src/main/domain",
    // XMLConstants.VERSION_XSD });
    // }
    // finally
    // {
    // ServerProperties.setAllowModificationOfMdAttribute(false);
    // }

    // testCreateVertexClass();
    // testDeleteVertexClass();

    // addToMetadata();
    // System.out.println(UUID.randomUUID().toString());
    // inRequest();
    // testCreateDB();
    // testRequest();
    //
    // GraphDBService.getInstance().closeConnectionPool();
    // System.out.println("Finished the request for the test");

    // testCreateGeoVertexClass();
    // testDeleteeGeoVertexClass();

    // testEmbeddedClass();
  }

  private static void testEmbeddedClass()
  {
    // GraphRequest graphRequest =
    // GraphDBService.getInstance().getGraphDBRequest();
    // GraphRequest graphDDLRequest =
    // GraphDBService.getInstance().getDDLGraphDBRequest();
    //
    // OrientDBRequest ddlOrientDBRequest = (OrientDBRequest) graphDDLRequest;
    // ODatabaseSession db = ddlOrientDBRequest.getODatabaseSession();
    //
    // // make sure the DDL graph request is current on the active thread.
    // db.activateOnCurrentThread();
    //
    // OClass outerOClass = db.createVertexClass("OuterClass");
    //
    // // Add string to an inner class
    // OClass innerOClass = db.createVertexClass("InnerClass");
    // OProperty oProperty = innerOClass.createProperty("testChar",
    // OType.STRING);
    // oProperty.setMax("16");
    //
    // outerOClass.createProperty("innerClass", OType.EMBEDDED, innerOClass);

    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    OrientDBRequest orientDBRequest = (OrientDBRequest) graphRequest;
    ODatabaseSession db = orientDBRequest.getODatabaseSession();

    db.activateOnCurrentThread();

    OClass outerOClass = db.getClass("OuterClass");
    OClass innerOClass = db.getClass("InnerClass");

    OVertex outerVertex = db.newVertex(outerOClass);

    OVertex innerVertex = db.newVertex(innerOClass);
    innerVertex.setProperty("testChar", "Test Character");

    outerVertex.setProperty("innerClass", innerVertex);

    outerVertex.save();

    ORID orid = outerVertex.getIdentity();
    System.out.println(orid.toString());
    // String statement = "SELECT FROM OuterClass ORID = ?";
    String statement = "SELECT FROM [" + orid.toString() + "]";

    // try (OResultSet rs = db.query(statement, orid))
    try (OResultSet rs = db.query(statement))
    {
      if (rs.hasNext())
      {
        OResult result = rs.next();

        if (result.isElement())
        {
          OElement element = result.toElement();

          Object value = element.getProperty("innerClass");
          OVertex doc = (OVertex) value;

          System.out.println("Heads up: " + element.getIdentity().toString() + "  " + doc.getProperty("testChar"));

        }
      }
    }
  }

  private static void testCreateGeoVertexClass()
  {
    MdGeoVertexDAO mdGeoVertex = TestFixtureFactory.createMdGeoVertex("TestProvince");
    mdGeoVertex.apply();
  }

  private static void testDeleteeGeoVertexClass()
  {
    String geoVertexType = TestFixConst.TEST_PACKAGE + ".TestProvince";
    MdGeoVertexDAO.getMdGeoVertexDAO(geoVertexType).getBusinessDAO().delete();
  }
  // TestFixConst.TEST_PACKAGE

  private static void testCreateVertexClass()
  {
    MdVertexDAO mdVertexDAO = MdVertexDAO.newInstance();
    mdVertexDAO.setValue(MdVertexInfo.NAME, "TestVertexClass");
    mdVertexDAO.setValue(MdVertexInfo.PACKAGE, "test");
    mdVertexDAO.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Vertex Class");
    mdVertexDAO.setValue(MdVertexInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdVertexDAO.setValue(MdTypeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdVertexDAO.apply();
  }

  private static void testDeleteVertexClass()
  {
    MdVertexDAOIF mdVertexDAOIF = MdVertexDAO.getMdVertexDAO("test.TestVertexClass");
    mdVertexDAOIF.getBusinessDAO().delete();
  }

  @Request
  private static void testCreateDB()
  {
    // this is put here because the relational database and the graph database
    // (if any) need to be created together
    GraphDBService.getInstance().initializeDB();
  }

  // Heads up: clean up
  // @Request
  // private static void testRequest()
  // {
  // System.out.println("\nStarting Request!");
  //
  // GraphRequest graphDBRequest =
  // GraphDBService.getInstance().getGraphDBRequest();
  // graphDBRequest.close();
  //
  // testTransaction();
  //
  // System.out.println("\nRequest executed!!!!");
  // }
  // @Transaction
  // private static void testTransaction()
  // {
  // GraphRequest graphRequest =
  // GraphDBService.getInstance().getGraphDBRequest();
  // GraphRequest graphDDLRequest =
  // GraphDBService.getInstance().getDDLGraphDBRequest();
  //
  // CreateVertexClassCommand createCommand = new
  // CreateVertexClassCommand(graphRequest, graphDDLRequest, false,
  // "testClass4");
  // createCommand.doIt();
  //
  // OrientDBRequest orientDBRequest = (OrientDBRequest)graphRequest;
  // ODatabaseSession db = orientDBRequest.getODatabaseSession();
  //
  // OVertex result = db.newVertex("testClass1");
  // result.save();
  //
  // createCommand = new CreateVertexClassCommand(graphRequest, graphDDLRequest,
  // false, "testClass2");
  // createCommand.doIt();
  //
  // result = db.newVertex("testClass2");
  // result.save();
  //
  // if (1 == 1)
  // {
  // throw new RuntimeException("Test Exception");
  // }
  //
  // System.out.println("\nExecuting Transaction!");
  // }

  /*
   * @Request private static void addToMetadata() { addToMetadataTransaction();
   * }
   * 
   * @Transaction private static void addToMetadataTransaction() { // MdBusiness
   * mdBusiness = MdBusiness.getMdBusiness(MdBusinessInfo.CLASS); MdBusiness
   * mdBusiness = MdBusiness.getMdBusiness(MdClass.CLASS);
   * 
   * // DefaultAttribute.CODE - defined by GeoEntity geoId MdAttributeBoolean
   * mdAttrBool = new MdAttributeBoolean();
   * mdAttrBool.setAttributeName(MdClassInfo.GRAPH_CLASS);
   * mdAttrBool.getDisplayLabel().setValue("Graph Class");
   * mdAttrBool.getDescription().
   * setValue("Indicates whether this class is included in the graph");
   * mdAttrBool.setDefiningMdClass(mdBusiness); mdAttrBool.setRequired(true);
   * mdAttrBool.addIndexType(MdAttributeIndices.NO_INDEX);
   * mdAttrBool.setDefaultValue(false);
   * mdAttrBool.getPositiveDisplayLabel().setDefaultValue("Is a Graph Class");
   * mdAttrBool.getNegativeDisplayLabel().setDefaultValue("Is not a Graph Class"
   * ); mdAttrBool.apply(); }
   */
  /*
   * @Request private static void addToMetadata() { addToMetadataTransaction();
   * }
   * 
   * @Transaction private static void addToMetadataTransaction() { // MdBusiness
   * mdBusiness = MdBusiness.getMdBusiness(MdBusinessInfo.CLASS); MdBusiness
   * mdBusiness = MdBusiness.getMdBusiness(MdClass.CLASS);
   * 
   * // DefaultAttribute.CODE - defined by GeoEntity geoId MdAttributeBoolean
   * mdAttrBool = new MdAttributeBoolean();
   * mdAttrBool.setAttributeName(MdClassInfo.GRAPH_CLASS);
   * mdAttrBool.getDisplayLabel().setValue("Graph Class");
   * mdAttrBool.getDescription().
   * setValue("Indicates whether this class is included in the graph");
   * mdAttrBool.setDefiningMdClass(mdBusiness); mdAttrBool.setRequired(true);
   * mdAttrBool.addIndexType(MdAttributeIndices.NO_INDEX);
   * mdAttrBool.setDefaultValue(false);
   * mdAttrBool.getPositiveDisplayLabel().setDefaultValue("Is a Graph Class");
   * mdAttrBool.getNegativeDisplayLabel().setDefaultValue("Is not a Graph Class"
   * ); mdAttrBool.apply(); } // // @Request // private static void inRequest()
   * // { // if (!LocalProperties.isRunwayEnvironment()) // { // throw new
   * RuntimeException("Runway environment expected"); // } //
   * LocalProperties.setSkipCodeGenAndCompile(false); // // SchedulerV2.doIt();
   * // }
   */
}
