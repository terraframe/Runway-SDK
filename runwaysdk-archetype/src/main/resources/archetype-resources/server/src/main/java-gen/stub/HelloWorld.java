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
package ${package};

import java.util.Random;

public class HelloWorld extends HelloWorldBase implements com.runwaysdk.generation.loader.
{
  private static final long serialVersionUID = 413407027;
  
  /**
   * A list of common English greetings.
   */
  private static String[] greetings = new String[]{
    "Ahoy",
    "G'day",
    "Greetings",
    "Hello",
    "Hey",
    "Hi",
    "How are you?",
    "How's it going?",
    "Howdy",
    "Salutations",
    "What's up?",
    "Yo",
    "Sup",
    "Hello World"
  };
  
  public HelloWorld()
  {
    super();
  }
  
  @Override
  public String toString() {
    return "["+this.getClassDisplayLabel()+"] - "+this.getGreeting();
  }
  
  /**
   * Generates a HelloWorld object with a random greeting.
   * 
   * @return
   */
  public static HelloWorld generateRandom()
  {
    int index = new Random().nextInt(greetings.length);
    String greeting = greetings[index];
    
    HelloWorld helloWorld = new HelloWorld();
    helloWorld.setGreeting(greeting);
    return helloWorld;
  }
}
