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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.KeyStore.PasswordProtection;
import java.security.cert.CertificateException;

import javax.crypto.SecretKey;

import com.runwaysdk.constants.ServerProperties;


/**
 * Singleton access to the application's keystore file. This is used to access
 * secret, public, and private keys.
 */
public class KeyStoreAccess
{
  /**
   * Singleton instance of this class.
   */
  private static KeyStoreAccess instance = null;

  /**
   * The keystore object to access the keystore file.
   */
  private KeyStore              keyStore;

  /**
   * The filename of the keystore.
   */
  private String                filename;

  /**
   * The password to access the keystore.
   */
  private PasswordProtection    password;

  /**
   * Constructor to open a keystore (or create one if it doesn't exist).
   */
  private KeyStoreAccess()
  {
    filename = ServerProperties.getKeyStoreFile();
    password = new PasswordProtection(ServerProperties.getKeyStorePassword().toCharArray());
    try
    {
      keyStore = KeyStore.getInstance(ServerProperties.getKeyStoreType());

      File fileTest = new File(filename);

      // if an existing keystore is there then use it. Otherwise, create a new,
      // empty keystore
      if (fileTest.exists() && fileTest.isFile() && fileTest.canRead() && fileTest.canWrite())
      {
        FileInputStream file = new FileInputStream(filename);
        keyStore.load(file, password.getPassword());
        file.close();
      }
      else
        keyStore.load(null, password.getPassword());
    }
    catch (KeyStoreException e)
    {
      throw new KeyAccessException("The keystore could not be correctly initialized.", e);
    }
    catch (FileNotFoundException e)
    {
      throw new KeyAccessException("The keystore file could not be found.", e);
    }
    catch (NoSuchAlgorithmException e)
    {
      throw new KeyAccessException("The algorithm used to check the integrity of the keystore cannot be found.", e);
    }
    catch (CertificateException e)
    {
      throw new KeyAccessException("The keystore could not be correctly initialized due to an improper certificate issue.", e);
    }
    catch (IOException e)
    {
      throw new KeyAccessException("The keystore could not be correctly initialized due to a problem with IO.", e);
    }
  }

  /**
   * Returns a singleton instance of this class.
   */
  public static synchronized KeyStoreAccess getInstance()
  {
    if (instance == null)
      instance = new KeyStoreAccess();
    return instance;
  }

  /**
   * Adds a new key to the keystore.
   * 
   * @param key
   * @param alias
   */
  public void addKey(SecretKey key, String alias)
  {
    try
    {
      KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(key);
      keyStore.setEntry(alias, entry, password);
      saveKeyStore();
    }
    catch (KeyStoreException e)
    {
      throw new KeyAccessException("A key could not be saved to the keystore.", e);
    }
  }

  /**
   * Since an alias and key are mapped together, if the alias isn't found, then
   * the key is also not there.
   * 
   * @return
   */
  public boolean containsKey(String alias)
  {
    boolean exists = false;
    try
    {
      exists = keyStore.containsAlias(alias);
    }
    catch (KeyStoreException e)
    {
      throw new KeyAccessException("A key could not be read from the keystore.", e);
    }

    return exists;
  }

  /**
   * Returns the secret key as aliased by the input.
   * 
   * @param alias
   * @return
   */
  public Key getKey(String alias)
  {
    Key key = null;
    try
    {
      key = keyStore.getKey(alias, password.getPassword());
    }
    catch (KeyStoreException e)
    {
      throw new KeyAccessException("The keystore could not be properly accessed.", e);
    }
    catch (NoSuchAlgorithmException e)
    {
      throw new KeyAccessException("The algorithm for recovering the key cannot be found.", e);
    }
    catch (UnrecoverableKeyException e)
    {
      throw new KeyAccessException("A key could not be found in the keystore.", e);
    }

    return key;
  }

  /**
   * Saves the keystore to the filesystem.
   */
  private void saveKeyStore()
  {
    FileOutputStream file;
    try
    {
      file = new FileOutputStream(filename);

      keyStore.store(file, password.getPassword());
      file.close();
    }
    catch (KeyStoreException e)
    {
      throw new KeyAccessException("The keystore could not be correctly initialized.", e);
    }
    catch (FileNotFoundException e)
    {
      throw new KeyAccessException("The keystore file could not be found.", e);
    }
    catch (NoSuchAlgorithmException e)
    {
      throw new KeyAccessException("The algorithm used to check the integrity of the keystore cannot be found.", e);
    }
    catch (CertificateException e)
    {
      throw new KeyAccessException("The keystore could not be correctly initialized due to an improper certificate issue.", e);
    }
    catch (IOException e)
    {
      throw new KeyAccessException("The keystore could not be correctly initialized due to a problem with IO.", e);
    }
  }

  /**
   * Deletes the keystore entry given by the specified alias
   * 
   * @param alias
   */
  public void deleteKey(String alias)
  {
    try
    {
      if (keyStore.containsAlias(alias))
      {
        keyStore.deleteEntry(alias);
        saveKeyStore();
      }
    }
    catch (KeyStoreException e)
    {
      throw new KeyAccessException("A keystore entry could not be deleted.", e);
    }
  }
}
