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

import com.runwaysdk.constants.MdAttributeBooleanUtil;
import com.runwaysdk.constants.MdAttributeDateTimeUtil;
import com.runwaysdk.constants.MdAttributeDateUtil;
import com.runwaysdk.constants.MdAttributeDecimalUtil;
import com.runwaysdk.constants.MdAttributeDoubleUtil;
import com.runwaysdk.constants.MdAttributeFloatUtil;
import com.runwaysdk.constants.MdAttributeIntegerUtil;
import com.runwaysdk.constants.MdAttributeLongUtil;
import com.runwaysdk.constants.MdAttributeTimeUtil;
import com.runwaysdk.dataaccess.MdAttributeBlobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeClobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecimalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFileDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFloatDAOIF;
import com.runwaysdk.dataaccess.MdAttributeHashDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalTextDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLongDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdAttributeSymmetricDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTextDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeVirtualDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAOVisitor;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiTermDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTermDAO;
import com.runwaysdk.manager.model.IComponentObject;
import com.runwaysdk.query.AttributeStruct;

public class QueryVisitor implements MdAttributeDAOVisitor
{
  private IComponentObject     entity;

  private QueryAttributeGetter query;

  public QueryVisitor(IComponentObject entity, QueryAttributeGetter query)
  {
    this.entity = entity;
    this.query = query;
  }

  @Override
  public void visitBlob(MdAttributeBlobDAOIF attribute)
  {
  }

  @Override
  public void visitBoolean(MdAttributeBooleanDAOIF attribute)
  {
    String name = attribute.definesAttribute();
    String value = entity.getValue(name);
    if (value != null && value.length() > 0)
    {
      query.WHERE(query.aBoolean(name).EQ(MdAttributeBooleanUtil.getTypeSafeValue(value)));
    }
  }

  @Override
  public void visitCharacter(MdAttributeCharacterDAOIF attribute)
  {
    visitChar(attribute);
  }

  private void visitChar(MdAttributeCharDAOIF attribute)
  {
    String name = attribute.definesAttribute();
    String value = entity.getValue(name);
    if (value != null && value.length() > 0)
    {
      query.WHERE(query.aCharacter(name).EQ(value));
    }
  }

  @Override
  public void visitDate(MdAttributeDateDAOIF attribute)
  {
    String name = attribute.definesAttribute();
    String value = entity.getValue(name);
    if (value != null && value.length() > 0)
    {
      query.WHERE(query.aDate(name).EQ(MdAttributeDateUtil.getTypeSafeValue(value)));
    }
  }

  @Override
  public void visitDateTime(MdAttributeDateTimeDAOIF attribute)
  {
    String name = attribute.definesAttribute();
    String value = entity.getValue(name);
    if (value != null && value.length() > 0)
    {
      query.WHERE(query.aDateTime(name).EQ(MdAttributeDateTimeUtil.getTypeSafeValue(value)));
    }
  }

  @Override
  public void visitDecimal(MdAttributeDecimalDAOIF attribute)
  {
    String name = attribute.definesAttribute();
    String value = entity.getValue(name);
    if (value != null && value.length() > 0)
    {
      query.WHERE(query.aDecimal(name).EQ(MdAttributeDecimalUtil.getTypeSafeValue(value)));
    }
  }

  @Override
  public void visitDouble(MdAttributeDoubleDAOIF attribute)
  {
    String name = attribute.definesAttribute();
    String value = entity.getValue(name);
    if (value != null && value.length() > 0)
    {
      query.WHERE(query.aDouble(name).EQ(MdAttributeDoubleUtil.getTypeSafeValue(value)));
    }
  }

  @Override
  public void visitEnumeration(MdAttributeEnumerationDAOIF attribute)
  {
    String name = attribute.definesAttribute();
    String value = entity.getValue(name);
    if (value != null && value.length() > 0)
    {
      // For now we only support exact matches on single select enums
      query.WHERE(query.aEnumeration(name).containsExactly(value));
    }
  }

  @Override
  public void visitFile(MdAttributeFileDAOIF attribute)
  {
  }

