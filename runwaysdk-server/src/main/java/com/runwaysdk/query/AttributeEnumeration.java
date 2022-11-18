/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.MdAttributeUUIDInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.dataaccess.AttributeDoesNotExistException;
import com.runwaysdk.dataaccess.MdAttributeBlobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeClobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecimalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFloatDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLongDAOIF;
import com.runwaysdk.dataaccess.MdAttributeRefDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTextDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.database.Database;

/**
 * You can only sort/order by attributes defined by the enumeration master list
 * class, and not on any attributes that are inherited from the enumeration
 * master's super class.
 *
 * @author nathan
 *
 */
public class AttributeEnumeration extends AttributeRef implements SelectableEnumeration, HasAttributeFactory
{
  private static Map<String, AttributeEnumeration.PluginIF> pluginMap = new ConcurrentHashMap<String, AttributeEnumeration.PluginIF>();

  public static void registerPlugin(AttributeEnumeration.PluginIF pluginFactory)
  {
    pluginMap.put(pluginFactory.getModuleIdentifier(), pluginFactory);
  }

  protected String mdEnumerationTableName;

  private String   cacheColumnName;

  private String   cacheColumnAlias;

  protected AttributeEnumeration(MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNameSpace, String definingTableName, String definingTableAlias, String mdEnumerationTableName, MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    this(mdAttributeIF, attributeNameSpace, definingTableName, definingTableAlias, mdEnumerationTableName, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, null);
  }

