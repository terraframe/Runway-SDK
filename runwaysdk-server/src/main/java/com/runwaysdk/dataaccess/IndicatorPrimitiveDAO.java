package com.runwaysdk.dataaccess;

import java.util.Map;

import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.constants.AggregationFunctionInfo;
import com.runwaysdk.constants.IndicatorPrimitiveInfo;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.session.Session;

public class IndicatorPrimitiveDAO extends IndicatorElementDAO implements IndicatorPrimitiveDAOIF
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
  public IndicatorPrimitiveDAO create(Map<String, Attribute> _attributeMap, String _classType)
  {
    return new IndicatorPrimitiveDAO(_attributeMap, IndicatorPrimitiveInfo.CLASS);
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
  public static IndicatorPrimitiveDAO get(String _id)
  {
    return (IndicatorPrimitiveDAO) BusinessDAO.get(_id);
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
    EnumerationItemDAOIF function = this.getAggregateFunction();
    
    String functionName = function.getValue(AggregationFunctionInfo.NAME);

    if (functionName.equals(AggregationFunctionInfo.COUNT))
    {
      return Long.class.getSimpleName();
    }
    else if (functionName.equals(AggregationFunctionInfo.SUM) ||
        functionName.equals(AggregationFunctionInfo.MIN) ||
        functionName.equals(AggregationFunctionInfo.MAX) )
    {
      MdAttributePrimitiveDAOIF mdAttribute = this.getMdAttributePrimitive();
      return mdAttribute.javaType(false);
    }
    else // AggregationFunctionInfo.STDEV   AggregationFunctionInfo.AVG
    {
      MdAttributePrimitiveDAOIF mdAttribute = this.getMdAttributePrimitive();
      
      if (mdAttribute instanceof MdAttributeDecimalDAOIF)
      {
        return mdAttribute.javaClass().getName();
      }
      else
      {
        return Long.class.getSimpleName();
      }
    }
  }
  
  /**
   * @see IndicatorCompositeDAOIF#accept
   */
  public void accept(IndicatorVisitor _indicatorVisitor)
  {
    _indicatorVisitor.visit(this);
  }

  /**
   * @see IndicatorElementDAOIF#evalNonAggregateValue
   */
  public Object evalNonAggregateValue(MdAttributeIndicatorDAOIF _mdAttributeIndicator, ComponentDAOIF _componentDAOIF)
  {
    MdAttributePrimitiveDAOIF mdAttrPrim = this.getMdAttributePrimitive();
    
    AttributeIF attribute = _componentDAOIF.getAttributeIF(mdAttrPrim.definesAttribute());
    
    if (attribute instanceof AttributeNumericalIF)
    {
      AttributeNumericalIF attributeNumerical = (AttributeNumericalIF)attribute;

      return attributeNumerical.getTypeSafeValue();
    }
    else
    {
      return null;
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

  @Override
  public boolean isPercentage()
  {
    return false;
  }

}
