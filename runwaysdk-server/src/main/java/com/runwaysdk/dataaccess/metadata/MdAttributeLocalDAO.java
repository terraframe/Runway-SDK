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
package com.runwaysdk.dataaccess.metadata;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.AttributeDoesNotExistException;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.MdTransientDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.session.Session;

public abstract class MdAttributeLocalDAO extends MdAttributeStructDAO implements MdAttributeLocalDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 7725006433522366704L;

  public MdAttributeLocalDAO()
  {
    super();
  }

  public MdAttributeLocalDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  @Override
  protected void initializeStrategyObject()
  {
    if (this.definedByClass() instanceof MdEntityDAOIF)
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeLocal_E(this));
    }
    else if (this.definedByClass() instanceof MdTransientDAOIF)
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeConcrete_S(this));
    }
    else
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeConcrete_T(this));
    }
  }

  @Override
  public MdAttributeLocalDAO getBusinessDAO()
  {
    return (MdAttributeLocalDAO) super.getBusinessDAO();
  }

  @Override
  public MdLocalStructDAOIF getMdStructDAOIF()
  {
    return (MdLocalStructDAOIF) super.getMdStructDAOIF();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String,
   * java.lang.String)
   */
  public static MdAttributeLocalDAOIF get(String oid)
  {
    return (MdAttributeLocalDAOIF) BusinessDAO.get(oid);
  }

  /**
   * Returns the name of the temp metadata display label for the given column index.
   * 
   * @param columnIndex
   * @return name of the temp metadata display label for the given column index.
   */
  public static String buildMetadataTempColumn(int columnIndex)
  {
    return MdAttributeLocalDAOIF.METADATA_DISPLAY_LABEL_COLUMN_TEMP+"_"+columnIndex;
  }
  
  public boolean definesLocale(Locale locale)
  {
    MdAttributeDAOIF mdAttribute = this.getMdStructDAOIF().definesAttribute(locale.toString());

    return ( mdAttribute != null );
  }

  public boolean definesLocale(MdDimensionDAOIF mdDimensionDAOIF, Locale locale)
  {
    MdAttributeDAOIF mdAttribute = this.getMdStructDAOIF().definesAttribute(mdDimensionDAOIF.getLocaleAttributeName(locale));

    return ( mdAttribute != null );
  }

  public boolean definesDefaultLocale(MdDimensionDAOIF mdDimension)
  {
    String attributeName = mdDimension.getDefaultLocaleAttributeName();
    MdAttributeDAOIF mdAttribute = this.getMdStructDAOIF().definesAttribute(attributeName);

    return ( mdAttribute != null );
  }

  protected boolean definesDefaultLocale()
  {
    return ( this.getMdStructDAOIF().definesAttribute(MdAttributeLocalInfo.DEFAULT_LOCALE) != null );
  }

  public void addLocale(Locale locale)
  {
    if (!this.definesLocale(locale))
    {
      String attributeName = locale.toString();
      String columnName = locale.toString().toLowerCase();
      String displayLabel = locale.getDisplayName(locale);
      String description = this.definesAttribute() + " localized for " + locale.getDisplayName(locale);

      addLocaleWrapper(attributeName, columnName, displayLabel, description, this.getMdStructDAOIF());
    }
  }

  /**
   * Does nothing if the locale is not defined.
   * 
   * @param locale
   */
  public void removeLocale(Locale locale)
  {
    if (this.definesLocale(locale))
    {
      MdAttributeDAOIF mdAttributeDAOIF = this.getMdStructDAOIF().definesAttribute(locale.toString());
      if (mdAttributeDAOIF != null)
      {
        mdAttributeDAOIF.getBusinessDAO().delete();
      }
    }
  }

  public void addDefaultLocale(MdDimensionDAO mdDimensionDAO)
  {
    String attributeName = mdDimensionDAO.getDefaultLocaleAttributeName();
    String columnName = "";
    String displayLabel = "Dimension " + mdDimensionDAO.getName() + " Default Locale";
    String description = "Dimension " + mdDimensionDAO.getName() + " Default Locale";

    MdAttributeDAOIF mdAttributeDAOIF = addLocaleWrapper(attributeName, columnName, displayLabel, description, this.getMdStructDAOIF());
    RelationshipDAO relationshipDAO = mdDimensionDAO.addChild(mdAttributeDAOIF, RelationshipTypes.DIMENSION_DEFINES_LOCAL_STRUCT_ATTRIBUTE.getType());
    String relKey = buildDimensionLocalStructAttrRelKey(mdDimensionDAO, mdAttributeDAOIF);
    relationshipDAO.setKey(relKey);
    relationshipDAO.apply();
  }

  public void addLocale(MdDimensionDAO mdDimensionDAO, Locale locale)
  {
    if (!this.definesLocale(mdDimensionDAO, locale))
    {
      String attributeName = mdDimensionDAO.getLocaleAttributeName(locale);
      String columnName = "";
      String displayLabel = locale.getDisplayName(locale);
      String description = this.definesAttribute() + " localized for " + locale.getDisplayName(locale) + " for Dimension " + mdDimensionDAO.getDisplayLabel(locale);

      MdAttributeDAOIF mdAttributeDAOIF = addLocaleWrapper(attributeName, columnName, displayLabel, description, this.getMdStructDAOIF());
      RelationshipDAO relationshipDAO = mdDimensionDAO.addChild(mdAttributeDAOIF, RelationshipTypes.DIMENSION_DEFINES_LOCAL_STRUCT_ATTRIBUTE.getType());
      String relKey = buildDimensionLocalStructAttrRelKey(mdDimensionDAO, mdAttributeDAOIF);
      relationshipDAO.setKey(relKey);
      relationshipDAO.apply();
    }
  }
  
  public static String buildDimensionLocalStructAttrRelKey(MdDimensionDAOIF mdDimensionDAOIF, MdAttributeDAOIF mdAttributeDAOIF)
  {
    return mdAttributeDAOIF.getKey()+"."+mdDimensionDAOIF.getName();
  }
  
  public abstract void addDefaultLocale();

  protected abstract MdAttributeDAOIF addLocaleWrapper(String attributeName, String columnName, String displayLabel, String description, MdLocalStructDAOIF mdLocalStructDAOIF);

  public void deleteLocale(Locale locale)
  {
    MdAttributeDAOIF mdAttribute = this.getMdStructDAOIF().definesAttribute(locale.toString());
    if (mdAttribute != null)
      ( (MdAttributeDAO) mdAttribute.getBusinessDAO() ).delete();
  }

  /**
   * Finds the closest attributeMatch for the given locale and returns its
   * value.
   * 
   * @param attributeLocalIF
   * @param locale
   * @return closest attributeMatch for the given locale and returns its value.
   */
  public static String findAttributeValueMatch(AttributeLocalIF attributeLocalIF, Locale locale)
  {
    MdLocalStructDAOIF mdLocalStruct = attributeLocalIF.getMdStructDAOIF();

    // localeString array contains 1 or 2 entries: the dimension-specific
    // locale (only if dimension is specified), and the non-dimension locale.
    String[] localeStringArray;

    MdDimensionDAOIF mdDimensionDAOIF = Session.getCurrentDimension();
    if (mdDimensionDAOIF != null)
    {
      localeStringArray = new String[2];
      localeStringArray[0] = mdDimensionDAOIF.getLocaleAttributeName(locale);
      localeStringArray[1] = locale.toString();
    }
    else
    {
      localeStringArray = new String[1];
      localeStringArray[0] = locale.toString();
    }

    boolean firstIterationComplete = false;
    for (String localeString : localeStringArray)
    {
      // Iterate over the locale string, starting with the entire string (most specific) and working down to less specific
      for (int i = localeString.length(); i > 0; i = localeString.lastIndexOf('_', i - 1))
      {
        String subLocale = localeString.substring(0, i);

        // Check to see if this specific locale has been defined
        MdAttributeDAOIF definesSublocale = mdLocalStruct.definesAttribute(subLocale);

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

      // Check the default for the dimension
      if (mdDimensionDAOIF != null && !firstIterationComplete)
      {
        String dimensionDefaultAttr = mdDimensionDAOIF.getDefaultLocaleAttributeName();

        MdAttributeDAOIF definesDimensionDefault = mdLocalStruct.definesAttribute(dimensionDefaultAttr);

        if (definesDimensionDefault != null)
        {
          String dimensionDefaultValue = attributeLocalIF.getValue(dimensionDefaultAttr);
          if (!dimensionDefaultValue.trim().equals(""))
          {
            return dimensionDefaultValue;
          }
        }
      }

      firstIterationComplete = true;
    }

    // No matching locale found. Resort to the default
    return attributeLocalIF.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE);
  }
  
  /**
   * Returns the chain of <code>AttributeIF</code> objects, starting with the closest
   * possible match and going to the default locale for the given locale and 
   * user's current dimension. 
   * 
   * @param attributeLocalIF
   * @param locale
   * @return closest attributeMatch for the given locale and returns its value.
   */
  public static List<AttributeIF> findAttributeChainAttributeValueMatch(AttributeLocalIF attributeLocalIF, Locale locale)
  {
    List<AttributeIF> attributeIFlist = new LinkedList<AttributeIF>();
    
    MdLocalStructDAOIF mdLocalStruct = attributeLocalIF.getMdStructDAOIF();

    // localeString array contains 1 or 2 entries: the dimension-specific
    // locale (only if dimension is specified), and the non-dimension locale.
    String[] localeStringArray;

    MdDimensionDAOIF mdDimensionDAOIF = Session.getCurrentDimension();
    if (mdDimensionDAOIF != null)
    {
      localeStringArray = new String[2];
      localeStringArray[0] = mdDimensionDAOIF.getLocaleAttributeName(locale);
      localeStringArray[1] = locale.toString();
    }
    else
    {
      localeStringArray = new String[1];
      localeStringArray[0] = locale.toString();
    }

    boolean firstIterationComplete = false;
    for (String localeString : localeStringArray)
    {
      // Iterate over the locale string, starting with the entire string (most specific) and working down to less specific
      for (int i = localeString.length(); i > 0; i = localeString.lastIndexOf('_', i - 1))
      {
        String subLocale = localeString.substring(0, i);

        // Check to see if this specific locale has been defined
        MdAttributeDAOIF definesSublocale = mdLocalStruct.definesAttribute(subLocale);

        if (definesSublocale != null)
        {
          try
          {
            attributeIFlist.add(attributeLocalIF.getAttributeIF(subLocale));
          }
          catch (AttributeDoesNotExistException e)
          {
            // METADATA: Inconsistency
          }
        }
      }

      // Check the default for the dimension
      if (mdDimensionDAOIF != null && !firstIterationComplete)
      {
        String dimensionDefaultAttr = mdDimensionDAOIF.getDefaultLocaleAttributeName();

        MdAttributeDAOIF definesDimensionDefault = mdLocalStruct.definesAttribute(dimensionDefaultAttr);

        if (definesDimensionDefault != null)
        {
          attributeIFlist.add(attributeLocalIF.getAttributeIF(dimensionDefaultAttr));
        }
      }

      firstIterationComplete = true;
    }

    // No matching locale found. Resort to the default
    attributeIFlist.add(attributeLocalIF.getAttributeIF(MdAttributeLocalInfo.DEFAULT_LOCALE));

    return attributeIFlist;
  }

}
