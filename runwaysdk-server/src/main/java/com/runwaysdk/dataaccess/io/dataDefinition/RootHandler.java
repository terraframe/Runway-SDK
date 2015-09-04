package com.runwaysdk.dataaccess.io.dataDefinition;

import com.runwaysdk.dataaccess.io.ImportManager;

public class RootHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{

  /**
   * @param manager
   *          TODO
   */
  public RootHandler(ImportManager manager)
  {
    super(manager);

    // Setup default dispatching
    this.addHandler(XMLTags.DELETE_TAG, new DeleteHandler(manager));
    this.addHandler(XMLTags.CREATE_TAG, new CreateHandler(manager));
    // this.addHandler(XMLTags.UPDATE_TAG, new UpdateHandler());
    // this.addHandler(XMLTags.CREATE_OR_UPDATE_TAG, new CreateOrUpdateHandler());
    // this.addHandler(XMLTags.PERMISSIONS_TAG, new PermissionsHandler());
  }
}
