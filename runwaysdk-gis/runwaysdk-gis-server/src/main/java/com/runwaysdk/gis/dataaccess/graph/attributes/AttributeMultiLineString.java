package com.runwaysdk.gis.dataaccess.graph.attributes;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.graph.attributes.Attribute;
import com.runwaysdk.gis.AttributeMultiLineStringParseException;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiLineStringDAOIF;
import com.runwaysdk.session.Session;
import com.vividsolutions.jts.geom.MultiLineString;

public class AttributeMultiLineString extends Attribute
{

  /**
   * 
   */
  private static final long serialVersionUID = 356523685120439629L;

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String)
   */
  protected AttributeMultiLineString(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass)
  {
    super(mdAttributeDAOIF, definingGraphClass);
  }

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String, String)
   */
  protected AttributeMultiLineString(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass, MultiLineString value)
  {
    super(mdAttributeDAOIF, definingGraphClass, value);
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by a concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return {@link MdAttributeMultiLineStringDAOIF} that defines the this
   *         attribute
   */
  public MdAttributeMultiLineStringDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeMultiLineStringDAOIF) super.getMdAttributeConcrete();
  }

  /**
   * @see Attribute#validate(Object)
   *
   */
  @Override
  public void validate(Object valueToValidate)
  {
    MdAttributeMultiLineStringDAOIF mdAttributeIF = this.getMdAttributeConcrete();

    // First verify that the object is of the correct type.
    if (valueToValidate != null && ! ( valueToValidate instanceof MultiLineString ))
    {
      String devMessage = "Value is not a " + MultiLineString.class.getName();
      throw new AttributeMultiLineStringParseException(devMessage, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()), valueToValidate.getClass().getName());
    }

    super.validate(valueToValidate);
  }
}
