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

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.internal.databinding.BindingMessages;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.runwaysdk.manager.widgets.IMessageWidget;

public class MessageUpdateStrategy extends UpdateValueStrategy
{
  private IMessageWidget widget;
  
  public MessageUpdateStrategy(IMessageWidget widget)
  {
    this.widget = widget;    
  }
  
  @Override
  protected IStatus doSet(IObservableValue observableValue, Object value)
  {
    try
    {
      observableValue.setValue(value);

      widget.setMessage("");
    }
    catch (Exception ex)
    {
      String msg = ex.getLocalizedMessage();

      widget.setMessage(msg);

      return ValidationStatus.error(BindingMessages.getString(BindingMessages.VALUEBINDING_ERROR_WHILE_SETTING_VALUE), ex);
    }

    return Status.OK_STATUS;
  }
}
