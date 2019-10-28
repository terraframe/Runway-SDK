package com.runwaysdk.dataaccess.graph.attributes;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.attributes.EmptyValueProblem;
import com.runwaysdk.dataaccess.attributes.ImmutableAttributeProblem;
import com.runwaysdk.dataaccess.attributes.SystemAttributeProblem;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.graph.GraphObjectDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;

public abstract class Attribute implements AttributeIF
{

  /**
   * 
   */
  private static final long          serialVersionUID = -94524589150108217L;

  /**
   * Canonical name of the attribute. <br>
   * <b>invariant </b> name != null <br>
   * <b>invariant </b> !name.trim().equals("")
   */
  final private String               name;

  /**
   * The {@link GraphObjectDAO} that contains this Attribute. Not defined on
   * creation, but set by the containing {@link GraphObjectDAO. <br>
   * <b>invariant </b> graphObjectDAO != null
   */
  private GraphObjectDAO             containingGraphObjectDAO;

  /**
   * Qualified name of the class that defines this attribute. <br>
   * <b>invariant </b> definingGraphClass != null <br>
   * <b>invariant </b> !definingGraphClass.equals("")
   */
  final private String               definingGraphClass;

  protected MdAttributeConcreteDAOIF mdAttributeDAOIF;

  /**
   * Value of the attribute. <br>
   * <b>invariant </b> value != null
   */
  protected Object                   value;

  /**
   * Indicates if the value of this Attribute has been modified since it was
   * last applied to the database.
   */
  private boolean                    isModified       = false;

  /**
   * Creates an attribute with the given name and initializes the value to
   * blank.
   *
   * <br>
   * <b>Precondition: </b> definingGraphClass != null <br>
   * <b>Precondition: </b> !definingGraphClass().equals("") <br>
   * <b>Precondition: </b> definingGraphClass is the name of a class that
   * defines an attribute with this name
   *
   * @param name
   *          name of the attribute
   * @param mdAttributeDAOIF
   *          defining metadata.
   * @param definingGraphClass
   *          name of the class that defines this attribute
   */
  protected Attribute(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass)
  {
    super();
    this.name = mdAttributeDAOIF.definesAttribute();
    this.mdAttributeDAOIF = mdAttributeDAOIF;
    this.definingGraphClass = definingGraphClass;
    this.value = null;
    this.containingGraphObjectDAO = null;
  }

  /**
   * Creates an attribute with the given name and initializes the value to the
   * given value.
   *
   * <br>
   * <b>Precondition: </b> definingGraphClass != null <br>
   * <b>Precondition: </b> !definingGraphClass.trim().equals("") <br>
   * <b>Precondition: </b> definingGraphClass represents a class that defines an
   * attribute with this name <br>
   * <b>Precondition: </b> value != null
   *
   * @param name
   *          name of the attribute
   * @param mdAttributeDAOIF
   *          defining metadata.
   * @param definingGraphClass
   *          name of the class that defines this attribute
   * @param value
   *          initial value of the attribute
   */
  protected Attribute(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass, Object value)
  {
    this(mdAttributeDAOIF, definingGraphClass);

    this.value = value;
  }

  /**
   * @see AttributeIF#getName()
   */
  @Override
  public String getName()
  {
    return this.name;
  }

  /**
   * @see AttributeIF#getDisplayLabel(Locale)
   */
  @Override
  public String getDisplayLabel(Locale locale)
  {
    return this.getMdAttribute().getDisplayLabel(locale);
  }

  /**
   * @see AttributeIF#getDisplayLabes()
   */
  @Override
  public Map<String, String> getDisplayLabes()
  {
    return this.getMdAttribute().getDisplayLabels();
  }

  /**
   * @see AttributeIF#isModified()
   */
  @Override
  public boolean isModified()
  {
    return this.isModified;
  }

  /**
   * Sets the attribute as modified.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   *
   * @param isModified
   *          true if the attribute has been modified false otherwise.
   */
  public void setModified(boolean isModified)
  {
    this.isModified = isModified;
  }

