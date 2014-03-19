package com.runwaysdk.system.metadata.ontology;

import java.util.List;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.business.ontology.TermAndRel;

public class TermFacade extends TermFacadeBase
{
  private static final long serialVersionUID = 1728175544;
  
  public TermFacade()
  {
    super();
  }
  
  public static java.lang.String[] getAllAncestors(java.lang.String termId, java.lang.String relationshipType)
  {
    Term term = (Term) Term.get(termId);
    
    List<Term> terms = term.getAllAncestors(relationshipType);
    
    String[] sTerms = new String[terms.size()];
    for (int i = 0; i < terms.size(); ++i) {
      sTerms[i] = terms.get(i).getId();
    }
    return sTerms;
  }
  
  public static java.lang.String[] getAllAncestorsAndRels(java.lang.String termId)
  {
    Term term = (Term) Term.get(termId);
    
    List<TermAndRel> terms = term.getAllAncestors();
    
    String[] sTerms = new String[terms.size()];
    for (int i = 0; i < terms.size(); ++i) {
      sTerms[i] = terms.get(i).toString();
    }
    return sTerms;
  }
  
  public static java.lang.String[] getAllDescendants(java.lang.String termId, java.lang.String relationshipType)
  {
    Term term = (Term) Term.get(termId);
    
    List<Term> terms = term.getAllDescendants(relationshipType);
    
    String[] sTerms = new String[terms.size()];
    for (int i = 0; i < terms.size(); ++i) {
      sTerms[i] = terms.get(i).getId();
    }
    return sTerms;
  }
  
  public static java.lang.String[] getAllDescendantsAndRels(java.lang.String termId)
  {
    Term term = (Term) Term.get(termId);
    
    List<TermAndRel> terms = term.getAllDescendants();
    
    String[] sTerms = new String[terms.size()];
    for (int i = 0; i < terms.size(); ++i) {
      sTerms[i] = terms.get(i).toString();
    }
    return sTerms;
  }
  
}
