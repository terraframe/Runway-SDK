package com.runwaysdk.dataaccess;

import com.runwaysdk.dataaccess.metadata.MdAttributeEmbeddedDAO;

public interface MdAttributeEmbeddedDAOIF extends MdAttributeConcreteDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE   = "md_attribute_local_embedded";
  
  /**
   *Returns the <code>MdClassDAOIF</code> that defines the class that defines the attributes on the embedded objects.
   *
   * @return the <code>MdClassDAOIF</code> that defines the class used to store the values of the struct attribute.
   */
  public MdClassDAOIF getEmbeddedMdClassDAOIF();

  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeEmbeddedDAO getBusinessDAO();
}
