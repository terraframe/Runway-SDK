package com.runwaysdk.dataaccess.metadata;

public class DeleteContext
{
  private boolean businessContext;

  private boolean enforceRemovable;

  private boolean removeValues;

  private boolean executeImmediately;

  public DeleteContext()
  {
    this(false);
  }

  public DeleteContext(boolean businessContext)
  {
    this.businessContext = businessContext;
    this.enforceRemovable = true;
    this.removeValues = true;
    this.executeImmediately = false;
  }

  public boolean isBusinessContext()
  {
    return businessContext;
  }

  public DeleteContext setBusinessContext(boolean businessContext)
  {
    this.businessContext = businessContext;

    return this;
  }

  public boolean isEnforceRemovable()
  {
    return enforceRemovable;
  }

  public DeleteContext setEnforceRemovable(boolean enforceRemovable)
  {
    this.enforceRemovable = enforceRemovable;

    return this;
  }

  public boolean isRemoveValues()
  {
    return removeValues;
  }

  public DeleteContext setRemoveValues(boolean removeValues)
  {
    this.removeValues = removeValues;

    return this;
  }

  public boolean isExecuteImmediately()
  {
    return executeImmediately;
  }

  public DeleteContext setExecuteImmediately(boolean executeImmediately)
  {
    this.executeImmediately = executeImmediately;

    return this;
  }
}
