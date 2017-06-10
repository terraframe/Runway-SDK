package com.runwaysdk.dataaccess;

public interface RatioDAOIF extends RatioElementDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE                   = "ratio";
  
  
  public static final String OPERATOR_COLUMN         = "math_operator";
  
  /**
   * Returns the {@link RatioElementDAOIF} of the left hand operand.
   * 
   * @return the {@link RatioElementDAOIF} of the left hand operand.
   */
  public RatioElementDAOIF getLeftOperand();
  
  /**
   * Returns the operator enumeration and assumes it is not null (precondition).
   * 
   * @return the operator enumeration and assumes it is not null (precondition).
   */
  public EnumerationItemDAOIF getOperator();
  
  /**
   * Returns the {@link RatioElementDAOIF} of the left hand operand.
   * 
   * @return the {@link RatioElementDAOIF} of the left hand operand.
   */
  public RatioElementDAOIF getRightOperand();
}
