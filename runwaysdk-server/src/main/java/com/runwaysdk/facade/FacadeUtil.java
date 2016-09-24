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
package com.runwaysdk.facade;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.RunwayException;
import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.BusinessEnumeration;
import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.ClassQueryDTO;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.business.Entity;
import com.runwaysdk.business.EnumDTO;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.business.Relationship;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.business.RelationshipQuery;
import com.runwaysdk.business.RelationshipQueryDTO;
import com.runwaysdk.business.SmartException;
import com.runwaysdk.business.Struct;
import com.runwaysdk.business.StructQuery;
import com.runwaysdk.business.StructQueryDTO;
import com.runwaysdk.business.ValueObjectDTO;
import com.runwaysdk.business.ValueQueryDTO;
import com.runwaysdk.business.View;
import com.runwaysdk.business.ViewQueryDTO;
import com.runwaysdk.business.generation.EntityQueryAPIGenerator;
import com.runwaysdk.business.generation.ViewQueryStubAPIGenerator;
import com.runwaysdk.business.generation.dto.ComponentDTOGenerator;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.business.ontology.TermAndRel;
import com.runwaysdk.business.ontology.TermAndRelDTO;
import com.runwaysdk.business.ontology.TermDTO;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.query.ComponentQuery;
import com.runwaysdk.query.GeneratedBusinessQuery;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.GeneratedRelationshipQuery;
import com.runwaysdk.query.GeneratedStructQuery;
import com.runwaysdk.query.GeneratedViewQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OrderBy;
import com.runwaysdk.query.OrderBy.SortOrder;
import com.runwaysdk.query.QueryException;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableSQLNumber;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.query.ViewIterator;
import com.runwaysdk.query.sql.MdAttributeConcrete_SQL;
import com.runwaysdk.session.PermissionFacade;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.conversion.ComponentIFtoComponentDTOIF;
import com.runwaysdk.transport.conversion.ConversionException;
import com.runwaysdk.transport.conversion.ValueObjectToValueObjectDTO;
import com.runwaysdk.transport.conversion.business.ClassToQueryDTO;
import com.runwaysdk.transport.conversion.business.MutableDTOToMutable;
import com.runwaysdk.transport.conversion.business.TermToTermDTO;

public class FacadeUtil
{

  public static Mutable populateMutable(String sessionId, MutableDTO mutableDTO)
  {
    MutableDTOToMutable dtoToComponent = MutableDTOToMutable.getConverter(sessionId, mutableDTO);
    Mutable component = dtoToComponent.populate();

    return component;
  }

  /**
   * Populates the provided Entity object with the data in the MutableDTO
   * object. The Component is then applied to persist the data.
   * 
   * @param ENTITY
   *          The Component object to populate and save.
   * @param entityDTO
   *          The MutableDTO object containing the information.
   * @throws Throwable
   */
  public static Mutable populateMutableAndApply(String sessionId, MutableDTO mutableDTO)
  {
    MutableDTOToMutable dtoToComponent = MutableDTOToMutable.getConverter(sessionId, mutableDTO);
    Mutable component = dtoToComponent.populate();

    if (dtoToComponent.isTypeSafe())
    {
      try
      {
        Class<?> clazz = LoaderDecorator.load(component.getType());
        clazz.getMethod("apply").invoke(component);
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
          SmartException smartEx = (SmartException) e.getTargetException();
          throw smartEx;
        }
        else
        {
          throw new ProgrammingErrorException(e);
        }
      }
      catch (NoSuchMethodException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }
    else
    {
      component.apply();
    }

    return component;
  }

  /**
   * Creates and populates an ComponentDTOIF based on the information in the
   * provided Entity.
   * 
   * @param sessionId
   * @param entity
   *          The Entity containing the required data to create an EntityDTO.
   * @param convertMetaData
   * @return An EntityDTO object representing the provided Entity object.
   */
  public static ComponentDTOIF populateComponentDTOIF(String sessionId, ComponentIF componentIF, boolean convertMetaData)
  {
    ComponentIFtoComponentDTOIF componentToDTO = ComponentIFtoComponentDTOIF.getConverter(sessionId, componentIF, convertMetaData);

    return componentToDTO.populate();
  }

