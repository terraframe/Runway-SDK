package com.runwaysdk.dataaccess;

public interface IndicatorPrimitiveDAOIF extends IndicatorElementDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE                   = "indicator_primitive";
  
  /**
   * Returns the {@link MdAttributePrimitiveDAOIF} that defines the primitive attribute used in the ratio equation.
   * 
   * @return the {@link MdAttributePrimitiveDAOIF} that defines the primitive attribute used in the ratio equation.
   */
  public MdAttributePrimitiveDAOIF getMdAttributePrimitive();
  
  /**
   * Returns the function enumeration item and assumes it is not null (precondition).
   * 
   * @return the function enumeration item and assumes it is not null (precondition).
   */
  public EnumerationItemDAOIF getAggregateFunction();
  
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public IndicatorPrimitiveDAO getBusinessDAO();
}
