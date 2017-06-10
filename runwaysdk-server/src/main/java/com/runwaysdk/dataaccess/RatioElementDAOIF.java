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
}
