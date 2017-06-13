package com.runwaysdk.dataaccess;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.constants.IndicatorCompositeInfo;
import com.runwaysdk.constants.MathOperatorInfo;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.metadata.InvalidIndicatorDefinition;

public class IndicatorDAO extends BusinessDAO implements IndicatorDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 7013814284000552548L;

  public IndicatorDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public IndicatorDAO()
  {
    super();
  }
  
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable,
   *      java.util.String, ComponentDTOIF, Map)
   */
  public IndicatorDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new IndicatorDAO(attributeMap, IndicatorCompositeInfo.CLASS);
  }
  
  /**
   * Returns a new {@link IndicatorDAO}. Some attributes will contain
   * default values, as defined in the attribute metadata. Otherwise, the
   * attributes will be blank.
   * 
   * @return instance of {@link IndicatorDAO}>.
   */
  public static IndicatorDAO newInstance()
  {
    return (IndicatorDAO) BusinessDAO.newInstance(IndicatorCompositeInfo.CLASS);
  }
  
  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static IndicatorDAOIF get(String id)
  {
    return (IndicatorDAOIF) BusinessDAO.get(id);
  }
  
  
  @Override
  public void delete(boolean businessContext)
  {
    // Delete the operands
    this.getLeftOperand().getBusinessDAO().delete();
    
    this.getRightOperand().getBusinessDAO().delete();
    
    super.delete(businessContext);
  }
  
  
  /**
   * @see IndicatorDAOIF#getLeftOperand
   */
  public IndicatorElementDAOIF getLeftOperand()
  {
    AttributeReferenceIF attributeReferenceIF = (AttributeReferenceIF)this.getAttributeIF(IndicatorCompositeInfo.LEFT_OPERAND);
    
    return (IndicatorElementDAOIF)attributeReferenceIF.dereference();
  }
  
  /**
   * @see IndicatorDAOIF#getOperator
   */
  public EnumerationItemDAOIF getOperator()
  {
    AttributeEnumerationIF attributeEnumerationIF = (AttributeEnumerationIF)this.getAttributeIF(IndicatorCompositeInfo.OPERATOR);
    
    EnumerationItemDAOIF[] enumerationItemArray = attributeEnumerationIF.dereference();
    
    return enumerationItemArray[0];
  }
  
  /**
   * @see IndicatorDAOIF#getRightOperand
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
    
    if (isValid == false)
    {
      String localizedLabel = this.getLocalizedLabel();
      
      String devMessage = "The ratio attribute definition is invalid ["+localizedLabel+"]. "+
      "A valid ratio consists of a number left operand, and operator, and a number right operand.";

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
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public IndicatorDAO getBusinessDAO()
  {
    return (IndicatorDAO) super.getBusinessDAO();
  }
  
  public String toString()
  {
    return this.getLocalizedLabel();
  }
 
  public static class ClassRatioVisitor
  {
    /**
     * Key: The key of the {@link MdAttributeIndicatorDAOIF}
     * Object: {@link MdAttributeIndicatorDAOIF}
     */
    private Map<String, MdAttributeIndicatorDAOIF> attrRatioMap;
    
    public ClassRatioVisitor()
    {
      this.attrRatioMap = new HashMap<String, MdAttributeIndicatorDAOIF>();
    }
    
    public void addMdAttributeRatio(MdAttributeIndicatorDAOIF mdAttributeRatio)
    {
      this.attrRatioMap.put(mdAttributeRatio.getKey(), mdAttributeRatio);
    }
    
    public List<MdAttributeIndicatorDAOIF> getMdAttributeRatioList()
    {
      return this.attrRatioMap.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }
    
    public void visit(IndicatorDAOIF ratioDAOIF)
    {
      
    }
  }
}


