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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.io.instance.ImportAction;
import com.runwaysdk.manager.controller.IAdminModuleController;
import com.runwaysdk.manager.controller.ViewConfig;
import com.runwaysdk.manager.general.Localizer;
import com.runwaysdk.manager.model.ChildCommand;
import com.runwaysdk.manager.model.IComponentObject;
import com.runwaysdk.manager.model.ParentCommand;
import com.runwaysdk.manager.model.object.BusinessObject;
import com.runwaysdk.manager.model.object.IEntityObject;
import com.runwaysdk.manager.widgets.EditWidgetVisitor;
import com.runwaysdk.manager.widgets.TabManager;
import com.runwaysdk.manager.widgets.WidgetVisitor;

public class EditView extends EntityView
{
  public static final String     ID = "com.runwaysdk.view.EditView";

  private Composite              pane;

  private ViewConfig             config;

  private IAdminModuleController controller;

  public EditView(IEntityObject entity, IAdminModule module, IAdminModuleController controller, ViewConfig config)
  {
    super(entity, module);

    this.config = config;
    this.controller = controller;

    this.setMessage(config.getMessage());
  }

  @Override
  public Composite getWidget()
  {
    return pane;
  }

  @Override
  public void createPartControl(Composite parent)
  {
    pane = new SashForm(parent, SWT.FILL | SWT.VERTICAL);

    // Add the Entity View
    super.createPartControl(pane);

    // Setup the business relationship information pane
    IComponentObject entity = this.getEntity();

    if (entity instanceof BusinessObject)
    {
      BusinessObject business = (BusinessObject) entity;
      MdEntityDAOIF mdClass = entity.getMdClassDAO();

      // Show each relationship as a different tab in a CTabFolder
      CTabFolder tabFolder = new CTabFolder(pane, SWT.NONE);
      tabFolder.setUnselectedCloseVisible(false);
      tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 2, 1));
      tabFolder.setLayout(new FillLayout());
      tabFolder.setSimple(false);

      MdBusinessDAOIF mdBusiness = (MdBusinessDAOIF) mdClass;

      new RelationshipTabBuilder(business, tabFolder, mdBusiness, getModule()).build(new ParentCommand());
      new RelationshipTabBuilder(business, tabFolder, mdBusiness, getModule()).build(new ChildCommand());

      if (tabFolder.getItemCount() > 0)
      {
        tabFolder.setSelection(0);
      }
    }
  }

  @Override
  protected Composite createPartContent(Composite parent)
  {
    Composite content = super.createPartContent(pane);

    Composite buttons = new Composite(content, SWT.FILL);
    buttons.setLayout(new FormLayout());

    Button apply = null;

    if (config.contains(ImportAction.APPLY))
    {
      SelectionListener applyListener = new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          apply();
        }
      };

      apply = new Button(buttons, SWT.RIGHT);
      apply.setText(Localizer.getMessage("APPLY"));
      apply.addSelectionListener(applyListener);
      apply.setLayoutData(new FormData());
    }

    if (!this.getEntity().isNew() && config.contains(ImportAction.DELETE))
    {
      SelectionListener deleteListener = new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          delete();
        }
      };

      FormData formData = new FormData();

      if (apply != null)
      {
        formData.left = new FormAttachment(apply);
      }

      Button delete = new Button(buttons, SWT.RIGHT);
      delete.setText(Localizer.getMessage("DELETE"));
      delete.addSelectionListener(deleteListener);
      delete.setLayoutData(formData);
    }

    return content;
  }

  public void apply()
  {
    try
    {
      this.getEntity().apply();

      if (config.isCloseOnSucess())
      {
        String key = TabManager.getEditKey(getEntity());

        getModule().closeTab(key);

        controller.resumeImport();
      }
    }
    catch (Throwable t)
    {
      getModule().error(t);
    }
  }

  public void delete()
  {
    Shell shell = getWidget().getShell();

    boolean confirm = MessageDialog.openConfirm(shell, Localizer.getMessage("CONFIRM_TITLE"), Localizer.getMessage("CONFIRM_DELETE"));

    if (confirm)
    {
      try
      {
        this.getEntity().delete();

        String key = TabManager.getEditKey(this.getEntity());

        getModule().closeTab(key);

        if (config.isCloseOnSucess())
        {
          controller.resumeImport();
        }
      }
      catch (Throwable t)
      {
        getModule().error(t);
      }
    }
  }

  @Override
  protected WidgetVisitor getWidgetVisitor(Composite content)
  {
    return new EditWidgetVisitor(content, this.getModule());
  }

  @Override
  public IEntityObject getEntity()
  {
    return (IEntityObject) super.getEntity();
  }
}
