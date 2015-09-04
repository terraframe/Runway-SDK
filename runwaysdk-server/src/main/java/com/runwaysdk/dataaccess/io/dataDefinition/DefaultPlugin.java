package com.runwaysdk.dataaccess.io.dataDefinition;

import com.runwaysdk.dataaccess.io.ImportManager;

public class DefaultPlugin implements ImportPluginIF
{

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.ImportPluginIF#register(java.util.Map)
   */
  @Override
  public void register(ImportManager manager)
  {
    manager.setRoot(new RootHandler(manager));
  }

}
