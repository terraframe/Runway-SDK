/**
 * 
 */
package com.runwaysdk.dataaccess;

import java.util.List;

public interface TermAttributeDAOIF
{
  public void addAttributeRoot(BusinessDAO term, Boolean selectable);

  public List<RelationshipDAOIF> getAllAttributeRoots();

  public String getAttributeRootRelationshipType();
  
  public MdTermDAOIF getReferenceMdBusinessDAO();
}
