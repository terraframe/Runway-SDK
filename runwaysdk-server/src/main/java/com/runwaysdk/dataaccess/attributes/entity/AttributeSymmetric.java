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
package com.runwaysdk.dataaccess.attributes.entity;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.AttributeSymmetricIF;
import com.runwaysdk.dataaccess.MdAttributeEncryptionDAOIF;
import com.runwaysdk.dataaccess.MdAttributeSymmetricDAOIF;
import com.runwaysdk.dataaccess.attributes.EncryptionException;
import com.runwaysdk.dataaccess.metadata.KeyStoreAccess;
import com.runwaysdk.util.Base64;


public class AttributeSymmetric extends AttributeEncryption implements AttributeSymmetricIF
{
  private static final long serialVersionUID = 8595402161639542580L;
  

  /**
   * @param name
   * @param mdAttributeKey key of the defining attribute metadata
   * @param definingEntityType
   */
  protected AttributeSymmetric(String name, String mdAttributeKey, String definingEntityType)
  {
    super(name, mdAttributeKey, definingEntityType);
  }

  /**
   * @param attributeName
   * @param mdAttributeKey key of the defining attribute metadata
   * @param classType
   * @param attributeValue
   */
  public AttributeSymmetric(String attributeName, String mdAttributeKey, String classType, String attributeValue)
  {
    super(attributeName, mdAttributeKey, classType, attributeValue);
  }

