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

import java.util.Set;

import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeRefDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.MdTableClassIF;

public abstract class GeneratedTableClassQuery extends GeneratedComponentQuery
{
  private TableClassQuery tableClassQuery = null;
  
  public GeneratedTableClassQuery()
  {
    super();
  }
  
  @Override
  public MdTableClassIF getMdClassIF()
  {
    return tableClassQuery.getMdTableClassIF();
  }

  public MdTableClassIF getMdTableClassIF()
  {
    return tableClassQuery.getMdTableClassIF();
  }

  /**
   * Returns {@link TableClassQuery} that all generated query methods delegate to.
   * 
   * @return {@link TableClassQuery} that all generated query methods delegate to.
   */
  @Override
  protected TableClassQuery getComponentQuery()
  {
    return this.tableClassQuery;
  }
  
  /**
   * Returns the table alias used for the table that stores instances of this type.
   * 
   * @return table alias used for the table that stores instances of this type.
   */
  public String getTableAlias()
  {
    return this.getComponentQuery().getTableAlias();
  }
  
  /**
   * Sets the {@link TableClassQuery} that all generated query methods delegate to.
   */
  protected void setComponentQuery(TableClassQuery _tableClassQuery)
  {
    this.tableClassQuery = _tableClassQuery;
  }

  public QueryFactory getQueryFactory()
  {
    return this.getComponentQuery().getQueryFactory();
  }


  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.query.GeneratedComponentQuery#get(java.lang.String, java.lang.String)
   */
  @Override
  public Attribute get(String attributeName, String userDefinedAlias)
  {
    return this.getComponentQuery().get(attributeName, userDefinedAlias);
  }

  public Attribute get(String attributeName)
  {
    return this.getComponentQuery().get(attributeName);
  }


//
//  @Override
//  protected SelectableUUID getOid()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }


  /**
   * Returns an AttributeReference with the given values. Generated subclasses with override this method and return subclasses of AttributeReference.
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
   * @return AttributeReference with the given values. Generated subclasses with override this method and return subclasses of AttributeReference.
   */
  protected AttributeReference referenceFactory(MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return this.getComponentQuery().referenceFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an <code>AttributeStruct</code> with the given values. Generated subclasses with override this method and return subclasses of <code>AttributeStruct</code>.
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
   * @return <code>AttributeStruct</code> with the given values. Generated subclasses with override this method and return subclasses of <code>AttributeStruct</code>.
   */
  protected AttributeStruct structFactory(MdAttributeStructDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, MdStructDAOIF mdStructIF, String structTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return this.getComponentQuery().structFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdStructIF, structTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an <code>AttributeLocal</code> with the given values. Generated subclasses with override this method and return subclasses of <code>AttributeLocal</code>.
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
   * @return <code>AttributeLocal</code> with the given values. Generated subclasses with override this method and return subclasses of <code>AttributeLocal</code>.
   */
  protected AttributeLocal localFactory(MdAttributeLocalDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, MdLocalStructDAOIF mdLocalStructIF, String structTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return this.getComponentQuery().localFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdLocalStructIF, structTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an AttributeEnumeration with the given values. Generated subclasses with override this method and return subclasses of AttributeEnumeration.
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
   * @return AttributeEnumeration with the given values. Generated subclasses with override this method and return subclasses of AttributeEnumeration.
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
