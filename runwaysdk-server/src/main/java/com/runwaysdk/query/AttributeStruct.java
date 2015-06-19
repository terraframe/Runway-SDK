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
package com.runwaysdk.query;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeClobInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
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
import com.runwaysdk.dataaccess.MdAttributeLongDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTextDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.database.Database;


public class AttributeStruct extends Attribute implements SelectableStruct
{
  protected String          structTableName;
  protected String          structTableAlias;
  protected MdStructDAOIF   mdStructIF;

  /**
   * Key: attribute name,  Value: attribute metadata.
   */
  private Map<String, ? extends MdAttributeConcreteDAOIF>   structMdAttributeIFMap;

  protected AttributeStruct(MdAttributeStructDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,
      MdStructDAOIF mdStructIF, String structTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias,
        rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);

    this.mdStructIF              = mdStructIF;
    this.structMdAttributeIFMap  = this.mdStructIF.getAllDefinedMdAttributeMap();
    this.structTableName         = this.mdStructIF.getTableName();
    this.structTableAlias        = structTableAlias;

    this.tableJoinSet.add(new LeftJoinEq(this.columnName, this.definingTableName, this.definingTableAlias, EntityDAOIF.ID_COLUMN, this.structTableName, this.structTableAlias));
  }

  @Override
  public MdAttributeStructDAOIF getMdAttributeIF()
  {
    return (MdAttributeStructDAOIF) super.getMdAttributeIF();
  }

  public MdStructDAOIF getMdStructDAOIF()
  {
    return this.mdStructIF;
  }

  /**
   * Returns the a nested aggregate function in this composite function tree, if there is one, or return null;
   * @return nested aggregate function in this composite function tree, if there is one, or return null;
   */
  public SelectableAggregate getAggregateFunction()
  {
    return null;
  }

  /**
   * Returns true if this selectable is an aggregate function or contains an aggregate function.  False otherwise.
   * @return true if this selectable is an aggregate function or contains an aggregate function.  False otherwise.
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
   * {@inheritDoc}
   */
  @Override
  public List<Attribute> getStructAttributes()
  {
    List<Attribute> attributeList = new LinkedList<Attribute>();
    
    List<? extends MdAttributeConcreteDAOIF> structMdAttributeList = this.getMdStructDAOIF().definesAttributes();

    for (MdAttributeConcreteDAOIF structMdAttributeIF : structMdAttributeList)
    {
      Attribute structAttribute = this.attributeFactory(structMdAttributeIF.definesAttribute(), structMdAttributeIF.getType(), null, null);
      attributeList.add(structAttribute);
    }
    
    return attributeList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ColumnInfo> getColumnInfoList()
  {
    List<ColumnInfo> columnInfoList = super.getColumnInfoList();

    List<Attribute> structAttributes = this.getStructAttributes();
    for (Attribute attribute : structAttributes)
    {
      columnInfoList.addAll(attribute.getColumnInfoList());
    }
    
    return columnInfoList;
  }
  
  /**
   * Compares the id of a component for equality.
   * @param id id of the object to compare.
   * @return Basic Condition object
   */
  public BasicCondition EQ(String id)
  {
    String formattedValue = Database.formatJavaToSQL(id, MdAttributeCharacterInfo.CLASS, false);
    StatementPrimitive statementPrimitive = new StatementPrimitive(formattedValue);
    return new BasicConditionEq(this, statementPrimitive, false);
  }

  /**
   * Compares the id of a component for equality.
   * @param id id of the object to compare.
   * @return Basic Condition object
   */
  public BasicCondition NE(String id)
  {
    String formattedValue = Database.formatJavaToSQL(id, MdAttributeCharacterInfo.CLASS, false);
    StatementPrimitive statementPrimitive = new StatementPrimitive(formattedValue);
    return new BasicConditionNotEq(this, statementPrimitive, false);
  }

  /**
   * Returns an attribute enumeration statement object.
   * @param name name of the attribute.
   * @return Attribute enumeration statement object.
   */
  public AttributeEnumeration aEnumeration(String name)
  {
    return this.aEnumeration(name, null, null);
  }

  /**
   * Returns an attribute enumeration statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @return Attribute enumeration statement object.
   */
  public AttributeEnumeration aEnumeration(String name, String userDefinedAlias)
  {
    return this.aEnumeration(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute enumeration statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute enumeration statement object.
   */
  public AttributeEnumeration aEnumeration(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.rootQuery.checkValidAttributeRequest(name, this.mdStructIF, mdAttributeIF, MdAttributeEnumerationInfo.CLASS);

    MdAttributeEnumerationDAOIF mdAttributeEnumerationIF = (MdAttributeEnumerationDAOIF)mdAttributeIF;
    MdEnumerationDAOIF mdEnumerationIF = mdAttributeEnumerationIF.getMdEnumerationDAO();
    String mdEnumerationTableName =  mdEnumerationIF.getTableName();

    MdBusinessDAOIF masterListMdBusinessIF = mdEnumerationIF.getMasterListMdBusinessDAO();
    String masterListTalbeAlias = this.rootQuery.getTableAlias(this.attributeNamespace, masterListMdBusinessIF.getTableName());

    return this.enumerationFactory(mdAttributeEnumerationIF, this.attributeNamespace+"."+this._getAttributeName(), this.structTableName, this.structTableAlias,
        mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute character statement object.
   * @param name name of the attribute.
   * @return Attribute character statement object.
   */
  public AttributeCharacter aCharacter(String name)
  {
    return this.aCharacter(name, null, null);
  }

  /**
   * Returns an attribute character statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @return Attribute character statement object.
   */
  public AttributeCharacter aCharacter(String name, String userDefinedAlias)
  {
    return this.aCharacter(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute character statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute character statement object.
   */
  public AttributeCharacter aCharacter(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.rootQuery.checkValidAttributeRequest(name, this.mdStructIF, mdAttributeIF, MdAttributeCharacterInfo.CLASS);

    return new AttributeCharacter((MdAttributeCharacterDAOIF) mdAttributeIF, this.attributeNamespace+"."+this._getAttributeName(), this.structTableName,
        this.structTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, this.getMdAttributeIF());
  }

  /**
   * Returns an attribute text statement object.
   * @param name name of the attribute.
   * @return Attribute text statement object.
   */
  public AttributeText aText(String name)
  {
    return this.aText(name, null, null);
  }

  /**
   * Returns an attribute text statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @return Attribute text statement object.
   */
  public AttributeText aText(String name, String userDefinedAlias)
  {
    return this.aText(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute text statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute text statement object.
   */
  public AttributeText aText(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.rootQuery.checkValidAttributeRequest(name, this.mdStructIF, mdAttributeIF, MdAttributeTextInfo.CLASS);

    return new AttributeText((MdAttributeTextDAOIF)mdAttributeIF, this.attributeNamespace+"."+this._getAttributeName(), this.structTableName,
        this.structTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, this.getMdAttributeIF());
  }

  /**
   * Returns an attribute CLOB statement object.
   * @param name name of the attribute.
   * @return Attribute CLOB statement object.
   */
  public AttributeClob aClob(String name)
  {
    return this.aClob(name, null, null);
  }

  /**
   * Returns an attribute CLOB statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @return Attribute CLOB statement object.
   */
  public AttributeClob aClob(String name, String userDefinedAlias)
  {
    return this.aClob(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute CLOB statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute CLOB statement object.
   */
  public AttributeClob aClob(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.rootQuery.checkValidAttributeRequest(name, this.mdStructIF, mdAttributeIF, MdAttributeClobInfo.CLASS);

    return new AttributeClob((MdAttributeClobDAOIF)mdAttributeIF, this.attributeNamespace+"."+this._getAttributeName(), this.structTableName,
        this.structTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, this.getMdAttributeIF());
  }

  /**
   * Returns an attribute date statement object.
   * @param name name of the attribute.
   * @return Attribute date statement object.
   */
  public AttributeDate aDate(String name)
  {
    return this.aDate(name, null, null);
  }

  /**
   * Returns an attribute date statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @return Attribute date statement object.
   */
  public AttributeDate aDate(String name, String userDefinedAlias)
  {
    return this.aDate(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute date statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute date statement object.
   */
  public AttributeDate aDate(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.rootQuery.checkValidAttributeRequest(name, this.mdStructIF, mdAttributeIF, MdAttributeDateInfo.CLASS);

    return new AttributeDate((MdAttributeDateDAOIF)mdAttributeIF, this.attributeNamespace+"."+this._getAttributeName(), this.structTableName,
        this.structTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, this.getMdAttributeIF());
  }

  /**
   * Returns an attribute time statement object.
   * @param name name of the attribute.
   * @return Attribute time statement object.
   */
  public AttributeTime aTime(String name)
  {
    return this.aTime(name, null, null);
  }

  /**
   * Returns an attribute time statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @return Attribute time statement object.
   */
  public AttributeTime aTime(String name, String userDefinedAlias)
  {
    return this.aTime(name, null, null);
  }

  /**
   * Returns an attribute time statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute time statement object.
   */
  public AttributeTime aTime(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.rootQuery.checkValidAttributeRequest(name, this.mdStructIF, mdAttributeIF, MdAttributeTimeInfo.CLASS);

    return new AttributeTime((MdAttributeTimeDAOIF)mdAttributeIF, this.attributeNamespace+"."+this._getAttributeName(), this.structTableName,
        this.structTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, this.getMdAttributeIF());
  }


  /**
   * Returns an attribute datetime statement object.
   * @param name name of the attribute.
   * @return Attribute datetime statement object.
   */
  public AttributeDateTime aDateTime(String name)
  {
    return this.aDateTime(name, null, null);
  }

  /**
   * Returns an attribute datetime statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @return Attribute datetime statement object.
   */
  public AttributeDateTime aDateTime(String name, String userDefinedAlias)
  {
    return this.aDateTime(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute datetime statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute datetime statement object.
   */
  public AttributeDateTime aDateTime(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.rootQuery.checkValidAttributeRequest(name, this.mdStructIF, mdAttributeIF, MdAttributeDateTimeInfo.CLASS);

    return new AttributeDateTime((MdAttributeDateTimeDAOIF)mdAttributeIF, this.attributeNamespace+"."+this._getAttributeName(), this.structTableName,
        this.structTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, this.getMdAttributeIF());
  }

  /**
   * Returns an attribute integer statement object.
   * @param name name of the attribute.
   * @return Attribute integer statement object.
   */
  public AttributeInteger aInteger(String name)
  {
    return this.aInteger(name, null, null);
  }

  /**
   * Returns an attribute integer statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @return Attribute integer statement object.
   */
  public AttributeInteger aInteger(String name, String userDefinedAlias)
  {
    return this.aInteger(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute integer statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute integer statement object.
   */
  public AttributeInteger aInteger(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.rootQuery.checkValidAttributeRequest(name, this.mdStructIF, mdAttributeIF, MdAttributeIntegerInfo.CLASS);

    return new AttributeInteger((MdAttributeIntegerDAOIF)mdAttributeIF, this.attributeNamespace+"."+this._getAttributeName(), this.structTableName,
        this.structTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, this.getMdAttributeIF());
  }

  /**
   * Returns an attribute long statement object.
   * @param name name of the attribute.
   * @return Attribute long statement object.
   */
  public AttributeLong aLong(String name)
  {
    return aLong(name, null, null);
  }

  /**
   * Returns an attribute long statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @return Attribute long statement object.
   */
  public AttributeLong aLong(String name, String userDefinedAlias)
  {
    return aLong(name, userDefinedAlias, null);
  }


  /**
   * Returns an attribute long statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute long statement object.
   */
  public AttributeLong aLong(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.rootQuery.checkValidAttributeRequest(name, this.mdStructIF, mdAttributeIF, MdAttributeLongInfo.CLASS);

    return new AttributeLong((MdAttributeLongDAOIF)mdAttributeIF, this.attributeNamespace+"."+this._getAttributeName(), this.structTableName,
        this.structTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, this.getMdAttributeIF());
  }

  /**
   * Returns an attribute double statement object.
   * @param name name of the attribute.
   * @return Attribute double statement object.
   */
  public AttributeDouble aDouble(String name)
  {
    return this.aDouble(name, null, null);
  }

  /**
   * Returns an attribute double statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @return Attribute double statement object.
   */
  public AttributeDouble aDouble(String name, String userDefinedAlias)
  {
    return this.aDouble(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute double statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute double statement object.
   */
  public AttributeDouble aDouble(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.rootQuery.checkValidAttributeRequest(name, this.mdStructIF, mdAttributeIF, MdAttributeDoubleInfo.CLASS);

    return new AttributeDouble((MdAttributeDoubleDAOIF)mdAttributeIF,  this.attributeNamespace+"."+this._getAttributeName(), this.structTableName,
        this.structTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, this.getMdAttributeIF());
  }

  /**
   * Returns an attribute decimal statement object.
   * @param name name of the attribute.
   * @return Attribute decimal statement object.
   */
  public AttributeDecimal aDecimal(String name)
  {
    return this.aDecimal(name, null, null);
  }

  /**
   * Returns an attribute decimal statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @return Attribute decimal statement object.
   */
  public AttributeDecimal aDecimal(String name, String userDefinedAlias)
  {
    return this.aDecimal(name, userDefinedAlias, null);
  }


  /**
   * Returns an attribute decimal statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute decimal statement object.
   */
  public AttributeDecimal aDecimal(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.rootQuery.checkValidAttributeRequest(name, this.mdStructIF, mdAttributeIF, MdAttributeDecimalInfo.CLASS);

    return new AttributeDecimal((MdAttributeDecimalDAOIF)mdAttributeIF,  this.attributeNamespace+"."+this._getAttributeName(), this.structTableName,
        this.structTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, this.getMdAttributeIF());
  }

  /**
   * Returns an attribute float statement object.
   * @param name name of the attribute.
   * @return Attribute float statement object.
   */
  public AttributeFloat aFloat(String name)
  {
    return this.aFloat(name, null, null);
  }

  /**
   * Returns an attribute float statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @return Attribute float statement object.
   */
  public AttributeFloat aFloat(String name, String userDefinedAlias)
  {
    return this.aFloat(name, userDefinedAlias, null);
  }


  /**
   * Returns an attribute float statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute float statement object.
   */
  public AttributeFloat aFloat(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.rootQuery.checkValidAttributeRequest(name, this.mdStructIF, mdAttributeIF, MdAttributeFloatInfo.CLASS);

    return new AttributeFloat((MdAttributeFloatDAOIF)mdAttributeIF, this.attributeNamespace+"."+this._getAttributeName(), this.structTableName,
        this.structTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, this.getMdAttributeIF());
  }

  /**
   * Returns an attribute boolean statement object.
   * @param name name of the attribute.
   * @return Attribute boolean statement object.
   */
  public AttributeBoolean aBoolean(String name)
  {
    return this.aBoolean(name, null, null);
  }

  /**
   * Returns an attribute boolean statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @return Attribute boolean statement object.
   */
  public AttributeBoolean aBoolean(String name, String userDefinedAlias)
  {
    return this.aBoolean(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute boolean statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute boolean statement object.
   */
  public AttributeBoolean aBoolean(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.rootQuery.checkValidAttributeRequest(name, this.mdStructIF, mdAttributeIF, MdAttributeBooleanInfo.CLASS);

    return new AttributeBoolean((MdAttributeBooleanDAOIF)mdAttributeIF, this.attributeNamespace+"."+this._getAttributeName(), this.structTableName,
        this.structTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, this.getMdAttributeIF());
  }

  /**
   * Returns an attribute blob statement object.
   * @param name name of the attribute.
   * @return Attribute blob statement object.
   */
  public AttributeBlob aBlob(String name)
  {
    return this.aBlob(name, null, null);
  }

  /**
   * Returns an attribute blob statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @return Attribute blob statement object.
   */
  public AttributeBlob aBlob(String name, String userDefinedAlias)
  {
    return this.aBlob(name, userDefinedAlias, null);
  }

  /**
   * Returns an attribute blob statement object.
   * @param name name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute blob statement object.
   */
  public AttributeBlob aBlob(String name, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);
    this.rootQuery.checkValidAttributeRequest(name, this.mdStructIF, mdAttributeIF, MdAttributeBlobInfo.CLASS);

    return new AttributeBlob((MdAttributeBlobDAOIF)mdAttributeIF, this.attributeNamespace+"."+this._getAttributeName(), this.structTableName,
        this.structTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, this.getMdAttributeIF());
  }

  /**
   * Returns the MdAttributeIF from the map that defines the attribute with the given name.
   * @param attributeName
   * @return MdAttributeIF
   */
  protected MdAttributeConcreteDAOIF getMdAttributeROfromMap(String attributeName)
  {
    return this.structMdAttributeIFMap.get(attributeName.toLowerCase());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Attribute get(String attributeName)
  {
    return this.get(attributeName, null, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Attribute get(String attributeName, String attributeAlias)
  {
    return this.get(attributeName, attributeAlias, null);
  }

  /**
   * Returns an Condition object representing an equals with the attribute with the given name with the given value.
   * @param attributeName name of the attribute.
   * @param attributeAlias user defined alias.
   * @param displayLabel user defined display label
   * @return Condition object representing an equals with the attribute with the given name with the given value.
   */
  public Attribute get(String attributeName, String attributeAlias, String displayLabel)
  {
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    if (mdAttributeIF == null)
    {
      String error = "An attribute named [" + this._getAttributeName() + "] does not exist on type [" + this.mdStructIF.definesType() + "]";
      throw new AttributeDoesNotExistException(error, this._getAttributeName(), this.mdStructIF);
    }

    String attributeType = mdAttributeIF.getType();

    return this.attributeFactory(attributeName, attributeType, attributeAlias, displayLabel);
  }

  
  
  
  /**
   * Returns an attribute query object of type AttributePrimitive for the attribute with the given name and type.
   * It is up to the client to ensure that the parameter attributeType is a primitiveAttributeType.
   * @param attributeName name of the attribute.
   * @param attributeType type of the attribute.
   * @return attribute query object for the attribute with the given name and type.
   */
  public AttributePrimitive attributePrimitiveFactory(String attributeName, String attributeType)
  {
    return (AttributePrimitive)this.attributeFactory(attributeName, attributeType, null, null);
  }

  /**
   * Returns an attribute query object for the attribute with the given name and type.
   * @param attributeName name of the attribute.
   * @param attributeType type of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return attribute query object for the attribute with the given name and type.
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
    else if (attributeType.equals(MdAttributeBlobInfo.CLASS))
    {
      return this.aBlob(attributeName, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (attributeType.equals(MdAttributeEnumerationInfo.CLASS))
    {
      return this.aEnumeration(attributeName, userDefinedAlias, userDefinedDisplayLabel);
    }
    else
    {
      String error = "Attribute type [" + attributeType + "] is invalid.";
      throw new QueryException(error);
    }
  }

  /**
   * Returns a condition based on the String version of the operator
   * and the Ref version of the value.
   * @param operator
   * @param value
   * @return condition based on the String version of the operator
   * and the String version of the value.
   */
  public Condition getCondition(String operator, String value)
  {
    return QueryUtil.getCondition(this, operator, value);
  }

  /**
   * Returns an AttributeEnumeration with the given values.  Generated
   * subclasses with override this method and return subclasses of
   * AttributeEnumeration.
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
   * @return AttributeEnumeration with the given values.  Generated
   * subclasses with override this method and return subclasses of
   * AttributeEnumeration.
   */
  protected AttributeEnumeration enumerationFactory(MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,
      String mdEnumerationTableName, MdBusinessDAOIF masterListMdBusinessIF, String masterListTalbeAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new AttributeEnumeration(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias,
        mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, this.getMdAttributeIF());
  }
}
