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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.runwaysdk.manager.model.IComponentObject;
import com.runwaysdk.manager.widgets.StructWidgetVisitor;
import com.runwaysdk.manager.widgets.WidgetVisitor;

public class StructView extends EntityView
{
  private Group group;

  public StructView(IComponentObject entity, IAdminModule module)
  {
    super(entity, module);
  }

  @Override
  public void createPartControl(Composite parent)
  {
    group = new Group(parent, SWT.BORDER | SWT.FILL);
    group.setLayout(new FillLayout());

    super.createPartControl(group);
  }

  public void setLayoutData(FormData data)
  {
    group.setLayoutData(data);
  }

  @Override
  protected WidgetVisitor getWidgetVisitor(Composite parent)
  {
    return new StructWidgetVisitor(parent, this.getModule());
  }
}
