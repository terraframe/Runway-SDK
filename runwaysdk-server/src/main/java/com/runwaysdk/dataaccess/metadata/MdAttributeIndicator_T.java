package com.runwaysdk.dataaccess.metadata;

import com.runwaysdk.constants.MdAttributeConcreteInfo;

public class MdAttributeIndicator_T extends MdAttributeConcrete_T
{

  /**
   * 
   */
  private static final long serialVersionUID = 8410913086961615052L;

  /**
   * @param mdAttribute
   */
  public MdAttributeIndicator_T(MdAttributeIndicatorDAO mdAttribute)
  {
    super(mdAttribute);
  } 
  
  protected void preSaveValidate()
  {
    super.preSaveValidate();
    
    if (this.getMdAttribute().isNew() && !this.appliedToDB)
    {
      // Supply a default value, as it does not make sense to have a column name for an attribute defined on an external table.
      this.getMdAttribute().getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValue("n_a");
    }
  }
  
}
