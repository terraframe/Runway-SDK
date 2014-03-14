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
package com.runwaysdk.facade;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.RunwayException;
import com.runwaysdk.RunwayExceptionIF;
import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.BusinessEnumeration;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.ClassLoaderException;
import com.runwaysdk.business.ClassQueryDTO;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.ComponentQueryDTO;
import com.runwaysdk.business.Element;
import com.runwaysdk.business.ElementDTO;
import com.runwaysdk.business.Entity;
import com.runwaysdk.business.EntityQueryDTO;
import com.runwaysdk.business.InvalidEnumerationName;
import com.runwaysdk.business.MethodMetaData;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.business.Relationship;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.business.RelationshipQueryDTO;
import com.runwaysdk.business.SessionComponent;
import com.runwaysdk.business.SessionDTO;
import com.runwaysdk.business.SmartException;
import com.runwaysdk.business.Struct;
import com.runwaysdk.business.StructDTO;
import com.runwaysdk.business.StructQueryDTO;
import com.runwaysdk.business.ValueQueryDTO;
import com.runwaysdk.business.ViewQueryDTO;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.json.JSONFacade;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.business.ontology.TermAndRel;
import com.runwaysdk.business.ontology.TermAndRelDTO;
import com.runwaysdk.business.ontology.TermDTO;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.business.state.StateMasterDAO;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.MdEnumerationTypes;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.constants.VaultFileInfo;
import com.runwaysdk.constants.WebFileInfo;
import com.runwaysdk.constants.XMLConstants;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.ExcelExporter;
import com.runwaysdk.dataaccess.io.ExcelImporter;
import com.runwaysdk.dataaccess.io.dataDefinition.SAXImporter;
import com.runwaysdk.dataaccess.io.instance.InstanceImporter;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.resolver.DefaultConflictResolver;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryException;
import com.runwaysdk.session.CreatePermissionException;
import com.runwaysdk.session.GrantAttributePermissionException;
import com.runwaysdk.session.GrantAttributeStatePermissionException;
import com.runwaysdk.session.GrantMethodPermissionException;
import com.runwaysdk.session.GrantStatePermissionException;
import com.runwaysdk.session.GrantTypePermissionException;
import com.runwaysdk.session.GroovyQueryExecuteException;
import com.runwaysdk.session.ImportDomainExecuteException;
import com.runwaysdk.session.InvalidLoginException;
import com.runwaysdk.session.ReadChildPermissionException;
import com.runwaysdk.session.ReadParentPermissionException;
import com.runwaysdk.session.ReadPermissionException;
import com.runwaysdk.session.ReadTypePermissionException;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.RevokeAttributePermissionException;
import com.runwaysdk.session.RevokeAttributeStatePermissionException;
import com.runwaysdk.session.RevokeMethodPermissionException;
import com.runwaysdk.session.RevokeStatePermissionException;
import com.runwaysdk.session.RevokeTypePermissionException;
import com.runwaysdk.session.RoleManagementException_ADD;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionFacade;
import com.runwaysdk.session.SessionIF;
import com.runwaysdk.system.metadata.MdDimension;
import com.runwaysdk.transport.attributes.AttributeStructDTO;
import com.runwaysdk.transport.conversion.EnumerationFacade;
import com.runwaysdk.transport.conversion.business.ClassToQueryDTO;
import com.runwaysdk.transport.conversion.business.TermToTermDTO;
import com.runwaysdk.transport.metadata.caching.ClassMdSession;
import com.runwaysdk.transport.metadata.caching.MetadataRetriever;
import com.runwaysdk.util.DTOConversionUtilInfo;
import com.runwaysdk.vault.VaultFileDAO;
import com.runwaysdk.vault.VaultFileDAOIF;
import com.runwaysdk.vault.WebFileDAO;
import com.runwaysdk.vault.WebFileDAOIF;
import com.runwaysdk.web.AdminScreenAccessException;

/**
 * This Facade class is an entry point to the core from outside client requests.
 */
public class Facade
{
  final static Logger logger = LoggerFactory.getLogger(Facade.class);
  
  /**
   * Moves the Term from one parent to another by first deleting the oldRelationship, then creating a new relationship between newParent
   * and child. All operations happen within a transaction. If the oldRelationshipId is null it is assumed to be a copy, not a move.
   * 
   * @param sessionId The id of a previously established session.
   * @param newParentId The id of the Term that the child will be appended under.
   * @param childId The id of the Term that will be either moved or copied.
   * @param oldRelationshipId The id of the relationship that currently exists between parent and child.
   * @param newRelationshipType The type string of the new relationship to create.
   */
  @Request(RequestType.SESSION)
  public static RelationshipDTO moveBusiness(String sessionId, String newParentId, String childId, String oldRelationshipId, String newRelationshipType)
  {
    Relationship rel = doMoveTerm(sessionId, newParentId, childId, oldRelationshipId, newRelationshipType);
    
    return (RelationshipDTO) FacadeUtil.populateComponentDTOIF(sessionId, rel, true);
  }
  @Transaction
  private static Relationship doMoveTerm(String sessionId, String newParentId, String childId, String oldRelationshipId, String newRelationshipType)
  {
    Term newParent = (Term) getEntity(newParentId);
    Term child = (Term) Term.get(childId);
    
    if (oldRelationshipId != null) {
      Relationship oldRel = (Relationship) getEntity(oldRelationshipId);
      
      List<TermAndRel> children = child.getDirectDescendants();
      
      child.removeTerm(oldRel.getType());
      
      // removeTerm also removes relationships with children, so we need to add those relationships back.
      Iterator<TermAndRel> it = children.iterator();
      while (it.hasNext()) {
        TermAndRel tnr = it.next();
        tnr.getTerm().copyTerm(child, tnr.getRelationshipType());
      }
    }
    
    Relationship newRel = child.copyTerm(newParent, newRelationshipType);
    
    return newRel;
  }
  
  /**
   * Returns all children of and their relationship with the given term by delegating to the term's ontology strategy.
   * 
   * @param sessionId The id of a previously established session.
   * @param parentId The id of the term to get all children.
   * @param pageNum Used to break large returns into chunks (pages), this denotes the page number in the iteration request. Set to 0 to not use pages.
   * @param pageSize Denotes the number of TermAndRel objects per page. A pageSize of 0 will be treated as infinity.
   * @return A list of TermAndRel objects of size pageSize.
   */
  @Request(RequestType.SESSION)
  public static List<TermAndRelDTO> getTermAllChildren(String sessionId, String parentId, Integer pageNum, Integer pageSize) {
    
    assertReadAccess(sessionId, getEntity(parentId)); 
    
    // Fetch the Term from id.
    Term parent = (Term) Term.get(parentId);
    
    // Ask the strategy for the term's direct descendants.
    List<TermAndRel> tnrs = parent.getDirectDescendants();
    
    // Convert the TermAndRel to a TermAndRelDTO.
    List<TermAndRelDTO> dtos = new ArrayList<TermAndRelDTO>();
    for (TermAndRel tnr : tnrs) {
      assertReadAccess(sessionId, tnr.getTerm()); // FIXME: This check may be redundant.
      
//      ComponentDTOIF dto = FacadeUtil.populateComponentDTOIF(sessionId, tnr.getTerm(), true);
      BusinessDTO termDTO = new TermToTermDTO(sessionId, tnr.getTerm(), true).populate();
      
      dtos.add(new TermAndRelDTO((TermDTO) termDTO, tnr.getRelationshipType(), tnr.getRelationshipId()));
    }
    
    return dtos;
  }
  
  private static void assertReadAccess(String sessionId, Mutable mutable) {
    boolean access = SessionFacade.checkAccess(sessionId, Operation.READ, mutable);

    if (!access)
    {
      UserDAOIF userIF = Session.getCurrentSession().getUser();

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the read permission for type [" + mutable.getType() + "]";
      throw new ReadPermissionException(errorMsg, mutable, userIF);
    }
  }
  
  /**
   * Returns the current locale for the given session.
   * 
   * @param sessionId
   * @return The locale associated with the session.
   */
  @Request(RequestType.SESSION)
  public static Locale getSessionLocale(String sessionId)
  {
    return Session.getCurrentLocale();
  }
  
  /**
   * Returns the metadata for the given types. This is useful for client/clerver-side metadata caching.
   * 
   * @param sessionId
   * @param types
   * @return ClassMdSession[] The requested metadata.
   */
  @Request(RequestType.SESSION)
  public static ClassMdSession[] getMetadata(String sessionId, String[] types) 
  {
    // Map used for preserving object identity.
    Map<String, ClassMdSession> classMdSessionMap = new HashMap<String, ClassMdSession>();
    
    ClassMdSession[] retObjs = new ClassMdSession[types.length];
    int i = 0;
    for (String type : types) 
    {
      retObjs[i] = MetadataRetriever.getMetadataForType(type, classMdSessionMap);
      ++i;
    }
    
    return retObjs;
  }
  
  /**
   * Returns a JavaScript string representing the given types. Each type is
   * converted into a definition for use with JSON objects.
   * 
   * @return A string of type definitions
   */
  @Request(RequestType.SESSION)
  public static String importTypes(String sessionId, String[] types)
  {
    return JSONFacade.importTypes(sessionId, types);
  }

