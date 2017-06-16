package com.runwaysdk.dataaccess;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.dataaccess.IndicatorElementDAO.IndicatorVisitor;

public interface IndicatorElementDAOIF extends BusinessDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE = "indicator_element";

  /**
   * Returns a localized readable label of the ratio.
   * 
   * @return a localized readable label of the ratio.
   */
  public String getLocalizedLabel();
  
  /**
   * Returns the java type that is the return type of the ration equation.
   * 
   * @return the java type that is the return type of the ration equation.
   */
  public String javaType();
  
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public IndicatorElementDAO getBusinessDAO();

  /**
   * A visitor that visits the indicator composite object structure.
   * 
   * @param indicatorVisitor
   */
  public void accept(IndicatorVisitor _indicatorVisitor);

  /**
   * Returns the unaggregated value of the indicator for the given {@link ComponentDAOIF}.
   * 
   * @return the unaggregated value of the indicator for the given {@link ComponentDAOIF}.
   */
  public Object evalNonAggregateValue(ComponentDAOIF _componentDAOIF);
}
