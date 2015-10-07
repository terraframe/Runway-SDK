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
package com.runwaysdk.query;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeClobInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFileInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalTextInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.MdAttributeMultiTermInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.AttributeDoesNotExistException;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdAttributeBlobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeClobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecimalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFloatDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLongDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiTermDAOIF;
import com.runwaysdk.dataaccess.MdAttributeRefDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTextDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeConcrete_Q;
import com.runwaysdk.dataaccess.cache.CacheAllRelationshipDAOStrategy;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.system.metadata.MdAttributeTerm;

public abstract class EntityQuery extends ComponentQuery implements HasAttributeFactory
{
  private static Map<String, PluginIF> pluginMap = new ConcurrentHashMap<String, PluginIF>();
  
  /**
   * Key: attribute namespace + attribute Value ColumnInfo Object attribute
   * namespace example: package.Class.attributeName attribute namespace example:
   * package.Class.refAttributeName.attributeName
   */
  protected Map<String, ColumnInfo> columnInfoMap;
  
  public static void registerPlugin(PluginIF pluginFactory)
  {
    pluginMap.put(pluginFactory.getModuleIdentifier(), pluginFactory);
  }

  /**
   * Indicates if this query Object is being used to generate SQL for a
   * singleObjectInstance.
   */
  protected boolean                                 instanceQuery;

  /**
   * Entity defining the type queried by this object.
   */
  protected MdEntityDAOIF                           mdEntityIF;

  protected String                                  type;

  /**
   * Key: attribute name in lower case, Value: attribute metadata.
   */
  protected Map<String, ? extends MdAttributeDAOIF> mdAttributeMap;

  /**
   * 
   * @param queryFactory
   * @param type
   */
  protected EntityQuery(QueryFactory queryFactory, String type)
  {
    super(queryFactory);
    this.init(type);
  }

  /**
   * 
   * @param valueQuery
   * @param type
   */
  protected EntityQuery(ValueQuery valueQuery, String type)
  {
    super(valueQuery);
    this.init(type);
  }

  private void init(String type)
  {
    this.instanceQuery = false;

    this.type = type;

    this.mdEntityIF = MdEntityDAO.getMdEntityDAO(type);
    this.mdAttributeMap = this.mdEntityIF.getAllDefinedMdAttributeMap();

    // Used for queries that select all attributes
    this.columnInfoMap = new HashMap<String, ColumnInfo>();
  }

  public Map<String, ColumnInfo> getColumnInfoMap()
  {
    return this.columnInfoMap;
  }
  
  /**
   * Returns the MdEntity that defines the type of objects that are queried from
   * this object.
   * 
   * @return MdEntity that defines the type of objects that are queried from
   *         this object.
   */
  public MdEntityDAOIF getMdEntityIF()
  {
    return this.mdEntityIF;
  }

  /**
   * Returns the type that this object queries.
   * 
   * @return type that this object queries.
   */
  public String getType()
  {
    return this.type;
  }

  /**
   * Returns the type of components that are queried by this object.
   * 
   * @return type of components that are queried by this object.
   */
  public String getEntityType()
  {
    return this.type;
  }

  /**
   * Returns the {@link MdAttributeDAOIF} from the map that defines the attribute with the
   * given name.
   * 
   * @param attributeName
   * @return MdAttributeIF
   */
  public MdAttributeDAOIF getMdAttributeROfromMap(String attributeName)
  {
    return this.mdAttributeMap.get(attributeName.toLowerCase());
  }

  /**
   * Returns an Condition object representing an equals with the attribute with
   * the given name with the given value.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param value
   *          value to compare.
   * @return Condition object representing an equals with the attribute with the
   *         given name with the given value.
   */
  public AttributePrimitive getPrimitive(String attributeName)
  {
    return (AttributePrimitive) get(attributeName, null, null);
  }

  /**
   * Returns an Condition object representing an equals with the attribute with
   * the given name with the given value.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param value
   *          value to compare.
   * @return Condition object representing an equals with the attribute with the
   *         given name with the given value.
   */
  public Attribute get(String attributeName)
  {
    return get(attributeName, null, null);
  }

  /**
   * Returns an Condition object representing an equals with the attribute with
   * the given name with the given value.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   *          user defined alias.
   * @return Condition object representing an equals with the attribute with the
   *         given name with the given value.
   */
  public Attribute get(String attributeName, String userDefinedAlias)
  {
    return get(attributeName, userDefinedAlias, null);
  }