  protected AttributeEnumeration(MdAttributeEnumerationDAOIF mdAttributeDAOIF, String attributeNameSpace, String definingTableName, String definingTableAlias, String mdEnumerationTableName, MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel, MdAttributeStructDAOIF mdAttributeStructIF)
  {
    super(mdAttributeDAOIF, attributeNameSpace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, mdAttributeStructIF);

    this.mdEnumerationTableName = mdEnumerationTableName;

    this.cacheColumnName = ( (MdAttributeEnumerationDAOIF) mdAttributeDAOIF ).getCacheColumnName();

    this.cacheColumnAlias = rootQuery.getColumnAlias(attributeNameSpace, this.cacheColumnName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCacheColumnName()
  {
    return this.cacheColumnName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCacheColumnAlias()
  {
    return this.cacheColumnAlias;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ColumnInfo getCacheColumnInfo()
  {
    return new ColumnInfo(this.getDefiningTableName(), this.getDefiningTableAlias(), this.getCacheColumnName(), this.getCacheColumnAlias());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ColumnInfo> getColumnInfoList()
  {
    List<ColumnInfo> columnInfoList = super.getColumnInfoList();
    columnInfoList.add(this.getCacheColumnInfo());
    return columnInfoList;
  }

  /**
   * Compares the oid of a component for equality.
   * 
   * @param oid
   *          oid of the object to compare.
   * @return Basic Condition object
   */
  public BasicCondition EQ(String oid)
  {
    String formattedValue = Database.formatJavaToSQL(oid, MdAttributeCharacterInfo.CLASS, false);
    StatementPrimitive statementPrimitive = new StatementPrimitive(formattedValue);
    return new BasicConditionEq(this, statementPrimitive, false);
  }

  /**
   * Compares the oid of a component for equality.
   * 
   * @param oid
   *          oid of the object to compare.
   * @return Basic Condition object
   */
  public BasicCondition NE(String oid)
  {
    String formattedValue = Database.formatJavaToSQL(oid, MdAttributeCharacterInfo.CLASS, false);
    StatementPrimitive statementPrimitive = new StatementPrimitive(formattedValue);
    return new BasicConditionNotEq(this, statementPrimitive, false);
  }

  /**
   * Returns an attribute statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute statement object.
   */
  public Attribute get(String name)
  {
    return this.get(name, null, null);
  }

  /**
   * Returns an attribute statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   *          user defined alias.
   * @return Attribute statement object.
   */
  public Attribute get(String name, String userDefinedAlias)
  {
    return this.get(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   *          user defined alias.
   * @param userDefinedDisplayLabel
   * @return Attribute statement object.
   */
  public Attribute get(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    if (mdAttributeIF == null)
    {
      String error = "An attribute named [" + name + "] does not exist on type [" + this.referenceMdBusinessIF.definesType() + "]";
      throw new AttributeDoesNotExistException(error, name, this.referenceMdBusinessIF);
    }

    MdEntityDAOIF definingMdEntity = (MdEntityDAOIF) mdAttributeIF.definedByClass();

    return this.internalAttributeFactory(name, mdAttributeIF, definingMdEntity, userDefinedAlias, userDefinedDisplayLabel);
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
   *          user defined alias.
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
   *          user defined alias.
   * @param userDefinedDisplayLabel
   * @return Attribute character statement object.
   */
  public AttributeCharacter aCharacter(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    this.rootQuery.checkValidAttributeRequest(name, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeCharacterInfo.CLASS);

    MdEntityDAOIF definingMdEntity = (MdEntityDAOIF) mdAttributeIF.definedByClass();

    return (AttributeCharacter) this.internalAttributeFactory(name, mdAttributeIF, definingMdEntity, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute character statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute character statement object.
   */
  public AttributeUUID aUUID(String name)
  {
    return this.aUUID(name, null, null);
  }

  /**
   * Returns an attribute character statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   *          user defined alias.
   * @return Attribute character statement object.
   */
  public AttributeUUID aUUID(String name, String userDefinedAlias)
  {
    return this.aUUID(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute character statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   *          user defined alias.
   * @param userDefinedDisplayLabel
   * @return Attribute character statement object.
   */
  public AttributeUUID aUUID(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    this.rootQuery.checkValidAttributeRequest(name, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeUUIDInfo.CLASS);

    MdEntityDAOIF definingMdEntity = (MdEntityDAOIF) mdAttributeIF.definedByClass();

    return (AttributeUUID) this.internalAttributeFactory(name, mdAttributeIF, definingMdEntity, userDefinedAlias, userDefinedDisplayLabel);
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
   *          user defined alias.
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
   *          user defined alias.
   * @param userDefinedDisplayLabel
   * @return Attribute text statement object.
   */
  public AttributeText aText(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    this.rootQuery.checkValidAttributeRequest(name, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeTextInfo.CLASS);

    MdEntityDAOIF definingMdEntity = (MdEntityDAOIF) mdAttributeIF.definedByClass();

    return (AttributeText) this.internalAttributeFactory(name, mdAttributeIF, definingMdEntity, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute CLOB statement object.
   * 
   * @param name
   *          name of the attribute..
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
   *          user defined alias.
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
   *          user defined alias.
   * @param userDefinedDisplayLabel
   * @return Attribute CLOB statement object.
   */
  public AttributeClob aClob(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    this.rootQuery.checkValidAttributeRequest(name, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeClobInfo.CLASS);

    MdEntityDAOIF definingMdEntity = (MdEntityDAOIF) mdAttributeIF.definedByClass();

    return (AttributeClob) this.internalAttributeFactory(name, mdAttributeIF, definingMdEntity, userDefinedAlias, userDefinedDisplayLabel);
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
   *          user defined alias.
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
   *          user defined alias.
   * @param userDefinedDisplayLabel
   * @return Attribute date statement object.
   */
  public AttributeDate aDate(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    this.rootQuery.checkValidAttributeRequest(name, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeDateInfo.CLASS);

    MdEntityDAOIF definingMdEntity = (MdEntityDAOIF) mdAttributeIF.definedByClass();

    return (AttributeDate) this.internalAttributeFactory(name, mdAttributeIF, definingMdEntity, userDefinedAlias, userDefinedDisplayLabel);
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
   *          user defined alias.
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
   *          user defined alias.
   * @param userDefinedDisplayLabel
   * @return Attribute time statement object.
   */
  public AttributeTime aTime(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    this.rootQuery.checkValidAttributeRequest(name, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeTimeInfo.CLASS);

    MdEntityDAOIF definingMdEntity = (MdEntityDAOIF) mdAttributeIF.definedByClass();

    return (AttributeTime) this.internalAttributeFactory(name, mdAttributeIF, definingMdEntity, userDefinedAlias, userDefinedDisplayLabel);
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
    return aDateTime(name, null, null);
  }

  /**
   * Returns an attribute datetime statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   *          user defined alias.
   * @return Attribute datetime statement object.
   */
  public AttributeDateTime aDateTime(String name, String userDefinedAlias)
  {
    return aDateTime(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute datetime statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   *          user defined alias.
   * @param userDefinedDisplayLabel
   * @return Attribute datetime statement object.
   */
  public AttributeDateTime aDateTime(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    this.rootQuery.checkValidAttributeRequest(name, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeDateTimeInfo.CLASS);

    MdEntityDAOIF definingMdEntity = (MdEntityDAOIF) mdAttributeIF.definedByClass();

    return (AttributeDateTime) this.internalAttributeFactory(name, mdAttributeIF, definingMdEntity, userDefinedAlias, userDefinedDisplayLabel);
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
   *          user defined alias.
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
   *          user defined alias.
   * @param userDefinedDisplayLabel
   * @return Attribute integer statement object.
   */
  public AttributeInteger aInteger(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    this.rootQuery.checkValidAttributeRequest(name, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeIntegerInfo.CLASS);

    MdEntityDAOIF definingMdEntity = (MdEntityDAOIF) mdAttributeIF.definedByClass();

    return (AttributeInteger) this.internalAttributeFactory(name, mdAttributeIF, definingMdEntity, userDefinedAlias, userDefinedDisplayLabel);
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
   *          user defined alias.
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
   *          user defined alias.
   * @param userDefinedDisplayLabel
   * @return Attribute long statement object.
   */
  public AttributeLong aLong(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    this.rootQuery.checkValidAttributeRequest(name, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeLongInfo.CLASS);

    MdEntityDAOIF definingMdEntity = (MdEntityDAOIF) mdAttributeIF.definedByClass();

    return (AttributeLong) this.internalAttributeFactory(name, mdAttributeIF, definingMdEntity, userDefinedAlias, userDefinedDisplayLabel);
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
   *          user defined alias.
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
   *          user defined alias.
   * @param userDefinedDisplayLabel
   * @return Attribute double statement object.
   */
  public AttributeDouble aDouble(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    this.rootQuery.checkValidAttributeRequest(name, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeDoubleInfo.CLASS);

    MdEntityDAOIF definingMdEntity = (MdEntityDAOIF) mdAttributeIF.definedByClass();

    return (AttributeDouble) this.internalAttributeFactory(name, mdAttributeIF, definingMdEntity, userDefinedAlias, userDefinedDisplayLabel);
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
   *          user defined alias.
   * @return Attribute decimal statement object.
   */
  public AttributeDecimal aDecimal(String name, String userDefinedAlias)
  {
    return this.aDecimal(name, null, null);
  }

  /**
   * Returns an attribute decimal statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   *          user defined alias.
   * @param userDefinedDisplayLabel
   * @return Attribute decimal statement object.
   */
  public AttributeDecimal aDecimal(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    this.rootQuery.checkValidAttributeRequest(name, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeDecimalInfo.CLASS);

    MdEntityDAOIF definingMdEntity = (MdEntityDAOIF) mdAttributeIF.definedByClass();

    return (AttributeDecimal) this.internalAttributeFactory(name, mdAttributeIF, definingMdEntity, userDefinedAlias, userDefinedDisplayLabel);
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
   *          user defined alias.
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
   *          user defined alias.
   * @param userDefinedDisplayLabel
   * @return Attribute float statement object.
   */
  public AttributeFloat aFloat(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    this.rootQuery.checkValidAttributeRequest(name, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeFloatInfo.CLASS);

    MdEntityDAOIF definingMdEntity = (MdEntityDAOIF) mdAttributeIF.definedByClass();

    return (AttributeFloat) this.internalAttributeFactory(name, mdAttributeIF, definingMdEntity, userDefinedAlias, userDefinedDisplayLabel);
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
    return this.aBoolean(name, null, null);
  }

  /**
   * Returns an attribute boolean statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   *          user defined alias.
   * @return Attribute boolean statement object.
   */
  public AttributeBoolean aBoolean(String name, String userDefinedAlias)
  {
    return this.aBoolean(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute boolean statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   *          user defined alias.
   * @param userDefinedDisplayLabel
   * @return Attribute boolean statement object.
   */
  public AttributeBoolean aBoolean(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    this.rootQuery.checkValidAttributeRequest(name, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeBooleanInfo.CLASS);

    MdEntityDAOIF definingMdEntity = (MdEntityDAOIF) mdAttributeIF.definedByClass();

    return (AttributeBoolean) this.internalAttributeFactory(name, mdAttributeIF, definingMdEntity, userDefinedAlias, userDefinedDisplayLabel);
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
   *          user defined alias.
   * @param userDefinedDisplayLabel
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
   *          user defined alias.
   * @param userDefinedDisplayLabel
   * @return Attribute blob statement object.
   */
  public AttributeBlob aBlob(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    this.rootQuery.checkValidAttributeRequest(name, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeBlobInfo.CLASS);

    MdEntityDAOIF definingMdEntity = (MdEntityDAOIF) mdAttributeIF.definedByClass();

    return (AttributeBlob) this.internalAttributeFactory(name, mdAttributeIF, definingMdEntity, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute reference statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute referrence statement object.
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
   *          user defined alias.
   * @return Attribute referrence statement object.
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
   *          user defined alias.
   * @param userDefinedDisplayLabel
   * @return Attribute referrence statement object.
   */
  public AttributeReference aReference(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    this.rootQuery.checkValidAttributeRequest(name, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeReferenceInfo.CLASS);

    MdEntityDAOIF definingMdEntity = (MdEntityDAOIF) mdAttributeIF.definedByClass();

    return (AttributeReference) this.internalAttributeFactory(name, mdAttributeIF, definingMdEntity, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute reference statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute referrence statement object.
   */
  public AttributeReference aFile(String name)
  {
    return this.aFile(name, null, null);
  }

  /**
   * Returns an attribute reference statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   *          user defined alias.
   * @return Attribute referrence statement object.
   */
  public AttributeReference aFile(String name, String userDefinedAlias)
  {
    return this.aFile(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute reference statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   *          user defined alias.
   * @param userDefinedDisplayLabel
   * @return Attribute referrence statement object.
   */
  public AttributeReference aFile(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    this.rootQuery.checkValidAttributeRequest(name, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeFileInfo.CLASS);

    MdEntityDAOIF definingMdEntity = (MdEntityDAOIF) mdAttributeIF.definedByClass();

    return (AttributeReference) this.internalAttributeFactory(name, mdAttributeIF, definingMdEntity, userDefinedAlias, userDefinedDisplayLabel);
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
   *          user defined alias.
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
   *          user defined alias.
   * @param userDefinedDisplayLabel
   * @return Attribute struct statement object.
   */
  public AttributeStruct aStruct(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    this.rootQuery.checkValidAttributeRequest(name, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeStructInfo.CLASS);

    MdEntityDAOIF definingMdEntity = (MdEntityDAOIF) mdAttributeIF.definedByClass();

    return (AttributeStruct) this.internalAttributeFactory(name, mdAttributeIF, definingMdEntity, userDefinedAlias, userDefinedDisplayLabel);
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
   *          user defined alias.
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
   *          user defined alias.
   * @param userDefinedDisplayLabel
   * @return Attribute local statement object.
   */
  public AttributeLocal aLocalCharacter(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    this.rootQuery.checkValidAttributeRequest(name, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeLocalCharacterInfo.CLASS);

    MdEntityDAOIF definingMdEntity = (MdEntityDAOIF) mdAttributeIF.definedByClass();

    return (AttributeLocal) this.internalAttributeFactory(name, mdAttributeIF, definingMdEntity, userDefinedAlias, userDefinedDisplayLabel);
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
   *          user defined alias.
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
   *          user defined alias.
   * @param userDefinedDisplayLabel
   * @return Attribute local statement object.
   */
  public AttributeLocal aLocalText(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    this.rootQuery.checkValidAttributeRequest(name, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeLocalTextInfo.CLASS);

    MdEntityDAOIF definingMdEntity = (MdEntityDAOIF) mdAttributeIF.definedByClass();

    return (AttributeLocal) this.internalAttributeFactory(name, mdAttributeIF, definingMdEntity, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute enumeration statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute struct enumeration object.
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
   *          user defined alias.
   * @return Attribute struct enumeration object.
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
   *          user defined alias.
   * @param userDefinedDisplayLabel
   * @return Attribute struct enumeration object.
   */
  public AttributeEnumeration aEnumeration(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    this.rootQuery.checkValidAttributeRequest(name, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeStructInfo.CLASS);

    MdEntityDAOIF definingMdEntity = (MdEntityDAOIF) mdAttributeIF.definedByClass();

    return (AttributeEnumeration) this.internalAttributeFactory(name, mdAttributeIF, definingMdEntity, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Factory to construct an attribute object with the given name.
   * 
   * @return attribute object with the given name.
   */
  private Attribute internalAttributeFactory(String name, MdAttributeConcreteDAOIF mdAttributeIF, MdEntityDAOIF definingMdEntity, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String refAttributeNameSpace = this.attributeNamespace + "." + this.attributeName;

    // Join the mapping table with the attribute on the entity.
    Join enumMappingTableJoin = new InnerJoinEq(this.columnName, this.definingTableName, this.definingTableAlias, MdEnumerationDAOIF.SET_ID_COLUMN, this.mdEnumerationTableName, rootQuery.getTableAlias(this.attributeNamespace, this.mdEnumerationTableName));

    // Join the mapping table with the enumeration item master table
    Join tableJoin = new InnerJoinEq(EntityInfo.OID, this.referenceTableName, this.referenceTableAlias, MdEnumerationInfo.ITEM_ID, this.mdEnumerationTableName, rootQuery.getTableAlias(this.attributeNamespace, this.mdEnumerationTableName));

    this.tableJoinSet.add(enumMappingTableJoin);
    this.tableJoinSet.add(tableJoin);

    String parameterTableName;
    String parameterTableAlias;

    String tableName = definingMdEntity.getTableName();
    if (!tableName.equals(this.referenceTableName))
    {
      String tableAlias = rootQuery.getTableAlias(refAttributeNameSpace, tableName);
      this.tableJoinSet.add(new InnerJoinEq(EntityInfo.OID, this.referenceTableName, this.referenceTableAlias, EntityInfo.OID, tableName, tableAlias));

      parameterTableName = tableName;
      parameterTableAlias = tableAlias;
    }
    else
    {
      parameterTableName = this.referenceTableName;
      parameterTableAlias = this.referenceTableAlias;
    }

    Attribute attribute = null;

    if (mdAttributeIF instanceof MdAttributeCharacterDAOIF)
    {
      attribute = new AttributeCharacter((MdAttributeCharacterDAOIF) mdAttributeIF, refAttributeNameSpace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeTextDAOIF)
    {
      attribute = new AttributeText((MdAttributeTextDAOIF) mdAttributeIF, refAttributeNameSpace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeClobDAOIF)
    {
      attribute = new AttributeClob((MdAttributeClobDAOIF) mdAttributeIF, refAttributeNameSpace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeDateDAOIF)
    {
      attribute = new AttributeDate((MdAttributeDateDAOIF) mdAttributeIF, refAttributeNameSpace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeTimeDAOIF)
    {
      attribute = new AttributeTime((MdAttributeTimeDAOIF) mdAttributeIF, refAttributeNameSpace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeDateTimeDAOIF)
    {
      attribute = new AttributeDateTime((MdAttributeDateTimeDAOIF) mdAttributeIF, refAttributeNameSpace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeIntegerDAOIF)
    {
      attribute = new AttributeInteger((MdAttributeIntegerDAOIF) mdAttributeIF, refAttributeNameSpace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeLongDAOIF)
    {
      attribute = new AttributeLong((MdAttributeLongDAOIF) mdAttributeIF, refAttributeNameSpace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeDoubleDAOIF)
    {
      attribute = new AttributeDouble((MdAttributeDoubleDAOIF) mdAttributeIF, refAttributeNameSpace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeDecimalDAOIF)
    {
      attribute = new AttributeDecimal((MdAttributeDecimalDAOIF) mdAttributeIF, refAttributeNameSpace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeFloatDAOIF)
    {
      attribute = new AttributeFloat((MdAttributeFloatDAOIF) mdAttributeIF, refAttributeNameSpace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeBooleanDAOIF)
    {
      attribute = new AttributeBoolean((MdAttributeBooleanDAOIF) mdAttributeIF, refAttributeNameSpace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeBlobDAOIF)
    {
      attribute = new AttributeBlob((MdAttributeBlobDAOIF) mdAttributeIF, refAttributeNameSpace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeLocalDAOIF)
    {
      MdAttributeLocalDAOIF mdAttributeLocalIF = (MdAttributeLocalDAOIF) mdAttributeIF;
      MdLocalStructDAOIF localStructMdBusinessIF = mdAttributeLocalIF.getMdStructDAOIF();
      String structTableName = localStructMdBusinessIF.getTableName();
      String structTableAlias = this.rootQuery.getTableAlias(refAttributeNameSpace, structTableName);

      attribute = this.localFactory(mdAttributeLocalIF, refAttributeNameSpace, parameterTableName, parameterTableAlias, localStructMdBusinessIF, structTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeStructDAOIF)
    {
      MdAttributeStructDAOIF mdAttributeStructIF = (MdAttributeStructDAOIF) mdAttributeIF;
      MdStructDAOIF structMdBusinessIF = mdAttributeStructIF.getMdStructDAOIF();
      String structTableName = structMdBusinessIF.getTableName();
      String structTableAlias = this.rootQuery.getTableAlias(refAttributeNameSpace, structTableName);

      attribute = this.structFactory(mdAttributeStructIF, refAttributeNameSpace, parameterTableName, parameterTableAlias, structMdBusinessIF, structTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeEnumerationDAOIF)
    {
      MdAttributeEnumerationDAOIF mdAttributeEnumerationIF = (MdAttributeEnumerationDAOIF) mdAttributeIF;
      MdEnumerationDAOIF mdEnumerationIF = mdAttributeEnumerationIF.getMdEnumerationDAO();
      String mdEnumerationTableName = mdEnumerationIF.getTableName();

      MdBusinessDAOIF masterListMdBusinessIF = mdEnumerationIF.getMasterListMdBusinessDAO();
      String masterListTalbeAlias = this.rootQuery.getTableAlias(this.attributeNamespace, masterListMdBusinessIF.getTableName());

      attribute = this.enumerationFactory(mdAttributeEnumerationIF, refAttributeNameSpace, parameterTableName, parameterTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeRefDAOIF)
    {
      MdAttributeRefDAOIF mdAttributerRefIF = (MdAttributeRefDAOIF) mdAttributeIF;

      MdBusinessDAOIF refAttrMdBusinessIF = mdAttributerRefIF.getReferenceMdBusinessDAO();

      String referenceAttrTableName = refAttrMdBusinessIF.getTableName();
      String referenceAttrTableAlias = this.rootQuery.getTableAlias(refAttributeNameSpace, referenceAttrTableName);

      attribute = referenceFactory(mdAttributerRefIF, refAttributeNameSpace, parameterTableName, parameterTableAlias, refAttrMdBusinessIF, referenceAttrTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }

    if (attribute == null)
    {
      for (AttributeEnumeration.PluginIF plugin : pluginMap.values())
      {
        attribute = plugin.internalAttributeFactory(name, mdAttributeIF, refAttributeNameSpace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);

        if (attribute != null)
        {
          break;
        }
      }
    }

    if (attribute == null)
    {
      String error = "Attribute [" + name + "] is not primitive.";
      throw new QueryException(error);
    }

    return attribute;
  }

  /**
   * Returns an AttributeReference with the given values. This can represent an
   * AttributeFile as well. Generated subclasses with override this method and
   * return subclasses of AttributeReference.
   *
   * @param name
   * @param mdAttributeIF
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
  protected AttributeReference referenceFactory(MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
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
   * @return <code>AttributeStruct</code> with the given values. Generated
   *         subclasses with override this method and return subclasses of
   *         <code>AttributeStruct</code>.
   */
  protected AttributeStruct structFactory(MdAttributeStructDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, MdStructDAOIF mdStructIF, String structTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new AttributeStruct(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdStructIF, structTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an <code>AttributeLocale</code> with the given values. Generated
   * subclasses with override this method and return subclasses of
   * <code>AttributeLocale</code>.
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
   * @return <code>AttributeLocale</code> with the given values. Generated
   *         subclasses with override this method and return subclasses of
   *         <code>AttributeLocale</code>.
   */
  protected AttributeLocal localFactory(MdAttributeLocalDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, MdLocalStructDAOIF mdStructIF, String structTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new AttributeLocal(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdStructIF, structTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
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
   * @return AttributeEnumeration with the given values. Generated subclasses
   *         with override this method and return subclasses of
   *         AttributeEnumeration.
   */
  protected AttributeEnumeration enumerationFactory(MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdEnumerationTableName, MdBusinessDAOIF masterListMdBusinessIF, String masterListTalbeAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new AttributeEnumeration(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute query object for the attribute with the given name and
   * type.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param attributeType
   *          type of the attribute.
   * @param userDefinedAlias
   * @return attribute query object for the attribute with the given name and
   *         type.
   */
  public Attribute attributeFactory(String attributeName, String attributeType, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    if (attributeType.equals(MdAttributeBooleanInfo.CLASS))
    {
      return this.aBoolean(attributeName, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (attributeType.equals(MdAttributeCharacterInfo.CLASS))
    {
      return this.aCharacter(attributeName, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (attributeType.equals(MdAttributeUUIDInfo.CLASS))
    {
      return this.aUUID(attributeName, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (attributeType.equals(MdAttributeTextInfo.CLASS))
    {
      return this.aText(attributeName, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (attributeType.equals(MdAttributeClobInfo.CLASS))
    {
      return this.aClob(attributeName, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (attributeType.equals(MdAttributeDateInfo.CLASS))
    {
      return this.aDate(attributeName, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (attributeType.equals(MdAttributeDateTimeInfo.CLASS))
    {
      return this.aDateTime(attributeName, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (attributeType.equals(MdAttributeTimeInfo.CLASS))
    {
      return this.aTime(attributeName, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (attributeType.equals(MdAttributeDecimalInfo.CLASS))
    {
      return this.aDecimal(attributeName, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (attributeType.equals(MdAttributeDoubleInfo.CLASS))
    {
      return this.aDouble(attributeName, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (attributeType.equals(MdAttributeFloatInfo.CLASS))
    {
      return this.aFloat(attributeName, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (attributeType.equals(MdAttributeIntegerInfo.CLASS))
    {
      return this.aInteger(attributeName, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (attributeType.equals(MdAttributeLongInfo.CLASS))
    {
      return this.aLong(attributeName, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (attributeType.equals(MdAttributeEnumerationInfo.CLASS))
    {
      return this.aEnumeration(attributeName, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (attributeType.equals(MdAttributeReferenceInfo.CLASS))
    {
      return this.aReference(attributeName, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (attributeType.equals(MdAttributeFileInfo.CLASS))
    {
      return this.aFile(attributeName, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (attributeType.equals(MdAttributeBlobInfo.CLASS))
    {
      return this.aBlob(attributeName, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (attributeType.equals(MdAttributeStructInfo.CLASS))
    {
      return this.aStruct(attributeName, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (attributeType.equals(MdAttributeLocalCharacterInfo.CLASS))
    {
      return this.aLocalCharacter(attributeName, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (attributeType.equals(MdAttributeLocalTextInfo.CLASS))
    {
      return this.aLocalText(attributeName, userDefinedAlias, userDefinedDisplayLabel);
    }
    else
    {
      String error = "Attribute type [" + attributeType + "] is invalid.";
      throw new QueryException(error);
    }
  }

  // Any
  /**
   * Checks if the enumeration attribute contains a mapping with one of the
   * enumeration items with the given oid.
   * 
   * @param enumIds
   *          OID of an enumeration.
   * @return Condition representing the query constraint.
   */
  public Condition containsAny(String... enumIds)
  {
    StatementPrimitive[] statementPrimitiveArray = new StatementPrimitive[enumIds.length];
    for (int i = 0; i < enumIds.length; i++)
    {
      String formattedValue = Database.formatJavaToSQL(enumIds[i], MdAttributeEnumerationInfo.CLASS, false);
      statementPrimitiveArray[i] = new StatementPrimitive(formattedValue);
    }
    return new BasicConditionIn(this.subFactory(this.attributeNamespace), statementPrimitiveArray);
  }

  // Not Any
  /**
   * Checks if the enumeration attribute does not contain a mapping with one of
   * the enumeration items with the given oid.
   * 
   * @param enumIds
   *          OID of an enumeration.
   * @return Condition representing the query constraint.
   */
  public Condition notContainsAny(String... enumIds)
  {
    return new EnumerationNotContainsAny(this.subFactory(this.attributeNamespace), new EnumerationSubSelectNotContainsAny(this.columnName, this.definingTableAlias, this.mdEnumerationTableName, enumIds));
  }

  // All
  /**
   * Checks if the enumeration attribute contains a mapping with all of the
   * enumeration items with the given oid.
   * 
   * @param enumIds
   *          OID of an enumeration.
   * @return Condition representing the query constraint.
   */
  public Condition containsAll(String... enumIds)
  {
    StatementPrimitive[] statementPrimitiveArray = new StatementPrimitive[enumIds.length];
    Condition previousCondition = null;
    for (int i = 0; i < enumIds.length; i++)
    {
      String formattedValue = Database.formatJavaToSQL(enumIds[i], MdAttributeEnumerationInfo.CLASS, false);
      statementPrimitiveArray[i] = new StatementPrimitive(formattedValue);

      BasicCondition basicCondition = new BasicConditionEq(this.subFactory(this.attributeNamespace + enumIds[i]), statementPrimitiveArray[i]);

      if (previousCondition != null)
      {
        previousCondition = new AND(previousCondition, basicCondition);
      }
      else
      {
        previousCondition = basicCondition;
      }
    }
    return previousCondition;
  }

  // NOT All
  /**
   * Checks if the enumeration attribute does not contain a mapping with all of
   * the enumeration items with the given oid.
   * 
   * @param enumIds
   *          OID of an enumeration.
   * @return Condition representing the query constraint.
   */
  public Condition notContainsAll(String... enumIds)
  {
    StatementPrimitive[] statementPrimitiveArray = new StatementPrimitive[enumIds.length];
    Condition previousCondition = null;
    for (int i = 0; i < enumIds.length; i++)
    {
      String formattedValue = Database.formatJavaToSQL(enumIds[i], MdAttributeEnumerationInfo.CLASS, false);
      statementPrimitiveArray[i] = new StatementPrimitive(formattedValue);

      BasicCondition basicCondition = new BasicConditionNotEq(this.subFactory(this.attributeNamespace), statementPrimitiveArray[i]);

      if (previousCondition != null)
      {
        previousCondition = new AND(previousCondition, basicCondition);
      }
      else
      {
        previousCondition = basicCondition;
      }
    }
    return previousCondition;
  }

  // Exactly
  /**
   * Checks if the enumeration attribute contains a mapping with exactly the
   * given set of the enumeration items with the given oid.
   * 
   * @param enumIds
   *          OID of an enumeration.
   * @return Condition representing the query constraint.
   */
  public Condition containsExactly(String... enumIds)
  {
    StatementPrimitive[] statementPrimitiveArray = new StatementPrimitive[enumIds.length];
    Condition previousCondition = null;
    for (int i = 0; i < enumIds.length; i++)
    {
      String formattedValue = Database.formatJavaToSQL(enumIds[i], MdAttributeEnumerationInfo.CLASS, false);
      statementPrimitiveArray[i] = new StatementPrimitive(formattedValue);

      BasicCondition basicCondition = new BasicConditionEq(this.subFactory(this.attributeNamespace + enumIds[i]), statementPrimitiveArray[i]);

      if (previousCondition != null)
      {
        previousCondition = new AND(previousCondition, basicCondition);
      }
      else
      {
        previousCondition = basicCondition;
      }
    }
    Expression subSelectStatement = new EnumerationCountSubSelect(this.columnName, this.definingTableAlias, this.mdEnumerationTableName);

    StatementSubSelectCondition basicConditionSubSelect = new StatementSubSelectCondition(new StatementPrimitive(Integer.toString(enumIds.length)), subSelectStatement, false);

    return new AND(previousCondition, basicConditionSubSelect);
  }

  /**
   * Returns the a nested aggregate function in this composite function tree, if
   * there is one, or return null;
   * 
   * @return nested aggregate function in this composite function tree, if there
   *         is one, or return null;
   */
  public SelectableAggregate getAggregateFunction()
  {
    return null;
  }

  /**
   * Returns true if this selectable is an aggregate function or contains an
   * aggregate function. False otherwise.
   * 
   * @return true if this selectable is an aggregate function or contains an
   *         aggregate function. False otherwise.
   */
  public boolean isAggregateFunction()
  {
    if (this.getAggregateFunction() != null)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Returns a condition based on the String version of the operator and the Ref
   * version of the value.
   * 
   * @param operator
   * @param value
   * @return condition based on the String version of the operator and the
   *         String version of the value.
   */
  public Condition getCondition(String operator, String value)
  {
    return QueryUtil.getCondition(this, operator, value);
  }

  /**
   * Constructs the appropriate AttributeEnumeration_Sub object, depending if
   * this attribute is a part of the stub or not.
   * 
   * @param attributeNameSpace
   * @param userDefinedAlias
   * @return appropriate AttributeEnumeration_Sub object, depending if this
   *         attribute is a part of the stub or not.
   */
  private AttributeEnumeration_Sub subFactory(String attributeNameSpace)
  {
    return new AttributeEnumeration_Sub((MdAttributeEnumerationDAOIF) this.mdAttributeIF, attributeNameSpace, this.definingTableName, this.definingTableAlias, this.mdEnumerationTableName, this.rootQuery.getTableAlias(attributeNameSpace, this.mdEnumerationTableName), this.rootQuery, this.tableJoinSet);
  }

  static class EnumerationNotContainsAny extends AttributeCondition
  {
    private EnumerationSubSelectNotContainsAny statement;

    public EnumerationNotContainsAny(AttributeEnumeration_Sub attributeLeft, EnumerationSubSelectNotContainsAny statement)
    {
      super(attributeLeft);
      this.statement = statement;
    }

    public String getSQL()
    {
      return ( (AttributeEnumeration_Sub) this.selectableLeft ).getSQL2() + " NOT IN \n" + statement.getSQL();
    }

    /**
     * Returns a Set of TableJoin objects that represent joins statements that
     * are required for this expression.
     * 
     * @return Set of TableJoin objects that represent joins statements that are
     *         required for this expression, or null of there are none.
     */
    public Set<Join> getJoinStatements()
    {
      return null;
    }

    /**
     * Returns a Map representing tables that should be included in the from
     * clause, where the key is the table alias and the value is the name of the
     * table.
     * 
     * @return Map representing tables that should be included in the from
     *         clause, where the key is the table alias and the value is the
     *         name of the table.
     */
    public Map<String, String> getFromTableMap()
    {
      return null;
    }

    /**
     * Visitor to traverse the query object structure.
     * 
     * @param visitor
     */
    public void accept(Visitor visitor)
    {
      super.accept(visitor);
      this.statement.accept(visitor);
    }
  }

  /**
   *
   * @author nathan
   *
   */
  static class EnumerationSubSelectNotContainsAny extends Condition
  {
    private String columnName;

    private String definingTableAlias;

    private String mdEnumerationTableName;

    private String enumIds[];

    public EnumerationSubSelectNotContainsAny(String columnName, String definingTableAlias, String mdEnumerationTableName, String... enumIds)
    {
      this.columnName = columnName;
      this.definingTableAlias = definingTableAlias;
      this.mdEnumerationTableName = mdEnumerationTableName;
      this.enumIds = enumIds;
    }

    public String getSQL()
    {
      StringBuilder sqlStmt = new StringBuilder();
      sqlStmt.append("(SELECT " + MdEnumerationInfo.SET_ID + " \n");
      sqlStmt.append(" FROM " + this.mdEnumerationTableName + "\n");
      sqlStmt.append(" WHERE " + MdEnumerationDAOIF.SET_ID_COLUMN + " = " + this.definingTableAlias + "." + this.columnName + "\n");
      sqlStmt.append("   AND (");

      boolean firstIteration = true;
      for (String enumId : this.enumIds)
      {
        if (!firstIteration)
        {
          sqlStmt.append("\n        OR ");
        }
        firstIteration = false;

        sqlStmt.append(MdEnumerationInfo.ITEM_ID + " = '" + enumId + "'");
      }

      sqlStmt.append("))");
      return sqlStmt.toString();
    }

    /**
     * Returns a Set of TableJoin objects that represent joins statements that
     * are required for this expression.
     * 
     * @return Set of TableJoin objects that represent joins statements that are
     *         required for this expression, or null of there are none.
     */
    public Set<Join> getJoinStatements()
    {
      return null;
    }

    /**
     * Returns a Map representing tables that should be included in the from
     * clause, where the key is the table alias and the value is the name of the
     * table.
     * 
     * @return Map representing tables that should be included in the from
     *         clause, where the key is the table alias and the value is the
     *         name of the table.
     */
    public Map<String, String> getFromTableMap()
    {
      return null;
    }

    /**
     * Visitor to traverse the query object structure.
     * 
     * @param visitor
     */
    public void accept(Visitor visitor)
    {
      visitor.visit(this);
    }
  }

  /**
   *
   * @author nathan
   *
   */
  static class EnumerationCountSubSelect extends Condition
  {
    private String columnName;

    private String definingTableAlias;

    private String mdEnumerationTableName;

    public EnumerationCountSubSelect(String columnName, String definingTableAlias, String mdEnumerationTableName)
    {
      this.columnName = columnName;
      this.definingTableAlias = definingTableAlias;
      this.mdEnumerationTableName = mdEnumerationTableName;
    }

    public String getSQL()
    {
      return "(SELECT COUNT(*) FROM " + this.mdEnumerationTableName + " WHERE " + MdEnumerationDAOIF.SET_ID_COLUMN + " = " + this.definingTableAlias + "." + this.columnName + ")";
    }

    /**
     * Returns a Set of TableJoin objects that represent joins statements that
     * are required for this expression.
     * 
     * @return Set of TableJoin objects that represent joins statements that are
     *         required for this expression, or null of there are none.
     */
    public Set<Join> getJoinStatements()
    {
      return null;
    }

    /**
     * Returns a Map representing tables that should be included in the from
     * clause, where the key is the table alias and the value is the name of the
     * table.
     * 
     * @return Map representing tables that should be included in the from
     *         clause, where the key is the table alias and the value is the
     *         name of the table.
     */
    public Map<String, String> getFromTableMap()
    {
      return null;
    }

    /**
     * Visitor to traverse the query object structure.
     * 
     * @param visitor
     */
    public void accept(Visitor visitor)
    {
      visitor.visit(this);
    }
  }

  /**
   *
   * @author nathan
   *
   */
  static class AttributeEnumeration_Sub extends Attribute
  {
    private String mdEnumerationTableName;

    private String mdEnumerationTableAlias;

    private Join   enumMappingTableJoin;

    protected AttributeEnumeration_Sub(MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNameSpace, String definingTableName, String definingTableAlias, String mdEnumerationTableName, String mdEnumerationTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet)
    {
      super(mdAttributeIF, attributeNameSpace, definingTableName, definingTableAlias, rootQuery, tableJoinSet, null, null);

      this.mdEnumerationTableName = mdEnumerationTableName;
      this.mdEnumerationTableAlias = mdEnumerationTableAlias;

      this.enumMappingTableJoin = new InnerJoinEq(this.columnName, this.definingTableName, this.definingTableAlias, MdEnumerationDAOIF.SET_ID_COLUMN, this.mdEnumerationTableName, this.mdEnumerationTableAlias);

    }

    /**
     * Compares the oid of a component for equality. This class is used only by
     * its enclosing class and is never called by a client. Hence This method is
     * implemented only to satisfy the contract of the super class;
     * 
     * @param oid
     *          oid of the object to compare.
     * @return null;
     */
    public BasicCondition EQ(String oid)
    {
      return null;
    }

    /**
     * Compares the oid of a component for equality. This class is used only by
     * its enclosing class and is never called by a client. Hence This method is
     * implemented only to satisfy the contract of the super class;
     * 
     * @param oid
     *          oid of the object to compare.
     * @return null;
     */
    public BasicCondition NE(String oid)
    {
      return null;
    }

    /**
     * Returns a condition based on the String version of the operator and the
     * Ref version of the value.
     * 
     * @param operator
     * @param value
     * @return condition based on the String version of the operator and the
     *         String version of the value.
     */
    public Condition getCondition(String operator, String value)
    {
      return QueryUtil.getCondition(this, operator, value);
    }

    /**
     * Returns the a nested aggregate function in this composite function tree,
     * if there is one, or return null;
     * 
     * @return nested aggregate function in this composite function tree, if
     *         there is one, or return null;
     */
    public SelectableAggregate getAggregateFunction()
    {
      return null;
    }

    /**
     * Returns true if this selectable is an aggregate function or contains an
     * aggregate function. False otherwise.
     * 
     * @return true if this selectable is an aggregate function or contains an
     *         aggregate function. False otherwise.
     */
    public boolean isAggregateFunction()
    {
      if (this.getAggregateFunction() != null)
      {
        return true;
      }
      else
      {
        return false;
      }
    }

    /**
     * Returns the SQL representation of this condition.
     *
     * @return SQL representation of this condition.
     */
    public String getSQL()
    {
      return this.mdEnumerationTableAlias + "." + MdEnumerationInfo.ITEM_ID;
    }

    /**
     * Returns the SQL representation of this condition.
     *
     * @return SQL representation of this condition.
     */
    public String getSQL2()
    {
      return this.definingTableAlias + "." + this.columnName;
    }

    /**
     * Returns a Set of TableJoin objects that represent joins statements that
     * are required for this expression.
     * 
     * @return Set of TableJoin objects that represent joins statements that are
     *         required for this expression, or null of there are none.
     */
    public Set<Join> getJoinStatements()
    {
      Set<Join> tableJoinSet = new HashSet<Join>();

      Set<Join> superTableJoinSet = super.getJoinStatements();
      if (superTableJoinSet != null)
      {
        tableJoinSet.addAll(superTableJoinSet);
      }
      tableJoinSet.add(this.enumMappingTableJoin);
      return tableJoinSet;
    }
  }

  public static interface PluginIF
  {
    public String getModuleIdentifier();

    public Attribute internalAttributeFactory(String attributeName, MdAttributeConcreteDAOIF mdAttributeIF, String refAttributeNameSpace, String parameterTableName, String parameterTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel);
  }
}
