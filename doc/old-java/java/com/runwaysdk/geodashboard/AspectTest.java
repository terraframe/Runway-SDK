package com.runwaysdk.geodashboard;

import java.lang.reflect.InvocationTargetException;

import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.geodashboard.ontology.Classifier;
import com.runwaysdk.session.Request;
import com.runwaysdk.geodashboard.ServerSourceAspectTest;

public class AspectTest
{
  @Test
  public void testServerTransactions()
  {
    try
    {
      ServerSourceAspectTest.testTranasctionInClassloader();
    }
    catch (RuntimeException e)
    {
      Throwable t = e;
      while (t.getCause() != null && !t.getCause().equals(t))
      {
        t = t.getCause();
      }
      
      if (t.getMessage() == null || !t.getMessage().equals("Test Ex"))
      {
        throw e;
      }
      
      try
      {
        Classifier.getByKey("com.geodashboard.test.TestClassifier");
        
        Assert.fail("The transaction did not roll back.");
      }
      catch (DataNotFoundException dnfe)
      {
        return; // success case
      }
    }
    
    Assert.fail("An exception was never thrown.");
  }
  
  @Test
  public void testTestTransactions()
  {
    try
    {
      testTranasctionInClassloader();
    }
    catch (RuntimeException e)
    {
      Throwable t = e;
      while (t.getCause() != null && !t.getCause().equals(t))
      {
        t = t.getCause();
      }
      
      if (t.getMessage() == null || !t.getMessage().equals("Test Ex"))
      {
        throw e;
      }
      
      try
      {
        Classifier.getByKey("com.geodashboard.test.TestClassifier");
        
        Assert.fail("The transaction did not roll back.");
      }
      catch (DataNotFoundException dnfe)
      {
        return; // success case
      }
    }
    
    Assert.fail("An exception was never thrown.");
  }
  public static void testTranasctionInClassloader()
  {
    try
    {
      Class<?> aspectTest = LoaderDecorator.load(AspectTest.class.getName());
      aspectTest.getMethod("testTransactionInRequest", null).invoke(null, null);
    }
    catch (InvocationTargetException ite)
    {
      throw new RuntimeException(ite);
    }
    catch (NoSuchMethodException nsme)
    {
      throw new RuntimeException(nsme);
    }
    catch (IllegalAccessException iae)
    {
      throw new RuntimeException(iae);
    }
  }
  @Request
  public static void testTransactionInRequest()
  {
    testTransactionsInTransaction();
  }
  @Transaction
  public static void testTransactionsInTransaction()
  {
    Classifier c = new Classifier();
    c.setClassifierPackage("com.geodashboard.test");
    c.setClassifierId("TestClassifier");
    c.apply();
    System.out.println(c.getKey());
    
    throw new RuntimeException("Test Ex");
  }
}
