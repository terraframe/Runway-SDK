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
package com.runwaysdk.dataaccess.metadata;

import java.sql.Connection;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.business.generation.JavaArtifactMdLocalizableCommand;
import com.runwaysdk.business.generation.JavaArtifactMdTypeCommand;
import com.runwaysdk.constants.MdLocalizableInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.MdLocalizableDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

/**
 * Abstract root for localizable type definitions.
 *
 * @author Eric
 */
public abstract class MdLocalizableDAO extends MdTransientDAO implements MdLocalizableDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -1180294697124891309L;

  /**
   * Default constructor sets no attributes
   */
  public MdLocalizableDAO()
  {
    super();
  }

  /**
   *
   *
   * @param attributeMap
   * @param type
   */
  public MdLocalizableDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * Localizes the message template and returns it
   *
   * @param locale
   *
   * @return The localized message template
   */
  public String getMessage(Locale locale)
  {
    return ((AttributeLocalIF)this.getAttributeIF(MdLocalizableInfo.MESSAGE)).getValue(locale);
  }

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getMessages()
  {
    return ((AttributeLocalIF)this.getAttributeIF(MdLocalizableInfo.MESSAGE)).getLocalValues();
  }

  @Override
  public Command getDeleteJavaArtifactCommand(Connection conn)
  {
    return new JavaArtifactMdLocalizableCommand(this, JavaArtifactMdTypeCommand.Operation.DELETE, conn);
  }

  /**
   * Returns true if an attribute that stores source or class has been modified.
   *
   * @return true if an attribute that stores source or class has been modified.
   */
  @Override
  public boolean javaArtifactsModifiedOnObject()
  {
    if (super.javaArtifactsModifiedOnObject())
    {
      return true;
    }

    if (!this.isSystemPackage())
    {
      if (this.getAttribute(MdTypeInfo.BASE_SOURCE).isModified())
      {
        return true;
      }
    }

    return false;
  }
}
