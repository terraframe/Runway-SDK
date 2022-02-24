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

import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.GeneratorIF;
import com.runwaysdk.business.generation.LocalStructBaseGenerator;
import com.runwaysdk.business.generation.StructQueryAPIGenerator;
import com.runwaysdk.business.generation.StructStubGenerator;
import com.runwaysdk.business.generation.dto.LocalStructDTOBaseGenerator;
import com.runwaysdk.business.generation.dto.LocalStructDTOStubGenerator;
import com.runwaysdk.business.generation.dto.StructQueryDTOGenerator;
import com.runwaysdk.constants.MdLocalStructInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalCharacterDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.ObjectCache;

public class MdLocalStructDAO extends MdStructDAO implements MdLocalStructDAOIF
{
  private static final long serialVersionUID = -4262759261586248103L;

  public MdLocalStructDAO()
  {
    super();
  }

  /**
   * 
   * @param attributeMap
   * @param classType
   */
  public MdLocalStructDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public static MdLocalStructDAO newInstance()
  {
    return (MdLocalStructDAO) BusinessDAO.newInstance(MdLocalStructInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdLocalStructDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdLocalStructDAO(attributeMap, MdLocalStructInfo.CLASS);
  }

  public MdLocalStructDAO getBusinessDAO()
  {
    return (MdLocalStructDAO) super.getBusinessDAO();
  }

  @SuppressWarnings("unchecked")
  public List<? extends MdAttributeLocalCharacterDAOIF> getMdAttributeStruct()
  {
    return (List<? extends MdAttributeLocalCharacterDAOIF>) super.getMdAttributeStruct();
  }

  /**
   * Delete the local struct if no localized attributes are referencing it.
   * 
   * @param businessContext
   *          true if this is being deleted in a business context, false
   *          otherwise.
   * 
   */
  public void deleteIfNotReferenced()
  {
    if (this.getMdAttributeStruct().size() == 0)
    {
      this.delete(new DeleteContext());
    }
  }

  public MdLocalStructDAOIF getRootMdClassDAO()
  {
    return (MdLocalStructDAOIF) super.getRootMdClassDAO();
  }

  /**
   *Returns a MdLocalStructDAOIF instance of the metadata for the given class.
   * 
   * <br/>
   * <b>Precondition:</b> localStructType != null <br/>
   * <b>Precondition:</b> !localStructType.trim().equals("") <br/>
   * <b>Precondition:</b> localStructType is a valid class defined in the
   * database <br/>
   * <b>Postcondition:</b> return value is not null <br/>
   * <b>Postcondition:</b> Returns a MdLocalStructDAOIF instance of the metadata
   * for the given class
   * (MdLocalStructDAOIF().definesType().equals(localStructType)
   * 
   * @param localStructType
   *          class type
   * @return MdLocalStructDAOIF instance of the metadata for the given class
   *         type.
   */
  public static MdLocalStructDAOIF getMdLocalStructDAO(String localStructType)
  {
    return ObjectCache.getMdLocalStructDAO(localStructType);
  }

  @Override
  public List<GeneratorIF> getGenerators()
  {
    List<GeneratorIF> list = new LinkedList<GeneratorIF>();

    // Don't generate reserved types
    if (GenerationUtil.isSkipCompileAndCodeGeneration(this))
    {
      return list;
    }

    list.add(new LocalStructBaseGenerator(this));
    list.add(new StructStubGenerator(this));
    list.add(new LocalStructDTOBaseGenerator(this));
    list.add(new LocalStructDTOStubGenerator(this));

    if (!GenerationUtil.isHardcodedType(this))
    {
      list.add(new StructQueryAPIGenerator(this));
      list.add(new StructQueryDTOGenerator(this));
    }

    return list;
  }

  public MdAttributeDAOIF getLocale(MdDimensionDAOIF mdDimensionDAO, Locale locale)
  {
    String attributeName = mdDimensionDAO.getLocaleAttributeName(locale);

    return this.definesAttribute(attributeName);
  }

  public MdAttributeDAOIF getLocale(Locale locale)
  {
    String attributeName = locale.toString();

    return this.definesAttribute(attributeName);
  }

  public MdAttributeDAOIF getDefaultLocale(MdDimensionDAOIF mdDimensionDAO)
  {
    String attributeName = mdDimensionDAO.getDefaultLocaleAttributeName();

    return this.definesAttribute(attributeName);
  }

}
