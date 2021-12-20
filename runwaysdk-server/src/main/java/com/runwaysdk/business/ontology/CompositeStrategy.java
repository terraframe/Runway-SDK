/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.business.ontology;

import com.runwaysdk.query.OIterator;

public class CompositeStrategy implements OntologyStrategyIF
{
  private OntologyStrategyIF[] strategies;

  public CompositeStrategy(OntologyStrategyIF... strategies)
  {
    this.strategies = strategies;
  }

  @Override
  public boolean isInitialized()
  {
    boolean initialized = true;

    for (OntologyStrategyIF strategy : strategies)
    {
      initialized = initialized && strategy.isInitialized();
    }

    return initialized;
  }

  @Override
  public void initialize(String relationshipType)
  {
    this.initialize(relationshipType, null);
  }

  @Override
  public void initialize(String relationshipType, InitializationStrategyIF pStrategy)
  {
    for (OntologyStrategyIF strategy : strategies)
    {
      strategy.initialize(relationshipType, pStrategy);
    }
  }

  @Override
  public void shutdown()
  {
    for (OntologyStrategyIF strategy : strategies)
    {
      strategy.shutdown();
    }
  }

  @Override
  public void shutdown(String relationshipType)
  {
    for (OntologyStrategyIF strategy : strategies)
    {
      strategy.shutdown(relationshipType);
    }
  }

  @Override
  public void reinitialize(String relationshipType)
  {
    this.reinitialize(relationshipType, null);
  }

  @Override
  public void reinitialize(String relationshipType, InitializationStrategyIF pStrategy)
  {
    for (OntologyStrategyIF strategy : strategies)
    {
      strategy.reinitialize(relationshipType, pStrategy);
    }
  }

  @Override
  public void addLink(Term parent, Term child, String relationshipType)
  {
    for (OntologyStrategyIF strategy : strategies)
    {
      strategy.addLink(parent, child, relationshipType);
    }
  }

  @Override
  public boolean isLeaf(Term term, String relationshipType)
  {
    for (OntologyStrategyIF strategy : strategies)
    {
      return strategy.isLeaf(term, relationshipType);
    }

    return false;
  }

  @Override
  public OIterator<Term> getAllAncestors(Term term, String relationshipType)
  {
    for (OntologyStrategyIF strategy : strategies)
    {
      OIterator<Term> it = strategy.getAllAncestors(term, relationshipType);

      if (it != null)
      {
        return it;
      }
    }

    return null;
  }

  @Override
  public OIterator<Term> getAllDescendants(Term term, String relationshipType)
  {
    for (OntologyStrategyIF strategy : strategies)
    {
      OIterator<Term> it = strategy.getAllDescendants(term, relationshipType);

      if (it != null)
      {
        return it;
      }
    }

    return null;
  }

  @Override
  public OIterator<Term> getDirectAncestors(Term term, String relationshipType)
  {
    for (OntologyStrategyIF strategy : strategies)
    {
      OIterator<Term> it = strategy.getDirectAncestors(term, relationshipType);

      if (it != null)
      {
        return it;
      }
    }

    return null;
  }

  @Override
  public OIterator<Term> getDirectDescendants(Term term, String relationshipType)
  {
    for (OntologyStrategyIF strategy : strategies)
    {
      OIterator<Term> it = strategy.getDirectDescendants(term, relationshipType);

      if (it != null)
      {
        return it;
      }
    }

    return null;
  }

  @Override
  public void add(Term term, String relationshipType)
  {
    for (OntologyStrategyIF strategy : strategies)
    {
      strategy.add(term, relationshipType);
    }
  }

  @Override
  public void removeTerm(Term term, String relationshipType)
  {
    for (OntologyStrategyIF strategy : strategies)
    {
      strategy.removeTerm(term, relationshipType);
    }
  }

  @Override
  public void removeLink(Term parent, Term term, String relationshipType)
  {
    for (OntologyStrategyIF strategy : strategies)
    {
      strategy.removeLink(parent, term, relationshipType);
    }
  }

  @Override
  public void configure(String termClass)
  {
    for (OntologyStrategyIF strategy : strategies)
    {
      strategy.configure(termClass);
    }
  }

  @Override
  public DeleteStrategyProviderIF getDeleteStrategyProvider(Term deleteRoot, String relationshipType)
  {
    for (OntologyStrategyIF strategy : strategies)
    {
      DeleteStrategyProviderIF provider = strategy.getDeleteStrategyProvider(deleteRoot, relationshipType);

      if (provider != null)
      {
        return provider;
      }
    }

    return null;
  }

  @Override
  public void addSynonym(Term term, OntologyEntryIF synonym)
  {
    for (OntologyStrategyIF strategy : strategies)
    {
      strategy.addSynonym(term, synonym);
    }
  }

  @Override
  public void updateSynonym(OntologyEntryIF synonym)
  {
    for (OntologyStrategyIF strategy : strategies)
    {
      strategy.updateSynonym(synonym);
    }
  }

  @Override
  public void removeSynonym(OntologyEntryIF synonym)
  {
    for (OntologyStrategyIF strategy : strategies)
    {
      strategy.removeSynonym(synonym);
    }
  }

  @Override
  public void updateLabel(Term term, String label)
  {
    for (OntologyStrategyIF strategy : strategies)
    {
      strategy.updateLabel(term, label);
    }
  }

}
