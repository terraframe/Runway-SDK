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
package com.runwaysdk.gis;

import java.util.Locale;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdWebAttributeInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.dataaccess.MigrationUtil;
import com.runwaysdk.form.web.JSONFormVisitor;
import com.runwaysdk.form.web.WebFormObject;
import com.runwaysdk.gis.constants.GISConstants;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdClass;
import com.runwaysdk.system.metadata.MdForm;
import com.runwaysdk.system.metadata.MdFormDTO;
import com.runwaysdk.system.metadata.MdFormQuery;
import com.runwaysdk.system.metadata.MdType;
import com.runwaysdk.web.WebClientSession;

public class GISTestSandbox
{
  public static java.util.Date startTime = new java.util.Date();

  public static void main(String[] args)
  {
    MigrationUtil.setReferenceAttributeDefaultIndexes();
  }

  @Request
  public static void testForm() throws JSONException
  {
    MdFormQuery q = new MdFormQuery(new QueryFactory());
    MdForm mdForm = q.getIterator().getAll().get(0);

    Locale[] locs = new Locale[] { CommonProperties.getDefaultLocale() };
    WebClientSession session = WebClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, locs);

    MdFormDTO mdFormDTO = MdFormDTO.get(session.getRequest(), mdForm.getOid());

    WebFormObject obj = WebFormObject.newInstance(mdFormDTO);
    JSONFormVisitor v = new JSONFormVisitor(CommonProperties.getDefaultLocale());
    obj.accept(v);

