package com.runwaysdk.controller;

/**
 * Programming exception thrown when multiple endpoints with the same action
 * name are registered multiple times
 * 
 * @author terraframe
 */
public class DuplicateEndpointException extends RuntimeException
{

  /**
   * 
   */
  private static final long serialVersionUID = 3536724705273765083L;

  private String            endpoint;

  /**
   * 
   */
  public DuplicateEndpointException(String endpoint)
  {
    super();

    this.endpoint = endpoint;
  }

  /**
   * @param message
   * @param cause
   */
  public DuplicateEndpointException(String endpoint, String message)
  {
    super(message);

    this.endpoint = endpoint;
  }

  public String getEndpoint()
  {
    return endpoint;
  }
}
