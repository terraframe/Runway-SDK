package com.runwaysdk.dataaccess;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.BiFunction;

import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.constants.IndicatorCompositeInfo;
import com.runwaysdk.constants.MathOperatorInfo;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.metadata.InvalidIndicatorDefinition;

public class IndicatorCompositeDAO extends IndicatorElementDAO implements IndicatorCompositeDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 7013814284000552548L;

  public IndicatorCompositeDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public IndicatorCompositeDAO()
  {
    super();
  }
  
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable,
   *      java.util.String, ComponentDTOIF, Map)
   */
  public IndicatorCompositeDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new IndicatorCompositeDAO(attributeMap, IndicatorCompositeInfo.CLASS);
  }
  
  /**
   * Returns a new {@link IndicatorCompositeDAO}. Some attributes will contain
   * default values, as defined in the attribute metadata. Otherwise, the
   * attributes will be blank.
   * 
   * @return instance of {@link IndicatorCompositeDAO}>.
   */
  public static IndicatorCompositeDAO newInstance()
  {
    return (IndicatorCompositeDAO) BusinessDAO.newInstance(IndicatorCompositeInfo.CLASS);
  }
  
  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static IndicatorCompositeDAOIF get(String id)
  {
    return (IndicatorCompositeDAOIF) BusinessDAO.get(id);
  }
  

  /**
   * @see IndicatorCompositeDAOIF#evalNonAggregateValue
   * 
   * @return the object value of the attribute referenced by this {@link IndicatorPrimitiveDAO}
   */
  public Object evalNonAggregateValue(ComponentDAOIF _componentDAOIF)
  {
//    IndicatorElementDAOIF leftOperand = this.getLeftOperand();
//    IndicatorElementDAOIF rightOperand = this.getRightOperand();
//    
//    EnumerationItemDAOIF operator = this.getOperator();
//    
//    Object leftObjectValue = leftOperand.evalNonAggregateValue(_componentDAOIF);
//    Object rightObjectValue = rightOperand.evalNonAggregateValue(_componentDAOIF);
//    
//    BiFunction<String, String,String> bi = (x, y) -> {      
//      return x + y;
//    };
//    
//    BiFunction<Integer, Integer, Number> bi2 = (x, y) -> {      
//      return x / y;
//    };    
//
//    
//    if (leftObjectValue instanceof Integer)
//    {
//      Integer leftIntValue = (Integer)leftObjectValue;
//
//      if (rightObjectValue instanceof Integer)
//      {
//        Integer rightIntValue = (Integer)rightObjectValue;
//        
//      }
//    }
    
    Integer returnVal = 0;
    
    return returnVal;
  }
  
  @Override
  public void delete(boolean businessContext)
  {
    // Delete this from the database so that the reference checks on the operands do not prevent
    // them from being deleted because they reference this object.
    super.delete(businessContext);
    
    // Delete the operands
    this.getLeftOperand().getBusinessDAO().delete();
    
    this.getRightOperand().getBusinessDAO().delete();
  }
  
  
  /**
   * @see IndicatorCompositeDAOIF#getLeftOperand
   */
  public IndicatorElementDAOIF getLeftOperand()
  {
    AttributeReferenceIF attributeReferenceIF = (AttributeReferenceIF)this.getAttributeIF(IndicatorCompositeInfo.LEFT_OPERAND);
    
    return (IndicatorElementDAOIF)attributeReferenceIF.dereference();
  }
  
  /**
   * @see IndicatorCompositeDAOIF#getOperator
   */
  public EnumerationItemDAOIF getOperator()
  {
    AttributeEnumerationIF attributeEnumerationIF = (AttributeEnumerationIF)this.getAttributeIF(IndicatorCompositeInfo.OPERATOR);
    
    EnumerationItemDAOIF[] enumerationItemArray = attributeEnumerationIF.dereference();
    
    return enumerationItemArray[0];
  }
  
  /**
   * @see IndicatorCompositeDAOIF#getRightOperand
   */
  public IndicatorElementDAOIF getRightOperand()
  {
    AttributeReferenceIF attributeReferenceIF = (AttributeReferenceIF)this.getAttributeIF(IndicatorCompositeInfo.RIGHT_OPERAND);
    
    return (IndicatorElementDAOIF)attributeReferenceIF.dereference();
  }
  
  /**
   * @see IndicatorElementDAOIF#getLocalizedLabel
   */
  public String getLocalizedLabel()
  {
    String localizedLabel = "";
    
    IndicatorElementDAOIF leftOperand = getLeftOperand();
    EnumerationItemDAOIF operator = this.getOperator();
    IndicatorElementDAOIF rightOperand = getRightOperand();
    
    localizedLabel += "("+leftOperand.getLocalizedLabel()+")";

    localizedLabel += " "+operator.getValue(MathOperatorInfo.OPERATOR_SYMBOL)+" ";
      
    localizedLabel += "("+rightOperand.getLocalizedLabel()+")";
    
    return localizedLabel;
  }

  /**
   * Validates this metadata object.
   * 
   * @throws {@link InvalidIndicatorDefinition}
   *           when this MetaData object is not valid.
   */
  protected void validate()
  {
    super.validate();
    
    IndicatorElementDAOIF leftOperand = this.getLeftOperand();
    
    IndicatorElementDAOIF rightOperand = this.getRightOperand();
    
    boolean isValid = false;
    // For now, we are only supporting numerical operands and not nested operands, but that should change
    // in the future
    if((leftOperand instanceof IndicatorPrimitiveDAOIF && ((IndicatorPrimitiveDAOIF)leftOperand).getMdAttributePrimitive() instanceof MdAttributeNumberDAOIF)
        &&
        (rightOperand instanceof IndicatorPrimitiveDAOIF && ((IndicatorPrimitiveDAOIF)rightOperand).getMdAttributePrimitive() instanceof MdAttributeNumberDAOIF))
    {
      isValid = true;
    }    
    else if ((leftOperand instanceof IndicatorPrimitiveDAOIF && ((IndicatorPrimitiveDAOIF)leftOperand).getMdAttributePrimitive() instanceof MdAttributeBooleanDAOIF)
        &&
        (rightOperand instanceof IndicatorPrimitiveDAOIF && ((IndicatorPrimitiveDAOIF)rightOperand).getMdAttributePrimitive() instanceof MdAttributeBooleanDAOIF))
    {
      isValid = true;
    }
    
    if (isValid == false)
    {
      String localizedLabel = this.getLocalizedLabel();
      
      String devMessage = "The indicator attribute definition is invalid ["+localizedLabel+"]. "+
      "The left and right operands must both be either a number or a boolean.";

      throw new InvalidIndicatorDefinition(devMessage, localizedLabel);
    }
  }
  
  /**
   * @see IndicatorElementDAOIF#javaType
   */
  public String javaType()
  { 
    String leftOperandType = this.getLeftOperand().javaType();
    
    String rightOperandType = this.getRightOperand().javaType();
    
    if (leftOperandType.equals(Boolean.class.getName()) && 
        rightOperandType.equals(Boolean.class.getName()))
    {
      return Boolean.class.getName();
    }
    
    
    if(leftOperandType.equals(BigDecimal.class.getName()) || 
       rightOperandType.equals(BigDecimal.class.getName()))
    {
      return BigDecimal.class.getName();
    }
    else if (leftOperandType.equals(Double.class.getName()) || 
        rightOperandType.equals(Double.class.getName()))
    {
      return Double.class.getName();
    }
    else if (leftOperandType.equals(Float.class.getName()) || 
        rightOperandType.equals(Float.class.getName()))
    {
      return Float.class.getName();
    }
    else if (leftOperandType.equals(Long.class.getName()) || 
        rightOperandType.equals(Long.class.getName()))
    {
      return Long.class.getName();
    }
    else
    {
      return Integer.class.getName();
    }
  }
  
  /**
   * @see IndicatorCompositeDAOIF#accept
   */
  public void accept(IndicatorVisitor indicatorVisitor)
  {
    indicatorVisitor.visit(this);
    
    IndicatorElementDAOIF leftOperand = getLeftOperand();
    leftOperand.accept(indicatorVisitor);

    IndicatorElementDAOIF rightOperand = getRightOperand();
    rightOperand.accept(indicatorVisitor);
  }
  
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public IndicatorCompositeDAO getBusinessDAO()
  {
    return (IndicatorCompositeDAO) super.getBusinessDAO();
  }
  
  public String toString()
  {
    return this.getLocalizedLabel();
  }
}


