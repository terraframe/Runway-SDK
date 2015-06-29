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
package com.runwaysdk.web.applet;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

/**
 * Standard display policy for the UploadApplet
 * 
 * @author Justin Smethie
 */
public class StandardPolicy implements DisplayPolicy
{
  /**
   * The upload button
   */
  protected JButton                    uploadButton;

  /**
   * The browse button
   */
  protected JButton                    browseButton;

  /**
   * The clear all button
   */
  protected JButton                    clearButton;

  /**
   * Header panel: The top panel - displays column names
   */
  protected JPanel                     headerPanel;

  /**
   * Status panel: Main panel - displays the status of the upload or information
   * about the file
   */
  protected JPanel                     mainPanel;

  /**
   * Below main panel - Displays buttons for browsing and clearing, and total
   * bytes
   */
  protected JPanel                     footerPanel;

  /**
   * Below button panel - Displays upload button and total progress bar
   */
  protected JPanel                     subFooterPanel;

  /**
   * Label for information on the total upload size
   */
  protected JLabel                     globalSizeLabel;

  /**
   * Progress Bar for display the status of all uploads
   */
  protected UnlimitedProgressBar       globalProgressBar;

  /**
   * A map between a files absolute path and file component
   */
  protected HashMap<String, Component> componentMap;

  /**
   * Text field for displaying error messages
   */
  protected JTextField                 errorText;

  /**
   * Color of the main panel
   */
  protected Color                      mainColor;
  
  /**
   * Color of the foot panel
   */
  protected Color                      footColor;

  /**
   * Mapping of images
   */
  protected HashMap<String, Image>     images;

  private final String               MAIN_IMAGE                = "MAIN_IMAGE";

  private final String               UPLOAD_ON_IMAGE           = "UPLOAD_ON_IMAGE";

  private final String               UPLOAD_OFF_IMAGE          = "UPLOAD_OFF_IMAGE";

  private final String               DELETE_ON_IMAGE           = "DELETE_ON_IMAGE";

  private final String               DELETE_OFF_IMAGE          = "DELETE_OFF_IMAGE";

  private final String               DELETE_MENU_IMAGE         = "DELETE_MENU_IMAGE";

  private final String               HEADER_BACKGROUND_COLOR   = "HEADER_BACKGROUND_COLOR";

  private final String               MAIN_BACKGROUND_COLOR     = "MAIN_BACKGROUND_COLOR";

  private final String               FOOT_BACKGROUND_COLOR     = "FOOT_BACKGROUND_COLOR";

  private final String               SUB_FOOT_BACKGROUND_COLOR = "SUB_FOOT_BACKGROUND_COLOR";

