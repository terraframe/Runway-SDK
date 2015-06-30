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
package com.runwaysdk.mobile;

import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;

/**
 * 
 * @author Richard Rowlands
 * 
 *         This class takes a globally unique server id and converts it into a
 *         concise mobile id and back again. This new id is unique to a client's
 *         session and will reduce network load.
 * 
 */
public class IdConverter
{
  private static final String          PACKAGE                       = "com.runwaysdk.system.mobile";
  
  private static final String          LOCAL_ID_MAPPING_TYPENAME     = PACKAGE + ".LocalIdMapping";

  private static final String          SESSION_ID_MAPPING_TYPENAME   = PACKAGE + ".SessionIdToMobileIdMapping";

  protected static final String        LINKED_STACK_PERSIST_TYPENAME = PACKAGE + ".LinkedStackPersistance";

  private final GlobalIdLocalIdCache   globalLocalCache              = new GlobalIdLocalIdCache(30);

  private final LocalIdGlobalIdCache   localGlobalCache              = new LocalIdGlobalIdCache(globalLocalCache.getMaxSize());

  private final SessionIdMobileIdCache sessionMobileCache            = new SessionIdMobileIdCache(30);

  private final MobileIdSessionIdCache mobileSessionCache            = new MobileIdSessionIdCache(sessionMobileCache.getMaxSize());

  // The total number of local ids stored in memory by the id generator is these
  // two numbers multiplied together.
  protected static final int           MAX_LOCAL_IDS_PER_STACK       = 30;

  private final LocalIdGeneratorCache  localIdGeneratorCache         = new LocalIdGeneratorCache(30);

  private static class SingletonHolder
  {
    public static final IdConverter instance = new IdConverter();
  }

  public static IdConverter getInstance()
  {
    return SingletonHolder.instance;
  }

  private IdConverter()
  {
    // initialize();
  }

  // NOTE: This concatenation separator MUST NOT be a valid character in global
  // id strings or local id strings.
  private static final String KEY_CONCATENATION_SEPARATOR = "_";

  private class GlobalIdLocalIdCache extends RunwayMemoryCache<String, String>
  {
    public GlobalIdLocalIdCache(int maxSize)
    {
      super(maxSize);
    }

    public String get(String mobileId, String globalId)
    {
      String key = mobileId.concat(KEY_CONCATENATION_SEPARATOR).concat(globalId);
      return super.get(key);
    }

    public void put(String mobileId, String globalId, String localId)
    {
      String key = mobileId.concat(KEY_CONCATENATION_SEPARATOR).concat(globalId);
      super.put(key, localId);
    }

    public String remove(String mobileId, String globalId)
    {
      String key = mobileId.concat(KEY_CONCATENATION_SEPARATOR).concat(globalId);
      return super.remove(key);
    }

    @Override
    protected void onPut(String key, String localId)
    {
      String[] split = key.split(KEY_CONCATENATION_SEPARATOR);
      String mobileId = split[0];
      String globalId = split[1];

      BusinessDAO idMapping = BusinessDAO.newInstance(LOCAL_ID_MAPPING_TYPENAME);
      idMapping.setValue("mobileId", mobileId);
      idMapping.setValue("globalId", globalId);
      idMapping.setValue("localId", localId);
      idMapping.apply();
    }

    @Override
    protected String onMiss(String key)
    {
      String[] split = key.split(KEY_CONCATENATION_SEPARATOR);
      String mobileId = split[0];
      String globalId = split[1];

      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(LOCAL_ID_MAPPING_TYPENAME);
      query.WHERE(query.aCharacter("globalId").EQ(globalId).AND(query.aCharacter("mobileId").EQ(mobileId)));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      try
      {
        if (iterator.hasNext())
        {
          return iterator.next().getAttributeIF("localId").getValue();
        }
      }
      finally
      {
        iterator.close();
      }

      return null;
    }
  }

  private class LocalIdGlobalIdCache extends GlobalIdLocalIdCache
  {
    public LocalIdGlobalIdCache(int maxSize)
    {
      super(maxSize);
    }

    @Override
    protected void onPut(String key, String localId)
    {
      // Persistence is handled in the GlobalIdLocalIdCache, since we have 2
      // caches for the same mapping
    }

