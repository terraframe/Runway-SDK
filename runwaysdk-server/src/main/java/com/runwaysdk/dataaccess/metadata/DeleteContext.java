package com.runwaysdk.dataaccess.metadata;

public class DeleteContext
{
  private boolean businessContext;

  private boolean enforceRemovable;

  private boolean removeValues;

  public DeleteContext()
  {
    this(false);
  }

  public DeleteContext(boolean businessContext)
  {
    this.businessContext = businessContext;
    this.enforceRemovable = true;
    this.removeValues = true;
  }

  public boolean isBusinessContext()
  {
    return businessContext;
  }

  public void setBusinessContext(boolean businessContext)
  {
    this.businessContext = businessContext;
  }

  public boolean isEnforceRemovable()
  {
    return enforceRemovable;
  }

  public void setEnforceRemovable(boolean enforceRemovable)
  {
    this.enforceRemovable = enforceRemovable;
  }

  public boolean isRemoveValues()
  {
    return removeValues;
  }

  public void setRemoveValues(boolean removeValues)
  {
    this.removeValues = removeValues;
  }
}
