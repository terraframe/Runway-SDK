package com.runwaysdk.dataaccess.graph.attributes;

import com.runwaysdk.AttributeUUIDParseException;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.attributes.EmptyValueProblem;
import com.runwaysdk.session.Session;

public class AttributeReference extends Attribute
{

  /**
   * 
   */
  private static final long serialVersionUID = 7368938382761174978L;

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String)
   */
  protected AttributeReference(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass)
  {
    super(mdAttributeDAOIF, definingGraphClass);
  }

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String, String)
   */
  protected AttributeReference(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass, String value)
  {
    super(mdAttributeDAOIF, definingGraphClass, value);
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by a concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return {@link MdAttributeReferenceDAOIF} that defines the this attribute
   */
  public MdAttributeReferenceDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeReferenceDAOIF) super.getMdAttributeConcrete();
  }

  /**
   * 
   * @return
   */
  public BusinessDAOIF dereference()
  {
    MdAttributeReferenceDAOIF mdAttribute = getMdAttributeConcrete();

    return com.runwaysdk.dataaccess.attributes.entity.AttributeReference.dereference(this.getValue(), mdAttribute);
  }

  /**
   * @see Attribute#validateRequired(Object, MdAttributeDAOIF)
   * 
   *      <br>
   *      <b>Precondition: </b> value is of type UUID <br>
   *
   */
  public void validateRequired(Object valueToValidate, MdAttributeDAOIF mdAttributeIF)
  {
    // make sure a value is provided if a value is required
    if (mdAttributeIF.isRequired() && valueToValidate == null)
    {
      String error = "Attribute [" + getName() + "] on type [" + getDefiningClassType() + "] requires a value";
      EmptyValueProblem problem = new EmptyValueProblem(this.getContainingComponent().getProblemNotificationId(), mdAttributeIF.definedByClass(), mdAttributeIF, error, this);
      problem.throwIt();
    }
  }

  /**
   * @see Attribute#validate(Object)
   *
   */
  @Override
  public void validate(Object valueToValidate)
  {
    MdAttributeReferenceDAOIF mdAttributeIF = this.getMdAttributeConcrete();

    // First verify that the object is of the correct type.
    if (valueToValidate != null && ! ( valueToValidate instanceof String ))
    {
      String devMessage = "Value is not a " + String.class.getName();
      throw new AttributeUUIDParseException(devMessage, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()), valueToValidate.getClass().getName());
    }

    super.validate(valueToValidate);
  }

}
