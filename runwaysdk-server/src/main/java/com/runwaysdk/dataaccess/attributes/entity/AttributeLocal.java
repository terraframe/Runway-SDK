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
package com.runwaysdk.dataaccess.attributes.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.constants.StructInfo;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.attributes.EmptyValueProblem;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalDAO;
import com.runwaysdk.session.Session;

public abstract class AttributeLocal extends AttributeStruct implements AttributeLocalIF
{
  /**
   * Generated by eclipse
   */
  private static final long serialVersionUID = 6964640721123114444L;

  /**
   * @param name
   * @param mdAttributeKey
   *          key of the defining attribute metadata.
   * @param definingType
   * @param value
   */
  public AttributeLocal(String name, String mdAttributeKey, String definingType, String value)
  {
    super(name, mdAttributeKey, definingType, value);
  }

  /**
   * 
   * @param name
   * @param mdAttributeKey
   *          key of the defining attribute metadata.
   * @param definingEntityType
   * @param value
   * @param structDAO
   */
  public AttributeLocal(String name, String mdAttributeKey, String definingEntityType, String value, StructDAO structDAO)
  {
    super(name, mdAttributeKey, definingEntityType, value, structDAO);
  }

  /**
   * Returns the <code>MdLocalStructDAOIF</code> that defines the type that this
   * attribute references.
   * 
   * Preconditions: this.structDAO has been initialized.
   * 
   */
  public MdLocalStructDAOIF getMdStructDAOIF()
  {
    return (MdLocalStructDAOIF) super.getMdStructDAOIF();
  }

  /**
   * Returns the localized string that is the best fit for the given locale.
   * 
   * Duplicated in <code>LocalStruct</code>
   */
  public String getValue(Locale locale)
  {
    return MdAttributeLocalDAO.findAttributeValueMatch(this, locale);
  }

  /**
   * Alias for getLocaleMap
   */
  public Map<String, String> getLocalValues()
  {
    return getLocalValues(this.getStructDAO());
  }
  
  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getLocaleMap()
  {
    return getLocalValues(this.getStructDAO());
  }

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   * 
   * @param structDAO
   * 
   * @return map where the key is the locale and the value is the localized
   *         String value.
   */
  public static Map<String, String> getLocalValues(StructDAO structDAO)
  {
    //return (Map<String, String>) ((Object)(structDAO).getAsMap());
    
    Map<String, String> localeMap = new HashMap<String, String>();

    for (MdAttributeConcreteDAOIF mdAttributeConcreteDAOIF : structDAO.getMdAttributeDAOs())
    {
      if (!mdAttributeConcreteDAOIF.isSystem())
      {
        String attributeName = mdAttributeConcreteDAOIF.definesAttribute();
        localeMap.put(mdAttributeConcreteDAOIF.definesAttribute(), structDAO.getAttribute(attributeName).getValue());
      }
    }

    return localeMap;
  }

  /**
   * Checks if this attribute is required for its defining BusinessDAO.
   * 
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   * 
   * @param valueToValidate
   *          The value to be checked if its required in the the defining
   *          BusinessDAO
   * @param mdAttributeIF
   *          The Metatdata BusinessDAO that defines the Attribute
   * 
   * @throws EmptyValueProblem
   *           if this attribute is required for its defining BusinessDAO but
   *           contains an empty value.
   */
  public void validateRequired(String valueToValidate, MdAttributeConcreteDAOIF mdAttributeIF)
  {
    List<AttributeIF> attributeIFlist = MdAttributeLocalDAO.findAttributeChainAttributeValueMatch(this, Session.getCurrentLocale());

    boolean isRequired = mdAttributeIF.isRequired();
    boolean isDimensionRequired = false;

    MdDimensionDAOIF mdDimensionDAOIF = Session.getCurrentDimension();

    if (mdDimensionDAOIF != null)
    {
      isDimensionRequired = mdAttributeIF.isDimensionRequired();
    }

    if (isRequired || isDimensionRequired)
    {
      boolean foundValue = false;
      for (com.runwaysdk.dataaccess.AttributeIF attributeIF : attributeIFlist)
      {
        if (!attributeIF.getValue().trim().equals(""))
        {
          foundValue = true;
          break;
        }
      }

      if (!foundValue)
      {
        String error = "Attribute [" + getName() + "] on type [" + getDefiningClassType() + "] requires a value";
        EmptyValueProblem problem = new EmptyValueProblem(this.getContainingComponent().getProblemNotificationId(), mdAttributeIF.definedByClass(), mdAttributeIF, error, this);
        problem.throwIt();
      }
    }
  }

  /**
   * 
   * @return the string OID of the structDAO object. precondition:
   *         this.structDAO is initialized.
   */
  protected String save(boolean validateRequired)
  {
    StructDAO structDAO = this.getStructDAO();
    structDAO.setKey(structDAO.getOid());

    if (!this.isImport())
    {
      return structDAO.save(validateRequired);
    }
    else
    {
      /*
       * IMPORTANT: During import when an MdAttributeLocal definition are added
       * to an existing type with existing objects and the objects are updated
       * to with the attribute local values the transaction log does not contain
       * all of the information required to created a valid entity. Specifically
       * the site master is not included. As such we need to infer the site
       * master as that of the containing entity.
       */
      if (structDAO.isNew() && ( structDAO.getSiteMaster() == null || structDAO.getSiteMaster().length() == 0 ))
      {
        structDAO.setAppliedToDB(false);
        structDAO.getAttribute(StructInfo.SITE_MASTER).setValueNoValidation(this.getContainingComponent().getSiteMaster());
      }

      /*
       * IMPORTANT: During import when multiple MdAttributeLocal definition are
       * added to an existing type with existing objects and the objects are
       * updated to with the only one of the attribute local values the
       * underlying struct objects of the other attribute locals are then
       * created with default values. This breaks synchronization because the
       * default struct values then differ with those on the originating node.
       * As such we only want to apply the underlying struct object when the
       * values have been modified to be the same as the originating node.
       */
      if (this.getContainingComponent().isNew() || !structDAO.isNew() || structDAO.isAppliedToDB() || structDAO.isNonSystemAttributeModified())
      {
        return structDAO.importSave();
      }

      return "";
    }
  }

}
