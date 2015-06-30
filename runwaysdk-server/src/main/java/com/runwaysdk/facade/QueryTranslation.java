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

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.ClassQueryDTO;
import com.runwaysdk.business.ClassQueryDTO.QueryCondition;
import com.runwaysdk.business.ClassQueryDTO.StructOrderBy;
import com.runwaysdk.business.RelationshipQuery;
import com.runwaysdk.business.RelationshipQueryDTO;
import com.runwaysdk.business.StructQuery;
import com.runwaysdk.business.StructQueryDTO;
import com.runwaysdk.business.ViewQueryDTO;
import com.runwaysdk.business.generation.ViewQueryStubAPIGenerator;
import com.runwaysdk.constants.AdminConstants;
import com.runwaysdk.constants.QueryConditions;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalTextDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeBoolean;
import com.runwaysdk.query.AttributeLocal;
import com.runwaysdk.query.AttributeMoment;
import com.runwaysdk.query.AttributeNumber;
import com.runwaysdk.query.AttributeReference;
import com.runwaysdk.query.AttributeStruct;
import com.runwaysdk.query.ComponentQuery;
import com.runwaysdk.query.Condition;
import com.runwaysdk.query.ConditionOperator;
import com.runwaysdk.query.GeneratedViewQuery;
import com.runwaysdk.query.QueryException;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.SelectableChar;
import com.runwaysdk.query.SelectableNumber;
import com.runwaysdk.query.SelectablePrimitive;
import com.runwaysdk.query.ValueQuery;

public class QueryTranslation
{
  /**
   * Hard-coded struct order by to indicate that the query should order by the
   * localized value of a local attribute
   */
  public static final String LOCALIZE = "LOCALIZE";

  public static StructQuery buildStructQuery(StructQueryDTO queryDTO)
  {
    // create a new StructQuery
    QueryFactory factory = new QueryFactory();
    StructQuery query = factory.structQuery(queryDTO.getType());

    buildQueryConditions(queryDTO, query);
    buildOrderBys(queryDTO, query);

    return query;
  }

  /**
   * Builds the query conditions on an EntityQuery given a QueryDTO.
   * 
   * @param query
   */
  private static void buildQueryConditions(ClassQueryDTO queryDTO, ComponentQuery query)
  {
    // get the attribute metadata the type defines
    MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(queryDTO.getType());
    Map<String, ? extends MdAttributeDAOIF> mdAttributeMap = mdClass.getAllDefinedMdAttributeMap();

    // Check for a JSON query, which is used by the admin page
    String jsonQuery = queryDTO.getJsonQuery();
    if (jsonQuery != null && jsonQuery.trim().length() > 0)
    {
      try
      {
        JSONArray criteria = new JSONArray(jsonQuery);
        for (int i = 0; i < criteria.length(); i++)
        {
          JSONObject criterion = criteria.getJSONObject(i);

          String operator = criterion.getString(AdminConstants.JSON_QUERY_OPERATOR);
          String attribute = criterion.getString(AdminConstants.JSON_QUERY_ATTRIBUTE);
          String condition = criterion.getString(AdminConstants.JSON_QUERY_CONDITION);
          String value = criterion.getString(AdminConstants.JSON_QUERY_VALUE);

          MdAttributeDAOIF mdAttribute = mdAttributeMap.get(attribute.toLowerCase());

          Condition translatedCondition = translateCondition(query, mdAttribute, condition, value);

          if (operator.equalsIgnoreCase(ConditionOperator.AND.name()))
          {
            query.AND(translatedCondition);
          }
          else if (operator.equalsIgnoreCase(ConditionOperator.OR.name()))
          {
            query.OR(translatedCondition);
          }
          else
          {
            // invalid operator specified!
            String error = "The operator [" + operator + "] is invalid for a JSON query.";
            throw new ProgrammingErrorException(error);
          }
        }
      }
      catch (JSONException e)
      {
        String error = "Invalid JSON query on QueryDTO";
        throw new ProgrammingErrorException(error);
      }
    }
    // use the query conditions on the QueryDTO
    else
    {
      // add conditions to the query
      List<QueryCondition> queryConditions = queryDTO.getConditions();
      for (QueryCondition queryCondition : queryConditions)
      {
        String attributeName = queryCondition.getAttribute();
        String condition = queryCondition.getCondition();
        String conditionValue = queryCondition.getConditionValue();
        Condition translateCondition;

        if (queryDTO instanceof RelationshipQueryDTO && attributeName.equals(RelationshipInfo.PARENT_ID))
        {
          RelationshipQuery relationshipQuery = (RelationshipQuery) query;
          translateCondition = translateCharCondition(relationshipQuery.parentId(), condition, conditionValue);
        }
        else if (queryDTO instanceof RelationshipQueryDTO && attributeName.equals(RelationshipInfo.CHILD_ID))
        {
          RelationshipQuery relationshipQuery = (RelationshipQuery) query;
          translateCondition = translateCharCondition(relationshipQuery.childId(), condition, conditionValue);
        }
        else
        {
          // get the MdAttribute defining the attribute in the condition
          MdAttributeDAOIF mdAttribute = mdAttributeMap.get(attributeName.toLowerCase());
          translateCondition = translateCondition(query, mdAttribute, condition, conditionValue);
        }

        query.WHERE(translateCondition);
      }
    }
  }

