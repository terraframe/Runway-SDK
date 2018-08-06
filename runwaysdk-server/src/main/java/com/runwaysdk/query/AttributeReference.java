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

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.BusinessQuery;
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
import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.dataaccess.AttributeDoesNotExistException;
import com.runwaysdk.dataaccess.EntityDAOIF;
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
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.database.Database;

public class AttributeReference extends AttributeRef implements SelectableReference, HasAttributeFactory
{
  private static Map<String, PluginIF> pluginMap = new ConcurrentHashMap<String, PluginIF>();

  public static void registerPlugin(PluginIF pluginFactory)
  {
    pluginMap.put(pluginFactory.getModuleIdentifier(), pluginFactory);
  }

  private String refAttributeNameSpace;

  /**
   * 
   * @param mdAttributeIF
   *          can be an MdAttributeReferenceIF or an MdAttributeFileIF
   * @param attributeNamespace
   * @param definingTableName
   * @param definingTableAlias
   * @param referenceMdBusinessIF
   * @param referenceTableAlias
   * @param rootEntityQuery
   * @param tableJoinSet
   */
  protected AttributeReference(MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);

    this.refAttributeNameSpace = this.attributeNamespace + "." + this.attributeName;
  }

  /**
   * Returns the query factory used by this ComponentQuery.
   * 
   * @return query factory used by this ComponentQuery.
   */
  protected QueryFactory getQueryFactory()
  {
    return this.rootQuery.getQueryFactory();
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

  // Equals
  /**
   * Returns an attribute character statement object representing the object's
   * oid attribute.
   * 
   * @param attributeName
   *          name of the attribute.
   * @return Attribute character statement object.
   */
  public AttributeCharacter oid()
  {
    String attributeName = EntityInfo.OID;
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    this.tableJoinSet.add(new InnerJoinEq(this.columnName, this.definingTableName, this.definingTableAlias, EntityInfo.OID, this.referenceTableName, this.referenceTableAlias));

    return new AttributeCharacter((MdAttributeCharacterDAOIF) mdAttributeIF, this.refAttributeNameSpace + "." + attributeName, this.referenceTableName, this.referenceTableAlias, this.rootQuery, this.tableJoinSet, null, null);
  }

  /**
   * Compares this reference attribute with objects of the given type.
   * 
   * @param businessDAOquery
   * @return Condition object
   */
  public Condition EQ(BusinessDAOQuery businessDAOquery)
  {
    Attribute attribute = businessDAOquery.oid();

    return this.refEqCondition(attribute);
  }

  /**
   * Comparess this reference attirbute with objects of the given type.
   * 
   * @param businessQuery
   * @return Condition object
   */
  public Condition EQ(BusinessQuery businessQuery)
  {
    Attribute attribute = businessQuery.oid();

    return this.refEqCondition(attribute);
  }

  /**
   * Comparess this reference attirbute with objects of the given type.
   * 
   * @param genBusinessQuery
   * @return Condition object
   */
  public Condition EQ(GeneratedBusinessQuery genBusinessQuery)
  {
    AttributeChar attribute = (AttributeChar) genBusinessQuery.getOid();

    return this.refEqCondition(attribute);
  }

  /**
   * Compares this AttributeReference with another.
   * 
   * @param attributeIF
   * @return Condition object
   */
  public Condition EQ(SelectableReference attributeIF)
  {
    AttributeReference attribute = ( (AttributeReference) attributeIF );

    return this.refEqCondition(attribute);
  }

  private Condition refEqCondition(Attribute attribute)
  {
    if (this.getRootQuery() == attribute.getRootQuery())
    {
      return new BasicConditionEq(this, attribute, false);
    }
    else if (attribute.getRootQuery().isUsedInValueQuery() || this.getRootQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionEq(this, attribute, false);
    }
    else
    {
      return new SubSelectBasicConditionEq(this, attribute, false);
    }
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
    StatementPrimitive statementPrimitive = this.formatAndValidate(oid);
    return new BasicConditionEq(this, statementPrimitive, false);
  }

  /**
   * Compares the Component for equality.
   * 
   * @param component
   *          component to compare.
   * @return Basic Condition object
   */
  public BasicCondition EQ(ComponentIF component)
  {
    StatementPrimitive statementPrimitive = this.formatAndValidate(component.getOid());
    return new BasicConditionEq(this, statementPrimitive, false);
  }

  /**
   * Character Equals.
   * 
   * @param selectable
   * @return Condition object
   */
  public Condition EQ(SelectableChar selectable)
  {
    return QueryUtil.EQ(this, selectable);
  }

  // Not Equals
  /**
   * Compares this AttributeReference with another.
   * 
   * @param attributeIF
   * @return Condition object
   */
  public Condition NE(SelectableReference attributeIF)
  {
    AttributeReference attribute = ( (AttributeReference) attributeIF );

    return this.refNotEqCondition(attribute);
  }

  /**
   * Comparess this reference attirbute with objects of the given type.
   * 
   * @param businessDAOquery
   * @return Condition object
   */
  public Condition NE(BusinessDAOQuery businessDAOquery)
  {
    Attribute attribute = businessDAOquery.oid();

    return this.refNotEqCondition(attribute);
  }

  /**
   * Comparess this reference attirbute with objects of the given type.
   * 
   * @param businessQuery
   * @return Condition object
   */
  public Condition NE(BusinessQuery businessQuery)
  {
    Attribute attribute = businessQuery.oid();

    return this.refNotEqCondition(attribute);
  }

  /**
   * Comparess this reference attirbute with objects of the given type.
   * 
   * @param genBusinessQuery
   * @return Condition object
   */
  public Condition NE(GeneratedBusinessQuery genBusinessQuery)
  {
    AttributeChar attribute = (AttributeChar) genBusinessQuery.getOid();

    return this.refNotEqCondition(attribute);
  }

  private Condition refNotEqCondition(Attribute attribute)
  {
    if (this.getRootQuery() == attribute.getRootQuery())
    {
      return new BasicConditionNotEq(this, attribute, false);
    }
    else if (attribute.getRootQuery().isUsedInValueQuery() || this.getRootQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionNotEq(this, attribute, false);
    }
    else
    {
      return new SubSelectBasicConditionNotEq(this, attribute, false);
    }
  }

  /**
   * Compares the oid of a component for non equality.
   * 
   * @param oid
   *          oid of the object to compare.
   * @return Basic Condition object
   */
  public BasicCondition NE(String oid)
  {
    StatementPrimitive statementPrimitive = this.formatAndValidate(oid);
    return new BasicConditionNotEq(this, statementPrimitive, false);
  }

  /**
   * Compares the Component for non equality.
   * 
   * @param component
   *          component to compare.
   * @return Basic Condition object
   */
  public BasicCondition NE(ComponentIF component)
  {
    StatementPrimitive statementPrimitive = this.formatAndValidate(component.getOid());
    return new BasicConditionNotEq(this, statementPrimitive, false);
  }

  /**
   * Character Not Equals.
   * 
   * @param selectable
   * @return Condition object
   */
  public Condition NE(SelectableChar selectable)
  {
    return QueryUtil.NE(this, selectable);
  }

  /**
   * Returns an attribute statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute statement object.
   */
  public Attribute get(String attributeName)
  {
    return get(attributeName, null, null);
  }

  /**
   * Returns an attribute statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute statement object.
   */
  public Attribute get(String attributeName, String userDefinedAlias)
  {
    return get(attributeName, userDefinedAlias, null);
  }

  /**
   * Returns an attribute statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute statement object.
   */
  public Attribute get(String attributeName, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    if (mdAttributeIF == null)
    {
      String error = "An attribute named [" + attributeName + "] does not exist on type [" + this.referenceMdBusinessIF.definesType() + "]";
      throw new AttributeDoesNotExistException(error, attributeName, this.referenceMdBusinessIF);
    }

    MdBusinessDAOIF definingMdBusineessIF = (MdBusinessDAOIF) mdAttributeIF.definedByClass();

    return this.internalAttributeFactory(attributeName, mdAttributeIF, definingMdBusineessIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute character statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @return Attribute character statement object.
   */
  public AttributeCharacter aCharacter(String attributeName)
  {
    return aCharacter(attributeName, null, null);
  }

  /**
   * Returns an attribute character statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute character statement object.
   */
  public AttributeCharacter aCharacter(String attributeName, String userDefinedAlias)
  {
    return aCharacter(attributeName, userDefinedAlias, null);
  }

  /**
   * Returns an attribute character statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute character statement object.
   */
  public AttributeCharacter aCharacter(String attributeName, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    this.rootQuery.checkValidAttributeRequest(attributeName, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeCharacterInfo.CLASS);

    MdBusinessDAOIF definingMdBusineessIF = (MdBusinessDAOIF) mdAttributeIF.definedByClass();

    return (AttributeCharacter) this.internalAttributeFactory(attributeName, mdAttributeIF, definingMdBusineessIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute text statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @return Attribute text statement object.
   */
  public AttributeText aText(String attributeName)
  {
    return aText(attributeName, null, null);
  }

  /**
   * Returns an attribute text statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute text statement object.
   */
  public AttributeText aText(String attributeName, String userDefinedAlias)
  {
    return aText(attributeName, userDefinedAlias, null);
  }

  /**
   * Returns an attribute text statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute text statement object.
   */
  public AttributeText aText(String attributeName, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    this.rootQuery.checkValidAttributeRequest(attributeName, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeTextInfo.CLASS);

    MdBusinessDAOIF definingMdBusinessIF = (MdBusinessDAOIF) mdAttributeIF.definedByClass();

    return (AttributeText) this.internalAttributeFactory(attributeName, mdAttributeIF, definingMdBusinessIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute CLOB statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @return Attribute CLOB statement object.
   */
  public AttributeClob aClob(String attributeName)
  {
    return aClob(attributeName, null, null);
  }

  /**
   * Returns an attribute CLOB statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute CLOB statement object.
   */
  public AttributeClob aClob(String attributeName, String userDefinedAlias)
  {
    return aClob(attributeName, userDefinedAlias, null);
  }

  /**
   * Returns an attribute CLOB statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute CLOB statement object.
   */
  public AttributeClob aClob(String attributeName, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    this.rootQuery.checkValidAttributeRequest(attributeName, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeClobInfo.CLASS);

    MdBusinessDAOIF definingMdBusinessIF = (MdBusinessDAOIF) mdAttributeIF.definedByClass();

    return (AttributeClob) this.internalAttributeFactory(attributeName, mdAttributeIF, definingMdBusinessIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute date statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @return Attribute date statement object.
   */
  public AttributeDate aDate(String attributeName)
  {
    return this.aDate(attributeName, null, null);
  }

  /**
   * Returns an attribute date statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute date statement object.
   */
  public AttributeDate aDate(String attributeName, String userDefinedAlias)
  {
    return this.aDate(attributeName, userDefinedAlias, null);
  }

  /**
   * Returns an attribute date statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute date statement object.
   */
  public AttributeDate aDate(String attributeName, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    this.rootQuery.checkValidAttributeRequest(attributeName, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeDateInfo.CLASS);

    MdBusinessDAOIF definingMdBusinessIF = (MdBusinessDAOIF) mdAttributeIF.definedByClass();

    return (AttributeDate) this.internalAttributeFactory(attributeName, mdAttributeIF, definingMdBusinessIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute time statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @return Attribute time statement object.
   */
  public AttributeTime aTime(String attributeName)
  {
    return this.aTime(attributeName, null, null);
  }

  /**
   * Returns an attribute time statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute time statement object.
   */
  public AttributeTime aTime(String attributeName, String userDefinedAlias)
  {
    return this.aTime(attributeName, userDefinedAlias, null);
  }

  /**
   * Returns an attribute time statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute time statement object.
   */
  public AttributeTime aTime(String attributeName, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    this.rootQuery.checkValidAttributeRequest(attributeName, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeTimeInfo.CLASS);

    MdBusinessDAOIF definingMdBusinessIF = (MdBusinessDAOIF) mdAttributeIF.definedByClass();

    return (AttributeTime) this.internalAttributeFactory(attributeName, mdAttributeIF, definingMdBusinessIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute datetime statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @return Attribute datetime statement object.
   */
  public AttributeDateTime aDateTime(String attributeName)
  {
    return this.aDateTime(attributeName, null, null);
  }

  /**
   * Returns an attribute datetime statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute datetime statement object.
   */
  public AttributeDateTime aDateTime(String attributeName, String userDefinedAlias)
  {
    return this.aDateTime(attributeName, userDefinedAlias, null);
  }

  /**
   * Returns an attribute datetime statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute datetime statement object.
   */
  public AttributeDateTime aDateTime(String attributeName, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    this.rootQuery.checkValidAttributeRequest(attributeName, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeDateTimeInfo.CLASS);

    MdBusinessDAOIF definingMdBusinessIF = (MdBusinessDAOIF) mdAttributeIF.definedByClass();

    return (AttributeDateTime) this.internalAttributeFactory(attributeName, mdAttributeIF, definingMdBusinessIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute integer statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @return Attribute integer statement object.
   */
  public AttributeInteger aInteger(String attributeName)
  {
    return this.aInteger(attributeName, null, null);
  }

  /**
   * Returns an attribute integer statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute integer statement object.
   */
  public AttributeInteger aInteger(String attributeName, String userDefinedAlias)
  {
    return this.aInteger(attributeName, userDefinedAlias, null);
  }

  /**
   * Returns an attribute integer statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute integer statement object.
   */
  public AttributeInteger aInteger(String attributeName, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    this.rootQuery.checkValidAttributeRequest(attributeName, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeIntegerInfo.CLASS);

    MdBusinessDAOIF definingMdBusinessIF = (MdBusinessDAOIF) mdAttributeIF.definedByClass();

    return (AttributeInteger) this.internalAttributeFactory(attributeName, mdAttributeIF, definingMdBusinessIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute long statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @return Attribute long statement object.
   */
  public AttributeLong aLong(String attributeName)
  {
    return aLong(attributeName, null, null);
  }

  /**
   * Returns an attribute long statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute long statement object.
   */
  public AttributeLong aLong(String attributeName, String userDefinedAlias)
  {
    return aLong(attributeName, userDefinedAlias, null);
  }

  /**
   * Returns an attribute long statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute long statement object.
   */
  public AttributeLong aLong(String attributeName, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    this.rootQuery.checkValidAttributeRequest(attributeName, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeLongInfo.CLASS);

    MdBusinessDAOIF definingMdBusinessIF = (MdBusinessDAOIF) mdAttributeIF.definedByClass();

    return (AttributeLong) this.internalAttributeFactory(attributeName, mdAttributeIF, definingMdBusinessIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute double statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @return Attribute double statement object.
   */
  public AttributeDouble aDouble(String attributeName)
  {
    return this.aDouble(attributeName, null, null);
  }

  /**
   * Returns an attribute double statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute double statement object.
   */
  public AttributeDouble aDouble(String attributeName, String userDefinedAlias)
  {
    return this.aDouble(attributeName, userDefinedAlias, null);
  }

  /**
   * Returns an attribute double statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute double statement object.
   */
  public AttributeDouble aDouble(String attributeName, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    this.rootQuery.checkValidAttributeRequest(attributeName, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeDoubleInfo.CLASS);

    MdBusinessDAOIF definingMdBusinessIF = (MdBusinessDAOIF) mdAttributeIF.definedByClass();

    return (AttributeDouble) this.internalAttributeFactory(attributeName, mdAttributeIF, definingMdBusinessIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute decimal statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @return Attribute decimal statement object.
   */
  public AttributeDecimal aDecimal(String attributeName)
  {
    return this.aDecimal(attributeName, null, null);
  }

  /**
   * Returns an attribute decimal statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute decimal statement object.
   */
  public AttributeDecimal aDecimal(String attributeName, String userDefinedAlias)
  {
    return this.aDecimal(attributeName, userDefinedAlias, null);
  }

  /**
   * Returns an attribute decimal statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute decimal statement object.
   */
  public AttributeDecimal aDecimal(String attributeName, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    this.rootQuery.checkValidAttributeRequest(attributeName, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeDecimalInfo.CLASS);

    MdBusinessDAOIF definingMdBusinessIF = (MdBusinessDAOIF) mdAttributeIF.definedByClass();

    return (AttributeDecimal) this.internalAttributeFactory(attributeName, mdAttributeIF, definingMdBusinessIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute float statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @return Attribute float statement object.
   */
  public AttributeFloat aFloat(String attributeName)
  {
    return this.aFloat(attributeName, null, null);
  }

  /**
   * Returns an attribute float statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute float statement object.
   */
  public AttributeFloat aFloat(String attributeName, String userDefinedAlias)
  {
    return this.aFloat(attributeName, userDefinedAlias, null);
  }

  /**
   * Returns an attribute float statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute float statement object.
   */
  public AttributeFloat aFloat(String attributeName, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    this.rootQuery.checkValidAttributeRequest(attributeName, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeFloatInfo.CLASS);

    MdBusinessDAOIF definingMdBusinessIF = (MdBusinessDAOIF) mdAttributeIF.definedByClass();

    return (AttributeFloat) this.internalAttributeFactory(attributeName, mdAttributeIF, definingMdBusinessIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute boolean statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @return Attribute boolean statement object.
   */
  public AttributeBoolean aBoolean(String attributeName)
  {
    return this.aBoolean(attributeName, null, null);
  }

  /**
   * Returns an attribute boolean statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute boolean statement object.
   */
  public AttributeBoolean aBoolean(String attributeName, String userDefinedAlias)
  {
    return this.aBoolean(attributeName, userDefinedAlias, null);
  }

  /**
   * Returns an attribute boolean statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute boolean statement object.
   */
  public AttributeBoolean aBoolean(String attributeName, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    this.rootQuery.checkValidAttributeRequest(attributeName, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeBooleanInfo.CLASS);

    MdBusinessDAOIF definingMdBusinessIF = (MdBusinessDAOIF) mdAttributeIF.definedByClass();

    return (AttributeBoolean) this.internalAttributeFactory(attributeName, mdAttributeIF, definingMdBusinessIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute blob statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @return Attribute blob statement object.
   */
  public AttributeBlob aBlob(String attributeName)
  {
    return this.aBlob(attributeName, null, null);
  }

  /**
   * Returns an attribute blob statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute blob statement object.
   */
  public AttributeBlob aBlob(String attributeName, String userDefinedAlias)
  {
    return this.aBlob(attributeName, userDefinedAlias, null);
  }

  /**
   * Returns an attribute blob statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute blob statement object.
   */
  public AttributeBlob aBlob(String attributeName, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    this.rootQuery.checkValidAttributeRequest(attributeName, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeBlobInfo.CLASS);

    MdBusinessDAOIF definingMdBusinessIF = (MdBusinessDAOIF) mdAttributeIF.definedByClass();

    return (AttributeBlob) this.internalAttributeFactory(attributeName, mdAttributeIF, definingMdBusinessIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute struct statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @return Attribute struct statement object.
   */
  public AttributeStruct aStruct(String attributeName)
  {
    return this.aStruct(attributeName, null, null);
  }

  /**
   * Returns an attribute struct statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute struct statement object.
   */
  public AttributeStruct aStruct(String attributeName, String userDefinedAlias)
  {
    return this.aStruct(attributeName, userDefinedAlias, null);
  }

  /**
   * Returns an attribute struct statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabe
   * @return Attribute struct statement object.
   */
  public AttributeStruct aStruct(String attributeName, String userDefinedAlias, String userDefinedDisplayLabe)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    this.rootQuery.checkValidAttributeRequest(attributeName, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeStructInfo.CLASS);

    MdBusinessDAOIF definingMdBusinessIF = (MdBusinessDAOIF) mdAttributeIF.definedByClass();

    return (AttributeStruct) this.internalAttributeFactory(attributeName, mdAttributeIF, definingMdBusinessIF, userDefinedAlias, userDefinedDisplayLabe);
  }

  /**
   * Returns an attribute local statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @return Attribute local statement object.
   */
  public AttributeLocal aLocalCharacter(String attributeName)
  {
    return this.aLocalCharacter(attributeName, null, null);
  }

  /**
   * Returns an attribute local statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute local statement object.
   */
  public AttributeLocal aLocalCharacter(String attributeName, String userDefinedAlias)
  {
    return this.aLocalCharacter(attributeName, userDefinedAlias, null);
  }

  /**
   * Returns an attribute local statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabe
   * @return Attribute local statement object.
   */
  public AttributeLocal aLocalCharacter(String attributeName, String userDefinedAlias, String userDefinedDisplayLabe)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    this.rootQuery.checkValidAttributeRequest(attributeName, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeLocalCharacterInfo.CLASS);

    MdBusinessDAOIF definingMdBusinessIF = (MdBusinessDAOIF) mdAttributeIF.definedByClass();

    return (AttributeLocal) this.internalAttributeFactory(attributeName, mdAttributeIF, definingMdBusinessIF, userDefinedAlias, userDefinedDisplayLabe);
  }

  /**
   * Returns an attribute local statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @return Attribute local statement object.
   */
  public AttributeLocal aLocalText(String attributeName)
  {
    return this.aLocalText(attributeName, null, null);
  }

  /**
   * Returns an attribute local statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute local statement object.
   */
  public AttributeLocal aLocalText(String attributeName, String userDefinedAlias)
  {
    return this.aLocalText(attributeName, userDefinedAlias, null);
  }

  /**
   * Returns an attribute local statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabe
   * @return Attribute local statement object.
   */
  public AttributeLocal aLocalText(String attributeName, String userDefinedAlias, String userDefinedDisplayLabe)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    this.rootQuery.checkValidAttributeRequest(attributeName, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeLocalTextInfo.CLASS);

    MdBusinessDAOIF definingMdBusinessIF = (MdBusinessDAOIF) mdAttributeIF.definedByClass();

    return (AttributeLocal) this.internalAttributeFactory(attributeName, mdAttributeIF, definingMdBusinessIF, userDefinedAlias, userDefinedDisplayLabe);
  }

  /**
   * Returns an attribute reference statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @return Attribute referrence statement object.
   */
  public AttributeReference aReference(String attributeName)
  {
    return this.aReference(attributeName, null, null);
  }

  /**
   * Returns an attribute reference statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute referrence statement object.
   */
  public AttributeReference aReference(String attributeName, String userDefinedAlias)
  {
    return this.aReference(attributeName, userDefinedAlias, null);
  }

  /**
   * Returns an attribute reference statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute referrence statement object.
   */
  public AttributeReference aReference(String attributeName, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    this.rootQuery.checkValidAttributeRequest(attributeName, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeReferenceInfo.CLASS);

    MdBusinessDAOIF definingMdBusinessIF = (MdBusinessDAOIF) mdAttributeIF.definedByClass();

    return (AttributeReference) this.internalAttributeFactory(attributeName, mdAttributeIF, definingMdBusinessIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute term statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute term statement object.
   */
  public AttributeTerm aTerm(String attributeName, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    this.rootQuery.checkValidAttributeRequest(attributeName, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeReferenceInfo.CLASS);

    MdBusinessDAOIF definingMdBusinessIF = (MdBusinessDAOIF) mdAttributeIF.definedByClass();

    return (AttributeTerm) this.internalAttributeFactory(attributeName, mdAttributeIF, definingMdBusinessIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute file statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @return Attribute file statement object.
   */
  public AttributeReference aFile(String attributeName)
  {
    return this.aFile(attributeName, null, null);
  }

  /**
   * Returns an attribute file statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute file statement object.
   */
  public AttributeReference aFile(String attributeName, String userDefinedAlias)
  {
    return this.aFile(attributeName, userDefinedAlias, null);
  }

  /**
   * Returns an attribute file statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute file statement object.
   */
  public AttributeReference aFile(String attributeName, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    this.rootQuery.checkValidAttributeRequest(attributeName, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeFileInfo.CLASS);

    MdBusinessDAOIF definingMdBusinessIF = (MdBusinessDAOIF) mdAttributeIF.definedByClass();

    return (AttributeReference) this.internalAttributeFactory(attributeName, mdAttributeIF, definingMdBusinessIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute enumeration statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @return Attribute enumeration statement object.
   */
  public AttributeEnumeration aEnumeration(String attributeName)
  {
    return this.aEnumeration(attributeName, null, null);
  }

  /**
   * Returns an attribute enumeration statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute enumeration statement object.
   */
  public AttributeEnumeration aEnumeration(String attributeName, String userDefinedAlias)
  {
    return this.aEnumeration(attributeName, userDefinedAlias, null);
  }

  /**
   * Returns an attribute enumeration statement object.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute enumeration statement object.
   */
  public AttributeEnumeration aEnumeration(String attributeName, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeROfromMap(attributeName);

    this.rootQuery.checkValidAttributeRequest(attributeName, this.referenceMdBusinessIF, mdAttributeIF, MdAttributeEnumerationInfo.CLASS);

    MdBusinessDAOIF definingMdBusinessIF = (MdBusinessDAOIF) mdAttributeIF.definedByClass();

    return (AttributeEnumeration) this.internalAttributeFactory(attributeName, mdAttributeIF, definingMdBusinessIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Factory to construct an attribute object with the given name, but excludes
   * AttriubteEnumerations.
   * 
   * @return attribute object with the given name.
   */
  private Attribute internalAttributeFactory(String attributeName, MdAttributeConcreteDAOIF mdAttributeIF, MdBusinessDAOIF definingMdBusinessIF, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    this.tableJoinSet.add(new InnerJoinEq(this.columnName, this.definingTableName, this.definingTableAlias, EntityDAOIF.ID_COLUMN, this.referenceTableName, this.referenceTableAlias));

    String refAttrReffAttrNamespace = this.refAttributeNameSpace + "." + attributeName;

    String parameterTableName;
    String parameterTableAlias;

    String tableName = definingMdBusinessIF.getTableName();
    if (!tableName.equals(this.referenceTableName))
    {
      String tableAlias = this.rootQuery.getTableAlias(refAttrReffAttrNamespace, tableName);
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
      attribute = new AttributeCharacter((MdAttributeCharacterDAOIF) mdAttributeIF, refAttrReffAttrNamespace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeTextDAOIF)
    {
      attribute = new AttributeText((MdAttributeTextDAOIF) mdAttributeIF, refAttrReffAttrNamespace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeClobDAOIF)
    {
      attribute = new AttributeClob((MdAttributeClobDAOIF) mdAttributeIF, refAttrReffAttrNamespace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeDateDAOIF)
    {
      attribute = new AttributeDate((MdAttributeDateDAOIF) mdAttributeIF, refAttrReffAttrNamespace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeTimeDAOIF)
    {
      attribute = new AttributeTime((MdAttributeTimeDAOIF) mdAttributeIF, refAttrReffAttrNamespace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeDateTimeDAOIF)
    {
      attribute = new AttributeDateTime((MdAttributeDateTimeDAOIF) mdAttributeIF, refAttrReffAttrNamespace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeIntegerDAOIF)
    {
      attribute = new AttributeInteger((MdAttributeIntegerDAOIF) mdAttributeIF, refAttrReffAttrNamespace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeLongDAOIF)
    {
      attribute = new AttributeLong((MdAttributeLongDAOIF) mdAttributeIF, refAttrReffAttrNamespace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeDoubleDAOIF)
    {
      attribute = new AttributeDouble((MdAttributeDoubleDAOIF) mdAttributeIF, refAttrReffAttrNamespace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeDecimalDAOIF)
    {
      attribute = new AttributeDecimal((MdAttributeDecimalDAOIF) mdAttributeIF, refAttrReffAttrNamespace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeFloatDAOIF)
    {
      attribute = new AttributeFloat((MdAttributeFloatDAOIF) mdAttributeIF, refAttrReffAttrNamespace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeBooleanDAOIF)
    {
      attribute = new AttributeBoolean((MdAttributeBooleanDAOIF) mdAttributeIF, refAttrReffAttrNamespace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeBlobDAOIF)
    {
      attribute = new AttributeBlob((MdAttributeBlobDAOIF) mdAttributeIF, refAttrReffAttrNamespace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeLocalDAOIF)
    {
      MdAttributeLocalDAOIF mdAttributeLocalIF = (MdAttributeLocalDAOIF) mdAttributeIF;
      MdLocalStructDAOIF localStructMdBusinessIF = mdAttributeLocalIF.getMdStructDAOIF();
      String structTableName = localStructMdBusinessIF.getTableName();
      String structTableAlias = this.rootQuery.getTableAlias(refAttrReffAttrNamespace, structTableName);

      attribute = this.localFactory(mdAttributeLocalIF, refAttrReffAttrNamespace, parameterTableName, parameterTableAlias, localStructMdBusinessIF, structTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeStructDAOIF)
    {
      MdAttributeStructDAOIF mdAttributeStructIF = (MdAttributeStructDAOIF) mdAttributeIF;
      MdStructDAOIF structMdBusinessIF = mdAttributeStructIF.getMdStructDAOIF();
      String structTableName = structMdBusinessIF.getTableName();
      String structTableAlias = this.rootQuery.getTableAlias(refAttrReffAttrNamespace, structTableName);

      attribute = this.structFactory(mdAttributeStructIF, refAttrReffAttrNamespace, parameterTableName, parameterTableAlias, structMdBusinessIF, structTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeEnumerationDAOIF)
    {
      MdAttributeEnumerationDAOIF mdAttributeEnumerationIF = (MdAttributeEnumerationDAOIF) mdAttributeIF;
      MdEnumerationDAOIF mdEnumerationIF = mdAttributeEnumerationIF.getMdEnumerationDAO();
      String mdEnumerationTableName = mdEnumerationIF.getTableName();

      MdBusinessDAOIF masterListMdBusinessIF = mdEnumerationIF.getMasterListMdBusinessDAO();
      String masterListTalbeAlias = this.rootQuery.getTableAlias(this.attributeNamespace, masterListMdBusinessIF.getTableName());

      attribute = this.enumerationFactory(mdAttributeEnumerationIF, refAttrReffAttrNamespace, parameterTableName, parameterTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeRefDAOIF)
    {
      MdAttributeRefDAOIF mdAttributeRefIF = (MdAttributeRefDAOIF) mdAttributeIF;

      MdBusinessDAOIF refAttrMdBusinessIF = mdAttributeRefIF.getReferenceMdBusinessDAO();

      String referenceAttrTableName = refAttrMdBusinessIF.getTableName();
      String referenceAttrTableAlias = this.rootQuery.getTableAlias(refAttrReffAttrNamespace, referenceAttrTableName);

      attribute = referenceFactory(mdAttributeRefIF, refAttrReffAttrNamespace, parameterTableName, parameterTableAlias, refAttrMdBusinessIF, referenceAttrTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }

    if (attribute == null)
    {
      for (PluginIF plugin : pluginMap.values())
      {
        attribute = plugin.internalAttributeFactory(definingMdBusinessIF, attributeName, mdAttributeIF, this.refAttributeNameSpace, parameterTableName, parameterTableAlias, this.rootQuery, this.tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);

        if (attribute != null)
        {
          break;
        }
      }
    }

    if (attribute == null)
    {
      String error = "Attribute [" + attributeName + "] is not primitive.";
      throw new QueryException(error);
    }

    return attribute;
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
   * @return AttributeEnumeration with the given values. Generated subclasses
   *         with override this method and return subclasses of
   *         AttributeEnumeration.
   */
  protected AttributeEnumeration enumerationFactory(MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdEnumerationTableName, MdBusinessDAOIF masterListMdBusinessIF, String masterListTalbeAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new AttributeEnumeration(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an AttributeReference or an AttributeFile with the given values.
   * Generated subclasses with override this method and return subclasses of
   * AttributeReference.
   * 
   * @param dAttributeIF
   *          either an MdAttributeReferenceIF or an MdAttributeFileIF
   * @param attributeNamespace
   * @param definingTableName
   * @param definingTableAlias
   * @param referenceMdBusinessIF
   * @param referenceTableAlias
   * @param rootEntityQuery
   * @param tableJoinSet
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
   * @return AttributeStruct with the given values. Generated subclasses with
   *         override this method and return subclasses of AttributeStruct.
   */
  protected AttributeStruct structFactory(MdAttributeStructDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, MdStructDAOIF mdStructIF, String structTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
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
  protected AttributeLocal localFactory(MdAttributeLocalDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, MdLocalStructDAOIF mdLocalStructIF, String structTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new AttributeLocal(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdLocalStructIF, structTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
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
    else if (attributeType.equals(MdAttributeTextInfo.CLASS))
    {
      return this.aText(attributeName, userDefinedAlias, userDefinedDisplayLabel);
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
    else if (attributeType.equals(MdAttributeReferenceInfo.CLASS))
    {
      return this.aReference(attributeName, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (attributeType.equals(MdAttributeTermInfo.CLASS))
    {
      return this.aTerm(attributeName, userDefinedAlias, userDefinedDisplayLabel);
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

  /**
   * Formats and validates a character string.
   * 
   * @param statement
   * @return
   */
  protected StatementPrimitive formatAndValidate(String statement)
  {
    String formattedValue = Database.formatJavaToSQL(statement, MdAttributeReferenceInfo.CLASS, false);
    return new StatementPrimitive(formattedValue);
  }

  /**
   * Restricts the query to include objects that participate as parents in the
   * given relationship type. Does a subselect if a <code>ValueQuery</code> is
   * involved, otherwise not. Instantiate a AbstractRelationshipQuery object of
   * the desired relationship type.
   * 
   * @param abstractRelationshipQuery
   * @return Condition restricting objects that participate as parents in the
   *         given relationship.
   */
  public Condition isParentIn(AbstractRelationshipQuery abstractRelationshipQuery)
  {
    if (this.rootQuery.isUsedInValueQuery() || abstractRelationshipQuery.isUsedInValueQuery())
    {
      return new ValueJoinConditionEq((AttributeCharacter) this.oid(), abstractRelationshipQuery.parentOid());
    }
    else
    {
      return new SubSelectBasicConditionEq((AttributeCharacter) this.oid(), abstractRelationshipQuery.parentOid());
    }
  }

  /**
   * Restricts the query to include objects that participate as parents in the
   * given relationship type. Instantiate a AbstractRelationshipQuery object of
   * the desired relationship type.
   * 
   * @param abstractRelationshipQuery
   * @return Condition restricting objects that participate as parents in the
   *         given relationship.
   */
  public Condition isParentIn_SUBSELECT(AbstractRelationshipQuery abstractRelationshipQuery)
  {
    return new SubSelectBasicConditionEq((AttributeCharacter) this.oid(), abstractRelationshipQuery.parentOid());
  }

  /**
   * Restricts the query to include objects that do not participate as parents
   * in the given relationship type. Does a subselect if a
   * <code>ValueQuery</code> is involved, otherwise not. Instantiate a
   * AbstractRelationshipQuery object of the desired relationship type.
   * 
   * @param abstractRelationshipQuery
   * @return Condition restricting objects that do not participate as parents in
   *         the given relationship.
   */
  public Condition isNotParentIn(AbstractRelationshipQuery abstractRelationshipQuery)
  {
    if (this.rootQuery.isUsedInValueQuery() || abstractRelationshipQuery.isUsedInValueQuery())
    {
      return new ValueJoinConditionNotEq((AttributeCharacter) this.oid(), abstractRelationshipQuery.parentOid());
    }
    else
    {
      return new SubSelectBasicConditionNotEq((AttributeCharacter) this.oid(), abstractRelationshipQuery.parentOid());
    }
  }

  /**
   * Restricts the query to include objects that do not participate as parents
   * in the given relationship type. Instantiate a AbstractRelationshipQuery
   * object of the desired relationship type.
   * 
   * @param abstractRelationshipQuery
   * @return Condition restricting objects that do not participate as parents in
   *         the given relationship.
   */
  public Condition isNotParentIn_SUBSELECT(AbstractRelationshipQuery abstractRelationshipQuery)
  {
    return new SubSelectBasicConditionNotEq((AttributeCharacter) this.oid(), abstractRelationshipQuery.parentOid());
  }

  /**
   * Restricts the query to include objects that participate as parents in the
   * given relationship type. Does a subselect if a <code>ValueQuery</code> is
   * involved, otherwise not. Instantiate a GeneratedRelationshipQuery object of
   * the desired relationship type.
   * 
   * @param generatedRelationshipQuery
   * @return Condition restricting objects that participate as parents in the
   *         given relationship.
   */
  public Condition isParentIn(GeneratedRelationshipQuery generatedRelationshipQuery)
  {
    if (this.rootQuery.isUsedInValueQuery() || generatedRelationshipQuery.getComponentQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionEq((AttributeCharacter) this.oid(), generatedRelationshipQuery.parentOid());
    }
    else
    {
      return new SubSelectBasicConditionEq((AttributeCharacter) this.oid(), generatedRelationshipQuery.parentOid());
    }
  }

  /**
   * Restricts the query to include objects that participate as parents in the
   * given relationship type. Instantiate a GeneratedRelationshipQuery object of
   * the desired relationship type.
   * 
   * @param generatedRelationshipQuery
   * @return Condition restricting objects that participate as parents in the
   *         given relationship.
   */
  public Condition isParentIn_SUBSELECT(GeneratedRelationshipQuery generatedRelationshipQuery)
  {
    return new SubSelectBasicConditionEq((AttributeCharacter) this.oid(), generatedRelationshipQuery.parentOid());
  }

  /**
   * Restricts the query to include objects that do not participate as parents
   * in the given relationship type. Does a subselect if a
   * <code>ValueQuery</code> is involved, otherwise not. Instantiate a
   * GeneratedRelationshipQuery object of the desired relationship type.
   * 
   * @param generatedRelationshipQuery
   * @return Condition restricting objects that do not participate as parents in
   *         the given relationship.
   */
  public Condition isNotParentIn(GeneratedRelationshipQuery generatedRelationshipQuery)
  {
    if (this.rootQuery.isUsedInValueQuery() || generatedRelationshipQuery.getComponentQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionNotEq((AttributeCharacter) this.oid(), generatedRelationshipQuery.parentOid());
    }
    else
    {
      return new SubSelectBasicConditionNotEq((AttributeCharacter) this.oid(), generatedRelationshipQuery.parentOid());
    }
  }

  /**
   * Restricts the query to include objects that do not participate as parents
   * in the given relationship type. Instantiate a GeneratedRelationshipQuery
   * object of the desired relationship type.
   * 
   * @param generatedRelationshipQuery
   * @return Condition restricting objects that do not participate as parents in
   *         the given relationship.
   */
  public Condition isNotParentIn_SUBSELECT(GeneratedRelationshipQuery generatedRelationshipQuery)
  {
    return new SubSelectBasicConditionNotEq((AttributeCharacter) this.oid(), generatedRelationshipQuery.parentOid());
  }

  /**
   * Restricts the query to include objects that participate as children in the
   * given relationship type. Does a subselect if a <code>ValueQuery</code> is
   * involved, otherwise not. Instantiate a AbstractRelationshipQuery object of
   * the desired relationship type.
   * 
   * @param abstractRelationshipQuery
   * @return Condition restricting objects that participate as children in the
   *         given relationship.
   */
  public Condition isChildIn(AbstractRelationshipQuery abstractRelationshipQuery)
  {
    if (this.rootQuery.isUsedInValueQuery() || abstractRelationshipQuery.isUsedInValueQuery())
    {
      return new ValueJoinConditionEq((AttributeCharacter) this.oid(), abstractRelationshipQuery.childOid());
    }
    else
    {
      return new SubSelectBasicConditionEq((AttributeCharacter) this.oid(), abstractRelationshipQuery.childOid());
    }
  }

  /**
   * Restricts the query to include objects that participate as children in the
   * given relationship type. Instantiate a AbstractRelationshipQuery object of
   * the desired relationship type.
   * 
   * @param abstractRelationshipQuery
   * @return Condition restricting objects that participate as children in the
   *         given relationship.
   */
  public Condition isChildIn_SUBSELECT(AbstractRelationshipQuery abstractRelationshipQuery)
  {
    return new SubSelectBasicConditionEq((AttributeCharacter) this.oid(), abstractRelationshipQuery.childOid());
  }

  /**
   * Restricts the query to include objects that do not participate as children
   * in the given relationship type. Does a subselect if a
   * <code>ValueQuery</code> is involved, otherwise not. Instantiate a
   * AbstractRelationshipQuery object of the desired relationship type.
   * 
   * @param abstractRelationshipQuery
   * @return Condition restricting objects that do not participate as children
   *         in the given relationship.
   */
  public Condition isNotChildIn(AbstractRelationshipQuery abstractRelationshipQuery)
  {
    if (this.rootQuery.isUsedInValueQuery() || abstractRelationshipQuery.isUsedInValueQuery())
    {
      return new ValueJoinConditionNotEq((AttributeCharacter) this.oid(), abstractRelationshipQuery.childOid());
    }
    else
    {
      return new SubSelectBasicConditionNotEq((AttributeCharacter) this.oid(), abstractRelationshipQuery.childOid());
    }
  }

  /**
   * Restricts the query to include objects that do not participate as children
   * in the given relationship type. Instantiate a AbstractRelationshipQuery
   * object of the desired relationship type.
   * 
   * @param abstractRelationshipQuery
   * @return Condition restricting objects that do not participate as children
   *         in the given relationship.
   */
  public Condition isNotChildIn_SUBSELECT(AbstractRelationshipQuery abstractRelationshipQuery)
  {
    return new SubSelectBasicConditionNotEq((AttributeCharacter) this.oid(), abstractRelationshipQuery.childOid());
  }

  /**
   * Restricts the query to include objects that participate as children in the
   * given relationship type. Does a subselect if a <code>ValueQuery</code> is
   * involved, otherwise not. Instantiate a GeneratedRelationshipQuery object of
   * the desired relationship type.
   * 
   * @param generatedRelationshipQuery
   * @return Condition restricting objects that participate as children in the
   *         given relationship.
   */
  public Condition isChildIn(GeneratedRelationshipQuery generatedRelationshipQuery)
  {
    if (this.rootQuery.isUsedInValueQuery() || generatedRelationshipQuery.getComponentQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionEq((AttributeCharacter) this.oid(), generatedRelationshipQuery.childOid());
    }
    else
    {
      return new SubSelectBasicConditionEq((AttributeCharacter) this.oid(), generatedRelationshipQuery.childOid());
    }
  }

  /**
   * Restricts the query to include objects that participate as children in the
   * given relationship type. Instantiate a GeneratedRelationshipQuery object of
   * the desired relationship type.
   * 
   * @param generatedRelationshipQuery
   * @return Condition restricting objects that participate as children in the
   *         given relationship.
   */
  public Condition isChildIn_SUBSELECT(GeneratedRelationshipQuery generatedRelationshipQuery)
  {
    return new SubSelectBasicConditionEq((AttributeCharacter) this.oid(), generatedRelationshipQuery.childOid());
  }

  /**
   * Restricts the query to include objects that do not participate as children
   * in the given relationship type. Does a subselect if a
   * <code>ValueQuery</code> is involved, otherwise not. Instantiate a
   * GeneratedRelationshipQuery object of the desired relationship type.
   * 
   * @param generatedRelationshipQuery
   * @return Condition restricting objects that do not participate as children
   *         in the given relationship.
   */
  public Condition isNotChildIn(GeneratedRelationshipQuery generatedRelationshipQuery)
  {
    if (this.rootQuery.isUsedInValueQuery() || generatedRelationshipQuery.getComponentQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionNotEq((AttributeCharacter) this.oid(), generatedRelationshipQuery.childOid());
    }
    else
    {
      return new SubSelectBasicConditionNotEq((AttributeCharacter) this.oid(), generatedRelationshipQuery.childOid());
    }
  }

  /**
   * Restricts the query to include objects that do not participate as children
   * in the given relationship type. Instantiate a GeneratedRelationshipQuery
   * object of the desired relationship type.
   * 
   * @param generatedRelationshipQuery
   * @return Condition restricting objects that do not participate as children
   *         in the given relationship.
   */
  public Condition isNotChildIn_SUBSELECT(GeneratedRelationshipQuery generatedRelationshipQuery)
  {
    return new SubSelectBasicConditionNotEq((AttributeCharacter) this.oid(), generatedRelationshipQuery.childOid());
  }

  public static interface PluginIF
  {
    public String getModuleIdentifier();

    public Attribute internalAttributeFactory(MdBusinessDAOIF definingMdBusinessIF, String attributeName, MdAttributeConcreteDAOIF mdAttributeIF, String refAttributeNameSpace, String parameterTableName, String parameterTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel);
  }

  private BasicCondition privateNIN(boolean in, String... ids)
  {
    StatementPrimitive[] tempStatementArray = new StatementPrimitive[ids.length];
    for (int i = 0; i < ids.length; i++)
    {
      String oid = ids[i];
      if (oid == null)
      {
        String error = "Parameter may not be null";
        throw new QueryException(error);
      }
      else
      {
        tempStatementArray[i] = this.formatAndValidate(ids[i]);
      }
    }

    if (in)
    {
      return new BasicConditionIn(this, tempStatementArray, false);
    }
    else
    {
      return new BasicConditionNotIn(this, tempStatementArray, false);
    }
  }

  public BasicCondition IN(String... ids)
  {
    return privateNIN(true, ids);
  }

  public BasicCondition NI(String... ids)
  {
    return privateNIN(false, ids);
  }
}
