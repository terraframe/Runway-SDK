package com.runwaysdk.dataaccess.attributes.value;

import java.util.Set;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;

public class AttributeIndicator extends Attribute
{
  /**
   * 
   */
  private static final long serialVersionUID = -515867588485578378L;

  /**
   * Creates an attribute with the given name.
   * 
   * @param name name of the attribute
   * @param the value of the attribute
   * @param definingEntityType name of the class that defines this attribute from which the value came
   * @param mdAttributeIF metadata that defines the attribute from which the value came.
   * @param entityMdAttributeIFset all MdAttributes that were involved in constructing this attribute.
   */
  protected AttributeIndicator(String name, String value, String definingEntityType, MdAttributeConcreteDAOIF mdAttributeIF, Set<MdAttributeConcreteDAOIF> entityMdAttributeIFset)
  {
    super(name, value, definingEntityType, mdAttributeIF, entityMdAttributeIFset);
  }

}
