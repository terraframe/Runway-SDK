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
package com.runwaysdk.manager.view;

import java.util.Iterator;

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

public class ColumnRemovableVisitor implements MdAttributeDAOVisitor
{
  private Iterator<MdAttributeDAOIF> it;

  public ColumnRemovableVisitor(Iterator<MdAttributeDAOIF> it)
  {
    this.it = it;
  }

  @Override
  public void visitBlob(MdAttributeBlobDAOIF attribute)
  {
    it.remove();
  }

  @Override
  public void visitBoolean(MdAttributeBooleanDAOIF attribute)
  {
  }

  @Override
  public void visitCharacter(MdAttributeCharacterDAOIF attribute)
  {
  }

  @Override
  public void visitDate(MdAttributeDateDAOIF attribute)
  {
  }

  @Override
  public void visitDateTime(MdAttributeDateTimeDAOIF attribute)
  {
  }

  @Override
  public void visitDecimal(MdAttributeDecimalDAOIF attribute)
  {
  }

  @Override
  public void visitDouble(MdAttributeDoubleDAOIF attribute)
  {
  }

  @Override
  public void visitEnumeration(MdAttributeEnumerationDAOIF attribute)
  {
  }

  @Override
  public void visitFile(MdAttributeFileDAOIF attribute)
  {
    it.remove();
  }

  @Override
  public void visitFloat(MdAttributeFloatDAOIF attribute)
  {
  }

  @Override
  public void visitHash(MdAttributeHashDAOIF attribute)
  {
    it.remove();
  }

  @Override
  public void visitInteger(MdAttributeIntegerDAOIF attribute)
  {
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
  }

  @Override
  public void visitReference(MdAttributeReferenceDAOIF attribute)
  {
  }

  @Override
  public void visitStruct(MdAttributeStructDAOIF attribute)
  {
    it.remove();
  }

  @Override
  public void visitSymmetric(MdAttributeSymmetricDAOIF attribute)
  {
    it.remove();
  }

  @Override
  public void visitText(MdAttributeTextDAOIF attribute)
  {
    it.remove();
  }

  @Override
  public void visitTime(MdAttributeTimeDAOIF attribute)
  {
  }

  @Override
  public void visitVirtual(MdAttributeVirtualDAOIF attribute)
  {
    it.remove();
  }

  @Override
  public void visitClob(MdAttributeClobDAOIF attribute)
  {
    it.remove();
  }

  @Override
  public void visitTerm(MdAttributeTermDAO attribute)
  {
  }

  @Override
  public void visitMultiReference(MdAttributeMultiReferenceDAO attribute)
  {
  }

  @Override
  public void visitMultiTerm(MdAttributeMultiTermDAO attribute)
  {
  }

}
