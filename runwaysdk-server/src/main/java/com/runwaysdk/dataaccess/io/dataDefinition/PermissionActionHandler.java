/**
 * 
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.dataaccess.io.ImportManager;

public class PermissionActionHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  private PermissionAction action;

  public PermissionActionHandler(ImportManager manager, PermissionAction action)
  {
    super(manager);

    this.action = action;

    MdClassPermissionHandler handler = new MdClassPermissionHandler(manager);

    this.addHandler(XMLTags.MD_STRUCT_PERMISSION_TAG, handler);
    this.addHandler(XMLTags.MD_UTIL_PERMISSION_TAG, handler);
    this.addHandler(XMLTags.MD_VIEW_PERMISSION_TAG, handler);
    this.addHandler(XMLTags.MD_BUSINESS_PERMISSION_TAG, new MdBusinessPermissionHandler(manager));
    this.addHandler(XMLTags.MD_RELATIONSHIP_PERMISSION_TAG, new MdRelationshipPermissionHandler(manager));
    this.addHandler(XMLTags.MD_FACADE_PERMISSION_TAG, new MdFacadePermissionHandler(manager));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    context.setObject("action", this.action);
  }

}