    @Override
    protected String onMiss(String key)
    {
      String[] split = key.split(KEY_CONCATENATION_SEPARATOR);
      String mobileId = split[0];
      String localId = split[1];

      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(LOCAL_ID_MAPPING_TYPENAME);
      query.WHERE(query.aCharacter("localId").EQ(localId).AND(query.aCharacter("mobileId").EQ(mobileId)));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      try
      {
        if (iterator.hasNext())
        {
          return iterator.next().getAttributeIF("globalId").getValue();
        }
      }
      finally
      {
        iterator.close();
      }

      return null;
    }
  }

  private class SessionIdMobileIdCache extends RunwayMemoryCache<String, String>
  {
    public SessionIdMobileIdCache(int maxSize)
    {
      super(maxSize);
    }

    @Override
    protected void onPut(String sessionId, String mobileId)
    {
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(SESSION_ID_MAPPING_TYPENAME);
      query.WHERE(query.aCharacter("mobileId").EQ(mobileId));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      try
      {
        if (iterator.hasNext())
        {
          BusinessDAO mapping = iterator.next().getBusinessDAO();
          mapping.setValue("sessionId", sessionId);
          mapping.apply();
          return;
        }
      }
      finally
      {
        iterator.close();
      }

      BusinessDAO sessionMapping = BusinessDAO.newInstance(SESSION_ID_MAPPING_TYPENAME);
      sessionMapping.setValue("sessionId", sessionId);
      sessionMapping.setValue("mobileId", mobileId);
      sessionMapping.apply();
    }

    @Override
    protected String onMiss(String sessionId)
    {
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(SESSION_ID_MAPPING_TYPENAME);
      query.WHERE(query.aCharacter("sessionId").EQ(sessionId));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      try
      {
        if (iterator.hasNext())
        {
          return iterator.next().getValue("mobileId");
        }
      }
      finally
      {
        iterator.close();
      }

      return null;
    }
  }

  private class MobileIdSessionIdCache extends RunwayMemoryCache<String, String>
  {
    public MobileIdSessionIdCache(int maxSize)
    {
      super(maxSize);
    }

    @Override
    protected void onPut(String key, String value)
    {
      // Persistence is handled in the SessionIdMobileIdCache, since we have 2
      // caches for the same mapping
    }

    @Override
    protected String onMiss(String mobileId)
    {
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(SESSION_ID_MAPPING_TYPENAME);
      query.WHERE(query.aCharacter("mobileId").EQ(mobileId));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      try
      {
        if (iterator.hasNext())
        {
          return iterator.next().getValue("sessionId");
        }
      }
      finally
      {
        iterator.close();
      }

      return null;
    }
  }

  protected class LocalIdGeneratorCache extends RunwayMemoryCache<String, byte[]>
  {
    public LocalIdGeneratorCache(int maxSize)
    {
      super(maxSize);
    }

    public void put(String mobileId, Integer stackIndex, byte[] serialized)
    {
      String key = mobileId.concat(KEY_CONCATENATION_SEPARATOR).concat(String.valueOf(stackIndex));
      super.put(key, serialized);
    }

    public boolean contains(String mobileId, Integer stackIndex)
    {
      String key = mobileId.concat(KEY_CONCATENATION_SEPARATOR).concat(String.valueOf(stackIndex));
      return super.containsKey(key);
    }

    public byte[] get(String mobileId, Integer stackIndex)
    {
      String key = mobileId.concat(KEY_CONCATENATION_SEPARATOR).concat(String.valueOf(stackIndex));
      return super.get(key);
    }

    public void remove(String mobileId, Integer stackIndex)
    {
      String key = mobileId.concat(KEY_CONCATENATION_SEPARATOR).concat(String.valueOf(stackIndex));
      super.remove(key);
    }

    @Override
    protected void onPut(String key, byte[] value)
    {
    }

    @Override
    protected byte[] onMiss(String mobileId)
    {
      return null;
    }
  }

  /**
   * Returns the globalId that is mapped to the given localId, for a given
   * mobileId.
   * 
   * @param mobileId
   * @param localId
   * @return The globalId. If a mapping does not exist an IdConversionException
   *         is thrown.
   */
  @Request
  public String getGlobalIdFromLocalId(String mobileId, String localId)
  {
    if (localId != null && localId.length() > 0)
    {
      String globalId = localGlobalCache.get(mobileId, localId);

      if (globalId == null)
      {
        throw new IdConversionException("The supplied localId '" + localId + "' is not mapped to a globalId.");
      }

      return globalId;
    }

    return localId;
  }

