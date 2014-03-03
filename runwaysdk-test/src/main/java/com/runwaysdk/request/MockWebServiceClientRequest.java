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
package com.runwaysdk.request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import com.runwaysdk.dataaccess.io.FileWriteExceptionDTO;
import com.runwaysdk.facade.WebServiceAdapter;
import com.runwaysdk.transport.conversion.ClientConversionFacade;
import com.runwaysdk.transport.conversion.ConversionFacade;
import com.runwaysdk.util.DTOConversionUtilInfo;
import com.runwaysdk.util.FileIO;

public class MockWebServiceClientRequest extends ClientRequest
{
  /**
   *
   */
  private static final long serialVersionUID = 4861613943744488840L;

  /**
   * Constructor that takes in a <code>ClientSession</code> that contains an
   * an address to the location of the server hosting the web services.
   *
   * @param clientSession
   * @param locales
   */
  public MockWebServiceClientRequest(ClientSession clientSession, Locale[] locales)
  {
    super(clientSession, locales);
  }

  /**
   * Constructor that takes in a <code>ClientSession</code> that contains an
   * an address to the location of the server hosting the web services.
   *
   * @param clientSession
   * @param sessionId
   */
  public MockWebServiceClientRequest(ClientSession clientSession, String sessionId)
  {
    super(clientSession, sessionId);
  }

  /**
   * Constructor that takes in a <code>ClientSession</code> that contains an
   * an address to the location of the server hosting the web services.
   *
   * @param clientSession
   * @param userName
   * @param password
   * @param locales
   */
  public MockWebServiceClientRequest(ClientSession clientSession, String userName, String password, Locale[] locales)
  {
    super(clientSession, userName, password, locales);
  }

