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

import com.runwaysdk.dataaccess.MdBusinessDAOIF;

public abstract class AbstractObjectQuery extends EntityQuery
{
  protected AbstractObjectQuery(QueryFactory queryFactory, String type)
  {
    super(queryFactory, type);
    this.init();
  }

  /**
   *
   * @param valueQuery
   * @param type
   */
  protected AbstractObjectQuery(ValueQuery valueQuery, String type)
  {
    super(valueQuery, type);
    this.init();
  }


  private void init()
  {
    if ( !(this.getMdEntityIF() instanceof MdBusinessDAOIF))
    {
      String error = "AbstractObjectQuery can only query for Business object types, not [" + this.getMdEntityIF().definesType() + "]s.";
      throw new QueryException(error);
    }
  }

  /**
   * Returns the MdBusinessIF that defines the type of objects that
   * are queried from this object.
   *
   * @return MdBusinessIF that defines the type of objects that
   * are queried from this object.
   */
  protected MdBusinessDAOIF getMdBusinessIF()
  {
    return (MdBusinessDAOIF)this.getMdEntityIF();
  }

  /**
   * Restricts the query to include objects that participate as parents in the given relationship type.
   * Does a subselect if a <code>ValueQuery</code> is involved, otherwise not. 
   * Instantiate a AbstractRelationshipQuery object of the desired relationship type.
   * @param abstractRelationshipQuery
   * @return Condition restricting objects that participate as parents in the given relationship.
   */
  public Condition isParentIn(AbstractRelationshipQuery abstractRelationshipQuery)
  {
    if (this.isUsedInValueQuery() || abstractRelationshipQuery.isUsedInValueQuery())
    {
      return new ValueJoinConditionEq((AttributeUUID)this.oid(), abstractRelationshipQuery.parentOid());
    }
    else
    {
      return new SubSelectBasicConditionEq((AttributeUUID)this.oid(), abstractRelationshipQuery.parentOid());
    }
  }

  /**
   * Restricts the query to include objects that participate as parents in the given relationship type.  
   * Does a subselect if a <code>ValueQuery</code> is involved, otherwise not. 
   * Instantiate a GeneratedRelationshipQuery object of the desired relationship type.
   * @param generatedRelationshipQuery
   * @return Condition restricting objects that participate as parents in the given relationship.
   */
  public Condition isParentIn(GeneratedRelationshipQuery generatedRelationshipQuery)
  {
    if (this.isUsedInValueQuery() || generatedRelationshipQuery.getComponentQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionEq((AttributeUUID)this.oid(), generatedRelationshipQuery.parentOid());
    }
    else
    {
      return new SubSelectBasicConditionEq((AttributeUUID)this.oid(), generatedRelationshipQuery.parentOid());
    }
  }

  /**
   * Restricts the query to include objects that participate as parents in the given relationship type.
   * Instantiate a AbstractRelationshipQuery object of the desired relationship type.
   * @param abstractRelationshipQuery
   * @return Condition restricting objects that participate as parents in the given relationship.
   */
  public Condition isParentIn_SUBSELECT(AbstractRelationshipQuery abstractRelationshipQuery)
  {
    return new SubSelectBasicConditionEq((AttributeUUID)this.oid(), abstractRelationshipQuery.parentOid());
  }

  /**
   * Restricts the query to include objects that participate as parents in the given relationship type.
   * Instantiate a GeneratedRelationshipQuery object of the desired relationship type.
   * @param generatedRelationshipQuery
   * @return Condition restricting objects that participate as parents in the given relationship.
   */
  public Condition isParentIn_SUBSELECT(GeneratedRelationshipQuery generatedRelationshipQuery)
  {
    return new SubSelectBasicConditionEq((AttributeUUID)this.oid(), generatedRelationshipQuery.parentOid());
  }

  
  /**
   * Restricts the query to include objects that do not participate as parents in the given relationship type.
   * Does a subselect if a <code>ValueQuery</code> is involved, otherwise not. 
   * Instantiate a AbstractRelationshipQuery object of the desired relationship type.
   * @param abstractRelationshipQuery
   * @return Condition restricting objects that do not participate as parents in the given relationship.
   */
  public Condition isNotParentIn(AbstractRelationshipQuery abstractRelationshipQuery)
  {
    if (this.isUsedInValueQuery() || abstractRelationshipQuery.isUsedInValueQuery())
    {
      return new ValueJoinConditionNotEq((AttributeUUID)this.oid(), abstractRelationshipQuery.parentOid());
    }
    else
    {
      return new SubSelectBasicConditionNotEq((AttributeUUID)this.oid(), abstractRelationshipQuery.parentOid());
    }
  }