    JSONObject json = v.getJSON();
    System.out.println(json.toString(2));
  }

  // private static void export(String oid, Set<String> all)
  // {
  // if (!all.contains(oid))
  // {
  // EntityDAOIF entity = EntityDAO.get(oid);
  //
  // if (!isRunwayMetadata(entity))
  // {
  // all.add(oid);
  //
  // if (entity instanceof BusinessDAOIF)
  // {
  // BusinessDAOIF businessDAO = (BusinessDAOIF) entity;
  // List<RelationshipDAOIF> children = businessDAO.getAllChildren();
  //
  // for (RelationshipDAOIF child : children)
  // {
  // export(child.getOid(), all);
  // }
  //
  // List<RelationshipDAOIF> parents = businessDAO.getAllParents();
  //
  // for (RelationshipDAOIF parent : parents)
  // {
  // export(parent.getOid(), all);
  // }
  // }
  // else if (entity instanceof RelationshipDAOIF)
  // {
  // RelationshipDAOIF relationship = (RelationshipDAOIF) entity;
  //
  // export(relationship.getParentOid(), all);
  // export(relationship.getChildOid(), all);
  // }
  // }
  // }
  // }

  @Transaction
  public static void addPointField()
  {
    Database.enableLoggingDMLAndDDLstatements(true);

    MdClass mdPrimitive = MdClass.getMdClass(MdWebAttributeInfo.CLASS);

    MdBusinessDAO mdGeometry = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdGeometry.setValue(MdType.PACKAGENAME, GISConstants.GIS_METADATA_PACKAGE);
    mdGeometry.setValue(MdType.TYPENAME, "MdWebGeometry");
    mdGeometry.setValue(MdBusinessInfo.ABSTRACT, "true");
    mdGeometry.setValue(MdBusiness.SUPERMDBUSINESS, mdPrimitive.getOid());
    mdGeometry.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Geometry Field");
    mdGeometry.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web Geometry Field");
    mdGeometry.apply();

    MdBusinessDAO mdWebPoint = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebPoint.setValue(MdType.PACKAGENAME, GISConstants.GIS_METADATA_PACKAGE);
    mdWebPoint.setValue(MdType.TYPENAME, "MdWebPoint");
    mdWebPoint.setValue(MdBusiness.SUPERMDBUSINESS, mdGeometry.getOid());
    mdWebPoint.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Point Field");
    mdWebPoint.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web Point Field");
    mdWebPoint.apply();
  }

  @Transaction
  public static void createAttributeClasses(String sessionId)
  {
    String JUNIT_PACKAGE = "temporary.junit.test.gis";
    TypeInfo TEST_CLASS = new TypeInfo(JUNIT_PACKAGE, "TestClass");

    // MdBusinessDAO testClassMdBusinessDAO = MdBusinessDAO.newInstance();
    // testClassMdBusinessDAO.setValue(MdBusinessInfo.NAME,
    // TEST_CLASS.getTypeName());
    // testClassMdBusinessDAO.setValue(MdBusinessInfo.PACKAGE,
    // TEST_CLASS.getPackageName());
    // testClassMdBusinessDAO.setValue(MdBusinessInfo.DISPLAY_LABEL,
    // TEST_CLASS.getTypeName());
    // testClassMdBusinessDAO.setValue(MdBusinessInfo.DESCRIPTION,
    // TEST_CLASS.getTypeName());
    // testClassMdBusinessDAO.setValue(MdBusinessInfo.REMOVE,
    // MdAttributeBooleanInfo.TRUE);
    // testClassMdBusinessDAO.setValue(MdBusinessInfo.EXTENDABLE,
    // MdAttributeBooleanInfo.TRUE);
    // testClassMdBusinessDAO.setValue(MdBusinessInfo.ABSTRACT,
    // MdAttributeBooleanInfo.FALSE);
    // testClassMdBusinessDAO.setValue(MdBusinessInfo.CACHE_ALGORITHM,
    // EntityCacheMaster.CACHE_NOTHING.getOid());
    // testClassMdBusinessDAO.apply();
    //
    // MdAttributePointDAO mdAttributePointDAO =
    // MdAttributePointDAO.newInstance();
    // mdAttributePointDAO.setValue(MdAttributePointInfo.NAME, "testPoint");
    // mdAttributePointDAO.setValue(MdAttributePointInfo.DISPLAY_LABEL,
    // "Test Point");
    // mdAttributePointDAO.setValue(MdAttributePointInfo.DESCRIPTION,
    // "Test Point");
    // mdAttributePointDAO.setValue(MdAttributePointInfo.REQUIRED,
    // MdAttributeBooleanInfo.FALSE);
    // mdAttributePointDAO.setValue(MdAttributePointInfo.IMMUTABLE,
    // MdAttributeBooleanInfo.FALSE);
    // mdAttributePointDAO.setValue(MdAttributePointInfo.REQUIRED,
    // MdAttributeBooleanInfo.FALSE);
    // mdAttributePointDAO.setValue(MdAttributePointInfo.IMMUTABLE,
    // MdAttributeBooleanInfo.FALSE);
    // mdAttributePointDAO.setValue(MdAttributePointInfo.SRID, "4326");
    // // mdAttributePointDAO.setValue(MdAttributePointInfo.DIMENSION, "2");
    // mdAttributePointDAO.setValue(MdAttributePointInfo.DEFINING_MD_CLASS,
    // testClassMdBusinessDAO.getOid());
    // mdAttributePointDAO.apply();
    //
    // BusinessDAO businessDAO =
    // BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());
    // businessDAO.setValue("testPoint", "POINT(191232 243118)");
    // businessDAO.apply();

    QueryFactory queryFactory = new QueryFactory();

    BusinessQuery testClassQuery = queryFactory.businessQuery(TEST_CLASS.getType());

    ValueQuery valueQuery = new ValueQuery(queryFactory);
    valueQuery.SELECT(testClassQuery.get("testPoint", "geometry"));

    System.out.println(valueQuery.getSQL());

    OIterator<ValueObject> i = valueQuery.getIterator();

    for (ValueObject valueObject : i)
    {
      valueObject.printAttributes();
    }

  }

  public static void beginTest(String sessionId)
  {

    // WKTWriter geometryWriter = new WKTWriter(2);
    //
    //
    // GeometryFactory geometryFactory = new GeometryFactory();
    //
    // Point point = geometryFactory.createPoint(
    // new Coordinate(100, 100));
    //
    // String wktString = geometryWriter.write(point);
    //
    // System.out.println(geometryWriter.write(point));
    //
    // WKTReader geometryReader = new WKTReader(geometryFactory);
    //
    // try
    // {
    // Geometry newGeometry = geometryReader.read(wktString);
    //
    // System.out.println(newGeometry+" "+newGeometry.toText());
    // }
    // catch(Exception e)
    // {
    // e.printStackTrace();
    // }

    // System.out.println(((Geometry) value).toString());
    //
    // ((Geometry) value).setSRID(4326);
    //
    // System.out.println(((Geometry) value).getSRID());
    //
    // WKBWriter geometryWriter = new WKBWriter(2);
    // prepStmt.setBytes(index, geometryWriter.write((Geometry) value));
    //
    // WKTWriter geometryWriter = new WKTWriter(2);
    // String setString = "GeomFromText('"+toText+"', 4326)";

    // Connection conx = Database.getConnection();
    // String selectStatement = "SELECT roads_geom FROM roads";
    //
    // Statement statement = null;
    // ResultSet resultSet = null;
    //
    // try
    // {
    // statement = conx.createStatement();
    // resultSet = statement.executeQuery(selectStatement);
    //
    // while(resultSet.next())
    // {
    // Object returnObject = resultSet.getObject("roads_geom");
    //
    // System.out.println("---------------------------");
    // System.out.println("Geometry Class:
    // :"+returnObject.getClass().getName());
    //
    // if (returnObject instanceof JtsGeometry)
    // {
    // JtsGeometry pgGeometry = (JtsGeometry)returnObject;
    // Geometry geometry = pgGeometry.getGeometry();
    //
    // System.out.println("Class: "+geometry.getClass().getName());
    //
    // System.out.println("Text String: "+geometry.toText());
    // System.out.println("Geometry Type: "+geometry.getGeometryType());
    // System.out.println("Number of Points: "+geometry.getNumPoints());
    //
    // if (geometry instanceof LineString)
    // {
    // LineString lineString = (LineString)geometry;
    // System.out.println("Length: "+lineString.getLength());
    //
    // for (int i=0; i < lineString.getNumPoints(); i++)
    // {
    // Point point = lineString.getPointN(i);
    // System.out.println(" Point Text: "+point.toText());
    // }
    //
    // }
    //
    // }
    //
    // // Standard PostGIS Objects
    // //
    // // if (returnObject instanceof PGgeometry)
    // // {
    // // PGgeometry pgGeometry = (PGgeometry)returnObject;
    // // Geometry geometry = pgGeometry.getGeometry();
    // //
    // // System.out.println("---------------------------");
    // // System.out.println(geometry.getClass().getName());
    // //
    // // System.out.println("Type: "+geometry.getType());
    // // System.out.println("Value: "+geometry.getValue());
    // // System.out.println("TypeString: "+geometry.getTypeString());
    // //
    // // LineString lineString = (LineString)geometry;
    // //
    // // System.out.println("Number of points: "+lineString.numPoints());
    // //
    // // for (Point point : lineString.getPoints())
    // // {
    // // System.out.println(" Point: "+point.getValue());
    // // }
    // // }
    //
    // }
    //
    // }
    // catch (SQLException ex)
    // {
    // ex.printStackTrace();
    // }
    // finally
    // {
    // try
    // {
    // if (resultSet != null )
    // {
    // resultSet.close();
    // }
    //
    // if (statement != null )
    // {
    // statement.close();
    // }
    //
    // conx.close();
    // }
    // catch (SQLException e)
    // {
    // e.printStackTrace();
    // }
    // }
    //
    //
    // QueryFactory qf = new QueryFactory();
    //
    // BusinessDAOQuery q = qf.businessDAOQuery(MdAttributeInfo.CLASS);
    //
    // System.out.println(q.getSQL());
    //
    // System.out.println("----------------------------------");
    //
    // System.out.println(q.getCount());
    //
    // OIterator<BusinessDAOIF> iterator = q.getIterator();
    //
    // for (BusinessDAOIF businessDAO : iterator)
    // {
    // businessDAO.printAttributes();
    // }
  }
}
