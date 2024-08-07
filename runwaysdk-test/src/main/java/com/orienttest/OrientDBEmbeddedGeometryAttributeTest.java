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
package com.orienttest;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.spatial4j.shape.Shape;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.spatial.shape.OShapeFactory;
import com.orientechnologies.spatial.shape.OShapeType;

/**
 * This class exists to test for the existence of a critical blocking bug discovered in versions 3.2.13 and 3.1.20 of OrientDB.
 * This bug prevents upgrade of OrientDB past v3.0.
 * 
 * A ticket was filed against OrientDB on 15/2023 at: https://github.com/orientechnologies/orientdb/issues/9910
 * 
 * @author rrowlands
 */
public class OrientDBEmbeddedGeometryAttributeTest
{
  /*
   * Constants related to the database connection. Database and user must exist prior to running this test.
   */
  public static final String DB_URL = "remote:localhost";
  
  public static final String DB_USER = "admin";
  
  public static final String DB_PASS = "admin";
  
  public static final String DB_NAME = "testdb";
  
  /*
   * Constants related to the test data
   */
  public static final String PREFIX = "embt";
  
  public static final String VERTEX_CLASS_NAME = PREFIX + "test_class";
  
  public static final String EMBEDDED_ATTRIBUTE_NAME = PREFIX + "test_embedded_attribute";
  
  public static final String EMBEDDED_ATTRIBUTE_TEXT = PREFIX + "test_embedded_attribute_text";
  
  public static final String EMBEDDED_CLASS_NAME = PREFIX + "test_embedded_class";
  
  public static void main(String[] args) throws Throwable
  {
    OrientDB orientDB = new OrientDB(DB_URL, DB_USER, DB_PASS, OrientDBConfig.defaultConfig());
    ODatabaseSession session = orientDB.open(DB_NAME, DB_USER, DB_PASS);
    
    session.activateOnCurrentThread();
    
    try
    {
      createSchema(session);
      
//      session.begin(); // Bug does NOT happen if you are inside of a transaction...
      testEmbeddedGeometry(session);
//      session.commit();
    }
    finally
    {
      session.close();
      orientDB.close();
    }
  }
  
  public static void createSchema(ODatabaseSession session) throws Throwable
  {
    OClass oClass = session.createVertexClass(VERTEX_CLASS_NAME);
//    OClass embeddedClass = session.createClass(EMBEDDED_CLASS_NAME);
    oClass.createProperty(EMBEDDED_ATTRIBUTE_NAME, OType.EMBEDDED);
//    embeddedClass.createProperty(EMBEDDED_ATTRIBUTE_TEXT, OType.EMBEDDED);
  }
  
  public static void testEmbeddedGeometry(ODatabaseSession session) throws Throwable
  {
    // Create new outer vertex
    OVertex outerVertex = session.newVertex(VERTEX_CLASS_NAME);
    
    // Create new embedded
//     OElement embedded = session.newElement(EMBEDDED_CLASS_NAME);
    ODocument embedded = new ODocument();
    
    outerVertex.setProperty(EMBEDDED_ATTRIBUTE_NAME, embedded);
    outerVertex.save();
    
    // Update
    OVertex outerVertex2 = session.load(outerVertex.getIdentity());
    OElement embedded2 = outerVertex2.getProperty(EMBEDDED_ATTRIBUTE_NAME);
    
    GeometryFactory geomFac = new GeometryFactory();
    Point point = geomFac.createPoint(new Coordinate(10,10));
    ODocument geomDoc = OShapeFactory.INSTANCE.toDoc(point);
    embedded2.setProperty(EMBEDDED_ATTRIBUTE_TEXT, geomDoc);
    outerVertex2.setProperty(EMBEDDED_ATTRIBUTE_NAME, embedded2);
    outerVertex2.save(); // Error is thrown here
    
    // Read
    OVertex outerVertex3 = session.load(outerVertex.getIdentity());
    OElement embedded3 = outerVertex3.getProperty(EMBEDDED_ATTRIBUTE_NAME);
    
    Point embeddedPointValue = (Point) OShapeFactory.INSTANCE.toGeometry(OShapeFactory.INSTANCE.fromDoc(embedded3.getProperty(EMBEDDED_ATTRIBUTE_TEXT)));
    
    if (!embeddedPointValue.equals(point))
    {
      throw new RuntimeException("Expected [?] but was [?].");
    }
    System.out.println(embeddedPointValue.toString());
  }
}

