package com.runwaysdk.dataaccess;


/**
 * Interface for Primitive Attributes
 * 
 * @author nathan
 *
 */
public interface AttributeNumericalIF extends AttributeIF
{

  /**
   * Returns the Java primitive type of the value.
   * 
   * @return the Java primitive type of the value.
   */
  public Object getTypeSafeValue();
  
}
