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

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAOVisitor;
import com.runwaysdk.manager.model.IComponentObject;
import com.runwaysdk.manager.widgets.DetailVisitor;

public class DetailView extends ViewPart implements IViewPart
{
  private IComponentObject       entity;

  protected ScrolledComposite outer;

  public DetailView(IComponentObject entity)
  {
    this.entity = entity;
  }

  public Composite getWidget()
  {
    return outer;
  }

  public IComponentObject getEntity()
  {
    return entity;
  }

  @Override
  public void createPartControl(Composite parent)
  {
    outer = new ScrolledComposite(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

    Composite content = this.createPartContent(outer);

    outer.setContent(content);
    outer.setExpandHorizontal(true);
    outer.setExpandVertical(true);
    outer.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
  }

  protected Composite createPartContent(Composite parent)
  {
    Composite content = new Composite(outer, SWT.NONE);
    content.setLayout(new GridLayout(1, true));

    List<MdAttributeDAOIF> attributes = this.entity.definesMdAttributes();

    MdAttributeDAOVisitor visitor = new DetailVisitor(content, entity);

    for (MdAttributeDAOIF mdAttribute : attributes)
    {
      mdAttribute.accept(visitor);
    }

    return content;
  }

  @Override
  public void setFocus()
  {
    outer.setFocus();
  }
}
