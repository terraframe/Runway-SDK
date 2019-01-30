package com.runwaysdk.business.ontology;

import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;

public interface InitializationStrategyIF
{
  public void preApply(MdBusinessDAO mdBusiness);

  public void postApply(MdBusinessDAO mdBusiness);
}
