package com.runwaysdk.dataaccess.graph;

import com.runwaysdk.dataaccess.ComponentDAO;
import com.runwaysdk.dataaccess.ComponentDAOIF;

public interface GraphObjectDAOIF extends ComponentDAOIF
{
  /**
   * Column name of the attribute that specifies the OID.
   */
  public static final String ID_ATTRIBUTE = "oid";

  public Object getRID();
  
  /**
   * Returns the embedded {@link ComponentDAO} for the attribute of the given name.
   * 
   * @param attriubteName
   * @return
   */
  public ComponentDAO getEmbeddedComponentDAO(String attributeName);
}
