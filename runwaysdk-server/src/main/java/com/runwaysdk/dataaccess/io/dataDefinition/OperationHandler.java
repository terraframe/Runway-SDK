/**
 * 
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.List;

import org.xml.sax.Attributes;

import com.runwaysdk.business.rbac.ActorDAO;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.MetadataDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;

public class OperationHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  /**
     * 
     */
  public OperationHandler(ImportManager manager)
  {
    super(manager);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    String operationName = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

    this.setPermission(operationName, context);
  }

  @SuppressWarnings("unchecked")
  protected List<ActorDAO> getActors(TagContext context)
  {
    return (List<ActorDAO>) context.getObject("actors");
  }

  protected MetadataDAOIF getMetadata(TagContext context)
  {
    return (MetadataDAOIF) context.getObject(MetadataInfo.CLASS);
  }

  protected MdTypeDAOIF getMdType(TagContext context)
  {
    return (MdTypeDAOIF) context.getObject(MdTypeInfo.CLASS);
  }

  protected PermissionAction getAction(TagContext context)
  {
    return (PermissionAction) context.getObject("action");
  }

  protected void setPermission(Operation operation, String id, TagContext context)
  {
    List<ActorDAO> actors = this.getActors(context);
    PermissionAction action = this.getAction(context);

    for (ActorDAO actor : actors)
    {
      if (action.equals(PermissionAction.GRANT))
      {
        actor.grantPermission(operation, id);
      }
      else if (action.equals(PermissionAction.REVOKE))
      {
        actor.revokePermission(operation, id);
      }
    }
  }

  protected void setPermission(String operationName, TagContext context)
  {
    MetadataDAOIF metadata = this.getMetadata(context);
    MdTypeDAOIF mdClass = this.getMdType(context);

    if (operationName.equals(XMLTags.ALL))
    {
      this.setPermission(Operation.CREATE, metadata.getId(), context);
      this.setPermission(Operation.DELETE, metadata.getId(), context);
      this.setPermission(Operation.READ, metadata.getId(), context);
      this.setPermission(Operation.READ_ALL, metadata.getId(), context);
      this.setPermission(Operation.WRITE, metadata.getId(), context);
      this.setPermission(Operation.WRITE_ALL, metadata.getId(), context);

      if (mdClass instanceof MdRelationshipDAOIF)
      {
        this.setPermission(Operation.ADD_CHILD, metadata.getId(), context);
        this.setPermission(Operation.ADD_PARENT, metadata.getId(), context);

        this.setPermission(Operation.DELETE_CHILD, metadata.getId(), context);
        this.setPermission(Operation.DELETE_PARENT, metadata.getId(), context);

        this.setPermission(Operation.READ_CHILD, metadata.getId(), context);
        this.setPermission(Operation.READ_PARENT, metadata.getId(), context);

        this.setPermission(Operation.WRITE_CHILD, metadata.getId(), context);
        this.setPermission(Operation.WRITE_PARENT, metadata.getId(), context);
      }
    }
    else if (operationName.equals(XMLTags.READ_ALL_ATTRIBUTES))
    {
      this.setPermission(Operation.READ_ALL, metadata.getId(), context);
    }
    else if (operationName.equals(XMLTags.WRITE_ALL_ATTRIBUTES))
    {
      this.setPermission(Operation.WRITE_ALL, metadata.getId(), context);
    }
    else
    {
      Operation operation = Operation.valueOf(operationName);

      this.setPermission(operation, metadata.getId(), context);
    }
  }
}
