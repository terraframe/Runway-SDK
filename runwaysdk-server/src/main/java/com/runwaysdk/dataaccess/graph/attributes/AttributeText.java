package com.runwaysdk.dataaccess.graph.attributes;

import com.runwaysdk.AttributeTextParseException;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTextDAOIF;
import com.runwaysdk.session.Session;

public class AttributeText extends Attribute
{

  /**
   * 
   */
  private static final long serialVersionUID = 356523685120439629L;

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String)
   */
  protected AttributeText(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass)
  {
    super(mdAttributeDAOIF, definingGraphClass);
  }

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String, String)
   */
  protected AttributeText(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass, String value)
  {
    super(mdAttributeDAOIF, definingGraphClass, value);
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by a concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return {@link MdAttributeTextDAOIF} that defines the this attribute
   */
  public MdAttributeTextDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeTextDAOIF) super.getMdAttributeConcrete();
  }

  /**
   * @see Attribute#validateRequired(Object, MdAttributeDAOIF)
   * 
   *      <br>
   *      <b>Precondition: </b> value is of type String <br>
   *
   */
  @Override
  public void validateRequired(Object valueToValidate, MdAttributeDAOIF mdAttributeIF)
  {
    String stringValue = (String) valueToValidate;

    boolean blankValue = false;
    if (stringValue == null || stringValue.trim().equals(""))
    {
      blankValue = true;
    }

    // make sure a value is provided if a value is required
    if (mdAttributeIF.isRequired() && blankValue)
    {
      this.throwEmptyValueProblem(mdAttributeIF);
    }
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
      MdAttributeTextDAOIF mdAttributeIF = this.getMdAttributeConcrete();

      // First verify that the object is of the correct type.
      if (! ( valueToValidate instanceof String ))
      {
        String devMessage = "Value is not a " + String.class.getName();
        throw new AttributeTextParseException(devMessage, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()), valueToValidate.getClass().getName());
      }
    }

    super.validate(valueToValidate);
  }
}
