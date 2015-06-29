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

import java.util.HashMap;

import com.runwaysdk.constants.GeneratedActions;
import com.runwaysdk.constants.MdActionInfo;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.session.Session;

/**
 * {@link ContentListener} used to generate 'createComponent.jsp', which is
 * responsible for writing the CRUD attributes of a new {@link MdEntityDAOIF}.
 * 
 * @author Justin Smethie
 */
public class CreateComponentListener extends ContentAdapter
{
  
  public CreateComponentListener(MdEntityDAOIF mdEntity)
  {
    this(mdEntity, "createComponent", "jsp");
  }
  
  public CreateComponentListener(MdEntityDAOIF mdEntity, String fileName, String extension)
  {
    super(mdEntity, fileName, extension);
  }

  @Override
  public void header()
  {
    writeIncludes();
    
    writeTitle("Create a new " + this.getMdEntity().getDisplayLabel(Session.getCurrentLocale()));
  }

  @Override
  public void beforeForm()
  {
    // Open list tag
    getWriter().openTag(DL_TAG);
  }

  @Override
  public void afterCloseForm()
  {
    // Close list tag
    getWriter().closeTag();
  }

  @Override
  public void form()
  {
    String type = this.getMdEntity().definesType();
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
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("file", "form.jsp");

    getWriter().writeEmptyTag("<%@", "%>", "include", map);
  }

  @Override
  public void beforeCloseForm()
  {
    String type = getMdEntity().definesType();
    String link = type + CONTROLLER_SUFFIX + "." + GeneratedActions.CREATE_ACTION.getName() + MdActionInfo.ACTION_SUFFIX;
    String linkName = type + ".form.create.button";
    String linkDisplay = "Create";

    writeCommand(link, linkName, linkDisplay);
  }
}
