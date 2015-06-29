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

import javax.servlet.http.HttpServletRequest;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.GeneratedActions;
import com.runwaysdk.constants.MdActionInfo;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.session.Session;

/**
 * {@link ContentListener} used to generate 'newRelationshipComponent.jsp',
 * which is responsible for setting the parent/child attributes on a new
 * relationship.
 * 
 * @author Justin Smethie
 */
public class NewRelationshipComponentListener extends ContentAdapter
{
  /**
   * Name of the {@link HttpServletRequest} attribute containing the list of
   * parents
   */
  public static final String PARENT_ATTRIBUTE = "parentList";

  /**
   * Name of the {@link HttpServletRequest} attribute containing the list of
   * children
   */
  public static final String CHILD_ATTRIBUTE  = "childList";

  public NewRelationshipComponentListener(MdRelationshipDAOIF mdRelationship)
  {
    this(mdRelationship, "newRelationshipComponent", "jsp");
  }
  
  public NewRelationshipComponentListener(MdEntityDAOIF mdEntity, String fileName, String extension)
  {
    super(mdEntity, fileName, extension);
  }

  @Override
  public MdRelationshipDAOIF getMdEntity()
  {
    return (MdRelationshipDAOIF) super.getMdEntity();
  }

  @Override
  public void header()
  {
    writeIncludes();
    
    writeTitle("Select " + this.getMdEntity().getDisplayLabel(Session.getCurrentLocale()) + " Participants");
  }

  @Override
  public void form()
  {
    String type = this.getMdEntity().definesType();

    // Open form tag
    writeForm("POST", type + ".form.id", type + ".form.name");
  }

  @Override
  public void closeForm()
  {
    getWriter().closeTag();
  }

  @Override
  public void afterForm()
  {
    // Open DL tag
    getWriter().openTag(DL_TAG);
  }

  @Override
  public void beforeCloseForm()
  {
    String type = this.getMdEntity().definesType();
    String link = type + CONTROLLER_SUFFIX + "." + GeneratedActions.NEW_INSTANCE_ACTION.getName() + MdActionInfo.ACTION_SUFFIX;
    String linkName = type + ".form.newInstance.button";
    String linkDisplay = "New Instance";

    writeCommand(link, linkName, linkDisplay);

    // Close DL tag
    getWriter().closeTag();
  }

  @Override
  public void parent(RelationshipEventIF event)
  {
    writeLabel(event.getParent().getDisplayLabel(CommonProperties.getDefaultLocale()));

    // Open DD tag
    getWriter().openTag(DD_TAG);

    generateReference(PARENT_ATTRIBUTE, "parentId");

    // Close DD tag
    getWriter().closeTag();
  }

  @Override
  public void child(RelationshipEventIF event)
  {
    writeLabel(event.getChild().getDisplayLabel(CommonProperties.getDefaultLocale()));

    // Open DD tag
    getWriter().openTag(DD_TAG);

    generateReference(CHILD_ATTRIBUTE, "childId");

    // Close DD tag
    getWriter().closeTag();
  }

  protected void writeLabel(String label)
  {
    // Open the DT tag
    getWriter().openTag(DT_TAG);

    // Open the label tag
    getWriter().openTag(LABEL_TAG);

    // Write the label
    getWriter().writeValue(label);

    // Close the label tag
    getWriter().closeTag();

    // Close the DT tag
    getWriter().closeTag();
  }

  protected void generateReference(String items, String param)
  {
    // Open select tag
    writeSelect("${" + items + "}", "current", param, "id");

    writeOption("${current.keyName}");

    // Close select tag
    getWriter().closeTag();
  }
}
