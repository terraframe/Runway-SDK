package com.runwaysdk.gis.dataaccess.graph.attributes;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.graph.attributes.Attribute;
import com.runwaysdk.gis.AttributeMultiPolygonParseException;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPolygonDAOIF;
import com.runwaysdk.session.Session;
import com.vividsolutions.jts.geom.MultiPolygon;

public class AttributeMultiPolygon extends Attribute
{

  /**
   * 
   */
  private static final long serialVersionUID = 356523685120439629L;

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String)
   */
  protected AttributeMultiPolygon(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass)
  {
    super(mdAttributeDAOIF, definingGraphClass);
  }

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String, String)
   */
  protected AttributeMultiPolygon(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass, MultiPolygon value)
  {
    super(mdAttributeDAOIF, definingGraphClass, value);
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by a concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return {@link MdAttributeMultiPolygonDAOIF} that defines the this
   *         attribute
   */
  public MdAttributeMultiPolygonDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeMultiPolygonDAOIF) super.getMdAttributeConcrete();
  }

  /**
   * @see Attribute#validate(Object)
   *
   */
  @Override
  public void validate(Object valueToValidate)
  {
    MdAttributeMultiPolygonDAOIF mdAttributeIF = this.getMdAttributeConcrete();

    // First verify that the object is of the correct type.
    if (valueToValidate != null && ! ( valueToValidate instanceof MultiPolygon ))
    {
      String devMessage = "Value is not a " + MultiPolygon.class.getName();
      throw new AttributeMultiPolygonParseException(devMessage, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()), valueToValidate.getClass().getName());
    }

    super.validate(valueToValidate);
  }
}
