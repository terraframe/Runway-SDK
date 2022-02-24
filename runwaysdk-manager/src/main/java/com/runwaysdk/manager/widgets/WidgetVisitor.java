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
package com.runwaysdk.manager.widgets;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.RadioButton;
import org.eclipse.swt.widgets.RadioGroup;
import org.eclipse.swt.widgets.Text;

import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdAttributeBlobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeClobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecimalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEncryptionDAOIF;
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
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAOVisitor;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiTermDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTermDAO;
import com.runwaysdk.manager.general.Localizer;
import com.runwaysdk.manager.model.object.SearchObject;
import com.runwaysdk.manager.view.IAdminModule;
import com.runwaysdk.manager.view.LabelValuePair;
import com.runwaysdk.manager.view.StructView;

public class WidgetVisitor implements MdAttributeDAOVisitor
{
  public static final int               TEXT_WIDTH       = 525;

  public static final int               TEXT_HEIGHT      = 100;

  public static final int               CHARACTER_HEIGHT = 20;

  private Composite                     parent;

  private LinkedHashMap<String, Object> controls;

  private IAdminModule                  module;

  public WidgetVisitor(Composite parent, IAdminModule module)
  {
    this.parent = parent;
    this.controls = new LinkedHashMap<String, Object>();
    this.module = module;
  }

  public LinkedHashMap<String, Object> getControls()
  {
    return controls;
  }

  @Override
  public void visitBlob(MdAttributeBlobDAOIF attribute)
  {
    // DO NOTHING: THERE IS NO WIDGET FOR BLOBS
  }

  @Override
  public void visitBoolean(MdAttributeBooleanDAOIF attribute)
  {
    if (validateAttribute(attribute))
    {
      boolean enabled = this.isEnabled(attribute);

      Composite composite = new Composite(parent, SWT.NONE);
      composite.setLayout(new FormLayout());

      Label label = new Label(composite, SWT.NONE);
      label.setText(attribute.getDisplayLabel(Localizer.getLocale()));
      label.setSize(200, 20);
      label.setLayoutData(new FormData(200, 20));

      FormData data = new FormData();
      data.left = new FormAttachment(label);

      RadioGroup group = new RadioGroup(composite, SWT.FILL);
      group.setLayout(new FillLayout());
      group.setLayoutData(data);

      Button trueButton = new RadioButton(group, SWT.RADIO, MdAttributeBooleanInfo.TRUE);
      trueButton.setText(attribute.getPositiveDisplayLabel(Localizer.getLocale()));
      trueButton.setEnabled(enabled);

      Button falseButton = new RadioButton(group, SWT.RADIO, MdAttributeBooleanInfo.FALSE);
      falseButton.setText(attribute.getNegativeDisplayLabel(Localizer.getLocale()));
      falseButton.setEnabled(enabled);

      if (!attribute.isRequired())
      {
        Button noValue = new RadioButton(group, SWT.RADIO, "");
        noValue.setText(Localizer.getMessage("NO_VALE"));
        noValue.setEnabled(enabled);
      }

      this.controls.put(attribute.definesAttribute(), group);
    }
  }

  @Override
  public void visitCharacter(MdAttributeCharacterDAOIF attribute)
  {
    this.visitAttribute(attribute);
  }

  @Override
  public void visitClob(MdAttributeClobDAOIF attribute)
  {
    if (validateAttribute(attribute))
    {
      boolean enabled = this.isEnabled(attribute);

      Composite composite = new Composite(parent, SWT.NONE);
      composite.setLayout(new FormLayout());

      Label label = new Label(composite, SWT.NONE);
      label.setText(attribute.getDisplayLabel(Localizer.getLocale()));
      label.setLayoutData(new FormData(200, 20));

      FormData data = new FormData(TEXT_WIDTH, TEXT_HEIGHT);
      data.left = new FormAttachment(label);

      Text text = new Text(composite, SWT.BORDER | SWT.MULTI);
      text.setLayoutData(data);
      text.setEnabled(enabled);

      this.controls.put(attribute.definesAttribute(), text);
    }
  }

