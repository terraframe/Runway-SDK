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

import java.util.Map;
import java.util.Set;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeRefDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;


public abstract class AttributeRef extends Attribute
{
  protected String          referenceTableName;
  protected String          referenceTableAlias;
  protected MdBusinessDAOIF       referenceMdBusinessIF;

  /**
   * Key: attribute name,  Value: attribute metadata.
   */
  private Map<String, ? extends MdAttributeConcreteDAOIF>   referenceMdAttributeIFMap;

  protected AttributeRef(MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,
      MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    this(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, null);
  }

  protected AttributeRef(MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,
      MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel, MdAttributeStructDAOIF mdAttributeStructIF)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, mdAttributeStructIF);

    this.referenceMdBusinessIF      = referenceMdBusinessIF;
    this.referenceMdAttributeIFMap  = this.referenceMdBusinessIF.getAllDefinedMdAttributeMap();
    this.referenceTableName         = this.referenceMdBusinessIF.getTableName();
    this.referenceTableAlias        = referenceTableAlias;
  }

  /**
   * Compares the id of a component for equality.
   * @param id id of the object to compare.
   * @return Basic Condition object
   */
  public abstract BasicCondition EQ(String id);

  /**
   * Compares the id of a component for equality.
   * @param id id of the object to compare.
   * @return Basic Condition object
   */
  public abstract BasicCondition NE(String id);

  /**
   * Returns an attribute character statement object.
   * @param name name of the attribute.
   * @return Attribute character statement object.
   */
  public abstract AttributeCharacter aCharacter(String name);

  /**
   * Returns an attribute text statement object.
   * @param name name of the attribute.
   * @return Attribute text statement object.
   */
  public abstract AttributeText aText(String name);

  /**
   * Returns an attribute date statement object.
   * @param name name of the attribute.
   * @return Attribute date statement object.
   */
  public abstract AttributeDate aDate(String name);

  /**
   * Returns an attribute time statement object.
   * @param name name of the attribute.
   * @return Attribute time statement object.
   */
  public abstract AttributeTime aTime(String name);

  /**
   * Returns an attribute datetime statement object.
   * @param name name of the attribute.
   * @return Attribute datetime statement object.
   */
  public abstract AttributeDateTime aDateTime(String name);

  /**
   * Returns an attribute integer statement object.
   * @param name name of the attribute.
   * @return Attribute integer statement object.
   */
  public abstract AttributeInteger aInteger(String name);

  /**
   * Returns an attribute long statement object.
   * @param name name of the attribute.
   * @return Attribute long statement object.
   */
  public abstract AttributeLong aLong(String name);

  /**
   * Returns an attribute double statement object.
   * @param name name of the attribute.
   * @return Attribute double statement object.
   */
  public abstract AttributeDouble aDouble(String name);

  /**
   * Returns an attribute decimal statement object.
   * @param name name of the attribute.
   * @return Attribute decimal statement object.
   */
  public abstract AttributeDecimal aDecimal(String name);

  /**
   * Returns an attribute float statement object.
   * @param name name of the attribute.
   * @return Attribute float statement object.
   */
  public abstract AttributeFloat aFloat(String name);

  /**
   * Returns an attribute boolean statement object.
   * @param name name of the attribute.
   * @return Attribute boolean statement object.
   */
  public abstract AttributeBoolean aBoolean(String name);

  /**
   * Returns the MdAttributeIF from the map that defines the attribute with the given name.
   * @param attributeName
   * @return MdAttributeIF
   */
  protected MdAttributeConcreteDAOIF getMdAttributeROfromMap(String attributeName)
  {
    return this.referenceMdAttributeIFMap.get(attributeName.toLowerCase());
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
}
