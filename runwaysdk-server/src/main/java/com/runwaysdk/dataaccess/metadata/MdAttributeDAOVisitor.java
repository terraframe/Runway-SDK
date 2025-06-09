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
package com.runwaysdk.dataaccess.metadata;

import com.runwaysdk.dataaccess.MdAttributeBlobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeClobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecimalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFileDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFloatDAOIF;
import com.runwaysdk.dataaccess.MdAttributeHashDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF;
import com.runwaysdk.dataaccess.MdAttributeJsonDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalTextDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLongDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdAttributeSymmetricDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTextDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeVirtualDAOIF;

public interface MdAttributeDAOVisitor
{
  public void visitBlob(MdAttributeBlobDAOIF attribute);

  public void visitHash(MdAttributeHashDAOIF attribute);

  public void visitSymmetric(MdAttributeSymmetricDAOIF attribute);

  public void visitEnumeration(MdAttributeEnumerationDAOIF attribute);

  public void visitFile(MdAttributeFileDAOIF attribute);

  public void visitBoolean(MdAttributeBooleanDAOIF attribute);

  public void visitCharacter(MdAttributeCharacterDAOIF attribute);

  public void visitDate(MdAttributeDateDAOIF attribute);

  public void visitDateTime(MdAttributeDateTimeDAOIF attribute);

  public void visitTime(MdAttributeTimeDAOIF attribute);

  public void visitDecimal(MdAttributeDecimalDAOIF attribute);

  public void visitDouble(MdAttributeDoubleDAOIF attribute);

  public void visitFloat(MdAttributeFloatDAOIF attribute);

  public void visitInteger(MdAttributeIntegerDAOIF attribute);

  public void visitLong(MdAttributeLongDAOIF attribute);

  public void visitText(MdAttributeTextDAOIF attribute);

  public void visitJson(MdAttributeJsonDAOIF attribute);
  
  public void visitClob(MdAttributeClobDAOIF attribute);

  public void visitReference(MdAttributeReferenceDAOIF attribute);

  public void visitStruct(MdAttributeStructDAOIF attribute);

  public void visitLocalCharacter(MdAttributeLocalCharacterDAOIF attribute);

  public void visitLocalText(MdAttributeLocalTextDAOIF attribute);

  public void visitVirtual(MdAttributeVirtualDAOIF attribute);

  public void visitTerm(MdAttributeTermDAO attribute);

  public void visitMultiReference(MdAttributeMultiReferenceDAO attribute);

  public void visitMultiTerm(MdAttributeMultiTermDAO attribute);

  public void visitUUID(MdAttributeUUIDDAO attribute);
}
