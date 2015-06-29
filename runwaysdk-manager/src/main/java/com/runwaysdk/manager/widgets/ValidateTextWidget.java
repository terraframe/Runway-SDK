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
package com.runwaysdk.manager.widgets;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.runwaysdk.manager.general.Localizer;

public abstract class ValidateTextWidget extends Composite implements IMessageWidget
{
  private Label msg;

  private Text  text;

  public ValidateTextWidget(Composite parent, int style)
  {
    super(parent, style);

    this.setLayout(new FormLayout());

    this.text = new Text(this, SWT.BORDER | SWT.SINGLE);

    this.msg = new Label(this, SWT.NONE);
    this.msg.setText("");
    this.msg.setForeground(new Color(this.getDisplay(), new RGB(184, 58, 57)));
    
    this.setPartLayout();
  }

  private void setPartLayout()
  {
    FormData textData = new FormData(WidgetVisitor.TEXT_WIDTH, 20);
    
    FormData msgData = new FormData();
    msgData.left = new FormAttachment(text, 5);
    msgData.right = new FormAttachment(100, -5);
    msgData.top = new FormAttachment(text, 0, SWT.CENTER);
        
    text.setLayoutData(textData);
    msg.setLayoutData(msgData);
  }
  
  public String getMessage()
  {
    return Localizer.getMessage("INVALID_VALUE");
  }
  
  public abstract void validateValue(String value);  

  public IValidator getValidator()
  {
    IValidator validator = new IValidator()
    {
      @Override
      public IStatus validate(Object value)
      {
        if (value instanceof String)
        {
          String string = (String) value;
          
          try
          {
            validateValue(string);
            
            msg.setText("");
            
            return Status.OK_STATUS;
          }
          catch(Exception e)
          {
            msg.setText(getMessage());

            return ValidationStatus.error(e.getLocalizedMessage());
          }          
        }
        else
        {
          throw new RuntimeException("Not supposed to be called for non-strings.");
        }
      }
    };
    
    return validator;
  }

  public Text getText()
  {
    return this.text;
  }
  
  @Override
  public void setMessage(String text)
  {
    this.msg.setText(text);
  }
}
