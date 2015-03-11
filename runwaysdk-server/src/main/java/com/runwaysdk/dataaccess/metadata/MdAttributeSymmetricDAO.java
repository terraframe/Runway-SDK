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

import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.Map;
import java.util.Random;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.runwaysdk.constants.MdAttributeSymmetricInfo;
import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.constants.SymmetricMethods;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityGenerator;
import com.runwaysdk.dataaccess.MdAttributeSymmetricDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.attributes.EncryptionException;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeEnumeration;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeSymmetricDAO extends MdAttributeEncryptionDAO implements MdAttributeSymmetricDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = -7755466844766051093L;

  // Add the provider for Symmetric Encryption
//  static
//  {
//      Security.addProvider(new SunJCE());
//  }

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeSymmetricDAO()
  {
    super();
  }

  public MdAttributeSymmetricDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdAttributeSymmetricDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeSymmetricDAO(attributeMap, MdAttributeSymmetricInfo.CLASS);
  }

  /**
   * For data generation. Sets a random symmetric encryption value.
   */
  public void setRandomValue(EntityDAO object)
  {
    Random random = EntityGenerator.getRandom();
    String s = new String();
    String alpha = "abcdefghijklmnopqrstuvwxyz    ";
    for (int i=0; i<256; i++)
      s += alpha.charAt(random.nextInt(30));

    object.setValue(definesAttribute(), s);
  }

  @Override
  protected void initializeStrategyObject()
  {
    if (this.definedByClass() instanceof MdEntityDAOIF)
    {
      this.mdAttributeStrategy = new MdAttributeConcrete_E(this);
    }
    else
    {
      this.mdAttributeStrategy = new MdAttributeConcrete_S(this);
    }
  }

  /**
   * Returns the encryption method of this attribute.
   */
  public String getEncryptionMethod()
  {
    AttributeEnumeration encrypt = (AttributeEnumeration) this.getAttributeIF(MdAttributeSymmetricInfo.SYMMETRIC_METHOD);
    BusinessDAOIF deref = encrypt.dereference()[0];
    return deref.getAttributeIF(SymmetricMethods.TRANSFORMATION).getValue();
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
   * Returns the secret key size of this attribute.
   */
  private static int getSecretKeySize(String secretKeySize)
  {
    return Integer.parseInt(secretKeySize);
  }

  protected static void deleteKey(String id)
  {
    // delete keystore entry
    KeyStoreAccess access = KeyStoreAccess.getInstance();
    access.deleteKey(id);
  }

  /**
   * Saves this attribute metadata by first saving it in the normal MdAttribute fashion (by calling super.save()),
   * and then by creating a Key Store to hold the secret keys.
   */
  public String save(boolean validationRequired)
  {
    boolean isAppliedToDB = this.isAppliedToDB();
    
    String id = super.save(validationRequired);

    // if this MdAttributeSymmetric instance is new, generate a secret key
    if (isNew() && isAppliedToDB);
    {
      try
      {
        // generate the key
        Provider provider = Security.getProvider(ServerProperties.getSecurityProvider());
        KeyGenerator instance = KeyGenerator.getInstance(this.getEncryptionMethod().split("/")[0], provider);

        instance.init(getSecretKeySize(this.getAttributeIF(MdAttributeSymmetricInfo.SECRET_KEY_SIZE).getValue()));
        SecretKey key = instance.generateKey();

        // store the key
        KeyStoreAccess access = KeyStoreAccess.getInstance();
        String alias = this.getId();
        access.addKey(key, alias);
      }
      catch (NoSuchAlgorithmException e)
      {
        String error = "The Symmetric Attribute [" + this.definesAttribute()
            + "] on [" + this.definedByClass().definesType()
            + "] is using an incorrect or unsupported encryption algorithm.";
        throw new EncryptionException(error, this);
      }
    }
    return id;
  }


  /**
   * Deletes this attribute by first deleting its entry in the keystore, then by calling super.delete().
   *
   * @param businessContext true if this is being called from a business context, false
   * otherwise. If true then cascading deletes of other Entity objects will happen at the Business
   * layer instead of the data access layer.
   *
   */
  public void delete(boolean businessContext)
  {
    deleteKey(this.getId());
    super.delete(businessContext);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeSymmetricDAO getBusinessDAO()
  {
    return (MdAttributeSymmetricDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new MdAttributeSymmetric.
   * Some attributes will contain default values, as defined in the attribute
   * metadata. Otherwise, the attributes will be blank.
   *
   * @return MdAttributeSymmetric.
   */
  public static MdAttributeSymmetricDAO newInstance()
  {
    return (MdAttributeSymmetricDAO) BusinessDAO.newInstance(MdAttributeSymmetricInfo.CLASS);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdAttributeSymmetricDAOIF get(String id)
  {
    return (MdAttributeSymmetricDAOIF) BusinessDAO.get(id);
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitSymmetric(this);
  }
  
  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeSymmetricDAOIF.class.getName();
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
