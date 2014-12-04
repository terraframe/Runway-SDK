/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.query;

import java.util.Set;

import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeRefDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;

/**
 * All generated query classes extend this class. It delegates to a type unsafe
 * EntityQuery to hide all of the type unsafe query methods.
 * 
 * @author nathan
 * 
 */
public abstract class GeneratedEntityQuery extends GeneratedComponentQuery
{
  private EntityQuery entityQuery = null;

  /**
   *
   */
  protected GeneratedEntityQuery()
  {
    super();
  }

  /**
   * Returns the MdEntityIF of components that are queried by this object.
   * 
   * @return MdEntityIF of components that are queried by this object.
   */
  public MdEntityDAOIF getMdClassIF()
  {
    return entityQuery.getMdEntityIF();
  }

  public QueryFactory getQueryFactory()
  {
    return entityQuery.getQueryFactory();
  }

  public Attribute get(String attributeName)
  {
    return this.getComponentQuery().get(attributeName);
  }

  public Attribute id()
  {
    return this.getComponentQuery().id();
  }

  /**
   * Compares this AttributeReference with another.
   * 
   * @param attributeIF
   * @return Condition object
   */
  public Condition EQ(SelectableReference _attributeIF)
  {
    AttributeCharacter idCharacter = this.getComponentQuery().id();
    
    MdAttributeCharacterDAOIF mdAttributeCharacterDAOIF = (MdAttributeCharacterDAOIF)idCharacter.getMdAttributeIF();
    MdAttributeReferenceDAOIF mdAttributeReferenceDAOIF = mdAttributeCharacterDAOIF.convertToReference();
    
    AttributeReference idReference = (AttributeReference) 
      this.getComponentQuery().internalAttributeFactory(mdAttributeCharacterDAOIF.definesAttribute(), mdAttributeReferenceDAOIF, this, idCharacter.getUserDefinedAlias(), idCharacter.getUserDefinedDisplayLabel());
    
    return idReference.EQ(_attributeIF);
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
   * @param selectable
   */
  public LeftJoinGtEq LEFT_JOIN_GE(SelectableSingle selectable)
  {
    return new LeftJoinGtEq(this.id(), selectable);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param selectableArray
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
   * Returns ComponentQuery that all generated query methods delegate to.
   * 
   * @return ComponentQuery that all generated query methods delegate to.
   */
  protected EntityQuery getComponentQuery()
  {
    return this.entityQuery;
  }

  /**
   * Throws <code>QueryException</code> if this <code>EntityQuery</code> is used
   * in a <code>ValueQuery</code>.
   */
  protected void checkNotUsedInValueQuery()
  {
    this.getComponentQuery().checkNotUsedInValueQuery();
  }

  /**
   * Sets the EntityQuery that all generated query methods delegate to.
   */
  protected void setComponentQuery(EntityQuery entityQuery)
  {
    this.entityQuery = entityQuery;
  }

  /**
   * Returns the table alias used for the table that stores instances of this
   * type.
   * 
   * @return table alias used for the table that stores instances of this type.
   */
  public String getTableAlias()
  {
    return this.entityQuery.getTableAlias();
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
   * @param userDefinedDisplayLabel
   * @return AttributeReference with the given values. Generated subclasses with
   *         override this method and return subclasses of AttributeReference.
   */
  protected AttributeReference referenceFactory(MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return this.getComponentQuery().referenceFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
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
  protected AttributeStruct structFactory(MdAttributeStructDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, MdStructDAOIF mdStructIF, String structTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return this.getComponentQuery().structFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdStructIF, structTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
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
    return this.getComponentQuery().localFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdLocalStructIF, structTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
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
  protected AttributeEnumeration enumerationFactory(MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdEnumerationTableName, MdBusinessDAOIF masterListMdBusinessIF, String masterListTalbeAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return this.getComponentQuery().enumerationFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * @param mdAttributeMultiReferenceIF
   * @param definesType
   * @param definingTableName
   * @param definingTableAlias
   * @param attributeTableName
   * @param referenceMdBusinessIF
   * @param referenceTableAlias
   * @param rootQuery
   * @param tableJoinSet
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return
   */
  protected Attribute multiReferenceFactory(MdAttributeMultiReferenceDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String attributeTableName, MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return this.getComponentQuery().multiReferenceFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, attributeTableName, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
  }

}
