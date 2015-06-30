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
package com.runwaysdk.request;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.healthmarketscience.rmiio.RemoteInputStream;
import com.healthmarketscience.rmiio.RemoteInputStreamClient;
import com.healthmarketscience.rmiio.RemoteInputStreamServer;
import com.healthmarketscience.rmiio.RemoteOutputStream;
import com.healthmarketscience.rmiio.RemoteOutputStreamClient;
import com.healthmarketscience.rmiio.SimpleRemoteInputStream;
import com.healthmarketscience.rmiio.SimpleRemoteOutputStream;
import com.runwaysdk.ClientRequest;
import com.runwaysdk.ClientSession;
import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.MessageExceptionDTO;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.ClassQueryDTO;
import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.business.ComponentQueryDTO;
import com.runwaysdk.business.ElementDTO;
import com.runwaysdk.business.EntityDTO;
import com.runwaysdk.business.EntityQueryDTO;
import com.runwaysdk.business.ExceptionDTO;
import com.runwaysdk.business.MethodMetaData;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.business.RelationshipQueryDTO;
import com.runwaysdk.business.SessionDTO;
import com.runwaysdk.business.SmartExceptionDTO;
import com.runwaysdk.business.StructDTO;
import com.runwaysdk.business.StructQueryDTO;
import com.runwaysdk.business.ValueQueryDTO;
import com.runwaysdk.business.ViewQueryDTO;
import com.runwaysdk.business.ontology.TermAndRelDTO;
import com.runwaysdk.business.ontology.TermDTO;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.transport.conversion.ClientConversionFacade;
import com.runwaysdk.transport.conversion.ConversionFacade;
import com.runwaysdk.util.DTOConversionUtilInfo;
import com.runwaysdk.vault.VaultExceptionDTO;

/**
 * This RMIClientRequest class extends the functionality of ClientRequest by
 * converting any input into a format suitable for the RMIController.
 */
public class RMIClientRequest extends ClientRequest
{
  /**
   *
   */
  private static final long serialVersionUID = -3798463194753755412L;

  /**
   * RemoteAdapter interface object to id server-side RMI implementation.
   */
  private RemoteAdapter     rmiAdapter       = null;

  /**
   * Constructor.
   * 
   * @param clientSession
   * @param locales
   */
  public RMIClientRequest(ClientSession clientSession, Locale[] locales)
  {
    super(clientSession, locales);
    this.init(clientSession);
  }

  /**
   * Constructor.
   * 
   * @param clientSession
   * @param sessionId
   */
  public RMIClientRequest(ClientSession clientSession, String sessionId)
  {
    super(clientSession, sessionId);
    this.init(clientSession);
  }

  /**
   * Constructor that takes in an address to the location of the server hosting
   * the web services as well as a username and parameter.
   * 
   * @param clientSession
   * @param userName
   * @param password
   * @param locales
   */
  public RMIClientRequest(ClientSession clientSession, String userName, String password, Locale[] locales)
  {
    super(clientSession, userName, password, locales);
    this.init(clientSession);
  }

  protected void init(ClientSession clientSession)
  {
    super.init(clientSession);

    try
    {
      // Connect to the server and use the interface
      String address = clientSession.getConnectionLabel().getAddress();
      String lookup = address + CommonProperties.getRMIService();

      rmiAdapter = (RemoteAdapter) Naming.lookup(lookup);
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
      throw new RMIClientException(e);
    }
    catch (RemoteException e)
    {
      e.printStackTrace();

      throw new RMIClientException(e);
    }
    catch (NotBoundException e)
    {
      e.printStackTrace();

      throw new RMIClientException(e);
    }
  }

  protected RemoteAdapter getController()
  {
    return rmiAdapter;
  }

