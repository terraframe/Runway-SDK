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
/*
 * Created on August 13, 2004
 */
package com.runwaysdk.dataaccess.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.ArrayUtils;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.rbac.MethodActorDAO;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.SDutyDAO;
import com.runwaysdk.business.rbac.SDutyDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.state.MdStateMachineDAO;
import com.runwaysdk.business.state.StateMasterDAO;
import com.runwaysdk.constants.AndFieldConditionInfo;
import com.runwaysdk.constants.CharacterConditionInfo;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.DatabaseAllPathsStrategyInfo;
import com.runwaysdk.constants.DateConditionInfo;
import com.runwaysdk.constants.DoubleConditionInfo;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.EntityTypes;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.ImportLogInfo;
import com.runwaysdk.constants.LongConditionInfo;
import com.runwaysdk.constants.MdActionInfo;
import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeClobInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDimensionInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFileInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeHashInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalTextInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.MdAttributeMultiTermInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeSymmetricInfo;
import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.MdAttributeVirtualInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdClassDimensionInfo;
import com.runwaysdk.constants.MdControllerInfo;
import com.runwaysdk.constants.MdDimensionInfo;
import com.runwaysdk.constants.MdDomainInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdExceptionInfo;
import com.runwaysdk.constants.MdGraphInfo;
import com.runwaysdk.constants.MdIndexInfo;
import com.runwaysdk.constants.MdInformationInfo;
import com.runwaysdk.constants.MdLocalStructInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdParameterInfo;
import com.runwaysdk.constants.MdProblemInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdStateMachineInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.constants.MdTermRelationshipInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.constants.MdUtilInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.MdWarningInfo;
import com.runwaysdk.constants.MdWebBooleanInfo;
import com.runwaysdk.constants.MdWebBreakInfo;
import com.runwaysdk.constants.MdWebCharacterInfo;
import com.runwaysdk.constants.MdWebCommentInfo;
import com.runwaysdk.constants.MdWebDateInfo;
import com.runwaysdk.constants.MdWebDateTimeInfo;
import com.runwaysdk.constants.MdWebDecimalInfo;
import com.runwaysdk.constants.MdWebDoubleInfo;
import com.runwaysdk.constants.MdWebFloatInfo;
import com.runwaysdk.constants.MdWebFormInfo;
import com.runwaysdk.constants.MdWebGeoInfo;
import com.runwaysdk.constants.MdWebGroupInfo;
import com.runwaysdk.constants.MdWebHeaderInfo;
import com.runwaysdk.constants.MdWebIntegerInfo;
import com.runwaysdk.constants.MdWebLongInfo;
import com.runwaysdk.constants.MdWebMultipleTermInfo;
import com.runwaysdk.constants.MdWebReferenceInfo;
import com.runwaysdk.constants.MdWebSingleTermGridInfo;
import com.runwaysdk.constants.MdWebSingleTermInfo;
import com.runwaysdk.constants.MdWebTextInfo;
import com.runwaysdk.constants.MdWebTimeInfo;
import com.runwaysdk.constants.MethodActorInfo;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.constants.SupportedLocaleInfo;
import com.runwaysdk.constants.TransactionItemInfo;
import com.runwaysdk.constants.TransactionRecordInfo;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.constants.VaultFileInfo;
import com.runwaysdk.constants.VaultInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.DuplicateGraphPathException;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.StaleEntityException;
import com.runwaysdk.dataaccess.UnexpectedTypeException;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeEnumeration;
import com.runwaysdk.dataaccess.cache.CacheAllBusinessDAOstrategy;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.AndFieldConditionDAO;
import com.runwaysdk.dataaccess.metadata.CharacterConditionDAO;
import com.runwaysdk.dataaccess.metadata.DateConditionDAO;
import com.runwaysdk.dataaccess.metadata.DomainTupleDAO;
import com.runwaysdk.dataaccess.metadata.DomainTupleDAOIF;
import com.runwaysdk.dataaccess.metadata.DoubleConditionDAO;
import com.runwaysdk.dataaccess.metadata.LongConditionDAO;
import com.runwaysdk.dataaccess.metadata.MdActionDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeBlobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeClobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDecimalDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFileDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeHashDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiTermDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeStructDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeSymmetricDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTermDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeVirtualDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdControllerDAO;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdDomainDAO;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdExceptionDAO;
import com.runwaysdk.dataaccess.metadata.MdGraphDAO;
import com.runwaysdk.dataaccess.metadata.MdIndexDAO;
import com.runwaysdk.dataaccess.metadata.MdInformationDAO;
import com.runwaysdk.dataaccess.metadata.MdLocalStructDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.dataaccess.metadata.MdProblemDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.dataaccess.metadata.MdTermRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdTreeDAO;
import com.runwaysdk.dataaccess.metadata.MdUtilDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.dataaccess.metadata.MdWarningDAO;
import com.runwaysdk.dataaccess.metadata.MdWebBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdWebBreakDAO;
import com.runwaysdk.dataaccess.metadata.MdWebCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdWebCommentDAO;
import com.runwaysdk.dataaccess.metadata.MdWebDateDAO;
import com.runwaysdk.dataaccess.metadata.MdWebDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdWebDecimalDAO;
import com.runwaysdk.dataaccess.metadata.MdWebDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFormDAO;
import com.runwaysdk.dataaccess.metadata.MdWebGeoDAO;
import com.runwaysdk.dataaccess.metadata.MdWebGroupDAO;
import com.runwaysdk.dataaccess.metadata.MdWebHeaderDAO;
import com.runwaysdk.dataaccess.metadata.MdWebIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdWebLongDAO;
import com.runwaysdk.dataaccess.metadata.MdWebMultipleTermDAO;
import com.runwaysdk.dataaccess.metadata.MdWebReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdWebSingleTermDAO;
import com.runwaysdk.dataaccess.metadata.MdWebSingleTermGridDAO;
import com.runwaysdk.dataaccess.metadata.MdWebTextDAO;
import com.runwaysdk.dataaccess.metadata.MdWebTimeDAO;
import com.runwaysdk.dataaccess.metadata.SupportedLocaleDAO;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAO;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAOIF;
import com.runwaysdk.dataaccess.metadata.ontology.OntologyStrategyDAO;
import com.runwaysdk.dataaccess.transaction.ImportLogDAO;
import com.runwaysdk.dataaccess.transaction.TransactionCache;
import com.runwaysdk.dataaccess.transaction.TransactionCacheIF;
import com.runwaysdk.dataaccess.transaction.TransactionItemDAO;
import com.runwaysdk.dataaccess.transaction.TransactionRecordDAO;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.ColumnInfo;
import com.runwaysdk.query.EntityQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.RelationshipDAOQuery;
import com.runwaysdk.util.IdParser;
import com.runwaysdk.vault.VaultDAO;
import com.runwaysdk.vault.VaultFileDAO;
import com.runwaysdk.vault.WebFileDAO;
import com.runwaysdk.vault.WebFileDAOIF;

