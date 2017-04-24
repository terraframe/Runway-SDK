package com.runwaysdk.dataaccess.metadata;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdTableDAOIF;

public class MdAttributeConcrete_T extends MdAttributeConcreteStrategy
{
  /**
   * 
   */
  private static final long serialVersionUID = 1414234751280491256L;

  /**
   * @param mdAttribute
   */
  public MdAttributeConcrete_T(MdAttributeConcreteDAO mdAttribute)
  {
    super(mdAttribute);
  } 
  
  /**
   * Returns the {@link MdTableDAOIF} that defines this {@link MdAttributeConcreteDAOIF}.
   * 
   * @return the {@link MdTableDAOIF} that defines this {@link MdAttributeConcreteDAOIF}.
   */
  public MdTableDAOIF definedByClass()
  { 
    return (MdTableDAOIF) super.definedByClass();    
  }
  
  protected void preSaveValidate()
  {
    super.preSaveValidate();
 
    this.nonMdEntityCheckExistingForAttributeOnCreate();
  }
  
  
  /**
   * Contains special logic for saving an attribute.
   */
  public void save()
  {
    this.validate();
    
    this.nonMdEntityPostSaveValidationOperations();
  }

  /**
   * No special validation logic.
   */
  protected void validate() 
  {
    this.nonMdEntityValidate();
  }
}
