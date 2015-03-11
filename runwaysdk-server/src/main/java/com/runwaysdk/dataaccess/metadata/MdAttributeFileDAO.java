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

import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.MdAttributeFileInfo;
import com.runwaysdk.constants.VaultFileInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityGenerator;
import com.runwaysdk.dataaccess.MdAttributeFileDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.transport.metadata.AttributeFileMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeFileDAO extends MdAttributeConcreteDAO implements MdAttributeFileDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = 5893299722923297444L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeFileDAO()
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
    return super.getSignature()+" FileType:"+this.getReferenceMdBusinessDAO().definesType();
  }

  /**
   * Constructs a MdAttributeFile from the given hashtable of Attributes.
   *
   * <br/><b>Precondition:</b>   attributeMap != null
   * <br/><b>Precondition:</b>   type != null
   *
   * @param attributeMap
   * @param type
   */
  public MdAttributeFileDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }


  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdAttributeFileDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeFileDAO(attributeMap, classType);
  }

  @Override
  protected void initializeStrategyObject()
  {
    if (this.definedByClass() instanceof MdEntityDAOIF)
    {
      this.mdAttributeStrategy = new MdAttributeFile_E(this);
    }
    else
    {
      this.mdAttributeStrategy = new MdAttributeConcrete_S(this);
    }
  }

  /**
   * Returns the MdBusinsess that defines vault file.
   * @return MdBusinsess that defines vault file.
   */
  public static MdBusinessDAOIF getVaultFileMdBusiness()
  {
    return MdBusinessDAO.getMdBusinessDAO(VaultFileInfo.CLASS);
  }

  /**
   *Returns the metadata object that defines the MdBusiness type that this attribute referenes,
   * or null if it does not reference anything.
   *
   * @return the metadata object that defines the MdBusiness type that this attribute referenes,
   * or null if it does not reference anything.
   */
  public MdBusinessDAOIF getReferenceMdBusinessDAO()
  {
    return getVaultFileMdBusiness();
  }


  /**
   * Called for java class generation.  Returns the java type of this attribute
   * (String), which is used in the generated classes for type safety.
   *
   * @return The java type of this attribute (String)
   */
  public String javaType(boolean isDTO)
  {
    return "String";
  }

  /**
   * Returns a string representing the query attribute class for attributes of this type.
   *
   * @return string representing the query attribute class for attributes of this type.
   */
  public String queryAttributeClass()
  {
    return com.runwaysdk.query.SelectableReference.class.getName();
  }

  /**
   *
   */
  public void setRandomValue(EntityDAO object)
  {
    MdBusinessDAOIF valutFlieMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(VaultFileInfo.CLASS);
    List<String> referenceIDs = EntityDAO.getEntityIdsDB(valutFlieMdBusinessIF.definesType());
    int index = EntityGenerator.getRandom().nextInt(referenceIDs.size());
    object.setValue(this.definesAttribute(), referenceIDs.get(index));
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeFileDAO getBusinessDAO()
  {
    return (MdAttributeFileDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new MdAttributeFile.
   * Some attributes will contain default values, as defined in the attribute
   * metadata. Otherwise, the attributes will be blank.
   *
   * @return MdAttributeFile
   */
  public static MdAttributeFileDAO newInstance()
  {
    return (MdAttributeFileDAO) BusinessDAO.newInstance(MdAttributeFileInfo.CLASS);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String, java.lang.String)
   */
  public static MdAttributeFileDAOIF get(String id)
  {
    return (MdAttributeFileDAOIF) BusinessDAO.get(id);
  }

  @Override
  public String attributeMdDTOType()
  {
    return AttributeFileMdDTO.class.getName();
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitFile(this);
  }
  
  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeFileDAOIF.class.getName();
  }
  
  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession() 
  {
    throw new UnsupportedOperationException();
  }

}
