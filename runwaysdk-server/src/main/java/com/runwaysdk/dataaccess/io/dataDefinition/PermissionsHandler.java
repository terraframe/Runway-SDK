package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.dataaccess.io.ImportManager;

public class PermissionsHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  public PermissionsHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.USER_TAG, new UserPermissionHandler(manager));
    this.addHandler(XMLTags.ROLE_TAG, new RolePermissionHandler(manager));
    this.addHandler(XMLTags.METHOD_TAG, new MethodPermissionHandler(manager));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF,
   * com.runwaysdk.dataaccess.io.ImportManager)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    this.getManager().enterPermissionsState();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onEndElement(java.lang.String, java.lang.String, java.lang.String, com.runwaysdk.dataaccess.io.ImportManager)
   */
  @Override
  public void onEndElement(String uri, String localName, String name, TagContext context)
  {
    this.getManager().leavingCurrentState();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#modifiesState(java.lang.String)
   */
  @Override
  public boolean modifiesState(String localName)
  {
    return localName.equals(XMLTags.PERMISSIONS_TAG);
  }
}
