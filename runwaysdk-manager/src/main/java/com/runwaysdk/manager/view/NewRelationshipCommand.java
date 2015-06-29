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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Shell;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.manager.controller.IComponentListener;
import com.runwaysdk.manager.controller.ViewConfig;
import com.runwaysdk.manager.model.IRelationshipStrategy;
import com.runwaysdk.manager.model.object.BusinessObject;
import com.runwaysdk.manager.model.object.EntityObject;
import com.runwaysdk.manager.model.object.IEntityObject;
import com.runwaysdk.manager.model.object.RelationshipObject;
import com.runwaysdk.manager.widgets.ReferenceDialog;

public class NewRelationshipCommand extends Action implements IComponentListener
{
  private IRelationshipStrategy command;

  private IAdminModule          module;

  private BusinessObject        input;

  private StructuredViewer      viewer;

  private MdRelationshipDAOIF   mdRelationship;

  public NewRelationshipCommand(String text, StructuredViewer viewer, IRelationshipStrategy command, MdRelationshipDAOIF mdRelationship, IAdminModule module)
  {
    super(text);

    this.command = command;
    this.module = module;
    this.viewer = viewer;
    this.mdRelationship = mdRelationship;
  }

  @Override
  public void run()
  {
    Shell shell = viewer.getControl().getShell();

    input = (BusinessObject) viewer.getInput();

    if (input != null)
    {
      MdBusinessDAOIF mdBusiness = command.getInverseType(mdRelationship);
      EntityObject entity = BusinessObject.newInstance(mdBusiness.definesType());

      new ReferenceDialog(shell, entity, module, this).open();
    }
  }

  @Override
  public void componentChanged(ComponentIF component)
  {
    if (input != null)
    {
      BusinessDAO businessDAO = (BusinessDAO) input.getEntityDAO();
      RelationshipDAO relationship = command.create(businessDAO, (BusinessDAOIF) component, mdRelationship.definesType());

      IEntityObject entity = new RelationshipObject(relationship);

      module.edit(entity, new ViewConfig());
    }
  }
}
