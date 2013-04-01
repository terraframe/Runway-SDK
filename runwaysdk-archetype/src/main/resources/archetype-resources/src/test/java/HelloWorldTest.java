package ${package};


import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.session.Request;
import ${package}.HelloWorld;

public class HelloWorldTest {
  
  /**
   * Tests the simple creating and retrieval of a HelloWorld object.
   */
  @Test
  @Request
  public void createHelloWorld()
  {
    HelloWorld original = null;
    try
    {
      original = new HelloWorld();
      original.setGreeting("Hey-yo!");
      original.apply();
    
      HelloWorld fetched = HelloWorld.get(original.getId());
      
      Assert.assertEquals(original, fetched);
    }
    finally
    {
      if(original.isAppliedToDB())
      {
        original.delete();
      }
    }
  }
  
  /**
   * Tests that a HelloWorld object with a randomized greeting
   * is generated.
   */
  @Test
  @Request
  public void generateRandom()
  {
    HelloWorld generated = HelloWorld.generateRandom();
    String greeting = generated.getGreeting();    
    
    Assert.assertNotNull(greeting);
    Assert.assertTrue(greeting.trim().length() > 0);
  }
}
