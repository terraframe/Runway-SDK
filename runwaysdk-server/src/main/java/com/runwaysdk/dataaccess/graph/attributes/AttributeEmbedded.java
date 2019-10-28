package com.runwaysdk.dataaccess.graph.attributes;

import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.ComponentDAO;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEmbeddedDAOIF;

public class AttributeEmbedded extends Attribute
{
  /**
   * 
   */
  private static final long serialVersionUID = -4481544615179246050L;

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String)
   */
  protected AttributeEmbedded(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass)
  {
    super(mdAttributeDAOIF, definingGraphClass);
  }
  
  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String, ComponentDAO)
   */
  protected AttributeEmbedded(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass, ComponentDAO value)
  {
    super(mdAttributeDAOIF, definingGraphClass, value);
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by a concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return {@link MdAttributeEmbeddedDAOIF} that defines the this attribute
   */
  public MdAttributeEmbeddedDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeEmbeddedDAOIF) super.getMdAttributeConcrete();
  }
  
  /**
   * @see AttributeIF#getObjectValue()
   */
  @Override
  public ComponentDAO getObjectValue()
  {
    return (ComponentDAO)super.getObjectValue();
  }
  
  @Override
  public String toString()
  {
    String stringValue = super.toString();
    
    stringValue += "\n-----------EMBEDDED OBJECT-------------\n";
    
    stringValue += this.getObjectValue().toString();
    
    return stringValue;
  }
  
}
