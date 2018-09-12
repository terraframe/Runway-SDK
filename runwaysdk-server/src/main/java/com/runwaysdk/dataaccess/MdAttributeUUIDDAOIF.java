package com.runwaysdk.dataaccess;

import com.runwaysdk.dataaccess.metadata.MdAttributeUUIDDAO;

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


  /**
   * Precondition: assumes this character attribute is an OID. The collection of
   * <code>AttributeDAO</code> objects do not have their containing reference updated to
   * the returned <code>MdAttributeReferenceDAO</code> 
   */
  public MdAttributeReferenceDAOIF convertToReference();
  
  /**
   * This is used by the query API to allow for parent ids and child ids of relationships to
   * be used in queries.
   * 
   * Precondition: assumes this character attribute is an OID. The collection of
   * <code>AttributeDAO</code> objects do not have their containing reference updated to
   * the returned <code>MdAttributeReferenceDAO</code> 
   * 
   * @param the code>MdBusinessDAOIF</code> of the referenced type in the relationship.
   */
  public MdAttributeReferenceDAOIF convertToReference(MdBusinessDAOIF mdReferenecedBusinessDAOIF);
}
