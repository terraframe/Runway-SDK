package com.runwaysdk.dataaccess;

import com.runwaysdk.dataaccess.metadata.MdAttributeLocalEmbeddedDAO;

public interface MdAttributeLocalEmbeddedDAOIF extends MdAttributeEmbeddedDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE   = "md_attribute_local_embedded";
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeLocalEmbeddedDAO getBusinessDAO();
}
