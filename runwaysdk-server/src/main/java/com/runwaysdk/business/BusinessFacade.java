/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.business;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.runwaysdk.RunwayException;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.rbac.ActorDAOIF;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.business.state.MdStateMachineDAOIF;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.RelationshipDTOInfo;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.ComponentDAO;
import com.runwaysdk.dataaccess.ElementDAO;
import com.runwaysdk.dataaccess.ElementDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdExceptionDAOIF;
import com.runwaysdk.dataaccess.MdInformationDAOIF;
import com.runwaysdk.dataaccess.MdProblemDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdSessionDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.MdWarningDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.StructDAOIF;
import com.runwaysdk.dataaccess.TransientDAO;
import com.runwaysdk.dataaccess.TransientDAOFactory;
import com.runwaysdk.dataaccess.TransitionDAOIF;
import com.runwaysdk.dataaccess.UnexpectedTypeException;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.Ownable;
import com.runwaysdk.transport.attributes.AttributeDTOFacade;
import com.runwaysdk.transport.attributes.AttributeEnumerationDTO;

/**
 * Created to allow access to methods or variables in {@link com.runwaysdk.business.Business}, {@link com.runwaysdk.business.Entity}, and {@link com.runwaysdk.business.Relationship} that have default
 * visibility. Default visibility prevents generated classes from inheriting the methods, but still allows this Facade to access them, allowing the needed access to the Data Access Layer without
 * exposing implementation.
 * 
 * @author Eric Grunzke
 */
public class BusinessFacade
{

  /**
   * Returns a UserIF object that represents the user that has a lock on this entity, or null if the entity is not locked.
   * 
   * @return UserIF object that represents the user that has a lock on this entity, or null if the entity is not locked.
   */
  public static UserDAOIF getLockedByDAO(Element element)
  {
    return element.getLockedByDAO();
  }

  /**
   * Returns the current state of the Business parameter
   * 
   * @param object
   *          A Business
   * @return The current state of object
   */
  public static StateMasterDAOIF currentState(Business object)
  {
    return object.currentState();
  }

  /**
   * Gets a business MdAttribute by name from a Entity.
   * 
   * @param entity
   *          The entity that has the specified attribute
   * @param attributeName
   *          The name of the desired attribute
   * @return AttribtueIF For the attributeName
   */
  public static AttributeIF getAttribute(Entity entity, String attributeName)
  {
    return entity.getEntityDAO().getAttributeIF(attributeName);
  }

  /**
   * Gets a business MdAttribute by name from a Entity.
   * 
   * @param sessionComponent
   *          The entity that has the specified attribute
   * @param attributeName
   *          The name of the desired attribute
   * @return AttribtueIF For the attributeName
   */
  public static AttributeIF getAttribute(SessionComponent sessionComponent, String attributeName)
  {
    return sessionComponent.getTransientDAO().getAttributeIF(attributeName);
  }

  /**
   * Sets enumeration item names on the given <code>AttributeEnumerationDTO</code>.
   * 
   * @param entity
   *          The entity to dereference
   * @param attributeName
   *          The name of the attribute to dereference
   * @param attributeEnumerationDTO
   *          enumeration items are set on this attribute.
   */
  public static void getAttributeEnumerationNames(Entity entity, String attributeName, AttributeEnumerationDTO attributeEnumerationDTO)
  {
    // This cast is OK, as the DAO is not being modified.
    setEnumNames((EntityDAO) entity.getEntityDAO(), attributeName, attributeEnumerationDTO);
  }

  /**
   * Sets enumeration item names on the given <code>AttributeEnumerationDTO</code>.
   * 
   * @param smartException
   *          The smartException to dereference
   * @param attributeName
   *          The name of the attribute to dereference
   * @param attributeEnumerationDTO
   *          enumeration items are set on this attribute.
   */
  public static void getAttributeEnumerationNames(SmartException smartException, String attributeName, AttributeEnumerationDTO attributeEnumerationDTO)
  {
    setEnumNames(smartException.getTransientDAO(), attributeName, attributeEnumerationDTO);
  }

  /**
   * Sets enumeration item names on the given <code>AttributeEnumerationDTO</code>.
   * 
   * @param problem
   *          The smartException to dereference
   * @param attributeName
   *          The name of the attribute to dereference
   * @param attributeEnumerationDTO
   *          enumeration items are set on this attribute.
   */
  public static void getAttributeEnumerationNames(Problem problem, String attributeName, AttributeEnumerationDTO attributeEnumerationDTO)
  {
    setEnumNames(problem.getTransientDAO(), attributeName, attributeEnumerationDTO);
  }

