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
package com.runwaysdk.business.generation.view;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;

/**
 * {@link ContentListener} used to generate 'form.jsp', which is responsible for
 * writing the CRUD attributes of a given {@link MdEntityDAOIF}
 * 
 * @author jsmethie
 */
public class FormListener extends AttributeAdapter
{
  public FormListener(MdEntityDAOIF mdEntity)
  {
    this(mdEntity, "form", "jsp");
  }

  public FormListener(MdEntityDAOIF mdEntity, String fileName, String extension)
  {
    super(mdEntity, fileName, extension);
  }

  @Override
  public void header()
  {
    writeIncludes();
  }

  @Override
  public void component()
  {
    // Open component tag
    writeComponent(this.getComponentName(), "dto");
  }

  @Override
  public void closeComponent()
  {
    // Close the component tag
    getWriter().closeTag();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.business.generation.view.AttributeAdapter#generateReference
   * (com.runwaysdk.dataaccess.MdAttributeDAOIF)
   */
  protected void generateReference(MdAttributeDAOIF mdAttribute)
  {
    String attributeName = mdAttribute.definesAttribute();

    writeDT(attributeName);
    writeSelect("${_" + attributeName + "}", "current", attributeName, "id");

    writeOption("${current.keyName}");

    // Close the select tag
    getWriter().closeTag();

    // Close the dt tag
    getWriter().closeTag();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.business.generation.view.AttributeAdapter#generateEnumeration
   * (com.runwaysdk.dataaccess.MdAttributeDAOIF)
   */
  protected void generateEnumeration(MdAttributeDAOIF mdAttribute)
  {
    String attributeName = mdAttribute.definesAttribute();
    String optionValue = "${" + this.getComponentName() + "." + attributeName + "Md.enumItems[current.enumName]}";
    String optionSelected = "${mjl:contains(" + this.getComponentName() + "." + attributeName + "EnumNames, current.enumName) ? 'selected' : 'false'}";

    writeDT(attributeName);
    writeSelect("${_" + attributeName + "}", "current", attributeName, "enumName");

    writeOption(optionValue, optionSelected);

    // Close the select tag
    getWriter().closeTag();

    // Close the dt tag
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

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.business.generation.view.AttributeAdapter#generateBoolean
   * (com.runwaysdk.dataaccess.MdAttributeDAOIF)
   */
  protected void generateBoolean(MdAttributeDAOIF mdAttribute)
  {
    writeDT(mdAttribute.definesAttribute());
    writeBoolean(mdAttribute.definesAttribute());
    getWriter().closeTag();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.business.generation.view.AttributeAdapter#generateAttribute
   * (com.runwaysdk.dataaccess.MdAttributeDAOIF)
   */
  protected void generateAttribute(MdAttributeDAOIF mdAttribute)
  {
    writeDT(mdAttribute.definesAttribute());
    writeInput(mdAttribute.definesAttribute(), "text");
    getWriter().closeTag();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.business.generation.view.AttributeAdapter#generateEncryption
   * (com.runwaysdk.dataaccess.MdAttributeDAOIF)
   */
  @Override
  protected void generateEncryption(MdAttributeDAOIF mdAttribute)
  {
    writeDT(mdAttribute.definesAttribute());
    writeInput(mdAttribute.definesAttribute(), "password");
    getWriter().closeTag();
  }

  @Override
  protected void generateMoment(MdAttributeDAOIF mdAttribute)
  {
    generateAttribute(mdAttribute);
  }

}
