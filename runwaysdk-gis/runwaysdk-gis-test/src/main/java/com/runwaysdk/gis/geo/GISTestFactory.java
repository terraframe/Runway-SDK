/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.gis.geo;

import com.runwaysdk.business.ontology.TermAndRel;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.LocatedIn;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.gis.geo.UniversalInput;
import com.runwaysdk.system.gis.geo.UniversalView;

/**
 * Helper methods to facilitate GIS testing.
 */
public class GISTestFactory
{

  /**
   * Creates a universal with the given name and parent.
   * 
   * @param name
   * @param allowedInParent
   * @return
   */
  public static TermAndRel createUniversal(String name, UniversalView... allowedInParents)
  {
    Universal uni = new Universal();
    uni.getDisplayLabel().setValue(name);

    return Universal.create(uni, allowedInParents[0].getUniversal().getOid(), AllowedIn.CLASS);
  }

  public static UniversalInput convertToInput(UniversalView view)
  {
    UniversalInput input = new UniversalInput();

    input.setUniversal(view.getUniversal());
    input.setDisplayLabel(view.getDisplayLabel());
    input.setDescription(view.getDescription());

    return input;
  }

  /**
   * Deletes the Universal based on a valid UniversalView.
   * 
   * @param view
   */
  public static void deleteUniversal(UniversalView view)
  {
    if (view != null)
    {
      Universal u = view.getUniversal();
      if (u.isAppliedToDB())
      {
        u.delete();
      }
    }
  }

  /**
   * @param name
   * @return
   */
  public static Universal createUniversal(String name)
  {
    Universal universal = new Universal();
    universal.setUniversalId(name);
    universal.getDisplayLabel().setValue(name);
    universal.getDescription().setValue(name);

    return universal;
  }

  /**
   * @param name
   * @return
   */
  public static Universal createAndApplyUniversal(String name)
  {
    Universal universal = GISTestFactory.createUniversal(name);
    universal.apply();

    return universal;
  }

  public static Universal createAndApplyUniversal(String name, Universal parent)
  {
    Universal universal = GISTestFactory.createUniversal(name);
    universal.apply();

    universal.addLink(parent, AllowedIn.CLASS);

    return universal;
  }

  /**
   * @param universalNames
   */
  public static void deleteUniversals(String... universalNames)
  {
    for (String name : universalNames)
    {
      try
      {
        Universal.getByKey(name).delete();
        ;
      }
      catch (DataNotFoundException e)
      {
        // Do nothing
      }
    }
  }

  public static AllowedIn createAllowedIn(Universal universal)
  {
    return GISTestFactory.createAllowedIn(Universal.getRoot(), universal);
  }

  public static AllowedIn createAllowedIn(Universal parent, Universal child)
  {
    return new AllowedIn(parent, child);
  }

  public static GeoEntity createAndApplyGeoEntity(String geoId, Universal universal)
  {
    GeoEntity entity = new GeoEntity();
    entity.setGeoId(geoId);
    entity.getDisplayLabel().setValue(geoId);
    entity.setUniversal(universal);
    entity.apply();

    return entity;
  }

  public static GeoEntity createAndApplyGeoEntity(String geoId, Universal universal, GeoEntity parent)
  {
    GeoEntity retGeo = createAndApplyGeoEntity(geoId, universal);

    retGeo.addLink(parent, LocatedIn.CLASS);

    return retGeo;
  }

  public static void deleteGeoEntities(String... geoIds)
  {
    for (String geoId : geoIds)
    {
      try
      {
        GeoEntity.getByKey(geoId).delete();
      }
      catch (DataNotFoundException e)
      {
        // Do nothing
      }
    }
  }

}