  public RelationshipDTO addChild(String parentId, String childId,
      String relationshipType)
  {
    this.clearNotifications();
    Document returnDoc = null;
    RelationshipDTO generic = null;
    try
    {
      returnDoc = WebServiceAdapter.addChild(this.getSessionId(), parentId, childId, relationshipType);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (RelationshipDTO)me.getReturnObject();
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

  public RelationshipDTO addParent(String parentId, String childId,
      String relationshipType)
  {
    this.clearNotifications();
    Document returnDoc = null;
    RelationshipDTO generic = null;
    try
    {
      returnDoc = WebServiceAdapter.addParent(this.getSessionId(), parentId, childId, relationshipType);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (RelationshipDTO)me.getReturnObject();
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

  protected void changeLogin(String username, String password)
  {
    this.clearNotifications();
    try
    {
      WebServiceAdapter.changeLogin(this.getSessionId(), username, password);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
    this.setIsPublicUser(username);
  }

  public BusinessDTO getSessionUser()
  {
    this.clearNotifications();
    Document document = null;
    BusinessDTO generic = null;
    try
    {
      document = WebServiceAdapter.getSessionUser(this.getSessionId());
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (BusinessDTO)me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
    if (generic == null)
    {
      generic = (BusinessDTO)ConversionFacade.getComponentDTOIFfromDocument(this, document);
    }
    return (BusinessDTO)ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  public Map<String, String> getSessionUserRoles()
  {
    throw new RuntimeException("Unimplemented method");
  }

  public void createSessionComponent(SessionDTO sessionDTO)
  {
    this.clearNotifications();
    Document returnDoc = null;
    SessionDTO generic = null;
    Document document = null;
    document = ConversionFacade.getDocumentFromComponentDTO(sessionDTO, false);
    try
    {
      returnDoc = WebServiceAdapter.createSessionComponent(this.getSessionId(), document);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (SessionDTO)me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (SessionDTO)ConversionFacade.getComponentDTOIFfromDocument(this, returnDoc);
    }
    ConversionFacade.typeSafeCopy(this, generic, sessionDTO);
  }

  public void createBusiness(BusinessDTO businessDTO)
  {
    this.clearNotifications();
    Document returnDoc = null;
    EntityDTO generic = null;
    Document document = ConversionFacade.getDocumentFromComponentDTO(businessDTO, false);;
    try
    {
      returnDoc = WebServiceAdapter.createBusiness(this.getSessionId(), document);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (EntityDTO)me.getReturnObject();
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

  public void createRelationship(RelationshipDTO relationshipDTO)
  {
    this.clearNotifications();
    Document returnDoc = null;
    EntityDTO generic = null;
    Document document = ConversionFacade.getDocumentFromComponentDTO(relationshipDTO, false);
    try
    {
      returnDoc = WebServiceAdapter.createRelationship(this.getSessionId(), document);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (EntityDTO)me.getReturnObject();
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
    try
    {
      Document document = ConversionFacade.getDocumentFromComponentDTO(structDTO, false);
      returnDoc = WebServiceAdapter.createStruct(this.getSessionId(), document);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (EntityDTO)me.getReturnObject();
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

  public void deleteChild(String relationshipId)
  {
    this.clearNotifications();
    try
    {
      WebServiceAdapter.deleteChild(this.getSessionId(), relationshipId);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
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
    try
    {
      WebServiceAdapter.deleteChildren(this.getSessionId(), id, relationshipType);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  public void delete(String id)
  {
    this.clearNotifications();
    try
    {
      WebServiceAdapter.delete(this.getSessionId(), id);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  public void deleteParent(String relationshipId)
  {
    this.clearNotifications();
    try
    {
      WebServiceAdapter.deleteParent(this.getSessionId(), relationshipId);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
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
    try
    {
      WebServiceAdapter.deleteParents(this.getSessionId(), id, relationshipType);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  public MutableDTO get(String id)
  {
    this.clearNotifications();
    Document document = null;
    MutableDTO generic = null;
    try
    {
      document = WebServiceAdapter.get(this.getSessionId(), id);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (MutableDTO)me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (MutableDTO)ConversionFacade.getComponentDTOIFfromDocument(this, document);
    }
    return (MutableDTO)ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  public List<? extends RelationshipDTO> getChildRelationships(String id, String relationshipType)
  {
    this.clearNotifications();
    Document documentRels = null;
    RelationshipDTO[] genericArray = null;
    try
    {
      documentRels = WebServiceAdapter.getChildRelationships(this.getSessionId(), id, relationshipType);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        genericArray = (RelationshipDTO[])me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (genericArray == null)
    {
      genericArray = (RelationshipDTO[])ConversionFacade.getObjectFromDocument(this, documentRels);
    }

    List<RelationshipDTO> relationshipDTOs = new LinkedList<RelationshipDTO>();
    for (RelationshipDTO generic : genericArray)
    {
      relationshipDTOs.add((RelationshipDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic));
    }
    return relationshipDTOs;
  }

  public List<? extends BusinessDTO> getChildren(String parentId, String relationshipType)
  {
    this.clearNotifications();
    Document documentRels = null;
    BusinessDTO[] genericArray = null;
    try
    {
      documentRels = WebServiceAdapter.getChildren(this.getSessionId(), parentId, relationshipType);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        genericArray = (BusinessDTO[])me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (genericArray == null)
    {
      genericArray = (BusinessDTO[])ConversionFacade.getObjectFromDocument(this, documentRels);
    }

    List<BusinessDTO> businessDTOs = new LinkedList<BusinessDTO>();
    for (BusinessDTO generic : genericArray)
    {
      businessDTOs.add((BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic));
    }
    return businessDTOs;
  }

  public BusinessDTO getEnumeration(String enumType, String enumName)
  {
    this.clearNotifications();
    Document document = null;
    BusinessDTO generic = null;
    try
    {
      document = WebServiceAdapter.getEnumeration(this.getSessionId(), enumType, enumName);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (BusinessDTO)me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (BusinessDTO)ConversionFacade.getComponentDTOIFfromDocument(this, document);
    }

    return (BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  public List<BusinessDTO> getEnumerations(String enumType, String[] enumNames)
  {
    this.clearNotifications();
    Document documentRels = null;
    BusinessDTO[] genericArray = null;
    try
    {
      documentRels = WebServiceAdapter.getEnumerations(this.getSessionId(), enumType, enumNames);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        genericArray = (BusinessDTO[])me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (genericArray == null)
    {
      genericArray = (BusinessDTO[])ConversionFacade.getObjectFromDocument(this, documentRels);
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
    Document documentRels = null;
    BusinessDTO[] genericArray = null;
    try
    {
      documentRels = WebServiceAdapter.getAllEnumerations(this.getSessionId(), enumType);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        genericArray = (BusinessDTO[])me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (genericArray == null)
    {
      genericArray = (BusinessDTO[])ConversionFacade.getObjectFromDocument(this, documentRels);
    }

    List<BusinessDTO> businessDTOs = new LinkedList<BusinessDTO>();
    for (BusinessDTO generic : genericArray)
    {
      businessDTOs.add((BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic));
    }

    return businessDTOs;
  }

  protected InputStream getFileFromServer(String fileId)
  {
    this.clearNotifications();
    Document document = null;
    Byte[] bytes = null;
    try
    {
      document = WebServiceAdapter.getFile(this.getSessionId(), fileId);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        bytes = (Byte[])me.getReturnObject();
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

  public List<? extends RelationshipDTO> getParentRelationships(String id, String relationshipType)
  {
    this.clearNotifications();
    Document documentRels = null;
    RelationshipDTO[] genericArray = null;
    try
    {
      documentRels = WebServiceAdapter.getParentRelationships(this.getSessionId(), id, relationshipType);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        genericArray = (RelationshipDTO[])me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (genericArray == null)
    {
      genericArray = (RelationshipDTO[])ConversionFacade.getObjectFromDocument(this, documentRels);
    }

    List<RelationshipDTO> relationshipDTOs = new LinkedList<RelationshipDTO>();
    for (RelationshipDTO generic : genericArray)
    {
      relationshipDTOs.add((RelationshipDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic));
    }
    return relationshipDTOs;
  }

  public List<? extends BusinessDTO> getParents(String childid, String relationshipType)
  {
    this.clearNotifications();
    Document documentRels = null;
    BusinessDTO[] genericArray = null;
    try
    {
      documentRels = WebServiceAdapter.getParents(this.getSessionId(), childid, relationshipType);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        genericArray = (BusinessDTO[])me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (genericArray == null)
    {
      genericArray = (BusinessDTO[])ConversionFacade.getObjectFromDocument(this, documentRels);
    }

    List<BusinessDTO> businessDTOs = new LinkedList<BusinessDTO>();
    for (BusinessDTO generic : genericArray)
    {
      businessDTOs.add((BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic));
    }
    return businessDTOs;
  }

  public ClassQueryDTO getQuery(String type)
  {
    this.clearNotifications();
    Document document = null;
    ClassQueryDTO classQueryDTO = null;
    try
    {
      document = WebServiceAdapter.getQuery(this.getSessionId(), type);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        classQueryDTO = (ClassQueryDTO)me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (classQueryDTO == null)
    {
      classQueryDTO = (ClassQueryDTO)ConversionFacade.getQueryDTOFromDocument(this, document, true);
    }

    return classQueryDTO;
  }

  protected InputStream getSecureFileFromServer(String attributeName, String type, String fileId)
  {
    this.clearNotifications();
    Document document = null;
    Byte[] bytes = null;
    try
    {
      document = WebServiceAdapter.getSecureFile(this.getSessionId(), attributeName, type, fileId);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        bytes = (Byte[])me.getReturnObject();
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

  protected InputStream getSecureFileFromServer(String fileId)
  {
    this.clearNotifications();
    Document document = null;
    Byte[] bytes = null;
    try
    {
      document = WebServiceAdapter.getSecureFile(this.getSessionId(), fileId);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        bytes = (Byte[])me.getReturnObject();
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

  public BusinessDTO getVaultFileDTO(String type, String attributeName, String fileId)
  {
    this.clearNotifications();
    Document returnDocument = null;
    BusinessDTO generic = null;
    try
    {
      returnDocument = WebServiceAdapter.getVaultFileDTO(this.getSessionId(), type, attributeName, fileId);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (BusinessDTO)me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (BusinessDTO)ConversionFacade.getObjectFromDocument(this, returnDocument);
    }

    return generic;
  }

  /**
   * @see com.runwaysdk.request.ClientRequest#assignMember(
   *      java.lang.String, java.lang.String...)
   */
  public void assignMember(String userId, String ... roles)
  {
    this.clearNotifications();
    try
    {
      WebServiceAdapter.assignMember(this.getSessionId(), userId, roles);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  /**
   * @see com.runwaysdk.request.ClientRequest#removeMember(
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public void removeMember(String userId, String ... roles)
  {
    this.clearNotifications();
    try
    {
      WebServiceAdapter.removeMember(this.getSessionId(), userId, roles);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  public void grantAttributePermission(String actorId, String mdAttributeId, String... operationNames)
  {
    this.clearNotifications();
    try
    {
      WebServiceAdapter.grantAttributePermission(this.getSessionId(), actorId, mdAttributeId, operationNames);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  public void grantAttributeStatePermission(String actorId, String mdAttributeId, String stateId, String... operationNames)
  {
    this.clearNotifications();
    try
    {
      WebServiceAdapter.grantAttributeStatePermission(this.getSessionId(), actorId, mdAttributeId, stateId, operationNames);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  public void grantStatePermission(String actorId, String stateId, String... operationNames)
  {
    this.clearNotifications();
    try
    {
      WebServiceAdapter.grantStatePermission(this.getSessionId(), actorId, stateId, operationNames);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  public void grantTypePermission(String actorId, String mdTypeId, String... operationNames)
  {
    this.clearNotifications();
    try
    {
      WebServiceAdapter.grantTypePermission(this.getSessionId(), actorId, mdTypeId, operationNames);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  public void grantMethodPermission(String actorId, String mdMethodId, String... operationNames)
  {
    this.clearNotifications();
    try
    {
      WebServiceAdapter.grantMethodPermission(this.getSessionId(), actorId, mdMethodId, operationNames);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }


  /**
   * Returns a ComponentQueryDTO containing the results of an arbitrary query for a given type.
   *
   * @param ComponentQueryDTO
   * @return ComponentQueryDTO containing the query result.
   */
  public ComponentQueryDTO groovyObjectQuery(ComponentQueryDTO componentQueryDTO)
  {
    this.clearNotifications();
    Document document;
    ComponentQueryDTO generic = null;

    componentQueryDTO.clearResultSet();
    document = ConversionFacade.getDocumentFromQueryDTO(componentQueryDTO);

    try
    {
      document = WebServiceAdapter.groovyObjectQuery(this.getSessionId(), document);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (ComponentQueryDTO)me.getReturnObject();
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
   * Returns a ValueQueryDTO containing the results of an arbitrary value query.
   *
   * @param valueQueryDTO
   * @return ValueQueryDTO containing the query result.
   */
  public ValueQueryDTO groovyValueQuery(ValueQueryDTO valueQueryDTO)
  {
    this.clearNotifications();
    valueQueryDTO.clearResultSet();
    Document document = ConversionFacade.getDocumentFromQueryDTO(valueQueryDTO);
    ValueQueryDTO generic = null;
    try
    {
      document = WebServiceAdapter.groovyValueQuery(this.getSessionId(), document);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (ValueQueryDTO)me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (ValueQueryDTO)ConversionFacade.getQueryDTOFromDocument(this, document, false);
    }

    return generic;
  }

  public Object invokeMethod(MethodMetaData metadata, MutableDTO mutableDTO, Object[] parameters)
  {
    this.clearNotifications();
    Object[] output = null;
    Document genericDTO = null;

    // Get the String representation of the business layer Class equivalent to the parameters
    String[] actualTypes = ConversionFacade.getClassNames(parameters);
    metadata.setActualTypes(actualTypes);

    Document metaDoc = ConversionFacade.getDocumentFromMethodMetaData(metadata);

    // Convert the MutableDTO and the parameters into their generic, serializable form
    if(mutableDTO != null)
    {
      MutableDTO genericCopy = (MutableDTO)ConversionFacade.createGenericCopy(mutableDTO);
      genericDTO = ConversionFacade.getDocumentFromComponentDTO(genericCopy, false);
    }

    Object[] convertGeneric = ConversionFacade.convertGeneric(parameters);
    Document[] generics = ConversionFacade.getDocumentArrayFromObjects(convertGeneric, false);

    Document out = null;

    try
    {
      out = WebServiceAdapter.invokeMethod(this.getSessionId(), metaDoc, genericDTO, generics);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        output = (Object[])me.getReturnObject();
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

    // Update the value of the MutableDTO
    if(mutableDTO != null)
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

  public void lock(ElementDTO elementDTO)
  {
    this.clearNotifications();
    Document document = null;
    ElementDTO generic = null;
    try
    {
      document = WebServiceAdapter.lock(this.getSessionId(), elementDTO.getId());
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (ElementDTO)me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (ElementDTO)ConversionFacade.getComponentDTOIFfromDocument(this, document);
    }

    ConversionFacade.typeSafeCopy(this, generic, elementDTO);
  }

  protected String login(String username, String password, Locale[] locales)
  {
    this.clearNotifications();
    String sessionId;
    try
    {
      String[] stringLocales = new String[locales.length];

      for(int i = 0; i < locales.length; i++)
      {
        stringLocales[i] = locales[i].toString();
      }

      sessionId = WebServiceAdapter.login(username, password, stringLocales);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        sessionId = (String)me.getReturnObject();
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

  protected String login(String username, String password, String dimensionKey, Locale[] locales)
  {
    this.clearNotifications();
    String sessionId;
    try
    {
      String[] stringLocales = new String[locales.length];

      for(int i = 0; i < locales.length; i++)
      {
        stringLocales[i] = locales[i].toString();
      }

      sessionId = WebServiceAdapter.login(username, password, dimensionKey, stringLocales);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        sessionId = (String)me.getReturnObject();
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

  protected void setDimension(String sessionId, String dimensionKey)
  {
    this.clearNotifications();
    try
    {
      WebServiceAdapter.setDimension(sessionId, dimensionKey);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        sessionId = (String)me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  protected String loginAnonymous(Locale[] locales)
  {
    this.clearNotifications();
    String sessionId;
    try
    {
      String[] stringLocales = new String[locales.length];

      for(int i = 0; i < locales.length; i++)
      {
        stringLocales[i] = locales[i].toString();
      }

      sessionId = WebServiceAdapter.loginAnonymous(stringLocales);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        sessionId = (String)me.getReturnObject();
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

  protected String loginAnonymous(String dimensionKey, Locale[] locales)
  {
    this.clearNotifications();
    String sessionId;
    try
    {
      String[] stringLocales = new String[locales.length];

      for(int i = 0; i < locales.length; i++)
      {
        stringLocales[i] = locales[i].toString();
      }

      sessionId = WebServiceAdapter.loginAnonymous(dimensionKey, stringLocales);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        sessionId = (String)me.getReturnObject();
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

  protected void logout()
  {
    this.clearNotifications();
    try
    {
      WebServiceAdapter.logout(this.getSessionId());
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
    this.setLoginStatus(false, false);
  }

  public BusinessDTO newBusiness(String type)
  {
    this.clearNotifications();
    Document document = null;
    BusinessDTO generic = null;
    try
    {
      document = WebServiceAdapter.newBusiness(this.getSessionId(), type);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (BusinessDTO)me.getReturnObject();
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

  public MutableDTO newMutable(String type)
  {
    this.clearNotifications();
    Document document = null;
    MutableDTO generic = null;
    try
    {
      document = WebServiceAdapter.newMutable(this.getSessionId(), type);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (MutableDTO)me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (MutableDTO)ConversionFacade.getComponentDTOIFfromDocument(this, document);
    }

    return (MutableDTO)ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  public BusinessDTO newFile(String path, String filename, String extension, InputStream stream)
  {
    this.clearNotifications();
    BusinessDTO generic = null;
    Byte[] bytes;
    try
    {
      bytes = FileIO.getBytesFromStream(stream);
    }
    catch(IOException e)
    {
      throw new FileWriteExceptionDTO(e.getLocalizedMessage());
    }

    Document document = ConversionFacade.getDocumentFromObject(bytes, false);
    Document output = null;

    try
    {
      output = WebServiceAdapter.newFile(this.getSessionId(), path, filename, extension, document);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (BusinessDTO)me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic =  (BusinessDTO)ConversionFacade.getObjectFromDocument(this, output);
    }

    return (BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  public BusinessDTO newGenericBusiness(String type)
  {
    this.clearNotifications();
    Document document = null;
    BusinessDTO generic = null;
    try
    {
      document = WebServiceAdapter.newBusiness(this.getSessionId(), type);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (BusinessDTO)me.getReturnObject();
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

  public MutableDTO newGenericMutable(String type)
  {
    this.clearNotifications();
    Document document = null;
    MutableDTO generic = null;
    try
    {
      document = WebServiceAdapter.newMutable(this.getSessionId(), type);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (MutableDTO)me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (MutableDTO)ConversionFacade.getComponentDTOIFfromDocument(this, document);
    }

    return (MutableDTO) ConversionFacade.createGenericCopyWithTypeSafeAttributes(this, generic);
  }

  public SmartExceptionDTO newGenericException(String type)
  {
    this.clearNotifications();
    Document document = null;
    ExceptionDTO generic = null;
    try
    {
      document = WebServiceAdapter.newMutable(this.getSessionId(), type);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (ExceptionDTO)me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (ExceptionDTO)ConversionFacade.getComponentDTOIFfromDocument(this, document);
    }

    return (SmartExceptionDTO) ConversionFacade.createGenericCopyWithTypeSafeAttributes(this, generic);
  }

  public StructDTO newGenericStruct(String type)
  {
    this.clearNotifications();
    Document document = null;
    StructDTO generic = null;
    try
    {
      document = WebServiceAdapter.newStruct(this.getSessionId(), type);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (StructDTO)me.getReturnObject();
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

  public BusinessDTO newSecureFile(String filename, String extension, InputStream stream)
  {
    this.clearNotifications();
    Byte[] bytes;
    BusinessDTO generic = null;
    try
    {
      bytes = FileIO.getBytesFromStream(stream);
    }
    catch(IOException e)
    {
      //Change exception type - unable to read bytes from the stream
      throw new RuntimeException(e);
    }
    Document document = ConversionFacade.getDocumentFromObject(bytes, false);
    Document output = null;
    try
    {
      output = WebServiceAdapter.newSecureFile(this.getSessionId(), filename, extension, document);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (BusinessDTO)me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (BusinessDTO)ConversionFacade.getObjectFromDocument(null, output);
    }

    return (BusinessDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  public StructDTO newStruct(String type)
  {
    this.clearNotifications();
    Document document = null;
    StructDTO generic = null;
    try
    {
      document = WebServiceAdapter.newStruct(this.getSessionId(), type);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (StructDTO)me.getReturnObject();
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

    return (StructDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, generic);
  }

  public void promoteObject(BusinessDTO businessDTO, String transitionName)
  {
    this.clearNotifications();
    Document returnDoc = null;
    BusinessDTO generic = null;

    Document document = ConversionFacade.getDocumentFromComponentDTO(businessDTO, false);
    try
    {
      returnDoc = WebServiceAdapter.promoteObject(this.getSessionId(), document, transitionName);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (BusinessDTO)me.getReturnObject();
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

  public ViewQueryDTO queryViews(ViewQueryDTO queryDTO)
  {
    this.clearNotifications();
    queryDTO.clearResultSet();
    Document document = ConversionFacade.getDocumentFromQueryDTO(queryDTO);

    ViewQueryDTO viewQueryDTO = null;

    try
    {
      document = WebServiceAdapter.queryViews(this.getSessionId(), document);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        viewQueryDTO = (ViewQueryDTO)me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
    if (viewQueryDTO == null)
    {
      viewQueryDTO = (ViewQueryDTO)ConversionFacade.getQueryDTOFromDocument(this, document, true);
      return (ViewQueryDTO)ConversionFacade.convertGenericQueryToTypeSafe(this, viewQueryDTO);
    }
    else
    { // query object  from the message query is already type safe.
      return viewQueryDTO;
    }
  }

  public BusinessQueryDTO queryBusinesses(BusinessQueryDTO queryDTO)
  {
    this.clearNotifications();
    queryDTO.clearResultSet();
    Document document = ConversionFacade.getDocumentFromQueryDTO(queryDTO);

    BusinessQueryDTO businessQueryDTO = null;

    try
    {
      document = WebServiceAdapter.queryBusinesses(this.getSessionId(), document);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        businessQueryDTO = (BusinessQueryDTO)me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (businessQueryDTO == null)
    {
      businessQueryDTO = (BusinessQueryDTO)ConversionFacade.getQueryDTOFromDocument(this, document, true);
      return (BusinessQueryDTO) ConversionFacade.convertGenericQueryToTypeSafe(this, businessQueryDTO);
    }
    else
    { // query object  from the message query is already type safe.
      return businessQueryDTO;
    }
  }

  public EntityQueryDTO queryEntities(EntityQueryDTO queryDTO)
  {
    this.clearNotifications();
    queryDTO.clearResultSet();
    Document document = ConversionFacade.getDocumentFromQueryDTO(queryDTO);
    EntityQueryDTO entityQueryDTO = null;

    try
    {
      document = WebServiceAdapter.queryEntities(this.getSessionId(), document);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        entityQueryDTO = (EntityQueryDTO)me.getReturnObject();
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
    { // query object  from the message query is already type safe.
      return entityQueryDTO;
    }
  }

  public RelationshipQueryDTO queryRelationships(RelationshipQueryDTO queryDTO)
  {
    this.clearNotifications();
    queryDTO.clearResultSet();
    Document document = ConversionFacade.getDocumentFromQueryDTO(queryDTO);

    RelationshipQueryDTO relationshipQueryDTO = null;

    try
    {
      document = WebServiceAdapter.queryRelationships(this.getSessionId(), document);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        relationshipQueryDTO = (RelationshipQueryDTO)me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (relationshipQueryDTO == null)
    {
      relationshipQueryDTO = (RelationshipQueryDTO)ConversionFacade.getQueryDTOFromDocument(this, document, true);
      return (RelationshipQueryDTO) ConversionFacade.convertGenericQueryToTypeSafe(this, relationshipQueryDTO);
    }
    else
    { // query object  from the message query is already type safe.
      return relationshipQueryDTO;
    }
  }

  public StructQueryDTO queryStructs(StructQueryDTO queryDTO)
  {
    this.clearNotifications();
    queryDTO.clearResultSet();
    Document document = ConversionFacade.getDocumentFromQueryDTO(queryDTO);

    StructQueryDTO structQueryDTO = null;
    try
    {
      document = WebServiceAdapter.queryStructs(this.getSessionId(), document);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        structQueryDTO = (StructQueryDTO)me.getReturnObject();
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
    { // query object  from the message query is already type safe.
      return structQueryDTO;
    }
  }

  public void revokeAttributePermission(String actorId, String mdAttributeId, String... operationNames)
  {
    this.clearNotifications();
    try
    {
      WebServiceAdapter.revokeAttributePermission(this.getSessionId(), actorId, mdAttributeId, operationNames);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  public void revokeAttributeStatePermission(String actorId, String mdAttributeId, String stateId, String... operationNames)
  {
    this.clearNotifications();
    try
    {
      WebServiceAdapter.revokeAttributeStatePermission(this.getSessionId(), actorId, mdAttributeId, stateId, operationNames);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  public void revokeStatePermission(String actorId, String stateId, String... operationNames)
  {
    this.clearNotifications();
    try
    {
      WebServiceAdapter.revokeStatePermission(this.getSessionId(), actorId, stateId, operationNames);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  public void revokeTypePermission(String actorId, String mdTypeId, String... operationNames)
  {
    this.clearNotifications();
    try
    {
      WebServiceAdapter.revokeTypePermission(this.getSessionId(), actorId, mdTypeId, operationNames);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  public void revokeMethodPermission(String actorId, String mdMethodId, String... operationNames)
  {
    this.clearNotifications();
    try
    {
      WebServiceAdapter.revokeMethodPermission(this.getSessionId(), actorId, mdMethodId, operationNames);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }
  }

  public void unlock(ElementDTO elementDTO)
  {
    this.clearNotifications();
    Document document = null;
    ElementDTO generic = null;
    try
    {
      document = WebServiceAdapter.unlock(this.getSessionId(), elementDTO.getId());
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (ElementDTO)me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (ElementDTO)ConversionFacade.getComponentDTOIFfromDocument(this, document);
    }

    ConversionFacade.typeSafeCopy(this, generic, elementDTO);
  }

  public void update(MutableDTO mutableDTO)
  {
    this.clearNotifications();
    Document returnDoc = null;
    MutableDTO generic = null;

    Document document = ConversionFacade.getDocumentFromComponentDTO(mutableDTO, false);

    try
    {
      returnDoc = WebServiceAdapter.update(this.getSessionId(), document);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (MutableDTO)me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if (generic == null)
    {
      generic = (MutableDTO)ConversionFacade.getComponentDTOIFfromDocument(this, returnDoc);
    }

    ConversionFacade.typeSafeCopy(this, generic, mutableDTO);
  }

  public void importDomainModel(String xml, String xsd)
  {
    this.clearNotifications();
    try
    {
      WebServiceAdapter.importDomainModel(this.getSessionId(), xml, xsd);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
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
    try
    {
      WebServiceAdapter.importInstanceXML(this.getSessionId(), xml);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
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
    try
    {
      WebServiceAdapter.checkAdminScreenAccess(this.getSessionId());
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
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


    try
    {
      document = (Document) WebServiceAdapter.getAllInstances(this.getSessionId(), type, sortAttribute, ascending, pageSize, pageNumber);
    }
    catch (Throwable e)
    {
      RuntimeException rte = ClientConversionFacade.buildThrowable(e, this, true);
      if (rte instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO me = (MessageExceptionDTO)rte;
        generic = (EntityQueryDTO) me.getReturnObject();
        this.setMessagesConvertToTypeSafe(me);
      }
      else
      {
        throw rte;
      }
    }

    if(generic == null)
    {
      return (EntityQueryDTO) ConversionFacade.getQueryDTOFromDocument(this, document, true);
    }

    return (EntityQueryDTO) ConversionFacade.convertGenericQueryToTypeSafe(this, generic);
  }

  public InputStream exportExcelFile(String type, String listenerMethod, String...params)
  {
    throw new ClientException("You cannot export Excel files over web services.");
  }

  public InputStream importExcelFile(InputStream stream, String type, String listenerMethod, String...params)
  {
    throw new ClientException("You cannot import Excel files over web services.");
  }

  /**
   * @see com.runwaysdk.constants.ClientRequestIF#getTermAllChildren(java.lang.String, java.lang.Integer, java.lang.Integer)
   */
  @Override
  public List<TermAndRelDTO> getTermAllChildren(String parentId, Integer pageNum, Integer pageSize)
  {
    throw new UnsupportedOperationException("Not Implemented");
  }

}
