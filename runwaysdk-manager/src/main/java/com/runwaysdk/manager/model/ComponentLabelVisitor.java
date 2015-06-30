/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

import com.runwaysdk.ComponentIF;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.AttributeMultiReferenceIF;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.ComponentDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.EnumerationItemDAOIF;
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
import com.runwaysdk.manager.general.Localizer;
import com.runwaysdk.manager.model.object.PersistanceFacade;

public class ComponentLabelVisitor implements MdAttributeDAOVisitor
{
  private ComponentIF component;

  private String      label;

  public ComponentLabelVisitor(ComponentIF component)
  {
    this.component = component;
  }

  public String getLabel()
  {
    return label;
  }

  @Override
  public void visitBlob(MdAttributeBlobDAOIF attribute)
  {
    label = "";
  }

  @Override
  public void visitBoolean(MdAttributeBooleanDAOIF attribute)
  {
    label = "";

    String value = component.getValue(attribute.definesAttribute());

    if (value.length() > 0)
    {
      if (value.equals(MdAttributeBooleanInfo.TRUE))
      {
        label = attribute.getPositiveDisplayLabel(Localizer.getLocale());
      }
      else
      {
        label = attribute.getNegativeDisplayLabel(Localizer.getLocale());
      }
    }
  }

  @Override
  public void visitCharacter(MdAttributeCharacterDAOIF attribute)
  {
    label = component.getValue(attribute.definesAttribute());
  }

  @Override
  public void visitDate(MdAttributeDateDAOIF attribute)
  {
    label = component.getValue(attribute.definesAttribute());
  }

  @Override
  public void visitDateTime(MdAttributeDateTimeDAOIF attribute)
  {
    label = component.getValue(attribute.definesAttribute());
  }

  @Override
  public void visitDecimal(MdAttributeDecimalDAOIF attribute)
  {
    label = component.getValue(attribute.definesAttribute());
  }

  @Override
  public void visitDouble(MdAttributeDoubleDAOIF attribute)
  {
    label = component.getValue(attribute.definesAttribute());
  }

  @Override
  public void visitEnumeration(MdAttributeEnumerationDAOIF attribute)
  {
    label = "";

    if (component instanceof ComponentDAO)
    {
      ComponentDAO componentDAO = (ComponentDAO) component;
      String attributeName = attribute.definesAttribute();

      AttributeEnumerationIF attributeEnumeration = (AttributeEnumerationIF) componentDAO.getAttributeIF(attributeName);

      EnumerationItemDAOIF[] items = attributeEnumeration.dereference();

      StringBuffer buffer = new StringBuffer("[");

      for (EnumerationItemDAOIF item : items)
      {
        if (item != null)
        {
          buffer.append(", " + item.getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, Localizer.DEFAULT_LOCALE));
        }
      }

      buffer.append("]");

      label = buffer.toString().replaceFirst(", ", "");
    }
  }

  @Override
  public void visitFile(MdAttributeFileDAOIF attribute)
  {
    label = "";
  }

  @Override
  public void visitFloat(MdAttributeFloatDAOIF attribute)
  {
    label = component.getValue(attribute.definesAttribute());
  }

  @Override
  public void visitHash(MdAttributeHashDAOIF attribute)
  {
    label = "";
  }

  @Override
  public void visitInteger(MdAttributeIntegerDAOIF attribute)
  {
    label = component.getValue(attribute.definesAttribute());
  }

  @Override
  public void visitLocalCharacter(MdAttributeLocalCharacterDAOIF attribute)
  {
    if (component != null)
    {
      if (component instanceof BusinessDAOIF)
      {
        BusinessDAOIF entity = (BusinessDAOIF) component;

        label = entity.getStructValue(attribute.definesAttribute(), Localizer.DEFAULT_LOCALE);
      }
      else
      {
        label = component.getValue(attribute.definesAttribute());
      }
    }
  }

  @Override
  public void visitLocalText(MdAttributeLocalTextDAOIF attribute)
  {
    if (component != null)
    {
      if (component instanceof BusinessDAOIF)
      {
        BusinessDAOIF entity = (BusinessDAOIF) component;

        label = entity.getStructValue(attribute.definesAttribute(), Localizer.DEFAULT_LOCALE);
      }
      else
      {
        label = component.getValue(attribute.definesAttribute());
      }
    }
  }

  @Override
  public void visitLong(MdAttributeLongDAOIF attribute)
  {
    label = component.getValue(attribute.definesAttribute());
  }

  @Override
  public void visitReference(MdAttributeReferenceDAOIF attribute)
  {
    label = "";

    String id = component.getValue(attribute.definesAttribute());

    if (id != null && id.length() > 0)
    {
      EntityDAOIF entity = PersistanceFacade.get(id);

      label = entity.getKey();
    }
  }

  @Override
  public void visitStruct(MdAttributeStructDAOIF attribute)
  {
    label = "";
  }

  @Override
  public void visitSymmetric(MdAttributeSymmetricDAOIF attribute)
  {
    label = "";
  }

  @Override
  public void visitText(MdAttributeTextDAOIF attribute)
  {
    label = component.getValue(attribute.definesAttribute());
  }

  @Override
  public void visitTime(MdAttributeTimeDAOIF attribute)
  {
    label = component.getValue(attribute.definesAttribute());
  }

  @Override
  public void visitVirtual(MdAttributeVirtualDAOIF attribute)
  {
    label = "";
  }

  @Override
  public void visitClob(MdAttributeClobDAOIF attribute)
  {
    label = component.getValue(attribute.definesAttribute());
  }

  @Override
  public void visitTerm(MdAttributeTermDAO attribute)
  {
    this.visitReference(attribute);
  }

  @Override
  public void visitMultiReference(MdAttributeMultiReferenceDAO attribute)
  {
    label = "";

    if (component instanceof ComponentDAO)
    {
      ComponentDAO componentDAO = (ComponentDAO) component;
      String attributeName = attribute.definesAttribute();

      AttributeMultiReferenceIF attributeMultiReference = (AttributeMultiReferenceIF) componentDAO.getAttributeIF(attributeName);

      BusinessDAOIF[] items = attributeMultiReference.dereference();

      StringBuffer buffer = new StringBuffer("[");

      for (BusinessDAOIF item : items)
      {
        if (item != null)
        {
          buffer.append(", " + item.getKey());
        }
      }

      buffer.append("]");

      label = buffer.toString().replaceFirst(", ", "");
    }
  }

  @Override
  public void visitMultiTerm(MdAttributeMultiTermDAO attribute)
  {
    this.visitMultiReference(attribute);
  }

}
