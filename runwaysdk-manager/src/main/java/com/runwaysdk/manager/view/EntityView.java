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
package com.runwaysdk.manager.view;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.RadioGroup;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.manager.general.Localizer;
import com.runwaysdk.manager.model.IComponentObject;
import com.runwaysdk.manager.model.databinding.IDateObservableValue;
import com.runwaysdk.manager.model.databinding.RadioGroupObservableValue;
import com.runwaysdk.manager.model.databinding.ReferenceObservableValue;
import com.runwaysdk.manager.model.databinding.RunwayObservables;
import com.runwaysdk.manager.model.databinding.StructObservableValue;
import com.runwaysdk.manager.model.object.RelationshipObject;
import com.runwaysdk.manager.widgets.IDateTime;
import com.runwaysdk.manager.widgets.IMessageWidget;
import com.runwaysdk.manager.widgets.ReferenceWidget;
import com.runwaysdk.manager.widgets.ValidateTextWidget;
import com.runwaysdk.manager.widgets.WidgetVisitor;

public abstract class EntityView extends ViewPart implements IViewPart
{
  public static final String            ID = "com.runwaysdk.view.EntityView";

  private IComponentObject              entity;

  private LinkedHashMap<String, Object> controls;

  private DataBindingContext            bindingContext;

  protected ScrolledComposite           outer;

  private Realm                         realm;

  private IAdminModule                  module;

  private String                        message;

  public EntityView(IComponentObject entity, IAdminModule module)
  {
    this.entity = entity;
    this.controls = new LinkedHashMap<String, Object>();
    this.realm = SWTObservables.getRealm(module.getDisplay());
    this.bindingContext = new DataBindingContext(realm);
    this.module = module;
    this.message = null;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage(String message)
  {
    this.message = message;
  }

  public Composite getWidget()
  {
    return outer;
  }

  public IComponentObject getEntity()
  {
    return entity;
  }

  public void setEntity(IComponentObject entity)
  {
    this.entity = entity;

    this.bind();
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

    this.bind();
  }

  protected Composite createPartContent(Composite parent)
  {
    Composite content = new Composite(outer, SWT.FILL);
    content.setLayout(new GridLayout(1, true));

    if (this.message != null)
    {
      Label label = new Label(content, SWT.BORDER);
      label.setText(this.message);
      label.setForeground(new Color(content.getDisplay(), new RGB(184, 58, 57)));
    }

    if (this.entity instanceof RelationshipObject)
    {
      RelationshipObject relationship = (RelationshipObject) entity;

      this.createRelationshipContent(content, Localizer.getMessage("PARENT_ID"), relationship.getParentId());
      this.createRelationshipContent(content, Localizer.getMessage("CHILD_ID"), relationship.getChildId());
    }

    List<MdAttributeDAOIF> attributes = this.entity.definesMdAttributes();

    WidgetVisitor visitor = this.getWidgetVisitor(content);

    for (MdAttributeDAOIF mdAttribute : attributes)
    {
      mdAttribute.accept(visitor);
    }

    this.controls = visitor.getControls();

    return content;
  }

  private void createRelationshipContent(Composite content, String labelText, String value)
  {
    Composite composite = new Composite(content, SWT.NONE);
    composite.setLayout(new FormLayout());

    Label label = new Label(composite, SWT.NONE);
    label.setText(labelText);
    label.setLayoutData(new FormData(200, WidgetVisitor.CHARACTER_HEIGHT));

    FormData data = new FormData(WidgetVisitor.TEXT_WIDTH, WidgetVisitor.CHARACTER_HEIGHT);
    data.left = new FormAttachment(label);

    Text text = new Text(composite, SWT.BORDER | SWT.SINGLE);
    text.setLayoutData(data);
    text.setEnabled(false);
    text.setText(value);
  }

  protected abstract WidgetVisitor getWidgetVisitor(Composite content);

  public IAdminModule getModule()
  {
    return module;
  }

  @Override
  public void setFocus()
  {
    Set<String> keys = this.controls.keySet();

    for (String key : keys)
    {
      Object control = this.controls.get(key);

      if (control instanceof Control)
      {
        ( (Control) control ).setFocus();
      }

      break;
    }
  }

  private final void bind()
  {
    Set<String> keys = controls.keySet();

    for (String key : keys)
    {
      Object control = controls.get(key);

      this.bind(control, key);
    }
  }

  private final void bind(Object control, String attribute)
  {
    IComponentObject modelObject = this.getEntity();

    if (control instanceof Text)
    {
      // The DataBindingContext object will manage the databindings
      IObservableValue uiElement = SWTObservables.observeText((Text) control, SWT.Modify);
      IObservableValue modelElement = RunwayObservables.observeValue(realm, modelObject, attribute);

      bindingContext.bindValue(uiElement, modelElement, null, null);
    }
    else if (control instanceof IDateTime)
    {
      // The DataBindingContext object will manage the databindings
      IObservableValue uiElement = new IDateObservableValue(realm, (IDateTime) control);
      IObservableValue modelElement = RunwayObservables.observeValue(realm, modelObject, attribute);

      // The bindValue method call binds the text element with the model
      bindingContext.bindValue(uiElement, modelElement, null, null);
    }
    else if (control instanceof ValidateTextWidget)
    {
      ValidateTextWidget widget = (ValidateTextWidget) control;

      // The DataBindingContext object will manage the databindings
      IObservableValue uiElement = SWTObservables.observeText(widget.getText(), SWT.Modify);
      IObservableValue modelElement = RunwayObservables.observeValue(realm, modelObject, attribute);

      UpdateValueStrategy strategy = new UpdateValueStrategy();
      strategy.setBeforeSetValidator(widget.getValidator());

      bindingContext.bindValue(uiElement, modelElement, strategy, null);
    }
    else if (control instanceof ReferenceWidget)
    {
      // The DataBindingContext object will manage the databindings
      IObservableValue uiElement = new ReferenceObservableValue(realm, (ReferenceWidget) control);
      IObservableValue modelElement = RunwayObservables.observeValue(realm, modelObject, attribute);

      // The bindValue method call binds the text element with the model
      MessageUpdateStrategy strategy = new MessageUpdateStrategy((IMessageWidget) control);

      bindingContext.bindValue(uiElement, modelElement, strategy, null);
    }
    else if (control instanceof StructView)
    {
      // The DataBindingContext object will manage the databindings
      IObservableValue uiElement = new StructObservableValue(realm, (StructView) control);
      IObservableValue modelElement = RunwayObservables.observeValue(realm, modelObject, attribute);

      // The bindValue method call binds the text element with the model
      bindingContext.bindValue(uiElement, modelElement, null, null);
    }
    else if (control instanceof ComboViewer)
    {
      IObservableValue uiElement = ViewersObservables.observeSingleSelection((ComboViewer) control);
      IObservableValue modelElement = RunwayObservables.observeValue(realm, modelObject, attribute);

      bindingContext.bindValue(uiElement, modelElement, null, null);
    }
    else if (control instanceof RadioGroup)
    {
      // The DataBindingContext object will manage the databindings
      IObservableValue uiElement = new RadioGroupObservableValue(realm, (RadioGroup) control);
      IObservableValue modelElement = RunwayObservables.observeValue(realm, modelObject, attribute);

      // The bindValue method call binds the text element with the model
      bindingContext.bindValue(uiElement, modelElement, null, null);
    }
  }
}