  /**
   * Adds the OrderBy data on the ClassQueryDTO to the given EntityQuery.
   * 
   * @param queryDTO
   * @param query
   */
  public static void buildOrderBys(ClassQueryDTO queryDTO, ComponentQuery query)
  {
    // get the attribute metadata the type defines
    MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(queryDTO.getType());
    Map<String, ? extends MdAttributeDAOIF> mdAttributeMap = mdClass.getAllDefinedMdAttributeMap();

    // apply any order by criteria
    for (ClassQueryDTO.OrderBy orderBy : queryDTO.getOrderByList())
    {
      SelectablePrimitive attributePrimitive;

      if (orderBy instanceof StructOrderBy)
      {
        StructOrderBy structOrderBy = (StructOrderBy) orderBy;

        String structName = structOrderBy.getAttributeStruct();
        String attributeName = structOrderBy.getAttribute();

        MdAttributeStructDAOIF mdAttributeStruct = (MdAttributeStructDAOIF) mdAttributeMap.get(structName.toLowerCase());

        if (mdAttributeStruct instanceof MdAttributeLocalDAOIF)
        {
          MdAttributeDAOIF mdAttribute = mdAttributeStruct.getMdStructDAOIF().getAllDefinedMdAttributeMap().get(attributeName.toLowerCase());
          AttributeLocal attributeLocal = QueryTranslation.attributeLocalFactory(query, structName, (MdAttributeLocalDAOIF) mdAttributeStruct);

          if (attributeName.equals(LOCALIZE))
          {
            attributePrimitive = attributeLocal.localize();
          }
          else
          {
            attributePrimitive = attributeLocal.attributePrimitiveFactory(mdAttribute.definesAttribute(), mdAttribute.getType());
          }

        }
        else
        {
          MdAttributeDAOIF mdAttribute = mdAttributeStruct.getMdStructDAOIF().getAllDefinedMdAttributeMap().get(attributeName.toLowerCase());
          AttributeStruct attributeStruct = query.aStruct(structName);

          attributePrimitive = attributeStruct.attributePrimitiveFactory(mdAttribute.definesAttribute(), mdAttribute.getType());
        }
      }
      else
      {
        MdAttributeDAOIF mdAttribute = mdAttributeMap.get(orderBy.getAttribute().toLowerCase());
        attributePrimitive = query.getPrimitive(mdAttribute.definesAttribute());
      }

      query.ORDER_BY(attributePrimitive, com.runwaysdk.query.OrderBy.SortOrder.getSortOrder(orderBy.getOrder()));
    }
  }

  /**
   * @param query
   * @param attributeName
   * @param mdAttribute
   * @return
   */
  private static AttributeLocal attributeLocalFactory(ComponentQuery query, String attributeName, MdAttributeLocalDAOIF mdAttribute)
  {
    if (mdAttribute instanceof MdAttributeLocalCharacterDAOIF)
    {
      return query.aLocalCharacter(attributeName);
    }
    else if (mdAttribute instanceof MdAttributeLocalTextDAOIF)
    {
      return query.aLocalText(attributeName);
    }

    throw new ProgrammingErrorException("Component query does not support the local attribute type [" + mdAttribute.getClass().getName() + "].");
  }