  /**
   * Sets enumeration item names on the given <code>AttributeEnumerationDTO</code>.
   * 
   * @param message
   *          The smartException to dereference
   * @param attributeName
   *          The name of the attribute to dereference
   * @param attributeEnumerationDTO
   *          enumeration items are set on this attribute.
   */
  public static void getAttributeEnumerationNames(Message message, String attributeName, AttributeEnumerationDTO attributeEnumerationDTO)
  {
    setEnumNames(message.getTransientDAO(), attributeName, attributeEnumerationDTO);
  }

  /**
   * Sets enumeration item names on the given <code>AttributeEnumerationDTO</code>.
   * 
   * @param session
   *          The Session to dereference
   * @param attributeName
   *          The name of the attribute to dereference
   * @param attributeEnumerationDTO
   *          enumeration items are set on this attribute.
   */
  public static void getAttributeEnumerationNames(SessionComponent session, String attributeName, AttributeEnumerationDTO attributeEnumerationDTO)
  {
    setEnumNames(session.getTransientDAO(), attributeName, attributeEnumerationDTO);
  }

  /**
   * Sets enumeration item names on the given <code>AttributeEnumerationDTO</code>.
   * 
   * @param entity
   *          The entity to dereference.
   * @param attributeName
   *          The name of the attribute to dereference.
   * @param attributeEnumerationDTO
   *          enumeration items are set on this attribute.
   */
  public static void getAttributeEnumerationNames(ComponentDAO component, String attributeName, AttributeEnumerationDTO attributeEnumerationDTO)
  {
    setEnumNames(component, attributeName, attributeEnumerationDTO);
  }

  private static void setEnumNames(ComponentDAO component, String attributeName, AttributeEnumerationDTO attributeEnumerationDTO)
  {
    AttributeEnumerationIF attribute = (AttributeEnumerationIF) component.getAttributeIF(attributeName);
    BusinessDAOIF[] businessDAOs = attribute.dereference();

    for (BusinessDAOIF businessDAOIF : businessDAOs)
    {
      String enumName = businessDAOIF.getAttributeIF(EnumerationMasterInfo.NAME).getValue();
      AttributeDTOFacade.addEnumItemInternal(attributeEnumerationDTO, enumName);
    }
  }

  /**
   * Returns the EntityDAO from the DataAccess layer used by the given Entity object. Absolutely do not call this method unless you know what you are doing.
   * 
   * @param entity
   * @return EntityDAO from the DataAccess layer used by the given Entity object.
   */
  public static EntityDAOIF getEntityDAO(Entity entity)
  {
    return entity.getEntityDAO();
  }

  public static ActorDAOIF getOwner(Ownable hasOwner)
  {
    return hasOwner.getOwnerDAO();
  }

  /**
   * Returns the TransientDAO from the DataAccess layer used by the given SessionComponent object. Absolutely do not call this method unless you know what you are doing.
   * 
   * @param sessionComponent
   * @return Session from the DataAccess layer used by the given SessionComponent object.
   */
  public static TransientDAO getTransientDAO(SessionComponent sessionComponent)
  {
    return sessionComponent.getTransientDAO();
  }

  /**
   * Returns the TransientDAO from the DataAccess layer used by the given SmartException object. Absolutely do not call this method unless you know what you are doing.
   * 
   * @param smartException
   * @return Session from the DataAccess layer used by the given SmartException object.
   */
  public static TransientDAO getTransientDAO(SmartException smartException)
  {
    return smartException.getTransientDAO();
  }

  /**
   * Returns the TransientdDAO from the DataAccess layer used by the given problem object. Absolutely do not call this method unless you know what you are doing.
   * 
   * @param problem
   * @return Session from the DataAccess layer used by the given problem object.
   */
  public static TransientDAO getTransientDAO(Problem problem)
  {
    return problem.getTransientDAO();
  }

  /**
   * Provides a way to construct a typesafe {@link Entity} from an {@link EntityDAO}.
   * 
   * @param entityDAOIF
   *          Data access layer object to wrap in Business Layer
   * @return Entity
   */
  public static Entity get(EntityDAOIF entityDAOIF)
  {
    if (entityDAOIF instanceof BusinessDAOIF)
    {
      return Business.instantiate((BusinessDAOIF) entityDAOIF);
    }
    else if (entityDAOIF instanceof RelationshipDAOIF)
    {
      return Relationship.instantiate((RelationshipDAOIF) entityDAOIF);
    }
    else if (entityDAOIF instanceof StructDAOIF)
    {
      return Struct.instantiate((StructDAOIF) entityDAOIF);
    }
    else
    {
      return Struct.instantiate((StructDAOIF) entityDAOIF);
    }
  }

