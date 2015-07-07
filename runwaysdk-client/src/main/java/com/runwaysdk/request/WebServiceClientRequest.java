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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.w3c.dom.Document;

import com.runwaysdk.ClientException;
import com.runwaysdk.ClientRequest;
import com.runwaysdk.ClientSession;
import com.runwaysdk.MessageExceptionDTO;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.ClassQueryDTO;
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
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.FacadeMethods;
import com.runwaysdk.constants.WebServiceAdapterInfo;
import com.runwaysdk.dataaccess.io.FileWriteExceptionDTO;
import com.runwaysdk.transport.conversion.ClientConversionFacade;
import com.runwaysdk.transport.conversion.ConversionFacade;
import com.runwaysdk.util.DTOConversionUtilInfo;
import com.runwaysdk.util.FileIO;

/**
 * This WebServiceClientRequest class extends the functionality of ClientRequest
 * by converting any input into a format suitable for the WebServiceController.
 */
public class WebServiceClientRequest extends ClientRequest
{
  /**
   *
   */
  private static final long serialVersionUID = -2803180569683900530L;

  /**
   * Constructor that takes in an address to the location of the server hosting
   * the web services.
   * 
   * @param clientSession
   * @param locales
   */
  public WebServiceClientRequest(ClientSession clientSession, Locale[] locales)
  {
    super(clientSession, locales);
  }

