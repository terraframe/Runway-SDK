package com.runwaysdk.dataaccess;

public interface RatioElementDAOIF extends BusinessDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE = "ratio_element";

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
}
