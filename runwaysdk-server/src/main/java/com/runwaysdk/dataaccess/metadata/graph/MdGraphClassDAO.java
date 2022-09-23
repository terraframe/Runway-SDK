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
package com.runwaysdk.dataaccess.metadata.graph;

import java.util.List;
import java.util.Map;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.constants.BusinessInfo;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.graph.MdGraphClassInfo;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.metadata.DeleteContext;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.MetadataDAO;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdAttributeIndices;
import com.runwaysdk.system.metadata.MdAttributeLocal;
import com.runwaysdk.system.metadata.MdAttributeLocalCharacterEmbedded;

public abstract class MdGraphClassDAO extends MdClassDAO implements MdGraphClassDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -2511111344204829156L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdGraphClassDAO()
  {
    super();
  }

  /**
   * Constructs a {@link MdGraphClassDAO} from the given hashtable of
   * Attributes.
   *
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null <br/>
   *
   *
   * @param attributeMap
   * @param type
   */
  public MdGraphClassDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * @see MdGraphClassDAOIF#getRootMdClassDAO()
   */
  @Override
  public MdGraphClassDAOIF getRootMdClassDAO()
  {
    return (MdGraphClassDAOIF) super.getRootMdClassDAO();
  }

  /**
   * @see MdGraphClassDAOIF#getSubClasses()
   */
  @Override
  public abstract List<? extends MdGraphClassDAOIF> getSubClasses();

  /**
   * @see MdGraphClassDAOIF#getAllConcreteSubClasses()
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<? extends MdGraphClassDAOIF> getAllConcreteSubClasses()
  {
    return (List<? extends MdGraphClassDAOIF>) super.getAllConcreteSubClasses();
  }

  /**
   * @see MdGraphClassDAOIF#getAllSubClasses()
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<? extends MdGraphClassDAOIF> getAllSubClasses()
  {
    return (List<? extends MdVertexDAOIF>) super.getAllSubClasses();
  }

  /**
   * @see MdGraphClassDAOIF#getSuperClass()
   */
  public abstract MdGraphClassDAOIF getSuperClass();

  /**
   * @see MdGraphClassDAOIF#getSuperClasses()
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<? extends MdGraphClassDAOIF> getSuperClasses()
  {
    return (List<MdGraphClassDAOIF>) super.getSuperClasses();
  }

  /**
   * Returns a list of <code>MdAttributeDAOIF</code> objects that this
   * <code>MdClassDAOIF</code> defines.
   * 
   * @return an List of <code>MdAttributeDAOIF</code> objects that this
   *         <code>MdClassDAOIF</code> defines.
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdAttributeConcreteDAOIF> definesAttributes()
  {
    return (List<? extends MdAttributeConcreteDAOIF>) super.definesAttributes();
  }

  @Override
  public boolean isEnableChangeOverTime()
  {
    if (this.hasAttribute(MdGraphClassInfo.ENABLE_CHANGE_OVER_TIME))
    {
      String value = this.getAttribute(MdGraphClassInfo.ENABLE_CHANGE_OVER_TIME).getValue();

      if (value != null && value.length() > 0)
      {
        return Boolean.parseBoolean(value);
      }
    }

    return false;
  }

  /**
   * Returns an {@link MdGraphClassDAOIF} instance of the metadata for the given
   * type.
   * 
   * <br/>
   * <b>Precondition:</b> graphClassType != null <br/>
   * <b>Precondition:</b> !graphClassType.trim().equals("") <br/>
   * <b>Precondition:</b> graphClassType is a valid class defined in the
   * database <br/>
   * <b>Postcondition:</b> Returns a {@link MdGraphClassDAOIF} instance of the
   * metadata for the given class
   * (MdGraphClassDAOIF().definesType().equals(graphClassType)
   * 
   * @param graphClassType
   * @return {@link MdGraphClassDAOIF} instance of the metadata for the given
   *         type.
   */
  public static MdGraphClassDAOIF getMdGraphClassDAO(String transientType)
  {
    return ObjectCache.getMdGraphClassDAO(transientType);
  }

  @Override
  public String apply()
  {
    if (this.isNew() && !this.isAppliedToDB())
    {
      // Supply a class name for the grpah database if one was not provided
      Attribute dbClassNameAttr = this.getAttribute(MdGraphClassInfo.DB_CLASS_NAME);
      if (!dbClassNameAttr.isModified() || dbClassNameAttr.getValue().trim().length() == 0)
      {
        // Create a dbClassName
        String dbClassName = MdTypeDAO.createTableName(MetadataDAO.convertCamelCaseToUnderscore(this.getTypeName()));
        dbClassNameAttr.setValue(dbClassName);
      }
      else
      {
        dbClassNameAttr.setValue(dbClassNameAttr.getValue().toLowerCase());
      }
    }

    return super.apply();
  }

  /**
   *
   */
  public String save(boolean validateRequired)
  {
    boolean applied = this.isAppliedToDB();

    String oid = super.save(validateRequired);

    if (this.isNew() && !applied)
    {
      this.createClassInDB();

      // Define default attributes.
      if (this.isRootOfHierarchy() && !this.isImport())
      {
        this.createDefaultAttributes();
      }
    }

    return oid;
  }

  /**
   * 
   * @param businessContext
   *          true if this is being called from a business context, false
   *          otherwise. If true then cascading deletes of other Entity objects
   *          will happen at the Business layer instead of the data access
   *          layer.
   * 
   */
  @Override
  public void delete(DeleteContext context)
  {
    // Delete subclasses
    this.deleteAllChildClasses(context);

    // Delete all MdMethods defined by this type
    this.dropAllMdMethods();

    // Delete all attribute MdAttribue objects that this type defines
    // delete all attribute metadata for this class
    this.dropAllAttributes(context);

    // Delete all permission tuples that this class participates in
    this.dropTuples();

    // Delete this BusinessDAO
    super.delete(context);

    this.deleteClassInDB();
  }

  protected void createDefaultAttributes()
  {
    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(ComponentInfo.CLASS);
    this.copyAttribute((MdAttributeDAO) mdBusinessIF.definesAttribute(ComponentInfo.OID).getBusinessDAO());

    MdBusinessDAOIF mdElementIF = MdBusinessDAO.getMdBusinessDAO(ElementInfo.CLASS);
    this.copyAttribute((MdAttributeDAO) mdElementIF.definesAttribute(BusinessInfo.SEQUENCE).getBusinessDAO());
  }

  /**
   * Copies the given attribute so that it is also defined by this entity.
   * 
   * @param mdAttributeIFOriginal
   */
  @Override
  public void copyAttribute(MdAttributeDAOIF mdAttributeIFOriginal)
  {
    MdAttributeDAO newMdAttribute;
    
    if (mdAttributeIFOriginal instanceof MdAttributeLocalDAO)
    {
      newMdAttribute = (MdAttributeDAO) createNewFromLocal((MdAttributeLocalDAO) mdAttributeIFOriginal);
    }
    else
    {
      newMdAttribute = (MdAttributeDAO) mdAttributeIFOriginal.copy();
    }

    if (mdAttributeIFOriginal instanceof MdAttributeConcreteDAOIF)
    {
      // The copied attribute is now defined by this entity.
      newMdAttribute.getAttribute(MdAttributeConcreteInfo.DEFINING_MD_CLASS).setValue(this.getOid());
      // Make sure that the unique database constraint for the key attribute is
      // enabled.
      if (newMdAttribute.definesAttribute().equals(ComponentInfo.KEY))
      {
        newMdAttribute.getAttribute(MdAttributeConcreteInfo.INDEX_TYPE).setModified(true);
      }
    }

    if (newMdAttribute.definesAttribute().equals(BusinessInfo.SEQUENCE))
    {
      newMdAttribute.setValue(MdAttributeConcreteInfo.REQUIRED, Boolean.FALSE.toString());
      newMdAttribute.setValue(MdAttributeConcreteInfo.SYSTEM, Boolean.FALSE.toString());
    }

    newMdAttribute.apply();
  }

  private MdAttributeDAO createNewFromLocal(MdAttributeLocalDAO mdAttributeIFOriginalDAO)
  {
    MdAttributeLocal mdAttributeIFOriginal = MdAttributeLocal.get(mdAttributeIFOriginalDAO.getOid());
    MdAttributeLocalCharacterEmbedded embedded = new MdAttributeLocalCharacterEmbedded();
    
    String[] copyAttrs = new String[] { MdAttributeConcrete.ATTRIBUTENAME, MdAttributeConcreteInfo.REQUIRED };
    
    for (String attr : copyAttrs)
    {
      embedded.setValue(attr, mdAttributeIFOriginal.getValue(attr));
    }
    
    List<MdAttributeIndices> indexes = mdAttributeIFOriginal.getIndexType();
    if (indexes.size() > 0 && indexes.get(0).equals(MdAttributeIndices.UNIQUE_INDEX))
    {
      embedded.addIndexType(MdAttributeIndices.UNIQUE_INDEX);
    }
    
    embedded.getDisplayLabel().setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, mdAttributeIFOriginal.getDisplayLabel().getDefaultValue());
    embedded.getDisplayLabel().setLocaleMap(mdAttributeIFOriginal.getDisplayLabel().getLocaleMap());
    
    embedded.getDescription().setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, mdAttributeIFOriginal.getDescription().getDefaultValue());
    embedded.getDescription().setLocaleMap(mdAttributeIFOriginal.getDescription().getLocaleMap());
    
    return (MdAttributeDAO) BusinessFacade.getEntityDAO(embedded);
  }

  /**
   * Creates the class in the graph database.
   */
  protected abstract void createClassInDB();

  /**
   * Deletes the class from the graph database.
   */
  protected abstract void deleteClassInDB();

  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdGraphClassDAO getBusinessDAO()
  {
    return (MdGraphClassDAO) super.getBusinessDAO();
  }

  @Override
  public String getDBClassName()
  {
    return this.getAttribute(MdGraphClassInfo.DB_CLASS_NAME).getValue();
  }

  /*
   * @Override public boolean isAbstract() { // TODO Auto-generated method stub
   * return false; }
   * 
   * @Override public boolean isExtendable() { // TODO Auto-generated method
   * stub return false; }
   * 
   * 
   * @Override public boolean isRootOfHierarchy() { // TODO Auto-generated
   * method stub return false; }
   */

  /**
   * Returns the <code>MdEntityDAOIF</code> instance that defines the given
   * table name.
   * 
   * @param tableName
   * 
   * @return <code>MdEntityDAOIF</code> that defines the table with the given
   *         name.
   */
  public static MdGraphClassDAOIF getMdGraphClassByTableName(String tableName)
  {
    MdGraphClassDAOIF mdEntity = (MdGraphClassDAOIF) ObjectCache.getMdClassByTableName(tableName);

    if (mdEntity == null)
    {
      String error = "Metadata not found that defines table [" + tableName + "]";

      // Feed in the MdEntityDAO for MdEntityDAO. Yes, it's self-describing.
      throw new DataNotFoundException(error, MdEntityDAO.getMdEntityDAO(MdGraphClassInfo.CLASS));
    }

    return mdEntity;
  }

}