  /**
   * Provides a way to construct a typesafe {@link Entity} from an {@link TransientDAO}.
   * 
   * @param transientDAO
   *          Data access layer object to wrap in Business Layer
   * @return SessionComponent
   */
  public static SessionComponent get(TransientDAO transientDAO)
  {
    return SessionComponent.instantiate(transientDAO);
  }

  /**
   * Returns a View class whoes attributes are derived from the given <param>valueObject</param>.
   * 
   * @param viewType
   * @param valueObject
   * @return
   */
  public static View convertValueObjectToView(String viewType, ValueObject valueObject)
  {
    TransientDAO transientDAO = TransientDAOFactory.buildTransientDAOfromValueObject(viewType, valueObject);
    return (View) get(transientDAO);
  }

  /**
   * Provides a way to construct a typeUNsafe {@link Struct} from an {@link StructDAOIF}.
   * 
   * @param structDAOIF
   *          Data access layer object to wrap in Struct Layer
   * @return Struct
   */
  public static Struct typeUnsafeStruct(StructDAO structDAO)
  {
    return Struct.typeUnsafeStructFactory(structDAO);
  }

  /**
   * Provides a way to construct a typesafe {@link Business} from an {@link BusinessDAOIF}.
   * 
   * @param businessDAOIF
   *          Data access layer object to wrap in Business Layer
   * @return Business
   */
  public static Business get(BusinessDAOIF businessDAOIF)
  {
    return Business.instantiate((BusinessDAOIF) businessDAOIF);
  }

  /**
   * Provides a way to construct a typesafe {@link Relationship} from an {@link RelationshipDAOIF}.
   * 
   * @param relationshipDAOIF
   *          Data access layer object to wrap in Business Layer
   * @return Relationship
   */
  public static Relationship get(RelationshipDAOIF relationshipDAOIF)
  {
    return Relationship.instantiate((RelationshipDAOIF) relationshipDAOIF);
  }

  /**
   * Provides a way to construct a typesafe {@link Struct} from an {@link StructDAOIF}.
   * 
   * @param structDAOIF
   *          Data access layer object to wrap in Business Layer
   * @return Struct
   */
  public static Struct get(StructDAOIF structDAOIF)
  {
    return Struct.instantiate((StructDAOIF) structDAOIF);
  }

