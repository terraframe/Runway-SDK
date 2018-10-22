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
package com.runwaysdk.system.ontology;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.runwaysdk.business.Relationship;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.business.ontology.TermAndRel;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdTermDAOIF;
import com.runwaysdk.dataaccess.MdTermRelationshipDAOIF;
import com.runwaysdk.dataaccess.io.TimeFormat;
import com.runwaysdk.dataaccess.io.dataDefinition.VersionExporter;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.ontology.io.TermExporter;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.system.ontology.io.TermFileFormat;

public class TermUtil extends TermUtilBase
{
  private static final long serialVersionUID = -1933843303;

  public TermUtil()
  {
    super();
  }

  // public static com.runwaysdk.business.ontology.Term[]
  // getOrderedAllAncestors(String termId, String[] relationshipTypes)
  // {
  // // termId, depth, displayLabel
  //
  // // Term subtypes all have a generated DisplayLabel
  // String termType = Term.get(termId).getMdTerm().definesType();
  // String displayLabelTable = MdEntity.getMdEntity(termType +
  // "DisplayLabel").getTableName();
  //
  // MdEntityDAOIF termMd = MdEntityDAO.getMdEntityDAO(termType);
  // String termTable = termMd.getTableName();
  //
  // MdEntityDAOIF mdTypeMd = MdEntityDAO.getMdEntityDAO(MdTypeInfo.CLASS);
  // String mdTypeTable = mdTypeMd.getTableName();
  //
  // String termRelTable =
  // MdEntityDAO.getMdEntityDAO(TermRelationship.CLASS).getTableName();
  //
  // String sql = "";
  //
  // sql += "WITH RECURSIVE term_flags AS( \n";
  // // sql += "SELECT t.oid, dl.default_locale \n";
  // sql += "SELECT term.oid \n";
  // sql += "FROM " + termTable + " term, " + termRelTable + " termRel \n";
  // // sql += "INNER JOIN " + displayLabelTable + " dl\n";
  // // sql += "ON dl." + "oid" + "=t." + "display_label";
  // sql += "WHERE  termRel." + RelationshipInfo.PARENT_OID + " = term.oid \n";
  // sql += ") \n";
  // sql += ", recursive_rollup AS ( \n";
  // sql += " SELECT * ,0 as depth \n";
  // sql += "   \n";
  // sql += "  FROM term_flags \n";
  // sql += " UNION \n";
  // sql += " SELECT * \n";
  // sql += " FROM recursive_rollup a,  term_flags b \n";
  // sql += " WHERE a.child_type = b.parent_type \n";
  // sql += ") \n";
  //
  // }

  /**
   * MdMethod, delegates to the ontology strategy.
   * 
   * @param termId
   * @param relationshipTypes
   * @return
   */
  public static com.runwaysdk.business.ontology.Term[] getAllAncestors(String termId, String[] relationshipTypes)
  {
    List<Term> terms = new ArrayList<Term>();
    Term parent = Term.get(termId);

    for (String relType : relationshipTypes)
    {
      terms.addAll(parent.getAllAncestors(relType).getAll());
    }

    return terms.toArray(new Term[terms.size()]);
  }

  /**
   * MdMethod, delegates to the ontology strategy.
   * 
   * @param termId
   * @param relationshipTypes
   * @return
   */
  public static com.runwaysdk.business.ontology.Term[] getAllDescendants(String termId, String[] relationshipTypes)
  {
    List<Term> terms = new ArrayList<Term>();
    Term parent = Term.get(termId);

    for (String relType : relationshipTypes)
    {
      terms.addAll(parent.getAllDescendants(relType).getAll());
    }

    return terms.toArray(new Term[terms.size()]);
  }

  /**
   * MdMethod, returns the direct ancestors of the given term, with the given
   * relationship type. The TermAndRels are sorted by display label.
   * 
   * @param termId
   * @param relationshipTypes
   * @return
   */
  public static com.runwaysdk.business.ontology.TermAndRel[] getDirectAncestors(String termId, String[] relationshipTypes)
  {
    List<TermAndRel> tnrs = new ArrayList<TermAndRel>();
    Term child = Term.get(termId);

    for (String relType : relationshipTypes)
    {
      OIterator<? extends Relationship> parentRels = child.getParentRelationships(relType);

      for (Relationship rel : parentRels)
      {
        tnrs.add(new TermAndRel((Term) rel.getParent(), relType, rel.getOid()));
      }
    }

    // Sort by displayLabel
    Collections.sort(tnrs, new Comparator<TermAndRel>()
    {
      public int compare(TermAndRel t1, TermAndRel t2)
      {
        return t1.getTerm().getDisplayLabel().getValue().compareToIgnoreCase(t2.getTerm().getDisplayLabel().getValue());
      }
    });

    return tnrs.toArray(new TermAndRel[tnrs.size()]);
  }

