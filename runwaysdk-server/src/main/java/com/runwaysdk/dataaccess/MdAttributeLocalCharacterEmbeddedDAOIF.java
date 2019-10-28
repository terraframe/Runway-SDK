package com.runwaysdk.dataaccess;

import com.runwaysdk.dataaccess.metadata.MdAttributeLocalCharacterEmbeddedDAO;

public interface MdAttributeLocalCharacterEmbeddedDAOIF extends MdAttributeLocalEmbeddedDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE   = "md_attribute_local_character_e";
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeLocalCharacterEmbeddedDAO getBusinessDAO();
}
