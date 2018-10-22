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
package com.runwaysdk.system.metadata.ontology;

import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdTermDAOIF;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.session.Request;

public class OntologyStrategyTest
{
  /**
   *
   *
   */
  @Request
  @Test
  public void testCreateAndGetMdTerm()
  {

    MdTermDAO mdTerm = MdTermDAO.newInstance();
    mdTerm.setValue(MdTermInfo.NAME, "Term");
    mdTerm.setValue(MdTermInfo.PACKAGE, "com.test");
    mdTerm.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdTerm.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdTerm.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTerm.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
    // mdTerm.setValue(MdTermInfo.STRATEGY, state.getOid());
    mdTerm.apply();

    try
    {
      BusinessDAO state = BusinessDAO.newInstance(DatabaseAllPathsStrategy.CLASS);
      state.addItem(DatabaseAllPathsStrategy.STRATEGYSTATE, StrategyState.UNINITIALIZED.getOid());
      state.setValue(DatabaseAllPathsStrategy.TERMCLASS, mdTerm.getOid());
      state.apply();

      try
      {
        MdTermDAOIF result = MdTermDAO.getMdTermDAO(mdTerm.definesType());

        Assert.assertNotNull(result);
        // Assert.assertEquals(result.getValue(MdTermInfo.STRATEGY),
        // mdTerm.getValue(MdTermInfo.STRATEGY));
        
        BusinessDAOIF test = BusinessDAO.get(state.getOid());

        Assert.assertNotNull(test);
        Assert.assertEquals(state.getOid(), test.getOid());
        Assert.assertEquals(state.getValue(DatabaseAllPathsStrategy.STRATEGYSTATE), test.getValue(DatabaseAllPathsStrategy.STRATEGYSTATE));
      }
      finally
      {
        TestFixtureFactory.delete(state);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdTerm);
    }
  }
}
