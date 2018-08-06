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
package com.runwaysdk.query;

import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.constants.BusinessInfo;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;

public class GenericBusinessQuery extends GeneratedBusinessQuery
{
  private MdBusinessDAOIF mdBusiness;

  public GenericBusinessQuery(MdBusinessDAOIF mdBusiness, QueryFactory componentQueryFactory)
  {
    super();

    this.mdBusiness = mdBusiness;

    if (this.getComponentQuery() == null)
    {
      this.setBusinessQuery(componentQueryFactory.businessQuery(mdBusiness.definesType()));
    }
  }

  public GenericBusinessQuery(MdBusinessDAOIF mdBusiness, ValueQuery valueQuery)
  {
    super();

    this.mdBusiness = mdBusiness;

    if (this.getComponentQuery() == null)
    {
      this.setBusinessQuery(new BusinessQuery(valueQuery, mdBusiness.definesType()));
    }
  }

  @Override
  public String getClassType()
  {
    return this.mdBusiness.definesType();
  }

  public SelectableChar getOid()
  {
    return this.getOid(null);
  }

  public SelectableChar getOid(String alias)
  {
    return (SelectableChar) this.getComponentQuery().get(BusinessInfo.OID, alias, null);
  }

  public SelectableChar getOid(String alias, String displayLabel)
  {
    return (SelectableChar) this.getComponentQuery().get(BusinessInfo.OID, alias, displayLabel);
  }
}
