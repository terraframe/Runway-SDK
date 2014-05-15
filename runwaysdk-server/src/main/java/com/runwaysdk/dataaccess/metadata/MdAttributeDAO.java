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

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.constants.MdAttributeDimensionInfo;
import com.runwaysdk.constants.MdAttributeInfo;
import com.runwaysdk.constants.MdDimensionInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeBoolean;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionFacade;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public abstract class MdAttributeDAO extends MetadataDAO implements MdAttributeDAOIF
{
  /**
   *
   */
  private static final long                     serialVersionUID = -1594689949351852914L;

  /**
   * A comparator that sorts <code>MdAttributeDAOIF</code> alphabetically
   */
  protected static Comparator<MdAttributeDAOIF> alphabetical     = new Comparator<MdAttributeDAOIF>()
                                                                 {
                                                                   public int compare(MdAttributeDAOIF o1, MdAttributeDAOIF o2)
                                                                   {
                                                                     return o1.definesAttribute().compareTo(o2.definesAttribute());
                                                                   }
                                                                 };
  
 /** 
  * Cached attribute values from <code>MdAttributeDimensionDAOIF</code> objects assigned 
  * to this attribute. 
  * 
  * Key: Id of the <code>MdDimensionDAOIF</code> object for the dimension
  * Value: <code>MdAttributeDimensionCache</code> object containing attribute values for the dimension. 
  */                         
  private Map<String, MdAttributeDimensionCache> mdAttributeDimensionMap;
  
 /** 
   * The default constructor, does not set any attributes
   */
  public MdAttributeDAO()
  {
    super();
    this.mdAttributeDimensionMap = new HashMap<String, MdAttributeDimensionCache>();
  }
  
  /**
   * Constructs a BusinessDAO from the given hashtable of Attributes.
   *
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b>ObjectCache.isSubTypeOf(classType, Constants.MD_CLASS)
   *
   * @param attributeMap
   * @param classType
   */
  public MdAttributeDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
    this.mdAttributeDimensionMap = new HashMap<String, MdAttributeDimensionCache>();
  }

  /**
   * Used for client-side metadata caching.
   */
  protected void populateAttributeMdSession(AttributeMdSession attrMd) 
  {
    attrMd.setAttributeName(this.definesAttribute());
    attrMd.setDescription(this.getDescription(Session.getCurrentLocale()));
    attrMd.setDisplayLabel(this.getDisplayLabel(Session.getCurrentLocale()));
    attrMd.setImmutable(this.isImmutable());
    attrMd.setRequired(this.isRequiredForDTO());
    attrMd.setDefaultValue(this.getDefaultValue());
    attrMd.setSystem(this.isSystem());
    
    Session session = (Session)Session.getCurrentSession();
    attrMd.setWritable(SessionFacade.checkAttributeAccess(session, Operation.WRITE, this));
    
    if (this instanceof MdAttributeHashDAO) {
      attrMd.setReadable(false);
    }
    else {
      attrMd.setReadable(SessionFacade.checkAttributeAccess(session, Operation.READ, this));
    }
  }
 
  
  /**
   * Returns the signature of the metadata.
   *
   * @return signature of the metadata.
   */
  public String getSignature()
  {
    return "Name:" + this.definesAttribute() + " Type:" + this.getType() + " Getter:" + this.getGetterVisibility().getJavaModifier() + " Setter:" + this.getSetterVisibility().getJavaModifier();
  }

  public boolean getGenerateAccessor()
  {
    MdAttributeConcreteDAOIF concrete = this.getMdAttributeConcrete();

    return Boolean.parseBoolean(concrete.getValue(MdAttributeInfo.GENERATE_ACCESSOR));
  }

  /**
   * Returns the visibility modifier of the getter.
   *
   * @return the visibility modifier of the getter.
   */
  public abstract VisibilityModifier getGetterVisibility();

  @Override
  public String apply()
  {
    this.setAndBuildKey();

    return super.apply();
  }

  public String save(boolean validateRequired)
  {
    boolean first = this.isNew() && !this.isAppliedToDB() && !this.isImport();

    String id = super.save(validateRequired);

    // Add columns to the local struct classes
    if (first)
    {
      // Add dimensions to all attributes
      QueryFactory qf = new QueryFactory();
      BusinessDAOQuery q = qf.businessDAOQuery(MdDimensionInfo.CLASS);

      OIterator<BusinessDAOIF> i = q.getIterator();

      // Add attribute dimensions for
      try
      {
        for (BusinessDAOIF businessDAOIF : i)
        {
          MdDimensionDAOIF mdDimensionDAOIF = (MdDimensionDAOIF) businessDAOIF;
          MdAttributeDimensionDAO mdAttributeDimensionDAO = MdAttributeDimensionDAO.newInstance();
          mdAttributeDimensionDAO.setDefiningMdDimension(mdDimensionDAOIF);
          mdAttributeDimensionDAO.setDefiningMdAttribute(this);
          mdAttributeDimensionDAO.apply();
        }
      }
      finally
      {
        i.close();
      }
    }

    Attribute attributeKey = this.getAttribute(MdAttributeInfo.KEY);
    if (attributeKey.isModified())
    {
      List<RelationshipDAOIF> relList = this.getParents(RelationshipTypes.DIMENSION_DEFINES_LOCAL_STRUCT_ATTRIBUTE.getType());
      
      for (RelationshipDAOIF relationshipDAOIF : relList)
      {
        RelationshipDAO relationshipDAO = relationshipDAOIF.getRelationshipDAO();
        relationshipDAO.setKey(attributeKey.getValue());
        relationshipDAO.apply();
      }
    }
   
    return id;
  }

  /**
   * This is a hook method for aspects. It records the resultant key in a cache.
   *
   */
  private void setAndBuildKey()
  {
    this.setKey(buildKey(this.definedByClass().definesType(), this.definesAttribute()));
  }

  /**
   * Returns the key for attributes.
   *
   * @param definedByType
   *          type that defines the attribute
   * @param attributeName
   * @return key
   */
  public static String buildKey(String definedByType, String attributeName)
  {
    return definedByType + "." + attributeName;
  }

  /**
   * Returns the <code>MdAttributeIF</code> with the given key.
   *
   * @param key
   * @return <code>MdAttributeIF</code> with the given key.
   */
  public static MdAttributeDAOIF getByKey(String key)
  {
    return ObjectCache.getMdAttributeDAOWithKey(key);
  }

  /**
   * Validates this metadata object.
   *
   * @throws DataAccessException
   *           when this MetaData object is not valid.
   */
  protected void validateAttributeName(String name)
  {
    MetadataDAO.validateName(name);

    // check for reserved words
    if (!this.isImport() && ReservedWords.javaContains(name))
    {
      throw new ReservedWordException("The name [" + name + "] is reserved.", name, ReservedWordException.Origin.ATTRIBUTE);
    }
  }

  public static MdAttributeDAOIF get(String id)
  {
    return (MdAttributeDAOIF) BusinessDAO.get(id);
  }

  @Override
  public void delete(boolean businessContext)
  {
    // Delete all of the MdAttributeDimension definitions

    List<MdAttributeDimensionDAOIF> mdAttributeDimensions = this.getMdAttributeDimensions();

    for (MdAttributeDimensionDAOIF mdAttributeDimension : mdAttributeDimensions)
    {
      mdAttributeDimension.getBusinessDAO().delete(businessContext);
    }

    super.delete(businessContext);
  }

  /**
   * Returns all attribute dimensions for this attribute.
   *
   * @return all attribute dimensions for this attribute.
   */
  public synchronized List<MdAttributeDimensionDAOIF> getMdAttributeDimensions()
  {
    List<MdAttributeDimensionDAOIF> list = new ArrayList<MdAttributeDimensionDAOIF>();

//    if (ObjectCache.getCachedEntityDAOs(MdDimensionInfo.CLASS).size() > 0)
//    if (this.definedByClass().getMdClassDimensions().size() > 0)
    {
      // A dimension has been defined, but the local attribute dimension map has not been initialized
      if (this.mdAttributeDimensionMap.keySet().size() == 0)
      {
        this.initializeMdAttributeDimensionMap();
      }
      
      for (MdAttributeDimensionCache mdAttributeDimensionCache : this.mdAttributeDimensionMap.values())
      {
        list.add(MdAttributeDimensionDAO.get(mdAttributeDimensionCache.getId()));
      }
    }
    
    return list;

// Heads up: test
//    List<MdAttributeDimensionDAOIF> list = new ArrayList<MdAttributeDimensionDAOIF>();
//    List<RelationshipDAOIF> relationships = this.getChildren(RelationshipTypes.ATTRIBUTE_HAS_DIMENSION.getType());
//
//    for (RelationshipDAOIF relationship : relationships)
//    {
//      list.add((MdAttributeDimensionDAOIF) relationship.getChild());
//    }
//
//    return list;
  }
  
  /**
   * Returns the attribute dimension for the given dimension.
   *
   * @param mdDimension
   *          dimension
   *
   * @return attribute dimension for the given dimension.
   */
  public synchronized MdAttributeDimensionDAOIF getMdAttributeDimension(MdDimensionDAOIF mdDimension)
  {
    MdAttributeDimensionDAOIF mdAttributeDimensionDAOIF = null;
    
    // A dimension has been defined, but the local attribute dimension map has not been initialized
    if (this.mdAttributeDimensionMap.keySet().size() == 0)
    {
      this.initializeMdAttributeDimensionMap();
    }
   
    MdAttributeDimensionCache mdAttributeDimensionCache = this.mdAttributeDimensionMap.get(mdDimension.getId());

    if (mdAttributeDimensionCache != null)
    {
      mdAttributeDimensionDAOIF = MdAttributeDimensionDAO.get(mdAttributeDimensionCache.getId());
    }
       
    return mdAttributeDimensionDAOIF;
  }

  /**
   * Initialized the cache of the <code>MdAttributeDimensionDAOIF</code> fields.
   */
  private synchronized void initializeMdAttributeDimensionMap()
  {
    
    ResultSet resultSet = Database.getMdAttributeDimensionFields(this.getId());
    
    try
    {
      while (resultSet.next())
      {
        String mdAttrDimensionId = resultSet.getString(MdAttributeDimensionInfo.ID);
        String stringBooleanValue = resultSet.getString(MdAttributeDimensionInfo.REQUIRED);
        String defaultValue = resultSet.getString(MdAttributeDimensionDAOIF.DEFAULT_VALUE_COLUMN);
        boolean isRequired = AttributeBoolean.getBooleanValue(stringBooleanValue);
        String mdDefiningDimensionId = resultSet.getString(MdAttributeDimensionDAOIF.DEFINING_MD_DIMENSION_COLUMN);

        this.addAttributeDimension(mdAttrDimensionId, isRequired, defaultValue, mdDefiningDimensionId);
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
  }
  
  /**
   * Returns true if the current session is associated with a dimension and that
   * dimension requires this attribute.
   *
   * @return true if the current session is associated with a dimension and that
   *         dimension requires this attribute.
   */
  public boolean isDimensionRequired()
  {
    MdDimensionDAOIF mdDimensionDAOIF = Session.getCurrentDimension();

    if (mdDimensionDAOIF != null)
    {
      MdAttributeDimensionDAOIF mdAttributeDimensionDAOIF = this.getMdAttributeDimension(mdDimensionDAOIF);
      if (mdAttributeDimensionDAOIF != null)
      {
        return mdAttributeDimensionDAOIF.isRequired();
      }
    }

    return false;
  }

  /**
   * Returns true if the metadata for this attribute on a DTO needs to be marked
   * as required, false otherwise.
   *
   * @return true if the metadata for this attribute on a DTO needs to be marked
   *         as required, false otherwise.
   */
  public boolean isRequiredForDTO()
  {
    return this.isRequired() || this.isDimensionRequired();
  }

  /**
   * Returns the default value for the session's dimension. If there is no
   * default value specified or there is no dimension attached to the session,
   * then an empty string is returned.
   *
   * @return default value for the session's dimension.
   */
  public String getDimensionDefaultValue()
  {
    MdDimensionDAOIF mdDimensionDAOIF = Session.getCurrentDimension();

    if (mdDimensionDAOIF != null)
    {
      MdAttributeDimensionDAOIF mdAttributeDimensionDAOIF = this.getMdAttributeDimension(mdDimensionDAOIF);
      if (mdAttributeDimensionDAOIF != null)
      {
        return mdAttributeDimensionDAOIF.getDefaultValue();
      }
    }

    return "";
  }

  /**
   * If a default value has been defined for a dimension attached to this
   * session, then that value is returned, otherwise the default value assigned
   * to the attribute definition is returned.
   *
   * @return default value
   */
  public String getAttributeInstanceDefaultValue()
  {
    String dimensionDefaultValue = "";

    MdDimensionDAOIF mdDimensionDAOIF = Session.getCurrentDimension();

    if (mdDimensionDAOIF != null)
    {
      MdAttributeDimensionDAOIF mdAttributeDimensionDAOIF = this.getMdAttributeDimension(mdDimensionDAOIF);
      if (mdAttributeDimensionDAOIF != null)
      {
        dimensionDefaultValue = mdAttributeDimensionDAOIF.getDefaultValue();
      }
    }

    String defaultValue = this.getDefaultValue();

    if (dimensionDefaultValue.trim().length() != 0)
    {
      return dimensionDefaultValue;
    }
    else
    {
      return defaultValue;
    }
  }
  
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeDAO getBusinessDAO()
  {    
    MdAttributeDAO mdAttributeDAO = (MdAttributeDAO) super.getBusinessDAO();
    
    for (String mdDimensionId : this.mdAttributeDimensionMap.keySet())
    {
      MdAttributeDimensionCache mdAttributeDimensionCache = this.mdAttributeDimensionMap.get(mdDimensionId);
      mdAttributeDAO.mdAttributeDimensionMap.put(mdDimensionId, mdAttributeDimensionCache.clone());
    }
    
    return mdAttributeDAO;
  }
  
  /**
   * Cache important attributes for an <code>MdAttributeDimensionDAOIF</code>.
   * 
   * @param _mdAttributeDimensionId
   * @param _required
   * @param _defaultValue
   * @param _mdDimensionId
   */
  public synchronized void addAttributeDimension(String _mdAttributeDimensionId, boolean _required, String _defaultValue, String _mdDimensionId)
  {
    MdAttributeDimensionCache mdAttrDimCache = new MdAttributeDimensionCache(_mdAttributeDimensionId, _required, _defaultValue);
    this.mdAttributeDimensionMap.put(_mdDimensionId, mdAttrDimCache);
  }
  
  /**
   * Remove the cached information for an <code>MdAttributeDimensionDAOIF</code> associated 
   * with the dimension of the given id.
   * 
   * @param _mdDimensionId
   */
  public synchronized void removeMdAttributeDimension(String _mdDimensionId)
  {
    this.mdAttributeDimensionMap.remove(_mdDimensionId);
  }
  
  public static class MdAttributeDimensionCache implements Serializable
  {
    /**
     * 
     */
    private static final long serialVersionUID = 603568053768026277L;
    
    private String    id;
    private boolean   required;
    private String    defaultValue;
    
    private MdAttributeDimensionCache(String _mdAttributeDimensionId, boolean _required, String _defaultValue)
    {
      this.id             = _mdAttributeDimensionId;
      this.required       = _required;
      this.defaultValue   = _defaultValue;
    }
    
    public String getId()
    {
      return this.id;
    }
    
    public boolean isRequired()
    {
      return this.required;
    }
    
    public String getDefaultValue()
    {
      return this.defaultValue;
    }
    
    public MdAttributeDimensionCache clone()
    {
      return new MdAttributeDimensionCache(this.getId(), this.isRequired(), this.getDefaultValue()); 
    }
    
  }
}