  /**
   * MdMethod, returns the direct descendants of the given term, with the given
   * relationship type. The TermAndRels are sorted by display label.
   * 
   * @param termId
   * @param relationshipTypes
   * @return
   */
  public static com.runwaysdk.business.ontology.TermAndRel[] getDirectDescendants(String termId, String[] relationshipTypes)
  {
    List<TermAndRel> tnrs = new ArrayList<TermAndRel>();
    Term parent = Term.get(termId);

    for (String relType : relationshipTypes)
    {
      OIterator<? extends Relationship> childRels = parent.getChildRelationships(relType);

      for (Relationship rel : childRels)
      {
        tnrs.add(new TermAndRel((Term) rel.getChild(), relType, rel.getOid()));
      }
    }

    // Sort by displayLabel
    Collections.sort(tnrs, new Comparator<TermAndRel>()
    {
      public int compare(TermAndRel t1, TermAndRel t2)
      {
        return t1.getTerm().getDisplayLabel().getValue().compareToIgnoreCase(t2.getTerm().getDisplayLabel().getValue());
      }
    });

    return tnrs.toArray(new TermAndRel[tnrs.size()]);
  }

  /**
   * MdMethod
   * 
   * @param childOid
   * @param oldParentOid
   * @param oldRelType
   * @param newParentOid
   * @param newRelType
   * @return
   */
  @Transaction
  public static com.runwaysdk.business.Relationship addAndRemoveLink(String childOid, String oldParentOid, String oldRelType, String newParentOid, String newRelType)
  {
    // Term child = (Term) Term.get(childOid);
    // Term oldParent = (Term) Term.get(oldParentOid);
    // Term newParent = (Term) Term.get(newParentOid);
    //
    // return child.addAndRemoveLink(oldParent, oldRelType, newParent,
    // newRelType);

    Relationship retRel = addLink(childOid, newParentOid, newRelType);
    removeLink(childOid, oldParentOid, oldRelType);
    return retRel;
  }

  /**
   * MdMethod
   * 
   * @param childOid
   * @param parentOid
   * @param relationshipType
   * @return
   */
  @Transaction
  public static com.runwaysdk.business.Relationship addLink(String childOid, String parentOid, String relationshipType)
  {
    Term child = (Term) Term.get(childOid);
    Term parent = (Term) Term.get(parentOid);

    return child.addLink(parent, relationshipType);
  }

  /**
   * MdMethod
   * 
   * @param childOid
   * @param parentOid
   * @param relationshipType
   */
  @Transaction
  public static void removeLink(String childOid, String parentOid, String relationshipType)
  {
    Term child = (Term) Term.get(childOid);
    Term parent = (Term) Term.get(parentOid);

    child.removeLink(parent, relationshipType);
  }

  /**
   * MdMethod Exports the term to the output stream.
   * 
   * @param outputStream
   * @param parentOid
   * @param exportParent
   * @param format
   */
  public static void exportTerm(OutputStream outputStream, String parentOid, Boolean exportParent, TermFileFormat format)
  {
    if (format == TermFileFormat.XML)
    {
      Term term = Term.get(parentOid);

      TermExporter exporter = new TermExporter(new VersionExporter(outputStream));
      exporter.exportAll(term, exportParent);
    }
    else
    {
      throw new UnsupportedOperationException("Unsupported format [" + format.name() + "]");
    }
  }

  /**
   * MdMethod Returns a timestamp for a new schema file.
   * 
   * @return
   */
  public static String getTimestamp()
  {
    long time = System.currentTimeMillis();
    String format = new TimeFormat(time).format();

    return format;
  }

  /**
   * (Currently Server-only) convenience method, fetches all direct descendants
   * of all valid relationship types.
   * 
   * @param termId
   * @return
   */
  // public static TermAndRel[] getDirectDescendants(String termId) {
  // return getDirectDescendants(termId, getAllParentRelationships(termId));
  // }

  /**
   * (Currently Server-only) convenience method, returns all relationships that
   * this term is a valid parent in.
   * 
   * @param termId
   * @return
   */
  public static String[] getAllParentRelationships(String termId)
  {
    Term term = (Term) Term.get(termId);

    MdTermDAOIF mdTerm = term.getMdTerm();
    List<MdRelationshipDAOIF> mdRelationships = mdTerm.getAllParentMdRelationships();

    ArrayList<String> rels = new ArrayList<String>();

    for (MdRelationshipDAOIF mdRelationshipDAOIF : mdRelationships)
    {
      if (mdRelationshipDAOIF instanceof MdTermRelationshipDAOIF)
      {
        rels.add(mdRelationshipDAOIF.definesType());
      }
    }

    return rels.toArray(new String[rels.size()]);
  }

  /**
   * (Currently Server-only) convenience method, returns all relationships that
   * this term is a valid child in.
   * 
   * @param termId
   * @return
   */
  public static String[] getAllChildRelationships(String termId)
  {
    Term term = (Term) Term.get(termId);

    MdTermDAOIF mdTerm = term.getMdTerm();
    List<MdRelationshipDAOIF> mdRelationships = mdTerm.getAllChildMdRelationships();

    ArrayList<String> rels = new ArrayList<String>();

    for (MdRelationshipDAOIF mdRelationshipDAOIF : mdRelationships)
    {
      if (mdRelationshipDAOIF instanceof MdTermRelationshipDAOIF)
      {
        rels.add(mdRelationshipDAOIF.definesType());
      }
    }

    return rels.toArray(new String[rels.size()]);
  }

}
