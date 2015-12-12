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
import java.util.Random;

import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityGenerator;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeLengthCharacterException;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeInteger;
import com.runwaysdk.transport.metadata.AttributeCharacterMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeCharacterMdSession;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeCharacterDAO extends MdAttributePrimitiveDAO implements MdAttributeCharacterDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = 2721131657891759115L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeCharacterDAO()
  {
    super();
  }

  /**
   * Constructs a BusinessDAO from the given hashtable of Attributes.
   *
   * <br/><b>Precondition:</b> attributeMap != null
   * <br/><b>Precondition:</b> type != null
   * <br/><b>Precondition:</b>ObjectCache.isSubTypeOf(classType, Constants.MD_CLASS)
   *
   * @param attributeMap
   * @param classType
   */
  public MdAttributeCharacterDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdAttributeCharacterDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeCharacterDAO(attributeMap, classType);
  }

  @Override
  protected void initializeStrategyObject()
  {
    if (this.definedByClass() instanceof MdEntityDAOIF)
    {
      this.mdAttributeStrategy = new MdAttributeCharacter_E(this);
    }
    else
    {
      this.mdAttributeStrategy = new MdAttributeConcrete_S(this);
    }
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

  @Override
  public String attributeMdDTOType()
  {
    return AttributeCharacterMdDTO.class.getName();
  }

  /**
   * Returns a string representing the query attribute class for attributes of this type.
   *
   * @return string representing the query attribute class for attributes of this type.
   */
  public String queryAttributeClass()
  {
    return com.runwaysdk.query.SelectableChar.class.getName();
  }

  /**
   *Returns the total maximum length of this character field.
   * @return total maximum length of this character field.
   */
  public String getSize()
  {
    return this.getAttributeIF(MdAttributeCharacterInfo.SIZE).getValue();
  }

  /**
   * Used for data generation.  Returns a random String that conforms to this
   * instances size constraint.
   */
  public void setRandomValue(EntityDAO object)
  {
    Random random = EntityGenerator.getRandom();
    String s = new String();
    String alpha = "abcdefghijklmnopqrstuvwxyz    ";
    int size = Integer.parseInt(this.getSize());
    for (int i=0; i<size && i<32; i++)
      s += alpha.charAt(random.nextInt(30));
    object.setValue(this.definesAttribute(), s);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeCharacterDAO getBusinessDAO()
  {
    return (MdAttributeCharacterDAO) super.getBusinessDAO();
  }

   /**
   * Returns a new MdAttributeCharacter.
   * Some attributes will contain default values, as defined in the attribute
   * metadata. Otherwise, the attributes will be blank.
   *
   * @return MdAttributeCharacter
   */
  public static MdAttributeCharacterDAO newInstance()
  {
    return (MdAttributeCharacterDAO) BusinessDAO.newInstance(MdAttributeCharacterInfo.CLASS);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String, java.lang.String)
   */
  public static MdAttributeCharacterDAOIF get(String id)
  {
    return (MdAttributeCharacterDAOIF) BusinessDAO.get(id);
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitCharacter(this);
  }

  /**
   * Validates this metadata object.
   *
   * @throws DataAccessException
   *           when this MetaData object is not valid.
   */
  protected void validate()
  {
    super.validate();

    AttributeInteger attributeSize = (AttributeInteger)this.getAttributeIF(MdAttributeCharacterInfo.SIZE);
    if (attributeSize.isModified() && !attributeSize.getValue().trim().equals(""))
    {
      int charLength = Integer.valueOf(attributeSize.getValue());
      int maxCharLength = MdAttributeCharacterInfo.MAX_CHARACTER_SIZE;

      if (charLength > charLength)
      {
        String error = "Attribute [" + attributeSize.getName() + "] on type [" + attributeSize.getDefiningClassType()
            + "] may not exceed " + maxCharLength + " characters in length.";
        throw new AttributeLengthCharacterException(error, attributeSize, maxCharLength);
      }
    }
  }
  
  /**
   * Precondition: assumes this character attribute is an ID. The collection of
   * <code>AttributeDAO</code> objects do not have their containing reference updated to
   * the returned <code>MdAttributeReferenceDAO</code> 
   */
  public MdAttributeReferenceDAOIF convertToReference()
  {
    MdAttributeReferenceDAO mdAttributeReferenceDAO = MdAttributeReferenceDAO.newInstance();
    
    mdAttributeReferenceDAO.replaceAttributeMap(this.getObjectState().getAttributeMap());
    
    mdAttributeReferenceDAO.getAttribute(MdAttributeReferenceInfo.REF_MD_ENTITY).setValue(this.getMdBusinessDAO().getId());
    
    return mdAttributeReferenceDAO;
  }
  
  /**
   * This is used by the query API to allow for parent ids and child ids of relationships to
   * be used in queries.
   * 
   * Precondition: assumes this character attribute is an ID. The collection of
   * <code>AttributeDAO</code> objects do not have their containing reference updated to
   * the returned <code>MdAttributeReferenceDAO</code> 
   * 
   * @param the code>MdBusinessDAOIF</code> of the referenced type in the relationship.
   */
  public MdAttributeReferenceDAOIF convertToReference(MdBusinessDAOIF mdReferenecedBusinessDAOIF)
  {
    MdAttributeReferenceDAO mdAttributeReferenceDAO = MdAttributeReferenceDAO.newInstance();
    
    mdAttributeReferenceDAO.replaceAttributeMap(this.getObjectState().getAttributeMap());
    
    mdAttributeReferenceDAO.getAttribute(MdAttributeReferenceInfo.REF_MD_ENTITY).setValue(mdReferenecedBusinessDAOIF.getId());
    
    return mdAttributeReferenceDAO;
  }
  
  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession() 
  {
    AttributeCharacterMdSession attrSes = new AttributeCharacterMdSession(Integer.valueOf(this.getSize()));
    super.populateAttributeMdSession(attrSes);
    return attrSes;
  }
  
  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeCharacterDAOIF.class.getName();
  }
}