/**
 * Most SQL operations to the database concerning BusinessDAOs are performed
 * here. Primarily BusinessDAO CRUD operations to the database that do not
 * require DDL statements.
 * 
 * @author nathan
 * 
 * @version $Revision: 1.6 $
 * @since 1.4
 */
public class BusinessDAOFactory
{
  private static Map<String, BusinessDAO> factory = loadFactory();

  public static void registerDAOTypes(ConcurrentHashMap<String, BusinessDAO> map)
  {
    factory.putAll(map);
  }

  private static Map<String, BusinessDAO> loadFactory()
  {
    Map<String, BusinessDAO> map = new ConcurrentHashMap<String, BusinessDAO>();

    map.put(MdIndexInfo.CLASS, new MdIndexDAO());
    map.put(MdBusinessInfo.CLASS, new MdBusinessDAO());
    map.put(MdTermInfo.CLASS, new MdTermDAO());
    map.put(MdStructInfo.CLASS, new MdStructDAO());
    map.put(MdLocalStructInfo.CLASS, new MdLocalStructDAO());
    map.put(MdStateMachineInfo.CLASS, new MdStateMachineDAO());
    map.put(MdEnumerationInfo.CLASS, new MdEnumerationDAO());
    map.put(MdRelationshipInfo.CLASS, new MdRelationshipDAO());
    map.put(MdGraphInfo.CLASS, new MdGraphDAO());
    map.put(MdTermRelationshipInfo.CLASS, new MdTermRelationshipDAO());
    map.put(MdTreeInfo.CLASS, new MdTreeDAO());
    map.put(MdExceptionInfo.CLASS, new MdExceptionDAO());
    map.put(MdWarningInfo.CLASS, new MdWarningDAO());
    map.put(MdInformationInfo.CLASS, new MdInformationDAO());
    map.put(MdViewInfo.CLASS, new MdViewDAO());
    map.put(MdUtilInfo.CLASS, new MdUtilDAO());
    map.put(MdProblemInfo.CLASS, new MdProblemDAO());
    map.put(UserInfo.CLASS, new UserDAO());
    map.put(MethodActorInfo.CLASS, new MethodActorDAO());
    map.put(RoleDAOIF.CLASS, new RoleDAO());
    map.put(SDutyDAOIF.CLASS, new SDutyDAO());
    map.put(TypeTupleDAOIF.CLASS, new TypeTupleDAO());
    map.put(DomainTupleDAOIF.CLASS, new DomainTupleDAO());
    map.put(VaultInfo.CLASS, new VaultDAO());
    map.put(VaultFileInfo.CLASS, new VaultFileDAO());
    map.put(WebFileDAOIF.CLASS, new WebFileDAO());
    map.put(MdMethodInfo.CLASS, new MdMethodDAO());
    map.put(MdParameterInfo.CLASS, new MdParameterDAO());
    map.put(MdControllerInfo.CLASS, new MdControllerDAO());
    map.put(MdActionInfo.CLASS, new MdActionDAO());
    map.put(MdDomainInfo.CLASS, new MdDomainDAO());
    map.put(MdDimensionInfo.CLASS, new MdDimensionDAO());
    map.put(MdClassDimensionInfo.CLASS, new MdClassDimensionDAO());
    map.put(MdAttributeDimensionInfo.CLASS, new MdAttributeDimensionDAO());
    map.put(MdAttributeVirtualInfo.CLASS, new MdAttributeVirtualDAO());
    map.put(MdAttributeIntegerInfo.CLASS, new MdAttributeIntegerDAO());
    map.put(MdAttributeLongInfo.CLASS, new MdAttributeLongDAO());
    map.put(MdAttributeTimeInfo.CLASS, new MdAttributeTimeDAO());
    map.put(MdAttributeDateTimeInfo.CLASS, new MdAttributeDateTimeDAO());
    map.put(MdAttributeDateInfo.CLASS, new MdAttributeDateDAO());
    map.put(MdAttributeBooleanInfo.CLASS, new MdAttributeBooleanDAO());
    map.put(MdAttributeCharacterInfo.CLASS, new MdAttributeCharacterDAO());
    map.put(MdAttributeFileInfo.CLASS, new MdAttributeFileDAO());
    map.put(MdAttributeTextInfo.CLASS, new MdAttributeTextDAO());
    map.put(MdAttributeClobInfo.CLASS, new MdAttributeClobDAO());
    map.put(MdAttributeBlobInfo.CLASS, new MdAttributeBlobDAO());
    map.put(MdAttributeFloatInfo.CLASS, new MdAttributeFloatDAO());
    map.put(MdAttributeDoubleInfo.CLASS, new MdAttributeDoubleDAO());
    map.put(MdAttributeDecimalInfo.CLASS, new MdAttributeDecimalDAO());
    map.put(MdAttributeReferenceInfo.CLASS, new MdAttributeReferenceDAO());
    map.put(MdAttributeMultiReferenceInfo.CLASS, new MdAttributeMultiReferenceDAO());
    map.put(MdAttributeMultiTermInfo.CLASS, new MdAttributeMultiTermDAO());
    map.put(MdAttributeTermInfo.CLASS, new MdAttributeTermDAO());
    map.put(MdAttributeEnumerationInfo.CLASS, new MdAttributeEnumerationDAO());
    map.put(MdAttributeStructInfo.CLASS, new MdAttributeStructDAO());
    map.put(MdAttributeLocalCharacterInfo.CLASS, new MdAttributeLocalCharacterDAO());
    map.put(MdAttributeLocalTextInfo.CLASS, new MdAttributeLocalTextDAO());
    map.put(MdAttributeHashInfo.CLASS, new MdAttributeHashDAO());
    map.put(MdAttributeSymmetricInfo.CLASS, new MdAttributeSymmetricDAO());

    // web form
    map.put(MdWebFormInfo.CLASS, new MdWebFormDAO());
    map.put(MdWebCharacterInfo.CLASS, new MdWebCharacterDAO());
    map.put(MdWebDateInfo.CLASS, new MdWebDateDAO());
    map.put(MdWebDateTimeInfo.CLASS, new MdWebDateTimeDAO());
    map.put(MdWebTimeInfo.CLASS, new MdWebTimeDAO());
    map.put(MdWebTextInfo.CLASS, new MdWebTextDAO());
    map.put(MdWebDoubleInfo.CLASS, new MdWebDoubleDAO());
    map.put(MdWebDecimalInfo.CLASS, new MdWebDecimalDAO());
    map.put(MdWebFloatInfo.CLASS, new MdWebFloatDAO());
    map.put(MdWebIntegerInfo.CLASS, new MdWebIntegerDAO());
    map.put(MdWebLongInfo.CLASS, new MdWebLongDAO());
    map.put(MdWebSingleTermInfo.CLASS, new MdWebSingleTermDAO());
    map.put(MdWebMultipleTermInfo.CLASS, new MdWebMultipleTermDAO());
    map.put(MdWebSingleTermGridInfo.CLASS, new MdWebSingleTermGridDAO());
    map.put(MdWebGeoInfo.CLASS, new MdWebGeoDAO());
    map.put(MdWebBooleanInfo.CLASS, new MdWebBooleanDAO());
    map.put(MdWebHeaderInfo.CLASS, new MdWebHeaderDAO());
    map.put(MdWebBreakInfo.CLASS, new MdWebBreakDAO());
    map.put(MdWebCommentInfo.CLASS, new MdWebCommentDAO());
    map.put(MdWebGroupInfo.CLASS, new MdWebGroupDAO());
    map.put(MdWebReferenceInfo.CLASS, new MdWebReferenceDAO());

    // mobile form
    // FIXME add the rest of the mobile attributes (wait until web fields are
    // done)
    // map.put(MdMobileFormInfo.CLASS, new MdMobileFormDAO());
    // map.put(MdMobileCharacterInfo.CLASS, new MdMobileCharacterDAO());
    // map.put(MdMobileDateInfo.CLASS, new MdMobileDateDAO());
    // map.put(MdMobileTextInfo.CLASS, new MdMobileTextDAO());
    // map.put(MdMobileDoubleInfo.CLASS, new MdMobileDoubleDAO());
    // map.put(MdMobileFloatInfo.CLASS, new MdMobileFloatDAO());
    // map.put(MdMobileIntegerInfo.CLASS, new MdMobileIntegerDAO());
    // map.put(MdMobileLongInfo.CLASS, new MdMobileLongDAO());
    // map.put(MdMobileSingleTermInfo.CLASS, new MdMobileSingleTermDAO());
    // map.put(MdMobileMultipleTermInfo.CLASS, new MdMobileMultipleTermDAO());
    // map.put(MdMobileSingleTermGridInfo.CLASS, new
    // MdMobileSingleTermGridDAO());
    // map.put(MdMobileGeoInfo.CLASS, new MdMobileGeoDAO());
    // map.put(MdMobileBooleanInfo.CLASS, new MdMobileBooleanDAO());
    // map.put(MdMobileHeaderInfo.CLASS, new MdMobileHeaderDAO());
    // map.put(MdMobileBreakInfo.CLASS, new MdMobileBreakDAO());
    // map.put(MdMobileGroupInfo.CLASS, new MdMobileGroupDAO());

    map.put(TransactionRecordInfo.CLASS, new TransactionRecordDAO());
    map.put(TransactionItemInfo.CLASS, new TransactionItemDAO());
    map.put(ImportLogInfo.CLASS, new ImportLogDAO());

    // Condition types
    map.put(CharacterConditionInfo.CLASS, new CharacterConditionDAO());
    map.put(DateConditionInfo.CLASS, new DateConditionDAO());
    map.put(DoubleConditionInfo.CLASS, new DoubleConditionDAO());
    map.put(LongConditionInfo.CLASS, new LongConditionDAO());
    map.put(AndFieldConditionInfo.CLASS, new AndFieldConditionDAO());

    // Ontology
    map.put(DatabaseAllPathsStrategyInfo.CLASS, new OntologyStrategyDAO(DatabaseAllPathsStrategyInfo.CLASS));

    return map;
  }

