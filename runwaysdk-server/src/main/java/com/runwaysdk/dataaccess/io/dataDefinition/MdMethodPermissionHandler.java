/**
 * 
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;

public class MdMethodPermissionHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  public MdMethodPermissionHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.OPERATION_TAG, new OperationHandler(manager));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.AbstractPermissionHandler.OperationHandler#onStartElement(java.lang.String, org.xml.sax.Attributes,
   * com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    MdTypeDAOIF mdType = (MdTypeDAOIF) context.getObject(MdTypeInfo.CLASS);

    String methodName = attributes.getValue(XMLTags.METHOD_PERMISSION_NAME_ATTRIBUTE);
    MdMethodDAOIF mdMethod = mdType.getMdMethod(methodName);

    context.setObject(MetadataInfo.CLASS, mdMethod);
  }
}
