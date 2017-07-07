package com.runwaysdk.dataaccess.metadata;

import java.math.BigDecimal;
import java.util.Map;

import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.constants.MdAttributeBooleanUtil;
import com.runwaysdk.constants.MdAttributeDecimalUtil;
import com.runwaysdk.constants.MdAttributeDoubleUtil;
import com.runwaysdk.constants.MdAttributeFloatUtil;
import com.runwaysdk.constants.MdAttributeIndicatorInfo;
import com.runwaysdk.constants.MdAttributeIntegerUtil;
import com.runwaysdk.constants.MdAttributeLongUtil;
import com.runwaysdk.dataaccess.AttributeReferenceIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.IndicatorElementDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIndicatorDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdTransientDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.transport.metadata.AttributeBooleanMdDTO;
import com.runwaysdk.transport.metadata.AttributeNumberMdDTO;
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
    return new MdAttributeIndicatorDAO(attributeMap, MdAttributeIndicatorInfo.CLASS);
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
  public IndicatorElementDAOIF getIndicator()
  {
    return (IndicatorElementDAOIF)((AttributeReferenceIF)this.getAttributeIF(MdAttributeIndicatorInfo.INDICATOR_ELEMENT)).dereference();
  }

  @Override
  public void delete(boolean _businessContext)
  {    
    // Delete this from the database so that the reference check on the indicator do not prevent
    // it from being deleted because they reference this object.
    super.delete(_businessContext);
    
    // Delete referenced indicator
    IndicatorElementDAOIF indicator = this.getIndicator();
    indicator.getBusinessDAO().delete();
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
    return com.runwaysdk.query.SelectableIndicator.class.getName();
  }

  @Override
  public String javaType(boolean isDTO)
  {
    return this.getIndicator().javaType();
    
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.metadata.MdAttribute#generateTypesafeFormatting(java.lang.String)
   */
  protected String generateTypesafeFormatting(String formatMe)
  {
    String util;
    
    String type = this.getIndicator().javaType();
    
    if (type.equals(Boolean.class.getSimpleName()))
    {
      util = MdAttributeBooleanUtil.class.getName();
    }
    else if (type.equals(BigDecimal.class.getName()))
    {
      util = MdAttributeDecimalUtil.class.getName();
    }
    else if (type.equals(Double.class.getSimpleName()))
    {
      util = MdAttributeDoubleUtil.class.getName();
    }
    else if (type.equals(Float.class.getSimpleName()))
    {
      util = MdAttributeFloatUtil.class.getName();
    }
    else if (type.equals(Long.class.getSimpleName()))
    {
      util = MdAttributeLongUtil.class.getName();
    }
    else if (type.equals(Integer.class.getSimpleName()))
    {
      util = MdAttributeIntegerUtil.class.getName();
    }
    else
    {
      util = MdAttributeDoubleUtil.class.getName();
    }
    
    return util + ".getTypeSafeValue(" + formatMe + ")";
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public String attributeMdDTOType()
  {
    String type = this.getIndicator().javaType();

    if (type.equals(Boolean.class.getName()))
    {
      return AttributeBooleanMdDTO.class.getName();
    }
    else
    {
      return AttributeNumberMdDTO.class.getName();
    }
  }

  @Override
  protected void initializeStrategyObject()
  {
    if (this.definedByClass() instanceof MdEntityDAOIF)
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeIndicator_E(this));
    }
    else if (this.definedByClass() instanceof MdTransientDAOIF)
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeConcrete_S(this));
    }
    else
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeConcrete_T(this));
    }

  }


  
  
}
