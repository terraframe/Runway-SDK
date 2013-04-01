/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
 * This file is part of Runway SDK(tm).
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.manager.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.dataaccess.EntityDAOIF;
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
import com.runwaysdk.dataaccess.MdAttributeLocalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalTextDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLongDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdAttributeSymmetricDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTextDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeVirtualDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAOVisitor;
import com.runwaysdk.manager.general.Localizer;
import com.runwaysdk.manager.model.IComponentObject;
import com.runwaysdk.manager.model.object.PersistanceFacade;

public class DetailVisitor implements MdAttributeDAOVisitor
{
  private Composite     parent;

  private IComponentObject entity;

  public DetailVisitor(Composite parent, IComponentObject entity)
  {
    this.parent = parent;
    this.entity = entity;
  }

  @Override
  public void visitBlob(MdAttributeBlobDAOIF attribute)
  {
    // DO NOTHING: THERE IS NO WIDGET FOR BLOBS
  }

  @Override
  public void visitBoolean(MdAttributeBooleanDAOIF attribute)
  {
    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(new FormLayout());

    Label label = new Label(composite, SWT.NONE);
    label.setText(attribute.getDisplayLabel(Localizer.getLocale()));
    label.setSize(200, 20);
    label.setLayoutData(new FormData(200, 20));

    FormData data = new FormData();
    data.left = new FormAttachment(label);

    Label valueLabel = new Label(composite, SWT.BORDER);
    valueLabel.setSize(200, 20);
    valueLabel.setLayoutData(data);

    String value = entity.getValue(attribute.definesAttribute());

    if (value != null && value.equals(MdAttributeBooleanInfo.TRUE))
    {
      valueLabel.setText(attribute.getPositiveDisplayLabel(Localizer.getLocale()));
    }
    else if (value != null)
    {
      valueLabel.setText(attribute.getNegativeDisplayLabel(Localizer.getLocale()));
    }
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
    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(new FormLayout());

    Label label = new Label(composite, SWT.NONE);
    label.setText(attribute.getDisplayLabel(Localizer.getLocale()));
    label.setSize(200, 20);
    label.setLayoutData(new FormData(200, 20));

    FormData data = new FormData();
    data.left = new FormAttachment(label);

//    if (entity instanceof EntityObject)
//    {
//      EntityObject decorator = (EntityObject) entity;
//
//      AttributeEnumerationIF attributeIF = (AttributeEnumerationIF) decorator.getAttribute(attribute.definesAttribute());
//
//      StringBuilder builder = new StringBuilder("[");
//
//      EnumerationItemDAOIF[] items = attributeIF.dereference();
//
//      for (EnumerationItemDAOIF item : items)
//      {
//        builder.append(", " + item.getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, Localizer.DEFAULT_LOCALE));
//      }
//
//      builder.append("]");
//
//      Label valueLabel = new Label(composite, SWT.BORDER);
//      valueLabel.setSize(200, 20);
//      valueLabel.setLayoutData(data);
//      valueLabel.setText(builder.toString().replace(", ", ""));
//    }
  }

  @Override
  public void visitFile(MdAttributeFileDAOIF attribute)
  {
    // DO NOTHING: THERE IS NO WIDGET FOR FILE
  }

  @Override
  public void visitFloat(MdAttributeFloatDAOIF attribute)
  {
    this.visitAttribute(attribute);
  }

  @Override
  public void visitHash(MdAttributeHashDAOIF attribute)
  {
    // DO NOTHING: THERE IS NO WIDGET FOR HASH
  }

  @Override
  public void visitInteger(MdAttributeIntegerDAOIF attribute)
  {
    this.visitAttribute(attribute);
  }

  @Override
  public void visitLocalCharacter(MdAttributeLocalCharacterDAOIF attribute)
  {
    this.visitLocal(attribute);
  }

  @Override
  public void visitLocalText(MdAttributeLocalTextDAOIF attribute)
  {
    this.visitLocal(attribute);
  }

  @Override
  public void visitLong(MdAttributeLongDAOIF attribute)
  {
    this.visitAttribute(attribute);
  }

  @Override
  public void visitReference(MdAttributeReferenceDAOIF attribute)
  {
    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(new FormLayout());

    Label label = new Label(composite, SWT.NONE);
    label.setText(attribute.getDisplayLabel(Localizer.getLocale()));
    label.setSize(200, 20);
    label.setLayoutData(new FormData(200, 20));

    FormData data = new FormData();
    data.left = new FormAttachment(label);

    Label valueLabel = new Label(composite, SWT.BORDER);
    valueLabel.setSize(200, 20);
    valueLabel.setLayoutData(data);

    String value = entity.getValue(attribute.definesAttribute());

    if (value != null && value.length() > 0)
    {
      EntityDAOIF reference = PersistanceFacade.get(value);

      valueLabel.setText(reference.getKey());
    }
  }

  @Override
  public void visitStruct(MdAttributeStructDAOIF attribute)
  {
    // DO NOTHING: THERE IS NO WIDGET FOR STRUCTS
  }

  @Override
  public void visitSymmetric(MdAttributeSymmetricDAOIF attribute)
  {
    // DO NOTHING: THERE IS NO WIDGET FOR SYMMETRIC
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
    // DO NOTHING: THERE IS NO WIDGET FOR VIRTUAL
  }

  private void visitAttribute(MdAttributeDAOIF attribute)
  {
    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(new FormLayout());

    Label label = new Label(composite, SWT.NONE);
    label.setText(attribute.getDisplayLabel(Localizer.getLocale()));
    label.setSize(200, 20);
    label.setLayoutData(new FormData(200, 20));

    FormData data = new FormData();
    data.left = new FormAttachment(label);

    Label valueLabel = new Label(composite, SWT.BORDER);
    valueLabel.setSize(200, 20);
    valueLabel.setLayoutData(data);
    valueLabel.setText(entity.getValue(attribute.definesAttribute()));
  }
  
  private void visitLocal(MdAttributeLocalDAOIF attribute)
  {
    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(new FormLayout());

    Label label = new Label(composite, SWT.NONE);
    label.setText(attribute.getDisplayLabel(Localizer.getLocale()));
    label.setSize(200, 20);
    label.setLayoutData(new FormData(200, 20));

    FormData data = new FormData();
    data.left = new FormAttachment(label);

    Label valueLabel = new Label(composite, SWT.BORDER);
    valueLabel.setSize(200, 20);
    valueLabel.setLayoutData(data);
    valueLabel.setText(entity.getStructValue(attribute.definesAttribute(), Localizer.DEFAULT_LOCALE));
  }

  @Override
  public void visitClob(MdAttributeClobDAOIF attribute)
  {
    this.visitAttribute(attribute);
  }
}