  /**
   * 
   * @param attributeMap
   * @param type
   * @return
   */
  public static BusinessDAO factoryMethod(Map<String, Attribute> attributeMap, String type, boolean useCache)
  {
    String name = type.trim();

    BusinessDAO businessDAO = factory.get(name);

    if (businessDAO != null)
    {
      return businessDAO.create(attributeMap, name);
    }

    boolean isUser;

    isUser = MdElementDAO.isSubEntity(type, UserInfo.CLASS);
    if (isUser)
    {
      return new UserDAO(attributeMap, type);
    }

    boolean isEnumItem;

    isEnumItem = MdElementDAO.isSubEntity(type, EnumerationMasterInfo.CLASS);

    if (isEnumItem)
    {
      if (MdElementDAO.isSubEntity(type, SupportedLocaleInfo.CLASS))
      {
        return new SupportedLocaleDAO(attributeMap, type);
      }

      return new EnumerationItemDAO(attributeMap, type);
    }

    boolean isStateItem;

    isStateItem = MdElementDAO.isSubEntity(type, EntityTypes.STATE_MASTER.getType());

    if (isStateItem)
    {
      return new StateMasterDAO(attributeMap, type);
    }

    return new BusinessDAO(attributeMap, type);
  }

  /**
   * 
   * @param id
   * @return
   */
  public static BusinessDAOIF get(String id)
  {
    BusinessDAOQuery businessDAOsQuery = BusinessDAOQuery.getObjectInstance(id);
    List<BusinessDAO> businessDAOlist = queryBusiessDAOs(businessDAOsQuery);

    // If the BusinessDAO list is null, then a BusinessDAO with the given ID
    // does not exist.
    if (businessDAOlist.size() == 0)
    {
      return null;
    }
    else
    {
      return businessDAOlist.get(0);
    }
  }

  /**
   * Returns a BusinessDAO of the given type with the given key in the database.
   * This method does the same thing as get(String id), but is faster. If you
   * know the type of the id, use this method. Otherwise use the get(String id)
   * method.
   * 
   * <br/>
   * <b>Precondition:</b> key != null <br/>
   * <b>Precondition:</b> !key.trim().equals("") <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("") <br/>
   * <b>Postcondition:</b> BusinessDAO representing the item in the database of
   * the given key and type is returned
   * 
   * @param type
   *          fully qualified type of an item in the database
   * @param key
   *          key of an item in the database
   * 
   * @return BusinessDAO instance of the given type and key
   */
  public static BusinessDAO get(String type, String key)
  {
    BusinessDAOIF businessDAO = null;
    QueryFactory qFactory = new QueryFactory();
    BusinessDAOQuery query = qFactory.businessDAOQuery(type);
    query.WHERE(query.aCharacter(ComponentInfo.KEY).EQ(key));

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    if (iterator.hasNext())
    {
      businessDAO = iterator.next();
    }
    iterator.close();

    if (businessDAO == null)
    {
      MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO(type);
      String msg = "An item of type [" + type + "] with the key [" + key + "] does not exist.";

      throw new DataNotFoundException(msg, mdBusiness);
    }

    return businessDAO.getBusinessDAO();
  }

