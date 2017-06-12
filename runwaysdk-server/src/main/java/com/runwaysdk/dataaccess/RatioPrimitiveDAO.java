package com.runwaysdk.dataaccess;

import java.math.BigDecimal;
import java.util.Map;

import com.runwaysdk.constants.RatioInfo;
import com.runwaysdk.constants.RatioPrimitiveInfo;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeInteger;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
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
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable,
   *      java.util.String, ComponentDTOIF, Map)
   */
  public RatioPrimitiveDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new RatioPrimitiveDAO(attributeMap, RatioPrimitiveInfo.CLASS);
  }
  
  /**
   * Returns a new {@link RatioPrimitiveDAO}. Some attributes will contain
   * default values, as defined in the attribute metadata. Otherwise, the
   * attributes will be blank.
   * 
   * @return instance of {@link RatioDAO}>.
   */
  public static RatioPrimitiveDAO newInstance()
  {
    return (RatioPrimitiveDAO) BusinessDAO.newInstance(RatioPrimitiveInfo.CLASS);
  }
  
  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static RatioPrimitiveDAO get(String id)
  {
    return (RatioPrimitiveDAO) BusinessDAO.get(id);
  }
  
  
  @Override
  public void delete(boolean businessContext)
  {
    super.delete(businessContext);
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
  
  /**
   * @see RatioElementDAOIF#javaType
   */
  public String javaType()
  {
    MdAttributePrimitiveDAOIF mdAttribute = this.getMdAttributePrimitive();
    
    if (mdAttribute instanceof MdAttributeDecimalDAOIF)
    {
      return BigDecimal.class.getName();
    }
    else if (mdAttribute instanceof MdAttributeDoubleDAOIF)
    {
      return Double.class.getName();
    }
    else if (mdAttribute instanceof MdAttributeFloatDAOIF)
    {
      return Float.class.getName();
    }
    else if (mdAttribute instanceof MdAttributeLongDAOIF)
    {
      return Long.class.getName();
    }
    else
    {
      return Integer.class.getName();
    }
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
  
  
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public RatioPrimitiveDAO getBusinessDAO()
  {
    return (RatioPrimitiveDAO) super.getBusinessDAO();
  }

}
