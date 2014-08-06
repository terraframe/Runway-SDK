package com.runwaysdk.system.ontology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.runwaysdk.business.Relationship;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.business.ontology.TermAndRel;
import com.runwaysdk.business.ontology.TermRelationship;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdTermDAOIF;
import com.runwaysdk.dataaccess.MdTermRelationshipDAOIF;
import com.runwaysdk.dataaccess.io.TimeFormat;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.ontology.io.XMLTermExporter;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.metadata.MdEntity;
import com.runwaysdk.system.ontology.io.TermFileFormat;

public class TermUtil extends TermUtilBase
{
  private static final long serialVersionUID = -1933843303;
  
  public TermUtil()
  {
    super();
  }
  
//  public static com.runwaysdk.business.ontology.Term[] getOrderedAllAncestors(java.lang.String termId, java.lang.String[] relationshipTypes)
//  {
//    // termId, depth, displayLabel 
//    
//    // Term subtypes all have a generated DisplayLabel
//    String termType = Term.get(termId).getMdTerm().definesType();
//    String displayLabelTable = MdEntity.getMdEntity(termType + "DisplayLabel").getTableName();
//    
//    MdEntityDAOIF termMd = MdEntityDAO.getMdEntityDAO(termType);
//    String termTable = termMd.getTableName();
//    
//    MdEntityDAOIF mdTypeMd = MdEntityDAO.getMdEntityDAO(MdTypeInfo.CLASS);
//    String mdTypeTable = mdTypeMd.getTableName();
//    
//    String termRelTable = MdEntityDAO.getMdEntityDAO(TermRelationship.CLASS).getTableName();
//    
//    String sql = "";
//    
//    sql += "WITH RECURSIVE term_flags AS( \n";
////    sql += "SELECT t.id, dl.default_locale \n";
//    sql += "SELECT term.id \n";
//    sql += "FROM " + termTable + " term, " + termRelTable + " termRel \n";
////    sql += "INNER JOIN " + displayLabelTable + " dl\n";
////    sql += "ON dl." + "id" + "=t." + "display_label";
//    sql += "WHERE  termRel." + RelationshipInfo.PARENT_ID + " = term.id \n";
//    sql += ") \n";
//    sql += ", recursive_rollup AS ( \n";
//    sql += " SELECT * ,0 as depth \n";
//    sql += "   \n";
//    sql += "  FROM term_flags \n";
//    sql += " UNION \n";
//    sql += " SELECT * \n";
//    sql += " FROM recursive_rollup a,  term_flags b \n";
//    sql += " WHERE a.child_type = b.parent_type \n";
//    sql += ") \n";
//    
//  }
  
