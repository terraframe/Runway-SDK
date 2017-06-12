package com.runwaysdk.dataaccess.metadata;

import java.util.Map;

import com.runwaysdk.constants.MdAttributeRatioInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.RatioPrimitiveInfo;
import com.runwaysdk.dataaccess.AttributeReferenceIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EnumerationItemDAOIF;
import com.runwaysdk.dataaccess.MdAttributeRatioDAOIF;
import com.runwaysdk.dataaccess.RatioDAOIF;
import com.runwaysdk.dataaccess.RatioPrimitiveDAO;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeRatioDAO extends MdAttributeConcreteDAO implements MdAttributeRatioDAOIF
{
  

  /**
   * 
   */
  private static final long serialVersionUID = -854643054147455045L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeRatioDAO()
  {
    super();
  }

  /**
   * Constructs a {@link MdAttributeRatioDAO} from the given hashtable of Attributes.
   *
   * <br/><b>Precondition:</b> attributeMap != null
   * <br/><b>Precondition:</b> type != null
   * <br/><b>Precondition:</b>ObjectCache.isSubTypeOf(classType, Constants.MD_CLASS)
   *
   * @param attributeMap
   * @param classType
   */
  public MdAttributeRatioDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }
  
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable,
   *      java.util.String, ComponentDTOIF, Map)
   */
  public MdAttributeRatioDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeRatioDAO(attributeMap, MdAttributeReferenceInfo.CLASS);
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeRatioDAO getBusinessDAO()
  {
    return (MdAttributeRatioDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new {@link MdAttributeRatioDAO}. Some attributes will contain default
   * values, as defined in the attribute metadata. Otherwise, the attributes
   * will be blank.
   * 
   * @return {@link MdAttributeRatioDAO}.
   */
  public static MdAttributeRatioDAO newInstance()
  {
    return (MdAttributeRatioDAO) BusinessDAO.newInstance(MdAttributeRatioInfo.CLASS);
  }
  
  /**
   * @see MdAttributeRatioDAOIF#getRatio
   */
  public RatioDAOIF getRatio()
  {
    return (RatioDAOIF)((AttributeReferenceIF)this.getAttributeIF(MdAttributeRatioInfo.RATIO)).dereference();
  }
  
  /**
   * Method used for testing purposes but not applicable for ratio attributes.
   * 
   * @throws UnsupportedOperationException()
   */
  @Override
  public void setRandomValue(EntityDAO object)
  {
    throw new UnsupportedOperationException();
  }

  //Heads up: Test: Implement this method
  @Override
  public AttributeMdSession getAttributeMdSession()
  {
    throw new UnsupportedOperationException();
  }

  //Heads up: Test: Create a Selectable implementation for Ratios and return that instead
  @Override
  public String queryAttributeClass()
  {
    return com.runwaysdk.query.SelectableReference.class.getName();
  }

  @Override
  public String javaType(boolean isDTO)
  {
    return this.getRatio().javaType();
    
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public String attributeMdDTOType()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected void initializeStrategyObject()
  {
    // TODO Auto-generated method stub

  }


  
  
}