  @Override
  public void visitDate(MdAttributeDateDAOIF attribute)
  {
    if (validateAttribute(attribute))
    {
      boolean enabled = this.isEnabled(attribute);

      Composite composite = new Composite(parent, SWT.NONE);
      composite.setLayout(new FormLayout());

      Label label = new Label(composite, SWT.NONE);
      label.setText(attribute.getDisplayLabel(Localizer.getLocale()));
      label.setSize(200, 20);
      label.setLayoutData(new FormData(200, 20));

      FormData data = new FormData();
      data.left = new FormAttachment(label);

      CDateTime dateTime = new CDateTime(composite, SWT.DATE, false);
      dateTime.setLayoutData(data);
      dateTime.setEnabled(enabled);

      this.controls.put(attribute.definesAttribute(), dateTime);
    }
  }

  @Override
  public void visitDateTime(MdAttributeDateTimeDAOIF attribute)
  {
    if (validateAttribute(attribute))
    {
      boolean enabled = this.isEnabled(attribute);

      Composite composite = new Composite(parent, SWT.NONE);
      composite.setLayout(new FormLayout());

      Label label = new Label(composite, SWT.NONE);
      label.setText(attribute.getDisplayLabel(Localizer.getLocale()));
      label.setSize(200, 20);
      label.setLayoutData(new FormData(200, 20));

      FormData data = new FormData();
      data.left = new FormAttachment(label);

      DateAndTime dateTime = new DateAndTime(composite, SWT.FILL | SWT.BORDER, false);
      dateTime.setLayoutData(data);
      dateTime.setEnabled(enabled);

      this.controls.put(attribute.definesAttribute(), dateTime);
    }
  }

  @Override
  public void visitDecimal(MdAttributeDecimalDAOIF attribute)
  {
    this.visitValidateDouble(attribute);
  }

  @Override
  public void visitDouble(MdAttributeDoubleDAOIF attribute)
  {
    this.visitValidateDouble(attribute);
  }

