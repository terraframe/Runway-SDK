package com.runwaysdk.constants.graph;

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.Constants;

public interface GraphClassInfo extends ComponentInfo
{
  /**
   * This class, unlike other CLASS constants, this does <i>not</i>
   * correlate directly with the database. This class is extended by generated
   * classes (which do have database counterparts), but {@link GraphClass}
   * exists only as part of the bridge between layers.
   */
  public static final String CLASS = Constants.SYSTEM_BUSINESS_PACKAGE+".GraphClass";
  
  // There is no class defined called {@link GraphClass} defined, so the existing architecture breaks
  // down here a bit. Therefore, no value is defined here and {@link GraphClass#ID_VALUE}
  // public static final String ID_VALUE          = "";
}
