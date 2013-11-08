/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.business.generation.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.runwaysdk.constants.GeneratedActions;
import com.runwaysdk.constants.MdActionInfo;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.session.Session;

/**
 * {@link ContentListener} used to generate 'viewComponent.jsp', which is
 * responsible for showing the CRUD attributes of a given {@link MdEntityDAOIF}
 * 
 * @author Justin Smethie
 */
public class ViewComponentListener extends AttributeAdapter
{
  public ViewComponentListener(MdEntityDAOIF mdEntity)
  {
    this(mdEntity, "viewComponent", "jsp");
  }

  public ViewComponentListener(MdEntityDAOIF mdEntity, String fileName, String extension)
  {
    super(mdEntity, fileName, extension);
  }

  @Override
  public void header()
  {
    writeIncludes();

    writeTitle("View a " + this.getMdEntity().getDisplayLabel(Session.getCurrentLocale()));
  }

  @Override
  public void beforeForm()
  {
    // Open list tag
    getWriter().openTag(DL_TAG);
  }

  @Override
  public void form()
  {
    String type = this.getMdEntity().definesType();

    // Open form tag
    writeForm("POST", type + ".form.id", type + ".form.name");
  }

  @Override
  public void afterForm()
  {
    writeInput("id", "hidden", "${item.id}");
  }

  @Override
  public void component()
  {
    // Open component tag
    writeComponent(this.getComponentName(), "dto");
  }

  @Override
  public void parent(RelationshipEventIF event)
  {
    writeRelationshipField(event.getParent());
  }

  @Override
  public void child(RelationshipEventIF event)
  {
    writeRelationshipField(event.getChild());
  }

  @Override
  public void closeComponent()
  {
    getWriter().closeTag();
  }

  @Override
  public void beforeCloseForm()
  {
    String type = this.getMdEntity().definesType();
    String link = type + CONTROLLER_SUFFIX + "." + GeneratedActions.EDIT_ACTION.getName() + MdActionInfo.ACTION_SUFFIX;
    String linkName = type + ".form.edit.button";
    String linkDisplay = "Edit";

    super.writeCommand(link, linkName, linkDisplay);
  }

  @Override
  public void closeForm()
  {
    // Close form tag
    getWriter().closeTag();
  }

  @Override
  public void afterCloseForm()
  {
    // Close list tag
    getWriter().closeTag();

    // Generate links for relationship
    MdClassDAOIF mdClass = this.getMdEntity();
    String type = mdClass.definesType();

    if (mdClass instanceof MdBusinessDAOIF)
    {
      writeRelationshipLinks((MdBusinessDAOIF) mdClass);
    }

    // Generate convience ViewAll link
    String link = type + CONTROLLER_SUFFIX + "." + GeneratedActions.VIEW_ALL_ACTION.getName() + MdActionInfo.ACTION_SUFFIX;
    String linkName = type + "." + GeneratedActions.VIEW_ALL_ACTION.getName() + ".link";
    String linkDisplay = "View All";

    writeCommandLinkWithNoProperties(link, linkName, linkDisplay);
  }

  protected void generateReference(MdAttributeDAOIF mdAttribute)
  {
    String componentName = this.getComponentName();
    String attributeName = mdAttribute.definesAttribute();

    writeDT(attributeName);
    getWriter().writeValue("${" + componentName + "." + attributeName + ".keyName}");
    getWriter().closeTag();
  }

