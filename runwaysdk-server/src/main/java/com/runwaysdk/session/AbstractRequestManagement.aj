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
package com.runwaysdk.session;

import java.sql.Connection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.AttributeNotification;
import com.runwaysdk.DomainErrorException;
import com.runwaysdk.ProblemIF;
import com.runwaysdk.business.Business;
import com.runwaysdk.business.Element;
import com.runwaysdk.business.Entity;
import com.runwaysdk.business.Information;
import com.runwaysdk.business.LockException;
import com.runwaysdk.business.Message;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.business.Relationship;
import com.runwaysdk.business.Struct;
import com.runwaysdk.business.Warning;
import com.runwaysdk.business.graph.GraphObject;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.business.rbac.MethodActorDAOIF;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.transaction.AbstractTransactionManagement;
import com.runwaysdk.dataaccess.transaction.AttributeNotificationMap;
import com.runwaysdk.dataaccess.transaction.LockObject;
import com.runwaysdk.logging.RunwayLogUtil;
import com.runwaysdk.util.IdParser;

/**
 * Captures the session oid and executes authentication.
 */
privileged public abstract aspect AbstractRequestManagement percflow(topLevelSession())
{
  static
  {
    try
    {
      // Initialize the connection pool of the graph database, if one is present
      GraphDBService.getInstance().initializeConnectionPool();
    }
    catch (Throwable t)
    {
      t.printStackTrace();
      throw t;
    }
  }

  declare                              precedence : AbstractTransactionManagement+;

  protected AbstractRequestAspectState state;
  
  public AbstractRequestAspectState getState()
  {
    return this.state;
  }


  public pointcut defaultRequest(Request request)
  : execution (@com.runwaysdk.session.Request * *.*(..))
  && @annotation(request) && (if (request.value() == RequestType.DEFAULT));

  // Collect session context information.
  public pointcut sessionRequest(Request request, String _sessionId)
  :  (execution (@com.runwaysdk.session.Request * *.*(String, ..)))
      && args(_sessionId, ..)
      && @annotation(request) && (if (request.value() == RequestType.SESSION));

  public pointcut threadRequest(Request request)
  : execution (@com.runwaysdk.session.Request * *.*(..))
  && @annotation(request) && (if (request.value() == RequestType.THREAD));

  // A non thread request is everything but a threaded one.
  public pointcut nonThreadRequest()
  : (nonAnnotizedRequestEntryPoints() || defaultRequest(Request) || sessionRequest(Request, String));

  // public pointcut nonThreadRequest(Request request)
  // : (nonAnnotizedRequestEntryPoints() || defaultRequest(Request) ||
  // sessionRequest(Request, String)) && @annotation(request);

  public abstract pointcut request();

  // public abstract pointcut request(Request request);

  public pointcut nonAnnotizedRequestEntryPoints()
  :  execution (public * com.runwaysdk.dataaccess.io..*.*(..))
    || (
          ( execution (* junit.framework.TestSuite+.*(..)) ||
            execution (* junit.extensions.TestSetup+.*(..)) ||
            execution (* junit.framework.TestCase+.*(..))
          )

        && (

// !cflow(execution (* com.runwaysdk.facade.RMIAdapterTest.*(..)))
// &&
// !cflow(execution (*
// com.runwaysdk.facade.WebServiceAdapterTest.*(..))) &&
// !cflow(execution (*
// com.runwaysdk.facade.WebServiceFacadeGenerationTest.*(..))) &&
// !cflow(execution (*
// com.runwaysdk.facade.WebServiceInvokeMethodTest.*(..))) &&
// !cflow(execution (*
// com.runwaysdk.facade.JSONWebServiceInvokeMethodTest.*(..)))

//            !cflow(execution (* com.runwaysdk.facade.RMIAdapterTest.*(..))) &&
//            !cflow(execution (* com.runwaysdk.facade.WebServiceAdapterTest.*(..))) &&
//            !cflow(execution (* com.runwaysdk.facade.WebServiceFacadeGenerationTest.*(..))) &&
//            !cflow(execution (* com.runwaysdk.facade.WebServiceInvokeMethodTest.*(..))) &&
            !cflow(execution (* com.runwaysdk.facade.JSONWebServiceInvokeMethodTest.*(..)))

           && !within(com.runwaysdk.facade.AdapterTest+)
           && !within(com.runwaysdk.facade.SessionDTOAdapterTest+)
           && !within(com.runwaysdk.facade.FacadeGenerationTest+)
           && !within(com.runwaysdk.facade.InvokeSessionComponentMethodTestBase+)
           && !within(com.runwaysdk.facade.InvokeMethodTestBase+)
           && !within(com.runwaysdk.facade.MessageTest+)
           && !within(com.runwaysdk.facade.*SeleniumTest*)
           && !within(com.runwaysdk.business.ontology.OntologyFacadeMethodsTest+)

           && !within(com.runwaysdk.DoNotWeave+)
    ));
  // suite.addTest(WebServiceAdapterTest.suite());
  // suite.addTest(WebServiceInvokeMethodTest.suite());
  // suite.addTest(WebServiceFacadeGenerationTest.suite());
  // suite.addTest(JSONWebServiceInvokeMethodTest.suite());
  // suite.addTest(SeleniumTestSuite.suite());

  public pointcut allRequestEntryPoints()
  : nonAnnotizedRequestEntryPoints() || nonThreadRequest() || threadRequest(Request);

  // public pointcut allRequestEntryPoints()
  // : nonAnnotizedRequestEntryPoints() || nonThreadRequest(Request) ||
  // threadRequest(Request);

  public abstract pointcut enterSession();

  /*
   * public pointcut enterSession() : execution (@com.runwaysdk.session.Request
   * * *+.*(..)) || execution (public * com.runwaysdk.dataaccess.io..*.*(..)) ||
   * ( ( execution (* junit.framework.TestSuite+.*(..)) || execution (*
   * junit.extensions.TestSetup+.*(..)) || execution (*
   * junit.framework.TestCase+.*(..)) )
   * 
   * && (
   * 
   * // !cflow(execution (* com.runwaysdk.facade.RMIAdapterTest.*(..))) // && //
   * !cflow(execution (* // com.runwaysdk.facade.WebServiceAdapterTest.*(..)))
   * && // !cflow(execution (* //
   * com.runwaysdk.facade.WebServiceFacadeGenerationTest.*(..))) && //
   * !cflow(execution (* //
   * com.runwaysdk.facade.WebServiceInvokeMethodTest.*(..))) && //
   * !cflow(execution (* //
   * com.runwaysdk.facade.JSONWebServiceInvokeMethodTest.*(..)))
   * 
   * // !cflow(execution (* com.runwaysdk.facade.RMIAdapterTest.*(..))) && //
   * !cflow(execution (* com.runwaysdk.facade.WebServiceAdapterTest.*(..))) &&
   * // !cflow(execution (*
   * com.runwaysdk.facade.WebServiceFacadeGenerationTest.*(..))) && //
   * !cflow(execution (* com.runwaysdk.facade.WebServiceInvokeMethodTest.*(..)))
   * && !cflow(execution (*
   * com.runwaysdk.facade.JSONWebServiceInvokeMethodTest.*(..)))
   * 
   * && !within(com.runwaysdk.facade.AdapterTest+) &&
   * !within(com.runwaysdk.facade.SessionDTOAdapterTest+) &&
   * !within(com.runwaysdk.facade.FacadeGenerationTest+) &&
   * !within(com.runwaysdk.facade.InvokeSessionComponentMethodTestBase+) &&
   * !within(com.runwaysdk.facade.InvokeMethodTestBase+) &&
   * !within(com.runwaysdk.facade.MessageTest+) &&
   * !within(com.runwaysdk.facade.*SeleniumTest*)
   * 
   * && !within(com.runwaysdk.DoNotWeave+) ));
   * 
   * // suite.addTest(WebServiceAdapterTest.suite()); //
   * suite.addTest(WebServiceInvokeMethodTest.suite()); //
   * suite.addTest(WebServiceFacadeGenerationTest.suite()); //
   * suite.addTest(JSONWebServiceInvokeMethodTest.suite()); //
   * suite.addTest(SeleniumTestSuite.suite());
   */
  public pointcut topLevelSession()
    :  enterSession()
    && !cflowbelow(enterSession());

  protected pointcut topLevelPermission(String _sessionId)
  :  (sessionRequest(Request, String)
     && !cflowbelow(sessionRequest(Request, String))
     ) && args(_sessionId, ..);

  // these objects should be unlocked at the end of the session request
  protected pointcut setAppLock(String oid)
    : (execution (* com.runwaysdk.dataaccess.transaction.LockObject.recordAppLock(String)) && args(oid))
  && cflow(topLevelSession());

  before(String oid) :  setAppLock(oid)
  {
    state.addAppLock(oid);
  }

  protected pointcut throwProblem(ProblemIF problemIF)
  : (execution (* com.runwaysdk.ProblemIF+.throwIt())
  && target(problemIF));

  before(ProblemIF problemIF) : throwProblem(problemIF)
  {
    state.throwProblem(problemIF);
  }

  protected pointcut getProblemsInCurrentRequest()
  : (execution (* com.runwaysdk.session.RequestState.getProblemsInCurrentRequest()));

  Object around() : getProblemsInCurrentRequest()
  {
    return state.getProblemList();
  }

  // Execute the create Commands now, but save the deletes for later.
  protected pointcut attributeNotificationMap(AttributeNotificationMap attributeNotificationMap)
    : execution(com.runwaysdk.dataaccess.transaction.AttributeNotificationMap+.new(..)) && this(attributeNotificationMap);

  after(AttributeNotificationMap attributeNotificationMap)
    : attributeNotificationMap(attributeNotificationMap)
  {
    state.addAttributeNotificationMap(attributeNotificationMap);
  }

  protected pointcut getConnection()
  :  call(Connection Database.getConnection())
  && !within(RequestState)
  && !withincode(* com.runwaysdk.dataaccess.transaction.AbstractTransactionManagement+.getDDLConnection())
  && !withincode(* com.runwaysdk.dataaccess.database.general.MySQL.getNextSequenceNumber(..))
  && !withincode(* com.runwaysdk.dataaccess.database.general.SQLServer.getNextSequenceNumber(..));

  Object around() : getConnection()
  {
    return state.getConnection();
  }

  protected pointcut getGraphRequest()
  // : call(Optional<GraphRequest>
  // com.runwaysdk.dataaccess.graph.GraphDBService.getGraphDBRequest())
    :  call(* com.runwaysdk.dataaccess.graph.GraphDBService.getGraphDBRequest())
  && !within(RequestState);

  Object around() : getGraphRequest()
  {
    return state.getGraphRequest();
  }

  protected pointcut closeGraphRequest()
  :  call(void com.runwaysdk.dataaccess.graph.GraphRequest+.close())
  && !within(AbstractRequestManagement+)
  && !within(AbstractTransactionManagement+)
  && !within(RequestState+)
  && !within(com.runwaysdk.dataaccess.transaction.TransactionState+);

  void around() : closeGraphRequest()
  {
    // NOT clossing the Graph DB
  }

  // Trace usage of all connection objects, except those used by assertion
  // checking
  // protected pointcut connectionTrace()
  // : getConnection();
  // after() : connectionTrace()
  // {
  // if (this.getRequestState().isDebug())
  // {
  // System.out.println("Calling AbstractDatabase+.getConnection() " + conn + "
  // "+thisEnclosingJoinPointStaticPart.getSourceLocation());
  // }
  // }

  // do not close connections in the core code
  protected pointcut closeConnection()
  :  call(* com.runwaysdk.dataaccess.database.general.AbstractDatabase+.closeConnection(Connection))
  && !within(RequestState)
  && !within(AbstractTransactionManagement+)
  && !withincode(* com.runwaysdk.dataaccess.database.general.AbstractDatabase+.executeAsRoot(..))
  && !withincode(* com.runwaysdk.dataaccess.database.general.MySQL+.getNextSequenceNumber(..))
  && !withincode(* com.runwaysdk.dataaccess.database.general.SQLServer+.getNextSequenceNumber(..));

  void around() : closeConnection()
  {
    // System.out.println(" Would have closed connection (but didn't) " + conn +
    // " "+thisEnclosingJoinPointStaticPart.getSourceLocation());
  }

  pointcut mapNewInstanceTempId(String oldTempId, String newId)
  : execution (* com.runwaysdk.session.Session.mapNewInstanceTempId(String, String)) && args(oldTempId, newId);

  before(String oldTempId, String newId) : mapNewInstanceTempId(oldTempId, newId)
  {
    state.mapNewInstanceTempId(oldTempId, newId);
  }

  // //////////////////////////////////////////////////////////////////////////////////////////////////////
  pointcut getCurrentSession()
  : execution (* com.runwaysdk.session.Session.getCurrentSession());

  Object around() : getCurrentSession()
  {
    return state.getCurrentSession();
  }

  pointcut getCurrentRequestState()
  : execution (* com.runwaysdk.session.RequestState.getCurrentRequestState());

  Object around() : getCurrentRequestState()
  {
    return state.getRequestState();
  }

  protected pointcut checkMethodExecutePermission(Mutable mutable)
  : ( execution (@Authenticate  * *.*(..)) && this(mutable));

  @SuppressAjWarnings({ "adviceDidNotMatch" })
  Object around(Mutable mutable) : checkMethodExecutePermission(mutable)
  {
    Signature signature = thisJoinPointStaticPart.getSignature();

    String type = signature.getDeclaringTypeName();
    String methodName = signature.getName();

    state.checkMethodExecutePermission(type, methodName, mutable);

    try
    {
      Object result = proceed(mutable);
      return result;
    }
    finally
    {
      state.removeMethodFromStack();
    }
  }

  protected pointcut checkStaticMethodExecutePermission()
  : ( execution (@Authenticate  static * *.*(..)));

  @SuppressAjWarnings({ "adviceDidNotMatch" })
  Object around() : checkStaticMethodExecutePermission()
  {
    Signature signature = thisJoinPointStaticPart.getSignature();
    String type = signature.getDeclaringTypeName();
    String methodName = signature.getName();

    state.checkStaticMethodExecutePermission(type, methodName);

    try
    {
      Object result = proceed();
      return result;
    }
    finally
    {
      state.removeMethodFromStack();
    }
  }

  // Ensures a userIF has create permissions on a Business when requesting a new
  // instance
  // pointcut getNewBusinessFromFacade(String type)
  // : call (* com.runwaysdk.business.BusinessFacade.newBusiness(..)) &&
  // args(type)
  // && withincode (* com.runwaysdk.facade.Facade.newBusiness(String, String))
  // && (cflow(topLevelPermission(String)));
  // Object around(String type) : getNewBusinessFromFacade(type)
  // {
  // Business business = (Business) proceed(type);
  //
  // // Check execute permission on this method
  // if (this.mdMethodIFStack.size() > 0)
  // {
  // MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
  // checkEntityCreatePermission(mdMethodIF, business);
  // }
  // else
  // {
  // checkEntityCreatePermission(sessionId, business);
  // }
  //
  // return business;
  // }

  pointcut getNewBusinessFromFacade(String type)
  : call (* com.runwaysdk.business.BusinessFacade.newBusiness(..)) && args(type)
  && withincode (* com.runwaysdk.facade.Facade.newBusiness(String, String));

  Object around(String type) : getNewBusinessFromFacade(type)
  {
    Business business = (Business) proceed(type);

    this.state.getNewBusinessFromFacade(business);

    return business;
  }

  pointcut getNewDisconnectedEntityFromFacade(String type)
  : call (* com.runwaysdk.business.BusinessFacade.newEntity(..)) && args(type)
  && withincode (* com.runwaysdk.facade.Facade.newDisconnectedEntity(String, String));

  Object around(String type) : getNewDisconnectedEntityFromFacade(type)
  {
    Entity business = (Entity) proceed(type);

    this.state.getNewDisconnectedEntityFromFacade(business);

    return business;
  }

  // Used to ensure that a userIF must have a lock on the object in order to
  // modify it.
  pointcut applyEntity(Entity entity)
  : execution (* com.runwaysdk.business.Entity.apply()) && this(entity);

  before(Entity entity)
  : applyEntity(entity)
  {
    this.state.applyEntity(entity);
  }

  // Check write permission on an object. Check on userLock().
  pointcut elementLockedByCheck(Element element)
    : (execution (* com.runwaysdk.business.Element+.userLock(String)))
      && target(element);

  before(Element element)
  : elementLockedByCheck(element)
  {
    this.state.elementLockedByCheck(element);
  }

  // Check write permission on an object attribute
  pointcut modifyEntityAttribute(Entity entity, String attributeName)
  : (
      execution (* com.runwaysdk.business.Entity.setValue(String, ..))           ||
      execution (* com.runwaysdk.business.Entity.addEnumItem(String, ..))        ||
      execution (* com.runwaysdk.business.Entity.clearEnum(String))              ||
      execution (* com.runwaysdk.business.Entity.removeEnumItem(String, ..))     ||
      execution (* com.runwaysdk.business.Entity.setBlob(String, ..))
  )
   && this(entity) && args(attributeName, ..);

  before(Entity entity, String attributeName) :
      modifyEntityAttribute(entity, attributeName)
  {
    this.state.modifyEntityAttribute(entity, attributeName);
  }

  // Check write permission on an object attribute
  pointcut modifyElementAttribute(Element element, String attributeName)
  : (
      // Structs
      execution (* com.runwaysdk.business.Element.setStructValue(String, ..))     ||
      execution (* com.runwaysdk.business.Element.addStructItem(String, ..))      ||
      execution (* com.runwaysdk.business.Element.clearStructItems(String, ..))   ||
      execution (* com.runwaysdk.business.Element.removeStructItem(String, ..))   ||
      execution (* com.runwaysdk.business.Element.setStructBlob(String, ..))
  )
   && this(element) && args(attributeName, ..);

  before(Element element, String attributeName) :
    modifyElementAttribute(element, attributeName)
  {
    this.state.modifyEntityAttribute(element, attributeName);
  }

  // Check write permission on an object attribute
  // pointcut modifySessionComponentAttribute(SessionComponent sessionComponent,
  // String attributeName)
  // : (
  // execution (*
  // com.runwaysdk.business.SessionComponent.setValue(String, ..)) ||
  // execution (*
  // com.runwaysdk.business.SessionComponent.addEnumItem(String, ..))
  // ||
  // execution (*
  // com.runwaysdk.business.SessionComponent.clearEnum(String)) ||
  // execution (*
  // com.runwaysdk.business.SessionComponent.removeEnumItem(String,
  // ..)) ||
  // execution (*
  // com.runwaysdk.business.SessionComponent.setBlob(String, ..)) ||
  //
  // // Structs
  // execution (*
  // com.runwaysdk.business.SessionComponent.setStructValue(String,
  // ..)) ||
  // execution (*
  // com.runwaysdk.business.SessionComponent.addStructItem(String,
  // ..)) ||
  // execution (*
  // com.runwaysdk.business.SessionComponent.clearStructItems(String,
  // ..)) ||
  // execution (*
  // com.runwaysdk.business.SessionComponent.removeStructItem(String,
  // ..)) ||
  // execution (*
  // com.runwaysdk.business.SessionComponent.setStructBlob(String,
  // ..))
  // )
  // && this(sessionComponent) && args(attributeName, ..)
  // && cflow(topLevelPermission(String));
  // before (SessionComponent sessionComponent, String attributeName) :
  // modifySessionComponentAttribute(sessionComponent, attributeName)
  // {
  // checkAttributePermissions(sessionComponent, attributeName);
  // }

  // Check delete permission on an object
  pointcut deleteMutable(Mutable mutable)
    : execution (* com.runwaysdk.business.Mutable.delete()) && this(mutable);

  before(Mutable mutable) : deleteMutable(mutable)
  {
    this.state.checkDeletePermissions(mutable);
  }

  // Check permission for adding a child to an object
  pointcut addChildObject(Business parentBusiness, Business childBusiness, String relationshipType)
    : execution (* com.runwaysdk.business.Business.addChild(Business, String))
      && args(childBusiness, relationshipType) && this(parentBusiness);

  before(Business parentBusiness, Business childBusiness, String relationshipType)
    : addChildObject(parentBusiness, childBusiness, relationshipType)
  {
    this.state.checkAddChildObject(parentBusiness, childBusiness, relationshipType);
  }

  // Check permission for adding a child to an object
  pointcut addChildOid(Business parentBusiness, String childOid, String relationshipType)
    : execution (* com.runwaysdk.business.Business.addChild(String, String))
      && args(childOid, relationshipType) && this(parentBusiness);

  before(Business parentBusiness, String childOid, String relationshipType)
    : addChildOid(parentBusiness, childOid, relationshipType)
  {
    this.state.checkAddChildObject(parentBusiness, childOid, relationshipType);
  }

  // Check permission for removing a child from an object
  pointcut removeChildObject(Business parentBusiness, Relationship relationship)
    : execution (* com.runwaysdk.business.Business.removeChild(Relationship))
      && args(relationship) && this(parentBusiness);

  before(Business parentBusiness, Relationship relationship)
    : removeChildObject(parentBusiness, relationship)
  {
    this.state.removeChildObject(parentBusiness, relationship);
  }

  pointcut removeChildOid(Business parentBusiness, String relationshipId)
    : execution (* com.runwaysdk.business.Business.removeChild(String))
      && args(relationshipId) && this(parentBusiness);

  before(Business parentBusiness, String relationshipId)
    : removeChildOid(parentBusiness, relationshipId)
  {
    this.state.removeChildOid(parentBusiness, relationshipId);
  }

  pointcut removeAllChildObjects(Business parentBusiness, Business childBusiness, String relationshipType)
    : execution (* com.runwaysdk.business.Business.removeAllChildren(Business, String))
      && args(childBusiness, relationshipType) && this(parentBusiness);

  before(Business parentBusiness, Business childBusiness, String relationshipType)
    : removeAllChildObjects(parentBusiness, childBusiness, relationshipType)
  {
    this.state.removeAllChildObjects(parentBusiness, childBusiness, relationshipType);
  }

  pointcut removeAllChildOid(Business parentBusiness, String childOid, String relationshipType)
    : execution (* com.runwaysdk.business.Business.removeAllChildren(String, String))
      && args(childOid, relationshipType) && this(parentBusiness);

  before(Business parentBusiness, String childOid, String relationshipType)
    : removeAllChildOid(parentBusiness, childOid, relationshipType)
  {
    this.state.removeAllChildOid(parentBusiness, childOid, relationshipType);
  }

  // Check permission for adding a parent to an object
  pointcut addParentObject(Business parentBusiness, Business childBusiness, String relationshipType)
    : execution (* com.runwaysdk.business.Business.addParent(Business, String))
      && args(parentBusiness, relationshipType) && this(childBusiness);

  before(Business parentBusiness, Business childBusiness, String relationshipType)
    : addParentObject(parentBusiness, childBusiness, relationshipType)
  {
    this.state.addParentObject(parentBusiness, childBusiness, relationshipType);
  }

  // Check permission for adding a parent to an object
  pointcut addParentOid(String parentOid, Business childBusiness, String relationshipType)
    : execution (* com.runwaysdk.business.Business.addParent(String, String))
      && args(parentOid, relationshipType) && this(childBusiness);

  before(String parentOid, Business childBusiness, String relationshipType)
    : addParentOid(parentOid, childBusiness, relationshipType)
  {
    this.state.addParentOid(parentOid, childBusiness, relationshipType);
  }

  // Check permission for adding a parent to an object
  pointcut removeParentObject(Business childBusiness, Relationship relationship)
    : execution (* com.runwaysdk.business.Business.removeParent(Relationship))
      && args(relationship) && this(childBusiness);

  before(Business childBusiness, Relationship relationship)
    : removeParentObject(childBusiness, relationship)
  {
    this.state.removeParentObject(childBusiness, relationship);
  }

  pointcut removeParentOid(Business childBusiness, String relationshipId)
    : execution (* com.runwaysdk.business.Business.removeParent(String))
      && args(relationshipId) && this(childBusiness);

  before(Business childBusiness, String relationshipId)
    : removeParentOid(childBusiness, relationshipId)
  {
    this.state.removeParentOid(childBusiness, relationshipId);
  }

  pointcut removeAllParentObjects(Business parentBusiness, Business childBusiness, String relationshipType)
    : execution (* com.runwaysdk.business.Business.removeAllParents(Business, String))
      && args(parentBusiness, relationshipType) && this(childBusiness);

  before(Business parentBusiness, Business childBusiness, String relationshipType)
    : removeAllParentObjects(parentBusiness, childBusiness, relationshipType)
  {
    this.state.removeAllParentObjects(parentBusiness, childBusiness, relationshipType);
  }

  // Check permission for adding a parent to an object
  pointcut removeAllParentOids(String parentOid, Business childBusiness, String relationshipType)
    : execution (* com.runwaysdk.business.Business.removeAllParents(String, String))
      && args(parentOid, relationshipType) && this(childBusiness);

  before(String parentOid, Business childBusiness, String relationshipType)
    : removeAllParentOids(parentOid, childBusiness, relationshipType)
  {
    this.state.removeAllParentOids(parentOid, childBusiness, relationshipType);
  }

  protected pointcut throwMessage(Message message)
  : (execution (* com.runwaysdk.business.Message+.throwIt())
  && target(message));

  before(Message message) : throwMessage(message)
  {
    this.state.throwMessage(message);
  }

  protected pointcut throwAttributeNotification(AttributeNotification attributeNotification)
  : (call (* com.runwaysdk.AttributeNotification+.throwIt())
  && target(attributeNotification));

  before(AttributeNotification attributeNotification) : throwAttributeNotification(attributeNotification)
  {
    this.state.throwAttributeNotification(attributeNotification);
  }

  // //////////////////////////////////////////////////////////////////////////////////////////////////////
  // GRAPH OBJECT ASPECTS AND METHODS
  // //////////////////////////////////////////////////////////////////////////////////////////////////////

  // Used to ensure that a userIF must have a lock on the object in order to
  // modify it.
  pointcut applyGraphObject(GraphObject graph)
  : execution (* com.runwaysdk.business.graph.GraphObject.apply()) && this(graph);

  before(GraphObject graph)
  : applyGraphObject(graph)
  {
    this.state.applyGraphObject(graph);
  }

  // Check permission for adding a child to an object
  pointcut addChildVertex(VertexObject parent, VertexObject child, MdEdgeDAOIF mdEdge)
    : execution (* com.runwaysdk.business.graph.VertexObject.addChild(VertexObject, MdEdgeDAOIF))
      && args(child, mdEdge) && this(parent);

  before(VertexObject parent, VertexObject child, MdEdgeDAOIF mdEdge)
    : addChildVertex(parent, child, mdEdge)
  {
    this.state.addChildVertex(parent, child, mdEdge);
  }

  // Check permission for removing a child from an object
  pointcut removeChildVertex(VertexObject parent, VertexObject child, MdEdgeDAOIF mdEdge)
  : execution (* com.runwaysdk.business.graph.VertexObject.removeChild(VertexObject, MdEdgeDAOIF))
      && args(child, mdEdge) && this(parent);

  before(VertexObject parent, VertexObject child, MdEdgeDAOIF mdEdge)
    : removeChildVertex(parent, child, mdEdge)
  {
    this.state.removeChildVertex(parent, child, mdEdge);
  }

  // Check permission for adding a parent to an object
  pointcut addParentVertex(VertexObject child, VertexObject parent, MdEdgeDAOIF mdEdge)
  : execution (* com.runwaysdk.business.graph.VertexObject.addParent(VertexObject, MdEdgeDAOIF))
  && args(parent, mdEdge) && this(child);

  before(VertexObject child, VertexObject parent, MdEdgeDAOIF mdEdge)
  : addParentVertex(child, parent, mdEdge)
  {
    this.state.addParentVertex(child, parent, mdEdge);
  }

  // Check permission for removing a parent from an object
  pointcut removeParentVertex(VertexObject parent, VertexObject child, MdEdgeDAOIF mdEdge)
  : execution (* com.runwaysdk.business.graph.VertexObject.removeParent(VertexObject, MdEdgeDAOIF))
  && args(parent, mdEdge) && this(child);

  before(VertexObject child, VertexObject parent, MdEdgeDAOIF mdEdge)
  : removeParentVertex(child, parent, mdEdge)
  {
    this.state.removeParentVertex(child, parent, mdEdge);
  }

  // Check write permission on an object attribute
  pointcut modifyGraphAttribute(GraphObject entity, String attributeName)
  : (
      execution (* com.runwaysdk.business.graph.GraphObject.setValue(String, ..))           ||
      execution (* com.runwaysdk.business.graph.GraphObject.addEnumItem(String, ..))        ||
      execution (* com.runwaysdk.business.graph.GraphObject.clearEnum(String))              ||
      execution (* com.runwaysdk.business.graph.GraphObject.removeEnumItem(String, ..))
  )
   && this(entity) && args(attributeName, ..);

  before(GraphObject entity, String attributeName) :
    modifyGraphAttribute(entity, attributeName)
  {
    this.state.modifyGraphAttribute(entity, attributeName);
  }

}
