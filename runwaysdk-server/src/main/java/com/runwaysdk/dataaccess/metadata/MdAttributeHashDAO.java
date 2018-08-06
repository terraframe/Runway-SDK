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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.constants.HashMethods;
import com.runwaysdk.constants.MdAttributeHashInfo;
import com.runwaysdk.dataaccess.AttributeEncryptionIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityGenerator;
import com.runwaysdk.dataaccess.MdAttributeHashDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdTransientDAOIF;
import com.runwaysdk.dataaccess.attributes.EncryptionException;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeEnumeration;
import com.runwaysdk.transport.metadata.caching.AttributeHashMdSession;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

import sun.security.provider.Sun;

public class MdAttributeHashDAO extends MdAttributeEncryptionDAO implements MdAttributeHashDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 2395264531315915578L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeHashDAO()
  {
    super();
  }
 
  public MdAttributeHashDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdAttributeHashDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeHashDAO(attributeMap, MdAttributeHashInfo.CLASS);
  }
  
  
  @Override
  protected void initializeStrategyObject()
  {
    if (this.definedByClass() instanceof MdEntityDAOIF)
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeHash_E(this));
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
   * Hashed attribtues are special because the hash is never actually retrived, only
   * compared to. As such, the getter for a hashed attribute is substituted with an
   * equality check.
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#generatedServerGetter()
   */
  @Override
  protected String generatedServerGetter(String attributeName)
  {
    return "(("+AttributeEncryptionIF.class.getName()+")"
    + BusinessFacade.class.getName()+".getAttribute(this, \""
    + attributeName + "\")).encryptionEquals(value, false)";
  }
  
  public String getEncryptionMethod()
  {
    AttributeEnumeration encrypt = (AttributeEnumeration) this.getAttributeIF(MdAttributeHashInfo.HASH_METHOD);
    BusinessDAOIF deref = encrypt.dereference()[0];
    return deref.getAttributeIF(HashMethods.MESSAGE_DIGEST).getValue();
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
   * For data generation. Sets a random hash value.
   */
  public void setRandomValue(EntityDAO object)
  {
    Random random = EntityGenerator.getRandom();
    String s = new String();
    String alpha = "abcdefghijklmnopqrstuvwxyz    ";
    for (int i=0; i<256; i++)
      s += alpha.charAt(random.nextInt(30));
    
    // hash the value according to the hashing method
    String type = getEncryptionMethod();
    String hash = null;
    try
    {
      MessageDigest digest = MessageDigest.getInstance(type, new Sun());
      digest.update(s.getBytes());
      hash = new String(digest.digest());
    }
    catch (NoSuchAlgorithmException e)
    {
      String error = "Attribute [" + this.definesAttribute() + "] on ["
          + this.definedByClass().definesType() + "] has an incorrect hashing algorithm.";
      throw new EncryptionException(error, this);
    }
    object.setValue(this.definesAttribute(), hash);
  }
  
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeHashDAO getBusinessDAO()
  {
    return (MdAttributeHashDAO) super.getBusinessDAO();
  }
  
  /**
   * Returns a new MdAttributeHash. 
   * Some attributes will contain default values, as defined in the attribute
   * metadata. Otherwise, the attributes will be blank.
   * 
   * @return MdAttributeHash
   */
  public static MdAttributeHashDAO newInstance()
  {
    return (MdAttributeHashDAO) BusinessDAO.newInstance(MdAttributeHashInfo.CLASS);
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdAttributeHashDAOIF get(String oid)
  {
    return (MdAttributeHashDAOIF) BusinessDAO.get(oid);
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitHash(this);
  }
  
  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession() 
  {
    AttributeHashMdSession attrSes = new AttributeHashMdSession();
    super.populateAttributeMdSession(attrSes);
    return attrSes;
  }

  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeHashDAOIF.class.getName();
  }
  
}
