package com.runwaysdk.resource;

public class ClasspathResourceTest extends ApplicationTreeResourceTest
{
  public ClasspathResourceTest()
  {
    super(new ClasspathResource("com/runwaysdk/resources/xsd/webservice.xsd"));
  }
}
