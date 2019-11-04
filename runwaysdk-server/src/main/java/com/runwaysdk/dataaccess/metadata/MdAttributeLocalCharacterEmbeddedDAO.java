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

  public void addDefaultLocale()
  {
    addDefaultLocale(this.getEmbeddedMdClassDAOIF());
  }

  protected MdAttributeDAOIF addLocaleWrapper(String attributeName, String columnName, String displayLabel, String description)
  {
    return addLocale(attributeName, columnName, displayLabel, description, this.getEmbeddedMdClassDAOIF());
  }

  public static MdAttributeCharacterDAOIF addDefaultLocale(MdClassDAOIF mdClassDAOIF)
  {
    String attributeName = MdAttributeLocalInfo.DEFAULT_LOCALE;
    String columnName = "";
    String displayLabel = "Default Locale";
    String description = "Default locale";

    return addLocale(attributeName, columnName, displayLabel, description, mdClassDAOIF);
  }

  protected static MdAttributeCharacterDAOIF addLocale(String attributeName, String columnName, String displayLabel, String description, MdClassDAOIF mdClassDAOIF)
  {
    MdAttributeCharacterDAO localChar = MdAttributeCharacterDAO.newInstance();
    localChar.getAttribute(MdAttributeCharacterInfo.NAME).setValue(attributeName);
    if (columnName.trim().length() != 0)
    {
      localChar.getAttribute(MdAttributeCharacterInfo.COLUMN_NAME).setValue(columnName);
    }
    ( (AttributeLocal) localChar.getAttribute(MdAttributeCharacterInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, displayLabel);
    ( (AttributeLocal) localChar.getAttribute(MdAttributeCharacterInfo.DESCRIPTION) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, description);
    localChar.getAttribute(MdAttributeCharacterInfo.REQUIRED).setValue(MdAttributeBooleanInfo.FALSE);
    localChar.getAttribute(MdAttributeCharacterInfo.IMMUTABLE).setValue(MdAttributeBooleanInfo.FALSE);
    localChar.getAttribute(MdAttributeCharacterInfo.SIZE).setValue(Integer.toString(MdAttributeCharacterInfo.MAX_CHARACTER_SIZE));
    localChar.getAttribute(MdAttributeCharacterInfo.GENERATE_ACCESSOR).setValue(Boolean.toString(false));
    localChar.getAttribute(MdAttributeCharacterInfo.DEFINING_MD_CLASS).setValue(mdClassDAOIF.getOid());
    localChar.apply();

    return localChar;
  }

}
