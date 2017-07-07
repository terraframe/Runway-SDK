package com.runwaysdk.dataaccess;

import com.runwaysdk.ComponentIF;

public interface ComponentDAOIF extends ComponentIF
{
  /**
   * Returns the attribute object of the given name.
   *
   * @param name
   * @return
   */
  public AttributeIF getAttributeIF(String name);
}
