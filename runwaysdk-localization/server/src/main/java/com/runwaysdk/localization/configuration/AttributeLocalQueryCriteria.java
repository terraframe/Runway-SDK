package com.runwaysdk.localization.configuration;

import java.util.ArrayList;
import java.util.List;

import com.runwaysdk.dataaccess.EntityDAOIF;

public class AttributeLocalQueryCriteria
{
  private List<String> entityKeyMustInclude = new ArrayList<String>();
  
  private List<String> definingTypeMustNotInclude = new ArrayList<String>();
  
  public AttributeLocalQueryCriteria()
  {
    
  }
  
  public void definingTypeMustNotInclude(String badWord)
  {
    this.definingTypeMustNotInclude.add(badWord);
  }
  
  public void entityKeyMustInclude(String required)
  {
    this.entityKeyMustInclude.add(required);
  }
  
  public boolean shouldExportDefiningType(String definingType)
  {
    return !this.definingTypeMustNotInclude.contains(definingType);
  }
  
  public boolean shouldExportEntity(EntityDAOIF entity)
  {
    if (this.entityKeyMustInclude.size() == 0)
    {
      return true;
    }
    
    for (String mustInclude : this.entityKeyMustInclude)
    {
      if (entity.getKey().contains(mustInclude))
      {
        return true;
      }
    }
    
    return false;
  }
}
