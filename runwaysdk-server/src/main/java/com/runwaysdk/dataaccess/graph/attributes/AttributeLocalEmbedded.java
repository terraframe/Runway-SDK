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
package com.runwaysdk.dataaccess.graph.attributes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.AttributeDoesNotExistException;
import com.runwaysdk.dataaccess.ComponentDAO;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEmbeddedDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.attributes.EmptyValueProblem;
import com.runwaysdk.session.Session;

public abstract class AttributeLocalEmbedded extends AttributeEmbedded
{
  /**
   * 
   */
  private static final long serialVersionUID = -4481544615179246050L;

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String)
   */
  protected AttributeLocalEmbedded(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass)
  {
    super(mdAttributeDAOIF, definingGraphClass);
  }

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String, ComponentDAO)
   */
  protected AttributeLocalEmbedded(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass, ComponentDAO value)
  {
    super(mdAttributeDAOIF, definingGraphClass, value);
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by a concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return {@link MdAttributeEmbeddedDAOIF} that defines the this attribute
   */
  public MdAttributeEmbeddedDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeEmbeddedDAOIF) super.getMdAttributeConcrete();
  }

  /**
   * Returns the localized string that is the best fit for the given locale.
   * 
   * Duplicated in <code>LocalStruct</code>
   */
  public String getValue(Locale locale)
  {
    return AttributeLocalEmbedded.findAttributeValueMatch(this, locale);
  }

  @Override
  public String getValue(String attributeName)
  {
    return (String) super.getValue(attributeName);
  }

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   * 
   * 
   * @return map where the key is the locale and the value is the localized
   *         String value.
   */
  public Map<String, String> getLocalValues()
  {
    return getLocalValues(this.getObjectValue());
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
    List<Attribute> attributeIFlist = AttributeLocalEmbedded.findAttributeChainAttributeValueMatch(this, Session.getCurrentLocale());

    boolean isRequired = mdAttributeIF.isRequired();

    if (isRequired)
    {
      boolean foundValue = false;

      for (Attribute attributeIF : attributeIFlist)
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
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   * 
   * @param vertexDAO
   * 
   * @return map where the key is the locale and the value is the localized
   *         String value.
   */
  public static Map<String, String> getLocalValues(ComponentDAO vertexDAO)
  {
    Map<String, String> localeMap = new HashMap<String, String>();

    List<? extends MdAttributeDAOIF> attributes = vertexDAO.getMdClassDAO().getAllDefinedMdAttributes();

    for (MdAttributeDAOIF mdAttributeConcreteDAOIF : attributes)
    {
      if (!mdAttributeConcreteDAOIF.isSystem())
      {
        String attributeName = mdAttributeConcreteDAOIF.definesAttribute();

        localeMap.put(mdAttributeConcreteDAOIF.definesAttribute(), ( (Attribute) vertexDAO.getAttributeIF(attributeName) ).getValue());
      }
    }

    return localeMap;
  }

  /**
   * Finds the closest attributeMatch for the given locale and returns its
   * value.
   * 
   * @param attributeLocalIF
   * @param locale
   * @return closest attributeMatch for the given locale and returns its value.
   */
  public static String findAttributeValueMatch(AttributeLocalEmbedded attributeLocalIF, Locale locale)
  {
    MdClassDAOIF embeddedMdClass = attributeLocalIF.getMdAttributeConcrete().getEmbeddedMdClassDAOIF();

    // localeString array contains 1 or 2 entries: the dimension-specific
    // locale (only if dimension is specified), and the non-dimension locale.
    String[] localeStringArray;

    localeStringArray = new String[1];
    localeStringArray[0] = locale.toString();

    for (String localeString : localeStringArray)
    {
      // Iterate over the locale string, starting with the entire string (most
      // specific) and working down to less specific
      for (int i = localeString.length(); i > 0; i = localeString.lastIndexOf('_', i - 1))
      {
        String subLocale = localeString.substring(0, i);

        // Check to see if this specific locale has been defined
        MdAttributeDAOIF definesSublocale = embeddedMdClass.definesAttribute(subLocale);

        if (definesSublocale != null)
        {
          try
          {
            String subLocaleValue = attributeLocalIF.getValue(subLocale);

            // If we've found a match, we can return it
            if (!subLocaleValue.trim().equals(""))
            {
              return subLocaleValue;
            }
          }
          catch (AttributeDoesNotExistException e)
          {
            // METADATA: Inconsistency
          }
        }
      }
    }

    // No matching locale found. Resort to the default
    return attributeLocalIF.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE);
  }

  /**
   * Returns the chain of <code>AttributeIF</code> objects, starting with the
   * closest possible match and going to the default locale for the given locale
   * and user's current dimension.
   * 
   * @param attributeLocalIF
   * @param locale
   * @return closest attributeMatch for the given locale and returns its value.
   */
  public static List<Attribute> findAttributeChainAttributeValueMatch(AttributeLocalEmbedded attributeLocalIF, Locale locale)
  {
    List<Attribute> attributeIFlist = new LinkedList<Attribute>();

    MdClassDAOIF embeddedMdClass = attributeLocalIF.getMdAttributeConcrete().getEmbeddedMdClassDAOIF();

    // localeString array contains 1 or 2 entries: the dimension-specific
    // locale (only if dimension is specified), and the non-dimension locale.
    String[] localeStringArray;
    localeStringArray = new String[1];
    localeStringArray[0] = locale.toString();

    for (String localeString : localeStringArray)
    {
      // Iterate over the locale string, starting with the entire string (most
      // specific) and working down to less specific
      for (int i = localeString.length(); i > 0; i = localeString.lastIndexOf('_', i - 1))
      {
        String subLocale = localeString.substring(0, i);

        // Check to see if this specific locale has been defined
        MdAttributeDAOIF definesSublocale = embeddedMdClass.definesAttribute(subLocale);

        if (definesSublocale != null)
        {
          try
          {
            attributeIFlist.add(attributeLocalIF.getAttribute(subLocale));
          }
          catch (AttributeDoesNotExistException e)
          {
            // METADATA: Inconsistency
          }
        }
      }
    }

    // No matching locale found. Resort to the default
    attributeIFlist.add(attributeLocalIF.getAttribute(MdAttributeLocalInfo.DEFAULT_LOCALE));

    return attributeIFlist;
  }

}