/* Output from running this class is:
Jan 05, 2023 3:06:59 PM com.orientechnologies.common.log.OLogManager log
INFO: Can not detect value of limit of open files.
Jan 05, 2023 3:06:59 PM com.orientechnologies.common.log.OLogManager log
INFO: Default limit of open files (512) will be used.
Jan 05, 2023 3:07:00 PM com.orientechnologies.common.log.OLogManager log
INFO: - shutdown storage: testdb...
Exception in thread "main" com.orientechnologies.orient.core.exception.ODatabaseException: Error during saving of record with rid #34:0
    DB name="testdb"
    at com.orientechnologies.orient.core.tx.OTransactionNoTx.saveRecord(OTransactionNoTx.java:226)
    at com.orientechnologies.orient.core.db.document.ODatabaseDocumentAbstract.saveInternal(ODatabaseDocumentAbstract.java:1486)
    at com.orientechnologies.orient.core.db.document.ODatabaseDocumentAbstract.save(ODatabaseDocumentAbstract.java:1434)
    at com.orientechnologies.orient.core.db.document.ODatabaseDocumentAbstract.save(ODatabaseDocumentAbstract.java:80)
    at com.orientechnologies.orient.core.record.impl.ODocument.save(ODocument.java:2373)
    at com.orientechnologies.orient.core.record.impl.ODocument.save(ODocument.java:2362)
    at com.orientechnologies.orient.core.record.impl.ODocument.save(ODocument.java:121)
    at com.orienttest.OrientDBEmbeddedAttributeTest.testEmbedded(OrientDBEmbeddedAttributeTest.java:80)
    at com.orienttest.OrientDBEmbeddedAttributeTest.main(OrientDBEmbeddedAttributeTest.java:46)
Caused by: com.orientechnologies.orient.core.exception.OSerializationException: Cannot read transaction record from the network. Transaction aborted
    DB name="testdb"
    DB name="testdb"
    at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
    at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
    at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
    at java.lang.reflect.Constructor.newInstance(Constructor.java:423)
    at com.orientechnologies.orient.client.binary.OChannelBinaryAsynchClient.handleException(OChannelBinaryAsynchClient.java:355)
    at com.orientechnologies.orient.client.binary.OChannelBinaryAsynchClient.handleStatus(OChannelBinaryAsynchClient.java:303)
    at com.orientechnologies.orient.client.binary.OChannelBinaryAsynchClient.handleStatus(OChannelBinaryAsynchClient.java:325)
    at com.orientechnologies.orient.client.binary.OChannelBinaryAsynchClient.beginResponse(OChannelBinaryAsynchClient.java:209)
    at com.orientechnologies.orient.client.binary.OChannelBinaryAsynchClient.beginResponse(OChannelBinaryAsynchClient.java:167)
    at com.orientechnologies.orient.client.remote.OStorageRemote.beginResponse(OStorageRemote.java:2001)
    at com.orientechnologies.orient.client.remote.OStorageRemote.lambda$networkOperationRetryTimeout$2(OStorageRemote.java:435)
    at com.orientechnologies.orient.client.remote.OStorageRemote.baseNetworkOperation(OStorageRemote.java:500)
    at com.orientechnologies.orient.client.remote.OStorageRemote.networkOperationRetryTimeout(OStorageRemote.java:415)
    at com.orientechnologies.orient.client.remote.OStorageRemote.networkOperationNoRetry(OStorageRemote.java:450)
    at com.orientechnologies.orient.client.remote.OStorageRemote.commit(OStorageRemote.java:1366)
    at com.orientechnologies.orient.core.db.document.ODatabaseDocumentRemote.internalCommit(ODatabaseDocumentRemote.java:1184)
    at com.orientechnologies.orient.core.tx.OTransactionOptimistic.doCommit(OTransactionOptimistic.java:621)
    at com.orientechnologies.orient.core.tx.OTransactionOptimistic.commit(OTransactionOptimistic.java:114)
    at com.orientechnologies.orient.core.tx.OTransactionOptimistic.commit(OTransactionOptimistic.java:92)
    at com.orientechnologies.orient.core.db.document.ODatabaseDocumentRemote.saveAll(ODatabaseDocumentRemote.java:683)
    at com.orientechnologies.orient.core.tx.OTransactionNoTx.saveRecord(OTransactionNoTx.java:208)
    ... 8 more
    Suppressed: com.orientechnologies.orient.core.exception.OSerializationException: Cannot read transaction record from the network. Transaction aborted
    DB name="testdb"
        at com.orientechnologies.orient.server.tx.OTransactionOptimisticServer.begin(OTransactionOptimisticServer.java:229)
        at com.orientechnologies.orient.core.db.document.ODatabaseDocumentAbstract.rawBegin(ODatabaseDocumentAbstract.java:856)
        at com.orientechnologies.orient.server.OConnectionBinaryExecutor.executeCommit38(OConnectionBinaryExecutor.java:1547)
        at com.orientechnologies.orient.client.remote.message.OCommit38Request.execute(OCommit38Request.java:141)
        at com.orientechnologies.orient.server.network.protocol.binary.ONetworkProtocolBinary.sessionRequest(ONetworkProtocolBinary.java:355)
        at com.orientechnologies.orient.server.network.protocol.binary.ONetworkProtocolBinary.execute(ONetworkProtocolBinary.java:239)
        at com.orientechnologies.common.thread.OSoftThread.run(OSoftThread.java:68)
    Caused by: java.lang.ClassCastException: com.orientechnologies.orient.core.record.impl.OVertexDelegate cannot be cast to com.orientechnologies.orient.core.record.impl.ODocument
        at com.orientechnologies.orient.core.serialization.serializer.record.binary.ODocumentSerializerDelta.deserializeDeltaValue(ODocumentSerializerDelta.java:222)
        at com.orientechnologies.orient.core.serialization.serializer.record.binary.ODocumentSerializerDelta.deserializeDeltaEntry(ODocumentSerializerDelta.java:207)
        at com.orientechnologies.orient.core.serialization.serializer.record.binary.ODocumentSerializerDelta.deserializeDelta(ODocumentSerializerDelta.java:186)
        at com.orientechnologies.orient.core.serialization.serializer.record.binary.ODocumentSerializerDelta.deserializeDelta(ODocumentSerializerDelta.java:168)
        at com.orientechnologies.orient.server.tx.OTransactionOptimisticServer.begin(OTransactionOptimisticServer.java:106)
        ... 6 more
Caused by: [CIRCULAR REFERENCE: java.lang.ClassCastException: com.orientechnologies.orient.core.record.impl.OVertexDelegate cannot be cast to com.orientechnologies.orient.core.record.impl.ODocument]
 */
