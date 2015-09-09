/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.ParameterMarker;
import com.runwaysdk.dataaccess.metadata.Type;

public class MdMethodHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  public MdMethodHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.CREATE_TAG, new CreateDecorator(this));
    this.addHandler(XMLTags.MD_PARAMETER_TAG, new MdParameterHandler(manager));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    MdTypeDAO mdType = (MdTypeDAO) context.getObject(MdTypeInfo.CLASS);

    String methodName = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String typeName = mdType.definesType();
    String key = typeName + "." + methodName;

    MdMethodDAO mdMethod = (MdMethodDAO) this.getManager().getEntityDAO(MdMethodInfo.CLASS, key).getEntityDAO();
    mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdType.getId());

    if (attributes.getValue(XMLTags.METHOD_RETURN_ATTRIBUTE) != null)
    {
      Type returnType = new Type(attributes.getValue(XMLTags.METHOD_RETURN_ATTRIBUTE));

      // Ensure that the return type has been defined in the core if it is not a primitive
      if (returnType.isDefinedType() && !MdTypeDAO.isDefined(returnType.getRootType()))
      {
        SearchHandler.searchEntity(this.getManager(), XMLTags.TYPE_TAGS, XMLTags.NAME_ATTRIBUTE, returnType.getRootType(), typeName);
      }

      mdMethod.setValue(MdMethodInfo.RETURN_TYPE, returnType.getType());
    }

    ImportManager.setValue(mdMethod, MdMethodInfo.NAME, attributes, XMLTags.NAME_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdMethod, MdMethodInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdMethod, MdMethodInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdMethod, MdMethodInfo.IS_STATIC, attributes, XMLTags.METHOD_STATIC_ATTRIBUTE);
    mdMethod.apply();

    context.setObject(ParameterMarker.class.getName(), mdMethod);
  }
}
