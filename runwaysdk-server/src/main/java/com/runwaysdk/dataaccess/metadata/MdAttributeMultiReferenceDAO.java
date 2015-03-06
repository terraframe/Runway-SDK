/**
 * 
 */
package com.runwaysdk.dataaccess.metadata;

import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.MdEntityInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityGenerator;
import com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeReference;
import com.runwaysdk.session.Session;
import com.runwaysdk.transport.metadata.AttributeMultiReferenceMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;
import com.runwaysdk.transport.metadata.caching.AttributeMultiReferenceMdSession;

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
public class MdAttributeMultiReferenceDAO extends MdAttributeConcreteDAO implements MdAttributeMultiReferenceDAOIF
{
  /**
  *
  */
  private static final long serialVersionUID = 5175941555575362814L;

  public MdAttributeMultiReferenceDAO()
  {
    super();
  }

  /**
   * Constructs a BusinessDAO from the given hashtable of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> classType != null
   * 
   * 
   * @param attributeMap
   * @param classType
   */
  public MdAttributeMultiReferenceDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Map, String)
   */
  public MdAttributeMultiReferenceDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeMultiReferenceDAO(attributeMap, classType);
  }

  /**
   * Returns the signature of the metadata.
   * 
   * @return signature of the metadata.
   */
  public String getSignature()
  {
    return super.getSignature() + " MultiReference:" + this.getReferenceMdBusinessDAO().definesType();
  }

  /**
   * Returns the default value for the attribute that this metadata defines. If
   * no default value has been defined, an empty string is returned.
   * 
   * @return the default value for the attribute that this metadata defines.
   */
  public String getDefaultValue()
  {
    return getDefaultValue(this.getAttributeIF(MdAttributeMultiReferenceInfo.DEFAULT_VALUE).getValue());
  }

  /**
   * Returns the type of AttributeMdDTO this MdAttributeMultiReference requires
   * at the DTO Layer.
   * 
   * @return class name of the AttributeMdDTO to represent this
   *         MdAttributeMultiReference
   */
  @Override
  public String attributeMdDTOType()
  {
    return AttributeMultiReferenceMdDTO.class.getName();
  }

  @Override
  protected void initializeStrategyObject()
  {
    if (this.definedByClass() instanceof MdEntityDAOIF)
    {
      this.mdAttributeStrategy = new MdAttributeMultiReference_E(this);
    }
    else
    {
      this.mdAttributeStrategy = new MdAttributeMultiReference_S(this);
    }
  }

  @Override
  protected String generatedServerGetter(String attributeName)
  {
    return generateTypesafeFormatting("this.getMultiItems(" + attributeName.toUpperCase() + ")");
  }

  /**
   * Typically called for java class generation, but Enum atributes require
   * special logic, which is contained in the generator. Included only to
   * satisfy the interface, this method should never be called, and will throw
   * an exception if it is.
   * 
   * @return nothing
   * @throws ForbiddenMethodException
   *           if called
   */
  @Override
  public String generatedServerSetter()
  {
    throw new ForbiddenMethodException("MdAttributeMultiReference.setString() should never be called.");
  }

  @Override
  protected String generatedServerSetter(String attributeName)
  {
    throw new ForbiddenMethodException("MdAttributeMultiReference.setString() should never be called.");
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.metadata.MdAttribute#generateTypesafeFormatting
   * (java.lang.String)
   */
  protected String generateTypesafeFormatting(String formatMe)
  {
    return "(java.util.List<" + this.getReferenceMdBusinessDAO().definesType() + ">) " + formatMe;
  }

  /**
   * Returns a string representing the query attribute class for attributes of
   * this type.
   * 
   * @return string representing the query attribute class for attributes of
   *         this type.
   */
  public String queryAttributeClass()
  {
    return com.runwaysdk.query.SelectableMultiReference.class.getName();
  }

  /**
   * Called for java class generation. Returns the java type of this attribute
   * (String), which is used in the generated classes for type safety.
   * 
   * @return The java type of this attribute (String)
   */
  public String javaType(boolean isDTO)
  {
    return this.getReferenceMdBusinessDAO().definesType();
  }

  /**
   * Returns the metadata object that defines the MdBusiness type that this
   * attribute referenes, or null if it does not reference anything.
   * 
   * @return the metadata object that defines the MdBusiness type that this
   *         attribute referenes, or null if it does not reference anything.
   */
  public MdBusinessDAOIF getReferenceMdBusinessDAO()
  {
    if (this.getAttributeIF(MdAttributeMultiReferenceInfo.REF_MD_ENTITY).getValue().trim().equals(""))
    {
      return null;
    }
    else
    {
      AttributeReference attributeReference = (AttributeReference) this.getAttributeIF(MdAttributeMultiReferenceInfo.REF_MD_ENTITY);

      return (MdBusinessDAOIF) attributeReference.dereference();
    }
  }

  /**
   * Deletes the MdAttributeMultiReference records but does not delete mappings
   * between instances of this attribute and items in the enumeration list.
   * 
   * @param businessContext
   *          true if this is being called from a business context, false
   *          otherwise. If true then cascading deletes of other Entity objects
   *          will happen at the Business layer instead of the data access
   *          layer.
   * 
   */
  public void deleteButDoNotDeleteMappingInstances(boolean businessContext)
  {
    MdAttributeConcreteStrategy mdAttributeStrategy = this.getMdAttributeStrategy();

    if (mdAttributeStrategy instanceof MdAttributeMultiReference_E)
    {
      MdAttributeMultiReference_E mdAttributeMultiReference_E = (MdAttributeMultiReference_E) mdAttributeStrategy;
      mdAttributeMultiReference_E.deleteInstances = false;
    }

    this.delete(businessContext);
  }

  /**
   * Sets appliedToDB to false if the object is new, as the database will
   * rollback any newly inserted records.
   * 
   */
  public void rollbackState()
  {
    super.rollbackState();

    MdAttributeConcreteStrategy mdAttributeStrategy = this.getMdAttributeStrategy();

    if (mdAttributeStrategy instanceof MdAttributeMultiReference_E)
    {
      MdAttributeMultiReference_E mdAttributeMultiReference_E = (MdAttributeMultiReference_E) mdAttributeStrategy;
      mdAttributeMultiReference_E.deleteInstances = true;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeMultiReferenceDAO getBusinessDAO()
  {
    return (MdAttributeMultiReferenceDAO) super.getBusinessDAO();
  }

  public void setRandomValue(EntityDAO object)
  {
    List<String> referenceIDs = EntityDAO.getEntityIdsDB(this.getReferenceMdBusinessDAO().definesType());
    int index = EntityGenerator.getRandom().nextInt(referenceIDs.size());
    object.setValue(definesAttribute(), referenceIDs.get(index));
  }

  /**
 *
 */
  public String save(boolean validateRequired)
  {
    boolean applied = this.isAppliedToDB();

    if (this.isNew() && !applied)
    {
      // Supply a table name if one was not provided
      Attribute tableNameAttribute = this.getAttribute(MdEntityInfo.TABLE_NAME);
      if (!tableNameAttribute.isModified() || tableNameAttribute.getValue().trim().length() == 0)
      {
        // Create a table name
        String tableName = MetadataDAO.convertCamelCaseToUnderscore(this.definedByClass().getTypeName());
        tableName += "_" + MetadataDAO.convertCamelCaseToUnderscore(this.definesAttribute());
        tableName = MdTypeDAO.createTableName(tableName);

        tableNameAttribute.setValue(tableName);
      }
      else
      {
        tableNameAttribute.setValue(tableNameAttribute.getValue().toLowerCase());
      }
    }

    return super.save(validateRequired);
  }

  /**
   * Returns a new BusinessDAO. Some attributes will contain default values, as
   * defined in the attribute metadata. Otherwise, the attributes will be blank.
   * 
   * @return BusinessDAO instance of MdAttributeMultiReference
   */
  public static MdAttributeMultiReferenceDAO newInstance()
  {
    return (MdAttributeMultiReferenceDAO) BusinessDAO.newInstance(MdAttributeMultiReferenceInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String,
   * java.lang.String)
   */
  public static MdAttributeMultiReferenceDAOIF get(String id)
  {
    return (MdAttributeMultiReferenceDAOIF) BusinessDAO.get(id);
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitMultiReference(this);
  }

  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession()
  {
    AttributeMultiReferenceMdSession attrSes = new AttributeMultiReferenceMdSession(this.getReferenceMdBusinessDAO().getId(), this.getReferenceMdBusinessDAO().getDisplayLabel(Session.getCurrentLocale()));
    super.populateAttributeMdSession(attrSes);

    return attrSes;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF#getTableName()
   */
  @Override
  public String getTableName()
  {
    return this.getAttribute(MdAttributeMultiReferenceInfo.TABLE_NAME).getValue();
  }
  
  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeMultiReferenceDAOIF.class.getName();
  }
  
}