  /**
   * Builds a GeneratedViewQuery object from a QueryDTO object.
   * 
   * @param queryDTO
   *          The QueryDTO object to translate.
   * @return GeneratedViewQuery instance representing the input QueryDTO
   */
  public static GeneratedViewQuery buildViewQuery(ViewQueryDTO queryDTO)
  {
    // create a new BusinessQuery
    QueryFactory factory = new QueryFactory();

    GeneratedViewQuery generatedViewQuery = null;

    try
    {
      String queryType = ViewQueryStubAPIGenerator.getQueryStubClass(queryDTO.getType());
      Class<?> queryClass = LoaderDecorator.load(queryType);
      generatedViewQuery = (GeneratedViewQuery) queryClass.getConstructor(QueryFactory.class).newInstance(factory);
    }
    catch (IllegalArgumentException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (SecurityException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (InstantiationException e)
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

    // Translate the conditions to the value query
    ValueQuery valueQuery = generatedViewQuery.getComponentQuery();

    buildQueryConditions(queryDTO, valueQuery);
    buildOrderBys(queryDTO, valueQuery);

    return generatedViewQuery;
  }

  /**
   * Builds a BusinessQuery object from a QueryDTO object.
   * 
   * @param queryDTO
   *          The QueryDTO object to translate.
   * @return BusinessQuery instance representing the input QueryDTO
   */
  public static BusinessQuery buildBusinessQuery(BusinessQueryDTO queryDTO)
  {
    // create a new BusinessQuery
    QueryFactory factory = new QueryFactory();
    BusinessQuery query = factory.businessQuery(queryDTO.getType());

    buildQueryConditions(queryDTO, query);
    buildOrderBys(queryDTO, query);

    return query;
  }

  /**
   * Builds a RelationshipQuery object from a QueryDTO object.
   * 
   * @param queryDTO
   * @return
   */
  public static RelationshipQuery buildRelationshipQuery(RelationshipQueryDTO queryDTO)
  {
    // create a new RelationshipQuery
    QueryFactory factory = new QueryFactory();
    RelationshipQuery query = factory.relationshipQuery(queryDTO.getType());

    buildQueryConditions(queryDTO, query);
    buildOrderBys(queryDTO, query);

    return query;
  }

  /**
   * Translates a single condition from a QueryDTO into a condition for use in
   * the Query API.
   * 
   * @param query
   * @param mdAttribute
   * @param condition
   * @param conditionValue
   * @return
   */
  private static Condition translateCondition(ComponentQuery query, MdAttributeDAOIF mdAttribute, String condition, String conditionValue)
  {
    Attribute attribute = null;

    if (query instanceof ValueQuery)
    {
      attribute = (Attribute) ( (ValueQuery) query ).getSelectableRef(mdAttribute.definesAttribute());
    }
    else
    {
      attribute = query.get(mdAttribute.definesAttribute());
    }

    if (attribute instanceof SelectableChar)
    {
      return translateCharCondition((SelectableChar) attribute, condition, conditionValue);
    }
    else if (attribute instanceof SelectableNumber)
    {
      return translateNumberCondition((SelectableNumber) attribute, condition, conditionValue);
    }
    else if (attribute instanceof AttributeBoolean)
    {
      return translateBooleanCondition(attribute, condition, conditionValue);
    }
    else if (attribute instanceof AttributeMoment)
    {
      return translateMomentCondition(attribute, condition, conditionValue);
    }
    else if (attribute instanceof AttributeReference)
    {
      return translateReferenceCondition(attribute, condition, conditionValue);
    }
    else
    {
      String message = "A query cannot be translated for attribute [" + mdAttribute.definesAttribute() + "] of type " + "[" + mdAttribute.getType() + "].";
      throw new QueryException(message);
    }
  }

  /**
   * Translates conditions specific to AttributeChar objects.
   * 
   * @param attribute
   * @param condition
   * @param conditionValue
   * @return
   */
  private static Condition translateCharCondition(SelectableChar attribute, String condition, String conditionValue)
  {
    // Use LIKE if the conditionValue contains a wildcard. Otherwise, use EQUALS
    SelectableChar attributeCharIF = (SelectableChar) attribute;

    if (condition.equals(QueryConditions.EQUALS))
    {
      return attributeCharIF.EQ(conditionValue);
    }
    else if (condition.equals(QueryConditions.EQUALS_IGNORE_CASE))
    {
      return attributeCharIF.EQi(conditionValue);
    }
    else if (condition.equals(QueryConditions.NOT_EQUALS))
    {
      return attributeCharIF.NE(conditionValue);
    }
    else if (condition.equals(QueryConditions.NOT_EQUALS_IGNORE_CASE))
    {
      return attributeCharIF.NEi(conditionValue);
    }
    else if (condition.equals(QueryConditions.LIKE))
    {
      return attributeCharIF.LIKE(conditionValue);
    }
    else if (condition.equals(QueryConditions.LIKE_IGNORE_CASE))
    {
      return attributeCharIF.LIKEi(conditionValue);
    }
    else if (condition.equals(QueryConditions.NOT_LIKE))
    {
      return attributeCharIF.NLIKE(conditionValue);
    }
    else if (condition.equals(QueryConditions.NOT_LIKE_IGNORE_CASE))
    {
      return attributeCharIF.NLIKEi(conditionValue);
    }
    else if (condition.equals(QueryConditions.IN))
    {
      return attributeCharIF.IN(conditionValue);
    }
    else if (condition.equals(QueryConditions.IN_IGNORES_CASE))
    {
      return attributeCharIF.INi(conditionValue);
    }
    else if (condition.equals(QueryConditions.NOT_IN))
    {
      return attributeCharIF.NI(conditionValue);
    }
    else if (condition.equals(QueryConditions.NOT_IN_IGNORES_CASE))
    {
      return attributeCharIF.NIi(conditionValue);
    }
    else
    {
      String message = "A query cannot translate the condition [" + condition + "] for attribute characters.";
      throw new QueryException(message);
    }
  }

  /**
   * Translates conditions specific to AttributeNumber objects.
   * 
   * @param attribute
   * @param condition
   * @param conditionValue
   * @return
   */
  private static Condition translateNumberCondition(SelectableNumber attribute, String condition, String conditionValue)
  {
    if (condition.equals(QueryConditions.EQUALS))
    {
      return ( (AttributeNumber) attribute ).EQ(conditionValue);
    }
    if (condition.equals(QueryConditions.NOT_EQUALS))
    {
      return ( (AttributeNumber) attribute ).NE(conditionValue);
    }
    else if (condition.equals(QueryConditions.LT))
    {
      return ( (AttributeNumber) attribute ).LT(conditionValue);
    }
    else if (condition.equals(QueryConditions.GT))
    {
      return ( (AttributeNumber) attribute ).GT(conditionValue);
    }
    else if (condition.equals(QueryConditions.LT_EQ))
    {
      return ( (AttributeNumber) attribute ).LE(conditionValue);
    }
    else if (condition.equals(QueryConditions.GT_EQ))
    {
      return ( (AttributeNumber) attribute ).GE(conditionValue);
    }
    else
    {
      String message = "A query cannot translate the condition [" + condition + "] for attribute numbers.";
      throw new QueryException(message);
    }
  }

  /**
   * Translates conditions specific to AttributeMoment objects.
   * 
   * @param attribute
   * @param condition
   * @param conditionValue
   * @return
   */
  private static Condition translateMomentCondition(Attribute attribute, String condition, String conditionValue)
  {
    if (condition.equals(QueryConditions.EQUALS))
    {
      return ( (AttributeMoment) attribute ).EQ(conditionValue);
    }
    if (condition.equals(QueryConditions.NOT_EQUALS))
    {
      return ( (AttributeMoment) attribute ).NE(conditionValue);
    }
    else if (condition.equals(QueryConditions.LT))
    {
      return ( (AttributeMoment) attribute ).LT(conditionValue);
    }
    else if (condition.equals(QueryConditions.GT))
    {
      return ( (AttributeMoment) attribute ).GT(conditionValue);
    }
    else if (condition.equals(QueryConditions.LT_EQ))
    {
      return ( (AttributeMoment) attribute ).LE(conditionValue);
    }
    else if (condition.equals(QueryConditions.GT_EQ))
    {
      return ( (AttributeMoment) attribute ).GE(conditionValue);
    }
    else
    {
      String message = "A query cannot translate the condition [" + condition + "] for attribute moments.";
      throw new QueryException(message);
    }
  }

  /**
   * Translates conditions specific to AttributeBoolean objects.
   * 
   * @param attribute
   * @param condition
   * @param conditionValue
   * @return
   */
  private static Condition translateBooleanCondition(Attribute attribute, String condition, String conditionValue)
  {
    if (condition.equals(QueryConditions.EQUALS))
    {
      return ( (AttributeBoolean) attribute ).EQ(conditionValue);
    }
    if (condition.equals(QueryConditions.NOT_EQUALS))
    {
      return ( (AttributeBoolean) attribute ).NE(conditionValue);
    }
    else
    {
      String message = "A query cannot translate the condition [" + condition + "] for attribute booleans.";
      throw new QueryException(message);
    }
  }

  /**
   * Translates conditions specific to AttributeReference objects.
   * 
   * @param attribute
   * @param condition
   * @param conditionValue
   * @return
   */
  private static Condition translateReferenceCondition(Attribute attribute, String condition, String conditionValue)
  {
    if (condition.equals(QueryConditions.EQUALS))
    {
      return ( (AttributeReference) attribute ).EQ(conditionValue);
    }
    if (condition.equals(QueryConditions.NOT_EQUALS))
    {
      return ( (AttributeReference) attribute ).NE(conditionValue);
    }
    else
    {
      String message = "A query cannot translate the condition [" + condition + "] for attribute reference.";
      throw new QueryException(message);
    }
  }
}