  /**
   * Unbind the current RMIClientRequest. After this is called, this
   * RMIClientRequest cannot be used again.
   */
  public void unbindRMIClientRequest()
  {
    try
    {
      Naming.unbind(this.getClientSession().getConnectionLabel().getAddress() + CommonProperties.getRMIService());
    }
    catch (MalformedURLException e)
    {
      throw new RMIClientException(e);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    catch (NotBoundException e)
    {
      throw new RMIClientException(e);
    }
  }

  /**
   * @see com.runwaysdk.ClientRequest#getTermAllChildren(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
   */
  @SuppressWarnings("unchecked")
  public List<TermAndRelDTO> getTermAllChildren(String parentId, Integer pageNum, Integer pageSize)
  {
    this.clearNotifications();
    List<TermAndRelDTO> tnr;
    try
    {
      tnr = rmiAdapter.getTermAllChildren(this.getSessionId(), parentId, pageNum, pageSize);
    }
    catch (MessageExceptionDTO me)
    {
      tnr = (List<TermAndRelDTO>) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    
    List<TermAndRelDTO> retList = new ArrayList<TermAndRelDTO>();
    for (int i = 0; i < tnr.size(); ++i) {
      ComponentDTOIF dtoCopy = ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, tnr.get(i).getTerm());
      retList.add(new TermAndRelDTO((TermDTO) dtoCopy, tnr.get(i).getRelationshipType(), tnr.get(i).getRelationshipId()));
    }
    
    return retList;
  }
  
  /**
   * @see com.runwaysdk.ClientRequest#addChild(java.lang.String,
   *      java.lang.String, com.runwaysdk.business.RelationshipDTO)
   */
  public RelationshipDTO addChild(String parentId, String childId, String relationshipType)
  {
    this.clearNotifications();
    RelationshipDTO generic;
    try
    {
      generic = rmiAdapter.addChild(this.getSessionId(), parentId, childId, relationshipType);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (RelationshipDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    return ConversionFacade.createTypeSafeCopy(generic, this);
  }

  /**
   * @see com.runwaysdk.ClientRequest#addParent(java.lang.String,
   *      java.lang.String, java.lang.String,)
   */
  public RelationshipDTO addParent(String parentId, String childId, String relationshipType)
  {
    this.clearNotifications();
    RelationshipDTO generic;
    try
    {
      generic = rmiAdapter.addParent(this.getSessionId(), parentId, childId, relationshipType);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (RelationshipDTO) me.getReturnObject();
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    return ConversionFacade.createTypeSafeCopy(generic, this);
  }

  /**
   * @see com.runwaysdk.ClientRequest#deleteBusiness(java.lang.String)
   */
  public void delete(String id)
  {
    this.clearNotifications();
    try
    {
      rmiAdapter.delete(this.getSessionId(), id);
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  /**
   * @see com.runwaysdk.ClientRequest#get(java.lang.String)
   */
  public MutableDTO get(String id)
  {
    this.clearNotifications();
    MutableDTO generic;
    try
    {
      generic = rmiAdapter.get(this.getSessionId(), id);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (MutableDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    return (MutableDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  public EntityQueryDTO getAllInstances(String type, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    this.clearNotifications();

    EntityQueryDTO generic;
    try
    {
      generic = rmiAdapter.getAllInstances(this.getSessionId(), type, sortAttribute, ascending, pageSize, pageNumber);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (EntityQueryDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }

    return (EntityQueryDTO) ConversionFacade.convertGenericQueryToTypeSafe(this, generic);
  }

  /**
   * @see com.runwaysdk.ClientRequest#createSessionComponent(com.runwaysdk.business.SessionDTO)
   */
  public void createSessionComponent(SessionDTO sessionDTO)
  {
    this.clearNotifications();
    SessionDTO generic;
    try
    {
      SessionDTO _sessionDTO = (SessionDTO) ConversionFacade.createGenericCopy(sessionDTO);
      generic = rmiAdapter.createSessionComponent(this.getSessionId(), _sessionDTO);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (SessionDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    ConversionFacade.typeSafeCopy(this, generic, sessionDTO);
  }

  /**
   * @see com.runwaysdk.ClientRequest#createBusiness(com.runwaysdk.business.BusinessDTO)
   */
  public void createBusiness(BusinessDTO businessDTO)
  {
    this.clearNotifications();
    BusinessDTO generic;
    try
    {
      BusinessDTO _businessDTO = (BusinessDTO) ConversionFacade.createGenericCopy(businessDTO);
      generic = rmiAdapter.createBusiness(this.getSessionId(), _businessDTO);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (BusinessDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    ConversionFacade.typeSafeCopy(this, generic, businessDTO);
  }

  /**
   * @see com.runwaysdk.ClientRequest#createBusiness(com.runwaysdk.business.BusinessDTO)
   */
  public void createRelationship(RelationshipDTO relationshipDTO)
  {
    this.clearNotifications();
    RelationshipDTO generic;
    try
    {
      RelationshipDTO _relationshipDTO = (RelationshipDTO) ConversionFacade.createGenericCopy(relationshipDTO);
      generic = rmiAdapter.createRelationship(this.getSessionId(), _relationshipDTO);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (RelationshipDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    ConversionFacade.typeSafeCopy(this, generic, relationshipDTO);
  }

  /**
   * @see com.runwaysdk.ClientRequest#login(java.lang.String, java.lang.String,
   *      Locale[])
   */
  protected String login(String username, String password, Locale[] locales)
  {
    this.clearNotifications();
    try
    {
      this.setSessionId(rmiAdapter.login(username, password, locales));
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    this.setLoginStatus(username, true);
    return this.getSessionId();
  }

  /**
   * @see com.runwaysdk.ClientRequest#login(java.lang.String, java.lang.String,
   *      java.lang.String, Locale[])
   */
  protected String login(String username, String password, String dimensionKey, Locale[] locales)
  {
    this.clearNotifications();
    try
    {
      this.setSessionId(rmiAdapter.login(username, password, dimensionKey, locales));
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    this.setLoginStatus(username, true);
    return this.getSessionId();
  }

  /**
   * @see com.runwaysdk.ClientRequest#setDimension(java.lang.String,
   *      java.lang.String)
   */
  protected void setDimension(String sessionId, String dimensionKey)
  {
    this.clearNotifications();
    try
    {
      rmiAdapter.setDimension(sessionId, dimensionKey);
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  /**
   * @see com.runwaysdk.ClientRequest#changeLogin(java.lang.String,
   *      java.lang.String)
   */
  protected void changeLogin(String username, String password)
  {
    this.clearNotifications();
    try
    {
      rmiAdapter.changeLogin(this.getSessionId(), username, password);
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    this.setIsPublicUser(username);
  }

  /**
   * @see com.runwaysdk.ClientRequest#getSessionUser()
   */
  public BusinessDTO getSessionUser()
  {
    this.clearNotifications();
    BusinessDTO generic;
    try
    {
      generic = rmiAdapter.getSessionUser(this.getSessionId());
    }
    catch (MessageExceptionDTO me)
    {
      generic = (BusinessDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    return (BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  /**
   * @see com.runwaysdk.ClientRequest#getSessionUserRoles()
   */
  @SuppressWarnings("unchecked")
  public Map<String, String> getSessionUserRoles()
  {
    this.clearNotifications();
    Map<String, String> roleMap;
    try
    {
      roleMap = rmiAdapter.getSessionUserRoles(this.getSessionId());
    }
    catch (MessageExceptionDTO me)
    {
      roleMap = (Map<String, String>) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    return roleMap;
  }

  /**
   * @see com.runwaysdk.ClientRequest#loginAnonymous(Locale[])
   */
  protected String loginAnonymous(Locale[] locales)
  {
    this.clearNotifications();
    try
    {
      this.setSessionId(rmiAdapter.loginAnonymous(locales));
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    this.setLoginStatus(true, true);
    return this.getSessionId();
  }

  /**
   * @see com.runwaysdk.ClientRequest#loginAnonymous(java.lang.String, Locale[])
   */
  protected String loginAnonymous(String dimensionKey, Locale[] locales)
  {
    this.clearNotifications();
    try
    {
      this.setSessionId(rmiAdapter.loginAnonymous(dimensionKey, locales));
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    this.setLoginStatus(true, true);
    return this.getSessionId();
  }

  /**
   * @see com.runwaysdk.ClientRequest#logout()
   */
  protected void logout()
  {
    this.clearNotifications();
    try
    {
      rmiAdapter.logout(this.getSessionId());
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    this.setLoginStatus(false, false);
  }

  /**
   * @see com.runwaysdk.constant.ClientRequestIF#newBusiness(java.lang.String)
   */
  public BusinessDTO newBusiness(String type)
  {
    this.clearNotifications();
    BusinessDTO generic;
    try
    {
      generic = rmiAdapter.newBusiness(this.getSessionId(), type);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (BusinessDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    return (BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }
  
  @Override
  public EntityDTO newDisconnectedEntity(String type)
  {
    this.clearNotifications();
    EntityDTO generic;
    try
    {
      generic = rmiAdapter.newDisconnectedEntity(this.getSessionId(), type);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (EntityDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    return (EntityDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }
  
  /**
   * @see com.runwaysdk.constant.ClientRequestIF#newGenericBusiness(java.lang.String)
   */
  public BusinessDTO newGenericBusiness(String type)
  {
    this.clearNotifications();
    BusinessDTO generic;
    try
    {
      generic = rmiAdapter.newBusiness(this.getSessionId(), type);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (BusinessDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    return (BusinessDTO) ConversionFacade.createGenericCopyWithTypeSafeAttributes(this, generic);
  }

  /**
   * @see com.runwaysdk.ClientRequest#update(com.runwaysdk.business.MutableDTO)
   */
  public void update(MutableDTO mutableDTO)
  {
    this.clearNotifications();
    MutableDTO generic;
    try
    {
      MutableDTO send = (MutableDTO) ConversionFacade.createGenericCopy(mutableDTO);
      generic = rmiAdapter.update(this.getSessionId(), send);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (MutableDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    ConversionFacade.typeSafeCopy(this, generic, mutableDTO);
  }

  /**
   * @see com.runwaysdk.ClientRequest#assignMember(java.lang.String,
   *      java.lang.String, java.lang.String...)
   */
  public void assignMember(String userId, String... roles)
  {
    this.clearNotifications();
    try
    {
      rmiAdapter.assignMember(this.getSessionId(), userId, roles);
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  /**
   * @see com.runwaysdk.ClientRequest#removeMember(java.lang.String,
   *      java.lang.String, java.lang.String...)
   */
  public void removeMember(String userId, String... roles)
  {
    this.clearNotifications();
    try
    {
      rmiAdapter.removeMember(this.getSessionId(), userId, roles);
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantStatePermission(java.lang.String,
   *      java.lang.String, String...)
   */
  public void grantStatePermission(String actorId, String stateId, String... operationNames)
  {
    this.clearNotifications();
    try
    {
      rmiAdapter.grantStatePermission(this.getSessionId(), actorId, stateId, operationNames);
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantAttributePermission(java.lang.String,
   *      java.lang.String, String...)
   */
  public void grantAttributePermission(String actorId, String mdAttributeId, String... operationNames)
  {
    this.clearNotifications();
    try
    {
      rmiAdapter.grantAttributePermission(this.getSessionId(), actorId, mdAttributeId, operationNames);
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public void grantAttributeStatePermission(String actorId, String mdAttributeId, String stateId, String... operationNames)
  {
    this.clearNotifications();
    try
    {
      rmiAdapter.grantAttributeStatePermission(this.getSessionId(), actorId, mdAttributeId, stateId, operationNames);
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantTypePermission(java.lang.String,
   *      java.lang.String, String...)
   */
  public void grantTypePermission(String actorId, String mdTypeId, String... operationNames)
  {
    this.clearNotifications();
    try
    {
      rmiAdapter.grantTypePermission(this.getSessionId(), actorId, mdTypeId, operationNames);
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantMethodPermission(java.lang.String,
   *      java.lang.String, String...)
   */
  public void grantMethodPermission(String actorId, String mdMethodId, String... operationNames)
  {
    this.clearNotifications();
    try
    {
      rmiAdapter.grantMethodPermission(this.getSessionId(), actorId, mdMethodId, operationNames);
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  /**
   * @see com.runwaysdk.ClientRequest#promoteObject(java.lang.String,
   *      java.lang.String)
   */
  public void promoteObject(BusinessDTO businessDTO, String transitionName)
  {
    this.clearNotifications();
    BusinessDTO generic;
    try
    {
      generic = rmiAdapter.promoteObject(this.getSessionId(), businessDTO, transitionName);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (BusinessDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    ConversionFacade.typeSafeCopy(this, generic, businessDTO);
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeTypePermission(java.lang.String,
   *      java.lang.String, java.lang.String...)
   */
  public void revokeTypePermission(String actorId, String mdTypeId, String... operationNames)
  {
    this.clearNotifications();
    try
    {
      rmiAdapter.revokeTypePermission(this.getSessionId(), actorId, mdTypeId, operationNames);
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeMethodPermission(java.lang.String,
   *      java.lang.String, java.lang.String...)
   */
  public void revokeMethodPermission(String actorId, String mdMethodId, String... operationNames)
  {
    this.clearNotifications();
    try
    {
      rmiAdapter.revokeMethodPermission(this.getSessionId(), actorId, mdMethodId, operationNames);
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String...)
   */
  public void revokeStatePermission(String actorId, String stateId, String... operationNames)
  {
    this.clearNotifications();
    try
    {
      rmiAdapter.revokeStatePermission(this.getSessionId(), actorId, stateId, operationNames);
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeAttributePermission(java.lang.String,
   *      java.lang.String, java.lang.String...)
   */
  public void revokeAttributePermission(String actorId, String mdAttributeId, String... operationNames)
  {
    this.clearNotifications();
    try
    {
      rmiAdapter.revokeAttributePermission(this.getSessionId(), actorId, mdAttributeId, operationNames);
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public void revokeAttributeStatePermission(String actorId, String mdAttributeId, String stateId, String... operationNames)
  {
    this.clearNotifications();
    try
    {
      rmiAdapter.revokeAttributeStatePermission(this.getSessionId(), actorId, mdAttributeId, stateId, operationNames);
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  /**
   * @see com.runwaysdk.ClientRequest#lock(java.lang.String, java.lang.String)
   */
  public void lock(ElementDTO elementDTO)
  {
    this.clearNotifications();
    EntityDTO generic;
    try
    {
      generic = rmiAdapter.lock(this.getSessionId(), elementDTO.getId());
    }
    catch (MessageExceptionDTO me)
    {
      generic = (EntityDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    ConversionFacade.typeSafeCopy(this, generic, elementDTO);
  }

  /**
   * @see com.runwaysdk.ClientRequest#unlock(ElementDTO)
   */
  public void unlock(ElementDTO elementDTO)
  {
    this.clearNotifications();
    EntityDTO generic;
    try
    {
      generic = rmiAdapter.unlock(this.getSessionId(), elementDTO.getId());
    }
    catch (MessageExceptionDTO me)
    {
      generic = (EntityDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    ConversionFacade.typeSafeCopy(this, generic, elementDTO);
  }

  /**
   * @see com.runwaysdk.ClientRequest#deleteChild(java.lang.String)
   */
  public void deleteChild(String relationshipId)
  {
    this.clearNotifications();
    try
    {
      rmiAdapter.deleteChild(this.getSessionId(), relationshipId);
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  /**
   * @see com.runwaysdk.ClientRequest#deleteParent(java.lang.String)
   */
  public void deleteParent(String relationshipId)
  {
    this.clearNotifications();
    try
    {
      rmiAdapter.deleteParent(this.getSessionId(), relationshipId);
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public List<? extends BusinessDTO> getChildren(String id, String relationshipType)
  {
    this.clearNotifications();
    List<BusinessDTO> generics;
    try
    {
      generics = rmiAdapter.getChildren(this.getSessionId(), id, relationshipType);
    }
    catch (MessageExceptionDTO me)
    {
      generics = (List<BusinessDTO>) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }

    List<BusinessDTO> safes = new LinkedList<BusinessDTO>();

    for (EntityDTO generic : generics)
    {
      safes.add((BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic));
    }

    return safes;
  }

  @SuppressWarnings("unchecked")
  public List<? extends BusinessDTO> getParents(String id, String relationshipType)
  {
    this.clearNotifications();
    List<BusinessDTO> generics;
    try
    {
      generics = rmiAdapter.getParents(this.getSessionId(), id, relationshipType);
    }
    catch (MessageExceptionDTO me)
    {
      generics = (List<BusinessDTO>) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }

    List<BusinessDTO> safes = new LinkedList<BusinessDTO>();

    for (EntityDTO generic : generics)
    {
      safes.add((BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic));
    }

    return safes;
  }

  @SuppressWarnings("unchecked")
  public List<? extends RelationshipDTO> getChildRelationships(String id, String relationshipType)
  {
    this.clearNotifications();
    List<RelationshipDTO> generics;
    try
    {
      generics = rmiAdapter.getChildRelationships(this.getSessionId(), id, relationshipType);
    }
    catch (MessageExceptionDTO me)
    {
      generics = (List<RelationshipDTO>) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }

    List<RelationshipDTO> safes = new LinkedList<RelationshipDTO>();

    for (EntityDTO generic : generics)
    {
      safes.add((RelationshipDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic));
    }

    return safes;
  }

  @SuppressWarnings("unchecked")
  public List<? extends RelationshipDTO> getParentRelationships(String id, String relationshipType)
  {
    this.clearNotifications();
    List<RelationshipDTO> generics;
    try
    {
      generics = rmiAdapter.getParentRelationships(this.getSessionId(), id, relationshipType);
    }
    catch (MessageExceptionDTO me)
    {
      generics = (List<RelationshipDTO>) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }

    List<RelationshipDTO> safes = new LinkedList<RelationshipDTO>();

    for (EntityDTO generic : generics)
    {
      safes.add((RelationshipDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic));
    }

    return safes;
  }

  public StructQueryDTO queryStructs(StructQueryDTO queryDTO)
  {
    this.clearNotifications();
    StructQueryDTO generic;
    queryDTO.clearResultSet();
    try
    {
      generic = rmiAdapter.queryStructs(this.getSessionId(), queryDTO);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (StructQueryDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    return (StructQueryDTO) ConversionFacade.convertGenericQueryToTypeSafe(this, generic);
  }

  public ViewQueryDTO queryViews(ViewQueryDTO queryDTO)
  {
    this.clearNotifications();
    ViewQueryDTO generic;
    queryDTO.clearResultSet();
    try
    {
      generic = rmiAdapter.queryViews(this.getSessionId(), queryDTO);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (ViewQueryDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    return (ViewQueryDTO) ConversionFacade.convertGenericQueryToTypeSafe(this, generic);
  }

  public BusinessQueryDTO queryBusinesses(BusinessQueryDTO queryDTO)
  {
    this.clearNotifications();
    BusinessQueryDTO generic;
    queryDTO.clearResultSet();
    try
    {
      generic = rmiAdapter.queryBusinesses(this.getSessionId(), queryDTO);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (BusinessQueryDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    return (BusinessQueryDTO) ConversionFacade.convertGenericQueryToTypeSafe(this, generic);
  }

  /**
   * Returns a ComponentQueryDTO containing the results of an arbitrary query
   * for a given type.
   * 
   * @param ComponentQueryDTO
   * @return ComponentQueryDTO containing the query result.
   */
  public ComponentQueryDTO groovyObjectQuery(ComponentQueryDTO componentQueryDTO)
  {
    this.clearNotifications();
    ComponentQueryDTO returnObject;
    componentQueryDTO.clearResultSet();
    try
    {
      returnObject = rmiAdapter.groovyObjectQuery(this.getSessionId(), componentQueryDTO);
    }
    catch (MessageExceptionDTO me)
    {
      returnObject = (ComponentQueryDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    return returnObject;
  }

  /**
   * Returns a ValueQueryDTO containing the results of an arbitrary value query.
   * 
   * @param valueQueryDTO
   * @return ValueQueryDTO containing the query result.
   */
  public ValueQueryDTO groovyValueQuery(ValueQueryDTO valueQueryDTO)
  {
    this.clearNotifications();
    ValueQueryDTO returnObject;
    valueQueryDTO.clearResultSet();
    try
    {
      returnObject = rmiAdapter.groovyValueQuery(this.getSessionId(), valueQueryDTO);
    }
    catch (MessageExceptionDTO me)
    {
      returnObject = (ValueQueryDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    return returnObject;
  }

  public EntityQueryDTO queryEntities(EntityQueryDTO queryDTO)
  {
    this.clearNotifications();
    EntityQueryDTO generic;
    queryDTO.clearResultSet();
    try
    {
      generic = rmiAdapter.queryEntities(this.getSessionId(), queryDTO);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (EntityQueryDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    return (EntityQueryDTO) ConversionFacade.convertGenericQueryToTypeSafe(this, generic);
  }

  public RelationshipQueryDTO queryRelationships(RelationshipQueryDTO queryDTO)
  {
    this.clearNotifications();
    RelationshipQueryDTO generic;
    queryDTO.clearResultSet();
    try
    {
      generic = rmiAdapter.queryRelationships(this.getSessionId(), queryDTO);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (RelationshipQueryDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    return (RelationshipQueryDTO) ConversionFacade.convertGenericQueryToTypeSafe(this, generic);
  }

  public void deleteChildren(String id, String relationshipType)
  {
    this.clearNotifications();
    try
    {
      rmiAdapter.deleteChildren(this.getSessionId(), id, relationshipType);
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public void deleteParents(String id, String relationshipType)
  {
    this.clearNotifications();
    try
    {
      rmiAdapter.deleteParents(this.getSessionId(), id, relationshipType);
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  /**
   * @see com.runwaysdk.constants.ClientRequestIF#invokeMethod(com.runwaysdk.transport.MethodMetaData,
   *      com.runwaysdk.transport.MutableDTO, java.lang.Object[])
   */
  public Object invokeMethod(MethodMetaData metadata, MutableDTO mutableDTO, Object[] parameters)
  {
    this.clearNotifications();
    Object[] output = null;

    MutableDTO genericDTO = null;

    // Get the String representation of the business layer Class equivalent to
    // the parameters
    String[] actualTypes = ConversionFacade.getClassNames(parameters);
    metadata.setActualTypes(actualTypes);

    // Convert the entityDTO and the parameters into their generic, serializable
    // form
    if (mutableDTO != null)
    {
      genericDTO = (MutableDTO) ConversionFacade.createGenericCopy(mutableDTO);
    }
    // Convert the entityDTO and the parameters into their generic, serializable
    // form
    Object[] generics = ConversionFacade.convertGeneric(parameters);

    try
    {
      for (int i = 0; i < parameters.length; i++)
      {
        if (parameters[i] instanceof InputStream)
        {
          parameters[i] = new SimpleRemoteInputStream((InputStream) parameters[i]);
        }
        else if (parameters[i] instanceof OutputStream)
        {
          parameters[i] = new SimpleRemoteOutputStream((OutputStream) parameters[i]);
        }
      }

      // Invoke the method
      output = (Object[]) rmiAdapter.invokeMethod(this.getSessionId(), metadata, genericDTO, generics);
    }
    catch (MessageExceptionDTO me)
    {
      output = (Object[]) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    catch (IOException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.SystemException.getExceptionClass(), e.getMessage(), e);
    }

    // Update the value of the entityDTO
    if (mutableDTO != null)
    {
      MutableDTO generic = (MutableDTO) output[DTOConversionUtilInfo.CALLED_OBJECT];

      ConversionFacade.typeSafeCopy(this, generic, mutableDTO);
    }

    // If a value was returned convert it to its type safe format
    Object returnObject = output[DTOConversionUtilInfo.RETURN_OBJECT];

    if (returnObject != null)
    {
      if (returnObject instanceof RemoteInputStream)
      {
        try
        {
          return RemoteInputStreamClient.wrap((RemoteInputStream) returnObject);
        }
        catch (IOException e)
        {
          CommonExceptionProcessor.processException(ExceptionConstants.SystemException.getExceptionClass(), e.getMessage(), e);
        }
      }
      else if (returnObject instanceof RemoteOutputStream)
      {
        try
        {
          return RemoteOutputStreamClient.wrap((RemoteOutputStream) returnObject);
        }
        catch (IOException e)
        {
          CommonExceptionProcessor.processException(ExceptionConstants.SystemException.getExceptionClass(), e.getMessage(), e);
        }
      }
      else
      {
        String returnType = (String) output[DTOConversionUtilInfo.RETURN_DTO_TYPE];

        return ConversionFacade.convertToTypeSafe(this, returnType, returnObject);
      }
    }

    return null;
  }

  public BusinessDTO getEnumeration(String enumType, String enumName)
  {
    this.clearNotifications();
    BusinessDTO generic;
    try
    {
      generic = rmiAdapter.getEnumeration(this.getSessionId(), enumType, enumName);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (BusinessDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    return (BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  @SuppressWarnings("unchecked")
  public List<BusinessDTO> getEnumerations(String enumType, String[] enumNames)
  {
    this.clearNotifications();
    List<BusinessDTO> generics;
    try
    {
      generics = rmiAdapter.getEnumerations(this.getSessionId(), enumType, enumNames);
    }
    catch (MessageExceptionDTO me)
    {
      generics = (List<BusinessDTO>) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }

    List<BusinessDTO> safes = new LinkedList<BusinessDTO>();

    for (EntityDTO generic : generics)
    {
      safes.add((BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic));
    }

    return safes;
  }

  @SuppressWarnings("unchecked")
  public List<BusinessDTO> getAllEnumerations(String enumType)
  {
    this.clearNotifications();
    List<BusinessDTO> generics;
    try
    {
      generics = rmiAdapter.getAllEnumerations(this.getSessionId(), enumType);
    }
    catch (MessageExceptionDTO me)
    {
      generics = (List<BusinessDTO>) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }

    List<BusinessDTO> safes = new LinkedList<BusinessDTO>();

    for (EntityDTO generic : generics)
    {
      safes.add((BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic));
    }

    return safes;
  }

  public BusinessDTO getVaultFileDTO(String type, String attributeName, String fileId)
  {
    this.clearNotifications();
    BusinessDTO returnObject;
    try
    {
      returnObject = rmiAdapter.getVaultFileDTO(this.getSessionId(), type, attributeName, fileId);
    }
    catch (MessageExceptionDTO me)
    {
      returnObject = (BusinessDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    return returnObject;
  }

  public void createStruct(StructDTO structDTO)
  {
    this.clearNotifications();
    StructDTO generic;
    StructDTO _structDTO = (StructDTO) ConversionFacade.createGenericCopy(structDTO);
    try
    {
      generic = rmiAdapter.createStruct(this.getSessionId(), _structDTO);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (StructDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    ConversionFacade.typeSafeCopy(this, generic, structDTO);
  }

  public StructDTO newStruct(String type)
  {
    this.clearNotifications();
    StructDTO generic;
    try
    {
      generic = rmiAdapter.newStruct(this.getSessionId(), type);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (StructDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    return (StructDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  public StructDTO newGenericStruct(String type)
  {
    this.clearNotifications();
    StructDTO generic;
    try
    {
      generic = rmiAdapter.newStruct(this.getSessionId(), type);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (StructDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    return (StructDTO) ConversionFacade.createGenericCopyWithTypeSafeAttributes(this, generic);
  }

  public MutableDTO newMutable(String type)
  {
    this.clearNotifications();
    MutableDTO generic;
    try
    {
      generic = rmiAdapter.newMutable(this.getSessionId(), type);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (MutableDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    return (MutableDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  public MutableDTO newGenericMutable(String type)
  {
    this.clearNotifications();
    MutableDTO generic;
    try
    {
      generic = rmiAdapter.newMutable(this.getSessionId(), type);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (MutableDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    return (MutableDTO) ConversionFacade.createGenericCopyWithTypeSafeAttributes(this, generic);
  }

  public SmartExceptionDTO newGenericException(String type)
  {
    this.clearNotifications();
    ExceptionDTO generic;
    try
    {
      generic = (ExceptionDTO) rmiAdapter.newMutable(this.getSessionId(), type);
    }
    catch (MessageExceptionDTO me)
    {
      generic = (ExceptionDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    return (SmartExceptionDTO) ConversionFacade.createGenericCopyWithTypeSafeAttributes(this, generic);
  }

  public ClassQueryDTO getQuery(String type)
  {
    this.clearNotifications();
    ClassQueryDTO classQueryDTO;
    try
    {
      classQueryDTO = rmiAdapter.getQuery(this.getSessionId(), type);
    }
    catch (MessageExceptionDTO me)
    {
      classQueryDTO = (ClassQueryDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    return classQueryDTO;
  }

  public void importDomainModel(String xml, String xsd)
  {
    this.clearNotifications();
    try
    {
      rmiAdapter.importDomainModel(this.getSessionId(), xml, xsd);
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  protected InputStream getFileFromServer(String fileId)
  {
    this.clearNotifications();
    RemoteInputStream remoteStream;
    try
    {
      remoteStream = rmiAdapter.getFile(this.getSessionId(), fileId);
    }
    catch (MessageExceptionDTO me)
    {
      remoteStream = (RemoteInputStream) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }

    try
    {
      return RemoteInputStreamClient.wrap(remoteStream);
    }
    catch (IOException e)
    {
      throw new VaultExceptionDTO(e.getMessage());
    }
  }

  protected InputStream getSecureFileFromServer(String attributeName, String type, String fileId)
  {
    this.clearNotifications();
    RemoteInputStream remoteStream;
    try
    {
      remoteStream = rmiAdapter.getSecureFile(this.getSessionId(), type, attributeName, fileId);
    }
    catch (MessageExceptionDTO me)
    {
      remoteStream = (RemoteInputStream) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }

    try
    {
      return RemoteInputStreamClient.wrap(remoteStream);
    }
    catch (IOException e)
    {
      throw new VaultExceptionDTO(e.getMessage());
    }
  }

  protected InputStream getSecureFileFromServer(String fileId)
  {
    this.clearNotifications();
    RemoteInputStream remoteStream;
    try
    {
      remoteStream = rmiAdapter.getSecureFile(this.getSessionId(), fileId);
    }
    catch (MessageExceptionDTO me)
    {
      remoteStream = (RemoteInputStream) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }

    try
    {
      return RemoteInputStreamClient.wrap(remoteStream);
    }
    catch (IOException e)
    {
      throw new VaultExceptionDTO(e.getMessage());
    }
  }

  public BusinessDTO newFile(String path, String filename, String extension, InputStream stream)
  {
    this.clearNotifications();
    RemoteInputStreamServer remoteStream = new SimpleRemoteInputStream(stream);
    BusinessDTO generic;
    try
    {
      generic = rmiAdapter.newFile(this.getSessionId(), path, filename, extension, remoteStream.export());
    }
    catch (MessageExceptionDTO me)
    {
      generic = (BusinessDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    catch (IOException e)
    {
      throw new VaultExceptionDTO(e.getLocalizedMessage());
    }

    return (BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  public BusinessDTO newSecureFile(String filename, String extension, InputStream stream)
  {
    BusinessDTO generic;
    RemoteInputStreamServer remoteStream = new SimpleRemoteInputStream(stream);
    try
    {
      generic = rmiAdapter.newSecureFile(this.getSessionId(), filename, extension, remoteStream.export());
    }
    catch (MessageExceptionDTO me)
    {
      generic = (BusinessDTO) me.getReturnObject();
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    catch (IOException e)
    {
      throw new VaultExceptionDTO(e.getLocalizedMessage());
    }
    return (BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  public void checkAdminScreenAccess()
  {
    this.clearNotifications();
    try
    {
      rmiAdapter.checkAdminScreenAccess(this.getSessionId());
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public void importInstanceXML(String xml)
  {
    this.clearNotifications();
    try
    {
      rmiAdapter.importInstanceXML(this.getSessionId(), xml);
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public InputStream exportExcelFile(String type, String listenerMethod, String... params)
  {
    this.clearNotifications();
    RemoteInputStream remoteStream;
    try
    {
      remoteStream = rmiAdapter.exportExcelFile(this.getSessionId(), type, listenerMethod, params);
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
      remoteStream = (RemoteInputStream) me.getReturnObject();
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (Exception e)
    {
      throw new RMIClientException(e);
    }

    try
    {
      return RemoteInputStreamClient.wrap(remoteStream);
    }
    catch (IOException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.SystemException.getExceptionClass(), e.getMessage(), e);
      return null;
    }
  }

  public InputStream importExcelFile(InputStream stream, String type, String listenerMethod, String... params)
  {
    this.clearNotifications();
    RemoteInputStream returnStream;
    RemoteInputStreamServer remoteStream = new SimpleRemoteInputStream(stream);
    try
    {
      returnStream = rmiAdapter.importExcelFile(this.getSessionId(), remoteStream.export(), type, listenerMethod, params);
    }
    catch (MessageExceptionDTO me)
    {
      this.setMessagesConvertToTypeSafe(me);
      returnStream = (RemoteInputStream) me.getReturnObject();
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, false);
    }
    catch (Exception e)
    {
      throw new RMIClientException(e);
    }

    try
    {
      return RemoteInputStreamClient.wrap(returnStream);
    }
    catch (IOException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.SystemException.getExceptionClass(), e.getMessage(), e);
      return null;
    }
  }
}