  /**
   * Returns the newest last updated date of a type in a list of types
   * 
   * @return A string of type definitions
   */
  @Request(RequestType.SESSION)
  public static String getNewestUpdateDate(String sessionId, String[] types)
  {
    return JSONFacade.getNewestUpdateDate(sessionId, types);
  }

  /**
   * Creates a new Business.
   * 
   * @param sessionId
   * @param businessDTO
   * @return A SessionDTO object representing the created business.
   */
  @Request(RequestType.SESSION)
  public static SessionDTO createSessionComponent(String sessionId, SessionDTO sessionDTO)
  {
    SessionComponent sessionComponent = (SessionComponent) FacadeUtil.populateMutableAndApply(sessionId, sessionDTO);

    return (SessionDTO) FacadeUtil.populateComponentDTOIF(sessionId, sessionComponent, true);
  }

  /**
   * Creates a new Business.
   * 
   * @param sessionId
   * @param businessDTO
   * @return A BusinessDTO object representing the created business.
   */
  @Request(RequestType.SESSION)
  public static BusinessDTO createBusiness(String sessionId, BusinessDTO businessDTO)
  {
    Business business = (Business) FacadeUtil.populateMutableAndApply(sessionId, businessDTO);
    
    return (BusinessDTO) FacadeUtil.populateComponentDTOIF(sessionId, business, true);
  }

  /**
   * Checks if the user with the given session id can access the admin screen.
   * 
   * @param sessionId
   * @return
   */
  @Request(RequestType.SESSION)
  public static void checkAdminScreenAccess(String sessionId)
  {
    RoleDAOIF adminScreenRole = RoleDAO.findRole(RoleDAOIF.ADMIN_SCREEN_ROLE);
    UserDAOIF user = SessionFacade.getUser(sessionId);

    for (RoleDAOIF role : user.authorizedRoles())
    {
      if (role.getId().equals(adminScreenRole.getId()))
      {
        return;
      }
    }

    String error = "The user [" + user.getUsername() + "] cannot access the admin screen because " + "it does not belong to the [" + RoleDAOIF.ADMIN_SCREEN_ROLE + "] role.";
    throw new AdminScreenAccessException(error, user);
  }

  /**
   * Creates a new RelationshipDTO
   * 
   * @param sessionId
   * @param relationshipDTO
   * @return
   */
  @Request(RequestType.SESSION)
  public static RelationshipDTO createRelationship(String sessionId, RelationshipDTO relationshipDTO)
  {
    Relationship relationship = (Relationship) FacadeUtil.populateMutableAndApply(sessionId, relationshipDTO);

    return (RelationshipDTO) FacadeUtil.populateComponentDTOIF(sessionId, relationship, true);
  }

  /**
   * Creates a new StructDTO
   * 
   * @param sessionId
   * @param structDTO
   * @return
   */
  @Request(RequestType.SESSION)
  public static StructDTO createStruct(String sessionId, StructDTO structDTO)
  {
    Struct businessStruct = (Struct) FacadeUtil.populateMutableAndApply(sessionId, structDTO);

    return (StructDTO) FacadeUtil.populateComponentDTOIF(sessionId, businessStruct, true);
  }

  /**
   * Updates an existing component.
   * 
   * @param sessionId
   * @param mutableDTO
   * @return EntityDTO object representing the updated entity.
   */
  @Request(RequestType.SESSION)
  public static MutableDTO update(String sessionId, MutableDTO mutableDTO)
  {
    Mutable mutable = FacadeUtil.populateMutableAndApply(sessionId, mutableDTO);

    return (MutableDTO) FacadeUtil.populateComponentDTOIF(sessionId, mutable, true);
  }

  /**
   * Deletes an entity.
   * 
   * @param sessionId
   * @param id
   * @param definesType
   */
  @Request(RequestType.SESSION)
  public static void delete(String sessionId, String id)
  {
    SessionIF session = Session.getCurrentSession();

    // Check if the object has been stored in the user's session
    Mutable mutable = session.get(id);

    if (mutable == null)
    {
      // instantiate a type-unsafe EntityDTO if the type is reserved.
      // This should throw a DataNotFound exception if the id represents a
      // Transient and not an Entity.

      mutable = BusinessFacade.getEntity(id);
    }

    mutable.delete();
  }

  /**
   * Returns a new instance of a specified type.
   * 
   * @param sessionId
   * @param type
   * @return BusinessDTO object representing the new instance.
   */
  @Request(RequestType.SESSION)
  public static BusinessDTO newBusiness(String sessionId, String type)
  {
    Business business = BusinessFacade.newBusiness(type);

    return (BusinessDTO) FacadeUtil.populateComponentDTOIF(sessionId, business, true);
  }

  /**
   * Returns a new generic MutableDTO.
   * 
   * @param sessionId
   * @param type
   * @return
   */
  @Request(RequestType.SESSION)
  public static MutableDTO newMutable(String sessionId, String type)
  {
    Mutable mutable = BusinessFacade.newMutable(type);

    return (MutableDTO) FacadeUtil.populateComponentDTOIF(sessionId, mutable, true);
  }

  /**
   * Returns the instance associated with the specified id and type.
   * 
   * @param sessionId
   * @param id
   * @param definesType
   * @return EntityDTO
   */
  @Request(RequestType.SESSION)
  public static MutableDTO get(String sessionId, String id)
  {
    SessionIF session = Session.getCurrentSession();

    // Check if the object has been stored in the user's session
    Mutable mutable = session.get(id);

    if (mutable == null)
    {
      // instantiate a type-unsafe Entity if the type is reserved.
      // This should throw a DataNotFound exception if the id represents a
      // Transient and not an Entity.
      mutable = getEntity(id);
    }

    boolean access = SessionFacade.checkAccess(sessionId, Operation.READ, mutable);

    if (!access)
    {
      UserDAOIF userIF = session.getUser();

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the read permission for type [" + mutable.getType() + "]";
      throw new ReadPermissionException(errorMsg, mutable, userIF);
    }

    return (MutableDTO) FacadeUtil.populateComponentDTOIF(sessionId, mutable, true);
  }

