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
package com.runwaysdk.dataaccess.metadata;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.constants.FormObjectInfo;
import com.runwaysdk.constants.MdActionInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdParameterInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.AttributeReferenceIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdActionDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeLocal;
import com.runwaysdk.system.metadata.MdAction;

public class MdActionDAO extends MetadataDAO implements MdActionDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = -4206299533529878429L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdActionDAO()
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
    String signature = "Name:" + this.getName() + " IsPost:" + this.isPost() + " Parameters[";

    boolean firstIteration = true;
    for (MdParameterDAOIF mdParameterDAOIF : this.getMdParameterDAOs())
    {
      if (!firstIteration)
      {
        signature += ", ";
      }
      else
      {
        firstIteration = false;
      }
      signature += mdParameterDAOIF.getSignature();
    }

    signature += "]";

    return signature;
  }

  /**
   * True if this action is a post, false if otherwise.
   * 
   * @return true if this action is a post, false if otherwise.
   */
  public boolean isPost()
  {
    return ( (AttributeBooleanIF) this.getAttributeIF(MdActionInfo.IS_POST) ).getBooleanValue();
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
  public MdActionDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Map, String)
   */
  public MdActionDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdActionDAO(attributeMap, MdActionInfo.CLASS);
  }

  /**
   * Returns the display label of this metadata object
   * 
   * @param
   * 
   * @return the display label of this metadata object
   */
  public String getDisplayLabel(Locale locale)
  {
    return ( (AttributeLocal) this.getAttributeIF(MdActionInfo.DISPLAY_LABEL) ).getValue(locale);
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
    return ( (AttributeLocalIF) this.getAttributeIF(MdActionInfo.DISPLAY_LABEL) ).getLocalValues();
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdActionDAO getBusinessDAO()
  {
    return (MdActionDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new {@link MdAction}. Some attributes will contain default
   * values, as defined in the attribute metadata. Otherwise, the attributes
   * will be blank.
   * 
   * @return MdFacade
   */
  public static MdActionDAO newInstance()
  {
    return (MdActionDAO) BusinessDAO.newInstance(MdActionInfo.CLASS);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdActionDAOIF get(String id)
  {
    return (MdActionDAOIF) BusinessDAO.get(id);
  }

  /**
   * 
   * @param definingMdType
   * @param actionName
   * @return
   */
  public static String buildKey(String definingMdType, String actionName)
  {
    return definingMdType + "." + actionName;
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
    // Validate values
    validateName();
    validatePost();

    boolean isAppliedToDB = this.isAppliedToDB();
    
    // Set key and apply
    String key = buildKey(this.getEnclosingMdTypeDAO().definesType(), this.getName());
    this.setKey(key);
    String id = super.apply();

    // Create the appropriate relationship between this MdMethod
    // and either a MdClass or a MdFacade, only create a relationship the first
    // time
    // this MdMethod is ever applied.
    if (this.isNew() && !isAppliedToDB)
    {
      if (!this.isImport())
      {
        String mdControllerId = this.getAttribute(MdActionInfo.ENCLOSING_MD_CONTROLLER).getValue();

        String relationshipType = RelationshipTypes.CONTROLLER_ACTION.getType();
        RelationshipDAO relationshipDAO = RelationshipDAO.newInstance(mdControllerId, id, relationshipType);
        relationshipDAO.setKey(key);
        relationshipDAO.apply();

        // Check if this is a query action
        String isQuery = this.getAttribute(MdActionInfo.IS_QUERY).getValue();

        if (isQuery.equals(MdAttributeBooleanInfo.TRUE))
        {
          // Create query parameter: Sort Attribute
          MdParameterDAO sortAttribute = MdParameterDAO.newInstance();
          sortAttribute.getAttribute(MdParameterInfo.ENCLOSING_METADATA).setValue(id);
          sortAttribute.getAttribute(MdParameterInfo.NAME).setValue(MdActionInfo.SORT_ATTRIBUTE);
          sortAttribute.getAttribute(MdParameterInfo.ORDER).setValue("-4");
          sortAttribute.getAttribute(MdParameterInfo.TYPE).setValue(String.class.getName());
          ( (AttributeLocal) sortAttribute.getAttribute(MdParameterInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "Sort Attribute");
          sortAttribute.apply();

          // Create query parameter: Ascending Order
          MdParameterDAO isAscending = MdParameterDAO.newInstance();
          isAscending.getAttribute(MdParameterInfo.ENCLOSING_METADATA).setValue(id);
          isAscending.getAttribute(MdParameterInfo.NAME).setValue(MdActionInfo.IS_ASCENDING);
          isAscending.getAttribute(MdParameterInfo.ORDER).setValue("-3");
          isAscending.getAttribute(MdParameterInfo.TYPE).setValue(Boolean.class.getName());
          ( (AttributeLocal) isAscending.getAttribute(MdParameterInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "Is Ascending");
          isAscending.apply();

          // Create query parameter: Page Size
          MdParameterDAO pageSize = MdParameterDAO.newInstance();
          pageSize.getAttribute(MdParameterInfo.ENCLOSING_METADATA).setValue(id);
          pageSize.getAttribute(MdParameterInfo.NAME).setValue(MdActionInfo.PAGE_SIZE);
          pageSize.getAttribute(MdParameterInfo.ORDER).setValue("-2");
          pageSize.getAttribute(MdParameterInfo.TYPE).setValue(Integer.class.getName());
          ( (AttributeLocal) pageSize.getAttribute(MdParameterInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "Page Size");
          pageSize.apply();

          // Create query parameter: Page Number
          MdParameterDAO pageNumber = MdParameterDAO.newInstance();
          pageNumber.getAttribute(MdParameterInfo.ENCLOSING_METADATA).setValue(id);
          pageNumber.getAttribute(MdParameterInfo.NAME).setValue(MdActionInfo.PAGE_NUMBER);
          pageNumber.getAttribute(MdParameterInfo.ORDER).setValue("-1");
          pageNumber.getAttribute(MdParameterInfo.TYPE).setValue(Integer.class.getName());
          ( (AttributeLocal) pageNumber.getAttribute(MdParameterInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "Page Number");
          pageNumber.apply();
        }
      }
    }
    else
    {  
      List<RelationshipDAOIF> relList = this.getChildren(RelationshipTypes.METADATA_PARAMETER.getType());
      for (RelationshipDAOIF relationshipDAOIF : relList)
      {
        MdParameterDAO mdParameterDAO = (MdParameterDAO)relationshipDAOIF.getChild().getBusinessDAO();
        mdParameterDAO.apply();
      }
    }
    
    return id;
  }

  /**
   * Ensure that the method name is not a duplicate of another MdMethod which
   * exists on the MdClass or MdFacade.
   */
  private void validateName()
  {
    if (!this.getAttributeIF(MdActionInfo.NAME).isModified())
    {
      return;
    }

    // Ensure that the name is a legal java method name
    String validateName = this.getAttribute(MdActionInfo.NAME).getValue();

    validateName(validateName);
  }

  /**
   * Ensure that if a MdAction is a query it is not post only
   */
  private void validatePost()
  {
    if (this.getAttribute(MdActionInfo.IS_QUERY).equals(MdAttributeBooleanInfo.TRUE) && this.getAttribute(MdActionInfo.IS_POST).equals(MdAttributeBooleanInfo.TRUE))
    {
      // TODO fix exception type
      String msg = "A query action cannot be a post action";
      throw new RuntimeException(msg);
    }
  }

  /**
   * Get the MdControllerIF on which the MdAction is generated.
   * 
   * @return MdControllerIF on which the MdAction is generated.
   */
  public MdTypeDAOIF getEnclosingMdTypeDAO()
  {
    AttributeReferenceIF attribute = (AttributeReferenceIF) this.getAttributeIF(MdActionInfo.ENCLOSING_MD_CONTROLLER);

    return (MdTypeDAOIF) attribute.dereference();
  }

  /**
   * Get the name of the MdAction.
   * 
   * @return
   */
  public String getName()
  {
    return this.getAttribute(MdActionInfo.NAME).getValue();
  }

  @Override
  public void delete(boolean businessContext)
  {
    // 1. Delete the MdParameters defined by this MdAction
    List<RelationshipDAOIF> relationships = this.getChildren(RelationshipTypes.METADATA_PARAMETER.getType());

    for (RelationshipDAOIF relationship : relationships)
    {
      MdParameterDAO mdParameter = ( (MdParameterDAOIF) relationship.getChild() ).getBusinessDAO();
      mdParameter.delete(businessContext);
    }

    super.delete(businessContext);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.MdMethodIF#getMdParameters()
   */
  public List<MdParameterDAOIF> getMdParameterDAOs()
  {
    List<MdParameterDAOIF> parameters = new LinkedList<MdParameterDAOIF>();
    List<RelationshipDAOIF> relationships = this.getChildren(RelationshipTypes.METADATA_PARAMETER.getType());

    for (RelationshipDAOIF relationship : relationships)
    {
      MdParameterDAOIF mdParameter = (MdParameterDAOIF) relationship.getChild();
      parameters.add(mdParameter);
    }

    // Sort the MdParamters into ascending order by the parameter order
    Collections.sort(parameters, new Comparator<MdParameterDAOIF>()
    {
      public int compare(MdParameterDAOIF p1, MdParameterDAOIF p2)
      {
        Integer o1 = Integer.parseInt(p1.getParameterOrder());
        Integer o2 = Integer.parseInt(p2.getParameterOrder());

        return o1.compareTo(o2);
      }
    });

    return parameters;
  }

  public boolean isValidType(String type)
  {
    boolean isPost = Boolean.parseBoolean(this.getAttribute(MdActionInfo.IS_POST).getValue());
    Type validate = new Type(type);

    // GET action can only have primitive parameters
    if (!isPost && ( !validate.isPrimitive() || validate.isArray() ))
    {
      return false;
    }

    if (validate.isArray() && validate.getDimensions() > 1)
    {
      return false;
    }

    if (validate.getType().equals(FormObjectInfo.CLASS))
    {
      return true;
    }

    if (validate.getType().equals(MdActionInfo.MULTIPART_FILE_PARAMETER))
    {
      return true;
    }

    return MdMethodDAO.validType(type);
  }

}
