package com.runwaysdk.dataaccess;

import java.util.Map;

import com.runwaysdk.constants.MathOperatorInfo;
import com.runwaysdk.constants.RatioInfo;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.metadata.InvalidRatioDefinition;

public class RatioDAO extends BusinessDAO implements RatioDAOIF
{

  public RatioDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public RatioDAO()
  {
    super();
  }
  
  /**
   * @see RatioDAOIF#getLeftOperand
   */
  public RatioElementDAOIF getLeftOperand()
  {
    AttributeReferenceIF attributeReferenceIF = (AttributeReferenceIF)this.getAttributeIF(RatioInfo.LEFT_OPERAND);
    
    return (RatioElementDAOIF)attributeReferenceIF.dereference();
  }
  
  /**
   * @see RatioDAOIF#getOperator
   */
  public EnumerationItemDAOIF getOperator()
  {
    AttributeEnumerationIF attributeEnumerationIF = (AttributeEnumerationIF)this.getAttributeIF(RatioInfo.OPERATOR);
    
    EnumerationItemDAOIF[] enumerationItemArray = attributeEnumerationIF.dereference();
    
    return enumerationItemArray[0];
  }
  
  /**
   * @see RatioDAOIF#getRightOperand
   */
  public RatioElementDAOIF getRightOperand()
  {
    AttributeReferenceIF attributeReferenceIF = (AttributeReferenceIF)this.getAttributeIF(RatioInfo.RIGHT_OPERAND);
    
    return (RatioElementDAOIF)attributeReferenceIF.dereference();
  }
  
  /**
   * @see RatioElementDAOIF#getLocalizedLabel
   */
  public String getLocalizedLabel()
  {
    String localizedLabel = "";
    
    RatioElementDAOIF leftOperand = getLeftOperand();
    EnumerationItemDAOIF operator = this.getOperator();
    RatioElementDAOIF rightOperand = getRightOperand();
    
    localizedLabel += "("+leftOperand.getLocalizedLabel()+")";

    localizedLabel += " "+operator.getValue(MathOperatorInfo.OPERATOR_SYMBOL)+" ";
      
    localizedLabel += "("+rightOperand.getLocalizedLabel()+")";
    
    return localizedLabel;
  }

  /**
   * Validates this metadata object.
   * 
   * @throws {@link InvalidRatioDefinition}
   *           when this MetaData object is not valid.
   */
  protected void validate()
  {
    super.validate();
    
    RatioElementDAOIF leftOperand = getLeftOperand();
    
    RatioElementDAOIF rightOperand = getRightOperand();
    
    boolean isValid = false;
    // For now, we are only supporting numerical operands and not nested operands, but that should change
    // in the future
    if((leftOperand instanceof RatioPrimitiveDAOIF && ((RatioPrimitiveDAOIF)leftOperand).getMdAttributePrimitive() instanceof MdAttributeNumberDAOIF)
        &&
        (rightOperand instanceof RatioPrimitiveDAOIF && ((RatioPrimitiveDAOIF)rightOperand).getMdAttributePrimitive() instanceof MdAttributeNumberDAOIF))
    {
      isValid = true;
    }    
    
    if (isValid == false)
    {
      String localizedLabel = this.getLocalizedLabel();
      
      String devMessage = "The ratio attribute definition is invalid ["+localizedLabel+"]. "+
      "A valid ratio consists of a number left operand, and operator, and a number right operand.";

      throw new InvalidRatioDefinition(devMessage, localizedLabel);
    }
  }
  
  
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public RatioDAO getBusinessDAO()
  {
    return (RatioDAO) super.getBusinessDAO();
  }
  
  public String toString()
  {
    return this.getLocalizedLabel();
  }
 
  
}