  /**
   * Restricts the query to include objects that do not participate as parents in the given relationship type.
   * Does a subselect if a <code>ValueQuery</code> is involved, otherwise not. 
   * Instantiate a GeneratedRelationshipQuery object of the desired relationship type.
   * @param generatedRelationshipQuery
   * @return Condition restricting objects that do not participate as parents in the given relationship.
   */
  public Condition isNotParentIn(GeneratedRelationshipQuery generatedRelationshipQuery)
  {
    if (this.isUsedInValueQuery() || generatedRelationshipQuery.getComponentQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionNotEq((AttributeUUID)this.oid(), generatedRelationshipQuery.parentOid());
    }
    else
    {
      return new SubSelectBasicConditionNotEq((AttributeUUID)this.oid(), generatedRelationshipQuery.parentOid());
    }
  }

  /**
   * Restricts the query to include objects that do not participate as parents in the given relationship type.
   * Instantiate a AbstractRelationshipQuery object of the desired relationship type.
   * @param abstractRelationshipQuery
   * @return Condition restricting objects that do not participate as parents in the given relationship.
   */
  public Condition isNotParentIn_SUBSELECT(AbstractRelationshipQuery abstractRelationshipQuery)
  {
    return new SubSelectBasicConditionNotEq((AttributeUUID)this.oid(), abstractRelationshipQuery.parentOid());
  }

  /**
   * Restricts the query to include objects that do not participate as parents in the given relationship type.
   * Instantiate a GeneratedRelationshipQuery object of the desired relationship type.
   * @param generatedRelationshipQuery
   * @return Condition restricting objects that do not participate as parents in the given relationship.
   */
  public Condition isNotParentIn_SUBSELECT(GeneratedRelationshipQuery generatedRelationshipQuery)
  {
    return new SubSelectBasicConditionNotEq((AttributeUUID)this.oid(), generatedRelationshipQuery.parentOid());
  }
  
  /**
   * Restricts the query to include objects that participate as children in the given relationship type.
   * Does a subselect if a <code>ValueQuery</code> is involved, otherwise not. 
   * Instantiate a AbstractRelationshipQuery object of the desired relationship type.
   * @param abstractRelationshipQuery
   * @return Condition restricting objects that participate as children in the given relationship.
   */
  public Condition isChildIn(AbstractRelationshipQuery abstractRelationshipQuery)
  {
    if (this.isUsedInValueQuery() || abstractRelationshipQuery.isUsedInValueQuery())
    {
      return new ValueJoinConditionEq((AttributeUUID)this.oid(), abstractRelationshipQuery.childOid(), false);
    }
    else
    {
      return new SubSelectBasicConditionEq((AttributeUUID)this.oid(), abstractRelationshipQuery.childOid());
    }
  }

  /**
   * Restricts the query to include objects that participate as children in the given relationship type.
   * Does a subselect if a <code>ValueQuery</code> is involved, otherwise not. 
   * Instantiate a GeneratedRelationshipQuery object of the desired relationship type.
   * @param generatedRelationshipQuery
   * @return Condition restricting objects that participate as children in the given relationship.
   */
  public Condition isChildIn(GeneratedRelationshipQuery generatedRelationshipQuery)
  {
    if (this.isUsedInValueQuery() || generatedRelationshipQuery.getComponentQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionEq((AttributeUUID)this.oid(), generatedRelationshipQuery.childOid());
    }
    else
    {
      return new SubSelectBasicConditionEq((AttributeUUID)this.oid(), generatedRelationshipQuery.childOid());
    }
  }

