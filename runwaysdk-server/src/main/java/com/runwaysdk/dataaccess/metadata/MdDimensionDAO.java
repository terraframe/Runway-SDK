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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.constants.MdAttributeDimensionInfo;
import com.runwaysdk.constants.MdAttributeInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdDimensionInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdClassDimensionDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeLocal;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdAction;

public class MdDimensionDAO extends MetadataDAO implements MdDimensionDAOIF
{

  /**
   *
   */
  private static final long serialVersionUID = -1410182427551516394L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdDimensionDAO()
  {
    super();
  }

  /**
   * Constructs a BusinessDAO from the given map of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> ObjectCache.isSubClass(type, Constants.MD_TYPE)
   * 
   * @param attributeMap
   * @param type
   * @param useCache
   */
  public MdDimensionDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Map, String)
   */
  public MdDimensionDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdDimensionDAO(attributeMap, MdDimensionInfo.CLASS);
  }

  /**
   * {@link MdDimensionDAO} does not contribute to the signature of a generated
   * class, so this returns an empty string.
   * 
   * @return empty String
   */
  public String getSignature()
  {
    return "";
  }

  /**
   * Returns the name of this {@link MdDimensionDAO}
   * 
   * @return name of this {@link MdDimensionDAO}
   */
  public String getName()
  {
    return this.getAttributeIF(MdDimensionInfo.NAME).getValue();
  }

  /**
   * Returns the attribute accessor name of this {@link MdDimensionDAO} that is
   * added to local structs.
   * 
   * @return attribute accessor name of this {@link MdDimensionDAO} that is
   *         added to local structs.
   */
  public String getAttributeAccessor()
  {
    return CommonGenerationUtil.lowerFirstCharacter(this.getName());
  }

  /**
   * Returns the name of the attribute that defines the default locale for the
   * dimension.
   * 
   * @return name of the attribute that defines the default locale for the
   *         dimension
   */
  public String getDefaultLocaleAttributeName()
  {
    return this.getAttributeAccessor() + "_" + MdAttributeLocalInfo.DEFAULT_LOCALE;
  }

  /**
   * Returns the name of the attribute that defines the locale for the
   * dimension.
   * 
   * @param locale
   * 
   * @return name of the attribute that defines the locale for the dimension
   */
  public String getLocaleAttributeName(Locale locale)
  {
    return this.getLocaleAttributeName(locale.toString());
  }

  /**
   * Returns the name of the attribute that defines the locale for the
   * dimension.
   * 
   * @param localeToStringValue
   * 
   * @return name of the attribute that defines the locale for the dimension
   */
  public String getLocaleAttributeName(String localeToStringValue)
  {
    return this.getAttributeAccessor() + "_" + localeToStringValue;
  }

  public String getDisplayLabel(Locale locale)
  {
    return ( (AttributeLocal) this.getAttributeIF(MdDimensionInfo.DISPLAY_LABEL) ).getValue(locale);
  }

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   * 
   * @return map where the key is the locale and the value is the localized
   *         String value.
   */
  public Map<String, String> getDisplayLabels()
  {
    return ( (AttributeLocalIF) this.getAttributeIF(MdDimensionInfo.DISPLAY_LABEL) ).getLocalValues();
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdDimensionDAO getBusinessDAO()
  {
    return (MdDimensionDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new {@link MdAction}. Some attributes will contain default
   * values, as defined in the attribute metadata. Otherwise, the attributes
   * will be blank.
   * 
   * @return MdFacade
   */
  public static MdDimensionDAO newInstance()
  {
    return (MdDimensionDAO) BusinessDAO.newInstance(MdDimensionInfo.CLASS);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdDimensionDAOIF get(String id)
  {
    return (MdDimensionDAOIF) BusinessDAO.get(id);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdDimensionDAOIF get(String type, String key)
  {
    return (MdDimensionDAOIF) BusinessDAO.get(type, key);
  }

  public static MdDimensionDAOIF getByName(String name)
  {
    return (MdDimensionDAOIF) BusinessDAO.get(MdDimensionInfo.CLASS, MdDimensionDAO.buildKey(name));
  }

  @Override
  public String apply()
  {
    String key = buildKey(this.getName());
    this.setKey(key);

    return super.apply();
  }

  /**
   * 
   * @param businessContext
   */
  @Override
  public String save(boolean businessContext)
  {
    // Add columns to the local struct classes
    if (this.isNew() && !this.isAppliedToDB() && !this.isImport())
    {
      List<Locale> localeList = SupportedLocaleDAO.getSupportedLocales();

      List<String> ids = SupportedLocaleDAO.getMdAttributeLocalIds();

      for (String mdAttributeLocalId : ids)
      {
        MdAttributeLocalDAO mdAttributeLocal = MdAttributeLocalDAO.get(mdAttributeLocalId).getBusinessDAO();
        MdLocalStructDAOIF mdLocalStructDAOIF = mdAttributeLocal.getMdStructDAOIF();

        if (mdLocalStructDAOIF.definesAttribute(this.getDefaultLocaleAttributeName()) == null)
        {
          mdAttributeLocal.addDefaultLocale(this);
        }

        for (Locale locale : localeList)
        {
          mdAttributeLocal.addLocale(this, locale);
        }
      }
    }

    String id = super.save(businessContext);

    // Add columns to the local struct classes
    if (!this.isImport())
    {
      if (this.isNew() && !this.isAppliedToDB())
      {
        // Add dimensions to all attributes
        QueryFactory qf = new QueryFactory();
        BusinessDAOQuery q = qf.businessDAOQuery(MdAttributeInfo.CLASS);

        OIterator<BusinessDAOIF> i = q.getIterator();

        try
        {
          for (BusinessDAOIF businessDAOIF : i)
          {
            MdAttributeDAOIF mdAttributeDAOIF = (MdAttributeDAOIF) businessDAOIF;

            MdAttributeDimensionDAO mdAttributeDimensionDAO = MdAttributeDimensionDAO.newInstance();
            mdAttributeDimensionDAO.setDefiningMdDimension(this);
            mdAttributeDimensionDAO.setDefiningMdAttribute(mdAttributeDAOIF);
            mdAttributeDimensionDAO.apply();
          }
        }
        finally
        {
          i.close();
        }

        // Add dimensions to all mdClasses
        QueryFactory mdClassFactory = new QueryFactory();
        BusinessDAOQuery mdClassQuery = mdClassFactory.businessDAOQuery(MdClassInfo.CLASS);

        OIterator<BusinessDAOIF> iterator = mdClassQuery.getIterator();

        try
        {
          for (BusinessDAOIF businessDAOIF : iterator)
          {
            MdClassDAOIF mdClassDAOIF = (MdClassDAOIF) businessDAOIF;

            MdClassDimensionDAO mdClassDimensionDAO = MdClassDimensionDAO.newInstance();
            mdClassDimensionDAO.setDefiningMdDimension(this);
            mdClassDimensionDAO.setDefiningMdClass(mdClassDAOIF);
            mdClassDimensionDAO.apply();
          }
        }
        finally
        {
          i.close();
        }
      }
      // !this.isNew() || this.isAppliedToDB()
      else
      {
        Attribute keyAttribute = this.getAttribute(MdDimensionInfo.KEY);
        
        if (keyAttribute.isModified())
        {
          List<MdClassDimensionDAOIF> mdClassDimensions = this.getMdClassDimensions();

          for (MdClassDimensionDAOIF mdClassDimensionDAOIF : mdClassDimensions)
          {
            // The apply method will update the key
            (mdClassDimensionDAOIF.getBusinessDAO()).apply();
          }
        }
      }
    }

    return id;
  }

  @Override
  public void delete(boolean businessContext)
  {
    // Delete all corresponding MdAttributeDimensions
    List<MdAttributeDimensionDAOIF> list = this.getMdAttributeDimensions();

    for (MdAttributeDimensionDAOIF mdAttributeDimension : list)
    {
      mdAttributeDimension.getBusinessDAO().delete();
    }

    // Delete all corresponding MdClassDimensions
    List<MdClassDimensionDAOIF> mdClassDimensions = this.getMdClassDimensions();

    for (MdClassDimensionDAOIF mdClassDimension : mdClassDimensions)
    {
      mdClassDimension.getBusinessDAO().delete();
    }

    // Drop all localized attributes that were defined on local struct types to
    // support this dimension
    for (MdAttributeConcreteDAOIF mdAttributeConcreteDAOIF : this.getLocalStructMdAttributes())
    {
      mdAttributeConcreteDAOIF.getBusinessDAO().delete(businessContext);
    }

    super.delete(businessContext);
  }

  /**
   * Builds the key for the dimension with the given name.
   * 
   * @param dimensionName
   * 
   * @return key for the dimension with the given name.
   */
  public static String buildKey(String dimensionName)
  {
    return MdDimensionInfo.KEY_ROOT + dimensionName;
  }

  public List<MdClassDimensionDAOIF> getMdClassDimensions()
  {
    List<MdClassDimensionDAOIF> list = new ArrayList<MdClassDimensionDAOIF>();
    List<RelationshipDAOIF> relationships = this.getChildren(RelationshipTypes.DIMENSION_HAS_CLASS.getType());

    for (RelationshipDAOIF relationship : relationships)
    {
      list.add((MdClassDimensionDAOIF) relationship.getChild());
    }

    return list;
  }

  public MdClassDimensionDAOIF getMdClassDimension(MdClassDAOIF mdClass)
  {
    String id = mdClass.getId();

    List<MdClassDimensionDAOIF> mdClassDimensions = this.getMdClassDimensions();

    for (MdClassDimensionDAOIF mdClassDimension : mdClassDimensions)
    {
      String _id = mdClassDimension.definingMdClass().getId();

      if (_id.equals(id))
      {
        return mdClassDimension;
      }
    }

    return null;
  }

  public List<MdAttributeDimensionDAOIF> getMdAttributeDimensions()
  {
    List<MdAttributeDimensionDAOIF> returnList = new ArrayList<MdAttributeDimensionDAOIF>();
   
    ResultSet resultSet = Database.getMdAttributeDimensionIds(this.getId());
    
    try
    {
      while (resultSet.next())
      {
        String mdAttrDimensionId = resultSet.getString(MdAttributeDimensionInfo.ID);
        returnList.add(MdAttributeDimensionDAO.get(mdAttrDimensionId));
      }
    }
    catch (SQLException sqlEx1)
    {
      Database.throwDatabaseException(sqlEx1);
    }
    finally
    {
      try
      {
        java.sql.Statement statement = resultSet.getStatement();
        resultSet.close();
        statement.close();
      }
      catch (SQLException sqlEx2)
      {
        Database.throwDatabaseException(sqlEx2);
      }
    }
  
    return returnList; 
    
  }

  /**
   * Returns all {@link MdAttributesConcreteDAO} that were defined on local
   * structs to implement localization.
   * 
   * @return {@link MdAttributesConcreteDAO} that were defined on local structs
   *         to implement localization.
   */
  public List<MdAttributeConcreteDAOIF> getLocalStructMdAttributes()
  {
    List<MdAttributeConcreteDAOIF> mdAttributeList = new LinkedList<MdAttributeConcreteDAOIF>();

    List<RelationshipDAOIF> relationshipList = this.getChildren(RelationshipTypes.DIMENSION_DEFINES_LOCAL_STRUCT_ATTRIBUTE.getType());
    for (RelationshipDAOIF relationshipDAOIF : relationshipList)
    {
      mdAttributeList.add((MdAttributeConcreteDAOIF) relationshipDAOIF.getChild());
    }

    return mdAttributeList;
  }

  public static List<MdDimensionDAOIF> getAllMdDimensions()
  {
    List<MdDimensionDAOIF> dimensionList = new LinkedList<MdDimensionDAOIF>();

    for (String id : EntityDAO.getEntityIdsDB(MdDimensionInfo.CLASS))
    {
      dimensionList.add(MdDimensionDAO.get(id));
    }

    return dimensionList;
  }

  @Override
  public String getPermissionKey()
  {
    return this.getKey();
  }
}
