/**
 * 
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.dataDefinition.VersionHandler.Action;

public class VersionPlugin implements ImportPluginIF
{
  private class NullHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public NullHandler(ImportManager manager)
    {
      super(manager);
    }
  }

  private class VersionRootHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public VersionRootHandler(ImportManager manager, Action action)
    {
      super(manager);

      if (action.equals(Action.DO_IT))
      {
        this.addHandler(XMLTags.DO_IT_TAG, new RootHandler(manager));
        this.addHandler(XMLTags.UNDO_IT_TAG, new NullHandler(manager));
      }
      else if (action.equals(Action.UNDO_IT))
      {
        this.addHandler(XMLTags.DO_IT_TAG, new NullHandler(manager));
        this.addHandler(XMLTags.UNDO_IT_TAG, new RootHandler(manager));
      }
    }
  }

  private Action action;

  /**
   * 
   */
  public VersionPlugin(Action action)
  {
    this.action = action;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.ImportPluginIF#register(com.runwaysdk.dataaccess.io.ImportManager)
   */
  @Override
  public void register(ImportManager manager)
  {
    manager.setRoot(new VersionRootHandler(manager, this.action));
  }

}