  /**
   * Restricts the query to include objects that participate as children in the given relationship type.
   * Instantiate a AbstractRelationshipQuery object of the desired relationship type.
   * @param abstractRelationshipQuery
   * @return Condition restricting objects that participate as children in the given relationship.
   */
  public Condition isChildIn_SUBSELECT(AbstractRelationshipQuery abstractRelationshipQuery)
  {
    return new SubSelectBasicConditionEq((AttributeUUID)this.oid(), abstractRelationshipQuery.childOid());
  }

  /**
   * Restricts the query to include objects that participate as children in the given relationship type.
   * Instantiate a GeneratedRelationshipQuery object of the desired relationship type.
   * @param generatedRelationshipQuery
   * @return Condition restricting objects that participate as children in the given relationship.
   */
  public Condition isChildIn_SUBSELECT(GeneratedRelationshipQuery generatedRelationshipQuery)
  {
    return new SubSelectBasicConditionEq((AttributeUUID)this.oid(), generatedRelationshipQuery.childOid());
  }
  
  /**
   * Restricts the query to include objects that do not participate as children in the given relationship type.
   * Does a subselect if a <code>ValueQuery</code> is involved, otherwise not. 
   * Instantiate a AbstractRelationshipQuery object of the desired relationship type.
   * @param abstractRelationshipQuery
   * @return Condition restricting objects that do not participate as children in the given relationship.
   */
  public Condition isNotChildIn(AbstractRelationshipQuery abstractRelationshipQuery)
  {
    if (this.isUsedInValueQuery() || abstractRelationshipQuery.isUsedInValueQuery())
    {
      return new ValueJoinConditionNotEq((AttributeUUID)this.oid(), abstractRelationshipQuery.childOid());
    }
    else
    {
      return new SubSelectBasicConditionNotEq((AttributeUUID)this.oid(), abstractRelationshipQuery.childOid());
    }
  }

  /**
   * Restricts the query to include objects that do not participate as children in the given relationship type.
   * Does a subselect if a <code>ValueQuery</code> is involved, otherwise not. 
   * Instantiate a GeneratedRelationshipQuery object of the desired relationship type.
   * @param generatedRelationshipQuery
   * @return Condition restricting objects that do not participate as children in the given relationship.
   */
  public Condition isNotChildIn(GeneratedRelationshipQuery generatedRelationshipQuery)
  {
    if (this.isUsedInValueQuery() || generatedRelationshipQuery.getComponentQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionNotEq((AttributeUUID)this.oid(), generatedRelationshipQuery.childOid());
    }
    else
    {
      return new SubSelectBasicConditionNotEq((AttributeUUID)this.oid(), generatedRelationshipQuery.childOid());
    }
  }

  
  /**
   * Restricts the query to include objects that do not participate as children in the given relationship type.
   * Instantiate a AbstractRelationshipQuery object of the desired relationship type.
   * @param abstractRelationshipQuery
   * @return Condition restricting objects that do not participate as children in the given relationship.
   */
  public Condition isNotChildIn_SUBSELECT(AbstractRelationshipQuery abstractRelationshipQuery)
  {
    return new SubSelectBasicConditionNotEq((AttributeUUID)this.oid(), abstractRelationshipQuery.childOid());
  }

  /**
   * Restricts the query to include objects that do not participate as children in the given relationship type.
   * Instantiate a GeneratedRelationshipQuery object of the desired relationship type.
   * @param generatedRelationshipQuery
   * @return Condition restricting objects that do not participate as children in the given relationship.
   */
  public Condition isNotChildIn_SUBSELECT(GeneratedRelationshipQuery generatedRelationshipQuery)
  {
    return new SubSelectBasicConditionNotEq((AttributeUUID)this.oid(), generatedRelationshipQuery.childOid());
  }
}
