package com.runwaysdk.dataaccess.graph.attributes;

import com.runwaysdk.AttributeIntegerParseException;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.session.Session;

public class AttributeBoolean extends Attribute
{

  /**
   * 
   */
  private static final long serialVersionUID = 7368938382761174978L;

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String)
   */
  protected AttributeBoolean(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass)
  {
    super(mdAttributeDAOIF, definingGraphClass);
  }

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String, String)
   */
  protected AttributeBoolean(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass, Boolean value)
  {
    super(mdAttributeDAOIF, definingGraphClass, value);
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by a concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return {@link MdAttributeBooleanDAOIF} that defines the this attribute
   */
  public MdAttributeBooleanDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeBooleanDAOIF) super.getMdAttributeConcrete();
  }

  /**
   * @see Attribute#validate(Object)
   *
   */
  @Override
  public void validate(Object valueToValidate)
  {
    if (valueToValidate != null)
    {
      MdAttributeBooleanDAOIF mdAttributeIF = this.getMdAttributeConcrete();

      // First verify that the object is of the correct type.
      if (! ( valueToValidate instanceof Boolean ))
      {
        String devMessage = "Value is not a " + Boolean.class.getName();
        throw new AttributeIntegerParseException(devMessage, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()), valueToValidate.getClass().getName());
      }
    }

    super.validate(valueToValidate);
  }

}
