package com.runwaysdk.dataaccess.graph.attributes;

import com.runwaysdk.dataaccess.ComponentDAO;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;

public class AttributeLocalCharacterEmbedded extends AttributeLocalEmbedded
{
  /**
   * 
   */
  private static final long serialVersionUID = -4481544615179246050L;

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String)
   */
  protected AttributeLocalCharacterEmbedded(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass)
  {
    super(mdAttributeDAOIF, definingGraphClass);
  }

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String, ComponentDAO)
   */
  protected AttributeLocalCharacterEmbedded(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass, ComponentDAO value)
  {
    super(mdAttributeDAOIF, definingGraphClass, value);
  }
}
