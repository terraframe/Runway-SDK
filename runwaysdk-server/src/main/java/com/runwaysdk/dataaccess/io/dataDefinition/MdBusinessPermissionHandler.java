/**
 * 
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import com.runwaysdk.dataaccess.io.ImportManager;

public class MdBusinessPermissionHandler extends MdClassPermissionHandler implements TagHandlerIF, HandlerFactoryIF
{
  public MdBusinessPermissionHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.STATE_PERMISSION_TAG, new StatePermissionHandler(manager));
  }
}
