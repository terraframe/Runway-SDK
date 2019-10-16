package com.runwaysdk.dataaccess.graph.attributes;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;

public interface GraphAttributeFactoryIF
{
  public Attribute createGraphAttribute(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingType);
}
