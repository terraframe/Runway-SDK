/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.business.generation.view;

import java.util.HashMap;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.GeneratedActions;
import com.runwaysdk.constants.MdActionInfo;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.session.Session;

/**
 * {@link ContentListener} used to generate 'viewAllComponent.jsp', which is
 * generating a table to display all non system attributes of every instance of
 * the given {@link MdEntityDAOIF}.
 * 
 * @author Justin Smethie
 */
public class ViewAllComponentListener extends AttributeAdapter implements ContentListener
{
  public ViewAllComponentListener(MdEntityDAOIF mdEntity)
  {
    this(mdEntity, "viewAllComponent", "jsp");
  }

  public ViewAllComponentListener(MdEntityDAOIF mdEntity, String fileName, String extension)
  {
    super(mdEntity, fileName, extension);
  }

  @Override
  public void header()
  {
    writeIncludes();

    writeTitle("View all " + this.getMdEntity().getDisplayLabel(Session.getCurrentLocale()));
  }

  @Override
  public void form()
  {
    // Open table tag
    writeTable("${query}", "item");
  }

  @Override
  public void afterForm()
  {
    writeContex(this.getMdEntity().definesType() + CONTROLLER_SUFFIX + "." + GeneratedActions.VIEW_PAGE_ACTION.getName() + MdActionInfo.ACTION_SUFFIX);

    // Open columns tag
    writeColumns();
  }

  @Override
  public void parent(RelationshipEventIF event)
  {
    // Open a free column for the parent reference
    writeReferenceColumn(event.getParent(), "parent");
  }

  @Override
  public void child(RelationshipEventIF event)
  {
    writeReferenceColumn(event.getChild(), "child");
  }

  @Override
  public void afterAttributes()
  {
    // Open free column
    writeFreeColumn();

    writeTableHeader("");

    // Open table row
    writeTableRow();

    // Write link
    String link = this.getMdEntity().definesType() + CONTROLLER_SUFFIX + "." + GeneratedActions.VIEW_ACTION.getName() + MdActionInfo.ACTION_SUFFIX;

    // Open the command link tag
    writeCommandLink(link, "view.link", "View");

    // Write the property
    writeProperty("id", "${item.id}");

    // Close the command link tag
    getWriter().closeTag();

    // Close table row
    getWriter().closeTag();

    writeTableFooter("");

    // Close free column
    getWriter().closeTag();
  }

  @Override
  public void beforeCloseForm()
  {
    // Close columns tag
    getWriter().closeTag();

    writePagination();
  }

  @Override
  public void closeForm()
  {
    // Close table tag
    getWriter().closeTag();
  }

  @Override
  public void afterCloseForm()
  {
    getWriter().writeEmptyTag(BR_TAG);

    writeNewInstanceLink(this.getMdEntity());
  }

  protected void writeNewInstanceLink(MdClassDAOIF mdClass)
  {
    String type = mdClass.definesType();
    String typeName = mdClass.getTypeName();
    String label = mdClass.getDisplayLabel(CommonProperties.getDefaultLocale());

    if (mdClass instanceof MdRelationshipDAOIF)
    {
      String link = type + CONTROLLER_SUFFIX + "." + GeneratedActions.NEW_RELATIONSHIP_ACTION.getName() + MdActionInfo.ACTION_SUFFIX;
      ;
      String linkName = typeName + CONTROLLER_SUFFIX + ".newRelationship";
      String linkDisplay = "Create a new " + label;

      writeCommandLinkWithNoProperties(link, linkName, linkDisplay);
    }
    else
    {
      String link = type + CONTROLLER_SUFFIX + "." + GeneratedActions.NEW_INSTANCE_ACTION.getName() + MdActionInfo.ACTION_SUFFIX;
      String linkName = typeName + CONTROLLER_SUFFIX + ".newInstance";
      String linkDisplay = "Create a new " + label;

      writeCommandLinkWithNoProperties(link, linkName, linkDisplay);
    }
  }

