package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.constants.MdTableInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdTableDAO;

public class MdTableHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  /**
   * @param manager
   *          TODO
   */
  public MdTableHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.CREATE_TAG, new CreateDecorator(this));
    this.addHandler(XMLTags.ATTRIBUTES_TAG, new MdAttributeHandler(manager));
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.io.dataDefinition.HandlerFactory#supports(com.
   * runwaysdk.dataaccess.io.dataDefinition.TagContext, java.lang.String)
   */
  @Override
  public boolean supports(TagContext context, String localName)
  {
    MdTableDAO mdTableDAO = (MdTableDAO) context.getObject(MdTypeInfo.CLASS);

    if (mdTableDAO != null && this.getManager().isCreated(mdTableDAO.definesType()))
    {
      return false;
    }

    return super.supports(context, localName);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onStartElement(java
   * .lang.String, org.xml.sax.Attributes,
   * com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF,
   * com.runwaysdk.dataaccess.io.ImportManager)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    // Get the MdTableDAO to import, if this is a create then a new instance
    // of MdTableDAO is imported
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

    MdTableDAO mdTableDAO = this.createMdTable(localName, name);

    this.importMdTable(mdTableDAO, localName, attributes);

    // Make sure the class has not already been defined
    if (!this.getManager().isCreated(mdTableDAO.definesType()))
    {
      mdTableDAO.apply();

      this.getManager().addMapping(name, mdTableDAO.getId());
    }

    context.setObject(MdTypeInfo.CLASS, mdTableDAO);
  }

  private final MdTableDAO createMdTable(String localName, String name)
  {
    return (MdTableDAO) this.getManager().getEntityDAO(MdTableInfo.CLASS, name).getEntityDAO();
  }

  /**
   * Creates an MdTableDAO from the parse of the class tag attributes.
   * 
   * @param mdTableDAO
   *          TODO
   * @param attributes
   *          The attributes of an class tag
   * @return MdTableDAO from the parse of the class tag attributes.
   */
  private final void importMdTable(MdTableDAO mdTableDAO, String localName, Attributes attributes)
  {
    // Import the required attributes and Breakup the type into a package and
    // name
    String type = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    mdTableDAO.setValue(MdTypeInfo.NAME, BusinessDAOFactory.getClassNameFromType(type));
    mdTableDAO.setValue(MdTypeInfo.PACKAGE, BusinessDAOFactory.getPackageFromType(type));

    // Import optional attributes
    ImportManager.setLocalizedValue(mdTableDAO, MdTableInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setValue(mdTableDAO, MetadataInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdTableDAO, MetadataInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdTableDAO, MdTableInfo.PUBLISH, attributes, XMLTags.PUBLISH_ATTRIBUTE);
    ImportManager.setValue(mdTableDAO, MdTableInfo.EXPORTED, attributes, XMLTags.EXPORTED_ATTRIBUTE);

    String tableName = attributes.getValue(XMLTags.ENTITY_TABLE);
    if (tableName != null)
    {
      mdTableDAO.setValue(MdTableInfo.TABLE_NAME, tableName);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onEndElement(java.
   * lang.String, java.lang.String, java.lang.String,
   * com.runwaysdk.dataaccess.io.dataDefinition.TagContext,
   * com.runwaysdk.dataaccess.io.ImportManager)
   */
  @Override
  public void onEndElement(String uri, String localName, String name, TagContext context)
  {
    if (localName.equals(XMLTags.MD_TABLE_TAG))
    {
      MdTableDAO mdTableDAO = (MdTableDAO) context.getObject(MdTypeInfo.CLASS);

      this.getManager().endImport(mdTableDAO.definesType());
    }
  }
}
