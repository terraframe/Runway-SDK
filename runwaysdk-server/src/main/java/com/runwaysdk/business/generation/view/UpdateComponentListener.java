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
package com.runwaysdk.business.generation.view;

import java.util.HashMap;

import com.runwaysdk.constants.GeneratedActions;
import com.runwaysdk.constants.MdActionInfo;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.session.Session;

/**
 * {@link ContentListener} used to generate 'updateComponent.jsp', which is
 * responsible for writing the CRUD attributes of an existing {@link MdEntityDAOIF}.
 * 
 * @author Justin Smethie
 */
public class UpdateComponentListener extends ContentAdapter
{
  public UpdateComponentListener(MdEntityDAOIF mdEntity)
  {
    this(mdEntity, "editComponent", "jsp");
  }
  
  public UpdateComponentListener(MdEntityDAOIF mdEntity, String fileName, String extension)
  {
    super(mdEntity, fileName, extension);
  }

  @Override
  public void header()
  {
    writeIncludes();
    
    writeTitle("Edit an existing " + this.getMdEntity().getDisplayLabel(Session.getCurrentLocale()));
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
    String updateLink = type + CONTROLLER_SUFFIX + "." + GeneratedActions.UPDATE_ACTION.getName() + MdActionInfo.ACTION_SUFFIX;
    String deleteLink = type + CONTROLLER_SUFFIX + "." + GeneratedActions.DELETE_ACTION.getName() + MdActionInfo.ACTION_SUFFIX;
    String cancelLink = type + CONTROLLER_SUFFIX + "." + GeneratedActions.CANCEL_ACTION.getName() + MdActionInfo.ACTION_SUFFIX;

    writeCommand(updateLink, type + ".form.update.button", "Update");
    writeCommand(deleteLink, type + ".form.delete.button", "Delete");
    writeCommand(cancelLink, type + ".form.cancel.button", "Cancel");
  }

}
