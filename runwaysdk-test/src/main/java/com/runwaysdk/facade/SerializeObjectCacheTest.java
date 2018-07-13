/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
 */
package com.runwaysdk.facade;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;

public class SerializeObjectCacheTest
{
  private static final String ATTRIBUTE_NAME = "testBoolean";

  @Request
  @Test
  public void testSerializeObjectCache()
  {
    MdBusinessDAO mdBusiness = this.createObjects();

    try
    {
      ObjectCache.shutdownGlobalCache();

      this.getObjects(mdBusiness.definesType());
    }
    finally
    {
      this.deleteObjects(mdBusiness);
    }
  }

  @Request
  private MdBusinessDAO createObjects()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness("CacheTest");
    mdBusiness.apply();

    MdAttributeBooleanDAO mdAttribute = TestFixtureFactory.addBooleanAttribute(mdBusiness, ATTRIBUTE_NAME);
    mdAttribute.apply();

    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    business.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
    business.apply();

    return mdBusiness;
  }

  @Request
  private void getObjects(String type)
  {
    MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO(type);
    MdAttributeConcreteDAOIF mdAttribute = mdBusiness.definesAttribute(ATTRIBUTE_NAME);

    Assert.assertNotNull(mdAttribute);

    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(type);

    OIterator<BusinessDAOIF> it = query.getIterator();

    try
    {
      List<BusinessDAOIF> results = it.getAll();

      Assert.assertTrue(results.size() > 0);
    }
    finally
    {
      it.close();
    }
  }

  @Request
  private void deleteObjects(MdBusinessDAO mdBusiness)
  {
    TestFixtureFactory.delete(mdBusiness);
  }
}