  /**
   * Returns a List of BusinessDAO objects that match the criteria specified
   * with the given BusinessDAOQuery.
   * 
   * @param businessDAOquery
   *          specifies criteria.
   * @return List of BusinessDAO objects that match the criteria specified with
   *         the given BusinessDAOQuery.
   */
  public static List<BusinessDAO> queryBusiessDAOs(BusinessDAOQuery businessDAOquery)
  {
    return queryBusiessDAOs(businessDAOquery, null);
  }

  /**
   * Returns a List of BusinessDAO objects that match the criteria specified
   * with the given BusinessDAOQuery.
   * 
   * @param businessDAOquery
   *          specifies criteria.
   * @param cacheStrategy
   *          cacheStrategy that is updated for each result.
   * @return List of BusinessDAO objects that match the criteria specified with
   *         the given BusinessDAOQuery.
   */
  public static List<BusinessDAO> queryBusiessDAOs(BusinessDAOQuery businessDAOquery, CacheAllBusinessDAOstrategy cacheStrategy)
  {
    List<BusinessDAO> businessDAOlist = new LinkedList<BusinessDAO>();

    String sqlStmt = businessDAOquery.getSQL();

    Map<String, ColumnInfo> columnInfoMap = businessDAOquery.getColumnInfoMap();
    ResultSet results = Database.query(sqlStmt);

    // ThreadRefactor: get rid of this map.
    // Key: ID of an MdAttribute Value: MdEntity that defines the attribute;
    Map<String, MdEntityDAOIF> definedByMdEntityMap = new HashMap<String, MdEntityDAOIF>();
    // This is map improves performance.
    // Key: type Values: List of MdAttributeIF objects that an instance of the
    // type has.
    Map<String, List<? extends MdAttributeConcreteDAOIF>> mdEntityMdAttributeMap = new HashMap<String, List<? extends MdAttributeConcreteDAOIF>>();

    MdEntityDAOIF rootMdEntityIF = businessDAOquery.getMdEntityIF().getRootMdClassDAO();
    String typeColumnAlias = columnInfoMap.get(rootMdEntityIF.definesType() + "." + EntityDAOIF.TYPE_COLUMN).getColumnAlias();

    Statement statement = null;
    try
    {
      statement = results.getStatement();
      while (results.next())
      {
        String businessDAOtype = results.getString(typeColumnAlias);

        List<? extends MdAttributeConcreteDAOIF> mdAttributeIFList = mdEntityMdAttributeMap.get(businessDAOtype);
        if (mdAttributeIFList == null)
        {
          mdAttributeIFList = MdElementDAO.getMdElementDAO(businessDAOtype).getAllDefinedMdAttributes();
          mdEntityMdAttributeMap.put(businessDAOtype, mdAttributeIFList);
        }

        BusinessDAO businessDAO = buildObjectFromQuery(businessDAOtype, columnInfoMap, definedByMdEntityMap, mdAttributeIFList, results);

        if (cacheStrategy != null)
        {
          cacheStrategy.updateCache(businessDAO);
        }
        // instantiate the businessDAO
        businessDAOlist.add(businessDAO);
      }
    }
    catch (SQLException sqlEx)
    {
      Database.throwDatabaseException(sqlEx);
    }
    finally
    {
      try
      {
        results.close();
        if (statement != null)
        {
          statement.close();
        }
      }
      catch (SQLException sqlEx)
      {
        Database.throwDatabaseException(sqlEx);
      }
    }

    return businessDAOlist;
  }

  /**
   * Builds a BusinessDAO from a row from the given resultset.
   * 
   * @param type
   *          type of the object.
   * @param columnInfoMap
   *          contains information about attributes used in the query
   * @param definedByMdEntityMap
   *          sort of a hack. It is a map where the key is the id of an
   *          MdAttribute and the value is the MdEntity that defines the
   *          attribute. This is used to improve performance.
   * @param MdAttributeIFList
   *          contains MdAttribute objects for the attributes used in this query
   * @param resultSet
   *          ResultSet object from a query.
   * @return BusinessDAO from a row from the given resultset
   */

  public static BusinessDAO buildObjectFromQuery(String type, Map<String, ColumnInfo> columnInfoMap, Map<String, MdEntityDAOIF> definedByMdEntityMap, List<? extends MdAttributeConcreteDAOIF> MdAttributeIFList, ResultSet results)
  {
    // Get the attributes
    Map<String, Attribute> attributeMap = EntityDAOFactory.getAttributesFromQuery(columnInfoMap, definedByMdEntityMap, MdAttributeIFList, results);
    BusinessDAO businessDAO = factoryMethod(attributeMap, type, true);
    return businessDAO;
  }

  /**
   * Assumes that class type is valid with a package and a class name.
   * 
   * @param classType
   * @return
   */
  public static String getClassNameFromType(String classType)
  {
    return EntityDAOFactory.getTypeNameFromType(classType);
  }

  /**
   * Assumes that class type is valid with a package and a class name.
   * 
   * @param classType
   * @return
   */
  public static String getPackageFromType(String classType)
  {
    return EntityDAOFactory.getPackageFromType(classType);
  }

