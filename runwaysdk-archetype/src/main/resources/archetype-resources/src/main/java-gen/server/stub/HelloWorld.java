package ${package};

import java.util.Random;

public class HelloWorld extends HelloWorldBase implements com.runwaysdk.generation.loader.Reloadable
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
