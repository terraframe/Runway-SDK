package com.runwaysdk.dataaccess;

public interface MdAttributeRatioDAOIF extends MdAttributeConcreteDAOIF
{   
  /**
   * Return the {@link MdAttributeConcreteDAOIF} referenced in the left operand.
   * 
   * @return the {@link MdAttributeConcreteDAOIF} referenced in the left operand.
   */
  public MdAttributeConcreteDAOIF getLeftOperand();
  
  /**
   * Returns the operator enumeration.
   * 
   * @return the operator enumeration.
   */
  public EnumerationItemDAOIF getOperator();

  /**
   * Return the {@link MdAttributeConcreteDAOIF} referenced in the right operand.
   * 
   * @return the {@link MdAttributeConcreteDAOIF} referenced in the right operand.
   */
  public MdAttributeConcreteDAOIF getRightOperand();
  
}
