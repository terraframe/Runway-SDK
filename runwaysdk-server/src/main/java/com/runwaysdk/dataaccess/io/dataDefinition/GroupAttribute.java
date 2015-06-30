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
/**
 * 
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.constants.MdFieldInfo;
import com.runwaysdk.dataaccess.MdWebGroupDAOIF;
import com.runwaysdk.dataaccess.metadata.MdFieldDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFieldDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFormDAO;
import com.runwaysdk.dataaccess.metadata.MdWebGroupDAO;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
public class GroupAttribute
{
  private MdWebFormDAO  mdForm;

  private MdWebFieldDAO mdField;

  private String        groupName;

  /**
   * @param tagName
   * @param attributes
   * @param mdField
   */
  public GroupAttribute(Attributes attributes, MdWebFormDAO mdForm, MdFieldDAO mdField)
  {
    this.mdForm = mdForm;
    this.mdField = (MdWebFieldDAO) mdField;
    this.groupName = attributes.getValue(XMLTags.GROUP_NAME_ATTRIBUTE);
  }

  public void process()
  {
    MdWebGroupDAOIF existingGroup = this.mdField.getContainingGroup();

    if (existingGroup != null)
    {
      existingGroup.removeField(this.mdField);
    }

    if (groupName != null)
    {
      MdWebGroupDAOIF group = this.getGroup();
      group.addField(this.mdField).apply();
    }
  }

  /**
   * @param fieldName
   * @return
   */
  private MdWebGroupDAOIF getGroup()
  {
    String key = MdFieldDAO.buildKey(this.mdForm.getKey(), this.groupName);

    return (MdWebGroupDAOIF) MdWebGroupDAO.get(MdFieldInfo.CLASS, key);
  }
}