  public static List<BusinessDTO> buildBusinessDTOListFromBusinesses(String sessionId, List<? extends Business> objects)
  {
    List<BusinessDTO> returnList = new LinkedList<BusinessDTO>();
    for (Entity entity : objects)
    {
      returnList.add((BusinessDTO) FacadeUtil.populateComponentDTOIF(sessionId, entity, true));
    }

    return returnList;
  }

  public static List<RelationshipDTO> buildRelationshipDTOListFromRelationships(String sessionId, List<? extends Relationship> objects)
  {
    List<RelationshipDTO> returnList = new LinkedList<RelationshipDTO>();
    for (Entity entity : objects)
    {
      returnList.add((RelationshipDTO) FacadeUtil.populateComponentDTOIF(sessionId, entity, true));
    }

    return returnList;
  }

  public static ValueQueryDTO populateValueQueryDTOWithValueQueryResults(String sessionId, ValueQuery valueQuery)
  {
    ValueQueryDTO valueQueryDTO = new ValueQueryDTO(null);

    copyToValueQueryDTO(sessionId, valueQuery, valueQueryDTO);

    return valueQueryDTO;
  }

  /**
   * Copies the information from the source ValueQuery to the destination
   * ValueQueryDTO.
   * 
   * @param valueQuery
   */
  private static void copyToValueQueryDTO(String sessionId, ValueQuery valueQuery, ValueQueryDTO valueQueryDTO)
  {
    Boolean hasTypeReadAccess = true;
    // Key: MdAttribute id Value: indicates if the user has read permission on
    // the attribute.
    Map<String, Boolean> attrReadPermissionMap = new HashMap<String, Boolean>();

    List<Selectable> selectableList = valueQuery.getSelectableRefs();

    // Key: MdAttribute.getId(), Value: Defining MdEntityIF
    Map<String, MdClassDAOIF> definesAttributeMap = new HashMap<String, MdClassDAOIF>();

    // Permission checking is at the type level only. Build a permission map for
    // the entire
    // set of attributes.
    for (Selectable selectable : selectableList)
    {
      MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

      // Skip the permission check if the attribute is derived from a custom SQL
      // string
      if (! ( mdAttributeIF instanceof MdAttributeConcrete_SQL ))
      {
        MdClassDAOIF mdClassIF = definesAttributeMap.get(mdAttributeIF.getId());

        if (mdClassIF == null)
        {
          // Pass through SQL objects do not have type metadata
          mdClassIF = mdAttributeIF.definedByClass();
          if (mdClassIF != null)
          {
            definesAttributeMap.put(mdAttributeIF.getId(), mdClassIF);
          }
        }

        if (!PermissionFacade.checkTypeReadAccess(sessionId, mdClassIF))
        {
          hasTypeReadAccess = false;
        }

        boolean hasAttributeReadAccess = true;

        for (MdAttributeConcreteDAOIF someMdAttributeDAOIF : selectable.getAllEntityMdAttributes())
        {
          if (!PermissionFacade.checkAttributeTypeReadAccess(sessionId, someMdAttributeDAOIF))
          {
            hasAttributeReadAccess = false;
            break;
          }
        }

        attrReadPermissionMap.put(mdAttributeIF.getId(), hasAttributeReadAccess);
      }
    }

    OIterator<ValueObject> iterator;

    if (valueQueryDTO.getPageSize() != 0 && valueQueryDTO.getPageNumber() != 0)
    {
      iterator = valueQuery.getIterator(valueQueryDTO.getPageSize(), valueQueryDTO.getPageNumber());
    }
    else
    {
      iterator = valueQuery.getIterator();
    }

    try
    {
      for (ValueObject valueObject : iterator)
      {
        ValueObjectToValueObjectDTO converter = new ValueObjectToValueObjectDTO(sessionId, valueObject, hasTypeReadAccess, attrReadPermissionMap);
        ValueObjectDTO valueObjectDTO = converter.populate();
        ComponentDTOFacade.addResultItemToQueryDTO(valueQueryDTO, valueObjectDTO);
      }
    }
    finally
    {
      iterator.close();
    }

    // Populate the ValueQueryDTO by converting the Selectables into
    // AttributeDTOs.
    // Note that this step MUST occur after the ValueObjects have been queries.
    for (Selectable selectable : selectableList)
    {
      AttributeDTO attributeDTO = ValueObjectToValueObjectDTO.convertSelectable(sessionId, selectable);
      ComponentDTOFacade.addAttributeQueryDTO(valueQueryDTO, attributeDTO);
    }

    // set the count
    if (valueQueryDTO.getResultSet().size() > 0 && valueQuery.hasCountSelectable())
    {
      SelectableSQLNumber countSelectable = valueQuery.getCountSelectable();

      // we know we have at least one row which will contain a column with the
      // count value.
      ValueObjectDTO obj = valueQueryDTO.getResultSet().get(0);
      String countStr = obj.getValue(countSelectable.getUserDefinedAlias());
      valueQueryDTO.setCount(Long.parseLong(countStr));
    }
    else
    {
      valueQueryDTO.setCount(valueQuery.getCount());
    }
  }

