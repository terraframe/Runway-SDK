package com.runwaysdk.dataaccess.graph.attributes;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeSet;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;

public class AttributeEnumeration extends Attribute implements AttributeSet
{

  /**
   * 
   */
  private static final long serialVersionUID = 356523685120439629L;

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String)
   */
  protected AttributeEnumeration(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass)
  {
    super(mdAttributeDAOIF, definingGraphClass, new TreeSet<String>());
  }

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String, String)
   */
  protected AttributeEnumeration(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass, Set<String> value)
  {
    super(mdAttributeDAOIF, definingGraphClass, value);
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by a concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return {@link MdAttributeEnumerationDAOIF} that defines the this attribute
   */
  public MdAttributeEnumerationDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeEnumerationDAOIF) super.getMdAttributeConcrete();
  }

  @Override
  public synchronized void setValue(Object value)
  {
    if (this.getObjectValue().size() == 1 && this.getObjectValue().contains(value))
    {
      return;
    }
    else
    {
      this.validate(value);

      this.clearItems();
      this.getObjectValue().add((String) value);

      this.setModified(true);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public Set<String> getObjectValue()
  {
    return (Set<String>) super.getObjectValue();
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
    // Set<String> stringValue = (Set<String>) valueToValidate;
    //
    // boolean blankValue = false;
    // if (stringValue == null || stringValue.size() == 0)
    // {
    // blankValue = true;
    // }
    //
    // // make sure a value is provided if a value is required
    // if (mdAttributeIF.isRequired() && blankValue)
    // {
    // String error = "Attribute [" + getName() + "] on type [" +
    // getDefiningClassType() + "] requires a value";
    // EmptyValueProblem problem = new
    // EmptyValueProblem(this.getContainingComponent().getProblemNotificationId(),
    // mdAttributeIF.definedByClass(), mdAttributeIF, error, this);
    // problem.throwIt();
    // }
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
      MdAttributeEnumerationDAOIF mdAttributeIF = this.getMdAttributeConcrete();

      MdEnumerationDAOIF mdEnumeration = mdAttributeIF.getMdEnumerationDAO();

      if (mdEnumeration != null)
      {
        String enumItemID = (String) valueToValidate;

        if (!mdEnumeration.isValidEnumerationItem(enumItemID))
        {
          String error = "[" + enumItemID + "] is not a valid value for the enumerated Attribute [" + getName() + "] on type [" + getDefiningClassType() + "]";
          throw new AttributeValueException(error, this, enumItemID);
        }
      }
    }

    super.validate(valueToValidate);
  }

  @Override
  public boolean addItem(String value)
  {
    if (this.getObjectValue().contains(value))
    {
      return true;
    }

    MdAttributeEnumerationDAOIF mdAttribute = this.getMdAttributeConcrete();

    this.validate(value);

    if (!mdAttribute.selectMultiple())
    {
      this.clearItems();
    }

    this.getObjectValue().add(value);

    this.setModified(true);
    return true;
  }

  @Override
  public boolean replaceItems(Collection<String> values)
  {
    this.value = new TreeSet<String>(values);

    return true;
  }

  @Override
  public void removeItem(String value)
  {
    this.getObjectValue().remove(value);
    this.setModified(true);
  }

  @Override
  public void clearItems()
  {
    this.getObjectValue().clear();
    this.setModified(true);
  }
}
