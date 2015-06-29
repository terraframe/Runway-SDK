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
package com.runwaysdk.manager.model;

import java.util.List;

import com.runwaysdk.dataaccess.EnumerationItemDAOIF;
import com.runwaysdk.dataaccess.MdAttributeBlobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeClobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
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
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAOVisitor;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiTermDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTermDAO;

public class EqualVisitor implements MdAttributeDAOVisitor
{
  private boolean          equals;

  private IComponentObject entity1;

  private IComponentObject entity2;

  public EqualVisitor(IComponentObject object1, IComponentObject object2)
  {
    this.entity1 = object1;
    this.entity2 = object2;

    this.equals = object1.getType().equals(object2.getType());
  }

  public boolean isEqual()
  {
    return this.equals;
  }

  @Override
  public void visitBlob(MdAttributeBlobDAOIF attribute)
  {
    // Do nothing
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
  public void visitDate(MdAttributeDateDAOIF attribute)
  {
    this.visitAttribute(attribute);
  }

  @Override
  public void visitDateTime(MdAttributeDateTimeDAOIF attribute)
  {
    this.visitAttribute(attribute);
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
    String attributeName = attribute.definesAttribute();

    List<EnumerationItemDAOIF> items1 = entity1.getItems(attributeName);
    List<EnumerationItemDAOIF> items2 = entity2.getItems(attributeName);

    if (items1 != null && items2 != null)
    {
      if (! ( items1.containsAll(items2) && items2.containsAll(items1) ))
      {
        equals = false;
      }
    }
    else if (! ( items1 == null && items2 == null ))
    {
      equals = false;
    }
  }

  @Override
  public void visitFile(MdAttributeFileDAOIF attribute)
  {
    // Do not compare
  }

  @Override
  public void visitFloat(MdAttributeFloatDAOIF attribute)
  {
    this.visitAttribute(attribute);
  }

  @Override
  public void visitHash(MdAttributeHashDAOIF attribute)
  {
    // Do not compare
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
    this.visitAttribute(attribute);
  }

  @Override
  public void visitStruct(MdAttributeStructDAOIF attribute)
  {
    String attributeName = attribute.definesAttribute();

    IComponentObject struct1 = entity1.getStruct(attributeName);
    IComponentObject struct2 = entity2.getStruct(attributeName);

    EqualVisitor visitor = new EqualVisitor(struct1, struct2);

    MdStructDAOIF mdStruct = attribute.getMdStructDAOIF();
    List<? extends MdAttributeConcreteDAOIF> attributes = mdStruct.definesAttributes();

    for (MdAttributeDAOIF mdAttribute : attributes)
    {
      mdAttribute.accept(visitor);
    }

    if (!visitor.isEqual())
    {
      equals = false;
    }
  }

  @Override
  public void visitSymmetric(MdAttributeSymmetricDAOIF attribute)
  {
    // Do not compare
  }

  @Override
  public void visitText(MdAttributeTextDAOIF attribute)
  {
    this.visitAttribute(attribute);
  }

  @Override
  public void visitTime(MdAttributeTimeDAOIF attribute)
  {
    this.visitAttribute(attribute);
  }

  @Override
  public void visitVirtual(MdAttributeVirtualDAOIF attribute)
  {
    // Do not compare
  }

  private void visitAttribute(MdAttributeDAOIF attribute)
  {
    String attributeName = attribute.definesAttribute();

    String value1 = entity1.getValue(attributeName);
    String value2 = entity2.getValue(attributeName);

    if (value1 != null && value2 != null)
    {
      if (!value1.equals(value2))
      {
        equals = false;
      }
    }
    else if (! ( value1 == null && value2 == null ))
    {
      equals = false;
    }
  }

  @Override
  public void visitClob(MdAttributeClobDAOIF attribute)
  {
    this.visitAttribute(attribute);
  }

  @Override
  public void visitTerm(MdAttributeTermDAO attribute)
  {
    this.visitReference(attribute);
  }

  @Override
  public void visitMultiReference(MdAttributeMultiReferenceDAO attribute)
  {
    // Do not compare
  }

  @Override
  public void visitMultiTerm(MdAttributeMultiTermDAO attribute)
  {
    // Do not compare
  }

}
