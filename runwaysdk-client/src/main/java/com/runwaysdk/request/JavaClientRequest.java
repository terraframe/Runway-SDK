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

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.ClientRequest;
import com.runwaysdk.ClientSession;
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
import com.runwaysdk.constants.AdapterInfo;
import com.runwaysdk.constants.FacadeMethods;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.transport.conversion.ClientConversionFacade;
import com.runwaysdk.transport.conversion.ConversionFacade;
import com.runwaysdk.util.DTOConversionUtilInfo;

/**
 * This JavaClientRequest class extends the functionality of ClientRequest by
 * converting any input into a format suitable for the JavaController.
 */
public class JavaClientRequest extends ClientRequest
{
  /**
   *
   */
  private static final long serialVersionUID = -5500068650524345774L;

  public JavaClientRequest(ClientSession clientSession, Locale[] locales)
  {
    super(clientSession, locales);
  }

  public JavaClientRequest(ClientSession clientSession, String sessionId)
  {
    super(clientSession, sessionId);
  }

  public JavaClientRequest(ClientSession clientSession, String userName, String password, Locale[] locales)
  {
    super(clientSession, userName, password, locales);
  }

  /**
   * @see com.runwaysdk.ClientRequest#getTermAllChildren(java.lang.String,
   *      java.lang.String, java.lang.Integer, java.lang.Integer)
   */
  @SuppressWarnings("unchecked")
  public List<TermAndRelDTO> getTermAllChildren(String parentId, Integer pageNum, Integer pageSize)
  {
    this.clearNotifications();
    List<TermAndRelDTO> tnr;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      tnr = (List<TermAndRelDTO>) javaAdapterClass.getMethod(FacadeMethods.GET_TERM_ALL_CHILDREN.getName(), String.class, String.class, Integer.class, Integer.class).invoke(null, this.getSessionId(), parentId, pageNum, pageSize);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        tnr = (List<TermAndRelDTO>) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    List<TermAndRelDTO> retList = new ArrayList<TermAndRelDTO>();
    for (int i = 0; i < tnr.size(); ++i)
    {
      ComponentDTOIF dtoCopy = ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, tnr.get(i).getTerm());
      retList.add(new TermAndRelDTO((TermDTO) dtoCopy, tnr.get(i).getRelationshipType(), tnr.get(i).getRelationshipId()));
    }

