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

package com.runwaysdk.business.ontology;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.business.generation.BusinessQueryAPIGenerator;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.GeneratorIF;
import com.runwaysdk.business.generation.dto.BusinessDTOBaseGenerator;
import com.runwaysdk.business.generation.dto.BusinessDTOStubGenerator;
import com.runwaysdk.business.generation.dto.BusinessQueryDTOGenerator;
import com.runwaysdk.business.generation.ontology.TermBaseGenerator;
import com.runwaysdk.business.generation.ontology.TermStubGenerator;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdTermDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;

public class MdTermDAO extends MdBusinessDAO implements MdTermDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 4151317755345791369L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdTermDAO()
  {
    super();
  }

  /**
   * Constructs a <code>MdTermDAO</code> from the given hashtable of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null <br/>
   * 
   * 
   * @param attributeMap
   * @param type
   * @param useCache
   */
  public MdTermDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable,
   *      java.util.String, ComponentDTOIF, Map)
   */
  public MdTermDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdTermDAO(attributeMap, MdTermInfo.CLASS);
  }

  /**
   * Returns a new <code>MdTermDAO</code>. Some attributes will contain default
   * values, as defined in the attribute metadata. Otherwise, the attributes
   * will be blank.
   * 
   * @return instance of <code>MdTermDAO</code>.
   */
  public static MdTermDAO newInstance()
  {
    return (MdTermDAO) BusinessDAO.newInstance(MdTermInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdTermDAOIF get(String id)
  {
    return (MdTermDAOIF) BusinessDAO.get(id);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdTermDAO getBusinessDAO()
  {
    return (MdTermDAO) super.getBusinessDAO();
  }

  /**
   * Returns a MdTermIF instance of the metadata for the given class.
   * 
   * <br/>
   * <b>Precondition:</b> classType != null <br/>
   * <b>Precondition:</b> !classType.trim().equals("") <br/>
   * <b>Precondition:</b> classType is a valid class defined in the database <br/>
   * <b>Postcondition:</b> return value is not null <br/>
   * <b>Postcondition:</b> Returns a MdTermIF instance of the metadata for the
   * given class (MdTermIF().definesType().equals(classType)
   * 
   * @param classType
   *          class type
   * @return MdTermIF instance of the metadata for the given class type.
   */
  public static MdTermDAOIF getMdTermDAO(String classType)
  {
    return ObjectCache.getMdTermDAO(classType);
  }
  
  @Override
  public List<GeneratorIF> getGenerators()
  {
    List<GeneratorIF> list = new LinkedList<GeneratorIF>();

    //Dont generate reserved types
    if (GenerationUtil.isReservedType(this))
    {
      return list;
    }

    list.add(new TermBaseGenerator(this));
    list.add(new TermStubGenerator(this));
    list.add(new BusinessDTOBaseGenerator(this));
    list.add(new BusinessDTOStubGenerator(this));

    if (!GenerationUtil.isHardcodedType(this)) 
    {
      list.add(new BusinessQueryAPIGenerator(this));
      list.add(new BusinessQueryDTOGenerator(this));
    }

    return list;
  }
  
  /*
   * @see com.runwaysdk.dataaccess.metadata.MdBusinessDAO#save(boolean)
   */
  @Override
  public String save(boolean flag)
  {
    boolean firstApply = this.isNew() && !this.isAppliedToDB() && !this.isImport();
    
    String retval = super.save(flag);
    
      // Add display label to metadata.
      
    if (firstApply) {
      MdAttributeLocalCharacterDAO displayLabel = MdAttributeLocalCharacterDAO.newInstance();
      displayLabel.setValue(MdAttributeStructInfo.NAME, MdTermInfo.DISPLAY_LABEL);
      displayLabel.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, this.getId());
      displayLabel.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Display Label");
      displayLabel.apply();
    }
    
    return retval;
  }

}