  /**
   * Returns true if the MdType defining the specified type is reserved. Reserved types don't have classes generated for them.
   * 
   * @param type
   * @return
   */
  public static boolean isReservedType(String type)
  {
    MdTypeDAOIF mdType = MdElementDAO.getMdTypeDAO(type);

    if (GenerationUtil.isReservedType(mdType) || !mdType.isGenerateSource())
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Returns a new instance of a struct. The returned instance is type-safe if the struct is not a reserved type.
   * 
   * @param type
   * @return
   */
  public static Struct newStruct(String type)
  {
    Struct businessStruct = null;

    if (isReservedType(type))
    {
      businessStruct = Struct.newInstance(type);
    }
    else
    {
      try
      {
        Class<?> clazz = LoaderDecorator.load(type);
        Object newInstance = clazz.newInstance();
        if (newInstance instanceof Struct)
          return (Struct) newInstance;
        String message = "Called Struct.newStruct() for type [" + type + "], which is not a Struct type.";
        throw new UnexpectedTypeException(message);
      }
      catch (InstantiationException e)
      {
        throw new ProgrammingErrorException(e);
      }
      catch (IllegalAccessException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }

    return businessStruct;
  }

  public static void setLocalStructDTODefaultValue(LocalStructDTO localStructDTO, String defaultValue)
  {
    localStructDTO.copyLocalizedValue(defaultValue);
  }

  /**
   * Returns a new instance of a business relationship. The returned instance is type-safe if the relationship is not a reserved type.
   * 
   * @param parentId
   * @param childId
   * @param type
   * @return
   */
  public static Relationship newRelationship(String parentId, String childId, String type)
  {
    Relationship relationship = null;

    if (isReservedType(type))
    {
      relationship = new Relationship(parentId, childId, type);
    }
    else
    {
      try
      {
        Class<?> clazz = LoaderDecorator.load(type);
        Constructor<?> constructor = clazz.getConstructor(String.class, String.class);
        relationship = (Relationship) constructor.newInstance(parentId, childId);
      }
      catch (InstantiationException e)
      {
        throw new ProgrammingErrorException(e);
      }
      catch (IllegalAccessException e)
      {
        throw new ProgrammingErrorException(e);
      }
      catch (IllegalArgumentException e)
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
      catch (SecurityException e)
      {
        throw new ProgrammingErrorException(e);
      }
      catch (NoSuchMethodException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }

    return relationship;
  }

  /**
   * Returns a new, type-safe SmartException of the specified type.
   * 
   * @param type
   * @return
   */
  public static SmartException newSmartException(String type)
  {
    try
    {
      Class<?> clazz = LoaderDecorator.load(type);
      Object newInstance = clazz.newInstance();
      if (newInstance instanceof SmartException)
      {
        return (SmartException) newInstance;
      }
      String message = "Called SmartException.newInstance() for type [" + type + "], which is not a View type.";
      throw new UnexpectedTypeException(message);
    }
    catch (InstantiationException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (IllegalAccessException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  /**
   * Returns a new, type-safe Problem of the specified type.
   * 
   * @param type
   * @return
   */
  public static Problem newProblem(String type)
  {
    try
    {
      Class<?> clazz = LoaderDecorator.load(type);
      Object newInstance = clazz.newInstance();
      if (newInstance instanceof Problem)
      {
        return (Problem) newInstance;
      }
      String message = "Called Problem.newInstance() for type [" + type + "], which is not a View type.";
      throw new UnexpectedTypeException(message);
    }
    catch (InstantiationException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (IllegalAccessException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  /**
   * Returns a new, type-safe Warning of the specified type.
   * 
   * @param type
   * @return
   */
  public static Warning newWarning(String type)
  {
    try
    {
      Class<?> clazz = LoaderDecorator.load(type);
      Object newInstance = clazz.newInstance();
      if (newInstance instanceof Warning)
      {
        return (Warning) newInstance;
      }
      String message = "Called Warning.newInstance() for type [" + type + "], which is not a View type.";
      throw new UnexpectedTypeException(message);
    }
    catch (InstantiationException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (IllegalAccessException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  /**
   * Returns a new, type-safe Information of the specified type.
   * 
   * @param type
   * @return
   */
  public static Information newInformation(String type)
  {
    try
    {
      Class<?> clazz = LoaderDecorator.load(type);
      Object newInstance = clazz.newInstance();
      if (newInstance instanceof Information)
      {
        return (Information) newInstance;
      }
      String message = "Called Information.newInstance() for type [" + type + "], which is not a View type.";
      throw new UnexpectedTypeException(message);
    }
    catch (InstantiationException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (IllegalAccessException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  /**
   * Returns a new, type-safe SessionComponent of the specified type.
   * 
   * @param type
   * @return
   */
  public static SessionComponent newSessionComponent(String type)
  {
    try
    {
      Class<?> clazz = LoaderDecorator.load(type);
      Object newInstance = clazz.newInstance();
      if (newInstance instanceof View || newInstance instanceof Util)
      {
        return (SessionComponent) newInstance;
      }
      String message = "Called SessionComponent.newInstance() for type [" + type + "], which is not a View type.";
      throw new UnexpectedTypeException(message);
    }
    catch (InstantiationException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (IllegalAccessException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  /**
   * Returns a new, type-safe Business of the specified type. However, if the type is reserved (see {@link #isReservedType(String)}), then the returned object will not be typesafe.
   * 
   * @param type
   * @return
   */
  public static Business newBusiness(String type)
  {
    Business business = null;

    if (isReservedType(type))
    {
      business = new Business(type);
    }
    else
    {
      try
      {
        Class<?> clazz = LoaderDecorator.load(type);
        Object newInstance = clazz.newInstance();
        if (newInstance instanceof Business)
          return (Business) newInstance;
        String message = "Called Business.newInstance() for type [" + type + "], which is not a Business type.";
        throw new UnexpectedTypeException(message);
      }
      catch (InstantiationException e)
      {
        throw new ProgrammingErrorException(e);
      }
      catch (IllegalAccessException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }

    return business;
  }

  /**
   * Returns the type-safe (through reflection) instance of the Element with the given id
   * 
   * @return
   */
  public static Element getElement(String id)
  {
    ElementDAOIF elementDAOIF = ElementDAO.get(id);
    return (Element) get(elementDAOIF);
  }

  /**
   * Returns the type-safe (through reflection) instance of the Entity with the given id
   * 
   * @return
   */
  public static Entity getEntity(String id)
  {
    EntityDAOIF entityDAO = EntityDAO.get(id);
    return get(entityDAO);
  }

  /**
   * Returns a new instance
   * 
   * @param type
   * @return
   */
  public static Mutable newMutable(String type)
  {
    MdClassDAOIF mdClassIF = MdClassDAO.getMdClassDAO(type);

    if (mdClassIF instanceof MdEntityDAOIF)
    {
      return newEntity(type);
    }
    else if (mdClassIF instanceof MdExceptionDAOIF)
    {
      return newSmartException(type);
    }
    else if (mdClassIF instanceof MdSessionDAOIF)
    {
      return newSessionComponent(type);
    }
    else if (mdClassIF instanceof MdProblemDAOIF)
    {
      return newProblem(type);
    }
    else if (mdClassIF instanceof MdInformationDAOIF)
    {
      return newInformation(type);
    }
    else if (mdClassIF instanceof MdWarningDAOIF)
    {
      return newWarning(type);
    }
    else
    {
      String message = "The type [" + type + "] does not represent a [" + Mutable.class.getName() + "] class.";
      throw new UnexpectedTypeException(message);
    }
  }

  /**
   * Returns a new instance
   * 
   * @param type
   * @return
   */
  public static Entity newEntity(String type)
  {
    MdClassDAOIF mdClassIF = MdClassDAO.getMdClassDAO(type);

    if (mdClassIF instanceof MdBusinessDAOIF)
    {
      return newBusiness(type);
    }
    else if (mdClassIF instanceof MdRelationshipDAOIF)
    {
      return newRelationship(RelationshipDTOInfo.EMPTY_PARENT_ID, RelationshipDTOInfo.EMPTY_CHILD_ID, type);
    }
    else if (mdClassIF instanceof MdStructDAOIF)
    {
      return newStruct(type);
    }
    else
    {
      String message = "The type [" + type + "] does not represent a [" + Entity.class.getName() + "] class.";
      throw new UnexpectedTypeException(message);
    }
  }

  /**
   * Given a Business object, this method returns a list of all possible transitions (transition names) available to that object in its current state. If the object does not partake in a state machine
   * or if there are no possible transitions available, then an empty list is returned.
   * 
   * @param business
   * @return
   */
  public static List<String> getTransitions(Business business)
  {
    List<String> transitions = new LinkedList<String>();

    // check for a state machine and the transitions/states
    MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO(business.getType());
    if (mdBusiness.hasStateMachine())
    {
      MdStateMachineDAOIF mdStateMachine = mdBusiness.definesMdStateMachine();
      for (StateMasterDAOIF stateMasterIF : mdStateMachine.definesStateMasters())
      {
        String stateName = stateMasterIF.getValue(StateMasterDAOIF.STATE_NAME);

        if (stateName.equals(business.getState()))
        {
          for (TransitionDAOIF transition : mdStateMachine.definesTransitions(stateMasterIF))
          {
            String name = transition.getName();

            transitions.add(name);
          }

          break;
        }
      }
    }

    return transitions;
  }

  /**
   * Returns the current state of the mutable object if there is one. If the mutable object does not have a state then null is returned
   * 
   * @param mutable
   *          The mutable object to get state
   * @return The current state of the mutable object, or null
   */
  public static StateMasterDAOIF getState(Mutable mutable)
  {
    if (mutable instanceof Business)
    {
      // Get the current state of the BusinessDAO
      Business business = (Business) mutable;

      return BusinessFacade.currentState(business);
    }

    return null;
  }

  public static StateMasterDAOIF getSink(Entity entity, String transitionName)
  {
    StateMasterDAOIF source = BusinessFacade.getState(entity);

    if (source == null)
    {
      return null;
    }

    // Get the sink state
    try
    {
      return source.getNextState(transitionName);
    }
    catch (DataNotFoundException e)
    {
      return null;
    }
  }

  /**
   * Sets the locale on the {@link SmartException}.
   * 
   * @param smartException
   * @param locale
   */
  public static void setSmartExceptionLocale(SmartException smartException, Locale locale)
  {
    smartException.setLocale(locale);
  }

}
