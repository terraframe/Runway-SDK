/**
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
 */
package com.runwaysdk.query;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.BusinessQuery;

public interface SelectableReference extends Selectable
{
  /**
   * Returns an attribute character statement object representing the object's id attribute.
   * @param attributeName name of the attribute.
   * @return Attribute character statement object.
   */
  public AttributeCharacter id();


  // Equals
  /**
   * Compares the id of a component for equality.
   * @param id id of the object to compare.
   * @return Basic Condition object
   */
  public BasicCondition EQ(String id);
  
  /**
   * Compares the id to an array of ids for
   * equality.
   * 
   * @param statementArray
   * @return
   */
  public BasicCondition IN(String ... ids);
  
  /**
   * Comopares the id to an array of ids for inequality.
   * 
   * @param ids
   * @return
   */
  public BasicCondition NI(String ... ids);

  /**
   * Character Equals.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition EQ(SelectableChar selectable);

  /**
   * Compares the Component for equality.
   * @param component component to compare.
   * @return Basic Condition object
   */
  public BasicCondition EQ(ComponentIF component);

  /**
   * Compares this AttributeReference with another.
   * @param selectableReference
   * @return Condition object
   */
  public Condition EQ(SelectableReference selectableReference);

  /**
   * Comparess this reference attirbute with objects of the given type.
   * @param businessDAOquery
   * @return Condition object
   */
  public Condition EQ(BusinessDAOQuery businessDAOquery);

  /**
   * Comparess this reference attirbute with objects of the given type.
   * @param businessQuery
   * @return Condition object
   */
  public Condition EQ(BusinessQuery businessQuery);

  /**
   * Comparess this reference attirbute with objects of the given type.
   * @param genBusinessQuery
   * @return Condition object
   */
  public Condition EQ(GeneratedBusinessQuery genBusinessQuery);

  /**
   * Represents a left join between tables in the query.
   *
   * @param selectable
   */
  public LeftJoinEq LEFT_JOIN_EQ(Selectable selectable);

  /**
   * Represents a left join between tables in the query.
   *
   * @param selectableArray
   */
  public LeftJoinEq LEFT_JOIN_EQ(Selectable... selectableArray);

  /**
   * Represents a join between tables in the query.
   *
   * @param entityQuery
   */
  public LeftJoinEq LEFT_JOIN_EQ(EntityQuery entityQuery);

  /**
   * Represents a join between tables in the query.
   *
   * @param componentQuery
   */
  public LeftJoinEq LEFT_JOIN_EQ(GeneratedEntityQuery componentQuery);

  // Not Equals
  /**
   * Compares the id of a component for non equality.
   * @param id id of the object to compare.
   * @return Basic Condition object
   */
  public BasicCondition NE(String id);

  /**
   * Character Not Equals.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition NE(SelectableChar selectable);

  /**
   * Compares this AttributeReference with another.
   * @param selectableReference
   * @return Condition object
   */
  public Condition NE(SelectableReference selectableReference);

  /**
   * Compares the Component for non equality.
   * @param component component to compare.
   * @return Basic Condition object
   */
  public BasicCondition NE(ComponentIF component);

  /**
   * Comparess this reference attirbute with objects of the given type.
   * @param businessDAOquery
   * @return Condition object
   */
  public Condition NE(BusinessDAOQuery businessDAOquery);

  /**
   * Comparess this reference attirbute with objects of the given type.
   * @param businessQuery
   * @return Condition object
   */
  public Condition NE(BusinessQuery businessQuery);

  /**
   * Comparess this reference attirbute with objects of the given type.
   * @param genBusinessQuery
   * @return Condition object
   */
  public Condition NE(GeneratedBusinessQuery genBusinessQuery);

  /**
   * Represents a join between tables in the query.
   *
   * @param selectable
   */
  public LeftJoinNotEq LEFT_JOIN_NE(Selectable selectable);

  /**
   * Represents a join between tables in the query.
   *
   * @param selectableArray
   */
  public LeftJoinNotEq LEFT_JOIN_NE(Selectable... selectableArray);

  /**
   * Represents a join between tables in the query.
   *
   * @param entityQuery
   */
  public LeftJoinNotEq LEFT_JOIN_NE(EntityQuery entityQuery);

  /**
   * Represents a join between tables in the query.
   *
   * @param entityQuery
   */
  public LeftJoinNotEq LEFT_JOIN_NE(GeneratedEntityQuery entityQuery);

