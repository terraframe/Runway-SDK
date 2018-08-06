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
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.constants.MdWarningInfo;
import com.runwaysdk.dataaccess.MdWarningDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdLocalizableDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.MdWarningDAO;

public class MdWarningHandler extends MdLocalizableHandler implements TagHandlerIF, HandlerFactoryIF
{
  public MdWarningHandler(ImportManager manager)
  {
    super(manager, MdWarningInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.MdNotificationHandler#populate(com.runwaysdk.dataaccess.metadata.MdNotificationDAO, org.xml.sax.Attributes)
   */
  @Override
  protected void populate(MdLocalizableDAO mdLocalizable, Attributes attributes)
  {
    super.populate(mdLocalizable, attributes);

    // Import optional reference attributes
    String extend = attributes.getValue(XMLTags.EXTENDS_ATTRIBUTE);

    if (extend != null)
    {
      // Ensure the parent class has already been defined in the database
      if (!MdTypeDAO.isDefined(extend))
      {
        // The type is not defined in the database, check if it is defined
        // in the further down in the xml document.
        String[] search_tags = { XMLTags.MD_WARNING_TAG };
        SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, extend, mdLocalizable.definesType());
      }

      MdWarningDAOIF superException = MdWarningDAO.getMdWarning(extend);
      mdLocalizable.setValue(MdWarningInfo.SUPER_MD_WARNING, superException.getOid());
    }

  }
}