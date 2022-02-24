/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdLocalizableInfo;
import com.runwaysdk.constants.MdMessageInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdLocalizableDAO;

public abstract class MdLocalizableHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  private String type;

  public MdLocalizableHandler(ImportManager manager, String type)
  {
    super(manager);

    this.type = type;

    this.addHandler(XMLTags.CREATE_TAG, new CreateDecorator(this));
    this.addHandler(XMLTags.ATTRIBUTES_TAG, new MdAttributeHandler(manager));
    this.addHandler(XMLTags.MD_METHOD_TAG, new MdMethodHandler(manager));
    this.addHandler(XMLTags.STUB_SOURCE_TAG, new SourceHandler(manager, XMLTags.STUB_SOURCE_TAG, MdClassInfo.STUB_SOURCE));
    this.addHandler(XMLTags.DTO_STUB_SOURCE_TAG, new SourceHandler(manager, XMLTags.DTO_STUB_SOURCE_TAG, MdClassInfo.DTO_STUB_SOURCE));
    this.addHandler(XMLTags.QUERY_STUB_SOURCE_TAG, new SourceHandler(manager, XMLTags.QUERY_STUB_SOURCE_TAG, MdViewInfo.QUERY_STUB_SOURCE));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.HandlerFactory#supports(com.runwaysdk.dataaccess.io.dataDefinition.TagContext, java.lang.String)
   */
  @Override
  public boolean supports(TagContext context, String localName)
  {
    MdLocalizableDAO mdLocalizable = (MdLocalizableDAO) context.getObject(MdTypeInfo.CLASS);

    if (mdLocalizable != null && this.getManager().isCreated(mdLocalizable.definesType()))
    {
      return false;
    }

    return super.supports(context, localName);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    // Get the MdExcpetion to import, if this is a create then a new instance of MdProblem is imported
    MdLocalizableDAO mdLocalizable = (MdLocalizableDAO) this.getManager().getEntityDAO(this.type, attributes.getValue(XMLTags.NAME_ATTRIBUTE)).getEntityDAO();

    populate(mdLocalizable, attributes);

    // Make sure the class has not already been defined
    if (!this.getManager().isCreated(mdLocalizable.definesType()))
    {
      mdLocalizable.apply();
    }

    context.setObject(MdTypeInfo.CLASS, mdLocalizable);
  }

  protected void populate(MdLocalizableDAO mdLocalizable, Attributes attributes)
  {
    // Import the required attributes and Breakup the type into a package and name
    String type = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    mdLocalizable.setValue(MdTypeInfo.NAME, BusinessDAOFactory.getClassNameFromType(type));
    mdLocalizable.setValue(MdTypeInfo.PACKAGE, BusinessDAOFactory.getPackageFromType(type));

    // Import optional attributes
    ImportManager.setLocalizedValue(mdLocalizable, MdLocalizableInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setValue(mdLocalizable, MdLocalizableInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdLocalizable, MdLocalizableInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdLocalizable, MdLocalizableInfo.PUBLISH, attributes, XMLTags.PUBLISH_ATTRIBUTE);
    ImportManager.setValue(mdLocalizable, MdLocalizableInfo.ABSTRACT, attributes, XMLTags.ABSTRACT_ATTRIBUTE);
    ImportManager.setValue(mdLocalizable, MdLocalizableInfo.EXTENDABLE, attributes, XMLTags.EXTENDABLE_ATTRIBUTE);
    ImportManager.setValue(mdLocalizable, MdLocalizableInfo.EXPORTED, attributes, XMLTags.EXPORTED_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdLocalizable, MdMessageInfo.MESSAGE, attributes, XMLTags.MESSAGE_ATTRIBUTE);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onEndElement(java.lang.String, java.lang.String, java.lang.String, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onEndElement(String uri, String localName, String name, TagContext context)
  {
    MdLocalizableDAO mdLocalizable = (MdLocalizableDAO) context.getObject(MdTypeInfo.CLASS);

    this.getManager().endImport(mdLocalizable.definesType());
  }

}
