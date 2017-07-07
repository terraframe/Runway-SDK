package com.runwaysdk.dataaccess;

public interface MdAttributeIndicatorDAOIF extends MdAttributeConcreteDAOIF
{   
  /**
   * Returns the {@link IndicatorElementDAOIF} object.
   * 
   * @return the {@link IndicatorElementDAOIF} object.
   */
  public IndicatorElementDAOIF getIndicator();
}
