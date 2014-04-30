package com.runwaysdk.system.ontology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.runwaysdk.business.Relationship;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.business.ontology.TermAndRel;
import com.runwaysdk.business.ontology.TermAndRelDTO;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.query.OIterator;

public class TermUtil extends TermUtilBase
{
  private static final long serialVersionUID = -1933843303;
  
  public TermUtil()
  {
    super();
  }
  
  public static com.runwaysdk.business.ontology.Term[] getAllAncestors(java.lang.String termId, java.lang.String[] relationshipTypes)
  {
    List<Term> terms = new ArrayList<Term>();
    Term parent = Term.get(termId);
    
    for (String relType : relationshipTypes) {
      terms.addAll(parent.getAllAncestors(relType).getAll());
    }
    
    return terms.toArray(new Term[terms.size()]);
  }
  
  public static com.runwaysdk.business.ontology.Term[] getAllDescendants(java.lang.String termId, java.lang.String[] relationshipTypes)
  {
    List<Term> terms = new ArrayList<Term>();
    Term parent = Term.get(termId);
    
    for (String relType : relationshipTypes) {
      terms.addAll(parent.getAllDescendants(relType).getAll());
    }
    
    return terms.toArray(new Term[terms.size()]);
  }
  
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
  
}