  public static void populateQueryDTOWithViewResults(String sessionId, ViewQueryDTO queryDTO)
  {
    // translate the query
    GeneratedViewQuery query = QueryTranslation.buildViewQuery(queryDTO);

    populateQueryDTOWithViewResults(sessionId, query, queryDTO);
  }

  @SuppressWarnings("unchecked")
  public static void populateQueryDTOWithViewResults(String sessionId, GeneratedViewQuery generatedViewQuery, ViewQueryDTO queryDTO)
  {
    // set the count (if enabled)
    if (queryDTO.isCountEnabled())
    {
      queryDTO.setCount(generatedViewQuery.getCount());
    }

    String queryType = ViewQueryStubAPIGenerator.getQueryStubClass(queryDTO.getType());
    Class<?> queryClass = LoaderDecorator.load(queryType);

    ViewIterator<View> iterator = null;
    try
    {
      if (queryDTO.getPageSize() != 0 && queryDTO.getPageNumber() != 0)
      {
        generatedViewQuery.restrictRows(queryDTO.getPageSize(), queryDTO.getPageNumber());
      }

      iterator = (ViewIterator<View>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(generatedViewQuery);
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
      throw new ProgrammingErrorException(e);
    }
    catch (NoSuchMethodException e)
    {
      throw new ProgrammingErrorException(e);
    }

    try
    {
      while (iterator.hasNext())
      {
        View view = iterator.next();
        if (PermissionFacade.checkReadAccess(sessionId, view))
        {
          ComponentDTOFacade.addResultItemToQueryDTO(queryDTO, FacadeUtil.populateComponentDTOIF(sessionId, view, false));
        }
      }
    }
    finally
    {
      iterator.close();
    }
  }

  public static void populateQueryDTOWithBusinessResults(String sessionId, BusinessQueryDTO queryDTO)
  {
    // translate the query
    BusinessQuery query = QueryTranslation.buildBusinessQuery(queryDTO);

    populateQueryDTOWithBusinessResults(sessionId, query, queryDTO);
  }

  public static void populateQueryDTOWithBusinessResults(String sessionId, BusinessQuery query, BusinessQueryDTO queryDTO)
  {
    // set the count (if enabled)
    if (queryDTO.isCountEnabled())
    {
      queryDTO.setCount(query.getCount());
    }

    OIterator<Business> iterator;
    if (queryDTO.getPageSize() != 0 && queryDTO.getPageNumber() != 0)
    {
      iterator = query.getIterator(queryDTO.getPageSize(), queryDTO.getPageNumber());
    }
    else
    {
      iterator = query.getIterator();
    }

    try
    {
      for (Business business : iterator)
      {
        if (PermissionFacade.checkReadAccess(sessionId, business))
        {
          ComponentDTOFacade.addResultItemToQueryDTO(queryDTO, FacadeUtil.populateComponentDTOIF(sessionId, business, false));
        }
      }
    }
    finally
    {
      iterator.close();
    }
  }

  @SuppressWarnings("rawtypes")
  public static void populateQueryDTOWithBusinessResults(String sessionId, GeneratedBusinessQuery generatedBusinessQuery, BusinessQueryDTO queryDTO)
  {
    // set the count (if enabled)
    if (queryDTO.isCountEnabled())
    {
      queryDTO.setCount(generatedBusinessQuery.getCount());
    }

    String queryType = EntityQueryAPIGenerator.getQueryClass(queryDTO.getType());
    Class<?> queryClass = LoaderDecorator.load(queryType);

    OIterator iterator = null;

    try
    {
      iterator = (OIterator) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(generatedBusinessQuery);
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
      throw new ProgrammingErrorException(e);
    }
    catch (NoSuchMethodException e)
    {
      throw new ProgrammingErrorException(e);
    }

    try
    {
      while (iterator.hasNext())
      {
        Business business = (Business) iterator.next();
        if (PermissionFacade.checkReadAccess(sessionId, business))
        {
          ComponentDTOFacade.addResultItemToQueryDTO(queryDTO, FacadeUtil.populateComponentDTOIF(sessionId, business, false));
        }
      }
    }
    finally
    {
      iterator.close();
    }
  }

  public static void populateQueryDTOWithStructResults(String sessionId, StructQueryDTO queryDTO)
  {
    // translate the query
    StructQuery query = QueryTranslation.buildStructQuery(queryDTO);

    populateQueryDTOWithStructResults(sessionId, query, queryDTO);
  }

  public static void populateQueryDTOWithStructResults(String sessionId, StructQuery query, StructQueryDTO queryDTO)
  {

    // set the count (if enabled)
    if (queryDTO.isCountEnabled())
    {
      queryDTO.setCount(query.getCount());
    }

    OIterator<Struct> iterator;
    if (queryDTO.getPageSize() != 0 && queryDTO.getPageNumber() != 0)
    {
      iterator = query.getIterator(queryDTO.getPageSize(), queryDTO.getPageNumber());
    }
    else
    {
      iterator = query.getIterator();
    }

    try
    {
      for (Struct struct : iterator)
      {
        if (PermissionFacade.checkReadAccess(sessionId, struct))
        {
          ComponentDTOFacade.addResultItemToQueryDTO(queryDTO, FacadeUtil.populateComponentDTOIF(sessionId, struct, false));
        }
      }
    }
    finally
    {
      iterator.close();
    }
  }


  @SuppressWarnings("rawtypes")
  public static void populateQueryDTOWithStructResults(String sessionId, GeneratedStructQuery generatedStructQuery, StructQueryDTO queryDTO)
  {
    // set the count (if enabled)
    if (queryDTO.isCountEnabled())
    {
      queryDTO.setCount(generatedStructQuery.getCount());
    }

    String queryType = EntityQueryAPIGenerator.getQueryClass(queryDTO.getType());
    Class<?> queryClass = LoaderDecorator.load(queryType);

    OIterator iterator = null;

    try
    {
      iterator = (OIterator) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(generatedStructQuery);
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
      throw new ProgrammingErrorException(e);
    }
    catch (NoSuchMethodException e)
    {
      throw new ProgrammingErrorException(e);
    }

    try
    {
      while (iterator.hasNext())
      {
        Struct struct = (Struct) iterator.next();
        if (PermissionFacade.checkReadAccess(sessionId, struct))
        {
          ComponentDTOFacade.addResultItemToQueryDTO(queryDTO, FacadeUtil.populateComponentDTOIF(sessionId, struct, false));
        }
      }
    }
    finally
    {
      iterator.close();
    }
  }

  public static void populateQueryDTOWithRelationshipResults(String sessionId, RelationshipQueryDTO queryDTO)
  {
    // translate the query
    RelationshipQuery query = QueryTranslation.buildRelationshipQuery(queryDTO);

    populateQueryDTOWithRelationshipResults(sessionId, query, queryDTO);
  }

  public static void populateQueryDTOWithRelationshipResults(String sessionId, RelationshipQuery query, RelationshipQueryDTO queryDTO)
  {
    // set the count (if enabled)
    if (queryDTO.isCountEnabled())
    {
      queryDTO.setCount(query.getCount());
    }

    OIterator<Relationship> iterator;
    if (queryDTO.getPageSize() != 0 && queryDTO.getPageNumber() != 0)
    {
      iterator = query.getIterator(queryDTO.getPageSize(), queryDTO.getPageNumber());
    }
    else
    {
      iterator = query.getIterator();
    }

    try
    {
      for (Relationship relationshipObject : iterator)
      {
        if (PermissionFacade.checkReadAccess(sessionId, relationshipObject))
        {
          ComponentDTOFacade.addResultItemToQueryDTO(queryDTO, FacadeUtil.populateComponentDTOIF(sessionId, relationshipObject, false));
        }
      }
    }
    finally
    {
      iterator.close();
    }
  }

  public static void populateQueryDTOWithRelationshipResults(String sessionId, GeneratedRelationshipQuery generatedRelationshipQuery, RelationshipQueryDTO queryDTO)
  {
    // set the count (if enabled)
    if (queryDTO.isCountEnabled())
    {
      queryDTO.setCount(generatedRelationshipQuery.getCount());
    }

    String queryType = EntityQueryAPIGenerator.getQueryClass(queryDTO.getType());
    Class<?> queryClass = LoaderDecorator.load(queryType);

    OIterator<?> iterator = null;

    try
    {
      iterator = (OIterator<?>) queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(generatedRelationshipQuery);
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
      throw new ProgrammingErrorException(e);
    }
    catch (NoSuchMethodException e)
    {
      throw new ProgrammingErrorException(e);
    }

    try
    {
      while (iterator.hasNext())
      {
        Relationship relationship = (Relationship) iterator.next();
        if (PermissionFacade.checkReadAccess(sessionId, relationship))
        {
          ComponentDTOFacade.addResultItemToQueryDTO(queryDTO, FacadeUtil.populateComponentDTOIF(sessionId, relationship, false));
        }
      }
    }
    finally
    {
      iterator.close();
    }
  }

  /**
   * 
   * @param sessionId
   * @param mutableDTO
   * @param lockEntity
   * @return
   */
  public static Mutable populateComponent(String sessionId, MutableDTO mutableDTO)
  {
    MutableDTOToMutable dtoToComponent = MutableDTOToMutable.getConverter(sessionId, mutableDTO);
    return dtoToComponent.populate();
  }

  /**
   * Converts EntityDTOs into EntityDAOs. If the object is not a DTO then the
   * method just adds it to the returned array without changing anything.
   * 
   * @param objects
   * @return
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   * @throws NoSuchMethodException
   * @throws IllegalArgumentException
   * @throws SecurityException
   */
  public static Object[] convertDTOstoTypes(String sessionId, String[] parameterTypes, Object[] objects)
  {

    Object[] converted = new Object[objects.length];

    for (int i = 0; i < objects.length; i++)
    {
      converted[i] = convertDTOToType(sessionId, parameterTypes[i], objects[i]);
    }

    return converted;
  }

  /**
   * Converts EntityDTOs into EntityDAOs. If the parameter is a primitive type
   * then the method returns the parameter without changing it. If the object is
   * an array then the array is converted to an arry of EntityDAOs or an array
   * of a primitive type.
   * 
   * @param type
   *          A String representation of the type of the object. Must follow
   *          Java ClassLoader name conventions.
   * @param object
   *          The EntityDTO, Java primitive, or array to convert
   * @return Either an EntityDAO, or a Java primitive, or an array of either
   * 
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   * @throws NoSuchMethodException
   * @throws IllegalArgumentException
   * @throws SecurityException
   */
  public static Object convertDTOToType(String sessionId, String type, Object object)
  {
    if (type == null)
    {
      return null;
    }

    try
    {
      // Ensure that the Classes of the actual parameters are already
      // loaded before trying to convert the parameters.
      Class<?> cls = FacadeUtil.loadClasses(new String[] { type })[0];

      if (cls.isArray())
      {
        Object[] array = (Object[]) object;
        Class<?> componentType = cls.getComponentType();
        Object[] converted = (Object[]) Array.newInstance(componentType, array.length);

        for (int i = 0; i < array.length; i++)
        {
          Object inner = array[i];

          Object convertedArray = convertDTOToType(sessionId, componentType.getName(), inner);
          converted[i] = convertedArray;
        }

        return converted;
      }
      else if (object instanceof MutableDTO)
      {
        MutableDTO mutableDTO = (MutableDTO) object;

        return populateComponent(sessionId, mutableDTO);
      }
      else if (object instanceof EnumDTO)
      {
        EnumDTO enumDTO = (EnumDTO) object;

        BusinessEnumeration businessEnumeration = convertDTOtoEnum(enumDTO);

        return businessEnumeration;
      }
      else if (object instanceof TermAndRelDTO)
      {
        TermAndRelDTO tnr = (TermAndRelDTO) object;

        Term term = (Term) convertDTOToType(sessionId, type, tnr.getTerm());

        return new TermAndRel(term, tnr.getRelationshipType(), tnr.getRelationshipId());
      }
      else
      {
        return object;
      }
    }
    catch (SecurityException e)
    {
      throw new ConversionException(e);
    }
    catch (IllegalArgumentException e)
    {
      throw new ConversionException(e);
    }
  }

  /**
   * Converts an EnumDTO into its BusinessEnumeration
   * 
   * @param enumDTO
   * @return
   */
  private static BusinessEnumeration convertDTOtoEnum(EnumDTO enumDTO)
  {
    try
    {
      String type = enumDTO.getEnumType();
      Class<?> clazz = LoaderDecorator.load(type);
      // int ref = ((GeneratedClassLoader) clazz.getClassLoader()).number;
      return (BusinessEnumeration) clazz.getMethod("valueOf", String.class).invoke(null, enumDTO.getEnumName());
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  /**
   * Converts Business layer classes to generic DTOs. If the parameter is a
   * primitive type then the method returns the parameter without changing it.
   * If the object is an array then the array is converted to an arry of
   * business classes or an array of a primitive type.
   * 
   * @param sessionId
   * @param parameters
   * @return
   */
  public static Object convertTypeToDTO(String sessionId, Object object)
  {
    if (object == null)
    {
      return null;
    }

    if (object.getClass().isArray())
    {
      Object[] parameters = (Object[]) object;
      String type = getGenericDTOArrayType(parameters.getClass());
      Class<?> componentType = LoaderDecorator.load(type).getComponentType();

      Object[] converted = (Object[]) Array.newInstance(componentType, parameters.length);
      for (int i = 0; i < parameters.length; i++)
      {
        Object convertedArray = convertTypeToDTO(sessionId, parameters[i]);
        converted[i] = convertedArray;
      }
      return converted;
    }
    else if (object instanceof Term)
    {
      return new TermToTermDTO(sessionId, (Term) object, true).populate();
    }
    else if (object instanceof BusinessEnumeration)
    {
      return populateEnumDTO(sessionId, (BusinessEnumeration) object);
    }
    else if (object instanceof ComponentIF)
    {
      // Convert the Component into its ComponentDTO representation
      return populateComponentDTOIF(sessionId, (ComponentIF) object, true);
    }
    else if (object instanceof GeneratedComponentQuery)
    {
      return populateQueryDTO(sessionId, (GeneratedComponentQuery) object, true);
    }
    else if (object instanceof ValueQuery)
    {
      return populateValueQueryDTOWithValueQueryResults(sessionId, (ValueQuery) object);
    }
    else if (object instanceof TermAndRel)
    {
      TermAndRel tnr = (TermAndRel) object;

      TermDTO term = (TermDTO) new TermToTermDTO(sessionId, (Term) tnr.getTerm(), true).populate();

      return new TermAndRelDTO(term, tnr.getRelationshipType(), tnr.getRelationshipId());
    }
    else
    {
      return object;
    }
  }

  private static Object populateQueryDTO(String sessionId, GeneratedComponentQuery query, boolean enableCount)
  {
    ClassToQueryDTO typeToQueryDTO = ClassToQueryDTO.getConverter(sessionId, query.getMdClassIF());

    ClassQueryDTO populate = typeToQueryDTO.populate();

    populate.setCountEnabled(enableCount);
    populate.setPageNumber(query.getPageNumber());
    populate.setPageSize(query.getPageSize());

    // Set order by information
    for (OrderBy orderBy : query.getOrderByList())
    {
      Selectable selectable = orderBy.getSelectable();

      MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

      String attribute = mdAttributeIF.definesAttribute();
      String order = ( orderBy.getSortOrder().equals(SortOrder.ASC) ) ? "asc" : "desc";
      String alias = orderBy.getSortAlias();

      if (selectable.getAttribute() != null)
      {
        MdAttributeConcreteDAOIF mdAttributeStructIF = selectable.getAttribute().getMdAttributeStructIF();

        if (mdAttributeStructIF != null)
        {
          populate.addStructOrderBy(mdAttributeStructIF.definesAttribute(), attribute, order, alias);
        }
        else
        {
          populate.addOrderBy(attribute, order, alias);
        }
      }
      else
      {
        populate.addOrderBy(attribute, order, alias);
      }
    }

    if (query instanceof GeneratedBusinessQuery)
    {
      BusinessQueryDTO businessQueryDTO = (BusinessQueryDTO) populate;

      populateQueryDTOWithBusinessResults(sessionId, (GeneratedBusinessQuery) query, businessQueryDTO);

      return businessQueryDTO;
    }
    else if (query instanceof GeneratedRelationshipQuery)
    {
      RelationshipQueryDTO relationshipQueryDTO = (RelationshipQueryDTO) populate;

      populateQueryDTOWithRelationshipResults(sessionId, (GeneratedRelationshipQuery) query, relationshipQueryDTO);

      return relationshipQueryDTO;
    }
    else if (query instanceof GeneratedStructQuery)
    {
      StructQueryDTO structQueryDTO = (StructQueryDTO) populate;

      populateQueryDTOWithStructResults(sessionId, (GeneratedStructQuery) query, structQueryDTO);

      return structQueryDTO;
    }
    else if (query instanceof GeneratedViewQuery)
    {
      ViewQueryDTO viewQueryDTO = (ViewQueryDTO) populate;
      populateQueryDTOWithViewResults(sessionId, (GeneratedViewQuery) query, viewQueryDTO);
      return viewQueryDTO;
    }
    else
    {
      String errMsg = "Invalid genrated query type to convert into a query DTO.";
      throw new QueryException(errMsg);
    }
  }

  private static Object populateEnumDTO(String sessionId, BusinessEnumeration enumeration)
  {
    String name = enumeration.name();
    String type = enumeration.getClass().getName();

    return new EnumDTO(type, name);
  }

  /**
   * Get the Class<?> objects which define an array of Object types
   * 
   * @param types
   * @return
   */
  public static Class<?>[] loadClasses(String[] types)
  {
    Class<?>[] classes = new Class[types.length];

    for (int i = 0; i < types.length; i++)
    {
      classes[i] = LoaderDecorator.load(types[i]);
    }

    return classes;
  }

  private static String getGenericDTOArrayType(Class<?> c)
  {
    Class<?> baseComponent = getBaseComponent(c);
    String baseName = baseComponent.getName();
    String name = c.getName();

    if (BusinessEnumeration.class.isAssignableFrom(baseComponent))
    {
      return name.replace(baseName, EnumDTO.class.getName());
    }
    else if (Mutable.class.isAssignableFrom(baseComponent))
    {
      return name.replace(baseName, MutableDTO.class.getName());
    }
    else if (TermAndRel.class.isAssignableFrom(baseComponent))
    {
      return name.replace(baseName, TermAndRelDTO.class.getName());
    }

    return name;
  }

  /**
   * Returns the base component of an array.
   * 
   * @param c
   * @return
   */
  private static Class<?> getBaseComponent(Class<?> c)
  {
    while (c.isArray())
    {
      c = c.getComponentType();
    }

    return c;
  }

  /**
   * Gets the Java String representation of the DTO class which corresponds to
   * the given type.
   * 
   * @param o
   * @return
   */
  public static String getDTOType(Object o)
  {
    Class<?> baseComponent = FacadeUtil.getBaseComponent(o.getClass());
    String type = o.getClass().getName();

    if (ValueQuery.class.isAssignableFrom(baseComponent))
    {
      return ValueQueryDTO.class.getName();
    }
    else if (ComponentQuery.class.isAssignableFrom(baseComponent))
    {
      type = type.concat(ComponentDTOGenerator.DTO_SUFFIX);
    }
    else if (GeneratedComponentQuery.class.isAssignableFrom(baseComponent) || Mutable.class.isAssignableFrom(baseComponent) || BusinessEnumeration.class.isAssignableFrom(baseComponent) || TermAndRel.class.isAssignableFrom(baseComponent))
    {
      if (o.getClass().isArray())
      {
        type = type.replace(";", ComponentDTOGenerator.DTO_SUFFIX + ";");
      }
      else
      {
        type = type.concat(ComponentDTOGenerator.DTO_SUFFIX);
      }
    }

    return type;
  }
}
