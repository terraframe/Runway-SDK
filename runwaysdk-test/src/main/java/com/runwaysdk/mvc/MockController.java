package com.runwaysdk.mvc;

import java.util.Random;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.controller.ServletMethod;

@Controller(url = "test")
public class MockController
{
  private Random random;

  public MockController()
  {
    this.random = new Random();
  }

  public RestBodyResponse testMethod()
  {
    return new RestBodyResponse(new Integer(15));
  }

  @Endpoint(url = "generate", method = ServletMethod.POST)
  public RestBodyResponse generateInteger()
  {
    return new RestBodyResponse(new Integer(this.random.nextInt()));
  }

  public RestBodyResponse number(@RequestParamter(name = "value") Integer value)
  {
    return new RestBodyResponse(value);
  }

  @Endpoint(method = ServletMethod.POST)
  public ResponseIF dto(@RequestParamter(name = "dto", parser = ParseType.BASIC_JSON) BusinessDTO dto)
  {
    RestResponse response = new RestResponse();
    response.set("dto", dto);

    return response;
  }
}
