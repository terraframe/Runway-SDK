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
package com.runwaysdk.manager.view;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.RadioButton;
import org.eclipse.swt.widgets.RadioGroup;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.runwaysdk.manager.controller.IAdminModuleController;
import com.runwaysdk.manager.general.Localizer;
import com.runwaysdk.manager.model.ExportBean;
import com.runwaysdk.manager.model.databinding.RadioGroupObservableValue;
import com.runwaysdk.manager.widgets.WidgetVisitor;

public class ExportDialog extends Dialog
{
  private RadioGroup             group;

  private Composite              range;

  private Text                   fileText;

  private Text                   startText;

  private ExportBean             bean;

  private IAdminModuleController controller;

  public ExportDialog(Shell parentShell, IAdminModuleController controller)
  {
    super(parentShell);

    this.controller = controller;
    this.bean = new ExportBean();
  }

  @Override
  protected void configureShell(Shell newShell)
  {
    super.configureShell(newShell);

    newShell.setText(Localizer.getMessage("EXPORT_TRANSACTION"));
  }

  @Override
  protected Control createDialogArea(Composite parent)
  {
    Composite composite = (Composite) super.createDialogArea(parent);
    composite.setLayout(new FormLayout());

    group = new RadioGroup(composite, SWT.FILL);
    group.setLayout(new FillLayout(SWT.HORIZONTAL));
    group.setLayoutData(new FormData());

    Button allButton = new RadioButton(group, SWT.RADIO, "ALL");
    allButton.setText(Localizer.getMessage("ALL"));
    allButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        enableTextInput(false);
      }
    });

    Button rangeButton = new RadioButton(group, SWT.RADIO, "RANGE");
    rangeButton.setText(Localizer.getMessage("RANGE"));
    rangeButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        enableTextInput(true);
      }
    });

    Button notImportedButton = new RadioButton(group, SWT.RADIO, "NOT_IMPORTED");
    notImportedButton.setText(Localizer.getMessage("NOT_IMPORTED"));
    notImportedButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        enableTextInput(false);
      }
    });

    FormData rangeData = new FormData(SWT.DEFAULT, 20);
    rangeData.top = new FormAttachment(group);

    range = new Composite(composite, SWT.NONE);
    range.setLayout(new FillLayout());
    range.setLayoutData(rangeData);

    Label startLabel = new Label(range, SWT.NONE);
    startLabel.setText(Localizer.getMessage("START"));

    startText = new Text(range, SWT.BORDER);
    startText.setEnabled(false);

    FormData fileCompositeData = new FormData();
    fileCompositeData.top = new FormAttachment(range);

    Composite fileComposite = new Composite(composite, SWT.NONE);
    fileComposite.setLayout(new FormLayout());
    fileComposite.setLayoutData(fileCompositeData);

    fileText = new Text(fileComposite, SWT.BORDER);
    fileText.setLayoutData(new FormData(WidgetVisitor.TEXT_WIDTH, 20));
    fileText.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseDown(MouseEvent e)
      {
        openFileDialog();
      }
    });

    FormData fileButtonData = new FormData();
    fileButtonData.left = new FormAttachment(fileText);

    Button fileButton = new Button(fileComposite, SWT.BORDER);
    fileButton.setLayoutData(fileButtonData);
    fileButton.setText(Localizer.getMessage("SELECT_FILE"));
    fileButton.addListener(SWT.Selection, new Listener()
    {
      @Override
      public void handleEvent(Event event)
      {
        openFileDialog();
      }
    });

    this.bind();

    return composite;
  }

  private void openFileDialog()
  {
    FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
    dialog.setFilterNames(new String[] { "Zip Files", "All Files (*.*)" });
    dialog.setFilterExtensions(new String[] { "*.zip", "*.*" });

    String location = dialog.open();

    if (location != null)
    {
      fileText.setText(location);
    }
  }

  private void enableTextInput(boolean enabled)
  {
    startText.setEnabled(enabled);
  }

  private void bind()
  {
    Realm realm = SWTObservables.getRealm(getShell().getDisplay());
    DataBindingContext bindingContext = new DataBindingContext(realm);

    // Bind the type value
    bindingContext.bindValue(new RadioGroupObservableValue(realm, group), BeansObservables.observeValue(bean, "type"), null, null);

    // Bind the lower value
    bindingContext.bindValue(SWTObservables.observeText(startText, SWT.Modify), BeansObservables.observeValue(bean, "lower"), null, null);

    // Bind the location value
    bindingContext.bindValue(SWTObservables.observeText(fileText, SWT.Modify), BeansObservables.observeValue(bean, "location"), null, null);
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

    Button ok = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    ok.addListener(SWT.Selection, new Listener()
    {

      @Override
      public void handleEvent(Event e)
      {
        bean.validate();

        close();

        controller.exportTransactions(bean);
      }

    });

  }
}