  /**
   * Constructor that takes in an address to the location of the server hosting
   * the web services.
   * 
   * @param clientSession
   * @param sessionId
   */
  public WebServiceClientRequest(ClientSession clientSession, String sessionId)
  {
    super(clientSession, sessionId);
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
  public WebServiceClientRequest(ClientSession clientSession, String userName, String password, Locale[] locales)
  {
    super(clientSession, userName, password, locales);
  }

  private Call newCall()
  {
    try
    {
      Service service = new Service();
      Call call = (Call) service.createCall();
      call.setTargetEndpointAddress(new URL(this.getClientSession().getConnectionLabel().getAddress() + WebServiceAdapterInfo.CLASS));
      call.setTimeout(CommonProperties.getContainerWebServiceCallTimeout());
      return call;
    }
    catch (ServiceException e)
    {
      throw new WebServiceClientRequestException(e);
    }
    catch (MalformedURLException e)
    {
      throw new WebServiceClientRequestException(e);
    }
  }

  /**
   * @see com.runwaysdk.ClientRequest#getTermAllChildren(java.lang.String,
   *      java.lang.String, java.lang.Integer, java.lang.Integer)
   */
  @SuppressWarnings("unchecked")
  public List<TermAndRelDTO> getTermAllChildren(String parentId, Integer pageNum, Integer pageSize)
  {
    this.clearNotifications();

    Object[] params = { this.getSessionId(), parentId, pageNum, pageSize };
    Call call = newCall();

    try
    {
      call.invoke(FacadeMethods.GET_TERM_ALL_CHILDREN.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        List<TermAndRelDTO> termAndRel = (List<TermAndRelDTO>) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
        return termAndRel;
      }
      else
      {
        throw rte;
      }
    }

    throw new UnsupportedOperationException("not implemented");

    // if (term == null)
    // {
    // term = (BusinessDTO) ConversionFacade.getComponentDTOIFfromDocument(this,
    // document);
    // }
    //
    // List<TermAndRelDTO> retList = new ArrayList<TermAndRelDTO>();
    // for (int i = 0; i < tnr.size(); ++i) {
    // ComponentDTOIF dtoCopy =
    // ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this,
    // tnr.get(i).getTerm());
    // retList.add(new TermAndRelDTO((BusinessDTO) dtoCopy,
    // tnr.get(i).getRelationshipType(), tnr.get(i).getRelationshipId()));
    // }
    //
    // return retList;
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#addChild(java.lang.String,
   *      java.lang.String, com.runwaysdk.facade.RelationshipDTO)
   */
  public RelationshipDTO addChild(String parentId, String childId, String relationshipType)
  {
    this.clearNotifications();

    Document returnDoc = null;
    RelationshipDTO generic = null;

    Object[] params = { this.getSessionId(), parentId, childId, relationshipType };
    Call call = newCall();

    try
    {
      returnDoc = (Document) call.invoke(FacadeMethods.ADD_CHILD.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (RelationshipDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = ConversionFacade.getRelationshipDTOFromDocument(this, returnDoc);
    }

    return (RelationshipDTO) ConversionFacade.createTypeSafeCopy(generic, this);
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#addParent(java.lang.String,
   *      java.lang.String, com.runwaysdk.facade.RelationshipDTO)
   */
  public RelationshipDTO addParent(String parentId, String childId, String relationshipType)
  {
    this.clearNotifications();

    Document returnDoc = null;
    EntityDTO generic = null;

    Object[] params = { this.getSessionId(), parentId, childId, relationshipType };

    Call call = newCall();
    try
    {
      returnDoc = (Document) call.invoke(FacadeMethods.ADD_PARENT.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (RelationshipDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = ConversionFacade.getRelationshipDTOFromDocument(this, returnDoc);
    }

    return (RelationshipDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#changeLogin(java.lang.String,
   *      java.lang.String)
   */
  protected void changeLogin(String username, String password)
  {
    this.clearNotifications();

    Call call = newCall();
    Object[] params = { this.getSessionId(), username, password };
    try
    {
      call.invoke(FacadeMethods.CHANGE_LOGIN.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
    this.setIsPublicUser(username);
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#getSessionUser()
   */
  public BusinessDTO getSessionUser()
  {
    this.clearNotifications();

    Document document = null;
    BusinessDTO generic = null;

    Object[] params = { this.getSessionId() };
    Call call = newCall();
    try
    {
      document = (Document) call.invoke(FacadeMethods.GET_SESSION_USER.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (BusinessDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (BusinessDTO) ConversionFacade.getComponentDTOIFfromDocument(this, document);
    }

    return (BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  public Map<String, String> getSessionUserRoles()
  {
    throw new RuntimeException("Unimplemented method");
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#createSessionComponent(com.runwaysdk.facade.SessionDTO)
   */
  public void createSessionComponent(SessionDTO sessionDTO)
  {
    this.clearNotifications();
    Document returnDoc = null;
    SessionDTO generic = null;

    Document document = ConversionFacade.getDocumentFromComponentDTO(sessionDTO, false);
    Object[] params = { this.getSessionId(), document };
    Call call = newCall();
    try
    {
      returnDoc = (Document) call.invoke(FacadeMethods.CREATE_SESSION_COMPONENT.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (SessionDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (SessionDTO) ConversionFacade.getComponentDTOIFfromDocument(this, returnDoc);
    }

    ConversionFacade.typeSafeCopy(this, generic, sessionDTO);
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#createBusiness(com.runwaysdk.facade.BusinessDTO)
   */
  public void createBusiness(BusinessDTO businessDTO)
  {
    this.clearNotifications();
    Document returnDoc = null;
    EntityDTO generic = null;

    Document document = ConversionFacade.getDocumentFromComponentDTO(businessDTO, false);
    Object[] params = { this.getSessionId(), document };
    Call call = newCall();

    try
    {
      returnDoc = (Document) call.invoke(FacadeMethods.CREATE_BUSINESS.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (EntityDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = ConversionFacade.getBusinessDTOFromDocument(this, returnDoc);
    }

    ConversionFacade.typeSafeCopy(this, generic, businessDTO);
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#createBusiness(com.runwaysdk.facade.RelationshipDTO)
   */
  public void createRelationship(RelationshipDTO relationshipDTO)
  {
    this.clearNotifications();
    Document returnDoc = null;
    EntityDTO generic = null;
    Document document = ConversionFacade.getDocumentFromComponentDTO(relationshipDTO, false);

    Object[] params = { this.getSessionId(), document };
    Call call = newCall();

    try
    {
      returnDoc = (Document) call.invoke(FacadeMethods.CREATE_RELATIONSHIP.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (EntityDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = ConversionFacade.getRelationshipDTOFromDocument(this, returnDoc);
    }

    ConversionFacade.typeSafeCopy(this, generic, relationshipDTO);
  }

  public void createStruct(StructDTO structDTO)
  {
    this.clearNotifications();
    Document returnDoc = null;
    EntityDTO generic = null;

    Document document = ConversionFacade.getDocumentFromComponentDTO(structDTO, false);
    Object[] params = { this.getSessionId(), document };
    Call call = newCall();

    try
    {
      returnDoc = (Document) call.invoke(FacadeMethods.CREATE_STRUCT.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (EntityDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = ConversionFacade.getStructDTOFromDocument(this, returnDoc);
    }

    ConversionFacade.typeSafeCopy(this, generic, structDTO);
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#deleteChild(java.lang.String)
   */
  public void deleteChild(String relationshipId)
  {
    this.clearNotifications();
    Object[] params = { this.getSessionId(), relationshipId };
    Call call = newCall();

    try
    {
      call.invoke(FacadeMethods.DELETE_CHILD.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  public void deleteChildren(String id, String relationshipType)
  {
    this.clearNotifications();
    Object[] params = { this.getSessionId(), id, relationshipType };
    Call call = newCall();

    try
    {
      call.invoke(FacadeMethods.DELETE_CHILDREN.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#deleteBusiness(java.lang.String)
   */
  public void delete(String id)
  {
    this.clearNotifications();
    Object[] params = { this.getSessionId(), id };
    Call call = newCall();
    try
    {
      call.invoke(FacadeMethods.DELETE.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#deleteParent(java.lang.String)
   */
  public void deleteParent(String relationshipId)
  {
    this.clearNotifications();
    Object[] params = { this.getSessionId(), relationshipId };
    Call call = newCall();
    try
    {
      call.invoke(FacadeMethods.DELETE_PARENT.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  public void deleteParents(String id, String relationshipType)
  {
    this.clearNotifications();
    Object[] params = { this.getSessionId(), id, relationshipType };
    Call call = newCall();
    try
    {
      call.invoke(FacadeMethods.DELETE_PARENTS.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#get(java.lang.String)
   */
  public MutableDTO get(String id)
  {
    this.clearNotifications();
    Document document = null;
    MutableDTO generic = null;

    Object[] params = { this.getSessionId(), id };
    Call call = newCall();

    try
    {
      document = (Document) call.invoke(FacadeMethods.GET_INSTANCE.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (MutableDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (MutableDTO) ConversionFacade.getComponentDTOIFfromDocument(this, document);
    }

    return (MutableDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  public List<? extends RelationshipDTO> getChildRelationships(String id, String relationshipType)
  {
    this.clearNotifications();
    Document documentRels = null;
    RelationshipDTO[] genericArray = null;

    Object[] params = { this.getSessionId(), id, relationshipType };
    Call call = newCall();

    try
    {
      documentRels = (Document) call.invoke(FacadeMethods.GET_CHILD_RELATIONSHIPS.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        genericArray = (RelationshipDTO[]) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (genericArray == null)
    {
      genericArray = (RelationshipDTO[]) ConversionFacade.getObjectFromDocument(this, documentRels);
    }

    List<RelationshipDTO> relationshipDTOs = new LinkedList<RelationshipDTO>();
    for (RelationshipDTO generic : genericArray)
    {
      relationshipDTOs.add((RelationshipDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic));
    }
    return relationshipDTOs;
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#login(java.lang.String,
   *      java.lang.String, java.util.Locale[])
   */
  protected String login(String username, String password, Locale[] locales)
  {
    this.clearNotifications();
    String sessionId;
    Call call = newCall();
    Object[] params = { username, password, locales.toString() };

    try
    {
      sessionId = (String) call.invoke(FacadeMethods.LOGIN.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        sessionId = (String) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
    this.setSessionId(sessionId);
    this.setLoginStatus(username, true);
    return this.getSessionId();
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#login(java.lang.String,
   *      java.lang.String, java.lang.String, java.util.Locale[])
   */
  protected String login(String username, String password, String dimensionKey, Locale[] locales)
  {
    this.clearNotifications();
    String sessionId;
    Call call = newCall();
    Object[] params = { username, password, dimensionKey, locales.toString() };

    try
    {
      sessionId = (String) call.invoke(FacadeMethods.LOGIN.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        sessionId = (String) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
    this.setSessionId(sessionId);
    this.setLoginStatus(username, true);
    return this.getSessionId();
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#loginAnonymous(java.util.Locale[])
   */
  protected String loginAnonymous(Locale[] locales)
  {
    this.clearNotifications();
    String sessionId;

    Call call = newCall();
    Object[] params = { locales.toString() };

    try
    {
      sessionId = (String) call.invoke(FacadeMethods.LOGIN_ANONYMOUS.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        sessionId = (String) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
    this.setSessionId(sessionId);
    this.setLoginStatus(true, true);
    return this.getSessionId();
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#loginAnonymous(java.lang.String,
   *      java.util.Locale[])
   */
  protected String loginAnonymous(String dimensionKey, Locale[] locales)
  {
    this.clearNotifications();
    String sessionId;

    Call call = newCall();
    Object[] params = { dimensionKey, locales.toString() };

    try
    {
      sessionId = (String) call.invoke(FacadeMethods.LOGIN_ANONYMOUS.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        sessionId = (String) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
    this.setSessionId(sessionId);
    this.setLoginStatus(true, true);
    return this.getSessionId();
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#setDimension(java.lang.String,
   *      java.lang.String)
   */
  protected void setDimension(String sessionId, String dimensionKey)
  {
    this.clearNotifications();

    Call call = newCall();
    Object[] params = { sessionId, dimensionKey };

    try
    {
      call.invoke(FacadeMethods.SET_DIMENSION.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        sessionId = (String) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#logout()
   */
  protected void logout()
  {
    this.clearNotifications();
    Call call = newCall();
    Object[] params = { this.getSessionId() };

    try
    {
      call.invoke(FacadeMethods.LOGOUT.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
    this.setLoginStatus(false, false);
  }

  /**
   * @see com.runwaysdk.constant.ClientRequestIF#newBusiness(java.lang.String)
   */
  public BusinessDTO newBusiness(String type)
  {
    this.clearNotifications();

    Document document = null;
    Call call = newCall();
    Object[] params = { this.getSessionId(), type };

    BusinessDTO generic = null;
    try
    {
      document = (Document) call.invoke(FacadeMethods.NEW_BUSINESS.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (BusinessDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = ConversionFacade.getBusinessDTOFromDocument(this, document);
    }

    return (BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  @Override
  public EntityDTO newDisconnectedEntity(String type)
  {
    this.clearNotifications();

    Document document = null;
    Call call = newCall();
    Object[] params = { this.getSessionId(), type };

    EntityDTO generic = null;
    try
    {
      document = (Document) call.invoke(FacadeMethods.NEW_DISCONNECTED_ENTITY.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (EntityDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = ConversionFacade.getBusinessDTOFromDocument(this, document);
    }

    return (EntityDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  /**
   * @see com.runwaysdk.constant.ClientRequestIF#newGenericBusiness(java.lang.String)
   */
  public BusinessDTO newGenericBusiness(String type)
  {
    this.clearNotifications();
    Document document = null;
    BusinessDTO generic = null;
    Call call = newCall();
    Object[] params = { this.getSessionId(), type };

    try
    {
      document = (Document) call.invoke(FacadeMethods.NEW_BUSINESS.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (BusinessDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = ConversionFacade.getBusinessDTOFromDocument(this, document);
    }

    return (BusinessDTO) ConversionFacade.createGenericCopyWithTypeSafeAttributes(this, generic);
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#update(com.runwaysdk.facade.MutableDTO)
   */
  public void update(MutableDTO mutableDTO)
  {
    this.clearNotifications();
    Document returnDoc = null;
    MutableDTO generic = null;

    Document document = ConversionFacade.getDocumentFromComponentDTO(mutableDTO, false);

    Object[] params = { this.getSessionId(), document };
    Call call = newCall();
    try
    {
      returnDoc = (Document) call.invoke(FacadeMethods.UPDATE.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (MutableDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (MutableDTO) ConversionFacade.getComponentDTOIFfromDocument(this, returnDoc);
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
    Object[] params = { this.getSessionId(), userId, roles };
    Call call = newCall();

    try
    {
      call.invoke(FacadeMethods.ASSIGN_MEMBER.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  /**
   * @see com.runwaysdk.ClientRequest#removeMember(java.lang.String,
   *      java.lang.String, java.lang.String...)
   */
  public void removeMember(String userId, String... roles)
  {
    this.clearNotifications();
    Object[] params = { this.getSessionId(), userId, roles };
    Call call = newCall();
    try
    {
      call.invoke(FacadeMethods.REMOVE_MEMBER.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#grantTypePermission(java.lang.String,
   *      String..., String...)
   */
  public void grantTypePermission(String actorId, String mdTypeId, String... operationNames)
  {
    this.clearNotifications();

    Object[] params = { this.getSessionId(), actorId, mdTypeId, operationNames };
    Call call = newCall();

    try
    {
      call.invoke(FacadeMethods.GRANT_TYPE_PERMISSION.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#grantMethodPermission(java.lang.String,
   *      String..., String...)
   */
  public void grantMethodPermission(String actorId, String mdMethodId, String... operationNames)
  {
    this.clearNotifications();

    Object[] params = { this.getSessionId(), actorId, mdMethodId, operationNames };
    Call call = newCall();

    try
    {
      call.invoke(FacadeMethods.GRANT_METHOD_PERMISSION.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#grantStatePermission(java.lang.String,
   *      String..., String...)
   */
  public void grantStatePermission(String actorId, String stateId, String... operationNames)
  {
    this.clearNotifications();
    Object[] params = { this.getSessionId(), actorId, stateId, operationNames };
    Call call = newCall();

    try
    {
      call.invoke(FacadeMethods.GRANT_STATE_PERMISSION.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#grantAttributePermission(
   *      java.lang.String, String..., String......)
   */
  public void grantAttributePermission(String actorId, String mdAttributeId, String... operationNames)
  {
    this.clearNotifications();
    Object[] params = { this.getSessionId(), actorId, mdAttributeId, operationNames };
    Call call = newCall();

    try
    {
      call.invoke(FacadeMethods.GRANT_ATTRIBUTE_PERMISSION.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#grantAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public void grantAttributeStatePermission(String actorId, String mdAttributeId, String stateId, String... operationNames)
  {
    this.clearNotifications();
    Object[] params = { this.getSessionId(), actorId, mdAttributeId, stateId, operationNames };
    Call call = newCall();

    try
    {
      call.invoke(FacadeMethods.GRANT_ATTRIBUTE_STATE_PERMISSION.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#promoteObject(BusinessDTO,
   *      java.lang.String)
   */
  public void promoteObject(BusinessDTO businessDTO, String transitionName)
  {
    this.clearNotifications();
    Document returnDoc = null;
    EntityDTO generic = null;
    Document document = ConversionFacade.getDocumentFromComponentDTO(businessDTO, false);

    Object[] params = { this.getSessionId(), document, transitionName };
    Call call = newCall();

    try
    {
      returnDoc = (Document) call.invoke(FacadeMethods.PROMOTE_OBJECT.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (BusinessDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = ConversionFacade.getBusinessDTOFromDocument(this, returnDoc);
    }

    ConversionFacade.typeSafeCopy(this, generic, businessDTO);
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#revokeTypePermission(java.lang.String,
   *      java.lang.String, String...)
   */
  public void revokeTypePermission(String actorId, String mdTypeId, String... operationNames)
  {
    this.clearNotifications();
    Object[] params = { this.getSessionId(), actorId, mdTypeId, operationNames };
    Call call = newCall();

    try
    {
      call.invoke(FacadeMethods.REVOKE_TYPE_PERMISSION.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#revokeMethodPermission(java.lang.String,
   *      java.lang.String, String...)
   */
  public void revokeMethodPermission(String actorId, String mdMethodId, String... operationNames)
  {
    this.clearNotifications();
    Object[] params = { this.getSessionId(), actorId, mdMethodId, operationNames };
    Call call = newCall();

    try
    {
      call.invoke(FacadeMethods.REVOKE_METHOD_PERMISSION.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#revokeStatePermission(java.lang.String,
   *      java.lang.String, String...)
   */
  public void revokeStatePermission(String actorId, String stateId, String... operationNames)
  {
    this.clearNotifications();

    Object[] params = { this.getSessionId(), actorId, stateId, operationNames };
    Call call = newCall();

    try
    {
      call.invoke(FacadeMethods.REVOKE_STATE_PERMISSION.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#revokeAttributePermission(java.lang.String,
   *      java.lang.String, String...)
   */
  public void revokeAttributePermission(String actorId, String mdAttributeId, String... operationNames)
  {
    this.clearNotifications();

    Object[] params = { this.getSessionId(), actorId, mdAttributeId, operationNames };
    Call call = newCall();

    try
    {
      call.invoke(FacadeMethods.REVOKE_ATTRIBUTE_PERMISSION.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#revokeAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public void revokeAttributeStatePermission(String actorId, String mdAttributeId, String stateId, String... operationNames)
  {
    this.clearNotifications();

    Object[] params = { this.getSessionId(), actorId, mdAttributeId, stateId, operationNames };
    Call call = newCall();

    try
    {
      call.invoke(FacadeMethods.REVOKE_ATTRIBUTE_STATE_PERMISSION.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#lock(com.runwaysdk.business.ElementDTO)
   */
  public void lock(ElementDTO elementDTO)
  {
    this.clearNotifications();
    Document document = null;
    ElementDTO generic = null;

    Object[] params = { this.getSessionId(), elementDTO.getId() };
    Call call = newCall();
    try
    {
      document = (Document) call.invoke(FacadeMethods.LOCK.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (ElementDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (ElementDTO) ConversionFacade.getComponentDTOIFfromDocument(this, document);
    }

    ConversionFacade.typeSafeCopy(this, generic, elementDTO);
  }

  /**
   * @see com.runwaysdk.facade.client.ClientRequest#unlock(com.runwaysdk.business.ElementDTO)
   */
  public void unlock(ElementDTO elementDTO)
  {
    this.clearNotifications();
    Document document = null;
    ElementDTO generic = null;

    Object[] params = { this.getSessionId(), elementDTO.getId() };
    Call call = newCall();

    try
    {
      document = (Document) call.invoke(FacadeMethods.UNLOCK.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (ElementDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (ElementDTO) ConversionFacade.getComponentDTOIFfromDocument(this, document);
    }

    ConversionFacade.typeSafeCopy(this, generic, elementDTO);
  }

  public List<? extends BusinessDTO> getChildren(String id, String relationshipType)
  {
    this.clearNotifications();
    Document documentRels = null;
    BusinessDTO[] genericArray = null;

    Object[] params = { this.getSessionId(), id, relationshipType };
    Call call = newCall();

    try
    {
      documentRels = (Document) call.invoke(FacadeMethods.GET_CHILDREN.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        genericArray = (BusinessDTO[]) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (genericArray == null)
    {
      genericArray = (BusinessDTO[]) ConversionFacade.getObjectFromDocument(this, documentRels);
    }

    List<BusinessDTO> businessDTOs = new LinkedList<BusinessDTO>();
    for (BusinessDTO generic : genericArray)
    {
      businessDTOs.add((BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic));
    }
    return businessDTOs;
  }

  public List<? extends BusinessDTO> getParents(String id, String relationshipType)
  {
    this.clearNotifications();
    Document documentRels = null;
    BusinessDTO[] genericArray = null;

    Object[] params = { this.getSessionId(), id, relationshipType };
    Call call = newCall();

    try
    {
      documentRels = (Document) call.invoke(FacadeMethods.GET_PARENTS.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        genericArray = (BusinessDTO[]) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (genericArray == null)
    {
      genericArray = (BusinessDTO[]) ConversionFacade.getObjectFromDocument(this, documentRels);
    }

    List<BusinessDTO> businessDTOs = new LinkedList<BusinessDTO>();
    for (BusinessDTO generic : genericArray)
    {
      businessDTOs.add((BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic));
    }
    return businessDTOs;
  }

  public List<? extends RelationshipDTO> getParentRelationships(String id, String relationshipType)
  {
    this.clearNotifications();
    Document documentRels = null;
    RelationshipDTO[] genericArray = null;

    Object[] params = { this.getSessionId(), id, relationshipType };
    Call call = newCall();

    try
    {
      documentRels = (Document) call.invoke(FacadeMethods.GET_PARENT_RELATIONSHIPS.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        genericArray = (RelationshipDTO[]) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (genericArray == null)
    {
      genericArray = (RelationshipDTO[]) ConversionFacade.getObjectFromDocument(this, documentRels);
    }

    List<RelationshipDTO> relationshipDTOs = new LinkedList<RelationshipDTO>();
    for (RelationshipDTO generic : genericArray)
    {
      relationshipDTOs.add((RelationshipDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic));
    }
    return relationshipDTOs;
  }

  /**
   *
   */
  public ViewQueryDTO queryViews(ViewQueryDTO queryDTO)
  {
    this.clearNotifications();
    queryDTO.clearResultSet();
    Document document = ConversionFacade.getDocumentFromQueryDTO(queryDTO);

    ViewQueryDTO viewQueryDTO = null;

    Object[] params = { this.getSessionId(), document };
    Call call = newCall();

    try
    {
      document = (Document) call.invoke(FacadeMethods.QUERY_VIEWS.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        viewQueryDTO = (ViewQueryDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (viewQueryDTO == null)
    {
      viewQueryDTO = (ViewQueryDTO) ConversionFacade.getQueryDTOFromDocument(this, document, true);
    }

    if (viewQueryDTO == null)
    {
      viewQueryDTO = (ViewQueryDTO) ConversionFacade.getQueryDTOFromDocument(this, document, true);
      return (ViewQueryDTO) ConversionFacade.convertGenericQueryToTypeSafe(this, viewQueryDTO);
    }
    else
    { // query object from the message query is already type safe.
      return viewQueryDTO;
    }
  }

  /**
   *
   */
  public BusinessQueryDTO queryBusinesses(BusinessQueryDTO queryDTO)
  {
    this.clearNotifications();
    queryDTO.clearResultSet();
    Document document = ConversionFacade.getDocumentFromQueryDTO(queryDTO);

    BusinessQueryDTO businessQueryDTO = null;

    Object[] params = { this.getSessionId(), document };
    Call call = newCall();

    try
    {
      document = (Document) call.invoke(FacadeMethods.QUERY_BUSINESSES.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        businessQueryDTO = (BusinessQueryDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (businessQueryDTO == null)
    {
      businessQueryDTO = (BusinessQueryDTO) ConversionFacade.getQueryDTOFromDocument(this, document, true);
      return (BusinessQueryDTO) ConversionFacade.convertGenericQueryToTypeSafe(this, businessQueryDTO);
    }
    else
    { // query object from the message query is already type safe.
      return businessQueryDTO;
    }
  }

  /**
   *
   */
  public StructQueryDTO queryStructs(StructQueryDTO queryDTO)
  {
    this.clearNotifications();
    queryDTO.clearResultSet();
    Document document = ConversionFacade.getDocumentFromQueryDTO(queryDTO);

    StructQueryDTO structQueryDTO = null;

    Object[] params = { this.getSessionId(), document };
    Call call = newCall();
    try
    {
      document = (Document) call.invoke(FacadeMethods.QUERY_STRUCTS.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        structQueryDTO = (StructQueryDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (structQueryDTO == null)
    {
      structQueryDTO = (StructQueryDTO) ConversionFacade.getQueryDTOFromDocument(this, document, true);
      return (StructQueryDTO) ConversionFacade.convertGenericQueryToTypeSafe(this, structQueryDTO);
    }
    else
    { // query object from the message query is already type safe.
      return structQueryDTO;
    }
  }

  /**
   *
   */
  public ComponentQueryDTO groovyObjectQuery(ComponentQueryDTO componentQueryDTO)
  {
    this.clearNotifications();
    componentQueryDTO.clearResultSet();
    Document document = ConversionFacade.getDocumentFromQueryDTO(componentQueryDTO);

    ComponentQueryDTO generic = null;

    Object[] params = { this.getSessionId(), document };
    Call call = newCall();

    try
    {
      document = (Document) call.invoke(FacadeMethods.GROOVY_OBJECT_QUERY.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (ComponentQueryDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = ConversionFacade.getQueryDTOFromDocument(this, document, false);
    }

    return generic;
  }

  /**
   *
   */
  public ValueQueryDTO groovyValueQuery(ValueQueryDTO valueQueryDTO)
  {
    this.clearNotifications();
    valueQueryDTO.clearResultSet();
    Document document = ConversionFacade.getDocumentFromQueryDTO(valueQueryDTO);

    ValueQueryDTO generic = null;

    Object[] params = { this.getSessionId(), document };
    Call call = newCall();

    try
    {
      document = (Document) call.invoke(FacadeMethods.GROOVY_VALUE_QUERY.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (ValueQueryDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (ValueQueryDTO) ConversionFacade.getQueryDTOFromDocument(this, document, false);
    }

    return generic;
  }

  /**
   *
   */
  public EntityQueryDTO queryEntities(EntityQueryDTO queryDTO)
  {
    this.clearNotifications();
    queryDTO.clearResultSet();
    Document document = ConversionFacade.getDocumentFromQueryDTO(queryDTO);

    EntityQueryDTO entityQueryDTO = null;

    Object[] params = { this.getSessionId(), document };
    Call call = newCall();

    try
    {
      document = (Document) call.invoke(FacadeMethods.QUERY_ENTITIES.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        entityQueryDTO = (EntityQueryDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (entityQueryDTO == null)
    {
      entityQueryDTO = (EntityQueryDTO) ConversionFacade.getQueryDTOFromDocument(this, document, true);
      return (RelationshipQueryDTO) ConversionFacade.convertGenericQueryToTypeSafe(this, entityQueryDTO);
    }
    else
    { // query object from the message query is already type safe.
      return entityQueryDTO;
    }
  }

  /**
   *
   */
  public RelationshipQueryDTO queryRelationships(RelationshipQueryDTO queryDTO)
  {
    this.clearNotifications();
    queryDTO.clearResultSet();
    Document document = ConversionFacade.getDocumentFromQueryDTO(queryDTO);

    RelationshipQueryDTO relationshipQueryDTO = null;

    Object[] params = { this.getSessionId(), document };
    Call call = newCall();

    try
    {
      document = (Document) call.invoke(FacadeMethods.QUERY_RELATIONSHIPS.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        relationshipQueryDTO = (RelationshipQueryDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (relationshipQueryDTO == null)
    {
      relationshipQueryDTO = (RelationshipQueryDTO) ConversionFacade.getQueryDTOFromDocument(this, document, true);
      return (RelationshipQueryDTO) ConversionFacade.convertGenericQueryToTypeSafe(this, relationshipQueryDTO);
    }
    else
    { // query object from the message query is already type safe.
      return relationshipQueryDTO;
    }
  }

  public Object invokeMethod(MethodMetaData metadata, MutableDTO mutableDTO, Object[] parameters)
  {
    this.clearNotifications();
    Object[] output = null;
    Document genericDTO = null;

    // Get the String representation of the business layer Class equivalent to
    // the parameters
    String[] actualTypes = ConversionFacade.getClassNames(parameters);
    metadata.setActualTypes(actualTypes);

    Document metaDoc = ConversionFacade.getDocumentFromMethodMetaData(metadata);

    // Convert the entityDTO and the parameters into their generic, serializable
    // form
    if (mutableDTO != null)
    {
      MutableDTO genericCopy = (MutableDTO) ConversionFacade.createGenericCopy(mutableDTO);
      genericDTO = ConversionFacade.getDocumentFromComponentDTO(genericCopy, false);
    }

    Object[] convertGeneric = ConversionFacade.convertGeneric(parameters);
    Document[] generics = ConversionFacade.getDocumentArrayFromObjects(convertGeneric, false);

    Object[] params = { this.getSessionId(), metaDoc, genericDTO, generics };
    Call call = newCall();

    Document out = null;

    try
    {
      out = (Document) call.invoke(FacadeMethods.INVOKE_METHOD.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        output = (Object[]) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (output == null)
    {
      output = (Object[]) ConversionFacade.getObjectFromDocument(this, out);
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
      String returnType = (String) output[DTOConversionUtilInfo.RETURN_DTO_TYPE];

      return ConversionFacade.convertToTypeSafe(this, returnType, returnObject);
    }

    return null;
  }

  public BusinessDTO getEnumeration(String enumType, String enumName)
  {
    this.clearNotifications();
    Document document = null;
    BusinessDTO generic = null;

    Object[] params = { this.getSessionId(), enumType, enumName };
    Call call = newCall();

    try
    {
      document = (Document) call.invoke(FacadeMethods.GET_ENUMERATION.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (BusinessDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (BusinessDTO) ConversionFacade.getComponentDTOIFfromDocument(this, document);
    }

    return (BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  public List<BusinessDTO> getEnumerations(String enumType, String[] enumNames)
  {
    this.clearNotifications();
    Document document = null;
    BusinessDTO[] genericArray = null;

    Object[] params = { this.getSessionId(), enumType, enumNames };
    Call call = newCall();

    try
    {
      document = (Document) call.invoke(FacadeMethods.GET_ENUMERATIONS.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        genericArray = (BusinessDTO[]) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (genericArray == null)
    {
      genericArray = (BusinessDTO[]) ConversionFacade.getObjectFromDocument(this, document);
    }

    List<BusinessDTO> businessDTOs = new LinkedList<BusinessDTO>();
    for (BusinessDTO generic : genericArray)
    {
      businessDTOs.add((BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic));
    }
    return businessDTOs;
  }

  public List<BusinessDTO> getAllEnumerations(String enumType)
  {
    this.clearNotifications();
    Document document = null;
    BusinessDTO[] genericArray = null;

    Object[] params = { this.getSessionId(), enumType };
    Call call = newCall();

    try
    {
      document = (Document) call.invoke(FacadeMethods.GET_ALL_ENUMERATIONS.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        genericArray = (BusinessDTO[]) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (genericArray == null)
    {
      genericArray = (BusinessDTO[]) ConversionFacade.getObjectFromDocument(this, document);
    }

    List<BusinessDTO> businessDTOs = new LinkedList<BusinessDTO>();
    for (BusinessDTO generic : genericArray)
    {
      businessDTOs.add((BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic));
    }
    return businessDTOs;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.constants.ClientRequestIF#getFileStream(java.lang.String)
   */
  protected InputStream getFileFromServer(String webFileId)
  {
    this.clearNotifications();
    Document document = null;
    Byte[] bytes = null;

    Object[] params = { this.getSessionId(), webFileId };
    Call call = newCall();

    try
    {
      document = (Document) call.invoke(FacadeMethods.GET_FILE.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        bytes = (Byte[]) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (bytes == null)
    {
      bytes = (Byte[]) ConversionFacade.getObjectFromDocument(this, document);
    }

    return new ByteArrayInputStream(FileIO.convertFromBytes(bytes));
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.constants.ClientRequestIF#getSecureFileStream(java.lang.String
   * )
   */
  protected InputStream getSecureFileFromServer(String fileId)
  {
    this.clearNotifications();
    Document document = null;
    Byte[] bytes = null;

    Object[] params = { this.getSessionId(), fileId };
    Call call = newCall();

    try
    {
      document = (Document) call.invoke(FacadeMethods.GET_SECURE_FILE.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        bytes = (Byte[]) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (bytes == null)
    {
      bytes = (Byte[]) ConversionFacade.getObjectFromDocument(this, document);
    }

    return new ByteArrayInputStream(FileIO.convertFromBytes(bytes));
  }

  protected InputStream getSecureFileFromServer(String attributeName, String type, String fileId)
  {
    this.clearNotifications();
    Document returnDocument = null;
    Byte[] bytes = null;

    Object[] params = { this.getSessionId(), attributeName, type, fileId };
    Call call = newCall();

    try
    {
      returnDocument = (Document) call.invoke(FacadeMethods.GET_SECURE_FILE.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        bytes = (Byte[]) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (bytes == null)
    {
      bytes = (Byte[]) ConversionFacade.getObjectFromDocument(this, returnDocument);
    }
    return new ByteArrayInputStream(FileIO.convertFromBytes(bytes));
  }

  public BusinessDTO getVaultFileDTO(String type, String attributeName, String fileId)
  {
    this.clearNotifications();
    Document returnDocument = null;
    BusinessDTO generic = null;

    Object[] params = { this.getSessionId(), type, attributeName, fileId };
    Call call = newCall();

    try
    {
      returnDocument = (Document) call.invoke(FacadeMethods.GET_VAULT_FILE_DTO.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (BusinessDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (BusinessDTO) ConversionFacade.getObjectFromDocument(this, returnDocument);
    }

    return generic;
  }

  public BusinessDTO newSecureFile(String filename, String extension, InputStream stream)
  {
    this.clearNotifications();
    Byte[] bytes;
    BusinessDTO generic = null;

    try
    {
      bytes = FileIO.getBytesFromStream(stream);
    }
    catch (IOException e)
    {
      // Change exception type - unable to read bytes from the stream
      throw new RuntimeException(e);
    }

    Document document = ConversionFacade.getDocumentFromObject(bytes, false);
    Document output = null;

    Object[] params = { this.getSessionId(), filename, extension, document };
    Call call = newCall();

    try
    {
      output = (Document) call.invoke(FacadeMethods.NEW_SECURE_FILE.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (BusinessDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (BusinessDTO) ConversionFacade.getObjectFromDocument(null, output);
    }

    return (BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.constants.ClientRequestIF#newFile(java.lang.String,
   * java.lang.String, java.lang.String, java.lang.String, java.lang.Byte[])
   */
  // FIXME: this is broken.
  public BusinessDTO newFile(String path, String filename, String extension, InputStream stream)
  {
    this.clearNotifications();
    BusinessDTO generic = null;
    Byte[] bytes;
    try
    {
      bytes = FileIO.getBytesFromStream(stream);
    }
    catch (IOException e)
    {
      throw new FileWriteExceptionDTO(e.getLocalizedMessage());
    }

    Document document = ConversionFacade.getDocumentFromObject(bytes, false);
    Document output = null;

    Object[] params = { this.getSessionId(), path, filename, extension, document };
    Call call = newCall();

    try
    {
      output = (Document) call.invoke(FacadeMethods.NEW_FILE.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (BusinessDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (BusinessDTO) ConversionFacade.getObjectFromDocument(this, output);
    }

    return (BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  public StructDTO newStruct(String type)
  {
    Document document = null;
    StructDTO generic = null;
    try
    {
      Call call = newCall();
      Object[] params = { this.getSessionId(), type };
      document = (Document) call.invoke(FacadeMethods.NEW_STRUCT.getName(), params);
      generic = ConversionFacade.getStructDTOFromDocument(this, document);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildThrowable(e, this, true);
    }
    return (StructDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  public StructDTO newGenericStruct(String type)
  {
    this.clearNotifications();
    Document document = null;
    StructDTO generic = null;

    Call call = newCall();
    Object[] params = { this.getSessionId(), type };

    try
    {
      document = (Document) call.invoke(FacadeMethods.NEW_STRUCT.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (StructDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = ConversionFacade.getStructDTOFromDocument(this, document);
    }

    return (StructDTO) ConversionFacade.createGenericCopyWithTypeSafeAttributes(this, generic);
  }

  public MutableDTO newMutable(String type)
  {
    this.clearNotifications();
    Document document = null;
    MutableDTO generic = null;

    Call call = newCall();
    Object[] params = { this.getSessionId(), type };

    try
    {
      document = (Document) call.invoke(FacadeMethods.NEW_MUTABLE.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (MutableDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (MutableDTO) ConversionFacade.getComponentDTOIFfromDocument(this, document);
    }

    return (MutableDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  public MutableDTO newGenericMutable(String type)
  {
    this.clearNotifications();
    Document document = null;
    MutableDTO generic = null;

    Call call = newCall();
    Object[] params = { this.getSessionId(), type };

    try
    {
      document = (Document) call.invoke(FacadeMethods.NEW_MUTABLE.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (MutableDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (MutableDTO) ConversionFacade.getComponentDTOIFfromDocument(this, document);
    }

    return (MutableDTO) ConversionFacade.createGenericCopyWithTypeSafeAttributes(this, generic);
  }

  public SmartExceptionDTO newGenericException(String type)
  {
    this.clearNotifications();
    Document document = null;
    ExceptionDTO generic = null;

    Call call = newCall();
    Object[] params = { this.getSessionId(), type };

    try
    {
      document = (Document) call.invoke(FacadeMethods.NEW_MUTABLE.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (ExceptionDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (ExceptionDTO) ConversionFacade.getComponentDTOIFfromDocument(this, document);
    }

    return (SmartExceptionDTO) ConversionFacade.createGenericCopyWithTypeSafeAttributes(this, generic);
  }

  public ClassQueryDTO getQuery(String type)
  {
    this.clearNotifications();
    Document document = null;
    ClassQueryDTO classQueryDTO = null;

    Call call = newCall();
    Object[] params = { this.getSessionId(), type };

    try
    {
      document = (Document) call.invoke(FacadeMethods.GET_QUERY.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        classQueryDTO = (ClassQueryDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (classQueryDTO == null)
    {
      classQueryDTO = (ClassQueryDTO) ConversionFacade.getQueryDTOFromDocument(this, document, true);
    }

    return classQueryDTO;
  }

  public void importDomainModel(String xml, String xsd)
  {
    this.clearNotifications();

    Call call = newCall();
    Object[] params = { this.getSessionId(), xml, xsd };

    try
    {
      call.invoke(FacadeMethods.IMPORT_DOMAIN_MODEL.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  public void checkAdminScreenAccess()
  {
    this.clearNotifications();

    Call call = newCall();
    Object[] params = { this.getSessionId() };

    try
    {
      call.invoke(FacadeMethods.CHECK_ADMIN_SCREEN_ACCESS.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  public void importInstanceXML(String xml)
  {
    this.clearNotifications();

    Call call = newCall();
    Object[] params = { this.getSessionId(), xml };

    try
    {
      call.invoke(FacadeMethods.IMPORT_INSTANCE_XML.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  public EntityQueryDTO getAllInstances(String type, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    this.clearNotifications();

    Document document = null;
    EntityQueryDTO generic = null;

    Object[] params = { this.getSessionId(), type, sortAttribute, ascending, pageSize, pageNumber };
    Call call = newCall();

    try
    {
      document = (Document) call.invoke(FacadeMethods.GET_ALL_INSTANCES.getName(), params);
    }
    catch (RemoteException e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (EntityQueryDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      return (EntityQueryDTO) ConversionFacade.getQueryDTOFromDocument(this, document, true);
    }

    return (EntityQueryDTO) ConversionFacade.convertGenericQueryToTypeSafe(this, generic);
  }

  public InputStream exportExcelFile(String type, String listenerMethod, String... params)
  {
    throw new ClientException("You cannot export Excel files over web services.");
  }

  public InputStream importExcelFile(InputStream stream, String type, String listenerMethod, String... params)
  {
    throw new ClientException("You cannot import Excel files over web services.");
  }
}