  /**
   * Returns an attribute character statement object.
   * @param attributeName name of the attribute.
   * @return Attribute character statement object.
   */
  public AttributeCharacter aCharacter(String attributeName);

  /**
   * Returns an attribute text statement object.
   * @param attributeName name of the attribute.
   * @return Attribute text statement object.
   */
  public AttributeText aText(String attributeName);

  /**
   * Returns an attribute date statement object.
   * @param attributeName name of the attribute.
   * @return Attribute date statement object.
   */
  public AttributeDate aDate(String attributeName);

  /**
   * Returns an attribute time statement object.
   * @param attributeName name of the attribute.
   * @return Attribute time statement object.
   */
  public AttributeTime aTime(String attributeName);

  /**
   * Returns an attribute datetime statement object.
   * @param attributeName name of the attribute.
   * @return Attribute datetime statement object.
   */
  public AttributeDateTime aDateTime(String attributeName);

  /**
   * Returns an attribute integer statement object.
   * @param attributeName name of the attribute.
   * @return Attribute integer statement object.
   */
  public AttributeInteger aInteger(String attributeName);

  /**
   * Returns an attribute long statement object.
   * @param attributeName name of the attribute.
   * @return Attribute long statement object.
   */
  public AttributeLong aLong(String attributeName);

  /**
   * Returns an attribute double statement object.
   * @param attributeName name of the attribute.
   * @return Attribute double statement object.
   */
  public AttributeDouble aDouble(String attributeName);

  /**
   * Returns an attribute decimal statement object.
   * @param attributeName name of the attribute.
   * @return Attribute decimal statement object.
   */
  public AttributeDecimal aDecimal(String attributeName);

  /**
   * Returns an attribute float statement object.
   * @param attributeName name of the attribute.
   * @return Attribute float statement object.
   */
  public AttributeFloat aFloat(String attributeName);

  /**
   * Returns an attribute boolean statement object.
   * @param attributeName name of the attribute.
   * @return Attribute boolean statement object.
   */
  public AttributeBoolean aBoolean(String attributeName);

  /**
   * Returns an attribute blob statement object.
   * @param attributeName name of the attribute.
   * @return Attribute blob statement object.
   */
  public AttributeBlob aBlob(String attributeName);

  /**
   * Returns an attribute struct statement object.
   * @param attributeName name of the attribute.
   * @return Attribute struct statement object.
   */
  public AttributeStruct aStruct(String attributeName);

  /**
   * Returns an attribute reference statement object.
   * @param attributeName name of the attribute.
   * @return Attribute referrence statement object.
   */
  public AttributeReference aReference(String attributeName);

  /**
   * Returns an attribute file statement object.
   * @param attributeName name of the attribute.
   * @return Attribute file statement object.
   */
  public AttributeReference aFile(String attributeName);

  /**
   * Returns an attribute enumeration statement object.
   * @param attributeName name of the attribute.
   * @return Attribute enumeration statement object.
   */
  public AttributeEnumeration aEnumeration(String attributeName);

  /**
   * Restricts the query to include objects that participate as parents in the given relationship type.
   * Instantiate a AbstractRelationshipQuery object of the desired relationship type.
   * @param abstractRelationshipQuery
   * @return Condition restricting objects that participate as parents in the given relationship.
   */
  public Condition isParentIn(AbstractRelationshipQuery abstractRelationshipQuery);

  /**
   * Restricts the query to include objects that participate as parents in the given relationship type.
   * Instantiate a GeneratedRelationshipQuery object of the desired relationship type.
   * @param generatedRelationshipQuery
   * @return Condition restricting objects that participate as parents in the given relationship.
   */
  public Condition isParentIn(GeneratedRelationshipQuery generatedRelationshipQuery);

  /**
   * Restricts the query to include objects that participate as children in the given relationship type.
   * Instantiate a AbstractRelationshipQuery object of the desired relationship type.
   * @param abstractRelationshipQuery
   * @return Condition restricting objects that participate as children in the given relationship.
   */
  public Condition isChildIn(AbstractRelationshipQuery abstractRelationshipQuery);

  /**
   * Restricts the query to include objects that participate as children in the given relationship type.
   * Instantiate a GeneratedRelationshipQuery object of the desired relationship type.
   * @param generatedRelationshipQuery
   * @return Condition restricting objects that participate as children in the given relationship.
   */
  public Condition isChildIn(GeneratedRelationshipQuery generatedRelationshipQuery);
}
