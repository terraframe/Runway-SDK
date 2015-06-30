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
package com.runwaysdk.web.applet;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.util.LinkedList;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class SinglePolicy extends StandardPolicy
{
  public SinglePolicy(JApplet applet)
  {
    super(applet);    
  }

  /**
   * Builds the browse button
   */
  protected void buildBrowseButton()
  {
    browseButton = new JButton();
    browseButton.setText("Browse...");
    browseButton.setBackground(footColor);
    browseButton.setForeground(Color.BLUE);
    browseButton.setBorder(new EmptyBorder(5, 5, 5, 5));
  }

  /**
   * Builds the clear all button
   */
  protected void buildClearAllButton()
  {
    clearButton = new JButton();
    clearButton.setText("Clear");
    clearButton.setBackground(footColor);
    clearButton.setForeground(Color.BLUE);
    clearButton.setBorder(new EmptyBorder(5, 5, 5, 5));
  }
  
  @Override
  protected void redrawMainPanel()
  {
    mainPanel.removeAll();

    // Place all components except the last one
    LinkedList<String> keySet = new LinkedList<String>(componentMap.keySet());

    // Place the last component
    if (keySet.size() > 0)
    {
      String key = keySet.getLast();
      GridBagConstraints c = new GridBagConstraints();
      c.gridx = 0;
      c.gridy = GridBagConstraints.RELATIVE;
      c.fill = GridBagConstraints.BOTH;
      c.gridheight = 20;
      c.weightx = 1;
      c.weighty = 1;
      c.anchor = GridBagConstraints.NORTH;

      mainPanel.add(componentMap.get(key), c);
    }

    mainPanel.revalidate();
    mainPanel.repaint();
  }
}