  protected void generateEnumeration(MdAttributeDAOIF mdAttribute)
  {
    String componentName = this.getComponentName();
    String attributeName = mdAttribute.definesAttribute();

    HashMap<String, String> forEach = new HashMap<String, String>();
    forEach.put("var", "enumName");
    forEach.put("items", "${" + componentName + "." + attributeName + "EnumNames}");

    writeDT(attributeName);
    getWriter().openTag(UL_TAG);
    getWriter().openTag(FOR_EACH_TAG, forEach);

    getWriter().openTag(LI_TAG);
    getWriter().writeValue("${" + componentName + "." + attributeName + "Md.enumItems[enumName]}");
    getWriter().closeTag();

    getWriter().closeTag();
    getWriter().closeTag();
    getWriter().closeTag();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.business.generation.view.AttributeAdapter#generateMultiReference
   * (com.runwaysdk.dataaccess.MdAttributeDAOIF)
   */
  @Override
  protected void generateMultiReference(MdAttributeDAOIF mdAttribute)
  {
  }

  @Override
  protected void generateMoment(MdAttributeDAOIF mdAttribute)
  {
    generateAttribute(mdAttribute);
  }

  protected void generateBoolean(MdAttributeDAOIF mdAttribute)
  {
    String componentName = this.getComponentName();
    String attributeName = mdAttribute.definesAttribute();

    writeDT(attributeName);
    getWriter().writeValue("${" + componentName + "." + attributeName + " ? " + componentName + "." + attributeName + "Md.positiveDisplayLabel : " + componentName + "." + attributeName + "Md.negativeDisplayLabel}");
    getWriter().closeTag();
  }

  protected void generateAttribute(MdAttributeDAOIF mdAttribute)
  {
    String componentName = this.getComponentName();
    String attributeName = mdAttribute.definesAttribute();

    writeDT(attributeName);
    getWriter().writeValue("${" + componentName + "." + attributeName + "}");
    getWriter().closeTag();
  }

  @Override
  protected void generateEncryption(MdAttributeDAOIF mdAttribute)
  {
    // Do nothing, by default we do not want to show encrypted attributes
  }

  protected void writeRelationshipField(MdBusinessDAOIF mdBusiness)
  {
    String type = mdBusiness.definesType();

    super.writeLabel(mdBusiness.getDisplayLabel(Session.getCurrentLocale()));

    // Open the dd tag
    getWriter().openTag(DD_TAG);

    // Generate link for parent
    String link = type + CONTROLLER_SUFFIX + "." + GeneratedActions.VIEW_ACTION.getName() + MdActionInfo.ACTION_SUFFIX;
    String linkName = type + ".form.view.link";
    String linkDisplay = "${item.parent.keyName}";

    if (mdBusiness.hasMdController())
    {
      writeCommandLink(link, linkName, linkDisplay);

      writeProperty("id", "${item.parentId}");

      // Close link tag
      getWriter().closeTag();
    }
    else
    {
      getWriter().writeValue(linkDisplay);
    }

    // Close the dd tag
    getWriter().closeTag();
  }

  protected void writeRelationshipLinks(MdBusinessDAOIF mdBusiness)
  {
    List<MdRelationshipDAOIF> parentRelationships = mdBusiness.getAllParentMdRelationships();
    List<MdRelationshipDAOIF> childRelationships = mdBusiness.getAllChildMdRelationships();

    if (parentRelationships.size() > 0 || childRelationships.size() > 0)
    {
      getWriter().openTag(DL_TAG);

      writeRelationshipLinks(parentRelationships, "Parent Relationships", "parentId", GeneratedActions.PARENT_QUERY_ACTION.getName());
      writeRelationshipLinks(childRelationships, "Child Relationships", "childId", GeneratedActions.CHILD_QUERY_ACTION.getName());

      getWriter().closeTag();
    }
  }

  protected void writeRelationshipLinks(List<MdRelationshipDAOIF> relationships, String label, String propertyName, String action)
  {
    this.filterRelationships(relationships);

    if (relationships.size() > 0)
    {
      writeLabel(label);

      getWriter().openTag(DD_TAG);

      getWriter().openTag(UL_TAG);

      for (MdRelationshipDAOIF mdRelationship : relationships)
      {
        String type = mdRelationship.definesType();
        String link = type + CONTROLLER_SUFFIX + "." + action + MdActionInfo.ACTION_SUFFIX;
        String linkName = type + "." + action + ".link";
        String linkDisplay = mdRelationship.getChildDisplayLabel(Session.getCurrentLocale());

        getWriter().openTag(LI_TAG);

        // Open up the command link tag
        writeCommandLink(link, linkName, linkDisplay);

        writeProperty(propertyName, "${item.id}");

        // Close the command link tag
        getWriter().closeTag();

        getWriter().closeTag();
      }

      getWriter().closeTag();

      getWriter().closeTag();
    }
  }

  private void filterRelationships(List<MdRelationshipDAOIF> relationships)
  {
    Iterator<MdRelationshipDAOIF> it = relationships.iterator();

    while (it.hasNext())
    {
      MdRelationshipDAOIF next = it.next();

      if (!next.hasMdController())
      {
        it.remove();
      }
    }
  }
}
