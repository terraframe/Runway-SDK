/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. This file is part of
 * Runway SDK(tm). Runway SDK(tm) is free software: you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. Runway SDK(tm) is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with Runway
 * SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
import com.runwaysdk.query.AttributeReference;
import com.runwaysdk.query.AttributeStruct;
import com.runwaysdk.query.AttributeText;
import com.runwaysdk.query.AttributeTime;
import com.runwaysdk.query.Condition;
import com.runwaysdk.query.EntityQuery;

public class EntityQueryDecorator implements QueryAttributeGetter
{
  private EntityQuery query;

  public EntityQueryDecorator(EntityQuery query)
  {
    this.query = query;
  }

  public AttributeBlob aBlob(String name)
  {
    return query.aBlob(name);
  }

  public AttributeBoolean aBoolean(String name)
  {
    return query.aBoolean(name);
  }

  public AttributeCharacter aCharacter(String name)
  {
    return query.aCharacter(name);
  }

  public AttributeDate aDate(String name)
  {
    return query.aDate(name);
  }

  public AttributeDateTime aDateTime(String name)
  {
    return query.aDateTime(name);
  }

  public AttributeDecimal aDecimal(String name)
  {
    return query.aDecimal(name);
  }

  public AttributeDouble aDouble(String name)
  {
    return query.aDouble(name);
  }

  public AttributeEnumeration aEnumeration(String name)
  {
    return query.aEnumeration(name);
  }

  public AttributeReference aFile(String name)
  {
    return query.aFile(name);
  }

  @Override
  public AttributeMultiReference aMultiReference(String name)
  {
    return query.aMultiReference(name);
  }

  public AttributeFloat aFloat(String name)
  {
    return query.aFloat(name);
  }

  public AttributeInteger aInteger(String name)
  {
    return query.aInteger(name);
  }

  public AttributeLocal aLocalCharacter(String name)
  {
    return query.aLocalCharacter(name);
  }

  public AttributeLocal aLocalText(String name)
  {
    return query.aLocalText(name);
  }

  public AttributeLong aLong(String name)
  {
    return query.aLong(name);
  }

  public AttributeReference aReference(String name)
  {
    return query.aReference(name);
  }

  public AttributeStruct aStruct(String name)
  {
    return query.aStruct(name);
  }

  public AttributeText aText(String name)
  {
    return query.aText(name);
  }

  public AttributeTime aTime(String name)
  {
    return query.aTime(name);
  }

  public void WHERE(Condition condition)
  {
    query.WHERE(condition);
  }

  public EntityQuery getEntityQuery()
  {
    return query;
  }
}