  /**
   * Returns a new BusinessDAO instance of the given class name. Default values
   * are assigned attributes if specified by the metadata.
   * 
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("")
   * 
   * @param type
   *          Class name of the new BusinessDAO to instantiate
   * @return new BusinessDAO of the given BusinessDAO
   * @throws DataAccessException
   *           if the given class name is abstract
   * @throws DataAccessException
   *           if metadata is not defined for the given class name
   */
  public static BusinessDAO newInstance(String type)
  {
    // get the meta data for the given class
    MdEntityDAOIF mdEntityIF = MdEntityDAO.getMdEntityDAO(type);

    if (! ( mdEntityIF instanceof MdBusinessDAOIF ))
    {
      throw new UnexpectedTypeException("Type [" + type + "] is not a BusinessDAO");
    }

    MdBusinessDAOIF mdBusinessIF = (MdBusinessDAOIF) mdEntityIF;

    if (mdBusinessIF.isAbstract())
    {
      String errMsg = "Class [" + mdBusinessIF.definesType() + "] is abstract and cannot be instantiated";
      throw new AbstractInstantiationException(errMsg, mdBusinessIF);
    }

    Hashtable<String, Attribute> attributeMap = new Hashtable<String, Attribute>();

    // get list of all classes in inheritance relationship
    List<MdBusinessDAOIF> superMdBusinessIFList = mdBusinessIF.getSuperClasses();

    for (MdBusinessDAOIF superMdBusinessIF : superMdBusinessIFList)
    {
      attributeMap.putAll(EntityDAOFactory.createRecordsForEntity(superMdBusinessIF));
    }
    // Create the businessDAO
    BusinessDAO newBusinessDAO = factoryMethod(attributeMap, mdBusinessIF.definesType(), true);

    newBusinessDAO.setIsNew(true);
    newBusinessDAO.setAppliedToDB(false);

    newBusinessDAO.setTypeName(mdBusinessIF.definesType());

    // This used to be in EntityDAO.save(), but has been moved here to help with
    // distributed issues
    String newId = IdParser.buildId(ServerIDGenerator.nextID(), mdEntityIF.getId());
    newBusinessDAO.getAttribute(EntityInfo.ID).setValue(newId);

    return newBusinessDAO;
  }

  /**
   * Returns a List of all businessDAOs of the given CONCRETE type.
   * 
   * <br/>
   * <b>Precondition:</b> classType is a concrete class
   * 
   * @param classType
   *          type of the struct.
   * @param cacheStrategy
   *          cacheStrategy that is updated for each result.
   */
  public static void getBusinessDAOtypeInstances(String classType, CacheAllBusinessDAOstrategy cacheStrategy)
  {
    BusinessDAOQuery businessDAOquery = new QueryFactory().businessDAOQuery(classType);
    queryBusiessDAOs(businessDAOquery, cacheStrategy);
  }

  /**
   * Changes all references to this object with its current id to the new given
   * id.
   * 
   * @param _businessDAO
   * @param _oldId
   *          the old reference id
   * @param _newId
   *          new id to reference
   */
  public static void floatObjectIdReferences(BusinessDAO _businessDAO, String _oldId, String _newId)
  {
    BusinessDAOFactory.floatObjectIdReferences(_businessDAO, _oldId, _newId, false);
  }

  public static void floatObjectIdReferences(BusinessDAO _businessDAO, String _oldId, String _newId, boolean _ignoreRelationshipException)
  {
    updateAttributeReferences(_businessDAO, _oldId, _newId);

    updateCachedAttributeEnumerations(_businessDAO, _oldId, _newId);

    updateRelationshipReferences(_businessDAO, _oldId, _newId, _ignoreRelationshipException);

    updateEnumerations(_businessDAO, _oldId, _newId);
  }

  private static void updateEnumerations(BusinessDAO _businessDAO, String _oldId, String _newId)
  {
    MdBusinessDAOIF mdBusinessDAOIF = _businessDAO.getMdBusinessDAO();

    List<MdEnumerationDAOIF> mdEnums = mdBusinessDAOIF.getMdEnumerationDAOs();

    List<String> sqlList = new LinkedList<String>();
    for (MdEnumerationDAOIF mdEnumerationDAOIF : mdEnums)
    {
      String tableName = mdEnumerationDAOIF.getTableName();
      sqlList.add(Database.buildUpdateEnumItemStatement(tableName, _oldId, _newId));
    }

    Database.executeBatch(sqlList);
  }

  private static void updateCachedAttributeEnumerations(BusinessDAO _businessDAO, String _oldId, String _newId)
  {
    TransactionCacheIF cache = TransactionCache.getCurrentTransactionCache();

    MdBusinessDAOIF mdBusinessDAOIF = _businessDAO.getMdBusinessDAO();

    List<MdAttributeEnumerationDAOIF> mdAttrEnumList = mdBusinessDAOIF.getAllEnumerationAttributes();

    QueryFactory queryFactory = new QueryFactory();

    for (MdAttributeEnumerationDAOIF mdAttrEnumDAOIF : mdAttrEnumList)
    {
      MdClassDAOIF mdClassDAOIF = mdAttrEnumDAOIF.definedByClass();

      if (mdClassDAOIF instanceof MdEntityDAOIF)
      {
        MdEntityDAOIF mdEntityDAOIF = ( (MdEntityDAOIF) mdClassDAOIF );

        EntityQuery entityQ = queryFactory.entityQueryDAO(mdEntityDAOIF);
        entityQ.WHERE(entityQ.aEnumeration(mdAttrEnumDAOIF.definesAttribute()).containsAny(_oldId));

        OIterator<? extends ComponentIF> i = null;

        try
        {
          i = entityQ.getIterator();

          while (i.hasNext())
          {
            EntityDAO entityDAO = ( (EntityDAOIF) i.next() ).getEntityDAO();
            AttributeEnumeration attribute = (AttributeEnumeration) entityDAO.getAttribute(mdAttrEnumDAOIF.definesAttribute());

            // Refresh the enum items
            attribute.getEnumItemIdList();

            String oldEnumItemIds = attribute.getCachedEnumItemIds();
            String newEnumItemIds = oldEnumItemIds.replaceAll(_oldId, _newId);

            attribute.setValueNoValidation(_newId);

            // Write the field to the database
            PreparedStatement statement = Database.buildPreparedUpdateFieldStatement(mdEntityDAOIF.getTableName(), entityDAO.getId(), mdAttrEnumDAOIF.getCacheColumnName(), "?", newEnumItemIds, MdAttributeCharacterInfo.CLASS);

            List<PreparedStatement> statements = new LinkedList<PreparedStatement>();
            statements.add(statement);

            Database.executeStatementBatch(statements);

            // Update the transaction cache.
            cache.updateEntityDAO(entityDAO);
          }
        }
        finally
        {
          if (i != null)
          {
            i.close();
          }
        }
      }

      // Update the default values
      if (mdAttrEnumDAOIF.getDefaultValue() != null && mdAttrEnumDAOIF.getDefaultValue().equals(_oldId))
      {
        MdAttributeConcreteDAO mdAttributeConcrete = (MdAttributeConcreteDAO) mdAttrEnumDAOIF.getMdAttributeConcrete().getBusinessDAO();

        Attribute attribute = mdAttributeConcrete.getAttribute(MdAttributeConcreteInfo.DEFAULT_VALUE);
        attribute.setValueNoValidation(_newId);

        MdBusinessDAOIF mdBusinessDAO = mdAttributeConcrete.getMdBusinessDAO();

        String tableName = mdBusinessDAO.getTableName();
        MdAttributeConcreteDAOIF mdDefaultValue = mdBusinessDAO.definesAttribute(MdAttributeConcreteInfo.DEFAULT_VALUE);

        // Write the field to the database
        List<PreparedStatement> preparedStatementList = new LinkedList<PreparedStatement>();
        PreparedStatement preparedStmt = Database.buildPreparedUpdateFieldStatement(tableName, mdAttributeConcrete.getId(), mdDefaultValue.getDefinedColumnName(), "?", _oldId, _newId, MdAttributeCharacterInfo.CLASS);
        preparedStatementList.add(preparedStmt);
        Database.executeStatementBatch(preparedStatementList);

        // Update the transaction cache.
        cache.updateEntityDAO(mdAttributeConcrete);
      }

      List<MdAttributeDimensionDAOIF> mdAttributeDimensions = mdAttrEnumDAOIF.getMdAttributeDimensions();

      for (MdAttributeDimensionDAOIF mdAttributeDimension : mdAttributeDimensions)
      {
        if (mdAttributeDimension.getDefaultValue() != null && mdAttributeDimension.getDefaultValue().equals(_oldId))
        {
          MdAttributeDimensionDAO mdAttributeConcrete = (MdAttributeDimensionDAO) mdAttributeDimension.getBusinessDAO();

          Attribute attribute = mdAttributeConcrete.getAttribute(MdAttributeConcreteInfo.DEFAULT_VALUE);
          attribute.setValueNoValidation(_newId);

          MdBusinessDAOIF mdBusinessDAO = mdAttributeConcrete.getMdBusinessDAO();
          String tableName = mdBusinessDAO.getTableName();
          MdAttributeConcreteDAOIF mdDefaultValue = mdBusinessDAO.definesAttribute(MdAttributeConcreteInfo.DEFAULT_VALUE);

          // Write the field to the database
          List<PreparedStatement> preparedStatementList = new LinkedList<PreparedStatement>();
          PreparedStatement preparedStmt = Database.buildPreparedUpdateFieldStatement(tableName, mdAttributeConcrete.getId(), mdDefaultValue.getDefinedColumnName(), "?", _oldId, _newId, MdAttributeCharacterInfo.CLASS);
          preparedStatementList.add(preparedStmt);
          Database.executeStatementBatch(preparedStatementList);

          // Update the transaction cache.
          cache.updateEntityDAO(mdAttributeConcrete);
        }
      }
    }
  }

