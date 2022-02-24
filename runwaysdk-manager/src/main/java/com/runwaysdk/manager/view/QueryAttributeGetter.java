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
package com.runwaysdk.manager.view;

import com.runwaysdk.query.AttributeBlob;
import com.runwaysdk.query.AttributeBoolean;
import com.runwaysdk.query.AttributeCharacter;
import com.runwaysdk.query.AttributeDate;
import com.runwaysdk.query.AttributeDateTime;
import com.runwaysdk.query.AttributeDecimal;
import com.runwaysdk.query.AttributeDouble;
import com.runwaysdk.query.AttributeEnumeration;
import com.runwaysdk.query.AttributeFloat;
import com.runwaysdk.query.AttributeInteger;
import com.runwaysdk.query.AttributeLocal;
import com.runwaysdk.query.AttributeLong;
import com.runwaysdk.query.AttributeMultiReference;
import com.runwaysdk.query.AttributeRef;
import com.runwaysdk.query.AttributeStruct;
import com.runwaysdk.query.AttributeText;
import com.runwaysdk.query.AttributeTime;
import com.runwaysdk.query.Condition;
import com.runwaysdk.query.EntityQuery;

public interface QueryAttributeGetter
{
  /**
   * Returns an attribute text statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute text statement object.
   */
  public AttributeText aText(String name);

  /**
   * Returns an attribute date statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute date statement object.
   */
  public AttributeDate aDate(String name);

  /**
   * Returns an attribute time statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute time statement object.
   */
  public AttributeTime aTime(String name);

  /**
   * Returns an attribute datetime statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute datetime statement object.
   */
  public AttributeDateTime aDateTime(String name);

  /**
   * Returns an attribute integer statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute integer statement object.
   */
  public AttributeInteger aInteger(String name);

  /**
   * Returns an attribute long statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute long statement object.
   */
  public AttributeLong aLong(String name);

  /**
   * Returns an attribute double statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute double statement object.
   */
  public AttributeDouble aDouble(String name);

  /**
   * Returns an attribute decimal statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute decimal statement object.
   */
  public AttributeDecimal aDecimal(String name);

  /**
   * Returns an attribute float statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute float statement object.
   */
  public AttributeFloat aFloat(String name);

  /**
   * Returns an attribute boolean statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute boolean statement object.
   */
  public AttributeBoolean aBoolean(String name);

  /**
   * Returns an attribute blob statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute blob statement object.
   */
  public AttributeBlob aBlob(String name);

  /**
   * Returns an attribute Struct statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute Struct statement object.
   */
  public AttributeStruct aStruct(String name);

  /**
   * Returns an attribute Local character statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute Local character statement object.
   */
  public AttributeLocal aLocalCharacter(String name);

  /**
   * Returns an attribute Local text statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute Local text statement object.
   */
  public AttributeLocal aLocalText(String name);

  /**
   * Returns an attribute File statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute File statement object.
   */
  public AttributeRef aFile(String name);

  /**
   * Returns an attribute Reference statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute Reference statement object.
   */
  public AttributeRef aReference(String name);

  /**
   * Returns an attribute Enumeration statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute Enumeration statement object.
   */
  public AttributeEnumeration aEnumeration(String name);

  /**
   * Returns an attribute Enumeration statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute Enumeration statement object.
   */
  public AttributeMultiReference aMultiReference(String name);

  public AttributeCharacter aCharacter(String name);

  public void WHERE(Condition condition);

  public EntityQuery getEntityQuery();
}
