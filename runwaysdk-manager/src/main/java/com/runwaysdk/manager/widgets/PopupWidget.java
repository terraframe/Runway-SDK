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
package com.runwaysdk.manager.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.manager.controller.IComponentListener;
import com.runwaysdk.manager.model.IComponentObject;
import com.runwaysdk.manager.model.object.EntityObject;
import com.runwaysdk.manager.model.object.SearchObject;

public abstract class PopupWidget extends ViewPart implements IComponentListener, IMessageWidget
{
  private final int        COMPONENT_PROP = 101;

  private Composite        composite;

  private IComponentObject entity;

  private ComponentIF      component;

  private Text             text;

  private Button           button;

  private String           label;

  private Label            message;

  public PopupWidget(IComponentObject entity, String label)
  {
    this.entity = entity;
    this.label = label;
  }

  public abstract void openDialog();

  @Override
  public void createPartControl(Composite parent)
  {
    composite = new Composite(parent, SWT.FILL);
    composite.setLayout(new FormLayout());

    text = new Text(composite, SWT.BORDER);
    text.setEnabled(false);

    button = new Button(composite, SWT.NONE);
    button.setText(label);
    button.addSelectionListener(new SelectionListener()
    {

      @Override
      public void widgetSelected(SelectionEvent e)
      {
        openDialog();
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e)
      {
        openDialog();
      }
    });

    message = new Label(composite, SWT.NONE);
    message.setText("");
    message.setForeground(new Color(composite.getDisplay(), new RGB(184, 58, 57)));

    this.setPartLayout();
  }

  private void setPartLayout()
  {
    FormData textData = new FormData(100, SWT.DEFAULT);

    FormData buttonData = new FormData();
    buttonData.left = new FormAttachment(text);

    FormData messageData = new FormData();
    messageData.left = new FormAttachment(button, 5);
    messageData.right = new FormAttachment(100, -5);
    messageData.top = new FormAttachment(button, 0, SWT.CENTER);

    text.setLayoutData(textData);
    button.setLayoutData(buttonData);
    message.setLayoutData(messageData);
  }

  public void setMessage(String text)
  {
    message.setText(text);
  }

  public void setLayoutData(FormData data)
  {
    composite.setLayoutData(data);
  }

  @Override
  public void setFocus()
  {
    text.setFocus();
  }

  public IComponentObject getEntity()
  {
    return entity;
  }

  public void setComponent(ComponentIF component)
  {
    this.component = component;

    if (component != null)
    {
      text.setText(component.getKey());
      entity.copyAttributes(EntityObject.get(this.component.getOid()));
    }
    else
    {
      text.setText("");
      entity = new SearchObject(entity.getMdClassDAO());
    }

    this.firePropertyChange(COMPONENT_PROP);
  }

  public ComponentIF getComponent()
  {
    return component;
  }

  public Composite getWidget()
  {
    return composite;
  }

  protected Shell getShell()
  {
    return text.getShell();
  }

  @Override
  public void componentChanged(ComponentIF component)
  {
    this.setComponent(component);
  }

  public void setEnabled(boolean enabled)
  {
    button.setEnabled(enabled);
  }
}