  private static void updateAttributeReferences(BusinessDAO businessDAO, String oldId, String newId)
  {
    TransactionCacheIF cache = TransactionCache.getCurrentTransactionCache();

    MdBusinessDAOIF mdBusinessDAOIF = businessDAO.getMdBusinessDAO();

    List<MdAttributeReferenceDAOIF> mdAttrRefList = mdBusinessDAOIF.getAllReferenceAttributes();

    QueryFactory queryFactory = new QueryFactory();

    for (MdAttributeReferenceDAOIF mdAttrRefDAOIF : mdAttrRefList)
    {
      MdAttributeReferenceDAOIF mdAttrRefDAO = (MdAttributeReferenceDAOIF) mdAttrRefDAOIF;
      MdClassDAOIF mdClassDAOIF = mdAttrRefDAO.definedByClass();

      // Update the attribute references
      if (mdClassDAOIF instanceof MdEntityDAOIF)
      {
        MdEntityDAOIF mdEntityDAOIF = ( (MdEntityDAOIF) mdClassDAOIF );

        EntityQuery entityQ = queryFactory.entityQueryDAO(mdEntityDAOIF);
        entityQ.WHERE(entityQ.aReference(mdAttrRefDAO.definesAttribute()).EQ(oldId));

        OIterator<? extends ComponentIF> i = null;

        try
        {
          i = entityQ.getIterator();

          while (i.hasNext())
          {
            EntityDAO entityDAO = ( (EntityDAOIF) i.next() ).getEntityDAO();
            entityDAO.getAttribute(mdAttrRefDAO.definesAttribute()).setValueNoValidation(newId);

            // Write the field to the database
            List<PreparedStatement> preparedStatementList = new LinkedList<PreparedStatement>();
            PreparedStatement preparedStmt = null;
            preparedStmt = Database.buildPreparedUpdateFieldStatement(mdEntityDAOIF.getTableName(), entityDAO.getId(), mdAttrRefDAO.getDefinedColumnName(), "?", oldId, newId, MdAttributeCharacterInfo.CLASS);
            preparedStatementList.add(preparedStmt);
            Database.executeStatementBatch(preparedStatementList);

            // Update the transaction cache.
            cache.updateEntityDAO(entityDAO);
          }
        }
        finally
        {
          if (i != null)
          {
            i.close();
          }
        }
      }

      // Update the default values
      if (mdAttrRefDAOIF.getDefaultValue() != null && mdAttrRefDAOIF.getDefaultValue().equals(oldId))
      {
        MdAttributeConcreteDAO mdAttributeConcrete = (MdAttributeConcreteDAO) mdAttrRefDAOIF.getMdAttributeConcrete().getBusinessDAO();

        Attribute attribute = (Attribute) mdAttributeConcrete.getAttribute(MdAttributeConcreteInfo.DEFAULT_VALUE);
        attribute.setValueNoValidation(newId);

        MdBusinessDAOIF mdBusinessDAO = mdAttributeConcrete.getMdBusinessDAO();

        String tableName = mdBusinessDAO.getTableName();
        MdAttributeConcreteDAOIF mdDefaultValue = mdBusinessDAO.definesAttribute(MdAttributeConcreteInfo.DEFAULT_VALUE);

        // Write the field to the database
        List<PreparedStatement> preparedStatementList = new LinkedList<PreparedStatement>();
        PreparedStatement preparedStmt = Database.buildPreparedUpdateFieldStatement(tableName, mdAttributeConcrete.getId(), mdDefaultValue.getDefinedColumnName(), "?", oldId, newId, MdAttributeCharacterInfo.CLASS);
        preparedStatementList.add(preparedStmt);
        Database.executeStatementBatch(preparedStatementList);

        // Update the transaction cache.
        if (cache != null)
        {
          cache.updateEntityDAO(mdAttributeConcrete);
        }
      }

      List<MdAttributeDimensionDAOIF> mdAttributeDimensions = mdAttrRefDAOIF.getMdAttributeDimensions();

      for (MdAttributeDimensionDAOIF mdAttributeDimensionIF : mdAttributeDimensions)
      {
        if (mdAttributeDimensionIF.getDefaultValue() != null && mdAttributeDimensionIF.getDefaultValue().equals(oldId))
        {
          MdAttributeDimensionDAO mdAttributeDimension = (MdAttributeDimensionDAO) mdAttributeDimensionIF.getBusinessDAO();

          Attribute attribute = (Attribute) mdAttributeDimension.getAttribute(MdAttributeConcreteInfo.DEFAULT_VALUE);
          attribute.setValueNoValidation(newId);

          MdBusinessDAOIF mdBusinessDAO = mdAttributeDimension.getMdBusinessDAO();
          String tableName = mdBusinessDAO.getTableName();
          MdAttributeConcreteDAOIF mdDefaultValue = mdBusinessDAO.definesAttribute(MdAttributeConcreteInfo.DEFAULT_VALUE);

          // Write the field to the database
          List<PreparedStatement> preparedStatementList = new LinkedList<PreparedStatement>();
          PreparedStatement preparedStmt = Database.buildPreparedUpdateFieldStatement(tableName, mdAttributeDimension.getId(), mdDefaultValue.getDefinedColumnName(), "?", oldId, newId, MdAttributeCharacterInfo.CLASS);
          preparedStatementList.add(preparedStmt);
          Database.executeStatementBatch(preparedStatementList);

          // Update the transaction cache.
          if (cache != null)
          {
            cache.updateEntityDAO(mdAttributeDimension);
          }
        }
      }
    }
  }