  @Override
  public void visitEnumeration(MdAttributeEnumerationDAOIF attribute)
  {
    if (validateAttribute(attribute))
    {
      boolean enabled = this.isEnabled(attribute);

      Composite composite = new Composite(parent, SWT.NONE);
      composite.setLayout(new FormLayout());

      Label label = new Label(composite, SWT.NONE);
      label.setText(attribute.getDisplayLabel(Localizer.getLocale()));
      label.setSize(200, 20);
      label.setLayoutData(new FormData(200, 20));

      FormData data = new FormData();
      data.left = new FormAttachment(label);

      MdEnumerationDAOIF mdEnumeration = attribute.getMdEnumerationDAO();

      List<BusinessDAOIF> items = mdEnumeration.getAllEnumItems();
      List<LabelValuePair> list = new LinkedList<LabelValuePair>();

      for (BusinessDAOIF item : items)
      {
        String itemLabel = item.getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, Localizer.DEFAULT_LOCALE);
        String oid = item.getOid();

        list.add(new LabelValuePair(itemLabel, oid));
      }

      final ComboViewer combo = new ComboViewer(composite, SWT.READ_ONLY);
      combo.setContentProvider(new ArrayContentProvider());
      combo.setLabelProvider(new LabelProvider());
      combo.setInput(list.toArray(new LabelValuePair[list.size()]));
      combo.getControl().setLayoutData(data);
      combo.getControl().setEnabled(enabled);

      this.controls.put(attribute.definesAttribute(), combo);
    }
  }

  @Override
  public void visitFile(MdAttributeFileDAOIF attribute)
  {
    // DO NOTHING: THERE IS NO WIDGET FOR FILE
  }

  @Override
  public void visitFloat(MdAttributeFloatDAOIF attribute)
  {
    this.visitValidateDouble(attribute);
  }

  @Override
  public void visitHash(MdAttributeHashDAOIF attribute)
  {
    this.visitEncryption(attribute);
  }

  @Override
  public void visitInteger(MdAttributeIntegerDAOIF attribute)
  {
    this.visitValidateLong(attribute);
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
    this.visitValidateLong(attribute);
  }

  @Override
  public void visitReference(MdAttributeReferenceDAOIF attribute)
  {
    if (validateAttribute(attribute))
    {
      boolean enabled = this.isEnabled(attribute);

      Composite composite = new Composite(parent, SWT.NONE);
      composite.setLayout(new FormLayout());

      Label label = new Label(composite, SWT.NONE);
      label.setText(attribute.getDisplayLabel(Localizer.getLocale()));
      label.setSize(200, 20);
      label.setLayoutData(new FormData(200, 20));

      FormData data = new FormData(600, 30);
      data.left = new FormAttachment(label);

      PopupWidget view = new ReferenceWidget(SearchObject.newInstance(attribute.getReferenceMdBusinessDAO()), module);
      view.createPartControl(composite);
      view.setLayoutData(data);
      view.setEnabled(enabled);

      this.controls.put(attribute.definesAttribute(), view);
    }
  }

  @Override
  public void visitStruct(MdAttributeStructDAOIF attribute)
  {
    if (validateAttribute(attribute))
    {
      Composite composite = new Composite(parent, SWT.NONE);
      composite.setLayout(new FormLayout());

      Label label = new Label(composite, SWT.NONE);
      label.setText(attribute.getDisplayLabel(Localizer.getLocale()));
      label.setSize(200, 20);
      label.setLayoutData(new FormData(200, 20));

      FormData data = new FormData();
      data.left = new FormAttachment(label);

      StructView view = new StructView(SearchObject.newInstance(attribute.getMdStructDAOIF()), module);
      view.createPartControl(composite);
      view.setLayoutData(data);

      this.controls.put(attribute.definesAttribute(), view);
    }
  }

  @Override
  public void visitSymmetric(MdAttributeSymmetricDAOIF attribute)
  {
    this.visitEncryption(attribute);
  }

  @Override
  public void visitText(MdAttributeTextDAOIF attribute)
  {
    if (validateAttribute(attribute))
    {
      boolean enabled = this.isEnabled(attribute);

      Composite composite = new Composite(parent, SWT.NONE);
      composite.setLayout(new FormLayout());

      Label label = new Label(composite, SWT.NONE);
      label.setText(attribute.getDisplayLabel(Localizer.getLocale()));
      label.setLayoutData(new FormData(200, 20));

      FormData data = new FormData(TEXT_WIDTH, TEXT_HEIGHT);
      data.left = new FormAttachment(label);

      Text text = new Text(composite, SWT.BORDER | SWT.MULTI);
      text.setLayoutData(data);
      text.setEnabled(enabled);

      this.controls.put(attribute.definesAttribute(), text);
    }
  }

  @Override
  public void visitTime(MdAttributeTimeDAOIF attribute)
  {
    if (validateAttribute(attribute))
    {
      boolean enabled = this.isEnabled(attribute);

      Composite composite = new Composite(parent, SWT.NONE);
      composite.setLayout(new FormLayout());

      Label label = new Label(composite, SWT.NONE);
      label.setText(attribute.getDisplayLabel(Localizer.getLocale()));
      label.setSize(200, 20);
      label.setLayoutData(new FormData(200, 20));

      FormData data = new FormData();
      data.left = new FormAttachment(label);

      CDateTime dateTime = new CDateTime(composite, SWT.TIME, false);
      dateTime.setLayoutData(data);
      dateTime.setEnabled(enabled);

      this.controls.put(attribute.definesAttribute(), dateTime);
    }
  }

  @Override
  public void visitVirtual(MdAttributeVirtualDAOIF attribute)
  {
    // DO NOTHING: THERE IS NO WIDGET FOR VIRTUAL
  }

  private void visitAttribute(MdAttributeDAOIF attribute)
  {
    if (validateAttribute(attribute))
    {
      boolean enabled = this.isEnabled(attribute);

      Composite composite = new Composite(parent, SWT.NONE);
      composite.setLayout(new FormLayout());

      Label label = new Label(composite, SWT.NONE);
      label.setText(attribute.getDisplayLabel(Localizer.getLocale()));
      label.setLayoutData(new FormData(200, CHARACTER_HEIGHT));

      FormData data = new FormData(TEXT_WIDTH, CHARACTER_HEIGHT);
      data.left = new FormAttachment(label);

      Text text = new Text(composite, SWT.BORDER | SWT.SINGLE);
      text.setLayoutData(data);
      text.setEnabled(enabled);

      this.controls.put(attribute.definesAttribute(), text);
    }
  }

  private void visitValidateLong(MdAttributeDAOIF attribute)
  {
    if (validateAttribute(attribute))
    {
      boolean enabled = this.isEnabled(attribute);

      Composite composite = new Composite(parent, SWT.NONE);
      composite.setLayout(new FormLayout());

      Label label = new Label(composite, SWT.NONE);
      label.setText(attribute.getDisplayLabel(Localizer.getLocale()));
      label.setLayoutData(new FormData(200, 20));

      FormData data = new FormData( ( TEXT_WIDTH + 10 ) * 2, 30);
      data.left = new FormAttachment(label);
      // data.right = new FormAttachment(100, 0);

      LongWidget widget = new LongWidget(composite, SWT.NONE);
      widget.setLayoutData(data);
      widget.setEnabled(enabled);

      this.controls.put(attribute.definesAttribute(), widget);
    }
  }

  private void visitValidateDouble(MdAttributeDAOIF attribute)
  {
    if (validateAttribute(attribute))
    {
      boolean enabled = this.isEnabled(attribute);

      Composite composite = new Composite(parent, SWT.NONE);
      composite.setLayout(new FormLayout());

      Label label = new Label(composite, SWT.NONE);
      label.setText(attribute.getDisplayLabel(Localizer.getLocale()));
      label.setLayoutData(new FormData(200, 20));

      FormData data = new FormData( ( TEXT_WIDTH + 10 ) * 2, 30);
      data.left = new FormAttachment(label);
      // data.right = new FormAttachment(100, 0);

      DoubleWidget widget = new DoubleWidget(composite, SWT.NONE);
      widget.setLayoutData(data);
      widget.setEnabled(enabled);

      this.controls.put(attribute.definesAttribute(), widget);
    }
  }

  private void visitEncryption(MdAttributeEncryptionDAOIF attribute)
  {
    if (validateAttribute(attribute))
    {
      boolean enabled = this.isEnabled(attribute);

      Composite composite = new Composite(parent, SWT.NONE);
      composite.setLayout(new FormLayout());

      Label label = new Label(composite, SWT.NONE);
      label.setText(attribute.getDisplayLabel(Localizer.getLocale()));
      label.setLayoutData(new FormData(200, 20));

      FormData data = new FormData(TEXT_WIDTH, 20);
      data.left = new FormAttachment(label);

      Text text = new Text(composite, SWT.BORDER | SWT.SINGLE | SWT.PASSWORD);
      text.setLayoutData(data);
      text.setEnabled(enabled);

      this.controls.put(attribute.definesAttribute(), text);
    }
  }

  protected boolean validateAttribute(MdAttributeDAOIF attribute)
  {
    return true;
  }

  protected boolean isEnabled(MdAttributeDAOIF attribute)
  {
    return true;
  }

  @Override
  public void visitTerm(MdAttributeTermDAO attribute)
  {
    this.visitReference(attribute);
  }

  @Override
  public void visitMultiReference(MdAttributeMultiReferenceDAO attribute)
  {
    // DO NOTHING: THERE IS NO WIDGET FOR MULTI REFERENCES
  }

  @Override
  public void visitMultiTerm(MdAttributeMultiTermDAO attribute)
  {
    // DO NOTHING: THERE IS NO WIDGET FOR MULTI TERMS
  }

}
