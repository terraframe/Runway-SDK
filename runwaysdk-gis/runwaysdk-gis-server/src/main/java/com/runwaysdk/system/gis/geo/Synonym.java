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
package com.runwaysdk.system.gis.geo;

import com.runwaysdk.business.ontology.OntologyStrategyIF;
import com.runwaysdk.business.ontology.OntologyEntryIF;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.business.ontology.TermAndRel;
import com.runwaysdk.dataaccess.transaction.Transaction;

public class Synonym extends SynonymBase implements OntologyEntryIF
{
  private static final long serialVersionUID = -613276923;

  public Synonym()
  {
    super();
  }

  @Override
  public String getLabel()
  {
    return this.getDisplayLabel().getValue();
  }

  @Override
  public void apply()
  {
    if (this.isAppliedToDB())
    {
      OntologyStrategyIF strategy = GeoEntity.createStrategy();
      strategy.updateSynonym(this);
    }

    super.apply();
  }

  @Override
  public void delete()
  {
    if (this.isAppliedToDB())
    {
      OntologyStrategyIF strategy = GeoEntity.createStrategy();
      strategy.removeSynonym(this);
    }

    super.delete();
  }

  /**
   * MdMethod used for creating Synonyms.
   * 
   * @param termId
   * @param name
   * @return
   */
  @Transaction
  public static TermAndRel create(Synonym synonym, String geoId)
  {
    String keyName = geoId + "-" + synonym.getDisplayLabel().getValue().trim().replaceAll("\\s+", "");

    synonym.setKeyName(keyName);
    synonym.apply();

    GeoEntity geo = GeoEntity.get(geoId);

    OntologyStrategyIF strategy = GeoEntity.createStrategy();
    strategy.addSynonym(geo, synonym);

    SynonymRelationship rel = geo.addSynonym(synonym);
    rel.apply();

    return new TermAndRel(synonym, SynonymRelationship.CLASS, rel.getId());
  }

  public static Term getRoot()
  {
    return GeoEntity.getRoot();
  }
}