  /**
   * MdMethod, delegates to the ontology strategy.
   * 
   * @param termId
   * @param relationshipTypes
   * @return
   */
  public static com.runwaysdk.business.ontology.Term[] getAllAncestors(java.lang.String termId, java.lang.String[] relationshipTypes)
  {
    List<Term> terms = new ArrayList<Term>();
    Term parent = Term.get(termId);
    
    for (String relType : relationshipTypes) {
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
  public static com.runwaysdk.business.ontology.Term[] getAllDescendants(java.lang.String termId, java.lang.String[] relationshipTypes)
  {
    List<Term> terms = new ArrayList<Term>();
    Term parent = Term.get(termId);
    
    for (String relType : relationshipTypes) {
      terms.addAll(parent.getAllDescendants(relType).getAll());
    }
    
    return terms.toArray(new Term[terms.size()]);
  }
  
  /**
   * MdMethod, returns the direct ancestors of the given term, with the given relationship type. The TermAndRels are sorted by display label. 
   * 
   * @param termId
   * @param relationshipTypes
   * @return
   */
  public static com.runwaysdk.business.ontology.TermAndRel[] getDirectAncestors(java.lang.String termId, java.lang.String[] relationshipTypes)
  {
    List<TermAndRel> tnrs = new ArrayList<TermAndRel>();
    Term parent = Term.get(termId);
    
    for (String relType : relationshipTypes) {
      OIterator<? extends Relationship> parentRels = parent.getParentRelationships(relType);
      
      for (Relationship rel : parentRels) {
        tnrs.add(new TermAndRel((Term) rel.getChild(), relType, rel.getId()));
      }
    }
    
    // Sort by displayLabel
    Collections.sort(tnrs, new Comparator<TermAndRel>(){
      public int compare(TermAndRel t1, TermAndRel t2) {
        return t1.getTerm().getDisplayLabel().getValue().compareToIgnoreCase(t2.getTerm().getDisplayLabel().getValue());
      }
    });
    
    return tnrs.toArray(new TermAndRel[tnrs.size()]);
  }
  
  /**
   * MdMethod, returns the direct descendants of the given term, with the given relationship type. The TermAndRels are sorted by display label. 
   * 
   * @param termId
   * @param relationshipTypes
   * @return
   */
  public static com.runwaysdk.business.ontology.TermAndRel[] getDirectDescendants(java.lang.String termId, java.lang.String[] relationshipTypes)
  {
    List<TermAndRel> tnrs = new ArrayList<TermAndRel>();
    Term parent = Term.get(termId);
    
    for (String relType : relationshipTypes) {
      OIterator<? extends Relationship> childRels = parent.getChildRelationships(relType);
      
      for (Relationship rel : childRels) {
        tnrs.add(new TermAndRel((Term) rel.getChild(), relType, rel.getId()));
      }
    }
    
    // Sort by displayLabel
    Collections.sort(tnrs, new Comparator<TermAndRel>(){
      public int compare(TermAndRel t1, TermAndRel t2) {
        return t1.getTerm().getDisplayLabel().getValue().compareToIgnoreCase(t2.getTerm().getDisplayLabel().getValue());
      }
    });
    
    return tnrs.toArray(new TermAndRel[tnrs.size()]);
  }
  
  /**
   * MdMethod
   * 
   * @param childId
   * @param oldParentId
   * @param oldRelType
   * @param newParentId
   * @param newRelType
   * @return
   */
  @Transaction
  public static com.runwaysdk.business.Relationship addAndRemoveLink(java.lang.String childId, java.lang.String oldParentId, java.lang.String oldRelType, java.lang.String newParentId, java.lang.String newRelType)
  {
//    Term child = (Term) Term.get(childId);
//    Term oldParent = (Term) Term.get(oldParentId);
//    Term newParent = (Term) Term.get(newParentId);
//    
//    return child.addAndRemoveLink(oldParent, oldRelType, newParent, newRelType);
    
    Relationship retRel = addLink(childId, newParentId, newRelType);
    removeLink(childId, oldParentId, oldRelType);
    return retRel;
  }
  
  /**
   * MdMethod
   * 
   * @param childId
   * @param parentId
   * @param relationshipType
   * @return
   */
  @Transaction
  public static com.runwaysdk.business.Relationship addLink(java.lang.String childId, java.lang.String parentId, java.lang.String relationshipType)
  {
    Term child = (Term) Term.get(childId);
    Term parent = (Term) Term.get(parentId);
    
    return child.addLink(parent, relationshipType);
  }
  
  /**
   * MdMethod
   * 
   * @param childId
   * @param parentId
   * @param relationshipType
   */
  @Transaction
  public static void removeLink(java.lang.String childId, java.lang.String parentId, java.lang.String relationshipType)
  {
    Term child = (Term) Term.get(childId);
    Term parent = (Term) Term.get(parentId);
    
    child.removeLink(parent, relationshipType);
  }
  
  /**
   * MdMethod
   * Exports the term to the output stream.
   * 
   * @param outputStream
   * @param parentId
   * @param exportParent
   * @param format
   */
  public static void exportTerm(java.io.OutputStream outputStream, java.lang.String parentId, java.lang.Boolean exportParent, com.runwaysdk.system.ontology.io.TermFileFormat format)
  {
    if (format == TermFileFormat.XML) {
      XMLTermExporter exporter = new XMLTermExporter(outputStream);
      exporter.exportAll(Term.get(parentId), exportParent);
    }
//    else if (format == TermFileFormat.EXCEL) {
//      cr.exportExcelFile();
//    }
    else {
      throw new UnsupportedOperationException("Unsupported TermFileFormat.");
    }
  }
  
  /**
   * MdMethod
   * Returns a timestamp for a new schema file.
   * 
   * @return
   */
  public static java.lang.String getTimestamp()
  {
    long time = System.currentTimeMillis();
    String format = new TimeFormat(time).format();
    
    return format;
  }
  
  /**
   * (Currently Server-only) convenience method, fetches all direct descendants of all valid relationship types.
   * 
   * @param termId
   * @return
   */
//   public static TermAndRel[] getDirectDescendants(String termId) {
//     return getDirectDescendants(termId, getAllParentRelationships(termId));
//   }
  
  /**
   * (Currently Server-only) convenience method, returns all relationships that this term is a valid parent in.
   * 
   * @param termId
   * @return
   */
  public static String[] getAllParentRelationships(String termId) {
    Term term = (Term) Term.get(termId);
    
    MdTermDAOIF mdTerm = term.getMdTerm();
    List<MdRelationshipDAOIF> mdRelationships = mdTerm.getAllParentMdRelationships();
    
    ArrayList<String> rels = new ArrayList<String>();
    
    for(MdRelationshipDAOIF mdRelationshipDAOIF : mdRelationships)
    {
      if(mdRelationshipDAOIF instanceof MdTermRelationshipDAOIF)
      {
        rels.add(mdRelationshipDAOIF.definesType());
      }
    }
    
    return rels.toArray(new String[rels.size()]);
  }
  
  /**
   * (Currently Server-only) convenience method, returns all relationships that this term is a valid child in.
   * 
   * @param termId
   * @return
   */
  public static String[] getAllChildRelationships(String termId) {
    Term term = (Term) Term.get(termId);
    
    MdTermDAOIF mdTerm = term.getMdTerm();
    List<MdRelationshipDAOIF> mdRelationships = mdTerm.getAllChildMdRelationships();
    
    ArrayList<String> rels = new ArrayList<String>();
    
    for(MdRelationshipDAOIF mdRelationshipDAOIF : mdRelationships)
    {
      if(mdRelationshipDAOIF instanceof MdTermRelationshipDAOIF)
      {
        rels.add(mdRelationshipDAOIF.definesType());
      }
    }
    
    return rels.toArray(new String[rels.size()]);
  }
  
}
