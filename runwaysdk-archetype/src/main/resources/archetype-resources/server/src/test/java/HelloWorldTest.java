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