  /**
   * Sets the state of the attribute object after a transaction has been
   * committed/
   *
   */
  public void setCommitState()
  {
    this.setModified(false);
  }

  /**
   * @see AttributeIF#getContainingComponent()
   */
  @Override
  public GraphObjectDAO getContainingComponent()
  {
    if (this.containingGraphObjectDAO == null)
    {
      String error = "Attribute [" + name + "]'s Contianing Component is null. It is the component's " + "responsiblity to call setContainingComponent() for all of its attributes.";
      throw new DataNotFoundException(error, MdTypeDAO.getMdTypeDAO(ComponentInfo.CLASS));
    }
    return this.containingGraphObjectDAO;
  }

  /**
   * Sets a reference to the object that contains this attribute
   *
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> this.getContainingComponent() == transientDAO
   *
   * @param container
   */
  public void setContainingComponent(GraphObjectDAO containingGraphObjectDAO)
  {
    this.containingGraphObjectDAO = containingGraphObjectDAO;
  }

  /**
   * @see AttributeIF#getDefiningClassType()
   */
  @Override
  public String getDefiningClassType()
  {
    return this.definingGraphClass;
  }

  /**
   * @see AttributeIF#getValue()
   */
  @Override
  public String getValue()
  {    
    if (this.value != null)
    {
      return this.value.toString();
    }
    else
    {
      return "NULL";
    }
    
  }

  /**
   * @see AttributeIF#getObjectValue()
   */
  @Override
  public Object getObjectValue()
  {
    return this.value;
  }

  /**
   * @see AttributeIF#getRawValue()
   */
  @Override
  public String getRawValue()
  {
    return this.getValue();
  }

  /**
   * Most, but not all, attributes are represented as strings. For some, they
   * are represented as objects. Object will be cast into the appropriate type.
   * Precondition object is of expected type.
   */
  public void setValue(Object value)
  {
    this.validate(value);

    // If the new value is the same as the old one, skip it
    if (this.value == null || !this.value.equals(value))
    {
      this.value = value;
    }
  }

  /**
   * For internal use only as it bypasses validation.
   * 
   * @param value
   */
  public void setValueInternal(Object value)
  {
    this.value = value;
  }

  /**
   * Check to see if the given value is valid {@link GraphObjectDAO}.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   *
   * @param valueToValidate
   *          The value to be checked if its required in the the defining
   *          {@link GraphObjectDAO}
   * @param {@link
   *          MdAttributeDAOIF} The Metatdata TransientDAO that defines the
   *          Attribute
   *
   * @throws EmptyValueProblem
   *           if this attribute is required for its defining
   *           {@link GraphObjectDAO} but contains an empty value.
   */
  public void validate(Object valueToValidate)
  {
    this.validateSystem();

    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttribute();
    this.validateMutable(mdAttributeIF);
  }

  /**
   * Checks if this attribute is required for its defining
   * {@link GraphObjectDAO}.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   *
   * @param valueToValidate
   *          The value to be checked if its required in the the defining
   *          {@link GraphObjectDAO}
   * @param {@link
   *          MdAttributeDAOIF} The Metatdata TransientDAO that defines the
   *          Attribute
   *
   * @throws EmptyValueProblem
   *           if this attribute is required for its defining
   *           {@link GraphObjectDAO} but contains an empty value.
   */
  public void validateRequired(Object valueToValidate, MdAttributeDAOIF mdAttributeIF)
  {
    // make sure a value is provided if a value is required
    if (mdAttributeIF.isRequired() && valueToValidate == null)
    {
      this.throwEmptyValueProblem(mdAttributeIF);
    }
  }

  /** 
   * Creates an {@link EmptyValueProblem}
   * 
   * @param mdAttributeIF
   */
  protected void throwEmptyValueProblem(MdAttributeDAOIF mdAttributeIF)
  {
    String error = "Attribute [" + getName() + "] on type [" + getDefiningClassType() + "] requires a value";
    EmptyValueProblem problem = new EmptyValueProblem(this.getContainingComponent().getProblemNotificationId(), mdAttributeIF.definedByClass(), mdAttributeIF, error, this);
    problem.throwIt();
  }
  

