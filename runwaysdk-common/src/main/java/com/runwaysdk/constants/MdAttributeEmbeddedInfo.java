package com.runwaysdk.constants;

public interface MdAttributeEmbeddedInfo extends MdAttributeConcreteInfo
{
  /**
   * Class.
   */
  public static final String CLASS   = Constants.METADATA_PACKAGE+".MdAttributeEmbedded";
  
  /**
   * Name of the attribute that references the name of the {@link MdClassDAOIF}
   * used to define the attributes that make up this struct attribute.
   */
  public static final String EMBEDDED_MD_CLASS   = "embeddedMdClass";
  
  /**
   * OID.
   */
  public static final String ID_VALUE  = "b0ac09d5-4507-34b5-9794-0d03a400003a";
}
