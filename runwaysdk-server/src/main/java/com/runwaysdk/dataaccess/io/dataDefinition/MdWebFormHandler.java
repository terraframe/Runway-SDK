/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MdWebFormInfo;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFormDAO;

public class MdWebFormHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  public MdWebFormHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.CREATE_TAG, new CreateDecorator(this));
    this.addHandler(XMLTags.FIELDS_TAG, new MdWebFieldHandler(manager));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.HandlerFactory#supports(com.runwaysdk.dataaccess.io.dataDefinition.TagContext, java.lang.String)
   */
  @Override
  public boolean supports(TagContext context, String localName)
  {
    MdWebFormDAO mdForm = (MdWebFormDAO) context.getObject(MdTypeInfo.CLASS);

    if (mdForm != null && this.getManager().isCreated(mdForm.definesType()))
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
    // Get the MdWebForm to import, if this is a create then a new instance of
    // MdWebForm is imported
    MdWebFormDAO mdForm = (MdWebFormDAO) this.getManager().getEntityDAO(MdWebFormInfo.CLASS, attributes.getValue(XMLTags.NAME_ATTRIBUTE)).getEntityDAO();

    importMdWebForm(mdForm, attributes);

    // Make sure the class has not already been defined
    if (!this.getManager().isCreated(mdForm.definesType()))
    {
      mdForm.apply();

      this.getManager().addMapping(mdForm.definesType(), mdForm.getOid());
    }

    context.setObject(MdTypeInfo.CLASS, mdForm);
  }

  /**
   * Creates an MdWebForm from the parse of the class tag attributes.
   * 
   * @param mdForm
   *          TODO
   * @param attributes
   *          The attributes of an class tag
   * 
   * @return MdWebForm from the parse of the class tag attributes.
   */
  private final void importMdWebForm(MdWebFormDAO mdForm, Attributes attributes)
  {
    // Breakup the type into a package and name
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    mdForm.setValue(MdWebFormInfo.NAME, BusinessDAOFactory.getClassNameFromType(name));
    mdForm.setValue(MdWebFormInfo.PACKAGE, BusinessDAOFactory.getPackageFromType(name));

    ImportManager.setValue(mdForm, MdWebFormInfo.FORM_NAME, attributes, XMLTags.FORM_NAME);
    ImportManager.setValue(mdForm, MdWebFormInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setValue(mdForm, MdWebFormInfo.EXPORTED, attributes, XMLTags.EXPORTED_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdForm, MdWebFormInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdForm, MdWebFormInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);

    // Import optional reference attributes
    String classType = attributes.getValue(XMLTags.FORM_MD_CLASS);

    if (classType != null)
    {
      // Ensure the parent class has already been defined in the database
      if (!MdTypeDAO.isDefined(classType))
      {
        // The type is not defined in the database, check if it is defined
        // in the further down in the xml document.
        String[] search_tags = { XMLTags.MD_BUSINESS_TAG, XMLTags.MD_TERM_TAG };
        SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, classType, mdForm.definesType());
      }

      MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(classType);
      mdForm.setValue(MdWebFormInfo.FORM_MD_CLASS, mdClass.getOid());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onEndElement(java.lang.String, java.lang.String, java.lang.String, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onEndElement(String uri, String localName, String name, TagContext context)
  {
    MdWebFormDAO mdForm = (MdWebFormDAO) context.getObject(MdTypeInfo.CLASS);

    this.getManager().endImport(mdForm.definesType());
  }
}
