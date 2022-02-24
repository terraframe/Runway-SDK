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
package com.runwaysdk.web.applet;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

public class LowerLineBorder extends AbstractBorder
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  protected Color color;

  public LowerLineBorder(Color color)
  {
    this.color = color;
  }

  @Override
  public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
  {
    g.setColor(color);
    g.drawLine(x, (y + height-1), (x + width - 1), (y + height - 1));
  }

  public Insets getBorderInsets(Component c)
  {
    return new Insets(1, 0, 1, 0);
  }

  public boolean isBorderOpaque()
  {
    return false;
  }
}
