/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.session;

import java.sql.Connection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.Signature;

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
import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.business.rbac.MethodActorDAOIF;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.transaction.AbstractTransactionManagement;
import com.runwaysdk.dataaccess.transaction.AttributeNotificationMap;
import com.runwaysdk.dataaccess.transaction.LockObject;
import com.runwaysdk.logging.RunwayLogUtil;
import com.runwaysdk.util.IdParser;

/**
 * Captures the session id and executes authentication.
 */
privileged public abstract aspect AbstractRequestManagement percflow(topLevelSession())
{
  declare                                       precedence : AbstractTransactionManagement+;

  protected RequestState                        requestState;

  protected Set<String>                         setAppLocksSet              = new HashSet<String>();

  private Stack<MdMethodDAOIF>                  mdMethodIFStack             = new Stack<MdMethodDAOIF>();

  protected List<Message>                       messageList                 = new LinkedList<Message>();

  protected Map<String, String>                 idMap                       = new HashMap<String, String>();

  private Map<String, AttributeNotificationMap> attributeNotificationMapMap = new HashMap<String, AttributeNotificationMap>();

  private List<ProblemIF> problemList = new LinkedList<ProblemIF>();

  protected Log log = LogFactory.getLog(AbstractRequestManagement.class);

  public RequestState getRequestState()
  {
    return this.requestState;
  }

  public List<ProblemIF> getProblemList()
  {
    return this.problemList;
  }

  /**
   * Some things, like permission checking, does not need to be done if
   * the request does not occur in an initialized session.
   *
   * @return true if the session has been initialized, false otherwise;
   */
  protected boolean isSessionInitialized()
  {
    if (this.getRequestState().getSession() == null)
    {
      return false;
    }
    else
    {
      return true;
    }
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

//  public pointcut nonThreadRequest(Request request)
//  : (nonAnnotizedRequestEntryPoints() || defaultRequest(Request) || sessionRequest(Request, String)) && @annotation(request);

  public abstract pointcut request();

//  public abstract pointcut request(Request request);

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

//  public pointcut allRequestEntryPoints()
//  : nonAnnotizedRequestEntryPoints() || nonThreadRequest(Request) || threadRequest(Request);

  public abstract pointcut enterSession();

/*
  public pointcut enterSession() :
    execution (@com.runwaysdk.session.Request * *+.*(..))
    || execution (public * com.runwaysdk.dataaccess.io..*.*(..))
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

           && !within(com.runwaysdk.DoNotWeave+)
    ));

  // suite.addTest(WebServiceAdapterTest.suite());
  // suite.addTest(WebServiceInvokeMethodTest.suite());
  // suite.addTest(WebServiceFacadeGenerationTest.suite());
  // suite.addTest(JSONWebServiceInvokeMethodTest.suite());
  // suite.addTest(SeleniumTestSuite.suite());
*/
  public pointcut topLevelSession()
    :  enterSession()
    && !cflowbelow(enterSession());

  protected pointcut topLevelPermission(String _sessionId)
  :  (sessionRequest(Request, String)
     && !cflowbelow(sessionRequest(Request, String))
     ) && args(_sessionId, ..);

  // these objects should be unlocked at the end of the session request
  protected pointcut setAppLock(String id)
    : (execution (* com.runwaysdk.dataaccess.transaction.LockObject.recordAppLock(String)) && args(id))
  && cflow(topLevelSession());

  before(String id) :  setAppLock(id)
  {
    setAppLocksSet.add(id);
  }

  protected pointcut throwProblem(ProblemIF problemIF)
  : (execution (* com.runwaysdk.ProblemIF+.throwIt())
  && target(problemIF));
  before (ProblemIF problemIF) : throwProblem(problemIF)
  {
    log.info( RunwayLogUtil.formatLoggableMessage(problemIF.getDeveloperMessage(), problemIF.getLocalizedMessage()) );
    problemList.add(problemIF);
  }

  protected pointcut getProblemsInCurrentRequest()
  : (execution (* com.runwaysdk.session.RequestState.getProblemsInCurrentRequest()));
  Object around () : getProblemsInCurrentRequest()
  {
    return problemList;
  }
  
  // Execute the create Commands now, but save the deletes for later.
  protected pointcut attributeNotificationMap(AttributeNotificationMap attributeNotificationMap)
    : execution(com.runwaysdk.dataaccess.transaction.AttributeNotificationMap+.new(..)) && this(attributeNotificationMap);

  after(AttributeNotificationMap attributeNotificationMap)
    : attributeNotificationMap(attributeNotificationMap)
  {
    this.attributeNotificationMapMap.put(attributeNotificationMap.getMapKey(), attributeNotificationMap);
  }

  protected pointcut getConnection()
  :  call(Connection Database.getConnection())
  && !within(RequestState)
  && !withincode(* com.runwaysdk.dataaccess.transaction.AbstractTransactionManagement+.getDDLConnection())
  && !withincode(* com.runwaysdk.dataaccess.database.general.MySQL.getNextSequenceNumber(..))
  && !withincode(* com.runwaysdk.dataaccess.database.general.SQLServer.getNextSequenceNumber(..));

  Object around() : getConnection()
  {
    return this.getRequestState().getDatabaseConnection();
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
    this.idMap.put(newId, oldTempId);
  }

  // //////////////////////////////////////////////////////////////////////////////////////////////////////
  pointcut getCurrentSession()
  : execution (* com.runwaysdk.session.Session.getCurrentSession());
  Object around() : getCurrentSession()
  {
    return this.getRequestState().getSession();
  }

  pointcut getCurrentRequestState()
  : execution (* com.runwaysdk.session.RequestState.getCurrentRequestState());
  Object around() : getCurrentRequestState()
  {
    return this.getRequestState();
  }

  protected pointcut checkMethodExecutePermission(Mutable mutable)
  : ( execution (@Authenticate  * *.*(..)) && this(mutable));

  Object around(Mutable mutable) : checkMethodExecutePermission(mutable)
  {
    Signature signature = thisJoinPointStaticPart.getSignature();

    String type = signature.getDeclaringTypeName();
    String methodName = signature.getName();

    MdClassDAOIF mdClassIF = MdClassDAO.getMdClassDAO(type);

    MdMethodDAOIF mdMethodToExecute = mdClassIF.getMdMethod(methodName);

    // Check execute permission on this method
    if (this.mdMethodIFStack.size() > 0)
    {
      MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
      this.checkMethodExecutePermission_INSTANCE(mutable, mdMethodIF, mdMethodToExecute);
    }
    else
    {
      UserDAOIF userIF = this.getRequestState().getSession().getUser();
      this.checkMethodExecutePermission_INSTANCE(userIF, mutable, mdMethodToExecute);
    }

    this.mdMethodIFStack.push(mdMethodToExecute);

    if (this.getRequestState().getSession() != null)
    {
      ( (Session) this.getRequestState().getSession() ).setFirstMdMethodDAOIF(mdMethodToExecute);
    }

    Object result;
    try
    {
      result = proceed(mutable);
    }
    finally
    {
      this.mdMethodIFStack.pop();
    }
    return result;
  }

  protected pointcut checkStaticMethodExecutePermission()
  : ( execution (@Authenticate  static * *.*(..)));

  Object around() : checkStaticMethodExecutePermission()
  {
    Signature signature = thisJoinPointStaticPart.getSignature();
    String type = signature.getDeclaringTypeName();
    String methodName = signature.getName();

    MdTypeDAOIF mdTypeIF = MdTypeDAO.getMdTypeDAO(type);
    MdMethodDAOIF mdMethodToExecute = mdTypeIF.getMdMethod(methodName);
    Session _session = (Session) this.getRequestState().getSession();

    // Check execute permission on this method
    if (this.mdMethodIFStack.size() > 0)
    {
      MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
      this.checkMethodExecutePermission_STATIC(mdTypeIF, mdMethodIF, mdMethodToExecute);
    }
    else
    {
      if (this.isSessionInitialized())
      {
        UserDAOIF userIF = _session.getUser();
        this.checkMethodExecutePermission_STATIC(userIF, mdTypeIF, mdMethodToExecute);
      }
    }

    this.mdMethodIFStack.push(mdMethodToExecute);

    if (_session != null)
    {
      _session.setFirstMdMethodDAOIF(mdMethodToExecute);
    }

    Object result;
    try
    {
      result = proceed();
    }
    finally
    {
      this.mdMethodIFStack.pop();
    }

    return result;
  }

  private void checkMethodExecutePermission_INSTANCE(Mutable mutable, MdMethodDAOIF mdMethodIF, MdMethodDAOIF mdMethodToExecute)
  {
    boolean access = MethodFacade.checkExecuteAccess(mdMethodIF, mdMethodToExecute);

    if (!access)
    {
      String errorMsg = "Method [" + mdMethodIF.getName() + "] does not have execute permission for the method [" + mdMethodToExecute.getName() + "] on entity [" + mutable.getId() + "]";
      throw new DomainErrorException(errorMsg);
    }
  }

  private void checkMethodExecutePermission_STATIC(MdTypeDAOIF mdTypeIF, MdMethodDAOIF mdMethodIF, MdMethodDAOIF mdMethodToExecute)
  {
    boolean access = MethodFacade.checkExecuteAccess(mdMethodIF, mdMethodToExecute);

    if (!access)
    {
      String errorMsg = "Method [" + mdMethodIF.getName() + "] does not have execute permission for the method [" + mdMethodToExecute.getName() + "] on [" + mdTypeIF.definesType() + "]";
      throw new DomainErrorException(errorMsg);
    }
  }

  private void checkMethodExecutePermission_INSTANCE(UserDAOIF userIF, Mutable mutable, MdMethodDAOIF mdMethodToExecute)
  {
    boolean access = SessionFacade.checkMethodAccess(this.getRequestState().getSession().getId(), Operation.EXECUTE, mutable, mdMethodToExecute);

    if (!access)
    {
      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the execute permission for the method [" + mdMethodToExecute.getName() + "] on entity [" + mutable.getId() + "]";
      throw new ExecuteInstancePermissionException(errorMsg, mutable, mdMethodToExecute, userIF);
    }
  }

  private void checkMethodExecutePermission_STATIC(UserDAOIF userIF, MdTypeDAOIF mdTypeIF, MdMethodDAOIF mdMethodToExecute)
  {
    boolean access = SessionFacade.checkMethodAccess(this.getRequestState().getSession().getId(), Operation.EXECUTE, mdMethodToExecute);

    if (!access)
    {
      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the execute permission for the method [" + mdMethodToExecute.getName() + "] on [" + mdTypeIF.definesType() + "]";
      throw new ExecuteStaticPermissionException(errorMsg, mdTypeIF, mdMethodToExecute, userIF);
    }
  }

  // Ensures a userIF has create permissions on a Business when requesting a new
  // instance
//  pointcut getNewBusinessFromFacade(String type)
//  : call (* com.runwaysdk.business.BusinessFacade.newBusiness(..)) && args(type)
//  && withincode (* com.runwaysdk.facade.Facade.newBusiness(String, String))
//  && (cflow(topLevelPermission(String)));
//  Object around(String type) : getNewBusinessFromFacade(type)
//  {
//    Business business = (Business) proceed(type);
//
//    // Check execute permission on this method
//    if (this.mdMethodIFStack.size() > 0)
//    {
//      MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
//      checkEntityCreatePermission(mdMethodIF, business);
//    }
//    else
//    {
//      checkEntityCreatePermission(sessionId, business);
//    }
//
//    return business;
//  }

  pointcut getNewBusinessFromFacade(String type)
  : call (* com.runwaysdk.business.BusinessFacade.newBusiness(..)) && args(type)
  && withincode (* com.runwaysdk.facade.Facade.newBusiness(String, String));
  Object around(String type) : getNewBusinessFromFacade(type)
  {
    Business business = (Business) proceed(type);

    if (this.isSessionInitialized())
    {
      // Check execute permission on this method
      if (this.mdMethodIFStack.size() > 0)
      {
        MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
        checkEntityCreatePermission(mdMethodIF, business);
      }
      else
      {
        this.checkEntityCreatePermission(business);
      }
    }

    return business;
  }

  // Used to ensure that a userIF must have a lock on the object in order to
  // modify it.
  pointcut applyEntity(Entity entity)
  : execution (* com.runwaysdk.business.Entity.apply()) && this(entity);
  before(Entity entity)
  : applyEntity(entity)
  {
    if (this.isSessionInitialized())
    {
      EntityDAO entityDAO = entity.entityDAO;
      UserDAOIF userIF = this.getRequestState().getSession().getUser();

      // Check if userIF has a lock on the object.
      if (entity instanceof Element)
      {
        checkLock((Element) entity);
      }

      if (entityDAO.isNew())
      {
        // If the entityDAOIF is new and no owner has been defined, then set the
        // owner to the userIF currently performing the action

        // Do not overwrite the owner if we are importing the object.
        if (!entityDAO.isImport() && entityDAO.hasAttribute(ElementInfo.OWNER)
            && ( entityDAO.getAttributeIF(ElementInfo.OWNER).getValue().trim().equals("") ))
        {
          entityDAO.getAttribute(ElementInfo.OWNER).setValue(userIF.getId());
        }

        if (this.mdMethodIFStack.size() > 0)
        {
          MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
          checkEntityCreatePermission(mdMethodIF, entity);
        }
        else
        {
          this.checkEntityCreatePermission(entity);
        }
      }
      else
      {
        if (this.mdMethodIFStack.size() > 0)
        {
          MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
          checkEntityWritePermission(mdMethodIF, entity);
        }
        else
        {
          this.checkEntityWritePermission(entity);
        }
      }

      if (entityDAO.hasAttribute(ElementInfo.LAST_UPDATED_BY))
      {

        if (this.mdMethodIFStack.size() > 0 && entity.hasAttribute(ElementInfo.LOCKED_BY)
            && !entity.getValue(ElementInfo.LOCKED_BY).equals(userIF.getId()))
        {
          MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
          MethodActorDAOIF methodActorIF = MethodFacade.getMethodActorIF(mdMethodIF);
          // I don't think this will ever be null, as the above
          // checkEntityWritePermission method would have thrown an exception.
          if (methodActorIF != null)
          {
            if (!entityDAO.isImport() &&
                (!entityDAO.isImportResolution() || (entityDAO.isImportResolution() && entityDAO.isMasteredHere())))
            {
              entityDAO.getAttribute(ElementInfo.LAST_UPDATED_BY).setValue(methodActorIF.getId());
            }
          }
        }
        else
        {
          if (!entityDAO.isImport() &&
              (!entityDAO.isImportResolution() || (entityDAO.isImportResolution() && entityDAO.isMasteredHere())))
          {
            entityDAO.getAttribute(ElementInfo.LAST_UPDATED_BY).setValue(userIF.getId());
          }
        }
      }
    }
  }

  // Check write permission on an object. Check on userLock().
  pointcut elementLockedByCheck(Element element)
    : (execution (* com.runwaysdk.business.Element+.userLock(String)))
      && target(element);
  before(Element element)
  : elementLockedByCheck(element)
  {
    if (this.isSessionInitialized())
    {
      if (this.mdMethodIFStack.size() > 0)
      {
        MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
        checkEntityWritePermission(mdMethodIF, element);
      }
      else
      {
        this.checkEntityWritePermission(element);
      }
    }
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
    if (this.isSessionInitialized())
    {
      if (entity instanceof Struct)
      {
        checkAttributePermissions((Struct) entity, attributeName);
      }
      else
      {
        checkAttributePermissions((Element) entity, attributeName);
      }
    }
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
    if (this.isSessionInitialized())
    {
      checkAttributePermissions(element, attributeName);
    }
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

  private void checkAttributePermissions(Mutable mutable, String attributeName)
  {
    UserDAOIF userIF = this.getRequestState().getSession().getUser();

    // Write permissions are not checked when the containing object is new.
    if (mutable.isNew())
    {
      return;
    }

    // check to make sure the object is locked before setting a value on an
    // attribute
    if (mutable instanceof Element)
    {
      checkLock((Element) mutable);
    }

    MdAttributeDAOIF mdAttribute = mutable.getMdAttributeDAO(attributeName);

//    boolean hasLock = mutable.hasAttribute(ElementInfo.LOCKED_BY);
//    String lockById = mutable.getValue(ElementInfo.LOCKED_BY);

    if (this.mdMethodIFStack.size() > 0)
    {
      MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();

      boolean access = MethodFacade.checkAttributeAccess(mdMethodIF, Operation.WRITE, mutable, mdAttribute);

      // IMPORTANT: If the method does not have access check if the user has permissions.
      if(!access)
      {
        access = SessionFacade.checkAttributeAccess(this.getRequestState().getSession().getId(), Operation.WRITE, mutable, mdAttribute);
      }

      if (!access)
      {
        String errorMsg = "Method [" + mdMethodIF.getName() + "] does not have the write permission for attribute ["
            + mdAttribute.definesAttribute() + "] on type [" + mutable.getType() + "] with id [" + mutable.getId() + "]";
        throw new DomainErrorException(errorMsg);
      }
    }
    else
    {
      boolean access = SessionFacade.checkAttributeAccess(this.getRequestState().getSession().getId(), Operation.WRITE, mutable, mdAttribute);

      if (!access)
      {
        String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the write permission for attribute ["
            + mdAttribute.definesAttribute() + "] on type [" + mutable.getType() + "]";
        throw new AttributeWritePermissionException(errorMsg, mutable, mdAttribute, userIF);
      }
    }
  }

  private void checkAttributePermissions(Struct struct, String attributeName)
  {
    // Check permissions on Struct objects when they are instantiated directly
    // and not
    // through a containing Entity
    if (struct.parent == null)
    {
      // Write permissions are not checked when the containing object is new.
      if (struct.isNew())
      {
        return;
      }

      StructDAO structDAO = struct.getStructDAO();

      MdAttributeConcreteDAOIF mdAttribute = structDAO.getMdAttributeDAO(attributeName);

      if (this.mdMethodIFStack.size() > 0)
      {
        MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
        boolean access = MethodFacade.checkAttributeAccess(mdMethodIF, Operation.WRITE, struct, mdAttribute);

        if (!access)
        {
          String errorMsg = "Method [" + mdMethodIF.getName() + "] does not have the write permission for attribute [" + mdAttribute.definesAttribute() + "] on type [" + struct.getType() + "] with id [" + struct.getId() + "]";
          throw new DomainErrorException(errorMsg);
        }
      }
      else
      {
        boolean access = SessionFacade.checkAttributeAccess(this.getRequestState().getSession().getId(), Operation.WRITE, struct, mdAttribute);

        if (!access)
        {
          UserDAOIF userIF = this.getRequestState().getSession().getUser();

          String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the write permission for attribute [" + mdAttribute.definesAttribute() + "] on type [" + struct.getType() + "]";
          throw new AttributeWritePermissionException(errorMsg, struct, mdAttribute, userIF);
        }
      }
    }
  }

  // Check delete permission on an object
  pointcut deleteMutable(Mutable mutable)
    : execution (* com.runwaysdk.business.Mutable.delete()) && this(mutable);
  before(Mutable mutable) : deleteMutable(mutable)
  {
    if (this.isSessionInitialized())
    {
      UserDAOIF userIF = this.getRequestState().getSession().getUser();

      if (this.mdMethodIFStack.size() > 0)
      {
        MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
        boolean access = MethodFacade.checkAccess(mdMethodIF, Operation.DELETE, mutable);

        if (!access && !checkUserDeletePermissions(mutable, userIF))
        {
          String errorMsg = "Method [" + mdMethodIF.getName() + "] does not have permission to delete [" + mutable.getType() + "] instance with id [" + mutable.getId() + "].";
          throw new DomainErrorException(errorMsg);
        }
      }
      else
      {
        boolean access = checkUserDeletePermissions(mutable, userIF);

        if (!access)
        {
          String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have permission to delete [" + mutable.getType() + "] instances.";
          throw new DeletePermissionException(errorMsg, mutable, userIF);
        }
      }
    }
  }

  private boolean checkUserDeletePermissions(Mutable mutable, UserDAOIF userIF)
  {
    // Check if userIF has a lock on the object.
    if (mutable.hasAttribute(ElementInfo.LOCKED_BY))
    {
      Element element = (Element) mutable;

      ( LockObject.getLockObject() ).appLock(mutable.getId());

      element.setDataEntity( ( EntityDAO.get(element.getId()).getEntityDAO() ));

      String lockedBy = element.getValue(ElementInfo.LOCKED_BY);

      // we do not perform the lockedby check on new BusinessDAOs
      if (!lockedBy.equals(userIF.getId()) && !lockedBy.trim().equals(""))
      {
        // Release the lock on this object
        ( LockObject.getLockObject() ).releaseAppLock(element.getId());

        String err = "User [" + userIF.getSingleActorName() + "] must have a lock on the object in order to delete it.";
        throw new LockException(err, element, "ExistingLockException");
      }
    }

    return SessionFacade.checkAccess(this.getRequestState().getSession().getId(), Operation.DELETE, mutable);
  }

  // Check permission for adding a child to an object
  pointcut addChildObject(Business parentBusiness, Business childBusiness, String relationshipType)
    : execution (* com.runwaysdk.business.Business.addChild(Business, String))
      && args(childBusiness, relationshipType) && this(parentBusiness);
  before(Business parentBusiness, Business childBusiness, String relationshipType)
    : addChildObject(parentBusiness, childBusiness, relationshipType)
  {
    if (this.isSessionInitialized())
    {
      this.checkAddChildObject(parentBusiness, childBusiness.getId(), relationshipType);
    }
  }

  // Check permission for adding a child to an object
  pointcut addChildId(Business parentBusiness, String childId, String relationshipType)
    : execution (* com.runwaysdk.business.Business.addChild(String, String))
      && args(childId, relationshipType) && this(parentBusiness);
  before(Business parentBusiness, String childId, String relationshipType)
    : addChildId(parentBusiness, childId, relationshipType)
  {
    if (this.isSessionInitialized())
    {
      this.checkAddChildObject(parentBusiness, childId, relationshipType);
    }
  }

  /**
   * Checks if session userIF has permission to add the given child object to
   * the given parent object.
   *
   * @param parentBusiness
   *          object that the child object is added to.
   * @param childId
   *          reference to the child object.
   * @param relationship
   *          type of involved.
   */
  private void checkAddChildObject(Business parentBusiness, String childId, String relationshipType)
  {
    BusinessDAO parentBusinessDAO = parentBusiness.businessDAO();

    MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(relationshipType);

    if (this.mdMethodIFStack.size() > 0)
    {
      MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
      boolean access = MethodFacade.checkRelationshipAccess(mdMethodIF, Operation.ADD_CHILD, parentBusiness, mdRelationshipIF.getId());

      if (!access)
      {

        String errorMsg = "Method [" + mdMethodIF.getName() + "] does not have the add_child permission for relationship [" + mdRelationshipIF.definesType() + "] on type [" + parentBusinessDAO.getType() + "]";
        throw new DomainErrorException(errorMsg);
      }
    }
    else
    {
      boolean access = SessionFacade.checkRelationshipAccess(this.getRequestState().getSession().getId(), Operation.ADD_CHILD, parentBusiness, mdRelationshipIF.getId());

      if (!access)
      {
        Business childBusiness = Business.get(childId);
        UserDAOIF userIF = this.getRequestState().getSession().getUser();

        String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the add_child permission for relationship [" + mdRelationshipIF.definesType() + "] on type [" + parentBusinessDAO.getType() + "]";
        throw new AddChildPermissionException(errorMsg, parentBusiness, childBusiness, mdRelationshipIF, userIF);
      }
    }
  }

  // Check permission for removing a child from an object
  pointcut removeChildObject(Business parentBusiness, Relationship relationship)
    : execution (* com.runwaysdk.business.Business.removeChild(Relationship))
      && args(relationship) && this(parentBusiness);
  before(Business parentBusiness, Relationship relationship)
    : removeChildObject(parentBusiness, relationship)
  {
    if (this.isSessionInitialized())
    {
      this.checkRemoveChildObject(parentBusiness, relationship.getChildId(), relationship.getType());
    }
  }

  pointcut removeChildId(Business parentBusiness, String relationshipId)
    : execution (* com.runwaysdk.business.Business.removeChild(String))
      && args(relationshipId) && this(parentBusiness);
  before(Business parentBusiness, String relationshipId)
    : removeChildId(parentBusiness, relationshipId)
  {
    if (this.isSessionInitialized())
    {
      MdClassDAOIF mdClassIF = MdClassDAO.getMdClassByRootId(IdParser.parseMdTypeRootIdFromId(relationshipId));
      Relationship relationship = Relationship.get(relationshipId);
      this.checkRemoveChildObject(parentBusiness, relationship.getChildId(), mdClassIF.definesType());
    }
  }

  pointcut removeAllChildObjects(Business parentBusiness, Business childBusiness, String relationshipType)
    : execution (* com.runwaysdk.business.Business.removeAllChildren(Business, String))
      && args(childBusiness, relationshipType) && this(parentBusiness);
  before(Business parentBusiness, Business childBusiness, String relationshipType)
    : removeAllChildObjects(parentBusiness, childBusiness, relationshipType)
  {
    if (this.isSessionInitialized())
    {
      this.checkRemoveChildObject(parentBusiness, childBusiness.getId(), relationshipType);
    }
  }

  pointcut removeAllChildId(Business parentBusiness, String childId, String relationshipType)
    : execution (* com.runwaysdk.business.Business.removeAllChildren(String, String))
      && args(childId, relationshipType) && this(parentBusiness);
  before(Business parentBusiness, String childId, String relationshipType)
    : removeAllChildId(parentBusiness, childId, relationshipType)
  {
    if (this.isSessionInitialized())
    {
      this.checkRemoveChildObject(parentBusiness, childId, relationshipType);
    }
  }

  /**
   * Checks if session userIF has permission to remove the given child object
   * from the given parent object. Either childId or relationshipId is null, but
   * not both.
   *
   *
   * @param parentBusiness
   *          object that the child object is removed from.
   * @param childId
   *          id to the child object.
   * @param relationshipType
   *          name of the relationship type.
   * @param relationshipId
   *          id to the relationship.
   */
  private void checkRemoveChildObject(Business parentBusiness, String childId, String relationshipType)
  {
    BusinessDAO parentBusinessDAO = parentBusiness.businessDAO();

    MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(relationshipType);

    if (this.mdMethodIFStack.size() > 0)
    {
      MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
      boolean access = MethodFacade.checkRelationshipAccess(mdMethodIF, Operation.DELETE_CHILD, parentBusiness, mdRelationshipIF.getId());

      if (!access)
      {
        UserDAOIF userIF = this.getRequestState().getSession().getUser();

        String errorMsg = "Method [" + userIF.getSingleActorName() + "] does not have the delete_child permission for relationship [" + mdRelationshipIF.definesType() + "] on type [" + parentBusinessDAO.getType() + "]";
        throw new DomainErrorException(errorMsg);
      }
    }
    else
    {
      boolean access = SessionFacade.checkRelationshipAccess(this.getRequestState().getSession().getId(), Operation.DELETE_CHILD, parentBusiness, mdRelationshipIF.getId());

      if (!access)
      {
        Business childBusiness = Business.get(childId);
        UserDAOIF userIF = this.getRequestState().getSession().getUser();

        String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the delete_child permission for relationship [" + mdRelationshipIF.definesType() + "] on type [" + parentBusinessDAO.getType() + "]";
        throw new DeleteChildPermissionException(errorMsg, parentBusiness, childBusiness, mdRelationshipIF, userIF);
      }
    }
  }

  // Check permission for adding a parent to an object
  pointcut addParentObject(Business parentBusiness, Business childBusiness, String relationshipType)
    : execution (* com.runwaysdk.business.Business.addParent(Business, String))
      && args(parentBusiness, relationshipType) && this(childBusiness);
  before(Business parentBusiness, Business childBusiness, String relationshipType)
    : addParentObject(parentBusiness, childBusiness, relationshipType)
  {
    if (this.isSessionInitialized())
    {
      this.checkAddParentObject(childBusiness, parentBusiness.getId(), relationshipType);
    }
  }

  // Check permission for adding a parent to an object
  pointcut addParentId(String parentId, Business childBusiness, String relationshipType)
    : execution (* com.runwaysdk.business.Business.addParent(String, String))
      && args(parentId, relationshipType) && this(childBusiness);
  before(String parentId, Business childBusiness, String relationshipType)
    : addParentId(parentId, childBusiness, relationshipType)
  {
    if (this.isSessionInitialized())
    {
      this.checkAddParentObject(childBusiness, parentId, relationshipType);
    }
  }

  /**
   * Checks if session userIF has permission to add the given parent object to
   * the given child object.
   *
   * @param childBusiness
   *          object that the parent object is added to.
   * @param parentId
   *          id to the child object.
   * @param relationship
   *          type of relationship involved.
   */
  private void checkAddParentObject(Business childBusiness, String parentId, String relationshipType)
  {
    BusinessDAO childBusinessDAO = childBusiness.businessDAO();

    MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(relationshipType);

    if (this.mdMethodIFStack.size() > 0)
    {
      MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
      boolean access = MethodFacade.checkRelationshipAccess(mdMethodIF, Operation.ADD_PARENT, childBusiness, mdRelationshipIF.getId());

      if (!access)
      {
        String errorMsg = "Method [" + mdMethodIF.getName() + "] does not have the add_parent permission for relationship [" + mdRelationshipIF.definesType() + "] on type [" + childBusinessDAO.getType() + "]";
        throw new DomainErrorException(errorMsg);
      }
    }
    else
    {
      boolean access = SessionFacade.checkRelationshipAccess(this.getRequestState().getSession().getId(), Operation.ADD_PARENT, childBusiness, mdRelationshipIF.getId());

      if (!access)
      {
        Business parentBusiness = Business.get(parentId);
        UserDAOIF userIF = this.getRequestState().getSession().getUser();

        String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the add_parent permission for relationship [" + mdRelationshipIF.definesType() + "] on type [" + childBusinessDAO.getType() + "]";
        throw new AddParentPermissionException(errorMsg, parentBusiness, childBusiness, mdRelationshipIF, userIF);
      }
    }
  }

  // Check permission for adding a parent to an object
  pointcut removeParentObject(Business childBusiness, Relationship relationship)
    : execution (* com.runwaysdk.business.Business.removeParent(Relationship))
      && args(relationship) && this(childBusiness);
  before(Business childBusiness, Relationship relationship)
    : removeParentObject(childBusiness, relationship)
  {
    if (this.isSessionInitialized())
    {
      this.checkRemoveParentObject(childBusiness, relationship.getParentId(), relationship.getType());
    }
  }

  pointcut removeParentId(Business childBusiness, String relationshipId)
    : execution (* com.runwaysdk.business.Business.removeParent(String))
      && args(relationshipId) && this(childBusiness);
  before(Business childBusiness, String relationshipId)
    : removeParentId(childBusiness, relationshipId)
  {
    if (this.isSessionInitialized())
    {
      MdClassDAOIF mdClassIF = MdClassDAO.getMdClassByRootId(IdParser.parseMdTypeRootIdFromId(relationshipId));
      Relationship relationship = Relationship.get(relationshipId);
      this.checkRemoveParentObject(childBusiness, relationship.getParentId(), mdClassIF.definesType());
    }
  }

  pointcut removeAllParentObjects(Business parentBusiness, Business childBusiness, String relationshipType)
    : execution (* com.runwaysdk.business.Business.removeAllParents(Business, String))
      && args(parentBusiness, relationshipType) && this(childBusiness);
  before(Business parentBusiness, Business childBusiness, String relationshipType)
    : removeAllParentObjects(parentBusiness, childBusiness, relationshipType)
  {
    if (this.isSessionInitialized())
    {
      checkRemoveParentObject(childBusiness, parentBusiness.getId(), relationshipType);
    }
  }

  // Check permission for adding a parent to an object
  pointcut removeAllParentIds(String parentId, Business childBusiness, String relationshipType)
    : execution (* com.runwaysdk.business.Business.removeAllParents(String, String))
      && args(parentId, relationshipType) && this(childBusiness);
  before(String parentId, Business childBusiness, String relationshipType)
    : removeAllParentIds(parentId, childBusiness, relationshipType)
  {
    if (this.isSessionInitialized())
    {
      checkRemoveParentObject(childBusiness, parentId, relationshipType);
    }
  }

  /**
   * Checks if session userIF has permission to remove the given parent object
   * from the given child object.
   *
   * @param childBusiness
   *          object that the parent object is removed from.
   * @param parentId
   *          reference to the child object.
   * @param relationshipType
   *          name of the relationship type.
   * @param relationshipId
   *          reference to the relationship.
   */
  private void checkRemoveParentObject(Business childBusiness, String parentId, String relationshipType)
  {
    BusinessDAO childBusinessDAO = childBusiness.businessDAO();

    MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(relationshipType);

    if (this.mdMethodIFStack.size() > 0)
    {
      MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
      boolean access = MethodFacade.checkRelationshipAccess(mdMethodIF, Operation.DELETE_PARENT, childBusiness, mdRelationshipIF.getId());

      if (!access)
      {
        String errorMsg = "Method [" + mdMethodIF.getName() + "] does not have the delete_parent permission for relationship [" + mdRelationshipIF.definesType() + "] on type [" + childBusinessDAO.getType() + "]";
        throw new DomainErrorException(errorMsg);
      }
    }
    else
    {
      boolean access = SessionFacade.checkRelationshipAccess(this.getRequestState().getSession().getId(), Operation.DELETE_PARENT, childBusiness, mdRelationshipIF.getId());

      if (!access)
      {
        Business parentBusiness = Business.get(parentId);
        UserDAOIF userIF = this.getRequestState().getSession().getUser();

        String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the delete_parent permission for relationship [" + mdRelationshipIF.definesType() + "] on type [" + childBusinessDAO.getType() + "]";
        throw new DeleteParentPermissionException(errorMsg, parentBusiness, childBusiness, mdRelationshipIF, userIF);
      }
    }
  }

  pointcut promote(Business business, String transitionName)
  : ( execution (* com.runwaysdk.business.Business.promote(String)) &&
      args(transitionName)) && target(business);
  before(Business business, String transitionName)
  : promote(business, transitionName)
  {
    if (this.isSessionInitialized())
    {
      // Check if userIF has a lock on the object.
      checkLock(business);

      if (this.mdMethodIFStack.size() > 0)
      {
        MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
        boolean access = MethodFacade.checkPromoteAccess(mdMethodIF, business, transitionName);

        if (!access)
        {
          MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(business.getType());

          String errorMsg = "Method [" + mdMethodIF.getName() + "] does not have permission to promote type [" + mdBusinessIF.definesType() + "] using transition [" + transitionName + "]";
          throw new DomainErrorException(errorMsg);
        }
      }
      else
      {
        boolean access = SessionFacade.checkPromoteAccess(this.getRequestState().getSession().getId(), business, transitionName);

        if (!access)
        {
          MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(business.getType());
          UserDAOIF userIF = this.getRequestState().getSession().getUser();

          String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have permission to promote type [" + mdBusinessIF.definesType() + "] using transition [" + transitionName + "]";
          throw new PromotePermissionException(errorMsg, business, transitionName, userIF);
        }
      }
    }
  }



  /**
   * Throws a business exception if the userIF bound to the given session does
   * not have a lock on the given element.
   *
   * @param element
   */
  private void checkLock(Element element)
  {
    if (element.isNew())
    {
      return;
    }

    // If a thread has a lock on it, it could be an application or a user lock.
    Thread threadThatLocksObject = ( LockObject.getLockObject() ).getThreadForAppLock(element.getId());

    if (threadThatLocksObject != null)
    {
      // User has an app lock
      if (LockObject.getCurrentThread() == threadThatLocksObject)
      {
        return;
      }
      else
      {
        String err = "You must obtain an application lock before modifying an object. " + "Another thread has a lock on the object with id [" + element.getId() + "].";
        throw new LockException(err);
      }
    }
    ( LockObject.getLockObject() ).appLock(element.getId());

    UserDAOIF userIF = this.getRequestState().getSession().getUser();

    // Check if the object is locked by another user.
    if (element.hasAttribute(ElementInfo.LOCKED_BY))
    {
      String lockedBy = element.getValue(ElementInfo.LOCKED_BY);
      // we do not perform the lockedby check on new BusinessDAOs
      if (!lockedBy.equals(userIF.getId()) && !lockedBy.trim().equals(""))
      {
        ( LockObject.getLockObject() ).releaseAppLock(element.getId());

        String err = "User [" + userIF.getSingleActorName() + "] must have a lock on the object with id [" + element.getId() + "] in order to modify it. " + "It is currently locked by someone else.";
        throw new LockException(err, element, "ExistingLockException");
      }
      // User has a user lock
      else if (lockedBy.equals(userIF.getId()))
      {
        return;
      }
    }

    ( LockObject.getLockObject() ).releaseAppLock(element.getId());

    String err = "User [" + userIF.getSingleActorName() + "] must have a lock on the object with id [" + element.getId() + "] in order to modify it. ";
    throw new LockException(err, element, "NeedLockException");
  }

  /**
   * Checks if the session userIF has create permissions on the given Entity.
   *
   * @param entity
   */
  private void checkEntityCreatePermission(Entity entity)
  {
    boolean access = SessionFacade.checkAccess(this.getRequestState().getSession().getId(), Operation.CREATE, entity);

    if (!access)
    {
      UserDAOIF sessionUserIF = this.getRequestState().getSession().getUser();
      String errorMsg = "User [" + sessionUserIF.getSingleActorName() + "] does not have permission to create [" + entity.getType() + "] instances.";
      throw new CreatePermissionException(errorMsg, entity, sessionUserIF);
    }
  }

  /**
   * Checks if the currently executing method has create permissions on the
   * given Entity.
   *
   * @param mdMethodIF
   * @param entity
   */
  private void checkEntityCreatePermission(MdMethodDAOIF mdMethodIF, Entity entity)
  {
    boolean access = MethodFacade.checkAccess(mdMethodIF, Operation.CREATE, entity);

    if (!access)
    {
      String errorMsg = "Method [" + mdMethodIF.getName() + "] does not have permission to create [" + entity.getType() + "] instances.";
      throw new DomainErrorException(errorMsg);
    }
  }

  /**
   * Checks if the session userIF has write permissions on the given Entity.
   *
   * @param entity
   */
  private void checkEntityWritePermission(Entity entity)
  {
    boolean access = SessionFacade.checkAccess(this.getRequestState().getSession().getId(), Operation.WRITE, entity);

    if (!access)
    {
      UserDAOIF sessionUserIF = this.getRequestState().getSession().getUser();
      String errorMsg = "User [" + sessionUserIF.getSingleActorName() + "] does not have permission to write to [" + entity.getType() + "] instances.";
      throw new WritePermissionException(errorMsg, entity, sessionUserIF);
    }
  }

  /**
   * Checks if the currently executing method has write permissions on the given
   * Entity.
   *
   * @param mdMethodIF
   * @param entity
   */
  private void checkEntityWritePermission(MdMethodDAOIF mdMethodIF, Entity entity)
  {
    boolean access = MethodFacade.checkAccess(mdMethodIF, Operation.WRITE, entity);

    if (!access && !SessionFacade.checkAccess(this.getRequestState().getSession().getId(), Operation.WRITE, entity))
    {
      String errorMsg = "Method [" + mdMethodIF.getName() + "] does not have permission to write to [" + entity.getType() + "] instance with id [" + entity.getId() + "].";
      throw new DomainErrorException(errorMsg);
    }
  }

  protected pointcut throwMessage(Message message)
  : (execution (* com.runwaysdk.business.Message+.throwIt())
  && target(message));

  before(Message message) : throwMessage(message)
  {
    if (message instanceof Warning)
    {
      log.info( RunwayLogUtil.formatLoggableMessage(message.getDeveloperMessage(), message.getLocalizedMessage()) );
    }
    else if (message instanceof Information)
    {
      log.info( RunwayLogUtil.formatLoggableMessage(message.getDeveloperMessage(), message.getLocalizedMessage()) );
    }

    messageList.add(message);
  }

  protected pointcut throwAttributeNotification(AttributeNotification attributeNotification)
  : (call (* com.runwaysdk.AttributeNotification+.throwIt())
  && target(attributeNotification));

  before(AttributeNotification attributeNotification) : throwAttributeNotification(attributeNotification)
  {
    String mapKey = AttributeNotificationMap.getMapKey(attributeNotification.getComponentId(), attributeNotification.getAttributeName());
    AttributeNotificationMap attributeNotificationMap = this.attributeNotificationMapMap.get(mapKey);

    if (attributeNotificationMap != null)
    {
      attributeNotificationMap.convertAttributeNotification(attributeNotification);
    }
  }

  // //////////////////////////////////////////////////////////////////////////////////////////////////////

}
