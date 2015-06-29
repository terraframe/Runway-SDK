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

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;

import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.manager.controller.ViewConfig;
import com.runwaysdk.manager.model.AdminEvent;
import com.runwaysdk.manager.model.IAdminEvent;
import com.runwaysdk.manager.model.IRelationshipStrategy;
import com.runwaysdk.manager.model.object.BusinessObject;
import com.runwaysdk.manager.model.object.IEntityObject;
import com.runwaysdk.manager.model.object.RelationshipObject;

public class EditRelationshipAction extends Action implements ISelectionChangedListener
{
  private StructuredViewer      viewer;

  private IAdminModule          module;

  private IRelationshipStrategy strategy;

  private String                relationshipType;

  public EditRelationshipAction(String text, StructuredViewer viewer, IRelationshipStrategy strategy, String relationshipType, IAdminModule module)
  {
    super(text);

    this.viewer = viewer;
    this.module = module;
    this.strategy = strategy;
    this.relationshipType = relationshipType;
  }

  public void run()
  {
    IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

    if (selection.size() != 1)
    {
      return;
    }

    BusinessObject component = (BusinessObject) selection.getFirstElement();

    List<RelationshipDAOIF> parents = strategy.getInverse(component, relationshipType);

    if (parents.size() > 0)
    {
      IEntityObject relationship = new RelationshipObject(parents.get(0));

      IAdminEvent event = new AdminEvent(IAdminEvent.EDIT);
      event.setData(IAdminEvent.OBJECT, relationship);

      module.edit(relationship, new ViewConfig());
    }
  }

  public void selectionChanged(SelectionChangedEvent event)
  {
    this.run();
  }
}
