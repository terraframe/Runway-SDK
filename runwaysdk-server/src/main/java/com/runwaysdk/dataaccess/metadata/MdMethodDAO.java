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

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.business.generation.EntityQueryAPIGenerator;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.rbac.MethodActorDAOIF;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.AttributeReferenceIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeReference;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.cache.ObjectCache;

/**
 * Defines a Method which will be auto generated on a MdType
 * 
 * @author Justin Smethie
 * @date 4/27/2007
 */
public class MdMethodDAO extends MetadataDAO implements MdMethodDAOIF
{
  /**
   * A comparator that sorts <code>MdMethodDAOIF</code> alphabetically
   */
  protected static Comparator<MdMethodDAOIF> alphabetical     = new Comparator<MdMethodDAOIF>()
                                                              {
                                                                public int compare(MdMethodDAOIF o1, MdMethodDAOIF o2)
                                                                {
                                                                  return o1.getName().compareTo(o2.getName());
                                                                }
                                                              };

  /**
   * Auto generated Eclipse UID
   */
  private static final long                  serialVersionUID = -4398783098229320291L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdMethodDAO()
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
    String signature = "Name:" + this.getName() + " IsStatic:" + this.isStatic() + " ReturnType: " + this.getReturnType().getType() + " Parameters[";

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
   * Constructs a MdMethod from the given hashtable of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> ObjectCache.isSubClass(type, Constants.MD_TYPE)
   * 
   * @param attributeMap
   * @param type
   */
  public MdMethodDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdMethodDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdMethodDAO(attributeMap, MdMethodInfo.CLASS);
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
    return ( (AttributeLocalIF) this.getAttributeIF(MdMethodInfo.DISPLAY_LABEL) ).getValue(locale);
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
    return ( (AttributeLocalIF) this.getAttributeIF(MdMethodInfo.DISPLAY_LABEL) ).getLocalValues();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdMethodDAO getBusinessDAO()
  {
    return (MdMethodDAO) super.getBusinessDAO();
  }

  /**
   * Get the name of the MdMethod.
   * 
   * @return
   */
  public String getName()
  {
    Attribute attribute = this.getAttribute(MdMethodInfo.NAME);
    return attribute.getValue();
  }

  /**
   * Get the return type of the MdMethod
   * 
   * @return
   */
  public Type getReturnType()
  {
    Attribute attribute = this.getAttribute(MdMethodInfo.RETURN_TYPE);
    String value = attribute.getValue();

    return new Type(value);
  }

  /**
   * Get the MdType on which the MdMethod is generated.
   * 
   * @return Id of an MdFacade
   */
  private String getMdTypeId()
  {
    Attribute attribute = this.getAttribute(MdMethodInfo.REF_MD_TYPE);
    return attribute.getValue();
  }

  /**
   * Get the MdType on which the MdMethod is generated.
   * 
   * @return MdType on which the MdMethod is generated.
   */
  public MdTypeDAOIF getMdType()
  {
    AttributeReference attributeReference = (AttributeReference) this.getAttribute(MdMethodInfo.REF_MD_TYPE);
    return (MdTypeDAOIF) ( attributeReference.dereference() );
  }

