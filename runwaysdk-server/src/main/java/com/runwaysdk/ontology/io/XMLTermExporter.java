/**
 * 
 */
package com.runwaysdk.ontology.io;

import java.io.OutputStream;
import java.util.Stack;

import com.runwaysdk.business.Relationship;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.io.instance.OutputStreamInstanceExporter;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.system.ontology.TermUtil;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
 ******************************************************************************/
public class XMLTermExporter
{
  OutputStreamInstanceExporter exporter;
  
  public XMLTermExporter(OutputStream stream) {
    this.exporter = new OutputStreamInstanceExporter(stream, "classpath:com/runwaysdk/resources/xsd/instance.xsd", false);
  }

  /**
   * Exports all children of the given parent term to Runway's XML metadata
   * syntax. If includeParent is set to true it will also export the parent
   * node.
   * 
   * @param term
   */
  public void exportAll(Term parent, boolean includeParent)
  {
    exporter.open();

    if (includeParent)
    {
      exporter.export(parent.getId());

      // Loop over relationships with parents
      String[] prelts = TermUtil.getAllChildRelationships(parent.getId());
      for (String prelt : prelts)
      {
        OIterator<? extends Relationship> rel = parent.getParentRelationships(prelt);
        
        try {
          while (rel.hasNext()) {
            Relationship relat = rel.next();
            Term relParent = (Term) relat.getParent();
            if (!relParent.equals(Term.getRoot(relParent.getClass().getName()))) {
              exporter.export(rel.next().getId());
            }
          }
        }
        finally
        {
          rel.close();
        }
      }
    }

    // This stack contains terms that have been exported, but have children that
    // have not yet been exported.
    Stack<Term> s = new Stack<Term>();

    Term p = parent;
    do
    {
      String[] relTypes = TermUtil.getAllParentRelationships(p.getId());
      for (String relType : relTypes)
      {
        OIterator<? extends Relationship> rChildren = p.getChildRelationships(relType);
        try
        {
          for (Relationship rChild : rChildren)
          {
            exporter.export(rChild.getChildId());
            exporter.export(rChild.getId());
            s.push((Term) rChild.getChild());
          }
        }
        finally
        {
          rChildren.close();
        }
      }

      if (!s.empty())
      {
        p = s.pop();
      }
      else
      {
        p = null;
      }
    } while (s.size() > 0 || p != null);

    exporter.close();
  }

  // Tree traversal using all paths
  // ArrayList<String> duplicateIds = new ArrayList<String>();
  //
  // String[] rels = TermUtil.getAllParentRelationships(parent.getId());
  // for (String rel : rels) {
  // OIterator<Term> descends = parent.getAllDescendants(rel);
  // try {
  // for (Term t : descends) {
  // OIterator<? extends Relationship> parentRels =
  // t.getParentRelationships(rel);
  // try {
  // exporter.writeCreate(t);
  //
  // if (parentRels.hasNext()) {
  // Relationship firstRel = parentRels.next();
  // if (parentRels.hasNext()) {
  // if (!duplicateIds.contains(firstRel.getId())) {
  // duplicateIds.add(firstRel.getId());
  // exporter.writeCreate(firstRel);
  // }
  // while(parentRels.hasNext()) {
  // Relationship n = parentRels.next();
  // if (!duplicateIds.contains(n.getId())) {
  // duplicateIds.add(n.getId());
  // exporter.writeCreate(n);
  // }
  // }
  // }
  // else {
  // exporter.writeCreate(firstRel);
  // }
  // }
  // }
  // finally {
  // parentRels.close();
  // }
  // }
  // }
  // finally {
  // descends.close();
  // }
  // }
}
