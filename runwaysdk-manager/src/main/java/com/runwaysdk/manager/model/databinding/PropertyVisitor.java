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
package com.runwaysdk.manager.model.databinding;

import java.util.Date;

import org.eclipse.core.databinding.property.value.SimpleValueProperty;

import com.runwaysdk.constants.MdAttributeDateTimeUtil;
import com.runwaysdk.constants.MdAttributeDateUtil;
import com.runwaysdk.constants.MdAttributeTimeUtil;
import com.runwaysdk.dataaccess.MdAttributeBlobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeClobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
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

public class PropertyVisitor implements MdAttributeDAOVisitor
{
  private String              propertyName;

  private SimpleValueProperty property;

  public PropertyVisitor(String propertyName)
  {
    this.propertyName = propertyName;
  }

  public SimpleValueProperty getProperty()
  {
    return this.property;
  }

  @Override
  public void visitBlob(MdAttributeBlobDAOIF attribute)
  {
  }

  @Override
  public void visitBoolean(MdAttributeBooleanDAOIF attribute)
  {
    this.visitAttribute(attribute);
  }

  @Override
  public void visitCharacter(MdAttributeCharacterDAOIF attribute)
  {
    this.visitAttribute(attribute);
  }

  @Override
  public void visitClob(MdAttributeClobDAOIF attribute)
  {
    this.visitAttribute(attribute);
  }

  @Override
  public void visitDate(MdAttributeDateDAOIF attribute)
  {
    this.property = new DateTimeValueProperty(propertyName, new IDateConversionCommand()
    {
      @Override
      public String convert(Date date)
      {
        return MdAttributeDateUtil.getTypeUnsafeValue(date);
      }

      @Override
      public Date convert(String date)
      {
        return MdAttributeDateUtil.getTypeSafeValue(date);
      }
    });
  }

  @Override
  public void visitDateTime(MdAttributeDateTimeDAOIF attribute)
  {
    this.property = new DateTimeValueProperty(propertyName, new IDateConversionCommand()
    {
      @Override
      public String convert(Date date)
      {
        return MdAttributeDateTimeUtil.getTypeUnsafeValue(date);
      }

      @Override
      public Date convert(String date)
      {
        return MdAttributeDateTimeUtil.getTypeSafeValue(date);
      }
    });
  }

  @Override
  public void visitDecimal(MdAttributeDecimalDAOIF attribute)
  {
    this.visitAttribute(attribute);
  }

  @Override
  public void visitDouble(MdAttributeDoubleDAOIF attribute)
  {
    this.visitAttribute(attribute);
  }

  @Override
  public void visitEnumeration(MdAttributeEnumerationDAOIF attribute)
  {
    this.property = new SingleValueProperty(propertyName);
  }

  @Override
  public void visitFile(MdAttributeFileDAOIF attribute)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void visitFloat(MdAttributeFloatDAOIF attribute)
  {
    this.visitAttribute(attribute);
  }

  @Override
  public void visitHash(MdAttributeHashDAOIF attribute)
  {
    this.property = new EncryptionValueProperty(propertyName);
  }

  @Override
  public void visitInteger(MdAttributeIntegerDAOIF attribute)
  {
    this.visitAttribute(attribute);
  }

  @Override
  public void visitLocalCharacter(MdAttributeLocalCharacterDAOIF attribute)
  {
    this.visitStruct(attribute);
  }

  @Override
  public void visitLocalText(MdAttributeLocalTextDAOIF attribute)
  {
    this.visitStruct(attribute);
  }

  @Override
  public void visitLong(MdAttributeLongDAOIF attribute)
  {
    this.visitAttribute(attribute);
  }

  @Override
  public void visitReference(MdAttributeReferenceDAOIF attribute)
  {
    this.property = new ComponentValueProperty(propertyName);
  }

  @Override
  public void visitStruct(MdAttributeStructDAOIF attribute)
  {
    this.property = new StructValueProperty(propertyName);
  }

  @Override
  public void visitSymmetric(MdAttributeSymmetricDAOIF attribute)
  {
    this.property = new EncryptionValueProperty(propertyName);
  }

  @Override
  public void visitText(MdAttributeTextDAOIF attribute)
  {
    this.visitAttribute(attribute);
  }

  @Override
  public void visitTime(MdAttributeTimeDAOIF attribute)
  {
    this.property = new DateTimeValueProperty(propertyName, new IDateConversionCommand()
    {
      @Override
      public String convert(Date date)
      {
        return MdAttributeTimeUtil.getTypeUnsafeValue(date);
      }

      @Override
      public Date convert(String date)
      {
        return MdAttributeTimeUtil.getTypeSafeValue(date);
      }
    });
  }

  @Override
  public void visitVirtual(MdAttributeVirtualDAOIF attribute)
  {
    // TODO Auto-generated method stub

  }

  private void visitAttribute(MdAttributeDAOIF attribute)
  {
    this.property = new TextValueProperty(propertyName);
  }

  @Override
  public void visitTerm(MdAttributeTermDAO attribute)
  {
    this.visitReference(attribute);
  }

  @Override
  public void visitMultiReference(MdAttributeMultiReferenceDAO attribute)
  {
    this.property = new SingleValueProperty(propertyName);
  }

  @Override
  public void visitMultiTerm(MdAttributeMultiTermDAO attribute)
  {
    this.visitMultiReference(attribute);
  }
}
