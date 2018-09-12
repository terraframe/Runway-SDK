package com.runwaysdk.dataaccess.metadata;

import java.util.Map;
import java.util.UUID;

import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeUUIDInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdTransientDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.transport.metadata.AttributeUUIDMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;
import com.runwaysdk.transport.metadata.caching.AttributeUUIDMdSession;

public class MdAttributeUUIDDAO extends MdAttributePrimitiveDAO implements MdAttributeUUIDDAOIF
{

  /**
   *
   */
  private static final long serialVersionUID = 998077633471702965L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeUUIDDAO()
  {
    super();
  }

  /**
   * Constructs a MdAttributeUUID from the given hashtable of Attributes.
   *
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null
   *
   * @param attributeMap
   * @param type
   */
  public MdAttributeUUIDDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdAttributeUUIDDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeUUIDDAO(attributeMap, classType);
  }

  @Override
  protected void initializeStrategyObject()
  {
    if (this.definedByClass() instanceof MdEntityDAOIF)
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeConcrete_E(this));
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

  /**
   * Called for java class generation. Returns the java type of this attribute
   * (boolean), which is used in the generated classes for type safety.
   *
   * @return The java type of this attribute (boolean)
   */
  public String javaType(boolean isDTO)
  {
    return "String";
  }

  /**
   * Returns the java class object for the attribute type.
   * 
   * @return the java class object for the attribute type.
   */
  public Class<?> javaClass()
  {
    return String.class;
  }

  /**
   * Returns a string representing the query attribute class for attributes of
   * this type.
   *
   * @return string representing the query attribute class for attributes of
   *         this type.
   */
  public String queryAttributeClass()
  {
    return com.runwaysdk.query.SelectableChar.class.getName();
  }

  @Override
  protected String generatedServerSetter(String attributeName)
  {
    String conversion = "java.lang.String.toString(value)";
    return this.setterWrapper(attributeName, conversion);
  }

  /**
   * Used for data generation. Returns a random boolean.
   */
  public void setRandomValue(EntityDAO object)
  {
    object.setValue(this.definesAttribute(), "" + UUID.randomUUID().toString());
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeUUIDDAO getBusinessDAO()
  {
    return (MdAttributeUUIDDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new BusinessDAO. Some attributes will contain default values, as
   * defined in the attribute metadata. Otherwise, the attributes will be blank.
   *
   * @return BusinessDAO instance of MdAttributeUUID.
   */
  public static MdAttributeUUIDDAO newInstance()
  {
    return (MdAttributeUUIDDAO) BusinessDAO.newInstance(MdAttributeUUIDInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String,
   * java.lang.String)
   */
  public static MdAttributeUUIDDAOIF get(String oid)
  {
    return (MdAttributeUUIDDAOIF) BusinessDAO.get(oid);
  }

  @Override
  public String attributeMdDTOType()
  {
    return AttributeUUIDMdDTO.class.getName();
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitUUID(this);
  }

  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession()
  {
    AttributeUUIDMdSession attrSes = new AttributeUUIDMdSession();
    super.populateAttributeMdSession(attrSes);
    return attrSes;
  }

  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeUUIDDAOIF.class.getName();
  }
  
  /**
   * If a default value has been defined for a dimension attached to this
   * session, then that value is returned, otherwise the default value assigned
   * to the attribute definition is returned.
   * 
   * @return default value
   */
  public String getAttributeInstanceDefaultValue()
  {
    return "";
  }
  
  /**
   * Precondition: assumes this character attribute is an OID. The collection of
   * <code>AttributeDAO</code> objects do not have their containing reference updated to
   * the returned <code>MdAttributeReferenceDAO</code> 
   */
  public MdAttributeReferenceDAOIF convertToReference()
  {
    MdAttributeReferenceDAO mdAttributeReferenceDAO = MdAttributeReferenceDAO.newInstance();
    
    mdAttributeReferenceDAO.replaceAttributeMap(this.getObjectState().getAttributeMap());
    
    mdAttributeReferenceDAO.getAttribute(MdAttributeReferenceInfo.REF_MD_ENTITY).setValue(this.getMdBusinessDAO().getOid());
    
    return mdAttributeReferenceDAO;
  }
  
  /**
   * This is used by the query API to allow for parent ids and child ids of relationships to
   * be used in queries.
   * 
   * Precondition: assumes this character attribute is an OID. The collection of
   * <code>AttributeDAO</code> objects do not have their containing reference updated to
   * the returned <code>MdAttributeReferenceDAO</code> 
   * 
   * @param the code>MdBusinessDAOIF</code> of the referenced type in the relationship.
   */
  public MdAttributeReferenceDAOIF convertToReference(MdBusinessDAOIF mdReferenecedBusinessDAOIF)
  {
    MdAttributeReferenceDAO mdAttributeReferenceDAO = MdAttributeReferenceDAO.newInstance();
    
    mdAttributeReferenceDAO.replaceAttributeMap(this.getObjectState().getAttributeMap());
    
    mdAttributeReferenceDAO.getAttribute(MdAttributeReferenceInfo.REF_MD_ENTITY).setValue(mdReferenecedBusinessDAOIF.getOid());
    
    return mdAttributeReferenceDAO;
  }
  
  
}