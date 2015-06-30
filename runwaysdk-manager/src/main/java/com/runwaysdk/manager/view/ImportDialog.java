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
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.runwaysdk.manager.controller.IAdminModuleController;
import com.runwaysdk.manager.general.Localizer;
import com.runwaysdk.manager.model.ImportBean;
import com.runwaysdk.manager.widgets.WidgetVisitor;

public class ImportDialog extends Dialog
{
  private Text                   fileText;

  private ImportBean             bean;

  private IAdminModuleController controller;

  public ImportDialog(Shell parentShell, IAdminModuleController controller)
  {
    super(parentShell);

    this.controller = controller;
    this.bean = new ImportBean();
  }

  @Override
  protected void configureShell(Shell newShell)
  {
    super.configureShell(newShell);

    newShell.setText(Localizer.getMessage("IMPORT_TRANSACTION"));
  }

  @Override
  protected Control createDialogArea(Composite parent)
  {
    Composite composite = (Composite) super.createDialogArea(parent);
    composite.setLayout(new FormLayout());

    Composite fileComposite = new Composite(composite, SWT.BORDER);
    fileComposite.setLayout(new FormLayout());
    fileComposite.setLayoutData(new FormData());

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
    FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
    dialog.setFilterNames(new String[] { "Zip Files", "All Files (*.*)" });
    dialog.setFilterExtensions(new String[] { "*.zip", "*.*" });

    String location = dialog.open();

    if (location != null)
    {
      fileText.setText(location);
    }
  }

  private void bind()
  {
    Realm realm = SWTObservables.getRealm(getShell().getDisplay());
    DataBindingContext bindingContext = new DataBindingContext(realm);

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

        controller.importTransaction(bean);
      }
    });

  }
}
