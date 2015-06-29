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
package com.runwaysdk.transport.metadata;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.session.Session;

public class ServerAttributeMdBuilder
{
  protected MdAttributeDAOIF source;

  protected AttributeMdDTO dest;

  protected ServerAttributeMdBuilder(MdAttributeDAOIF source, AttributeMdDTO dest)
  {
    this.source = source;
    this.dest = dest;
  }

  protected void build()
  {
      dest.setDisplayLabel(source.getDisplayLabel(Session.getCurrentLocale()));
      dest.setDescription(source.getDescription(Session.getCurrentLocale()));
      dest.setImmutable(source.isImmutable());
      dest.setRequired(source.isRequiredForDTO());
      dest.setId(source.getId());
      dest.setName(source.definesAttribute());
      dest.setSystem(source.isSystem());
      dest.setGenerateAccessor(source.getGenerateAccessor());
  }
}
