package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = 787447094)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MdTerm.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class MdTermBase extends com.runwaysdk.system.metadata.MdBusiness
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdTerm";
  private static final long serialVersionUID = 787447094;
  
  public MdTermBase()
  {
    super();
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static MdTerm get(String id)
  {
    return (MdTerm) com.runwaysdk.business.Business.get(id);
  }
  
  public static MdTerm getByKey(String key)
  {
    return (MdTerm) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static MdTerm lock(java.lang.String id)
  {
    MdTerm _instance = MdTerm.get(id);
    _instance.lock();
    
    return _instance;
  }
  
  public static MdTerm unlock(java.lang.String id)
  {
    MdTerm _instance = MdTerm.get(id);
    _instance.unlock();
    
    return _instance;
  }
  
  public String toString()
  {
    if (this.isNew())
    {
      return "New: "+ this.getClassDisplayLabel();
    }
    else
    {
      return super.toString();
    }
  }
}