  @Override
  public void visitFloat(MdAttributeFloatDAOIF attribute)
  {
    String name = attribute.definesAttribute();
    String value = entity.getValue(name);
    if (value != null && value.length() > 0)
    {
      query.WHERE(query.aFloat(name).EQ(MdAttributeFloatUtil.getTypeSafeValue(value)));
    }
  }

  @Override
  public void visitHash(MdAttributeHashDAOIF attribute)
  {
  }

  @Override
  public void visitInteger(MdAttributeIntegerDAOIF attribute)
  {
    String name = attribute.definesAttribute();
    String value = entity.getValue(name);
    if (value != null && value.length() > 0)
    {
      query.WHERE(query.aInteger(name).EQ(MdAttributeIntegerUtil.getTypeSafeValue(value)));
    }
  }

  @Override
  public void visitLocalCharacter(MdAttributeLocalCharacterDAOIF attribute)
  {
  }

  @Override
  public void visitLocalText(MdAttributeLocalTextDAOIF attribute)
  {
  }

  @Override
  public void visitLong(MdAttributeLongDAOIF attribute)
  {
    String name = attribute.definesAttribute();
    String value = entity.getValue(name);
    if (value != null && value.length() > 0)
    {
      query.WHERE(query.aLong(name).EQ(MdAttributeLongUtil.getTypeSafeValue(value)));
    }
  }

  @Override
  public void visitReference(MdAttributeReferenceDAOIF attribute)
  {
    String name = attribute.definesAttribute();
    String value = entity.getValue(name);
    if (value != null && value.length() > 0)
    {
      // EQ on AttributeReference is overridden to expect an ID
      query.WHERE(query.aReference(name).EQ(value));
    }
  }

  @Override
  public void visitStruct(MdAttributeStructDAOIF attribute)
  {
    String structName = attribute.definesAttribute();
    AttributeStruct attributeStruct = query.aStruct(structName);
    AttributeStructDecorator decorator = new AttributeStructDecorator(query.getEntityQuery(), attributeStruct);
    QueryVisitor visitor = new QueryVisitor(entity.getStruct(structName), decorator);

    for (MdAttributeConcreteDAOIF c : attribute.getMdStructDAOIF().definesAttributes())
    {
      c.accept(visitor);
    }
  }

  @Override
  public void visitSymmetric(MdAttributeSymmetricDAOIF attribute)
  {
  }

  @Override
  public void visitText(MdAttributeTextDAOIF attribute)
  {
    String name = attribute.definesAttribute();
    String value = entity.getValue(name);

    if (value != null && value.length() > 0)
    {
      query.WHERE(query.aText(name).EQ(value));
    }
  }

  @Override
  public void visitTime(MdAttributeTimeDAOIF attribute)
  {
    String name = attribute.definesAttribute();
    String value = entity.getValue(name);
    if (value != null && value.length() > 0)
    {
      query.WHERE(query.aTime(name).EQ(MdAttributeTimeUtil.getTypeSafeValue(value)));
    }
  }

  @Override
  public void visitVirtual(MdAttributeVirtualDAOIF attribute)
  {
    attribute.getMdAttributeConcrete().accept(this);
  }

  @Override
  public void visitClob(MdAttributeClobDAOIF attribute)
  {
    String name = attribute.definesAttribute();
    String value = entity.getValue(name);

    if (value != null && value.length() > 0)
    {
      query.WHERE(query.aText(name).EQ(value));
    }
  }

  @Override
  public void visitTerm(MdAttributeTermDAO attribute)
  {
    this.visitReference(attribute);
  }

  @Override
  public void visitMultiReference(MdAttributeMultiReferenceDAO attribute)
  {
    String name = attribute.definesAttribute();
    String value = entity.getValue(name);
    if (value != null && value.length() > 0)
    {
      // EQ on AttributeReference is overridden to expect an ID
      query.WHERE(query.aMultiReference(name).containsExactly(value));
    }
  }

  @Override
  public void visitMultiTerm(MdAttributeMultiTermDAO attribute)
  {
    this.visitMultiReference(attribute);
  }

}