    return retList;
  }

  /**
   * @throws NoSuchMethodException
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   * @see com.runwaysdk.ClientRequest#addChild(java.lang.String,
   *      java.lang.String, com.runwaysdk.business.RelationshipDTO)
   */
  public RelationshipDTO addChild(String parentId, String childId, String relationshipType)
  {
    this.clearNotifications();
    RelationshipDTO generic;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (RelationshipDTO) javaAdapterClass.getMethod("addChild", String.class, String.class, String.class, String.class).invoke(null, this.getSessionId(), parentId, childId, relationshipType);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    return ConversionFacade.createTypeSafeCopy(generic, this);
  }

  /**
   * @see com.runwaysdk.ClientRequest#addParent(java.lang.String,
   *      java.lang.String, com.runwaysdk.business.RelationshipDTO)
   */
  public RelationshipDTO addParent(String parentId, String childId, String relationshipType)
  {
    this.clearNotifications();
    RelationshipDTO generic;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (RelationshipDTO) javaAdapterClass.getMethod("addParent", String.class, String.class, String.class, String.class).invoke(null, this.getSessionId(), parentId, childId, relationshipType);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    return ConversionFacade.createTypeSafeCopy(generic, this);
  }

  /**
   * @see com.runwaysdk.ClientRequest#delete(java.lang.String)
   */
  public void delete(String id)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      javaAdapterClass.getMethod("delete", String.class, String.class).invoke(null, this.getSessionId(), id);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
   * @see com.runwaysdk.ClientRequest#get(java.lang.String)
   */
  public MutableDTO get(String id)
  {
    this.clearNotifications();
    MutableDTO generic;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (MutableDTO) javaAdapterClass.getMethod(CommonGenerationUtil.GET, String.class, String.class).invoke(null, this.getSessionId(), id);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    return (MutableDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  public EntityQueryDTO getAllInstances(String type, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    this.clearNotifications();
    EntityQueryDTO generic;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (EntityQueryDTO) javaAdapterClass.getMethod("getAllInstances", String.class, String.class, String.class, Boolean.class, Integer.class, Integer.class).invoke(null, this.getSessionId(), type, sortAttribute, ascending, pageSize, pageNumber);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    return (EntityQueryDTO) ConversionFacade.convertGenericQueryToTypeSafe(this, generic);
  }

  /**
   * @see com.runwaysdk.ClientRequest#createSessionComponent(com.runwaysdk.business.SessionDTO)
   */
  public void createSessionComponent(SessionDTO sessionDTO)
  {
    this.clearNotifications();
    SessionDTO generic;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      SessionDTO _sessionDTO = (SessionDTO) ConversionFacade.createGenericCopy(sessionDTO);

      generic = (SessionDTO) javaAdapterClass.getMethod("createSessionComponent", String.class, SessionDTO.class).invoke(null, this.getSessionId(), _sessionDTO);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    ConversionFacade.typeSafeCopy(this, generic, sessionDTO);
  }

  /**
   * @see com.runwaysdk.ClientRequest#createBusiness(com.runwaysdk.business.BusinessDTO)
   */
  public void createBusiness(BusinessDTO businessDTO)
  {
    this.clearNotifications();
    BusinessDTO generic;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      BusinessDTO _businessDTO = (BusinessDTO) ConversionFacade.createGenericCopy(businessDTO);

      generic = (BusinessDTO) javaAdapterClass.getMethod("createBusiness", String.class, BusinessDTO.class).invoke(null, this.getSessionId(), _businessDTO);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    ConversionFacade.typeSafeCopy(this, generic, businessDTO);
  }

  /**
   * @see com.runwaysdk.ClientRequest#createRelationship(com.runwaysdk.business.RelationshipDTO)
   */
  public void createRelationship(RelationshipDTO relationshipDTO)
  {
    this.clearNotifications();
    RelationshipDTO generic;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      RelationshipDTO _relationshipDTO = (RelationshipDTO) ConversionFacade.createGenericCopy(relationshipDTO);

      generic = (RelationshipDTO) javaAdapterClass.getMethod("createRelationship", String.class, RelationshipDTO.class).invoke(null, this.getSessionId(), _relationshipDTO);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    ConversionFacade.typeSafeCopy(this, generic, relationshipDTO);
  }

  /**
   * @see com.runwaysdk.ClientRequest#login(java.lang.String, java.lang.String,
   *      java.util.Locale[])
   */
  protected String login(String username, String password, Locale[] locales)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    String sessionId;

    try
    {
      sessionId = (String) javaAdapterClass.getMethod("login", String.class, String.class, Locale[].class).invoke(null, username, password, locales);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
   * @see com.runwaysdk.ClientRequest#login(java.lang.String, java.lang.String,
   *      java.lang.String, java.util.Locale[])
   */
  protected String login(String username, String password, String dimensionKey, Locale[] locales)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    String sessionId;

    try
    {
      sessionId = (String) javaAdapterClass.getMethod("login", String.class, String.class, String.class, Locale[].class).invoke(null, username, password, dimensionKey, locales);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
   * @see com.runwaysdk.ClientRequest#setDimension(java.lang.String,
   *      java.lang.String)
   */
  protected void setDimension(String sessionId, String dimensionKey)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      javaAdapterClass.getMethod("setDimension", String.class, String.class).invoke(null, sessionId, dimensionKey);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
   * @see com.runwaysdk.ClientRequest#changeLogin(java.lang.String,
   *      java.lang.String)
   */
  protected void changeLogin(String username, String password)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      javaAdapterClass.getMethod("changeLogin", String.class, String.class, String.class).invoke(null, this.getSessionId(), username, password);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
   * @see com.runwaysdk.ClientRequest#getSessionUser()
   */
  public BusinessDTO getSessionUser()
  {
    this.clearNotifications();
    BusinessDTO generic;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (BusinessDTO) javaAdapterClass.getMethod("getSessionUser", String.class).invoke(null, this.getSessionId());
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      roleMap = (Map<String, String>) javaAdapterClass.getMethod("getSessionUserRoles", String.class).invoke(null, this.getSessionId());
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        roleMap = (Map<String, String>) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    return roleMap;
  }

  /**
   * @see com.runwaysdk.ClientRequest#loginAnonymous(Locale[])
   */
  protected String loginAnonymous(Locale[] locales)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    String sessionId;

    try
    {
      sessionId = (String) javaAdapterClass.getMethod("loginAnonymous", Locale[].class).invoke(null, (Object) locales);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
   * @see com.runwaysdk.ClientRequest#loginAnonymous(java.lang.String, Locale[])
   */
  protected String loginAnonymous(String dimensionKey, Locale[] locales)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    String sessionId;

    try
    {
      sessionId = (String) javaAdapterClass.getMethod("loginAnonymous", String.class, Locale[].class).invoke(null, dimensionKey, locales);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
   * @see com.runwaysdk.ClientRequest#logout()
   */
  protected void logout()
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      javaAdapterClass.getMethod("logout", String.class).invoke(null, this.getSessionId());
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
    BusinessDTO generic;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (BusinessDTO) javaAdapterClass.getMethod("newBusiness", String.class, String.class).invoke(null, this.getSessionId(), type);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    return (BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  /**
   * @see com.runwaysdk.constant.ClientRequestIF#newBusiness(java.lang.String)
   */
  public EntityDTO newDisconnectedEntity(String type)
  {
    this.clearNotifications();
    EntityDTO generic;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (EntityDTO) javaAdapterClass.getMethod("newDisconnectedEntity", String.class, String.class).invoke(null, this.getSessionId(), type);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    return (EntityDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  /**
   * @see com.runwaysdk.constant.ClientRequestIF#newGenericBusiness(java.lang.String)
   */
  public BusinessDTO newGenericBusiness(String type)
  {
    this.clearNotifications();
    BusinessDTO generic;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (BusinessDTO) javaAdapterClass.getMethod("newBusiness", String.class, String.class).invoke(null, this.getSessionId(), type);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    return (BusinessDTO) ConversionFacade.createGenericCopyWithTypeSafeAttributes(this, generic);
  }

  /**
   * @see com.runwaysdk.ClientRequest#update(com.runwaysdk.business.MutableDTO)
   */
  public void update(MutableDTO mutableDTO)
  {
    this.clearNotifications();
    MutableDTO generic;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      MutableDTO send = (MutableDTO) ConversionFacade.createGenericCopy(mutableDTO);

      generic = (MutableDTO) javaAdapterClass.getMethod("update", String.class, MutableDTO.class).invoke(null, this.getSessionId(), send);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    ConversionFacade.typeSafeCopy(this, generic, mutableDTO);
  }

  /**
   * @see com.runwaysdk.ClientRequest#assignMember(java.lang.String,
   *      java.lang.String...)
   */
  public void assignMember(String userId, String... roles)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      javaAdapterClass.getMethod("assignMember", String.class, String.class, String[].class).invoke(null, this.getSessionId(), userId, roles);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
   *      java.lang.String...)
   */
  public void removeMember(String userId, String... roles)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      javaAdapterClass.getMethod("removeMember", String.class, String.class, String[].class).invoke(null, this.getSessionId(), userId, roles);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
   * @see com.runwaysdk.ClientRequest#grantStatePermission(java.lang.String,
   *      java.lang.String, String...)
   */
  public void grantStatePermission(String actorId, String stateId, String... operationNames)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      javaAdapterClass.getMethod("grantStatePermission", String.class, String.class, String.class, String[].class).invoke(null, this.getSessionId(), actorId, stateId, operationNames);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
   * @see com.runwaysdk.ClientRequest#grantAttributePermission(java.lang.String,
   *      java.lang.String, String...)
   */
  public void grantAttributePermission(String actorId, String mdAttributeId, String... operationNames)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      javaAdapterClass.getMethod("grantAttributePermission", String.class, String.class, String.class, String[].class).invoke(null, this.getSessionId(), actorId, mdAttributeId, operationNames);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
   * @see com.runwaysdk.ClientRequest#grantAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public void grantAttributeStatePermission(String actorId, String mdAttributeId, String stateId, String... operationNames)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      javaAdapterClass.getMethod("grantAttributeStatePermission", String.class, String.class, String.class, String.class, String[].class).invoke(null, this.getSessionId(), actorId, mdAttributeId, stateId, operationNames);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
   * @see com.runwaysdk.ClientRequest#grantTypePermission(java.lang.String,
   *      java.lang.String, String...)
   */
  public void grantTypePermission(String actorId, String mdTypeId, String... operationNames)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      javaAdapterClass.getMethod("grantTypePermission", String.class, String.class, String.class, String[].class).invoke(null, this.getSessionId(), actorId, mdTypeId, operationNames);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
   * @see com.runwaysdk.ClientRequest#grantMethodPermission(java.lang.String,
   *      java.lang.String, String...)
   */
  public void grantMethodPermission(String actorId, String mdMethodId, String... operationNames)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      javaAdapterClass.getMethod("grantMethodPermission", String.class, String.class, String.class, String[].class).invoke(null, this.getSessionId(), actorId, mdMethodId, operationNames);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
   * @see com.runwaysdk.ClientRequest#promoteObject(BusinessDTO,
   *      java.lang.String)
   */
  public void promoteObject(BusinessDTO businessDTO, String transitionName)
  {
    this.clearNotifications();
    BusinessDTO generic;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (BusinessDTO) javaAdapterClass.getMethod("promoteObject", String.class, BusinessDTO.class, String.class).invoke(null, this.getSessionId(), businessDTO, transitionName);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    ConversionFacade.typeSafeCopy(this, generic, businessDTO);
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeTypePermission(java.lang.String,
   *      java.lang.String, java.lang.String...)
   */
  public void revokeTypePermission(String actorId, String mdTypeId, String... operationNames)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      javaAdapterClass.getMethod("revokeTypePermission", String.class, String.class, String.class, String[].class).invoke(null, this.getSessionId(), actorId, mdTypeId, operationNames);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
   * @see com.runwaysdk.ClientRequest#revokeMethodPermission(java.lang.String,
   *      java.lang.String, java.lang.String...)
   */
  public void revokeMethodPermission(String actorId, String mdMethodId, String... operationNames)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      javaAdapterClass.getMethod("revokeMethodPermission", String.class, String.class, String.class, String[].class).invoke(null, this.getSessionId(), actorId, mdMethodId, operationNames);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
   * @see com.runwaysdk.ClientRequest#revokeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String...)
   */
  public void revokeStatePermission(String actorId, String stateId, String... operationNames)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      javaAdapterClass.getMethod("revokeStatePermission", String.class, String.class, String.class, String[].class).invoke(null, this.getSessionId(), actorId, stateId, operationNames);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
   * @see com.runwaysdk.ClientRequest#revokeAttributePermission(java.lang.String,
   *      java.lang.String, java.lang.String...)
   */
  public void revokeAttributePermission(String actorId, String mdAttributeId, String... operationNames)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      javaAdapterClass.getMethod("revokeAttributePermission", String.class, String.class, String.class, String[].class).invoke(null, this.getSessionId(), actorId, mdAttributeId, operationNames);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
   * @see com.runwaysdk.ClientRequest#revokeAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public void revokeAttributeStatePermission(String actorId, String mdAttributeId, String stateId, String... operationNames)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      javaAdapterClass.getMethod("revokeAttributeStatePermission", String.class, String.class, String.class, String.class, String[].class).invoke(null, this.getSessionId(), actorId, mdAttributeId, stateId, operationNames);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
   * @see com.runwaysdk.ClientRequest#lock(ElementDTO)
   */
  public void lock(ElementDTO elementDTO)
  {
    this.clearNotifications();
    ElementDTO generic;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (ElementDTO) javaAdapterClass.getMethod("lock", String.class, String.class).invoke(null, this.getSessionId(), elementDTO.getId());
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    ConversionFacade.typeSafeCopy(this, generic, elementDTO);
  }

  /**
   * @see com.runwaysdk.ClientRequest#unlock(ElementDTO)
   */
  public void unlock(ElementDTO elementDTO)
  {
    this.clearNotifications();
    ElementDTO generic;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (ElementDTO) javaAdapterClass.getMethod("unlock", String.class, String.class).invoke(null, this.getSessionId(), elementDTO.getId());
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    ConversionFacade.typeSafeCopy(this, generic, elementDTO);
  }

  /**
   * @see com.runwaysdk.ClientRequest#deleteChild(java.lang.String)
   */
  public void deleteChild(String relationshipId)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      javaAdapterClass.getMethod("deleteChild", String.class, String.class).invoke(null, this.getSessionId(), relationshipId);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
   * @see com.runwaysdk.ClientRequest#deleteParent(java.lang.String)
   */
  public void deleteParent(String relationshipId)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      javaAdapterClass.getMethod("deleteParent", String.class, String.class).invoke(null, this.getSessionId(), relationshipId);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
   *
   */
  @SuppressWarnings("unchecked")
  public List<? extends BusinessDTO> getChildren(String id, String relationshipType)
  {
    this.clearNotifications();
    List<BusinessDTO> generics;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generics = (List<BusinessDTO>) javaAdapterClass.getMethod("getChildren", String.class, String.class, String.class).invoke(null, this.getSessionId(), id, relationshipType);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generics = (List<BusinessDTO>) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
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

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generics = (List<BusinessDTO>) javaAdapterClass.getMethod("getParents", String.class, String.class, String.class).invoke(null, this.getSessionId(), id, relationshipType);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generics = (List<BusinessDTO>) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    List<BusinessDTO> safes = new LinkedList<BusinessDTO>();
    for (EntityDTO generic : generics)
    {
      safes.add((BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic));
    }

    return safes;
  }

  /**
   * @see com.runwaysdk.ClientRequest#getChildren(java.lang.String,
   *      java.lang.String)
   */
  @SuppressWarnings("unchecked")
  public List<? extends RelationshipDTO> getChildRelationships(String id, String relationshipType)
  {
    this.clearNotifications();
    List<RelationshipDTO> generics;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generics = (List<RelationshipDTO>) javaAdapterClass.getMethod("getChildRelationships", String.class, String.class, String.class).invoke(null, this.getSessionId(), id, relationshipType);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generics = (List<RelationshipDTO>) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    List<RelationshipDTO> safes = new LinkedList<RelationshipDTO>();
    for (RelationshipDTO generic : generics)
    {
      safes.add((RelationshipDTO) ConversionFacade.createTypeSafeCopy(generic, this));
    }
    return safes;
  }

  /**
   * @see com.runwaysdk.ClientRequest#getParents(com.runwaysdk.business.BusinessDTO,
   *      java.lang.String)
   */
  @SuppressWarnings("unchecked")
  public List<? extends RelationshipDTO> getParentRelationships(String id, String relationshipType)
  {
    this.clearNotifications();
    List<RelationshipDTO> generics;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generics = (List<RelationshipDTO>) javaAdapterClass.getMethod("getParentRelationships", String.class, String.class, String.class).invoke(null, this.getSessionId(), id, relationshipType);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generics = (List<RelationshipDTO>) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    List<RelationshipDTO> safes = new LinkedList<RelationshipDTO>();
    for (RelationshipDTO generic : generics)
    {
      safes.add((RelationshipDTO) ConversionFacade.createTypeSafeCopy(generic, this));
    }
    return safes;
  }

  public StructQueryDTO queryStructs(StructQueryDTO queryDTO)
  {
    this.clearNotifications();
    StructQueryDTO generic;
    queryDTO.clearResultSet();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (StructQueryDTO) javaAdapterClass.getMethod("queryStructs", String.class, StructQueryDTO.class).invoke(null, this.getSessionId(), queryDTO);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (StructQueryDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    return (StructQueryDTO) ConversionFacade.convertGenericQueryToTypeSafe(this, generic);
  }

  /**
   *
   */
  public ViewQueryDTO queryViews(ViewQueryDTO queryDTO)
  {
    this.clearNotifications();
    ViewQueryDTO generic;
    queryDTO.clearResultSet();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (ViewQueryDTO) javaAdapterClass.getMethod("queryViews", String.class, ViewQueryDTO.class).invoke(null, this.getSessionId(), queryDTO);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (ViewQueryDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    return (ViewQueryDTO) ConversionFacade.convertGenericQueryToTypeSafe(this, generic);
  }

  /**
   *
   */
  public BusinessQueryDTO queryBusinesses(BusinessQueryDTO queryDTO)
  {
    this.clearNotifications();
    BusinessQueryDTO generic;
    queryDTO.clearResultSet();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (BusinessQueryDTO) javaAdapterClass.getMethod("queryBusinesses", String.class, BusinessQueryDTO.class).invoke(null, this.getSessionId(), queryDTO);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (BusinessQueryDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
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

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      returnObject = (ComponentQueryDTO) javaAdapterClass.getMethod("groovyObjectQuery", String.class, ComponentQueryDTO.class).invoke(null, this.getSessionId(), componentQueryDTO);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        returnObject = (ComponentQueryDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
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

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      returnObject = (ValueQueryDTO) javaAdapterClass.getMethod("groovyValueQuery", String.class, ValueQueryDTO.class).invoke(null, this.getSessionId(), valueQueryDTO);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        returnObject = (ValueQueryDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    return returnObject;
  }

  public EntityQueryDTO queryEntities(EntityQueryDTO queryDTO)
  {
    this.clearNotifications();
    EntityQueryDTO generic;
    queryDTO.clearResultSet();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (EntityQueryDTO) javaAdapterClass.getMethod("queryEntities", String.class, EntityQueryDTO.class).invoke(null, this.getSessionId(), queryDTO);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    return (EntityQueryDTO) ConversionFacade.convertGenericQueryToTypeSafe(this, generic);
  }

  public RelationshipQueryDTO queryRelationships(RelationshipQueryDTO queryDTO)
  {
    this.clearNotifications();
    RelationshipQueryDTO generic;
    queryDTO.clearResultSet();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (RelationshipQueryDTO) javaAdapterClass.getMethod("queryRelationships", String.class, RelationshipQueryDTO.class).invoke(null, this.getSessionId(), queryDTO);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generic = (RelationshipQueryDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    return (RelationshipQueryDTO) ConversionFacade.convertGenericQueryToTypeSafe(this, generic);
  }

  public void deleteChildren(String id, String relationshipType)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      javaAdapterClass.getMethod("deleteChildren", String.class, String.class, String.class).invoke(null, this.getSessionId(), id, relationshipType);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      javaAdapterClass.getMethod("deleteParents", String.class, String.class, String.class).invoke(null, this.getSessionId(), id, relationshipType);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
   * 
   * @see com.runwaysdk.constants.ClientRequestIF#invokeMethod(com.runwaysdk.transport.MutableDTO,
   *      java.lang.String, java.lang.String[], java.lang.String[],
   *      java.lang.Object[])
   */
  public Object invokeMethod(MethodMetaData metadata, MutableDTO mutableDTO, Object[] parameters)
  {
    this.clearNotifications();
    Object[] output;

    MutableDTO genericDTO = null;

    // Get the String representation of the business layer Class equivalent to
    // the parameters
    String[] actualTypes = ConversionFacade.getClassNames(parameters);
    metadata.setActualTypes(actualTypes);

    // Convert the mutableDTO and the parameters into their generic,
    // serializable form
    if (mutableDTO != null)
    {
      genericDTO = (MutableDTO) ConversionFacade.createGenericCopy(mutableDTO);
    }

    Object[] generics = ConversionFacade.convertGeneric(parameters);

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      // Invoke the method
      output = (Object[]) javaAdapterClass.getMethod("invokeMethod", String.class, MethodMetaData.class, MutableDTO.class, Object[].class).invoke(null, this.getSessionId(), metadata, genericDTO, generics);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    // Update the value of the mutableDTO
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
      Object temp = ConversionFacade.convertToTypeSafe(this, returnType, returnObject);

      return temp;
    }

    return null;

  }

  /**
   * 
   * @see com.runwaysdk.constants.ClientRequestIF#getEnumeration(java.lang.String,
   *      java.lang.String)
   */
  public BusinessDTO getEnumeration(String enumType, String enumName)
  {
    this.clearNotifications();
    BusinessDTO generic;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (BusinessDTO) javaAdapterClass.getMethod("getEnumeration", String.class, String.class, String.class).invoke(null, this.getSessionId(), enumType, enumName);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    return (BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  /**
   * @see com.runwaysdk.constants.ClientRequestIF#getEnumerations(String,
   *      String, String[])
   */
  @SuppressWarnings("unchecked")
  public List<BusinessDTO> getEnumerations(String enumType, String[] enumNames)
  {
    this.clearNotifications();
    List<BusinessDTO> generics;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generics = (List<BusinessDTO>) javaAdapterClass.getMethod("getEnumerations", String.class, String.class, String[].class).invoke(null, this.getSessionId(), enumType, enumNames);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generics = (List<BusinessDTO>) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    List<BusinessDTO> safes = new LinkedList<BusinessDTO>();
    for (BusinessDTO generic : generics)
    {
      safes.add((BusinessDTO) ConversionFacade.createTypeSafeCopy(generic, this));
    }
    return safes;
  }

  /**
   * @see com.runwaysdk.constants.ClientRequestIF#getAllEnumerations(String,
   *      String)
   */
  @SuppressWarnings("unchecked")
  public List<BusinessDTO> getAllEnumerations(String enumType)
  {
    this.clearNotifications();
    List<BusinessDTO> generics;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generics = (List<BusinessDTO>) javaAdapterClass.getMethod("getAllEnumerations", String.class, String.class).invoke(null, this.getSessionId(), enumType);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        generics = (List<BusinessDTO>) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    List<BusinessDTO> safes = new LinkedList<BusinessDTO>();
    for (BusinessDTO generic : generics)
    {
      safes.add((BusinessDTO) ConversionFacade.createTypeSafeCopy(generic, this));
    }
    return safes;
  }

  public BusinessDTO getVaultFileDTO(String type, String attributeName, String fileId)
  {
    this.clearNotifications();
    BusinessDTO returnObject;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      returnObject = (BusinessDTO) javaAdapterClass.getMethod("getVaultFileDTO", String.class, String.class, String.class, String.class).invoke(null, this.getSessionId(), type, attributeName, fileId);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        returnObject = (BusinessDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    return returnObject;
  }

  public void createStruct(StructDTO structDTO)
  {
    this.clearNotifications();
    StructDTO generic;
    StructDTO _structDTO = (StructDTO) ConversionFacade.createGenericCopy(structDTO);

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (StructDTO) javaAdapterClass.getMethod("createStruct", String.class, StructDTO.class).invoke(null, this.getSessionId(), _structDTO);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    ConversionFacade.typeSafeCopy(this, generic, structDTO);
  }

  public StructDTO newStruct(String type)
  {
    this.clearNotifications();
    StructDTO generic;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (StructDTO) javaAdapterClass.getMethod("newStruct", String.class, String.class).invoke(null, this.getSessionId(), type);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    return (StructDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  /**
   * @see com.runwaysdk.constant.ClientRequestIF#newGenericStruct(java.lang.String)
   */
  public StructDTO newGenericStruct(String type)
  {
    this.clearNotifications();
    StructDTO generic;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (StructDTO) javaAdapterClass.getMethod("newStruct", String.class, String.class).invoke(null, this.getSessionId(), type);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    return (StructDTO) ConversionFacade.createGenericCopyWithTypeSafeAttributes(this, generic);
  }

  /**
   * @see com.runwaysdk.constant.ClientRequestIF#newMutable(java.lang.String)
   */
  public MutableDTO newMutable(String type)
  {
    this.clearNotifications();
    MutableDTO generic;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (MutableDTO) javaAdapterClass.getMethod("newMutable", String.class, String.class).invoke(null, this.getSessionId(), type);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    return (MutableDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  /**
   * @see com.runwaysdk.constant.ClientRequestIF#newGenericMutable(java.lang.String)
   */
  public MutableDTO newGenericMutable(String type)
  {
    this.clearNotifications();
    MutableDTO generic;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (MutableDTO) javaAdapterClass.getMethod("newMutable", String.class, String.class).invoke(null, this.getSessionId(), type);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    return (MutableDTO) ConversionFacade.createGenericCopyWithTypeSafeAttributes(this, generic);
  }

  /**
   * @see com.runwaysdk.constant.ClientRequestIF#newGenericException(java.lang.String)
   */
  public SmartExceptionDTO newGenericException(String type)
  {
    this.clearNotifications();
    ExceptionDTO generic;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (ExceptionDTO) javaAdapterClass.getMethod("newMutable", String.class, String.class).invoke(null, this.getSessionId(), type);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    return (SmartExceptionDTO) ConversionFacade.createGenericCopyWithTypeSafeAttributes(this, generic);
  }

  public ClassQueryDTO getQuery(String type)
  {
    this.clearNotifications();
    ClassQueryDTO classQueryDTO;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      classQueryDTO = (ClassQueryDTO) javaAdapterClass.getMethod("getQuery", String.class, String.class).invoke(null, this.getSessionId(), type);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    return classQueryDTO;
  }

  public void importDomainModel(String xml, String xsd)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      javaAdapterClass.getMethod("importDomainModel", String.class, String.class, String.class).invoke(null, this.getSessionId(), xml, xsd);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

  protected InputStream getFileFromServer(String fileId)
  {
    this.clearNotifications();
    InputStream inputStream;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      inputStream = (InputStream) javaAdapterClass.getMethod("getFile", String.class, String.class).invoke(null, this.getSessionId(), fileId);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        inputStream = (InputStream) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    return inputStream;
  }

  protected InputStream getSecureFileFromServer(String attributeName, String type, String fileId)
  {
    this.clearNotifications();
    InputStream inputStream;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      inputStream = (InputStream) javaAdapterClass.getMethod("getSecureFile", String.class, String.class, String.class, String.class).invoke(null, this.getSessionId(), type, attributeName, fileId);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        inputStream = (InputStream) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    return inputStream;
  }

  protected InputStream getSecureFileFromServer(String fileId)
  {
    this.clearNotifications();
    InputStream inputStream;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      inputStream = (InputStream) javaAdapterClass.getMethod("getSecureFile", String.class, String.class).invoke(null, this.getSessionId(), fileId);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        inputStream = (InputStream) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    return inputStream;
  }

  public BusinessDTO newFile(String path, String filename, String extension, InputStream stream)
  {
    this.clearNotifications();
    BusinessDTO generic;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (BusinessDTO) javaAdapterClass.getMethod("newFile", String.class, String.class, String.class, String.class, InputStream.class).invoke(null, this.getSessionId(), path, filename, extension, stream);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    return (BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  public BusinessDTO newSecureFile(String filename, String extension, InputStream stream)
  {
    this.clearNotifications();
    BusinessDTO generic;

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      generic = (BusinessDTO) javaAdapterClass.getMethod("newSecureFile", String.class, String.class, String.class, InputStream.class).invoke(null, this.getSessionId(), filename, extension, stream);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    return (BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  public void checkAdminScreenAccess()
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      javaAdapterClass.getMethod("checkAdminScreenAccess", String.class).invoke(null, this.getSessionId());
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      javaAdapterClass.getMethod("importInstanceXML", String.class, String.class).invoke(null, this.getSessionId(), xml);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
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
   * This method generates an excel file template for import for the given class type. The listener builder class
   * will build the necessary listeners and add them to the excel exporter.
   * 
   * @param sessionId
   * @param exportType type to be exported
   * @param excelListenerBuilderClass the class that builds the listeners 
   * @param listenerMethod defined on the given view type
   * @param params parameters for the listener method
   * @return
   */
  public InputStream exportExcelFile(String exportType, String excelListenerBuilderClass, String listenerMethod, String... params)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      return (InputStream) javaAdapterClass.getMethod("exportExcelFile", String.class, String.class, String.class, String.class, String[].class).invoke(null, this.getSessionId(), exportType, excelListenerBuilderClass, listenerMethod, params);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
        return (InputStream) me.getReturnObject();
      }
      else
      {
        throw rte;
      }
    }
  }
  
  /**
   * This method generates an excel file template for import for the entity types that are referenced by 
   * the given view type. Sometimes the type that the user is familiar with is not the same type as
   * what is stored in the database for normalization reasons. The give view type defines the given {@param listenerMethod}. 
   * 
   * @param sessionId
   * @param viewType view type that references entity types to be imported
   * @param listenerMethod defined on the given view type
   * @param params parameters for the listener method
   * @return
   */
  public InputStream exportExcelFile(String viewType, String listenerMethod, String... params)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      return (InputStream) javaAdapterClass.getMethod("exportExcelFile", String.class, String.class, String.class, String[].class).invoke(null, this.getSessionId(), viewType, listenerMethod, params);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
        return (InputStream) me.getReturnObject();
      }
      else
      {
        throw rte;
      }
    }
  }

  public InputStream importExcelFile(InputStream stream, String type, String listenerMethod, String... params)
  {
    this.clearNotifications();

    Class<?> javaAdapterClass = LoaderDecorator.load(AdapterInfo.JAVA_ADAPTER_CLASS);

    try
    {
      return (InputStream) javaAdapterClass.getMethod("importExcelFile", String.class, InputStream.class, String.class, String.class, String[].class).invoke(null, this.getSessionId(), stream, type, listenerMethod, params);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, false);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO) rte;
        this.setMessagesConvertToTypeSafe(me);
        return (InputStream) me.getReturnObject();
      }
      else
      {
        throw rte;
      }
    }
  }
}
