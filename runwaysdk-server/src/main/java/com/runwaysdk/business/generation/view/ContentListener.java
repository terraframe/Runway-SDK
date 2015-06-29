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
package com.runwaysdk.business.generation.view;

import com.runwaysdk.dataaccess.MdEntityDAOIF;

/**
 * Inteface defined to generate custom jsp content based on a series of events.
 * Specific events are defined instead of a generic event to provide developers
 * the highest level of customization. With specific events a developer can
 * simply overwrite the events which they are intered in instead of overwriting
 * the entire implementation.
 * 
 * @author Justin Smethie
 */
public interface ContentListener
{
  /**
   * @return Flag denoting if the generated file already exists on the file
   *         system
   */
  public boolean alreadyExists();

  /**
   * @return The {@link MdEntityDAOIF} which is used to generate the content
   */
  public MdEntityDAOIF getMdEntity();

  /**
   * @return Fully qualified path of the source file generated
   */
  public String getPath();

  /**
   * The header event
   */
  public void header();

  /**
   * After header event
   */
  public void afterHeader();

  /**
   * Before form event
   */
  public void beforeForm();

  /**
   * Form event
   */
  public void form();

  /**
   * After form event
   */
  public void afterForm();

  /**
   * Before component event
   */
  public void beforeComponent();

  /**
   * Component event
   */
  public void component();

  /**
   * After component event
   */
  public void afterComponent();

  /**
   * If the generated objbect is a MdRelationship then the write parent is
   * fired. The write parent is after the form element and is used to for
   * writing commands that retrieve/display the parent
   */
  public void parent(RelationshipEventIF event);

  /**
   * If the generated objbect is a MdRelationship then the write child is fired.
   * The write child is after the form element and is used to for writing
   * commands that retrieve/display the child
   */
  public void child(RelationshipEventIF event);

  /**
   * Before attributes event
   */
  public void beforeAttributes();

  /**
   * Before a single attribute event
   * 
   * @param event
   *          The event containing information about the MdAttribute
   */
  public void beforeAttribute(AttributeEventIF event);

  /**
   * Before a single struct attribute event
   * 
   * @param event
   *          The event containing information about the MdAttribute
   */
  public void beforeStructAttribute(AttributeEventIF event);

  /**
   * A single attribute event
   * 
   * @param attributeEvent
   *          The event containing information about the MdAttribute
   */
  public void attribute(AttributeEventIF event);

  /**
   * After a single struct attribute event
   * 
   * @param event
   *          The event containing information about the MdAttribute
   */
  public void afterStructAttribute(AttributeEventIF event);

  /**
   * Event fired after an individual attribute event has fired.
   * 
   * @param event
   *          The event containing information about the MdAttribute
   */
  public void afterAttribute(AttributeEventIF event);

  /**
   * Event fired after all of the individual attribute events have fired.
   */
  public void afterAttributes();

  /**
   * Event fired before the close component event
   */
  public void beforeCloseComponent();

  /**
   * Close component event
   */
  public void closeComponent();

  /**
   * Event fired immediately after the close component event
   */
  public void afterCloseComponent();

  /**
   * Event fired before the close form event
   */
  public void beforeCloseForm();

  /**
   * Close form event
   */
  public void closeForm();

  /**
   * Event fired immediately after the close form event
   */
  public void afterCloseForm();

  /**
   * Footer event.  The last event to fire.
   */
  public void footer();
}
