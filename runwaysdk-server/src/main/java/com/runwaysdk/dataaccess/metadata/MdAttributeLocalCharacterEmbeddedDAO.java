package com.runwaysdk.dataaccess.metadata;

import java.util.Map;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterEmbeddedInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalCharacterEmbeddedDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeLocal;

public class MdAttributeLocalCharacterEmbeddedDAO extends MdAttributeLocalEmbeddedDAO implements MdAttributeLocalCharacterEmbeddedDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -5159428428690263486L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeLocalCharacterEmbeddedDAO()
  {
    super();
  }

  /**
   * Constructs a {@link MdAttributeLocalCharacterEmbeddedDAO} from the given
   * hashtable of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> classType != null
   * 
   * 
   * @param attributeMap
   * @param classType
   */
  public MdAttributeLocalCharacterEmbeddedDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Map, String)
   */
  public MdAttributeLocalCharacterEmbeddedDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeLocalCharacterEmbeddedDAO(attributeMap, classType);
  }

  @Override
  public MdAttributeLocalCharacterEmbeddedDAO getBusinessDAO()
  {
    return (MdAttributeLocalCharacterEmbeddedDAO) super.getBusinessDAO();
  }

  public static MdAttributeLocalCharacterEmbeddedDAO newInstance()
  {
    return (MdAttributeLocalCharacterEmbeddedDAO) BusinessDAO.newInstance(MdAttributeLocalCharacterEmbeddedInfo.CLASS);
  }
}
