package com.runwaysdk.dataaccess;

import java.math.BigDecimal;
import java.util.Map;

import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.constants.IndicatorCompositeInfo;
import com.runwaysdk.constants.IndicatorPrimitiveInfo;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.session.Session;

public class IndicatorPrimitiveDAO extends BusinessDAO implements IndicatorPrimitiveDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -6523351580284127511L;

  public IndicatorPrimitiveDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public IndicatorPrimitiveDAO()
  {
    super();
  }
  
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable,
   *      java.util.String, ComponentDTOIF, Map)
   */
  public IndicatorPrimitiveDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new IndicatorPrimitiveDAO(attributeMap, IndicatorPrimitiveInfo.CLASS);
  }
  
  /**
   * Returns a new {@link IndicatorPrimitiveDAO}. Some attributes will contain
   * default values, as defined in the attribute metadata. Otherwise, the
   * attributes will be blank.
   * 
   * @return instance of {@link RatioDAO}>.
   */
  public static IndicatorPrimitiveDAO newInstance()
  {
    return (IndicatorPrimitiveDAO) BusinessDAO.newInstance(IndicatorPrimitiveInfo.CLASS);
  }
  
  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static IndicatorPrimitiveDAO get(String id)
  {
    return (IndicatorPrimitiveDAO) BusinessDAO.get(id);
  }
  
  
  @Override
  public void delete(boolean businessContext)
  {
    super.delete(businessContext);
  }
  
  /**
   * @see IndicatorPrimitiveInfo#getMdAttributePrimitive
   */
  @Override
  public MdAttributePrimitiveDAOIF getMdAttributePrimitive()
  {
    AttributeReferenceIF attributeReferenceIF = (AttributeReferenceIF)this.getAttributeIF(IndicatorPrimitiveInfo.MD_ATTRIBUTE_PRIMITIVE);
    return (MdAttributePrimitiveDAOIF)attributeReferenceIF.dereference();
  }
  
  /**
   * @see IndicatorPrimitiveInfo#getFunction
   */
  public EnumerationItemDAOIF getAggregateFunction()
  {
    AttributeEnumerationIF attributeEnumerationIF = (AttributeEnumerationIF)this.getAttributeIF(IndicatorPrimitiveInfo.INDICATOR_FUNCTION);
    
    EnumerationItemDAOIF[] enumerationItemArray = attributeEnumerationIF.dereference();
    
    return enumerationItemArray[0];
  }
  
  /**
   * @see IndicatorElementDAOIF#javaType
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
   * @see IndicatorElementDAOIF#getLocalizedLabel
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
  public IndicatorPrimitiveDAO getBusinessDAO()
  {
    return (IndicatorPrimitiveDAO) super.getBusinessDAO();
  }

}
