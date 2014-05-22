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

import java.awt.Component;
import java.io.File;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;

public interface DisplayPolicy
{
  /**
   * @return The button responsible for initiating the uploads
   */
  public JButton getUploadButton();
  
  /**
   * @return The button responsible for initiating browsing
   */
  public JButton getBrowseButton();
  
  /**
   * @return The button responsible for initialing a clear all
   */
  public JButton getClearButton();
  
  /**
   * @return The Header panel
   */
  public JPanel getHeaderPanel();
  
  /**
   * @return The main panel
   */
  public JPanel getMainPanel();
  
  /**
   * @return The footer panel
   */
  public JPanel getFooterPanel();
  
  /**
   * @return The sub footer panel
   */
  public JPanel getSubFooterPanel();
  
  /**
   * @param file A file to upload
   * @return The display component associated with a file
   */
  public Component getFileComponent(File file);  
  
  /**
   * Creates a new display component for the given file.
   * 
   * @param file The file to upload
   * @return The button responsible for deleting the file from the upload list
   */
  public JButton createFileComponent(File file);
  
  /**
   * Removes a files display component
   * 
   * @param file
   */
  public void removeFileComponent(File file);

  /**
   * Removes all file display components
   */
  public void clearFileComponents();
  
  /**
   * Sets the text of global upload size label
   * 
   * @param text Text to set
   */
  public void setLabelText(String text);
  
  /**
   * Defines button behavior when the mouse enters a delete button
   * 
   * @param button The delete button
   */
  public void onDeleteButton(JButton button);
  
  /**
   * Defines button behavior when the mouse exits a delete button
   * 
   * @param button The delete button
   */
  public void offDeleteButton(JButton button);

  /**
   * Ready the file display components for displaying uploading information
   *  
   * @param totalBytes Number of bytes to upload globally
   * @param files List of files in which to ready the display components
   */
  public void createUploadingDisplays(long totalBytes, Set<File> files);
  
  /**
   * @return The progress bar displaying the global progress
   */
  public UnlimitedProgressBar getGlobalProgress();

  /**
   * Change the display to it's post upload look
   */
  public void finishedUploading();

  /**
   * Defines button behavior when the mouse enters the upload button
   * 
   * @param button The upload button
   */
  public void onUploadButton(JButton button);

  /**
   * Defines button behavior when the mouse exits the upload button
   * 
   * @param button The upload button
   */
  public void offUploadButton(JButton button);
  
  public void displayError(String txt);
}
