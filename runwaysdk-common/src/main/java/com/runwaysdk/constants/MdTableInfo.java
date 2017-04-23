package com.runwaysdk.constants;

public interface MdTableInfo extends MdClassInfo
{
  /**
   * Class {@link MdTableInfo}.
   */
  public static final String CLASS   = Constants.METADATA_PACKAGE+".MdTable";
  
  /**
   * ID.
   */
  public static final String ID_VALUE                   = "imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001";  
  
  /**
   * Name of the attribute that stores the name of the the database table used
   * to store instances of the class defined by this metadata object.
   */
  public static final String TABLE_NAME                 = "tableName";
  
  /**
   * Databasee column of the attribute that stores the name of the the database table used
   * to store instances of the class defined by this metadata object.
   */
  public static final String COLUMN_TABLE_NAME          = "table_name";
  
  /**
   * The maximum size of the name of the database name.
   */
  public static final String MAX_TABLE_NAME             = "128";
  
}