  /**
   * Initializes all of the panels for this display policy and adds them to the
   * main component of the applet. Additionally, all required images of the
   * applet are loaded into a mapping.
   * 
   * @param applet The applet being displayed
   */
  public StandardPolicy(JApplet applet)
  {
    componentMap = new LinkedHashMap<String, Component>();
    images = new HashMap<String, Image>();

    // Load all images from the applet
    this.loadImage(applet, MAIN_IMAGE);
    this.loadImage(applet, UPLOAD_OFF_IMAGE);
    this.loadImage(applet, UPLOAD_ON_IMAGE);
    this.loadImage(applet, DELETE_OFF_IMAGE);
    this.loadImage(applet, DELETE_ON_IMAGE);
    this.loadImage(applet, DELETE_MENU_IMAGE);

    applet.setLayout(new GridBagLayout());
    this.setLookAndFeel(applet);

    /***************************************************************************
     * SETUP HEADER PANEL
     **************************************************************************/
    Color headerBackground = this.loadParameterColor(applet, HEADER_BACKGROUND_COLOR, Color.WHITE);

    // Create the file label
    Font font = new Font("Lucida Sans", Font.PLAIN, 10);
    JTextField fileNameLabel = new JTextField("File Name", 20);
    fileNameLabel.setFont(font);
    fileNameLabel.setEditable(false);
    fileNameLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
    fileNameLabel.setBackground(headerBackground);

    // Create the file size label
    JLabel fileSizeLabel = new JLabel("File Size", JLabel.LEFT);
    fileSizeLabel.setFont(font);

    // Create the delete label
    JLabel deleteLabel = new JLabel();

    if (images.containsKey(DELETE_MENU_IMAGE))
    {
      deleteLabel.setIcon(new ImageIcon(images.get(DELETE_MENU_IMAGE)));
    }
    else
    {
      deleteLabel.setText("Delete");
      deleteLabel.setHorizontalAlignment(JLabel.LEFT);
      deleteLabel.setFont(font);
    }

    // Create the header panel
    GridBagConstraints[] array = this.getHeaderConstrains();

    headerPanel = new JPanel();
    headerPanel.setSize(new Dimension(400, 20));
    headerPanel.setLayout(new GridBagLayout());
    headerPanel.add(fileNameLabel, array[0]);
    headerPanel.add(fileSizeLabel, array[1]);
    headerPanel.add(deleteLabel, array[2]);
    headerPanel.setBackground(headerBackground);
    headerPanel.setBorder(new EmptyBorder(2, 0, 2, 0));

    /***************************************************************************
     * SETUP MAIN PANEL
     **************************************************************************/
    mainColor = this.loadParameterColor(applet, MAIN_BACKGROUND_COLOR, Color.WHITE);
    mainPanel = new ImagePanel(images.get(MAIN_IMAGE));
    mainPanel.setBackground(mainColor);
    mainPanel.setLayout(new GridBagLayout());

    /***************************************************************************
     * SETUP FOOTER PANEL 1
     **************************************************************************/
    footColor = this.loadParameterColor(applet, FOOT_BACKGROUND_COLOR, Color.WHITE);

    this.buildBrowseButton();
    this.buildClearAllButton();
    globalSizeLabel = new JLabel("0 b Total");
    globalSizeLabel.setFont(new Font("Lucida Sans", Font.PLAIN, 10));

    footerPanel = new JPanel();
    footerPanel.setBackground(footColor);
    footerPanel.setLayout(new GridBagLayout());
    footerPanel.setBorder(new EmptyBorder(2, 0, 2, 0));

    GridBagConstraints c = new GridBagConstraints();
    c.anchor = GridBagConstraints.WEST;
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = .25;

    footerPanel.add(browseButton, c);

    c = new GridBagConstraints();
    c.anchor = GridBagConstraints.WEST;
    c.gridx = GridBagConstraints.RELATIVE;
    c.gridy = 0;
    c.weightx = .25;

    footerPanel.add(clearButton, c);

    c = new GridBagConstraints();
    c.anchor = GridBagConstraints.EAST;
    c.gridx = GridBagConstraints.RELATIVE;
    c.gridy = 0;
    c.weightx = .5;
    c.insets = new Insets(0, 0, 0, 5);

    footerPanel.add(globalSizeLabel, c);

    /***************************************************************************
     * SETUP SUB-FOOTER PANEL
     **************************************************************************/
    Color subBackground = this.loadParameterColor(applet, SUB_FOOT_BACKGROUND_COLOR, Color.WHITE);

    this.buildUploadButton();
    this.subFooterPanel = new JPanel();
    this.subFooterPanel.setLayout(new GridBagLayout());
    this.subFooterPanel.setBackground(subBackground);
    this.subFooterPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

    errorText = new JTextField(20);
    errorText.setBackground(subBackground);
    errorText.setBorder(new EmptyBorder(0, 0, 0, 0));

    c = new GridBagConstraints();
    c.anchor = GridBagConstraints.WEST;
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets(0, 2, 0, 0);

    subFooterPanel.add(errorText, c);

    c = new GridBagConstraints();
    c.anchor = GridBagConstraints.EAST;
    c.gridx = 1;
    c.gridy = 0;
    c.weightx = 1;
    c.weighty = 1;
    c.insets = new Insets(0, 0, 0, 5);

    subFooterPanel.add(uploadButton, c);

    /***************************************************************************
     * SETUP ROOT PANEL
     **************************************************************************/
    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 0;
    c.gridheight = 20;

    applet.add(headerPanel, c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = GridBagConstraints.RELATIVE;
    c.weighty = 1;
    c.weightx = 1;

    applet.add(new JScrollPane(mainPanel), c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = GridBagConstraints.RELATIVE;
    c.gridheight = 50;
    applet.add(footerPanel, c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = GridBagConstraints.RELATIVE;
    c.gridheight = 50;
    applet.add(subFooterPanel, c);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.web.applet.DisplayPolicy#getMainPanel()
   */
  public JPanel getMainPanel()
  {
    return mainPanel;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.web.applet.DisplayPolicy#getHeaderPanel()
   */
  public JPanel getHeaderPanel()
  {
    return headerPanel;
  }

  /**
   * @return The GridBagConstraints used in the header and file components
   */
  private GridBagConstraints[] getHeaderConstrains()
  {
    GridBagConstraints[] array = new GridBagConstraints[3];

    array[0] = new GridBagConstraints();
    array[0].gridx = 0;
    array[0].gridy = 0;
    array[0].weightx = .65;
    array[0].insets = new Insets(0, 5, 0, 0);
    array[0].anchor = GridBagConstraints.WEST;

    array[1] = new GridBagConstraints();
    array[1].gridx = GridBagConstraints.RELATIVE;
    array[1].gridy = 0;
    array[1].weightx = .20;

    array[2] = new GridBagConstraints();
    array[2].gridx = GridBagConstraints.RELATIVE;
    array[2].gridy = 0;
    array[2].weightx = .15;
    array[2].anchor = GridBagConstraints.EAST;
    array[2].insets = new Insets(0, 0, 0, 5);

    return array;
  }

  /**
   * Sets the look and feel of this policy
   * 
   * @param root The root component
   */
  private final void setLookAndFeel(Component root)
  {
    try
    {
      UIManager.put("ProgressBar.selectionBackground", Color.black);
      UIManager.put("ProgressBar.selectionForeground", Color.black);
    }
    catch (Exception e)
    {
      // Use the default look and feel
    }
  }

  /**
   * Builds the browse button
   */
  protected void buildBrowseButton()
  {
    browseButton = new JButton();
    browseButton.setText("Browse for More...");
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
    clearButton.setText("Clear All");
    clearButton.setBackground(footColor);
    clearButton.setForeground(Color.BLUE);
    clearButton.setBorder(new EmptyBorder(5, 5, 5, 5));
  }

  /**
   * Builds the upload button
   */
  private void buildUploadButton()
  {
    uploadButton = new JButton();

    if (images.containsKey(UPLOAD_OFF_IMAGE))
    {
      uploadButton.setIcon(new ImageIcon(images.get(UPLOAD_OFF_IMAGE)));
      uploadButton.setBorder(new EmptyBorder(0, 0, 0, 0));
    }
    else
    {
      uploadButton.setText("Upload");
    }
  }

  /**
   * Redraws the main panel: Removes all file components from the main panel and
   * then re adds them.
   */
  protected void redrawMainPanel()
  {
    mainPanel.removeAll();

    // Place all components except the last one
    LinkedList<String> keySet = new LinkedList<String>(componentMap.keySet());

    for (int i = 0; i < keySet.size() - 1; i++)
    {
      String key = keySet.get(i);
      GridBagConstraints c = new GridBagConstraints();
      c.gridx = 0;
      c.gridy = GridBagConstraints.RELATIVE;
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridheight = 20;
      c.weightx = 1;
      c.weighty = 0;
      c.anchor = GridBagConstraints.NORTH;

      mainPanel.add(componentMap.get(key), c);
    }

    // Place the last component
    if (keySet.size() > 0)
    {
      String key = keySet.getLast();
      GridBagConstraints c = new GridBagConstraints();
      c.gridx = 0;
      c.gridy = GridBagConstraints.RELATIVE;
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridheight = 20;
      c.weightx = 1;
      c.weighty = 1;
      c.anchor = GridBagConstraints.NORTH;

      mainPanel.add(componentMap.get(key), c);
    }

    mainPanel.revalidate();
    mainPanel.repaint();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.web.applet.DisplayPolicy#getBrowseButton()
   */
  public JButton getBrowseButton()
  {
    return browseButton;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.web.applet.DisplayPolicy#getClearButton()
   */
  public JButton getClearButton()
  {
    return clearButton;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.web.applet.DisplayPolicy#getFooterPanel()
   */
  public JPanel getFooterPanel()
  {
    return footerPanel;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.web.applet.DisplayPolicy#getSubFooterPanel()
   */
  public JPanel getSubFooterPanel()
  {
    return subFooterPanel;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.web.applet.DisplayPolicy#getUploadButton()
   */
  public JButton getUploadButton()
  {
    return uploadButton;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.web.applet.DisplayPolicy#createFileComponent(java.io.File)
   */
  public JButton createFileComponent(File file)
  {
    final JPanel filePanel = new JPanel();

    Font font = new Font("Lucida Sans", Font.BOLD, 10);

    String fileName = file.getName();

    JTextField fileNameLabel = new JTextField(fileName, 20);
    fileNameLabel.setFont(font);
    fileNameLabel.setEditable(false);
    fileNameLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
    fileNameLabel.setBackground(mainColor);

    // Create the file size label
    JLabel fileSizeLabel = new JLabel(new FileSize(file.length()).toString(), JLabel.LEFT);
    fileSizeLabel.setFont(font);
    fileSizeLabel.setBackground(mainColor);

    // Create the delete label
    final JButton deleteButton = new JButton();
    deleteButton.setBackground(mainColor);

    if (images.containsKey(DELETE_OFF_IMAGE))
    {
      deleteButton.setIcon(new ImageIcon(images.get(DELETE_OFF_IMAGE)));
      deleteButton.setBorder(new EmptyBorder(0, 0, 0, 0));
    }
    else
    {
      deleteButton.setText("Delete");
    }

    // Create the file display panel
    GridBagConstraints[] c = this.getHeaderConstrains();
    filePanel.setSize(new Dimension(400, 20));
    filePanel.setLayout(new GridBagLayout());
    filePanel.add(fileNameLabel, c[0]);
    filePanel.add(fileSizeLabel, c[1]);
    filePanel.add(deleteButton, c[2]);
    filePanel.setBorder(new CompoundBorder(new LowerLineBorder(Color.LIGHT_GRAY), new EmptyBorder(2, 0, 2, 0)));
    filePanel.setBackground(mainColor);

    componentMap.put(file.getAbsolutePath(), filePanel);

    this.redrawMainPanel();

    return deleteButton;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.web.applet.DisplayPolicy#onDeleteButton(javax.swing.JButton)
   */
  public void onDeleteButton(JButton button)
  {
    if (images.containsKey(DELETE_ON_IMAGE))
    {
      button.setIcon(new ImageIcon(images.get(DELETE_ON_IMAGE)));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.web.applet.DisplayPolicy#offDeleteButton(javax.swing.JButton)
   */
  public void offDeleteButton(JButton button)
  {
    if (images.containsKey(DELETE_OFF_IMAGE))
    {
      button.setIcon(new ImageIcon(images.get(DELETE_OFF_IMAGE)));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.web.applet.DisplayPolicy#getFileComponent(java.io.File)
   */
  public Component getFileComponent(File file)
  {
    return componentMap.get(file.getAbsolutePath());
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.web.applet.DisplayPolicy#removeFileComponent(java.io.File)
   */
  public void removeFileComponent(File file)
  {
    componentMap.remove(file.getAbsolutePath());

    this.redrawMainPanel();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.web.applet.DisplayPolicy#clearFileComponents()
   */
  public void clearFileComponents()
  {
    componentMap.clear();

    this.redrawMainPanel();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.web.applet.DisplayPolicy#setLabelText(java.lang.String)
   */
  public void setLabelText(String text)
  {
    globalSizeLabel.setText(text);
    globalSizeLabel.revalidate();
    globalSizeLabel.repaint();

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.web.applet.DisplayPolicy#createUploadingDisplays(long,
   *      java.util.Set)
   */
  public void createUploadingDisplays(long globalBytes, Set<File> files)
  {
    globalProgressBar = new UnlimitedProgressBar();
    globalProgressBar.setValue(0);
    globalProgressBar.setStringPainted(true);
    globalProgressBar.setForeground(new Color(184, 215, 158));
    globalProgressBar.setString(new FileSize(0).toString() + " (0%)");
    globalProgressBar.setMaximum(globalBytes + 1);
    globalProgressBar.setBorder(new EmptyBorder(1, 0, 0, 0));

    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 1;
    c.weighty = 1;
    c.fill = GridBagConstraints.BOTH;

    subFooterPanel.removeAll();
    subFooterPanel.add(globalProgressBar, c);
    subFooterPanel.revalidate();
    subFooterPanel.repaint();

    // Change the display components of all JPanels to FileProgressBar

    for (File file : files)
    {
      Component component = componentMap.get(file.getAbsolutePath());

      if (component instanceof JPanel)
      {
        FileProgressBar progressBar = new FileProgressBar(file);
        progressBar.setValue(0);
        progressBar.setForeground(new Color(173, 202, 226));
        progressBar.setStringPainted(true);
        progressBar.setString(file.getName() + " - " + new FileSize(0).toString() + " (0%)");
        progressBar.setMaximum(file.length() + 1);

        componentMap.remove(file.getAbsolutePath());
        componentMap.put(file.getAbsolutePath(), progressBar);
      }
    }

    // Redraw the status panel with the new ProgressBars
    redrawMainPanel();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.web.applet.DisplayPolicy#getGlobalProgress()
   */
  public UnlimitedProgressBar getGlobalProgress()
  {
    return globalProgressBar;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.web.applet.DisplayPolicy#finishedUploading()
   */
  public void finishedUploading()
  {
    subFooterPanel.removeAll();

    GridBagConstraints c = new GridBagConstraints();
    c.anchor = GridBagConstraints.EAST;
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 1;
    c.weighty = 1;
    c.insets = new Insets(0, 0, 0, 5);

    if (images.containsKey(UPLOAD_OFF_IMAGE))
    {
      uploadButton.setIcon(new ImageIcon(images.get(UPLOAD_OFF_IMAGE)));
      uploadButton.setBorder(new EmptyBorder(0, 0, 0, 0));
    }
    else
    {
      uploadButton.setText("Upload");
    }

    // Redisplay the upload panel
    subFooterPanel.add(uploadButton, c);
    subFooterPanel.revalidate();
    subFooterPanel.repaint();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.web.applet.DisplayPolicy#onUploadButton(javax.swing.JButton)
   */
  public void onUploadButton(JButton button)
  {

    if (images.containsKey(UPLOAD_ON_IMAGE))
    {
      button.setIcon(new ImageIcon(images.get(UPLOAD_ON_IMAGE)));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.web.applet.DisplayPolicy#offUploadButton(javax.swing.JButton)
   */
  public void offUploadButton(JButton button)
  {
    if (images.containsKey(UPLOAD_OFF_IMAGE))
    {
      button.setIcon(new ImageIcon(images.get(UPLOAD_OFF_IMAGE)));
    }
  }

  public void displayError(String txt)
  {
    errorText.setText(txt);
    errorText.revalidate();
    errorText.repaint();
  }

  protected Color loadParameterColor(JApplet applet, String name, Color defaultColor)
  {
    String value = applet.getParameter(name);

    if (value == null)
    {
      return defaultColor;
    }

    return new Color(Integer.parseInt(value));
  }

  /**
   * Loads an image from the application server from which a JApplet originates.  Stores
   * the the image in the image mapping with the parameter name as the key.
   * 
   * @param applet The JApplet
   * @param parameter Name of the parameter which specifies the relative path of the image
   */
  protected void loadImage(JApplet applet, String parameter)
  {
    String codeBase = applet.getCodeBase().toString();
    String path = applet.getParameter(parameter);

    if (path != null)
    {
      try
      {
        URL url = new URL(codeBase + path);
        BufferedImage image = ImageIO.read(url);

        images.put(parameter, image);
      }
      catch (Exception e)
      {
        //Unable to load image do nothing
      }
    }
  }
}
