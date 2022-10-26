/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.mvc;

import java.util.Random;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.constants.ClientRequestIF;
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
    return new RestBodyResponse(Integer.valueOf(15));
  }

  @Endpoint(url = "generate", method = ServletMethod.POST)
  public RestBodyResponse generateInteger()
  {
    return new RestBodyResponse(Integer.valueOf(this.random.nextInt()));
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

  @Endpoint(method = ServletMethod.POST)
  public ResponseIF runway(ClientRequestIF request, @RequestParamter(name = "dto", parser = ParseType.RUNWAY_JSON) BusinessDTO dto)
  {
    RestResponse response = new RestResponse();
    response.set("dto", dto);
    response.set("sessionId", request.getSessionId());

    return response;
  }

  @Endpoint(method = ServletMethod.GET)
  public ResponseIF bad()
  {
    throw new RuntimeException("Test Message");
  }

  @Endpoint(method = ServletMethod.GET, error = ErrorSerialization.JSON)
  public ResponseIF hello(@RequestParamter(name = "message") String message)
  {
    throw new RuntimeException(message);
  }
}
