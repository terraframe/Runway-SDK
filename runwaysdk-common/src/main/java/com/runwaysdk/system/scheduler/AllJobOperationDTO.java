package com.runwaysdk.system.scheduler;

/**
 * @deprecated
 * DO NOT USE, THIS ONLY EXISTS FOR DDMS.
 */
@com.runwaysdk.business.ClassSignature(hash = -1240787115)
public enum AllJobOperationDTO implements com.runwaysdk.business.EnumerationDTOIF
{
  CANCEL(),
  
  PAUSE(),
  
  RESUME(),
  
  START(),
  
  STOP();
  
  public final static String CLASS = "com.runwaysdk.system.scheduler.AllJobOperation";
  
  
  public com.runwaysdk.system.scheduler.JobOperationDTO item(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    return (com.runwaysdk.system.scheduler.JobOperationDTO) clientRequest.getEnumeration(com.runwaysdk.system.scheduler.AllJobOperationDTO.CLASS, this.name());
  }
  
  @java.lang.SuppressWarnings("unchecked")
  public static java.util.List<com.runwaysdk.system.scheduler.JobOperationDTO> items(com.runwaysdk.constants.ClientRequestIF clientRequest, AllJobOperationDTO ... items)
  {
    java.lang.String[] itemNames = new java.lang.String[items.length];
    for(int i=0; i<items.length; i++)
    {
      itemNames[i] = items[i].name();
    }
    return (java.util.List<com.runwaysdk.system.scheduler.JobOperationDTO>) clientRequest.getEnumerations(com.runwaysdk.system.scheduler.AllJobOperationDTO.CLASS, itemNames);
  }
  
  @java.lang.SuppressWarnings("unchecked")
  public static java.util.List<com.runwaysdk.system.scheduler.JobOperationDTO> allItems(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    return (java.util.List<com.runwaysdk.system.scheduler.JobOperationDTO>) clientRequest.getAllEnumerations(com.runwaysdk.system.scheduler.AllJobOperationDTO.CLASS);
  }
  
  public java.lang.String getName()
  {
    return this.name();
  }
}
