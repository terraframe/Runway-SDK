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

import com.runwaysdk.dataaccess.metadata.ForbiddenMethodException;
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

public class AttributeStructDecorator implements QueryAttributeGetter
{
  private AttributeStruct struct;

  private EntityQuery     query;

  public AttributeStructDecorator(EntityQuery query, AttributeStruct struct)
  {
    this.struct = struct;
    this.query = query;
  }

  public AttributeBlob aBlob(String name)
  {
    return struct.aBlob(name);
  }

  public AttributeBoolean aBoolean(String name)
  {
    return struct.aBoolean(name);
  }

  public AttributeCharacter aCharacter(String name)
  {
    return struct.aCharacter(name);
  }

  public AttributeDate aDate(String name)
  {
    return struct.aDate(name);
  }

  public AttributeDateTime aDateTime(String name)
  {
    return struct.aDateTime(name);
  }

  public AttributeDecimal aDecimal(String name)
  {
    return struct.aDecimal(name);
  }

  public AttributeDouble aDouble(String name)
  {
    return struct.aDouble(name);
  }

  public AttributeEnumeration aEnumeration(String name)
  {
    return struct.aEnumeration(name);
  }

  public AttributeFloat aFloat(String name)
  {
    return struct.aFloat(name);
  }

  public AttributeInteger aInteger(String name)
  {
    return struct.aInteger(name);
  }

  public AttributeLong aLong(String name)
  {
    return struct.aLong(name);
  }

  public AttributeText aText(String name)
  {
    return struct.aText(name);
  }

  public AttributeTime aTime(String name)
  {
    return struct.aTime(name);
  }

  public AttributeRef aFile(String name)
  {
    throw new ForbiddenMethodException("Attribute Structs do not implement this method");
  }

  public AttributeLocal aLocalCharacter(String name)
  {
    throw new ForbiddenMethodException("Attribute Structs do not implement this method");
  }

  public AttributeLocal aLocalText(String name)
  {
    throw new ForbiddenMethodException("Attribute Structs do not implement this method");
  }

  public AttributeRef aReference(String name)
  {
    throw new ForbiddenMethodException("Attribute Structs do not implement this method");
  }

  @Override
  public AttributeMultiReference aMultiReference(String name)
  {
    throw new ForbiddenMethodException("Attribute Structs do not implement this method");
  }

  public AttributeStruct aStruct(String name)
  {
    throw new ForbiddenMethodException("Attribute Structs do not implement this method");
  }

  @Override
  public void WHERE(Condition condition)
  {
    query.WHERE(condition);
  }

  public EntityQuery getEntityQuery()
  {
    return query;
  }

}
