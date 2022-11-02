/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.query;

import java.util.HashSet;
import java.util.Set;

public abstract class Visitor
{
  private Set<ComponentQuery> visitedQueries;

  private Set<Component>      visitedComponents;

  private ComponentQuery      componentQuery;

  public Visitor(ComponentQuery componentQuery)
  {
    super();

    this.componentQuery = componentQuery;
    this.visitedQueries = new HashSet<ComponentQuery>();
    this.visitedComponents = new HashSet<Component>();
  }

  public ComponentQuery getComponentQuery()
  {
    return this.componentQuery;
  }

  public boolean hasVisitedQuery(ComponentQuery componentQuery)
  {
    return visitedQueries.contains(componentQuery);
  }

  public void addVisitedQuery(ComponentQuery componentQuery)
  {
    this.visitedQueries.add(componentQuery);
  }

  public boolean hasVisitedComponent(Component component)
  {
    return visitedComponents.contains(component);
  }

  public void addVisitedComponent(Component component)
  {
    this.visitedComponents.add(component);
  }

  public void visit(Component component)
  {
    // Do nothing as the default implementation.
  }

  public void visit(ComponentQuery query)
  {
    if (query instanceof Component)
    {
      this.visit((Component) query);
    }
  }

  public void visit(InnerJoin join)
  {
    this.visit((Component) join);
  }

  public void visit(LeftJoin join)
  {
    this.visit((Component) join);
  }

  public void visit(AggregateFunction function)
  {
    this.visit((Component) function);
  }

  public void visit(Condition condition)
  {
    this.visit((Component) condition);
  }

  public void visit(Attribute condition)
  {
    this.visit((Component) condition);
  }

  public void visit(Function function)
  {
    this.visit((Component) function);
  }

  public void visit(SimpleFunction function)
  {
    this.visit((Component) function);
  }

  public void visit(StatementPrimitive statement)
  {
    this.visit((Component) statement);
  }

  public void visit(Selectable selectable)
  {
    this.visit((Component) selectable);
  }

  public void visit(SelectableSQL selectable)
  {
    this.visit((Component) selectable);
  }

  public void visit(SubSelectCondition condition)
  {
    this.visit((Component) condition);
  }

}
