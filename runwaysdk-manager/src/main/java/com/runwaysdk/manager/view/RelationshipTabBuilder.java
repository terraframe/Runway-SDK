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

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.manager.general.Localizer;
import com.runwaysdk.manager.model.IRelationshipStrategy;
import com.runwaysdk.manager.model.KeyLabelProvider;
import com.runwaysdk.manager.model.RelationshipContentProvider;
import com.runwaysdk.manager.model.object.BusinessObject;

public class RelationshipTabBuilder
{
  private BusinessObject  input;

  private CTabFolder      tabFolder;

  private MdBusinessDAOIF mdBusiness;

  private IAdminModule    module;

  public RelationshipTabBuilder(BusinessObject input, CTabFolder tabFolder, MdBusinessDAOIF mdBusiness, IAdminModule module)
  {
    this.input = input;
    this.tabFolder = tabFolder;
    this.mdBusiness = mdBusiness;
    this.module = module;
  }

  public IAdminModule getModule()
  {
    return module;
  }

  public void build(final IRelationshipStrategy command)
  {
    for (MdRelationshipDAOIF mdRelationship : command.getMdRelationships(mdBusiness))
    {
      final String type = mdRelationship.definesType();

      // Prepare Tree context menu: Edit relationship action

      // Create the CTabItem and the Tree Widget
      CTabItem item = new CTabItem(tabFolder, SWT.NONE);
      item.setText(mdRelationship.getDisplayLabel(Localizer.getLocale()) + " " + command.getTabPostfix());
      item.setShowClose(false);

      final TreeViewer viewer = new TreeViewer(tabFolder);
      viewer.setContentProvider(new RelationshipContentProvider(mdRelationship.definesType(), command));
      viewer.setLabelProvider(new KeyLabelProvider());
      viewer.setInput(input);
      viewer.addDoubleClickListener(new IDoubleClickListener()
      {
        @Override
        public void doubleClick(DoubleClickEvent event)
        {

          new EditAction(Localizer.getMessage("EDIT"), viewer, module).run();
        }
      });

      // Add the Context menu to the Tree Widget
      MenuManager manager = new MenuManager();
      viewer.getControl().setMenu(manager.createContextMenu(viewer.getControl()));

      manager.add(new EditRelationshipAction(Localizer.getMessage("EDIT_RELATIONSHIP"), viewer, command, type, module));
      manager.add(new EditAction(Localizer.getMessage("EDIT"), viewer, module));
      manager.add(new NewRelationshipCommand(Localizer.getMessage("ADD"), viewer, command, mdRelationship, module));

      item.setControl(viewer.getControl());
    }
  }

}
