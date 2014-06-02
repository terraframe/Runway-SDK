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
package com.runwaysdk.manager.model;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.graphics.Image;

import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.ComponentDAO;
import com.runwaysdk.manager.general.Localizer;

public class ClassLabelProvider implements ILabelProvider, ISelection
{
  @Override
  public Image getImage(Object element)
  {
    return null;
  }

  @Override
  public String getText(Object element)
  {
    if (element instanceof ComponentDAO)
    {
      AttributeLocalIF attribute = (AttributeLocalIF) ( (ComponentDAO) element ).getAttributeIF(MdTypeInfo.DISPLAY_LABEL);

      return attribute.getValue(Localizer.getLocale());
    }

    return null;
  }

  @Override
  public void addListener(ILabelProviderListener arg0)
  {
  }

  @Override
  public void dispose()
  {
  }

  @Override
  public boolean isLabelProperty(Object arg0, String arg1)
  {
    return false;
  }

  @Override
  public void removeListener(ILabelProviderListener arg0)
  {
  }

  @Override
  public boolean isEmpty()
  {
    // TODO Auto-generated method stub
    return false;
  }
}
