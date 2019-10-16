package com.runwaysdk.dataaccess.graph.attributes;

import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFloatDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLongDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
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
    else if (mdAttributeDAOIF instanceof MdAttributeIntegerDAOIF)
    {
      attribute = new AttributeInteger(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeLongDAOIF)
    {
      attribute = new AttributeLong(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeDoubleDAOIF)
    {
      attribute = new AttributeDouble(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeFloatDAOIF)
    {
      attribute = new AttributeFloat(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeBooleanDAOIF)
    {
      attribute = new AttributeBoolean(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeDateDAOIF)
    {
      attribute = new AttributeDate(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeDateTimeDAOIF)
    {
      attribute = new AttributeDateTime(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeTimeDAOIF)
    {
      attribute = new AttributeTime(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeCharacterDAOIF)
    {
      attribute = new AttributeCharacter(mdAttributeDAOIF, definingType);
    }
    else
    {
      throw new UnsupportedOperationException("Graph AttributeFactory doesn't support the metadata [" + mdAttributeDAOIF.getClass().getName() + "].");
      // attribute = new AttributeCharacter(mdAttributeDAOIF, definingType);
    }

    return attribute;
  }
}
