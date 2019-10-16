package com.runwaysdk.dataaccess.graph.attributes;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF;

public class AttributeFactory
{
  /**
   * Returns an Attribute object of the appropriate sub class for the given
   * DataAccess attribute class.
   * 
   * <br/>
   * <b>Precondition:</b> attributeType != null <br/>
   * <b>Precondition:</b> !attributeType.trim().equals("") <br/>
   * <b>Precondition:</b> attributeType is a concrete sub class of
   * Constants.MD_ATTRIBUTE <br/>
   * <b>Precondition:</b> mdAttributeDAOIF != null <br/>
   * <b>Precondition:</b> !attributeName.trim().equals("") <br/>
   * <b>Precondition:</b> definingType != null <br/>
   * <b>Precondition:</b> !definingType.trim().equals("") <br/>
   * <b>Precondition:</b> attributeValue != null <br/>
   * <b>Postcondition:</b> return value may not be null
   * 
   * @param attributeType
   * @param mdAttributeKey
   *          key of the defining metadata.
   * @param attributeName
   * @param definingType
   * @param attributeValue
   * 
   * @return Attribute object of the appropriate sub class for the given
   *         DataAccess attribute type
   */
  public static Attribute createAttribute(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingType)
  {
    Attribute attribute = null;
    
    if (mdAttributeDAOIF instanceof MdAttributeUUIDDAOIF)
    {
      attribute = new AttributeUUID(mdAttributeDAOIF, definingType);
    }
    else
    {
      attribute = new AttributeCharacter(mdAttributeDAOIF, definingType);
    }
    
    return attribute;
  }
}
