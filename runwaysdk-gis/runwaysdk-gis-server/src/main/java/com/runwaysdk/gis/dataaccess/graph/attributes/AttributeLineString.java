package com.runwaysdk.gis.dataaccess.graph.attributes;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.graph.attributes.Attribute;
import com.runwaysdk.gis.AttributeLineStringParseException;
import com.runwaysdk.gis.dataaccess.MdAttributeLineStringDAOIF;
import com.runwaysdk.session.Session;
import com.vividsolutions.jts.geom.LineString;

public class AttributeLineString extends Attribute
{

  /**
   * 
   */
  private static final long serialVersionUID = 356523685120439629L;

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String)
   */
  protected AttributeLineString(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass)
  {
    super(mdAttributeDAOIF, definingGraphClass);
  }

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String, String)
   */
  protected AttributeLineString(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass, LineString value)
  {
    super(mdAttributeDAOIF, definingGraphClass, value);
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by a concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return {@link MdAttributeLineStringDAOIF} that defines the this attribute
   */
  public MdAttributeLineStringDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeLineStringDAOIF) super.getMdAttributeConcrete();
  }

  /**
   * @see Attribute#validate(Object)
   *
   */
  @Override
  public void validate(Object valueToValidate)
  {
    MdAttributeLineStringDAOIF mdAttributeIF = this.getMdAttributeConcrete();

    // First verify that the object is of the correct type.
    if (valueToValidate != null && ! ( valueToValidate instanceof LineString ))
    {
      String devMessage = "Value is not a " + LineString.class.getName();
      throw new AttributeLineStringParseException(devMessage, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()), valueToValidate.getClass().getName());
    }

    super.validate(valueToValidate);
  }
}
