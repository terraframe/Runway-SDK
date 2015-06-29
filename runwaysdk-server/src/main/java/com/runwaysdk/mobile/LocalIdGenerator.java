/**
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
 */
package com.runwaysdk.mobile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.EmptyStackException;
import java.util.Stack;

import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.mobile.IdConverter.LocalIdGeneratorCache;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

class LocalIdGenerator implements Serializable {
  private static final long serialVersionUID = 6220355735426778054L;
  
  private transient LocalIdGeneratorCache cache;
  private String mobileId;
  private int idGenCounter;
  private int currentStackIndex;
  private transient BusinessDAOIF thisDAO = null;
  private transient Stack<String> stack = null;
  private transient BusinessDAOIF stackDAO = null;
  
  private LocalIdGenerator(String mobileId, LocalIdGeneratorCache cache) {
    super();
    
    this.cache = cache;
    this.mobileId = mobileId;
    this.idGenCounter = 0;
    this.currentStackIndex = 1;
    
    stack = new Stack<String>();
    persistCurrentStack();
  }
  
  private void push(String data) {
    if (stack.size() == IdConverter.MAX_LOCAL_IDS_PER_STACK) {
      //System.out.println("stack size (" + maxSize + ") reached. Creating new stack with index " + (currentStackIndex+1));
      stack = new Stack<String>();
      currentStackIndex++;
      stack.push(data);
      stackDAO = null;
      persistCurrentStack();
      persistThis();
    }
    else {
      stack.push(data);
      persistCurrentStack();
    }
  }
  
  private String pop() {
    if (stack.size() == 0) {
      if (currentStackIndex == 1) {
        throw new EmptyStackException();
      }
      else {
        //System.out.println("Stack size has reached 0. currentStackIndex = " + currentStackIndex);
        deleteStack(mobileId, currentStackIndex);
        currentStackIndex--;
        loadCurrentStack();
        persistThis();
      }
    }
    
    String rtn = stack.pop();
    persistCurrentStack();
    return rtn;
  }
  
  public static String generateNewLocalId(String mobileId, LocalIdGeneratorCache cache) {
    LocalIdGenerator localIdGen = loadLocalIdGenerator(mobileId, cache);
    
    if (localIdGen == null) {
      localIdGen = new LocalIdGenerator(mobileId, cache);
      localIdGen.idGenCounter++;
      localIdGen.persistThis();
      return BaseConverter.toMaxBase(0);
    }
    
    try {
      return localIdGen.pop();
    }
    catch (EmptyStackException e) {
      int localId = localIdGen.idGenCounter;
      ++localIdGen.idGenCounter;
      localIdGen.persistThis();
      
      return BaseConverter.toMaxBase(localId);
    }
  }
  
  public static void pushFreeLocalId(String mobileId, String localId, LocalIdGeneratorCache cache) {
    LocalIdGenerator localIdGen = loadLocalIdGenerator(mobileId, cache);
    
    if (localIdGen == null) {
      throw new IdConversionException("The provided mobileId has never generated any localIds.");
    }
    
    localIdGen.push(localId);
  }
  
  private void persistThis() {
    ByteArrayOutputStream byteOuts = new ByteArrayOutputStream();
    ObjectOutputStream objOut;
    
    try
    {
      objOut = new ObjectOutputStream(byteOuts);
      objOut.writeObject(this);
    }
    catch (IOException e)
    {
      throw new IdConversionException(e);
    }
    
    byte[] bytes = byteOuts.toByteArray();
    
    // The DAO was null because it was loaded from the cache, but this isn't a new instance, so load the DAO.
    if (thisDAO == null && cache.contains(mobileId, 0)) {
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(IdConverter.LINKED_STACK_PERSIST_TYPENAME);
      query.WHERE(query.aCharacter("mobileId").EQ(mobileId).AND(query.aInteger("stackIndex").EQ(0)));
      
      OIterator<BusinessDAOIF> iterator = query.getIterator();
      
      try {
        if (!iterator.hasNext()) {
          throw new IdConversionException("Attempting to load the LocalIdGenerator for the mobile id [" + mobileId + "] failed.");
        }
        
        thisDAO = iterator.next();
      }
      finally {
        iterator.close();
      }
    }
    
    
    if (thisDAO != null) {
      BusinessDAO dao = thisDAO.getBusinessDAO();
      dao.setBlob("serializedStack", bytes);
      dao.apply();
      //System.out.println("PersistThis existing instance " + bytes);
    }
    else {
      // The DAO object is null and thus does not exist in the database, it is a new instance.
      BusinessDAO stackPersist = BusinessDAO.newInstance(IdConverter.LINKED_STACK_PERSIST_TYPENAME);
      stackPersist.setValue("mobileId", mobileId);
      stackPersist.setValue("stackIndex", String.valueOf(0));
      stackPersist.setBlob("serializedStack", bytes);
      stackPersist.apply();
      //System.out.println("PersistThis new instance " + bytes);
    }
    
    cache.put(mobileId, 0, bytes);
  }
  
