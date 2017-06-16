package com.runwaysdk.dataaccess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public abstract class IndicatorElementDAO extends BusinessDAO implements IndicatorElementDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 1842135005287070391L;

  public IndicatorElementDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public IndicatorElementDAO()
  {
    super();
  }
  
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public IndicatorElementDAO getBusinessDAO()
  {
    return (IndicatorElementDAO) super.getBusinessDAO();
  }
  
  public static class IndicatorVisitor
  {
    /**
     * Key: The key of the {@link MdAttributePrimitiveDAOIF}
     * Object: {@link MdAttributePrimitiveDAOIF}
     */
    private Map<String, MdAttributePrimitiveDAOIF> attrPrimitiveMap;
  
    public IndicatorVisitor()
    {
      this.attrPrimitiveMap = new HashMap<String, MdAttributePrimitiveDAOIF>();
    }
  
    public void addMdAttributePrimitive(MdAttributePrimitiveDAOIF mdAttributePrimitive)
    {
      this.attrPrimitiveMap.put(mdAttributePrimitive.getKey(), mdAttributePrimitive);
    }
  
    public List<MdAttributePrimitiveDAOIF> getMdAttributePrimitives()
    {
      return this.attrPrimitiveMap.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }
  
    public void visit(IndicatorCompositeDAOIF indicatorComposite)
    {
      // Nothing to collect for now.
    }
    
    public void visit(IndicatorPrimitiveDAOIF indicatorPrimitive)
    {
      MdAttributePrimitiveDAOIF mdAttrPrimitive = indicatorPrimitive.getMdAttributePrimitive();
      this.addMdAttributePrimitive(mdAttrPrimitive);
    }
  }

}
