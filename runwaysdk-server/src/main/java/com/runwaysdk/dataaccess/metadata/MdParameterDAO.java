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

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdParameterInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MethodActorInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.AttributeReferenceIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.MetadataDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeLocal;

public class MdParameterDAO extends MetadataDAO implements MdParameterDAOIF
{
  /**
   * Eclipse auto generated UID
   */
  private static final long serialVersionUID = 6618855689529527646L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdParameterDAO()
  {
    super();
  }

  /**
   * Returns the signature of the metadata.
   * 
   * @return signature of the metadata.
   */
  public String getSignature()
  {
    return "Name: " + this.getParameterName() + " Type: " + this.getParameterType().getType();
  }

  /**
   * Constructs a BusinessDAO from the given hashtable of Attributes.
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
  public MdParameterDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdParameterDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdParameterDAO(attributeMap, MdParameterInfo.CLASS);
  }

  /**
   * Returns the display label of this metadata object
   * 
   * @param locale
   * 
   * @return the display label of this metadata object
   */
  public String getDisplayLabel(Locale locale)
  {
    return ( (AttributeLocal) this.getAttributeIF(MdParameterInfo.DISPLAY_LABEL) ).getValue(locale);
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
    return ( (AttributeLocalIF) this.getAttributeIF(MdTypeInfo.DISPLAY_LABEL) ).getLocalValues();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdParameterDAO getBusinessDAO()
  {
    return (MdParameterDAO) super.getBusinessDAO();
  }

  /**
   * @return
   */
  private String getMdMethodId()
  {
    Attribute attribute = this.getAttribute(MdParameterInfo.ENCLOSING_METADATA);
    return attribute.getValue();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.MdParameterIF#getEnclosingMetadata()
   */
  public ParameterMarker getEnclosingMetadata()
  {
    return (ParameterMarker) ( (AttributeReferenceIF) this.getAttribute(MdParameterInfo.ENCLOSING_METADATA) ).dereference();
  }

  public String getParameterName()
  {
    Attribute attribute = this.getAttribute(MdParameterInfo.NAME);
    return attribute.getValue();
  }

  public Type getParameterType()
  {
    Attribute attribute = this.getAttribute(MdParameterInfo.TYPE);
    String value = attribute.getValue();

    return new Type(value);
  }

  public String getParameterOrder()
  {
    Attribute attribute = this.getAttribute(MdParameterInfo.ORDER);
    return attribute.getValue();
  }

  /**
   * IMPORTANT: Do not delete this method. It is a hook for apsects.
   */
  public String save(boolean validateRequired)
  {
    return super.save(validateRequired);
  }

  @Override
  public String apply()
  {
    validateReference();
    validateName();
    validateType();
    validateOrder();

    ParameterMarker marker = this.getEnclosingMetadata();

    String key = MdParameterDAO.buildKey(marker.getEnclosingMdTypeDAO().definesType(), marker.getName(), this.getParameterName());
    this.setKey(key);

    boolean isAppliedToDB = this.isAppliedToDB();
    
    String oid = super.apply();

    // Create the appropriate relationship between this MdParameter and its
    // MdMethod
    if (this.isNew() && !isAppliedToDB )
    {
      if (!this.isImport())
      {
        String relationshipType = RelationshipTypes.METADATA_PARAMETER.getType();
        String mdMethodId = this.getMdMethodId();

        RelationshipDAO relationshipDAO = RelationshipDAO.newInstance(mdMethodId, oid, relationshipType);
        relationshipDAO.setKey(key);
        relationshipDAO.apply();
      }
    }
    else
    {
      Attribute keyAttribute = this.getAttribute(MethodActorInfo.KEY);

      if (keyAttribute.isModified())
      {
        List<RelationshipDAOIF> relList = this.getParents(RelationshipTypes.METADATA_PARAMETER.getType());
        for (RelationshipDAOIF relationshipDAOIF : relList)
        {
          RelationshipDAO relationshipDAO = relationshipDAOIF.getRelationshipDAO();
          relationshipDAO.setKey(key);
          relationshipDAO.apply();
        }
      }
    }

    return oid;
  }

  private void validateReference()
  {
    String reference = this.getAttribute(MdParameterInfo.ENCLOSING_METADATA).getValue();
    BusinessDAOIF metadata = MetadataDAO.get(reference);

    if (! ( metadata instanceof MdMethodDAO ))
    {
      // TODO Fix exception type
      String msg = "The attribute [" + MdParameterInfo.ENCLOSING_METADATA + "] on [" + this.getType() + "] must be reference a [" + MdMethodInfo.CLASS + "] or []";

      throw new RuntimeException(msg);
    }

  }

  // This method is a hook for a pointcut in TransactionManagement
  /**
   * @param businessContext
   *          true if this is being called from a business context, false
   *          otherwise. If true then cascading deletes of other Entity objects
   *          will happen at the Business layer instead of the data access
   *          layer.
   * 
   */
  public void delete(boolean businessContext, DeleteContext context)
  {
    super.delete(context);
  }

  /**
   * Ensure that the parameter name is not a duplicate of another MdParameter
   * which exists on a MdMethod.
   */
  private void validateName()
  {
    if (!this.getAttributeIF(MdParameterInfo.NAME).isModified())
    {
      return;
    }

    // This MdMethod is defined on a MdEntity
    String validateName = getParameterName();

    MetadataDAO.validateName(validateName);

    String relationshipType = RelationshipTypes.METADATA_PARAMETER.getType();

    ParameterMarker marker = this.getEnclosingMetadata();
    List<RelationshipDAOIF> children = marker.getChildren(relationshipType);

    for (RelationshipDAOIF child : children)
    {
      BusinessDAOIF mdParameter = child.getChild();

      String name = mdParameter.getValue(MdParameterInfo.NAME);

      if (name.equals(validateName) && !mdParameter.getOid().equals(this.getOid()))
      {
        String msg = "A MdParameter of the name [" + validateName + "] already exists on the MdMethod [" + marker.getOid() + "]";
        throw new ParameterDefinitionException_NameExists(msg, this, marker);
      }
    }
  }

  /**
   * Ensure that the parameter name is not a duplicate of another MdParameter
   * which exists on a MdMethod.
   */
  private void validateOrder()
  {
    // This MdMethod is defined on a MdEntity
    String validateOrder = getParameterOrder();

    String relationshipType = RelationshipTypes.METADATA_PARAMETER.getType();

    MetadataDAOIF metadata = this.getEnclosingMetadata();
    List<RelationshipDAOIF> children = metadata.getChildren(relationshipType);

    for (RelationshipDAOIF child : children)
    {
      MdParameterDAOIF existingMdParameter = (MdParameterDAOIF) child.getChild();
      String oid = existingMdParameter.getOid();
      String order = existingMdParameter.getValue(MdParameterInfo.ORDER);

      if (order.equals(validateOrder) && !oid.equals(this.getOid()))
      {
        String msg = "A MdParameter of the order [" + validateOrder + "] already exists on the MdMethod [" + metadata.getOid() + "]";
        throw new ParameterDefinitionException_InvalidOrder(msg, this, metadata, existingMdParameter);
      }
    }
  }

  /**
   * Ensure the type of the MdParameter is valid
   */
  private void validateType()
  {
    String type = getParameterType().getType();
    ParameterMarker enclosingMetadata = this.getEnclosingMetadata();

    if (!enclosingMetadata.isValidType(type))
    {
      String methodName = enclosingMetadata.getName();
      String msg = "Type [" + type + "] of parameter [" + this.getParameterName() + "] on method [" + methodName + "] is invalid.";
      throw new ParameterDefinitionException_InvalidType(msg, this, enclosingMetadata);
    }
  }

  /**
   * Returns a new MdParameter. Some attributes will contain default values, as
   * defined in the attribute metadata. Otherwise, the attributes will be blank.
   * 
   * @return MdParameter.
   */
  public static MdParameterDAO newInstance()
  {
    return (MdParameterDAO) BusinessDAO.newInstance(MdParameterInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String,
   * java.lang.String)
   */
  public static MdParameterDAOIF get(String oid)
  {
    return (MdParameterDAOIF) BusinessDAO.get(oid);
  }

  public static String buildKey(String markerEnclosingType, String markerName, String parameterName)
  {
    return MdParameterDAO.buildKey(MdMethodDAO.buildKey(markerEnclosingType, markerName), parameterName);
  }

  public static String buildKey(String markerKey, String parameterName)
  {
    return markerKey + "." + parameterName;
  }
}
