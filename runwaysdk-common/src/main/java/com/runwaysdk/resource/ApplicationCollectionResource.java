package com.runwaysdk.resource;

import java.util.Collection;

public interface ApplicationCollectionResource extends ApplicationResource
{
  public Collection<ApplicationResource> getContents();
}
