package com.runwaysdk.dataaccess;

public interface RatioPrimitiveDAOIF extends RatioElementDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE                   = "ratio_primitive";
  
  /**
   * The column name on the defining type that stores the reference to the object instance. This is used to retrieve
   * the attribute defined on this ratio on that object.
   */
  public static final String COLUMN_NAME             = "columnName";
  
  /**
   * Returns the {@link MdAttributePrimitiveDAOIF} that defines the primitive attribute used in the ratio equation.
   * 
   * @return the {@link MdAttributePrimitiveDAOIF} that defines the primitive attribute used in the ratio equation.
   */
  public MdAttributePrimitiveDAOIF getMdAttributePrimitive();
  
  /**
   * Returns the column that stores the references of the objects with the desired attribute.
   * 
   * @return the column that stores the references of the objects with the desired attribute.
   */
  public String getColumnName();
  
  /**
   * Returns the sequence number of this attribute in the ratio equation.
   * 
   * @return the sequence number of this attribute in the ratio equation.
   */
  public int getAttributeSequence();
}
