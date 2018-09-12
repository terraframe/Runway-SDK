package com.runwaysdk.dataaccess;

public interface MdAttributeUUIDDAOIF extends MdAttributePrimitiveDAOIF
{
  /**
   * Name of the table used to store intances of this class.
   */
  public static final String TABLE    = "md_attribute_uuid";

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeUUIDDAO getBusinessDAO();

}