  @Request(RequestType.SESSION)
  public static EntityQueryDTO getAllInstances(String sessionId, String type, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    boolean access = SessionFacade.checkTypeAccess(sessionId, Operation.READ, type);

    if (!access)
    {
      UserDAOIF userIF = Session.getCurrentSession().getUser();

      MdEntityDAOIF mdEntity = MdEntityDAO.getMdEntityDAO(type);

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the read permission for type [" + type + "]";
      throw new ReadTypePermissionException(errorMsg, mdEntity, userIF);
    }

    try
    {
      Class<?> c = LoaderDecorator.load(type);
      Method getAllInstances = c.getMethod("getAllInstances", String.class, Boolean.class, Integer.class, Integer.class);
      Object query = getAllInstances.invoke(null, sortAttribute, ascending, pageSize, pageNumber);

      return (EntityQueryDTO) FacadeUtil.convertTypeToDTO(sessionId, query);
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  /**
   * Adds a child to a parent for a given relationship.
   * 
   * @param sessionId
   * @param parentId
   * @param childId
   * @param relationshipDTO
   * @return RelationshipDTO
   */
  @Request(RequestType.SESSION)
  public static RelationshipDTO addChild(String sessionId, String parentId, String childId, String relationshipType)
  {
    // get the parent
    Business parent = (Business) getEntity(parentId);

    // create the relationship (the returned relationship is type-unsafe)
    Relationship relationship = parent.addChild(childId, relationshipType);

    return (RelationshipDTO) FacadeUtil.populateComponentDTOIF(sessionId, relationship, true);
  }

  /**
   * Deletes a child from a parent in a relationship.
   * 
   * @param sessionId
   * @param relationshipId
   */
  @Request(RequestType.SESSION)
  public static void deleteChild(String sessionId, String relationshipId)
  {
    Relationship relationship = (Relationship) getEntity(relationshipId);
    relationship.getParent().removeChild(relationship);
  }

  /**
   * Deletes a parent from a child in a relationship.
   * 
   * @param sessionId
   * @param relationshipId
   */
  @Request(RequestType.SESSION)
  public static void deleteParent(String sessionId, String relationshipId)
  {
    Relationship relationship = (Relationship) getEntity(relationshipId);
    relationship.getChild().removeParent(relationship);
  }

  /**
   * Adds a parent to a child for a given relationship.
   * 
   * @param sessionId
   * @param parentId
   * @param childId
   * @param relationshipDTO
   * @return RelationshipDTO
   */
  @Request(RequestType.SESSION)
  public static RelationshipDTO addParent(String sessionId, String parentId, String childId, String relationshipType)
  {
    Business child = (Business) getEntity(childId);

    // create the relationship (the returned relationship is type-unsafe)
    Relationship relationship = child.addParent(parentId, relationshipType);

    return (RelationshipDTO) FacadeUtil.populateComponentDTOIF(sessionId, relationship, true);
  }

  /**
   * Attempts to log a user in with the specified username and password.
   * 
   * @param username
   * @param password
   * @param locales
   * @return id of the new session.
   */
  @Request
  public static String login(String username, String password, Locale[] locales)
  {
    try
    {
      return SessionFacade.logIn(username, password, locales);
    }
    catch (DataNotFoundException e)
    {
      throw new InvalidLoginException(e);
    }
  }

  /**
   * Attempts to log a user in with the specified username and password for the
   * given dimension.
   * 
   * @param username
   * @param password
   * @param dimensionKey
   * @param locales
   * @return id of the new session.
   */
  @Request
  public static String login(String username, String password, String dimensionKey, Locale[] locales)
  {
    try
    {
      return SessionFacade.logIn(username, password, dimensionKey, locales);
    }
    catch (DataNotFoundException e)
    {
      throw new InvalidLoginException(e);
    }
  }

  /**
   * Sets the dimension of an existing {@link Session}.
   * 
   * @param sessionId
   *          The id of the {@link Session}.
   * @param dimensionKey
   *          key of a {@link MdDimension}.
   */
  @Request(RequestType.SESSION)
  public static void setDimension(String sessionId, String dimensionKey)
  {
    SessionFacade.setDimension(dimensionKey, sessionId);
  }

  /**
   * Changes the user of the given session.
   * 
   * @param sessionId
   * @param username
   * @param password
   */
  @Request
  public static void changeLogin(String sessionId, String username, String password)
  {
    try
    {
      SessionFacade.changeLogin(sessionId, username, password);
    }
    catch (DataNotFoundException e)
    {
      throw new InvalidLoginException(e);
    }
  }

  /**
   * Returns a DTO representing the object of the user who is logged into the
   * session with the given session id.
   * 
   * @param sessionId
   * @return DTO representing the object of the user who is logged into the
   *         session with the given session id.
   */
  @Request(RequestType.SESSION)
  public static BusinessDTO getSessionUser(String sessionId)
  {
    UserDAOIF userDAO = SessionFacade.getUser(sessionId);

    Business userBusiness = BusinessFacade.get(userDAO);

    boolean access = SessionFacade.checkAccess(sessionId, Operation.READ, userBusiness);

    if (!access)
    {
      String errorMsg = "User [" + userDAO.getSingleActorName() + "] does not have the read permission for type [" + userDAO.getType() + "]";
      throw new ReadPermissionException(errorMsg, userBusiness, userDAO);
    }

    return (BusinessDTO) FacadeUtil.populateComponentDTOIF(sessionId, userBusiness, true);
  }

  /**
   * Returns a Map representing all of the roles assigned to the given user,
   * either implicitly or explicitly. The key is the role name. The value is the
   * role display label.
   * 
   * @param sessionId
   * @return Map representing all of the roles assigned to the given user,
   *         either implicitly or explicitly.
   */
  @Request(RequestType.SESSION)
  public static Map<String, String> getSessionUserRoles(String sessionId)
  {
    return SessionFacade.getSessionForRequest(sessionId).getUserRoles();
  }

  /**
   * Creates a session with the public user.
   * 
   * @param locales
   *          locale of the user
   * @return id of the new session.
   */
  @Request
  public static String loginAnonymous(Locale[] locales)
  {
    try
    {
      return SessionFacade.logIn(UserInfo.PUBLIC_USER_NAME, ServerConstants.PUBLIC_USER_PASSWORD, locales);
    }
    catch (DataNotFoundException e)
    {
      throw new InvalidLoginException(e);
    }
  }

  /**
   * Creates a session with the public user for the given dimension.
   * 
   * @param dimensionKey
   * @param locales
   *          locale of the user
   * @return id of the new session.
   */
  @Request
  public static String loginAnonymous(String dimensionKey, Locale[] locales)
  {
    try
    {
      return SessionFacade.logIn(UserInfo.PUBLIC_USER_NAME, ServerConstants.PUBLIC_USER_PASSWORD, dimensionKey, locales);
    }
    catch (DataNotFoundException e)
    {
      throw new InvalidLoginException(e);
    }
  }

  /**
   * Logs a user out.
   * 
   * @param sessionId
   */
  @Request
  public static void logout(String sessionId)
  {
    SessionFacade.closeSession(sessionId);
  }

  /**
   * Invokes a method defined by an MdMethod on the given MutableDTO in the
   * BusinessLayer
   * 
   * @param sessionId
   *          SessionId of the session invoking the method
   * @param metadata
   *          Metadata containing information about the method.
   * @param mutableDTO
   *          MutableDTO on which to invoke the method, if null then it is
   *          assumed that the method is static.
   * @param parameters
   *          Parameters to invoke the method
   * @return Return type of the invoked method or null if there is no object
   *         returned
   * 
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   * @throws NoSuchMethodException
   */
  @Request(RequestType.SESSION)
  public static Object invokeMethod(String sessionId, MethodMetaData metadata, MutableDTO mutableDTO, Object[] parameters)
  {
    validateMetaData(metadata, mutableDTO);

    Mutable mutable = null;
    MutableDTO calledDTO = null;
    Class<?> mutableClass = LoaderDecorator.load(metadata.getClassName());

    try
    {
      // Update the values of the mutable to those of the DTO, but do not
      // apply the changes, that is left up to the user.
      if (mutableDTO != null)
      {
        mutable = FacadeUtil.populateComponent(sessionId, mutableDTO);
      }

      // Get the Classes of the parameters of the method to invoke
      Class<?>[] methodClasses = FacadeUtil.loadClasses(metadata.getDeclaredTypes());

      // Ensure that the Classes of the actual parameters are already
      // loaded before trying to convert the parameters. This is no
      // longer needed because the actual types are loaded in the
      // validateMetadata method.
      // FacadeUtil.loadClasses(metadata.getActualTypes());

      // Convert the parameters into their business layer representation
      Object[] businessParameters = FacadeUtil.convertDTOstoTypes(sessionId, metadata.getActualTypes(), parameters);

      // Invoke the method
      Method method = mutableClass.getMethod(metadata.getMethodName(), (Class[]) methodClasses);
      Object output = method.invoke(mutable, businessParameters);
      String outputType = null;

      // Convert the return object to its DTO representation
      if (output != null)
      {
        outputType = FacadeUtil.getDTOType(output);
        output = FacadeUtil.convertTypeToDTO(sessionId, output);
      }

      // Convert the mutable back into its DTO representation, with its
      // updated values, that might have changed when the method was invoked.
      if (mutableDTO != null)
      {
        calledDTO = (MutableDTO) FacadeUtil.populateComponentDTOIF(sessionId, mutable, true);

        // The modified flag MUST be persisted for each attribute on the
        // returned DTO
        boolean isModified = false;
        for (String name : calledDTO.getAttributeNames())
        {
          boolean modified = mutable.isModified(name);
          calledDTO.setModified(name, modified);

          // we only want to mark the MutableDTO as modified if a non-struct
          // attribute was modified
          // this is because structs are always marked as modified
          if (modified && ! ( ComponentDTOFacade.getAttributeDTO(mutableDTO, name) instanceof AttributeStructDTO ))
          {
            isModified = true;
          }
        }

        calledDTO.setModified(isModified);
      }

      Object[] ret = new Object[3];
      ret[DTOConversionUtilInfo.RETURN_OBJECT] = output;
      ret[DTOConversionUtilInfo.RETURN_DTO_TYPE] = outputType;
      ret[DTOConversionUtilInfo.CALLED_OBJECT] = calledDTO;

      return ret;
    }
    catch (SecurityException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (NoSuchMethodException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (IllegalArgumentException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (IllegalAccessException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (InvocationTargetException e)
    {
      Throwable targetException = e.getTargetException();
      if (targetException instanceof RunwayExceptionIF)
      {
        throw (RuntimeException) targetException;
      }
      else
      {
        throw new ProgrammingErrorException(e);
      }
    }
  }

  /**
   * Ensure that the metadata to invoke the method is valid. Additionally if
   * mutableDTO is not null, ensure that that the mutableDTO type is assignable
   * from the class type defined in the metadata.
   * 
   * @param metadata
   * @param mutableDTO
   */
  private static void validateMetaData(MethodMetaData metadata, MutableDTO mutableDTO)
  {
    // Ensure that the MutableDTO is assignable from the metadata class
    if (mutableDTO != null)
    {
      validateAssignment(metadata.getClassName(), mutableDTO.getType());
    }

    String[] declaredTypes = metadata.getDeclaredTypes();
    String[] actualTypes = metadata.getActualTypes();

    for (int i = 0; i < declaredTypes.length; i++)
    {
      validateAssignment(declaredTypes[i], actualTypes[i]);
    }
  }

  /**
   * Ensure that the actual type is a valid assignment of the declared type.
   * 
   * @param declaredType
   * @param actualType
   */
  private static void validateAssignment(String declaredType, String actualType)
  {
    if (actualType != null)
    {
      Class<?> declaredClass = LoaderDecorator.load(declaredType);
      Class<?> actualClass = LoaderDecorator.load(actualType);

      if (!declaredClass.isAssignableFrom(actualClass))
      {
        String msg = "The actual type [" + actualType + "] is not a valid form of the declared type [" + declaredType + "]";
        throw new ProgrammingErrorException(msg);
      }
    }
  }

  /**
   * Adds the user with the given userId to the role with the given role name.
   * 
   * @param sessionId
   * @param role
   * @param userId
   */
  @Request(RequestType.SESSION)
  public static void assignMember(String sessionId, String userId, String... roles)
  {
    SessionIF sessionIF = Session.getCurrentSession();

    if (sessionIF.userHasRole(RoleDAOIF.ROLE_ADMIN_ROLE))
    {
      assignMemberTransaction(sessionId, userId, roles);
    }
    else
    {
      String errMsg = "Role [" + RoleDAOIF.ROLE_ADMIN_ROLE + "] required to add a user to roles.";
      throw new RoleManagementException_ADD(errMsg);
    }
  }

  /**
   * Adds the user with the given userId to the role with the given role name.
   * 
   * @param sessionId
   * @param role
   * @param userId
   */
  @Transaction
  private static void assignMemberTransaction(String sessionId, String userId, String... roles)
  {
    UserDAOIF user = UserDAO.get(userId);
    for (String roleName : roles)
    {
      RoleDAO role = RoleDAO.findRole(roleName).getBusinessDAO();
      role.assignMember(user);
    }
  }

  /**
   * Removes the roles with the given names from the user with the given userId.
   * 
   * @param sessionId
   * @param role
   * @param userId
   */
  @Request(RequestType.SESSION)
  public static void removeMember(String sessionId, String userId, String... roles)
  {
    SessionIF sessionIF = Session.getCurrentSession();

    if (sessionIF.userHasRole(RoleDAOIF.ROLE_ADMIN_ROLE))
    {
      removeMemberTransaction(sessionId, userId, roles);
    }
    else
    {
      String errMsg = "Role [" + RoleDAOIF.ROLE_ADMIN_ROLE + "] required to add a user to roles.";
      throw new RoleManagementException_ADD(errMsg);
    }
  }

  /**
   * Removes the roles with the given names from the user with the given userId.
   * 
   * @param sessionId
   * @param role
   * @param userId
   */
  @Transaction
  private static void removeMemberTransaction(String sessionId, String userId, String... roles)
  {
    UserDAOIF user = UserDAO.get(userId);
    for (String roleName : roles)
    {
      RoleDAO role = RoleDAO.findRole(roleName).getBusinessDAO();
      role.deassignMember(user);
    }
  }

  /**
   * Converts the given operation enumeration names into ids.
   * 
   * @param operationNames
   * @return operation ids.
   */
  private static String[] convertOperationEnumNamesToIds(String... operationNames)
  {
    Class<?> c = null;
    try
    {
      c = LoaderDecorator.load(MdEnumerationTypes.ALL_OPERATIONS.getType());
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }

    String[] operationIds = new String[operationNames.length];
    BusinessEnumeration[] busEnumArray = new BusinessEnumeration[operationNames.length];

    for (int i = 0; i < operationNames.length; i++)
    {
      try
      {
        busEnumArray[i] = (BusinessEnumeration) c.getMethod("valueOf", String.class).invoke(null, operationNames[i]);
        operationIds[i] = busEnumArray[i].getId();
      }
      catch (InvocationTargetException ite)
      {
        Throwable cause = ite.getCause();
        if (cause != null)
        {
          if (cause instanceof IllegalArgumentException)
          {
            MdEnumerationDAOIF mdEnumerationIF = MdEnumerationDAO.getMdEnumerationDAO(MdEnumerationTypes.ALL_OPERATIONS.getType());

            String errMsg = "The enummeration name [" + operationNames[i] + "] is not valid for enumeration [" + MdEnumerationTypes.ALL_OPERATIONS.getType() + "].";
            throw new InvalidEnumerationName(errMsg, operationNames[i], mdEnumerationIF);
          }
        }
        throw new ProgrammingErrorException(ite);
      }
      catch (Exception e)
      {
        throw new ProgrammingErrorException(e);
      }
    }

    return operationIds;
  }

  /**
   * Grants state permission.
   * 
   * @param sessionId
   * @param actorId
   * @param stateId
   * @param operationNames
   */
  @Request(RequestType.SESSION)
  public static void grantStatePermission(String sessionId, String actorId, String stateId, String... operationNames)
  {
    boolean access = SessionFacade.checkAccess(sessionId, Operation.GRANT, stateId);

    if (!access)
    {
      SessionIF session = Session.getCurrentSession();

      UserDAOIF userIF = session.getUser();

      StateMasterDAOIF stateMasterIF = StateMasterDAO.get(stateId);

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have grant permission on state [" + stateMasterIF.getName() + "].";
      throw new GrantStatePermissionException(errorMsg, stateMasterIF, userIF);
    }

    Grant.grantStatePermission(sessionId, actorId, stateId, convertOperationEnumNamesToIds(operationNames));
  }

  /**
   * Grants attribute permission.
   * 
   * @param sessionId
   * @param actorId
   * @param mdAttributeId
   * @param operationNames
   */
  @Request(RequestType.SESSION)
  public static void grantAttributePermission(String sessionId, String actorId, String mdAttributeId, String... operationNames)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = MdAttributeConcreteDAO.get(mdAttributeId);

    boolean access = SessionFacade.checkAttributeAccess(sessionId, Operation.GRANT, mdAttributeIF);

    if (!access)
    {
      SessionIF session = Session.getCurrentSession();

      UserDAOIF userIF = session.getUser();

      MdClassDAOIF mdClassIF = mdAttributeIF.definedByClass();

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have grant permission on attribute [" + mdAttributeIF.definesAttribute() + "] " + " on class [" + mdClassIF.definesType() + "]";
      throw new GrantAttributePermissionException(errorMsg, mdClassIF, mdAttributeIF, userIF);
    }

    Grant.grantAttributePermission(sessionId, actorId, mdAttributeId, convertOperationEnumNamesToIds(operationNames));
  }

  /**
   * Grants attribute permission on a state.
   * 
   * @param sessionId
   * @param actorId
   * @param mdAttributeId
   * @param stateId
   * @param operationNames
   */
  @Request(RequestType.SESSION)
  // Heads up: ask Smethie about granting permissions based on domain
  public static void grantAttributeStatePermission(String sessionId, String actorId, String mdAttributeId, String stateId, String... operationNames)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = MdAttributeConcreteDAO.get(mdAttributeId);

    boolean access = SessionFacade.checkAttributeAccess(sessionId, Operation.GRANT, stateId, mdAttributeIF);

    if (!access)
    {
      SessionIF session = Session.getCurrentSession();

      UserDAOIF userIF = session.getUser();

      MdClassDAOIF mdClassIF = mdAttributeIF.definedByClass();

      StateMasterDAOIF stateMasterIF = StateMasterDAO.get(stateId);

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have grant permission on attribute [" + mdAttributeIF.definesAttribute() + "] " + " on class [" + mdClassIF.definesType() + "] in state [" + stateMasterIF.getName() + "]";
      throw new GrantAttributeStatePermissionException(errorMsg, mdClassIF, mdAttributeIF, stateMasterIF, userIF);
    }

    Grant.grantAttributeStatePermission(sessionId, actorId, mdAttributeId, stateId, convertOperationEnumNamesToIds(operationNames));
  }

  /**
   * Grants permission to a role or user to execute an operation on a type.
   * 
   * @pre: get(mdTypeId)instanceof MdType
   * @param session
   *          id.
   * @param actorId
   *          of the actor to receive the given operation permissions.
   * @param mdTypeId
   *          The id of the type.
   * @param operationNames
   *          id of operation to grant.
   */
  @Request(RequestType.SESSION)
  public static void grantTypePermission(String sessionId, String actorId, String mdTypeId, String... operationNames)
  {
    MdTypeDAOIF mdTypeIF = MdTypeDAO.get(mdTypeId);

    boolean access = SessionFacade.checkTypeAccess(sessionId, Operation.GRANT, mdTypeIF.definesType());

    if (!access)
    {
      SessionIF session = Session.getCurrentSession();

      UserDAOIF userIF = session.getUser();

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have grant permission on type [" + mdTypeIF.definesType() + "]";
      throw new GrantTypePermissionException(errorMsg, mdTypeIF, userIF);
    }

    Grant.grantMetaDataPermission(sessionId, actorId, mdTypeId, convertOperationEnumNamesToIds(operationNames));
  }

  /**
   * Grants permission to a role or user to execute an operation on a method.
   * 
   * @pre: get(mdMethodId)instanceof MdMethod
   * @param session
   *          id.
   * @param actorId
   *          of the actor to receive the given operation permissions.
   * @param mdMethodId
   *          The id of the type.
   * @param operationNames
   *          id of operation to grant.
   */
  @Request(RequestType.SESSION)
  public static void grantMethodPermission(String sessionId, String actorId, String mdMethodId, String... operationNames)
  {
    MdMethodDAOIF mdMethodIF = MdMethodDAO.get(mdMethodId);

    boolean access = SessionFacade.checkMethodAccess(sessionId, Operation.GRANT, mdMethodIF);

    if (!access)
    {
      SessionIF session = Session.getCurrentSession();

      UserDAOIF userIF = session.getUser();

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have grant permission on type [" + mdMethodIF.getName() + "]";
      throw new GrantMethodPermissionException(errorMsg, mdMethodIF.getEnclosingMdTypeDAO(), mdMethodIF, userIF);
    }

    Grant.grantMetaDataPermission(sessionId, actorId, mdMethodId, convertOperationEnumNamesToIds(operationNames));
  }

  /**
   * Removes an operation permission from a user or role on a type. Does nothing
   * if the operation is not in the permission set.
   * 
   * @pre: get(mdTypeId)instanceof MdType
   * @param sessionId
   *          .
   * @param actorId
   *          of the actor to revoke the given operation permissions.
   * @param mdTypeId
   *          The id of the type.
   * @param operationNames
   *          ids of operation to revoke.
   */
  @Request(RequestType.SESSION)
  public static void revokeTypePermission(String sessionId, String actorId, String mdTypeId, String... operationNames)
  {
    MdTypeDAOIF mdTypeIF = MdTypeDAO.get(mdTypeId);

    boolean access = SessionFacade.checkTypeAccess(sessionId, Operation.GRANT, mdTypeIF.definesType());

    if (!access)
    {
      SessionIF session = Session.getCurrentSession();

      UserDAOIF userIF = session.getUser();

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have grant permission on type [" + mdTypeIF.definesType() + "]";
      throw new RevokeTypePermissionException(errorMsg, mdTypeIF, userIF);
    }

    Grant.revokeMetaDataPermission(sessionId, actorId, mdTypeId, convertOperationEnumNamesToIds(operationNames));
  }

  /**
   * Removes an operation permission from a user or role on a type. Does nothing
   * if the operation is not in the permission set.
   * 
   * @pre: get(mdMethodId)instanceof MdType
   * @param sessionId
   *          .
   * @param actorId
   *          of the actor to revoke the given operation permissions.
   * @param mdTypeId
   *          The id of the type.
   * @param operationNames
   *          ids of operation to revoke.
   */
  @Request(RequestType.SESSION)
  public static void revokeMethodPermission(String sessionId, String actorId, String mdMethodId, String... operationNames)
  {
    MdMethodDAOIF mdMethodIF = MdMethodDAO.get(mdMethodId);

    boolean access = SessionFacade.checkMethodAccess(sessionId, Operation.GRANT, mdMethodIF);

    if (!access)
    {
      SessionIF session = Session.getCurrentSession();

      UserDAOIF userIF = session.getUser();

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have grant permission on type [" + mdMethodIF.getName() + "]";
      throw new RevokeMethodPermissionException(errorMsg, mdMethodIF.getEnclosingMdTypeDAO(), mdMethodIF, userIF);
    }

    Grant.revokeMetaDataPermission(sessionId, actorId, mdMethodId, convertOperationEnumNamesToIds(operationNames));
  }

  /**
   * Revokes state permission.
   * 
   * @param sessionId
   * @param actorId
   * @param stateId
   * @param operationNames
   */
  @Request(RequestType.SESSION)
  public static void revokeStatePermission(String sessionId, String actorId, String stateId, String... operationNames)
  {
    boolean access = SessionFacade.checkAccess(sessionId, Operation.GRANT, stateId);

    if (!access)
    {
      SessionIF session = Session.getCurrentSession();

      UserDAOIF userIF = session.getUser();

      StateMasterDAOIF stateMasterIF = StateMasterDAO.get(stateId);

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have grant permission on state [" + stateMasterIF.getName() + "].";
      throw new RevokeStatePermissionException(errorMsg, stateMasterIF, userIF);
    }

    Grant.revokeStatePermission(sessionId, actorId, stateId, convertOperationEnumNamesToIds(operationNames));
  }

  /**
   * Revokes attribute permission.
   * 
   * @param sessionId
   * @param actorId
   * @param mdAttributeId
   * @param operationNames
   */
  @Request(RequestType.SESSION)
  public static void revokeAttributePermission(String sessionId, String actorId, String mdAttributeId, String... operationNames)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = MdAttributeConcreteDAO.get(mdAttributeId);

    boolean access = SessionFacade.checkAttributeAccess(sessionId, Operation.GRANT, mdAttributeIF);

    if (!access)
    {
      SessionIF session = Session.getCurrentSession();

      UserDAOIF userIF = session.getUser();

      MdClassDAOIF mdClassIF = mdAttributeIF.definedByClass();

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have grant permission on attribute [" + mdAttributeIF.definesAttribute() + "] " + " on class [" + mdClassIF.definesType() + "]";
      throw new RevokeAttributePermissionException(errorMsg, mdClassIF, mdAttributeIF, userIF);
    }

    Grant.revokeAttributePermission(sessionId, actorId, mdAttributeId, convertOperationEnumNamesToIds(operationNames));
  }

  /**
   * Revokes attribute permission on a state.
   * 
   * @param sessionId
   * @param actorId
   * @param mdAttributeId
   * @param stateId
   * @param operationNames
   */
  @Request(RequestType.SESSION)
  public static void revokeAttributeStatePermission(String sessionId, String actorId, String mdAttributeId, String stateId, String... operationNames)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = MdAttributeConcreteDAO.get(mdAttributeId);

    boolean access = SessionFacade.checkAttributeAccess(sessionId, Operation.GRANT, stateId, mdAttributeIF);

    if (!access)
    {
      SessionIF session = Session.getCurrentSession();

      UserDAOIF userIF = session.getUser();

      MdClassDAOIF mdClassIF = mdAttributeIF.definedByClass();

      StateMasterDAOIF stateMasterIF = StateMasterDAO.get(stateId);

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have grant permission on attribute [" + mdAttributeIF.definesAttribute() + "] " + " on class [" + mdClassIF.definesType() + "] in state [" + stateMasterIF.getName() + "]";
      throw new RevokeAttributeStatePermissionException(errorMsg, mdClassIF, mdAttributeIF, stateMasterIF, userIF);
    }

    Grant.revokeAttributeStatePermission(sessionId, actorId, mdAttributeId, stateId, convertOperationEnumNamesToIds(operationNames));
  }

  /**
   * Promotes the business object to the state with the given name.
   * 
   * @param sessionid
   * @param objectId
   * @param transitionName
   */
  @Request(RequestType.SESSION)
  public static BusinessDTO promoteObject(String sessionId, BusinessDTO businessDTO, String transitionName)
  {
    // update the Business object with the most recent values on the BusinessDTO
    Business business = (Business) FacadeUtil.populateComponent(sessionId, businessDTO);

    // check permission for promoting
    SessionFacade.checkPromoteAccess(sessionId, business, transitionName);

    try
    {
      Class<?> collectionClass = LoaderDecorator.load(business.getType());
      String methodName = CommonGenerationUtil.lowerFirstCharacter(transitionName);
      collectionClass.getMethod(methodName).invoke(business);
    }
    catch (IllegalArgumentException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (SecurityException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (IllegalAccessException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (InvocationTargetException e)
    {
      if (e.getTargetException() instanceof RunwayException)
      {
        RunwayException fwEx = (RunwayException) e.getTargetException();
        throw fwEx;
      }
      else if (e.getTargetException() instanceof SmartException)
      {
        throw (SmartException) e.getTargetException();
      }
      else
      {
        throw new ProgrammingErrorException(e);
      }
    }
    catch (NoSuchMethodException e)
    {
      if (GenerationUtil.isReservedType(MdTypeDAO.getMdTypeDAO(business.getType())))
      {
        business.promote(transitionName);
      }
      else
      {
        throw new ProgrammingErrorException(e);
      }
    }

    return (BusinessDTO) FacadeUtil.populateComponentDTOIF(sessionId, business, true);
  }

  /**
   * Locks a business element with the specified id.
   * 
   * @param sessionId
   * @param id
   *          The id of the business element to lock.
   * @return An EntityDTO representing the locked Element.
   */
  @Request(RequestType.SESSION)
  public static ElementDTO lock(String sessionId, String id)
  {
    // lock the business object
    Element element = (Element) getEntity(id);

    element.lock();

    return (ElementDTO) FacadeUtil.populateComponentDTOIF(sessionId, element, true);
  }

  /**
   * Unlocks a business entity with the specified id.
   * 
   * @param sessionId
   * @param id
   *          The id of the business entity to unlock.
   * @return An EntityDTO representing the unlocked Element.
   */
  @Request(RequestType.SESSION)
  public static ElementDTO unlock(String sessionId, String id)
  {
    // unlock the business object
    Element element = (Element) getEntity(id);

    element.unlock();

    return (ElementDTO) FacadeUtil.populateComponentDTOIF(sessionId, element, true);
  }

  @Request(RequestType.SESSION)
  public static void deleteChildren(String sessionId, String parentId, String relationshipType)
  {
    // get all children of the business object and delete them
    _deleteChildren(sessionId, parentId, relationshipType);
  }

  @Transaction
  private static void _deleteChildren(String sessionId, String parentId, String relationshipType)
  {
    // get all children of the business object and delete them
    Business business = (Business) getEntity(parentId);

    OIterator<? extends Relationship> childrenRels = business.getChildRelationships(relationshipType);

    for (Relationship childRel : childrenRels)
    {
      business.removeChild(childRel);
    }
  }

  @Request(RequestType.SESSION)
  public static void deleteParents(String sessionId, String childId, String relationshipType)
  {
    _deleteParents(sessionId, childId, relationshipType);
  }

  @Transaction
  private static void _deleteParents(String sessionId, String childId, String relationshipType)
  {
    // get all children of the business object and delete them
    Business business = (Business) getEntity(childId);

    OIterator<? extends Relationship> parentRels = business.getParentRelationships(relationshipType);
    for (Relationship parentRel : parentRels)
    {
      business.removeParent(parentRel);
    }
  }

  /**
   * Returns a list of RelationshipDTO objects (with parent information) of the
   * specified relationhip type for the given BusinessDTO object.
   * 
   * @param sessionId
   * @param id
   * @param relationshipType
   * @return
   */
  @Request(RequestType.SESSION)
  public static List<RelationshipDTO> getParentRelationships(String sessionId, String id, String relationshipType)
  {
    // Get all the Relationships associated with the parents.
    Business business = (Business) getEntity(id);

    MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(relationshipType);

    boolean access = SessionFacade.checkRelationshipAccess(sessionId, Operation.READ_PARENT, business, mdRelationshipIF.getId());

    if (!access)
    {
      UserDAOIF userIF = Session.getCurrentSession().getUser();

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have permission to read parents of type [" + mdRelationshipIF.definesType() + "] for parent object [" + id + "]";
      throw new ReadParentPermissionException(errorMsg, business, mdRelationshipIF, userIF);
    }

    // Convert each Relationship into a RelationshipDTO
    List<? extends Relationship> relationshipObjects = business.getParentRelationships(relationshipType).getAll();
    return FacadeUtil.buildRelationshipDTOListFromRelationships(sessionId, relationshipObjects);
  }

  @Request(RequestType.SESSION)
  public static List<BusinessDTO> getParents(String sessionId, String id, String relationshipType)
  {
    // Get all the children associated with the parent.
    Business business = (Business) getEntity(id);

    MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(relationshipType);

    boolean access = SessionFacade.checkRelationshipAccess(sessionId, Operation.READ_PARENT, business, mdRelationshipIF.getId());

    if (!access)
    {
      UserDAOIF userIF = Session.getCurrentSession().getUser();

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have permission to read parents of type [" + mdRelationshipIF.definesType() + "] for parent object [" + id + "]";
      throw new ReadParentPermissionException(errorMsg, business, mdRelationshipIF, userIF);
    }

    // convert the set to a list (Sets aren't serializable)
    List<? extends Business> businessList = business.getParents(relationshipType).getAll();
    return FacadeUtil.buildBusinessDTOListFromBusinesses(sessionId, businessList);
  }

  @Request(RequestType.SESSION)
  public static List<BusinessDTO> getChildren(String sessionId, String id, String relationshipType)
  {
    // Get all the children associated with the parent.
    Business business = (Business) getEntity(id);

    MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(relationshipType);

    boolean access = SessionFacade.checkRelationshipAccess(sessionId, Operation.READ_CHILD, business, mdRelationshipIF.getId());

    if (!access)
    {
      UserDAOIF userIF = Session.getCurrentSession().getUser();

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have permission to read children of type [" + mdRelationshipIF.definesType() + "] for parent object [" + id + "]";
      throw new ReadChildPermissionException(errorMsg, business, mdRelationshipIF, userIF);
    }

    // convert the set to a list (Sets aren't serializable)
    List<? extends Business> businessList = business.getChildren(relationshipType).getAll();

    return FacadeUtil.buildBusinessDTOListFromBusinesses(sessionId, businessList);
  }

  /**
   * Returns a list of RelationshipDTO objects (with children information) of
   * the specified relationhip type for the given BusinessDTO object.
   * 
   * @param sessionId
   * @param id
   * @param relationshipType
   * @return
   */
  @Request(RequestType.SESSION)
  public static List<RelationshipDTO> getChildRelationships(String sessionId, String id, String relationshipType)
  {
    // Get all the Relationships associated with the children.
    Business business = (Business) getEntity(id);

    MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(relationshipType);

    boolean access = SessionFacade.checkRelationshipAccess(sessionId, Operation.READ_CHILD, business, mdRelationshipIF.getId());

    if (!access)
    {
      UserDAOIF userIF = Session.getCurrentSession().getUser();

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have permission to read children of type [" + mdRelationshipIF.definesType() + "] for parent object [" + id + "]";
      throw new ReadChildPermissionException(errorMsg, business, mdRelationshipIF, userIF);
    }

    // Convert each Relationship into a RelationshipDTO
    List<? extends Relationship> relationshipObjects = business.getChildRelationships(relationshipType).getAll();
    return FacadeUtil.buildRelationshipDTOListFromRelationships(sessionId, relationshipObjects);
  }

  /**
   * Returns a list of StructDTO objects that match the query conditions
   * specified int he given QueryDTO
   * 
   * @param sessionId
   * @param queryDTO
   * @return
   */
  @Request(RequestType.SESSION)
  public static StructQueryDTO queryStructs(String sessionId, StructQueryDTO queryDTO)
  {
    boolean access = SessionFacade.checkTypeAccess(sessionId, Operation.READ, queryDTO.getType());

    if (!access)
    {
      UserDAOIF userIF = Session.getCurrentSession().getUser();

      MdTypeDAOIF mdTypeIF = MdTypeDAO.getMdTypeDAO(queryDTO.getType());

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the read permission for type [" + queryDTO.getType() + "]";
      throw new ReadTypePermissionException(errorMsg, mdTypeIF, userIF);
    }

    FacadeUtil.populateQueryDTOWithStructResults(sessionId, queryDTO);
    ClassToQueryDTO.getConverter(sessionId, queryDTO); // repopulate the
    // QueryDTO with
    // metadata info

    return queryDTO;
  }

  /**
   * Returns a QueryDTO with the result of the QueryDTO specified as a
   * parameter.
   * 
   * @param sessionId
   * @param queryDTO
   * @return
   */
  @Request(RequestType.SESSION)
  public static ClassQueryDTO getQuery(String sessionId, String type)
  {
    MdClassDAOIF mdClassIF = MdClassDAO.getMdClassDAO(type);

    if (!SessionFacade.checkTypeAccess(sessionId, Operation.READ, type))
    {
      UserDAOIF userIF = SessionFacade.getUser(sessionId);
      String error = "User [" + userIF.getSingleActorName() + "] does not have read permission to query type [" + type + "].";
      throw new ReadTypePermissionException(error, mdClassIF, userIF);
    }

    ClassToQueryDTO classToQueryDTO = ClassToQueryDTO.getConverter(sessionId, mdClassIF);
    return classToQueryDTO.populate();
  }

  /**
   * Returns a ComponentQueryDTO containing the results of an arbitrary query
   * for a given type.
   * 
   * @param sessionId
   * @param ComponentQueryDTO
   * @return ComponentQueryDTO containing the query result.
   */
  @Request(RequestType.SESSION)
  public static ComponentQueryDTO groovyObjectQuery(String sessionId, ComponentQueryDTO componentQueryDTO)
  {
    UserDAOIF user = SessionFacade.getUser(sessionId);

    if (!user.isAdministrator())
    {
      String errMsg = "Membership in the [" + RoleDAOIF.ADMIN_ROLE + "] role is required to execute arbitrary Groovy queries.";
      throw new GroovyQueryExecuteException(errMsg, RoleDAOIF.ADMIN_ROLE);
    }

    FacadeUtil.populateComponentQueryFromGroovyQuery(sessionId, componentQueryDTO);

    if (componentQueryDTO instanceof ClassQueryDTO)
    {
      // re-populate the metadata information
      ClassToQueryDTO classToQueryDTO = ClassToQueryDTO.getConverter(sessionId, (ClassQueryDTO) componentQueryDTO);
      return classToQueryDTO.populate();
    }

    return componentQueryDTO;
  }

  /**
   * Returns a ValueQueryDTO containing the results of an arbitrary value query.
   * 
   * @param sessionId
   * @param valueQueryDTO
   * @return ValueQueryDTO containing the query result.
   */
  @Request(RequestType.SESSION)
  public static ValueQueryDTO groovyValueQuery(String sessionId, ValueQueryDTO valueQueryDTO)
  {
    UserDAOIF user = SessionFacade.getUser(sessionId);

    if (!user.isAdministrator())
    {
      String errMsg = "Membership in the [" + RoleDAOIF.ADMIN_ROLE + "] role is required to execute arbitrary Groovy queries.";
      throw new GroovyQueryExecuteException(errMsg, RoleDAOIF.ADMIN_ROLE);
    }

    FacadeUtil.populateValueQueryWithValueObjectResults(sessionId, valueQueryDTO);

    return valueQueryDTO;
  }

  /**
   * Returns a list of EntityDTO objects that match the query conditions
   * specified int he given QueryDTO
   * 
   * @param sessionId
   * @param queryDTO
   * @return
   */
  @Request(RequestType.SESSION)
  public static EntityQueryDTO queryEntities(String sessionId, EntityQueryDTO queryDTO)
  {
    boolean access = SessionFacade.checkTypeAccess(sessionId, Operation.READ, queryDTO.getType());

    if (!access)
    {
      UserDAOIF userIF = Session.getCurrentSession().getUser();

      MdTypeDAOIF mdTypeIF = MdTypeDAO.getMdTypeDAO(queryDTO.getType());

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the read permission for type [" + queryDTO.getType() + "]";
      throw new ReadTypePermissionException(errorMsg, mdTypeIF, userIF);
    }

    if (queryDTO instanceof BusinessQueryDTO)
    {
      FacadeUtil.populateQueryDTOWithBusinessResults(sessionId, (BusinessQueryDTO) queryDTO);
    }
    else if (queryDTO instanceof RelationshipQueryDTO)
    {
      FacadeUtil.populateQueryDTOWithRelationshipResults(sessionId, (RelationshipQueryDTO) queryDTO);
    }
    else if (queryDTO instanceof StructQueryDTO)
    {
      FacadeUtil.populateQueryDTOWithStructResults(sessionId, (StructQueryDTO) queryDTO);
    }
    else
    {
      String error = "The type [" + queryDTO.getType() + "] cannot be queried via the queryEntities() method.";
      throw new QueryException(error);
    }

    ClassToQueryDTO.getConverter(sessionId, queryDTO); // repopulate the
    // QueryDTO with
    // metadata info
    return queryDTO;
  }

  /**
   * Returns a list of ViewDTOs objects returned by the query specified in the
   * QueryDTO.
   * 
   * @param sessionId
   * @param queryDTO
   * @return
   */
  @Request(RequestType.SESSION)
  public static ViewQueryDTO queryViews(String sessionId, ViewQueryDTO queryDTO)
  {
    boolean access = SessionFacade.checkTypeAccess(sessionId, Operation.READ, queryDTO.getType());

    if (!access)
    {
      UserDAOIF userIF = Session.getCurrentSession().getUser();

      MdTypeDAOIF mdTypeIF = MdTypeDAO.getMdTypeDAO(queryDTO.getType());

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the read permission for type [" + queryDTO.getType() + "]";
      throw new ReadTypePermissionException(errorMsg, mdTypeIF, userIF);
    }

    FacadeUtil.populateQueryDTOWithViewResults(sessionId, queryDTO);
    ClassToQueryDTO.getConverter(sessionId, queryDTO); // repopulate the
    // QueryDTO with
    // metadata info

    return queryDTO;
  }

  /**
   * Returns a list of BusinessDTOs objects returned by the query specified in
   * the QueryDTO.
   * 
   * @param sessionId
   * @param queryDTO
   * @return
   */
  @Request(RequestType.SESSION)
  public static BusinessQueryDTO queryBusinesses(String sessionId, BusinessQueryDTO queryDTO)
  {
    boolean access = SessionFacade.checkTypeAccess(sessionId, Operation.READ, queryDTO.getType());

    if (!access)
    {
      UserDAOIF userIF = Session.getCurrentSession().getUser();

      MdTypeDAOIF mdTypeIF = MdTypeDAO.getMdTypeDAO(queryDTO.getType());

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the read permission for type [" + queryDTO.getType() + "]";
      throw new ReadTypePermissionException(errorMsg, mdTypeIF, userIF);
    }

    FacadeUtil.populateQueryDTOWithBusinessResults(sessionId, queryDTO);
    ClassToQueryDTO.getConverter(sessionId, queryDTO); // repopulate the
    // QueryDTO with
    // metadata info

    return queryDTO;
  }

  /**
   * Returns a list of BusinessDTOs objects returned by the query specified in
   * the QueryDTO.
   * 
   * @param sessionId
   * @param queryDTO
   * @return
   */
  @Request(RequestType.SESSION)
  public static RelationshipQueryDTO queryRelationships(String sessionId, RelationshipQueryDTO queryDTO)
  {
    boolean access = SessionFacade.checkTypeAccess(sessionId, Operation.READ, queryDTO.getType());

    if (!access)
    {
      UserDAOIF userIF = Session.getCurrentSession().getUser();

      MdTypeDAOIF mdTypeIF = MdTypeDAO.getMdTypeDAO(queryDTO.getType());

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the read permission for type [" + queryDTO.getType() + "]";
      throw new ReadTypePermissionException(errorMsg, mdTypeIF, userIF);
    }

    FacadeUtil.populateQueryDTOWithRelationshipResults(sessionId, queryDTO);
    ClassToQueryDTO.getConverter(sessionId, queryDTO); // repopulate the
    // QueryDTO with
    // metadata info

    return queryDTO;
  }

  /**
   * Returns the BusinessDTO of the values for an enumeration of the given type
   * and name
   * 
   * @param sessionId
   *          The session Id
   * @param enumType
   *          The type of the enumeration
   * @param enumName
   *          The name of the enumerated item
   * @return
   */
  @Request(RequestType.SESSION)
  public static BusinessDTO getEnumeration(String sessionId, String enumType, String enumName)
  {
    Business entity = EnumerationFacade.getBusinessForEnumeration(enumType, enumName);

    boolean access = SessionFacade.checkAccess(sessionId, Operation.READ, entity);

    if (!access)
    {
      UserDAOIF userIF = Session.getCurrentSession().getUser();

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the read permission for type [" + entity.getType() + "]";
      throw new ReadPermissionException(errorMsg, entity, userIF);
    }

    return (BusinessDTO) FacadeUtil.populateComponentDTOIF(sessionId, entity, true);
  }

  /**
   * Returns the BusinessDTO of the enumeration items for the given
   * MdEnumeration type with the given names.
   * 
   * @param sessionId
   *          The session Id
   * @param enumType
   *          The type of the enumeration
   * @param enumNames
   *          names of the enumeration items.
   * @return BusinessDTO of the enumeration items for the give MdEnumeration
   *         type.
   */
  @Request(RequestType.SESSION)
  public static List<BusinessDTO> getEnumerations(String sessionId, String enumType, String[] enumNames)
  {
    // This will generate a localized message if the enumType is not valid.
    MdEnumerationDAOIF mdEnumerationIF = MdEnumerationDAO.getMdEnumerationDAO(enumType);

    BusinessEnumeration[] busEnumArray = new BusinessEnumeration[enumNames.length];

    Class<?> c;
    try
    {
      c = LoaderDecorator.load(enumType);
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }

    for (int i = 0; i < enumNames.length; i++)
    {
      try
      {
        busEnumArray[i] = (BusinessEnumeration) c.getMethod("valueOf", String.class).invoke(null, enumNames[i]);
      }
      catch (InvocationTargetException ite)
      {
        Throwable cause = ite.getCause();
        if (cause != null)
        {
          if (cause instanceof IllegalArgumentException)
          {
            String errMsg = "The enummeration name [" + enumNames[i] + "] is not valid for enumeration [" + enumType + "].";
            throw new InvalidEnumerationName(errMsg, enumNames[i], mdEnumerationIF);
          }
        }
        throw new ProgrammingErrorException(ite);
      }
      catch (Exception e)
      {
        throw new ProgrammingErrorException(e);
      }
    }

    return getEnumerationItems(sessionId, busEnumArray);
  }

  /**
   * Returns the BusinessDTO of the enumeration items for the given
   * MdEnumeration type.
   * 
   * @param sessionId
   *          The session Id
   * @param enumType
   *          The type of the enumeration
   * @return BusinessDTO of the enumeration items for the give MdEnumeration
   *         type.
   */
  @Request(RequestType.SESSION)
  public static List<BusinessDTO> getAllEnumerations(String sessionId, String enumType)
  {
    // This will generate a localized message if the enumType is not valid.
    MdEnumerationDAO.getMdEnumerationDAO(enumType);

    BusinessEnumeration[] busEnumArray;

    try
    {
      Class<?> c = LoaderDecorator.load(enumType);
      busEnumArray = (BusinessEnumeration[]) c.getMethod("values").invoke(null);
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }

    return getEnumerationItems(sessionId, busEnumArray);
  }

  /**
   * Returns <code>BusinessDTO</code>s that have attributes for the given
   * enumerations.
   * 
   * @param sessionId
   * @param busEnumArray
   * @return <code>BusinessDTO</code>s that have attributes for the given
   *         enumerations.
   */
  private static List<BusinessDTO> getEnumerationItems(String sessionId, BusinessEnumeration[] busEnumArray)
  {
    List<BusinessDTO> businessDTOList = new LinkedList<BusinessDTO>();

    for (BusinessEnumeration businessEnumeration : busEnumArray)
    {
      Business entity = Business.get(businessEnumeration.getId());

      boolean access = SessionFacade.checkAccess(sessionId, Operation.READ, entity);

      if (!access)
      {
        UserDAOIF userIF = Session.getCurrentSession().getUser();

        String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the read permission for type [" + entity.getType() + "]";
        throw new ReadPermissionException(errorMsg, entity, userIF);
      }

      businessDTOList.add((BusinessDTO) FacadeUtil.populateComponentDTOIF(sessionId, entity, true));
    }

    return businessDTOList;
  }

  /**
   * Retrieves a globally readable file from the server.
   * 
   * @param sessionId
   *          The id of the session
   * @param fileId
   *          The id of the file to retrieve
   * @return
   */
  @Request(RequestType.SESSION)
  public static InputStream getFile(String sessionId, String fileId)
  {
    WebFileDAOIF file = WebFileDAO.get(fileId);
    return file.getFile();
  }

  @Request(RequestType.SESSION)
  public static BusinessDTO newFile(String sessionId, String path, String filename, String extension, InputStream stream)
  {
    return newFileTransaction(sessionId, path, filename, extension, stream);
  }

  // A transaction needs to be nested within the control flow of a session.
  @Transaction
  private static BusinessDTO newFileTransaction(String sessionId, String path, String filename, String extension, InputStream stream)
  {
    Business entity = BusinessFacade.newBusiness(WebFileInfo.CLASS);
    boolean access = SessionFacade.checkAccess(sessionId, Operation.CREATE, entity);

    WebFileDAO file = (WebFileDAO) BusinessFacade.getEntityDAO(entity);

    file.setExtension(extension);
    file.setFileName(filename);
    file.setFilePath(path);

    if (!access)
    {
      UserDAOIF user = SessionFacade.getUser(sessionId);
      String errorMsg = "User [" + user.getSingleActorName() + "] does not have permission to create the file [" + file.getFileName() + "] ";
      throw new CreatePermissionException(errorMsg, entity, user);
    }

    entity.apply();
    file.putFile(stream);

    return (BusinessDTO) FacadeUtil.populateComponentDTOIF(sessionId, entity, true);
  }

  @Request(RequestType.SESSION)
  public static InputStream getSecureFile(String sessionId, String fileId)
  {
    Entity entity = Entity.getEntity(fileId);
    VaultFileDAOIF file = (VaultFileDAOIF) BusinessFacade.getEntityDAO(entity);

    boolean access = SessionFacade.checkAccess(sessionId, Operation.READ, entity);

    if (!access)
    {
      UserDAOIF user = SessionFacade.getUser(sessionId);
      String errorMsg = "User [" + user.getSingleActorName() + "] does not have permission to read the file [" + file.getFileName() + "] ";
      throw new ReadPermissionException(errorMsg, entity, user);
    }

    return file.getFileStream();
  }

  @Request(RequestType.SESSION)
  public static InputStream getSecureFile(String sessionId, String attributeName, String type, String fileId)
  {
    Business vaultFileBusiness = Business.get(fileId);
    MdEntityDAOIF mdEntity = MdEntityDAO.getMdEntityDAO(type);

    boolean access = SessionFacade.checkAttributeAccess(sessionId, Operation.READ, vaultFileBusiness, mdEntity.definesAttribute(attributeName));

    if (!access)
    {
      UserDAOIF userIF = Session.getCurrentSession().getUser();

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the read permission for type [" + vaultFileBusiness.getType() + "]";
      throw new ReadPermissionException(errorMsg, vaultFileBusiness, userIF);
    }

    VaultFileDAOIF vaultDAO = (VaultFileDAOIF) BusinessFacade.getEntityDAO(vaultFileBusiness);

    return vaultDAO.getFileStream();
  }

  @Request(RequestType.SESSION)
  public static BusinessDTO getVaultFileDTO(String sessionId, String type, String attributeName, String fileId)
  {
    MdEntityDAOIF mdEntity = MdEntityDAO.getMdEntityDAO(type);
    Business vaultFile = Business.get(fileId);

    boolean access = SessionFacade.checkAttributeAccess(sessionId, Operation.READ, vaultFile, mdEntity.definesAttribute(attributeName));

    if (!access)
    {
      UserDAOIF userIF = Session.getCurrentSession().getUser();

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the read permission for type [" + vaultFile.getType() + "]";
      throw new ReadPermissionException(errorMsg, vaultFile, userIF);
    }

    return (BusinessDTO) FacadeUtil.populateComponentDTOIF(sessionId, vaultFile, true);
  }

  @Request(RequestType.SESSION)
  public static BusinessDTO newSecureFile(String sessionId, String filename, String extension, InputStream stream)
  {
    return writeSecureFile(sessionId, filename, extension, stream);
  }

  @Transaction
  private static BusinessDTO writeSecureFile(String sessionId, String filename, String extension, InputStream stream)
  {
    Business entity = BusinessFacade.newBusiness(VaultFileInfo.CLASS);
    VaultFileDAO file = (VaultFileDAO) BusinessFacade.getEntityDAO(entity);

    boolean access = SessionFacade.checkAccess(sessionId, Operation.CREATE, entity);

    if (!access)
    {
      UserDAOIF user = SessionFacade.getUser(sessionId);
      String errorMsg = "User [" + user.getSingleActorName() + "] does not have permission to create the file [" + file.getFileName() + "] ";
      throw new CreatePermissionException(errorMsg, entity, user);
    }

    entity.setValue(VaultFileInfo.EXTENSION, extension);
    entity.setValue(VaultFileInfo.FILE_NAME, filename);

    file.setSize(0);
    entity.apply();
    file.putFile(stream);

    return (BusinessDTO) FacadeUtil.populateComponentDTOIF(sessionId, entity, true);
  }

  /**
   * Creates a new struct instance
   * 
   * @param sessionId
   * @param type
   */
  @Request(RequestType.SESSION)
  public static StructDTO newStruct(String sessionId, String type)
  {
    Struct struct = BusinessFacade.newStruct(type);

    return (StructDTO) FacadeUtil.populateComponentDTOIF(sessionId, struct, true);
  }

  /**
   * Import the given domain model specified in a xml string into the database.
   * 
   * @param sessionId
   * @param xml
   */
  @Request(RequestType.SESSION)
  public static void importDomainModel(String sessionId, String xml, String xsd)
  {
    UserDAOIF user = SessionFacade.getUser(sessionId);

    if (!user.isAdministrator())
    {
      String errMsg = "Membership in the [" + RoleDAOIF.ADMIN_ROLE + "] role is required to import a domain model.";
      throw new ImportDomainExecuteException(errMsg, RoleDAOIF.ADMIN_ROLE);
    }

    SAXImporter.runImport(xml, xsd);
  }

  /**
   * Imports the given instance xml.
   * 
   * @param sessionId
   * @param xml
   */
  @Request(RequestType.SESSION)
  public static void importInstanceXML(String sessionId, String xml)
  {
    UserDAOIF user = SessionFacade.getUser(sessionId);

    if (!user.isAdministrator())
    {
      String errMsg = "Membership in the [" + RoleDAOIF.ADMIN_ROLE + "] role is required to import instance definitions.";
      throw new ImportDomainExecuteException(errMsg, RoleDAOIF.ADMIN_ROLE);
    }

    URL instanceXSD = FacadeUtil.class.getResource("/" + XMLConstants.INSTANCE_XSD);
    InstanceImporter.runImport(xml, instanceXSD, new DefaultConflictResolver());
  }

  /**
   * Returns a type-safe entity if possible. Otherwise, it returns a type-unsafe
   * entity.
   * 
   * @param id
   * @return
   */
  private static Entity getEntity(String id)
  {
    Entity entity;
    try
    {
      entity = BusinessFacade.getEntity(id);
    }
    catch (ClassLoaderException e)
    {
      entity = Entity.getEntity(id);
    }

    return entity;
  }

  @Request(RequestType.SESSION)
  public static InputStream exportExcelFile(String sessionId, String type, String listenerMethod, String[] params)
  {
    ExcelExporter exporter = new ExcelExporter();

    if (listenerMethod != null)
    {
      try
      {
        // Load the type which is being exported
        Class<?> c = LoaderDecorator.load(type);

        // Get the listener method
        Method method = c.getMethod(listenerMethod, ExcelExporter.class, String[].class);

        // Invoke the method and get the ExcelExportListener
        method.invoke(null, exporter, (Object) params);
      }
      catch (SecurityException e)
      {
        throw new ProgrammingErrorException(e);
      }
      catch (NoSuchMethodException e)
      {
        // Do nothing if the method doesn't exist then continue
      }
      catch (IllegalArgumentException e)
      {
        throw new ProgrammingErrorException(e);
      }
      catch (IllegalAccessException e)
      {
        throw new ProgrammingErrorException(e);
      }
      catch (InvocationTargetException e)
      {
        Throwable targetException = e.getTargetException();
        if (targetException instanceof RunwayExceptionIF)
        {
          throw (RuntimeException) targetException;
        }
        else
        {
          throw new ProgrammingErrorException(e);
        }
      }
    }

    exporter.addTemplate(type);

    return new ByteArrayInputStream(exporter.write());
  }

  @Request(RequestType.SESSION)
  public static InputStream importExcelFile(String sessionId, InputStream stream, String type, String listenerMethod, String[] params)
  {
    ExcelImporter importer = new ExcelImporter(stream);

    if (type != null && listenerMethod != null)
    {
      try
      {
        // Load the type which is being exported
        Class<?> c = LoaderDecorator.load(type);

        // Get the listener method
        Method method = c.getMethod(listenerMethod, ExcelImporter.class, String[].class);

        // Invoke the method and get the ExcelExportListener
        method.invoke(null, importer, (Object) params);
      }
      catch (NoSuchMethodException e)
      {
        // Do nothing if the method doesn't exist then continue
      }
      catch (InvocationTargetException e)
      {
        Throwable targetException = e.getTargetException();
        if (targetException instanceof RunwayExceptionIF)
        {
          throw (RuntimeException) targetException;
        }
        else
        {
          throw new ProgrammingErrorException(e);
        }
      }
      catch (Exception e)
      {
        throw new ProgrammingErrorException(e);
      }
    }

    return new ByteArrayInputStream(importer.read());
  }

}
