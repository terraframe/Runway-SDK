package com.runwaysdk.localization;

import java.io.InputStream;
import java.sql.Savepoint;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.ConfigurationException;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.metadata.MdClass;

public class LocalizedValueStore extends LocalizedValueStoreBase
{
  private static final long serialVersionUID = -1881642271;
  
  private static final Logger logger = LoggerFactory.getLogger(LocalizedValueStore.class);
  
  public static final String TAG_NAME_RUNWAY_EXCEPTION = "RunwayException";
  
  public static final String TAG_NAME_UI_TEXT = "UIText";
  
  public LocalizedValueStore()
  {
    super();
  }
  
  @Override
  protected String buildKey()
  {
    return this.getStoreKey();
  }
  
  /**
   * Fetches the localization for the given key and locale from the localization store.
   */
  public static String localize(String key)
  {
    return LocalizedValueStore.localize(key, Session.getCurrentLocale());
  }
  
  /**
   * Fetches the localization for the given key and locale from the localization store.
   */
  public static String localize(String key, Locale locale)
  {
    LocalizedValueStoreQuery query = new LocalizedValueStoreQuery(new QueryFactory());
    query.WHERE(query.getStoreKey().EQ(key));
    
    OIterator<? extends LocalizedValueStore> it = query.getIterator();
    
    try
    {
      if (it.hasNext())
      {
        LocalizedValueStore store = it.next();
        
        return store.getStoreValue().getValue(locale);
      }
      else
      {
        return null;
      }
    }
    finally
    {
      it.close();
    }
  }
  
  public static Map<String, String> getAll()
  {
    return LocalizedValueStore.getAll(Session.getCurrentLocale());
  }
  
  public static Map<String, String> getAll(Locale locale)
  {
    Map<String, String> map = new HashMap<String, String>();
    
    LocalizedValueStoreQuery query = new LocalizedValueStoreQuery(new QueryFactory());
    
    OIterator<? extends LocalizedValueStore> it = query.getIterator();
    
    try
    {
      while (it.hasNext())
      {
        LocalizedValueStore store = it.next();
        
        String value = store.getStoreValue().getValue(locale);
        String key = store.getStoreKey();
        
        map.put(key, value);
      }
    }
    finally
    {
      it.close();
    }
    
    return map;
  }
  
  /**
   * Imports a java properties file into the store. If the keys already exist in the store they will be overridden.
   */
  @Transaction
  public static void importPropertiesIntoStore(InputStream stream, String tagName)
  {
    Properties props = new Properties();
    
    try 
    {
      props.load(stream);
    }
    catch (Exception e)
    {
      throw new ConfigurationException(e);
    }
    
    Set<Object> keys = props.keySet();
    
    int newProps = 0;
    int replacedProps = 0;
    for (Object key : keys)
    {
      String sKey = (String) key;
      String sValue = props.getProperty(sKey);
      
      if (sValue == null || sValue.length() == 0)
      {
        logger.error("Skipping key [" + sKey + "] because the value is either null or empty.");
        continue;
      }
      
      Savepoint sp = Database.setSavepoint();
      try
      {
        LocalizedValueStore store = new LocalizedValueStore();
        store.setStoreKey(sKey);
        store.getStoreValue().setValue(sValue);
        store.setStoreTag(tagName);
        store.apply();
        
        newProps++;
      }
      catch (DuplicateDataException e)
      {
        Database.rollbackSavepoint(sp);
        
        LocalizedValueStore store = LocalizedValueStore.getByKey(sKey);
        store.appLock();
        store.setStoreKey(sKey);
        store.getStoreValue().setValue(sValue);
        store.setStoreTag(tagName);
        store.apply();
        
        replacedProps++;
      }
      finally
      {
        Database.releaseSavepoint(sp);
      }
    }
    
    logger.info("Added " + newProps + " new properties and updated " + replacedProps + " existing values in the LocalizedValueStore.");
  }
}