  /**
   * Returns a localId that maps to the globalId, for a given mobileId. If a
   * mapping does not exist, it will generate a new one. If a mapping does exist
   * it will return the already mapped localId.
   * 
   * @param mobileId
   * @param globalId
   * @return localId.
   */
  @Request
  public synchronized String generateLocalIdFromGlobalId(String mobileId, String globalId)
  {
    return generateLocalIdFromGlobalIdTransaction(mobileId, globalId);
  }

  @Request
  public synchronized void setLocalIdFromGlobalId(String mobileId, String globalId, String localId)
  {
    globalLocalCache.put(mobileId, globalId, localId);
    localGlobalCache.put(mobileId, globalId, localId);
  }

  @Transaction
  private String generateLocalIdFromGlobalIdTransaction(String mobileId, String globalId)
  {
    if (globalId != null && globalId.length() > 0)
    {
      String localId = globalLocalCache.get(mobileId, globalId);

      if (localId == null)
      {
        localId = LocalIdGenerator.generateNewLocalId(mobileId, localIdGeneratorCache);

        globalLocalCache.put(mobileId, globalId, localId);
        localGlobalCache.put(mobileId, globalId, localId);
      }

      return localId;
    }

    return globalId;
  }

  /**
   * Invalidates the globalId and all mappings it is contained in. All localIds
   * that were mapped to the globalId are now released and may be remapped to a
   * different globalId.
   * 
   * @param globalId
   */
  @Request
  public synchronized void invalidateGlobalId(String globalId)
  {
    invalidateGlobalIdTransaction(globalId);
  }

  @Transaction
  private void invalidateGlobalIdTransaction(String globalId)
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(LOCAL_ID_MAPPING_TYPENAME);
    query.WHERE(query.aCharacter("globalId").EQ(globalId));

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        while (iterator.hasNext())
        {
          BusinessDAOIF mapping = iterator.next();

          String mobileId = mapping.getValue("mobileId");
          String localId = mapping.getValue("localId");

          // TODO : ordering by mobileId will increase efficiency.
          LocalIdGenerator.pushFreeLocalId(mobileId, localId, localIdGeneratorCache);

          globalLocalCache.remove(mobileId, globalId);
          localGlobalCache.remove(mobileId, localId);

          mapping.getBusinessDAO().delete();
        }
      }
      else
      {
        throw new IdConversionException("The globalId [" + globalId + "] is not mapped.");
      }
    }
    finally
    {
      iterator.close();
    }
  }

  /**
   * Maps the sessionId to the mobileId so that one can be retrieved from the
   * other later. If a mapping already exists between the given mobileId and
   * another sessionId, the mapping is updated.
   * 
   * @param sessionId
   * @param mobileId
   */
  @Request
  public synchronized void mapSessionIdToMobileId(String sessionId, String mobileId)
  {
    mobileSessionCache.put(mobileId, sessionId);
    sessionMobileCache.put(sessionId, mobileId);
  }

  /**
   * Retrieves the sessionId that is mapped to the provided mobileId. If a
   * mapping does not exist, an IdConversionException is thrown.
   * 
   * @param mobileId
   * @return sessionId
   */
  @Request
  public String getSessionIdFromMobileId(String mobileId)
  {
    String sessionId = mobileSessionCache.get(mobileId);

    if (sessionId == null)
    {
      throw new IdConversionException("The supplied mobileId '" + mobileId + "' is not mapped to a sessionId.");
    }

    return sessionId;
  }

  /**
   * Retrieves the mobileId that is mapped to the provided sessionId. If a
   * mapping does not exist, an IdConversionException is thrown.
   * 
   * @param sessionId
   * @return mobileId
   */
  @Request
  public String getMobileIdFromSessionId(String sessionId)
  {

    String mobileId = sessionMobileCache.get(sessionId);

    if (mobileId == null)
    {
      throw new IdConversionException("The supplied sessionId '" + sessionId + "' is not mapped to a mobileId.");
    }

    return mobileId;
  }
}
