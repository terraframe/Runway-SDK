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

import java.util.Map;

import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityGenerator;
import com.runwaysdk.dataaccess.MdAttributeBlobDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdTransientDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.transport.metadata.AttributeBlobMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeBlobMdSession;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeBlobDAO extends MdAttributeConcreteDAO implements MdAttributeBlobDAOIF
{

  /**
   *
   */
  private static final long serialVersionUID = 4774139222594558587L;

  /**
   *
   */
  private static int maxLength = 1048576;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeBlobDAO()
  {
    super();
  }

  /**
   * Constructs a BusinessDAO from the given {@link Map} of Attributes.
   *
   * <br/><b>Precondition:</b> attributeMap != null
   * <br/><b>Precondition:</b> type != null
   * <br/><b>Precondition:</b>ObjectCache.isSubTypeOf(classType, Constants.MD_CLASS)
   *
   * @param attributeMap
   * @param classType
   */
  public MdAttributeBlobDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public static int getMaxLength()
  {
    return maxLength;
  }

  /**
   * Initializes the strategy object.
   */
  protected void initializeStrategyObject()
  {
    if (this.definedByClass() instanceof MdEntityDAOIF)
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeBlob_E(this));
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

  /**
   * Typically called for java class generation, but Blob atributes require
   * special logic, which is contained in the generator. Included only to
   * satisfy the interface, this method should never be called, and will throw
   * an exception if it is.
   *
   * @return nothing
   */
  public String javaType(boolean isDTO)
  {
    return blobJavaType(isDTO);
  }

  /**
   *
   * @param isDTO
   * @return
   */
   protected static String blobJavaType(boolean isDTO)
   {
     return "byte[]";
   }

   @Override
   protected String generatedServerGetter(String attributeName)
   {
     return "getBlob(" + attributeName.toUpperCase() + ")";
   }

   @Override
   protected String generatedServerSetter(String attributeName)
   {
     return "setBlob(" + attributeName.toUpperCase() + ", value)";
   }

  /**
   * Returns a string representing the query attribute class for attributes of this type.
   *
   * @return string representing the query attribute class for attributes of this type.
   */
  public String queryAttributeClass()
  {
    return com.runwaysdk.query.SelectableBlob.class.getName();
  }

  /**
   * Used for data generation.  Returns a random 256-character String.
   */
  public void setRandomValue(EntityDAO object)
  {
    byte[] random = new byte[512];
    EntityGenerator.getRandom().nextBytes(random);
    object.setBlob(definesAttribute(), random);
  }

  /**
   * Returns a new MdAttributeBlob.  Some attributes will contain default values, as
   * defined in the attribute metadata. Otherwise, the attributes will be blank.
   *
   * @return MdAttributeBlob
   */
  public static MdAttributeBlobDAO newInstance()
  {
    return (MdAttributeBlobDAO) BusinessDAO.newInstance(MdAttributeBlobInfo.CLASS);
  }

  public MdAttributeBlobDAO getBusinessDAO()
  {
    return (MdAttributeBlobDAO) super.getBusinessDAO();
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String, java.lang.String)
   */
  public static MdAttributeBlobDAOIF get(String id)
  {
    return (MdAttributeBlobDAOIF) BusinessDAO.get(id);
  }

  public MdAttributeBlobDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeBlobDAO(attributeMap, MdAttributeBlobInfo.CLASS);
  }

  @Override
  public String attributeMdDTOType()
  {
    return AttributeBlobMdDTO.class.getName();
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitBlob(this);
  }
  
  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession() 
  {
    AttributeBlobMdSession attrSes = new AttributeBlobMdSession();
    super.populateAttributeMdSession(attrSes);
    return attrSes;
  }
  
  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeBlobDAOIF.class.getName();
  }
}