  /**
   * Returns the MdAttributeIF that defines the this attribute.
   * 
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   * 
   * @return MdAttributeIF that defines the this attribute
   */
  public MdAttributeSymmetricDAOIF getMdAttribute()
  {
    return (MdAttributeSymmetricDAOIF)super.getMdAttribute();
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute.  If this is defined 
   * by aa concrete attribute, this object is returned.  If it is a virtual attribute, then the 
   * concrete attribute it references is returned.
   * 
   * @return MdAttributeSymmetricDAOIF that defines the this attribute
   */
  public MdAttributeSymmetricDAOIF getMdAttributeConcrete()
  {
    return this.getMdAttribute();
  }

  /**
   * Deeps clones this attribute.
   * 
   * @return a new instance of AttributeSymmetric
   */
  public Attribute attributeClone()
  {
    return new AttributeSymmetric(this.getName(), this.mdAttributeKey, this.getDefiningClassType(), new String(this.getRawValue()));
  }

  public String getValue()
  {
    String value = super.getValue();
    if (value.length() == 0)
      return value;
    else
      return decrypt(value);
  }

  /**
   * Sets the value of this attribute after encrypting it.
   */
  public void setValue(String value)
  {
    if (value.length() == 0)
    {
      super.setValue(value);
    }
    else
    {
      String encrypted = encrypt(value);
      super.setValue(encrypted);
    }
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.attributes.Attribute#importValue(java.lang.String)
   * @param value The base 64 representation of the encryption
   */
  public void importValue(String value)
  {
    // Do not hash, as the value is already hashed
    if (!this.value.equals(value))
    {
      this.value = value;
      this.setModified(true);
    }
  }

  /**
   * Encrypts the input value and returns the result.
   * 
   * @param value
   * @return
   */
  public String encrypt(String value)
  {
    return cipher(this, value, Cipher.ENCRYPT_MODE);
  }

  /**
   * Decrypts the input value and returns the result.
   * 
   * @param value
   * @return
   */
  public String decrypt(String value)
  {
    return cipher(this, value, Cipher.DECRYPT_MODE);
  }

  /**
   * Encrypts/Decryptes the input value according to the specified algorithm.
   * The method will either encrypt or decrypt based on the opmode value.
   * 
   * @param value
   * @param opmode
   * @return an encrypted or decrypted string.
   */
  public static String cipher(AttributeSymmetricIF attributeSymmetricIF, String value, int opmode)
  {
    String alias = attributeSymmetricIF.getMdAttribute().getId();

    KeyStoreAccess access = KeyStoreAccess.getInstance();
    Cipher cipher = null;
    String transformation = ((MdAttributeEncryptionDAOIF) attributeSymmetricIF.getMdAttribute()).getEncryptionMethod();
    try
    {
      Provider provider = Security.getProvider(ServerProperties.getSecurityProvider());
      cipher = Cipher.getInstance(transformation, provider);
      Key key = access.getKey(alias);
      
      // create the IV
      int size = cipher.getBlockSize();
      byte[] iv = new byte[size];
      for(int i=0; i<size; i++)
      {
        iv[i] = (byte) ( 0x00+i );
      }
      
      cipher.init(opmode, key, new IvParameterSpec(iv));
        
      // encrypt the value
      if (opmode == Cipher.ENCRYPT_MODE)
      {
        byte[] encryptedBytes = cipher.doFinal(value.getBytes());
        return Base64.encodeToString(encryptedBytes, false);
      }
      // decrypt the value
      else
      {
        byte[] encryptedBytes = Base64.decode(value.toCharArray());
        byte[] decrypted = cipher.doFinal(encryptedBytes);
        return new String(decrypted, "ASCII");
      }
    }
    catch (NoSuchAlgorithmException e)
    {
      String error = "Attribute [" + attributeSymmetricIF.getName() + "] on an instance of [" + attributeSymmetricIF.getDefiningClassType() + "] has an invalid hashing method.";
      throw new EncryptionException(error, e, attributeSymmetricIF.getMdAttributeConcrete());
    }
    catch (InvalidKeyException e)
    {
      String error = "The Symmetric Attribute [" + attributeSymmetricIF.getName() + "] is using an incorrect key alias";
      throw new EncryptionException(error, e, attributeSymmetricIF.getMdAttributeConcrete());
    }
    catch (NoSuchPaddingException e)
    {
      String error = "Attribute [" + attributeSymmetricIF.getName() + "] on an instance of ["
          + attributeSymmetricIF.getDefiningClassType() + "] uses the [" + transformation
          + "] transformation, which contains a padding scheme that is not available.";
      throw new EncryptionException(error, e, attributeSymmetricIF.getMdAttributeConcrete());
    }
    catch (IllegalBlockSizeException e)
    {
      String error = "Attribute [" + attributeSymmetricIF.getName() + "] on an instance of ["
          + attributeSymmetricIF.getDefiningClassType() + "] has an invalid block size.";
      throw new EncryptionException(error, e, attributeSymmetricIF.getMdAttributeConcrete());
    }
    catch (BadPaddingException e)
    {
      String error = "Attribute [" + attributeSymmetricIF.getName() + "] on an instance of ["
          + attributeSymmetricIF.getDefiningClassType() + "] is not bounded by the appropriate padding bytes";
      throw new EncryptionException(error, e, attributeSymmetricIF.getMdAttributeConcrete());
    }
    catch (InvalidAlgorithmParameterException e)
    {
      String error = "Attribute [" + attributeSymmetricIF.getName() + "] on an instance of ["
          + attributeSymmetricIF.getDefiningClassType() + "] is using an invalid algorithm parameter";
      throw new EncryptionException(error, e, attributeSymmetricIF.getMdAttributeConcrete());
    }
    catch (IOException e)
    {
      String error = "Attribute [" + attributeSymmetricIF.getName() + "] on an instance of ["
          + attributeSymmetricIF.getDefiningClassType() + "] had an IO problem during ciphering";
      throw new EncryptionException(error, e, attributeSymmetricIF.getMdAttributeConcrete());
    }
  }

  /**
   * Returns the decrypted value of this attribute using the secret key.
   */
  public String getDecryptedData()
  {
    //Must call this.getValue instead of super.getValue because
    //super.getValue returns the encrypted value instead of the
    //decrypted one.
    return this.getValue();
  }

  /**
   * @see com.runwaysdk.dataaccess.attributes.entity.AttributeEncryption#encryptionEquals(String,
   *      boolean)
   */
  public boolean encryptionEquals(String compareValue, boolean alreadyEncrypted)
  {
    if (alreadyEncrypted)
      compareValue = decrypt(compareValue);

    if (compareValue.equals(getValue()))
      return true;
    else
      return false;
  }
}
