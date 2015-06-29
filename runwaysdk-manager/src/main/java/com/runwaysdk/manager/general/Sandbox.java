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
package com.runwaysdk.manager.general;


public class Sandbox
{
//  static abstract class TransactionThread implements Runnable
//  {
//    private ThreadTransactionState state;
//
//    private boolean                finished;
//
//    public TransactionThread(ThreadTransactionState state)
//    {
//      this.state = state;
//      this.finished = false;
//    }
//
//    public boolean isFinished()
//    {
//      return this.finished;
//    }
//
//    @Override
//    public void run()
//    {
//      try
//      {
//        testRequest(state);
//      }
//      finally
//      {
//        this.finished = true;
//      }
//    }
//
//    @Request(RequestType.THREAD)
//    private void testRequest(ThreadTransactionState state)
//    {
//      testTransaction(state);
//    }
//
//    @Transaction(TransactionType.THREAD)
//    private void testTransaction(ThreadTransactionState state)
//    {
//      test();
//    }
//
//    public abstract void test();
//  }
//
//  static class ImportThread implements Runnable
//  {
//    @Request
//    public void run()
//    {
//      call();
//    }
//
//    @Transaction
//    public void call()
//    {
//      Thread thread = RequestState.getCurrentRequestState().getMainThread();
//      System.out.println(thread.getName());
//
//      final ThreadTransactionState state = ThreadTransactionState.getCurrentThreadTransactionState();
//
//      TransactionThread testThread = new TransactionThread(state)
//      {
//        @Override
//         public void test()
//        {
//          Thread thread = RequestState.getCurrentRequestState().getMainThread();
//          System.out.println(thread.getName());
//        }
//      };
//
//      Thread t = new Thread(testThread);
//      t.setDaemon(true);
//      t.start();
//
//      while (!testThread.isFinished())
//      {
//        try
//        {
//          Thread.sleep(1000);
//        }
//        catch (InterruptedException e)
//        {
//          e.printStackTrace();
//        }
//      }
//    }
//
//  }
//
//  @Request
//  public static void main(String[] args)
//  {
////    createRelationshipClasses();
//
//    MdRelationshipDAOIF mdRelationship = MdRelationshipDAO.getMdRelationshipDAO(Constants.TEST_PACKAGE + ".Relationship1");
//    
//    RelationshipDAO relationship = RelationshipDAO.newInstance(IDGenerator.nextID(), IDGenerator.nextID(), mdRelationship.definesType());
//    relationship.apply();
//  }
//  
//  @Request
//  public static void createRelationshipClasses()
//  {
//    createRelationshipClassesTransaction();
//  }
//  
//  @Transaction
//  public static void createRelationshipClassesTransaction()
//  {
//    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
//    mdBusiness.apply();
//    
//    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
//    mdBusiness2.apply();
//    
//    MdRelationshipDAO mdRelationship = TestFixtureFactory.createMdRelationship1(mdBusiness, mdBusiness2);
//    mdRelationship.apply();
//    
//    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
//    businessDAO.apply();
//
//    BusinessDAO business2DAO = BusinessDAO.newInstance(mdBusiness2.definesType());
//    business2DAO.apply();    
//  }
//
//  @Request
//  public static void createClasses()
//  {
//    createClassesTransaction();
//  }
//  
//  @Transaction
//  private static void createClassesTransaction()
//  {
//    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
//    mdBusiness.apply();
//    
//    MdAttributeCharacterDAO mdAttributeCharacter = TestFixtureFactory.createCharacterAttribute(mdBusiness);
//    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getId());
//    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, "true");
//    mdAttributeCharacter.apply();
//    
//    MdBusinessDAO mdBusiness2 = TestFixtureFactory.createMdBusiness2();
//    mdBusiness2.apply();
//    
//    MdAttributeReferenceDAO mdAttributeReference = TestFixtureFactory.addReferenceAttribute(mdBusiness2, mdBusiness);
//    mdAttributeReference.setValue(MdAttributeReferenceInfo.REQUIRED, "true");
//    mdAttributeReference.apply();
//    
//    MdAttributeReferenceDAO mdAttributeReference2 = TestFixtureFactory.addReferenceAttribute(mdBusiness, mdBusiness2);
//    mdAttributeReference2.setValue(MdAttributeReferenceInfo.REQUIRED, "false");
//    mdAttributeReference2.apply();    
//  }
//  
//  @Request
//  private static void createObjects()
//  {    
//    createObjectsTransaction();
//  }
//  
//  @Transaction
//  private static void createObjectsTransaction()
//  {
//    BusinessDAO businessDAO = BusinessDAO.newInstance(Constants.TEST_PACKAGE + "." + Constants.TEST_CLASS1);
//    businessDAO.setValue(Constants.ATTRIBUTE_NAME, "test");
//    businessDAO.apply();
//    
//    BusinessDAO referenceDAO = BusinessDAO.newInstance(Constants.TEST_PACKAGE + "." + Constants.TEST_CLASS2);
//    referenceDAO.setValue("testReference", businessDAO.getId());
//    referenceDAO.apply();
//    
//    businessDAO.setValue("testReference", referenceDAO.getId());
//    businessDAO.apply();
//  }
//
//  public static void testGetCurrentThread()
//  {
//    new Thread(new ImportThread()).start();
//  }
//
//  @Request
//  public static void createRoles()
//  {
//    roles(3);
//  }
//
//  @Transaction
//  private static void roles(int count)
//  {
//    RoleDAO role = null;
//
//    for (int i = 0; i < count; i++)
//    {
//      if (role == null)
//      {
//        role = RoleDAO.createRole("number.Role" + i, "Numbered Role " + i);
//      }
//      else
//      {
//        role = role.addAscendant("number.Role" + i, "Numbered Role " + i);
//      }
//    }
//  }
//
//  @Request
//  public static void createUsers()
//  {
//    users(3);
//  }
//
//  @Transaction
//  private static void users(int count)
//  {
//    for (int i = 0; i < count; i++)
//    {
//      UserDAO user = UserDAO.newInstance();
//      user.setUsername("TestUser" + i);
//      user.setPassword("TestUser" + i);
//      user.apply();
//    }
//  }
}
