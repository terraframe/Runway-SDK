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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.business.Relationship;
import com.runwaysdk.business.ontology.OntologyStrategyBuilderIF;
import com.runwaysdk.business.ontology.OntologyStrategyFactory;
import com.runwaysdk.business.ontology.OntologyStrategyIF;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.business.ontology.TermAndRel;
import com.runwaysdk.dataaccess.transaction.AbortIfProblem;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.logging.LogLevel;
import com.runwaysdk.query.Condition;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategy;
import com.runwaysdk.system.ontology.TermUtil;

public class Universal extends UniversalBase
{
  private static final long  serialVersionUID = 1467479707;

  public static final Log    log              = LogFactory.getLog(Universal.class);

  /**
   * Constant name for the root node
   */
  public static final String ROOT             = "ROOT";

  public Universal()
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
    boolean isNew = this.isNew() && !this.isAppliedToDB();

    super.apply();

    if (isNew)
    {
      // Universal root = Universal.getRoot();

      /*
       * Add the term to the ontology strategy
       */
      this.addTerm(AllowedIn.CLASS);

      // /*
      // * By default set this term as a child of the root node
      // */
      // this.addLink(root, AllowedIn.CLASS);
    }
    // else {
    // List<? extends Business> parents = this.getParents(AllowedIn.CLASS);
    // boolean isCountry = parents.size() == 1 &&
    // parents.get(0).getId().equals(Universal.getRoot().getId());
    //
    // if (isCountry) {
    // // Update the GeoEntity country.
    // }
    // }
  }

  /**
   * Builds the this object's key name.
   */
  @Override
  public String buildKey()
  {
    return this.getUniversalId();
  }

  @Override
  @Transaction
  @com.runwaysdk.logging.Log(level = LogLevel.DEBUG)
  public void beforeDeleteTerm()
  {
    // Delete all GeoEntites that reference this Universal. This must be done
    // before
    // the Universal itself is deleted because each GeoEntity has a required
    // reference back to its defining Universal object (ie, *this*).
    GeoEntityQuery query = new GeoEntityQuery(new QueryFactory());
    query.WHERE(query.getUniversal().EQ(this));

    OIterator<? extends GeoEntity> iter = query.getIterator();

    try
    {
      while (iter.hasNext())
      {
        iter.next().delete();
      }
    }
    finally
    {
      iter.close();
    }
  }

  /**
   * Returns an array of Universals derived from the given UniversalView
   * objects.
   * 
   * @param views
   * @return
   */
  public static Universal[] queryUniversals(UniversalView... views)
  {
    // although we could call UniversalView.getUniversal() and collect
    // those as an array, it would be slow because each call would require
    // a separate query. Instead we perform one query that matches on ids.
    String[] ids = new String[views.length];
    for (int i = 0; i < views.length; i++)
    {
      ids[i] = views[i].getUniversalId();
    }

    Universal[] universals = new Universal[views.length];

    UniversalQuery q = new UniversalQuery(new QueryFactory());
    q.WHERE(q.getId().IN(ids));

    OIterator<? extends Universal> iter = q.getIterator();

    try
    {
      int count = 0;
      while (iter.hasNext())
      {
        universals[count] = iter.next();
        count++;
      }

      return universals;
    }
    finally
    {
      iter.close();
    }
  }

  /**
   * Adds the given Universals as AllowedIn parents of this Universal.
   * 
   * @param parents
   */
  @Transaction
  public void addAllowedInParent(Universal... parents)
  {
    if (parents != null)
    {
      for (Universal p : parents)
      {
        this.addAllowedIn(p).apply();
      }
    }
  }

  @Transaction
  public void addAllowedInParent(UniversalView... parents)
  {
    this.addAllowedInParent(Universal.queryUniversals(parents));
  }

  /**
   * This MdMethod will apply the Universal dto and append it to parentId with
   * relationshipType. If parentId == RootUniversal then it will also create a
   * corresponding GeoEntity.
   * 
   * @param dto
   * @param parentId
   * @param relationshipType
   * @return JSON {term, relType, relId}.
   */
  @Transaction
  public static TermAndRel create(Universal dto, String parentId, String relationshipType)
  {
    return Universal.doCreate(dto, parentId, relationshipType);
  }

  @AbortIfProblem
  private static TermAndRel doCreate(Universal child, String parentId, String relationshipType)
  {
    child.apply();

    Universal parent = Universal.get(parentId);

    // Creating a Country Universal automatically creates a GeoEntity of the
    // same name.
    if (parentId.equals(Universal.getRoot().getId()))
    {
      GeoEntity geo = new GeoEntity();
      geo.setGeoId(child.getUniversalId());
      geo.setUniversal(child);
      geo.getDisplayLabel().setValue(child.getDisplayLabel().getValue());
      geo.apply();

      geo.addLink(GeoEntity.getRoot(), LocatedIn.CLASS);

      // LocatedIn rel = geo.addLocatedIn(GeoEntity.getRoot());
      // rel.apply();
    }

    Relationship rel = child.addLink(parent, relationshipType);

    return new TermAndRel(child, relationshipType, rel.getId());
  }

  public static Universal getRoot()
  {
    return (Universal) Term.getRoot(Universal.CLASS);
  }

  /**
   * Creates a Universal based on the UniversalView input.
   * 
   * @param universalView
   * @return
   */
  // @com.runwaysdk.logging.Log(level = LogLevel.DEBUG)
  // @Transaction
  // public static UniversalView createUniversal(UniversalInput universalInput)
  // {
  // // this will validate the fields before
  // // attempting the actual creation.
  // universalInput.applyNoPersist();
  //
  // return createUniversalInternal(universalInput);
  // }
  //
  // /**
  // * Internal method to complete creating a Universal.
  // *
  // * @param universalView
  // * @return
  // */
  // @AbortIfProblem
  // private static UniversalView createUniversalInternal(UniversalInput
  // universalInput)
  // {
  // String displayLabel = universalInput.getDisplayLabel();
  //
  // String name = NameConventionUtil.buildClass(displayLabel);
  //
  // // Now relate the Universal to the MdBusiness
  // Universal universal = new Universal();
  // universal.setUniversalId(name);
  // universal.getDisplayLabel().setValue(universalInput.getDisplayLabel());
  // universal.getDescription().setValue(universalInput.getDescription());
  // universal.apply();
  //
  // // Set the AllowedIn relationship with the specified parent, or Root if
  // // not specified.
  // Universal allowedInParent = universalInput.getParentUniversal();
  //
  // if (allowedInParent == null)
  // {
  // allowedInParent = Universal.getRoot();
  // }
  //
  // universal.addAllowedInParent(allowedInParent);
  //
  // // fetch a fresh copy of this Universal as a UniversalView, which will
  // // pull all the most recent values (in case business logic changed them).
  // return universal.copyAsView();
  // }
  //
  // /**
  // * Returns this Universal as a UniversalView object. This will copy the
  // values
  // * currently on the Universal, which may differ from the persisted values
  // * since the last apply.
  // *
  // * @return
  // */
  // public UniversalView copyAsView()
  // {
  // TODO does the copy logic go here or in UniversalView?
  // TODO Implement (or does this even need to exist?)
  // throw new RuntimeException("Universal.copyAsView() Not implemented yet.");
  // }

  /**
   * Removing a link between a Universal may invalidate some GeoEntity
   * dependencies. Do an Enforcement check here.
   */
  // public Relationship addAndRemoveLink(Term oldParent, String oldRel, Term
  // newParent, String newRel)
  // {
  // if (this.getUniversalId().equals(ROOT))
  // {
  // ImmutableRootException exception = new ImmutableRootException("Cannot
  // modify the root universal");
  // exception.setRootName(Universal.ROOT);
  // exception.apply();
  //
  // throw exception;
  // }
  //
  //// if ( !(oldParent.equals(newParent) && !oldRel.equals(newRel)) ) {
  //// this.enforceValidRemoveLink(oldParent, relationshipType);
  //// }
  //
  // return super.addAndRemoveLink(oldParent, oldRel, newParent, newRel);
  // }

  /**
   * Removing a link between a Universal may invalidate some GeoEntity
   * dependencies. Do an Enforcement check here.
   */
  @Transaction
  public void removeLink(Term parent, String relationshipType)
  {
    super.removeLink(parent, relationshipType);

    // If we just invalidated the GeoEntity tree this enforcement will throw an
    // exception and the transaction will rollback, undoing that remove link.
    this.enforceValidRemoveLink(parent, relationshipType);
  }

  private void enforceValidRemoveLink(Term parent, String relationshipType)
  {
    // Its possible that the link between these two universals may exist, but by
    // a different relationship type.
    // If this link exists, then we're not deleting the link and the GeoEntites
    // won't be invalidated, so we skip the check.
    // boolean skipCheck = false;
    // String[] possibleRels =
    // TermUtil.getAllParentRelationships(parent.getId());
    // for (String possibleRel : possibleRels) {
    // if (!possibleRel.equals(relationshipType)) {
    // // TODO : There may be a way to do this in one query, ask Naifeh/Smethie.
    // RelationshipQuery relq = new
    // QueryFactory().relationshipQuery(possibleRel);
    // relq.WHERE(relq.parentId().EQ(parent.getId()).AND(relq.childId().EQ(this.getId())));
    // OIterator<? extends Relationship> relit = relq.getIterator();
    //
    // try {
    // if (relit.hasNext()) {
    // skipCheck = true;
    // break;
    // }
    // }
    // finally {
    // relit.close();
    // }
    // }
    // }

    // if (!skipCheck) {
    List<Term> thisAndAllAncestors = new LinkedList<Term>(Arrays.asList(TermUtil.getAllAncestors(this.getId(), TermUtil.getAllChildRelationships(this.getId()))));
    thisAndAllAncestors.add(this);

    // 1) Get all GeoEntites that reference this universal or any of our
    // descendants
    GeoEntityQuery query = new GeoEntityQuery(new QueryFactory());
    Condition condition = query.getUniversal().EQ(this);

    Term[] thisAllDescends = TermUtil.getAllDescendants(this.getId(), TermUtil.getAllParentRelationships(this.getId()));
    for (int i = 0; i < thisAllDescends.length; ++i)
    {
      condition = condition.OR(query.getUniversal().EQ((Universal) thisAllDescends[i]));
    }
    query.WHERE(condition);
    OIterator<? extends GeoEntity> iter = query.getIterator();

    // Loop over this set of potential GoeEntitys that may now be invalid.
    List<String> invalidGeos = new LinkedList<String>();
    try
    {
      while (iter.hasNext())
      {
        // Check to ensure that this GeoEntity is still valid. A GeoEntity is
        // valid iff its universal is a descendant of the parent geo's universal
        GeoEntity geo = iter.next();

        List<Term> uniAncestors = Arrays.asList(TermUtil.getAllAncestors(geo.getUniversalId(), TermUtil.getAllChildRelationships(geo.getUniversalId())));

        String[] rels = TermUtil.getAllChildRelationships(geo.getId());
        for (String rel : rels)
        {
          OIterator<Term> geoAncestIt = geo.getDirectAncestors(rel);

          try
          {
            while (geoAncestIt.hasNext())
            {
              Term t = geoAncestIt.next();

              if (t instanceof GeoEntity)
              {
                Universal ancestUni = ( (GeoEntity) t ).getUniversal();

                if (!uniAncestors.contains(ancestUni))
                {
                  invalidGeos.add(geo.getDisplayLabel().getValue());
                }
              }
            }
          }
          finally
          {
            geoAncestIt.close();
          }
        }
      }
    }
    finally
    {
      iter.close();
    }

    if (invalidGeos.size() > 0)
    {
      InvalidUniversalRemoveLinkException ex = new InvalidUniversalRemoveLinkException();
      ex.setParentUni(parent.getDisplayLabel().getValue());
      ex.setChildUni(this.getDisplayLabel().getValue());
      ex.setGeoEntities(StringUtils.join(invalidGeos, ", "));
      throw ex;
    }
    // }
  }

  /**
   * Factory method that instantiates a new type-safe GeoEntity defined by this
   * Universal. The returned GeoEntity is not persisted.
   * 
   * @return
   */
  // public GeoEntity geoEntityFactory()
  // {
  // return GeoEntity.geoEntityFactory(this);
  // }

  /**
   * The strategy to manage the Universal ontology behavior.
   */
  public static OntologyStrategyIF createStrategy()
  {
    return OntologyStrategyFactory.get(Universal.CLASS, new OntologyStrategyBuilderIF()
    {
      @Override
      public OntologyStrategyIF build()
      {
        return DatabaseAllPathsStrategy.factory(Universal.CLASS);
      }
    });
  }
}
