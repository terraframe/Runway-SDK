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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.manager.controller.IComponentListener;
import com.runwaysdk.manager.general.Localizer;
import com.runwaysdk.manager.model.IComponentObject;
import com.runwaysdk.manager.view.IAdminModule;
import com.runwaysdk.manager.view.SelectionView;

public class ReferenceDialog extends Dialog
{
  /**
   * Id of the clear action
   */
  public static int            NO_VALUE_ID = 34304;

  private IComponentObject        entity;

  private IComponentListener[] listeners;

  private IAdminModule         module;

  public ReferenceDialog(Shell parentShell, IComponentObject entity, IAdminModule module, IComponentListener... listeners)
  {
    super(parentShell);

    this.entity = entity;
    this.listeners = listeners;
    this.module = module;

    setShellStyle(getShellStyle() | SWT.RESIZE | SWT.SHELL_TRIM);
  }

  @Override
  protected void configureShell(Shell shell)
  {
    super.configureShell(shell);

    MdEntityDAOIF mdEntity = entity.getMdClassDAO();
    String text = mdEntity.getDisplayLabel(Localizer.getLocale());

    shell.setText(text);
  }

  @Override
  protected Control createDialogArea(Composite parent)
  {
    Composite composite = (Composite) super.createDialogArea(parent);
    composite.setLayout(new FillLayout());

    SelectionView selection = new SelectionView(this, module, listeners);

    selection.createPartControl(composite);

    return composite;
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    createButton(parent, NO_VALUE_ID, Localizer.getMessage("NO_VALUE"), true);
    createButton(parent, IDialogConstants.CANCEL_ID, Localizer.getMessage("CANCEL"), true);
  }

  @Override
  protected void buttonPressed(int buttonId)
  {
    if (buttonId == NO_VALUE_ID)
    {
      for (IComponentListener listener : listeners)
      {
        listener.componentChanged(null);
      }

      this.close();
    }
    else
    {
      super.buttonPressed(buttonId);
    }
  }

  public IComponentObject getEntity()
  {
    return entity;
  }
}
