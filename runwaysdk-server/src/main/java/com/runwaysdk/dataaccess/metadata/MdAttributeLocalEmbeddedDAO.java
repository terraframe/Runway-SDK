package com.runwaysdk.dataaccess.metadata;

import java.util.Map;

import com.runwaysdk.dataaccess.MdAttributeLocalEmbeddedDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public class MdAttributeLocalEmbeddedDAO extends MdAttributeEmbeddedDAO implements MdAttributeLocalEmbeddedDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -1850829906666660082L;

  public MdAttributeLocalEmbeddedDAO()
  {
    super();
  }
  
  public MdAttributeLocalEmbeddedDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }
  
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeLocalEmbeddedDAO getBusinessDAO()
  {
    return (MdAttributeLocalEmbeddedDAO) super.getBusinessDAO();
  }
}
