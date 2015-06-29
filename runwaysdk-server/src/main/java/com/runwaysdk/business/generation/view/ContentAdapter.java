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
 * Abstract adapter which balk implements {@link ContentListener}. Sub-classes
 * of this adapter only need to overwrite the methods of {@link ContentListener}
 * which they are intrested in.
 * 
 * @author Justin Smethie
 */
public abstract class ContentAdapter extends AbstractViewGenerator implements ContentListener
{
  protected ContentAdapter(MdEntityDAOIF mdEntity, String fileName, String extension)
  {
    super(mdEntity, fileName, extension);
  }

  public void afterAttribute(AttributeEventIF event)
  {
  }

  public void afterAttributes()
  {
  }

  public void afterCloseComponent()
  {
  }

  public void afterCloseForm()
  {
  }

  public void afterComponent()
  {
  }

  public void afterForm()
  {
  }

  public void afterHeader()
  {
  }

  public void attribute(AttributeEventIF event)
  {
  }

  public void beforeAttribute(AttributeEventIF event)
  {
  }

  public void beforeAttributes()
  {
  }

  public void beforeStructAttribute(AttributeEventIF attributeEvent)
  {
  }

  public void afterStructAttribute(AttributeEventIF attributeEvent)
  {
  }

  public void beforeCloseComponent()
  {
  }

  public void beforeCloseForm()
  {
  }

  public void beforeComponent()
  {
  }

  public void beforeForm()
  {
  }

  public void child(RelationshipEventIF event)
  {
  }

  public void closeComponent()
  {
  }

  public void closeForm()
  {
  }

  public void component()
  {

  }

  public void footer()
  {

  }

  public void form()
  {

  }

  public void header()
  {

  }

  public void parent(RelationshipEventIF event)
  {

  }
}
