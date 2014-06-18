/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.web.applet;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel
{
  /**
   * 
   */
  private static final long serialVersionUID = 4265590540592599752L;

  private Image image;
  
  public ImagePanel(File image)
  {
    try
    {
      this.image = ImageIO.read(image);
    }
    catch (Exception e)
    {
      this.image = null;
    }
  } 
  
  public ImagePanel(Image image)
  {
    this.image = image;
  }
  
  @Override
  protected void paintComponent(Graphics g)
  {
    //Set the background
    g.setColor(this.getBackground());
    g.fillRect(0, 0, this.getWidth(), this.getHeight());
    g.setColor(this.getForeground());
    
    if(image != null)
    {
      Point location = this.getDrawLocation();
      
      g.drawImage(image, location.x, location.y, null);
    }
  }
  
  private Point getDrawLocation()
  {
    //Determine the bounds of the image
    int width = image.getWidth(null);
    int height = image.getHeight(null);

    if(width == -1 || height == -1)
    {
      //Image bounds unable to be determined:
      //Drawing image in top left corner of panel
      return new Point(0,0);
    }
    else
    {
      //Center image in the background of the panel
      Point panelCenter = new Point(this.getWidth()/2, this.getHeight()/2);
      Point imageCenter = new Point(width/2, height/2);
      return new Point(panelCenter.x-imageCenter.x, panelCenter.y-imageCenter.y);
    }
  }
}