  private void persistCurrentStack() {
    ByteArrayOutputStream byteOuts = new ByteArrayOutputStream();
    ObjectOutputStream objOut;
    
    try
    {
      objOut = new ObjectOutputStream(byteOuts);
      objOut.writeObject(stack);
    }
    catch (IOException e)
    {
      throw new IdConversionException(e);
    }
    
    byte[] bytes = byteOuts.toByteArray();
    
    
    // The DAO was null because it was loaded from the cache, but this isn't a new instance, so load the DAO.
    if (stackDAO == null && cache.contains(mobileId, currentStackIndex)) {
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(IdConverter.LINKED_STACK_PERSIST_TYPENAME);
      query.WHERE(query.aCharacter("mobileId").EQ(mobileId).AND(query.aInteger("stackIndex").EQ(currentStackIndex)));
      
      OIterator<BusinessDAOIF> iterator = query.getIterator();
      
      try {
        if (!iterator.hasNext()) {
          throw new IdConversionException("Attempting to load the stack for the mobile id [" + mobileId + "] with index [" + currentStackIndex + "] failed.");
        }
        
        stackDAO = iterator.next();
      }
      finally {
        iterator.close();
      }
    }
    
    if (stackDAO != null) {
      BusinessDAO dao = stackDAO.getBusinessDAO();
      dao.setBlob("serializedStack", bytes);
      dao.apply();
    }
    else {
      // The persistence object is null and thus does not exist in the database, it is a new instance.
      BusinessDAO stackPersist = BusinessDAO.newInstance(IdConverter.LINKED_STACK_PERSIST_TYPENAME);
      stackPersist.setValue("mobileId", mobileId);
      stackPersist.setValue("stackIndex", String.valueOf(currentStackIndex));
      stackPersist.setBlob("serializedStack", bytes);
      stackPersist.apply();
    }
    
    cache.put(mobileId, currentStackIndex, bytes);
  }
  
  private static LocalIdGenerator loadLocalIdGenerator(String mobileId, LocalIdGeneratorCache cache) {
    // Check the cache first
    byte[] serialized = cache.get(mobileId, 0);
    if (serialized != null) {
      try {
        ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(serialized));
        LocalIdGenerator rtnObj = (LocalIdGenerator) objIn.readObject();
        rtnObj.cache = cache;
        rtnObj.loadCurrentStack();
        return rtnObj;
      }
      catch(Throwable t) {
        throw new IdConversionException(t);
      }
    }
    
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(IdConverter.LINKED_STACK_PERSIST_TYPENAME);
    query.WHERE(query.aCharacter("mobileId").EQ(mobileId).AND(query.aInteger("stackIndex").EQ(0)));
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    try {
      if (!iterator.hasNext()) {
        //throw new IdConversionException("Attempting to load the LocalIdGenerator for the mobile id [" + mobileId + "] failed.");
        return null;
      }
      
      BusinessDAOIF stackPersist = iterator.next();
      
      ByteArrayInputStream byteIn = new ByteArrayInputStream(stackPersist.getBlob("serializedStack"));
      ObjectInputStream objIn = new ObjectInputStream(byteIn);
      
      LocalIdGenerator retObj = (LocalIdGenerator) objIn.readObject();
      retObj.cache = cache;
      retObj.thisDAO = stackPersist;
      retObj.loadCurrentStack();
      return retObj;
    }
    catch (IOException e)
    {
      throw new IdConversionException(e);
    }
    catch (ClassNotFoundException e)
    {
      throw new IdConversionException(e);
    }
    finally {
      iterator.close();
    }
  }
  
  @SuppressWarnings("unchecked")
  private void loadCurrentStack() {
    // Check the cache first
    byte[] serialized = cache.get(mobileId, currentStackIndex);
    if (serialized != null) {
      try {
        ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(serialized));
        stack = (Stack<String>) objIn.readObject();
        return;
      }
      catch(Throwable t) {
        throw new IdConversionException(t);
      }
    }
    
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(IdConverter.LINKED_STACK_PERSIST_TYPENAME);
    query.WHERE(query.aCharacter("mobileId").EQ(mobileId).AND(query.aInteger("stackIndex").EQ(currentStackIndex)));
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    try {
      if (!iterator.hasNext()) {
        throw new IdConversionException("Attempt to load the current stack, but it does not exist.");
      }
      
      //System.out.println("Stack successfuly loaded. stackindex = " + currentStackIndex);
      
      BusinessDAOIF stackPersist = iterator.next();
      
      stackDAO = stackPersist;
      
      ByteArrayInputStream byteIn = new ByteArrayInputStream(stackPersist.getBlob("serializedStack"));
      ObjectInputStream objIn = new ObjectInputStream(byteIn);
      stack = (Stack<String>) objIn.readObject();
    }
    catch (IOException e)
    {
      throw new IdConversionException(e);
    }
    catch (ClassNotFoundException e)
    {
      throw new IdConversionException(e);
    }
    finally {
      iterator.close();
    }
  }
  
  private void deleteStack(String mobileId, Integer index) {
    cache.remove(mobileId, index);
    
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(IdConverter.LINKED_STACK_PERSIST_TYPENAME);
    query.WHERE(query.aCharacter("mobileId").EQ(mobileId).AND(query.aInteger("stackIndex").EQ(index)));
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    try {
      if (!iterator.hasNext()) {
        throw new IdConversionException("Attempt to delete a stack that does not exist.");
      }
      
      BusinessDAOIF stackPersist = iterator.next();
      stackPersist.getBusinessDAO().delete();
    }
    finally {
      iterator.close();
    }
  }
}