  /**
   * Updates all relationship references to the given object with the newly
   * given id.
   * 
   * @param businessDAO
   * @param oldId
   * @param newId
   * @param ignoreDatabaseExceptions
   */
  private static void updateRelationshipReferences(BusinessDAO businessDAO, String oldId, String newId, boolean _ignoreRelationshipExceptions)
  {
    TransactionCacheIF cache = TransactionCache.getCurrentTransactionCache();

    QueryFactory queryFactory = new QueryFactory();
    // Update parent IDs
    List<MdRelationshipDAOIF> parentMdRelationshipList = businessDAO.getMdBusinessDAO().getAllParentMdRelationships();
    for (MdRelationshipDAOIF mdRelationshipDAOIF : parentMdRelationshipList)
    {
      if (mdRelationshipDAOIF.isAbstract())
      {
        continue;
      }

      List<MdRelationshipDAOIF> superMdRelationshipDAOIF = mdRelationshipDAOIF.getSuperClasses();

      RelationshipDAOQuery relQuery = queryFactory.relationshipDAOQuery(mdRelationshipDAOIF.definesType());
      relQuery.WHERE(relQuery.parentId().EQ(oldId));
      OIterator<RelationshipDAOIF> iterator = null;
      try
      {
        iterator = relQuery.getIterator();

        for (RelationshipDAOIF relationshipDAOIF : iterator)
        {
          RelationshipDAO relationshipDAO = relationshipDAOIF.getRelationshipDAO();
          relationshipDAO.setParentId(newId);

          // Not calling the apply method because we do not want to invoke any
          // other logic.
          List<PreparedStatement> preparedStatementList = new LinkedList<PreparedStatement>();
          // includes this and all parent relationships

          for (MdRelationshipDAOIF parentMdRelationshipDAOIF : superMdRelationshipDAOIF)
          {
            PreparedStatement preparedStmt = null;
            preparedStmt = Database.buildPreparedUpdateFieldStatement(parentMdRelationshipDAOIF.getTableName(), relationshipDAO.getId(), RelationshipInfo.PARENT_ID, "?", oldId, newId, MdAttributeCharacterInfo.CLASS);
            preparedStatementList.add(preparedStmt);
          }

          int[] batchResults = BusinessDAOFactory.executeStatementBatch(preparedStatementList, _ignoreRelationshipExceptions);

          // check for a stale object delete.
          for (int i = 0; i < batchResults.length; i++)
          {
            if (batchResults[i] == 0)
            {
              String type = relationshipDAO.getType();
              String key = relationshipDAO.getKey();
              String error = "Object with id [" + relationshipDAO.getId() + "] of type [" + type + "] with key [" + key + "] is stale and cannot be deleted.";
              throw new StaleEntityException(error, relationshipDAO);
            }
          }

          cache.addRelationship(relationshipDAO);
          cache.updateEntityDAO(relationshipDAO);
        }
      }
      finally
      {
        if (iterator != null)
        {
          iterator.close();
        }
      }
    }

    // Update child ids
    List<MdRelationshipDAOIF> childMdRelationshipList = businessDAO.getMdBusinessDAO().getAllChildMdRelationships();
    for (MdRelationshipDAOIF mdRelationshipDAOIF : childMdRelationshipList)
    {
      if (mdRelationshipDAOIF.isAbstract())
      {
        continue;
      }

      List<MdRelationshipDAOIF> superMdRelationshipDAOIF = mdRelationshipDAOIF.getSuperClasses();

      RelationshipDAOQuery relQuery = queryFactory.relationshipDAOQuery(mdRelationshipDAOIF.definesType());
      relQuery.WHERE(relQuery.childId().EQ(oldId));
      OIterator<RelationshipDAOIF> iterator = null;
      try
      {
        iterator = relQuery.getIterator();

        for (RelationshipDAOIF relationshipDAOIF : iterator)
        {
          RelationshipDAO relationshipDAO = relationshipDAOIF.getRelationshipDAO();
          relationshipDAO.setChildId(newId);

          // Not calling the apply method because we do not want to invoke any
          // other logic.
          List<PreparedStatement> preparedStatementList = new LinkedList<PreparedStatement>();
          // includes this and all parent relationships
          for (MdRelationshipDAOIF parentMdRelationshipDAOIF : superMdRelationshipDAOIF)
          {
            PreparedStatement preparedStmt = null;
            preparedStmt = Database.buildPreparedUpdateFieldStatement(parentMdRelationshipDAOIF.getTableName(), relationshipDAO.getId(), RelationshipInfo.CHILD_ID, "?", oldId, newId, MdAttributeCharacterInfo.CLASS);
            preparedStatementList.add(preparedStmt);
          }

          int[] batchResults = BusinessDAOFactory.executeStatementBatch(preparedStatementList, _ignoreRelationshipExceptions);

          // check for a stale object delete.
          for (int i = 0; i < batchResults.length; i++)
          {
            if (batchResults[i] == 0)
            {
              String type = relationshipDAO.getType();
              String key = relationshipDAO.getKey();
              String error = "Object with id [" + relationshipDAO.getId() + "] of type [" + type + "] with key [" + key + "] is stale and cannot be deleted.";
              throw new StaleEntityException(error, relationshipDAO);
            }
          }

          cache.addRelationship(relationshipDAO);
          cache.updateEntityDAO(relationshipDAO);
        }
      }
      finally
      {
        if (iterator != null)
        {
          iterator.close();
        }
      }
    }
  }