  /**
   * Get the MdTypeIF on which the MdMethod is generated (either an MdClass or
   * an MdFacade).
   * 
   * @return MdTypeIF on which the MdMethod is generated (either an MdClass or
   *         an MdFacade).
   */
  public MdTypeDAOIF getEnclosingMdTypeDAO()
  {
    AttributeReferenceIF attribute = (AttributeReferenceIF) this.getAttributeIF(MdMethodInfo.REF_MD_TYPE);

    return (MdTypeDAOIF) attribute.dereference();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.MdMethodDAOIF#getMdParameterDAOs()
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

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.MdMethodIF#isStatic()
   */
  public boolean isStatic()
  {
    Attribute attribute = this.getAttribute(MdMethodInfo.IS_STATIC);
    String value = attribute.getValue();
    String trueConstant = MdAttributeBooleanInfo.TRUE;

    return trueConstant.equalsIgnoreCase(value);
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
    validateReturnType();
    validateReference();
    validateName();

    // If this is the first time the MdMethod has ever been applied to the
    // database
    boolean firstApply = ( this.isNew() && !this.isAppliedToDB() && !this.isImport() );

    String key = MdMethodDAO.buildKey(this.getEnclosingMdTypeDAO().definesType(), this.getName());
    this.setKey(key);
    String id = super.apply();

    // Create the appropriate relationship between this MdMethod
    // and either a MdClass or a MdFacade, only create a relationship the first
    // time
    // this MdMethod is ever applied.
    if (firstApply)
    {
      String mdTypeId = this.getMdTypeId();

      String relationshipType = RelationshipTypes.MD_TYPE_MD_METHOD.getType();
      RelationshipDAO relationshipDAO = RelationshipDAO.newInstance(mdTypeId, id, relationshipType);
      relationshipDAO.setKey(key);
      relationshipDAO.apply();
    }

    return id;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.MdMethodIF#getAssignableMethod()
   */
  public MethodActorDAOIF getMethodActor()
  {
    List<RelationshipDAOIF> children = this.getChildren(RelationshipTypes.MD_METHOD_METHOD_ACTOR.getType());

    if (children.size() != 0)
    {
      return (MethodActorDAOIF) children.get(0).getChild();
    }

    return null;
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
  public void delete(boolean businessContext)
  {
    // 1. Delete the MdParameters defined by this MdMethod
    List<RelationshipDAOIF> relationships = this.getChildren(RelationshipTypes.METADATA_PARAMETER.getType());

    for (RelationshipDAOIF relationship : relationships)
    {
      MdParameterDAO mdParameter = ( (MdParameterDAOIF) relationship.getChild() ).getBusinessDAO();
      mdParameter.delete(businessContext);
    }

    // 2. Delete the AssignableMethod defined by this MdMehtod
    MethodActorDAOIF assignableMethod = this.getMethodActor();

    if (assignableMethod != null)
    {
      assignableMethod.getBusinessDAO().delete(businessContext);
    }

    // 3. Delete this BusinessDAO
    super.delete(businessContext);
  }

  /**
   * Ensure the returnType of the Method is valid
   */
  public static boolean validType(String type)
  {
    // If the type is an array remove the array brackets before validating the
    // type
    Type validate = new Type(type);

    if (validate.isPrimitive() || validate.isStream() || validate.isValueQuery())
    {
      return true;
    }

    MdTypeDAOIF mdType = null;

    try
    {
      mdType = MdTypeDAO.getMdTypeDAO(validate.getRootType());
    }
    catch (DataNotFoundException e1)
    {
      // Check if this is a
      int index = type.indexOf(EntityQueryAPIGenerator.QUERY_API_SUFFIX);

      if (index > 0 && ( index == type.length() - EntityQueryAPIGenerator.QUERY_API_SUFFIX.length() ))
      {
        String rootType = type.substring(0, index);

        try
        {
          mdType = MdTypeDAO.getMdTypeDAO(rootType);
        }
        // type is not defined in the core
        catch (DataNotFoundException e2)
        {
        }
      }
    }

    if (mdType == null)
    {
      return false;
    }

    // Ensure that the type is defined. Types are expected to have a package and
    // name.
    int index = type.lastIndexOf('.');
    if (index != -1 && index != ( type.length() - 1 ))
    {
      if (mdType instanceof MdClassDAO)
      {
        boolean isPublished = ( (MdClassDAO) mdType ).isPublished();
        if (isPublished)
        {
          return true;
        }
        else
        {
          return false;
        }
      }
      else
      {
        return true;
      }
    }

    return false;
  }

  public String getMethodSignature()
  {
    String parameters = GenerationUtil.buildBusinessParameters(this.getMdParameterDAOs(), true);
    String modifier = GenerationUtil.getModifier(this.isStatic(), false);

    return "public " + modifier + this.getReturnType().getType() + " " + this.getName() + "(" + parameters + ")";
  }

  /**
   * Ensure that this MdMethod only references either a MdClass or a MdFacade
   */
  private void validateReference()
  {
    MdTypeDAOIF mdType = this.getEnclosingMdTypeDAO();

    // Ensure that the MdMethod references a MdClass or an MdFacade
    if (! ( mdType instanceof MdClassDAO ) && ! ( mdType instanceof MdFacadeDAO ))
    {
      String msg = "A MdMethod can only reference a MdClass or a MdFacade.";

      throw new MethodDefinitionException_DefiningType(msg, this, mdType);
    }
  }

  /**
   * Validate the return type
   */
  private void validateReturnType()
  {
    Type returnType = getReturnType();
    String type = returnType.getType();

    if (!isValidType(type) && !returnType.isVoid())
    {
      String msg = "The return type [" + type + "] was not found";
      throw new MethodDefinitionException_InvalidReturnType(msg, this, type);
    }
  }

  /**
   * Ensure that the method name is not a duplicate of another MdMethod which
   * exists on the MdClass or MdFacade.
   */
  private void validateName()
  {
    if (!this.getAttributeIF(MdMethodInfo.NAME).isModified())
    {
      return;
    }

    // Ensure that the name is a legal java method name
    String validateName = this.getName();

    MetadataDAO.validateName(validateName);

    List<MdMethodDAOIF> mdMethodIFList = null;
    MdTypeDAOIF mdTypeIF = this.getMdType();

    // Get the MdMethods already defined for either the MdFacade
    // or MdClass on which this MdMethod is to be added.
    if (mdTypeIF instanceof MdFacadeDAO)
    {
      mdMethodIFList = mdTypeIF.getMdMethods();

      validateName(validateName, mdMethodIFList, mdTypeIF);
    }
    else
    {
      MdTypeDAOIF parentMdTypeIF = mdTypeIF;

      validateChildren(mdTypeIF, validateName);

      // Traverse the MdEntities entire inheritence tree front and back
      while (parentMdTypeIF != null)
      {
        mdMethodIFList = parentMdTypeIF.getMdMethods();

        validateName(validateName, mdMethodIFList, parentMdTypeIF);
        parentMdTypeIF = ( (MdClassDAOIF) parentMdTypeIF ).getSuperClass();
      }
    }
  }

  private void validateChildren(MdTypeDAOIF mdTypeIF, String validateName)
  {
    List<MdMethodDAOIF> mdMethodIFList = mdTypeIF.getMdMethods();

    validateName(validateName, mdMethodIFList, mdTypeIF);

    // Recursively check all children
    if (mdTypeIF instanceof MdClassDAOIF)
    {
      for (MdClassDAOIF subMdClassIF : ( (MdClassDAOIF) mdTypeIF ).getSubClasses())
      {
        validateChildren(subMdClassIF, validateName);
      }
    }

  }

  private void validateName(String validateName, List<MdMethodDAOIF> mdMethodIFList, MdTypeDAOIF parentMdType)
  {
    // Ensure that a the method name does not already exist in the children
    for (MdMethodDAOIF mdMethodIF : mdMethodIFList)
    {
      if (! ( mdMethodIF.getId().equals(this.getId()) ) && mdMethodIF.getName().equals(validateName))
      {
        String msg = "A MdMethod of the name [" + validateName + "] already exists on [" + parentMdType.getId() + "]";
        throw new MethodDefinitionException_NameExists(msg, this, parentMdType);
      }
    }
  }

  /**
   * Returns a new MdMethod. Some attributes will contain default values, as
   * defined in the attribute metadata. Otherwise, the attributes will be blank.
   * 
   * @return MdMethod.
   */
  public static MdMethodDAO newInstance()
  {
    return (MdMethodDAO) BusinessDAO.newInstance(MdMethodInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdMethodDAOIF get(String id)
  {
    return (MdMethodDAOIF) BusinessDAO.get(id);
  }

  /**
   * Returns an {@link MdMethodDAOIF} with the given key. The key may not
   * contain the exact type that defines the method, but all types in the
   * hierarchy of the type defined in the key are checked. If a match is not
   * found, then a {@link DataNotFoundException} is thrown.
   * 
   * @param key
   * @return
   * @throws DataNotFoundException
   *           when no method is found with the given key.
   */
  public static MdMethodDAOIF getMdMethod(String key)
  {
    return ObjectCache.getMdMethod(key);
  }

  /**
   * 
   * @param definingMdType
   * @param methodName
   * @return
   */
  public static String buildKey(String definingMdType, String methodName)
  {
    return definingMdType + "." + methodName;
  }

  public boolean isValidType(String type)
  {
    return MdMethodDAO.validType(type);
  }

}
