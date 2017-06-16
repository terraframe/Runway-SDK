package com.runwaysdk.dataaccess;


public interface IndicatorCompositeDAOIF extends IndicatorElementDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE                   = "indicator";
  
  
  public static final String OPERATOR_COLUMN         = "math_operator";
  
  /**
   * Returns the {@link IndicatorElementDAOIF} of the left hand operand.
   * 
   * @return the {@link IndicatorElementDAOIF} of the left hand operand.
   */
  public IndicatorElementDAOIF getLeftOperand();
  
  /**
   * Returns the operator enumeration and assumes it is not null (precondition).
   * 
   * @return the operator enumeration and assumes it is not null (precondition).
   */
  public EnumerationItemDAOIF getOperator();
  
  /**
   * Returns the {@link IndicatorElementDAOIF} of the left hand operand.
   * 
   * @return the {@link IndicatorElementDAOIF} of the left hand operand.
   */
  public IndicatorElementDAOIF getRightOperand();
  
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public IndicatorCompositeDAO getBusinessDAO();
  
}
