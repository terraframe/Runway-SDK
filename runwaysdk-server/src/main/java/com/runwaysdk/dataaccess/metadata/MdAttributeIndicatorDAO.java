package com.runwaysdk.dataaccess.metadata;

import java.util.Map;

import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.constants.MdAttributeIndicatorInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.dataaccess.AttributeReferenceIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.IndicatorDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIndicatorDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeIndicatorDAO extends MdAttributeConcreteDAO implements MdAttributeIndicatorDAOIF
{
  

  /**
   * 
   */
  private static final long serialVersionUID = -854643054147455045L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeIndicatorDAO()
  {
    super();
  }

  /**
   * Constructs a {@link MdAttributeIndicatorDAO} from the given hashtable of Attributes.
   *
   * <br/><b>Precondition:</b> attributeMap != null
   * <br/><b>Precondition:</b> type != null
   * <br/><b>Precondition:</b>ObjectCache.isSubTypeOf(classType, Constants.MD_CLASS)
   *
   * @param attributeMap
   * @param classType
   */
  public MdAttributeIndicatorDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }
  
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable,
   *      java.util.String, ComponentDTOIF, Map)
   */
  public MdAttributeIndicatorDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeIndicatorDAO(attributeMap, MdAttributeReferenceInfo.CLASS);
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeIndicatorDAO getBusinessDAO()
  {
    return (MdAttributeIndicatorDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new {@link MdAttributeIndicatorDAO}. Some attributes will contain default
   * values, as defined in the attribute metadata. Otherwise, the attributes
   * will be blank.
   * 
   * @return {@link MdAttributeIndicatorDAO}.
   */
  public static MdAttributeIndicatorDAO newInstance()
  {
    return (MdAttributeIndicatorDAO) BusinessDAO.newInstance(MdAttributeIndicatorInfo.CLASS);
  }
  
  /**
   * @see MdAttributeIndicatorDAOIF#getIndicator
   */
  public IndicatorDAOIF getIndicator()
  {
    return (IndicatorDAOIF)((AttributeReferenceIF)this.getAttributeIF(MdAttributeIndicatorInfo.INDICATOR)).dereference();
  }
  
  /**
   * Method used for testing purposes but not applicable for indicator attributes.
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

  //Heads up: Test: Create a Selectable implementation for Indicators and return that instead
  @Override
  public String queryAttributeClass()
  {
    return com.runwaysdk.query.SelectableReference.class.getName();
  }

  @Override
  public String javaType(boolean isDTO)
  {
    return this.getIndicator().javaType();
    
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
