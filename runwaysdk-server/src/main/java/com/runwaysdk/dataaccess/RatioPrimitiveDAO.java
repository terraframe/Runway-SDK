package com.runwaysdk.dataaccess;

import java.util.Map;

import com.runwaysdk.constants.RatioPrimitiveInfo;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeInteger;
import com.runwaysdk.session.Session;

public class RatioPrimitiveDAO extends BusinessDAO implements RatioPrimitiveDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -6523351580284127511L;

  public RatioPrimitiveDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public RatioPrimitiveDAO()
  {
    super();
  }
  
  /**
   * @see RatioPrimitiveInfo#MD_ATTRIBUTE_PRIMITIVE
   */
  @Override
  public MdAttributePrimitiveDAOIF getMdAttributePrimitive()
  {
    AttributeReferenceIF attributeReferenceIF = (AttributeReferenceIF)this.getAttributeIF(RatioPrimitiveInfo.MD_ATTRIBUTE_PRIMITIVE);
    return (MdAttributePrimitiveDAOIF)attributeReferenceIF.dereference();
  }

  @Override
  public String getColumnName()
  {
    return this.getAttributeIF(RatioPrimitiveInfo.COLUMN_NAME).getValue();
  }

  @Override
  public int getAttributeSequence()
  {
    AttributeInteger attributeInteger = (AttributeInteger)this.getAttribute(RatioPrimitiveInfo.SEQUENCE);
    return Integer.parseInt(attributeInteger.getValue());
  }
  
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public RatioPrimitiveDAO getBusinessDAO()
  {
    return (RatioPrimitiveDAO) super.getBusinessDAO();
  }


  /**
   * @see RatioElementDAOIF#getLocalizedLabel
   */
  public String getLocalizedLabel()
  {
    return "["+this.getMdAttributePrimitive().getDisplayLabel(Session.getCurrentLocale())+"]";
  }
  
  public String toString()
  {
    return this.getLocalizedLabel();
  }
}