  private static int[] executeStatementBatch(List<PreparedStatement> _statements, boolean _ignoreOnDatabaseException)
  {
    if (_ignoreOnDatabaseException)
    {
      List<Integer> results = new LinkedList<Integer>();

      for (PreparedStatement statement : _statements)
      {
        Savepoint savepoint = Database.setSavepoint();

        try
        {
          List<PreparedStatement> temp = new LinkedList<PreparedStatement>();
          temp.add(statement);

          int[] result = Database.executeStatementBatch(temp);

          results.add(result[0]);

          Database.releaseSavepoint(savepoint);
        }
        catch (DuplicateGraphPathException e)
        {
          // Do nothing
          Database.rollbackSavepoint(savepoint);
        }
      }

      return ArrayUtils.toPrimitive(results.toArray(new Integer[results.size()]));
    }
    else
    {
      return Database.executeStatementBatch(_statements);
    }
  }

  /**
   * Changes all references to this object with its current id to the new given
   * id.
   * 
   * @param _businessDAO
   * @param _oldId
   *          the old reference id
   * @param _newId
   *          new id to reference
   * @param _ignoreRelationshipException
   */
  public static void floatObjectIdReferencesDatabase(BusinessDAO _businessDAO, String _oldId, String _newId, boolean _ignoreRelationshipException)
  {
    updateAttributeReferencesDatabase(_businessDAO, _oldId, _newId);

    updateRelationshipReferences(_businessDAO, _oldId, _newId, _ignoreRelationshipException);

    updateCachedAttributeEnumerations(_businessDAO, _oldId, _newId);

    updateEnumerations(_businessDAO, _oldId, _newId);
  }

  private static void updateAttributeReferencesDatabase(BusinessDAO _businessDAO, String _oldId, String _newId)
  {
    TransactionCacheIF cache = TransactionCache.getCurrentTransactionCache();

    MdBusinessDAOIF mdBusinessDAOIF = _businessDAO.getMdBusinessDAO();

    List<MdAttributeReferenceDAOIF> mdAttrRefList = mdBusinessDAOIF.getAllReferenceAttributes();

    for (MdAttributeReferenceDAOIF mdAttrRefDAOIF : mdAttrRefList)
    {
      MdAttributeReferenceDAOIF mdAttrRefDAO = (MdAttributeReferenceDAOIF) mdAttrRefDAOIF;
      MdClassDAOIF mdClassDAOIF = mdAttrRefDAO.definedByClass();

      // Update the entire table in one statement
      if (mdClassDAOIF instanceof MdEntityDAOIF)
      {
        MdEntityDAOIF mdEntityDAOIF = ( (MdEntityDAOIF) mdClassDAOIF );

        List<PreparedStatement> preparedStatementList = new LinkedList<PreparedStatement>();
        PreparedStatement preparedStmt = null;
        preparedStmt = Database.buildPreparedUpdateFieldStatement(mdEntityDAOIF.getTableName(), null, mdAttrRefDAO.getDefinedColumnName(), "?", _oldId, _newId, MdAttributeCharacterInfo.CLASS);
        preparedStatementList.add(preparedStmt);
        Database.executeStatementBatch(preparedStatementList);
      }

      // Update the default values
      if (mdAttrRefDAOIF.getDefaultValue() != null && mdAttrRefDAOIF.getDefaultValue().equals(_oldId))
      {
        MdAttributeConcreteDAO mdAttributeConcrete = (MdAttributeConcreteDAO) mdAttrRefDAOIF.getMdAttributeConcrete().getBusinessDAO();

        Attribute attribute = (Attribute) mdAttributeConcrete.getAttribute(MdAttributeConcreteInfo.DEFAULT_VALUE);
        attribute.setValueNoValidation(_newId);

        MdBusinessDAOIF mdBusinessDAO = mdAttributeConcrete.getMdBusinessDAO();

        String tableName = mdBusinessDAO.getTableName();
        MdAttributeConcreteDAOIF mdDefaultValue = mdBusinessDAO.definesAttribute(MdAttributeConcreteInfo.DEFAULT_VALUE);

        // Write the field to the database
        List<PreparedStatement> preparedStatementList = new LinkedList<PreparedStatement>();
        PreparedStatement preparedStmt = Database.buildPreparedUpdateFieldStatement(tableName, mdAttributeConcrete.getId(), mdDefaultValue.getDefinedColumnName(), "?", _oldId, _newId, MdAttributeCharacterInfo.CLASS);
        preparedStatementList.add(preparedStmt);
        Database.executeStatementBatch(preparedStatementList);

        // Update the transaction cache.
        if (cache != null)
        {
          cache.updateEntityDAO(mdAttributeConcrete);
        }
      }

      List<MdAttributeDimensionDAOIF> mdAttributeDimensions = mdAttrRefDAOIF.getMdAttributeDimensions();

      for (MdAttributeDimensionDAOIF mdAttributeDimensionIF : mdAttributeDimensions)
      {
        if (mdAttributeDimensionIF.getDefaultValue() != null && mdAttributeDimensionIF.getDefaultValue().equals(_oldId))
        {
          MdAttributeDimensionDAO mdAttributeDimension = (MdAttributeDimensionDAO) mdAttributeDimensionIF.getBusinessDAO();

          Attribute attribute = (Attribute) mdAttributeDimension.getAttribute(MdAttributeConcreteInfo.DEFAULT_VALUE);
          attribute.setValueNoValidation(_newId);

          MdBusinessDAOIF mdBusinessDAO = mdAttributeDimension.getMdBusinessDAO();
          String tableName = mdBusinessDAO.getTableName();
          MdAttributeConcreteDAOIF mdDefaultValue = mdBusinessDAO.definesAttribute(MdAttributeConcreteInfo.DEFAULT_VALUE);

          // Write the field to the database
          List<PreparedStatement> preparedStatementList = new LinkedList<PreparedStatement>();
          PreparedStatement preparedStmt = Database.buildPreparedUpdateFieldStatement(tableName, mdAttributeDimension.getId(), mdDefaultValue.getDefinedColumnName(), "?", _oldId, _newId, MdAttributeCharacterInfo.CLASS);
          preparedStatementList.add(preparedStmt);
          Database.executeStatementBatch(preparedStatementList);

          // Update the transaction cache.
          if (cache != null)
          {
            cache.updateEntityDAO(mdAttributeDimension);
          }
        }
      }
    }
  }
}