  /**
   * Returns an Condition object representing an equals with the attribute with
   * the given name with the given value.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   *          user defined alias.
   * @param userDefinedDisplayLabel
   * @return Condition object representing an equals with the attribute with the
   *         given name with the given value.
   */
  public Attribute get(String attributeName, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    if (mdAttributeIF == null)
    {
      String error = "An attribute named [" + attributeName + "] does not exist on type [" + this.getMdEntityIF().definesType() + "]";
      throw new AttributeDoesNotExistException(error, attributeName, this.getMdEntityIF());
    }

    if (mdAttributeIF.definesAttribute().equals(EntityInfo.ID))
    {
      Set<Join> attributeTableJoinSet = new HashSet<Join>();

      String definingTableName = this.mdEntityIF.getTableName();
      String definingTableAlias = this.getTableAlias("", definingTableName);

      return new AttributeCharacter((MdAttributeCharacterDAOIF) mdAttributeIF, type, definingTableName, definingTableAlias, this, attributeTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }

    return this.internalAttributeFactory(attributeName, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute character statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute character statement object.
   */
  public AttributeCharacter aCharacter(String name)
  {
    return this.aCharacter(name, null, null);
  }

  /**
   * Returns an attribute character statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute character statement object.
   */
  public AttributeCharacter aCharacter(String name, String userDefinedAlias)
  {
    return this.aCharacter(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute character statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute character statement object.
   */
  public AttributeCharacter aCharacter(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Set<Join> attributeTableJoinSet = new HashSet<Join>();

    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    if (name.equals(EntityInfo.ID))
    {
      String definingTableName = this.mdEntityIF.getTableName();
      String definingTableAlias = this.getTableAlias("", definingTableName);

      return new AttributeCharacter((MdAttributeCharacterDAOIF) mdAttributeIF, type, definingTableName, definingTableAlias, this, attributeTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else
    {
      this.checkValidAttributeRequest(name, this.mdEntityIF, mdAttributeIF, MdAttributeCharacterInfo.CLASS);

      return (AttributeCharacter) this.internalAttributeFactory(name, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  /**
   * Returns an attribute character statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute character statement object.
   */
  public AttributeCharacter id()
  {
    return this.id(null);
  }

  /**
   * Returns an attribute character statement object.
   * 
   * @param userDefinedAlias
   * @return Attribute character statement object.
   */
  public AttributeCharacter id(String userDefinedAlias)
  {
    String name = EntityInfo.ID;
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    String definingTableName = this.mdEntityIF.getTableName();
    String definingTableAlias = this.getTableAlias("", definingTableName);

    Set<Join> attributeTableJoinSet = new HashSet<Join>();

    return new AttributeCharacter((MdAttributeCharacterDAOIF) mdAttributeIF, type, definingTableName, definingTableAlias, this, attributeTableJoinSet, userDefinedAlias, null);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param selectable
   */
  public LeftJoinEq LEFT_JOIN_EQ(Selectable selectable)
  {
    return new LeftJoinEq(this.id(), selectable);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param selectableArray
   */
  public LeftJoinEq LEFT_JOIN_EQ(Selectable... selectableArray)
  {
    return new LeftJoinEq(this.id(), selectableArray);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param selectable
   */
  public LeftJoinNotEq LEFT_JOIN_NE(SelectableSingle selectable)
  {
    return new LeftJoinNotEq(this.id(), selectable);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param selectableArray
   */
  public LeftJoinNotEq LEFT_JOIN_NE(SelectableSingle... selectableArray)
  {
    return new LeftJoinNotEq(this.id(), selectableArray);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param selectable
   */
  public LeftJoinGt LEFT_JOIN_GT(SelectableSingle selectable)
  {
    return new LeftJoinGt(this.id(), selectable);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param selectableArray
   */
  public LeftJoinGt LEFT_JOIN_GT(SelectableSingle... selectableArray)
  {
    return new LeftJoinGt(this.id(), selectableArray);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param entityQuery
   */
  public LeftJoinGtEq LEFT_JOIN_GE(SelectableSingle selectable)
  {
    return new LeftJoinGtEq(this.id(), selectable);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param entityQuery
   */
  public LeftJoinGtEq LEFT_JOIN_GE(SelectableSingle... selectableArray)
  {
    return new LeftJoinGtEq(this.id(), selectableArray);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param selectable
   */
  public LeftJoinLt LEFT_JOIN_LT(SelectableSingle selectable)
  {
    return new LeftJoinLt(this.id(), selectable);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param selectableArray
   */
  public LeftJoinLt LEFT_JOIN_LT(SelectableSingle... selectableArray)
  {
    return new LeftJoinLt(this.id(), selectableArray);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param selectable
   */
  public LeftJoinLtEq LEFT_JOIN_LE(SelectableSingle selectable)
  {
    return new LeftJoinLtEq(this.id(), selectable);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param selectableArray
   */
  public LeftJoinLtEq LEFT_JOIN_LE(SelectableSingle... selectableArray)
  {
    return new LeftJoinLtEq(this.id(), selectableArray);
  }

  /**
   * Returns an attribute text statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute text statement object.
   */
  public AttributeText aText(String name)
  {
    return this.aText(name, null, null);
  }

  /**
   * Returns an attribute text statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute text statement object.
   */
  public AttributeText aText(String name, String userDefinedAlias)
  {
    return this.aText(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute text statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute text statement object.
   */
  public AttributeText aText(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.checkValidAttributeRequest(name, this.mdEntityIF, mdAttributeIF, MdAttributeTextInfo.CLASS);

    return (AttributeText) this.internalAttributeFactory(name, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute CLOB statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute CLOB statement object.
   */
  public AttributeClob aClob(String name)
  {
    return this.aClob(name, null, null);
  }

  /**
   * Returns an attribute CLOB statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute CLOB statement object.
   */
  public AttributeClob aClob(String name, String userDefinedAlias)
  {
    return this.aClob(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute CLOB statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute CLOB statement object.
   */
  public AttributeClob aClob(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.checkValidAttributeRequest(name, this.mdEntityIF, mdAttributeIF, MdAttributeClobInfo.CLASS);

    return (AttributeClob) this.internalAttributeFactory(name, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute date statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute date statement object.
   */
  public AttributeDate aDate(String name)
  {
    return this.aDate(name, null, null);
  }

  /**
   * Returns an attribute date statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute date statement object.
   */
  public AttributeDate aDate(String name, String userDefinedAlias)
  {
    return this.aDate(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute date statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute date statement object.
   */
  public AttributeDate aDate(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.checkValidAttributeRequest(name, this.mdEntityIF, mdAttributeIF, MdAttributeDateInfo.CLASS);

    return (AttributeDate) this.internalAttributeFactory(name, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute time statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute time statement object.
   */
  public AttributeTime aTime(String name)
  {
    return this.aTime(name, null, null);
  }

  /**
   * Returns an attribute time statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute time statement object.
   */
  public AttributeTime aTime(String name, String userDefinedAlias)
  {
    return this.aTime(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute time statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute time statement object.
   */
  public AttributeTime aTime(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.checkValidAttributeRequest(name, this.mdEntityIF, mdAttributeIF, MdAttributeTimeInfo.CLASS);

    return (AttributeTime) this.internalAttributeFactory(name, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute datetime statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute datetime statement object.
   */
  public AttributeDateTime aDateTime(String name)
  {
    return this.aDateTime(name, null, null);
  }

  /**
   * Returns an attribute datetime statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute datetime statement object.
   */
  public AttributeDateTime aDateTime(String name, String userDefinedAlias)
  {
    return this.aDateTime(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute datetime statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute datetime statement object.
   */
  public AttributeDateTime aDateTime(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.checkValidAttributeRequest(name, this.mdEntityIF, mdAttributeIF, MdAttributeDateTimeInfo.CLASS);

    return (AttributeDateTime) this.internalAttributeFactory(name, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute integer statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute integer statement object.
   */
  public AttributeInteger aInteger(String name)
  {
    return this.aInteger(name, null, null);
  }

  /**
   * Returns an attribute integer statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute integer statement object.
   */
  public AttributeInteger aInteger(String name, String userDefinedAlias)
  {
    return this.aInteger(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute integer statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute integer statement object.
   */
  public AttributeInteger aInteger(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.checkValidAttributeRequest(name, this.mdEntityIF, mdAttributeIF, MdAttributeIntegerInfo.CLASS);

    return (AttributeInteger) this.internalAttributeFactory(name, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute long statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute long statement object.
   */
  public AttributeLong aLong(String name)
  {
    return this.aLong(name, null, null);
  }

  /**
   * Returns an attribute long statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute long statement object.
   */
  public AttributeLong aLong(String name, String userDefinedAlias)
  {
    return this.aLong(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute long statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute long statement object.
   */
  public AttributeLong aLong(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.checkValidAttributeRequest(name, this.mdEntityIF, mdAttributeIF, MdAttributeLongInfo.CLASS);

    return (AttributeLong) this.internalAttributeFactory(name, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute double statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute double statement object.
   */
  public AttributeDouble aDouble(String name)
  {
    return this.aDouble(name, null, null);
  }

  /**
   * Returns an attribute double statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute double statement object.
   */
  public AttributeDouble aDouble(String name, String userDefinedAlias)
  {
    return this.aDouble(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute double statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute double statement object.
   */
  public AttributeDouble aDouble(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.checkValidAttributeRequest(name, this.mdEntityIF, mdAttributeIF, MdAttributeDoubleInfo.CLASS);

    return (AttributeDouble) this.internalAttributeFactory(name, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute decimal statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute decimal statement object.
   */
  public AttributeDecimal aDecimal(String name)
  {
    return this.aDecimal(name, null, null);
  }

  /**
   * Returns an attribute decimal statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute decimal statement object.
   */
  public AttributeDecimal aDecimal(String name, String userDefinedAlias)
  {
    return this.aDecimal(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute decimal statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute decimal statement object.
   */
  public AttributeDecimal aDecimal(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.checkValidAttributeRequest(name, this.mdEntityIF, mdAttributeIF, MdAttributeDecimalInfo.CLASS);

    return (AttributeDecimal) this.internalAttributeFactory(name, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute float statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute float statement object.
   */
  public AttributeFloat aFloat(String name)
  {
    return this.aFloat(name, null, null);
  }

  /**
   * Returns an attribute float statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute float statement object.
   */
  public AttributeFloat aFloat(String name, String userDefinedAlias)
  {
    return this.aFloat(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute float statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute float statement object.
   */
  public AttributeFloat aFloat(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.checkValidAttributeRequest(name, this.mdEntityIF, mdAttributeIF, MdAttributeFloatInfo.CLASS);

    return (AttributeFloat) this.internalAttributeFactory(name, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute boolean statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute boolean statement object.
   */
  public AttributeBoolean aBoolean(String name)
  {
    return aBoolean(name, null, null);
  }

  /**
   * Returns an attribute boolean statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute boolean statement object.
   */
  public AttributeBoolean aBoolean(String name, String userDefinedAlias)
  {
    return aBoolean(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute boolean statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute boolean statement object.
   */
  public AttributeBoolean aBoolean(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.checkValidAttributeRequest(name, this.mdEntityIF, mdAttributeIF, MdAttributeBooleanInfo.CLASS);

    return (AttributeBoolean) this.internalAttributeFactory(name, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute struct statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute struct statement object.
   */
  public AttributeStruct aStruct(String name)
  {
    return this.aStruct(name, null, null);
  }

  /**
   * Returns an attribute struct statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute struct statement object.
   */
  public AttributeStruct aStruct(String name, String userDefinedAlias)
  {
    return this.aStruct(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute struct statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute struct statement object.
   */
  public AttributeStruct aStruct(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.checkValidAttributeRequest(name, this.mdEntityIF, mdAttributeIF, MdAttributeStructInfo.CLASS);

    return (AttributeStruct) this.internalAttributeFactory(name, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute local statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute local statement object.
   */
  public AttributeLocal aLocalCharacter(String name)
  {
    return this.aLocalCharacter(name, null, null);
  }

  /**
   * Returns an attribute local statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute local statement object.
   */
  public AttributeLocal aLocalCharacter(String name, String userDefinedAlias)
  {
    return this.aLocalCharacter(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute local statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute local statement object.
   */
  public AttributeLocal aLocalCharacter(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.checkValidAttributeRequest(name, this.mdEntityIF, mdAttributeIF, MdAttributeLocalCharacterInfo.CLASS);

    return (AttributeLocal) this.internalAttributeFactory(name, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute local statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute local statement object.
   */
  public AttributeLocal aLocalText(String name)
  {
    return this.aLocalText(name, null, null);
  }

  /**
   * Returns an attribute local statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute local statement object.
   */
  public AttributeLocal aLocalText(String name, String userDefinedAlias)
  {
    return this.aLocalText(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute local statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute local statement object.
   */
  public AttributeLocal aLocalText(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.checkValidAttributeRequest(name, this.mdEntityIF, mdAttributeIF, MdAttributeLocalTextInfo.CLASS);

    return (AttributeLocal) this.internalAttributeFactory(name, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute blob statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute blob statement object.
   */
  public AttributeBlob aBlob(String name)
  {
    return this.aBlob(name, null, null);
  }

  /**
   * Returns an attribute blob statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute blob statement object.
   */
  public AttributeBlob aBlob(String name, String userDefinedAlias)
  {
    return this.aBlob(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute blob statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute blob statement object.
   */
  public AttributeBlob aBlob(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.checkValidAttributeRequest(name, this.mdEntityIF, mdAttributeIF, MdAttributeBlobInfo.CLASS);

    return (AttributeBlob) this.internalAttributeFactory(name, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute reference statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return attribute reference statement object.
   */
  public AttributeReference aReference(String name)
  {
    return this.aReference(name, null, null);
  }

  /**
   * Returns an attribute reference statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return attribute reference statement object.
   */
  public AttributeReference aReference(String name, String userDefinedAlias)
  {
    return this.aReference(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute reference statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return attribute reference statement object.
   */
  public AttributeReference aReference(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    
    // TODO : Implement a proper, generic solution for Attribute subtypes
    if (mdAttributeIF != null && mdAttributeIF.getType().equals(MdAttributeTerm.CLASS)) {
      this.checkValidAttributeRequest(name, this.mdEntityIF, mdAttributeIF, MdAttributeTerm.CLASS);
    }
    else {
      this.checkValidAttributeRequest(name, this.mdEntityIF, mdAttributeIF, MdAttributeReferenceInfo.CLASS);
    }

    return (AttributeReference) this.internalAttributeFactory(name, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute reference statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return attribute reference statement object.
   */
  public AttributeTerm aTerm(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.checkValidAttributeRequest(name, this.mdEntityIF, mdAttributeIF, MdAttributeTermInfo.CLASS);

    return (AttributeTerm) this.internalAttributeFactory(name, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute file statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return attribute file statement object.
   */
  public AttributeReference aFile(String name)
  {
    return this.aFile(name, null, null);
  }

  /**
   * Returns an attribute file statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return attribute file statement object.
   */
  public AttributeReference aFile(String name, String userDefinedAlias)
  {
    return this.aFile(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute file statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return attribute file statement object.
   */
  public AttributeReference aFile(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.checkValidAttributeRequest(name, this.mdEntityIF, mdAttributeIF, MdAttributeFileInfo.CLASS);

    return (AttributeReference) this.internalAttributeFactory(name, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute enumeration statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute enumeration statement object.
   */
  public AttributeEnumeration aEnumeration(String name)
  {
    return this.aEnumeration(name, null, null);
  }

  /**
   * Returns an attribute enumeration statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute enumeration statement object.
   */
  public AttributeEnumeration aEnumeration(String name, String userDefinedAlias)
  {
    return this.aEnumeration(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute enumeration statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute enumeration statement object.
   */
  public AttributeEnumeration aEnumeration(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.checkValidAttributeRequest(name, this.mdEntityIF, mdAttributeIF, MdAttributeEnumerationInfo.CLASS);

    return (AttributeEnumeration) this.internalAttributeFactory(name, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute enumeration statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute enumeration statement object.
   */
  public AttributeMultiReference aMultiReference(String name)
  {
    return this.aMultiReference(name, null, null);
  }

  /**
   * Returns an attribute enumeration statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute enumeration statement object.
   */
  public AttributeMultiReference aMultiReference(String name, String userDefinedAlias)
  {
    return this.aMultiReference(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute enumeration statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute enumeration statement object.
   */
  public AttributeMultiReference aMultiReference(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.checkValidAttributeRequest(name, this.mdEntityIF, mdAttributeIF, MdAttributeMultiReferenceInfo.CLASS);

    return (AttributeMultiReference) this.internalAttributeFactory(name, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute enumeration statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute enumeration statement object.
   */
  public AttributeMultiTerm aMultiTerm(String name)
  {
    return this.aMultiTerm(name, null, null);
  }

  /**
   * Returns an attribute enumeration statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute enumeration statement object.
   */
  public AttributeMultiTerm aMultiTerm(String name, String userDefinedAlias)
  {
    return this.aMultiTerm(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute enumeration statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute enumeration statement object.
   */
  public AttributeMultiTerm aMultiTerm(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.checkValidAttributeRequest(name, this.mdEntityIF, mdAttributeIF, MdAttributeMultiTermInfo.CLASS);

    return (AttributeMultiTerm) this.internalAttributeFactory(name, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Used internally by this class. It does the actual work of constructing a
   * primitive object. Returns attributes of any type except
   * AttributeEnumerations.
   * 
   * @param name
   *          name of the attribute.
   * @param mdAttributeIF
   *          metadata that defines the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return query Attribute object.
   */
  private Attribute internalAttributeFactory(String name, MdAttributeDAOIF mdAttributeIF, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return this.internalAttributeFactory(name, mdAttributeIF, null, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns the table alias used for the table that stores instances of this
   * type.
   * 
   * @return table alias used for the table that stores instances of this type.
   */
  public String getTableAlias()
  {
    return this.getTableAlias("", this.mdEntityIF.getTableName());
  }

  /**
   * Used internally by this class. It does the actual work of constructing a
   * primitive object. Returns attributes of any type except
   * AttributeEnumerations.
   * 
   * @param name
   *          name of the attribute.
   * @param mdAttributeIF
   *          metadata that defines the attribute.
   * @param genEntityQuery
   *          generated query class that is used to instantiate specific
   *          AttributeReference AttributeStruct and AttributeEnumeration
   *          objects.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return query Attribute object.
   */
  public Attribute internalAttributeFactory(String name, MdAttributeDAOIF mdAttributeIF, GeneratedEntityQuery genEntityQuery, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdEntityDAOIF definingEntityIF = (MdEntityDAOIF) mdAttributeIF.definedByClass();
    String definingTableName = definingEntityIF.getTableName();
    String definingTableAlias = this.getTableAlias("", definingTableName);

    Set<Join> attrTableJoinSet = new HashSet<Join>();

    // Only join if this attribute is not defined by the bottem level table, as
    // that table does not
    // need to join with itself.
    String thisTypeTable = this.mdEntityIF.getTableName();
    if (!mdAttributeIF.definesAttribute().equals(EntityInfo.ID) && !definingTableName.equals(thisTypeTable))
    {
      Join tableJoin = new InnerJoinEq(EntityDAOIF.ID_COLUMN, thisTypeTable, this.getTableAlias(), EntityDAOIF.ID_COLUMN, definingTableName, definingTableAlias);
      attrTableJoinSet.add(tableJoin);
    }

    Attribute attribute = null;

    if (mdAttributeIF instanceof MdAttributeBooleanDAOIF)
    {
      attribute = new AttributeBoolean((MdAttributeBooleanDAOIF) mdAttributeIF, definingEntityIF.definesType(), definingTableName, definingTableAlias, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeCharacterDAOIF)
    {
      attribute = new AttributeCharacter((MdAttributeCharacterDAOIF) mdAttributeIF, definingEntityIF.definesType(), definingTableName, definingTableAlias, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeTextDAOIF)
    {
      attribute = new AttributeText((MdAttributeTextDAOIF) mdAttributeIF, definingEntityIF.definesType(), definingTableName, definingTableAlias, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeClobDAOIF)
    {
      attribute = new AttributeClob((MdAttributeClobDAOIF) mdAttributeIF, definingEntityIF.definesType(), definingTableName, definingTableAlias, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeDateDAOIF)
    {
      attribute = new AttributeDate((MdAttributeDateDAOIF) mdAttributeIF, definingEntityIF.definesType(), definingTableName, definingTableAlias, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeDateTimeDAOIF)
    {
      attribute = new AttributeDateTime((MdAttributeDateTimeDAOIF) mdAttributeIF, definingEntityIF.definesType(), definingTableName, definingTableAlias, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeTimeDAOIF)
    {
      attribute = new AttributeTime((MdAttributeTimeDAOIF) mdAttributeIF, definingEntityIF.definesType(), definingTableName, definingTableAlias, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeDecimalDAOIF)
    {
      attribute = new AttributeDecimal((MdAttributeDecimalDAOIF) mdAttributeIF, definingEntityIF.definesType(), definingTableName, definingTableAlias, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeDoubleDAOIF)
    {
      attribute = new AttributeDouble((MdAttributeDoubleDAOIF) mdAttributeIF, definingEntityIF.definesType(), definingTableName, definingTableAlias, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeFloatDAOIF)
    {
      attribute = new AttributeFloat((MdAttributeFloatDAOIF) mdAttributeIF, definingEntityIF.definesType(), definingTableName, definingTableAlias, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeIntegerDAOIF)
    {
      attribute = new AttributeInteger((MdAttributeIntegerDAOIF) mdAttributeIF, definingEntityIF.definesType(), definingTableName, definingTableAlias, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeLongDAOIF)
    {
      attribute = new AttributeLong((MdAttributeLongDAOIF) mdAttributeIF, definingEntityIF.definesType(), definingTableName, definingTableAlias, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeBlobDAOIF)
    {
      attribute = new AttributeBlob((MdAttributeBlobDAOIF) mdAttributeIF, definingEntityIF.definesType(), definingTableName, definingTableAlias, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF.getType().equals(MdAttributeLocalCharacterInfo.CLASS) || mdAttributeIF.getType().equals(MdAttributeLocalTextInfo.CLASS))
    {
      MdAttributeLocalDAOIF mdAttributeLocalIF = (MdAttributeLocalDAOIF) mdAttributeIF;
      MdLocalStructDAOIF mdLocalStructIF = mdAttributeLocalIF.getMdStructDAOIF();
      String structTableName = mdLocalStructIF.getTableName();
      String structTableAlias = this.getTableAlias(name, structTableName);

      if (genEntityQuery != null)
      {
        attribute = genEntityQuery.localFactory(mdAttributeLocalIF, definingEntityIF.definesType(), definingTableName, definingTableAlias, mdLocalStructIF, structTableAlias, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
      }
      else
      {
        attribute = this.localFactory(mdAttributeLocalIF, definingEntityIF.definesType(), definingTableName, definingTableAlias, mdLocalStructIF, structTableAlias, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
      }
    }
    else if (mdAttributeIF.getType().equals(MdAttributeStructInfo.CLASS))
    {
      MdAttributeStructDAOIF mdAttributeStructIF = (MdAttributeStructDAOIF) mdAttributeIF;
      MdStructDAOIF mdStructIF = mdAttributeStructIF.getMdStructDAOIF();
      String structTableName = mdStructIF.getTableName();
      String structTableAlias = this.getTableAlias(name, structTableName);

      if (genEntityQuery != null)
      {
        attribute = genEntityQuery.structFactory(mdAttributeStructIF, definingEntityIF.definesType(), definingTableName, definingTableAlias, mdStructIF, structTableAlias, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
      }
      else
      {
        attribute = this.structFactory(mdAttributeStructIF, definingEntityIF.definesType(), definingTableName, definingTableAlias, mdStructIF, structTableAlias, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
      }
    }
    else if (mdAttributeIF.getType().equals(MdAttributeEnumerationInfo.CLASS))
    {
      MdAttributeEnumerationDAOIF mdAttributeEnumerationIF = (MdAttributeEnumerationDAOIF) mdAttributeIF;
      MdEnumerationDAOIF mdEnumerationIF = mdAttributeEnumerationIF.getMdEnumerationDAO();
      String mdEnumerationTableName = mdEnumerationIF.getTableName();

      MdBusinessDAOIF masterListMdBusinessIF = mdEnumerationIF.getMasterListMdBusinessDAO();
      String masterListTalbeAlias = this.getTableAlias(name, masterListMdBusinessIF.getTableName());

      if (genEntityQuery != null)
      {
        attribute = genEntityQuery.enumerationFactory(mdAttributeEnumerationIF, definingEntityIF.definesType(), definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
      }
      else
      {
        attribute = this.enumerationFactory(mdAttributeEnumerationIF, definingEntityIF.definesType(), definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
      }
    }
    else if (mdAttributeIF instanceof MdAttributeMultiReferenceDAO)
    {
      MdAttributeMultiReferenceDAOIF mdAttributeMultiReferenceIF = (MdAttributeMultiReferenceDAOIF) mdAttributeIF;

      String attributeTableName = mdAttributeMultiReferenceIF.getTableName();
      MdBusinessDAOIF referenceMdBusinessIF = mdAttributeMultiReferenceIF.getReferenceMdBusinessDAO();
      String referenceTableName = referenceMdBusinessIF.getTableName();

      if (genEntityQuery != null)
      {
        attribute = genEntityQuery.multiReferenceFactory(mdAttributeMultiReferenceIF, definingEntityIF.definesType(), definingTableName, definingTableAlias, attributeTableName, referenceMdBusinessIF, referenceTableName, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
      }
      else
      {
        attribute = this.multiReferenceFactory(mdAttributeMultiReferenceIF, definingEntityIF.definesType(), definingTableName, definingTableAlias, attributeTableName, referenceMdBusinessIF, referenceTableName, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
      }
    }
    else if (mdAttributeIF instanceof MdAttributeRefDAOIF)
    {
      MdAttributeRefDAOIF mdAttributeRefIF = (MdAttributeRefDAOIF) mdAttributeIF;

      MdBusinessDAOIF referenceMdBusinessIF = mdAttributeRefIF.getReferenceMdBusinessDAO();

      String referenceTableName = referenceMdBusinessIF.getTableName();
      String referenceTableAlias = this.getTableAlias(name, referenceTableName);

      // This is a special case where the ID field is treated like a reference. We want the generic attribute reference to be instantiated
      // as we do not have a concrete attribute reference class defined for treating IDs as references.
      if (genEntityQuery != null && !mdAttributeIF.definesAttribute().equals(ComponentInfo.ID))
      {
        attribute = genEntityQuery.referenceFactory(mdAttributeRefIF, definingEntityIF.definesType(), definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
      }
      else
      {
        attribute = this.referenceFactory(mdAttributeRefIF, definingEntityIF.definesType(), definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
      }
    }

    if (attribute == null)
    {
      for (PluginIF plugin : pluginMap.values())
      {
        attribute = plugin.internalAttributeFactory(name, mdAttributeIF, definingEntityIF, definingTableName, definingTableAlias, this, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);

        if (attribute != null)
        {
          break;
        }
      }
    }

    if (attribute == null)
    {
      String error = "Attribute type [" + mdAttributeIF.getType() + "] is invalid.";
      throw new QueryException(error);
    }

    return attribute;
  }

  /**
   * Returns an AttributeReference with the given values. Generated subclasses
   * with override this method and return subclasses of AttributeReference.
   * 
   * @param mdAttributeIF
   * @param attributeNamespace
   * @param definingTableName
   * @param definingTableAlias
   * @param referenceMdBusinessIF
   * @param referenceTableAlias
   * @param rootEntityQuery
   * @param tableJoinSet
   * @param userDefinedAlias
   * @return AttributeReference with the given values. Generated subclasses with
   *         override this method and return subclasses of AttributeReference.
   */
  public AttributeReference referenceFactory(MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new AttributeReference(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an <code>AttributeStruct</code> with the given values. Generated
   * subclasses with override this method and return subclasses of
   * <code>AttributeStruct</code>.
   * 
   * @param mdAttributeIF
   * @param attributeNamespace
   * @param definingTableName
   * @param definingTableAlias
   * @param mdStructIF
   * @param structTableAlias
   * @param rootEntityQuery
   * @param tableJoinSet
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return <code>AttributeStruct</code> with the given values. Generated
   *         subclasses with override this method and return subclasses of
   *         <code>AttributeStruct</code>.
   */
  public AttributeStruct structFactory(MdAttributeStructDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, MdStructDAOIF mdStructIF, String structTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new AttributeStruct(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdStructIF, structTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an <code>AttributeLocal</code> with the given values. Generated
   * subclasses with override this method and return subclasses of
   * <code>AttributeLocal</code>.
   * 
   * @param mdAttributeIF
   * @param attributeNamespace
   * @param definingTableName
   * @param definingTableAlias
   * @param mdLocalStructIF
   * @param structTableAlias
   * @param rootEntityQuery
   * @param tableJoinSet
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return <code>AttributeLocal</code> with the given values. Generated
   *         subclasses with override this method and return subclasses of
   *         <code>AttributeLocal</code>.
   */
  public AttributeLocal localFactory(MdAttributeLocalDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, MdLocalStructDAOIF mdLocalStructIF, String structTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new AttributeLocal(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdLocalStructIF, structTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an AttributeEnumeration with the given values. Generated subclasses
   * with override this method and return subclasses of AttributeEnumeration.
   * 
   * @param mdAttributeIF
   * @param attributeNamespace
   * @param definingTableName
   * @param definingTableAlias
   * @param mdEnumerationTableName
   * @param masterListMdBusinessIF
   * @param masterListTalbeAlias
   * @param rootEntityQuery
   * @param tableJoinSet
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return AttributeEnumeration with the given values. Generated subclasses
   *         with override this method and return subclasses of
   *         AttributeEnumeration.
   */
  public AttributeEnumeration enumerationFactory(MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdEnumerationTableName, MdBusinessDAOIF masterListMdBusinessIF, String masterListTalbeAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new AttributeEnumeration(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, this, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an AttributeMultiReference with the given values. Generated
   * subclasses with override this method and return subclasses of
   * AttributeMultiReference.
   * 
   * @param mdAttributeIF
   * @param attributeNamespace
   * @param definingTableName
   * @param definingTableAlias
   * @param mdMultiReferenceTableName
   * @param masterListMdBusinessIF
   * @param masterListTalbeAlias
   * @param rootEntityQuery
   * @param tableJoinSet
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return AttributeMultiReference with the given values. Generated subclasses
   *         with override this method and return subclasses of
   *         AttributeMultiReference.
   */
  public AttributeMultiReference multiReferenceFactory(MdAttributeMultiReferenceDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdMultiReferenceTableName, MdBusinessDAOIF masterListMdBusinessIF, String masterListTalbeAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    if (mdAttributeIF instanceof MdAttributeMultiTermDAOIF)
    {
      return new AttributeMultiTerm((MdAttributeMultiTermDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdMultiReferenceTableName, masterListMdBusinessIF, masterListTalbeAlias, this, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }

    return new AttributeMultiReference(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdMultiReferenceTableName, masterListMdBusinessIF, masterListTalbeAlias, this, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a Map with the tableName as the value and the table alias as the
   * key for any tables that need to be included in a query.
   * 
   * @return Map with the tableName as the value and the table alias as the key
   *         for any tables that need to be included in a query.
   */
  protected Map<String, String> getFromTableMapInfoForQuery()
  {
    Map<String, String> fromTableMap = new HashMap<String, String>();

    String typeTableName = this.mdEntityIF.getTableName();
    String typeTableAlias = this.getTableAlias("", typeTableName);

    fromTableMap.put(typeTableAlias, typeTableName);

    return fromTableMap;
  }

  /**
   * Returns the SQL representation of this query, including all attributes of
   * the type in the select clause.
   * 
   * @return SQL representation of this query, including all attributes of the
   *         type in the select clause.
   */
  protected String getSQL(boolean limitRowRange, int limit, int skip)
  {
    List<QueryMdEntityInfo> queryMdEntityInfoList = new LinkedList<QueryMdEntityInfo>();

    List<MdAttributeConcreteDAOIF> totalMdAttributeIFList = new LinkedList<MdAttributeConcreteDAOIF>();

    if (this.instanceQuery)
    {
      queryMdEntityInfoList.add(new QueryMdEntityInfo(this.mdEntityIF, new LinkedList<MdEntityDAOIF>()));
      totalMdAttributeIFList.addAll(this.mdEntityIF.getAllDefinedMdAttributes());
    }
    else
    {
      this.findMdEntitiesForQuery(true, this.mdEntityIF, queryMdEntityInfoList, totalMdAttributeIFList);
      // We need to generate at least one query, so add the type of the query to
      // the list.
      if (queryMdEntityInfoList.size() == 0)
      {
        queryMdEntityInfoList.add(new QueryMdEntityInfo(this.mdEntityIF, new LinkedList<MdEntityDAOIF>()));
      }
    }

    // If there is a super entity, add all attributes inherited from it.
    MdEntityDAOIF superMdEntityIF = this.mdEntityIF.getSuperClass();
    if (superMdEntityIF != null)
    {
      totalMdAttributeIFList.addAll(superMdEntityIF.getAllDefinedMdAttributeMap().values());
    }

    BuildSQLVisitor visitor = this.visitQuery();

    Set<Selectable> leftAttributeSubSelectSet = visitor.leftAttributeSubSelectSet;

    StringBuffer sqlStmt = new StringBuffer();

    // Build the columnInfoMap for all attributes
    Map<String, ColumnInfo> _columnInfoMap = new HashMap<String, ColumnInfo>();

    // Now, for each type, build a select statement and union them together.
    boolean firstIteration = true;
    for (QueryMdEntityInfo queryMdEntityInfo : queryMdEntityInfoList)
    {
      this.getColumnInfoForSelectClause(queryMdEntityInfo.getMdEntityIF(), _columnInfoMap, totalMdAttributeIFList);

      Set<Join> tableJoinSet = new HashSet<Join>();
      tableJoinSet.addAll(visitor.tableJoinSet);
      Map<String, String> fromTableMap = new HashMap<String, String>();

      StringBuffer selectClause = this.buildSelectClause(queryMdEntityInfo.getMdEntityIF(), tableJoinSet, fromTableMap, _columnInfoMap, totalMdAttributeIFList, queryMdEntityInfo.getMdEntityIF().getAllDefinedMdAttributeIDMap());
      StringBuffer fromClause = this.buildFromClause(visitor, tableJoinSet, fromTableMap);

      this.addJoinForSubSelects(queryMdEntityInfo.getMdEntityIF(), tableJoinSet, leftAttributeSubSelectSet);

      StringBuffer joinClause = this.buildJoinClause(tableJoinSet);
      StringBuffer typeRestrictionClause = new StringBuffer(this.buildTypeRestrictionClause(queryMdEntityInfo));
      StringBuffer criteriaClause = new StringBuffer(this.getQueryConditionSQL());

      List<StringBuffer> sqlQueryClauses = new LinkedList<StringBuffer>();

      sqlQueryClauses.add(joinClause);
      sqlQueryClauses.add(typeRestrictionClause);
      sqlQueryClauses.add(criteriaClause);

      if (!firstIteration)
      {
        sqlStmt.append("\nUNION ALL\n");
      }

      sqlStmt.append(selectClause);
      sqlStmt.append("\n" + fromClause);

      this.appendQueryClauses(sqlStmt, sqlQueryClauses);

      firstIteration = false;
    }

    String orderByClause = this.buildOrderByClause();

    sqlStmt = this.appendOderByClause(limitRowRange, limit, skip, sqlStmt, _columnInfoMap, orderByClause);

    this.columnInfoMap = _columnInfoMap;

    return sqlStmt.toString();
  }

  /**
   * Build the select clause for this query (without the SELECT keyword),
   * including all attributes required to instantiate instances of this object.
   * 
   * @param mdAttributeIDMap
   *          Key: MdAttribute.getId() Value: MdAttributeIF
   * @return select clause for this query.
   */
  protected StringBuffer buildSelectClause(List<Selectable> _selectableList, Set<Join> tableJoinSet, Map<String, String> fromTableMap, Map<String, ColumnInfo> _columnInfoMap)
  {
    // Key: ID of an MdAttribute Value: MdEntity that defines the attribute;
    Map<String, MdEntityDAOIF> mdEntityMap = new HashMap<String, MdEntityDAOIF>();

    StringBuffer selectString = new StringBuffer("SELECT \n");

    this.appendDistinctToSelectClause(selectString);

    Set<String> hashSet = new HashSet<String>();

    // Order by fields must also be in the select clause.
    boolean firstIteration = true;

    for (Selectable selectable : _selectableList)
    {
      ComponentQuery componentQuery = selectable.getRootQuery();

      MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

      String attributeQualifiedName = selectable.getFullyQualifiedNameSpace();

      ColumnInfo columnInfo = _columnInfoMap.get(attributeQualifiedName);

      hashSet.add(columnInfo.getColumnAlias());

      if (!firstIteration)
      {
        selectString.append(",\n");
      }

      this.buildSelectColumn(selectString, selectable, mdAttributeIF, columnInfo);

      if (componentQuery instanceof EntityQuery)
      {
        MdEntityDAOIF mdEntityIF = mdEntityMap.get(mdAttributeIF.getId());
        if (mdEntityIF == null)
        {
          mdEntityIF = (MdEntityDAOIF) mdAttributeIF.definedByClass();
          mdEntityMap.put(mdAttributeIF.getId(), mdEntityIF);
        }

        fromTableMap.put(columnInfo.getTableAlias(), columnInfo.getTableName());

        String baseTableName = mdEntityIF.getTableName();
        if (!columnInfo.getColumnName().equals(EntityDAOIF.ID_COLUMN) && !baseTableName.equals(columnInfo.getTableName())
            // For functions, sometimes they are applying either to the ID or to the type itself, and therefore do not need to be joined with the table that defines the ID in metadata
            && !(selectable instanceof Function && ((Function)selectable).getSelectable().getDbColumnName().equals(EntityDAOIF.ID_COLUMN) && selectable.getDefiningTableName().equals(columnInfo.getTableName()) ))
        {
          String baseTableAlias = componentQuery.getTableAlias("", baseTableName);
          Join tableJoin = new InnerJoinEq(EntityDAOIF.ID_COLUMN, baseTableName, baseTableAlias, EntityDAOIF.ID_COLUMN, columnInfo.getTableName(), columnInfo.getTableAlias());
          tableJoinSet.add(tableJoin);
        }
      }

      if (mdAttributeIF instanceof MdAttributeEnumerationDAOIF)
      {
        String cacheColumnName = ( (MdAttributeEnumerationDAOIF) mdAttributeIF ).getCacheColumnName();
        String cacheAttributeQualifiedName = selectable.getAttributeNameSpace() + "." + cacheColumnName;
        ColumnInfo cacheColumnInfo = _columnInfoMap.get(cacheAttributeQualifiedName);
        selectString.append(",\n");

        this.buildSelectColumn(selectString, selectable, mdAttributeIF, cacheColumnInfo);
      }
      else if (mdAttributeIF instanceof MdAttributeStructDAOIF)
      {
        MdAttributeStructDAOIF mdAttributeStructIF = (MdAttributeStructDAOIF) mdAttributeIF;
        MdStructDAOIF mdStructIF = mdAttributeStructIF.getMdStructDAOIF();
        List<? extends MdAttributeConcreteDAOIF> structMdAttributeList = mdStructIF.definesAttributes();

        if (componentQuery instanceof EntityQuery)
        {
          tableJoinSet.addAll(selectable.getJoinStatements());
        }

        for (MdAttributeConcreteDAOIF structMdAttributeIF : structMdAttributeList)
        {
          String structQualifiedAttributeName = attributeQualifiedName + "." + structMdAttributeIF.definesAttribute();
          ColumnInfo structColumnInfo = _columnInfoMap.get(structQualifiedAttributeName);

          if (componentQuery instanceof EntityQuery)
          {
            fromTableMap.put(structColumnInfo.getTableAlias(), structColumnInfo.getTableName());
          }
          selectString.append(",\n");

          Selectable structSelectable = ( (AttributeStruct) selectable ).attributeFactory(structMdAttributeIF.definesAttribute(), structMdAttributeIF.getType(), null, null);
          this.buildSelectColumn(selectString, structSelectable, structMdAttributeIF, structColumnInfo);

          // If the attribute is an enumeration, include the cache column
          if (structMdAttributeIF instanceof MdAttributeEnumerationDAOIF)
          {
            MdAttributeEnumerationDAOIF structEnumMdAttributeIF = (MdAttributeEnumerationDAOIF) structMdAttributeIF;
            String structEnumQualifiedAttributeName = attributeQualifiedName + "." + structEnumMdAttributeIF.getCacheColumnName();
            ColumnInfo structEnumColumnInfo = _columnInfoMap.get(structEnumQualifiedAttributeName);

            if (componentQuery instanceof EntityQuery)
            {
              fromTableMap.put(structEnumColumnInfo.getTableAlias(), structEnumColumnInfo.getTableName());
            }
            selectString.append(",\n");

            this.buildSelectColumn(selectString, structSelectable, structMdAttributeIF, structColumnInfo);
          }
        }
      }

      firstIteration = false;
    }

    this.addOrderByAttributesToSelectClause(selectString, hashSet, this.orderByList, firstIteration);

    return selectString;
  }
  
  /**
   * 
   * @param limitRowRange
   * @param limit
   * @param skip
   * @param sqlStmt
   * @param _columnInfoMap
   * @param orderByClause
   * @return
   */
  protected StringBuffer appendOderByClause(boolean limitRowRange, int limit, int skip, StringBuffer sqlStmt, Map<String, ColumnInfo> _columnInfoMap, String orderByClause)
  {
    // Don't do anything if no columns were selected.
    if (_columnInfoMap.size() == 0)
    {
      return sqlStmt;
    }

    // Restrict the number of rows returned from the database.
    if (limitRowRange)
    {
      String selectClauseAttributes = "";
      boolean firstIteration = true;
      ColumnInfo fistColumnInfo = null;
      for (ColumnInfo columnInfo : _columnInfoMap.values())
      {
        if (!firstIteration)
        {
          selectClauseAttributes += ", ";
        }
        else
        {
          fistColumnInfo = columnInfo;
        }
        selectClauseAttributes += columnInfo.getColumnAlias();

        firstIteration = false;
      }

      if (orderByClause.trim().length() == 0)
      {
        orderByClause = "ORDER BY " + fistColumnInfo.getColumnAlias() + " ASC";
      }

      sqlStmt = Database.buildRowRangeRestriction(sqlStmt, limit, skip, selectClauseAttributes, orderByClause);
    }
    else
    {
      sqlStmt.append("\n" + orderByClause);
    }
    return sqlStmt;
  }
  
  /**
   * Adds join criteria for subselects such that the lefthand selectable is in
   * the join clause.
   * 
   * @param _mdEntityDAOIF
   * @param tableJoinSet
   * @param _columnInfoMap
   * @param leftAttributeSubSelectSet
   */
  private void addJoinForSubSelects(MdEntityDAOIF _mdEntityDAOIF, Set<Join> tableJoinSet, Set<Selectable> leftAttributeSubSelectSet)
  {
    String baseTableName = _mdEntityDAOIF.getTableName();

    for (Selectable selectable : leftAttributeSubSelectSet)
    {
      // Do not exclude the join if the selectable is for the ID attribute, as
      // of right now there is no guarantee that a join has been defined between
      // the selectable's table and the base table for this query.
      if (!baseTableName.equals(selectable.getDefiningTableName()))
      {
        String baseTableAlias = this.getTableAlias("", baseTableName);

        Join tableJoin = new InnerJoinEq(EntityDAOIF.ID_COLUMN, baseTableName, baseTableAlias, EntityDAOIF.ID_COLUMN, selectable.getDefiningTableName(), selectable.getDefiningTableAlias());
        tableJoinSet.add(tableJoin);
      }
    }
  }

  /**
   * Initializes the columnInfoMap to include all attributes required to build a
   * select clause that includes all attributes of the component.
   */
  protected void getColumnInfoForSelectClause(MdEntityDAOIF _mdEntityIF, Map<String, ColumnInfo> _columnInfoMap, List<MdAttributeConcreteDAOIF> totalMdAttributeIFList)
  {
    // Populate the columInfoMap with all attributes involved in the query thus
    // far.
    for (MdAttributeConcreteDAOIF mdAttributeIF : totalMdAttributeIFList)
    {
      // Get all aliases for tables for attributes involved in the select clause
      // (but not struct tables)
      MdEntityDAOIF definingMdEntityIF = (MdEntityDAOIF) mdAttributeIF.definedByClass();
      String definingTableName = definingMdEntityIF.getTableName();
      String definingTableAlias = this.getTableAlias("", definingTableName);

      String attributeNameSpace = definingMdEntityIF.definesType();
      String attributeQualifiedName = definingMdEntityIF.definesType() + "." + mdAttributeIF.definesAttribute();

      String columnNameAlias = null;
      if (mdAttributeIF.definesAttribute().equals(EntityInfo.ID))
      {
        columnNameAlias = EntityDAOIF.ID_COLUMN;
      }
      else
      {
        columnNameAlias = this.getColumnAlias(attributeNameSpace, mdAttributeIF.getColumnName());
      }

      this.buildColumnInfoForAttribute(_columnInfoMap, mdAttributeIF.definesAttribute(), mdAttributeIF.getColumnName(), definingTableName, definingTableAlias, attributeNameSpace, columnNameAlias, attributeQualifiedName, this, mdAttributeIF);
    }
  }

  /**
   * Build the select clause for this query (without the SELECT keyword),
   * including all attributes required to instantiate instances of this object.
   * 
   * @param mdAttributeIDMap
   *          Key: MdAttribute.getId() Value: MdAttributeIF
   * @return select clause for this query.
   */
  protected StringBuffer buildSelectClause(MdEntityDAOIF _mdEntityIF, Set<Join> tableJoinSet, Map<String, String> fromTableMap, Map<String, ColumnInfo> _columnInfoMap, List<MdAttributeConcreteDAOIF> totalMdAttributeIFList, Map<String, ? extends MdAttributeConcreteDAOIF> mdAttributeIDMap)
  {
    // Key: ID of an MdAttribute Value: MdEntity that defines the attribute;
    Map<String, MdEntityDAOIF> mdEntityMap = new HashMap<String, MdEntityDAOIF>();

    StringBuffer selectString = new StringBuffer("SELECT \n");

    Set<String> hashSet = new HashSet<String>();

    // Order by fields must also be in the select clause.
    boolean firstIteration = true;

    for (MdAttributeConcreteDAOIF mdAttributeIF : totalMdAttributeIFList)
    {
      MdEntityDAOIF mdEntityIF = mdEntityMap.get(mdAttributeIF.getId());
      if (mdEntityIF == null)
      {
        mdEntityIF = (MdEntityDAOIF) mdAttributeIF.definedByClass();
        mdEntityMap.put(mdAttributeIF.getId(), mdEntityIF);
      }
      String attributeNameSpace = mdEntityIF.definesType();

      String attributeQualifiedName = attributeNameSpace + "." + mdAttributeIF.definesAttribute();

      ColumnInfo columnInfo = _columnInfoMap.get(attributeQualifiedName);

      hashSet.add(columnInfo.getColumnAlias());

      if (!firstIteration)
      {
        selectString.append(",\n");
      }

      if (mdAttributeIDMap.get(mdAttributeIF.getId()) != null)
      {
        selectString.append(selectIndent + columnInfo.getSelectClauseString(mdAttributeIF));

        fromTableMap.put(columnInfo.getTableAlias(), columnInfo.getTableName());

        String baseTableName = _mdEntityIF.getTableName();
        if (!columnInfo.getColumnName().equals(EntityDAOIF.ID_COLUMN) && !baseTableName.equals(columnInfo.getTableName()))
        {
          String baseTableAlias = this.getTableAlias("", baseTableName);
          Join tableJoin = new InnerJoinEq(EntityDAOIF.ID_COLUMN, baseTableName, baseTableAlias, EntityDAOIF.ID_COLUMN, columnInfo.getTableName(), columnInfo.getTableAlias());
          tableJoinSet.add(tableJoin);
        }

        if (mdAttributeIF instanceof MdAttributeEnumerationDAOIF)
        {
          String cacheColumnName = ( (MdAttributeEnumerationDAOIF) mdAttributeIF ).getCacheColumnName();
          String cacheAttributeQualifiedName = attributeNameSpace + "." + cacheColumnName;

          ColumnInfo cacheColumnInfo = _columnInfoMap.get(cacheAttributeQualifiedName);
          selectString.append(",\n" + selectIndent + cacheColumnInfo.getSelectClauseString(mdAttributeIF));
        }
        else if (mdAttributeIF instanceof MdAttributeStructDAOIF)
        {
          MdAttributeStructDAOIF mdAttributeStructIF = (MdAttributeStructDAOIF) mdAttributeIF;
          MdStructDAOIF mdStructIF = mdAttributeStructIF.getMdStructDAOIF();
          List<? extends MdAttributeConcreteDAOIF> structMdAttributeList = mdStructIF.definesAttributes();

          String structTable = mdStructIF.getTableName();
          String structTableAlias = this.getTableAlias(mdAttributeIF.definesAttribute(), structTable);

          Join structTableJoin = new LeftJoinEq(mdAttributeIF.getColumnName(), columnInfo.getTableName(), columnInfo.getTableAlias(), EntityDAOIF.ID_COLUMN, structTable, structTableAlias);

          tableJoinSet.add(structTableJoin);

          for (MdAttributeConcreteDAOIF structMdAttributeIF : structMdAttributeList)
          {
            String structQualifiedAttributeName = attributeQualifiedName + "." + structMdAttributeIF.definesAttribute();
            ColumnInfo structColumnInfo = _columnInfoMap.get(structQualifiedAttributeName);

            fromTableMap.put(structColumnInfo.getTableAlias(), structColumnInfo.getTableName());

            selectString.append(",\n" + selectIndent + structColumnInfo.getSelectClauseString(structMdAttributeIF));

            // If the attribute is an enumeration, include the cache column
            if (structMdAttributeIF instanceof MdAttributeEnumerationDAOIF)
            {
              MdAttributeEnumerationDAOIF structEnumMdAttributeIF = (MdAttributeEnumerationDAOIF) structMdAttributeIF;
              String structEnumQualifiedAttributeName = attributeQualifiedName + "." + structEnumMdAttributeIF.getCacheColumnName();
              ColumnInfo structEnumColumnInfo = _columnInfoMap.get(structEnumQualifiedAttributeName);
              fromTableMap.put(structEnumColumnInfo.getTableAlias(), structEnumColumnInfo.getTableName());
              selectString.append(",\n" + selectIndent + structEnumColumnInfo.getSelectClauseString(structMdAttributeIF));
            }

          }
        }
      }
      else
      {
        selectString.append(selectIndent + Database.formatColumnAlias(columnInfo.getColumnAlias(), mdAttributeIF.getType()));

        if (mdAttributeIF instanceof MdAttributeEnumerationDAOIF)
        {
          String cacheColumnName = ( (MdAttributeEnumerationDAOIF) mdAttributeIF ).getCacheColumnName();

          String cacheAttributeQualifiedName = attributeNameSpace + "." + cacheColumnName;

          ColumnInfo cacheColumnInfo = _columnInfoMap.get(cacheAttributeQualifiedName);
          selectString.append(",\n" + selectIndent + Database.formatColumnAlias(cacheColumnInfo.getColumnAlias(), MdAttributeEnumerationInfo.CACHE_COLUMN_DATATYPE));
        }
        else if (mdAttributeIF instanceof MdAttributeStructDAOIF)
        {
          MdAttributeStructDAOIF mdAttributeStructIF = (MdAttributeStructDAOIF) mdAttributeIF;
          MdStructDAOIF mdStructIF = mdAttributeStructIF.getMdStructDAOIF();
          List<? extends MdAttributeDAOIF> structMdAttributeList = mdStructIF.definesAttributes();

          for (MdAttributeDAOIF structMdAttributeIF : structMdAttributeList)
          {
            String structQualifiedAttributeName = attributeQualifiedName + "." + structMdAttributeIF.definesAttribute();
            ColumnInfo structColumnInfo = _columnInfoMap.get(structQualifiedAttributeName);

            selectString.append(",\n" + selectIndent + Database.formatColumnAlias(structColumnInfo.getColumnAlias(), structMdAttributeIF.getType()));

            // If the attribute is an enumeration, include the cache column
            if (structMdAttributeIF instanceof MdAttributeEnumerationDAOIF)
            {
              MdAttributeEnumerationDAOIF structEnumMdAttributeIF = (MdAttributeEnumerationDAOIF) structMdAttributeIF;
              String structEnumQualifiedAttributeName = attributeQualifiedName + "." + structEnumMdAttributeIF.getCacheColumnName();
              ColumnInfo structEnumColumnInfo = _columnInfoMap.get(structEnumQualifiedAttributeName);

              selectString.append(",\n" + selectIndent + Database.formatColumnAlias(structEnumColumnInfo.getColumnAlias(), structEnumMdAttributeIF.getType()));
            }

          }
        }
      }

      firstIteration = false;
    }

    this.addOrderByAttributesToSelectClause(selectString, hashSet, this.orderByList, firstIteration);

    return selectString;
  }

  /**
   * Adds the DISTINCT keyword to the select string, if required. DISTINCT is
   * not used when querying for entities. This method is here just so that code
   * can be shared in common with <code>EntityQuery</code> and
   * <code>ValueQuery</code>.
   * 
   * @param selectString
   */
  protected void appendDistinctToSelectClause(StringBuffer selectString)
  {
  }

  /**
   * Determines what should be in the group by clause.
   */
  @Override
  protected void computeGroupByClauseForCount()
  {
  }

  /**
   * Adds selectables to the select clause when the count() function has a group
   * by clause.
   */
  @Override
  protected String addGroupBySelectablesToSelectClauseForCount()
  {
    return "";
  }

  /**
   * Builds the group by clause for this query.
   * 
   * @return group by clause for this query.
   */
  protected String buildGroupByClause()
  {
    // Entity queries do not have group by clauses
    return "";
  }

  /**
   * Returns the SQL to count the objects that match the specified criteria.
   * 
   * @return sql for counting objects match the specified criteria.
   */
  @Override
  public String getCountSQL()
  {
    BuildSQLVisitor visitor = this.visitQuery();

    Set<Join> tableJoinSet = new HashSet<Join>();

    Map<String, String> fromTableMap = this.getFromTableMapInfoForQuery();

    tableJoinSet.addAll(visitor.tableJoinSet);

    Set<Selectable> leftAttributeSubSelectSet = visitor.leftAttributeSubSelectSet;

    this.addCustomJoins(tableJoinSet);

    StringBuffer selectStmt = new StringBuffer("SELECT COUNT(*) " + Database.formatColumnAlias("ct") + "\n");

    selectStmt.append(this.buildFromClause(visitor, tableJoinSet, fromTableMap));

    List<StringBuffer> sqlQueryClauses = new LinkedList<StringBuffer>();

    this.addJoinForSubSelects(this.getMdEntityIF(), tableJoinSet, leftAttributeSubSelectSet);

    StringBuffer joinClause = this.buildJoinClause(tableJoinSet);
    StringBuffer queryCriteria = new StringBuffer(this.getQueryConditionSQL());

    sqlQueryClauses.add(joinClause);
    sqlQueryClauses.add(queryCriteria);

    this.appendQueryClauses(selectStmt, sqlQueryClauses);

    return selectStmt.toString();
  }

  /**
   * @return returns a rank function 
   */
  public RANK RANK()
  {
    return new RANK(this);
  }
  
  /**
   * @return returns a rank function 
   */
  public RANK RANK(String userDefinedAlias)
  {
    return new RANK(this, userDefinedAlias);
  }
  
  /**
   * Returns the SQL representation of this query to be used within a left join.
   * <code>Selectable</code> array represents the attributes in the select
   * clause that come from this entity.
   * 
   * @return SQL representation of this query to be used within a left join.
   */
  protected String getSQL(List<Selectable> selectableList)
  {
    for (Selectable selectable : selectableList)
    {
      MdAttributeDAOIF mdAttributeIF = selectable.getMdAttributeIF();

      if (mdAttributeIF instanceof MdAttributeConcrete_Q)
      {
        ( (MdAttributeConcrete_Q) mdAttributeIF ).resetAttributeNameAndLabel();
      }
    }

    BuildSQLVisitor visitor = this.visitQuery();
    for (Selectable selectable : selectableList)
    {
      selectable.accept(visitor);
    }

    Set<Join> tableJoinSet = new HashSet<Join>();

    tableJoinSet.addAll(visitor.tableJoinSet);

    Set<Selectable> leftAttributeSubSelectSet = visitor.leftAttributeSubSelectSet;

    StringBuffer sqlStmt = new StringBuffer();

    // Build the columnInfoMap for all attributes
    Map<String, ColumnInfo> _columnInfoMap = new HashMap<String, ColumnInfo>();
    this.getColumnInfoForSelectClause(selectableList, _columnInfoMap);

    Map<String, String> fromTableMap = new HashMap<String, String>();

    StringBuffer selectClause = this.buildSelectClause(selectableList, tableJoinSet, fromTableMap, _columnInfoMap);

    StringBuffer fromClause = this.buildFromClause(visitor, tableJoinSet, fromTableMap);

    this.addJoinForSubSelects(this.getMdEntityIF(), tableJoinSet, leftAttributeSubSelectSet);

    StringBuffer joinClause = this.buildJoinClause(tableJoinSet);
    StringBuffer criteriaClause = new StringBuffer(this.getQueryConditionSQL());

    List<StringBuffer> sqlQueryClauses = new LinkedList<StringBuffer>();

    sqlQueryClauses.add(joinClause);
    sqlQueryClauses.add(criteriaClause);

    sqlStmt.append(selectClause);
    sqlStmt.append("\n" + fromClause);

    this.appendQueryClauses(sqlStmt, sqlQueryClauses);

    this.columnInfoMap = _columnInfoMap;

    return sqlStmt.toString();
  }

  /**
   * Initializes the columnInfoMap to include all attributes required to build a
   * select clause that includes all attributes of the component.
   */
  protected void getColumnInfoForSelectClause(List<Selectable> selectableList, Map<String, ColumnInfo> _columnInfoMap)
  {
    for (Selectable selectable : selectableList)
    {
      ComponentQuery componentQuery = selectable.getRootQuery();

      String columnAlias = selectable.getColumnAlias();

      String fullyQualifiedAttributeNamespace = selectable.getFullyQualifiedNameSpace();

      MdAttributeDAOIF mdAttributeIF = selectable.getMdAttributeIF();

      this.buildColumnInfoForAttribute(_columnInfoMap, selectable._getAttributeName(), selectable.getDbColumnName(), selectable.getDefiningTableName(), selectable.getDefiningTableAlias(), selectable.getAttributeNameSpace(), columnAlias, fullyQualifiedAttributeNamespace, componentQuery, mdAttributeIF);
    }
  }
  
  /**
   * 
   * @param _columnInfoMap
   * @param attributePrimitive
   * @param componentQuery
   * @param columnAlias
   * @param fullyQualifiedAttributeNamespace
   * @param mdAttributeIF
   */
  protected void buildColumnInfoForAttribute(Map<String, ColumnInfo> _columnInfoMap, String attributeName, String columnName, String attributeDefiningTableName, String attributeDefiningTableAlias, String attributeNamespace, String columnAlias, String fullyQualifiedAttributeNamespace, ComponentQuery componentQuery, MdAttributeDAOIF mdAttributeIF)
  {
    ColumnInfo columnInfo = new ColumnInfo(attributeDefiningTableName, attributeDefiningTableAlias, columnName, columnAlias);
    _columnInfoMap.put(fullyQualifiedAttributeNamespace, columnInfo);

    if (mdAttributeIF instanceof MdAttributeEnumerationDAOIF)
    {
      String cacheColumnName = ( (MdAttributeEnumerationDAOIF) mdAttributeIF ).getCacheColumnName();
      String cacheColumnAlias = componentQuery.getColumnAlias(attributeNamespace, cacheColumnName);

      String cacheAttributeQualifiedName = attributeNamespace + "." + cacheColumnName;

      ColumnInfo cacheColumnInfo = new ColumnInfo(attributeDefiningTableName, attributeDefiningTableAlias, cacheColumnName, cacheColumnAlias);
      _columnInfoMap.put(cacheAttributeQualifiedName, cacheColumnInfo);
    }
    else if (mdAttributeIF instanceof MdAttributeStructDAOIF)
    {
      MdStructDAOIF mdStructIF = ( (MdAttributeStructDAOIF) mdAttributeIF ).getMdStructDAOIF();
      String structTableName = mdStructIF.getTableName();

      String structTableAlias = componentQuery.getTableAlias(attributeName, structTableName);

      List<? extends MdAttributeConcreteDAOIF> structMdAttributeList = mdStructIF.getAllDefinedMdAttributes();

      for (MdAttributeConcreteDAOIF mdAttributeStructIF : structMdAttributeList)
      {
        String structSelectAliasSpace = fullyQualifiedAttributeNamespace + "." + mdAttributeStructIF.definesAttribute();

        ColumnInfo structColumnInfo = new ColumnInfo(mdStructIF.getTableName(), structTableAlias, mdAttributeStructIF.getColumnName(), componentQuery.getColumnAlias(fullyQualifiedAttributeNamespace, mdAttributeStructIF.getColumnName()));
        _columnInfoMap.put(structSelectAliasSpace, structColumnInfo);

        // Put in a record for the cache column attribute.
        if (mdAttributeStructIF instanceof MdAttributeEnumerationDAOIF)
        {
          String cacheColumnName = ( (MdAttributeEnumerationDAOIF) mdAttributeStructIF ).getCacheColumnName();

          String structEnumSelectAliasSpace = fullyQualifiedAttributeNamespace + "." + cacheColumnName;

          String cacheColumnAlias = componentQuery.getColumnAlias(fullyQualifiedAttributeNamespace, cacheColumnName);

          ColumnInfo structCacheColumnInfo = new ColumnInfo(mdStructIF.getTableName(), structTableAlias, cacheColumnName, cacheColumnAlias);
          _columnInfoMap.put(structEnumSelectAliasSpace, structCacheColumnInfo);
        }

      } // for (MdAttributeIF mdAttributeStructIF : structMdAttributeList)
    } // if (mdAttributeIF instanceof MdAttributeStructIF)
  }
  
  /**
   * Finds out which types in the hierarchy should have queries generated.
   * 
   * @param rootNode
   *          true if this is the first non-recursive call. Hey, I know it is a
   *          little getto but it works.
   * @param _mdEntityIF
   *          entity currently being evaluated as to whether a query should be
   *          generated.
   * @param totalMdAttributeROMap
   *          map of all attributes defined by the classes in the hierarchy
   *          starting with the _mdEntityIF value passed in the first
   *          non-recursive call.
   */
  private FindMdEntitiesForQuery findMdEntitiesForQuery(boolean rootNode, MdEntityDAOIF _mdEntityIF, List<QueryMdEntityInfo> queryMdEntityInfoList, List<MdAttributeConcreteDAOIF> totalMdAttributeIFList)
  {
    List<? extends MdEntityDAOIF> subEntityList = _mdEntityIF.getSubClasses();

    // list of types to exclude
    List<MdEntityDAOIF> excludeEntityList = new LinkedList<MdEntityDAOIF>();
    // list of types that have not yet been accounted for in a query
    List<MdEntityDAOIF> unaccountedForEntityList = new LinkedList<MdEntityDAOIF>();

    for (MdEntityDAOIF childMdEntityIF : subEntityList)
    {
      FindMdEntitiesForQuery findMdEntitiesForQuery = this.findMdEntitiesForQuery(false, childMdEntityIF, queryMdEntityInfoList, totalMdAttributeIFList);

      excludeEntityList.addAll(findMdEntitiesForQuery.getExcludeEntityList());
      unaccountedForEntityList.addAll(findMdEntitiesForQuery.getUnaccountedForEntityList());
    }

    // If I define attributes, add them to the list.
    Map<String, ? extends MdAttributeConcreteDAOIF> definedMdAttributeMap = _mdEntityIF.getDefinedMdAttributeMap();
    if (definedMdAttributeMap.size() > 0)
    {
      totalMdAttributeIFList.addAll(definedMdAttributeMap.values());
    }

    // Make sure all of the needed system attributes are there.  In a rare case when there is a relationship
    // type defined where {@link MdAttributeConcreteDAOIF} is the parent and the attribute TYPE is deleted, then the
    // query to delete instances of the type will fail, as the necessary attribute metadata will not be there..
    if (_mdEntityIF.getSuperClass() == null)
    {
      // A convoluted way to get system attributes directly from the global cache, which is not affected by the transaction.
      CacheAllRelationshipDAOStrategy relationshipStrategy = (CacheAllRelationshipDAOStrategy)ObjectCache.getTypeCollection(RelationshipTypes.CLASS_ATTRIBUTE_CONCRETE.getType());
      
      List<RelationshipDAOIF> mdAttributeRels = relationshipStrategy.getChildren(_mdEntityIF.getId(), RelationshipTypes.CLASS_ATTRIBUTE_CONCRETE.getType());
      
      for (RelationshipDAOIF relationshipDAOIF : mdAttributeRels)
      {
        MdAttributeConcreteDAOIF mdAttributeConcreteDAOIF = (MdAttributeConcreteDAOIF)relationshipDAOIF.getChild();
        // Add the system attribute if it was not already there.
        if (mdAttributeConcreteDAOIF.isSystem() && !definedMdAttributeMap.containsKey(mdAttributeConcreteDAOIF.definesAttribute().toLowerCase()))
        {
          totalMdAttributeIFList.add(mdAttributeConcreteDAOIF);
        }
      }
    }
    
    // If I do not have any attributes, then querying for records in a super
    // table will contain records
    // of my type.
    if (definedMdAttributeMap.size() == 0)
    {
      // If I am concrete, then then add me to the unaccounted for list, as a
      // query will eventually need
      // to be made to account for me
      if (!_mdEntityIF.isAbstract())
      {
        unaccountedForEntityList.add(_mdEntityIF);
      }
      // If I am abstract and I do not define attributes, then there is no need
      // to query for me
    }
    else
    {
      // Concrete
      if (!_mdEntityIF.isAbstract())
      {
        // Exclude any types below me that should be excluded
        queryMdEntityInfoList.add(new QueryMdEntityInfo(_mdEntityIF, excludeEntityList));
        // All subtypes should now be accounted for
        unaccountedForEntityList.clear();
        excludeEntityList = new LinkedList<MdEntityDAOIF>();

        excludeEntityList.add(_mdEntityIF);
      }
      // Abstract
      else
      {
        // If this abstract class defines attributes and there are unaccounted
        // subclasses, then we will query
        // for this type
        if (unaccountedForEntityList.size() > 0)
        {
          // Exclude any types below me that should be excluded
          queryMdEntityInfoList.add(new QueryMdEntityInfo(_mdEntityIF, excludeEntityList));

          // All subtypes should now be accounted for
          unaccountedForEntityList.clear();
          excludeEntityList = new LinkedList<MdEntityDAOIF>();

          excludeEntityList.add(_mdEntityIF);
        }
        // If there are no unaccounted types, then attributes defined on this
        // type are already included in other queries
      }
    }

    return new FindMdEntitiesForQuery(excludeEntityList, unaccountedForEntityList);
  }

  /**
   * Restricts the types of the instances that participate in the query.
   * 
   * @param queryMdEntityInfo
   * @return SQL clause that restricts the types of the instances that
   *         participate in the query.
   */
  protected String buildTypeRestrictionClause(QueryMdEntityInfo queryMdEntityInfo)
  {
    List<MdEntityDAOIF> excludeEntityTypeList = queryMdEntityInfo.getExcludeEntityTypeList();

    MdEntityDAOIF rootMdEntityIF = queryMdEntityInfo.getMdEntityIF();
    String rootDefiningTableName = rootMdEntityIF.getTableName();
    String rootTableAlias = this.getTableAlias("", rootDefiningTableName);

    String typeRestrictionClause = "";
    boolean firstIteration = true;
    for (MdEntityDAOIF excludedMdEntityType : excludeEntityTypeList)
    {
      String definingTableName = excludedMdEntityType.getTableName();

      if (!firstIteration)
      {
        typeRestrictionClause += "\nAND ";
      }
      else
      {
        firstIteration = false;
      }

      typeRestrictionClause += rootTableAlias + "." + EntityInfo.ID + " NOT IN " + "(SELECT " + definingTableName + "." + EntityInfo.ID + " FROM " + definingTableName + ")";
    }
    return typeRestrictionClause;
  }

  /**
   * Is the return value for the findMdEntitiesForQuery method. Returns a list
   * of classes to exclude from a query, as well as classes that have not yet
   * been accounted for in a query.
   * 
   * @author nathan
   * 
   */
  private class FindMdEntitiesForQuery
  {
    private List<MdEntityDAOIF> excludeEntityList;

    private List<MdEntityDAOIF> unaccountedForEntityList;

    /**
     * 
     * @param definesAttributes
     * @param entityTypeList
     */
    public FindMdEntitiesForQuery(List<MdEntityDAOIF> excludeEntityList, List<MdEntityDAOIF> unaccountedForEntityList)
    {
      this.excludeEntityList = excludeEntityList;
      this.unaccountedForEntityList = unaccountedForEntityList;
    }

    /**
     * Returns the list of entitiess to be excluded in a query.
     * 
     * @return list of entitys to be excluded in a query.
     */
    public List<MdEntityDAOIF> getExcludeEntityList()
    {
      return this.excludeEntityList;
    }

    /**
     * Returns list of entities the list of entity types that should be excluded
     * for the given tree node.
     * 
     * @return list of entity types that should be excluded for the given tree
     *         node.
     */
    public List<MdEntityDAOIF> getUnaccountedForEntityList()
    {
      return this.unaccountedForEntityList;
    }
  }

  public String getOrderBySQL(OrderBy orderBy)
  {
    return orderBy.getSQL();
  }

  /**
   * Throws <code>QueryException</code> if this <code>EntityQuery</code> is used
   * in a <code>ValueQuery</code>.
   */
  protected void checkNotUsedInValueQuery()
  {
    if (this.isUsedInValueQuery())
    {
      String errMsg = "Entity query for [" + this.getType() + "]is used for value queries and cannot be used for querying objects.";
      throw new QueryException(errMsg);
    }
  }

  public static interface PluginIF
  {
    public String getModuleIdentifier();

    public Attribute internalAttributeFactory(String name, MdAttributeDAOIF mdAttributeIF, MdEntityDAOIF definingEntityIF, String definingTableName, String definingTableAlias, ComponentQuery rootQuery, Set<Join> attrTableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel);
  }
}