  /**
   * Checks if this attribute is required for its defining
   * {@link GraphObjectDAO}.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   *
   * @param valueToValidate
   *          The value to be checked if its required in the the defining
   *          BusinessDAO.
   *
   * @throws {@link
   *           SystemAttributeProblem} if this attribute is required for its
   *           defining {@link GraphObjectDAO} but contains an empty value.
   */
  protected void validateSystem()
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttribute();
    // make sure a value is provided if a value is required
    if (mdAttributeIF.isSystem())
    {
      String error = "Attribute [" + getName() + "] on type [" + getDefiningClassType() + "] is a system attribute and cannot be modified.";
      SystemAttributeProblem problem = new SystemAttributeProblem(this.getContainingComponent().getProblemNotificationId(), mdAttributeIF.definedByClass(), mdAttributeIF, error, this);
      problem.throwIt();
    }
  }

  /**
   * Checks if this attribute is immutable.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   *
   * @param mdAttribute
   *          The Metatdata BusinessDAO that defines the Attribute
   *
   * @throws ImmutableAttributeProblem
   *           if this attribute is immutable, and the containing component is
   *           not new
   */
  protected void validateMutable(MdAttributeConcreteDAOIF mdAttribute)
  {
    if (!mdAttribute.isImmutable() || this.containingGraphObjectDAO.isNew() || ServerProperties.getAllowModificationOfMdAttribute())
    {
      return;
    }
    else
    {
      String error = "Cannot change the immutable attribute [" + getName() + "] on type [" + getDefiningClassType() + "]";
      ImmutableAttributeProblem problem = new ImmutableAttributeProblem(this.getContainingComponent().getProblemNotificationId(), mdAttribute.definedByClass(), mdAttribute, error, this);
      problem.throwIt();
    }
  }

  /**
   * @see AttributeIF#getMdAttribute()
   */
  @Override
  public MdAttributeConcreteDAOIF getMdAttribute()
  {
    return this.mdAttributeDAOIF;
  }

  /**
   * @see AttributeIF#getMdAttributeConcrete()
   */
  @Override
  public MdAttributeConcreteDAOIF getMdAttributeConcrete()
  {
    return this.getMdAttribute();
  }

  /**
   * @see AttributeIF#getAllMdAttributes()
   */
  @Override
  public Set<MdAttributeConcreteDAOIF> getAllMdAttributes()
  {
    Set<MdAttributeConcreteDAOIF> set = new HashSet<MdAttributeConcreteDAOIF>();
    set.add(this.getMdAttribute());
    return set;
  }

  /**
   * Cleans up any object that references this attribute in some way.
   * <b>Precondition: </b> this attribute MUST be a member of the given
   * EntityDAO <br>
   * 
   * @param graphClassDAO
   */
  public void removeReferences(GraphObjectDAO graphClassDAO, boolean businessContext)
  {
  }

  /**
   * Initializes any references to this attribute. Many attribute classes have
   * nothing to initialize.
   * 
   * <b>Precondition: </b> this MdAttribute MUST be the metadata of this
   * attribute. <br>
   */
  public void initReferences(MdAttributeConcreteDAOIF mdAttribute)
  {
  }

  /**
   * Updates any references to this attribute. Many attribute classes aren't
   * referenced by anything.
   * 
   * <b>Precondition: </b> this MdAttribute MUST be the metadata of this
   * attribute. <br>
   */
  public void updateReferences(MdAttributeConcreteDAOIF mdAttribute)
  {
  }

  @Override
  public String toString()
  {
    return getName() + "=" + getValue().toString();
  }

  /**
   * Sets appliedToDB to false if the object is new, as the database will
   * rollback any newly inserted records.
   * 
   */
  public void rollbackState()
  {
    // Balk: In general this method does not need to do anything.
    // However, it behavior is overwritten in some of its children
    // classes.
  }

}