  protected void writeReferenceColumn(MdBusinessDAOIF mdBusiness, String attribute)
  {
    writeFreeColumn();

    writeTableHeader(mdBusiness.getDisplayLabel(CommonProperties.getDefaultLocale()));

    // Open table row
    writeTableRow();

    // Write link
    if (mdBusiness.hasMdController())
    {
      String link = mdBusiness.definesType() + CONTROLLER_SUFFIX + "." + GeneratedActions.VIEW_ACTION.getName() + MdActionInfo.ACTION_SUFFIX;

      // Open command link tag
      writeCommandLink(link, attribute + ".link", "${item." + attribute + ".keyName}");

      writeProperty("id", "${item." + attribute + "Id}");
      
      // Close command link tag
      getWriter().closeTag();
    }
    else
    {
      getWriter().writeValue("${item." + attribute + ".keyName}");
    }

    // Close table row
    getWriter().closeTag();

    writeTableFooter("");

    // Close free column
    getWriter().closeTag();
  }

  @Override
  public void beforeStructAttribute(AttributeEventIF attributeEvent)
  {
    MdAttributeDAOIF mdAttribute = attributeEvent.getMdAttribute();

    // Open struct column tag
    writeStructColumn(mdAttribute.definesAttribute());

    writeTableHeader(mdAttribute.getDisplayLabel(Session.getCurrentLocale()));

    this.pushComponent(mdAttribute.definesAttribute());
  }

  @Override
  public void afterStructAttribute(AttributeEventIF attributeEvent)
  {
    this.popComponent();

    // Close struct column tag
    getWriter().closeTag();
  }

  protected boolean validAttribute(MdAttributeDAOIF mdAttribute)
  {
    if (mdAttribute.isSystem())
    {
      return false;
    }

    String name = mdAttribute.definesAttribute();
    return ( !name.equals(ElementInfo.DOMAIN) && !name.equals(ElementInfo.OWNER) );
  }

  @Override
  public void attribute(AttributeEventIF event)
  {
    if (validAttribute(event.getMdAttribute().getMdAttributeConcrete()))
    {
      super.attribute(event);
    }
  }

  @Override
  protected void generateAttribute(MdAttributeDAOIF mdAttribute)
  {
    if (this.validAttribute(mdAttribute))
    {
      // Open the attribute column tag
      writeAttributeColumn(mdAttribute.definesAttribute());

      getWriter().closeTag();
    }
  }

  @Override
  protected void generateBoolean(MdAttributeDAOIF mdAttribute)
  {
    generateAttribute(mdAttribute);
  }

  @Override
  protected void generateEncryption(MdAttributeDAOIF mdAttribute)
  {
    // Do nothing, by default we do not want to show encrypted attributes
  }

  @Override
  protected void generateEnumeration(MdAttributeDAOIF mdAttribute)
  {
    // Open the attribute column tag
    String componentName = this.getComponentName();
    String attributeName = mdAttribute.definesAttribute();

    HashMap<String, String> forEach = new HashMap<String, String>();
    forEach.put("var", "enumName");
    forEach.put("items", "${" + componentName + "." + attributeName + "EnumNames}");

    writeAttributeColumn(attributeName);

    getWriter().openTag(ROW_TAG);
    getWriter().openTag(UL_TAG);
    getWriter().openTag(FOR_EACH_TAG, forEach);

    getWriter().openTag(LI_TAG);
    getWriter().writeValue("${" + componentName + "." + attributeName + "Md.enumItems[enumName]}");
    getWriter().closeTag();

    getWriter().closeTag();
    getWriter().closeTag();
    getWriter().closeTag();
    getWriter().closeTag();
  }

  @Override
  protected void generateReference(MdAttributeDAOIF mdAttribute)
  {
    String componentName = this.getComponentName();
    String attributeName = mdAttribute.definesAttribute();

    // Open the attribute column tag
    writeAttributeColumn(attributeName);
    getWriter().openTag(ROW_TAG);
    getWriter().writeValue("${" + componentName + "." + attributeName + ".keyName}");
    getWriter().closeTag();
    getWriter().closeTag();
  }

  @Override
  protected void generateMoment(MdAttributeDAOIF mdAttribute)
  {
    this.generateAttribute(mdAttribute);
  }
}
