package com.runwaysdk.dataaccess.metadata;

import java.util.Locale;
import java.util.Map;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalEmbeddedDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeLocal;
import com.runwaysdk.dataaccess.metadata.graph.MdAttributeLocalEmbedded_G;

public abstract class MdAttributeLocalEmbeddedDAO extends MdAttributeEmbeddedDAO implements MdAttributeLocalEmbeddedDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -1850829906666660082L;

  public MdAttributeLocalEmbeddedDAO()
  {
    super();
  }

  public MdAttributeLocalEmbeddedDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeLocalEmbeddedDAO getBusinessDAO()
  {
    return (MdAttributeLocalEmbeddedDAO) super.getBusinessDAO();
  }

  public boolean definesLocale(Locale locale)
  {
    MdAttributeDAOIF mdAttribute = this.getEmbeddedMdClassDAOIF().definesAttribute(locale.toString());

    return ( mdAttribute != null );
  }

  public boolean definesLocale(MdDimensionDAOIF mdDimensionDAOIF, Locale locale)
  {
    MdAttributeDAOIF mdAttribute = this.getEmbeddedMdClassDAOIF().definesAttribute(mdDimensionDAOIF.getLocaleAttributeName(locale));

    return ( mdAttribute != null );
  }

  public boolean definesDefaultLocale(MdDimensionDAOIF mdDimension)
  {
    String attributeName = mdDimension.getDefaultLocaleAttributeName();
    MdAttributeDAOIF mdAttribute = this.getEmbeddedMdClassDAOIF().definesAttribute(attributeName);

    return ( mdAttribute != null );
  }

  public boolean definesDefaultLocale()
  {
    return ( this.getEmbeddedMdClassDAOIF().definesAttribute(MdAttributeLocalInfo.DEFAULT_LOCALE) != null );
  }

  public void addLocale(Locale locale)
  {
    if (!this.definesLocale(locale))
    {
      String attributeName = locale.toString();
      String columnName = locale.toString().toLowerCase();
      String displayLabel = locale.getDisplayName(locale);
      String description = this.definesAttribute() + " localized for " + locale.getDisplayName(locale);

      this.addLocaleWrapper(attributeName, columnName, displayLabel, description);
    }
  }

  /**
   * Does nothing if the locale is not defined.
   * 
   * @param locale
   */
  public void removeLocale(Locale locale)
  {
    if (this.definesLocale(locale))
    {
      MdAttributeDAOIF mdAttributeDAOIF = this.getEmbeddedMdClassDAOIF().definesAttribute(locale.toString());

      if (mdAttributeDAOIF != null)
      {
        mdAttributeDAOIF.getBusinessDAO().delete();
      }
    }
  }

  private MdAttributeDAOIF addLocaleWrapper(String attributeName, String columnName, String displayLabel, String description)
  {
    MdAttributeTextDAO localChar = MdAttributeTextDAO.newInstance();
    localChar.getAttribute(MdAttributeTextInfo.NAME).setValue(attributeName);
    if (columnName.trim().length() != 0)
    {
      localChar.getAttribute(MdAttributeTextInfo.COLUMN_NAME).setValue(columnName);
    }
    ( (AttributeLocal) localChar.getAttribute(MdAttributeTextInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, displayLabel);
    ( (AttributeLocal) localChar.getAttribute(MdAttributeTextInfo.DESCRIPTION) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, description);
    localChar.getAttribute(MdAttributeTextInfo.REQUIRED).setValue(MdAttributeBooleanInfo.FALSE);
    localChar.getAttribute(MdAttributeTextInfo.IMMUTABLE).setValue(MdAttributeBooleanInfo.FALSE);
    localChar.getAttribute(MdAttributeTextInfo.GENERATE_ACCESSOR).setValue(Boolean.toString(false));
    localChar.getAttribute(MdAttributeTextInfo.DEFINING_MD_CLASS).setValue(this.getEmbeddedMdClassDAOIF().getOid());
    localChar.apply();

    return localChar;

  }

  @Override
  protected void initializeStrategyObject()
  {
    if (this.definedByClass() instanceof MdGraphClassDAOIF)
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeLocalEmbedded_G(this));
    }
    else
    {
      throw new UnsupportedOperationException();
    }
  }
}
